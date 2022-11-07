# 第一章 基础概念

## 1.1 大型互联网项目架构目标

首先看一下传统项目和互联网项目：

![](D:\Java\笔记\图片\3-07【Dubbo】\1-1.png)

衡量一个网站速度是否快：

- 打开一个新页面一瞬间完成；页面内跳转，一刹那间完成。
- 根据佛经《僧祇律》记载：一刹那者为一念，二十念为一瞬，二十瞬为一弹指，二十弹指为一罗预，二十罗预为一须臾，一日一夜有三十须臾。 
- 经过周密的计算，一瞬间为0.36 秒，一刹那有 0.018 秒 

互联网项目特点：用户多、流量大，并发高、海量数据、易受攻击、功能繁琐、变更快。

互联网项目架构目标：高性能，提供快速的访问体验。

衡量网站的性能指标：

- 响应时间：指执行一个请求从开始到最后收到响应数据所花费的总体时间。
- 并发数：指系统同时能处理的请求数量。
  - 并发连接数：指的是客户端向服务器发起请求，并建立了TCP连接。每秒钟服务器连接的总TCP数量
  - 请求数：也称为`QPS(Query Per Second)` 指每秒多少请求.
  - 并发用户数：单位时间内有多少用户
- 吞吐量：指单位时间内系统能处理的请求数量。
  - `QPS`：`Query Per Second` 每秒查询数。 
  - `TPS`：`Transactions Per Second` 每秒事务数。 
  - 一个事务是指一个客户机向服务器发送请求然后服务器做出反应的过程。客户机在发送请求时开始计时，收到服务器响应后结束计时，以此来计算使用的时间和完成的事务个数。
  - 一个页面的一次访问，只会形成一个TPS；但一次页面请求，可能产生多次对服务器的请求，就会有多个QPS

**大型互联网项目架构目标**

- 高性能：提供快速的访问体验。
- 高可用：网站服务一直可以正常访问。
- 可伸缩：通过硬件增加/减少，提高/降低处理能力。
- 高可扩展：系统间耦合低，方便的通过新增/移除方式，增加/减少新的功能/模块。 
- 安全性：提供网站安全访问和数据加密，安全存储等策略。
- 敏捷性：随需应变，快速响应。

## 1.2 分布式和集群

* 分布式：很多“人”一起，干不一样的事。这些不一样的事，合起来是一件大事。

  用专业的语言来说就是一个大的业务系统，拆分为小的业务模块，分别部署在不同的机器上。 

  一个人洗菜，一个人切菜，一个人炒菜。合在一起就是做一份菜的全部步骤了，这就是分布式。

  ![](D:\Java\笔记\图片\3-07【Dubbo】\2-2分布式.png)

* 集群：很多“人”一起 ，干一样的事。 

  用专业的语言来说就是一个业务模块，部署在多台服务器上。 

  ![](D:\Java\笔记\图片\3-07【Dubbo】\2-1集群.png)

分布式、集群的共同点是：都是多台机器（服务器）组成的。因此口语中混淆两者概念的时候都是显贵与单机而言。

## 1.3 架构演进

Dubbo 是 SOA时代的产物，SpringCloud 是微服务时代的产物。

![](D:\Java\笔记\图片\3-07【Dubbo】\1-2架构演进.png)

**单体架构**

![](D:\Java\笔记\图片\3-07【Dubbo】\1-2-1单体架构.png)

- 优点：简单，开发部署都很方便，小型项目首选。
- 缺点：项目启动慢、可靠性差、可伸缩性差、扩展性和可维护性差、性能低。

**垂直架构**

![](D:\Java\笔记\图片\3-07【Dubbo】\1-2-2垂直架构.png)

垂直架构是指将单体架构中的多个模块拆分为多个独立的项目。形成多个独立的单体架构。

* 垂直架构存在的问题：重复功能太多。

**分布式架构**

![](D:\Java\笔记\图片\3-07【Dubbo】\1-2-3分布式架构.png)

分布式架构是指在垂直架构的基础上，将公共业务模块抽取出来，作为独立的服务，供其他调用者消费，以实现服务的共享和重用。

RPC： Remote Procedure Call 远程过程调用。有非常多的协议和技术来都实现了RPC的过程。比如：HTTP REST风格，Java RMI规范、WebService SOAP协议、Hession等等。

* 分布式架构存在的问题：服务提供方一旦产生变更，所有消费方都需要变更。

**SOA架构**

![](D:\Java\笔记\图片\3-07【Dubbo】\1-2-4SOA架构.png)

SOA：（Service-Oriented Architecture，面向服务的架构）是一个组件模型，它将应用程序的不同功能单元（称为服务）进行拆分，并通过这些服务之间定义良好的接口和契约联系起来。

ESB：(Enterparise Servce Bus) 企业服务总线，服务中介。主要是提供了一个服务于服务之间的交互。ESB 包含的功能如：负载均衡，流量控制，加密处理，服务的监控，异常处理，监控告急等等。

**微服务架构**

![](D:\Java\笔记\图片\3-07【Dubbo】\1-2-5微服务架构.png)

微服务架构是在 SOA 上做的升华，微服务架构强调的一个重点是“业务需要彻底的组件化和服务化”，原有的单个业务系统会拆分为多个可以独立开发、设计、运行的小应用。这些小应用之间通过服务完成交互和集成。

微服务架构 = 80%的SOA服务架构思想 + 100%的组件化架构思想 + 80%的领域建模思想。

特点：

- 服务实现组件化：开发者可以自由选择开发技术。也不需要协调其他团队
- 服务之间交互一般使用REST API
- 去中心化：每个微服务有自己私有的数据库持久化业务数据
- 自动化部署：把应用拆分成为一个一个独立的单个服务，方便自动化部署、测试、运维

## 1.4 微服务

微服务的架构特征：

- 单一职责：微服务拆分粒度更小，每一个服务都对应唯一的业务能力，做到单一职责
- 自治：团队独立、技术独立、数据独立，独立部署和交付
- 面向服务：服务提供统一标准的接口，与语言和技术无关
- 隔离性强：服务调用做好隔离、容错、降级，避免出现级联问题

![image-20210713203753373](D:\Java\笔记\图片\3-07【Dubbo】\3-1微服务.png)

微服务的上述特性其实是在给分布式架构制定一个标准，进一步降低服务之间的耦合度，提供服务的独立性和灵活性。做到高内聚，低耦合。

因此，可以认为**微服务**是一种经过良好架构设计的**分布式架构方案** 。

但方案该怎么落地？选用什么样的技术栈？全球的互联网公司都在积极尝试自己的微服务落地方案。其中在Java领域最引人注目的就是SpringCloud提供的方案了。

微服务：一种良好的分布式架构方案

- 优点：拆分粒度更小、服务更独立、耦合度更低

- 缺点：架构非常复杂，运维、监控、部署难度提高

SpringCloud是微服务架构的一站式解决方案，集成了各种优秀微服务功能组件。

## 1.5 SpringCloud

SpringCloud是目前国内使用最广泛的微服务框架。官网地址：https://spring.io/projects/spring-cloud。

SpringCloud集成了各种微服务功能组件，并基于SpringBoot实现了这些组件的自动装配，从而提供了良好的开箱即用体验。

其中常见的组件包括：

![image-20210713204155887](D:\Java\笔记\图片\3-07【Dubbo】\3-2SpringCloud.png)

再加上一个负载均衡组件：Ribbon

另外，SpringCloud底层是依赖于SpringBoot的，并且有版本的兼容关系，如下：

| Release Train                                                | Boot Version                          |
| :----------------------------------------------------------- | :------------------------------------ |
| [2021.0.x](https://github.com/spring-cloud/spring-cloud-release/wiki/Spring-Cloud-2021.0-Release-Notes) aka Jubilee | 2.6.x, 2.7.x (Starting with 2021.0.3) |
| [2020.0.x](https://github.com/spring-cloud/spring-cloud-release/wiki/Spring-Cloud-2020.0-Release-Notes) aka Ilford | 2.4.x, 2.5.x (Starting with 2020.0.3) |
| [Hoxton](https://github.com/spring-cloud/spring-cloud-release/wiki/Spring-Cloud-Hoxton-Release-Notes) | 2.2.x, 2.3.x (Starting with SR5)      |
| [Greenwich](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Greenwich-Release-Notes) | 2.1.x                                 |
| [Finchley](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Finchley-Release-Notes) | 2.0.x                                 |
| [Edgware](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Edgware-Release-Notes) | 1.5.x                                 |
| [Dalston](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Dalston-Release-Notes) | 1.5.x                                 |

我们课堂学习的版本是 Hoxton.SR10，因此对应的SpringBoot版本是2.3.x版本。

# 第二章 Dubbo 快速入门

Dubbo是阿里巴巴公司开源的一个高性能、轻量级的 Java RPC 框架。致力于提供高性能和透明化的 RPC 远程服务调用方案，以及 SOA 服务治理方案。官网：http://dubbo.apache.org

现在Dubbo已经3.0了，我们学的都是2.7的技术，之后再学习新的技术。

## 2.1 Dubbo概述

![](D:\Java\笔记\图片\3-07【Dubbo】\1-3Dubbo概述.png)

节点角色说明：

- `Provider`：暴露服务的服务提供方
- `Container`：服务运行容器
- `Consumer`：调用远程服务的服务消费方
- `Registry`：服务注册与发现的注册中心
- `Monitor`：统计服务的调用次数和调用时间的监控中心

## 2.2 Zookeeper安装

Dubbo官方推荐使用Zookeeper作为注册中心

前提：ZooKeeper服务器是用Java创建的，它运行在JVM之上。需要安装JDK 7或更高版本。

**上传压缩包**

将下载的ZooKeeper放到Linux中`/opt/ZooKeeper`目录下，并解压（我这里放在了Centos7这个虚拟机里面，`ip192.168.66.130`）。

```shell
#上传zookeeper alt+p
put f:/setup/apache-zookeeper-3.5.6-bin.tar.gz
#打开 opt目录
cd /opt
#创建zooKeeper目录
mkdir  zooKeeper
#将zookeeper安装包移动到 /opt/zooKeeper
mv apache-zookeeper-3.5.6-bin.tar.gz /opt/zookeeper/
```

```shell
# 将tar包解压到/opt/zookeeper目录下
tar -zxvf apache-ZooKeeper-3.5.6-bin.tar.gz 
```

**接下来就是配置了**

进入到conf目录拷贝一个zoo_sample.cfg并完成配置

```shell
#进入到conf目录
cd /opt/zooKeeper/apache-zooKeeper-3.5.6-bin/conf/
#拷贝 zoo_sample.cfg是不生效的，想要让它生效需要拷贝一份zoo.cfg文件生效
cp  zoo_sample.cfg  zoo.cfg
```

```shell
#打开目录
cd /opt/zooKeeper/
#创建zooKeeper存储目录
mkdir  zkdata
#修改zoo.cfg的存储目录
vim /opt/zooKeeper/apache-zooKeeper-3.5.6-bin/conf/zoo.cfg

# 将存储目录修改为/opt/zookeeper/zkdata 也就是我们上面创建的存储目录
```

**启动ZooKeeper**

```shell
cd /opt/zooKeeper/apache-zooKeeper-3.5.6-bin/bin/
#启动
 ./zkServer.sh  start
```

* 启动成功

  ```shell
  [root@localhost apache-zookeeper-3.5.6-bin]# bin/zkServer.sh start
  ZooKeeper JMX enabled by default
  Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
  Starting zookeeper ... STARTED
  ```

* 启动失败

  ```shell
  [linxuan@localhost bin]$  ./zkServer.sh  start
  ZooKeeper JMX enabled by default
  Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
  Starting zookeeper ... ./zkServer.sh: line 169: /opt/zookper/zkdata/zookeeper_server.pid: Permission denied
  FAILED TO WRITE PID
  [linxuan@localhost bin]$ ./zkServer.sh: line 158: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../logs/zookeeper-linxuan-server-localhost.localdomain.out: Permission denied
  ```

  这样就启动失败了！我这里的解决方式是：修改了端口号。

  ZooKeeper中的AdminService服务(一般不会用到)会占用8080端口，与Tomcat冲突， 故需要`vim zoo.cfg`，将此服务占用的端口号修改为没有被占用的端口号

  ```shell
  [root@localhost apache-zookeeper-3.5.6-bin]# vim conf/zoo.cfg 
  # The number of milliseconds of each tick
  tickTime=2000
  # The number of ticks that the initial 
  # synchronization phase can take
  initLimit=10
  # The number of ticks that can pass between 
  # sending a request and getting an acknowledgement
  syncLimit=5
  # the directory where the snapshot is stored.
  # do not use /tmp for storage, /tmp here is just 
  # example sakes.
  dataDir=/opt/zookper/zkdata
  # the port at which the clients will connect
  clientPort=2181
  # 添加上这个
  admin.serverPort=9091
  ```

  ```shell
  [root@localhost apache-zookeeper-3.5.6-bin]# bin/zkServer.sh start
  ZooKeeper JMX enabled by default
  Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
  Starting zookeeper ... STARTED
  ```

**关闭Zookeeper**

```shell
[root@localhost apache-zookeeper-3.5.6-bin]# bin/zkServer.sh stop
ZooKeeper JMX enabled by default
Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
Stopping zookeeper ... STOPPED
```

**查看ZooKeeper状态**

```shell
./zkServer.sh status
```

zookeeper启动成功。standalone代表zk没有搭建集群，现在是单节点

```shell
# 启动成功
[root@localhost apache-zookeeper-3.5.6-bin]# bin/zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost.
Mode: standalone
```

zookeeper没有启动

```shell
[root@localhost apache-zookeeper-3.5.6-bin]# bin/zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost.
Error contacting service. It is probably not running.
```

## 2.3 Dubbo快速入门

实现步骤：

1. 创建服务提供者Provider模块
2. 创建服务消费者Consumer模块
3. 在服务提供者模块编写 UserServiceImpl 提供服务
4. 在服务消费者中的 UserController 远程调用UserServiceImpl 提供的服务
5. 分别启动两个服务，测试

# 第三章 Dubbo 高级特性