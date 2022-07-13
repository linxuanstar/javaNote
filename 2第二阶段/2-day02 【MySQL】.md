# 第一章 主从复制

MysSQL主从复制是一个异步的复制过程，底层是基于Mysql数据库自带的**二进制日志**功能。就是一台或多台AysQL数据库(slave，即**从库**）从另一台MysQL数据库(master，即**主库**）进行日志的复制然后再解析日志并应用到自身，最终实现**从库**的数据和**主库**的数据保持一致。MySQL主从复制是MysQL数据库自带功能，无需借助第三方工具。

MysQL复制过程分成三步:

- master将改变记录到二进制日志（ binary log)
- slave将master的binary log拷贝到它的中继日志（relay log）
- slave重做中继日志中的事件，将改变应用到自己的数据库中

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\10-1.png)

## 1.1 配置-前置条件

提前准备好两台服务器，分别安装Mysql并启动服务成功。我们是克隆了一台Centos7搞的。

- 主库Master 192.168.66.130
- 从库slave 192.168.66.136

> **注意**：克隆的虚拟机需要修改网卡和数据库的uuid

## 1.2 配置-主库Master

1. 修改Mysq1数据库的配置文件/etc/my.cnf

   ```ini
   [mysqld]
   log-bin=mysql-bin #[必须]启用二进制日志
   server-id=100 #[必须]服务器唯一ID
   ```

2. 重启Mysql服务  `systemctl restart mysqld`

3. 登录Mysql数据库，执行下面SQL

   ```sql
   GRANT REPLICATION SLAVE ON *.* to 'xiaoming'@'%' identified by 'Root@123456';
   ```

   注:上面SQL的作用是创建一个用户**xiaoming**，密码为**Root@123456**，并且给xiaoming用户授予**REPLICATION SLAVE**权限。常用于建立复制时所需要用到的用户权限，也就是slave必须被master授权具有该权限的用户，才能通过该用户复制。

4. 登录Mysql数据库，执行下面SQL，记录下结果中File和Position的值

   ```sql
   show master status;
   ```

   注:上面SQL的作用是查看Master的状态，执行完此SQL后不要再执行任何操作

## 1.3 配置-从库Slave

1. 修改Mysq1数据库的配置文件/etc/my.cnf

   ```ini
   [mysqld]
   server-id=101 #[必须]服务器唯一ID
   ```

2. 重启Mysql服务   `systemctl restart mysqld`

3. 登录Mysq1数据库，执行下面SQL：

   ```sql
   # 没有换行
   # master_log_file和master_log_pos的值是上面记录的记录的值
   change master to master_host='192.168.66.130',master_user='xiaoming',master_password='Root@123456',master_log_file='mysql-bin.000003',master_log_pos=441;
   
   # 开启主从复制
   start slave;
   ```

4. 登录Mysql数据库，执行下面SQL，查看从数据库的状态`show slave status;`或者使用`show slave status\G;`

# 第二章 读写分离

面对日益增加的系统访问量，数据库的吞吐量面临着巨大瓶颈。对于同一时刻有大量并发读操作和较少写操作类型的应用系统来说，将数据库拆分为**主库和从库**，主库负责处理事务性的增删改操作，从库负责处理查询操作，能够有效的避免由数据更新导致的行锁，使得整个系统的查询性能得到极大的改善。

## 2.1 Sharding-JDBC介绍

Sharding-JDBC定位为轻量级Java框架，在Java的JDBC层提供的额外服务。它使用客户端直连数据库,以jar包形式提供服务，无需额外部署和依赖，可理解为增强版的JDBC驱动，完全兼容JDBC和各种ORM框架。

使用Sharding-JDBC可以在程序中轻松的实现数据库读写分离。

- 适用于任何基于JDBC的ORM框架，如: JPA, Hibernate,Mybatis, Spring JDBC Template或直接使用JDBC。
- 支持任何第三方的数据库连接池，如:DBCP，C3PO,BoneCP, Druid, HikariCP等。
- 支持任意实现JDBC规范的数据库。目前支持MySQL，Oracle,SQLServer，PostgreSQL以及任何遵循SQL92标准的数据库。

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.0.0-RC1</version>
</dependency>
```

## 2.2 案例实现

使用Sharding-JDBC实现读写分离步骤:

1. 导入maven坐标

   ```xml
   <dependency>
       <groupId>org.apache.shardingsphere</groupId>
       <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
       <version>4.0.0-RC1</version>
   </dependency>
   ```

2. 在配置文件中配置读写分离规则

   ```yml
   spring:
     shardingsphere:
       datasource:
         names:
           master,slave
         # 主数据源
         master:
           type: com.alibaba.druid.pool.DruidDataSource
           driver-class-name: com.mysql.cj.jdbc.Driver
           url: jdbc:mysql://192.168.66.130:3306/rw?characterEncoding=utf-8&useSSL=false
           username: root
           password: root
         # 从数据源
         slave:
           type: com.alibaba.druid.pool.DruidDataSource
           driver-class-name: com.mysql.cj.jdbc.Driver
           url: jdbc:mysql://192.168.66.136:3306/rw?characterEncoding=utf-8&useSSL=false
           username: root
           password: root
       masterslave:
         # 读写分离配置
         load-balance-algorithm-type: round_robin #轮询
         # 最终的数据源名称
         name: dataSource
         # 主库数据源名称
         master-data-source-name: master
         # 从库数据源名称列表，多个逗号分隔
         slave-data-source-names: slave
       props:
         sql:
           show: true #开启SQL显示，默认false
   ```

3. 在配置文件中配置**允许bean定义覆盖**配置项

   ```yml
   spring:
       main:
           allow-bean-definition-overriding: true
   ```

   
