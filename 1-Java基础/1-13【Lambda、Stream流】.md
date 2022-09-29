# 第一章 Lambda表达式

数学中，函数式有输入量、输出量的一套计算方案，也就是”拿什么东西做什么事情“。相对而言，面向对象过分强调”必须通过对象的形式来做事情“，而函数式思想尽量忽略面向对象的复杂语法——强调做什么，而不是以什么形式做。

## 1.1 冗余的Runnable

当需要启动一个线程去完成任务时，通常会通过`java.lang.Runnable`接口来定义任务内容，并使用`java.lang.Thread`类来启动该线程。代码如下：

```java
public class RunnableImpl implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "新线程创建了");
    }
}
```

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
```

对于`Runnable`的匿名内部类用法，可以分析出几点内容：

* `Thread`类需要`Runnable`接口作为参数，其中的抽象run方法是用来指定线程任务内容的核心；
* 为了指定`run`的方法体，不得不需要`Runnable`接口的实现类；
* 为了省去定义一个`RunnableImpl`实现类的麻烦，**不得不**使用匿名内部类；
* 必须覆盖重写抽象`run`方法，所以方法名称、方法参数、方法返回值**不得不**再写一遍，且不能写错；
* 而实际上，似乎只有方法体才是关键所在。

## 1.2 匿名内部类

```java
// 使用匿名内部类方式，实现多线程
new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "新线程创建了");
    }
}).start();
```

一方面，匿名内部类可以帮我们省去实现类的定义；另一方面，匿名内部类的语法——确实太复杂了！

仔细分析代码中语义，`Runnable`接口只有一个`run`方法的定义：`public abstract void run()`;

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

## 1.3 体验Lambda

我们真的希望创建一个匿名内部类对象吗？不。我们只是为了做这件事而不得不创建一个对象，我们真正希望做的事情是：将run方法体内的代码传递给Thread类知晓。

传递一段代码——这才是我们真正的目的。而创建对象只是受限于面向对象语法而不得不采取的一种手段方式。

下面看一下Lambda的写法：

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

Lambda省去面向对象的条条框框，Lambda表达式的标准格式是：

```apl
（参数类型 参数名称） -> {代码语句}
```

格式说明：

* `()`小括号内的语法与传统方法参数列表一致：无参数则留空；多个参数则用逗号分隔。
* `->`是引入的语法格式，代表指向动作。Lambda运算符，可以叫做箭头符号，或者goes to。
* `{}`大括号内的语法与传统方法体要求基本一致。

> Lambda表达式的本质就是匿名函数。

> “语法糖”是指适用更加方便，但是原理不变的代码语法。例如在遍历集合时会用的for-each语法，其实底层实现原理依旧是迭代器，这就是“语法糖”。
>
> 从应用层面来讲，java中的Lambda可以被当做是匿名内部类的”语法糖“，但是二者在原理上是**不同**的。

## 1.4 Lambda练习

### 1.4.1 无参无返回

给定一个厨子`Cook`接口，内含唯一的抽象方法`makeFood`，且无参数，无返回值。使用Lambda标准格式调用`invokeCook`方法，打印输出“吃饭了”字样

```java
public interface Cook {
    // 定义一个Cook接口，内含唯一的抽象方法makeFood
    public abstract void makeFood();
}
```

```java
public class Demo01Cook {
    // 定义一个方法，参数传递Cook接口，方法内部调用Cook接口中的方法makeFood
    public static void invokeCook(Cook cook) {
        cook.makeFood();
    }
    
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
}
```

或者下面这种方法：

```java
public class Demo01 {
    
    public static void main(String[] args) {
        Cook cook = () -> {System.out.println("吃饭了");};
        cook.makeFood();
    }

    interface Cook{
        void makeFood();
    }
}
```



### 1.4.2 有参有返回1

使用数组存储多个Person对象，对数组中的Person对象使用Arrays中的sort方法进行升序排序

```java
public class Person {
    private String name;
    private int age;
	// 省略get set toString和构造方法
}
```

```java
public class Demo01Arrays {
    public static void main(String[] args) {
        Person[] arr = {
                new Person("迪丽热巴", 20),
                new Person("古力娜扎",18),
                new Person("稀里哗啦", 23)
        };

        // 对数组中的Person对象使用Arrays的sort方法通过年龄进行升序（前边-后边）排序
        Arrays.sort(arr, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge();
            }
        });

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
> `int compare(Object o1, Object o2)` 返回一个基本类型的整型。根据第一个参数小于、等于或大于第二个参数分别返回负整数、零或正整数

### 1.4.3 有参有返回2

给定一个计算器`Calculator`接口，内含抽象方法`calc`可以将两个int数字相加得到和值

```java
public interface Calculator {
    // 定义一个计算两个int整数和的方法并返回结果
    public abstract int calc(int a, int b);
}
```

```java
public class Demo01Calculator {

    public static void invokeCalc(int a, int b, Calculator c) {
        int sum = c.calc(a, b);
        System.out.println(sum);
    }
    
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
}
```

## 1.5 省略规则和使用前提

**省略规则**

Lambda强调的是“做什么”而不是“怎么做”，所以凡事可以根据上下文推导得知的信息，都可以省略。可推导即可省略。

在Lambda标准格式的基础上，使用省略写法的规则为：

1. 小括号内参数的类型可以省略；
2. 如果小括号内有且仅有一个参，则小括号可以省略；
3. 如果大括号内有且仅有一个语句，则无论是否有返回值，都可以省略大括号、return关键字及语句分号。

**使用前提**

Lambda的语法非常简洁，完全没有面向对象复杂的束缚。但是使用时有几个问题需要特别注意：

1. 使用Lambda必须具有接口，且要求接口中有且仅有一个抽象对象。

   无论是JDK内置的`Runnable`、`Comparator`接口还是自定义的接口，只有当接口中的抽象方法存在且唯一时，才可以使用Lambda。

2. 使用Lambda必须具有上下文判断。

   也就是方法的参数或局部变量必须为Lambda对应的接口类型，才能使用Lambda作为该接口的案例。

> 备注：有且仅有一个抽象方法的接口，称为“函数式接口”。

# 第二章 函数式接口

函数式接口在java中是指：有且仅有一个抽象方法的接口。

函数式接口，即适用于函数式编程场景的接口。而java中的函数式编程体现就是Lambda，所以函数式接口就是可以适用于Lambda适用的接口。只有确保接口中有且仅有一个抽象方法，java中的Lambda才能顺利地进行推导。

定义函数式接口很简单，只要确保接口中有且仅有一个抽象方法即可：

```java
修饰符 interface 接口名称 {
    public abstract 返回值类型 方法名称（可选参数信息）;
    // 其他非抽象方法内容
}
```

由于接口当中抽象方法的`public abstract`是可以省略的，所以可以省略：

```java
public interface MyFunctionaliInterface{
    void myMethod();
}
```

## 2.1 FunctionalInterface注解

`@FunctionalInterface`注解作用：可以检测接口是否是一个函数式接口。这种类型的接口也成为SAM接口，即Single Abstract Method interface。

特点如下：

* 接口有且仅有一个抽象方法
* 允许定义静态方法
* 允许定义默认方法
* 允许Java.lang.Object中的public方法
* 该注解不是必须的。如果是函数式接口，那么加上注解能够更好地让编译器检查。如果不是函数式接口，加上注解会报错。

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    // 定义一个抽象方法
    public abstract void method();
}
```

## 2.2 自定义函数式接口

函数式接口的使用：一般可以作为方法的参数和返回值类型。

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    // 定义一个抽象方法
    public abstract void method();
}
```

```java
public class MyFunctionalInterfaceImpl implements MyFunctionalInterface{
    @Override
    public void method() {
        System.out.println("调用show方法，方法的参数是一个接口，所以可以传递接口的实现类对象");
    }
}
```

```java
public class Demo01 {
    // 定义一个方法，参数使用函数式接口MyFunctionalInterface
    public static void show(MyFunctionalInterface myIner) {
        myIner.method();
    }

    public static void main(String[] args) {
        // 调用show方法，方法的参数是一个接口，所以可以传递接口的实现类对象
        show(new MyFunctionalInterfaceImpl());

        // 调用show方法，方法的参数是一个接口，所以我们可以传递接口的匿名内部类
        show(new MyFunctionalInterface() {
            @Override
            public void method() {
                System.out.println("使用匿名内部类重写接口中的抽象方法");
            }
        });

        // 调用show方法，方法的参数是一个函数式接口，所以我们可以使用Lambda表达式
        show(() -> {
            System.out.println("使用Lambda表达式重写接口中的抽象方法");
        });

        // 简化Lambda表达式
        show(() -> System.out.println("简化Lambda表达式重写接口中的抽象方法"));
    }
}
```

上面的也可以这么搞：

```java
public class Demo01 {
    public static void main(String[] args) {
        MyFunctionalInterface interface1 = new MyFunctionalInterface() {
            @Override
            public void method() {
                System.out.println("林炫你好");
            }
        };
        interface1.method();
    }

    interface MyFunctionalInterface {
        // 定义一个抽象方法
        public abstract void method();
    }
}
```

## 2.3 Lambda的延迟执行

> 注：日志可以帮助我们快速的定位问题，记录程序运行过程中的情况，以便项目的监控和优化。

一种典型的场景就是对参数进行有条件使用，例如对日志消息进行拼接后，在满足条件的情况下进行打印输出

```java
public class Demo01Logger {
    // 定义一个根据日志的级别，显示日志信息的方法
    public static void showLog(int level, String message) {
        // 对日志等级进行判断，如果是1级，那么输出日志信息
        if (level == 1) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        // 定义三个日志信息
        String msg1 = "Hello";
        String msg2 = "World";
        String msg3 = "Java";

        // 调用showLog方法，传递日志级别和日志信息
        showLog(2, msg1 + msg2 + msg3);
    }
}
```

1. 可以发现上面代码存在一些性能浪费的问题。
2. 调用showLog方法后，传递的第二个参数是拼接好的字符串。
3. 可是传递的第一个参数并不是1，并不会打印。
4. 所以字符串白拼接了，所以存在了浪费。

性能浪费的话，那么使用Lambda表达式作为参数传递，仅仅是把参数传递到showLog方法中。

只有满足条件，日志的等级是1级，才会调用MessageBuilder接口中的方法builderMessage，才会进行字符串的拼接。如果条件不满足，日志等级不是1级，那么不会调用，也不会执行。

```java
@FunctionalInterface
public interface MessageBuilder {
    // 定义一个拼接消息的抽象方法，返回被拼接的消息
    public abstract String builderMessage();
}

public class Demo02Lambda {
    // 定义一个显示日志信息的方法，方法的参数传递日志等级和MessageBuilder接口
    public static void showLog(int level, MessageBuilder mb) {
        // 对日志等级进行判断，如果是1级，那么输出日志信息
        if (level == 1) {
            System.out.println(mb.builderMessage());
        }
    }

    public static void main(String[] args) {
        // 定义三个日志信息
        String msg1 = "Hello";
        String msg2 = "World";
        String msg3 = "Java";

        // 调用showLog方法，传递日志级别和日志信息
        showLog(2, () -> {
            // 返回一个拼接好的字符串
            return  msg1 + msg2 + msg3;
        });
    }
}
```

## 2.4 Lambda有参数和返回值

如果抛开实现原理不说，java中的Lambda表达式可以被当做是匿名内部类的替代品。如果方法的参数是一个函数式接口类型，那么就可以使用Lambda表达式进行代替。

使用Lambda表达式作为方法参数，其实就是使用函数式接口作为方法参数。

例如`java.lang.Runnable`接口就是一个函数式接口，假设有一个`startThread`方法使用该接口作为参数，那么就可以使用Lambda进行传参。这种情况其实和`Thread`类的构造方法参数为`Runnable`没有本质区别。

代码如下：

```java
public class Demo01Runnable {
    // 定义一个方法startThread，方法的参数使用函数式接口Runnable
    public static void startThread(Runnable run) {
        // 开启多线程
        new Thread(run).start();
    }

    public static void main(String[] args) {
        // 调用startThread方法，方法的参数是一个接口，那么我们可以传递这个接口的匿名内部类
        startThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "-->" + "线程启动了");
            }
        });

        // 调用startThread方法，方法的参数是一个函数式接口，可以传递Lambda表达式
        startThread(() -> {
            System.out.println(Thread.currentThread().getName() + "-->" + "线程启动了");
        });
        
        // 优化Lambda表达式
        startThread(() -> System.out.println(Thread.currentThread().getName() + "-->" + "线程启动了"));
    }
}
```

类似地，如果一个方法的返回值类型是一个函数式接口，那么就可以直接返回一个Lambda表达式。当需要通过一个方法来获取一个`java.util.Comparator`接口类型的对象作为排序器时，就可以调该方法获取。

```java
public class Demo02Comparator {
    // 定义一个方法，方法的返回值类型使用函数式接口Comparator
    public static Comparator<String> getComparator() {

        // 方法的返回值类型是一个函数式接口，所以我们可以返回一个Lambda表达式
        /*return (String o1, String o2) -> {
            return o2.length() - o1.length();
        };*/

        // 优化Lambda表达式 降序
        return (o1, o2) -> o2.length() - o1.length();
    }

    public static void main(String[] args) {
        // 创建一个字符串数组
        String[] arr = {"aaa", "b", "cccc", "dd"};
        // 输出排序前的数组
        System.out.println(Arrays.toString(arr));
        // 调用sort方法，排序
        Arrays.sort(arr, getComparator());
        // 输出排序后的数组
        System.out.println(Arrays.toString(arr));
    }
}
```

## 2.5 常用函数式接口

JDK提供了大量的常用的函数式接口以丰富Lambda的典型使用场景，它们主要在`java.util.function`包中被提供。

下面是最简单的几个接口及使用实例。

### 2.5.1 Supplier接口

<!--Supplier:供应商; 供货商; 供应者; 供货方;-->

`java.util.function.Supplier<T>`接口仅包含一个无参的方法：`T get()`。用来获取一个泛型参数指定类型的对象数据。由于这是一个函数式接口，这就意味着对应的Lambda表达式需要“对外提供”一个符合泛型类型的对象数据。

`Supplier<T>`接口被称为生产性接口，指定接口的泛型是什么类型，那么接口中的`get`方法就会生产什么类型的数据。

```java
public class Demo01Supplier {
    // 定义一个方法，方法的参数传递Supplier<T>接口，泛型是String，get方法会返回一个字符串
    public static String getString(Supplier<String> sup) {
        return sup.get();
    }

    public static void main(String[] args) {
        // 调用getString方法，方法的参数supplier是一个函数式接口，所以可以传递Lambda表达式
        /*String s = getString(() -> {
            return "林炫";
        });*/
        
        // 优化Lambda表达式
        String s = getString(() -> "林炫");
        
        System.out.println(s);
    }
}
```

或者直接使用下面的方式：

```java
// 未简写前
public class Demo01 {
    public static void main(String[] args) {
        Supplier<String> sup = () -> {
            return "林炫";
        };
        System.out.println(sup.get());
    }
}
```

```java
// 简写后
public class Demo01 {
    public static void main(String[] args) {
        Supplier<String> sup = () -> "林炫";
        System.out.println(sup.get());
    }
}
```

看下面的案例：

求数组元素最大值。使用`Supplier`接口作为方法参数类型，通过Lambda表达式求出int数组的最大值。接口的泛型使用`java.lang.Integer`类。

```java
public class Demo02Test {
    public static int getMax(Supplier<Integer> sup) {
        return sup.get();
    }

    public static void main(String[] args) {
        int[] arr = {100, 0, 20, 541, 65};
        int number = getMax(() -> {
            int max = arr[0];
            for (int i : arr) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        });

        System.out.println(number);
    }
}
```

```java
// 也可以使用这种方法
public class Demo01 {
    public static void main(String[] args) {
        int[] arr = {100, 0, 20, 541, 65};
        Supplier<Integer> su = () -> {
            int max = arr[0];
            for (int i : arr) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        };
        System.out.println(su.get());
    }
}
```

### 2.5.2 Consumer接口

`java.util.function.Consumer<T>`接口正好与`Supplier`接口相反，它不是生产一个数据，而是消费一个数据，其数据类型由泛型决定。

源码如下：

```java
@FunctionalInterface
public interface Consumer<T> {

    void accept(T t);

    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { 
            accept(t); 
            after.accept(t); 
        };
    }
}
```

一个抽象方法accept和一个默认方法andThen。

* **抽象方法accept**

  `Consumer`接口中包含抽象方法`void accept(T t)`，意为消费一个指定泛型的数据。基本使用如：

  ```java
  public class Demo01Consumer {
      public static void method(String name, Consumer<String> cons) {
          cons.accept(name);
      }
  
      public static void main(String[] args) {
          method("林炫", (String name) -> {
              // 对传递的字符串进行消费
              // 消费方式：直接输出字符串
              // System.out.println(name);
  
              // 消费方式：把字符串进行反转输出
              System.out.println(new StringBuffer(name).reverse().toString());
          });
      }
  }
  ```

  或者下面这种方式：

  ```java
  // 未简化前
  public class Demo01 {
      public static void main(String[] args) {
          Consumer<String> con = (String msg) -> {
              System.out.println(msg);
          };
          con.accept("linxuan");
      }
  }
  ```

  ```java
  // 简化后
  public class Demo01 {
      public static void main(String[] args) {
          Consumer<String> con = msg -> System.out.println(msg);
          con.accept("linxuan");
      }
  }
  ```

  

* **默认方法andThen**

  如果一个方法的参数和返回值全部都是`Consumer`类型，那么就可以实现效果：消费数据的时候，首先做一个操作，然后再做一个操作，实现组合。而这个方法就是`Consumer`接口中的`default`方法`andThen`。

  ```java
  default Consumer<T> andThen(Consumer<? super T> after) {
      Objects.requireNonNull(after);
      return (T t) -> { 
          accept(t); 
          after.accept(t); 
      };
  }
  ```

  ```java
  // 返回一个组合的Consumer ，依次执行此操作，然后执行after操作。 如果执行任一操作会抛出异常，它将被转发到组合操作的调用者。 如果执行此操作会引发异常，则不会执行after操作。
  
  Consumer<String> con1;
  Consumer<String> con2;
  String s = "Hello";
  
  con1.accept(s);
  con2.accept(s);
  // 等价于
  con1.andThen(con2).accpet(s);
  ```

  > 备注：java.util.Objects的requireNonNull静态方法将会在参数为null时主动抛出异常。这就省去了重复编写if语句和抛出空指针异常的麻烦。

  要想实现组合，需要两个或者多个Lambda表达式，而andThen的语义正是“一步一步”操作。例如两个步骤组合的情况：

  ```java
  public class Demo02AndThen {
      // 定义一个方法，方法的参数传递一个字符串和两个Consumer接口，Consumer接口的泛型使用String
      public static void method(String s, Consumer<String> con1, Consumer<String> con2) {
          // con1.accept(s);
          // con2.accept(s);
  
          // 使用andThen方法，把两个Consumer接口连接到一起，再消费数据
          // con1连接con2，先执行con1消费数据再执行con2消费数据
          con1.andThen(con2).accept(s);
      }
  
      public static void main(String[] args) {
          method("Hello",
                  (t) -> {
                      // 消费方式：把字符串转换为大写输出
                      System.out.println(t.toUpperCase());
                  },
                  (t) -> {
                      // 消费方式：把字符串转换为小写输出
                      System.out.println(t.toLowerCase());
                  });
      }
  }
  ```


下面来看一个案例，格式化打印信息：

下面的字符串数组当中存有多条信息，请按照“姓名：XX。性别：XX。”的格式将信息打印出来。要求将打印姓名的动作作为第一个Consumer接口的Lambda实例，将打印性别的动作作为第二个Consumer接口的Lambda实例，将两个Consumer接口按照顺序“拼接”到一起。`String[] array = {"迪丽热巴, 女", "古力娜扎, 女", "马尔扎哈, 男"};`

```java
public class Demo03Test {
    public static void method(String[] arr, Consumer<String> con1, Consumer<String> con2) {
        // 遍历数组
        for (String message : arr) {
            // 使用andThen方法连接两个Consumer接口，消费字符串
            con1.andThen(con2).accept(message);
        }
    }

    public static void main(String[] args) {
        String[] array = {"迪丽热巴, 女", "古力娜扎, 女", "马尔扎哈, 男"};

        method(array,
                (message) -> {
                    // 消费方式：对其进行切割，获取姓名，按照指定的格式输出
                    String name = message.split(",")[0];
                    System.out.print("姓名：" + name);
                },
                (message) -> {
                    // 消费方式：对其进行切割，获取年龄，按照指定的格式输出
                    String age = message.split(",")[1];
                    System.out.println("；年龄：" + age + "。");
                });
    }
}
```

### 2.5.3 Predicate接口

有时候我们需要对某种类型的数据进行判断，从而得到一个boolean值结果。这时候可以使用`java.util.function.Predicate<T>`接口。

```java
@FunctionalInterface
public interface Predicate<T> {

    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : object -> targetRef.equals(object);
    }
}
```

一个抽象方法test和三个默认方法and、or、negate，以及静态方法isEqual。

* **抽象方法：test**

  `Predicate`接口中包含一个抽象方法：`boolean test(T t)`。用于条件判断的场景：

  ```java
  import java.util.function.Predicate;
  
  public class Demo01Predicate {
      public static boolean checkString(String s, Predicate<String> pre) {
          return pre.test(s);
      }
  
      public static void main(String[] args) {
          // 定义一个字符串
          String s = "abcdef";
  
          // 调用checkString方法对字符串进行校验，参数传递字符串和Lambda表达式
          boolean b = checkString(s, (String str) -> {
                      return str.length() > 5;
          });
          System.out.println(b);
      }
  }
  ```

  条件判断的标准是传入的Lambda表达式逻辑，只要字符串长度大于5则认为很长。

* **默认方法：and**

  既然是条件判断，就会存在与、或、非三种常见的逻辑关系，其中将两个`Predicate`条件使用“**与**”逻辑连接起来实现“**并且**”的效果的时候，可以使用`default`方法`and`。其JDK源代码为：

  ```java
  default Predicate<T> and(Predicate<? super T> other) {
      Objects.requireNonNull(other);
      return (t) -> test(t) && other.test(t);
  }
  ```

  如果要判断一个字符串既要包含"a"，并且字符串长度还要大于5，那么：

  ```java
  import java.util.function.Predicate;
  
  public class Demo02Predicate_and {
      public static boolean checkString (String s, Predicate<String> pre1, Predicate<String> pre2) {
          // return pre1.test(s) && pre2.test(s);
          return pre1.and(pre2).test(s); // 等价于return pre1.test(s) && pre2.test(s);
  
      }
  
      public static void main(String[] args) {
          // 定义一个字符串
          String str = "abcdef";
          boolean b = checkString(str, 
  			(String s1) -> {
              	// 判断字符串的长度是否大于5
              	return s1.length() > 5;
          	}, 
  			(String s2) -> {
              	// 判断字符串中是否包含a
              	return s2.contains("a");
          	});
          System.out.println(b);
      }
  }
  ```

* **默认方法：or**

  与`and`的“与”类似，默认方法`or`实现逻辑关系中的“**或**”。JDK源代码为：

  ```java
  default Predicate<T> or(Predicate<? super T> other) {
      Objects.requireNonNull(other);
      return (t) -> test(t) || other.test(t);
  }
  ```

* **默认方法：negate**

  "与"、"或"已经了解了，剩下的"非"（取反）也很简单。默认方法negate的JDK源代码为：

  ```java
  default Predicate<T> negate() {
      return (t) -> !test(t);
  }
  ```

  很容易可以看出，它是执行了test方法后，对结果Boolean值进行"!"取反而已。

练习：集合信息筛选

数组当中有多条“姓名+性别”的信息如下，请通过Predicate接口的拼装将符合要求的字符串筛选到结合ArrayList中，需要同时满足两个条件：必须为女生，姓名为4个字。`String[] arr = {"迪丽热巴, 女", "古力娜扎, 女", "马尔扎哈, 男", "赵丽颖, 女"};`

```java
public class Demo03Test {
    public static ArrayList<String> fileter(String[] arr, Predicate<String> pre1, Predicate<String> pre2) {
        // 定义一个ArrayList集合，用于存储过滤后的信息。
        ArrayList<String> list = new ArrayList<>();
        // 遍历数组，获取数组中的每一条信息
        for (String s : arr) {
            // 使用Predicate接口中的方法test对获取到的字符串进行判断
            boolean b = pre1.and(pre2).test(s);
            if (b) {
                // 条件成立的话，把信息存储到ArrayList集合中
                list.add(s);
            }
        }
        // 把集合返回
        return list;
    }

    public static void main(String[] args) {
        // 定义一个存储字符串的数组
        String[] arr = {"迪丽热巴, 女", "古力娜扎, 女", "马尔扎哈, 男", "赵丽颖, 女"};
        // 调用filter方法，传递字符串数组和两个Lambda表达式
        ArrayList<String> arrayList = fileter(arr, (String s) -> {
            // 获取字符串中的性别，判断是否为女
            return s.split(", ")[1].equals("女");
            }, (String s) -> {
            // 获取字符串中的姓名，判断长度是否为4个字符。
            return s.split(", ")[0].length() == 4;
        });

        // 遍历集合
        for (String s : arrayList) {
            System.out.println(s);
        }
    }
}
```

### 2.5.4 Function接口

`java.util.function.Function<T, R>`接口用来根据一个类型的数据得到另一个类型的数据，前者为前置条件，后者称为后置条件。

```java
@FunctionalInterface
public interface Function<T, R> {

    R apply(T t);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
```

* **抽象方法：apply**

  `Function`接口中最主要的抽象方法为：`R apply(T t)`，根据类型T的参数获取类型R的结果。

  使用场景例如：将`String`类型转换为`Integer`类型。

  ```java
  public class Demo01Function {
      public static void change(String s, Function<String, Integer> fun) {
          // Integer in = fun.apply(s);
          int in = fun.apply(s); // 自动拆箱 Integer -> int
          System.out.println(in);
      }
  
      public static void main(String[] args) {
          // 定义一个字符串类型的整数
          String s = "12345";
          // 调用change方法，传递字符串类型的整数，和Lambda表达式。
          /*change(s, (String str) -> {
              return Integer.parseInt(str);
          });*/
  
          // 优化Lambda表达式
          change(s, (str) -> Integer.parseInt(str)); // 12345
      }
  }
  ```

* **默认方法：andThen**

  `Function`接口中有一个默认的`andThen`方法，用来进行组合操作。JDK源代码：

  ```java
  default <V> Function<T,V> andThen (Function<? super R,? extends V> after) {
      Objcets.requireNonNull(after);
      return (T t) -> after.apply(apply(t));
  }
  ```

  该方法同样用于“先做什么，再做什么”的场景，和`Consumer`中的`andThen`差不多

  看一下下面一个例子：第一个操作时将字符串解析成为int数字，第二个操作时乘以10。两个操作通过`andThen`

  ```java
  import java.util.function.Function;
  
  public class Demo02Function {
      public static void change(String s, Function<String, Integer> fun1, Function<Integer, String> fun2) {
          String str = fun1.andThen(fun2).apply(s);
          System.out.println(str);
      }
  
      public static void main(String[] args) {
          String s = "13";
          // 调用change方法，传递字符串和两个Lambda表达式
          /*change(s, (String str) -> {
              // 把字符串转换为整数并加10
              return Integer.parseInt(str) + 10;
          }, (Integer in) -> {
              // 把整数转换为字符串
              return in + "";
          });*/
  
          // 优化Lambda表达式
          change(s, str -> Integer.parseInt(str) + 10, in -> in + "");
      }
  }
  ```


练习：自定义函数模型拼接。请使用`Function`进行函数模型的拼接，按照顺序需要执行的多个函数操作为：	`String str = “赵丽颖，20”；`

1. 将字符串截取数字年龄部分，得到字符串；
2. 将上一步的字符串转换成为int类型的数字；
3. 将上一步的int数字累加100，得到结果数字。

第一种解：

```java
public class Demo03Function {
    public static void change(String s, Function<String, Integer> fun) {
        Integer integer = fun.apply(s);
        System.out.println(integer);
    }

    public static void main(String[] args) {
        String str = "赵丽颖, 20";
        String s = str.split(", ")[1];

        change(s, (String s1) -> {
            return Integer.parseInt(s) + 100;
        });
    }
}
```

第二种解：

```java
public class Demo03Function {
    public static void change(String ss, Function<String, String> fun1, Function<String, Integer> fun2, Function<Integer, Integer> fun3) {
        Integer integer = fun1.andThen(fun2).andThen(fun3).apply(ss);
        System.out.println(integer);
    }

    public static void main(String[] args) {
        String str = "赵丽颖, 20";

        change(str, (String s) -> {
            return s.split(", ")[1];
        }, (String s) -> {
            return Integer.parseInt(s);
        }, (Integer in) -> {
            return in + 100;
        });
    }
}
```

# 第三章 方法引用

在使用Lambda表达式的时候，我们实际上传递进去的就是一种解决方案：拿什么参数做什么操作。那么考虑一种情况：如果我们在Lambda中所指定的操作方案，已经有地方存在相同方案，那是否还有必要再写重复逻辑？

有时候多个Lambda表达式的实现函数一样，我们可以封装成通用方法，以便于维护。这时候就可以使用方法引用来实现了。

## 3.1 冗余的Lambda场景

来看一个简单的函数式接口以应用Lambda表达式：

```java
@FunctionInterface
public interface Printable {
    void print(String str);
}
```

在Printable接口唯一的抽象方法print接收一个字符串参数，目的就是为了打印显示它。那么通过Lambda表达式来使用它的代码很简单：

```java
public interface Printable {
    void print(String s);
}
```

```java
public class Demo01Printable {
    // 定义一个方法，方法参数是一个Printable接口，对字符串进行打印
    public static void printString(Printable p) {
        p.print("Hello world");
    }

    public static void main(String[] args) {
        // 调用printString方法，方法参数Printable是一个函数式接口，所以传递Lambda表达式
        printString(s -> System.out.println(s));

        /*
        * 分析：
        *   Lambda表达式的目的，打印参数传递的字符串
        *   把参数s，传递给了System.out对象，调用out对象中的方法println对字符串进行了输出。
        *   注意：
        *       1.System.out对象是已经存在的。
        *       2.println方法也是已经存在的。
        *   所以我们可以使用方法引用来优化Lambda表达式
        *   可以使用System.out方法来直接引用（调用）println方法。
        * */
        printString(System.out::println);
    }
}
```

或者下面这种：

```java
public class Demo01 {
    public static void main(String[] args) {
        Consumer<String> consumer = (System.out::printf);
        
        consumer.accept("linxuan");
    }
}
```

## 3.2 方法引用符

双冒号`::`为引用运算符，而他所在的表达式被称为**方法引用**。

如果Lambda要表达的函数方案已经存在于某个方法的实现中，那么则可以通过双冒号来引用该方法作为Lambda的替代者。

例如上例中，`System.out`对象中有一个存在的`println(String)`方法恰好就是我们所需要的。那么对于`printString`方法和函数式接口参数，对比下面两种写法，完全等效：

* Lambda表达式：`s -> System.out.println(s)`;
* 方法引用写法：`System.out::println`；

第一种语义是指：拿到参数之后经Lambda之手，继而传递给`System.out.println()`方法去处理。

第二种等效写法的语义是指：直接让`System.out`中的`println`方法来取代Lambda。两种写法的执行效果完全一样，而第二种方法引用的写法复用了已有方案，更加简洁。

> 备注：Lambda中 传递的参数 一定是方法引用中的那个方法可以接收的类型，否则会抛出异常。

如果使用Lambda，那么根据“可推导就是可省略”的原则，无需指定参数类型，也无需指定的重载形式——他们都将被自动推导，而如果使用方法引用，也是同样可以根据上下文进行推导。**函数式接口是Lambda的基础，而方法引用是Lambda的孪生兄弟。**

## 3.3 通过对象名引用成员方法

```java
// 函数式接口
public interface Printable {
    void print(String s);
}
```

```java
public class MethodRerObject {
    // 定义一个成员方法，传递字符串，把字符串按照大小写输出
    public void printUpperCaseString(String str) {
        System.out.println(str.toUpperCase());
    }
}
```

```java
public class Demo01ObjectMethodReference {
    // 定义一个方法，方法的参数传递Printable接口
    public static void printString(Printable p) {
        p.print("Hello World");
    }

    public static void main(String[] args) {
        // 调用printString方法，方法的参数是一个函数式接口，可以使用Lambda表达式传递
        printString((s) -> {
            // 创建MethodRerObject对象
            MethodRerObject obj = new MethodRerObject();
            // 调用此对象的成员方法printUpperCaseString
            obj.printUpperCaseString(s);
        });
        
        // 或者使用下面的方式：
        printString(new MethodRerObject()::printUpperCaseString);
    }
}
```

## 3.4 通过类名成引用静态方法

由于在`java.lang.Math`类中已经存在了静态方法abs，所以当我们需要通过Lambda来调用该方法的时候，有两种写法。首先是函数式接口。

```java
// 函数式接口，这个注解@FunctionalInterface代表了这个是函数式接口
@FunctionalInterface
public interface Calcable {
    int calc(int num);
}
```

```java
public interface Calcable {
    int calcable(int number);
}
```

```java
public class Demo01StaticMethodReference {
    public static int method(int number, Calcable c) {
        return c.calcable(number);
    }

    public static void main(String[] args) {
        int num = method(-10, (n) -> {
            return Math.abs(n);
        });
        System.out.println(num);

        /*
            使用方法引用优化Lambda表达式
            Math类存在
            abs计算绝对值的静态方法也存在
            所以可以直接通过类名来引用静态方法
         */
        int number = method(-20, Math::abs);
        System.out.println(number);
    }
}
```

下面这种方法一样：

```java
public class Demo01 {
    public static void main(String[] args) {
        Calcable calcable = (int num) -> {
            return Math.abs(num);
        };
        System.out.println(calcable.calc(-10));

        Calcable calcable2 = (Math::abs);
        System.out.println(calcable2.calc(-20));
    }

    @FunctionalInterface
    public interface Calcable {
        int calc(int num);
    }
}
```

## 3.5 通过super引用成员方法

```java
// 定义见面的函数式接口
@FunctionalInterface
public interface Greetable {
    // 定义一个见面的方法
    void greet();
}
```

```java
// 定义父类
public class Human {
    // 定义一个sayHello的方法
    public void sayHello() {
        System.out.println("Hello, 我是父类方法");
    }
}
```

```java
// 定义子类
public class Man extends Human{
    // 子类重写父类sayHello的方法
    @Override
    public void sayHello() {
        System.out.println("Hello 我是Man!");
    }

    // 定义一个方法参数传递Greetable接口
    public void method(Greetable g) {
        g.greet();
    }

    public void show() {
        // 调用method方法，参数是函数式接口，使用Lambda表达式
        method(() -> {
            // 创建父类对象
            Human h = new Human();
            // 调用父类方法
            h.sayHello();
        });

        // 因为是子父类继承关系，所以可以直接使用super关键字
        method(() -> {
            super.sayHello();
        });

        // 使用 方法引用 super是存在的 父类的成员方法也存在
        method(super::sayHello);
    }

    public static void main(String[] args) {
        new Man().show();
    }
}
```

## 3.6 通过this引用成员方法

this代表当前对象，如果需要引用的方法就是当前类中的成员方法，那么可以使用“`this::成员方法`”的格式来使用方法引用。

```java
public class Husband {
    // 定义一个买房子的方法
    public void buyHouse() {
        System.out.println("北京二环购买四合院");
    }

    // 定义结婚方法，参数传递Richable接口
    public void marry(Richable r) {
        r.buy();
    }

    // 定义一个非常高兴的方法
    public void soHappy() {
        // 调用结婚的方法，方法的参数Richable是一个函数式接口，传递Lambda表达式
        marry(() -> {
            // 使用this.成员方法，调用本类买房子的方法
            this.buyHouse();
        });

        // 方法引用
        marry(this::buyHouse);
    }

    public static void main(String[] args) {
        new Husband().soHappy();
    }
}
```

## 3.7 类的构造器引用

由于构造器的名称与类名完全一样，并不固定。所以构造器引用使用`类名称::new`的格式来表示。

```java
@FunctionalInterface
public interface PersonBuilder {
    Person builderPerson(String name);
}
```

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
}
```

```java
// 类的构造器（构造方法）引用
public class Demo {
    // 定义一个方法，参数传递姓名和PersonBuilder接口，方法通过姓名创建Person对象
    public static void printName(String name, PersonBuilder per) {
        Person person = per.builderPerson(name);
        System.out.println(person.getName());
    }

    public static void main(String[] args) {
        // 调用printName方法，方法的参数是函数式接口，Lambda表达式
        printName("林炫", (name) -> {
            return new Person(name);
        }); 

        /*
            使用方法引用优化Lambda表达式
            构造方法new Person(String name)已知
            创建对象已知 new
            就可以使用Person引用new创建对象
         */
        // 使用Person类的带参构造方法，通过传递的姓名创建对象
        printName("你好", Person::new);
    }
}
```

## 3.8 数组的构造器引用

数组也是Object的子类对象，所以同样具有构造器，只是语法稍微不同。如果对应到Lambda的场景中，需要一个函数式接口。

```java
// 定义一个创建数组的函数式接口
@FunctionalInterface
public interface ArrayBuilder {
    // 定义一个创建int类型数组的方法，参数传递数组的长度，返回创建好的int类型数组
    int[] buildArray(int length);
}
```

```java
// 数组的构造器使用
public class Demo {
    /*
        定义一个方法
        方法的参数传递创建数组的长度和ArrayBuilder接口
        方法内部根据传递的长度使用ArrayBuilder中的方法创建数组并返回
     */
    public static int[] createArray(int length, ArrayBuilder ab) {
        return ab.buildArray(length);
    }

    public static void main(String[] args) {
        // 调用createArray方法，传递数组的长度和Lambda表达式
        int[] array1 = createArray(10, (len) -> {
            // 根据数组的长度，创建数组并且返回
            return new int[len];
        });
        System.out.println(array1.length);

        /*
            使用方法引用优化Lambda表达式
            已知创建的就是int[]数组
            数组的长度也是已知的
            就可以使用方法引用int[]引用new，根据参数传递的长度来创建数组
         */
        int[] array2 = createArray(10, int[]::new);
        System.out.println(Arrays.toString(array2));
        System.out.println(array2.length);
    }
}
```

# 第四章 Stream流

java8中，得益于Lambda所带来的函数式编程，引入了一个全新的Stream概念，用于解决已有的集合类库既有的弊端。

几乎所有的集合（如Collection接口或者Map接口等）都支持直接或者间接的遍历操作。而当我们需要对集合中的元素进行操作的时候，除了必需的添加、删除、获取外，最典型的就是集合的遍历。

```java
public class Demo01ForEach {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("迪丽热巴");
        list.add("古力娜扎");
        list.add("马尔扎哈");
        for (String name : list) {
            System.out.println(name);
        }
    }
}
```

这是一段非常简单的集合遍历操作：对集合中每一个字符串进行打印输出操作。

## 4.1 循环遍历的弊端

java8的Lambda表达式让我们可以专注于做什么（What），而不是怎么做（How），这点此前已经结合内部类进行了对比说明。现在，我们仔细体会一下上述代码，可以发现：

* for循环的语法就是“怎么做”
* for循环的循环体才是“做什么”

为什么使用循环？因为要进行遍历。但循环式遍历的唯一方式吗？遍历是指每一个元素逐一进行处理，而并不是从第一个到最后一个顺次处理的循环。前者是目的，后者是方式。

试想一下，如果希望对集合中的元素进行筛选过滤：

1. 将集合A根据条件一过滤为子集B。
2. 然后再根据条件二过滤为子集C。

```java
import java.util.ArrayList;
import java.util.List;

public class Demo02List {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("张无忌");
        list.add("周芷若");
        list.add("赵敏");
        list.add("张强");
        list.add("张三丰");

        // 对List集合中的元素进行过滤，只要以张开头的文件，存储到一个新的集合中。
        List<String> listA = new ArrayList<>();
        for (String s : list) {
            if (s.startsWith("张")) {
                listA.add(s);
            }
        }

        // 对ListA集合进行过滤，只要姓名长度为3的人，存储到一个集合中
        List<String> listB = new ArrayList<>();
        for (String s : listA) {
            if (s.length() == 3) {
                listB.add(s);
            }
        }

        // 遍历listB集合输出
        for (String s : listB) {
            System.out.println(s);
        }
    }
}
```

这段代码中有三个循环，每一个作用不同：

1. 首先筛选所有姓张的人；
2. 然后筛选名字有三个的人；
3. 最后进行结果进行打印输出。

每当我们需要对集合中的元素进行操作的时候，总是需要进行循环、循环、再循环。这是理所当然的吗？当然不是，循环只是做事情的方式，而不是目的。另一方面，使用线性循环就意味着只能遍历一次。如果希望再次遍历，只能在使用另一个循环从头开始。

## 4.2 Stream流写法概述

下面请看Lambda表达式的衍生物Stream给我们带来的更优雅的写法：

```java
public class Demo03Stream {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("张无忌");
        list.add("周芷若");
        list.add("赵敏");
        list.add("张强");
        list.add("张三丰");

        // 对List集合中的元素进行过滤，只要以张开头的文件，存储到一个新的集合中。
        // 对ListA集合进行过滤，只要姓名长度为3的人，存储到一个集合中
        // 遍历listB集合输出
        list.stream()
                .filter(name -> name.startsWith("张"))
                .filter(name -> name.length() == 3)
                .forEach(name -> System.out.println(name));
    }
}
```

> **注意：请暂时忘记对传统IO流的固有印象！**

整体来看，流式思想类似于工厂车间的“生产流水线”。

当需要对多个元素进行操作的时候，考虑到性能及便利性，我们应该首先拼好一个模型步骤方案，然后按照方案去执行它。

![](..\图片\1-13【Lambda、Stream流】\1.png)

这张图展示了过滤、映射、跳过、计数等多步操作，这是一种集合元素的处理方案，而方案就是一种“函数模型”。图中每一个方框都是一个“流”，调用指定的方法，可以从一个流模型转换为另一个流模型。而最右侧的数字3是最终结果。

这里的`filter`、`map`、`skip`都是在对函数模型进行操作，集合元素并没有被真正的处理。只有当终结方法`count`执行的时候，整个模型才会按照指定策略执行操作。而这得益于Lambda的延迟执行特性。

> 备注：“Stream流”其实是一个集合元素的函数模型，它不是集合，也不是数据结构，其本身并不存储任何元素（或其地址）。

Stream（流）是一个来自数据源的元素队列

* 元素是特定类型的对象，形成一个队列。java中的Stream并不会存储元素，而是按照需求计算。
* 数据源流的来源。可以是集合，数组等。

和以前的Collection操作不同，Stream操作还有两个基础的特征：

* **Pipelining**：中间操作都会返回流对象本身。这样多个操作可以串联一个管道，如同流式风格（fluent style）。这样做可以对操作进行优化，比如延迟执行（laziness）和短路（short-circuiting）。
* **内部迭代**：以前对集合遍历都是通过Iterator或者增强for的方式，显示的在集合外部进行迭代，这就叫做外部迭代。Stream提供了内部迭代的方式，流可以直接调用遍历方法。

当时用一个流的时候，通常包括三个基本步骤：获取一个数据源（source）-> 数据转换 -> 执行操作获取想要的结果，每次转换原有Stream对象不改变，返回一个新的Stream对象（可以有多次转换），这就允许对齐操作可以先链条一样排列，变成一个管道。

## 4.3 获取流

`java.util.stream.Stream<T>`是java 8新加入的最常用的流接口。（**注意：这并不是一个函数式接口**）

获取一个流非常简单，有一下几种常用的方式：

* 所有的`Collection`集合都可以通过`stream`默认方法获取流；
* `Stream`接口的静态方法`of`可以获取数组对应的流。

```java
public class Demo01GetStream {
    public static void main(String[] args) {
        // 把集合转换为Stream流
        List<String> list = new ArrayList<>();
        Stream<String> stream1 = list.stream();

        Set<String> set = new HashSet<>();
        Stream<String> stream2 = set.stream();

        Map<String, String> map = new HashMap<>();
        // 获取键，存储到一个Set集合中
        Set<String> keySet = map.keySet();
        Stream<String> stream3 = keySet.stream();

        // 获取值，存储到一个Collection集合中
        Collection<String> values = map.values();
        Stream<String> stream4 = values.stream();

        // 获取键值对（键与值映射关系 entrySet）
        Set<Map.Entry<String, String>> entries = map.entrySet();
        Stream<Map.Entry<String, String>> stream5 = entries.stream();

        // 把数组转换为Stream流
        Stream<Integer> stream6 = Stream.of(1, 2, 3, 4, 5);
        // 可变参数可以传递数组
        Integer[] arr1 = {1, 2, 3, 4};
        Stream<Integer> stream7 = Stream.of(arr1);
        String[] arr2 = {"a", "bb", "ccc"};
        Stream<String> stream8 = Stream.of(arr2);
    }
}
```

## 1.4 常用流方法

![](..\图片\1-13【Lambda、Stream流】\2.png)

流类型很丰富，这里介绍一些常用的API。这些方法可以分为两种：

* 延迟方法：返回值类型仍然是`Stream`接口自身类型的方法，因此支持链式调用。（除了终结方法外，其余方法均为延迟方法）
* 终结方法：返回值类型不再是`Stream`接口自身类型的方法，因此不在支持类似`StringBuilder`那样的链式调用。**本小节中，终结方法包括`count`和`forEach`方法。**

### 4.4.1 逐一处理--forEach

虽然名字叫做`forEach`，但是与for循环中的“for-each”昵称不同。

```java
void forEach(Consumer<? super T> action)
```

该方法接收一个`Consumer`接口函数，会将每一个流元素交个该函数进行处理。`java.util.function.Consumer<T>`接口是一个消费型接口。`Consumer`接口中包含抽象方法`void accept(T t)`，意为消费一个指定泛型的数据。

```java
public class Demo02Stream_forEach {
    public static void main(String[] args) {
        // 获取一个Stream流
        Stream<String> stream = Stream.of("张三", "李四", "王五", "赵六", "田七");
        // 使用Stream流中的forEach方法，对Stream流中的数据进行遍历输出
        /*stream.forEach((String name) -> {
            System.out.println(name);
        });*/

        stream.forEach(name -> System.out.println(name));
    }
}
```

### 4.4.2 过滤--filter

可以通过filter方法将一个流转换成另一个子集流。方法：

```java
Stream<T> filter (Predicate<? super T> predicate);
```

该接口接收一个`Predicate`函数式接口参数（可以是一个Lambda或者方法引用）作为筛选条件。

![](..\图片\1-13【Lambda、Stream流】\3.png)

此前学过`java.util.stream.Predicate`函数式接口，其中唯一的抽象方法为：`boolean test(T t);`

该方法将会产生一个Boolean值结果，代表指定的条件是否满足。如果结果为TRUE，那么Stream流的`filter`方法将会留用元素；如果结果为FALSE，那么`filter`方法将会舍弃元素。

```java
public class Demo03Stream_filter {
    public static void main(String[] args) {
        // 创建一个Stream流
        Stream<String> stream = Stream.of("张三", "李四", "王五", "张三一", "张三二");
        // 对Stream流中的元素进行过滤，只要姓张的
        Stream<String> stream1 = stream.filter((String name) -> {
            return name.startsWith("张");
        });
        // 遍历Stream流
        stream1.forEach(name -> System.out.println(name));
    }
}
```

Stream流属于管道流，只能被消费，使用一次。第一个Stream流调用完毕后，数据会流转到下一个Stream上面。而这时候第一个Stream流已经使用完毕，就会自动关闭。所以第一个Stream流就不能使用了，如果再次使用会抛出异常。

### 4.4.3 映射--map

如果需要将流中的元素映射到另一个流中，可以使用map方法。方法：

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
```

该接口需要一个Function函数式接口参数，可以将当前流中的T类型数据转换成为另一种R类型的流。

![](..\图片\1-13【Lambda、Stream流】\4.png)

此前我们已经学习过`java.util.stream.Function`函数式接口，其中唯一的抽象方法为：`R apply(T t);`。这可以将一种T类型转换成为R类型，而这种转换的动作，就成为“映射”。

Stream流中的map方法基本使用代码如下：

```java
import java.util.stream.Stream;

public class Demo04Stream_map {
    public static void main(String[] args) {
        // 获取一个String类型的Stream流
        Stream<String> stream1 = Stream.of("1", "2", "3", "4", "5");
        // 使用map方法，把字符串类型转换为Integer类型
        Stream<Integer> stream2 = stream1.map((String s) -> {
            return Integer.parseInt(s);
        });
        // 遍历Stream流
        stream2.forEach(i -> System.out.println(i));

    }
}
```

### 4.4.4 统计个数--count

正如旧集合Collection当中的size方法一样，流提供count方法来数一数其中的元素的个数：

```java
long count();
```

该方法返回一个long值代表元素个数（不再像旧集合那样是int值）。基本使用：

```java
public class Demo05Stream_count {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        Stream<Integer> stream = list.stream();
        long count = stream.count();
        System.out.println(count);
    }
}
```

### 4.4.5 取用前几个--limit

limit方法可以对流进行截取，只取用前n个。方法：

```java
Stream<T> limit(long maxSize);
```

参数是一个long型，如果集合当前长度大于参数则进行截取；否则不进行操作。基本使用：

![](..\图片\1-13【Lambda、Stream流】\5.png)

```java
public class Demo06Stream_limit {
    public static void main(String[] args) {
        // 获取一个Stream流
        String[] arr = {"张三", "李四", "王五", "赵六", "田七"};
        Stream<String> stream1 = Stream.of(arr);
        // 使用limit对Stream流中的元素截取，只要前三个元素
        Stream<String> stream2 = stream1.limit(3);
        // 遍历Stream流
        stream2.forEach(name -> System.out.println(name));
    }
}
```

### 4.4.6 跳过前几个--skip

如果希望跳过前几个元素，可以使用skip方法获取一个截取之后的新流：

```java
Stream<T> skip(long n);
```

如果流的当前长度大于n，则跳过前n个；否则将会得到一个长度为0的空流。基本使用如下：

![](..\图片\1-13【Lambda、Stream流】\6.png)

```java
public class Demo07Stream_skip {
    public static void main(String[] args) {
        // 获取一个Stream流
        String[] arr = {"张三", "李四", "王五", "赵六", "田七"};
        // 链式编程，创建一个Stream流，操作，输出
        Stream.of(arr).skip(3).forEach(name -> System.out.println(name));
    }
}
```

### 4.4.7 组合--concat

如果有两个流，希望合并成为一个流，那么可以使用Stream接口的静态方法concat；

```java
static <T> Strema<T> concat(Stream<? extends T> a, Stream<? extends T> b);
```

> 备注：这是一个静态方法，与java.lang.String当中的concat方法是不同的。

该方法的基本使用如下：

```java
public class Demo08Stream_concat {
    public static void main(String[] args) {
        String[] arr1 = {"张三", "李四", "王五", "赵六", "田七"};
        Stream<String> stream1 = Stream.of(arr1);
        String[] arr2 = {"林炫", "你好"};
        Stream<String> stream2 = Stream.of(arr2);

        // 把两个流合并为一个流
        Stream<String> concat = Stream.concat(stream1, stream2);
        // 遍历输出
        concat.forEach(name -> System.out.println(name));
    }
}
```

