![](..\图片\2-31【Linux】\1.png)

# 第一章 Linux文件系统

操作系统中负责管理和存储文件信息的软件机构称为文件管理系统，简称文件系统；文件系统的结构通常叫做目录树结构，从斜杠/根目录开始；

Linux号称“万物皆文件”，意味着针对Linux的操作，大多数是在针对Linux文件系统操作。

![](D:\Java\笔记\图片\2-31【Linux】\2-1Linux文件系统.png)

| 目录        | 作用                                                         |
| ----------- | ------------------------------------------------------------ |
| /bin        | 存放二进制可执行文件(`ls`,`cat`,`mkdir`等)，常用命令一般都在这里。 |
| /boot       | 存放用于系统引导时使用的各种文件                             |
| /dev        | 用于存放设备文件。                                           |
| /etc        | 存放系统管理和配置文件                                       |
| /home       | 存放所有用户文件的根目录，是用户主目录的基点，比如用户user的主目录就是`/home/user`，可以用`~user`表示 |
| /lib        | 存放跟文件系统中的程序运行所需要的共享库及内核模块。共享库又叫动态链接共享库，作用类似windows里的`.dll`文件，存放了根文件系统程序运行所需的共享文件。 |
| /mnt        | 系统管理员安装临时文件系统的安装点，系统提供这个目录是让用户临时挂载其他的文件系统。 |
| /opt        | 额外安装的可选应用程序包所放置的位置。一般情况下，我们可以把tomcat等都安装到这里。 |
| /root       | 超级用户（系统管理员）的主目录（特权阶级）                   |
| /sbin       | 存放二进制可执行文件，只有`root`才能访问。这里存放的是系统管理员使用的系统级别的管理命令和程序。如`ifconfig`等。 |
| /tmp        | 用于存放各种临时文件，是公用的临时文件存储点。               |
| /usr        | 用于存放系统应用程序，比较重要的目录`/usr/local`本地系统管理员软件安装目录（安装系统级的应用）。这是最庞大的目录，要用到的应用程序和文件几乎都在这个目录。 |
| /var        | 用于存放运行时需要改变数据的文件，也是某些大文件的溢出区，比方说各种服务的日志文件（系统启动日志等。）等。 |
| /proc       | 虚拟文件系统目录，是系统内存的映射。可直接访问这个目录来获取系统信息。 |
| /lost+found | 这个目录平时是空的，系统非正常关机而留下“无家可归”的文件（windows下叫什么`.chk`）就在这里 |

几乎主流的文件系统都是从/根目录开始的，Linux也不例外，而windows文件系统会以盘符来区分不同文件系统；

目录树中节点分为两个种类：目录（directory）、文件（file）。从根目录开始，路径具有唯一性；只有在目录下才可以继续创建下一级目录，换句话说目录树到文件终止蔓延。

Linux中三种文件类型：

- 普通文件： 包括文本文件、数据文件、可执行的二进制程序文件等。 
- 目录文件： Linux系统把目录看成是一种特殊的文件，利用它构成文件系统的树型结构。 
- 设备文件： Linux系统把每一个设备都看成是一个文件

## 1.1 路径概念

- 当前路径：也叫当前工作目录，当下用户所属的位置；
- 相对路径：相对当前工作目录开始的路径，会随当前路径变化而变化；
- 绝对路径：不管工作目录在哪，绝对路径都是从/根目录开始，唯一不重复。

## 1.2 特殊符号

- `.` 目录或者文件名字以.开始表示是隐藏的文件，如果路径以.开始表示当前路径
-  `..` 当前目录的上一级目录
-  `~` 当前用户的home目录，比如root用户home目录是/root
-  `/` 根目录

## 1.3 文件权限

在权限里面一共是有四个部分的，第一个部分是1个字符，第二个第三个第四个部分是3个字符，一共由十个字符代替。

1位表示文件类型；2-4位表示文件所有者的权限，`u`权限；5-7位表示文件所有者所属组成员的权限，`g`权限；8-10位表示所有者所属组之外的用户的权限，`o`权限 ；2-10位的权限总和有时称为`a`权限；

![](D:\Java\笔记\图片\2-31【Linux】\1-0.png)

第一位显示的是文件类型，linux下文件一共分为7类：`-`表示普通文件、`d`表示目录文件、`b`表示块设备文件、`c`表示字符设备、`l`表示符号链接、`p`表示管道文件、`s`表示套件字文件。

第二个部分、第三个部分和第四个部分：

- `r(read)`：读权限。对文件是指可读取内容，对目录是可以执行`ls`命令，代表数字是4。
- `w(write)`：写权限。对文件是指可修改文件内容，对目录是指可以在其中创建或删除子节点(目录或文件)。代表数字是2。
- `x(excute)`：执行权限。对文件是指是否可以运行这个文件，对目录是指是否可以`cd`进入这个目录。代表数字是1。

修改权限的命令：`chmod` 。

`chmod u=rwx,g=rx,o=rx a.txt`：对a.txt的权限进行修改。u是当前的用户，g是当前组内其他用户，o是其他组内用户。也可以使用数字相加来帮忙。`u = 4`、`g = 2`、`o = 1`；相加起来为一组的权限。`chmod 755 a.txt` 

# 第二章 Linux的常用命令

Linux命令格式如下：`command [-options] [parameter]`

说明：

* `command`：命令名称
* `[-options]`：选项，可以用来对命令进行控制，也可以省略
* `[parameter]`：传给命令的参数，可以是零个、一个或者多个。

> `[]`代表可选，命令名称、选项、参数之间要有空格进行分割。

如果我们不知道参数，可以使用`--help`。例如：`mkdir --help`，那么就会显示参数。Tab键可以实现自动补全和提示，要合理使用；history命令可以显示历史执行记录，或者使用方向键来切换前后执行过的命令；

## 2.1 关机、重启和注销

| 命令              | 作用             |
| ----------------- | ---------------- |
| shutdown -h now   | 即刻关机         |
| shutdown -r now   | 重启             |
| shutdown -h 10    | 10分钟后关机     |
| shutdown -h 11:00 | 11：00关机       |
| shutdown -c       | 取消指定时间关机 |
| shutdown -r 10    | 10分钟之后重启   |
| shutdown -r 11:00 | 定时重启         |

| 命令      | 作用     |
| --------- | -------- |
| init 0    | ⽴刻关机 |
| telinit 0 | 关机     |
| poweroff  | ⽴刻关机 |
| halt      | 关机     |
| reboot    | 重启     |
| init 6    | 重启     |

## 2.2 文件和目录操作

**切换目录命令**

可以使用tab键来补全文件路径

| 命令        | 作用                                     |
| ----------- | ---------------------------------------- |
| cd <目录名> | 进⼊某个目录 cd（change directory）      |
| cd ..       | 回上级目录                               |
| cd          | 进个⼈主目录                             |
| cd -        | 回上⼀步所在目录                         |
| cd /        | 切换到系统根目录                         |
| cd ~        | 切换到用户主目录                         |
| pwd         | 显示当前路径 pwd（print work directory） |

**列出文件列表**

`ls`是list files的缩写，通过ls 命令不仅可以查看目录和文件信息，还可以目录和文件权限、大小、主人和组等信息。`ls(list)`是一个非常有用的命令，用来显示当前目录下的内容。配合参数的使用，能以不同的方式显示目录内容。 

 格式：`ls[参数] [路径或文件名]`

| 命令    | 作用                                           |
| ------- | ---------------------------------------------- |
| ls      | 查看⽂件目录列表                               |
| ls -F   | 查看目录中内容（显示是⽂件还是目录）           |
| ls -l   | 查看⽂件和目录的详情列表（等于`ll`命令）       |
| ls -a   | 显示所有文件或目录（包含隐藏的文件）           |
| ls -lh  | 查看⽂件和目录的详情列表（增强⽂件大小易读性） |
| ls -lSr | 查看⽂件和目录列表（以⽂件大小升序查看）       |
| tree    | 查看⽂件和目录的树形结构                       |

**创建目录、移除文件和目录**

`rm` 删除文件。用法：`rm [选项]... 文件...`   删除需要用户确认, `rm - f` 删除不询问

| 命令                    | 作用                                                   |
| ----------------------- | ------------------------------------------------------ |
| mkdir <目录名>          | 创建目录 mkdir（make directory）                       |
| mkdir dir1 dir2         | 同时创建两个目录                                       |
| mkdir -p /tmp/dir1/dir2 | 创建目录树，创建了tmp下面又创建了dir1，dir1下面有dir2  |
| touch a.txt             | 如果文件不存在创建一个空文件                           |
| rmdir dir1              | 删除'dir1'目录（如果dir1目录下面有东西，那么无法删除） |
| rm -f file1             | 删除'file1'⽂件，无需确认直接删除                      |
| rm -r a                 | 删除文件夹及内容，删除前需要确认                       |
| rm -rf dir1             | 删除'dir1'目录和其内容，不询问递归删除                 |
| rm -rf dir1 dir2        | 同时删除两个目录及其内容                               |
| rm -rf *                | 删除所有文件                                           |
| rm -rf /*               | 自杀                                                   |

**重命名和移动目录**

`mv` 移动或者重命名

| 命令               | 作用                                                         |
| ------------------ | ------------------------------------------------------------ |
| mv old_dir new_dir | 重命名/移动目录                                              |
| mv a.txt ../       | 将`a.txt`文件移动到上一层目录中                              |
| mv a.txt b.txt     | 将`a.txt`文件重命名为`b.txt`                                 |
| mv lin/ xuan/      | 如果xuan目录不存在，那么将lin目录更名为xuan<br />如果xuan目录村子啊，那么将lin目录移动到xuan目录中 |

**复制文件和目录**

`cp(copy)`命令可以将文件从一处复制到另一处。一般在使用`cp`命令时将一个文件复制成另一个文件或复制到某目录时，需要指定源文件名与目标文件名或目录。

| 命令                | 作用                                        |
| ------------------- | ------------------------------------------- |
| cp file1 file2      | 复制⽂件                                    |
| cp -a dir1 dir2     | 复制目录                                    |
| cp -r lin/ ./xuan/  | 将lin目录和目录下所有文件复制到xuan目录下面 |
| cp -r lin/* ./xuan/ | 将lin目录下所有文件复制到xuan目录下面       |
| cp a.txt b.txt      | 将`a.txt`复制为`b.txt`文件                  |
| cp a.txt ../        | 将`a.txt`文件复制到上一层目录中             |

**查找文件**

`find`查找命令。`find`指令用于查找符合条件的文件

| 命令                             | 作用                                      |
| -------------------------------- | ----------------------------------------- |
| find . -name "*.java"            | 在当前目录及其子目录下面查找.java结尾文件 |
| find / -name “ins*”              | 查找文件名称是以ins开头的文件             |
| find /dir -name *.bin            | 在目录/dir中搜带有.bin后缀的⽂件          |
| find / –user linxuan –ls         | 查找用户linxuan的文件                     |
| find / –user linxuan –type d –ls | 查找用户linxuan的目录                     |
| find /-perm -777 –type d-ls      | 查找权限是777的文件                       |

## 2.3 文件查看和处理

**查看文件内容**

按 q 键退出查看，ctrl+c也可以结束查看

| 命令             | 作用                                                         |
| ---------------- | ------------------------------------------------------------ |
| cat file1        | 查看文件内容。`cat`用于显示文件的内容                        |
| cat -n file1     | 查看内容并标示行数                                           |
| tac file1        | 从最后⼀行开始反看⽂件内容                                   |
| more file1       | `more`用于要显示的内容会超过一个画面长度的情况。<br />按空格键显示下一个画面。回车显示下一行内容。按 `q` 键退出查看，`ctrl+c`也可以结束查看，`b (back)`返回上一页。 |
| less file1       | `less`用法和`more`类似，不同的是`less`可以通过`PgUp`、`PgDn`键来控制。 |
| head file1       | 查看文件前十行信息                                           |
| head -2 file1    | `PgUp` 和 `PgDn` 进行上下翻页。查看⽂件前两⾏。              |
| tail -2 file1    | 查看⽂件后两⾏。用于显示文件后几行的内容。                   |
| tail -f /log/msg | 实时查看添加到⽂件中的内容，动态查看日志。`ctrl+c` 结束查看  |
| wc -l file1      | 查看文件行数                                                 |

**查找文件内容**

`grep`查找字符串，查找文件里符合条件的字符串。用法: `grep [选项]... PATTERN [FILE]...`

| 命令                         | 作用                                                     |
| ---------------------------- | -------------------------------------------------------- |
| grep linxuan hello.txt       | 在⽂件hello.txt中查找关键词linxuan                       |
| grep hello *.java            | 查找当前目录中所有.java结尾的文件中包含hello字符串的位置 |
| grep linxuanhello.txt –color | 在⽂件hello.txt中查找关键词linxuan并且高亮显示           |
| grep ^linxuanhello.txt       | 在⽂件hello.txt中查找以linxuan开头的内容                 |
| grep [0-9] hello.txt         | 选择hello.txt⽂件中所有包含数字的⾏                      |

## 2.4 打包和解压

`tar`打包或解压

`tar`命令位于`/bin`目录下，它能够将用户所指定的文件或目录打包成一个文件，但不做压缩。

一般Linux上常用的压缩方式是选用`tar`将许多文件打包成一个文件，再以`gzip`压缩命令压缩成xxx.tar.gz(或称为`xxx.tgz`)的文件。常用参数：

- `-z`：z代表的是gzip，调用`gzip`压缩命令进行压缩
- `-c`：c代表的是create，创建一个新`tar`文件
- `-x`：x代表的是extract，解开`tar`文件
- `-v`：v代表的是verbose，显示运行过程的信息
- `-f`：f代表的是file，用于指定文件名

| 命令                               | 作用                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| zip xxx.zip file                   | 压缩⾄zip包                                                  |
| tar –cvf hello.tar ./              | 将当前目录下面所有文件打包，打包后的文件叫做hello.tar        |
| tar –zcvf hello.tar.gz ./*         | 将当前目录下面所有文件打包并且压缩，打包后的文件叫做hello.tar.gz |
| tar –xvf hello.tar                 | 将hello.tar文件解包，并将解包后的文件放在当前的目录          |
| tar -zxvf hello.tar.gz -C /usr/aaa | 将hello.tar.gz文件解压，并将解包后的文件放在/usr/aaa的目录   |

## 2.5 日期和时间

`date`命令：用来显示或设定系统的日期与时间，在显示方面，使用者可以设定欲显示的格式，格式设定为一个加号 后接数个标记。

`cal`（calendar）命令：用于用于显示当前或者指定日期的公历。

## 2.6 重定向输出>和>>

`>` 重定向输出，覆盖原有内容；`>>` 重定向输出，有追加功能；

前面的命令没必要必须是查看，只要是能够在控制台输出的命令即可。

- `cat /etc/passwd > a.txt` 将输出定向到a.txt中
- `cat /etc/passwd >> a.txt` 输出并且追加
- `ifconfig > ifconfig.txt` 将控制台输出的IP信息，放到文件里面

## 2.7 系统管理命令

`ps` 正在运行的某个进程的状态

- `ps –ef` 查看所有进程
- `ps –ef | grep '进程的名称'` 查找某一进程
- `kill 2868` 杀掉2868编号的进程
- `kill -9 2868` 强制杀死进程

`free`命令：用于显示内存状态。会显示内存的使用情况，包括实体内存，虚拟的交换文件内存，共享内存区段，以 及系统核心使用的缓冲区等。

`df`（英文全拼：disk free）命令：用于显示目前在 Linux 系统上的文件系统磁盘使用情况统计。

`jps`命令：这是JDK自带的命令，专门用于查看本机运行的java进程情况。

top命令：看到正在运行的程序进程

## 2.8 管道|

管道是Linux命令中重要的一个概念，其作用是将一个命令的输出用作另一个命令的输入。

- `ls --help | more` 分页查询帮助信息
- `ps –ef | grep java` 查询名称中包含java的进程
- `ifconfig | more`
- `cat index.html | more`
- `ps –ef | grep aio`

## 2.9 其他常用命令

| 命令                                     | 作用                                             |
| ---------------------------------------- | ------------------------------------------------ |
| clear/ crtl + L                          | 清屏                                             |
| sudo su                                  | 切换管理员。$ 是普通权限， #是管理员权限。       |
| su 用户名                                | 退出管理员权限。或者用`exit`。                   |
| hostnamectl                              | 查看本机名称                                     |
| echo 'LANG=en_US.UTF-8″' >> /etc/profile | 将'LANG=en_US.UTF-8″' 追加至`/etc/profile`文件中 |
| source /etc/profile                      | 重新加载`/etc/profile`文件                       |

# 第三章Vim编辑器

vi是一款很多unix及其延伸系统内置的文本编辑器，具有强大的文本编辑能力。vim是从vi发展出来的一个文本编辑器，可以理解为vi编辑器的增强版。vim中代码补完、编译及错误跳转等方便编 程的功能特别丰富。

vi/vim编辑器的核心设计思想：让程序员的手指始终保持在键盘的核心区域, 就能完成所有的编辑操作。学习使用vi/vim编辑器最大挑战：万物皆命令。

`vim /path/file`：如果打开的文件不存在，此时就是新建文件，编辑器左下角会提示new file。如果文件已经存在，此时就打开这个文件，进入命令模式

Vim模式如下：

- 普通模式 / 默认模式：执行 Vim 的指令，如移动光标，复制、删除 、粘贴文本等等，不能进行输入（打字）。
- 插入模式 / 编辑模式：进行文本输入（打字），和普通编辑器一样。
- 替换模式：进行字符替换。
- 可视模式 / 选择模式：进行文本选择。
- 命令模式：在 Vim 底部最后一行中输入命令，按回车并执行。

## 3.1 普通模式

在Linux的命令行通过`vi文件名`或`vim文件名`即可用VIM编辑器打开你的文件，打开文件后默认是进入了普通模式，我们可以用h、j、k、l四个按键分别左、下、上、右地移动光标。

回到普通模式，仅需要敲一下键盘左上角的Esc按键即可（界面左下角的--INSERT--标志消失）。

**移动**

- `h j k l`：四个按键分别代表左、下、上、右。
- `Ctrl + f`：屏幕『向下』移动一页，相当于 [Page Down]按键
- `Ctrl + b`：屏幕『向上』移动一页，相当于 [Page Up] 按键
- `0或者Home`：移动到这一行的最前面字符处
- `$或者End`：移动到这一行的最后面字符处（注意输入法状态`￥`和`$`不一样）
- `G`：移动到文件最后一行
- `[数字n]G`：移动到文件的第n行
- `gg`：移动到文件的第1行，相当于`1G`
- `n<Enter>`：n 为数字。光标向下移动 n 行

**删除**

在普通模式下，删除单一字符可以用`x`命令。跟在插入模式按下Delete键一样，x命令是删除光标指定的字符。

删除更多字符可以使用d命令！使用删除操作符`d`的删除命令的格式是`dmotion`，`d`是`Delete`的意思，`motion`即指定要删除的对象，所以这个命令可以很灵活！

* `dgg`：删除光标所在行（包含）到文件开头的所有字符
* `dG`：删除光标所在行（包含）到文件末尾的所有字符
* `dd`：删除光标所在行的所有字符

> 注意：这上面所有的“删除”操作并不是真的删除，它们事实上是存放在VIM的一个缓冲区（VIM把它称之为寄存器）中，相当于Windows的剪切功能！！！

**粘贴**

使用`p`命令可以将最后一次删除的内容粘贴到光标之后。大写的`P`则是粘贴到光标之前。

先使用`dd`命令将当前行删除，然后再使用`p`命令就可以粘贴了

**拷贝**

VIM用`y`命令实现拷贝。语法跟删除的`d`命令一样：`ymotion`。

- `yy`表示拷贝当前行
- `3yy`则表示拷贝3行
- `y$`表示从光标所在的位置拷贝到行尾的所有字符
- `yG`则表示从光标所在行拷贝到文件末尾行的所有字符

**撤销**

小写的`u`表示撤销最后一次修改；大写的`U`表示撤销对整行的修改。

`Ctrl+r`快捷键可以恢复撤销的内容！！

**简单替换**

VIM还提供了一个简单的替换命令：`r`命令。`r`用于替换光标所在的字符，做法是先将光标移动到需要替换的字符处，按一下`r`键，然后输入新的字符。

不会进入插入模式，也不会进入替换模式。

**其他**

* 快捷键`ctrl+g`可以查看当前的文件信息，比如文件名，文件状态，文件的总行数，以及光标所在的相对位置。
* 将光标移动到`()`，`[]`，`{}`，中的任何一半括号上，按下`%`键，便可看到此时光标已经跳转到另外一半的括号上了。

## 3.2 插入模式

敲一下i按键使得VIM切换到插入模式（界面左下角显示--INSERT--字样），此时你可以自由的编辑文档了，就像你在Windows下使用记事本那样去编辑。

当然，并不只有敲击按键i才能进入插入模式，其实方法多了去了：

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

## 3.3 替换模式

对于需要替换多个字符，更好的方案是直接进入替换模式。按下大写的`R`键，屏幕左下角出现`--REPLACE--`字样，说明你已经处于替换模式。

此时输入字符可以连续替换光标及其后边的内容。注意：退格键（`Backspace`）在替换模式中被解释为如果左边内容被替换过，则恢复到原来的样子；如果没有被替换过，则简单的向左移动。修改完毕后，按下`Esc`回到普通模式。

## 3.4 可视模式

这时，你可以按一下`v`进入可视模式（左下角出现`--VISUAL--`字样），然后通过`h、j、k、l`或其他`motion`来移动你的光标，此时光标所到之处必被一道亮光所包围（表示被选中），选择好需要缩进的目标后，只需按一下`>`即可完成任务。

## 3.5 命令模式

输入冒号（`:`）进入命令行模式

在普通模式下按下斜杠（`/`）也是进入命令行模式，此时该字符和光标均出现在屏幕的底部，这跟冒号（`:`）一样。

在普通模式下按下问号（`?`）也是进入命令行模式，实现的也是搜索功能。

**退出**

- `:q`：退出Vim
- `:q!`：若曾修改过文件，又不想储存，使用 ! 为强制离开不储存档案。
- `:w`：保存
- `:wq`：储存后离开，若为 :wq! 则为强制储存后离开

**搜索**

`/目标`和`?目标`都是搜索

|              | **/目标**      | **?目标**      |
| ------------ | -------------- | -------------- |
| **搜索方向** | 从光标位置向后 | 从光标位置向前 |
| **n**        | 向后搜索下一个 | 向前搜索下一个 |
| **N**        | 向前搜索下一个 | 向后搜索下一个 |

当搜索到了文件的末尾（`/目标`）或开头（`?目标`），页面下方面会显示`“SearchhitBOTTOM,continuingatTOP”`或`“SearchhitTOP,continuingatBOTTOM”`的字样。

表示一轮搜索到文件尾/头了，搜索下一个就是从文件头/尾开始咯。

VIM会自动高亮所有匹配的目标，即使在你找到目标之后，它们仍然高傲的亮着！如何取消呢？输入冒号（`:`）进入命令行模式，然后输入`nohl`即可。

> 最后提个醒：在搜索命令中，`.`、`*`、`[`、`]`、`^`、`%`、`/`、`?`、`~`和`$`这10个字符有着特殊意义，所以在使用这些字符的时候要在前面加上一个反斜杠（`\`），比如你要搜索问号，则输入`/\?`

**高级替换**

- `:s/old/new`，这样即可将光标所在行的第一个`old`替换为`new`；
- `:s/old/new/g`，则表示将光标所在行的所有`old`替换为`new`。
- `:%s/old/new/g`表示替换整个文件中每个匹配的字符串。
- `:5,13s/old/new/g`即可胜任你的要求。5表示开始替换的行号，13表示结束替换的行号

## 3.6 VIM数字和命令

VIM利用数字可以干老多的事儿了。

比如普通模式下按一下`h`按键是将光标向左移动一格，而在其前面输入数字3，即`3h`则是将光标向前移动3格，依此类推`3j`则是将光标向下移动三行，`3w`则是将光标跳到三个单词后的开始位置……

同样的，`d3h`表示删除光标前的3个字符，`d3j`表示删除光标所在行以及下面3行的所有字符，而`d3w`则表示向后删除当前光标到后面第三个单词前的所有字符。

# 第四章 Linux网络操作

这些都是Centos6的命令，有的对于Centos7不适用

## 4.1 主机名配置

- `hostname` 查看主机名

- `hostname xxx` 修改主机名 重启后无效

  如果想要永久生效，可以修改`/etc/sysconfig/network`文件

## 4.2 IP地址配置

`ifconfig 查看(修改)ip地址(重启后无效)`

`ifconfig eth0 192.168.12.22 修改ip地址`

如果想要永久生效，修改 `/etc/sysconfig/network-scripts/ifcfg-eth0`文件

Centos7是使用`ip addr`来查看IP地址

```apl
DEVICE=eth0 #网卡名称
BOOTPROTO=static #获取ip的方式(static/dhcp/bootp/none)
HWADDR=00:0C:29:B5:B2:69 #MAC地址
IPADDR=12.168.177.129 #IP地址
NETMASK=255.255.255.0 #子网掩码
NETWORK=192.168.177.0 #网络地址
BROADCAST=192.168.0.255 #广播地址
NBOOT=yes #  系统启动时是否设置此网络接口，设置为yes时，系统启动时激活此设备。
```

## 4.3 域名映射

`/etc/hosts`文件用于在通过主机名进行访问时做ip地址解析之用，相当于windows系统的`C:\Windows\System32\drivers\etc\hosts`文件的功能

![](..\图片\2-31【Linux】\1-3.png)

就是我们起了一个别的名字，`127.0.0.1 == localhost`。

## 4.4 网络服务管理

- `service network status` 查看指定服务的状态

- `service network stop` 停止指定服务

- `service network start` 启动指定服务

- `service network restart` 重启指定服务
- `service --status–all` 查看系统中所有后台服务

- `netstat –nltp` 查看系统中网络进程的端口监听情况

**防火墙设置**

防火墙根据配置文件`/etc/sysconfig/iptables`来控制本机的”出”、”入”网络访问行为。这是Centos6的

- `service iptables status` 查看防火墙状态
- `service iptables stop` 关闭防火墙
- `service iptables start` 启动防火墙
- `chkconfig iptables off` 禁止防火墙自启

Centos7的防火墙操作

| 命令                                                         | 作用                     |
| ------------------------------------------------------------ | ------------------------ |
| systemctl status firewalld                                   | 查看防火墙状态           |
| firewall-cmd --state                                         | 查看防火墙状态 输出简洁  |
| systemctl start firewalld                                    | 启动防火墙               |
| systemctl stop firewalld                                     | 关闭防火墙               |
| systemctl enable firewalld                                   | 开机启用防火墙           |
| systemctl disable firewalld                                  | 开机禁用，永久关闭防火墙 |
| firewall-cmd --zone=public --add-port=8080/tcp --permanent   | 开放指定端口8080         |
| firewall-cmd --zone=public --list-ports                      | 查看防火墙开放的端口     |
| firewall-cmd --zone=public --query-port=8080/tcp             | 查看指定端口访问权限8080 |
| firewall-cmd --zone=public --remove-port=8080/tcp --permanent | 关闭指定端口防火墙8080   |
| firewall-cmd --reload                                        | 重新加载配置 立即生效    |

# 第五章 软件安装与项目部署

## 5.1 软件安装

一般我们下载的软件都会放在/usr/local里面

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

1. `FileZilla`， 我们已经下载了。

2. `lrzsz`

   我们可以使用yum安装方式安装 yum install lrzsz

   注意：必须有网络

   可以在crt中设置上传与下载目录 在命令行输入`rz`就可以上传文件了

3. `sftp`

   使用alt + p 组合键打开sftp窗口

   使用put 命令上传

   使用get命令下载

### 5.1.1 安装jdk

1. 使用上传工具FileZilla上传到Linux，上传的是二进制发布包，这里的是jdk1.7

2. 解压安装包：`tar -zxvf jdk版本 -C /usr/local`。这样会在usr/local解压，然后会有一个文件夹名为jdk1.7.0_75，里面包含着jdk。

   不过我们是在usr/local下面创建了一个jdk文件夹，让安装包解压到了jdk文件夹里面。`tar -zxvf jdk版本 -C /usr/local/jdk`

3. 配置环境变量，使用vim修改/etc/profile文件，在文件末尾加入如下配置

   ```bah
   JAVA_HOME=/usr/local/jdk/jdk1.7.0_75
   PATH=$JAVA_HOME/bin:$PATH
   ```

4. 重新加载profile文件，使更改的配置立即生效，命令为：`source /etc/profile`

5. 检查安装是否成功，命令为：`java -version`，出现版本号就代表正确了。

### 5.1.2 安装Tomcat

1. 利用上传工具FileZilla将Tomcat二进制发布包上传到Linux。

2. 解压安装包：`tar -zxvf apache-tomcat版本号 -C /usr/local`。

   老规矩我们在/usr/local下面创建了一个tomcat文件夹存放Tomcat，所以解压命令为：`tar -zxvf apache-tomcat版本号 -C /usr/local/tomcat`。

3. 进入Tomcat的bin目录启动服务，命令为：`sh.startup.sh`或者`./startup.sh`。

验证Tomcat是否启动成功，有多种方式：

* 查看启动日志

  ```bash
  more /usr/local/apache-tomcat-7.0.57/logs/catalina.out
  tail -50 /usr/local/apache-tomcat-7.0.57/logs/catalina.out
  ```

* 查看进程 ps -ef | grep tomcat

  ps命令是Linux下面非常强大的进程查看命令，相当于Windows中的任务管理器，通过ps -ef可以查看当前运行的所有进程的详细信息。

  | 在Linux中成为管道符，可以将前一个命令的结果输出给后一个命令作为输入

  使用ps命令查看进程的时候，经常配合管道符和查找命令grep一起使用，来查看特定的进程。

停止Tomcat服务的方式：

* 运行Tomcat的bin目录提供的停止服务的脚本文件shutdown.sh

  ```bash
  sh.shutdown.sh
  ./shutdown.sh
  ```

* 结束Tomcat进程

  查看Tomcat进程，获得进程id，然后使用kill命令来删除即可

  kill命令是Linux提供的用于结束进程的命令，-9表示强制结束

### 5.1.3 安装MySql

安装Mysql的话我们使用的是RPM安装方式，RPM软件包管理器，是红帽用于管理和安装软件的工具。

1. 检查当前系统中是否安装MySql数据库

   如果当前系统中已经安装有MySql数据库，安装将会失败。Centos7自带mariadb，与MySql数据库冲突。

   ```apl
   rpm -qa							# 查询当前系统中安装的所有软件
   rpm -qa | grep mysql			# 查询当前系统中安装的名称带有mysql的软件
   rpm -qa | grep mariadb			# 查询当前系统中安装的名称带有mariadb的软件
   ```

2. 卸载已经安装的冲突软件

   ```apl
   rpm -e --nodeps 软件名称						# 卸载软件
   rpm -e --nodeps mariadb-libs-libs版本号		# 卸载mariadb软件
   ```

3. 将MySql安装包上传到Linux并且解压，解压后会得到6个rpm的安装包文件。

   ```apl
   mkdir /usr/local/mysql							# 创建一个mysql目录
   tar -zxvf mysql-版本号 -C /usr/local/mysql		  # 解压至mysql目录
   ```

4. 启动Mysql，我们在Centos7上面安装的东西是之前的999集中老师教的，有点缺陷。凑活用吧。

   启动Mysql服务：service mysql start

   进去Mysql：mysql -uroot -p132456

   我们也可以使用SQLyog来连接，已经保存好东西了。

   上面的是之前的老师教的，与后面老师弄得有出入。

## 5.2 项目部署

### 5.2.1 手动部署项目

1. 在IDEA中SpringBoot项目并打成jar包

2. 将Jar包上传到Linux服务器，放到/usr/local/app里面

3. 启动SpringBoot程序，命令为：`java -jar 【程序名称】`

4. 检查防火墙，确保8080端口是对外面开放的，这个我们已经关闭防火墙了。使用win10访问SpringBoot项目发现可以访问。

5. 改为后台运行SpringBoot程序，并将日志输出到日志文件中。

   目前程序中运行的问题：

   * 线上程序不会采用控制台霸屏的形式运行程序，而是将程序在后台运行。我们的SpringBoot程序运行后会霸屏，终端无法输入，必须重新克隆一个才可以。
   * 线上程序不会讲日志输出到控制台，而是输出到日志文件，方便运维查阅信息。

   ```apl
   nohup命令：英文全称 no hang up(不挂起)，用于不挂断地运行指定命令，退出终端不会影响程序的运行
   语法格式：nohup COmmand [Arg...] [&]
   参数说明：
   	* Command：要执行的命令
   	* Arg：一些参数，可以指定输出文件
   	* &：让命令在后台运行
   举例：
   nohup java -jar boot工程.jar &> hello.log &  # 后台运行Java -jar命令，并将日志输出到hello.log文件。
   ```

6. 停止SpringBoot程序

   通过杀进程的方式来停掉，`ps -ef | grep java` `kill -9 【进程PID】`

   显示进程的时候第一个数字就是进程的PID了，第二个和第三个不用管。root之后的就是PID

### 5.2.3 Shell脚本自动部署项目

1. Linux中安装Git

   ```apl
   yum list git			# 列出git安装包
   yum install git			# 在线安装git
   ```

   现在我们已经可以使用git从网上拉取代码下来了，例如：`git clone https://gitee.com/linxuanhi/spring-boot-test.git`，就会创建一个spring-boot-test程序。我们都放在了/usr/local/projects/

2. Linux中安装Maven

   将资料中提供的Maven安装包上传到Linux中，安装Maven

   ```apl
   mkdir /usr/local/maven	# 在/usr/local下面创建文件夹maven
   tar -zxvf apache-maven-3.5.4-bin.tar.gz -C /usr/local/maven # 将maven文件解压至/usr/local/maven
   
   vim /etc/profile					# 打开/etc/profile配置环境变量 增加下面两行代码
   
   export MAVEN_HOME=/usr/local/maven/apache-maven-3.5.4
   export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
   
   source /etc/profile					# 让修改的配置文件重新加载 生效
   
   mvn -version						# 查看maven版本
   vim /usr/local/maven/apache-maven-3.5.4/conf/settings.xml  # 添加Maven仓库位置 添加下一行代码
   <localRepository>/usr/local/maven/repo</localRepostitory>
   ```

3. 编写Shell脚本（拉取代码、编译、打包、启动）

   在usr/local下面创建sh目录，在sh目录下面创建一个bootStart.sh文件，复制下面内容就可以了。

   ```apl
   cd /usr/local 		# 进入/usr/local目录
   mkdir sh			# 创建sh目录
   cd sh				# 进入sh目录
   vim bootStart.sh	# 创建并打开bootStart.sh文件
   ```

   ```sh
   #!/bin/sh
   echo =================================
   echo  自动化部署脚本启动
   echo =================================
   
   echo 停止原来运行中的工程
   APP_NAME=spring-boot-test
   
   tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
   if [ ${tpid} ]; then
       echo 'Stop Process...'
       kill -15 $tpid
   fi
   sleep 2
   tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
   if [ ${tpid} ]; then
       echo 'Kill Process!'
       kill -9 $tpid
   else
       echo 'Stop Success!'
   fi
   
   echo 准备从Git仓库拉取最新代码
   cd /usr/local/projects/spring-boot-test
   
   echo 开始从Git仓库拉取最新代码
   git pull
   echo 代码拉取完成
   
   echo 开始打包
   output=`mvn clean package -Dmaven.test.skip=true`
   
   cd target
   
   echo 启动项目
   nohup java -jar springbootest-0.0.1-SNAPSHOT.jar &> spring-boot-test.log &
   echo 项目启动完成
   ```

4. 为用户授予执行Shell脚本的权限

   ```apl
   chmod 777 bootStart.sh
   ```

5. 执行Shell脚本

6. 修改IP地址 这里我们就不修改了。

现在我们运行情况大概如下：

```bash
[root@192 local]# pwd
/usr/local
[root@192 local]# ll
total 4
drwxr-xr-x. 2 root root   46 Jul  8 22:07 app
drwxr-xr-x. 2 root root    6 Apr 11  2018 bin
drwxr-xr-x. 2 root root    6 Apr 11  2018 etc
drwxr-xr-x. 2 root root    6 Apr 11  2018 games
drwxr-xr-x. 2 root root    6 Apr 11  2018 include
drwxr-xr-x. 4 root root  112 Jul  9 06:52 jdk
drwxr-xr-x. 2 root root    6 Apr 11  2018 lib
drwxr-xr-x. 2 root root    6 Apr 11  2018 lib64
drwxr-xr-x. 2 root root    6 Apr 11  2018 libexec
drwxr-xr-x. 4 root root   81 Jul  9 09:22 maven
drwxr-xr-x. 2 root root 4096 Apr 20 13:33 mysql
drwxr-xr-x. 6 root root   87 Apr 20 20:07 nginx
drwxr-xr-x. 3 root root   30 Jul  9 10:00 projects
drwxr-xr-x. 4 root root   62 Apr 20 14:45 redis
drwxr-xr-x. 2 root root    6 Apr 11  2018 sbin
drwxr-xr-x. 2 root root   26 Jul  9 15:27 sh
drwxr-xr-x. 5 root root   49 Apr 20 18:48 share
drwxr-xr-x. 2 root root    6 Apr 11  2018 src
drwxr-xr-x. 3 root root   69 Apr 20 14:22 tomcat
[root@192 local]# cd projects/
[root@192 projects]# ll
total 0
drwxr-xr-x. 5 root root 58 Jul  9 15:44 spring-boot-test
[root@192 projects]# cd spring-boot-test/
[root@192 spring-boot-test]# ll
total 4
-rw-r--r--. 1 root root 1570 Jul  9 10:01 pom.xml
drwxr-xr-x. 4 root root   30 Jul  9 10:01 src
drwxr-xr-x. 6 root root  205 Jul  9 15:44 target
[root@192 spring-boot-test]# cd /usr/local/sh
[root@192 sh]# ll
```
