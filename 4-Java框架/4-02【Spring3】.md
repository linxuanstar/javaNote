# 第一章 AOP基础

对于AOP，我们前面提过一句话是：AOP是在不改原有代码的前提下对其进行增强。

我们都知道OOP是一种编程思想，那么AOP也是一种编程思想，编程思想主要的内容就是指导程序员该如何编写程序，所以它们两个是不同的`编程范式`。

* `AOP(Aspect Oriented Programming)`：面向切面编程，一种编程范式，指导开发者如何组织程序结构。
* `OOP(Object Oriented Programming)`：面向对象编程

AOP的作用：在不惊动原始设计的基础上为其进行功能增强。前面咱们有技术就可以实现这样的功能即代理模式。

## 1.1 AOP核心概念

为了能更好的理解AOP的相关概念，我们准备了一个环境，整个环境的内容我们暂时可以不用关注，最主要的类为：`BookDaoImpl`

```java
@Repository
public class BookDaoImpl implements BookDao {
    public void save() {
        //记录程序当前执行执行（开始时间）
        Long startTime = System.currentTimeMillis();
        //业务执行万次
        for (int i = 0;i<10000;i++) {
            System.out.println("book dao save ...");
        }
        //记录程序当前执行时间（结束时间）
        Long endTime = System.currentTimeMillis();
        //计算时间差
        Long totalTime = endTime-startTime;
        //输出信息
        System.out.println("执行万次消耗时间：" + totalTime + "ms");
    }
    public void update(){
        System.out.println("book dao update ...");
    }
    public void delete(){
        System.out.println("book dao delete ...");
    }
    public void select(){
        System.out.println("book dao select ...");
    }
}
```

正常来说，在主方法中从容器中获取bookDao对象后，执行save方法会打印信息并打印时间差，而update、delete和select方法并不会。如果有AOP参与的情况下，那么执行`save`、`update`和`delete`方法会打印信息并打印时间差，而`select`方法并不会打印时间差（我们对其进行设置了）。

- Spring的AOP：在不惊动(改动)原有设计(代码)的前提下，想给谁添加功能就给谁添加。
- Spring的理念：无入侵式/无侵入式。

Spring到底是如何实现如下：

![1630144353462](..\图片\4-02【Spring】\3-1.png)

1. 前面一直在强调，Spring的AOP是对一个类的方法在不进行任何修改的前提下实现增强。对于上面的案例中`BookServiceImpl`中有`save`，`update`，`delete`和`select`方法，这些方法我们给起了一个名字叫连接点。

2. 在`BookServiceImpl`的四个方法中，`update`和`delete`只有打印功能并没有去计算万次执行消耗时间，但是在运行的时候已经有该功能，那也就是说`update`和`delete`方法都已经被增强，所以对于需要增强的方法我们给起了一个名字叫切入点。

3. 执行`BookServiceImpl`的`update`和`delete`方法的时候都被添加了一个计算万次执行消耗时间的功能，将这个功能抽取到一个方法中，换句话说就是存放共性功能的方法，我们给起了个名字叫通知。

4. 通知是要增强的内容，会有多个，切入点是需要被增强的方法，也会有多个，那哪个切入点需要添加哪个通知，就需要提前将它们之间的关系描述清楚，那么对于通知和切入点之间的关系描述，我们给起了个名字叫切面。

5. 通知是一个方法，方法不能独立存在需要被写在一个类中，这个类我们也给起了个名字叫通知类。

至此AOP中的核心概念就已经介绍完了，总结下：

* 连接点(JoinPoint)：程序执行过程中的任意位置，粒度为执行方法、抛出异常、设置变量等。在SpringAOP中，理解为方法的执行。
* 切入点(Pointcut)：匹配连接点的式子。在SpringAOP中，一个切入点可以描述一个具体方法，也可也匹配多个方法。
  * 一个具体的方法：如`com.linxuan.dao`包下的BookDao接口中的无形参无返回值的save方法。
  * 匹配多个方法：所有的save方法，所有的get开头的方法，所有以Dao结尾的接口中的任意方法，所有带有一个参数的方法。
* 通知(Advice)：在切入点处执行的操作，也就是共性功能。在SpringAOP中，功能最终以方法的形式呈现。
* 通知类：定义通知的类。
* 切面(Aspect)：描述通知与切入点的对应关系。

> 连接点范围要比切入点范围大，是切入点的方法也一定是连接点，但是是连接点的方法就不一定要被增强，所以可能不是切入点。

## 1.2 AOP入门案例

需求为：使用SpringAOP的注解方式完成在方法执行的前打印出当前系统时间。

接下来我们准备一个环境：

1. 创建一个Maven项目

2. pom.xml添加Spring依赖

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-context</artifactId>
           <version>5.2.10.RELEASE</version>
       </dependency>
   </dependencies>
   ```

3. 添加`BookDao`和`BookDaoImpl`类

   ```java
   package com.linxuan.dao;
   
   public interface BookDao {
       public void save();
       public void update();
   }
   ```

   ```java
   package com.linxuan.dao.impl;
   
   import com.linxuan.dao.BookDao;
   import org.springframework.stereotype.Repository;
   
   // 相当于Component注解，不过是dao层的Component注解。
   // 创建一个Been对象，让Spring管理，默认名字是当前类名首字母小写
   @Repository
   public class BookDaoImpl implements BookDao {
   
       @Override
       public void save() {
           System.out.println(System.currentTimeMillis());
           System.out.println("book dao save ...");
       }
   
       @Override
       public void update() {
           System.out.println("book dao update ...");
       }
   }
   ```

4. 创建Spring的配置类

   ```java
   package com.linxuan.config;
   
   @Configuration
   @ComponentScan("com.linxuan")
   public class SpringConfig {
   }
   ```

5. 编写App运行类

   ```java
   package com.linxuan;
   
   public class App {
       public static void main(String[] args) {
           ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
   
           BookDao bookDao = ctx.getBean(BookDao.class);
           bookDao.update();
       }
   }
   // book dao update ...
   ```


目前打印save方法的时候，因为方法中有打印系统时间，所以运行的时候是可以看到系统时间。对于update方法来说，就没有该功能。我们要使用SpringAOP的方式在不改变update方法的前提下让其具有打印系统时间的功能。

![1630144353462](..\图片\4-02【Spring】\3-1.png)

步骤如下：

1. 添加依赖。在`pom.xml`文件中添加如下坐标：

   ```xml
   <dependency>
       <groupId>org.aspectj</groupId>
       <artifactId>aspectjweaver</artifactId>
       <version>1.9.4</version>
   </dependency>
   ```

   因为`spring-context`中已经导入了`spring-aop`，所以不需要再单独导入`spring-aop`

   导入AspectJ的jar包，AspectJ是AOP思想的一个具体实现，Spring有自己的AOP实现，但是相比于AspectJ来说比较麻烦，所以我们直接采用Spring整合ApsectJ的方式进行AOP开发。

2. 定义接口与实现类。环境准备的时候，`BookDaoImpl`已经准备好，这里我们不需要做任何修改

3. 定义通知类和通知。通知就是将共性功能抽取出来后形成的方法，共性功能指的就是当前系统时间的打印。

   ```java
   package com.linxuan.aop;
   
   // 类名和方法名没有要求，可以任意。
   public class MyAdvice {
       public void method(){
           System.out.println(System.currentTimeMillis());
       }
   }
   ```

4. 定义切入点。

   创建一个私有的方法，无参数，无返回值，方法体无实际逻辑。在创建的私有的方法上面添加一个注解：`@Pointcut`。该注解的意思就是切入点。

   注解里面写一个参数`execution`(执行)，后面会自动补全一个括号，括号里面我们写上方法的返回值、方法的全类名。

   `@Pointcut("execution(void com.linxuan.dao.BookDao.update())")`：当执行到`void com.linxuan.dao.BookDao.update()`这个方法的时候，告知我们这是一个切入点。

   ```java
   package com.linxuan.aop;
   
   import org.aspectj.lang.annotation.Pointcut;
   
   public class MyAdvice {
   
       @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
       private void pt(){}
   
       public void method(){
           System.out.println(System.currentTimeMillis());
       }
   }
   ```

5. 制作切面。切面是用来描述通知和切入点之间的关系。

   绑定切入点与通知关系，并指定通知添加到原始连接点的具体执行位置。`@Before`翻译过来是之前，也就是说通知会在切入点方法执行之前执行。

   ```java
   public class MyAdvice {
   
       @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
       private void pt(){}
   
       @Before("pt()")
       public void method(){
           System.out.println(System.currentTimeMillis());
       }
   }
   ```

6. 将通知类配给容器并标识其为切面类

   ```java
   // 让Spring管理
   @Component
   // 标识为切面类
   @Aspect
   public class MyAdvice {
   
       @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
       private void pt(){}
   
       @Before("pt()")
       public void method(){
           System.out.println(System.currentTimeMillis());
       }
   }
   ```

7. 开启注解格式AOP功能，告诉spring我们这是用注解开发的aop。

   ```java
   @Configuration
   @ComponentScan("com.linxuan")
   @EnableAspectJAutoProxy
   public class SpringConfig {
   }
   ```

8. 运行程序

   ```java
   public class App {
       public static void main(String[] args) {
           ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
           BookDao bookDao = ctx.getBean(BookDao.class);
           bookDao.update();
       }
   }
   // 1652436698003
   // book dao update ...
   ```
   

| 名称 | @EnableAspectJAutoProxy |
| ---- | ----------------------- |
| 类型 | 配置类注解              |
| 位置 | 配置类定义上方          |
| 作用 | 开启注解格式AOP功能     |

| 名称 | @Aspect               |
| ---- | --------------------- |
| 类型 | 类注解                |
| 位置 | 切面类定义上方        |
| 作用 | 设置当前类为AOP切面类 |

| 名称 | @Pointcut                   |
| ---- | --------------------------- |
| 类型 | 方法注解                    |
| 位置 | 切入点方法定义上方          |
| 作用 | 设置切入点方法              |
| 属性 | value（默认）：切入点表达式 |

| 名称 | @Before                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解                                                     |
| 位置 | 通知方法定义上方                                             |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法前运行 |

# 第二章 AOP工作流程及配置

## 2.1 AOP工作流程

由于AOP是基于Spring容器管理的bean做的增强，所以整个工作过程需要从Spring加载bean说起：

> Spring容器启动 -> 读取所有切面配置中的切入点 -> 初始化bean -> 获取bean执行方法

1. Spring容器启动。

   容器启动就需要去加载bean，一共有两种类需要被加载。第一种是需要被增强的类，例如`BookDaoImpl`类；第二种是通知类，例如`MyAdvice`类。

   注意此时bean对象还没有创建成功。

2. 读取所有切面配置中的切入点。

   上面的例子中我们只定义了一个切入点并且将其描述了与通知的关系，所以上面定义的切入点是一定会读取的。

   而对于下面的这个例子而言，我们定义了两个切入点，但是第一个`ptx()`并没有被使用，所以不会被读取。

   ```java
   package com.linxuan.aop;
   
   // 让Spring管理
   @Component
   // 标识为切面类
   @Aspect
   public class MyAdvice {
   
       @Pointcut("execution(void com.linxuan.dao.BookDao.save())")
       private void ptx(){}
       
       @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
       private void pt(){}
   
       @Before("pt()")
       public void method(){
           System.out.println(System.currentTimeMillis());
       }
   }
   ```
   
3. 初始化bean。判定bean对应的类中的方法是否匹配到任意切入点。要被实例化bean对象的类中的方法和切入点进行匹配。

   ![1630152538083](..\图片\4-02【Spring】\3-2.png)

   * 匹配失败，创建原始对象，如`UserDao`。匹配失败说明不需要增强，直接调用原始对象的方法即可。

   * 匹配成功，创建原始对象（目标对象）的代理对象，如：`BookDao`。匹配成功说明需要对其进行增强，对哪个类做增强，这个类对应的对象就叫做目标对象。因为要对目标对象进行功能增强，而采用的技术是动态代理，所以会为其创建一个代理对象。最终运行的是代理对象的方法，在该方法中会对原始方法进行功能增强

   > 注意第1步在容器启动的时候，bean对象还没有被创建成功。

4. 获取bean执行方法

   获取的bean是原始对象时，调用方法并执行，完成操作。

   获取的bean是代理对象时，根据代理对象的运行模式运行原始方法与增强的内容，完成操作。

## 2.2 验证容器中是否为代理对象

为了验证IOC容器中创建的对象和我们刚才所说的结论是否一致，首先先把结论理出来：

* 如果目标对象中的方法会被增强，那么容器中将存入的是目标对象的代理对象。
* 如果目标对象中的方法不被增强，那么容器中将存入的是目标对象本身。

我们直接打印bookDao对象和bookDao对象的`getClass()`方法：

```java
public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        BookDao bookDao = ctx.getBean(BookDao.class);
        System.out.println(bookDao);
        System.out.println(bookDao.getClass());
    }
}
// com.linxuan.dao.impl.BookDaoImpl@69504ae9
// class jdk.proxy2.$Proxy20
```

发现对于匹配成功后，确实创建原始对象（目标对象）的代理对象。

那么我们手动让它匹配不成功，将切面类里面的切入点修改为并不存在，这样就匹配不成功，然后再运行：

```java
@Component
@Aspect
public class MyAdvice {

    @Pointcut("execution(void com.linxuan.dao.BookDao.update1())")
    private void pt(){}

    @Before("pt()")
    public void method(){
        System.out.println(System.currentTimeMillis());
    }
}
```

```java
public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        BookDao bookDao = ctx.getBean(BookDao.class);
        System.out.println(bookDao);
        System.out.println(bookDao.getClass());
    }
}
// com.linxuan.dao.impl.BookDaoImpl@65987993
// class com.linxuan.dao.impl.BookDaoImpl
```

不能直接打印对象，从上面两次结果中可以看出，直接打印对象走的是对象的toString方法，不管是不是代理对象打印的结果都是一样的，原因是内部对toString方法进行了重写。

## 2.3 AOP核心概念

在上面介绍AOP的工作流程中，我们提到了两个核心概念，分别是：

* 目标对象(Target)：原始功能去掉共性功能对应的类产生的对象，这种对象是无法直接完成最终工作的。
* 代理(Proxy)：目标对象无法直接完成工作，需要对其进行功能回填，通过原始对象的代理对象实现。

上面这两个概念比较抽象，简单来说：

- 目标对象就是要增强的类，例如`BookServiceImpl`类对应的对象，也叫原始对象，不能说它不能运行，只能说它在运行的过程中对于要增强的内容是缺失的。

- SpringAOP是在不改变原有设计(代码)的前提下对其进行增强的，它的底层采用的是代理模式实现的。所以要对原始对象进行增强，就需要对原始对象创建代理对象，在代理对象中的方法把通知（例如：`MyAdvice`中的`method`方法）内容加进去，就实现了增强，这就是我们所说的代理(Proxy)。


## 2.4 AOP切入点表达式

前面的案例中，有涉及到如下内容：切入点表达式

```java
@Pointcut("execution(void com.linxuan.dao.BookDao.update())")
```

切入点表达式如下：`execution(void com.linxuan.dao.BookDao.update()`

首先我们先要明确两个概念：

* 切入点：要进行增强的方法
* 切入点表达式：要进行增强的方法的描述方式

对于切入点的描述，我们其实是有两种方式的，先来看下前面的例子

```java
package com.linxuan.dao;

public interface BookDao {
    public void update();
}
```

```java
package com.linxuan.dao.impl;

@Repository
public class BookDaoImpl implements BookDao {

    @Override
    public void update() {
        System.out.println("book dao update ...");
    }
}
```

我们将上述代码中的`update()`方法变成一个切入点，为此我们一共有两种描述方式：

1. 执行`com.linxuan.dao`包下的`BookDao`接口中的无参数`update`方法

   ```
   execution(void com.linxuan.dao.BookDao.update())
   ```

2. 执行`com.linxuan.dao.impl`包下的`BookDaoImpl`类中的无参数`update`方法

   ```
   execution(void com.linxuan.dao.impl.BookDaoImpl.update())
   ```

因为调用接口方法的时候最终运行的还是其实现类的方法，所以上面两种描述方式都是可以的。

```java
// 让Spring管理
@Component
// 标识为切面类
@Aspect
public class MyAdvice {

    // 第一种方式：@Pointcut("execution(void com.linxuan.dao.BookDao.update())")
    // 第二种方式：@Pointcut("execution(void com.linxuan.dao.impl.BookDaoImpl.update())")
    // 两种任意选择其一，每一种都可以
    private void pt(){}

    @Before("pt()")
    public void method(){
        System.out.println(System.currentTimeMillis());
    }
}
```

**切入点表达式语法**

对于切入点表达式的语法为：`动作关键字(访问修饰符  返回值  包名.类/接口名.方法名(参数) 异常名）`

对于这个格式，我们不需要硬记，通过一个例子，理解它：

```java
execution(public User com.linxuan.service.UserService.findById(int))
```

* `execution`：动作关键字，描述切入点的行为动作，例如execution表示执行到指定切入点
* `public`：访问修饰符，还可以是public，private等，可以省略
* `User`：返回值，写返回值类型
* `com.linxuan.service`：包名，多级包使用点连接
* `UserService`：类/接口名称
* `findById`：方法名
* `int`：参数，直接写参数的类型，多个类型用逗号隔开
* `异常名`：方法定义中抛出指定异常，可以省略

切入点表达式就是要找到需要增强的方法，所以它就是对一个具体方法的描述，但是方法的定义会有很多，所以如果每一个方法对应一个切入点表达式，那么会很麻烦。所以接下来了解一下通配符

**通配符**

我们使用通配符描述切入点，主要的目的就是简化之前的配置。

* `*`：单个独立的任意符号，可以独立出现，也可以作为前缀或者后缀的匹配符出现

  ```java
  execution(public * com.linxuan.*.UserService.find*(*))
  ```

  匹配`com.linxuan`包下的任意包中的`UserService`类或接口中所有`find`开头的带有一个参数的方法。

* `..`：多个连续的任意符号，可以独立出现，常用于简化包名与参数的书写

  ```java
  execution(public User com..UserService.findById(..))
  ```

  匹配`com`包下的任意包中的`UserService`类或接口中所有名称为`findById`的方法

* `+`：专用于匹配子类类型

  ```java
  execution(* *..*Service+.*(..))
  ```

  这个使用率较低，描述子类的，咱们做JavaEE开发，继承机会就一次，使用都很慎重，所以很少用它。`*Service+`，表示所有以Service结尾的接口的子类。

接下来，我们把案例中使用到的切入点表达式来分析下：

```java
// 匹配接口，能匹配到
execution(void com.linxuan.dao.BookDao.update())

// 匹配实现类，能匹配到
execution(void com.linxuan.dao.impl.BookDaoImpl.update())
    
// 返回值任意，能匹配到
execution(* com.linxuan.dao.impl.BookDaoImpl.update())
    
// 返回值任意，但是update方法必须要有一个参数，无法匹配，要想匹配需要在update接口和实现类添加参数
execution(* com.linxuan.dao.impl.BookDaoImpl.update(*))
    
// 返回值为void，com包下的任意包三层包下的任意类的update方法，匹配到的是实现类，能匹配
execution(void com.*.*.*.*.update())

// 返回值为void，com包下的任意两层包下的任意类的update方法，匹配到的是接口，能匹配
execution(void com.*.*.*.update())

// 返回值为void，方法名是update的任意包下的任意类，能匹配
execution(void *..update())

// 匹配项目中任意类的任意方法，能匹配，但是不建议使用这种方式，影响范围广
execution(* *..*(..))
    
// 匹配项目中任意包任意类下只要以u开头的方法，update方法能满足，能匹配
execution(* *..u*(..))
    
// 匹配项目中任意包任意类下只要以e结尾的方法，update和save方法能满足，能匹配
execution(* *..*e(..))
    
// 返回值为void，com包下的任意包任意类任意方法，能匹配，*代表的是方法
execution(void com..*())

    
// 接下来两种更符合我们平常切入点表达式的编写规则
// 将项目中所有业务层方法的以find开头的方法匹配
execution(* com.linxuan.*.*Service.find*(..))
    
// 将项目中所有业务层方法的以save开头的方法匹配
execution(* com.linxuan.*.*Service.save*(..))
```

**书写技巧**

所有代码按照标准规范开发，否则以下技巧全部失效

1. 描述切入点通常描述接口，而不描述实现类，如果描述到实现类，就出现紧耦合了
2. 访问控制修饰符针对接口开发均采用public描述（可省略访问控制修饰符描述）
3. 返回值类型对于增删改类使用精准类型加速匹配，对于查询类使用\*通配快速描述
4. 包名书写尽量不使用`..`匹配，效率过低，常用\`*`做单个包描述匹配，或精准匹配
5. 接口名/类名书写名称与模块相关的采用`*`匹配，例如UserService书写成`*`Service，绑定业务层接口名
6. 方法名书写以动词进行精准匹配，名词采用`*`匹配，例如getById书写成getBy`*`，selectAll书写成selectAll
7. 参数规则较为复杂，根据业务方法灵活调整
8. 通常不使用异常作为匹配规则

## 2.5 AOP通知类型

前面的案例中，有涉及到如下内容：`@Before("pt()")`。它所代表的含义是将`通知`添加到`切入点`方法执行的前面。接下来我们来介绍一下其他的注解。

我们先来回顾下AOP通知：AOP通知描述了抽取的共性功能，根据共性功能抽取的位置不同，最终运行代码时要将其加入到合理的位置。

AOP一共提供了5种通知类型：前置通知、后置通知、环绕通知(重点)、返回后通知(了解)、抛出异常后通知(了解)。

为了更好的理解这几种通知类型，我们来看一张图

![1630166147697](..\图片\4-02【Spring】\3-3.png)

- 前置通知：追加功能到方法执行前，类似于在代码1或者代码2添加内容
- 后置通知：追加功能到方法执行后，不管方法执行的过程中有没有抛出异常都会执行，类似于在代码5添加内容
- 返回后通知：追加功能到方法执行后，只有方法正常执行结束后才进行，类似于在代码3添加内容，如果方法执行抛出异常，返回后通知将不会被添加
- 抛出异常后通知：追加功能到方法抛出异常后，只有方法执行出异常才进行，类似于在代码4添加内容，只有方法抛出异常后才会被添加
- 环绕通知：环绕通知功能比较强大，它可以追加功能到方法执行的前后，这也是比较常用的方式，它可以实现其他四种通知类型的功能。

**环境准备**

首先我们需要准备一下环境：在之前创建的环境上面修改一下就可以了。

将`BookDao`接口中的`select`方法返回值改为int型，修改`BookDaoImpl`类中`select`方法：

```java
package com.linxuan.dao;

public interface BookDao {
    public int select();
    public void update();
}
```

```java
package com.linxuan.dao.impl;

@Repository
public class BookDaoImpl implements BookDao {

    @Override
    public int select() {
        System.out.println("book dao select is running ...");
        return 100;
    }

    @Override
    public void update() {
        System.out.println("book dao update ...");
    }
}
```

修改通知类，添加一些方法：

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
    private void pt(){}

    public void before() {
        System.out.println("before advice ...");
    }

    public void after() {
        System.out.println("after advice ...");
    }

    public void around(){
        System.out.println("around before advice ...");
        System.out.println("around after advice ...");
    }

    public void afterReturning() {
        System.out.println("afterReturning advice ...");
    }
    
    public void afterThrowing() {
        System.out.println("afterThrowing advice ...");
    }
}
```

主方法如下：

```java
package com.linxuan;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        BookDao bookDao = ctx.getBean(BookDao.class);
        bookDao.update();
    }
}
```

### 2.5.1 前置通知

修改MyAdvice，在before方法上添加`@Before注解`

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.itheima.dao.BookDao.update())")
    private void pt(){}
    
    @Before("pt()")
    //此处也可以写成 @Before("MyAdvice.pt()")，不建议
    public void before() {
        System.out.println("before advice ...");
    }
}

// before advice ...
// book dao update ...
```

### 2.5.2 后置通知

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.itheima.dao.BookDao.update())")
    private void pt(){}
    
    @Before("pt()")
    public void before() {
        System.out.println("before advice ...");
    }
    @After("pt()")
    public void after() {
        System.out.println("after advice ...");
    }
}

// before advice ...
// book dao update ...
// after advice ...
```

### 2.5.3 环绕通知

我们来看下面代码

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.itheima.dao.BookDao.update())")
    private void pt(){}
    
    @Around("pt()")
    public void around(){
        System.out.println("around before advice ...");
        System.out.println("around after advice ...");
    }
}

// around before advice ...
// around after advice ...
```

运行结果中，通知的内容打印出来，但是原始方法的内容却没有被执行。这是因为环绕通知需要在原始方法的前后进行增强，所以环绕通知就必须要能对原始操作进行调用。实现方法如下：

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.itheima.dao.BookDao.update())")
    private void pt(){}
    
    @Around("pt()")
    public void around(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("around before advice ...");
        // 表示对原始操作的调用
        // proceed()要抛出异常
        pjp.proceed();
        System.out.println("around after advice ...");
    }
}

// around before advice ...
// book dao update ...
// around after advice ...
```

在进行环绕通知的时候，有件事情我们需要注意一下：原始方法有返回值的处理

* 我们修改一下MyAdvice，对`BookDao`中的`select`方法添加环绕通知

  ```java
  @Component
  @Aspect
  public class MyAdvice {
      @Pointcut("execution(void com.itheima.dao.BookDao.update())")
      private void pt(){}
      
      @Pointcut("execution(int com.itheima.dao.BookDao.select())")
      private void pt2(){}
      
      @Around("pt2()")
      public void aroundSelect(ProceedingJoinPoint pjp) throws Throwable {
          System.out.println("around before advice ...");
          //表示对原始操作的调用
          pjp.proceed();
          System.out.println("around after advice ...");
      }
  }
  ```

* 修改App类，调用select方法

  ```java
  public class App {
      public static void main(String[] args) {
          ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
          BookDao bookDao = ctx.getBean(BookDao.class);
          int num = bookDao.select();
          System.out.println(num);
      }
  }
  ```

  运行后会报错，错误内容为：

  ```bash
  around before advice ...
  book dao select is running ...
  around after advice ...
  
  Exception in thread "main" org.springframework.aop.AopInvocationException： Null return value from advice does not match primitive return type for： public abstract int com.linxuan.dao.BookDao.select()
  	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java：226)
  	at com.sun.proxy.$Proxy19.select(Unknown Source)
  	at com.itheima.App.main(App.java：12)
  ```

  错误大概的意思是：`空的返回不匹配原始方法的int返回`。void就是返回Null，原始方法就是BookDao下的select方法。

  这是为什么呢？我们的原始方法也就是select方法的返回值是int，而通知的返回值是void，所以根本不匹配。我们在环绕通知里面必须调用原始方法，根据上述代码，调用完之后就相等于：

  ```java
      public void aroundSelect(ProceedingJoinPoint pjp) throws Throwable {
          System.out.println("around before advice ...");
          //表示对原始操作的调用
          /*
          调用下面代码
              System.out.println("book dao select is running ...");
              return 100;
          */
          pjp.proceed();
          System.out.println("around after advice ...");
      }
  ```

  所以`pjp.proceed()`是有返回值的，它的返回值就是100。

  因为我们的AOP就是对原始方法进行增强的，所以这时候我们可以看成原始方法里面的代码块等同于上述代码，正因为如此，出问题了。返回值不同，上述代码返回值是空的，而原始方法返回值是int。

  所以如果我们使用环绕通知的话，要根据原始方法的返回值来设置环绕通知的返回值，具体解决方案为：

  ```java
  @Component
  @Aspect
  public class MyAdvice {
      @Pointcut("execution(void com.itheima.dao.BookDao.update())")
      private void pt(){}
      
      @Pointcut("execution(int com.itheima.dao.BookDao.select())")
      private void pt2(){}
      
      @Around("pt2()")
      // 是Object类型更通用
      public Object aroundSelect(ProceedingJoinPoint pjp) throws Throwable {
          System.out.println("around before advice ...");
          //表示对原始操作的调用
          Object ret = pjp.proceed();
          System.out.println("around after advice ...");
          
          // 在环绕通知中是可以对原始方法返回值进行修改的。我们这里可以不返回ret，直接返回200。那么原始方法返回值就变为了200。
          return ret;
      }
  }
  
  // around before advice ...
  // book dao select is running ...
  // around after advice ...
  // 100
  ```

**环绕通知注意事项**

1. 环绕通知必须依赖形参`ProceedingJoinPoint`才能实现对原始方法的调用，进而实现原始方法调用前后同时添加通知
2. 通知中如果未使用`ProceedingJoinPoint`对原始方法进行调用将跳过原始方法的执行
3. 对原始方法的调用可以不接收返回值，通知方法设置成void即可，如果接收返回值，最好设定为Object类型
4. 原始方法的返回值如果是void类型，通知方法的返回值类型可以设置成void，也可以设置成Object
5. 由于无法预知原始方法运行后是否会抛出异常，因此环绕通知方法必须要处理Throwable异常

我们来思考下环绕通知是如何实现其他通知类型的功能的？因为环绕通知是可以控制原始方法执行的，所以我们把增强的代码写在调用原始方法的不同位置就可以实现不同的通知类型的功能，如：

![1630170090945](..\图片\4-02【Spring】\3-4.png)

### 2.5.4 返回后通知

- 返回后通知是需要在原始方法`select`正常执行后才会被执行，如果`select()`方法执行的过程中出现了异常，那么返回后通知是不会被执行。
- 后置通知是不管原始方法有没有抛出异常都会被执行。

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.itheima.dao.BookDao.update())")
    private void pt(){}
    
    @Pointcut("execution(int com.itheima.dao.BookDao.select())")
    private void pt2(){}
    
    @AfterReturning("pt2()")
    public void afterReturning() {
        System.out.println("afterReturning advice ...");
    }
}

// book dao select is running ...
// afterReturning advice ...
// 100
```

### 2.5.5 异常后通知

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.itheima.dao.BookDao.update())")
    private void pt(){}
    
    @Pointcut("execution(int com.itheima.dao.BookDao.select())")
    private void pt2(){}
    
    @AfterThrowing("pt2()")
    public void afterThrowing() {
        System.out.println("afterThrowing advice ...");
    }
}

/*
正常打印结果如下：
        book dao select is running ...
        100
但是如果在select()方法中添加一行代码int i = 1/0，那么就会抛出异常，因此异常后通知就会执行。
如果没有抛异常，异常后通知将不会被执行。
执行后结果如下：
        book dao select is running ...
        afterThrowing advice ...
        Exception in thread "main" java.lang.ArithmeticException: / by zero
*/
```

**注意：**异常后通知是需要原始方法抛出异常，可以在`select()`方法中添加一行代码`int i = 1/0`即可。如果没有抛异常，异常后通知将不会被执行。

我们来讲通知类型总结一下：

| 名称 | @After                                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解                                                     |
| 位置 | 通知方法定义上方                                             |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法后运行 |

| 名称 | @AfterReturning                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解                                                     |
| 位置 | 通知方法定义上方                                             |
| 作用 | 设置当前通知方法与切入点之间绑定关系，当前通知方法在原始切入点方法正常执行完毕后执行 |

| 名称 | @AfterThrowing                                               |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解                                                     |
| 位置 | 通知方法定义上方                                             |
| 作用 | 设置当前通知方法与切入点之间绑定关系，当前通知方法在原始切入点方法运行抛出异常后执行 |

| 名称 | @Around                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解                                                     |
| 位置 | 通知方法定义上方                                             |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法前后运行 |

接下来我们通过一些案例加深下对通知类型的学习。

## 2.6 案例-测试执行效率

需求：任意业务层接口执行均可显示其执行效率，也就是执行时间。这个案例的目的是查看每个业务层执行的时间，这样就可以监控出哪个业务比较耗时，将其查找出来方便优化。

具体实现的思路：

1. 开始执行方法之前记录一个时间

2. 执行方法

3. 执行完方法之后记录一个时间

4. 用后一个时间减去前一个时间的差值，就是我们需要的结果。


所以要在方法执行的前后添加业务，经过分析我们将采用`环绕通知`。

接下来我们准备环境：

- 创建一个Maven项目

- `pom.xml`添加Spring依赖

  ```xml
  <dependencies>
      <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-context</artifactId>
          <version>5.2.10.RELEASE</version>
      </dependency>
      <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-jdbc</artifactId>
          <version>5.2.10.RELEASE</version>
      </dependency>
      <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-test</artifactId>
          <version>5.2.10.RELEASE</version>
      </dependency>
      <dependency>
          <groupId>org.aspectj</groupId>
          <artifactId>aspectjweaver</artifactId>
          <version>1.9.4</version>
      </dependency>
      <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <version>5.1.47</version>
      </dependency>
      <dependency>
          <groupId>com.alibaba</groupId>
          <artifactId>druid</artifactId>
          <version>1.1.16</version>
      </dependency>
      <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis</artifactId>
          <version>3.5.6</version>
      </dependency>
      <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis-spring</artifactId>
          <version>1.3.0</version>
      </dependency>
      <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.12</version>
          <scope>test</scope>
      </dependency>
  </dependencies>
  ```

- 添加`AccountService`、`AccountServiceImpl`、`AccountDao`与`Account`类

  ```java
  package com.linxuan.service;
  
  public interface AccountService {
      void save(Account account);
      void delete(Integer id);
      void update(Account account);
      List<Account> findAll();
      Account findById(Integer id);
  }
  ```

  ```java
  package com.linxuan.service.impl;
  
  @Service
  public class AccountServiceImpl implements AccountService {
  
      @Autowired
      private AccountDao accountDao;
  
      public void save(Account account) {
          accountDao.save(account);
      }
  
      public void update(Account account){
          accountDao.update(account);
      }
  
      public void delete(Integer id) {
          accountDao.delete(id);
      }
  
      public Account findById(Integer id) {
          return accountDao.findById(id);
      }
  
      public List<Account> findAll() {
          return accountDao.findAll();
      }
  }
  ```

  ```java
  package com.linxuan.dao;
  
  public interface AccountDao {
      @Insert("insert into tbl_account(name，money)values(#{name}, #{money})")
      void save(Account account);
  
      @Delete("delete from tbl_account where id = #{id} ")
      void delete(Integer id);
  
      @Update("update tbl_account set name = #{name}, money = #{money} where id = #{id} ")
      void update(Account account);
  
      @Select("select * from tbl_account")
      List<Account> findAll();
  
      @Select("select * from tbl_account where id = #{id} ")
      Account findById(Integer id);
  }
  ```

  ```java
  package com.linxuan.domain;
  
  @Data
  public class Account {
      private Integer id;
      private String name;
      private Double money;
  }
  ```

- resources下提供一个`jdbc.properties`

  ```properties
  jdbc.driver=com.mysql.jdbc.Driver
  jdbc.url=jdbc:mysql://localhost:3306/string_db
  jdbc.username=root
  jdbc.password=root
  ```

- 创建相关配置类

  ```java
  //Spring配置类：SpringConfig
  package com.linxuan.config;
  
  @Component
  @ComponentScan("com.linxuan")
  @PropertySource("classpath:jdbc.properties")
  @Import({JdbcConfig.class, MybatisConfig.class})
  public class SpringConfig {
  }
  ```

  ```java
  //JdbcConfig配置类
  package com.linxuan.config;
  
  public class JdbcConfig {
      @Value("${jdbc.driver}")
      private String driver;
      @Value("${jdbc.url}")
      private String url;
      @Value("${jdbc.username}")
      private String userName;
      @Value("${jdbc.password}")
      private String password;
  
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
  //MybatisConfig配置类
  package com.linxuan.config;
  
  public class MybatisConfig {
      @Bean
      public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
          SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
          ssfb.setTypeAliasesPackage("com.linxuan.domain");
          ssfb.setDataSource(dataSource);
          return ssfb;
      }
  
      @Bean
      public MapperScannerConfigurer mapperScannerConfigurer(){
          MapperScannerConfigurer msc = new MapperScannerConfigurer();
          msc.setBasePackage("com.linxuan.dao");
          return msc;
      }
  }
  ```

- 编写Spring整合Junit的测试类

  ```java
  // 这是在测试文件中
  package com.linxuan.service;
  
  @RunWith(SpringJUnit4ClassRunner.class)
  @ContextConfiguration(classes = SpringConfig.class)
  public class AccountServiceTestCase {
      @Autowired
      private AccountService accountService;
  
      @Test
      public void testFindById(){
          Account ac = accountService.findById(2);
      }
  
      @Test
      public void testFindAll(){
          List<Account> all = accountService.findAll();
      }
  }
  ```

环境创建好了，接下来我们来添加一下AOP：

1. 开启SpringAOP的注解功能。在Spring的主配置文件`SpringConfig`类中添加注解

   ```java
   @EnableAspectJAutoProxy
   ```

2. 创建AOP的通知类

   该类要被Spring管理，需要添加`@Component`。要标识该类是一个AOP的切面类，需要添加`@Aspect`。配置切入点表达式，需要添加一个方法，并添加`@Pointcut`。

   ```java
   package com.linxuan.aop;
   
   @Component
   @Aspect
   public class ProjectAdvice {
       //配置业务层的所有方法
       @Pointcut("execution(* com.linxuan.service.*Service.*(..))")
       private void servicePt(){}
       
       public void runSpeed(){
           
       } 
   }
   ```

3. 添加环绕通知。在`runSpeed()`方法上添加`@Around`。目前并没有做任何增强。

   ```java
   package com.linxuan.aop;
   
   @Component
   @Aspect
   public class ProjectAdvice {
   
       //配置业务层的所有方法
       @Pointcut("execution(* com.linxuan.service.*Service.*(..))")
       private void servicePt(){}
   
       //@Around("ProjectAdvice.servicePt()") 可以简写为下面的方式
       @Around("servicePt()")
       public Object runSpeed(ProceedingJoinPoint pjp) throws Throwable {
   
           Object ret = pjp.proceed();
           return ret;
       }
   }
   ```

4. 完成核心业务，记录万次执行的时间

   ```java
   package com.linxuan.aop;
   
   @Component
   @Aspect
   public class ProjectAdvice {
   
       @Pointcut("execution(* com.linxuan.service.*Service.*(..))")
       private void servicePt(){}
   
       @Around("servicePt()")
       public void runSpeed(ProceedingJoinPoint pjp) throws Throwable {
   
           long start = System.currentTimeMillis();
           for (int i = 0; i < 10000; i++) {
               pjp.proceed();
           }
           long end = System.currentTimeMillis();
           System.out.println("业务层接口万次执行时间： "+(end-start)+"ms");
       }
   }
   ```

5. 运行单元测试类。**注意：**因为程序每次执行的时长是不一样的，所以运行多次最终的结果是不一样的。

   ```java
   @Test
   public void testFindById(){
       Account ac = accountService.findById(2);
   }
   
   // 业务层接口万次执行时间： 2176ms
   ```

6. 程序优化

   目前程序所面临的问题是，多个方法一起执行测试的时候，控制台都打印的是：`业务层接口万次执行时间：xxxms`。我们没有办法区分到底是哪个接口的哪个方法执行的具体时间，所以需要优化一下。

   ```java
   @Component
   @Aspect
   public class ProjectAdvice {
       
       //配置业务层的所有方法
       @Pointcut("execution(* com.linxuan.service.*Service.*(..))")
       private void servicePt(){}
       
       //@Around("ProjectAdvice.servicePt()") 可以简写为下面的方式
       @Around("servicePt()")
       public void runSpeed(ProceedingJoinPoint pjp){
           //获取执行签名信息
           Signature signature = pjp.getSignature();
           //通过签名获取执行操作名称(接口名)
           String className = signature.getDeclaringTypeName();
           //通过签名获取执行操作名称(方法名)
           String methodName = signature.getName();
           
           long start = System.currentTimeMillis();
           for (int i = 0; i < 10000; i++) {
              pjp.proceed();
           }
           long end = System.currentTimeMillis();
           System.out.println("万次执行："+ className+"."+methodName+"---->" +(end-start) + "ms");
       } 
   }
   
   // 运行测试类：
   // 万次执行：com.linxuan.service.AccountService.findAll---->1746ms
   // 万次执行：com.linxuan.service.AccountService.findById---->798ms
   ```

## 2.7 AOP通知获取数据

目前我们写AOP仅仅是在原始方法前后追加一些操作，接下来我们要说说AOP中数据相关的内容，我们将从`获取参数`、`获取返回值`和`获取异常`三个方面来研究切入点的相关信息。

环境准备

- 创建一个Maven项目，在Pom.xml文件中添加Spring坐标

- 添加`BookDao`和`BookDaoImpl`类

  ```java
  package com.linxuan.dao;
  
  public interface BookDao {
      public String findName(int id);
  }
  ```

  ```java
  package com.linxuan.dao.impl;
  
  @Repository
  public class BookDaoImpl implements BookDao {
      @Override
      public String findName(int id) {
          System.out.println("id："+id);
          return "linxuan";
      }
  }
  ```

- 创建Spring的配置类

  ```java
  package com.linxuan.config;
  
  @Component
  @ComponentScan("com.linxuan")
  @EnableAspectJAutoProxy
  public class SpringConfig {
  }
  ```

- 编写通知类

  ```java
  package com.linxuan.aop;
  
  @Component
  @Aspect
  public class MyAdvice {
      @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
      private void pt(){}
  
      @Before("pt()")
      public void before() {
          System.out.println("before advice ..." );
      }
  
      @After("pt()")
      public void after() {
          System.out.println("after advice ...");
      }
  
      @Around("pt()")
      public Object around(ProceedingJoinPoint pjp) throws Throwable{
          Object ret = pjp.proceed();
          return ret;
      }
      @AfterReturning("pt()")
      public void afterReturning() {
          System.out.println("afterReturning advice ...");
      }
  
  
      @AfterThrowing("pt()")
      public void afterThrowing() {
          System.out.println("afterThrowing advice ...");
      }
  }
  ```

- 编写App运行类

  ```java
  public class App {
      public static void main(String[] args) {
          ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
          BookDao bookDao = ctx.getBean(BookDao.class);
          String name = bookDao.findName(100);
          System.out.println(name);
      }
  }
  
  // before advice ...
  // id：100
  // afterReturning advice ...
  // after advice ...
  // linxuan
  ```

### 2.7.1 获取参数

获取切入点方法的参数，所有的通知类型都可以获取参数

* `JoinPoint`：适用于前置、后置、返回后、抛出异常后通知
* `ProceedingJoinPoint`：适用于环绕通知

**非环绕通知获取方式**

在方法上添加`JoinPoint`，通过`JoinPoint`来获取参数

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Before("pt()")
    public void before(JoinPoint jp) {
        Object[] args = jp.getArgs();
        System.out.println(Arrays.toString(args));
        System.out.println("before advice ..." );
    }
	//...其他的略，将其他的通知先注释掉，太多的话我们看不过来。
}

// [100]
// before advice ...
// id：100
// linxuan
```

运行App类，可以获取一个数组，数组内容就是参数。为什么是数组呢？因为因为参数的个数是不固定的，假如是两个参数，那么结果也自然是两个。

使用`JoinPoint`的方式获取参数适用于`前置`、`后置`、`返回后`、`抛出异常后`通知。

**环绕通知获取方式**

环绕通知使用的是`ProceedingJoinPoint`，因为`ProceedingJoinPoint`是`JoinPoint`类的子类，所以对于`ProceedingJoinPoint`类中应该也会有对应的`getArgs()`方法：

```java
package com.linxuan.aop;

@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object[] args = pjp.getArgs();
        System.out.println(Arrays.toString(args));
        Object ret = pjp.proceed();
        return ret;
    }
    //其他的略
}

// 运行App后查看运行结果，说明ProceedingJoinPoint也是可以通过getArgs()获取参数
// [100]
// id：100
// linxuan
```

`pjp.proceed()`方法是有两个重载方法，分别是：

* `Object proceed() throws Throwable;`

* `Object proceed(Object[] var1) throws Throwable;`

调用无参数的proceed，当原始方法有参数，会在调用的过程中自动传入参数。所以调用这两个方法的任意一个都可以完成功能，但是当需要修改原始方法的参数时，就只能采用带有参数的方法，如下：

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object[] args = pjp.getArgs();
        System.out.println(Arrays.toString(args));
        args[0] = 666;
        Object ret = pjp.proceed(args);
        return ret;
    }
	//其他的略
}

// [100]
// id：666
// linxuan
```

有了这个特性后，我们就可以在环绕通知中对原始方法的参数进行拦截过滤，避免由于参数的问题导致程序无法正确运行，保证代码的健壮性。

### 2.7.2 获取返回值

获取切入点方法返回值，前置和抛出异常后通知是没有返回值，后置通知可有可无，所以不做研究。对于返回值，只有返回后`AfterReturing`和环绕`Around`这两个通知类型可以获取。

* 返回后通知`AfterReturing`
* 环绕通知`Around`

**环绕通知获取返回值**

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object[] args = pjp.getArgs();
        System.out.println(Arrays.toString(args));
        args[0] = 666;
        Object ret = pjp.proceed(args);
        return ret;
    }
	//其他的略
}
```

上述代码中，`ret`就是方法的返回值，我们是可以直接获取，不但可以获取，如果需要还可以进行修改。

**返回后通知获取返回值**

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @AfterReturning(value = "pt()", returning = "ret")
    public void afterReturning(Object ret) {
        System.out.println("afterReturning advice ..." + ret);
    }
	//其他的略
}

// id：100
// afterReturning advice ...linxuan
// linxuan
```

注意下列问题：

1. 参数名的问题

   ```java
   // 注解里面的返回值的值必须和参数名称相同
   // Object ret == returning = "ret"
   @AfterReturning(value = "pt()"，returning = "ret")
   public void afterReturning(Object ret) {
   	System.out.println("afterReturning advice ..."+ret);
   }
   ```

2. `afterReturning`方法参数类型的问题

   参数类型可以写成String，但是为了能匹配更多的参数类型，建议写成Object类型

3. `afterReturning`方法参数的顺序问题

   如果参数存在JoinPoint，那么该参数必须放在第一位，否则会出现报错。

### 2.7.3 获取异常

获取切入点方法运行异常信息，前置和返回后通知是不会有，后置通知可有可无，所以不做研究。对于获取抛出的异常，只有抛出异常后`AfterThrowing`和环绕`Around`这两个通知类型可以获取。

* 抛出异常后通知`AfterThrowing`
* 环绕通知`Around`

**环绕通知获取异常**

这块比较简单，以前我们是抛出异常，现在只需要将异常捕获，就可以获取到原始方法的异常信息了。在catch方法中就可以获取到异常，至于获取到异常以后该如何处理，这个就和业务需求有关了。

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        System.out.println(Arrays.toString(args));
        args[0] = 666;
        Object ret = null;
        try {
            ret = pjp.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }
	//其他的略
}

// [100]
// id：666
// linxuan
```

**抛出异常后通知获取异常**

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @AfterThrowing(value = "pt()"，throwing = "t")
    public void afterThrowing(Throwable t) {
        System.out.println("afterThrowing advice ..." + t);
    }
	//其他的略
}
```

运行上述代码，发现没什么变化。当然，有变化是在发生异常之后，所以我们需要手动让原始方法抛出异常。如何让原始方法抛出异常，方式有很多：

```java
@Repository
public class BookDaoImpl implements BookDao {

    public String findName(int id，String password) {
        System.out.println("id："+id);
        if(true){
            throw new NullPointerException();
        }
        return "linxuan";
    }
}

// id：100
// afterThrowing advice ...
// Exception in thread "main" java.lang.NullPointerException...
```

注意：`throwing = "t"` == `Throwable t`名称必须一致。

## 2.8 案例-百度网盘密码兼容

需求： 对百度网盘分享链接输入密码时尾部多输入的空格做兼容处理。

当我们从别人发给我们的内容中复制提取码的时候，有时候会多复制到一些空格，直接粘贴到百度的提取码输入框。但是百度那边记录的提取码是没有空格的。这个时候如果不做处理，直接对比的话，就会引发提取码不一致，导致无法访问百度盘上的内容。所以多输入一个空格可能会导致项目的功能无法正常使用。

我们就可以将输入的参数先帮用户去掉空格再操作，我们只需要在业务方法执行之前对所有的输入参数进行格式处理——`trim()`。`public String trim()`：返回字符串的副本，忽略前导空白和尾部空白。

一般只需要针对字符串处理即可。以后涉及到需要去除前后空格的业务可能会有很多，可以考虑使用AOP来统一处理。我们的需求是将原始方法的参数处理后在参与原始方法的调用，能做这件事的就只有环绕通知。

综上所述，我们需要考虑两件事：

1. 在业务方法执行之前对所有的输入参数进行格式处理——`trim()`
2. 使用处理后的参数调用原始方法——环绕通知中存在对原始方法的调用

**环境准备**

- 创建一个Maven项目，`pom.xml`添加Spring依赖

  ```xml
  <dependencies>
      <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-context</artifactId>
          <version>5.2.10.RELEASE</version>
      </dependency>
      <dependency>
          <groupId>org.aspectj</groupId>
          <artifactId>aspectjweaver</artifactId>
          <version>1.9.4</version>
      </dependency>
  </dependencies>
  ```

- 添加`ResourcesService`，`ResourcesServiceImpl`，`ResourcesDao`和`ResourcesDaoImpl`类

  ```java
  package com.linxuan.dao;
  
  public interface ResourcesDao {
      boolean readResources(String url, String password);
  }
  ```

  ```java
  package com.linxuan.dao.impl;
  
  @Repository
  public class ResourcesDaoImpl implements ResourcesDao {
  
      @Override
      public boolean readResources(String url, String password) {
          //模拟校验
          return password.equals("root");
      }
  }
  ```

  ```java
  package com.linxuan.service;
  
  public interface ResourcesService {
      public boolean openURL(String url, String password);
  }
  ```

  ```java
  package com.linxuan.service.impl;
  
  @Service
  public class ResourcesServiceImpl implements ResourcesService {
  
      @Autowired
      private ResourcesDao resourcesDao;
  
      @Override
      public boolean openURL(String url, String password) {
          return resourcesDao.readResources(url, password);
      }
  }
  ```

- 创建Spring的配置类

  ```java
  package com.linxuan.config;
  
  @Component
  @ComponentScan("com.linxuan")
  public class SpringConfig {
  }
  ```

- 编写App运行类

  ```java
  package com.linxuan;
  
  public class App {
      public static void main(String[] args) {
          ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
          ResourcesService resourcesService = ctx.getBean(ResourcesService.class);
  
          boolean flag = resourcesService.openURL("http://pan.baidu.com/haha", "root");
          System.out.println(flag);
      }
  }
  ```

现在项目的效果是，当输入密码为"root"控制台打印为true，如果密码改为"root  "控制台打印的是false。

需求是使用AOP将参数进行统一处理，不管输入的密码`root`前后包含多少个空格，最终控制台打印的都是true。

**具体实现如下** 

1. 开启SpringAOP的注解功能

   ```java
   package com.linxuan.config;
   
   @Component
   @ComponentScan("com.linxuan")
   @EnableAspectJAutoProxy
   public class SpringConfig {
   }
   ```

2. 编写通知类

   ```java
   package com.linxuan.aop;
   
   @Component
   @Aspect
   public class DataAdvice {
   
       @Pointcut("execution(boolean com.linxuan.service.*Service.openURL(*, *))")
       private void ServicePt(){}
   
   }
   ```

3. 添加环绕通知

   ```java
   @Component
   @Aspect
   public class DataAdvice {
       @Pointcut("execution(boolean com.linxuan.service.*Service.*(*，*))")
       private void servicePt(){}
       
       @Around("DataAdvice.servicePt()")
       // @Around("servicePt()")这两种写法都对
       public Object trimStr(ProceedingJoinPoint pjp) throws Throwable {
           Object ret = pjp.proceed();
           return ret;
       }
   }
   ```
   
4. 完成核心业务，处理参数中的空格

   ```java
   @Component
   @Aspect
   public class DataAdvice {
       @Pointcut("execution(boolean com.linxuan.service.*Service.*(*，*))")
       private void servicePt(){}
       
       @Around("DataAdvice.servicePt()")
       // @Around("servicePt()")这两种写法都对
       public Object trimStr(ProceedingJoinPoint pjp) throws Throwable {
           // 获取原始方法的参数
           Object[] args = pjp.getArgs();
           for (int i = 0; i < args.length; i++) {
               // 判断参数是不是字符串
               if(args[i].getClass().equals(String.class)){
                   args[i] = args[i].toString().trim();
               }
           }
           // 将修改后的参数传入到原始方法的执行中
           Object ret = pjp.proceed(args);
           return ret;
       }
   }
   ```
   
5. 运行程序。不管密码`root`前后是否加空格，最终控制台打印的都是true

6. 优化测试。为了能更好的看出AOP已经生效，我们可以修改`ResourcesImpl`类，在方法中将密码的长度进行打印

   ```java
   @Repository
   public class ResourcesDaoImpl implements ResourcesDao {
       public boolean readResources(String url， String password) {
           System.out.println(password.length());
           //模拟校验
           return password.equals("root");
       }
   }
   ```

   再次运行成功，就可以根据最终打印的长度来看看，字符串的空格有没有被去除掉。

# 第三章 Spring事务管理

首先来看一下事务：

- 事务作用：在数据层保障一系列的数据库操作同成功同失败
- Spring事务作用：在数据层或**业务层**保障一系列的数据库操作同成功同失败

数据层有事务我们可以理解，为什么业务层也需要处理事务呢？

举个简单的例子：转账业务会有两次数据层的调用，一次是加钱一次是减钱。把事务放在数据层，加钱和减钱就有两个事务。没办法保证加钱和减钱同时成功或者同时失败，这个时候就需要将事务放在业务层进行处理。

Spring为了管理事务，提供了一个平台事务管理器`PlatformTransactionManager`

```java
public interface PlatformTransactionManager extends TransactionManager {
    TransactionStatus getTransaction(@Nullable TransactionDefinition var1) throws TransactionException;

    void commit(TransactionStatus var1) throws TransactionException;

    void rollback(TransactionStatus var1) throws TransactionException;
}
```

`commit`是用来提交事务，`rollback`是用来回滚事务。

`PlatformTransactionManager`只是一个接口，Spring还为其提供了一个具体的实现：

```java
public class DataSourceTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {

}
```

从名称上可以看出，我们只需要给它一个DataSource对象，它就可以帮你去在业务层管理事务。其内部采用的是JDBC的事务。所以说如果你持久层采用的是JDBC相关的技术，就可以采用这个事务管理器来管理你的事务。Mybatis内部采用的正是JDBC的事务，所以之后Spring整合Mybatis就采用这个`DataSourceTransactionManager`事务管理器。

接下来通过一个案例来学习下Spring是如何来管理事务的。

## 3.1 转账案例

先来分析下需求：

- 需求： 实现任意两个账户间转账操作

- 需求微缩： A账户减钱，B账户加钱


为了实现上述的业务需求，我们可以按照下面步骤来实现下：

1. 数据层提供基础操作，指定账户减钱（outMoney），指定账户加钱（inMoney）
2. 业务层提供转账操作（transfer），调用减钱与加钱的操作

3. 提供2个账号和操作金额执行转账操作

4. 基于Spring整合MyBatis环境搭建上述操作


### 3.1.1 环境搭建

步骤1：准备数据库表。之前我们在整合Mybatis的时候已经创建了这个表，可以直接使用

```sql
create database spring_db character set utf8;
use spring_db;

create table tbl_account(
    id int primary key auto_increment，
    name varchar(35)，
    money double
);

INSERT INTO tbl_account VALUES(1, 'Tom', 1000);
INSERT INTO tbl_account VALUES(2, 'Jerry', 1000);
```

步骤2：创建项目导入jar包。项目的pom.xml添加相关依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>

    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.6</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.47</version>
    </dependency>

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>

    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>1.3.0</version>
    </dependency>

    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
</dependencies>
```

步骤3：根据表创建模型类

```java
package com.linxuan.domain;

public class Account implements Serializable {

    private Integer id;
    private String name;
    private Double money;
	//setter...getter...toString...方法略    
}
```

步骤4：创建Dao接口

```java
package com.linxuan.dao;

public interface AccountDao {

    @Update("update tbl_account set money = money + #{money} where name = #{name}")
    void inMoney(@Param("name") String name, @Param("money") Double money);

    @Update("update tbl_account set money = money - #{money} where name = #{name}")
    void outMoney(@Param("name") String name, @Param("money") Double money);
}
```

步骤5：创建Service接口和实现类

```java
package com.linxuan.service;

public interface AccountService {
    /**
     * 转账操作
     * @param out 输出方
     * @param in 输入方
     * @param money 钱
     */
    public void transfer(String out, String in, Double money) ;
}
```

```java
package com.linxuan.service.impl;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
        accountDao.inMoney(in, money);
    }
}
```

步骤6：添加`jdbc.properties`文件

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/string_db
jdbc.username=root
jdbc.password=root
```

步骤7：创建`JdbcConfig`配置类

```java
package com.linxuan.config;

public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

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

步骤8：创建`MybatisConfig`配置类

```java
package com.linxuan.config;

public class MybatisConfig {
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setTypeAliasesPackage("com.linxuan.domain");
        ssfb.setDataSource(dataSource);
        return ssfb;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.linxuan.dao");
        return msc;
    }
}
```

步骤9：创建SpringConfig配置类

```java
package com.linxuan.config;

@Component
@ComponentScan("com.linxuan")
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class, MybatisConfig.class})
public class SpringConfig {
}
```

步骤10：编写测试类

```java
package com.linxuan.service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void testTransfer() throws IOException {
        accountService.transfer("Tom", "Jerry", 100D);
    }
}
```

### 3.1.2 事务管理

上述环境，运行单元测试类，会执行转账操作，`Tom`的账户会减少100，`Jerry`的账户会加100。

这是正常情况下的运行结果，但是如果在转账的过程中出现了异常，如：

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    public void transfer(String out，String in ，Double money) {
        accountDao.outMoney(out，money);
        int i = 1/0;
        accountDao.inMoney(in，money);
    }

}
```

这个时候就模拟了转账过程中出现异常的情况，运行之后`Tom`账户为800而`Jerry`还是1100，100块钱凭空消失了。不管哪种情况，都是不允许出现的，对刚才的结果我们做一个分析：

- 程序正常执行时，账户金额A减B加，没有问题

- 程序出现异常后，转账失败，但是异常之前操作成功，异常之后操作失败，整体业务失败


当程序出问题后，我们需要让事务进行回滚，而且这个事务应该是加在业务层上，而Spring的事务管理就是用来解决这类问题的。

Spring事务管理具体的实现步骤为：

步骤1：在需要被事务管理的方法上添加注解

```java
public interface AccountService {
    /**
     * 转账操作
     * @param out 传出方
     * @param in 转入方
     * @param money 金额
     */
    //配置当前接口方法具有事务
    public void transfer(String out, String in,Double money) ;
}
```

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    @Transactional
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
        int i = 1 / 0;
        accountDao.inMoney(in, money);
    }
}
```

注意：

`@Transactional`可以写在接口类上、接口方法上、实现类上和实现类方法上

* 写在接口类上，该接口的所有实现类的所有方法都会有事务
* 写在接口方法上，该接口的所有实现类的该方法都会有事务
* 写在实现类上，该类中的所有方法都会有事务
* 写在实现类方法上，该方法上有事务
* 建议写在实现类或实现类的方法上

步骤2：在`JdbcConfig`类中配置事务管理器

```java
public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }

    //配置事务管理器，mybatis使用的是jdbc事务
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
```

事务管理器根据使用技术来选择，Mybatis框架使用的是JDBC事务，可以直接使用`DataSourceTransactionManager`

步骤3：开启事务注解。在SpringConfig的配置类中开启

```java
@Component
@ComponentScan("com.linxuan")
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class, MybatisConfig.class})
//开启注解式事务驱动
@EnableTransactionManagement
public class SpringConfig {
}
```

步骤4：运行测试类。会发现在转换的业务出现错误后，事务就可以控制回顾，保证数据的正确性。

| 名称 | @EnableTransactionManagement           |
| ---- | -------------------------------------- |
| 类型 | 配置类注解                             |
| 位置 | 配置类定义上方                         |
| 作用 | 设置当前Spring环境中开启注解式事务支持 |

| 名称 | @Transactional                                               |
| ---- | ------------------------------------------------------------ |
| 类型 | 接口注解  类注解  方法注解                                   |
| 位置 | 业务层接口上方  业务层实现类上方  业务方法上方               |
| 作用 | 为当前业务层方法添加事务（如果设置在类或接口上方则类或接口中所有方法均添加事务） |

## 3.2 Spring事务角色

这节中我们重点要理解两个概念，分别是`事务管理员`和`事务协调员`。

- 事务管理员：发起事务方，在Spring中通常指代业务层开启事务的方法
- 事务协调员：加入事务方，在Spring中通常指代数据层方法，也可以是业务层方法

```java
package com.linxuan.service.impl;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
        accountDao.inMoney(in, money);
    }
}
```

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    @Transactional
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
        int i = 1 / 0;
        accountDao.inMoney(in, money);
    }
}
```

1. 未开启Spring事务之前：

   * `AccountDao`的`outMoney`因为是修改操作，会开启一个事务T1

   * `AccountDao`的`inMoney`因为是修改操作，会开启一个事务T2

   * `AccountService`的`transfer`没有事务，运行过程中如果没有抛出异常，则T1和T2都正常提交，数据正确。如果在两个方法中间抛出异常，T1因为执行成功提交事务，T2因为抛异常不会被执行，就会导致数据出现错误


2. 开启Spring的事务管理后

   * `transfer`上添加了`@Transactional`注解，在该方法上就会有一个事务T

   * `AccountDao`的`outMoney`方法的事务T1加入到`transfer`的事务T中

   * `AccountDao`的`inMoney`方法的事务T2加入到`transfer`的事务T中

   * 这样就保证他们在同一个事务中，当业务层中出现异常，整个事务就会回滚，保证数据的准确性。


通过上面例子的分析，我们就可以得到如下概念：

- 事务管理员：发起事务方，在Spring中通常指代业务层开启事务的方法
- 事务协调员：加入事务方，在Spring中通常指代数据层方法，也可以是业务层方法

> 注意：目前的事务管理是基于`DataSourceTransactionManager`和`SqlSessionFactoryBean`使用的是同一个数据源。

## 3.3 Spring事务属性

### 3.3.1 事务配置

![1630250069844](..\图片\4-02【Spring】\3-5.png)

上面这些属性都可以在`@Transactional`注解的参数上进行设置。

* `readOnly`：true只读事务，false读写事务，增删改要设为false，查询设为true。
* `timeout`：设置超时时间单位秒，在多长时间之内事务没有提交成功就自动回滚，-1表示不设置超时时间。
* `rollbackFor`：当出现指定异常进行事务回滚
* `noRollbackFor`：当出现指定异常不进行事务回滚
* `rollbackForClassName`等同于`rollbackFor`，只不过属性为异常的类全名字符串
* `noRollbackForClassName`等同于`noRollbackFor`，只不过属性为异常的类全名字符串
* `isolation`设置事务的隔离级别
    * `DEFAULT`   ：默认隔离级别， 会采用数据库的隔离级别
    * `READ_UNCOMMITTED` ： 读未提交
    * `READ_COMMITTED` ： 读已提交
    * `REPEATABLE_READ` ： 重复读取
    * `SERIALIZABLE`： 串行化

出现异常事务会自动回滚，这个是我们之前就已经知道的，`noRollbackFor`是设定对于指定的异常不回滚，这个好理解。`rollbackFor`是指定回滚异常，对于异常事务不应该都回滚么，为什么还要指定？

这块需要更正一个知识点，并不是所有的异常都会回滚事务，比如下面的代码就不会回滚

```java
public interface AccountService {
    /**
     * 转账操作
     * @param out 传出方
     * @param in 转入方
     * @param money 金额
     */
    //配置当前接口方法具有事务
    public void transfer(String out, String in, Double money) throws IOException;
}

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    
	@Transactional
    public void transfer(String out, String in, Double money) throws IOException{
        accountDao.outMoney(out，money);
        // int i = 1/0; //这个异常事务会回滚
        if(true){
            throw new IOException(); //这个异常事务就不会回滚
        }
        accountDao.inMoney(in，money);
    }

}
```

出现这个问题的原因是，Spring的事务只会对`Error异常`和`RuntimeException异常`及其子类进行事务回滚，其他的异常类型是不会回滚的，对应`IOException`不符合上述条件所以不回滚。此时就可以使用`rollbackFor`属性来设置出现`IOException`异常回滚。

`@Transactional(rollbackFor = {IOException.class})`即可，这样就会回滚。

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
	@Transactional(rollbackFor = {IOException.class})
    public void transfer(String out，String in ，Double money) throws IOException{
        accountDao.outMoney(out，money);
        //int i = 1/0; //这个异常事务会回滚
        if(true){
            throw new IOException(); //这个异常事务就不会回滚
        }
        accountDao.inMoney(in，money);
    }

}
```

介绍完上述属性后，还有最后一个事务的传播行为，为了讲解该属性的设置，我们需要完成下面的案例。


### 3.3.2 案例-转账追加日志

在前面的转案例的基础上添加新的需求，完成转账后记录日志。

- 需求：实现任意两个账户间转账操作，并对每次转账操作在数据库进行留痕
- 需求微缩：A账户减钱，B账户加钱，数据库记录日志

基于上述的业务需求，我们来分析下该如何实现：

- 基于转账操作案例添加日志模块，实现数据库中记录日志

- 业务层转账操作（transfer），调用减钱、加钱与记录日志功能


需要注意一点就是，我们这个案例的预期效果为：无论转账操作是否成功，均进行转账操作的日志留痕。

**环境准备**

该环境是基于转账环境来完成的，所以环境的准备可以参考`3.1的环境搭建步骤`，在其基础上，我们继续往下写

步骤1：创建日志表

```sql
CREATE TABLE tbl_log(
   id INT PRIMARY KEY AUTO_INCREMENT,
   info VARCHAR(255),
   createDate DATETIME
)
```

步骤2：添加LogDao接口

```java
public interface LogDao {
    @Insert("insert into tbl_log (info，createDate) values(#{info}, now())")
    void log(String info);
}
```

步骤3：添加LogService接口与实现类

```java
package com.linxuan.service;

public interface LogService {
    void log(String out, String in, Double money);
}
```

```java
package com.linxuan.service.impl;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Override
    @Transactional
    public void log(String out, String in, Double money) {
        logDao.log("转账操作由" + out + "到" + in + "，金额：" + money);
    }
}
```

步骤4：在转账的业务中添加记录日志

```java
package com.linxuan.service;

public interface AccountService {
    /**
     * 转账操作
     * @param out 传出方
     * @param in 转入方
     * @param money 金额
     */
    // 配置当前接口方法具有事务
    public void transfer(String out, String in, Double money);
}
```

```java
package com.linxuan.service.impl;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private LogService logService;

    @Override
    @Transactional(rollbackFor = IOException.class)
    public void transfer(String out, String in, Double money){
        try {
            accountDao.outMoney(out, money);
            accountDao.inMoney(in, money);
        } finally {
            logService.log(out, in, money);
        }
    }
}
```

步骤5：运行程序

* 当程序正常运行，`tbl_account`表中转账成功，`tbl_log`表中日志记录成功

* 当转账业务之间出现异常(`int i =1/0`)，转账失败，`tbl_account`成功回滚，但是`tbl_log`表未添加数据。这个结果和我们想要的不一样，什么原因？该如何解决？

  失败原因：日志的记录与转账操作隶属同一个事务，同成功同失败。

  我们想要的最终效果：无论转账操作是否成功，日志必须保留

这就需要用到下面的知识了：事务传播行为

### 3.3.3 事务传播行为

事务传播行为指的是：事务协调员对事务管理员所携带事务的处理态度。

![1630253779575](..\图片\4-02【Spring】\3-6.png)

对于上述案例的分析：

* `log`方法、`inMoney`方法和`outMoney`方法都属于增删改，分别有`事务T1，T2，T3`
* `transfer`因为加了`@Transactional`注解，也开启了`事务T`
* 前面我们讲过Spring事务会把`T1，T2，T3`都加入到事务T中
* 所以当转账失败后，所有的事务都回滚，导致日志没有记录下来
* 这和我们的需求不符，这个时候我们就想能不能让log方法单独是一个事务呢？

要想解决这个问题，就需要用到事务传播行为，所谓的事务传播行为指的是：**事务协调员对事务管理员所携带事务的处理态度**。

具体如何解决，就需要用到之前我们没有说的`propagation属性`。

1. 修改`logService`改变事务的传播行为

   ```java
   @Service
   public class LogServiceImpl implements LogService {
   
       @Autowired
       private LogDao logDao;
       
   	// propagation设置事务属性：传播行为设置为当前操作需要新事务
       @Transactional(propagation = Propagation.REQUIRES_NEW)
       public void log(String out, String in, Double money ) {
           logDao.log("转账操作由"+out+"到"+in+"，金额："+money);
       }
   }
   ```

   运行后，就能实现我们想要的结果，不管转账是否成功，都会记录日志。

2. 事务传播行为的可选值

   ![1630254257628](..\图片\4-02【Spring】\3-7.png)

对于我们开发实际中使用的话，因为默认值需要事务是常态的。根据开发过程选择其他的就可以了，例如案例中需要新事务就需要手工配置。其实入账和出账操作上也有事务，采用的就是默认值。