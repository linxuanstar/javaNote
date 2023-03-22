![](..\图片\3-31【Linux】\0掌握程度.png)

# 第一章 Linux系统介绍

Linux是一套免费使用和自由传播的类Unix操作系统，是一个基于POSIX和Unix的多用户、多任务、支持多线程和多CPU的操作系统。

Linux与其他操作系统相比 ，具有开放源码、没有版权、技术社区用户多等特点 ，开放源码使得用户可以自由裁剪，灵活性高，功能强大，成本低。尤其系统中内嵌网络协议栈 ，经过适当的配置就可实现路由器的功能。这些特点使得Linux成为开发路由交换设备的理想开发平台。

## 1.1 历史介绍

**GNU历史**

* 1983年理查德·马修·斯托曼（Richard Matthew Stallman，简称rms或者RMS）在麻省理工学院公开发起一项GNU计划（又被译为革奴计划）。GNU其名称取自英文“GNU’s Not Unix”，意为“GNU不是Unix”。就是为了创建一个完全由免费软件组成的类Unix计算机操作系统。
* 在1985年10月RMS创立了自由软件基金会（Free Software Foundation，FSF）。主要工作是运行GNU计划，开发更多的自由软件。
* 1985年RMS开发出了开发GNU Emacs。它是著名的集成开发环境和文本编辑器，Emacs被公认为是最受专业程序员喜爱的代码编辑器之一，另一个是Vim。
* 1987年RMS发布了GCC编译器。GCC（GNU Compiler Collection，GNU编译器套件）是由GNU开发的编程语言编译器。
* 1989年RMS编写了GNU通用公共许可协议（GNU General Public License，GPL）。它给予了用户充分的自由，允许用户运行、学习、共享和修改软件。GPL是一个Copyleft许可证，也就是说GPL的派生作品只能以相同的许可证发布。Copyleft 要求作者所许可的人对改作后的派生作品要使用相同许可证授予作者，以保障其后续所有派生作品都能被任何人自由使用。
* 1990年GNU计划已经开发出的软件有：Emacs编辑器、GCC（GNU Compiler Collection，GNU编译器集合）。以及大部分UNIX系统的程序库和工具。唯一依然没有完成的重要组件就是操作系统的内核。
* 1991年RMS发布GPL第二版本。
* 1991年Linus Torvalds编写出了与UNIX兼容的Linux操作系统内核并在GPL条款下发布。Linux之后在网上广泛流传，许多程序员参与了开发与修改。
* 1992年Linux与其他GNU软件结合，完全自由的操作系统正式诞生。该操作系统往往被称为“GNU/Linux”或简称Linux。但Linux本身不属于GNU计划的一部分，GNU计划自己的内核Hurd依然在开发中。Linux操作系统包涵了Linux内核与其他自由软件项目中的GNU组件和软件，可以被称为GNU/Linux。

**Linux历史**

* 20世纪80年代，当时可用操作系统有Unix、DOS和MacOS几种。Unix价格昂贵，不能运行于PC；DOS显得简陋，且源代码被软件厂商严格保密； MacOS是一种专门用于苹果计算机的操作系统。
* 1984年AndrewS.Tanenbaum编写了一个操作系统MINIX用于向学生讲述操作系统内部工作原理。
* 1989年，Andrew S.Tanenbaum将Minix系统运行于x86的PC平台。
* 1991年Linus Torvalds在吸收了MINIX精华的基础上，写出了Linux操作系统。Linux是一种外观和性能与UNIX相同或更好的操作系统，但Linux不源于任何版本的UNIX的源代码，并不是UNIX，而是一个类似于UNIX的产品。 

## 1.2 发行体系

发行类型如下：

* 服务器发行版：服务器要求软件包稳定、安全、久经考验。一般自带安全设施、管理工具、各类基础网络服务。服务器发行版的软件包往往很旧，依赖关系牢固。
* 工作站发行版：工作站是开发用机，要求软件包足够新，方便开发者跟进开发。开发者可以在工作站上尝试新工具，有别于服务器上只部署久经考验的稳定服务。工作站一般自带各类开发环境。
* 容器发行版：为Docker等容器量身打造的版本，为规模计算机集群而优化，着重提供自动化部署和其他集群服务能力。
* 桌面发行版：桌面主要供日常使用，要对桌面操作、键盘鼠标、音频视频播放、浏览网页提供较好的体验。一般自带各类桌面应用。软件包管理不如服务器版和工作站版规整，软件结构比较差。

服务器Linux和工作站Linux最好成套，例如都采用红帽系，或都采用SUSE系，或都采用Debian系。这样可以让开发环境和部署环境相对一致，拥有相同的工具栈，开发效率高，部署成本低。

Linux发行版一共分为三大体系：Debian系、Suse/Slackware系、Redhat/Fedora系。

![](D:\Java\笔记\图片\3-31【Linux】\1-1Linux发行体系.png)

**Debian系**

Debian系有：Debian（服务器）、Ubuntu（桌面）、Linux Mint（桌面）。

* Debian是指一个致力于创建自由操作系统的合作组织及其作品，由于Debian项目众多内核分支中以Linux内核为主，而且 Debian开发者所创建的操作系统中绝大部分基础工具来自于GNU工程 ，因此 “Debian” 常指Debian GNU/Linux。
* Ubuntu是一个以桌面应用为主的Linux操作系统，基于Debian，与Debian的不同在于它每6个月会发布一个新版本。Ubuntu的目标在于为一般用户提供一个最新的、同时又相当稳定的主要由自由软件构建而成的操作系统。
* Linux Mint 由Linux Mint Team团队于2006年开始发行，是一份基于Debian和Ubuntu的Linux发行版。它诞生的目的是为家庭用户和企业提供一个免费的，易用的，舒适而优雅的桌面操作系统。

**Suse/Slackware系**

Suse/Slackware系有：SUSE（服务器）、openSUSE（工作站、桌面）、SLES（SUSE Linux Enterprise Server，企业服务器操作系统）。

* SUSE是基于Slackware二次开发的一款欧洲的企业用发行版，主要用于商业桌面、服务器。SUSE比红帽更为高端，适合大型企业使用。
* openSUSE是由SUSE发展而来，旨在推进linux的广泛使用，主要用于桌面环境，用户界面非常华丽，而且性能良好。
* SLES是企业服务器操作系统，是唯一与微软系统兼容的Linux操作系统。

**Redhat/Fedora系**

Redhat/Fedora系有：Red Hat Enterprise Linux（付费服务器）、CentOS（免费服务器）、Fedora。

- Red Hat Enterprise Linux简称RHEL，是红帽公司提供的付费服务器发行版，配套红帽公司的付费支持服务。取代Red Hat Linux在商业应用的领域。
- CentOS是RHEL的开源版，由红帽社区根据RHEL采用的软件包编译而来，免费。
- Fedora 是一款由全球社区爱好者构建的面向日常应用的快速、稳定、强大的操作系统。 它由一个强大的社群开发，这个社群的成员以自己的不懈努力，提供并维护自由、开放源码的软件和开放的标准。Fedora 项目由 Fedora 基金会管理和控制，得到了 Red Hat 的支持。

**其他发行版本**

1. Gentoo：基于linux的自由操作系统，基于Linux的自由操作系统，它能为几乎任何应用程序或需求自动地作出优化和定制。追求极限的配置、性能，以及顶尖的用户和开发者社区，都是Gentoo体验的标志特点， Gentoo的哲学是自由和选择。得益于一种称为Portage的技术，Gentoo能成为理想的安全服务器、开发工作站、专业桌面、游戏系统、嵌入式解决方案或者别的东西--你想让它成为什么，它就可以成为什么。由于它近乎无限的适应性，可把Gentoo称作元发行版。
2. Aech Linux(或称Arch)：以轻量简洁为设计理念的Linux发行版。其开发团队秉承简洁、优雅和代码最小化的设计宗旨。

## 1.4 Linux文件系统

操作系统中负责管理和存储文件信息的软件机构称为文件管理系统，简称文件系统；文件系统的结构通常叫做目录树结构，从斜杠`/`根目录开始；

Linux号称“万物皆文件”，意味着针对Linux的操作，大多数是在针对Linux文件系统操作。

![](D:\Java\笔记\图片\3-31【Linux】\2-1Linux文件系统.png)

| 目录        | 作用                                                         |
| ----------- | ------------------------------------------------------------ |
| /bin        | 存放二进制可执行文件(`ls`,`cat`,`mkdir`等)，常用命令一般都在这里。 |
| /boot       | 存放用于系统引导时使用的各种文件                             |
| /dev        | 用于存放设备文件                                             |
| /etc        | 存放系统管理和配置文件                                       |
| /home       | 存放除root用户外用户文件的根目录。用户linxuan主目录就是`/home/linxuan`，可以用`~`表示 |
| /lib        | 存放跟文件系统中的程序运行所需要的共享库及内核模块。共享库又叫动态链接共享库，作用类似windows里的`.dll`文件，存放了根文件系统程序运行所需的共享文件。 |
| /mnt        | 系统管理员安装临时文件系统的安装点，系统提供这个目录是让用户临时挂载其他的文件系统。 |
| /opt        | 额外安装的可选应用程序包所放置的位置。一般情况下，我们可以把tomcat等都安装到这里。 |
| /root       | 超级用户（系统管理员）的主目录（特权阶级）                   |
| /sbin       | 存放二进制可执行文件，只有`root`才能访问。这里存放的是系统管理员使用的系统级别的管理命令和程序。如`ifconfig`等。 |
| /tmp        | 用于存放各种临时文件，是公用的临时文件存储点。               |
| /usr        | 用于存放系统应用程序，比较重要的目录`/usr/local`本地系统管理员软件安装目录（安装系统级的应用）。这是最庞大的目录，要用到的应用程序和文件几乎都在这个目录。 |
| /var        | 用于存放运行时需要改变数据的文件，也是某些大文件的溢出区，比方说各种服务的日志文件（系统启动日志等。）等。 |
| /proc       | 虚拟文件系统目录，是系统内存的映射。可直接访问这个目录来获取系统信息。 |
| /lost+found | 这个目录平时是空的，系统非正常关机而留下“无家可归”的文件（windows下叫`.chk`）就在这里 |

目录树中节点分为两个种类：目录（directory）、文件（file）。从根目录开始，路径具有唯一性；只有在目录下才可以继续创建下一级目录，换句话说目录树到文件终止蔓延。

Linux中三种文件类型：

- 普通文件： 包括文本文件、数据文件、可执行的二进制程序文件等。 
- 目录文件： Linux系统把目录看成是一种特殊的文件，利用它构成文件系统的树型结构。 
- 设备文件： Linux系统把每一个设备都看成是一个文件

**路径操作**

```sh
# 查看当前路径
[linxuan@linxuanVM ~]$ pwd
/home/linxuan
# 通过相对路径的方式到达上一层路径
[linxuan@linxuanVM ~]$ cd ../
# 通过绝对路径的方式到达/home目录
[linxuan@linxuanVM home]$ cd /home
```

相关概念：

- 当前路径：也叫当前工作目录，当下用户所属的位置；
- 相对路径：相对当前工作目录开始的路径，会随当前路径变化而变化；
- 绝对路径：不管工作目录在哪，绝对路径都是从根目录`/`开始，唯一不重复。

| 常用符号 | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| `.`      | 目录或者文件名字以`.`开始表示是隐藏的文件，如果路径以`.`开始表示当前路径 |
| `..`     | 当前目录的上一级目录                                         |
| `~`      | 当前用户的home目录。root用户home目录是/root、linxuan用户的home目录是/home/linxuan |
| `/`      | 单个就是根目录，放在路径中间那么就是路径分隔符               |
| `?`      | 任意字符                                                     |
| `*`      | 任意字符串                                                   |

**文件权限**

Linux系统上对文件的权限有着严格的控制，如果想对某个文件执行某种操作，必须具有对应的权限方可执行成功。

Linux下文件的权限类型一般包括读`r`，写`w`，执行`x`。Linux下权限的粒度有拥有者 、群组 、其它组。每个文件都可以针对三个粒度，设置不同的`rwx`权限。通常情况下，一个文件只能归属于一个用户和组，如果其它的用户想有这个文件的权限，则可以将该用户加入具备权限的群组，一个用户可以同时归属于多个组。



Linux上通常使用chmod命令对文件的权限进行设置和更改。



在权限里面一共是有四个部分的，第一个部分是1个字符，第二个第三个第四个部分是3个字符，一共由十个字符代替。

1位表示文件类型；2-4位表示文件所有者的权限，`u`权限；5-7位表示文件所有者所属组成员的权限，`g`权限；8-10位表示所有者所属组之外的用户的权限，`o`权限 ；2-10位的权限总和有时称为`a`权限；

![](D:\Java\笔记\图片\3-31【Linux】\1-0.png)

第一位显示的是文件类型，linux下文件一共分为7类：`-`表示普通文件、`d`表示目录文件、`b`表示块设备文件、`c`表示字符设备、`l`表示符号链接、`p`表示管道文件、`s`表示套件字文件。

第二个部分、第三个部分和第四个部分：

- `r(read)`：读权限。对文件是指可读取内容，对目录是可以执行`ls`命令，代表数字是4。
- `w(write)`：写权限。对文件是指可修改文件内容，对目录是指可以在其中创建或删除子节点(目录或文件)。代表数字是2。
- `x(excute)`：执行权限。对文件是指是否可以运行这个文件，对目录是指是否可以`cd`进入这个目录。代表数字是1。

修改权限的命令：`chmod` 。

`chmod u=rwx,g=rx,o=rx a.txt`：对a.txt的权限进行修改。u是当前的用户，g是当前组内其他用户，o是其他组内用户。也可以使用数字相加来帮忙。`u = 4`、`g = 2`、`o = 1`；相加起来为一组的权限。`chmod 755 a.txt` 

# 第二章 命令行常用命令

命令行界面CLI（command-line interfaceI）：不支持鼠标，用户通过键盘输入指令，计算机接收到指令后，予以执行。也有人称之为字符用户界面CUI。命令行界面要较图形用户界面节约计算机系统的资源。在熟记命令的前提下，使用命令行界面往往要较使用图形用户界面的操作速度要快。

**命令提示符**

当我们打开终端时，不论是在图形界面还是在字符界面，我们看到的格式都是类似于`[root@linxuan ~]#`这种格式的一串字符，这串字符就是命令提示符。

```sh
# root是当前用户名称；@是分隔符；~[ˈtɪldə]的位置是用户当前所在目录名称，这里表示家目录；
# #这个位置是用户身份提示符，#表示超级用户管理员；$表示普通用户
[root@linxuanVM ~]# 
```

**命令语法格式**

```sh
[root@linxuanVM ~]# ls -l -a -h /linxuan
```

命令+选项+操作对象这三部分是组成了一个标准的linux命令。

* 其中命令部分需要输入命令的名称。
* 选项部分定义命令的执行特性，可以有长短两种选项。
  * 长选项用`--`（发音dash[dæʃ]）引导，后面跟完整的单词，如 `--help`；长选项不能组合使用。
  * 短选项用`-`引导，后面跟单个的字符， 如 `-a` 。多个短选项可以组合使用，例如：`-h -l -a == -hla`。
* 操作对象就是文件或者目录了，操作对象可以有多个。

命令与选项，选项与选项，选项与操作对象，操作对象与操作对象他们之间是必须用空格分隔！

## 2.1 获取命令帮助

| 命令              | 解释                                                   |
| ----------------- | ------------------------------------------------------ |
| type [ 命令 ]     | 判断是内部命令 or 外部命令                             |
| help [内部命令]   | 只针对系统内部命令                                     |
| [外部命令] --help | 外部命令                                               |
| man [命令]        | 内容清晰、详细、在线文档、支持搜索                     |
| info [命令]       | 详细的帮助信息                                         |
| /usr/share/doc    | 存放帮助文档，在与软件同名的目录下有所有软件的使用文档 |

内部命令默认开机加载进内存中，当执行内部命令的时候就直接从内存中放到CPU里面直接运行了，外部命令对应的程序在硬盘上，就需要把硬盘中的文件加载到内存中再到CPU才可以运行。执行速度肯定是内部命令>外部命令。

help命令只能显示shell内部命令的帮助信息，而linux系统中绝大多数命令是外部命令，所以help命令的作用非常有限。而对于外部命令的帮助信息可以使man命令或者info命令查看。

| help命令参数 | 描述                                          |
| ------------ | --------------------------------------------- |
| -d           | 输出每个主题的简短描述                        |
| -m           | 以伪 man 手册的格式显示使用方法               |
| -s           | 为每一个匹配 PATTERN 模式的主题仅显示一个用法 |

--help命令将会显示可执行程序自带的信息，这些信息是嵌入到程序本身的，所以–help信息较简短。

```sh
# 查看类型
[root@linxuanVM ~]# type ls
ls is aliased to `ls --color=auto'
[root@linxuanVM ~]# type cd
cd is a shell builtin

# cd是内部命令，所以无法使用--help查询帮助文档
[root@linxuanVM ~]# cd --help
-bash: cd: --: invalid option
cd: usage: cd [-L|[-P [-e]]] [dir]

# 可以使用help [内部命令]来查询
[root@linxuanVM ~]# help cd
cd: cd [-L|[-P [-e]]] [dir]
    Change the shell working directory.
    
    Change the current directory to DIR.  The default DIR is the value of the
    HOME shell variable.

# 查询ls帮助文档
[root@linxuanVM ~]# ls --help
Usage: ls [OPTION]... [FILE]...
List information about the FILEs (the current directory by default).
Sort entries alphabetically if none of -cftuvSUX nor --sort is specified.
...
```

## 2.1 关机、重启和注销

| 关机命令 | 作用                                                         |
| -------- | ------------------------------------------------------------ |
| shutdown | 以一种安全的方式关闭系统。所有登陆用户都可以看到关机信息提示，并且禁止登陆 |

```sh
[root@linxuanVM ~]# shutdown --help
shutdown [OPTIONS...] [TIME] [WALL...]

Shut down the system.

     --help      Show this help # 显示帮助命令
  -H --halt      Halt the machine # 停止设备
  -P --poweroff  Power-off the machine 
  -r --reboot    Reboot the machine
  -h             Equivalent to --poweroff, overridden by --halt
  -k             Don't halt/power-off/reboot, just send warnings
     --no-wall   Don't send wall message before halt/power-off/reboot
  -c             Cancel a pending shutdown
```



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
| mv lin/ xuan/      | xuan目录不存在，那么将lin目录更名为xuan。存在，那么将lin目录移动到xuan目录中 |

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

```sh
查看外网IP

curl cip.cc
curl ifconfig.me
curl ipinfo.io
```

## 4.3 域名映射

`/etc/hosts`文件用于在通过主机名进行访问时做ip地址解析之用，相当于windows系统的`C:\Windows\System32\drivers\etc\hosts`文件的功能

![](..\图片\3-31【Linux】\1-3.png)

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

一般大型软件或者是一些服务程序安装到 /opt 目录下，普通软件一般安装到 usr/local 目录下。这里我们统一放到opt目录下面了。

```sh
# /opt/目录下面创建这三个文件夹 用于存放 数据 软件 安装包
[root@node1 ~]# ls /opt/
data  server  software
```

## 5.1 软件安装

Linux上的软件安装有以下几种常见方式

1. 二进制发布包：软件已经针对具体平台编译打包发布，只要解压，修改配置即可

2. RPM包：软件已经按照redhat的包管理工具规范RPM进行打包发布，需要获取到相应的软件RPM发布包，然后用RPM命令进行安装

3. Yum在线安装：软件已经以RPM规范打包，但发布在了网络上的一些服务器上，可用yum在线安装服务器上的rpm软件，并且会自动解决软件安装过程中的库依赖问题

4. 源码编译安装：软件以源码工程的形式发布，需要获取到源码工程后用相应开发工具进行编译打包部署。


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

```sh
# /export/目录下面创建这三个文件夹 用于存放 数据 软件 安装包
[root@linxuan ~]# ls /export/
data  server  software
```

### 5.1.1 安装jdk

1. 使用上传工具FileZilla上传到Linux，上传的是二进制发布包，这里的是jdk1.7

2. 解压安装包：`tar -zxvf jdk版本 -C /usr/local`。这样会在usr/local解压，然后会有一个文件夹名为jdk1.7.0_75，里面包含着jdk。

   不过我们是在usr/local下面创建了一个jdk文件夹，让安装包解压到了jdk文件夹里面。`tar -zxvf jdk版本 -C /usr/local/jdk`

3. 配置环境变量，使用vim修改`/etc/profile`文件，在文件末尾加入如下配置

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

安装Mysql的话我们使用的是RPM安装方式，RPM软件包管理器，是红帽用于管理和安装软件的工具。`mysql-5.7.29-1.el7.x86_64.rpm-bundle.tar`。

1. 检查当前系统中是否安装MySql数据库

   如果当前系统中已经安装有MySql数据库，安装将会失败。Centos7自带mariadb，与MySql数据库冲突。

   ```apl
   rpm -qa 					   # 查询当前系统中安装的所有软件
   rpm -qa | grep mysql			# 查询当前系统中安装的名称带有mysql的软件
   rpm -qa | grep mariadb			# 查询当前系统中安装的名称带有mariadb的软件
   ```

2. 卸载已经安装的冲突软件

   ```apl
   rpm -e --nodeps 软件名称						# 卸载软件
   rpm -e --nodeps mariadb-libs-libs版本号		# 卸载mariadb软件
   ```

3. 将MySql安装包上传到Linux并且解压，解压后会得到6个乃至多个rpm的安装包文件。

   ```apl
   mkdir /export/software/mysql							# 创建一个mysql目录
   tar -xvf mysql-版本号 -C /usr/local/mysql		            # 解压至mysql目录
   ```

4. 安装依赖

   ```sh
   yum -y install libaio
   ```

5. 安装

   ```sh
   rpm -ivh mysql-community-common-5.7.29-1.el7.x86_64.rpm mysql-community-libs-5.7.29-1.el7.x86_64.rpm mysql-community-client-5.7.29-1.el7.x86_64.rpm mysql-community-server-5.7.29-1.el7.x86_64.rpm 
   ```

   如果出现报错，那么就是用下一条命令

   ```sh
   warning: mysql-community-client-5.7.28-1.el7.x86_64.rpm: Header V3 DSA/SHA1 Signature, key ID 5072e1f5: NOKEY
   # 原因：这是由于yum安装了旧版本的GPG keys造成的
   # 解决办法：后面加上 --force --nodeps
   ```

   ```sh
   rpm -ivh mysql-community-common-5.7.29-1.el7.x86_64.rpm mysql-community-libs-5.7.29-1.el7.x86_64.rpm mysql-community-client-5.7.29-1.el7.x86_64.rpm mysql-community-server-5.7.29-1.el7.x86_64.rpm  --force --nodeps
   ```

6. 初始化设置

   ```sh
   #初始化
   mysqld --initialize
   
   #更改所属组
   chown mysql:mysql /var/lib/mysql -R
   
   #启动mysql
   systemctl start mysqld.service
   
   #查看生成的临时root密码
   cat  /var/log/mysqld.log
   xWoHJtz-<3gS
   2022-11-12T09:10:39.165263Z 1 [Note] A temporary password is generated for root@localhost: wrljqkrty2%Y
   ```

7. 进入有可能报错

   ```sh
   [root@node2 ~]# mysql -u root -p
   mysql: error while loading shared libraries: libncurses.so.5: cannot open shared object file: No such file or directory
   ```

   ```sh
   # 联网状态可以安装包
   yum install libncurses* -y
   ```

8. 修改root密码 授权远程访问 设置开机自启动。这里Centos8的设置为root

   ```sh
   [root@node2 ~]# mysql -u root -p
   Enter password:     # 这里输入在日志中生成的临时密码
   Welcome to the MySQL monitor.  Commands end with ; or \g.
   Your MySQL connection id is 3
   Server version: 5.7.29
   
   Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.
   
   Oracle is a registered trademark of Oracle Corporation and/or its
   affiliates. Other names may be trademarks of their respective
   owners.
   
   Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
   
   mysql> 
   
   
   #更新root密码  设置为root123
   mysql> alter user user() identified by "root";
   Query OK, 0 rows affected (0.00 sec)
   
   
   #授权
   mysql> use mysql;
   # 这句话的意思，允许任何IP地址(上面的%就是这个意思)的电脑来访问这个MySQL Server
   mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
   
   # 权限刷新
   mysql> FLUSH PRIVILEGES;
   
   # 退出
   mysql> exit
   Bye
   
   #mysql的启动和关闭 状态查看 （这几个命令必须记住）
   systemctl stop mysqld
   systemctl status mysqld
   systemctl start mysqld
   
   #建议设置为开机自启动服务
   [root@node2 ~]# systemctl enable  mysqld
   
   Created symlink from /etc/systemd/system/multi-user.target.wants/mysqld.service to /usr/lib/systemd/system/mysqld.service.
   
   #查看是否已经设置自启动成功
   [root@node2 ~]# systemctl list-unit-files | grep mysqld
   mysqld.service                                enabled 
   
   # systemctl disable mysqld 关闭开机自启动服务
   ```

9. Centos7 干净卸载mysql 5.7

   ```sh
   #关闭mysql服务
   systemctl stop mysqld.service
   
   #查找安装mysql的rpm包
   [root@node3 ~]# rpm -qa | grep -i mysql      
   mysql-community-libs-5.7.29-1.el7.x86_64
   mysql-community-common-5.7.29-1.el7.x86_64
   mysql-community-client-5.7.29-1.el7.x86_64
   mysql-community-server-5.7.29-1.el7.x86_64
   
   #卸载
   [root@node3 ~]# yum remove mysql-community-libs-5.7.29-1.el7.x86_64 mysql-community-common-5.7.29-1.el7.x86_64 mysql-community-client-5.7.29-1.el7.x86_64 mysql-community-server-5.7.29-1.el7.x86_64
   
   #查看是否卸载干净
   rpm -qa | grep -i mysql
   
   #查找mysql相关目录 删除
   [root@node1 ~]# find / -name mysql
   /var/lib/mysql
   /var/lib/mysql/mysql
   /usr/share/mysql
   
   [root@node1 ~]# rm -rf /var/lib/mysql
   [root@node1 ~]# rm -rf /var/lib/mysql/mysql
   [root@node1 ~]# rm -rf /usr/share/mysql
   
   #删除默认配置 日志
   rm -rf /etc/my.cnf 
   rm -rf /var/log/mysqld.log
   ```

### 5.1.4 安装Redis

**Linux中操作redis-3.0.4**

在Linux中操作和Windows一样，首先启动服务器端然后再启动客户端即可。

```sh
# 启动服务器端。启动之后页面就会霸屏了
[root@node1 src]# ./redis-server
# 启动客户端。服务端启动之后页面就会霸屏了，所以重新克隆一个页面启动客户端
[root@node1 src]# ./redis-cli
```

配置环境变量：

```sh
# 配置环境变量
vim /etc/profile
# 文件添加下面内容 
export REDIS_HOME=/usr/local/redis/redis-3.0.4
export PATH=$PATH:$REDIS_HOME/src
# 刷新配置
source /etc/profile
```

修改Redis霸屏操作：

```sh
# 修改Redis霸屏操作
[root@node1 src]# vim redis.conf
# 进入vim编辑器，使用搜索命令/dae，光标定位daemonize no，修改为yes
# 重新加载配置文件
[root@node1 /usr/local/redis/redis-3.0.4]# src/redis-server ./redis.conf
```

设置密码

```sh
# 设置密码
[root@node1 src]# vim redis.conf
# 进入vim编辑器，使用搜索命令/pass，光标定位# requirepass foobared。注释取消，将foobared换成密码即可。
# 重新加载配置文件
[root@node1 /usr/local/redis/redis-3.0.4/src]# ./redis-server ./redis.conf
# 使用密码启动客户端。如果不使用密码登录进去，是无法使用命令的，可以使用auth 密码使用命令。
[root@node1 /usr/local/redis/redis-3.0.4/src]# redis-cli -h localhost -p 6379 -a 密码
```

远程连接Redis

```sh
# 修改Redis配置文件IP地址访问
[root@node1 src]# vim redis.conf
# 将bind 127.0.0.1注释起来。绑定地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0可以在任意IP访问

# windows客户端操作：打开PowerShell窗口
.\redis-cli.exe -h 192.168.66.130 -p 6379
```

**Linux中操作redis-6.2.4**

```sh
# 安装Redis所需要的依赖
yum install -y gcc tcl
# 解压缩redis-6.2.4.tar.gz
tar -xvf redis-6.2.4.tar.gz
# 进入redis目录
cd redis-6.2.4
# 运行编译命令
make && make install
```

```sh
# 修改redis.conf文件中的一些配置
vim redis.conf
# 绑定地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0则可以在任意IP访问
bind 0.0.0.0
# 数据库数量，设置为1
databases 1
```

```sh
# 启动Redis服务端
redis-server redis.conf
# 启动redis客户端
redis-cli
# 停止redis服务，就是使用的redis-cli shutdown
redis-cli shutdown
```

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
