# 第三章Vim编辑器

<!-- yank:猛拉、拖拽；-->

vi 是一款很多 unix 及其延伸系统内置的文本编辑器，具有强大的文本编辑能力。vim 是从 vi 发展出来的一个文本编辑器，可以理解为 vi 编辑器的增强版。vim 中代码补全、编译及错误跳转等方便编程的功能特别丰富。

Vim模式如下：

- 普通模式 / 默认模式：执行 Vim 的指令，如移动光标，复制、删除 、粘贴文本等等，不能进行输入（打字）。
- 插入模式 / 编辑模式：进行文本输入（打字），和普通编辑器一样。
- 替换模式：进行字符替换。
- 可视模式 / 选择模式：进行文本选择。
- 命令模式：在 Vim 底部最后一行中输入命令，按回车并执行。

`vim /path/file`：如果打开的文件不存在，此时就是新建文件，编辑器左下角会提示 new file。如果文件已经存在，此时就打开这个文件，直接进入命令模式。仅需要敲一下键盘左上角的Esc按键即可回到普通模式（界面左下角的--INSERT--标志消失）。

## 3.1 基础模式

**普通模式**

| 移动相关快捷键   | 作用                                          |
| ---------------- | --------------------------------------------- |
| `h j k l`        | 四个按键分别代表左、下、上、右                |
| `0或者Home`      | 移动到这一行的最前面字符处                    |
| `$或者End`       | 移动到这一行的最后面字符处                    |
| `G`              | 移动到文件最后一行                            |
| `[数字n]G`       | 移动到文件的第n行                             |
| `gg`             | 移动到文件的第1行，相当于`1G`                 |
| `[数字n]<Enter>` | n 为数字。光标向下移动 n 行                   |
| `Ctrl + f`       | 屏幕『向下』移动一页，相当于「Page Down」按键 |
| `Ctrl + b`       | 屏幕『向上』移动一页，相当于「Page Up」按键   |

| 复制与粘贴相关快捷键 | 作用                                            |
| -------------------- | ----------------------------------------------- |
| `[数字n]y [motion]`  | `y`是`yank`的意思，`motion`即指定要复制的对象。 |
| `yy`                 | 拷贝当前行                                      |
| `3yy`                | 拷贝3行                                         |
| `y$`                 | 从光标所在的位置拷贝到行尾的所有字符            |
| `yG`                 | 从光标所在行拷贝到文件末尾行的所有字符          |
| `p`                  | 内容粘贴到光标之后                              |
| `P`                  | 内容粘贴到光标之前                              |

| 剪切相关快捷键 | 作用                                                         |
| -------------- | ------------------------------------------------------------ |
| `x`            | 剪切光标指定的字符，剪切单个字符                             |
| `d [motion]`   | `d`是`Delete`的意思，`motion`即指定要删除的对象。并非真删除，只是放到缓冲区中 |
| `dd`           | 剪切光标所在行的所有字符                                     |
| `dG`           | 剪切光标所在行（包含）到文件末尾的所有字符                   |
| `dgg`          | 剪切光标所在行（包含）到文件开头的所有字符                   |

| 其他常用快捷键 | 作用                                                         |
| -------------- | ------------------------------------------------------------ |
| `u`            | `u`是`undo`的意思，代表撤销最后一次修改                      |
| `U`            | 撤销对整行的修改                                             |
| `Ctrl+r`       | 恢复撤销的内容                                               |
| `ctrl+g`       | 查看当前的文件信息，比如文件名，文件状态，文件的总行数，以及光标所在的相对位置 |
| `%`            | 将光标移动到`()`、`[]`、`{}`中一半括号然后就可以找寻另一半括号。 |

**插入模式**

| 进入插入模式快捷键 | 含义                                                         |
| ------------------ | ------------------------------------------------------------ |
| `i`                | VIM切换到插入模式（界面左下角显示--INSERT--字样），此时可以自由编辑文档 |
| `I`                | 在光标所在行的行首进入插入模式                               |
| `a`                | 在光标的后边进入插入模式                                     |
| `A`                | 在光标所在行的行尾进入插入模式                               |
| `o`                | 在光标所在行的下方插入空行并进入插入模式                     |
| `O`                | 在光标所在行的上方插入空行并进入插入模式                     |
| `s`                | 删除光标指定的字符并进入插入模式                             |
| `S`                | 将光标所在行清除并进入插入模式                               |

**替换模式**

| 替换模式快捷键 | 作用                                                         |
| -------------- | ------------------------------------------------------------ |
| `r`            | 进入单字符替换模式，在该模式只能替换单个字符，然后自动返回到常规模式 |
| `R`            | 进入替换模式。屏幕左下角出现`--REPLACE--`字样，可以替换多个字符。 |
| `<Backspace>`  | 如果左边内容被替换过，则恢复到原来的样子；如果没有被替换过，则简单的向左移动 |

**可视模式**

| 可视模式相关快捷键 | 作用                                                         |
| ------------------ | ------------------------------------------------------------ |
| `v`                | 进入可视模式（左下角出现`--VISUAL--`字样），然后可以通过`h、j、k、l`来移动光标 |
| `>`                | 可视模式中用来缩进                                           |

## 3.5 命令模式

输入冒号`:`进入命令行模式，按下`/`和`?`进入命令模式中的搜索功能（两个搜索方向不同）。

| 命令行模式常用命令         | 作用                                                      |
| -------------------------- | --------------------------------------------------------- |
| `:q`                       | 退出Vim                                                   |
| `:q!`                      | 若曾修改过文件，又不想储存，使用 ! 为强制离开不储存档案。 |
| `:w`                       | 保存                                                      |
| `:wq`                      | 储存后离开，若为 `:wq!` 则为强制储存后离开                |
| `:nohl`                    | 取消搜索结果高亮                                          |
| `:set number`或者`:set nu` | 启动绝对行号                                              |
| `:set rnu`                 | 启动相对行号                                              |
| `:set nornu`               | 关闭相对行号显示                                          |

**搜索**

|              | **/目标**      | **?目标**      |
| ------------ | -------------- | -------------- |
| **搜索方向** | 从光标位置向后 | 从光标位置向前 |
| **n**        | 向后搜索下一个 | 向前搜索下一个 |
| **N**        | 向前搜索下一个 | 向后搜索下一个 |

> 最后提个醒：在搜索命令中，`.`、`*`、`[`、`]`、`^`、`%`、`/`、`?`、`~`和`$`这10个字符有着特殊意义，所以在使用这些字符的时候要在前面加上一个反斜杠（`\`），比如你要搜索问号，则输入`/\?`

**高级替换**

- `:s/old/new`，这样即可将光标所在行的第一个`old`替换为`new`；
- `:s/old/new/g`，则表示将光标所在行的所有`old`替换为`new`。
- `:%s/old/new/g`表示替换整个文件中每个匹配的字符串。
- `:5,13s/old/new/g`即可胜任你的要求。5表示开始替换的行号，13表示结束替换的行号

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

3. Yum在线安装：Yum(全称为Yellow dog Updater, Modified)是一个在Fedora和RedHat以及CentoS中的Shell前端软件包管理器。基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖关系，并且一次安装所有依赖的软件包，无须繁琐地一次次下载、安装。

4. 源码编译安装：软件以源码工程的形式发布，需要获取到源码工程后用相应开发工具进行编译打包部署。


文件上传与下载工具介绍

1. `FileZilla`， 我们已经下载了。

2. `lrzsz`，我们可以使用yum安装方式安装 `yum install lrzsz`。可以在crt中设置上传与下载目录 在命令行输入`rz`就可以上传文件了。

3. `sftp`，使用alt + p 组合键打开sftp窗口、使用put 命令上传、使用get命令下载。

```sh
# /export/目录下面创建这三个文件夹 用于存放 数据 软件 安装包
[root@linxuan ~]# ls /export/
data  server  software
```

### 5.1.1 安装jdk

```sh
[root@node1 ~]# cd /export/software/
[root@node1 software]# ll
total 181168
-rw-r--r-- 1 root root 185515842 Jul 21 17:45 jdk8.tar.gz
[root@node1 software]# tar -zxvf jdk8.tar.gz -C ../server
[root@node1 software]# cd ../server/jdk1.8.0_144/
[root@node1 jdk1.8.0_144]# pwd
/export/server/jdk1.8.0_144
# 配置环境变量
[root@node1 software]# vim /etc/profile
JAVA_HOME=/export/server/jdk1.8.0_144
PATH=$JAVA_HOME/bin:$PATH
[root@node1 software]# source /etc/profile
[root@node1 ~]# java -version
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

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
   error: Failed dependencies:
           mariadb-libs is obsoleted by mysql-community-libs-5.7.29-1.el7.x86_64
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
   cat  /var/log/mysqld.log | grep root@localhost:
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
   mysql> alter user user() identified by "root123";
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

```sh
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

# 第一章 Nginx 基础

[Nginx](https://nginx.org/) 是一款高性能的 http 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。由俄罗斯的程序设计师伊戈尔·西索夫（Igor Sysoev）所开发，官方测试 nginx 能够支支撑 5 万并发链接，并且 cpu、内存等资源消耗却非常低，运行非常稳定。

Nginx 应用场景：

1. http 服务器。Nginx 是一个 http 服务可以独立提供 http 服务。可以做网页静态服务器。
2. 虚拟主机。可以实现在一台服务器虚拟出多个网站。例如个人网站使用的虚拟主机。
3. 反向代理，负载均衡。当网站的访问量达到一定程度后，单台服务器不能满足用户的请求时，需要用多台服务器集群可以使用 nginx 做反向代理。并且多台服务器可以平均分担负载，不会因为某台服务器负载高宕机而某台服务器闲置的情况。 

```sh
# 下载前进行操作，同样也是卸载操作，如果需要卸载也可以怎么做
# 如果有nginx在运行中查找出来，使用kill -9 端口号 将进程干掉
[root@linxuanVM software]# ps -ef | grep nginx
# 查看所有与Nginx有关的文件夹
[root@linxuanVM software]# find / -name nginx
# 删除与Nginx有关的文件夹
[root@linxuanVM software]# rm -rf file 文件夹

# 安装编译所需依赖包
[root@linxuanVM software]# yum -y install gcc pcre-devel zlib-devel openssl openssl-devel
# 下载wget可以从网络上下载包
[root@linxuanVM software]# yum install wget
# 使用wget获取nginx，如果遇到下面情况，那么需要将其删除然后重新下载
[root@linxuanVM software]# wget https://nginx.org/download/nginx-1.16.1.tar.gz
-bash: wget: command not found
[root@linxuanVM software]# yum remove wget
[root@linxuanVM software]# yum -y install wget

# 解压
[root@linxuanVM software]# tar -zxvf nginx-1.16.1.tar.gz -C ../server/
# 进入解压目录
[root@linxuanVM software]# cd ../server/nginx-1.16.1/
# 软件安装地址，一会编译之后就会安装的地址
[root@linxuanVM software]# ./configure --prefix=/opt/server/nginx
# 编译及安装
[root@linxuanVM software]# make && make install
# 软件就安装好了
[root@linxuanVM server]# ll nginx
total 16
drwxr-xr-x 2 root root 4096 May  7 20:39 conf
drwxr-xr-x 2 root root 4096 May  7 20:39 html
drwxr-xr-x 2 root root 4096 May  7 20:39 logs
drwxr-xr-x 2 root root 4096 May  7 20:39 sbin
```

可以通过修改profile文件配置环境变量，在任意目录下可以直接使用nginx命令

```sh
# 使用VIM打开profile配置文件
vim etc/profile
# 在文件最下面一行添加如下代码
PATH=/usr/local/nginx/sbin:$JAVA_HOME/bin:$PATH
# 使配置文件生效
source /etc/profile
```

## 1.1 Nginx 目录结构

安装完Nginx后，我们先来熟悉一下Nginx的目录结构，如下图：

```apl
[root@linxuanVM nginx]# tree
.
├── conf # 配置文件
│   ├── fastcgi.conf
│   ├── fastcgi.conf.default
│   ├── fastcgi_params
│   ├── fastcgi_params.default
│   ├── koi-utf
│   ├── koi-win
│   ├── mime.types
│   ├── mime.types.default
│   ├── nginx.conf # nginx配置文件
│   ├── nginx.conf.default
│   ├── scgi_params
│   ├── scgi_params.default
│   ├── uwsgi_params
│   ├── uwsgi_params.default
│   └── win-utf
├── html # 存放静态文件(html、css、Js等)
│   ├── 50x.html
│   └── index.html
├── logs # 日志目录，存放日志文件
└── sbin # 二进制文件，用于启动、停止Nginx服务
    └── nginx
```

## 1.2 Nginx 常用命令

| 命令                 | 作用                                                    |
| -------------------- | ------------------------------------------------------- |
| ./nginx -v           | 查看Nginx版本（在sbin目录使用）                         |
| ./nginx -t           | 检查conf/nginx.conf文件配置是否有错误（在sbin目录使用） |
| ./nginx              | 启动Nginx（在sbin目录使用）                             |
| ./nginx -s stop      | 停止Nginx服务（在sbin目录使用）                         |
| ps -ef \| grep nginx | 查看Nginx进程                                           |
| ./nginx -s reload    | 重启服务 重新加载配置文件（在sbin目录使用）             |

```sh
# 查看版本
[root@linxuanVM sbin]# ./nginx -v
nginx version: nginx/1.16.1
# 查看配置文件是否有错误
[root@linxuanVM sbin]# ./nginx -t
nginx: the configuration file /opt/server/nginx/conf/nginx.conf syntax is ok
nginx: configuration file /opt/server/nginx/conf/nginx.conf test is successful
# 启动nginx
[root@linxuanVM sbin]# ./nginx
# 停止nginx
[root@linxuanVM sbin]# ./nginx -s stop
```

如果已经启动服务，我们想要访问那么只需要在物理机浏览器上面输入服务器的IP地址即可，因为Nginx的端口号默认是80。防火墙的话我们已经关闭了，浏览器访问的是/nginx/html/index.html页面。

## 1.3 nginx.conf 介绍

Nginx配置文件（conf/nginx.conf）整体分为三部分：全局块、events块、http块。

* 全局块：和Nginx运行相关的全局配置
* events块：和网络连接相关的配置
* http块：代理、缓存、日志记录、虚拟主机配置。这里面又分为了http全局块以及Server块，Server块又分为了Server全局块以及Location块。http块中可以配置多个Server块，每个Server块中可以配置多个location块。

```sh
[root@linxuanVM conf]# cat nginx.conf

# 全局块，和Nginx运行相关的配置。
# 该命令为进程数目，可以修改一下，设置进程数目为2，但是这里就不设置了
worker_processes  1;

# events块，和网络连接相关的配置
events {
    worker_connections  1024;
}

# http块，代理、缓存、日志记录、虚拟主机配置。又分为了http全局块以及Server块。
http {
    # http全局块
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
    
    # server块，http块中可以配置多个Server块
    server {
        # server全局块
        listen       80;
        server_name  localhost;
        
        # location块，每个Server块中可以配置多个location块
        location / {
            root   html;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        # location块，每个Server块中可以配置多个location块
        location = /50x.html {
            root   html;
        }
    }
}
```

## 1.4 Nginx 具体应用

Nginx 具体应用如下：部署静态资源、反向代理、负载均衡。

### 1.4.1 部署静态资源

Nginx可以作为静态web服务器来部署静态资源。静态资源指在服务端真实存在并且能够直接展示的一些文件，比如常见的html页面、css文件、js文件、图片、视频等资源。

相对于Tomcat，Nginx处理静态资源的能力更加高效，所以在生产环境下，一般都会将静态资源部署到Nginx中。将静态资源部署到Nginx非常简单，只需要将文件复制到Nginx安装目录下的html目录中即可。

```apl
# 这个server块原本就有，所以这里我们不用动
server {
  listen 80;                # 监听端口
  server_name localhost;    # 服务器名称
  location / {              # 匹配客户端请求url
    root html;              # 指定静态资源根目录
    index index.html;       # 指定默认首页
}
```

### 1.4.2 反向代理

先来说一下正向代理。所谓正向代理就是是一个位于客户端和原始服务器(origin server)之间的服务器，为了从原始服务器取得内容，客户端向代理发送一个请求并指定目标(原始服务器)，然后代理向原始服务器转交请求并将获得的内容返回给客户端。

正向代理一般是在客户端设置代理服务器，通过代理服务器转发请求，最终访问到目标服务器。正向代理的典型用途是为在防火墙内的局域网客户端提供访问Internet的途径。

![](..\图片\4-04【Linux软件】\1-2.png)

------

反向代理服务器位于用户与目标服务器之间，但是对于用户而言，反向代理服务器就相当于目标服务器，即用户直接访问反向代理服务器就可以获得目标服务器的资源，反向代理服务器负责将请求转发给目标服务器。

用户不需要知道目标服务器的地址，也无须在用户端作任何设定。

![](..\图片\4-04【Linux软件】\1-3.png)

```apl
# 配置反向代理
server {
  listen       82;
  server_name  localhost;

  location / {
    # 反向代理配置
    proxy_pass http://192.168.188.101:8080; 
  } 
}
```

### 1.4.3 负载均衡

早期的网站流量和业务功能都比较简单，单台服务器就可以满足基本需求，但是随着互联网的发展，业务流量越来越大并且业务逻辑也越来越复杂，单台服务器的性能及单点故障问题就凸显出来了，因此需要多台服务器组成应用集群，进行性能的水平扩展以及避免单点故障出现。

我们可以使用负载均衡器将用户请求根据对应的负载均衡算法分发到应用集群中的一台服务器进行处理。

| 负载均衡算法名称 |       说明       |
| :--------------: | :--------------: |
|       轮询       |     默认方式     |
|      weight      |     权重方式     |
|     IP_hash      |  根据IP分配方式  |
|    least_conn    | 依据最少连接方式 |
|     url_hash     | 依据url连接方式  |
|       fair       | 依据相应时间方式 |

```apl
# upstream 指令可以定义一组服务器
upstream targetserver{    
  server 192.168.188.101:8080;
  server 192.168.188.101:8081;
}

server {
  listen  8080;
  server_name  localhost;
  location / {
    proxy_pass http://targetserver;
  }
}
```

## 1.5 配置虚拟主机

虚拟主机，也叫“网站空间”，就是把一台运行在互联网上的物理服务器划分成多个“虚拟”服务器。虚拟主机技术极大的促进了网络技术的应用和普及。同时虚拟主机的租用服务也成了网络时代的一种新型经济形式。

### 1.5.1 端口绑定

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


### 1.5.2 域名绑定

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

```sh
[root@localhost sbin]# ./nginx -s reload
```

测试：

```apl
地址栏输入http://www.hmtravel.com/
```

# 第二章 Lua

Lua 是一种轻量小巧的脚本语言，用标准C语言编写并以源代码形式开放， 其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。Lua经常嵌入到C语言开发的程序中，例如游戏开发、游戏插件等。Nginx本身也是C语言开发，因此也允许基于Lua做拓展。官网：https://www.lua.org/

HelloWorld案例如下：

```sh
# 新建一个hello.lua文件
touch hello.lua
# 添加如下内容
print("Hello World!")
# 运行该文件
lua hello.lua
```

## 2.1 数据类型

Lua中支持的常见数据类型包括：

| 数据类型 | 描述                                                         |
| -------- | ------------------------------------------------------------ |
| nil      | 这个最简单，只有值nil属于该类，标识一个无效值。条件表达式中等于false。 |
| boolean  | 包含两个值：false和true                                      |
| number   | 标识双精度类型的实浮点数                                     |
| string   | 字符串由一对双引号或者单引号表示                             |
| function | 由C或者Lua编写的函数                                         |
| table    | 关联数组，数组的索引可以是数字、字符串或者表类型。创建通过构造表达式完成 `{}`创建空表 |

另外，Lua提供了`type()`函数来判断一个变量的数据类型：

```sh
[root@192 lua]# lua
Lua 5.1.4  Copyright (C) 1994-2008 Lua.org, PUC-Rio
> print(type("hello world"))
string
> print(type(123))
number
> print(type(print))
function
```

## 2.2 变量和循环

**变量**

Lua声明变量的时候无需指定数据类型，而是用local来声明变量为局部变量：

```lua
-- 声明字符串，可以用单引号或双引号，
local str = 'hello'
-- 字符串拼接可以使用 ..
local str2 = 'hello' .. 'world'
-- 声明数字
local num = 21
-- 声明布尔类型
local flag = true
```

Lua中的table类型既可以作为数组，又可以作为Java中的map来使用。数组就是特殊的table，key是数组角标而已：

```lua
-- 声明数组 ，key为角标的 table
local arr = {'java', 'python', 'lua'}
-- 声明table，类似java的map
local map =  {name='Jack', age=21}
```

Lua中的数组角标是从1开始，访问的时候与Java中类似：

```lua
-- 访问数组，lua数组的角标从1开始
print(arr[1])
```

Lua中的table可以用key来访问：

```lua
-- 访问table
print(map['name'])
print(map.name)
```

**循环**

对于table，我们可以利用for循环来遍历。不过数组和普通table遍历略有差异。

遍历数组：

```lua
-- 声明数组 key为索引的 table
local arr = {'java', 'python', 'lua'}
-- 遍历数组
for index, value in ipairs(arr) do
    print(index, value) 
end
```

遍历普通table

```lua
-- 声明map，也就是table
local map = {name='Jack', age=21}
-- 遍历table
for key, value in pairs(map) do
   print(key, value) 
end
```

## 2.3 流程控制

类似Java的条件控制，例如if、else语法：

```lua
if(布尔表达式)
then
   --[ 布尔表达式为 true 时执行该语句块 --]
else
   --[ 布尔表达式为 false 时执行该语句块 --]
end
```

与java不同，布尔表达式中的逻辑运算是基于英文单词：

| 操作符 | 描述                                                         | 实例                  |
| ------ | ------------------------------------------------------------ | --------------------- |
| and    | 逻辑与操作符。A为false，则返回A，否则返回B。                 | (A and B) 为 false    |
| or     | 逻辑或操作符。A为true，则返回A，否则返回B。                  | (A or B) 为 true      |
| not    | 逻辑非操作符。与逻辑运算结果相反，如果条件为true，逻辑非为false | not (A and B) 为 true |

## 2.4 函数

Lua中的条件控制和函数声明与Java类似。定义函数的语法：

```lua
function 函数名(argument1, argument2..., argumentn)
    -- 函数体
    return 返回值
end
```

例如，定义一个函数，用来打印数组：

```lua
function printArr(arr)
    for index, value in ipairs(arr) do
        print(value)
    end
end
```

