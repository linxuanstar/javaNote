# 第一章 缓冲流

## 1.1 概述

<!--P383-->

![缓冲流](D:\Java\笔记\图片\day10【缓冲流、转换流、序列化流、打印流】\1.png)

缓冲流，也叫够高效流是对4个基本的FileXxx流的增强，所以也是4个流，按照数据类型分类：

* 字节缓冲流：`BufferedInputStream`，`BufferedOutputStream`
* 字符缓冲流：`BufferedReader`，`BufferedWriter`

缓冲流的基本原理，是在创建流对象的时候，会创建一个内置的默认大小的缓冲区数组，通过缓冲区读写，减少系统IO次数，从而提高读写的效率。

## 1.2 字节缓冲流

### 构造方法

* `public BufferedInputStream(InputStream in)`：创建一个新的缓冲输入流。
* `public BufferedOutputStream(OutputStream out)`：创建一个新的缓冲输出流。

#### BufferedOutputStream

<!--P384-->

`BufferedOutputStream`:字节缓冲输出流

`java.io.BufferedOutputStream` extends `OutputStream`

**继承父类的成员方法：**

- `public void close()` :关闭此输出流并释放与此流相关联的任何系统资源。
- `public void flush()` :刷新此输出流并强制任何缓冲的输出字节被写出。
- `public void write(byte[] b)` :将b.length字节从指定的字节数组写入此输出流。
- `public void write(byte[] b, int off, int len)` :从指定的字节数组写入len字节，从偏移量off开始输出到此输出流。
- `public abstract void write(int b)` :将指定的字节输出流。

**构造方法：**

* `BufferedOutputStream(OutputStream out)` :创建一个新的缓冲输出流，将数据写入指定的底层输出流。

* `BufferedOutputStream(OutputStream out, int size)` :创建一个新的缓冲输出流，将具有指定缓冲区大小的数据写入指定的底层输出流。

  `OutputStream out`：字节输出流

  我们可以传递`FileOutputStream`，缓冲流会给`FIleOutputStream`增加一个缓冲区，提高`FileOutputStream`的写入效率

**使用步骤：**

1. 创建`FileOutputStream`对象，构造方法中绑定要输出的目的地。
2. 创建`BufferedOutputStream`对象，构造方法中传递`FileOutputStream`对象对象，提高`FileOutputStream`对象效率。
3. 使用`BufferedOutputStream`对象中的方法`write`，把数据写入到内部缓冲区中。
4. 使用`BufferedOutputStream`对象中的方法`flush`，把内部缓冲区的数据，刷新到文件中。
5. 释放资源（释放资源的时候，会先调用`flush`犯法刷新数据，所以第四步可以省略）。

```java
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo01BufferedOutputStream {
    public static void main(String[] args) throws IOException {
        // 创建FileOutputStream对象，构造方法中绑定要输出的目的地。
        FileOutputStream fos = new FileOutputStream("10_IO\\a.txt");
        // 创建BufferedOutputStream对象，构造方法中传递FileOutputStream对象对象，提高FileOutputStream对象效率。
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        // 使用BufferedOutputStream对象中的方法write，把数据写入到内部缓冲区中。
        byte[] bytes = "林炫你好".getBytes();
        bos.write(bytes);
        // bos.write("林炫你好呀！".getBytes());
        // 使用BufferedOutputStream对象中的方法flush，把内部缓冲区的数据，刷新到文件中。
        bos.flush();
        // 释放资源,关闭缓冲流
        bos.close();
    }
}
```

#### BufferedInputStream

<!--P385-->

`BufferedInputStream`：字节缓冲输入流

`java.io.BufferedInputStream` extends `InputStream`

**继承父类的成员方法：**

- `public void close()` :关闭此输入流并是方法与此流相关联的任何系统资源。
- `public abstract int read()` :从输入流读取数据的下一个字节。
- `public int read(byte[] b)` :从输入流中读取一些字节数，并把它们存储到字节数组b中。

**构造方法：**

* `BufferedInputStream(InputStream in)` :创建一个`BufferedInputStream`并保存其参数，即输入流in，以便将来使用。

* `BufferedInputStream(InputStream in, int size)` :创建具有指定缓冲区大小的`BufferedInputStream`并保存其参数，即输入流。

  `InputStream out`：字节输入流

  我们可以传递`FileInputStream`，缓冲流会给`FileInputStream`增加一个缓冲区，提高`FileInputStream`的读取效率

**使用步骤：**

1. 创建`FileInputStream`对象，构造方法中绑定要读取的数据源。
2. 创建`BufferedInputStream`对象，构造方法中传递`FileInputStream`对象，提高`FileInputStream`对象的读取效率。
3. 使用`BufferedInputStream`对象中的方法`read`，读取文件。
4. 释放资源。

```java
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Demo02BufferedInputStream {
    public static void main(String[] args) throws IOException {
        // 创建FileInputStream对象，构造方法中绑定要读取的数据源。
        FileInputStream fis = new FileInputStream("10_IO\\a.txt");
        // 创建BufferedInputStream对象，构造方法中传递FileInputStream对象，提高FileInputStream对象的读取效率。
        BufferedInputStream bis = new BufferedInputStream(fis);

        // 使用BufferedInputStream对象中的方法read，读取文件。
/*        int len = 0;
        while((len = bis.read()) != -1) {
            System.out.println((char)len);
        }*/
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = bis.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }

        // 释放资源，释放bis即可
        bis.close();
    }
}
```

### 效率测试

<!--P386 1.05-->

使用文件复制来测试

步骤：

1. 创建字节缓冲输入流对象，构造方法中传递字节输入流
2. 创建字节缓冲输出流对象，构造方法中传递字节输出流
3. 使用字节缓冲输入流对象中的`read`，读取文件
4. 使用字节缓冲输出流中的方法`write`，把读取的数据写入到内部缓冲区中
5. 释放资源(会先把缓冲区的数据，刷新到文件中)

```java
public class Demo02CopyFile {
    public static void main(String[] args) throws IOException {
        long s = System.currentTimeMillis();

        // 1.创建字节缓冲输入流对象，构造方法中传递字节输入流
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("D:\\图片\\动漫\\清纯诱惑\\18.jpg"));
        // 2.创建字节缓冲输出流对象，构造方法中传递字节输出流
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\Java\\18.jpg"));
        // 3.使用字节缓冲输入流对象中的read，读取文件
/*        int len = 0;
        while((len = bis.read()) != -1) {
            // 4.使用字节缓冲输出流中的方法write，把读取的数据写入到内部缓冲区中
            bos.write(len);
        }*/
        //使用数组，更快
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }

        // 5.释放资源(会先把缓冲区的数据，刷新到文件中)
        bos.close();
        bis.close();

        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - s) + "毫秒");
    }
}
```

## 1.3 字符缓冲流

### 构造方法

* `public BufferedWriter(Writer out)` :创建一个新的缓冲输出流。

* `public BufferedReader(Reader in)` :创建一个新的缓冲输入流。

构造举例，代码如下：

```java
// 创建字符缓冲输入流
BufferedReader br = new BufferedReader(new FileReader("br.txt"));
// 创建字符缓冲输出流
BufferedWriter bw = new BufferedWriter(new FileWriter("bw.txt"));
```

#### BufferedWriter

<!--P387-->

`BufferedWriter`: 字符缓冲输出流

`java.io.BufferedWriter` extends `Writer`

**继承父类的成员方法：**

- `void Write(int c)` :写入单个字符。
- `void Write(char[] cbuf)` :写入字符数组。
- `abstract void Write(char[] cbuf, int off, int len)` :写入字符数组的某一部分，off数组的开始所以，len写的字符个数。
- `void Write(String str)` :写入字符串。
- `void Write(String str, int off, int len)` :写入字符串的某一部分，off字符串的开始索引，len写的字符个数。
- `void flush()` :刷新该流的缓冲。
- `void close()` :关闭此流，但是要先刷新它。

**构造方法：**

*  `BufferedWriter(Writer out)` : 创建一个使用默认大小输出缓冲区的缓冲字符输出流。
* `BufferedWriter(Writer out,  int sz)` : 创建一个使用给定大小输出缓冲区的新缓冲字符输出流。

**特有方法：**

* BufferedWriter: `public void newLine()` :写一行分隔符，由系统属性定义符号。

**使用步骤：**

1. 创建字符缓冲输出流对象，构造方法中传递字符输出流。
2. 调用字符缓冲输出流中的方法`write`，把数据写入到内存缓冲区中。
3. 调用字符缓冲输出流中的方法`flush`，把内存缓冲区中的数据，刷新到文件中。
4. 释放资源。

```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Demo03BufferedWriter {
    public static void main(String[] args) throws IOException {
        long s = System.currentTimeMillis();
        // 创建字符缓冲输出流对象，构造方法中传递字符输出流。
        BufferedWriter bw = new BufferedWriter(new FileWriter("10_IO\\b.txt"));
        // 调用字符缓冲输出流中的方法write，把数据写入到内存缓冲区中。
        for (int i = 0; i < 5; i++) {
            bw.write("林炫你好");
            // bw.write("\r\n");
            bw.newLine();
        }
        // 调用字符缓冲输出流中的方法flush，把内存缓冲区中的数据，刷新到文件中。
        bw.flush();
        // 释放资源。
        bw.close();

        long e = System.currentTimeMillis();
        System.out.println("耗时为：" + (e - s) + "毫秒");
    }
}
```

#### BufferedReader

<!--P388-->

`BufferedReader`: 字符缓冲输入流

`java.io.BufferedReader ` extends `Reader`

**继承父类的成员方法：**

- `public void close()` :关闭此流并释放与此流相关联的任何系统资源。
- `public int read()` :从输入流中读取一个字符。
- `public int read(char[] cbuf)` :从输入流中读取一些字符，并将他们存储到字符数组cbuf中。

**构造方法：**

* `BufferedReader(Reader in)` ：创建一个使用默认大小输入缓冲区的缓冲字符输入流。
* `BufferedReader(Reader in,  int sz)`：创建一个使用指定大小输入缓冲区的缓冲字符输入流。

**特有方法：**

* BufferedReader: `public String readLine()` :读一行文字。

  返回值：包含该行内容的字符串，不包含任何终止符号，如果已经到达流末尾，则返回null。

**使用步骤：**

1. 创建字符缓冲对象，构造方法中传递字符输入流。
2. 使用字符缓冲输入流对象中的方法read/readLine读取文本。
3. 释放资源。

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Demo04BufferedReader {
    public static void main(String[] args) throws IOException {
        long s = System.currentTimeMillis();

        // 创建字符缓冲对象，构造方法中传递字符输入流。
        BufferedReader br = new BufferedReader(new FileReader("10_IO\\b.txt"));

        // 使用字符缓冲输入流对象中的方法read/readLine读取文本。
/*        String line = br.readLine();
        System.out.println(line); // 林炫你好1

        line = br.readLine();
        System.out.println(line); // 林炫你好2

        line = br.readLine();
        System.out.println(line); // null*/

        // 上述代码重复，所以可以使用循环
        String line;
        while((line = br.readLine()) != null) {
            System.out.println(line);
        }

        // 释放资源。
        br.close();

        long e = System.currentTimeMillis();
        System.out.println("耗时为：" + (e - s) + "毫秒");
    }
}
```

## 1.4 练习：文本排序

<!--P389-->

将以下段落进行排序

```java
4.将军向宠，性行淑均，晓畅军事，试用于昔日，先帝称之曰能，是以众议举宠为督。愚以为营中之事，悉以咨之，必能使行阵和睦，优劣得所。
1.先帝创业未半而中道崩殂，今天下三分，益州疲弊，此诚危急存亡之秋也。然侍卫之臣不懈于内，忠志之士忘身于外者，盖追先帝之殊遇，欲报之于陛下也。诚宜开张圣听，以光先帝遗德，恢弘志士之气，不宜妄自菲薄，引喻失义，以塞忠谏之路也。
5.亲贤臣，远小人，此先汉所以兴隆也；亲小人，远贤臣，此后汉所以倾颓也。先帝在时，每与臣论此事，未尝不叹息痛恨于桓、灵也。侍中、尚书、长史、参军，此悉贞良死节之臣，愿陛下亲之信之，则汉室之隆，可计日而待也。
9.今当远离，临表涕零，不知所言。
8.愿陛下托臣以讨贼兴复之效，不效，则治臣之罪，以告先帝之灵。若无兴德之言，则责攸之、祎、允等之慢，以彰其咎；陛下亦宜自谋，以咨诹善道，察纳雅言，深追先帝遗诏，臣不胜受恩感激。
2.宫中府中，俱为一体，陟罚臧否，不宜异同。若有作奸犯科及为忠善者，宜付有司论其刑赏，以昭陛下平明之理，不宜偏私，使内外异法也。
7.先帝知臣谨慎，故临崩寄臣以大事也。受命以来，夙夜忧叹，恐托付不效，以伤先帝之明，故五月渡泸，深入不毛。今南方已定，兵甲已足，当奖率三军，北定中原，庶竭驽钝，攘除奸凶，兴复汉室，还于旧都。此臣所以报先帝而忠陛下之职分也。至于斟酌损益，进尽忠言，则攸之、祎、允之任也。
3.侍中、侍郎郭攸之、费祎、董允等，此皆良实，志虑忠纯，是以先帝简拔以遗陛下。愚以为宫中之事，事无大小，悉以咨之，然后施行，必能裨补阙漏，有所广益。
6.臣本布衣，躬耕于南阳，苟全性命于乱世，不求闻达于诸侯。先帝不以臣卑鄙，猥自枉屈，三顾臣于草庐之中，咨臣以当世之事，由是感激，遂许先帝以驱驰。后值倾覆，受任于败军之际，奉命于危难之间，尔来二十有一年矣。
```

### 案例分析

1. 创建一个HashMap集合对象，key存储每行文本的序号（1， 2， 3）；value存储每行的文本。
2. 创建字符缓冲输入流对象，构造方法中绑定字符输入流。
3. 创建字符缓冲输出流对象，构造方法中绑定字符输出流。
4. 使用字符缓冲输入流中的方法readLine，逐行读取文本。
5. 对读取到的文本进行切割，获取行中的序号和文本内容。
6. 把切割好的序号和文本的内容存储到HashMap集合中（key序号是有序的，会自动排序）。
7. 遍历HashMap集合，获取每一个键值对。
8. 把每一个键值对，拼接为一个文本行
9. 把拼接好的文本，使用字符缓冲输出流中的方法write，写入到文件中。
10. 释放资源。

### 案例实现

```java
import java.io.*;
import java.util.HashMap;

public class Demo05Test {
    public static void main(String[] args) throws IOException {
        // 1. 创建一个HashMap集合对象，key存储每行文本的序号（1， 2， 3）；value存储每行的文本。
        HashMap<String, String> map = new HashMap<>();
        // 2. 创建字符缓冲输入流对象，构造方法中绑定字符输入流。
        BufferedReader br = new BufferedReader(new FileReader("10_IO\\in.txt"));
        // 3. 创建字符缓冲输出流对象，构造方法中绑定字符输出流。
        BufferedWriter bw = new BufferedWriter(new FileWriter("10_IO\\out.txt"));
        // 4. 使用字符缓冲输入流中的方法readLine，逐行读取文本。
        String line;
        while((line = br.readLine()) != null) {
            // 5. 对读取到的文本进行切割，获取行中的序号和文本内容。
            String[] arr = line.split("\\.");
            // 6. 把切割好的序号和文本的内容存储到HashMap集合中（key序号是有序的，会自动排序）。
            map.put(arr[0], arr[1]);
        }

        // 7. 遍历HashMap集合，获取每一个键值对。
        for (String key : map.keySet()) {
            String value = map.get(key);
            // 8. 把每一个键值对，拼接为一个文本行
            line = key + "." + value;
            // 9. 把拼接好的文本，使用字符缓冲输出流中的方法write，写入到文件中。
            bw.write(line);
            bw.newLine();
        }
        // 10. 释放资源。
        bw.close();
        br.close();
    }
}
```

# 第二章 转换流

## 2.1 字符编码和字符集

### 字符编码

<!--P390-->

按照某种规则，将字符存储到计算机中，称为**编码**。反之，将存储在计算机中的二进制数按照某种规则解析显示出来，成为**解码**。

编码：字符（能看懂的）-- 字节（看不懂的）

解码：字节（看不懂的）-- 字符（能看懂的）

* 字符编码Character Encoding :就是一套自然语言的字符与二进制数之间的对应规则。
* 编码表：生活中文字和计算机中二进制的对应规则。

### 字符集

* 字符集`Charset`：也叫编码表。是一个系统支持的所有字符的集合，包括各国家文字、标点符号、图形符号、数字等。

计算机要准确的存储和识别各种字符集符号，需要进行字符编码，一套字符集必然至少有一套字符编码。常见字符集有ASCII字符集，GBK字符集，Unicode字符集等。

![2](D:\Java\笔记\图片\day10【缓冲流、转换流、序列化流、打印流】\2.png)

可见，当指定了编码，它所对应的字符集自然就指定了，所以编码是我们最终要关心的。

* ASCII字符集：

  ASCII ((American Standard Code for Information Interchange): 美国信息交换标准代码）是基于拉丁字母的一套电脑编码系统，主要用于显示现代英语和其他西欧语言。它是最通用的信息交换标准，并等同于国际标准ISO/IEC 646。ASCII第一次以规范标准的类型发表是在1967年，最后一次更新则是在1986年，到目前为止共定义了128个字符。

## 2.2 编码引出的问题

<!--P391-->

在IDEA中，使用FileReader读取项目中的文本文件，在IDEA中，都是默认的UTF-8编码，所以没有乱码。但是如果在使用计算机的新建，新建一个文档，由于Windows系统默认的是GBK编码，所以IDEA读取会出现乱码。这就是编码与解码不一样导致的乱码。

我用计算机测试没有问题，应该是更新了一下。

```java
import java.io.FileReader;
import java.io.IOException;

public class Demo01FileReader {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("10_IO\\GBK编码.txt");
        int len = 0;
        while((len = fr.read()) != -1) {
            System.out.print((char)len);
        }
        fr.close();
    }
}
```

<!--P392-->

## 2.3 OutputStreamWriter类

<!--P393 1.06-->

`OutputStreamWriter`:是字符流通过字节流的桥梁，可使用指定的charset将要写入流中的字符编码成字节。

`java.io.OutputStreamWriter` extends `Writer`

**继承父类的共性成员方法：**

- `void Write(int c)` :写入单个字符。
- `void Write(char[] cbuf)` :写入字符数组。
- `abstract void Write(char[] cbuf, int off, int len)` :写入字符数组的某一部分，off数组的开始所以，len写的字符个数。
- `void Write(String str)` :写入字符串。
- `void Write(String str, int off, int len)` :写入字符串的某一部分，off字符串的开始索引，len写的字符个数。
- `void flush()` :刷新该流的缓冲。
- `void close()` :关闭此流，但是要先刷新它。

**构造方法：**

* `OutputStreamWriter(OutputStream out)`: 创建使用默认字符编码的`OutputStreamWriter`。

* `OutputStreamWriter(OutputStream out, String charsetName)`: 创建使用指定字符集的`OutputStreamWriter`。

  参数：

  * `OutputStream out`: 字节输出流，可以用来写转换之后的字节到文件中。
  * `String charsetName`: 指定的编码表名称，不分大小写，可以是UTF-8，也可以是utf-8，不指定会默认使用UTF-8。

**使用步骤：**

1. 创建`OutputStreamWriter`对象，构造方法中传递字节输出流和指定的编码表名称。
2. 使用`OutputStreamWriter`对象中的方法`write`，把字符转换为字节存储缓冲区中(编码)。
3. 使用`OutputStreamWriter`对象中的方法`flush`，把内存缓冲区中的字节刷新到文件中(使用字节流写字节的过程)。
4. 释放资源。

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Demo02OutputStreamWriter {
    public static void main(String[] args) throws IOException {
        write_utf_8();
    }

    // 使用转换流OutputStreamWriter写UTF-8格式的文件
    private static void write_utf_8() throws IOException {
        // 创建OutputStreamWriter对象，构造方法中传递字节输出流和指定的编码表名称。
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("10_IO\\c.txt"), "utf-8");
        // 使用OutputStreamWriter对象中的方法write，把字符转换为字节存储缓冲区中(编码)。
        osw.write("林炫你好");
        // 使用OutputStreamWriter对象中的方法flush，把内存缓冲区中的字节刷新到文件中(使用字节流写字节的过程)。
        osw.flush();
        // 释放资源。
        osw.close();
    }
}
```

## 2.4 InputStreamReader类

<!--P394 1.7-->

`InputStreamReader`: 是字节流流通字符流的桥梁；它使用指定的charset读取字节并将其解码为字符。

`java.io.InputStreamReader` extends `Reader`

**继承父类的共性成员方法：**

- `public void close()` :关闭此流并释放与此流相关联的任何系统资源。
- `public int read()` :从输入流中读取一个字符。
- `public int read(char[] cbuf)` :从输入流中读取一些字符，并将他们存储到字符数组cbuf中。

**构造方法：**

* `InputStreamReader(InputStream in)`：创建一个使用默认字符集的`InputStreamReader`。

* `InputStreamReader(InputStream in, String charsetName)`：创建使用指定字符集的`InputStreamReader`。

  参数：

  * `InputStream in`：字节输入流，用来读取文件中保存的字节。
  * `String charsetName`：指定的编码表名称，不区分大小写，可以是UTF-8，utf-8。

**使用步骤：**

1. 创建`InputStreamReader`对象，构造方法中传递字节输入流和指定的编码表名称。
2. 使用`InputStreamReader`对象中的`read`读取文件。
3. 释放资源。

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Demo03InputStreamReader {
    public static void main(String[] args) throws IOException {
        read_utf_8();
    }

    private static void read_utf_8() throws IOException {
        // 创建InputStreamReader对象，构造方法中传递字节输入流和指定的编码表名称。
        InputStreamReader isr = new InputStreamReader(new FileInputStream("10_IO\\c.txt"), "utf-8");
        // 使用InputStreamReader对象中的read读取文件。
        int len = 0;
        while((len = isr.read()) != -1) {
            System.out.println((char)len);
        }
        // 释放资源。
        isr.close();
    }
}
```

## 2.5 练习：转换文件编码

<!--P395 1.10-->

将GBK编码的文本文件，转换为UTF-8编码的文本文件。

### 案例分析

1. 指定GBK编码的转换流，读取文本文件。
2. 使用UTF-8编码的转换流，写出文本文件。

* 创建InputStreamReader对象，构造方法中传递字节输入流和指定的编码表名称GBK
* 创建OutputStreamWriter对象，构造方法中传递字节输出流和指定的编码表名称UTF-8
* 使用InputStreamReader对象中的read读取文件
* 使用OutputStreamWriter对象中的方法write，把读取的文件写入到文件中
* 释放资源

### 案例实现

```java
public class Demo04Test {
    public static void main(String[] args) throws IOException {
        // 1.创建InputStreamReader对象，构造方法中传递字节输入流和指定的编码表名称GBK
        InputStreamReader isr = new InputStreamReader(new FileInputStream("10_IO\\GBK编码.txt"), "GBK");
        // 2.创建OutputStreamWriter对象，构造方法中传递字节输出流和指定的编码表名称UTF-8
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("10_IO\\UTF-8编码.txt"), "UTF-8");
        // 3.使用InputStreamReader对象中的read读取文件
        int len = 0;
        while((len = isr.read()) != -1) {
            // 4.使用OutputStreamWriter对象中的方法write，把读取的文件写入到文件中
            osw.write(len);
        }
        // 5.释放资源
        osw.close();
        isr.close();
    }
}
```

# 第三章 序列化

## 3.1 概述

<!--P396 1.11-->

java提供了一种对象序列化的机制。用一个字节序列可以表示一个对象，该字节序列包含该对象的数据、对象的类型和对象中存储的属性等信息。字节序列写出到文件之后，相当于文件中持久保存了一个对象的信息。

反之，该字节序列还可以从文件中读取回来，重构对象，对它进行反序列化。对象的数据、对象的类型和对象中的存储的数据信息，都可以用力啊在内存中创建对象。

![序列化](D:\Java\笔记\图片\day10【缓冲流、转换流、序列化流、打印流】\3.png)

## 3.2 ObjectOutputStream类

<!--P397-->

`java.io.ObjectOutputStream`类，将java对象中的原始数据类型写出到文件，事先对象的持久存储。

### 构造方法

* `public ObjcetOutputStream(OutputStream out)`: 创建一个指定的OutputStream的ObjectOutputStream。

构造举例，代码如下：

### 序列化操作

一个对象要想要序列化，必须要满足两个条件：

* 该类必须实现`java.io.Serializable`接口，`Serializable`是一个标记接口，不实现此接口的类将不会使任何状态序列化或者反序列化，会抛出`NotSerializableException`。

* 该类的所有竖向必须是可序列化的。如果有一个属性不需要可序列化的，则该属性必须注明是瞬态的，使用`transient`关键字修饰。

  <!--P399-->

  static关键字：静态关键字

  ​	静态关键优先于非静态加载到内存中（静态优先于对象进入到内存中）

  ​	被static 修饰的成员变量不能被序列化的，序列化的都是对象

  transient关键字：瞬态关键字

  ​	被transient 修饰的成员变量不能被序列化的，但是它却没有static的作用，只是能够避免序列化

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Demo01ObjectOutputStream {
    public static void main(String[] args) throws IOException {
        // 1.创建ObjcetOutputStream对象，构造方法中传递字节输出流
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("10_IO\\oos.txt"));
        // 2.使用ObjectOutputStream对象中的方法writeObject，把对象写入到文件中
        oos.writeObject(new Person("小仙女", 19));
        // 3.释放资源
        oos.close();
    }
}



import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {
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

## 3.3 ObjectInputStream类

<!--P398-->

ObjectInputStream反序列流，将之前使用`ObjectOutputStream`序列化的原始数据恢复为对象。

### 构造方法

* `public ObjectInputStream(InputStream in)`: 创建一个指定的`InputStream`的`ObjectInputStream`。

### 反序列化操作1

如果能找到一个对象的class文件，我们可以进行反序列化操作，调用`ObjectInputStream`读取对象的方法。

* `public final Object readobject()`：读取一个对象，返回值是Object。

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

// ClassNotFoundException 文件找不到异常，所以使用必须文件存在，否则会抛出异常
public class Demo02ObjectInputStream {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 1.创建ObjectInputStream对象，构造方法中传递字节输入流
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("10_IO\\oos.txt"));
        // 2.使用ObjectInputStream对象中的方法readObject读取保存对象中的文件
        Object o = ois.readObject();
        // 3.释放资源
        ois.close();
        // 4.使用读取出来的对象(打印)
        System.out.println(o);// Person{name='小仙女', age=19}
        System.out.println((Person)o);// Person{name='小仙女', age=19}
        Person p = (Person)o;
        System.out.println(p.getName() + p.getAge());// 小仙女19
    }
}
```

**对于JVM可以反序列化对象，它必须是能够找到class文件的类。如果找不到该类的class文件，则抛出一个`ClassNotFoundException`异常。**

### 反序列化操作2

<!--400-->

**另外，当JVM反序列化对象时，能找到class文件，但是class文件在序列化对象之后发生了修改，那么反序列化操作也会失败，抛出一个`InvalidClassException`异常。发生这个异常的原因如下：**

* 该类的序列版本号与从流中读取的类描述符的版本号不匹配
* 该类包含位置数据类型
* 该类没有可访问的无参数构造方法

`Serializable`接口给需要序列化的类，提供了一个序列版本号。`SerialVersionUID`该版本号的目的在于验证序列化的对象和对应类是否版本匹配。

可以直接给他一个序列号，常量，这样不能改变了，这样修改过后序列号不会变，所以可以继续反序列化。

static final long SerialVersionUID = count(常量)。

![反序列化操作](D:\Java\笔记\图片\day10【缓冲流、转换流、序列化流、打印流】\4.png)

## 3.4 练习：序列化集合

<!--P401-->

1. 将存有多个自定义对象的集合序列化操作，保存到`list.txt`文件中。
2. 反序列化`list.txt`，并遍历集合，打印对象信息。

### 案例分析

1. 定义一个存储Person对象的ArrayList集合。
2. 往ArrayList集合中存储Person对象。
3. 创建一个序列化流ObjectOutputStream对象。
4. 使用ObjectOutputStream对象中的方法writeObjcet，对集合进行序列化。
5. 创建一个反序列化ObjectInputStream对象。
6. 使用ObjectInputStream对象中的方法readObject读取文件中保存的集合。
7. 把Object类型的集合转换为ArrayList类型。
8. 遍历ArrayList集合。
9. 释放资源。

### 案例实现

```java
public class Demo03Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 1. 定义一个存储Person对象的ArrayList集合。
        ArrayList<Person> list = new ArrayList<>();
        // 2. 往ArrayList集合中存储Person对象。
        list.add(new Person("张三", 19));
        list.add(new Person("李四", 20));
        list.add(new Person("王五", 23));
        // 3. 创建一个序列化流ObjectOutputStream对象。
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("10_IO\\Demo03Test.txt"));
        // 4. 使用ObjectOutputStream对象中的方法writeObjcet，对集合进行序列化。
        oos.writeObject(list);
        // 5. 创建一个反序列化ObjectInputStream对象。
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("10_IO\\Demo03Test.txt"));
        // 6. 使用ObjectInputStream对象中的方法readObject读取文件中保存的集合。
        Object o = ois.readObject();
        // 7. 把Object类型的集合转换为ArrayList类型。
        ArrayList<Person> list2 = (ArrayList<Person>) o;
        // 8. 遍历ArrayList集合。
        for (Person person : list2) {
            System.out.println(person);
        }
        // 9. 释放资源。
        ois.close();
        oos.close();
    }
}
```

# 第四章 打印流

## 4.1 概述

<!--P402 1.12-->

平时我们在控制台打印输出，是调用`print`方法和`println`方法完成的，这两个方法都来自于`java.io.PrintStream`类，该类能够方便地打印各种数据类型的值，是一种便捷的输出方式。

## 4.2 PrintStream类

`PrintStream`：为其他输出流添加了功能，使他们能够方便的打印出各种数据值表示形式。

`java.io.PrintStream`: 打印流

`java.io.PrintStream` extends `OutputStream`

### 构造方法

* `public PrintStream(File file)`: 输出的目的地是一个文件。
* `public PrintStream(OutputStream out)`: 输出的目的地是一个字节输出流。
* `public PrintStream(String fileName)`: 使用指定的文件名创建一个新的打印流。

构造举例，代码如下：

```java
PrintStream ps = new PrintStream("ps.txt");
```

**继承父类的成员方法：**

- `public void close()` :关闭此输出流并释放与此流相关联的任何系统资源。
- `public void flush()` :刷新此输出流并强制任何缓冲的输出字节被写出。
- `public void write(byte[] b)` :将b.length字节从指定的字节数组写入此输出流。
- `public void write(byte[] b, int off, int len)` :从指定的字节数组写入len字节，从偏移量off开始输出到此输出流。
- `public abstract void write(int b)` :将指定的字节输出流。

```java
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Demo01PrintStream {
    public static void main(String[] args) throws FileNotFoundException {
        // 创建打印流PrintStream对象，构造方法中绑定要输出的目的地
        PrintStream ps = new PrintStream("10_IO\\print.txt");
        // 如果使用继承子父类的write方法写数据，那么查看数据的时候会查询编码表，97 == a
        ps.write(97); // a
        
        // 如果使用自己特于的方法print/Println方法写数据，那么会原样输出 97 == 97
        ps.print(97);

        // 释放资源
        ps.close();
    }
}
```

### 改变打印流向

`System.out`就是`PrintStream`类型的，只不过它的流向是系统规定的，打印在控制台上。不过，既然是流对象，我们就可以改变它的流向。

使用`System.setOut`方法改变输出语句的目的地改为参数中传递的打印流的目的地

* `static void setOut(PrintStream out)`: 重新分配“标准输出流”。

```java
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Demo02PrintStream {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("我是在控制台输出");

        PrintStream ps = new PrintStream("10_IO\\目的地打印流");
        // 把输出语句的目的地改变Wie打印流的目的地。
        System.setOut(ps);
        System.out.println("打印流中输出");// 打印流中输出

        // 释放资源
        ps.close();
    }
}
```