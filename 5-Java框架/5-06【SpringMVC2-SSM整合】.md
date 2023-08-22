![](..\图片\5-06【SpringMVC】\0-1.png)

# 第一章 SSM整合

前面我们已经把`Mybatis`、`Spring`和`SpringMVC`三个框架进行了学习，下面学习一下将他们整合开发。

首先来导入依赖、创建数据库及表、导入MySQL连接数据库四要素配置文件

```xml
<!-- 导入依赖 -->
<dependencies>
    <!-- SpringMVC依赖环境 -->
    <!-- servlet依赖 -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <!-- 依赖作用范围未provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>
    <!-- 该jar包依赖于spring-context，所以不用导入Spring的环境 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- SpringMVC默认使用的是jackson来处理json的转换，所以需要在pom.xml添加jackson依赖 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.9.0</version>
    </dependency>

    <!-- Spring整合MyBatis环境 -->
    <!-- MySQL驱动依赖的jar包，没有这个jar包根本无法连接数据库 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>
    <!-- Druid连接池技术，简化开发 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- MyBatis基础依赖 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.7</version>
    </dependency>
    <!-- Spring整合JDBC -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- Spring整合MyBatis -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>1.3.0</version>
    </dependency>

    <!-- Spring整合Junit环境 -->
    <!-- Junit单元测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
    <!-- Spring整合Junit依赖的jar包 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>

    <!-- 其他依赖环境 -->
    <!-- Lombok简化开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
        <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;
DROP TABLE IF EXISTS tb_book;
# 如果不存在tb_book表那么就创建
CREATE TABLE IF NOT EXISTS tb_book(
    id int primary key auto_increment,
    type varchar(20),
    name varchar(50),
    description varchar(255)
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_book;
# 插入数据
INSERT  INTO `tb_book`(`id`, `type`, `name`, `description`) VALUES 
	(1, '计算机理论', 'Spring实战 第五版', 'Spring入门经典教程'), 
	(2, '计算机理论', 'Spring 5核心原理与30个类手写实践', '手写Spring精华思想'), 
	(3, '计算机理论', 'Spring 5设计模式', '刨析Spring源码中蕴含的10大设计模式'), 
	(4, '计算机理论', 'Spring MVC+Mybatis开发', '全方位解析面向Web应用的轻量级框架'), 
	(5, '计算机理论', '轻量级Java Web企业应用实战', '源码级刨析Spring框架'), 
	(6, '计算机理论', 'Java核心技术', 'Core Java第11版, Jolt大奖获奖作品'), 
	(7, '计算机理论', '深入理解Java虚拟机', '5个纬度全面刨析JVM'), 
	(8, '计算机理论', 'Java编程思想(第4版)', 'Java学习必读经典'), 
	(9, '计算机理论', '零基础学Java(全彩版)', '零基础自学编程的入门图书'), 
	(10, '市场营销', '直播就这么做:主播高效沟通实战指南', '成长为网红的秘密'), 
	(11, '市场营销', '直播销讲实战一本通', '和秋叶一起学系列网络营销书籍'), 
	(12, '市场营销', '直播带货:淘宝、天猫直播从新手到高手', '一本教你如何玩转直播的书');
```

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan?useSSL=false
jdbc.username=root
jdbc.password=root
```

接下来就要整合配置了，Spring的配置有三种：配置文件开发、注解开发、混合开发。

## 1.1 全注解开发整合配置

```java
package com.linxuan.config;

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

    // 开启事务第二步，配置事务管理器
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        DataSourceTransactionManager ds = new DataSourceTransactionManager();
        ds.setDataSource(dataSource);
        return ds;
    }
}
```

```java
package com.linxuan.config;

// 替换掉了MyBatis的配置文件mybatis-config.xml了
public class MyBatisConfig {
    // 定义bean，SqlSessionFactoryBean，用于产生SqlSessionFactory对象
    // 这里的dataSource会自动装配进去，我们不用管
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置模型类的别名扫描，这个等于<typeAliases><package name="com.linxuan.domain"></typeAliases>
        sqlSessionFactoryBean.setTypeAliasesPackage("com.linxuan.domain");
        // 设置数据源 这个要在前面配置 否则会报错
        sqlSessionFactoryBean.setDataSource(dataSource);


        Configuration configuration = sqlSessionFactoryBean.getObject().getConfiguration();
        // 设置配置中的日志 采用标准输出 可以采用 Log4jImpl.class
        configuration.setLogImpl(StdOutImpl.class);
        // 开启驼峰命名
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean;
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

```java
package com.linxuan.config;

// 标识为配置类
@Configuration
// 只需要扫描com.linxuan.service，dao里面都是接口。如果接下来有需要扫描的包，我们只需要将其扫描即可。
// dao层都被MapperScannerConfigurer对象来进行扫描处理的
@ComponentScan("com.linxuan.service")
// 导入其他配置类
@Import({JdbcConfig.class, MyBatisConfig.class})
// 开启注解式事务驱动，替换掉了Spring配置文件的<tx:annotation-driven transaction-manager="txManager"/>
@EnableTransactionManagement
public class SpringConfig {
}
```

```java
package com.linxuan.config;

// 标识为配置类
@Configuration
// 包扫描，SpringMVC仅需要扫描controller包即可
@ComponentScan("com.linxuan.controller")
// 开启SpringMVC的注解支持，有多项辅助功能。包含将JSON转换成对象的功能。标配，不要忘记。
@EnableWebMvc
public class SpringMvcConfig {
}
```

```java
// Web项目入口配置类 替换web.xml。这种在配置类基础上更简化，之前继承AbstractDispatcherServletInitializer
public class ServletConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    // 加载Spring配置类
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    // 加载SpringMVC配置类
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    // 设置SpringMVC请求地址拦截规则
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

## 1.2 配置文件开发整合

web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!-- web.xml中标签加载顺序为：context-param > listener > filter > servlet -->

    <!-- spring配置 Spring加载的xml文件,不配置默认为applicationContext.xml-->
    <!--指定web项目从项目的src路径下加载application-context.xml，这是springMVC所必不可少的配置项-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:applicationContext.xml</param-value>
    </context-param>

    <!--SpringMVC配置-->
    <!-- 该类作为spring的listener使用，它会在创建时自动查找web.xml配置的applicationContext.xml文件 -->
    <!-- 启动服务器时，通过监听器加载spring运行环境 通过ContextLoaderListener自动装配ApplicationContext的配置信息-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- 配置请求过滤器，编码格式设为UTF-8，避免中文乱码 -->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <!-- init-param将初始化需要的参数传入到filter-class中从而进行初始化 -->
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <!-- filter-mapping是将filter和url路径进行映射 filter和filter-mapping中的name必须是相同的，才能起到映射的作用 -->
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <!-- <url-pattern>匹配请求路径，这里是匹配所有路径 -->
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!--spring mvc配置-->
    <!-- 配置Sping MVC的DispatcherServlet,也可以配置为继承了DispatcherServlet的自定义类,这里配置spring mvc的配置(扫描controller) -->
    <servlet>
        <!-- 配置DispatcherServlet -->
        <servlet-name>DispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 指定spring mvc配置文件位置 不指定使用默认情况 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath*:springmvc.xml</param-value>
        </init-param>
        <!-- 设置启动顺序 使系统在启动时装在servlet而不是第一个servlet被访问-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <!-- ServLet 匹配映射 -->
    <servlet-mapping>
        <servlet-name>DispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="
                           http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context-4.0.xsd
                           http://www.springframework.org/schema/tx
                           http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
                           http://www.springframework.org/schema/aop
                           http://www.springframework.org/schema/aop/spring-aop-4.0.xsd">

    <!-- 配置IOC注解解析器，等于@ComponentScan("com.linxuan.service") -->
    <context:component-scan base-package="com.linxuan.service"/>

    <!-- 加载dbconfig.properties，等于@PropertySource -->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!--数据源-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!-- mybatis和spring完美整合，不需要mybatis的配置映射文件 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="com.linxuan.entity"/>
        <!-- 配置配置类 -->
        <property name="configuration" ref="settings"/>
    </bean>

    <!-- mybatis配置驼峰形式的设置类 -->
    <bean id="settings" class="org.apache.ibatis.session.Configuration">
        <property name="mapUnderscoreToCamelCase" value="true"/>
    </bean>

    <!-- DAO接口所在包名，Spring会自动查找其下的类 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.linxuan.dao"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>

    <!-- 对dataSource 数据源进行事务管理 事务管理器 等于JdbcConfig里面配置的bean对象-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--开启注解式事务 等于EnableTransactionManagement-->
    <tx:annotation-driven transaction-manager="txManager"/>
</beans>
```

springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- xmlns:mvc开启SpringMVC命名空间 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/mvc
                           http://www.springframework.org/schema/mvc/spring-mvc.xsd
                           http://www.springframework.org/schema/context
                           https://www.springframework.org/schema/context/spring-context.xsd">

    <!--
        启用spring mvc 注解, 默认的注解映射的支持。
        自动注册 RequestMappingHandlerMapping RequestMappingHandlerAdapter 两个 bean
        这是 Spring mvc 分发请求所必须的
        并提供了数据绑定支持、读写XML支持、读写Json支持
        等同于@EnableWebMvc
    -->
    <mvc:annotation-driven/>

    <!--包扫描 等同于@ComponentScan-->
    <context:component-scan base-package="com.linxuan.controller"/>

    <!-- 静态资源配置方案 -->
    <!-- 
        添加标签后，会定义一个DefaultServletHttpRequestHandler对象，
        该对象对进入DispatcherServlet的URL惊醒过滤筛查，如果发现是静态资源请求
        那么转交给Tomcat的默认的DefaultServlet处理，如果不是静态资源请求，那么继续有SpringMVC处理
    -->
    <mvc:default-servlet-handler/>
</beans>
```

## 1.3 混合开发

这里没有做，以后遇到再说

## 1.4 功能模块开发

```java
package com.linxuan.domain;

@Data
public class Book {
    private Integer id;
    private String type;
    private String name;
    private String description;
}
```

```java
package com.linxuan.dao;

public interface BookDao {
    @Insert("insert into tb_book (type, name, description) values(#{type}, #{name}, #{description})")
    public void save(Book book);

    @Update("update tb_book set type = #{type}, name = #{name}, description = #{description} where id = #{id}")
    public void update(Book book);

    @Delete("delete from tb_book where id = #{id}")
    public void delete(Integer id);

    @Select("select * from tb_book where id = #{id}")
    public Book getById(Integer id);

    @Select("select * from tb_book")
    public List<Book> getAll();
}
```

```java
// 添加事务，为当前接口所有实现类的所有方法上添加事务
@Transactional
public interface BookService {
    /**
     * 保存
     * @param book
     * @return
     */
    public boolean save(Book book);

    /**
     * 修改
     * @param book
     * @return
     */
    public boolean update(Book book);

    /**
     * 按id删除
     * @param id
     * @return
     */
    public boolean delete(Integer id);

    /**
     * 按id查询
     * @param id
     * @return
     */
    public Book getById(Integer id);

    /**
     * 查询全部
     * @return
     */
    public List<Book> getAll();
}
```

```java
package com.linxuan.service.impl;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    public boolean save(Book book) {
        bookDao.save(book);
        return true;
    }

    public boolean update(Book book) {
        bookDao.update(book);
        return true;
    }

    public boolean delete(Integer id) {
        bookDao.delete(id);
        return true;
    }

    public Book getById(Integer id) {
        return bookDao.getById(id);
    }

    public List<Book> getAll() {
        return bookDao.getAll();
    }
}
```

![](..\图片\5-06【SpringMVC】\1-6.png)

BookDao是一个接口，没有实现类，接口是不能创建对象的，所以最终注入的应该是代理对象。代理对象是由Spring的IOC容器来创建管理的，IOC容器又是在Web服务器启动的时候才会创建。IDEA在检测依赖关系的时候，没有找到适合的类注入，所以会提示错误提示。但是程序运行的时候，代理对象就会被创建，框架会使用DI进行注入，所以程序运行无影响。

解决方案：1.因为运行时正常的，所以不管他。2.设置错误级别

![](..\图片\5-06【SpringMVC】\1-7.png)

```java
// 等于@Controller + ResponseBody。设置当前控制器类为RESTful风格。该类交由Spring管理，所有方法返回值作为响应体，无需解析。
@RestController
@RequestMapping("/books")
public class BookController {

    // 自动装配
    @Autowired
    private BookService bookService;

    @PostMapping
    // @RequestBody 将请求中请求体所包含的数据传递给请求参数，就是将前端的JSON数据映射到形参的对象中作为数据
    public boolean save(@RequestBody Book book) {
        return bookService.save(book);
    }

    @PutMapping
    public boolean update(@RequestBody Book book) {
        return bookService.update(book);
    }

    @DeleteMapping("/{id}")
    // @PathVariable RESTful风格传递路径参数，绑定路径参数与后端方法形参间的关系，要求路径参数名与形参名一一对应
    public boolean delete(@PathVariable Integer id) {
        return bookService.delete(id);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Integer id) {
        return bookService.getById(id);
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }
}
```

## 1.5 Junit/PostMan测试

**Junit单元测试**

```java
package com.linxuan.service;

//设置类运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 如果是注解开发，那么就使用这种方式加载配置类，设置Spring环境对应的配置类
@ContextConfiguration(classes = SpringConfig.class)
// 如果是配置文件开发（或者混合开发），那么就是用这种方式加载配置文件
// @ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Test
    public void testGetById() {
        Book book = bookService.getById(1);
        System.out.println(book);
    }

    @Test
    public void testGetAll() {
        List<Book> list = bookService.getAll();
        for (Book book : list) {
            System.out.println(book);
        }
    }
}
```

**PostMan测试**

* 新增。选择POST方式发送：http://localhost/books，携带JSON数据

  ```json
  {
  	"type":"类别测试数据",
      "name":"书名测试数据",
      "description":"描述测试数据"
  }
  ```

* 修改。选择PUT方式发送：http://localhost/books，携带JSON数据

  ```json
  {
      "id":13,
  	"type":"类别测试数据",
      "name":"书名测试数据666",
      "description":"描述测试数据"
  }
  ```

* 删除。选择Delete方式发送：http://localhost/books/13。

* 查询单个。选择GET方式发送，URL输入http://localhost/books/1。

* 查询所有。选择GET方式发送，URL输入http://localhost/books。

# 第二章 统一结果封装

## 2.1 数据传输协议定义

前面开发的案例有一个问题，那就是返回给前端的数据格式不统一，例如：

* 在Controller层”增删改“返回给前端的是boolean类型数据

  ```json
  true
  ```

* 在Controller层查询单个返回给前端的是JSON对象（返回的对象由`@EnableWebMvc`注解开启的功能转换了）

  ```json
  {
      "id": 1,
      "type": "计算机理论",
      "name": "Spring实战 第五版",
      "description": "Spring入门经典教程, 深入理解Spring原理技术内幕"
  }
  ```

* 在Controller层查询所有返回给前端的是JSON对象数组

  ```json
  [
      {
          "id": 1,
          "type": "计算机理论",
          "name": "Spring实战 第五版",
          "description": "Spring入门经典教程, 深入理解Spring原理技术内幕"
      },
      {
          "id": 2,
          "type": "计算机理论",
          "name": "Spring 5核心原理与30个类手写实践",
          "description": "十年沉淀之作, 手写Spring精华思想"
  	}
  ]
  ```

这样有三种格式返回给前端，对于前端开发人员在解析数据的时候就比较凌乱了。如果后台能够返回一个统一的数据结果，前端在解析的时候就可以按照一种方式进行解析。开发就会变得更加简单。

所以我们可以将返回结果的数据进行统一，设置一个统一数据返回结果类：

```java
public class Result{
    // 该属性封装返回的结果数据
	private Object data;
    // 该属性封装返回的数据是前端操作动作及是否操作成功的代码
	private Integer code;
    // 该属性封装返回的错误信息
	private String msg;
}
```

这样将这个结果类返回给前端，然后中间会处理成JSON格式。最后返回前端内容示例：

```json
{
    // 自己设置的代码，含义为新增成功
    "code": 20031,
    // 返回的数据为true
    "data": true
}
```

```json
{
    // 查询数据失败
    "code": 20040,
    "data": null,
    "msg": "数据查询失败，请重试"
}
```

> Result类名及类中的字段并不是固定的，可以根据需要自行增减提供若干个构造方法，方便操作。

## 2.2 数据传输协议实现

```java
// 结果封装实在表现层进行处理，所以将结果类放在controller包下面。
package com.linxuan.controller;

@Data
public class Result {
    // 描述统一格式中的数据
    private Object data;
    // 描述统一格式中的编码，用于区分操作。可以简化配置0或1表示成功失败，这里就不简化了
    private Integer code;
    // 描述统一格式中的消息，可选属性
    private String msg;

    public Result() {
    }
	// 构造方法是方便对象的创建
    public Result(Integer code, Object data) {
        this.data = data;
        this.code = code;
    }
    
	//构造方法是方便对象的创建
    public Result(Integer code, Object data, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }
}
```

```java
// 状态码
public class Code {
    // 可以设计为如下的类型，当然这不是固定的，也可以增加。例如查询细分GET_OK，GET_ALL_OK，GET_PAGE_OK
    public static final Integer SAVE_OK = 20011;
    public static final Integer DELETE_OK = 20021;
    public static final Integer UPDATE_OK = 20031;
    public static final Integer GET_OK = 20041;

    public static final Integer SAVE_ERR = 20010;
    public static final Integer DELETE_ERR = 20020;
    public static final Integer UPDATE_ERR = 20030;
    public static final Integer GET_ERR = 20040;
}
```

修改Controller类的返回值

```java
// 统一每一个控制器方法返回值
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public Result save(@RequestBody Book book) {
        boolean flag = bookService.save(book);
        return new Result(flag ? Code.SAVE_OK: Code.SAVE_ERR, flag);
    }

    @PutMapping
    public Result update(@RequestBody Book book) {
        boolean flag = bookService.update(book);
        return new Result(flag ? Code.UPDATE_OK: Code.UPDATE_ERR，flag);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean flag = bookService.delete(id);
        return new Result(flag ? Code.DELETE_OK: Code.DELETE_ERR，flag);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Book book = bookService.getById(id);
        Integer code = book != null ? Code.GET_OK: Code.GET_ERR;
        String msg = book != null ? "" : "数据查询失败，请重试！";
        return new Result(code，book，msg);
    }

    @GetMapping
    public Result getAll() {
        List<Book> bookList = bookService.getAll();
        Integer code = bookList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = bookList != null ? "" : "数据查询失败，请重试！";
        return new Result(code，bookList，msg);
    }
}
```

# 第三章 统一异常处理

在讲解这一部分知识点之前，我们先来演示个效果，修改`BookController`类的`getById`方法

```java
@GetMapping("/{id}")
public Result getById(@PathVariable Integer id) {
    // 手动添加一个数学异常信息
    if(id == 1){
        int i = 1 / 0;
    }
    Book book = bookService.getById(id);
    Integer code = book != null ? Code.GET_OK : Code.GET_ERR;
    String msg = book != null ? "" : "数据查询失败，请重试！";
    return new Result(code，book，msg);
}
```

使用PostMan发送请求，当传入的id为1，会出现异常。前端接受到的只会是一大堆HTML代码，而视图则是显示`HTTP状态 500 - 内部服务器错误`。前端接收到这个信息后和之前我们约定的格式不一致，这个问题该如何解决?

在解决问题之前，我们先来看下异常的种类及出现异常的原因：

- 框架内部抛出的异常：因使用不合规导致
- 数据层抛出的异常：因外部服务器故障导致（例如：服务器访问超时）
- 业务层抛出的异常：因业务逻辑书写错误导致（例如：遍历业务书写操作，导致索引异常等）
- 表现层抛出的异常：因数据收集、校验等规则导致（例如：不匹配的数据类型间导致异常）
- 工具类抛出的异常：因工具类书写不严谨不够健壮导致（例如：必要释放的连接长期未释放等）

各个层级均出现异常，异常处理代码那么我们就抛出到表现层进行处理，然后表现层将所有的异常分类处理。表现层处理异常，每个方法中单独书写，代码书写量巨大且意义不强，这时候我们就可以使用AOP来解决！

SpringMVC已经为我们提供了一套解决方案，那就是异常处理器。异常处理器：集中的、统一的处理项目中出现的异常。

## 3.1 异常处理器的使用

创建异常处理器类

```java
// @RestControllerAdvice标识该类为REST风格对应的异常处理器，包含了@ControllerAdvice + @ResponseBody
@RestControllerAdvice
public class ProjectExceptionAdvice {
    // 除了自定义的异常处理器，保留对Exception类型的异常处理，用于处理非预期的异常
    @ExceptionHandler(Exception.class)
    public Result doException(Exception ex){
        System.out.println("嘿嘿，异常你哪里跑！");
        return new Result(666, null, "嘿嘿，异常你哪里跑！");
    }
}
```

```java
// 让getByID方法抛出一个异常测试
@GetMapping("/{id}")
public Result getById(@PathVariable Integer id) {
  	int i = 1/0;
    Book book = bookService.getById(id);
    Integer code = book != null ? Code.GET_OK : Code.GET_ERR;
    String msg = book != null ? "" : "数据查询失败，请重试！";
    return new Result(code，book，msg);
}
```

运行程序，PostMan用GET请求方式输入http://localhost/books/1，会接收到如下数据：

```json
{
    "data": null,
    "code": 666,
    "msg": "嘿嘿，异常你哪里跑！"
}
```

至此，就算后台执行的过程中抛出异常，最终也能按照我们和前端约定好的格式返回给前端。

| 名称 | @RestControllerAdvice                                        |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在Rest风格开发的控制器增强类上方定义                 |
| 包含 | 包含了`@ControllerAdvice` + `@ResponseBody`                  |
| 作用 | 为Rest风格开发的控制器类做增强，标识该类为REST风格对应的异常处理器类。 |

| 名称 | @ExceptionHandler                                            |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在专用于异常处理的控制器方法上方定义。例如`@ExceptionHandler(Exception.class)` |
| 作用 | 设置指定异常的处理方案。出现异常后终止原始控制器执行，并转入当前方法执行。 |

## 3.2 项目异常处理方案

异常处理器我们已经能够使用了，那么在咱们的项目中该如何来处理异常呢？因为异常的种类有很多，如果每一个异常都对应一个`@ExceptionHandler`，那么会特别的麻烦。所以我们在处理异常之前，需要对异常进行一个分类，根据这个分类来处理。我们分为三类：业务异常（BusinessException）、系统异常（SystemException）、系统异常（SystemException）。

业务异常（BusinessException）：

- 规范的用户行为产生的异常。例如：用户输入内容未按照指定格式进行填写，如在年龄框输入的是字符串

- 不规范的用户行为操作产生的异常。例如：用户故意传递错误数据

- 解决方案：发送对应消息传递给用户，提醒规范操作。我们常见的就是提示用户名已存在或密码格式不正确等。


系统异常（SystemException）：

- 项目运行过程中可预计但无法避免的异常。例如：数据库或服务器宕机

- 解决方案：
  - 发送固定消息传递给用户，安抚用户。例如：系统繁忙，请稍后再试；系统正在维护升级，请稍后再试；系统出问题，请联系系统管理员等。
  - 发送特定消息给运维人员，提醒维护。例如可以发送短信、邮箱或者是公司内部通信软件
  - 记录日志。发消息和记录日志对用户来说是不可见的，属于后台程序


其他异常（Exception）

- 编程人员未预期到的异常。例如：用到的文件不存在，也就是出现`FileNotFoundException`异常。
- 解决方案：
  - 发送固定消息传递给用户，安抚用户
  - 发送特定消息给编程人员，提醒维护（纳入预期范围内）。一般是程序没有考虑全，比如未做非空校验等
  - 记录日志


**异常解决方案实现**

思路如下：

1. 先通过自定义异常，完成`BusinessException`和`SystemException`的定义
2. 将其他异常包装成自定义异常类型
3. 在异常处理器类中对不同的异常进行处理

接下来我们来具体实现一下：

1. 自定义异常类

   ```java
   package com.linxuan.exception;
   
   // 自定义异常处理器，用于封装异常信息，对异常进行分类
   public class SystemException extends RuntimeException {
       private Integer code;
   
       public SystemException(Integer code, String message) {
           super(message);
           this.code = code;
       }
   
       public SystemException(Integer code, String message, Throwable cause) {
           super(message, cause);
           this.code = code;
       }
   
       public Integer getCode() {
           return code;
       }
   
       public void setCode(Integer code) {
           this.code = code;
       }
   }
   ```
   
   ```java
   package com.linxuan.exception;
   
   // 自定义异常处理器，用于封装异常信息，对异常进行分类
   public class BusinessException extends RuntimeException {
       private Integer code;
   
       public Integer getCode() {
           return code;
       }
   
       public void setCode(Integer code) {
           this.code = code;
       }
   
       public BusinessException(Integer code, String message) {
           super(message);
           this.code = code;
       }
   
       public BusinessException(Integer code, String message, Throwable cause) {
           super(message, cause);
           this.code = code;
       }
   }
   ```
   
   让自定义异常类继承`RuntimeException`的好处是，后期在抛出这两个异常的时候，就不用在try...catch...或throws了
   
   自定义异常类中添加`code`属性的原因是为了更好的区分异常是来自哪个业务的
   
2. 将其他异常包弄成自定义异常。

   假如在`BookServiceImpl`的`getById`方法抛异常了，该如何来包装呢?

   ```java
   public Book getById(Integer id) {
       // 模拟业务异常，包装成自定义异常
       if (id == 1) {
           throw new BusinessException(Code.BUSINESS_ERR, "请不要使用你的技术挑战我的耐性!");
       }
       // 模拟系统异常，将可能出现的异常进行包装，转换成自定义异常
       try {
           int i = 1 / 0;
       } catch (Exception e) {
           throw new SystemException(Code.SYSTEM_TIMEOUT_ERR, "服务器访问超时，请重试!", e);
       }
       return bookDao.getById(id);
   }
   ```

   具体的包装方式有：

   * 方式一：`try{}catch(){}`在`catch`中重新`throw`我们自定义异常即可。

   * 方式二：直接throw自定义异常即可

   上面为了使`code`看着更专业些，我们在Code类中再新增需要的属性

   ```java
   package com.linxuan.controller;
   
   // 状态码
   public class Code {
       public static final Integer SAVE_OK = 20011;
       public static final Integer DELETE_OK = 20021;
       public static final Integer UPDATE_OK = 20031;
       public static final Integer GET_OK = 20041;
   
       public static final Integer SAVE_ERR = 20010;
       public static final Integer DELETE_ERR = 20020;
       public static final Integer UPDATE_ERR = 20030;
       public static final Integer GET_ERR = 20040;
   
       public static final Integer SYSTEM_ERR = 50001;
       public static final Integer SYSTEM_TIMEOUT_ERR = 50002;
       public static final Integer SYSTEM_UNKNOW_ERR = 59999;
   
       public static final Integer BUSINESS_ERR = 60002;
   }
   ```

3. 处理器类中处理自定义异常

   ```java
   package com.linxuan.controller;
   
   // @RestControllerAdvice用于标识当前类为REST风格对应的异常处理器
   @RestControllerAdvice
   public class ProjectExceptionAdvice {
   
       // @ExceptionHandler用于设置当前处理器类对应的异常类型
       @ExceptionHandler(SystemException.class)
       public Result doSystemException(SystemException ex) {
           // 记录日志
           // 发送消息给运维
           // 发送邮件给开发人员，ex对象发送给开发人员
           return new Result(ex.getCode(), null, ex.getMessage());
       }
   
       @ExceptionHandler(BusinessException.class)
       public Result doBusinessException(BusinessException ex) {
           return new Result(ex.getCode(), null, ex.getMessage());
       }
   
       // 除了自定义的异常处理器，保留对Exception类型的异常处理，用于处理非预期的异常
       @ExceptionHandler(Exception.class)
       public Result doException(Exception ex) {
           // 记录日志
           // 发送消息给运维
           // 发送邮件给开发人员，ex对象发送给开发人员
           return new Result(Code.SYSTEM_UNKNOW_ERR, null, "系统繁忙，请稍后再试！");
       }
   }
   ```

4. 运行程序

   根据ID查询，如果传入的参数为1，会报`BusinessException`。打开PostMan使用GET请求方式发送URL为`http://localhost/books/1`，那么会报`BusinessException`，返回结果数据如下：

   ```JSON
   {
       "data": null,
       "code": 60002,
       "msg": "请不要使用你的技术挑战我的耐性!"
   }
   ```
   
   如果传入的是其他参数，会报`SystemException`。打开PostMan使用GET请求方式发送URL为`http://localhost/books/2`，那么会报`SystemException`，返回结果数据如下：

   ```json
   {
       "data": null,
       "code": 50002,
       "msg": "服务器访问超时，请重试!"
   }
   ```

对于异常我们就已经处理完成了，不管后台哪一层抛出异常，都会以我们与前端约定好的方式进行返回，前端只需要把信息获取到，根据返回的正确与否来展示不同的内容即可。

以后项目中的异常处理方式为:

![1630658821746](../图片/5-06【SpringMVC】/2-5.png)

# 第五章 拦截器Interceptor

讲解拦截器的概念之前，我们先看一张图:

![1630676280170](../图片/5-06【SpringMVC】/2-6.png)

1. 浏览器发送一个请求会先到Tomcat的web服务器

2. Tomcat服务器接收到请求以后，会去判断请求的是静态资源还是动态资源

3. 如果是静态资源，会直接到Tomcat的项目部署目录下去直接访问

4. 如果是动态资源，就需要交给项目的后台代码进行处理

5. 在找到具体的方法之前，我们可以去配置过滤器(可以配置多个)，按照顺序进行执行

6. 然后进入到到中央处理器(SpringMVC中的内容)，SpringMVC会根据配置的规则进行拦截

7. 如果满足规则，则进行处理，找到其对应的controller类中的方法进行执行，完成后返回结果

8. 如果不满足规则，则不进行处理

9. 这个时候，如果我们需要在每个Controller方法执行的前后添加业务，那么就用到了拦截器。


拦截器（Interceptor）是一种动态拦截方法调用的机制，在SpringMVC中动态拦截控制器方法的执行。拦截器作用是在指定的方法调用前后执行预先设定的代码、阻止原始方法的执行。拦截器就是用来做增强。

拦截器和过滤器在作用和执行顺序上也很相似，但是他们不是一个东西。两者之前有区别

- 归属不同：Filter属于Servlet技术，Interceptor属于SpringMVC技术
- 拦截内容不同：Filter对所有访问进行增强，Interceptor仅针对SpringMVC的访问进行增强

![1630676903190](../图片/5-06【SpringMVC】/2-7.png)

## 5.1 拦截器入门案例

**环境准备**

- 创建一个Web的Maven项目

- pom.xml添加SSM整合所需jar包

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
  
    <groupId>com.linxuan</groupId>
    <artifactId>springmvc05_intercetor</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
  
    <dependencies>
      <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.10.RELEASE</version>
      </dependency>
      <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.9.0</version>
      </dependency>
    </dependencies>
  
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
  </project>
  ```
  
- 创建对应的配置类

  ```java
  package com.linxuan.config;
  
  public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
      @Override
      protected Class<?>[] getRootConfigClasses() {
          return new Class[0];
      }
  
      @Override
      protected Class<?>[] getServletConfigClasses() {
          return new Class[]{SpringMvcConfig.class};
      }
  
      @Override
      protected String[] getServletMappings() {
          return new String[]{"/"};
      }
  }
  ```

  ```java
  package com.linxuan.config;
  
  @Configuration
  @ComponentScan("com.linxuan.controller")
  @EnableWebMvc
  public class SpringMvcConfig {
  }
  ```

- 创建模型类Book

  ```java
  package com.linxuan.domain;
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class Book {
      private String name;
      private double price;
  }
  ```

- 编写Controller

  ```java
  @RestController
  @RequestMapping("/books")
  public class BookController {
  
      @PostMapping
      public String save(@RequestBody Book book){
          System.out.println("book save..." + book);
          return "{'module':'book save'}";
      }
  
      @DeleteMapping("/{id}")
      public String delete(@PathVariable Integer id){
          System.out.println("book delete..." + id);
          return "{'module':'book delete'}";
      }
  
      @PutMapping
      public String update(@RequestBody Book book){
          System.out.println("book update..."+book);
          return "{'module':'book update'}";
      }
  
      @GetMapping("/{id}")
      public String getById(@PathVariable Integer id){
          System.out.println("book getById..."+id);
          return "{'module':'book getById'}";
      }
  
      @GetMapping
      public String getAll(){
          System.out.println("book getAll...");
          return "{'module':'book getAll'}";
      }
  }
  ```

**拦截器开发**

1. 创建拦截器类

   让类实现`HandlerInterceptor`接口，重写接口中的三个方法。

   ```java
   package com.linxuan.controller.interceptor;
   
   @Component
   // 定义拦截器类，实现HandlerInterceptor接口
   // 注意当前类必须受Spring容器控制
   public class ProjectInterceptor implements HandlerInterceptor {
       // 原始方法调用前执行的内容
       @Override
       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
           System.out.println("preHandle...");
           return true;
       }
   
       // 原始方法调用后执行的内容
       @Override
       public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
           System.out.println("postHandle...");
       }
   
       // 原始方法调用完成后执行的内容
       @Override
       public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
           System.out.println("afterCompletion...");
       }
   }
   ```

   > 拦截器类要被SpringMVC容器扫描到。

2. 配置拦截器类

   ```java
   package com.linxuan.config;
   
   @Configuration
   public class SpringMvcSupport extends WebMvcConfigurationSupport {
   
       @Autowired
       private ProjectInterceptor projectInterceptor;
   
       @Override
       protected void addResourceHandlers(ResourceHandlerRegistry registry) {
           registry.addResourceHandler("/pages/**").addResourceLocations("/pages/");
       }
   
       // 配置拦截器
       @Override
       protected void addInterceptors(InterceptorRegistry registry) {
           registry.addInterceptor(projectInterceptor).addPathPatterns("/books");
       }
   }
   ```

3. SpringMVC添加`SpringMvcSupport`包扫描

   ```java
   @Configuration
   @ComponentScan({"com.linxuan.controller"，"com.linxuan.config"})
   @EnableWebMvc
   public class SpringMvcConfig{
   }
   ```
   
4. 运行程序测试

   使用PostMan发送`http://localhost/books`

   如果发送`http://localhost/books/100`会发现拦截器没有被执行，原因是拦截器的`addPathPatterns`方法配置的拦截路径是`/books`，我们现在发送的是`/books/100`，所以没有匹配上，因此没有拦截，拦截器就不会执行。

5. 修改拦截器拦截规则

   ```java
   @Configuration
   public class SpringMvcSupport extends WebMvcConfigurationSupport {
       @Autowired
       private ProjectInterceptor projectInterceptor;
   
       @Override
       protected void addResourceHandlers(ResourceHandlerRegistry registry) {
           registry.addResourceHandler("/pages/**").addResourceLocations("/pages/");
       }
   
       @Override
       protected void addInterceptors(InterceptorRegistry registry) {
           //配置拦截器
           registry.addInterceptor(projectInterceptor).addPathPatterns("/books", "/books/*" );
       }
   }
   
   /*
       preHandle...
       book getById...100
       postHandle...
       afterCompletion...
   */
   ```
   
   这个时候，如果再次访问`http://localhost/books/100`，拦截器就会被执行。
   
   在addPathPatterns方法里面的参数为什么可以直接添加呢？不用数组。我们看一下源码就知道了，它的参数是一个可变参数，所以可以直接添加。`public InterceptorRegistration addPathPatterns(String... patterns)`
   
   拦截器中的`preHandler`方法，如果返回true，则代表放行，会执行原始Controller类中要请求的方法，如果返回false，则代表拦截，后面的就不会再执行了。

- 简化`SpringMvcSupport`的编写

  ```java
  @Configuration
  @ComponentScan({"com.linxuan.controller"})
  @EnableWebMvc
  // 实现WebMvcConfigurer接口可以简化开发，但具有一定的侵入性
  public class SpringMvcConfig implements WebMvcConfigurer {
      @Autowired
      private ProjectInterceptor projectInterceptor;
  
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          // 配置多拦截器
          registry.addInterceptor(projectInterceptor).addPathPatterns("/books", "/books/*");
      }
  }
  ```

  此后咱们就不用再写`SpringMvcSupport`类了。

  最后我们来看下拦截器的执行流程：

  ![1630679464294](../图片/5-06【SpringMVC】/2-8.png)

  当有拦截器后，请求会先进入`preHandle`方法：

  * 如果方法返回true，则放行继续执行后面的handle[controller的方法]和后面的方法。

  * 如果返回false，则直接跳过后面方法的执行。

## 5.2 拦截器参数

### 5.2.1 前置处理方法

原始方法之前运行preHandle

```java
public boolean preHandle(HttpServletRequest request,
                         HttpServletResponse response,
                         Object handler) throws Exception {
    System.out.println("preHandle");
    return true;
}
```

* `request`:请求对象
* `response`:响应对象
* `handler`:被调用的处理器对象，本质上是一个方法对象，对反射中的Method对象进行了再包装

使用request对象可以获取请求数据中的内容，如获取请求头的`Content-Type`

```java
public boolean preHandle(HttpServletRequest request, 
                         HttpServletResponse response, 
                         Object handler) throws Exception {
    String contentType = request.getHeader("Content-Type");
    System.out.println("preHandle..." + contentType);
    return true;
}

/*
    preHandle...application/json
    book getAll...
    postHandle...
    afterCompletion...
*/
```

使用handler参数，可以获取方法的相关信息

```java
public boolean preHandle(HttpServletRequest request, 
                         HttpServletResponse response, 
                         Object handler) throws Exception {
    HandlerMethod hm = (HandlerMethod)handler;
    String methodName = hm.getMethod().getName(); // 可以获取方法的名称
    System.out.println("preHandle..."+methodName);
    return true;
}

/*
    preHandle...getAll
    book getAll...
    postHandle...
    afterCompletion...
*/
```

### 5.2.2 后置处理方法

原始方法运行后运行，如果原始方法被拦截，则不执行  

```java
public void postHandle(HttpServletRequest request，
                       HttpServletResponse response，
                       Object handler，
                       ModelAndView modelAndView) throws Exception {
    System.out.println("postHandle");
}
```

前三个参数和上面的是一致的。

`modelAndView`：如果处理器执行完成具有返回结果，可以读取到对应数据与页面信息，并进行调整。

因为咱们现在都是返回json数据，所以该参数的使用率不高。

### 5.2.3 完成处理方法

拦截器最后执行的方法，无论原始方法是否执行

```java
public void afterCompletion(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler,
                            Exception ex) throws Exception {
    System.out.println("afterCompletion");
}
```

前三个参数与上面的是一致的。

`ex`：如果处理器执行过程中出现异常对象，可以针对异常情况进行单独处理。因为我们现在已经有全局异常处理器类，所以该参数的使用率也不高。

这三个方法中，最常用的是`preHandle`，在这个方法中可以通过返回值来决定是否要进行放行，我们可以把业务逻辑放在该方法中，如果满足业务则返回true放行，不满足则返回false拦截。

## 5.3 拦截器链配置

目前，我们在项目中只添加了一个拦截器，如果有多个，该如何配置？配置多个后，执行顺序是什么？

接下来我们做一下来看看：

1. 创建拦截器类

   实现接口，并重写接口中的方法

   ```java
   @Component
   public class ProjectInterceptor2 implements HandlerInterceptor {
       @Override
       public boolean preHandle(HttpServletRequest request， HttpServletResponse response， Object handler) throws Exception {
           System.out.println("preHandle222...");
           return false;
       }
   
       @Override
       public void postHandle(HttpServletRequest request， HttpServletResponse response， Object handler， ModelAndView modelAndView) throws Exception {
           System.out.println("postHandle222...");
       }
   
       @Override
       public void afterCompletion(HttpServletRequest request， HttpServletResponse response， Object handler， Exception ex) throws Exception {
           System.out.println("afterCompletion222...");
       }
   }
   ```

2. 配置拦截器类

   ```java
   @Configuration
   @ComponentScan({"com.linxuan.controller"})
   @EnableWebMvc
   //实现WebMvcConfigurer接口可以简化开发，但具有一定的侵入性
   public class SpringMvcConfig implements WebMvcConfigurer {
       @Autowired
       private ProjectInterceptor projectInterceptor;
       @Autowired
       private ProjectInterceptor2 projectInterceptor2;
   
       @Override
       public void addInterceptors(InterceptorRegistry registry) {
           //配置多拦截器
           registry.addInterceptor(projectInterceptor).addPathPatterns("/books"，"/books/*");
           registry.addInterceptor(projectInterceptor2).addPathPatterns("/books"，"/books/*");
       }
   }
   ```

3. 运行程序，观察顺序

   ```apl
   preHandle111...
   preHandle222...
   book getAll...
   postHandle222...
   postHandle111...
   afterCompletion222...
   afterCompletion111...
   ```

   拦截器执行的顺序是和配置顺序有关。就和前面所提到的运维人员进入机房的案例，先进后出。

   * 当配置多个拦截器时，形成拦截器链

   * 拦截器链的运行顺序参照拦截器添加顺序为准

   * 当拦截器中出现对原始处理器的拦截，后面的拦截器均终止运行

   * 当拦截器运行中断，仅运行配置在前面的拦截器的afterCompletion操作

   ![1630680579735](../图片/5-06【SpringMVC】/2-9.png)

   `preHandle`：与配置顺序相同，必定运行。

   `postHandle`：与配置顺序相反，可能不运行。

   `afterCompletion`：与配置顺序相反，可能不运行。

   这个顺序不太好记，最终只需要把握住一个原则即可：以最终的运行结果为准。