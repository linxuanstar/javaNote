![](..\图片\3-02【Spring】\0-1.png)

# 第一章 Spring基础

<!--full-stack 全栈-->

Spring是分层的 Java SE/EE应用 full-stack 轻量级开源框架，以 `IoC(Inverse Of Control：控制反转)`和`AOP(Aspect Oriented Programming：面向切面编程)`为内核。提供了展现层 `SpringMVC` 和持久层 `Spring JDBCTemplate` 以及业务层事务管理等众多的企业级应用技术，还能整合开源世界众多著名的第三方框架和类库，逐渐成为使用最多的Java EE 企业应用开源框架。

Spring的优势如下：

- **方便解耦，简化开发**：通过 Spring 提供的 IoC容器，可以将对象间的依赖关系交由 Spring 进行控制，避免硬编码所造成的过度耦合。用户也不必再为单例模式类、属性文件解析等这些很底层的需求编写代码，可以更专注于上层的应用。
- **AOP 编程的支持**：通过 Spring的 AOP 功能，方便进行面向切面编程，许多不容易用传统 OOP 实现的功能可以通过 AOP 轻松实现。
- **声明式事务的支持**：可以将我们从单调烦闷的事务管理代码中解脱出来，通过声明式方式灵活的进行事务管理，提高开发效率和质量。
- **方便程序的测试**：可以用非容器依赖的编程方式进行几乎所有的测试工作，测试不再是昂贵的操作，而是随手可做的事情。
- **方便集成各种优秀框架**：Spring对各种优秀框架(Struts、Hibernate、Hessian、Quartz等)的支持。
- **降低 JavaEE API 的使用难度**：Spring对 JavaEE API(如 JDBC、JavaMail、远程调用等)进行了薄薄的封装层，使这些 API 的使用难度大为降低。
- **Java 源码是经典学习范例**：Spring的源代码设计精妙、结构清晰、匠心独用，处处体现着大师对Java 设计模式灵活运用以及对 Java技术的高深造诣。它的源代码无意是 Java 技术的最佳实践的范例。

我们可以看到Spring框架主要的优势是在`简化开发`和`框架整合`上，我们就是学的这两方面的的主要内容。而关于简化开发，Spring框架中提供了两个核心技术：IOC和AOP。


综上所述，对于Spring的学习，主要学习四块内容：

* IOC、整合Mybatis(IOC的具体应用)、AOP、声明式事务(AOP的具体应用)

## 1.1 Spring介绍

官网：`https://spring.io`，从官网我们可以大概了解到：

* Spring能做什么：用以开发web、微服务以及分布式系统等，光这三块就已经占了JavaEE开发的九成多。
* Spring并不是单一的一个技术，而是一个大家族，可以从官网的`Projects`中查看其包含的所有技术。

Spring发展到今天已经形成了一种开发的生态圈，Spring提供了若干个项目，每个项目用于完成特定的功能。

* Spring已形成了完整的生态圈，也就是说我们可以完全使用Spring技术完成整个项目的构建、设计与开发。

* Spring有若干个项目，可以根据需要自行选择，把这些个项目组合起来，起了一个名称叫全家桶，如下图所示

  ![](..\图片\3-02【Spring】\1-0.png)

这些技术并不是所有的都需要学习，额外需要重点关注`Spring Framework`、`SpringBoot`和`SpringCloud`：

![](..\图片\3-02【Spring】\1-1.png)

* `Spring Framework`：Spring框架，是Spring中最早最核心的技术，也是所有其他技术的基础。
* `SpringBoot`：Spring是来简化开发，而SpringBoot是来帮助Spring在简化的基础上能更快速进行开发。
* `SpringCloud`：这个是用来做分布式之微服务架构的相关开发。

除了上面的这三个技术外，还有很多其他的技术，也比较流行，如`SpringData`，`SpringSecurity`等，这些都可以被应用在我们的项目中。我们今天所学习的Spring其实指的是`Spring Framework`。

## 1.2 Spring系统架构

前面我们说spring指的是`Spring Framework`，下面从系统架构图来进行说明一下：

* `Spring Framework`是Spring生态圈中最基础的项目，是其他项目的根基。

* `Spring Framework`的发展也经历了很多版本的变更，每个版本都有相应的调整

  ![](..\图片\3-02【Spring】\1-2.png)

* `Spring Framework`的5版本目前没有最新的架构图，而最新的是4版本，所以接下来主要研究的是4的架构图

  ![](..\图片\3-02【Spring】\1-3.png)

  1. 核心层

     - `Core Container`：核心容器，这个模块是Spring最核心的模块，其他的都需要依赖该模块

  2. AOP层

     * AOP：面向切面编程，它依赖核心层容器，目的是在不改变原有代码的前提下对其进行功能增强
  
     * `Aspects`：AOP是思想，Aspects是对AOP思想的具体实现
  
  3. 数据层
  
     * `Data Access`：数据访问，Spring全家桶中有对数据访问的具体实现技术
  
     * `Data Integration`：数据集成，Spring支持整合其他的数据层解决方案，比如Mybatis
  
     * `Transactions`：事务，Spring中事务管理是Spring AOP的一个具体实现。
  
  4. Web层
  
     * 这一层的内容将在SpringMVC框架具体学习
  
  5. Test层
  
     * Spring主要整合了Junit来完成单元测试和集成测试

## 1.3 Spring核心概念

在Spring核心概念这部分内容中主要包含`IOC/DI`、`IOC容器`和`Bean`，那么问题就来了，这些都是什么呢？

要想解答这个问题，就需要先分析下目前咱们代码在编写过程中遇到的问题：

![1629723232339](..\图片\3-02【Spring】\1-5.png)

业务层需要调用数据层的方法，就需要在业务层`new`数据层的对象。如果数据层的实现类发生变化，那么业务层的代码也需要跟着改变，发生变更后，都需要进行编译打包和重部署。所以，现在代码在编写的过程中存在的问题是：耦合度偏高。

![1629724206002](..\图片\3-02【Spring】\1-6.png)

我们就想，如果能把框中的内容给去掉，不就可以降低依赖了么，但是又会引入新的问题，去掉以后程序能运行么？答案肯定是不行，因为bookDao没有赋值，那么引用为Null，强行运行就会出现空指针异常。

所以现在的问题就是，业务层不想new对象，运行的时候又需要这个对象，该咋办呢？针对这个问题，Spring就提出了一个解决方案：`使用对象时，在程序中不要主动使用new产生对象，转换为由外部提供对象`，这种实现思就是Spring的一个核心概念。

- **IOC(`Inversion of Control`)控制反转**：使用对象时，由主动new产生对象转换为由外部提供对象，此过程中对象创建控制权由程序转移到外部，此思想称为控制反转。
- **IOC容器**：Spring技术对IOC思想进行了实现，Spring提供了一个容器，称为IOC容器，用来充当IOC思想中的"外部"。IOC思想中的`别人[外部]`指的就是Spring的IOC容器。
- **Bean**：IOC容器负责对象的创建、初始化等一系列工作，其中包含了数据层和业务层的类对象，被创建或被管理的对象在IOC容器中统称为Bean。IOC容器中放的就是一个个的Bean对象。

那么问题来了，当IOC容器中创建好service和dao对象后，程序能正确执行么？当然是不行的，因为service运行需要依赖dao对象，IOC容器中虽然有service和dao对象，但是service对象和dao对象没有任何关系。需要把dao对象交给service，也就是说要绑定service和dao对象之间的关系。像这种在容器中建立对象与对象之间的绑定关系就要用到DI。

**DI(Dependency Injection)依赖注入**：在容器中建立bean与bean之间的依赖关系的整个过程，称为依赖注入。业务层要用数据层的类对象，以前是自己`new`的，现在自己不new了，靠`别人[外部其实指的就是IOC容器]`来给注入进来。这种思想就是依赖注入。

![1629735078619](..\图片\3-02【Spring】\1-7.png)

介绍完Spring的IOC和DI的概念后，我们会发现这两个概念的最终目标就是：充分解耦。

- 具体实现靠：使用IOC容器管理bean(IOC)、在IOC容器内将有依赖关系的bean进行关系绑定(DI)。

- 最终结果为：使用对象时不仅可以直接从IOC容器中获取，并且获取到的bean已经绑定了所有的依赖关系。

## 1.4 快速入门案例

介绍完Spring的核心概念后，接下来我们得思考一个问题就是，Spring到底是如何来实现IOC和DI的，那接下来就通过一些简单的入门案例，来演示下具体实现过程：

### 1.4.1 IOC入门案例

1. 创建Maven项目。我们创建了一个Maven项目`ssm_spring01`，然后在该项目下面创建了一个模块`spring01`。

   注意：创建的时候需要联网

2. 添加Spring的依赖jar包。在`pom.xml`添加一些标签，导入坐标：

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-context</artifactId>
           <version>5.2.10.RELEASE</version>
       </dependency>
   </dependencies>
   ```

   会变成下面这样：

   ```xml
   <？xml version="1.0" encoding="UTF-8"？>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <parent>
           <artifactId>ssm_spring01</artifactId>
           <groupId>com.linxuan</groupId>
           <version>1.0-SNAPSHOT</version>
       </parent>
       <modelVersion>4.0.0</modelVersion>
       <artifactId>spring01</artifactId>
       <properties>
           <maven.compiler.source>16</maven.compiler.source>
           <maven.compiler.target>16</maven.compiler.target>
       </properties>
       <dependencies>
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-context</artifactId>
               <version>5.2.10.RELEASE</version>
           </dependency>
       </dependencies>
   </project>
   ```

3. 添加案例中需要的类。创建`BookService`，`BookServiceImpl`，`BookDao`和`BookDaoImpl`四个类

   ```java
   public interface BookDao {
       public void save();
   }
   public class BookDaoImpl implements BookDao {
       public void save() {
           System.out.println("book dao save ...");
       }
   }
   public interface BookService {
       public void save();
   }
   public class BookServiceImpl implements BookService {
       private BookDao bookDao = new BookDaoImpl();
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
       }
   }
   ```

4. 添加spring配置文件。`new`下面的`resources`下添加spring配置文件`applicationContext.xml`，并完成bean的配置

5. 在配置文件中完成bean的配置

   ```xml
   <？xml version="1.0" encoding="UTF-8"？>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <!--
   		bean标签标示配置bean
       	id属性标示给bean起名字
       	class属性表示给bean定义类型
   	-->
       <bean id="bookDao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl"/>
       <bean id="bookService" class="cn.com.linxuan.demo01.service.impl.BookServiceImpl"/>
   </beans>
   ```

   > 注意事项：bean定义时id属性在同一个上下文中(配置文件)不能重复

6. 获取IOC容器。使用Spring提供的接口完成IOC容器的创建，创建App类，编写main方法

   ```java
   public class App {
       public static void main(String[] args) {
           // 获取IOC容器
           // 加载配置文件得到上下文对象，也就是容器对象
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
       }
   }
   ```

7. 从容器中获取对象进行方法调用

   ```java
   public class App {
       public static void main(String[] args) {
           // 加载配置文件得到上下文对象，也就是容器对象
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           // 获取资源
           BookService bookService = (BookService) ctx.getBean("bookService");
           bookService.save();
       }
   }
   ```

8. 运行程序。测试结果为：

   ```java
   // book service save ...
   // book dao save ...
   ```

### 1.4.2 DI入门案例

Spring的IOC入门案例已经完成，但是在`BookServiceImpl`的类中依然存在`BookDaoImpl`对象的new操作，它们之间的耦合度还是比较高，这块该如何解决，就需要用到下面的`DI(Dependency Injection)：依赖注入`。

1. 去除代码中的new。在`BookServiceImpl`类中，删除业务层中使用new的方式创建的dao对象

   ```java
   public class BookServiceImpl implements BookService {
       //删除业务层中使用new的方式创建的dao对象
       private BookDao bookDao;
   
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
       }
   }
   ```

2. 为属性提供setter方法，在`BookServiceImpl`类中，为`BookDao`提供`setter`方法

   ```java
   public class BookServiceImpl implements BookService {
       //删除业务层中使用new的方式创建的dao对象
       private BookDao bookDao;
   
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
       }
       //提供对应的set方法
       public void setBookDao(BookDao bookDao) {
           this.bookDao = bookDao;
       }
   }
   ```

3. 修改配置完成注入，在配置文件中添加依赖注入的配置

   ```xml
   <？xml version="1.0" encoding="UTF-8"？>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
       
       <!--
   		bean标签标示配置bean
       	id属性标示给bean起名字
       	class属性表示给bean定义类型
   	-->
       <bean id="bookDao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl"/>
       <bean id="bookService" class="cn.com.linxuan.demo01.service.impl.BookServiceImpl">
           <!--配置service与dao的关系-->
           <!--
               property标签表示配置当前bean的属性
               name属性表示配置哪一个具体的属性
               ref属性表示参照哪一个bean
   		-->
           <property name="bookDao" ref="bookDao"/>
       </bean>
   </beans>
   ```

   注意：配置中的两个bookDao的含义是不一样的

   * `name="bookDao"`中`bookDao`的作用是让Spring的IOC容器在获取到名称后，将首字母大写，前面加set找对应的`setBookDao()`方法进行对象注入

   * `ref="bookDao"`中`bookDao`的作用是让Spring能在IOC容器中找到id为`bookDao`的Bean对象给`bookService`进行注入

   所以如果我们把`<bean id="bookDao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl"/>`中`id`的值修改为：`bookDao1`，那么`<property name="bookDao" ref="bookDao"/>`应该修改为：`<property name="bookDao" ref="bookDao1"/>`

   综上所述，对应关系如下：

   ![1629736314989](..\图片\3-02【Spring】\1-8.png)

4. 运行程序。运行，测试结果为：

   ```html
   <!--
   	book service save ...
   	book dao save ...
   -->
   ```

# 第二章 IOC

## 2.1 bean配置

![](..\图片\3-02【Spring】\1-11.png)

### 2.1.1 id与class

对于bean的基础配置，在前面的案例中已经使用过：

```xml
<bean id="bookDao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl"/>
```

> class属性不能直接写接口如`BookDao`的全类名。显而易见，这里用到了反射来创建Bean对象，而接口是没办法创建对象的，所以这里不能写接口的全类名。class属性表示给bean定义类型

### 2.1.2 name属性

<!-- Ebi全称Enterprise Business Interface，翻译为企业业务接口 -->

name：为bean指定别名，别名可以有多个，使用逗号，分号，空格进行分隔。接下来我们来使用一下：

1. 配置别名。打开Spring的配置文件`applicationContext.xml`

   ```xml
   <!--
   	name：为bean指定别名，别名可以有多个，使用逗号，分号，空格进行分隔
   	Ebi全称Enterprise Business Interface，翻译为企业业务接口
   -->
   <bean id="bookService" name="service service4 bookEbi" class="cn.com.linxuan.demo01.service.impl.BookServiceImpl">
       <property name="bookDao" ref="bookDao"/>
   </bean>
   
   <bean id="bookDao" name="dao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl"/>
   ```

2. 根据名称容器中获取bean对象

   ```java
   public class AppForName {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           //此处根据bean标签的id属性和name属性的任意一个值来获取bean对象
           BookService bookService = (BookService) ctx.getBean("service4");
           bookService.save();
       }
   }
   ```

3. 运行程序。测试结果为：

   ```java
   // book service save ...
   // book dao save ...
   ```

注意事项：

* `name="bookDao"`中`bookDao`的作用是让Spring的IOC容器在获取到名称后，将首字母大写，前面加set找对应的`setBookDao()`方法进行对象注入

* `ref="bookDao"`中`bookDao`的作用是让Spring能在IOC容器中找到id为`bookDao`的Bean对象给`bookService`进行注入。bean依赖注入的ref属性指定bean，必须在容器中存在。如果不存在，则会报错。

获取bean无论是通过id还是name获取，如果无法获取到，将抛出异常`NoSuchBeanDefinitionException`

### 2.1.3 作用范围scope

看到这个作用范围，我们就得思考bean的作用范围是来控制bean哪块内容的？我们先来看下`bean作用范围的配置属性`：

![image-20210729183628138](..\图片\3-02【Spring】\1-12.png)

来看一段程序：

```java
public class App2 {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        BookDao bookDao1 = (BookDao) ctx.getBean("bookDao");
        BookDao bookDao2 = (BookDao) ctx.getBean("bookDao");
        System.out.println(bookDao1);
        System.out.println(bookDao2);
    }
}

// cn.com.linxuan.demo01.dao.impl.BookDaoImpl@6293abcc
// cn.com.linxuan.demo01.dao.impl.BookDaoImpl@6293abcc
```

可以发现，Spring默认给我们创建的Bean对象都是单例的。如果想要创建非单例的，那么可以在Spring配置文件中，配置scope属性来实现bean的非单例创建。

`singleton`默认为单例，`prototype`为非单例。在Spring的配置文件中，修改`<bean>`的scope属性。将scope设置为`prototype`：

```xml
<bean id="bookDao" name="dao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl" scope="prototype"/>
```

运行`App2`，打印看结果

```java
// cn.com.linxuan.demo01.dao.impl.BookDaoImpl@6293abcc
// cn.com.linxuan.demo01.dao.impl.BookDaoImpl@7995092a
```

那么问题来了，为什么bean默认为单例？
* bean为单例的意思是在Spring的IOC容器中只会有该类的一个对象，bean对象只有一个就避免了对象的频繁创建与销毁，达到了bean对象的复用，性能高。

bean在容器中是单例的，会不会产生线程安全问题？
* 如果对象是有状态对象，即该对象有成员变量可以用来存储数据的，因为所有请求线程共用一个bean对象，所以会存在线程安全问题。
* 如果对象是无状态对象，即该对象没有成员变量没有进行数据存储的，因为方法中的局部变量在方法调用完成后会被销毁，所以不会存在线程安全问题。

哪些bean对象适合交给容器进行管理？
* 表现层对象
* 业务层对象
* 数据层对象
* 工具对象

哪些bean对象不适合交给容器进行管理？
* 封装实例的域对象，因为会引发线程安全问题，所以不适合。

## 2.2 bean实例化

对象已经能交给Spring的IOC容器来创建了，但是容器是如何来创建对象的呢？

这就需要研究下`bean的实例化过程`，在这块内容中主要解决两部分内容，分别是：

* bean是如何创建的
* 实例化bean的三种方式，`构造方法`，`静态工厂`和`实例工厂`

在讲解这三种创建方式之前，我们需要先确认一件事：bean本质上就是对象，对象在new的时候会使用构造方法完成，那创建bean也是使用构造方法完成的。

### 2.2.1 构造方法实例化

我们来研究下Spring中的第一种bean的创建方式`构造方法实例化`：

1. 准备需要被创建的类。准备一个`BookDao`和`BookDaoImpl`类

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

2. 将类配置到Spring容器

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="bookDao" name="dao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl"/>
   </beans>
   ```

3. 编写运行程序 

   ```java
   public class App2 {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           BookDao bookDao = (BookDao) ctx.getBean("bookDao");
           bookDao.save();
       }
   }
   ```

4. 类中提供构造函数测试。在`BookDaoImpl`类中添加一个无参构造函数，并打印一句话，方便观察结果。

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

5. 将构造函数改成private测试

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

6. 构造函数中添加一个参数测试

   ```java
   public class BookDaoImpl implements BookDao {
       private BookDaoImpl(int i) {
           System.out.println("book dao constructor is running ....");
       }
       public void save() {
           System.out.println("book dao save ...");
       }
   }
   ```

   运行程序，程序会报错，说明Spring底层使用的是类的无参构造方法。

   ![1629776331499](..\图片\3-02【Spring】\1-13.png)

**分析Spring的错误信息**

接下来，我们主要研究下Spring的报错信息来学一学如阅读。

错误信息从下往上依次查看，因为上面的错误大都是对下面错误的一个包装，最核心错误是在最下面。

```asciiarmor
Caused by: java.lang.NoSuchMethodException: cn.com.linxuan.demo01.dao.impl.BookDaoImpl.<init>()
	at java.base/java.lang.Class.getConstructor0(Class.java:3517)
	at java.base/java.lang.Class.getDeclaredConstructor(Class.java:2691)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:78)
	... 14 more
```

`Caused by: java.lang.NoSuchMethodException: cn.com.linxuan.demo01.dao.impl.BookDaoImpl.<init>()`

* `Caused by` 翻译为引起，即出现错误的原因
* `java.lang.NoSuchMethodException`：抛出的异常为没有这样的方法异常
* `com.itheima.dao.impl.BookDaoImpl.<init>()`：哪个类的哪个方法没有被找到导致的异常，`<init>()`指定是类的构造方法，即该类的无参构造方法

如果最后一行错误获取不到错误信息，接下来查看第二层：

```asciiarmor
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [cn.com.linxuan.demo01.dao.impl.BookDaoImpl]: No default constructor found; nested exception is java.lang.NoSuchMethodException: cn.com.linxuan.demo01.dao.impl.BookDaoImpl.<init>()
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:83)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateBean(AbstractAutowireCapableBeanFactory.java:1310)
	... 13 more
```

* `nested`：嵌套的意思，后面的异常内容和最底层的异常是一致的
* `BeanInstantiationException`：翻译为`bean实例化异常`
* `No default constructor found`：没有一个默认的构造函数被发现

至此，关于Spring的构造方法实例化就已经学习完了，因为每一个类默认都会提供一个无参构造函数，所以其实真正在使用这种方式的时候，我们什么也不需要做。这也是我们以后比较常用的一种方式。

所以以后我们再对它的构造方法重写的时候，我们需要将空参的构造方法也重写一下。

### 2.2.2 静态工厂实例化

接下来研究Spring中的第二种bean的创建方式`静态工厂实例化`：

**工厂方式创建对象**

在讲这种方式之前，我们需要先回顾一个知识点是使用工厂来创建对象的方式：

1. 准备一个`OrderDao`和`OrderDaoImpl`类

   ```java
   public interface OrderDao {
       public void save();
   }
   
   public class OrderDaoImpl implements OrderDao {
       public void save() {
           System.out.println("order dao save ...");
       }
   }
   ```

2. 创建一个工厂类`OrderDaoFactory`并提供一个静态方法

   ```java
   //静态工厂创建对象
   public class OrderDaoFactory {
       public static OrderDao getOrderDao(){
           return new OrderDaoImpl();
       }
   }
   ```

3. 编写`AppForInstanceOrder`运行类，在类中通过工厂获取对象

   ```java
   public class AppForInstanceOrder {
       public static void main(String[] args) {
           //通过静态工厂创建对象
           OrderDao orderDao = OrderDaoFactory.getOrderDao();
           orderDao.save();
       }
   }
   ```

4. 运行后，可以查看到结果

   ```java
   // order dao save ...
   ```

**Spring静态工厂实例化**

如果代码中对象是通过上面的这种方式来创建的，如何将其交给Spring来管理呢？这就要用到Spring中的静态工厂实例化的知识了，具体实现步骤为：

1. 在spring的配置文件`application.properties`中添加以下内容：

   ```xml
   <!--
   	class：工厂类的全类名
   	factory-mehod：具体工厂类中创建对象的方法名
   -->
   
   <bean id="orderDao" class="cn.com.linxuan.demo01.factory.OrderDaoFactory" factory-method="getOrderDao"/>
   ```
   
2. 在`AppForInstanceOrder`运行类，使用从IOC容器中获取bean的方法进行运行测试

   ```java
   public class AppForInstanceOrder {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           OrderDao orderDao = (OrderDao) ctx.getBean("orderDao");
           orderDao.save();
       }
   }
   ```
   
3. 运行后，可以查看到结果

   ```java
   // order dao save ...
   ```

看到这，可能有人会问了，你这种方式在工厂类中不也是直接new对象的，和我自己直接new没什么太大的区别，而且静态工厂的方式反而更复杂，这种方式的意义是什么？

主要的原因是：在工厂的静态方法中，我们除了new对象还可以做其他的一些业务操作，这些操作必不可少。

```java
public class OrderDaoFactory {
    public static OrderDao getOrderDao(){
        System.out.println("factory setup....");//模拟必要的业务操作
        return new OrderDaoImpl();
    }
}

// factory setup....
// order dao save ...
```

这种方式一般是用来兼容早期的一些老系统，所以了解为主。

### 2.2.3 实例工厂 FactoryBean

接下来继续来研究Spring的第三种bean的创建方式`实例工厂实例化`：

1. 准备一个`UserDao`和`UserDaoImpl`类

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

2. 创建一个工厂类`OrderDaoFactory`并提供一个普通方法，注意此处和静态工厂的工厂类不一样的地方是方法不是静态方法。

   ```java
   public class UserDaoFactory {
       public UserDao getUserDao(){
           return new UserDaoImpl();
       }
   }
   ```

3. 编写`AppForInstanceUser`运行类，在类中通过工厂获取对象

   ```java
   public class AppForInstanceUser {
       public static void main(String[] args) {
           //创建实例工厂对象
           UserDaoFactory userDaoFactory = new UserDaoFactory();
           //通过实例工厂对象创建对象
           UserDao userDao = userDaoFactory.getUserDao();
           userDao.save();
   }
   ```

4. 运行后，可以查看到结果

   ```java
   // user dao save ...
   ```

对于上面这种实例工厂的方式如何交给Spring管理呢？

**实例工厂实例化**

1. 在spring的配置文件中添加以下内容：

   ```xml
   <bean id="userDaoFactory" class="cn.com.linxuan.demo01.factory.UserDaoFactory"/>
   
   <!--
   	factory-bean：工厂的实例对象
   	factory-method：工厂对象中的具体创建对象的方法名，对应关系如下：
   -->
   <bean id="userDao" factory-method="getUserDao" factory-bean="userDaoFactory"/>
   ```

   实例化工厂运行的顺序是：

   * 创建实例化工厂对象，对应的是第一行配置
   * 调用对象中的方法来创建bean，对应的是最后一行配置

2. 在`AppForInstanceUser`运行类，使用从IOC容器中获取bean的方法进行运行测试

   ```java
   public class AppForInstanceUser {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           UserDao userDao = (UserDao) ctx.getBean("userDao");
           userDao.save();
       }
   }
   ```
   
3. 运行后，可以查看到结果

   ```java
   // user dao save ...
   ```

实例工厂实例化的方式就已经介绍完了，配置的过程还是比较复杂，所以Spring为了简化这种配置方式就提供了一种叫`FactoryBean`的方式来简化开发，这种方式重点掌握。

**FactoryBean的使用**

1. 创建一个`UserDaoFactoryBean`的类，实现`FactoryBean`接口，重写接口的方法

   ```java
   public class UserDaoFactoryBean implements FactoryBean<UserDao> {
       //代替原始实例工厂中创建对象的方法
       public UserDao getObject() throws Exception {
           return new UserDaoImpl();
       }
       //返回所创建类的Class对象
       public Class<？> getObjectType() {
           return UserDao.class;
       }
   }
   ```

2. 在Spring的配置文件中进行配置

   ```xml
   <bean id="userDao" class="cn.com.linxuan.demo01.factory.UserDaoFactoryBean"/>
   ```

3. `AppForInstanceUser`运行类不用做任何修改，直接运行

   ```java
   // user dao save ...
   ```

这种方式在Spring去整合其他框架的时候会被用到，所以这种方式需要理解掌握。

查看源码会发现，FactoryBean接口其实会有三个方法，分别是：

```java
public interface FactoryBean<T> {
	String OBJECT_TYPE_ATTRIBUTE = "factoryBeanObjectType";

	@Nullable
	T getObject() throws Exception;

	@Nullable
	Class<?> getObjectType();

	default boolean isSingleton() {
		return true;
	}
}
```

- `getObject()`：被重写后，在方法中进行对象的创建并返回。

- `getObjectType()`：被重写后，主要返回的是被创建类的Class对象。

- `isSingleton()`：没有被重写，因为它已经给了默认值，从方法名中可以看出其作用是设置对象是否为单例，默认true，从意思上来看，默认值就是单例。如果我们想要想改成非单例，那么直接重写该方法然后将返回值修改为false即可。但是一般情况下我们都会采用单例，也就是采用默认即可。所以`isSingleton()`方法一般不需要进行重写。

## 2.3 bean的生命周期

生命周期：从创建到消亡的完整过程，例如人从出生到死亡的整个过程就是一个生命周期。

bean生命周期：bean对象从创建到销毁的整体过程。

bean生命周期控制：在bean创建后到销毁前做一些事情。

对于bean的生命周期控制在bean的整个生命周期中所处的位置如下：

* 初始化容器
  * 创建对象(内存分配)
  * 执行构造方法
  * 执行属性注入(set操作)
  * 执行bean 初始化方法

  * 使用bean 执行业务操作

* 关闭/销毁容器
  * 执行bean销毁方法

现在我们面临的问题是如何在bean的创建之后和销毁之前把我们需要添加的内容添加进去。

### 2.3.1 使用Bean标签控制

1. 添加初始化和销毁方法。针对这两个阶段，我们在`BooDaoImpl`类中分别添加两个方法。

   ```java
   public class BookDaoImpl implements BookDao {
       public void save() {
           System.out.println("book dao save ...");
       }
       //表示bean初始化对应的操作
       public void init(){
           System.out.println("init...");
       }
       //表示bean销毁前对应的操作
       public void destroy(){
           System.out.println("destory...");
       }
   }
   ```

2. 配置生命周期。在配置文件添加配置，如下：

   ```xml
   <bean id="bookDao" class="cn.com.linxuan.demo01.dao.impl.BookDaoImpl" init-method="init" destroy-method="destroy"/>
   <bean id="bookService" class="cn.com.linxuan.demo01.service.impl.BookServiceImpl">
       <!--配置service与dao的关系-->
       <!--
           property标签表示配置当前bean的属性
           	name属性表示配置哪一个具体的属性
           	ref属性表示参照哪一个bean
   	-->
       <property name="bookDao" ref="bookDao"/>
   </bean>
   ```

3. 运行程序。运行主方法：

   ```java
   public class App2 {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
   
           BookDao bookDao = (BookDao) ctx.getBean("bookDao");
           bookDao.save();
       }
   }
   
   // init...
   // book dao save ...
   ```

从结果中可以看出，`init`方法执行了，但是`destroy`方法却未执行，这是为什么呢？

原因如下：Spring的IOC容器是运行在JVM中。运行main方法后，JVM启动，Spring加载配置文件生成IOC容器，从容器获取bean对象，然后调方法执行。main方法执行完后，JVM退出，这个时候IOC容器中的bean还没有来得及销毁就已经结束了。所以没有调用对应的destroy方法。

对此我们的解决方法有两种：close关闭容器和注册钩子关闭容器。

**close关闭容器**

首先我们要了解到`ApplicationContext`中没有close方法，所以我们需要将`ApplicationContext`更换成`ClassPathXmlApplicationContext`。

```java
ClassPathXmlApplicationContext ctx = new 
    ClassPathXmlApplicationContext("applicationContext.xml");
```

调用ctx的close()方法

```java
ctx.close();
```

运行程序，就能执行destroy方法的内容

```java
// init...
// book dao save ...
// destroy...
```

**注册钩子关闭容器**

我们可以在容器未关闭之前，提前设置好回调函数，让JVM在退出之前回调此函数来关闭容器。调用ctx的`registerShutdownHook()`方法

```java
ctx.registerShutdownHook();
```

> **注意：**registerShutdownHook在ApplicationContext中也没有

运行后，查询打印结果

```java
// init...
// book dao save ...
// destroy...
```

两种方式介绍完后，`close`和`registerShutdownHook`选哪个？

- 相同点：这两种都能用来关闭容器

- 不同点：`close()`是在调用的时候关闭，`registerShutdownHook()`是在JVM退出前调用关闭。


### 2.3.2 接口完成声明周期控制

分析上面的实现过程，会发现添加需要初始化和销毁方法，即需要编码也需要配置，实现起来步骤比较多也比较乱。Spring提供了两个接口来完成生命周期的控制，好处是可以不用再进行配置`init-method`和`destroy-method`。

接下来在`BookServiceImpl`完成这两个接口的使用：修改`BookServiceImpl`类，添加两个接口`InitializingBean`， `DisposableBean`并实现接口中的两个方法`afterPropertiesSet`和`destroy`：

```java
public class BookServiceImpl implements BookService, InitializingBean, DisposableBean {
    private BookDao bookDao;
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
    public void save() {
        System.out.println("book service save ...");
        bookDao.save(); 
    }
    public void destroy() throws Exception {
        System.out.println("service destroy");
    }
    
    // 对于InitializingBean接口中的afterPropertiesSet方法，翻译过来为属性设置之后。
    public void afterPropertiesSet() throws Exception {
        System.out.println("service init");
    }
}
```

重新运行主方法类：

```java
public class App2 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        BookDao bookDao = (BookDao) ctx.getBean("bookDao");
        bookDao.save();
        ctx.close();
    }
}
// init...
// service init
// book dao save ...
// service destroy
// destroy...

// 由于我们在applicationContext.xml对service也完成了配置，所以也会打印service里面的内容
```

* 对于`InitializingBean`接口中的`afterPropertiesSet`方法，翻译过来为`属性设置之后`。
* 对于`BookServiceImpl`来说，`bookDao`是它的一个属性。`setBookDao`方法是Spring的IOC容器为其注入属性的方法

`afterPropertiesSet`和`setBookDao`谁先执行？从方法名分析，猜想应该是`setBookDao`方法先执行。

我们在`setBookDao`方法中添加一句话：

```java
public void setBookDao(BookDao bookDao) {
    System.out.println("set .....");
    this.bookDao = bookDao;
}
```

重新运行`AppForLifeCycle`，打印结果如下：

```java
// init...
// set .....
// service init
// book dao save ...
// service destroy
// destroy...
```

验证的结果和我们猜想的结果是一致的，所以初始化方法会在类中属性设置之后执行。

# 第三章 DI依赖注入

向一个类中传递数据的方式有两种：普通方法(set方法)、构造方法。

依赖注入描述了在容器中建立bean与bean之间的依赖关系的过程，如果bean运行需要的是数字或字符串，那么就注入引用类型和简单类型(基本数据类型与String)。

Spring就是基于上面这些知识点，为我们提供了两种注入方式，分别是：setter注入和构造器注入。而它们分别又有着简单类型和引用类型。

首先来搭建一个环境：项目中添加`BookDao`、`BookDaoImpl`、`UserDao`、`UserDaoImpl`、`BookService`和`BookServiceImpl`类

```java
public interface BookDao {
    public void save();
}

public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ...");
    }
}
public interface UserDao {
    public void save();
}
public class UserDaoImpl implements UserDao {
    public void save() {
        System.out.println("user dao save ...");
    }
}

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

`resources`下提供spring的配置文件

```xml
<？xml version="1.0" encoding="UTF-8"？>
<beans xmlns="http：//www.springframework.org/schema/beans"
       xmlns：xsi="http：//www.w3.org/2001/XMLSchema-instance"
       xsi：schemaLocation="http：//www.springframework.org/schema/beans http：//www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="bookDao" class="cn.com.linxuan.dao.impl.BookDaoImpl"/>
    <bean id="bookService" class="cn.com.linxuan.service.impl.BookServiceImpl">
        <property name="bookDao" ref="bookDao"/>
    </bean>
</beans>
```

编写`AppForDISet`运行类，加载Spring的IOC容器，并从中获取对应的bean对象

```java
public class AppForDISet {
    public static void main( String[] args ) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookService = (BookService) ctx.getBean("bookService");
        bookService.save();
    }
}
```

## 3.1 setter注入

对于setter方式注入引用类型的方式之前已经学习过，我们来快速回顾下：

1. 在bean中定义引用类型属性，并提供可访问的set方法

   ```java
   public class BookServiceImpl implements BookService {
       private BookDao bookDao;
       public void setBookDao(BookDao bookDao) {
           this.bookDao = bookDao;
       }
   }
   ```

2. 配置中使用property标签ref属性注入引用类型对象

   ```xml
   <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
   	<property name="bookDao" ref="bookDao"/>
   </bean>
   
   <bean id="bookDao" class="com.itheima.dao.imipl.BookDaoImpl"/>
   ```

上面就是setter注入了，接下来讲述一下如何注入引用数据类型和注入简单数据类型：

### 3.1.1 注入引用数据类型

需求：在bookServiceImpl对象中注入userDao

1. 声明属性并提供setter方法。在`BookServiceImpl`中声明`userDao`属性，并提供`setter`方法

   ```java
   public class BookServiceImpl implements BookService{
       private BookDao bookDao;
       private UserDao userDao;
       
       public void setUserDao(UserDao userDao) {
           this.userDao = userDao;
       }
       public void setBookDao(BookDao bookDao) {
           this.bookDao = bookDao;
       }
   
       public void save() {
           System.out.println("book service save ...");
           bookDao.save();
           userDao.save();
       }
   }
   ```

2. 配置文件中进行注入配置。在`applicationContext.xml`配置文件中使用`property`标签注入

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="bookDao" class="cn.com.linxuan.dao.impl.BookDaoImpl"/>
       <bean id="userDao" class="cn.com.linxuan.dao.impl.UserDaoImpl"/>
       <bean id="bookService" class="cn.com.linxuan.service.impl.BookServiceImpl">
           <property name="bookDao" ref="bookDao"/>
           <property name="userDao" ref="userDao"/>
       </bean>
   </beans>
   ```

3. 运行程序。运行`AppForDISet`类，查看结果，说明`userDao`已经成功注入。

   ```java
   // book service save ...
   // book dao save ...
   // user dao save ...
   ```

### 3.1.2 注入简单数据类型

需求：给BookDaoImpl注入一些简单数据类型的数据

1. 声明属性并提供`setter`方法。在`BookDaoImpl`类中声明对应的简单数据类型的属性，并提供对应的setter方法

   ```java
   public class BookDaoImpl implements BookDao {
   
       private String databaseName;
       private int connectionNum;
   
       public void setConnectionNum(int connectionNum) {
           this.connectionNum = connectionNum;
       }
   
       public void setDatabaseName(String databaseName) {
           this.databaseName = databaseName;
       }
   
       public void save() {
           System.out.println("book dao save ..."+databaseName+"，"+connectionNum);
       }
   }
   ```

2. 配置文件中进行注入配置。在`applicationContext.xml`配置文件中使用property标签注入

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
           <!--
   			两个property注入标签的顺序可以任意。
                   value：后面跟的是简单数据类型，对于参数类型，Spring在注入的时候会自动转换
                   databaseName是String类型，注入的时候会自动转换
                   connectionNum是int类型，不能将值写成字符串类型，这样会报错。
   		-->
           <property name="databaseName" value="mysql"/>
        	<property name="connectionNum" value=10/>
       </bean>
       <bean id="userDao" class="cn.com.linxuan.dao.impl.UserDaoImpl"/>
       <bean id="bookService" class="cn.com.linxuan.service.impl.BookServiceImpl">
           <property name="bookDao" ref="bookDao"/>
           <property name="userDao" ref="userDao"/>
       </bean>
   </beans>
   ```

3. 运行程序。运行`AppForDISet`类，查看结果，说明`userDao`已经成功注入。

   ```java
   // book service save ...
   // book dao save ...mysql，10
   // user dao save ...
   ```

对于setter注入方式的基本使用就已经介绍完了，

* 对于引用数据类型使用的是`<property name="" ref=""/>`
* 对于简单数据类型使用的是`<property name="" value=""/>`

## 3.2  构造器注入

在setter注入的环境中新建一个`AppForDIConstructor`运行类，加载Spring的IOC容器，并从中获取对应的bean对象

```java
public class AppForDIConstructor {
    public static void main( String[] args ) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookService = (BookService) ctx.getBean("bookService");
        bookService.save();
    }
}
```

### 3.2.1 注入引用数据类型

需求：将`BookServiceImpl`类中的`bookDao`修改成使用构造器的方式注入。

1. 删除`setter`方法并提供构造方法。在`BookServiceImpl`类中将bookDao的setter方法删除掉，并添加带有bookDao参数的构造方法

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

2. 配置文件中进行配置构造方式注入。在`applicationContext.xml`中配置

   > 对于注入多个应用数据类型配置的时候记得都需要配置。

   ```xml
   <？xml version="1.0" encoding="UTF-8"？>
   <beans xmlns="http：//www.springframework.org/schema/beans"
          xmlns：xsi="http：//www.w3.org/2001/XMLSchema-instance"
          xsi：schemaLocation="http：//www.springframework.org/schema/beans http：//www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl"/>
       <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
           <!--
   			标签<constructor-arg>中
                   * name属性对应的值为构造函数中方法形参的参数名，必须要保持一致。
                   * ref属性指向的是spring的IOC容器中其他bean对象。
   		-->
           <constructor-arg name="bookDao" ref="bookDao"/>
       </bean>
   </beans>
   ```

3. 运行程序。运行`AppForDIConstructor`类，查看结果，说明bookDao已经成功注入。

### 3.2.2 注入简单数据类型

同理，只是在配置的时候需要多操点心。和Setter注入没啥差别，将`property`换成了`constructor-arg`。

### 3.2.3 注入问题

上面已经完成了构造函数注入的基本使用，但是会存在一些问题：

![1629803529598](..\图片\3-02【Spring】\1-14.png)

当构造函数中方法的参数名发生变化后，配置文件中的name属性也需要跟着变，这两块存在紧耦合，具体该如何解决？

1. 方式一：删除name属性，添加type属性，按照类型注入

   ```xml
   <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
       <constructor-arg type="int" value="10"/>
       <constructor-arg type="java.lang.String" value="mysql"/>
   </bean>
   ```

   这种方式可以解决构造函数形参名发生变化带来的耦合问题。但是如果构造方法参数中有类型相同的参数，这种方式就不太好实现了

2. 方式二：删除type属性，添加index属性，按照索引下标注入，下标从0开始

   ```xml
   <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
       <constructor-arg index="1" value="100"/>
       <constructor-arg index="0" value="mysql"/>
   </bean>
   ```

   这种方式可以解决参数类型重复问题，但是如果构造方法参数顺序发生变化后，这种方式又带来了耦合问题

介绍完两种参数的注入方式，具体我们该如何选择呢？

- 强制依赖使用构造器进行，使用setter注入有概率不进行注入导致null对象出现。强制依赖指对象在创建的过程中必须要注入指定的参数
- 可选依赖使用setter注入进行，灵活性强。可选依赖指对象在创建过程中注入的参数可有可无。
- Spring框架倡导使用构造器，第三方框架内部大多数采用构造器注入的形式进行数据初始化，相对严谨。
- 如果有必要可以两者同时使用，使用构造器注入完成强制依赖的注入，使用setter注入完成可选依赖的注入。
- 实际开发过程中还要根据实际情况分析，如果受控对象没有提供setter方法就必须使用构造器注入。
- 自己开发的模块推荐使用setter注入。

## 3.4 自动配置

前面花了大量的时间把Spring的注入去学习了下，总结起来就一个字麻烦。麻烦在哪？配置文件的编写配置上。有更简单方式么？有，自动配置。

依赖自动装配：IOC容器根据bean所依赖的资源在容器中自动查找并注入到bean中的过程称为自动装配。

自动装配方式如下：

* 按类型(常用)
* 按名称
* 按构造方法
* 不启用自动装配

自动装配只需要修改`applicationContext.xml`配置文件即可：

1. 将`<property>`标签删除

2. 在`<bean>`标签中添加`autowire`属性。首先来实现按照类型注入的配置，`autowire="byType"`

   ```xml
   <？xml version="1.0" encoding="UTF-8"？>
   <beans xmlns="http：//www.springframework.org/schema/beans"
          xmlns：xsi="http：//www.w3.org/2001/XMLSchema-instance"
          xsi：schemaLocation="http：//www.springframework.org/schema/beans http：//www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean class="com.itheima.dao.impl.BookDaoImpl"/>
       <!--autowire属性：开启自动装配，通常使用按类型装配-->
       <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl" autowire="byType"/>
   
   </beans>
   ```

   > 需要注入属性的类中对应属性的setter方法不能省略，被注入的对象必须要被Spring的IOC容器管理。按照类型在Spring的IOC容器中如果找到多个对象，会报`NoUniqueBeanDefinitionException`
   
   一个类型在IOC中有多个对象，还想要注入成功，这个时候就需要按照名称注入，`autowire="byName"`

   ```xml
   <？xml version="1.0" encoding="UTF-8"？>
   <beans xmlns="http：//www.springframework.org/schema/beans"
          xmlns：xsi="http：//www.w3.org/2001/XMLSchema-instance"
          xsi：schemaLocation="http：//www.springframework.org/schema/beans http：//www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean class="com.itheima.dao.impl.BookDaoImpl"/>
       <!--autowire属性：开启自动装配，通常使用按类型装配-->
       <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl" autowire="byName"/>
   
   </beans>
   ```
   
   按照名称注入中的名称指的是什么？

   ```java
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
   
   bookDao是private修饰的，外部类无法直接访问，外部类只能通过属性的set方法进行访问。对外部类来说，setBookDao方法名，去掉set后首字母小写是其属性名，为什么是去掉set首字母小写？这个规则是set方法生成的默认规则，set方法的生成是把属性名首字母大写前面加set形成的方法名。
   
   所以按照名称注入，其实是和对应的set方法有关，但是如果按照标准起名称，属性名和set对应的名是一致的，如果按照名称去找对应的bean对象，找不到则注入Null。当某一个类型在IOC容器中有多个对象，按照名称注入只找其指定名称对应的bean对象，不会报错 

两种方式介绍完后，以后用的更多的是按照类型注入。

最后对于依赖注入，需要注意一些其他的配置特征：

1. 自动装配用于引用类型依赖注入，不能对简单类型进行操作
2. 使用按类型装配时(byType)必须保障容器中相同类型的bean唯一，推荐使用
3. 使用按名称装配时(byName)必须保障容器中具有指定名称的bean，因变量名与配置耦合，不推荐使用
4. 自动装配优先级低于setter注入与构造器注入，同时出现时自动装配配置失效

## 3.5 集合注入

前面我们已经能完成引入数据类型和简单数据类型的注入，但是还有一种数据类型集合，集合中既可以装简单数据类型也可以装引用数据类型，对于集合，在Spring中该如何注入呢？

先来回顾下，常见的集合类型有哪些？`数组、List、Set、Map、Properties`。针对不同的集合类型，该如何实现注入呢？

我们首先来搭建一个环境，创建Maven项目：

1. 项目中添加添加BookDao、BookDaoImpl类

   ```java
   public interface BookDao {
       public void save();
   }
    
   public class BookDaoImpl implements BookDao {
   
       private int[] array;
   
       private List<String> list;
   
       private Set<String> set;
   
       private Map<String，String> map;
   
       private Properties properties;
   
        public void save() {
           System.out.println("book dao save ...");
   
           System.out.println("遍历数组：" + Arrays.toString(array));
   
           System.out.println("遍历List" + list);
   
           System.out.println("遍历Set" + set);
   
           System.out.println("遍历Map" + map);
   
           System.out.println("遍历Properties" + properties);
       }
   	//setter....方法省略，自己使用工具生成
   }
   ```
   
2. resources下提供spring的配置文件，applicationContext.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
       <bean id="bookDao" class="cn.com.linxuan.dao.impl.BookDaoImpl"/>
   </beans>
   ```
   
3. 编写`AppForDICollection`运行类，加载Spring的IOC容器，并从中获取对应的bean对象

   ```java
   public class AppForDICollection {
       public static void main( String[] args ) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           BookDao bookDao = (BookDao) ctx.getBean("bookDao");
           bookDao.save();
       }
   }
   ```

接下来，在上面这个环境中来完成`集合注入`的学习。下面的所以配置方式，都是在bookDao的bean标签中使用`<property>`进行注入

* 注入数组类型数据

  ```xml
  <property name="array">
      <array>
          <value>100</value>
          <value>200</value>
          <value>300</value>
      </array>
  </property>
  ```

* 注入List类型数据

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

* 注入Set类型数据

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

* 注入Map类型数据

  ```xml
  <property name="map">
      <map>
          <entry key="country" value="china"/>
          <entry key="province" value="henan"/>
          <entry key="city" value="kaifeng"/>
      </map>
  </property>
  ```

* 注入Properties类型数据

  ```xml
  <property name="properties">
      <props>
          <prop key="country">china</prop>
          <prop key="province">henan</prop>
          <prop key="city">kaifeng</prop>
      </props>
  </property>
  ```

配置完成后，运行下看结果：

```java
/*
    book dao save ...
    遍历数组：[100, 200, 300]
    遍历List[itcast, itheima, boxuegu, chuanzhihui]
    遍历Set[itcast, itheima, boxuegu]
    遍历Map{country=china, province=henan, city=kaifeng}
    遍历Properties{country=china, province=henan, city=kaifeng}
*/
```

**说明：**

* property标签表示setter方式注入，构造方式注入`constructor-arg`标签内部也可以写`<array>`、`<list>`、`<set>`、`<map>`、`<props>`标签
* List的底层也是通过数组实现的，所以`<list>`和`<array>`标签是可以混用
* 集合中要添加引用类型，只需要把`<value>`标签改成`<ref>`标签，这种方式用的比较少