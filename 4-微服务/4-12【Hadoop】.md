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

![](D:\Java\笔记\图片\4-12【Hadoop】\1-1.png)

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

“ ” : , ( )	

最早提出“大数据”时代到来的是全球知名咨询公司麦肯锡,其称:“数据,已经渗透到当今每一个行业和业务职能领域,成为重要的生产因素。人们对于海量数据的挖掘和运用,预示着新一波生产率增长和消费者盈余浪潮的到来。”2019年,央视推出了国内首部大数据产业题材纪录片《大数据时代》,节目细致而生动地讲述了大数据技术在政府治理、民生服务、数据安全、工业转型、未来生活等方面给我们带来的改变和影响。

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

![](D:\Java\笔记\图片\4-12【Hadoop】\1-2.jpg)

**特性优点**

- 可靠性（reliability）：能自动维护数据的多份复制，并且在任务失败后能自动地重新部署（redeploy）计算任务。所以Hadoop的按位存储和处理数据的能力值得人们信赖。
- 效率高（efficiency）：通过并发数据，Hadoop可以在节点之间动态并行的移动数据，使得速度非常快。
- 成本低（Economical）：Hadoop集群允许通过部署普通廉价的机器组成集群来处理大数据，以至于成本很低。看重的是集群整体能力。
- 扩容能力（scalability）：Hadoop是在可用的计算机集群间分配数据并完成计算任务的，这些集群可方便灵活的方式扩展到数以千计的节点。

**Hadoop发行版本**

一共有两个版本：开源社区版和商业发行版。

* 开源社区版：Apache开源社区发行、也是官方发行版本。Apache开源社区版本http://hadoop.apache.org/。我们使用的是Apache版的Hadoop，版本号为：3.3.0
  * 优点：更新迭代快
  * 缺点：兼容稳定性不周
* 商业发行版：商业公司发行、基于Apache开源协议、某些服务需要收费。商业发行版本，Cloudera：https://www.cloudera.com/products/open-source/apache-hadoop.html。Hortonworks ：https://www.cloudera.com/products/hdp.html。
  * 优点：稳定兼容好
  * 缺点：收费 版本更新慢

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

Hadoop集群包括两个集群：HDFS集群、YARN集群。两个集群逻辑上分离，通常物理上在一起，互相之间没有依赖、互不影响。两个集群都是标准的主从架构集群。MapReduce是计算框架、代码层面的组件 没有集群之说。

- HDFS集群 (分布式存储)：主角色NameNode、从角色DataNode、主角色辅助角色SecondaryNameNode。
- YARN集群 (资源管理、调度)：主角色ResourceManage、从角色NodeManager。

![](D:\Java\笔记\图片\4-12【Hadoop】\2-1.png)

安装包、源码包下载地址：https://archive.apache.org/dist/hadoop/common/hadoop-3.3.0/

需要重新编译Hadoop源码：匹配不同操作系统本地库环境，Hadoop某些操作比如压缩、IO需要调用系统本地库（*`.so`|*`.dll`）。修改源码、重构源码

编译Hadoop方式：源码包根目录下文件：BUILDING.txt

详细步骤参考附件资料

课程提供编译好的Hadoop安装包：hadoop-3.3.0-Centos7-64-with-snappy.tar.gz



Hadoop是在Linux上面安装的，一共提供了三台虚拟机让我们搭建集群：node1、node2、node3。想要在这三台虚拟机上面搭建Hadoop集群，那么我们首先需要做一些准备工作。这些准备工作每台机器都要做：

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
   	
   	export JAVA_HOME=/export/server/jdk1.8.0_241
   	export PATH=$PATH:$JAVA_HOME/bin
   	export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
   	
   	#重新加载环境变量文件
   	source /etc/profile
   ```

   

