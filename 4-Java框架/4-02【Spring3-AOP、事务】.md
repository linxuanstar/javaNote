# 第一章 AOP基础

AOP(Aspect Oriented Programming)：在不惊动原始设计的基础上为其进行功能增强。和代理模式功能类似。

OOP是一种编程思想，那么AOP也是一种编程思想，它们两个是不同的编程范式（指导程序员如何编写程序）：

* `AOP(Aspect Oriented Programming)`：面向切面编程。
* `OOP(Object Oriented Programming)`：面向对象编程。

环境介绍：

```xml
<!-- 导入依赖 -->
<dependencies>
    <!-- Spring依赖环境 该jar包默认依赖spring-aop，spring-beans，spring-core，spring-expression-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
</dependencies>
```

```java
package com.linxuan.dao;

public interface BookDao {
    void save();
    void update();
}
```

```java
package com.linxuan.dao.impl;

@Repository
public class BookDaoImpl implements BookDao {

    public void save() {
        System.out.println(System.currentTimeMillis());
        System.out.println("book dao save ...");
    }

    public void update() {
        System.out.println("book dao update ...");
    }
}
```

```java
package com.linxuan.config;

@Configuration
@ComponentScan("com.linxuan")
public class SpringConfig {   
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.save();
}
// 1675646306687
// book dao save ...
```

## 1.1 AOP操作及概念

执行save方法会打印`系统时间`和`book dao save ...`，而执行update方法只会打印`book dao update ...`。可以使用SpringAOP的方式在不改变`delete`方法的前提下让其具有打印系统时间的功能。

```xml
<!-- 导入依赖 -->
<dependencies>
    <!-- 之前已经导入过spring-context依赖，该依赖默认依赖于spring-aop，所以不用导入spring-aop依赖 -->
    <!-- 导入AspectJ依赖，它是AOP思想的一个具体实现，Spring自己的AOP实现比较麻烦，故使用AspectJ开发 -->
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.9.4</version>
    </dependency>
</dependencies>
```

```java
@Configuration
@ComponentScan("com.linxuan")
// 开启注解格式AOP功能，如果使用AOP那么该注解必须在配置类上面标识
@EnableAspectJAutoProxy
public class SpringConfig {   
}
```

```java
package com.linxuan.aop;

// 让Spring管理
@Component
// 标识为切面类
@Aspect
// 定义通知类，类名没有要求
public class MyAdvice {
    
    // 定义切入点，就是执行到这个com.linxuan.dao.BookDao.update()方法的时候告知Spring这是切入点，会有操作
    @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
    private void pt(){}
    
    // 制作切面，切面就是用来描述通知和切入点之间的关系。@Before代表该通知会在切入点方法执行之前执行
    @Before("pt()")
    // 定义通知method，通知就是将共性功能抽取出来后形成的方法，方法名没有要求
    public void method() {
        System.out.println(System.currentTimeMillis());
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.update();
}
// 1675677716035
// book dao update ...
```

上面已经成功的利用Spring的AOP对update方法进行了增强，并且没有修改任何的update方法。下面来描述一下Spring的AOP中一些基本概念：

* 连接点(JoinPoint)：程序执行过程中的任意位置，在SpringAOP中理解为方法的执行。通俗来讲就是所有可以被AOP连接用来增强的方法，对于上面的环境而言就是`save`、`update`这两个方法。
* 切入点(Pointcut)：匹配连接点的式子。在SpringAOP中，就是需要被增强的方法。一个切入点可以描述一个具体方法`com.linxuan.dao.BookDao.update()`，也可以匹配多个方法`com.linxuan.dao.BookDao.*()`。
* 通知(Advice)：在切入点处执行的操作，也就是共性功能。通知就是要增强的内容，以方法的形式呈现。
* 通知类：定义通知的类。
* 切面(Aspect)：描述通知与切入点的对应关系。

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
| 类型 | 方法注解，在切入点上方定义。`@Pointcut("execution(void com.linxuan.dao.BookDao.update())")` |
| 作用 | 设置切入点方法。                                             |
| 属性 | value（默认）：切入点表达式                                  |

| 名称 | @Before                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义                               |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法前运行 |
| 属性 | value（默认）：切入点方法。例如：`@Before("pt()")`           |

## 1.2 AOP工作流程

AOP是基于Spring容器管理的bean做的增强，所以整个工作过程需要从Spring加载bean说起：Spring容器启动 -> 读取所有切面配置中的切入点 -> 初始化bean -> 获取bean执行方法。

**Spring容器启动**

容器启动就需要去加载类，一共有两种类需要被加载。第一种是需要被增强的类，例如`BookDaoImpl`类；第二种是通知类，例如`MyAdvice`类。注意此时bean对象还没有创建成功。

**读取所有切面配置中的切入点**

如果定义切入点并将其制作切面（描述切入点和通知的关系），那么该切入点就会被读取，但是只定义了切入点并没有在任何地方制作切面（没有使用它），那么就不会读取它。

```java
package com.linxuan.aop;

// 让Spring管理
@Component
// 标识为切面类
@Aspect
public class MyAdvice {

    // 没有将其制作切面，也就是没有地方应用它，那么就不会被读取
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

**初始化bean**

判定要被SpringIOC容器管理的bean对应的类中的方法是否匹配到任意切入点，也就是要被实例化bean对象的类中的方法和切入点进行匹配。

```java
package com.linxuan.aop;

@Component
@Aspect
public class MyAdvice {
    
    @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
    private void pt(){}
    
    @Before("pt()")
    public void method() {
        System.out.println(System.currentTimeMillis());
    }
}
```

```java
@Repository
public class BookDaoImpl implements BookDao {

    public void save() {
        System.out.println(System.currentTimeMillis());
        System.out.println("book dao save ...");
    }
    
    public void update() {
        System.out.println("book dao update ...");
    }
}
```

判断BookDaoImpl类里面的方法与切入点方法进行匹配：

* 匹配成功：创建原始对象的代理对象。匹配成功说明需要对其进行增强，这个增强类对应的对象叫做目标对象。因为要对目标对象进行功能增强，而采用的技术是动态代理，所以会为其创建一个代理对象。最终运行的是代理对象的方法，在该方法中会对原始方法进行功能增强。
* 全部匹配失败：创建原始对象。匹配失败说明不需要增强，直接调用原始对象的方法即可。

注意第1步在容器启动的时候，bean对象还没有被创建成功。

**获取bean执行方法**

获取的bean是代理对象时，根据代理对象的运行模式运行原始方法与增强的内容，完成操作。获取的bean是原始对象时，调用方法并执行，完成操作。

## 1.3 验证代理对象

如果目标对象中的方法会被增强，那么容器中将存入的是目标对象的代理对象，否则存入目标对象本身。接下来验证一下：

我们直接打印bookDao对象和bookDao对象的`getClass()`方法：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao);
    System.out.println(bookDao.getClass());
}
// com.linxuan.dao.impl.BookDaoImpl@7d8704ef
// class com.sun.proxy.$Proxy19 这里说明，确实会创建原始对象的代理对象。
```

那么手动让它匹配不成功，将切面类里面的切入点修改为并不存在，这样就匹配不成功，然后再运行：

```java
@Component
@Aspect
public class MyAdvice {

    // 将这个切入点的方法修改为update1，这样并不存在，从而导致匹配失败，那么容器中存入的便是目标对象本身。
    @Pointcut("execution(void com.linxuan.dao.BookDao.update1())")
    private void pt(){}

    @Before("pt()")
    public void method(){
        System.out.println(System.currentTimeMillis());
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao);
    System.out.println(bookDao.getClass());
}
// com.linxuan.dao.impl.BookDaoImpl@65987993
// class com.linxuan.dao.impl.BookDaoImpl 这里说明Spring容器存入的确实是目标对象本身
```

在上面的流程中，提到了两个核心概念，分别是：

* 目标对象（Target）：原始功能去掉共性功能对应的类产生的对象，这种对象是无法直接完成最终工作的。也就是需要增强的类，例如`BookServiceImpl`类对应的对象，也叫做原始对象。
* 代理（Proxy）：目标对象无法直接完成工作，需要对其进行功能回填，通过原始对象的代理对象实现。AOP是在不改变原有设计(代码)的前提下对其进行增强的，它的底层采用的是代理模式实现的。

# 第二章 AOP配置


## 2.1 切入点表达式

```java
// 切入点表达式：execution(void com.linxuan.dao.BookDao.update())
@Pointcut("execution(void com.linxuan.dao.BookDao.update())")
```

切入点是要进行增强的方法，而切入点表达式就是该方法的描述方式。对于切入点表达式的书写一共有两种方式：执行接口中的无参数方法、执行实现类中的无参数方法。因为调用接口方法的时候最终运行的还是其实现类的方法，所以两种描述方式都是可以的。

```java
// 执行接口中的无参数方法
execution(void com.linxuan.dao.BookDao.update())
// 执行实现类中的无参数方法
execution(void com.linxuan.dao.impl.BookDaoImpl.update())
```

**切入点表达式语法**

切入点表达式的语法为`动作关键字(访问修饰符 返回值 包名.类/接口名.方法名(参数) 异常名）`，例如：

```java
// execution就是动作关键字，它描述切入点的行为动作
// public是访问修饰符 User是返回值 com.linxuan.dao是包名 UserDao是类/接口名称 findById是方法名
// int是参数，如果参数由多个类型那么使用逗号隔开   异常名这里没有，可以省略
execution(public User com.linxuan.dao.UserDao.findById(int))
```

切入点表达式就是要找到需要被增强的方法，所以它就是对一个具体方法的描述。

**通配符**

方法的定义会有很多，所以如果每一个方法对应一个切入点表达式，那么会很麻烦。我们使用通配符描述切入点，简化之前的配置。

* `*`：单个独立的任意符号，可以独立出现，也可以作为前缀或者后缀的匹配符出现

  ```java
  // 匹配com.linxuan包下的任意包中的UserService类或接口中所有find开头的带有一个参数的方法。
  execution(public * com.linxuan.*.UserService.find*(*))
  ```

* `..`：多个连续的任意符号，可以独立出现，常用于简化包名与参数的书写

  ```java
  // 匹配com包下的任意包中的UserService类或接口中所有名称为findById的方法
  execution(public User com..UserService.findById(..))
  ```
  
* `+`：专用于匹配子类类型

  ```java
  // *Service+表示所有以Service结尾的接口的子类，很少使用
  execution(* *..*Service+.*(..))
  ```
  

**书写技巧**

所有代码按照标准规范开发，否则以下技巧全部失效：

1. 描述切入点通常描述接口，而不描述实现类，如果描述到实现类，就出现紧耦合了。
2. 访问控制修饰符针对接口开发均采用public描述（可省略访问控制修饰符描述）
3. 返回值类型对于增删改类使用精准类型加速匹配，对于查询类使用`*`通配快速描述
4. 包名书写尽量不使用`..`匹配，效率过低，常用`*`做单个包描述匹配，或精准匹配
5. 接口名/类名书写名称与模块相关的采用`*`匹配，例如`BookDao`书写成`*Dao`，绑定数据层接口名
6. 方法名书写以动词进行精准匹配，名词采用`*`匹配。`getById`书写成`getBy*`，`selectAll`书写成`selectAll`
7. 参数规则较为复杂，根据业务方法灵活调整
8. 通常不使用异常作为匹配规则

## 2.2 AOP通知类型

通知(Advice)：在切入点处执行的操作，也就是共性功能。通知就是要增强的内容，以方法的形式呈现。

AOP一共提供了5种通知类型：前置通知、后置通知、环绕通知(重点)、返回后通知(了解)、抛出异常后通知(了解)。前面提到的`@Before("pt()")`就是前置通知，会将通知添加到切入点方法执行的前面。

- 前置通知：追加功能到方法执行前，类似于在代码1或者代码2添加内容
- 后置通知：追加功能到方法执行后，不管方法执行的过程中有没有抛出异常都会执行，类似于在代码5添加内容
- 返回后通知：追加功能到方法执行后，只有方法正常执行结束后才进行，类似于在代码3添加内容，如果方法执行抛出异常，返回后通知将不会被添加
- 抛出异常后通知：追加功能到方法抛出异常后，只有方法执行出异常才进行，类似于在代码4添加内容，只有方法抛出异常后才会被添加
- 环绕通知：环绕通知功能比较强大，它可以追加功能到方法执行的前后，这也是比较常用的方式，它可以实现其他四种通知类型的功能。

<img src="..\图片\4-02【Spring】\3-1.png" />

环境介绍：

```java
public interface BookDao {
    // 这里的返回值修改为了Int类型，和上面的返回值不同。
    public int select();
    public void update();
}
```

```java
@Repository
public class BookDaoImpl implements BookDao {

    public int select() {
        System.out.println("book dao select is running ...");
        return 100;
    }

    public void update() {
        System.out.println("book dao update ...");
    }
}
```

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

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.update();
}
// book dao update ...
```

### 2.2.1 前置和后置

**前置通知**

在before方法上添加`@Before注解`

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
    private void pt(){}
    
    @Before("pt()")
    //此处也可以写成 @Before("MyAdvice.pt()")，不建议
    public void before() {
        System.out.println("before advice ...");
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.update();
}
// before advice ...
// book dao update ...
```

**后置通知**

在after方法上添加`@After注解`

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
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
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.update();
}
// before advice ...
// book dao update ...
// after advice ...
```

| 名称 | @Before                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义                               |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法前运行 |
| 属性 | value（默认）：切入点方法。例如：`@Before("pt()")`           |

| 名称 | @After                                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义                               |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法后运行 |
| 属性 | value（默认）：切入点方法。例如：`@After("pt()")`            |

### 2.2.2 环绕通知

**原始方法无返回值**

在around方法上方添加`@Around`注解：

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
    private void pt(){}
    
    @Around("pt()")
    public void around(){
        System.out.println("around before advice ...");
        System.out.println("around after advice ...");
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.update();
}
// around before advice ...
// around after advice ...
```

环绕通知的内容打印出来了，但是原始方法的内容却没有被执行。这是因为环绕通知需要在原始方法的前后进行增强，所以环绕通知就必须要能对原始操作进行调用。

实现方法如下：

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.linxuan.dao.BookDao.update())")
    private void pt(){}
    
    @Around("pt()")
    // 方法弄一个参数，这样可以调用原始操作
    public void around(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("around before advice ...");
        // 表示对原始操作的调用
        // proceed()要抛出异常
        pjp.proceed();
        System.out.println("around after advice ...");
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    bookDao.update();
}
// around before advice ...
// book dao update ...
// around after advice ...
```

**原始方法有返回值**

上面在进行环绕通知的时候，原始方法有返回值的处理，接下来修改为有返回值的切入点：

```java
@Repository
public class BookDaoImpl implements BookDao {

    public int select() {
        System.out.println("book dao select is running ...");
        return 100;
    }

    public void update() {
        System.out.println("book dao update ...");
    }
}
```

```java
@Component
@Aspect
public class MyAdvice {
    // 修改为有返回值的切入点，select方法的返回值是int类型的
    @Pointcut("execution(int com.linxuan.dao.BookDao.select())")
    private void pt(){}
    
    @Around("pt()")
    public void aroundSelect(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("around before advice ...");
        // 表示对原始操作的调用
        pjp.proceed();
        System.out.println("around after advice ...");
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    int num = bookDao.select();
    System.out.println(num);
}
// 报错：Null return value from advice does not match primitive return type for： public abstract int com.linxuan.dao.BookDao.select()
```

运行之后报错：空的返回不匹配原始方法的int返回。

原因：原始方法的返回值是int，而通知方法的返回值是void。`pjp.proceed();`标识对原始方法的调用，所以该通知方法也就等于

```java
@Around("pt()")
public void aroundSelect(ProceedingJoinPoint pjp) throws Throwable {
    System.out.println("around before advice ...");
    // pjp.proceed();等于下面两行的代码
    System.out.println("book dao select is running ...");
    return 100;
    System.out.println("around after advice ...");
}
```

所以`pjp.proceed()`是有返回值的，它的返回值就是100。如果使用环绕通知的话，要根据原始方法的返回值来设置环绕通知的返回值，具体解决方案为：

```java
@Component
@Aspect
public class MyAdvice {
    
    @Pointcut("execution(int com.linxuan.dao.BookDao.select())")
    private void pt(){}
    
    @Around("pt()")
    // 是Object类型更通用
    public Object aroundSelect(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("around before advice ...");
        // 表示对原始操作的调用
        Object ret = pjp.proceed();
        System.out.println("around after advice ...");
        // 在环绕通知中是可以对原始方法返回值进行修改的。假如这里返回200。那么原始方法返回值就变为了200。
        return ret;
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    int num = bookDao.select();
    System.out.println(num);
}
// around before advice ...
// book dao select is running ...
// around after advice ...
// 100
```

**环绕通知注意事项**

1. 环绕通知依赖形参`ProceedingJoinPoint`才能实现对原始方法的调用，进而实现原始方法调用前后同时添加通知
2. 通知中如果未使用`ProceedingJoinPoint`对原始方法进行调用将跳过原始方法的执行
3. 对原始方法的调用可以不接收返回值，通知方法设置成void即可，如果接收返回值，最好设定为Object类型
4. 原始方法的返回值如果是void类型，通知方法的返回值类型可以设置成void，也可以设置成Object
5. 由于无法预知原始方法运行后是否会抛出异常，因此环绕通知方法必须要处理Throwable异常

| 名称 | @Around                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义                               |
| 作用 | 设置当前通知方法与切入点之间的绑定关系，当前通知方法在原始切入点方法前后运行 |
| 属性 | value（默认）：切入点方法。例如：`@Around("pt()")`           |

### 2.2.3 返回后和异常后

<img src="..\图片\4-02【Spring】\3-1.png" />

返回后通知、抛出异常后通知、后置通知差别：

- 返回后通知是需要在原始方法正常执行后才会被执行
- 如果原始方法执行的过程中出现了异常，那么返回后通知是不会被执行，转而执行抛出异常后通知。
- 后置通知是不管原始方法有没有抛出异常都会被执行。

**返回后通知**

```java
@Component
@Aspect
public class MyAdvice {

    @Pointcut("execution(int com.linxuan.dao.BookDao.select())")
    private void pt(){}
    
    @AfterReturning("pt()")
    public void afterReturning() {
        System.out.println("afterReturning advice ...");
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao.select());
}
// book dao select is running ...
// afterReturning advice ...
// 100
```

**异常后通知**

```java
@Repository
public class BookDaoImpl implements BookDao {

    public int select() {
        System.out.println("book dao select is running ...");
        // 这里添加一个数学异常，这样执行这个的时候抛出异常才会执行异常后通知
        int i = 1 / 0;
        return 100;
    }

    public void update() {
        System.out.println("book dao update ...");
    }
}
```

```java
@Component
@Aspect
public class MyAdvice {
    
    @Pointcut("execution(int com.linxuan.dao.BookDao.select())")
    private void pt(){}
    
    @AfterThrowing("pt()")
    public void afterThrowing() {
        System.out.println("afterThrowing advice ...");
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao.select());
}
// book dao select is running ...
// afterThrowing advice ... 抛出异常后通知执行了
// Exception in thread "main" java.lang.ArithmeticException: / by zero 抛出异常
```

| 名称 | @AfterReturning                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义                               |
| 作用 | 设置当前通知方法与切入点之间绑定关系，当前通知方法在原始切入点方法正常执行完毕后执行 |
| 属性 | value（默认）：切入点方法。例如：`@AfterReturning("pt()")`   |

| 名称 | @AfterThrowing                                               |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，在通知方法的上方定义                               |
| 作用 | 设置当前通知方法与切入点之间绑定关系，当前通知方法在原始切入点方法运行抛出异常后执行 |
| 属性 | value（默认）：切入点方法。例如：`@AfterReturning("pt()")`   |

## 2.3 案例-测试MyBatis效率

案例：任意业务层接口执行均可显示其执行效率，也就是执行时间。使用环绕通知解决。

**Spring整合MyBatis、Junit环境搭建**

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

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan?useSSL=false
jdbc.username=root
jdbc.password=root
```

```xml
<!-- 导入依赖 -->
<dependencies>
    <!-- Spring环境 -->
    <!-- Spring基础依赖，里面依赖着spring-aop、spring-beans、spring-core、spring-expression-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
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
    </dependency>
</dependencies>
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

```java
package com.linxuan.dao;

public interface AccountDao {
    /**
     * 新增
     * @param account
     */
    @Insert("insert into tb_account(name，money) values(#{name}, #{money})")
    void save(Account account);

    /**
     * 根据用户ID删除信息
     * @param id
     */
    @Delete("delete from tb_account where id = #{id} ")
    void delete(Integer id);

    /**
     * 根据用户ID更新信息
     * @param account
     */
    @Update("update tb_account set name = #{name}, money = #{money} where id = #{id} ")
    void update(Account account);

    /**
     * 查询所有用户信息
     * @return List<Account>
     */
    @Select("select * from tb_account")
    List<Account> findAll();

    /**
     * 根据ID查询指定用户信息
     * @param id
     * @return
     */
    @Select("select * from tb_account where id = #{id} ")
    Account findById(Integer id);
}
```

```java
package com.linxuan.service;

public interface AccountService {
    /**
     * 新增
     * @param account
     */
    void save(Account account);
    
    /**
     * 根据用户ID删除信息
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据用户ID更新信息
     * @param account
     */
    void update(Account account);

    /**
     * 查询所有用户信息
     * @return List<Account>
     */
    List<Account> findAll();

    /**
     * 根据ID查询指定用户信息
     * @param id
     * @return
     */
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
// Jdbc配置类
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
// Mybatis配置类
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

```java
// Spring主配置类：SpringConfig
package com.linxuan.config;

// 标识为配置类
@Configuration
// 包扫描，扫描包及子包类上面的注解
@ComponentScan("com.linxuan")
// 引入配置文件
@PropertySource("classpath:jdbc.properties")
// 导入配置类
@Import({JdbcConfig.class, MybatisConfig.class})
public class SpringConfig {
}
```

```java
// 创建单元测试
package com.linxuan.service;

// 设置JUnit运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 设置JUnit加载Spring的核心配置类
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void testFindById(){
        Account ac = accountService.findById(2);
        System.out.println(ac);
    }

    @Test
    public void testFindAll(){
        List<Account> accounts = accountService.findAll();
        accounts.forEach(System.out::println);
    }
}
```

**AOP实现获取执行效率**

```xml
<!-- SpringAOP实现，Spring自带的不好用，所以使用这个来实现-->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.4</version>
</dependency>
```

```java
@Configuration
@ComponentScan("com.linxuan")
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class, MybatisConfig.class})
// 开启SpringAOP的注解功能
@EnableAspectJAutoProxy
public class SpringConfig {
}
```

```java
package com.linxuan.aop;

// 创建通知类，让其被Spring包扫描到，被Spring管理
@Component
// 标识该类是一个AOP的切面类
@Aspect
public class ProjectAdvice {

    // 配置切入点为业务层的所有方法
    @Pointcut("execution(* com.linxuan.service.*Service.*(..))")
    private void servicePt() {
    }

    // 切入点进行环绕通知
    @Around("servicePt()")
    // 通知方法，抽取的需要添加的共性功能方法
    public Object runSpeed(ProceedingJoinPoint pjp) throws Throwable {
        // 获取执行签名信息 import org.aspectj.lang.Signature;
        Signature signature = pjp.getSignature();
        // 通过签名获取执行操作名称(接口名)
        String className = signature.getDeclaringTypeName();
        // 通过签名获取执行操作名称(方法名)
        String methodName = signature.getName();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            pjp.proceed();
        }
        long end = System.currentTimeMillis();
        System.out.println("万次执行：" + className + "." + methodName + "为" + (end - start) + "ms");

        return null;
    }
}
```

```java
@Test
public void testFindById(){
    Account ac = accountService.findById(2);
}
// 万次执行：com.linxuan.service.AccountService.findById为6990ms
```

## 2.4 AOP通知获取数据

接下来说AOP中数据相关的内容，将从获取参数、获取返回值和获取异常三个方面来研究切入点的相关信息。

环境如下：

```xml
<!-- 导入依赖 -->
<dependencies>
    <!-- Spring环境 -->
    <!-- Spring基础依赖，里面依赖着spring-aop、spring-beans、spring-core、spring-expression-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- SpringAOP实现，Spring自带的不好用，所以使用这个来实现-->
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.9.4</version>
    </dependency>
</dependencies>   
```

```java
package com.linxuan.dao;

public interface BookDao {
    /**
     * 根据ID查询名称
     * @param id
     * @return
     */
    String findName(int id);
}
```

```java
package com.linxuan.dao.impl;

@Repository
public class BookDaoImpl implements BookDao {
    public String findName(int id) {
        System.out.println("id：" + id);
        return "linxuan";
    }
}
```

```java
package com.linxuan.config;

@Configuration
@ComponentScan("com.linxuan")
@EnableAspectJAutoProxy
public class SpringConfig {
}
```

```java
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

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    String name = bookDao.findName(100);
    System.out.println(name);
}
// before advice ...
// id：100
// afterReturning advice ...
// after advice ...
// linxuan
```

### 2.4.1 获取参数

获取切入点方法的参数，所有的通知类型都可以获取参数

* `JoinPoint`：适用于前置、后置、返回后、抛出异常后通知
* `ProceedingJoinPoint`：适用于环绕通知，`ProceedingJoinPoint`是`JoinPoint`类的子类。

**非环绕通知获取方式**

在方法上添加`JoinPoint`，通过`JoinPoint`来获取参数。使用`JoinPoint`的方式获取参数适用于`前置`、`后置`、`返回后`、`抛出异常后`通知。

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Before("pt()")
    public void before(JoinPoint jp) {
        // 参数以数组的形式被获取到
        Object[] args = jp.getArgs();
        System.out.println("参数为：" + Arrays.toString(args) + "，before advice ..." );
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao.findName(100));
}
// 参数为：[100]，before advice ...
// id：100
// linxuan
```

**环绕通知获取方式**

环绕通知使用的是`ProceedingJoinPoint`，它是`JoinPoint`类的子类，所以`ProceedingJoinPoint`类中应该也会有对应的`getArgs()`方法：

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        System.out.println(Arrays.toString(args));
        Object ret = pjp.proceed();
        return ret;
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao.findName(100));
}
// [100]
// id：100
// linxuan
```

`pjp.proceed()`方法是有两个重载方法：

```java
public interface ProceedingJoinPoint extends JoinPoint {
    // 无参
    public Object proceed() throws Throwable;
    // 有参
    public Object proceed(Object[] args) throws Throwable;
}
```

调用无参数的proceed，当原始方法有参数，会在调用的过程中自动传入参数。但是当需要修改原始方法的参数时，就只能采用带有参数的方法，如下：

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        System.out.println(Arrays.toString(args));
        args[0] = 666;
        Object ret = pjp.proceed(args);
        return ret;
    }
}
```

有了这个特性后，我们就可以在环绕通知中对原始方法的参数进行拦截过滤，避免由于参数的问题导致程序无法正确运行，保证代码的健壮性。

### 2.4.2 获取返回值

获取切入点方法返回值，前置和抛出异常后通知是没有返回值，后置通知可有可无，所以不做研究。对于返回值，只有返回后`AfterReturing`和环绕`Around`这两个通知类型可以获取。

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
        // 这个ret就是方法的返回值，可以调用pjp.proceed方法直接获取，也可以进行修改它
        Object ret = pjp.proceed(args);
        return ret;
    }
}
```

**返回后通知获取返回值**

<img src="..\图片\4-02【Spring】\3-1.png" />

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    // 返回通知是原始业务操作执行完之后不抛出异常就会执行的操作，这时候原始方法返回值，返回后通知获取该值
    // 获取方式就是用returning = ""，注意该值必须和参数名称相同，这样才能够注入到该方法里面
    @AfterReturning(value = "pt()", returning = "ret")
    // 这里的参数可以是String，但是Object更好。参数存在JoinPoint，那么该参数必须放在第一位，否则会出现报错。
    public void afterReturning(Object ret) {
        System.out.println("afterReturning advice ..." + ret);
    }
}
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookDao bookDao = ctx.getBean(BookDao.class);
    System.out.println(bookDao.findName(100));
}
// id：100
// afterReturning advice ...linxuan
// linxuan
```

### 2.4.3 获取异常

获取切入点方法运行异常信息，前置和返回后通知是不会有，后置通知可有可无，所以不做研究。对于获取抛出的异常，只有抛出异常后`AfterThrowing`和环绕`Around`这两个通知类型可以获取。

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
}
```

**抛出异常后通知获取异常**

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
```

```java
@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* com.linxuan.dao.BookDao.findName(..))")
    private void pt(){}

    // 名称保持一直：throwing = "t" == Throwable t，否则注入不进去
    @AfterThrowing(value = "pt()"，throwing = "t")
    public void afterThrowing(Throwable t) {
        // 抛出异常后通知是原始方法抛出异常就会执行的代码片段，所以这里获取抛出的异常，然后进行业务操作。
        System.out.println("afterThrowing advice ..." + t);
    }
}
```

# 第三章 Spring事务管理

首先来看一下事务：

- MySQL事务：事务是一组操作的集合，事务会把所有操作作为一个整体一起向系统提交或撤销操作请求，即这些操作要么同时成功，要么同时失败。在数据层保障一系列的数据库操作同成功同失败。
- Spring事务：在数据层或**业务层**保障一系列的数据库操作同成功同失败。

Spring的业务层也需要处理事务。例如：转账业务在数据层会有加前和减钱两次调用，如果把事务放在数据层，这就是两个事务了，无法保证转账的成功。但是，如果将事务放在业务层那就没有任何问题了。

Spring为了管理事务，提供了一个平台事务管理器`PlatformTransactionManager`

```java
public interface PlatformTransactionManager extends TransactionManager {
    TransactionStatus getTransaction(@Nullable TransactionDefinition var1) throws TransactionException;

    // commit提交事务
    void commit(TransactionStatus var1) throws TransactionException;

    // rollback回滚事务
    void rollback(TransactionStatus var1) throws TransactionException;
}
```

`PlatformTransactionManager`只是一个接口，Spring还为其提供了一个具体的实现：

```java
public class DataSourceTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {}
```

从名称上可以看出，我们只需要给它一个DataSource对象，它就可以在业务层管理事务。其内部采用的是JDBC的事务，所以说如果持久层采用的是JDBC相关的技术，就可以采用这个事务管理器来管理你的事务。Mybatis内部采用的正是JDBC的事务，所以之后Spring整合Mybatis就采用这个`DataSourceTransactionManager`事务管理器。

## 3.1 转账案例

需求： A账户减钱，B账户加钱。同时通过事务管理。

### 3.1.1 环境搭建

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
INSERT INTO tb_account VALUES(NULL, "林炫", 1000), (NULL, "陈沐阳", 1000);
# 查询数据
SELECT * FROM tb_account;
```

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan?useSSL=false
jdbc.username=root
jdbc.password=root
```

```xml
<!-- 导入依赖 -->
<dependencies>
    <!-- Spring环境 -->
    <!-- Spring基础依赖，里面依赖着spring-aop、spring-beans、spring-core、spring-expression-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.10.RELEASE</version>
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
    </dependency>
</dependencies>
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

```java
package com.linxuan.dao;

public interface AccountDao {

    /**
     * 加钱操作
     * @param name
     * @param money
     */
    @Update("update tb_account set money = money + #{money} where name = #{name}")
    void inMoney(@Param("name") String name, @Param("money") Double money);

    /**
     * 减钱操作
     * @param name
     * @param money
     */
    @Update("update tb_account set money = money - #{money} where name = #{name}")
    void outMoney(@Param("name") String name, @Param("money") Double money);
}
```

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

```java
// Jdbc配置类
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
// Mybatis配置类
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

```java
// Spring主配置类：SpringConfig
package com.linxuan.config;

// 标识为配置类
@Configuration
// 包扫描，扫描包及子包类上面的注解
@ComponentScan("com.linxuan")
// 引入配置文件
@PropertySource("classpath:jdbc.properties")
// 导入配置类
@Import({JdbcConfig.class, MybatisConfig.class})
public class SpringConfig {
}
```

```java
package com.linxuan.service;

// 设置JUnit运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 设置JUnit加载Spring的核心配置类
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void testTransfer() throws IOException {
        accountService.transfer("林炫", "陈沐阳", 100D);
    }
}
```

### 3.1.2 注解事务管理

运行单元测试类，会执行转账操作。这是正常情况下的运行结果，但是如果在转账的过程中出现了异常，如：

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    public void transfer(String out，String in ，Double money) {
        accountDao.outMoney(out，money);
        int i = 1 / 0;
        accountDao.inMoney(in，money);
    }
}
```

```java
@Test
public void testTransfer() throws IOException {
    accountService.transfer("林炫", "陈沐阳", 100D);
}
```

这个时候就模拟了转账过程中出现异常的情况，运行之后`林炫`账户为900而`陈沐阳`还是1000，100块钱凭空消失了。不管哪种情况，都是不允许出现的。Spring的事务管理就是用来解决这类问题的：

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    // 在需要被事务管理的方法上添加注解@Transactional，它可以写在接口类上、接口方法上、实现类上和实现类方法上
    // 建议写在实现类或者实现类方法上
    @Transactional
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
        int i = 1 / 0;
        accountDao.inMoney(in, money);
    }
}
```

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

    // 配置事务管理器，mybatis使用的是jdbc事务
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
```

```java
@Configuration
@ComponentScan("com.linxuan")
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class, MybatisConfig.class})
// 开启注解式事务驱动
@EnableTransactionManagement
public class SpringConfig {
}
```

```java
@Test
// 这时候运行该测试类，发现在转换的业务出现异常后，事务就可以控制回顾，保证数据的正确性。
public void testTransfer() throws IOException {
    accountService.transfer("林炫", "陈沐阳", 100D);
}
```

| 名称 | @EnableTransactionManagement                                 |
| ---- | ------------------------------------------------------------ |
| 类型 | 配置类注解，在Spring配置类上方定义                           |
| 替换 | 替换掉了`<tx:annotation-driven transaction-manager="txManager"/>` |
| 作用 | 设置当前Spring环境中开启注解式事务支持                       |

| 名称 | @Transactional                                               |
| ---- | ------------------------------------------------------------ |
| 类型 | 接口注解、类注解、方法注解，在业务层接口上方、业务层实现类上方、业务方法上方定义 |
| 替换 | 无                                                           |
| 作用 | 为当前业务层方法添加事务（如果设置在类或接口上方则类或接口中所有方法均添加事务） |

### 3.1.3 配置文件与注解混合

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    // 在需要被事务管理的方法上添加注解@Transactional，它可以写在接口类上、接口方法上、实现类上和实现类方法上
    // 建议写在实现类或者实现类方法上
    @Transactional
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
        int i = 1 / 0;
        accountDao.inMoney(in, money);
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 开启了tx命名空间 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/tx 
                           http://www.springframework.org/schema/tx/spring-tx.xsd
                           ">

    <!--因为是混合开发，所以这里需要配置-->
    <!-- 配置IOC注解解析器，等于@ComponentScan("com.linxuan") -->
    <context:component-scan base-package="com.linxuan"/>

    <!--开启注解式事务 等于EnableTransactionManagement-->
    <tx:annotation-driven transaction-manager="txManager"/>

    <!--事务管理器 等于JdbcConfig里面配置的bean对象-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
</beans>
```

```java
package com.linxuan.test;

// 设置JUnit运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 设置JUnit加载Spring的核心配置配置文件，因为是混合开发，所以加载核心配置文件
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void testTransfer() throws IOException {
        accountService.transfer("林炫", "陈沐阳", 100D);
    }
}
```

## 3.2 Spring事务角色

事务中的角色：事务管理员和事务协调员。

- 事务管理员：发起事务方，在Spring中通常指代业务层开启事务的方法
- 事务协调员：加入事务方，在Spring中通常指代数据层方法，也可以是业务层方法

```java
// 未开启事务前，且中间没有任何异常抛出
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

未开启Spring事务之前：`AccountDao`的`outMoney`操作会开启一个事务T1、`AccountDao`的`inMoney`会开启一个事务T2、`AccountService`的`transfer`没有事务管理，运行过程中如果没有抛出异常，则T1和T2都正常提交，数据正确。但是如果在两个方法中间抛出异常，T1因为执行成功提交事务，T2因为抛异常不会被执行，就会导致数据出现错误。

```java
// 开启事务后，中间有异常抛出
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

开启Spring的事务管理后：`transfer`上添加了`@Transactional`注解，在该方法上就会有一个事务T，`AccountDao`的`outMoney`和`inMoney`方法的事务加入到`transfer`的事务T中。这样就保证他们在同一个事务中，当业务层中出现异常，整个事务就会回滚，保证数据的准确性。


通过上面例子的分析，我们就可以得到如下概念：

- 事务管理员：发起事务方，在Spring中通常指代业务层开启事务的方法
- 事务协调员：加入事务方，在Spring中通常指代数据层方法，也可以是业务层方法

## 3.3 Spring事务属性

事务的这些属性都可以在`@Transactional`注解的参数上进行设置，属性如下：

|          属性          |             作用             |                   实例                   |
| :--------------------: | :--------------------------: | :--------------------------------------: |
|        readOnly        |      设置是否为只读事务      | readOnly=true 设置为只读事务（查询操作） |
|        timeout         |       设置事务超时时间       |           timeout=-1 永不超时            |
|      rollbackFor       |  设置事务回滚异常（class）   |   rollbackFor=NullPointException.class   |
|  rollbackForClassName  |  设置事务回滚异常（String）  |            同上，格式为字符串            |
|     noRollbackFor      | 设置事务不回滚异常（calss）  |  noRollbackFor=NullPointException.class  |
| noRollbackForClassName | 设置事务不回滚异常（String） |            同上，格式为字符串            |
|       isolation        |       设置事务隔离级别       |       isolation=Isolation.DEFAULT        |
|      propagation       |       设置事务传播行为       |                   ...                    |

* `readOnly`：true只读事务，false读写事务，增删改要设为false，查询设为true。
* `timeout`：设置超时时间单位秒，在多长时间之内事务没有提交成功就自动回滚，-1表示不设置超时时间。
* `rollbackFor`：当出现指定异常进行事务回滚，例如出现`IOException`异常。
* `rollbackForClassName`：等同于`rollbackFor`，只不过属性为异常的类全名字符串
* `noRollbackFor`：当出现指定异常不进行事务回滚
* `noRollbackForClassName`：等同于`noRollbackFor`，只不过属性为异常的类全名字符串
* `isolation`：设置事务的隔离级别。
    * `DEFAULT`   ：默认隔离级别， 会采用数据库的隔离级别
    * `READ_UNCOMMITTED` ： 读未提交
    * `READ_COMMITTED` ： 读已提交
    * `REPEATABLE_READ` ： 重复读取
    * `SERIALIZABLE`： 串行化

`rollbackFor`是指定回滚异常，对于异常事务不应该都回滚么，为什么还要指定？这块需要更正一个知识点，并不是所有的异常都会回滚事务，比如下面的代码就不会回滚：

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    
	@Transactional
    public void transfer(String out, String in, Double money) throws IOException{
        accountDao.outMoney(out，money);
        // 这个异常 事务会回滚
        // int i = 1/0; 
        if(true){
            // 这个异常事务就不会回滚。
            throw new IOException(); 
        }
        accountDao.inMoney(in，money);
    }

}
```

Spring的事务只会对`Error异常`和`RuntimeException异常`及其子类进行事务回滚，其他的异常类型是不会回滚的，对应`IOException`不符合上述条件所以不回滚。IOException是编译器异常/受检查异常。

<img src="..\图片\4-02【Spring】\3-2异常分类.png" />

可以使用`rollbackFor`属性来设置出现`IOException`异常回滚。

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    // 设置属性rollbackFor = {IOException.class}，遇到IOException会回滚
    @Transactional(rollbackFor = {IOException.class})
    public void transfer(String out，String in ，Double money) throws IOException{
        accountDao.outMoney(out，money);
        if(true){
            throw new IOException(); 
        }
        accountDao.inMoney(in，money);
    }

}
```


## 3.3 案例-转账追加日志

在前面的转账案例的基础上添加新的需求，无论是否完成转账都要记录日志。

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;
CREATE TABLE IF NOT EXISTS tb_log(
   id INT PRIMARY KEY AUTO_INCREMENT,
   info VARCHAR(255),
   createDate DATETIME
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_log;
```

```java
// 添加LogDao接口
public interface LogDao {
    @Insert("insert into tb_log (info, createDate) values(#{info}, now())")
    void log(String info);
}
```

```java
public interface LogService {
    void log(String out, String in, Double money);
}
```

```java
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

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private LogService logService;

    @Override
    @Transactional
    public void transfer(String out, String in, Double money){
        try {
            accountDao.outMoney(out, money);
            accountDao.inMoney(in, money);
        } finally {
            // 转账的业务操作中添加日志
            logService.log(out, in, money);
        }
    }
}
```

```java
@Test
public void testTransfer() throws IOException {
    accountService.transfer("林炫", "陈沐阳", 100D);
}
```

* 当程序正常运行，`tb_account`表中转账成功，`tb_log`表中日志记录成功

* 当转账业务之间出现异常(`int i = 1 / 0`)，转账失败，`tb_account`成功回滚，但是`tb_log`表未添加数据。


当转账业务操作中出现异常的时候，结果和我们设想的不一样，这是因为日志的记录与转账操作隶属同一个事务，同成功同失败。但是我们就想要无论转账操作是否成功，日志必须保留，所以这就要用到事务的传播行为了。

## 3.4 事务传播行为

事务传播行为指的是：事务协调员对事务管理员所携带事务的处理态度。

- 事务管理员：发起事务方，在Spring中通常指代业务层开启事务的方法
- 事务协调员：加入事务方，在Spring中通常指代数据层方法，也可以是业务层方法

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
           logDao.log("转账操作由" + out + "到" + in + "，金额：" + money);
       }
   }
   ```

   运行后，就能实现我们想要的结果，不管转账是否成功，都会记录日志。

2. 事务传播行为的可选值

   <img src="..\图片\4-02【Spring】\3-7.png" />

对于我们开发实际中使用的话，因为默认值需要事务是常态的。根据开发过程选择其他的就可以了，例如案例中需要新事务就需要手工配置。其实入账和出账操作上也有事务，采用的就是默认值。

