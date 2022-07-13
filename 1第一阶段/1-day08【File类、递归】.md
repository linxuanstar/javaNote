# 第一章 File类

## 1.1 概述

<!--P343-->

java.io.File类是文件和目录路径名的抽象表示，主要用于文件和目录的创建、查找和删除等操作。

<!--P344 12.28-->

```java
import java.io.File;

/*
    file:文件
    directory:文件夹/目录
    path:路径
 */
public class Demo01File {
    public static void main(String[] args) {
        /*
            static String pathSeparator 与系统有关的路径分隔符，为了方便，它被表示为一个字符串。
            static char pathSeparatorChar 与系统有关的路径分隔符。

            static String separator 与系统有关的默认名称分隔符，为了方便，它被表示为一个字符串。
            static char separatorChar 与系统有关的默认名称分隔符。

            操作路径：路径不能写死
            windows:C:\develop\a\a.txt
            linux:C:/develop/a/a.txt
            把路径斜杠/\换成"+File.separator+"
            写成："C:"+File.separator+"develop"+File.separator+"a"+File.separator+"a.txt"
        */
        String pathSeparator = File.pathSeparator;
        System.out.println(pathSeparator); // 路径分隔符 Windows：分号；  Linux：冒号：

        String separator = File.separator;
        System.out.println(separator);// 文件名称分隔符 Windows：反斜杠\ linux:正斜杠/
    }
}
```

<!--P345-->

windows中使用反斜杠代表路径。反斜杠是转义字符，所以两个反斜杠代表一个普通的反斜杠

## 1.2 构造方法

- 静态的成员可以根据类名直接访问
- 通过构造方法可以创建对象
- 创建完对象，通过对象名可以直接访问对象中的成员方法

<!--P346-->

* `public File(String pathname)`：通过将给定的路径名字符串转换为抽象路径名来创建新的File实例。

* `public File(String parent, String child)`：从父路径名字符串和子路径名字符串创建新的File案例。

* `public File(File parent, String child)`：从父抽象路径名和子路径名字符串创建新的File实例。

* 构造举例，代码如下：

* ```java
  import java.io.File;
  
  public class Demo02File {
      public static void main(String[] args) {
          // show02("c:\\", "a.txt");
          show03();
      }
  
      /*
          File(File parent, String child) 根据 parent 抽象路径名和 child 路径名字符串创建一个新 File 实例。
       */
      private static void show03() {
          File person = new File("c:\\");
          File f = new File(person, "a.txt");
          System.out.println(f);
      }
  
      /*
          File(String parent, String child) 根据 parent 路径名字符串和 child 路径名字符串创建一个新 File 实例。
          参数：把路径分成了两部分
              String parent:父路径
              String Child:子路径
          好处：
              父路径和子路径，可以单独书写，使用起来非常灵活；父路径和子路径都可以变化
       */
      private static void show02(String parent, String child) {
          File file = new File(parent, child);
          System.out.println(file);
      }
  
      /*
          File(String pathname) 通过将给定路径名字符串转换为抽象路径名来创建一个新 File 实例。
          参数：
              String pathname:字符串的路径名称
              路径可以是以文件结尾，也可以是以文件夹结尾
              路径可以是相对路径，也可以是绝对路径
              路径可以存在，也可以不存在，无需考虑是否存在
       */
      private static void show01() {
          File f1 = new File("D:\\Java\\a.txt");
          System.out.println(f1); // 重写了Object类的toString方法：D:\Java\a.txt
  
          File f2 = new File("D:\\Java");
          System.out.println(f2); // D:\Java
  
          File f3 = new File("b.txt");
          System.out.println(f3); // b.txt
      }
  }
  ```

## 1.3 常用方法

### 获取功能的方法

<!--P347 12.29-->

* `public String getAbsouletPath()`:返回此File的绝对路径名字符串。

* `public String getPath()`:将此File转换为路径名字符串。

* `public String getName()`:返回由此File表示的文件或者目录的名称。

* `public long length()`:返回由此File表示的文件的长度。

  方法演示，代码如下：

  ```java
  import java.io.File;
  
  /*
      File类获取功能的方法
          - public String getAbsouletPath():返回此File的绝对路径名字符串。
          - public String getPath():将此File转换为路径名字符串。
          - public String getName():返回由此File表示的文件或者目录的名称。
          - public long length():返回由此File表示的文件的长度。
  
   */
  public class Demo03File {
      public static void main(String[] args) {
          // show01();
          // show02();
          // show03();
          show04();
      }
  
      /*
          public long length():返回由此File表示的文件的长度。
          获取的是构造方法指定的文件的大小，以字节为单位
          注意：
              文件夹是没有大小概念的，不能获取文件夹的大小
              如果构造方法给的路径不存在，那么length方法返回0
       */
      private static void show04() {
          File f1 = new File("D:\\B站视频\\79312032\\347\\79312032_347_0.mp4");
          System.out.println(f1.length()); // 32862870
  
          File f2 = new File("D:\\B站视频\\79312032\\347");
          System.out.println(f2.length()); // 0
      }
  
      /*
          public String getName():返回由此File表示的文件或者目录的名称。
          获取的就是构造方法传递路径的结尾部分（文件/文件夹）
       */
      private static void show03() {
          File f1 = new File("D:\\B站视频\\79312032\\347");
          String name1 = f1.getName();
          System.out.println(name1); // 347
      }
  
      /*
          - public String getPath():将此File转换为路径名字符串。
       */
      private static void show02() {
          File f1 = new File("D:\\B站视频\\79312032\\347");
          File f2 = new File("a.txt");
          String path1 = f1.getPath();
          String path2 = f2.getPath();
          System.out.println(path1); // D:\B站视频\79312032\347
          System.out.println(path2); // a.txt
  
          System.out.println(f1); // D:\B站视频\79312032\347
          System.out.println(f1.toString()); // D:\B站视频\79312032\347
      }
  
      /*
            - public String getAbsouletPath():返回此File的绝对路径名字符串。
            获取的构造方法中传递的路径
            无论路径是绝对的还是相对的，getAbsouletPath方法返回的都是绝对路径
       */
      private static void show01() {
          File f1 = new File("D:\\B站视频\\79312032\\347");
          String absoulutePath1 = f1.getAbsolutePath();
          System.out.println(absoulutePath1); // D:\B站视频\79312032\347
  
          File f2 = new File("a.txt");
          String absolutePath2 = f2.getAbsolutePath();
          System.out.println(absolutePath2); // D:\Java\IdeaProjects\basic-code\a.txt
      }
  }
  ```

### 判断功能的方法

<!--P348-->

* `public boolean exists()`:此File表示的文件或者目录是否实际存在。

* `public boolean isDirectory()`:此File表示的是否为目录。

* `public boolean isFile()`:此FIle表示的是否为文件。

  方法演示，代码如下：

  ```java
  import java.io.File;
  
  /*
      - public boolean exists():此File表示的文件或者目录是否实际存在。
      - public boolean isDirectory():此File表示的是否为目录。
      - public boolean isFile():此FIle表示的是否为文件。
  
   */
  public class Demo04File {
      public static void main(String[] args) {
          // show01();
          show02();
      }
  
      /*
          - public boolean isDirectory():此File表示的是否为目录。
          - public boolean isFile():此FIle表示的是否为文件。
          注意：
              电脑中不是文件就是文件夹，所以两个方法互斥
              这两个方法使用前提是路径必须存在，不存在都返回false
       */
      private static void show02() {
          File f1 = new File("D:\\B站视频\\79312032\\348\\a.txt");
          System.out.println(f1.isDirectory());
          System.out.println(f1.isFile());
          File f2 = new File("D:\\B站视频\\79312032\\348");
          System.out.println(f2.isDirectory());
          System.out.println(f2.isFile());
      }
  
      /*
          - public boolean exists():此File表示的文件或者目录是否实际存在。
       */
      private static void show01() {
          File f1 = new File("D:\\B站视频\\79312032\\348");
          boolean exists1 = f1.exists();
          System.out.println(exists1); // true
          File f2 = new File("79312032_348_0.mp4");
          System.out.println(f2.exists()); // false
      }
  }
  ```

### 创建删除功能的方法

<!--P349-->

* `public boolean creatNewFile()`:当且仅当具有该名称的文件上尚不存在时，创建一个新的空文件。

* `public boolean delete()`:删除由此File表示的文件或者目录。

* `public boolean mkdir()`:创建由此File表示的目录。

* `public boolean mkdirs()`:创建由此File表示的目录，包括任何必需但不存在的父目录。

  方法演示，代码如下：

  ```java
  import java.io.File;
  import java.io.IOException;
  
  /*
      - public boolean creatNewFile():当且仅当具有该名称的文件上尚不存在时，创建一个新的空文件。
      - public boolean delete():删除由此File表示的文件或者目录。
      - public boolean mkdir():创建由此File表示的目录。
      - public boolean mkdirs():创建由此File表示的目录，包括任何必需但不存在的父目录。
   */
  public class Demo05File {
      public static void main(String[] args) throws IOException {
          // show01();
          show02();
      }
  
      /*
          - public boolean mkdir():创建由此File表示的目录。
          - public boolean mkdirs():创建由此File表示的目录，包括任何必需但不存在的父目录。
          返回值：布尔值
              true:文件夹不存在，创建文件夹
              false：文件夹存在，不创建文件夹
          注意：
              1.此方法只能创建文件夹，不能创建文件
              如果不存在，不会抛出异常
       */
      private static void show02() {
          File f1 = new File("D:\\B站视频\\79312032\\349\\1");
          boolean mkdir1 = f1.mkdir();
          System.out.println(mkdir1);
  
          File f2 = new File("D:\\B站视频\\79312032\\111\\222\\333");
          boolean mkdirs2 = f2.mkdirs();
          System.out.println(mkdirs2);
  
          File f4 = new File("08_F\\ccc");
          boolean mkdirs4 = f4.mkdirs();
          System.out.println(mkdirs4); // 不会抛出异常
      }
  
      /*
          - public boolean creatNewFile():
          当且仅当具有该名称的文件上尚不存在时，创建一个新的空文件。
          创建文件的路径和名称在构造方法中给出（构造方法的参数）
          返回值：布尔值
              true:文件不存在，创建文件
              false：文件存在，不创建文件
          注意：
              1.此方法只能创建文件，不能创建文件夹
              2.创建文件的路径需要存在，否则会抛出异常
  
          public boolean createNewFile() throws IOException
          createNewFile声明抛出了IOException，我们调用这个方法也需要处理这个异常，throws或者try catch
  
       */
      private static void show01() throws IOException {
          File f1 = new File("D:\\B站视频\\79312032\\349\\1.txt");
          boolean b1 = f1.createNewFile();
          System.out.println(b1);
      }
  }
  ```

## 1.4 目录的遍历

<!--P350-->

* `public String[] list()`:返回一个String数组，表示该File目录中所有的子文件或者目录。
* `public File[] listFiles()`:返回一个File数组，表示该File目录中的所有的子文件或者目录。

```java
import java.io.File;

/*
    - public String[] list():返回一个String数组，表示该File目录中所有的子文件或者目录。
    - public File[] listFiles():返回一个File数组，表示该File目录中的所有的子文件或者目录。

    注意：
        list方法和listFiles方法遍历的是构造方法中给出的目录
        如果构造方法中给出的目录路径不存在，会抛出空指针异常
        如果构造方法中给出的路径不是一个目录，也会抛出空指针异常
 */
public class Demo06File {
    public static void main(String[] args) {
        // show01();
        show02();
    }

    /*
        - public File[] listFiles():返回一个File数组，表示该File目录中的所有的子文件或者目录。
        遍历构造方法中给出的目录，会获取目录中所有的文件/文件夹，把文件/文件夹封装为File对象，多个File对象存储到File数组中
     */
    private static void show02() {
        File f2 = new File("D:\\B站视频\\79312032\\350");
        File[] files2 = f2.listFiles();
        for (File file : files2) {
            System.out.println(file);
        }
    }

    /*
        - public String[] list():返回一个String数组，表示该File目录中所有的子文件或者目录。
        遍历构造方法中给出的目录，会获取目录中所有文件/文件夹的名称，把获取到的多个名称存储到一个String类型的数组中
     */
    private static void show01() {
        File f1 = new File("D:\\B站视频\\79312032\\350");
        String[] list1 = f1.list();
        for (String s : list1) {
            System.out.println(s);
        }
    }
}
```

> 小提示：
>
> 调用listFiles方法的File对象，表示的必须是实际存在的目录，否则返回null，无法进行遍历。



# 第二章 递归

## 2.1 概述

<!--P351-->

* 递归：指在当前方法内调用自己的这种现象。
* 递归的分类：
  * 递归分为两种，直接递归和间接递归。
  * 直接递归称为方法自身调用自己。
  * 间接递归可以方法A调用B方法，B方法调用C方法，C方法调用A方法。
* 注意事项：
  * 递归要有条件限定，保证递归可以停止，否则会栈内存溢出。
  * 递归次数不要太多，否则会栈内存溢出。
  * 构造方法，禁止递归。

```java
public class Demo01Recursion {
    public static void main(String[] args) {
        // a();
        // b(1);
    }

    /*
        构造方法，禁止递归。
     */
/*
    public Demo01Recursion() {
        Demo01Recursion();
    }
*/


    /*
        递归次数不要太多，否则会栈内存溢出。
        23070 Exception in thread "main" java.lang.StackOverflowError
     */
    private static void b(int i) {
        System.out.println(i);
        if (i == 30000) {
            return;
        }
        b(++i);
    }

    /*
        递归要有条件限定，保证递归可以停止，否则会栈内存溢出。
        Exception in thread "main" java.lang.StackOverflowError
     */
    private static void a() {
        System.out.println("a方法");
        a();
    }
}
```

## 2.2 递归累加求和

### 计算1~n的和

<!--P352 12.30-->

```java
public class Demo02Recursion {

    public static void main(String[] args) {
        int s = sum(10);
        System.out.println(s);
    }

    private static int sum(int n) {
        if (n == 1) {
            return 1;
        }
        return n + sum(n - 1);
    }
}
```

## 2.3 递归求阶乘

<!--P353-->

```java
/*
    递归求阶乘
 */
public class Demo03Recursion {
    public static void main(String[] args) {
        int sum = multiply(5);
        System.out.println(sum);
    }

    public static int multiply(int n) {
        if (n == 1) {
            return 1;
        }
        return n * multiply(n - 1);
    }
}
```

## 2.4 递归打印多级目录

<!--P354-->

**分析**：多级目录的打印，就是当前目录的嵌套。遍历之前，无从知道有多少级的目录，所以我们还是要使用递归实现。

**代码实现：**

```java
import java.io.File;

public class Demo04Recursion {
    public static void main(String[] args) {
        File file = new File("D:\\Java\\笔记");
        show(file);
    }

    public static void show(File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                show(f);
            } else {
                System.out.println(f);
            }
        }
    }
}
```

# 第三章 综合案例

## 3.1 文件搜索

<!--P355-->

搜索`D:\\Java\\`笔记目录中的`.png`文件。

**分析：**

1. 目录搜索，无法判断有多少级目录，所以使用递归，遍历所有目录。
2. 遍历目录时，获取的子文件，通过文件名称，判断是否符合条件。

**代码实现：**

```java
import java.io.File;

public class Demo05Recursion {
    public static void main(String[] args) {
        File file = new File("D:\\Java\\笔记");
        show(file);
    }

    public static void show(File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                show(f);
            } else {
                // f是一个文件，直接打印就好了
                /*
                    只要.png结尾的文件
                    要把File对象f,转为字符串对象
                 */
/*                String s = f.toString();

                // 把字符创转为小写
                s = s.toLowerCase();

                // 调用String类中的方法endswith判断字符串
                boolean b = s.endsWith(".png");

                // 如果是，输出
                if (b) {
                    System.out.println(f);
                }*/

                // 链式编程：
                if (f.toString().toLowerCase().endsWith(".png")) {
                    System.out.println(f);
                }
            }
        }
    }
}
```

## 3.2 文件过滤器优化

<!--P356 12.31-->

`java.io.FileFilter`是一个接口，是File的过滤器。该接口的对象可以传递给File类的`listFiles(FileFilter)`作为参数，接口中只有一个方法。

`boolean accept(File pathname)`：测试pathname是否应该包含在当前File目录中，符合则返回true。

分析：

1. 接口作为参数，需要传递子类对象，重写其中方法。我们选择匿名内部类方式，比较简单。

2. `accept`方法，参数为File，表示当前File下所有的子文件和子目录。保留住则返回true，过滤掉则返回false。

   保留规则：

   1. 要么是.java文件。
   2. 要么是目录，用于继续遍历。

3. 通过过滤器的作用

```java
import java.io.File;

public class Demo01Filter {
    public static void main(String[] args) {
        File file = new File("D:\\Java\\笔记");
        show(file);
    }

    public static void show(File dir) {
        File[] files = dir.listFiles(new FileFilterImpl());
        for (File f : files) {
            if (f.isDirectory()) {
                show(f);
            } else {
                System.out.println(f);
            }
        }
    }
}



import java.io.File;
import java.io.FileFilter;

public class FileFilterImpl implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }
        return pathname.toString().toLowerCase().endsWith(".png");
    }
}

```

## 3.3 Lambda优化

<!--P357 1.1-->

```java
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class Demo02Filter {
    public static void main(String[] args) {
        File file = new File("D:\\Java\\笔记");
        show(file);
    }

    public static void show(File dir) {
        // 传递过滤器对象，使用匿名内部类 FileFileter
/*        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                // 过滤规则，pathname是文件夹或者是.png结尾的文件返回true
                return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(".png");
            }
        });*/

        // Lambda优化，括号只有一个参数，那么括号可以删去
        File[] files = dir.listFiles(pathname -> pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(".png"));

        // FilenameFilter
/*        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // 过滤规则，pathname是文件夹或者是.png结尾的文件返回true
                return new File(dir, name).isDirectory() || name.toLowerCase().endsWith(".png");
            }
        });*/

        // 使用Lambda表达式优化
/*        File[] files = dir.listFiles((File d, String name) -> {
            return new File(d, name).isDirectory() || name.toLowerCase().endsWith(".png");
        });*/

        // 再进行优化
        // 当里面只有一行代码的时候可以删掉一些东西。数据类型，大括号，return，分号删掉，要是想要删掉一个就都需要删掉
        // File[] files = dir.listFiles((d, name) -> new File(d, name).isDirectory() || name.toLowerCase().endsWith(".png"));

        for (File f : files) {
            if (f.isDirectory()) {
                show(f);
            } else {
                System.out.println(f);
            }
        }
    }
}
```