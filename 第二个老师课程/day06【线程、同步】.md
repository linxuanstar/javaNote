# 第一章 线程

## 1.1 多线程原理

<!--P308-->

<!--P309-->

体现一下多线程程序的执行流程。

翻阅API后得知创建线程的方式总共有两种，一种是继承Thread类方式，一种是实现Runnable接口方式。

## 1.2 第一种（继承Thread类）

<!--P310-->

API中定义了有关线程的一些方法，具体如下：

**构造方法：**

* `public Thread()`:分配一个新的线程对象。
* `public Thread(String name)`:分配一个指定名称的新的线程对象。
* `public Thread(Runnable target)`:分配一个带有指定目标新的线程对象。
* `public Thread(Runnable target, String name)`:分配一个带有指定目标新的线程对象并指定名字。

**常用方法：**

* `public String getName()`:获取当前线程名称。

```java
// 定义一个Thread类的子类
public class MyThread extends Thread{
    // 重写Thread类中的run方法，设置线程任务
    @Override
    public void run() {
        // 获取线程名称
        // String name = getName();
        // System.out.println(name);

        // Thread t = Thread.currentThread();
        // System.out.println(t);
        // String name = t.getName();
        // System.out.println(name);
        
        // 链式编程
        System.out.println(Thread.currentThread().getName());
    }
}

public class Demo01GetThreadName {
    public static void main(String[] args) {
        // 创建Thread类的子类
        MyThread mt = new MyThread();
        // 调用start方法，开启新线程，执行run方法
        mt.start();
    }
}
```

<!--P311-->

* `public String setName()`:设置当前线程名称。

```java
public class Demo01SetThreadName {
    public static void main(String[] args) {
        // 启动多线程
        MyThread mt = new MyThread();
        // 设置线程名称
        mt.setName("林炫");
        mt.start();

        // 第二种方法
        new MyThread("你好").start();
    }
}

public class MyThread extends Thread{
    public MyThread() {}

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        // 获取线程的名称
        System.out.println(Thread.currentThread().getName());
    }
}
```

* `public void start()`:导致此线程开始执行，java虚拟机调用此线程的run方法。
* `public void run()`:此线程要执行的任务在此处定义代码。

<!--P312-->

* `public static void sleep(long millis)`:是当前正在执行的线程以指定的毫秒数暂停（暂时停止执行）。

```java
/*
    public static void sleep(long millis):
    是当前正在执行的线程以指定的毫秒数暂停（暂时停止执行）。
 */
public class Demo01Sleep {
    public static void main(String[] args) {
        // 模拟秒表
        for (int i = 1; i <= 60; i++) {
            System.out.println(i);

            // 使用Thread类中的sleep方法让程序睡眠1秒钟
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

* `public static Thread currentThread()`:返回对当前正在执行的线程对象的引用。

## 1.3 第二种 （实现Runnable接口）

<!--P313-->

<!--runnable 就绪; 就绪状态; 可运行; 可运行状态; 可运行态 -->

采用`java.lang.Runnable`也是非常常见的一种，我们只需要重写run方法即可。

步骤如下：

1. 定义Runnable接口的实现类，并重写该接口的run()方法，该run()方法的方法体同样是该线程的线程执行体。
2. 创建Runnable实现类的实例，并以此实例作为Thread的target来创建Thread对象，该对象才是真正的线程对象。
3. 调用线程对象的start()方法来启动线程。

```java
/*
    创建多线程程序的第二种方式：实现Runnable接口
    实现步骤：
        1.创建一个Runnable接口的实现类
        2.在实现类中重写Runnable接口的run方法，设置线程任务。
        3.创建一个Runnable接口的实现类对象
        4.创建Thread类对象，构造方法中传递Runnable接口的实现类对象
        5.调用Thread类中的start方法，开启新的线程执行run方法
 */
public class Demo01Runnable {

    public static void main(String[] args) {
        // 3.创建一个RUnnable接口的实现类对象
        RunnableImpl run = new RunnableImpl();
        // 4.创建Thread类对象，构造方法中传递Runnable接口的实现类
        Thread t = new Thread(run);
        // 5.调用Thread类中的start方法，开启新的线程执行run方法
        t.start();

        for (int i = 0; i < 20; i++) {
            System.out.println(Thread.currentThread().getName() + "-->" + i);
        }
    }
}

// 1.创建一个Runnable接口的实现类
public class RunnableImpl implements Runnable{
    // 2.在实现类中重写Runnable接口的run方法，设置线程任务。
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println(Thread.currentThread().getName() + "-->" + i);
        }
    }
}

```

## 1.4 Thread和Runnable的区别

<!--P314 12.24-->

如果一个类继承Thread，则不适合资源共享，但是如果实现了Runnable接口的话，则很容易的实现资源共享。

**总结：**

**实现Runnable接口比继承Thread类所具有的优势：**

1. 适合多个相同的程序代码的线程去共享同一个资源。
2. 可以避免java中的单继承的局限性。
3. 增加程序的健壮性，实现解偶操作，代码可以被多个线程共享，代码和线程独立。
4. 线程池只能放入实现Runnable或者Callable类线程，不能直接放入继承Thread的类。

> 扩充：在java中，每次程序运行至少启动2个线程，一个是main线程，一个是垃圾收集线程，因为每当使用java命令执行一个类的时候，实际上都会启动一个JVM，每一个JVM其实就是在操作系统中启动了一个进程。

## 1.5 匿名内部类方式实现线程的创建

使用线程的匿名内部类方式，可以方便的实现每个线程执行不同的线程任务操作。

**匿名内部类方式实现线程的创建**

* 匿名：没有名字
* 内部类：写在其他类内部的类

* 匿名内部类作用：简化代码
  * 把子类继承父类，重写父类方法，创建子类对象 合成 一步完成
  * 把实现类实现接口，重写接口中的方法，创建实现类对象 合成 一步完成
* 匿名内部类的最终产物：子类/实现对象，而这个类没有名字

* 格式：

  ```java
  new 父类/接口() {
  	重写父类/接口中的方法
  };
  ```

使用匿名内部类的方式实现Runnable接口，重新Runnable接口中的Runnable方法：

<!--P315-->

```java
public class Demo01InnerClassThread {
    public static void main(String[] args) {
        // 线程的父类是Thread
        // new MyThread().start();
        new Thread() {
            // 重写线程方法，设置线程任务
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "-->" + "林炫");
                }
            }
        }.start();

        // 线程的接口Runnable
        // Runnable r = new Runnabel(); 这是个多态
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "-->" + "你好");
                }
            }
        };
        new Thread(r).start();

        // 简化接口的方式
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "-->" + "呀");
                }
            }
        }).start();
    }
}
```

# 第二章 线程安全

## 2.1 线程安全

<!--P316-->

如果有多个线程在同时运行，而这些线程可能会同时运行这段代码。程序每次运行结果和单线程运行的结果是一样的，而且其他的变量的值也和预期的是一样的，就是线程安全的。

我们通过一个案例，演示线程的安全问题：

电影院要卖票，我们模拟电影院的卖票过程。假设要播放的电影是“葫芦娃大战奥特曼”。本次电影的座位共100个（本场电影只能卖100张票)。

我们来模拟电影院的售票窗口，实现多个窗口同时卖“葫芦娃大战奥特曼"这场电影（多个窗口一起卖这100张票）

需要窗口，采用线程对象来模拟；需要票，Runnable接口子类来模拟

<!--P317-->

<!--P318-->

```java
public class RunnableImpl implements Runnable{

    // 定义一个多个线程共享的票源
    private int ticket = 100;

    // 设置线程任务
    @Override
    public void run() {
        while(true) {
            if (ticket > 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "-->正在卖第" + ticket + "张票");
                ticket--;
            }
        }
    }
}

public class Demo01Ticket {
    public static void main(String[] args) {
        // 创建Runnable接口的实现类对象
        RunnableImpl run = new RunnableImpl();
        // 创建Thread类对象，构造方法中传递Runnable接口的实现类对象
        Thread t0 = new Thread(run);
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        // 调用start方法开启多线程
        t0.start();
        t1.start();
        t2.start();
    }
}
```

## 2.2 线程同步

<!--P319-->

<!--P320-->

当我们使用多个线程访问同一资源的时候，且多个线程中对资源有写的操作，就容易出现线程安全问题。

要解决上述多线程并发访问一个资源的安全性问题：也就是解决重复票与不存在票的问题，java中提供了同步机制（synchronized）来解决。

根据案例简述：

```
窗口1线程进入操作的时候，窗口2和窗口3线程只能在外等着，窗口1操作结束，窗口1和窗口2和窗口3才有机会进入代码去执行。也就是说在摸个线程修改共享资源的时候，其他线程不能修改该资源，等待修改完毕同步之后，才能去抢夺CPU资源，完成对应的操作，保证了数据的同步性，解决了线程不安全的现象。
```

为了保证每个线程都能正常执行原子操作，java引入了线程同步机制。

有三种方法完成同步操作：

1. **同步代码块。**
2. **同步方法。**
3. **锁机制。**

## 2.3 同步代码块

* 同步代码块：`synchronized`关键字可以用于方法中的某个区块中，表示只对这个区块的资源实行互斥访问。

<!--synchronized [ˈsɪŋkrənaɪzd] (使)同步，在时间上一致，同速进行;-->

同步锁：

对象的同步锁只是一个概念，可以想象为在对象上标记了一个锁。

1. 锁对象 可以是任意类型
2. 多个线程对象 要使用同一把锁。

> 注意：在任何时候，最多允许一个线程拥有同步锁，谁拿到锁就进入代码块，其他的线程只能在外面等着（BLOCKED）。

使用同步代码块解决代码：

```java
public class Demo01Ticket {

    public static void main(String[] args) {
        // 创建Runnable接口的实现类对象
        RunnableImpl run = new RunnableImpl();
        // 创建Thread类对象，构造方法中传递Runnable接口的实现类对象
        Thread t0 = new Thread(run);
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        // 调用start方法开启多线程
        t0.start();
        t1.start();
        t2.start();
    }
}

public class RunnableImpl implements Runnable{

    // 定义一个多个线程共享的票源
    private int ticket = 100;

    // 创建一个锁对象
    Object obj = new Object();

    // 设置线程任务
    @Override
    public void run() {
        while(true) {
            // 同步代码块
            synchronized (obj) {
                // 判断票是否存在
                if (ticket > 0) {
                    // 提高安全问题出现的概率，让程序睡眠
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + "-->正在卖第" + ticket + "张票");
                    ticket--;
                }
            }
        }
    }
}
```

## 2.4 同步方法

<!--P321-->

<!--P322-->

* 同步方法：使用`synchronized`修饰的方法，就叫做同步方法，保证A线程执行该方法的时候，其他线程只能在方法外面等着。

格式：

```java
public synchronized void method() {
    // 可能会产生线程安全问题的代码
}
```

> 同步锁是谁？
>
> ​	对于非static方法，同步锁就是this。
>
> ​	对于static 方法，我们使用当前方法所在类的字节码对象(类名.class)。

使用同步方法代码如下：

```java
public class Demo01Ticket {

    public static void main(String[] args) {
        // 创建Runnable接口的实现类对象
        RunnableImpl run = new RunnableImpl();

        // 创建Thread类对象，构造方法中传递Runnable接口的实现类对象
        Thread t0 = new Thread(run);
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        // 调用start方法开启多线程
        t0.start();
        t1.start();
        t2.start();
    }
}


public class RunnableImpl implements Runnable{

    // 定义一个多个线程共享的票源
    private int ticket = 100;

    // 设置线程任务 卖票
    @Override
    public void run() {
        while(true) {
            payTicket();
        }
    }

    // 定义一个同步方法
    public synchronized void payTicket() {
        // 判断票是否存在
        if (ticket > 0) {
            // 提高安全问题出现的概率，让程序睡眠
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "-->正在卖第" + ticket + "张票");
            ticket--;
        }
    }
}
```

## 2.5 Lock锁

<!--P323-->

`java.util.concurrent.locks.Lock`机制提供了比**synchronized**代码块和**synchronized**方法更广泛的锁定操作，同步代码块/同步方法具有的功能`Lock`都有，除此之外更加强大，更体现面向对象。

Lock锁也称同步锁，加锁与释放锁方法化了，如下：

* `public void lock()`:加同步锁。
* `public void unlock()`:释放同步锁。

使用如下：

```java
public class Demo01Ticket {

    public static void main(String[] args) {
        // 创建Runnable接口的实现类对象
        RunnableImpl run = new RunnableImpl();
        // 创建Thread类对象，构造方法中传递Runnable接口的实现类对象
        Thread t0 = new Thread(run);
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        // 调用start方法开启多线程
        t0.start();
        t1.start();
        t2.start();
    }
}

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    解决线程安全问题的第三种方法：使用Lock锁
 */
public class RunnableImpl implements Runnable{

    // 定义一个多个线程共享的票源
    private int ticket = 100;

    // 1,在成员位置创建一个Reentrantlock对象
    Lock l = new ReentrantLock();

    // 设置线程任务
    @Override
    public void run() {
        while(true) {

            // 2.在可能出现安全问题的代码前调用Lock接口中的方法Lock获取锁
            l.lock();

            // 判断票是否存在
            if (ticket > 0) {
                // 提高安全问题出现的概率，让程序睡眠
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "-->正在卖第" + ticket + "张票");
                ticket--;
            }

            // 3.在可能会出现安全问题的代码后调用Lock接口中的方法unlock释放锁
            l.unlock();
        }
    }
}
```

# 第三章 线程状态

## 3.1 线程状态描述

<!--P324 12.25-->

当线程被创建并且启动以后，他不是已启动就进入了执行状态，也不是一直处于执行状态。在API中`java.lang.Thread.State`这个枚举中给出了六种线程状态:

| 线程状态                | 导致线程状态发生的条件                                       |
| ----------------------- | ------------------------------------------------------------ |
| NEW(新建)               | 线程刚被创建，但是没有启动。没有调用start方法。              |
| Runnable(可运行)        | 线程可以在java虚拟机中运行的状态，可能正在运行自己的代码，也可能没有，这取决于操作系统处理器 |
| Blocked(锁阻塞)         | 当一个线程试图获取一个对象锁，而该对象锁被其他的线程持有，则该线程进入Blocked状态；当该线程持有锁时，该线程将变成Runnable状态。 |
| Waiting(无线等待)       | 一个线程在等待另一个线程执行一个(唤醒)动作时，该线程进入Waiting状态。进入这个状态后是不能自动唤醒的，必须等待另一个线程调用notify或者notifyAll方法才能够唤醒。 |
| Timed Waiting(计时等待) | 同Waiting状态，有几个方法有超时参数，调用他们将进入Timed Waiting状态。这一状态将一直保持到超时期满或者接受到唤醒通知。带有超时参数的常用方法有Thread.sleep、Object.wait。 |
| Terminated(死亡状态)    | 已退出的线程处于这种状态                                     |

## 3.2 Timed Waiting(计时等待)

<!--P325-->

在API中的描述：一个正在限时等待另一个线程执行一个(唤醒)动作的线程处于这一状态。

之前已经接触过，run方法的sleep语句

当我们调用sleep方法后，当前执行的线程会进入到“休眠状态”,其实就是所谓的Timed Waiting(计时等待)。

![计时等待](D:\Java\笔记\图片\day06【线程、同步】\1.png)



## 3.3 BLOCKED(锁阻塞)

![锁阻塞](D:\Java\笔记\图片\day06【线程、同步】\2.png)

## 3.4 Waiting(无线等待)

![无线等待](D:\Java\笔记\图片\day06【线程、同步】\3.png)

<!--P326-->

```java
public class Demo01WaitAndNotify {
    public static void main(String[] args) {
        // 创建锁对象，保证线程唯一
        Object obj = new Object();
        // 创建一个顾客线程(消费者)
        new Thread() {
            @Override
            public void run() {
                // 保证等待和唤醒的线程只能有一个执行，使用同步代码块
                synchronized (obj) {
                    System.out.println("告知老板吃的包子");
                    // 调用wait方法，放弃CPU的执行，进入到WAITING状态
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 唤醒后面的代码
                    System.out.println("开吃");
                }
            }
        }.start();

        // 创建一个老板线程（生产者）
        new Thread () {
            @Override
            public void run() {
                // 花费5秒做包子
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 保证等待和唤醒只能有一个执行，使用同步技术
                synchronized (obj) {
                    System.out.println("5秒后，做好了");
                    // 做好包子了，调用notify方法，唤醒顾客吃包子
                    obj.notify();
                }
            }
        }.start();

    }
}
```

## 3.5 补充知识点

<!--P327-->

wait(时间);

notifyAll();