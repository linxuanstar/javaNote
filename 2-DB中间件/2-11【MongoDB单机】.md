# 第一章 MongoDB基础

传统的关系型数据库在数据操作的“三高”需求以及对应的 Web 2.0 网站需求面前，会有”力不从心”的感觉。三高需求：高并发、高性能、高可用。

- High Performance：对数据库的高并发读写的要求
- High Storage：对海量数据的高效率存储和访问的需求
- High Scalability && High Available：对数据的高扩展性和高可用性的需求

而 [MongoDB](https://www.mongodb.com/) 可以应对这些三高需求，具体的应用场景如下：

- 社交场景。使用 MongoDB 存储存储用户信息，以及用户发表的朋友圈信息，通过地理位置索引实现附近的人，地点等功能。
- 游戏场景。使用 MongoDB 存储游戏用户信息，用户的装备，积分等直接以内嵌文档的形式存储，方便查询，高效率存储和访问。
- 物流场景。使用 MongoDB 存储订单信息，订单状态在运送过程中会不断更新，以 MongoDB 内嵌数组的形式来存储，一次查询就能将订单所有的变更读取出来。
- 物联网场景。使用 MongoDB 存储所有接入的智能设备信息，以及设备汇报的日志信息，并对这些信息进行多维度的分析。
- 视频直播。使用 MongoDB 存储用户信息，点赞互动信息等。

上述的应用场景中，数据操作方面的共同特点为数据量大、读写操作频繁、价值密度低因此对事务要求不高。对于这样的场景，我们可以使用MongoDB来实现数据的存储。

## 1.1 MongoDB 简介

MongoDB是一个开源、高性能、无模式的文档型数据库，当初的设计就是用于简化开发和方便扩展，是NoSQL数据库产品中的一种。是最像关系型数据库（MySQL）的非关系型数据库。 

| SQL术语/概念 | MongoDB术语/概念 |                  解释/说明                  |
| :----------: | :--------------: | :-----------------------------------------: |
|   database   |     database     |                   数据库                    |
|    table     |    collection    | 数据库表/集合，类似于数组, 在集合中存放文档 |
|     row      |     document     |   数据记录行/文档，文档型数据库的最小单位   |
|    column    |      field       |                 数据字段/域                 |
|    index     |      index       |                    索引                     |
| table joins  |                  |            表连接，MongoDB不支持            |
|              |     嵌入文档     |     MongoDB通过嵌入式文档来代替多表连接     |
| primary key  |   primary key    |    主键，MongoDB自动将_id字段设置为主键     |

MongoDB 的最小存储单位是 document 文档对象，它对应于关系型数据库的行。数据在MongoDB中以 BSON 文档的格式存储在磁盘上面。BSON 支持的数据结构非常松散，既可以存储比较复杂的数据类型，又相当的灵活。它具有轻量型、可遍历性、高效性三个特点，可以有效描述非结构化数据和结构化数据。这种格式的优点是灵活性高，但是它的缺点是空间利用率不是很理性。

BSON（Binary Serialized Document Format）是一种类 JSON 的一种二进制形式的存储格式，简称Binary JSON。BSON 和 JSON 一样，支持内嵌的文档对象和数组对象，但是 BSON 有 JSON 没有的一些数据类型，如 Date 和BinData类型。

| 数据类型      | 描述                                                      | 举例                              |
| ------------- | --------------------------------------------------------- | --------------------------------- |
| 字符串        | UTF-8 字符串都可表示为字符串类型的数据                    | {"x" : "foobar"}                  |
| 对象id        | 对象 id 是文档的12字节的唯一 ID                           | {"x" : ObjectId() }               |
| 布尔值        | 真或者假，true、false                                     | {"x" : true}+                     |
| 数组          | 值的集合或者列表可以表示成为数组                          | {"x" : ["a", "b", "c"]}           |
| 32位整数      | 类型不可用，JS 仅支持64位浮点数，所以32位整数会被转换     | 默认转为64位浮点数                |
| 64位整数      | 不支持该类型。shell会使用一个特殊的内嵌文档来显示64位整数 | 默认转为64位浮点数                |
| 64位浮点数    | shell中的数字就是这一种类型                               | {"x" : 3.14159, "y": 3}           |
| null          | 表示空值或者未定义的对象                                  | {"x" : null}                      |
| undefined     | 文档中也可以使用未定义的类型                              | {"x" : undefined}                 |
| 符号          | shell 不支持。自动转换成字符串                            |                                   |
| 正则表达式    | 采用JS的正则表达式语法                                    | {"x" : /foobar/i}                 |
| 代码          | 文档中可以包含JS代码                                      | {"x" : function() {/* ..... */ }} |
| 二进制数据    | 二进制数据可以由任意字节的串组成，不过shell无法使用       |                                   |
| 最大值/最小值 | BSON 包括一个特殊类型，表示可能的最值。shell没有改类型    |                                   |

> shell 默认使用64位浮点型数值。对于整型值，可以使用 NumberInt 4字节符号整数或者 NumberLong 8字节符号整数，例如`{"x" : NumberInt("3")}`

## 1.2 单机环境部署

MongoDB的版本命名规范为：x.y.z； 

* y为奇数时表示当前版本为开发版，如：1.5.2、4.1.13； 
* y为偶数时表示当前版本为稳定版，如：1.6.3、4.0.10；
*  z是修正版本号，数字越大越好。

**Windows 部署**

1. 从[官网](https://www.mongodb.com/download-center#community)下载预编译的二进制包，这里下载4.0.12版本的ZIP包。
2. 将压缩包解压到一个目录中。 在解压目录中，手动建立一个目录用于存放数据文件。4.0.12之后的某些版本已经不需要手动创建目录来存放数据文件了，下载之后就会自动创建 data 目录。

```sh
E:\MongoDB\bin>mongod --version
db version v6.0.5
Build Info: {
    "version": "6.0.5",
    "gitVersion": "c9a99c120371d4d4c52cbb15dac34a36ce8d3b1d",
    "modules": [],
    "allocator": "tcmalloc",
    "environment": {
        "distmod": "windows",
        "distarch": "x86_64",
        "target_arch": "x86_64"
    }
}
```

**Linux 部署（未测试，老师教程上面就是这样的）**

在Linux中部署一个单机的MongoDB，作为生产环境下使用。 步骤如下：

1. 先到官网下载压缩包 mongod-linux-x86_64-4.0.10.tgz 。

2. 上传压缩包到Linux中，解压到当前目录

   ```sh
   tar -xvf mongodb-linux-x86_64-4.0.10.tgz
   ```

3. 移动解压后的文件夹到指定的目录中

   ```sh
   mv mongodb-linux-x86_64-4.0.10 /usr/local/mongodb
   ```

4. 新建几个目录，分别用来存储数据和日志

   ```sh
   # 数据存储目录
   mkdir -p /mongodb/single/data/db
   # 日志存储目录
   mkdir -p /mongodb/single/log
   ```

5. 新建并修改配置文件 配置文件的内容如下：

   ```sh
   vi /mongodb/single/mongod.conf
   ```

   ```yml
   systemLog:
       #MongoDB发送所有日志输出的目标指定为文件
       # #The path of the log file to which mongod or mongos should send all diagnostic logging information
       destination: file
       #mongod或mongos应向其发送所有诊断日志记录信息的日志文件的路径
       path: "/mongodb/single/log/mongod.log"
       #当mongos或mongod实例重新启动时，mongos或mongod会将新条目附加到现有日志文件的末尾。
       logAppend: true
   storage:
       #mongod实例存储其数据的目录。storage.dbPath设置仅适用于mongod。
       ##The directory where the mongod instance stores its data.Default Value is "/data/db".
       dbPath: "/mongodb/single/data/db"
       journal:
           #启用或禁用持久性日志以确保数据文件保持有效和可恢复。
           enabled: true
   processManagement:
       #启用在后台运行mongos或mongod进程的守护进程模式。
       fork: true
   net:
       #服务实例绑定的IP，默认是localhost
       bindIp: localhost,192.168.0.2
       #bindIp
       #绑定的端口，默认是27017
       port: 27017
   ```

6. 启动MongoDB服务 

   ```sh
   [root@bobohost single]# /usr/local/mongodb/bin/mongod -f /mongodb/single/mongod.conf
   about to fork child process, waiting until server is ready for connections.
   forked process: 90384
   child process started successfully, parent exiting
   ```

   注意： 如果启动后不是 successfully ，则是启动失败了。原因基本上就是配置文件有问题。 通过进程来查看服务是否启动了： 

   ```sh
   [root@bobohost single]# ps -ef |grep mongod
   root 90384 1 0 8月26 ? 00:02:13 /usr/local/mongdb/bin/mongod -f /mongodb/single/mongod.conf
   ```

7. 分别使用mongo命令和compass工具来连接测试。 提示：如果远程连接不上，需要配置防火墙放行，或直接关闭linux防火墙 

8. 停止关闭服务 停止服务的方式有两种：快速关闭和标准关闭，下面依次说明：

   1. 快速关闭方法（快速，简单，数据可能会出错） 目标：通过系统的kill命令直接杀死进程：

      ```sh
      # 通过进程编号关闭节点
      kill -2 54410
      ```

       杀完要检查一下，避免有的没有杀掉。 

   2. 通过mongo客户端中的shutdownServer命令来关闭服务

      ```sh
      # 客户端登录服务，注意，这里通过localhost登录，如果需要远程登录，必须先登录认证才行。
      mongo --port 27017
      # 切换到admin库
      use admin
      # 关闭服务
      db.shutdownServer()
      ```

如果一旦是因为数据损坏，则需要进行如下操作（了解）：

```sh
# 删除lock文件
rm -f /mongodb/single/data/db/*.lock
# 修复数据
/usr/local/mongdb/bin/mongod --repair --dbpath=/mongodb/single/data/db
```

## 1.3 启动与卸载

### 1.3.1 启动服务

在data目录下面创建db文件夹，然后在 bin 目录中打开命令行提示符，输入如下命令：

```sh
# 启动MongoDB服务，并且数据存储在E:\MongoDB\data\db目录下面
mongod --dbpath E:\MongoDB\data\db
```

我们在启动信息中可以看到，mongoDB的默认端口是27017，如果我们想改变默认的启动端口，可以通过`--port`来指定端口。

### 1.3.2 连接服务

这里我用的是 6.0.5 版本，在 6.0 版本已经将其进行了较大的更新。删除了MongDB-Shell，所以需要我们自己进行下载，我下载的是1.8.2版本的，它更名为了`mongosh.exe`，所以运行的命令也从`mongo`更改为了`mongosh`。

```sh
# 启动mongosh连接MongoDB
E:\MongoDB\bin>mongosh
Current Mongosh Log ID: 6494099b52870cbac6495efe
Connecting to:          mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+1.8.2
Using MongoDB:          6.0.5
Using Mongosh:          1.8.2
# 查看所有数据库
test> show databases
admin    40.00 KiB
config   60.00 KiB
local    80.00 KiB
yapi    468.00 KiB
# 退出shell窗口 
test> exit

# 查看mongosh帮助手册
E:\MongoDB\bin>mongosh --help

  $ mongosh [options] [db address] [file names (ending in .js or .mongodb)]

  Options:

    -h, --help                                 Show this usage information
    -f, --file [arg]                           Load the specified mongosh script
```

> MongoDB javascript shell是一个基于 javascript 的解释器，故是支持 JS 程序的。

### 1.3.3 关闭服务

```sh
# 客户端登录服务
mongo --port 27017
# 切换到admin库
use admin
# 关闭服务
db.shutdownServer()
```

# 第二章 常用命令

在 MongoDB 中，数据库和集合都不需要手动创建，当我们创建文档时，如果文档所在的集合或者数据库不存在，则会自动创建数据库或者集合。

接下来将一个存放文章评论的数据存放到 MongoDB 中，数据库名称为 articledb，数据结构如下：

| 字段名称       | 字段含义       | 字段类型         | 备注                      |
| -------------- | -------------- | ---------------- | ------------------------- |
| _id            | ID             | ObjectId或String | Mongo的主键的字段         |
| articleid      | 文章ID         | String           |                           |
| content        | 评论内容       | String           |                           |
| userid         | 评论人ID       | String           |                           |
| nickname       | 评论人昵称     | String           |                           |
| createdatetime | 评论的日期时间 | Date             |                           |
| likenum        | 点赞数         | Int32            |                           |
| replynum       | 回复数         | Int32            |                           |
| state          | 状态           | String           | 0：不可见；1：可见；      |
| parentid       | 上级ID         | String           | 如果为0表示文章的顶级评论 |

## 2.1 数据库操作

默认保留的数据库

- admin：从权限角度考虑，这是 `root` 数据库，如果将一个用户添加到这个数据库，这个用户自动继承所有数据库的权限，一些特定的服务器端命令也只能从这个数据库运行，比如列出所有的数据库或者关闭服务器
- local：数据永远不会被复制，可以用来存储限于本地的单台服务器的集合（部署集群，分片等）
- config：Mongo 用于分片设置时，`config` 数据库在内部使用，用来保存分片的相关信息

|                     操作                     |               语法               |
| :------------------------------------------: | :------------------------------: |
|                查看所有数据库                | `show dbs;` 或 `show databases;` |
|                查看当前数据库                |              `db;`               |
| 切换到某数据库（若数据库不存在则创建数据库） |         `use <db_name>;`         |
|                删除当前数据库                |       `db.dropDatabase();`       |

```sh
# mongoShell
E:\MongoDB\bin>mongosh
# 查看所有数据库，默认会直接进入test数据库，如果没有选择数据库，集合将存放在 test 数据库中
test> show dbs;
admin    40.00 KiB
config   72.00 KiB
local    80.00 KiB
# 创建并切换数据库
test> use articledb
switched to db articledb
# 查看所有数据库并没有显示articledb，这是因为创建数据库之后会在内存中创建，并没有保存至磁盘中
# 数据库只有在内容插入后才会创建! 就是说，创建数据库后要再插入一个集合，数据库才会真正创建
articledb> show dbs;
admin    40.00 KiB
config   96.00 KiB
local    80.00 KiB
# 查看当前正在使用的数据库
articledb> db
articledb
# 删除当前使用的数据库，主要用来删除已经持久化的数据库
articledb> db.dropDatabase();
{ ok: 1, dropped: 'articledb' }
```

## 2.2 集合操作

集合类似关系型数据库中的表。可以显示的创建，也可以隐式的创建。显示创建就是调用命令来创建一个集合，隐式创建是插入文档的时候自动创建集合。

集合的命名规范： 

* 集合名不能是空字符串""。
* 集合名不能含有`\0`字符（空字符)，这个字符表示集合名的结尾。
* 集合名不能`system.`开头，这是为系统集合保留的前缀。
* 用户创建的集合名字不能含有保留字符。有些驱动程序的确支持在集合名里面包含，这是因为某些系统生成的集合中包含该字符。除非你要访问这种系统创建的集合，否则千万不要在名字里出现$。

|     操作     |                    语法                     |
| :----------: | :-----------------------------------------: |
| 查看所有集合 |    `show collections;`或者`show tables;`    |
|   创建集合   | `db.createCollection("<collection_name>");` |
|   删除集合   |        `db.<collection_name>.drop()`        |

```sh
# 查看当前使用的数据库
articledb> db
articledb
# 创建集合
articledb> db.createCollection("mycollection");
{ ok: 1 }
# 查看所有集合
articledb> show collections
mycollection
# 查看所有集合
articledb> show tables;
mycollection
# 删除mycollection集合。如果成功删除选定集合，则 drop() 方法返回 true，否则返回 false。
articledb> db.mycollection.drop()
true
```

## 2.3 文档操作

文档（document）的数据结构和 JSON 基本一样，所有存储在集合中的数据都是 BSON 格式。文档基础操作[CRUD](https://docs.mongodb.com/manual/crud/)：Create、Retrieve、Update、Delete。

### 2.3.1 文档的添加

可以向集合中添加一个文档或者多个文档。

* 使用 `db.<collection_name>.insert()` 向集合中添加一个文档，参数一个 json 格式的文档。
* 使用 `db.<collection_name>.insertMany()` 向集合中添加多个文档，参数为 json 文档数组。

注意事项：

1. 文档中的键/值对是有序的。
2. 文档中的值不仅可以是在双引号里面的字符串，还可以是其他几种数据类型（甚至可以是整个嵌入的文档)。
3. MongoDB区分类型和大小写。
4. MongoDB的文档不能有重复的键。
5. 文档的键是字符串。除了少数例外情况，键可以使用任意UTF-8字符。

文档键命名规范：键不能含有`\0` (空字符)，该字符用来表示键的结尾。`.`和`$`有特别的意义，只有在特定环境下才能使用。 以下划线`_`开头的键是保留的，并非严格要求。

当我们向集合中插入`document`文档时，如果没有给文档指定`_id`属性，那么数据库会为文档自动添加`_id`field，并且值类型是`ObjectId(blablabla)`，就是文档的唯一标识，类似于关系型数据库里的 `primary key`。

**添加单个文档**

使用 insert()、save()、insertOne()方法向集合中插入文档，语法如下：

```js
// <collection_name>为插入的集合，如果该集合不存在会隐式创建该集合
db.<collection_name>.insert({
    <document or array of documents>,
    writeConcern: <document>,
    ordered: <boolean>
})
```

| 参数         | 类型              | 介绍                                                         |
| ------------ | ----------------- | ------------------------------------------------------------ |
| document     | document or array | 要插入到集合中的文档或文档数组。（json格式）                 |
| writeConcern | document          | Optional. A document expressing the write concern. Omit to use the default write concern. See Write Concern.Do not explicitly set the write concern for the operation if run in a transaction. To use write concern with transactions, see Transactions and Write Concern. |
| ordered      | boolean           | 可选。如果为真，则按顺序插入数组中的文档，如果其中一个文档出现错误，MongoDB将返回而不处理数组中的其余文档。如果为假，则执行无序插入，如果其中一个文档出现错误，则继续处理数组中的主文档。在版本2.6+中默认为true |

```sh
# 向comment集合中添加数据，如果没有该集合，那么隐士创建集合
test> db.comment.insert({"articleid":"100000","content":"今天天气真好，阳光明媚","userid":"1001","nickname":"Rose","createdatetime":new Date(),"likenum":NumberInt(10),"state":null})
# 插入文档之后打印的结果
DeprecationWarning: Collection.insert() is deprecated. Use insertOne, insertMany, or bulkWrite.
{
  acknowledged: true,
  insertedIds: { '0': ObjectId("649568ab4325c10f27845873") }
}
```



```js
// 向集合中添加一个文档
db.mycollection.insertOne(
    { item: "canvas", qty: 100, tags: ["cotton"], size: { h: 28, w: 35.5, uom: "cm" } }
)
// 向集合中添加多个文档
db.mycollection.insertMany([
    { item: "journal", qty: 25, tags: ["blank", "red"], size: { h: 14, w: 21, uom: "cm" } },
    { item: "mat", qty: 85, tags: ["gray"], size: { h: 27.9, w: 35.5, uom: "cm" } },
    { item: "mousepad", qty: 25, tags: ["gel", "blue"], size: { h: 19, w: 22.85, uom: "cm" } }
])
```



如果某条数据插入失败, 将会终止插入, 但已经插入成功的数据**不会回滚掉**. 因为批量插入由于数据较多容易出现失败, 因此, 可以使用 `try catch` 进行异常捕捉处理, 测试的时候可以不处理.如：

```js
try {
  db.comment.insertMany([
    {"_id":"1","articleid":"100001","content":"我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.","userid":"1002","nickname":"相忘于江湖","createdatetime":new Date("2019-0805T22:08:15.522Z"),"likenum":NumberInt(1000),"state":"1"},
    {"_id":"2","articleid":"100001","content":"我夏天空腹喝凉开水, 冬天喝温开水","userid":"1005","nickname":"伊人憔 悴","createdatetime":new Date("2019-08-05T23:58:51.485Z"),"likenum":NumberInt(888),"state":"1"},
    {"_id":"3","articleid":"100001","content":"我一直喝凉开水, 冬天夏天都喝.","userid":"1004","nickname":"杰克船 长","createdatetime":new Date("2019-08-06T01:05:06.321Z"),"likenum":NumberInt(666),"state":"1"},
    {"_id":"4","articleid":"100001","content":"专家说不能空腹吃饭, 影响健康.","userid":"1003","nickname":"凯 撒","createdatetime":new Date("2019-08-06T08:18:35.288Z"),"likenum":NumberInt(2000),"state":"1"},
    {"_id":"5","articleid":"100001","content":"研究表明, 刚烧开的水千万不能喝, 因为烫 嘴.","userid":"1003","nickname":"凯撒","createdatetime":new Date("2019-0806T11:01:02.521Z"),"likenum":NumberInt(3000),"state":"1"}

]);

} catch (e) {
  print (e);
}
```

### 2.3.2 查询 Read

- 使用 `db.<collection_name>.find()` 方法对集合进行查询, 接受一个 json 格式的查询条件. 返回的是一个**数组**
- `db.<collection_name>.findOne()` 查询集合中符合条件的第一个文档, 返回的是一个**对象**

可以使用 `$in` 操作符表示*范围查询*

```js
db.inventory.find( { status: { $in: [ "A", "D" ] } } )
```

多个查询条件用逗号分隔, 表示 `AND` 的关系

```js
db.inventory.find( { status: "A", qty: { $lt: 30 } } )
```

等价于下面 sql 语句

```mysql
SELECT * FROM inventory WHERE status = "A" AND qty < 30
```

使用 `$or` 操作符表示后边数组中的条件是OR的关系

```js
db.inventory.find( { $or: [ { status: "A" }, { qty: { $lt: 30 } } ] } )
```

等价于下面 sql 语句

```mysql
SELECT * FROM inventory WHERE status = "A" OR qty < 30
```

联合使用 `AND` 和 `OR` 的查询语句

```js
db.inventory.find( {
     status: "A",
     $or: [ { qty: { $lt: 30 } }, { item: /^p/ } ]
} )
```

在 terminal 中查看结果可能不是很方便, 所以我们可以用 `pretty()` 来帮助阅读

```js
db.inventory.find().pretty()
```

匹配内容

```js
db.posts.find({
  comments: {
    $elemMatch: {
      user: 'Harry Potter'
    }
  }
}).pretty()

// 正则表达式
db.<collection_name>.find({ content : /once/ })
```

创建索引

```js
db.posts.createIndex({
  { title : 'text' }
})

// 文本搜索
// will return document with title "Post One"
// if there is no more posts created
db.posts.find({
  $text : {
    $search : "\"Post O\""
  }
}).pretty()
```

### 2.3.3 更新 Update

- 使用 `db.<collection_name>.updateOne(<filter>, <update>, <options>)` 方法修改一个匹配 `<filter>` 条件的文档
- 使用 `db.<collection_name>.updateMany(<filter>, <update>, <options>)` 方法修改所有匹配 `<filter>` 条件的文档
- 使用 `db.<collection_name>.replaceOne(<filter>, <update>, <options>)` 方法**替换**一个匹配 `<filter>` 条件的文档
- `db.<collection_name>.update(查询对象, 新对象)` 默认情况下会使用新对象替换旧对象

其中 `<filter>` 参数与查询方法中的条件参数用法一致.

如果需要修改指定的属性, 而不是替换需要用“修改操作符”来进行修改

- `$set` 修改文档中的制定属性

其中最常用的修改操作符即为`$set`和`$unset`,分别表示**赋值**和**取消赋值**.

```js
db.inventory.updateOne(
    { item: "paper" },
    {
        $set: { "size.uom": "cm", status: "P" },
        $currentDate: { lastModified: true }
    }
)

db.inventory.updateMany(
    { qty: { $lt: 50 } },
    {
        $set: { "size.uom": "in", status: "P" },
        $currentDate: { lastModified: true }
    }
)
```

> - uses the [`$set`](https://docs.mongodb.com/manual/reference/operator/update/set/#up._S_set) operator to update the value of the `size.uom` field to `"cm"` and the value of the `status` field to `"P"`,
> - uses the [`$currentDate`](https://docs.mongodb.com/manual/reference/operator/update/currentDate/#up._S_currentDate) operator to update the value of the `lastModified` field to the current date. If `lastModified` field does not exist, [`$currentDate`](https://docs.mongodb.com/manual/reference/operator/update/currentDate/#up._S_currentDate) will create the field. See [`$currentDate`](https://docs.mongodb.com/manual/reference/operator/update/currentDate/#up._S_currentDate) for details.

`db.<collection_name>.replaceOne()` 方法替换除 `_id` 属性外的**所有属性**, 其`<update>`参数应为一个**全新的文档**.

```js
db.inventory.replaceOne(
    { item: "paper" },
    { item: "paper", instock: [ { warehouse: "A", qty: 60 }, { warehouse: "B", qty: 40 } ] }
)
```

**批量修改**

```js
// 默认会修改第一条
db.document.update({ userid: "30", { $set {username: "guest"} } })

// 修改所有符合条件的数据
db.document.update( { userid: "30", { $set {username: "guest"} } }, {multi: true} )
```

**列值增长的修改**

如果我们想实现对某列值在原有值的基础上进行增加或减少, 可以使用 `$inc` 运算符来实现

```js
db.document.update({ _id: "3", {$inc: {likeNum: NumberInt(1)}} })
```

### 修改操作符

| Name                                                         | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`$currentDate`](https://docs.mongodb.com/manual/reference/operator/update/currentDate/#up._S_currentDate) | Sets the value of a field to current date, either as a Date or a Timestamp. |
| [`$inc`](https://docs.mongodb.com/manual/reference/operator/update/inc/#up._S_inc) | Increments the value of the field by the specified amount.   |
| [`$min`](https://docs.mongodb.com/manual/reference/operator/update/min/#up._S_min) | Only updates the field if the specified value is less than the existing field value. |
| [`$max`](https://docs.mongodb.com/manual/reference/operator/update/max/#up._S_max) | Only updates the field if the specified value is greater than the existing field value. |
| [`$mul`](https://docs.mongodb.com/manual/reference/operator/update/mul/#up._S_mul) | Multiplies the value of the field by the specified amount.   |
| [`$rename`](https://docs.mongodb.com/manual/reference/operator/update/rename/#up._S_rename) | Renames a field.                                             |
| [`$set`](https://docs.mongodb.com/manual/reference/operator/update/set/#up._S_set) | Sets the value of a field in a document.                     |
| [`$setOnInsert`](https://docs.mongodb.com/manual/reference/operator/update/setOnInsert/#up._S_setOnInsert) | Sets the value of a field if an update results in an insert of a document. Has no effect on update operations that modify existing documents. |
| [`$unset`](https://docs.mongodb.com/manual/reference/operator/update/unset/#up._S_unset) | Removes the specified field from a document.                 |

### 2.3.4 删除 Delete

- 使用 `db.collection.deleteMany()` 方法删除所有匹配的文档.
- 使用 `db.collection.deleteOne()` 方法删除单个匹配的文档.
- `db.collection.drop()`
- `db.dropDatabase()`

```js
db.inventory.deleteMany( { qty : { $lt : 50 } } )
```

> Delete operations **do not drop indexes**, even if deleting all documents from a collection.
>
> 一般数据库中的数据都不会真正意义上的删除, 会添加一个字段, 用来表示这个数据是否被删除

## 2.4 文档排序和投影 (sort & projection)

#### 2.3.1 排序 Sort

在查询文档内容的时候, 默认是按照 `_id` 进行排序

我们可以用 `$sort` 更改文档排序规则

```
{ $sort: { <field1>: <sort order>, <field2>: <sort order> ... } }
```

For the field or fields to sort by, set the sort order to `1` or `-1` to specify an *ascending* or *descending* sort respectively, as in the following example:

```
db.users.aggregate(
   [
     { $sort : { age : -1, posts: 1 } }
     // ascending on posts and descending on age
   ]
)
```

##### `$sort` Operator and Memory

##### `$sort` + `$limit` Memory Optimization

When a [`$sort`](https://docs.mongodb.com/manual/reference/operator/aggregation/sort/index.html#pipe._S_sort) precedes a [`$limit`](https://docs.mongodb.com/manual/reference/operator/aggregation/limit/#pipe._S_limit) and there are no intervening stages that modify the number of documents, the optimizer can coalesce the [`$limit`](https://docs.mongodb.com/manual/reference/operator/aggregation/limit/#pipe._S_limit) into the [`$sort`](https://docs.mongodb.com/manual/reference/operator/aggregation/sort/index.html#pipe._S_sort). This allows the [`$sort`](https://docs.mongodb.com/manual/reference/operator/aggregation/sort/index.html#pipe._S_sort) operation to **only maintain the top `n` results as it progresses**, where `n` is the specified limit, and ensures that MongoDB only needs to store `n` items in memory. This optimization still applies when `allowDiskUse` is `true` and the `n` items exceed the [aggregation memory limit](https://docs.mongodb.com/manual/core/aggregation-pipeline-limits/#agg-memory-restrictions).

Optimizations are subject to change between releases.

> 有点类似于用 heap 做 topK 这种问题, 只维护 k 个大小的 heap, 会加速 process

举个栗子:

```
db.posts.find().sort({ title : -1 }).limit(2).pretty()
```

#### 2.3.2 投影 Projection

有些情况, 我们对文档进行查询并不是需要所有的字段, 比如只需要 id 或者 用户名, 我们可以对文档进行“投影”

- `1` - display
- `0` - dont display

```
> db.users.find( {}, {username: 1} )

> db.users.find( {}, {age: 1, _id: 0} )
```

### 2.4 forEach()

```
> db.posts.find().forEach(fucntion(doc) { print('Blog Post: ' + doc.title) })
```

### 2.5 其他查询方式

#### 2.5.1 正则表达式

```
$ db.collection.find({field:/正则表达式/})

$ db.collection.find({字段:/正则表达式/})
```

#### 2.5.2 比较查询

`<`, `<=`, `>`, `>=` 这些操作符也是很常用的, 格式如下:

```
db.collection.find({ "field" : { $gt: value }}) // 大于: field > value
db.collection.find({ "field" : { $lt: value }}) // 小于: field < value
db.collection.find({ "field" : { $gte: value }}) // 大于等于: field >= value
db.collection.find({ "field" : { $lte: value }}) // 小于等于: field <= value
db.collection.find({ "field" : { $ne: value }}) // 不等于: field != value
```

#### 2.5.3 包含查询

包含使用 `$in` 操作符. 示例：查询评论的集合中 `userid` 字段包含 `1003` 或 `1004`的文档

```
db.comment.find({userid:{$in:["1003","1004"]}})
```

不包含使用 `$nin` 操作符. 示例：查询评论集合中 `userid` 字段不包含 `1003` 和 `1004` 的文档

```
db.comment.find({userid:{$nin:["1003","1004"]}})
```

## 2.6 常用命令小结

```
选择切换数据库：use articledb
插入数据：db.comment.insert({bson数据})
查询所有数据：db.comment.find();
条件查询数据：db.comment.find({条件})
查询符合条件的第一条记录：db.comment.findOne({条件})
查询符合条件的前几条记录：db.comment.find({条件}).limit(条数)
查询符合条件的跳过的记录：db.comment.find({条件}).skip(条数)

修改数据：db.comment.update({条件},{修改后的数据})
        或
        db.comment.update({条件},{$set:{要修改部分的字段:数据})

修改数据并自增某字段值：db.comment.update({条件},{$inc:{自增的字段:步进值}})

删除数据：db.comment.remove({条件})
统计查询：db.comment.count({条件})
模糊查询：db.comment.find({字段名:/正则表达式/})
条件比较运算：db.comment.find({字段名:{$gt:值}})
包含查询：db.comment.find({字段名:{$in:[值1, 值2]}})
        或
        db.comment.find({字段名:{$nin:[值1, 值2]}})

条件连接查询：db.comment.find({$and:[{条件1},{条件2}]})
           或
           db.comment.find({$or:[{条件1},{条件2}]})
```

## 3. 文档间的对应关系

- 一对一 (One To One)
- 一对多 (One To Many)
- 多对多 (Many To Many)

举个例子, 比如“用户-订单”这个一对多的关系中, 我们想查询某一个用户的所有或者某个订单, 我们可以

```
var user_id = db.users.findOne( {username: "username_here"} )._id
db.orders.find( {user_id: user_id} )
```

## 4. MongoDB 的索引

### 4.1 概述

索引支持在 MongoDB 中高效地执行查询.如果没有索引, MongoDB 必须执行全集合扫描, 即扫描集合中的每个文档, 以选择与查询语句 匹配的文档.这种扫描全集合的查询效率是非常低的, 特别在处理大量的数据时, 查询可以要花费几十秒甚至几分钟, 这对网站的性能是非常致命的.

如果查询存在适当的索引, MongoDB 可以使用该索引限制必须检查的文档数.

索引是特殊的数据结构, 它以易于遍历的形式存储集合数据集的一小部分.索引存储特定字段或一组字段的值, 按字段值排序.索引项的排 序支持有效的相等匹配和基于范围的查询操作.此外, MongoDB 还可以使用索引中的排序返回排序结果.

MongoDB 使用的是 B Tree, MySQL 使用的是 B+ Tree

```
// create index
db.<collection_name>.createIndex({ userid : 1, username : -1 })

// retrieve indexes
db.<collection_name>.getIndexes()

// remove indexes
db.<collection_name>.dropIndex(index)

// there are 2 ways to remove indexes:
// 1. removed based on the index name
// 2. removed based on the fields

db.<collection_name>.dropIndex( "userid_1_username_-1" )
db.<collection_name>.dropIndex({ userid : 1, username : -1 })

// remove all the indexes, will only remove non_id indexes
db.<collection_name>.dropIndexes()
```

### 4.2 索引的类型

#### 4.2.1 单字段索引

MongoDB 支持在文档的单个字段上创建用户定义的**升序/降序索引**, 称为**单字段索引** Single Field Index

对于单个字段索引和排序操作, 索引键的排序顺序（即升序或降序）并不重要, 因为 MongoDB 可以在任何方向上遍历索引.

![img](https://raw.githubusercontent.com/Zhenye-Na/img-hosting-picgo/master/img/image-20200505231043779.png)

#### 4.2.2 复合索引

MongoDB 还支持多个字段的用户定义索引, 即复合索引 Compound Index

复合索引中列出的字段顺序具有重要意义.例如, 如果复合索引由 `{ userid: 1, score: -1 }` 组成, 则索引首先按 `userid` 正序排序, 然后 在每个 `userid` 的值内, 再在按 `score` 倒序排序.

![img](https://raw.githubusercontent.com/Zhenye-Na/img-hosting-picgo/master/img/image-20200505231305941.png)

#### 4.2.3 其他索引

- 地理空间索引 Geospatial Index
- 文本索引 Text Indexes
- 哈希索引 Hashed Indexes

##### 地理空间索引（Geospatial Index）

为了支持对地理空间坐标数据的有效查询, MongoDB 提供了两种特殊的索引: 返回结果时使用平面几何的二维索引和返回结果时使用球面几何的二维球面索引.

##### 文本索引（Text Indexes）

MongoDB 提供了一种文本索引类型, 支持在集合中搜索字符串内容.这些文本索引不存储特定于语言的停止词（例如 “the”, “a”, “or”）, 而将集合中的词作为词干, 只存储根词.

##### 哈希索引（Hashed Indexes）

为了支持基于散列的分片, MongoDB 提供了散列索引类型, 它对字段值的散列进行索引.这些索引在其范围内的值分布更加随机, 但只支持相等匹配, 不支持基于范围的查询.

### 4.3 索引的管理操作

#### 4.3.1 索引的查看

语法

```
db.collection.getIndexes()
```

默认 `_id` 索引： MongoDB 在创建集合的过程中, 在 `_id` 字段上创建一个唯一的索引, 默认名字为 `_id` , 该索引可防止客户端插入两个具有相同值的文 档, 不能在 `_id` 字段上删除此索引.

注意：该索引是**唯一索引**, 因此值不能重复, 即 `_id` 值不能重复的.

在分片集群中, 通常使用 `_id` 作为**片键**.

#### 4.3.2 索引的创建

语法

```
db.collection.createIndex(keys, options)
```

参数

![image-20200506203419523](https://raw.githubusercontent.com/Zhenye-Na/img-hosting-picgo/master/img/image-20200506203419523.png)

options（更多选项）列表

![image-20200506203453430](https://raw.githubusercontent.com/Zhenye-Na/img-hosting-picgo/master/img/image-20200506203453430.png)

注意在 3.0.0 版本前创建索引方法为 `db.collection.ensureIndex()` , 之后的版本使用了 `db.collection.createIndex()` 方法, `ensureIndex()` 还能用, 但只是 `createIndex()` 的别名.

举个🌰

```
$  db.comment.createIndex({userid:1})
{
  "createdCollectionAutomatically" : false,
  "numIndexesBefore" : 1,
  "numIndexesAfter" : 2,
  "ok" : 1
}

$ db.comment.createIndex({userid:1,nickname:-1})
...
```

#### 4.3.3 索引的删除

语法

```
# 删除某一个索引
$ db.collection.dropIndex(index)

# 删除全部索引
$ db.collection.dropIndexes()
```

提示:

`_id` 的字段的索引是无法删除的, 只能删除非 `_id` 字段的索引

示例

```
# 删除 comment 集合中 userid 字段上的升序索引
$ db.comment.dropIndex({userid:1})
```

### 4.4 索引使用

#### 4.4.1 执行计划

分析查询性能 (Analyze Query Performance) 通常使用执行计划 (解释计划 - Explain Plan) 来查看查询的情况

```
$ db.<collection_name>.find( query, options ).explain(options)
```

比如: 查看根据 `user_id` 查询数据的情况

**未添加索引之前**

`"stage" : "COLLSCAN"`, 表示全集合扫描

![img](https://raw.githubusercontent.com/Zhenye-Na/img-hosting-picgo/master/img/image-20200506205714154.png)

**添加索引之后**

`"stage" : "IXSCAN"`, 基于索引的扫描

#### 4.4.2 涵盖的查询

当查询条件和查询的投影仅包含索引字段是, MongoDB 直接从索引返回结果, 而不扫描任何文档或将文档带入内存, 这些覆盖的查询十分有效

> https://docs.mongodb.com/manual/core/query-optimization/#covered-query

## 5. 在 Nodejs 中使用 MongoDB - mongoose

mongoose 是一个对象文档模型（ODM）库

> https://mongoosejs.com/

- 可以为文档创建一个模式结构（Schema）
- 可以对模型中的对象/文档进行验证
- 数据可以通过类型转换转换为对象模型
- 可以使用中间件应用业务逻辑

### 5.1 mongoose 提供的新对象类型

- Schema
  - 定义约束了数据库中的文档结构
  - 个人感觉类似于 SQL 中建表时事先规定表结构
- Model
  - 集合中的所有文档的表示, 相当于 MongoDB 数据库中的 collection
- Document
  - 表示集合中的具体文档, 相当于集合中的一个具体的文档

### 5.2 简单使用 Mongoose

> https://mongoosejs.com/docs/guide.html

使用 mongoose 返回的是一个 `mogoose Query object`, mongoose 执行 query 语句后的结果会被传进 callback 函数 `callback(error, result)`

> A mongoose query can be executed in one of two ways. First, if you pass in a `callback` function, Mongoose will execute the query asynchronously and pass the results to the `callback`.
>
> A query also has a `.then()` function, and thus can be used as a promise.

```
const q = MyModel.updateMany({}, { isDeleted: true }, function() {
  console.log("Update 1");
}));

q.then(() => console.log("Update 2"));
q.then(() => console.log("Update 3"));
```

上面这一段代码会执行三次 `updateMany()` 操作, 第一次是因为 callback, 之后的两次是因为 `.then()` (因为 `.then()` 也会调用 `updatemany()`)

**连接数据库并且创建 Model 类**

```
const mongoose = require('mongoose');
// test is the name of database, will be created automatically
mongoose.connect('mongodb://localhost:27017/test', {useNewUrlParser: true});

const Cat = mongoose.model('Cat', { name: String });

const kitty = new Cat({ name: 'Zildjian' });
kitty.save().then(() => console.log('meow'));
```

**监听 MongoDB 数据库的连接状态**

在 mongoose 对象中, 有一个属性叫做 `connection`, 该对象就表示数据库连接.通过监视该对象的状态, 可以来监听数据库的连接和端口

```
mongoose.connection.once("open", function() {
  console.log("connection opened.")
});

mongoose.connection.once("close", function() {
  console.log("connection closed.")
});
```

### 5.3 Mongoose 的 CRUD

首先定义一个 `Schema`

```
const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const blogSchema = new Schema({
    title:  String, // String is shorthand for {type: String}
    author: String,
    body:   String,
    comments: [{ body: String, date: Date }],
    date: { type: Date, default: Date.now },
    hidden: Boolean,
    meta: {
        votes: Number,
        favs:  Number
    }
});
```

然后在 `blogSchema` 基础上创建 `Model`

```
const Blog = mongoose.model('Blog', blogSchema);
// ready to go!

module.exports = Blog;
```

当调用上面这一行代码时, MongoDB 会做如下操作

1. 是否存在一个数据库叫做 `Blog` 啊? 没的话那就创建一个
2. 每次用到 Blog 库的时候都要注意内部数据要按照 `blogSchema` 来规定

向数据库中插入文档数据

```
Blog.create({
  title: "title"
  ...
}, function (err){
  if (!err) {
    console.log("successful")
  }
});
```

简单的查询一下下

```
// named john and at least 18 yo
MyModel.find({ name: 'john', age: { $gte: 18 }});
```

mongoose 支持的用法有:

- [`Model.deleteMany()`](https://mongoosejs.com/docs/api.html#model_Model.deleteMany)
- [`Model.deleteOne()`](https://mongoosejs.com/docs/api.html#model_Model.deleteOne)
- [`Model.find()`](https://mongoosejs.com/docs/api.html#model_Model.find)
- [`Model.findById()`](https://mongoosejs.com/docs/api.html#model_Model.findById)
- [`Model.findByIdAndDelete()`](https://mongoosejs.com/docs/api.html#model_Model.findByIdAndDelete)
- [`Model.findByIdAndRemove()`](https://mongoosejs.com/docs/api.html#model_Model.findByIdAndRemove)
- [`Model.findByIdAndUpdate()`](https://mongoosejs.com/docs/api.html#model_Model.findByIdAndUpdate)
- [`Model.findOne()`](https://mongoosejs.com/docs/api.html#model_Model.findOne)
- [`Model.findOneAndDelete()`](https://mongoosejs.com/docs/api.html#model_Model.findOneAndDelete)
- [`Model.findOneAndRemove()`](https://mongoosejs.com/docs/api.html#model_Model.findOneAndRemove)
- [`Model.findOneAndReplace()`](https://mongoosejs.com/docs/api.html#model_Model.findOneAndReplace)
- [`Model.findOneAndUpdate()`](https://mongoosejs.com/docs/api.html#model_Model.findOneAndUpdate)
- [`Model.replaceOne()`](https://mongoosejs.com/docs/api.html#model_Model.replaceOne)
- [`Model.updateMany()`](https://mongoosejs.com/docs/api.html#model_Model.updateMany)
- [`Model.updateOne()`](https://mongoosejs.com/docs/api.html#model_Model.updateOne)

## 6. 使用 Mocha 编写测试 “Test Driven Development”

Mocha 是一个 js 测试的包, 编写测试有两个关键字 `describe` 和 `it`

- `describe` 是一个”统领块”, 所有的 test functions 都会在它”名下”
- `it` 表示每一个 test function

```
create_test.js
const assert = require('assert')
// assume we have a User model defined in src/user.js
const User = require('../src/user')

// after installing Mocha, we have global access
// to describe and it keywords
describe('Creating records', () => {
  it('saves a user', () => {
    const joe = new User({ name: "Joe" });
    joe.save();
    assert()
  });
});
```

## 7. NoSQL Databases

**Benefits of NoSQL**

- Easy for inserting and retrieving data, since they are contained in one block, in one json object
- Flexible schema, if a new attribute added, it is easy to just add / append to the object
- Scalability, horizontally partition the data (availability > consistency)
- Aggregation, find metrics and etc

**Drawbacks of NoSQL**

- Update = Delete + Insert, not built for update
- Not consistent, ACID is not guaranteed, do not support transactions
- Not read optimized. Read entire block find the attribute. But SQL, just need one column (read time compartively slow)
- Relations are not implicit
- JOINS are hard to accomplish, all manually