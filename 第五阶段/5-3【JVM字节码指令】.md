# 第二章 字节码指令

可参考：https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5

接着上一节研究一下两组字节码指令，一个是 `public cn.linxuan.jvm.HelloWorld();` 构造方法的字节码指令：

```assembly
2a b7 00 01 b1
```

1. 2a => aload_0 加载 slot 0 的局部变量，即 this，做为下面的 invokespecial 构造方法调用的参数
2. b7 => invokespecial 预备调用构造方法，哪个方法呢？
3. 00 01 引用常量池中 #1 项，即【 Method java/lang/Object."<init>":()V 】
4. b1 表示返回

另一个是 `public static void main(java.lang.String[]);` 主方法的字节码指令：

```assembly
b2 00 02 12 03 b6 00 04 b1
```

1. b2 => getstatic 用来加载静态变量，哪个静态变量呢？
2. 00 02 引用常量池中 #2 项，即【Field java/lang/System.out:Ljava/io/PrintStream;】
3. 12 => ldc 加载参数，哪个参数呢？
4. 03 引用常量池中 #3 项，即 【String hello world】
5. b6 => invokevirtual 预备调用成员方法，哪个方法呢？
6. 00 04 引用常量池中 #4 项，即【Method java/io/PrintStream.println:(Ljava/lang/String;)V】
7. b1 表示返回

## 2.1 javap工具

Oracle 提供了 **javap** 工具来反编译 class 文件

```java
[root@localhost ~]# javap -v HelloWorld.class
Classfile /root/HelloWorld.class
  Last modified 2020-6-6; size 434 bytes
  MD5 checksum df1dce65bf6fb0b4c1de318051f4a67e
  Compiled from "Demo1.java"
public class com.linxuan.JVM.day5.Demo1
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #6.#15         // java/lang/Object."<init>":()V
   #2 = Fieldref           #16.#17        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = String             #18            // hello world
   #4 = Methodref          #19.#20        // java/io/PrintStream.println:(Ljava/lang/String;)V
   #5 = Class              #21            // com/nyima/JVM/day5/Demo1
   #6 = Class              #22            // java/lang/Object
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               main
  #12 = Utf8               ([Ljava/lang/String;)V
  #13 = Utf8               SourceFile
  #14 = Utf8               Demo1.java
  #15 = NameAndType        #7:#8          // "<init>":()V
  #16 = Class              #23            // java/lang/System
  #17 = NameAndType        #24:#25        // out:Ljava/io/PrintStream;
  #18 = Utf8               hello world
  #19 = Class              #26            // java/io/PrintStream
  #20 = NameAndType        #27:#28        // println:(Ljava/lang/String;)V
  #21 = Utf8               com/linxuan/JVM/day5/Demo1
  #22 = Utf8               java/lang/Object
  #23 = Utf8               java/lang/System
  #24 = Utf8               out
  #25 = Utf8               Ljava/io/PrintStream;
  #26 = Utf8               java/io/PrintStream
  #27 = Utf8               println
  #28 = Utf8               (Ljava/lang/String;)V
{
  public com.nyima.JVM.day5.Demo1();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:	// 字节码行号对应源代码行号
        line 7: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #2           // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #3           // String hello world
         5: invokevirtual #4           // Method java/io/PrintStream.println:(Ljava/lang/String;)V

         8: return
      LineNumberTable:
        line 9: 0		// 源代码第9行对应字节码0行。
        line 10: 8
}
```



## 2.2 图解方法执行流程

```java
public class Demo01 {
	public static void main(String[] args) {
		int a = 10;        
		int b = Short.MAX_VALUE + 1;        
		int c = a + b;        
		System.out.println(c);   
	}
}
```

```java
// 编译后：
Classfile /D:/Java/vscode-java/MavenProjects/jvm/target/classes/com/linxuan/Demo01.class
  Last modified 2022-8-23; size 583 bytes
  MD5 checksum ced08233f346a6db40f417d09ecd17fe
  Compiled from "Demo01.java"
public class com.linxuan.Demo01
  minor version: 0
  major version: 51
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Class              #2             // com/linxuan/Demo01
   #2 = Utf8               com/linxuan/Demo01
   #3 = Class              #4             // java/lang/Object
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Utf8               Code
   #8 = Methodref          #3.#9          // java/lang/Object."<init>":()V
   #9 = NameAndType        #5:#6          // "<init>":()V
  #10 = Utf8               LineNumberTable
  #11 = Utf8               LocalVariableTable
  #12 = Utf8               this
  #13 = Utf8               Lcom/linxuan/Demo01;
  #14 = Utf8               main
  #15 = Utf8               ([Ljava/lang/String;)V
  #16 = Integer            32768
  #17 = Fieldref           #18.#20        // java/lang/System.out:Ljava/io/PrintStream;
  #18 = Class              #19            // java/lang/System
  #19 = Utf8               java/lang/System
  #20 = NameAndType        #21:#22        // out:Ljava/io/PrintStream;
  #21 = Utf8               out
  #22 = Utf8               Ljava/io/PrintStream;
  #23 = Methodref          #24.#26        // java/io/PrintStream.println:(I)V
  #24 = Class              #25            // java/io/PrintStream
  #25 = Utf8               java/io/PrintStream
  #26 = NameAndType        #27:#28        // println:(I)V
  #27 = Utf8               println
  #28 = Utf8               (I)V
  #29 = Utf8               args
  #30 = Utf8               [Ljava/lang/String;
  #31 = Utf8               a
  #32 = Utf8               I
  #33 = Utf8               b
  #34 = Utf8               c
  #35 = Utf8               SourceFile
  #36 = Utf8               Demo01.java
{
  public com.linxuan.Demo01();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #8                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/linxuan/Demo01;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=4, args_size=1
         0: bipush        10
         2: istore_1
         3: ldc           #16                 // int 32768
         5: istore_2
         6: iload_1
         7: iload_2
         8: iadd
         9: istore_3
        10: getstatic     #17                 // Field java/lang/System.out:Ljava/io/PrintStream;
        13: iload_3
        14: invokevirtual #23                 // Method java/io/PrintStream.println:(I)V
        17: return
      LineNumberTable:
        line 6: 0
        line 7: 3
        line 8: 6
        line 9: 10
        line 10: 17
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      18     0  args   [Ljava/lang/String;
            3      15     1     a   I
            6      12     2     b   I
           10       8     3     c   I
}
SourceFile: "Demo01.java"
```

步骤如下：

类加载器将main方法所在的类进行类加载操作，所谓类加载实际上就是把二进制字节码数据读取到内存中。二进制字节码就是上面的所有数据了。

* **常量池载入运行时常量池**

  常量池也属于方法区，只不过这里单独提出来了

  `#16  = Integer 32768`，Short范围内的数字是和字节码指令存放在一起的，大于这个范围的时候就会存放在常量池里面了。

  ![](D:\Java\笔记\图片\5-1【jvm】\14-1.png)

  

* **方法字节码载入方法区**

  （stack=2，locals=4） 对应操作数栈有2个空间（每个空间4个字节），局部变量表中有4个槽位

  ```java
    public static void main(java.lang.String[]);
      descriptor: ([Ljava/lang/String;)V
      flags: ACC_PUBLIC, ACC_STATIC
      Code:
        stack=2, locals=4, args_size=1
           0: bipush        10
           2: istore_1
           3: ldc           #16                 // int 32768
           5: istore_2
           6: iload_1
           7: iload_2
           8: iadd
           9: istore_3
          10: getstatic     #17                 // Field java/lang/System.out:Ljava/io/PrintStream;
          13: iload_3
          14: invokevirtual #23                 // Method java/io/PrintStream.println:(I)V
          17: return
  ```

  ![](D:\Java\笔记\图片\5-1【jvm】\14-2.png)

  

* **执行引擎开始执行字节码**

  `bipush 10`：将一个 byte 压入操作数栈 （操作数栈的宽度是4个字节，byte是1个字节，所以其长度会用0或1补齐 4 个字节），类似的指令还有

  - `sipush` 将一个 short 压入操作数栈（其长度会补齐 4 个字节）
  - `ldc` 将一个 int 压入操作数栈
  - `ldc2_w` 将一个 long 压入操作数栈（因为 long 是 8 个字节 所以分两次压入）
  - 这里小的数字都是和字节码指令存在一起，超过 short 范围的数字存入了常量池

  ![](D:\Java\笔记\图片\5-1【jvm】\14-3.png)

  

  `istore_1`：将操作数栈栈顶元素弹出，放入局部变量表的slot 1中，后面的1就代表存入到哪一个槽位。

  对应Java代码中的`a = 10`。

  ![](D:\Java\笔记\图片\5-1【jvm】\14-4.png)

  ![](D:\Java\笔记\图片\5-1【jvm】\14-5.png)

  

* `ldc #3`：读取运行时常量池中#3，即32768(超过short最大值范围的数会被放到运行时常量池中)，将其加载到操作数栈中

  Short.MAX_VALUE 是 32767，所以 32768 = Short.MAX_VALUE + 1 实际是在编译期间计算好的

  ![](D:\Java\笔记\图片\5-1【jvm】\14-6.png)

  

* `istore 2`：将操作数栈中的元素弹出，放到局部变量表的2号位置

  ![](D:\Java\笔记\图片\5-1【jvm】\14-7.png)

  ![](D:\Java\笔记\图片\5-1【jvm】\14-8.png)

  

* `iload1 iload2`：将局部变量表中1号位置和2号位置的元素放入操作数栈中。因为只能在操作数栈中执行运算操作

  ![](D:\Java\笔记\图片\5-1【jvm】\14-9.png)

  ![](D:\Java\笔记\图片\5-1【jvm】\14-10.png)

  

* `iadd`：将操作数栈中的两个元素弹出栈并相加，结果再压入操作数栈中

  ![](D:\Java\笔记\图片\5-1【jvm】\14-11.png)
  
  ![](D:\Java\笔记\图片\5-1【jvm】\14-12.png)
  
  
  
* `istore 3`：将操作数栈中的元素弹出，放入局部变量表的3号位置

  ![](D:\Java\笔记\图片\5-1【jvm】\14-13.png)

  ![](D:\Java\笔记\图片\5-1【jvm】\14-14.png)

  

* `getstatic #17`：在运行时常量池中找到#17，发现是一个对象。在堆内存中找到该对象，并将其**引用**放入操作数栈中

  ![](D:\Java\笔记\图片\5-1【jvm】\14-15.png)

  ![](D:\Java\笔记\图片\5-1【jvm】\14-16.png)

  

* `iload_3`：将局部变量表中3号位置的元素压入操作数栈中

  ![](D:\Java\笔记\图片\5-1【jvm】\14-17.png)

  

* `invokevirtual #23`：找到常量池 #23 项，定位到方法区 java/io/PrintStream.println:(I)V 方法。生成新的栈帧（分配 locals、stack等），传递参数，执行新栈帧中的字节码。

  ![](D:\Java\笔记\图片\5-1【jvm】\14-18.png)

  执行完毕，弹出栈帧

  清除 main 操作数栈内容

  ![](D:\Java\笔记\图片\5-1【jvm】\14-19.png)

  

* `return`：完成 main 方法调用，弹出 main 栈帧，程序结束

## 2.3 分析i++与++i

从字节码角度分析 i++ 相关题目

- i++：先赋值后操作，先执行iload再执行iinc；
- ++i：先操作后赋值

```java
package cn.itcast.jvm.t3.bytecode;
/**
* 从字节码角度分析 a++ 相关题目
*/
public class Demo3_2 {
    public static void main(String[] args) {
        int a = 10;
        int b = a++ + ++a + a--;
        System.out.println(a);
        System.out.println(b);
    }
}
```

```java
public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: bipush        10
         2: istore_1
         3: iload_1
         4: iinc          1, 1
         7: iinc          1, 1
        10: iload_1
        11: iadd
        12: iload_1
        13: iinc          1, -1
        16: iadd
        17: istore_2
        18: getstatic     #16                 // Field java/lang/System.out:Ljava/io/PrintStream;
        21: iload_1
        22: invokevirtual #22                 // Method java/io/PrintStream.println:(I)V
        25: getstatic     #16                 // Field java/lang/System.out:Ljava/io/PrintStream;
        28: iload_2
        29: invokevirtual #22                 // Method java/io/PrintStream.println:(I)V
        32: return
      LineNumberTable:
        line 6: 0
        line 7: 3
        line 8: 18
        line 9: 25
        line 10: 32
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      33     0  args   [Ljava/lang/String;
            3      30     1     a   I
           18      15     2     b   I
```

分析：

- 注意 iinc 指令是直接在局部变量 slot 上进行运算
- a++ 和 ++a 的区别是先执行 iload 还是 先执行 iinc。a++是先执行iload再执行iinc，而++a相反。

![](D:\Java\笔记\图片\5-1【jvm】\15-1.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-2.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-3.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-4.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-5.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-6.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-7.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-8.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-9.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-10.png)

![](D:\Java\笔记\图片\5-1【jvm】\15-11.png)

## 2.4 条件判断指令

| 二进制 |  字节码   |       含义       |
| :----: | :-------: | :--------------: |
|  0x99  |   ifeq    |  判断是否 == 0   |
|  0x9a  |   ifne    |   判断是否 !=0   |
|  0x9b  |   iflt    |   判断是否 < 0   |
|  0x9c  |   ifge    |  判断是否 >= 0   |
|  0x9d  |   ifgt    |   判断是否 > 0   |
|  0x9e  |   ifle    |  判断是否 <= 0   |
|  0x9f  | if_icmpeq |  两个int是否 ==  |
|  0xa0  | if_icmpne |  两个int是否 !=  |
|  0xa1  | if_icmplt |  两个int是否 <   |
|  0xa2  | if_icmpge |  两个int是否 >=  |
|  0xa3  | if_icmpgt |  两个int是否 >   |
|  0xa4  | if_icmple |  两个int是否 <=  |
|  0xa5  | if_acmpeq | 两个引用是否 ==  |
|  0xa6  | if_acmpne | 两个引用是否 !=  |
|  0xc6  |  ifnull   | 判断是否 == null |
|  0xc7  | ifnonnull | 判断是否 != null |

几点说明：

- byte，short，char 都会按 int 比较，因为操作数栈都是 4 字节
- goto 用来进行跳转到指定行号的字节码

```java
public class Demo3_3 {
    public static void main(String[] args) {
        int a = 0;
        if(a == 0) {
            a = 10;
        } else {
            a = 20;
        }
    }
}
```

```java
// 部分字节码如下：
public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=2, args_size=1
         0: iconst_0			// 比较小的数都会用const来保存 -1~5 准备一个0放到操作数栈
         1: istore_1			// 放到局部变量表槽1=a中， 这两行等于a = 0 
         2: iload_1
         3: ifne          12
         6: bipush        10
         8: istore_1
         9: goto          15
        12: bipush        20
        14: istore_1
```

## 2.5 循环控制指令

其实循环控制还是前面介绍的那些指令，例如 while 循环：

```java
public class Demo3_4 {
    public static void main(String[] args) {
        int a = 0;
        while (a < 10) {
            a++;
        }
    }
}
```

字节码是：

```java
    0: iconst_0
    1: istore_1
    2: iload_1
    3: bipush 10
    5: if_icmpge 14
    8: iinc 1, 1
    11: goto 2
    14: return
```

再比如 do while 循环：

```java
public class Demo3_5 {
    public static void main(String[] args) {
        int a = 0;
        do {
            a++;
        } while (a < 10);
    }
}
```

字节码是：

```java
     0: iconst_0
     1: istore_1
     2: iinc 1, 1
     5: iload_1
     6: bipush 10
     8: if_icmplt 2
     11: return
```

最后再看看 for 循环：

```java
public class Demo3_6 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
        }
    }
}
```

字节码是：

```java
    0: iconst_0
    1: istore_1
    2: iload_1
    3: bipush 10
    5: if_icmpge 14
    8: iinc 1, 1
    11: goto 2
    14: return
```

> 比较 while 和 for 的字节码，你发现它们是一模一样的，殊途也能同归

## 2.6 练习-判断结果

```java
public class Demo3_6_1 {
    public static void main(String[] args) {
        int i = 0;
        int x = 0;
        while (i < 10) {
            x = x++;
            i++;
        }
        System.out.println(x); // 结果是 0
    }
}
```

```java
// 字节码指令如下：  
public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: iconst_0
         1: istore_1
         2: iconst_0
         3: istore_2
         4: goto          15
         7: iload_2							// 重点就是下面三行 一直在循环
         8: iinc          2, 1
        11: istore_2
        12: iinc          1, 1
        15: iload_1
        16: bipush        10
        18: if_icmplt     7
        21: getstatic     #16                 // Field java/lang/System.out:Ljava/io/PrintStream;
        24: iload_2
        25: invokevirtual #22                 // Method java/io/PrintStream.println:(I)V
        28: return

```

## 2.7 构造方法

### 2.7.1 \<cinit>()V

```java
public class Demo3 {
	static int i = 10;

	static {
		i = 20;
	}

	static {
		i = 30;
	}

	public static void main(String[] args) {
		System.out.println(i); //结果为30
	}
}
```

编译器会按从上至下的顺序，收集所有 static 静态代码块和静态成员赋值的代码，合并为一个特殊的方法`<cinit>()V` ：

```java
  static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=1, locals=0, args_size=0
         0: bipush        10
         2: putstatic     #10                 // Field i:I		// 进行static变量的赋值
         5: bipush        20
         7: putstatic     #10                 // Field i:I
        10: bipush        30
        12: putstatic     #10                 // Field i:I
        15: return
```

`<cinit>()V` 方法会在类加载的初始化阶段被调用。

### 2.7.2 \<init>()V

```java
public class Demo4 {
	private String a = "s1";

	{
		b = 20;
	}

	private int b = 10;

	{
		a = "s2";
	}

	public Demo4(String a, int b) {
		this.a = a;
		this.b = b;
	}

	public static void main(String[] args) {
		Demo4 d = new Demo4("s3", 30);
		System.out.println(d.a);
		System.out.println(d.b);
	}
}
```

编译器会按从上至下的顺序，收集所有 {} 代码块和成员变量赋值的代码，形成新的构造方法，但原始构造方法内的代码总是在后：

```java
  public com.linxuan.Demo01(java.lang.String, int);
    descriptor: (Ljava/lang/String;I)V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=3
         0: aload_0
         1: invokespecial #12                 // Method java/lang/Object."<init>":()V
         4: aload_0
         5: ldc           #15                 // String s1
         7: putfield      #17                 // Field a:Ljava/lang/String;
        10: aload_0
        11: bipush        20
        13: putfield      #19                 // Field b:I
        16: aload_0
        17: bipush        10
        19: putfield      #19                 // Field b:I
        22: aload_0
        23: ldc           #21                 // String s2
        25: putfield      #17                 // Field a:Ljava/lang/String;
        28: aload_0
        29: aload_1
        30: putfield      #17                 // Field a:Ljava/lang/String;
        33: aload_0
        34: iload_2
        35: putfield      #19                 // Field b:I
        38: return
      LineNumberTable:
        line 17: 0
        line 5: 4
        line 8: 10
        line 11: 16
        line 14: 22
        line 18: 28
        line 19: 33
        line 20: 38
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      39     0  this   Lcom/linxuan/Demo01;
            0      39     1     a   Ljava/lang/String;
            0      39     2     b   I
```

## 2.8 方法调用

```java
public class Demo01 {
    public Demo3_9() { }						// 构造方法  invokespecial指令
    
    private void test1() { }					// 私有方法  invokespecial指令
    
    private final void test2() { }				 // final方法	invokespecial指令
    
    public void test3() { }						// 普通成员方法 invokevirtual指令
    
    public static void test4() { }				// 静态方法   invokestatic指令
    
    public static void main(String[] args) {
        Demo01 demo01 = new Demo01();
        demo01.test1();
        demo01.test2();
        demo01.test3();
        demo01.test4();
        Demo01.test4();
    }
}
```

不同方法在调用时，对应的虚拟机指令有所区别

- 私有、构造、被final修饰的方法，在调用时都使用`invokespecial`指令
- 普通成员方法在调用时，使用`invokevirtual`指令。因为编译期间无法确定该方法的内容，只有在运行期间才能确定
- 静态方法在调用时使用`invokestatic`指令

```java
    0: new #2 					 // class class com/linxuan/Demo01
    3: dup
    4: invokespecial #3 		  // Method "<init>":()V
    7: astore_1
    8: aload_1
    9: invokespecial #4			 // Method test1:()V
    12: aload_1
    13: invokespecial #5 		 // Method test2:()V
    16: aload_1
    17: invokevirtual #6 		 // Method test3:()V
    20: aload_1
    21: pop
    22: invokestatic #7 		// Method test4:()V
    25: invokestatic #7 		// Method test4:()V
    28: return
```

- new 是创建【对象】，给对象分配堆内存，执行成功会将【对象引用】压入操作数栈
- dup 是赋值操作数栈栈顶的内容，本例即为【对象引用】，为什么需要两份引用呢，一个是要配合 invokespecial 调用该对象的构造方法 `"<init>":()V` （会消耗掉栈顶一个引用），另一个要配合 astore_1 赋值给局部变量
- 最终方法（final），私有方法（private），构造方法都是由 invokespecial 指令来调用，属于静态绑定
- 普通成员方法是由 invokevirtual 调用，属于动态绑定，即支持多态
- 成员方法与静态方法调用的另一个区别是，执行方法前是否需要【对象引用】
- 比较有意思的是 d.test4(); 是通过【对象引用】调用一个静态方法，可以看到在调用invokestatic 之前执行了 pop 指令，把【对象引用】从操作数栈弹掉了
- 还有一个执行 invokespecial 的情况是通过 super 调用父类方法

## 2.9 多态原理

<!--这里少些了很多-->

因为普通成员方法需要在运行时才能确定具体的内容，所以虚拟机需要调用**invokevirtual**指令

在执行invokevirtual指令时，经历了以下几个步骤

- 先通过栈帧中对象的引用找到对象
- 分析对象头，找到对象实际的Class
- Class结构中有**vtable**
- 查询vtable找到方法的具体地址
- 执行方法的字节码

## 2.10 异常处理

### 2.10.1 try-catch

```java
public class Demo01 {
	public static void main(String[] args) {
		int i = 0;
		try {
			i = 10;
		}catch (Exception e) {
			i = 20;
		}
	}
}
```

```java
// 编译后部分字节码文件：
public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=3, args_size=1
         0: iconst_0
         1: istore_1
         2: bipush        10
         4: istore_1
         5: goto          12
         8: astore_2
         9: bipush        20
        11: istore_1
        12: return
      Exception table:		// 异常表 会检测2~5行的异常(不包含5行) 有异常会跳转至8行
         from    to  target type
             2     5     8   Class java/lang/Exception
      LineNumberTable:
        line 5: 0
        line 8: 2
        line 9: 5
        line 10: 9
        line 12: 12
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      13     0  args   [Ljava/lang/String;
            2      11     1     i   I
            9       3     2     e   Ljava/lang/Exception;
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 8
          locals = [ class "[Ljava/lang/String;", int ]
          stack = [ class java/lang/Exception ]
        frame_type = 3 /* same */
```

- 可以看到多出来一个 Exception table 的结构，[from, to) 是**前闭后开**（也就是检测2~4行）的检测范围，一旦这个范围内的字节码执行出现异常，则通过 type 匹配异常类型，如果一致，进入 target 所指示行号
- 8行的字节码指令 astore_2 是将异常对象引用存入局部变量表的2号位置（为e）

### 2.10.2 多个single-catch

```java
public class Demo01 {
	public static void main(String[] args) {
		int i = 0;

		try {
			i = 10;
		} catch (ArithmeticException e) {
			i = 20;
		} catch (NullPointerException e) {
			i = 30;
		} catch (Exception e) {
			i = 40;
		}
	}
}
```

```java
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=3, args_size=1
         0: iconst_0
         1: istore_1
         2: bipush        10
         4: istore_1
         5: goto          26
         8: astore_2
         9: bipush        20
        11: istore_1
        12: goto          26
        15: astore_2
        16: bipush        30
        18: istore_1
        19: goto          26
        22: astore_2
        23: bipush        40
        25: istore_1
        26: return
      Exception table:
         from    to  target type
             2     5     8   Class java/lang/ArithmeticException
             2     5    15   Class java/lang/NullPointerException
             2     5    22   Class java/lang/Exception
      LineNumberTable:...
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      27     0  args   [Ljava/lang/String;
            2      25     1     i   I
            9       3     2     e   Ljava/lang/ArithmeticException;
           16       3     2     e   Ljava/lang/NullPointerException;
           23       3     2     e   Ljava/lang/Exception;
```

- 因为异常出现时，只能进入 Exception table 中一个分支，所以局部变量表 slot 2 位置被共用

### 2.10.3 multi-catch 的情况

```java
public class Demo01 {
	public static void main(String[] args) {
		try {
			Method test = Demo01.class.getMethod("test");
			test.invoke(null);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public static void test() {
		System.out.println("ok");
	}
}
```

```java
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=3, locals=2, args_size=1
         0: ldc           #1                  // class com/linxuan/demo01/Demo01
         2: ldc           #16                 // String test
         4: iconst_0
         5: anewarray     #18                 // class java/lang/Class
         8: invokevirtual #20                 // Method java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        11: astore_1
        12: aload_1
        13: aconst_null
        14: iconst_0
        15: anewarray     #3                  // class java/lang/Object
        18: invokevirtual #24                 // Method java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        21: pop
        22: goto          30
        25: astore_1
        26: aload_1
        27: invokevirtual #30                 // Method java/lang/ReflectiveOperationException.printStackTrace:()V
        30: return
      Exception table:
         from    to  target type
             0    22    25   Class java/lang/NoSuchMethodException
             0    22    25   Class java/lang/IllegalAccessException
             0    22    25   Class java/lang/reflect/InvocationTargetException
      LineNumberTable:...
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      31     0  args   [Ljava/lang/String;
           12      10     1  test   Ljava/lang/reflect/Method;
           26       4     1     e   Ljava/lang/ReflectiveOperationException;
```



### 2.10.4 finally

```java
public class Demo01 {
	public static void main(String[] args) {
		int i = 0;
		try {
			i = 10;
		} catch (Exception e) {
			i = 20;
		} finally {
			i = 30;
		}
	}
}
```

```java
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=4, args_size=1
         0: iconst_0
         1: istore_1
         2: bipush        10			// try代码块
         4: istore_1
         5: goto          24
         8: astore_2					// catch代码块
         9: bipush        20			
        11: istore_1
        12: bipush        30			// 执行finally代码块
        14: istore_1
        15: goto          27
        18: astore_3					// catch其余的异常类型
        19: bipush        30			// 执行finally代码块
        21: istore_1
        22: aload_3
        23: athrow
        24: bipush        30
        26: istore_1
        27: return
      Exception table:
         from    to  target type
             2     5     8   Class java/lang/Exception
             2    12    18   any
      LineNumberTable:...
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      28     0  args   [Ljava/lang/String;
            2      26     1     i   I
            9       3     2     e   Ljava/lang/Exception;
```

可以看到 ﬁnally 中的代码被复制了 3 份，分别放入 try 流程，catch 流程以及 catch剩余的异常类型流程

虽然从字节码指令看来，每个块中都有finally块，但是finally块中的代码只会被执行一次

### 2.10.5 finally中的return

```java
public class Demo3 {
	public static void main(String[] args) {
		int i = Demo3.test();
        //结果为20
		System.out.println(i);
	}

	public static int test() {
		int i;
		try {
			i = 10;
			return i;
		} finally {
			i = 20;
			return i;
		}
	}
}
```

```java
Code:
     stack=1, locals=3, args_size=0
        0: bipush        10
        2: istore_0
        3: iload_0
        4: istore_1 				 //暂存返回值
        5: bipush        20
        7: istore_0
        8: iload_0
        9: ireturn					//ireturn会返回操作数栈顶的整型值20
       10: astore_2					//如果出现异常，还是会执行finally块中的内容，没有抛出异常
       11: bipush        20
       13: istore_0
       14: iload_0
       15: ireturn					//这里没有athrow了
     Exception table:
        from    to  target type
            0     5    10   anyCopy
```

- 由于 ﬁnally 中的 ireturn 被插入了所有可能的流程，因此返回结果肯定以ﬁnally的为准
- 至于字节码中第 2 行，似乎没啥用，且留个伏笔，看下个例子
- 跟上例中的 ﬁnally 相比，发现没有 athrow 了，这告诉我们：如果在 ﬁnally 中出现了 return，会吞掉异常
- 所以不要在finally中进行返回操作

例如下面的操作会吞掉异常：

```java
public class Demo3 {
   public static void main(String[] args) {
      int i = Demo3.test();
      //最终结果为20
      System.out.println(i);
   }

   public static int test() {
      int i;
      try {
         i = 10;
         i = i/0;			//这里应该会抛出异常 但是并没有
         return i;
      } finally {
         i = 20;
         return i;
      }
   }
}
```

会发现打印结果为20，并未抛出异常

### 2.10.6 finally不带return

```java
public class Demo4 {
	public static void main(String[] args) {
		int i = Demo4.test();
		System.out.println(i);
	}

	public static int test() {
		int i = 10;
		try {
			return i;
		} finally {
			i = 20;
		}
	}
}
```

```java
Code:
     stack=1, locals=3, args_size=0
        0: bipush        10
        2: istore_0 						// 赋值给i 10
        3: iload_0							// 加载到操作数栈顶
        4: istore_1 						// 加载到局部变量表的1号位置
        5: bipush        20
        7: istore_0 						// 赋值给i 20
        8: iload_1							// 加载局部变量表1号位置的数10到操作数栈
        9: ireturn 							// 返回操作数栈顶元素 10
       10: astore_2
       11: bipush        20
       13: istore_0
       14: aload_2 							// 加载异常
       15: athrow 							// 抛出异常
     Exception table:
        from    to  target type
            3     5    10   anyCopy
```

## 2.8 Synchronized

```java
public class Demo01 {
	public static void main(String[] args) {
		Object lock = new Object();
		synchronized (lock) {
			System.out.println("ok");
		}
	}
}
```

```java
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: new           #3                  // class java/lang/Object
         3: dup
         4: invokespecial #8                  // Method java/lang/Object."<init>":()V
         7: astore_1
         8: aload_1
         9: dup
        10: astore_2
        11: monitorenter
        12: getstatic     #16                 // Field java/lang/System.out:Ljava/io/PrintStream;   
        15: ldc           #22                 // String ok
        17: invokevirtual #24                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        20: aload_2
        21: monitorexit
        22: goto          28
        25: aload_2
        26: monitorexit
        27: athrow
        28: return
      Exception table:
         from    to  target type
            12    22    25   any
            25    27    25   any
      LineNumberTable:...
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      29     0  args   [Ljava/lang/String;
            8      21     1  lock   Ljava/lang/Object;
```

> 方法级别的 synchronized 不会在字节码指令中有所体现
