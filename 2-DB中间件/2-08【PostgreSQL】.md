# 第一章 PG简介

<!-- Object Relational:对象关系； -->

PostgreSQL（可读作`Post-gress-Q-L`） 是一个功能强大的开源对象关系数据库管理系统（ORDBMS）。PostgreSQL 由 PostgreSQL 全球志愿者团队开发，开源，它不受任何公司或其他私人实体控制。

常用网站如下：[PostgreSQL 官方网站](https://www.postgresql.org/)、[PostgreSQL 中文文档](http://postgres.cn/docs/12/)、[全球数据库排行榜](https://db-engines.com/en/ranking)、[国产数据库排行榜](https://www.modb.pro/dbRank)

​                                                                       全球数据库排行榜

| Jul 2023 | Jun 2023 | Jul 2022 | DBMS                 | Jul 2023 | Jun 2023 |
| :------- | :------- | -------- | -------------------- | -------- | -------- |
| 1.       | 1.       | 1.       | Oracle               | 1256.01  | +24.54   |
| 2.       | 2.       | 2.       | MySQL                | 1150.35  | -13.59   |
| 3.       | 3.       | 3.       | Microsoft SQL Server | 921.60   | -8.47    |
| 4.       | 4.       | 4.       | PostgreSQL           | 617.83   | +5.01    |
| 5.       | 5.       | 5.       | MongoDB              | 435.49   | +10.13   |
| 6.       | 6.       | 6.       | Redis                | 163.76   | -3.59    |
| 7.       | 7.       | 7.       | IBM Db2              | 139.81   | -5.07    |
| 8.       | 8.       | 8.       | Elasticsearch        | 139.59   | -4.16    |
| 9.       | 9.       | 9.       | Microsoft Access     | 130.72   | -3.73    |

​                                                                       国产数据库排行榜

| 排行 | 上月 | 半年前 | 名称                                        | 模型                                     | 专利 | 论文  | 得分       |
| ---- | ---- | ------ | ------------------------------------------- | ---------------------------------------- | ---- | ----- | ---------- |
| 1    | 1    | 1      | [OceanBase ](https://www.modb.pro/wiki/34)  | [关系型](https://www.modb.pro/wiki/1222) | 151  | 22    | **638.36** |
| 2    | 3    | 3      | [openGauss ](https://www.modb.pro/wiki/601) | [关系型](https://www.modb.pro/wiki/1222) | 562  | 11    | **627.08** |
| 3    | 2    | 2      | [TiDB ](https://www.modb.pro/wiki/20)       | [关系型](https://www.modb.pro/wiki/1222) | 40   | 44    | **577.30** |
| 4    | 6    | 6      | [GaussDB ](https://www.modb.pro/wiki/46)    | [关系型](https://www.modb.pro/wiki/1222) | 630  | 14    | **536.91** |
| 5    | 7    | 5      | [PolarDB ](https://www.modb.pro/wiki/37)    | [关系型](https://www.modb.pro/wiki/1222) | 592  | 70    | **518.99** |
| 6    | 4    | 4      | [达梦 ](https://www.modb.pro/wiki/10)       | [关系型](https://www.modb.pro/wiki/1222) | 518  | **0** | **459.69** |
| 7    | 5    | 7      | [人大金仓 ](https://www.modb.pro/wiki/13)   | [关系型](https://www.modb.pro/wiki/1222) | 273  | **0** | **443.01** |
| 8    | 8    | 9      | [GBase ](https://www.modb.pro/wiki/26)      | [关系型](https://www.modb.pro/wiki/1222) | 152  | **0** | **337.92** |
| 9    | 9    | 8      | [TDSQL ](https://www.modb.pro/wiki/50)      | [关系型](https://www.modb.pro/wiki/1222) | 39   | 10    | **336.20** |
| 10   | 10   | 10     | [AnalyticDB ](https://www.modb.pro/wiki/87) | [关系型](https://www.modb.pro/wiki/1222) | 480  | 54    | **191.77** |

## 1.1 PG介绍

<!-- Structured Query Language:SQL 结构化查询语言 -->

PostgreSQL 最初设想于1986年，当时被叫做 Berkley Postgres Project。该项目一直到 1994 年都处于演进和修改中，直到开发人员 Andrew Yu 和 Jolly Chen 在 Postgres 中添加了一个 SQL 翻译程序，在开放源代码社区发放。开始以社区的形式运作。

1996年，对 Postgres95 做了较大的改动，并将其作为 PostgresSQL6.0 版发布。该版本的 Postgres 提高了后端的速度，包括增强型 SQL92 标准以及重要的后端特性（包括子选择、默认值、约束和触发器）。

2005年，发布 8.0 版本，开始支持 windows 系统环境。2021 年，PostgreSQL 全球开发组宣布，功能最为强大的开源数据库 PostgreSQL 14版本正式发布！目前生产环境主流的版本是 PostgreSQL 12

------

Postgres 遵守 BSD（Berkeley Software Distribution license） 许可证发行，它是[自由软件](https://zh.wikipedia.org/wiki/自由軟體)中使用最广泛的许可协议之一。

BSD 许可证，也被称为伯克利软件发行许可证，是一种允许软件自由使用、修改和发行的开源许可证。BSD许可证是允许性的，这意味着它对许可软件的使用和分发的限制最小。BSD 许可证的主要要求是，任何软件的再分发必须包括许可证的副本和免责声明。许多软件开发商和公司使用这种许可证，以确保他们的作品能被广泛的用户使用，同时保持软件的权利。

BSD许可证的条款通常包括以下内容。

- 该软件可以用于任何目的，包括商业用途。
- 该软件可以不受任何限制地修改和分发。
- 源代码必须包括在软件的任何分发中。
- 软件的任何分发都必须包括一份许可证的副本。
- 软件的任何分发都必须包括免责声明。

------

MySQL 被 Oracle 所控制，MySQL 同时使用了 GPL 和一种商业许可（称为双重许可）。

- GPL（General Public license）是公共许可，遵循了 GPL 的软件是公共的。如果某软件使用了GPL软件，那么该软件也需要开源，如果不开源，就不能使用 GPL 软件，这和是否把该软件商用与否是没关系的。

- 如果无法满足GPL，就需要获得商业许可，通过与 Oracle 公司联系，制定解决方案，受 Oracle 公司约束。


## 1.2 PG与MySQL比较 

PostgreSQL 相对于 MySQL的优势：

1. 在 SQL 的标准实现上要比 MySQL 完善，而且功能实现比较严谨。
2. 对表连接支持较完整，优化器的功能较完整，支持的索引类型很多，复杂查询能力较强。
3. PG 主表采用堆表存放，MySQL 采用索引组织表，能够支持比 MySQL 更大的数据量。 
4. PG 的主备复制属于物理复制，相对于 MySQL 基于 binlog 的逻辑复制，数据的一致性更加可靠，复制性能更高，对主机性能的影响也更小。
5. PG 支持 JSON 和其他 NoSQL 功能，如本机 XML 支持和使用 HSTORE 的键值对。它还支持索引 JSON 数据以加快访问速度，特别是 10 版本 JSONB 更是强大。 
6. PG 完全免费，而且是 BSD 协议，这表明了 PG 不会被其它公司控制。MySQL 现在被 Oracle 公司控制。

MySQL 相对于 PG 的优势：

1. innodb 的基于回滚段实现的 MVCC 机制，相对 PG 新老数据一起存放的基于 XID 的 MVCC 机制是占优的。新老数据一起存放，需要定时触发 VACUUM，会带来多余的 IO 和数据库对象加锁开销，引起数据库整体的并发能力下降。而且 VACUUM 清理不及时，还可能会引发数据膨胀。
2. MySQL 采用索引组织表，这种存储方式非常适合基于主键匹配的查询、删改操作，但对表结构设计存在约束。 
3. MySQL 的优化器较简单，系统表、运算符、数据类型的实现都很精简，非常适合简单的查询操作。
4. MySQL 相对于 PG 在国内的流行度更高，PG 在国内显得就有些落寞了。
5. MySQL 的存储引擎插件化机制，使得它的应用场景更加广泛，比如除了 innodb 适合事务处理场景外，myisam 适合静态数据的查询场景。

从应用场景来说，PG 更加适合严格的企业应用场景（比如金融、电信、ERP、CRM），但不仅仅限制于此，PostgreSQL 的 json，jsonb，hstore 等数据格式，特别适用于一些大数据格式的分析；而 MySQL 更加适合业务逻辑相对简单、数据可靠性要求较低的互联网场景（比如 google、facebook、alibaba），当然现在 MySQL 的在 innodb 引擎的大力发展，功能表现良好

## 1.3 PG的下载与安装

### 1.3.2 Centos7 安装

访问官网[下载地址](https://www.postgresql.org/download/)，选择Linux -> Centos7，然后跟着步骤走即可。

```sh
# 导入yum源
[root@linxuanVM local]# sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
Loaded plugins: fastestmirror
pgdg-redhat-repo-latest.noarch.rpm                                                                   
...
# 安装PostgreSQL服务
[root@linxuanVM local]# sudo yum install -y postgresql14-server
# 初始化数据库
[root@linxuanVM local]# sudo /usr/pgsql-14/bin/postgresql-14-setup initdb
# 设置PostgreSQL服务为开机启动
[root@linxuanVM local]# sudo systemctl enable postgresql-14
# 启动PostgreSQL服务
[root@linxuanVM local]# sudo systemctl start postgresql-14
```

PostgreSQL 安装成功之后，会默认创建一个名为 postgres 的 Linux 用户，从 root 用户切换为 postgres 用户命令为`su postgres`。初始化数据库后，会有名为 postgres 的数据库，来存储数据库的基础信息，例如用户信息等等，相当于MySQL中默认的名为mysql数据库。

postgres 数据库中会初始化一名超级用户`postgres`，为了方便我们使用postgres账号进行管理，我们可以修改该账号的密码。

```sh
# 从root用户切换为postgres用户，所在仍然是Linux的Shell窗口，psql命令会进入SQL界面
[root@linxuanVM ~]# su postgres
bash-4.2$ psql
could not change directory to "/root": Permission denied
psql (14.8)
Type "help" for help.

# 这里设置密码为postgres
postgres=# ALTER USER postgres WITH PASSWORD 'postgres';   
ALTER ROLE
postgres=# exit
bash-4.2$ exit
exit
```

配置远程访问

```sh
# 尝试开发5432端口，但是我们的服务器防火墙是关闭的，所以执行失败。
[root@linxuanVM ~]# sudo firewall-cmd --add-port=5432/tcp --permanent
FirewallD is not running
# 如果执行成功，那么需要重启防火墙：sudo firewall-cmd --reload

# 修改配置文件，将监听地址配置为*
[root@linxuanVM ~]# vim /var/lib/pgsql/14/data/postgresql.conf
listen_addresses = '*'

[root@linxuanVM ~]# vim /var/lib/pgsql/14/data/pg_hba.conf
# 在这个位置添加一行数据，表示允许所有的IP访问
# IPv4 local connections:
host    all             all             127.0.0.1/32            scram-sha-256
host    all             all             0.0.0.0/0               scram-sha-256 # 添加改行

# 重启PG服务
[root@linxuanVM ~]# sudo systemctl restart postgresql-14
```

### 1.3.1 Windows 安装

访问官网[下载地址](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)，下载最新发布的 PostgreSQL 14。我设置的 PG 超级用户密码为 linxuan。

# 第二章 PG基本使用

|    psql常用命令    |           解释           |
| :----------------: | :----------------------: |
|     \password      |         设置密码         |
|         \q         |           退出           |
|         \h         |    查看SQL命令的解释     |
|         \?         |     查看psql命令列表     |
|         \l         |      列出所有数据库      |
| \c [database_name] |      连接其他数据库      |
|         \d         | 列出当前数据库的所有表格 |
|  \d [table_name]   |   列出某一张表格的结构   |
|        \du         |       列出所有用户       |

## 2.1 PG登录

```sql
# 使用psql连接PG的格式为：psql -h 服务器 -U 用户名 -d 数据库 -p 端口地址
# 如果使用psql命令直接连接PG，那么代表用户名和数据库都是postgres
[root@linxuanVM ~]# su postgres
bash-4.2$ psql
could not change directory to "/root": Permission denied
psql (14.8)
Type "help" for help.

# 退出psql
postgres-# \q
# 返回Linux的Shell窗口
bash-4.2$ 
```

## 2.2 数据库操作

```sql
-- 连接postgres数据库，默认情况下会直接连接postgres数据库
bash-4.2$ psql
could not change directory to "/root": Permission denied
psql (14.8)
Type "help" for help.

-- 创建数据库mydb
postgres=# create database mydb;
CREATE DATABASE
-- 查看所有数据库
postgres=# \l
                                  List of databases
   Name    |  Owner   | Encoding |   Collate   |    Ctype    |   Access privileges   
-----------+----------+----------+-------------+-------------+-----------------------
 mydb      | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 postgres  | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 template0 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
 template1 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
(4 rows)

-- 切换数据库
postgres=# \c mydb;
You are now connected to database "mydb" as user "postgres".
-- 切换数据库
mydb=# \c postgres
You are now connected to database "postgres" as user "postgres".
-- 删除数据库
postgres=# drop database mydb;
DROP DATABASE
-- 退出psql
postgres=# \q
```

```sh
[root@linxuanVM ~]# sudo su - postgres
Last login: Mon Jul 31 16:58:08 CST 2023 on pts/0
# 连接mydb数据库
-bash-4.2$ psql -d mydb
psql (14.8)
Type "help" for help.

mydb=# \q
```

## 2.3 数据类型

PotgreSQL 中主要有三类数据类型：数值数据类型、字符串数据类型、日期 / 时间数据类型。其他数据类型类型还有布尔值 boolean （true 或 false），货币数额 money 和 几何数据等。

| 数值类型名称 | 存储长度 | 描述                 | 范围                                         |
| ------------ | -------- | -------------------- | -------------------------------------------- |
| smallint     | 2 字节   | 小范围整数           | -32768 到 +32767                             |
| integer      | 4 字节   | 常用的整数           | -2147483648 到 +2147483647                   |
| bigint       | 8 字节   | 大范围整数           | -9223372036854775808 到 +9223372036854775807 |
| decimal      | 可变长   | 用户指定的精度，精确 | 小数点前 131072 位；小数点后 16383 位        |
| numeric      | 可变长   | 用户指定的精度，精确 | 小数点前 131072 位；小数点后 16383 位        |
| real         | 4 字节   | 可变精度，不精确     | 6 位十进制数字精度                           |
| double       | 8 字节   | 可变精度，不精确     | 15 位十进制数字精度                          |

| 字符串类型名称 | 描述                                                         |
| -------------- | ------------------------------------------------------------ |
| char(size)     | character(size)：固定长度字符串，size 规定了需存储的字符数，由右边的空格补齐 |
| varchar(size)  | character varying(size)：可变长度字符串，size 规定了需存储的字符数 |
| text           | 可变长度字符串                                               |

| 日期类型名称 | 描述         |
| ------------ | ------------ |
| timestamp    | 日期和时间   |
| date         | 日期，无时间 |
| time         | 时间         |

## 2.4 序列自增

PostgreSQL 使用序列来标识字段的自增长，数据类型有 smallserial、serial 和 bigserial 。这些属性类似于 MySQL 数据库支持的 AUTO_INCREMENT 属性。

SMALLSERIAL、SERIAL 和 BIGSERIAL 范围：

| 伪类型      | 存储大小 | 范围                          |
| ----------- | -------- | ----------------------------- |
| SMALLSERIAL | 2字节    | 1 到 32,767                   |
| SERIAL      | 4字节    | 1 到 2,147,483,647            |
| BIGSERIAL   | 8字节    | 1 到 922,337,2036,854,775,807 |

```sql
-- 以下命令并没有使用psql操作，而是使用DataGrip
-- 创建数据库，仅仅代表创建了，并没有切换。
create database mydb;

-- 查询所有的数据库，发现已经有mydb了。
select * from pg_database;

-- 查询当前使用数据库，使用的仍然是postgres
select current_database();

-- 创建表
create table company
(
    id      serial primary key,
    name    text not null,
    age     int  not null,
    address char(50),
    salary  real
);
INSERT INTO COMPANY (NAME,AGE,ADDRESS,SALARY) VALUES ( 'Paul', 32, 'California', 20000.00 );
INSERT INTO COMPANY (NAME,AGE,ADDRESS,SALARY) VALUES ('Allen', 25, 'Texas', 15000.00 );

-- 查询表数据
select * from company;

-- 查询当前数据库的所有表
select * from pg_tables;

-- 删除该表
drop table company;
```

# 第三章 PG 中级知识

## 3.1 用户操作

```sql
-- 创建用户并设置密码
CREATE USER username WITH PASSWORD 'password';

-- 修改用户密码
ALTER USER username WITH PASSWORD 'password';

-- 数据库授权，两行代码
-- 数据库授权,赋予指定账户指定数据库所有权限，dbname代表数据库名称，username代表用户名称
GRANT ALL PRIVILEGES ON DATABASE dbname TO username;
-- 但此时用户还是没有读写权限，需要继续授权表。该sql语句必须在所要操作的数据库里执行
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO username;

-- 移除权限
-- 移除指定账户指定数据库所有权限，dbname代表数据库名称，username代表用户名称
REVOKE ALL PRIVILEGES ON DATABASE dbname from username;
-- 移除所有表的权限。该sql语句必须在所要操作的数据库里执行
REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public from username;

-- 删除用户
drop user test

# 查看用户
\du
```

```sh
[root@linxuanVM ~]# su postgres
bash-4.2$ psql
could not change directory to "/root": Permission denied
psql (14.8)
Type "help" for help.

# 创建用户
postgres=# create user linxuan with password 'linxuan';
CREATE ROLE
# 查看所有用户
postgres=# \du
                                   List of roles
 Role name |                         Attributes                         | Member of 
-----------+------------------------------------------------------------+-----------
 linxuan   |                                                            | {}
 postgres  | Superuser, Create role, Create DB, Replication, Bypass RLS | {}
postgres=# \q

# 以linxuan这个用户登录psql，返现没有办法登录，报错了：linxuan这个用户并没有peer认证的权限
# peer认证：PG所在的操作系统上的用户登陆。只要当前系统用户和要登陆到PostgreSQL的用户名相同，就可以登陆
bash-4.2$ psql -U linxuan
could not change directory to "/root": Permission denied
psql: error: connection to server on socket "/var/run/postgresql/.s.PGSQL.5432" failed: FATAL:  Peer authentication failed for user "linxuan"
bash-4.2$ exit
logout

# 修改文件本地用户登录验证方式为md5
[root@linxuanVM ~]# vim /var/lib/pgsql/14/data/pg_hba.conf 
# "local" is for Unix domain socket connections only
# local   all             all                                     peer
# 复制的一行，修改一下登录验证方式为md5，这样本地登录的时候就是md5方式登录，不会使用peer方式
local   all             all                                     md5
# 重启PG
[root@linxuanVM ~]# systemctl restart postgresql-14


[root@linxuanVM ~]# sudo su - postgres
Last login: Mon Jul 31 17:37:57 CST 2023 on pts/1
# 发现psql也需要密码了
-bash-4.2$ psql
Password for user postgres: 
psql (14.8)
Type "help" for help.
postgres=# \q
# 直接登录会出错，这是因为会默认登录linxuan数据库，但是PG并没有，所以出错
-bash-4.2$ psql -U linxuan
Password for user linxuan: 
psql: error: connection to server on socket "/var/run/postgresql/.s.PGSQL.5432" failed: FATAL:  database "linxuan" does not exist
# 指定用户名和数据库登录
-bash-4.2$ psql -U linxuan -d mydb
Password for user linxuan: 
psql (14.8)
Type "help" for help.
# 虽然登录进去了，但是没有任何权限。新创建的用户是没有任何权限的。
mydb=> select * from company;
ERROR:  permission denied for table company
mydb=> \q

# 使用postgres用户连接PG
-bash-4.2$ psql
Password for user postgres: 
psql (14.8)
Type "help" for help.
# mydb数据库授权
postgres=# grant all privileges on database mydb to linxuan;
GRANT
# 切换至mydb数据库。
postgres=# \c mydb
You are now connected to database "mydb" as user "postgres".
# 该sql语句必须在所要操作的数据库里执行
mydb=# grant all privileges on all tables in schema public to linxuan;
GRANT
# 发现有查询权限了
mydb=# select * from company; 
 id | name | age | address | salary 
----+------+-----+---------+--------
(0 rows)
```

## 3.2 角色管理

在 PostgreSQL 里没有区分用户和角色的概念，`CREATE USER`为`CREATE ROLE`的别名，这两个命令几乎是完全相同的，唯一的区别是`CREATE USER`命令创建的用户默认带有 LOGIN 属性，而`CREATE ROLE`命令创建的用户默认不带LOGIN 属性。

| 属性        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| login       | 只有具有 LOGIN 属性的角色可以用做数据库连接的初始角色名。    |
| superuser   | 数据库超级用户                                               |
| createdb    | 创建数据库权限                                               |
| createrole  | 允许其创建或删除其他普通的用户角色(超级用户除外)             |
| replication | 做流复制的时候用到的一个用户属性，一般单独设定。             |
| password    | 在登录时要求指定密码时才会起作用，比如md5或者password模式，跟客户端的连接认证方式有关 |
| inherit     | 用户组对组员的一个继承标志，成员可以继承用户组的权限特性     |

创建 david 角色和 sandy 用户

```sh
# 默认不带 LOGIN 属性
postgres=# CREATE ROLE david;
CREATE ROLE
# 默认具有 LOGIN 属性
postgres=# CREATE USER sandy;
CREATE ROLE
postgres=# \du
                             List of roles
 Role name |                   Attributes                   | Member of 
-----------+------------------------------------------------+-----------
 david     | Cannot login                                   | {}
 postgres  | Superuser, Create role, Create DB, Replication | {}
 sandy     |                                                | {}

postgres=# SELECT rolname from pg_roles ;
 rolname  
----------
 postgres
 david
 sandy
(3 rows)

# 角色david 创建时没有分配login权限，所以没有创建用户
postgres=# SELECT usename from pg_user;
 usename  
----------
 postgres
 sandy
(2 rows)
```

**创建用户时赋予角色属性**

如果要在创建角色时就赋予角色一些属性，可以使用下面的方法。

```sql
# 创建用户时直接赋予角色属性
postgres=# CREATE ROLE bella CREATEDB ;
CREATE ROLE
# 创建角色 renee 并赋予其创建数据库及带有密码登录的属性
postgres=# CREATE ROLE renee CREATEDB PASSWORD 'abc123' LOGIN;
CREATE ROLE
```

**给已存在用户赋予权限**

```sql
# 赋予登录权限
postgres=# ALTER ROLE bella WITH LOGIN;
ALTER ROLE
# 赋予 renee 创建角色的权限
postgres=# ALTER ROLE renee WITH CREATEROLE;
ALTER ROLE
```

## 3.3 Schema

PostgreSQL 模式（SCHEMA）可以看着是一个表的集合。一个模式可以包含视图、索引、数据类型、函数和操作符等。相同的对象名称可以被用于不同的模式中而不会出现冲突，例如 schema1 和 myschema 都可以包含名为 mytable 的表。

使用模式的优势：

1. 允许多个用户使用一个数据库并且不会互相干扰。
2. 将数据库对象组织成逻辑组以便更容易管理。
3. 第三方应用的对象可以放在独立的模式中，这样它们就不会与其他对象的名称发生冲突。

模式类似于操作系统层的目录，但是模式不能嵌套。

```sql
-- 创建schema
create schema myschema;

-- 创建schema下的表
create table myschema.company
(
    id      int         not null,
    name    varchar(20) not null,
    age     int         not null,
    address char(25),
    salary  decimal(18, 2),
    primary key (id)
);

-- 查询当前数据库所有表，这样会将schema和表都查询出来
select * from pg_tables;

-- 删除schema，因为有其他对象依赖他，所以无法删除
drop schema myschema;

-- 级联删除，删除一个模式以及其中包含的所有对象
DROP SCHEMA myschema CASCADE;
```

## 3.4 备份PG

如果在生产环境中使用 PostgreSQL，请务必采取预防措施以确保用户的数据不会丢失。

PostgreSQL 提供了 pg_dump 实用程序来简化备份单个数据库的过程。必须以对要备份的数据库具有读取权限的用户身份运行此命令。备份格式有几种选择：

1. `*.bak`：压缩二进制格式
2. `*.sql`：明文转储
3. `*.tar`：tarball

默认情况下，PostgreSQL 将忽略备份过程中发生的任何错误。这可能导致备份不完整。要防止这种情况，可以使用 -1 选项运行 pg_dump 命令。 这会将整个备份过程视为单个事务，这将在发生错误时阻止部分备份。

```sh
# 切换Linux用户为postgres
[root@linxuanVM ~]# su postgres
# 进入psql控制台
bash-4.2$ psql
could not change directory to "/root": Permission denied
psql (14.8)
Type "help" for help.

# 查询当前所有的数据库，和select * from pg_database差不多，只是比SQL语句简化了结果
postgres=# \l
                                  List of databases
   Name    |  Owner   | Encoding |   Collate   |    Ctype    |   Access privileges   
-----------+----------+----------+-------------+-------------+-----------------------
 postgres  | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 template0 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres   postgres=CTc/postgres
 template1 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres   postgres=CTc/postgres
(3 rows)

# 创建mydb数据库
postgres=# create database mydb;
CREATE DATABASE
# 切换mydb数据库
postgres=# \c mydb;
You are now connected to database "mydb" as user "postgres".
# 创建表
mydb=# create table company (
mydb(# id  serial primary key,
mydb(# name text not null,
mydb(# age int not null,
mydb(# address char(50),
mydb(# salary real
mydb(# );
CREATE TABLE

# 查询当前数据库所有表 
mydb=# \d
               List of relations
 Schema |      Name      |   Type   |  Owner   
--------+----------------+----------+----------
 public | company        | table    | postgres
 public | company_id_seq | sequence | postgres
(2 rows)
# 查询表数据
mydb=# select * from company;
 id | name | age | address | salary 
----+------+-----+---------+--------
(0 rows)
```

```sh
# su：Shift user切换用户，参数为用户名。如果是root用户切换至其他用户不需要输入密码，否则需要输入密码。
# sudo：Super user do超级用户操作，默认以root用户来执行该命令，也就是授予该命令root权限
# su - postgres：变更帐号为postgres并改变工作目录至postgres的家目录（home dir）
# sudo su - postgres：使用sudo授权切换到postgres用户并改变工作目录至postgres的家目录
[root@linxuanVM ~]# sudo su - postgres
Last login: Mon Jul 31 15:04:32 CST 2023 on pts/0
# 备份数据库
-bash-4.2$ pg_dump mydb > mydb.bak
-bash-4.2$ ll
total 8
drwx------ 4 postgres postgres 4096 Jul 30 16:21 14
-rw-r--r-- 1 postgres postgres 1910 Jul 31 15:10 mydb.bak

# 进入psql干掉mydb数据库
-bash-4.2$ psql
psql (14.8)
Type "help" for help.
postgres=# drop database mydb ;
DROP DATABASE
# 退出psql
postgres=# quit

# 重新进入psql，这是因为想要恢复丢失的数据，需要在其位置创建一个空数据库
-bash-4.2$ psql
psql (14.8)
Type "help" for help.
postgres=# create database mydb;
CREATE DATABASE
postgres=# \q

# 恢复数据库数据
-bash-4.2$ psql -f mydb.bak mydb;
SET
...
ALTER TABLE
```

由于 pg_dump 一次只创建一个数据库的备份，因此它不会存储有关数据库角色或其他群集范围配置的信息。 要存储此信息并同时备份所有数据库，可以使用 pg_dumpall。

```sql
# 创建备份文件
pg_dumpall > pg_backup.bak
# 从备份还原所有的数据库
psql -f pg_backup.bak postgres
```

```sql
# 备份数据库
-- 导出postgres数据库保存为postgres.sql
$ pg_dump -U postgres -f /tmp/postgres.sql postgres
-- 导出postgres数据库中表test的数据
$ pg_dump -U postgres -f /tmp/postgres.sql -t test postgres
-- 导出postgres数据库以tar形式压缩保存为postgres.tar
$ pg_dump -U postgres -F t -f /tmp/postgres.tar postgres

# 恢复数据库
-- 恢复postgres.sql数据到bk01数据库
$ psql -U postgres -f /tmp/postgres.sql bk01 ()
-- pg_restore代表从pg_dump创建的备份文件中恢复PostgreSQL数据库。
-- 恢复postgres.tar数据到bk01数据库
$ pg_restore -U postgres -d bk01 /tmp/postgres.tar
```
