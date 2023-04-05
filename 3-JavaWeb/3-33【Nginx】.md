![](..\图片\3-33【Nginx】\1.png)

# 第一章 Nginx

## 1.1 Nginx基础

Nginx 是一款高性能的 http 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。由俄罗斯的程序设计师伊戈尔·西索夫（Igor Sysoev）所开发，官方测试 nginx 能够支支撑 5 万并发链接，并且 cpu、内存等资源消耗却非常低，运行非常稳定。

**Nginx** **应用场景：**

1. http 服务器。Nginx 是一个 http 服务可以独立提供 http 服务。可以做网页静态服务器。
2. 虚拟主机。可以实现在一台服务器虚拟出多个网站。例如个人网站使用的虚拟主机。
3. 反向代理，负载均衡。当网站的访问量达到一定程度后，单台服务器不能满足用户的请求时，需要用多台服务器集群可以使用 nginx 做反向代理。并且多台服务器可以平均分担负载，不会因为某台服务器负载高宕机而某台服务器闲置的情况。 

## 1.2 Nginx在Linux下的安装

这里我有两台Centos的虚拟机，在克隆的机子上面搞的。将之前的Nginx删除了，安装了新的：

卸载：

1. 查找Nginx进程 `ps -ef | grep nginx`，如果有启动的进程（3~4行），关闭：`./nginx -s stop` 停止Nginx服务（在sbin目录使用）。
2. 查看所有与Nginx有关的文件夹 `find / -name nginx`。
3. 删除与Nginx有关的文件夹 `rm -rf file /usr/local/nginx*`。
4. 卸载Nginx相关的依赖 `yum remove nginx`。

安装：

1. 安装依赖包`yum -y install gcc pcre-devel zlib-devel openssl openssl-devel`
2. 下载Nginx安装包`wget https://nginx.org/download/nginx-1.16.1.tar.gz`(需要先`yum install wget`)
3. 解压`tar -zxvf nginx-1.16.1.tar.gz`
4. `cd nginx-1.16.1`
5. `./configure --prefix=/usr/local/nginx`  然后再  `make && make install`
6. 就可以发现在usr/local/nginx下面多了四个文件夹了。这就安装成功了

重点目录/文件:

- `conf/nginx.conf`：nginx配置文件
- `html`：存放静态文件(html、css、Js等)
- `logs`：日志目录，存放日志文件
- `sbin/nginx`：二进制文件，用于启动、停止Nginx服务

常用命令如下：

| 命令                 | 作用                                                    |
| -------------------- | ------------------------------------------------------- |
| ./nginx -v           | 查看Nginx版本（在sbin目录使用）                         |
| ./nginx -t           | 检查conf/nginx.conf文件配置是否有错误（在sbin目录使用） |
| ./nginx              | 启动Nginx（在sbin目录使用）                             |
| ./nginx -s stop      | 停止Nginx服务（在sbin目录使用）                         |
| ps -ef \| grep nginx | 查看Nginx进程                                           |
| ./nginx -s reload    | 重启服务 重新加载配置文件（在sbin目录使用）             |

如果已经启动服务，我们想要访问那么只需要在物理机浏览器上面输入虚拟机的IP地址即可，因为Nginx的端口号默认是80。防火墙的话我们已经关闭了，浏览器访问的是/nginx/html/index.html页面。

## 1.3 配置环境变量

配置变量：可以通过修改profile文件配置环境变量，在`/`目录下可以直接使用nginx命令

```bash
vim /etc/profile

# 修改为下面的内容
PATH=/usr/local/nginx/sbin:$JAVA_HOME/bin:$PATH

# 修改之后记得要使配置文件生效
source /etc/profile
```

这样以后我们再使用Nginx的时候不用去/nginx/sbin目录了，在任何位置使用命令都可以操作了。

- 重启Nginx：`nginx -s reload`
- 停止Nginx：`nginx -s stop`
- 启动Nginx：`nginx`

## 1.4 Nginx配置文件结构

Nginx配置文件(conf/nginx.conf)整体分为三部分:

- 全局块
  和Nginx运行相关的全局配置

- events块
  和网络连接相关的配置

- http块

  代理、缓存、日志记录、虚拟主机配置

  - http全局块
  - Server块
    - Server全局块
    - location块

**注意**:http块中可以配置多个Server块，每个Server块中可以配置多个location块。

![](..\图片\3-33【Nginx】\1-1.png)

## 1.5 Nginx静态网站部署

Nginx可以作为静态web服务器来部署静态资源。静态资源指在服务端真实存在并且能够直接展示的一些文件，比如常见的html页面、css文件、js文件、图片、视频等资源。

相对于Tomcat，Nginx处理静态资源的能力更加高效，所以在生产环境下，一般都会将静态资源部署到Nginx中。

将静态资源部署到Nginx非常简单，只需要将文件复制到Nginx安装目录下的html目录中即可。

```bash
server {
  listen 80;                #监听端口
  server_name localhost;    #服务器名称
  location/{                #匹配客户端请求url
    root html;              #指定静态资源根目录
    index index.html;       #指定默认首页
}
```

## 1.6 反向代理

**正向代理**

是一个位于客户端和原始服务器(origin server)之间的服务器，为了从原始服务器取得内容，客户端向代理发送一个请求并指定目标(原始服务器)，然后代理向原始服务器转交请求并将获得的内容返回给客户端。

正向代理的典型用途是为在防火墙内的局域网客户端提供访问Internet的途径。

正向代理一般是在客户端设置代理服务器，通过代理服务器转发请求，最终访问到目标服务器。

![](..\图片\3-33【Nginx】\1-2.png)

**什么是反向代理**

反向代理服务器位于用户与目标服务器之间，但是对于用户而言，反向代理服务器就相当于目标服务器，即用户直接访问反向代理服务器就可以获得目标服务器的资源，反向代理服务器负责将请求转发给目标服务器。

用户不需要知道目标服务器的地址，也无须在用户端作任何设定。

![](..\图片\3-33【Nginx】\1-3.png)

**配置反向代理**

这里我们就不操作了。

1. 在克隆的上面传送hello工程并启动。打开浏览器：`http://192.168.66.136:8080/hello`，相应没有问题

2. 配置代理服务器

   ```bash
   server {
     listen 82;
     server_name localhost;
   
     location / {
             proxy_pass http://192.168.66.136:8080; #反向代理配置
     } 
   }
   ```

3. 浏览器输入：`http://192.168.66.130:82/hello`，然后就会代理给另一台服务器 没有任何问题

## 1.7 负载均衡

早期的网站流量和业务功能都比较简单，单台服务器就可以满足基本需求，但是随着互联网的发展，业务流量越来越大并且业务逻辑也越来越复杂，单台服务器的性能及单点故障问题就凸显出来了，因此需要多台服务器组成应用集群，进行性能的水平扩展以及避免单点故障出现。

- 应用集群：将同一应用部署到多台机器上，组成应用集群，接收负载均衡器分发的请求，进行业务处理并返回响应数据
- 负载均衡器：将用户请求根据对应的负载均衡算法分发到应用集群中的一台服务器进行处理

![](..\图片\3-33【Nginx】\1-4.png)

**配置负载均衡**:

修改ngnix.conf

```bash
upstream targetserver{    #upstream指令可以定义一组服务器
  server 192.168.188.101:8080;    # server 192.168.188.101:8080 weight=2; 使用权重方式
  server 192.168.188.101:8081;
}

server {
  listen  8080;
  server_name     localhost;
  location / {
          proxy_pass http://targetserver;
  }
}
```

**负载均衡策略**

| 名称       | 说明             |
| ---------- | ---------------- |
| 轮询       | 默认方式         |
| weight     | 权重方式         |
| IP_hash    | 根据IP分配方式   |
| least_conn | 依据最少连接方式 |
| url_hash   | 依据url连接方式  |
| fair       | 依据相应时间方式 |

## 1.5 配置虚拟主机

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
