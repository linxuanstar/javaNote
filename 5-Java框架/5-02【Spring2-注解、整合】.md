# 第一章 常用注解

从Java EE5规范开始，Servlet中增加了两个影响Servlet生命周期的注解，`@PostConstruct`和`@PreDestroy`，这两个注解被用来修饰一个非静态的`void()`方法。

| 名称 | @PostConstruct（构造之后）、@PreDestroy（销毁之前）          |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在方法上定义                                       |
| 替换 | 替换掉了`<bean init-method=""/>`、`<bean destroy-method=""/>` |
| 作用 | 设置该方法为初始化方法、销毁方法                             |
| 属性 | 无                                                           |

## 1.1 Spring注解

<!-- repository 仓库 -->

### 1.1.1 IOC/DI

| 名称 | @Component/@Controller/@Service/@Repository                  |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换掉了`<bean id="" class=""/>`                             |
| 作用 | 设置该类为spring管理的bean。@Controller(表现层)、@Service(业务层)、@Repository(数据层)。 |
| 属性 | value（默认）：定义bean的id                                  |

| 名称 | @Configuration                           |
| ---- | ---------------------------------------- |
| 类型 | 类注解，在类的上方定义                   |
| 替换 | 替换掉了`applicationContext.xml`配置文件 |
| 作用 | 设置该类为Spring的配置类。               |
| 属性 | value（默认）：定义bean的id              |

| 名称 | @ComponentScan                                               |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义。例如`@ComponentScan("com.linxuan")`  |
| 替换 | 替换掉了`<context:component-scan base-package=""/>`          |
| 作用 | 设置配置类包扫描路径，扫描该包及其子包中所有类上的注解。此注解只添加一次，多个数据用数组格式 |
| 属性 | value（默认）：扫描路径，此路径可以逐层向下扫描<br />excludeFilters：排除扫描路径中加载的bean，需要指定类别(type)和具体项(classes)<br/>includeFilters：加载指定的bean，需要指定类别(type)和具体项(classes) |

| 名称 | @Scope                                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换掉了`<bean scope=""/>`                                   |
| 作用 | 设置该类创建对象的作用范围，可用于设置创建出的bean是否为单例对象 |
| 属性 | value（默认）：定义bean作用范围，默认值singleton（单例），可选值prototype（非单例） |

| 名称 | @Autowired                                                  |
| ---- | ----------------------------------------------------------- |
| 类型 | 属性注解、方法注解（了解）或者方法形参注解（了解）          |
| 替换 | 替换掉了`<bean autowire="byType|byName"/>`                  |
| 作用 | 自动装配，默认按照该属性类型在IOC容器中找该Bean对象进行注入 |
| 属性 | required：true/false，定义该属性是否允许为null              |

| 名称 | @Qualifier（和@Autowired一起使用）                           |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解、方法注解（了解）                                   |
| 替换 | 替换掉了替换掉了`<bean autowire="byName"/>`                  |
| 作用 | 指定注入bean对象的ID。如果在IOC容器找到该类型有多个对象，那么需要它用来指定名称。 |
| 属性 | value（默认）：设置注入的bean对象ID                          |

| 名称 | @Value                                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解、方法注解（了解）                                   |
| 替换 | 替换掉了`<property name="" value=""/>`、`<constructor-arg name="" value=""/>` |
| 作用 | 为基本数据类型或字符串类型属性设置值                         |
| 属性 | value（默认）：要注入的属性值                                |

| 名称 | @PropertySource                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换掉了`<context:property-placeholder location=""/>`        |
| 作用 | 加载properties文件                                           |
| 属性 | value（默认）：设置加载的properties文件对应的文件名或文件名组成的数组 |

| 名称 | @Bean                                                        |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，定义在方法的上方                                   |
| 替换 | 替换掉了`<bean id="" class=""> <property name="" value=""/> </bean>` |
| 作用 | 设置该方法的返回值作为spring管理的bean                       |
| 属性 | value（默认）：定义bean的id                                  |

| 名称 | @Import                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 无                                                           |
| 作用 | 导入其他配置类，@Import注解在配置类中只写一次，使用数组格式一次性导入多个配置类 |
| 属性 | value（默认）：定义导入的配置类类名。                        |

### 1.1.2 测试/事务

**测试**

| 名称 | @RunWith                                                     |
| ---- | ------------------------------------------------------------ |
| 类型 | 测试类注解，在测试类上方定义。`@RunWith(SpringJUnit4ClassRunner.class)` |
| 替换 | 无                                                           |
| 作用 | 设置JUnit运行器，测试类必须要设置                            |
| 属性 | value（默认）：运行所使用的运行期                            |

| 名称 | @ContextConfiguration                                        |
| ---- | ------------------------------------------------------------ |
| 类型 | 测试类注解，在测试类上方定义`@ContextConfiguration(classes/locations={配置类.class/"配置文件"})` |
| 替换 | 无                                                           |
| 作用 | 设置JUnit加载Spring的核心配置文件/核心配置类。               |
| 属性 | classes使用数组格式加载配置类，locations使用数组的格式加载配置文件 |

**事务**

| 名称 | @EnableTransactionManagement                                 |
| ---- | ------------------------------------------------------------ |
| 类型 | 配置类注解，在Spring配置类上方定义                           |
| 替换 | 替换掉了`<tx:annotation-driven transaction-manager="txManager"/>` |
| 作用 | 设置当前Spring环境中开启注解式事务支持                       |

| 名称 | @Transactional                                               |
| ---- | ------------------------------------------------------------ |
| 类型 | 接口注解、类注解、方法注解，在业务层接口上方、业务层实现类上方、业务方法上方定义 |
| 作用 | 为当前业务层方法添加事务（如果设置在类或接口上方则类或接口中所有方法均添加事务） |

### 1.1.3 AOP

| 名称 | @EnableAspectJAutoProxy                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 配置类注解，在类的上方定义                                   |
| 替换 | 替换掉了 `<aop:aspectj-autoproxy/>`                          |
| 作用 | 开启注解格式AOP功能，如果使用AOP那么该注解必须在配置类上面标识 |
| 属性 | 无                                                           |

| 名称 | @Aspect                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 作用 | 设置当前类为AOP切面类，这个切面类里面描述通知和切入点的对应关系 |
| 属性 | 无                                                           |

| 名称 | @Pointcut                                                    |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在切入点上方定义。`@Pointcut("execution(void com.linxuan.dao.BookDao.delete())")` |
| 作用 | 设置切入点方法。                                             |
| 属性 | value（默认）：切入点表达式                                  |

| 名称 | @Before                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义。`@Before("pt()")`            |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法前运行 |
| 属性 | value（默认）：切入点方法                                    |

| 名称 | @After                                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义。`@After("pt()")`             |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法后运行 |
| 属性 | value（默认）：切入点方法                                    |

| 名称 | @Around                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义。`@Around("pt()")`            |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法前后运行 |
| 属性 | value（默认）：切入点方法                                    |

## 1.2 SpringMVC注解

| 名称 | @RequestMapping                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解或方法注解，在SpringMVC控制器类或方法定义上方定义。`@RequestMapping("/save")` |
| 作用 | 设置当前控制器方法请求访问路径                               |
| 属性 | value(默认)，请求访问路径                                    |

| 名称 | @ResponseBody                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解或方法注解，在SpringMVC控制器类或方法上方，写在类上，该类的所有方法都有该注解功能。 |
| 作用 | 设置当前控制器返回值作为响应体，并不是资源名称无需解析。 <br />返回值为字符串，会将其作为文本响应给前端。返回值为对象，会将对象转换成JSON响应给前端。 |
| 属性 | pattern：指定日期时间格式字符串                              |

| 名称 | @RequestParam                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义。                |
| 示例 | `public String commonParam(@RequestParam("name") String userName)` |
| 作用 | 绑定请求参数与处理器方法形参间的关系，前端参数和后端形参变量名不一样的时候用该注解。 |
| 属性 | required是否为必传参数、defaultValue参数默认值。             |

| 名称 | @EnableWebMvc                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 配置类注解，在SpringMVC配置类上方定义                        |
| 替换 | 替换掉了`<mvc:annotation-driven>`                            |
| 作用 | 开启SpringMVC的注解支持，有多项辅助功能。包含将JSON转换成对象的功能。标配，不要忘记。 |

| 名称 | @RequestBody                                                 |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String commonParam(@RequestBody User user)`          |
| 作用 | 将请求中请求体所包含的数据传递给请求参数，就是将前端的JSON数据映射到形参的对象中作为数据<br />使用@RequestBody注解将外部传递的json数组数据映射到形参的集合对象中作为数据 |

| 名称 | @DateTimeFormat                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String dataParam(@DateTimeFormat(pattern="yyyy/MM/dd HH:mm:ss") Date date)` |
| 作用 | 设定日期时间型数据格式，如果前端传输日期格式不是yyyy/MM/dd，那么就需要使用它来指定日期格式。 |
| 属性 | pattern：指定日期时间格式字符串                              |

| 名称 | @PathVariable                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String delete(@PathVariable Integer id)`             |
| 作用 | RESTful风格传递路径参数，绑定路径参数与后端方法形参间的关系，要求路径参数名与形参名一一对应 |

| 名称 | @RestController                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，基于SpringMVC的RESTful开发控制器类上方定义           |
| 替换 | 替换掉了@Controller与@ResponseBody两个注解                   |
| 作用 | 设置当前控制器类为RESTful风格。设置该类交由Spring管理，所有方法返回值作为响应体，无需解析。 |

| 名称 | @GetMapping/@PostMapping/@PutMapping/@DeleteMapping          |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，基于SpringMVC的RESTful开发控制器方法上方定义。例如`@GetMapping("/{id}")` |
| 作用 | 设置当前控制器方法请求访问路径与请求动作，每种对应一个请求动作。@GetMapping对应GET请求 |
| 属性 | value（默认）：请求访问路径                                  |

| 名称 | @RestControllerAdvice                                        |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在Rest风格开发的控制器增强类上方定义                 |
| 包含 | 包含了`@RestController` = `@Response` + `@Controller`        |
| 作用 | 为Rest风格开发的控制器类做增强，标识该类为REST风格对应的异常处理器类。 |

| 名称 | @ExceptionHandler                                            |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在专用于异常处理的控制器方法上方定义。例如`@ExceptionHandler(Exception.class)` |
| 作用 | 设置指定异常的处理方案。出现异常后终止原始控制器执行，并转入当前方法执行。 |

## 1.3 SpringBoot注解

| 名称 | @SpringBootApplication                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换了`@Configuration`、`@EnableAutoConfiguration`、`@ComponentScan`三个注解 |
| 作用 | SpringBoot程序启动类，申明让spring boot自动给程序进行必要的配置。 |

| 名称 | @EnableAutoConfiguration                                     |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 作用 | SpringBoot自动配置。根据依赖中的jar包，自动选择实例化某些配置，实例化对象。 |

| 名称 | @ConfigurationProperties                          |
| ---- | ------------------------------------------------- |
| 类型 | 类注解，在类的上方定义                            |
| 作用 | 获取配置文件中的属性定义并绑定到Java Bean或属性中 |

| 名称 | @SpringBootTest        |
| ---- | ---------------------- |
| 类型 | 类注解，在类的上方定义 |
| 作用 | 测试类                 |

| 名称 | @Mapper                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在dao层接口上面定义                                  |
| 作用 | 指定扫描Mapper接口，MyBatis会扫描该接口并创建它的代理对象交给SpringIOC容器管理 |

# 第二章 注解开发基础

Spring的IOC/DI对应的配置开发前面已经讲解完成，但是使用起来相对来说还是比较复杂的，复杂的地方在配置文件。要想真正简化开发，就需要用到Spring的注解开发。

Spring对注解支持的版本历程：2.0版开始支持注解 --> 2.5版注解功能趋于完善 --> 3.0版支持纯注解开发。Spring现在已经开发到了5.X版本了：

![](..\图片\5-02【Spring】\1-3.png)

关于注解开发，会讲解两块内容：注解开发定义bean和纯注解开发。注解开发定义bean用的是2.5版提供的注解，纯注解开发用的是3.0版提供的注解。

准备环境：

```xml
<!-- Spring依赖环境 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.2.10.RELEASE</version>
</dependency>
<!-- Junit单元测试 -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
</dependency>
<!-- Lombok简化开发 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```

```java
public interface BookDao {
    public void save();
}
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ..." );
    }
}
public interface BookService {
    public void save();
}

public class BookServiceImpl implements BookService{
    private BookDao bookDao;

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
</beans>
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    BookDao bookDao = (BookDao) ctx.getBean("bookDao");
    bookDao.save();
}
// bookDao...
```

## 2.1 注解定义bean

Spring通过注解实现bean的方式如下：

1. 删除原XML配置。将配置文件中的`<bean>`标签删除掉

   ```xml
   <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
   ```

2. Dao和Service上添加注解。

   ```java
   // 将这个类注册到IOC容器中，ID为bookDao。@Component注解不可以添加在接口上，因为接口是无法创建对象的。
   @Component("bookDao")
   public class BookDaoImpl implements BookDao {
       public void save() {
           System.out.println("book dao save ..." );
       }
   }
   ```
   
   ```java
   @Component
   public class BookServiceImpl implements BookService {
       private BookDao bookDao;
   
       public void setBookDao(BookDao bookDao) {
           this.bookDao = bookDao;
       }
   
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
       }
   }
   ```

3. 配置Spring的注解包扫描。为了让Spring能扫描到写在类上的注解，需要在配置文件上开启context命名空间进行包扫描

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!-- 开启context命名空间 -->
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="
                              http://www.springframework.org/schema/beans
                              http://www.springframework.org/schema/beans/spring-beans.xsd
                              http://www.springframework.org/schema/context
                              http://www.springframework.org/schema/context/spring-context.xsd
                              ">
       
       <!-- 
           component-scan：
               * component：组件，Spring将管理的bean视作自己的一个组件
               * scan：扫描
           base-package指定Spring框架扫描的包路径，它会扫描指定包及其子包中的所有类上的注解。
               * 包路径越多[如：com.linxuan.dao.impl]，扫描的范围越小速度越快
               * 包路径越少[如：com.linxuan]，扫描的范围越大速度越慢
               * 一般扫描到项目的组织名称即Maven的groupId下[如：com.linxuan]即可。
       -->
       <context:component-scan base-package="com.linxuan"/>
   </beans>
   ```

4. 运行主程序，查看结果。

   ```java
   public static void main(String[] args) {
       ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
       BookDao bookDao = (BookDao) ctx.getBean("bookDao");
       System.out.println(bookDao);
       // 按类型获取bean
       BookService bookService1 = ctx.getBean(BookService.class);
       System.out.println(bookService1);
       // @Component注解如果不起名称，会有一个默认值：当前类名首字母小写。所以可以按照名称获取
       BookService bookService2 = (BookService) ctx.getBean("bookServiceImpl");
       System.out.println(bookService2);
   }
   // com.linxuan.dao.impl.BookDaoImpl@41e68d87
   // com.linxuan.service.impl.BookServiceImpl@49ff7d8c
   // com.linxuan.service.impl.BookServiceImpl@49ff7d8c
   ```

对于`@Component`注解，还衍生出了其他三个注解`@Controller`(表现层)、`@Service`(业务层)、`@Repository`(数据层)。这三个注解和`@Component`注解的作用是一样的，这样方便我们后期在编写类的时候能很好的区分出这个类是属于表现层、业务层还是数据层的类。

| 名称 | @Component/@Controller/@Service/@Repository                  |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换掉了`<bean id="" class="" scope="" name=""/>`            |
| 作用 | 设置该类为spring管理的bean。@Controller(表现层)、@Service(业务层)、@Repository(数据层)。 |
| 属性 | value（默认）：定义bean的id                                  |

## 2.2 Spring配置文件

混合开发中仍然需要用到Spring配置文件，以后我们都是纯注解开发，但是避免不了仍然遇到，所以了解一下：

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
            http://www.springframework.org/schema/aop/spring-aop-4.0.xsd"
       default-lazy-init="true">

    <!-- 加载dbconfig.properties，等于@PropertySource -->
    <context:property-placeholder location="classpath:dbconfig.properties" />

    <!-- dataSource 配置数据源 -->
    <bean id="dataSource"
          class="com.alibaba.druid.pool.DruidDataSource"
          init-method="init"
          destroy-method="close">
        <!-- 基本属性 url、user、password -->
        <property name="url" value="${jdbc.url}" />
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
        <!-- 配置初始化大小、最小、最大 -->
        <property name="initialSize" value="1" />
        <property name="minIdle" value="1" />
        <property name="maxActive" value="20" />
        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="60000" />
        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="60000" />
        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="300000" />
        <property name="validationQuery" value="SELECT 'x'" />
        <property name="testWhileIdle" value="true" />
        <property name="testOnBorrow" value="false" />
        <property name="testOnReturn" value="false" />
        <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
        <property name="poolPreparedStatements" value="false" />
        <property name="maxPoolPreparedStatementPerConnectionSize" value="20" />
        <!-- 配置监控统计拦截的filters -->
        <property name="filters" value="stat" />
    </bean>

    <!-- mybatis和spring完美整合，不需要mybatis的配置映射文件 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- DAO接口所在包名，Spring会自动查找其下的类 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="lm.solution"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
    </bean>

    <!-- 对dataSource 数据源进行事务管理 -->
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 事务管理 通知 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!-- 对insert,create,update,modify,delete,remove 开头的方法进行事务管理,只要有异常就回滚 -->
            <tx:method name="insert*" propagation="REQUIRED" rollback-for="java.lang.Throwable"  read-only="false"/>
            <tx:method name="add*" propagation="REQUIRED" rollback-for="java.lang.Throwable"  read-only="false"/>
            <tx:method name="create*" propagation="REQUIRED" rollback-for="java.lang.Throwable"  read-only="false"/>
            <tx:method name="update*" propagation="REQUIRED" rollback-for="java.lang.Throwable"  read-only="false"/>
            <tx:method name="modify*" propagation="REQUIRED" rollback-for="java.lang.Throwable"  read-only="false"/>
            <tx:method name="delete*" propagation="REQUIRED" rollback-for="java.lang.Throwable"  read-only="false"/>
            <tx:method name="remove*" propagation="REQUIRED" rollback-for="java.lang.Throwable" read-only="false"/>
            <!-- find,get,select,count开头的方法,开启只读,提高数据库访问性能 -->
            <tx:method name="find*" read-only="true" />
            <tx:method name="get*" read-only="true" />
            <tx:method name="select*" read-only="true" />
            <tx:method name="count*" read-only="true" />
            <!-- 对其他方法 使用默认的事务管理 -->
            <tx:method name="*" />
        </tx:attributes>
    </tx:advice>

    <!-- 配置 Annotation 驱动，扫描@Transactional注解的类定义事务  -->
    <tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true"/>

    <!-- 事务 aop 配置 -->
    <aop:config>
        <aop:pointcut id="serviceMethods" expression="execution(public * lm.solution.service.mysql.impl..*(..))" />
        <aop:advisor advice-ref="txAdvice" pointcut-ref="serviceMethods" />
    </aop:config>

    <!-- 开启基于@AspectJ切面的注解处理器 -->
    <aop:aspectj-autoproxy proxy-target-class="true" />

</beans>
```

## 2.3 纯注解开发

上面已经可以使用注解来配置bean，但是依然有用到配置文件，在配置文件中对包进行了扫描。Spring在3.0版已经支持纯注解开发，使用Java类替代配置文件，开启了Spring快速开发赛道。也就是将配置文件删除掉，使用类来替换。

在上面的环境下删除配置文件applicationContext.xml，然后创建一个配置类：

```java
package com.linxuan.config;

// @Configuration注解是将这个类标识为一个配置类，替换掉了applicationContext.xml
// @ComponentScan包扫描注解，替换掉了<context:component-scan base-package=""/>
@Configuration
@ComponentScan("com.linxuan")
public class SpringConfig {
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = (BookDao) ctx.getBean("bookDao");
    System.out.println(bookDao);
    BookService bookService = ctx.getBean(BookService.class);
    System.out.println(bookService);
}
// com.linxuan.dao.impl.BookDaoImpl@489115ef
// com.linxuan.service.impl.BookServiceImpl@3857f613
```

可以看到两个对象依然被获取成功。

| 名称 | @Configuration                           |
| ---- | ---------------------------------------- |
| 类型 | 类注解，在类的上方定义                   |
| 替换 | 替换掉了`applicationContext.xml`配置文件 |
| 作用 | 设置该类为Spring的配置类。               |
| 属性 | value（默认）：定义bean的id              |

| 名称 | @ComponentScan                                               |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换掉了`<context:component-scan base-package=""/>`          |
| 作用 | 设置配置类包扫描路径，扫描该包及其子包中所有类上的注解。此注解只添加一次，多个数据用数组格式 |
| 属性 | value（默认）：扫描路径，此路径可以逐层向下扫描              |

# 第三章 注解开发中级

环境介绍：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
</dependencies>
```

```java
@Configuration
@ComponentScan("com.linxuan")
public class SpringConfig {
}
```

```java
public interface BookDao {
    public void save();
}
```

```java
// @Component的衍生注解，和@Component注解作用一样，定义Bean。只不过这是数据层专属的注解
@Repository
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ..." );
    }
}
```

```java
public interface BookService {
    public void save();
}
```

```java
// @Component的衍生注解，和@Component注解作用一样，定义Bean。只不过这是业务层专属的注解
@Service
public class BookServiceImpl implements BookService {
    private BookDao bookDao;
	public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    // 获取BookDao类型的Bean
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.save();
}
// book dao save ...
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    // 获取BookService类型的Bean
    BookService bookService = ctx.getBean(BookService.class);
    // // BookServiceImpl类中添加了BookDao属性，并提供了setter方法，但是没有提供配置注入BookDao，所以bookDao对象为Null，调用其save方法就会报`空指针异常`
    bookService.save();
}
// book service save ...
// Exception in thread "main" java.lang.NullPointerException
//     at com.linxuan.service.impl.BookServiceImpl.save(BookServiceImpl.java:14)
//     at com.linxuan.App.main(App.java:19)
```

## 3.1 bean作用范围 @scope

Spring默认创建的Bean对象都是单例的，Bean为单例的意思是在Spring的IOC容器中只会有该类的一个对象，bean对象只有一个就避免了对象的频繁创建与销毁，达到了bean对象的复用，性能高。也就是说多次创建相同的Bean对象他们的地址是一样的。在配置文件中由`bean`标签中的`scope="prototype"`属性来控制bean的非单例创建。

在注解开发中，由`@scope`注解控制bean的非单例创建，在BookDaoImpl类上面定义该注解这样就会变成非单例。

```java
@Repository
// @Scope设置bean的作用范围
@Scope("prototype")
public class BookDaoImpl implements BookDao {

    public void save() {
        System.out.println("book dao save ...");
    }
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao1 = ctx.getBean(BookDao.class);
    BookDao bookDao2 = ctx.getBean(BookDao.class);
    System.out.println(bookDao1);
    System.out.println(bookDao2);
}
// com.linxuan.dao.impl.BookDaoImpl@4206a205 设置好了scope，所以这里创建的是非单例
// com.linxuan.dao.impl.BookDaoImpl@29ba4338
```

| 名称 | @Scope                                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换掉了`<bean scope=""/>`                                   |
| 作用 | 设置该类创建对象的作用范围，可用于设置创建出的bean是否为单例对象 |
| 属性 | value（默认）：定义bean作用范围，默认值singleton（单例），可选值prototype（非单例） |

## 3.2 bean生命周期管理

对于bean的生命周期控制在bean的整个生命周期中所处的位置如下：

* 初始化容器：创建对象(内存分配) -> 执行构造方法 -> 执行属性注入(set操作) -> 执行bean 初始化方法 -> 使用bean 执行业务操作。

* 关闭/销毁容器：执行bean销毁方法

之前介绍过两种控制声明周期的办法：使用Bean标签控制`<bean init-method="" destroy-method=""/>`、使用接口控制`InitializingBean`和`DisposableBean`。

在注解开发中，取而代之的是`@PostConstruct`和`@PreDestroy`注解：

```java
@Repository
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ...");
    }
    
    // 在构造方法之后执行，替换 init-method
    @PostConstruct 
    public void init() {
        System.out.println("init ...");
    }
    
    // 在销毁方法之前执行，替换 destroy-method
    @PreDestroy 
    public void destroy() {
        System.out.println("destroy ...");
    }
}
```

```java
public static void main(String[] args) {
    // 需要调用AnnotationConfigApplicationContext中的close方法来关闭容器，这样会执行destroy方法
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao);
    // 关闭容器
    ctx.close(); 
}
```

`@PostConstruct`和`@PreDestroy`注解如果找不到，需要导入下面的jar包。找不到的原因是，从JDK9以后jdk中的`javax.annotation`包被移除了，这两个注解刚好就在这个包中。

```xml
<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>javax.annotation-api</artifactId>
    <version>1.3.2</version>
</dependency>
```

| 名称 | @PostConstruct（构造之后）、@PreDestroy（销毁之前）          |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在方法上定义                                       |
| 替换 | 替换掉了`<bean init-method=""/>`、`<bean destroy-method=""/>` |
| 作用 | 设置该方法为初始化方法、销毁方法                             |
| 属性 | 无                                                           |

## 3.3 类型依赖注入 @Autowired

Spring为了使用注解简化开发，并没有提供构造函数注入、setter注入对应的注解，只提供了自动装配的注解实现。

可以在`BookServiceImpl`类的`bookDao`属性上添加`@Autowired`注解，`@Autowired`可以写在属性上，也可也写在setter方法上，最简单的处理方式是写在属性上并将setter方法删除掉。

```java
@Service
public class BookServiceImpl implements BookService {

    // Autowired写在属性上面，并将setter方法删除掉
    @Autowired
    private BookDao bookDao;

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}
```

自动装配基于反射设计创建对象并通过暴力反射为私有属性进行设值，普通反射只能获取public修饰的内容。暴力反射除了获取public修饰的内容还可以获取private修改的内容。所以此处无需提供setter方法。

`@Autowired`是按照类型注入，`BookDao`接口如果有多个实现类，比如添加`BookDaoImpl2`，这个时候运行主方法，就会报错：

```java
@Repository
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ..." );
    }
}
```

```java
@Repository
public class BookDaoImpl2 implements BookDao {
    public void save() {
        System.out.println("book dao save ...2");
    }
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookService bookService = ctx.getBean(BookService.class);
    bookService.save();
}
// 报错信息如下：找到了两个Bean，不知道注入哪一个
// expected single matching bean but found 2: bookDaoImpl,bookDaoImpl2
```

此时，按照类型注入就无法区分到底注入哪个对象，解决方案就是按照名称注入了。给两个Dao类起个名称：

```java
@Repository("bookDao")
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ..." );
    }
}
@Repository("bookDao2")
public class BookDaoImpl2 implements BookDao {
    public void save() {
        System.out.println("book dao save ...2" );
    }
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookService bookService = ctx.getBean(BookService.class);
    bookService.save();
}
// 注入成功：
// book service save ...
// book dao save ...
```

这时候就会注入成功：`@Autowired`默认按照类型自动装配，如果IOC容器中同类的Bean找到多个，就按照属性名和Bean的名称匹配。因为属性名叫`bookDao`而容器中也有一个`booDao`，所以可以成功注入。

如果将两个Dao类分别改成`bookDao1`和`bookDao2`，那么会报`NoUniqueBeanDefinitionException`。因为按照类型会找到多个bean对象，此时会按照`bookDao`名称去找，而此时IOC容器只有名称叫`bookDao1`和`bookDao2`，所以找不到，会报`NoUniqueBeanDefinitionException`。

| 名称 | @Autowired                                                  |
| ---- | ----------------------------------------------------------- |
| 类型 | 属性注解、方法注解（了解）或者方法形参注解（了解）          |
| 替换 | 替换掉了`<bean autowire="byType|byName"/>`                  |
| 作用 | 自动装配，默认按照该属性类型在IOC容器中找该Bean对象进行注入 |
| 属性 | required：true/false，定义该属性是否允许为null              |

## 3.4 名称依赖注入 @Qualifier

当根据类型在容器中找到多个bean，需要注入参数的属性名又和容器中bean的名称不一致，这个时候就需要使用到`@Qualifier`来指定注入哪个名称的bean对象。

```java
@Repository("bookDao1")
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ..." );
    }
}
@Repository("bookDao2")
public class BookDaoImpl2 implements BookDao {
    public void save() {
        System.out.println("book dao save ...2" );
    }
}
```

```java
@Service
public class BookServiceImpl implements BookService {

    // @Autowired自动装配，默认按照该属性类型在IOC容器中找该Bean对象进行注入
    // @Qualifier指定注入哪个名称的bean对象，如果在IOC容器中找到该类型对象有多个，那么需要指定名称
    @Autowired
    @Qualifier("bookDao1")
    private BookDao bookDao;

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}
```

`@Qualifier`注解后的值就是需要注入的bean的名称。`@Qualifier`不能独立使用，必须和`@Autowired`一起使用

| 名称 | @Qualifier（和@Autowired一起使用）                           |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解、方法注解（了解）                                   |
| 替换 | 替换掉了替换掉了`<bean autowire="byName"/>`                  |
| 作用 | 指定注入bean对象的ID。如果在IOC容器找到该类型有多个对象，那么需要它用来指定名称。 |
| 属性 | value（默认）：设置注入的bean对象ID                          |

## 3.5 简单数据类型注入 @Value

简单类型注入的是基本数据类型或者字符串类型，在`BookDaoImpl`类中添加`name`属性，用其进行简单类型注入。数据类型换了，对应的注解也要跟着换，这次使用`@Value`注解，将值写入注解的参数中就行了。

```java
@Repository("bookDao")
public class BookDaoImpl implements BookDao {
    @Value("林炫")
    private String name;

    public void save() {
        System.out.println("book dao save ..." + name);
    }

    public String getName() {
        return name;
    }
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    // 想要使用BookDaoImpl类里面的getName()方法，那么就不能够使用多态
    BookDaoImpl bookDaoImpl = ctx.getBean(BookDaoImpl.class);
    // 林炫
    System.out.println(bookDaoImpl.getName());
}
```

这里需要注意数据格式要匹配，如将"abc"注入给int值，这样程序就会报错。

这个注解虽然看似跟直接赋值是一个效果，但是如果直接将值赋给name，那么就相当于直接写死了这个值。而有时候这些值是从配置文件中读取的，所以这时候能够使用`@Value("${}")`属性占位符读取。

| 名称 | @Value                                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解、方法注解（了解）                                   |
| 替换 | 替换掉了`<property name="" value=""/>`、`<constructor-arg name="" value=""/>` |
| 作用 | 为基本数据类型或字符串类型属性设置值                         |
| 属性 | value（默认）：要注入的属性值                                |

## 3.6 读取配置 @PropertySource

`@Value`一般会被用在从properties配置文件中读取内容进行使用：

```properties
# jdbc.properties
name=linxuan123
```

```java
@Configuration
@ComponentScan("com.linxuan")
// 配置类上添加@PropertySource注解，该注解用于加载properties文件
@PropertySource("jdbc.properties")
public class SpringConfig {
}
```

```java
@Repository
public class BookDaoImpl implements BookDao {
    @Value("${name}")
    private String name;

    public void save() {
        System.out.println("book dao save ..." + name);
    }
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = (BookDao) ctx.getBean(BookDao.class);
    bookDao.save();
}
// book dao save ...linxuan123
```

如果读取的properties配置文件有多个，可以使用`@PropertySource`的属性来指定多个。该注解的属性不支持使用通配符`*`，运行会报错。属性中可以把`classpath:`加上，代表从当前项目的根路径找文件。

```java
// 可以使用@PropertySource的属性来指定加载多个配置文件
@PropertySource({"jdbc.properties"，"xxx.properties"})
// 不支持使用通配符*，运行会报错
@PropertySource({"*.properties"}) 
// 属性中可以把classpath:加上，代表从当前项目的根路径找文件
@PropertySource({"classpath:jdbc.properties"})
```

| 名称 | @PropertySource                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换掉了`<context:property-placeholder location=""/>`        |
| 作用 | 加载properties文件                                           |
| 属性 | value（默认）：设置加载的properties文件对应的文件名或文件名组成的数组 |

# 第四章 管理第三方bean

前面定义bean的时候都是在自己开发的类上面写个注解就完成了，如果是第三方的类，这些类都是在jar包中，我们没有办法在类上面添加注解。这就用到了一个全新的注解`@Bean`，下面使用注解来管理的数据源。

环境介绍：

```xml
<dependencies>
    <!-- Spring依赖环境 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- Junit单元测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.11</version>
        <scope>test</scope>
    </dependency>
    <!-- Lombok简化开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
    </dependency>
    <!-- druid连接池技术 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
</dependencies>
```

```java
package com.linxuan.config;

// @Configuration注解是将这个类标识为一个配置类，替换掉了applicationContext.xml
@Configuration
public class SpringConfig {
}
```

```java
package com.linxuan.dao;

public interface BookDao {
    public void save();
}
```

```java
package com.linxuan.dao.impl;

// 声明该类受Spring管理，注册该bean对象到SpringIOC容器中，默认Bean名称为bookDaoImpl
@Repository
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ..." );
    }
}
```

## 4.1 管理Druid数据源 @Bean

在配置类中添加一个方法，方法的返回值就是要创建的Bean对象类型。在方法上添加`@Bean`注解。`@Bean`注解的作用是将方法的返回值制作为Spring管理的一个bean对象。

```java
// @Configuration注解是将这个类标识为一个配置类，替换掉了applicationContext.xml
@Configuration
public class SpringConfig {
    
    // @Bean注解：将该方法的返回值制作为SpringIOC容器管理的一个Bean对象
    @Bean
    public DataSource dataSource() {
        // 不能使用多态。DataSource接口中没有对应的setter方法来设置属性，所以这里用DruidDataSource实现类
        DruidDataSource ds = new DruidDataSource();
        // MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/linxuan");
        ds.setUsername("root");
        ds.setPassword("root");
        return ds;
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    DataSource dataSource = ctx.getBean(DataSource.class);
    System.out.println(dataSource);
}
// 打印信息如下：{ CreateTime:"2022-04-27 11:23:15", ActiveCount:0, PoolingCount:0, CreateCount:0,	DestroyCount:0, CloseCount:0, ConnectCount:0, Connections:[] }
```

| 名称 | @Bean                                                        |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，定义在方法的上方                                   |
| 替换 | 替换掉了`<bean id="" class=""> <property name="" value=""/> </bean>` |
| 作用 | 设置该方法的返回值作为spring管理的bean                       |
| 属性 | value（默认）：定义bean的id                                  |

## 4.2 引入外部配置类 @Import

如果把所有的第三方bean都配置到Spring的配置类`SpringConfig`中，这样不利于代码阅读和分类管理，所以可以按照类别将这些bean配置到不同的配置类中。

上面配置数据源的bean可以弄到`JdbcConfig`配置类里面，通过SpringConfig类加载并创建`DataSource`对象在IOC容器中。导入有两种方式：包扫描导入、`@Import`导入。

```java
package com.linxuan.config;

public class JdbcConfig {
    // @Bean注解：将该方法的返回值制作为SpringIOC容器管理的一个Bean对象
    @Bean
    public DataSource dataSource() {
        // 不能使用多态。DataSource接口中没有对应的setter方法来设置属性，所以这里用DruidDataSource实现类
        DruidDataSource ds = new DruidDataSource();
        // MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/linxuan");
        ds.setUsername("root");
        ds.setPassword("root");
        return ds;
    }
}
```

**使用包扫描引入**

```java
package com.linxuan.config;

// 在JdbcConfig上添加配置注解，标识该类为配置类
@Configuration
public class JdbcConfig {
    @Bean
    public DataSource dataSource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/day14");
        ds.setUsername("root");
        ds.setPassword("root");
        return ds;
    }
}
```

```java
@Configuration
// 在Spring的配置类上添加包扫描，扫描config包下面的类
@ComponentScan("com.linxuan.config")
public class SpringConfig {
}
```

这种方式虽然能够扫描到，但是不能很快的知晓都引入了哪些配置类，所有这种方式不推荐使用。

**使用`@Import`引入**

方案一实现起来有点复杂，Spring提供了第二种方案：去除外部配置类上面的注解`@Configuration`，在Spring配置类上将包扫描注解`@ComponentScan`替换为`@Import`注解手动引入需要加载的配置类。

```java
// 去除该类上面的注解@Configuration，使用手动引入加载该配置类
public class JdbcConfig {
	@Bean
    public DataSource dataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/day14");
        ds.setUsername("root");
        ds.setPassword("root");
        return ds;
    }
}
```

```java
@Configuration
// @Import注解导入外部配置类。参数需要的是一个数组，可以引入多个配置类。@Import注解在配置类中只能写一次.
@Import({JdbcConfig.class})
public class SpringConfig {
}
```

| 名称 | @Import                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 无                                                           |
| 作用 | 导入其他配置类，@Import注解在配置类中只写一次，使用数组格式一次性导入多个配置类 |
| 属性 | value（默认）：定义导入的配置类类名。                        |

## 4.3 为bean注入数据资源

在使用`@Bean`创建bean对象的时候，如果方法在创建的过程中需要其他资源数据怎么办？常用的数据类型有简单数据类型和引用数据类型。

**简单数据类型**

如果需要引入简单数据类型，那么使用`@Value`引入即可。例如使用注解管理Druid数据源时，关于数据库的四要素不应该写死在代码中，应该是从properties配置文件中读取。

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan
jdbc.username=root
jdbc.password=root
```

```java
// 配置类上添加@PropertySource注解，该注解用于加载properties文件。必须添加该注解，否则不会加载这些数据
@PropertySource("jdbc.properties")
public class JdbcConfig {

    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    // @Bean注解：将该方法的返回值制作为SpringIOC容器管理的一个Bean对象
    @Bean
    public DataSource dataSource() {
        // 不能使用多态。DataSource接口中没有对应的setter方法来设置属性，所以这里用DruidDataSource实现类
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        // 打印这些数据，看看是否从配置文件中加载
        System.out.println(driver + " " + url + " " + userName + " " + password);
        return ds;
    }
}
```

```java
// 标识该类为配置类
@Configuration
// 导入外部配置类
@Import({JdbcConfig.class})
public class SpringConfig {
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    DataSource dataSource = ctx.getBean(DataSource.class);
    System.out.println(dataSource);
}
// com.mysql.cj.jdbc.Driver jdbc:mysql://localhost:3306/linxuan root root
// { CreateTime:"2022-04-27 11:23:15", ActiveCount:0, PoolingCount:0, CreateCount:0,	DestroyCount:0, CloseCount:0, ConnectCount:0, Connections:[] }
```

**引用数据类型**

假设在构建DataSource对象的时候，需要用到BookDao对象。这种情况就是为第三方bean注入引用数据类型的资源了，引用类型注入只需要为bean定义方法设置形参即可，容器会根据类型自动装配对象。

```java
@PropertySource("jdbc.properties")
public class JdbcConfig {

    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    // 引用类型注入只需要为bean定义方法设置形参即可，容器会根据类型自动装配对象。
    @Bean
    public DataSource dataSource(BookDao bookDao){
        // 打印bookDao对象，看看是否被成功注入
        System.out.println(bookDao);
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
```

```java
@Configuration
// 扫描dao包及其子包，让Spring看到BookDaoImpl上面的@Repository注解，将BookDaoImpl类注册Bean对象到容器
@ComponentScan("com.linxuan.dao")
@Import({JdbcConfig.class})
public class SpringConfig {
}
```

```java
public static void main(String[] args) {
    // 加载配置类初始化容器，因为这里将配置文件删除掉了，所以加载配置类
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    DataSource dataSource = ctx.getBean(DataSource.class);
    System.out.println(dataSource);
}
// com.linxuan.dao.impl.BookDaoImpl@448ff1a8 对象地址被打印出来了，说明已经成功注入了
// { CreateTime:"2022-04-27 11:23:15", ActiveCount:0, PoolingCount:0, CreateCount:0,	DestroyCount:0, CloseCount:0, ConnectCount:0, Connections:[] }
```

# 第五章 Spring整合

## 5.1 Spring整合Mybatis

**搭建Mybatis环境**

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;
# 如果不存在tb_account表那么就创建
CREATE TABLE IF NOT EXISTS tb_account(
    id INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(35),
    money DOUBLE
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_account;
# 插入数据
INSERT INTO tb_account VALUES(NULL, "林炫", 10000), (NULL, "陈沐阳", 20000);
# 查询数据
SELECT * FROM tb_account;
```

```xml
<!-- 项目pom.xml导入依赖 -->
<dependencies>
    <!-- Lombok简化开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
    </dependency>
    <!-- druid连接池技术 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- mybatis依赖 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.7</version>
    </dependency>
    <!-- mysql驱动jar包 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>
</dependencies>
```

```java
package com.linxuan.pojo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Integer id;
    private String name;
    private Double money;
}
```

```java
package com.linxuan.dao;

/**
 * Dao接口相当于之前在Mybatis中学过的mapper接口，去掉了Mybatis-config.xml文件
 * 直接将实现查询的SQL语句用注解的形式在这里编写，当然如果比较复杂的查询方式还是要使用配置文件方式
 */
public interface AccountDao {
    @Insert("insert into tb_account(name，money)values(#{name}, #{money})")
    void save(Account account);

    @Delete("delete from tb_account where id = #{id} ")
    void delete(Integer id);

    @Update("update tb_account set name = #{name} , money = #{money} where id = #{id} ")
    void update(Account account);

    @Select("select * from tb_account")
    List<Account> findAll();

    @Select("select * from tb_account where id = #{id} ")
    Account findById(Integer id);
}
```

```java
package com.linxuan.service;

public interface AccountService {

    /**
     * 插入数据
     * @param account
     */
    void save(Account account);

    /**
     * 根据id删除数据
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id修改数据
     * @param account
     */
    void update(Account account);
    
    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    Account findById(Integer id);

    /**
     * 查询所有数据
     * @return
     */
    List<Account> findAll();
}      
```

```java
package com.linxuan.service.impl;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * 插入数据
     */
    public void save(Account account) {
        accountDao.save(account);
    }

    /**
     * 删除数据
     */
    public void delete(Integer id) {
        accountDao.delete(id);
    }

    /**
     * 更新数据
     */
    public void update(Account account){
        accountDao.update(account);
    }

    /**
     * 根据ID查询数据
     */
    public Account findById(Integer id) {
        return accountDao.findById(id);
    }

    /**
     * 查询所有数据
     */
    public List<Account> findAll() {
        return accountDao.findAll();
    }
}
```

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan?useSSL=false
jdbc.username=root
jdbc.password=root
```

resources目录下创建MyBatis的核心配置文件，习惯上命名为`mybatis-config.xml`：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!-- 读取外部properties配置文件 -->
    <properties resource="jdbc.properties"></properties>
    <!-- 设置连接数据库的环境 使用development环境 -->
    <environments default="development">
        <!-- 设置该数据库环境id为development -->
        <environment id="development">
            <!-- JDBC设置当前环境的事务管理都必须手动处理，MANAGED设置事务被管理 -->
            <transactionManager type="JDBC"/>
            <!-- type="POOLED"使用数据库连接池，会将创建的连接进行缓存，下次使用可以从缓存中直接获取 -->
            <!-- type="UNPOOLED"不使用数据库连接池，即每次使用连接都需要重新创建 -->
            <!-- type="JNDI" 调用上下文中的数据源 -->
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    <!-- 映射文件扫描包路径 虽然这里没有用到配置文件的方式，但是也需要导入，否则报错 -->
    <mappers>
        <package name="com.linxuan.dao"></package>
    </mappers>
</configuration>
```

```java
public static void main(String[] args) throws IOException {
    // 1. 创建SqlSessionFactoryBuilder对象
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    // 2. 加载SqlMapConfig.xml配置文件
    InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    // 3. 创建SqlSessionFactory对象
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
    // 4. 获取SqlSession
    SqlSession sqlSession = sqlSessionFactory.openSession();
    // 5. 执行SqlSession对象执行查询，获取结果User
    AccountDao accountDao = sqlSession.getMapper(AccountDao.class);

    Account ac = accountDao.findById(1);
    System.out.println(ac);

    // 6. 释放资源
    sqlSession.close();
}
// Account(id=1, name=林炫, money=10000.0)
```

**思路分析**

Spring就是为简化开发而生的，上面步骤中配置文件的编写和获取`SqlSession`对象的步骤是极为麻烦的，而`SqlSession`是由`SqlSessionFactory`创建出来的。

所以可以用Spring来简化开发。配置文件可以转换为各种配置类，`SqlSessionFactory`第三方bean对象可以交由Spring管理。

```xml
<!-- 分析一下该配置文件用Spring代替的方案 -->
<configuration>
    <!-- 读取外部properties配置文件 Spring中用@PropertySource注解代替-->
    <properties resource="jdbc.properties"></properties>
    <!-- 设置连接数据库的环境 创建配置类，制作Druid数据源bean对象交由Spring管理 -->
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
    <!-- 映射文件扫描包路径 也需要交由Spring管理 -->
    <mappers>
        <package name="com.linxuan.dao"></package>
    </mappers>
</configuration>
```

**整合Mybatis**

```xml
<!-- 导入依赖 -->
<dependencies>
    <!-- Spring依赖环境 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- Spring操作数据库需要该jar包 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- Spring与Mybatis整合的jar包，这个jar包mybatis在前面，是Mybatis提供的-->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>1.3.0</version>
    </dependency>
</dependencies>
```

```java
// 加载配置文件，读取值。如果不加载，那么@Value就无法获取值。
@PropertySource("classpath:jdbc.properties")
public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    // 制作bean，交由Spring管理
    @Bean
    public DataSource dataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
```

```java
package com.linxuan.config;

public class MybatisConfig {

    // 定义bean，SqlSessionFactoryBean，用于产生SqlSessionFactory对象
    // 这里的dataSource会自动装配进去，我们不用管
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        // 设置模型类的别名扫描，这个等于<typeAliases><package name="com.linxuan.pojo"></typeAliases>
        ssfb.setTypeAliasesPackage("com.linxuan.pojo");
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

```java
// 定义该类为配置类
@Configuration
// 包扫描路径，主要需要扫描Service包下面AccountServiceImpl类上面的@Service注解
@ComponentScan("com.linxuan")
// 引入其他配置类
@Import({JdbcConfig.class, MybatisConfig.class})
public class SpringConfig {
}
```

```java
public static void main(String[] args) {
    // 获取SpringIOC容器
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    AccountService accountService = ctx.getBean(AccountService.class);
    Account ac = accountService.findById(1);
    System.out.println(ac);
}
// 二月 05, 2023 7:09:43 下午 com.alibaba.druid.support.logging.JakartaCommonsLoggingImpl info
// 信息: {dataSource-1} inited
// Account(id=1, name=林炫, money=10000.0)
```

## 5.2 Spring整合Junit

```xml
<!-- 导入依赖 -->
<dependencies>
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
</dependencies>
```

```java
//设置类运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 加载配置类，设置Spring环境对应的配置类
@ContextConfiguration(classes = {SpringConfiguration.class})
// 加载配置文件
// @ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class AccountServiceTest {
    //支持自动装配注入bean
    @Autowired
    private AccountService accountService;
    
    @Test
    public void testFindById(){
        System.out.println(accountService.findById(1));
    }
    
    @Test
    public void testFindAll(){
        System.out.println(accountService.findAll());
    }
}
```

**注意：**

* 单元测试，如果测试的是注解配置类，则使用`@ContextConfiguration(classes = 配置类.class)`
* 单元测试，如果测试的是配置文件，则使用`@ContextConfiguration(locations={配置文件名，...})`
* Junit运行后是基于Spring环境运行的，所以Spring提供了一个专用的类运行器，这个务必要设置，这个类运行器就在Spring的测试专用包中提供的，导入的坐标就是这个东西`SpringJUnit4ClassRunner`
* 上面两个配置都是固定格式，当需要测试哪个bean时，使用自动装配加载对应的对象，下面的工作就和以前做Junit单元测试完全一样了

| 名称 | @RunWith                                                     |
| ---- | ------------------------------------------------------------ |
| 类型 | 测试类注解，在测试类上方定义                                 |
| 替换 | 无                                                           |
| 作用 | 设置JUnit运行器，测试类必须要设置`@RunWith(SpringJUnit4ClassRunner.class)` |
| 属性 | value（默认）：运行所使用的运行期                            |

| 名称 | @ContextConfiguration                                        |
| ---- | ------------------------------------------------------------ |
| 类型 | 测试类注解，在测试类上方定义                                 |
| 替换 | 无                                                           |
| 作用 | 设置JUnit加载Spring的核心配置类。`@ContextConfiguration(classes = {SpringConfiguration.class})` |
| 属性 | classes使用数组格式加载配置类，locations使用数组的格式加载配置文件 |