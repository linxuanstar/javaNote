# 第三章 Lambda表达式

## 3.1 函数式编程思想概述

<!--P335-->

数学中，函数式有输入量、输出量的一套计算方案，也就是”拿什么东西做什么事情“。相对而言，面向对象过分强调”必须通过对象的形式来做事情“，而函数式思想尽量忽略面向对象的复杂语法——强调做什么，而不是以什么形式做。

## 3.2 冗余的Runnable代码

<!--P336 12.27-->

### 传统写法

当需要启动一个线程去完成任务时，通常会通过`java.lang.Runnable`接口来定义任务内容，并使用`java.lang.Thread`类来启动该线程。代码如下：

```java
public class Demo01Runnable {
    public static void main(String[] args) {
        // 创建Runnable接口的实现类对象
        RunnableImpl run = new RunnableImpl();
        // 创建Thread类对象，构造方法中传递Runnable接口的实现类
        Thread t = new Thread(run);
        // 调用start方法开启新线程，执行run方法
        t.start();

        // 简化代码，使用匿名内部类，实现多线程程序
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "新线程创建了");
            }
        };
        new Thread(r).start();

        // 简化代码
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "新线程创建了");
            }
        }).start();

    }
}


public class RunnableImpl implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "新线程创建了");
    }
}
```

### 代码分析

对于`Runnable`的匿名内部类用法，可以分析出几点内容：

* `Thread`类需要`Runnable`接口作为参数，其中的抽象run方法是用来指定线程任务内容的核心；
* 为了指定`run`的方法体，**不得不需要**`Runnable`接口的实现类；
* 为了省去定义一个`RunnableImpl`实现类的麻烦，**不得不**使用匿名内部类；
* 必须覆盖重写抽象`run`方法，所以方法名称、方法参数、方法返回值**不得不**再写一遍，且不能写错；
* 而实际上，**似乎只有方法体才是关键所在**。

## 3.3 编程思想转换

### 做什么，而不是怎么做

我们真的希望创建一个匿名内部类对象吗？不。我们只是为了做这件事而不得不创建一个对象，我们真正希望做的事情是：将run方法体内的代码传递给Thread类知晓。

**传递一段代码**——这才是我们真正的目的。而创建对象只是受限于面向对象语法而不得不采取的一种手段方式。

## 3.4 体验Lambda的更优写法

<!--P337-->

```java
public class Demo02Lambda {
    public static void main(String[] args) {
        // 使用Lambda表达式，实现多线程
        new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "新线程创建了");
            }
        ).start();
    }
}
```

## 3.5 回顾匿名内部类

<!--P338-->

```java
    // 使用匿名内部类方式，实现多线程
    new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "新线程创建了");
        }
    }).start();
```

### 匿名内部类的好处与弊端

一方面，匿名内部类可以帮我们省去实现类的定义；另一方面，匿名内部类的语法——确实太复杂了！

### 语义分析

仔细分析代码中语义，`Runnable`接口只有一个`run`方法的定义：

* `public abstract void run()`;

即制定了一种做事情的方案（其实就是一个函数）：

* **无参数**：不需要任何条件即可执行该方案。
* **无返回值**：该方案不产生任何结果。
* **代码块（方法体）**：该方案的具体执行步骤。

同样的语义体现在`Lambda`语法中，要更加的简单：

```java
() -> System.out.println("多线程任务执行！");
```

* 前面的一堆小括号即**run方法的参数（无）**，代表不需要任何条件。
* 中间的一个箭头代表将前面的参数传递给后面的代码。
* 后面的输出语句即业务逻辑代码。

## 3.6 Lambda标准格式

<!--P339-->

Lambda省去面向对象的条条框框，格式由**3部分**组成：

* 一些参数
* 一个箭头
* 一段代码

Lambda表达式的**标准格式**是：

```java
（参数类型 参数名称） -> {代码语句}
```

格式说明：

* 小括号内的语法与传统方法参数列表一致：无参数则留空；多个参数则用逗号分隔。
* `->`是引入的语法格式，代表指向动作。
* 大括号内的语法与传统方法体要求基本一致。

## 3.7 练习：使用Lambda标准格式(无参无返回)

给定一个厨子Cook接口，内含唯一的抽象方法makeFood，且无参数，无返回值

```java
public interface Cook {
    // 定义一个Cook接口，内含唯一的抽象方法makeFood
    public abstract void makeFood();
}
```

使用Lambda标准格式调用invokeCook方法，打印输出“吃饭了”字样

```java
public class Demo01Cook {
    public static void main(String[] args) {
        // 调用invokeCook方法，参数是Cook接口，传递Cook接口的匿名内部类对象
        invokeCook(new Cook() {
            @Override
            public void makeFood() {
                System.out.println("吃饭了");
            }
        });

        // 使用Lambda表达式，简化匿名内部类对象的缩写
        invokeCook(() -> {
            System.out.println("吃饭了");
        });
    }

    // 定义一个方法，参数传递Cook接口，方法内部调用Cook接口中的方法makeFood
    public static void invokeCook(Cook cook) {
        cook.makeFood();
    }
}
```

## 3.8 Lambda的参数和返回值

<!--P340-->

需求：

使用数组存储多个Person对象

对数组中的Person对象使用Arrays中的sort方法进行升序排序

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

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
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
}
```

```java
import java.util.Arrays;
import java.util.Comparator;

/*
    Lambda表达式有参数有返回值的练习
    需求：
        使用数组存储多个Person对象
        对数组中的Person对象使用Arrays中的sort方法进行升序排序
 */
public class Demo01Arrays {
    public static void main(String[] args) {
        // 使用数组存储多个Person对象
        Person[] arr = {
                new Person("迪丽热巴", 20),
                new Person("古力娜扎",18),
                new Person("稀里哗啦", 23)
        };

        // 对数组中的Person对象使用Arrays的sort方法通过年龄进行升序（前边-后边）排序
        /*Arrays.sort(arr, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge();
            }
        });*/

        // 使用Lambda表达式，简化匿名内部类
        Arrays.sort(arr, (Person o1, Person o2) -> {
            return o1.getAge() - o2.getAge();
        });

        // 遍历数组
        for (Person p : arr) {
            System.out.println(p);
        }
    }
}
```

> Comparator接口可以实现自定义排序，实现Comparator接口时，要重写compare方法：
> int compare(Object o1, Object o2) 返回一个基本类型的整型
>
> * 如果要按照升序排序,则o1 小于o2，返回-1（负数），相等返回0，01大于02返回1（正数）
> * 如果要按照降序排序,则o1 小于o2，返回1（正数），相等返回0，01大于02返回-1（负数）

## 3.9 练习：使用Lambda标准格式（有参有返回）

<!--P341-->

### 题目

给定一个计算器Calculator接口，内含抽象方法calc可以将两个int数字相加得到和值

### 解答

```java
/*
    给定一个计算器Calculator接口，内含抽象方法calc可以将两个int数字相加得到和值
 */
public interface Calculator {
    // 定义一个计算两个int整数和的方法并返回结果
    public abstract int calc(int a, int b);
}



public class Demo01Calculator {
    public static void main(String[] args) {
        // 调用invokeCalc方法，方法的参数是一个接口，可以使用匿名内部类
        invokeCalc(10, 20, new Calculator() {
            @Override
            public int calc(int a, int b) {
                return a + b;
            }
        });

        // 使用Lambda表达式
        invokeCalc(120, 130, (int a, int b) -> {
            return a + b;
        });
    }

    /*
        定义一个方法
        参数传递两个int类型的整数
        参数传递Calcu接口
        方法内部调用Calculator中的方法calc计算两个整数的和
     */
    public static void invokeCalc(int a, int b, Calculator c) {
        int sum = c.calc(a, b);
        System.out.println(sum);
    }
}
```

## 3.10 Lambda省略格式

### 可推导即可省略

<!--P342-->

Lambda强调的是“做什么”而不是“怎么做”，所以凡事可以根据上下文推导得知的信息，都可以省略。

### 省略规则

在Lambda标准格式的基础上，使用省略写法的规则为：

1. 小括号内参数的类型可以省略；
2. 如果小括号内有且仅有一个参，则小括号可以省略；
3. 如果大括号内有且仅有一个语句，则无论是否有返回值，都可以省略大括号、return关键字及语句分号。

> 备注：掌握这些省略规则后，回顾本章开头的多线程案例。

## 3.11 Lambda的使用前提

Lambda的语法非常简洁，完全没有面向对象复杂的束缚。但是使用时有几个问题需要特别注意：

1. 使用Lambda必须具有接口，且要求**接口中有且仅有一个抽象对象**。

   无论是JDK内置的`Runnable`、`Comparator`接口还是自定义的接口，只有当接口中的抽象方法存在且唯一时，才可以使用Lambda。

2. 使用Lambda必须具有**上下文判断**。

   也就是方法的参数或局部变量必须为Lambda对应的接口类型，才能使用Lambda作为该接口的案例。

> 备注：<font color = "red">**有且仅有一个抽象方法的接口，称为“函数式接口”。**</font>

