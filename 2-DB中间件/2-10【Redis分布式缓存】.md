单机的Redis存在四大问题：

* 数据丢失问题——实现Redis数据持久化
* 并发能力问题——搭建主从集群，实现读写分离
* 故障恢复问题——利用Redis哨兵，实现健康检测和自动恢复
* 存储能力问题——搭建分片集群，利用插槽机制实现动态扩容

# 第一章 Redis持久化

Redis是一个内存数据库，当Redis服务器重启，获取电脑重启，数据会丢失，我们可以将Redis内存中的数据持久化保存到硬盘的文件中。Redis持久化机制有两种：RDB和AOF。

## 1.1 RDB持久化

RDB（Redis Database Backup file）：Redis数据备份文件，也被叫做Redis数据快照。简单来说就是把内存中的所有数据都记录到磁盘中。当Redis实例故障重启后，从磁盘读取快照文件，恢复数据。快照文件称为RDB文件，默认是保存在当前运行目录。

RDB这种方式就是默认的，不需要进行配置，默认就使用这种机制。

**执行时机**

RDB持久化在四种情况下会执行：执行save命令、执行bgsave命令、Redis停机时、触发RDB条件时。

- 执行save命令：save命令会导致主进程执行RDB，这个过程中其它所有命令都会被阻塞。数据迁移时用到。

  ```sh
  [linxuan@192 ~]$ redis-cli
  127.0.0.1:6379> save
  OK
  127.0.0.1:6379> 
  ```
  
- 执行bgsave命令：这个命令执行后会开启独立进程完成RDB，主进程可以持续处理用户请求，不受影响。

  ```sh
  [linxuan@192 ~]$ redis-cli
  127.0.0.1:6379> bgsave
  Background saving started
  127.0.0.1:6379> 
  ```
  
- Redis停机时：Redis停机时会执行一次save命令，实现RDB持久化。

- 触发RDB条件时：Redis内部有触发RDB的机制，可以在redis.conf文件中找到，格式如下：

  ```properties
  # 除非另有说明，默认情况下Redis会保存DB:
  # Unless specified otherwise, by default Redis will save the DB:
  #   * 在3600秒(一小时)后，如果至少有一个键改变 则执行bgsave
  #   * After 3600 seconds (an hour) if at least 1 key changed
  #   * After 300 seconds (5 minutes) if at least 100 keys changed
  #   * After 60 seconds if at least 10000 keys changed
  #
  # 可以通过取消注释下面的三行显式地设置这些参数。
  # You can set these explicitly by uncommenting the three following lines.
  #
  # save 3600 1
  # save 300 100
  # save 60 10000
  ```
  
  RDB的其它配置也可以在`redis.conf`文件中设置：
  
  ```properties
  # 是否压缩 ,建议不开启，压缩也会消耗cpu，磁盘的话不值钱
  rdbcompression yes
  # RDB文件名称
  dbfilename dump.rdb  
  # RDB文件保存的路径目录
  dir ./ 
  ```

**RDB原理**

bgsave开始时会fork主进程得到子进程，子进程共享主进程的内存数据。完成fork后读取内存数据并写入 RDB 文件。

fork采用的是copy-on-write技术：

- 当主进程执行读操作时，访问共享内存；
- 当主进程执行写操作时，则会拷贝一份数据，执行写操作。
- 用新RDB文件替换旧的RDB文件

<img src="..\图片\2-10【Redis分布式缓存】/1-1.png" />

**RDB缺点**

- RDB执行间隔时间长，两次RDB之间写入数据有丢失的风险
- fork子进程、压缩、写出RDB文件都比较耗时

## 1.2 AOF持久化

AOF（Append Only File）：追加文件。Redis处理的每一个写命令都会记录在AOF文件中，可以看做该文件是命令日志文件。

**AOF配置**

AOF默认是关闭的，需要修改redis.conf配置文件来开启AOF：

```properties
# 是否开启AOF功能，默认是no
appendonly yes
# AOF文件的名称
appendfilename "appendonly.aof"
```

AOF的命令记录的频率也可以通过redis.conf文件来配：

```properties
# 表示每执行一次写命令，立即记录到AOF文件
appendfsync always 
# 写命令执行完先放入AOF缓冲区，然后表示每隔1秒将缓冲区数据写到AOF文件，是默认方案
appendfsync everysec 
# 写命令执行完先放入AOF缓冲区，由操作系统决定何时将缓冲区内容写回磁盘
appendfsync no
```

三种策略对比：

| 配置项   | 刷盘时机     | 优点                     | 缺点                         |
| -------- | ------------ | ------------------------ | ---------------------------- |
| Always   | 同步刷盘     | 可靠性高，几乎不丢失数据 | 性能影响大                   |
| everysec | 每秒刷盘     | 性能适中                 | 最多丢失1秒数据              |
| no       | 操作系统控制 | 性能最好                 | 可靠性较差，可能丢失大量数据 |

**AOF文件重写**

因为是记录命令，AOF文件会比RDB文件大的多。而且AOF会记录对同一个key的多次写操作，但只有最后一次写操作才有意义。通过执行`bgrewriteaof`命令，可以让AOF文件执行重写功能，用最少的命令达到相同效果。

<img src="..\图片\2-10【Redis分布式缓存】\1-2.png" />

Redis也会在触发阈值时自动去重写AOF文件。阈值也可以在redis.conf中配置：

```properties
# AOF文件比上次文件 增长超过多少百分比则触发重写
auto-aof-rewrite-percentage 100
# AOF文件体积最小多大以上才触发重写 
auto-aof-rewrite-min-size 64mb 
```

## 1.3 RDB与AOF对比

RDB和AOF各有自己的优缺点，如果对数据安全性要求较高，在实际开发中往往会结合两者来使用。

|                | RDB                                          | AOF                                                          |
| -------------- | -------------------------------------------- | ------------------------------------------------------------ |
| 持久化方式     | 定时对整个内存做快照                         | 记录每一次执行的命令                                         |
| 数据完整性     | 不完整，两次备份之间会丢失                   | 相对完整，取决于刷盘策略                                     |
| 文件大小       | 会有压缩，文件体积小                         | 记录命令，文件体积很大                                       |
| 宕机恢复速度   | 很快                                         | 慢                                                           |
| 数据恢复优先级 | 低，因为数据完整性不如AOF                    | 高，因为数据完整性更高                                       |
| 系统资源占用   | 高，大量CPU和内存消耗                        | 低，主要占用磁盘IO资源<br />但AOF重写时占用大量CPU和内存资源 |
| 使用场景       | 可以容忍数分钟的数据丢失，追求更快的启动速度 | 对数据安全性要求较高常见                                     |

# 第二章 主从架构

单节点Redis的并发能力是有上限的，要进一步提高Redis的并发能力，就需要搭建主从集群，实现读写分离。

<img src="..\图片\2-10【Redis分布式缓存】\2-1.png" />

## 2.1 搭建主从架构

这里我们会在同一台虚拟机中开启3个Redis实例，模拟主从集群。共包含三个节点，一个主节点，两个从节点。信息如下：

|       IP       | PORT |  角色  |
| :------------: | :--: | :----: |
| 192.168.66.136 | 7001 | master |
| 192.168.66.136 | 7002 | slave  |
| 192.168.66.136 | 7003 | slave  |

**搭建主从架构**

要在同一台虚拟机开启3个实例，必须准备三份不同的配置文件和目录，配置文件所在目录也就是工作目录。

1. 创建目录：我们创建三个文件夹，名字分别叫7001、7002、7003。

   ```sh
   # 进入/usr/local/redis目录
   cd /usr/local/redis
   # 创建目录
   mkdir 7001 7002 7003
   ```
   
2. 恢复原始配置：修改redis.conf文件，将其中的持久化模式改为默认的RDB模式，AOF保持关闭状态。

   ```properties
   # 开启RDB
   # save ""
   save 3600 1
   save 300 100
   save 60 10000
   
   # 关闭AOF
   appendonly no
   ```
   
3. 拷贝配置文件到每个实例目录：将redis.conf文件拷贝到三个目录中。

   ```sh
   # 方式一：逐个拷贝
   cp redis-6.2.4/redis.conf 7001
   cp redis-6.2.4/redis.conf 7002
   cp redis-6.2.4/redis.conf 7003
   # 方式二：管道组合命令，一键拷贝
   echo 7001 7002 7003 | xargs -t -n 1 cp redis-6.2.4/redis.conf
   ```
   
4. 修改每个实例的端口、工作目录：修改每个文件夹内的配置文件，将端口分别修改为7001、7002、7003，将rdb文件保存位置都修改为自己所在目录

   ```sh
   sed -i -e 's/6379/7001/g' -e 's/dir .\//dir \/usr\/local\/Redis\/7001\//g' 7001/redis.conf
   sed -i -e 's/6379/7002/g' -e 's/dir .\//dir \/usr\/local\/Redis\/7002\//g' 7002/redis.conf
   sed -i -e 's/6379/7003/g' -e 's/dir .\//dir \/usr\/local\/Redis\/7003\//g' 7003/redis.conf
   ```
   
5. 修改每个实例的声明IP：虚拟机本身有多个IP，为了避免将来混乱，我们需要在redis.conf文件中指定每一个实例的绑定ip信息。

   ```properties
   # Redis实例的声明 IP
   replica-announce-ip 192.168.66.140
   ```
   
   每个目录都要改，我们一键完成修改（在/usr/local/redis目录执行下列命令）：

   ```sh
   # 逐一执行
   sed -i '1a replica-announce-ip 192.168.66.140' 7001/redis.conf
   sed -i '1a replica-announce-ip 192.168.66.140' 7002/redis.conf
   sed -i '1a replica-announce-ip 192.168.66.140' 7003/redis.conf
   
   # 或者一键修改
   printf '%s\n' 7001 7002 7003 | xargs -I{} -t sed -i '1a replica-announce-ip 192.168.66.140' {}/redis.conf
   ```

**启动**

为了方便查看日志，我们打开3个ssh窗口，分别启动3个Redis实例，启动命令：

```sh
# 第1个
redis-server /usr/local/redis/7001/redis.conf
# 第2个
redis-server /usr/local/redis/7002/redis.conf
# 第3个
redis-server /usr/local/redis/7003/redis.conf
```

如果要一键停止，可以运行下面命令：

```sh
printf '%s\n' 7001 7002 7003 | xargs -I{} -t redis-cli -p {} shutdown
```

**开启主从关系**

现在三个实例还没有任何关系，要配置主从可以使用`replicaof` 或者`slaveof`（5.0以前）命令。在5.0以后新增命令replicaof，与salveof效果一致。

有临时和永久两种模式：

- 修改配置文件（永久生效），在redis.conf中添加一行配置：

  ```apl
  slaveof <masterip> <masterport>
  ```

- 使用redis-cli客户端连接到Redis服务，执行slaveof命令（重启后失效）：

  ```apl
  slaveof <masterip> <masterport>
  ```

这里我们为了演示方便，使用方式二。通过redis-cli命令连接7002，执行下面命令：

```sh
# 连接 7002
redis-cli -p 7002
# 执行slaveof
slaveof 192.168.66.140 7001
```

通过redis-cli命令连接7003，执行下面命令：

```sh
# 连接 7003
redis-cli -p 7003
# 执行slaveof
slaveof 192.168.66.140 7001
```

然后连接 7001节点，查看集群状态：

```sh
# 连接 7001
redis-cli -p 7001
# 查看状态
info replication
# 结果如下
[root@192 7003]# redis-cli -p 7001
127.0.0.1:7001> info replication
# Replication
role:master
connected_slaves:2
slave0:ip=192.168.66.136,port=7002,state=online,offset=84,lag=1
slave1:ip=192.168.66.136,port=7003,state=online,offset=84,lag=1
master_failover_state:no-failover
master_replid:87dd223d1887dd3a1ece905331402f21771c22c6
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:84
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:84
```

**测试**

执行下列操作以测试：

```sh
# 连接7001端口号，执行写操作
[root@node1 redis]# redis-cli -p 7001
127.0.0.1:7001> set num 123
OK
127.0.0.1:7001> exit
# 连接7002端口号，执行读和写操作，发现写操作报错。
[root@node1 7001]# redis-cli -p 7002
127.0.0.1:7002> get num
"123"
127.0.0.1:7002> set num 666
(error) READONLY You can't write against a read only replica.
127.0.0.1:7002> 
```

可以发现，只有在7001这个master节点上可以执行写操作，7002和7003这两个slave节点只能执行读操作。

## 2.2 主从数据同步原理

这里要讲解的有全量同步、增量同步、repl_backlog。

**全量同步**

主从第一次建立连接时，会执行全量同步，将master节点的所有数据都拷贝给slave节点，流程：

<img src="..\图片\2-10【Redis分布式缓存】\2-2.png" />

master得知salve是来连接的依据有replid和offset。slave做数据同步，必须向master声明自己的replication id 和offset，master才可以判断到底需要同步哪些数据。

- **Replication Id**：简称replid，是数据集的标记，id一致则说明是同一数据集。每一个master都有唯一的replid，slave则会继承master节点的replid
- **offset**：偏移量，随着记录在repl_baklog中的数据增多而逐渐增大。slave完成同步时也会记录当前同步的offset。如果slave的offset小于master的offset，说明slave数据落后于master，需要更新。

master判断一个节点是否是第一次同步的依据，就是看replid是否一致。

slave原本是一个master，有自己的replid和offset。变成slave与master建立连接时，发送的replid和offset是自己的replid和offset。master判断发现slave发送来的replid与自己的不一致，说明这是一个全新的slave，就知道要做全量同步了。master会将自己的replid和offset都发送给这个slave，slave保存这些信息。以后slave的replid就与master一致了。

完整流程描述：

- slave节点请求增量同步
- master节点判断replid，发现不一致，拒绝增量同步
- master将完整内存数据生成RDB，发送RDB到slave
- slave清空本地数据，加载master的RDB
- master将RDB期间的命令记录在repl_baklog，并持续将log中的命令发送给slave
- slave执行接收到的命令，保持与master之间的同步

**增量同步**

全量同步需要先做RDB，然后将RDB文件通过网络传输个slave，成本太高了。因此除了第一次做全量同步，其它大多数时候slave与master都是做增量同步。

增量同步：就是只更新slave与master存在差异的部分数据。如图：

<img src="..\图片\2-10【Redis分布式缓存】\2-3.png" />

增量同步中master获取到salve和自己的数据差异依靠的是repl_backlog文件。slave提交自己的offset到master，master获取repl_baklog中从offset之后的命令给slave。

**repl_backlog**

这个文件是一个固定大小的数组，只不过数组是环形，也就是说角标到达数组末尾后，会再次从0开始读写，这样数组头部的数据就会被覆盖。

repl_baklog中会记录Redis处理过的命令日志及offset，包括master当前的offset，和slave已经拷贝到的offset。

slave与master的offset之间的差异，就是salve需要增量拷贝的数据了。随着不断有数据写入，master的offset逐渐变大，slave也不断的拷贝，追赶master的offset。直到数组被填满，如果有新的数据写入，就会覆盖数组中的旧数据（绿色的）。

<img src="..\图片\2-10【Redis分布式缓存】\2-4.png" />

但是，如果slave出现网络阻塞，导致master的offset远远超过了slave的offset，甚至已经达到尾部。这时如果master继续写入新数据，其offset就会覆盖旧的数据，直到将slave现在的offset也覆盖。

<img src="..\图片\2-10【Redis分布式缓存】\2-5.png" />

棕色框中的红色部分，就是尚未同步，但是却已经被覆盖的数据。此时如果slave恢复，需要同步，却发现自己的offset都没有了，无法完成增量同步了。只能做全量同步。

repl_baklog大小有上限，写满后会覆盖掉最早的数据。如果slave断开时间过久，导致尚未备份的数据被覆盖，则无法基于log做增量同步，只能再次全量同步。

## 2.3 主从同步优化

主从同步可以保证主从数据的一致性，非常重要。可以从以下几个方面来优化Redis主从集群：

- 在master中配置`repl-diskless-sync yes`启用无磁盘复制，避免全量同步时的磁盘IO。
- Redis单节点上的内存占用不要太大，减少RDB导致的过多磁盘IO。
- 适当提高repl_baklog的大小，发现slave宕机时尽快实现故障恢复，尽可能避免全量同步。
- 限制一个master上的slave节点数量，如果实在是太多slave，则可以采用主-从-从链式结构，减少master压力。

<img src="..\图片\2-10【Redis分布式缓存】\2-6.png" />



# 第三章 哨兵Sentinel

<!-- Sentinel 哨兵 -->

Redis提供了哨兵（Sentinel）机制来实现主从集群的自动故障恢复。

哨兵的作用如下：监控、故障转移、通知。

- 监控：Sentinel 会不断检查master和slave是否按预期工作。
- 故障转移：也叫做自动故障恢复。如果master故障，Sentinel会将一个slave提升为master。当故障恢复后也以新的master为主。
- 通知：Sentinel充当Redis客户端的服务发现来源，当集群发生故障转移时，会将最新信息推送给Redis的客户端

哨兵的结构如图：

<img src="..\图片\2-10【Redis分布式缓存】\3-1.png" />

## 3.1 哨兵原理

**监控**

Sentinel基于心跳机制监测服务状态，每隔1秒向集群的每个实例发送ping命令来判断该节点是否宕机：

- 主观下线：如果某sentinel节点发现某实例未在规定时间响应，则认为该实例主观下线。

- 客观下线：若超过指定数量（quorum）的sentinel都认为该实例主观下线，则该实例客观下线。quorum值最好超过Sentinel实例数量的一半。

**故障转移**

一旦发现master故障，sentinel需要在salve中选择一个作为新的master，选择依据是这样的：

- 首先会判断slave节点与master节点断开时间长短，如果超过指定值则会排除该slave节点
- 然后判断slave节点的slave-priority值，越小优先级越高，如果是0则永不参与选举。默认都为1。
- 如果slave-prority一样，则判断slave节点的offset值，越大说明数据越新，优先级越高
- 最后是判断slave节点的运行id大小，越小优先级越高。

当选出一个新的master后，就该进行从节点向主节点的切换了：

- sentinel给备选的slave1节点发送`slaveof no one`命令，让该节点成为master
- sentinel给所有其它slave发送`slaveof IP地址 晋升主节点的端口号` 命令，让这些slave成为新master的从节点，开始从新的master上同步数据。
- 最后，sentinel将故障节点标记为slave，当故障节点恢复后会自动成为新的master的slave节点

## 3.2 搭建哨兵集群

这里我们搭建一个三节点形成的Sentinel集群，来监管之前的Redis主从集群。三个sentinel实例信息如下：

| 节点 |       IP       | PORT  |
| ---- | :------------: | :---: |
| s1   | 192.168.66.136 | 27001 |
| s2   | 192.168.66.136 | 27002 |
| s3   | 192.168.66.136 | 27003 |

**前提**

哨兵是用来监控redis的，所以需要将redis-server启动，三个都启动。启动命令：

```sh
# 第1个
redis-server /usr/local/redis/7001/redis.conf
# 第2个
redis-server /usr/local/redis/7002/redis.conf
# 第3个
redis-server /usr/local/redis/7003/redis.conf
```

如果要一键停止，可以运行下面命令：

```sh
printf '%s\n' 7001 7002 7003 | xargs -I{} -t redis-cli -p {} shutdown
```

**准备实例和配置**

要在同一台虚拟机开启3个实例，必须准备三份不同的配置文件和目录，配置文件所在目录也就是工作目录。我们创建三个文件夹，名字分别叫s1、s2、s3：

```sh
# 进入/usr/local/redis目录
cd /usr/local/redis
# 创建目录
mkdir s1 s2 s3
```

然后在s1目录创建一个`sentinel.conf`文件，添加下面的内容：

```properties
# 当前sentinel实例的端口
port 27001
# 监控的IP
sentinel announce-ip 192.168.66.140
# 指定主节点信息。mymaster为主节点信息，自定义。2为选举master时的quorum值
# 若超过quorum的sentinel都认为一个节点主观下线，则该实例客观下线。quorum值最好超过Sentinel实例数量的一半。
sentinel monitor mymaster 192.168.66.140 7001 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 60000
dir "/usr/local/redis/s1"
```

然后将`sentinel.conf`文件拷贝到s2、s3两个目录中：

```sh
# 方式一：逐个拷贝
cp s1/sentinel.conf s2
cp s1/sentinel.conf s3
# 方式二：管道组合命令，一键拷贝
echo s2 s3 | xargs -t -n 1 cp s1/sentinel.conf
```

修改s2、s3两个文件夹内的配置文件，将端口分别修改为27002、27003：

```sh
sed -i -e 's/27001/27002/g' -e 's/s1/s2/g' s2/sentinel.conf
sed -i -e 's/27001/27003/g' -e 's/s1/s3/g' s3/sentinel.conf
```

**启动**

为了方便查看日志，打开3个ssh窗口，分别启动3个Redis实例，启动命令：

```sh
# 第1个
redis-sentinel s1/sentinel.conf
# 第2个
redis-sentinel s2/sentinel.conf
# 第3个
redis-sentinel s3/sentinel.conf
# 这里不能够一键启动三个哨兵服务，除非设置取消霸屏操作，否则打印下面语句就会直接开启一个哨兵实例，无法打开其他的哨兵实例。
printf '%s\n' s1 s2 s3 | xargs -I{} -t redis-sentinel {}\/sentinel.conf
```

**关闭**

```sh
[root@node1 redis]# redis-cli -p 27001 shutdown
[root@node1 redis]# redis-cli -p 27002 shutdown
[root@node1 redis]# redis-cli -p 27003 shutdown
```

```sh
# 一键关闭三个哨兵服务
printf '%s\n' 27001 27002 27003 | xargs -I{} -t redis-cli -p {} shutdown
```

**测试**

尝试让master节点7001宕机，

```sh
[root@node1 redis]# redis-cli -p 7001 shutdown
[root@node1 redis]# ps -ef | grep redis
root       1649      1  0 09:52 ?        00:00:01 redis-server 0.0.0.0:7002
root       1660      1  0 09:52 ?        00:00:01 redis-server 0.0.0.0:7003
root       1677   1587  0 09:53 pts/1    00:00:01 redis-sentinel *:27001 [sentinel]
root       1759   1682  0 09:54 pts/2    00:00:01 redis-sentinel *:27002 [sentinel]
root       1764   1720  0 09:54 pts/3    00:00:01 redis-sentinel *:27003 [sentinel]
root       1790   1398  0 09:57 pts/0    00:00:00 grep --color=auto redis
```

查看sentinel日志：

```sh
# sdown：主观认为端口为7001的节点已经下线
1764:X  * +sdown master mymaster 192.168.88.151 7001
# odown，客观认为端口为7001节点下面，因为quorum 3/2 已经达标，实锤了，就是下线了。
1764:X  * +odown master mymaster 192.168.88.151 7001 #quorum 2/2
# 尝试去等待一会端口为7001的节点
1764:X  * +try-failover master mymaster 192.168.88.151 7001
# Sentinel内部选举一个领导者（vote-for-leader投票选举一个领导者）去进行故障切换
1764:X  * +vote-for-leader 8b0a65b211539d7a7bb224597630cdc0cdc8b5c4 1

# 准备挑选一个slave节点作为新的master节点
1764:X  * +failover-state-select-slave master mymaster 192.168.88.151 7001
# 挑选了7003这个节点作为master节点
1764:X  * +selected-slave slave  192.168.88.151 7003 @ mymaster 192.168.88.151 7001

# 7003节点执行slave none，成为新的master
1764:X  * +failover-state-send-slaveof-noone slave 192.168.88.151 7003 @ mymaster 192.168.88.151 7001
# 7003节点等待提升，在这期间让其他的slave节点执行slave of 192.168.88.151 7003。
1764:X  * +failover-state-wait-promotion slave 192.168.88.151 7003 @ mymaster 192.168.88.151 7001
# 7003节点正式提升为master
1764:X  * +promoted-slave slave 192.168.88.151 7003 @ mymaster 192.168.88.151 7001

# 修改下线的7001节点的配置文件，将其标记为7003节点的slave
1764:X  * +failover-state-reconf-slaves master mymaster 192.168.88.151 7001
# 修改7002节点的配置文件，将其标记为7003节点的slave
1764:X  * +slave-reconf-sent slave 192.168.88.151 7002 @ mymaster 192.168.88.151 7001
1764:X  * +slave-reconf-inprog slave 192.168.88.151 7002 @ mymaster 192.168.88.151 7001
1764:X  * +slave-reconf-done slave 192.168.88.151 7002 @ mymaster 192.168.88.151 7001

# 事件处理完毕
1764:X  * +failover-end master mymaster 192.168.88.151 7001
# 主从节点切换完成，7003节点变为主节点，7001节点变为从节点
1764:X  * +switch-master mymaster 192.168.88.151 7001 192.168.88.151 7003
```

## 3.3 RedisTemplate

在Sentinel集群监管下的Redis主从集群，其节点会因为自动故障转移而发生变化，Redis的客户端必须感知这种变化，及时更新连接信息。Spring的RedisTemplate底层利用lettuce实现了节点的感知和自动切换。

下面通过一个SpringBoot项目来实现RedisTemplate集成哨兵机制。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-Redis</artifactId>
</dependency>
```

```yaml
spring:
  # 在配置文件application.yml中指定Redis的sentinel相关信息，不需要配置redis和jedis的其他信息。直接复制即可
  redis:
    sentinel:
      master: mymaster
      nodes:
        - 192.168.150.101:27001
        - 192.168.150.101:27002
        - 192.168.150.101:27003
```

```java
// 在项目的启动类中，添加一个新的bean配置读写分离策略
@Bean
public LettuceClientConfigurationBuilderCustomizer clientConfigurationBuilderCustomizer(){
    return clientConfigurationBuilder -> clientConfigurationBuilder.readFrom(ReadFrom.REPLICA_PREFERRED);
}
```

这个bean中配置的就是读写策略，包括四种：

- `MASTER`：从主节点读取
- `MASTER_PREFERRED`：优先从master节点读取，master不可用才读取replica
- `REPLICA`：从slave（replica）节点读取
- `REPLICA_PREFERRED`：优先从slave（replica）节点读取，所有的slave都不可用才读取master

# 第四章 Redis分片集群

主从和哨兵可以解决高可用、高并发读的问题。但是依然有两个问题没有解决：海量数据存储问题、高并发写的问题。

使用分片集群可以解决上述问题。分片集群特征：

- 集群中有多个master，每个master保存不同数据

- 每个master都可以有多个slave节点

- master之间通过ping监测彼此健康状态

- 客户端请求可以访问集群任意节点，最终都会被转发到正确节点

<img src="..\图片\2-10【Redis分布式缓存】/4-1.png" />

## 4.1.搭建分片集群

分片集群需要的节点数量较多，这里我们搭建一个最小的分片集群，包含3个master节点，每个master包含一个slave节点。

这里我们会在同一台虚拟机中开启6个Redis实例，模拟分片集群，信息如下：

|       IP       | PORT |  角色  |
| :------------: | :--: | :----: |
| 192.168.66.140 | 7001 | master |
| 192.168.66.140 | 7002 | master |
| 192.168.66.140 | 7003 | master |
| 192.168.66.140 | 8001 | slave  |
| 192.168.66.140 | 8002 | slave  |
| 192.168.66.140 | 8003 | slave  |



### 4.1.2 准备实例和配置

删除之前的7001、7002、7003这几个目录，重新创建出7001、7002、7003、8001、8002、8003目录：

```sh
# 进入/usr/local/redis目录
cd /usr/local/redis
# 删除旧的，避免配置干扰
rm -rf 7001 7002 7003
# 创建目录
mkdir 7001 7002 7003 8001 8002 8003
```

在/usr/local/redis下准备一个新的redis.conf文件，内容如下：

```ini
port 6379
# 开启集群功能
cluster-enabled yes
# 集群的配置文件名称，不需要我们创建，由Redis自己维护
cluster-config-file /usr/local/redis/6379/nodes.conf
# 节点心跳失败的超时时间
cluster-node-timeout 5000
# 持久化文件存放目录
dir /usr/local/redis/6379
# 绑定地址
bind 0.0.0.0
# 让Redis后台运行
daemonize yes
# 注册的实例ip
replica-announce-ip 192.168.66.140
# 保护模式
protected-mode no
# 数据库数量
databases 1
# 日志
logfile /usr/local/redis/6379/run.log
```

将这个文件拷贝到每个目录下：

```sh
# 进入/usr/local/redis目录
cd /usr/local/redis
# 执行拷贝
echo 7001 7002 7003 8001 8002 8003 | xargs -t -n 1 cp redis.conf
```

修改每个目录下的redis.conf，将其中的6379修改为与所在目录一致：

```sh
# 进入/usr/local/redis目录
cd /usr/local/redis
# 修改配置文件
printf '%s\n' 7001 7002 7003 8001 8002 8003 | xargs -I{} -t sed -i 's/6379/{}/g' {}/redis.conf
```



### 4.1.3 启动

因为已经配置了后台启动模式，所以可以直接启动服务：

```sh
# 进入/usr/local/redis目录
cd /usr/local/redis
# 一键启动所有服务
printf '%s\n' 7001 7002 7003 8001 8002 8003 | xargs -I{} -t redis-server {}/redis.conf
```

通过ps查看状态：

```sh
ps -ef | grep Redis
```

发现服务都已经正常启动：

```sh
[root@192 Redis]# ps -ef | grep Redis
root       8469      1  0 10:03 ?        00:00:00 redis-server 0.0.0.0:7001 [cluster]
root       8471      1  0 10:03 ?        00:00:00 redis-server 0.0.0.0:7002 [cluster]
root       8473      1  0 10:03 ?        00:00:00 redis-server 0.0.0.0:7003 [cluster]
root       8483      1  0 10:03 ?        00:00:00 redis-server 0.0.0.0:8001 [cluster]
root       8489      1  0 10:03 ?        00:00:00 redis-server 0.0.0.0:8002 [cluster]
root       8491      1  0 10:03 ?        00:00:00 redis-server 0.0.0.0:8003 [cluster]
root       8505   7979  0 10:03 pts/3    00:00:00 grep --color=auto Redis
```

如果要关闭所有进程，可以执行命令：

```sh
ps -ef | grep Redis | awk '{print $2}' | xargs kill
```

或者（推荐这种方式）：

```sh
printf '%s\n' 7001 7002 7003 8001 8002 8003 | xargs -I{} -t redis-cli -p {} shutdown
```



### 4.1.4 创建集群

虽然服务启动了，但是目前每个服务之间都是独立的，没有任何关联。

我们需要执行命令来创建集群，在Redis5.0之前创建集群比较麻烦，5.0之后集群管理命令都集成到了redis-cli中。



1）Redis5.0之前

Redis5.0之前集群命令都是用Redis安装包下的src/Redis-trib.rb来实现的。因为Redis-trib.rb是有ruby语言编写的所以需要安装ruby环境。

 ```sh
# 安装依赖
yum -y install zlib ruby rubygems
gem install Redis
 ```



然后通过命令来管理集群：

```sh
# 进入Redis的src目录
cd /tmp/redis-6.2.4/src
# 创建集群
./Redis-trib.rb create --replicas 1 192.168.150.101:7001 192.168.150.101:7002 192.168.150.101:7003 192.168.150.101:8001 192.168.150.101:8002 192.168.150.101:8003
```



2）Redis5.0以后

我们使用的是Redis6.2.4版本，集群管理以及集成到了redis-cli中，格式如下：

```sh
redis-cli --cluster create --cluster-replicas 1 192.168.66.140:7001 192.168.66.140:7002 192.168.66.140:7003 192.168.66.140:8001 192.168.66.140:8002 192.168.66.140:8003
```

命令说明：

- `redis-cli --cluster`或者`./Redis-trib.rb`：代表集群操作命令
- `create`：代表是创建集群
- `--replicas 1`或者`--cluster-replicas 1` ：指定集群中每个master的副本个数为1，此时`节点总数 ÷ (replicas + 1)` 得到的就是master的数量。因此节点列表中的前n个就是master，其它节点都是slave节点，随机分配到不同master



运行后的样子：



这里输入yes，则集群开始创建：



通过命令可以查看集群状态：

```sh
redis-cli -p 7001 cluster nodes
```





### 4.1.5 测试

尝试连接7001节点，存储一个数据：

```sh
# 连接
redis-cli -p 7001
# 存储数据
set num 123
# 读取数据
get num
# 再次存储
set a 1
```

结果悲剧了：



集群操作时，需要给`redis-cli`加上`-c`参数才可以：

```sh
redis-cli -c -p 7001
```

这次可以了：





## 4.2 散列插槽

### 4.2.1 插槽原理

Redis会把每一个master节点映射到0~16383共16384个插槽（hash slot）上，查看集群信息时就能看到：

```sh
redis-cli -p 7001 cluster nodes
```

<img src="..\图片\2-10【Redis分布式缓存】/image-20210725155820320.png" />



数据key不是与节点绑定，而是与插槽绑定。Redis会根据key的有效部分计算插槽值，分两种情况：

- key中包含"{}"，且“{}”中至少包含1个字符，“{}”中的部分是有效部分
- key中不包含“{}”，整个key都是有效部分



例如：key是num，那么就根据num计算，如果是{itcast}num，则根据itcast计算。计算方式是利用CRC16算法得到一个hash值，然后对16384取余，得到的结果就是slot值。

```sh
[root@192 Redis]# redis-cli -c -p 7001
127.0.0.1:7001> set a 1
-> Redirected to slot [15495] located at 192.168.66.140:7003
OK
192.168.66.140:7003> get a
"1"
```

 在7001这个节点执行set a 1时，对a做hash运算，对16384取余，得到的结果是15495，因此要存储到103节点。

到了7003后，执行`get num`时，对num做hash运算，对16384取余，得到的结果是2765，因此需要切换到7001节点



### 4.2.2 小结

Redis如何判断某个key应该在哪个实例？

- 将16384个插槽分配到不同的实例
- 根据key的有效部分计算哈希值，对16384取余
- 余数作为插槽，寻找插槽所在实例即可

如何将同一类数据固定的保存在同一个Redis实例？

- 这一类数据使用相同的有效部分，例如key都以{typeId}为前缀



## 4.3 集群伸缩

redis-cli --cluster提供了很多操作集群的命令，可以通过下面方式查看：

```sh
redis-cli --cluster help
```

比如，添加节点的命令：

```sh
  add-node       new_host:new_port existing_host:existing_port
                 --cluster-slave
                 --cluster-master-id <arg>
```



### 4.3.1 需求分析

需求：向集群中添加一个新的master节点，并向其中存储 num = 10

- 启动一个新的Redis实例，端口为7004
- 添加7004到之前的集群，并作为一个master节点
- 给7004节点分配插槽，使得num这个key可以存储到7004实例

这里需要两个新的功能：

- 添加一个节点到集群中
- 将部分插槽分配到新插槽



### 4.3.2 创建新的Redis实例

创建一个文件夹：

```sh
mkdir 7004
```

拷贝配置文件：

```sh
 cp redis.conf 7004/
```

修改配置文件：

```sh
sed -i s/6379/7004/g 7004/redis.conf
```

启动

```sh
redis-server 7004/redis.conf
```



### 4.3.3 添加新节点到Redis

添加节点的语法如下：

```sh
  add-node       new_host:new_port existing_host:existing_port
                 --cluster-slave
                 --cluster-master-id <arg>
```

执行命令：

```sh
redis-cli --cluster add-node  192.168.66.140:7004 192.168.66.140:7001
```



通过命令查看集群状态：

```sh
redis-cli -p 7001 cluster nodes
```

如图，7004加入了集群，并且默认是一个master节点：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210725161007099.png" />

但是，可以看到7004节点的插槽数量为0，因此没有任何数据可以存储到7004上



### 4.3.4 转移插槽

我们要将num存储到7004节点，因此需要先看看num的插槽是多少：

```sh
192.168.66.140:7003> get num
-> Redirected to slot [2765] located at 192.168.66.140:7001     # num的插槽为2765
"13"
```

我们可以将0~3000的插槽从7001转移到7004，命令格式如下：

```sh
[root@192 Redis]# redis-cli --cluster help
Cluster Manager Commands:  
  reshard        host:port
                 --cluster-from <arg>
                 --cluster-to <arg>
                 --cluster-slots <arg>
                 --cluster-yes
                 --cluster-timeout <arg>
                 --cluster-pipeline <arg>
                 --cluster-replace
```



具体命令如下：

建立连接：

```sh
[root@192 Redis]# redis-cli --cluster reshard 192.168.66.140:7001
```

得到下面的反馈：

```sh
[root@192 Redis]# redis-cli --cluster reshard 192.168.66.140:7001
>>> Performing Cluster Check (using node 192.168.66.140:7001)
M: 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543 192.168.66.140:7001
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: b6d881c8009cd18da7c6147a604d5698fc743cff 192.168.66.140:8002
   slots: (0 slots) slave
   replicates 87b91279db622a336e00a17b48d23654f64300e6
M: 5dc3725236630b1414ef61900863d1ee5c28d5ba 192.168.66.140:7002
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: 2813c684e539dcff774ed85b4cbe321b2a726555 192.168.66.140:8003
   slots: (0 slots) slave
   replicates 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
M: 87b91279db622a336e00a17b48d23654f64300e6 192.168.66.140:7003
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
M: e3e67d2b444cdf99253a054bf5b480875b52d3db 192.168.66.140:7004        # 7004的ID，下面有用
   slots: (0 slots) master
S: 8b38959e7810c90ae71c30175e1f9f665ce14aa7 192.168.66.140:8001
   slots: (0 slots) slave
   replicates 5dc3725236630b1414ef61900863d1ee5c28d5ba
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
How many slots do you want to move (from 1 to 16384)? 3000
What is the receiving node ID?       # 询问要移动多少个插槽，我们计划是3000个
```

新的问题来了：

```sh
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
How many slots do you want to move (from 1 to 16384)? 3000
What is the receiving node ID?                             # 哪个node来接收这些插槽？？
```



显然是7004，那么7004节点的id是多少呢？上面已经在屏幕输出了。

复制这个id，然后拷贝到刚才的控制台后：

```sh
How many slots do you want to move (from 1 to 16384)? 3000
What is the receiving node ID? e3e67d2b444cdf99253a054bf5b480875b52d3db   # 拷贝7004ID
Please enter all the source node IDs.
  Type 'all' to use all the nodes as source nodes for the hash slots.
  Type 'done' once you entered all the source nodes IDs.
Source node #1:                                      # 这里询问，你的插槽是从哪里移动过来的？
```

这里询问，你的插槽是从哪里移动过来的？

- all：代表全部，也就是三个节点各转移一部分
- 具体的id：目标节点的id
- done：没有了

这里我们要从7001获取，因此填写7001的id：

```sh
How many slots do you want to move (from 1 to 16384)? 3000
What is the receiving node ID? e3e67d2b444cdf99253a054bf5b480875b52d3db
Please enter all the source node IDs.
  Type 'all' to use all the nodes as source nodes for the hash slots.
  Type 'done' once you entered all the source nodes IDs.
Source node #1: 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543   #　这里我们要从7001获取，因此填写7001的id
Source node #2: done
```



填完后，点击done，这样插槽转移就准备好了：

```sh
    ...
    Moving slot 2992 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
    Moving slot 2993 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
    Moving slot 2994 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
    Moving slot 2995 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
    Moving slot 2996 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
    Moving slot 2997 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
    Moving slot 2998 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
    Moving slot 2999 from 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543
Do you want to proceed with the proposed reshard plan (yes/no)?       # 确认要转移吗？输入yes：
```



然后，通过命令查看结果，可以看到： 

```sh
[root@192 Redis]# redis-cli -p 7001 cluster nodes
b6d881c8009cd18da7c6147a604d5698fc743cff 192.168.66.140:8002@18002 slave 87b91279db622a336e00a17b48d23654f64300e6 0 1659253992711 3 connected
5dc3725236630b1414ef61900863d1ee5c28d5ba 192.168.66.140:7002@17002 master - 0 1659253992000 2 connected 5461-10922
2813c684e539dcff774ed85b4cbe321b2a726555 192.168.66.140:8003@18003 slave 5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543 0 1659253991171 1 connected
87b91279db622a336e00a17b48d23654f64300e6 192.168.66.140:7003@17003 master - 0 1659253991581 3 connected 10923-16383
5cd7dc96b94df08e4e504c8e15d5c2c2d2a3f543 192.168.66.140:7001@17001 myself,master - 0 1659253991000 1 connected 3000-5460
e3e67d2b444cdf99253a054bf5b480875b52d3db 192.168.66.140:7004@17004 master - 0 1659253992404 7 connected 0-2999       # 可以看到已经有插槽了
8b38959e7810c90ae71c30175e1f9f665ce14aa7 192.168.66.140:8001@18001 slave 5dc3725236630b1414ef61900863d1ee5c28d5ba 0 1659253991375 2 connected
```

目的达成。



## 4.4 故障转移

集群初识状态是这样的：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210727161152065.png" />

其中7001、7002、7003都是master，我们计划让7002宕机。



### 4.4.1 自动故障转移

当集群中有一个master宕机会发生什么呢？

直接停止一个Redis实例，例如7002：

```sh
redis-cli -p 7002 shutdown
```



1）首先是该实例与其它实例失去连接

2）然后是疑似宕机：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210725162319490.png" />

3）最后是确定下线，自动提升一个slave为新的master：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210725162408979.png" />

4）当7002再次启动，就会变为一个slave节点了：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210727160803386.png" />



### 4.4.2 手动故障转移

利用cluster failover命令可以手动让集群中的某个master宕机，切换到执行cluster failover命令的这个slave节点，实现无感知的数据迁移。其流程如下：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210725162441407.png" />



这种failover命令可以指定三种模式：

- 缺省：默认的流程，如图1~6歩
- force：省略了对offset的一致性校验
- takeover：直接执行第5歩，忽略数据一致性、忽略master状态和其它master的意见



**案例需求**：在7002这个slave节点执行手动故障转移，重新夺回master地位

步骤如下：

1）利用redis-cli连接7002这个节点

2）执行cluster failover命令

如图：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210727160037766.png" />



效果：

<img src="..\图片\2-10【Redis分布式缓存】/image-20210727161152065.png" />



## 4.5 RedisTemplate访问分片集群

RedisTemplate底层同样基于lettuce实现了分片集群的支持，而使用的步骤与哨兵模式基本一致：

1）引入Redis的starter依赖

2）配置分片集群地址

3）配置读写分离

与哨兵模式相比，其中只有分片集群的配置方式略有差异，如下：

```yaml
spring:
  Redis:
    cluster:
      nodes:
        - 192.168.66.140:7001
        - 192.168.66.140:7002
        - 192.168.66.140:7003
        - 192.168.66.140:8001
        - 192.168.66.140:8002
        - 192.168.66.140:8003
```





