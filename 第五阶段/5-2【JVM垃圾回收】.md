# 第一章 判断对象是否可回收

有两个算法，一个是引用计数法，还有一个是可达性分析算法。

## 1.1 引用计数法

引用计数法：每个对象上都有一个引用计数，对象每被引用一次，引用计数器就+1，对象引用被释放，引用计数器-1，直到对象的引用计数为0，对象就标识可以回收

但是这个算法有明显的缺陷，对于循环引用的情况下，循环引用的对象就不会被回收。例如下图：A对象，B对象循环引用，没有其他的对象引用A和B，则A和B都不会被回收。

![](D:\Java\笔记\图片\5-1【jvm】\9-1.png)

## 1.2 可达性分析算法

Java 虚拟机中的垃圾回收器采用可达性分析算法来探索所有存活的对象。

可达性分析算法：扫描堆中的对象，看是否能够沿着 GC Root对象为起点的引用链找到该对象，找不到，表示可以回收。

GC Root：根对象。也就是这几个对象是jvm虚拟机不会被回收的对象，所以这些对象引用的对象都是在使用中的对象，这些对象未使用的对象就是即将要被回收的对象。简单就是说：如果对象能够达到root，就不会被回收，如果对象不能够达到root，就会被回收。

可以作为GC Root的有：

- 虚拟机栈(栈帧中的本地变量表)中引用的对象，`就是在方法中new的对象`。
- 方法区中静态属性引用的对象，`用static修饰的全局的静态的对象`。
- 方法区中常量引用的对象，`static final关键字`。
- 本地方法栈中(Native方法)引用的对象，`引用Native方法的所有对象`。

## 1.3 五种引用

说是四种引用，但是实际有五种引用。

> 实线代表强引用，虚线代表软弱虚引用。

![](D:\Java\笔记\图片\5-1【jvm】\9-2.png)

### 1.3.1 强引用

其实我们平时在用的所有的引用都属于强引用。比如，new了一个对象，同等号赋值给了一个变量，那么这个变量强引用了刚刚的对象。

强引用的特点是，只要沿着GC Root的引用，能够找到他，那么他就不会被垃圾回收。

只有所有GC Roots对象都不通过【强引用】引用该对象，该对象才能被垃圾回收。如上图B、C对象都不引用A1对象时，A1对象才会被回收

### 1.3.2 软引用

当GC Root指向软引用对象时，在内存不足时，会回收软引用所引用的对象，当然前提是没有其他强应用在引用它。如上图如果B对象不再引用A2对象且内存不足时，软引用所引用的A2对象就会被回收

来看下面这种情况：

```java
// 设置JVM参数：-Xmx20m -XX:+PrintGCDetails -verbose:gc 堆内存设置为20M
public class Demo01 {
	private static final int _4MB = 4 * 1024 * 1024;
	
	public static void main(String[] args) throws IOException {
		List<byte[]> list = new ArrayList<>();
		for (int i = 0; i < 5; i ++) {
             // 由于堆内存目前大小是20兆，所以循环5次之后，肯定超过堆内存大小了。
			list.add(new byte[_4MB]);
		}
	}
}
```

由于堆内存设置为20M，而List集合中假如的byte数组就20M了，再加上一些其他的对象，肯定第五次循环就会导致内存泄漏。`java.lang.OutOfMemoryError:Java heap space`。

而对于软引用的话就不会出现这种情况。软引用引用的对象在内存不足时会被回收：

```java
// 设置JVM参数：-Xmx20m -XX:+PrintGCDetails -verbose:gc 堆内存设置为20M
public class Demo02 {
    private static final int _4MB = 4 * 1024 * 1024;

    public static void main(String[] args) throws IOException {
        // SoftReference软引用，引用的对象为byte[]。list和SoftReference之间是强引用
        List<SoftReference<byte[]>> list = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            // 构建软引用对象，软引用ref引用byte数组对象
            SoftReference<byte[]> ref = new SoftReference<>(new Byte[_4MB]);
            // 软引用的get方法，获取内部包装的数组new Byte[_4MB]
            System.out.println(ref.get()); 
            list.add(ref);
            System.out.println(list.size());
        }
        System.out.print("循环结束：" + list.size());
        for (SoftReference<byte[]> ref : list) {
            System.out.println(ref.get());
        }
    }
}
```

第五次向List集合中添加byte[]的时候堆内存就不够了，所以就会回收弱引用对象引用的对象，也就是之前产生的byte数组。

如果在垃圾回收时发现内存不足，在回收软引用所指向的对象时，软引用本身不会被清理

如果想要清理软引用，需要使用引用队列。当软引用对象引用的对象被回收的时候，软引用对象自己也会假如到引用队列里面，所以到时候我们只需要从队列里面获取该对象并删除即可。

```java
public class Demo01 {

    public final static int _4MB = 4 * 1024 * 1024;

    public static void main(String[] args) throws Exception {
        List<SoftReference<byte[]>> list = new ArrayList<>();
        // 构建引用队列
        ReferenceQueue<byte[]> queue = new ReferenceQueue<>();

        for (int i = 0; i < 5; i ++) {
            // 构建软引用对象，软引用ref真正引用byte数组对象
            SoftReference<byte[]> ref = new SoftReference<>(new byte[_4MB], queue);
            // 软引用的get方法，获取内部包装的数组new Byte[_4MB]
            System.out.println(ref.get());
            list.add(ref);
            System.out.println(list.size());
        }

        // 从队列中，获取最先放入队列的元素移除队列，返回值是Reference，他是SoftReference的父类型。
        Reference<? extends byte[]> poll = queue.poll();
        while (poll != null) {
            // 从list里remove软引用对象。这样的话，不需要的软引用对象，从list中清除掉了
            list.remove(poll);
            poll = queue.poll();
        }

        System.out.print("循环结束：" + list.size());
        for (SoftReference<byte[]> ref : list) {
            System.out.println(ref.get());
        }
    }
}
```

大概思路为：查看引用队列中有无软引用，如果有，则将该软引用从存放它的集合中移除（这里为一个list集合）

### 1.3.3 弱引用

![](D:\Java\笔记\图片\5-1【jvm】\9-2.png)

只有弱引用引用该对象时，在垃圾回收时，无论内存是否充足，都会回收弱引用所引用的对象。如上图如果B对象不再引用A3对象，则A3对象会被回收

弱引用的使用和软引用类似，只是将 SoftReference 换为了 WeakReference，可以配合引用队列来释放弱引用自身。

### 1.3.4 虚引用

当虚引用对象所引用的对象被回收以后，虚引用对象就会被放入引用队列中，调用虚引用的方法。

- 虚引用的一个体现是释放直接内存所分配的内存。创建ByteBuffer的实现类对象时，他就会创建一个叫Cleaner的虚引用对象，当引用的对象ByteBuffer被垃圾回收以后，虚引用对象Cleaner就会被放入引用队列中，然后会有一个叫做ReferenceHandler的线程调用Cleaner的clean方法来释放直接内存。
- 如上图，B对象不再引用ByteBuffer对象，ByteBuffer就会被回收。但是直接内存中的内存还未被回收。这时需要将虚引用对象Cleaner放入引用队列中，然后调用它的clean方法来释放直接内存。

### 1.3.5 终结器引用

所有的类都继承自Object类，Object类有一个finalize方法。当某个对象不再被其他的对象所引用时，会先将终结器引用对象放入引用队列中，然后根据终结器引用对象找到它所引用的对象，然后调用该对象的finalize方法。调用以后，该对象就可以被垃圾回收了

如上图，B对象不再引用A4对象。这时终结器对象就会被放入引用队列中，引用队列会根据它，找到它所引用的对象。然后调用被引用对象的finalize方法。调用以后，该对象就可以被垃圾回收了。

### 1.3.6 引用队列

- 软引用和弱引用可以配合引用队列。在软引用和弱引用所引用的对象被回收以后，会将这些引用放入引用队列中，方便一起回收这些软/弱引用对象
- 虚引用和终结器引用必须配合引用队列。虚引用和终结器引用在使用时会关联一个引用队列

# 第二章 垃圾回收算法

判断一个对象是否是垃圾，就是沿着GC Root的引用链去找，扫描整个堆对象的过程中，如果发现这个对象确实被引用了，那么他需要保留，但比如有个对象没有被GC Root直接或间接的引用，那么他就可以当成是垃圾进行回收。

## 2.1 标记-清除

<!--标记-清除 Mark Sweep-->

标记清除算法顾名思义，是指在虚拟机执行垃圾回收的过程中，先采用标记算法确定可回收对象，然后垃圾收集器根据标识清除相应的内容，给堆内存腾出相应的空间

这里的腾出内存空间并不是将内存空间的字节清0，而是记录下这段内存的起始结束地址。下次分配内存的时候，会到这个空闲地址列表中去找看有没有一块儿足够的空间容纳新新的对象，如果有就会进行内存分配，并不会把这个他所占用的内存做一个清零的处理。

- 优点：垃圾清理的速度快。
- 缺点**：**容易产生大量的内存碎片，可能无法满足大对象的内存分配，一旦导致无法分配对象，那就会导致jvm启动gc，一旦启动gc，应用程序就会暂停，这就导致应用的响应速度变慢。

![](D:\Java\笔记\图片\5-1【jvm】\10-1.png)

上图中灰色部分是做了标记，然后清除。

## 2.2 标记-整理

<!--标记-整理 Mark Compact-->

标记-整理会将不被GC Root引用的对象回收，清楚其占用的内存空间。然后整理剩余的对象，可以有效避免因内存碎片而导致的问题，但是因为整体需要消耗一定的时间，所以效率较低

- 优点：没有内存碎片。
- 缺点：速度慢。

![](D:\Java\笔记\图片\5-1【jvm】\10-2.png)

## 2.2 复制

<!--复制 Copy-->

复制算法：首先会将内存分为等大小的两个区域，FROM和TO（TO中为空）。先将被GC Root引用的对象从FROM放入TO中，再回收不被GC Root引用的对象。然后交换FROM和TO。这样也可以避免内存碎片的问题，但是会占用双倍的内存空间。

* 优点：不会有内存碎片。
* 缺点：需要占用双倍内存空间。

划分两个区域，FROM区域和TO区域。

![](D:\Java\笔记\图片\5-1【jvm】\10-3.png)

FROM区域标记需要被回收的对象

![](D:\Java\笔记\图片\5-1【jvm】\10-4.png)

从FROM区域复制没有被标记的对象到TO区域，同时完成碎片的整理。

![](D:\Java\笔记\图片\5-1【jvm】\10-5.png)

FROM区域需要回收的对象回收

![](D:\Java\笔记\图片\5-1【jvm】\10-6.png)

将FROM区域与TO区域交换，以后创建的对象添加至FROM对象。然后循环。

![](D:\Java\笔记\图片\5-1【jvm】\10-7.png)

实际的JVM不会单独采用其中的一种算法，他都是结合这前面的三种算法，让他们协同工作。具体的一个实现就是虚拟机的叫分代的垃圾回收机制。

# 第三章 分代回收

他把我们整个堆内存划分成两块儿，一个是**新生代**，一个叫**老年代**。新生代又进一步划分了三个小的区域，分别叫`伊甸园`、`幸存区From`、`幸存区To`。

回收步骤如下：

- 对象首先分配在伊甸园区域。
- 新生代空间不足时，触发 `Minor GC`，伊甸园和 `幸存区From` 存活的对象使用 `复制算法Copy` 复制到`幸存区To` 中，存活的对象年龄加`1`并且交换 `幸存区From` `幸存区To`
- `Minor GC` 会引发 `stop the world`，暂停其它用户的线程，等垃圾回收结束，用户线程才恢复运行。
- 当对象寿命超过阈值时，会晋升至老年代，最大寿命是15（4bit）
- 当老年代空间不足，会先尝试触发 `Minor GC`，如果之后空间仍不足，那么触发 `Full GC`，`STW(Stop The World`)的时间更长

![](D:\Java\笔记\图片\5-1【jvm】\11-1.png)

## 3.1 回收流程

新创建的对象都被放在了**新生代的伊甸园**中

![](D:\Java\笔记\图片\5-1【jvm】\11-2.png)

当伊甸园中的内存不足时，就会进行一次垃圾回收，这时的回收叫做 **Minor GC**。

Minor GC 会将**伊甸园和幸存区FROM**存活的对象**先**复制到 **幸存区 TO**中， 并让其**寿命加1**，再**交换两个幸存区**。（会触发 **stop the world**， 暂停其他用户线程，只让垃圾回收线程工作）

![](D:\Java\笔记\图片\5-1【jvm】\11-3.png)

![](D:\Java\笔记\图片\5-1【jvm】\11-4.png)

再次创建对象，若新生代的伊甸园又满了，则会**再次触发 Minor GC**（会触发 **stop the world**， 暂停其他用户线程，只让垃圾回收线程工作），这时不仅会回收伊甸园中的垃圾，**还会回收幸存区中的垃圾**，再将活跃对象复制到幸存区TO中。回收以后会交换两个幸存区，并让幸存区中的对象**寿命加1**。

![](D:\Java\笔记\图片\5-1【jvm】\11-5.png)

如果幸存区中的对象的**寿命超过某个阈值**（最大为15，4bit），就会被**放入老年代**中

![](D:\Java\笔记\图片\5-1【jvm】\11-6.png)

如果新生代老年代中的内存都满了，就会先触发Minor GC，再触发**Full GC**，扫描**新生代和老年代中**所有不再使用的对象并回收。

## 3.2 GC 参数

GC相关JVM参数：

| 含义               | 参数                                                         |
| ------------------ | ------------------------------------------------------------ |
| 堆初始大小         | -Xms                                                         |
| 堆最大大小         | -Xmx 或 -XX:MaxHeapSize=size                                 |
| 新生代大小         | -Xmn 或 (-XX:NewSize=size + -XX:MaxNewSize=size)             |
| 幸存区比例（动态） | -XX:InitialSurvivorRatio=ratio 和 -XX:+UseAdaptiveSizePolicy |
| 幸存区比例         | -XX:SurvivorRatio=ratio                                      |
| 晋升阈值           | -XX:MaxTenuringThreshold=threshold                           |
| 晋升详情           | -XX:+PrintTenuringDistribution                               |
| GC详情             | -XX:+PrintGCDeatils -verbose:gc                              |
| FullGC前MinorGC    | -XX:+ScavengeBeforeFullGC                                    |

`幸存区比例 -XX:SurvivorRatio=ratio` ，radio是伊甸园所占内存的比例。例如radaio为8，有10M新生代内存，那么有8M是伊甸园的，2M平分给幸存区From和幸存区To。

## 3.3 GC分析

先来看一个空的方法运行后的打印JVM堆内存信息：

```apl
# 虚拟参数设置为：-Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDeatils -verbose:gc
    -Xms20M(初始堆空间20兆) 
    -Xmx20M（最大堆空间20兆）
    -Xmn10M（新生代划10兆）
    -XX:+UseSerialGC（设置垃圾回收器，因为jdk8下的默认垃圾回收期不是他，所以这里改成这个），
    -XX:+PrintGCDeatils -verbose:gc（这两个参数是打印GC详情）
```

直接运行之后，由于没有什么对象，而且20M够用，所以不会打印出GC信息。它只打印了一些程序运行结束以后的堆的情况。

```apl
Heap # 堆
  def new generation	total 9216K, used 2309k... # 新生代
    eden space 8192k, 28% used... # 伊甸园
    from space 1024k, 0% used... # 幸存区From
    to space 1024k, 0% used... # 幸存区To
  tenured generation	total 10240k, used 0k... # 晋升区 老年代
    the space 10240k, 0% used... 
  Metaspace used 3279k, capacity 4496k, committed 4864k, reserved 1056768k # 元空间（并不属于堆）
    class space used 359k, capacity 388k, committed 512k, reserved 1048576k
```

而我们创建一个7M的对象就会触发GC了：

```java
public class Demo {
	private static final int _7MB = 7 * 1024 * 1024;

	public static void main(String[] args) {
        ArrayList<byte[]> list = new ArrayList<>();
        list.add(new byte[_7MB]);	// 加7M的对象	
	}
}
```

由于创建了7兆的对象，在上面的eden space 8192k, 28% used中可以看到啥都没有时8M的伊甸园用率为28%，这里又创建了7MB的对象，所以必然会触发垃圾回收，因为已经用了28%的8兆伊甸园里放不下7M的对象了。再运行时输出如下：

```apl
[GC (Allocation Failure) [DefNew: 1984k->667k(9216k), 0.0028851 secs] 1984k->667k(19456k), 0.0029666 secs]...
Heap
  def new generation	total 9216K, used 8082k...
    eden space 8192k, 90% used...
    from space 1024k, 65% used...
    to space 1024k, 0% used...
  tenured generation	total 10240k, used 0k...
    the space 10240k, 0% used...
  Metaspace used 3283k, capacity 4496k, committed 4864k, reserved 1056768k
    class space used 359k, capacity 388k, committed 512k, reserved 1048576k

```

### 3.3.1 大对象处理策略

当遇到一个较大的对象时，就算新生代的伊甸园为空，也无法容纳该对象时，会将该对象直接晋升为老年代

```java
public class Demo {
	private static final int _8MB = 8 * 1024 * 1024;

	public static void main(String[] args) {
        ArrayList<byte[]> list = new ArrayList<>();
        list.add(new byte[_8MB]);	// 加8M的对象	
	}
}
```

```apl
Heap
  def new generation	total 9216K, used 8082k...
    eden space 8192k, 28% used...
    from space 1024k, 0% used...
    to space 1024k, 0% used...
  tenured generation	total 10240k, used 0k...
    the space 10240k, 80% used...  # 放到老年代了
  Metaspace used 3283k, capacity 4496k, committed 4864k, reserved 1056768k
    class space used 359k, capacity 388k, committed 512k, reserved 1048576k

```

### 3.2.2 线程内存溢出

某个线程的内存溢出了而抛异常（out of memory），不会让其他的线程结束运行

这是因为当一个线程抛出OOM异常后，它所占据的内存资源会全部被释放掉，从而不会影响其他线程的运行，进程依然正常

# 第四章 垃圾回收器

1. 串行
   单线程
   堆内存较小，适合个人电脑
2. 吞吐量优先
    多线程
    堆内存较大，多核 cpu
    让单位时间内，STW 的时间最短 0.2 0.2 = 0.4，垃圾回收时间占比最低，这样就称吞吐量高
3. 响应时间优先
    多线程
    堆内存较大，多核 cpu
    尽可能让单次 STW 的时间最短 0.1 0.1 0.1 0.1 0.1 = 0.5

**并行收集**：指多条垃圾收集线程并行工作，但此时用户线程仍处于等待状态。

**并发收集**：指用户线程与垃圾收集线程同时工作（不一定是并行的可能会交替执行）。用户程序在继续运行，而垃圾收集程序运行在另一个CPU上

**吞吐量**：即CPU用于运行用户代码的时间与CPU总消耗时间的比值（吞吐量 = 运行用户代码时间 / ( 运行用户代码时间 + 垃圾收集时间 )），也就是。例如：虚拟机共运行100分钟，垃圾收集器花掉1分钟，那么吞吐量就是99%

## 4.1 串行

- 单线程
- 内存较小，个人电脑（CPU核数较少）

```apl
-XX:+UseSerialGC = Serial + SerialOld		# 开启串行的垃圾回收器的JVM参数
```

![](D:\Java\笔记\图片\5-1【jvm】\12-1.png)

**安全点**：让其他线程都在这个点停下来，以免垃圾回收时移动对象地址，使得其他线程找不到被移动的对象。

因为是串行的，所以只有一个垃圾回收线程。且在该线程执行回收工作时，其他线程进入阻塞状态

* **Serial 收集器**

  Serial收集器是最基本的、发展历史最悠久的收集器。

  特点：单线程、简单高效（与其他收集器的单线程相比），采用复制算法。对于限定单个CPU的环境来说，Serial收集器由于没有线程交互的开销，专心做垃圾收集自然可以获得最高的单线程手机效率。收集器进行垃圾回收时，必须暂停其他所有的工作线程，直到它结束（Stop The World）

* **ParNew 收集器**

  ParNew收集器其实就是Serial收集器的多线程版本

  特点：多线程、ParNew收集器默认开启的收集线程数与CPU的数量相同，在CPU非常多的环境中，可以使用`-XX:ParallelGCThreads`参数来限制垃圾收集的线程数。和Serial收集器一样存在Stop The World问题。

*  Serial Old 收集器

  Serial Old是Serial收集器的老年代版本

  特点：同样是单线程收集器，采用标记-整理算法

## 4.2 吞吐量优先

| 含义                                      | 参数                                       |
| ----------------------------------------- | ------------------------------------------ |
| 开启吞吐量优先的垃圾回收器                | -XX:+UseParallelGC ~ -XX:+UseParallelOldGC |
| 设置线程数量                              | -XX:ParallelGCThreads=n                    |
| 动态调整新生代大小                        | -XX:+UseAdaptiveSizePolicy                 |
| 动态调正堆大小达到1/(1+ratio)垃圾回收占比 | -XX:GCTimeRatio=ratio                      |
| 最大暂停时间                              | -XX:MaxGCPauseMillis=ms                    |

特点如下：

- 多线程
- 堆内存较大，多核CPU
- 单位时间内，STW（stop the world，停掉其他所有工作线程）时间最短
- **JDK1.8默认使用**的垃圾回收器，可以使用`-XX:+UseParallelGC ~ -XX:+UseParallelOldGC`参数 开启。

吞吐量就是 CPU用于运行用户代码的时间与 CPU总消耗时间的比值，即吞吐量 = 运行用户代码时间 / （运行用户代码时间 + 垃圾收集时间），虚拟机总共运行了100分钟，其中垃圾收集花掉了1分钟，那吞吐量就是99%。

那么如果我们想要提高吞吐量，就应该尽量减少垃圾回收时间，那就尽可能少的运行 GC的次数，但是这样导致垃圾会很多，导致最大垃圾回收停顿时间变大。最大垃圾回收停顿时间是当我们去进行 GC的时候，需要把应用暂停下来，那么这段时间就是最大垃圾回收停顿时间。把停顿时间缩短就会导致垃圾回收次数增大。

如果要高吞吐量，那势必会导致某次的最大垃圾回收停顿时间很长，用户需要等很久！如果要用户体验，那就得缩短最大垃圾回收停顿时间，那势必就得频繁运行 GC，这样吞吐量就会下降了！

![](D:\Java\笔记\图片\5-1【jvm】\12-2.png)

* **Parallel Scavenge 收集器**

  与吞吐量关系密切，故也称为吞吐量优先收集器

  特点：属于新生代收集器也是采用复制算法的收集器（用到了新生代的幸存区），又是并行的多线程收集器（与ParNew收集器类似）

  该收集器的目标是达到一个可控制的吞吐量。还有一个值得关注的点是：**GC自适应调节策略**（与ParNew收集器最重要的一个区别）

- **Parallel Old 收集器**

  是Parallel Scavenge收集器的老年代版本

  特点：多线程，采用标记-整理算法（老年代没有幸存区）

GC自适应调节策略：`Parallel Scavenge`收集器可设置-XX:+UseAdptiveSizePolicy参数。当开关打开时**不需要**手动指定新生代的大小（-Xmn）、Eden与Survivor区的比例（-XX:SurvivorRation）、晋升老年代的对象年龄（-XX:PretenureSizeThreshold）等，虚拟机会根据系统的运行状况收集性能监控信息，动态设置这些参数以提供最优的停顿时间和最高的吞吐量，这种调节方式称为GC的自适应调节策略。

Parallel Scavenge收集器使用两个参数控制吞吐量：

- `XX:MaxGCPauseMillis` 控制最大的垃圾收集停顿时间
- `XX:GCRatio` 直接设置吞吐量的大小

## 4.3 响应时间优先

特点如下：

- 多线程
- 堆内存较大，多核CPU
- 尽可能让单次STW时间变短（尽量不影响其他线程运行）

![](D:\Java\笔记\图片\5-1【jvm】\12-3.png)

CMS 收集器：Concurrent Mark Sweep，一种以获取最短回收停顿时间为目标的老年代收集器。基于标记-清除算法实现。并发收集、低停顿，但是会产生内存碎片。适用于注重服务的响应速度，希望系统停顿时间最短，给用户带来更好的体验等场景下。如web程序、b/s服务。

CMS收集器的运行过程分为下列4步：

- 初始标记：标记GC Roots能直接到的对象。速度很快但是仍存在Stop The World问题
- 并发标记：进行GC Roots Tracing 的过程，找出存活对象且用户线程可并发执行
- 重新标记：为了修正并发标记期间因用户程序继续运行而导致标记产生变动的那一部分对象的标记记录。仍然存在Stop The World问题
- 并发清除：对标记的对象进行清除回收

CMS收集器的内存回收过程是与用户线程一起**并发执行**的。

`-XX:+UseConcMarkSweepGC`：开启CMS收集器，Conc是concurrent，是并发的意思，Mark是标记，最后的Sweep是清除，即他是一款基于`标记+清除`算法的垃圾回收器，并且他是并发的。

`-XX:+UseParNewGC`：工作在新生代的垃圾收集器，采用复制算法。

## 4.4 G1

G1，Garbage First。我们要读`Garbage One`。

- 2004 论文发布
- 2009 JDK 6u14 体验
- 2012 JDK 7u4 官方支持
- 2017 JDK 9 默认，替代了CMS垃圾收集器。

适用场景如下：

- 同时注重吞吐量（Throughput）和低延迟（Low latency），默认的暂停目标是 200 ms
- 超大堆内存，会将堆划分为多个大小相等的 Region
- 整体上是 标记+整理 算法，两个区域之间是 复制 算法

相关 JVM 参数：

`-XX:+UseG1GC`：开启Garbage One垃圾回收器。

`-XX:G1HeapRegionSize=size`：设置Region的大小

`-XX:MaxGCPauseMillis=time`：设置暂停目标

G1垃圾回收阶段如下：

新生代伊甸园垃圾回收—–>内存不足，新生代回收+并发标记—–>回收新生代伊甸园、幸存区、老年代内存——>新生代伊甸园垃圾回收(重新开始)

![](D:\Java\笔记\图片\5-1【jvm】\13-1.png)



### 4.4.1 Young Collection

分区算法region：分代是按对象的生命周期划分，分区则是将堆空间划分连续几个不同小区间，每一个小区间独立回收，可以控制一次回收多少个小区间，方便控制 GC 产生的停顿时间。

![](D:\Java\笔记\图片\5-1【jvm】\13-2.png)

E：伊甸园 S：幸存区 O：老年代

会触发STW，Stop The World。



![](D:\Java\笔记\图片\5-1【jvm】\13-3.png)

![](D:\Java\笔记\图片\5-1【jvm】\13-4.png)

### 4.4.2 Young Collection + CM

CM：并发标记

- 在 Young GC 时会**对 GC Root 进行初始标记**
- 在老年代**占用堆内存的比例**达到阈值时，对进行并发标记（不会STW），阈值可以根据用户来进行设定

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151150.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151150.png)

### 4.4.3 Mixed Collection

会对E S O 进行**全面的回收**

- 最终标记
- **拷贝**存活

-XX:MaxGCPauseMills:xxx 用于指定最长的停顿时间

**问**：为什么有的老年代被拷贝了，有的没拷贝？

因为指定了最大停顿时间，如果对所有老年代都进行回收，耗时可能过高。为了保证时间不超过设定的停顿时间，会**回收最有价值的老年代**（回收后，能够得到更多内存）

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151201.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151201.png)

### 4.4.4 Full GC

G1在老年代内存不足时（老年代所占内存超过阈值）

- 如果垃圾产生速度慢于垃圾回收速度，不会触发Full GC，还是并发地进行清理
- 如果垃圾产生速度快于垃圾回收速度，便会触发Full GC

### 4.4.5 Young Collection 跨代引用

- 新生代回收的跨代引用（老年代引用新生代）问题

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151211.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151211.png)

- 卡表与Remembered Set
  - Remembered Set 存在于E中，用于保存新生代对象对应的脏卡
    - 脏卡：O被划分为多个区域（一个区域512K），如果该区域引用了新生代对象，则该区域被称为脏卡
- 在引用变更时通过post-write barried + dirty card queue
- concurrent refinement threads 更新 Remembered Set

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151222.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151222.png)

### 4.4.6 Remark

重新标记阶段

在垃圾回收时，收集器处理对象的过程中

黑色：已被处理，需要保留的 灰色：正在处理中的 白色：还未处理的

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151229.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151229.png)

但是在**并发标记过程中**，有可能A被处理了以后未引用C，但该处理过程还未结束，在处理过程结束之前A引用了C，这时就会用到remark

过程如下

- 之前C未被引用，这时A引用了C，就会给C加一个写屏障，写屏障的指令会被执行，将C放入一个队列当中，并将C变为 处理中 状态
- 在**并发标记**阶段结束以后，重新标记阶段会STW，然后将放在该队列中的对象重新处理，发现有强引用引用它，就会处理它

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151239.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151239.png)

### 4.4.7 JDK 8u20 字符串去重

过程

- 将所有新分配的字符串（底层是char[]）放入一个队列
- 当新生代回收时，G1并发检查是否有重复的字符串
- 如果字符串的值一样，就让他们**引用同一个字符串对象**
- 注意，其与String.intern的区别
  - intern关注的是字符串对象
  - 字符串去重关注的是char[]
  - 在JVM内部，使用了不同的字符串标

优点与缺点

- 节省了大量内存
- 新生代回收时间略微增加，导致略微多占用CPU

### 4.4.8 JDK 8u40 并发标记类卸载

在并发标记阶段结束以后，就能知道哪些类不再被使用。如果一个类加载器的所有类都不在使用，则卸载它所加载的所有类

### 4.4.9 JDK 8u60 回收巨型对象

- 一个对象大于region的一半时，就称为巨型对象
- G1不会对巨型对象进行拷贝
- 回收时被优先考虑
- G1会跟踪老年代所有incoming引用，如果老年代incoming引用为0的巨型对象就可以在新生代垃圾回收时处理掉

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151249.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151249.png)

# 第五章 GC 调优

查看虚拟机参数命令

```
"F:\JAVA\JDK8.0\bin\java" -XX:+PrintFlagsFinal -version | findstr "GC"Copy
```

可以根据参数去查询具体的信息

#### 调优领域

- 内存
- 锁竞争
- CPU占用
- IO
- GC

#### 确定目标

低延迟/高吞吐量？ 选择合适的GC

- CMS G1 ZGC
- ParallelGC
- Zing

#### 最快的GC是不发生GC

首先排除减少因为自身编写的代码而引发的内存问题

- 查看Full GC前后的内存占用，考虑以下几个问题
  - 数据是不是太多？
  - 数据表示是否太臃肿
    - 对象图
    - 对象大小
  - 是否存在内存泄漏

#### 新生代调优

- 新生代的特点
  - 所有的new操作分配内存都是非常廉价的
    - TLAB
  - 死亡对象回收零代价
  - 大部分对象用过即死（朝生夕死）
  - MInor GC 所用时间远小于Full GC
- 新生代内存越大越好么？
  - 不是
    - 新生代内存太小：频繁触发Minor GC，会STW，会使得吞吐量下降
    - 新生代内存太大：老年代内存占比有所降低，会更频繁地触发Full GC。而且触发Minor GC时，清理新生代所花费的时间会更长
  - 新生代内存设置为内容纳[并发量*(请求-响应)]的数据为宜

#### 幸存区调优

- 幸存区需要能够保存 **当前活跃对象**+**需要晋升的对象**
- 晋升阈值配置得当，让长时间存活的对象尽快晋升

#### 老年代调优

https://nyimac.gitee.io/2020/06/08/并发编程/#四、共享模型之内存)