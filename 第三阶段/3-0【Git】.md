![](D:\Java\笔记\图片\3-day31【Linux】\屏幕截图 2022-04-21 081544.png)

# 第一章 概述及安装

## 1.1 版本控制器的方式

1. 集中式版本控制工具

   集中式版本控制工具，版本库是集中存放在中央服务器的，team里每个人work时从中央服务器下载代 码，是必须联网才能工作，局域网或互联网。个人修改后然后提交到中央版本库。

   举例：SVN和CVS

2. 分布式版本控制工具

   分布式版本控制系统没有"中央服务器"，每个人的电脑上都是一个完整的版本库，这样工作的时候，无 需要联网了，因为版本库就在你自己的电脑上。多人协作只需要各自的修改推送给对方，就能互相看到对方的 修改了。

   举例：Git

## 1.2 Git

Git是分布式的,Git不需要有中心服务器，我们每台电脑拥有的东西都是一样的。我们使用Git并且有个 中心服务器，仅仅是为了方便交换大家的修改，但是这个服务器的地位和我们每个人的PC是一样的。我们可以 把它当做一个开发者的pc就可以就是为了大家代码容易交流不关机用的。没有它大家一样可以工作，只不过"交换"修改不方便而已。

git是一个开源的分布式版本控制系统，可以有效、高速地处理从很小到非常大的项目版本管理。

Git是 Linus Torvalds 为了帮助管理 Linux 内核开发而开发的一个开放源码的版本控制软件。 <font color = "red">所以我们可以在Git里面进行很多的Linux操作。</font>

同生活中的许多伟大事物一样，Git 诞生于一个极富纷争大举创新的年代。Linux 内核开源项目有着为数众 多的参与者。 绝大多数的 Linux 内核维护工作都花在了提交补丁和保存归档的繁琐事务上（1991－2002 年间）。 到 2002 年，整个项目组开始启用一个专有的分布式版本控制系统 BitKeeper 来管理和维护代 码。

到了 2005 年，开发 BitKeeper 的商业公司同 Linux 内核开源社区的合作关系结束，他们收回了 Linux 内核社区免费使用 BitKeeper 的权力。 这就迫使 Linux 开源社区（特别是 Linux 的缔造者 Linus Torvalds）基于使用 BitKeeper 时的经验教训，开发出自己的版本系统。 他们对新的系统制订 了若干目标：

- 速度

- 简单的设计

- 对非线性开发模式的强力支持（允许成千上万个并行开发的分支）

- 完全分布式

- 有能力高效管理类似 Linux 内核一样的超大规模项目（速度和数据量）


## 1.3 Git命令

命令如下：

1. clone（克隆）: 从远程仓库中克隆代码到本地仓库
2. checkout （检出）:从本地仓库中检出一个仓库分支然后进行修订
3. add（添加）: 在提交前先将代码提交到暂存区
4. commit（提交）: 提交到本地仓库。本地仓库中保存修改的各个历史版本
5. fetch (抓取) ： 从远程库，抓取到本地仓库，不进行任何的合并动作，一般操作比较少。 6. pull (拉取) ： 从远程库拉到本地库，自动进行合并(merge)，然后放到到工作区，相当于fetch+merge
6. push（推送） : 修改完成后，需要和团队成员共享代码时，将代码推送到远程仓库

## 1.4 下载与安装

到官网下载安装文件。双击下载的安装文件来安装Git。安装完成后在电脑桌面（也可以是其他目录）点击右键，如果能够看到新增加两个菜单则说明Git安装成功。

- Git GUI：Git提供的图形界面工具

- Git Bash：Git提供的命令行工具

## 1.5 基本配置

<font color = "red">当安装Git后首先要做的事情是设置用户名称和email地址。</font>这是非常重要的，因为每次Git提交都会使用该用户信息。如果不设置的话，那么有的步骤是会出错的。

1. 打开Git Bash

2. 设置用户信息

   ```ABAP
   git conﬁg --global user.name "名称"
   git conﬁg --global user.email "邮箱@.mail"
   ```

3. 查看配置信息

   ```ABAP
   git conﬁg --global user.name
   git conﬁg --global user.email
   ```

# 第二章 Git常用命令

## 2.2 获取本地仓库 

要使用Git对我们的代码进行版本控制，首先需要获得本地仓库

1. 在电脑的任意位置创建一个空目录（例如test）作为我们的本地Git仓库
2. 进入这个目录中，点击右键打开Git bash窗口
3. 执行命令`git init`命令。
4. 如果创建成功后可在文件夹下看到隐藏的.git目录。

## 2.3 基础操作指令

Linux的常用命令：

| 命令名称 | 作用                     |
| -------- | ------------------------ |
| dd       | 删除光标所在行的所有字符 |

Git工作目录下对于文件的修改(增加、删除、更新)会存在几个状态，这些修改的状态会随着我们执行Git 的命令而发生变化。

| 命令名称                             | 作用             |
| ------------------------------------ | ---------------- |
| git config --global user.name 用户名 | 设置用户签名     |
| git config --global user.email 邮箱  | 设置用户签名     |
| git init                             | 初始化本地库     |
| git status                           | 查看本地库状态   |
| git add 文件名                       | 添加到暂存区     |
| git commit -m "日志信息" 文件名      | 提交到本地库     |
| git reflog                           | 查看历史记录     |
| git log                              | 查看详细历史记录 |
| git reset --hard 版本号              | 版本穿梭         |

* `git status`：查看的修改的状态（暂存区、工作区）

* `git add 单个文件名|通配符`：添加工作区一个或多个文件的修改到暂存区

  * `git add .`：将所有修改加入暂存区

* `git commit -m '注释内容'`：提交暂存区内容到本地仓库的当前分支

* `git log [option]`：查看提交日志(log)，查看提交记录
* `options`的参数如下：
  * `--all` 显示所有分支
  
  * `--pretty=oneline` 将提交信息显示为一行
  
  * `--abbrev-commit` 使得输出的commitId更简短
  
  * `--graph` 以图的形式显示

* `git reset --hard commitID`：版本切换

  * `commitID` 可以使用 `git-log`或 `git log`指令查看
  * 如何查看已经删除的记录？`git reﬂog`：这个指令可以看到已经删除的提交记录

# 第三章 分支

## 3.1 介绍分支

在版本控制过程中，同时推进多个任务，为每个任务，我们就可以创建每个任务的单独分支。使用分支意味着程序员可以把自己的工作从开发主线上分离开来，开发自己分支的时候，不会影响主线分支的运行。对于初学者而言，分支可以简单理解为副本，一个分支就是一个单独的副本。（分支底层其实也是指针的引用）

几乎所有的版本控制系统都以某种形式支持分支。 使用分支意味着你可以把你的工作从开发主线上分离开来进行重大的Bug修改、开发新的功能，以免影响开发主线。

## 3.2 分支常用命令

各种关于分支的命令如下：

| 命令名称            | 作用                         |
| ------------------- | ---------------------------- |
| git branch 分支名   | 创建分支                     |
| git branch -v       | 查看分支                     |
| git checkout 分支名 | 切换分支                     |
| git merge 分支名    | 把指定的分支合并到当前分支上 |

* `git branch`：查看本地分支

* `git branch 分支名`：创建本地分支

* `git checkout 分支名`：切换分支(checkout)

  > `git checkout -b 分支名`：我们还可以直接切换到一个不存在的分支（创建并切换）

* `git merge 分支名称`：一个分支上的提交可以合并到另一个分支，合并分支(merge)

* `git branch -d b1`：删除分支时，需要做各种检查

* `git branch -D b1`：不做任何检查，强制删除

  > 不能删除当前分支，只能删除其他分支

## 3.3 分支冲突

冲突产生的表现：后面状态为MERGING 

```asciiarmor
Layne@LAPTOP-Layne MINGW64 /d/Git-Space/SH0720 (master|MERGING) 
```

冲突产生的原因： 

* 合并分支时，两个分支在同一个文件的同一个位置有两套完全不同的修改。Git 无法替我们决定使用哪一个。必须人为决定新代码内容。 查看状态（检测到有文件有两处修改） 

当两个分支上对文件的修改可能会存在冲突，例如同时修改了同一个文件的同一行，这时就需要手动解决冲突，解决冲突步骤如下：

1. 处理文件中冲突的地方，编辑有冲突的文件，删除特殊符号，决定要使用的内容。
2. 将解决完冲突的文件加入暂存区(add)，`git add 文件名称`。
3. 提交到仓库(commit)，<font color = "red">这个时候提交到仓库的时候不要加上文件名称，只使用如下命令就可以了：`git commit -m "文件信息"`。</font>

> master、hot-fix 其实都是指向具体版本记录的指针。当前所在的分支，其实是由HEAD决定的。所以创建分支的本质就是多创建一个指针。 
>
> * HEAD 如果指向master，那么我们现在就在master 分支上。 
> * HEAD 如果执行hotfix，那么我们现在就在hotfix 分支上。 
>
> 所以切换分支的本质就是移动HEAD 指针。 

## 3.4 分支使用原则与流程

几乎所有的版本控制系统都以某种形式支持分支。 使用分支意味着你可以把你的工作从开发主线上分离 开来进行重大的Bug修改、开发新的功能，以免影响开发主线。

在开发中，一般有如下分支使用原则与流程：

* master （生产） 分支

  线上分支，主分支，中小规模项目作为线上运行的应用对应的分支；

* develop（开发）分支

  是从master创建的分支，一般作为开发部门的主要开发分支，如果没有其他并行开发不同期上线 要求，都可以在此版本进行开发，阶段开发完成后，需要是合并到master分支,准备上线。

* feature/xxxx分支

  从develop创建的分支，一般是同期并行开发，但不同期上线时创建的分支，分支上的研发任务完 成后合并到develop分支。

* hotﬁx/xxxx分支，

  从master派生的分支，一般作为线上bug修复使用，修复完成后需要合并到master、test、develop分支。

还有一些其他分支，在此不再详述，例如test分支（用于代码测试）、pre分支（预上线分支）等 等。

# 第四章 Git 团队协作机制 

## 4.1 团队内协作 

![](D:\Java\笔记\图片\3-0【Git】\1-1.png)

## 4.2 跨团队协作 

![](D:\Java\笔记\图片\3-0【Git】\1-2.png)

# 第五章 GitHub操作

GitHub 网址：https://github.com/ 
Ps:全球最大同性交友网站，技术宅男的天堂，新世界的大门，你还在等什么? 

## 5.1 创建远程仓库 

```apl
https://github.com/linxuan137/git-demo.git
```

## 5.2 远程仓库操作

- **创建远程仓库别名**
- **推送本地分支到远程仓库** 
- **拉取分支到本地仓库**
- **克隆远程仓库到本地** 

常用命令如下：

| 命令名称                           | 作用                                                     |
| ---------------------------------- | -------------------------------------------------------- |
| git remote -v                      | 查看当前所有远程地址别名                                 |
| git remote add 别名 远程地址       | 起别名                                                   |
| git push 别名 分支                 | 推送本地分支上的内容到远程仓库                           |
| git clone 远程地址                 | 将远程仓库的内容克隆到本地                               |
| git pull 远程库地址别名 远程分支名 | 将远程仓库对于分支最新内容拉下来后与当前本地分支直接合并 |

**创建远程仓库别名**

- `git remote -v` ：查看当前所有远程地址别名 

- `git remote add 别名 远程地址` ：起别名 

- 我们的仓库的地址是：`https://github.com/linxuan137/git-demo.git`，可是该命令很长，所以我们可以直接为他起别名。

  ```apl
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ git remote -v
  
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ git remote add git-demo https://github.com/linxuan137/git-demo.git 
  
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ git remote -v
  git-demo        https://github.com/linxuan137/git-demo.git (fetch)
  git-demo        https://github.com/linxuan137/git-demo.git (push)
  ```

**推送本地分支到远程仓库** 

* `git push 别名 分支` ：推送本地分支上的内容到远程仓库。<font color = "red">注意：最小单位是分支，所以必须加上分支名称。</font>

  ```apl
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ git push git-demo master
  Enumerating objects: 18, done.
  Counting objects: 100% (18/18), done.
  Delta compression using up to 8 threads
  Compressing objects: 100% (12/12), done.
  Writing objects: 100% (18/18), 1.31 KiB | 335.00 KiB/s, done.
  Total 18 (delta 3), reused 0 (delta 0), pack-reused 0
  remote: Resolving deltas: 100% (3/3), done.
  To https://github.com/linxuan137/git-demo.git
   * [new branch]      master -> master
  ```

  这样就是拉取到GitHub上面了。

**拉取分支到本地仓库**

* `git pull 远程库地址别名 远程分支名`：将远程仓库对于分支最新内容拉下来后与当前本地分支直接合并。

  ```apl
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ cat hello.txt
  hello 123
  hello
  hello
  hello
  hello
  hello
  hello linxuan
  hello star
  
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ git pull git-demo master
  remote: Enumerating objects: 5, done.
  remote: Counting objects: 100% (5/5), done.
  remote: Compressing objects: 100% (2/2), done.
  remote: Total 3 (delta 1), reused 0 (delta 0), pack-reused 0
  Unpacking objects: 100% (3/3), 638 bytes | 49.00 KiB/s, done.
  From https://github.com/linxuan137/git-demo
   * branch            master     -> FETCH_HEAD
     d70a1c5..b593667  master     -> git-demo/master
  Updating d70a1c5..b593667
  Fast-forward
   hello.txt | 1 +
   1 file changed, 1 insertion(+)
  
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ git status
  On branch master
  nothing to commit, working tree clean
  
  林轩@DESKTOP-PA8B4PG MINGW64 /e/git/git-demo (master)
  $ cat hello.txt
  hello 123
  hello
  hello
  hello
  hello
  hello
  hello linxuan
  hello star
  111111111111111
  ```

  > 这条命令执行之后拉取到的代码文件会自动添加并且提交保存。

**克隆远程仓库到本地** 

* `git clone 远程地址` ：将远程仓库的内容克隆到本地 

  进行该项的时候我们首先需要将自己本地为Git官网设置的密码删掉，这是因为克隆的话不需要登陆。

  * 然后我们随便创建一个文件夹，我们可以看到文件夹是空的。

  * 查看起的别名也是为空，说明：别名只在起别名的文件夹下面存在。当然，这里我们甚至都没有`git init`。

  ```elm
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test
  $ ll
  total 0
  
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test
  $ git remote -v
  fatal: not a git repository (or any of the parent directories): .git
  
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test
  $ git clone https://github.com/linxuan137/git-demo.git
  Cloning into 'git-demo'...
  remote: Enumerating objects: 21, done.
  remote: Counting objects: 100% (21/21), done.
  remote: Compressing objects: 100% (11/11), done.
  remote: Total 21 (delta 4), reused 17 (delta 3), pack-reused 0
  Receiving objects: 100% (21/21), done.
  Resolving deltas: 100% (4/4), done.
  
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test
  $ ll
  total 0
  drwxr-xr-x 1 林轩 197121 0 Apr 22 13:57 git-demo/
  
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test
  $ cd git-demo/
  
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test/git-demo (master)
  $ ll
  total 1
  -rw-r--r-- 1 林轩 197121 97 Apr 22 13:57 hello.txt
  
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test/git-demo (master)
  $ git status
  On branch master
  Your branch is up to date with 'origin/master'.
  
  nothing to commit, working tree clean
  
  林轩@DESKTOP-PA8B4PG MINGW64 /d/Java/笔记/第三阶段/git-test/git-demo (master)
  $ git remote -v
  origin  https://github.com/linxuan137/git-demo.git (fetch)
  origin  https://github.com/linxuan137/git-demo.git (push)
  ```

  > 小结：clone 会做如下操作。1、拉取代码。2、初始化本地仓库。3、创建别名 

## 5.3 邀请加入团队

1. 在岳不群的账号的项目里面选择邀请合作者 
2. 填入想要合作的人的账号
3. 复制地址并通过微信钉钉等方式发送给该用户，复制内容如下：`https://github.com/atguiguyueyue/git-shTest/invitations` 
4. 在atguigulinghuchong 这个账号中的地址栏复制收到邀请的链接，点击接受邀请。 
5. 成功之后可以在atguigulinghuchong 这个账号上看到git-Test 的远程仓库。 
6. 令狐冲可以修改内容并push 到远程仓库。 
7. 回到atguiguyueyue 的GitHub 远程仓库中可以看到，最后一次是lhc 提交的。 

## 5.4 跨团队协作 

1. 将远程仓库的地址复制发给邀请跨团队协作的人，比如东方不败。 

2. 在东方不败的GitHub 账号里的地址栏复制收到的链接，然后点击Fork 将项目叉到自
   己的本地仓库。 

3. 东方不败就可以在线编辑叉取过来的文件。 

4. 编辑完毕后，填写描述信息并点击左下角绿色按钮提交。 

5. 接下来点击上方的Pull 请求，并创建一个新的请求。 

6. 回到岳岳GitHub 账号可以看到有一个Pull request 请求。 

   进入到聊天室，可以讨论代码相关内容。 

7. 如果代码没有问题，可以点击Merge pull reque 合并代码。 

## 5.5 SSH 免密登录 

我们可以看到远程仓库中还有一个SSH 的地址，因此我们也可以使用SSH 进行访问。

```bash
--进入当前用户的家目录
C:\Users\林轩

--运行命令生成.ssh秘钥目录[注意：这里-C这个参数是大写的C] 
$ ssh-keygen -t rsa -C linxuan123_n@163.com
```

这样就创建好了SSH秘钥目录了。

复制id_rsa.pub 文件内容，登录GitHub，点击用户头像→Settings→SSH and GPG keys 

# 第六章 GitLab

## 6.1 GitLab 简介 

GitLab 是由GitLabInc.开发，使用MIT 许可证的基于网络的Git 仓库管理工具，且具有wiki 和issue 跟踪功能。使用Git 作为代码管理工具，并在此基础上搭建起来的web 服务。 

GitLab 由乌克兰程序员DmitriyZaporozhets 和ValerySizov 开发，它使用Ruby 语言写成。后来，一些部分用Go 语言重写。截止2018 年5 月，该公司约有290 名团队成员，以及2000 多名开源贡献者。GitLab 被IBM，Sony，JülichResearchCenter，NASA，Alibaba，Invincea，O’ReillyMedia，Leibniz-Rechenzentrum(LRZ)，CERN，SpaceX 等组织使用。 

GitLab 官网地址如下：

- 官网地址：https://about.gitlab.com/ 
- 安装说明：https://about.gitlab.com/installation/ 

## 6.2 GitLab 安装 

**服务器准备** 

- 准备一个系统为CentOS7 以上版本的服务器，要求内存4G，磁盘50G。 
- 关闭防火墙，并且配置好主机名和IP，保证服务器可以上网。 

**安装包准备** 

Yum 在线安装gitlab- ce 时，需要下载几百M 的安装文件，非常耗时，所以最好提前把所需RPM 包下载到本地，然后使用离线rpm 的方式安装。 

下载地址如下：

```asciiarmor
https://packages.gitlab.com/gitlab/gitlab-ce/packages/el/7/gitlab-ce-13.10.2-ce.0.el7.x86_64.rpm 
```

> 注：资料里提供了此rpm 包，直接将此包上传到服务器/opt/module 目录下即可。 

**编写安装脚本** 

安装gitlab 步骤比较繁琐，因此我们可以参考官网编写gitlab 的安装脚本。 

```
[root@gitlab-server module]# vim gitlab-install.sh 
sudo rpm -ivh /opt/module/gitlab-ce-13.10.2-ce.0.el7.x86_64.rpm 
sudo yum install -y curl policycoreutils-python openssh-server cronie 
sudo lokkit -s http -s ssh 
sudo yum install -y postfix  
sudo service postfix start  
sudo chkconfig postfix on 
curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.rpm.sh | sudo bash 
sudo EXTERNAL_URL="http://gitlab.example.com" yum -y install gitlab-ce 
```

给脚本增加执行权限 

```
[root@gitlab-server module]# chmod +x gitlab-install.sh  
[root@gitlab-server module]# ll 
总用量 403104 
-rw-r--r--. 1 root root 412774002 4月   7 15:47 gitlab-ce-13.10.2-
ce.0.el7.x86_64.rpm 
-rwxr-xr-x. 1 root root       416 4月   7 15:49 gitlab-install.sh 
```

然后执行该脚本，开始安装gitlab-ce。注意一定要保证服务器可以上网。 

```
[root@gitlab-server module]# ./gitlab-install.sh  
警告：/opt/module/gitlab-ce-13.10.2-ce.0.el7.x86_64.rpm: 头V4 
RSA/SHA1 Signature, 密钥 ID f27eab47: NOKEY 
准备中...                          ################################# 
[100%] 
正在升级/安装... 
   1:gitlab-ce-13.10.2-ce.0.el7        
################################# [100%]  
。 。 。 。 。 。 
```

**初始化GitLab 服务** 

执行以下命令初始化GitLab 服务，过程大概需要几分钟，耐心等待… 

```
[root@gitlab-server module]# gitlab-ctl reconfigure 
 
。 。 。 。 。 。 
Running handlers: 
Running handlers complete 
Chef Client finished, 425/608 resources updated in 03 minutes 08 
seconds 
gitlab Reconfigured! 
 
```

**启动GitLab 服务** 

执行以下命令启动GitLab 服务，如需停止，执行gitlab-ctl stop 

```
[root@gitlab-server module]# gitlab-ctl start 
ok: run: alertmanager: (pid 6812) 134s 
ok: run: gitaly: (pid 6740) 135s 
ok: run: gitlab-monitor: (pid 6765) 135s 
ok: run: gitlab-workhorse: (pid 6722) 136s 
ok: run: logrotate: (pid 5994) 197s 
ok: run: nginx: (pid 5930) 203s 
ok: run: node-exporter: (pid 6234) 185s 
ok: run: postgres-exporter: (pid 6834) 133s 
ok: run: postgresql: (pid 5456) 257s 
ok: run: prometheus: (pid 6777) 134s 
ok: run: redis: (pid 5327) 263s 
ok: run: redis-exporter: (pid 6391) 173s 
ok: run: sidekiq: (pid 5797) 215s 
ok: run: unicorn: (pid 5728) 221s 
```

**使用浏览器访问GitLab** 

使用主机名或者IP 地址即可访问GitLab 服务。需要提前配一下windows 的hosts 文件。 

首次登陆之前，需要修改下GitLab 提供的root 账户的密码，要求8 位以上，包含大小写子母和特殊符号。因此我们修改密码为Atguigu.123456 
然后使用修改后的密码登录GitLab。 

GitLab 登录成功。 
