# 第一章 服务拆分和远程调用

任何分布式架构都离不开服务的拆分，微服务也是一样。

## 1.1 服务拆分原则

这里总结了微服务拆分时的几个原则：

- 不同微服务，不要重复开发相同业务
- 微服务数据独立，不要访问其它微服务的数据库
- 微服务可以将自己的业务暴露为接口，供其它微服务调用

![](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713210800950.png)

## 1.2 服务拆分示例

接下来我们来做一个服务拆分的例子：以课前资料中的微服务cloud-demo为例，，其结构如下：

```bash
cloud-demo # 父工程，管理依赖
    |
    |-- order-Service # 根据id查询订单 订单微服务，负责订单相关业务
    |-- user-service # 根据id查询用户  用户微服务，负责用户相关业务
```

要求如下：

* 订单微服务和用户微服务都必须有各自的数据库，相互独立。
* 订单服务和用户服务都对外暴露Restful的接口。
* 订单服务如果需要查询用户信息，只能调用用户服务的Restful接口，不能查询用户数据库

在order-service服务中，有一个根据id查询订单的接口：

```java
@RestController
@RequestMapping("order")
public class OrderController {

   @Autowired
   private OrderService orderService;

    @GetMapping("{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        // 根据id查询订单并返回
        return orderService.queryOrderById(orderId);
    }
}
```

在user-service中有一个根据id查询用户的接口：

```java
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 路径： /user/110
     * 端口号是8081 id最小为1
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }
}
```

正常情况下两者查询时不会相互关联的，查询订单出来的就是订单，查询用户出来的就是用户。

我们的需求就是修改order-service中的根据id查询订单业务，要求在查询订单的同时，根据订单中包含的userId查询出用户信息，一起返回。

![image-20210713213312278](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713213312278.png)

因此，我们需要在order-service中 向user-service发起一个http的请求，调用`http://localhost:8081/user/{userId}`这个接口。

大概的步骤是这样的：

- 注册一个RestTemplate的实例到Spring容器
- 修改order-service服务中的OrderService类中的queryOrderById方法，根据Order对象中的userId查询User
- 将查询的User填充到Order对象，一起返回

接下来我们实际操作一下：

1. 注册RestTemplate
   
   首先，我们在order-service服务中的OrderApplication启动类中，注册RestTemplate实例：
   
   ```java
   package cn.itcast.order;
   
   @MapperScan("cn.itcast.order.mapper")
   @SpringBootApplication
   public class OrderApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(OrderApplication.class, args);
       }
   
       @Bean
       public RestTemplate restTemplate() {
           return new RestTemplate();
       }
   }
   ```
   
2. 实现远程调用
   
   修改order-service服务中的cn.itcast.order.service包下的OrderService类中的queryOrderById方法：
   
   ```java
   @Service
   public class OrderService {
   
       @Autowired
       private OrderMapper orderMapper;
   
       @Autowired
       private RestTemplate restTemplate;
   
       public Order queryOrderById(Long orderId) {
           // 1.查询订单
           Order order = orderMapper.findById(orderId);
           // 2.根据查询到的订单里面的userId来调用查询user
           String url = "http://localhost:8081/user/" + order.getUserId();
           User user = restTemplate.getForObject(url, User.class);
           // 3.user信息存入order里面
           order.setUser(user);
           // 4.返回
           return order;
       }
   }
   ```

## 1.3 提供者与消费者

在服务调用关系中，会有两个不同的角色：

- **服务提供者**：一次业务中，被其它微服务调用的服务。（提供接口给其它微服务）

- **服务消费者**：一次业务中，调用其它微服务的服务。（调用其它微服务提供的接口）

![image-20210713214404481](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713214404481.png)

但是，服务提供者与服务消费者的角色并不是绝对的，而是相对于业务而言。

如果服务A调用了服务B，而服务B又调用了服务C，服务B的角色是什么？

- 对于A调用B的业务而言：A是服务消费者，B是服务提供者
- 对于B调用C的业务而言：B是服务消费者，C是服务提供者
- 因此，服务B既可以是服务提供者，也可以是服务消费者。

# 第二章 Eureka注册中心

假如我们的服务提供者user-service部署了多个实例，如图：

![image-20210713214925388](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713214925388.png)

我们来思考几个问题：

- order-service在发起远程调用的时候，该如何得知user-service实例的ip地址和端口？
- 有多个user-service实例地址，order-service调用时该如何选择？
- order-service如何得知某个user-service实例是否依然健康，是不是已经宕机？

## 2.1 Eureka的结构和作用

这些问题都需要利用SpringCloud中的注册中心来解决，其中最广为人知的注册中心就是Eureka，其结构如下：

![image-20210713220104956](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713220104956.png)

我们可以利用Eureka来回答之前的各个问题：

* 问题1：order-service如何得知user-service实例地址？
  
  获取地址信息的流程如下：
  
  - user-service服务实例启动后，将自己的信息注册到eureka-server（Eureka服务端）。这个叫服务注册
  
  - eureka-server保存服务名称到服务实例地址列表的映射关系
  
  - order-service根据服务名称，拉取实例地址列表。这个叫服务发现或服务拉取

* 问题2：order-service如何从多个user-service实例中选择具体的实例？
  
  - order-service从实例列表中利用负载均衡算法选中一个实例地址
  
  - 向该实例地址发起远程调用

* 问题3：order-service如何得知某个user-service实例是否依然健康，是不是已经宕机？
  
  - user-service会每隔一段时间（默认30秒）向eureka-server发起请求，报告自己状态，称为心跳。
  
  - 当超过一定时间没有发送心跳时，eureka-server会认为微服务实例故障，将该实例从服务列表中剔除。
  
  - order-service拉取服务时，就能将故障实例排除了。

> 注意：一个微服务，既可以是服务提供者，又可以是服务消费者，因此eureka将服务注册、服务发现等功能统一封装到了eureka-client端

因此，接下来我们动手实践的步骤包括：

![image-20210713220509769](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713220509769.png)

## 2.2 搭建eureka-server

首先注册中心服务端：eureka-server，这必须是一个独立的微服务

1. 创建eureka-server服务
   
   在cloud-demo父工程下，创建一个子模块，利用Maven创建名称为eureka-server。

2. 引入eureka依赖
   
   在eureka-server模块的pom.xml文件中引入SpringCloud为eureka提供的starter依赖
   
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
   </dependency>
   ```

3. 编写启动类
   
   给eureka-server服务编写一个启动类，一定要添加一个`@EnableEurekaServer`注解，开启eureka的注册中心功能：
   
   ```java
   package cn.linxuan.eureka;
   
   @SpringBootApplication
   @EnableEurekaServer
   public class EurekaApplication {
       public static void main(String[] args) {
           SpringApplication.run(EurekaApplication.class, args);
       }
   }
   ```

4. 编写配置文件
   
   在eureka-server模块的resources文件夹下面编写一个application.yml文件，内容如下：
   
   ```yml
   server:
     port: 10086
   spring:
     application:
       name: eureka-server
   eureka:
     client:
       service-url:
         defaultZone: http://127.0.0.1:10086/eureka
       registerWithEureka: false # 表示是否将自己注册到Eureka Server，默认为true。由于当前这个应用就是Eureka Server，故而设为false。
       fetchRegistry: false # 表示是否从Eureka Server获取注册信息，默认为true。因为这是一个单点的Eureka Server，不需要同步其他的Eureka Server节点的数据，故而设为false。
   ```

5. 启动服务
   
   启动微服务，然后在浏览器访问：http://127.0.0.1:10086。看到下面结果就是成功了，我这里不会注册自己的。上面yml文件设置了：
   
   ![image-20210713222157190](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713222157190.png)

## 2.3 服务注册

下面，我们将user-service注册到eureka-server中去。

1. 引入依赖
   
   在user-service的pom文件中，引入下面的eureka-client依赖：
   
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   ```

2. 修改配置文件
   
   在user-service中，修改application.yml文件，添加服务名称、eureka地址：
   
   ```yml
   spring:
     application:
       name: userservice
   eureka:
     client:
       service-url:
         defaultZone: http://127.0.0.1:10086/eureka
   ```

3. 启动多个user-service实例
   
   为了演示一个服务有多个实例的场景，我们添加一个SpringBoot的启动配置，再启动一个user-service。
   
   首先，复制原来的user-service启动配置：
   
   ![image-20210713222656562](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713222656562.png)
   
   然后，在弹出的窗口中，填写信息：-Dserver.port=8082
   
   ![image-20210713222757702](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713222757702.png)
   
   现在，SpringBoot窗口会出现两个user-service启动配置：
   
   <img title="" src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713222841951.png" alt="image-20210713222841951" width="448" data-align="center">
   
   不过，第一个是8081端口，第二个是8082端口。
   
   启动两个user-service实例：
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713223041491.png" title="" alt="image-20210713223041491" data-align="center">
   
   查看eureka-server管理页面：
   
   ![image-20210713223150650](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713223150650.png)

## 2.4 服务发现

下面，我们将order-service的逻辑修改：向eureka-server拉取user-service的信息，实现服务发现。

1. 引入依赖
   
   之前说过，服务发现、服务注册统一都封装在eureka-client依赖，因此这一步与服务注册时一致。
   
   在order-service的pom文件中，引入下面的eureka-client依赖：
   
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   ```

2. 配置文件
   
   服务发现也需要知道eureka地址，因此第二步与服务注册一致，都是配置eureka信息：
   
   在order-service中，修改application.yml文件，添加服务名称、eureka地址：
   
   ```yml
   spring:
     application:
       name: orderservice
   eureka:
     client:
       service-url:
         defaultZone: http://127.0.0.1:10086/eureka
   ```

3. 服务拉取和负载均衡
   
   最后，我们要去eureka-server中拉取user-service服务的实例列表，并且实现负载均衡。不过这些动作不用我们去做，只需要添加一些注解即可。
   
   在order-service的OrderApplication中，给RestTemplate这个Bean添加一个@LoadBalanced注解：
   
   ```java
   @MapperScan("cn.itcast.order.mapper")
   @SpringBootApplication
   public class OrderApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(OrderApplication.class, args);
       }
   
       @Bean
       @LoadBalanced // 负载均衡注解
       public RestTemplate restTemplate() {
           return new RestTemplate();
       }
   }
   ```
   
   修改order-service服务中的cn.itcast.order.service包下的OrderService类中的queryOrderById方法。修改访问的url路径，用服务名代替ip、端口：
   
   ```java
   @Service
   public class OrderService {
   
       @Autowired
       private OrderMapper orderMapper;
   
       @Autowired
       private RestTemplate restTemplate;
   
       public Order queryOrderById(Long orderId) {
           // 1.查询订单
           Order order = orderMapper.findById(orderId);
           // 2.根据查询到的订单里面的userId来调用查询user
           String url = "http://userservice/user/" + order.getUserId();
           User user = restTemplate.getForObject(url, User.class);
           // 3.user信息存入order里面
           order.setUser(user);
           // 4.返回
           return order;
       }
   }
   ```

spring会自动帮助我们从eureka-server端，根据userservice这个服务名称，获取实例列表，而后完成负载均衡。

## 2.5 Nginx负载均衡

**什么是负载均衡**

负载均衡 建立在现有网络结构之上，它提供了一种廉价有效透明的方法扩展网络设备和服务器的带宽、增加吞吐量、加强网络数据处理能力、提高网络的灵活性和可用性。
负载均衡，英文名称为Load Balance，其意思就是分摊到多个操作单元上进行执行，例如Web服务器、FTP服务器、企业关键应用服务器和其它关键任务服务器等，从而共同完成工作任务。

**配置负载均衡-准备工作**

1. 将刚才的存放工程的tomcat复制三份，修改端口分别为8080 ，8081，8082 。

2. 分别启动这三个tomcat服务。

3. 为了能够区分是访问哪个服务器的网站，可以在首页标题加上标记以便区分。

**配置负载均衡**

修改 Nginx配置文件：

```apl
  upstream tomcat-travel {
       server 192.168.177.129:8080;
       server 192.168.177.129:8081;
       server 192.168.177.129:8082;
    }

    server {
        listen       80; # 监听的端口
        server_name  www.hmtravel.com; # 域名或ip
        location / {    # 访问路径配置
            # root   index;# 根目录
        proxy_pass http://tomcat-travel;

            index  index.html index.htm; # 默认首页
        }
        error_page   500 502 503 504  /50x.html;    # 错误页面
        location = /50x.html {
            root   html;
        }
    }
```

地址栏输入http:// www.hmtravel.com / 刷新观察每个网页的标题，看是否不同。

经过测试，三台服务器出现的概率各为33.3333333%，交替显示。

如果其中一台服务器性能比较好，想让其承担更多的压力，可以设置权重。

比如想让NO.1出现次数是其它服务器的2倍，则修改配置如下：

```apl
    upstream tomcat-travel {
       server 192.168.177.129:8080;
       server 192.168.177.129:8081 weight=2;
       server 192.168.177.129:8082;
    }
```

经过测试，每刷新四次，有两次是8081

# 第三章 Ribbon负载均衡

上一节中，我们添加了`@LoadBalanced`注解，即可实现负载均衡功能，这是什么原理呢？

## 3.1 负载均衡原理

SpringCloud底层其实是利用了一个名为Ribbon的组件，来实现负载均衡功能的。

![image-20210713224517686](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713224517686.png)

那么我们发出的请求明明是http://userservice/user/1，怎么变成了http://localhost:8081的呢？

## 3.2 源码跟踪

为什么我们只输入了service名称就可以访问了呢？之前还要获取ip和端口。

显然有人帮我们根据service名称，获取到了服务实例的ip和端口。它就是`LoadBalancerInterceptor`，这个类会在对RestTemplate的请求进行拦截，然后从Eureka根据服务id获取服务列表，随后利用负载均衡算法得到真实的服务地址信息，替换服务id。

我们进行源码跟踪：

1. LoadBalancerIntercepor
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1525620483637.png" title="" alt="" width="1023">
   
   可以看到这里的intercept方法，拦截了用户的HttpRequest请求，然后做了几件事：
   
   * `request.getURI()`：获取请求uri，本例中就是 http://user-service/user/8
   
   * `originalUri.getHost()`：获取uri路径的主机名，其实就是服务id，`user-service`
   
   * `this.loadBalancer.execute()`：处理服务id，和用户请求。
   
   这里的`this.loadBalancer`是`LoadBalancerClient`类型，我们继续跟入。

2. LoadBalancerClient
   
   继续跟入execute方法：
   
   <img title="" src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1525620787090.png" alt="" width="1059">
   
   代码是这样的：
   
   * `getLoadBalancer(serviceId)`：根据服务id获取ILoadBalancer，而ILoadBalancer会拿着服务id去eureka中获取服务列表并保存起来。
   
   * `getServer(loadBalancer)`：利用内置的负载均衡算法，从服务列表中选择一个。本例中，可以看到获取了8082端口的服务
   
   放行后，再次访问并跟踪，发现获取的是8081：
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1525620835911.png" title="" alt="" width="1037">
   
   果然实现了负载均衡。

3. 负载均衡策略IRule
   
   在刚才的代码中，可以看到获取服务使通过一个`getServer`方法来做负载均衡:
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1525620835911.png" title="" alt="" width="1010">
   
   我们继续跟入：
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1544361421671.png" title="" alt="" width="1119">
   
   继续跟踪源码chooseServer方法，发现这么一段代码：
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1525622652849.png" title="" alt="" width="1073">
   
   我们看看这个rule是谁：
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1525622699666.png" title="" alt="" width="1071">
   
   这里的rule默认值是一个`RoundRobinRule`，看类的介绍：
   
   <img src="..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/1525622754316.png" title="" alt="" width="1153">
   
   这不就是轮询的意思嘛。
   
   到这里，整个负载均衡的流程我们就清楚了。

总结：

`SpringCloudRibbon`的底层采用了一个拦截器，拦截了`RestTemplate`发出的请求，对地址做了修改。用一幅图来总结一下：

![image-20210713224724673](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713224724673.png)

基本流程如下：

- 拦截我们的RestTemplate请求http://userservice/user/1
- RibbonLoadBalancerClient会从请求url中获取服务名称，也就是user-service
- DynamicServerListLoadBalancer根据user-service到eureka拉取服务列表
- eureka返回列表，localhost:8081、localhost:8082
- IRule利用内置负载均衡规则，从列表中选择一个，例如localhost:8081
- RibbonLoadBalancerClient修改请求地址，用localhost:8081替代userservice，得到http://localhost:8081/user/1，发起真实请求

## 3.3 负载均衡策略

### 3.3.1 负载均衡策略

负载均衡的规则都定义在IRule接口中，而IRule有很多不同的实现类：

![image-20210713225653000](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713225653000.png)

不同规则的含义如下：

| **内置负载均衡规则类**             | **规则描述**                                                                                                                                                                                                                                                 |
| ------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| RoundRobinRule            | 简单轮询服务列表来选择服务器。它是Ribbon默认的负载均衡规则。                                                                                                                                                                                                                        |
| AvailabilityFilteringRule | 对以下两种服务器进行忽略：   （1）在默认情况下，这台服务器如果3次连接失败，这台服务器就会被设置为“短路”状态。短路状态将持续30秒，如果再次连接失败，短路的持续时间就会几何级地增加。  （2）并发数过高的服务器。如果一个服务器的并发连接数过高，配置了AvailabilityFilteringRule规则的客户端也会将其忽略。并发连接数的上限，可以由客户端的<clientName>.<clientConfigNameSpace>.ActiveConnectionsLimit属性进行配置。 |
| WeightedResponseTimeRule  | 为每一个服务器赋予一个权重值。服务器响应时间越长，这个服务器的权重就越小。这个规则会随机选择服务器，这个权重值会影响服务器的选择。                                                                                                                                                                                        |
| **ZoneAvoidanceRule**     | 以区域可用的服务器为基础进行服务器的选择。使用Zone对服务器进行分类，这个Zone可以理解为一个机房、一个机架等。而后再对Zone内的多个服务做轮询。                                                                                                                                                                             |
| BestAvailableRule         | 忽略那些短路的服务器，并选择并发数较低的服务器。                                                                                                                                                                                                                                 |
| RandomRule                | 随机选择一个可用的服务器。                                                                                                                                                                                                                                            |
| RetryRule                 | 重试机制的选择逻辑                                                                                                                                                                                                                                                |

默认的实现就是ZoneAvoidanceRule，是一种轮询方案

### 3.3.2 自定义负载均衡策略

通过定义IRule实现可以修改负载均衡规则，有两种方式：

1. 第一种代码方式：在order-service中的OrderApplication类中，定义一个新的IRule：
   
   ```java
   // 这里的实现方式的RandomRule，其他方式也可以的。这样就将负载均衡规则换成了随机。
   @Bean
   public IRule randomRule(){
       return new RandomRule();
   }
   ```

2. 第二种配置文件方式：在order-service的application.yml文件中，添加新的配置也可以修改规则：
   
   ```yml
   userservice: # 给某个微服务配置负载均衡规则，这里是userservice服务
     ribbon:
       NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则 
   ```

> **注意**，一般用默认的负载均衡规则，不做修改。

## 3.4 饥饿加载

我们重启服务器，然后访问http://localhost:8080/order/103，如果是第一次访问的话会消耗很长的时间。这是由于Ribbon的原因，Ribbon默认是采用懒加载，即第一次访问时才会去创建LoadBalanceClient，请求时间会很长。

而饥饿加载则会在项目启动时创建，降低第一次访问的耗时，通过下面配置开启饥饿加载：

```yaml
ribbon:
  eager-load:
    enabled: true # 开启饥饿加载
    clients: userservice # 指定饥饿加载的服务名称
```

# 第四章 Nacos注册中心

国内公司一般都推崇阿里巴巴的技术，比如注册中心，SpringCloudAlibaba也推出了一个名为Nacos的注册中心。

[Nacos](https://nacos.io/)是阿里巴巴的产品，现在是[SpringCloud](https://spring.io/projects/spring-cloud)中的一个组件。相比[Eureka](https://github.com/Netflix/eureka)功能更加丰富，在国内受欢迎程度较高。

我们使用的是windows版本`nacos-server-1.4.1.zip`包，将其解压缩就可以直接使用了。然后执行命令即可：`startup.cmd -m standalone`。在浏览器输入地址：http://127.0.0.1:8848/nacos即可访问。默认的账号和密码都是nacos。

> 经测试，如果双击打开运行文件，没有用。

## 4.1 服务注册到nacos

Nacos是SpringCloudAlibaba的组件，而SpringCloudAlibaba也遵循SpringCloud中定义的服务注册、服务发现规范。因此使用Nacos和使用Eureka对于微服务来说，并没有太大区别。

主要差异在于：依赖不同、服务地址不同。

我们接下来操作一下：

1. 引入依赖
   
   在cloud-demo父工程的pom文件中的`<dependencyManagement>`中引入SpringCloudAlibaba的依赖：
   
   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-alibaba-dependencies</artifactId>
       <version>2.2.5.RELEASE</version>
       <type>pom</type>
       <scope>import</scope>
   </dependency>
   ```
   
   然后在user-service和order-service中的pom文件中引入nacos-discovery依赖：
   
   ```xml
   <!--nacos客户端依赖-->
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```
   
   > 注意：不要忘了注释掉eureka的依赖。

2. 配置nacos地址
   
   在user-service和order-service的application.yml中添加nacos地址：
   
   ```yml
   spring:
     cloud:
       nacos:
         server-addr: localhost:8848
   ```
   
   > **注意**：不要忘了注释掉eureka的地址

3. 重启

   重启微服务后，登录nacos管理页面，可以看到微服务信息：

   ![image-20210713231439607](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713231439607.png)

## 4.2 服务分级存储模型

一个**服务**可以有多个**实例**，例如我们的user-service，可以有:

- 127.0.0.1:8081
- 127.0.0.1:8082
- 127.0.0.1:8083

假如这些实例分布于全国各地的不同机房，例如：

- 127.0.0.1:8081，在上海机房
- 127.0.0.1:8082，在上海机房
- 127.0.0.1:8083，在杭州机房

Nacos就将同一机房内的实例 划分为一个**集群**。

也就是说，user-service是服务，一个服务可以包含多个集群，如杭州、上海，每个集群下可以有多个实例，形成分级模型，如图：

![image-20210713232522531](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713232522531.png)

微服务互相访问时，应该尽可能访问同集群实例，因为本地访问速度更快。当本集群内不可用时，才访问其它集群。例如：

![image-20210713232658928](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713232658928.png)

杭州机房内的order-service应该优先访问同机房的user-service。

### 4.2.1 给user-service配置集群

修改user-service的application.yml文件，添加集群配置：

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ # 集群名称
```

重启两个user-service实例后，我们可以在nacos控制台看到下面结果：

![image-20210713232916215](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713232916215.png)

我们再次复制一个user-service启动配置，添加属性：

```sh
-Dserver.port=8083
-Dspring.cloud.nacos.discovery.cluster-name=SH
```

配置如图所示：

![image-20210713233528982](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713233528982.png)

启动UserApplication3后再次查看nacos控制台：

![image-20210713233727923](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713233727923.png)

### 4.2.2 同集群优先的负载均衡

默认的`ZoneAvoidanceRule`并不能实现根据同集群优先来实现负载均衡。

因此Nacos中提供了一个`NacosRule`的实现，可以优先从同集群中挑选实例。

1. 给order-service配置集群信息

   修改order-service的application.yml文件，添加集群配置：

   ```yml
   spring:
     cloud:
       nacos:
         server-addr: localhost:8848
         discovery:
           cluster-name: HZ # 集群名称
   ```

2. 修改负载均衡规则

   修改order-service的application.yml文件，修改负载均衡规则：

   ```yml
   userservice:
     ribbon:
       NFLoadBalancerRuleClassName: com.alibaba.cloud.nacos.ribbon.NacosRule # 负载均衡规则 
   ```

## 4.3 权重配置

实际部署中会出现这样的场景：

- 服务器设备性能有差异，部分实例所在机器性能较好，另一些较差，我们希望性能好的机器承担更多的用户请求。但默认情况下NacosRule是同集群内随机挑选，不会考虑机器的性能问题。


因此，Nacos提供了权重配置来控制访问频率，权重越大则访问频率越高。

在nacos控制台，找到user-service的实例列表，点击编辑，即可修改权重：

![image-20210713235133225](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713235133225.png)

在弹出的编辑窗口，修改权重：

![image-20210713235235219](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210713235235219.png)

> **注意**：如果权重修改为0，则该实例永远不会被访问。

## 4.4 环境隔离

Nacos提供了namespace来实现环境隔离功能。

- nacos中可以有多个namespace
- namespace下可以有group、service等
- 不同namespace之间相互隔离，例如不同namespace的服务互相不可见

![image-20210714000101516](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000101516.png)

### 4.4.1 创建namespace

默认情况下，所有service、data、group都在同一个namespace，名为public：

![image-20210714000414781](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000414781.png)

我们可以点击页面新增按钮，添加一个namespace：

![image-20210714000440143](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000440143.png)

然后，填写表单：

![image-20210714000505928](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000505928.png)

就能在页面看到一个新的namespace：

![image-20210714000522913](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000522913.png)

### 4.4.2 配置namespace

给微服务配置namespace只能通过修改配置来实现。

例如，修改order-service的application.yml文件：

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ
        namespace: 492a7d5d-237b-46a1-a99a-fa8e98e4b0f9 # 命名空间，填ID
```

重启order-service后，访问控制台，可以看到下面的结果：

![image-20210714000830703](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000830703.png)

![image-20210714000837140](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000837140.png)

此时访问order-service，因为namespace不同，会导致找不到userservice，控制台会报错：

![image-20210714000941256](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714000941256.png)

## 4.5 Nacos与Eureka的区别

Nacos的服务实例分为两种l类型：

- 临时实例：如果实例宕机超过一定时间，会从服务列表剔除，默认的类型。

- 非临时实例：如果实例宕机，不会从服务列表剔除，也可以叫永久实例。

配置一个服务实例为永久实例：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        ephemeral: false # 设置为非临时实例
```

Nacos和Eureka整体结构类似，服务注册、服务拉取、心跳等待，但是也存在一些差异：

![image-20210714001728017](..\图片\4-01【微服务、Eureka、Ribbon、Nacos】/image-20210714001728017.png)

Nacos与eureka的共同点：

- 都支持服务注册和服务拉取
- 都支持服务提供者心跳方式做健康检测

Nacos与Eureka的区别：

- Nacos支持服务端主动检测提供者状态：临时实例采用心跳模式，非临时实例采用主动检测模式
- 临时实例心跳不正常会被剔除，非临时实例则不会被剔除
- Nacos支持服务列表变更的消息推送模式，服务列表更新更及时
- Nacos集群默认采用AP方式，当集群中存在非临时实例时，采用CP模式；Eureka采用AP方式



# 第五章 Nacos配置管理

Nacos除了可以做注册中心，同样可以做配置管理来使用。

* Nacos启动：`startup.cmd -m standalone`
* Nacos关闭：直接双击`shutdown.cmd`

## 5.1 统一配置管理

当微服务部署的实例越来越多，达到数十、数百时，逐个修改微服务配置就会让人抓狂，而且很容易出错。我们需要一种统一配置管理方案，可以集中管理所有实例的配置。

Nacos一方面可以将配置集中管理，另一方可以在配置变更时，及时通知微服务，实现配置的热更新。所以我们首先需要在Nacos中完成配置的管理，然后再从Nacos中拉取过来配置到本地。

### 5.1.1 nacos中管理配置

在nacos中管理配置方法如下：

1. 启动Nacos，访问，在左侧菜单栏配置管理里面的配置列表选择添加。

2. 然后在弹出的表单中，填写配置信息：

   * Data ID(配置文件的id)：[服务名称].[profile].[后缀名]，例如userservice-dev.yaml。

   * Group：分组，默认即可。

   * 描述：userservice的开发环境配置

   * 配置格式：yaml。目前支持yaml和properties。

   * 配置内容：填写需要修改的配置信息，这里我们修改为：

     ```yaml
     pattern:
         dateformat: yyyy-MM-dd hh:mm:ss 
     ```

> 注意：项目的核心配置，需要热更新的配置才有放到nacos管理的必要。基本不会变更的一些配置还是保存在微服务本地比较好。

### 5.1.2 从微服务拉取配置

微服务要拉取nacos中管理的配置，并且与本地的application.yml配置合并，才能完成项目启动。但如果尚未读取application.yml，又如何得知nacos地址呢？

因此spring引入了一种新的配置文件：bootstrap.yaml文件，会在application.yml之前被读取，流程如下：

1. 项目启动，加载bootstrap.yaml文件，获取nacos地址，配置文件Id。
2. 根据id去nacos中获取配置，读取nacos中配置文件。
3. 加载本地配置application.yml，与nacos中拉取到的配置合并。
4. 创建Spring容器。
5. 加载Bean。

![img](D:\Java\笔记\图片\4-02【Nacos配置管理、Feign、Gateway】\L0iFYNF.png)

接下来我们来实际操作一下，从Nacos中拉取配置，上面我们已经在Nacos中配置时间的打印格式了：

1. 引入nacos-config依赖

   首先，在user-service服务中，引入nacos-config的客户端依赖。因为我们上面配置的就是userservice的管理：

   ```xml
   <!--nacos配置管理依赖-->
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
   </dependency>
   ```

2. 添加bootstrap.yaml

   然后，在user-service中添加一个bootstrap.yaml文件，内容如下：

   ```yml
   spring:
     application:
       name: userservice # 服务名称
     profiles:
       active: dev #开发环境，这里是dev 
     cloud:
       nacos:
         server-addr: localhost:8848 # Nacos地址
         config:
           file-extension: yaml # 文件后缀名
   ```

   这里会根据spring.cloud.nacos.server-addr获取nacos地址，再根据`${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}`作为文件id，来读取配置。本例中，就是去读取`userservice-dev.yaml`。

3. 删除重复的配置。将application中重复的配置删除掉。有：服务名称、Nacos地址、集群配置。

   > 备注：经测试，不删除好像也可以，但是最好删除吧。

4. 读取nacos配置，证明我们已经拉取到了Nacos的配置。

   在user-service中的UserController中添加业务逻辑，读取pattern.dateformat配置：

   ```java
   @Slf4j
   @RestController
   @RequestMapping("/user")
   public class UserController {
   
       @Autowired
       private UserService userService;
   
       @Value("${pattern.dateformat}")
       private String dateformat;
       
       @GetMapping("now")
       public String now(){
           return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
       }
       // ...略
   }
   ```

   在页面访问，可以看到效果：

   ![image-20210714170449612](D:\Java\笔记\图片\4-02【Nacos配置管理、Feign、Gateway】\image-20210714170449612.png)

这样是在Nacos中配置了管理了，但是如果我们在Nacos中修改配置后必须等我们重启之后才能够生效，不会立即生效的。这样根本无法实现配置的热更新。

## 5.2 配置热更新

我们最终的目的，是修改nacos中的配置后，微服务中无需重启即可让配置生效，也就是**配置热更新**。

要实现配置热更新，可以使用两种方式：

* 在`@Value`注入的变量所在类上添加注解`@RefreshScope`。
* 使用`@ConfigurationProperties`注解代替`@Value`注解。

这里我们重点讲解一下第二种方式，使用`@ConfigurationProperties`注解代替`@Value`注解。

1. 在user-service服务中，添加一个类，读取patterrn.dateformat属性：

   ```java
   package cn.itcast.user.config;
   
   @Component // 定义成为Bean
   @Data
   @ConfigurationProperties(prefix = "pattern") // 加一个前缀，前缀加上变量等于配置名称，就注入进去
   public class PatternProperties {
       private String dateformat;
   }
   ```

2. 在UserController中使用这个类代替@Value：

   ```java
   @Slf4j
   @RestController
   @RequestMapping("/user")
   public class UserController {
   
       @Autowired
       private UserService userService;
   
       // 自动装配进去
       @Autowired
       private PatternProperties patternProperties;
   
       @GetMapping("now")
       public String now(){
           return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternProperties.getDateformat()));
       }
   
       // 略
   }
   ```

## 5.3 配置共享

其实微服务启动时，会去nacos读取多个配置文件，例如：

- `[spring.application.name]-[spring.profiles.active].yaml`，例如：userservice-dev.yaml

- `[spring.application.name].yaml`，例如：userservice.yaml

而`[spring.application.name].yaml`不包含环境，因此可以被多个环境共享。

下面我们通过案例来测试配置共享

1. 添加一个环境共享配置

   我们在nacos中添加一个userservice.yaml文件：名称是userservice.yaml，配置内容设置为：

   ```yml
   pattern:
   	envSharedValue: 多环境共享属性值
   ```

2. 在user-service中读取共享配置

   在user-service服务中，修改PatternProperties类，读取新添加的属性：

   ```java
   @Component
   @Data
   @ConfigurationProperties(prefix = "pattern")
   public class PatternProperties {
       private String dateformat;
       private String envSharedValue;
   }
   ```

   在user-service服务中，修改UserController，添加一个方法：

   ```java
   @Slf4j
   @RestController
   @RequestMapping("/user")
   public class UserController {
   
       @Autowired
       private UserService userService;
   
       @Autowired
       private PatternProperties patternProperties;
   
       // 直接将patternProperties对象返回给浏览器 会以json格式展示
       @GetMapping("/pro")
       public PatternProperties pro() {
           return patternProperties;
       }
   	
       // 省略了一些方法
   }
   ```

3. 运行两个UserApplication，使用不同的profile环境来测试一下。

   UserApplication我们正常启动不用修改，使用dev开发环境。但是UserApplication2启动的时候我们修改一下环境，改成test测试环境，这样浏览器访问的时候对比一下。修改环境方法如下：右键、Edit Configuration、下滑找到Active profiles、填写`test`、apply、ok。

   这样，UserApplication(8081)使用的profile是dev，UserApplication2(8082)使用的profile是test。启动UserApplication和UserApplication2。结果如下：

   * 访问http://localhost:8081/user/pro，显示`{"dateformat":"yyy年MM月dd日 hh:mm:ss","envSharedValue":"多环境共享属性值"}`
   * 访问http://localhost:8082/user/pro，显示`{"dateformat":null,"envSharedValue":"多环境共享属性值"}`

   可以看出来，不管是dev，还是test环境，都读取到了envSharedValue这个属性的值。

4. 配置共享的优先级

   当nacos、服务本地同时出现相同属性时，优先级有高低之分：

![image-20210714174623557](D:\Java\笔记\图片\4-02【Nacos配置管理、Feign、Gateway】\image-20210714174623557.png)

## 5.4 搭建Nacos集群

Nacos生产环境下一定要部署为集群状态。

官方给出的Nacos集群图：

![image-20210409210621117](D:\Java\笔记\图片\4-02【Nacos配置管理、Feign、Gateway】\image-20210409210621117.png)

其中包含3个nacos节点，然后一个负载均衡器代理3个Nacos。这里负载均衡器可以使用nginx。

我们计划的集群结构：

![image-20210409211355037](D:\Bprogram\java\黑马程序员\第四阶段\第一篇\day02-SpringCloud02\资料\..\图片\4-02【Nacos配置管理、Feign、Gateway】\image-20210409211355037.png)

搭建集群的基本步骤：

- 搭建数据库，初始化数据库表结构
- 下载nacos安装包
- 配置nacos
- 启动nacos集群
- nginx反向代理

Nacos默认数据存储在内嵌数据库Derby中，不属于生产可用的数据库。

官方推荐的最佳实践是使用带有主从的高可用数据库集群，主从模式的高可用数据库可以参考**传智教育**的后续高手课程。

这里我们以单点的数据库为例来讲解。

1. 首先新建一个数据库，命名为nacos，而后导入下面的SQL：

   ```sql
   /******************************************/
   /* 
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     这两行代码会出错，CURRENT_TIMESTAMP不支持5.5版本的，可以升级到5.6.5版本。我们这里没有升级。只是删掉了有关这两行代码的sql脚本。
   */
   /******************************************/
   
   CREATE TABLE `config_info` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(255) DEFAULT NULL,
     `content` longtext NOT NULL COMMENT 'content',
     `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     `src_user` text COMMENT 'source user',
     `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
     `app_name` varchar(128) DEFAULT NULL,
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     `c_desc` varchar(256) DEFAULT NULL,
     `c_use` varchar(64) DEFAULT NULL,
     `effect` varchar(64) DEFAULT NULL,
     `type` varchar(64) DEFAULT NULL,
     `c_schema` text,
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_info_aggr   */
   /******************************************/
   CREATE TABLE `config_info_aggr` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(255) NOT NULL COMMENT 'group_id',
     `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
     `content` longtext NOT NULL COMMENT '内容',
     `gmt_modified` datetime NOT NULL COMMENT '修改时间',
     `app_name` varchar(128) DEFAULT NULL,
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';
   
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_info_beta   */
   /******************************************/
   CREATE TABLE `config_info_beta` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(128) NOT NULL COMMENT 'group_id',
     `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
     `content` longtext NOT NULL COMMENT 'content',
     `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
     `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     `src_user` text COMMENT 'source user',
     `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_info_tag   */
   /******************************************/
   CREATE TABLE `config_info_tag` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(128) NOT NULL COMMENT 'group_id',
     `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
     `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
     `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
     `content` longtext NOT NULL COMMENT 'content',
     `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     `src_user` text COMMENT 'source user',
     `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_tags_relation   */
   /******************************************/
   CREATE TABLE `config_tags_relation` (
     `id` bigint(20) NOT NULL COMMENT 'id',
     `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
     `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(128) NOT NULL COMMENT 'group_id',
     `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
     `nid` bigint(20) NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (`nid`),
     UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
     KEY `idx_tenant_id` (`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = group_capacity   */
   /******************************************/
   CREATE TABLE `group_capacity` (
     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
     `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
     `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
     `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
     `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
     `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
     `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_group_id` (`group_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = his_config_info   */
   /******************************************/
   CREATE TABLE `his_config_info` (
     `id` bigint(64) unsigned NOT NULL,
     `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
     `data_id` varchar(255) NOT NULL,
     `group_id` varchar(128) NOT NULL,
     `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
     `content` longtext NOT NULL,
     `md5` varchar(32) DEFAULT NULL,
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `src_user` text,
     `src_ip` varchar(50) DEFAULT NULL,
     `op_type` char(10) DEFAULT NULL,
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     PRIMARY KEY (`nid`),
     KEY `idx_gmt_create` (`gmt_create`),
     KEY `idx_gmt_modified` (`gmt_modified`),
     KEY `idx_did` (`data_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';
   
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = tenant_capacity   */
   /******************************************/
   CREATE TABLE `tenant_capacity` (
     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
     `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
     `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
     `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
     `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
     `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
     `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_tenant_id` (`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';
   
   
   CREATE TABLE `tenant_info` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `kp` varchar(128) NOT NULL COMMENT 'kp',
     `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
     `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
     `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
     `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
     `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
     `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
     KEY `idx_tenant_id` (`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';
   
   CREATE TABLE `users` (
   	`username` varchar(50) NOT NULL PRIMARY KEY,
   	`password` varchar(500) NOT NULL,
   	`enabled` boolean NOT NULL
   );
   
   CREATE TABLE `roles` (
   	`username` varchar(50) NOT NULL,
   	`role` varchar(50) NOT NULL,
   	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
   );
   
   CREATE TABLE `permissions` (
       `role` varchar(50) NOT NULL,
       `resource` varchar(255) NOT NULL,
       `action` varchar(8) NOT NULL,
       UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
   );
   
   INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);
   
   INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
   ```

   > 这里这一步我们出错了，因为我们的数据库版本是5.5的，有的默认值不支持。

2. 下载Nacos，虽然我们之前已经下载了，但是这里重新搞一下。将老师的文件解压缩，然后重命名为nacos1。

3. 配置Nacos。

   进入nacos的conf目录，修改配置文件`cluster.conf.example`，重命名为`cluster.conf`。

   然后将内容修改一下

   ```bash
   192.168.16.101:8847
   192.168.16.102
   192.168.16.103
   # 修改为如下代码：
   127.0.0.1:8845
   127.0.0.1.8846
   127.0.0.1.8847
   ```

   然后修改application.properties文件，添加数据库配置

   ```properties
   # 将数据源的注释打开 大概在33行左右
   spring.datasource.platform=mysql  
   
   # 数据库数量为1 注释打开 大概在36行左右
   db.num=1
   
   # 数据库的连接地址 用户名称 密码 注释打开 大概在39~41行左右
   db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
   db.user.0=root
   db.password.0=root
   ```

4. 启动

   将nacos文件夹再复制两份，分别命名为：nacos2、nacos3。这样我们就有了三个Nacos了。 

   然后分别修改三个文件夹中的application.properties，nacos1：`server.port=8845`、nacos2：`server.port=8846`、nacos3：`server.port=8847`。大概在21行。

   然后分别进入他们的bin目录，启动三个nacos节点：`startup.cmd`。

5. nginx反向代理

   我们下载到了E:\nginx。修改conf/nginx.conf文件，添加配置到http代码块里面，如下：

   ```nginx
   upstream nacos-cluster {
       server 127.0.0.1:8845;
   	server 127.0.0.1:8846;
   	server 127.0.0.1:8847;
   }
   
   server {
       listen       80;
       server_name  localhost;
   
       location /nacos {
           proxy_pass http://nacos-cluster;
       }
   }
   ```

   而后在浏览器访问：http://localhost/nacos即可，这时候需要重新登录了就。

6. Java代码配置

   代码中bootstarp.yml文件修改配置如下：

   ```yml
   spring:
     cloud:
       nacos:
         server-addr: localhost:80 # Nacos地址 将nacos地址修改一下
   ```

   启动项目，就会在nacos中看到注册的实例了。

   但是如果我们如果新增加一个配置的话是不会成功的，这是因为版本不对应，我们的nacos是1.4.1的，而mysql是5.5。nacos1.4.1对应的mysql版本是8.0以上。这里就不更换了，懒得搞了。

优化：

- 实际部署时，需要给做反向代理的nginx服务器设置一个域名，这样后续如果有服务器迁移nacos的客户端也无需更改配置.

- Nacos的各个节点应该部署到多个不同服务器，做好容灾和隔离
