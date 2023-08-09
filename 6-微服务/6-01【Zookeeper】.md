# 第一章 Zookeeper基础

<!-- ephemeral:短暂的； sequential:顺序的； -->

Zookeeper 是 Apache Hadoop 项目下的一个子项目，是一个树形目录服务。Zookeeper 翻译过来就是动物园管理员，他是用来管 Hadoop（大象）、Hive（蜜蜂）、Pig（小猪）的管理员。简称zk。

Zookeeper 是一个分布式的、开源的分布式应用程序的协调服务。Zookeeper 提供的主要功能包括：配置管理、分布式锁、集群管理。

ZooKeeper 是一个树形目录服务，其数据模型和Unix的文件系统目录树很类似，拥有一个层次化结构。这里面的每一个节点都被称为  ZNode，每个节点上都会保存自己的数据和节点信息。 节点可以拥有子节点，同时也允许少量（1MB）数据存储在该节点之下。节点可以分为四大类：

- PERSISTENT 持久化节点 
- EPHEMERAL 临时节点 ：-e
- PERSISTENT_SEQUENTIAL 持久化顺序节点 ：-s
- EPHEMERAL_SEQUENTIAL 临时顺序节点  ：-es

![](D:\Java\笔记\图片\6-01【Zookeeper】\1-1ZNode.png)

## 1.1 安装配置

前提：ZooKeeper 服务器是用 Java 创建的，它运行在 JVM 之上。需要安装 JDK 7 或更高版本。

```sh
# /export/目录下面创建这三个文件夹 用于存放 数据 软件 安装包
[root@node1 ~]# ls /export/
data  server  software
```

**上传压缩包**

```shell
# 使用sftp协议传输文件，首先使用 alt+p 打开sftp窗口，然后就可以使用 put 命令上传、get 命令下载
# 进入sftp窗口，进入/export/software/目录
sftp> cd /export/software/
# 发现该目录下面没有任何文件，这里不能使用ll命令
sftp> ls
# 上传zookeeper至/export/software/目录
sftp> put E:\VMware\uploads\apache-zookeeper-3.5.6-bin.tar.gz
# Ctrl + D 关闭 sftp 连接
```

**解压及配置**

```sh
# 将tar包解压到/export/server/目录下
[root@node1 software]# tar -zxvf apache-zookeeper-3.5.6-bin.tar.gz -C /export/server/

# 进入到conf目录修改配置文件
[root@node1 software]# cd /export/server/apache-zookeeper-3.5.6-bin/conf/
[root@node1 conf]# ll
total 12
-rw-r--r-- 1 1000 1000  535 Oct  5  2019 configuration.xsl
-rw-r--r-- 1 1000 1000 2712 Oct  5  2019 log4j.properties
-rw-r--r-- 1 1000 1000  922 Oct  9  2019 zoo_sample.cfg
# 拷贝文件。zoo_sample.cfg默认是不生效的，想要让它生效需要拷贝一份zoo.cfg文件生效
[root@node1 conf]# cp zoo_sample.cfg zoo.cfg
[root@node1 conf]# ll
total 16
-rw-r--r-- 1 1000 1000  535 Oct  5  2019 configuration.xsl
-rw-r--r-- 1 1000 1000 2712 Oct  5  2019 log4j.properties
-rw-r--r-- 1 root root  922 Jul 23 09:42 zoo.cfg
-rw-r--r-- 1 1000 1000  922 Oct  9  2019 zoo_sample.cfg

# 打开目录
[root@node1 conf]# cd /export/data
# 创建zooKeeper存储目录
[root@node1 data]# mkdir zkdata
# 进入存储目录，获取目录路径
[root@node1 data]# cd zkdata/
[root@node1 zkdata]# pwd
/export/data/zkdata
# 修改zoo.cfg的存储目录
[root@node1 zkdata]# vim /export/server/apache-zookeeper-3.5.6-bin/conf/zoo.cfg
# 将存储目录修改为/export/data/zkdata 也就是我们上面创建的存储目录
dataDir=/export/data/zkdata
```

**启动ZooKeeper**

```shell
cd /export/server/apache-zookeeper-3.5.6-bin/bin/
# 启动
./zkServer.sh start
```

* 启动成功

  ```sh
  [root@localhost apache-zookeeper-3.5.6-bin]# bin/zkServer.sh start
  ZooKeeper JMX enabled by default
  Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
  Starting zookeeper ... STARTED
  ```

* 启动失败

  ```sh
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
  # the port at which the clients will connect 默认端口号2181
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
[root@localhost apache-zookeeper-3.5.6-bin]# ./zkServer.sh stop
ZooKeeper JMX enabled by default
Using config: /opt/zookper/apache-zookeeper-3.5.6-bin/bin/../conf/zoo.cfg
Stopping zookeeper ... STOPPED
```

**查看ZooKeeper状态**

```shell
./zkServer.sh status
```

zookeeper 启动成功。standalone 代表 zk 没有搭建集群，现在是单节点

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

**服务端常用操作**

| 作用                    | 命令                  |      |
| ----------------------- | --------------------- | ---- |
| 启动 ZooKeeper 服务     | ./zkServer.sh start   |      |
| 查看 ZooKeeper 服务状态 | ./zkServer.sh status  |      |
| 停止 ZooKeeper 服务     | ./zkServer.sh stop    |      |
| 重启 ZooKeeper 服务     | ./zkServer.sh restart |      |

**客户端常用命令**

| 作用                                               | 命令                       |
| -------------------------------------------------- | -------------------------- |
| 连接Zookeeper服务端                                | ./zkCli.sh -server ip:port |
| 断开连接                                           | quit                       |
| 查看命令帮助                                       | help                       |
| 显示指定目录下面节点                               | ls 目录                    |
| 创建节点                                           | create /节点path value     |
| 创建临时节点，一旦`quit`，会话关闭那么节点就会删除 | create -e /节点path value  |
| 创建顺序节点                                       | create -s /节点path value  |
| 查询节点详细信息                                   | ls -s /节点path            |
| 获取节点值                                         | get /节点path              |
| 设置节点值                                         | set /节点path value        |
| 删除单个节点                                       | delete /节点path           |
| 删除带有子节点的节点                               | deleteall /节点path        |

# 第二章 JavaAPI 操作

Curator 是 Apache ZooKeeper 的Java客户端库。常见的ZooKeeper Java API ：原生Java API、ZkClient、Curator。Curator 项目的目标是简化 ZooKeeper 客户端的使用。

Curator 最初是 Netfix 研发的，后来捐献了 Apache 基金会，目前是 Apache 的顶级项目。官网：http://curator.apache.org/。Curator API 常用操作：建立连接、添加节点、删除节点、修改节点、查询节点、Watch事件监听、分布式锁实现。

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
        <!-- 官网有介绍，对于ZooKeeper为3.5.x及以上，curator应该使用4.0，当然curator可以向下兼容 -->
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

```java
/**
 * 测试连接
 * test connection
 */
@Test
public void testConnect() {
    // 第一种方式创建连接
    // 创建重试策略对象
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
    /*
     * @param connectString       连接字符串。zk server地址和端口，例如"192.168.88.151:2181"
     * @param sessionTimeoutMs    会话超时时间 单位为ms。不写有默认值
     * @param connectionTimeoutMs 连接超时时间 单位为ms。不写有默认值
     * @param retryPolicy         重试策略
     */
    CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.88.151:2181",
                                                                60 * 1000, 15 * 1000, retryPolicy);
    // 开启连接
    client.start();
}
```

```java
/**
 * 测试连接
 * test connection
 */
@Test
public void testConnect() {
    // 第二种方式创建连接
    // 创建重试策略对象
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
    CuratorFramework client = CuratorFrameworkFactory.builder()
        .connectString("192.168.88.151:2181")
        .sessionTimeoutMs(60 * 1000)
        .connectionTimeoutMs(15 * 1000)
        .retryPolicy(retryPolicy)
        .namespace("linxuan") // 添加上命名空间，这样所有的操作都在linxuan节点下面
        .build();
    // 开启连接
    client.start();
}
```

## 2.2 创建节点

ZK 中节点有四种：persistent持久化节点、 ephemeral临时节点、persistent_sequential持久化顺序节点、ephemeral_sequential临时顺序节点。而在此基础上，节点又可以添加数据亦或是不添加数据。

1. 创建基本节点：`create().forPath("")`
2. 创建数据节点：`create().forPath("")`
3. 创建类型节点：`create().withMode().forPath("", data)`
4. 创建多级节点：`create().creatingParentsIfNeeded().forPath("", data)`

**创建基本节点**

```sh
# 客户端连接
[root@node1 bin]# ./zkCli.sh -server localhost:2181
[zk: localhost:2181(CONNECTED) 0] ls /
[zookeeper]
[zk: localhost:2181(CONNECTED) 1] 
```

```java
public class CuratorTest {

    // 设置一个全局变量
    private CuratorFramework client;

    /**
     * 其他Test方法执行之前就会执行，给全局变量client赋值
     * test connection
     */
    @Before
    public void testConnect() {
        // 第二种方式创建连接
        // 创建重试策略对象
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.88.151:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(retryPolicy)
                .namespace("linxuan") // 添加上命名空间，这样所有的操作都在linxuan节点下面
                .build();
        client.start();
    }

    @Test
    public void testCreate() throws Exception {
        // 创建一个基本的节点，如果没有指定数据，则默认将当前客户端IP作为数据存储。返回创建的路径
        String path = client.create().forPath("/app1");
        System.out.println(path);
    }

    @After
    public void testClose() {
        if (client != null) {
            client.close();
        }
    }
}
```

```sh
[zk: localhost:2181(CONNECTED) 1] ls /
[linxuan, zookeeper]
[zk: localhost:2181(CONNECTED) 2] ls /linxuan
[app1]
[zk: localhost:2181(CONNECTED) 3] get /linxuan/app1
192.168.88.1
[zk: localhost:2181(CONNECTED) 4] 
```

**创建数据节点**

```java
/**
 * 测试创建节点
 * @throws Exception 创建的节点可能抛异常
 */
@Test
public void testCreate() throws Exception {
    // forPath方法第二个参数是字节数组，所以字符串要转成字节数组
    String path = client.create().forPath("/app2", "linxuan你好".getBytes());
    System.out.println(path);
}
```

```sh
[zk: localhost:2181(CONNECTED) 7] ls /linxuan
[app1, app2]
[zk: localhost:2181(CONNECTED) 8] get /linxuan/app2
linxuan你好
[zk: localhost:2181(CONNECTED) 9] 
```

**创建类型节点**

```java
/**
 * 测试创建节点
 * @throws Exception 创建的节点可能抛异常
 */
@Test
public void testCreate() throws Exception {
    // 使用withMode即可创建其他类型节点，参数为枚举类型
    // 创建一个临时节点，当前Java客户端会话完毕就会删除，因此无法在服务端查看，除非while死循环
    String path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/app3");
    System.out.println(path);
}
```

```sh
[root@node1 ~]# cd /export/server/apache-zookeeper-3.5.6-bin/bin/
[root@node1 bin]# ./zkCli.sh -server localhost:2181
[zk: localhost:2181(CONNECTED) 0] ls /linxuan
[app1, app2]
[zk: localhost:2181(CONNECTED) 1] 
```

**创建多级节点**

```java
/**
 * 测试创建节点
 * @throws Exception 创建的节点可能抛异常
 */
@Test
public void testCreate() throws Exception {
    // 使用creatingParentContainersIfNeeded()方法可以创建多级节点
    // 创建多级节点/linxuan/app3/p1(之前将linxuan设置为了命名空间)
    String path = client.create().creatingParentContainersIfNeeded().forPath("/app3/p1");
    System.out.println(path);
}
```

## 2.3 查询节点

可以将查询节点大致分为三类：查询节点数据、查询子节点、查询节点状态信息。

* 查询节点数据原生 API 为`get`，Curator 的 API 为`getData().forPath()`
* 查询子节点原生 API 为`ls`，Curator 的 API 为`getChildren().forPath()`
* 查询节点状态信息原生 API 为`ls -s`，Curator 的 API 为`getData().storingStatIn(状态对象).forPath()`

```java
public class CuratorTest {

    // 设置一个全局变量
    private CuratorFramework client;

    /**
     * 其他Test方法执行之前就会执行，给全局变量client赋值
     * test connection
     */
    @Before
    public void testConnect() {
        // 第二种方式创建连接
        // 创建重试策略对象
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        client = CuratorFrameworkFactory.builder()
            .connectString("192.168.88.151:2181")
            .sessionTimeoutMs(60 * 1000)
            .connectionTimeoutMs(15 * 1000)
            .retryPolicy(retryPolicy)
            .namespace("linxuan") // 添加上命名空间，这样所有的操作都在linxuan节点下面
            .build();
        client.start();
    }

    /**
     * 测试方法执行完成之后将连接给关闭掉
     */
    @After
    public void testClose() {
        if (client != null) {
            client.close();
        }
    }
}
```

**查询节点数据**

```java
/**
 * 测试查询节点
 */
@Test
public void testGet() throws Exception {
    byte[] data = client.getData().forPath("/app1");
    System.out.println(new String(data));
}
```

**查询子节点**

```java
/**
 * 测试查询节点
 */
@Test
public void testGet() throws Exception {
    // 查询/linxuan节点下面的所有子节点
    List<String> list = client.getChildren().forPath("/");
    System.out.println(list);
}
```

**查询节点状态**

该API因为某些历史遗留问题，所以没有单独API，应该使用`getData().storingStain(状态对象).forPath()`

```java
/**
 * 测试查询节点
 */
@Test
public void testGet() throws Exception {
    // 创建状态对象并调用toString方法打印
    Stat stat = new Stat();
    System.out.println(stat);
    // 获取该节点的状态封装至stat状态对象中并打印
    client.getData().storingStatIn(stat).forPath("/app1");
    System.out.println(stat);
}
```

## 2.5 修改节点

修改节点数据有两种情况：直接修改、根据版本号进行修改。根据版本号修改可以避免多线程下其他线程同时操作该节点的问题。

**直接修改节点数据**

```java
/**
 * 测试修改节点
 */
@Test
public void testSet() throws Exception {
    byte[] result = client.getData().forPath("/app1");
    System.out.println("未修改之前的/linxuan/app1节点值：" + new String(result));
    client.setData().forPath("/app1", "haha".getBytes());
}
```

**根据版本号修改**

```java
/**
 * 测试修改节点
 */
@Test
public void testSet() throws Exception {
    Stat stat = new Stat();
    // 获取/linxuan/app1节点状态信息，状态信息里面包括版本号
    client.getData().storingStatIn(stat).forPath("/app1");
    int version = stat.getVersion();
    // 根据版本号进行修改节点数据
    client.setData().withVersion(version).forPath("/app1", "hehe".getBytes());
}
```

## 2.4 删除节点

删除节点也分为多种情况：删除单个节点、删除带有子节点的节点、必须成功删除节点、。

* 删除单个节点：`delete().forPath("")`
* 删除带有子节点的节点：`delete().deletingChildrenIfNeeded().forPath("")`
* 必须成功删除节点：`delete().guaranteed().forPath("")`
* 删除之后调用回调函数：`delete().guaranteed().inBackground(BackgroundCallback接口).forPath("")`

**删除单个节点**

```java
/**
 * 测试删除节点
 * @throws Exception 可能抛出的异常
 */
@Test
public void testDelete() throws Exception {
    client.delete().forPath("/app1");
}
```

**删除带有子节点的节点**

```java
/**
 * 测试删除节点
 * @throws Exception 可能抛出的异常
 */
@Test
public void testDelete() throws Exception {
    // 删除带有子节点的节点
    client.delete().deletingChildrenIfNeeded().forPath("/app3");
}
```

**必须成功删除节点**

如果有网络抖动的情况，那么会导致删除节点可能失败，因此大部分情况都会强制删除节点。

```java
/**
 * 测试删除节点
 * @throws Exception 可能抛出的异常
 */
@Test
public void testDelete() throws Exception {
    // 强制删除该节点，即使有网络抖动。本质就是重试
    client.delete().guaranteed().forPath("/app2");
}
```

**删除之后调用回调函数**

```java
/**
 * 测试删除节点
 * @throws Exception 可能抛出的异常
 */
@Test
public void testDelete() throws Exception {
    // 强制删除该节点，并且加上回调函数。这里使用Lambda函数
    client.delete().guaranteed().inBackground((client, event) -> {
        System.out.println("我被删除了");
        // CuratorEventImpl{type=DELETE, resultCode=0, path='/app1', name='null', children=null, context=null, stat=null, data=null, watchedEvent=null, aclList=null, opResults=null}
        System.out.println(event);
    }).forPath("/app1");
}
```

## 2.6 Watch事件监听

ZooKeeper 允许用户在指定节点上注册一些 Watcher，并且在一些特定事件触发的时候，ZooKeeper 服务端会将事件通知到感兴趣的客户端上去，该机制是 ZooKeeper 实现分布式协调服务的重要特性。

ZooKeeper 中引入了 Watcher 机制来实现了发布 / 订阅功能，能够让多个订阅者同时监听某一个对象，当一个对象自身状态变化时，会通知所有订阅者。

![](D:\Java\笔记\图片\6-01【Zookeeper】\1-1ZNode.png)

ZooKeeper 原生支持通过注册 Watcher 来进行事件监听，但是其使用并不是特别方便，需要开发人员自己反复注册Watcher，比较繁琐。因此 Curator 引入了 Cache 来实现对 ZooKeeper 服务端事件的监听。

ZooKeeper提供了三种 Watcher：

* NodeCache：只是监听某一个特定的节点
* PathChildrenCache：监控一个ZNode的子节点. 
* TreeCache：可以监控整个树上的所有节点，类似于 PathChildrenCache 和 NodeCache 的组合

### 2.6.1 NodeCache

```java
public class CuratorWatcherTest {

    // 设置一个全局变量
    private CuratorFramework client;

    /**
     * 其他Test方法执行之前就会执行，给全局变量client赋值
     */
    @Before
    public void testConnect() {
        // 创建重试策略对象
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        client = CuratorFrameworkFactory.builder()
            .connectString("192.168.88.151:2181")
            .sessionTimeoutMs(60 * 1000)
            .connectionTimeoutMs(15 * 1000)
            .retryPolicy(retryPolicy)
            .namespace("linxuan") // 添加上命名空间，这样所有的操作都在linxuan节点下面
            .build();
        client.start();
    }

    /**
     * 测试方法执行完成之后将连接给关闭掉
     */
    @After
    public void testClose() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * 给指定的节点注册监听器
     */
    @Test
    public void testNodeCache() throws Exception {
        // 创建NodeCache对象
        final NodeCache nodeCache = new NodeCache(client, "/app1");
        // 注册监听
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            // 增删改都会执行
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点发生变化了");
                byte[] data = nodeCache.getCurrentData().getData();
                System.out.println("变化后的数据为：" + new String(data));
            }
        });

        // 开启监听，如果设置为true，则加载缓冲数据
        nodeCache.start(true);

        // 开启while循环，让单元测试一直执行
        while (true) {

        }
    }
}
```

### 2.6.2 PathChildrenCache

```java
/**
 * 测试PathChildrenCache：监听某个节点的所有子节点，无法监听自己
 */
@Test
public void testPathChildrenCache() throws Exception {
    // 创建监听对象
    PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/app1", true);

    // 绑定监听器
    pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
        @Override
        public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
            System.out.println("子节点变化了");
            System.out.println(event);
            // 监听子节点的数据变更，并且拿到变更后的数据
            // 获取类型
            PathChildrenCacheEvent.Type type = event.getType();
            if (type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                // PathChildrenCacheEvent{type=CHILD_UPDATED, data=ChildData{path='/app1/p1', stat=71,72,1690160676406,1690160687954,1,0,0,0,5,0,71, data=[110, 105, 104, 97, 111]}}
                byte[] data = event.getData().getData();
                System.out.println("修改后的数据为：" + new String(data));
            }
        }
    });

    // 开启监听
    pathChildrenCache.start();

    // 强制一直运行
    while(true) {

    }
}
```

### 2.6.3 TreeCache

TreeCache：可以监控整个树上的所有节点，类似于 PathChildrenCache 和 NodeCache 的组合

## 2.7 分布式锁

### 2.7.1 分布式锁介绍

在我们进行单机应用开发，涉及并发同步的时候，我们往往采用 synchronized 或者 Lock 的方式来解决多线程间的代码同步问题，这时多线程的运行都是在同一个JVM之下，没有任何问题。

但当我们的应用是分布式集群工作的情况下，属于多 JVM 下的工作环境，跨 JVM 之间已经无法通过多线程的锁解决同步问题。那么就需要一种更加高级的锁机制，来处理种跨机器的进程之间的数据同步问题——这就是分布式锁。

通常实现分布式锁有多种方案：

* 基于缓存实现分布式锁，使用 Redis 或者 Memcache
* Zookeeper 实现分布式锁，Curator 封装了很多方法简化我们开发
* 数据库层面实现分布式锁，使用乐观锁或者悲观锁

![](D:\Java\笔记\图片\6-01【Zookeeper】\2-1Distributed Lock.png)

•核心思想：当客户端要获取锁，则创建节点，使用完锁，则删除该节点。

1. 客户端获取锁时，在 lock 节点下创建临时顺序节点。创建临时节点是因为如果客户端宕机，那么获取到的锁也能够立马释放。创建顺序节点是获取到子节点序号最小的，用以判断是否获取到锁。
2. 然后获取 lock 下面的所有子节点，客户端获取到所有的子节点之后，如果发现自己创建的子节点序号最小，那么就认为该客户端获取到了锁。使用完锁后，将该节点删除。
3. 如果发现自己创建的节点并非 lock 所有子节点中最小的，说明自己还没有获取到锁，此时客户端需要找到比自己小的那个节点，同时对其注册事件监听器，监听删除事件。
4. 如果发现比自己小的那个节点被删除，则客户端的 Watcher 会收到相应通知，此时再次判断自己创建的节点是否是 lock 子节点中序号最小的。如果是则获取到了锁，如果不是则重复以上步骤继续获取到比自己小的一个节点并注册监听。

### 2.7.2 分布式锁模拟

在 Curator 中有五种锁方案：

- InterProcessSemaphoreMutex：分布式排它锁（非可重入锁）
- InterProcessMutex：分布式可重入排它锁
- InterProcessReadWriteLock：分布式读写锁
- InterProcessMultiLock：将多个锁作为单个实体管理的容器
- InterProcessSemaphoreV2：共享信号量

```java
public class Ticket12306 implements Runnable {

    // 数据库剩余的票数
    private int tickets = 10;

    private InterProcessMutex lock;

    public Ticket12306() {
        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        // 创建CuratorFramework对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.88.151:2181")
            .sessionTimeoutMs(60 * 1000)
            .connectionTimeoutMs(15 * 1000)
            .retryPolicy(retryPolicy)
            .namespace("linxuan") // 添加上命名空间，这样所有的操作都在linxuan节点下面
            .build();
        // 开启连接
        client.start();
        lock = new InterProcessMutex(client, "/lock");
    }

    @Override
    public void run() {
        while (true) {
            // 获取锁
            try {
                lock.acquire(3, TimeUnit.SECONDS);
                if (tickets > 0) {
                    Thread.sleep(100);
                    System.out.println(Thread.currentThread() + ":" + tickets);
                    tickets--;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    // 释放锁
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

```java
public class LockTest {
    public static void main(String[] args) {
        Ticket12306 ticket12306 = new Ticket12306();

        // 创建客户端
        Thread t1 = new Thread(ticket12306, "携程");
        Thread t2 = new Thread(ticket12306, "飞猪");

        t1.start();
        t2.start();
    }
}
```

# 第三章 Zookeeper集群

![](D:\Java\笔记\图片\6-01【Zookeeper】\3-1Zookeeper colony .png)

Leader 选举的因素有三个：

* Serverid：服务器ID， 编号越大在选择算法中的权重越大。
* Zxid：数据ID，服务器中存放的最大数据 ID 值越大说明数据越新，在选举算法中数据越新权重越大。
* 在Leader选举的过程中，如果某台 ZooKeeper 获得了超过半数的选票，则此 ZooKeeper 就可以成为 Leader了。

## 3.1 集群角色详解

![](D:\Java\笔记\图片\6-01【Zookeeper】\3-1Colony character .png)

在 ZooKeeper 集群服务中有三个角色：

1. Leader 领导者：处理事务请求；集群内部各服务器的调度者。
2. Follower 跟随者 ：处理客户端非事务请求，转发事务请求给Leader服务器；参与Leader选举投票。
3. Observer 观察者：处理客户端非事务请求，转发事务请求给Leader服务器。

事务请求：增删改就是事务请求，一般查询就是非事务请求

## 3.1 搭建伪集群

真实的集群是需要部署在不同的服务器上的，但是在我们测试时同时启动很多个虚拟机内存会吃不消，所以我们通常会搭建伪集群，也就是把所有的服务都搭建在一台虚拟机上，用端口进行区分。我们这里搭建一个三个节点的 Zookeeper 伪集群。

### 3.1.1 配置集群

建立目录，将 Zookeeper 复制到目录

```sh
[root@node1 server]# ll
total 0
drwxr-xr-x 7 root root 146 Jul 23 09:50 apache-zookeeper-3.5.6-bin
drwxr-xr-x 8   10  143 255 Jul 22  2017 jdk1.8.0_144
drwxr-xr-x 6 root root  89 Mar  3  2020 kafka_2.12-2.4.1
[root@node1 server]# mkdir /usr/local/zookeeper-cluster
[root@node1 server]# ls /usr/local
bin  etc  games  include  lib  lib64  libexec  sbin  share  src  zookeeper-cluster
[root@node1 server]# cp -r  apache-zookeeper-3.5.6-bin /usr/local/zookeeper-cluster/zookeeper-1
[root@node1 server]# cp -r  apache-zookeeper-3.5.6-bin /usr/local/zookeeper-cluster/zookeeper-2
[root@node1 server]# cp -r  apache-zookeeper-3.5.6-bin /usr/local/zookeeper-cluster/zookeeper-3
[root@node1 server]# ll /usr/local/zookeeper-cluster/                                           
total 0
drwxr-xr-x 7 root root 146 Jul 24 14:43 zookeeper-1
drwxr-xr-x 7 root root 146 Jul 24 14:44 zookeeper-2
drwxr-xr-x 7 root root 146 Jul 24 14:44 zookeeper-3
```

创建 data 目录 ，并且将 conf 下 zoo_sample.cfg 文件改名为 zoo.cfg

```sh
# 创建 data 目录
[root@node1 server]# mkdir /usr/local/zookeeper-cluster/zookeeper-1/data
[root@node1 server]# mkdir /usr/local/zookeeper-cluster/zookeeper-2/data
[root@node1 server]# mkdir /usr/local/zookeeper-cluster/zookeeper-3/data
# 之前已经复制出来了 zoo.cfg
[root@node1 server]# ll /usr/local/zookeeper-cluster/zookeeper-1/conf/
total 16
-rw-r--r-- 1 root root  535 Jul 24 14:43 configuration.xsl
-rw-r--r-- 1 root root 2712 Jul 24 14:43 log4j.properties
-rw-r--r-- 1 root root  927 Jul 24 14:43 zoo.cfg
-rw-r--r-- 1 root root  922 Jul 24 14:43 zoo_sample.cfg
```

配置每一个 Zookeeper 的 dataDir 和 clientPort

```sh
[root@node1 zookeeper-cluster]# ll
total 0
drwxr-xr-x 8 root root 158 Jul 24 14:48 zookeeper-1
drwxr-xr-x 8 root root 158 Jul 24 14:49 zookeeper-2
drwxr-xr-x 8 root root 158 Jul 24 14:49 zookeeper-3

# 配置第一台Zookeeper的 dataDir 和 clientPort
[root@node1 data]# pwd
/usr/local/zookeeper-cluster/zookeeper-1/data
[root@node1 data]# vim ../conf/zoo.cfg 
dataDir=/usr/local/zookeeper-cluster/zookeeper-1/data
clientPort=2181

# 配置第二台Zookeeper的 dataDir 和 clientPort
[root@node1 zookeeper-cluster]# vim /usr/local/zookeeper-cluster/zookeeper-2/conf/zoo.cfg
dataDir=/usr/local/zookeeper-cluster/zookeeper-2/data
clientPort=2182

# 配置第三台Zookeeper的 dataDir 和 clientPort
[root@node1 zookeeper-cluster]# vim /usr/local/zookeeper-cluster/zookeeper-3/conf/zoo.cfg
dataDir=/usr/local/zookeeper-cluster/zookeeper-3/data
clientPort=2183
```

在每个 zookeeper 的 data 目录下创建一个 myid 文件，内容分别是1、2、3 。这个文件就是记录每个服务器的ID

```sh
[root@node1 zookeeper-cluster]# echo 1 >/usr/local/zookeeper-cluster/zookeeper-1/data/myid
[root@node1 zookeeper-cluster]# echo 2 >/usr/local/zookeeper-cluster/zookeeper-2/data/myid
[root@node1 zookeeper-cluster]# echo 3 >/usr/local/zookeeper-cluster/zookeeper-3/data/myid
[root@node1 zookeeper-cluster]# cat /usr/local/zookeeper-cluster/zookeeper-1/data/myid 
1
```

在每一个 zookeeper 的 zoo.cfg 配置客户端访问端口（clientPort）和集群服务器IP列表

```sh
# server.服务器ID=服务器IP地址：服务器之间通信端口：服务器之间投票选举端口。
# 伪集群动端口，如果是真集群那么修改IP地址，端口都不能够动，服务器之间通信端口2881、服务器之间投票选举端口3991

# 在第一台zookeeper里面配置客户端访问端口（clientPort）和集群服务器IP列表
[root@node1 zookeeper-cluster]# vim /usr/local/zookeeper-cluster/zookeeper-1/conf/zoo.cfg
server.1=192.168.88.151:2881:3881
server.2=192.168.88.151:2882:3882
server.3=192.168.88.151:2883:3883
# 在第二台zookeeper里面配置客户端访问端口（clientPort）和集群服务器IP列表
[root@node1 zookeeper-cluster]# vim /usr/local/zookeeper-cluster/zookeeper-2/conf/zoo.cfg
server.1=192.168.88.151:2881:3881
server.2=192.168.88.151:2882:3882
server.3=192.168.88.151:2883:3883
# 在第三台zookeeper里面配置客户端访问端口（clientPort）和集群服务器IP列表
[root@node1 zookeeper-cluster]# vim /usr/local/zookeeper-cluster/zookeeper-3/conf/zoo.cfg
server.1=192.168.88.151:2881:3881
server.2=192.168.88.151:2882:3882
server.3=192.168.88.151:2883:3883
```

### 3.1.3 启动关闭

启动集群就是分别启动每个实例

```sh
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-1/bin/zkServer.sh start
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-2/bin/zkServer.sh start
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-3/bin/zkServer.sh start
```

启动后我们查询一下每个实例的运行状态

```sh
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-1/bin/zkServer.sh status
# Mode为follower表示是跟随者（从）
Mode: follower
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-2/bin/zkServer.sh status
# Mod 为leader表示是领导者（主）
Mode: leader
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-3/bin/zkServer.sh status
# Mode为follower表示是跟随者（从）
Mode: follower
```

关闭集群

```sh
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-1/bin/zkServer.sh stop
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-2/bin/zkServer.sh stop
[root@node1 zookeeper-cluster]# /usr/local/zookeeper-cluster/zookeeper-3/bin/zkServer.sh stop
```

### 3.1.4 集群故障

* 三台服务器，只有一台从服务器宕机，那么集群不会出现问题
* 三台服务器，两台从服务器宕机，那么主服务器也会宕机。因为可运行的机器没有超过集群总数量的半数。
* 再次启动一台从服务器，那么这时主服务器也会正常工作，并且仍然是领导者。
* 集群中的主服务器挂了，集群中的其他服务器会自动进行选举状态，然后产生新得 leader 
* 当领导者产生后，再次有新服务器加入集群，不会影响到现任领导者

## 3.2 搭建真集群

**搭建集群**

```sh
# 查看配置的主机别名
[root@node1 server]# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.88.151 node1 node1.linxuan.com
192.168.88.152 node2 node2.linxuan.com
192.168.88.153 node3 node3.linxuan.com

# 将Zookeeper复制到node2和node3服务器上的/export/server目录下面
[root@node1 server]# scp -r apache-zookeeper-3.5.6-bin/ node2.linxuan.com:/export/server
[root@node1 server]# scp -r apache-zookeeper-3.5.6-bin/ node3.linxuan.com:/export/server

# 将数据目录也复制到其他两台服务器上面
[root@node1 server]# scp -r /export/data/zkdata/ node2.linxuan.com:/export/data
[root@node1 server]# scp -r /export/data/zkdata/ node3.linxuan.com:/export/data

# 在每台服务器的zookeeper的data目录下面创建myid文件并设置每个服务器的ID
[root@node1 data]# echo 1 >/export/data/zkdata/myid
[root@node2 ~]# echo 2 >/export/data/zkdata/myid
[root@node3 ~]# echo 3 >/export/data/zkdata/myid
```

在每一个 zookeeper 的 zoo.cfg 配置客户端访问端口（clientPort）和集群服务器IP列表

```sh
# server.服务器ID=服务器IP地址：服务器之间通信端口：服务器之间投票选举端口。
# 伪集群动端口，如果是真集群那么修改IP地址，端口都不能够动，服务器之间通信端口2881、服务器之间投票选举端口3991

# 在node1服务器里面配置客户端访问端口（clientPort）和集群服务器IP列表
[root@node1 apache-zookeeper-3.5.6-bin]# vim /export/server/apache-zookeeper-3.5.6-bin/conf/zoo.cfg
server.1=192.168.88.151:2881:3881
server.2=192.168.88.152:2881:3881
server.3=192.168.88.153:2881:3881
# 在node2服务器里面配置客户端访问端口（clientPort）和集群服务器IP列表
[root@node2 apache-zookeeper-3.5.6-bin]#  vim /export/server/apache-zookeeper-3.5.6-bin/conf/zoo.cfg
server.1=192.168.88.151:2881:3881
server.2=192.168.88.152:2881:3881
server.3=192.168.88.153:2881:3881
# 在node3服务器里面配置客户端访问端口（clientPort）和集群服务器IP列表
[root@node3 apache-zookeeper-3.5.6-bin]#  vim /export/server/apache-zookeeper-3.5.6-bin/conf/zoo.cfg
server.1=192.168.88.151:2881:3881
server.2=192.168.88.152:2881:3881
server.3=192.168.88.153:2881:3881
```

**启动与关闭**

```sh
[root@node1 apache-zookeeper-3.5.6-bin]# ./bin/zkServer.sh start
[root@node2 apache-zookeeper-3.5.6-bin]# ./bin/zkServer.sh start
[root@node3 apache-zookeeper-3.5.6-bin]# ./bin/zkServer.sh start
```

```sh
[root@node1 apache-zookeeper-3.5.6-bin]# /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh stop
[root@node2 apache-zookeeper-3.5.6-bin]# /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh stop
[root@node3 apache-zookeeper-3.5.6-bin]# /export/server/apache-zookeeper-3.5.6-bin/bin/zkServer.sh stop
```

