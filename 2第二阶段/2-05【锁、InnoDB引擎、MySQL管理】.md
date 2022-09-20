# 第七章 锁

锁是计算机协调多个进程或线程并发访问某一资源的机制。在数据库中，除传统的计算资源（CPU、RAM、I/O）的争用以外，数据也是一种供许多用户共享的资源。如何保证数据并发访问的一致性、有效性是所有数据库必须解决的一个问题，锁冲突也是影响数据库并发访问性能的一个重要因素。从这个角度来说，锁对数据库而言显得尤其重要，也更加复杂。

MySQL中的锁，按照锁的粒度分，分为以下三类： 

- 全局锁：锁定数据库中的所有表。 
- 表级锁：每次操作锁住整张表。 
- 行级锁：每次操作锁住对应的行数据。

## 7.1 全局锁

全局锁就是对整个数据库实例加锁，加锁后整个实例就处于只读状态，后续的DML的写语句，DDL语句，已经更新操作的事务提交语句都将被阻塞。

其典型的使用场景是做全库的逻辑备份，对所有的表进行锁定，从而获取一致性视图，保证数据的完整性。

![](..\图片\2-01【MySQL】\16.png)

```sql
-- 数据备份步骤如下：
flush tables with read lock ;	-- 加上全局锁
mysqldump -uroot –p1234 linxuan > D:/linxuan.sql   -- 数据备份，在shell窗口就可以 linxuan是数据库名称
unlock tables ;					-- 释放锁
```

数据库中加全局锁，是一个比较重的操作，存在以下问题：

- 如果在主库上备份，那么在备份期间都不能执行更新，业务基本上就得停摆。 
- 如果在从库上备份，那么在备份期间从库不能执行主库同步过来的二进制日志（binlog），会导致主从延迟。

在InnoDB引擎中，我们可以在备份时加上参数 --single-transaction 参数来完成不加锁的一致 性数据备份。

```sql
mysqldump --single-transaction -uroot –p123456 linxuan > D:/linxuan.sql
```



## 7.2 表级锁

表级锁，每次操作锁住整张表。锁定粒度大，发生锁冲突的概率最高，并发度最低。应用在MyISAM、InnoDB、BDB等存储引擎中。

对于表级锁，主要分为以下三类： 

- 表锁 
- 元数据锁（meta data lock，MDL） 
- 意向锁

### 7.2.1 表锁

对于表锁，分为两类： 

- 表共享读锁（read lock） 
- 表独占写锁（write lock）

```sql
-- 语法如下
加锁：lock tables 表名... read/write。	-- lock tables student read;
释放锁：unlock tables / 客户端断开连接 。	-- unlock tables;
```

表共享读锁： 读锁不会阻塞其他客户端的读，但是会阻塞写。

![](..\图片\2-01【MySQL】\17-1.png)

表独占写锁：写锁既会阻塞其他客户端的读，又会阻塞其他客户端的写。

![](..\图片\3-day01 【MySQL基础】\17-2.png)

 读锁不会阻塞其他客户端的读，但是会阻塞写。写锁既会阻塞其他客户端的读，又会阻塞其他客户端的写。

### 7.2.2 元数据锁

meta data lock , 元数据锁，简写MDL。

MDL加锁过程是系统自动控制，无需显式使用，在访问一张表的时候会自动加上。MDL锁主要作用是维护表元数据的数据一致性，在表上有活动事务的时候，不可以对元数据进行写入操作。为了避免DML与DDL冲突，保证读写的正确性。

这里的元数据，可以简单理解为就是一张表的表结构。 也就是说，某一张表涉及到未提交的事务时，是不能够修改这张表的表结构的。

在MySQL5.5中引入了MDL，当对一张表进行增删改查的时候，加MDL读锁(共享)；当对表结构进行变更操作的时候，加MDL写锁(排他)。

常见的SQL操作时，所添加的元数据锁：

* `lock tables xxx read / write`：SHARED_READ_ONLY / SHARED_NO_READ_WRITE
* `select 、select ... lock in share mode`：SHARED_READ
* `insert 、update、 delete、select ... for update`：SHARED_WRITE
* `alter table ...` ：EXCLUSIVE 

SHARED_READ和SHARED_WRITE与SHARED_READ、 SHARED_WRITE兼容，与 EXCLUSIVE互斥。

```sql
-- 查看数据库中的元数据锁的情况
select object_type,object_schema,object_name,lock_type,lock_duration from performance_schema.metadata_locks ;
```

演示如下：

* 当执行SELECT、INSERT、UPDATE、DELETE等语句时，添加的是元数据共享锁（SHARED_READ / SHARED_WRITE），之间是兼容的。
* 当执行SELECT语句时，添加的是元数据共享锁（SHARED_READ），会阻塞元数据排他锁 （EXCLUSIVE），之间是互斥的。

### 7.2.3 意向锁

为了避免DML在执行时，加的行锁与表锁的冲突，在InnoDB中引入了意向锁，使得表锁不用检查每行数据是否加锁，使用意向锁来减少表锁的检查。

假如没有意向锁，客户端一开启一个事务，然后执行DML操作，在执行DML语句时，会对涉及到的行加行锁。当客户端二想对这张表加表锁时，会检查当前表是否有对应的行锁，如果没有，则添加表锁，此时就会从第一行数据，检查到最后一行数据，效率较低。

![](..\图片\2-01【MySQL】\17-3.png)

有了意向锁之后，客户端一在执行DML操作时，会对涉及的行加行锁，同时也会对该表加上意向锁。而其他客户端，在对这张表加表锁的时候，会根据该表上所加的意向锁来判定是否可以成功加表锁，而不用逐行判断行锁情况了。

- 意向共享锁(IS): 由语句select ... lock in share mode添加 。 与表锁共享锁 (read)兼容，与表锁排他锁(write)互斥。 
- 意向排他锁(IX): 由insert、update、delete、select...for update添加 。与表锁共享锁(read)及排他锁(write)都互斥，意向锁之间不会互斥。

一旦事务提交了，意向共享锁、意向排他锁，都会自动释放。

```sql
-- 查看意向锁及行锁的加锁情况
select object_schema,object_name,index_name,lock_type,lock_mode,lock_data from performance_schema.data_locks;
```



我们来看一下：

首先开启两个客户端。

```sql
-- 客户端1打开事务，并添加意向共享锁
mysql>  begin;
mysql>  select * from student where id  = 1 lock in share mode;
+----+--------+------------+
| id | name   | no         |
+----+--------+------------+
|  1 | 黛绮丝 | 2000100102 |
+----+--------+------------+
1 row in set (0.00 sec)
```

这个时候就能够在客户端2查看意向共享锁的情况了：

```sql
-- 客户端2
mysql>  select object_schema,object_name,index_name,lock_type,lock_mode,lock_data from performance_schema.data_locks;
+---------------+-------------+------------+-----------+---------------+-----------+
| object_schema | object_name | index_name | lock_type | lock_mode     | lock_data |
+---------------+-------------+------------+-----------+---------------+-----------+
| linxuan       | student     | NULL       | TABLE     | IS            | NULL      |
| linxuan       | student     | PRIMARY    | RECORD    | S,REC_NOT_GAP | 1         |
+---------------+-------------+------------+-----------+---------------+-----------+
2 rows in set (0.00 sec)

-- lock_type:锁的类型		TABLE:表锁		 RECORD:行锁
-- lock_mode:锁的类型		IS:意向共享锁
```

而这个时候在客户端2为表加共享读锁是没有任何问题的，但是加上表独占写锁就有问题了，就会被阻塞。

```sql
mysql> lock tables student read;		-- 表共享读锁 
Query OK, 0 rows affected (0.00 sec)

mysql> lock tables student write; -- 表独占写锁 阻塞
```

## 7.4 行级锁

行级锁，每次操作锁住对应的行数据。锁定粒度最小，发生锁冲突的概率最低，并发度最高。应用在InnoDB存储引擎中。

InnoDB的数据是基于索引组织的，行锁是通过对索引上的索引项加锁来实现的，而不是对记录加的锁。对于行级锁，主要分为以下三类：

* 行锁（Record Lock）：锁定单个行记录的锁，防止其他事务对此行进行update和delete。在RC(`read committed`)、RR(`repeatable read`)隔离级别下都支持。

  ![](..\图片\2-01【MySQL】\18-1.png)

* 间隙锁（Gap Lock）：锁定索引记录间隙（不含该记录），确保索引记录间隙不变，防止其他事务在这个间隙进行insert，产生幻读。在RR隔离级别下都支持。

  ![](..\图片\2-01【MySQL】\18-2.png)

* 临键锁（Next-Key Lock）：行锁和间隙锁组合，同时锁住数据，并锁住数据前面的间隙Gap。在RR隔离级别下支持。

  ![](..\图片\2-01【MySQL】\18-3.png)

### 7.4.1 行锁

InnoDB实现了以下两种类型的行锁：

- 共享锁（S）：允许一个事务去读一行，阻止其他事务获得相同数据集的排它锁。
- 排他锁（X）：允许获取排他锁的事务更新数据，阻止其他事务获得相同数据集的共享锁和排他锁。

两种行锁的兼容情况如下:

| 共享锁和共享锁 | 共享锁和排他锁 | 排他锁和排他锁 |
| -------------- | -------------- | -------------- |
| 兼容           | 冲突           | 冲突           |

常见的SQL语句，在执行时，所加的行锁如下：

| SQL                           | 行锁类型   | 说明                                     |
| ----------------------------- | ---------- | ---------------------------------------- |
| INSERT / UPDATE /DELETE       | 排他锁     | 自动加锁                                 |
| SELECT（正常）                | 不加任何锁 |                                          |
| SELECT ... LOCK IN SHARE MODE | 共享锁     | 需要手动在SELECT之后加LOCK IN SHARE MODE |
| SELECT ... FOR UPDATE         | 排他锁     | 需要手动在SELECT之后加FOR UPDATE         |

默认情况下，InnoDB在 REPEATABLE READ事务隔离级别运行，InnoDB使用 next-key 锁进行搜索和索引扫描，以防止幻读。

- 针对唯一索引进行检索时，对已存在的记录进行等值匹配时，将会自动优化为行锁。
- InnoDB的行锁是针对于索引加的锁，不通过索引条件检索数据，那么InnoDB将对表中的所有记 录加锁，此时 就会升级为表锁。

```sql
-- 查看意向锁及行锁的加锁情况
select object_schema,object_name,index_name,lock_type,lock_mode,lock_data from
performance_schema.data_locks;
```

### 7.4.2 间隙锁&临键锁

默认情况下，InnoDB在 REPEATABLE READ事务隔离级别运行，InnoDB使用 next-key 锁进行搜索和索引扫描，以防止幻读。

- 索引上的等值查询(唯一索引)，给不存在的记录加锁时, 优化为间隙锁 。 
- 索引上的等值查询(非唯一普通索引)，向右遍历时最后一个值不满足查询需求时，next-key lock 退化为间隙锁。
- 索引上的范围查询(唯一索引)--会访问到不满足条件的第一个值为止。

注意：间隙锁唯一目的是防止其他事务插入间隙。间隙锁可以共存，一个事务采用的间隙锁不会阻止另一个事务在同一间隙上采用间隙锁。

# 第八章 InnoDB引擎

## 8.1 逻辑存储结构

InnoDB的逻辑存储结构如下图所示：

![](..\图片\2-01【MySQL】\11.png)

1). 表空间

表空间是InnoDB存储引擎逻辑结构的最高层， 如果用户启用了参数 innodb_file_per_table(在8.0版本中默认开启) ，则每张表都会有一个表空间（xxx.ibd），一个mysql实例可以对应多个表空间，用于存储记录、索引等数据。

2). 段

段，分为数据段（Leaf node segment）、索引段（Non-leaf node segment）、回滚段（Rollback segment），InnoDB是索引组织表，数据段就是B+树的叶子节点， 索引段即为B+树的非叶子节点。段用来管理多个Extent（区）。

3). 区

区，表空间的单元结构，每个区的大小为1M。 默认情况下， InnoDB存储引擎页大小为16K， 即一个区中一共有64个连续的页。

4). 页

页，是InnoDB 存储引擎磁盘管理的最小单元，每个页的大小默认为 16KB。为了保证页的连续性，InnoDB 存储引擎每次从磁盘申请 4-5 个区。

5). 行

行，InnoDB 存储引擎数据是按行进行存放的。 

在行中，默认有两个隐藏字段： 

- Trx_id：每次对某条记录进行改动时，都会把对应的事务id赋值给trx_id隐藏列。 
- Roll_pointer：每次对某条引记录进行改动时，都会把旧的版本写入到undo日志中，然后这个隐藏列就相当于一个指针，可以通过它来找到该记录修改前的信息。

## 8.2 架构

MySQL5.5 版本开始，默认使用InnoDB存储引擎，它擅长事务处理，具有崩溃恢复特性，在日常开发中使用非常广泛。下面是InnoDB架构图，左侧为内存结构，右侧为磁盘结构。

![](..\图片\2-01【MySQL】\19.png)

### 8.2.1 内存结构

在左侧的内存结构中，主要分为这么四大块儿： Buffer Pool、Change Buffer、Adaptive Hash Index、Log Buffer。 接下来介绍一下这四个部分。

1). Buffer Pool

InnoDB存储引擎基于磁盘文件存储，访问物理硬盘和在内存中进行访问，速度相差很大，为了尽可能弥补这两者之间的I/O效率的差值，就需要把经常使用的数据加载到缓冲池中，避免每次访问都进行磁盘I/O。

在InnoDB的缓冲池中不仅缓存了索引页和数据页，还包含了undo页、插入缓存、自适应哈希索引以及InnoDB的锁信息等等。

缓冲池 Buffer Pool，是主内存中的一个区域，里面可以缓存磁盘上经常操作的真实数据，在执行增删改查操作时，先操作缓冲池中的数据（若缓冲池没有数据，则从磁盘加载并缓存），然后再以一定频率刷新到磁盘，从而减少磁盘IO，加快处理速度。

缓冲池以Page页为单位，底层采用链表数据结构管理Page。根据状态，将Page分为三种类型：

-  free page：空闲page，未被使用。
-  clean page：被使用page，数据没有被修改过。
-  dirty page：脏页，被使用page，数据被修改过，也中数据与磁盘的数据产生了不一致。

在专用服务器上，通常将多达80％的物理内存分配给缓冲池 。参数设置： `show variables`、`like 'innodb_buffer_pool_size'`;

2). Change Buffer

Change Buffer，更改缓冲区（针对于非唯一二级索引页），在执行DML语句时，如果这些数据Page没有在Buffer Pool中，不会直接操作磁盘，而会将数据变更存在更改缓冲区 Change Buffer中，在未来数据被读取时，再将数据合并恢复到Buffer Pool中，再将合并后的数据刷新到磁盘中。Change Buffer的意义是什么呢?

先来看一幅图，这个是二级索引的结构图：

![](..\图片\2-01【MySQL】\20 二级索引.png)

与聚集索引不同，二级索引通常是非唯一的，并且以相对随机的顺序插入二级索引。同样，删除和更新可能会影响索引树中不相邻的二级索引页，如果每一次都操作磁盘，会造成大量的磁盘IO。有了ChangeBuffer之后，我们可以在缓冲池中进行合并处理，减少磁盘IO。



3). Adaptive Hash Index

自适应hash索引，用于优化对Buffer Pool数据的查询。MySQL的innoDB引擎中虽然没有直接支持hash索引，但是给我们提供了一个功能就是这个自适应hash索引。因为前面我们讲到过，hash索引在进行等值匹配时，一般性能是要高于B+树的，因为hash索引一般只需要一次IO即可，而B+树，可能需要几次匹配，所以hash索引的效率要高，但是hash索引又不适合做范围查询、模糊匹配等。

InnoDB存储引擎会监控对表上各索引页的查询，如果观察到在特定的条件下hash索引可以提升速度，则建立hash索引，称之为自适应hash索引。

**自适应哈希索引，无需人工干预，是系统根据情况自动完成。**

参数： adaptive_hash_index



4). Log Buffer

Log Buffer：日志缓冲区，用来保存要写入到磁盘中的log日志数据（redo log 、undo log），默认大小为 16MB，日志缓冲区的日志会定期刷新到磁盘中。如果需要更新、插入或删除许多行的事务，增加日志缓冲区的大小可以节省磁盘 I/O。

参数:

innodb_log_buffer_size：缓冲区大小

innodb_flush_log_at_trx_commit：日志刷新到磁盘时机，取值主要包含以下三个：

- 1: 日志在每次事务提交时写入并刷新到磁盘，默认值。
- 0: 每秒将日志写入并刷新到磁盘一次。
- 2: 日志在每次事务提交后写入，并每秒刷新到磁盘一次。

### 8.2.2 磁盘结构

![](..\图片\2-01【MySQL】\19.png)

1). System Tablespace

系统表空间是更改缓冲区的存储区域。如果表是在系统表空间而不是每个表文件或通用表空间中创建的，它也可能包含表和索引数据。(在MySQL5.x版本中还包含InnoDB数据字典、undolog等)

参数：innodb_data_file_path

系统表空间，默认的文件名叫 ibdata1。



2). File-Per-Table Tablespaces

如果开启了innodb_file_per_table开关 ，则每个表的文件表空间包含单个InnoDB表的数据和索引 ，并存储在文件系统上的单个数据文件中。

开关参数：innodb_file_per_table ，该参数默认开启。

那也就是说，我们没创建一个表，都会产生一个表空间文件。



3). General Tablespaces

通用表空间，需要通过 CREATE TABLESPACE 语法创建通用表空间，在创建表时，可以指定该表空间。

A. 创建表空间

```sql
CREATE TABLESPACE ts_name ADD DATAFILE 'file_name' ENGINE = engine_name;
```

B. 创建表时指定表空间

```sql
CREATE TABLE xxx ... TABLESPACE ts_name
```





4). Undo Tablespaces

撤销表空间，MySQL实例在初始化时会自动创建两个默认的undo表空间（初始大小16M），用于存储undo log日志。



5). Temporary Tablespaces

InnoDB 使用会话临时表空间和全局临时表空间。存储用户创建的临时表等数据。



6). Doublewrite Buffer Files

双写缓冲区，innoDB引擎将数据页从Buffer Pool刷新到磁盘前，先将数据页写入双写缓冲区文件中，便于系统异常时恢复数据。



7). Redo Log

重做日志，是用来实现事务的持久性。该日志文件由两部分组成：重做日志缓冲（redo logbuffer）以及重做日志文件（redo log）,前者是在内存中，后者在磁盘中。当事务提交之后会把所有修改信息都会存到该日志中, 用于在刷新脏页到磁盘时,发生错误时, 进行数据恢复使用。

以循环方式写入重做日志文件，涉及两个文件：ib_logfile0和ib_logfile1

### 8.2.3 后台线程

![](..\图片\2-01【MySQL】\21MySQL后台线程.png)

在InnoDB的后台线程中，分为4类，分别是：Master Thread 、IO Thread、Purge Thread、Page Cleaner Thread。

* Master Thread

  核心后台线程，负责调度其他线程，还负责将缓冲池中的数据异步刷新到磁盘中, 保持数据的一致性，还包括脏页的刷新、合并插入缓存、undo页的回收 。

  

* IO Thread

  在InnoDB存储引擎中大量使用了AIO来处理IO请求, 这样可以极大地提高数据库的性能，而IOThread主要负责这些IO请求的回调。

  | 线程类型             | 默认个数 | 职责                         |
  | -------------------- | -------- | ---------------------------- |
  | Read thread          | 4        | 负责读操作                   |
  | Write thread         | 4        | 负责写操作                   |
  | Log thread           | 1        | 负责将日志缓冲区刷新到磁盘   |
  | Insert buffer thread | 1        | 负责将写缓冲区内容刷新到磁盘 |

  我们可以通过以下的这条指令，查看到InnoDB的状态信息，其中就包含IO Thread信息。

  ```sql
  show engine innodb status \G;
  ```

  

* Purge Thread 

  主要用于回收事务已经提交了的undo log，在事务提交之后，undo log可能不用了，就用它来回收。

  

* Page Cleaner Thread
  协助 Master Thread 刷新脏页到磁盘的线程，它可以减轻 Master Thread 的工作压力，减少阻塞。

## 8.3 事务原理

事务 是一组操作的集合，它是一个不可分割的工作单位，事务会把所有的操作作为一个整体一起向系统提交或撤销操作请求，即这些操作要么同时成功，要么同时失败。

事务的四大特性：`ACID`

* A原子性(atomicity)：事务中的所有操作，要么全部成功，要么全部失败。或称不可分割性。
* C一致性(consistency)：要保证数据库内部完整性约束、声明性约束。
* I隔离性(isolation)：对同一资源操作的事务不能够同时发生。又称独立性。
* D持久性(durability)：对数据库做出的一切修改将永久保存，不管是否出现故障。

隔离级别一共有四种：

1. `read uncommitted`：读未提交

   产生的问题：脏读、不可重复读、幻读。

2. `read committed`：读已提交(Oracle默认隔离级别)

   产生的问题：不可重复读、幻读。

3. `repeatable read`：可重复读(MySQL默认级别)

   产生的问题：幻读。

4. `serializable`：串行化

   可以解决所有的问题

那实际上，我们研究事务的原理，就是研究MySQL的InnoDB引擎是如何保证事务的这四大特性的。

而对于这四大特性，实际上分为两个部分。 其中的原子性、一致性、持久性，实际上是由InnoDB中的两份日志来保证的，一份是redo log日志，一份是undo log日志。 而隔离性是通过数据库的锁，加上MVCC来保证的。

![](..\图片\2-01【MySQL】\22事务原理.png)

### 8.3.1 redo log

redo log日志保证的是持久性。

重做日志，记录的是事务提交时数据页的物理修改，是用来实现事务的持久性。

该日志文件由两部分组成：重做日志缓冲（redo log buffer）以及重做日志文件（redo logfile）,前者是在内存中，后者在磁盘中。当事务提交之后会把所有修改信息都存到该日志文件中，用于在刷新脏页到磁盘,发生错误时, 进行数据恢复使用。

![](..\图片\2-01【MySQL】\19.png)

如果没有redolog，可能会存在什么问题的？ 我们一起来分析一下：

在InnoDB引擎中的内存结构中，主要的内存区域就是缓冲池，在缓冲池中缓存了很多的数据页。 在一个事务中，执行多个增删改的操作时，InnoDB引擎会先操作缓冲池Buffer Pool中的数据，如果缓冲池没有对应的数据，会通过后台线程将磁盘中的数据加载出来，存放在缓冲区Change Buffer中，然后将缓冲池中的数据修改，修改后的数据页我们称为脏页。 

脏页会在一定的时机，通过后台线程刷新到磁盘中，从而保证缓冲区与磁盘的数据一致。 而缓冲区的脏页数据并不是实时刷新的，而是一段时间之后将缓冲区的数据刷新到磁盘中，假如刷新到磁盘的过程出错了，提示给用户事务提交成功，而数据却没有持久化下来，这就出现问题了，没有保证事务的持久性。

![](..\图片\2-01【MySQL】\23-1redo log.png)

在InnoDB中提供了一份日志 redo log，接下来我们再来分析一 下，通过redolog如何解决这个问题。

有了redolog之后，当对缓冲区的数据进行增删改之后，会首先将操作的数据页的变化记录在redolog buffer中。在事务提交时，会将redolog buffer中的数据刷新到redo log磁盘文件中。过一段时间之后，如果刷新缓冲区的脏页到磁盘时发生错误，此时就可以借助于redo log进行数据恢复，这样就保证了事务的持久性。 而如果脏页成功刷新到磁盘或者涉及到的数据已经落盘，此时redolog就没有作用了，就可以删除了，所以存在的两个redolog文件是循环写的。

那为什么每一次提交事务，要刷新redo log 到磁盘中呢，而不是直接将buffer pool中的脏页刷新到磁盘呢 ?

因为在业务操作中，我们操作数据一般都是随机读写磁盘的，而不是顺序读写磁盘。 而redo log在往磁盘文件中写入数据，由于是日志文件，所以都是顺序写的。顺序写的效率，要远大于随机写。 这种先写日志的方式，称之为 WAL（Write-Ahead Logging）。

![](..\图片\2-01【MySQL】\23-2redolog.png)

### 8.3.2 undo log

undo log：回滚日志，用于记录数据被修改前的信息。

作用包含两个 : 提供回滚(保证事务的原子性) 和MVCC(多版本并发控制) 。

undo log和redo log记录物理日志不一样，它是逻辑日志。可以认为当delete一条记录时，undolog中会记录一条对应的insert记录，反之亦然，当update一条记录时，它记录一条对应相反的update记录。当执行rollback时，就可以从undo log中的逻辑记录读取到相应的内容并进行回滚。

- Undo log销毁：undo log在事务执行时产生，事务提交时，并不会立即删除undo log，因为这些日志可能还用于MVCC。
- Undo log存储：undo log采用段的方式进行管理和记录，存放在前面介绍的 rollback segment回滚段中，内部包含1024个undo log segment。

## 8.4 MVCC

当前读：读取的是记录的最新版本，读取时还要保证其他并发事务不能修改当前记录，会对读取的记录进行加锁。对于我们日常的操作，如：`select ... lock in share mode(共享锁)`，`select ...for update、update、insert、delete(排他锁)`都是一种当前读。	

快照读：简单的select（不加锁）就是快照读，快照读，读取的是记录数据的可见版本，有可能是历史数据， 不加锁，是非阻塞读。 

- `Read Committed`：每次select，都生成一个快照读。
- `Repeatable Read`：开启事务后第一个select语句才是快照读的地方。 
- `Serializable`：快照读会退化为当前读。

MVCC：全称 Multi-Version Concurrency Control，多版本并发控制。指维护一个数据的多个版本，使得读写操作没有冲突，快照读为MySQL实现MVCC提供了一个非阻塞读功能。MVCC的具体实现，还需要依赖于数据库记录中的三个隐式字段、undo log日志、readView。

### 8.4.1 隐藏字段

![](..\图片\2-01【MySQL】\24隐藏字段.png)

当我们创建了上面的这张表，我们在查看表结构的时候，就可以显式的看到这三个字段。 实际上除了 这三个字段以外，InnoDB还会自动的给我们添加三个隐藏字段及其含义分别是：

| 隐藏字段    | 含义                                                         |
| ----------- | ------------------------------------------------------------ |
| DB_TRX_ID   | 最近修改事务ID，记录插入这条记录或最后一次修改该记录的事务ID。 |
| DB_ROLL_PTR | 回滚指针，指向这条记录的上一个版本，用于配合undo log，指向上一个版本。 |
| DB_ROW_ID   | 隐藏主键，如果表结构没有指定主键，将会生成该隐藏字段。       |


上述的前两个字段是肯定会添加的， 是否添加最后一个字段`DB_ROW_ID`，得看当前表有没有主键， 如果有主键，则不会添加该隐藏字段。

查看有主键的表stu：

```sh
# 进入服务器中的 /var/lib/mysql/itcast/ , 查看stu的表结构信息
ibd2sdi stu.ibd
```

### 8.4.2 undolog

回滚日志，在insert、update、delete的时候产生的便于数据回滚的日志。

当insert的时候，产生的undo log日志只在回滚时需要，在事务提交后，可被立即删除。而update、delete的时候，产生的undo log日志不仅在回滚时需要，在快照读时也需要，不会立即被删除。

undolog版本链：

- `DB_TRX_ID` : 代表最近修改事务ID，记录插入这条记录或最后一次修改该记录的事务ID，是 自增的。
-  `DB_ROLL_PTR` ： 由于这条数据是才插入的，没有被更新过，所以该字段值为null。

不同事务或相同事务对同一条记录进行修改，会导致该记录的undolog生成一条记录版本链表，链表的头部是最新的旧记录，链表尾部是最早的旧记录。

![](..\图片\2-01【MySQL】\25undolog版本链.png)

### 8.4.3 readview

ReadView（读视图）是 快照读 SQL执行时MVCC提取数据的依据，记录并维护系统当前活跃的事务（未提交的）id。

ReadView中包含了四个核心字段：

| 字段           | 含义                                                 |
| -------------- | ---------------------------------------------------- |
| m_ids          | 当前活跃的事务ID集合                                 |
| min_trx_id     | 最小活跃事务ID                                       |
| max_trx_id     | 预分配事务ID，当前最大事务ID+1（因为事务ID是自增的） |
| creator_trx_id | ReadView创建者的事务ID                               |

而在readview中就规定了版本链数据的访问规则：`trx_id` 代表当前undolog版本链对应事务ID。

| 条件                                   | 是否可以访问                                   | 说明                                       |
| -------------------------------------- | ---------------------------------------------- | ------------------------------------------ |
| trx_id == creator_trx_id               | 可以访问该版本                                 | 成立，说明数据是当前这个事务更改的。       |
| trx_id < min_trx_id                    | 可以访问该版本                                 | 成立，说明数据已经提交了。                 |
| trx_id > max_trx_id                    | 不可以访问该版本                               | 成立，说明该事务是在ReadView生成后才开启。 |
| min_trx_id <= trx_id<br/><= max_trx_id | 如果trx_id不在m_ids中，<br/>是可以访问该版本的 | 成立，说明数据已经提交。                   |

不同的隔离级别，生成ReadView的时机不同：

- `READ COMMITTED` ：在事务中每一次执行快照读时生成ReadView。
- `REPEATABLE READ`：仅在事务中第一次执行快照读时生成ReadView，后续复用该ReadView。

### 8.4.3 原理分析

RC隔离级别下，在事务中每一次执行快照读时生成ReadView。

假设在事务5中，查询了两次id为30的记录，由于隔离级别为Read Committed，所以每一次进行快照读都会生成一个ReadView，那么生成两次ReadView。

那么这两次快照读在获取数据时，就需要根据所生成的ReadView以及ReadView的版本链访问规则， 到undolog版本链中匹配数据，最终决定此次快照读返回的数据。

RR隔离级别下，仅在事务中第一次执行快照读时生成ReadView，后续复用该ReadView。 而RR 是可 重复读，在一个事务中，执行两次相同的select语句，查询到的结果是一样的。

![](..\图片\2-01【MySQL】\27.png)

# 第九章 MySQL管理

## 9.1 系统管理库

Mysql数据库安装完成后，自带了一下四个数据库，具体作用如下：

| 数据库             | 含义                                                         |
| ------------------ | ------------------------------------------------------------ |
| mysql              | 存储MySQL服务器正常运行所需要的各种信息 （时区、主从、用户、权限等） |
| information_schema | 提供了访问数据库元数据的各种表和视图，包含数据库、表、字段类型及访问权限等 |
| performance_schema | 为MySQL服务器运行时状态提供了一个底层监控功能，<br />主要用于收集数据库服务器性能参数 |
| sys                | 包含了一系列方便 DBA 和开发人员利用performance_schema性能数据库进行性能调优和诊断的视图 |

## 9.2 常用工具

### 9.2.1 MySQL

该mysql不是指mysql服务，而是指mysql的客户端工具。

```sql
语法 ：
	mysql [options] [database]
选项 ：
    -u, --user=name #指定用户名
    -p, --password[=name] #指定密码
    -h, --host=name #指定服务器IP或域名
    -P, --port=port #指定连接端口
    -e, --execute=name #执行SQL语句并退出
```

`-e`选项可以在Mysql客户端执行SQL语句，而不用连接到MySQL数据库再执行，对于一些批处理脚本，这种方式尤其方便。

```sh
# -e选项
mysql -u用户名 –p密码 数据库名称 -e "执行语句"		# 模板
mysql -uroot -p1234 linxuan -e "select * from course" 	# 示例
```

### 9.2.2 mysqladmin

mysqladmin 是一个执行管理操作的客户端程序。可以用它来检查服务器的配置和当前状态、创建并删除数据库等。

```sql
通过帮助文档查看选项：
	mysqladmin --help
```

```sql
语法:
	mysqladmin [options] command ...
选项:
    -u, --user=name #指定用户名
    -p, --password[=name] #指定密码
    -h, --host=name #指定服务器IP或域名
    -P, --port=port #指定连接端口
```

```sh
# 示例
mysqladmin -uroot -p1234 create 'test01'		# 创建数据库
mysql -uroot -p1234 -e 'show databases'			# 查看所有数据库
mysqladmin -uroot -p1234 drop 'test01'			# 删除数据库
```

### 9.2.3  mysqlbinlog

由于服务器生成的二进制日志文件以二进制格式保存，所以如果想要检查这些文本的文本格式，就会使用到mysqlbinlog 日志管理工具。

```sql
语法 ：
	mysqlbinlog [options] log-files1 log-files2 ...
选项 ：
    -d, --database=name 指定数据库名称，只列出指定的数据库相关操作。
    -o, --offset=# 忽略掉日志中的前n行命令。
    -r,--result-file=name 将输出的文本格式日志输出到指定文件。
    -s, --short-form 显示简单格式， 省略掉一些信息。
    --start-datatime=date1 --stop-datetime=date2 指定日期间隔内的所有日志。
    --start-position=pos1 --stop-position=pos2 指定位置间隔内的所有日志。
```

```sh
# mysqlbinlog日志默认在 /var/lib/mysql中 我们要进去这个文件夹
mysqlbinlog -s binlog.000001		# 查看binlog.000001 简略查看
```

### 9.2.4 mysqlshow

mysqlshow 客户端对象查找工具，用来很快地查找存在哪些数据库、数据库中的表、表中的列或者索引。

```sql
语法 ：
	mysqlshow [options] [db_name [table_name [col_name]]]
选项 ：
    --count 显示数据库及表的统计信息（数据库，表 均可以不指定）
    -i 显示指定数据库或者指定表的状态信息
```

```sh
#查询test库中每个表中的字段，及行数
mysqlshow -uroot -p1234 test --count
#查询test库中book表的详细情况
mysqlshow -uroot -p1324 test book --count
# 查看test数据库中book表的状态信息
mysqlshow -uroot -p1324 test book -i
```

### 9.2.5 mysqldump

mysqldump 客户端工具用来备份数据库或在不同数据库之间进行数据迁移。备份内容包含创建表，及插入表的SQL语句。

```sql
语法 ：
    mysqldump [options] db_name [tables]
    mysqldump [options] --database/-B db1 [db2 db3...]
    mysqldump [options] --all-databases/-A
连接选项 ：
    -u, --user=name 指定用户名
    -p, --password[=name] 指定密码
    -h, --host=name 指定服务器ip或域名
    -P, --port=# 指定连接端口
输出选项：
    --add-drop-database 在每个数据库创建语句前加上 drop database 语句
    --add-drop-table 在每个表创建语句前加上 drop table 语句 , 默认开启 ; 不开启 (--skip-add-drop-table)
    -n, --no-create-db 不包含数据库的创建语句
    -t, --no-create-info 不包含数据表的创建语句
    -d --no-data 不包含数据
    -T, --tab=name 自动生成两个文件：一个.sql文件，创建表结构的语句；一个.txt文件，数据文件
```

* **备份db01数据库：**

  ```sh
  mysqldump -uroot -p1234 db01 > db01.sql 		 
  ```

  备份出来的数据包含：

  - 删除表的语句
  - 创建表的语句
  - 数据插入语句

  如果我们在数据备份时，不需要创建表，或者不需要备份数据，只需要备份表结构，都可以通过对应的参数来实现。

* **备份db01数据库中的表数据，不备份表结构(-t)**

  ```sh
  mysqldump -uroot -p1234 -t db01 > db01.sql
  ```

  打开 db02.sql ，来查看备份的数据，只有insert语句，没有备份表结构。

* **将db01数据库的表的表结构与数据分开备份(-T)**

  ```sh
  mysqldump -uroot -p1234 -T /root db01 score
  ```

  执行上述指令，会出错，数据不能完成备份，原因是因为我们所指定的数据存放目录/root，MySQL认为是不安全的，需要存储在MySQL信任的目录下。那么，哪个目录才是MySQL信任的目录呢，可以查看一下系统变量 secure_file_priv 。

### 9.2.6 mysqlimport/source

mysqlimport 是客户端数据导入工具，用来导入mysqldump 加 -T 参数后导出的文本文件。

```sql
语法 ：
	mysqlimport [options] db_name textfile1 [textfile2...]
示例 ：
	mysqlimport -uroot -p2143 test /tmp/city.txt
```

如果需要导入sql文件,可以使用mysql中的source 指令 :

```sql
语法 ：
	source /root/xxxxx.sql
```

