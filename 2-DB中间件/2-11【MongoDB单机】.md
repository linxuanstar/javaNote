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

将一个存放文章评论的数据存放到 MongoDB 中，数据库名称为 articledb，集合名称为 comment，数据结构如下：

| 字段名称       | 字段含义       | 字段类型           | 备注                      |
| -------------- | -------------- | ------------------ | ------------------------- |
| _id            | ID             | ObjectId 或 String | Mongo的主键的字段         |
| articleid      | 文章ID         | String             |                           |
| content        | 评论内容       | String             |                           |
| userid         | 评论人ID       | String             |                           |
| nickname       | 评论人昵称     | String             |                           |
| createdatetime | 评论的日期时间 | Date               |                           |
| likenum        | 点赞数         | Int32              |                           |
| replynum       | 回复数         | Int32              |                           |
| state          | 状态           | String             | 0：不可见；1：可见；      |
| parentid       | 上级ID         | String             | 如果为0表示文章的顶级评论 |

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

1. 文档中的键值对是有序的。
2. 文档中的值不仅可以是在双引号里面的字符串，还可以是其他几种数据类型（甚至可以是整个嵌入的文档)。
3. MongoDB区分类型和大小写。
4. MongoDB的文档不能有重复的键。
5. 文档的键是字符串。除了少数例外情况，键可以使用任意UTF-8字符。

文档键命名规范：键不能含有`\0` (空字符)，该字符用来表示键的结尾。`.`和`$`有特别的意义，只有在特定环境下才能使用。 以下划线`_`开头的键是保留的，并非严格要求。

当我们向集合中插入`document`文档时，如果没有给文档指定`_id`属性，那么数据库会为文档自动添加`_id`field，并且值类型是`ObjectId(blablabla)`，就是文档的唯一标识，类似于关系型数据库里的 `primary key`。插入时如果指定了`_id`，则主键就是该值。

**添加单个文档**

使用 `insert()`、`save()`、`insertOne()`方法向集合中插入文档，语法如下：

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

```js
// 向comment集合中添加数据，如果没有该集合，那么隐士创建集合
test> db.comment.insert({
    "articleid":"100000",
    "content":"今天天气真好，阳光明媚",
    "userid":"1001",
    "nickname":"Rose",
    "createdatetime":new Date(),
    "likenum":NumberInt(10),
    "state":null
})
// 插入文档之后打印的结果
DeprecationWarning: Collection.insert() is deprecated. Use insertOne, insertMany, or bulkWrite.
{
  acknowledged: true,
  insertedIds: { '0': ObjectId("649568ab4325c10f27845873") }
}
```

**添加多个文档**

使用 `db.<collection_name>.insertMany()` 向集合中添加多个文档，参数为 json 文档数组。

```js
// 向集合中添加多个文档
db.comment.insertMany([
        {"_id":"1","articleid":"100001","content":"我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.","userid":"1002","nickname":"相忘于江湖","createdatetime":new Date("2019-0805T22:08:15.522Z"),"likenum":NumberInt(1000),"state":"1"},
        {"_id":"2","articleid":"100001","content":"我夏天空腹喝凉开水, 冬天喝温开水","userid":"1005","nickname":"伊人憔 悴","createdatetime":new Date("2019-08-05T23:58:51.485Z"),"likenum":NumberInt(888),"state":"1"}
])
```

如果某条数据插入失败，将会终止插入，但已经插入成功的数据不会回滚掉。因为批量插入由于数据较多容易出现失败，因此可以使用 `try catch` 进行异常捕捉处理，测试的时候可以不处理。如：

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

### 2.3.2 文档的查询

```js
// 查询数据的语法格式
db.collection.find(<query>, [projection])
```

| 参数       | 类型     | 描述                                                       |
| ---------- | -------- | ---------------------------------------------------------- |
| query      | document | 可选。使用查询运算符指定选择筛选器。                       |
| projection | document | 可选。指定要在与查询筛选器匹配的文档中返回的字段（投影）。 |

**查询所有**

- 使用 `db.<collection_name>.find()` 方法对集合进行查询，接受一个 json 格式的查询条件，返回一个数组。
- 使用`db.<collection_name>.findOne()` 查询集合中符合条件的第一个文档，返回的是一个对象。

```js
// 查询该集合所有文档
test> db.comment.find()
[
  {
    _id: ObjectId("649568ab4325c10f27845873"),
    articleid: '100000',
    content: '今天天气真好，阳光明媚',
    userid: '1001',
    nickname: 'Rose',
    createdatetime: ISODate("2023-06-23T09:40:59.430Z"),
    likenum: 10,
    state: null
  }
]
// 查询该集合中第一个符合该条件的文档
test> db.comment.findOne({articleid: "100000"})
{
  _id: ObjectId("649568ab4325c10f27845873"),
  articleid: '100000',
  content: '今天天气真好，阳光明媚',
  userid: '1001',
  nickname: 'Rose',
  createdatetime: ISODate("2023-06-23T09:40:59.430Z"),
  likenum: 10,
  state: null
}
```

**投影查询**

如果要查询结果返回部分字段，则需要使用投影查询（不显示所有字段，只显示指定的字段）。默认会返回`_id`字段，当然也可以将其强制屏蔽掉。

```js
// 投影查询，默认会返回_id字段
test> db.comment.find({articleid: "100001"}, {userid: 1, content: 1})
[
  { _id: '1', content: '我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.', userid: '1002' },
  { _id: '2', content: '我夏天空腹喝凉开水, 冬天喝温开水', userid: '1005' },
  { _id: '3', content: '我一直喝凉开水, 冬天夏天都喝.', userid: '1004' },
  { _id: '4', content: '专家说不能空腹吃饭, 影响健康.', userid: '1003' },
  { _id: '5', content: '研究表明, 刚烧开的水千万不能喝, 因为烫 嘴.', userid: '1003' }
]
// 将_id字段强制屏蔽掉，不让其返回
test> db.comment.find({articleid: "100001"}, {userid: 1, content: 1, _id: 0})
[
  { content: '我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.', userid: '1002' },
  { content: '我夏天空腹喝凉开水, 冬天喝温开水', userid: '1005' },
  { content: '我一直喝凉开水, 冬天夏天都喝.', userid: '1004' },
  { content: '专家说不能空腹吃饭, 影响健康.', userid: '1003' },
  { content: '研究表明, 刚烧开的水千万不能喝, 因为烫 嘴.', userid: '1003' }
]
```







可以使用 `$in` 操作符表示范围查询

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

### 2.3.3 文档的更新

-  `db.<collection_name>.updateOne(<filter>, <update>, <options>)` 方法修改一个匹配 `<filter>` 条件的文档
-  `db.<collection_name>.updateMany(<filter>, <update>, <options>)` 方法修改所有匹配 `<filter>` 条件的文档
-  `db.<collection_name>.replaceOne(<filter>, <update>, <options>)` 方法替换一个匹配 `<filter>` 条件的文档
- `db.<collection_name>.update(查询对象, 新对象)` 默认情况下会使用新对象替换旧对象，其中 `<filter>` 参数与查询方法中的条件参数用法一致。

| 操作符                                                       | 介绍                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`$currentDate`](https://docs.mongodb.com/manual/reference/operator/update/currentDate/#up._S_currentDate) | 将字段的值设置为当前日期，可以是date或Timestamp              |
| [`$inc`](https://docs.mongodb.com/manual/reference/operator/update/inc/#up._S_inc) | 将字段的值按指定的量递增                                     |
| [`$min`](https://docs.mongodb.com/manual/reference/operator/update/min/#up._S_min) | 仅在指定值小于现有字段值时更新字段                           |
| [`$max`](https://docs.mongodb.com/manual/reference/operator/update/max/#up._S_max) | 仅在指定值大于现有字段值时更新字段.                          |
| [`$mul`](https://docs.mongodb.com/manual/reference/operator/update/mul/#up._S_mul) | 将字段的值乘以指定的量                                       |
| [`$rename`](https://docs.mongodb.com/manual/reference/operator/update/rename/#up._S_rename) | 重命名字段                                                   |
| [`$set`](https://docs.mongodb.com/manual/reference/operator/update/set/#up._S_set) | 设置文档中字段的值                                           |
| [`$setOnInsert`](https://docs.mongodb.com/manual/reference/operator/update/setOnInsert/#up._S_setOnInsert) | 如果更新导致插入文档，则设置字段的值。对修改现有文档的更新操作没有影响。 |
| [`$unset`](https://docs.mongodb.com/manual/reference/operator/update/unset/#up._S_unset) | 从文档中删除指定字段                                         |

```js
// 最常用的更新操作
db.collection.update(query, update, options)
//或
db.collection.update(
    // 更新的选择条件。使用与find()查询方法中相同的查询选择器，类似sql update查询内where后面的。
    <query>,
    // 要应用的修改。该值可以是：更新运算符表达式的文、对的替换文档、在MongoDB 4.2中启动聚合管道。
    <update>,
    {
    	// 可选。如果设置为true，则在没有与查询条件匹配的文档时创建新文档。默认值为false。
        upsert: <boolean>,
    	// 可选。如果设置为true，则更新符合查询条件的多个文档。默认值为false。
        multi: <boolean>,
    	// 可选。表示写问题的文档。抛出异常的级别。
        writeConcern: <document>,
    	// 可选。指定要用于操作的校对规则。
        collation: <document>,
    	// 可选。一个筛选文档数组，用于确定要为数组字段上的更新操作修改哪些数组元素。
        arrayFilters: [ <filterdocument1>, ... ],
    	// 可选。指定用于支持查询谓词的索引的文档或字符串。
        hint: <document|string> // Available starting in MongoDB 4.2
	}
)
```

**覆盖修改**

使用`db.<collection_name>.update(查询对象, 新对象)`修改文档对象的时候，默认是进行覆盖修改，也就是会使用新对象替换掉旧对象。在6.0已经强制要求用户使用局部修改了：

```js
// 查询_id为1的文档
test> db.comment.find({_id: "1"})
[
  {
    _id: '1',
    articleid: '100001',
    content: '我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.',
    userid: '1002',
    nickname: '相忘于江湖',
    createdatetime: ISODate("1970-01-01T00:00:00.000Z"),
    likenum: 1000,
    state: '1'
  }
]
// 默认修改为覆盖修改，6.0已经强制要求用户改为局部修改，所以这样会出错
test> db.comment.update({_id: "1"}, {likenum: NumberInt(1001)})
MongoInvalidArgumentError: Update document requires atomic operators
```

**局部修改**

使用修改器/修改操作符`$set`来实现局部修改，`$set`用于修改文档中的制定属性，`$unset`为取消赋值。

```js
// 使用修改器局部修改
test> db.comment.update({_id: "1"}, {$set: {likenum: NumberInt(1001)}})
{
  acknowledged: true,
  insertedId: null,
  matchedCount: 1,
  modifiedCount: 1,
  upsertedCount: 0
}
```

**批量修改**

修改文档的时候默认只会修改第一条符合条件的文档，如果需要批量修改，那么需要添加`multi: true`条件。

```js
// comment集合中有一个文档，userid都是1003，修改它们的nickname为张三
test> db.comment.find()
[
    {
        _id: '4',
        articleid: '100001',
        content: '专家说不能空腹吃饭, 影响健康.',
        userid: '1003',
        nickname: '凯 撒',
        createdatetime: ISODate("2019-08-06T08:18:35.288Z"),
        likenum: 2000,
        state: '1'
    },
    {
        _id: '5',
        articleid: '100001',
        content: '研究表明, 刚烧开的水千万不能喝, 因为烫 嘴.',
        userid: '1003',
        nickname: '凯撒',
        createdatetime: ISODate("1970-01-01T00:00:00.000Z"),
        likenum: 3000,
        state: '1'
    }
]
// 修改userid为1003的文档，将nickname修改为张三。但是可以看到只有一条数据被修改，默认只会修改第一条
test> db.comment.update({userid: "1003"}, {$set: {nickname: "张三"}})
{
  acknowledged: true,
  insertedId: null,
  matchedCount: 1,
  modifiedCount: 1,
  upsertedCount: 0
}
```

```js
// 这时候就会修改两条
test> db.comment.update({userid: "1003"}, {$set: {nickname: "李四"}}, {multi: true})
{
  acknowledged: true,
  insertedId: null,
  matchedCount: 2,
  modifiedCount: 2,
  upsertedCount: 0
}
```

**列值增长的修改**

如果我们想实现对某列值在原有值的基础上进行增加或减少，可以使用 `$inc` 运算符来实现

```js
// 初始的likenum为1001
test> db.comment.find()
[
  {
    _id: '1',
    articleid: '100001',
    content: '我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.',
    userid: '1002',
    nickname: '相忘于江湖',
    createdatetime: ISODate("1970-01-01T00:00:00.000Z"),
    likenum: 1001,
    state: '1'
  },
]
// 设置增加一
test> db.comment.update({_id: "1"}, {$inc: {likenum: NumberInt(1)}})
{
  acknowledged: true,
  insertedId: null,
  matchedCount: 1,
  modifiedCount: 1,
  upsertedCount: 0
}
// 现在查询出来的likenum为1002
test> db.comment.find({_id: "1"})
[
  {
    _id: '1',
    articleid: '100001',
    content: '我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.',
    userid: '1002',
    nickname: '相忘于江湖',
    createdatetime: ISODate("1970-01-01T00:00:00.000Z"),
    likenum: 1002,
    state: '1'
  }
]
```

### 2.3.4 文档的删除

```js
// 使用remove方法已经过时了，所以改为deleteOne、deleteMany、findOneAndDelete
test> db.comment.remove({_id: "5"})
DeprecationWarning: Collection.remove() is deprecated. Use deleteOne, deleteMany, findOneAndDelete, or bulkWrite.
{ acknowledged: true, deletedCount: 1 }
```

文档删除常用的方法如下：

- 使用 `db.<collection_name>.deleteMany()`方法删除所有匹配的文档。
- 使用 `db.<collection_name>.deleteOne({})`方法删除单个匹配的文档。
- 使用`db.<collection_name>.drop()`方法删除集合。
- 使用`db.dropDatabase()`方法删除当前正在使用的数据库。

```js
// 删除id为4的文档
test> db.comment.deleteOne({_id: "4"})
{ acknowledged: true, deletedCount: 1 }
```

> 一般数据库中的数据都不会真正意义上的删除，会添加一个字段，用来表示这个数据是否被删除。

## 2.4 常用方法查询

| 常用方法查询 |  方法   |
| :----------: | :-----: |
|   统计查询   | count() |
|   分页查询   | limit() |
|   跳过查询   | skip()  |
|   排序查询   | sort()  |

**统计查询**

统计查询使用`count()`方法，但是在6.0中该方法已经过时，因此修改为使用`countDocuments()`方法

```sql
-- 使用count()会警告该方法已经过时，建议使用countDocuments方法
test> db.comment.count()
DeprecationWarning: Collection.count() is deprecated. Use countDocuments or estimatedDocumentCount.
5
-- 使用countDocuments()方法查询该集合中所有的文档数量
test> db.comment.countDocuments()
5
-- 使用countDocuments()方法查询该集合中userid为1004的文档的数量
test> db.comment.count({userid: "1004"})
1
```

**分页查询**

可以使用`limit()`方法来读取指定数量的数据，使用`skip()`方法来跳过指定数量的数据。

```sql
-- 分页查询前三条数据，默认值为20
db.comment.find().limit(3)
-- 跳过前三条数据查询之后的数据
db.comment.find().skip(3)

-- 分页查询，每页查询两条数据。跳过（当前页 - 1）* 每页查询的数量，读取每页需要查询的数量
-- 第一页
db.comment.find().skip(0).limit(2)
-- 第二页
db.comment.find().skip(2).limit(2)
-- 第三页
db.comment.find().skip(4).limit(2)
```

**排序查询**

`sort()` 方法对数据进行排序，`sort()` 方法可以通过参数指定排序的字段，并使用 1 和 -1 来指定排序的方式，其中 1 为升序排列，而 -1 是用于降序排列。

```sql
-- 使用语法
db.<collection_name>.find().sort({KEY:1})
-- 对userid降序排列，并对访问量进行升序排列
db.comment.find().sort({userid:-1,likenum:1})
```

> `skip()`、`limilt()`、`sort()`执行的顺序是`sort()` --> `skip()` --> `limit()`，和命令编写顺序无关。

## 2.5 操作符查询

|        查询方式         |     操作符     |
| :---------------------: | :------------: |
| 模糊查询/正则表达式查询 | `/正则表达式/` |
|          大于           |     `$gt`      |
|        大于等于         |     `$gte`     |
|          小于           |     `$lt`      |
|        小于等于         |     `$lte`     |
|         不等于          |     `$ne`      |
|          包含           |     `$in`      |
|         不包含          |     `$nin`     |
|        连接查询         | `$and`、`$or`  |

**正则查询**

MongoDB的模糊查询是通过正则表达式的方式实现的。正则表达式是js的语法，直接量的写法。

```sql
-- 正则表达式查询
db.<collection_name>.find({field:/正则表达式/})
-- 查询comment集合中content内容包含“开水”的所有文档
db.comment.find({content: /开水/})
-- 查询comment集合中评论的内容中以“专家”开头的
db.comment.find({content:/^专家/})
```

**比较查询**

<!-- gt:greater than、lt:less than、gte:greater than or equal、ne:not equal-->

`<, <=, >, >=` 这些操作符是很常用的

```sql
-- 展示所有大于value的字段的文档
db.<collection_name>.find({ "field" : { $gt: value }})
db.<collection_name>.find({ "field" : { $lt: value }})
db.<collection_name>.find({ "field" : { $gte: value }})
db.<collection_name>.find({ "field" : { $lte: value }})
db.<collection_name>.find({ "field" : { $ne: value }})
-- 查询comment集合中评论点赞数量likenum大于700的记录
db.comment.find({likenum: {$gt: NumberInt(700)}})
```

**包含查询**

包含使用`$in`操作符，不包含使用`$nin`操作符。 

```sql
-- 查询comment集合中userid字段包含1003或1004的文档
db.comment.find({userid: {$in: ["1003","1004"]}})
-- 查询comment集合中userid字段不包含1003和1004的文档
db.comment.find({userid: {$nin: ["1003","1004"]}})
```

**条件连接查询**

如果需要查询同时满足两个以上条件，需要使用`$and`操作符将条件进行关联，格式为：`$and:[ { },{ },{ } ]`。如果两个以上条件之间是或者的关系，我们使用`$or`操作符进行关联。

```sql
-- 查询评论集合中likenum大于等于700 并且小于2000的文档
db.comment.find({$and: [{likenum: {$gte: NumberInt(700)}}, {likenum: {$lt: NumberInt(2000)}}]})
-- 查询评论集合中userid为1003，或者点赞数小于1000的文档记录
db.comment.find({$or:[ {userid:"1003"} ,{likenum:{$lt:1000} }]})
```

## 2.6 JS 查询

```js
test> db.comment.find().forEach(function(doc) { print('content:' + doc.content)})
content:我们不应该把清晨浪费在手机上, 健康很重要, 一杯温水幸福你我 他.
content:我夏天空腹喝凉开水, 冬天喝温开水
content:我一直喝凉开水, 冬天夏天都喝.
content:专家说不能空腹吃饭, 影响健康.
content:研究表明, 刚烧开的水千万不能喝, 因为烫 嘴.
```

# 第三章 索引

[索引](https://docs.mongodb.com/manual/indexes/
)支持在 MongoDB 中高效地执行查询。如果没有索引，MongoDB 必须执行全集合扫描，即扫描集合中的每个文档，以选择与查询语句匹配的文档。这种扫描全集合的查询效率是非常低的，特别在处理大量的数据时，查询可能要花费几十秒甚至几分钟，这对网站的性能是非常致命的。

如果查询存在适当的索引，MongoDB 可以使用该索引限制必须检查的文档数。

索引是特殊的数据结构，它以易于遍历的形式存储集合数据集的一小部分。索引存储特定字段或一组字段的值，按字段值排序。索引项的排序支持有效的相等匹配和基于范围的查询操作。此外，MongoDB 还可以使用索引中的排序返回排序结果。

MongoDB 使用索引数据结构为的是 B Tree，MySQL 使用的是 B + Tree。



```sql
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

## 3.1 索引的类型

MongoDB中索引分为：单字段索引、复合索引、地理空间索引、文本索引、哈希索引。

**单字段索引**

MongoDB 支持在文档的单个字段上创建用户定义的升序/降序索引，称为单字段索引（Single Field Index）。对于单个字段索引和排序操作，索引键的排序顺序并不重要，因为 MongoDB 可以在任何方向上遍历索引。

![](D:\Java\笔记\图片\2-11【MongoDB单机】\3-1.png)

**复合索引**

MongoDB 还支持多个字段的用户定义索引，即复合索引（Compound Index）。复合索引中列出的字段顺序具有重要意义，如果复合索引由 `{ userid: 1, score: -1 }` 组成，则索引首先按 `userid` 正序排序，然后再按 `score` 倒序排序。

![](D:\Java\笔记\图片\2-11【MongoDB单机】\3-2.png)

**地理空间索引**

为了支持对地理空间坐标数据的有效查询，MongoDB 提供了两种特殊的索引：返回结果时使用平面几何的二维索引和返回结果时使用球面几何的二维球面索引。

**文本索引**

MongoDB 提供了一种文本索引类型，支持在集合中搜索字符串内容。这些文本索引不存储特定于语言的停止词（例如 “the”、“a”、“or”），而将集合中的词作为词干，只存储根词。

**哈希索引**

为了支持基于散列的分片，MongoDB 提供了散列索引类型，它对字段值的散列进行索引。这些索引在其范围内的值分布更加随机，但只支持相等匹配，不支持基于范围的查询。

## 3.2 索引的操作

**查看索引**

```sql
-- 查看索引语法
db.<collection_name>.getIndexes()
-- 查看comment集合中的索引，这里面的索引为_id索引，唯一索引。
db.comment.getIndexes()
[ { v: 2, key: { _id: 1 }, name: '_id_' } ]
```

默认为`_id`索引，MongoDB 在创建集合的过程中会在 `_id` 字段上创建一个唯一的索引，默认名字为 `_id`。该索引可防止客户端插入两个具有相同值的文 档，不能在 `_id` 字段上删除此索引。在分片集群中，通常使用 `_id` 作为片键。

**创建索引**

```sql
-- 创建索引语法
db.<collection_name>.createIndex(keys, options)
```

| 参数    | 类型     | 介绍                                                         |
| ------- | -------- | ------------------------------------------------------------ |
| keys    | document | 包含字段和值对的文档，其中字段是索引键，值描述该字段的索引类型（升序或者降序） |
| options | document | 可选。包含一组控制索引创建的选项的文档。有关详细信息，请参见下面的表格 |

| 参数               | 类型          | 介绍                                                         |
| ------------------ | ------------- | ------------------------------------------------------------ |
| background         | Boolean       | 默认创建索引会阻塞其它数据库操作，可指定以后台方式创建索引   |
| unique             | Boolean       | 建立的索引是否唯一，指定为true创建唯一索引。默认值为false。  |
| name               | string        | 索引的名称。默认根据连接索引的字段名和排序顺序生成名称。     |
| dropDups           | Boolean       | 已废弃。                                                     |
| sparse             | Boolean       | 对文档中不存在的字段数据不启用索引，默认值为 false。         |
| expireAfterSeconds | integer       | 指定一个以秒为单位的数值，完成 TTL设定，设定集合的生存时间。 |
| v                  | index version | 索引的版本号。默认的索引版本取决于mongod创建索引时运行的版本。 |
| weights            | document      | 索引权重值，数值在 1 到 99,999 之间。                        |
| language_override  | string        | 该参数指定了包含在文档中的字段名，语言覆盖默认的language     |
| default_language   | string        | 该参数决定了停用词及词干和词器的规则的列表。默认为英语       |

在 3.0.0 版本前创建索引方法为 `db.collection.ensureIndex()` ，之后的版本使用 `db.collection.createIndex()` 方法， `ensureIndex()` 还能用，但只是 `createIndex()` 的别名。

```sql
-- 对 userid 字段建立索引
test> db.comment.createIndex({userid: 1})
userid_1
test> db.comment.getIndexes()
[
  { v: 2, key: { _id: 1 }, name: '_id_' },
  { v: 2, key: { userid: 1 }, name: 'userid_1' }
]
-- 对 userid 和 nickname 同时建立复合（Compound）索引
test> db.comment.createIndex({userid:1,nickname:-1})
userid_1_nickname_-1
test> db.comment.getIndexes()
[
  { v: 2, key: { _id: 1 }, name: '_id_' },
  { v: 2, key: { userid: 1 }, name: 'userid_1' },
  {
    v: 2,
    key: { userid: 1, nickname: -1 },
    name: 'userid_1_nickname_-1'
  }
]
```

**索引的删除**

```sql
-- 指定索引的移除，可以通过索引名称或索引规范文档指定索引。若要删除文本索引，请指定索引名称。
db.<collection_name>.dropIndex(index)
-- 移除全部索引。_id 的字段的索引是无法删除的，只能删除非_id 字段的索引。
db.<collection_name>.dropIndexes()
```

```sql
-- 查看所有索引
test> db.comment.getIndexes()
[
    { v: 2, key: { _id: 1 }, name: '_id_' },
    { v: 2, key: { userid: 1 }, name: 'userid_1' },
    {
        v: 2,
        key: { userid: 1, nickname: -1 },
        name: 'userid_1_nickname_-1'
    }
]
-- 删除索引
test> db.comment.dropIndex({userid: 1})
{ nIndexesWas: 3, ok: 1 }
-- 再次查看索引，发现userid:1的索引已经删除
test> db.comment.getIndexes()
[
    { v: 2, key: { _id: 1 }, name: '_id_' },
    {
        v: 2,
        key: { userid: 1, nickname: -1 },
        name: 'userid_1_nickname_-1'
    }
]
```

## 3.3 执行计划

分析查询性能（Analyze Query Performance）通常使用执行计划（解释计划、Explain Plan）来查看查询的情况，如查询耗费的时间、是否基于索引查询等。 

```sql
-- 执行计划/解释计划语法
db.<collection_name>.find( query, options ).explain(options)
```

接下来通过执行`explain()`来看一下加索引和不加索引之后的效率：

```sql
-- 查寻comment集合中所有的索引，发现只有_id索引
test> db.comment.getIndexes()
[ { v: 2, key: { _id: 1 }, name: '_id_' } ]
-- 通过执行计划查看该条语句详情信息
test> db.comment.find({userid: "1003"}).explain()
{
  explainVersion: '1',
  queryPlanner: {
    namespace: 'test.comment',
    indexFilterSet: false,
    parsedQuery: { userid: { '$eq': '1003' } },
    queryHash: '82257C83',
    planCacheKey: '82257C83',
    maxIndexedOrSolutionsReached: false,
    maxIndexedAndSolutionsReached: false,
    maxScansToExplodeReached: false,
    winningPlan: {
      -- 代表全集合扫描
      stage: 'COLLSCAN',
      filter: { userid: { '$eq': '1003' } },
      direction: 'forward'
    },
    rejectedPlans: []
  },
  command: { find: 'comment', filter: { userid: '1003' }, '$db': 'test' },
  serverInfo: {
    host: 'DESKTOP-PA8B4PG',
    port: 27017,
    version: '6.0.5',
    gitVersion: 'c9a99c120371d4d4c52cbb15dac34a36ce8d3b1d'
  },
  serverParameters: {
    internalQueryFacetBufferSizeBytes: 104857600,
    internalQueryFacetMaxOutputDocSizeBytes: 104857600,
    internalLookupStageIntermediateDocumentMaxSizeBytes: 104857600,
    internalDocumentSourceGroupMaxMemoryBytes: 104857600,
    internalQueryMaxBlockingSortMemoryUsageBytes: 104857600,
    internalQueryProhibitBlockingMergeOnMongoS: 0,
    internalQueryMaxAddToSetBytes: 104857600,
    internalDocumentSourceSetWindowFieldsMaxMemoryBytes: 104857600
  },
  ok: 1
}
-- 创建索引
test>  db.comment.createIndex({userid:1})
userid_1
-- 再次查看该SQL语句执行效率
test> db.comment.find({userid: "1003"}).explain()
{
  explainVersion: '1',
  queryPlanner: {
    namespace: 'test.comment',
    indexFilterSet: false,
    parsedQuery: { userid: { '$eq': '1003' } },
    queryHash: '82257C83',
    planCacheKey: '75A2858A',
    maxIndexedOrSolutionsReached: false,
    maxIndexedAndSolutionsReached: false,
    maxScansToExplodeReached: false,
    winningPlan: {
      stage: 'FETCH',
      inputStage: {
        -- 基于索引的扫描
        stage: 'IXSCAN',
        keyPattern: { userid: 1 },
        indexName: 'userid_1',
        isMultiKey: false,
        multiKeyPaths: { userid: [] },
        isUnique: false,
        isSparse: false,
        isPartial: false,
        indexVersion: 2,
        direction: 'forward',
        indexBounds: { userid: [ '["1003", "1003"]' ] }
      }
    },
    rejectedPlans: []
  },
  command: { find: 'comment', filter: { userid: '1003' }, '$db': 'test' },
  serverInfo: {
    host: 'DESKTOP-PA8B4PG',
    port: 27017,
    version: '6.0.5',
    gitVersion: 'c9a99c120371d4d4c52cbb15dac34a36ce8d3b1d'
  },
  serverParameters: {
    internalQueryFacetBufferSizeBytes: 104857600,
    internalQueryFacetMaxOutputDocSizeBytes: 104857600,
    internalLookupStageIntermediateDocumentMaxSizeBytes: 104857600,
    internalDocumentSourceGroupMaxMemoryBytes: 104857600,
    internalQueryMaxBlockingSortMemoryUsageBytes: 104857600,
    internalQueryProhibitBlockingMergeOnMongoS: 0,
    internalQueryMaxAddToSetBytes: 104857600,
    internalDocumentSourceSetWindowFieldsMaxMemoryBytes: 104857600
  },
  ok: 1
}
```

## 3.4 覆盖索引

当查询条件和查询的投影仅包含索引字段时，MongoDB直接从索引返回结果，而不扫描任何文档或将文档带入内存。这些覆盖的查询可以非常有效。

# 第四章 文章评论案例

需要实现以下功能：

1. 基本增删改查API
2. 根据文章id查询评论
3. 评论点赞

这里仅需要做一下后端的CRUD开发即可，无需搭建前端静态页面！

| 字段名称       | 字段含义       | 字段类型           | 备注                      |
| -------------- | -------------- | ------------------ | ------------------------- |
| _id            | ID             | ObjectId 或 String | Mongo的主键的字段         |
| articleid      | 文章ID         | String             |                           |
| content        | 评论内容       | String             |                           |
| userid         | 评论人ID       | String             |                           |
| nickname       | 评论人昵称     | String             |                           |
| createdatetime | 评论的日期时间 | Date               |                           |
| likenum        | 点赞数         | Int32              |                           |
| replynum       | 回复数         | Int32              |                           |
| state          | 状态           | String             | 0：不可见；1：可见；      |
| parentid       | 上级ID         | String             | 如果为0表示文章的顶级评论 |

## 4.1 技术选型

可以选择偏底层的 mongodb-driver，也可以选择封装大部分功能的 SpringDataMongoDB，这里我们选择SpringDataMongoDB。

* [mongodb-driver](http://mongodb.github.io/mongo-java-driver/) 是 mongo 官方推出的 java 连接 mongoDB 的驱动包，相当于JDBC驱动。我们通过一个入门的案例来了解mongodb-driver 的基本使用。
* [SpringDataMongoDB](https://projects.spring.io/spring-data-mongodb/) 是 SpringData 家族成员之一，用于操作 MongoDB 的持久层框架，封装了底层的mongodb-driver。

## 4.2 项目搭建

本项目采用SpringBoot + MongoDB技术栈，创建项目的时候先创建一个空的Maven项目，然后再导入需要的依赖。

```xml
<!--配置当前工程继承自parent工程-->
<parent>
    <!-- SpringBoot起步依赖 -->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.6.RELEASE</version>
    <!-- 设置父项目pom.xml位置路径，如果是<relativePath/>那么代表从仓库里面找 -->
    <relativePath/>
</parent>

<dependencies>
    <!-- test测试依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <!-- mongodb依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <!-- Lombok简化实体类开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

```yaml
spring:
  #数据源配置
  data:
    mongodb:
      # 主机地址
      host: localhost
      # 数据库
      database: test
      # 默认端口是27017
      port: 27017
      # 也可以使用uri连接
      # uri: mongodb://localhost:27017/test
```

```java
package com.linxuan;

@SpringBootApplication
public class Demo01Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo01Application.class, args);
    }
}
```

## 4.3 实体类编写

```java
package com.linxuan.pojo;

/**
 * 文章评论实体类
 *  1.Document注解把类声明为mongodb集合，可以通过collection指定该类对应的集合。默认使用类名小写映射集合。
 *  2.CompoundIndex注解创建复合索引：@CompoundIndex( def = "{'userid': 1, 'nickname': -1}")
 *  3.索引可以通过Mongo的命令来添加，也可以在Java的实体类中通过注解添加。
 */
@Data
@ToString
@Document(collection = "comment")
public class Comment implements Serializable {
    // 主键标识，该属性的值会自动对应mongodb的主键字段"_id"。如果该属性名就叫“id”，则该注解可以省略。
    @Id
    private String id;
    // 吐槽内容。该属性对应mongodb的字段的名字，如果一致，则无需该注解
    @Field("content")
    private String content;
    // 发布人ID。添加一个单字段的索引，如果comment集合中已经有该索引，那么会报错。
    @Indexed
    private String userid;
    
    // 发布日期
    private Date publishtime;
    // 昵称
    private String nickname;
    // 评论的日期时间
    private LocalDateTime createdatetime;
    // 点赞数
    private Integer likenum;
    // 回复数
    private Integer replynum;
    // 状态
    private String state;
    // 上级ID
    private String parentid;
    // 文章ID
    private String articleid;
}
```

## 4.4 评论的增删改查

```java
package com.linxuan.dao;

public interface CommentDao extends MongoRepository<Comment, String> {
}
```

```java
package com.linxuan.service;

public interface CommentService {

    /**
     * 新增评论
     * @param comment 需要新增的评论
     */
    void saveComment(Comment comment);

    /**
     * 更新评论
     * @param comment 更新后的评论，根据comment里面的主键进行更新
     */
    void updateComment(Comment comment);

    /**
     * 根据ID删除评论
     * @param id id
     */
    void deleteCommentById(String id);

    /**
     * 查询所有的评论列表
     * @return 返回所有的评论集合
     */
    List<Comment> findCommentList();

    /**
     * 根据id查询评论
     * @param id 需要查询的id
     * @return 返回评论
     */
    Comment findCommentById(String id);
}
```

```java
package com.linxuan.service.impl;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public void saveComment(Comment comment) {
        commentDao.save(comment);
    }

    @Override
    public void updateComment(Comment comment) {
        commentDao.save(comment);
    }

    @Override
    public void deleteCommentById(String id) {
        commentDao.deleteById(id);
    }

    @Override
    public List<Comment> findCommentList() {
        return commentDao.findAll();
    }

    @Override
    public Comment findCommentById(String id) {
        return commentDao.findById(id).get();
    }
}
```

新建Junit测试类，测试保存和查询所有

```java
package com.linxuan;

// SpringBoot的Junit集成测试
@RunWith(SpringRunner.class)
// SpringBoot的测试环境初始化，参数：启动类
@SpringBootTest(classes = Demo01Application.class)
public class CommentServiceTest {

    // 注入Service
    @Autowired
    private CommentService commentService;

    /**
     * 新增一个评论
     */
    @Test
    public void testSaveComment() {
        Comment comment = new Comment();
        comment.setArticleid("100000");
        comment.setContent("测试添加的数据");
        comment.setCreatedatetime(LocalDateTime.now());
        comment.setUserid("1003");
        comment.setNickname("凯撒大帝");
        comment.setState("1");
        comment.setLikenum(0);
        comment.setReplynum(0);
        commentService.saveComment(comment);
    }

    /**
     * 查询所有数据
     */
    @Test
    public void testFindAll() {
        List<Comment> list = commentService.findCommentList();
        list.forEach(System.out::println);
    }

    /**
     * 测试根据id查询
     */
    @Test
    public void testFindCommentById() {
        Comment comment = commentService.findCommentById("1");
        System.out.println(comment);
    }
}
```

## 4.5 根据上级ID分页查询

```java
public interface CommentDao extends MongoRepository<Comment, String> {

    /**
     * 根据父id，查询子评论的分页列表
     *
     * @param parentid 父评论id
     * @param pageable 所有分页相关信息的一个抽象
     * @return 返回分页查询结果
     */
    Page<Comment> findByParentid(String parentid, Pageable pageable);
}
```

```java
public interface CommentService {
    
    /**
     * 根据父id查询分页列表
     *
     * @param parentid 父id
     * @param page 页数
     * @param size 每页数量
     * @return 查询结果
     */
    Page<Comment> findCommentListPageByParentid(String parentid, int page, int size);
}
```

```java
@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public Page<Comment> findCommentListPageByParentid(String parentid, int page, int size) {
        return commentDao.findByParentid(parentid, PageRequest.of(page, size));
    }
}
```

```java
// SpringBoot的Junit集成测试
@RunWith(SpringRunner.class)
// SpringBoot的测试环境初始化，参数：启动类
@SpringBootTest(classes = Demo01Application.class)
public class CommentServiceTest {

    @Test
    public void testFindCommentListPageByParentid() {
        Page<Comment> pageResponse = commentService.findCommentListPageByParentid("3", 1, 2);
        System.out.println("----总记录数：" + pageResponse.getTotalElements());
        System.out.println("----当前页数据：" + pageResponse.getContent());
    }
}
```

## 4.6 评论点赞

我们看一下以下点赞的临时示例代码： CommentService 新增updateThumbup方法

```java
/**
* 点赞-效率低
* @param id
*/
public void updateCommentThumbupToIncrementingOld(String id){
    Comment comment = CommentRepository.findById(id).get();
    comment.setLikenum(comment.getLikenum()+1);
    CommentRepository.save(comment);
}
```

以上方法虽然实现起来比较简单，但是执行效率并不高，因为我们只需要将点赞数加1就可以了，没必要查询出所有字段修改后再更新所有字段。

我们可以使用MongoTemplate类来实现对某列的操作。

```java
@Service
public class CommentServiceImpl implements CommentService {
    // 注入MongoTemplate
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 点赞数+1
     *
     * @param id 需要更新的id
     */
    public void updateCommentLikenum(String id) {
        // 查询对象
        Query query = Query.query(Criteria.where("_id").is(id));
        // 更新对象
        Update update = new Update();
        // update.set(key,value); 局部更新，相当于$set
        // update.inc("likenum",1); 递增$inc
        update.inc("likenum");
        // 参数1为查询对象、参数2为更新对象、参数3为集合的名字或实体类的类型Comment.class
        mongoTemplate.updateFirst(query, update, "comment");
    }
}
```

