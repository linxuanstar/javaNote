# 第一章 List的子类

## 1.1 ArrayList集合

`ArrayList`是一个动态数组，查询快、增删慢。`ArrayList`是线程不安全的，运行效率快，允许元素为`null`。

1. <font color = "red">ArrayList与LinkedList的区别有哪些？</font>

   * `ArrayList`的底层数据结构为数组，增删慢、查询快，线程不安全，效率高。

   * `LinkedList`的底层数据结构为链表，增删快、查询慢，线程不安全，效率高。

2. <font color = "red">ArrayList与Vector的区别？</font>

   * `Vector`底层数据结构为数组，增删慢、查询快，线程安全，效率低，每次扩容为原来数组的2倍。

   * `ArrayList`底层数据结构为数组，增删慢、查询快，线程不安全，效率高，每次扩容为原来数组的1.5倍。

3. <font color = "red">ArrayList的底层是数组，数组的名称是什么？类型是什么？</font>

   - 名称是`elementData`，类型是`Object[]`，所以`ArrayList`里面可以存放任意类型的元素。

4. <font color = "red">ArrayList底层数组的默认初始化容量是多少？当超出这个大小时，每次扩容是多少？</font>

   * 默认初始化容量是10。当超出这个大小时，每次扩容1.5倍。

5. <font color = "red">LinkedList的底层是什么？</font>

   * 双向链表。

6. <font color = "red">ArrayList里面可以存null吗？</font>

   * 可以。

7. <font color = "red">ArrayList的底层是数组，它和数组有什么区别吗？</font>

   * ArrayList区别于数组的地方在于能够自动扩展大小，每次扩容1.5倍。

8. <font color = "red">当向ArrayList集合中添加元素时需要调用add()方法，add()方法的执行流程是怎样的？</font>

   * 调用`add()`方法时，`add()`方法首先调用`ensureCapacityInternal()`来判断`elementData`数组容量是否足够。

     `ensureCapacityInternal()`之所以能够判断，是因为它内部调用了`ensureExplicitCapacity()`方法，这个方法才是真正判断`elementData`数组容量是否够用的关键方法。

     如果容量足够，则直接将元素添加到`ArrayList`中；如果容量不够，则`ensureExplicityCapacity()`方法内部会调用`grow()`方法来对数组进行扩容。扩容成功之后，再将元素添加到`ArrayList`扩容之后的新数组中。

   > 注意：如何扩容呢？会先创建一个原来数组1.5倍大小的新数组，然后将数据拷贝到新数组中。

9. <font color = "red">在调用ArrayList的remove(int index)方法时，执行流程是怎样的？</font>

   * 首先判断`index`是否合理，如果合理的话，会调用`System.arraycopy()`方法把指定下标到数组末尾的元素向前移动一个单位，并且会把数组最后一个元素设为`null`。这样是为了方便GC回收。

10. <font color = "red">ArrayList在调用get(int index)方法查询的时候，执行流程是怎样的？</font>

    * 首先判断`index`是否合理，然后调用`elementData()`方法，`elementData()`方法返回根据i`ndex`查到的具体的元素。

    > 注意：这里的返回值都经过了向下转型（Object -> E）。

11. <font color = "red">ArrayList的大小是如何自动增加的？你能分享一下你的代码吗？</font>

    * 当向`ArrayList`中增加一个对象的时候，Java首先会判断`ArrayList`的底层数组`elementData`是否还有足够的空间来存储这个对象，如果有，就直接存，如果没有，就会基于原有的数组扩容出一个1.5倍的新数组，并将数据全部复制到新数组中。

    > 请注意这样一个情况，新建了一个数组，旧数组的对象被复制到了新的数组中，并且现有的数组引用指向新的数组。

12. <font color = "red">什么情况下你会使用ArrayList？什么情况下你会选择LinkedList？</font>

    * 当对数据的主要操作为索引或只在集合的末端增加、删除数据时，使用`ArrayList`效率比较高；
    * 当对数据的操作主要为指定位置的插入或删除操作时，使用`LinkedList`效率比较高。

13. <font color = "red">在ArrayList的增、删、改、查中，什么地方会修改modCount？</font>

    * 增如果导致扩容，则会修改`modCount`；删一定会修改`modCount`；改和查一定不会修改`modCount`。

14. <font color = "red">为什么ArrayList在增、删的时候效率低？</font>

    * 因为在增、删的过程中会涉及到数组的复制，效率低。

15. <font color = "red">ArrayList的时间复杂度是多少？</font>

    * 当修改、查询或者只在数组末尾增、删时，时间复杂度为`O(1)`；
    * 对指定位置的元素进行增、删时，时间复杂度为`O(n)`。

## 1.2 ArrayList 和 LinkedList区别

![](D:\Java\笔记\图片\day03【List、Set、数据结构、Collection】\1-ArrayList 和 LinkedList区别.png)

`ArrayList`与`LinkedList`都是`List`接口的实现类， `List`是一个接口又继承了`Collection`接口。

* `ArrayList`的方法： `get()` 、`add()`、`remove()`
* `LinkedList`:的方法：`addFirst()`、`addLast()`、`getFirst()`、`getLast()`、`removeFirst()`、`removeLast()`

### 底层实现

从底层实现角度来看：

- `ArrayList`：是基于动态数组的数据结构；名称是`elementData`，类型是`Object[]`，所以`ArrayList`里面可以存放任意类型的元素

- `LinkedList`：是基于双向循环链表的数据结构

### 访问元素

从随机访问（索引）元素角度来看：

- `ArrayList`：数据存储是连续的，因此支持用下标来访问元素`get(int index)`，直接返回`index`位置上的元素，随机访问元素速度快 `O(1)`
- `LinkedList`：需要通过`for`循环进行查找，虽然`LinkedList`已经在查找方法上做了优化，比如`index < size / 2`，则从左边开始查找，反之从右边开始查找，但是还是比`ArrayList`要慢。 `O(N)`

### 插入和删除元素

从插入，删除元素角度来看：

- `ArrayList`：想要在指定位置插入或删除元素时，主要耗时的是`System.arraycopy`动作，会移动`index`后面所有的元素，从而会重新调整索引顺序,调整索引顺序会消耗一定的时间，所以速度上就会比`LinkedList`要慢许多；`O(N)`
- `LinkedList`：主耗时的是要先通过`for`循环找到`index`，再改变前后对象的引用，然后直接插入或删除，效率较高。`O(N)`

### 初始容量和扩容

从初始容量和扩容来看：

- `ArrayList`：默认初始化容量是10，当存储元素大小超过初始容量时，需要动态扩容为原来的1.5倍加1。

  如果设置后的新容量还不够，则直接新容量设置为传入的参数（也就是所需的容量），而后用`Arrays.copyof()`方法将元素拷贝到新的数组。从中可以看出，当容量不够时，每次增加元素，都要将原来的元 素拷贝到一个新的数组中，非常之耗时。因此我们可以指定容量，避免扩容，

- `LinkedList`：使用了链表的结构，因此不需要维护容量的大小。

### 线程安全

从线程安全角度来看：

- 二者都是非线程安全的容器。


### 实现栈和队列

从实现栈和队列角度来看：

- `LinkedList`要优于`ArrayList`。因为`LinkedList`是个双向链表，它同样可以被当作栈、队列或双端队列来使用。

  例如`Queue queue=new LinkedList<>()`;

### 总结

<font color = "red">**当对数据的主要操作为索引或者只在集合末端增加、删除元素时候，使用ArrayList效率比较高。当对数据的主要操作为指定位置的插入或者删除操作时，使用LinkedList效率比较高。**</font>

# 第二章 Map集合

## 2.1概述

## 2.9 Map集合联系

**需求：**

计算一个字符串每个字符出现次数。

**分析：**

1. 获取字符串对象
2. 创建一个Map集合，键代表字符，值代表次数。
3. 遍历字符串得到每个字符。
4. 判断Map中是否有该键。
5. 如果没有，第一次出现，存储次数为1；如果有，则说明已经出现过，获取到对应的值进行++，再次存储。
6. 打印最终结果。

**代码如下：**

```java
import java.util.HashMap;
import java.util.Scanner;

public class Demo03MapTest {

    public static void main(String[] args) {
        // 使用Scanner获取用户输入的字符串
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String str = sc.next();
        // 创建Map集合，key是字符串中的字符，value是字符的个数
        HashMap<Character, Integer> map = new HashMap<>();
        // 遍历字符串，获取每一个字符
        for (char c : str.toCharArray()) {
            // 使用获取到的字符，去Map集合判断key是否存在
            if (map.containsKey(c)) {
                // key存在
                Integer value = map.get(c);
                value++;
                map.put(c, value);
            } else {
                map.put(c, 1);
            }
        }
        // 遍历Map集合，输出结果
        for (Character key : map.keySet()) {
            Integer value = map.get(key);
            System.out.println(key + "=" + value);
        }
    }
}
```

# 第三章 补充知识点

## 3.1 JDK9对集合添加的优化

通常，我们在代码中创建一个集合（例如，List或者Set），并直接用一些元素填充它，实例化集合，几个add方法调用，是的代码重复。

**集合往里面添加元素，单列集合使用add；双列集合Map可以使用put方法。**

**代码如下：**

```java
import java.util.List;
import java.util.ArrayList;

public class Demo04 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("def");
        list.add("ghi");
        System.out.println(list);
    }
}
```

但是，如上代码所示，这种方法有点麻烦，一次只能添加一个元素。

Java 9，添加了几种集合工厂方法，更方便创建少量元素的集合、map实例。新的List、Set、Map的静态工厂方法可以更加方便的创建集合的不可变实例。

**特性：**

​	List接口， Set接口，Map接口，里面增加了一个静态的方法of，可以给集合一次性添加多个元素

**实例：**

​	static<E> List<E> of (E... elements)

**使用前提：**

​	当集合中存储的元素的个数已经确定了，不再改变时使用

**注意：**

1. of方法只适用于List接口，Set接口，Map接口，不适用于接口的实现类（ArrarList, HashSet, HashMap）。
2. of方法的返回值是一个不能改变的集合，集合不能再使用add，put方法添加元素，会抛出异常。
3. Set接口和Map接口在调用of方法的时候，不能有重复的元素，否则会抛出异常。
4. of方法前面没有new。

**代码如下：**

```java
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Demo01JDK9 {

    public static void main(String[] args) {
        List<String> list = List.of("a", "b", "a", "c", "d");
        System.out.println(list);
        // list.add("w");// UnsupportedOperationException 不支持操作异常

        // Set<String> set =Set.of("a", "b", "a", "c", "d");// IllegalArgumentException 非法参数异常 有重复的元素
        Set<String> set =Set.of("a", "b", "c", "d");
        System.out.println(set);
        // set.add("w");// UnsupportedOperationException 不支持操作异常

        // Map<String, Integer> map = Map.of("张三", 18, "李四", 19, "王五", 20, "张三", 19);// IllegalArgumentException 非法参数异常 有重复的元素
        Map<String, Integer> map = Map.of("张三", 18, "李四", 19, "王五", 20);// IllegalArgumentException 非法参数异常 有重复的元素
        System.out.println(map);
        // map.put("赵四", 30);// UnsupportedOperationException 不支持操作异常
    }
}

```

## 3.2Debug追踪

**使用IDEA的断点调试功能，查看程序的运行过程**

1. 在有效代码行，点击行号右边的空白区域，设置断点，程序执行到断点将停止，我们可以手动来运行程序。
2. 点击Debug运行模式

**Debug调试程序：**

​	可以让代码逐行执行，查看代码执行的过程，调试程序中出现的bug

**使用方式：**

1. 在行号的右边，鼠标左键单击，添加断点（每个方法的第一行，等熟悉之后可以哪里有bug添加到哪里）
2. 右键，选择Debug执行程序
3. 程序就会停留在添加的第一个断点处

**执行程序：**

* F8：逐行执行程序
* F7：进入到方法中
* Shift + F8：跳出方法
* F9：跳到下一个断点，如果没有下一个断点，那么就结束程序
* Ctrl + F2：退出Debug模式，停止程序
* Console：切换到控制台

# 第四章 模拟斗地主洗牌发牌（P286）

## 4.1 案例介绍

按照斗地主的规则，完成洗牌发牌的动作。

具体规则：

1. 组装54张扑克牌将
2. 54张牌顺序打乱
3. 三个玩家参与游戏，三人交替摸牌，没人17张牌，最后三张留作底牌
4. 查看三人各自手中的牌（按照牌的大小进行排序）、底牌

> 规则：手中扑克牌从大到小的摆放顺序：大王,小王,2,A,K,Q,J,10,9,8,7,6,5,4,3

## 4.2 案例需求分析

1. 准备牌
2. 洗牌
3. 发牌
4. 排序
5. 看牌

**代码如下（P287）：**

```java
import java.util.*;

public class DouDIZhu {
    public static void main(String[] args) {
        /*
            1,准备牌
            创建一个Map集合，存储牌的索引和组装好的牌
         */
        HashMap<Integer, String> poker = new HashMap<>();
        // 创建一个List集合，存储牌的索引
        ArrayList<Integer> pokerIndex = new ArrayList<>();
        // 定义两个集合，存储花色和牌的序号
        List<String> colors = List.of("♥", "♠", "♣", "♦");
        List<String> numbers = List.of("2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3");
        // 把大王和小王存储到集合中
        // 定义一个牌的索引
        int index = 0;
        poker.put(index, "大王");
        pokerIndex.add(index);
        index++;
        poker.put(index, "小王");
        pokerIndex.add(index);
        index++;
        // 循环嵌套遍历两个集合，组装52张牌，存储到集合中
        for (String number : numbers) {
            for (String color : colors) {
                poker.put(index, color + number);
                pokerIndex.add(index);
                index++;
            }
        }
        // System.out.println(poker);
        // System.out.println(pokerIndex);

        /*
           2.洗牌
           使用Collections中的方法shuffle(List)
         */
        Collections.shuffle(pokerIndex);
        // System.out.println(pokerIndex);

        /*
            3.发牌
            定义四个集合，存储玩家牌的索引和底牌的索引
         */
        ArrayList<Integer> player01 = new ArrayList<>();
        ArrayList<Integer> player02 = new ArrayList<>();
        ArrayList<Integer> player03 = new ArrayList<>();
        ArrayList<Integer> dipai = new ArrayList<>();
        // 遍历存储牌索引的List集合，获取每一个牌的索引
        for (int i = 0; i < pokerIndex.size(); i++) {
            Integer in = pokerIndex.get(i);
            // 先判断底牌
            if (i >= 51) {
                // 给底牌发牌
                dipai.add(in);
            } else if (i % 3 == 0) {
                // 给玩家1发牌
                player01.add(in);
            } else if (i % 3 == 1) {
                // 给玩家2发牌
                player02.add(in);
            } else if (i % 3 == 2) {
                // 给玩家3发牌
                player03.add(in);
            }
        }

        /*
            4.排序
            使用Collections中的方法sort(List),默认是升序排序
         */
        Collections.sort(player01);
        Collections.sort(player02);
        Collections.sort(player03);
        Collections.sort(dipai);

        /*
            5.看牌
            调用看牌的方法
         */
        lookpoker("刘德华", poker, player01);
        lookpoker("周润发", poker, player02);
        lookpoker("周星驰", poker, player03);
        lookpoker("底牌", poker, dipai);
    }

    /*
        定义一个看牌的方法，提高代码的复用性
     */
    public static void lookpoker(String name, HashMap<Integer, String> poker, ArrayList<Integer> list) {
        // 输出玩家名称，不换行
        System.out.print(name + ": ");
        // 遍历玩家或者底牌集合，获取牌的索引
        for (Integer key : list) {
            // 使用牌的索引，去Map集合中找到对应的值
            String value = poker.get(key);
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
```

