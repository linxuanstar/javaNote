# 第一章 Kafka基础

<!--  Publish and subscribe:发布与订阅； Store:存储； Process:处理 -->

kafka 的诞生，是为了解决 Linkedin 的数据管道问题，起初 Linkedin 采用了 ActiveMQ 来进行数据交换。大约是在2010 年前后，那时的 ActiveMQ 还远远无法满足 Linkedin 对数据传递系统的要求，经常由于各种缺陷而导致消息阻塞或者服务无法正常访问，为了能够解决这个问题，Linkedin 决定研发自己的消息传递系统，当时 Linkedin 的首席架构师jay kreps便开始组织团队进行消息传递系统的研发。

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

## 1.1 Kafka消息队列两种模式

Kafka 消息队列有两种模式：点对点模式、发布/订阅模式。

### 1.1.1 点对点模式

消息发送者生产消息发送到消息队列中，然后消息接收者从消息队列中取出并且消费消息。消息被消费以后，消息队列中不再有存储，所以消息接收者不可能消费到已经被消费的消息。

![](..\图片\2-21【Kafka】\1-1peer-to-peer mode.png)

点对点模式特点：

* 每个消息只有一个接收者 Consumer，即一旦被消费，消息就不再在消息队列中。
* 发送者和接收者间没有依赖性，发送者发送消息之后，不管有没有接收者在运行，都不会影响到发送者下次发送消息；
* 接收者在成功接收消息之后需向队列应答成功，以便消息队列删除当前接收的消息；

### 1.1.2 发布/订阅模式

![](..\图片\2-21【Kafka】\1-2publish or subscribe model.png)

发布 / 订阅模式特点：

* 每个消息可以有多个订阅者；
* 发布者和订阅者之间有时间上的依赖性。针对某个主题（Topic）的订阅者，它必须创建一个订阅者之后，才能消费发布者的消息。
* 为了消费消息，订阅者需要提前订阅该角色主题，并保持在线运行；

## 1.2 Linux 集群部署

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

| 目录名称  | 说明                                                         |
| --------- | ------------------------------------------------------------ |
| bin       | Kafka的所有执行脚本都在这里。例如：启动Kafka服务器、创建Topic、生产者、消费者程序等等 |
| config    | Kafka的所有配置文件                                          |
| libs      | 运行Kafka所需要的所有JAR包                                   |
| logs      | Kafka的所有日志文件，如果Kafka出现一些问题，需要到该目录中去查看异常信息 |
| site-docs | Kafka的网站帮助文件                                          |

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

```sh
# 关闭Linux三个node节点
[root@node1 ~]# vim nodeShutdown.sh 
#!/bin/sh
for host in node2 node3 node1
do
    ssh $host "source /etc/profile; init 0"
    echo "$host 关闭"
    echo "------------------------------"
done
[root@node1 ~]# chmod 744 ./nodeShutdown.sh 
```

| 启动与关闭过程                | 命令                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| 一键脚本启动 ZK 集群          | /export/server/onekey/zkStart.sh                             |
| 查询本节点 ZK 是否启动成功    | /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh status |
| 一键启动 Kafka 集群           | /export/server/onekey/start-kafka.sh                         |
| 验证本节点 Kafka 是否启动成功 | jps 命令查看 Java 进程是否有 Kafka                           |
| 一键关闭 Kafka 集群           | /export/server/onekey/stop-kafka.sh                          |
| 一键关闭 ZK 集群              | /export/server/onekey/zkStop.sh                              |
| 一键关闭Linux集群             | /root/nodeShutdown.sh                                        |

## 1.3 Windows 单机部署

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

## 1.4 生产与消费基础操作

![](..\图片\2-21【Kafka】\1-3producer and consumer.png)

Kafka 中所有的消息都是保存在主题中，要生产消息到 Kafka，首先必须要有一个确定的主题。

```sh
# 当前在kafka的bin目录下面
[root@node1 bin]# pwd
/export/server/kafka_2.12-2.4.1/bin
# 创建名为test的主题
[root@node1 bin]# ./kafka-topics.sh --create --bootstrap-server node1.linxuan.com:9092 --topic test
# 查看目前Kafka中的主题
[root@node1 bin]# ./kafka-topics.sh --list --bootstrap-server node1.linxuan.com:9092
test
```

创建好主题之后接下来就是生产者生产消息、消费者消费消息了。Kafka 内置了一些测试程序供我们生产消息和消费消息，接下来测试一下

```sh
# 当前在kafka的bin目录下面
[root@node1 bin]# pwd
/export/server/kafka_2.12-2.4.1/bin
# 生产消息到Kafka的test主题 (这个命令在node1节点的一个控制台窗口输入，另一个命令克隆一个窗口输入)
[root@node1 bin]# ./kafka-console-producer.sh --broker-list node1.linxuan.com:9092 --topic test
# 消费 test 主题中的消息 (该命令负责消费，所以重新克隆一个窗口)
[root@node1 bin]# ./kafka-console-consumer.sh --bootstrap-server node1.linxuan.com:9092 --topic test --from-beginning
```

## 1.5 Kafka Tools工具

Kafka Tools 是一款带有可视化页面的 Kafka 工具，现在该软件已经更名为 Offeset Exploer。我们将其下载至本地Windows 环境的`E:\Kafka Tools`

想要使用它连接虚拟机上面的 Kafka 集群，首先要在 Windows 本地配置好集群的 IP 地址。

```properties
# C:\Windows\System32\drivers\etc目录下面的hosts文件添加如下 IP + 别名
192.168.88.151     node1
192.168.88.152     node2
192.168.88.153     node3
```

然后输入对应的配置信息即可

![](..\图片\2-21【Kafka】\1-4Kafka Tools.png)

## 1.6 Kafka 架构及概念

<!-- broker:中间人、中间商； -->

**broker**

* 一个 Kafka 的集群通常由多个 broker 组成，这样才能实现负载均衡以及容错。broker 就是一个 Kafka 进程 
* broker 是无状态（Sateless）的，它们是通过 ZooKeeper 来维护集群状态
* 一个 Kafka 的 broker 每秒可以处理数十万次读写，每个 broker 都可以处理 TB 消息而不影响性能

![](..\图片\2-21【Kafka】\1-5Kafka broker.png)

**zookeeper**

- ZK 用来管理和协调 broker，并且存储了 Kafka 的元数据（例如有多少 topic、partition、consumer）
- ZK 服务用于通知生产者和消费者在 Kafka 集群中有新的 broker 加入、或者 Kafka 集群中出现故障的 broker。

Kafka 正在逐步想办法将 ZooKeeper 剥离，维护两套集群成本较高，社区提出 KIP-500 就是要替换掉 ZooKeeper 的依赖。“Kafka on Kafka”——Kafka 自己来管理自己的元数据，我们在 Windows 部署 Kafka 已经无需 ZK 了。

**consumer（消费者）**

* 消费者负责从 broker 的 topic 中拉取数据，并自己进行处理

**consumer group（消费者组）**

* consumer group 是 kafka 提供的可扩展且具有容错性的消费者机制
* 一个消费者组有一个唯一的ID（group Id）且可以包含多个消费者，组内的消费者一起消费主题的所有分区数据

![](..\图片\2-21【Kafka】\1-6consumer group.png)

**分区（Partitions）**

* 在 Kafka 集群中，主题被分为多个分区

![](..\图片\2-21【Kafka】\1-7partitions.png)

**主题（Topic）**

- 主题是一个逻辑概念，用于生产者发布数据，消费者拉取数据
- Kafka 中的主题必须要有标识符，而且是唯一的，Kafka 中可以有任意数量的主题，没有数量上的限制
- 在主题中的消息是有结构的，一般一个主题包含某一类消息
- 一旦生产者发送消息到主题中，这些消息就不能被更新（更改）

![](..\图片\2-21【Kafka】\1-8topic.png)

**偏移量（offset）**

- offset 记录着下一条将要发送给 Consumer 的消息的序号，默认 Kafka 将 offset 存储在 ZooKeeper 中
- 在一个分区中，消息是有顺序的方式存储着，每个在分区的消费都是有一个递增的 id。这个就是偏移量 offset
- 偏移量在分区中才是有意义的。在分区之间，offset 是没有任何意义的

![](..\图片\2-21【Kafka】\1-9offset.png)

**副本（Replicas）**

* 副本可以确保某个服务器出现故障时，确保数据依然可用 
* 在 Kafka 中，一般都会设计副本的个数 ＞1

![](..\图片\2-21【Kafka】\1-10replicas.png)

## 1.7 监控工具Kafka-eagle

[Kafka Eagle](https://www.kafka-eagle.org/)是一款结合了目前大数据 Kafka 监控工具的特点，重新研发的一块开源免费的 Kafka 集群优秀的监控工具。它可以非常方便的监控生产环境中的 offset、lag 变化、partition 分布、owner等。

**开启Kafka JMX端口**

JMX（Java Management Extensions）是一个为应用程序植入管理功能的框架。JMX是一套标准的代理和服务，实际上，用户可以在任何Java应用程序中使用这些代理和服务实现管理。很多的一些软件都提供了JMX接口，来实现一些管理、监控功能。

```sh
# 开启Kafka JMX，这个脚本里面已经写好了
[root@node1 ~]# /export/server/onekey/start-kafka.sh
```

如果 JMX 开启无误那么可以在本机使用 JConsole 来连接远程的 Kafka

```sh
# 想要使用本机Jconsole连接远程的Kafka，首先需要设置变量JMX_PORT=9988，脚本里面已经设置好了
# 接下来就是修改kafka-run-class.sh文件的KAFKA_JMX_OPTS了
[root@node1 kafka_2.12-2.4.1]# pwd
/export/server/kafka_2.12-2.4.1
# 在该文件的KAFKA_JMX_OPTS里面添加-Djava.rmi.server.hostname=192.168.88.151
[root@node1 kafka_2.12-2.4.1]# vim bin/kafka-run-class.sh
# JMX settings
if [ -z "$KAFKA_JMX_OPTS" ]; then
  KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmx remote.ssl=false -Djava.rmi.server.hostname=192.168.88.151"
fi
# 接着重启Kafka即可，一键关闭然后一键启动
[root@node1 kafka_2.12-2.4.1]# /export/server/onekey/stop-kafka.sh
[root@node1 kafka_2.12-2.4.1]# /export/server/onekey/start-kafka.sh
```

之后就可以在本地使用 JConsole 来连接 Kafka 了

```sh
# 直接在命令行敲jconsole即可打开JConsole。使用远程进程，输入192.168.88.151:9988即可连接成功(无需用户名密码)
C:\Windows\System32>jconsole
```

**安装MySQL**

```sh
# 查询当前系统中安装的名称带有mysql的软件。如果当前系统中已经安装有MySql数据库，安装将会失败
[root@node1 software]# rpm -qa | grep mysql
# 查询当前系统中安装的名称带有mariadb的软件。Centos7自带mariadb，与MySql数据库冲突。
[root@node1 software]# rpm -qa | grep mariadb
# 如果有冲突的那么就卸载软件
[root@node1 software]# rpm -e --nodeps 软件名称

# 上传文件至Linux
[root@node1 software]# ll
total 533040
-rw-r--r-- 1 root root 545832960 Aug 18 23:48 mysql-5.7.29-1.el7.x86_64.rpm-bundle.tar
# 解压，注意使用的参数不要加上-z
[root@node1 software]# tar -xvf mysql-5.7.29-1.el7.x86_64.rpm-bundle.tar 
mysql-community-embedded-devel-5.7.29-1.el7.x86_64.rpm
mysql-community-test-5.7.29-1.el7.x86_64.rpm
mysql-community-embedded-5.7.29-1.el7.x86_64.rpm
mysql-community-embedded-compat-5.7.29-1.el7.x86_64.rpm
mysql-community-libs-5.7.29-1.el7.x86_64.rpm
mysql-community-client-5.7.29-1.el7.x86_64.rpm
mysql-community-server-5.7.29-1.el7.x86_64.rpm
mysql-community-devel-5.7.29-1.el7.x86_64.rpm
mysql-community-libs-compat-5.7.29-1.el7.x86_64.rpm
mysql-community-common-5.7.29-1.el7.x86_64.rpm

# 安装依赖
[root@node1 software]# yum -y install libaio
# 正式安装，发现报错。这是由于yum安装了旧版本的GPG keys造成的，后面加上 --force --nodeps即可
[root@node1 software]# rpm -ivh mysql-community-common-5.7.29-1.el7.x86_64.rpm mysql-community-libs-5.7.29-1.el7.x86_64.rpm mysql-community-client-5.7.29-1.el7.x86_64.rpm mysql-community-server-5.7.29-1.el7.x86_64.rpm
warning: mysql-community-common-5.7.29-1.el7.x86_64.rpm: Header V3 DSA/SHA1 Signature, key ID 5072e1f5: NOKEY
error: Failed dependencies:
        mariadb-libs is obsoleted by mysql-community-libs-5.7.29-1.el7.x86_64
[root@node1 software]# rpm -ivh mysql-community-common-5.7.29-1.el7.x86_64.rpm mysql-community-libs-5.7.29-1.el7.x86_64.rpm mysql-community-client-5.7.29-1.el7.x86_64.rpm mysql-community-server-5.7.29-1.el7.x86_64.rpm  --force --nodeps
warning: mysql-community-common-5.7.29-1.el7.x86_64.rpm: Header V3 DSA/SHA1 Signature, key ID 5072e1f5: NOKEY
Preparing...                          ################################# [100%]
Updating / installing...
   1:mysql-community-common-5.7.29-1.e################################# [ 25%]
   2:mysql-community-libs-5.7.29-1.el7################################# [ 50%]
   3:mysql-community-client-5.7.29-1.e################################# [ 75%]
   4:mysql-community-server-5.7.29-1.e################################# [100%]

# 开始初始化设置
# 初始化
[root@node1 software]# mysqld --initialize
# 更改所属组
[root@node1 software]# chown mysql:mysql /var/lib/mysql -R
# 启动MySQL
[root@node1 software]# systemctl start mysqld.service
# 查看生成的临时root密码
[root@node1 software]# cat  /var/log/mysqld.log | grep root@localhost:
2023-08-21T01:02:23.311274Z 1 [Note] A temporary password is generated for root@localhost: 5ReV4fwpq;H-

# 进入MySQL
[root@node1 software]# mysql -u root -p
Enter password: # 这里输入在日志中生成的临时密码
# 更新root密码  设置为123456
mysql> alter user user() identified by "123456";
Query OK, 0 rows affected (0.00 sec)
# 切换数据库更新权限
mysql> use mysql;
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
Query OK, 0 rows affected, 1 warning (0.00 sec)
# 权限刷新
mysql> FLUSH PRIVILEGES;
Query OK, 0 rows affected (0.00 sec)
# 退出
mysql> exit
Bye

# 设置为开机自启动服务
[root@node1 software]# systemctl enable  mysqld
# 查看是否已经设置自启动成功
[root@node1 software]# systemctl list-unit-files | grep mysqld
mysqld.service                                enabled 
mysqld@.service                               disabled
```

**安装Kafka-Eagle**

```sh
[root@node1 ~]# cd /export/software/
[root@node1 software]# ll
total 82976
-rw-r--r-- 1 root root  9230052 May 19  2021 apache-zookeeper-3.5.6-bin.tar.gz
-rw-r--r-- 1 root root 75734959 Aug 18 23:27 kafka-eagle-bin-1.4.5.tar.gz
[root@node1 software]# tar -zxvf kafka-eagle-bin-1.4.5.tar.gz -C ../server/
kafka-eagle-bin-1.4.5/
kafka-eagle-bin-1.4.5/kafka-eagle-web-1.4.5-bin.tar.gz
[root@node1 software]# cd ../server/kafka-eagle-bin-1.4.5/
[root@node1 kafka-eagle-bin-1.4.5]# ll
total 73952
-rw-rw-r-- 1 root root 75722791 Mar 21  2020 kafka-eagle-web-1.4.5-bin.tar.gz
[root@node1 kafka-eagle-bin-1.4.5]# tar -zxvf kafka-eagle-web-1.4.5-bin.tar.gz 
[root@node1 kafka-eagle-bin-1.4.5]# ll
total 73952
drwxr-xr-x 8 root root       74 Aug 18 23:30 kafka-eagle-web-1.4.5
-rw-rw-r-- 1 root root 75722791 Mar 21  2020 kafka-eagle-web-1.4.5-bin.tar.gz
[root@node1 kafka-eagle-bin-1.4.5]# cd kafka-eagle-web-1.4.5/
[root@node1 kafka-eagle-web-1.4.5]# pwd
/export/server/kafka-eagle-bin-1.4.5/kafka-eagle-web-1.4.5

# 配置 kafka_eagle 环境变量
[root@node1 kafka-eagle-web-1.4.5]# vim /etc/profile
export KE_HOME=/export/server/kafka-eagle-bin-1.4.5/kafka-eagle-web-1.4.5
export PATH=$PATH:$KE_HOME/bin
[root@node1 kafka-eagle-web-1.4.5]# source /etc/profile

# 配置 kafka_eagle。打开conf目录下的system-config.properties，vim的「:set number」是启用行号
[root@node1 kafka-eagle-web-1.4.5]# vim conf/system-config.properties
# 修改第4行，配置kafka集群别名
kafka.eagle.zk.cluster.alias=cluster1
# 修改第5行，配置ZK集群地址
cluster1.zk.list=node1.linxuan.com:2181,node2.linxuan.com:2181,node3.linxuan.com:2181
# 注释第6行
# cluster2.zk.list=xdn10:2181,xdn11:2181,xdn12:2181
# 修改第32行，打开图标统计
kafka.eagle.metrics.charts=true
kafka.eagle.metrics.retain=30
# 注释第69行，取消sqlite数据库连接配置
# kafka.eagle.driver=org.sqlite.JDBC
# kafka.eagle.url=jdbc:sqlite:/hadoop/kafka-eagle/db/ke.db
# kafka.eagle.username=root
# kafka.eagle.password=www.kafka-eagle.org
# 修改第77行，开启MySQL
kafka.eagle.driver=com.mysql.jdbc.Driver
kafka.eagle.url=jdbc:mysql://node1.linxuan.com:3306/ke?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
kafka.eagle.username=root
kafka.eagle.password=123456

# 配置JAVA_HOME
[root@node1 software]# cd /export/server/kafka-eagle-bin-1.4.5/kafka-eagle-web-1.4.5/bin/
[root@node1 bin]# vim ke.sh
# 在第24行添加JAVA_HOME环境配置
export JAVA_HOME=/export/server/jdk1.8.0_144

# 修改Kafka eagle可执行权限
[root@node1 bin]# cd /export/server/kafka-eagle-bin-1.4.5/kafka-eagle-web-1.4.5/bin/
[root@node1 bin]# chmod +x ke.sh

# 启动kafka_eagle
[root@node1 bin]# ./ke.sh start
# 访问Kafka eagle，默认用户为admin，密码为：123456
http://node1.linxuan.com:8048/ke
# 关闭kafka_eagle
[root@node1 bin]# ./ke.sh stop
```

![](..\图片\2-21【Kafka】\1-11Kafka-eagle.png)

| 指标                | 意义                         |
| ------------------- | ---------------------------- |
| Brokers Spread      | broker使用率                 |
| Brokers Skew        | 分区是否倾斜                 |
| Brokers Leader Skew | leader partition是否存在倾斜 |

# 第二章 Java 操作 Kafka

注意，这些代码并不能在单元测试中执行，只能在 main 函数里面执行。

```xml
<properties>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
    <!-- kafka客户端工具 -->
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>2.4.1</version>
    </dependency>

    <!-- 工具类 -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-io</artifactId>
        <version>1.3.2</version>
    </dependency>

    <!-- SLF桥接LOG4J日志 -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.6</version>
    </dependency>

    <!-- LOG4J日志 -->
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.7.0</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
<repositories>
    <repository>
        <id>maven_central</id>
        <name>Maven Central</name>
        <url>https://repo.maven.apache.org/maven2/</url>
    </repository>
</repositories>
```

```properties
# log4j的配置文件，命名为log4j.properties，放在maven工程的resources目录下面
log4j.rootLogger=INFO,stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p - %m%n
```

## 2.1 同步消息到Kafka

编写Java程序，将1-100的数字消息写入到Kafka中。在创建连接的时候一共有四个方面需要来进行配置：

* bootstrap.servers：Kafka 的服务器地址
* acks：表示当生产者生产数据到 Kafka 中，Kafka中会以什么样的策略返回
* key.serializer：Kafka中的消息是以 key、value 键值对存储的，而且生产者生产的消息是需要在网络上传输的，那么就避免不了序列化与反序列化。这里指定的是 StringSerializer 方式，就是以字符串方式序列化发送，将来还可以使用其他的一些序列化框架：Google ProtoBuf、Avro。
* value.serializer：value值的序列化方式

```java
public class KafkaProducerTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建用于连接Kafka的Properties配置
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.88.151:9092");
        properties.put("acks", "all");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 创建生产者对象KafkaProducer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送1-100的消息到指定的topic中
        for (int i = 0; i < 100; i++) {
            // 构建消息
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", null, i + " ");
            Future<RecordMetadata> future = kafkaProducer.send(record);
            // 有异常，这里声明处理
            future.get();
            System.out.println("第" + i + "条消息生产成功");
        }
        // 关闭生产者
        kafkaProducer.close();
    }
}
```

## 2.2 从topic消费消息

从 test topic 中，将消息都消费，并将记录的 offset、key、value 打印出来。Kafka 是一种拉消息模式的消息队列，在消费者中会有一个 offset，表示从哪条消息开始拉取数据。

* group.id：消费者组的概念，一个组中的消费者共同消费 Kafka 中 topic 的数据。
* kafkaConsumer.poll：Kafka 的消费者 API 是一批一批数据的拉取

```java
public static void main(String[] args) {
    // 创建Kafka消费者配置
    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "192.168.88.151:9092");
    // group.id是消费者组，可以使用消费者组将若干个消费者组织到一起，共同消费Kafka中topic的数据
    props.setProperty("group.id", "test");
    // 自动提交offset
    props.setProperty("enable.auto.commit", "true");
    // 自动提交offset的时间间隔
    props.setProperty("auto.commit.interval.ms", "1000");
    // 拉取的key、value数据的反序列化方式
    props.setProperty("key.deserializer", 
                      "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", 
                      "org.apache.kafka.common.serialization.StringDeserializer");

    // 创建Kafka消费者对象，传入Kafka消费者配置
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(props);

    // 指定消费的主题，消费者从那个主题topic中拉取数据
    kafkaConsumer.subscribe(Arrays.asList("test"));

    // 使用一个while循环，不断从Kafka的topic中拉取消息
    while (true) {
        // 拉取超时时间是5秒，5秒内获取不到消息就关闭。消费者不断的从主题中拉取数据直到超时
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(5));
        // 将记录record的offset、key、value都打印出来
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            // 获取到主题、偏移量(这条消息处于Kafka分区中的哪个位置)、key、value
            String topic = consumerRecord.topic();
            long offset = consumerRecord.offset();
            String key = consumerRecord.key();
            String value = consumerRecord.value();

            System.out.println("topic: " + topic + " offset:" + offset + " key:" + key + " value:" + value);
        }
    }
}
```

## 2.3 带有回调函数方法生产消息

如果我们想获取生产者消息是否成功，或者成功生产消息到 Kafka 后，执行一些其他动作，那么可以使用带有回调函数的 API 来发送消息。

* metadata：消息的元数据，属于哪一个 topic、属于哪一个 partition、对应的 offset 是什么
* exception：抛出的异常，如果异常对象为空，那么代表发送成功。

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    // 创建用于连接Kafka的Properties配置
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "192.168.88.151:9092");
    properties.put("acks", "all");
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    // 创建生产者对象KafkaProducer
    KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
    // 发送1-100的消息到指定的topic中
    for (int i = 0; i < 100; i++) {
        // 构建消息
        ProducerRecord<String, String> record = new ProducerRecord("test", null, i + " ");
        // 带回调函数异步方式发送消息。这里也可以使用Lambda来编写，Callback是函数式接口
        kafkaProducer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                // 异常对象为空 那么代表发送成功
                if (exception == null) {
                    String topic = metadata.topic();
                    int partition = metadata.partition();
                    long offset = metadata.offset();

                    System.out.println("发送消息到Kafka中的名字为" + topic 
                                       + "的主题，第" + partition + "分区，第" 
                                       + offset + "条数据成功!");
                } else {
                    System.out.println("发送消息出现异常");
                }
            }
        });
    }
    // 关闭生产者
    kafkaProducer.close();
}
```

## 2.4 消费者组消费主题分区数据

Kafka 支持有一个消费者组中的多个消费者同时消费一个主题中的数据。

* 一个 topic 中如果只有一个分区，那么这个分区只能被某个组中的一个消费者消费
* 有多少个分区，那么就可以被同一个组内的多少个消费者消费。

接下来测试启动两个消费者共同来消费生产者向 test 主题发送的数据。

```java
/**
 * 生产者向test主题发送数据
 */
public class KafkaProducerTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 获取Properties配置
        Properties kafkaProperties = getKafkaProperties();
        // 创建生产者对象KafkaProducer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(kafkaProperties);

        while (true) {
            // 每隔1秒发送1-100的消息到指定的topic中
            for (int i = 0; i < 100; i++) {
                // 构建消息
                ProducerRecord<String, String> record = new ProducerRecord<>("test", null, i + " ");
                Future<RecordMetadata> future = kafkaProducer.send(record);
                // 有异常，这里声明处理
                future.get();
                System.out.println("第" + i + "条消息生产成功");
            }
            // 睡眠3秒再发送
            Thread.sleep(3000);
        }
        // 这里就不用关闭生产者了 一直在源源不断的发送消息 无需关闭
    }

    /**
     * 创建配置
     * @return 用于连接Kafka的Properties配置
     */
    private static Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.88.151:9092");
        properties.put("acks", "all");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }
}
```

```java
/**
 * 消费者消费数据
 */
public class KafkaConsumerTest {
    public static void main(String[] args) {

        // 获取Kafka消费者配置
        Properties consumerProperties = getKafkaConsumerProperties();
        // 创建Kafka消费者对象，传入Kafka消费者配置
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(consumerProperties);

        // 指定消费的主题，消费者从那个主题topic中拉取数据
        kafkaConsumer.subscribe(Arrays.asList("test"));

        // 使用一个while循环，不断从Kafka的topic中拉取消息
        while (true) {
            // 拉取超时时间是5秒，消费者不断的从主题中拉取数据直到超时
            ConsumerRecords<String, String> consumerRecords = 
                kafkaConsumer.poll(Duration.ofSeconds(5));
            // 将记录record的offset、key、value都打印出来
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                // 获取到主题、偏移量(这条消息处于Kafka分区中的哪个位置)、key、value
                String topic = consumerRecord.topic();
                long offset = consumerRecord.offset();
                String key = consumerRecord.key();
                String value = consumerRecord.value();

                System.out.println("topic: " + topic 
                                   + " offset:" + offset 
                                   + " key:" + key 
                                   + " value:" + value);
            }
        }
    }

    /**
     * 创建Kafka消费者配置
     */
    private static Properties getKafkaConsumerProperties() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.88.151:9092");
        // group.id是消费者组，可以使用消费者组将若干个消费者组织到一起，共同消费Kafka中topic的数据
        props.setProperty("group.id", "test");
        // 自动提交offset
        props.setProperty("enable.auto.commit", "true");
        // 自动提交offset的时间间隔
        props.setProperty("auto.commit.interval.ms", "1000");
        // 拉取的key、value数据的反序列化方式
        props.setProperty("key.deserializer", 
                          "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", 
                          "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
```

复制一个消费者的运行配置，两个消费者一起消费 test 主题中的消息。

![](..\图片\2-21【Kafka】\2-1copy configuration.png)

这样就变成了两个消费者来消费数据，一个生产者生产数据，启动三个配置可以发现结果并非我们想象的那样。消费者 1 并没有消费数据，反而消费者 2 一直在消费数据！

![](..\图片\2-21【Kafka】\2-2result.png)

要让两个消费者同时消费消息，必须要给 test 主题，添加一个分区。

```sh
# 设置 test topic为2个分区
[root@node1 bin]# ./kafka-topics.sh --zookeeper 192.168.88.151:2181 -alter --partitions 2 --topic test
WARNING: If partitions are increased for a topic that has a key, the partition logic or ordering of the messages will be affected
Adding partitions succeeded!
```

![](..\图片\2-21【Kafka】\2-3Kafka Tools.png)

重新运行生产者、两个消费者程序，我们就可以看到两个消费者都可以消费 Kafka Topic 的数据了。

总结如下：一个 topic 中如果只有一个分区，那么这个分区只能被某个组中的一个消费者消费。有多少个分区，那么就可以被同一个组内的多少个消费者消费

## 2.5 高级API和低级API

```java
/**
 * 消费者程序：从test主题中消费数据
 */
public class _2ConsumerTest {
    public static void main(String[] args) {
        // 1. 创建Kafka消费者配置
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.88.151:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", 
                          "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", 
                          "org.apache.kafka.common.serialization.StringDeserializer");

        // 2. 创建Kafka消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // 3. 订阅要消费的主题
        consumer.subscribe(Arrays.asList("test"));

        // 4. 使用一个while循环，不断从Kafka的topic中拉取消息
        while (true) {
            // 定义100毫秒超时
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", 
                                  record.offset(), record.key(), record.value());
        }
    }
}
```

上面是之前编写的代码，消费 Kafka 的消息很容易实现，写起来比较简单。我们不需要执行去管理 offset，直接通过 ZK 管理；也不需要管理分区、副本，由 Kafka 统一管理。消费者会自动根据上一次在 ZK 中保存的 offset 去接着获取数据。它同样具有缺点：不能控制 offset 以及不能细化控制分区、副本、ZK等。

通过使用低级 API，我们可以自己来控制 offset，想从哪儿读，就可以从哪儿读。也可以自己控制连接分区，对分区自定义负载均衡。另外，使用低级 API，我们可以自己存储offset，不使用ZK来存储，例如存储在文件、MySQL、或者内存中。

------

**手动消费分区数据**

之前 Kafka 根据消费组中的消费者动态地为 topic 分配要消费的分区。但某些时候，我们需要指定要消费的分区：

- 如果某个程序将某个指定分区的数据保存到外部存储中，例如 Redis、MySQL，那么保存数据的时候，只需要消费该指定的分区数据即可
- 如果某个程序是高可用的，在程序出现故障时将自动重启。这时程序将从指定的分区重新开始消费数据。

```java
String topic = "test";
TopicPartition partition0 = new TopicPartition(topic, 0);
TopicPartition partition1 = new TopicPartition(topic, 1);
consumer.assign(Arrays.asList(partition0, partition1));
```

想要手动消费分区中的数据，那么要求不再使用之前的 `subscribe` 方法订阅主题，而使用 `assign` 方法指定分区。一旦指定了分区，就可以就像前面一样，在循环中调用`poll`方法消费消息。

当手动管理消费分区时，即使 GroupID 是一样的，Kafka 的组协调器都将不再起作用。如果消费者失败，也将不再自动进行分区重新分配。

# 第三章 生产者幂等性与事务

拿 http 举例来说，一次或多次请求，得到地响应是一致的（网络超时等问题除外），换句话说，就是执行多次操作与执行一次操作的影响是一样的。

如果某个系统是不具备幂等性的，用户重复提交了某个表格，就可能会造成不良影响。例如：用户在浏览器上点击了多次提交订单按钮，会在后台生成多个一模一样的订单。

## 3.1 Kafka生产者幂等性

<!-- idempotence:幂等性； Sequence:序列、顺序；  -->

在生产者生产消息时，如果出现 retry 时，有可能会一条消息被发送了多次。如果 Kafka 不具备幂等性，就有可能会在 partition 中保存多条一模一样的消息。

![](..\图片\2-21【Kafka】\3-1producer idempotence.png)

为了实现生产者的幂等性，Kafka 引入了 Producer ID（PID）和 Sequence Number 的概念。

- PID：每个 Producer 在初始化时，都会分配一个唯一的 PID，这个 PID 对用户来说，是透明状态（感知不到）
- Sequence Number：针对每个生产者（对应PID）发送到指定主题分区的消息都对应一个从 0 开始递增的Sequence Number。

![](..\图片\2-21【Kafka】\3-2idempotence.png)

```java
// 配置幂等性
props.put("enable.idempotence",true);
```

## 3.2 Kafka 事务编程

<!-- in metric:度量； period:句点； underscore:下划线； collide:冲突；  -->

<!-- either:两者之一；  -->

Kafka 事务是 2017 年 Kafka 0.11.0.0 引入的新特性。类似于数据库的事务。Kafka 事务指的是生产者生产消息以及消费者提交 offset 的操作可以在一个原子操作中，要么都成功，要么都失败。尤其是在生产者、消费者并存时，事务的保障尤其重要。（consumer-transform-producer模式）

![](..\图片\2-21【Kafka】\3-3consumer-transform-producer.png)

Producer 接口中定义了以下 5 个事务相关方法：

1. initTransactions（初始化事务）：要使用Kafka事务，必须先进行初始化操作
2. beginTransaction（开始事务）：启动一个Kafka事务
3. sendOffsetsToTransaction（提交偏移量）：批量地将分区对应的offset发送到事务中，方便后续一块提交
4. commitTransaction（提交事务）：提交事务
5. abortTransaction（放弃事务）：取消事务

在 Kafka 的 topic「ods_user」中有一些用户数据，数据格式如下

```apl
姓名,性别,出生日期
张三,1,1980-10-09
李四,0,1985-11-01

王五,1,1980-10-09
赵六,0,1985-11-01
```

我们需要编写程序，将用户的性别转换为男、女（1-男，0-女），转换后将数据写入到 topic「dwd_user」中。要求使用事务保障，要么消费了数据同时写入数据到 topic，提交 offset。要么全部失败。

接下来操作一下：

1. 启动生产者控制台程序模拟数据

   ```sh
   # 进入Kafka的bin目录
   [root@node1 bin]# pwd
   /export/server/kafka_2.12-2.4.1/bin
   # 创建名为ods_user的主题
   [root@node1 bin]# ./kafka-topics.sh --create --bootstrap-server node1:9092 --topic ods_user 
   WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid issues it is best to use either, but not both.
   # 创建dwd_user的主题
   [root@node1 bin]# ./kafka-topics.sh --create --bootstrap-server node1:9092 --topic dwd_user 
   WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid issues it is best to use either, but not both.
   # 生产数据到 ods_user
   [root@node1 bin]# ./kafka-console-producer.sh --broker-list node1:9092 --topic ods_user
   姓名,性别,出生日期
   张三,1,1980-10-09
   李四,0,1985-11-01
   # 重新打开一个窗口从ods_user消费数据
   [root@node1 bin]# ./kafka-console-consumer.sh --bootstrap-server node1.linxuan.com:9092 --topic ods_user --from-beginning
   # 重新打开一个窗口从dwd_user消费数据
   [root@node1 bin]# ./kafka-console-consumer.sh --bootstrap-server node1:9092 --topic dwd_user --from-beginning --isolation-level read_committed
   ```

2. 编写创建消费者代码。编写一个方法 createConsumer，该方法中返回一个消费者，订阅「ods_user」主题。注意：需要配置事务隔离级别、关闭自动提交。

   ```java
   public class KafkaTransactionTest {
       /**
        * 创建消费者
        * @return 返回创建的消费者
        */
       public static Consumer<String, String> createConsumer() {
           // 创建Kafka消费者配置
           Properties properties = createConsumerProperties();
           // 创建Kafka消费者
           KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
           // 订阅要消费的主题
           consumer.subscribe(Arrays.asList("ods_user"));
           // 返回消费者
           return consumer;
       }
   
       /**
        * 创建Kafka消费者配置
        * @return 返回Kafka消费者配置类Properties
        */
       private static Properties createConsumerProperties() {
           Properties props = new Properties();
           props.setProperty("bootstrap.servers", "192.168.88.151:9092");
           // group.id是消费者组，可以使用消费者组将若干个消费者组织到一起，共同消费Kafka中topic的数据
           props.setProperty("group.id", "test");
           // 自动提交offset
           props.setProperty("enable.auto.commit", "true");
           // 自动提交offset的时间间隔
           props.setProperty("auto.commit.interval.ms", "1000");
           // 拉取的key、value数据的反序列化方式
           props.setProperty("key.deserializer", 
                             "org.apache.kafka.common.serialization.StringDeserializer");
           props.setProperty("value.deserializer", 
                             "org.apache.kafka.common.serialization.StringDeserializer");
           // 消费者需要设置隔离级别
           props.put("isolation.level", "read_committed");
           // 关闭自动提交
           props.setProperty("enable.auto.commit", "false");
           return props;
       }
   }
   ```

3. 编写创建生产者代码。编写一个方法 createProducer，返回一个生产者对象。注意：需要配置事务的id，开启了事务会默认开启幂等性。

   ```java
   public class KafkaTransactionTest {
       /**
        * 创建生产者
        * @return 返回生产者对象KafkaProducer
        */
       public static Producer<String, String> createProducer() {
           // 创建生产者配置
           Properties properties = createProducerProperties();
           // 返回创建的生产者对象
           return new KafkaProducer<>(properties);
       }
   
       /**
        * 创建Kafka生产者配置
        *
        * @return 用于连接Kafka生产者的Properties配置
        */
       private static Properties createProducerProperties() {
           Properties properties = new Properties();
           properties.put("bootstrap.servers", "192.168.88.151:9092");
           // 连接生产者需要配置事务的id，开启了事务会默认开启幂等性
           properties.put("transactional.id", "dwd_user");
           properties.put("acks", "all");
           properties.put("key.serializer", 
                          "org.apache.kafka.common.serialization.StringSerializer");
           properties.put("value.serializer", 
                          "org.apache.kafka.common.serialization.StringSerializer");
           return properties;
       }
   }
   ```

4. 编写代码消费并生产数据

   ```java
   
   public class KafkaTransactionTest {
   
       public static void main(String[] args) {
           Consumer<String, String> consumer = createConsumer();
           Producer<String, String> producer = createProducer();
           // 初始化事务
           producer.initTransactions();
   
           while (true) {
               try {
                   // 开启事务
                   producer.beginTransaction();
                   // 定义Map结构，用于保存分区对应的offset
                   Map<TopicPartition, OffsetAndMetadata> offsetCommits = new HashMap<>();
                   // 拉取消息
                   ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
                   for (ConsumerRecord<String, String> record : records) {
                       // 保存偏移量
                       offsetCommits.put(new TopicPartition(record.topic(), record.partition()),
                                         new OffsetAndMetadata(record.offset() + 1));
                       // 进行转换、拼接处理
                       String[] fields = record.value().split(",");
                       fields[1] = fields[1].equalsIgnoreCase("1") ? "男" : "女";
                       String message = fields[0] + "," + fields[1] + "," + fields[2];
                       // 生产消息到dwd_user
                       producer.send(new ProducerRecord<>("dwd_user", message));
                   }
                   // 提交偏移量到事务
                   producer.sendOffsetsToTransaction(offsetCommits, "ods_user");
                   // 提交事务
                   producer.commitTransaction();
               } catch (Exception e) {
                   // 放弃事务
                   producer.abortTransaction();
               }
           }
       }
   }
   ```

运行之后发现并没有任何问题：

![](..\图片\2-21【Kafka】\3-4test transaction.png)

模拟一下处理业务逻辑中出现的异常，然后事务对其进行的处理

```java
// 添加到字符串处理的位置。模拟业务处理出现异常
int i = 1 / 0;
```

这时候启动项目发现「dwd_user」消费者并不会消费到消息，出现异常之后就会放弃事务，offset 也是不会被提交的，除非消费、生产消息都成功，才会提交事务。

# 第四章 分区和副本机制

## 4.1 生产者分区写入策略

生产者写入消息到 topic，Kafka 将依据不同的写入策略将数据分配到不同的分区中。Kafka 的分区策略主要有四种：轮询分区策略、随机分区策略、按 key 分区分配策略、自定义分区策略。

1. 轮询分区策略：轮询分区策略是默认的策略，也是使用最多的策略，可以最大限度保证所有消息平均分配到一个分区。如果在生产消息时，key 为 null，则使用轮询算法均衡地分配分区。

2. 随机分区策略：随机策略，每次都随机地将消息分配到每个分区。在较早的版本，默认的分区策略就是随机策略，也是为了将消息均衡地写入到每个分区。但后续轮询策略表现更佳，所以基本上很少会使用随机策略。

3. 按 key 分区分配策略：按 key 分配策略，有可能会出现「数据倾斜」，例如某个 key 包含了大量的数据，因为key 值一样，所有所有的数据将都分配到一个分区中，造成该分区的消息数量远大于其他的分区。

   ![](..\图片\2-21【Kafka】\4-1key allocation.png)

轮询策略、随机策略都会导致一个问题，生产到 Kafka 中的数据是乱序存储的。而按 key 分区可以一定程度上实现数据有序存储——也就是局部有序，但这又可能会导致数据倾斜，所以在实际生产环境中要结合实际情况来做取舍。

自定义分区策略：需要创建一个自定义分区器来分区。

![](..\图片\2-21【Kafka】\4-2my strategy.png)

```java
public class KeyWithRandomPartitioner implements Partitioner {

    private Random r;

    @Override
    public void configure(Map<String, ?> configs) {
        r = new Random();
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // cluster.partitionCountForTopic 表示获取指定topic的分区数量
        return r.nextInt(1000) % cluster.partitionCountForTopic(topic);
    }

    @Override
    public void close() {
    }
}
```

```java
// 在Kafka生产者配置中，自定使用自定义分区器的类名
props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, KeyWithRandomPartitioner.class.getName());
```

## 4.2 消费者组Rebalance机制

Kafka 中的 Rebalance 称之为再均衡，是 Kafka 中确保 Consumer group 下所有的 consumer 如何达成一致，分配订阅的 topic 的每个分区的机制。

发生 Rebalance 时，consumer group 下的所有 consumer 都会协调在一起共同参与，Kafka 使用分配策略尽可能达到最公平的分配。Rebalance 过程会对 consumer group 产生非常严重的影响，Rebalance 的过程中所有的消费者都将停止工作，直到 Rebalance 完成。

Rebalance 触发的时机如下：

1. 消费者组中 consumer 的个数发生变化。例如有新的 consumer 加入到消费者组，或者是某个 consumer 停止
2. 订阅的 topic 个数发生变化。消费者可以订阅多个主题，假设当前的消费者组订阅了三个主题，但有一个主题突然被删除了，此时也需要发生再均衡。
3. 订阅的 topic 分区数发生变化。

## 4.3 消费者分区分配策略

消费者分区分配策略主要有三种：Range范围分配策略、RoudRobin轮询策略、Stricky粘性分配策略。

**Range范围分配策略**

Range 范围分配策略是 Kafka 默认的消费者分配策略，它可以确保每个消费者消费的分区数量是均衡的。Rangle 范围分配策略是针对每个 Topic 的。

配置消费者的 `partition.assignment.strategy` 为 `org.apache.kafka.clients.consumer.RangeAssignor`。

![](..\图片\2-21【Kafka】\4-3consumer range strategy.png)

**RoundRobin轮询策略**

RoundRobinAssignor 轮询策略是将消费组内所有消费者以及消费者所订阅的所有 topic 的 partition 按照字典序排序（topic 和分区的 hashcode 进行排序），然后通过轮询方式逐个将分区以此分配给每个消费者。

配置消费者的 `partition.assignment.strategy` 为 `org.apache.kafka.clients.consumer.RoundRobinAssignor`。

![](..\图片\2-21【Kafka】\4-4consumer roundrobin strategy.png)

**Stricky粘性分配策略**

从 Kafka 0.11.x 开始，引入此类分配策略。主要目的：

1. 分区分配尽可能均匀
2. 在发生 rebalance 的时候，分区的分配尽可能与上一次分配保持相同

没有发生 rebalance 时，Striky 粘性分配策略和 RoundRobin 分配策略类似。如果 consumer2 崩溃了，此时需要进行 rebalance，如果是 Range 分配和轮询分配都会重新进行分配，consumer0 和 consumer1 消费的分区相比较于之前大多数会发生改变。

![](..\图片\2-21【Kafka】\4-5stricky1.png)

Striky 粘性分配策略恰恰相反，它会保留 rebalance 之前的分配结果。这样，只是将原先 consumer2 负责的两个分区再均匀分配给 consumer0、consumer1，这样可以明显减少系统资源的浪费。

![](..\图片\2-21【Kafka】\4-5stricky2.png)

## 4.4 副本机制

副本的目的就是冗余备份，当某个 Broker 上的分区数据丢失时，依然可以保障数据可用。因为在其他的 Broker 上的副本是可用的。

```java
Properties props = new Properties();
props.put("bootstrap.servers", "node1.linxuan.com:9092");
props.put("acks", "all");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
```

对副本关系较大的就是，producer 配置的 acks 参数了，acks 参数表示当生产者生产消息的时候，写入到副本的要求严格程度。它决定了生产者如何在性能和可靠性之间做取舍。接下来分析一下 acks 参数为0、1、-1、all的情况。

**acks配置为0**

![](..\图片\2-21【Kafka】\4-6acks0.png)

**acks配置为1**

当生产者的 ACK 配置为 1 时，生产者会等待 leader 副本确认接收后，才会发送下一条数据，性能中等。

![](..\图片\2-21【Kafka】\4-7acks1.png)

**acks配置为-1或者all**

![](..\图片\2-21【Kafka】\4-8acks-1.png)

# 第五章 Kafka 原理

## 5.1 分区的leader与follower

Kafka 中每个 topic 都可以配置多个分区以及多个副本，每个分区都有一个 leader 以及 0 个或者多个 follower，在创建 topic 时，Kafka 会将每个分区的 leader 均匀地分配在每个 broker 上。

我们正常使用 kafka 是感觉不到 leader、follower 的存在的。但其实所有的读写操作都是由 leader 处理，而所有的 follower 都复制 leader 的日志数据文件，如果 leader 出现故障时，follower 就会被选举为 leader。

- Kafka 中的 leader 负责处理读写操作，而 follower 只负责副本数据的同步
- 如果 leader 出现故障，其他 follower 会被重新选举为 leader
- follower 像一个 consumer 一样，拉取 leader 对应分区的数据，并保存到日志数据文件中

![](..\图片\2-21【Kafka】\5-1.png)

### 5.1.1 AR、ISR、OSR

在实际环境中，leader 有可能会出现一些故障，所以 Kafka 一定会选举出新的 leader。Kafka 中，把 follower 可以按照不同状态分为三类——AR、ISR、OSR。

AR = ISR + OSR，正常情况下，所有的follower副本都应该与leader副本保持同步，即AR = ISR，OSR集合为空。

- AR（Assigned Replicas 已分配的副本）：分区的所有副本称为 AR。
- ISR（In-Sync Replicas 同步中的副本）：与 leader 副本保持一定程度同步的副本（包括 leader 副本）
- OSR（Out-of-Sync Replias 未同步的副本）：由于 follower 副本同步滞后过多的副本（不包括 leader 副本）

![](..\图片\2-21【Kafka】\5-2.png)

### 5.1.2 leader选举

Kafka 启动时，会在所有的 broker 中选择一个 controller。创建 topic、或者添加分区、修改副本数量之类的管理任务都是由 controller 完成的。Kafka 分区leader 的选举，也是由 controller 决定的。

> 前面 leader 和 follower 是针对 partition，而 controller 是针对 broker 的。

Kafka 集群启动的时候，每个 broker 都会尝试去 ZooKeeper 上注册成为 Controller（ZK临时节点）。但只有一个竞争成功，其他的 broker 会注册该节点的监视器，一旦该临时节点状态发生变化，就可以进行相应的处理。 Controller 也是高可用的，一旦某个 broker 崩溃，其他的 broker 会重新注册为 Controller。

![](..\图片\2-21【Kafka】\5-3.png)

所有 Partition 的 leader 选举都由 controller 决定，controller 会将 leader 的改变直接通过 RPC 的方式通知需为此作出响应的 Broker。

controller 读取到当前分区的 ISR，只要有一个 Replica 还幸存，就选择其中一个作为 leader，否则任意选一个 Replica 作为 leader。如果该 partition 的所有 Replica 都已经宕机，则新的 leader 为 -1。

Kafka 中 leader 的选举并没有使用 ZK 来辅助选举。这是因为 Kafka 集群如果业务很多的情况下，会有很多的partition，假设某个 broker 宕机，就会出现很多的 partiton 都需要重新选举 leader，如果使用 zookeeper 选举 leader，会给 zookeeper 带来巨大的压力。

## 5.2 生产与消费数据流程

**数据写入流程**

1. 生产者先从 zookeeper 的`/brokers/topics/主题名/partitions/分区名/state`节点找到该 partition 的 leader
2. 生产者在 ZK 中找到该 ID 对应的 broker
3. broker 进程上的 leader 将消息写入到本地 log 中
4. follower 从 leader 上拉取消息，写入到本地 log，并向 leader 发送 ACK
5. leader 接收到所有的 ISR 中的 Replica 的 ACK 后，并向生产者返回 ACK

![](..\图片\2-21【Kafka】\5-4.png)

**Kafka数据消费流程**

消息队列中消费数据一共有两种方式：推模式、拉模式。

* 推模式（push）：消息队列记录所有消费的状态，如果某一条消息被标记为已消费，那么消费者不能再去进行消费。代表有 RabbitMQ。
* 拉模式（pull）：消费者自己记录消费状态，每个消费者互相独立地顺序拉取消息。代表有 Kafka

kafka采用拉取模型，由消费者自己记录消费状态，每个消费者互相独立地顺序拉取每个分区的消息。消费者可以按照任意的顺序消费消息。比如，消费者可以重置到旧的偏移量，重新处理之前已经消费过的消息；或者直接跳到最近的位置，从当前的时刻开始消费。

每个 consumer 都可以根据分配策略（默认 RangeAssignor），获得要消费的分区。获取到 consumer 对应的 offset（默认从ZK中获取上一次消费的offset）。找到该分区的 leader，拉取数据。最后消费者提交 offset。

![](..\图片\2-21【Kafka】\5-5kafka consum data.png)

## 5.3 数据存储形式

Kafka 中一个 topic 由多个分区组成，一个分区（partition）由多个 segment（段）组成，一个segment（段）由多个文件（log、index、timeindex）组成。

![](..\图片\2-21【Kafka】\5-6file storage.png)

Kafka 中的数据是保存在 `/export/server/kafka_2.12-2.4.1/data`中，消息是保存在以「主题名-分区ID」的文件夹中的，数据文件夹中包含以下内容：

```sh
[root@node1 __consumer_offsets-0]# pwd
/export/server/kafka_2.12-2.4.1/data/__consumer_offsets-0
[root@node1 __consumer_offsets-0]# ll
total 4
-rw-r--r-- 1 root root 10485760 Aug 22 09:09 00000000000000000000.index
-rw-r--r-- 1 root root        0 Aug  9 15:16 00000000000000000000.log
-rw-r--r-- 1 root root 10485756 Aug 22 09:09 00000000000000000000.timeindex
-rw-r--r-- 1 root root        8 Aug 22 09:10 leader-epoch-checkpoint
```

| 文件名                         | 说明                                                     |
| :----------------------------- | -------------------------------------------------------- |
| 00000000000000000000.index     | 索引文件，根据 offset 查找数据就是通过该索引文件来操作的 |
| 00000000000000000000.log       | 日志数据文件                                             |
| 00000000000000000000.timeindex | 时间索引                                                 |
| leader-epoch-checkpoint        | 持久化每个 partition leader 对应的 LEO                   |

> LEO ：log end offset、日志文件中下一条待写入消息的 offset

每个日志文件的文件名为起始偏移量，因为每个分区的起始偏移量是 0，所以分区的日志文件都以 0000000.log 开始。默认的每个日志文件最大为`log.segment.bytes = 1024 * 1024 * 1024 = 1G`。

为了方便测试观察，新创建一个topic「test_10m」，该 topic 每个日志数据文件最大为 10M。

```sh
[root@node1 kafka_2.12-2.4.1]# pwd
/export/server/kafka_2.12-2.4.1
[root@node1 kafka_2.12-2.4.1]# bin/kafka-topics.sh --create --zookeeper node1.linxuan.com --topic test_10m --replication-factor 2 --partitions 3 --config segment.bytes=10485760
```

新的消息总是写入到最后的一个日志文件中，该文件如果到达指定的大小（默认 1 GB）时，将滚动到一个新的文件中。这里我们是指定了每个日志数据文件最大为 10 M，所以这里到达 10 M 就会生成新的日志。

使用之前的生产者程序往「test_10m」主题中生产数据，可以观察到如下：

* 每个 log 文件最大为 10M，一旦超出那么就会创建新的 log 文件。这是我们在创建该 topic 的时候设置的。
* 由 00000000000000664141.log 文件可知 00000000000000000000.log 里面有 664141 条消息。

```sh
[root@node1 test_10m-1]# ll -h
total 16M
-rw-r--r-- 1 root root 5.4K Aug 22 10:15 00000000000000000000.index
-rw-r--r-- 1 root root  10M Aug 22 10:15 00000000000000000000.log
-rw-r--r-- 1 root root 8.0K Aug 22 10:15 00000000000000000000.timeindex
-rw-r--r-- 1 root root  10M Aug 22 10:15 00000000000000664141.index
-rw-r--r-- 1 root root 736K Aug 22 10:15 00000000000000664141.log
-rw-r--r-- 1 root root   10 Aug 22 10:15 00000000000000664141.snapshot
-rw-r--r-- 1 root root  10M Aug 22 10:15 00000000000000664141.timeindex
-rw-r--r-- 1 root root    8 Aug 22 09:58 leader-epoch-checkpoint
```

## 5.4 消息不丢失机制

消息不丢失机制中一共有三个角色：broker 数据不丢失、生产者数据不丢失和消费者数据不丢失。

**broker 数据不丢失**

生产者通过分区的 leader 写入数据后，所有在 ISR 中 follower 都会从 leader 中复制数据，这样，可以确保即使 leader 崩溃了，其他的 follower 的数据仍然是可用的。

**生产者数据不丢失**

生产者连接 leader 写入数据时，可以通过 ACK 机制来确保数据已经成功写入。ACK 机制有三个可选配置

1. 配置 ACK 响应要求为 -1 时：表示所有的节点都收到数据（leader和follower都接收到数据）
2. 配置 ACK 响应要求为 1 时 ：表示 leader 收到数据
3. 配置 ACK 响应要求为 0 时 ：生产者只负责发送数据，不关心数据是否丢失（这种情况可能会产生数据丢失，但性能是最好的）

**消费者数据不丢失**

在消费者消费数据的时候，只要每个消费者记录好 oﬀset 值即可，就能保证数据不丢失。

## 5.5 数据积压

Kafka 消费者消费数据的速度是非常快的，但如果由于处理 Kafka 消息时，由于有一些外部IO、或者是产生网络拥堵，就会造成 Kafka 中的数据积压（或称为数据堆积）。如果数据一直积压，会导致数据出来的实时性受到较大影响。

![](..\图片\2-21【Kafka】\5-7.png)

![](..\图片\2-21【Kafka】\5-8.png)

当Kafka出现数据积压问题时，首先要找到数据积压的原因。以下是在企业中出现数据积压的几个类场景。

* 数据写入 MySQL 失败：数据写入 MySQL 中报错，导致消费分区的 offset 一自没有提交，所以数据积压。 
* 网络延迟消费导致失败：网络抖动，可以将消费的超时时间增长即可。

# 第六章 Kafka 数据清理

Kafka 的消息存储在磁盘中，为了控制磁盘占用空间，Kafka 需要不断地对过去的一些消息进行清理工作。Kafka 的每个分区都有很多的日志文件，这样也是为了方便进行日志的清理。在 Kafka 中，提供两种日志清理方式：

* 日志删除（Log Deletion）：按照指定的策略直接删除不符合条件的日志。
* 日志压缩（Log Compaction）：按照消息的 key 整合，相同 key 但有不同 value 值，只保留最后一个版本。

在 Kafka 的 broker 或 topic 配置中：

| 配置项             | 配置值         | 说明                 |
| ------------------ | -------------- | -------------------- |
| log.cleaner.enable | true（默认）   | 开启自动清理日志功能 |
| log.cleanup.policy | delete（默认） | 删除日志             |
| log.cleanup.policy | compaction     | 压缩日志             |
| log.cleanup.policy | delete,compact | 同时支持删除、压缩   |

## 6.1 日志删除

日志删除是以段（segment 日志）为单位来进行定期清理的。

Kafka 日志管理器中会有一个专门的日志删除任务来定期检测和删除不符合保留条件的日志分段文件，这个周期可以通过 broker 端参数 `log.retention.check.interval.ms` 来配置，默认值为 300,000 毫秒，即5分钟。

当前日志分段的保留策略有3种：基于时间的保留策略、基于日志大小的保留策略以及基于日志起始偏移量的保留策略。

![](..\图片\2-21【Kafka】\6-1.png)

**基于时间的保留策略**

以下三种配置可以指定如果 Kafka 中的消息超过指定的阈值，就会将日志进行自动清理：`log.retention.hours`、 `log.retention.minutes`、`log.retention.ms`。优先级为 `log.retention.ms` > `log.retention.minutes` > `log.retention.hours`。

默认情况，在 broker 中配置为 `log.retention.hours=168`。默认日志的保留时间为 168 小时，相当于保留 7 天。

删除日志分段时:
1. 从日志文件对象中所维护日志分段的跳跃表中移除待删除的日志分段，保证没有线程对日志分段进行读取操作
2. 将日志分段文件添加上`.deleted`的后缀（也包括日志分段对应的索引文件）
3. Kafka 的后台定时任务会定期删除这些`.deleted`为后缀的文件，这个任务的延迟执行时间可以通过`file.delete.delay.ms`参数来设置，默认值为60000，即1分钟。

**基于日志大小的保留策略**

日志删除任务会检查当前日志的大小是否超过设定的阈值来寻找可删除的日志分段的文件集合。可以通过 broker 端参数 `log.retention.bytes` 来配置，默认值为 -1，表示无穷大。如果超过该大小，会自动将超出部分删除。

`log.retention.bytes` 配置的是日志文件的总大小，并非单个的日志分段的大小，一个日志文件包含多个日志分段

**基于日志起始偏移量保留策略**

每个 segment 日志都有它的起始偏移量，如果起始偏移量小于 logStartOffset，那么这些日志文件将会标记为删除

## 6.2 日志压缩

日志压缩（Log Compaction ）是默认的日志删除之外的清理过时数据的方式。它会将相同的 key 对应的数据只保留一个版本。

* Log Compaction 执行后，offset 将不再连续，但依然可以查询 Segment
* Log Compaction 执行前后，日志分段中的每条消息偏移量保持不变。Log Compaction 会生成一个新的Segment 文件
* Log Compaction 是针对 key 的，在使用的时候注意每个消息的 key 不为空
* 基于 Log Compaction 可以保留 key 的最新更新，可以基于 Log Compaction 来恢复消费者的最新状态

![](..\图片\2-21【Kafka】\6-2.png)

# 第七章 Kafka配额限速机制

<!-- Quotas:配额 -->

生产者和消费者以极高的速度生产/消费大量数据或产生请求，从而占用 broker 上的全部资源，造成网络 IO 饱和。有了配额（Quotas）就可以避免这些问题。Kafka 支持配额管理，从而可以对 Producer 和 Consumer 的produce&fetch 操作进行流量限制，防止个别业务压爆服务器。

**限制producer端速率**

为所有 client id 设置默认值，以下为所有 producer 程序设置其 TPS 不超过 1MB/s，即 1048576/s，命令如下：

```sh
bin/kafka-configs.sh --zookeeper node1.linxuan.com:2181 --alter --add-config 'producer_byte_rate=1048576' --entity-type clients --entity-default
```

**限制consumer端速率**

对 consumer 限速与 producer 类似，只不过参数名不一样。为指定的 topic 进行限速，以下为所有 consumer 程序设置 topic 速率不超过 1MB/s，即 1048576/s。命令如下：

```sh
bin/kafka-configs.sh --zookeeper node1.linxuan.com:2181 --alter --add-config 'consumer_byte_rate=1048576' --entity-type clients --entity-default
```

**取消Kafka的Quota配置**

```sh
bin/kafka-configs.sh --zookeeper node1.linxuan.com:2181 --alter --delete-config 'producer_byte_rate' --entity-type clients --entity-default
bin/kafka-configs.sh --zookeeper node1.linxuan.com:2181 --alter --delete-config 'consumer_byte_rate' --entity-type clients --entity-default
```

