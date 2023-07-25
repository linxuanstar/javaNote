# 第一章 MyBatis基础

MyBatis最初是Apache的一个开源项目iBatis，2010年6月这个项目由Apache Software Foundation迁移到了Google Code。随着开发团队转投Google Code旗下，`iBatis3.x`正式更名为MyBatis，代码于2013年11月迁移到Github。正因更名的原因，有些Mybatis的依赖jar包的路径任然是ibatis。

iBatis一词来源于“internet”和“abatis”的组合，是一个基于Java的持久层框架。iBatis提供的持久层框架包括SQL Maps和Data Access Objects（DAO）。

MyBatis特性：

1. MyBatis 是支持定制化SQL、存储过程以及高级映射的优秀的持久层框架。
2. MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。
3. MyBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO（Plain Old Java Objects，普通的Java对象）映射成数据库中的记录。
4. MyBatis 是一个 半自动的ORM（Object Relation Mapping，对象关系映射）框架。

MyBatis源码下载地址：https://github.com/mybatis/mybatis-3

MyBatis和其它持久化层技术对比：

- JDBC：SQL 夹杂在Java代码中耦合度高，导致硬编码内伤。维护不易且实际开发需求中 SQL 有变化，频繁修改的情况多见。代码冗长，开发效率低。
- Hibernate 和 JPA：操作简便，开发效率高。程序中的长难复杂 SQL 需要绕过框架。内部自动生产的 SQL，不容易做特殊优化。基于全映射的全自动框架，大量字段的 POJO 进行部分映射时比较困难。反射操作太多，导致数据库性能下降。
- MyBatis：轻量级，性能出色。SQL 和 Java 编码分开，功能边界清晰。Java代码专注业务、SQL语句专注数据。开发效率稍逊于HIbernate，但是完全能够接受。
## 1.1 搭建MyBatis环境
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
           <version>8.0.32</version>
       </dependency>
       <!--导入lombok，简化实体类的开发-->
       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <version>1.18.12</version>
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
                   <!-- MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver -->
                   <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                   <!--打开MySql，创建一个数据库MyBatis-->
                   <property name="url" value="jdbc:mysql://localhost:3306/MyBatis"/>
                   <property name="username" value="root"/>
                   <property name="password" value="root"/>
               </dataSource>
           </environment>
       </environments>
   </configuration>
   ```
   
3. 创建数据库和表，MyBatis数据库，tb_user表，User实体类。

   ```sql
   USE mybatis;
   
   CREATE TABLE tb_user(
   	id INT PRIMARY KEY AUTO_INCREMENT,
   	username VARCHAR(20),
   	PASSWORD VARCHAR(20),
   	age INT,
   	sex CHAR,
   	email VARCHAR(20)
   );
   
   SHOW TABLES;
   
   SELECT * FROM tb_user;
   ```

   ```java
   package com.linxuan.mybatis.pojo;
   
   @Data
   public class User {
       private Integer id;
       private String username;
       private String password;
       private Integer age;
       private String sex;
       private String email;
   }
   ```

4. 创建mapper接口。MyBatis中的mapper接口相当于以前的dao。但是区别在于，mapper仅仅是接口，我们不需要提供实现类

   <!--mapper 映射器; 映射对象; 映像器; 映射程序; 变换器;-->

   ```java
   package com.linxuan.mybatis.mapper;
   
   public interface UserMapper {
   	/**  
   	* 添加用户信息  
   	*/  
       int insertUser();
   }
   ```

5. 创建MyBatis的映射文件。ORM（Object Relationship Mapping）对象关系映射。对象指的是Java的实体类对象，关系指的是关系型数据库，映射指的是二者之间的对应关系。

   MyBatis映射文件用于编写SQL，访问以及操作表中的数据。存放的位置是`src/main/resources`目录下。映射文件的命名规则是表所对应的实体类的`类名+Mapper.xml`。例如：表`tb_user`，映射的实体类为User，所对应的映射文件为`UserMapper.xml` 。因此一个映射文件对应一个实体类，对应一张表的操作。

   MyBatis中可以面向接口操作数据，要保证两个一致：
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
           insert into tb_user values(null,'张三','123',23,'男', '12345@qq.com')
       </insert>
   </mapper>
   ```

6. 引入映射文件。在`configuration`标签里面`environments`标签下面引入映射文件。

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

   这种方式我们需要手动提交事务，如果要自动提交事务，则在获取sqlSession对象时，使用`SqlSession sqlSession = sqlSessionFactory.openSession(true);`，传入一个Boolean类型的参数，值为true，这样就可以自动提交。

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

   `SqlSession`代表Java程序和数据库之间的会话。（HttpSession是Java程序和浏览器之间的会话）。`SqlSessionFactory`是“生产”SqlSession的“工厂”。
   
   工厂模式：如果创建某一个对象，使用的过程基本固定，那么我们就可以把创建这个对象的相关代码封装到一个“工厂类”中，以后都使用这个工厂类来“生产”我们需要的对象。
   
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
       <!-- appender设置日志信息的去向 这里是ConsoleAppender控制台 -->
       <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
           <!-- 日志输出编码 -->
           <param name="Encoding" value="UTF-8" />
           <!-- 日志输出格式 -->
           <layout class="org.apache.log4j.PatternLayout">
               <param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS} %m (%F:%L) \n" />
           </layout>
       </appender>
       <!-- 日志级别：ERROR、WARN、INFO、DEBUG -->
       <!-- sql输出级别为debug级别 -->
       <logger name="java.sql">
           <level value="debug" />
       </logger>
       <!-- mybatis输出级别为info级别 -->
       <logger name="org.apache.ibatis">
           <level value="info" />
       </logger>
       <!-- Spring输出级别为info级别 -->
       <logger name="org.springframework">
           <level value="info"/>
       </logger>
       <root>
           <level value="debug" />
           <appender-ref ref="STDOUT" />
       </root>
   </log4j:configuration>
   ```
   
   > 日志的级别：FATAL(致命)>ERROR(错误)>WARN(警告)>INFO(信息)>DEBUG(调试) 从左到右打印的内容越来越详细
   >

对于执行mybatis的代码的时候，有很多的代码重复度很高，那么就可以抽取出来作为工具类使用：

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
```

## 1.2 MyBatis简单查询

查询`select`必须设置`resultType`或`resultMap`属性，该属性用于设置实体类和数据库表的映射关系。`resultType`是自动映射，用于属性名和表中字段名一致的情况。`resultMap`是自定义映射，用于一对多或多对一或字段名和属性名不一致的情况。

当查询的数据为多条时，只能使用集合作为返回值，否则会抛出异常`TooManyResultsException`；但是若查询的数据只有一条，可以使用实体类或集合作为返回值。

查询一个实体类对象

```xml
<!--User findById();-->
<select id="findById" resultType="com.linxuan.mybatis.pojo.User">
    select * from tb_user where id = 1;
</select>
```

查询集合

```xml
<!--List<User> findAll();-->
<select id="findAll" resultType="com.linxuan.mybatis.pojo.User">
    select * from tb_user;
</select>
```

## 1.3 MyBatis获取参数

前面MyBatis简单查询的时候没有任何参数的传递，所以接下来讲解一下如何获取参数。MyBatis获取参数值一共有两种方式：`${}`和`#{}`。`${}`的本质就是字符串拼接，`#{}`的本质就是占位符赋值。

`${}`使用字符串拼接的方式拼接sql，若为字符串类型或日期类型的字段进行赋值时，需要手动加单引号；`#{}`使用占位符赋值的方式拼接sql，此时为字符串类型或日期类型的字段进行赋值时，可以自动添加单引号。

### 1.3.1 参数种类及获取

参数有如下种类：单个字面量类型的参数（String username）、多个字面量类型的参数（String username, String password）、map集合类型的参数（Map<String, Object> hashMap）、实体类类型的参数（User user）。

**单个字面量类型的参数**

若mapper接口中的方法参数为单个的字面量类型，此时可以使用`${}`和`#{}`以任意的名称（最好见名识意）获取参数的值，注意`${}`需要手动加单引号

```xml
<!--User getUserByUsername(String username);-->
<select id="getUserByUsername" resultType="com.linxuan.mybatis.pojo.User">
    <!--使用${}获取参数-->
    select * from tb_user where username = '${username}';
    <!--使用#{}-->
    select * from tb_user where username = #{username};
</select>
```

**多个字面量类型的参数**

若mapper接口中的方法参数为多个时，此时MyBatis会自动将这些参数放在一个map集合中。这时候的获取方式有两种：以arg0，arg1...为键，以参数为值；以param1，param2...为键，以参数为值；

因此通过`${}`和`#{}`访问map集合的键就可以获取相对应的值，注意`${}`需要手动加单引号。使用arg或者param都行，要注意的是，arg是从arg0开始的，param是从param1开始的。它们都放在了同一个Map集合中，所以可以混着用，也无所谓的配套了。

```xml
<!--User checkLogin(String username, String password);-->
<select id="checkLogin" resultType="com.linxuan.mybatis.pojo.User">  
    <!--以param1，param2...为键，以参数为值-->
    select * from tb_user where username = '${param1}' and password = '${param2}';
    <!--以arg0，arg1...为键，以参数为值-->
    select * from tb_user where username = #{arg0} and password = #{arg1}; 
</select>
```

**map集合类型的参数**

若mapper接口中的方法需要的参数为多个时，可以创建map集合，将这些数据放在map中。通过`${}`和`#{}`访问map集合的键就可以获取相对应的值，注意`${}`需要手动加单引号。

```java
/**
 * 通过集合来查询
 */
User getUserByMap(Map<String, Object> hashMap);
```

```xml
<!--User getUserByMap(Map<String, Object>);-->
<select id="getUserByMap" resultType="com.linxuan.mybatis.pojo.User">
    select * from tb_user where username = #{username} and password = #{password};
</select>
```

```java
@Test
public void testUserByMap() {
    // 之前抽取了一个工具类
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

若mapper接口中的方法参数为实体类对象时此时可以使用`${}`和`#{}`访问实体类对象中的属性名获取属性值，注意`${}`需要手动加单引号。

```xml
<!--int insertUser(User user);-->
<insert id="insertUser">
	insert into tb_user value(null, #{username}, #{password}, #{age}, #{sex}, #{email})
</insert>
```

```java
@Test
public void testInsertUser() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    User user = new User(null, "Tom", "123456", 12, "男", "123@321.com");
    mapper.insertUser(user);
}
```

### 1.3.2 @Param标识参数

可以通过`@Param`注解标识mapper接口中的方法参数，标识之后会将这些参数放在map集合中。获取参数的方式有两种：以`@Param`注解的value属性值为键，以参数为值；以param1，param2...为键，以参数为值；

通过`${}`和`#{}`访问map集合的键就可以获取相对应的值，注意`${}`需要手动加单引号。

```xml
<!--User checkLoginByParam(@Param("username") String username,@Param("password") String password);-->
<select id="checkLoginByParam" resultType="com.linxuan.mybatis.pojo.User">
    select * from tb_user where username = #{username} and password = #{password}
</select>
```

```java
@Test
public void testCheckLoginByParam() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    mapper.CheckLoginByParam("admin","123456");
}
```

## 1.4 查询结果分析

在MyBatis中，对于Java中常用的类型都设置了类型别名。例如：`java.lang.Integer-->int|integer`、`int-->_int|_integer`、`Map-->map,List-->list`。

通常查询数据之后返回的结果有三种：查询失败、返回一条数据、返回多条数据。对于查询失败我们不用处理，接下来详解一下另外两种。

**返回一条数据**

如果查询出的数据只有一条，可以通过实体类对象接收、List集合接收、List集合接收。

```java
/**
 * 根据用户id查询用户信息，通过实体类对象接收
 * @param id
 * @return
 */
User getUserById(@Param("id") int id);
```

```java
/**
 * 查询所有用户信息，但是数据库里面只有一条数据，所以可以使用List集合接收。当然多条数据也可以使用集合接收
 * @return
 */
List<User> getUserList();
```

```java
/**  
 * 根据用户id查询用户信息为map集合  
 * @param id  
 * @return  
 */  
Map<String, Object> getUserToMap(@Param("id") int id);
```

**返回多条数据**

如果查询出的数据有多条，一定不能用实体类对象接收，会抛异常`TooManyResultsException`。可以通过实体类类型的List集合接收、Map类型的List集合接收、

```xml
<!--通过实体类类型的List集合接收-->
<!--List<User> getAllUser();-->
<select id="getAllUser" resultType="com.linxuan.mybatis.pojo.User">
    select * from tb_user;
</select>
```

```xml
<!--List<Map<String, Object>> getAllUserToMap();-->  
<!--将表中的数据以map集合的方式查询，一条数据对应一个map；将这些map放在一个list集合中获取-->
<select id="getAllUserToMap" resultType="map">  
	select * from tb_user  
</select>
<!--
	结果：
	[{password=123456, sex=男, id=1, age=23, username=admin},
	{password=123456, sex=男, id=2, age=23, username=张三},
	{password=123456, sex=男, id=3, age=23, username=张三}]
-->
```

在mapper接口的方法上添加`@MapKey`注解

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
	select * from tb_user
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

## 1.5 核心配置文件

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
            <!--JDBC设置当前环境的事务管理都必须手动处理，MANAGED设置事务被管理-->
            <transactionManager type="JDBC"/>
            <!--type="POOLED"使用数据库连接池，会将创建的连接进行缓存，下次使用可以从缓存中直接获取-->
            <!--type="UNPOOLED"不使用数据库连接池，即每次使用连接都需要重新创建-->
            <!--type="JNDI" 调用上下文中的数据源-->
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

**properties**

创建`jdbc.properties`文件：

```properties
# com.mysql.cj.jdbc.Driver驱动是MySQL8的，如果是之前的版本可以试试com.mysql.jdbc.Driver驱动
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/MyBatis
jdbc.username=root
jdbc.password=root
```

引入properties文件：

```xml
<!--引入properties文件，此时就可以${属性名}的方式访问属性值-->
<properties resource="jdbc.properties"/>
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

**settings**

```xml
<!--设置Mybatis全局配置-->
<settings>
    <!-- 开启驼峰命名 -->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
    <!--开启延迟加载-->
    <setting name="lazyLoadingEnabled" value="true"/>
    <!-- 打印sql日志，也可以将Value设置为LOG4J，然后搞一个Log4J的配置文件，上面已经弄过了 -->
    <setting name="logImpl" value="STDOUT_LOGGING" />
</settings>
```

**environment**

| 标签               | 作用                           | 属性    | 作用和取值                    |
| ------------------ | ------------------------------ | ------- | ----------------------------- |
| environments       | 设置多个连接数据库的环境       | default | 设置默认使用的环境的id        |
| environment        | 设置具体的连接数据库的环境信息 | id      | 设置环境的唯一标识            |
| transactionManager | 设置事务管理方式               | type    | type="JDBC\|MANAGED"          |
| dataSource         | 设置数据源                     | type    | type="POOLED\|UNPOOLED\|JNDI" |

**typeAliases**

| 标签        | 作用                                                         |
| ----------- | :----------------------------------------------------------- |
| typeAliases | 设置类型的别名                                               |
| typeAlias   | 设置某个具体的类型的别名。属性是type和alias。                |
| package     | 以包为单位，设置该包下所有的类型都拥有默认的别名，即类名且不区分大小写 |

| typeAlias标签属性 | 作用                                                         |
| ----------------- | ------------------------------------------------------------ |
| type              | 需要设置别名的类型的全类名                                   |
| alias             | 设置别名且别名不区分大小写。若不设置此属性，该类型拥有默认的别名，即类名 |

```xml
<!-- 对于resultType="com.linxuan.mybatis.pojo.User" 全类名很长，我们可以使用别名-->
<select id="getAllUser" resultType="com.linxuan.mybatis.pojo.User">
	select * from tb_user;
</select>
```

```xml
<typeAliases>
    <!--以包为单位，设置该包下所有的类型都拥有默认的别名，即类名且不区分大小写-->
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

## 1.6 MyBatis注解开发

这种方式是将映射文件直接给抹去，在mapper/dao接口的方法上写上SQL。例如：

```java
// @Insert、@Delete、@Update、@Select对应<insert/><delete/><update/><select/>
public interface UserMapper {
	/**  
	* 添加用户信息  
	*/  
    @Insert("insert into tb_user values(null,'张三','123',23,'男', '12345@qq.com')")
    int insertUser();
    
    /**  
	* 根据用户id查询信息
	*/  
    @Select("select * from tb_user where id = 1;")
    User findById();
    
    /**  
	* 查询所有用户信息
	*/  
    @Select("select * from tb_user;")
    List<User> findAll();
}
```

虽然这种方式抹去了映射文件，但是也必须在核心配置文件中指明映射文件。

```xml
<!--引入映射文件 可以不创建这些包和映射文件，但是必须在核心配置文件中配置-->
<mappers>
    <package name="com.linxuan.mybatis.mapper"/>
</mappers>
```

这种方式仅适合简单SQL的编写，复杂SQL根本不行，所以还是建议使用映射文件编写。

后面会学习Spring整合MyBatis，到时候可以不用配置文件，直接全部使用配置类来编写

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan?useSSL=false
jdbc.username=root
jdbc.password=root
```

```java
// 配置类上添加@PropertySource注解，该注解用于加载properties文件。必须添加该注解，否则不会加载这些数据
@PropertySource("classpath:jdbc.properties")
public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    // @Bean注解将该方法的返回值制作为SpringIOC容器管理的一个Bean对象
    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
```

```java
public class MyBatisConfig {
    // 定义bean，SqlSessionFactoryBean，用于产生SqlSessionFactory对象
    // 这里的dataSource会自动装配进去，我们不用管
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        // 设置模型类的别名扫描，这个等于<typeAliases><package name="com.linxuan.pojo"></typeAliases>
        ssfb.setTypeAliasesPackage("com.linxuan.domain");
        // 设置数据源
        ssfb.setDataSource(dataSource);
        return ssfb;
    }

    // 定义bean，返回MapperScannerConfigurer对象
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        // MapperScannerConfigurer对象处理原始配置文件中的mappers相关配置，加载数据层的Mapper接口类
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        // 用来设置所扫描的包路径，等于配置文件中<mappers><package name="com.linxuan.dao"></package>
        msc.setBasePackage("com.linxuan.dao");
        return msc;
    }
}
```

# 第二章 特殊SQL的执行

对于这些特殊SQL主要是考察MyBatis获取参数值的两种方式：`${}`和`#{}`。

`${}`的本质就是字符串拼接，`#{}`的本质就是占位符赋值。`${}`使用字符串拼接的方式拼接sql，若为字符串类型或日期类型的字段进行赋值时，需要手动加单引号；`#{}`使用占位符赋值的方式拼接sql，此时为字符串类型或日期类型的字段进行赋值时，可以自动添加单引号。

## 2.1 模糊查询

MySQL中使用`LIKE`关键字来进行模糊查询，后面的`_`匹配单个字符，`%`匹配任意个字符。

在Mybatis中使用模糊查询的化一共有三种方式，如下：

* `like '%${mohu}%'`：这种方式获取参数的时候只能使用`${}`。`#{}`的本质是占位符`?`，当使用`#{}`的时候会替换为`?`，而因为模糊查询是有单引号的，所以会将`?`变成字符，导致无法进行替换。另外使用`${}`不用加上`''`。
* `like concat('%',#{mohu},'%')`：使用拼接函数来拼接。
* `like "%"#{mohu}"%"`：这种方式最常用。

```java
/**
 * 根据用户名进行模糊查询
 * @param username 
 * @return java.util.List<com.linxuan.mybatis.pojo.User>
 * @date 2022/2/26 21:56
 */
List<User> getUserByLike(@Param("username") String username);
```
```xml
<!--List<User> getUserByLike(@Param("username") String username);-->
<select id="getUserByLike" resultType="User">
	<!--select * from tb_user where username like '%${mohu}%'-->  
	<!--select * from tb_user where username like concat('%',#{mohu},'%')-->  
	select * from tb_user where username like "%"#{mohu}"%"
</select>
```
## 2.2 批量删除

对于批量删除的语句，我们这里只能使用`${}`并且`${}`不加引号。如果使用`#{}`，则解析后的sql语句为`delete from tb_user where id in ('1, 2, 3')`，这样是将`1, 2, 3`看做一个整体，只有id为`1, 2, 3`的数据会被删除。

正确的语句应该是`delete from tb_user where id in (1, 2, 3)`。

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
	delete from tb_user where id in (${ids})
</delete>
```
```java
//测试类
@Test
public void testDeleteMore() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
	int result = mapper.deleteMore("1, 2, 3, 8");
	System.out.println(result);
}
```
## 2.3 动态设置表名
只能使用`${}`，因为表名不能加单引号

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
## 2.4 获取自增主键的值
有两张表，班级表和学生表：

```sql
create table t_clazz(
    clazz_id int primary key auto_increment comment "班级ID",
    clazz_name varchar(20) not null comment "班级名称"
) comment "班级表";
```

```sql
create table t_student(
    stu_id int primary key auto_increment comment "学生ID",
    stu_name varchar(20) not null comment "学生姓名",
    clazz_id int comment "该学生所在班级ID",
    foreign key (clazz_id) references t_clazz(clazz_id)
) comment "学生表";
```

两个表刚刚创建内容都为空，如果想要添加信息必须首先添加班级表的信息，然后再添加学生表的信息，这样学生表添加的时候能够获取到外键。可是向学生表添加信息的时候并不知道班级ID。所以需要获取班级ID，然后为班级分配学生。

```sql
# t_clazz表添加信息 这时候clazz_id=1
insert into t_clazz values(null, "1班");
# 向1班添加信息 可是正常来说我们并不知道这个班级的ID
insert into t_student values(null, "林炫", 1);
```

那么问题来了，我们怎么知道向`t_student`表添加信息的时候班级的ID呢？可以这样思考：向`t_clazz`表添加信息的时候返回过来班级ID就可以了。

MyBatis的mapper.xml中设置两个属性可以完成这件事情：`useGeneratedKeys`、`keyProperty`。

| 属性             | 作用                                               |
| ---------------- | -------------------------------------------------- |
| useGeneratedKeys | 设置使用自增的主键                                 |
| keyProperty      | 将获取的自增的主键放在传输的参数对象的某个属性中。 |

接下来操作一下：

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Clazz {
    Integer clazzId;
    String clazzName;
}
```

```java
/**
 * 操作Clazz表
 */
public interface ClazzDao {
	// 插入数据
    int insertClazz(Clazz clazz);
}
```

```xml
<!-- int insertClazz(Clazz clazz); -->
<insert id="insertClazz" useGeneratedKeys="true" keyProperty="clazzId">
    insert into t_clazz values(null, #{clazzName});
</insert>
```

```java
@Test
public void testInsertClazz() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    ClazzDao clazzDao = sqlSession.getMapper(ClazzDao.class);
    Clazz clazz = new Clazz(null, "计算机");
    int ret = clazzDao.insertClazz(clazz);
    // Clazz(clazzId=5, clazzName=计算机)
    System.out.println(clazz); 
}
```

# 第三章 自定义映射resultMap

首先来搭建一下环境：创建一个tb_emp和tb_dept表，让两个表产生关联：

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;
-- 关闭外键约束
SET FOREIGN_KEY_CHECKS = 0;

# 如果这个表存在，那么就删除
Drop TABLE IF EXISTS tb_dept;
-- 创建一个tb_dept 部门表
CREATE TABLE IF NOT EXISTS tb_dept(
	did INT PRIMARY KEY AUTO_INCREMENT, 
	dept_name VARCHAR(20)
);

-- 向tb_dept添加数据
INSERT INTO tb_dept VALUES (NULL, "A"), (NULL, "B"), (NULL, "C");
-- 查看tb_dept字段
SELECT * FROM tb_dept;
```

```sql
# 如果这个表存在，那么就删除
Drop TABLE IF EXISTS tb_emp;
-- 创建一个tb_emp表
CREATE TABLE tb_emp (
    eid INT PRIMARY KEY AUTO_INCREMENT,
    emp_name VARCHAR(20), 
    age INT, 
    sex CHAR,
    email VARCHAR(20),
    did INT,
    FOREIGN KEY (did) REFERENCES tb_dept(did)
);

-- 向tb_emp添加数据
INSERT INTO tb_emp VALUES (NULL, "张三", 23, "男", "123@gmail.com", 1), 
						(NULL, "李四", 24, "女", "1234@gmail.com", 2),
						(NULL, "王五", 25, "男", "12345@gmail.com", 3),
						(NULL, "赵六", 26, "女", "123456@gmail.com", 1),
						(NULL, "田七", 27, "男", "1234567@gmail.com", 2);
-- 外键约束置为1
SET FOREIGN_KEY_CHECKS = 1;
```

创建Emp和Dept类，属性分别如下：

```java
@Data
public class Emp {
    private Integer eid;
    private String empName;
    private Integer age;
    private String sex;
    private String email;
}
```

```java
@Data
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
        select * from tb_emp;
    </select>
</mapper>
```

```java
public class EmpDaoTest {
    @Test
    public void testGetAllEmp() {
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

当调用查询语句`select * from tb_emp;`查询出来结果时会赋值给`List<Emp>`集合，而集合的泛型是Emp，对于返回值当然是各找各妈了，但是name属性和字段的名称不一样，所以这里肯定是null了。

## 3.1 处理字段和属性映射关系

对于上述这种情况，我们有三种解决方案：通过字段别名解决、全局配置驼峰命名规则、resultMap处理映射。

**通过字段别名解决字段名和属性名的映射关系**

究其原因是因为名字不同导致的，那么修改一下名字就可以了呗！因为这两种命名方式都是正确的，所以不能够随意修改。但是可以创建一个别名，为emp_name字段创建一个别名就可以了，这样返回结果的时候就能够找到了。

```xml
<!--List<Emp> getAllEmp();-->
<select id="getAllEmp" resultType="emp">
    select eid, emp_name empName, age, sex, email from tb_emp;
</select>
```

**全局配置驼峰命名规则解决字段名和属性名的映射关系**

可以在MyBatis的核心配置文件中的`setting`标签中，设置一个全局配置信息`mapUnderscoreToCamelCase`。这样查询表中数据时，会自动将`_`类型的字段名转换为驼峰。例如字段名`user_name`转换为`userName`。

```xml
<!--设置Mybatis全局配置-->
<settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

**resultMap处理字段和属性的映射关系**

可以通过resultMap设置自定义映射。但是这种方法也有坏处，那就是即使字段名和属性名一致的属性也要映射，也就是全部属性都要列出来。

```xml
<!-- id属性表示自定义映射的唯一标识、type属性是查询的数据要映射的实体类的类型 -->
<resultMap id="empResultMap" type="Emp">
    <!-- id标签是设置主键的映射关系 property属性设置实体类中的属性名 column属性设置对应字段名-->
    <id property="eid" column="eid"></id>
    <!-- result标签设置普通字段的映射关系 property属性设置实体类中的属性名 column属性设置对应字段名-->
    <result property="empName" column="emp_name"></result>
    <result property="age" column="age"></result>
    <result property="sex" column="sex"></result>
    <result property="email" column="email"></result>
</resultMap>
<!--List<Emp> getAllEmp();-->
<select id="getAllEmp" resultMap="empResultMap">
    select * from tb_emp
</select>
```

## 3.2 处理多对一映射关系

上面创建的两个表中tb_emp是员工表，tb_dept是部门表，通常都是多个员工对应着一个部门。如果这时候想要查询员工信息以及员工所对应的部门信息应该怎么查呢？

不使用mybatis，只使用sql语法，那么使用下列查询方式都可以：

```sql
-- 隐式内连接
select * from tb_emp, tb_dept where tb_emp.did = tb_dept.did and tb_emp.eid = 1;
-- 显示内连接 select 字段列表 from 表名1 [inner] join 表名2 on 条件
select * from tb_emp inner join tb_dept on tb_emp.did = tb_dept.did and tb_emp.eid = 1;
-- 左外连接：查询左表及交集  SELECT 字段列表 FROM 表1 LEFT [ OUTER ] JOIN 表2 ON 条件 ... ;
select * from tb_emp left join tb_dept on tb_emp.eid = tb_dept.did where tb_emp.eid = 1;
```

如果使用Mybatis来查询，这样就会产生一个问题，我们产生的查询结果对应着Java中的类是什么？因为我们查询出来，最后会有着员工信息以及员工所对应的部门信息。在我们创建的Java类中没有该类，所以我们需要在Emp类中添加一条属性来让他们能够产生映射的关系。

对于多对一，我们需要在多的一方添加一个属性：一的一方的类对象。

```java
@Data
public class Emp {  
    private Integer eid;  
    private String empName;  
    private Integer age;  
    private String sex;  
    private String email;  
    // 添加属性：Dept对象
    private Dept dept;
}
```
那么接下来就是让查询结果分别对应Emp中的属性即可，可是对于dept属性的映射关系仍然需要设置一下。查询结果产生的有`did`,`dept_name`这正好对应着Dept类的属性，所以让他们产生映射关系就可以了。

对此我们有三种方法：级联方式处理映射关系、使用association处理映射关系、分步查询。下面来详细介绍一下。

**级联方式处理映射关系**

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

    <!-- id属性表示自定义映射的唯一标识、type属性是查询的数据要映射的实体类的类型 -->
    <resultMap id="empAndDeptResultMapOne" type="Emp">
        <!-- id标签是设置主键的映射关系 property属性设置实体类中的属性名 column属性设置对应字段名-->
        <id property="eid" column="eid"></id>
        <!-- result标签设置普通字段的映射关系 property属性设置实体类中的属性名 column属性设置对应字段名-->
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="sex" column="sex"></result>
        <result property="email" column="email"></result>
        <result property="dept.did" column="did"></result>
        <result property="dept.deptName" column="dept_name"></result>
    </resultMap>

    <!--Emp getEmpAndDept(@Param("eid") Integer eid);-->
    <select id="getEmpAndDept" resultMap="empAndDeptResultMapOne">
        select * from tb_emp left join tb_dept on tb_emp.eid = tb_dept.did where tb_emp.eid = #{eid};
    </select>
</mapper>
```

```java
public class EmpDaoTest {
    @Test
    public void testGetAllEmpTest() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

        Emp empAndDept = mapper.getEmpAndDept(1);
        System.out.println(empAndDept);
    }
}
// Emp{eid=1, empName='张三', age=23, sex='男', email='123@gmail.com', dept=Dept{did=1, deptName='A'}}
```

**使用association处理映射关系**

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

        <!-- association标签用来处理多对一的映射关系 -->
        <!-- property：映射数据库列的实体对象的属性(Emp类添加的dept属性) javaType：该属性的类型-->
        <association property="dept" javaType="Dept">
            <id property="did" column="did"></id>
            <result property="deptName" column="dept_name"></result>
        </association>
    </resultMap>

    <!--Emp getEmpAndDept(@Param("eid") Integer eid);-->
    <select id="getEmpAndDept" resultMap="empAndDeptResultMapOne">
        select * from tb_emp left join tb_dept on tb_emp.eid = tb_dept.did where tb_emp.eid = #{eid};
    </select>
</mapper>
```

**分步查询**

我们可以使用分步查询来查询信息，可以第一步查询员工信息，然后根据查询的员工信息的did来查询tb_dept表，然后将信息返回即可。

第一步：查询员工信息

```java
public interface EmpMapper {
    /**
     * 通过分部查询来查询emp字段和dept字段
     * 这是步骤一：先来查询emp字段
     */
    Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);
}
```

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
			column：设置分步查询的条件，映射数据库列名或者别名
		-->
        <association property="dept"
                     select="com.linxuan.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
                     column="did">
        </association>
    </resultMap>

    <!--Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);-->
    <select id="getEmpAndDeptByStepOne" resultMap="getEmpAndDeptByStepResultMapOne">
        select * from tb_emp where eid = #{eid};
    </select>
</mapper>
```

第二步：查询部门信息

```java
public interface DeptMapper {
    /**
     * 通过分部查询来查询emp字段和dept字段
     * 这是步骤二：查询dept字段
     */
    Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.linxuan.mybatis.mapper.DeptMapper">

    <!--Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);-->
	<!-- tb_dept表与Dept类的字段和属性不匹配，这里采用全局配置开启驼峰命名的方式。	-->
    <select id="getEmpAndDeptByStepTwo" resultType="Dept">
        select * from tb_dept where did = #{did};
    </select>
</mapper>
```

```xml
<!--设置Mybatis全局配置 开启驼峰命名-->
<settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

## 3.3 处理一对多映射关系

<!-- 对一对应对象，对多对应集合 -->

查询Emp和Dept表的时候是多个Emp对应一个Dept表，也就是多对一。那么接下来查询Dept和Emp表，这是一对多，那么根据对多对应集合，直接在Dept类中添加一条属性：

```java
@Data
public class Dept {
    private Integer did;
    private String deptName;
    // 添加集合属性
    private List<Emp> emps;
}
```

这时又需要处理字段和属性的映射关系了：两表联查并且使用`collection`来处理映射关系、分步查询。

**通过两表联查查询使用`collection`来处理映射关系**

```java
public interface DeptMapper {

    /**
     * 通过两表联查来查询Dept和Emp表的字段，一对多的情况。
     * 这里是以部门表为主表的，所以部门表的英文在前。
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
        
        <!--collection标签用来处理一对多的映射关系 ofType表示该属性对应的集合中存储的数据的类型-->
        <!--property代表映射数据库列的实体对象的属性(Dept类添加的emps属性)-->
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
        select * from tb_dept left join tb_emp on tb_dept.did = tb_emp.did where tb_dept.did = #{did}
    </select>
</mapper>
```

**分步查询**

我们可以使用分步查询来查询信息，可以第一步查询部门信息，然后根据查询的部门信息的did来查询tb_emp表，然后将信息返回即可。

第一步：查询部门信息

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
        select * from tb_dept where did = #{did};
    </select>
</mapper>
```

第二步：根据部门id查询部门中的所有员工

```java
public interface EmpMapper {
    /**
     * 通过分步查询来查询Dept和Emp表
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
        select * from tb_emp where did = #{did};
    </select>
    
</mapper>
```

## 3.4 延迟加载

在上面处理多对一的映射关系和一对多的映射关系的时候尽量使用分步查询，因为分步查询可以实现延迟加载。想要实现延迟加载，那么我们需要在核心配置文件中设置全局配置信息：

- `lazyLoadingEnabled`：延迟加载的全局开关。当开启时，所有关联对象都会延迟加载  
- `aggressiveLazyLoading`：当开启时，任何方法的调用都会加载该对象的所有属性。 否则，每个属性会按需加载。

此时就可以实现按需加载，获取的数据是什么，就只会执行相应的sql。

对于处理多对一映射关系中的分布查询代码，测试代码：

```java
@Test
public void testGetEmpAndDeptByStepOne() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    EmpDao empDao = sqlSession.getMapper(EmpDao.class);
    Emp emp = empDao.getEmpAndDeptByStepOne(1);
    System.out.println(emp.getEmpName());
    System.out.println("------------");
    System.out.println(emp.getDept());
}

// 控制台输出：
DEBUG 02-03 12:53:22,124 ==>  Preparing: select * from tb_emp where eid = ?; 
DEBUG 02-03 12:53:22,159 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
DEBUG 02-03 12:53:22,185 ====>  Preparing: select * from tb_dept where did = ?; 
DEBUG 02-03 12:53:22,186 ====> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
DEBUG 02-03 12:53:22,189 <====      Total: 1 (BaseJdbcLogger.java:137) 
DEBUG 02-03 12:53:22,191 <==      Total: 1 (BaseJdbcLogger.java:137) 
张三
------------
Dept(did=1, deptName=A, emps=null)
```

如果想要延迟加载/按需加载。获取的数据是什么，就只会执行相应的sql。那么需要在配置文件中设置一下全局配置信息：

```xml
<!--在mybatis-config.xml文件中添加下列代码-->
<settings>
	<!--开启延迟加载-->
	<setting name="lazyLoadingEnabled" value="true"/>
</settings>
```

这时的控制台打印的信息就改变了：

```java
DEBUG 02-03 12:56:11,129 ==>  Preparing: select * from tb_emp where eid = ?; 
DEBUG 02-03 12:56:11,166 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
DEBUG 02-03 12:56:11,252 <==      Total: 1 (BaseJdbcLogger.java:137) 
张三
------------
DEBUG 02-03 12:56:11,255 ==>  Preparing: select * from tb_dept where did = ?; 
DEBUG 02-03 12:56:11,256 ==> Parameters: 1(Integer) (BaseJdbcLogger.java:137) 
DEBUG 02-03 12:56:11,258 <==      Total: 1 (BaseJdbcLogger.java:137) 
Dept(did=1, deptName=A, emps=null)
```

如果想要有的地方不使用延迟加载，那么我们就可以在其对应的xml文件中的`association`和`collection`标签中的`fetchType`属性来设置当前的分步查询不使用延迟加载。

```apl
fetchType="lazy(延迟加载)|eager(立即加载)"
```

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
        select * from tb_emp where eid = #{eid};
    </select>
</mapper>
```

这样控制台的信息就又会变回原来的样子。

# 第四章 动态SQL和Mybatis缓存

## 4.1 常见的动态SQL标签

Mybatis框架的动态SQL技术是一种根据特定条件动态拼装SQL语句的功能，它存在的意义是为了解决拼接SQL语句字符串时的痛点问题。

介绍下面几种动态SQL标签：`if`、`where`、`trim`、`choose`、`when`、`otherwise`、`foreach`。

**if动态SQL标签**

`if`表示判断。查询数据库时有些情况会需要用户输入条件，有的用户只会输入部分条件，这样拼接sql的时候会导致SQL语句查询失败。例如根据用户输入的姓名、年龄、性别、邮箱查询信息，但是有些用户只输入了姓名，那么这时候这条SQL语句就会查询失败。

```sql
# 使用该SQL语句查询的时候，如果前端返回的参数只有empName，那么就会导致SQL语句查询失败
select * from tb_emp where 1 = 1 and emp_name = $'empName' and age = $'age' and sex = $'sex' and email = $'email'
```

if标签可通过test属性（即传递过来的数据）的表达式进行判断，若表达式的结果为true，则标签中的内容会执行；反之标签中的内容不会执行。

```java
public interface EmpMapper {
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
<mapper namespace="com.linxuan.mybatis.mapper.EmpMapper">
    
    <!--List<Emp> getEmpByCondition(Emp emp);-->
    <select id="getEmpByCondition" resultType="Emp">
        <!-- where后添加一个恒成立的条件1 = 1，防止后面where和and连用，导致SQL语句报错 -->
        select * from tb_emp where 1 = 1
        
        <!--这些标签内部不要使用符号，如果用&&的话会被转义，无法识别-->
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
@Test
public void tesGetEmpByCondition() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    EmpDao empDao = sqlSession.getMapper(EmpDao.class);
    // 查询性别为男的员工
    List<Emp> emps = empDao.getEmpByCondition(new Emp(null, null, null, "男", null));
    emps.forEach(emp -> System.out.println(emp));
}
// 输出为：
DEBUG 02-03 13:30:55,413 ==>  Preparing: select * from tb_emp where 1 = 1 and sex = ? 
DEBUG 02-03 13:30:55,448 ==> Parameters: 男(String) (BaseJdbcLogger.java:137) 
DEBUG 02-03 13:30:55,482 <==      Total: 3 (BaseJdbcLogger.java:137) 
Emp(eid=1, empName=张三, age=23, sex=男, email=123@gmail.com, dept=null)
Emp(eid=3, empName=王五, age=25, sex=男, email=12345@gmail.com, dept=null)
Emp(eid=5, empName=田七, age=27, sex=男, email=1234567@gmail.com, dept=null)
```

**where动态SQL标签**

where和if一般结合使用：

- 若where标签中的if条件都不满足，则where标签没有任何功能，即不会添加where关键字  
- 若where标签中的if条件满足，则where标签会自动添加where关键字，并将条件最前方多余的`and/or`去掉  

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
    select * from tb_emp
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
```

> 注意：where标签不能去掉条件后多余的and/or，and和or关键字只能放在前面，不能够放在后面。

**trim动态SQL标签**

trim用于去掉或添加标签中的内容。常用属性如下：

| 属性            | 作用                                   |
| --------------- | -------------------------------------- |
| prefix          | 在trim标签中的内容的前面添加某些内容。 |
| suffix          | 在trim标签中的内容的后面添加某些内容。 |
| prefixOverrides | 在trim标签中的内容的前面去掉某些内容。 |
| suffixOverrides | 在trim标签中的内容的后面去掉某些内容。 |

若trim中的标签都不满足条件，则trim标签没有任何效果，也就是只剩下`select * from tb_emp`

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
    select * from tb_emp
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
@Test
public void tesGetEmpByCondition() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    EmpDao empDao = sqlSession.getMapper(EmpDao.class);
    List<Emp> emps = empDao.getEmpByCondition(new Emp(null, "张三", null, "", ""));
    emps.forEach(emp -> System.out.println(emp));
}
// 控制台输出信息：
DEBUG 02-03 15:09:30,832 ==>  Preparing: select * from tb_emp where emp_name = ? 
DEBUG 02-03 15:09:30,866 ==> Parameters: 张三(String) (BaseJdbcLogger.java:137) 
DEBUG 02-03 15:09:30,897 <==      Total: 1 (BaseJdbcLogger.java:137) 
Emp(eid=1, empName=张三, age=23, sex=男, email=123@gmail.com, dept=null)
```

**choose、when、otherwise动态SQL标签**

- `choose、when、otherwise`相当于`if...else if..else`
- when至少要有一个，otherwise至多只有一个

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
    select * from tb_emp
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
```

**foreach动态SQL标签**

foreach循环，这用于批量操作。例如批量添加或者批量删除。常用属性如下：  

| 属性       | 作用                                                      |
| ---------- | --------------------------------------------------------- |
| collection | 设置要循环的数组或集合                                    |
| item       | 表示集合或数组中的每一个数据                              |
| separator  | 设置循环体之间的分隔符，分隔符前后默认有一个空格，如` , ` |
| open       | 设置foreach标签中的内容的开始符                           |
| close      | 设置foreach标签中的内容的结束符                           |

批量添加

```java
public interface EmpMapper {
    /**
     * 添加多条数据
     */
    Integer insertMoreByArray(@Param("emps") List<Emp> emps);
}
```

```xml
<!--Integer insertMoreByArray(@Param("emps") List<Emp> emps);-->
<!-- 添加数据的时候，最后一个数据是did。我们不知道，这里弄成null-->
<insert id="insertMoreByArray">
    insert into tb_emp values
    <foreach collection="emps" item="emp" separator=",">
        (null, #{emp.empName}, #{emp.age}, #{emp.sex}, #{emp.email}, null)
    </foreach>
</insert>
```

批量删除

```java
public interface EmpMapper {
    /**
     * 批量删除数据
     */
    Integer deleteMoreByArray(@Param("eids") Integer[] eids);
}
```

```xml
<!--Integer deleteMoreByArray(@Param("eids") Integer[] eids);-->
<!--第一种删除方式：delete from tb_emp where eid in (1,2,3)-->
<delete id="deleteMoreByArray">
    delete from tb_emp where eid in
    <foreach collection="eids" item="eid" close=")" open="(" separator=",">
        #{eid}
    </foreach>
</delete>
<!--第二种删除方式：delete from tb_emp where eid = 1 or eid = 2 or eid = 3-->
<delete id="deleteMoreByArray">
    delete from tb_emp where
    <foreach collection="eids" item="eid" close=")" open="(" separator="or">
        eid = #{eid}
    </foreach>
</delete>
```

## 4.2 SQL标签与SQL片段

查询数据的时候，有时候不是想要查询所有的字段，仅仅想要查询部分字段。但是部分字段也有点多，并且我们会重复利用这些字段。这时候我们就可以将这些字段弄成公共的SQL片段。

声明sql片段：`<sql>`标签

```xml
<sql id="empColumns">eid, emp_name, age, sex, email</sql>
```

引用sql片段：`<include>`标签

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select <include refid="empColumns"></include> from tb_emp
</select>
```

## 4.3 MyBatis缓存

缓存查询顺序如下：

- 先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用  

- 如果二级缓存没有命中，再查询一级缓存  

- 如果一级缓存也没有命中，则查询数据库  

- SqlSession关闭之后，一级缓存中的数据会写入二级缓存

**MyBatis一级缓存**

一级缓存是`SqlSession`级别的，通过同一个`SqlSession`查询的数据会被缓存，下次查询相同的数据，就会从缓存中直接获取，不会从数据库重新访问。

使一级缓存失效的四种情况：  不同的SqlSession对应不同的一级缓存、同一个SqlSession但是查询条件不同、同一个SqlSession两次查询期间执行了任何一次增删改操作、同一个SqlSession两次查询期间手动清空了缓存，例如使用`SqlSession.clearCache方法`。

**MyBatis二级缓存**

二级缓存是`SqlSessionFactory`级别，通过同一个`SqlSessionFactory`创建的`SqlSession`查询的结果会被缓存；此后若再次执行相同的查询语句，结果就会从缓存中获取。

二级缓存开启的条件：

1. 设置全局配置属性`cacheEnabled="true"`，默认为`true`，不需要设置。
2. 在映射文件中设置标签`<cache />`
3. 二级缓存必须在`SqlSession`关闭或提交之后有效
4. 查询的数据所转换的实体类类型必须实现序列化的接口

使二级缓存失效的情况：两次查询之间执行了任意的增删改，会使一级和二级缓存同时失效

## 4.4 整合第三方缓存EHCache

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

创建EHCache的配置文件`ehcache.xml`，名字必须叫`ehcache.xml`

```xml
<?xml version="1.0" encoding="utf-8" ?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
    <!-- 磁盘保存路径 -->
    <diskStore path="D:\mybatis\ehcache"/>
    <!--
	defaultCache属性介绍：
		- maxElementsInMemory：必须添加该属性，在内存中缓存的element的最大数目
		- maxElementsOnDisk：必须，在磁盘上缓存的element的最大数目，若是0表示无穷大
		- eternal：必须，设定缓存的elements是否永远不过期。如果为true，则缓存的数据始终有效。
		- overflowToDisk：必须，设定当内存缓存溢出的时候是否将过期的element缓存到磁盘上
		- timeToIdleSeconds：非必须，缓存数据前后访问时间超过该值时数据会删除，默认值是0无穷大
		- timeToLiveSeconds：非必须，缓存element的有效生命期，默认是0无穷大
		- diskExpiryThreadIntervalSeconds：非必须，磁盘缓存的清理线程运行间隔，默认是120秒
		- memoryStoreEvictionPolicy：缓存达到最大，有新的element加入的时候，移除缓存中element的策略。默认是LRU（最近最少使用），可选的有LFU（最不常使用）和FIFO（先进先出）
	-->
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

设置二级缓存的类型。在`xxxMapper.xml`文件中设置二级缓存类型

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

加入logback日志。存在SLF4J时，作为简易日志的log4j将失效，此时我们需要借助SLF4J的具体实现logback来打印日志。创建logback的配置文件`logback.xml`，名字固定，不可改变。

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
    <logger name="com.linxuan.mapper" level="DEBUG"/>
</configuration>
```

# 第五章 逆向工程和分页插件

正向工程：先创建Java实体类，由框架负责根据实体类生成数据库表。Hibernate是支持正向工程的

逆向工程：先创建数据库表，由框架负责根据数据库表，反向生成如下资源：  Java实体类、Mapper接口、Mapper映射文件。MyBatis可以添加插件来支持逆向工程。

## 5.1 创建逆向工程

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

创建逆向工程的配置文件。文件名必须是：`generatorConfig.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--
        targetRuntime: 执行生成的逆向工程的版本
         - MyBatis3Simple: 生成基本的CRUD（清新简洁版）
         - MyBatis3: 生成带条件的CRUD（奢华尊享版）
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
                             targetPackage="com.linxuan.mybatis.mapper" 
                             targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="tb_emp" domainObjectName="Emp"/>
        <table tableName="tb_dept" domainObjectName="Dept"/>
    </context>
</generatorConfiguration>
```

执行MBG插件的generate目标

## 5.2 QBC查询和增改

QBC，即 Query By Criteria，使用 QBC 查询不需要写语句，直接使用方法实现。QBC操作的是实体类和属性，使用 Criteria对象实现 QBC 查询。QBC查询是将查询条件通过Java对象进行模块化封装。

上面执行完插件之后，就会生成相应的Java代码。同时也会在pojo包下面生成`实体类名+Example`类，这个类里面封装了很多方法供使用。

- `selectByExample`：按条件查询，需要传入一个example对象或者null；如果传入一个null，则表示没有条件，也就是查询所有数据
- `example.createCriteria().xxx`：创建条件对象，通过andXXX方法为SQL添加查询添加，每个条件之间是and关系
- `example.or().xxx`：将之前添加的条件通过or拼接其他条件

```java
@Test 
public void testMBG() throws IOException {
    
	InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
	SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
	SqlSession sqlSession = sqlSessionFactory.openSession(true);
    
	EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
	EmpExample example = new EmpExample();
	// 名字为张三，且年龄大于等于20
	example.createCriteria().andEmpNameEqualTo("张三").andAgeGreaterThanOrEqualTo(20);
	// 或者did不为空
	example.or().andDidIsNotNull();
	List<Emp> emps = mapper.selectByExample(example);
	emps.forEach(System.out::println);
}
```

`updateByPrimaryKey`：通过主键进行数据修改，如果某一个值为null，也会将对应的字段改为null

```java
mapper.updateByPrimaryKey(new Emp(1,"admin",22,null,"456@qq.com",3));
```

`updateByPrimaryKeySelective()`：通过主键进行选择性数据修改，如果某个值为null，则不修改这个字段

```java
mapper.updateByPrimaryKeySelective(new Emp(2,"admin2",22,null,"456@qq.com",3));
```

## 5.3 分页插件使用

添加依赖

```xml
<!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
<dependency>
	<groupId>com.github.pagehelper</groupId>
	<artifactId>pagehelper</artifactId>
	<version>5.2.0</version>
</dependency>
```

配置分页插件。在MyBatis的核心配置文件（mybatis-config.xml）中配置插件

```xml
<plugins>
	<!--设置分页插件-->
	<plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
</plugins>
```

开启分页功能。在查询功能之前使用`PageHelper.startPage(int pageNum, int pageSize)`开启分页功能：

```java
@Test
public void testPageHelper() throws IOException {
	InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
	SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
	SqlSession sqlSession = sqlSessionFactory.openSession(true);
	EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
	// 访问第一页，每页四条数据
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
    // 访问第一页，每页四条数据
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

| 数据                        | 作用                         |
| --------------------------- | ---------------------------- |
| pageNum                     | 当前页的页码                 |
| pageSize                    | 每页显示的条数               |
| size                        | 当前页显示的真实条数         |
| total                       | 总记录数                     |
| pages                       | 总页数                       |
| prePage                     | 上一页的页码                 |
| nextPage                    | 下一页的页码                 |
| isFirstPage/isLastPage      | 是否为第一页/最后一页        |
| hasPreviousPage/hasNextPage | 是否存在上一页/下一页        |
| navigatePages               | 导航分页的页码数             |
| navigatepageNums            | 导航分页的页码，\[1,2,3,4,5] |
