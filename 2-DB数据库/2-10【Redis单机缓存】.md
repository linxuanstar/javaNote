# 第一章 NOSQL

NoSQL(NoSQL = Not Only SQL)，意即“不仅仅是SQL”，是一项全新的数据库理念，泛指非关系型的数据库。

随着互联网web2.0网站的兴起，传统的关系数据库在应付web2.0网站，特别是超大规模和高并发的SNS类型的web2.0纯动态网站已经显得力不从心，暴露了很多难以克服的问题，而非关系型的数据库则由于其本身的特点得到了非常迅速的发展。NoSQL数据库的产生就是为了解决大规模数据集合多重数据种类带来的挑战，尤其是大数据应用难题。

NOSQL数据库与关系型数据库对比：

* 成本：nosql数据库简单易部署，基本都是开源软件，不需要像使用oracle那样花费大量成本购买使用，相比关系型数据库价格便宜。
* 查询速度：nosql数据库将数据存储于缓存之中，关系型数据库将数据存储在硬盘中，自然查询速度远不及nosql数据库。NoSql数据库性能高，性能NOSQL是基于键值对的，可以想象成表中的主键和值的对应关系，而且不需要经过SQL层的解析，所以性能非常高。
* 存储数据的格式：nosql的存储格式是`key,value`形式、文档形式、图片形式等等，所以可以存储基础类型以及对象或者是集合等各种格式，而数据库则只支持基础类型。
* 扩展性：关系型数据库有类似join这样的多表查询机制的限制导致扩展很艰难。而非关系型数据库有很高的可扩展性。可扩展性同样也是因为基于键值对，数据之间没有耦合性，所以非常容易水平扩展。
* 维护的工具和资料有限，因为nosql是属于新的技术，不能和关系型数据库10几年的技术同日而语。
* 不提供对sql的支持，如果不支持sql这样的工业标准，将产生一定用户的学习和使用成本。
* 不提供关系型数据库对事务的处理。

关系型数据库与NoSQL数据库并非对立而是互补的关系，即通常情况下使用关系型数据库，在适合使用NoSQL的时候使用NoSQL数据库，让NoSQL数据库对关系型数据库的不足进行弥补。一般会将数据存储在关系型数据库中，在nosql数据库中备份存储关系型数据库的数据

## 1.1 主流的NOSQL产品

**键值`(Key-Value)`存储数据库**

* 相关产品： `Tokyo Cabinet/Tyrant`、`Redis`、`Voldemort`、`Berkeley DB`
* 典型应用： 内容缓存，主要用于处理大量数据的高访问负载。 
* 数据模型： 一系列键值对
* 优势： 快速查询
* 劣势： 存储的数据缺少结构化

**列存储数据库**

* 相关产品：`Cassandra`, `HBase`, `Riak`
* 典型应用：分布式的文件系统
* 数据模型：以列簇式存储，将同一列数据存在一起
* 优势：查找速度快，可扩展性强，更容易进行分布式扩展
* 劣势：功能相对局限

**文档型数据库**

* 相关产品：`CouchDB`、`MongoDB`
* 典型应用：Web应用（与Key-Value类似，Value是结构化的）
* 数据模型： 一系列键值对
* 优势：数据结构要求不严格
* 劣势： 查询性能不高，而且缺乏统一的查询语法

**图形`(Graph)`数据库**

* 相关数据库：`Neo4J`、`InfoGrid`、`Infinite Graph`
* 典型应用：社交网络
* 数据模型：图结构
* 优势：利用图结构相关算法。
* 劣势：需要对整个图做计算才能得出结果，不容易做分布式的集群方案。

## 1.2 Redis介绍

<!--daemonize 守护进程;-->

Redis是一款高性能的NOSQL系列的非关系型数据库。Redis是用C语言开发的一个开源的高性能键值对（key-value）数据库，且Redis通过提供多种键值数据类型来适应不同场景下的存储需求。

redis的应用场景如下：缓存（数据查询、短连接、新闻内容、商品内容等等）、聊天室的在线好友列表、任务队列（秒杀、抢购、12306等等）、应用排行榜、网站访问统计、数据过期处理（可以精确到毫秒）、分布式集群架构中的`session`分离。

目前为止Redis支持的键值数据类型如下：字符串类型 string、哈希类型 hash、列表类型 list、集合类型 set、有序集合类型 sortedset

官网：https://redis.io，中文网：http://www.redis.net.cn/。

绿色版，直接解压便可以使用，目录如下：

```apl
redis-2.8.9
  |-- redis.windows.conf   # 配置文件
  |-- redis-benchmark.exe
  |-- redis-check-aof.exe
  |-- redis-check-dump.exe
  |-- redis-cli.exe        # redis的客户端，使用的时候先双击启动服务端，然后才可以使用客户端
  |-- redis-server.exe     # redis服务器端
```

## 1.3 Linux操作

```sh
# /export/目录下面创建这三个文件夹 用于存放 数据 软件 安装包
[root@node1 ~]# ls /export/
data  server  software
```

**Linux中操作redis-3.0.4**

在Linux中操作和Windows一样，首先启动服务器端然后再启动客户端即可。

```sh
# 启动服务器端。启动之后页面就会霸屏了
[root@node1 src]# ./redis-server
# 启动客户端。服务端启动之后页面就会霸屏了，所以重新克隆一个页面启动客户端
[root@node1 src]# ./redis-cli
```

配置环境变量：

```sh
# 配置环境变量
vim /etc/profile
# 文件添加下面内容
export REDIS_HOME=/usr/local/redis/redis-3.0.4
export PATH=$PATH:$REDIS_HOME/src
# 刷新配置
source /etc/profile
```

修改Redis霸屏操作：

```sh
# 修改Redis霸屏操作
[root@node1 src]# vim redis.conf
# 进入vim编辑器，使用搜索命令/dae，光标定位daemonize no，修改为yes
# 重新加载配置文件
[root@node1 /usr/local/redis/redis-3.0.4]# src/redis-server ./redis.conf
```

设置密码

```sh
# 设置密码
[root@node1 src]# vim redis.conf
# 进入vim编辑器，使用搜索命令/pass，光标定位# requirepass foobared。注释取消，将foobared换成密码即可。
# 重新加载配置文件
[root@node1 /usr/local/redis/redis-3.0.4/src]# ./redis-server ./redis.conf
# 使用密码启动客户端。如果不使用密码登录进去，是无法使用命令的，可以使用auth 密码使用命令。
[root@node1 /usr/local/redis/redis-3.0.4/src]# redis-cli -h localhost -p 6379 -a 密码
```

远程连接Redis

```sh
# 修改Redis配置文件IP地址访问
[root@node1 src]# vim redis.conf
# 将bind 127.0.0.1注释起来。绑定地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0可以在任意IP访问

# windows客户端操作：打开PowerShell窗口
.\redis-cli.exe -h 192.168.66.130 -p 6379
```

**Linux中操作redis-6.2.4**

```sh
# 安装Redis所需要的依赖
yum install -y gcc tcl
# 解压缩redis-6.2.4.tar.gz
tar -xvf redis-6.2.4.tar.gz
# 进入redis目录
cd redis-6.2.4
# 运行编译命令
make && make install
```

```sh
# 修改redis.conf文件中的一些配置
vim redis.conf
# 绑定地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0则可以在任意IP访问
bind 0.0.0.0
# 数据库数量，设置为1
databases 1
```

```sh
# 启动Redis服务端
redis-server redis.conf
# 启动redis客户端
redis-cli
# 停止redis服务，就是使用的redis-cli shutdown
redis-cli shutdown
```

# 第二章 命令操作

`redis`存储的是`key,value`格式的数据，其中`key`都是字符串，`value`有5种不同的数据结构：

1. 字符串类型 string
2. 哈希类型 hash：`map`格式  
3. 列表类型 list：`linkedlist`格式。支持重复元素
4. 集合类型 set：不允许重复元素
5. 有序集合类型 sortedset：不允许重复元素，且元素有顺序

通用命令操作：

| 命令          | 作用                                                       |
| ------------- | ---------------------------------------------------------- |
| keys pattern  | 查找所有符合给定模式pattern的key                           |
| keys *        | 查询所有的键                                               |
| exists key    | 检查给定的key是否存在                                      |
| type key      | 返回key所存储的值的类型                                    |
| ttl key       | 返回给定key的剩余生存时间（TTL、time to live），以秒为单位 |
| del key       | 该命令用于在key存在时删除key                               |
| clear         | 清屏                                                       |
| select [数字] | 切换数据库。一共有16个数据库，启动的时候默认使用0号数据库  |

## 2.1 字符串类型 string

Redis中字符串类型常用命令如下：

| 命令                    | 作用                                              |
| ----------------------- | ------------------------------------------------- |
| set key value           | 设置指定key的值                                   |
| get key                 | 获取指定key的值                                   |
| setex key seconds value | 设置指定key的值，并将key的过期时间设置为seconds秒 |
| setnx key value         | 只有在key不存在的时候设置key的值                  |

```sql
127.0.0.1:6379> set username zhangsan # 存储
OK
127.0.0.1:6379> get username # 获取
"zhangsan"
127.0.0.1:6379> del username # 删除
(integer) 1
```

## 2.2 哈希类型 hash

Redis hash是一个String类型的field和value的映射表，map格式。hash特别适合用于存储对象。

![](..\图片\2-10【Redis单机缓存】\2-1.png)

常用命令如下：

| 命令                 | 作用                                    |
| -------------------- | --------------------------------------- |
| hset key field value | 将哈希表key中的字段filed的值设置为value |
| hget key field       | 获取存储在哈希表key中的指定字段的值     |
| hdel key field       | 删除存储在哈希表key中的指定字段         |
| hkeys key            | 获取哈希表key中的所有字段               |
| hvals key            | 获取哈希表key中所有的值                 |
| hgetall key          | 获取在哈希表中指定key的所有字段和值     |

```sql
127.0.0.1:6379> hset myhash username lisi # 向myhash哈希表存储username字段的值lisi
(integer) 1
127.0.0.1:6379> hset myhash password 123 # 向myhash哈希表存储password字段的值123
(integer) 1
127.0.0.1:6379> hget myhash username # 获取myhash哈希表中username字段的值
"lisi"
127.0.0.1:6379> hgetall myhash # 获取myhash哈希表中所有字段和值
1) "username"
2) "lisi"
3) "password"
4) "123"
127.0.0.1:6379> hdel myhash username # 删除存储在myhash哈希表中username字段及值
(integer) 1
```

## 2.3 列表类型 list

Redis列表是简单的字符串列表，按照插入顺序排序。

![](..\图片\2-10【Redis单机缓存】\2-2.png)

常用命令如下：

| 命令                      | 作用                                                         |
| ------------------------- | ------------------------------------------------------------ |
| lpush key value [value2]  | 将一个或者多个值插入到列表头部                               |
| rpush key value [value2]  | 将一个或者多个值插入到列表尾部                               |
| lrange key start end      | 获取列表指定范围内的元素。0表示第一个元素，-1表示最后一个元素。 |
| lpop key                  | 删除列表最左边的元素，并将元素返回                           |
| rpop key                  | 删除列表最右边的元素，并将元素返回                           |
| llen key                  | 获取列表长度                                                 |
| brpop key1 [key2] timeout | 移出并获取列表最后一个元素。列表没有元素会阻塞直到超时或者发现可弹出元素 |

```sql
127.0.0.1:6379> lpush mylist a # 插入列表头部
(integer) 1
127.0.0.1:6379> lpush mylist b # 插入列表头部
(integer) 2
127.0.0.1:6379> rpush mylist c # 插入列表尾部
(integer) 3
127.0.0.1:6379> llen mylist # 获取长度
(integer) 3
127.0.0.1:6379> lrange mylist 0 2 # 获取列表范围内元素
1) "b"
2) "a"
3) "c"
127.0.0.1:6379> lpop mylist # 删除列表最左边的元素，并将元素返回
"b"
127.0.0.1:6379> lrange mylist 0 -1
1) "a"
2) "c"
```

## 2.4 集合类型 set

Redis set是String类型的无序集合。集合成员是唯一的，这就意味着集合中不能够出现重复的数据。

![](..\图片\2-10【Redis单机缓存】\2-3.png)

常用命令如下：

| 命令                       | 作用                                     |
| -------------------------- | ---------------------------------------- |
| sadd key member1 [member2] | 向集合添加一个或者多个成员               |
| smembers key               | 返回集合中的所有成员                     |
| scard                      | 获取集合的成员数                         |
| srem key member1 [member2] | 移除集合中一个或者多个成员               |
| sinter key1 [key2]         | 返回所有给定集合的交集                   |
| sunion key1 [key2]         | 返回所有给定集合的并集                   |
| sdiff key1 [key2]          | 返回所有给定集合的差集 顺序不同 结果不同 |

```sql
127.0.0.1:6379> sadd myset a # 存储元素
(integer) 1
127.0.0.1:6379> sadd myset a # 存储重复元素会失败！
(integer) 0
127.0.0.1:6379> smembers myset # 获取set集合中所有元素
1) "a"
127.0.0.1:6379> srem myset a # 删除set集合中的某个元素
(integer) 1
```

## 2.5 有序集合类型 sortedset

有序集合类型 `sortedset`：不允许重复元素，且元素有顺序，每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。

![](..\图片\2-10【Redis单机缓存】\2-4.png)

常用命令如下：

| 命令                                     | 作用                                             |
| ---------------------------------------- | ------------------------------------------------ |
| zadd key score1 member1 [score2 member2] | 添加一个或者多个成员，或者更新已经存在成员的分数 |
| zrange key start stop [withscores]       | 通过索引区间返回有序集合中指定区间内的成员       |
| zincrby key increment member             | 有序集合中对指定成员的分数加上增量increment      |
| zrem key member [member...]              | 移除有序集合中的一个或者多个成员                 |

```sql
127.0.0.1:6379> zadd mysort 60 zhangsan # 存储
(integer) 1
127.0.0.1:6379> zadd mysort 50 lisi # 存储
(integer) 1
127.0.0.1:6379> zadd mysort 80 wangwu # 存储
(integer) 1
127.0.0.1:6379> zrange mysort 0 -1 # 返回指定区间内的成员
1) "lisi"
2) "zhangsan"
3) "wangwu"
127.0.0.1:6379> zrange mysort 0 -1 withscores # 返回指定区间内的成员及关联的分数
1) "lisi"
2) "50"
3) "zhangsan"
4) "60"
5) "wangwu"
6) "80"
127.0.0.1:6379> zrem mysort lisi # 移除有序集合中的一个成员
(integer) 1
```

# 第三章 Java客户端 Jedis

Redis的Java客户端有很多，官方推荐的有三种：Jedis、Lettcue、Redisson。

Spring对Redis客户端进行了整合，提供了Spring Data Redis，在Spring Boot中还提供了对应的stater，就是spring-boot-stater-data-redis。  

```xml
<dependencies>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>2.8.0</version>
    </dependency>
</dependencies>
```

```java
public class Demo01RedisTest {

    @Test
    public void test() {
        // 获取连接，如果在参数设置为空 不传进去参数。那么会有默认值，默认值就是"localhost", 6379。
        Jedis jedis = new Jedis("localhost", 6379);
        // 操作
        jedis.set("username", "zhangsan");
        // 关闭连接
        jedis.close();
    }
}
```

## 3.1 Jedis操作redis

**字符串类型 string**

| 命令                    | 作用                                              |
| ----------------------- | ------------------------------------------------- |
| set key value           | 设置指定key的值                                   |
| get key                 | 获取指定key的值                                   |
| setex key seconds value | 设置指定key的值，并将key的过期时间设置为seconds秒 |
| setnx key value         | 只有在key不存在的时候设置key的值                  |

```java
@Test
public void test1() {
    // 获取连接
    Jedis jedis = new Jedis();

    // 操作
    jedis.set("username", "zhangsan");
    System.out.println(jedis.get("username"));
    // 设置过期时间。将键值对activecode，123键值对存入redis，10秒后自动删除
    jedis.setex("activecode", 10, "123");

    // 关闭连接
    jedis.close();
}
```

**哈希类型 hash**

| 命令                 | 作用                                    |
| -------------------- | --------------------------------------- |
| hset key field value | 将哈希表key中的字段filed的值设置为value |
| hget key field       | 获取存储在哈希表key中的指定字段的值     |
| hdel key field       | 删除存储在哈希表key中的指定字段         |
| hkeys key            | 获取哈希表key中的所有字段               |
| hvals key            | 获取哈希表key中所有的值                 |
| hgetall key          | 获取在哈希表中指定key的所有字段和值     |

```java
@Test
public void test02() {
    // 获取连接
    Jedis jedis = new Jedis();
    // 存储哈希数据
    jedis.hset("user", "name", "linxuan");
    jedis.hset("user", "age", "19");
    jedis.hset("user", "gender", "male");
    // 获取数据
    System.out.println(jedis.hget("user", "name"));

    // 获取hash的所有map中的数据
    Map<String, String> user = jedis.hgetAll("user");
    // 使用Lambda表达式遍历输出
    user.forEach((key, value) -> System.out.println(key + " " + value));

    // 关闭Jedis
    jedis.close();
}
```

**列表类型 list**

```java
@Test
public void test03() {
    // 获取连接
    Jedis jedis = new Jedis();

    // list 存储
    jedis.lpush("mylist", "a", "b", "c"); // 从左边存
    jedis.rpush("mylist", "a", "b", "c"); // 从右边存

    // list 范围获取。[c, b, a, a, b, c]
    List<String> mylist = jedis.lrange("mylist", 0, -1);
    System.out.println(mylist);

    // list 左边弹出，结果为c
    System.out.println(jedis.lpop("mylist"));
    // list 右边弹出，结果为c
    System.out.println(jedis.rpop("mylist"));

    // 3. 关闭连接
    jedis.close();
}
```

**集合类型 set**

| 命令                       | 作用                                     |
| -------------------------- | ---------------------------------------- |
| sadd key member1 [member2] | 向集合添加一个或者多个成员               |
| smembers key               | 返回集合中的所有成员                     |
| scard                      | 获取集合的成员数                         |
| srem key member1 [member2] | 移除集合中一个或者多个成员               |
| sinter key1 [key2]         | 返回所有给定集合的交集                   |
| sunion key1 [key2]         | 返回所有给定集合的并集                   |
| sdiff key1 [key2]          | 返回所有给定集合的差集 顺序不同 结果不同 |

```java
@Test
public void test04() {
    // 获取连接
    Jedis jedis = new Jedis();

    // set 存储
    jedis.sadd("myset", "java", "php", "c++");
    // set 获取
    Set<String> myset = jedis.smembers("myset");
    System.out.println(myset);

    // 3. 关闭连接
    jedis.close();
}
```

**有序集合类型 sortedset**

| 命令                                     | 作用                                             |
| ---------------------------------------- | ------------------------------------------------ |
| zadd key score1 member1 [score2 member2] | 添加一个或者多个成员，或者更新已经存在成员的分数 |
| zrange key start stop [withscores]       | 通过索引区间返回有序集合中指定区间内的成员       |
| zincrby key increment member             | 有序集合中对指定成员的分数加上增量increment      |
| zrem key member [member...]              | 移除有序集合中的一个或者多个成员                 |

```java
@Test
public void test05() {
    // 获取连接
    Jedis jedis = new Jedis();

    // sortedset 存储
    jedis.zadd("mysortedset", 3, "亚瑟");
    jedis.zadd("mysortedset", 30, "后裔");
    jedis.zadd("mysortedset", 55, "孙悟空");
    // sortedset 获取
    Set<String> mysortedset = jedis.zrange("mysortedset", 0, -1);
    System.out.println(mysortedset);

    //3. 关闭连接
    jedis.close();
}
```

## 3.2 JedisPool连接池

  ```java
  public static void main(String[] args) {
      // 0.创建一个配置对象
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxTotal(50);
      config.setMaxIdle(10);
  
      // 1.创建Jedis连接池对象
      JedisPool jedisPool = new JedisPool(config, "localhost", 6379);
  
      // 2.获取连接
      Jedis jedis = jedisPool.getResource();
      // 3. 使用
      jedis.set("hehe", "heihei");
  
      // 4. 关闭 归还到连接池中
      jedis.close();
  }
  ```

配置对象的参数有很多，如下：

```sql
# 最大活动对象数     
redis.pool.maxTotal=1000    
# 最大能够保持idel状态的对象数      
redis.pool.maxIdle=100  
# 最小能够保持idel状态的对象数   
redis.pool.minIdle=50    
# 当池内没有返回对象时，最大等待时间    
redis.pool.maxWaitMillis=10000    
# 当调用borrow Object方法时，是否进行有效性检查    
redis.pool.testOnBorrow=true    
# 当调用return Object方法时，是否进行有效性检查    
redis.pool.testOnReturn=true  
# “空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1.  
redis.pool.timeBetweenEvictionRunsMillis=30000  
# 向调用者输出“链接”对象时，是否检测它的空闲超时；  
redis.pool.testWhileIdle=true  
# 对于“空闲链接”检测线程而言，每次检测的链接资源的个数。默认为3.  
redis.pool.numTestsPerEvictionRun=50  
# redis服务器的IP    
redis.ip=xxxxxx  
# redis服务器的Port    
redis1.port=6379   
```

**Jedis连接池工具类**

```properties
# Jedis连接池配置文件，命名为jedis.properties，在resources目录下创建该文件
host = 127.0.0.1
port = 6379
maxTotal = 50
maxIdle = 10
```

```java
public class JedisPoolUtils {
    private static JedisPool jedisPool;

    static {
        // 读取配置文件
        InputStream is = JedisPoolUtils.class.getClassLoader()
            .getResourceAsStream("jedis.properties");

        // 创建Properties对象
        Properties pro = new Properties();
        // 关联文件
        try {
            pro.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取数据，设置到JedisPoolConfig中
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));

        // 初始化JedisPool
        jedisPool = new JedisPool(config,
                                  pro.getProperty("host"), 
                                  Integer.parseInt(pro.getProperty("port")));
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
```

## 3.3 加载所有省份案例

需求：

1. 提供`index.html`页面，页面中有一个省份 下拉列表
2. 当 页面加载完成后 发送`ajax`请求，加载所有省份

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
        <select id="province">
            <option>--请选择省份</option>
        </select>
    </body>
</html>
```

### 3.3.1 连接MySQL环境搭建

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>

<dependencies>
    <!-- Servlet依赖 -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <!-- 依赖作用范围未provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>

    <!-- Spring整合JDBC -->
    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.32</version>
    </dependency>
    <!-- Druid连接池 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- Spring整合JDBC核心依赖 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>6.0.4</version>
    </dependency>

    <!-- Redis数据库客户端Jedis -->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>2.8.0</version>
    </dependency>

    <!-- JSON和Java对象格式的转换 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.67_noneautotype2</version>
    </dependency>

    <!-- 测试依赖 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>

    <!-- 其他依赖 -->
    <!-- Lombok简化实体类开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
        <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;

# 如果存在该表那么就删除
DROP TABLE IF EXISTS tb_province;
# 如果不存在tb_brand表那么就创建
CREATE TABLE IF NOT EXISTS tb_province(
    id INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(20) NOT NULL
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_province;

# 插入数据
INSERT INTO tb_province VALUES(NULL,'北京'), (NULL,'上海'), (NULL,'广州'), (NULL,'陕西');
# 查询数据
SELECT * FROM tb_province;
```

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# 这里面的键最好是这些，如果修改那么无法初始化连接池对象。和Spring里面的键不一样。
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
driver = com.mysql.cj.jdbc.Driver
url = jdbc:mysql://localhost:3306/linxuan?useSSL=false
username = root
password = root
```

```java
// JDBC工具类
public class JDBCUtils {

    private static DataSource ds;

    static {
        try {
            // 创建Properties对象
            Properties pro = new Properties();
            // 使用ClassLoader加载配置文件，获取字节输入流
            ClassLoader classLoader = JDBCUtils.class.getClassLoader();
            InputStream resourceAsStream = classLoader.getResourceAsStream("jdbc.properties");
            // 加载配置文件
            pro.load(resourceAsStream);
            // 初始化连接池对象
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接池对象
     * @return DataSource
     */
    public static DataSource getDataSource() {
        return ds;
    }

    /**
     * 获取连接Connection对象
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
```

```java
package com.linxuan.pojo;

@Data
public class Province {
    private Integer id;
    private String name;
}
```

```java
public interface ProvinceDao {

    /**
     * 查询所有省份信息
     * @return
     */
    List<Province> findAll();
}
```

```java
public class ProvinceDaoImpl implements ProvinceDao {

    // 声明成员变量 jdbctemplement
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Province> findAll() {
        // 定义sql
        String sql = "select * from tb_province";
        // 执行sql
        List<Province> list = template.query(sql, new BeanPropertyRowMapper<Province>(Province.class));
        return list;
    }
}
```

```java
public interface ProvinceService {

    /**
     * 查询所有省份信息
     * @return
     */
    List<Province> finAll();
}
```

```java
public class ProvinceServiceImpl implements ProvinceService {

    private ProvinceDao provinceDao = new ProvinceDaoImpl();

    @Override
    public List<Province> finAll() {
        return provinceDao.findAll();
    }
}
```

```java
// 测试类
public class ProvinceServiceTest {

    private ProvinceService provinceService = new ProvinceServiceImpl();

    @Test
    public void testFindAll() {
        List<Province> list = provinceService.finAll();
        String str = JSON.toJSONString(list);
        System.out.println(str);
    }
}
```

### 3.3.2 不使用Redis返回数据

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
        <select id="province">
            <option>--请选择省份</option>
        </select>
        <!-- 引入axios.js资源文件。需要提前导入axios.js文件。 -->
        <script src="js/axios-0.18.0.js"></script>
        <script>
            //1. 当页面加载完成后，发送ajax请求
            window.onload = function () {
                //2. 发送ajax请求
                axios.get("http://localhost/findAll").then(function (resp) {
                    // 获取响应的数据
                    let provinces = resp.data;
                    let data;

                    // 遍历响应的数据
                    for (let i = 0; i < provinces.length; i++) {
                        // 其中的一个
                        let province = provinces[i];
                        data += "<option name='"+ province.id +"'>" + province.name + "</option>>"
                    }
                    document.getElementById("province").innerHTML = data;
                })
            }
            /*
            或者也可以使用JQuery，当然需要引入资源文件
                $(function () {
                    // 发送ajax请求，加载所有省份数据
                    $.get("provinceServlet", {}, function (data) {
                        // 获取select
                        var province = $("#province");
                        // 遍历json数组
                        $(data).each(function () {
                            // 创建<option>
                            var option = "<option name='"+ this.id +"'>" + this.name + "</option>>"
                            // 调用select的append追加option
                            province.append(option);
                        });

                    });
                });
            */
        </script>
    </body>
</html>
```

```java
@WebServlet("/findAll")
public class ProvinceServlet extends HttpServlet {
    private ProvinceService provinceService = new ProvinceServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 调用service，获取数据库结果
        List<Province> list = provinceService.finAll();
        // 将Java对象转换为JSON格式的数据
        String str = JSON.toJSONString(list);
        System.out.println(str);

        // 设置返回前端数据类型
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(str);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

### 3.3.3 使用Redis优化程序

如果一直使用`MySQL`的话，会对性能造成影响，我们可以使用redis缓存一些不经常发生变化的数据。

- 数据库的数据一旦发生改变，则需要更新缓存。
- 数据库的表执行 增删改的相关操作，需要将redis缓存数据情况，再次存入
- 在service对应的增删改方法中，将redis数据删除。

操作如下：

```properties
# Jedis连接池配置文件，命名为jedis.properties，在resources目录下创建该文件
host = 127.0.0.1
port = 6379
maxTotal = 50
maxIdle = 10
```

```java
public class JedisPoolUtils {
    private static JedisPool jedisPool;

    static {
        // 读取配置文件
        InputStream is = JedisPoolUtils.class.getClassLoader()
                .getResourceAsStream("jedis.properties");

        // 创建Properties对象
        Properties pro = new Properties();
        // 关联文件
        try {
            pro.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取数据，设置到JedisPoolConfig中
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));

        // 初始化JedisPool
        jedisPool = new JedisPool(config,
                pro.getProperty("host"),
                Integer.parseInt(pro.getProperty("port")));
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
```

```java
public interface ProvinceService {

    /**
     * 查询所有省份信息
     * @return
     */
    List<Province> findAll();

    /**
     * 通过Redis来查询所有省份信息
     * @return String类型，这样方便从Redis里面直接拿出来数据
     */
    String findAllByRedis();
}

```

```java
public class ProvinceServiceImpl implements ProvinceService {

    private ProvinceDao provinceDao = new ProvinceDaoImpl();

    @Override
    public List<Province> findAll() {
        return provinceDao.findAll();
    }

    // 从Redis中查询数据
    @Override
    public String findAllByRedis() {
        // 创建JedisPoll连接池
        Jedis jedis = JedisPoolUtils.getJedis();
        // 从Redis里面查询数据
        String provice = jedis.get("provice");
        // 如果查询失败
        if (provice == null || provice.length() == 0) {
            System.out.println("Redis里面并没有该数据");
            // 从数据库中查询
            List<Province> list = provinceDao.findAll();
            // 转换为JSON格式
            provice = JSON.toJSONString(list);
            // 存储到redis里面
            jedis.set("provice", provice);
            // 归还连接
            jedis.close();
        } else {
            System.out.println("redis中有数据,查询redis缓存");
        }
        return provice;

    }
}
```

```java
public class ProvinceServiceTest {

    private ProvinceService provinceService = new ProvinceServiceImpl();

    // 如果测试失败，看看是不是忘记打开Redis客户端了
    @Test
    public void testFindAllByRedis() {
        String str = provinceService.findAllByRedis();
        System.out.println(str);
    }
}
```

```java
@WebServlet("/findAll")
public class ProvinceServlet extends HttpServlet {
    private ProvinceService provinceService = new ProvinceServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String str = provinceService.findAllByRedis();
        System.out.println(str);

        // 设置返回前端数据类型
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(str);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

# 第四章 Spring Data Redis

在Spring Boot项目中，可以使用Spring Data Redist来简化Redist操作，Maven坐标如下：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

application.yml配置如下：

```yml
spring:
  application:
    name: springbootredis
  # Redis相关配置
  redis:
    host: localhost
    port: 6379
    # password: 123456
    database: 0 # 启动默认使用0号数据库 
    jedis:
      # Redis连接池配置
      pool:
        max-active: 8 # 最大连接数量
        max-wait: 1ms # 连接池最大阻塞等待时间
        max-idle: 4 # 连接池中的最大空闲连接
        min-idle: 0 # 连接池中的最小空闲连接
```

Spring Data Redis中提供了一个高度封装的类：`RedisTemplate`，针对Jedis客户端中大量api进行了归档封装，将同一类型操作封装为operation接口，具体分类如下：

- ValueOperations：简单K-V操作
- HashOperations：针对map类型的数据操作

- ListOperations：针对list类型的数据操作

- SetOperations：set类型数据操作

- ZSetOperations：zset类型数据操作

## 4.1 快速入门

搭建一个SpringBoot项目，导入坐标：

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.0.3'
    id 'io.spring.dependency-management' version '1.1.0'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation group: 'junit', name: 'junit', version: '4.13.2'
}
```

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
</dependency>
```

```yaml
# SpringBoot3.0的配置信息如下，2.0的配置在上方
spring:
  application:
    name: springbootredis
  # Redis相关配置
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
    jedis:
      # Redis连接池配置
      pool:
        max-active: 8 # 最大连接数量
        max-wait: 1ms # 连接池最大阻塞等待时间
        max-idle: 4 # 连接池中的最大空闲连接
        min-idle: 0 # 连接池中的最小空闲连接
```

```java
@Configuration
public class RedisConfig extends CachingConfigurationSelector {

    // 第三方Bean对象RedisTemplate交由Spring管理，可以不设置默认已经管理了，但是这里要设置一下序列化器
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        // 默认的Key序列化器为：JdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }
}
```

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class SpringBootRedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("city", "city1");
    }
}
```

## 4.2 操作Redis

操作Redis中字符串类型：

```java
@Test
public void testString() {
    // 存储字符串类型 key:city value:beijing
    // ValueOperations valueOperations = redisTemplate.opsForValue();
    redisTemplate.opsForValue().set("city", "beijing");

    // 从Redis中获取字符串类型键为city的值 并打印
    String value = (String) redisTemplate.opsForValue().get("city");
    System.out.println(value);

    // 存储 但是时间只有10秒 第三个参数是时间 第四个参数是单位
    redisTemplate.opsForValue().set("key1", "value1", 10, TimeUnit.SECONDS);

    // 如果Redis中没有city键 那么存储返回true 否则返回false
    Boolean flag = redisTemplate.opsForValue().setIfAbsent("city", "tianjin");
    System.out.println(flag);
}
```

操作Hash类型数据：

* `redisTemplate.opsForHash()`：获取到HashOperations
* `HashOperations.put()`：存储
* `HashOperations.get()`：获取
* `HashOperations.keys()`：获取hash结构中的所有字段
* `HashOperations.values()`：获取hash结构中的所有值

操作List类型数据：

* `redisTemplate.opsForList()`：获取到ListOperations
* `ListOperations.leftPush()`：头部左侧存储一个值
* `ListOperations.leftPushAll()`：存储多个值
* `ListOperations.range()`：根据范围取值
* `ListOperations.rightPop()`：从末尾取值并移除队列
* `ListOperations.size()`：获得列表长度

操作Set类型的数据：

* `redisTemplate.opsForSet()`：获取到SetOperations
* `SetOperations.add()`：存储，可以存储多个值，但是不能够存储重复的值，无序的
* `SetOperations.members()`：取值
* `SetOperations.remove()`：删除成员 

操作ZSet类型的数据：

* `redisTemplate.opsForZSet()`：获取到ZSetOperations
* `ZSetOperations.add()`：存储值
* `ZSetOperations.range()`：取值
* `ZSetOperations.incrementScore()`：修改分数
* `ZSetOperations.remove()`：删除成员

通用操作：

* `redisTemplate.keys("*")`：获取Redis中所有的key
* `redisTemplate.hasKey()`：判断某个key是否存在，字符串类型
* `redisTemplate.delete()`：删除指定key
* `redisTemplate.type()`：获取指定key对应的value的数据类型
