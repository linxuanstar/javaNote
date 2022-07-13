![](D:\Java\笔记\图片\3-2【MyBatis】\0-1.png)

# 第一章 Mybatis基础

## 1.1 MyBatis简介

MyBatis最初是Apache的一个开源项目iBatis, 2010年6月这个项目由Apache Software Foundation迁移到了Google Code。随着开发团队转投Google Code旗下，`iBatis3.x`正式更名为MyBatis。代码于2013年11月迁移到Github

iBatis一词来源于“internet”和“abatis”的组合，是一个基于Java的持久层框架。iBatis提供的持久层框架包括SQL Maps和Data Access Objects（DAO）

MyBatis特性：

1. MyBatis 是支持定制化 SQL、存储过程以及高级映射的优秀的持久层框架
2. MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集
3. MyBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO（Plain Old Java Objects，普通的Java对象）映射成数据库中的记录
4. MyBatis 是一个 半自动的ORM（Object Relation Mapping，对象关系映射）框架

MyBatis下载地址：`https://github.com/mybatis/mybatis-3`

MyBatis和其它持久化层技术对比：

- JDBC  
	- SQL 夹杂在Java代码中耦合度高，导致硬编码内伤  
	- 维护不易且实际开发需求中 SQL 有变化，频繁修改的情况多见  
	- 代码冗长，开发效率低
- Hibernate 和 JPA
	- 操作简便，开发效率高  
	- 程序中的长难复杂 SQL 需要绕过框架  
	- 内部自动生产的 SQL，不容易做特殊优化  
	- 基于全映射的全自动框架，大量字段的 POJO 进行部分映射时比较困难。  
	- 反射操作太多，导致数据库性能下降
- MyBatis
	- 轻量级，性能出色  
	- SQL 和 Java 编码分开，功能边界清晰。Java代码专注业务、SQL语句专注数据  
	- 开发效率稍逊于HIbernate，但是完全能够接受
## 1.2 搭建MyBatis
1. 创建maven工程。

   修改打包方式：jar。打开`pom.xml`，添加代码：

   ```xml
   <packaging>jar</packaging>
   ```

   引入依赖。打开`pom.xml`，添加代码：

   ```xml
   <dependencies>
   	<!-- Mybatis核心 -->
   	<dependency>
   		<groupId>org.mybatis</groupId>
   		<artifactId>mybatis</artifactId>
   		<version>3.5.7</version>
   	</dependency>
   	<!-- junit测试 -->
   	<dependency>
   		<groupId>junit</groupId>
   		<artifactId>junit</artifactId>
   		<version>4.12</version>
   		<scope>test</scope>
   	</dependency>
   	<!-- MySQL驱动 -->
   	<dependency>
   		<groupId>mysql</groupId>
   		<artifactId>mysql-connector-java</artifactId>
   		<version>5.1.3</version>
   		</dependency>
   </dependencies>
   ```

   在之后会报一个错误，原因是JDK版本太高了，所以我们需要调低一下，直接将下面代码加入`pom.xml`文件中：

   ```xml
       <build>
           <plugins>
               <plugin>
                   <groupId>org.apache.maven.plugins</groupId>
                   <artifactId>maven-compiler-plugin</artifactId>
                   <version>2.3.2</version>
                   <configuration>
                       <source>1.8</source>
                       <target>1.8</target>
                   </configuration>
               </plugin>
           </plugins>
       </build>
   ```

2. 创建MyBatis的核心配置文件。习惯上将MyBatis的核心配置文件命名为`mybatis-config.xml`。核心配置文件主要用于配置连接数据库的环境以及MyBatis的全局配置信息。核心配置文件存放的位置是`src/main/resources`目录下。

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
       <!--设置连接数据库的环境-->
       <environments default="development">
           <environment id="development">
               <transactionManager type="JDBC"/>
               <dataSource type="POOLED">
                   <property name="driver" value="com.mysql.jdbc.Driver"/>
                   <!--打开MySql，创建一个数据库MyBatis-->
                   <property name="url" value="jdbc:mysql://localhost:3306/MyBatis"/>
                   <property name="username" value="root"/>
                   <property name="password" value="root"/>
               </dataSource>
           </environment>
       </environments>
       <!--引入映射文件-->
       <mappers>
           <mapper resource="mappers/UserMapper.xml"/>
       </mappers>
   </configuration>
   ```

3. 创建数据库和表，MyBatis数据库，t_user表，User实体类。

   ```sql
   USE mybatis;
   
   CREATE TABLE t_user(
   	id INT PRIMARY KEY AUTO_INCREMENT,
   	username VARCHAR(20),
   	PASSWORD VARCHAR(20),
   	age INT,
   	sex CHAR,
   	email VARCHAR(20)
   );
   
   SHOW TABLES;
   
   SELECT * FROM t_user;
   ```

   ```java
   package com.linxuan.mybatis.pojo;
   
   public class User {
       private Integer id;
   
       private String username;
   
       private String password;
   
       private Integer age;
   
       private String sex;
   
       private String email;
       
       // ...
   }
   ```

4. 创建mapper接口。MyBatis中的mapper接口相当于以前的dao。但是区别在于，mapper仅仅是接口，我们不需要提供实现类

   ```java
   package com.linxuan.mybatis.mapper;
   
   public interface UserMapper {
   	/**  
   	* 添加用户信息  
   	*/  
       int insertUser();
   }
   ```

5. 创建MyBatis的映射文件。ORM（Object Relationship Mapping）对象关系映射。  对象：Java的实体类对象  关系：关系型数据库  映射：二者之间的对应关系

   - 映射文件的命名规则
   	- 表所对应的实体类的`类名+Mapper.xml`。例如：表t_user，映射的实体类为User，所对应的映射文件为`UserMapper.xml` 。因此一个映射文件对应一个实体类，对应一张表的操作
   	- MyBatis映射文件用于编写SQL，访问以及操作表中的数据
   	- MyBatis映射文件存放的位置是`src/main/resources/mappers`目录下
   - MyBatis中可以面向接口操作数据，要保证两个一致
     - mapper接口的全类名和映射文件的命名空间（namespace）保持一致
     - mapper接口中方法的方法名和映射文件中编写SQL的标签的id属性保持一致

   在`src/main/resources`下面创建`mappers`文件夹，再创建`UserMapper.xml` 。里面键入如下代码：

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.linxuan.mybatis.mapper.UserMapper">
       <!--int insertUser();-->
       <insert id="insertUser">
           insert into t_user values(null,'张三','123',23,'男', '12345@qq.com')
       </insert>
   </mapper>
   ```

6. 引入映射文件。在`mybatis-config.xml`修改引入映射文件代码，不过这里不用修改。

   ```xml
       <!--引入映射文件-->
       <mappers>
           <mapper resource="mappers/UserMapper.xml"/>
       </mappers>
   ```

7. 通过junit测试功能。在Maven项目里面的`test/java`文件夹下面创建包和类来测试。创建如下包：`com/linxuan/mybatis/test`。再创建测试类：

   ```java
   public class UserMapperTest {
       @Test
       public void testInsertUser() throws IOException {
           // 读取MyBatis的核心配置文件
           InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
           // 获取SqlSessionFactoryBuilder对象
           SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
           // 通过核心配置文件所对应的字节输入流创建工厂类SqlSessionFactory，生产SqlSession对象
           SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
           // 获取sqlSession，此时通过SqlSession对象所操作的sql都必须手动提交或回滚事务
           SqlSession sqlSession = sqlSessionFactory.openSession();
           
           // 通过代理模式创建UserMapper接口的代理实现类对象
           UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
           // 调用UserMapper接口中的方法，就可以根据UserMapper的全类名匹配元素文件，通过调用的方法名匹配映射文件中的SQL标签，并执行标签中的SQL语句
           int result = userMapper.insertUser();
           // 提交事务
           sqlSession.commit();
           System.out.println("result:" + result);
       }
   }
   // 结果 result:1
   ```

   此时需要手动提交事务，如果要自动提交事务，则在获取sqlSession对象时，使用`SqlSession sqlSession = sqlSessionFactory.openSession(true);`，传入一个Boolean类型的参数，值为true，这样就可以自动提交。

   ```java
   public class UserMapperTest {
       @Test
       public void testInsertUser() throws IOException {
           // 读取MyBatis的核心配置文件
           InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
           // 获取SqlSessionFactoryBuilder对象
           SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
           // 通过核心配置文件所对应的字节输入流创建工厂类SqlSessionFactory，生产SqlSession对象
           SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
           
   	    // 创建SqlSession对象，此时通过SqlSession对象所操作的sql都会自动提交  
   		SqlSession sqlSession = sqlSessionFactory.openSession(true);
           // 通过代理模式创建UserMapper接口的代理实现类对象
           UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
           // 调用UserMapper接口中的方法，就可以根据UserMapper的全类名匹配元素文件，通过调用的方法名匹配映射文件中的SQL标签，并执行标签中的SQL语句
           int result = userMapper.insertUser();
           System.out.println("result:" + result);
       }
   }
   // 输出结果 result:1
   ```

   > `SqlSession`：代表Java程序和数据库之间的会话。（HttpSession是Java程序和浏览器之间的会话）
   >
   > `SqlSessionFactory`：是“生产”SqlSession的“工厂”
   >
   > 工厂模式：如果创建某一个对象，使用的过程基本固定，那么我们就可以把创建这个对象的相关代码封装到一个“工厂类”中，以后都使用这个工厂类来“生产”我们需要的对象

8. 加入`log4j`日志功能：

   加入依赖

   ```xml
   <!-- log4j日志 -->
   <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>1.2.17</version>
   </dependency>
   ```

   加入log4j的配置文件。log4j的配置文件名为`log4j.xml`，存放的位置是`src/main/resources`目录下，复制：

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
   <!-- xmlns:log4j="http://jakarta.apache.org/log4j/"会爆红，不过没得关系 -->
   <log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
       <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
           <param name="Encoding" value="UTF-8" />
           <layout class="org.apache.log4j.PatternLayout">
   			<param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS} %m (%F:%L) \n" />
           </layout>
       </appender>
       <logger name="java.sql">
           <level value="debug" />
       </logger>
       <logger name="org.apache.ibatis">
           <level value="info" />
       </logger>
       <root>
           <level value="debug" />
           <appender-ref ref="STDOUT" />
       </root>
   </log4j:configuration>
   ```

   > 日志的级别：
   >
   > FATAL(致命)>ERROR(错误)>WARN(警告)>INFO(信息)>DEBUG(调试) 从左到右打印的内容越来越详细

## 1.3 SQL基本语法

```sql
insert into 表名(列名1，列名2，...列名n) values(值1，值2，...值n);
-- INSERT INTO stu(id, NAME) VALUES(1, "林炫");
-- INSERT INTO stu VALUES(1, "张三", 21, 10, "2001-11-11", NULL);		-- 不定义列名，直接全部赋值
```

```sql
delete from 表名 [where条件];
-- DELETE FROM stu WHERE id = 1;
```

```sql
update 表名 set 列名 = 值1， 列名 = 值2， ...[where 条件];
```

```sql
select
	字段列表
from
	表名
where
	条件列表
group by
	分组字段
having
	分组之后的条件
order by
	排序
limit
	分页限定;
```

**条件查询**

<!--P503 1.22 视频集数反了-->

`where`字句后面跟条件语句

运算符：

* `> 、<、<=、>=、=、<>。`

  ```sql
  select * from 表名 where 列名 运算符 条件;
  select * from person where age > 20;		-- 挑选person表里面age列大于20的人
  ```

  注意：

  ​	运算符里面没有`==`，只有`=`，所以判断的时候用`=`就好了。

  ​	运算符`<>` 等价于 `!=` 。

* `between...and`

  ```sql
  select * from 表名 where 列名 between...and...;
  select * from person where age between 20 and 30;		-- 挑选person表里面age列大于等于20并且小于等于30
  select * from person where age >= 20 and <= 30;			-- 两者等价 
  ```

* `in(集合)`

  ```sql
  select * from 表名 where 列名 in(集合);
  select * from person where age in(22, 18, 55);		-- 挑选person表里面age列的22，18,55行。
  select * from person where age 22 or 18 or 55;		-- 等价于这一行
  ```

* `is null`

  ```sql
  select * from 表名 where 列名 is null;
  select * from person where english is null;			-- 查询英语成绩为null
  select * from person where english is not null;		-- 查询英语成绩不为null
  ```

* `and`或者`&&`    `or`或者`||`    `not`或者`！`

  ```sql
  select * from 表名 where 列名 and或者&& 条件;
  select * from person where age > 20 && age < 50;		-- 挑选person表里面age列大于20,并且小于50的人。 										注意：不建议这样使用，可以使用and关键字
  select * from person where age > 20 and age < 50;		-- 建议使用
  ```

  ```sql
  select * from 表名 where 列名 or或者|| 条件;
  ```

  ```
  select * from 表名 where 列名 not或者！ 条件;
  ```

* `like`：模糊查询

  <!--P504-->

  占位符：

  ​	`"_"`：单个任意字符

  ​	`"%"`多个任意字符

  在SQL里面单双引号都可以

  ```sql
  select * from 表名 where 列名 like 条件;
  ```

  ```sql
  select * from person where name like "马%";			-- 查询姓马的人
  select * from person where name like "_化%";			-- 查询第二个字是化的人
  select * from person where name like "___";			-- 查询姓名是三个字的人
  select * from person where name like "%德%";			-- 查询姓名中包括德的人
  ```

<!--P497-->

**排序查询**

<!--P498-->

* 语法

  ```sql
  select * from 表名 order by 字句；
  select * from 表名 order by 排序字段1 排序方式1，排序字段2 排序方式2...;
  ```

* 排序方式

  ```sql
  ASC：升序，默认的。
  DESC：降序。
  ```

* 注意：

  如果有多个排序条件，则当前面的条件值一样的时候，就会排序第二条件。

```sql
SELECT * FROM person ORDER BY math;		-- 默认对math列进行升序排序
SELECT * FROM person ORDER BY math DESC;	-- 对math列进行降序排序
SELECT * FROM person ORDER BY math ASC, english DESC;	-- 对math列进行升序排序，math列有重复值的时候判断English列，按照english列进行降序排序
```

**聚合函数**

<!--P499-->

聚合函数：将一列数据作为一个整体，进行纵向计算。例如：计算个数，计算和，计算平均值。

聚合函数的计算是排除NULL值的，就是遇到有NULL值会忽略。

```sql
select 函数名称(列名称) from 表名称;
```

* `count`：计算个数

  ```sql
  select count(主键、ID列) from 表名称;		-- 一般选择非空的列：主键
  ```

  ```sql
  select count(*) from 表名称;		-- * 代表全部，找到一个非空的列来搞，不建议
  ```

* `max`：计算最大值

  ```sql
  select max(列名称) from 表名称;		-- 计算该表该列最大值
  ```

* `min`：计算最小值

  ```sql
  select min(列名称) from 表名称;		-- 计算该表该列最大值
  ```

* `sum`：计算和

  ```sql
  select sum(列名称) from 表名称;		-- 计算该表该列数值的和
  ```

* `avg`：计算平均值

  ```sql
  select avg(列名称) from 表名称;		-- 计算该表该列平均值
  ```

由于聚合函数的计算是排除`NULL`的值的，所以对此我们有两种解决方案：

1. 选择不包含非空的列进行计算

2. `IFNULL函数`

   ```sql
   select 函数(IFNULL(列名称， 值)) from 表名称;		-- IFNULL函数会判断NULL，如果是NULL那么会将NULL变为值
   select count(IFNULL(math, 0)) from student;		-- count函数计算math列个数，IFNULL函数对math列进行判断，如果有着NULL，那么将NULL变为0，其他的不改变
   ```

**分组查询**

<!--P500-->

分组查询就是将以行为单位，来进行查询。

```sql
select 分组查询字段 from 表名称 group by 列名;	-- 分组查询字段：分组字段、聚合函数
```

```sql
select sex, avg(math) from 表名称 group by sex;		-- 按照性别进行分组，分别查询男、女同学平均分
```

```sql
select sex, avg(math), count(id) from 表名称 group by sex;		-- 按照性别进行分组，分别查询男、女同学平均分、人数
```

```sql
select sex, avg(math), count(id) from 表名称 where math > 70 group by sex;		-- 按照性别进行分组，分别查询男、女同学平均分、人数   要求：分数低于70的人不参与分组
```

```sql
select sex, avg(math), count(id) from 表名称 where math > 70 group by sex having count(id) > 2; -- 按照性别进行分组，分别查询男、女同学平均分、人数   要求：分数低于70的人不参与分组
```

`where` 和 `having`区别：

1. `where`在分组之前进行限定，如果不满足条件，那么不参与分组。`having`在分组之后进行限定，如果不满足结果，则不会被查询出来。
2. `where`后面不可以跟聚合函数，`having`后面可以进行聚合函数的判断。

**分页查询**

<!--P501-->

分页查询，类似于百度搜索出来后有很多词条，词条按页码来出现。

```sql
select * from 表名 limit 开始的索引，每页查询的条数;
select * from person limit 0, 3;		-- 第1页
select * from person limit 3, 3;		-- 第2页
select * from person limit 6, 3;		-- 第3页
```

* 公式：开始的索引 = （当前的页码 - 1） * 每页显示的条数。
* 开始的索引是从0开始的
* 分页操作是一个“方言”， 其他的数据库都有各自的“方言”

## 1.4 MyBatis基础查询

> * 查询的标签select必须设置属性resultType或resultMap，用于设置实体类和数据库表的映射关系  
>
>   - `resultType`：自动映射，用于属性名和表中字段名一致的情况  
>
>   - `resultMap`：自定义映射，用于一对多或多对一或字段名和属性名不一致的情况  
>
> * 当查询的数据为多条时，只能使用集合作为返回值，否则会抛出异常`TooManyResultsException`；但是若查询的数据只有一条，可以使用实体类或集合作为返回值

查询一个实体类对象

```xml
<!--User selectUser();-->
<select id="selectUser" resultType="com.linxuan.mybatis.pojo.User">
    select * from t_user where id = 1;
</select>
```

查询集合

```xml
<!--List<User> getAllUser();-->
<select id="getAllUser" resultType="com.linxuan.mybatis.pojo.User">
    select * from t_user;
</select>
```

## 1.5 核心配置文件详解

核心配置文件中的标签必须按照固定的顺序(有的标签可以不写，但顺序一定不能乱)：
`properties、settings、typeAliases、typeHandlers、objectFactory、objectWrapperFactory、reflectorFactory、plugins、environments、databaseIdProvider、mappers`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--设置连接数据库的环境-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/MyBatis"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <!--引入映射文件-->
    <mappers>
        <mapper resource="mappers/UserMapper.xml"/>
    </mappers>
</configuration>
```

**environment**

- `environments`：设置多个连接数据库的环境

  属性 default：设置默认使用的环境的id

- `environment`：设置具体的连接数据库的环境信息

  属性 id：设置环境的唯一标识，可通过environments标签中的default设置某一个环境的id，表示默认使用的环境

- `transactionManager`：设置事务管理方式

  属性 type：设置事务管理方式，type="JDBC|MANAGED"

  - `type="JDBC"`：设置当前环境的事务管理都必须手动处理
  - `type="MANAGED"`：设置事务被管理，例如spring中的AOP

- `dataSource`：设置数据源

  属性 type：设置数据源的类型，type="POOLED|UNPOOLED|JNDI"

  - `type="POOLED"`：使用数据库连接池，即会将创建的连接进行缓存，下次使用可以从缓存中直接获取，不需要重新创建
  - `type="UNPOOLED"`：不使用数据库连接池，即每次使用连接都需要重新创建
  - `type="JNDI"`：调用上下文中的数据源

**properties**

创建`jdbc.properties`文件：

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/MyBatis
jdbc.username=root
jdbc.password=root
```

引入properties文件：

```xml
<!--引入properties文件，此时就可以${属性名}的方式访问属性值-->
<properties resource="jdbc.properties"></properties>
```

访问属性值：

```xml
<dataSource type="POOLED">
    <!--设置驱动类的全类名-->
    <property name="driver" value="${jdbc.driver}"/>
    <!--设置连接数据库的连接地址-->
    <property name="url" value="${jdbc.url}"/>
    <!--设置连接数据库的用户名-->
    <property name="username" value="${jdbc.username}"/>
    <!--设置连接数据库的密码-->
    <property name="password" value="${jdbc.password}"/>
</dataSource>
```

**typeAliases**

```xml
<!-- 对于resultType="com.linxuan.mybatis.pojo.User" 全类名很长，我们可以使用别名-->
<select id="getAllUser" resultType="com.linxuan.mybatis.pojo.User">
	select * from t_user;
</select>
```

`typeAlias`：设置某个具体的类型的别名。属性有两个：`type`和`alias`。

- `type`：需要设置别名的类型的全类名
- `alias`：设置此类型的别名，<font color = "red">且别名不区分大小写</font>。若不设置此属性，该类型拥有默认的别名，即类名

```xml
<typeAliases>
    <!--
         <typeAlias type="com.linxuan.mybatis.pojo.User"></typeAlias>
    -->
    
    <!--
   		<typeAlias type="com.linxuan.mybatis.pojo.User" alias="user"></typeAlias>
  	-->
    
    <!--以包为单位，设置改包下所有的类型都拥有默认的别名，即类名且不区分大小写-->
    <package name="com.linxuan.mybatis.pojo"/>
</typeAliases>
```

**mappers**

```xml
<!--引入映射文件-->
<mappers>
    <!-- <mapper resource="mappers/UserMapper.xml"/> -->
    <!--
        以包为单位，将包下所有的映射文件引入核心配置文件
        注意：
           1. 此方式必须保证mapper接口和mapper映射文件必须在相同的包下
           2. mapper接口要和mapper映射文件的名字一致
		   3. 创建包的时候使用的是：com/linxuan/mybatis/mapper
        -->
    <package name="com.linxuan.mybatis.mapper"/>
</mappers>
```

## 1.6 抽取工具类

对于下面代码很多，重复度很高。那么我们又不想要一直重复写，我们可以抽取出来。

```java
public class UserMapperTest {
    @Test
    public void testInsertUser() throws IOException {
        // 读取MyBatis的核心配置文件
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        // 获取SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 通过核心配置文件所对应的字节输入流创建工厂类SqlSessionFactory，生产SqlSession对象
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        
	    // 创建SqlSession对象，此时通过SqlSession对象所操作的sql都会自动提交  
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 通过代理模式创建UserMapper接口的代理实现类对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 调用UserMapper接口中的方法，就可以根据UserMapper的全类名匹配元素文件，通过调用的方法名匹配映射文件中的SQL标签，并执行标签中的SQL语句
        int result = userMapper.insertUser();
        System.out.println("result:" + result);
    }
}
// 输出结果 result:1
```

创建`SqlSessionUtils.java`：

```java
public class SqlSessionUtils {
    public static SqlSession getSqlSession() {
        SqlSession sqlSession = null;
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            sqlSession = sqlSessionFactory.openSession(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSession;
    }
}
// 如果有报错，肯定是乱改了，好好查一下
```

测试：

```java
    @Test
    public void testCRUD() throws IOException {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        List<User> users = mapper.getAllUser();
        users.forEach(user -> System.out.println(user));
    }
```

# 第二章 MyBatis获取参数和查询

MyBatis获取参数值的两种方式：`${}`和`#{}`  

- `${}`的本质就是字符串拼接，`#{}`的本质就是占位符赋值  
- `${}`使用字符串拼接的方式拼接sql，若为字符串类型或日期类型的字段进行赋值时，需要手动加单引号；但是`#{}`使用占位符赋值的方式拼接sql，此时为字符串类型或日期类型的字段进行赋值时，可以自动添加单引号

只有两种处理方式

1. 实体类类型的参数
2. 使用`@Param`标识参数

## 2.1 实体类型的参数

**单个字面量类型的参数**

若mapper接口中的方法参数为单个的字面量类型，此时可以使用`${}`和`#{}`以任意的名称（最好见名识意）获取参数的值，<font color = "red">注意`${}`需要手动加单引号</font>

使用${}：

```xml
<!--User getUserByUsername(String username);-->
<select id="getUserByUsername" resultType="User">
    select * from t_user where username = '${username}';
</select>
```

```java
@Test
public void testGetUserByUsername() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    User user = mapper.getUserByUsername("张三");
    System.out.println(user);
}
```

使用`#{}`：

```xml
<!--User getUserByUsername(String username);-->
<select id="getUserByUsername" resultType="User">
	select * from t_user where username = #{username}
</select>
```
**多个字面量类型的参数**

若mapper接口中的方法参数为多个时，此时MyBatis会自动将这些参数放在一个map集合中

1. 以arg0,arg1...为键，以参数为值；
2. 以param1,param2...为键，以参数为值；

因此只需要通过`\${}`和`#{}`访问map集合的键就可以获取相对应的值，注意`${}`需要手动加单引号。使用arg或者param都行，要注意的是，arg是从arg0开始的，param是从param1开始的。它们都放在了同一个Map集合中，所以可以混着用，也无所谓的配套了。

```xml
<!--User checkLogin(String username,String password);-->
<select id="checkLogin" resultType="User">  
	select * from t_user where username = #{arg0} and password = #{arg1}  
</select>
```
```xml
<!--User checkLogin(String username,String password);-->
<select id="checkLogin" resultType="User">
	select * from t_user where username = '${param1}' and password = '${param2}'
</select>
```
**map集合类型的参数**

若mapper接口中的方法需要的参数为多个时，此时可以手动创建map集合，将这些数据放在map中只需要通过`${}`和`#{}`访问map集合的键就可以获取相对应的值，注意`${}`需要手动加单引号

```java
/**
 * 通过集合来查询
 */
User getUserByMap(Map<String, Object> hashMap);
```

```xml
<!--    User getUserByMap(Map<String, Object>);-->
<select id="getUserByMap" resultType="user">
    select * from t_user where username = #{username} and password = #{password};
</select>
```

```java
@Test
public void testGetUserByUsernameAndPassword() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    Map<String, Object> hashMap = new HashMap<>();
    hashMap.put("username", "张三");
    hashMap.put("password", "123");
    User user = mapper.getUserByMap(hashMap);
    System.out.println(user);
}
```

**实体类类型的参数**

若mapper接口中的方法参数为实体类对象时此时可以使用`${}`和`#{}`，通过访问实体类对象中的属性名获取属性值，注意`${}`需要手动加单引号

```xml
<!--int insertUser(User user);-->
<insert id="insertUser">
	insert into t_user values(null,#{username},#{password},#{age},#{sex},#{email})
</insert>
```
```java
@Test
public void insertUser() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
	User user = new User(null,"Tom","123456",12,"男","123@321.com");
	mapper.insertUser(user);
}
```
## 2.2 @Param标识参数
可以通过`@Param`注解标识mapper接口中的方法参数，此时，会将这些参数放在map集合中 

1. 以`@Param`注解的value属性值为键，以参数为值；
2. 以param1,param2...为键，以参数为值；

只需要通过`${}`和`#{}`访问map集合的键就可以获取相对应的值，注意`${}`需要手动加单引号

```xml
<!--User CheckLoginByParam(@Param("username") String username, @Param("password") String password);-->
    <select id="CheckLoginByParam" resultType="User">
        select * from t_user where username = #{username} and password = #{password}
    </select>
```
```java
@Test
public void checkLoginByParam() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
	mapper.CheckLoginByParam("admin","123456");
}
```
## 2.3 MyBatis的查询功能

> 在MyBatis中，对于Java中常用的类型都设置了类型别名  
>
>  * 例如：java.lang.Integer-->int|integer  
>  * 例如：int-->_int|_integer  
>  * 例如：Map-->map,List-->list  

如果查询出的数据只有一条，可以通过
1. 实体类对象接收

   ```java
   /**
    * 根据用户id查询用户信息
    * @param id
    * @return
    */
   User getUserById(@Param("id") int id);
   ```

   ```xml
   <!--User getUserById(@Param("id") int id);-->
   <select id="getUserById" resultType="User">
   	select * from t_user where id = #{id}
   </select>
   ```

2. List集合接收

   ```java
   /**
    * 查询所有用户信息
    * @return
    */
   List<User> getUserList();
   ```

   ```xml
   <!--List<User> getUserList();-->
   <select id="getUserList" resultType="User">
   	select * from t_user
   </select>
   ```

3. Map集合接收，结果`{password=123456, sex=男, id=1, age=23, username=admin}`

   ```java
   /**  
    * 根据用户id查询用户信息为map集合  
    * @param id  
    * @return  
    */  
   Map<String, Object> getUserToMap(@Param("id") int id);
   ```

   ```xml
   <!--Map<String, Object> getUserToMap(@Param("id") int id);-->
   <select id="getUserToMap" resultType="map">
   	select * from t_user where id = #{id}
   </select>
   <!--结果：{password=123456, sex=男, id=1, age=23, username=admin}-->
   ```

如果查询出的数据有多条，一定不能用实体类对象接收，会抛异常`TooManyResultsException`，可以通过
1. 实体类类型的List集合接收

2. Map类型的List集合接收

   ```java
   /**  
    * 查询所有用户信息为map集合  
    * @return  
    * 将表中的数据以map集合的方式查询，一条数据对应一个map；若有多条数据，就会产生多个map集合，此时可以将这些map放在一个list集合中获取  
    */  
   List<Map<String, Object>> getAllUserToMap();
   ```

   ```xml
   <!--Map<String, Object> getAllUserToMap();-->  
   <select id="getAllUserToMap" resultType="map">  
   	select * from t_user  
   </select>
   <!--
   	结果：
   	[{password=123456, sex=男, id=1, age=23, username=admin},
   	{password=123456, sex=男, id=2, age=23, username=张三},
   	{password=123456, sex=男, id=3, age=23, username=张三}]
   -->
   ```

3. 在mapper接口的方法上添加`@MapKey`注解

   ```java
   /**
    * 查询所有用户信息为map集合
    * @return
    * 将表中的数据以map集合的方式查询，一条数据对应一个map；若有多条数据，就会产生多个map集合，并且最终要以一个map的方式返回数据，此时需要通过@MapKey注解设置map集合的键，值是每条数据所对应的map集合
    */
   @MapKey("id")
   Map<String, Object> getAllUserToMap();
   ```

   ```xml
   <!--Map<String, Object> getAllUserToMap();-->
   <select id="getAllUserToMap" resultType="map">
   	select * from t_user
   </select>
   <!--
   	结果：
   	{
   	1={password=123456, sex=男, id=1, age=23, username=admin},
   	2={password=123456, sex=男, id=2, age=23, username=张三},
   	3={password=123456, sex=男, id=3, age=23, username=张三}
   	}
   -->
   ```

# 第三章 特殊SQL的执行

## 3.1 模糊查询

占位符，在SQL里面单双引号都可以：

- `"_"`：单个任意字符
- `"%"`多个任意字符

一共有三种方式，第三种是最常用的：

- `select * from t_user where username like '%${mohu}%'`  

  > 对于这种方式`'%${mohu}%`，只能使用`${}`，我们知道`#{}`的本质是占位符，也就是`？`。那么当我们使用`#{}`的时候会替换为`？`，而因为模糊查询是有单引号的，会将`？`变成一个字符串，所以无法进行替换，所以也就会报错。
  >
  > 另外使用`${}`不用加上`''`。

- `select * from t_user where username like concat('%',#{mohu},'%')` 

- `select * from t_user where username like "%"#{mohu}"%"`

```java
/**
 * 根据用户名进行模糊查询
 * @param username 
 * @return java.util.List<com.atguigu.mybatis.pojo.User>
 * @date 2022/2/26 21:56
 */
List<User> getUserByLike(@Param("username") String username);
```
```xml
<!--List<User> getUserByLike(@Param("username") String username);-->
<select id="getUserByLike" resultType="User">
	<!--select * from t_user where username like '%${mohu}%'-->  
	<!--select * from t_user where username like concat('%',#{mohu},'%')-->  
	select * from t_user where username like "%"#{mohu}"%"
</select>
```
## 3.2 批量删除

对于批量删除的语句，我们这里只能使用`${}`，如果使用`#{}`，则解析后的sql语句为`delete from t_user where id in ('1,2,3')`，这样是将`1,2,3`看做是一个整体，只有id为`1,2,3`的数据会被删除。

正确的语句应该是`delete from t_user where id in (1,2,3)`。

> 注意：`${}`不加分号

```java
/**
 * 根据id批量删除
 * @param ids 
 * @return int
 * @date 2022/2/26 22:06
 */
int deleteMore(@Param("ids") String ids);
```
```xml
<delete id="deleteMore">
	delete from t_user where id in (${ids})
</delete>
```
```java
//测试类
@Test
public void deleteMore() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
	int result = mapper.deleteMore("1,2,3,8");
	System.out.println(result);
}
```
## 3.3 动态设置表名
- 只能使用`${}`，因为表名不能加单引号
```java
/**
 * 查询指定表中的数据
 * @param tableName 
 * @return java.util.List<com.atguigu.mybatis.pojo.User>
 * @date 2022/2/27 14:41
 */
List<User> getUserByTable(@Param("tableName") String tableName);
```
```xml
<!--List<User> getUserByTable(@Param("tableName") String tableName);-->
<select id="getUserByTable" resultType="User">
	select * from ${tableName}
</select>
```
## 3.4 获取自增主键的值
使用场景

* 我们有两张表，班级表和学生表：`t_clazz(clazz_id,clazz_name)`  `t_student(student_id,student_name,clazz_id)`  。首先来添加班级信息，接着我们就要往班级里面添加学生了。可是这时候我们并不知道我们添加的班级的id是多少，所以需要获取新添加的班级的id，然后为班级分配学生，即将某学的班级id修改为新添加的班级的id

在`mapper.xml`中设置两个属性
- `useGeneratedKeys`：设置使用自增的主键  
* `keyProperty`：因为增删改有统一的返回值是受影响的行数，因此只能将获取的自增的主键放在传输的参数user对象的某个属性中

```java
/**
 * 添加用户信息
 * @param user 
 * @date 2022/2/27 15:04
 */
void insertUser(User user);
```
```xml
<!--void insertUser(User user);-->
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
	insert into t_user values (null,#{username},#{password},#{age},#{sex},#{email})
</insert>
```
```java
//测试类
@Test
public void insertUser() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
	User user = new User(null, "ton", "123", 23, "男", "123@321.com");
	mapper.insertUser(user);
	System.out.println(user);
	//输出：user{id=10, username='ton', password='123', age=23, sex='男', email='123@321.com'}，自增主键存放到了user的id属性中
}
```
# 第四章 自定义映射resultMap

## 4.1 环境搭建

创建一个t_emp和t_dept表，did让两个表产生关联：

```sql
-- 创建一个t_emp表
CREATE TABLE t_emp (
	eid INT PRIMARY KEY AUTO_INCREMENT;
	emp_name VARCHAR(20), 
	age INT, 
	sex CHAR,
	email VARCHAR(20),
	did INT
);

-- 查询t_emp所有字段
SELECT * FROM t_emp;

-- 向t_emp添加数据
INSERT INTO t_emp VALUES (NULL, "张三", 23, "男", "123@gmail.com", 1);
INSERT INTO t_emp VALUES (NULL, "李四", 24, "女", "1234@gmail.com", 2);
INSERT INTO t_emp VALUES (NULL, "王五", 25, "男", "12345@gmail.com", 3);
INSERT INTO t_emp VALUES (NULL, "赵六", 26, "女", "123456@gmail.com", 1);
INSERT INTO t_emp VALUES (NULL, "田七", 27, "男", "1234567@gmail.com", 2);

```

```sql
-- 向t_emp添加数据
INSERT INTO t_emp VALUES (NULL, "张三", 23, "男", "123@gmail.com", 1);
INSERT INTO t_emp VALUES (NULL, "李四", 24, "女", "1234@gmail.com", 2);
INSERT INTO t_emp VALUES (NULL, "王五", 25, "男", "12345@gmail.com", 3);
INSERT INTO t_emp VALUES (NULL, "赵六", 26, "女", "123456@gmail.com", 1);
INSERT INTO t_emp VALUES (NULL, "田七", 27, "男", "1234567@gmail.com", 2);

-- 创建一个t_dept表
CREATE TABLE t_dept(
	did INT PRIMARY KEY AUTO_INCREMENT, 
	dept_name VARCHAR(20)
);


-- 向t_dept添加数据
INSERT INTO t_dept VALUES (NULL, "A");
INSERT INTO t_dept VALUES (NULL, "B");
INSERT INTO t_dept VALUES (NULL, "C");

-- 查看t_dept字段
SELECT * FROM t_dept;

```

创建Emp和Dept类，属性分别如下：

```java
public class Emp {

	private Integer eid;

    private String empName;

    private Integer age;

    private String sex;

    private String email;

}
```

```java
public class Dept {
    private Integer did;

    private String deptName;
}
```

接下来我们创建测试类，来测试一下：

```java
public interface EmpMapper {
    /**
     * 查询所有emp字段
     */
    List<Emp> getAllEmp();
}
```

```xml
<!--EmpMapper.xml文件如下-->

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.linxuan.mybatis.mapper.EmpMapper">

    <!--List<Emp> getAllEmp();-->
    <select id="getAllEmp" resultType="emp">
        select * from t_emp;
    </select>

</mapper>
```

```java
public class demo01 {
    @Test
    public void getAllEmpTest() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

        List<Emp> allEmp = mapper.getAllEmp();
        allEmp.forEach(emp -> System.out.println(emp));
    }
}

/*
    Emp{eid=1, empName='null', age=23, sex='男', email='123@gmail.com'}
    Emp{eid=2, empName='null', age=24, sex='女', email='1234@gmail.com'}
    Emp{eid=3, empName='null', age=25, sex='男', email='12345@gmail.com'}
    Emp{eid=4, empName='null', age=26, sex='女', email='123456@gmail.com'}
    Emp{eid=5, empName='null', age=27, sex='男', email='1234567@gmail.com'}
*/
```

我们可以看到`empName='null'`，这是为什么呢？仔细想想，我们在数据库中的name字段名称是`emp_name`，而在Emp类中name的属性名称为`empName`。他们都没有错误，都是按照正常的规则建立的。

但是因为名称不对，当我们调用查询语句`select * from t_emp;`查询出来结果时会赋值给`List<Emp>`集合，而集合的泛型是Emp，对于返回值当然是各找各妈了，但是name属性和字段的名称不一样，所以这里肯定是null了。

## 4.2 处理字段和属性映射关系

对于上述这种情况，我们有三种解决方案：

1. **通过字段别名解决字段名和属性名的映射关系**

   起别名：`select 字段名 as 新的名称，...from 表名;`as关键字可以省略，用空格来代替

   我们知道，究其原因是因为名字不同导致的，那么我们修改一下名字就可以了呗！但是，因为这两种命名方式都是正确的，所以我们不能够随意修改，但是我们可以创建一个别名啊，为emp_name字段创建一个别名就可以了。

   将查询语句修改一下就可以了：

   ```xml
   <!--List<Emp> getAllEmp();-->
   <select id="getAllEmp" resultType="emp">
       select eid,emp_name empName, age, sex, email  from t_emp;
   </select>
   ```

2. **全局配置`mapUnderscoreToCamelCase`解决字段名和属性名的映射关系**

   可以在MyBatis的核心配置文件中的`setting`标签中，设置一个全局配置信息mapUnderscoreToCamelCase，可以在查询表中数据时，自动将`_`类型的字段名转换为驼峰，例如：字段名`user_name`，设置了`mapUnderscoreToCamelCase`，此时字段名就会转换为userName。

   ```xml
   <!--设置Mybatis全局配置-->
   <settings>
       <setting name="mapUnderscoreToCamelCase" value="true"/>
   </settings>
   ```

   这时我们将查询语句修改回去，那么就可以查询全部的用户信息了：

   ```xml
   <!--List<Emp> getAllEmp();-->
   <select id="getAllEmp" resultType="emp">
       select * from t_emp;
   </select>
   ```

3. **resultMap处理字段和属性的映射关系**

   若字段名和实体类中的属性名不一致，则可以通过resultMap设置自定义映射，<font color = "red">即使字段名和属性名一致的属性也要映射，也就是全部属性都要列出来</font>

   ```xml
   <!--
   	- id：表示自定义映射的唯一标识，不能重复
   	- type：查询的数据要映射的实体类的类型  
   -->
   <resultMap id="empResultMap" type="Emp">
       <!--
   		- id：设置主键的映射关系  
   		- result：设置普通字段的映射关系
     			- property：设置映射关系中实体类中的属性名  
   			- column：设置映射关系中表中的字段名
   	-->
   	<id property="eid" column="eid"></id>
   	<result property="empName" column="emp_name"></result>
   	<result property="age" column="age"></result>
   	<result property="sex" column="sex"></result>
   	<result property="email" column="email"></result>
   </resultMap>
   <!--List<Emp> getAllEmp();-->
   <select id="getAllEmp" resultMap="empResultMap">
   	select * from t_emp
   </select>
   ```

   属性：  

   - id：表示自定义映射的唯一标识，不能重复
   - type：查询的数据要映射的实体类的类型  

   子标签：  

   - id：设置主键的映射关系  
   - result：设置普通字段的映射关系  
   - 子标签属性：  
     - property：设置映射关系中实体类中的属性名  
     - column：设置映射关系中表中的字段名


## 4.3 SQL多表查询

<!--P521-->

多表查询语法：

```sql
-- 创建部门表
CREATE TABLE dept(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20)
);

INSERT INTO dept (NAME) VALUES ("开发部"), ("市场部"), ("财务部");
```

```sql
-- 创建员工表
CREATE TABLE emp(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20),
	gender CHAR(1),
	salary DOUBLE,
	join_date DATE,
	dept_id INT,
	FOREIGN KEY (dept_id) REFERENCES dept(id)	-- 外键，关联部门表
);

INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("孙悟空", "男", 7200, "2013-02-24", 1);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("猪八戒", "男", 3600, "2010-12-02", 1);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("唐僧", "男", 9000, "2008-08-08", 2);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("白骨精", "女", 5000, "2015-10-07", 2);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("蜘蛛精", "女", 4500, "2011-3-14", 3);
```

多表查询语句：

```sql
select * from dept, emp;
```

**笛卡尔积**：

* 有两个集合，A，B，取两个集合的所有组成情况。例如上面的表，有两个表，外键存在，笛卡尔积为15，所以上面使用多表查询语句查询后会出来15条结果。
* 因此要完成多表查询，需要消除无用的数据。

多表查询分类。一共有三种：内连接查询，外连接查询，子查询

### 内连接查询

<!--P522-->

隐式内连接

```sql
SELECT * FROM emp, dept WHERE emp.`dept_id` = dept.`id`;
```

```sql
-- 查询员工表的姓名，性别。部门表的名称
SELECT emp.`name`, emp.`gender`, dept.`name` FROM emp, dept WHERE emp.`dept_id` = dept.`id`;
```

```sql
-- 这是最规范的写法
SELECT 
	t1.`name`,	-- 员工表的姓名
	t1.`gender`,	-- 员工表的性别
	t2.`name`	-- 部门表的名称
FROM
	emp t1, 	-- 对员工表进行重命名
	dept t2		-- 对部门表进行重命名
WHERE 
	t1.`dept_id` = t2.`id`;
```

显示内连接

* 语法：

  ```sql
  select 字段列表 from 表名1 inner join 表名2 on 条件
  ```

* 例如：

  ```sql
  SELECT * FROM emp INNER JOIN dept ON emp.`dept_id` = dept.`id`;
  SELECT * FROM emp [INNER] JOIN dept ON emp.`dept_id` = dept.`id`;	-- inner可选 
  ```

内连接查询需要了解以下：

1. 从那些表中查询数据
2. 条件是什么
3. 查询那些字段

### 外连接查询

<!--P523-->

1. 左外连接：查询的是左表所有的数据以及其交集部分。

   ```sql
   select 字段列表 from 表1 left [outer] join 表2 on 条件
   ```

   ```sql
   SELECT emp.*, dept.name FROM emp LEFT JOIN dept ON emp.`dept_id` = dept.`id`;
   ```

2. 右外连接：查询的是右表所有的数据以及其交集部分。

### 子查询

<!--P524-->

子查询：查询中嵌套查询，称嵌套查询为子查询。

```sql
-- 查询工资最高的员工信息是多少 9000
select max(salary) from emp;
```

```sql
-- 查询工资等于9000的员工信息
select * from emp where emp.'salary' = 9000;
```

```sql
-- 以上操作可以合为一项
select * from emp where emp.'salary' = (select max(salary) from emp);
```

<!--P525 1.24-->

子查询的不同情况：

1. 子查询的结果是单行单列的。

   子查询作为条件，使用运算符来判断。运算符：`> >= < <= =`

   ```sql
   -- 查询平均工资
   SELECT AVG(salary) FROM emp;
   -- 查询小于平均工资的
   SELECT * FROM emp WHERE emp.`salary` < 5860;
   -- 子查询，来直接查询小于平均工资的
   SELECT * FROM emp WHERE emp.`salary` < (SELECT AVG(salary) FROM emp);
   ```

   子查询的定义是，查询中嵌套查询，称嵌套查询为子查询。在上述语句中，子查询为查询平均工资的语句，该语句的结果正是单行单列的。

2. 子查询的结果是多行单列的。

   子查询作为条件，使用运算符in来判断，相当于or。

   ```sql
   -- 查询“财务部”和“市场部”所有的员工信息
   -- 查询“财务部”和“市场部”所有的员工信息的id
   SELECT id FROM dept WHERE NAME = "财务部" OR NAME = "市场部";
   -- 查询id信息
   SELECT * FROM emp WHERE dept_id = 3 OR dept_id = 2;
   -- 查询“财务部”和“市场部”所有的员工信息
   SELECT * FROM emp WHERE dept_id IN(SELECT id FROM dept WHERE NAME = "财务部" OR NAME = "市场部");
   ```

3. 子查询的结果是多行多列的

   <!--P526-->

   子查询可以作为一张虚拟表，参与查询

   ```sql
   -- 查询员工入职日期是2011-11-11日之后的员工信息和部门信息
   -- 普通内连接
   SELECT * FROM emp, dept WHERE emp.`dept_id` = dept.`id` AND emp.`join_date` > "2011-11-11";
   -- 子查询
   SELECT * FROM dept t1, (SELECT * FROM emp WHERE join_date > "2011-11-11") t2 WHERE t1.id = t2.dept_id;
   ```

### 多表查询练习

<!--P527-->

创建表并添加信息：

```sql
-- 部门表
CREATE TABLE dept (
	id INT PRIMARY KEY ,	-- 部门id
	dname VARCHAR(20), 	-- 部门名称
	location VARCHAR(20)	-- 部门所在地
);
-- 添加四个部门
INSERT INTO dept (id, dname, location) VALUES
(10, "教研部", "北京"),
(20, "学工部", "上海"), 
(30, "销售部", "广州"), 
(40, "财务部", "深圳");
```

```sql
-- 职务表
CREATE TABLE job (
	id INT PRIMARY KEY, 	-- 职务id
	jname VARCHAR(20), 	-- 职务名称
	description VARCHAR(50)	-- 职务描述
);
-- 添加四个职务
INSERT INTO job (id, jname, description) VALUES 
(1, "董事长", "管理整个公司"), 
(2, "经理", "管理部门员工"), 
(3, "销售员", "向客人推销产品"), 
(4, "文员", "使用办公软件");
```

```sql
-- 员工表
CREATE TABLE emp (
	id INT PRIMARY KEY, 	-- 员工id
	ename VARCHAR(20), 	-- 员工姓名
	job_id INT,		-- 职务id
	mgr INT, 		-- 上级领导
	joindate DATE, 		-- 入职日期
	salary DECIMAL(7, 2),	-- 工资
	bonus DECIMAL(7, 2), 	-- 奖金
	dept_id INT, 		-- 所在部门编号
	CONSTRAINT emp_jobid_ref_job_id_fk FOREIGN KEY (job_id) REFERENCES job (id), 
	CONSTRAINT emp_deptid_ref_dept_id_fk FOREIGN KEY (dept_id) REFERENCES dept(id)
);
-- 添加员工
INSERT INTO emp (id, ename, job_id, mgr, joindate, salary, bonus, dept_id) VALUES 
(1001, "孙悟空", 4, 1004, "2000-12-17", "8000.00", NULL, 20), 
(1002, "卢俊义", 3, 1006, "2001-02-20", "1600.00", "3000.00", 30), 
(1003, "林冲", 3, 1006, "2001-02-22", "12500.00", "5000.00", 30), 
(1004, "唐僧", 2, 1009, "2001-04-02", "29750.00", NULL, 20), 
(1005, "李逵", 4, 1006, "2001-09-26", "12800.00", "14000.00", 30), 
(1006, "宋江", 2, 1009, "2001-05-01", "28500.00", NULL, 30), 
(1007, "刘备", 2, 1009, "2001-09-01", "24500.00", NULL, 10), 
(1008, "猪八戒", 4, 1004, "2007-04-19", "30000.00", NULL, 20), 
(1009, "罗贯中", 1, NULL, "2001-11-17", "50000.00", NULL, 10), 
(1010, "吴用", 3, 1006, "2001-09-08", "15000.00", "0.00", 30), 
(1011, "沙僧", 4, 1004, "2007-05-23", "11000.00", NULL, 20), 
(1012, "李逵", 4, 1006, "2001-12-03", "9500.00", NULL, 30), 
(1013, "小白龙", 4, 1004, "2001-12-03", "30000.00", NULL, 20), 
(1014, "关羽", 4, 1007, "2002-12-03", "13000.00", NULL, 10);
```

```sql
-- 工资等级表
CREATE TABLE salarygrade (
	grade INT PRIMARY KEY,	-- 级别
	losalary INT, 		-- 最低工资
	hisalary INT 		-- 最高工资
);
-- 添加5个工资等级
INSERT INTO salarygrade(grade, losalary, hisalary) VALUES 
(1, 7000, 12000), 
(2, 12010, 14000), 
(3, 14010, 20000), 
(4, 20010, 30000), 
(5, 30010, 99990);
```

问题及需求：

```sql
-- 1. 查询所有员工信息。查询员工编号，员工姓名，工资，职务名称，职务描述

-- 2. 查询员工编号，员工姓名，工资，职务名称，职务描述，部门名称，部门位置

-- 3. 查询员工姓名，工资，工资等级

-- 4. 查询员工姓名，工资，职务名称，职务描述，部门名称，部门位置，工资等级

-- 5. 查询部门编号，部门名称，部门位置，部门人数

-- 6. 查询所有员工的姓名及其直接上级的姓名，没有领导的员工也需要查询
```

回答：

```sql
-- 1. 查询所有员工信息。查询员工编号，员工姓名，工资，职务名称，职务描述
SELECT
	emp.id, emp.ename, emp.salary, emp.job_id, job.jname, job.description 
FROM 
	emp, job 
WHERE 
	emp.job_id = job.id; 
```

```sql
-- 2. 查询员工编号，员工姓名，工资，职务名称，职务描述，部门名称，部门位置
SELECT 
	emp.id, emp.ename, emp.bonus, job.jname, job.description, dept.dname, dept.`location`
FROM
	emp, job, dept
WHERE
	emp.job_id = job.id AND emp.dept_id = dept.id;
```

<!--P528-->

```sql
-- 3. 查询员工姓名，工资，工资等级
SELECT
	emp.ename,
	emp.salary, 
	salarygrade.grade
FROM
	emp, 
	salarygrade
WHERE
	emp.salary BETWEEN salarygrade.losalary AND salarygrade.hisalary;
```

```sql
-- 4. 查询员工姓名，工资，职务名称，职务描述，部门名称，部门位置，工资等级
SELECT 
	emp.ename, 
	emp.salary, 
	job.jname, 
	job.description, 
	dept.dname, 
	dept.location, 
	salarygrade.grade
FROM
	emp, 
	job, 
	dept, 
	salarygrade
WHERE
	emp.job_id = job.id AND
	emp.dept_id = dept.id AND
	emp.salary BETWEEN salarygrade.losalary AND salarygrade.hisalary;
```

<!--P529 1.25-->

```sql
-- 5. 查询部门编号，部门名称，部门位置，部门人数
SELECT
	t1.id, 
	t1.dname, 
	t1.location, 
	t2.total
FROM 
	dept t1,
	-- 让这个表作为虚拟表 
	(
	SELECT
		dept_id, 
		COUNT(id) total
	FROM
		emp
	GROUP BY 
		dept_id	
	) t2
WHERE 
	t1.id = t2.dept_id;
```

```sql
-- 6. 查询所有员工的姓名及其直接上级的姓名，没有领导的员工也需要查询
-- 这个不完善
SELECT
	t1.ename, 
	t1.mgr, 
	t2.id, 
	t2.ename
FROM
	emp t1, 
	emp t2
WHERE
	t1.mgr = t2.id;
```

```sql
-- 这个完善了
SELECT
	t1.ename, 
	t1.mgr, 
	t2.id, 
	t2.ename
FROM
	emp t1
LEFT JOIN
	emp t2
ON
	t1.mgr = t2.id;
```

## 4.4 处理多对一映射关系

我们前面搭建了一个数据库，其中有一个`t_emp`表和`t_dept`表。在`t_emp`表中有一个字段为`did`，在`t_dept`表中有字段`did`,`dept_name`。其中t_emp表的字段中的did让两个表产生了联系。

我们知道t_emp是员工表，t_dept是部门表，通常都是多个员工对应着一个部门。所以这时候我们想要查询员工信息以及员工所对应的部门信息应该怎么查呢？

* 如果不适用mybatis，只适用sql语法。这很简单，使用左外连接查询即可：`select * from t_emp left join t_dept on t_emp.eid = t_dept.did where t_emp.eid = 1`
* 但是我们必须使用Mybatis来查询，这样就会产生一个问题，我们产生的查询结果对应着Java中的类是什么？因为我们查询出来，最后会有着员工信息以及员工部门所对应的部门信息。在我们创建的Java类中没有该类，所以我们需要在Emp类中添加一条属性来让他们能够产生映射的关系。

对于多对一，我们只需要在多的一方添加一个属性：一的一方的类对象。

```java
public class Emp {  
	private Integer eid;  
	private String empName;  
	private Integer age;  
	private String sex;  
	private String email;  
    // 添加属性：Dept对象
	private Dept dept;
	//...
}
```
OK，那么我们接下来就是让查询结果分别对应Emp中的属性即可，可是对于dept属性的映射关系我们仍然需要设置一下。我们查询结果产生的有`did`,`dept_name`这正好对应着Dept类的属性，所以让他们产生映射关系就可以了。

对此我们有三种方法：

1. **级联方式处理映射关系**

   ```java
   public interface EmpMapper {
       /**
        * 查询emp字段和dept字段
        */
       Emp getEmpAndDept(@Param("eid") Integer eid);
   }
   ```

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.linxuan.mybatis.mapper.EmpMapper">
       
       <resultMap id="empAndDeptResultMapOne" type="Emp">
           <id property="eid" column="eid"></id>
           <result property="empName" column="emp_name"></result>
           <result property="age" column="age"></result>
           <result property="sex" column="sex"></result>
           <result property="email" column="email"></result>
           <result property="dept.did" column="did"></result>
           <result property="dept.deptName" column="dept_name"></result>
       </resultMap>
       
       <!--Emp getEmpAndDept(@Param("eid") Integer eid);-->
       <select id="getEmpAndDept" resultMap="empAndDeptResultMapOne">
           select * from t_emp left join t_dept on t_emp.eid = t_dept.did where t_emp.eid = #{eid}
       </select>
       
   </mapper>
   ```

   ```java
   public class demo01 {
       @Test
       public void getAllEmpTest() {
           SqlSession sqlSession = SqlSessionUtils.getSqlSession();
           EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
   
           Emp empAndDept = mapper.getEmpAndDept(1);
           System.out.println(empAndDept);
       }
   }
   // Emp{eid=1, empName='张三', age=23, sex='男', email='123@gmail.com', dept=Dept{did=1, deptName='A'}}
   ```


2. **使用association处理映射关系**

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.linxuan.mybatis.mapper.EmpMapper">
   
       <resultMap id="empAndDeptResultMapOne" type="Emp">
           <id property="eid" column="eid"></id>
           <result property="empName" column="emp_name"></result>
           <result property="age" column="age"></result>
           <result property="sex" column="sex"></result>
           <result property="email" column="email"></result>
           
           <!--
   			- association：处理多对一的映射关系
               - property：需要处理多对的映射关系的属性名
               - javaType：该属性的类型
           -->
           <association property="dept" javaType="Dept">
               <id property="did" column="did"></id>
               <result property="deptName" column="dept_name"></result>
           </association>
       </resultMap>
   
       <!--Emp getEmpAndDept(@Param("eid") Integer eid);-->
       <select id="getEmpAndDept" resultMap="empAndDeptResultMapOne">
           select * from t_emp left join t_dept on t_emp.eid = t_dept.did where t_emp.eid = #{eid}
       </select>
       
   </mapper>
   ```


3. **分步查询**

   我们可以使用分布查询来查询信息，可以第一步查询员工信息，然后根据查询的员工信息的did来查询t_dept表，然后将信息返回即可。

   1. 查询员工信息

      `EmpMapper.java`：

      ```java
      public interface EmpMapper {
          /**
           * 通过分部查询来查询emp字段和dept字段
           * 这是步骤一：先来查询emp字段
           */
          Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);
      }
      ```

      `EmpMapper.xml`：

      ```xml
      <?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE mapper
              PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
              "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
      <mapper namespace="com.linxuan.mybatis.mapper.EmpMapper">
      
          <resultMap id="getEmpAndDeptByStepResultMapOne" type="Emp">
              <id property="eid" column="eid"></id>
              <result property="empName" column="emp_name"></result>
              <result property="age" column="age"></result>
              <result property="sex" column="sex"></result>
              <result property="email" column="email"></result>
              
              <!--
      			第二步查询：
      				select：设置分布查询的sql的唯一标识（namespace.SQLId或mapper接口的全类名.方法名）
      				column：设置分步查询的条件
      		-->
              <association property="dept"
                           select="com.linxuan.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
                           column="did">
              </association>
          </resultMap>
      
          <!--Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);-->
          <select id="getEmpAndDeptByStepOne" resultMap="getEmpAndDeptByStepResultMapOne">
              select * from t_emp where eid = #{eid};
          </select>
          
      </mapper>
      ```

   2. 查询部门信息

      `DeptMapper.java`：

      ```java
      public interface DeptMapper {
          /**
           * 通过分部查询来查询emp字段和dept字段
           * 这是步骤二：查询dept字段
           */
          Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);
      }
      ```

      `DeptMapper.xml`：

      ```xml
      <?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE mapper
              PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
              "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
      <mapper namespace="com.linxuan.mybatis.mapper.DeptMapper">
      
          <!--Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);-->
          <!--
      		t_dept表与Dept类的字段和属性是不匹配的，所以需要处理字段和属性的映射关系。对于这种情况我们有三种解决方法，这里我们采用全局配置的方式。
      	-->
          <select id="getEmpAndDeptByStepTwo" resultType="Dept">
              select * from t_dept where did = #{did};
          </select>
      
      </mapper>
      ```

      `mybatis-config.xml`：

      ```xml
          <!--设置Mybatis全局配置-->
          <settings>
              <setting name="mapUnderscoreToCamelCase" value="true"/>
          </settings>
      ```
   
   3. 测试类如下：
   
      ```java
      public class demo01 {
          @Test
          public void getAllEmpTest() {
              SqlSession sqlSession = SqlSessionUtils.getSqlSession();
              EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
      
              Emp empAndDept = mapper.getEmpAndDeptByStepOne(1);
              System.out.println(empAndDept);
          }
      }
      ```

## 4.5 延迟加载

分步查询的优点：可以实现延迟加载。所以以后我们尽量使用分部查询。

想要实现延迟加载，那么我们必须在核心配置文件中设置全局配置信息：

- `lazyLoadingEnabled`：延迟加载的全局开关。当开启时，所有关联对象都会延迟加载  
- `aggressiveLazyLoading`：当开启时，任何方法的调用都会加载该对象的所有属性。 否则，每个属性会按需加载  

<font color = "red">此时就可以实现按需加载，获取的数据是什么，就只会执行相应的sql</font>。

对于上述的分步查询代码，我们进行运行，可以看到下列信息，两条Sql语句都执行了：

```java
/*
    DEBUG 05-04 12:43:50,117 ==>  Preparing: select * from t_emp where eid = ?; (BaseJdbcLogger.java:137) 
    DEBUG 05-04 12:43:50,151 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
    DEBUG 05-04 12:43:50,174 ====>  Preparing: select * from t_dept where did = ?; (BaseJdbcLogger.java:137) 
    DEBUG 05-04 12:43:50,176 ====> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
    DEBUG 05-04 12:43:50,180 <====      Total: 1 (BaseJdbcLogger.java:137) 
    DEBUG 05-04 12:43:50,181 <==      Total: 1 (BaseJdbcLogger.java:137) 
    Emp{eid=1, empName='张三', age=23, sex='男', email='123@gmail.com', dept=Dept{did=1, deptName='A'}}
*/
```

但是如果我们想要延迟加载，按需加载，获取的数据是什么，就只会执行相应的sql。那么需要在配置文件中设置一下全局配置信息：

```xml
<!--在mybatis-config.xml文件中添加下列代码-->

<settings>
	<!--开启延迟加载-->
	<setting name="lazyLoadingEnabled" value="true"/>
</settings>
```

此时我们再修改一下测试类，改成如下代码：

```java
@Test
public void getEmpAndDeptByStepOne() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
	Emp emp = mapper.getEmpAndDeptByStepOne(1);
	System.out.println(emp.getEmpName());
	System.out.println("----------------");
	System.out.println(emp.getDept());
}
```

这时的控制台打印的信息就改变了：

```java
/*
    DEBUG 05-04 13:04:52,075 ==>  Preparing: select * from t_emp where eid = ?; (BaseJdbcLogger.java:137) 
    DEBUG 05-04 13:04:52,110 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
    DEBUG 05-04 13:04:52,207 <==      Total: 1 (BaseJdbcLogger.java:137) 
    张三
    ----------------
    DEBUG 05-04 13:04:52,209 ==>  Preparing: select * from t_dept where did = ?; (BaseJdbcLogger.java:137) 
    DEBUG 05-04 13:04:52,210 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
    DEBUG 05-04 13:04:52,213 <==      Total: 1 (BaseJdbcLogger.java:137) 
    Dept{did=1, deptName='A'}
*/
```

而如果我们想要有的地方不适用延迟加载，那么我们就可以在其对应的xml文件中设置fetchType属性。此时可通过association(多对一)和collection(一对多)中的fetchType属性设置当前的分步查询是否使用延迟加载，`fetchType="lazy(延迟加载)|eager(立即加载)"`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.linxuan.mybatis.mapper.EmpMapper">

    <resultMap id="getEmpAndDeptByStepResultMapOne" type="Emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="sex" column="sex"></result>
        <result property="email" column="email"></result>
        <association property="dept"
                     select="com.linxuan.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
                     column="did"
                     fetchType="lazy">
        </association>
    </resultMap>

    <!--Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);-->
    <select id="getEmpAndDeptByStepOne" resultMap="getEmpAndDeptByStepResultMapOne">
        select * from t_emp where eid = #{eid};
    </select>
    
</mapper>
```

## 4.6 处理一对多映射关系

<font color = "red">**对一对应对象，对多对应集合**</font>

之前我们是查询Emp和Dept表的，Emp对应多个Dept表。那么接下来我们需要查询Dept和Emp表，这是一对多，那么对多对应集合，我们直接在Dept类中添加一条属性：

```java
public class Dept {
    private Integer did;
    private String deptName;
    // 添加集合属性
    private List<Emp> emps;
    
	//...
}
```
而这种的话，我们又需要设置resultMap自定义映射来处理字段和属性的映射关系了：

1. 通过两表联查来查询：

   ```java
   // DeptMapper接口添加方法
   public interface DeptMapper {
   
       /**
        * 通过两表联查来查询Dept和Emp表的字段，一对多的情况。
        * 这里我们是以部门表为主表的，所以部门表的英文在前。
        */
       Dept getDeptAndEmp(@Param("did") Integer did);
   }
   ```

   ```xml
   <!--DeptMapper.xml-->
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.linxuan.mybatis.mapper.DeptMapper">
   
       <resultMap id="getDeptAndEmpResultMap" type="Dept">
           <id property="did" column="did"></id>
           <result property="deptName" column="dept_name"></result>
           
           <!--
               - collection：用来处理一对多的映射关系
               - ofType：表示该属性对饮的集合中存储的数据的类型
           -->
           <collection property="emps" ofType="Emp">
               <id property="eid" column="eid"></id>
               <result property="empName" column="emp_name"></result>
               <result property="age" column="age"></result>
               <result property="sex" column="sex"></result>
               <result property="email" column="email"></result>
           </collection>
       </resultMap>
   
       <!--Dept getDeptAndEmp(@Param("did") Integer did);-->
       <select id="getDeptAndEmp" resultMap="getDeptAndEmpResultMap">
           select * from t_dept left join t_emp on t_dept.did = t_emp.did where t_dept.did = #{did};
       </select>
   
   </mapper>
   ```

   ```java
   public class demo01 {
       @Test
       public void getDeptAndEmp() {
           SqlSession sqlSession = SqlSessionUtils.getSqlSession();
           DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
   
           Dept deptAndEmp = mapper.getDeptAndEmp(1);
           System.out.println(deptAndEmp);
       }
   }
   
   /*
   Dept{did=1, deptName='A', emps=[Emp{eid=1, empName='张三', age=23, sex='男', 			email='123@gmail.com', dept=null}, Emp{eid=4, empName='赵六', age=26, sex='女', 	email='123456@gmail.com', dept=null}]}
   */
   ```

2. 分步查询

   1. 查询部门信息

      ```java
      public interface DeptMapper {
      
          /**
           * 通过分布查询来查询Dept和Emp表
           * 第一步：查询Dept表
           */
          Dept getDeptAndEmpByStepOne(@Param("did") Integer did);
      }
      ```

      ```xml
      <?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE mapper
              PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
              "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
      <mapper namespace="com.linxuan.mybatis.mapper.DeptMapper">
          <resultMap id="getDeptAndEmpByStepResultMap" type="Dept">
              <id property="did" column="did"></id>
              <result property="deptName" column="dept_name"></result>
              <collection property="emps"
                          select="com.linxuan.mybatis.mapper.EmpMapper.getDeptAndEmpByStepTwo"
                          column="did"/>
          </resultMap>
      
          <!--Dept getDeptAndEmpByStepOne(@Param("did") Integer did);-->
          <select id="getDeptAndEmpByStepOne" resultMap="getDeptAndEmpByStepResultMap">
              select * from t_dept where did = #{did};
          </select>
      </mapper>
      ```

   2. 根据部门id查询部门中的所有员工

      ```java
      public interface EmpMapper {
          /**
           * 通过分布查询来查询Dept和Emp表
           * 第二步：查询Emp表
           */
          List<Emp> getDeptAndEmpByStepTwo(@Param("did") Integer did);
      }
      ```

      ```xml
      <?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE mapper
              PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
              "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
      <mapper namespace="com.linxuan.mybatis.mapper.EmpMapper">
      
          <!--Emp getDeptAndEmpByStepTwo(@Param("did") Integer did);-->
          <select id="getDeptAndEmpByStepTwo" resultType="Emp">
              select * from t_emp where did = #{did};
          </select>
          
      </mapper>
      ```

   3. 测试

      ```java
      public class demo01 {
      
          @Test
          public void getDeptAndEmpStep() {
              SqlSession sqlSession = SqlSessionUtils.getSqlSession();
              DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
      
              Dept deptAndEmpByStepOne = mapper.getDeptAndEmpByStepOne(1);
              System.out.println(deptAndEmpByStepOne);
          }
      }
      /*
      Dept{did=1, deptName='A', emps=[Emp{eid=1, empName='张三', age=23, sex='男', email='123@gmail.com', dept=null}, Emp{eid=4, empName='赵六', age=26, sex='女', email='123456@gmail.com', dept=null}]}
      */
      ```


# 第五章 动态SQL和Mybatis缓存
## 5.1 常见的动态SQL标签

Mybatis框架的动态SQL技术是一种根据特定条件动态拼装SQL语句的功能，它存在的意义是为了解决拼接SQL语句字符串时的痛点问题

我们会介绍下面几种动态SQL标签：`if`、`where`、`trim`、`choose`、`when`、`otherwise`、`foreach`。

* **if**，表示判断。

  我们以后会使用数据库来查询数据，而要查询数据那么就会需要条件。例如用户查询旅游网一些信息，有的条件他是不会填写的，而这时我们将要求传递过去，那么就会让查询失败。所以这时候需要判断一下要求，我们可以使用if标签来判断。

  if标签可通过test属性（即传递过来的数据）的表达式进行判断，若表达式的结果为true，则标签中的内容会执行；反之标签中的内容不会执行

  ```java
  public interface DynamicSQLMapper {
      /**
       * 通过条件查询
       */
      List<Emp> getEmpByCondition(Emp emp);
  }
  ```

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.linxuan.mybatis.mapper.DynamicSQLMapper">
      
      <!--List<Emp> getEmpByCondition(Emp emp);-->
      <select id="getEmpByCondition" resultType="Emp">
          select * from t_emp where
      
      	<!--
          	if标签可通过test属性（即传递过来的数据）的表达式进行判断
  			我们在测试类里面输入查询的Emp信息，传递到属性中，然后判断属性是否为空或者为空字符串，然后在数据库中查询。
  			为什么用and呢？如果用&&的话会被转义，无法识别的，所以Mybatis提供了and
          -->
          <if test="empName != null and empName != ''">
              and emp_name = #{empName}
          </if>
          <if test="age != null and age != ''">
              and age = #{age}
          </if>
          <if test="sex != null and sex != ''">
              and sex = #{sex}
          </if>
          <if test="email != null and email != ''">
              and email = #{email}
          </if>
      </select>
  </mapper>
  ```

  ```java
  public class demo01 {
  
      @Test
      public void getDeptAndEmpStep() {
          SqlSession sqlSession = SqlSessionUtils.getSqlSession();
          DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
  
          // 记得在Emp类里面加上全参的构造方法
          List<Emp> empByCondition = mapper.getEmpByCondition(new Emp(null, "张三", 23, "男", "123@gmail.com"));
          System.out.println(empByCondition);
      }
  }
  // [Emp{eid=1, empName='张三', age=23, sex='男', email='123@gmail.com'}]
  ```

  而因为是判断查询，所以有的不会查询，那么有可能语句会变成这样：为`select * from t_emp where and age = ? and sex = ? and email = ?`，此时`where`会与`and`连用，SQL语句会报错。

  这时我们可以在where后面添加一个恒成立的条件`1=1`，这个恒成立条件并不会影响查询的结果。这时候SQL语句为`select * from t_emp where 1= 1 and age = ? and sex = ? and email = ?`

  ```java
      <!--List<Emp> getEmpByCondition(Emp emp);-->
      <select id="getEmpByCondition" resultType="Emp">
          select * from t_emp where 1 = 1
          <if test="empName != null and empName != ''">
              and emp_name = #{empName}
          </if>
          <if test="age != null and age != ''">
              and age = #{age}
          </if>
          <if test="sex != null and sex != ''">
              and sex = #{sex}
          </if>
          <if test="email != null and email != ''">
              and email = #{email}
          </if>
      </select>
  ```

* **where**，where和if一般结合使用：

  - 若where标签中的if条件都不满足，则where标签没有任何功能，即不会添加where关键字  
  - 若where标签中的if条件满足，则where标签会自动添加where关键字，并将条件最前方多余的and/or去掉  
  
  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.linxuan.mybatis.mapper.DynamicSQLMapper">
      
      <!--List<Emp> getEmpByCondition(Emp emp);-->
      <select id="getEmpByCondition" resultType="Emp">
          select * from t_emp
          <where>
              <if test="empName != null and empName != ''">
                  and emp_name = #{empName}
              </if>
              <if test="age != null and age != ''">
                  and age = #{age}
              </if>
              <if test="sex != null and sex != ''">
                  and sex = #{sex}
              </if>
              <if test="email != null and email != ''">
                  and email = #{email}
              </if>
          </where>
      </select>
  </mapper>
  ```
  
  > 注意：where标签不能去掉条件后多余的and/or，and和or关键字只能放在前面，不能够放在后面。
  
  ```xml
  <!--这种用法是错误的，只能去掉条件前面的and/or，条件后面的不行-->
      <!--List<Emp> getEmpByCondition(Emp emp);-->
      <select id="getEmpByCondition" resultType="Emp">
          select * from t_emp
          <where>
              <if test="empName != null and empName != ''">
                  emp_name = #{empName} and 
              </if>
              <if test="age != null and age != ''">
                  age = #{age} and 
              </if>
              <if test="sex != null and sex != ''">
                  sex = #{sex} and 
              </if>
              <if test="email != null and email != ''">
                  email = #{email}
              </if>
          </where>
      </select>
  ```

* **trim**，trim用于去掉或添加标签中的内容  

  常用属性如下：
  - prefix：在trim标签中的内容的前面添加某些内容  
  - suffix：在trim标签中的内容的后面添加某些内容 
  - prefixOverrides：在trim标签中的内容的前面去掉某些内容  
  - suffixOverrides：在trim标签中的内容的后面去掉某些内容
  
  若trim中的标签都不满足条件，则trim标签没有任何效果，也就是只剩下`select * from t_emp`
  
  ```xml
  <!--List<Emp> getEmpByCondition(Emp emp);-->
  <select id="getEmpByCondition" resultType="Emp">
  	select * from t_emp
  	<trim prefix="where" suffixOverrides="and|or">
  		<if test="empName != null and empName !=''">
  			emp_name = #{empName} and
  		</if>
  		<if test="age != null and age !=''">
  			age = #{age} and
  		</if>
  		<if test="sex != null and sex !=''">
  			sex = #{sex} or
  		</if>
  		<if test="email != null and email !=''">
  			email = #{email}
  		</if>
  	</trim>
  </select>
  ```
  
  ```java
  public class demo01 {
      @Test
      public void getDeptAndEmpStep() {
          SqlSession sqlSession = SqlSessionUtils.getSqlSession();
          DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
  
          List<Emp> empByCondition = mapper.getEmpByCondition(new Emp(null, "张三", null, "", ""));
          System.out.println(empByCondition);
      }
  }
  
  /*
  DEBUG 05-05 16:30:33,498 ==>  Preparing: select * from t_emp where emp_name = ? (BaseJdbcLogger.java:137) 
  DEBUG 05-05 16:30:33,552 ==> Parameters: 张三(String) (BaseJdbcLogger.java:137) 
  DEBUG 05-05 16:30:33,583 <==      Total: 1 (BaseJdbcLogger.java:137) 
  [Emp{eid=1, empName='张三', age=23, sex='男', email='123@gmail.com'}]
  */
  ```

* **choose、when、otherwise**

  - `choose、when、otherwise`相当于`if...else if..else`
  - when至少要有一个，otherwise至多只有一个
  
  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.linxuan.mybatis.mapper.DynamicSQLMapper">
      
      <!--List<Emp> getEmpByCondition(Emp emp);-->
      <select id="getEmpByCondition" resultType="Emp">
          select * from t_emp
          <where>
              <choose>
                  <when test="empName != null and empName != ''">
                      emp_name = #{empName}
                  </when>
                  <when test="age != null and age != ''">
                      age = #{age}
                  </when>
                  <when test="sex != null and sex != ''">
                      sex = #{sex}
                  </when>
                  <when test="email != null and email != ''">
                      email = #{email}
                  </when>
                  <otherwise>
                      did = 1
                  </otherwise>
              </choose>
          </where>
      </select>
  </mapper>
  ```
  
  ```java
  public class demo01 {
      @Test
      public void getDeptAndEmpStep() {
          SqlSession sqlSession = SqlSessionUtils.getSqlSession();
          DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
  
          List<Emp> empByCondition = mapper.getEmpByCondition(new Emp(null, "", 24, "", ""));
          System.out.println(empByCondition);
      }
  }
  /*
  DEBUG 05-05 16:47:01,757 ==>  Preparing: select * from t_emp WHERE age = ? (BaseJdbcLogger.java:137) 
  DEBUG 05-05 16:47:01,809 ==> Parameters: 24(Integer) (BaseJdbcLogger.java:137) 
  DEBUG 05-05 16:47:01,880 <==      Total: 1 (BaseJdbcLogger.java:137) 
  [Emp{eid=2, empName='李四', age=24, sex='女', email='1234@gmail.com'}]
  */
  ```

* **foreach**，属性如下：  

  - `collection`：设置要循环的数组或集合  
  - `item`：表示集合或数组中的每一个数据  
  - `separator`：设置循环体之间的分隔符，分隔符前后默认有一个空格，如` , `
  - `open`：设置foreach标签中的内容的开始符  
  - `close`：设置foreach标签中的内容的结束符
  
  1. 批量删除

     批量删除的话我们有两种方式，一种是正确的语句应该是`delete from t_emp where eid in (1,2,3)`；另一种是`delete from t_emp where eid = 1 or eid = 2 or eid = 3`。
  
     首先我们先想数据库添加一些测试数据，用于一会删除，多余的信息不用填写，直接填写emp_name即可。让数据增加到10条。现在我们来删除多余的数据：
  
     ```java
     public interface DynamicSQLMapper {
         /**
          * 删除数据
          */
         Integer deleteMoreByArray(@Param("eids") Integer[] eids);
     }
     ```
  
     ```xml
     <?xml version="1.0" encoding="UTF-8" ?>
     <!DOCTYPE mapper
             PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
             "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     <mapper namespace="com.linxuan.mybatis.mapper.DynamicSQLMapper">
     
         <!--Integer deleteMoreByArray(@Param("eids") ArrayList<Integer> eids);-->
         <delete id="deleteMoreByArray">
             delete from t_emp where eid in
             <foreach collection="eids" item="eid" close=")" open="(" separator=",">
                 #{eid}
             </foreach>
         </delete>
         
         <!--这是第二种删除方式-->
         <!--    <delete id="deleteMoreByArray">
             delete from t_emp where
             <foreach collection="eids" item="eid" close=")" open="(" separator="or">
                 eid = #{eid}
             </foreach>
         </delete>-->
     </mapper>
     ```
  
     ```java
     public class demo01 {
         @Test
         public void getDeptAndEmpStep() {
             SqlSession sqlSession = SqlSessionUtils.getSqlSession();
             DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
     
             Integer integer = mapper.deleteMoreByArray(new Integer[]{6, 7, 8, 9});
             System.out.println(integer);
         }
     }
     
     /*
     DEBUG 05-05 17:22:48,712 ==>  Preparing: delete from t_emp where eid in ( ? , ? , ? , ? ) (BaseJdbcLogger.java:137) 
     DEBUG 05-05 17:22:48,766 ==> Parameters: 6(Integer), 7(Integer), 8(Integer), 9(Integer) (BaseJdbcLogger.java:137) 
     DEBUG 05-05 17:22:48,773 <==    Updates: 4 (BaseJdbcLogger.java:137) 
     4
     
     // 4条数据被删除了
     */
     ```
  
  2. 批量添加
  
     ```java
     public interface DynamicSQLMapper {
         /**
          * 添加多条数据
          */
         Integer insertMoreByArray(@Param("emps") List<Emp> emps);
     }
     ```
  
     ```xml
     <?xml version="1.0" encoding="UTF-8" ?>
     <!DOCTYPE mapper
             PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
             "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     <mapper namespace="com.linxuan.mybatis.mapper.DynamicSQLMapper">
     
         <!--Integer insertMoreByArray(@Param("emps") List<Emp> emps);-->
         <!--
     		这里不能够使用close=")" open="("。为什么呢？因为我们使用的是数组，要求每一个数组外面加上括号。而这两个属性实在数组的最外面加上括号。不是每个数组分隔开的括号。
     		为什么最后有个null？我们在Emp类中并没有添加did属性，但是如果我们使用此条语句添加数据的时候，如果没有最后一个null，那么会报错：Column count doesn't match value count at row 1。添加的数据与字段不匹配，仔细想想也是，我们少添加了一个did字段。
     	-->
         <insert id="insertMoreByArray">
             insert into t_emp values
             <foreach collection="emps" item="emp" separator=",">
                 (null, #{emp.empName}, #{emp.age}, #{emp.sex}, #{emp.email}, null)
             </foreach>
         </insert>
     </mapper>
     ```
  
     ```java
     public class demo01 {
         @Test
         public void getDeptAndEmpStep() {
             SqlSession sqlSession = SqlSessionUtils.getSqlSession();
             DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
     
             Emp emp1 = new Emp(null, "a1", 12, "男", "123@163.com");
             Emp emp2 = new Emp(null, "a2", 12, "男", "123@163.com");
             Emp emp3 = new Emp(null, "a3", 12, "男", "123@163.com");
             List<Emp> emps = Arrays.asList(emp1, emp2, emp3);
     
             System.out.println(mapper.insertMoreByArray(emps));
         }
     }
     
     // 3
     ```

## 5.2 SQL标签与SQL片段

我们在查询数据的时候，有时候不是想要查询所有的字段，仅仅想要查询部分字段。但是部分字段也有点多，并且我们会重复利用这些字段。这时候我们就可以将其弄成公共的SQL片段。

sql片段，可以记录一段公共sql片段，在使用的地方通过include标签进行引入

- 声明sql片段：`<sql>`标签

```xml
<sql id="empColumns">eid,emp_name,age,sex,email</sql>
```
- 引用sql片段：`<include>`标签
```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select <include refid="empColumns"></include> from t_emp
</select>
```
## 5.3 MyBatis的一级缓存
**一级缓存是SqlSession级别的**，通过同一个SqlSession查询的数据会被缓存，下次查询相同的数据，就会从缓存中直接获取，不会从数据库重新访问  

使一级缓存失效的四种情况：  

1. 不同的SqlSession对应不同的一级缓存  
2. 同一个SqlSession但是查询条件不同
3. 同一个SqlSession两次查询期间执行了任何一次增删改操作
4. 同一个SqlSession两次查询期间手动清空了缓存。`SqlSession.clearCache方法`

## 5.4 MyBatis的二级缓存
**二级缓存是SqlSessionFactory级别**，通过同一个SqlSessionFactory创建的SqlSession查询的结果会被缓存；此后若再次执行相同的查询语句，结果就会从缓存中获取  

二级缓存开启的条件

1. 在核心配置文件中，设置全局配置属性`cacheEnabled="true"`，默认为`true`，不需要设置
2. 在映射文件中设置标签`<cache />`
3. 二级缓存必须在SqlSession关闭或提交之后有效
4. 查询的数据所转换的实体类类型必须实现序列化的接口

使二级缓存失效的情况：两次查询之间执行了任意的增删改，会使一级和二级缓存同时失效

## 5.5 二级缓存的相关配置
在mapper配置文件中添加的cache标签可以设置一些属性

eviction属性：缓存回收策略  
- LRU（Least Recently Used） – 最近最少使用的：移除最长时间不被使用的对象。  
- FIFO（First in First out） – 先进先出：按对象进入缓存的顺序来移除它们。  
- SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。  
- WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。
- 默认的是 LRU

flushInterval属性：刷新间隔，单位毫秒
- 默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句（增删改）时刷新

size属性：引用数目，正整数
- 代表缓存最多可以存储多少个对象，太大容易导致内存溢出

readOnly属性：只读，true/false
- true：只读缓存；会给所有调用者返回缓存对象的相同实例。因此这些对象不能被修改。这提供了很重要的性能优势。  
- false：读写缓存；会返回缓存对象的拷贝（通过序列化）。这会慢一些，但是安全，因此默认是false

## 5.7 MyBatis缓存查询的顺序
先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用  

如果二级缓存没有命中，再查询一级缓存  

如果一级缓存也没有命中，则查询数据库  

SqlSession关闭之后，一级缓存中的数据会写入二级缓存

## 5.8 整合第三方缓存EHCache
添加依赖

```xml
<!-- Mybatis EHCache整合包 -->
<dependency>
	<groupId>org.mybatis.caches</groupId>
	<artifactId>mybatis-ehcache</artifactId>
	<version>1.2.1</version>
</dependency>
<!-- slf4j日志门面的一个具体实现 -->
<dependency>
	<groupId>ch.qos.logback</groupId>
	<artifactId>logback-classic</artifactId>
	<version>1.2.3</version>
</dependency>
```
各个jar包的功能

| jar包名称 | 作用 |
| --- | --- |
| mybatis-ehcache | Mybatis和EHCache的整合包 |
| ehcache | EHCache核心包 |
| slf4j-api | SLF4J日志门面包 |
| logback-classic | 支持SLF4J门面接口的一个具体实现 |

创建EHCache的配置文件ehcache.xml

- 名字必须叫`ehcache.xml`
```xml
<?xml version="1.0" encoding="utf-8" ?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
    <!-- 磁盘保存路径 -->
    <diskStore path="D:\atguigu\ehcache"/>
    <defaultCache
            maxElementsInMemory="1000"
            maxElementsOnDisk="10000000"
            eternal="false"
            overflowToDisk="true"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            diskExpiryThreadIntervalSeconds="120"
            memoryStoreEvictionPolicy="LRU">
    </defaultCache>
</ehcache>
```
设置二级缓存的类型

- 在xxxMapper.xml文件中设置二级缓存类型
```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```
加入logback日志

- 存在SLF4J时，作为简易日志的log4j将失效，此时我们需要借助SLF4J的具体实现logback来打印日志。创建logback的配置文件`logback.xml`，名字固定，不可改变
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <!-- 指定日志输出的位置 -->
    <appender name="STDOUT"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- 日志输出的格式 -->
            <!-- 按照顺序分别是：时间、日志级别、线程名称、打印日志的类、日志主体内容、换行 -->
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger] [%msg]%n</pattern>
        </encoder>
    </appender>
    <!-- 设置全局日志级别。日志级别按顺序分别是：DEBUG、INFO、WARN、ERROR -->
    <!-- 指定任何一个日志级别都只打印当前级别和后面级别的日志。 -->
    <root level="DEBUG">
        <!-- 指定打印日志的appender，这里通过“STDOUT”引用了前面配置的appender -->
        <appender-ref ref="STDOUT" />
    </root>
    <!-- 根据特殊需求指定局部日志级别 -->
    <logger name="com.atguigu.crowd.mapper" level="DEBUG"/>
</configuration>
```
EHCache配置文件说明

| 属性名 | 是否必须 | 作用 |
| --- | --- | --- |
| maxElementsInMemory | 是 | 在内存中缓存的element的最大数目 |
| maxElementsOnDisk | 是 | 在磁盘上缓存的element的最大数目，若是0表示无穷大 |
| eternal | 是 | 设定缓存的elements是否永远不过期。 如果为true，则缓存的数据始终有效， 如果为false那么还要根据timeToIdleSeconds、timeToLiveSeconds判断 |
| overflowToDisk | 是 | 设定当内存缓存溢出的时候是否将过期的element缓存到磁盘上 |
| timeToIdleSeconds | 否 | 当缓存在EhCache中的数据前后两次访问的时间超过timeToIdleSeconds的属性取值时， 这些数据便会删除，默认值是0,也就是可闲置时间无穷大 |
| timeToLiveSeconds | 否 | 缓存element的有效生命期，默认是0.,也就是element存活时间无穷大 |
| diskSpoolBufferSizeMB | 否 | DiskStore(磁盘缓存)的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区 |
| diskPersistent | 否 | 在VM重启的时候是否启用磁盘保存EhCache中的数据，默认是false |
| diskExpiryThreadIntervalSeconds | 否 | 磁盘缓存的清理线程运行间隔，默认是120秒。每个120s， 相应的线程会进行一次EhCache中数据的清理工作 |
| memoryStoreEvictionPolicy | 否 | 当内存缓存达到最大，有新的element加入的时候， 移除缓存中element的策略。 默认是LRU（最近最少使用），可选的有LFU（最不常使用）和FIFO（先进先出 |
# 第六章 逆向工程和分页插件
正向工程：先创建Java实体类，由框架负责根据实体类生成数据库表。Hibernate是支持正向工程的

逆向工程：先创建数据库表，由框架负责根据数据库表，反向生成如下资源：  
- Java实体类  
- Mapper接口  
- Mapper映射文件

## 6.1 创建逆向工程
添加依赖和插件

```xml
<dependencies>
	<!-- MyBatis核心依赖包 -->
	<dependency>
		<groupId>org.mybatis</groupId>
		<artifactId>mybatis</artifactId>
		<version>3.5.9</version>
	</dependency>
	<!-- junit测试 -->
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.13.2</version>
		<scope>test</scope>
	</dependency>
	<!-- MySQL驱动 -->
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>8.0.27</version>
	</dependency>
	<!-- log4j日志 -->
	<dependency>
		<groupId>log4j</groupId>
		<artifactId>log4j</artifactId>
		<version>1.2.17</version>
	</dependency>
</dependencies>
<!-- 控制Maven在构建过程中相关配置 -->
<build>
	<!-- 构建过程中用到的插件 -->
	<plugins>
		<!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
		<plugin>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-maven-plugin</artifactId>
			<version>1.3.0</version>
			<!-- 插件的依赖 -->
			<dependencies>
				<!-- 逆向工程的核心依赖 -->
				<dependency>
					<groupId>org.mybatis.generator</groupId>
					<artifactId>mybatis-generator-core</artifactId>
					<version>1.3.2</version>
				</dependency>
				<!-- 数据库连接池 -->
				<dependency>
					<groupId>com.mchange</groupId>
					<artifactId>c3p0</artifactId>
					<version>0.9.2</version>
				</dependency>
				<!-- MySQL驱动 -->
				<dependency>
					<groupId>mysql</groupId>
					<artifactId>mysql-connector-java</artifactId>
					<version>8.0.27</version>
				</dependency>
			</dependencies>
		</plugin>
	</plugins>
</build>
```
创建MyBatis的核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="jdbc.properties"/>
    <typeAliases>
        <package name=""/>
    </typeAliases>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <package name=""/>
    </mappers>
</configuration>
```
创建逆向工程的配置文件

- 文件名必须是：`generatorConfig.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--
        targetRuntime: 执行生成的逆向工程的版本
        MyBatis3Simple: 生成基本的CRUD（清新简洁版）
        MyBatis3: 生成带条件的CRUD（奢华尊享版）
    -->
    <context id="DB2Tables" targetRuntime="MyBatis3Simple">
        <!-- 数据库的连接信息 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/mybatis"
                        userId="root"
                        password="root">
        </jdbcConnection>
        <!-- javaBean的生成策略-->
        <javaModelGenerator targetPackage="com.linxuan.mybatis.pojo" targetProject=".\src\main\java">
            <!--让每一个点变成一层目录-->
            <property name="enableSubPackages" value="true" />
            <!--去掉字符串的前后面的空格，如果字段名称前后面有空格可以去掉-->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- SQL映射文件的生成策略 -->
        <sqlMapGenerator targetPackage="com.linxuan.mybatis.mapper"
                         targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- Mapper接口的生成策略 -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="com.linxuan.mybatis.mapper" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="t_emp" domainObjectName="Emp"/>
        <table tableName="t_dept" domainObjectName="Dept"/>
    </context>
</generatorConfiguration>
```
执行MBG插件的generate目标

## 6.2 QBC查询和增改
- `selectByExample`：按条件查询，需要传入一个example对象或者null；如果传入一个null，则表示没有条件，也就是查询所有数据
- `example.createCriteria().xxx`：创建条件对象，通过andXXX方法为SQL添加查询添加，每个条件之间是and关系
- `example.or().xxx`：将之前添加的条件通过or拼接其他条件
```java
@Test public void testMBG() throws IOException {
	InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
	SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
	SqlSession sqlSession = sqlSessionFactory.openSession(true);
	EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
	EmpExample example = new EmpExample();
	//名字为张三，且年龄大于等于20
	example.createCriteria().andEmpNameEqualTo("张三").andAgeGreaterThanOrEqualTo(20);
	//或者did不为空
	example.or().andDidIsNotNull();
	List<Emp> emps = mapper.selectByExample(example);
	emps.forEach(System.out::println);
}
```
- `updateByPrimaryKey`：通过主键进行数据修改，如果某一个值为null，也会将对应的字段改为null

  `mapper.updateByPrimaryKey(new Emp(1,"admin",22,null,"456@qq.com",3));`

- `updateByPrimaryKeySelective()`：通过主键进行选择性数据修改，如果某个值为null，则不修改这个字段

  `mapper.updateByPrimaryKeySelective(new Emp(2,"admin2",22,null,"456@qq.com",3));`

## 6.3 分页插件使用步骤
添加依赖

```xml
<!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
<dependency>
	<groupId>com.github.pagehelper</groupId>
	<artifactId>pagehelper</artifactId>
	<version>5.2.0</version>
</dependency>
```
配置分页插件

在MyBatis的核心配置文件（mybatis-config.xml）中配置插件

```xml
<plugins>
	<!--设置分页插件-->
	<plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
</plugins>
```
## 6.4 分页插件的使用
**开启分页功能**

在查询功能之前使用`PageHelper.startPage(int pageNum, int pageSize)`开启分页功能
- pageNum：当前页的页码  
- pageSize：每页显示的条数

```java
@Test
public void testPageHelper() throws IOException {
	InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
	SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
	SqlSession sqlSession = sqlSessionFactory.openSession(true);
	EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
	//访问第一页，每页四条数据
	PageHelper.startPage(1,4);
	List<Emp> emps = mapper.selectByExample(null);
	emps.forEach(System.out::println);
}
```

**分页相关数据**

方法一：直接输出

```java
@Test
public void testPageHelper() throws IOException {
	InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
	SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
	SqlSession sqlSession = sqlSessionFactory.openSession(true);
	EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
	//访问第一页，每页四条数据
	Page<Object> page = PageHelper.startPage(1, 4);
	List<Emp> emps = mapper.selectByExample(null);
	//在查询到List集合后，打印分页数据
	System.out.println(page);
}

/*
Page{count=true, pageNum=1, pageSize=4, startRow=0, endRow=4, total=8, pages=2, reasonable=false, pageSizeZero=false}[Emp{eid=1, empName='admin', age=22, sex='男', email='456@qq.com', did=3}, Emp{eid=2, empName='admin2', age=22, sex='男', email='456@qq.com', did=3}, Emp{eid=3, empName='王五', age=12, sex='女', email='123@qq.com', did=3}, Emp{eid=4, empName='赵六', age=32, sex='男', email='123@qq.com', did=1}]
*/
```
方法二：使用PageInfo

在查询获取list集合之后，使用`PageInfo<T> pageInfo = new PageInfo<>(List<T> list, intnavigatePages)`获取分页相关数据
- list：分页之后的数据。list中的数据等同于方法一中直接输出的page数据 
- navigatePages：导航分页的页码数

```java
@Test
public void testPageHelper() throws IOException {
	InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
	SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
	SqlSession sqlSession = sqlSessionFactory.openSession(true);
	EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
	PageHelper.startPage(1, 4);
	List<Emp> emps = mapper.selectByExample(null);
	PageInfo<Emp> page = new PageInfo<>(emps,5);
	System.out.println(page);
}

/*
PageInfo{
pageNum=1, pageSize=4, size=4, startRow=1, endRow=4, total=8, pages=2, 
list=Page{count=true, pageNum=1, pageSize=4, startRow=0, endRow=4, total=8, pages=2, reasonable=false, pageSizeZero=false}[Emp{eid=1, empName='admin', age=22, sex='男', email='456@qq.com', did=3}, Emp{eid=2, empName='admin2', age=22, sex='男', email='456@qq.com', did=3}, Emp{eid=3, empName='王五', age=12, sex='女', email='123@qq.com', did=3}, Emp{eid=4, empName='赵六', age=32, sex='男', email='123@qq.com', did=1}], 
prePage=0, nextPage=2, isFirstPage=true, isLastPage=false, hasPreviousPage=false, hasNextPage=true, navigatePages=5, navigateFirstPage=1, navigateLastPage=2, navigatepageNums=[1, 2]}
*/
```
常用数据：

- `pageNum`：当前页的页码  
- `pageSize`：每页显示的条数  
- `size`：当前页显示的真实条数  
- `total`：总记录数  
- `pages`：总页数  
- `prePage`：上一页的页码  
- `nextPage`：下一页的页码
- `isFirstPage/isLastPage`：是否为第一页/最后一页  
- `hasPreviousPage/hasNextPage`：是否存在上一页/下一页  
- `navigatePages`：导航分页的页码数  
- `navigatepageNums`：导航分页的页码，\[1,2,3,4,5]