# 第一章 大数据介绍

数据是指对客观事件进行记录并可以鉴别的符号，是对客观事物的性质、状态以及相互关系等进行记载的物理符号或这些物理符号的组合，它是可识别的、抽象的符号。

它不仅指狭义上的数字，还可以是具有一定意义的文字、字母、数字符号的组合、图形、图像、视频、音频等，也是客观事物的属性、数量、位置及其相互关系的抽象表示。例如， "0、1、2..."、“阴、雨、下降”、“学生的档案记录、货物的运输情况”等都是数据。

## 1.1 企业数据分析方向

企业数据分析方向：把隐藏在数据背后的信息集中和提炼出来，总结出所研究对象的内在规律，帮助管理者进行有效的判断和决策。

数据分析在企业日常经营分析中主要有三大方向：

* 原因分析(分析过去的数据)：某一现状为什么发生，确定原因，做出调整优化;
* 现状分析(分析当下的数据)：现阶段的整体情况，各个部分的构成占比、发展、变动;
* 预测分析(结合数据预测未来)：结合已有数据预测未来发展趋势

这三个方向也对应着大数据中的三个名词：

* 离线分析（Batch Processing）：面向过去，面向历史，分析已经有的数据；在时间纬度明显成批次行变化。一周一分析（T+ 7），一天一分析（T + 1），所以也叫做批处理。
* 实时分析（Real Time Processing | Streaming）：面向当下，分析实时产生的数据；所谓实时是指从数据产生到数据分析到数据应用的时间间隔很短，可细分未秒级、毫秒级。也叫做流式处理。
* 机器学习（Machine Learning）：基于历史数据和当下产生的实时数据预测未来发生的事情；侧重于数学算法的运用，如分类、聚类、关联、预测。

## 1.2 数据分析步骤

数据分析步骤的重要体现在：对如何开展数据分析提供了强有力的逻辑支撑；

张文霖在《数据分析六部曲》中说，典型的数据分析应该包含一下几个步骤：

* 明确分析目的和思路
* 数据收集
* 数据处理
* 数据分析
* 数据展现
* 报告撰写

**明确分析目的和思路**

- 目的是整个分析流程的起点，为数据的收集、处理及分析提供清晰的指引方向；

- 思路是使分析框架体系化，比如先分析什么，后分析什么，使各分析点之间具有逻辑练习，保证分析纬度的完整性，分析结果的有效性以及正确性，需要数据分析方法论进行支撑。

- 数据分析方法论是一些营销管理类相关理论，比如用户行为理论、PEST分析法、5W2H分析法等。


**数据收集**

- 数据从无到有的过程：比如传感器收集气象数据、埋点收集用户行为数据
- 数据传输搬运的过程：比如采集数据库数据到数据分析平台

![](..\图片\5-13【Hadoop】\1-1.png)

**数据处理**

- 准确来说，应该称之为数据预处理。
- 数据预处理需要对收集到的数据进行加工整理，形成适合数据分析的样式，主要包括数据清洗、数据转化、数据提取、数据计算;
- 数据预处理可以保证数据的一致性和有效性，让数据变成干净规整的结构化数据。

**数据分析**

- 用适当的分析方法及分析工具，对处理过的数据进行分析，提取有价值的信息，形成有效结论的过程;
- 需要掌握各种数据分析方法，还要熟悉数据分析软件的操作;

**数据展现**

- 数据展现又称之为数据可视化，指的是分析结果图表展示，因为人类是视觉动物;
- 数据可视化(Data Visualization)属于数据应用的一种;
- 注意，数据分析的结果不是只有可视化展示，还可以继续数据挖掘(Data Mining)、即席查询(Ad Hoc)等。

**报告撰写**

- 数据分析报告是对整个数据分析过程的一个总结与呈现
- 把数据分析的起因、过程、结果及建议完整地呈现出来，供决策者参考
- 需要有明确的结论，最好有建议或解决方案

## 1.3 大数据时代

“ ” : ， ( )	

最早提出“大数据”时代到来的是全球知名咨询公司麦肯锡，其称:“数据，已经渗透到当今每一个行业和业务职能领域，成为重要的生产因素。人们对于海量数据的挖掘和运用，预示着新一波生产率增长和消费者盈余浪潮的到来。”2019年，央视推出了国内首部大数据产业题材纪录片《大数据时代》，节目细致而生动地讲述了大数据技术在政府治理、民生服务、数据安全、工业转型、未来生活等方面给我们带来的改变和影响。

大数据(big data)是指无法在一定时间范围内用常规软件工具进行捕捉、管理和处理的数据集合;是需要新处理模式才能具有更强的决策力、洞察发现力和流程优化能力的海量、高增长率和多样化的信息资产。

大数据的特征用5个V来描述：

* 数据体量大（Volume）：采集数据量大、存储数据量大、计算数据量大、TB PB级别起步。
* 种类、来源多样化（Variety）：种类分为结构化、半结构化、非结构化。来源有图片、音频和视频等。
* 低价值密度（Value）：信息海量但是价值密度低，深度复杂的挖掘分析需要集器学习参与。
* 速度快（Velocity）：数据增长速度快、获取数据速度快、数据处理速度快。
* 数据的质量（Veracity）：数据的准确性，数据的可信赖度。

# 第二章 Hadoop基础

狭义上Hadoop指的是Apache软件基金会的一款开源软件。用java语言实现，开源，允许用户使用简单的编程模型实现跨机器集群对海量数据进行分布式计算处理。官网 http://hadoop.apache.org/。

Hadoop核心组件：

- Hadoop HDFS（分布式文件存储系统）：解决海量数据存储。HDFS作为分布式文件存储系统，处在生态圈的底层与核心地位；
- Hadoop YARN（集群资源管理和任务调度框架）：解决资源任务调度。YARN作为分布式通用的集群资源管理系统和任务调度平台，支撑各种计算引擎运行，保证了Hadoop地位；
- Hadoop MapReduce（分布式计算框架）：解决海量数据计算。MapReduce作为大数据生态圈第一代分布式计算引擎，由于自身设计的模型所产生的弊端，导致企业一线几乎不再直接使用MapReduce进行编程处理，但是很多软件的底层依然在使用MapReduce引擎来处理数据。

广义上Hadoop指的是围绕Hadoop打造的大数据生态圈。

![](..\图片\5-13【Hadoop】\1-2.jpg)

**特性优点**

- 可靠性（reliability）：能自动维护数据的多份复制，并且在任务失败后能自动地重新部署（redeploy）计算任务。所以Hadoop的按位存储和处理数据的能力值得人们信赖。
- 效率高（efficiency）：通过并发数据，Hadoop可以在节点之间动态并行的移动数据，使得速度非常快。
- 成本低（Economical）：Hadoop集群允许通过部署普通廉价的机器组成集群来处理大数据，以至于成本很低。看重的是集群整体能力。
- 扩容能力（scalability）：Hadoop是在可用的计算机集群间分配数据并完成计算任务的，这些集群可方便灵活的方式扩展到数以千计的节点。

**Hadoop发行版本**

一共有两个版本：开源社区版和商业发行版。

* 开源社区版：Apache开源社区发行、也是官方发行版本。Apache开源社区版本http://hadoop.apache.org/。优点是更新迭代快，缺点是兼容稳定性不周。我们使用的是Apache版的Hadoop，版本号为：3.3.0
* 商业发行版：商业公司发行、基于Apache开源协议、某些服务需要收费。商业发行版本，Cloudera：https://www.cloudera.com/products/open-source/apache-hadoop.html。Hortonworks ：https://www.cloudera.com/products/hdp.html。优点是稳定兼容好，缺点是收费、版本更新慢。

**Hadoop架构变迁**

Hadoop 1.0

- HDFS（分布式文件存储）
- MapReduce（资源管理和分布式数据处理）

Hadoop 2.0

- HDFS（分布式文件存储）
- MapReduce（分布式数据处理）
- YARN（集群资源管理、任务调度）

Hadoop 3.0：Hadoop 3.0架构组件和Hadoop 2.0类似，3.0着重于性能优化。

- 通用方面：精简内核、类路径隔离、shell脚本重构
- Hadoop HDFS：EC纠删码、多NameNode支持
- Hadoop MapReduce：任务本地化优化、内存参数自动推断
- Hadoop YARN：Timeline Service V2、队列配置

## 2.1 Hadoop集群搭建

Hadoop集群包括两个集群：HDFS集群、YARN集群。两个集群逻辑上分离，通常物理上在一起，互相之间没有依赖、互不影响。两个集群都是标准的主从架构集群。MapReduce是计算框架、代码层面的组件，没有集群之说。

- HDFS集群 (分布式存储)：主角色NameNode、从角色DataNode、主角色辅助角色SecondaryNameNode。
- YARN集群 (资源管理、调度)：主角色ResourceManager、从角色NodeManager。

![](..\图片\5-13【Hadoop】\2-1.png)

安装包、源码包下载地址：https://archive.apache.org/dist/hadoop/common/hadoop-3.3.0/。非生产环境下面我们直接下载安装包即可，不需要下载源码进行编译。课程提供编译好的Hadoop安装包：hadoop-3.3.0-Centos7-64-with-snappy.tar.gz

但是我们也要了解一下，下载下来源码包后需要重新编译Hadoop源码的原因：这是为了匹配不同操作系统本地库环境，Hadoop某些操作比如压缩、IO需要调用系统本地库（*`.so`|*`.dll`）。源码包根目录下文件有一个BUILDING.txt，根据这个操作就行了。详细步骤参考附件资料。

Hadoop是在Linux上面安装的，一共提供了三台虚拟机让我们搭建集群：node1、node2、node3。想要在这三台虚拟机上面搭建Hadoop集群，那么我们首先需要做一些准备工作。这些准备工作每台机器都要做。

| 主机  | 角色                 |
| ----- | -------------------- |
| node1 | NN    DN   RM  NM    |
| node2 | SNN  DN           NM |
| node3 | DN          NM       |

1. 保证每台机器都有一个独一无二的主机名称，查看`/etc/hostname`文件

   ```sh
   [root@node1 ~]# cat /etc/hostname
   node1.itcast.cn
   ```

2. ip地址和主机名称弄一个映射关系，并且起一个简短的名字

   ```sh
   # hosts映射
   vim /etc/hosts
   
   127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
   ::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
   
   192.168.88.151 node1.itcast.cn node1
   192.168.88.152 node2.itcast.cn node2
   192.168.88.153 node3.itcast.cn node3
   ```

3. 关闭当前防火墙

   ```sh
   # 关闭防火墙
   systemctl stop firewalld.service
   # 禁止开机自启防火墙 永久关闭
   systemctl disable firewalld.service
   # 查看防火墙状态
   systemctl status firewalld.service
   ```

4. ssh免密登录

   ```sh
   # ssh免密登录（只需要配置node1至node1、node2、node3即可）
   
   	#node1生成公钥私钥 (一路回车)
   	ssh-keygen  
   	
   	#node1配置免密登录到node1 node2 node3
   	ssh-copy-id node1
   	ssh-copy-id node2
   	ssh-copy-id node3
   ```

5. 集群时间同步

   ```sh
   # 集群时间同步
   ntpdate ntp5.aliyun.com
   ```

6. 创建统一工作目录

   ```sh
   # /export/目录下面创建这三个文件夹 用于存放 数据 软件 安装包
   [root@node1 ~]# ls /export/
   data  server  software
   ```

7. 安装JDK

   ```sh
   # JDK 1.8安装  上传 jdk-8u241-linux-x64.tar.gz到/export/server/目录下 
   # 这里我下载得是jdk1.8.0_144
   cd /export/server/
   tar zxvf jdk-8u241-linux-x64.tar.gz
   
   	#配置环境变量
   	vim /etc/profile
   	
   	export JAVA_HOME=/export/server/jdk1.8.0_144
   	export PATH=$PATH:$JAVA_HOME/bin
   	export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
   	
   	#重新加载环境变量文件
   	source /etc/profile
   ```

   我们可以重复上面的步骤给node2节点和node3节点弄一遍，当然也可以远程复制：

   ```sh
   # 将node1节点的/export/server/jdk1.8.0_144/文件拷贝至node2节点/export/server/目录下面
   [root@node1 ~]# scp -r /export/server/jdk1.8.0_144/ root@node2:/export/server/
   
   # 将node1节点的/etc/profile文件拷贝至node2节点/etc/目录下面
   [root@node1 ~]# scp /etc/profile root@node2:/etc/
   
   # 加载/etc/profile文件
   [root@node2 ~]# source /etc/profile
   ```

   然后再复制给node3节点！

8. 上传Hadoop安装包到node1节点的`/export/server`，并解压。Hadoop安装包目录结构如下：

   | 目录    | 说明                                                         |
   | ------- | ------------------------------------------------------------ |
   | bin     | Hadoop最基本的管理脚本和使用脚本的目录，这些脚本是sbin目录下管理脚本的基础实现，用户可以直接使用这些脚本管理和使用Hadoop。 |
   | etc     | Hadoop配置文件所在的目录                                     |
   | include | 对外提供的编程库头文件（具体动态库和静态库在lib目录中），这些头文件均是用C++定义的，通常用于C++程序访问HDFS或者编写MapReduce 程序。 |
   | lib     | 该目录包含了Hadoop对外提供的编程动态库和静态库，与include目录中的头文件结合使用。 |
   | libexec | 各个服务对用的shell配置文件所在的目录，可用于配置日志输出、启动参数（比如JVM参数）等基本信息。 |
   | sbin    | Hadoop管理脚本所在的目录，主要包含HDFS和YARN中各类服务的启动/关闭脚本。 |
   | share   | Hadoop各个模块编译后的jar包所在的目录，官方自带示例。        |

9. 修改配置文件(配置文件路径 `hadoop-3.3.0/etc/hadoop`)，官网文档：https://hadoop.apache.org/docs/r3.3.0/。

   - 第一类1个：hadoop-env.sh，这是一个shell文件，配置的是Java路径、一些运行的进程和角色用户名称。

     ```shell
     #文件最后添加
     export JAVA_HOME=/export/server/jdk1.8.0_144
     
     export HDFS_NAMENODE_USER=root
     export HDFS_DATANODE_USER=root
     export HDFS_SECONDARYNAMENODE_USER=root
     export YARN_RESOURCEMANAGER_USER=root
     export YARN_NODEMANAGER_USER=root 
     ```

   - 第二类4个：`xxxx-site.xml` ，site表示的是用户定义的配置，会覆盖default中的默认配置。

     `core-site.xml`：核心模块配置

     ```xml
     <!-- 设置默认使用的文件系统 Hadoop支持file、HDFS、GFS、ali|Amazon云等文件系统 -->
     <property>
         <name>fs.defaultFS</name>
         <value>hdfs://node1:8020</value>
     </property>
     
     <!-- 设置Hadoop本地保存数据路径 -->
     <property>
         <name>hadoop.tmp.dir</name>
         <value>/export/data/hadoop-3.3.0</value>
     </property>
     
     <!-- 设置HDFS web UI用户身份 -->
     <property>
         <name>hadoop.http.staticuser.user</name>
         <value>root</value>
     </property>
     
     <!-- 整合hive 用户代理设置 -->
     <property>
         <name>hadoop.proxyuser.root.hosts</name>
         <value>*</value>
     </property>
     
     <property>
         <name>hadoop.proxyuser.root.groups</name>
         <value>*</value>
     </property>
     
     <!-- 文件系统垃圾桶保存时间 -->
     <property>
         <name>fs.trash.interval</name>
         <value>1440</value>
     </property>
     ```

     `hdfs-site.xml`：hdfs文件系统模块配置

     ```xml
     <!-- 设置SNN进程运行机器位置信息 -->
     <property>
         <name>dfs.namenode.secondary.http-address</name>
         <value>node2:9868</value>
     </property>
     ```

     `mapred-site.xml`：MapReduce模块配置

     ```xml
     <!-- 设置MR程序默认运行模式： yarn集群模式 local本地模式 -->
     <property>
       <name>mapreduce.framework.name</name>
       <value>yarn</value>
     </property>
     
     <!-- MR程序历史服务地址 -->
     <property>
       <name>mapreduce.jobhistory.address</name>
       <value>node1:10020</value>
     </property>
      
     <!-- MR程序历史服务器web端地址 -->
     <property>
       <name>mapreduce.jobhistory.webapp.address</name>
       <value>node1:19888</value>
     </property>
     
     <property>
       <name>yarn.app.mapreduce.am.env</name>
       <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
     </property>
     
     <property>
       <name>mapreduce.map.env</name>
       <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
     </property>
     
     <property>
       <name>mapreduce.reduce.env</name>
       <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
     </property>
     ```

     `yarn-site.xml`：yarn模块配置

     ```xml
     <!-- 设置YARN集群主角色运行机器位置 -->
     <property>
     	<name>yarn.resourcemanager.hostname</name>
     	<value>node1</value>
     </property>
     
     <property>
         <name>yarn.nodemanager.aux-services</name>
         <value>mapreduce_shuffle</value>
     </property>
     
     <!-- 是否将对容器实施物理内存限制 -->
     <property>
         <name>yarn.nodemanager.pmem-check-enabled</name>
         <value>false</value>
     </property>
     
     <!-- 是否将对容器实施虚拟内存限制。 -->
     <property>
         <name>yarn.nodemanager.vmem-check-enabled</name>
         <value>false</value>
     </property>
     
     <!-- 开启日志聚集 -->
     <property>
       <name>yarn.log-aggregation-enable</name>
       <value>true</value>
     </property>
     
     <!-- 设置yarn历史服务器地址 -->
     <property>
         <name>yarn.log.server.url</name>
         <value>http://node1:19888/jobhistory/logs</value>
     </property>
     
     <!-- 历史日志保存的时间 7天 -->
     <property>
       <name>yarn.log-aggregation.retain-seconds</name>
       <value>604800</value>
     </property>
     ```

   - 第三类1个：workers。记录从角色运行在何处

     ```
     node1.itcast.cn
     node2.itcast.cn
     node3.itcast.cn
     ```

10. 分发同步hadoop安装包

    ```sh
    cd /export/server
    
    scp -r hadoop-3.3.0 root@node2:$PWD
    scp -r hadoop-3.3.0 root@node3:$PWD
    ```

11. 将hadoop添加到环境变量（3台机器）

    ```shell
    vim /etc/profile
    
    export HADOOP_HOME=/export/server/hadoop-3.3.0
    export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
    
    source /etc/profile
    
    #别忘了scp给其他两台机器
    scp /etc/profile root@node2:/etc/
    ```


## 2.2 Hadoop集群启动与关闭

**首次启动**

（==首次启动==）格式化namenode。首次启动HDFS时，必须对其进行格式化操作。format本质上是初始化工作，进行HDFS清理和准备工作

```shell
hdfs namenode -format

# 显示下面的代表成功
2022-11-07 18:28:41，354 INFO common.Storage: Storage directory /export/data/hadoop-3.3.0/dfs/name has been successfully formatted.
```

**手动逐个进程启停**

每台机器上每次手动启动关闭一个角色进程，可以精准控制每个进程启停，避免群起群停。

HDFS集群

```sh
#hadoop2.x版本命令
hadoop-daemon.sh start|stop namenode|datanode|secondarynamenode
#hadoop3.x版本命令
hdfs --daemon start|stop namenode|datanode|secondarynamenode
```

YARN集群

```sh
#hadoop2.x版本命令
yarn-daemon.sh start|stop resourcemanager|nodemanager
#hadoop3.x版本命令
yarn --daemon start|stop resourcemanager|nodemanager
```

**shell脚本一键启停**

在node1上，使用软件自带的shell脚本一键启动。前提：配置好机器之间的SSH免密登录和workers文件。

- HDFS集群：`start-dfs.sh` 、`stop-dfs.sh` 
- YARN集群：`start-yarn.sh`、`stop-yarn.sh`
- Hadoop集群：`start-all.sh`、`stop-all.sh` 

```shell
[root@node1 ~]# start-dfs.sh 
Starting namenodes on [node1]
Last login: Thu Nov  5 10:44:10 CST 2020 on pts/0
Starting datanodes
Last login: Thu Nov  5 10:45:02 CST 2020 on pts/0
Starting secondary namenodes [node2]
Last login: Thu Nov  5 10:45:04 CST 2020 on pts/0

[root@node1 ~]# start-yarn.sh 
Starting resourcemanager
Last login: Thu Nov  5 10:45:08 CST 2020 on pts/0
Starting nodemanagers
Last login: Thu Nov  5 10:45:44 CST 2020 on pts/0
```

**进程状态、日志查看**

启动完毕之后可以使用`jps`命令查看进程是否启动成功

Hadoop启动日志路径：`/export/server/hadoop-3.3.0/logs/`

**Web  UI页面**

- HDFS集群：http://namenode_host:9870 其中namenode_host是namenode运行所在机器的主机名或者ip 如果使用主机名访问，别忘了在Windows配置hosts。我们的就是http://192.168.88.151:9870/
- YARN集群：http://resourcemanager_host:8088 其中resourcemanager_host是resourcemanager运行所在机器的主机名或者ip 如果使用主机名访问，别忘了在Windows配置hosts。http://192.168.88.151:8088/

## 2.3 Hadoop初体验

**HDFS 初体验**

shell命令操作：

```sh
[root@node1 ~]# hadoop fs -mkdir /linxuan
[root@node1 ~]# hadoop fs -put a.txt /linxuan
[root@node1 ~]# hadoop fs -ls /
Found 1 items
drwxr-xr-x   - root supergroup          0 2022-11-07 19:02 /linxuan
[root@node1 ~]# 
```

Web UI页面操作

![](..\图片\5-13【Hadoop】\2-2.png)

**MapReduce+YARN 初体验**

执行Hadoop官方自带的MapReduce案例，评估圆周率π的值。

```sh
[root@node1 ~]# cd /export/server/hadoop-3.3.0/share/hadoop/mapreduce/
[root@node1 mapreduce]# ll
total 5276
-rw-r--r-- 1 root root  589704 Jul 15  2021 hadoop-mapreduce-client-app-3.3.0.jar
-rw-r--r-- 1 root root  803842 Jul 15  2021 hadoop-mapreduce-client-common-3.3.0.jar
-rw-r--r-- 1 root root 1623803 Jul 15  2021 hadoop-mapreduce-client-core-3.3.0.jar
-rw-r--r-- 1 root root  181995 Jul 15  2021 hadoop-mapreduce-client-hs-3.3.0.jar
-rw-r--r-- 1 root root   10323 Jul 15  2021 hadoop-mapreduce-client-hs-plugins-3.3.0.jar
-rw-r--r-- 1 root root   50701 Jul 15  2021 hadoop-mapreduce-client-jobclient-3.3.0.jar
-rw-r--r-- 1 root root 1651503 Jul 15  2021 hadoop-mapreduce-client-jobclient-3.3.0-tests.jar
-rw-r--r-- 1 root root   91017 Jul 15  2021 hadoop-mapreduce-client-nativetask-3.3.0.jar
-rw-r--r-- 1 root root   62310 Jul 15  2021 hadoop-mapreduce-client-shuffle-3.3.0.jar
-rw-r--r-- 1 root root   22637 Jul 15  2021 hadoop-mapreduce-client-uploader-3.3.0.jar
-rw-r--r-- 1 root root  281197 Jul 15  2021 hadoop-mapreduce-examples-3.3.0.jar
drwxr-xr-x 2 root root    4096 Jul 15  2021 jdiff
drwxr-xr-x 2 root root      30 Jul 15  2021 lib-examples
drwxr-xr-x 2 root root    4096 Jul 15  2021 sources
[root@node1 mapreduce]# hadoop jar hadoop-mapreduce-examples-3.3.0.jar pi 2 4
Number of Maps  = 2
Samples per Map = 4
Wrote input for Map #0
Wrote input for Map #1
Starting Job
2022-11-07 19:08:01，000 INFO client.DefaultNoHARMFailoverProxyProvider: Connecting to ResourceManager at node1/192.168.88.151:8032
2022-11-07 19:08:02，981 INFO mapreduce.JobResourceUploader: Disabling Erasure Coding for path: /tmp/hadoop-yarn/staging/root/.staging/job_1667818042197_0001
2022-11-07 19:08:03，565 INFO input.FileInputFormat: Total input files to process : 2
2022-11-07 19:08:03，782 INFO mapreduce.JobSubmitter: number of splits:2
2022-11-07 19:08:04，820 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1667818042197_0001
2022-11-07 19:08:04，820 INFO mapreduce.JobSubmitter: Executing with tokens: []
2022-11-07 19:08:05，509 INFO conf.Configuration: resource-types.xml not found
2022-11-07 19:08:05，510 INFO resource.ResourceUtils: Unable to find 'resource-types.xml'.
2022-11-07 19:08:06，591 INFO impl.YarnClientImpl: Submitted application application_1667818042197_0001
2022-11-07 19:08:06，919 INFO mapreduce.Job: The url to track the job: http://node1:8088/proxy/application_1667818042197_0001/
2022-11-07 19:08:06，920 INFO mapreduce.Job: Running job: job_1667818042197_0001
2022-11-07 19:08:46，009 INFO mapreduce.Job: Job job_1667818042197_0001 running in uber mode : false
2022-11-07 19:08:46，049 INFO mapreduce.Job:  map 0% reduce 0%
2022-11-07 19:09:31，234 INFO mapreduce.Job:  map 100% reduce 0%
2022-11-07 19:09:49，625 INFO mapreduce.Job:  map 100% reduce 100%
2022-11-07 19:09:51，701 INFO mapreduce.Job: Job job_1667818042197_0001 completed successfully
2022-11-07 19:09:52，151 INFO mapreduce.Job: Counters: 54
# ......
```

![](..\图片\5-13【Hadoop】\2-3.png)

# 第三章 HDFS分布式文件系统

**文件系统**

文件系统是一种存储和组织数据的方法，实现了数据的存储、分级组织、访问和获取等操作，使得用户对文件访问和查找变得容易。

文件系统使用树形目录的抽象逻辑概念代替了硬盘等物理设备使用数据块的概念，用户不必关心数据底层存在硬盘哪里，只需要记住这个文件的所属目录和文件名即可；文件系统通常使用硬盘和光盘这样的存储设备，并维护文件在设备中的物理位置。

传统常见的文件系统指的的单机的文件系统，也就是底层不会横跨多台机器实现。比如windows操作系统上的文件系统、Linux上的文件系统、FTP文件系统等等。

这些文件系统的共同特征包括：

1. 带有抽象的目录树结构，树都是从/根目录开始往下蔓延；
2. 树中节点分为两类：目录和文件；
3. 从根目录开始，节点路径具有唯一性。

**数据**：指存储的内容本身，比如文件、视频、图片等，这些数据底层最终是存储在磁盘等存储介质上的，一般用户无需关心， 只需要基于目录树进行增删改查即可，实际针对数据的操作由文件系统完成。

**元数据**：元数据（metadata）又称之为解释性数据，记录数据的数据；

文件系统元数据一般指文件大小、最后修改时间、底层存储位置、属性、所属用户、权限等信息。

## 3.1 分布式文件系统

大数据时代，面对海量数据，传统的文件存储系统会面临很多挑战：

* 成本高：传统存储硬件通用性差，设备投资加上后期维护、升级扩容的成本非常高。
* 如何支撑高效率的计算分析：传统存储方式意味着数据：存储是存储，计算是计算，当需要处理数据的时候把数据移动过来。程序和数据存储是属于不同的技术厂商实现，无法有机统一整合在一起。
* 性能低：单节点I/O性能瓶颈无法逾越，难以支撑海量数据的高并发高吞吐场景。
* 可扩展性差：无法实现快速部署和弹性扩展，动态扩容、缩容成本高，技术实现难度大。

因此，分布式存储应运而生。分布式存储系统核心属性：

* 分布式存储：无限扩展支撑海量数据存储。
* 元数据记录：快速定位文件位置便于查找。
* 分块存储：针对块并行操作提高效率。
* 副本机制：冗余存储保障数据安全。

**分布式存储的优点**

我们在单机存储遇到得问题是数据量大，单机存储遇到瓶颈。

那么分布式对此的解决方案如下： 

- 单机纵向扩展：磁盘不够加磁盘，有上限瓶颈限制 
- 多机横向扩展：机器不够加机器，理论上无限扩展

![](..\图片\5-13【Hadoop】\3-0.png)

**元数据记录的功能**

我们在单机存储遇到得问题是文件分布在不同机器上不利于寻找。

那么分布式对此的解决方案如下： 元数据记录下文件及其存储位置信息，快速定位文件位置

![](..\图片\5-13【Hadoop】\3-1.png)

**分块存储好处**

单机系统的问题是：文件过大导致单机存不下、上传下载效率低。

那么分布式对此的解决方案如下： 文件分块存储在不同机器，针对块并行操作提高效率。

![](..\图片\5-13【Hadoop】\3-2.png)

**副本机制的作用**

单机系统的问题是：硬件故障难以避免，数据易丢失。

那么分布式对此的解决方案如下： 不同机器设置备份，冗余存储，保障数据安全。

![](..\图片\5-13【Hadoop】\3-3.png)

## 3.2 HDFS介绍

HDFS（Hadoop Distributed File System ），意为：Hadoop分布式文件系统。是Apache Hadoop核心组件之一，作为大数据生态圈最底层的分布式存储服务而存在。也可以说大数据首先要解决的问题就是海量数据的存储问题。

![](..\图片\5-13【Hadoop】\1-2.jpg)

HDFS主要是解决大数据如何存储问题的。分布式意味着是HDFS是横跨在多台计算机上的存储系统。

HDFS是一种能够在普通硬件上运行的分布式文件系统，它是高度容错的，适应于具有大数据集的应用程序，它非
常适于存储大型数据 (比如 TB 和 PB)。

HDFS使用多台计算机存储文件， 并且提供统一的访问接口， 像是访问一个普通文件系统一样使用分布式文件系统。

![](..\图片\5-13【Hadoop】\3-4.png)

Doug Cutting领导Nutch项目研发，Nutch的设计目标是构建一个大型的全网搜索引擎，包括网页抓取、索引、 查询等功能。随着爬虫抓取网页数量的增加，遇到了严重的可扩展性问题——如何解决数十亿网页的存储和索引问题。2003年的时候， Google 发表的论文为该问题提供了可行的解决方案。 《分布式文件系统（GFS），可用于处理海量网页的存储》。Nutch的开发人员完成了相应的开源实现HDFS，并从Nutch中剥离和MapReduce成为独立项目HADOOP。

HDFS设计目标：

- 硬件故障（Hardware Failure）是常态， HDFS可能有成百上千的服务器组成，每一个组件都有可能出现故障。因此故障检测和自动快速恢复是HDFS的核心架构目标。
- HDFS上的应用主要是以流式读取数据（Streaming Data Access）。HDFS被设计成用于批处理，而不是用户交互式的。相较于数据访问的反应时间，更注重数据访问的高吞吐量。
- 典型的HDFS文件大小是GB到TB的级别。所以，HDFS被调整成支持大文件（Large Data Sets）。它应该提供很高的聚合数据带宽，一个集群中支持数百个节点，一个集群中还应该支持千万级别的文件。
- 大部分HDFS应用对文件要求的是write-one-read-many访问模型。一个文件一旦创建、写入、关闭之后就不需
  要修改了。这一假设简化了数据一致性问题，使高吞吐量的数据访问成为可能。
- 移动计算的代价比之移动数据的代价低。一个应用请求的计算，离它操作的数据越近就越高效。将计算移动到数据附近，比之将数据移动到应用所在显然更好。
- HDFS被设计为可从一个平台轻松移植到另一个平台。这有助于将HDFS广泛用作大量应用程序的首选平台。

HDFS设计场景：

- 大文件
- 数据流式访问
- 一次写入多次读取
- 低成本部署，廉价PC
- 高容错

## 3.3 HDFS重要特性

<!--Rack 机架-->

HDFS重要特性如下：主从架构、分块存储、副本机制、元数据记录、抽象统一的目录树结构（namespace）。

**主从架构**

HDFS集群是标准的master/slave主从架构集群。一般一个HDFS集群是有一个Namenode和一定数目的Datanode组成。

Namenode是HDFS主节点，Datanode是HDFS从节点，两种角色各司其职，共同协调完成分布式的文件存储服
务。官方架构图中是一主五从模式，其中五个从角色位于两个机架（Rack）的不同服务器上。

![](..\图片\5-13【Hadoop】\3-5.png)

**分块存储**

HDFS中的文件在物理上是分块存储（block）的，默认大小是128M（134217728），不足128M则本身就是一块。块的大小可以通过配置参数来规定，参数位于`hdfs-default.xml`中：`dfs.blocksize`。

![](..\图片\5-13【Hadoop】\3-6.png)

**副本机制**

文件的所有block都会有副本。副本系数可以在文件创建的时候指定，也可以在之后通过命令改变。副本数由参数`dfs.replication`控制，默认值是3，也就是会额外再复制2份，连同本身总共3份副本。

![](..\图片\5-13【Hadoop】\3-7.png)

**元数据记录**

在HDFS中，Namenode管理的元数据具有两种类型：

* 文件自身属性信息：文件名称、权限，修改时间，文件大小，复制因子，数据块大小。
* 文件块位置映射信息：记录文件块和DataNode之间的映射信息，即哪个块位于哪个节点上。

![](..\图片\5-13【Hadoop】\3-8.png)

**抽象统一的目录树结构（namespace）**

HDFS支持传统的层次型文件组织结构。用户可以创建目录，然后将文件保存在这些目录里。文件系统名字空间的
层次结构和大多数现有的文件系统类似：用户可以创建、删除、移动或重命名文件。

Namenode负责维护文件系统的namespace名称空间，任何对文件系统名称空间或属性的修改都将被Namenode
记录下来。

HDFS会给客户端提供一个统一的抽象目录树，客户端通过路径来访问文件，形如：hdfs://namenode:port/dira/dir-b/dir-c/file.data

![](..\图片\5-13【Hadoop】\3-9.png)

**数据块存储**

文件的各个block的具体存储管理由DataNode节点承担。

每一个block都可以在多个DataNode上存储。

![](..\图片\5-13【Hadoop】\3-10.png)

## 3.4 HDFS shell操作

命令行界面（英语：command-line interface，缩写：CLI），是指用户通过键盘输入指令，计算机接收到指令后，予以执行一种人际交互方式。Hadoop提供了文件系统的shell命令行客户端: `hadoop fs [generic options]`。

HDFS Shell CLI支持操作多种文件系统，包括本地文件系统（file:///）、分布式文件系统（hdfs://nn:8020）等。具体操作的是什么文件系统取决于命令中文件路径URL中的前缀协议。如果没有指定前缀，则将会读取环境变量中的`fs.defaultFS`属性，以该属性值作为默认文件系统。

```sh
hadoop fs -ls file:/// #操作本地文件系统
hadoop fs -ls hdfs://node1:8020/ #操作HDFS分布式文件系统
hadoop fs -ls / #直接根目录，没有指定协议 将加载读取fs.defaultFS值
```

目前版本来看，官方最终推荐使用的是hadoop fs。当然hdfs dfs在市面上的使用也比较多。

- hadoop dfs 只能操作HDFS文件系统（包括与Local FS间的操作），不过已经Deprecated；
- hdfs dfs 只能操作HDFS文件系统相关（包括与Local FS间的操作），常用；
- hadoop fs 可操作任意文件系统，不仅仅是hdfs文件系统，使用范围更广；

HDFS文件系统的操作命令很多和Linux类似，因此学习成本相对较低。可以通过`hadoop fs -help`命令来查看每个命令的详细用法。[命令官方指导文档](https://hadoop.apache.org/docs/r3.3.0/hadoop-project-dist/hadoop-common/FileSystemShell.html)

下面是常用命令：

* **创建文件夹**
  `hadoop fs -mkdir [-p]  ...` ：path 为待创建的目录。`-p`选项的行为与Unix中`mkdir -p`非常相似，它会沿着路径创建父目录。
* **查看指定目录下内容**
  `hadoop fs -ls [-h] [-R] [<path> ...]` ：path 指定目录路径。`-h` 人性化显示文件size，-R 递归查看指定目录及其子目录。
* **上传文件到HDFS指定目录下**
  `hadoop fs -put [-f] [-p] <localsrc> ... <dst>`：`-f` 覆盖目标文件（已存在下），`-p` 保留访问和修改时间，所有权和权限。localsrc 本地文件系统（客户端所在机器），dst 目标文件系统（HDFS）。
* **查看HDFS文件内容**
  `hadoop fs -cat  ...`：读取指定文件全部内容，显示在标准输出控制台。注意：对于大文件内容读取，慎重。
* **下载HDFS文件**
  `hadoop fs -get [-f] [-p] <src> ... <localdst>`：下载文件到本地文件系统指定目录，localdst必须是目录。`-f` 覆盖目标文件（已存在下），`-p` 保留访问和修改时间，所有权和权限。
* **拷贝HDFS文件**
  `hadoop fs -cp [-f] <src> ... <dst>` ：`-f` 覆盖目标文件（已存在下）。
* **追加数据到HDFS文件中**
  `hadoop fs -appendToFile <localsrc> ... <dst>`：将所有给定本地文件的内容追加到给定dst文件。dst如果文件不存在，将创建该文件。如果<localSrc>为-，则输入为从标准输入中读取。
* **HDFS数据移动操作**
  `hadoop fs -mv <src> ... <dst>`：移动文件到指定文件夹下。可以使用该命令移动数据，重命名文件的名称。

## 3.5 HDFS集群角色

![](..\图片\5-13【Hadoop】\3-5.png)

* **主角色：namenode**
  NameNode是Hadoop分布式文件系统的核心，架构中的主角色。NameNode维护和管理文件系统元数据，包括名称空间目录树结构、文件和块的位置信息、访问权限等信息。基于此，NameNode成为了访问HDFS的唯一入口。
  NameNode内部通过内存和磁盘文件两种方式管理元数据。其中磁盘上的元数据文件包括Fsimage内存元数据镜像文件和edits log（Journal）编辑日志。
* **从角色：datanode**
  DataNode是Hadoop HDFS中的从角色，负责具体的数据块存储。DataNode的数量决定了HDFS集群的整体数据存储能力。通过和NameNode配合维护着数据块。
* **主角色辅助角色： secondarynamenode**
  Secondary NameNode充当NameNode的辅助节点，但不能替代NameNode。主要是帮助主角色进行元数据文件的合并动作。可以通俗的理解为主角色的“秘书”。

namenode职责如下：

- NameNode仅存储HDFS的元数据：文件系统中所有文件的目录树，并跟踪整个集群中的文件，不存储实际数据。
- NameNode知道HDFS中任何给定文件的块列表及其位置。使用此信息NameNode知道如何从块中构建文件。
- NameNode不持久化存储每个文件中各个块所在的datanode的位置信息，这些信息会在系统启动时从DataNode重建。
- NameNode是Hadoop集群中的单点故障。
- NameNode所在机器通常会配置有大量内存（RAM）。

datanode职责如下：

- DataNode负责最终数据块block的存储。是集群的从角色，也称为Slave。
- DataNode启动时，会将自己注册到NameNode并汇报自己负责持有的块列表。
- 当某个DataNode关闭时，不会影响数据的可用性。 NameNode将安排由其他DataNode管理的块进行副本复制。
- DataNode所在机器通常配置有大量的硬盘空间，因为实际数据存储在DataNode中。

## 3.6 HDFS核心概念

**Pipeline管道**

Pipeline管道：Pipeline，中文翻译为管道。这是HDFS在上传文件写数据过程中采用的一种数据传输方式。

客户端将数据块写入第一个数据节点，第一个数据节点保存数据之后再将块复制到第二个数据节点，后者保存后将其复制到第三个数据节点。

![](..\图片\5-13【Hadoop】\3-12.png)

datanode之间采用pipeline线性传输，不是一次给三个datanode拓扑式传输原因：因为数据以管道的方式，顺序的沿着一个方向传输，这样能够充分利用每个机器的带宽，避免网络瓶颈和高延迟时的连接，最小化推送所有数据的延时。在线性推送模式下，每台机器所有的出口宽带都用于以最快的速度传输数据，而不是在多个接受者之间分配宽带。

**ACK应答响应**

ACK (Acknowledge character）即是确认字符，在数据通信中，接收方发给发送方的一种传输类控制字符。表示发来的数据已确认接收无误。

在HDFS pipeline管道传输数据的过程中，传输的反方向会进行ACK校验，确保数据传输安全。

![](..\图片\5-13【Hadoop】\3-13.png)

**默认3副本存储策略**

默认副本存储策略是由BlockPlacementPolicyDefault指定。

- 第一块副本：优先客户端本地，否则随机
- 第二块副本：不同于第一块副本的不同机架。
- 第三块副本：第二块副本相同机架不同机器。

## 3.7 HDFS工作流程

**HDFS写数据流程（上传文件）**

![](..\图片\5-13【Hadoop】\3-11.png)

流程如下：

1. HDFS客户端创建对象实例`DistributedFileSystem`， 该对象中封装了与HDFS文件系统操作的相关方法。
2. 调用`DistributedFileSystem`对象的`create()`方法，通过RPC请求NameNode创建文件。NameNode执行各种检查判断：目标文件是否存在、父目录是否存在、客户端是否具有创建该文件的权限。检查通过，NameNode就会为本次请求记下一条记录，返回`FSDataOutputStream`输出流对象给客户端用于写数据。
3. 客户端通过`FSDataOutputStream`输出流开始写入数据。 
4. 客户端写入数据时，将数据分成一个个数据包（packet 默认64k），内部组件`DataStreamer`请求NameNode挑 选出适合存储数据副本的一组DataNode地址，默认是3副本存储。 `DataStreamer`将数据包流式传输到`pipeline`的第一个DataNode，该DataNode存储数据包并将它发送到pipeline的第二个DataNode。同样，第二个DataNode存储数据包并且发送给第三个（也是最后一个）DataNode。
5. 传输的反方向上，会通过ACK机制校验数据包传输是否成功；
6. 客户端完成数据写入后，在`FSDataOutputStream`输出流上调用`close()`方法关闭。
7. `DistributedFileSystem`联系NameNode告知其文件写入完成，等待NameNode确认。因为namenode已经知道文件由哪些块组成（`DataStream`请求分配数据块），因此仅需等待最小复制块即可成功返回。 最小复制是由参数`dfs.namenode.replication.min`指定，默认是`1`。

**HDFS读数据流程（下载文件）**

![](..\图片\5-13【Hadoop】\3-14.png)

流程如下：

1. HDFS客户端创建对象实例`DistributedFileSystem`， 调用该对象的`open()`方法来打开希望读取的文件。
2. `DistributedFileSystem`使用RPC调用namenode来确定文件中前几个块的块位置（分批次读取）信息。对于每个块，namenode返回具有该块所有副本的datanode位置地址列表，并且该地址列表是排序好的，与客户端的网络拓扑距离近的排序靠前。
3. `DistributedFileSystem`将`FSDataInputStream`输入流返回到客户端以供其读取数据。
4. 客户端在`FSDataInputStream`输入流上调用`read()`方法。然后，已存储DataNode地址的`InputStream`连接到文件中第一个块的最近的DataNode。数据从DataNode流回客户端，结果客户端可以在流上重复调用`read()`。
5. 当该块结束时，`FSDataInputStream`将关闭与DataNode的连接，然后寻找下一个block块的最佳datanode位置。这些操作对用户来说是透明的。所以用户感觉起来它一直在读取一个连续的流。客户端从流中读取数据时，也会根据需要询问NameNode来检索下一批数据块的DataNode位置信息。
6. 一旦客户端完成读取，就对`FSDataInputStream`调用`close()`方法。

# 第四章 Hadoop MapReduce

MapReduce的思想核心是“先分再合，分而治之”。所谓“分而治之”就是把一个复杂的问题，按照一定的“分解”方法分为等价的规模较小的若干部分，然后逐个解决，分别找出各部分的结果，然后把各部分的结果组成整个问题的最终结果。

MapReduce一共有两个阶段，这两个阶段合起来正是MapReduce思想的体现：

- Map表示第一阶段，负责“拆分”：即把复杂的任务分解为若干个“简单的子任务”来并行处理。可以进行拆分的前提是这些小任务可以并行计算，彼此间几乎没有依赖关系。
- Reduce表示第二阶段，负责“合并”：即对map阶段的结果进行全局汇总。

比较形象解释MapReduce：要数停车场中的所有停放车的总数量。你数第一列，我数第二列…这就是Map阶段，人越多，能够同时数车的人就越多，速度就越快。数完之后，聚到一起，把所有人的统计数加在一起。这就是Reduce合并汇总阶段。

## 4.1 MapReduce介绍

Hadoop MapReduce是一个分布式计算框架，用于轻松编写分布式应用程序，这些应用程序以可靠，容错的方式并行处理大型硬件集群（数千个节点）上的大量数据（多TB数据集）。

> 分布式计算是一种计算方法，和集中式计算是相对的。随着计算技术的发展，有些应用需要非常巨大的计算能力才能完成，如果采用集中式计算，需要耗费相当长的时间来完成。分布式计算将该应用分解成许多小的部分，分配给多台计算机进行处理。这样可以节约整体计算时间，大大提高计算效率。

MapReduce是一种面向海量数据处理的一种指导思想，也是一种用于对大规模数据进行分布式计算的编程模型。

MapReduce特点如下：

* 易于编程：Mapreduce框架提供了用于二次开发的接口；简单地实现一些接口，就可以完成一个分布式程序。任务计算交给计算框架去处理，将分布式程序部署到hadoop集群上运行，集群节点可以扩展到成百上千个等。
* 良好的扩展性：当计算机资源不能得到满足的时候，可以通过增加机器来扩展它的计算能力。基于MapReduce的分布式计算得特点可以随节点数目增长保持近似于线性的增长，这个特点是MapReduce处理海量数据的关键，通过将计算节点增至几百或者几千可以很容易地处理数百TB甚至PB级别的离线数据。
* 高容错性：Hadoop集群是分布式搭建和部署得，任何单一机器节点宕机了，它可以把上面的计算任务转移到另一个节点上运行，不影响整个作业任务得完成，过程完全是由Hadoop内部完成的。
* 适合海量数据的离线处理：可以处理GB、TB和PB级别得数据量

MapReduce虽然有很多的优势，也有相对得局限性，局限性不代表不能做，而是在有些场景下实现的效果比较差，并不适合用MapReduce来处理。主要表现在以下几个方面：

* 实时计算性能差：MapReduce主要应用于离线作业，无法作到秒级或者是亚秒级的数据响应。
* 不能进行流式计算：流式计算特点是数据是源源不断得计算，并且数据是动态的；而MapReduce作为一个离线计算框架，主要是针对静态数据集得，数据是不能动态变化的。

一个完整的MapReduce程序在分布式运行时有三类：

- MRAppMaster：负责整个MR程序的过程调度及状态协调
- MapTask：负责map阶段的整个数据处理流程
- ReduceTask：负责reduce阶段的整个数据处理流程

一个MapReduce编程模型中只能包含一个Map阶段和一个Reduce阶段，或者只有Map阶段；不能有诸如多个map阶段、多个reduce阶段的情景出现；如果用户的业务逻辑非常复杂，那就只能多个MapReduce程序串行运行。

![](..\图片\5-13【Hadoop】\4-1.png)

整个MapReduce程序中，数据都是以`kv`键值对的形式流转的；在实际编程解决各种业务问题中，需要考虑每个阶段的输入输出`kv`分别是什么；MapReduce内置了很多默认属性，比如排序、分组等，都和数据的`k`有关，所以说`kv`的类型数据确定及其重要的。

## 4.2  MapReduce官方示例

一个最终完整版本的MR程序需要用户编写的代码和Hadoop自己实现的代码整合在一起才可以；其中用户负责map、reduce两个阶段的业务问题，Hadoop负责底层所有的技术问题；

由于MapReduce计算引擎天生的弊端（慢），当下企业中直接使用率已经日薄西山了，所以在企业中工作很少涉及到MapReduce直接编程，但是某些软件的背后还依赖MapReduce引擎。可以通过官方提供的示例来感受MapReduce及其内部执行流程，因为后续的新的计算引擎比如Spark，当中就有MapReduce深深的影子存在。

示例程序路径：`/export/server/hadoop-3.3.0/share/hadoop/mapreduce/`、示例程序：`hadoop-mapreduce-examples-3.3.0.jar`、MapReduce程序提交命令：`[hadoop jar|yarn jar] hadoop-mapreduce-examples-3.3.0.jar args…`、提交到YARN集群上分布式执行。

**评估圆周率π（PI）的值**

运行MapReduce程序评估一下圆周率的值，执行中可以去YARN页面上观察程序的执行的情况。

```sh
[root@node1 mapreduce]# pwd
/export/server/hadoop-3.3.0/share/hadoop/mapreduce
[root@node1 mapreduce]# hadoop jar hadoop-mapreduce-examples-3.3.0.jar pi 10 50
```

- 第一个参数：pi表示MapReduce程序执行圆周率计算任务；
- 第二个参数：用于指定map阶段运行的任务task次数，并发度，这里是10；
- 第三个参数：用于指定每个map任务取样的个数，这里是50。

**wordcount单词词频统计**

<!--WordCount 单词统计、词频统计-->

WordCount算是大数据计算领域经典的入门案例，相当于Hello World。WordCount编程实现思路如下：

- map阶段的核心：把输入的数据经过切割，全部标记`1`，因此输出就是`<单词，1>`。
- shuffle阶段核心：经过MR程序内部自带默认的排序分组等功能，把`key`相同的单词会作为一组数据构成新的`kv`对。
- reduce阶段核心：处理shuffle完的一组数据，该组数据就是该单词所有的键值对。对所有的`1`进行累加求和，就是单词的总次数。

WordCount程序提交：

* 上传课程资料中的文本文件`1.txt`到HDFS文件系统的`/input`目录下，如果没有这个目录，使用`shell`创建。`hadoop fs -mkdir /input`、`hadoop fs -put 1.txt /input`。

  ```apl
  Deer Bear River
  Car Car Driver
  Deer Car Bear
  ```

* 准备好之后，执行官方MapReduce实例，对上述文件进行单词次数统计。

  ```sh
  [root@node1 mapreduce]# pwd
  /export/server/hadoop-3.3.0/share/hadoop/mapreduce
  [root@node1 mapreduce]# hadoop jar hadoop-mapreduce-examples-3.3.0.jar wordcount /input /output
  ```
  
  第一个参数：wordcount表示执行单词统计任务；
  
  第二个参数：指定输入文件的路径；
  
  第三个参数：指定输出结果的路径（该路径不能已存在）；
  
* 执行流程如下：

![](..\图片\5-13【Hadoop】\4-5.png)

WordCount执行结果如下：

![](..\图片\5-13【Hadoop】\4-2.png)

```sh
[root@node1 mapreduce]# hadoop fs -cat /output/part-r-00000
Bear    2
Car     3
Deer    2
Driver  1
River   1
```

## 4.3 MapReduce执行流程

MapReduce整体执行流程图

![](..\图片\5-13【Hadoop】\4-3.png)

**Map阶段执行过程**

1. 第一阶段：把输入目录下文件按照一定的标准逐个进行逻辑切片，形成切片规划。默认`Split size = Block size（128M）`，每一个切片由一个MapTask处理。`（getSplits）`
2. 第二阶段：对切片中的数据按照一定的规则读取解析返回`<key,value>`对。默认是按行读取数据。key是每一行的起始位置偏移量，value是本行的文本内容。`（TextInputFormat）`
3. 第三阶段：调用Mapper类中的map方法处理数据。每读取解析出来的一个`<key,value>` ，调用一次map方法。
4. 第四阶段：按照一定的规则对Map输出的键值对进行分区`partition`。默认不分区，因为只有一个`reducetask`。分区的数量就是`reducetask`运行的数量。
5. 第五阶段：Map输出数据写入内存缓冲区，达到比例溢出到磁盘上。溢出`spill`的时候根据`key`进行排序`sort`。默认根据key字典序排序。
6. 第六阶段：对所有溢出文件进行最终的`merge`合并，成为一个文件。

**Reduce阶段执行流程**

1. 第一阶段：`ReduceTask`会主动从`MapTask`复制拉取属于需要自己处理的数据。
2. 第二阶段：把拉取来数据，全部进行合并`merge`，即把分散的数据合并成一个大的数据。再对合并后的数据排序。
3. 第三阶段是对排序后的键值对调用`reduce`方法。键相等的键值对调用一次`reduce`方法。最后把这些输出的键值对写入到HDFS文件中。

## 4.4 Shuffle机制

Shuffle的本意是洗牌、混洗的意思，就是把一组有规则的数据尽量打乱成无规则的数据。而在MapReduce中，Shuffle更像是洗牌的逆过程，指的是将map端的无规则输出按指定的规则“打乱”成具有一定规则的数据，以便reduce端接收处理。

一般把从Map产生输出开始到Reduce取得数据作为输入之前的过程称作shuffle。

![](..\图片\5-13【Hadoop】\4-4.png)

**Map端Shuffle**

- `Collect`阶段：将MapTask的结果收集输出到默认大小为100M的环形缓冲区，保存之前会对key进行分区的计算，默认Hash分区。
- `Spill`阶段：当内存中的数据量达到一定的阀值的时候，就会将数据写入本地磁盘，在将数据写入磁盘之前需要对数据进行一次排序的操作，如果配置了combiner，还会将有相同分区号和key的数据进行排序。
- `Merge`阶段：把所有溢出的临时文件进行一次合并操作，以确保一个MapTask最终只产生一个中间数据文件。

**Reducer端shuffle**

- `Copy`阶段： ReduceTask启动Fetcher线程到已经完成MapTask的节点上复制一份属于自己的数据。
- `Merge`阶段：在ReduceTask远程复制数据的同时，会在后台开启两个线程对内存到本地的数据文件进行合并操作。
- `Sort`阶段：在对数据进行合并的同时，会进行排序操作，由于MapTask阶段已经对数据进行了局部的排序，ReduceTask只需保证Copy的数据的最终整体有效性即可。

**shuffle机制弊端**

- Shuffle是MapReduce程序的核心与精髓，是MapReduce的灵魂所在。
- Shuffle也是MapReduce被诟病最多的地方所在。MapReduce相比较于Spark、Flink计算引擎慢的原因，跟Shuffle机制有很大的关系。
- Shuffle中频繁涉及到数据在内存、磁盘之间的多次往复。

# 第五章 Hadoop YARN

Apache Hadoop YARN （Yet Another Resource Negotiator，另一种资源协调者）是一种新的Hadoop资源管理器。YARN是一个通用资源管理系统和调度平台，可为上层应用提供统一的资源管理和调度。它的引入为集群在利用率、资源统一管理和数据共享等方面带来了巨大好处。

![](..\图片\5-13【Hadoop】\5-1.png)

YARN功能

- 资源管理系统：集群的硬件资源，和程序运行相关，比如内存、CPU等。
- 调度平台：多个程序同时申请计算资源如何分配，调度的规则（算法）。
- 通用：不仅仅支持MapReduce程序，理论上支持各种计算程序。YARN不关心你干什么，只关心你要资源，在有的情况下给你，用完之后还我。

可以把Hadoop YARN理解为相当于一个分布式的操作系统平台，而MapReduce等计算程序则相当于运行于操作系统之上的应用程序，YARN为这些程序提供运算所需的资源（内存、CPU等）。

Hadoop能有今天这个地位，YARN可以说是功不可没。因为有了YARN ，更多计算框架可以接入到 HDFS中，而不单单是 MapReduce，正是因为YARN的包容，使得其他计算框架能专注于计算性能的提升。HDFS可能不是最优秀的大数据存储系统，但却是应用最广泛的大数据存储系统， YARN功不可没。

## 5.1 YARN架构和组件

YARN架构如图所示：

![](..\图片\5-13【Hadoop】\5-2.png)

YARN3大组件：

- `ResourceManager（RM）`：YARN集群中的主角色，决定系统中所有应用程序之间资源分配的最终权限，即最终仲裁者。接收用户的作业提交，并通过NM分配、管理各个机器上的计算资源。
- `NodeManager（NM）`：YARN中的从角色，一台机器上一个，负责管理本机器上的计算资源。根据RM命令，启动Container容器、监视容器的资源使用情况。并且向RM主角色汇报资源使用情况。
- `ApplicationMaster（AM）`：用户提交的每个应用程序均包含一个AM。应用程序内的“老大”，负责程序内部各阶段的资源申请，监督程序的执行情况。

## 5.2 程序提交YARN交互流程

核心交互流程如下：

- MR作业提交 Client-->RM
- 资源的申请 MrAppMaster-->RM
- MR作业状态汇报 Container（Map|Reduce Task）-->Container（MrAppMaster）
- 节点的状态汇报 NM-->RM

当用户向 YARN 中提交一个应用程序后， YARN将分两个阶段运行该应用程序 。第一个阶段是客户端申请资源启动运行本次程序的`ApplicationMaster`；第二个阶段是由`ApplicationMaster`根据本次程序内部具体情况，为它申请资源，并监控它的整个运行过程，直到运行完成。

![](..\图片\5-13【Hadoop】\5-3.png)

**MR提交YARN交互流程**

- 第1步、用户通过客户端向YARN中`ResourceManager`提交应用程序（比如hadoop jar提交MR程序）；
- 第2步、`ResourceManager`为该应用程序分配第一个`Container`（容器），并与对应的`NodeManager`通信，要求它在这个`Container`中启动这个应用程序的`ApplicationMaster`。
- 第3步、`ApplicationMaster`启动成功之后，首先向`ResourceManager`注册并保持通信，这样用户可以直接通过`ResourceManager`查看应用程序的运行状态（处理了百分之几）;
- 第4步、`AM`为本次程序内部的各个Task任务向`RM`申请资源，并监控它的运行状态;
- 第5步、一旦 `ApplicationMaster` 申请到资源后，便与对应的 `NodeManager` 通信，要求它启动任务。
- 第6步、`NodeManager` 为任务设置好运行环境后，将任务启动命令写到一个脚本中，并通过运行该脚本启动任务。
- 第7步、各个任务通过某个 `RPC` 协议向 `ApplicationMaster` 汇报自己的状态和进度，以让 `ApplicationMaster` 随时掌握各个任务的运行状态，从而可以在任务失败时重新启动任务。在应用程序运行过程中，用户可随时通过`RPC` 向 `ApplicationMaster` 查询应用程序的当前运行状态。
- 第8步、应用程序运行完成后，`ApplicationMaster` 向 `ResourceManager` 注销并关闭自己。

## 5.3 YARN资源调度器Scheduler

在理想情况下，应用程序提出的请求将立即得到YARN批准。但是实际中，资源是有限的，并且在繁忙的集群上，应用程序通常将需要等待其某些请求得到满足。YARN调度程序的工作是根据一些定义的策略为应用程序分配资源。

在YARN中，负责给应用分配资源的就是`Scheduler`，它是`ResourceManager`的核心组件之一。`Scheduler`完全专
用于调度作业，它无法跟踪应用程序的状态。

一般而言，调度是一个难题，并且没有一个“最佳”策略，为此，YARN提供了多种调度器和可配置的策略供选择。

- `FIFO Scheduler（先进先出调度器）`
- `Capacity Scheduler（容量调度器）`
- `Fair Scheduler（公平调度器）`

Apache版本YARN默认使用`Capacity Scheduler`。如果需要使用其他的调度器，可以在`yarn-site.xml`中的`yarn.resourcemanager.scheduler.class`进行配置。

**FIFO Scheduler**

FIFO Scheduler是Hadoop1.x中JobTracker原有的调度器实现，此调度器在YARN中保留了下来。FIFO Scheduler是一个先进先出的思想，即先提交的应用先运行。调度工作不考虑优先级和范围，适用于负载较低的小规模集群。当使用大型共享集群时，它的效率较低且会导致一些问题。

FIFO Scheduler拥有一个控制全局的队列queue，默认queue名称为default，该调度器会获取当前集群上所有的资源信息作用于这个全局的queue。

- 优势：无需配置、先到先得、易于执行
- 坏处：任务的优先级不会变高，因此高优先级的作业需要等待。不适合共享集群

**Capacity Scheduler概述**

Capacity Scheduler容量调度是Apache Hadoop3.x默认调度策略。该策略允许多个组织共享整个集群资源，每个组织可以获得集群的一部分计算能力。通过为每个组织分配专门的队列，然后再为每个队列分配一定的集群资源，这样整个集群就可以通过设置多个队列的方式给多个组织提供服务了。

Capacity可以理解成一个个的资源队列，这个资源队列是用户自己去分配的。队列内部又可以垂直划分，这样一个组织内部的多个成员就可以共享这个队列资源了，在一个队列内部，资源的调度是采用的是先进先出(FIFO)策略。

Capacity Scheduler调度器以队列为单位划分资源。简单通俗点来说，就是一个个队列有独立的资源，队列的结构和资源是可以进行配置的。

![](..\图片\5-13【Hadoop】\5-4.png)

Capacity Scheduler特性优势：

- 层次化的队列设计（Hierarchical Queues）：层次化的管理，可以更容易、更合理分配和限制资源的使用。
- 容量保证（Capacity Guarantees）：每个队列上都可以设置一个资源的占比，保证每个队列都不会占用整个集群的资源。
- 安全（Security）：每个队列有严格的访问控制。用户只能向自己的队列里面提交任务，而且不能修改或者访问其他队列的任务。
- 弹性分配（Elasticity）：空闲的资源可以被分配给任何队列。当多个队列出现争用的时候，则会按照权重比例进行平衡。

**Fair Scheduler概述**

Fair Scheduler叫做公平调度，提供了YARN应用程序公平地共享大型集群中资源的另一种方式。使所有应用在平
均情况下随着时间的流逝可以获得相等的资源份额。Fair Scheduler设计目标是为所有的应用分配公平的资源（对公平的定义通过参数来设置）。公平调度可以在多个队列间工作，允许资源共享和抢占。

公平共享：

- 有两个用户A和B，每个用户都有自己的队列。
- A启动一个作业，由于没有B的需求，它分配了集群所有可用的资源。
- 然后B在A的作业仍在运行时启动了一个作业，经过一段时间，A,B各自作业都使用了一半的资源。
- 现在，如果B用户在其他作业仍在运行时开始第二个作业，它将与B的另一个作业共享其资源，因此B的每个作业将拥有资源的四分之一，而A的继续将拥有一半的资源。结果是资源在用户之间公平地共享。

Fair Scheduler特性优势：

- 分层队列：队列可以按层次结构排列以划分资源，并可以配置权重以按特定比例共享集群。
- 基于用户或组的队列映射：可以根据提交任务的用户名或组来分配队列。如果任务指定了一个队列,则在该队列中提交任务。
- 资源抢占：根据应用的配置，抢占和分配资源可以是友好的或是强制的。默认不启用资源抢占。
- 保证最小配额：可以设置队列最小资源，允许将保证的最小份额分配给队列，保证用户可以启动任务。当队列不能满足最小资源时,可以从其它队列抢占。当队列资源使用不完时,可以给其它队列使用。这对于确保某些用户、组或生产应用始终获得足够的资源。
- 允许资源共享：即当一个应用运行时,如果其它队列没有任务执行,则可以使用其它队列,当其它队列有应用需要资源时再将占用的队列释放出来。所有的应用都从资源队列中分配资源。
- 默认不限制每个队列和用户可以同时运行应用的数量。可以配置来限制队列和用户并行执行的应用数量。限制并行执行应用数量不会导致任务提交失败,超出的应用会在队列中等待。
