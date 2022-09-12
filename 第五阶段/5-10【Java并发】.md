学习路线如下：

![](D:\Java\笔记\图片\5-9【Java并发】\0-00000005.png)

本系列代码在JDK8下实现：

```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
<dependencies>
    <!--导入Lombok，简化JavaBean的编写-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.10</version>
    </dependency>
    <!--使用Logback日志来实现-->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.3</version>
    </dependency>
</dependencies>
```

```xml
<!--logback.xml配置文件-->
<?xml version="1.0" encoding="UTF-8"?>
<configuration
               xmlns="http://ch.qos.logback/xml/ns/logback"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://ch.qos.logback/xml/ns/logback logback.xsd">
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%date{HH:mm:ss} [%t] %logger - %m%n</pattern>
        </encoder>
    </appender>
    <logger name="c" level="debug" additivity="false">
        <appender-ref ref="STDOUT"/>
    </logger>
    <root level="ERROR">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

# 第一章 基础概念

## 1、进程与线程

### 进程

- 程序由指令和数据组成，但这些指令要运行，数据要读写，就必须将指令加载至 CPU，数据加载至内存。在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令、管理内存、管理 IO 的。
- 当一个程序被运行，从磁盘加载这个程序的代码至内存，这时就开启了一个进程。
- 进程就可以视为程序的一个实例。大部分程序可以同时运行多个实例进程（例如记事本、画图、浏览器 等），也有的程序只能启动一个实例进程（例如网易云音乐、360 安全卫士等）

### 线程

- 一个进程之内可以分为一到多个线程。
- 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给 CPU 执行 。
- Java 中，线程作为小调度单位，进程作为资源分配的小单位。 在 windows 中进程是不活动的，只是作 为线程的容器

### 二者对比

- 进程基本上相互独立的，而线程存在于进程内，是进程的一个子集 进程拥有共享的资源，如内存空间等，供其内部的线程共享
  - 进程间通信较为复杂 同一台计算机的进程通信称为 IPC（Inter-process communication）
  - 不同计算机之间的进程通信，需要通过网络，并遵守共同的协议，例如 HTTP
- 线程通信相对简单，因为它们共享进程内的内存，一个例子是多个线程可以访问同一个共享变量 线程更轻量，线程上下文切换成本一般上要比进程上下文切换低

#### 进程和线程的切换

**上下文切换**

内核为每一个进程维持一个上下文。**上下文就是内核重新启动一个被抢占的进程所需的状态。**包括以下内容：

- 通用目的寄存器
- 浮点寄存器
- 程序计数器
- 用户栈
- 状态寄存器
- 内核栈
- 各种内核数据结构：比如描绘地址空间的**页表**，包含有关当前进程信息的**进程表**，以及包含进程已打开文件的信息的**文件表**

**进程切换和线程切换的主要区别**

最主要的一个区别在于**进程切换涉及虚拟地址空间的切换而线程不会**。因为每个进程都有自己的虚拟地址空间，而**线程是共享所在进程的虚拟地址空间的**，因此同一个进程中的线程进行线程切换时不涉及虚拟地址空间的转换

页表查找是一个很慢的过程，因此通常使用cache来缓存常用的地址映射，这样可以加速页表查找，这个cache就是快表TLB（translation Lookaside Buffer，用来加速页表查找）。由于每个进程都有自己的虚拟地址空间，那么显然每个进程都有自己的页表，那么**当进程切换后页表也要进行切换，页表切换后TLB就失效了**，cache失效导致命中率降低，那么虚拟地址转换为物理地址就会变慢，表现出来的就是程序运行会变慢，而线程切换则不会导致TLB失效，因为线程线程无需切换地址空间，因此我们通常说线程切换要比较进程切换快

而且还可能出现**缺页中断**，这就需要操作系统将需要的内容调入内存中，若内存已满则还需要将不用的内容调出内存，这也需要花费时间

**为什么TLB能加快访问速度**

快表可以避免每次都对页号进行地址的有效性判断。快表中保存了对应的物理块号，可以直接计算出物理地址，无需再进行有效性检查

## 2、并发与并行

并发是一个CPU在不同的时间去不同线程中执行指令。

并行是多个CPU同时处理不同的线程。

引用 Rob Pike 的一段描述：

- 并发（concurrent）是同一时间**应对**（dealing with）多件事情的能力
- 并行（parallel）是同一时间**动手做**（doing）多件事情的能力

### 3、应用

#### 应用之异步调用（案例1）

以调用方角度来讲，如果

- 需要等待结果返回，才能继续运行就是同步
- 不需要等待结果返回，就能继续运行就是异步

\1) 设计
多线程可以让方法执行变为异步的（即不要巴巴干等着）比如说读取磁盘文件时，假设读取操作花费了 5 秒钟，如 果没有线程调度机制，这 5 秒 cpu 什么都做不了，其它代码都得暂停…
\2) 结论

- 比如在项目中，视频文件需要转换格式等操作比较费时，这时开一个新线程处理视频转换，避免阻塞主线程
- tomcat 的异步 servlet 也是类似的目的，让用户线程处理耗时较长的操作，避免阻塞
- tomcat 的工作线程 ui 程序中，开线程进行其他操作，避免阻塞 ui 线程

结论

1. 单核 cpu 下，多线程不能实际提高程序运行效率，只是为了能够在不同的任务之间切换，不同线程轮流使用 cpu ，不至于一个线程总占用 cpu，别的线程没法干活
2. 多核 cpu 可以并行跑多个线程，但能否提高程序运行效率还是要分情况的
   - 有些任务，经过精心设计，将任务拆分，并行执行，当然可以提高程序的运行效率。但不是所有计算任 务都能拆分（参考后文的【阿姆达尔定律】）
   - 也不是所有任务都需要拆分，任务的目的如果不同，谈拆分和效率没啥意义
3. IO 操作不占用 cpu，只是我们一般拷贝文件使用的是【阻塞 IO】，这时相当于线程虽然不用 cpu，但需要一 直等待 IO 结束，没能充分利用线程。所以才有后面的【非阻塞 IO】和【异步 IO】优化

# 二、线程的创建

## 1、创建一个线程（非主线程）

### 方法一：通过继承Thread创建线程

```
public class CreateThread {
	public static void main(String[] args) {
		Thread myThread = new MyThread();
        // 启动线程
		myThread.start();
	}
}

class MyThread extends Thread {
	@Override
	public void run() {
		System.out.println("my thread running...");
	}
}Copy
```

使用继承方式的好处是，在run（）方法内获取当前线程直接使用this就可以了，无须使用Thread.currentThread（）方法；不好的地方是Java不支持多继承，如果继承了Thread类，那么就不能再继承其他类。另外任务与代码没有分离，当多个线程执行一样的任务时需要多份任务代码

### 方法二：使用Runnable配合Thread(推荐)

```
public class Test2 {
	public static void main(String[] args) {
		//创建线程任务
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("Runnable running");
			}
		};
		//将Runnable对象传给Thread
		Thread t = new Thread(r);
		//启动线程
		t.start();
	}
}Copy
```

或者

```
public class CreateThread2 {
   private static class MyRunnable implements Runnable {

      @Override
      public void run() {
         System.out.println("my runnable running...");
      }
   }

   public static void main(String[] args) {
      MyRunnable myRunnable = new MyRunnable();
      Thread thread = new Thread(myRunnable);
      thread.start();
   }
}Copy
```

通过实现Runnable接口，并且实现run()方法。在创建线程时作为参数传入该类的实例即可

#### 方法二的简化：使用lambda表达式简化操作

**当一个接口带有@FunctionalInterface注解时，是可以使用lambda来简化操作的**

所以方法二中的代码可以被简化为

```
public class Test2 {
	public static void main(String[] args) {
		//创建线程任务
		Runnable r = () -> {
            //直接写方法体即可
			System.out.println("Runnable running");
			System.out.println("Hello Thread");
		};
		//将Runnable对象传给Thread
		Thread t = new Thread(r);
		//启动线程
		t.start();
	}
}Copy
```

可以再Runnable上使用Alt+Enter

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144534.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144534.png)

#### 原理之 Thread 与 Runnable 的关系

分析 Thread 的源码，理清它与 Runnable 的关系
**小结**

- 方法1 是把线程和任务合并在了一起
- 方法2 是把线程和任务分开了
- 用 Runnable 更容易与线程池等高级 API 配合 用 Runnable 让任务类脱离了 Thread 继承体系，更灵活

### 方法三：使用FutureTask与Thread结合

**使用FutureTask可以用泛型指定线程的返回值类型（Runnable的run方法没有返回值）**

```
public class Test3 {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
        //需要传入一个Callable对象
		FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println("线程执行!");
				Thread.sleep(1000);
				return 100;
			}
		});

		Thread r1 = new Thread(task, "t2");
		r1.start();
		//获取线程中方法执行后的返回结果
		System.out.println(task.get());
	}
}Copy
```

或

```
public class UseFutureTask {
   public static void main(String[] args) throws ExecutionException, InterruptedException {
      FutureTask<String> futureTask = new FutureTask<>(new MyCall());
      Thread thread = new Thread(futureTask);
      thread.start();
      // 获得线程运行后的返回值
      System.out.println(futureTask.get());
   }
}

class MyCall implements Callable<String> {
   @Override
   public String call() throws Exception {
      return "hello world";
   }
}Copy
```

### 总结

使用**继承方式的好处是方便传参**，你可以在子类里面添加成员变量，通过set方法设置参数或者通过构造函数进行传递，而如果使用Runnable方式，则只能使用主线程里面被声明为final的变量。**不好的地方是Java不支持多继承**，如果继承了Thread类，那么子类不能再继承其他类，而Runable则没有这个限制。**前两种方式都没办法拿到任务的返回结果，但是Futuretask方式可以**

## 2、原理之线程运行

#### 栈与栈帧

Java Virtual Machine Stacks （Java 虚拟机栈） 我们都知道 JVM 中由堆、栈、方法区所组成，其中栈内存是给谁用的呢？

- 其实就是线程，每个线程启动后，虚拟机就会为其分配一块**栈内存**
- 每个栈由多个栈帧（Frame）组成，对应着每次**方法调用时所占用的内存**
- 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法

#### 线程上下文切换（Thread Context Switch）

因为以下一些原因导致 cpu 不再执行当前的线程，转而执行另一个线程的代码

- 线程的 cpu 时间片用完
- 垃圾回收 有更高优先级的线程需要运行
- 线程自己调用了 sleep、yield、wait、join、park、synchronized、lock 等方法

当 Context Switch 发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java 中对应的概念 就是程序计数器（Program Counter Register），它的作用是记住下一条 jvm 指令的执行地址，是线程私有的

- 状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等
- Context Switch 频繁发生会影响性能

## 3、常用方法

### (1)start() vs run()

被创建的Thread对象直接调用重写的run方法时， run方法是在**主线程**中被执行的，而不是在我们所创建的线程中执行。所以如果想要在所创建的线程中执行run方法，**需要使用Thread对象的start方法。**

### (2)sleep()与yield()

#### **sleep** (使线程阻塞)

1. 调用 sleep 会让当前线程从 **Running 进入 Timed Waiting 状态（阻塞）**，可通过state()方法查看

2. 其它线程可以使用 **interrupt** 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException

3. 睡眠结束后的线程未必会立刻得到执行

4. 建议用 **TimeUnit 的 sleep** 代替 Thread 的 sleep 来获得更好的可读性 。如：

   ```
   //休眠一秒
   TimeUnit.SECONDS.sleep(1);
   //休眠一分钟
   TimeUnit.MINUTES.sleep(1);Copy
   ```

#### yield （让出当前线程）

1. 调用 yield 会让当前线程从 **Running 进入 Runnable 就绪状态**（仍然有可能被执行），然后调度执行其它线程
2. 具体的实现依赖于操作系统的任务调度器

#### 线程优先级

- 线程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它

- 如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用

- 设置方法：

  ```
  thread1.setPriority(Thread.MAX_PRIORITY); //设置为优先级最高Copy
  ```

### (3)join()方法

用于等待某个线程结束。哪个线程内调用join()方法，就等待哪个线程结束，然后再去执行其他线程。

如在主线程中调用ti.join()，则是主线程等待t1线程结束

```
Thread thread = new Thread();
//等待thread线程执行结束
thread.join();
//最多等待1000ms,如果1000ms内线程执行完毕，则会直接执行下面的语句，不会等够1000ms
thread.join(1000);Copy
```

### (4)interrupt()方法

用于打断**阻塞**(sleep wait join…)的线程。 处于阻塞状态的线程，CPU不会给其分配时间片。

- 如果一个线程在在运行中被打断，打断标记会被置为true。
- 如果是打断因sleep wait join方法而被阻塞的线程，会将打断标记置为false

```
//用于查看打断标记，返回值被boolean类型
t1.isInterrupted();Copy
```

正常运行的线程在被打断后，**不会停止**，会继续执行。如果要让线程在被打断后停下来，需要**使用打断标记来判断**。

```
while(true) {
    if(Thread.currentThread().isInterrupted()) {
        break;
    }
}Copy
```

##### **interrupt方法的应用**——两阶段终止模式

当我们在执行线程一时，想要终止线程二，这是就需要使用interrupt方法来**优雅**的停止线程二。

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144553.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144553.png)

**代码**

```
public class Test7 {
	public static void main(String[] args) throws InterruptedException {
		Monitor monitor = new Monitor();
		monitor.start();
		Thread.sleep(3500);
		monitor.stop();
	}
}

class Monitor {

	Thread monitor;

	/**
	 * 启动监控器线程
	 */
	public void start() {
		//设置线控器线程，用于监控线程状态
		monitor = new Thread() {
			@Override
			public void run() {
				//开始不停的监控
				while (true) {
                    //判断当前线程是否被打断了
					if(Thread.currentThread().isInterrupted()) {
						System.out.println("处理后续任务");
                        //终止线程执行
						break;
					}
					System.out.println("监控器运行中...");
					try {
						//线程休眠
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						//如果是在休眠的时候被打断，不会将打断标记设置为true，这时要重新设置打断标记
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		monitor.start();
	}

	/**
	 * 	用于停止监控器线程
	 */
	public void stop() {
		//打断线程
		monitor.interrupt();
	}
}Copy
```

### (5)不推荐使用的打断方法

- stop方法 停止线程运行（可能造成共享资源无法被释放，其他线程无法使用这些共享资源）
- suspend（暂停线程）/resume（恢复线程）方法

### (6)守护线程

当JAVA进程中有多个线程在执行时，只有当所有非守护线程都执行完毕后，JAVA进程才会结束。**但当非守护线程全部执行完毕后，守护线程无论是否执行完毕，也会一同结束。**

```
//将线程设置为守护线程, 默认为false
monitor.setDaemon(true);Copy
```

**守护线程的应用**

- 垃圾回收器线程就是一种守护线程
- Tomcat 中的 Acceptor 和 Poller 线程都是守护线程，所以 Tomcat 接收到 shutdown 命令后，不会等 待它们处理完当前请求

## 4、线程的状态

## 4、线程的状态

### (1)五种状态

这是从 **操作系统** 层面来描述的

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144606.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144606.png)

- 【初始状态】仅是在语言层面创建了线程对象，还未与操作系统线程关联（例如线程调用了start方法）
- 【可运行状态】（就绪状态）指该线程已经被创建（与操作系统线程关联），可以由 CPU 调度执行
- 【运行状态】指获取了 CPU 时间片运行中的状态
  - 当 CPU 时间片用完，会从【运行状态】转换至【可运行状态】，会导致线程的上下文切换
- 【阻塞状态】
  - 如果调用了阻塞 API，如 BIO 读写文件，这时该线程实际不会用到 CPU，会导致线程上下文切换，进入 【阻塞状态】
  - 等 BIO 操作完毕，会由操作系统唤醒阻塞的线程，转换至【可运行状态】
  - 与【可运行状态】的区别是，对【阻塞状态】的线程来说只要它们一直不唤醒，调度器就一直不会考虑调度它们
- 【终止状态】表示线程已经执行完毕，生命周期已经结束，不会再转换为其它状态

### (2)六种状态

这是从 **Java API** 层面来描述的
根据 Thread.State 枚举，分为六种状态

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144621.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144621.png)

- **NEW** 线程刚被创建，但是还没有调用 start() 方法
- **RUNNABLE** 当调用了 start() 方法之后，注意，Java API 层面的 RUNNABLE 状态涵盖了操作系统层面的 【可运行状态】、【运行状态】和【阻塞状态】（由于 BIO 导致的线程阻塞，在 Java 里无法区分，仍然认为 是可运行）
- **BLOCKED ， WAITING ， TIMED_WAITING** 都是 **Java API 层面**对【阻塞状态】的细分，如sleep就位TIMED_WAITING， join为WAITING状态。后面会在状态转换一节详述。
- **TERMINATED** 当线程代码运行结束

# 三、共享模型之管程

## 1、共享带来的问题

### (1)临界区 Critical Section

- 一个程序运行多个线程本身是没有问题的
- 问题出在多个线程访问共享资源
  - 多个线程读共享资源其实也没有问题
  - 在多个线程对共享资源读写操作时发生指令交错，就会出现问题
- 一段代码块内如果存在对共享资源的多线程读写操作，称这段代码块为**临界区**
  例如，下面代码中的临界区

```
static int counter = 0;
 
static void increment() 
// 临界区 
{   
    counter++; 
}
 
static void decrement() 
// 临界区 
{ 
    counter--; 
}Copy
```

### **(2)竞态条件 Race Condition**

多个线程在**临界区**内执行，由于代码的执行序列不同而导致结果无法预测，称之为发生了**竞态条件**

## 2、synchronized 解决方案

### (1)解决手段

为了避免临界区的竞态条件发生，有多种手段可以达到目的。

- 阻塞式的解决方案：synchronized，Lock
- 非阻塞式的解决方案：原子变量

本次课使用阻塞式的解决方案：**synchronized**，来解决上述问题，即俗称的**【对象锁】**，它采用互斥的方式让同一 时刻至多只有一个线程能持有【对象锁】，其它线程再想获取这个【对象锁】时就会阻塞住(blocked)。这样就能保证拥有锁 的线程可以安全的执行临界区内的代码，不用担心线程上下文切换

### (2)synchronized语法

```
synchronized(对象) {
	//临界区
}Copy
```

例：

```
static int counter = 0; 
//创建一个公共对象，作为对象锁的对象
static final Object room = new Object();
 
public static void main(String[] args) throws InterruptedException {    
	Thread t1 = new Thread(() -> {        
    for (int i = 0; i < 5000; i++) {            
        synchronized (room) {     
        counter++;            
       	 }       
 	   }    
    }, "t1");
 
    Thread t2 = new Thread(() -> {       
        for (int i = 0; i < 5000; i++) {         
            synchronized (room) {            
            counter--;          
            }    
        } 
    }, "t2");
 
    t1.start();    
    t2.start(); 
    t1.join();   
    t2.join();    
    log.debug("{}",counter); 
}Copy
```

### (3)synchronized加在方法上

- 加在成员方法上

  ```
  public class Demo {
  	//在方法上加上synchronized关键字
  	public synchronized void test() {
  	
  	}
  	//等价于
  	public void test() {
  		synchronized(this) {
  		
  		}
  	}
  }Copy
  ```

- 加在静态方法上

  ```
  public class Demo {
  	//在静态方法上加上synchronized关键字
  	public synchronized static void test() {
  	
  	}
  	//等价于
  	public void test() {
  		synchronized(Demo.class) {
  		
  		}
  	}
  }Copy
  ```

## 3、变量的线程安全分析

#### 成员变量和静态变量是否线程安全？

- 如果它们没有共享，则线程安全
- 如果它们被共享了，根据它们的状态是否能够改变，又分两种情况
  - 如果只有读操作，则线程安全
  - 如果有读写操作，则这段代码是临界区，需要考虑线程安全

#### 局部变量是否线程安全？

- 局部变量是线程安全的

- 但局部变量引用的对象则未必 （要看该对象

  是否被共享

  且被执行了读写操作）

  - 如果该对象没有逃离方法的作用范围，它是线程安全的
  - 如果该对象逃离方法的作用范围，需要考虑线程安全

- 局部变量是线程安全的——每个方法都在对应线程的栈中创建栈帧，不会被其他线程共享

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144636.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144636.png)

- 如果调用的对象被共享，且执行了读写操作，则**线程不安全**

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144649.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144649.png)

- 如果是局部变量，则会在堆中创建对应的对象，不会存在线程安全问题。

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144702.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144702.png)

### 常见线程安全类

- String
- Integer
- StringBuﬀer
- Random
- Vector （List的线程安全实现类）
- Hashtable （Hash的线程安全实现类）
- java.util.concurrent 包下的类

这里说它们是线程安全的是指，多个线程调用它们**同一个实例的某个方法时**，是线程安全的

- 它们的每个方法是原子的（都被加上了synchronized）
- 但注意它们**多个方法的组合不是原子的**，所以可能会出现线程安全问题

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144903.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144903.png)

### 不可变类线程安全性

String、Integer 等都是**不可变类**，因为其内部的状态不可以改变，因此它们的方法都是线程安全的

有同学或许有疑问，String 有 replace，substring 等方法【可以】改变值啊，那么这些方法又是如何保证线程安 全的呢？

这是因为这些方法的返回值都**创建了一个新的对象**，而不是直接改变String、Integer对象本身。

## 4、Monitor概念

### (1)原理之Monitor

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144917.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144917.png)

- 当线程执行到临界区代码时，如果使用了synchronized，会先查询synchronized中所指定的对象(obj)**是否绑定了Monitor**。

  - 如果**没有绑定**，则会先去去与Monitor绑定，并且将Owner设为当前线程。

  - 如果

    已经绑定

    ，则会去查询该Monitor是否已经有了Owner

    - 如果没有，则Owner与将当前线程绑定
    - 如果有，则放入EntryList，进入阻塞状态(blocked)

- 当Monitor的Owner将临界区中代码执行完毕后，Owner便会被清空，此时EntryList中处于**阻塞**状态的线程会被**叫醒并竞争**，此时的竞争是**非公平的**

- **注意**：

  - 对象在使用了synchronized后与Monitor绑定时，会将对象头中的**Mark Word**置为Monitor指针。
  - 每个对象都会绑定一个**唯一的Monitor**，如果synchronized中所指定的对象(obj)**不同**，则会绑定**不同**的Monitor

## 5、Synchronized原理进阶

### 对象头格式

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144926.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144926.png)

### (1)轻量级锁（用于优化Monitor这类的重量级锁）

**轻量级锁使用场景：**当一个对象被多个线程所访问，但访问的时间是**错开的（不存在竞争）**，此时就可以使用**轻量级锁**来优化。

- 创建**锁记录**（Lock Record）对象，每个线程的栈帧都会包含一个锁记录对象，内部可以存储锁定对象的mark word（不再一开始就使用Monitor）

  [![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144942.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144942.png)

- 让锁记录中的Object reference指向锁对象（Object），并尝试用cas去替换Object中的mark word，将此mark word放入lock record中保存

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144950.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144950.png)

- 如果cas替换成功，则将Object的对象头替换为**锁记录的地址**和**状态 00（轻量级锁状态）**，并由该线程给对象加锁

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144957.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608144957.png)

### (2)锁膨胀

- 如果一个线程在给一个对象加轻量级锁时，**cas替换操作失败**（因为此时其他线程已经给对象加了轻量级锁），此时该线程就会进入**锁膨胀**过程

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145004.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145004.png)

- 此时便会给对象加上重量级锁（使用Monitor）

  - 将对象头的Mark Word改为Monitor的地址，并且状态改为01(重量级锁)

  - 并且该线程放入入EntryList中，并进入阻塞状态(blocked)

    [![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145148.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145148.png)

### (3)自旋优化

**重量级锁**竞争时，还可以使用自选来优化，如果当前线程在**自旋成功**（使用锁的线程退出了同步块，**释放了锁**），这时就可以避免线程进入阻塞状态。

- 第一种情况

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145136.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145136.png)

- 第二种情况

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145125.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145125.png)

### (4)偏向锁(用于优化轻量级锁重入)

轻量级锁在没有竞争时，每次**重入**（该线程执行的方法中再次锁住该对象）操作仍需要cas替换操作，这样是会使性能降低的。

所以引入了**偏向锁**对性能进行优化：在**第一次**cas时会将**线程的ID**写入对象的Mark Word中。此后发现这个线程ID就是自己的，就表示没有竞争，就不需要再次cas，以后只要不发生竞争，这个对象就归该线程所有。

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145109.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145109.png)

#### 偏向状态

- Normal：一般状态，没有加任何锁，前面62位保存的是对象的信息，**最后2位为状态（01），倒数第三位表示是否使用偏向锁（未使用：0）**
- Biased：偏向状态，使用偏向锁，前面54位保存的当前线程的ID，**最后2位为状态（01），倒数第三位表示是否使用偏向锁（使用：1）**
- Lightweight：使用轻量级锁，前62位保存的是锁记录的指针，**最后两位为状态（00）**
- Heavyweight：使用重量级锁，前62位保存的是Monitor的地址指针，**后两位为状态(10)**

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145101.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145101.png)

- 如果开启了偏向锁（默认开启），在创建对象时，对象的Mark Word后三位应该是101
- 但是偏向锁默认是**有延迟**的，不会再程序一启动就生效，而是会在程序运行一段时间（几秒之后），才会对创建的对象设置为偏向状态
- 如果没有开启偏向锁，对象的Mark Word后三位应该是001

#### 撤销偏向

以下几种情况会使对象的偏向锁失效

- 调用对象的hashCode方法
- 多个线程使用该对象
- **调用了wait/notify方法**（调用wait方法会导致锁膨胀而使用**重量级锁**）

### (5)批量重偏向

- 如果对象虽然被多个线程访问，但是线程间不存在竞争，这时偏向T1的对象仍有机会重新偏向T2
  - 重偏向会重置Thread ID
- 当撤销超过20次后（超过阈值），JVM会觉得是不是偏向错了，这时会在给对象加锁时，重新偏向至加锁线程。

### (6)批量撤销

当撤销偏向锁的阈值超过40以后，就会将**整个类的对象都改为不可偏向的**

## 6、Wait/Notify

### (1)原理

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145204.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145204.png)

- 锁对象调用wait方法（obj.wait），就会使当前线程进入WaitSet中，变为WAITING状态。

- 处于BLOCKED和WAITING状态的线程都为

  阻塞

  状态，CPU都不会分给他们时间片。但是有所区别：

  - BLOCKED状态的线程是在竞争对象时，发现Monitor的Owner已经是别的线程了，此时就会进入EntryList中，并处于BLOCKED状态
  - WAITING状态的线程是获得了对象的锁，但是自身因为某些原因需要进入阻塞状态时，锁对象调用了wait方法而进入了WaitSet中，处于WAITING状态

- BLOCKED状态的线程会在锁被释放的时候被唤醒，但是处于WAITING状态的线程只有被锁对象调用了notify方法(obj.notify/obj.notifyAll)，才会被唤醒。

**注：只有当对象被锁以后，才能调用wait和notify方法**

```
public class Test1 {
	final static Object LOCK = new Object();
	public static void main(String[] args) throws InterruptedException {
        //只有在对象被锁住后才能调用wait方法
		synchronized (LOCK) {
			LOCK.wait();
		}
	}
}Copy
```

### (2)Wait与Sleep的区别

**不同点**

- Sleep是Thread类的静态方法，Wait是Object的方法，Object又是所有类的父类，所以所有类都有Wait方法。
- Sleep在阻塞的时候不会释放锁，而Wait在阻塞的时候会释放锁
- Sleep不需要与synchronized一起使用，而Wait需要与synchronized一起使用（对象被锁以后才能使用）

**相同点**

- 阻塞状态都为**TIMED_WAITING**

### (3)优雅地使用wait/notify

**什么时候适合使用wait**

- 当线程**不满足某些条件**，需要暂停运行时，可以使用wait。这样会将**对象的锁释放**，让其他线程能够继续运行。如果此时使用sleep，会导致所有线程都进入阻塞，导致所有线程都没法运行，直到当前线程sleep结束后，运行完毕，才能得到执行。

**使用wait/notify需要注意什么**

- 当有**多个**线程在运行时，对象调用了wait方法，此时这些线程都会进入WaitSet中等待。如果这时使用了**notify**方法，可能会造成**虚假唤醒**（唤醒的不是满足条件的等待线程），这时就需要使用**notifyAll**方法

```
synchronized (LOCK) {
	while(//不满足条件，一直等待，避免虚假唤醒) {
		LOCK.wait();
	}
	//满足条件后再运行
}

synchronized (LOCK) {
	//唤醒所有等待线程
	LOCK.notifyAll();
}Copy
```

## 7、模式之保护性暂停

### (1)定义

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145223.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145223.png)

### (2)举例

```
public class Test2 {
	public static void main(String[] args) {
		String hello = "hello thread!";
		Guarded guarded = new Guarded();
		new Thread(()->{
			System.out.println("想要得到结果");
			synchronized (guarded) {
				System.out.println("结果是："+guarded.getResponse());
			}
			System.out.println("得到结果");
		}).start();

		new Thread(()->{
			System.out.println("设置结果");
			synchronized (guarded) {
				guarded.setResponse(hello);
			}
		}).start();
	}
}

class Guarded {
	/**
	 * 要返回的结果
	 */
	private Object response;
	
    //优雅地使用wait/notify
	public Object getResponse() {
		//如果返回结果为空就一直等待，避免虚假唤醒
		while(response == null) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
		synchronized (this) {
			//唤醒休眠的线程
			this.notifyAll();
		}
	}

	@Override
	public String toString() {
		return "Guarded{" +
				"response=" + response +
				'}';
	}
}Copy
```

**带超时判断的暂停**

```
public Object getResponse(long time) {
		synchronized (this) {
			//获取开始时间
			long currentTime = System.currentTimeMillis();
			//用于保存已经等待了的时间
			long passedTime = 0;
			while(response == null) {
				//看经过的时间-开始时间是否超过了指定时间
				long waitTime = time -passedTime;
				if(waitTime <= 0) {
					break;
				}
				try {
                   	//等待剩余时间
					this.wait(waitTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//获取当前时间
				passedTime = System.currentTimeMillis()-currentTime		
            }
		}
		return response;
	}Copy
```

### (3)join源码——使用保护性暂停模式

```
public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }Copy
```

## 8、park/unpark

### (1)基本使用

**park/unpark都是LockSupport类中的的方法**

```
//暂停线程运行
LockSupport.park;

//恢复线程运行
LockSupport.unpark(thread);Copy
public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(()-> {
			System.out.println("park");
            //暂停线程运行
			LockSupport.park();
			System.out.println("resume");
		}, "t1");
		thread.start();

		Thread.sleep(1000);
		System.out.println("unpark");
    	//恢复线程运行
		LockSupport.unpark(thread);
	}Copy
```

### (2)特点

**与wait/notify的区别**

- wait，notify 和 notifyAll 必须配合**Object Monitor**一起使用，而park，unpark不必
- park ，unpark 是以**线程为单位**来**阻塞**和**唤醒**线程，而 notify 只能随机唤醒一个等待线程，notifyAll 是唤醒所有等待线程，就不那么精确
- park & unpark 可以**先 unpark**，而 wait & notify 不能先 notify
- **park不会释放锁**，而wait会释放锁

### (3)原理

每个线程都有一个自己的**Park对象**，并且该对象**_counter, _cond,__mutex**组成

- 先调用park再调用unpark时

  - 先调用park

    - 线程运行时，会将Park对象中的**_counter的值设为0**；
    - 调用park时，会先查看counter的值是否为0，如果为0，则将线程放入阻塞队列cond中
    - 放入阻塞队列中后，会**再次**将counter设置为0

  - 然后调用unpark

    - 调用unpark方法后，会将counter的值设置为1

    - 去唤醒阻塞队列cond中的线程

    - 线程继续运行并将counter的值设为0

      [![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145250.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145250.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145303.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145303.png)

- 先调用unpark，再调用park
  - 调用unpark
    - 会将counter设置为1（运行时0）
  - 调用park方法
    - 查看counter是否为0
    - 因为unpark已经把counter设置为1，所以此时将counter设置为0，但**不放入**阻塞队列cond中

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145313.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145313.png)

## 9、线程中的状态转换

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145330.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145330.png)

### 情况一：NEW –> RUNNABLE

- 当调用了t.start()方法时，由 NEW –> RUNNABLE

### 情况二： RUNNABLE <–> WAITING

- 当调用了t 线程用 synchronized(obj) 获取了对象锁后
  - 调用 obj.wait() 方法时，t 线程从 RUNNABLE –> WAITING
  - 调用 obj.notify() ， obj.notifyAll() ， t.interrupt() 时
    - 竞争锁成功，t 线程从 WAITING –> RUNNABLE
    - 竞争锁失败，t 线程从 WAITING –> BLOCKED

### 情况三：RUNNABLE <–> WAITING

- 当前线程

  调用 t.join() 方法时，当前线程从 RUNNABLE –> WAITING

  - 注意是**当前线程**在t 线程对象的监视器上等待

- t 线程**运行结束**，或调用了**当前线程**的 interrupt() 时，当前线程从 WAITING –> RUNNABLE

### 情况四： RUNNABLE <–> WAITING

- 当前线程调用 LockSupport.park() 方法会让当前线程从 RUNNABLE –> WAITING
- 调用 LockSupport.unpark(目标线程) 或调用了线程 的 interrupt() ，会让目标线程从 WAITING –> RUNNABLE

### 情况五： RUNNABLE <–> TIMED_WAITING

t 线程用 synchronized(obj) 获取了对象锁后

- 调用 obj.wait(**long n**) 方法时，t 线程从 RUNNABLE –> TIMED_WAITING
- t 线程等待时间超过了 n 毫秒，或调用 obj.notify() ， obj.notifyAll() ， t.interrupt() 时
  - 竞争锁成功，t 线程从 TIMED_WAITING –> RUNNABLE
  - 竞争锁失败，t 线程从 TIMED_WAITING –> BLOCKED

### 情况六：RUNNABLE <–> TIMED_WAITING

- 当前线程调用 t.join

  (long n

  ) 方法时，当前线程从 RUNNABLE –> TIMED_WAITING

  - 注意是当前线程在t 线程对象的监视器上等待

- 当前线程等待时间超过了 n 毫秒，或t 线程运行结束，或调用了当前线程的 interrupt() 时，当前线程从 TIMED_WAITING –> RUNNABLE

### 情况七：RUNNABLE <–> TIMED_WAITING

- 当前线程调用 Thread.sleep(long n) ，当前线程从 RUNNABLE –> TIMED_WAITING
- 当前线程等待时间超过了 n 毫秒，当前线程从 TIMED_WAITING –> RUNNABLE

### 情况八：RUNNABLE <–> TIMED_WAITING

- 当前线程调用 LockSupport.parkNanos(long nanos) 或 LockSupport.parkUntil(long millis) 时，当前线 程从 RUNNABLE –> TIMED_WAITING
- 调用 LockSupport.unpark(目标线程) 或调用了线程 的 interrupt() ，或是等待超时，会让目标线程从 TIMED_WAITING–> RUNNABLE

### 情况九：RUNNABLE <–> BLOCKED

- t 线程用 synchronized(obj) 获取了对象锁时如果**竞争失败**，从 RUNNABLE –> BLOCKED
- 持 obj 锁线程的同步代码块执行完毕，会唤醒该对象上所有 BLOCKED 的线程重新竞争，如果其中 t 线程竞争 成功，从 BLOCKED –> RUNNABLE ，其它**失败**的线程仍然 BLOCKED

### 情况十： RUNNABLE <–> TERMINATED

当前线**程所有代码运行完毕**，进入 TERMINATED

## 10、多把锁

**将锁的粒度细分**

```
class BigRoom {
    //额外创建对象来作为锁
	private final Object studyRoom = new Object();
	private final Object bedRoom = new Object();
}Copy
```

## 11、活跃性

### (1)定义

因为某种原因，使得代码一直无法执行完毕，这样的现象叫做活跃性

### (2)死锁

有这样的情况：一个线程需要**同时获取多把锁**，这时就容易发生死锁

如：t1线程获得A对象 锁，接下来想获取B对象的锁t2线程获得B对象锁，接下来想获取A对象的锁

```
public static void main(String[] args) {
		final Object A = new Object();
		final Object B = new Object();
		new Thread(()->{
			synchronized (A) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (B) {

				}
			}
		}).start();

		new Thread(()->{
			synchronized (B) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (A) {

				}
			}
		}).start();
	}Copy
```

#### 发生死锁的必要条件

- 互斥条件
  - 在一段时间内，一种资源只能被一个进程所使用
- 请求和保持条件
  - 进程已经拥有了至少一种资源，同时又去申请其他资源。因为其他资源被别的进程所使用，该进程进入阻塞状态，并且不释放自己已有的资源
- 不可抢占条件
  - 进程对已获得的资源在未使用完成前不能被强占，只能在进程使用完后自己释放
- 循环等待条件
  - 发生死锁时，必然存在一个进程——资源的循环链。

#### 定位死锁的方法

- jps+jstack ThreadID

  - 在JAVA控制台中的Terminal中输入**jps**指令可以查看运行中的线程ID，使用**jstack ThreadID**可以查看线程状态。

    [![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145351.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145351.png)

  ```
  F:\Thread_study>jps
  20672 RemoteMavenServer36
  22880 Jps
  4432 Launcher
  5316 Test5
  20184 KotlinCompileDaemon
  11132
  
  F:\Thread_study>jstack 5316Copy
  ```

- 打印的结果

  ```
  //找到一个java级别的死锁
  Found one Java-level deadlock:
  =============================
  "Thread-1":
    waiting to lock monitor 0x0000000017f40de8 (object 0x00000000d6188880, a java.lang.Object),
    which is held by "Thread-0"
  "Thread-0":
    waiting to lock monitor 0x0000000017f43678 (object 0x00000000d6188890, a java.lang.Object),
    which is held by "Thread-1"Copy
  ```

- jconsole检测死锁

  [![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145405.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145405.png)

  [![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145416.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145416.png)

#### 哲学家就餐问题

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145436.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145436.png)

#### 避免死锁的方法

在线程使用锁对象时**，顺序加锁**即可避免死锁

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145450.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608145450.png)

### (3)活锁

活锁出现在两个线程**互相改变对方的结束条件**，后谁也无法结束。

#### 避免活锁的方法

在线程执行时，中途给予**不同的间隔时间**即可。

#### 死锁与活锁的区别

- 死锁是因为线程互相持有对象想要的锁，并且都不释放，最后到时**线程阻塞**，**停止运行**的现象。
- 活锁是因为线程间修改了对方的结束条件，而导致代码**一直在运行**，却一直**运行不完**的现象。

### (4)饥饿

某些线程因为优先级太低，导致一直无法获得资源的现象。

在使用顺序加锁时，可能会出现饥饿现象

## 12、ReentrantLock

**和synchronized相比具有的的特点**

- 可中断
- 可以设置超时时间
- 可以设置为公平锁 (先到先得)
- 支持多个条件变量( 具有**多个**waitset)

**基本语法**

```
//获取ReentrantLock对象
private ReentrantLock lock = new ReentrantLock();
//加锁
lock.lock();
try {
	//需要执行的代码
}finally {
	//释放锁
	lock.unlock();
}Copy
```

#### 可重入

- 可重入是指同一个线程如果首次获得了这把锁，那么因为它是这把锁的拥有者，因此有权利再次获取这把锁
- 如果是不可重入锁，那么第二次获得锁时，自己也会被锁挡住

#### 可打断

如果某个线程处于阻塞状态，可以调用其interrupt方法让其停止阻塞，获得锁失败

**简而言之**就是：处于阻塞状态的线程，被打断了就不用阻塞了，直接停止运行

```
public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		Thread t1 = new Thread(()-> {
			try {
				//加锁，可打断锁
				lock.lockInterruptibly();
			} catch (InterruptedException e) {
				e.printStackTrace();
                //被打断，返回，不再向下执行
				return;
			}finally {
				//释放锁
				lock.unlock();
			}

		});

		lock.lock();
		try {
			t1.start();
			Thread.sleep(1000);
			//打断
			t1.interrupt();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}Copy
```

#### 锁超时

使用**lock.tryLock**方法会返回获取锁是否成功。如果成功则返回true，反之则返回false。

并且tryLock方法可以**指定等待时间**，参数为：tryLock(long timeout, TimeUnit unit), 其中timeout为最长等待时间，TimeUnit为时间单位

**简而言之**就是：获取失败了、获取超时了或者被打断了，不再阻塞，直接停止运行

不设置等待时间

```
public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		Thread t1 = new Thread(()-> {
            //未设置等待时间，一旦获取失败，直接返回false
			if(!lock.tryLock()) {
				System.out.println("获取失败");
                //获取失败，不再向下执行，返回
				return;
			}
			System.out.println("得到了锁");
			lock.unlock();
		});


		lock.lock();
		try{
			t1.start();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}Copy
```

设置等待时间

```
public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		Thread t1 = new Thread(()-> {
			try {
				//判断获取锁是否成功，最多等待1秒
				if(!lock.tryLock(1, TimeUnit.SECONDS)) {
					System.out.println("获取失败");
					//获取失败，不再向下执行，直接返回
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				//被打断，不再向下执行，直接返回
				return;
			}
			System.out.println("得到了锁");
			//释放锁
			lock.unlock();
		});


		lock.lock();
		try{
			t1.start();
			//打断等待
			t1.interrupt();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}Copy
```

#### 公平锁

在线程获取锁失败，进入阻塞队列时，**先进入**的会在锁被释放后**先获得**锁。这样的获取方式就是**公平**的。

```
//默认是不公平锁，需要在创建时指定为公平锁
ReentrantLock lock = new ReentrantLock(true);Copy
```

#### 条件变量

synchronized 中也有条件变量，就是我们讲原理时那个 waitSet 休息室，当条件不满足时进入waitSet 等待

ReentrantLock 的条件变量比 synchronized 强大之处在于，它是支持**多个**条件变量的，这就好比

- synchronized 是那些不满足条件的线程都在一间休息室等消息
- 而 ReentrantLock 支持多间休息室，有专门等烟的休息室、专门等早餐的休息室、唤醒时也是按休息室来唤 醒

使用要点：

- await 前需要**获得锁**
- await 执行后，会释放锁，进入 conditionObject 等待
- await 的线程被唤醒（或打断、或超时）取重新竞争 lock 锁
- 竞争 lock 锁成功后，从 await 后继续执

```
static Boolean judge = false;
public static void main(String[] args) throws InterruptedException {
	ReentrantLock lock = new ReentrantLock();
	//获得条件变量
	Condition condition = lock.newCondition();
	new Thread(()->{
		lock.lock();
		try{
			while(!judge) {
				System.out.println("不满足条件，等待...");
				//等待
				condition.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("执行完毕！");
			lock.unlock();
		}
	}).start();

	new Thread(()->{
		lock.lock();
		try {
			Thread.sleep(1);
			judge = true;
			//释放
			condition.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}).start();
}Copy
```

#### 通过Lock与AQS实现可重入锁

```
public class MyLock implements Lock {
   private static class Sync extends AbstractQueuedSynchronizer {
      @Override
      protected boolean tryAcquire(int arg) {
         if (getExclusiveOwnerThread() == null) {
            if (compareAndSetState(0, 1)) {
               setExclusiveOwnerThread(Thread.currentThread());
               return true;
            }
            return false;
         }

         if (getExclusiveOwnerThread() == Thread.currentThread()) {
            int state = getState();
            compareAndSetState(state, state + 1);
            return true;
         }

         return false;
      }

      @Override
      protected boolean tryRelease(int arg) {
         if (getState() <= 0) {
            throw new IllegalMonitorStateException();
         }

         if (getExclusiveOwnerThread() != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
         }

         int state = getState();
         if (state == 1) {
            setExclusiveOwnerThread(null);
            compareAndSetState(state, 0);
         } else {
            compareAndSetState(state, state - 1);
         }
         return true;
      }

      @Override
      protected boolean isHeldExclusively() {
         return getState() >= 1;
      }

      public Condition newCondition() {
         return new ConditionObject();
      }

   }

   Sync sync = new Sync();

   @Override
   public void lock() {
      sync.acquire(1);
   }

   @Override
   public void lockInterruptibly() throws InterruptedException {
      sync.acquireInterruptibly(1);
   }

   @Override
   public boolean tryLock() {
      return sync.tryAcquire(1);
   }

   @Override
   public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
      return sync.tryAcquireNanos(1, time);
   }

   @Override
   public void unlock() {
      sync.release(1);
   }

   @Override
   public Condition newCondition() {
      return sync.newCondition();
   }
}

class Main {
   static int num = 0;
   public static void main(String[] args) throws InterruptedException, IOException {
      MyLock lock = new MyLock();

      Object syncLock = new Object();

      Thread t1 = new Thread(() -> {
         for (int i = 0; i < 10000; i++) {
            lock.lock();
            try {
               lock.lock();
               try {
                  lock.lock();
                  try {
                     num++;
                  } finally {
                     lock.unlock();
                  }
               } finally {
                  lock.unlock();
               }
            } finally {
               lock.unlock();
            }
         }
      });

      Thread t2 = new Thread(() -> {
         for (int i = 0; i < 10000; i++) {
            lock.lock();
            try {
               lock.lock();
               try {
                  lock.lock();
                  try {
                     num--;
                  } finally {
                     lock.unlock();
                  }
               } finally {
                  lock.unlock();
               }
            } finally {
               lock.unlock();
            }
         }
      });

      t1.start();
      t2.start();
      t1.join();
      t2.join();

      int x = 0;
   }
}Copy
```

## 13、同步模式之顺序控制

### Wait/Notify版本

```
static final Object LOCK = new Object();
//判断先执行的内容是否执行完毕
static Boolean judge = false;
public static void main(String[] args) {
	new Thread(()->{
		synchronized (LOCK) {
			while (!judge) {
				try {
					LOCK.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("2");
		}
	}).start();

	new Thread(()->{
		synchronized (LOCK) {
			System.out.println("1");
			judge = true;
               //执行完毕，唤醒所有等待线程
			LOCK.notifyAll();
		}
	}).start();
}Copy
```

### 交替输出

**wait/notify版本**

```
public class Test4 {
	static Symbol symbol = new Symbol();
	public static void main(String[] args) {
		new Thread(()->{
			symbol.run("a", 1, 2);
		}).start();

		new Thread(()->{
			symbol.run("b", 2, 3);

		}).start();
		symbol.run("c", 3, 1);
		new Thread(()->{

		}).start();
	}
}

class Symbol {
	public synchronized void run(String str, int flag, int nextFlag) {
		for(int i=0; i<loopNumber; i++) {
			while(flag != this.flag) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(str);
			//设置下一个运行的线程标记
			this.flag = nextFlag;
			//唤醒所有线程
			this.notifyAll();
		}
	}

	/**
	 * 线程的执行标记， 1->a 2->b 3->c
	 */
	private int flag = 1;
	private int loopNumber = 5;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getLoopNumber() {
		return loopNumber;
	}

	public void setLoopNumber(int loopNumber) {
		this.loopNumber = loopNumber;
	}
}Copy
```

**await/signal版本**

```
public class Test5 {
	static AwaitSignal awaitSignal = new AwaitSignal();
	static Condition conditionA = awaitSignal.newCondition();
	static Condition conditionB = awaitSignal.newCondition();
	static Condition conditionC = awaitSignal.newCondition();
	public static void main(String[] args) {
		new Thread(()->{
			awaitSignal.run("a", conditionA, conditionB);
		}).start();

		new Thread(()->{
			awaitSignal.run("b", conditionB, conditionC);
		}).start();

		new Thread(()->{
			awaitSignal.run("c", conditionC, conditionA);
		}).start();


		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		awaitSignal.lock();
		try {
            //唤醒一个等待的线程
			conditionA.signal();
		}finally {
			awaitSignal.unlock();
		}
	}
}

class AwaitSignal extends ReentrantLock{
	public void run(String str, Condition thisCondition, Condition nextCondition) {
		for(int i=0; i<loopNumber; i++) {
			lock();
			try {
                //全部进入等待状态
				thisCondition.await();
				System.out.print(str);
				nextCondition.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				unlock();
			}
		}
	}

	private int loopNumber=5;

	public int getLoopNumber() {
		return loopNumber;
	}

	public void setLoopNumber(int loopNumber) {
		this.loopNumber = loopNumber;
	}
}Copy
```

## 14、ThreadLocal

### 简介

ThreadLocal是JDK包提供的，它提供了线程本地变量，也就是如果你创建了一个ThreadLocal变量，那么**访问这个变量的每个线程都会有这个变量的一个本地副本**。当多个线程操作这个变量时，实际操作的是自己本地内存里面的变量，从而避免了线程安全问题

### 使用

```
public class ThreadLocalStudy {
   public static void main(String[] args) {
      // 创建ThreadLocal变量
      ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
      ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

      // 创建两个线程，分别使用上面的两个ThreadLocal变量
      Thread thread1 = new Thread(()->{
         // stringThreadLocal第一次赋值
         stringThreadLocal.set("thread1 stringThreadLocal first");
         // stringThreadLocal第二次赋值
         stringThreadLocal.set("thread1 stringThreadLocal second");
         // userThreadLocal赋值
         userThreadLocal.set(new User("Nyima", 20));

         // 取值
         System.out.println(stringThreadLocal.get());
         System.out.println(userThreadLocal.get());
          
          // 移除
		 userThreadLocal.remove();
		 System.out.println(userThreadLocal.get());
      });

      Thread thread2 = new Thread(()->{
         // stringThreadLocal第一次赋值
         stringThreadLocal.set("thread2 stringThreadLocal first");
         // stringThreadLocal第二次赋值
         stringThreadLocal.set("thread2 stringThreadLocal second");
         // userThreadLocal赋值
         userThreadLocal.set(new User("Hulu", 20));

         // 取值
         System.out.println(stringThreadLocal.get());
         System.out.println(userThreadLocal.get());
      });

      // 启动线程
      thread1.start();
      thread2.start();
   }
}

class User {
   String name;
   int age;

   public User(String name, int age) {
      this.name = name;
      this.age = age;
   }

   @Override
   public String toString() {
      return "User{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
   }
}Copy
```

**运行结果**

```
thread1 stringThreadLocal second
thread2 stringThreadLocal second
User{name='Nyima', age=20}
User{name='Hulu', age=20}
nullCopy
```

从运行结果可以看出

- 每个线程中的ThreadLocal变量是每个线程私有的，而不是共享的
  - 从线程1和线程2的打印结果可以看出
- ThreadLocal其实就相当于其泛型类型的一个变量，只不过是每个线程私有的
  - stringThreadLocal被赋值了两次，保存的是最后一次赋值的结果
- ThreadLocal可以进行以下几个操作
  - set 设置值
  - get 取出值
  - remove 移除值

### 原理

#### Thread中的threadLocals

```
public class Thread implements Runnable {
 ...

 ThreadLocal.ThreadLocalMap threadLocals = null;

 // 放在后面说
 ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;

 ...
}Copy
static class ThreadLocalMap {
    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }Copy
```

可以看出Thread类中有一个threadLocals和一个inheritableThreadLocals，它们都是ThreadLocalMap类型的变量，而ThreadLocalMap是一个定制化的Hashmap。在默认情况下，每个线程中的这两个变量都为null。此处先讨论threadLocals，inheritableThreadLocals放在后面讨论

#### **ThreadLocal中的方法**

**set方法**

```
public void set(T value) {
    // 获取当前线程
    Thread t = Thread.currentThread();
    
    // 获得ThreadLocalMap对象 
    // 这里的get会返回Thread类中的threadLocals
    ThreadLocalMap map = getMap(t);
    
    // 判断map是否已经创建，没创建就创建并放入值，创建了就直接放入
    if (map != null)
        // ThreadLocal自生的引用作为key，传入的值作为value
        map.set(this, value);
    else
        createMap(t, value);
}Copy
```

**如果未创建**

```
void createMap(Thread t, T firstValue) {
    // 创建的同时设置想放入的值
    // hreadLocal自生的引用作为key，传入的值作为value
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}Copy
```

**get方法**

```
public T get() {
    // 获取当前线程
    Thread t = Thread.currentThread();
	// 获取当前线程的threadLocals变量
    ThreadLocalMap map = getMap(t);
    
    // 判断threadLocals是否被初始化了
    if (map != null) {
        // 已经初始化则直接返回
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    // 否则就创建threadLocals
    return setInitialValue();
}Copy
private T setInitialValue() {
    // 这个方法返回是null
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    
    // 无论map创建与否，最终value的值都为null
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
    return value;
}Copy
protected T initialValue() {
    return null;
}Copy
```

**remove方法**

```
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null)
        // 如果threadLocals已经被初始化，则移除
        m.remove(this);
}Copy
```

#### **总结**

在每个线程内部都有一个名为threadLocals的成员变量，该变量的类型为HashMap，其中**key为我们定义的ThreadLocal变量的this引用，value则为我们使用set方法设置的值**。每个线程的本地变量存放在线程自己的内存变量threadLocals中

只有当前线程**第一次调用ThreadLocal的set或者get方法时才会创建threadLocals**（inheritableThreadLocals也是一样）。其实每个线程的本地变量不是存放在ThreadLocal实例里面，而是存放在调用线程的threadLocals变量里面

## 15、InheritableThreadLocal

### 简介

从ThreadLocal的源码可以看出，无论是set、get、还是remove，都是相对于当前线程操作的

```
Thread.currentThread()Copy
```

所以ThreadLocal无法从父线程传向子线程，所以InheritableThreadLocal出现了，**它能够让父线程中ThreadLocal的值传给子线程。**

也就是从main所在的线程，传给thread1或thread2

### 使用

```
public class Demo1 {
   public static void main(String[] args) {
      ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
      InheritableThreadLocal<String> stringInheritable = new InheritableThreadLocal<>();

      // 主线程赋对上面两个变量进行赋值
      stringThreadLocal.set("this is threadLocal");
      stringInheritable.set("this is inheritableThreadLocal");

      // 创建线程
      Thread thread1 = new Thread(()->{
         // 获得ThreadLocal中存放的值
         System.out.println(stringThreadLocal.get());

         // 获得InheritableThreadLocal存放的值
         System.out.println(stringInheritable.get());
      });

      thread1.start();
   }
}Copy
```

**运行结果**

```
null
this is inheritableThreadLocalCopy
```

可以看出InheritableThreadLocal的值成功从主线程传入了子线程，而ThreadLocal则没有

### 原理

#### InheritableThreadLocal

```
public class InheritableThreadLocal<T> extends ThreadLocal<T> {
    // 传入父线程中的一个值，然后直接返回
    protected T childValue(T parentValue) {
        return parentValue;
    }

  	// 返回传入线程的inheritableThreadLocals
    // Thread中有一个inheritableThreadLocals变量
    // ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
    ThreadLocalMap getMap(Thread t) {
       return t.inheritableThreadLocals;
    }

 	// 创建一个inheritableThreadLocals
    void createMap(Thread t, T firstValue) {
        t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
    }
}Copy
```

由如上代码可知，InheritableThreadLocal继承了ThreadLocal，并重写了三个方法。InheritableThreadLocal重写了**createMap方法**，那么现在当第一次调用set方法时，创建的是当前线程的inheritableThreadLocals变量的实例而不再是threadLocals。当调用**getMap方法**获取当前线程内部的map变量时，获取的是inheritableThreadLocals而不再是threadLocals

#### childValue(T parentValue)方法的调用

在主函数运行时，会调用Thread的默认构造函数（**创建主线程**，也就是父线程），所以我们先看看Thread的默认构造函数

```
public Thread() {
    init(null, null, "Thread-" + nextThreadNum(), 0);
}Copy
private void init(ThreadGroup g, Runnable target, String name,
                  long stackSize, AccessControlContext acc,
                  boolean inheritThreadLocals) {
   	...
        
	// 获得当前线程的，在这里是主线程
    Thread parent = currentThread();
   
    ...
    
    // 如果父线程的inheritableThreadLocals存在
    // 我们在主线程中调用set和get时，会创建inheritableThreadLocals
    if (inheritThreadLocals && parent.inheritableThreadLocals != null)
        // 设置子线程的inheritableThreadLocals
        this.inheritableThreadLocals =
            ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
    
    /* Stash the specified stack size in case the VM cares */
    this.stackSize = stackSize;

    /* Set thread ID */
    tid = nextThreadID();
}Copy
static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
    return new ThreadLocalMap(parentMap);
}Copy
```

在createInheritedMap内部使用父线程的inheritableThreadLocals变量作为构造函数创建了一个新的ThreadLocalMap变量，然后赋值给了子线程的inheritableThreadLocals变量

```
private ThreadLocalMap(ThreadLocalMap parentMap) {
    Entry[] parentTable = parentMap.table;
    int len = parentTable.length;
    setThreshold(len);
    table = new Entry[len];

    for (int j = 0; j < len; j++) {
        Entry e = parentTable[j];
        if (e != null) {
            @SuppressWarnings("unchecked")
            ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
            if (key != null) {
                // 这里调用了 childValue 方法
                // 该方法会返回parent的值
                Object value = key.childValue(e.value);
                
                Entry c = new Entry(key, value);
                int h = key.threadLocalHashCode & (len - 1);
                while (table[h] != null)
                    h = nextIndex(h, len);
                table[h] = c;
                size++;
            }
        }
    }
}Copy
```

在该构造函数内部把父线程的inheritableThreadLocals成员变量的值复制到新的ThreadLocalMap对象中

#### 总结

InheritableThreadLocal类通过重写getMap和createMap，让本地变量保存到了具体线程的inheritableThreadLocals变量里面，那么线程在通过InheritableThreadLocal类实例的set或者get方法设置变量时，就会创建当前线程的inheritableThreadLocals变量。

**当父线程创建子线程时，构造函数会把父线程中inheritableThreadLocals变量里面的本地变量复制一份保存到子线程的inheritableThreadLocals变量里面。**
