# 第一章 初识Docker

## 1.1 什么是Docker

微服务虽然具备各种各样的优势，但服务的拆分通用给部署带来了很大的麻烦。

- 分布式系统中，依赖的组件非常多，不同组件之间部署时往往会产生一些冲突。
- 在数百上千台服务中重复部署，环境不一定一致，会遇到各种问题

大型项目组件较多，运行环境也较为复杂，部署时会碰到一些问题：

- 依赖关系复杂，容易出现兼容性问题

- 开发、测试、生产环境有差异

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731141907366.png" alt="image-20210731141907366" style="zoom:25%;" />

例如一个项目中，部署时需要依赖于node.js、Redis、RabbitMQ、MySQL等，这些服务部署时所需要的函数库、依赖项各不相同，甚至会有冲突。给部署带来了极大的困难。

### Docker解决依赖兼容问题

而Docker确巧妙的解决了这些问题，Docker是如何实现的呢？

Docker为了解决依赖的兼容问题的，采用了两个手段：

- 将应用的Libs（函数库）、Deps（依赖）、配置与应用一起打包

- 将每个应用放到一个隔离**容器**去运行，避免互相干扰

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731142219735.png" alt="image-20210731142219735" style="zoom:25%;" />

这样打包好的应用包中，既包含应用本身，也保护应用所需要的Libs、Deps，无需再操作系统上安装这些，自然就不存在不同应用之间的兼容问题了。

虽然解决了不同应用的兼容问题，但是开发、测试等环境会存在差异，操作系统版本也会有差异，怎么解决这些问题呢？

### Docker解决操作系统环境差异

要解决不同操作系统环境差异问题，必须先了解操作系统结构。以一个Ubuntu操作系统为例，结构如下：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731143401460.png" alt="image-20210731143401460" style="zoom:25%;" />

结构包括：

- 计算机硬件：例如CPU、内存、磁盘等
- 系统内核：所有Linux发行版的内核都是Linux，例如CentOS、Ubuntu、Fedora等。内核可以与计算机硬件交互，对外提供**内核指令**，用于操作计算机硬件。
- 系统应用：操作系统本身提供的应用、函数库。这些函数库是对内核指令的封装，使用更加方便。

应用于计算机交互的流程如下：

1. 应用调用操作系统应用（函数库），实现各种功能
2. 系统函数库是对内核指令集的封装，会调用内核指令
3. 内核指令操作计算机硬件

Ubuntu和CentOSpringBoot都是基于Linux内核，无非是系统应用不同，提供的函数库有差异：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731144304990.png" alt="image-20210731144304990" style="zoom:25%;" />

此时，如果将一个Ubuntu版本的MySQL应用安装到CentOS系统，MySQL在调用Ubuntu函数库时，会发现找不到或者不匹配，就会报错了：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731144458680.png" alt="image-20210731144458680" style="zoom:25%;" />

Docker如何解决不同系统环境的问题？

- Docker将用户程序与所需要调用的系统(比如Ubuntu)函数库一起打包
- Docker运行到不同操作系统时，直接基于打包的函数库，借助于操作系统的Linux内核来运行

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731144820638.png" alt="image-20210731144820638" style="zoom:25%;" />



Docker如何解决大型项目依赖关系复杂，不同组件依赖的兼容性问题？

- Docker允许开发中将应用、依赖、函数库、配置一起**打包**，形成可移植镜像
- Docker应用运行在容器中，使用沙箱机制，相互**隔离**

Docker如何解决开发、测试、生产环境有差异的问题？

- Docker镜像中包含完整运行环境，包括系统函数库，仅依赖系统的Linux内核，因此可以在任意Linux操作系统上运行

Docker是一个快速交付应用、运行应用的技术，具备下列优势：

- 可以将程序及其依赖、运行环境一起打包为一个镜像，可以迁移到任意Linux操作系统
- 运行时利用沙箱机制形成隔离容器，各个应用互不干扰
- 启动、移除都可以通过一行命令完成，方便快捷

## 1.2 Docker和虚拟机的区别

Docker可以让一个应用在任何操作系统中非常方便的运行。而以前我们接触的虚拟机，也能在一个操作系统中，运行另外一个操作系统，保护系统中的任何应用。

两者有什么差异呢？

**虚拟机**（virtual machine）是在操作系统中**模拟**硬件设备，然后运行另一个操作系统，比如在 Windows 系统里面运行 Ubuntu 系统，这样就可以运行任意的Ubuntu应用了。

**Docker**仅仅是封装函数库，并没有模拟完整的操作系统，如图：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731145914960.png" alt="image-20210731145914960" style="zoom:25%;" />

对比来看：

| 特性     | Docker   | 虚拟机   |
| -------- | -------- | -------- |
| 性能     | 接近原生 | 性能较差 |
| 硬盘占用 | 一般为MB | 一般为GB |
| 启动     | 秒级     | 分钟级   |

Docker和虚拟机的差异：

- docker是一个系统进程；虚拟机是在操作系统中的操作系统

- docker体积小、启动速度快、性能好；虚拟机体积大、启动速度慢、性能一般

## 1.3.Docker架构

镜像：将应用程序及其依赖、环境、配置打包在一起

容器：镜像运行起来就是容器，一个镜像可以运行多个容器

Docker结构：

- 服务端：接收命令或远程请求，操作镜像或容器

- 客户端：发送命令或者请求到Docker服务端

DockerHub：一个镜像托管的服务器，类似的还有阿里云镜像服务，统称为DockerRegistry

### 镜像和容器

Docker中有几个重要的概念：

- **镜像（Image）**：Docker将应用程序及其所需的依赖、函数库、环境、配置等文件打包在一起，称为镜像。
- **容器（Container）**：镜像中的应用程序运行后形成的进程就是**容器**，只是Docker会给容器进程做隔离，对外不可见。

一切应用最终都是代码组成，都是硬盘中的一个个的字节形成的**文件**。只有运行时，才会加载到内存，形成进程。

而**镜像**，就是把一个应用在硬盘上的文件、及其运行环境、部分系统函数库文件一起打包形成的文件包。这个文件包是只读的。

**容器**呢，就是将这些文件中编写的程序、函数加载到内存中允许，形成进程，只不过要隔离起来。因此一个镜像可以启动多次，形成多个容器进程。

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731153059464.png" alt="image-20210731153059464" style="zoom:25%;" />

例如你下载了一个QQ，如果我们将QQ在磁盘上的运行**文件**及其运行的操作系统依赖打包，形成QQ镜像。然后你可以启动多次，双开、甚至三开QQ，跟多个妹子聊天。

### DockerHub

开源应用程序非常多，打包这些应用往往是重复的劳动。为了避免这些重复劳动，人们就会将自己打包的应用镜像，例如Redis、MySQL镜像放到网络上，共享使用，就像GitHub的代码共享一样。

- DockerHub：DockerHub是一个官方的Docker镜像的托管平台。这样的平台称为Docker Registry。

- 国内也有类似于DockerHub 的公开服务，比如 [网易云镜像服务](https://c.163yun.com/hub)、[阿里云镜像库](https://cr.console.aliyun.com/)等。

我们一方面可以将自己的镜像共享到DockerHub，另一方面也可以从DockerHub拉取镜像：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731153743354.png" alt="image-20210731153743354" style="zoom:25%;" />

### Docker架构

我们要使用Docker来操作镜像、容器，就必须要安装Docker。

Docker是一个CS架构的程序，由两部分组成：

- 服务端(server)：Docker守护进程，负责处理Docker指令，管理镜像、容器等

- 客户端(client)：通过命令或RestAPI向Docker服务端发送指令。可以在本地或远程向服务端发送指令。

如图：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731154257653.png" alt="image-20210731154257653" style="zoom:25%;" />

## 1.4.安装Docker

企业部署一般都是采用Linux操作系统，而其中又数CentOS发行版占比最多，因此我们在CentOS下安装Docker。

Docker 分为 CE 和 EE 两大版本。CE 即社区版（免费，支持周期 7 个月），EE 即企业版，强调安全，付费使用，支持周期 24 个月。Docker CE 分为 `stable` `test` 和 `nightly` 三个更新频道。

官方网站上有各种环境下的 [安装指南](https://docs.docker.com/install/)，这里主要介绍 Docker CE 在 CentOS上的安装。

Docker CE 支持 64 位版本 CentOS 7，并且要求内核版本不低于 3.10， CentOS 7 满足最低内核的要求，所以我们在CentOS 7安装Docker。这里我们在Centos7_clone安装。

如果之前安装过旧版本的Docker，可以使用下面命令卸载：

```
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-selinux \
                  docker-engine-selinux \
                  docker-engine \
                  docker-ce
```

首先需要大家虚拟机联网，安装yum工具

```sh
yum install -y yum-utils \
           device-mapper-persistent-data \
           lvm2 --skip-broken
```

然后更新本地镜像源：

```shell
# 设置docker镜像源
yum-config-manager \
    --add-repo \
    https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
    
sed -i 's/download.docker.com/mirrors.aliyun.com\/docker-ce/g' /etc/yum.repos.d/docker-ce.repo

yum makecache fast
```

然后输入命令：

```shell
yum install -y docker-ce
```

docker-ce为社区免费版本。稍等片刻，docker即可安装成功。安装成功之后关闭防火墙，Docker应用需要用到各种端口，逐一去修改防火墙设置非常麻烦。我们的Centos7的防火墙一直是关闭的。基本命令如下：

| 命令                        | 作用               |
| --------------------------- | ------------------ |
| systemctl stop firewalld    | 关闭防火墙         |
| systemctl disable firewalld | 禁止开机启动防火墙 |
| systemctl start docker      | 启动docker服务     |
| systemctl stop docker       | 停止docker服务     |
| systemctl restart docker    | 重启docker服务     |
| docker -v                   | 查看docker版本     |

docker官方镜像仓库网速较差，我们需要设置国内镜像服务。参考阿里云的镜像加速文档：https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors，内容如下，将其复制到shell窗口即可：

```sh
sudo mkdir -p /etc/docker

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://zfz45z36.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload
sudo systemctl restart docker
```

# 第二章 Docker的基本操作

## 2.1 镜像操作

首先来看下镜像的名称组成：

- 镜像名称一般分两部分组成：[repository]:[tag]。
- 在没有指定tag时，默认是latest，代表最新版本的镜像

例如：mysql:5.7。这里的mysql就是repository，5.7就是tag，合一起就是镜像名称，代表5.7版本的MySQL镜像。

![image-20210731155649535](D:\Java\笔记\图片\4-3【Docker】/image-20210731155649535.png)

常见的镜像操作命令：

| 命令                   | 作用                     |
| ---------------------- | ------------------------ |
| docker build           | 构建镜像                 |
| docker images          | 查看镜像                 |
| docker rmi [镜像名称]  | 删除镜像                 |
| docker push            | 推送镜像到服务           |
| docker pull [镜像名称] | 从服务拉取镜像           |
| docker save            | 保存镜像为压缩包         |
| docker load            | 加载压缩包为镜像         |
| docker --help          | 帮助文档查看所有命令     |
| docker images --help   | 帮助文档查看镜像命令详情 |

例如：从DockerHub中拉取一个nginx镜像并查看，利用docker save将nginx镜像导出磁盘，然后再通过load加载回来。Docker Hub官方地址：https://hub.docker.com/。

| 命令                                           | 作用                            |
| ---------------------------------------------- | ------------------------------- |
| docker pull nginx                              | 拉取ngixn镜像                   |
| docker images                                  | 查看拉取到的镜像                |
| docker save --help                             | 查看save命令用法                |
| docker save -o [保存的目标文件名称] [镜像名称] | 保存镜像为压缩包到本地磁盘      |
| docker save -o nginx.tar nginx:latest          | 保存nginx镜像为压缩包到本地磁盘 |
| docker rmi nginx:latest                        | 删除本地的nginx镜像             |
| docker load --help                             | 查看load命令用法                |
| docker load -i nginx.tar                       | 加载本地文件                    |

## 2.2 容器操作

容器操作的命令如图：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731161950495.png" alt="image-20210731161950495" style="zoom: 33%;" />

容器保护三个状态：

- 运行：进程正常运行
- 暂停：进程暂停，CPU不再运行，并不释放内存
- 停止：进程终止，回收进程占用的内存、CPU等资源

常用命令如下：

| 命令                          | 作用                             |
| ----------------------------- | -------------------------------- |
| docker run                    | 创建并运行一个容器，处于运行状态 |
| docker pause                  | 让一个运行的容器暂停             |
| docker unpause                | 让一个容器从暂停状态恢复运行     |
| docker stop [容器名称]        | 停止一个运行的容器               |
| docker start [容器名称]       | 让一个停止的容器再次运行         |
| docker rm [参数] [容器名称]   | 删除一个容器，运行状态无法删除   |
| docker exec                   | 进入容器执行命令                 |
| exit                          | 退出容器                         |
| docker logs [参数] [容器名称] | 查看指定容器运行日志             |
| docker ps [参数]              | 查看所有运行的容器及状态         |

### 创建并运行一个容器

创建并运行nginx容器的命令：

```sh
docker run --name containerName -p 80:80 -d nginx
```

命令解读：

- docker run ：创建并运行一个容器
- --name : 给容器起一个名字，比如叫做mn
- -p ：将宿主机端口与容器端口映射，冒号左侧是宿主机端口，右侧是容器端口
- -d：后台运行容器
- nginx：镜像名称，例如nginx

这里的`-p`参数，是将容器端口映射到宿主机端口。

默认情况下，容器是隔离环境，我们直接访问宿主机的80端口，肯定访问不到容器中的nginx。现在，将容器的80与宿主机的80关联起来，当我们访问宿主机的80端口时，就会被映射到容器的80，这样就能访问到nginx了：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731163255863.png" alt="image-20210731163255863" style="zoom: 33%;" />

### 进入容器，修改文件

**需求**：进入Nginx容器，修改HTML文件内容，添加“林炫你好”

1）进入容器。进入我们刚刚创建的nginx容器的命令为：

```sh
docker exec -it mn bash
```

命令解读：

- docker exec ：进入容器内部，执行一个命令

- -it : 给当前进入的容器创建一个标准输入、输出终端，允许我们与容器交互

- mn ：要进入的容器的名称

- bash：进入容器后执行的命令，bash是一个linux终端交互命令

2）进入nginx的HTML所在目录 /usr/share/nginx/html

容器内部会模拟一个独立的Linux文件系统，看起来如同一个linux服务器一样。nginx的环境、配置、运行文件全部都在这个文件系统中，包括我们要修改的html文件。注意是阉割版的linux系统，连`ll`命令都没有。

查看DockerHub网站中的nginx页面，可以知道nginx的html目录位置在`/usr/share/nginx/html`

我们执行命令，进入该目录：

```sh
cd /usr/share/nginx/html
```

3）修改index.html的内容

容器内没有vi命令，无法直接修改，我们用下面的命令来修改：

```sh
sed -i -e 's#Welcome to nginx#林炫你好#g' -e 's#<head>#<head><meta charset="utf-8">#g' index.html
```

在浏览器访问自己的虚拟机地址，例如我的是：http://192.168.66.136/，即可看到结果。

4）其他命令：

| 命令                    | 作用                     |
| ----------------------- | ------------------------ |
| exit                    | 退出docker容器           |
| docker stop [容器名称]  | 停止一个运行的容器       |
| docker ps               | 查看所有运行的容器       |
| docker ps -a            | 查看所有容器             |
| docker start [容器名称] | 让一个停止的容器开始运行 |
| docker rm [容器名称]    | 删除停止运行的容器       |
| docker rm -f [容器名称] | 强制删除容器             |

再来一个操作，创建并运行一个redis容器，进入容器，添加一个键值对

| 命令                                                         | 作用                                  |
| ------------------------------------------------------------ | ------------------------------------- |
| docker run --name mr -p 6379:6379 -d redis redis-server --save 60 1 --loglevel warning | 创建并运行redis容器<br />容器名称为mr |
| docker ps -a                                                 | 查看所有容器                          |
| docker ps                                                    | 查看所有运行容器                      |
| docker exec -it mr bash                                      | 进入mr容器                            |
| redis-cli                                                    | 运行redis-cli                         |
| keys * （这是redis操作）                                     | 查看所有键值对                        |
| set key 666 （这是redis操作）                                | 添加键值对                            |
| get key （这是redis操作）                                    | 获的键为key的值                       |
| .\redis-cli.exe -h 192.168.66.136 -p 6379 （进入E:\redis\redis-2.8.9>） | 连接Linuxz中redis                     |

## 2.3 数据卷（容器数据管理）

在之前的nginx案例中，修改nginx的html页面时，需要进入nginx内部。并且因为没有编辑器，修改文件也很麻烦。

这就是因为容器与数据（容器内文件）耦合带来的后果。

![image-20210731172440275](D:\Java\笔记\图片\4-3【Docker】/image-20210731172440275.png)

要解决这个问题，必须将数据与容器解耦，这就要用到数据卷了。

**数据卷（volume）**是一个虚拟目录，指向宿主机文件系统中的某个目录。

![image-20210731173541846](D:\Java\笔记\图片\4-3【Docker】/image-20210731173541846.png)

一旦完成数据卷挂载，对容器的一切操作都会作用在数据卷对应的宿主机目录了。

这样，我们操作宿主机的/var/lib/docker/volumes/html目录，就等于操作容器内的/usr/share/nginx/html目录了

### 数据卷操作语法

数据卷操作的基本语法如下：

```sh
docker volume [COMMAND]
```

docker volume命令是数据卷操作，根据命令后跟随的command来确定下一步的操作：

- create 创建一个volume
- ls 列出所有的volume
- inspect 显示一个或多个volume的信息
- rm 删除一个或多个指定的volume
- prune 删除未使用的volume

| 命令                               | 作用                   |
| ---------------------------------- | ---------------------- |
| docker volume create [数据卷名称]  | 创建指定名称的数据卷   |
| docker volume ls                   | 查看所有数据卷         |
| docker volume inspect [数据卷名称] | 查看数据卷详细信息     |
| docker volume rm [数据卷名称]      | 删除指定数据卷         |
| docker volume prune                | 删除所有未使用的数据卷 |

我们在创建容器时，可以通过 -v 参数来挂载一个数据卷到某个容器内目录，命令格式如下：

```sh
docker run \
  --name mn \
  -v html:/root/html \
  -p 8080:80
  nginx \
```

这里的-v就是挂载数据卷的命令：

- `-v html:/root/htm` ：把html数据卷挂载到容器内的/root/html这个目录中。如果html数据卷不存在，那么先创建html数据卷再关在到容器内的/root/html这个目录。

### 给nginx挂载数据卷

**需求**：创建一个nginx容器，修改容器内的html目录内的index.html内容

**分析**：上个案例中，我们进入nginx容器内部，已经知道nginx的html目录所在位置/usr/share/nginx/html ，我们需要把这个目录挂载到html这个数据卷上，方便操作其中的内容。

**提示**：运行容器时使用 -v 参数挂载数据卷

步骤：

1. 创建容器并挂载数据卷到容器内的HTML目录

   ```sh
   docker run --name mn -v html:/usr/share/nginx/html -p 80:80 -d nginx
   ```

2. 进入html数据卷所在位置

   ```sh
   # 查看html数据卷的位置
   docker volume inspect html
   
   # 进入该目录
   cd /var/lib/docker/volumes/html/_data
   
   # 修改文件
   vi index.html
   ```

3. 修改HTML内容

   ```html
   <!DOCTYPE html>
   <html>
   <head>
   <meta charset = "UTF-8">
   <title>Welcome to nginx!</title>
   <style>
   html { color-scheme: light dark; }
   body { width: 35em; margin: 0 auto;
   font-family: Tahoma, Verdana, Arial, sans-serif; }
   </style>
   </head>
   <body>
   <h1>你好林炫</h1>
   <p>If you see this page, the nginx web server is successfully installed and
   working. Further configuration is required.</p>
   
   <p>For online documentation and support please refer to
   <a href="http://nginx.org/">nginx.org</a>.<br/>
   Commercial support is available at
   <a href="http://nginx.com/">nginx.com</a>.</p>
   
   <p><em>Thank you for using nginx.</em></p>
   </body>
   </html>
   ```

   浏览器输入：http://192.168.66.136/，就会有页面了。

## 2.4 容器挂载目录

容器不仅仅可以挂载数据卷，也可以直接挂载到宿主机目录上。关联关系如下：

- 带数据卷模式：宿主机目录 --> 数据卷 ---> 容器内目录
- 直接挂载模式：宿主机目录 ---> 容器内目录

如图：

![image-20210731175155453](D:\Java\笔记\图片\4-3【Docker】/image-20210731175155453.png)

**语法**：

目录挂载与数据卷挂载的语法是类似的：

- -v [宿主机目录]:[容器内目录]
- -v [宿主机文件]:[容器内文件]

数据卷挂载与目录直接挂载的

- 数据卷挂载耦合度低，由docker来管理目录，但是目录较深，不好找
- 目录挂载耦合度高，需要我们自己管理目录，不过目录容易寻找查看

docker run的命令中通过 -v 参数挂载文件或目录到容器中：

- -v volume名称:容器内目录
- -v 宿主机文件:容器内文
- -v 宿主机目录:容器内目录

# 第三章 Dockerfile自定义镜像

常见的镜像在DockerHub就能找到，但是我们自己写的项目就必须自己构建镜像了。

而要自定义镜像，就必须先了解镜像的结构才行。

## 3.1 镜像结构

镜像是将应用程序及其需要的系统函数库、环境、配置、依赖打包而成。

我们以MySQL为例，来看看镜像的组成结构：

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731175806273.png" alt="image-20210731175806273" style="zoom: 25%;" />

简单来说，镜像就是在系统函数库、运行环境基础上，添加应用程序文件、配置文件、依赖文件等组合，然后编写好启动脚本打包在一起形成的文件。

我们要构建镜像，其实就是实现上述打包的过程。

## 3.2 Dockerfile语法

构建自定义的镜像时，并不需要一个个文件去拷贝，打包。

我们只需要告诉Docker，我们的镜像的组成，需要哪些BaseImage、需要拷贝什么文件、需要安装什么依赖、启动脚本是什么，将来Docker会帮助我们构建镜像。

而描述上述信息的文件就是Dockerfile文件。

**Dockerfile**就是一个文本文件，其中包含一个个的**指令(Instruction)**，用指令来说明要执行什么操作来构建镜像。每一个指令都会形成一层Layer。

![image-20210731180321133](D:\Java\笔记\图片\4-3【Docker】/image-20210731180321133.png)

更新详细语法说明，请参考官网文档： https://docs.docker.com/engine/reference/builder

## 3.3 构建Java项目

### 基于Ubuntu构建Java项目

需求：基于Ubuntu镜像构建一个新镜像，运行一个java项目。

1. 新建一个空文件夹docker-demo。我们是在/usr/local/docker下面创建的。

2. 拷贝课前资料中的docker-demo.jar文件到docker-demo这个目录

3. 拷贝课前资料中的jdk8.tar.gz文件到docker-demo这个目录

4. 拷贝课前资料提供的Dockerfile到docker-demo这个目录

     其中的内容如下：

   ```sh
   # 指定基础镜像
   FROM ubuntu:16.04
   # 配置环境变量，JDK的安装目录
   ENV JAVA_DIR=/usr/local
   
   # 拷贝jdk和java项目的包
   COPY ./jdk8.tar.gz $JAVA_DIR/
   COPY ./docker-demo.jar /tmp/app.jar
   
   # 安装JDK
   RUN cd $JAVA_DIR \
    && tar -xf ./jdk8.tar.gz \
    && mv ./jdk1.8.0_144 ./java8
   
   # 配置环境变量
   ENV JAVA_HOME=$JAVA_DIR/java8
   ENV PATH=$PATH:$JAVA_HOME/bin
   
   # 暴露端口
   EXPOSE 8090
   # 入口，java项目的启动命令
   ENTRYPOINT java -jar /tmp/app.jar
   ```

5. 进入docker-demo，运行命令构建镜像：

   ```sh
   docker build -t javaweb:1.0 .
   ```

6. 基于镜像创建容器，并运行

   ```sh
   docker run --name web -p 8090:8090 -d javaweb:1.0
   ```

最后访问http://192.168.66.136:8090/hello/count，发现成功访问了，没有任何问题。

### 基于java8构建Java项目

虽然我们可以基于Ubuntu基础镜像，添加任意自己需要的安装包，构建镜像，但是却比较麻烦。所以大多数情况下，我们都可以在一些安装了部分软件的基础镜像上做改造。例如，构建java项目的镜像，可以在已经准备了JDK的基础镜像基础上构建。

需求：基于java:8-alpine镜像，将一个Java项目构建为镜像

实现思路如下：

1. 新建一个空的目录，然后在目录中新建一个文件，命名为Dockerfile

2. 拷贝课前资料提供的docker-demo.jar到这个目录中

3. 编写Dockerfile文件：

   ```dockerfile
   # 基于java:8-alpine作为基础镜像
   FROM java:8-alpine
   # 将app.jar拷贝到镜像中
   COPY ./docker-demo.jar /tmp/app.jar
   # 暴露端口
   EXPOSE 8090
   # 编写入口ENTRYPOINT
   ENTRYPOINT java -jar /tmp/app.jar
   ```

4. 使用docker build命令构建镜像

5. 使用docker run创建容器并运行

## 3.4 小结

1. Dockerfile的本质是一个文件，通过指令描述镜像的构建过程

2. Dockerfile的第一行必须是FROM，从一个基础镜像来构建

3. 基础镜像可以是基本操作系统，如Ubuntu。也可以是其他人制作好的镜像，例如：java:8-alpine

# 第四章 Docker-Compose

Docker Compose可以基于Compose文件帮我们快速的部署分布式应用，而无需手动一个个创建和运行容器！

<img src="D:\Java\笔记\图片\4-3【Docker】/image-20210731180921742.png" alt="image-20210731180921742" style="zoom: 33%;" />

Compose文件是一个文本文件，通过指令定义集群中的每个容器如何运行。格式如下：

```json
version: "3.8"
 services:
  mysql:
    image: mysql:5.7.25
    environment:
     MYSQL_ROOT_PASSWORD: 123 
    volumes:
     - "/tmp/mysql/data:/var/lib/mysql"
     - "/tmp/mysql/conf/hmy.cnf:/etc/mysql/conf.d/hmy.cnf"
  web:
    build: .
    ports:
     - "8090:8090"
```

上面的Compose文件就描述一个项目，其中包含两个容器：

- mysql：一个基于`mysql:5.7.25`镜像构建的容器，并且挂载了两个目录
- web：一个基于`docker build`临时构建的镜像容器，映射端口时8090

DockerCompose的详细语法参考官网：https://docs.docker.com/compose/compose-file/。也可以使用`docker-compose --help`来查看语法。

其实DockerCompose文件可以看做是将多个docker run命令写到一个文件，只是语法稍有差异。

## 4.1 安装DockerCompose

> 尝试无法下载，直接使用yum命令下载了。

Linux下需要通过命令下载：

```sh
# 安装
curl -L https://github.com/docker/compose/releases/download/1.23.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

如果下载速度较慢，或者下载失败，可以使用课前资料提供的docker-compose文件，上传到`/usr/local/bin/`目录也可以。

修改文件权限：

```sh
# 修改权限
chmod +x /usr/local/bin/docker-compose
```

Base自动补全命令：

```sh
# 补全命令
curl -L https://raw.githubusercontent.com/docker/compose/1.29.1/contrib/completion/bash/docker-compose > /etc/bash_completion.d/docker-compose
```

如果这里出现错误，需要修改自己的hosts文件：

```sh
echo "199.232.68.133 raw.githubusercontent.com" >> /etc/hosts
```

## 4.2 部署微服务集群

**需求**：将之前学习的cloud-demo微服务集群利用DockerCompose部署

**实现思路**：

1. 查看课前资料提供的cloud-demo文件夹，里面已经编写好了docker-compose文件

   查看课前资料提供的cloud-demo文件夹，里面已经编写好了docker-compose文件，而且每个微服务都准备了一个独立的目录

   ```yml
   version: "3.2"
   
   services:
     nacos: # 作为注册中心和配置中心
       image: nacos/nacos-server #  基于nacos/nacos-server镜像构建
       environment: # 环境变量
         MODE: standalone # 单点模式启动
       ports: # 端口映射，这里暴露了8848端口
         - "8848:8848"
     mysql:
       image: mysql:5.7.25 # 镜像版本是mysql:5.7.25
       environment:
         MYSQL_ROOT_PASSWORD: 123 # 设置数据库root账户的密码为123
       volumes: # 数据卷挂载，这里挂载了mysql的data、conf目录，其中有提前准备好的数据
         - "$PWD/mysql/data:/var/lib/mysql"
         - "$PWD/mysql/conf:/etc/mysql/conf.d/"
     # userservice orderservice gateway都是基于Dockerfile临时构建的
     userservice:
       build: ./user-service
     orderservice:
       build: ./order-service
     gateway:
       build: ./gateway
       ports:
         - "10010:10010"
   ```

2. 修改自己的cloud-demo项目，将数据库、nacos地址都命名为docker-compose中的服务名

   因为微服务将来要部署为docker容器，而容器之间互联不是通过IP地址，而是通过容器名。这里我们将order-service、user-service、gateway服务的mysql、nacos地址都修改为基于容器名的访问。

   ```yml
   spring:
     datasource:
       url: jdbc:mysql://mysql:3306/cloud_order?useSSL=false
       username: root
       password: 123
       driver-class-name: com.mysql.jdbc.Driver
     application:
       name: orderservice
     cloud:
       nacos:
         server-addr: nacos:8848 # nacos服务地址
   ```

3. 使用maven打包工具，将项目中的每个微服务都打包为app.jar

   接下来需要将我们的每个微服务都打包。因为之前查看到Dockerfile中的jar包名称都是app.jar，因此我们的每个微服务都需要用这个名称。

   可以通过修改pom.xml中的打包名称来实现，每个微服务都需要修改：

   ```xml
   <build>
     <!-- 服务打包的最终名称 -->
     <finalName>app</finalName>
     <plugins>
       <plugin>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-maven-plugin</artifactId>
       </plugin>
     </plugins>
   </build>
   ```

4. 将打包好的app.jar拷贝到cloud-demo中的每一个对应的子目录中

   编译打包好的app.jar文件，需要放到Dockerfile的同级目录中。注意：每个微服务的app.jar放到与服务名称对应的目录，别搞错了。

5. 将cloud-demo上传至虚拟机，利用 docker-compose up -d 来部署

   最后，我们需要将文件整个cloud-demo文件夹上传到虚拟机中，理由DockerCompose部署。

   进入cloud-demo目录，然后运行下面的命令：

   ```sh
   docker-compose up -d
   ```

6. 动态查看日志，发现报错，这是因为Nacos启动有点慢。所以userservice和orderservice以及gateway找不到nacos，无法注册。因此需要稍等一会，将userservice和orderservice以及gateway服务重新启动。然后再查看日志，如果不报错就可以了，报错的话，还是因为nacos没有启动完毕的原因，电脑不太行。

   ```sh
   # 动态查看日志
   docker-compose logs -f
   # 重启gateway orderservice userservice微服务
   docker-compose restart gateway orderservice userservice
   ```

访问http://192.168.66.136:10010/user/1?authorization=admin，显示`{"id":1,"username":"柳岩","address":"湖南省衡阳市"}`，代表没问题了。

关闭服务：`docker-compose stop`.

# 第五章 Docker镜像仓库 

## 5.1 搭建私有镜像仓库

搭建镜像仓库可以基于Docker官方提供的DockerRegistry来实现。官网地址：https://hub.docker.com/_/registry

搭建镜像仓库这里有两种方式：

1. **简化版镜像仓库**

   Docker官方的Docker Registry是一个基础版本的Docker镜像仓库，具备仓库管理的完整功能，但是没有图形化界面。搭建方式比较简单，命令如下：

   ```sh
   docker run -d \
       --restart=always \
       --name registry	\
       -p 5000:5000 \
       -v registry-data:/var/lib/registry \
       registry
   ```

   命令中挂载了一个数据卷registry-data到容器内的/var/lib/registry 目录，这是私有镜像库存放数据的目录。

   访问http://YourIp:5000/v2/_catalog 可以查看当前私有镜像服务中包含的镜像

2. **带有图形化界面版本**

   使用DockerCompose部署带有图象界面的DockerRegistry，命令如下：

   ```yaml
   version: '3.0'
   services:
     registry:
       image: registry
       volumes:
         - ./registry-data:/var/lib/registry
     ui:
       image: joxit/docker-registry-ui:static
       ports:
         - 8080:80
       environment:
         - REGISTRY_TITLE=林炫私有仓库
         - REGISTRY_URL=http://registry:5000
       depends_on:
         - registry
   ```

   但是我们的私服采用的是http协议，默认不被Docker信任，所以首先需要做一个配置。

   ```sh
   # 打开要修改的文件
   vim /etc/docker/daemon.json
   # 添加内容：
   "insecure-registries":["http://192.168.150.101:8080"]
   # 重加载
   systemctl daemon-reload
   # 重启docker
   systemctl restart docker
   ```


我们这里采用的是第二种图形化配置方式，步骤如下：

1. 使用编辑器打开需要修改的文件`vim /etc/docker/daemon.json`，修改为：

   ```json
   {
     "registry-mirrors": ["https://zfz45z36.mirror.aliyuncs.com"],
     "insecure-registries":["http://192.168.66.136:8080"]
   }
   ```

   也就是添加了一个`,`和"insecure-registries`":["http://192.168.66.136:8080"]`，加上自己的IP地址

2. 重新加载并重启

   ```sh
   # 重加载
   systemctl daemon-reload
   # 重启docker
   systemctl restart docker
   ```

3. 进入/usr/local/docker，创建registry-ui文件夹，在文件夹下面创建docker-compose.yml文件，文件内输入内容：

   ```sh
   # 进入/usr/local/docker
   cd /usr/local/docker
   # 创建registry-ui文件夹
   mkdir registry-ui
   # 在文件夹下面创建docker-compose.yml文件
   touch docker-compose.yml
   # 使用vim打开文件
   vim docker-compose.yml
   ```

   ```yml
   version: '3.0'
   services:
     registry:
       image: registry
       volumes:
         - ./registry-data:/var/lib/registry
     ui:
       image: joxit/docker-registry-ui:static
       ports:
         - 8080:80
       environment:
         - REGISTRY_TITLE=林炫私有仓库
         - REGISTRY_URL=http://registry:5000
       depends_on:
         - registry
   ```

4. 执行

   ```sh
   # 执行
   docker-compose up -d
   # 动态查看日志
   docker-compose logs -f
   ```

访问http://192.168.66.136:8080/，如果没有问题，那么就代表成功了。

## 5.2 推送、拉取镜像

推送镜像到私有镜像服务必须先tag，步骤如下：

1. 重新tag本地镜像，名称前缀为私有仓库的地址：192.168.150.101:8080/

   ```sh
   docker tag nginx:latest 192.168.66.136:8080/nginx:1.0 
   ```

2. 推送镜像

   ```sh
   docker push 192.168.66.136:8080/nginx:1.0 
   ```

3. 拉取镜像

   ```sh
   docker pull 192.168.150.101:8080/nginx:1.0 
   ```

