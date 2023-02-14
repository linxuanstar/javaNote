![](..\图片\4-02【Spring】\0-1.png)

# 第一章 Spring基础

<!--full-stack 全栈--> <!--Integration n.结合; 整合; 一体化;--> <!--Transaction 事务-->

Spring官网：https://spring.io。

Spring是分层的 Java SE/EE应用 full-stack 轻量级开源框架，以 `IoC(Inverse Of Control：控制反转)`和`AOP(Aspect Oriented Programming：面向切面编程)`为内核。提供了展现层 `SpringMVC` 和持久层 `Spring JDBCTemplate` 以及业务层事务管理等众多的企业级应用技术，还能整合开源世界众多著名的第三方框架和类库，逐渐成为使用最多的Java EE 企业应用开源框架。

Spring的优势如下：

- 方便解耦，简化开发：通过 Spring 提供的 IoC容器，可以将对象间的依赖关系交由 Spring 进行控制，避免硬编码所造成的过度耦合。用户也不必再为单例模式类、属性文件解析等这些很底层的需求编写代码，可以更专注于上层的应用。
- AOP 编程的支持：通过 Spring的 AOP 功能，方便进行面向切面编程，许多不容易用传统 OOP 实现的功能可以通过 AOP 轻松实现。
- 声明式事务的支持：可以将我们从单调烦闷的事务管理代码中解脱出来，通过声明式方式灵活的进行事务管理，提高开发效率和质量。
- 方便程序的测试：可以用非容器依赖的编程方式进行几乎所有的测试工作，测试不再是昂贵的操作，而是随手可做的事情。
- 方便集成各种优秀框架：Spring对各种优秀框架(Struts、Hibernate、Hessian、Quartz等)的支持。
- 降低 JavaEE API 的使用难度：Spring对 JavaEE API(如 JDBC、JavaMail、远程调用等)进行了薄薄的封装层，使这些 API 的使用难度大为降低。
- Java 源码是经典学习范例：Spring的源代码设计精妙、结构清晰、匠心独用，处处体现着大师对Java 设计模式灵活运用以及对 Java技术的高深造诣。它的源代码无意是 Java 技术的最佳实践的范例。

Spring发展到今天已经形成了一种开发的生态圈，我们可以完全使用Spring技术完成整个项目的构建、设计与开发。Spring有若干个项目，可以根据需要自行选择，把这些个项目组合起来，起了一个名称叫全家桶，如下图所示

![](..\图片\4-02【Spring】\1-0.png)

这些技术并不是所有的都需要学习，只需要重点关注Spring Framework、SpringBoot和SpringCloud：

* Spring Framework：Spring框架，是Spring中最早最核心的技术，也是所有其他技术的基础。
* SpringBoot：Spring是来简化开发，而SpringBoot是来帮助Spring在简化的基础上能更快速进行开发。
* SpringCloud：这个是用来做分布式之微服务架构的相关开发。

前面我们说Spring指的就是Spring Framework，下面从系统架构图来进行说明一下。

Spring Framework是Spring生态圈中最基础的项目，是其他项目的根基。Spring Framework的发展也经历了很多版本的变更，每个版本都有相应的调整。

Spring Framework5.x版本目前没有架构图，所以接下来研究的是Spring Framework4.x的架构图：

![](..\图片\4-02【Spring】\1-3.png)

分别介绍一下这些架构：

- 核心层：Core Container核心容器，这个模块是Spring最核心的模块，其他的都需要依赖该模块。
- AOP层：AOP面向切面编程，它依赖核心层容器，目的是在不改变原有代码的前提下对其进行功能增强。Aspects是对AOP思想的具体实现。

- 数据层：Data Access数据访问，Spring全家桶中有对数据访问的具体实现技术。Data Integration数据集成，Spring支持整合其他的数据层解决方案，比如Mybatis。Transactions事务，Spring中事务管理是Spring AOP的一个具体实现。
- Web层：SpringMVC

- Test层：整合了Junit来完成单元测试和集成测试


## 1.1 Spring核心概念

分析下目前编写代码过程中遇到的问题：

```java
// 数据层Dao
public class BookDaoImpl implements BookDao{
    @Override
    public void save() {
        System.out.println("book dao save");
    }
}
```

```java
// 业务层Service
public class BookServiceImpl implements BookService{

    private BookDao bookDao = new BookDaoImpl();

    @Override
    public void save() {
        bookDao.save();
    }
}
```

业务层需要调用数据层的方法，就需要在业务层`new`数据层的对象。如果数据层的实现类发生变化，那么业务层的代码也需要跟着改变，发生变更后，都需要进行编译打包和重部署。所以，现在代码在编写的过程中存在的问题是：耦合度偏高。

如果能把`new BookDaoImpl();`给去掉，变成`private BookDao bookDao;`可以降低依赖了。但是会产生新的问题，去掉以后程序无法正常运行，因为bookDao没有赋值，那么引用为Null，强行运行就会出现空指针异常。

```java
// 业务层Service
public class BookServiceImpl implements BookService{

    // 这里去掉new BookDaoImpl()
    private BookDao bookDao;

    @Override
    public void save() {
        bookDao.save();
    }
}
```

Spring提出了一个解决方案：使用对象时，在程序中不要主动使用new产生对象，转换为由外部提供对象。这就是IOC控制反转的思想。外部提供好了，精确的注入到需要的位置就是DI依赖注入了。

* **IOC(`Inversion of Control`)控制反转**：使用对象时，由主动new产生对象转换为由外部提供对象，此过程中对象创建控制权由程序转移到外部，此思想称为控制反转。
* **IOC容器**：Spring技术对IOC思想进行了实现，Spring提供了一个容器，称为IOC容器，用来充当IOC思想中的"外部"。IOC思想中的`别人[外部]`指的就是Spring的IOC容器，就是`applicationContext.xml`配置文件。
* **Bean**：IOC容器负责对象的创建、初始化等一系列工作，其中包含了数据层和业务层的类对象，被创建或被管理的对象在IOC容器中统称为Bean。IOC容器中放的就是一个个的Bean对象。
* **DI(Dependency Injection)依赖注入**：在容器中建立bean与bean之间的依赖关系的整个过程，称为依赖注入。业务层要用数据层的类对象，以前是自己`new`的，现在自己不new了，靠`别人[外部其实指的就是IOC容器]`来给注入进来。这种思想就是依赖注入。

## 1.2 IOC入门案例

**IOC(`Inversion of Control`)控制反转**：使用对象时，由主动new产生对象转换为由外部提供对象，此过程中对象创建控制权由程序转移到外部，此思想称为控制反转。

**IOC容器**：Spring技术对IOC思想进行了实现，Spring提供了一个容器，称为IOC容器，用来充当IOC思想中的"外部"。IOC思想中的`别人[外部]`指的就是Spring的IOC容器，就是`applicationContext.xml`配置文件。

**Bean**：IOC容器负责对象的创建、初始化等一系列工作，其中包含了数据层和业务层的类对象，被创建或被管理的对象在IOC容器中统称为Bean。IOC容器中放的就是一个个的Bean对象。

接下来就通过一些简单的入门案例，演示下Spring实现IOC的具体实现过程：

1. 创建Maven项目，添加Spring的依赖jar包。在`pom.xml`导入坐标：

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-context</artifactId>
           <version>5.2.10.RELEASE</version>
       </dependency>
   </dependencies>
   ```

2. 创建Java类：BookDao、BookDaoImpl、BookService、BookServiceImpl。

   ```java
   package com.linxuan.dao;
   
   public interface BookDao {
       void save();
   }
   ```

   ```java
   package com.linxuan.dao.impl;
   
   public class BookDaoImpl implements BookDao{
       public void save() {
           System.out.println("book dao save...");
       }
   }
   ```

   ```java
   package com.linxuan.service;
   
   public interface BookService {
       
       void save();
   }
   ```

   ```java
   package com.linxuan.service.impl;
   
   public class BookServiceImpl implements BookService{
       BookDao bookDao = new BookDaoImpl();
   
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
       }
   }
   ```

3. 添加spring配置文件并完成bean的配置。在resources目录下创建`applicationContext.xml`：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <!-- bean标签表示配置bean，id属性表示给bean起名字(ID唯一)，class属性表示给bean定义类型	-->
       <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
       <bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl"/>
   </beans>
   ```

4. 获取IOC容器，从容器中获取对象进行方法调用。

   ```java
   public static void main(String[] args) {
       // 获取IOC容器
       // 加载配置文件得到上下文对象，也就是容器对象
       ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
       // 获取资源
       BookService bookService = (BookService) ctx.getBean("bookService");
       bookService.save();
   }
   // 结果如下：
   // book service save ...
   // book dao save ...
   ```

## 1.3 DI入门案例

上面完成了Spring的IOC，在Spring容器中定义了`bookDao`和`bookService`的Bean对象，但是在`BookServiceImpl`的类中依然存在`BookDaoImpl`对象的new操作，它们之间的耦合度还是比较高。所以接下来用DI依赖注入解决。

**DI(Dependency Injection)依赖注入**：在容器中建立bean与bean之间的依赖关系的整个过程，称为依赖注入。业务层要用数据层的类对象，以前是自己`new`的，现在自己不new了，靠`别人[外部其实指的就是IOC容器]`来给注入进来。这种思想就是依赖注入。

1. 去除代码中的`new`操作。在`BookServiceImpl`类中，删除业务层中使用new的方式创建的dao对象。

   ```java
   public class BookServiceImpl implements BookService {
       // 删除业务层中使用new的方式创建的dao对象
       private BookDao bookDao;
   
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
       }
   }
   ```

2. 为属性提供setter方法，在`BookServiceImpl`类中，为`BookDao`提供`setter`方法。

   ```java
   public class BookServiceImpl implements BookService {
       // 删除业务层中使用new的方式创建的dao对象
       private BookDao bookDao;
   
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
       }
       // 提供对应的set方法
       public void setBookDao(BookDao bookDao) {
           this.bookDao = bookDao;
       }
   }
   ```

3. 修改配置完成注入，在配置文件中添加依赖注入的配置。

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <!-- bean标签表示配置bean，id属性表示给bean起名字(ID唯一)，class属性表示给bean定义类型	-->
       <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
       <bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
           <!-- 配置service与dao的关系 -->
           <!-- property配置当前bean的属性，name表示配置哪一个具体的属性，ref表示参照哪一个bean注入 -->
           <property name="bookDao" ref="bookDao"/>
       </bean>
   </beans>
   ```
   
   `name="bookDao"`：name表示配置哪一个具体的属性。这里是配置`BookServiceImpl`类中的`bookDao`属性。Spring的IOC容器在获取到名称后，会将首字母大写，前面加set找对应的`setBookDao()`方法进行对象注入。
   
   `ref="bookDao"`：ref表示参照哪一个bean注入。这里注入的是id为`bookDao`的Bean对象。Spring在IOC容器中找到id为`bookDao`的Bean对象给`bookService`进行注入。bean依赖注入的ref属性指定bean，必须在容器中存在。如果不存在，则会报错。
   
4. 运行程序。运行，测试结果和之前一样。

## 1.4 核心容器API

这里所说的核心容器，可以把它简单的理解为`ApplicationContext`，接下来来学习下容器的相关知识。

![](D:\Java\笔记\图片\4-02【Spring】\1-4.png)

```apl
BeanFactory(接口) # 这是容器类的顶层接口，初始化BeanFactory对象时，加载的bean延迟加载。
    |-- HierarchicalBeanFactory(接口)
    |    |-- ApplicationContext(接口) # 容器类的常用接口，初始化时bean立即加载
    |    |    |-- ConfigurableApplicationContext(接口) # 该接口提供了关闭容器的功能
    |    |         |-- AbstractApplicationContext(类)
    |    |             |-- AbstractRefreshableApplicationContext(类)
    |    |             |    |-- AbstractRefreshableConfigApplicationContext(类)
    |    |             |         |-- AbstractXmlApplicationContext(类)
    |    |             |              |-- ClassPathXmlApplicationContext(类) # 常用实现类
    |    |             |              |-- FileSystemXmlApplicationContext(类)
    |    |             |-- GenericApplicationContext(类)
    |    |-- ConfigurableBeanFactory(接口)
    |
    |-- SimpleJndiBeanFactory(类)
    |-- AutowireCapableBeanFactory(接口)
    |    |-- AbstractAutowireCapableBeanFactory
    |         |-- DefaultListableBeanFactory
    |              |-- XmlBeanFactory
    |-- ListableBeanFactory(接口)
```

**容器的创建方式**

容器的创建方式有两种，一种是我们常用的使用`ClassPathXmlApplicationContext`的方式(类路径下的XML配置文件)；另一种是使用`FileSystemXmlApplicationContext`的方式(文件系统下的XML配置文件)。

* 平常创建容器的方式是`new ClassPathXmlApplicationContext()`，翻译为类路径下的XML配置文件：

  ```java
  ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
  ```

* Spring还提供了一种创建方式`new FileSystemXmlApplicationContext()`，翻译为文件系统下的XML配置文件：

  ```java
  // 参数是具体路径，这种方式虽能实现，但是当项目的位置发生变化后，代码也需要跟着改，耦合度较高，不推荐使用。
  ApplicationContext ctx = new FileSystemXmlApplicationContext("D：\\spring\\src\\main\\resources\\applicationContext.xml");
  ```

**ConfigurableApplicationContext接口**

该接口提供了关闭容器的功能：

```java
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {
    @Override
    void close();
}
```

**ApplicationContext接口**

该接口是容器类的常用接口。

```java
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory, MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
}
```

```java
// 获取IOC容器
// 加载配置文件得到上下文对象，也就是容器对象
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
```

ApplicationContext接口常用方法：

* `<T> T getBean(Class<T> requiredType) throws BeansException;`：获取容器内Class类型的对象。

  ```java
  // 必须要确保IOC容器中该类型对应的bean对象只能有一个。
  BookDao bookDao = ctx.getBean(BookDao.class);
  ```

* `Object getBean(String name) throws BeansException;`：获取容器内ID为String名称的Bean对象。

  ```java
  // 这种方式获取Bean存在的问题是每次获取的时候都需要进行类型转换
  BookDao bookDao = (BookDao) ctx.getBean("bookDao");
  ```

* `<T> T getBean(String name, Class<T> requiredType) throws BeansException;`：获取容器内对象。

  ```java
  // 可以解决类型强转问题，但是参数又多加了一个，相对来说没有简化多少
  BookDao bookDao = ctx.getBean("bookDao"，BookDao.class);
  ```

* `String[] getBeanDefinitionNames();`：获取该容器内所有的Bean对象

**BeanFactory接口的使用**

搭建环境：

```java
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save...");
    }
	
    // 创建构造方法，用来测试BeanFactory获取Bean对象是否是延迟加载。
    public BookDaoImpl() {
        System.out.println("constructor...");
    }
}
```

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
```

`BeanFactory`是延迟加载，只有在获取bean对象的时候才会去创建。使用`BeanFactory`来创建IOC容器方式为：

```java
public static void main(String[] args) {
    Resource resources = new ClassPathResource("applicationContext.xml");
    BeanFactory bf = new XmlBeanFactory(resources);
    BookDao bookDao = bf.getBean(BookDao.class);
    bookDao.save();
}
// constructor...
// book dao save...
```

删除获取bean对象的方法再执行，这时什么都不会打印

```java
public static void main(String[] args) {
    Resource resources = new ClassPathResource("applicationContext.xml");
    BeanFactory bf = new XmlBeanFactory(resources);
}
// 什么都不会打印
```

但是如果使用`ApplicationContext`创建容器对象，这时候就会打印信息

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
}
// constructor...
```

`ApplicationContext`是立即加载，容器加载的时候就会创建bean对象。`ApplicationContext`要想成为延迟加载，只需要按照如下方式进行配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"  lazy-init="true"/>
</beans>
```

# 第二章 IOC控制反转

**IOC(`Inversion of Control`)控制反转**：使用对象时，由主动new产生对象转换为由外部提供对象，此过程中对象创建控制权由程序转移到外部，此思想称为控制反转。

<!-- Ebi全称Enterprise Business Interface，翻译为企业业务接口 -->

## 2.1 bean标签属性

bean标签可配置属性如下：

```xml
<bean
      id="bean的唯一标识"
      class="bean的全类名"
      scope="bean的取值范围，有singleton单例(默认)和prototype非单例"
      name="为bean取的别名"/>
```

**id与class**

对于bean的基础配置，在前面的案例中已经使用过：

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
```

可以不设置id，spring默认会使用类的全限定名作为bean的标识符。如果设置id属性，那么id就是bean的唯一标识符，在spring容器中必需唯一。

class属性表示给bean定义类型。需要注意的是class属性不能直接写接口，如`com.linxuan.dao.BookDao`。这里用到了反射来创建Bean对象，而接口是没办法创建对象的，所以这里不能写接口的全类名。

**name属性**

name属性为bean指定别名。别名可以有多个，可以使用逗号、分号和空格进行分隔。

```xml
<bean id="bookDao" name="dao" class="com.linxuan.dao.impl.BookDaoImpl"/>
<!--这里取的别名是service和bookEbi，获取该Bean对象的时候可以用这两个名字-->
<bean id="bookService" name="service bookEbi" class="com.linxuan.service.impl.BookServiceImpl">
    <property name="bookDao" ref="bookDao"/>
</bean>
```

**作用范围scope**

Spring默认创建的Bean对象都是单例的，Bean为单例的意思是在Spring的IOC容器中只会有该类的一个对象，bean对象只有一个就避免了对象的频繁创建与销毁，达到了bean对象的复用，性能高。也就是说多次创建相同的Bean对象他们的地址是一样的。

如果想要创建非单例的，那么可以在Spring配置文件中，配置`scope`属性来实现bean的非单例创建。

```xml
<!--创建的bookDaoImpl对象非单例-->
<bean id="bookDao" name="dao" class="com.linxuan.dao.impl.BookDaoImpl" scope="prototype"/>
```

bean在容器中是单例的，那么会不会产生线程安全问题？
* 如果对象是有状态对象，即该对象有成员变量可以用来存储数据的，因为所有请求线程共用一个bean对象，所以会存在线程安全问题。也因此这种对象不适合交给IOC容器进行管理，例如实体类对象。
* 如果对象是无状态对象，即该对象没有成员变量没有进行数据存储的，因为方法中的局部变量在方法调用完成后会被销毁，所以不会存在线程安全问题。这种对象适合交给IOC进行管理，例如表现层对象、业务层对象、数据层对象和工具对象。

## 2.2 bean实例化

接下来研究一下实例化bean的三种方式：`构造方法`、`静态工厂`和`实例工厂`。

### 2.2.1 构造方法实例化

Bean就是对象。对象在new的时候会使用构造方法完成，那创建Bean、实例化Bean也是使用构造方法完成的。

默认Spring就是用这种无参的构造方法来实例化Bean对象的。

1. 在之前的环境下编写运行程序 

   ```java
   public class App {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           BookDao bookDao = (BookDao) ctx.getBean("bookDao");
           bookDao.save();
       }
   }
   ```

2. 类中提供构造函数测试。在`BookDaoImpl`类中添加一个无参构造函数，并打印一句话，方便观察结果。

   ```java
   public class BookDaoImpl implements BookDao {
       public BookDaoImpl() {
           System.out.println("book dao constructor is running ....");
       }
       public void save() {
           System.out.println("book dao save ...");
       }
   }
   ```

   运行程序，控制台有打印构造函数中的输出，说明Spring容器在创建对象的时候也走的是构造函数

   ```java
   // book dao constructor is running ....
   // book dao save ...
   ```

3. 将构造函数改成private测试

   ```java
   public class BookDaoImpl implements BookDao {
       private BookDaoImpl() {
           System.out.println("book dao constructor is running ....");
       }
       public void save() {
           System.out.println("book dao save ...");
       }
   }
   ```

   运行程序，能执行成功，说明内部走的依然是构造函数，能访问到类中的私有构造方法，显而易见Spring底层用的是反射

   ```java
   // book dao constructor is running ....
   // book dao save ...
   ```

4. 构造函数中添加一个参数测试

   ```java
   public class BookDaoImpl implements BookDao {
       // 添加参数之后变成了有参构造方法，这样会默认消除掉JVM给我们带的无参构造方法
       private BookDaoImpl(int i) {
           System.out.println("book dao constructor is running ....");
       }
       public void save() {
           System.out.println("book dao save ...");
       }
   }
   ```

   运行程序，程序会报错，说明Spring底层使用的是类的无参构造方法。

   ```apl
   # 方法没有找到异常，找不到该类的无参构造方法
   Caused by: java.lang.NoSuchMethodException: com.linxuan.dao.impl.BookDaoImpl.<init>()
   	at java.base/java.lang.Class.getConstructor0(Class.java:3517)
   	at java.base/java.lang.Class.getDeclaredConstructor(Class.java:2691)
   	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:78)
   	... 14 more
   ```

每一个类默认都会提供一个无参构造函数，所以在使用这种方式实例化Bean对象的的时候，我们什么也不需要做。但是以后以后我们再对它的构造方法重写的时候，我们需要将空参的构造方法也重写一下，防止消除掉无参构造方法。

### 2.2.2 静态工厂实例化

接下来研究Spring中的第二种bean的创建方式静态工厂实例化：

1. 准备环境

   ```java
   public interface OrderDao {
       public void save();
   }
   ```
   
   ```java
   public class OrderDaoImpl implements OrderDao {
       public void save() {
           System.out.println("order dao save ...");
       }
   }
   ```
   
   ```java
   // 使用静态工厂创建对象
   public class OrderDaoFactory {
       public static OrderDao getOrderDao(){
           return new OrderDaoImpl();
       }
   }
   ```
   
2. 在spring的配置文件`applicationContex.xml`中添加以下内容：

   ```xml
   <!-- class属性表示工厂类的全类名，factory-mehod属性表示具体工厂类中创建对象的方法名 -->
   <bean id="orderDao" class="com.linxuan.factory.OrderDaoFactory" factory-method="getOrderDao"/>
   ```

3. 在`AppForInstanceOrder`运行类，使用从IOC容器中获取bean的方法进行运行测试

   ```java
   public class AppForInstanceOrder {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           OrderDao orderDao = (OrderDao) ctx.getBean("orderDao");
           orderDao.save();
       }
   }
   // order dao save ...
   ```

使用这种方式在工厂类中也是直接new对象的，和自己直接new没什么太大的区别。但是使用这种方式我们除了new对象还可以做其他的一些业务操作，这些操作必不可少。

```java
public class OrderDaoFactory {
    public static OrderDao getOrderDao(){
        // 模拟必要的业务操作
        System.out.println("factory setup....");
        return new OrderDaoImpl();
    }
}

// factory setup....
// order dao save ...
```

这种方式一般是用来兼容早期的一些老系统，所以了解为主。

### 2.2.3 实例工厂/FactoryBean

接下来继续来研究Spring的第三种bean的创建方式实例工厂实例化：

1. 准备环境

   ```java
   public interface UserDao {
       public void save();
   }
   ```
   
   ```java
   public class UserDaoImpl implements UserDao {
       public void save() {
           System.out.println("user dao save ...");
       }
   }
   ```
   
   ```java
   // 创建工厂类UserDaoFactory并提供一个普通方法。此处创建的是普通方法，静态工厂的工厂类的方法是静态方法
   public class UserDaoFactory {
       public UserDao getUserDao(){
           return new UserDaoImpl();
       }
   }
   ```
   
2. 在spring的配置文件中添加以下内容

   ```xml
   <bean id="userDaoFactory" class="com.linxuan.factory.UserDaoFactory"/>
   <!-- factory-bean表示工厂的实例对象，factory-method表示工厂对象中的具体创建对象的方法名 -->
   <bean id="userDao" factory-bean="userDaoFactory" factory-method="getUserDao" />
   ```

   实例化工厂运行的顺序是：创建实例化工厂对象（对应的是第一行配置），然后调用对象中的方法来创建bean（对应的是第二行配置）。

3. 在`AppForInstanceUser`运行类，使用从IOC容器中获取bean的方法进行运行测试

   ```java
   public class AppForInstanceUser {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           UserDao userDao = (UserDao) ctx.getBean("userDao");
           userDao.save();
       }
   }
   // user dao save ..
   ```

实例工厂实例化的方式就已经介绍完了，配置的过程还是比较复杂，所以Spring为了简化这种配置方式就提供了一种叫`FactoryBean`的方式来简化开发，这种方式重点掌握。

**FactoryBean的使用**

1. 创建一个`UserDaoFactoryBean`的类，实现`FactoryBean`接口，重写接口的方法

   ```java
   public class UserDaoFactoryBean implements FactoryBean<UserDao> {
       // 代替原始实例工厂中创建对象的方法
       @Override
       public UserDao getObject() throws Exception {
           return new UserDaoImpl();
       }
   
       // 返回所创建类的Class对象
       @Override
       public Class<?> getObjectType() {
           return UserDao.class;
       }
   }
   ```

2. 在Spring的配置文件中进行配置

   ```xml
   <bean id="userDao" class="com.linxuan.factory.UserDaoFactoryBean"/>
   ```

3. `AppForInstanceUser`运行类不用做任何修改，直接运行

   ```java
   // user dao save ...
   ```

这种方式在Spring去整合其他框架的时候会被用到，所以这种方式需要理解掌握。

查看源码会发现，FactoryBean接口其实有三个方法，分别是：

```java
public interface FactoryBean<T> {
	String OBJECT_TYPE_ATTRIBUTE = "factoryBeanObjectType";

    /**
     * 被重写后，在方法中进行对象的创建并返回
     */
	@Nullable
	T getObject() throws Exception;

    /**
     * 被重写后，主要返回的是被创建类的Class对象。
     */
	@Nullable
	Class<?> getObjectType();
    
    /**
     * 没有被重写，因为它已经给了默认值true。作用是设置对象是否为单例，一般情况下默认即可
     */
	default boolean isSingleton() {
		return true;
	}
}
```

## 2.3 bean的生命周期

生命周期：从创建到消亡的完整过程，例如人从出生到死亡的整个过程就是一个生命周期。bean生命周期是bean对象从创建到销毁的整体过程。

对于bean的生命周期控制在bean的整个生命周期中所处的位置如下：

* 初始化容器：创建对象(内存分配) -> 执行构造方法 -> 执行属性注入(set操作) -> 执行bean 初始化方法 -> 使用bean 执行业务操作。
  
* 关闭/销毁容器：执行bean销毁方法

现在我们面临的问题是如何在bean的创建之后和销毁之前把我们需要添加的内容添加进去。

### 2.3.1 使用Bean标签控制

1. 添加初始化和销毁方法。针对这两个阶段，我们在`BooDaoImpl`类中分别添加两个方法。

   ```java
   public class BookDaoImpl implements BookDao {
       public void save() {
           System.out.println("book dao save ...");
       }
       
       // 表示bean初始化对应的操作
       public void init(){
           System.out.println("init...");
       }
       
       // 表示bean销毁前对应的操作
       public void destroy(){
           System.out.println("destory...");
       }
   }
   ```

2. 配置生命周期。在配置文件添加配置，如下：

   ```xml
   <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl" init-method="init" destroy-method="destroy"/>
   <bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
       <property name="bookDao" ref="bookDao"/>
   </bean>
   ```
   
3. 运行程序。运行主方法：

   ```java
   public static void main(String[] args) {
       ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
   
       BookDao bookDao = (BookDao) ctx.getBean("bookDao");
       bookDao.save();
   }
   
   // init...
   // book dao save ...
   ```

`init`方法执行了，但是`destroy`方法却未执行。这是因为Spring的IOC容器是运行在JVM中。运行main方法后，JVM启动，Spring加载配置文件生成IOC容器，从容器获取bean对象，然后调方法执行。main方法执行完后，JVM退出，这个时候IOC容器中的bean还没有来得及销毁就已经结束了。所以没有调用对应的destroy方法。

对此我们的解决方法有两种：close关闭容器和注册钩子关闭容器。close是在调用的时候关闭，注册钩子关闭容器是在JVM退出前调用关闭。

**close关闭容器**

将main方法中的`ApplicationContext`更换成`ClassPathXmlApplicationContext`，然后调用`close()`方法。这时运行程序，就能够执行destroy方法的内容。

```java
public static void main(String[] args) {
    // ApplicationContext中没有close方法，更换为ClassPathXmlApplicationContext，不能用多态
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

    BookDao bookDao = (BookDao) ctx.getBean("bookDao");
    bookDao.save();
    ctx.close();
}
```

**注册钩子关闭容器**

可以在容器未关闭之前，提前设置好回调函数，让JVM在退出之前回调此函数来关闭容器。

```java
public static void main(String[] args) {
    // ApplicationContext中没有registerShutdownHook方法，更换为ClassPathXmlApplicationContext
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

    BookDao bookDao = (BookDao) ctx.getBean("bookDao");
    bookDao.save();
    // 调用ctx的registerShutdownHook()方法，让JVM在退出之前回调此函数来关闭容器。可以不放在最后
    ctx.registerShutdownHook();
}
```


### 2.3.2 接口完成声明周期控制

Spring提供了两个接口`InitializingBean`和`DisposableBean`来完成生命周期的控制，好处是可以不用再进行配置`init-method`和`destroy-method`，只需要实现接口中的`afterPropertiesSet`和`destroy`方法即可。

```java
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ...");
    }
    
    public void init(){
        System.out.println("init...");
    }
    
    public void destroy(){
        System.out.println("destory...");
    }
}
```

```java
public class BookServiceImpl implements BookService, InitializingBean, DisposableBean {
    private BookDao bookDao;
    
    public void setBookDao(BookDao bookDao) {
        // 这里添加一个输出，用来判断和afterPropertiesSet方法先后执行顺序
        System.out.println("set .....");
        this.bookDao = bookDao;
    }
    
    public void save() {
        System.out.println("book service save ...");
        bookDao.save(); 
    }
    
    public void destroy() throws Exception {
        System.out.println("service destroy");
    }
    
    // afterPropertiesSet方法，翻译过来为属性设置之后。也就是在setBookDao之后执行
    public void afterPropertiesSet() throws Exception {
        System.out.println("service init");
    }
}
```

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl" init-method="init" destroy-method="destroy"/>
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
    <property name="bookDao" ref="bookDao"/>
</bean>
```

```java
public static void main(String[] args) {
    // ApplicationContext中没有close方法，更换为ClassPathXmlApplicationContext，不能用多态
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

    BookService bookService = ctx.getBean(BookService.class);
    bookService.save();
    ctx.close();
}
// init...
// set .....
// service init
// book service save ...
// book dao save...
// service destroy
// destory...
```

# 第三章 DI依赖注入

**DI(Dependency Injection)依赖注入**：在容器中建立bean与bean之间的依赖关系的整个过程，称为依赖注入。业务层要用数据层的类对象，以前是自己`new`的，现在自己不new了，靠`别人[外部其实指的就是IOC容器]`来给注入进来。这种思想就是依赖注入。

向一个类中传递数据的方式有两种：普通方法(set方法)、构造方法。Spring注入方式和这两种方式一样：setter注入、构造器注入。而根据注入数据的不同，每种传递方式又分为注入引用类型和简单类型(基本数据类型与String)。

参数注入方式的选择：

- 强制依赖使用构造器进行，使用setter注入有概率不进行注入导致null对象出现。强制依赖指对象在创建的过程中必须要注入指定的参数
- 可选依赖使用setter注入进行，灵活性强。可选依赖指对象在创建过程中注入的参数可有可无。
- Spring框架倡导使用构造器，第三方框架内部大多数采用构造器注入的形式进行数据初始化，相对严谨。
- 如果有必要可以两者同时使用，使用构造器注入完成强制依赖的注入，使用setter注入完成可选依赖的注入。
- 实际开发过程中还要根据实际情况分析，如果受控对象没有提供setter方法就必须使用构造器注入。
- 自己开发的模块推荐使用setter注入。

环境介绍：

```java
public interface BookDao {
    public void save();
}
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ...");
    }
}
```

```java
public interface UserDao {
    public void save();
}
public class UserDaoImpl implements UserDao {
    public void save() {
        System.out.println("user dao save ...");
    }
}
```

```java
public interface BookService {
    public void save();
}

public class BookServiceImpl implements BookService{
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

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
    <property name="bookDao" ref="bookDao"/>
</bean>
```

```java
public static void main( String[] args ) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    BookService bookService = (BookService) ctx.getBean("bookService");
    bookService.save();
}
// book service save ...
// book dao save...
```

## 3.1 setter注入property

* 注入引用数据类型：`<property name="" ref=""/>`
* 注入简单数据类型：`<property name="" value=""/>`

对于setter方式注入引用类型的方式之前已经学习过：

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
    <!--在bean中配置引用类型属性 配置中使用property标签ref属性注入引用类型对象-->
    <property name="bookDao" ref="bookDao"/>
</bean>
```

```java
public class BookServiceImpl implements BookService{
    private BookDao bookDao;

    // 提供可访问的set方法
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}
```

接下来讲述一下如何注入简单数据类型：

```java
// 给BookDaoImpl注入一些简单数据类型的数据
public class BookDaoImpl implements BookDao {

    // 声明对应的简单数据类型的属性，并提供对应的setter方法
    private String name;
    private int age;
    
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void save() {
        System.out.println("book dao save..." + name + " " + age);
    }
}
```

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl">
    <!-- name表示配置哪一个具体的属性，value表示配置简单数据类型 -->
    <property name="name" value="林炫"/>
    <!--即使是数字类型也要使用双引号引起来，会自动转换的-->
    <property name="age" value="21"/>
</bean>
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.save();
}
```

## 3.2  构造器注入constructor-arg

构造器注入和setter注入不同的是将property标签换成了constructor-arg标签：

* 注入引用数据类型：`<constructor-arg name="" ref=""/>`
* 注入简单数据类型：`<constructor-arg name="" value=""/>`

**注入引用数据类型**

将`BookServiceImpl`类中的`bookDao`修改成使用构造器的方式注入。

```java
public class BookDaoImpl implements BookDao {

    public void save() {
        System.out.println("book dao save...");
    }
}
```

```java
public class BookServiceImpl implements BookService{
    private BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}
```

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
    <!-- name表示构造函数中方法形参的参数名。ref属性参照哪一个bean注入。-->
    <constructor-arg name="bookDao" ref="bookDao"/>
</bean>
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    BookService bookService = ctx.getBean(BookService.class);
    bookService.save();
}
// book service save ...
// book dao save...
```

**注入简单数据类型**

同理，只是在配置的时候需要多操点心。和Setter注入没啥差别，将`property`换成了`constructor-arg`。

## 3.3 注入问题type/index

> 元素 'property' 中不允许出现属性 'type'

上面已经介绍了setter注入和构造函数注入的基本使用，但是会存在一些问题：

```java
public class BookServiceImpl implements BookService{
    private BookDao bookDao;

    // 这里的形参和配置文件中name="bookDao"出现紧耦合问题，如果这里参数进行修改，那么配置文件也要修改
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}
```

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
    <!-- name="bookDao"这里出现了紧耦合，假如上面的参数修改了，那么这里也必须修改 -->
    <constructor-arg name="bookDao" ref="bookDao"/>
</bean>
```

当构造函数中方法的参数名发生变化后，配置文件中的name属性也需要跟着变，这两块存在紧耦合。这里有两种解决方式：按照类型注入、按照索引注入。

**按照类型注入**

删除name属性，添加type属性，按照类型注入

```xml
<!--构造器注入方式-->
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
    <!-- type表示注入数据的类型。ref属性参照哪一个bean注入。-->
    <constructor-arg type="BookDao" ref="bookDao"/>
</bean>
```

这种方式可以解决构造函数形参名发生变化带来的耦合问题。但是如果构造方法参数中有类型相同的参数，这种方式就不太好实现了

**按照索引注入**

删除type属性，添加index属性，按照索引下标注入，下标从0开始

```xml
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
    <!-- index表示按照索引下标注入。ref属性参照哪一个bean注入。-->
    <constructor-arg index="0" ref="bookDao"/>
</bean>
```

这种方式可以解决参数类型重复问题，但是如果构造方法参数顺序发生变化后，这种方式又带来了耦合问题。

## 3.4 自动装配autowire

Spring的注入中的配置文件配置起来很麻烦，所以Spring为我们弄了一种简单的方式：自动装配。IOC容器根据bean所依赖的资源在容器中自动查找并注入到bean中的过程称为自动装配。用autowire属性来完成。

开启自动装配需要将`<property>`标签删除，然后在`<bean>`标签中添加`autowire="byType|byName"`属性即可。

**按照类型自动装配**

按照类型自动装配：在`<bean>`标签中添加`autowire="byType"`属性。需要注入属性的类中对应属性的setter方法不能省略，被注入的对象必须要被Spring的IOC容器管理。

按照类型自动注入在Spring的IOC容器中如果找到多个该对象，会报`NoUniqueBeanDefinitionException`

```java
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save...");
    }
}
```

```java
public class BookServiceImpl implements BookService{
    private BookDao bookDao;

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }

    // setter方法不可或缺
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
}
```

```xml
<!-- 通过类型找到该Bean，然后注入进去。所以写不写ID都可以 -->
<bean class="com.linxuan.dao.impl.BookDaoImpl"/>
<!-- autowire属性：开启自动装配，通常使用按类型装配 -->
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl" autowire="byType"/>
```

**按照名称自动装配**

一个类型在IOC中有多个对象，还想要注入成功，这个时候就需要按照名称注入，`autowire="byName"`

```xml
<bean class="com.linxuan.dao.impl.BookDaoImpl"/>
<bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl" autowire="byName"/>
```

按照名称注入中的名称指的是对应setter方法去掉set后首字母小写的属性名。

最后对于依赖注入，需要注意一些其他的配置特征：

1. 自动装配用于引用类型依赖注入，不能对简单类型进行操作
2. 使用按类型装配时(byType)必须保障容器中相同类型的bean唯一，推荐使用
3. 使用按名称装配时(byName)必须保障容器中具有指定名称的bean，因变量名与配置耦合，不推荐使用
4. 自动装配优先级低于setter注入与构造器注入，同时出现时自动装配配置失效

## 3.5 集合注入

前面已经完成引入数据类型和简单数据类型的注入，但是还有一种数据类型：集合。集合中既可以装简单数据类型也可以装引用数据类型。Java中常见的集合有`数组、List、Set、Map、Properties`。

环境介绍：

```xml
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
```

```java
@Data
public class BookDaoImpl implements BookDao {
    
    private int[] array;
    private List<String> list;
    private Set<String> set;
    private Map<String, String> map;
    private Properties properties;

    public void save() {
        System.out.println("book dao save ...");
        System.out.println("遍历数组: " + Arrays.toString(array));
        System.out.println("遍历List: " + list);
        System.out.println("遍历Set: " + set);
        System.out.println("遍历Map: " + map);
        System.out.println("遍历Properties: " + properties);
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
public static void main( String[] args ) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    BookDao bookDao = (BookDao) ctx.getBean("bookDao");
    bookDao.save();
}
// book dao save ...
// 遍历数组: null
// 遍历List: null
// 遍历Set: null
// 遍历Map: null
// 遍历Properties: null
```

接下来，完成集合注入。下面的所以配置方式，都是在bookDao的bean标签中使用`<property>`进行注入。

* property标签表示setter方式注入，使用构造器方式注入`constructor-arg`标签内部也可以写`<array>`、`<list>`、`<set>`、`<map>`、`<props>`标签
* List的底层也是通过数组实现的，所以`<list>`和`<array>`标签是可以混用
* 集合中要添加引用类型，只需要把`<value>`标签改成`<ref>`标签，这种方式用的比较少

**注入数组类型数据**

```xml
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl">
    <property name="array">
        <array>
            <value>100</value>
            <value>200</value>
            <value>300</value>
        </array>
    </property>
</bean>
```

**注入List类型数据**

```xml
<property name="list">
    <list>
        <value>itcast</value>
        <value>itheima</value>
        <value>boxuegu</value>
        <value>chuanzhihui</value>
    </list>
</property>
```

**注入Set类型数据**

```xml
<property name="set">
    <set>
        <value>itcast</value>
        <value>itheima</value>
        <value>boxuegu</value>
        <value>boxuegu</value>
    </set>
</property>
```

**注入Map类型数据**

```xml
<property name="map">
    <map>
        <entry key="country" value="china"/>
        <entry key="province" value="henan"/>
        <entry key="city" value="kaifeng"/>
    </map>
</property>
```

**注入Properties类型数据**

```xml
<property name="properties">
    <props>
        <prop key="country">china</prop>
        <prop key="province">henan</prop>
        <prop key="city">kaifeng</prop>
    </props>
</property>
```

# 第四章 IOC/DI管理第三方bean

前面所讲的知识点都是基于我们自己写的类，以后我们会用到很多第三方的bean，下面使用前面提到过的数据源`Druid(德鲁伊)`和`C3P0`来学习下管理第三方jar包中的类。

## 4.1 实现Druid管理

来对Druid数据源进行配置管理，使用Spring的IOC容器来管理Druid连接池对象。

```xml
<!--pom.xml导入druid依赖-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.0.9</version>
</dependency>
<!-- 导入驱动，druid初始化不会加载驱动，所以这个项目中不导入驱动也不会出错 -->
<!-- 但是当调用DruidDataSource的getConnection()方法获取连接的时候，会报找不到驱动类的错误。 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <!--管理DruidDataSource对象-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <!-- driverClassName驱动，url连接地址，username用户名，password连接密码 -->
        <!-- MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver -->
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/linxuan"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>
</beans>
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    DataSource dataSource = (DataSource) ctx.getBean("dataSource");
    System.out.println(dataSource);
}
/* 打印下面结果，说明第三方bean对象已经被spring的IOC容器进行管理
    {
        CreateTime:"2022-04-25 16:14:20",
        ActiveCount:0,
        PoolingCount:0,
        CreateCount:0,
        DestroyCount:0,
        CloseCount:0,
        ConnectCount:0,
        Connections:[
        ]
    }
*/
```


## 4.2 实现C3P0管理

这次我们来管理C3P0数据源，使用Spring的IOC容器来管理C3P0连接池对象。

```xml
<dependency>
    <groupId>c3p0</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.1.2</version>
</dependency>
<!-- C3P0c初始化的时候会加载驱动，所以需要导入驱动jar包。否则会报ClassNotFoundException-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>
```

```xml
<!--
    * ComboPooledDataSource的属性是通过setter方式进行注入
    * 想注入属性就需要在ComboPooledDataSource类或其上层类中有提供属性对应的setter方法
    * C3P0的四个属性和Druid的四个属性是不一样的
-->
<!--管理C3P0对象-->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="com.mysql.cj.jdbc.Driver"/>
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/linxuan"/>
    <property name="user" value="root"/>
    <property name="password" value="root"/>
    <property name="maxPoolSize" value="1000"/>
</bean>
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    DataSource dataSource = (DataSource) ctx.getBean("dataSource");
    System.out.println(dataSource);
}
// 信息: Initializing c3p0 pool...
```

## 4.3 加载properties文件

上节中我们已经完成两个数据源`druid`和`C3P0`的配置，但是其中包含了一些问题：这两个数据源中都使用到了一些固定的常量如数据库连接四要素，把这些值写在Spring的配置文件中不利于后期维护。

所以需要将这些值提取到一个外部的properties配置文件中，那么Spring框架如何从配置文件中读取属性值来配置就是接下来要解决的问题。

环境介绍：

```xml
<!--pom.xml导入druid依赖-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.0.9</version>
</dependency>
<!-- 导入驱动，druid初始化不会加载驱动，所以这个项目中不导入驱动也不会出错 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>
```

```java
public interface BookDao {
    public void save();
}
```

```java
public class BookDaoImpl implements BookDao {

    private String driverClassName;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Override
    public void save() {
        System.out.println("bookDao..." + ", driverClassName = " + driverClassName);
    }
}
```

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan
jdbc.username=root
jdbc.password=root
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--这里和之前的不一样，新增加了context命名空间-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
                           http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context.xsd
                           ">

    <!-- 开启context命名空间，使用context命名空间加载properties文件 -->
    <context:property-placeholder location="jdbc.properties"/>

    <!-- 使用属性占位符${}读取properties文件中的属性 -->
    <bean class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
    <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl">
        <property name="driverClassName" value="${jdbc.driver}"/>
    </bean>
</beans>
```

```java
public static void main(String[] args) throws Exception{
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    BookDao bookDao = (BookDao) ctx.getBean("bookDao");
    bookDao.save();
}
// bookDao..., driverClassName = com.mysql.cj.jdbc.Driver
```

## 4.4 加载文件注意事项

**键值对的key为`username`引发的问题**

```java
public class BookDaoImpl implements BookDao {
	// 将这个属性修改为username
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void save() {
        System.out.println("bookDao..." + ", driverClassName = " + username);
    }
}
```

```properties
# resources目录下面创建一个jdbc2.properties文件，添加对应键值对
username=root666
```

```xml
<!-- 注入username属性 -->
<context：property-placeholder location="jdbc.properties"/>
<bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl">
    <property name="username" value="${username}"/>
</bean>
```

```java
public static void main(String[] args) throws Exception{
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    BookDao bookDao = (BookDao) ctx.getBean("bookDao");
    bookDao.save();
}
// bookDao..., driverClassName = 林轩
```

在控制台打印的却不是`root666`，而是自己电脑的用户名。出现问题的原因是`<context：property-placeholder/>`标签会加载系统的环境变量，而且环境变量的值会被优先加载。

可以在`<context:property-placeholder>`标签内部设置`system-properties-mode`属性为NEVER，表示不加载系统属性，就可以解决上述问题。

```xml
<context:property-placeholder location="jdbc.properties" system-properties-mode="NEVER"/>
```

当然还有一个解决方案就是避免使用`username`作为属性的`key`。

**多个properties配置文件需要被加载**

向上面我们一共创建了两个properties文件，`jdbc.properties`，`jdbc2.properties`。那么想要同时加载他们，就需要在配置文件里面进行修改了：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
                           http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context.xsd
                           ">

    <!--方式一 可以实现，如果配置文件多的话，每个都需要配置-->
    <context:property-placeholder location="jdbc.properties, jdbc2.properties"/>

    <!--方式二 *.properties代表所有以properties结尾的文件都会被加载，可以解决方式一的问题，但是不标准-->
    <context:property-placeholder location="*.properties"/>

    <!--方式三 标准的写法，classpath：代表的是从根路径下开始查找，但是只能查询当前项目的根路径-->
    <context:property-placeholder location="classpath:*.properties"/>

    <!--方式四 不仅可以加载当前项目还可以加载当前项目所依赖的所有项目的根路径下的properties配置文件-->
    <context:property-placeholder location="classpath*:*.properties"/>
</beans>	
```
