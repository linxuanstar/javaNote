# 第一章 基础概念

* 概念： `redis`是一款高性能的`NOSQL`系列的**非关系型数据库**。

![](D:\Java\笔记\图片\3-day23【redis】\1.关系型和非关系型数据库比较.bmp)

## 1.1 NOSQL

`NoSQL(NoSQL = Not Only SQL)`，意即“不仅仅是SQL”，是一项全新的数据库理念，泛指非关系型的数据库。

随着互联网web2.0网站的兴起，传统的关系数据库在应付web2.0网站，特别是超大规模和高并发的SNS类型的web2.0纯动态网站已经显得力不从心，暴露了很多难以克服的问题，而非关系型的数据库则由于其本身的特点得到了非常迅速的发展。

NoSQL数据库的产生就是为了解决大规模数据集合多重数据种类带来的挑战，尤其是大数据应用难题。

### NOSQL和关系型数据库比较

<font color = "red">**优点如下：**</font>

* 成本：nosql数据库简单易部署，基本都是开源软件，不需要像使用oracle那样花费大量成本购买使用，相比关系型数据库价格便宜。
* 查询速度：nosql数据库将数据存储于缓存之中，关系型数据库将数据存储在硬盘中，自然查询速度远不及nosql数据库。
* 存储数据的格式：nosql的存储格式是`key,value`形式、文档形式、图片形式等等，所以可以存储基础类型以及对象或者是集合等各种格式，而数据库则只支持基础类型。
* 扩展性：关系型数据库有类似join这样的多表查询机制的限制导致扩展很艰难。

<font color = "red">**缺点如下：**</font>

* 维护的工具和资料有限，因为nosql是属于新的技术，不能和关系型数据库10几年的技术同日而语。
* 不提供对sql的支持，如果不支持sql这样的工业标准，将产生一定用户的学习和使用成本。
* 不提供关系型数据库对事务的处理。

### 非关系型数据库的优势

* 性能NOSQL是基于键值对的，可以想象成表中的主键和值的对应关系，而且不需要经过SQL层的解析，所以性能非常高。
* 可扩展性同样也是因为基于键值对，数据之间没有耦合性，所以非常容易水平扩展。

### 关系型数据库的优势

* 复杂查询可以用SQL语句方便的在一个表以及多个表之间做非常复杂的数据查询。
* 事务支持使得对于安全性能很高的数据访问要求得以实现。对于这两类数据库，对方的优势就是自己的弱势，反之亦然。

### 总结

* 关系型数据库与NoSQL数据库并非对立而是互补的关系，即通常情况下使用关系型数据库，在适合使用NoSQL的时候使用NoSQL数据库，
* 让NoSQL数据库对关系型数据库的不足进行弥补。
* 一般会将数据存储在关系型数据库中，在nosql数据库中备份存储关系型数据库的数据

## 1.2 主流的NOSQL产品

1. 键值`(Key-Value)`存储数据库
   * 相关产品： `Tokyo Cabinet/Tyrant`、`Redis`、`Voldemort`、`Berkeley DB`
   * 典型应用： 内容缓存，主要用于处理大量数据的高访问负载。 
   * 数据模型： 一系列键值对
   * 优势： 快速查询
   * 劣势： 存储的数据缺少结构化
2. 列存储数据库
   * 相关产品：`Cassandra`, `HBase`, `Riak`
   * 典型应用：分布式的文件系统
   * 数据模型：以列簇式存储，将同一列数据存在一起
   * 优势：查找速度快，可扩展性强，更容易进行分布式扩展
   * 劣势：功能相对局限
3. 文档型数据库
   * 相关产品：`CouchDB`、`MongoDB`
   * 典型应用：Web应用（与Key-Value类似，Value是结构化的）
   * 数据模型： 一系列键值对
   * 优势：数据结构要求不严格
   * 劣势： 查询性能不高，而且缺乏统一的查询语法
4. 图形`(Graph)`数据库
   * 相关数据库：`Neo4J`、`InfoGrid`、`Infinite Graph`
   * 典型应用：社交网络
   * 数据模型：图结构
   * 优势：利用图结构相关算法。
   * 劣势：需要对整个图做计算才能得出结果，不容易做分布式的集群方案。

## 1.3 什么是Redis

Redis是用C语言开发的一个开源的高性能键值对（key-value）数据库，官方提供测试数据，50个并发执行100000个请求,读的速度是110000次/s,写的速度是81000次/s ，且Redis通过提供多种键值数据类型来适应不同场景下的存储需求。

目前为止Redis支持的键值数据类型如下：

1. <font color = "red">**字符串类型 string**</font>

2. <font color = "red">**哈希类型 hash**</font>

3. <font color = "red">**列表类型 list**</font>

4. <font color = "red">**集合类型 set**</font>

5. <font color = "red">**有序集合类型 sortedset**</font>

### redis的应用场景

- 缓存（数据查询、短连接、新闻内容、商品内容等等）
- 聊天室的在线好友列表
- 任务队列。（秒杀、抢购、12306等等）
- 应用排行榜
- 网站访问统计
- 数据过期处理（可以精确到毫秒）
- 分布式集群架构中的`session`分离

# 第二章 下载安装

下载安装
1. 官网：`https://redis.io`
2. 中文网：`http://www.redis.net.cn/`
3. 解压直接可以使用

     * 双击`redis-server.exe`让服务器端运行
     * 双击`redis-cli.exe`让客户端运行
3. 文件讲解：
	* `redis.windows.conf`：配置文件
	* `redis-cli.exe`：redis的客户端
	* `redis-server.exe`：redis服务器端

# 第三章 命令操作

`redis`存储的是：`key,value`格式的数据，其中`key`都是字符串，`value`有5种不同的数据结构

`value`的数据结构如下：

1. <font color = "red">**字符串类型 string**</font>
2. <font color = "red">**哈希类型 hash**</font>：`map`格式  
3. <font color = "red">**列表类型 list**</font>：`linkedlist`格式。支持重复元素
4. <font color = "red">**集合类型 set**</font>：不允许重复元素
5. <font color = "red">**有序集合类型 sortedset**</font>：不允许重复元素，且元素有顺序

![](D:\Java\笔记\图片\3-day23【redis】\2.redis数据结构.bmp)

## 3.1 字符串类型 string

1. 存储： `set key value`
	
	```sqlite
	127.0.0.1:6379> set username zhangsan
	OK
	```
	
2. 获取： `get key`
	
	```sqlite
	127.0.0.1:6379> get username
	"zhangsan"
	```
	
3. 删除： `del key`
	
	```sqlite
	127.0.0.1:6379> del age
	(integer) 1
	```

## 3.2 哈希类型 hash

<font color = "red">**哈希类型 hash**</font>：map格式  

1. 存储： `hset key field value`
	
	```sqlite
	127.0.0.1:6379> hset myhash username lisi
	(integer) 1
	127.0.0.1:6379> hset myhash password 123
	(integer) 1
	```
	
2. 获取： 
	* `hget key field`: 获取指定的field对应的值
		
		```
		127.0.0.1:6379> hget myhash username
		"lisi"
		```
		
	* `hgetall key`：获取所有的`field`和`value`
		
		```sqlite
		127.0.0.1:6379> hgetall myhash
		
		1) "username"
		2) "lisi"
		3) "password"
		4) "123"
		```
	
3. 删除： `hdel key field`
	
	```sqlite
	127.0.0.1:6379> hdel myhash username
	(integer) 1
	```

## 3.3 列表类型 list

![](D:\Java\笔记\图片\3-day23【redis】\3.列表list数据结构.bmp)

可以添加一个元素到列表的头部（左边）或者尾部（右边）

1. 添加：
	
	`lpush key value`: 将元素加入列表左边
	
	`rpush key value`：将元素加入列表右边
	
	```sqlite
	127.0.0.1:6379> lpush myList a
	(integer) 1
	127.0.0.1:6379> lpush myList b
	(integer) 2
	127.0.0.1:6379> rpush myList c
	(integer) 3
	```
	
2. 获取：
	
	`lrange key start end` ：范围获取。返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	
	```sqlite
	127.0.0.1:6379> lrange myList 0 -1
	
	1) "b"
	2) "a"
	3) "c"
	```
	
3. 删除：
	
	`lpop key`： 删除列表最左边的元素，并将元素返回
	
	`rpop key`： 删除列表最右边的元素，并将元素返回

## 3.4 集合类型 set

集合类型 `set` ： 不允许重复元素

1. 存储：`sadd key value`
	
	存储重复元素会失败！
	
	```sqlite
	127.0.0.1:6379> sadd myset a
	(integer) 1
	127.0.0.1:6379> sadd myset a
	(integer) 0
	```
	
2. 获取：`smembers key`:获取set集合中所有元素
	
	```sqlite
	127.0.0.1:6379> smembers myset
	1) "a"
	```
	
3. 删除：`srem key value`:删除set集合中的某个元素。`sremove`
	
	```sqlite
	127.0.0.1:6379> srem myset a
	(integer) 1
	```

## 3.5 有序集合类型 sortedset

有序集合类型 `sortedset`：不允许重复元素，且元素有顺序.每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。

1. 存储：`zadd key score value`
	
	如果重复存储，那么后一次存储的分数，那么就会覆盖之前的分数
	
	```sqlite
	127.0.0.1:6379> zadd mysort 60 zhangsan
	(integer) 1
	127.0.0.1:6379> zadd mysort 50 lisi
	(integer) 1
	127.0.0.1:6379> zadd mysort 80 wangwu
	(integer) 1
	```
	
2. 获取：`zrange key start end [withscores]`
	
	返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	
	```sqlite
	127.0.0.1:6379> zrange mysort 0 -1
	
	1) "lisi"
	2) "zhangsan"
	3) "wangwu"
	
	127.0.0.1:6379> zrange mysort 0 -1 withscores
	
	1) "zhangsan"
	2) "60"
	3) "wangwu"
	4) "80"
	5) "lisi"
	6) "500"
	```
	
3. 删除：`zrem key value`
	
	```sqlite
	127.0.0.1:6379> zrem mysort lisi
	(integer) 1
	```

## 3.6 通用命令

1. `keys *` : 查询所有的键
2. `type key` ： 获取键对应的`value`的类型
3. `del key`：删除指定的`key value`

# 第四章 持久化操作

`redis`是一个内存数据库，当`redis`服务器重启，获取电脑重启，数据会丢失，我们可以将`redis`内存中的数据持久化保存到硬盘的文件中。

`redis`持久化机制有两种：`RDB`和`AOF`

## 4.1 RDB

`RDB`：默认方式，不需要进行配置，默认就使用这种机制

在一定的间隔时间中，检测`key`的变化情况，然后持久化数据

步骤如下：

1. 编辑`redis.windwos.conf`文件
	
	源文件为：
	
	```sql
	#	带#号的都是注释
	#   after 900 sec (15 min) if at least 1 key changed
	save 900 1
	#   after 300 sec (5 min) if at least 10 keys changed
	save 300 10
	#   after 60 sec if at least 10000 keys changed
	save 60 10000
	```
	
	我们可以修改一下这个文件
	
	```sql
	#	带#好的都是注释
	#   after 900 sec (15 min) if at least 1 key changed
	save 900 1
	#   after 300 sec (5 min) if at least 10 keys changed
	save 300 10
	#   10秒内，修改键超过5次，自动持久化
	save 10 5
	```
	
2. 使用命令行的模式重新打开服务器，并指定配置文件名称：
	
	```ABAP
	E:\redis\redis-2.8.9\redis-server.exe redis.windows.conf
	```

3. 这时我们多次修改键，那么就会创建一个`rdb`文件，持久化存储。当再次打开，就会读取里面文件。

## 4.2 AOF

`AOF`：日志记录的方式，可以记录每一条命令的操作。可以每一次命令操作后，持久化数据

1. 编辑`redis.windwos.conf`文件

   ```sql
   # 最开始默认是关闭的，如果要开启，那么需要修改文件
   appendonly no（关闭aof） --> appendonly yes （开启aof）
   ```

   ```sql
   # 有三个取值
   # appendfsync always ： 每一次操作都进行持久化
   appendfsync everysec ： 每隔一秒进行一次持久化
   # appendfsync no	 ： 不进行持久化
   ```

2. 使用命令行的模式重新打开服务器，并指定配置文件名称：

   ```ABAP
   E:\redis\redis-2.8.9\redis-server.exe redis.windows.conf
   ```

3. 这时我们多次修改键，那么就会创建一个`aof`文件，持久化存储。当再次打开，就会读取里面文件。

# 第五章 Java客户端 Jedis

## 5.1 Jedis

`Jedis`: 一款java操作`redis`数据库的工具.

使用步骤：
1. 下载`jedis`的jar包，导入到项目里面

   `commons-pool2-2.3.jar`和`jedis-2.7.0.jar`

2. 使用
	
	```java
	public class Demo01Redis {
	
	    /**
	     * 测试Jedis
	     * 这里使用的是单元测试，无需使用main方法
	     */
	    @Test
	    public void test() {
	        // 获取连接
	        Jedis jedis = new Jedis("localhost", 6379);
	        // 操作
	        jedis.set("username", "zhangsan");
	        // 关闭连接
	        jedis.close();
	    }
	}
	```
	

> 注意：
>
> 对于获取redis连接的语句：`Jedis jedis = new Jedis("localhost", 6379);`，如果我们在参数里面设置为空，也就是不传进去参数。那么会有默认值，且默认值就是`"localhost", 6379`。

## 5.2 Jedis操作各种redis中的数据结构

### 字符串类型

* `SET key value` 设置指定 key 的值

- `GET key` 获取指定 key 的值。
- `SETEX key seconds value` 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。

```java
    /**
     * 创建一个单元测试
     */
    @Test
    public void test1() {
        // 获取连接
        Jedis jedis = new Jedis();
        // 操作
        jedis.set("username", "zhangsan");
        String username = jedis.get("username");
        System.out.println(username);

        // 设置过期时间
        // 将键值对activecode，123键值对存入redis，10秒后自动删除
        jedis.setex("activecode", 10, "123");
        // 关闭连接
        jedis.close();
    }
```

### 哈希类型

- `HSET key field value` 将哈希表 key 中的字段 field 的值设为 value 。
- `HGET key field` 获取存储在哈希表中指定字段的值
- `HGETALL key` 获取在哈希表中指定 key 的所有字段和值

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
        String name = jedis.hget("user", "name");
        System.out.println(name);

        // 获取hash的所有map中的数据
        Map<String, String> user = jedis.hgetAll("user");
        // 获取Keyset
        Set<String> keySet = user.keySet();
        for (String key : keySet) {
            String value = user.get(key);
            System.out.print(key + ":" + value);
            System.out.println();
        }

        // 关闭Jedis
        jedis.close();
    }
```

```html
<!--
    linxuan
    gender:male
    name:linxuan
    age:19
-->
```

### 列表类型

- `Redis Rpush` 命令用于将一个或多个值插入到列表的尾部(最右边)。
- `Redis Lpush` 命令将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
- `LPOP key` 移出并获取列表的第一个元素
- `RPOP key` 移除并获取列表最后一个元素
- `Redis Lrange` 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。

```java
    @Test
    public void test03() {
        //1. 获取连接
        Jedis jedis = new Jedis();
        //2. 操作
        // list 存储
        jedis.lpush("mylist","a","b","c");//从左边存
        jedis.rpush("mylist","a","b","c");//从右边存

        // list 范围获取
        List<String> mylist = jedis.lrange("mylist", 0, -1);
        System.out.println(mylist);

        // list 弹出
        String element1 = jedis.lpop("mylist");//c
        System.out.println(element1);

        String element2 = jedis.rpop("mylist");//c
        System.out.println(element2);

        // list 范围获取
        List<String> mylist2 = jedis.lrange("mylist", 0, -1);
        System.out.println(mylist2);

        //3. 关闭连接
        jedis.close();
    }
```

```html
<!--
    [c, b, a, a, b, c]
    c
    c
    [b, a, a, b]
-->
```

### 集合类型

- `Redis Sadd` 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。
- `Redis Smembers` 命令返回集合中的所有的成员。 不存在的集合 key 被视为空集合。

```java
    @Test
    public void test04() {
        // 获取连接
        Jedis jedis = new Jedis();
        // 操作
        // set 存储
        jedis.sadd("myset","java","php","c++");

        // set 获取
        Set<String> myset = jedis.smembers("myset");
        System.out.println(myset);

        //3. 关闭连接
        jedis.close();
    }
```

```html
<!--
	[c++, java, php]
-->
```

### 有序集合类型

- `Redis Zadd` 命令用于将一个或多个成员元素及其分数值加入到有序集当中。
- `Redis Zrange` 返回有序集中，指定区间内的成员。其中成员的位置按分数值递增(从小到大)来排序。具有相同分数值的成员按字典序(`lexicographical order` )来排列。

```java
    @Test
    public void test05() {
        // 获取连接
        Jedis jedis = new Jedis();
        // 操作
        // sortedset 存储
        jedis.zadd("mysortedset",3,"亚瑟");
        jedis.zadd("mysortedset",30,"后裔");
        jedis.zadd("mysortedset",55,"孙悟空");

        // sortedset 获取
        Set<String> mysortedset = jedis.zrange("mysortedset", 0, -1);

        System.out.println(mysortedset);

        //3. 关闭连接
        jedis.close();
    }
```

```html
<!--
	[亚瑟, 后裔, 孙悟空]
-->
```

## 5.3 JedisPool

`jedis`连接池： `JedisPool`

使用方式如下：

1. 创建`JedisPool`连接池对象

2. 调用方法 `getResource()`方法获取`Jedis`连接

  ```java
      @Test
      public void test06() {
          //0.创建一个配置对象
          JedisPoolConfig config = new JedisPoolConfig();
          config.setMaxTotal(50);
          config.setMaxIdle(10);
  
          //1.创建Jedis连接池对象
          JedisPool jedisPool = new JedisPool(config,"localhost",6379);
  
          //2.获取连接
          Jedis jedis = jedisPool.getResource();
          //3. 使用
          jedis.set("hehe","heihei");
  
          //4. 关闭 归还到连接池中
          jedis.close();
      }
  ```

配置对象的参数有很多，如下：

```sql
#最大活动对象数     
redis.pool.maxTotal=1000    
#最大能够保持idel状态的对象数      
redis.pool.maxIdle=100  
#最小能够保持idel状态的对象数   
redis.pool.minIdle=50    
#当池内没有返回对象时，最大等待时间    
redis.pool.maxWaitMillis=10000    
#当调用borrow Object方法时，是否进行有效性检查    
redis.pool.testOnBorrow=true    
#当调用return Object方法时，是否进行有效性检查    
redis.pool.testOnReturn=true  
#“空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1.  
redis.pool.timeBetweenEvictionRunsMillis=30000  
#向调用者输出“链接”对象时，是否检测它的空闲超时；  
redis.pool.testWhileIdle=true  
# 对于“空闲链接”检测线程而言，每次检测的链接资源的个数。默认为3.  
redis.pool.numTestsPerEvictionRun=50  
#redis服务器的IP    
redis.ip=xxxxxx  
#redis服务器的Port    
redis1.port=6379   
```

## 5.4 Jedis_连接池工具类

导入配置文件

```ABAP
host=127.0.0.1
port=6379
maxTotal=50
maxIdle=10
```

创建一个JedisPoolUtils类：

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedisPoolUtils {
    private static JedisPool jedisPool;

    static {
        // 读取配置文件
        InputStream is = JedisPoolUtils.class.getClassLoader().getResourceAsStream("jedis.properties");

        //创建Properties对象
        Properties pro = new Properties();
        //关联文件
        try {
            pro.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取数据，设置到JedisPoolConfig中
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));

        //初始化JedisPool
        jedisPool = new JedisPool(config,pro.getProperty("host"),Integer.parseInt(pro.getProperty("port")));
    }

    /**
     * 获取连接方法
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
```

单元测试运行

```java
import cn.com.utils.JedisPoolUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class Demo01Redis {
    @Test
    public void test(){
        //通过连接池工具类获取
        Jedis jedis = JedisPoolUtils.getJedis();
        //使用
        jedis.set("hello","world");

        //关闭 归还到连接池中
        jedis.close();
    }
}
```

## 5.5 案例

需求：

1. 提供`index.html`页面，页面中有一个省份 下拉列表
2. 当 页面加载完成后 发送`ajax`请求，加载所有省份

![](D:\Java\笔记\图片\3-day23【redis】\4.案例.bmp)

### 不使用redis

* Mysql数据库：

  ```sql
  CREATE DATABASE day23; -- 创建数据库
  USE day23; 			   -- 使用数据库
  CREATE TABLE province(   -- 创建表
  	id INT PRIMARY KEY AUTO_INCREMENT,
  	NAME VARCHAR(20) NOT NULL
  	
  );
  -- 插入数据
  INSERT INTO province VALUES(NULL,'北京');
  INSERT INTO province VALUES(NULL,'上海');
  INSERT INTO province VALUES(NULL,'广州');
  INSERT INTO province VALUES(NULL,'陕西');
  
  USE day23;
  SELECT * FROM province;
  ```

* 导入所需的`jar`包、连接池的配置文件和`jQuery`依赖的`js`文件。

* 创建包。

创建`domain`包，里面创建一个`province`类：

```java
package cn.net.domain;

public class Province {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

创建`Dao`层：

创建`ProvinceDao`接口：

```java
package cn.net.dao;

import cn.net.domain.Province;

import java.util.List;

public interface ProvinceDao {

    public List<Province> findAll();
}
```

实现`ProvinceDao`接口，里面定义方法内容

```java
package cn.net.dao.impl;

import cn.net.dao.ProvinceDao;
import cn.net.domain.Province;
import cn.net.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProvinceDaoImpl implements ProvinceDao {
    // 声明成员变量 jdbctemplement
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Province> findAll() {
        // 定义sql
        String sql = "select * from province";
        // 执行sql
        List<Province> list = template.query(sql, new BeanPropertyRowMapper<Province>(Province.class));
        return list;
    }
}
```

创建`Service`层

创建`ProvinceService`接口：

```java
package cn.net.service;

import cn.net.domain.Province;

import java.util.List;

public interface ProvinceService {
    public List<Province> findAll();
}

```

实现`ProvinceService`接口：

```java
package cn.net.service.impl;

import cn.net.dao.ProvinceDao;
import cn.net.dao.impl.ProvinceDaoImpl;
import cn.net.domain.Province;
import cn.net.service.ProvinceService;

import java.util.List;

public class ProvinceServiceImpl implements ProvinceService {

    // 声明Dao
    private ProvinceDao dao = new ProvinceDaoImpl();

    @Override
    public List<Province> findAll() {
        return dao.findAll();
    }
}

```

创建`util`包，包下面导入一些外部资源工具类，例如`JDBCUtils`工具类

创建`Servlet`层：

```java
package cn.net.web.servlet;

import cn.net.domain.Province;
import cn.net.service.ProvinceService;
import cn.net.service.impl.ProvinceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/provinceServlet")
public class ProvinceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 调用servince查询
        ProvinceService service = new ProvinceServiceImpl();
        List<Province> list = service.findAll();
        // 序列化list为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);

        System.out.println(json);
        // 相应结果
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

创建`index.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <script src="js/jquery-3.3.1.min.js"></script>
    <script>
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
    </script>
</head>
<body>
    <select id="province">
        <option>--请选择省份</option>
    </select>
</body>
</html>
```

### redis优化

> 注意：使用redis缓存一些不经常发生变化的数据。
> * 数据库的数据一旦发生改变，则需要更新缓存。
> * 数据库的表执行 增删改的相关操作，需要将redis缓存数据情况，再次存入
> * 在service对应的增删改方法中，将redis数据删除。

如果一直使用`MySQL`的话，会对性能造成影响，我们可以使用`redis`优化一下：

`ProvinceService`接口增加一个抽象方法：

```java
package cn.net.service;

import cn.net.domain.Province;

import java.util.List;

public interface ProvinceService {
    public List<Province> findAll();

    public String findAllJson();
}

```

`ProvinceService`接口的实现类`ProvinceServiceImpl`实现此方法

首先从`redis`中查询数据，如没有数据，那么查询数据库，最后将数据存入`redis`中。

```java
package cn.net.service.impl;

import cn.com.util.JedisPoolUtils;
import cn.net.dao.ProvinceDao;
import cn.net.dao.impl.ProvinceDaoImpl;
import cn.net.domain.Province;
import cn.net.service.ProvinceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.List;

public class ProvinceServiceImpl implements ProvinceService {

    // 声明Dao
    private ProvinceDao dao = new ProvinceDaoImpl();

    @Override
    public List<Province> findAll() {
        return dao.findAll();
    }

    @Override
    public String findAllJson() {
        // 从redis中查询数据
        Jedis jedis = JedisPoolUtils.getJedis();
        String province_json = jedis.get("province");

        // 判断province_json数据是否为null
        if (province_json == null || province_json.length() == 0) {
            // redis中没有数据
            System.out.println("redis中没有数据，查询数据库...");
            // 从数据库中查询
            List<Province> list = dao.findAll();
            // 将list序列化为json
            ObjectMapper mapper = new ObjectMapper();
            try {
                province_json = mapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // 将json数据存入redis中
            jedis.set("province", province_json);
            // 归还连接
            jedis.close();
        } else {
            System.out.println("redis中有数据,查询redis缓存");
        }
        return province_json;
    }
}
```

`servlet`修改如下：

```java
package cn.net.web.servlet;

import cn.net.domain.Province;
import cn.net.service.ProvinceService;
import cn.net.service.impl.ProvinceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/provinceServlet")
public class ProvinceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/*        // 调用servince查询
        ProvinceService service = new ProvinceServiceImpl();
        List<Province> list = service.findAll();
        // 序列化list为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);*/

        ProvinceService service = new ProvinceServiceImpl();
        String json = service.findAllJson();

        System.out.println(json);
        // 相应结果
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

```