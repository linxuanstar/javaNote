# 第一章 Zookeeper基础

Zookeeper 是 Apache Hadoop 项目下的一个子项目，是一个树形目录服务。Zookeeper 翻译过来就是动物园管理员，他是用来管 Hadoop（大象）、Hive(蜜蜂)、Pig(小 猪)的管理员。简称zk。

Zookeeper 是一个分布式的、开源的分布式应用程序的协调服务。Zookeeper 提供的主要功能包括：配置管理、分布式锁、集群管理。

ZooKeeper 是一个树形目录服务,其数据模型和Unix的文件系统目录树很类似，拥有一个层次化结构。这里面的每一个节点都被称为： ZNode，每个节点上都会保存自己的数据和节点信息。 节点可以拥有子节点，同时也允许少量（1MB）数据存储在该节点之下。节点可以分为四大类：

- PERSISTENT 持久化节点 
- EPHEMERAL 临时节点 ：-e
- PERSISTENT_SEQUENTIAL 持久化顺序节点 ：-s
- EPHEMERAL_SEQUENTIAL 临时顺序节点  ：-es

![](D:\Java\笔记\图片\4-00【Zookeeper】\1-1.png)

## 1.1 安装配置

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

## 1.2 基础命令操作

服务端常用操作：

- 启动 ZooKeeper 服务: `./zkServer.sh start`
- 查看 ZooKeeper 服务状态: `./zkServer.sh status`
- 停止 ZooKeeper 服务: `./zkServer.sh stop` 
- 重启 ZooKeeper 服务: `./zkServer.sh restart` 

客户端常用命令

* 连接Zookeeper服务端 `./zkCli.sh -server ip:port` 
* 断开连接 `quit`
* 查看命令帮助 `help`
* 显示指定目录下面节点 `ls 目录`
* 创建节点 `create /节点path value`
* 创建临时节点，一旦`quit`，会话关闭那么节点就会删除 `create -e /节点path value`
* 创建顺序节点 `create -s /节点path value`
* 查询节点详细信息 `ls -s /节点path`
* 获取节点值 `get /节点path`
* 设置节点值 `set /节点path value`
* 删除单个节点 `delete /节点path`
* 删除带有子节点的节点 `deleteall /节点path`

# 第二章 JavaAPI 操作

Curator 是 Apache ZooKeeper 的Java客户端库。常见的ZooKeeper Java API ：原生Java API、ZkClient、Curator。Curator 项目的目标是简化 ZooKeeper 客户端的使用。

Curator 最初是 Netfix 研发的，后来捐献了 Apache 基金会，目前是 Apache 的顶级项目。官网：http://curator.apache.org/。Curator API 常用操作：建立连接、添加节点、删除节点、修改节点、查询节点、Watch事件监听、分布式锁实现。

创建项目，Maven导入坐标

```xml
<dependencies>

    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.10</version>
        <scope>test</scope>
    </dependency>

    <!--curator-->
    <dependency>
        <groupId>org.apache.curator</groupId>
        <artifactId>curator-framework</artifactId>
        <version>4.0.0</version>
    </dependency>

    <dependency>
        <groupId>org.apache.curator</groupId>
        <artifactId>curator-recipes</artifactId>
        <version>4.0.0</version>
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

</dependencies>


<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

resources目录下面创建log4j.properties文件

```properties
log4j.rootLogger=off,stdout

log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern = [%d{yyyy-MM-dd HH/:mm/:ss}]%-5p %c(line/:%L) %x-%m%n
```

## 2.1 建立连接



## 2.2 添加节点

## 2.3 删除节点

## 2.4 修改节点

## 2.5 查询节点

## 2.6 Watch事件监听

## 2.7 分布式锁

# 第三章 Zookeeper集群
