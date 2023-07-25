# 第一章 消息队列

<!-- 栈push pop peek、队列offer poll peek -->

消息队列 Message Queue，经常缩写为 MQ。从字面上来理解，消息队列是一种用来存储消息的队列。

```java
// 1. 创建一个保存字符串的队列
Queue<String> stringQueue = new LinkedList<String>();
// 2. 往消息队列中放入消息
stringQueue.offer("hello");
// 3. 从消息队列中取出消息并打印
System.out.println(stringQueue.poll());
```

上述代码创建了一个队列，先往队列中添加了一个消息，然后又从队列中取出了一个消息。这说明了队列是可以用来存取消息的。消息队列就是将需要传输的数据存放在队列中。

## 1.1 消息队列中间件

消息队列中间件就是用来存储消息的软件（组件）。举个例子来理解，为了分析网站的用户行为，我们需要记录用户的访问日志。这些一条条的日志，可以看成是一条条的消息，我们可以将它们保存到消息队列中。将来有一些应用程序需要处理这些日志，就可以随时将这些消息取出来处理。

## 1.2 消息队列应用场景

消息队列主要有着四种应用场景：解耦、异步、削峰、日志处理。

**系统解耦**

秒杀系统中用户下单之后流程进入「订单系统」，「订单系统」需要保存用户的订单信息，这时候会调用「库存系统」的接口，让其减少库存。

可是这时候如果「库存系统」出现问题，那么会导致「订单系统」下单失败，而且如果库存系统接口修改了，这时候也会导致订单系统无法工作。

使用消息队列可以实现系统与系统之间的解耦，「订单系统」不再调用库存系统的接口，而是把订单消息写入到消息队列中。「库存系统」从消息队列中拉取消息，然后再减去库存，从而实现系统解耦。

**异步处理**

电商网站中，新的用户注册时，需要将用户的信息保存到数据库中，同时还需要额外发送注册的邮件通知、以及短信注册码给用户。但因为发送邮件、发送注册短信需要连接外部的服务器，需要额外等待一段时间，此时，就可以使用消息队列来进行异步处理，从而实现快速响应。

![](D:\Java\笔记\图片\2-13【Kafka】\1-1asynchronous advantage.png)

**流量削峰**

秒杀活动一般会因为流量过大，导致流量暴增，应用挂掉。为解决这个问题，一般需要在应用前端加入消息队列。这样可以控制活动的人数，可以缓解短时间内高流量压垮应用。

服务器接收用户请求之后，首先写入消息队列。假如消息队列长度超过最大数量，则直接抛弃用户请求或跳转到错误页面。秒杀业务根据消息队列中的请求信息，再做后续处理

**日志处理**

大型电商网站（淘宝、京东、国美、苏宁...）、App（抖音、美团、滴滴等）等需要分析用户行为，要根据用户的访问行为来发现用户的喜好以及活跃情况，需要在页面上收集大量的用户访问信息。

日志处理是指将消息队列用在日志处理中，比如Kafka的应用，解决大量日志传输的问题。

## 1.3 生产者与消费者

Java 服务器端开发的交互模型 http 请求与响应，而基于消息队列来编程，此时便是生产者与消费者模型。

![](D:\Java\笔记\图片\2-13【Kafka】\1-2producer and consumer.png)

## 1.4 消息队列两种模式

消息队列有两种模式：点对点模式、发布/订阅模式。

### 1.4.1 点对点模式

消息发送者生产消息发送到消息队列中，然后消息接收者从消息队列中取出并且消费消息。消息被消费以后，消息队列中不再有存储，所以消息接收者不可能消费到已经被消费的消息。

![](D:\Java\笔记\图片\2-13【Kafka】\1-3peer-to-peer mode.png)

点对点模式特点：

* 每个消息只有一个接收者 Consumer，即一旦被消费，消息就不再在消息队列中。
* 发送者和接收者间没有依赖性，发送者发送消息之后，不管有没有接收者在运行，都不会影响到发送者下次发送消息；
* 接收者在成功接收消息之后需向队列应答成功，以便消息队列删除当前接收的消息；

### 1.4.2 发布/订阅模式

![](D:\Java\笔记\图片\2-13【Kafka】\1-4publish or subscribe model.png)

发布/订阅模式特点：

* 每个消息可以有多个订阅者；
* 发布者和订阅者之间有时间上的依赖性。针对某个主题（Topic）的订阅者，它必须创建一个订阅者之后，才能消费发布者的消息。
* 为了消费消息，订阅者需要提前订阅该角色主题，并保持在线运行；

# 第二章 Kafka基础

<!--  Publish and subscribe:发布与订阅； Store:存储； Process:处理 -->

kafka 的诞生，是为了解决 linkedin 的数据管道问题，起初 linkedin 采用了 ActiveMQ 来进行数据交换。大约是在2010 年前后，那时的 ActiveMQ 还远远无法满足 linkedin 对数据传递系统的要求，经常由于各种缺陷而导致消息阻塞或者服务无法正常访问，为了能够解决这个问题，linkedin 决定研发自己的消息传递系统，当时 linkedin 的首席架构师jay kreps便开始组织团队进行消息传递系统的研发。

[Kafka](http://kafka.apache.org/) 是由 Apache 软件基金会开发的一个开源流平台，由 Scala 和 Java 编写。Apache Kafka是一个分布式流平台。一个分布式的流平台应该包含3点关键的能力：  

1. 发布和订阅流数据流，类似于消息队列或者是企业消息传递系统
2. 以容错的持久化方式存储数据流  
3. 处理数据流

|           |       RabbitMQ       |           ActiveMQ            |  RocketMQ  |   Kafka    |
| --------- | :------------------: | :---------------------------: | :--------: | :--------: |
| 公司/社区 |        Rabbit        |            Apache             |    阿里    |   Apache   |
| 开发语言  |        Erlang        |             Java              |    Java    | Scala&Java |
| 支持协议  | AMQP XMPP SMTP STOMP | OpenWire STOMP REST XMPP AMQP | 自定义协议 | 自定义协议 |
| 可用性    |          高          |             一般              |     高     |     高     |
| 吞吐量    |         一般         |              差               |     高     |   非常高   |
| 延迟      |        微秒级        |            毫秒级             |   毫秒级   |  毫秒以内  |
| 可靠性    |          高          |             一般              |     高     |    一般    |

## 2.1 Linux 集群部署

Linux 上面使用的 Kafka 版本为 2.4.1，是2020年3月12日发布的版本。可以注意到 Kafka 的版本号为 kafka_2.12-2.4.1，这是因为kafka主要是使用 scala 语言开发的，2.12 为 scala的版本号。

将Kafka的安装包上传到虚拟机，并解压

```sh
[root@node1 ~]# cd /export/software/
[root@node1 software]# pwd
/export/software
[root@node1 software]# ll
total 60900
-rw-r--r-- 1 root root 62358954 Jul 21 16:28 kafka_2.12-2.4.1.tgz
[root@node1 software]# tar -zxvf kafka_2.12-2.4.1.tgz -C ../server/
[root@node1 software]# cd ../server/
[root@node1 server]# ll
total 0
drwxr-xr-x 6 root root 89 Mar  3  2020 kafka_2.12-2.4.1
```

修改 server.properties

```sh
# 查看配置的主机别名
[root@node1 data]# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.88.151 node1 node1.linxuan.com
192.168.88.152 node2 node2.linxuan.com
192.168.88.153 node3 node3.linxuan.com
[root@node1 data]# cd /export/server/kafka_2.12-2.4.1/config
[root@node1 data]# vim server.properties
# 指定broker的id，每一个集群中的Kafa都要有不同的broker，因此一会要更换node2和node3的broker.id
broker.id=0
# 指定Kafka数据的位置，直接添加即可，最后会在/export/server/kafka_2.12-2.4.1下创建data目录
log.dirs=/export/server/kafka_2.12-2.4.1/data
# 配置zk的三个节点，这个老师并没有添加，但是课件里面有
zookeeper.connect=node1.linxuan.com:2181,node2.linxuan.com:2181,node3.linxuan.com:2181
```

将安装好的 kafka 复制到另外两台服务器

```sh
[root@node1 config]# cd /export/server
[root@node1 server]# scp -r kafka_2.12-2.4.1/ node2.linxuan.com:$PWD
[root@node1 server]# scp -r kafka_2.12-2.4.1/ node3.linxuan.com:$PWD

# 修改另外两个节点的broker.id分别为1和2
# ---------node2.linxuan.com--------------
[root@node2 server]# cd /export/server/kafka_2.12-2.4.1/config
[root@node2 config]# vim server.properties 
broker.id=1

# --------node3.linxuan.com--------------
[root@node2 server]# cd /export/server/kafka_2.12-2.4.1/config
[root@node2 config]# vim server.properties
broker.id=2
```

配置 KAFKA_HOME 环境变量

```sh
[root@node1 server]# vim /etc/profile
export KAFKA_HOME=/export/server/kafka_2.12-2.4.1
export PATH=:$PATH:${KAFKA_HOME}

# 分发到各个节点
# 进入到/etc目录，这样分发后的文件也会在/etc目录下面
[root@node1 etc]# cd /etc/
[root@node1 etc]# scp /etc/profile node2.linxuan.com:$PWD
[root@node1 etc]# scp /etc/profile node3.linxuan.com:$PWD

# 每个节点加载环境变量
[root@node1 etc]# source /etc/profile
[root@node2 server]# source /etc/profile
[root@node3 server]# source /etc/profile
```

启动服务器

```sh
# 现在应该启动服务器了，但是启动之前需要启动Zookeeper，启动ZK集群
[root@node1 config]# /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh start
[root@node2 config]# /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh start
[root@node3 config]# /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh start
```

想要看懂shell脚本，先去学shell

## 2.2 Windows 单机部署

Windows 上面使用的是2.8.0，这里我下载到了`E:\Kafka`目录下面

```properties
# 编辑config目录下的server.properties，在E:\Kafka下面创建了一个log文件夹
log.dirs=E:\Kafka\log
zookeeper.connect=localhost:2181

# 编辑config目录下的zookeeper.properties，在E:\Kafka下面创建了一个zookeeperLog文件夹
dataDir=E:\Kafka\zookeeperLog
clientPort=2181
```

```sh
# 启动zookeeper，在文件夹kafka_2.12-2.8.0中输入cmd,接着输入命令
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
# 启动kafka，在文件夹kafka_2.12-2.8.0中输入cmd,接着输入命令
bin\windows\kafka-server-start.bat .\config\server.properties
```

