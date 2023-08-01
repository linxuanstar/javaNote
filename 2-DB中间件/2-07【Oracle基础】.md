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

Windows 中 Oracle 的启动与关闭

```sql

```

## 1.2 遇到的BUG

**关机不正常导致的BUG**

```sql
-- windows server 2003 使用 sqlplus连接oracle报错
C:\Documents and Settings\Adminstrator> sqlplus system/linxuan
ORA-01034:ORACLE not available 
ORA-27101:shared memory realm does not exist
```

- 原因：很可能是数据库没有正常关闭
- 解决办法：关闭数据库然后再重新启动，关闭后再打开就行。

```sql
-- sys是用户名、dba是密码，sys登陆后面必须跟上as sysdba。这样可以直接打开
C:\Documents and Settings\Adminstrator> sqlplus sys/linxuan as sysdba
-- 强制干掉Oracle
SQL> shutdown abort
-- 重启
SQL> startup
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

## 1.5 自来水收费项目介绍

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

| 价格表（T_PRICETABLE）字段名 | 类型(位数)   | 是否必填 | 说明         |
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

### 1.5.1 创建系统表空间

```sql
-- 创建表空间。
-- waterboss为表空间名称、datafile用于设置物理文件名称(没必要一定和表空间名称相同)
-- size用于设置表空间的初始大小、autoextend on用于设置自动增长，如果存储量超过初始大小，则开始自动扩容
-- next 用于设置扩容的空间大小
create tablespace waterboss datafile 'c:\waterboss.dbf' size 100m autoextend on next 10m ;
```

### 1.5.2 创建用户并授权

```sql
-- 创建wateruser用户，密码为linxuan，默认表空间为waterboss
create user wateruser identified by linxuan default tablespace waterboss;
-- 虽然创建好用户了，但是仍然无法登录。这里给用户wateruser授予DBA权限后就可以登录了
grant dba to wateruser;
```

# 第二章 基础操作

基本上对于表和数据的增删改的 SQL 语句没有变化。

## 2.1 表和数据操作

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

## 2.2 JDBC 连接

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

## 2.3 数据导出导入

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

