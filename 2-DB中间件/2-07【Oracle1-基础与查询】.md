# 第一章 Oracle基础

ORACLE 数据库系统是美国 ORACLE 公司（甲骨文）提供的以分布式数据库为核心的一组软件产品，是目前最流行的客户 / 服务器（CLIENT / SERVER）或 B / S 体系结构的数据库之一。 

ORACLE 数据库是目前世界上使用最为广泛的数据库管理系统，作为一个通用的数据库系统，它具有完整的数据管理功能；作为一个关系数据库，它是一个完备关系的产品；作为分布式数据库它实现了分布式处理功能。

ORACLE 数据库具有以下特点： 

* 支持多用户、大事务量的事务处理
* 数据安全性和完整性控制
* 支持分布式数据处理
* 可移植性

## 1.1 安装与配置

安装到了Windows Server 2003 版本的虚拟机中，在本地下载了客户端连接工具 SQLPlus 用以连接。 lsnrctl start 

```sh
C:\Windows\system32>E:

# 进入到安装的SQL Plus目录
E:\>cd E:\Oracle\instantclient_12_1

# 运行sqlplus.exe远程连接Oracle。system是用户名称、linxuan是密码、orcl是默认生成的数据库名称（唯一）。
E:\Oracle\instantclient_12_1>sqlplus system/linxuan@192.168.80.10:1521/orcl

SQL*Plus: Release 12.1.0.1.0 Production on Sat Jul 29 16:46:50 2023

Copyright (c) 1982, 2013, Oracle.  All rights reserved.


Connected to:
Oracle Database 10g Enterprise Edition Release 10.2.0.1.0 - Production
With the Partitioning, OLAP and Data Mining options

# 查询所有的表
SQL> select * from tabs;

# 使用quit退出
SQL> quit
Disconnected from Oracle Database 10g Enterprise Edition Release 10.2.0.1.0 - Production
With the Partitioning, OLAP and Data Mining options
```

接下来使用 DataGrip 成功连接了！

```sql
# 设置一下中文编码
# 查询服务器端编码
SQL> select userenv('language') from dual
SIMPLIFIED CHINESE_CHINA.ZHS16GBK
# 计算机->属性->高级系统设置->环境变量->新建:NLS_LANG=SIMPLIFIED CHINESE_CHINA.ZHS16GBK
```

## 1.2 遇到的BUG

**ORA-01034: ORACLE not available、ORA-27101: shared memory realm does not exist**

```sql
-- windows server 2003 使用 sqlplus连接oracle报错
C:\Documents and Settings\Adminstrator> sqlplus system/linxuan
ORA-01034:ORACLE not available 
ORA-27101:shared memory realm does not exist
```

出现 ORA-01034 和 ORA-27101 的原因是多方面的。

* ORA-01034 主要是 oracle 当前的服务不可用，ORA-27101是因为 oracle 没有启动或没有正常启动，共享内存并没有分配给当前实例。所以，通过设置实例名，再用操作系统身份验证的方式，启动数据库。这样数据库就正常启动了，就不会报 ORA-01034 和 ORA-27101 两个启动异常了。
* 可能是登录数据库后，不正常的退出，比如直接关掉窗口，而这时数据库里有未完成的动作，再次登录时就会提示`insufficient privileges`的报错
* 可能是虚拟机的共享内存问题

这里我的是第一种原因，所以输入下面的命令就好了：

```sql
C:\Documents and Settings\Administrator>sqlplus system/linxuan

SQL*Plus: Release 10.2.0.1.0 - Production on 星期一 8月 7 16:35:46 2023
Copyright (c) 1982, 2005, Oracle.  All rights reserved.

ERROR:
ORA-01034: ORACLE not available
ORA-27101: shared memory realm does not exist

-- 先看oracle的监听和oracle的服务是否都启动了。
-- 启动oracle监听
C:\Documents and Settings\Adminstrator> lsnrctl start
-- 手工设置一下oralce的sid，我们的是orcl
C:\Documents and Settings\Adminstrator> set ORACLE_SID=orcl
-- 运行sqlplus，进入环境。但是不登录到数据库服务器，想要以系统管理员身份登录可以使用这种方法 + 下面命令
C:\Documents and Settings\Administrator>sqlplus  /nolog
SQL*Plus: Release 10.2.0.1.0 - Production on 星期一 8月 7 16:40:17 2023
Copyright (c) 1982, 2005, Oracle.  All rights reserved.

-- 管理员身份登录
SQL> conn / as sysdba;
已连接到空闲例程。
-- 启动Oracle数据库
SQL> startup
ORACLE 例程已经启动。

Total System Global Area  612368384 bytes
Fixed Size                  1250452 bytes
Variable Size             264244076 bytes
Database Buffers          343932928 bytes
Redo Buffers                2940928 bytes
数据库装载完毕。
数据库已经打开。
-- 测试一下 发现没有任何问题
SQL> select * from user_tables;
```

亦或是使用下面这个方式

```sql
C:\Documents and Settings\Administrator>sqlplus system/linxuan

SQL*Plus: Release 10.2.0.1.0 - Production on 星期一 8月 7 16:47:17 2023
Copyright (c) 1982, 2005, Oracle.  All rights reserved.

ERROR:
ORA-01034: ORACLE not available
ORA-27101: shared memory realm does not exist

-- 直接以系统管理员身份登录，这种方式比上面的方式简单
-- sys是用户名、/后面是密码，sys登陆后面必须跟上as sysdba。
C:\Documents and Settings\Administrator>sqlplus sys/linxuan as sysdba

SQL*Plus: Release 10.2.0.1.0 - Production on 星期一 8月 7 16:47:26 2023
Copyright (c) 1982, 2005, Oracle.  All rights reserved.
已连接到空闲例程。

-- 启动Oracle数据库
SQL> startup
ORACLE 例程已经启动。

Total System Global Area  612368384 bytes
Fixed Size                  1250452 bytes
Variable Size             268438380 bytes
Database Buffers          339738624 bytes
Redo Buffers                2940928 bytes
数据库装载完毕。
数据库已经打开。

-- 测试一下 发现没有任何问题
SQL> select * from user_tables;
```

## 1.3 Oracle 数据类型

| 类型        | 大小或范围              | 描述                             |
| ----------- | ----------------------- | -------------------------------- |
| CHAR        | 最多存储 2000 个字节    | 固定长度的字符类型               |
| VARCHAR2    | 最多存储 4000 个字节    | 可变长度的字符类型               |
| LONG        | 最大可以存储 2 个 G     | 大文本类型                       |
| NUMBER      |                         | 数值类型                         |
| NUMBER(5)   | 最大可以存的数为 99999  |                                  |
| NUMBER(5,2) | 最大可以存的数为 999.99 |                                  |
| DATE        |                         | 日期时间型，精确到秒             |
| TIMESTAMP   |                         | 精确到秒的小数点后 9 位          |
| CLOB        | 最大可以存 4 个 G       | 存储字符                         |
| BLOB        | 最多可以存 4 个 G       | 存储图像、声音、视频等二进制数据 |

## 1.4 Oracle 体系结构

Oracle 数据库是数据的物理存储，这就包括（数据文件 ORA 或者 DBF、控制文件、联机日志、参数文件）。其实 Oracle 数据库的概念和其它数据库不一样，这里的数据库是一个操作系统只有一个库。可以看作是 Oracle 就只有一个大数据库。

一个 Oracle 实例（Oracle Instance）有一系列的后台进程（Backguound Processes) 和内存结构（Memory Structures）组成。一个数据库可以有 n 个实例。

**数据文件 dbf**

数据文件是数据库的物理存储单位。数据库的数据是存储在表空间中的，真正是在某一个或者多个数据文件中。而一个表空间可以由一个或多个数据文件组成，一个数据文件只能属于一个表空间。一旦数据文件被加入到某个表空间后，就不能删除这个文件，如果要删除某个数据文件，只能删除其所属于的表空间才行。

**表空间**

表空间是 Oracle 对物理数据库上相关数据文件（ORA 或者 DBF 文件）的逻辑映射。一个数据库在逻辑上被划分成一到若干个表空间，每个表空间包含了在逻辑上相关联的一组结构。每个数据库至少有一个表空间（称之为 system 表空间）。

每个表空间由同一磁盘上的一个或多个文件组成，这些文件叫数据文件（datafile）。一个数据文件只能属于一个表空间。

![](D:\Java\笔记\图片\2-07【Oracle基础】\1-1dbf.png)

表的数据，是有用户放入某一个表空间的，而这个表空间会随机把这些表数据放到一个或者多个数据文件中。

由于 oracle 的数据库不是普通的概念，oracle 是有用户和表空间对数据进行管理和存放的。但是表不是有表空间去查询的，而是由用户去查的。因为不同用户可以在同一个表空间建立同一个名字的表！这里区分就是用户了！

**用户**

用户是在表空间下建立的。用户登陆后只能看到和操作自己的表，ORACLE的用户与 MYSQL 的数据库类似，每建立一个应用需要创建一个用户。

![](D:\Java\笔记\图片\2-07【Oracle基础】\1-2system structure.png)

# 第二章 权限、角色与用户管理

# 第三章 基本操作

基本上对于表和数据的增删改的 SQL 语句没有变化。

## 3.1 自来水收费项目

自来水公司为更好地对自来水收费进行规范化管理，决定开发《自来水公司收费系统》。考虑到自来水业务数量庞大， 数据并发量高，决定数据库采用 ORACLE 数据库。主要功能包括：

1. 基础信息管理：业主类型设置、价格设置、区域设置、收费员设置、地址设置
2. 业主信息管理：业主信息维护、业主信息查询
3. 收费管理：抄表登记、收费登记、收费记录查询、欠费用户清单
4. 统计分析：收费日报单、收费月报表 .......

所需要创建的表结构如下：

| 业主类型表（T_OWNERTYPE）字段名 | 类型(位数)   | 是否必填 | 说明     |
| ------------------------------- | ------------ | -------- | -------- |
| ID                              | NUMBER       | 是       | 主键     |
| NAME                            | VARCHAR2(30) | 是       | 类型名称 |

| 身家表（T_PRICETABLE）字段名 | 类型(位数)   | 是否必填 | 说明         |
| ---------------------------- | ------------ | -------- | ------------ |
| ID                           | NUMBER       | 是       | 主键         |
| PRICE                        | NUMBER(10,2) | 是       | 价格         |
| OWNERTYPEID                  | NUMBER       | 是       | 业主类型 ID  |
| MINNUM                       | NUMBER(10,2) | 是       | 区间数开始值 |
| MAXNUM                       | NUMBER(10,2) | 是       | 区间数截止值 |

| 区域表（T_AREA）字段名 | 类型(位数)   | 是否必填 | 说明     |
| ---------------------- | ------------ | -------- | -------- |
| ID                     | NUMBER       | 是       | 主键     |
| NAME                   | VARCHAR2(30) | 是       | 区域名称 |

| 收费员表（T_OPERATOR）字段名 | 类型(位数)   | 是否必填 | 说明       |
| ---------------------------- | ------------ | -------- | ---------- |
| ID                           | NUMBER       | 是       | 主键       |
| NAME                         | VARCHAR2(30) | 是       | 操作员名称 |

| 地址表（T_ADDRESS）字段名 | 类型(位数)   | 是否必填 | 说明      |
| ------------------------- | ------------ | -------- | --------- |
| ID                        | NUMBER       | 是       | 主键      |
| NAME                      | VARCHAR2(30) | 是       | 地址名称  |
| AREAID                    | NUMBER       | 是       | 区域 ID   |
| OPERATORID                | NUMBER       | 是       | 操作员 ID |

| 业主表（T_OWNERS）字段名 | 类型(位数)   | 是否必填 | 说明        |
| ------------------------ | ------------ | -------- | ----------- |
| ID                       | NUMBER       | 是       | 主键        |
| NAME                     | VARCHAR2(30) | 是       | 业主名称    |
| ADDRESSID                | NUMBER       | 是       | 地址 ID     |
| HOUSENUMBER              | VARCHAR2(30) | 是       | 门牌号      |
| WATERMETER               | VARCHAR2(30) | 是       | 水表编号    |
| ADDDATE                  | DATE         | 是       | 登记日期    |
| OWNERTYPEID              | NUMBER       | 是       | 业主类型 ID |

| 收费台账（T_ACCOUNT）字段名 | 类型(位数)   | 是否必填 | 说明       |
| --------------------------- | ------------ | -------- | ---------- |
| ID                          | NUMBER       | 是       | 主键       |
| OWNERID                     | NUMBER       | 是       | 业主编号   |
| OWNERTYPEID                 | NUMBER       | 是       | 业主类型   |
| AREAID                      | NUMBER       | 是       | 所在区域   |
| YEAR                        | CHAR(4)      | 是       | 账务年份   |
| MONTH                       | CHAR(2)      | 是       | 账务月份   |
| NUM0                        | NUMBER       |          | 上月累计数 |
| NUM1                        | NUMBER       |          | 本月累计数 |
| USENUM                      | NUMBER       |          | 本月使用数 |
| METERUSERID                 | NUMBER       |          | 抄表员     |
| METERDATE                   | DATE         |          | 抄表日期   |
| MONEY                       | NUMBER(10,2) |          | 应缴金额   |
| ISFEE                       | CHAR(1)      | 是       | 是否缴费   |
| FEEDATE                     | DATE         |          | 缴费日期   |
| FEEUSERID                   | NUMBER       |          | 收费员     |

![](D:\Java\笔记\图片\2-07【Oracle基础】\1-3.png)

### 3.1.1 创建系统表空间

```sql
-- 创建表空间。
-- waterboss为表空间名称、datafile用于设置物理文件名称(没必要一定和表空间名称相同)
-- size用于设置表空间的初始大小、autoextend on用于设置自动增长，如果存储量超过初始大小，则开始自动扩容
-- next 用于设置扩容的空间大小
create tablespace waterboss datafile 'c:\waterboss.dbf' size 100m autoextend on next 10m ;
```

### 3.1.2 创建用户并授权

```sql
-- 创建wateruser用户，密码为linxuan，默认表空间为waterboss
create user wateruser identified by linxuan default tablespace waterboss;
-- 虽然创建好用户了，但是仍然无法登录。这里给用户wateruser授予DBA权限后就可以登录了
grant dba to wateruser;
```

### 3.1.3 基础表创建

```sql
-- 建立价格区间表
create  table t_pricetable
(
    id number primary key,
    price number(10,2),
    ownertypeid number,
    minnum number,
    maxnum number
);

-- 业主类型
create table t_ownertype
(
    id number primary key,
    name varchar2(30)
);

-- 业主表
create table t_owners
(
    id number primary key,
    name varchar2(30),
    addressid number,
    housenumber varchar2(30),
    watermeter varchar2(30),
    adddate date,
    ownertypeid number
);

-- 区域表
create table t_area
(
    id number,
    name varchar2(30)
);

-- 收费员表
create table t_operator
(
    id number,
    name varchar2(30)
);


-- 地址表
create table t_address
(
    id number primary key,
    name varchar2(100),
    areaid number,
    operatorid number
);


-- 账务表--
create table t_account
(
    id number primary key,
    owneruuid number,
    ownertype number,
    areaid number,
    year char(4),
    month char(2),
    num0 number,
    num1 number,
    usenum number,
    meteruser number,
    meterdate date,
    money number(10,2),
    isfee char(1),
    feedate date,
    feeuser number
);

-- sequence是Oracle中的序列，可以实现字段的自增或自减效果
create sequence seq_account;

-- 业主类型
insert into t_ownertype values(1,'居民');
insert into t_ownertype values(2,'行政事业单位');
insert into t_ownertype values(3,'商业');

-- 地址信息--
insert into t_address values( 1,'明兴花园',1,1);
insert into t_address values( 2,'鑫源秋墅',1,1);
insert into t_address values( 3,'华龙苑南里小区',2,2);
insert into t_address values( 4,'河畔花园',2,2);
insert into t_address values( 5,'霍营',2,2);
insert into t_address values( 6,'回龙观东大街',3,2);
insert into t_address values( 7,'西二旗',3,2);

-- 业主信息
insert into t_owners values(1,'范冰',1,'1-1','30406',to_date('2015-04-12','yyyy-MM-dd'),1 );
insert into t_owners values(2,'王强',1,'1-2','30407',to_date('2015-02-14','yyyy-MM-dd'),1 );
insert into t_owners values(3,'马腾',1,'1-3','30408',to_date('2015-03-18','yyyy-MM-dd'),1 );
insert into t_owners values(4,'林小玲',2,'2-4','30409',to_date('2015-06-15','yyyy-MM-dd'),1 );
insert into t_owners values(5,'刘华',2,'2-5','30410',to_date('2013-09-11','yyyy-MM-dd'),1 );
insert into t_owners values(6,'刘东',2,'2-2','30411',to_date('2014-09-11','yyyy-MM-dd'),1 );
insert into t_owners values(7,'周健',3,'2-5','30433',to_date('2016-09-11','yyyy-MM-dd'),1 );
insert into t_owners values(8,'张哲',4,'2-2','30455',to_date('2016-09-11','yyyy-MM-dd'),1 );
insert into t_owners values(9,'昌平区中西医结合医院',5,'2-2','30422',to_date('2016-10-11','yyyy-MM-dd'),2 );
insert into t_owners values(10,'美廉美超市',5,'4-2','30423',to_date('2016-10-12','yyyy-MM-dd'),3 );


-- 操作员
insert into t_operator values(1,'马小云');
insert into t_operator values(2,'李翠花');

-- 地区--
insert into t_area values(1,'海淀');
insert into t_area values(2,'昌平');
insert into t_area values(3,'西城');
insert into t_area values(4,'东城');
insert into t_area values(5,'朝阳');
insert into t_area values(6,'玄武');

-- 价格表--
insert into t_pricetable values(1,2.45,1,0,5);
insert into t_pricetable values(2,3.45,1,5,10);
insert into t_pricetable values(3,4.45,1,10,null);
insert into t_pricetable values(4,3.87,2,0,5);
insert into t_pricetable values(5,4.87,2,5,10);
insert into t_pricetable values(6,5.87,2,10,null);
insert into t_pricetable values(7,4.36,3,0,5);
insert into t_pricetable values(8,5.36,3,5,10);
insert into t_pricetable values(9,6.36,3,10,null);

-- 账务表--
insert into t_account values( seq_account.nextval,1,1,1,'2012','01',30203,50123,0,1,sysdate,34.51,'1',to_date('2012-02-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','02',50123,60303,0,1,sysdate,23.43,'1',to_date('2012-03-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','03',60303,74111,0,1,sysdate,45.34,'1',to_date('2012-04-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','04',74111,77012,0,1,sysdate,52.54,'1',to_date('2012-05-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','05',77012,79031,0,1,sysdate,54.66,'1',to_date('2012-06-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','06',79031,80201,0,1,sysdate,76.45,'1',to_date('2012-07-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','07',80201,88331,0,1,sysdate,65.65,'1',to_date('2012-08-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','08',88331,89123,0,1,sysdate,55.67,'1',to_date('2012-09-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','09',89123,90122,0,1,sysdate,112.54,'1',to_date('2012-10-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','10',90122,93911,0,1,sysdate,76.21,'1',to_date('2012-11-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','11',93911,95012,0,1,sysdate,76.25,'1',to_date('2012-12-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,1,1,1,'2012','12',95012,99081,0,1,sysdate,44.51,'1',to_date('2013-01-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','01',30334,50433,0,1,sysdate,34.51,'1',to_date('2013-02-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','02',50433,60765,0,1,sysdate,23.43,'1',to_date('2013-03-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','03',60765,74155,0,1,sysdate,45.34,'1',to_date('2013-04-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','04',74155,77099,0,1,sysdate,52.54,'1',to_date('2013-05-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','05',77099,79076,0,1,sysdate,54.66,'1',to_date('2013-06-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','06',79076,80287,0,1,sysdate,76.45,'1',to_date('2013-07-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','07',80287,88432,0,1,sysdate,65.65,'1',to_date('2013-08-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','08',88432,89765,0,1,sysdate,55.67,'1',to_date('2013-09-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','09',89765,90567,0,1,sysdate,112.54,'1',to_date('2013-10-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','10',90567,93932,0,1,sysdate,76.21,'1',to_date('2013-11-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','11',93932,95076,0,1,sysdate,76.25,'1',to_date('2013-12-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,2,1,3,'2012','12',95076,99324,0,1,sysdate,44.51,'1',to_date('2014-01-14','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,100,1,3,'2012','12',95076,99324,0,1,sysdate,44.51,'1',to_date('2014-01-01','yyyy-MM-dd'),2 );
insert into t_account values( seq_account.nextval,101,1,3,'2012','12',95076,99324,0,1,sysdate,44.51,'1',to_date('2015-01-01','yyyy-MM-dd'),2 );

update t_account set usenum=num1-num0;
update t_account set money=usenum*2.45;
commit;
```

## 3.2 表和数据操作

事务是一组操作的集合，事务会把所有操作作为一个整体一起向系统提交或撤销操作请求，即这些操作要么同时成功，要么同时失败。

Oracle数据库默认是手动提交事务，需要先开启事务，然后再提交。因此每一次对数据进行增删改的时候都要手动`commit`提交。另外，Oracle的事务隔离级别是RC`read committed`读已提交，产生的问题有不可重复读、幻读。

**表的操作**

```sql
-- 创建表
CREATE TABLE 表名称(
    字段名 类型(长度) primary key,
    字段名 类型(长度),
    .......
);
```

```sql
-- 增加字段
ALTER TABLE 表名称 ADD(列名 1 类型 [DEFAULT 默认值], 列名 1 类型 [DEFAULT 默认值]...)
-- 修改字段语法1
ALTER TABLE 表名称 MODIFY(列名 1 类型 [DEFAULT 默认值], 列名 1 类型 [DEFAULT 默认值]...)
-- 修改字段语法2
ALTER TABLE 表名称 RENAME COLUMN 原列名 TO 新列名
-- 删除单个字段
ALTER TABLE 表名称 DROP COLUMN 列名
-- 删除多个字段
ALTER TABLE 表名称 DROP (列名 1,列名 2...)
```

```sql
-- 删除表
DROP TABLE 表名称
```

```sql
-- 查询所有表
select * from tabs;
```

**数据操作**

```sql
-- 插入数据，记得手动提交
INSERT INTO 表名[(列名 1，列名 2，...)] VALUES(值 1，值 2，...);
-- 新增数据，如果是varchar2类型需要使用单引号，日期可以使用sysdate变量来代替
insert into T_OWNERS VALUES (1, '张三丰', 1, '2-2', '5678', sysdate, 1);
```

```sql
-- 修改数据，记得手动提交
UPDATE 表名 SET 列名1=值1, 列名2=值2,.... WHERE 修改条件;
-- 修改日期，可以直接算数操作
update T_OWNERS set adddate=adddate - 3 where id = 1;
```

```sql
-- 删除数据，记得手动提交
DELETE FROM 表名 WHERE 删除条件;
-- 直接干掉原先的表然后新创建一个表，两个本质不一样
TRUNCATE TABLE 表名称;
```

## 3.3 JDBC 连接

**Maven导入依赖**

maven中没有Oracle的jar包，所以需要我们自己导入。

1. 从虚拟机 Oracle 安装目录 `C:\oracle\product\10.2.0\db_1\jdbc\lib` 获取连接 jar 包`ojdbc14.jar`并报存至本地，这里我们保存到`E:\Oracle\jdbc`目录。

2. 查找当前 Oracle 版本，在之后的 Maven 命令中会用到。

   ```sql
   -- 查看当前Oracle版本
   SQL> select * from v$version;
   
   BANNER
   ----------------------------------------------------------------
   Oracle Database 10g Enterprise Edition Release 10.2.0.1.0 - Prod
   PL/SQL Release 10.2.0.1.0 - Production
   CORE    10.2.0.1.0      Production
   TNS for 32-bit Windows: Version 10.2.0.1.0 - Production
   NLSRTL Version 10.2.0.1.0 - Production
   ```

3. 在本地保存`ojdbc14.jar`的位置打开 CMD 命令窗口

   ```sh
   # maven安装jar包命令
   # -Dfile="jar包的绝对路径"、-Dpackaging="文件打包方式"、
   E:\Oracle\jdbc>mvn install:install-file -Dfile=ojdbc14.jar -Dpackaging=jar -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.1.0
   [INFO] Scanning for projects...
   [INFO]
   [INFO] ------------------< org.apache.maven:standalone-pom >-------------------
   [INFO] Building Maven Stub Project (No POM) 1
   [INFO] --------------------------------[ pom ]---------------------------------
   [INFO]
   [INFO] --- maven-install-plugin:2.4:install-file (default-cli) @ standalone-pom ---
   [INFO] Installing E:\Oracle\jdbc\ojdbc14.jar to E:\Maven\apache-maven-3.6.0\mvn_repository\com\oracle\ojdbc14\10.2.0.1.0\ojdbc14-10.2.0.1.0.jar
   [INFO] Installing C:\Users\林轩\AppData\Local\Temp\mvninstall8011458702097400886.pom to E:\Maven\apache-maven-3.6.0\mvn_repository\com\oracle\ojdbc14\10.2.0.1.0\ojdbc14-10.2.0.1.0.pom
   [INFO] ------------------------------------------------------------------------
   [INFO] BUILD SUCCESS
   [INFO] ------------------------------------------------------------------------
   [INFO] Total time:  0.611 s
   [INFO] Finished at: 2023-07-30T14:24:53+08:00
   [INFO] ------------------------------------------------------------------------
   ```

4. Maven 导入依赖

   ```xml
   <dependencies>
       <dependency>
           <groupId>com.oracle</groupId>
           <artifactId>ojdbc14</artifactId>
           <version>10.2.0.1.0</version>
       </dependency>
   </dependencies>
   ```

**创建工具类**

```java
/**
 * JDBC连接Oracle工具类
 */
public class JDBCUtils {

    // 加载驱动
    static {
        try {
            // JDBC连接Oracle驱动为oracle.jdbc.OracleDriver
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 返回连接对象
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return java.sql.DriverManager.getConnection(
            "jdbc:oracle:thin:@192.168.80.10:1521:orcl",
            "wateruser", "linxuan");
    }

    /**
     * 关闭资源
     *
     * @param rs 返回的结果集
     * @param stmt SQL执行对象
     * @param conn 连接对象
     */
    public static void closeAll(ResultSet rs,
                                Statement stmt,
                                Connection conn) {
        // 关闭结果集
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 关闭执行对象
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 关闭连接对象
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

**导入项目依赖**

```xml
<dependencies>
    <!-- JDBC连接Oracle -->
    <dependency>
        <groupId>com.oracle</groupId>
        <artifactId>ojdbc14</artifactId>
        <version>10.2.0.1.0</version>
    </dependency>
    <!-- Lombok依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.28</version>
    </dependency>
    <!-- Junit单元测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**创建实体类**

```java
@Data
public class Owners {
    // 编号
    private Long id;
    // 业主名称
    private String name;
    // 地址编号
    private Long addressid;
    // 门牌号
    private String housenumber;
    // 水表编号
    private String watermeter;
    // 登记日期
    private Date adddate;
    // 业主类型ID
    private Long ownertypeid;
}
```

**编写业务代码**

```java
public class OwnersDao {

    /**
     * 查询业主
     */
    public void getAllOwners() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "select * from T_OWNERS";

        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                System.out.println("resultSet.getInt(\"id\") = " + resultSet.getLong("ID"));
                System.out.println("resultSet.getString(\"name\") = " + resultSet.getString("NAME"));
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(null, preparedStatement, connection);
        }
    }

    /**
     * 新增业主
     */
    public void addOwners(Owners owners) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "insert into T_OWNERS values ( ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, owners.getId());
            stmt.setString(2, owners.getName());
            stmt.setLong(3, owners.getAddressid());
            stmt.setString(4, owners.getHousenumber());
            stmt.setString(5, owners.getWatermeter());
            // 这里导入的是java.sql.Data
            stmt.setDate(6, new Date(owners.getAdddate().getTime()));
            stmt.setLong(7, owners.getOwnertypeid());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(null, stmt, conn);
        }
    }

    /**
     * 修改业主
     */
    public void updateOwners(Owners owners) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "update T_OWNERS set name=?, addressid=?, housenumber=?, " +
                "watermeter=?, adddate=?, ownertypeid=? where id=? ";
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, owners.getName());
            stmt.setLong(2, owners.getAddressid());
            stmt.setString(3, owners.getHousenumber());
            stmt.setString(4, owners.getWatermeter());
            // 这里导入的是java.sql.Data
            stmt.setDate(5, new Date(owners.getAdddate().getTime()));
            stmt.setLong(6, owners.getOwnertypeid());
            stmt.setLong(7, owners.getId());

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(null, stmt, conn);
        }
    }

    /**
     * 删除业主
     */
    public void deleteOwnersById(Long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "delete from T_OWNERS where id=?";
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(null, stmt, conn);
        }
    }
}
```

**编写测试代码**

```java
public class OwnersDaoTest {

    private static final OwnersDao ownersDao = new OwnersDao();
    private final Owners owners = new Owners();

    /**
     * 测试查询所有业主
     */
    @Test
    public void testGetAllOwners() {
        ownersDao.getAllOwners();
    }

    /**
     * 测试新增业主
     */
    @Test
    public void testAddOwners() {
        owners.setId(2L).setName("林炫").setAddressid(1L)
                .setHousenumber("1-2").setAdddate(new Date())
                .setOwnertypeid(1L);
        ownersDao.addOwners(owners);
    }

    /**
     * 测试修改业主
     */
    @Test
    public void testUpdateOwners() {
        owners.setId(2L).setName("陈沐阳").setAddressid(1L)
                .setHousenumber("1-2").setAdddate(new Date())
                .setOwnertypeid(1L);
        ownersDao.updateOwners(owners);
    }

    /**
     * 测试删除业主
     */
    @Test
    public void testDeleteOwnersById() {
        ownersDao.deleteOwnersById(2L);
    }
}
```

## 3.4 数据导出导入

当我们使用一个数据库时，总希望数据库的内容是可靠的、正确的，但由于计算机系统的故障（硬件故障、软件故障、网络故障、进程故障和系统故障）影响数据库系统的操作，影响数据库中数据的正确性，甚至破坏数据库，使数据库中全部或部分数据丢失。因此当发生上述故障后，希望能重构这个完整的数据库该处理称为数据库恢复，而要进行数据库的恢复必须要有数据库的备份工作。

**整库导出与导入**

```sh
# 这些导出与导入命令在安装Oracle的虚拟机的CMD命令窗口执行，不要在SQLPlus中执行。
# 整库导出命令exp(export)，添加参数 full=y 就是整库导出。执行命令后默认生成EXPDAT.DMP备份文件。
C:\Documents and Settings\Adminstrator> exp system/linxuan full=y
# 指定备份文件的名称，添加 file 参数
C:\Documents and Settings\Adminstrator> exp system/itcast file=文件名 full=y
# 整库导入命令imp(import)，如果不指定 file 参数，则默认用备份文件 EXPDAT.DMP 进行导入
C:\Documents and Settings\Adminstrator> imp system/itcast full=y
```

**按用户导出导入**

```sh
# 这些导出与导入命令在安装Oracle的虚拟机的CMD命令窗口执行，不要在SQLPlus中执行。
# 按照用户导出
C:\Documents and Settings\Adminstrator> exp system/linxuan owner=wateruser file=wateruser.dmp
# 按照用户导入
C:\Documents and Settings\Adminstrator> imp system/itcast file=wateruser.dmp fromuser=wateruser
```

**按表导出与导入**

```sh
# 这些导出与导入命令在安装Oracle的虚拟机的CMD命令窗口执行，不要在SQLPlus中执行。
# 按照表导出。用 tables 参数指定需要导出的表，如果有多个表用逗号分割即可
C:\Documents and Settings\Adminstrator> exp wateruser/linxuan file=a.dmp tables=t_account,a_area
# 按表导入
C:\Documents and Settings\Adminstrator> imp wateruser/itcast file=a.dmp tables=t_account,a_area
```

## 3.5 Oracle 事务

Oracle 支持 SQL92 标准的 READ_COMMITED、SERIALIZABLE，自身特有的 READ_ONLY。

|   Oracle 事务   |               描述               |
| :-------------: | :------------------------------: |
| NO_TRANSACTION  |            不支持事务            |
| READ_UNCOMMITED |    允许脏读、不可重复读、幻读    |
|  READ_COMMITED  | 允许不可重复读、幻读，不允许脏读 |
|   REPEATABLE    | 允许幻读，不允许脏读、不可重复读 |
|  SERIALIZABLE   |  脏读、不可重复读、幻读都不允许  |

## 3.6 Oracle 约束

在 Oracle 中，数据完整性可以使用约束、触发器、应用程序（过程、函数）三种方法来实现，在这三种方法中，因为约束易于维护，并且具有最好的性能，所以作为维护数据完整性的首选。

约束分为列级约束和表级约束：

* 列级约束：列级定义是在定义列的同时定义约束；

  ```sql
  column [CONSTRAINT constraint_name] constraint_type
  ```

* 表级约束：表级定义是指在定义了所有列后，再定义约束，这里需要注意，not null约束只能在列级上定义；

  ```sql
  column ,...,
  [CONSTRAINT constraint_name] constraint_type (column,...)
  ```

# 第四章 查询操作

## 4.1 单表查询

**基本查询**

```sql
-- 查询业主名称包含“刘”的或者门牌号包含 5 的业主记录，并且地址编号为 3 的记录。
-- 因为 and 的优先级比 or 大，所以我们需要用 () 来改变优先级。
select * from T_OWNERS where (NAME like '%刘%' or HOUSENUMBER like '%5%') and ADDRESSID = 3;
-- 查询台账记录中用水字数大于等于 10000，并且小于等于20000 的记录 用 between .. and ..来实现
select * from T_ACCOUNT where USENUM between 1000 and 20000;
```

```sql
-- 查询业主表中的地址ID并且去掉重复值
select distinct addressid from T_OWNERS;
```

**基于伪列的查询**

在 Oracle 的表的使用过程中，实际表中还有一些附加的列，称为伪列。伪列就像表中的列一样，但是在表中并不存储。伪列只能查询，不能进行增删改操作。接下来学习两个伪列：ROWID 和 ROWNUM。

* ROWID：表中的每一行在数据文件中都有一个物理地址，ROWID 伪列返回的就是该行的物理地址。使用 ROWID 可以快速的定位表中的某一行。ROWID 值可以唯一的标识表中的一行。由于 ROWID 返回的是该行的物理地址，因此使用ROWID可以显示行是如何存储的。

  ```sql
  -- 查询区域表的数据和ROWID
  select ROWID,t.* from T_AREA t;
  -- 根据ROWID直接查询数据，特别快
  select ROWID, t.* from T_AREA t where ROWID = 'AAAM11AAGAAAAEvAAA';
  ```

* ROWNUM：在查询的结果集中，ROWNUM 为结果集中每一行标识一个行号，第一行返回1，第二行返回 2，以此类推。通过 ROWNUM 伪列可以限制查询结果集中返回的行数。分页查询需要用到此伪列。

  ```sql
  -- 查询每一行的行号
  select rownum, t.* from T_OWNERTYPE t;
  ```

**聚合统计**

| 常用聚合函数 |     含义     |
| :----------: | :----------: |
|    sum()     |     求和     |
|    avg()     |    求平均    |
|    max()     |   求最大值   |
|    min()     |   求最小值   |
|   count()    | 统计记录个数 |

## 4.2 连接查询

Oracle 中的连接查询有隐式内连接查询、显式内连接查询、左外连接查询、右外连接查询。

**隐式内连接查询**

```sql
-- 使用隐式内连接查询。查询显示业主编号，业主名称，业主类型名称
select o.id 业主编号, o.NAME 业主名称, ot.NAME 业主类型
from T_OWNERS o,
     T_OWNERTYPE ot
where o.OWNERTYPEID = ot.ID;
```

**显式内连接查询**

```sql
-- 使用显式内连接查询。查询显示业主编号，业主名称，业主类型名称
select o.id 业主编号, o.NAME 业主名称, ot.NAME 业主类型
from T_OWNERS o
[inner] join T_OWNERTYPE ot on o.OWNERTYPEID = ot.ID;
```

**左外连接查询**

```sql
-- 查询业主的账务记录，显示业主编号、名称、年、月、金额。如果此业主没有账务记录也要列出姓名。下面两个都可以查
-- 按照 SQL1999 标准的语法
select ow.ID 业主编号, ow.name 业主名称, ac.YEAR 年, ac.MONTH 月, ac.MONEY 金额
from T_OWNERS ow
         left join T_ACCOUNT ac on ow.ID = ac.OWNERUUID;

-- 按照 ORACLE 的方言。如果是左外连接，就在右表所在的条件一端填上（+）
select ow.ID 业主编号, ow.name 业主名称, ac.YEAR 年, ac.MONTH 月, ac.MONEY 金额
from T_OWNERS ow,
     T_ACCOUNT ac
where ow.id = ac.OWNERUUID(+);
```

**右外连接查询**

```sql
-- 查询业主的账务记录，显示业主编号、名称、年、月、金额。如果账务记录没有对应的业主信息，也要列出记录。
-- 使用 SQL1999 标准的语法
select ow.id, ow.name, ac.year, ac.month, ac.money
from T_OWNERS ow
         right join T_ACCOUNT ac on ow.id = ac.owneruuid;
-- Oracle方言
select distinct ow.ID, ow.NAME, ac.year, ac.MONTH, ac.money
from T_ACCOUNT ac,
     T_OWNERS ow
where ac.OWNERUUID = ow.id(+);
```

## 4.3 分页查询

**简单分页查询**

在 ORACLE 进行分页查询，需要用到伪列 ROWNUM 和嵌套查询

```sql
-- 显示前十条记录，可以查询出来
select rownum, t.* from T_ACCOUNT t where rownum <= 10;
-- 查询第11条到第20条记录，无法查询出来
select rownum, t.* from T_ACCOUNT t where rownum >10 and rownum <= 20;
```

查询第 11 条到第 20 条记录的时候数据无法查询出来，这是因为 rownum 是在查询语句扫描每条记录时产生的，所以不能使用`>`符号，只能使用`<`或`<=` ，只用`=`也不行。我们可以使用嵌套子查询来实现

```sql
-- 使用嵌套查询方式查询第11条到第20条数据
select * from (select rownum r, t.* from T_ACCOUNT t where rownum <= 20) where r > 10;
```

**基于排序分页**

```sql
-- 按使用字数降序排序，分页查询台账表 T_ACCOUNT，每页 10 条记录
select *
from (select ROWNUM r, ret.*
      from (select * from T_ACCOUNT order by USENUM desc) re1
      where ROWNUM < 20)
where r > 10;

-- 按使用字数降序排序
select * from T_ACCOUNT order by USENUM desc
-- 根据倒叙的字数查询台账表并显示前20条记录。这里注意查询的结果ROWNUM需要重命名，否则之后不知道按哪个ROWNUM查
select ROWNUM r, ret1.*
from (select * from T_ACCOUNT order by USENUM desc) ret1
where ROWNUM < 20
-- 按使用字数降序排序，分页查询台账表 T_ACCOUNT，查询11~20条记录
select *
from (select ROWNUM r, ret.*
      from (select * from T_ACCOUNT order by USENUM desc) re1
      where ROWNUM < 20)
where r > 10;
```

## 4.4 单行函数

所谓单行函数就是给一个值 / 多个值，最后返回一个值。

由于 Oracle 严格执行 SQL 语法标准，所以不允许`select length('ABCD')`这样的存在，必须在后面添加上 `from 表名`。但是如果随意找一个表来查询这样会查询出来多行的数据，所以 Oracle 提供了一个伪表 dual 供我们进行函数调用查询。

```sql
-- 看一下伪表，可以看到仅仅是一行一列的数据，数据为X
SQL> select * from dual;

D
-
X
```

**字符函数**

|   函数    |              说明              |
| :-------: | :----------------------------: |
|   ASCII   |     返回对应字符的十进制值     |
|    CHR    |       给出十进制返回字符       |
|  CONCAT   |  拼接两个字符串，与 \|\| 相同  |
|  INITCAT  |  将字符串的第一个字母变为大写  |
|   INSTR   |      找出某个字符串的位置      |
|  INSTRB   |  找出某个字符串的位置和字节数  |
|  LENGTH   |     以字符给出字符串的长度     |
|  LENGTHB  |     以字节给出字符串的长度     |
|   LOWER   |       将字符串转换成小写       |
|   LPAD    | 使用指定的字符在字符的左边填充 |
|   LTRIM   |     在左边裁剪掉指定的字符     |
|   RPAD    | 使用指定的字符在字符的右边填充 |
|   RTRIM   | 使用指定的字符在字符的右边填充 |
|  REPLACE  |      执行字符串搜索和替换      |
|  SUBSTR   |         取字符串的子串         |
|  SUBSTRB  |    取字符串的子串（以字节）    |
|  SOUNDEX  |       返回一个同音字符串       |
| TRANSLATE |      执行字符串搜索和替换      |
|   TRIM    |    裁剪掉前面或后面的字符串    |
|   UPPER   |        将字符串变为大写        |

```sql
-- 计算字符串长度
SQL> select length('ABCD') from dual;
LENGTH('ABCD')
--------------
             4
-- 截取字符串             
SQL> select substr('ABCD', 2, 2) from dual;
SU
--
BC
-- 拼接字符串
SQL> select concat('ABCD', 'E') from dual;
CONCA
-----
ABCDE
-- 使用拼接符号来拼接字符串
SQL> select 'AB'||'CD'||'EF' from dual;

'AB'||
------
ABCDEF
```

**数值函数**

|         **函数**          |                  **说明**                   |
| :-----------------------: | :-----------------------------------------: |
|        ABS(value)         |                   绝对值                    |
|        CEIL(value)        |        大于或等于  value 的最小整数         |
|        COS(value)         |                    余弦                     |
|        COSH(value)        |                   反余弦                    |
|        EXP(value)         |               e 的 value 次幂               |
|       FLOOR(value)        |        小于或等于  value 的最大整数         |
|         LN(value)         |              value 的自然对数               |
|        LOG(value)         |          value 的以 10  为底的对数          |
|    MOD(value,divisor)     |                    求模                     |
|   POWER(value,exponent)   |           value 的 exponent 次幂            |
|  ROUND(value,precision)   |        按 precision 精度 4 舍  5 入         |
|        SIGN(value)        | value 为正返回 1；为负返回-1；为 0 返回 0。 |
|        SIN(value)         |                    余弦                     |
|        SINH(value)        |                   反余弦                    |
|        SQRT(value)        |               value 的平方根                |
|        TAN(value)         |                    正切                     |
|        TANH(value)        |                   反正切                    |
| TRUNC(value,按 precision) |          按照 precision 截取 value          |
|       VSIZE(value)        |     返回 value 在 ORACLE 的存储空间大小     |

**日期函数**

| SQRT(value)               | value 的平方根                                               |
| ------------------------- | ------------------------------------------------------------ |
| TAN(value)                | 正切                                                         |
| TANH(value)               | 反正切                                                       |
| TRUNC(value,按 precision) | 按照 precision 截取 value                                    |
| VSIZE(value)              | 返回 value 在 ORACLE 的存储空间大小                          |
| other’)                   |                                                              |
| ROUND(date,’format’)      | 未指定 format 时，如果日期中的时间在中午之前，则将日期中的时间截断为 12 A.M.(午夜，一天的开始),否  则进到第二天。时间截断为 12 A.M.(午夜，一天的开始),     否则进到第二天。 |
| TRUNC(date,’format’)      | 未指定 format 时，将日期截为 12 A.M.( 午夜，一天的     开始). |

**转换函数**

|     函 数      |              描   述               |
| :------------: | :--------------------------------: |
|  CHARTOROWID   |     将 字符转换到  rowid 类型      |
|    CONVERT     |   转换一个字符节到另外一个字符节   |
|    HEXTORAW    |      转换十六进制到  raw 类型      |
|    RAWTOHEX    |        转换 raw 到十六进制         |
|  ROWIDTOCHAR   |         转换 ROWID 到字符          |
|    TO_CHAR     |        转换日期格式到字符串        |
|    TO_DATE     | 按照指定的格式将字符串转换到日期型 |
|  TO_MULTIBYTE  |      把单字节字符转换到多字节      |
|   TO_NUMBER    |        将数字字串转换到数字        |
| TO_SINGLE_BYTE |         转换多字节到单字节         |

**其它函数**

| 其他函数 | 描述                                                         |
| -------- | ------------------------------------------------------------ |
| NVL      | NVL（检测的值，如果为 null 显示的值）；空值处理函数 NVL      |
| NVL2     | NVL2（检测的值，如果不为 null 显示的值，如果为 null 显示的值）； |
| decode   | decode(条件,值 1,翻译值 1,值 2,翻译值 2,...值 n,翻译值n,缺省值)。根据条件返回相应值 |

```sql
-- 使用decode函数查询
select name,decode( ownertypeid,1,' 居民',2,' 行政事业单位',3,'商业') as 类型 from T_OWNERS
-- 可以换成case when then语句实现
select name ,(case ownertypeid
                when 1 then '居民'
                when 2 then '行政事业单位'
                when 3 then '商业' 
                else '其它'
            end
) from T_OWNERS
```

## 4.5 行列转换

**按月份统计 2012 年各个地区的水费**

```sql
-- 查询每个区域的缴费金额
select sum(money) from T_ACCOUNT where YEAR = '2012' group by areaid;
-- 使用嵌套子查询添加上区域列
select (select name from T_AREA where id = AREAID) 区域, sum(money) 年度总缴费 from T_ACCOUNT where YEAR = '2012' group by areaid;
-- 修改总金额的列为一月月份
select (select name from T_AREA where id = AREAID) 区域,
       sum(case when MONTH='01' then money else 0 end) 一月
from T_ACCOUNT
where YEAR = '2012'
group by areaid;
-- 展示所有的月份金额
select (select name from T_AREA where id = AREAID) 区域,
       sum(case when MONTH='01' then money else 0 end) 一月,
       sum(case when MONTH='02' then money else 0 end) 二月,
       sum(case when MONTH='03' then money else 0 end) 三月,
       sum(case when MONTH='04' then money else 0 end) 四月,
       sum(case when MONTH='05' then money else 0 end) 五月,
       sum(case when MONTH='06' then money else 0 end) 六月,
       sum(case when MONTH='07' then money else 0 end) 七月,
       sum(case when MONTH='08' then money else 0 end) 八月,
       sum(case when MONTH='09' then money else 0 end) 九月,
       sum(case when MONTH='10' then money else 0 end) 十月,
       sum(case when MONTH='11' then money else 0 end) 十一月,
       sum(case when MONTH='12' then money else 0 end) 十二月
from T_ACCOUNT
where YEAR = '2012'
group by areaid;
```

## 4.6 分析函数

这里的分析函数主要用于排名使用！下图为三种排名方式的举例

| 分数 | 值相同，排名相同，序号跳跃 | 值相同，排名相同，序号连续 | 不论值是否相同，序号连续 |
| :--: | :------------------------: | :------------------------: | :----------------------: |
| 100  |             1              |             1              |            1             |
|  98  |             2              |             2              |            2             |
|  98  |             2              |             2              |            3             |
|  98  |             2              |             2              |            4             |
|  96  |             5              |             3              |            5             |
|  96  |             5              |             3              |            6             |
|  95  |             7              |             4              |            7             |
|  92  |             8              |             5              |            8             |
|  90  |             9              |             6              |            9             |
|  90  |             9              |             6              |            10            |
|  89  |             11             |             7              |            11            |

三个函数为：

* 值相同，排名相同，序号跳跃。函数为`RANK()`
* 值相同，排名相同，序号连续。函数为`DENSE_RANK()`
* 不论值是否相同，序号连续。函数为`ROW_NUMBER()`

```sql
-- 值相同，排名相同，序号跳跃查询。over里面写排序方式，rank() over (order by USENUM desc)是整体
select rank() over (order by USENUM desc) 排名, USENUM from T_ACCOUNT;
-- 值相同，排名相同，序号连续
select DENSE_RANK() over (order by USENUM desc) 排名, USENUM from T_ACCOUNT;
-- 不论值是否相同，序号连续
select ROW_NUMBER() over (order by USENUM desc) 排名, USENUM from T_ACCOUNT;
```

ROW_NUMBER 分析函数也可以实现分页查询，用它来实现分页查询比三层嵌套子查询相比较而言会简单些

```sql
-- 使用dense_rank()来实现分页查询
select *
from (select dense_rank() over (order by usenum desc) rownumber, t.* from T_ACCOUNT t)
where rownumber >= 10
  and rownumber <= 20;
```

## 4.7 集合运算

集合运算，集合运算就是将两个或者多个结果集组合成为一个结果集。集合运算包括：

- UNION ALL（并集所有），返回各个查询的所有记录，包括重复记录。
- UNION（并集），返回各个查询的所有记录，不包括重复记录。
- INTERSECT（交集），返回两个查询共有的记录。
- MINUS（差集），返回第一个查询检索出的记录减去第二个查询检索出的记录之后剩余的记录。

```sql
-- 并集运算，会包括重复的记录
select * from T_OWNERS where ID > 5
union all
select * from T_OWNERS where ID < 7;
```

同样的，也可以使用茶几 MINUS 来实现分页查询

```sql
-- 使用差集来实现分页查询
select ROWNUM, T_ACCOUNT.* from T_ACCOUNT where ROWNUM < 20
minus
select ROWNUM, T_ACCOUNT.* from T_ACCOUNT where ROWNUM < 10;
```
