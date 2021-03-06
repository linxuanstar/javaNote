![](D:\Java\笔记\图片\3-day31【Linux】\屏幕截图 2022-04-21 081544.png)

# 第一章Linux各目录

| 目录        |                                                              |
| ----------- | ------------------------------------------------------------ |
| /bin        | 存放二进制可执行文件(`ls`,`cat`,`mkdir`等)，常用命令一般都在这里。 |
| /etc        | 存放系统管理和配置文件                                       |
| /home       | 存放所有用户文件的根目录，是用户主目录的基点，比如用户user的主目录就是`/home/user`，可以用`~user`表示![img](D:\Java\笔记\图片\3-day31【Linux】\clip_image001.png) |
| /usr        | 用于存放系统应用程序，比较重要的目录`/usr/local`本地系统管理员软件安装目录（安装系统级的应用）。这是最庞大的目录，要用到的应用程序和文件几乎都在这个目录。 |
| /opt        | 额外安装的可选应用程序包所放置的位置。一般情况下，我们可以把tomcat等都安装到这里。 |
| /proc       | 虚拟文件系统目录，是系统内存的映射。可直接访问这个目录来获取系统信息。 |
| /root       | 超级用户（系统管理员）的主目录（特权阶级）                   |
| /sbin       | 存放二进制可执行文件，只有`root`才能访问。这里存放的是系统管理员使用的系统级别的管理命令和程序。如`ifconfig`等。 |
| /dev        | 用于存放设备文件。                                           |
| /mnt        | 系统管理员安装临时文件系统的安装点，系统提供这个目录是让用户临时挂载其他的文件系统。 |
| /boot       | 存放用于系统引导时使用的各种文件                             |
| /lib        | 存放跟文件系统中的程序运行所需要的共享库及内核模块。共享库又叫动态链接共享库，作用类似windows里的`.dll`文件，存放了根文件系统程序运行所需的共享文件。 |
| /tmp        | 用于存放各种临时文件，是公用的临时文件存储点。               |
| /var        | 用于存放运行时需要改变数据的文件，也是某些大文件的溢出区，比方说各种服务的日志文件（系统启动日志等。）等。 |
| /lost+found | 这个目录平时是空的，系统非正常关机而留下“无家可归”的文件（windows下叫什么`.chk`）就在这里 |

Linux目录和Windows目录有着很大的不同，Linux目录类似一个树，最顶层是其根目录，如下图：

![](D:\Java\笔记\图片\3-day31【Linux】\clip_image003.jpg)

- `/bin`二进制可执行命令

- `/dev`设备特殊文件

- `/etc`系统管理和配置文件

- `/etc/rc.d`启动的配置文件和脚本

- `/home`用户主目录的基点，比如用户user的主目录就是/home/user，可以用~user表示

- `/lib`标准程序设计库，又叫动态链接共享库，作用类似windows里的.dll文件

- `/sbin`超级管理命令，这里存放的是系统管理员使用的管理程序

- `/tmp`公共的临时文件存储点

- `/root`系统管理员的主目录

- `/mnt`系统提供这个目录是让用户临时挂载其他的文件系统

- `/lost+found`这个目录平时是空的，系统非正常关机而留下“无家可归”的文件（windows下叫什么.chk）就在这里

- `/proc`虚拟的目录，是系统内存的映射。可直接访问这个目录来获取系统信息。

- `/var`某些大文件的溢出区，比方说各种服务的日志文件

- `/usr`最庞大的目录，要用到的应用程序和文件几乎都在这个目录，其中包含：

  - `/usr/x11R6`存放xwindow的目录

  - `/usr/bin`众多的应用程序

  - `/usr/sbin`超级用户的一些管理程序

  - `/usr/doc`linux文档

  - `/usr/include`linux下开发和编译应用程序所需要的头文件

  - `/usr/lib`常用的动态链接库和软件包的配置文件

  - `/usr/man`帮助文档

  - `/usr/src`源代码，linux内核的源代码就放在/usr/src/linux里

  - `/usr/local/bin`本地增加的命令

  - `/usr/local/lib`本地增加的库根文件系统

![](D:\Java\笔记\图片\3-day31【Linux】\clip_image002.png)

通常情况下，根文件系统所占空间一般应该比较小，因为其中的绝大部分文件都不需要经常改动，而且包括严格的文件和一个小的不经常改变的文件系统不容易损坏。
除了可能的一个叫`/vmlinuz`标准的系统引导映像之外，根目录一般不含任何文件。所有其他文件在根文件系统的子目录中。

1. `/bin`目录
   `/bin`目录包含了引导启动所需的命令或普通用户可能用的命令(可能在引导启动后)。这些命令都是二进制文件的可执行程序(`bin`是`binary`--二进制的简称)，多是系统中重要的系统文件。
2. `/sbin`目录
   `/sbin`目录类似`/bin`，也用于存储二进制文件。因为其中的大部分文件多是系统管理员使用的基本的系统程序，所以虽然普通用户必要且允许时可以使用，但一般不给普通用户使用。
3. `/etc`目录
   `/etc`目录存放着各种系统配置文件，其中包括了用户信息文件`/etc/passwd`，系统初始化文件`/etc/rc`等。linux正是依赖这些文件才得以正常地运行。
4. `/root`目录
   `/root`目录是超级用户的目录。
5. `/lib`目录
   `/lib`目录是根文件系统上的程序所需的共享库，存放了根文件系统程序运行所需的共享文件。这些文件包含了可被许多程序共享的代码，以避免每个程序都包含有相同的子程序的副本，故可以使得可执行文件变得更小，节省空间。
6. `/lib/modules`目录
   `/lib/modules`目录包含系统核心可加载各种模块，尤其是那些在恢复损坏的系统时重新引导系统所需的模块(例如网络和文件系统驱动)。
7. `/dev`目录
   `/dev`目录存放了设备文件，即设备驱动程序，用户通过这些文件访问外部设备。比如，用户可以通过访问`/dev/mouse`来访问鼠标的输入，就像访问其他文件一样。
8. `/tmp`目录
   `/tmp`目录存放程序在运行时产生的信息和数据。但在引导启动后，运行的程序最好使用`/var/tmp`来代替`/tmp`，因为前者可能拥有一个更大的磁盘空间。
9. `/boot`目录
   `/boot`目录存放引导加载器(`bootstraploader`)使用的文件，如`lilo`，核心映像也经常放在这里，而不是放在根目录中。但是如果有许多核心映像，这个目录就可能变得很大，这时使用单独的文件系统会更好一些。还有一点要注意的是，要确保核心映像必须在ide硬盘的前1024柱面内。
10. `/mnt`目录
    `/mnt`目录是系统管理员临时安装(`mount`)文件系统的安装点。程序并不自动支持安装到`/mnt`。`/mnt`下面可以分为许多子目录，例如`/mnt/dosa`可能是使用`msdos`文件系统的软驱，而`/mnt/exta`可能是使用ext2文件系统的软驱，`/mnt/cdrom`光驱等等。
11. `/proc`,`/usr`,`/var`,`/home`目录
    其他文件系统的安装点。

# 第二章 Linux的常用命令

如果我们不知道参数，可以使用`--help`。例如：`mkdir --help`，那么就会显示参数。

## 2.1 切换目录命令

切换目录命令 `cd`

使用tab键来补全文件路径

- 使用`cd app` 切换到`app`目录
- `cd ..`  切换到上一层目录
- `cd /`    切换到系统根目录
- `cd ~`  切换到用户主目录
- `cd -`   切换到上一个所在目录
- `pwd` 显示当前所在目录

## 2.2 列出文件列表

列出文件列表：`ls` `ll` 

`ls(list)`是一个非常有用的命令，用来显示当前目录下的内容。配合参数的使用，能以不同的方式显示目录内容。 

 格式：`ls[参数] [路径或文件名]`

- `ls -a` 显示所有文件或目录（包含隐藏的文件）通常隐藏文件都是以`.`开头的。
- `ls -l` 显示文件的详细信息 == `ll`命令

## 2.3 创建目录和移除目录

创建目录和移除目录：`mkdir` `rmdir`

`mkdir(make directory)`命令可用来创建子目录。

- `mkdir app`在当前目录下创建app目录
- `mkdir –p app2/test` 创建`aap2`以及`test`目录，创建了一个`app2`的目录，然后在`app2`目录下面创建了一个`test`目录。

`rmdir(remove directory)`命令可用来删除“空”的子目录

- `rmdir app` 删除`app`目录，**但是如果app目录下面有其他文件，那么就无法删除**。

## 2.4 浏览文件

`cat`、`more`、`less`、`tail`

1. `cat`用于显示文件的内容。格式：`cat[参数]<文件名>`
   * `cat yum.conf`

2. `more`一般用于要显示的内容会超过一个画面长度的情况。

   按空格键显示下一个画面。回车显示下一行内容。**按 q 键退出查看，ctrl+c也可以结束查看**。

   * `more yum.conf`

3. `less`用法和`more`类似，不同的是`less`可以通过`PgUp`、`PgDn`键来控制。**按 q 键退出查看，ctrl+c也可以结束查看**

   PgUp 和 PgDn 进行上下翻页。

   * `less yum.conf`

4. `tail`命令是在实际使用过程中使用非常多的一个命令，它的功能是：用于显示文件后几行的内容。

   * `tail -10 /etc/passwd` 查看后10行数据

   * `tail -f catalina.log` 动态查看日志

   ctrl+c 结束查看

## 2.5 文件操作

`rm`、`cp`、`mv`、`tar`、`find`、`grep`

* `rm` 删除文件

  用法：`rm [选项]... 文件...`   删除需要用户确认, `rm - f` 删除不询问

  - `rm a.txt` 删除`a.txt`文件，

  - `rm -f a.txt` 不询问，直接删除rm 删除目录

  - `rm -r a` 删除文件夹

  - `rm -rf a` 不询问递归删除

  - `rm -rf *`  删除所有文件

  - <font color = "red">`rm -rf /*`  自杀</font>

* `cp`复制文件

  `cp(copy)`命令可以将文件从一处复制到另一处。一般在使用`cp`命令时将一个文件复制成另一个文件或复制到某目录时，需要指定源文件名与目标文件名或目录。

  - `cp a.txt b.txt` 将`a.txt`复制为`b.txt`文件

  - `cp a.txt ../` 将`a.txt`文件复制到上一层目录中

* `mv` 移动或者重命名

  - `mv a.txt ../` 将`a.txt`文件移动到上一层目录中

  - `mv a.txt b.txt` 将`a.txt`文件重命名为`b.txt`

* `tar`打包或解压

  `tar`命令位于`/bin`目录下，它能够将用户所指定的文件或目录<font color = "red">打包成一个文件，但不做压缩。</font>

  一般Linux上常用的压缩方式是选用`tar`将许多文件打包成一个文件，再以`gzip`压缩命令压缩成xxx.tar.gz(或称为`xxx.tgz`)的文件。常用参数：

  - `-c`：创建一个新`tar`文件

  - `-v`：显示运行过程的信息

  - `-f`：指定文件名

  - `-z`：调用`gzip`压缩命令进行压缩

  - `-t`：查看压缩文件的内容

  - `-x`：解开`tar`文件

  1. 打包`tar –cvf xxx.tar ./`
  2. 打包并且压缩：`tar –zcvf xxx.tar.gz ./*`  
  3. 解压  `tar –xvf xxx.tar`     `tar -zxvf xxx.tar.gz -C /usr/aaa`

* `find`查找命令

  `find`指令用于查找符合条件的文件

  - `find / -name “ins*”` 查找文件名称是以ins开头的文件

  - `find / -name “ins*” –ls` 

  - `find / –user itcast –ls` 查找用户itcast的文件

  - `find / –user itcast –type d –ls` 查找用户itcast的目录

  - `find /-perm -777 –type d-ls` 查找权限是777的文件

* `grep`查找字符串

  查找文件里符合条件的字符串。

  用法: `grep [选项]... PATTERN [FILE]...`

  - `grep lang anaconda-ks.cfg` 在文件中查找`lang`

  - `grep lang anaconda-ks.cfg –color` 高亮显示

## 2.6 其他常用命令

- `pwd` 显示当前所在目录
- `touch` 创建一个空文件 `touch a.txt`
- `clear/ crtl + L` 清屏

## 2.7 重定向输出>和>>

`>` 重定向输出，覆盖原有内容；`>>` 重定向输出，有追加功能；

前面的命令没必要必须是查看，只要是能够在控制台输出的命令即可。

- `cat /etc/passwd > a.txt` 将输出定向到a.txt中
- `cat /etc/passwd >> a.txt` 输出并且追加
- `ifconfig > ifconfig.txt` 将控制台输出的IP信息，放到文件里面

## 2.8 系统管理命令

`ps` 正在运行的某个进程的状态

- `ps –ef` 查看所有进程
- `ps –ef | grep '进程的名称'` 查找某一进程
- `kill 2868` 杀掉2868编号的进程
- `kill -9 2868` 强制杀死进程

## 2.9 管道 |

管道是Linux命令中重要的一个概念，其作用是<font color = "red">将一个命令的输出用作另一个命令的输入。</font>

- `ls --help | more` 分页查询帮助信息
- `ps –ef | grep java` 查询名称中包含java的进程
- `ifconfig | more`
- `cat index.html | more`
- `ps –ef | grep aio`

# 第三章Vim编辑器

## 3.1移动

`h j k l`四个按键分别代表左、下、上、右。

<font color="red">**h、j、k、l需要在普通模式才能使用**</font>

## 3.2切换模式

- **进入插入模式**
- **其他进入插入模式方法**
- **回到普通模式**
- **替换模式**
- **命令行模式**
- **可视模式**

VIM具有6种基本模式和5种派生模式。

- 基本模式：普通模式、插入模式、可视模式、选择模式、命令行模式和Ex模式。
- 派生模式：操作符等待模式、插入普通模式、插入可视模式、插入选择模式和替换模式。

**进入插入模式**

在Linux的命令行通过*vi文件名*或*vim文件名*即可<font color="red">用VIM编辑器打开你的文件</font>，打开文件后默认是进入了普通模式，我们可以用上节课跟大家介绍的h、j、k、l四个按键分别左、下、上、右地移动光标。

当光标抵达目标位置之后，敲一下i按键使得VIM切换到插入模式（界面左下角显示--INSERT--字样），此时你可以自由的编辑文档了，就像你在Windows下使用记事本那样去编辑。

**其他进入插入模式方法**

当然，并不只有敲击按键i才能进入插入模式，其实方法多了去了

| **按键** | **含义**                                 |
| -------- | ---------------------------------------- |
| i        | 在光标的前边进入插入模式                 |
| I        | 在光标所在行的行首进入插入模式           |
| a        | 在光标的后边进入插入模式                 |
| A        | 在光标所在行的行尾进入插入模式           |
| o        | 在光标所在行的下方插入空行并进入插入模式 |
| O        | 在光标所在行的上方插入空行并进入插入模式 |
| s        | 删除光标指定的字符并进入插入模式         |
| S        | 将光标所在行清除并进入插入模式           |

> 注意：最后这个大S是清除所在行，而不是删除。清除的意思就是保留行，把内容清空，然后在行首进入插入模式。

**回到普通模式**

回到普通模式，仅需要敲一下键盘左上角的Esc按键即可（界面左下角的--INSERT--标志消失）。

**替换模式**

对于需要替换多个字符，更好的方案是直接进入替换模式。按下大写的`R`键，屏幕左下角出现`--REPLACE--`字样，说明你已经处于替换模式。

此时输入字符可以连续替换光标及其后边的内容。注意：退格键（`Backspace`）在替换模式中被解释为<font color="red">如果左边内容被替换过，则恢复到原来的样子；如果没有被替换过，则简单的向左移动。</font>修改完毕后，按下`Esc`回到普通模式。

![img](https://xxx.ilovefishc.com/forum/201510/28/043031mh4174ga7s3fsxfz.gif)

**命令行模式**

输入冒号（`:`）进入命令行模式

在普通模式下按下斜杠（`/`）也是进入命令行模式，此时该字符和光标均出现在屏幕的底部，这跟冒号（`:`）一样。

在普通模式下按下问号（`?`）也是进入命令行模式，实现的也是搜索功能。

**可视模式**

你可以按一下`v`进入可视模式：左下角出现`--VISUAL--`字样

## 3.3退出VIM

一般退出VIM分两种：一种是保存修改并退出；另一种则是直接退出。无论你选择哪一种退出方式，都先请敲一下Esc按键回到普通模式。

对于第一种（保存修改并退出），这里教大家两招：

- `ZZ`（两个大写字母Z，也就是`Shift+z+z`）
- `:wq`（这是一个组合命令，如果是`:w`表示保存，而`:q`表示退出）

对于第二种（直接退出）则要区分情况：

- 如果你打开文件只是看看，并不做任何改动，那么直接输入`:q`即可
- 如果对文件动过手脚，但不希望保存（放弃修改）而直接退出，则需要再后边加上一个感叹号`:q!`

## 3.4VIM数字和命令

VIM利用数字可以干老多的事儿了。

比如普通模式下按一下`h`按键是将光标向左移动一格，而在其前面输入数字3，即`3h`则是将光标向前移动3格，依此类推`3j`则是将光标向下移动三行，`3w`则是将光标跳到三个单词后的开始位置……

同样的，`d3h`表示删除光标前的3个字符，`d3j`表示删除光标所在行以及下面3行的所有字符，而`d3w`则表示向后删除当前光标到后面第三个单词前的所有字符。

是不是很容易呢？

## 3.5简单的命令

- **删除命令**
- **更多删除命令**
- **撤销和恢复命令**
- **粘贴命令**
- **拷贝命令**
- **简单替换命令**
- **修改命令**
- **搜索命令**
- **高级替换命令**

**删除命令**

可以选择在插入模式中使用退格键（Backspace）或删除键（Delete）来删除光标前面或当前的字符。不过这样做挺麻烦的：因为你需要先通过方向键将光标调整到目标位置才行，`h、j、k、l`需要在普通模式才能使用。

我们曾经说过：”对于真正的VIM用户，插入模式根本不存在“。是的，回到普通模式去吧，那里有更广阔的天地，在那里可以大有作为的！

在普通模式下，删除单一字符可以用x命令。跟在插入模式按下Delete键一样，x命令是删除光标指定的字符。

删除更多字符可以使用d命令！

> **注意：这上面所有的“删除”操作并不是真的删除，它们事实上是存放在VIM的一个缓冲区（VIM把它称之为寄存器）中，相当于Windows的剪切功能！！！**

**更多删除命令**

使用删除操作符`d`的删除命令的格式是`dmotion`

`d`是`Delete`的意思，`motion`即指定要删除的对象，所以这个命令可以很灵活！

`motion`其实是一些表示操作范围的指令，比如：

| **按键** | **含义**                                                     |
| -------- | ------------------------------------------------------------ |
| 0        | 将光标定位到行首的位置                                       |
| ^        | 同上                                                         |
| $        | 将光标定位到行尾的位置                                       |
| b        | 将光标定位到光标所在单词的起始处                             |
| e        | 将光标定位到光标所在单词的结尾处                             |
| w        | 将光标定位到下一个单词的起始处（注意，是光标所在单词的下一个单词噢^_^） |
| gg       | 将光标定位到文件的开头                                       |
| G        | 将光标定位到文件的末尾                                       |

没错，使用上面这几个按键移动光标，比单纯多次点击`h、j、k、l`按键省心省力。

| **按键** | **含义**                                             |
| -------- | ---------------------------------------------------- |
| d0       | 删除光标从当前位置（不包含）到该行行首的所有字符     |
| d^       | 同上                                                 |
| d$       | 删除从光标当前位置（包含）到该行行尾的所有字符       |
| db       | 删除从光标当前位置（不包含）到单词起始处的所有字符   |
| de       | 删除从光标当前位置（包含）到单词结尾处的所有字符     |
| dw       | 删除从光标当前位置（包含）到下个单词起始处的所有字符 |
| dh       | 删除光标前面一个字符                                 |
| dl       | 删除光标指定的字符                                   |
| dj       | 删除光标所在行以及下一行的所有字符                   |
| dk       | 删除光标所在行以及上一行的所有字符                   |
| dd       | 删除光标所在行的所有字符                             |
| dgg      | 删除光标所在行（包含）到文件开头的所有字符           |
| dG       | 删除光标所在行（包含）到文件末尾的所有字符           |

其实你需要练习的就只是几个表示操作范围的命令就可以了，另外还有些规律可循的。

比如`0`和`^`两个是一样的，你随意记住一个即可；`d0`、`db`、`dh`这类往前删除字符的命令，它们是不会删除光标所指定的字符；而删除方向是往后的则相反（`d$`、`dw`、`dl`），会将当前字符也一并删除。

另外`dh`和`dl`两个都只是删除一个字符，而`dj`和`dk`则是一次性删除两行。

**撤销和恢复命令**

小写的`u`表示撤销最后一次修改；大写的`U`表示撤销对整行的修改。

`Ctrl+r`快捷键可以恢复撤销的内容！！

**粘贴命令**

使用`p`命令可以将最后一次删除的内容粘贴到光标之后。大写的`P`则是粘贴到光标之前。

先使用`dd`命令将当前行删除，然后再使用`p`命令就可以粘贴了

![img](D:\Java\笔记\图片\vim\043037k12z772bh5ox1ul2.gif)

> **注意：如果你需要粘贴的是整行为单位，那么p命令将在光标的下一行开始粘贴；如果你拷贝的是非整行的局部字符串，那么p命令将在光标后开始粘贴。**

**拷贝命令**

VIM用`y`命令实现拷贝。语法跟删除的`d`命令一样：`ymotion`

其中的`motion`同样是用来表示操作范围的指令，即`yy`表示拷贝当前行，`3yy`则表示拷贝3行；`y$`表示从光标所在的位置拷贝到行尾的所有字符；`yG`则表示从光标所在行拷贝到文件末尾行的所有字符。

`motion`是一些表示操作范围的指令，比如：

| **按键** | **含义**                                                     |
| -------- | ------------------------------------------------------------ |
| 0        | 将光标定位到行首的位置                                       |
| ^        | 同上                                                         |
| $        | 将光标定位到行尾的位置                                       |
| b        | 将光标定位到光标所在单词的起始处                             |
| e        | 将光标定位到光标所在单词的结尾处                             |
| w        | 将光标定位到下一个单词的起始处（注意，是光标所在单词的下一个单词噢^_^） |
| gg       | 将光标定位到文件的开头                                       |
| G        | 将光标定位到文件的末尾                                       |

拷贝完成之后同样使用`p`命令进行粘贴，没什么好说的。

**简单替换命令**

VIM还提供了一个简单的替换命令：`r`命令。

`r`用于替换光标所在的字符，做法是先将光标移动到需要替换的字符处，按一下`r`键，然后输入新的字符。<font color="red">**注意，全程无需进入插入模式，也不会进入插入模式。**</font>

数字+r可能会产生一些物理反应。先输入【数字】再输入r，最后输入新字符，说明从当前光标的位置开始，替换【数字】个新字符。

举个栗子：

- 第一行，先将光标移动到字符A处，按下r键，再按下C；
- 第二行，先将光标移动到第一个字符B出，按下3r键，再按下C。

![img](D:\Java\笔记\图片\vim\043035ab0r4afle9yozem0.gif)



**修改命令**

<font color="red">**修改跟替换是不一样的！修改会进入插入模式，替换不会进入插入模式！**</font>

修改命令我们使用`c`键来启动，格式是：`c[number]motion`

没错，`motion`依然表示范围，[数字]依然拥有奥义，同样是可选的，加上数字表示重复执行多次`motion`范围……

`motion`是一些表示操作范围的指令，比如：

| **按键** | **含义**                                                     |
| -------- | ------------------------------------------------------------ |
| 0        | 将光标定位到行首的位置                                       |
| ^        | 同上                                                         |
| $        | 将光标定位到行尾的位置                                       |
| b        | 将光标定位到光标所在单词的起始处                             |
| e        | 将光标定位到光标所在单词的结尾处                             |
| w        | 将光标定位到下一个单词的起始处（注意，是光标所在单词的下一个单词噢^_^） |
| gg       | 将光标定位到文件的开头                                       |
| G        | 将光标定位到文件的末尾                                       |

比如`cw`是修改光标指定单词的内容（VIM的做法就是删除当前光标位置到下个单词前的所有字符，并进入插入模式）；而`c2w`便是修改当前光标指定的单词以及下一个单词共计两个单词的内容……

| **按键** | **含义**                                                     |
| -------- | ------------------------------------------------------------ |
| c0       | 删除光标从当前位置（不包含）到该行行首的所有字符，并进入插入模式 |
| c^       | 同上                                                         |
| c$       | 删除从光标当前位置（包含）到该行行尾的所有字符，并进入插入模式 |
| cb       | 删除从光标当前位置（不包含）到单词起始处的所有字符，并进入插入模式 |
| ce       | 删除从光标当前位置（包含）到单词结尾处的所有字符，并进入插入模式 |
| cw       | 删除从光标当前位置（包含）到下个单词起始处的所有字符，并进入插入模式 |
| ch       | 删除光标前边一个字符，并进入插入模式                         |
| cl       | 删除光标指定的字符，并进入插入模式                           |
| cj       | 删除光标所在行以及下一行的所有字符，并在光标下一行进入插入模式 |
| ck       | 删除光标所在行以及上一行的所有字符，并在光标下一行进入插入模式 |
| cc       | 删除光标所在行的字符，并进入插入模式                         |

**搜索命令**

VIM的查找是从按下斜杠（`/`）那一刻开始的……

在普通模式下按下斜杠（`/`）也是进入命令行模式，此时该字符和光标均出现在屏幕的底部，这跟冒号（`:`）一样。

紧挨着斜杠（`/`）的是搜索目标，比如`/love`，说明你找的是`love`这个字符串在光标后边第一次出现的位置，当然你也可以输入中文，比如`/你瞅啥`

那如果要找下一个目标怎么办？这时你只需按`n`键即可定位到下一个符合的目标（向下查找），而按`N`键则返回上一个（向上查找）。

> 注意：第一个搜索到的目标不是文件中的第一个目标，而是从你的光标所在处开始找到的那个目标。所以你如果想要搜索文件中第一个匹配的目标，你应该先gg将光标移动到文件头，然后再使用搜索命令。

在普通模式下按下问号（`?`）也是进入命令行模式，实现的也是搜索功能。不过这回它是反过来的，你可以认为它是斜杠（`/`）功能的“反面派”。

前边说过`/FishC`是从光标位置向后开始搜索`FishC`这个字符串，而`?FishC`则相反，是从光标位置向前开始搜索。

|              | **/目标**      | **?目标**      |
| ------------ | -------------- | -------------- |
| **搜索方向** | 从光标位置向后 | 从光标位置向前 |
| **n**        | 向后搜索下一个 | 向前搜索下一个 |
| **N**        | 向前搜索下一个 | 向后搜索下一个 |

当搜索到了文件的末尾（`/目标`）或开头（`?目标`），页面下方面会显示`“SearchhitBOTTOM,continuingatTOP”`或`“SearchhitTOP,continuingatBOTTOM”`的字样。

表示一轮搜索到文件尾/头了，搜索下一个就是从文件头/尾开始咯。



![img](D:\Java\笔记\图片\vim\033625hda2d4622isdeuck.png)

VIM会自动高亮所有匹配的目标，即使在你找到目标之后，它们仍然高傲的亮着！如何取消呢？输入冒号（`:`）进入命令行模式，然后输入`nohl`即可。

> 最后提个醒：在搜索命令中，`.`、`*`、`[`、`]`、`^`、`%`、`/`、`?`、`~`和`$`这10个字符有着特殊意义，所以在使用这些字符的时候要在前面加上一个反斜杠（`\`），比如你要搜索问号，则输入`/\?`

**高级替换命令**

搜索在很多情况下都是为了替换。通过搜索功能，我们将光标定位到目标位置，如果你确定这个目标是可恶的，需要被替换的，你可以输入`:s/old/new`，这样即可将光标所在行的第一个`old`替换为`new`；你如果输入的是`:s/old/new/g`，则表示将光标所在行的所有`old`替换为`new`。

但如果要替换整个文件的所有匹配字符串怎么办？总不能每一行来一下吧？
只要你能想到的，VIM就有办法！输入`:%s/old/new/g`表示替换整个文件中每个匹配的字符串。

噢，你没有太大的把握，希望VIM在每次替换前都咨询一下你：“亲，我准备替换XX了，你确定要将XX替换成OO吗？”
可以，输入`:%s/old/new/gc`

![img](D:\Java\笔记\图片\vim\033626zihhmpltpmwmzhfw.png)

看到页面下方的(`y/n/a/q/l/^E/^Y`)了吗？这是VIM在咨询您的意见呢！

- `y`表示替换
- `n`表示不替换
- `a`表示替换所有
- `q`表示放弃替换
- `l`表示替换第一个并进入插入模式
- `^E`表示用`Ctrl+e`来滚动屏幕
- `^Y`表示用`Ctrl+y`来滚动屏幕

好吧，你可能会问“如果我只想替换第5行到第13行之间的所有XX，可以吗？”

当然可以啦，使用`:5,13s/old/new/g`即可胜任你的要求。5表示开始替换的行号，13表示结束替换的行号。

![img](D:\Java\笔记\图片\vim\033626fp5pefvbhj59npss.png)

## 3.6有趣的命令

- **查看文件信息**
- **跳转**
- **定位括号**
- **缩进**

**查看文件信息**

快捷键`ctrl+g`可以查看当前的文件信息，比如文件名，文件状态，文件的总行数，以及光标所在的相对位置。

![img](D:\Java\笔记\图片\vim\232631xzmv2mjpv9ii4vao.png)

> 注意：光标在文件中的相对位置是用百分数来显示的，那如果想知道光标具体的位置呢？看到还有个5,1了吗？那个是原来就有的，表示光标当前的位置（行，列）

**跳转**

在VIM有两种方式可以将光标跳转到指定的位置：

- `行号+G`
- `:行号`

如果想要跳转到333行的位置，那么就有两种方法：

- 输入数字333，再输入大写字母`G`。
- 输入冒号（`:`）进入命令行模式，再输入数字333，最后回车，也可以跳转到目的地。

> TIPS：如果单独输入G键（前边没有输入数字），那么光标是直接去到文件的最后一行；如果输入两个小写g，即gg，则将光标跳转到文件的第一行。

**定位括号**

VIM有个键可以帮你快速定位到另一半括号，就是`%`键。将光标移动到`()`，`[]`，`{}`，中的任何一半括号上，按下`%`键，便可看到此时光标已经跳转到另外一半的括号上了。

在程序调试时，这个功能用来查找不配对的括号是很有用的。因为有时候你代码删删删，括号一对给你删了一半，剩下一半落在那里，编译自然就会报错……此时你在落单的那一半括号上使用%键，你会发现VIM根本不鸟你，因为它找不到另一半了……

> 注意：别拿书名号`《》`什么的来开玩笑，`%`无法识别它们，因为VIM是为了编程而生的！

**缩进**

VIM可以使用尖括号（`<`或`>`）来控制缩进，我们常用的就是两个同方向的尖括号表示将光标所在的语句进行缩进和反缩进操作。很明显`>>`表示缩进，而`<<`则表示反缩进。

我们也可以使用数字加上快捷键命令：先输入数字2在输入`>>`表示将光标所在行以及下一行共两行同时插入一个缩进。

不过行数一多……到底要缩进多少行就成了一个问题……

这时，你可以按一下`v`进入可视模式（左下角出现`--VISUAL--`字样），然后通过`h、j、k、l`或其他`motion`来移动你的光标，此时光标所到之处必被一道亮光所包围（表示被选中），选择好需要缩进的目标后，只需按一下`>`即可完成任务。

![img](D:\Java\笔记\图片\vim\162556ihjb5obhtgf5s6jm.png)



## 3.7关于文件的命令

- **执行shell命令**
- **文件另存为**
- **局部内容另存为**
- **合并文件**
- **打开多个文件**

**执行shell命令**

运行在命令行下的VIM，貌似不可能“最小化”，这就有诸多不方便的地方了。比如有时我需要知道某个路径下有哪些文件（我的程序需要调用它们），那我可能就需要先关闭VIM，然后查看有哪些文件，最后重新打开VIM。

这就显得有点折腾了，不符合VIM一直强调的高效作风！因此，伟大的VIM作者（BramMoolenaar）高呼一声“我有办法！”，于是祭出了一个感叹号（`!`）

比如你想知道根目录（`/`）下面有哪些目录和文件，可以在VIM中输入`:!ls/`

然后敲一下回车

即可显示根目录（`/`）下边有哪些目录和文件：

![img](D:\Java\笔记\图片\vim\045629bve0pcbifu68vz9w.png)

总之，在输入冒号（`:`）进入命令行模式，输入感叹号（`!`），在其后便可以加上shell命令。此后VIM将临时跳转回shell，并执行命令。再次按下Enter键回到VIM。

**文件另存为**

一般的文本编辑工具都会有“另存为”的功能，用于将文件重新存放到一个新的文件中（旧文件保留不变）。VIM也可以这么干，做法是输入`:w新文件名`

下边我用VIM打开的是test文件，然后我输入`:wtest2`

![img](D:\Java\笔记\图片\vim\045629gs6sqaupuakdqsps.gif)

该命令会以`test2`为文件名拷贝保存整个`test`文件。

**局部内容另存为**

VIM除了支持文件另存为之外，还支持另一种新技能：文件局部另存为。言下之意就是，VIM可以将文件中的局部文本另存为一个新的文件，厉害吧？！

在普通模式中按下V键即进入可视模式，进入后左下角显示`--VISUAL--`

此时光标的位置开始为选中状态，你可以通过任何移动或范围的按键来移动光标，光标所到之处皆为选中状态（h、j、k、l移动光标，$去到行尾，0去到行首……）：

![img](D:\Java\笔记\图片\vim\045630tyb1fajeayuqjt64.png)

选好范围之后的操作就跟“文件另存为”一样了。

按下冒号（`:`）屏幕左下方出现`:'<,'>`，现在请输入`wtest2`。表示新建一个test2文件，并将选中的内容单独存放进去。

这里有个问题，如果路径中已经存在`test2`文件，那么VIM会提醒你需要加感叹号（`!`）才能强制覆盖文件。即输入`w!test2`

**合并文件**

所谓合并文件，便是在VIM打开的一个文件中读取并置入另一个文件。怎么样，光听着就觉得炫酷吧？！

命令很简单，只需要输入冒号（`:`）进入命令行模式，然后输入`r`文件名

即可将指定文件的内容读取并置入到光标的下一行中。

![img](D:\Java\笔记\图片\vim\045630eyqq1ozgo311g1tf.png)

**打开多个文件**

VIM还可以同时打开多个文件，并且允许你通过水平或垂直的方式并排它们。

VIM使用`–o`或`–O`选项打开多个文件，其中–o表示垂直并排，例如`vim-olesson4lesson5lesson6`

![img](D:\Java\笔记\图片\vim\143428h5h1ihqd3p5zddeq.png)

`-O`表示水平并排，例如`vim-Olesson4lesson5lesson6`

![img](D:\Java\笔记\图片\vim\143550dml3i8xomcc5oiis.png)

打开后默认光标是落在第一个文件中的，此时之前学过的所有命令都可以上，不过仅限于第一个文件。那如何将焦点（光标）切换到另一个文件中呢？

很简单，使用`ctrl+w+w`将光标切换到下一个文件；或者使用`ctrl+w+方向（方向键或h、j、k、l）`。

- 对于垂直并排的文件：使用`ctrl+w+上、下方向`，表示上、下切换文件；
- 对于水平并排的文件：使用`ctrl+w+左、右方向`，表示左、右切换文件。


不错吧，这样搭配宽屏显示器或者多个显示器，逼格简直爆表！

退出文件的话可以使用原来的`q`、`q!`、`wq`或者`ZZ（shirt+z+z）`。

> 但你肯定会发现你如果同时打开三四个文件，这样子却不得不退出三四次才行。那么我们只需在原来退出命令的后边加上小写a，则表示退出动作是针对所有的（ALL）：qa、qa!、wqa

# 第四章 Linux的权限命令

![](D:\Java\笔记\图片\3-day31【Linux】\clip_image004.png)

在权限里面一共是有四个部分的，第一个部分是1个字符，第二个第三个第四个部分是3个字符，一共由十个字符代替。如上图。

![](D:\Java\笔记\图片\3-day31【Linux】\clip_image006.png)

## 4.1 文件权限

- `r`：读，`read`。对文件是指可读取内容 对目录是可以`ls`，**代表数字是4**。
- `w`：写，`write`。对文件是指可修改文件内容，对目录 是指可以在其中创建或删除子节点(目录或文件)，**代表数字是2**。<font color = "red">如果用户没有写入的权限，那么使用vim将无法进行编辑，无法进入插入模式。</font>
- `x`：执行，`excute`。对文件是指是否可以运行这个文件，对目录是指是否可以`cd`进入这个目录，**代表数字是1**。

## 4.2 Linux三种文件类型

- 普通文件： 包括文本文件、数据文件、可执行的二进制程序文件等。 
- 目录文件： Linux系统把目录看成是一种特殊的文件，利用它构成文件系统的树型结构。 
- 设备文件： Linux系统把每一个设备都看成是一个文件

## 4.3 文件类型标识

普通文件（`-`）目录（`d`）符号链接（`l`）

* 进入`etc`可以查看，相当于快捷方式字符设备文件（c）块设备文件（s）套接字（s）命名管道（p）

## 4.4 文件权限管理

  `chmod` 变更文件或目录的权限。

* `chmod u=rwx,g=rx,o=rx a.txt`：对a.txt的权限进行修改

  u是当前的用户，g是当前组内其他用户，o是其他组内用户。

上述操作需要记忆字母，所以我们可以使用数字相加来帮忙。

<font color = "red">u == 4；g == 2；o == 1；相加起来为一组的权限。</font>

- `chmod 755 a.txt` 

  

# 第五章 Linux上常用网络操作

这些都是Centos6的命令，有的对于Centos7不适用

## 5.1 主机名配置

- `hostname` 查看主机名

- `hostname xxx` 修改主机名 重启后无效

  如果想要永久生效，可以修改`/etc/sysconfig/network`文件

## 5.2 IP地址配置

`ifconfig 查看(修改)ip地址(重启后无效)`

`ifconfig eth0 192.168.12.22 修改ip地址`

如果想要永久生效，修改 `/etc/sysconfig/network-scripts/ifcfg-eth0`文件

Centos7是使用`ip addr`来查看IP地址

```ABAP
DEVICE=eth0 #网卡名称
BOOTPROTO=static #获取ip的方式(static/dhcp/bootp/none)
HWADDR=00:0C:29:B5:B2:69 #MAC地址
IPADDR=12.168.177.129 #IP地址
NETMASK=255.255.255.0 #子网掩码
NETWORK=192.168.177.0 #网络地址
BROADCAST=192.168.0.255 #广播地址
NBOOT=yes #  系统启动时是否设置此网络接口，设置为yes时，系统启动时激活此设备。
```

## 5.3 域名映射

`/etc/hosts`文件用于在通过主机名进行访问时做ip地址解析之用,相当于windows系统的`C:\Windows\System32\drivers\etc\hosts`文件的功能

![](D:\Java\笔记\图片\3-day31【Linux】\clip_image005.png)

就是我们起了一个别的名字，`127.0.0.1 == localhost`。

## 5.4 网络服务管理

- `service network status` 查看指定服务的状态

- `service network stop` 停止指定服务

- `service network start` 启动指定服务

- `service network restart` 重启指定服务
- `service --status–all` 查看系统中所有后台服务

- `netstat –nltp` 查看系统中网络进程的端口监听情况

**防火墙设置**

防火墙根据配置文件`/etc/sysconfig/iptables`来控制本机的”出”、”入”网络访问行为。<font color = "red">这是Centos6的</font>

- `service iptables status` 查看防火墙状态

- `service iptables stop` 关闭防火墙

- `service iptables start` 启动防火墙

- `chkconfig iptables off` 禁止防火墙自启

# 第六章 Linux上软件安装

Linux上的软件安装有以下几种常见方式介绍

1. 二进制发布包

   软件已经针对具体平台编译打包发布，只要解压，修改配置即可

2. RPM包

   软件已经按照redhat的包管理工具规范RPM进行打包发布，需要获取到相应的软件RPM发布包，然后用RPM命令进行安装

3. Yum在线安装

   软件已经以RPM规范打包，但发布在了网络上的一些服务器上，可用yum在线安装服务器上的rpm软件，并且会自动解决软件安装过程中的库依赖问题

4. 源码编译安装

   软件以源码工程的形式发布，需要获取到源码工程后用相应开发工具进行编译打包部署。

上传与下载工具介绍

1. `FileZilla`

2. `lrzsz`

   我们可以使用yum安装方式安装 yum install lrzsz

   注意：必须有网络

   可以在crt中设置上传与下载目录

3. `sftp`

   使用alt + p 组合键打开sftp窗口

   使用put 命令上传

   使用get命令下载

# 第七章 Nginx

![](D:\Java\笔记\图片\3-day31【Linux】\屏幕截图 2022-04-21 081544.png)

## 7.1 Nginx基础

Nginx 是一款高性能的 http 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。由俄罗斯的程序设计师伊戈尔·西索夫（Igor Sysoev）所开发，官方测试 nginx 能够支支撑 5 万并发链接，并且 cpu、内存等资源消耗却非常低，运行非常稳定。

**Nginx** **应用场景：**

1. http 服务器。Nginx 是一个 http 服务可以独立提供 http 服务。可以做网页静态服务器。
2. 虚拟主机。可以实现在一台服务器虚拟出多个网站。例如个人网站使用的虚拟主机。
3. 反向代理，负载均衡。当网站的访问量达到一定程度后，单台服务器不能满足用户的请求时，需要用多台服务器集群可以使用 nginx 做反向代理。并且多台服务器可以平均分担负载，不会因为某台服务器负载高宕机而某台服务器闲置的情况。 

## 7.2 Nginx在Linux下的安装

1. 第一步：把 nginx 的源码包`nginx-1.8.0.tar.gz`上传到 linux 系统

   老师给我们提供好了，这时候我们需要使用`rz`命令来讲文件弄到Centos上面。

2. 第二步：解压缩     `tar zxvf nginx-1.8.0.tar.gz`  

3. 第三步：进入`nginx-1.8.0`目录  使用 configure 命令创建一 makeFile 文件。

```asciiarmor
./configure \
--prefix=/usr/local/nginx \
--pid-path=/var/run/nginx/nginx.pid \
--lock-path=/var/lock/nginx.lock \
--error-log-path=/var/log/nginx/error.log \
--http-log-path=/var/log/nginx/access.log \
--with-http_gzip_static_module \
--http-client-body-temp-path=/var/temp/nginx/client \
--http-proxy-temp-path=/var/temp/nginx/proxy \
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
--http-scgi-temp-path=/var/temp/nginx/scgi
```

执行后可以看到Makefile文件，`ll`命令可以查看已经有该文件了。

>  **----** **知识点小贴士** **----**  
>
> Makefile是一种配置文件， Makefile 一个工程中的源文件不计数，其按类型、功能、模块分别放在若干个目录中，makefile定义了一系列的规则来指定，哪些文件需要先编译，哪些文件需要后编译，哪些文件需要重新编译，甚至于进行更复杂的功能操作，因为 makefile就像一个Shell脚本一样，其中也可以执行操作系统的命令。  
>
> configure参数  
>
> ./configure \  
>
> --prefix=/usr  \                              指向安装目录  
>
> --sbin-path=/usr/sbin/nginx  \                  指向（执行）程序文件（nginx）  
>
> --conf-path=/etc/nginx/nginx.conf  \           指向配置文件  
>
> --error-log-path=/var/log/nginx/error.log  \         指向log  
>
> --http-log-path=/var/log/nginx/access.log  \      指向http-log  
>
> --pid-path=/var/run/nginx/nginx.pid  \             指向pid  
>
> --lock-path=/var/lock/nginx.lock  \              （安装文件锁定，防止安装文件被别人利用，或自己误操作。）  
>
> --user=nginx \  
>
> --group=nginx \  
>
> --with-http_ssl_module  \             启用ngx_http_ssl_module支持（使支持https请求，需已安装openssl）  
>
> --with-http_flv_module  \             启用ngx_http_flv_module支持（提供寻求内存使用基于时间的偏移量文件）  
>
> --with-http_stub_status_module  \   启用ngx_http_stub_status_module支持（获取nginx自上次启动以来的工作状态）  
>
> --with-http_gzip_static_module  \  启用ngx_http_gzip_static_module支持（在线实时压缩输出数据流）  
>
> --http-client-body-temp-path=/var/tmp/nginx/client/  \ 设定http客户端请求临时文件路径  
>
> --http-proxy-temp-path=/var/tmp/nginx/proxy/  \ 设定http代理临时文件路径  
>
> --http-fastcgi-temp-path=/var/tmp/nginx/fcgi/  \ 设定http fastcgi临时文件路径  
>
> --http-uwsgi-temp-path=/var/tmp/nginx/uwsgi  \ 设定http uwsgi临时文件路径  
>
> --http-scgi-temp-path=/var/tmp/nginx/scgi  \ 设定http scgi临时文件路径 
>
>  --with-pcre 启用pcre库  

4. 第四步：编译     `make`  

5. 第五步：安装      `make install`  

## 7.3 Nginx启动与访问

注意：启动nginx 之前，需要将临时文件目录指定为`/var/temp/nginx/client`， 所以我们需要在`/var` 下创建此 目录

  `mkdir /var/temp/nginx/client -p`  

* 进入到Nginx目录下的sbin目录   `cd /usr/local/ngiux/sbin`

* 输入命令启动Nginx     `./nginx`  

* 启动后查看进程      `ps aux|grep nginx`     

地址栏输入虚拟机的IP即可访问（默认为80端口），<font color = "red">记住关闭防火墙</font>

* 关闭 nginx：   `./nginx -s stop`    或者     `./nginx -s quit`

* 重启 nginx：先关闭后启动。     刷新配置文件：  `./nginx -s reload`  

## 7.4 Nginx静态网站部署

将 `/资料/静态页面/index` 目录下的所有内容 上传到服务器的`/usr/local/nginx/html`下即可访问

## 7.5 配置虚拟主机

虚拟主机，也叫“网站空间”，就是把一台运行在互联网上的物理服务器划分成多个“虚拟”服务器。虚拟主机技术极大的促进了网络技术的应用和普及。同时虚拟主机的租用服务也成了网络时代的一种新型经济形式。

### 端口绑定

1. 上传静态网站：

   将/资料/静态页面/index目录上传至 `/usr/local/nginx/index`下

   将/资料/静态页面/regist目录上传至 `/usr/local/nginx/regist`下

2. 修改Nginx 的配置文件：/usr/local/nginx/conf/nginx.conf

   ```apl
   	server {
           listen       81; # 监听的端口
           server_name  localhost; # 域名或ip
           location / {	# 访问路径配置
               root   index;# 根目录
               index  index.html index.htm; # 默认首页
           }
           error_page   500 502 503 504  /50x.html;	# 错误页面
           location = /50x.html {
               root   html;
           }
       }
   
   
        server {
           listen       82; # 监听的端口
           server_name  localhost; # 域名或ip
           location / {	# 访问路径配置
               root   regist;# 根目录
               index  regist.html; # 默认首页
           }
           error_page   500 502 503 504  /50x.html;	# 错误页面
           location = /50x.html {
               root   html;
           }    
       }
   ```

   3. 访问测试：

      地址栏输入`http://IP/:81` 可以看到首页面

      地址栏输入`http://IP/:82` 可以看到注册页面


### 域名绑定

**什么是域名**

域名（Domain Name），是由一串用“点”分隔的字符组成的Internet上某一台计算机或计算机组的名称，用于在数据传输时标识计算机的电子方位（有时也指地理位置，地理上的域名，指代有行政自主权的一个地方区域）。

* 域名是一个IP地址上有“面具” 。
* 域名的目的是便于记忆和沟通的一组服务器的地址（网站，电子邮件，FTP等）。
* 域名作为力所能及难忘的互联网参与者的名称。
* 域名按域名系统（DNS）的规则流程组成。
* 在DNS中注册的任何名称都是域名。
* 域名用于各种网络环境和应用程序特定的命名和寻址目的。
* 通常，域名表示互联网协议（IP）资源，例如用于访问因特网的个人计算机，托管网站的服务器计算机，或网站本身或通过因特网传送的任何其他服务。世界上第一个注册的域名是在1985年1月注册的。

**域名级别**

* 顶级域名

  顶级域名又分为两类：

  1. 一是国家顶级域名（national top-level domainnames，简称nTLDs），200多个国家都按照ISO3166国家代码分配了顶级域名，例如中国是cn，美国是us，日本是jp等；

  2. 二是国际顶级域名（international top-level domain names，简称iTDs），例如表示工商企业的 .Com .Top，表示网络提供商的.net，表示非盈利组织的.org，表示教育的.edu，以及没有限制的中性域名如.xyz等。

     大多数域名争议都发生在com的顶级域名下，因为多数公司上网的目的都是为了赢利。但因为自2014年以来新顶级域名的发展，域名争议案件数量增长幅度越来越大[5] 。

     为加强域名管理，解决域名资源的紧张，Internet协会、Internet分址机构及世界知识产权组织（WIPO）等国际组织经过广泛协商， 在原来三个国际通用顶级域名：（com）的基础上，新增加了7个国际通用顶级域名：firm（公司企业）、store（销售公司或企业）、Web（突出WWW活动的单位）、arts（突出文化、娱乐活动的单位）、rec (突出消遣、娱乐活动的单位）、info(提供信息服务的单位）、nom(个人），并在世界范围内选择新的注册机构来受理域名注册申请。

* 二级域名

  二级域名是指顶级域名之下的域名，在国际顶级域名下，它是指域名注册人的网上名称，例如 ibm，yahoo，microsoft等；在国家顶级域名下，它是表示注册企业类别的符号，例如.top，com，edu，gov，net等。

  中国在国际互联网络信息中心（Inter NIC） 正式注册并运行的顶级域名是CN，这也是中国的一级域名。在顶级域名之下，中国的二级域名又分为类别域名和行政区域名两类。类别域名共7个， 包括用于科研机构的ac；用于工商金融企业的com、top；用于教育机构的edu；用于政府部门的 gov；用于互联网络信息中心和运行中心的net；用于非盈利组织的org。而行政区域名有34个，分别对应于中国各省、自治区和直辖市。

* 三级域名

  三级域名用字母（ A～Z，a～z，大小写等）、数字（0～9）和连接符（－）组成， 各级域名之间用实点（.）连接，三级域名的长度不能超过20个字符。

  如无特殊原因，建议采用申请人的英文名（或者缩写）或者汉语拼音名 （或者缩写） 作为三级域名，以保持域名的清晰性和简洁性。

**域名与IP绑定**

一个域名对应一个 ip 地址，一个 ip 地址可以被多个域名绑定。

本地测试可以修改 hosts 文件（C:\Windows\System32\drivers\etc）

可以配置域名和 ip 的映射关系，如果 hosts 文件中配置了域名和 ip 的对应关系，不需要走dns 服务器。 

做好域名指向后，修改nginx配置文件

```apl
    server {
        listen       80;
        server_name  www.hmtravel.com;
        location / {
            root   cart;
            index  cart.html;
        }
    }
    server {
        listen       80;
        server_name  regist.hmtravel.com;
        location / {
            root   search;
            index  search.html;
        }
    }

```

执行以下命令，刷新配置

```asciiarmor
[root@localhost sbin]# ./nginx -s reload
```

测试：

```apl
地址栏输入http://www.hmtravel.com/
```

## 7.6 反向代理

**什么是反向代理**

反向代理（Reverse Proxy）方式是指以代理服务器来接受internet上的连接请求，然后将请求转发给内部网络上的服务器，并将从服务器上得到的结果返回给internet上请求连接的客户端，此时代理服务器对外就表现为一个反向代理服务器。

首先我们先理解正向代理，如下图：

![](D:\Java\笔记\图片\3-day31【Linux】\Nginx1.png)

正向代理是针对你的客户端，而反向代理是针对服务器的，如下图

![](D:\Java\笔记\图片\3-day31【Linux】\Nginx2.png)

![](D:\Java\笔记\图片\3-day31【Linux】\Nginx3.png)

**配置反向代理-准备工作**

1. 将travel案例部署到tomcat中（ROOT目录），上传到服务器。

2. 启动TOMCAT，输入网址`http://IP:8080` 可以看到网站首页

**配置反向代理**

1. 在Nginx主机修改 Nginx配置文件

   ```apl
      upstream tomcat-travel{
   	   server 192.168.177.129:8080;
       }
   
       server {
           listen       80; # 监听的端口
           server_name  www.hmtravel.com; # 域名或ip
           location / {	# 访问路径配置
               # root   index;# 根目录
   	    proxy_pass http://tomcat-travel;
               index  index.html index.htm; # 默认首页
           }
   }
   
   ```

2. 重新启动Nginx 然后用浏览器测试：http://www.hmtravel.com  （此域名须配置域名指向）

## 7.7 负载均衡

**什么是负载均衡**

负载均衡 建立在现有网络结构之上，它提供了一种廉价有效透明的方法扩展网络设备和服务器的带宽、增加吞吐量、加强网络数据处理能力、提高网络的灵活性和可用性。
负载均衡，英文名称为Load Balance，其意思就是分摊到多个操作单元上进行执行，例如Web服务器、FTP服务器、企业关键应用服务器和其它关键任务服务器等，从而共同完成工作任务。

**配置负载均衡-准备工作**

1. 将刚才的存放工程的tomcat复制三份，修改端口分别为8080 ，8081，8082 。

2. 分别启动这三个tomcat服务。

3. 为了能够区分是访问哪个服务器的网站，可以在首页标题加上标记以便区分。

**配置负载均衡**

修改 Nginx配置文件：

```apl
  upstream tomcat-travel {
	   server 192.168.177.129:8080;
	   server 192.168.177.129:8081;
	   server 192.168.177.129:8082;
    }

    server {
        listen       80; # 监听的端口
        server_name  www.hmtravel.com; # 域名或ip
        location / {	# 访问路径配置
            # root   index;# 根目录
	    proxy_pass http://tomcat-travel;

            index  index.html index.htm; # 默认首页
        }
        error_page   500 502 503 504  /50x.html;	# 错误页面
        location = /50x.html {
            root   html;
        }
    }
```

地址栏输入http:// www.hmtravel.com / 刷新观察每个网页的标题，看是否不同。

经过测试，三台服务器出现的概率各为33.3333333%，交替显示。

如果其中一台服务器性能比较好，想让其承担更多的压力，可以设置权重。

比如想让NO.1出现次数是其它服务器的2倍，则修改配置如下：

```apl
    upstream tomcat-travel {
	   server 192.168.177.129:8080;
	   server 192.168.177.129:8081 weight=2;
	   server 192.168.177.129:8082;
    }
```

经过测试，每刷新四次，有两次是8081

 

 
