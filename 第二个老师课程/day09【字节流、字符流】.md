# 第一章 IO概述

## 1.1 什么是IO

<!--P358-->

java中I/O操作主要是指使用`java.io`包下的内容，进行输入、输出操作。**输入**也叫做**读取**数据，**输出**也叫做**写出**数据。

## 1.2 IO的分类

根据数据的流向分为：**输入流和输出流**。

* **输入流**：把数据从其他设备上读取到内存中的流。
* **输出流**：把数据从内存中写出到其它设备上的流。

格局数据的类型分为：**字节流和字符流**。

## 1.3 IO的流向说明图解

Input.read();

output.write();

![](D:\Java\笔记\图片\day09【字节流、字符流】\1.png)

## 1.4 顶级父类们

|        | 输入流                       | 输出流                         |
| ------ | ---------------------------- | ------------------------------ |
| 字节流 | 字节输入流<br>**InputSteam** | 字节输出流<br>**OutputStream** |
| 字符流 | 字符输入流<br>**Reader**     | 字符输出流<br>**Writer**       |



# 第二章 字节流

## 2.1 一切皆为字节

<!--P359-->

一切文件数据（文本、图片、视频等）在存储时，都是以二进制数字的形式保存，都是一个一个的字节，那么传输时一样如此。所以，字节流可以传输任意文件数据。在操作流的时候，我们要时刻明确，无论使用什么样的流对象，底层传输的始终为二进制数据。

## 2.2 字节输出流【OutputStream】

<!--P360-->

`java.io.OutputStream`抽象类是表示字节输出流的所有类的超类，将指定的字节信息写出到目的地。它定义了字节输出流的基本共性功能方法。

抽象类无法创建对象，我们要使用他的子类来创建对象。

定义了一些子类共性的成员方法：

> 小贴士：
>
> close方法，当完成流的操作时，必须调用此方法，释放系统资源。

## 2.3 FileOutputStream类

`OutputStream`有很多子类，我们是从最简单的一个子类开始。

`java.io.FileOutputStream`类是文件输出流，用于将数据写出文件，也就是把内存中的数据写入到硬盘的文件中。

### 构造方法

* `FileOutputStream(String name)`：创建一个向具有指定名称的文件中写入数据的输出文件流。

* `FileOutputStream(File file)`：创建一个向指定 File 对象表示的文件中写入数据的文件输出流。

  参数：写入数据的目的地

  * `String name`: 目的地是一个文件的路径
  * `File file`：目的地是一个文件

  构造方法作用：

  1. 创建一个`FileOutputStream`对象。
  2. 会根据构造方法中传递的文件/文件路径，创建一个空的文件。
  3. 会把FileOutputStream对象指向创建好的文件。

### 写出字节数据

<!--P361-->

```java
import java.io.FileOutputStream;
import java.io.IOException;

/*

* */
// 字节输出流
public class Demo01OutputStream {
    public static void main(String[] args) throws IOException {
        // 1.创建一个FileOutputStream对象，构造方法中传递写入数据的目的地
        // Unhandled exception: java.io.FileNotFoundException
        FileOutputStream fos = new FileOutputStream("D:\\B站视频\\79312032\\361\\a.txt");
        // 2.调用FileOutputStream对象中的方法write，把数据写入到文件中
        // public abstract void write(int b):将指定的字节输出流。
        // Unhandled exception: java.io.IOException
        fos.write(21);
        // 3.释放资源
        // Unhandled exception: java.io.IOException
        fos.close();
        // 这些异常有继承关系，所以直接抛出IOException异常处理
    }
}
```

<!--P363-->

```java
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Demo02OutputStream {
    public static void main(String[] args) throws IOException {
        // 1.创建FIleOutputStream对象，构造方法中绑定要写入数据的目的地
        FileOutputStream fos = new FileOutputStream(new File("D:\\B站视频\\79312032\\361\\b.txt"));
        // 2.调用FIleOutputStream对象中的方法write，将数据写入到文件中
        fos.write(49);
        fos.write(48);
        fos.write(48);

        /*
                public void write(byte[] b) :将b.length字节从指定的字节数组写入此输出流。
                一次写多个字节：
                    如果写的第一个字节是整数（0-127），那么显示的时候会查询ASCII表
                    如果写的第一个字节是负数，则第一个字节和第二个字节组成一个中文显示，查询系统默认码表（GBK）
         */
        // byte[] bytes = {65, 66, 67, 68, 69}; // ABCDE
        // byte[] bytes = {-65, -66, -67, 68, 69}; // 烤紻E
        // fos.write(bytes);

        /*
                public void write(byte[] b, int off, int len) :把字节数组一部分写入到文件中
                从指定的字节数组写入len字节，从偏移量off开始输出到此输出流。
                    int off:数组的开始索引
                    ing len:写几个字节
         */
        // byte[] bytes = {65, 66, 67, 68, 69}; // ABCDE
        // fos.write(bytes, 1, 2); // BC

        /*
                写入字符的方法：可以使用String类中的方法把字符串转换为字节数组
                    byte[] getBytes():把字符串转换为字符数组
                GBK中中文字符是两个字节，而UTF-8是三个字节
         */
        byte[] bytes = "你好".getBytes();
        // System.out.println(Arrays.toString(bytes));
        fos.write(bytes);

        fos.close();
    }
}
```

### 数据追加续写

<!--P364-->

每当程序重新运行的时候，都会清空目标文件中的数据。

* `public FIleOutputStream(File file, boolean append)` :创建文件输出流以写入由指定的FIle对象表示的文件。
* `public FIleOutputStream(String name, boolean append)` :创建文件输出流以指定的名称写入文件。

这两个构造方法，参数中有一个boolean类型的值，`true`表示追加数据，`false`表示清空原有数据。

```java
import java.io.FileOutputStream;
import java.io.IOException;

/*
    public FIleOutputStream(String name, boolean append) :创建文件输出流以指定的名称写入文件。
    换行的话，也是转义字符
    Windows：\r\n
    linux:/r
    mac:/n
 */
// 追加读写
public class Demo03OutputStream {
    public static void main(String[] args) throws IOException {
        // public FIleOutputStream(String name, boolean append) :创建文件输出流以指定的名称写入文件。
        FileOutputStream fos = new FileOutputStream("D:\\B站视频\\79312032\\364\\a.txt", true);
        // 写入字符的方法：可以使用String类中的方法把字符串转换为字节数组
        fos.write("nihao".getBytes());
        fos.write("\r\n".getBytes());
        fos.close();
    }
}
```

## 2.4 字节输入流【InputStream】

<!--P365-->

`java.io.InputStream`抽象类是表示字节输入流的所有类的超类，可以读取字节信息到内存中。它定义 了字节输入流的基本共性功能方法。

* `public void close()` :关闭此输入流并是方法与此流相关联的任何系统资源。
* `public abstract int read()` :从输入流读取数据的下一个字节。
* `public int read(byte[] b)` :从输入流中读取一些字节数，并把它们存储到字节数组b中。

> 小贴士：
>
> close方法，当完成流的操作时，必须调用此方法，释放系统资源。

## 2.5 FileInputStream类

`java.io.FileInputStream`类是文件输入流，从文件中读取字节。

### 构造方法

* `FileInputStream(File file)` :通过打开与实际文件的连接来创建一个`FileInputStream`，该文件由文件系统中的File对象file命名。
* `FileInputStream(String name)` :通过打开与实际文件的连接来创建一个`FileInputStream`，该文件由文件系统中的路径名name命名。

当你创建一个流对象时，必须传入一个文件路径。该路径下，如果没有该文件，会抛出`FileNotFoundException`。

### 读取字节数据

<!--P366-->

1. 读取字节：`read`方法，每次可以读取一个字节的数据，提升为`int`类型，读取到文件末尾，返回`-1`。

   代码演示：

```java
import java.io.FileInputStream;
import java.io.IOException;

/*
    read()方法指针会自动转向下一个
    最后没有的时候会返回-1
 */
public class Demo01InputStream {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("09_IOAndProperties\\a.txt");

/*        int len = fis.read();
        System.out.println(len);

        len = fis.read();
        System.out.println(len);

        len = fis.read();
        System.out.println(len);

        len = fis.read();
        System.out.println(len);*/

        int len = 0;
        while ((len = fis.read()) != -1) {
            // char类型，将int类型数字，强制转换为char类型
            System.out.println((char)len);
        }

        fis.close();
    }
}
```

<!--P368-->

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/*
    字节输入流一次读取多个字节的方法：
        int read(byte[] b) 从输入流中读取一定数量的字节，并将其存储在缓冲区数组b中
    明确两件事情：
        1.方法的参数byte[]作用：
            起到缓冲作用，存储每次读取到的多个字节
            数组的长度定义的话，要定义为1024或者他的整数倍
        2.方法的返回值int是：
            每次读取的字节个数
 */
public class Demo02InputStream {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("09_IOAndProperties\\a.txt");
/*        byte[] bytes = new byte[2];

        // 方法的返回值int是：每次读取的字节个数
        int len = fis.read(bytes);
        System.out.println(len);

        // System.out.println(Arrays.toString(bytes));
        System.out.println(new String(bytes));

        len = fis.read(bytes);
        System.out.println(len);
        System.out.println(new String(bytes));

        len = fis.read(bytes);
        System.out.println(len);
        System.out.println(new String(bytes));

        len = fis.read(bytes);
        System.out.println(len);
        System.out.println(new String(bytes));*/

        // 使用while循环
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = fis.read(bytes)) != -1) {
            // String(byte[] bytes, int offset, int length)
            // 把字节数组的一部分转换为字符串 offset：数组的开始索引 length：转换的字节个数
            System.out.println(new String(bytes, 0, len));
        }

        fis.close();
    }
}
```

## 2.6 字节流联系：图片复制

### 案例实现

<!--P369-->

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
    文件复制练习：一读一写

    明确：
        数据源：D:\\图片\\1.jpg
        数据目的地：D:\\Java

    文件赋值步骤：
        1.创建一个字节输入流对象，构造方法中绑定要读取的数据源
        2.创建一个字节输出流对象，构造方法中绑定写入的目的地
        3.使用字节输入流对象中的方法read读取文件
        4.使用字节输出流中的方法write，把读取到的字节写入到目的地文件中
        5.释放资源
 */
public class Demo01CopyFile {
    public static void main(String[] args) throws IOException {
        // 测试时间
        long s = System.currentTimeMillis();

        // 1.创建一个字节输入流对象，构造方法中绑定要读取的数据源
        FileInputStream fis = new FileInputStream("D:\\图片\\动漫\\清纯诱惑\\18.jpg");
        // 2.创建一个字节输出流对象，构造方法中绑定写入的目的地
        FileOutputStream fos = new FileOutputStream("D:\\Java\\18.jpg");

        // 3.使用字节输入流对象中的方法read读取文件
        // 4.使用字节输出流中的方法write，把读取到的字节写入到目的地文件中
/*        int len = 0;
        while((len = fis.read()) != -1) {
            fos.write(len);
        }*/

        // 使用数组缓冲读取多个字节，写入多个字节，速度更快
        byte[] bytes = new byte[1024];
        // 3.使用字节输入流对象中的方法read读取文件
        // 4.使用字节输出流中的方法write，把读取到的字节写入到目的地文件中
        int len = 0;
        while((len = fis.read(bytes)) != -1) {
            fos.write(bytes);
        }

        // 5.释放资源
        // 先关闭写的流，因为写完了，那么肯定已经读取完毕了。
        fos.close();
        fis.close();

        // 测试时间
        long end = System.currentTimeMillis();
        // 文件大小不同，耗时不同
        System.out.println("耗时为：" + (end - s) + "毫秒");

    }
}
```

# 第三章 字符流

<!--P370-->

使用字节流读取文本文件的时候，会有一些小问题，遇到中文字符，不会显示完整的字符，因为一个中文字符可能占用多个字节存储。所以java提供一些字符流类，以字符为单位读写数据，专门处理文本文件

## 3.1 字符输入流【Reader】

<!--P371 1.3-->

`java.io.Reader`抽象类是表示用于读取字符流的所有类的超类，可以读取字符信息到内存中。它定义了字符输入流的基本共性方法。

* `public void close()` :关闭此流并释放与此流相关联的任何系统资源。
* `public int read()` :从输入流中读取一个字符。
* `public int read(char[] cbuf)` :从输入流中读取一些字符，并将他们存储到字符数组cbuf中。

## 3.2 FIleReader类

`java.io.FileReader`类是读取字符文件的便利类。构造时使用系统默认的字符编码和默认字节缓冲区。

> 小贴士：
>
> 1. 字符编码：字节与字符的对应规则。Windows系统的中文编码默认是GBK编码表。idea中是UTF-8。

### 读取字符数据

```java
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/*
    java.io.Reader：字符输入流，是字符输入流的最顶层的父类，定义了一些共性的成员方法，是一个抽象类。
    共性的成员方法：
        - public void close() :关闭此流并释放与此流相关联的任何系统资源。
        - public int read() :从输入流中读取一个字符。
        - public int read(char[] cbuf) :从输入流中读取一些字符，并将他们存储到字符数组cbuf中。

    java.io.FileReader extends InputStreamReader extends Reader
    FileReader:文件字符输入流
    作用：把硬盘文件中的数据以字符的方式读取到内存中

    构造方法：
        FileReader（String fileName）
        FileReader（File file）
        参数：读取文件的数据源
            String fileName：文件的路径
            File file：一个文件
        FileReader构造方法的作用：
            1.创建一个FileReader对象
            2.会把FileReader对象指向要读取的文件
 */
public class Demo01Reader {
    public static void main(String[] args) throws IOException {
        // 首先创建文件
        /*FileOutputStream fos = new FileOutputStream("09_IOAndProperties\\c.txt");
        byte[] bytes = "你好###abc123".getBytes();
        fos.write(bytes);
        fos.close();*/

        // 字符read
        FileReader fr = new FileReader("09_IOAndProperties\\c.txt");
/*        int len = 0;
        while((len = fr.read()) != -1) {
            System.out.print((char)len); // 你好###abc123
        }*/

        // int read(char[] cbuf)一次读取多个字符，将字符读入数组
        char[] cs = new char[1024];// 存储读取到的多个字符
        int len = 0;// 记录的是每次读取的有效字符个数
        while((len = fr.read(cs)) != -1) {
            // String 类的构造方法
            // String(char[] value) 把字符数组转换为字符串
            // String(char[] value, int offset, int count) 把字符数组的一部分转换为字符串
            System.out.println(new String(cs, 0, len));
        }

        fr.close();
    }
}
```

## 3.3 字符输出流【Writer】

<!--P373-->

`java.io.Writer`抽象类是表示用于写出字符流的所有类的超类，将指定的字符信息写入到目的地。它定义了字符输出流的基本共性功能方法。

* `void Write(int c)` :写入单个字符。
* `void Write(char[] cbuf)` :写入字符数组。
* `abstract void Write(char[] cbuf, int off, int len)` :写入字符数组的某一部分，off数组的开始所以，len写的字符个数。
* `void Write(String str)` :写入字符串。
* `void Write(String str, int off, int len)` :写入字符串的某一部分，off字符串的开始索引，len写的字符个数。
* `void flush()` :刷新该流的缓冲。
* `void close()` :关闭此流，但是要先刷新它。

## 3.4 FileWriter类

`java.io.FileWriter`类是写出字符到文件的便利类。构造时使用系统默认的字符编码和默认字节缓冲区。

### 构造方法

* `FileWriter(File file)`：根据给定的 File 对象构造一个 FileWriter 对象。
* `FileWriter(String fileName)`：根据给定的文件名构造一个 FileWriter 对象。

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
    - FileWriter(File file)：根据给定的 File 对象构造一个 FileWriter 对象。
    - FileWriter(String fileName)：根据给定的文件名构造一个 FileWriter 对象。

 */
public class Demo01Writer {
    public static void main(String[] args) throws IOException {
        // 使用File对象创建流对象
        File file = new File("09_IOAndProperties\\b.txt");
        FileWriter fw0 = new FileWriter(file);

        // 使用文件名称创建流对象
        FileWriter fw1 = new FileWriter("09_IOAndProperties\\b.txt");
    }
}
```

### 基本写出数据

<!--P374-->

```java
import java.io.FileWriter;
import java.io.IOException;

/*
    字符输出流的使用步骤：
        1.创建FileWriter对象，构造方法中绑定要写入数据的目的地
        2.使用FileWriter中的方法Write，把数据写入到内存缓冲区中（字符转换为字节的过程）
        3.使用FileWriter中的方法flush，把内存缓冲区的数据，刷新到文件中
        4.释放资源（会先把内存缓冲区的数据刷新到文件中）
 */
public class Demo02Writer {
    public static void main(String[] args) throws IOException {
        // 1.创建FileWriter对象，构造方法中绑定要写入数据的目的地
        FileWriter fw = new FileWriter("09_IOAndProperties\\d.txt");
        // 2.使用FileWriter中的方法Write，把数据写入到内存缓冲区中（字符转换为字节的过程）
        fw.write(97);
        // 3.使用FileWriter中的方法flush，把内存缓冲区的数据，刷新到文件中
        // fw.flush();

        // 4.释放资源（会先把内存缓冲区的数据刷新到文件中）
        fw.close();
    }
}
```

### 关闭和刷新

<!--P375-->

因为内置缓冲区的原因，如果不关闭输出流，是无法写出字符到文件中的。但是关闭的流对象，是无法继续写出字符的。如果我们既想写出数据，又想继续使用流，就需要使用`flush`方法了。

* `flush`：刷新缓冲区，流对象可以继续使用。
* `close`：先刷新缓冲区，然后通知系统释放资源。流对象不可以再被使用了。

```java
public class Demo03CloseAndFlush {
    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("09_IOAndProperties\\e.txt");
        fw.write(97);
        fw.flush();
        // 刷新之后 可以继续使用
        fw.write(98);
        fw.flush();

        fw.close();
        // 关闭之后 java.io.IOException: Stream closed
        // fw.write(99);
    }
}
```

### 写出其他数据

<!--P376-->

```java
/*
    写入字符数组的其他方法
    - void Write(char[] cbuf) :写入字符数组。
    - abstract void Write(char[] cbuf, int off, int len) :写入字符数组的某一部分，off数组的开始所以，len写的字符个数。
    - void Write(String str) :写入字符串。
    - void Write(String str, int off, int len) :写入字符串的某一部分，off字符串的开始索引，len写的字符个数。
 */
public class Demo04Writer {
    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("09_IOAndProperties\\f.txt");

        char[] cs = {'a', 'b', 'c', 'd'};
        // void Write(char[] cbuf) :写入字符数组。
        fw.write(cs);

        // abstract void Write(char[] cbuf, int off, int len) :写入字符数组的某一部分，off数组的开始所以，len写的字符个数。
        fw.write(cs, 0, 2);

        // void Write(String str) :写入字符串。
        fw.write("林炫");

        // void Write(String str, int off, int len) :写入字符串的某一部分，off字符串的开始索引，len写的字符个数。
        fw.write("林炫你好呀", 2, 3);

        fw.close();
    }
}
```

### 续写和换行

<!--P377-->

* 续写：追加写，使用两个参数的构造方法

  `FileWriter(String fileName, boolean append)` 

  `FileWriter(File file, boolean append)`

* 换行：换行符号

```java
import java.io.FileWriter;
import java.io.IOException;

public class Demo05Writer {
    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("09_IOAndProperties\\g.txt");

        for (int i = 0; i < 10; i++) {
            fw.write("hellow world" + i + "\r\n");
        }

        fw.close();
    }
}
```

# 第四章 IO异常的处理

<!--P378-->

之前的异常一直是抛出处理，实际开发中并不能这么处理，建议使用`try...catch...finally`代码块，处理异常部分。

代码部分：

```java
import java.io.FileWriter;
import java.io.IOException;

public class Demo01TryCatch {
    public static void main(String[] args) {
        // 提高变量fw的作用域，让finally可以使用
        // 变量在定义的时候可以没有值，但是使用的时候必须有值，如果new对象失败会没有值，所以需要提前赋值
        FileWriter fw = null;

        try {
            // 可能会产生异常的代码
            fw = new FileWriter("09_IOAndProperties\\h.txt");

            for (int i = 0; i < 10; i++) {
                fw.write("hellow world" + i + "\r\n");
            }

        } catch(IOException e) {
            // 异常的处理逻辑
            System.out.println(e);

        } finally {
            // 一定会使用的代码
            // 创建对象失败了，那么fw的默认值就是null，null是不能调用方法的，会抛出异常，所以需要提前判断一下
            if (fw != null) {
                try {
                    // fw.close方法声明抛出了IOException异常对象，所以需要处理
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

### JDK7的处理(扩展知识点了解内容)

<!--P379 1.4-->

JDK7的新特性：

​	在try后边加一个()，在括号中定义流对象。

​	try代码执行完毕后，流对象可以释放掉，不用写finally。

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo02JDK7 {
    public static void main(String[] args) {
        try(        // 1.创建一个字节输入流对象，构造方法中绑定要读取的数据源
                    FileInputStream fis = new FileInputStream("D:\\图片\\动漫\\清纯诱惑\\18.jpg");
                    // 2.创建一个字节输出流对象，构造方法中绑定写入的目的地
                    FileOutputStream fos = new FileOutputStream("D:\\Java\\18.jpg");) {
            // 3.使用字节输入流对象中的方法read读取文件
            // 4.使用字节输出流中的方法write，把读取到的字节写入到目的地文件中
            int len = 0;
            while((len = fis.read()) != -1) {
                fos.write(len);
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
```

JDK9的新特性：

* try前面定义流对象。

* try后面的()中可以直接引入遛对象的名称(变量名)。

* try代码执行完毕后，流对象可以释放掉，不用写finally。

* 定义流对象的时候有异常要抛出处理。

  格式：

  ```
  	A a = new A();
  	B b = new B();
  	
  	try(a；b) {
  		可能会产生异常的代码
  	} catch(异常类变量 变量名) {
  		异常的处理逻辑
  	}
  ```

```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo03JDK9 {
    public static void main(String[] args) throws FileNotFoundException {
        // 1.创建一个字节输入流对象，构造方法中绑定要读取的数据源
        FileInputStream fis = new FileInputStream("D:\\图片\\动漫\\清纯诱惑\\18.jpg");
        // 2.创建一个字节输出流对象，构造方法中绑定写入的目的地
        FileOutputStream fos = new FileOutputStream("D:\\Java\\19.jpg");

        try (fis; fos) {
            // 3.使用字节输入流对象中的方法read读取文件
            // 4.使用字节输出流中的方法write，把读取到的字节写入到目的地文件中
            int len = 0;
            while((len = fis.read()) != -1) {
                fos.write(len);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
```

# 第五章 属性集

## 5.1 概述

<!--P380-->

<!--Properties 所有物; 财产; 财物; 不动产; 房地产; 房屋及院落; 庄园;-->

<!--属性property-->

`java.util.Properties`继承于`Hashtable`，来表示一个持久的属性集。它使用键值结构存储数据，每个键与其对应值都是一个字符串。该类也被许多java类使用，比如获取系统属性时，`System.getProperties`方法就是返回一个`Properties`对象。

`java.util.Proterties`集合 extends `HashTable<k, v>` implements `Map<k, v>`

`Properties`类表示了一个持久的属性集，`Properties`可保存在流中或从流中加载。

`Properties`集合是一个唯一和IO流相结合的集合

* 可以使用`Properties`集合中的方法`store`，把集合中的临时数据，持久化写入到硬盘中存储。
* 可以使用`Properties`集合中的方法`load`，把硬盘中保存的文件(键值对)，读取到集合中使用。

属性列表中每个键与其对应值都是一个字符串。

`Properties`集合是一个双列集合，key和value默认都是字符串。

## 5.2 Properties类

### 构造方法

* `public Properties()`: 创建一个空的属性列表。

### 基本的存储方法

* `public Object setProperty(String key, String value)`: 保存一对属性，等于调用Hashtable的方法put.
* `public String getProperty(String key)`: 使用此属性列表中指定的键搜索属性值。通过key找到value值，此方法相当于Map集合中的get(key)方法。
* `public Set<String> stringPropertyNames()`: 所有键的名称的集合。返回此属性列表中的键集，其中该键及其对应值是字符串，此方法相当于Map集合中的keySet方法。

```java
import java.util.Properties;
import java.util.Set;

public class Demo01Properties {
    public static void main(String[] args) {
        // 创建Properties集合对象
        Properties prop = new Properties();
        // 使用setProperty往集合中添加数据
        prop.setProperty("迪丽热巴", "168");
        prop.setProperty("古力娜扎", "165");
        prop.setProperty("稀里哗啦", "169");

        // 使用stringPropertyNames把Properties集合中的键取出，存储到一个set集合中
        Set<String> set = prop.stringPropertyNames();
        
        // 遍历Set集合，取出Properties集合中的每一个键
        for (String key : set) {
            // 使用getProperty方法通过key获取value
            String value = prop.getProperty(key);
            System.out.println(key + " = " + value);

        }
    }
}
```

#### store方法

<!--P381-->

可以使用`Properties`集合中的方法`store`，把集合中的临时数据，持久化写入到硬盘中存储。

`void store(OutputStream out， String comments)` 字节输入流，不能使用中文

`void store(Writer writer, String comments)` 字符输入流，可以使用中文

参数：

* `OutputStream out`：字符输出流，不能写入中文。

* `Writer writer`：字符输出流，可以写中文

* `String comments`：注释，用来解释说明保存的文件是做什么用的，在保存的文件中的第一行会显示。

  不能使用中文，会乱码，因为计算机默认的是Unicode编码

  一般使用“ ”空字符串

使用步骤：

1. 创建一个`Properties`集合对象，添加数据。
2. 创建字节输出流/字符输出流对象，构造方法中绑定要输出的目的地。
3. 使用`Properties`集合中的方法`store`，把集合中的临时数据，持久化写入到硬盘中存储。
4. 释放资源。

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Demo02Properties {
    public static void main(String[] args) throws IOException {
        // 1.创建Properties集合对象,添加数据
        Properties prop = new Properties();
        prop.setProperty("迪丽热巴", "168");
        prop.setProperty("古力娜扎", "165");
        prop.setProperty("稀里哗啦", "169");

        // 可以使用中文
/*        // 2. 创建字节输出流/字符输出流对象，构造方法中绑定要输出的目的地。
        FileWriter fw = new FileWriter("09_IOAndProperties\\prop.txt");

        // 3. 使用Properties集合中的方法store，把集合中的临时数据，持久化写入到硬盘中存储。
        prop.store(fw, "save data");

        // 4. 释放资源。
        fw.close();*/

        // 创建字节输出流对象，不用释放资源，因为匿名，所以直接会释放资源的
        // 因为是字节输出流，并且不能使用中文，可是之前有中文，所以会出现乱码
        prop.store(new FileOutputStream("09_IOAndProperties\\prop2.txt"), " ");
    }
}
```
#### load方法

<!--P382-->

可以使用`Properties`集合中的方法`load`，把硬盘中保存的文件(键值对)，读取到集合中使用。

`void load(InputStream inStream)` 字节输入流，不能读取含有中文键值对的文件

`void load(Reader reader)` 字符输入流，可以读取含有中文键值对的文件

使用步骤：

1. 创建`Properties`集合对象
2. 使用`Properties`集合中的方法`load`读取保存键值对的文件
3. 遍历`Properties`集合

注意：

* 存储键值对的文件中，键与值默认的连接符号可以使用=，空格（其他符号）。
* 存储键值对的文件中，可以使用#进行注释，被注释的键值对不会再被读取。
* 存储键值对的文件中，键与值默认都是字符串，不用再加引号

```java
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class Demo03Properties {
    public static void main(String[] args) throws IOException {
        // 创建Properties集合对象
        Properties prop = new Properties();
        // 使用Properties集合中的方法load读取保存键值对的文件
        prop.load(new FileReader("09_IOAndProperties\\prop.txt"));
        // 遍历Properties集合
        Set<String> set = prop.stringPropertyNames();
        for (String key : set) {
            String value = prop.getProperty(key);
            System.out.println(key + "=" + value);
        }
    }
}
```
