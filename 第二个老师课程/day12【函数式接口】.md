# 第一章 函数式接口

## 1.1 概念

<!--P419-->

函数式接口在java中是指：<font color = "red">**有且仅有一个抽象方法的接口。**</font>

函数式接口，即适用于函数式编程场景的接口。而java中的函数式编程体现就是Lambda，所以**函数式接口就是可以适用于Lambda适用的接口**。只有确保接口中有且仅有一个抽象方法，java中的Lambda才能顺利地进行推导。

> 备注：“语法糖”是指适用更加方便，但是原理不变的代码语法。例如在遍历集合时会用的for-each语法，其实底层实现原理依旧是迭代器，这就是“语法糖”。
>
> 从应用层面来讲，java中的Lambda可以被当做是匿名内部类的”语法糖“，但是二者在原理上是**不同**的。

## 1.2 格式

只要确保接口中有且仅有一个抽象方法即可：

```java
修饰符 interface 接口名称 {
    public abstract 返回值类型 方法名称（可选参数信息）;
    // 其他非抽象方法内容
}
```

由于接口当中抽象方法的`public abstract`是可以省略的，所以定义一个函数式接口其实很简单：

```java
public interface MyFunctionaliInterface{
    void myMethod();
}
```

## 1.3 @FunctionalInterface注解

<font color = "red">@FunctionalInterface注解</font>作用：可以检测接口是否是一个函数式接口

* 是：编译成功
* 否：编译失败（接口中没有抽象方法/抽象方法的个数多余1个）

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    // 定义一个抽象方法
    public abstract void method();
}
```

## 1.4 自定义函数式接口

<!--P420-->

函数式接口的使用：一般可以作为方法的参数和返回值类型。

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    // 定义一个抽象方法
    public abstract void method();
}


public class MyFunctionalInterfaceImpl implements MyFunctionalInterface{
    @Override
    public void method() {
        System.out.println("调用show方法，方法的参数是一个接口，所以可以传递接口的实现类对象");
    }
}


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

# 第二章 函数式编程

## 2.1 Lambda的延迟执行

<!--P421-->

> 注：日志可以帮助我们快速的定位问题，记录程序运行过程中的情况，以便项目的监控和优化。

一种典型的场景就是对参数进行有条件使用，例如对日志消息进行拼接后，在满足条件的情况下进行打印输出

### 性能浪漫的日志案例

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

### 体验Lambda的更优写法

<!--P422-->

使用Lambda表达式作为参数传递，仅仅是把参数传递到showLog方法中

只有满足条件，日志的等级是1级，才会调用MessageBuilder接口中的方法builderMessage，才会进行字符串的拼接。

如果条件不满足，日志等级不是1级，那么不会调用，也不会执行。

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

## 2.2 使用Lambda作为参数和返回值

<!--P423-->

如果抛开实现原理不说，java中的Lambda表达式可以被当做是匿名内部类的替代品。如果方法的参数是一个函数式接口类型，那么就可以使用Lambda表达式进行代替。

**使用Lambda表达式作为方法参数，其实就是使用函数式接口作为方法参数。**

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

<!--P424-->

类似地，如果一个方法的返回值类型是一个函数式接口，那么就可以直接返回一个Lambda表达式。当需要通过一个方法来获取一个`java.util.Comparator`接口类型的对象作为排序器时，就可以调该方法获取。

```java
import java.util.Arrays;
import java.util.Comparator;

public class Demo02Comparator {
    // 定义一个方法，方法的返回值类型使用函数式接口Comparator
    public static Comparator<String> getComparator() {
        // 方法的返回值类型是一个接口，那么我们可以返回这个接口的匿名内部类
/*        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 按字符串降序设置
                return o2.length() - o1.length();
            }
        };*/

        // 方法的返回值类型是一个函数式接口，所以我们可以返回一个Lambda表达式
        /*return (String o1, String o2) -> {
            return o2.length() - o1.length();
        };*/

        // 优化Lambda表达式
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

# 第三章 常用函数式接口

<!--P425 1.15-->

JDK提供了大量的常用的函数式接口以丰富Lambda的典型使用场景，它们主要在`java.util.function`包中被提供。

下面是最简单的几个接口及 使用实例。

## 3.1 Supplier接口

<!--Supplier:供应商; 供货商; 供应者; 供货方;-->

`java.util.function.Supplier<T>`接口仅包含一个无参的方法：`T get()`。用来获取一个泛型参数指定类型的对象数据。由于这是一个函数式接口，这就意味着对应的Lambda表达式需要“对外提供”一个符合泛型类型的对象数据。

`Supplier<T>`接口被称为生产性接口，指定接口的泛型是什么类型，那么接口中的`get`方法就会生产什么类型的数据。

```java
import java.util.function.Supplier;

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

## 3.2 练习： 求数组元素最大值

<!--P426-->

### 题目

使用`Supplier`接口作为方法参数类型，通过Lambda表达式求出int数组的最大值。

提示：接口的泛型使用`java.lang.Integer`类。

### 解答

```java
import java.util.function.Supplier;

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

## 3.3 Consumer接口

<!--P427-->

`java.util.function.Consumer<T>`接口正好与`Supplier`接口相反，它不是生产一个数据，而是消费一个数据，其数据类型由泛型决定。

### 抽象方法：accept

`Consumer`接口中包含抽象方法`void accept(T t)`，意为消费一个指定泛型的数据。基本使用如：

```java
import java.util.function.Consumer;

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

### 默认方法：andThen

<!--P428-->

如果一个方法的参数和返回值全部都是`Consumer`类型，那么就可以实现效果：消费数据的时候，首先做一个操作，然后再做一个操作，实现组合。而这个方法就是`Consumer`接口中的`default`方法`andThen`。

下面是JDK的源代码：

```java
default Consumer<T> andThen (Consumer<? super T> after) {
    Objects.requireNonNull(after);
    return (T t) -> { 
    	accept(t);
    	after.accept(t);
    }
}

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
import java.util.function.Consumer;

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

## 3.4 练习：格式化打印信息

<!--P429-->

### 题目

下面的字符串数组当中存有多条信息，请按照“姓名：XX。性别：XX。”的格式将信息打印出来。要求将打印姓名的动作作为第一个Consumer接口的Lambda实例，将打印性别的动作作为第二个Consumer接口的Lambda实例，将两个Consumer接口按照顺序“拼接”到一起。

```java
public static void main(String[] args) {
    String[] array = {"迪丽热巴, 女", "古力娜扎, 女", "马尔扎哈, 男"};
}
```

### 解答

```java
import java.util.function.Consumer;

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

## 3.5 Predicate接口

<!--P430-->

有时候我们需要对某种类型的数据进行判断，从而得到一个boolean值结果。这时候可以使用`java.util.function.Predicate<T>`接口。

### 抽象方法：test

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

### 默认方法：and

<!--P431-->

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
        boolean b = checkString(str, (String s1) -> {
            // 判断字符串的长度是否大于5
            return s1.length() > 5;
        }, (String s2) -> {
            // 判断字符串中是否包含a
            return s2.contains("a");
        });
        System.out.println(b);
    }
}
```

### 默认方法：or

<!--P432-->

与`and`的“与”类似，默认方法`or`实现逻辑关系中的“**或**”。JDK源代码为：

```java
default Predicate<T> or(Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) || other.test(t);
}
```

### 默认方法：negate

"与"、"或"已经了解了，剩下的"非"（取反）也很简单。默认方法negate的JDK源代码为：

```java
default Predicate<T> negate() {
    return (t) -> !test(t);
}
```

很容易可以看出，它是执行了test方法后，对结果Boolean值进行"!"取反而已。

## 3.6 练习：集合信息筛选

<!--P433-->

### 题目

数组当中有多条“姓名+性别”的信息如下，请通过Predicate接口的拼装将符合要求的字符串筛选到结合ArrayList中，需要同时满足两个条件：

1. 必须为女生。
2. 姓名为4个字。

```java
public class DemoPredicate {
    public static void main(String[] args) {
        String[] arr = {"迪丽热巴, 女", "古力娜扎, 女", "马尔扎哈, 男", "赵丽颖, 女"};
    }
}
```

### 解答

```java
import java.util.ArrayList;
import java.util.function.Predicate;

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

## 3.7 Function接口

<!--P434 1.16-->

`java.util.function.Function<T, R>`接口用来根据一个类型的数据得到另一个类型的数据，前者为前置条件，后者称为后置条件。

### 抽象方法：apply

`Function`接口中最主要的抽象方法为：`R apply(T t)`，根据类型T的参数获取类型R的结果。

使用场景例如：将`String`类型转换为`Integer`类型。

```java
import java.util.function.Function;

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

### 默认方法：andThen

<!--P435-->

`Function`接口中有一个默认的`andThen`方法，用来进行组合操作。JDK源代码：

```java
default <V> Function<T,V> andThen (Function<? super R,? extends V> after) {
    Objcets.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
}
```

该方法同样用于“先做什么，再做什么”的场景，和`Consumer`中的`andThen`差不多

看一下下面一个例子：

第一个操作时将字符串解析成为int数字，第二个操作时乘以10。两个操作通过`andThen`

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

## 3.8 练习：自定义函数模型拼接

<!--P436-->

### 题目

请使用`Function`进行函数模型的拼接，按照顺序需要执行的多个函数操作为：

​	`String str = “赵丽颖，20”；`

1. 将字符串截取数字年龄部分，得到字符串；
2. 将上一步的字符串转换成为int类型的数字；
3. 将上一步的int数字累加100，得到结果数字。

### 解答

**第一种解：**

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

**第二种解：**

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