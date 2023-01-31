# 第一章 Spark基础入门

Apache Spark是用于大规模数据（large-scala data）处理的统一（unified）分析引擎。

Spark 最早源于一篇论文 Resilient Distributed Datasets: A Fault-Tolerant Abstraction for In-Memory Cluster Computing，该论文是由加州大学柏克莱分校的 Matei Zaharia 等人发表的。论文中提出了一种弹性分布式数据集（即 RDD）的概念。

RDD 是一种分布式内存抽象，其使得程序员能够在大规模集群中做内存运算，并且有一定的容错方式。而这也是整个 Spark 的核心数据结构，Spark 整个平台都围绕着RDD进行。

![](..\图片\6-04【Spark】\1-1.png)

Spark 借鉴了 MapReduce 思想发展而来，保留了其分布式并行计算的优点并改进了其明显的缺陷。让中间数据存储在内存中提高了运行速度、并提供丰富的操作数据的API提高了开发速度。

Spark是一款分布式内存计算的统一分析引擎。其特点就是对任意类型的数据进行自定义计算。Spark可以计算：结构化、半结构化、非结构化等各种类型的数据结构，同时也支持使用Python、Java、Scala、R以及SQL语言去开发应用程序计算数据。Spark的适用面非常广泛，所以，被称之为统一的（适用面广）的分析引擎（数据处理）。

**Spark VS Hadoop(MapReduce)**

|              | Hadoop                                    | Spark                                                        |
| ------------ | ----------------------------------------- | ------------------------------------------------------------ |
| 类型         | 基础平台，包含计算，存储，调度            | 纯计算工具（分布式）                                         |
| 场景         | 海量数据批处理（磁盘迭代计算）            | 海量数据的批处理（内存迭代计算、交互式计算）、海量数据流计算 |
| 价格         | 对机器要求低，便宜                        | 对内存有要求，相对较贵                                       |
| 编程范式     | Map+Reduce，API较为底层，算法适应性差     | RDD组成DAG有向无环图，API较为顶层，方便使用                  |
| 数据存储结构 | MapReduce中间计算结果在HDFS磁盘上，延迟大 | RDD中间运算结果在内存中，延迟小                              |
| 运行方式     | Task以进程方式维护，任务启动慢            | Task以线程方式维护，任务启动快，可批量创建提高并行能力       |

尽管Spark相对于Hadoop而言具有较大优势，但Spark并不能完全替代Hadoop

- 在计算层面，Spark相比较MR（MapReduce）有巨大的性能优势，但至今仍有许多计算工具基于MR构架，比如非常成熟的Hive
- Spark仅做计算，而Hadoop生态圈不仅有计算（MR）也有存储（HDFS）和资源管理调度（YARN），HDFS和YARN仍是许多大数据体系的核心架构。

Hadoop的基于进程的计算和Spark基于线程方式优缺点：Hadoop中的MR中每个map/reduce task都是一个java进程方式运行，好处在于进程之间是互相独立的，每个task独享进程资源，没有互相干扰，监控方便，但是问题在于task之间不方便共享数据，执行效率比较低。比如多个map task读取不同数据源文件需要将数据源加载到每个map task中，造成重复加载和浪费内存。而基于线程的方式计算是为了数据共享和提高执行效率，Spark采用了线程的最小的执行单位，但缺点是线程之间会有资源竞争。

**Spark特点**

Spark四大特点：速度快、易于使用、通用性强、运行方式。

* 速度快
  由于Apache Spark支持内存计算，并且通过DAG（有向无环图）执行引擎支持无环数据流，所以官方宣称其在内存中的运算速度要比 Hadoop的MapReduce快100倍，在硬盘中要快10倍。

  Spark处理数据与MapReduce处理数据相比，有如下两个不同点：Spark处理数据时，可以将中间处理结果数据存储到内存中；Spark 提供了非常丰富的算子(API)， 可以做到复杂任务在一个Spark 程序中完成.

* 易于使用
  Spark 的版本已经更新到 Spark 3.2.0（截止日期2021.10.13），支持了包括 Java、Scala、Python 、R和SQL语言在内的多种语言。为了 兼容Spark2.x企业级应用场景，Spark仍然持续更新Spark2版本。

* 通用性强
  在 Spark 的基础上，Spark 还提供了包括Spark SQL、Spark Streaming、MLib 及GraphX在内的多个工具库，我们可以在一个应用中无缝地使用这些工具库。

* 运行方式
  Spark 支持多种运行方式，包括在 Hadoop 和 Mesos 上，也支持 Standalone的独立运行模式，同时也可以运行在云Kubernetes（Spark 2.3开始支持）上。

**Spark 框架模块**

整个Spark 框架模块包含：Spark Core、 Spark SQL、 Spark Streaming、 Spark GraphX、 Spark MLlib，而后四项的能力都是建立在核心引擎之上

![](..\图片\6-04【Spark】\1-2.png)

Spark Core：Spark的核心，Spark核心功能均由Spark Core模块提供，是Spark运行的基础。Spark Core以RDD为数据抽象，提供Python、Java、Scala、R语言的API，可以编程进行海量离线数据批处理计算。

SparkSQL：基于SparkCore之上，提供结构化数据的处理模块。SparkSQL支持以SQL语言对数据进行处理，SparkSQL本身针对离线计算场景。同时基于SparkSQL，Spark提供了StructuredStreaming模块，可以以SparkSQL为基础，进行数据的流式计算。

SparkStreaming：以SparkCore为基础，提供数据的流式计算功能。

MLlib：以SparkCore为基础，进行机器学习计算，内置了大量的机器学习库和API算法等。方便用户以分布式计算的模式进行机器学习计算。

GraphX：以SparkCore为基础，进行图计算，提供了大量的图计算API，方便用于以分布式计算模式进行图计算。

**Spark的运行模式**

Spark提供多种运行模式，包括：

- 本地模式（单机）：本地模式就是以一个独立的进程，通过其内部的多个线程来模拟整个Spark运行时环境。
- Standalone模式（集群）：Spark中的各个角色以独立进程的形式存在，并组成Spark集群环境。
- Hadoop YARN模式（集群）：Spark中的各个角色运行在YARN的容器内部，并组成Spark集群环境。
- Kubernetes模式（容器集群）：Spark中的各个角色运行在Kubernetes的容器内部，并组成Spark集群环境。
- 云服务模式（运行在云平台上）。

## 1.1 环境搭建-Local

本质：启动一个JVM Process进程(一个进程里面有多个线程)，执行任务Task

 Local模式可以限制模拟Spark集群环境的线程数量， 即`Local[N]` 或 `Local[*]`。

- 其中N代表可以使用N个线程，每个线程拥有一个cpu core。如果不指定N，则默认是1个线程（该线程有1个core）。 通常Cpu有几个Core，就指定几个线程，最大化利用计算能力。

- 如果是`local[*]`，则代表 Run Spark locally with as many worker threads as logical cores on your machine.按照Cpu最多的Cores设置线程数本。


Local 下的角色分布：

- 资源管理：Master是Local进程本身；Worker是Local进程本身。

- 任务执行：Driver是Local进程本身；Executor不存在，没有独立的Executor角色， 由Local进程(也就是Driver)内的线程提供计算能力。

  > Driver也算一种特殊的Executor，只不过多数时候，我们将Executor当做纯Worker对待，这样和Driver好区分(一类是管理 一类是工人)

Local模式只能运行一个Spark程序，如果执行多个Spark程序，那就是由多个相互独立的Local进程在执行。

环境部署步骤如下：

1. Anaconda On Linux 安装 (单台服务器)

   上传资料中提供的`Anaconda3-2021.05-Linux-x86_64.sh`文件到Linux服务器上的`/export/software`，然后安装`sh ./Anaconda3-2021.05-Linux-x86_64.sh`。
   接下来，回车(接受安装) -> 四次空格键(查阅用户协议) -> yes(接受用户协议) -> /export/server/anaconda3(安装路径) -> yes(进行初始化)。 
   之后退出shell窗口，重新连接，出现（base）代表好了。

   ```sh
   Last login: Sun Nov 20 14:18:08 2022 from 192.168.88.1
   (base) [root@node1 ~]# 
   ```

   接下来配置一下conda连接源：

   ```sh
   vim ~/.condarc
   
   channels:
     - defaults
   show_channel_urls: true
   default_channels:
     - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main
     - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/r
     - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/msys2
   custom_channels:
     conda-forge: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     msys2: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     bioconda: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     menpo: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     pytorch: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     simpleitk: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
   ```

   创建pyspark虚拟环境

   ```sh
   (base) [root@node1 ~]# conda create -n pyspark python=3.8
   
   # To activate this environment, use
   #
   #     $ conda activate pyspark
   #
   # To deactivate an active environment, use
   #
   #     $ conda deactivate
   ```

2. 上传并解压缩

   ```sh
   (pyspark) [root@node1 software]# pwd
   /export/software
   (pyspark) [root@node1 software]# ll
   total 293920
   drwxr-xr-x 2 root root      4096 Nov 12 16:55 mysql
   -rw-r--r-- 1 root root 300965906 Nov 20 14:38 spark-3.2.0-bin-hadoop3.2.tgz
   (pyspark) [root@node1 spark-3.2.0-bin-hadoop3.2]# tar -zxvf spark-3.2.0-bin-hadoop3.2.tgz -C /export/server
   ```

3. 配置环境变量。配置Spark由如下5个环境变量需要设置

   - `SPARK_HOME`: 表示Spark安装路径在哪里
   - `PYSPARK_PYTHON`: 表示Spark想运行Python程序, 那么去哪里找python执行器
   - `JAVA_HOME`: 告知Spark Java在哪里 
   - `HADOOP_CONF_DIR`: 告知Spark Hadoop的配置文件在哪里 
   - `HADOOP_HOME`: 告知Spark  Hadoop安装在哪里

   ```sh
   # vim /etc/profile
   
   export JAVA_HOME=/export/server/jdk1.8.0_144
   export PATH=$PATH:$JAVA_HOME/bin
   export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
   
   export HADOOP_HOME=/export/server/hadoop-3.3.0
   export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
   
   export SPARK_HOME=/export/server/spark-3.2.0-bin-hadoop3.2
   export PYSPARK_PYTHON=/export/server/anaconda3/envs/pyspark/bin/python3.8
   export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
   ```

     `PYSPARK_PYTHON`和 `JAVA_HOME` 需要同样配置在: `/root/.bashrc`中

   ```sh
   (pyspark) [root@node1 server]# vim ~/.bashrc
   
   export JAVA_HOME=/export/server/jdk1.8.0_144
   export PYSPARK_PYTHON=/export/server/anaconda3/envs/pyspark/bin/python3.8
   ```

4. bin/pyspark

   bin/pyspark 程序, 可以提供一个  `交互式`的 Python解释器环境, 在这里面可以写普通python代码, 以及spark代码

   ```sh
   (pyspark) [root@node1 bin]# pwd
   /export/server/spark-3.2.0-bin-hadoop3.2/bin
   (pyspark) [root@node1 bin]# ./pyspark 
   Python 3.8.15 (default, Nov  4 2022, 20:59:55) 
   [GCC 11.2.0] :: Anaconda, Inc. on linux
   Type "help", "copyright", "credits" or "license" for more information.
   ...
   ```

   ```sh
   >>> print("Hello Spark")
   Hello Spark
   >>> sc.parallelize([1, 2, 3, 4, 5]).map(lambda x: x * 10).collect()
   [10, 20, 30, 40, 50]   
   ```

   按住`CTRL + D`退出该程序。

5. WEB UI (4040)

   Spark程序在运行的时候, 会绑定到机器的`4040`端口上。如果4040端口被占用, 会顺延到`4041 ... 4042...`。4040端口是一个WEBUI端口, 可以在浏览器内打开，输入`服务器ip:4040` 即可打开。

Local模式就是以一个独立进程配合其内部线程来提供完成Spark运行时环境。 Local模式可以通过spark-shell/pyspark/spark-submit等来开启。

bin/pyspark是一个交互式的解释器执行环境,环境启动后就得到了一个Local Spark环境，可以运行Python代码去进行Spark计算,类似Python自带解释器

## 1.2 环境搭建-Standalone

Standalone模式是Spark自带的一种集群模式，不同于前面本地模式启动多个进程来模拟集群的环境，Standalone模式是真实地在多个机器之间搭建Spark集群的环境，完全可以利用该模式搭建多机器集群，用于实际的大数据处理。

StandAlone 是完整的Spark运行环境,其中：

- Master角色以Master进程存在, Worker角色以Worker进程存在
- Driver和Executor运行于Worker进程内, 由Worker提供资源供给它们运行

StandAlone集群在进程上主要有3类进程:

- 主节点Master进程：Master角色, 管理整个集群资源，并托管运行各个任务的Driver
- 从节点Workers：Worker角色, 管理每个机器的资源，分配对应的资源来运行Executor(Task)；每个从节点分配资源信息给Worker管理，资源信息包含内存Memory和CPU Cores核数
- 历史服务器HistoryServer(可选)：Spark Application运行完成以后，保存事件日志数据至HDFS，启动HistoryServer可以查看应用运行相关信息。

使用三台Linux虚拟机来组成集群环境，分别是：`node1`、`node2`、`node3`。整个集群提供1个master进程 和 3个worker进程。

- 
  node1运行: Spark的Master进程  和 1个Worker进程

- 
  node2运行: spark的1个worker进程

- 
  node3运行: spark的1个worker进程


安装步骤如下：

1. 在所有机器安装Python(Anaconda)。都创建`pyspark`虚拟环境 以及安装虚拟环境所需要的包`pyspark jieba pyhive`

   上传资料中提供的`Anaconda3-2021.05-Linux-x86_64.sh`文件到Linux服务器上的`/export/software`，然后安装`sh ./Anaconda3-2021.05-Linux-x86_64.sh`。
   接下来，回车(接受安装) -> 四次空格键(查阅用户协议) -> yes(接受用户协议) -> `/export/server/anaconda3`(安装路径) -> yes(进行初始化)。 
   之后退出shell窗口，重新连接，出现（base）代表好了。

   ```sh
   Last login: Sun Nov 20 14:18:08 2022 from 192.168.88.1
   (base) [root@node1 ~]# 
   ```

   接下来配置一下conda连接源：

   ```sh
   vim ~/.condarc
   
   channels:
     - defaults
   show_channel_urls: true
   default_channels:
     - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main
     - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/r
     - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/msys2
   custom_channels:
     conda-forge: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     msys2: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     bioconda: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     menpo: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     pytorch: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
     simpleitk: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
   ```

   创建pyspark虚拟环境

   ```sh
   (base) [root@node1 ~]# conda create -n pyspark python=3.8
   
   # To activate this environment, use
   #
   #     $ conda activate pyspark
   #
   # To deactivate an active environment, use
   #
   #     $ conda deactivate
   ```

2. 在所有机器配置环境变量。

   配置环境变量。配置Spark由如下5个环境变量需要设置

   - `SPARK_HOME`: 表示Spark安装路径在哪里
   - `PYSPARK_PYTHON`: 表示Spark想运行Python程序, 那么去哪里找python执行器
   - `JAVA_HOME`: 告知Spark Java在哪里 
   - `HADOOP_CONF_DIR`: 告知Spark Hadoop的配置文件在哪里 
   - `HADOOP_HOME`: 告知Spark  Hadoop安装在哪里

   ```sh
   # vim /etc/profile
   
   export JAVA_HOME=/export/server/jdk1.8.0_144
   export PATH=$PATH:$JAVA_HOME/bin
   export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
   
   export HADOOP_HOME=/export/server/hadoop-3.3.0
   export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
   
   export SPARK_HOME=/export/server/spark-3.2.0-bin-hadoop3.2
   export PYSPARK_PYTHON=/export/server/anaconda3/envs/pyspark/bin/python3.8
   export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
   ```

   `PYSPARK_PYTHON`和 `JAVA_HOME` 需要同样配置在: `/root/.bashrc`中

   ```sh
   (pyspark) [root@node1 server]# vim ~/.bashrc
   
   export JAVA_HOME=/export/server/jdk1.8.0_144
   export PYSPARK_PYTHON=/export/server/anaconda3/envs/pyspark/bin/python3.8
   ```

3. 配置配置文件

   进入到spark的配置文件目录中, `cd /export/server/spark-3.2.0-bin-hadoop3.2/conf`

   ```shell
   (base) [root@node1 server]# cd /export/server/spark-3.2.0-bin-hadoop3.2/
   (base) [root@node1 spark-3.2.0-bin-hadoop3.2]# cd conf
   (base) [root@node1 conf]# ll
   total 36
   -rw-r--r-- 1 1000 1000 1105 Oct  6  2021 fairscheduler.xml.template
   -rw-r--r-- 1 1000 1000 2471 Oct  6  2021 log4j.properties.template
   -rw-r--r-- 1 1000 1000 9141 Oct  6  2021 metrics.properties.template
   -rw-r--r-- 1 1000 1000 1292 Oct  6  2021 spark-defaults.conf.template
   -rwxr-xr-x 1 1000 1000 4428 Oct  6  2021 spark-env.sh.template
   -rw-r--r-- 1 1000 1000  865 Oct  6  2021 workers.template
   (base) [root@node1 conf]# 
   ```
   
   配置workers文件
   
   
      ```shell
   # 改名, 去掉后面的.template后缀
   mv workers.template workers
   
   # 编辑worker文件
   vim workers
   # 将里面的localhost删除, 下面节点追加到workers文件内
   # 功能: 这个文件就是指示了  当前SparkStandAlone环境下, 有哪些worker
   # A Spark Worker will be started on each of the machines listed below.
   node1
   node2
   node3
      ```
   
   
   配置spark-env.sh文件
   
   
      ```shell
   # 1. 改名
   mv spark-env.sh.template spark-env.sh
   
   # 2. 编辑spark-env.sh, 在底部追加如下内容
   vim spark-env.sh
   
   ## 设置JAVA安装目录
   JAVA_HOME=/export/server/jdk1.8.0_144
   
   ## HADOOP软件配置文件目录，读取HDFS上文件和运行YARN集群
   HADOOP_CONF_DIR=/export/server/hadoop-3.3.0/etc/hadoop
   YARN_CONF_DIR=/export/server/hadoop-3.3.0/etc/hadoop
   
   ## 指定spark老大Master的IP和提交任务的通信端口
   # 告知Spark的master运行在哪个机器上
   export SPARK_MASTER_HOST=node1
   # 告知sparkmaster的通讯端口
   export SPARK_MASTER_PORT=7077
   # 告知spark master的 webui端口
   SPARK_MASTER_WEBUI_PORT=8080
   
   # worker cpu可用核数
   SPARK_WORKER_CORES=1
   # worker可用内存
   SPARK_WORKER_MEMORY=1g
   # worker的工作通讯地址
   SPARK_WORKER_PORT=7078
   # worker的 webui地址
   SPARK_WORKER_WEBUI_PORT=8081
   
   ## 设置历史服务器
   # 配置的意思是  将spark程序运行的历史日志 存到hdfs的/sparklog文件夹中
   SPARK_HISTORY_OPTS="-Dspark.history.fs.logDirectory=hdfs://node1:8020/sparklog/ -Dspark.history.fs.cleaner.enabled=true"
      ```
   
   
   在HDFS上创建程序运行历史记录存放的文件夹:
   
   
      ```shell
   hadoop fs -mkdir /sparklog
   hadoop fs -chmod 777 /sparklog
      ```
   
   
   配置spark-defaults.conf文件
   
   
      ```shell
   # 1. 改名
   mv spark-defaults.conf.template spark-defaults.conf
   
   # 2. 修改内容, 追加如下内容
   vim spark-defaults.conf
   
   # 修改粘贴模式
   :set paste
   
   # 开启spark的日期记录功能
   spark.eventLog.enabled 	true
   # 设置spark日志记录的路径
   spark.eventLog.dir	 hdfs://node1:8020/sparklog/ 
   # 设置spark日志是否启动压缩
   spark.eventLog.compress 	true
      ```
   
   
   配置log4j.properties 文件 [可选配置]
   
   
      ```shell
   # 1. 改名
   mv log4j.properties.template log4j.properties
   
   # 2. 修改内容 参考下图
   vim log4j.properties
      ```
   
      ```sh
   # Set everything to be logged to the console
   # 这里修改级别为WARN 原本是INFO
   log4j.rootCategory=WARN, console
   log4j.appender.console=org.apache.log4j.ConsoleAppender
   log4j.appender.console.target=System.err
   log4j.appender.console.layout=org.apache.log4j.PatternLayout
   log4j.appender.console.layout.ConversionPattern=%d{yy/MM/dd HH:mm:ss} %p %c{1}: %m%n
      ```
   
      > 这个文件的修改不是必须的,  为什么修改为WARN. 因为Spark是个话痨
      >
      > 会疯狂输出日志, 设置级别为WARN 只输出警告和错误日志, 不要输出一堆废话.

4. 将Spark安装文件夹  分发到其它的服务器上

   ```sh
   # 进入指定目录
   cd /export/server
   
   scp -r spark-3.2.0-bin-hadoop3.2 node2:/export/server/
   scp -r spark-3.2.0-bin-hadoop3.2 node3:/export/server/
   ```

5. 启动历史服务器

   ```sh
   [root@node1 server]# cd /export/server/spark-3.2.0-bin-hadoop3.2
   [root@node1 spark-3.2.0-bin-hadoop3.2]# 启动历史服务器
   [root@node1 spark-3.2.0-bin-hadoop3.2]# sbin/start-history-server.sh
   ```

   历史服务器的默认端口是: 18080。我们启动在node1上, 可以在浏览器打开http://192.168.88.151:18080/来进入到历史服务器的WEB UI上.

6. 启动Spark的Master和Worker进程

   ```sh
   # 启动全部master和worker
   sbin/start-all.sh
   
   # 或者可以一个个启动:
   # 启动当前机器的master
   sbin/start-master.sh
   # 启动当前机器的worker
   sbin/start-worker.sh
   
   # 停止全部
   sbin/stop-all.sh
   
   # 停止当前机器的master
   sbin/stop-master.sh
   
   # 停止当前机器的worker
   sbin/stop-worker.sh
   ```

7. 查看Master的WEB UI

   默认端口master我们设置到了8080。如果端口被占用, 会顺延到8081 ...;8082... 8083... 直到申请到端口为止。   可以在日志中查看, 具体顺延到哪个端口上:`Service 'MasterUI' could not bind on port 8080. Attempting port 8081.`   浏览器输入http://192.168.88.151:8080/，可以进入web-ui页面。

**连接到StandAlone集群**

1. bin/pyspark

   执行:
   
   
      ```shell
   bin/pyspark --master spark://node1:7077
   # 通过--master选项来连接到 StandAlone集群
   # 如果不写--master选项, 默认是local模式运行
      ```
   
      ```sh
   (base) [root@node1 spark-3.2.0-bin-hadoop3.2]# bin/pyspark --master spark://node1:7077
   Python 3.8.15 (default, Nov  4 2022, 20:59:55) 
   [GCC 11.2.0] :: Anaconda, Inc. on linux
   Type "help", "copyright", "credits" or "license" for more information.
   22/12/27 10:39:50 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
   Welcome to
         ____              __
        / __/__  ___ _____/ /__
       _\ \/ _ \/ _ `/ __/  '_/
      /__ / .__/\_,_/_/ /_/\_\   version 3.2.0
         /_/
   
   Using Python version 3.8.15 (default, Nov  4 2022 20:59:55)
   Spark context Web UI available at http://node1:4040
   Spark context available as 'sc' (master = spark://node1:7077, app id = app-20221227103959-0000).
   SparkSession available as 'spark'.
   >>> sc.parallelize([1, 2, 3, 4, 5]).map(lambda x: x * 10).collect()
      ```
   
   `CTRL + d` 退出

2. bin/spark-shell，`CTRL + d` 退出

   ```sh
   bin/spark-shell --master spark://node1:7077
   # 同样适用--master来连接到集群使用
   ```

   ```sh
   // 测试代码
   sc.parallelize(Array(1,2,3,4,5)).map(x=> x + 1).collect()
   ```

3. bin/spark-submit (PI)

   ```sh
   bin/spark-submit --master spark://node1:7077 /export/server/spark/examples/src/main/python/pi.py 100
   # 同样使用--master来指定将任务提交到集群运行
   ```

   这个由于是直接提交的所以任务完成后需要打开历史服务器来查看资源利用情况。可以在浏览器打开http://192.168.88.151:18080/来进入到历史服务器的WEB UI上.

## 1.3 Spark 应用架构

登录到Spark HistoryServer历史服务器WEB UI界面，点击刚刚运行圆周率PI程序：

![](..\图片\6-04【Spark】\1-3.png)

查看应用运行状况，切换到【Executors】Tab页面

![](..\图片\6-04【Spark】\1-4.png)

从图中可以看到Spark Application运行到集群上时，由两部分组成：`Driver Program`和`Executors`。

1. Driver Program

   相当于AppMaster，整个应用管理者，负责应用中所有Job的调度执行;

   运行JVM Process，运行程序的MAIN函数，必须创建SparkContext上下文对象；

   一个SparkApplication仅有一个；

2. Executors

   相当于一个线程池，运行JVM Process，其中有很多线程，每个线程运行一个Task任务，一个Task任务运行需要1 Core CPU，所有可以认为Executor中线程数就等于CPU Core核数；

   一个Spark Application可以有多个，可以设置个数和资源信息

![](..\图片\6-04【Spark】\1-5.png)

用户程序从最开始的提交到最终的计算执行，需要经历以下几个阶段：

1. 用户程序创建 SparkContext 时，新创建的 SparkContext 实例会连接到 ClusterManager。 Cluster Manager 会根据用户提交时设置的 CPU 和内存等信息为本次提交分配计算资源，启动 Executor。
2. Driver会将用户程序划分为不同的执行阶段Stage，每个执行阶段Stage由一组完全相同Task组成，这些Task分别作用于待处理数据的不同分区。在阶段划分完成和Task创建后， Driver会向Executor发送 Task；
3. Executor在接收到Task后，会下载Task的运行时依赖，在准备好Task的执行环境后，会开始执行Task，并且将Task的运行状态汇报给Driver；
4. Driver会根据收到的Task的运行状态来处理不同的状态更新。 Task分为两种：一种是Shuffle Map Task，它实现数据的重新洗牌，洗牌的结果保存到Executor 所在节点的文件系统中；另外一种是Result Task，它负责生成结果数据；
5. Driver 会不断地调用Task，将Task发送到Executor执行，在所有的Task 都正确执行或者超过执行次数的限制仍然没有执行成功时停止；

## 1.4 Spark程序运行层次结构

在前面我们接触到了不少的监控页面,有4040,有8080,有18080，区别如下：

- 4040: 是一个运行的Application在运行的过程中临时绑定的端口,用以查看当前任务的状态.4040被占用会顺延到4041.4042等。4040是一个临时端口,当前程序运行完成后, 4040就会被注销哦
- 8080: 默认是StandAlone下, Master角色(进程)的WEB端口,用以查看当前Master(集群)的状态
- 18080: 默认是历史服务器的端口, 由于每个程序运行完成后,4040端口就被注销了. 在以后想回看某个程序的运行状态就可以通过历史服务器查看,历史服务器长期稳定运行,可供随时查看被记录的程序的运行过程.

运行起来一个Spark Application, 然后打开其18080端口,并查看历史服务器资源状态：

![](..\图片\6-04【Spark】\1-6.png)

可以发现在一个Spark Application中，包含多个Job，每个Job有多个Stage组成，每个Job执行按照DAG图进行的。其中每个Stage中包含多个Task任务，每个Task以线程Thread方式执行，需要1Core CPU。

Spark Application程序运行时三个核心概念：Job、Stage、Task，说明如下：

- Job：由多个 Task 的并行计算部分，一般 Spark 中的action 操作（如 save、collect，后面进一步说明），会生成一个 Job。
- Stage：Job 的组成单位，一个 Job 会切分成多个 Stage，Stage 彼此之间相互依赖顺序执行，而每个 Stage 是多个 Task 的集合，类似 map 和 reduce stage。
- Task：被分配到各个 Executor 的单位工作内容，它是Spark 中的最小执行单位，一般来说有多少个 Paritition（物理层面的概念，即分支可以理解为将数据划分成不同部分并行处理），就会有多少个 Task，每个 Task 只会处理单一分支上的数据。

## 1.5 PySpark类库

类库：一堆别人写好的代码，你可以导入进行使用。Pandas就是Python的库。

框架：可以独立运行，并提供编程结构的一种软件产品。Spark就是一个独立的框架。

现在开始学的不是Python的代码类库而是独立可运行的框架，只不过提供了Python API。Pandas用于小规模数据集的处理；Spark用于大规模数据集的处理。无论大小数据集，都有合适的技术栈去处理，这样我们称之为合格的全栈数据开发工程师。

我们前面使用过`bin/pyspark`程序, 要注意, 这个只是一个应用程序, 提供一个Python解释器执行环境来运行Spark任务。我们现在说的PySpark，指的是Python的运行类库，是可以在Python代码中`import pyspark`。

PySpark 是Spark官方提供的一个Python类库。内置了完全的Spark API，可以通过PySpark类库来编写Spark应用程序，并将其提交到Spark集群中运行。

```sh
# linux三台服务器中安装一下类库
(base) [root@node2 ~]# pip install pyspark -i https://pypi.tuna.tsinghua.edu.cn/simple
```

## 1.6 本机开发环境搭建

**Hadoop DDL(Windows系统)**

1. 将课程资料中提供的: hadoop-3.3.0 文件, 复制到一个地方, 比如E:\hadoop
2. 将文件夹内bin内的hadoop.dll复制到: C:\Windows\System32里面去
3. 配置HADOOP_HOME环境变量指向 hadoop-3.3.0文件夹的路径。`HADOOP_HOME=E:\hadoop\hadoop-3.3.0`

配置这些的原因是：hadoop设计用于linux运行, 我们写spark的时候 在windows上开发 不可避免的会用到部分hadoop功能，为了避免在windows上报错, 我们给windows打补丁。

**PyCharm配置Python解释器-配置远程SSH Linux解释器**

现在我们来配置Linux远程的解释器，PySpark支持在Windows上执行，但是会有性能问题以及一些小bug，在Linux上执行是完美和高效的。所以，我们也可以配置好Linux上的远程解释器，来运行Python Spark代码。

设置远程SSH python pySpark 环境 settings->python Interpreter 之后连接就行了。当然，在此之前虚拟机需要启动。

## 1.7 WordCount代码实战

**应用入口：SparkContext**

Spark Application程序入口为：SparkContext，任何一个应用首先需要构建SparkContext对象，如下两步构建

1. 第一步、创建SparkConf对象。设置Spark Application基本信息，比如应用的名称AppName和应用运行Master
2. 第二步、基于SparkConf对象，创建SparkContext对象

文档地址：http://spark.apache.org/docs/3.1.2/rdd-programming-guide.html

**WordCount代码实战**

本地准备文件word.txt，上传到HDFS系统上面。

```sh
(base) [root@node1 linxuan_me]# hadoop fs -put word.txt /input
(base) [root@node1 linxuan_me]# hadoop fs -ls /input
Found 2 items
-rw-r--r--   3 root supergroup         45 2022-11-12 15:09 /input/1.txt
-rw-r--r--   3 root supergroup         47 2022-12-28 19:34 /input/word.txt
(base) [root@node1 linxuan_me]# hadoop fs -cat /input/word.txt
hello you Spark Flink
hello me hello she Spark
```

```txt
hello you Spark Flink
hello me hello she Spark
```

PySpark代码

```sh
from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    # 获取SparkConf对象，通过其构建SparkContext对象
    conf = SparkConf().setAppName("WordCountHelloWorld").setMaster("local[*]")
    sc = SparkContext(conf=conf)

    # 读取HDFS文件系统上面的words.txt文件，对其内部单词统计出现的数量
    # 读取文件
    file_rdd = sc.textFile("hdfs://node1:8020/input/word.txt")

    # 将单词进行切割，得到一个存储全部单词的集合对象
    words_rdd = file_rdd.flatMap(lambda line: line.split(" "))

    # 将单词转为元组对象，key是单词，value是数字1
    words_with_one_rdd = words_rdd.map(lambda x: (x, 1))

    # 将元组的value按照key来分组，堆所有的value执行聚合操作
    result_rdd = words_with_one_rdd.reduceByKey(lambda a, b: a + b)

    # 通过collect方法收集RDD数据打印输出结果
    print(result_rdd.collect())

```

```python
from pyspark import SparkConf, SparkContext
import os

# 选择本地PySpark环境执行Spark代码，那么就需要通过os配置一下环境变量
os.environ['PYSPARK_PYTHON'] = "E:\\Python\\Python38\\python.exe"

if __name__ == '__main__':
    # 获取SparkConf对象，通过其构建SparkContext对象
    conf = SparkConf().setAppName("WordCountHelloWorld").setMaster("local[*]")
    sc = SparkContext(conf=conf)

    # 读取HDFS文件系统上面的words.txt文件，对其内部单词统计出现的数量
    # 读取文件
    # 读取HDFS文件系统上的
    # file_rdd = sc.textFile("hdfs://node1:8020/input/word.txt")
    # 读取本地系统上面的，如果运行的服务器是Linux那么代表该文件也需要在Linux中存在
    file_rdd = sc.textFile("../data/input/word.txt")

    # 将单词进行切割，得到一个存储全部单词的集合对象
    words_rdd = file_rdd.flatMap(lambda line: line.split(" "))

    # 将单词转为元组对象，key是单词，value是数字1
    words_with_one_rdd = words_rdd.map(lambda x: (x, 1))

    # 将元组的value按照key来分组，堆所有的value执行聚合操作
    result_rdd = words_with_one_rdd.reduceByKey(lambda a, b: a + b)

    # 通过collect方法收集RDD数据打印输出结果
    print(result_rdd.collect())
```

![](..\图片\6-04【Spark】\1-7.png)

将文件修改一下提交到Linux服务器上面使用standalone模式运行

```sh
from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    # 获取SparkConf对象，通过其构建SparkContext对象
    conf = SparkConf().setAppName("WordCountHelloWorld")
    sc = SparkContext(conf=conf)

    # 读取HDFS文件系统上面的words.txt文件，对其内部单词统计出现的数量
    # 读取文件
    # 读取HDFS文件系统上的
    file_rdd = sc.textFile("hdfs://node1:8020/input/word.txt")
    # 读取本地系统上面的，如果运行的服务器是Linux那么代表该文件也需要在Linux中存在
    # file_rdd = sc.textFile("../data/input/word.txt")

    # 将单词进行切割，得到一个存储全部单词的集合对象
    words_rdd = file_rdd.flatMap(lambda line: line.split(" "))

    # 将单词转为元组对象，key是单词，value是数字1
    words_with_one_rdd = words_rdd.map(lambda x: (x, 1))

    # 将元组的value按照key来分组，堆所有的value执行聚合操作
    result_rdd = words_with_one_rdd.reduceByKey(lambda a, b: a + b)

    # 通过collect方法收集RDD数据打印输出结果
    print(result_rdd.collect())
```

```sh
# 使用本地模式提交代码到Spark运行
(base) [root@node1 demo01]# /export/server/spark-3.2.0-bin-hadoop3.2/bin/spark-submit --master local[*] /root/linxuan/demo01/HelloWorld.py 
22/12/29 10:59:25 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
[('hello', 3), ('you', 1), ('Spark', 2), ('Flink', 1), ('me', 1), ('she', 1)]
```

```sh
# 使用standalone模式提交代码到Spark运行，集群模式一定要在HDFS系统上面读取文件，否则读取不到
(base) [root@node1 demo01]# /export/server/spark-3.2.0-bin-hadoop3.2/bin/spark-submit --master spark://node1:7077 /root/linxuan/demo01/HelloWorld.py         
22/12/29 11:01:03 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
[('Spark', 2), ('Flink', 1), ('hello', 3), ('you', 1), ('me', 1), ('she', 1)]
```

## 1.8 常用网址

**Web  UI页面**

- HDFS集群：http://namenode_host:9870 其中namenode_host是namenode运行所在机器的主机名或者ip 如果使用主机名访问，别忘了在Windows配置hosts。我们的就是http://192.168.88.151:9870/
- YARN集群：http://resourcemanager_host:8088 其中resourcemanager_host是resourcemanager运行所在机器的主机名或者ip 如果使用主机名访问，别忘了在Windows配置hosts。http://192.168.88.151:8088/

# 第二章 RDD算子

分布式计算需要：分区控制、Shuffle控制、数据存储\序列化\发送、数据计算API、等一系列功能。这些功能不能简单的通过Python内置的本地集合对象(如 List\ 字典等)去完成，我们在分布式框架中需要有一个统一的数据抽象对象, 来实现上述分布式计算所需功能，这个抽象对象就是RDD。

RDD（Resilient Distributed Dataset）叫做弹性分布式数据集，是Spark中最基本的数据抽象，代表一个不可变、可
分区、里面的元素可并行计算的集合。

- Resilient：RDD中的数据可以存储在内存中或者磁盘中。
- Distributed：RDD中的数据是分布式存储的，可用于分布式计算。
- Dataset：一个数据集合，用于存放数据的。

![](..\图片\6-04【Spark】\2-1.png)

RDD（Resilient Distributed Dataset）弹性分布式数据集，是Spark中最基本的数据抽象，代表一个不可变、可分区、里面的元素可并行计算的集合。所有的运算以及操作都建立在 RDD 数据结构的基础之上。可以认为RDD是分布式的列表List或数组Array，抽象的数据结构，RDD是一个抽象类Abstract Class和泛型Generic Type。

## 2.1 RDD五大特性

RDD 数据结构内部有五个特性（摘录RDD 源码）：前三个特征每个RDD都具备的，后两个特征可选的。

1. a list of partiotioner。RDD是有分区的。一份RDD文件，本质上是分隔成了多个分区。

2. a function for partiotioner。RDD方法会作用在其所有分区上面。

3. a list of dependencies on other RDDs。RDD之间有依赖关系（血缘关系）。

4. Optionally, a Partitioner for key-value RDDs (e.g. to say that the RDD is hash-partitioned)。 key-value型的RDD可以有分区器。

5. Optionally, a list of preferred locations to compute each split on (e.g. block locations for an HDFS file)。RDD的分区规划，会尽量靠近数据所在的服务器。


# 第三章 Spark编程基础-Scala

启动HDFS，启动Spark，启动spark-shell

```sh
(base) [root@node1 bin]# start-all.sh  # 启动HDFS
(base) [root@node1 bin]# /export/server/spark-3.2.0-bin-hadoop3.2/sbin/start-all.sh # 启动Spark
(base) [root@node1 bin]# /export/server/spark-3.2.0-bin-hadoop3.2/bin/spark-shell # 启动spark-shell
```

```sh
(base) [root@node1 bin]# spark-shell
23/01/01 16:34:17 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
Spark context Web UI available at http://node1:4040
Spark context available as 'sc' (master = local[*], app id = local-1672562061130).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 3.2.0
      /_/
         
Using Scala version 2.12.15 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_144)
Type in expressions to have them evaluated.
Type :help for more information.

scala> 
```

设置日志级别：

```sh
scala> sc.setLogLevel("WARN") # 或者修改/export/server/spark-3.2.0-bin-hadoop3.2/con/log4j.properties
scala> sc.setLogLevel("INFO")
```

## 3.1 创建RDD

```sh
# 启动会创建一个SparkContext对象sc，但是注意一个SparkContext对象只能存在一个，如果我们要创建一个 那么要停止, 否则会出错 org.apache.spark.SparkException: Only one SparkContext should be running in this JVM (see SPARK-2243).The currently running SparkContext was created at:
Spark context available as 'sc' (master = local[*], app id = local-1672562061130).

# 关闭对象
scala> sc.stop

# 创建默认分区的设置对象
scala> var new_conf = sc.getConf.set("spark.default.parallelism", "3")
new_conf: org.apache.spark.SparkConf = org.apache.spark.SparkConf@156eb4cf

# 导入SparkContext
scala> import org.apache.spark.SparkContext
import org.apache.spark.SparkContext

# 根据配置对象创建SparkContext对象，这样以后RDD默认分区都是配置的了，就是3个
scala> var sc = new SparkContext(new_conf)
sc: org.apache.spark.SparkContext = org.apache.spark.SparkContext@9df7d7d
```

在Spark中创建RDD的创建方式大概可以分为如下三种：

1. 从集合中创建RDD。`parallelize()`:通过parallelize函数把一般数据结构加载为RDD。

   ```sh
   parallelize[T: ClassTag](seq: Seq[T],numSlices: Int = defaultParallelism): RDD[T]
   ```

   ```sh
   scala> var list = List("a", "b", "c", "d", "e");
   list: List[String] = List(a, b, c, d, e)
   
   scala> var rdd1 = sc.parallelize(list)
   rdd1: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[1] at parallelize at <console>:24
   
   # 查看分区数
   scala> rdd1.partitions.size
   res1: Int = 1
   
   scala> var rdd1 = sc.parallelize(list, 3) # 设置默认分区
   rdd1: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[2] at parallelize at <console>:24
   
   scala> rdd1.partitions.size
   res2: Int = 3
   ```

2. 从外部存储创建RDD。通过textFile直接加载数据文件为RDD。

   ```sh
   textFile(path: String, minPartitions: Int = defaultMinPartitions): RDD[String]
   ```

   ```sh
   # 从HDFS文件系统读取文件
   scala> var hdfsData = sc.textFile("hdfs://node1:8020/input/word.txt")
   res1: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/word.txt MapPartitionsRDD[3] at textFile at <console>:24
   
   scala> hdfsData.take(1)
   res0: Array[String] = Array(hello you Spark Flink)  
   
   # 本地读取文件
   scala> sc.textFile("/root/tmp_data/word.txt")
   res2: org.apache.spark.rdd.RDD[String] = /root/tmp_data/word.txt MapPartitionsRDD[5] at textFile at <console>:24
   ```

3. 从其他RDD创建 

## 3.2 transformation算子

**map(func)**

map: 将原来RDD的每个数据项通过map中的用户自定义函数f转换成一个新的RDD，map操作不会改变RDD的分区数目。

![](..\图片\6-04【Spark】\3-1.png)

```scala
scala> var data = sc.parallelize(List(1, 2, 3, 4))
data: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[2] at parallelize at <console>:23

// 查看所有元素
scala> data.collect()
res3: Array[Int] = Array(1, 2, 3, 4)

// 返回三条数据
scala> data.take(3)
res4: Array[Int] = Array(1, 2, 3)

scala> var square = data.map(x => x * x)
square: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[3] at map at <console>:23

scala> square.collect()
res1: Array[Int] = Array(1, 4, 9, 16)
```

**flatMap(func)**

flatMap：对集合中的每个元素进行map操作再扁平化。

![](..\图片\6-04【Spark】\3-2.png)

```scala
scala> var data = sc.parallelize(List("I am learning Spark", "I like Spark"))
data: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[4] at parallelize at <console>:23

// 使用map处理
scala> data.map(x => x.split(" "))
res5: org.apache.spark.rdd.RDD[Array[String]] = MapPartitionsRDD[5] at map at <console>:24

scala> res5.collect()
res8: Array[Array[String]] = Array(Array(I, am, learning, Spark), Array(I, like, Spark))

// 使用flatMap分割单词 最后会扁平化处理 
scala> data.flatMap(x => x.split(" "))
res9: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[6] at flatMap at <console>:24

scala> res9.collect()
res10: Array[String] = Array(I, am, learning, Spark, I, like, Spark)
```

**mapPartitions(func)**

和map功能类似，但是输入的元素是整个分区，即传入函数的操作对象是每个分区的Iterator集合，该操作不会导致Partitions数量的变化。

![](..\图片\6-04【Spark】\3-3.png)

```scala
// 创建一个1~10集合
scala> var rdd = sc.parallelize(1 to 10)
rdd: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[7] at parallelize at <console>:23

scala> rdd.collect()
res11: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

// 查看每个分区的数据
scala> rdd.glom().collect()
res12: Array[Array[Int]] = Array(Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

// 获取每个分区中大于3的值
scala> var mapPartitionsRDD = rdd.mapPartitions(iter => iter.filter(x => x > 3))
mapPartitionsRDD: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[9] at mapPartitions at <console>:23

scala> mapPartitionsRDD.collect()
res15: Array[Int] = Array(4, 5, 6, 7, 8, 9, 10)

// 获取每个分区中大于4的值 简洁写法
scala> var mapPartitionsRDD = rdd.mapPartitions(iter => iter.filter(_ > 4))
mapPartitionsRDD: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[10] at mapPartitions at <console>:23

scala> mapPartitionsRDD.collect()
res16: Array[Int] = Array(5, 6, 7, 8, 9, 10)
```

**sortBy(f:(T) => K， ascending， numPartitions)**

是可以对标准RDD进行排序。`sortBy()`可接受如下三个参数。

- `f:(T) => K`：左边是要被排序对象中的每一个元素，右边返回的值是元素中要进行排序的值。
- `ascending`：决定排序后RDD中的元素是升序还是降序，默认是true，也就是升序，false为降序排序。
- `numPartitions`：该参数决定排序后的RDD的分区个数，默认排序后的分区个数和排序之前的个数相等。

```scala
scala> var data = sc.parallelize(List((1, 3), (45, 3), (7, 6)))
data: org.apache.spark.rdd.RDD[(Int, Int)] = ParallelCollectionRDD[11] at parallelize at <console>:23

// 按照元素第二个值降序排序
scala> var sort_data = data.sortBy(x => x._2, false, 1)
sort_data: org.apache.spark.rdd.RDD[(Int, Int)] = MapPartitionsRDD[14] at sortBy at <console>:23

scala> sort_data.collect()
res17: Array[(Int, Int)] = Array((7,6), (1,3), (45,3))
```

**filter(func)**

保留通过函数func，返回值为true的元素，组成新的RDD。

```scala
scala> var data = sc.parallelize(List(1, 2, 3, 4))
data: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[17] at parallelize at <console>:23

scala> var result = data.filter(x => x > 2)
result: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[18] at filter at <console>:23

scala> result.collect()
res17: Array[Int] = Array(3, 4)
```

**distinct([numPartitions]))**

针对RDD中重复的元素，只保留一个元素。

```scala
scala> var data = sc.parallelize(List(1, 2, 2, 3, 3, 4))
data: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[19] at parallelize at <console>:23

scala> data.distinct.collect()
res18: Array[Int] = Array(4, 1, 3, 2)
```

**union(otherDataset)**

合并RDD，需要保证两个RDD元素类型一致。

```scala
scala> var data1 = sc.parallelize(List(1, 2, 3))
data1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[23] at parallelize at <console>:23

scala> var data2 = sc.parallelize(List(4, 5, 6))
data2: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[24] at parallelize at <console>:23

scala> data1.union(data2)
res20: org.apache.spark.rdd.RDD[Int] = UnionRDD[25] at union at <console>:25

scala> res20.collect()
res21: Array[Int] = Array(1, 2, 3, 4, 5, 6)
```

**intersection(otherDataset)**

找出两个RDD的共同元素，也就是找出两个RDD的交集

```scala
scala> var data1 = sc.parallelize(List(1, 2, 3, 4))
data1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[26] at parallelize at <console>:23

scala> var data2 = sc.parallelize(List(3, 4, 5, 6))
data2: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[27] at parallelize at <console>:23

scala> data1.intersection(data2).collect()
res22: Array[Int] = Array(4, 3)
```

**subtract (otherDataset)**

获取两个RDD之间的差集。

```scala
scala> var data1 = sc.parallelize(List(1, 2, 3, 4))
data1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[26] at parallelize at <console>:23

scala> var data2 = sc.parallelize(List(3, 4, 5, 6))
data2: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[27] at parallelize at <console>:23

scala> data1.subtract(data2).collect()
res23: Array[Int] = Array(1, 2)

scala> data2.subtract(data1).collect()
res24: Array[Int] = Array(5, 6)
```

**cartesian(otherDataset)**

笛卡尔积就是将两个集合的元素两两组合成一组。

```scala
scala> var data1 = sc.parallelize(List(1, 2, 3, 4))
data1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[26] at parallelize at <console>:23

scala> var data2 = sc.parallelize(List(3, 4, 5, 6))
data2: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[27] at parallelize at <console>:23

scala> data1.cartesian(data2).collect()
res25: Array[(Int, Int)] = Array((1,3), (1,4), (1,5), (1,6), (2,3), (2,4), (2,5), (2,6), (3,3), (3,4), (3,5), (3,6), (4,3), (4,4), (4,5), (4,6))
```

## 3.3 键值对RDD

虽然大部分Spark的RDD操作都支持所有种类的单值RDD，但是有少部分特殊的操作只能作用于键值对类型的RDD。

顾名思义，键值对RDD由一组组的键值对组成，这些RDD被称为PairRDD。PairRDD提供了并行操作各个键或跨节点重新进行数据分组的操作接口。例如，PairRDD提供了reduceByKey()方法，可以分别规约每个键对应的数据，还有join()方法，可以把两个RDD中键相同的元素组合在一起，合并为一个RDD。、

将一个普通的RDD转化为一个PairRDD时可以使用map函数来进行操作，传递的函数需要返回键值对。

```scala
scala> var rdd = sc.parallelize(List("a", "b", "c"))
rdd: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[53] at parallelize at <console>:23

scala> var rdd2 = rdd.map(x => (x, 1))
rdd2: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[54] at map at <console>:23

scala> rdd2.collect()
res16: Array[(String, Int)] = Array((a,1), (b,1), (c,1))
```

键值对类型的RDD，包含了键跟值两个部分。Spark提供了两个方法分别获取键值对RDD的键跟值。keys 返回一个仅包含键的RDD，values返回一个仅包含值的RDD。

```scala
scala> rdd2.keys.collect()
res17: Array[String] = Array(a, b, c)

scala> rdd2.values.collect()
res18: Array[Int] = Array(1, 1, 1)
```

**mapValues(func)**

类似map，针对键值对（Key，Value）类型的数据中的Value进行map操作，而不对Key进行处理。

![](..\图片\6-04【Spark】\3-4.png)

```scala
scala> rdd2.mapValues(x => (x, 1))
res19: org.apache.spark.rdd.RDD[(String, (Int, Int))] = MapPartitionsRDD[57] at mapValues at <console>:24

scala> res19.collect()
res20: Array[(String, (Int, Int))] = Array((a,(1,1)), (b,(1,1)), (c,(1,1)))
```

**groupByKey([numPartitions])**

按键分组，在（K，V）对组成的RDD上调用时，返回（K，Iterable）对组成的新的RDD。

```scala
scala> sc.parallelize(List("a", "b", "c", "c")).map(x => (x, 1))
res21: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[59] at map at <console>:24

scala> res21.groupByKey().collect()
res22: Array[(String, Iterable[Int])] = Array((a,CompactBuffer(1)), (b,CompactBuffer(1)), (c,CompactBuffer(1, 1)))
```

**reduceByKey(func, [numPartitions])**

将键值对RDD按键分组后进行聚合。当在（K，V）类型的键值对组成的RDD上调用时，返回一个（K，V）类型键值对组成的新RDD。其中新RDD每个键的值使用给定的reduce函数func进行聚合，该函数必须是（V，V）=>V类型

```scala
scala> sc.parallelize(List("a", "b", "c", "c")).map(x => (x, 1))
res16: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[21] at map at <console>:24

scala> res16.collect()
res17: Array[(String, Int)] = Array((a,1), (b,1), (c,1), (c,1))

scala> res16.reduceByKey((x, y) => x + y).collect()
res23: Array[(String, Int)] = Array((a,1), (b,1), (c,2))
```

**join(otherDataset, [numPartitions])**

把键值对数据相同键的值整合起来。

![](..\图片\6-04【Spark】\3-5.png)

```scala

scala> var rdd1 = sc.parallelize(List(("k1", "v1"), ("k2", "v2"), ("k3", "v3")))
rdd1: org.apache.spark.rdd.RDD[(String, String)] = ParallelCollectionRDD[62] at parallelize at <console>:23

scala> var rdd2 = sc.parallelize(List(("k1", "w1"), ("k2", "w2")))
rdd2: org.apache.spark.rdd.RDD[(String, String)] = ParallelCollectionRDD[63] at parallelize at <console>:23

scala> var rdd3 = rdd1.join(rdd2)
rdd3: org.apache.spark.rdd.RDD[(String, (String, String))] = MapPartitionsRDD[66] at join at <console>:24

scala> rdd3.collect()
res24: Array[(String, (String, String))] = Array((k2,(v2,w2)), (k1,(v1,w1)))
```

## 3.4 Action类型算子

**lookup(key: K)**

作用于（K,V）类型的RDD上，返回指定K的所有V值。

```scala
scala> var rdd = sc.parallelize(List("a", "b", "c"))
rdd: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[67] at parallelize at <console>:23

scala>  var rdd2 = rdd.map(x => (x, 1))
rdd2: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[68] at map at <console>:23

scala> rdd2.collect()
res26: Array[(String, Int)] = Array((a,1), (b,1), (c,1))

scala> rdd2.lookup("c")
res28: Seq[Int] = WrappedArray(1)
```

collect()：返回RDD中所有的元素。

take(num)：返回RDD前面num条记录。

count()：计算RDD中所有元素个数。

## 3.5 文件读取与存储

| 格式名称     | 结构化 | 描述                                     |
| ------------ | ------ | ---------------------------------------- |
| 文本文件     | 否     | 普通的文本文件，每一行一条记录。         |
| SequenceFile | 是     | 一种用于键值对数据的常见Hadoop文件格式。 |

`saveAsTextFile(path: String)`：把RDD保存到HDFS中。

`repartition(numPartitions: Int)`：可以增加或减少此RDD中的并行级别。在内部，它使用shuffle重新分发数据。如果要减少此RDD中的分区数，请考虑使用coalesce，这样可以避免执行shuffle。

`saveAsSequenceFile(path)`：将数据集的元素作为Hadoop SequenceFile编写，只支持键值对RDD。

`sequenceFile[K, V](path: String, keyClass: Class[K], valueClass: Class[V], minPartitions: Int)`：读取序列化文件

## 3.6 练习题

```apl
# result_bigdata.txt
1001    大数据基础      90
1002    大数据基础      94
1003    大数据基础      100
1004    大数据基础      99
1005    大数据基础      90
1006    大数据基础      94
1007    大数据基础      100
1008    大数据基础      93
1009    大数据基础      89
1010    大数据基础      78
1011    大数据基础      91
1012    大数据基础      84
```

```apl
# result_math.txt
1001    应用数学        96
1002    应用数学        94
1003    应用数学        100
1004    应用数学        100
1005    应用数学        94
1006    应用数学        80
1007    应用数学        90
1008    应用数学        94
1009    应用数学        84
1010    应用数学        86
1011    应用数学        79
1012    应用数学        91
```

```apl
# student.txt
1001    李正明
1002    王一磊
1003    陈志华
1004    张永丽
1005    赵信
1006    古明远
1007    刘浩明
1008    沈彬
1009    李子琪
1010    王嘉栋
1011    柳梦文
1012    钱多多
```

第一题：取出成绩排名前5的学生成绩信息。

```scala
scala> var bigdata = sc.textFile("hdfs://node1:8020/input/result_bigdata.txt")
bigdata: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/result_bigdata.txt MapPartitionsRDD[7] at textFile at <console>:23

scala> bigdata.collect()
res4: Array[String] = Array(1001        大数据基础      90, 1002        大数据基础      94, 1003        大数据基础      100, 1004   大数据基础      99, 1005        大数据基础      90, 1006        大数据基础      94, 1007        大数据基础      100, 1008   大数据基础      93, 1009        大数据基础      89, 1010        大数据基础      78, 1011        大数据基础      91, 1012    大数据基础      84)

scala> var math = sc.textFile("hdfs://node1:8020/input/result_math.txt")
math: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/result_math.txt MapPartitionsRDD[9] at textFile at <console>:23

scala> math.collect()
res5: Array[String] = Array(1001        应用数学        96, 1002        应用数学        94, 1003        应用数学        100, 1004   应用数学        100, 1005       应用数学        94, 1006        应用数学        80, 1007        应用数学        90, 1008    应用数学        94, 1009        应用数学        84, 1010        应用数学        86, 1011        应用数学        79, 1012    应用数学        91)


scala> bigdata.collect()
res10: Array[String] = Array(1001       大数据基础      90, 1002        大数据基础      94, 1003        大数据基础      100, 1004   大数据基础      99, 1005        大数据基础      90, 1006        大数据基础      94, 1007        大数据基础      100, 1008   大数据基础      93, 1009        大数据基础      89, 1010        大数据基础      78, 1011        大数据基础      91, 1012    大数据基础      84)

scala> var bigDataMap = bigdata.map(x => x.split("\t"))
bigDataMap: org.apache.spark.rdd.RDD[Array[String]] = MapPartitionsRDD[7] at map at <console>:23

scala> bigDataMap.collect()
res12: Array[Array[String]] = Array(Array(1001, 大数据基础, 90), Array(1002, 大数据基础, 94), Array(1003, 大数据基础, 100), Array(1004, 大数据基础, 99), Array(1005, 大数据基础, 90), Array(1006, 大数据基础, 94), Array(1007, 大数据基础, 100), Array(1008, 大数据基础, 93), Array(1009, 大数据基础, 89), Array(1010, 大数据基础, 78), Array(1011, 大数据基础, 91), Array(1012, 大数据基础, 84))

// 因为要根据第三个数来排序，所以需要转为Int类型，然后排序，去除前5个
scala> bigDataMap.map(x => (x(0), x(1), x(2).toInt)).sortBy(x => x._3, false).take(5)
res16: Array[(String, String, Int)] = Array((1003,大数据基础,100), (1007,大数据基础,100), (1004,大数据基础,99), (1002,大数据基础,94), (1006,大数据基础,94))
```

第二题：找出考试成绩得过100分的学生ID，最终的结果需要集合到一个RDD中。

```scala
// 首先根据制表符拆分，然后过滤出来100分的，最后收集
scala> bigdata.map(x => x.split("\t")).map(x => (x(0), x(2).toInt)).filter(x => x._2 == 100).map(x => x._1).collect()
res12: Array[String] = Array(1003, 1007)

// 获取数学100分学籍号
scala> math.map(x => x.split("\t")).map(x => (x(0), x(2).toInt)).filter(x => x._2 == 100).map(x => x._1).collect()
res13: Array[String] = Array(1003, 1004)

scala> var bigdataMap = bigdata.map(x => x.split("\t")).map(x => (x(0), x(2).toInt)).filter(x => x._2 == 100).map(x => x._1) 
bigdataMap: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[34] at map at <console>:23

scala> var mathMap = math.map(x => x.split("\t")).map(x => (x(0), x(2).toInt)).filter(x => x._2 == 100).map(x => x._1)
mathMap: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[38] at map at <console>:23

// 合并并去重
scala> bigdataMap.union(mathMap).distinct.collect()
res14: Array[String] = Array(1003, 1007, 1004)        
```

第三题：找出两门成绩都得100分的学生ID，结果汇总为一个RDD。

```scala
// 获取RDD
scala> var grade_two = bigdataMap.intersection(mathMap)
grade_two: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[52] at intersection at <console>:24

scala> grade_two.collect()
res15: Array[String] = Array(1003)
```

第四题：输出每位学生的总成绩，要求将两个成绩表中学生ID相同的成绩相加。

```scala
scala> var bigdata = sc.textFile("hdfs://node1:8020/input/result_bigdata.txt")
bigdata: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/result_bigdata.txt MapPartitionsRDD[4] at textFile at <console>:23

scala> var math = sc.textFile("hdfs://node1:8020/input/result_math.txt")
math: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/result_math.txt MapPartitionsRDD[6] at textFile at <console>:23

scala> var bigdata_kv = bigdata.map(x => x.split("\t")).map(x => (x(0), x(2).toInt))
bigdata_kv: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[8] at map at <console>:23

scala> var math_kv = math.map(x => x.split("\t")).map(x => (x(0), x(2).toInt))
math_kv: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[7] at map at <console>:23

scala> var score_kv = bigdata_kv.union(math_kv)
score_kv: org.apache.spark.rdd.RDD[(String, Int)] = UnionRDD[15] at union at <console>:24

// 或者使用：score_kv.reduceByKey(_+_).collect()
scala> var all_score = score_kv.reduceByKey((x, y) => x + y)
allscore: org.apache.spark.rdd.RDD[(String, Int)] = ShuffledRDD[14] at reduceByKey at <console>:23

scala> all_score.collect()
res4: Array[(String, Int)] = Array((1005,184), (1012,175), (1001,186), (1009,173), (1010,164), (1003,200), (1007,190), (1008,187), (1002,188), (1011,170), (1004,199), (1006,174))
```

第五题：输出每位学生的平均成绩，要求将两个成绩表中学生ID相同的成绩相加并计算出平均分。

```scala
// 这样也可以 但是如果有一个成绩表有该学生ID 另一个成绩表没有该学生ID的话　会出错
scala> var avg_score = score_kv.reduceByKey((x, y) => x + y).map(x => (x._1, x._2 / 2))
res3: Array[(String, Int)] = Array((1005,92), (1012,87), (1001,93), (1009,86), (1010,82), (1003,100), (1007,95), (1008,93), (1002,94), (1011,85), (1004,99), (1006,87))
```

```scala
// 设置每一个科目都考过一次试 最后相加 除以这个次数就可以了
scala> var score_kv_count = score_kv.mapValues(x => (x, 1))
score_kv_count: org.apache.spark.rdd.RDD[(String, (Int, Int))] = MapPartitionsRDD[16] at mapValues at <console>:23

scala> score_kv_count.collect()
res6: Array[(String, (Int, Int))] = Array((1001,(90,1)), (1002,(94,1)), (1003,(100,1)), (1004,(99,1)), (1005,(90,1)), (1006,(94,1)), (1007,(100,1)), (1008,(93,1)), (1009,(89,1)), (1010,(78,1)), (1011,(91,1)), (1012,(84,1)), (1001,(96,1)), (1002,(94,1)), (1003,(100,1)), (1004,(100,1)), (1005,(94,1)), (1006,(80,1)), (1007,(90,1)), (1008,(94,1)), (1009,(84,1)), (1010,(86,1)), (1011,(79,1)), (1012,(91,1)))

// 根据键相同然后在一起做运算 x是键相同的一个 y是键相同的另一个 都相加。然后除法
scala> var avg_score = score_kv_count.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2)).mapValues(x => x._1 / x._2)
avg_score: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[18] at mapValues at <console>:23

scala> avg_score.collect()
res7: Array[(String, Int)] = Array((1005,92), (1012,87), (1001,93), (1009,86), (1010,82), (1003,100), (1007,95), (1008,93), (1002,94), (1011,85), (1004,99), (1006,87))
```

第六题：合并每个学生的总成绩和平均成绩。

```scala
// join:把键值对数据相同键的值整合起来。
scala> all_score.join(avg_score).collect()
res8: Array[(String, (Int, Int))] = Array((1005,(184,92)), (1012,(175,87)), (1001,(186,93)), (1009,(173,86)), (1010,(164,82)), (1003,(200,100)), (1007,(190,95)), (1008,(187,93)), (1002,(188,94)), (1011,(170,85)), (1004,(199,99)), (1006,(174,87)))
```

第七题：汇总学生成绩并以文本格式存储在HDFS上，数据汇总为学生ID，姓名，总分，平均分。

```scala
scala> var student = sc.textFile("hdfs://node1:8020/input/student.txt")
student: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/student.txt MapPartitionsRDD[13] at textFile at <console>:23

scala> student.take(1)
res1: Array[String] = Array(1001        李正明)

scala> var student_kv = student.map(x => x.split("\t"))
student_kv: org.apache.spark.rdd.RDD[Array[String]] = MapPartitionsRDD[14] at map at <console>:23

scala> student_kv.collect()
res2: Array[Array[String]] = Array(Array(1001, 李正明), Array(1002, 王一磊), Array(1003, 陈志华), Array(1004, 张永丽), Array(1005, 赵信), Array(1006, 古明远), Array(1007, 刘浩明), Array(1008, 沈彬), Array(1009, 李子琪), Array(1010, 王嘉栋), Array(1011, 柳梦文), Array(1012, 钱多多))

scala> student_kv.map(x => (x(0), x(1))).collect()
res4: Array[(String, String)] = Array((1001,李正明), (1002,王一磊), (1003,陈志华), (1004,张永丽), (1005,赵信), (1006,古明远), (1007,刘浩明), (1008,沈彬), (1009,李子琪), (1010,王嘉栋), (1011,柳梦文), (1012,钱多多))

// 根据键相同 合并到一起
scala> all_score.join(avg_score).join(student_kv.map(x => (x(0), x(1))))
res13: org.apache.spark.rdd.RDD[(String, ((Int, Int), String))] = MapPartitionsRDD[29] at join at <console>:26

// 查看一下
scala> res13.collect()
res14: Array[(String, ((Int, Int), String))] = Array((1005,((184,92),赵信)), (1012,((175,87),钱多多)), (1001,((186,93),李正明)), (1009,((173,86),李子琪)), (1010,((164,82),王嘉栋)), (1003,((200,100),陈志华)), (1007,((190,95),刘浩明)), (1008,((187,93),沈彬)), (1002,((188,94),王一磊)), (1011,((170,85),柳梦文)), (1004,((199,99),张永丽)), (1006,((174,87),古明远)))

// 没有用
scala> res13.mapValues(x => (x._1, x._2)).take(1)
res17: Array[(String, ((Int, Int), String))] = Array((1005,((184,92),赵信)))

// 用一些其他的方法
scala> var save_data = res13.mapValues{case (x, y) => (x._1, x._2, y)}
save_data: org.apache.spark.rdd.RDD[(String, (Int, Int, String))] = MapPartitionsRDD[31] at mapValues at <console>:23

scala> save_data.take(1)
res18: Array[(String, (Int, Int, String))] = Array((1005,(184,92,赵信)))

// 再摘出去
scala> var save_data = res13.mapValues{case (x, y) => (x._1, x._2, y)}.map{case (x, y) => (x, y._1, y._2, y._3)}
save_data: org.apache.spark.rdd.RDD[(String, Int, Int, String)] = MapPartitionsRDD[33] at map at <console>:23

scala> save_data.take(1)
res19: Array[(String, Int, Int, String)] = Array((1005,184,92,赵信))

// b保存一下 HDFS文件系统中
scala> save_data.saveAsTextFile("/input/student_grade")

// 设置只用一个分区
scala> save_data.coalesce(1).saveAsTextFile("/input/student_grade.txt")
```

第八题：基于3个基站的日志数据，要求计算某个手机号码在一天之内出现时间最多的两个地点。

模拟了一些简单的日志数据，共4个字段：手机号码，时间戳，基站id，连接类型（1表示建立连接，0表示断开连接）。

```apl
# StationA
18688888888,20160327082400,16030401EAFB68F1E3CDF819735E1C66,1
18611132889,20160327082500,16030401EAFB68F1E3CDF819735E1C66,1
18688888888,20160327170000,16030401EAFB68F1E3CDF819735E1C66,0
18611132889,20160327180000,16030401EAFB68F1E3CDF819735E1C66,0
```

```apl
# StationB 
18611132889,20160327075000,9F36407EAD0629FC166F14DDE7970F68,1
18688888888,20160327075100,9F36407EAD0629FC166F14DDE7970F68,1
18611132889,20160327081000,9F36407EAD0629FC166F14DDE7970F68,0
18688888888,20160327081300,9F36407EAD0629FC166F14DDE7970F68,0
18688888888,20160327175000,9F36407EAD0629FC166F14DDE7970F68,1
18611132889,20160327182000,9F36407EAD0629FC166F14DDE7970F68,1
18688888888,20160327220000,9F36407EAD0629FC166F14DDE7970F68,0
18611132889,20160327230000,9F36407EAD0629FC166F14DDE7970F68,0
```

```apl
# StationC
18611132889,20160327081100,CC0710CC94ECC657A8561DE549D940E0,1
18688888888,20160327081200,CC0710CC94ECC657A8561DE549D940E0,1
18688888888,20160327081900,CC0710CC94ECC657A8561DE549D940E0,0
18611132889,20160327082000,CC0710CC94ECC657A8561DE549D940E0,0
18688888888,20160327171000,CC0710CC94ECC657A8561DE549D940E0,1
18688888888,20160327171600,CC0710CC94ECC657A8561DE549D940E0,0
18611132889,20160327180500,CC0710CC94ECC657A8561DE549D940E0,1
18611132889,20160327181500,CC0710CC94ECC657A8561DE549D940E0,0
```

```Scala

scala> var StationA = sc.textFile("hdfs://node1:8020/input/StationA.txt")
StationA: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/StationA.txt MapPartitionsRDD[1] at textFile at <console>:23

scala> var StationB = sc.textFile("hdfs://node1:8020/input/StationB.txt")
StationB: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/StationB.txt MapPartitionsRDD[3] at textFile at <console>:23

scala> var StationC = sc.textFile("hdfs://node1:8020/input/StationC.txt")
StationC: org.apache.spark.rdd.RDD[String] = hdfs://node1:8020/input/StationC.txt MapPartitionsRDD[5] at textFile at <console>:23

scala> var sA = StationA.map(x => x.split(",")).map(x => (x(0), x(1).toLong, x(2), x(3)))
sA: org.apache.spark.rdd.RDD[(String, Long, String, String)] = MapPartitionsRDD[7] at map at <console>:23


scala> var sB = StationB.map(x => x.split(",")).map(x => (x(0), x(1).toLong, x(2), x(3)))
sB: org.apache.spark.rdd.RDD[(String, Long, String, String)] = MapPartitionsRDD[9] at map at <console>:23

scala> var sC = StationC.map(x => x.split(",")).map(x => (x(0), x(1).toLong, x(2), x(3)))
sC: org.apache.spark.rdd.RDD[(String, Long, String, String)] = MapPartitionsRDD[11] at map at <console>:23

// 文件A和文件B连接起来
scala> var station = sA.union(sB).union(sC)
station: org.apache.spark.rdd.RDD[(String, Long, String, String)] = UnionRDD[13] at union at <console>:25

scala> station.count()
res4: Long = 20

// 将用户手机号和基站ID作为键 连接状态为1的转为复数 相加就是相减
scala> station.map{x => if(x._4 == "1") ((x._1, x._3), -x._2) else ((x._1, x._3), x._2)}.take(1)
res5: Array[((String, String), Long)] = Array(((18688888888,16030401EAFB68F1E3CDF819735E1C66),-20160327082400))

// 相同的键做相加操作
scala> var data = station.map{x => if(x._4 == "1") ((x._1, x._3), -x._2) else ((x._1, x._3), x._2)}.reduceByKey(_+_)
data: org.apache.spark.rdd.RDD[((String, String), Long)] = ShuffledRDD[16] at reduceByKey at <console>:23

// 得到时间差
scala> data.take(1)
res6: Array[((String, String), Long)] = Array(((18688888888,CC0710CC94ECC657A8561DE549D940E0),1300))

// 转一下键值对 这样手机号作为键 然后ID和时间差作为值
scala> data.map(x => (x._1._1, (x._1._2, x._2))).take(1)
res7: Array[(String, (String, Long))] = Array((18688888888,(CC0710CC94ECC657A8561DE549D940E0,1300)))

// 按照键分组
scala> data.map(x => (x._1._1, (x._1._2, x._2))).groupByKey().collect()
res8: Array[(String, Iterable[(String, Long)])] = Array((18688888888,CompactBuffer((CC0710CC94ECC657A8561DE549D940E0,1300), (9F36407EAD0629FC166F14DDE7970F68,51200), (16030401EAFB68F1E3CDF819735E1C66,87600))), (18611132889,CompactBuffer((9F36407EAD0629FC166F14DDE7970F68,54000), (CC0710CC94ECC657A8561DE549D940E0,1900), (16030401EAFB68F1E3CDF819735E1C66,97500))))

// 分组之后转为list之后排序，排序之后反转一下这样降序，之后取出来前两个。
scala> data.map(x => (x._1._1, (x._1._2, x._2))).groupByKey().mapValues{x => var line = x.toList.sortBy(y => y._2).reverse.take(2);line}.take(3)
res12: Array[(String, List[(String, Long)])] = Array((18688888888,List((16030401EAFB68F1E3CDF819735E1C66,87600), (9F36407EAD0629FC166F14DDE7970F68,51200))), (18611132889,List((16030401EAFB68F1E3CDF819735E1C66,97500), (9F36407EAD0629FC166F14DDE7970F68,54000))))

// 只有值的时候将值里面的第一个数取出来就可以
scala> data.map(x => (x._1._1, (x._1._2, x._2))).groupByKey().mapValues{x => var line = x.toList.sortBy(y => y._2).reverse.take(2).map(z => z._1);line}.take(3)
res13: Array[(String, List[String])] = Array((18688888888,List(16030401EAFB68F1E3CDF819735E1C66, 9F36407EAD0629FC166F14DDE7970F68)), (18611132889,List(16030401EAFB68F1E3CDF819735E1C66, 9F36407EAD0629FC166F14DDE7970F68)))


scala> var data2 = data.map(x => (x._1._1, (x._1._2, x._2))).groupByKey().mapValues{x => var line = x.toList.sortBy(y => y._2).reverse.take(2).map(z => z._1);line}
data2: org.apache.spark.rdd.RDD[(String, List[String])] = MapPartitionsRDD[28] at mapValues at <console>:23

scala> data2.collect()
res14: Array[(String, List[String])] = Array((18688888888,List(16030401EAFB68F1E3CDF819735E1C66, 9F36407EAD0629FC166F14DDE7970F68)), (18611132889,List(16030401EAFB68F1E3CDF819735E1C66, 9F36407EAD0629FC166F14DDE7970F68)))
```
