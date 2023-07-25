# 第一章 基础概念

## 1.1 大型互联网项目架构目标

首先看一下传统项目和互联网项目：

![](D:\Java\笔记\图片\6-00【Dubbo】\1-1.png)

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

  ![](D:\Java\笔记\图片\6-00【Dubbo】\2-2分布式.png)

* 集群：很多“人”一起 ，干一样的事。 

  用专业的语言来说就是一个业务模块，部署在多台服务器上。 

  ![](D:\Java\笔记\图片\6-00【Dubbo】\2-1集群.png)

分布式、集群的共同点是：都是多台机器（服务器）组成的。因此口语中混淆两者概念的时候都是相对与单机而言。

## 1.3 架构演进

Dubbo 是 SOA时代的产物，SpringCloud 是微服务时代的产物。

![](D:\Java\笔记\图片\6-00【Dubbo】\1-2架构演进.png)

**单体架构**

![](D:\Java\笔记\图片\6-00【Dubbo】\1-2-1单体架构.png)

优点：简单，开发部署都很方便，小型项目首选。

缺点：项目启动慢、可靠性差、可伸缩性差、扩展性和可维护性差、性能低。

**垂直架构**

![](D:\Java\笔记\图片\6-00【Dubbo】\1-2-2垂直架构.png)

垂直架构是指将单体架构中的多个模块拆分为多个独立的项目。形成多个独立的单体架构。

垂直架构存在的问题：重复功能太多。

**分布式架构**

![](D:\Java\笔记\图片\6-00【Dubbo】\1-2-3分布式架构.png)

分布式架构是指在垂直架构的基础上，将公共业务模块抽取出来，作为独立的服务，供其他调用者消费，以实现服务的共享和重用。

RPC： Remote Procedure Call 远程过程调用。有非常多的协议和技术来都实现了RPC的过程。比如：HTTP REST风格，Java RMI规范、WebService SOAP协议、Hession等等。

分布式架构存在的问题：服务提供方一旦产生变更，所有消费方都需要变更。

**SOA架构**

![](D:\Java\笔记\图片\6-00【Dubbo】\1-2-4SOA架构.png)

SOA：（Service-Oriented Architecture，面向服务的架构）是一个组件模型，它将应用程序的不同功能单元（称为服务）进行拆分，并通过这些服务之间定义良好的接口和契约联系起来。

ESB：(Enterparise Servce Bus) 企业服务总线，服务中介。主要是提供了一个服务于服务之间的交互。ESB 包含的功能如：负载均衡，流量控制，加密处理，服务的监控，异常处理，监控告急等等。

**微服务架构**

![](D:\Java\笔记\图片\6-00【Dubbo】\1-2-5微服务架构.png)

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

![](D:\Java\笔记\图片\6-00【Dubbo】\3-1微服务.png)

微服务的上述特性其实是在给分布式架构制定一个标准，进一步降低服务之间的耦合度，提供服务的独立性和灵活性。做到高内聚，低耦合。

因此，可以认为微服务是一种经过良好架构设计的分布式架构方案 。

但方案该怎么落地？选用什么样的技术栈？全球的互联网公司都在积极尝试自己的微服务落地方案。其中在Java领域最引人注目的就是SpringCloud提供的方案了。

微服务：一种良好的分布式架构方案

- 优点：拆分粒度更小、服务更独立、耦合度更低

- 缺点：架构非常复杂，运维、监控、部署难度提高

SpringCloud是微服务架构的一站式解决方案，集成了各种优秀微服务功能组件。

## 1.5 SpringCloud

SpringCloud是目前国内使用最广泛的微服务框架。官网地址：https://spring.io/projects/spring-cloud。

SpringCloud集成了各种微服务功能组件，并基于SpringBoot实现了这些组件的自动装配，从而提供了良好的开箱即用体验。

其中常见的组件包括：

![](D:\Java\笔记\图片\6-00【Dubbo】\3-2SpringCloud.png)

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

![](D:\Java\笔记\图片\6-00【Dubbo】\1-3Dubbo概述.png)

节点角色说明：

- `Provider`：暴露服务的服务提供方
- `Container`：服务运行容器
- `Consumer`：调用远程服务的服务消费方
- `Registry`：服务注册与发现的注册中心
- `Monitor`：统计服务的调用次数和调用时间的监控中心

## 2.2 Zookeeper安装

Dubbo官方推荐使用Zookeeper作为注册中心

前提：ZooKeeper服务器是用Java创建的，它运行在JVM之上。需要安装JDK 7或更高版本。

```sh
# /export/目录下面创建这三个文件夹 用于存放 数据 软件 安装包
[root@node1 ~]# ls /export/
data  server  software
```

**上传压缩包**

将下载的ZooKeeper放到Linux中`/export/software`目录下，并解压。

```shell
#上传zookeeper alt+p
put E:\VMware\uploads\apache-zookeeper-3.5.6-bin.tar.gz

# 将tar包解压到/opt/zookeeper目录下
tar -zxvf apache-zookeeper-3.5.6-bin.tar.gz -C /export/server/
```

**接下来就是配置了**

进入到conf目录拷贝一个zoo_sample.cfg并完成配置

```shell
#进入到conf目录
cd /export/server/apache-zookeeper-3.5.6-bin/conf/
#拷贝 zoo_sample.cfg是不生效的，想要让它生效需要拷贝一份zoo.cfg文件生效
cp  zoo_sample.cfg  zoo.cfg
```

```shell
#打开目录
cd /export/data
#创建zooKeeper存储目录
mkdir  zkdata
#修改zoo.cfg的存储目录
vim /export/server/apache-zookeeper-3.5.6-bin/conf/zoo.cfg 

# 将存储目录修改为/export/data/zkdata 也就是我们上面创建的存储目录
```

**启动ZooKeeper**

```shell
cd /export/server/apache-zookeeper-3.5.6-bin/bin/
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
[root@localhost apache-zookeeper-3.5.6-bin]#  ./zkServer.sh stop
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

## 2.3 Spring项目搭建

实现步骤：

1. 创建服务提供者Provider模块，起名称为dubbo-service
2. 创建服务消费者Consumer模块，起名称为dubbo-web
3. 在服务提供者模块编写 UserServiceImpl 提供服务
4. 在服务消费者中的 UserController 远程调用UserServiceImpl 提供的服务
5. 分别启动两个服务，测试

**Spring项目搭建**

1. 新建一个空的项目，New —— Empty Project。起名称为dubbo-pro。然后使用Maven构建两个模块：dubbo-service、dubbo-web。以前我们是将service和web写在一块的，这次我们分开写。

2. dubbo-web模块中的pom.xml导入坐标：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <groupId>com.linxuan</groupId>
       <artifactId>dubbo-web</artifactId>
       <version>1.0-SNAPSHOT</version>
       <!--因为这是一个Web项目 所以修改一下打包方式-->
       <packaging>war</packaging>
   
       <properties>
           <maven.compiler.source>8</maven.compiler.source>
           <maven.compiler.target>8</maven.compiler.target>
           <spring.version>5.1.9.RELEASE</spring.version>
           <dubbo.version>2.7.4.1</dubbo.version>
           <zookeeper.version>4.0.0</zookeeper.version>
       </properties>
   
       <dependencies>
           <!-- servlet3.0规范的坐标 -->
           <dependency>
               <groupId>javax.servlet</groupId>
               <artifactId>javax.servlet-api</artifactId>
               <version>3.1.0</version>
               <scope>provided</scope>
           </dependency>
           <!--spring的坐标-->
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-context</artifactId>
               <version>${spring.version}</version>
           </dependency>
           <!--springmvc的坐标-->
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-webmvc</artifactId>
               <version>${spring.version}</version>
           </dependency>
   
           <!--日志-->
           <dependency>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-api</artifactId>
               <version>1.7.21</version>
           </dependency>
           <dependency>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-log4j12</artifactId>
               <version>1.7.21</version>
           </dependency>
   
           <!--Dubbo的起步依赖，版本2.7之后统一为rg.apache.dubb -->
           <dependency>
               <groupId>org.apache.dubbo</groupId>
               <artifactId>dubbo</artifactId>
               <version>${dubbo.version}</version>
           </dependency>
           <!--ZooKeeper客户端实现 -->
           <dependency>
               <groupId>org.apache.curator</groupId>
               <artifactId>curator-framework</artifactId>
               <version>${zookeeper.version}</version>
           </dependency>
           <!--ZooKeeper客户端实现 -->
           <dependency>
               <groupId>org.apache.curator</groupId>
               <artifactId>curator-recipes</artifactId>
               <version>${zookeeper.version}</version>
           </dependency>
   
       </dependencies>
   
   
       <build>
           <plugins>
               <!--tomcat插件-->
               <plugin>
                   <groupId>org.apache.tomcat.maven</groupId>
                   <artifactId>tomcat7-maven-plugin</artifactId>
                   <version>2.1</version>
                   <configuration>
                       <port>8000</port>
                       <path>/</path>
                   </configuration>
               </plugin>
           </plugins>
       </build>
   </project>
   ```

   dubbo-service的坐标导入：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <groupId>com.linxuan</groupId>
       <artifactId>dubbo-service</artifactId>
       <version>1.0-SNAPSHOT</version>
   
       <properties>
           <maven.compiler.source>8</maven.compiler.source>
           <maven.compiler.target>8</maven.compiler.target>
           <spring.version>5.1.9.RELEASE</spring.version>
           <dubbo.version>2.7.4.1</dubbo.version>
           <zookeeper.version>4.0.0</zookeeper.version>
       </properties>
   
       <dependencies>
           <!-- servlet3.0规范的坐标 -->
           <dependency>
               <groupId>javax.servlet</groupId>
               <artifactId>javax.servlet-api</artifactId>
               <version>3.1.0</version>
               <scope>provided</scope>
           </dependency>
           <!--spring的坐标-->
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-context</artifactId>
               <version>${spring.version}</version>
           </dependency>
           <!--springmvc的坐标-->
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-webmvc</artifactId>
               <version>${spring.version}</version>
           </dependency>
   
           <!--日志-->
           <dependency>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-api</artifactId>
               <version>1.7.21</version>
           </dependency>
           <dependency>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-log4j12</artifactId>
               <version>1.7.21</version>
           </dependency>
   
           <!--Dubbo的起步依赖，版本2.7之后统一为rg.apache.dubb -->
           <dependency>
               <groupId>org.apache.dubbo</groupId>
               <artifactId>dubbo</artifactId>
               <version>${dubbo.version}</version>
           </dependency>
           <!--ZooKeeper客户端实现 -->
           <dependency>
               <groupId>org.apache.curator</groupId>
               <artifactId>curator-framework</artifactId>
               <version>${zookeeper.version}</version>
           </dependency>
           <!--ZooKeeper客户端实现 -->
           <dependency>
               <groupId>org.apache.curator</groupId>
               <artifactId>curator-recipes</artifactId>
               <version>${zookeeper.version}</version>
           </dependency>
   
       </dependencies>
   
   </project>
   ```

3. 首先来看dubbo-service的编写。新建一个UserService接口和它的实现类，并且将其注入为Bean。

   ```java
   package com.linxuan.service;
   
   public interface UserService {
   
       String sayHello();
   }
   ```

   ```java
   package com.linxuan.service.impl;
   
   import com.linxuan.service.UserService;
   import org.springframework.stereotype.Service;
   
   @Service
   public class UserServiceImpl implements UserService {
       @Override
       public String sayHello() {
           return "Hello Dubbo!";
       }
   }
   ```

   在resources目录下面创建log4j配置类`log4j.properties`：

   ```properties
   # DEBUG < INFO < WARN < ERROR < FATAL
   # Global logging configuration
   log4j.rootLogger=info, stdout,file
   # My logging configuration...
   #log4j.logger.com.tocersoft.school=DEBUG
   #log4j.logger.net.sf.hibernate.cache=debug
   ## Console output...
   log4j.appender.stdout=org.apache.log4j.ConsoleAppender
   log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
   log4j.appender.stdout.layout.ConversionPattern=%5p %d %C: %m%n
   
   log4j.appender.file=org.apache.log4j.FileAppender
   log4j.appender.file.File=../logs/iask.log
   log4j.appender.file.layout=org.apache.log4j.PatternLayout
   log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss}  %l  %m%n
   ```

   在resources目录下面创建Spring配置类`applicationContext.xml`：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
   	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   	   xmlns:dubbo="http://dubbo.apache.org/schema/dubbo" xmlns:context="http://www.springframework.org/schema/context"
   	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
   
   
   	<context:component-scan base-package="com.linxuan.service"/>
   </beans>
   ```

4. 然后再来编写dubbo-web模块的代码。

   因为这个模块需要依赖dubbo-service模块，所以在pom.xml中导入它的坐标：

   ```xml
           <!--依赖Service模块-->
           <dependency>
               <groupId>com.linxuan</groupId>
               <artifactId>dubbo-service</artifactId>
               <version>1.0-SNAPSHOT</version>
           </dependency>
   ```

   在resources目录下面创建log4j配置类`log4j.properties`：

   ```properties
   # DEBUG < INFO < WARN < ERROR < FATAL
   # Global logging configuration
   log4j.rootLogger=info, stdout,file
   # My logging configuration...
   #log4j.logger.com.tocersoft.school=DEBUG
   #log4j.logger.net.sf.hibernate.cache=debug
   ## Console output...
   log4j.appender.stdout=org.apache.log4j.ConsoleAppender
   log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
   log4j.appender.stdout.layout.ConversionPattern=%5p %d %C: %m%n
   
   log4j.appender.file=org.apache.log4j.FileAppender
   log4j.appender.file.File=../logs/iask.log
   log4j.appender.file.layout=org.apache.log4j.PatternLayout
   log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss}  %l  %m%n
   ```

   在resources目录下面创建SpringMVC配置类`springmvc.xml`：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
            http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
   
       <!--开启注解 等同于@@EnableWebMvc-->
   	<mvc:annotation-driven/>
       <!--包扫描 等同于@ComponentScan-->
       <context:component-scan base-package="com.linxuan.controller"/>
   </beans>
   ```

   因为这是web项目，所以在main下面创建`webapp`文件夹，然后在其下面创建`WEB-INF`目录，然后在其下面创建`web.xml`配置文件：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns="http://java.sun.com/xml/ns/javaee"
            xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
            version="2.5">
   
   
       <!-- spring -->
       <!-- 加载spring配置类 -->
       <context-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath*:/applicationContext*.xml</param-value>
       </context-param>
       <listener>
           <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
       </listener>
   
       <!-- Springmvc -->
       <servlet>
           <servlet-name>springmvc</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <!-- 指定加载的配置文件 ，通过参数contextConfigLocation加载-->
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:/springmvc.xml</param-value>
           </init-param>
       </servlet>
   
       <servlet-mapping>
           <servlet-name>springmvc</servlet-name>
           <url-pattern>*.do</url-pattern>
       </servlet-mapping>
   
   </web-app>
   ```

   编写Controller类：

   ```java
   package com.linxuan.controller;
   
   @RestController
   @RequestMapping("/user")
   public class UserController {
   
       // 注入Service
       @Autowired
       private UserService userService;
   
       @RequestMapping("/sayHello")
       public String sayHello() {
           return userService.sayHello();
       }
   }
   ```

5. 将dubbo-service模块下载到本地，然后导入到dubbo-web模块中。点击Maven，点击Plugins中的install。

6. 启动dubbo-web模块。点击Maven，点击Plugins中的tomcat7:run。

## 2.3 Dubbo入门

需要启动服务器中的zookeeper。

**服务注册**

将dubbo-service注册到zookeeper中。

1. 修改项目打包方式成为war

   ```xml
   <!--修改一下打包方式-->
   <packaging>war</packaging>
   
       <build>
           <plugins>
               <!--tomcat插件 让其运行-->
               <plugin>
                   <groupId>org.apache.tomcat.maven</groupId>
                   <artifactId>tomcat7-maven-plugin</artifactId>
                   <version>2.1</version>
                   <configuration>
                       <!--修改端口号为9000 这样一台机器上能运行两个tomcat服务-->
                       <port>9000</port>
                       <path>/</path>
                   </configuration>
               </plugin>
           </plugins>
       </build>
   ```

2. 修改`org.springframework.stereotype.Service`注解为`org.apache.dubbo.config.annotation.Service`。该注解将这个类提供的服务（方法）对外面发布，将访问的地址、IP、端口、路径注册到注册中心中。

   ```java
   package com.linxuan.service.impl;
   
   import com.linxuan.service.UserService;
   import org.apache.dubbo.config.annotation.Service;
   
   @Service
   public class UserServiceImpl implements UserService {
       @Override
       public String sayHello() {
           return "Hello Dubbo";
       }
   }
   ```

3. 复制dubbo-web模块下面的webapp目录到dubbo-service模块。并将SpringMVC的配置删除。

4. 进行dubbo的配置。在`applicationContext.xml`中进行修改：

   ```xml
   <!--dubbo的配置-->
   <!--配置项目的名称 唯一-->
   <dubbo:application name="dubbo-srevice"/>
   <!--配置注册中心的地址-->
   <dubbo:registry address="zookeeper://192.168.88.151:2181"/>
   <!--配置dubbo包扫描路径-->
   <dubbo:annotation package="com.linxuan.service.impl"/>
   ```

5. 服务启动，没有问题。

**服务消费者改造**

1. 我们是需要从注册中心拉下来Bean的，而不是本地导入依赖，所以这里先删除dubbo-service的依赖。

   ```xml
           <!--依赖Service模块-->
           <dependency>
               <groupId>com.linxuan</groupId>
               <artifactId>dubbo-service</artifactId>
               <version>1.0-SNAPSHOT</version>
           </dependency>
   ```

   但是这样就会导致UserController中的UserService爆红。所以这里我们在该模块下面创建一个UserService接口以及该接口的方法。这样先将编译通过。

   ```java
   package com.linxuan.service;
   
   public interface UserService {
       String sayHello();
   }
   ```

2. 将web.xml中关于Spring的配置文件删除。

   ```xml
       <!-- spring -->
       <!-- 加载spring配置类 -->
       <context-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath*:/applicationContext*.xml</param-value>
       </context-param>
       <listener>
           <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
       </listener>
   ```

3. 将`Autowired`注解更换为`org.apache.dubbo.config.annotation.Reference`注解。该注解作用如下：从zookeeper注册中心获取userService的访问url，进行远程调用RPC，将结果封装一个代理对象，给变量赋值。

   ```java
   @RestController
   @RequestMapping("/user")
   public class UserController {
   
       // 注入Service
       @Reference
       private UserService userService;
   
       @RequestMapping("/sayHello")
       public String sayHello() {
           return userService.sayHello();
       }
   }
   ```

4. 将dubbo的配置添加进`springmvc.xml`

   ```xml
       <!--开启注解 等同于@@EnableWebMvc-->
       <mvc:annotation-driven/>
       <!--包扫描 等同于@ComponentScan-->
       <context:component-scan base-package="com.linxuan.controller"/>
   
       <!--dubbo的配置-->
       <!--配置项目的名称 唯一-->
       <dubbo:application name="dubbo-web"/>
       <!--配置注册中心的地址-->
       <dubbo:registry address="zookeeper://192.168.88.151:2181"/>
       <!--配置dubbo包扫描路径-->
       <dubbo:annotation package="com.linxuan.controller"/>
   ```

5. 服务启动，没有问题。`http://localhost:8000/user/sayHello.do`

**解决端口冲突**

上面项目启动之后，会报错，信息如下：

```ABAP
ERROR 2022-12-20 19:42:14,809 org.apache.dubbo.qos.server.Server:  [DUBBO] qos-server can not bind localhost:22222, dubbo version: 2.7.4.1, current host: 192.168.88.1
java.net.BindException: Address already in use: bind
```

端口冲突。我们这两个项目是在同一台电脑上面启动的，首先启动了dubbo-service项目，然后该项目启动了qos并且占用了对应的端口。而这时启动dubbo-web项目就会产生端口冲突了。

解决方法如下是修改一下dubbo-web项目中dubbo的配置：

```xml
    <!--dubbo的配置-->
    <!--配置项目的名称 唯一-->
    <dubbo:application name="dubbo-web">
        <dubbo:parameter key="qos.port" value="33333"/>
    </dubbo:application>
```

重启该项目就不会报错了！

**抽取service模块**

可以发现，我们在dubbo-service模块中和dubbo-web模块中都写了UserService接口，这样就大大的加重了我们编写代码的重复性，所以接下来我们就是将其抽取出来了。

1. 新建Maven项目，dubbo-interface。新建UserService接口及方法

   ```java
   package com.linxuan.service;
   
   public interface UserService {
       String sayHello();
   }
   ```

2. 将dubbo-service和dubbo-web项目中的该接口删除。并且在项目中导入依赖

   ```xml
           <dependency>
               <groupId>com.linxuan</groupId>
               <artifactId>dubbo-interface</artifactId>
               <version>1.0-SNAPSHOT</version>
           </dependency>
   ```

3. 在Maven中install一下dubbo-interface，然后刷新，导包。

4. 启动项目没有任何问题。

# 第三章 Dubbo 高级特性

## 3.1 dubbo-admin

dubbo-admin 管理平台，是图形化的服务管理页面。从注册中心中获取到所有的提供者/消费者进行配置管理，它具有路由规则、动态配置、服务降级、访问控制、权重调整、负载均衡等管理功能。

dubbo-admin 是一个前后端分离的项目。前端使用vue，后端使用springboot。•安装 dubbo-admin 其实就是部署该项目。我们将dubbo-admin安装到开发环境上。要保证开发环境有jdk，maven，nodejs

**安装**

1. 因为前端工程是用vue开发的，所以需要安装node.js，node.js中自带了npm，后面我们会通过npm启动

   下载地址

   ```
   https://nodejs.org/en/
   ```

2. 下载 Dubbo-Admin

   进入github，搜索dubbo-admin，下载项目。

   ```
   https://github.com/apache/dubbo-admin
   ```

   把下载的zip包解压。

3. 修改配置文件

   解压后我们进入`…\dubbo-admin-develop\dubbo-admin-server\src\main\resources`目录，找到 `application.properties`配置文件进行配置修改，修改zookeeper地址。

   ```shell
   # centers in dubbo2.7
   # admin.registry.address注册中心
   admin.registry.address=zookeeper://192.168.88.151:2181
   # admin.config-center 配置中心
   admin.config-center=zookeeper://192.168.88.151:2181
   # admin.metadata-report.address元数据中心
   admin.metadata-report.address=zookeeper://192.168.88.151:2181
   ```

4. 打包项目

   在 dubbo-admin-develop 目录执行打包命令

   ```shell
   mvn  clean package
   ```

5. 启动后端

   切换到目录`dubbo-Admin-develop\dubbo-admin-distribution\target>`，执行下面的命令启动 dubbo-admin，dubbo-admin后台由SpringBoot构建。

   ```shell
   java -jar .\dubbo-admin-0.1.jar
   ```

6. 前台后端

   dubbo-admin-ui 目录下执行命令

   ```shell
   npm run dev
   ```

7. 访问

   浏览器输入。用户名密码都是root

   ```
   http://localhost:8081/
   ```

**简单使用**

启动虚拟机中的zookeeper，然后启动Spring项目。就可以在dubbo-admin-ui中看到服务了

![](D:\Java\笔记\图片\6-00【Dubbo】\4-1.png)

点击详情可以查看详情界面：

![](D:\Java\笔记\图片\6-00【Dubbo】\4-2.png)

可以看到该项目的端口号是20880。我们启动dubbo-service服务的时候，不仅仅tomcat要占用端口号，dubbo也需要占用一个端口号，那就是208080。一旦我们要在电脑上启动多个dubbo服务，那么就会导致端口冲突，所以需要在applicationContext.xml中修改一下端口号：

```xml
<!--dubbo的配置 配置端口号 默认就是20880-->
<dubbo:protocol port="20880"></dubbo:protocol>
```

我们查看`com.linxuan.service.UserService` （服务提供者）的具体详细信息，可以发现元数据信息是空的，也就是棕色的地方。

我们需要打开我们的生产者配置文件加入下面配置

```xml
<!-- 元数据配置 -->
<dubbo:metadata-report address="zookeeper://192.168.88.151:2181" />
```

重新启动生产者，再次打开Dubbo-Admin。这样我们的元数据信息就出来了

## 3.2 序列化

两个机器传输数据，如何传输Java对象？答案是序列化。

那么我们在两个项目中传输Java对象的时候也要相应的将其序列化。dubbo 内部已经将序列化和反序列化的过程内部封装了，我们只需要在定义pojo类时实现`Serializable`接口即可。一般会定义一个公共的pojo模块，让生产者和消费者都依赖该模块。

1. 创建一个模块dubbo-pojo，定义User类

   ```java
   package com.linxuan.pojo;
   
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class User {
       private int id;
       private String username;
       private String password;
   }
   ```

2. dubbo-interface依赖dubbo-pojo模块，并且在公共接口中创建一个方法

   ```xml
   <dependencies>
       <dependency>
           <groupId>com.linxuan</groupId>
           <artifactId>dubbo-pojo</artifactId>
           <version>1.0-SNAPSHOT</version>
       </dependency>
   </dependencies>
   ```

   ```java
   public interface UserService {
       String sayHello();
   
       /**
        * 根据用户id查询用户
        */
       User findById(int id);
   }
   ```

3. dubbo-service模块重写方法

   ```java
   @Service
   public class UserServiceImpl implements UserService {
       public String sayHello() {
           return "Hello Dubbo 123!";
       }
   
       public User findById(int id) {
           return new User(1, "linxuan", "123456");
       }
   }
   ```

4. dubbo-web模块调用该模块中的方法

   ```java
   @RestController
   @RequestMapping("/user")
   public class UserController {
   
       // 注入Service
       @Reference
       private UserService userService;
   
       @RequestMapping("/sayHello")
       public String sayHello() {
           return userService.sayHello();
       }
   
       // 调用方法
       @GetMapping("/find")
       public User findById(@RequestParam("id") int id) {
           return userService.findById(id);
       }
   }
   ```

5. 启动zookeeper，安装dubbo-pojo模块、dubbo-interface模块。启动dubbo-service模块和dubbo-web模块。浏览器输入`http://localhost:8000/user/find.do?id=1`，可以发现返回状态码是500，服务器内部错误。IDEA报错：

   ```apl
   java.lang.IllegalStateException: Serialized class com.linxuan.pojo.User must implement java.io.Serializable
   ```

   提示我们User类必须实现`Serializable`接口。

   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class User implements Serializable {
       private int id;
       private String username;
       private String password;
   }
   ```

   实现之后重启项目，发现没有问题了。

## 3.3 地址缓存

注册中心挂了，服务是否可以正常访问？注册中心也就是zookeeper它挂了，那么项目依然可以正常访问。

- 可以，因为dubbo服务消费者在第一次调用时，会将服务提供方地址缓存到本地，以后在调用则不会访问注册中心。这是为了避免和注册中心产生大量的交互。
- 当服务提供者地址发生变化时，注册中心会通知服务消费者。

## 3.4 超时与重试

服务消费者在调用服务提供者的时候发生了阻塞、等待的情形，这个时候，服务消费者会一直等待下去。在某个峰值时刻，大量的请求都在同时请求服务消费者，会造成线程的大量堆积，势必会造成雪崩。

![](D:\Java\笔记\图片\6-00【Dubbo】\4-3.png)

dubbo 利用超时机制来解决这个问题，设置一个超时时间，在这个时间段内，无法完成服务访问，则自动断开连接。使用`timeout`属性配置超时时间，默认值1000，单位毫秒。

```java
// 这里属性来配置
@Service(timeout = 3000)
public class UserServiceImpl implements UserService {
    public String sayHello() {
        return "Hello Dubbo 123!";
    }

    public User findById(int id) {
        return new User(1, "linxuan", "123456");
    }
}
```

```java
@RestController
@RequestMapping("/user")
public class UserController {

    // 注入Service
    // 这里也可以配置 但是我们建议在服务的提供方来配置
    @Reference(timeout = 3000)
    private UserService userService;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return userService.sayHello();
    }

    @GetMapping("/find")
    public User findById(@RequestParam("id") int id) {
        return userService.findById(id);
    }
}
```

设置了超时时间，在这个时间段内，无法完成服务访问，则自动断开连接。如果出现网络抖动，则这一次请求就会失败。Dubbo 提供重试机制来避免类似问题的发生。通过 retries  属性来设置重试次数。默认为 2 次。

```java
// 服务3秒超时，重试3此，一共四次。
@Service(timeout = 3000, retries = 3)
public class UserServiceImpl implements UserService {
    public String sayHello() {
        return "Hello Dubbo 123!";
    }

    public User findById(int id) {
        return new User(1, "linxuan", "123456");
    }
}
```

## 3.5 多版本

灰度发布：当出现新功能时，会让一部分用户先使用新功能，用户反馈没问题时，再将所有用户迁移到新功能。dubbo 中使用version 属性来设置和调用同一个接口的不同版本。

在dubbo-service中新建一个UserService接口的实现类并设置两个实现类的版本号：

```java
@Service(version = "v1.0")
public class UserServiceImpl implements UserService {
    public String sayHello() {
        return "Hello Dubbo 123!";
    }

    public User findById(int id) {
        // 输出老版本
        System.out.println("old...");
        return new User(1, "linxuan", "123456");
    }
}
```

```java
@Service(version = "v2.0")
public class UserServiceImpl2 implements UserService {
    public String sayHello() {
        return "Hello Dubbo 123!";
    }

    public User findById(int id) {
        // 输出新版本
        System.out.println("new...");
        return new User(1, "linxuan", "123456");
    }
}

```

dubbo-web模块中注入的Service模块中version可以进行更换，使用对应Version的值，那么也会在控制台输出相应的数据。

```java
@RestController
@RequestMapping("/user")
public class UserController {

    // 注入Service
    // 使用老版本 会在控制台输出 old...
    @Reference(version = "v1.0")
    private UserService userService;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return userService.sayHello();
    }

    @GetMapping("/find")
    public User findById(@RequestParam("id") int id) {
        return userService.findById(id);
    }
}
```
