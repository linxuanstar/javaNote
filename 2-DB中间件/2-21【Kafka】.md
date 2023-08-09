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

Linux 上面使用的 Kafka 版本为 2.4.1，是2020年3月12日发布的版本。可以注意到 Kafka 的版本号为 kafka_2.12-2.4.1，这是因为 Kafka 主要是使用 scala 语言开发的，2.12 为 scala 的版本号。

将 Kafka 的安装包上传到虚拟机，并解压

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
# 查看配置的主机别名，这里需要注意三台主机都要配置主机别名
[root@node1 data]# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.88.151 node1 node1.linxuan.com
192.168.88.152 node2 node2.linxuan.com
192.168.88.153 node3 node3.linxuan.com
# node2配置好了
[root@node2 server]# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.88.151 node1 node1.linxuan.com
192.168.88.152 node2 node2.linxuan.com
192.168.88.153 node3 node3.linxuan.com
# node3配置好了
[root@node3 server]# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.88.151 node1 node1.linxuan.com
192.168.88.152 node2 node2.linxuan.com
192.168.88.153 node3 node3.linxuan.com
# 重新进入node1操作
[root@node1 data]# cd /export/server/kafka_2.12-2.4.1/config
[root@node1 data]# vim server.properties
# 指定broker的id，每一个集群中的Kafa都要有不同的broker，因此一会要更换node2和node3的broker.id
broker.id=1
# 指定Kafka数据的位置，直接添加即可，最后会在/export/server/kafka_2.12-2.4.1下创建data目录
log.dirs=/export/server/kafka_2.12-2.4.1/data
# 配置zk的三个节点，这个老师并没有添加，但是课件里面有。因此，这里我就添加了
zookeeper.connect=node1.linxuan.com:2181,node2.linxuan.com:2181,node3.linxuan.com:2181
```

将安装好的 kafka 复制到另外两台服务器

```sh
[root@node1 config]# cd /export/server
[root@node1 server]# scp -r kafka_2.12-2.4.1/ node2.linxuan.com:$PWD
[root@node1 server]# scp -r kafka_2.12-2.4.1/ node3.linxuan.com:$PWD

# 修改另外两个节点的broker.id分别为2和3
# ---------node2.linxuan.com--------------
[root@node2 server]# cd /export/server/kafka_2.12-2.4.1/config
[root@node2 config]# vim server.properties 
broker.id=2

# --------node3.linxuan.com--------------
[root@node2 server]# cd /export/server/kafka_2.12-2.4.1/config
[root@node2 config]# vim server.properties
broker.id=3
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

启动 zookeeper

```sh
[root@node1 server]# pwd
/export/server
# 制作一键启动脚本叫目录
[root@node1 server]# mkdir onekey
# ZK集群一键启动脚本
[root@node1 onekey]# vim onekey/zkStart.sh 
#!/bin/sh
for host in node1 node2 node3
do
    ssh $host "source /etc/profile; /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh start"
    echo "$host zookeeper 启动~"
    echo "------------------------------"
done
# 添加执行权限
[root@node1 server]# chomod 744 onekey/zkStart.sh
# 创建关闭脚本
[root@node1 server]# vim onekey/zkStop.sh
#!/bin/sh
for host in node1 node2 node3
do
    ssh $host "source /etc/profile; /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh stop"
    echo "$host zookeeper 关闭"
    echo "------------------------------"
done
# 添加执行权限
[root@node1 server]# chmod 744 onekey/zkStop.sh
# 执行ZK启动脚本
[root@node1 server]# ./onekey/zkStart.sh 

# 查看三个节点的ZK是否启动
[root@node1 server]# ./apache-zookeeper-3.5.6-bin/bin/zkServer.sh status
/export/server/jdk1.8.0_144/bin/java
ZooKeeper JMX enabled by default
Using config: /export/server/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost.
Mode: follower
[root@node2 server]# ./apache-zookeeper-3.5.6-bin/bin/zkServer.sh status
/export/server/jdk1.8.0_144/bin/java
ZooKeeper JMX enabled by default
Using config: /export/server/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost.
Mode: follower
[root@node3 server]# ./apache-zookeeper-3.5.6-bin/bin/zkServer.sh status
/export/server/jdk1.8.0_144/bin/java
ZooKeeper JMX enabled by default
Using config: /export/server/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost.
Mode: leader
```

启动 Kafka 与测试

```sh
# node1启动
[root@node1 server]# cd /export/server/kafka_2.12-2.4.1
[root@node1 kafka_2.12-2.4.1]# nohup bin/kafka-server-start.sh config/server.properties &
# node2启动
[root@node2 server]# cd /export/server/kafka_2.12-2.4.1
[root@node2 kafka_2.12-2.4.1]# nohup bin/kafka-server-start.sh config/server.properties &
# node3启动
[root@node3 server]# cd /export/server/kafka_2.12-2.4.1
[root@node4 kafka_2.12-2.4.1]# nohup bin/kafka-server-start.sh config/server.properties &
# 测试Kafka集群是否启动成功，只要不报错就代表启动成功
[root@node1 kafka_2.12-2.4.1]# bin/kafka-topics.sh --bootstrap-server node1.linxuan.com:9092 --list
```

编写 Kafka 一键启动与关闭脚本

```sh
[root@node1 server]# pwd
/export/server
# 准备slave配置文件，用于保存要启动哪几个节点上的kafka
[root@node1 server]# vim onekey/slave
node1.linxuan.com
node2.linxuan.com
node3.linxuan.com
# 编写start-kafka.sh脚本
[root@node1 server]# vim onekey/start-kafka.sh
cat /export/server/onekey/slave | while read line
do
{
    # export JMX_PORT=9988;：做监控的时候会用到，监控调试。把端口打开
    # >/dev/nul*：标准输出重定向到空设备文件，也就是不输出任何信息到终端，说白了就是不显示任何信息
    # 2>&1：2表示stderr标准错误、&表示等同于、1表示stdout标准输出。标准错误输出重定向（等同于）标准输出
    echo $line
    ssh $line "source /etc/profile;export JMX_PORT=9988;nohup ${KAFKA_HOME}/bin/kafka-server-start.sh  ${KAFKA_HOME}/config/server.properties >/dev/nul* 2>&1 & " &
}&
# wait是在等待上一批或上一个脚本执行完（即上一个的进程终止），再执行wait之后的命令。上一个脚本结尾需要加上&
wait
done
# 编写stop-kafka.sh脚本
[root@node1 server]# vim onekey/stop-kafka.sh
cat /export/server/onekey/slave | while read line
do
{
    echo $line
    # xargs是给命令传递参数的一个过滤器，也是组合多个命令的一个工具
    ssh $line "source /etc/profile; jps | grep Kafka | cut -d ' ' -f 1 | xargs kill -s 9"
}&
wait
done
# 给start-kafka.sh、stop-kafka.sh配置执行权限
[root@node1 server]# chmod u+x onekey/start-kafka.sh
[root@node1 server]# chmod u+x onekey/stop-kafka.sh
# 执行一键启动和一键关闭
[root@node1 server]# ./onekey/start-kafka.sh
[root@node1 server]# ./onekey/stop-kafka.sh
```

| 启动与关闭过程                | 命令                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| 一键脚本启动 ZK 集群          | /export/server/onekey/zkStart.sh                             |
| 查询本节点 ZK 是否启动成功    | /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh status |
| 一键启动 Kafka 集群           | /export/server/onekey/start-kafka.sh                         |
| 验证本节点 Kafka 是否启动成功 | jps 命令查看 Java 进程是否有 Kafka                           |
| 一键关闭 Kafka 集群           | /export/server/onekey/stop-kafka.sh                          |
| 一键关闭 ZK 集群              | /export/server/onekey/zkStop.sh                              |

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

## 2.3 目录结构分析

| 目录名称  | 说明                                                         |
| --------- | ------------------------------------------------------------ |
| bin       | Kafka的所有执行脚本都在这里。例如：启动Kafka服务器、创建Topic、生产者、消费者程序等等 |
| config    | Kafka的所有配置文件                                          |
| libs      | 运行Kafka所需要的所有JAR包                                   |
| logs      | Kafka的所有日志文件，如果Kafka出现一些问题，需要到该目录中去查看异常信息 |
| site-docs | Kafka的网站帮助文件                                          |
