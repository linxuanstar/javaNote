# 第一章 Hibernate基础

Hibernate 是一款主流 ORM 框架 Object Relation Mapping 对象关系映射，将⾯向对象映射成⾯向关系。

## 1.1 快速入门

```xml
<dependencies>
    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.19</version>
    </dependency>
    <!-- Hibernate依赖 -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.4.10.Final</version>
    </dependency>
    <!-- Lombok简化开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.10</version>
    </dependency>
</dependencies>
```

```sql
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
USE linxuan;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS people;
CREATE TABLE IF NOT EXISTS people(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL,
	-- 这里为了简便直接用double类型了，真实情况不能这么使用
	money DOUBLE NOT NULL
);
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO people VALUES(1, "林炫1", 2100), (2, "林炫2", 3300), (3, "林炫3", 3000);
```

```java
@Data
public class Orders {
    private Integer id;
    private String name;
    // 一个订单被一个用户所持有。对一对应对象、对多对应集合
    private Customer customer;
}
```

```java
@Data
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> ordersSet;
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--　文件名称：hibernate.cfg.xml，该文件放在resources目录下面 -->
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- 数据源配置 -->
        <property name="connection.username">root</property>
        <property name="connection.password">root</property>
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.url">
            jdbc:mysql://localhost:3306/linxuan?useUnicode=true&amp;characterEncoding=UTF-8
        </property>
        <!-- C3P0 -->
        <property name="hibernate.c3p0.acquire_increment">10</property>
        <property name="hibernate.c3p0.idle_test_period">10000</property>
        <property name="hibernate.c3p0.timeout">5000</property>
        <property name="hibernate.c3p0.max_size">30</property>
        <property name="hibernate.c3p0.min_size">5</property>
        <property name="hibernate.c3p0.max_statements">10</property>
        <!-- 数据库⽅⾔，这里设置为MySQL方言，自动生成MySQL的语句-->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
        <!-- 打印SQL -->
        <property name="show_sql">true</property>
        <!-- 打印SQL的时候格式化SQL -->
        <property name="format_sql">true</property>
        <!-- 是否⾃动⽣成数据库 -->
        <property name="hibernate.hbm2ddl.auto"/>
    </session-factory>
</hibernate-configuration>
```

## 1.2 级联操作

## 1.3 一对多

```sql
-- 创建数据库并使用
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
USE linxuan;

-- 关闭外键约束
SET FOREIGN_KEY_CHECKS = 0;
-- 删除customer顾客数据库
DROP TABLE IF EXISTS customer;
-- 创建ustomer顾客数据库
CREATE TABLE IF NOT EXISTS customer(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL
) COMMENT "顾客表";
-- 删除orders订单数据库
DROP TABLE IF EXISTS orders;
-- 创建orders订单数据库
CREATE TABLE IF NOT EXISTS orders(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL,
	cid INT,
	FOREIGN KEY (cid) REFERENCES customer(id)
) COMMENT "订单表";
-- 开启外键约束
SET FOREIGN_KEY_CHECKS = 1;

-- 插入数据
INSERT INTO customer VALUES(1, "林炫1"), (2, "林炫2"), (3, "林炫3");
INSERT INTO orders VALUES(1, "订单1", 1), (2, "订单2", 1), (3, "订单3", 1), (4, "订单4", 2);
```

