# 第一章 MQ基础

MQ，中文是消息队列（MessageQueue），字面来看就是存放消息的队列。

介绍MQ之前先来了解一下微服务之间的通讯，微服务间通讯有同步和异步两种方式：

- 同步通讯：就像打电话，需要实时响应。

- 异步通讯：就像发邮件，不需要马上回复。


两种方式各有优劣，打电话可以立即得到响应，但是你却不能跟多个人同时通话。发送邮件可以同时与多个人收发邮件，但是往往响应会有延迟。

**同步通讯**

我们之前学习的Feign调用就属于同步方式，虽然调用可以实时得到结果，但存在下面的问题：
1. 耦合度高：每次加入新的需求，都要修改原来的代码。
2. 性能下降：调用者需要等待服务提供者相应，如果调用链过长则相应时间等于每次调用的时间之和。性能和吞吐能力下降。
3. 资源浪费：调用链中的每一个服务都在等待响应过程中，不能够释放请求占用的资源，高并发场景下会极度浪费系统资源。
4. 级联失败：如果服务提供者出现问题，所有调用方法都会跟着出现问题，如果多米诺骨牌一样，迅速导致整个微服务群故障。

同步调用的优点：

- 时效性较强，可以立即得到结果

**异步通讯**

异步调用则可以避免上述问题。

- 我们以购买商品为例，用户支付后需要调用订单服务完成订单状态修改，调用物流服务，从仓库分配响应的库存并准备发货。

- 在事件模式中，支付服务是事件发布者（publisher），在支付完成后只需要发布一个支付成功的事件（event），事件中带上订单id。

- 订单服务和物流服务是事件订阅者（Consumer），订阅支付成功的事件，监听到事件后完成自己业务即可。


为了解除事件发布者与订阅者之间的耦合，两者并不是直接通信，而是有一个中间人（Broker）。发布者发布事件到Broker，不关心谁来订阅事件。订阅者从Broker订阅事件，不关心谁发来的消息。

![](..\图片\5-05【RabbitMQ】\0-1异步通信.png)

Broker 是一个像数据总线一样的东西，所有的服务要接收数据和发送数据都发到这个总线上，这个总线就像协议一样，让服务间的通讯变得标准和可控。

好处：

- 耦合度极低，每个服务都可以灵活插拔，可替换
- 吞吐量提升：无需等待订阅者处理完成，响应更快速
- 调用间没有阻塞，不会造成无效的资源占用
- 故障隔离：服务没有直接调用，不存在级联失败问题
- 流量削峰：不管发布事件的流量波动多大，都由Broker接收，订阅者可以按照自己的速度去处理事件

缺点：

- 架构复杂了，业务没有明显的流程线，不好管理
- 需要依赖于Broker的可靠、安全、性能

好在现在开源软件或云平台上 Broker 的软件是非常成熟的，比较常见的一种就是我们今天要学习的MQ技术。

## 1.1 常见的MQ

MQ，中文是消息队列（MessageQueue），字面来看就是存放消息的队列。也就是事件驱动架构中的Broker。

比较常见的MQ实现：ActiveMQ、RabbitMQ、RocketMQ、Kafka。

几种常见MQ的对比：

|           |       RabbitMQ       |           ActiveMQ            |  RocketMQ  |   Kafka    |
| --------- | :------------------: | :---------------------------: | :--------: | :--------: |
| 公司/社区 |        Rabbit        |            Apache             |    阿里    |   Apache   |
| 开发语言  |        Erlang        |             Java              |    Java    | Scala&Java |
| 支持协议  | AMQP XMPP SMTP STOMP | OpenWire STOMP REST XMPP AMQP | 自定义协议 | 自定义协议 |
| 可用性    |          高          |             一般              |     高     |     高     |
| 吞吐量    |         一般         |              差               |     高     |   非常高   |
| 延迟      |        微秒级        |            毫秒级             |   毫秒级   |  毫秒以内  |
| 可靠性    |          高          |             一般              |     高     |    一般    |

对比如下：

- 追求可用性：Kafka、 RocketMQ 、RabbitMQ
- 追求可靠性：RabbitMQ、RocketMQ

- 追求吞吐能力：RocketMQ、Kafka

- 追求消息低延迟：RabbitMQ、Kafka


## 1.2 安装RabbitMQ

我们在Centos7虚拟机中使用Docker来安装。有两种安装方式：

1. 在线拉取

   ```sh
   docker pull rabbitmq:3-management
   ```

2. 从本地加载。已经在课前资料已经提供了镜像包mq.tar，上传到虚拟机`/usr/local/RabbitMQ`，使用命令加载镜像即可：

   ```sh
   docker load -i mq.tar
   ```

安装之后就执行下面的命令来运行MQ容器：

```sh
docker run \
 -e RABBITMQ_DEFAULT_USER=linxuan \
 -e RABBITMQ_DEFAULT_PASS=123321 \
 -v mq-plugins:/plugins \
 --name mq \
 --hostname mq1 \
 -p 15672:15672 \
 -p 5672:5672 \
 -d \
 rabbitmq:3.8-management
```

然后就可以在宿主机的浏览器访问了：http://192.168.66.136:15672。输入上面设置的用户名和密码就可以登录了。

MQ的基本结构：

![](..\图片\5-05【RabbitMQ】\0-2MQ.png)

RabbitMQ中的一些角色：

- publisher：生产者
- consumer：消费者
- exchange个：交换机，负责消息路由
- queue：队列，存储消息
- virtualHost：虚拟主机，隔离不同租户的exchange、queue、消息的隔离

RabbitMQ官方https://www.rabbitmq.com/，提供了5个不同的Demo示例，对应了不同的消息模型：

![image-20210717163332646](..\图片\5-05【RabbitMQ】\0-3MQ消息模型.png)

## 1.3 入门案例

课前资料提供了一个Demo工程，mq-demo，导入。

包括三部分：

- mq-demo：父工程，管理项目依赖
- publisher：消息的发送者
- consumer：消息的消费者

简单队列模式的模型图：

![image-20210717163434647](..\图片\5-05【RabbitMQ】\0-4简单队列.png)

官方的HelloWorld是基于最基础的消息队列模型来实现的，只包括三个角色：

- publisher：消息发布者，将消息发送到队列queue
- queue：消息队列，负责接受并缓存消息
- consumer：订阅队列，处理队列中的消息

**publisher实现**

```java
public class PublisherTest {
    @Test
    public void testSendMessage() throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.66.136");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("linxuan");
        factory.setPassword("123321");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 4.发送消息
        String message = "hello, rabbitmq!";
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("发送消息成功：【" + message + "】");

        // 5.关闭通道和连接
        channel.close();
        connection.close();
    }
}
```

**consumer实现**

```java
public class ConsumerTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.66.136");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("linxuan");
        factory.setPassword("123321");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 4.订阅消息
        channel.basicConsume(queueName, true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) 
                					 throws IOException {
                // 5.处理消息
                String message = new String(body);
                System.out.println("接收到消息：【" + message + "】");
            }
        });
        System.out.println("等待接收消息。。。。");
    }
}
```

<!--channel [ˈtʃænl] n:频道; 渠道; 电视台; 波段; 途径; 系统; （表达的）方式，方法，手段; 水渠; 水道; 海峡; 英吉利海峡;-->

基本消息队列的消息发送流程：

1. 建立connection

2. 创建channel

3. 利用channel声明队列

4. 利用channel向队列发送消息

基本消息队列的消息接收流程：

1. 建立connection

2. 创建channel

3. 利用channel声明队列

4. 定义consumer的消费行为handleDelivery()

5. 利用channel将消费者与队列绑定

# 第二章 SpringAMQP

AMQP，即`Advanced Message Queuing Protocol`，一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同的开发语言等条件的限制。

SpringAMQP是基于RabbitMQ封装的一套模板，并且还利用SpringBoot对其实现了自动装配，使用起来非常方便。SpringAmqp的官方地址：https://spring.io/projects/spring-amqp。

SpringAMQP提供了三个功能：

- 自动声明队列、交换机及其绑定关系 
- 基于注解的监听器模式，异步接收消息
- 封装了RabbitTemplate工具，用于发送消息 

![](..\图片\5-05【RabbitMQ】\0-5SpringAMQP.png)

## 2.1 BasicQueue基本消息队列

基本消息队列：BasicQueue。

在父工程mq-demo中引入依赖

```xml
<!--AMQP依赖，包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

**消息发送**

首先配置MQ地址，在publisher服务的`application.yml`中添加配置：

```yaml
spring:
  rabbitmq:
    host: 192.168.66.136 # 主机名
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: linxuan # 用户名
    password: 123321 # 密码
```

然后在publisher服务中编写测试类（测试类）SpringAmqpTest，并利用RabbitTemplate实现消息发送：

```java
package com.linxuan.mq.spring;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
```

**消息接收**

首先配置MQ地址，在consumer服务的application.yml中添加配置：

```yaml
spring:
  rabbitmq:
    host: 192.168.66.136 # 主机名
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: linxuan # 用户名
    password: 123321 # 密码
```

然后在consumer服务的`com.linxuan.mq.listener`包中新建一个类`SpringRabbitListener`，代码如下：

```java
package com.linxuan.mq.listener;

@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("spring 消费者接收到消息：【" + msg + "】");
    }
}
```


启动consumer服务，然后在publisher服务中运行测试代码，发送MQ消息。

## 2.2 WorkQueue工作消息队列

Work queues，也被称为（Task queues），任务模型。简单来说就是让多个消费者绑定到一个队列，共同消费队列中的消息。

![](..\图片\5-05【RabbitMQ】\0-6WorkQueue.png)

当消息处理比较耗时的时候，可能生产消息的速度会远远大于消息的消费速度。长此以往，消息就会堆积越来越多，无法及时处理。

此时就可以使用work 模型，多个消费者共同处理消息处理，速度就能大大提高了。

**消息发送**

这次我们循环发送，模拟大量消息堆积现象。

在publisher服务中的SpringAmqpTest类中添加一个测试方法：

```java
/**
     * workQueue
     * 向队列中不停发送消息，模拟消息堆积。
     */
@Test
public void testWorkQueue() throws InterruptedException {
    // 队列名称
    String queueName = "simple.queue";
    // 消息
    String message = "hello, message_";
    for (int i = 0; i < 50; i++) {
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message + i);
        Thread.sleep(20);
    }
}
```

**消息接收**

要模拟多个消费者绑定同一个队列，我们在consumer服务的SpringRabbitListener中添加2个新的方法：

```java
@RabbitListener(queues = "simple.queue")
public void listenWorkQueue1(String msg) throws InterruptedException {
    System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(20);
}

@RabbitListener(queues = "simple.queue")
public void listenWorkQueue2(String msg) throws InterruptedException {
    System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(200);
}
```

注意到这个消费者sleep了1000秒，模拟任务耗时。

**修改配置**

启动`ConsumerApplication`后，再执行publisher服务中刚刚编写的发送测试方法testWorkQueue。

可以看到消费者1很快完成了自己的25条消息。消费者2却在缓慢的处理自己的25条消息。也就是说消息是平均分配给每个消费者，并没有考虑到消费者的处理能力。这样显然是有问题的。

在spring中有一个简单的配置，可以解决这个问题。我们修改consumer服务的`application.yml`文件，添加配置：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 1 # 每次只能获取一条消息，处理完成才能获取下一个消息 默认是无限
```

这时候再分别启动`ConsumerApplication`和`testWorkQueue`发现，消费者1处理10条消息之后消费者2才会处理一条。

## 2.3 发布/订阅

发布订阅的模型如图：

![](..\图片\5-05【RabbitMQ】\0-7发布订阅.png)

可以看到，在订阅模型中，多了一个exchange角色，而且过程略有变化：

- `Publisher`：生产者，也就是要发送消息的程序，但是不再发送到队列中，而是发给X（交换机）
- `Exchange`：交换机，图中的X。一方面，接收生产者发送的消息。另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于Exchange的类型。Exchange有以下3种类型：
  1. `Fanout`：广播，将消息交给所有绑定到交换机的队列
   2. `Direct`：定向，把消息交给符合指定routing key 的队列
  3. `Topic`：通配符，把消息交给符合routing pattern（路由模式） 的队列
- `Consumer`：消费者，与以前一样，订阅队列，没有变化
- `Queue`：消息队列也与以前一样，接收消息、缓存消息。

Exchange（交换机）只负责转发消息，不具备存储消息的能力，因此如果没有任何队列与Exchange绑定，或者没有符合路由规则的队列，那么消息会丢失！

Spring提供了一个接口Exchange，来表示所有不同类型的交换机：

![](..\图片\5-05【RabbitMQ】\0-8交换机.png)

### 2.3.1 Fanout广播

Fanout，英文翻译是扇出，在MQ中叫广播更合适。

![](..\图片\5-05【RabbitMQ】\0-9Fanout.png)

在广播模式下，消息发送流程是这样的：

1.  可以有多个队列
2. 每个队列都要绑定到`Exchange`（交换机）
3. 生产者发送的消息，只能发送到交换机，交换机来决定要发给哪个队列，生产者无法决定
4. 交换机把消息发送给绑定过的所有队列
5. 订阅队列的消费者都能拿到消息

我们的计划是这样的：

- 创建一个交换机 `linxuan.fanout`，类型是`Fanout`
- 创建两个队列`fanout.queue1`和`fanout.queue2`，绑定到交换机`linxuan.fanout`

**声明队列和交换机**

在`consumer`中创建一个类，声明队列和交换机：

```java
package com.linxuan.mq.config;

@Configuration
public class FanoutConfig {
    /**
     * 声明交换机
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("linxuan.fanout");
    }

    /**
     * 第1个队列
     */
    @Bean
    public Queue fanoutQueue1(){
        return new Queue("fanout.queue1");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    /**
     * 第2个队列
     */
    @Bean
    public Queue fanoutQueue2(){
        return new Queue("fanout.queue2");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue2(Queue fanoutQueue2, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
```

定义的这些Bean都交由Spring管理，当项目启动的时候，这些Bean都会被创建。

**消息发送**

在publisher服务的SpringAmqpTest类中添加测试方法：

```java
@Test
public void testFanoutExchange() {
    // 队列名称
    String exchangeName = "linxuan.fanout";
    // 消息
    String message = "hello, everyone!";
    rabbitTemplate.convertAndSend(exchangeName, "", message);
}
```

**消息接收**

在consumer服务端添加类，作为消费者：

```java
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "fanout.queue1")
    public void fanoutListener1(String msg) {
        System.out.println("消费者1接收到Fanout消息：【" + msg + "】");
    }

    @RabbitListener(queues = "fanout.queue2")
    public void fanoutListener2(String msg) {
        System.out.println("消费者2接收到Fanout消息：【" + msg + "】");
    }
}
```

编写好之后来启动项目，首先启动consumer接受端整个项目，然后再启动publisher发送端的SpringAmqpTest中的testFanoutExchange方法，然后就可以在consumer接受端接收到信息了。

### 2.3.2 Direct定向

在Fanout模式中，一条消息，会被所有订阅的队列都消费。但是，在某些场景下，我们希望不同的消息被不同的队列消费。这时就要用到Direct类型的Exchange。

Direct交换机与Fanout交换机的差异：

- Fanout交换机将消息路由给每一个与之绑定的队列
- Direct交换机根据RoutingKey判断路由给哪个队列
- 如果多个队列具有相同的RoutingKey，则与Fanout功能类似

基于`@RabbitListener`注解声明队列和交换机有常见注解：`@Queue`、`@Exchange`。

![](..\图片\5-05【RabbitMQ】\0-10Direct.png)

 在Direct模型下：

- 队列与交换机的绑定，不能是任意绑定了，而是要指定一个`RoutingKey`（路由key）
- 消息的发送方在向 Exchange发送消息时，也必须指定消息的 `RoutingKey`。
- Exchange不再把消息交给每一个绑定的队列，而是根据消息的`Routing Key`进行判断，只有队列的`Routingkey`与消息的 `Routing key`完全一致，才会接收到消息

案例需求如下：

1. 利用`@RabbitListener`声明`Exchange`、`Queue`、`RoutingKey`。

2. 在consumer服务中，编写两个消费者方法，分别监听`direct.queue1`和`direct.queue2`。

3. 在publisher中编写测试方法，向`linxuan.direct`交换机发送消息。

**基于注解声明队列和交换机**

基于`@Bean`的方式声明队列和交换机比较麻烦，Spring还提供了基于注解方式来声明。在consumer消费端创建SpringRabbitListener类，里面声明两个方法，同时基于注解来声明队列和交换机：

```java
@Component
public class SpringRabbitListener {

    @RabbitListener(bindings = @QueueBinding(
        	value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "linxuan.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenerDirect1(String msg) {
        System.out.println("消费者接收到direct.queue1的消息：【" + msg + "】");
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
        	exchange = @Exchange(name = "linxuan.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenerDirect2(String msg) {
        System.out.println("消费者接收到direct.queue2的消息：【" + msg + "】");
    }
}
```

**消息发送**

在publisher服务的SpringAmqpTest类中添加测试方法：

```java
@Test
public void testSendDirectExchange() {
    // 交换机名称
    String exchangeName = "linxuan.direct";
    // 消息
    String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
    // 发送消息
    rabbitTemplate.convertAndSend(exchangeName, "red", message);
}
```

### 2.3.3 Topic通配符

`Topic`类型的`Exchange`与`Direct`相比，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic`类型`Exchange`可以让队列在绑定`Routing key` 的时候使用通配符！

Direct交换机与Topic交换机的差异：

- Topic交换机接收的消息`RoutingKey`必须是多个单词，以 `**.**` 分割
- Topic交换机与队列绑定时的`bindingKey`可以指定通配符
- `#`：代表0个或多个词
- `*`：代表1个词

`Routingkey` 一般都是有一个或多个单词组成，多个单词之间以”`.`”分割，例如： `item.insert`

 通配符规则：

- `#`：匹配一个或多个词
- `*`：匹配不多不少恰好1个词
- `item.#`：能够匹配`item.spu.insert` 或者 `item.spu`
- `item.*`：只能匹配`item.spu`

![image-20210717170705380](..\图片\5-05【RabbitMQ】\0-11Topic.png)

解释：

- `Queue1`：绑定的是`china.#` ，因此凡是以 `china.`开头的`routing key` 都会被匹配到。包括china.news和china.weather
- `Queue2`：绑定的是`#.news` ，因此凡是以 `.news`结尾的 `routing key` 都会被匹配。包括china.news和japan.news

案例需求如下：

1. 并利用`@RabbitListener`声明`Exchange`、`Queue`、`RoutingKey`

2. 在`consumer`服务中，编写两个消费者方法，分别监听`topic.queue1`和`topic.queue2`

3. 在`publisher`中编写测试方法，向`linxuan. topic`发送消息

**消息发送**

在publisher服务的SpringAmqpTest类中添加测试方法：

```java
/**
     * topicExchange
     */
@Test
public void testSendTopicExchange() {
    // 交换机名称
    String exchangeName = "linxuan.topic";
    // 消息
    String message = "喜报！孙悟空大战哥斯拉，胜!";
    // 发送消息
    rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
}
```

**消息接收**

在`consumer`服务的`SpringRabbitListener`中添加方法：

```java
@Component
public class SpringRabbitListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "linxuan.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenerTopic1(String msg) {
        System.out.println("消费者接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "linxuan.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenerTopic2(String msg) {
        System.out.println("消费者接收到direct.queue2的消息：【" + msg + "】");
    }
}
```

## 2.4 消息转换器

之前说过，Spring会把你发送的消息序列化为字节发送给MQ，接收消息的时候，还会把字节反序列化为Java对象。

![](..\图片\5-05【RabbitMQ】\0-12消息转换器.png)

只不过，默认情况下Spring采用的序列化方式是JDK序列化。众所周知，JDK序列化存在下列问题：数据体积过大、有安全漏洞、可读性差。下面我们来测试一下。

**测试默认转换器**

停止consumer服务，让我们发送消费不被消费。我们修改消息发送的代码，发送一个Map对象：

```java
@Test
public void testSendMap() throws InterruptedException {
    // 准备消息
    Map<String,Object> msg = new HashMap<>();
    msg.put("name", "Jack");
    msg.put("age", 21);
    // 发送消息
    rabbitTemplate.convertAndSend("simple.queue","", msg);
}
```

发送消息后查看控制台：

![](..\图片\5-05【RabbitMQ】\0-13测试消息转换器.png)

**配置JSON转换器**

显然，JDK序列化方式并不合适。我们希望消息体的体积更小、可读性更高，因此可以使用JSON方式来做序列化和反序列化。

在publisher和consumer两个服务中都引入依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.9.10</version>
</dependency>
```

配置消息转换器。在启动类中添加一个Bean即可：

```java
@Bean
public MessageConverter jsonMessageConverter(){
    return new Jackson2JsonMessageConverter();
}
```

------

消息队列在使用过程中，面临着很多实际问题需要思考：

* 消息可靠性问题。确保发送的消息至少被消费一次。
* 延迟消息问题。实现消息的延迟投递。
* 高可用问题。避免单点的MQ故障而导致的不可用的问题。
* 消息堆积问题。解决数百万消息堆积，无法及时消费的问题。

# 第三章 消息可靠性

接下来我们首先来看一个消息的可靠性。

消息从发送，到消费者接收，会经历多个过程：

![](..\图片\5-05【RabbitMQ】\0-7发布订阅.png)

其中的每一步都可能导致消息丢失，常见的丢失原因包括：

- 发送时丢失：生产者发送的消息未送达exchange、消息到达exchange后未到达queue。
- MQ宕机，queue将消息丢失
- consumer接收到消息后未消费就宕机

针对这些问题，RabbitMQ分别给出了解决方案：

- 开启生产者确认机制，确保生产者的消息能到达队列
- 开启持久化功能，确保消息未消费前在队列中不会丢失
- 开启消费者确认机制为auto，由spring确认消息处理成功后完成ack
- 开启消费者失败重试机制，并设置MessageRecoverer，多次重试失败后将消息投递到异常交换机，交由人工处理

下面我们就通过案例来演示每一个步骤。首先，导入课前资料提供的demo工程，项目结构如下：

```apl
mq-advanced-demo 
    |-- idea
    |-- consumer # 项目-模块 消息的消费者
    |-- publisher # 项目-模块 消息的发送者
    |-- src
    |-- pom.xml
```

## 3.1 生产者消息确认

<!--ack acknowledge 承认(权威、地位); 告知收悉; （微笑、挥手等）致意; （公开）感谢; -->

RabbitMQ提供了`publisher confirm`机制来避免消息发送到MQ过程中丢失。这种机制必须给每个消息指定一个唯一ID。消息发送到MQ以后，会返回一个结果给发送者，表示消息是否处理成功。

返回结果有两种方式：

- `publisher-confirm`，发送者确认。
  - 消息成功投递到交换机，返回ack。
  - 消息未投递到交换机，返回nack。
  - 消息发送过程中出现异常，没有收到回执。
- `publisher-return`，发送者回执。
  - 消息投递到交换机了，但是没有路由到队列。返回ACK，及路由失败原因。调用ReturnCallBack。

![](..\图片\5-05【RabbitMQ】\1-1.png)

注意：确认机制发送消息的时候，需要给每一个消息设置一个全局唯一的ID，用来区分不同的消息，避免ack冲突。

步骤如下：修改配置、定义Return回调、定义ConfirmCallback。接下来详细介绍一下：

**修改配置**

首先，修改publisher服务中的`application.yml`文件，添加下面的内容：

```yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated
    publisher-returns: true
    template:
      mandatory: true
```

- `publish-confirm-type`：开启`publisher-confirm`，这里支持两种类型：
  - `simple`：同步等待confirm结果，直到超时
  - `correlated`：异步回调，定义ConfirmCallback，MQ返回结果时会回调这个ConfirmCallback
- `publish-returns`：开启`publish-return`功能，同样是基于callback机制，不过是定义ReturnCallback
- `template.mandatory`：定义消息路由失败时的策略。true，则调用ReturnCallback；false：则直接丢弃消息。

**定义Return回调**

每个RabbitTemplate只能配置一个ReturnCallback，因此需要在项目加载时配置。修改publisher服务，添加一个：

```java
package com.linxuan.mq.config;

@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 设置ReturnCallback
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            // 投递失败，记录日志
            log.error("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                     replyCode, replyText, exchange, routingKey, message.toString());
            // 如果有业务需要，可以重发消息
        });
    }
}
```

**定义ConfirmCallback**

ConfirmCallback可以在发送消息时指定，因为每个业务处理confirm成功或失败的逻辑不一定相同。在publisher服务的`com.linxuan.mq.spring.SpringAmqpTest`类中，定义一个单元测试方法：

```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException {
        String routingKey = "simple";
        // 1.消息体
        String message = "hello, spring amqp!";

        // 2.全局唯一的消息ID，需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        // 3.添加callback
        correlationData.getFuture().addCallback(
            result -> {
                if (result.isAck()) {
                    // 3.1.ack，消息成功
                    log.debug("消息发送成功, ID:{}", correlationData.getId());
                } else {
                    // 3.2.nack，消息失败
                    log.error("失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
                }
            },
            ex -> {
                log.error("消息发送异常, ID:{}, 原因{}", correlationData.getId(), ex.getMessage());
            }
        );

        // 4.发送消息
        rabbitTemplate.convertAndSend("amq.topic", routingKey, message, correlationData);
    }
}
```

交换机如下：

![](..\图片\5-05【RabbitMQ】\1-3交换机.png)

将交换机与队列绑定

![](..\图片\5-05【RabbitMQ】\1-4交换机绑定.png)

上面代码运行之后就会将消息发送成功：`消息发送成功, ID:e258d4f2-2805-4d7d-b8fa-ff7c7e172a7e`

![](..\图片\5-05【RabbitMQ】\1-2消息发送成功.png)

接下来演示一下消息发送错误的情况：

* 消息未投送到交换机，返回nack。这种情况，我们直接修改消息发送时候交换机的名称就可以了，这样找不到交换机，消息肯定投送不了。

  ```Java
  // 4.发送消息 修改交换机名称 导致无法发送消息
  rabbitTemplate.convertAndSend("aamq.topic", routingKey, message, correlationData);
  ```

  ```apl
  17:51:16:567 ERROR 24732 --- [nectionFactory1] cn.itcast.mq.spring.SpringAmqpTest       : 失败, ID:bde67a65-1416-42c8-b892-e7716ee1474d, 原因channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'aamq.topic' in vhost '/', class-id=60, method-id=40)
  ```

* 消息投递到交换机了，但是没有路由到队列。返回ACK，及路由失败原因。这种情况有很多种，例如路由队列的时候MQ给挂了，这样就凉了，但是这种情况我们无法演示。

  所以修改一下Rouring Key，这样找不到队列，同样失败。记得把上面修改的交换机的名称恢复了！

  ```java
  // 修改一下RoutingKey名称 这样找不到队列 失败
  String routingKey = "linxuan.simple";
  ```

  ```apl
  17:56:02:046 DEBUG 11948 --- [168.66.136:5672] cn.itcast.mq.spring.SpringAmqpTest       : 消息发送成功, ID:a2f664e9-f3df-4b49-a3df-db2b76eef622
  17:56:02:046  INFO 11948 --- [nectionFactory1] cn.itcast.mq.config.CommonConfig         : 消息发送失败，应答码312， 原因NO_ROUTE， 交换机amq.topic， 路由键linxuan.simple，消息(Body:'hello, spring amqp!' MessageProperties [headers={spring_returned_message_correlation=a2f664e9-f3df-4b49-a3df-db2b76eef622}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0])
  ```

## 3.2 消息持久化

生产者确认可以确保消息投递到RabbitMQ的队列中，但是消息发送到RabbitMQ以后，如果突然宕机，也可能导致消息丢失。

我们可以模拟一下MQ的宕机，前面我们已经创建了一个`simple.queue`的队列，并且发送了一条消息。接下来我们重启一下MQ：`docker restart mq`。可以发现，队列和消息都没有了！但是系统创建的交换机仍然存在：

![](..\图片\5-05【RabbitMQ】\1-5交换机存在.png)

可以看到交换机上面有一个`Features`，`Features`有一个属性为`durable -> D`，该属性的值为`true`。

<!--Features 特色; 特征; 特点; 面容的一部分(如鼻、口、眼); (报章、电视等的)特写，专题节目;-->

<!--durable 耐用的; 持久的;-->

要想确保消息在RabbitMQ中安全保存，必须开启消息持久化机制。有三个：交换机持久化、队列持久化、消息持久化。

**交换机持久化**

RabbitMQ中交换机默认是非持久化的，mq重启后就丢失。SpringAMQP中可以通过代码指定交换机持久化：

```java
@Bean
public DirectExchange simpleExchange(){
    // 三个参数：交换机名称、是否持久化、当没有queue与其绑定时是否自动删除
    return new DirectExchange("simple.direct", true, false);
}
```

事实上，默认情况下，由SpringAMQP声明的交换机都是持久化的。

```java
public class DirectExchange extends AbstractExchange {
    public static final DirectExchange DEFAULT = new DirectExchange("");

    public DirectExchange(String name) {
        super(name);
    }

    public DirectExchange(String name, boolean durable, boolean autoDelete) {
        super(name, durable, autoDelete);
    }

    public final String getType() {
        return "direct";
    }
}
```

```java
public abstract class AbstractExchange extends AbstractDeclarable implements Exchange {
    private final String name;
    private final boolean durable;
    private final boolean autoDelete;
    private boolean delayed;
    private boolean internal;

    public AbstractExchange(String name) {
        this(name, true, false);
    }
}
```

可以在RabbitMQ控制台看到持久化的交换机都会带上`D`的标示：

![](..\图片\5-05【RabbitMQ】\2-1持久化交换机.png)

**队列持久化**

RabbitMQ中队列默认是非持久化的，mq重启后就丢失。SpringAMQP中可以通过代码指定交换机持久化：

```java
@Bean
public Queue simpleQueue(){
    // 使用QueueBuilder构建队列，durable就是持久化的
    return QueueBuilder.durable("simple.queue").build();
}
```

事实上，默认情况下，由SpringAMQP声明的队列都是持久化的。可以在RabbitMQ控制台看到持久化的队列都会带上`D`的标示：

![](..\图片\5-05【RabbitMQ】\2-1队列持久化.png)

**消息持久化**

在上面的环境中，我们即时重启mq容器，也不会将simple.direct交换机和simple.queue队列删除，因为已经将他们持久化了。在shell控制台键入命令：`docker restart mq`，重启mq容器，不会删除simple.direct交换机和simple.queue队列。

这时我们在Rabbit MQ中发送一条消息：

![](..\图片\5-05【RabbitMQ】\2-2发送消息.png)

![](..\图片\5-05【RabbitMQ】\2-3显示消息.png)

可是这个时候我们再重启mq容器，那么消息便会消失了。这是因为我们只持久化了交换机和队列，并没有持久化消息。

![](..\图片\5-05【RabbitMQ】\2-4消息为空.png)

这个时候我们就要持久化消息了。利用SpringAMQP发送消息时，可以设置消息的属性（MessageProperties），指定delivery-mode。

在publisher服务的`com.linxuan.mq.spring.SpringAmqpTest`类中，定义一个单元测试方法：

```java
@Test
public void testDurableMessage() {
    // 创建消息 设置消息持久化
    Message message = MessageBuilder
        .withBody("hello, RabbitMQ".getBytes(StandardCharsets.UTF_8))
        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
        .build();
    // 消息ID 需要封装到CorrelationData中
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    // 发送消息
    rabbitTemplate.convertAndSend("simple.queue", message, correlationData);
    // 记录日志
    log.debug("发送消息成功");
}
```

这样发送的消息就是持久化了。默认情况下，SpringAMQP发出的任何消息都是持久化的，不用特意指定。

## 3.3 消费者消息确认

RabbitMQ是阅后即焚机制，当发送的消息被消费者消费后RabbitMQ会将消息立刻删除。而RabbitMQ是通过消费者回执来确认消费者是否成功处理消息的，消费者获取消息后，应该向RabbitMQ发送ACK回执，表明自己已经处理消息。

设想这样的场景：RabbitMQ投递消息给消费者，消费者获取消息后返回ACK给RabbitMQ，RabbitMQ删除消息，消费者宕机，消息尚未处理。这样，消息就丢失了。因此消费者返回ACK的时机非常重要。

而SpringAMQP则允许配置三种确认模式：

- `manual`：手动ack，需要在业务代码结束后，调用api发送ack。自己根据业务情况，判断什么时候该ack，这种有代码侵入。
- `auto`：自动ack，由spring监测listener代码是否出现异常，没有异常则返回ack；抛出异常则返回nack。类似事务机制。
- `none`：关闭ack，MQ假定消费者获取消息后会成功处理，因此消息投递后立即被删除。消息投递是不可靠的，可能丢失

一般，我们都是使用默认的auto即可。

在消费者模块的yml文件配置，因为这是消费情况，消费者消息确认。如下：

```yml
spring:
  rabbitmq:
    host: 192.168.66.136 # rabbitMQ的ip地址
    port: 5672 # 端口
    username: linxuan
    password: 123321
    virtual-host: /
    listener:
      simple:
        prefetch: 1
        acknowledge-mode: auto # none关闭ack，manual手动ack，auto自动ack
```

**演示none模式**

`none`：关闭ack，MQ假定消费者获取消息后会成功处理，因此消息投递后立即被删除。消息投递是不可靠的，可能丢失。

修改consumer服务的application.yml文件，添加下面内容：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: none # 关闭ack
```

RabbitMQ中已经有一条消息了：

![](..\图片\5-05【RabbitMQ】\2-5RabbitMQ消息.png)

修改consumer服务的`SpringRabbitListener`类中的方法，模拟一个消息处理异常：

```java
@RabbitListener(queues = "simple.queue")
public void listenSimpleQueue(String msg) {
    log.info("消费者接收到simple.queue的消息：【{}】", msg);
    // 模拟异常
    System.out.println(1 / 0);
    log.debug("消息处理完成！");
}
```

测试可以发现，当消息处理抛异常时，消息依然被RabbitMQ删除了。

![](..\图片\5-05【RabbitMQ】\2-6消息被消费了.png)

**演示auto模式**

`auto`：自动ack，由spring监测listener代码是否出现异常，没有异常则返回ack；抛出异常则返回nack。类似事务机制。

再次把确认机制修改为auto：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: auto # 关闭ack
```

在异常位置打断点，再次发送消息，程序卡在断点时，可以发现此时消息状态为unack（未确定状态）：

![](..\图片\5-05【RabbitMQ】\2-7消息unack.png)

抛出异常后，因为Spring会自动返回nack，所以消息恢复至Ready状态，并且没有被RabbitMQ删除：

![](..\图片\5-05【RabbitMQ】\2-8消息恢复Ready.png)

但是当消费者出现异常后，消息会不断requeue（重入队）到队列，再重新发送给消费者，然后再次异常，再次requeue，无限循环。我们即时将打的端点放行，那么又会回来，这是因为RabbitMQ重新发送消息了！

## 3.4 消费失败重试机制

当消费者出现异常后，消息会不断requeue（重入队）到队列，再重新发送给消费者，然后再次异常，再次requeue，无限循环，导致mq的消息处理飙升，带来不必要的压力：

![](..\图片\5-05【RabbitMQ】\2-9消费者失败重试.png)

**本地重试**

我们可以利用Spring的retry机制，在消费者出现异常时利用本地重试，而不是无限制的requeue到mq队列。

修改consumer服务的application.yml文件，添加内容：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        retry:
          enabled: true # 开启消费者失败重试
          initial-interval: 1000 # 初始的失败等待时长为1秒
          multiplier: 1 # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
          max-attempts: 3 # 最大重试次数
          stateless: true # true无状态；false有状态。如果业务中包含事务，这里改为false
```

重启consumer服务，重复之前的测试。可以发现：

- 在重试3次后，SpringAMQP会抛出异常`AmqpRejectAndDontRequeueException`，说明本地重试触发了。
- 查看RabbitMQ控制台，发现消息被删除了，说明最后SpringAMQP返回的是ack，mq删除消息了。

```nginx
14:17:32:120 DEBUG 17080 --- [ntContainer#0-1] c.i.mq.listener.SpringRabbitListener     : 消费者接收到simple.queue的消息：【Hello Linxuan】
14:17:33:122 DEBUG 17080 --- [ntContainer#0-1] c.i.mq.listener.SpringRabbitListener     : 消费者接收到simple.queue的消息：【Hello Linxuan】
14:17:34:126 DEBUG 17080 --- [ntContainer#0-1] c.i.mq.listener.SpringRabbitListener     : 消费者接收到simple.queue的消息：【Hello Linxuan】
14:17:34:138  WARN 17080 --- [ntContainer#0-1] o.s.a.r.r.RejectAndDontRequeueRecoverer  : Retries exhausted for message (Body:'[B@be7d0e3(byte[13])' MessageProperties [headers={}, contentLength=0, receivedDeliveryMode=NON_PERSISTENT, redelivered=true, receivedExchange=, receivedRoutingKey=simple.queue, deliveryTag=1, consumerTag=amq.ctag-7js-gkvNiLPKoRnw3ILllA, consumerQueue=simple.queue])
```

开启本地重试时，消息处理过程中抛出异常，不会requeue到队列，而是在消费者本地重试。重试达到最大次数后，Spring会返回ack，消息会被丢弃

**失败策略**

在之前的测试中，达到最大重试次数后，消息会被丢弃，这是由Spring内部机制决定的。

在开启重试模式后，重试次数耗尽，如果消息依然失败，则需要有`MessageRecoverer`接口来处理，它包含三种不同的实现：

- `RejectAndDontRequeueRecoverer`：重试耗尽后，直接reject，丢弃消息。默认就是这种方式

- `ImmediateRequeueMessageRecoverer`：重试耗尽后，返回nack，消息重新入队

- `RepublishMessageRecoverer`：重试耗尽后，将失败消息投递到指定的交换机

比较优雅的一种处理方案是`RepublishMessageRecoverer`，失败后将消息投递到一个指定的，专门存放异常消息的队列，后续由人工集中处理。

![](..\图片\5-05【RabbitMQ】\2-10RepublishMessageRecoverer.png)

实现方式如下：

1. 在consumer服务中定义处理失败消息的交换机和队列

   ```java
   @Bean
   public DirectExchange errorMessageExchange(){
       return new DirectExchange("error.direct");
   }
   @Bean
   public Queue errorQueue(){
       return new Queue("error.queue", true);
   }
   @Bean
   public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange){
       return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with("error");
   }
   ```

2. 定义一个`RepublishMessageRecoverer`，关联队列和交换机

   ```java
   @Bean
   public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate){
       return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
   }
   ```

完整代码：

```java
package com.linxuan.mq.config;

@Configuration
public class ErrorMessageConfig {
    @Bean
    public DirectExchange errorMessageExchange(){
        return new DirectExchange("error.direct");
    }
    @Bean
    public Queue errorQueue(){
        return new Queue("error.queue", true);
    }
    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange){
        return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with("error");
    }

    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
```

在`simple.queue`队列中发送消息，过一会就会转发到`error.direct`，然后发送到`error.queue`。

# 第四章 死信交换机

当一个队列中的消息满足下列情况之一时，可以称为死信（dead letter）：

- 消费者使用`basic.reject`或 `basic.nack`声明消费失败，并且消息的requeue参数设置为false
- 消息是一个过期消息，超时无人消费
- 要投递的队列消息满了，无法投递

如果这个包含死信的队列配置了`dead-letter-exchange`属性，指定了一个交换机，那么队列中的死信就会投递到这个交换机中，而这个交换机称为死信交换机（Dead Letter Exchange，检查DLX）。另外也需要给队列设置`dead-letter-routing-key`属性，设置死信交换机与死信队列的RoutingKey。

死信交换机的作用

* 和消息失败重试机制中的RepublishMessageRecoverer一样做消息的异常兜底方案。
* 处理一些超时的消息

如图，一个消息被消费者拒绝了，变成了死信：

![](..\图片\5-05【RabbitMQ】\4-1死信.png)

因为`simple.queue`绑定了死信交换机 dl.direct，因此死信会投递给这个交换机。如果这个死信交换机也绑定了一个队列，则消息最终会进入这个存放死信的队列：

![](..\图片\5-05【RabbitMQ】\4-2进入死信队列.png)

另外，队列将死信投递给死信交换机时，必须知道两个信息：死信交换机名称和死信交换机与死信队列绑定的RoutingKey。这样才能确保投递的消息能到达死信交换机，并且正确的路由到死信队列。

![](..\图片\5-05【RabbitMQ】\4-3死信信息.png)

可以利用死信交换机收集所有消费者处理失败的消息（死信），交由人工处理，进一步提高消息队列的可靠性。

**消费失败重试机制RepublishMessageRecoverer策略和死信交换机和区别**

失败消息的投递对象不同：

对于消费失败重试机制中的RepublishMessageRecoverer策略而言是由消费者投递的：

![](..\图片\5-05【RabbitMQ】\2-10RepublishMessageRecoverer.png)

而对于死信交换机中是由queue队列投递消息的：

![](..\图片\5-05【RabbitMQ】\4-2进入死信队列.png)

## 4.1 利用死信交换机接收死信

在失败重试策略中，默认的`RejectAndDontRequeueRecoverer`会在本地重试次数耗尽后，发送reject给RabbitMQ，消息变成死信，被丢弃。

我们可以给`simple.queue`添加一个死信交换机，给死信交换机绑定一个队列。这样消息变成死信后也不会丢弃，而是最终投递到死信交换机，路由到与死信交换机绑定的队列。

![](..\图片\5-05【RabbitMQ】\4-2进入死信队列.png)

我们在consumer服务中，定义一组死信交换机、死信队列：

```java
// 声明普通的 simple.queue队列，并且为其指定死信交换机：dl.direct
@Bean
public Queue simpleQueue2(){
    return QueueBuilder.durable("simple.queue") // 指定队列名称，并持久化
        .deadLetterExchange("dl.direct") // 指定死信交换机
        .build();
}
// 声明死信交换机 dl.direct
@Bean
public DirectExchange dlExchange(){
    return new DirectExchange("dl.direct", true, false);
}
// 声明存储死信的队列 dl.queue
@Bean
public Queue dlQueue(){
    return new Queue("dl.queue", true);
}
// 将死信队列与死信交换机绑定
@Bean
public Binding dlBinding(){
    return BindingBuilder.bind(dlQueue()).to(dlExchange()).with("simple");
}
```

## 4.2 TTL驻留时间

TTL，也就是Time-To-Live。如果一个队列中的消息如果超时未消费，则会变为死信，超时分为两种情况：

- 消息所在的队列设置了超时时间
- 消息本身设置了超时时间

![](..\图片\5-05【RabbitMQ】\4-4TTL.png)

**接收超时死信的死信交换机**

在consumer服务的`SpringRabbitListener`中，定义一个新的消费者，并且声明死信交换机、死信队列：

```java
@Component
@Slf4j
public class SpringRabbitListener {
    
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dl.queue", durable = "true"),
            exchange = @Exchange(name = "dl.direct"),
            key = "dl"
    ))
    public void listenTtlQueue(String msg) {
        log.debug("消费者接收到dl.queue的消息：【" + msg + "】");
    }
}
```

**声明一个队列，并且指定TTL**

要给队列设置超时时间，需要在声明队列时配置`x-message-ttl`属性。声明交换机，将ttl与交换机绑定。

```java
@Configuration
public class TTLMessageConfig {

    // 创建交换机
    @Bean
    public DirectExchange ttlDirect() {
        return new DirectExchange("ttl.direct");
    }

    // 创建队列
    @Bean
    public Queue ttlQueue() {
        return QueueBuilder
                .durable("ttl.queue") // 指定队列名称，并持久化
                .ttl(10000) // 设置队列的超时时间，10秒
                .deadLetterExchange("dl.direct") // 指定死信交换机
                .deadLetterRoutingKey("dl") //指定死信交换机的RoutingKey
                .build();
    }

    // 将交换机与队列绑定
    @Bean
    public Binding ttlBinding() {
        return BindingBuilder.bind(ttlQueue()).to(ttlDirect()).with("ttl");
    }
}
```

**发送消息，但是不要指定TTL**

在发送者的测试类中发送消息：

```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Test
    public void testTTLMessage() {
        // 创建消息
        Message message = MessageBuilder
                .withBody("hello, TTL Message".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        // 消息ID 需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 发送消息
        rabbitTemplate.convertAndSend("ttl.direct","ttl", message, correlationData);
        // 记录日志
        log.info("发送消息成功");
    }
}
```

发送消息的日志：

```java
13:36:34:456  INFO 11384 --- [           main] cn.itcast.mq.spring.SpringAmqpTest       : 发送消息成功
```

查看下接收消息的日志：

```java
13:36:44:464 DEBUG 14368 --- [ntContainer#1-1] c.i.mq.listener.SpringRabbitListener     : 消费者接收到dl.queue的消息：【hello, TTL Message】
```

因为队列的TTL值是10000ms，也就是10秒。可以看到消息发送与接收之间的时差刚好是10秒。

**发送消息时，设定TTL**

在发送消息时，也可以指定TTL：

```java
@Test
public void testTTLMsg() {
    // 创建消息
    Message message = MessageBuilder
        .withBody("hello, ttl message".getBytes(StandardCharsets.UTF_8))
        .setExpiration("5000")
        .build();
    // 消息ID，需要封装到CorrelationData中
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    // 发送消息
    rabbitTemplate.convertAndSend("ttl.direct", "ttl", message, correlationData);
    log.debug("发送消息成功");
}
```

查看发送消息日志：

```java
13:42:29:831  INFO 25764 --- [           main] cn.itcast.mq.spring.SpringAmqpTest       : 发送消息成功
```

接收消息日志：

```java
13:42:34:838 DEBUG 14368 --- [ntContainer#1-1] c.i.mq.listener.SpringRabbitListener     : 消费者接收到dl.queue的消息：【hello, TTL Message】
```

这次，发送与接收的延迟只有5秒。说明当队列、消息都设置了TTL时，任意一个到期就会成为死信。

**总结**

消息超时的两种方式：

- 给队列设置ttl属性，进入队列后超过ttl时间的消息变为死信
- 给消息设置ttl属性，队列接收到消息超过ttl时间后变为死信

如何实现发送一个消息20秒后消费者才收到消息？

- 给消息的目标队列指定死信交换机
- 将消费者监听的队列绑定到死信交换机
- 发送消息时给消息设置超时时间为20秒

## 4.3 延迟队列

利用TTL结合死信交换机，我们实现了消息发出后，消费者延迟收到消息的效果。这种消息模式就称为延迟队列（Delay Queue）模式。

延迟队列的使用场景包括：

- 延迟发送短信
- 用户下单，如果用户在15 分钟内未支付，则自动取消
- 预约工作会议，20分钟后自动通知所有参会人员

因为延迟队列的需求非常多，所以RabbitMQ的官方也推出了一个插件，原生支持延迟队列效果。这个插件就是`DelayExchange`插件。参考RabbitMQ的插件列表页面：https://www.rabbitmq.com/community-plugins.html

使用方式可以参考官网地址：https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq

**安装DelayExchange插件**

上述文档是基于linux原生安装RabbitMQ，然后安装插件。因为我们之前是基于Docker安装RabbitMQ，所以下面我们会讲解基于Docker来安装RabbitMQ插件。

RabbitMQ有一个官方的插件社区，地址为：https://www.rabbitmq.com/community-plugins.html，其中包含各种各样的插件，包括我们要使用的DelayExchange插件。我们可以去对应的GitHub页面下载3.8.9版本的插件，地址为https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/tag/3.8.9这个对应RabbitMQ的3.8.5以上版本。课前资料也提供了下载好的插件。

因为我们是基于Docker安装，所以需要先查看RabbitMQ的插件目录对应的数据卷。

我们之前设定的RabbitMQ的数据卷名称为`mq-plugins`，所以我们使用下面命令查看数据卷：

```sh
docker volume inspect mq-plugins
```

可以得到下面结果：

```sh
[root@localhost ~]# docker volume inspect mq-plugins
[
    {
        "CreatedAt": "2022-10-30T14:45:58+08:00",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/mq-plugins/_data",
        "Name": "mq-plugins",
        "Options": null,
        "Scope": "local"
    }
]
```

接下来，将插件上传到这个`/var/lib/docker/volumes/mq-plugins/_data`目录即可。

最后就是安装了，需要进入MQ容器内部来执行安装。我们的容器名为`mq`，所以执行下面命令：

```sh
docker exec -it mq bash
```

进入容器内部后，执行下面命令开启插件：

```sh
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

结果如下：

```sh
[root@localhost ~]# docker exec -it mq bash
root@mq1:/# rabbitmq-plugins enable rabbitmq_delayed_message_exchange
Enabling plugins on node rabbit@mq1:
rabbitmq_delayed_message_exchange
The following plugins have been configured:
  rabbitmq_delayed_message_exchange
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_prometheus
  rabbitmq_web_dispatch
Applying plugin configuration to rabbit@mq1...
The following plugins have been enabled: # OK了
  rabbitmq_delayed_message_exchange

started 1 plugins.
```

**DelayExchange原理**

DelayExchange需要将一个交换机声明为delayed类型。当我们发送消息到delayExchange时，流程如下：

- 接收消息
- 判断消息是否具备x-delay属性
- 如果有x-delay属性，说明是延迟消息，持久化到硬盘，读取x-delay值，作为延迟时间
- 返回routing not found结果给消息发送者
- x-delay时间到期后，重新投递消息到指定队列

**使用DelayExchange**

插件的使用也非常简单：声明一个交换机，交换机的类型可以是任意类型，只需要设定delayed属性为true即可，然后声明队列与其绑定即可。

1. 声明DelayExchange交换机

   基于注解方式（推荐）：

   ```java
   @Component
   @Slf4j
   public class SpringRabbitListener {
       
   	@RabbitListener(bindings = @QueueBinding(
               value = @Queue(name = "delay.queue", durable = "true"),
               exchange = @Exchange(name = "delay.direct", delayed = "true"), // 增加延迟属性
               key = "delay"
       ))
       public void listenDelayQueue(String msg) {
           log.debug("消费者接收到dl.queue的消息：【" + msg + "】");
       }
   }
   ```

   也可以基于`@Bean`的方式：

   ```java
   @Configuration
   public class delayMessageConfig {
   
       // 创建交换机
       @Bean
       public DirectExchange delayedExchange() {
           return ExchangeBuilder
               .directExchange("delay.direct")
               .delayed() // 设置delay属性为true
               .durable(true)
               .build();
       }
   
       @Bean
       public Queue delayerQueue() {
           return new Queue("delay.queue");
       }
   
       // 将交换机与队列绑定
       @Bean
       public Binding delayedBinding() {
           return BindingBuilder.bind(delayerQueue()).to(delayedExchange()).with("delay");
       }
   }
   ```

2. 发送消息

   发送消息时，一定要携带x-delay属性，指定延迟的时间：

   ```java
   @Test
   public void testDelayedMsg() {
       // 创建消息
       Message message = MessageBuilder
           .withBody("hello, delayed message".getBytes(StandardCharsets.UTF_8))
           .setHeader("x-delay", 10000) // 携带x-delay属性，指定延迟的时间
           .build();
       // 消息ID，需要封装到CorrelationData中
       CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
       // 发送消息
       rabbitTemplate.convertAndSend("delay.direct", "delay", message, correlationData);
       log.debug("发送消息成功");
   }
   ```

结果如下：

```java
// 单元测试
20:13:28:246 DEBUG 12008 --- [           main] cn.itcast.mq.spring.SpringAmqpTest       : 发送消息成功
20:13:28:250  INFO 12008 --- [nectionFactory1] cn.itcast.mq.config.CommonConfig         : 消息发送失败，应答码312， 原因NO_ROUTE， 交换机delay.direct， 路由键delay，消息(Body:'[B@4ea68274(byte[22])' MessageProperties [headers={spring_returned_message_correlation=7f39ba60-1904-4c15-bdc7-12b98c26760b}, contentType=application/octet-stream, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, receivedDelay=10000, deliveryTag=0])
```

```java
// consumer微服务
20:13:38:269 DEBUG 11492 --- [ntContainer#0-1] c.i.mq.listener.SpringRabbitListener     : 消费者接收到dl.queue的消息：【hello, delayed message】
```

可以看到10秒之后确实收到消息了，单元测试也绿了，但是单元测试报了一个错误，消息发送失败了！可是我们消息明明是发送成功了呀。错误是哪里来的呢？

我们想一下，之前是在发送端定义过这么一段代码：

```java
package com.linxuan.mq.config;

@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 设置ReturnCallback
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            // 投递失败，记录日志
            log.error("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                     replyCode, replyText, exchange, routingKey, message.toString());
            // 如果有业务需要，可以重发消息
        });
    }
}
```

这是定义Retuen回调，如果消息无法到达队列那么直接返回错误！

我们想想延迟交换机的特点，消息来之后先不发送消息，首先保存到磁盘上面，等时间足够了然后转发消息。正是因为没有立即转发消息，导致队列没有接收到，所以报错了！

解决方式就是增加一条判断语句：

```java
@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routinKey) -> {
            // 判断是否是延迟消息
            if (message.getMessageProperties().getReceivedDelay() > 0) {
                // 是延迟消息 那么直接忽略错误提示 直接返回
                return;
            }
            // 投递失败 记录日志
            log.info("消息发送失败，应答码{}， 原因{}， 交换机{}， 路由键{}，消息{}",
                    replyCode, replyText, exchange, routinKey, message.toString());
            // 如果有业务需要 可以重新发送消息
        });
    }
}
```

这样就不会有任何的错误了。

# 第五章 惰性队列

## 5.1 消息堆积问题

当生产者发送消息的速度超过了消费者处理消息的速度，就会导致队列中的消息堆积，直到队列存储消息达到上限。之后发送的消息就会成为死信，可能会被丢弃，这就是消息堆积问题。

![](..\图片\5-05【RabbitMQ】\5-1消息堆积.png)

解决消息堆积有两种思路：

- 增加更多消费者，提高消费速度。也就是我们之前说的`work queue`模式。
- 扩大队列容积，提高堆积上限

要提升队列容积，把消息保存在内存中显然是不行的。这时候我们就可以用到惰性队列了！

## 5.2 惰性队列

从RabbitMQ的3.6.0版本开始，就增加了Lazy Queues的概念，也就是惰性队列。惰性队列的特征如下：

- 接收到消息后直接存入磁盘而非内存
- 消费者要消费消息时才会从磁盘中读取并加载到内存
- 支持数百万条的消息存储

**基于命令行设置lazy-queue**

要设置一个队列为惰性队列，只需要在声明队列时，指定`x-queue-mode`属性为lazy即可。可以通过命令行将一个运行中的队列修改为惰性队列：

```sh
rabbitmqctl set_policy Lazy "^lazy-queue$" '{"queue-mode":"lazy"}' --apply-to queues  
```

命令解读：

- `rabbitmqctl` ：RabbitMQ的命令行工具
- `set_policy` ：添加一个策略
- `Lazy` ：策略名称，可以自定义
- `"^lazy-queue$"` ：用正则表达式匹配队列的名字
- `'{"queue-mode":"lazy"}'` ：设置队列模式为lazy模式
- `--apply-to queues  `：策略的作用对象，是所有的队列

**基于@Bean声明lazy-queue**

```java
@Configuration
public class lazyMessageConfig {

    @Bean
    public Queue lazyQueue() {
        return QueueBuilder
            .durable("lazy.queue")
            .lazy() // lazy-queue
            .build();
    }
}
```

**基于@RabbitListener声明LazyQueue**

```java
@Component
@Slf4j
public class SpringRabbitListener {
    
    @RabbitListener(queuesToDeclare = @Queue(
    	name = "lazy.queue", 
        durable = "true",
        arguments = @Argument(name = "x-queue-mode", value = "lazy")
    ))
    public void listenLazyQueue(String msg) {
        log.debug("消费者接收到lazy.queue的消息：【" + msg + "】");
    }
}
```

惰性队列的优点如下：

- 基于磁盘存储，消息上限高
- 没有间歇性的page-out，性能比较稳定

惰性队列的缺点如下：

- 基于磁盘存储，消息时效性会降低
- 性能受限于磁盘的IO

## 5.3 测试惰性队列和普通队列

在consumer消费端声明两个Bean，分别是惰性队列和普通队列：

```java
@Configuration
public class LazyConfig {

    @Bean
    public Queue lazyQueue() {
        return QueueBuilder
                .durable("lazy.queue")
                .lazy()
                .build();
    }

    @Bean
    public Queue normalQueue() {
        return QueueBuilder
                .durable("normal.queue")
                .build();
    }
}
```

publisher消息发送端测试类代码如下：

```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testLazyQueue() {
        for (int i = 0; i < 1000000; i++) {
            // 创建消息
            Message message = MessageBuilder
                    .withBody("hello, Spring".getBytes(StandardCharsets.UTF_8))
                    .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT) // 非持久化
                    .build();
            // 发送消息
            rabbitTemplate.convertAndSend("lazy.queue", message);
        }
    }

    @Test
    public void testNormalQueue() {
        for (int i = 0; i < 1000000; i++) {
            // 创建消息
            Message message = MessageBuilder
                    .withBody("hello, Spring".getBytes(StandardCharsets.UTF_8))
                    .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT) // 非持久化
                    .build();
            // 发送消息
            rabbitTemplate.convertAndSend("normal.queue", message);
        }
    }
}
```

两个同时运行，一个向LazyQueue发送，另一个向NormalQueue发送：

* LazyQueue，一条消息都没有在memory内存中，全部paged out刷出到磁盘中了。

  ![](..\图片\5-05【RabbitMQ】\5-2测试.png)

* NormalQueue，总共40多万条，只有4000条在内存中是，剩下的都在磁盘中。内存也会波动，达到30000条的时候就会刷出到磁盘中。

  ![](..\图片\5-05【RabbitMQ】\5-3测试.png)

# 第六章 MQ集群

RabbitMQ的是基于Erlang语言编写，而Erlang又是一个面向并发的语言，天然支持集群模式。RabbitMQ的集群有两种模式：

- **普通集群**：是一种分布式集群，将队列分散到集群的各个节点，从而提高整个集群的并发能力。
- **镜像集群**：是一种主从集群，普通集群的基础上，添加了主从备份功能，提高集群的数据可用性。

镜像集群虽然支持主从，但主从同步并不是强一致的，某些情况下可能有数据丢失的风险。因此在RabbitMQ的3.8版本以后，推出了新的功能：**仲裁队列**来代替镜像集群，底层采用Raft协议确保主从的数据一致性。

## 6.1 普通集群

普通集群，或者叫标准集群（classic cluster），具备下列特征：

- 会在集群的各个节点间共享部分数据，包括：交换机、队列元信息。不包含队列中的消息。
- 当访问集群某节点时，如果队列不在该节点，会从数据所在节点传递到当前节点并返回
- 队列所在节点宕机，队列中的消息就会丢失

结构如图：

![](..\图片\5-05【RabbitMQ】\6-1集群.png)

**搭建集群**

在RabbitMQ的官方文档中，讲述了两种集群的配置方式：

- 普通模式：普通模式集群不进行数据同步，每个MQ都有自己的队列、数据信息（其它元数据信息如交换机等会同步）。例如我们有2个MQ：mq1，和mq2，如果你的消息在mq1，而你连接到了mq2，那么mq2会去mq1拉取消息，然后返回给你。如果mq1宕机，消息就会丢失。
- 镜像模式：与普通模式不同，队列会在各个mq的镜像节点之间同步，因此你连接到任何一个镜像节点，均可获取到消息。而且如果一个节点宕机，并不会导致数据丢失。不过，这种方式增加了数据同步的带宽消耗。

我们先来看普通模式集群，我们的计划部署3节点的mq集群：

| 主机名 | 控制台端口      | amqp通信端口    |
| ------ | --------------- | --------------- |
| mq1    | 8081 ---> 15672 | 8071 ---> 5672  |
| mq2    | 8082 ---> 15672 | 8072 ---> 5672  |
| mq3    | 8083 ---> 15672 | 8073  ---> 5672 |

集群中的节点标示默认都是：`rabbit@[hostname]`，因此以上三个节点的名称分别为：

- rabbit@mq1
- rabbit@mq2
- rabbit@mq3

RabbitMQ底层依赖于Erlang，而Erlang虚拟机就是一个面向分布式的语言，默认就支持集群模式。集群模式中的每个RabbitMQ 节点使用 cookie 来确定它们是否被允许相互通信。

要使两个节点能够通信，它们必须具有相同的共享秘密，称为**Erlang cookie**。cookie 只是一串最多 255 个字符的字母数字字符。每个集群节点必须具有**相同的 cookie**。实例之间也需要它来相互通信。

我们先在之前启动的mq容器中获取一个cookie值，作为集群的cookie。执行下面的命令：

```sh
docker exec -it mq cat /var/lib/rabbitmq/.erlang.cookie
```

可以看到cookie值如下：

```sh
EZFYFWUJLZKQKTTQSEQV
```

接下来，停止并删除当前的mq容器，我们重新搭建集群。

```sh
docker rm -f mq
```

在`/tmp`目录新建一个配置文件 `rabbitmq.conf`：

```sh
cd /tmp
# 创建文件
touch rabbitmq.conf
```

写入文件内容如下：

```nginx
loopback_users.guest = false
listeners.tcp.default = 5672
cluster_formation.peer_discovery_backend = rabbit_peer_discovery_classic_config
cluster_formation.classic_config.nodes.1 = rabbit@mq1
cluster_formation.classic_config.nodes.2 = rabbit@mq2
cluster_formation.classic_config.nodes.3 = rabbit@mq3
```

再创建一个文件，记录cookie

```sh
cd /tmp
# 创建cookie文件
touch .erlang.cookie
# 写入cookie
echo "EZFYFWUJLZKQKTTQSEQV" > .erlang.cookie
# 修改cookie文件的权限
chmod 600 .erlang.cookie
```

准备三个目录,mq1、mq2、mq3：

```sh
cd /tmp
# 创建目录
mkdir mq1 mq2 mq3
```

然后拷贝`rabbitmq.conf`、`cookie`文件到mq1、mq2、mq3：

```sh
# 进入/tmp
cd /tmp
# 拷贝
cp rabbitmq.conf mq1
cp rabbitmq.conf mq2
cp rabbitmq.conf mq3
cp .erlang.cookie mq1
cp .erlang.cookie mq2
cp .erlang.cookie mq3
```

**启动集群**

进入`/tmp`目录：

```sh
cd /tmp
```

创建一个网络：

```sh
docker network create mq-net
```

运行命令

```sh
docker run -d --net mq-net \
-v ${PWD}/mq1/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
-v ${PWD}/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
-e RABBITMQ_DEFAULT_USER=linxuan \
-e RABBITMQ_DEFAULT_PASS=123321 \
--name mq1 \
--hostname mq1 \
-p 8071:5672 \
-p 8081:15672 \
rabbitmq:3.8-management
```

```sh
docker run -d --net mq-net \
-v ${PWD}/mq2/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
-v ${PWD}/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
-e RABBITMQ_DEFAULT_USER=linxuan \
-e RABBITMQ_DEFAULT_PASS=123321 \
--name mq2 \
--hostname mq2 \
-p 8072:5672 \
-p 8082:15672 \
rabbitmq:3.8-management
```

```sh
docker run -d --net mq-net \
-v ${PWD}/mq3/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
-v ${PWD}/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
-e RABBITMQ_DEFAULT_USER=linxuan \
-e RABBITMQ_DEFAULT_PASS=123321 \
--name mq3 \
--hostname mq3 \
-p 8073:5672 \
-p 8083:15672 \
rabbitmq:3.8-management
```

> 这里老师之前的用户名称是itcast。

**测试集群**

在mq1这个节点上添加一个队列：

![](..\图片\5-05【RabbitMQ】\6-2集群测试.png)

如图，在mq2和mq3两个控制台也都能看到：

![](..\图片\5-05【RabbitMQ】\6-3测试集群.png)

**数据共享测试**

接下来我们测试一下数据是否共享：

* 点击这个队列，进入管理页面，然后利用控制台发送一条消息到这个队列。

  ![](..\图片\5-05【RabbitMQ】\6-4测试集群.png)

* 结果在mq2、mq3上都能看到这条消息

  ![](..\图片\5-05【RabbitMQ】\6-5测试集群.png)

* 这是为什么呢？我们看一下普通集群的特点：会在集群的各个节点间共享部分数据，包括：交换机、队列元信息。不包含队列中的消息。当访问集群某节点时，如果队列不在该节点，会从数据所在节点传递到当前节点并返回。

* 当访问集群某节点时，如果队列不在该节点，会从数据所在节点传递到当前节点并返回。

接下来测试一下集群的可用性：

* 我们让其中一台节点mq1宕机：`docker stop mq1`

  ```apl
  Error: could not connect to server since 2022-11-01 19:36:44. Will retry at 2022-11-01 19:37:51
  ```

  ![](..\图片\5-05【RabbitMQ】\6-6mq1挂机.png)

* 然后登录mq2或mq3的控制台，发现simple.queue也不可用了：

  ![](..\图片\5-05【RabbitMQ】\6-7mq2不可用.png)

* 说明数据并没有拷贝到mq2和mq3。

* 重新恢复mq1，那么数据就可以访问了

## 6.2 镜像集群

在刚刚的案例中，一旦创建队列的主机宕机，队列就会不可用。不具备高可用能力。如果要解决这个问题，必须使用官方提供的镜像集群方案。官方文档地址：https://www.rabbitmq.com/ha.html

默认情况下，队列只保存在创建该队列的节点上。而镜像模式下，创建队列的节点被称为该队列的主节点，队列还会拷贝到集群中的其它节点，也叫做该队列的镜像节点。但是，不同队列可以在集群中的任意节点上创建，因此不同队列的主节点可以不同。甚至，**一个队列的主节点可能是另一个队列的镜像节点**。

用户发送给队列的一切请求，例如发送消息、消息回执默认都会在主节点完成，如果是从节点接收到请求，也会路由到主节点去完成。**镜像节点仅仅起到备份数据作用**。当主节点接收到消费者的ACK时，所有镜像都会删除节点中的数据。

总结如下：

- 镜像队列结构是一主多从（从就是镜像）
- 所有操作都是主节点完成，然后同步给镜像节点
- 主宕机后，镜像节点会替代成新的主（如果在主从同步完成前，主就已经宕机，可能出现数据丢失）
- 不具备负载均衡功能，因为所有操作都会有主节点完成（但是不同队列，其主节点可以不同，可以利用这个提高吞吐量）

镜像集群：本质是主从模式，具备下面的特征：

- 交换机、队列、队列中的消息会在各个mq的镜像节点之间同步备份。
- 创建队列的节点被称为该队列的**主节点，**备份到的其它节点叫做该队列的**镜像**节点。
- 一个队列的主节点可能是另一个队列的镜像节点
- 所有操作都是主节点完成，然后同步给镜像节点
- 主宕机后，镜像节点会替代成新的主

结构如图：

![](..\图片\5-05【RabbitMQ】\6-8镜像集群.png)

镜像模式的配置有3种模式：

| ha-mode         | ha-params         | 效果                                                         |
| :-------------- | :---------------- | :----------------------------------------------------------- |
| 准确模式exactly | 队列的副本量count | 集群中队列副本（主服务器和镜像服务器之和）的数量。count如果为1意味着单个副本：即队列主节点。count值为2表示2个副本：1个队列主和1个队列镜像。换句话说：count = 镜像数量 + 1。如果群集中的节点数少于count，则该队列将镜像到所有节点。如果有集群总数大于count+1，并且包含镜像的节点出现故障，则将在另一个节点上创建一个新的镜像。 |
| all             | (none)            | 队列在群集中的所有节点之间进行镜像。队列将镜像到任何新加入的节点。镜像到所有节点将对所有群集节点施加额外的压力，包括网络I / O，磁盘I / O和磁盘空间使用情况。推荐使用exactly，设置副本数为（N / 2 +1）。 |
| nodes           | *node names*      | 指定队列创建到哪些节点，如果指定的节点全部不存在，则会出现异常。如果指定的节点在集群中存在，但是暂时不可用，会创建节点到当前客户端连接到的节点。 |

这里我们以`rabbitmqctl`命令作为案例来讲解配置语法。

命令随便在哪一个节点敲都可以，不过记住需要进入mq容器内部：`docker exec -it mq1 bash`

**exactly模式**

```sh
rabbitmqctl set_policy ha-two "^two\." '{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'
```

- `rabbitmqctl set_policy`：固定写法
- `ha-two`：策略名称，自定义。ha高可用
- `"^two\."`：匹配队列的正则表达式，符合命名规则的队列才生效，这里是任何以`two.`开头的队列名称
- `'{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'`: 策略内容
  - `"ha-mode":"exactly"`：策略模式，此处是exactly模式，指定副本数量
  - `"ha-params":2`：策略参数，这里是2，就是副本数量为2，1主1镜像
  - `"ha-sync-mode":"automatic"`：同步策略，默认是manual，即新加入的镜像节点不会同步旧的消息。如果设置为automatic，则新加入的镜像节点会把主节点中所有消息都同步，会带来额外的网络开销

**all模式**

```sh
rabbitmqctl set_policy ha-all "^all\." '{"ha-mode":"all"}'
```

- `ha-all`：策略名称，自定义
- `"^all\."`：匹配所有以`all.`开头的队列名
- `'{"ha-mode":"all"}'`：策略内容
  - `"ha-mode":"all"`：策略模式，此处是all模式，即所有节点都会称为镜像节点

**nodes模式**

```sh
rabbitmqctl set_policy ha-nodes "^nodes\." '{"ha-mode":"nodes","ha-params":["rabbit@nodeA", "rabbit@nodeB"]}'
```

- `rabbitmqctl set_policy`：固定写法
- `ha-nodes`：策略名称，自定义
- `"^nodes\."`：匹配队列的正则表达式，符合命名规则的队列才生效，这里是任何以`nodes.`开头的队列名称
- `'{"ha-mode":"nodes","ha-params":["rabbit@nodeA", "rabbit@nodeB"]}'`: 策略内容
  - `"ha-mode":"nodes"`：策略模式，此处是nodes模式
  - `"ha-params":["rabbit@mq1", "rabbit@mq2"]`：策略参数，这里指定副本所在节点名称

**测试**

我们使用exactly模式的镜像，因为集群节点数量为3，因此镜像数量就设置为2.

运行下面的命令：

```sh
docker exec -it mq1 rabbitmqctl set_policy ha-two "^two\." '{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'
```

![](..\图片\5-05【RabbitMQ】\6-9镜像集群.png)

下面，我们创建一个新的队列：

![](..\图片\5-05【RabbitMQ】\6-10测试镜像集群.png)

在任意一个控制台查看队列：

![](..\图片\5-05【RabbitMQ】\6-11.png)

接下来测试一下数据共享：

* 给two.queue发送一条消息：

  ![](..\图片\5-05【RabbitMQ】\6-12.png)

* 然后在mq1、mq2、mq3的任意控制台查看消息：

  ![](..\图片\5-05【RabbitMQ】\6-13.png)

下面来测试一下高可用：

* 现在，我们让two.queue的主节点mq1宕机：`docker stop mq1`

* 查看集群状态：

  ![](..\图片\5-05【RabbitMQ】\6-14.png)

* 查看队列状态：

  ![](..\图片\5-05【RabbitMQ】\6-15.png)

* 发现依然是健康的！并且其主节点切换到了`rabbit@mq2`上

* 如果过了一会mq1恢复了，那么并不会变成主节点了！

## 6.3 仲裁队列

<!--Quorum仲裁 法定人数-->

仲裁队列：仲裁队列是3.8版本以后才有的新功能，用来替代镜像队列，具备下列特征：

- 与镜像队列一样，都是主从模式，支持主从数据同步
- 使用非常简单，没有复杂的配置
- 主从同步基于Raft协议，强一致

**添加仲裁队列**

在任意控制台添加一个队列，一定要选择队列类型为Quorum类型。

![](..\图片\5-05【RabbitMQ】\7-1.png)

在任意控制台查看队列：

![](..\图片\5-05【RabbitMQ】\7-2.png)

可以看到，仲裁队列的 + 2字样。代表这个队列有2个镜像节点。因为仲裁队列默认的镜像数为5。如果你的集群有7个节点，那么镜像数肯定是5；而我们集群只有3个节点，因此镜像数量就是3.

**Java代码创建仲裁队列**

```java
@Bean
public Queue quorumQueue() {
    return QueueBuilder
        .durable("quorum.queue") // 持久化
        .quorum() // 仲裁队列
        .build();
}
```

**SpringAMQP连接MQ集群**

注意，这里用address来代替host、port方式

```java
spring:
  rabbitmq:
    addresses: 192.168.150.105:8071, 192.168.150.105:8072, 192.168.150.105:8073
    username: linxuan
    password: 123321
    virtual-host: /
```
