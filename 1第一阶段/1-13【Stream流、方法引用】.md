# 第一章 Stream流

java8中，得益于Lambda所带来的函数式编程，引入了一个全新的Stream概念，用于解决已有的集合类库既有的弊端。

## 1.1 引言

### 传统集合的多步遍历代码

几乎所有的集合（如Collection接口或者Map接口等）都支持直接或者间接的遍历操作。而当我们需要对集合中的元素进行操作的时候，除了必需的添加、删除、获取外，最典型的就是集合的遍历。

```java
import java.util.ArrayList;
import java.util.List;

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

### 循环遍历的弊端

java8的Lambda表达式让我们可以专注于**做什么**（What），而不是**怎么做**（How），这点此前已经结合内部类进行了对比说明。现在，我们仔细体会一下上述代码，可以发现：

* for循环的语法就是**“怎么做”**
* for循环的循环体才是**“做什么”**

为什么使用循环？因为要进行遍历。但循环式遍历的唯一方式吗？遍历是指每一个元素逐一进行处理，而并不是从第一个到最后一个顺次处理的循环。前者是目的，后者是方式。

试想一下，如果希望对集合中的元素进行筛选过滤：

1. 将集合A根据条件一过滤为**子集B**。
2. 然后再根据条件二过滤为**子集C**。

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

每当我们需要对集合中的元素进行操作的时候，总是需要进行循环、循环、再循环。这是理所当然的吗？**当然不是**，循环只是做事情的方式，而不是目的。另一方面，使用线性循环就意味着只能遍历一次。如果希望再次遍历，只能在使用另一个循环从头开始。

### Stream的更优写法

下面请看Lambda表达式的衍生物Stream给我们带来的更优雅的写法：

<!--P438-->

```java
import java.util.ArrayList;
import java.util.List;

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

## 1.2 流式思想概述

<!--P439-->

**注意：请暂时忘记对传统IO流的固有印象！**

整体来看，流式思想类似于工厂车间的**“生产流水线”**。

当需要对多个元素进行操作的时候，考虑到性能及便利性，我们应该首先拼好一个模型步骤方案，然后按照方案去执行它。

![](..\图片\1-13【Stream流、方法引用】\1.png)

这张图展示了过滤、映射、跳过、计数等多步操作，这是一种集合元素的处理方案，而方案就是一种“函数模型”。图中每一个方框都是一个“流”，调用指定的方法，可以从一个流模型转换为另一个流模型。而最右侧的数字3是最终结果。

这里的`filter`、`map`、`skip`都是在对函数模型进行操作，集合元素并没有被真正的处理。只有当终结方法`count`执行的时候，整个模型才会按照指定策略执行操作。而这得益于Lambda的延迟执行特性。

> 备注：“Stream流”其实是一个集合元素的函数模型，它不是集合，也不是数据结构，其本身并不存储任何元素（或其地址）。

Stream（流）是一个来自数据源的元素队列

* 元素是特定类型的对象，形成一个队列。java中的Stream并不会存储元素，而是按照需求计算。
* **数据源**流的来源。可以是集合，数组等。

和以前的Collection操作不同，Stream操作还有两个基础的特征：

* **Pipelining**：中间操作都会返回流对象本身。这样多个操作可以串联一个管道，如同流式风格（fluent style）。这样做可以对操作进行优化，比如延迟执行（laziness）和短路（short-circuiting）。
* **内部迭代**：以前对集合遍历都是通过Iterator或者增强for的方式，显示的在集合外部进行迭代，这就叫做外部迭代。Stream提供了内部迭代的方式，流可以直接调用遍历方法。

当时用一个流的时候，通常包括三个基本步骤：获取一个数据源（source）-> 数据转换 -> 执行操作获取想要的结果，每次转换原有Stream对象不改变，返回一个新的Stream对象（可以有多次转换）,这就允许对齐操作可以先链条一样排列，变成一个管道。

## 1.3 获取流

<!--P440-->

`java.util.stream.Stream<T>`是java 8新加入的最常用的流接口。（**注意：这并不是一个函数式接口**）

获取一个流非常简单，有一下几种常用的方式：

* 所有的`Collection`集合都可以通过`stream`默认方法获取流；
* `Stream`接口的静态方法`of`可以获取数组对应的流。

```java
import java.util.*;
import java.util.stream.Stream;

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

## 1.4 常用方法

<!--P441-->

![](..\图片\1-13【Stream流、方法引用】\2.png)

流类型很丰富，这里介绍一些常用的API。这些方法可以分为两种：

* 延迟方法：返回值类型仍然是`Stream`接口自身类型的方法，因此支持链式调用。（除了终结方法外，其余方法均为延迟方法）
* 终结方法：返回值类型不再是`Stream`接口自身类型的方法，因此不在支持类似`StringBuilder`那样的链式调用。**本小节中，终结方法包括`count`和`forEach`方法。**

### 逐一处理：forEach

虽然名字叫做`forEach`，但是与for循环中的“for-each”昵称不同。

```java
void forEach(Consumer<? super T> action)
```

该方法接收一个Consumer接口函数，会将每一个流元素交个该函数进行处理。

#### 复习Consumer接口

```java
java.util.function.Consumer<T>接口是一个消费型接口。
Consumer接口中包含抽象方法void accept(T t),意为消费一个指定泛型的数据。
```

#### 基本使用：

```java
import java.util.stream.Stream;

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

### 过滤：filter

<!--P442-->

可以通过filter方法将一个流转换成另一个子集流。方法：

```java
Stream<T> filter (Predicate<? super T> predicate);
```

该接口接收一个`Predicate`函数式接口参数（可以是一个Lambda或者方法引用）作为筛选条件。

![](..\图片\1-13【Stream流、方法引用】\3.png)

#### 复习Predicate接口

此前学过`java.util.stream.Predicate`函数式接口，其中唯一的抽象方法为：

```java
boolean test(T t);
```

该方法将会产生一个Boolean值结果，代表指定的条件是否满足。如果结果为TRUE，那么Stream流的`filter`方法将会留用元素；如果结果为FALSE，那么`filter`方法将会舍弃元素。

#### 基本使用

```java
import java.util.stream.Stream;

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

<!--P443-->

**Stream流属于管道流，只能被消费，使用一次。第一个Stream流调用完毕后，数据会流转到下一个Stream上面。而这时候第一个Stream流已经使用完毕，就会自动关闭。所以第一个Stream流就不能使用了，如果再次使用会抛出异常。**

### 映射：map

<!--P444-->

如果需要将流中的元素映射到另一个流中，可以使用map方法。方法：

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
```

该接口需要一个Function函数式接口参数，可以将当前流中的T类型数据转换成为另一种R类型的流。

![](..\图片\1-13【Stream流、方法引用】\4.png)

#### 复习Function接口

此前我们已经学习过`java.util.stream.Function`函数式接口，其中唯一的抽象方法为：

```java
R apply(T t);
```

这可以将一种T类型转换成为R类型，而这种转换的动作，就成为“映射”。

#### 基本使用

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

### 统计个数：count

<!--P445-->

正如旧集合Collection当中的size方法一样，流提供count方法来数一数其中的元素的个数：

```java
long count();
```

该方法返回一个long值代表元素个数（不再像旧集合那样是int值）。基本使用：

```java
import java.util.ArrayList;
import java.util.stream.Stream;

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

### 取用前几个：limit

<!--P446 1.17-->

limit方法可以对流进行截取，只取用前n个。方法：

```java
Stream<T> limit(long maxSize);
```

参数是一个long型，如果集合当前长度大于参数则进行截取；否则不进行操作。基本使用：

![](..\图片\1-13【Stream流、方法引用】\5.png)

```java
import java.util.stream.Stream;

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

### 跳过前几个：skip

<!--P447-->

如果希望跳过前几个元素，可以使用skip方法获取一个截取之后的新流：

```java
Stream<T> skip(long n);
```

如果流的当前长度大于n，则跳过前n个；否则将会得到一个长度为0的空流。基本使用如下：

![](..\图片\1-13【Stream流、方法引用】\6.png)

```java
import java.util.stream.Stream;

public class Demo07Stream_skip {
    public static void main(String[] args) {
        // 获取一个Stream流
        String[] arr = {"张三", "李四", "王五", "赵六", "田七"};
        // 链式编程，创建一个Stream流，操作，输出
        Stream.of(arr).skip(3).forEach(name -> System.out.println(name));
    }
}
```

### 组合：concat

<!--P448-->

如果有两个流，希望合并成为一个流，那么可以使用Stream接口的静态方法concat；

```java
static <T> Strema<T> concat(Stream<? extends T> a, Stream<? extends T> b);
```

> 备注：这是一个静态方法，与java.lang.String当中的concat方法是不同的。

该方法的基本使用如下：

```java
import java.util.stream.Stream;

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

## 1.5 练习：集合元素处理(传统方式)

### 题目

<!--P449-->

现在有两个`ArrayList`集合存储队伍当中的多个成员姓名，要求使用传统的for循环（或者增强for循环）依次进行一下若干操作：

1. 第一个队伍只要名字为3个字的成员姓名；存储到一个新集合当中。
2. 第一个队伍筛选之后只要前3个人；存储到一个新集合当中。
3. 第二个队伍只要姓张的成员姓名；存储到一个新集合当中。
4. 第二个队伍筛选之后不要前两个人；存储到一个新集合当中。
5. 将两个队伍合并为一个队伍；存储到一个新集合当中。
6. 根据姓名创建`Person`对象；存储到一个新集合当中。
7. 打印整个队伍的`Person`对象信息。

两个队伍（集合）的代码如下：

```java
import java.util.ArrayList;

public class Demo01StreamTest {
    public static void main(String[] args) {
        // 第一支队伍
        ArrayList<String> one = new ArrayList<>();
        one.add("迪丽热巴");
        one.add("宋远桥");
        one.add("苏星河");
        one.add("石破天");
        one.add("石中玉");
        one.add("老子");
        one.add("庄子");
        one.add("洪七公");

        // 第二支队伍
        ArrayList<String> two = new ArrayList<>();
        two.add("古力娜扎");
        two.add("张无忌");
        two.add("赵丽颖");
        two.add("张三丰");
        two.add("尼古拉斯赵四");
        two.add("张天爱");
        two.add("张二狗");
    }
}
```

### 解答

```java
import java.util.ArrayList;

public class Demo01StreamTest {
    public static void main(String[] args) {
        // 第一支队伍
        ArrayList<String> one = new ArrayList<>();
        one.add("迪丽热巴");
        one.add("宋远桥");
        one.add("苏星河");
        one.add("石破天");
        one.add("石中玉");
        one.add("老子");
        one.add("庄子");
        one.add("洪七公");
        // 1. 第一个队伍只要名字为3个字的成员姓名；存储到一个新集合当中。
        ArrayList<String> one1 = new ArrayList<>();
        for (String name : one) {
            if (name.length() == 3) {
                one1.add(name);
            }
        }
        // 2. 第一个队伍筛选之后只要前3个人；存储到一个新集合当中。
        ArrayList<String> one2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            one2.add(one1.get(i));
        }

        // 第二支队伍
        ArrayList<String> two = new ArrayList<>();
        two.add("古力娜扎");
        two.add("张无忌");
        two.add("赵丽颖");
        two.add("张三丰");
        two.add("尼古拉斯赵四");
        two.add("张天爱");
        two.add("张二狗");
        // 3. 第二个队伍只要姓张的成员姓名；存储到一个新集合当中。
        ArrayList<String> two1 = new ArrayList<>();
        for (String name : two) {
            if (name.startsWith("张")) {
                two1.add(name);
            }
        }
        // 4. 第二个队伍筛选之后不要前两个人；存储到一个新集合当中。
        ArrayList<String> two2 = new ArrayList<>();
        for (int i = 2; i < two1.size(); i++) {
            two2.add(two1.get(i));
        }

        // 5. 将两个队伍合并为一个队伍；存储到一个新集合当中。
        ArrayList<String> all = new ArrayList<>();
        all.addAll(one2);
        all.addAll(two2);

        // 6. 根据姓名创建Person对象；存储到一个新集合当中。
        ArrayList<Person> list = new ArrayList<>();
        for (String name : all) {
            list.add(new Person(name));
        }

        // 7. 打印整个队伍的Person对象信息。
        for (Person person : list) {
            System.out.println(person);
        }
    }
}
```

## 1.6 练习：集合元素处理(Stream方式)

<!--P450-->

### 解答

```java
import java.util.ArrayList;
import java.util.stream.Stream;

public class Demo02StreamTest {
    public static void main(String[] args) {
        // 第一支队伍
        ArrayList<String> one = new ArrayList<>();
        one.add("迪丽热巴");
        one.add("宋远桥");
        one.add("苏星河");
        one.add("石破天");
        one.add("石中玉");
        one.add("老子");
        one.add("庄子");
        one.add("洪七公");
        // 1. 第一个队伍只要名字为3个字的成员姓名；存储到一个新集合当中。
        Stream<String> stream1 = one.stream();
        Stream<String> stream_filter1 = stream1.filter(name -> name.length() == 3);
        // 2. 第一个队伍筛选之后只要前3个人；存储到一个新集合当中。
        Stream<String> stream_limit = stream_filter1.limit(3);

        // 第二支队伍
        ArrayList<String> two = new ArrayList<>();
        two.add("古力娜扎");
        two.add("张无忌");
        two.add("赵丽颖");
        two.add("张三丰");
        two.add("尼古拉斯赵四");
        two.add("张天爱");
        two.add("张二狗");
        // 3. 第二个队伍只要姓张的成员姓名；存储到一个新集合当中。
        Stream<String> stream2 = two.stream();
        Stream<String> stream_filter2 = stream2.filter(name -> name.startsWith("张"));
        // 4. 第二个队伍筛选之后不要前两个人；存储到一个新集合当中。
        Stream<String> stream_skip = stream_filter2.skip(2);

        // 5. 将两个队伍合并为一个队伍；存储到一个新集合当中。
        Stream<String> concat = Stream.concat(stream_limit, stream_skip);

        // 6. 根据姓名创建Person对象；存储到一个新集合当中。
        Stream<Person> personStream = concat.map(name -> new Person(name));

        // 7. 打印整个队伍的Person对象信息。
        personStream.forEach(name -> System.out.println(name));
    }
}
```

# 第二章 方法引用

<!--P451-->

在使用Lambda表达式的时候，我们实际上传递进去的就是一种解决方案：拿什么参数做什么操作。那么考虑一种情况：如果我们在Lambda中所指定的操作方案，已经有地方存在相同方案，那是否还有必要再写重复逻辑？

## 2.1 冗余的Lambda场景

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

## 2.2 方法引用符

双冒号`：：`为引用运算符，而他所在的表达式被称为**方法引用**。

**如果Lambda要表达的函数方案已经存在于某个方法的实现中，那么则可以通过双冒号来引用该方法作为Lambda的替代者。**

### 语义分析

例如上例中，`System.out`对象中有一个存在的`println(String)`方法恰好就是我们所需要的。那么对于`printString`方法和函数式接口参数，对比下面两种写法，完全等效：

* Lambda表达式：`s -> System.out.println(s)`;
* 方法引用写法：`System.out::println`；

第一种语义是指：拿到参数之后经Lambda之手，继而传递给`System.out.println()`方法去处理。

第二种等效写法的语义是指：直接让`System.out`中的`println`方法来取代Lambda。两种写法的执行效果完全一样，而第二种方法引用的写法复用了已有方案，更加简洁。

> 备注：Lambda中 传递的参数 一定是方法引用中的那个方法可以接收的类型，否则会抛出异常。

### 推导与省略

如果使用Lambda，那么根据“**可推导就是可省略**”的原则，无需指定参数类型，也无需指定的重载形式——他们都将被自动推导，而如果使用方法引用，也是同样可以根据上下文进行推导。

**函数式接口是Lambda的基础，而方法引用是Lambda的孪生兄弟。**

## 2.3 通过对象名引用成员方法

<!--P452-->

```java
// 函数式接口
public interface Printable {
    void print(String s);
}


public class MethodRerObject {
    // 定义一个成员方法，传递字符串，把字符串按照大小写输出
    public void printUpperCaseString(String str) {
        System.out.println(str.toUpperCase());
    }
}


import cn.com.demo04.Stream.Printable;

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
    }
}
```

## 2.4 通过类名成引用静态方法

<!--P453-->

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

## 2.5 通过super引用成员方法

<!--P454-->

```java
// 定义见面的函数式接口
@FunctionalInterface
public interface Greetable {
    // 定义一个见面的方法
    void greet();
}


// 定义父类
public class Human {
    // 定义一个sayHello的方法
    public void sayHello() {
        System.out.println("Hello, 我是父类方法");
    }
}


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

## 2.6 通过this引用成员方法

<!--P455-->

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

## 2.7 类的构造器引用

<!--P456-->

由于构造器的名称与类名完全一样，并不固定。所以构造器引用使用`类名称::new`的格式来表示。

```java
@FunctionalInterface
public interface PersonBuilder {
    Person builderPerson(String name);
}


public class Person {
    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


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

## 2.8 数组的构造器引用

<!--P457-->

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
import java.util.Arrays;

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