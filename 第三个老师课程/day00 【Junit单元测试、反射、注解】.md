<!--P458-->

# 第一章 Junit单元测试

<!--P459-->

测试分类：

1. 黑盒测试：不需要写代码，给输入值，看看程序是否能够输出期望的值。
2. 白盒测试：需要写代码，关注程序具体的执行流程。(Junit单元测试就是白盒测试)。

![](D:\Java\笔记\图片\3-day00 【Junit单元测试、反射、注解】\1.png)

## 1.1 Junit使用

<!--P460-->

<!--subtract 减-->

### 步骤

1. 定义一个测试类（测试用例）
   * 建议：
     * 测试类名命名规则：被测试的类名 + Test		例如：CalculatorTest
     * 包名命名规则：XXX.XXX.XXX.test                       例如：cn.com.test
2. 在测试类里面定义测试方法（测试方法可以独立运行，点击旁边的绿色小三角即可）
   * 建议：
     * 方法名命名规则：test + 测试的方法名			例如：testAdd()
     * 返回值：void
     * 参数列表：空参
   * 注意：
     * 测试方法要使用public修饰
3. 给测试方法加上注解@Test
4. 导入Junit依赖环境
5. 运行测试方法，检查判定结果（测试方法可以独立运行，点击旁边的绿色小三角即可）

### 判定结果

* 红色：失败

* 绿色：成功

* 一般我们会使用断言操作来处理结果

  Assert.assertEquals(期望的结果，result);

  如果结果与我们期望的结果不同，会抛出异常，变成红色。

**被测试类**

```
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }
}
```

**测试类**

```java
import cn.com.junit.Calculator;
import org.junit.Assert;
import org.junit.Test;

// 定义测试类
public class CalculatorTest {

    // 定义测试方法 加上注解@Test
    @Test
    public void testAdd() {
        // 创建对象，使用对象的方法
        Calculator c = new Calculator();
        int result = c.add(1, 2);

        // 断言操作
        Assert.assertEquals(3, result);
    }

    @Test
    public void testSub() {
        // 创建对象，使用对象的方法
        Calculator c = new Calculator();
        int result = c.sub(1, 2);

        // 断言操作
        // 期待的值与结果不一样，会变成红色，报错
        Assert.assertEquals(3, result);
    }
}
```

### 补充注解

<!--P461-->

当我们在测试的时候会有很多重复的操作，例如IO流中的获取资源和释放资源。每当测试一个方法就需要重复操作。有注解可以帮助我们。

* @Before：
  * 修饰的成员方法会在测试之前被自动执行
  * 初始化方法：用于资源申请，所有测试方法在执行之前都会先执行该方法
* @After：
  * 修饰的成员方法会在测试方法执行之后自动被执行
  * 释放资源方法：在所有测试方法执行完成之后，都会自动执行该方法

**@After注解的方法没有必要放在最后，两者放在前面就可以了，最后会自动执行这个注解的方法的。**

```java
@Before
public void init() {
    System.out.println("init...");
}

@After
public void close() {
    System.out.println("close...");
}
```

# 第二章 反射

<!--P462-->

反射：框架设计的灵魂

![](D:\Java\笔记\图片\3-day00 【Junit单元测试、反射、注解】\2.png)

## 2.1 框架

* 概念：半成品软件。可以再框架的基础上面进行软件的开发，简化代码

## 2.2 反射

* 概念：将类的各个组成部分封装为其他对象，这就是反射机制

**反射的好处**

1. 可以再程序运行过程中，操作这些对象
2. 可以解耦，降低耦合性，提高程序的可扩展性能

<!--P467 顺序有错误，467， 468， 463-->

## 2.3 获取Class对象的方式

1. Class.forName("全类名")：将字节码文件加载进内存中，返回Class对象。
   * 多用于配置文件，将类名定义在配置文件中。读取文件，加载类
2. 类名.class：通过类名的属性class获取。
   * 多用于参数的传递
3. 对象.getClass()：getClass()方法在Object类中定义着。
   * 多用于对象的获取字节码的方式

同一个字节码文件(*.class)在一个程序运行过程中，只会被加载一次，不论通过哪一种方式获取的Class对象都是同一个。

**代码：**

首先创建一个完整的Person类

```java
public class Person {
    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

再创建主方法

```java
import cn.com.domain.Person;

public class Demo01Reflect {
    public static void main(String[] args) throws ClassNotFoundException {
        // 1. Class.forName("全类名")：将字节码文件加载进内存中，返回Class对象。
        Class cls1 = Class.forName("cn.com.domain.Person");
        System.out.println(cls1);

        // 2. 类名.class：通过类名的属性class获取。
        Class cls2 = Person.class;
        System.out.println(cls2);

        // 3. 对象.getClass()：getClass()方法在Object类中定义着。
        Person p = new Person();
        Class cls3 = p.getClass();
        System.out.println(cls3);

        //  通过== 比较三个对象  ==会比较他们的地址
        System.out.println(cls1 == cls2);// true
        System.out.println(cls1 == cls3);// true
    }
}
```

## 2.4 获取Class对象的功能

<!--P468-->

### 获取成员变量们

<!--P463-->

* `Field[] getFields()`：获取所有public修饰符修饰的成员变量。
* `Field getField(String name)`：获取指定名称的public修饰的成员变量。

* `Field[] getDeclaredFields()`：获取所有的成员变量，不用考虑修饰符。
* `Field getDeclaredField(String name)`：获取指定的成员变量，不用考虑修饰符。

FIled：成员变量

获取之后可以对其进行的操作：

1. 设置值 `void set(Object obj, Object value)`
2. 获取值 `get(Object obj)`
3. 忽略访问权限修饰符的安全检查 `setAccessible(true)`：暴力反射**（对于private可以采取暴力忽略，后续获取也都可以使用）**

```java
import cn.com.domain.Person;
import java.lang.reflect.Field;

public class Demo02Reflect {
    public static void main(String[] args) throws Exception {
        // 获取class对象 类名.class：通过类名的属性class获取。
        Class cls = Person.class;

        // Field[] getFields()：获取所有public修饰符修饰的成员变量。
        // 需要对Person类进行一下修改，四个权限修饰符都加进去，并且重写toString方法
        Field[] fields = cls.getFields();
        for (Field field : fields) {
            System.out.println(field); // public java.lang.String cn.com.domain.Person.a
        }

        // Field getField(String name)：获取指定名称的public修饰的成员变量。
        Field a = cls.getField("a");
        // 获取成员变量后对其进行操作，打印输出成员变量a的值，使用get方法
        // 获取值 get(Object obj) 里面需要传递一个对象，成员变量在Person类里面，需要传递Person对象
        Person p = new Person();
        Object value = a.get(p);
        System.out.println(value);
        // 设置a的值
        a.set(p, "林炫");
        System.out.println(p);



        // Field[] getDeclaredFields()：获取所有的成员变量，不用考虑修饰符。
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField);
        }

        // Field getDeclaredField(String name)：获取指定的成员变量，不用考虑修饰符。
        Field c = cls.getDeclaredField("c");
        c.setAccessible(true); // 忽略访问权限修饰符的安全检查 setAccessible(true)：暴力反射
        Object o1 = c.get(p);
        System.out.println(o1);
    }
}
```
### 获取构造方法们

<!--P464-->

* `Constructor<?>[] getConstructors()`：获取所有public修饰的构造方法
* `Constructor<T> getConstructor(Class<?>... parameterTypes)`：通过参数获取指定构造方法

* `Constructor<?>[] getDeclaredConstructors()`：获取所有构造方法
* `Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes)`：获取指定构造方法

Constructor：构造方法

* 作用：创建对象
  * `T newInstance(Object... initargs)`
  * 如果使用空参数构造方法创建对象，操作可以简化**（已弃用，不用关心）**

```java
import cn.com.domain.Person;

import java.lang.reflect.Constructor;

public class Demo03Reflect {
    public static void main(String[] args) throws Exception {
        // 获取Person的Class对象
        Class personClass = Person.class;

        // Constructor<T> getConstructor(Class<?>... parameterTypes)
        // 全参构造方法
        Constructor constructor1 = personClass.getConstructor(String.class, int.class);
        System.out.println(constructor1);
        // 创建对象
        Object person1 = constructor1.newInstance("张三", 23);
        System.out.println(person1);

        System.out.println("================");

        // 无参构造方法
        Constructor constructor2 = personClass.getConstructor();
        System.out.println(constructor2);
        // 创建对象
        Object person2 = constructor2.newInstance();
        System.out.println(person2);
    }
}
```

### 获取成员方法们

<!--P465 1.19-->

* `Method[] getMethods()`：获取所有的public修饰符修饰的方法

  **虽然获取方法所在的类没有继承任何类，但是它继承着Object类，所以如果使用该方法获取方法会获取到Object类所包含的方法**

  ```java
  public java.lang.String cn.com.domain.Person.getName()
  public java.lang.String cn.com.domain.Person.toString()
  public void cn.com.domain.Person.setName(java.lang.String)
  public void cn.com.domain.Person.eat()
  public void cn.com.domain.Person.eat(java.lang.String)
  public void cn.com.domain.Person.setAge(int)
  public int cn.com.domain.Person.getAge()
  public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
  public final void java.lang.Object.wait() throws java.lang.InterruptedException
  public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
  public boolean java.lang.Object.equals(java.lang.Object)
  public native int java.lang.Object.hashCode()
  public final native java.lang.Class java.lang.Object.getClass()
  public final native void java.lang.Object.notify()
  public final native void java.lang.Object.notifyAll()
  ```

* `Method getMethod(String name, Class<?>... parameterTypes)`：获取指定名称的方法；

* `Method[] getDeclaredMethods()`：获取所有的方法
* `Method getDeclaredMethod(String name, Class<?>... parameterTypes)`：获取指定名称的方法

Method：方法

* 获取成员方法之后可以让其执行
  * `Object invoke(Object obj, Object...args)`：执行方法
* 获取方法名称：
  * `String getName()`：获取方法名称

```java
import cn.com.domain.Person;

import java.lang.reflect.Method;

public class Demo04Reflect {
    public static void main(String[] args) throws Exception {
        // 获取Person的Class对象
        Class personClass = Person.class;

        // Method getMethod(String name, Class<?>... parameterTypes)：获取指定名称的方法；
        // 获取eat无参方法，打印方法
        Method eat = personClass.getMethod("eat");
        System.out.println(eat);
        // 执行方法
        Person p = new Person();
        eat.invoke(p);

        // 获取eat字符串参数，执行该方法并传递参数
        Method eat1 = personClass.getMethod("eat", String.class);
        eat1.invoke(p, "饭");

        // Method[] getMethods()：获取所有的public修饰符修饰的方法
        Method[] methods = personClass.getMethods();
        for (Method method : methods) {
            System.out.println(method);
            // 获取方法名称
            String name = method.getName();
            // System.out.println(name);
            // 暴力反射
            // method.setAccessible(true);
        }
    }
}
```

### 获取类名

* `String getName()`：获取类名

```java
import cn.com.domain.Person;

public class Demo05Reflect {
    public static void main(String[] args) {
        Class personClass = Person.class;

        // 获取类名
        String name = personClass.getName();
        System.out.println(name);
    }
}
```

## 2.5 “框架”案例

<!--P466-->

### 需求

写一个“框架”， 在不改变该类的任何代码的前提下，改动配置文件，帮助我们创建任意类的对象，并且执行其中任意的方法

### 实现

**步骤**

1. 将需要创建的对象的全类名和需要执行的方法定义在配置文件当中
2. 在程序中加载读取配置文件
3. 使用反射技术来加载类文件进内存
4. 创建对象
5. 执行

**代码**

```java
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

public class Demo06ReflectTest {
    public static void main(String[] args) throws Exception {

        // 加载配置文件
        // 创建Properties对象， 调用对象的方法来加载配置文件， load方法需要传递一个字节或者字符流
        Properties pro = new Properties();
        // 获取class目录下面的配置文件
        // 使用类加载器来完成，该类.class.getClassLoader() 这样就获取了字节码文件对应的加载器， 由加载器将字节码文件加载进去内存
        ClassLoader classLoader = Demo06ReflectTest.class.getClassLoader();
        // 找到pro.properties文件
        InputStream is = classLoader.getResourceAsStream("pro.properties");
        pro.load(is);

        // 获取配置文件中定义的数据
        String className = pro.getProperty("className");
        String methodName = pro.getProperty("methodName");

        // 加载该类进内存
        Class cls = Class.forName(className);
        // 创建对象
        Constructor constructor = cls.getConstructor();
        Object obj = constructor.newInstance();
        // 获取方法对象
        Method method = cls.getMethod(methodName);
        // 执行方法
        method.invoke(obj);
    }
}
```

# 第三章 注解

<!--P469-->

注解概念：说明程序。给计算机看。

## 3.1 注解

从JDK5开始,Java增加对元数据的支持，也就是注解，注解与注释是有一定区别的，可以把注解理解为代码里的特殊标记，这些标记可以在编译，类加载，运行时被读取，并执行相应的处理。

通过注解开发人员可以在不改变原有代码和逻辑的情况下在源代码中嵌入补充信息。

概念描述

* JDK1.5之后的新特性
* 说明程序
* 使用注解：`@ + 注解名称`

作用分类：

1. 编写文档：通过代码里面标识的注解生成文档【生成文段doc文档】
2. 代码分析：通过代码里面标识的注解对代码进行分析【使用反射】
3. 编译检查：通过代码里面标识的注解让编译器能够实现基本的编译检查【`@Override`】

## 3.2 JDK中预定义的一些注解

<!--P470-->

* `@Override`：检查被该注解标注的方法是否是继承自父类/接口的

* `@Deprecated`：该注解标注的内容，标识已经过时

  虽然已经过时，但是仍然可以使用，就是被划上了横线

* `@SuppressWarnings`：压制警告

  需要传递参数，一般传递all		`@suppressWarnings("all")`

```java
@SuppressWarnings("all")
public class Demo01Annotation {

    // @Override注解 检查是否重写 因为所有子类继承Object类，所以重写toString方法
    @Override
    public String toString() {
        return super.toString();
    }

    @Deprecated
    public void show01() {
        // 有缺陷
    }

    public void show02() {
        // 没有缺陷
    }

    public void demo() {
        // 仍然可以使用show01方法 只是会划上一条线
        show01();
    }
}
```

## 3.3 自定义注解

<!--P471-->

### 格式

```java
元注解
public @interface 注解名称{}
```

### 本质

<font color = "red">**注解本质上就是一个接口，该接口默认继承Annotation接口**</font>

```java
publi interface 接口名称 extends java.lang.annotation.Annotation{}
```

### 属性

<!--P472-->

属性：接口中定义的成员方法

**属性的返回值类型要求有下列取值，其他的类型都不可以：**

* 基本数据类型
* String
* 枚举
* 注解
* 以上类型的数组

```java
// 创建一个枚举类，为了测试注解中的参数可以是枚举
public enum Person {
    p1, p2;
}
```

```java
// 创建一个注解，测试注解中属性的参数可以是注解
public @interface MyAnnotation2 {
}
```

```java
// 定义自己的注解 名称是MyAnnotation
public @interface MyAnnotation {

    // 不允许这样的返回值存在
    // void age();

    // 基本数据类型返回值
    int age();
    // 字符串数据类型返回值
    String name();
    // 枚举类型返回值
    Person per();
    // 注解类型返回值
    MyAnnotation2 ann();
    // 字符串类型数组返回值
    String[] strs();
}
```

**为属性赋值：**

* int类型赋值。

  ```java
  // 创建测试类来使用注解，对注解属性赋值
  // 当注解里面只有一个，返回值为int类型的属性
  // 从这里大概可以理解为什么叫做属性了，所以设置注解的属性的时候可以设置的像属性一点，而不是像方法
  @MyAnnotation(age = 1)
  public class Demo02Annotation {
  }
  ```

* 如果定义属性的时候，使用default关键字给属性默认初始化值，则使用注解的时候可以不进行赋值。

  ```java
  // 对于name不用赋值，已经提前赋值了
  @MyAnnotation(age = 2)
  public class Demo02Annotation {
  }
  ```

  ```java
  public @interface MyAnnotation {
      int age();
  
      // 如果定义属性的时候，使用default关键字给属性默认初始化值，则使用注解的时候可以不进行赋值
      String name() default "林炫";
  }
  ```

* 如果只有一个属性需要赋值，并且属性的名称是value，那么value可以省略，直接定义值就可以了。

  ```java
  @MyAnnotation(2)
  public class Demo02Annotation {
  }
  ```

  ```java
  public @interface MyAnnotation {
      int value();
  }
  ```

* 各种类型进行赋值

  ```java
  @MyAnnotation(value = 2, per = Person.p1, ann = @MyAnnotation2, strs = {"ab", "bbb"})
  public class Demo02Annotation {
  }
  ```

  ```java
  public @interface MyAnnotation {
      int value();
      Person per();
      MyAnnotation2 ann();
      String[] strs();
  }
  ```

* 数组赋值的时候，值使用{}包裹。如果数组中只有一个值，那么{}省略。

  ```java
  @MyAnnotation(strs = "bbb")
  public class Demo02Annotation {
  }
  ```

  ```java
  public @interface MyAnnotation {
      String[] strs();
  }
  ```

## 3.4 元注解

<!--P473-->

元注解：用于描述注解的注解

* `@Target`：描述注解能够作用的位置

  ElementType取值：

  * `TYPE`：可以作用于类上面
  * `METHOD`：可以作用于方法上面
  * `FILED`：可以作用于成员变量上面

  ```java
  @Annotation
  public class Person {
      // @Annotation
      // 不能在这里注解，会报错
      public void show() {
          
      }
  }
  ```

  ```java
  import java.lang.annotation.ElementType;
  import java.lang.annotation.Target;
  // 定义自己的注解
  // 表示该注解只能用于类上面
  @Target(value = {ElementType.TYPE})
  public @interface Annotation {
  }
  ```

* `@Retention`：描述注解被保留的阶段

  * `@Retention(RetentionPolicy.RUNTIME)`：当前被描述的注解，会保留到class字节码文件中，并被JVM读取到。

* `@Documented`：描述注解是否被抽取到api文档中

* `@Inherited`：描述注解是否被子类继承

## 3.5 在程序中使用（解析）注解

<!--P474 1.20 P474-P475 P483-未知-->

注解可以用来替换配置文件

### 重写“框架案例”

写一个“框架”， 在不改变该类的任何代码的前提下，改动注解，帮助我们创建任意类的对象，并且执行其中任意的方法

**Person类：**

```java
package cn.com.demo04.annotation;

// 创建一个Person类
// 改动注解，帮助我们创建Person类的对象，并且执行其中show的方法
public class Person {
    public void show() {
        System.out.println("Person...");
    }
}
```

**创建注解：**

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
// 创建一个我们自己的注解
public @interface MyAnnotation {
    String className();
    String methodName();
}
```

**解析注解，创建对象，调用方法**

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// 解析注解，创建对象，调用方法
@MyAnnotation(className = "cn.com.demo04.annotation.Person", methodName = "show")
public class AnnotationTest {
    public static void main(String[] args) throws Exception {

        // 解析注解
        // 获取该类的字节码文件对象
        Class<AnnotationTest> annotationTestClass = AnnotationTest.class;
        // 调用该对象的方法，获取注解对象
        MyAnnotation annotation = annotationTestClass.getAnnotation(MyAnnotation.class);
        // 调用注解对象中定义的抽象方法，获取返回值
        String className = annotation.className();
        String methodName = annotation.methodName();
        // 打印输出
        System.out.println(className);
        System.out.println(methodName);

        // 加载该类进内存
        Class cls = Class.forName(className);
        // 创建对象
        Constructor constructor = cls.getConstructor();
        Object obj = constructor.newInstance();
        // 获取方法对象
        Method method = cls.getMethod(methodName);
        // 执行方法
        method.invoke(obj);

    }
}
```

### 体现注解好处案例

**创建自己的注解**

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 创建自己注解，MyCheck
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCheck {
}
```

**定义注解的操作**

```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestMyCheck {
    public static void main(String[] args) throws IOException {
        // 创建计算器对象
        Calculator calculator = new Calculator();
        // 获取字节码文件对象
        Class cls = calculator.getClass();
        // 获取所有的方法
        Method[] methods = cls.getMethods();

        int count = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter("bug.txt"));

        for (Method method : methods) {
            // 判断方法上面是否有Check注解
            if (method.isAnnotationPresent(MyCheck.class)) {
                // 有就执行
                try {
                    method.invoke(calculator);
                } catch (Exception e) {
                    // 捕获异常

                    // 记录到文件中去
                    count++;

                    bw.write(method.getName() + "方法出异常了");
                    bw.newLine();
                    bw.write("异常的名称：" + e.getCause().getClass().getSimpleName());
                    bw.newLine();
                    bw.write("异常的原因：" + e.getCause().getMessage());
                    bw.newLine();
                    bw.write("=============================");
                    bw.newLine();
                }
            }
        }

        bw.write("本次操作共有 " + count + " 个异常");
        bw.flush();
        bw.close();
    }
}
```

**创建计算器类来使用注解**

```java
// 计算器类
public class Calculator {

    @MyCheck
    public void add() {
        System.out.println("1 + 0 = " + (1 + 0));
    }

    @MyCheck
    public void sub() {
        System.out.println("1 - 0 = " + (1 - 0));
    }

    @MyCheck
    public void mul() {
        System.out.println("1 * 0 = " + (1 * 0));
    }

    @MyCheck
    public void div() {
        System.out.println("1 / 0 = " + (1 / 0));
    }

    public void show() {
        System.out.println("...");
    }
}
```

## 3.5 小结

1. 以后大多数使用注解，而不是自定义注解。
2. 注解给**编译器**和**解析程序**使用
3. 注解不是程序的一部分，注解只是一个标签