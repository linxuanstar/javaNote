# 第一章 等待唤醒机制

## 1.1 线程间通信

<!--P328-->

**概念：**多个线程在处理同一个资源，但是处理的动作（线程的任务）不相同。

比如：线程A是生成包子的，线程B是来吃包子的，包子可以理解为同一资源，线程A与线程B处理的动作，一个是生产，一个是消费，那么线程A与线程B之间就存在线程通信问题。

**为什么要处理线程间通信：**

多个线程并发执行时，在默认情况下CPU是随机切换线程的，当我们需要多个线程来共同完成一件任务，并且我们希望他们有规律的执行，那么多个线程之间需要一些协调通信，以此来帮助我们达到多线程共同操作一份数据。

**如何保证线程间通信有效利用资源：**

 多个线程在处理同一个资源，并且任务不同时，需要线程通信来帮助解决线程之间对同一个变量的使用或操作。就是多个线程在操作同一份数据时，避免对同一共享变量的争夺。也就是我们需要通过一定的手段使各个线程能有效的利用资源。而这种手段即一一**等待唤酲机制。**

## 1.2 等待唤醒机制

<!--P329-->

**什么是等待唤酲机制**

这是多个线程间的一种**协作**机制。谈到线程我们经常想到的是线程间的**竞争（race）**，比如去争夺锁，但这并不是故事的全部，线程间也会有协作机制。就好比在公司里你和你的同事们，你们可能存在在晋升时的竞争，但更多时候你们更多是一起合作以完成某些任务。

就是在一个线程进行了规定操作后，就进入等待**状态（wait()）**，等待其他线程执行完他们的指定代码过后再将其唤醒（**notify()**）；在有多个线程进行等待时，如果需要，可以使用`notifyAll()`来唤醒所有的等待线程。

`wait/notify`就是线程间的一种协作机制。

**等待唤酲中的方法**

等待唤酲机制就是用于解决线程间通信的问题的，使用到的3个方法的含义如下

1. `wait`：线程不再活动，不再参与调度，进入`wait set`中，因此不会浪费CPU资源，也不会去竟争锁了，这时的线程状态即是`WAITING`.它还要等看别的线程执行一个**特别的动作**，也即是''**通知（notify）**"在这个对象上等待的线程从`wait set`中释放出来，重新进入到调度队列（ready queue）中
2. `notify`：则选取所通知对象的`wait set`中的一个线程释放；例如，餐馆有空位置后，等候就餐最久的顾客最先入座。
3. `notifyAll`：则释放所通知对象的`wait set`上的全部线程。

> 注意:哪怕只通知了一个等待的线程，被通知线程也不能立即恢复执行，因为它当初中断的地方是在同步块内，而此刻它已经不持有锁，所以她需要再次尝试去获取锁（很可能面临其它线程的竞争），成功后才能在当初调用wait方法之后的地方恢复执行。
>
> 总结如下：
>
> * 如果能获取锁，线程就从`WAITING`状态变成`RUNNABLE`状态，
> * 否则，从`wait set`出来，又进入`entry set`，线程就从`WAITING`状态又变成`BLOCKED`状态

**调用wait和notify方法需要注意的细节**

1. `wait`方法与`notify`方法必须要由同一个锁对象调用。因为：对应的锁对象可以通过`notify`唤酲使用同一个锁对象调用的`wait`方法后的线程。
2. `wait`方法与`notify`方法是属于`Object`类的方法的。因为：锁对象可以是任意对象，而任意对象的所属类都是继承了`Object`类的。
3. `wait`方法与`notify`方法必须要在同步代码块或者是同步函数中使用。因为：必须要通过锁对象调用这2个方法。

## 1.3 生产者与消费者关系

<!--P330 12.26-->

<!--P331-->

<!--p332-->

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\1.png)

代码：

```java
/*
    资源类：包子类
    设置包子的属性
        皮
        馅
        包子的状态:有TRUE，没有FALSE
 */
public class BaoZi {
    // 皮
    String pi;
    // 馅
    String xian;
    // 包子的状态:有TRUE，没有FALSE
    boolean flag = false;
}
```

分析：

```java
/*
    生产者类(包子铺)类：是一个线程类，可以继承Thread
    设置线程任务(run)：生产包子
    对包子的状态进行判断
    TRUE：有包子
        包子铺调用wait方法进入等待状态
    FALSE：没有包子
        包子铺生产包子
        增加一些趣味：交替生产两种包子
            有两种状态（i % 2 == 0）
        包子铺生产好了包子
        修改包子状态为TRUE有
        唤醒吃货线程，让吃货线程吃包子

    注意：
        包子线程和包子铺线程关系-->通信（互斥）
        必须同时同步技术保证两个线程只能有一个在执行
        锁对象必须保证唯一，可以使用包子对象作为锁对象
        包子铺类和吃货的类就需要把包子对象作为参数传递进来
            1.使用在成员的位置创建一个包子变量
            2.使用带参数构造方法，为这个包子变量赋值
 */
public class BaoZiPu extends Thread{

    // 1.需要在成员位置创建一个包子变量
    private BaoZi bz;

    // 2.使用带参数构造方法，为这个包子变量赋值
    public BaoZiPu(BaoZi bz) {
        this.bz = bz;
    }

    // 设置线程任务(run)：生产包子
    @Override
    public void run() {
        // 定义一个变量
        int count = 0;

        while (true) {
            // 必须同时同步技术保证两个线程只能有一个在执行
            synchronized (bz) {
                // 对包子的状态进行判断
                if (bz.flag == true) {
                    // 包子铺调用wait方法进入等待状态
                    try {
                        bz.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 被唤醒之后，包子铺生产包子
                // 增加一些趣味：交替生产两种包子
                if (count % 2 ==0) {
                    bz.pi = "薄皮";
                    bz.xian = "三鲜馅";
                } else {
                    bz.pi = "冰皮";
                    bz.xian = "牛肉馅";
                }
                count++;
                System.out.println("包子铺正在生产：" + bz.pi + bz.xian + "的包子");

                // 生产包子需要时间
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 包子铺生产好包子，修改包子状态为有
                bz.flag = true;

                // 唤醒吃货线程，让吃货线程吃包子
                bz.notify();
                System.out.println("包子铺已经生产好了：" + bz.pi + bz.xian + "包子，吃货已经开吃了");
            }
        }
    }
}
```

```java
/*
    消费者（吃货）：是一个线程，可以继承Thread
    设置线程任务（run）:吃包子
    对包子的状态进行判断
    FALSE：没有包子
        吃货调用wait方法进入等待状态
    TRUE：有包子
        吃货吃包子
        吃完包子，修改包子状态为FALSE没有
        唤醒包子铺线程，生产包子
 */
public class ChiHuo extends Thread{
    // 1.需要在成员位置创建一个包子变量
    private BaoZi bz;

    // 2.使用带参数构造方法，为这个包子变量赋值
    public ChiHuo(BaoZi bz) {
        this.bz = bz;
    }

    // 设置线程任务（run）：吃包子
    @Override
    public void run() {
        // 使用死循环，让吃货一直吃包子
        while (true) {
            // 必须同时同步技术保证两个线程只能有一个在执行
            synchronized (bz) {
                // 对包子状态进行判断
                if (bz.flag == false) {
                    try {
                        bz.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 被唤醒之后执行的代码，吃包子
                System.out.println("吃货在吃：" + bz.pi + bz.xian + "的包子");

                // 吃货吃完包子，修改状态为FALSE没有
                bz.flag = false;
                // 吃货唤醒包子铺线程，生产包子
                bz.notify();
                System.out.println("吃货已经把：" + bz.pi + bz.xian + "的包子吃完了，包子铺开始生产包子");
                System.out.println("===================================");
            }
        }
    }
}
```

```java
/*
    测试类
    包含main方法，程序执行的入口，启动程序
    创建包子对象
    创建包子铺线程，开启，生产包子
    创建吃货线程，开启，吃包子
 */
public class Demo {
    public static void main(String[] args) {
        // 创建包子对象
        BaoZi bz = new BaoZi();
        // 创建包子铺线程，开启，生产包子
        new BaoZiPu(bz).start();
        // 创建吃货线程，开启，吃包子
        new ChiHuo(bz).start();
    }
}
```

# 第二章 线程池

## 2.1 线程池思想概述

使得线程可以复用，执行完一个任务，并不被销毁，可以继续执行其他的任务

## 2.2 线程池概念

<!--P333-->

* 线程池：其实就是一个容纳多个线程的容器，其中的线程可以反复使用，省去了频繁创建线程对象的操作，无需反复创建线程而消耗过多资源。

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\2.png)

线程池工作原理：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\3.png)

合理利用线程池有三个好处：

1. 降低资源消耗。
2. 提高响应速度。
3. 提高线程的可管理性。

## 2.3 线程池的作用

java里面线程池的顶级接口是`java.util.concurrent.Executor`，但是严格意义上讲`Executor`并不是一个线程池，而只是一个执行线程的工具。真正的线程池接口是`java.util.concurrent.ExecutorService`。

要配置一个线程池比较复杂，尤其是对于线程池的原理不是很清楚的情况下，很有可能配置的线程池不是较优的，因此在`java.util.concurrent.Executors`线程工厂类里面提供了一些静态工厂，生成一些常用的线程池。<font color = "red">**官方建议使用`Executors`工程类来创建线程池对象**</font>：

`Executors`类中有个创建线程池的方法如下：

* `public static ExecutorService newFixedThreadPool(int nThreads)`：返回线程池对象。（创建的是有界线程池，也就是池中的线程个数可以指定最大数量）

获取到了一个线程池`ExecutorService`对象，那么怎么使用呢，在这里定义了一个使用线程池对象的方法如下：

* `public Future<?> submit(Runnable task)`:获取线程池中的某一个线程对象，并执行

> Future接口：用来记录线程任务执行完毕后产生的结果。线程池创建与使用。

使用线程池中线程对象的步骤：

1. 创建线程池对象。
2. 创建`Runnable`接口子类对象。（task）
3. 提交`Runnable`接口子类对象。（take task）
4. 关闭线程池（一般不做）。

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
    线程池的使用步骤：
        1.使用线程池的工厂类Executors里边提供的静态方法newFixedThreadPool生产一个指定线程数量的线程池
        2.创建一个类，实现Runnable接口，重写run方法，设置线程任务
        3.调用ExecutorsService中的方法submit，传递线程任务（实现类），开启现场，执行run方法。
        4.调用ExecutorsService中的方法shutdown销毁线程池（不建议执行）
 */
public class Demo01ThreadPool {
    public static void main(String[] args) {
        // 1.使用线程池的工厂类Executors里边提供的静态方法newFixedThreadPool生产一个指定线程数量的线程池
        ExecutorService es = Executors.newFixedThreadPool(2);
        // 3.调用ExecutorsService中的方法submit，传递线程任务（实现类），开启线程，执行run方法。
        // 线程池会一直开启，使用完了线程，会自动把线程归还给线程池，线程可以继续使用
        es.submit(new RunnableImpl());
        es.submit(new RunnableImpl());
        es.submit(new RunnableImpl());

        // 4.调用ExecutorsService中的方法shutdown销毁线程池（不建议执行）
        es.shutdown();

        es.submit(new RunnableImpl());// 抛出异常，线程池已经没有了，就不能获取线程了
    }
}



// 2.创建一个类，实现Runnable接口，重写run方法，设置线程任务
public class RunnableImpl implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "创建了一个新的线程执行");
    }
}
```

## 2.4 线程池的创建与使用

线程池的创建方法总共有 7 种，但总体来说可分为 2 类：

* 一类是通过 `ThreadPoolExecutor` 创建的线程池；
* 另一个类是通过 `Executors` 创建的线程池。

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\4线程池创建.png)


线程池的创建方式总共包含以下 7 种（其中 6 种是通过 `Executors` 创建的，1 种是通过`ThreadPoolExecutor` 创建的）：

1. `Executors.newFixedThreadPool`：创建一个固定大小的线程池，可控制并发的线程数，超出的线程会在队列中等待；
2. `Executors.newCachedThreadPool`：创建一个可缓存的线程池，若线程数超过处理所需，缓存一段时间后会回收，若线程数不够，则新建线程；
3. `Executors.newSingleThreadExecutor`：创建单个线程数的线程池，它可以保证先进先出的执行顺序；
4. `Executors.newScheduledThreadPool`：创建一个可以执行延迟任务的线程池；
5. `Executors.newSingleThreadScheduledExecutor`：创建一个单线程的可以执行延迟任务的线程池；
6. `Executors.newWorkStealingPool`：创建一个抢占式执行的线程池（任务执行顺序不确定）【JDK 1.8 添加】。
7. `ThreadPoolExecutor`：最原始的创建线程池的方式，它包含了 7 个参数可供设置，后面会详细讲。

**单线程池的意义**从以上代码可以看出 `newSingleThreadExecutor` 和`newSingleThreadScheduledExecutor` 创建的都是单线程池，那么单线程池的意义是什么呢？

答：<font color = "red">**虽然是单线程池，但提供了工作队列，生命周期管理，工作线程维护等功能。**</font>

那接下来我们来看每种线程池创建的具体使用。

### FixedThreadPool

创建一个固定大小的线程池，可控制并发的线程数，超出的线程会在队列中等待。

使用示例如下：

```java
public static void fixedThreadPool() {
    // 创建 2 个数据级的线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(2);

    // 创建任务
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("任务被执行,线程:" + Thread.currentThread().getName());
        }
    };

    // 线程池执行任务(一次添加 4 个任务)
    // 执行任务的方法有两种:submit 和 execute
    threadPool.submit(runnable);  // 执行方式 1:submit
    threadPool.execute(runnable); // 执行方式 2:execute
    threadPool.execute(runnable);
    threadPool.execute(runnable);
}
```
执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\5FixedThreadPool线程池.jpg)


如果觉得以上方法比较繁琐，还用更简单的使用方法，如下代码所示：

```java
public static void fixedThreadPool() {
    // 创建线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(2);
    // 执行任务
    threadPool.execute(() -> {
        System.out.println("任务被执行,线程:" + Thread.currentThread().getName());
    });
}
```

### CachedThreadPool

创建一个可缓存的线程池，若线程数超过处理所需，缓存一段时间后会回收，若线程数不够，则新建线程。

使用示例如下：

```java
public static void cachedThreadPool() {
    // 创建线程池
    ExecutorService threadPool = Executors.newCachedThreadPool();
    // 执行任务
    for (int i = 0; i < 10; i++) {
        threadPool.execute(() -> {
            System.out.println("任务被执行,线程:" + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
        });
    }
}
```


执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\6CachedThreadPool.jpg)


从上述结果可以看出，线程池创建了 10 个线程来执行相应的任务。

### SingleThreadExecutor

创建单个线程数的线程池，它可以保证先进先出的执行顺序。

使用示例如下：

```java
public static void singleThreadExecutor() {
    // 创建线程池
    ExecutorService threadPool = Executors.newSingleThreadExecutor();
    // 执行任务
    for (int i = 0; i < 10; i++) {
        final int index = i;
        threadPool.execute(() -> {
            System.out.println(index + ":任务被执行");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
        });
    }
}
```

执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\7SingleThreadExecutor.png)

### ScheduledThreadPool

创建一个可以执行延迟任务的线程池。

使用示例如下：

```java
public static void scheduledThreadPool() {
    // 创建线程池
    ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(5);
    // 添加定时执行任务(1s 后执行)
    System.out.println("添加任务,时间:" + new Date());
    threadPool.schedule(() -> {
        System.out.println("任务被执行,时间:" + new Date());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
    }, 1, TimeUnit.SECONDS);
}
```


执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\8ScheduledThreadPool.png)

从上述结果可以看出，任务在 1 秒之后被执行了，符合我们的预期。

### SingleThreadScheduledExecutor

创建一个单线程的可以执行延迟任务的线程池。

使用示例如下：

```java
public static void SingleThreadScheduledExecutor() {
    // 创建线程池
    ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
    // 添加定时执行任务(2s 后执行)
    System.out.println("添加任务,时间:" + new Date());
    threadPool.schedule(() -> {
        System.out.println("任务被执行,时间:" + new Date());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
    }, 2, TimeUnit.SECONDS);
}
```

执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\9SingleThreadScheduledExecutor.png)


从上述结果可以看出，任务在 2 秒之后被执行了，符合我们的预期。

### newWorkStealingPool

创建一个抢占式执行的线程池（任务执行顺序不确定），注意此方法只有在 JDK 1.8+ 版本中才能使用。

使用示例如下：

```java
public static void workStealingPool() {
    // 创建线程池
    ExecutorService threadPool = Executors.newWorkStealingPool();
    // 执行任务
    for (int i = 0; i < 10; i++) {
        final int index = i;
        threadPool.execute(() -> {
            System.out.println(index + " 被执行,线程名:" + Thread.currentThread().getName());
        });
    }
    // 确保任务执行完成
    while (!threadPool.isTerminated()) {
    }
}
```


执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\10newWorkStealingPool.jpg)


从上述结果可以看出，任务的执行顺序是不确定的，因为它是抢占式执行的。

### ThreadPoolExecutor

最原始的创建线程池的方式，它包含了 7 个参数可供设置。

使用示例如下：

```java
public static void myThreadPoolExecutor() {
    // 创建线程池
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    // 执行任务
    for (int i = 0; i < 10; i++) {
        final int index = i;
        threadPool.execute(() -> {
            System.out.println(index + " 被执行,线程名:" + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
```

执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\11ThreadPoolExecutor.jpg)

## 2.5 ThreadPoolExecutor 参数介绍

ThreadPoolExecutor 最多可以设置 7 个参数，如下代码所示：

```java
 public ThreadPoolExecutor(int corePoolSize,
                           int maximumPoolSize,
                           long keepAliveTime,
                           TimeUnit unit,
                           BlockingQueue<Runnable> workQueue,
                           ThreadFactory threadFactory,
                           RejectedExecutionHandler handler) {
     // 省略...
 }
```

7 个参数代表的含义如下：

### 参数 1：corePoolSize

核心线程数，线程池中始终存活的线程数。

### 参数 2：maximumPoolSize

最大线程数，线程池中允许的最大线程数，当线程池的任务队列满了之后可以创建的最大线程数。

### 参数 3：keepAliveTime

最大线程数可以存活的时间，当线程中没有任务执行时，最大线程就会销毁一部分，最终保持核心线程数量的线程。

### 参数 4：unit

单位是和参数 3 存活时间配合使用的，合在一起用于设定线程的存活时间 ，参数 `keepAliveTime` 的时间单位有以下 7 种可选：

* `TimeUnit.DAYS`：天
* `TimeUnit.HOURS`：小时
* `TimeUnit.MINUTES`：分
* `TimeUnit.SECONDS`：秒
* `TimeUnit.MILLISECONDS`：毫秒
* `TimeUnit.MICROSECONDS`：微妙
* `TimeUnit.NANOSECONDS`：纳秒

### 参数 5：workQueue

一个阻塞队列，用来存储线程池等待执行的任务，均为线程安全，它包含以下 7 种类型：

* `ArrayBlockingQueue`：一个由数组结构组成的有界阻塞队列。
* `LinkedBlockingQueue`：一个由链表结构组成的有界阻塞队列。
* `SynchronousQueue`：一个不存储元素的阻塞队列，即直接提交给线程不保持它们。
* `PriorityBlockingQueue`：一个支持优先级排序的无界阻塞队列。
* `DelayQueue`：一个使用优先级队列实现的无界阻塞队列，只有在延迟期满时才能从中提取元素。
* `LinkedTransferQueue`：一个由链表结构组成的无界阻塞队列。与`SynchronousQueue`类似，还含有非阻塞方法。
* `LinkedBlockingDeque`：一个由链表结构组成的双向阻塞队列。

较常用的是 `LinkedBlockingQueue` 和 `Synchronous`，线程池的排队策略与 `BlockingQueue` 有关。

### 参数 6：threadFactory

线程工厂，主要用来创建线程，默认为正常优先级、非守护线程。

### 参数 7：handler

拒绝策略，拒绝处理任务时的策略，系统提供了 4 种可选：

* `AbortPolicy`：拒绝并抛出异常。
* `CallerRunsPolicy`：使用当前调用的线程来执行此任务。
* `DiscardOldestPolicy`：抛弃队列头部（最旧）的一个任务，并执行当前任务。
* `DiscardPolicy`：忽略并抛弃当前任务。

默认策略为 `AbortPolicy`。

### 线程池的执行流程

`ThreadPoolExecutor` 关键节点的执行流程如下：

* 当线程数小于核心线程数时，创建线程。
* 当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。
* 当线程数大于等于核心线程数，且任务队列已满：若线程数小于最大线程数，创建线程；若线程数等于最大线程数，抛出异常，拒绝任务。

线程池的执行流程如下图所示：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\12ThreadPoolExecutor 执行流程.jpg)

### 线程拒绝策略

拒绝策略，拒绝处理任务时的策略，系统提供了 4 种可选：

* `AbortPolicy`：拒绝并抛出异常。
* `CallerRunsPolicy`：使用当前调用的线程来执行此任务。
* `DiscardOldestPolicy`：抛弃队列头部（最旧）的一个任务，并执行当前任务。
* `DiscardPolicy`：忽略并抛弃当前任务。

默认策略为 `AbortPolicy`。

我们来演示一下 `ThreadPoolExecutor` 的拒绝策略的触发，我们使用 `DiscardPolicy` 的拒绝策略，它会忽略并抛弃当前任务的策略，实现代码如下：

```java
public static void main(String[] args) {
    // 任务的具体方法
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("当前任务被执行,执行时间:" + new Date() +
                               " 执行线程:" + Thread.currentThread().getName());
            try {
                // 等待 1s
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    // 创建线程,线程的任务队列的长度为 1
    ThreadPoolExecutor threadPool = 
        							new ThreadPoolExecutor(1, 1, 100, TimeUnit.SECONDS, 
                                    	new LinkedBlockingQueue<>(1),
                                    	new ThreadPoolExecutor.DiscardPolicy());
    // 添加并执行 4 个任务
    threadPool.execute(runnable);
    threadPool.execute(runnable);
    threadPool.execute(runnable);
    threadPool.execute(runnable);
}
```

我们创建了一个核心线程数和最大线程数都为 1 的线程池，并且给线程池的任务队列设置为 1，这样当我们有 2 个以上的任务时就会触发拒绝策略，执行的结果如下图所示：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\13ThreadPoolExecutor 线程拒绝策略.jpg)


从上述结果可以看出只有两个任务被正确执行了，其他多余的任务就被舍弃并忽略了。其他拒绝策略的使用类似，这里就不一一赘述了。

### 自定义拒绝策略

除了 Java 自身提供的 4 种拒绝策略之外，我们也可以自定义拒绝策略，示例代码如下：

```java
public static void main(String[] args) {
    // 任务的具体方法
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("当前任务被执行,执行时间:" + new Date() +
                               " 执行线程:" + Thread.currentThread().getName());
            try {
                // 等待 1s
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    // 创建线程,线程的任务队列的长度为 1
    ThreadPoolExecutor threadPool = 
        							new ThreadPoolExecutor(1, 1,100, TimeUnit.SECONDS, 
                                    	new LinkedBlockingQueue<>(1),
                                   		new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 执行自定义拒绝策略的相关操作
            System.out.println("我是自定义拒绝策略~");
            }
        });
    
    // 添加并执行 4 个任务
    threadPool.execute(runnable);
    threadPool.execute(runnable);
    threadPool.execute(runnable);
    threadPool.execute(runnable);
}
```

程序的执行结果如下：

![](D:\Java\笔记\图片\day07【线程池、Lambda表达式】\14ThreadPoolExecutor 自定义拒绝.jpg)

## 2.6 究竟选用哪种线程池？

经过以上的学习我们对整个线程池也有了一定的认识了，那究竟该如何选择线程池呢？

我们来看下阿里巴巴《Java开发手册》给我们的答案：

> 【强制】线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
>
> 说明：Executors 返回的线程池对象的弊端如下：
>
> 1. FixedThreadPool 和 SingleThreadPool：允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
> 2. CachedThreadPool：允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。

所以综上情况所述，我们推荐使用 `ThreadPoolExecutor` 的方式进行线程池的创建，因为这种创建方式更可控，并且更加明确了线程池的运行规则，可以规避一些未知的风险。

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

