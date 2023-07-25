<img src="..\图片\4-04【Linux基础】\0掌握程度.png" />

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
* 1984年 AndrewS.Tanenbaum 编写了一个操作系统MINIX用于向学生讲述操作系统内部工作原理。
* 1989年 Andrew S.Tanenbaum 将Minix系统运行于x86的PC平台。
* 1991年Linus Torvalds在吸收了MINIX精华的基础上，写出了Linux操作系统。Linux是一种外观和性能与UNIX相同或更好的操作系统，但Linux不源于任何版本的UNIX的源代码，并不是UNIX，而是一个类似于UNIX的产品。 

## 1.2 发行体系

发行类型如下：

* 服务器发行版：服务器要求软件包稳定、安全、久经考验。一般自带安全设施、管理工具、各类基础网络服务。服务器发行版的软件包往往很旧，依赖关系牢固。
* 工作站发行版：工作站是开发用机，要求软件包足够新，方便开发者跟进开发。开发者可以在工作站上尝试新工具，有别于服务器上只部署久经考验的稳定服务。工作站一般自带各类开发环境。
* 容器发行版：为Docker等容器量身打造的版本，为规模计算机集群而优化，着重提供自动化部署和其他集群服务能力。
* 桌面发行版：桌面主要供日常使用，要对桌面操作、键盘鼠标、音频视频播放、浏览网页提供较好的体验。一般自带各类桌面应用。软件包管理不如服务器版和工作站版规整，软件结构比较差。

服务器Linux和工作站Linux最好成套，例如都采用红帽系，或都采用SUSE系，或都采用Debian系。这样可以让开发环境和部署环境相对一致，拥有相同的工具栈，开发效率高，部署成本低。

Linux发行版一共分为三大体系：Debian系、Redhat/Fedora系、Suse/Slackware系。

<img src="..\图片\4-04【Linux基础】\1-1Linux发行体系.png" />

**Debian系**

Debian系有：Debian（服务器）、Ubuntu（桌面）、Linux Mint（桌面）。

* Debian是指一个致力于创建自由操作系统的合作组织及其作品，由于Debian项目众多内核分支中以Linux内核为主，而且 Debian开发者所创建的操作系统中绝大部分基础工具来自于GNU工程 ，因此 “Debian” 常指Debian GNU/Linux。
* Ubuntu是一个以桌面应用为主的Linux操作系统，基于Debian，与Debian的不同在于它每6个月会发布一个新版本。Ubuntu的目标在于为一般用户提供一个最新的、同时又相当稳定的主要由自由软件构建而成的操作系统。
* Linux Mint 由Linux Mint Team团队于2006年开始发行，是一份基于Debian和Ubuntu的Linux发行版。它诞生的目的是为家庭用户和企业提供一个免费的，易用的，舒适而优雅的桌面操作系统。

**Redhat/Fedora系**

Redhat/Fedora系有：Red Hat Enterprise Linux（付费服务器）、CentOS（免费服务器）、Fedora。

- Red Hat Enterprise Linux 简称RHEL，是红帽公司提供的付费服务器发行版，配套红帽公司的付费支持服务。取代Red Hat Linux在商业应用的领域。
- CentOS是RHEL的开源版，由红帽社区根据RHEL采用的软件包编译而来，免费。
- Fedora 是一款由全球社区爱好者构建的面向日常应用的快速、稳定、强大的操作系统。 它由一个强大的社群开发，这个社群的成员以自己的不懈努力，提供并维护自由、开放源码的软件和开放的标准。Fedora 项目由 Fedora 基金会管理和控制，得到了 Red Hat 的支持。

**Suse/Slackware系**

Suse/Slackware系有：SUSE（服务器）、openSUSE（工作站、桌面）、SLES（SUSE Linux Enterprise Server，企业服务器操作系统）。

* SUSE是基于Slackware二次开发的一款欧洲的企业用发行版，主要用于商业桌面、服务器。SUSE比红帽更为高端，适合大型企业使用。
* openSUSE是由SUSE发展而来，旨在推进linux的广泛使用，主要用于桌面环境，用户界面非常华丽，而且性能良好。
* SLES是企业服务器操作系统，是唯一与微软系统兼容的Linux操作系统。

**其他发行版本**

1. Gentoo：基于linux的自由操作系统，基于Linux的自由操作系统，它能为几乎任何应用程序或需求自动地作出优化和定制。追求极限的配置、性能，以及顶尖的用户和开发者社区，都是Gentoo体验的标志特点， Gentoo的哲学是自由和选择。得益于一种称为Portage的技术，Gentoo能成为理想的安全服务器、开发工作站、专业桌面、游戏系统、嵌入式解决方案或者别的东西--你想让它成为什么，它就可以成为什么。由于它近乎无限的适应性，可把Gentoo称作元发行版。
2. Aech Linux(或称Arch)：以轻量简洁为设计理念的Linux发行版。其开发团队秉承简洁、优雅和代码最小化的设计宗旨。

## 1.3 文件系统

操作系统中负责管理和存储文件信息的软件机构称为文件管理系统，简称文件系统；文件系统的结构通常叫做目录树结构，从斜杠`/`根目录开始；

Linux号称“万物皆文件”，意味着针对Linux的操作，大多数是在针对Linux文件系统操作。

<img src="..\图片\4-04【Linux基础】\1-2Linux文件系统.png" />

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

**文件类型**

文件信息中的第一个位置表示文件类型，例如`lrwxrwxrwx`中第一个`l`就代表该文件的文件类型，表示链接文件。

| 符号 | 全拼      | 文件类型                                                     |
| ---- | --------- | ------------------------------------------------------------ |
| d    | directory | 表示文件夹                                                   |
| -    |           | 表示普通文件                                                 |
| l    | linkfile  | 表示链接文件                                                 |
| b    | block     | 表示块文件。块设备文件一般指硬盘、软盘等存储设备。           |
| c    | character | 表示字符设备。一般置于/dev目录下，一次传输一个字节的设备被称为字符设备。 |
| p    | pipe      | 表示管道文件。管道文件主要用于进程间通信。                   |
| s    | socket    | 表示socket套接字文件。主要用于通信。                         |

```sh
[root@node1 /]# ll
total 24
lrwxrwxrwx.   1 root root    7 Sep 11  2020 bin -> usr/bin
dr-xr-xr-x.   5 root root 4096 Sep 11  2020 boot
```

**文件权限**

Linux系统上对文件的权限有着严格的控制，如果想对某个文件执行某种操作，必须具有对应的权限方可执行成功。

Linux下文件的权限类型有四种：可读`r`、可写`w`、可执行`x`和无权限`-`。按照数字可读`r`表示4、可写`w`表示2、可执行`x`表示1和无权限`-`表示0。

- `r(read)`：读权限。对文件是指可读取内容，对目录是可以执行`ls`命令。
- `w(write)`：写权限。对文件是指可修改文件内容，对目录是指可创建或删除子文件。
- `x(exute)`：执行权限。对文件是指是否可以运行这个文件，对目录是指是否可以`cd`进入这个目录。

文件权限可分为三个不同角色：所有者、组成员、其他成员。每个文件都可以针对三个角色，设置不同的`rwx`权限。通常情况下，一个文件只能归属于一个用户和组，如果其它的用户想有这个文件的权限，则可以将该用户加入具备权限的群组，一个用户可以同时归属于多个组。

- `u(user)`：所有者，第2-4位表示所有者的权限。
- `g(group)`：组成员，第5-7位表示所有者所属组成员的权限。
- `o(other)`：其他成员，第8-10位表示所有者所属组之外的用户的权限。
- `a(all)`：所有人， 2-10位的权限总和有时称为a权限。

<img src="..\图片\4-04【Linux基础】\1-3.png" />

Linux上通常使用`chmod`（change mode）命令对文件的权限进行设置和更改。

```sh
# 通过type命令查看chomod命令是内部命令还是外部命令
[root@linxuanVM ~]# type chmod  
chmod is hashed (/usr/bin/chmod)
# 外部命令查看帮助手册
[root@linxuanVM ~]# chmod --help
Usage: chmod [OPTION]... MODE[,MODE]... FILE...
  or:  chmod [OPTION]... OCTAL-MODE FILE...
  or:  chmod [OPTION]... --reference=RFILE FILE...
Change the mode of each FILE to MODE.
With --reference, change the mode of each FILE to that of RFILE.
...
```

```sh
# 查看文件，a.txt文件是一个文件，所有者权限为rw、组内用户权限为r、其他用户权限为r
[root@linxuanVM ~]# ll
-rw-r--r-- 1 root root    4 Jun 30 19:59 a.txt
# 修改a.txt文件的权限，所有者u权限修改为rwx、组用户g权限修改为rw、其他用户o权限修改为rw。逗号后面不要有空格
[root@linxuanVM ~]# chmod u=rwx,g=rw,o=rw a.txt  
# 修改权限成功
[root@linxuanVM ~]# ll
-rwxrw-rw- 1 root root    4 Jun 30 19:59 a.txt
# 可以通过数字来进行修改权限，r=4、w=2、x=0，它们相加为一组权限。644代表u有rw权限、g有r权限、o有r权限
[root@linxuanVM ~]# chmod 644 a.txt
# 再次查看发现权限修改成功
[root@linxuanVM ~]# ll
-rw-r--r-- 1 root root    4 Jun 30 19:59 a.txt
```

**文件信息**

<!-- recursively:递归地；verbose:冗长的；symbolic:象征的；-->

```sh
[root@linxuanVM ~]# ll
-rw-r--r-- 1 root root    4 Jun 30 19:59 a.txt
```

敲打`ll`命令后一共可以看到有完整的文件属性信息，可以大致分为七部分：

* `-rw-r--r--`：文件或者目录的属性 + 文件/目录的所有者的权限 + 文件/目录所在群组其他用户的权限 + 剩下的其他用户的权限。
* `1`：节点数，表示在系统中可以有几个地方看到这个文件，1表示1个地方，2是两个地方。
* `root`：文件/目录的所有者
* `root`：文件/目录的所在群组
* `4`：文件/目录的大小，一般默认就是字节
* `Jun 30 19:59`：文件/目录的最后的修改时间
* `a.txt`：文件/目录名字

使用 `chown`（Change owner）命令可以将指定的文件的拥有者改为指定的用户和组。管理员可以改变一切文件的所属信息，而普通用户只能改变自己文件的所属信息。

```sh
[root@linxuanVM linxuan]# type chown
chown is /usr/bin/chown
[root@linxuanVM linxuan]# chown --help
Usage: chown [OPTION]... [OWNER][:[GROUP]] FILE...
  -c, --changes          like verbose but report only when a change is made # 更改时显示详细的报告
  -f, --silent, --quiet  suppress most error messages # 无法被修改也不显示错误信息
  -h, --no-dereference   affect symbolic links instead of any referenced file # 对链接文件变更
  -R, --recursive        operate on files and directories recursively # 递归修改目录下面的文件及文件夹
Examples:
  chown root /u        Change the owner of /u to "root". # 将 /u 的属主也就是用户更改为"root"
  chown root:staff /u  Likewise, but also change its group to "staff". # 属主修改为root，组修改为staff
  chown -hR root /u    Change the owner of /u and subfiles to "root". # 
```

```sh
# 当前目录下面有一个密码.txt文件，所属用户和所属组都是linxuan
[root@linxuanVM linxuan]# ll
-rw-rw-r-- 1 linxuan linxuan 11 Mar  8 08:23 密码.txt
# 修改该文件所属用户和所属组为root
[root@linxuanVM linxuan]# chown root:root 密码.txt 
# 再次查看文件信息，发现已经被修改了
[root@linxuanVM linxuan]# ll
-rw-rw-r-- 1 root root 11 Mar  8 08:23 密码.txt
# 将文件所属组修改为linxuan，并且通过-c参数查看修改报告
[root@linxuanVM linxuan]# chown -c :linxuan 密码.txt 
changed ownership of ‘密码.txt’ from root:root to :linxuan
# 也可以使用.linxuan来修改所属组
[root@linxuanVM linxuan]# chown -c .linxuan 密码.txt  
# 修改所属用户
[root@linxuanVM linxuan]# chown -c linxuan 密码.txt
changed ownership of ‘密码.txt’ from root to linxuan
```

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

<!-- indicate:指示,表明；interpreted:诠释,说明; executable:可执行的；synopsis:概要；-->

<!-- aliases:别名,化名；builtin:内嵌；suppress:抑制；lookup:查找；pseudo:伪； -->

<!-- either...or:或者；output:输出；reserved:矜持的,内向的,保留；respectively:分别； -->

| 命令              | 解释                                                   |
| ----------------- | ------------------------------------------------------ |
| type [ 命令 ]     | 判断是内部命令 or 外部命令                             |
| help [内部命令]   | 只针对系统内部命令                                     |
| [外部命令] --help | 针对外部命令查看帮助文档                               |
| man [命令]        | 内容清晰、详细、在线文档、支持搜索                     |
| info [命令]       | 详细的帮助信息                                         |
| /usr/share/doc    | 存放帮助文档，在与软件同名的目录下有所有软件的使用文档 |

**type命令查看类型**

`type`命令用来显示指定命令的类型，判断给出的指令是内部指令还是外部指令。内部命令默认开机加载进内存中，当执行内部命令的时候就直接从内存中放到CPU里面直接运行了，外部命令对应的程序在硬盘上，就需要把硬盘中的文件加载到内存中再到CPU才可以运行。

`type`命令的结果如下：lias代表别名、keyword代表Shell保留关键字、function代表Shell函数、builtin代表Shell内建命令、file是磁盘文件代表外部命令、unfound没有找到。

```sh
# ll是ls -l --color=auto的别名
[root@linxuanVM ~]# type ll
ll is aliased to `ls -l --color=auto'
# shell中默认保留的字段
[root@linxuanVM ~]# type if
if is a shell keyword
# 通过type命令看一下type是什么样的命令，内部指令还是外部指令。type是Shell内嵌命令
[root@linxuanVM ~]# type type
type is a shell builtin
# 外部命令，直接显示其路径了
[root@linxuanVM ~]# type lscpu
lscpu is /usr/bin/lscpu
```

**help命令和--help参数**

通过`type`命令查询出来命令是内部命令或者是外部命令之后就可以通过通过`help`命令或者`--help`参数查看帮助文档，格式为`help [内部命令]`和`[外部命令] --help`

```sh
# 查看help命令是内嵌命令
[root@linxuanVM systemd]# type help
help is a shell builtin
# 看一下help的帮助文档
[root@linxuanVM systemd]# help help
help: help [-dms] [pattern ...]
    Display information about builtin commands.
    
    Displays brief summaries of builtin commands.  If PATTERN is
    specified, gives detailed help on all commands matching PATTERN,
    otherwise the list of help topics is printed.
    
    Options:
      -d        output short description for each topic # 输出每个主题的简短描述
      -m        display usage in pseudo-manpage format # 以伪 man 手册的格式显示使用方法
      -s        output only a short usage synopsis for each topic matching
        PATTERN # 为每一个匹配 PATTERN 模式的主题仅显示一个用法
    
    Arguments:
      PATTERN   Pattern specifiying a help topic
    
    Exit Status:
    Returns success unless PATTERN is not found or an invalid option is given.
```

```sh
# 查看mv命令发现是别名
[root@linxuanVM ~]# type mv
mv is aliased to `mv -i'
# 通过内部命令方式查看无法查看命令帮助文档
[root@linxuanVM ~]# help mv
-bash: help: no help topics match `mv'.  Try `help help' or `man -k mv' or `info mv'.
# 通过--help可以查看帮助文档
[root@linxuanVM ~]# mv --help
Usage: mv [OPTION]... [-T] SOURCE DEST
  or:  mv [OPTION]... SOURCE... DIRECTORY
  or:  mv [OPTION]... -t DIRECTORY SOURCE...
Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.
```

**man 命令查询帮助手册**

man 是 manual 的简写，使用 man 命令查询帮助手册时会进入 man page 界面，而非直接打印在控制台上。man 命令的信息更全，help 则显示的信息简洁。

```sh
[root@linxuanVM systemd]# man mv
# MV(1)代表对所查询信息的一个分类，1代表用户在shell环境中可操作的标准命令或可执行文件
MV(1)                      User Commands                MV(1)
# NAME：命令名称及简要说明
NAME
       mv - move (rename) files
# SYNOPSIS：命令执行语法概要
SYNOPSIS
       mv [OPTION]... [-T] SOURCE DEST
       mv [OPTION]... SOURCE... DIRECTORY
       mv [OPTION]... -t DIRECTORY SOURCE...
# DESCRIPTION：完整的语法说明
DESCRIPTION
       Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.
       Mandatory arguments to long options are mandatory for short options too.
       --backup[=CONTROL]
              make a backup of each existing destination file
# 作者
AUTHOR
       Written by Mike Parker, David MacKenzie, and Jim Meyering.
# 版权、著作权
COPYRIGHT
       Copyright   ©   2013   Free   Software   Foundation,   Inc.    License   GPLv3+:  GNU  GPL  version  3  or  later <http://gnu.org/licenses/gpl.html>. This is free software: you are free to change and redistribute it.  There is NO WARRANTY, to the extent permitted by law.
# 有关这个命令的其他说明
SEE ALSO
       rename(2)
       The  full  documentation  for  mv  is  maintained  as a Texinfo manual.  If the info and mv programs are properly
       installed at your site, the command
              info coreutils 'mv invocation'
       should give you access to the complete manual.
GNU coreutils 8.22              October 2018                               MV(1)
```

| 数字 |                   代表含义                    |
| :--: | :-------------------------------------------: |
|  1   | 用户在shell环境中可操作的标准命令或可执行文件 |
|  2   |           系统内核调用的函数及工具            |
|  3   |                 常用的库函数                  |
|  4   |             设备文件与设备说明等              |
|  5   |              配置文件或文件格式               |
|  6   |                  游戏等娱乐                   |
|  7   |                  协议信息等                   |
|  8   |           系统管理员可用的管理命令            |
|  9   |          与 Linux 内核相关的文件文档          |

man 命令相比于 help 命令最大的优势在于用户可以在 man page 中，通过按键交互进行翻页、查找等操作。常见的按键操作如下所示。

|  按键  |                        功能                        |
| :----: | :------------------------------------------------: |
| 空格键 |                        翻页                        |
|  /str  |                 向后查找str字符串                  |
|  ?str  |                 向前查找str字符串                  |
|  n, N  | n 为搜索到的下一个字符串，N 为搜索到的上一个字符串 |
|   q    |                   退出 man page                    |

**info 提供的帮助信息**

info 命令的功能基本与 man 命令相似，能够显示出命令的相关资料和信息。

而与 man 命令稍有区别的是，一方面，info 命令可以获取所查询命令相关的更丰富的帮助信息；另一方面，info page 将文件数据进行段落拆分，并以 “节点” 的形式支撑整个页面框架，并将拆分的段落与节点对应，使得用户可以在节点间跳转而方便阅读每一个段落的内容。

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
| cd          | 进个人主目录                             |
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
| more file1       | `more`用于要显示的内容会超过一个画面长度的情况。<br />空格显示下一页、回车显示下一行、 `q` 退出查看、`ctrl+c`也可结束查看，`b`返回上一页。 |
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

# 第三章 Linux网络操作

这些都是Centos6的命令，有的对于Centos7不适用

## 3.1 主机名配置

- `hostname` 查看主机名

- `hostname xxx` 修改主机名 重启后无效

  如果想要永久生效，可以修改`/etc/sysconfig/network`文件

## 3.2 IP地址配置

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

## 3.3 域名映射

`/etc/hosts`文件用于在通过主机名进行访问时做ip地址解析之用，相当于windows系统的`C:\Windows\System32\drivers\etc\hosts`文件的功能

<img src="..\图片\4-04【Linux基础】\2-1.png" />

就是我们起了一个别的名字，`127.0.0.1 == localhost`。

## 3.4 网络服务管理

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

# 第四章 Shell编程

