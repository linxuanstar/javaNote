# 第六章 数据仓库与Apache Hive

数据仓库（英语：Data Warehouse，简称数仓、DW）,是一个用于存储、分析、报告的数据系统。数据仓库的目的是构建面向分析的集成化数据环境，分析结果为企业提供决策支持（Decision Support）。

数据仓库本身并不“生产”任何数据，其数据来源于不同外部系统；同时数据仓库自身也不需要“消费”任何的数据，其结果开放给各个外部应用使用；这也是为什么叫“仓库”，而不叫“工厂”的原因。

![](..\图片\5-14【Hive数据仓】\6-1.png)

下面我们来分析一下数据仓库为何而来，解决什么问题的？

- 中国人寿保险（集团）公司下辖多条业务线，包括：人寿险、财险、车险，养老险等。各业务线的业务正常运营需要记录维护包括客户、保单、收付费、核保、理赔等信息。这么多业务数据存储在哪里呢？
- 联机事务处理系统（OLTP）正好可以满足上述业务需求开展, 其主要任务是执行联机事务处理。其基本特征是前台接收的用户数据可以立即传送到后台进行处理，并在很短的时间内给出处理结果。
  关系型数据库（RDBMS）是OLTP典型应用，比如：Oracle、MySQL、SQL Server等。
- 随着集团业务的持续运营，业务数据将会越来越多。由此也产生出许多运营相关的困惑：能够确定哪些险种正在恶化或已成为不良险种？能够用有效的方式制定新增和续保的政策吗？理赔过程有欺诈的可能吗？现在得到的报表是否只是某条业务线的？集团整体层面数据如何？
- 为了能够正确认识这些问题，制定相关的解决措施。最稳妥办法就是基于业务数据开展数据分析，基于分析的结果给决策提供支撑。也就是所谓的数据驱动决策的制定。
- OLTP环境开展分析可行吗？可以，但是没必要。
  OLTP系统的核心是面向业务，支持业务，支持事务。所有的业务操作可以分为读、写两种操作，一般来说读的压力明显大于写的压力。
  如果在OLTP环境直接开展各种分析，有以下问题需要考虑：数据分析也是对数据进行读取操作，会让读取压力倍增；OLTP仅存储数周或数月的数据；数据分散在不同系统不同表中，字段类型属性不统一。
- 当分析所涉及数据规模较小的时候，在业务低峰期时可以在OLTP系统上开展直接分析。但为了更好的进行各种规模的数据分析，同时也不影响OLTP系统运行，此时需要构建一个集成统一的数据分析平台。该平台的目的很简单：面向分析，支持分析，并且和OLTP系统解耦合。基于这种需求，数据仓库的雏形开始在企业中出现了。

如数仓定义所说,数仓是一个用于存储、分析、报告的数据系统，目的是构建面向分析的集成化数据环境。我们把这种面向分析、支持分析的系统称之为OLAP（联机分析处理）系统。当然，数据仓库是OLAP系统的一种实现。

中国人寿保险公司就可以基于分析决策需求，构建数仓平台。

![](..\图片\5-14【Hive数据仓】\6-2.png)

## 6.1 数据仓库特征

数仓主要特征：

* 面向主题性（Subject-Oriented）：主题是一个抽象的概念，是较高层次上数据综合、归类并进行分析利用的抽象
* 集成性（Integrated）：主题相关的数据通常会分布在多个操作型系统中，彼此分散、独立、异构。需要集成到数仓主题下。
* 非易失性、非异变性（Non-Volatile）：数据仓库是分析数据的平台，而不是创造数据的平台。
* 时变性（Time-Variant）：数据仓库的数据需要随着时间更新，以适应决策的需要。

**面向主题性（Subject-Oriented）**

主题是一个抽象的概念，是较高层次上企业信息系统中的数据综合、归类并进行分析利用的抽象。在逻辑意义上，
它是对应企业中某一宏观分析领域所涉及的分析对象。

传统OLTP系统对数据的划分并不适用于决策分析。而基于主题组织的数据则不同，它们被划分为各自独立的领域
，每个领域有各自的逻辑内涵但互不交叉，在抽象层次上对数据进行完整、一致和准确的描述。

**集成性（Integrated）**

主题相关的数据通常会分布在多个操作型系统中，彼此分散、独立、异构。

因此在数据进入数据仓库之前，必然要经过统一与综合，对数据进行抽取、清理、转换和汇总，这一步是数据仓库
建设中最关键、最复杂的一步，所要完成的工作有：

* 要统一源数据中所有矛盾之处：如字段的同名异义、异名同义、单位不统一、字长不一致等等。

* 进行数据综合和计算：数据仓库中的数据综合工作可以在从原有数据库抽取数据时生成，但许多是在数据仓库内部生成的，即进入数据仓库以后进行综合生成的。

**非易失性、非异变性（Non-Volatile）**

数据仓库是分析数据的平台，而不是创造数据的平台。我们是通过数仓去分析数据中的规律，而不是去创造修改其
中的规律。因此数据进入数据仓库后，它便稳定且不会改变。

数据仓库的数据反映的是一段相当长的时间内历史数据的内容，数据仓库的用户对数据的操作大多是数据查询或比
较复杂的挖掘，一旦数据进入数据仓库以后，一般情况下被较长时间保留。数据仓库中一般有大量的查询操作，但修改和删除操作很少。

**时变性（Time-Variant）**

数据仓库包含各种粒度的历史数据，数据可能与某个特定日期、星期、月份、季度或者年份有关。当业务变化后会失去时效性。因此数据仓库的数据需要随着时间更新，以适应决策的需要。从这个角度讲，数据仓库建设是一个项目，更是一个过程 。

## 6.2 数据仓库主流开发语言--SQL

结构化查询语言（Structured Query Language）简称SQL，是一种数据库查询和程序设计语言，用于存取数据以及查询、更新和管理数据。SQL编程语言，应该称之为分析领域主流开发语言。

虽然SQL语言本身是针对数据库软件设计的，但是在数据仓库领域，尤其是大数据数仓领域，很多数仓软件都会去支持SQL语法；原因在于一是用户学习SQL成本低，二是SQL语言对于数据分析真的十分友好，爱不释手。

结构化数据：

- 结构化数据也称作行数据，是由二维表结构来逻辑表达和实现的数据，严格地遵循数据格式与长度规范，主要通过关系型数据库进行存储和管理。
- 与结构化数据相对的是不适于由数据库二维表来表现的非结构化数据，包括所有格式的办公文档、XML、HTML、各类报表、图片和音频、视频信息等。
- 通俗来说，结构化数据会有严格的行列对齐，便于解读与理解。

具体的看MySQL篇章：《2-01【MySQL基础】.md》

## 6.3 Apache Hive概述

Apache Hive是一款建立在Hadoop之上的开源数据仓库系统，可以将存储在Hadoop文件中的结构化、半结构化数据文件映射为一张数据库表，基于表提供了一种类似SQL的查询模型，称为Hive查询语言（HQL），用于访问和分析存储在Hadoop文件中的大型数据集。

Hive核心是将HQL转换为MapReduce程序，然后将程序提交到Hadoop群集执行。Hive由Facebook实现并开源。

Hive和Hadoop关系：

- 从功能来说，数据仓库软件，至少需要具备下述两种能力：存储数据的能力、分析数据的能力
- Apache Hive作为一款大数据时代的数据仓库软件，当然也具备上述两种能力。只不过Hive并不是自己实现了上述两种能力，而是借助Hadoop。Hive利用HDFS存储数据，利用MapReduce查询分析数据。
- 这样突然发现Hive没啥用，不过是套壳Hadoop罢了。其实不然，Hive的最大的魅力在于用户专注于编写HQL，Hive帮我们转换成为MapReduce程序完成对数据的分析。

Hive架构图：

![](..\图片\5-14【Hive数据仓】\6-3.png)

Hive组件：

* 用户接口：包括 CLI、JDBC/ODBC、WebGUI。其中，CLI(command line interface)为shell命令行；Hive中的Thrift服务器允许外部客户端通过网络与Hive进行交互，类似于JDBC或ODBC协议。WebGUI是通过浏览器访问Hive。
* 元数据存储：通常是存储在关系数据库如 mysql/derby中。Hive 中的元数据包括表的名字，表的列和分区及其属性，表的属性（是否为外部表等），表的数据所在目录等。
* Driver驱动程序，包括语法解析器、计划编译器、优化器、执行：完成 HQL 查询语句从词法分析、语法分析、编译、优化以及查询计划的生成。生成的查询计划存储在 HDFS 中，并在随后有执行引擎调用执行。
* 执行引擎：Hive本身并不直接处理数据文件。而是通过执行引擎处理。当下Hive支持MapReduce、Tez、Spark3种执行引擎。

## 6.4 Apache Hive元数据

元数据（Metadata），又称中介数据、中继数据，为描述数据的数据（data about data），主要是描述数据属性（property）的信息，用来支持如指示存储位置、历史数据、资源查找、文件记录等功能。

Hive Metadata即Hive的元数据。包含用Hive创建的database、table、表的位置、类型、属性，字段顺序类型等元信息。元数据存储在关系型数据库中。如hive内置的Derby、或者第三方如MySQL等。

Metastore即元数据服务。Metastore服务的作用是管理metadata元数据，对外暴露服务地址，让各种客户端通过连接metastore服务，由metastore再去连接MySQL数据库来存取元数据。有了metastore服务，就可以有多个客户端同时连接，而且这些客户端不需要知道MySQL数据库的用户名和密码，只需要连接metastore 服务即可。某种程度上也保证了hive元数据的安全。

![](..\图片\5-14【Hive数据仓】\6-4.png)

metastore服务配置有3种模式：内嵌模式、本地模式、远程模式。

|                         | 内嵌模式 | 本地模式 | 远程模式 |
| ----------------------- | -------- | -------- | -------- |
| Metastore单独配置、启动 | 否       | 否       | 是       |
| Metadata存储介质        | Derby    | MySQL    | MySQL    |

在生产环境中，建议用远程模式来配置Hive Metastore。在这种情况下，其他依赖hive的软件都可以通过Metastore访问hive。由于还可以完全屏蔽数据库层，因此这也带来了更好的可管理性/安全性。本系列课程中使用企业推荐模式--远程模式部署。

![](..\图片\5-14【Hive数据仓】\6-5.png)

## 6.5 Apache Hive部署实战

由于Apache Hive是一款基于Hadoop的数据仓库软件，通常部署运行在Linux系统之上。因此不管使用何种方式
配置Hive Metastore，必须要先保证服务器的基础环境正常，Hadoop集群健康可用。

服务器基础环境：集群时间同步、防火墙关闭、主机Host映射、免密登录、JDK安装

Hadoop集群健康可用：启动Hive之前必须先启动Hadoop集群。特别要注意，需等待HDFS安全模式关闭之后再启动运行Hive。Hive不是分布式安装运行的软件，其分布式的特性主要借由Hadoop完成。包括分布式存储、分布式计算。

因为Hive需要把数据存储在HDFS上，并且通过MapReduce作为执行引擎处理数据；因此需要在Hadoop中添加相关配置属性，以满足Hive在Hadoop上运行。修改Hadoop中`core-site.xml`，并且Hadoop集群同步配置文件，重启生效。

```xml
<!-- 这里我们之前搭建Hadoop集群的时候已经做过设置了 不需要动 -->
<!-- 整合hive 用户代理设置 -->
<property>
    <name>hadoop.proxyuser.root.hosts</name>
    <value>*</value>
</property>

<property>
    <name>hadoop.proxyuser.root.groups</name>
    <value>*</value>
</property>
```

搭建Hive之前需要安装MySQL，MySQL只需要在一台机器安装并且需要授权远程访问。具体安装教程可以看一下Linux的篇章：《2-31【Linux】.md》。

Hive只需要在一台机器安装，接下来部署Hive：

- 上传安装包 解压

  ```sh
  [root@node1 server]# pwd
  /export/server
  [root@node1 server]# tar -zxvf apache-hive-3.1.2-bin.tar.gz 
  ```

- 解决Hive与Hadoop之间guava版本差异

  ```shell
  cd /export/server/apache-hive-3.1.2-bin/
  rm -rf lib/guava-19.0.jar
  cp /export/server/hadoop-3.3.0/share/hadoop/common/lib/guava-27.0-jre.jar ./lib/
  ```

- 修改配置文件

  `hive-env.sh`

  ```shell
  cd /export/server/apache-hive-3.1.2-bin/conf
  mv hive-env.sh.template hive-env.sh
  
  vim hive-env.sh
  export HADOOP_HOME=/export/server/hadoop-3.3.0
  export HIVE_CONF_DIR=/export/server/apache-hive-3.1.2-bin/conf
  export HIVE_AUX_JARS_PATH=/export/server/apache-hive-3.1.2-bin/lib
  ```

  `hive-site.xml`

  ```shell
  vim hive-site.xml
  ```

  ```xml
  <configuration>
  <!-- 存储元数据mysql相关配置 -->
  <property>
  	<name>javax.jdo.option.ConnectionURL</name>
  	<value>jdbc:mysql://node1:3306/hive3?createDatabaseIfNotExist=true&amp;useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8</value>
  </property>
  
  <property>
  	<name>javax.jdo.option.ConnectionDriverName</name>
  	<value>com.mysql.jdbc.Driver</value>
  </property>
  
  <property>
  	<name>javax.jdo.option.ConnectionUserName</name>
  	<value>root</value>
  </property>
  
  <property>
  	<name>javax.jdo.option.ConnectionPassword</name>
  	<value>hadoop</value>
  </property>
  
  <!-- H2S运行绑定host -->
  <property>
      <name>hive.server2.thrift.bind.host</name>
      <value>node1</value>
  </property>
  
  <!-- 远程模式部署metastore metastore地址 -->
  <property>
      <name>hive.metastore.uris</name>
      <value>thrift://node1:9083</value>
  </property>
  
  <!-- 关闭元数据存储授权  --> 
  <property>
      <name>hive.metastore.event.db.notification.api.auth</name>
      <value>false</value>
  </property>
  </configuration>
  
  ```

- 上传mysql jdbc驱动到hive安装包lib下：`/export/server/apache-hive-3.1.2-bin/lib`

  ```
  mysql-connector-java-5.1.32.jar
  ```

- 初始化元数据

  ```shell
  cd /export/server/apache-hive-3.1.2-bin/
  
  bin/schematool -initSchema -dbType mysql -verbos
  #初始化成功会在mysql中创建74张表
  ```

- 在hdfs创建hive存储目录（如存在则不用操作）

  ```shell
  hadoop fs -mkdir /tmp
  hadoop fs -mkdir -p /user/hive/warehouse
  hadoop fs -chmod g+w /tmp
  hadoop fs -chmod g+w /user/hive/warehouse
  ```

- ==启动hive==

  启动metastore服务

  ```shell
  #前台启动  关闭ctrl+c
  /export/server/apache-hive-3.1.2-bin/bin/hive --service metastore
  
  #前台启动开启debug日志
  /export/server/apache-hive-3.1.2-bin/bin/hive --service metastore --hiveconf hive.root.logger=DEBUG,console  
  
  #后台启动 进程挂起  关闭使用jps+ kill -9
  nohup /export/server/apache-hive-3.1.2-bin/bin/hive --service metastore &
  ```

**Apache Hive客户端使用**

Hive自带客户端：bin/hive、bin/beeline

![](..\图片\5-14【Hive数据仓】\6-5.png)

Hive发展至今，总共历经了两代客户端工具。

- 第一代客户端（deprecated不推荐使用）：`$HIVE_HOME/bin/hive`, 是一个 shellUtil。主要功能：一是可用于以交互或批处理模式运行Hive查询；二是用于Hive相关服务的启动，比如metastore服务。
- 第二代客户端（recommended 推荐使用）：`$HIVE_HOME/bin/beeline`，是一个JDBC客户端，是官方强烈推荐使用的Hive命令行工具，和第一代客户端相比，性能加强安全性提高。

远程模式下beeline通过 Thrift 连接到单独的HiveServer2服务上，这也是官方推荐在生产环境中使用的模式。HiveServer2支持多客户端的并发和身份认证，旨在为开放API客户端如JDBC、ODBC提供更好的支持。

HiveServer2通过Metastore服务读写元数据。所以在远程模式下，启动HiveServer2之前必须先首先启动metastore服务。远程模式下，Beeline客户端只能通过HiveServer2服务访问Hive。而bin/hive是通过Metastore服务访问的。具体关系如上图。

* 在hive安装的服务器上，首先启动metastore服务，然后启动hiveserver2服务。

  ```sh
  # 后台启动 进程挂起  关闭使用jps+ kill -9
  nohup /export/server/apache-hive-3.1.2-bin/bin/hive --service metastore &
  
  # 启动metasore之后过一段时间再启动hiveserver2
  # 注意 启动hiveserver2需要一定的时间  不要启动之后立即beeline连接 可能连接不上
  nohup /export/server/apache-hive-3.1.2-bin/bin/hive --service hiveserver2 &
  ```

* 拷贝node1安装包到beeline客户端机器上（node3）

  ```sh
  scp -r /export/server/apache-hive-3.1.2-bin/ root@node3:/export/server/
  ```

* 在node3上使用been客户端进行连接访问

  ```sh
  [root@node3 server]# /export/server/apache-hive-3.1.2-bin/bin/hive
  hive> show databases;
  OK
  default
  Time taken: 0.882 seconds, Fetched: 1 row(s)
  hive> show tables;
  OK
  Time taken: 0.182 seconds
  ```

* 在node3上使用beeline客户端进行连接访问。需要注意hiveserver2服务启动之后需要稍等一会才可以对外提供服务。Beeline是JDBC的客户端，通过JDBC协议和Hiveserver2服务进行通信，协议的地址是：`jdbc:hive2://node1:10000`

  ```sh
  [root@node3 server]# /export/server/apache-hive-3.1.2-bin/bin/beeline
  beeline> ! connect jdbc:hive2://node1:10000
  beeline> root
  beeline> 直接回车
  ```


## 6.6 Hive DDL

Hive SQL（HQL）与标准SQL的语法大同小异，基本相通；

**数据库与建库**

在Hive中，默认的数据库叫做default，存储数据位置位于HDFS的`/user/hive/warehouse`下。用户自己创建的数据库存储位置是`/user/hive/warehouse/database_name.db`下。

* `create database`：创建数据库。

  ```sql
  CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_name
  [COMMENT database_comment]
  [LOCATION hdfs_path]
  [WITH DBPROPERTIES (property_name=property_value, ...)];
  ```

  ```sql
  create database if not exists linxuan
  comment "this is my first db"
  with dbproperties ('createdBy'='Allen');
  ```

  `create database`用于创建新的数据库

  `COMMENT`：数据库的注释说明语句

  `LOCATION`：指定数据库在HDFS存储位置，默认`/user/hive/warehouse/dbname.db`。如果需要使用`location`指定路径的时候，最好指向的是一个新创建的空文件夹。

  `WITH DBPROPERTIES`：用于指定一些数据库的属性配置。

* `use database`：选择特定的数据库，切换当前会话使用哪一个数据库进行操作。

* `drop database`：删除数据库，默认行为是RESTRICT，这意味着仅在数据库为空时才删除它。要删除带有表的数据库（不为空的数据库），我们可以使用CASCADE。

  ```sql
  DROP (DATABASE|SCHEMA) [IF EXISTS] database_name [RESTRICT|CASCADE];
  ```

**表与建表**

建表语法树（基础）：

```sql
CREATE TABLE [IF NOT EXISTS] [db_name.]table_name
(col_name data_type [COMMENT col_comment], ... )
[COMMENT table_comment]
[ROW FORMAT DELIMITED …];
```

`[ ]`中括号的语法表示可选。建表语句中的语法顺序要和语法树中顺序保持一致。

* 数据类型：Hive数据类型指的是表中列的字段类型；
  整体分为两类：原生数据类型（primitive data type）和复杂数据类型（complex data type）。最常用的数据类型是字符串String和数字类型Int。

  ![](..\图片\5-14【Hive数据仓】\6-6.png)

* 分隔符指定语法：`ROW FORMAT DELIMITED`语法用于指定字段之间等相关的分隔符，这样Hive才能正确的读取解析数据。或者说只有分隔符指定正确，解析数据成功，我们才能在表中看到数据。

  `LazySimpleSerDe`是Hive默认的，包含4种子语法，分别用于指定字段之间、集合元素之间、map映射 kv之间、换行的分隔符号。在建表的时候可以根据数据的特点灵活搭配使用。

  ![](..\图片\5-14【Hive数据仓】\6-7.png)

  Hive建表时如果没有`row format`语法指定分隔符，则采用默认分隔符；默认的分割符是`'\001'`，是一种特殊的字符，使用的是ASCII编码的值，键盘是打不出来的。在vim编辑器中，连续按下`Ctrl+v/Ctrl+a`即可输入`'\001'` ，显示`^A`。在一些文本编辑器中将以SOH的形式显示

  ![](..\图片\5-14【Hive数据仓】\6-8.png)

**SHOW语法**

```sql
-- 1、显示所有数据库 SCHEMAS和DATABASES的用法 功能一样
show databases;
show schemas;

-- 2、显示当前数据库所有表
show tables;
SHOW TABLES [IN database_name]; -- 指定某个数据库

-- 3、查询显示一张表的元数据信息
desc formatted t_team_ace_player;
```

**数据类型、分隔符练习**

文件`archer.txt`中记录了手游《王者荣耀》射手的相关信息，包括生命、物防、物攻等属性信息，其中字段之间分隔符为制表符`\t,`要求在Hive中建表映射成功该文件。

```txt
1	后羿	5986	1784	396	336	remotely	archer
2	马可波罗	5584	200	362	344	remotely	archer
3	鲁班七号	5989	1756	400	323	remotely	archer
4	李元芳	5725	1770	396	340	remotely	archer
5	孙尚香	6014	1756	411	346	remotely	archer
6	黄忠	5898	1784	403	319	remotely	archer
7	狄仁杰	5710	1770	376	338	remotely	archer
8	虞姬	5669	1770	407	329	remotely	archer
9	成吉思汗	5799	1742	394	329	remotely	archer
10	百里守约	5611	1784	410	329	remotely	archer	assassin
```

创建数据库及表：

```sql
-- 创建数据库并切换使用
create database if not exists linxuan;
-- 使用linxuan数据库
use linxuan;

-- ddl create table
create table t_archer(
    id int comment "ID",
    name string comment "英雄名称",
    hp_max int comment "最大生命",
    mp_max int comment "最大法力",
    attack_max int comment "最高物攻",
    defense_max int comment "最大物防",
    attack_range string comment "攻击范围",
    role_main string comment "主要定位",
    role_assist string comment "次要定位"
) comment "王者荣耀射手信息"
row format delimited
fields terminated by "\t";
```

建表成功之后，在Hive的默认存储路径下就生成了表对应的文件夹；把`archer.txt`文件上传到对应的表文件夹下。

```sh
# 执行命令把文件上传到HDFS表所对应的目录下
hadoop fs -put archer.txt /user/hive/warehouse/linxuan.db/t_archer
```

执行查询操作，可以看出数据已经映射成功。

**默认分隔符联系**

文件`team_ace_player.txt`中记录了手游《王者荣耀》主要战队内最受欢迎的王牌选手信息，字段之间使用的是`\001`作为分隔符,要求在Hive中建表映射成功该文件。

```txt
1成都AG超玩会一诺
2重庆QGhappyHurt
3DYG久诚
4上海EDG.M浪浪
5武汉eStarProCat
6RNG.M暴风锐
7RW侠渡劫
8TES滔搏迷神
9杭州LGD大鹅伪装
10南京Hero久竞清融
```

```sql
create table t_team_ace_player (
    id int,
    team_name string,
    ace_player_name string
);
```

```sh
# 执行命令把文件上传到HDFS表所对应的目录下
hadoop fs -put team_ace_player.txt /user/hive/warehouse/linxuan.db/t_team_ace_player
```

执行查询操作，可以看出数据已经映射成功。

## 6.7 注释中文乱码问题

上面创建表的时候用到了中文注释，但是我们查询表结构的时候注释并不会显示出来，乱码了：

```sql
desc formatted t_archer;
```

解决方式是在连接MySQL

```sql
--注意 下面sql语句是需要在MySQL中执行  修改Hive存储的元数据信息（metadata）
use hive3;
show tables;

alter table hive3.COLUMNS_V2 modify column COMMENT varchar(256) character set utf8;
alter table hive3.TABLE_PARAMS modify column PARAM_VALUE varchar(4000) character set utf8;
alter table hive3.PARTITION_PARAMS modify column PARAM_VALUE varchar(4000) character set utf8 ;
alter table hive3.PARTITION_KEYS modify column PKEY_COMMENT varchar(4000) character set utf8;
alter table hive3.INDEX_PARAMS modify column PARAM_VALUE varchar(4000) character set utf8;
```

## 6.8 Hive DML加载数据

这里只记录了一点与MySQL不同的笔记，对于查询的语句可以查看MySQL笔记：《2-01【MySQL基础】.md》。

在Hive中建表成功之后，就会在HDFS上创建一个与之对应的文件夹，且文件夹名字就是表名；文件夹父路径是由参数`hive.metastore.warehouse.dir`控制，默认值是`/user/hive/warehouse`；

不管路径在哪里，只有把数据文件移动到对应的表文件夹下面，Hive才能映射解析成功；最原始暴力的方式就是使用`hadoop fs –put|-mv`等方式直接将数据移动到表文件夹下；

**Load加载数据**

Hive官方推荐使用Load命令将数据加载到表中。Load英文单词的含义为：加载、装载；

- 所谓加载是指将数据文件移动到与Hive表对应的位置，移动时是纯复制、移动操作。
- 纯复制、移动指在数据load加载到表中时，Hive不会对表中的数据内容进行任何转换，任何操作。

在Hive客户端中操作，也就是beeline和DataGrip都可以。语句为：

```sql
LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename;
```

filepath：filepath表示待移动数据的路径。可以指向文件（在这种情况下，Hive将文件移动到表中），也可以指向目录（在这种情况下，Hive将把该目录中的所有文件移动到表中）。filepath文件路径支持下面三种形式，要结合LOCAL关键字一起考虑：

1. 相对路径，例如：`project/data1` 
2. 绝对路径，例如：`/user/hive/project/data1` 
3. 具有schema的完整URI，例如：`hdfs://namenode:9000/user/hive/project/data1`

LOCAL：指定LOCAL， 将在本地文件系统中查找文件路径。用户也可以为本地文件指定完整的URI-例如：`file:///user/hive/project/data1`。没有指定LOCAL关键字。如果filepath指向的是一个完整的URI，会直接使用这个URI；如果没有指定schema，Hive会使用在hadoop配置文件中参数fs.default.name指定的（不出意外，都是HDFS）
LOCAL本地：如果对HiveServer2服务运行此命令，本地文件系统指的是Hiveserver2服务所在机器的本地Linux文件系统，不是Hive客户端所在的本地文件系统。

![](..\图片\5-14【Hive数据仓】\6-9.png)

```sh
# 在node1下面创建一个students.txt
[root@node1 hivedata]# pwd
/root/hivedata

# 内容如下：
95001,李勇,男,20,CS
95002,刘晨,女,19,IS
95003,王敏,女,22,MA
95004,张立,男,19,IS
95005,刘刚,男,18,MA
95006,孙庆,男,23,CS
```

```sql
-- step1:建表
-- 建表student_local 用于演示从本地加载数据
create table student_local (
    num int,
    name string,
    sex string,
    age int,
    dept string
) 
row format delimited 
fields terminated by ',';

-- 建表student_HDFS 用于演示从HDFS加载数据
create table student_HDFS (
    num int,
    name string,
    sex string,
    age int,
    dept string
) 
row format delimited 
fields terminated by ',';
```

```sql
-- 建议使用beeline客户端 可以显示出加载过程日志信息
-- step2:加载数据
-- 从本地加载数据 数据位于HS2（node1）本地文件系统 本质是hadoop fs -put上传操作
LOAD DATA LOCAL INPATH '/root/hivedata/students.txt' INTO TABLE linxuan.student_local;

-- 从HDFS加载数据 数据位于HDFS文件系统根目录下 本质是hadoop fs -mv 移动操作 加载完毕之后/students.txt没了
-- 先把数据上传到HDFS上 hadoop fs -put /root/hivedata/students.txt /
LOAD DATA INPATH '/students.txt' INTO TABLE linxuan.student_HDFS;
```

**Insert插入数据**

Hive官方推荐加载数据的方式：清洗数据成为结构化文件，再使用Load语法加载数据到表中。这样的效率更高。也可以使用insert语法把数据插入到指定的表中，最常用的配合是把查询返回的结果插入到另一张表中。

insert+select表示：将后面查询返回的结果作为内容插入到指定表中。
1. 需要保证查询结果列的数目和需要插入数据表格的列数目一致。
2. 如果查询出来的数据类型和插入表格对应的列数据类型不一致，将会进行转换，但是不能保证转换一定成功，转换失败的数据将会为NULL。

```sql
INSERT INTO TABLE tablename select_statement1 FROM from_statement;
```

```sql
-- step1:创建一张源表student
drop table if exists student;
create table student(
    num int,          
    name string,
    sex string,
    age int,
    dept string
)
row format delimited
fields terminated by ',';

-- 加载数据
load data local inpath '/root/hivedata/students.txt' into table student;

-- step2：创建一张目标表 只有两个字段
create table student_from_insert(
    sno int,
    sname string
);

-- 使用insert+select插入数据到新表中
insert into table student_from_insert select num,name from student;

select * from student_insert1;
```

## 6.9 Hive 常用函数

Hive内建了不少函数，用于满足用户不同使用需求，提高SQL编写效率：
1. 使用`show functions`查看当下可用的所有函数；
2. 通过`describe function extended funcname`来查看函数的使用方式。

Hive的函数分为两大类：内置函数（Built-in Functions）、用户定义函数UDF（User-Defined Functions）

- 内置函数可分为：数值类型函数、日期类型函数、字符串类型函数、集合函数、条件函数等；

- 用户定义函数根据输入输出的行数可分为3类：UDF、UDAF、UDTF。

  - UDF（User-Defined-Function）普通函数，一进一出
  - UDAF（User-Defined Aggregation Function）聚合函数，多进一出
  - UDTF（User-Defined Table-Generating Functions）表生成函数，一进多出

  ![](..\图片\5-14【Hive数据仓】\6-10.png)

  UDF分类标准本来针对的是用户自己编写开发实现的函数，但是Hive觉得这样分类特别的好，所以UDF分类标准可以扩大到Hive的所有函数中：包括内置函数和用户自定义函数。

  因为不管是什么类型的函数，一定满足于输入输出的要求，那么从输入几行和输出几行上来划分没有任何问题。千万不要被UD（User-Defined）这两个字母所迷惑，照成视野的狭隘。

**Hive 常用的内置函数**

内置函数（build-in）指的是Hive开发实现好，直接可以使用的函数,也叫做内建函数。官方文档地址：https://cwiki.apache.org/confluence/display/Hive/LanguageManual+UDF。

内置函数根据应用归类整体可以分为8大种类型：

1. String Functions 字符串函数

   ```apl
   字符串长度函数：length
   字符串反转函数：reverse
   字符串连接函数：concat
   带分隔符字符串连接函数：concat_ws
   字符串截取函数：substr,substring
   ```

   ```sql
   -- ----------String Functions 字符串函数------------
   select length("linxuan");
   select reverse("linxuan");
   select concat("angela","baby");
   -- 带分隔符字符串连接函数：concat_ws(separator, [string | array(string)]+)
   select concat_ws('.', 'www', array('linxuan', 'com'));
   -- 字符串截取函数：substr(str, pos[, len]) 或者 substring(str, pos[, len])
   select substr("angelababy", -2); -- pos是从1开始的索引，如果为负数则倒着数
   select substr("angelababy", 2, 2);
   -- 分割字符串函数: split(str, regex)
   select split('apache hive', ' ');
   ```

2. Date Functions 日期函数

   ```sql
   -- --------- Date Functions 日期函数 -----------------
   -- 获取当前日期: current_date
   select current_date();
   -- 获取当前UNIX时间戳函数: unix_timestamp
   select unix_timestamp();
   -- 日期转UNIX时间戳函数: unix_timestamp
   select unix_timestamp("2011-12-07 13:01:03");
   -- 指定格式日期转UNIX时间戳函数: unix_timestamp
   select unix_timestamp('20111207 13:01:03','yyyyMMdd HH:mm:ss');
   -- UNIX时间戳转日期函数: from_unixtime
   select from_unixtime(1618238391);
   select from_unixtime(0, 'yyyy-MM-dd HH:mm:ss');
   -- 日期比较函数: datediff 日期格式要求'yyyy-MM-dd HH:mm:ss' or 'yyyy-MM-dd'
   select datediff('2012-12-08','2012-05-09');
   -- 日期增加函数: date_add
   select date_add('2012-02-28',10);
   -- 日期减少函数: date_sub
   select date_sub('2012-01-1',10);
   ```

3. Mathematical Functions 数学函数

   ```sql
   -- --Mathematical Functions 数学函数-------------
   -- 取整函数: round 返回double类型的整数值部分 （遵循四舍五入）
   select round(3.1415926);
   -- 指定精度取整函数: round(double a, int d) 返回指定精度d的double类型
   select round(3.1415926,4);
   -- 取随机数函数: rand 每次执行都不一样 返回一个0到1范围内的随机数
   select rand();
   -- 指定种子取随机数函数: rand(int seed) 得到一个稳定的随机数序列
   select rand(3);
   ```

4. Conditional Functions 条件函数

   主要用于条件判断、逻辑判断转换这样的场合

   ```sql
   -- ---Conditional Functions 条件函数------------------
   -- 使用之前课程创建好的student表数据
   select * from student limit 3;
   -- if条件判断: if(boolean testCondition, T valueTrue, T valueFalseOrNull)
   select if(1=2,100,200);
   select if(sex ='男','M','W') from student limit 3;
   -- 空值转换函数: nvl(T value, T default_value)
   select nvl("allen","itcast");
   select nvl(null,"itcast");
   -- 条件转换函数: CASE a WHEN b THEN c [WHEN d THEN e]* [ELSE f] END
   select case 100 when 50 then 'tom' when 100 then 'mary' else 'tim' end;
   select case sex when '男' then 'male' else 'female' end from student limit 3;
   ```

# 第七章 陌陌聊天数据分析

陌陌作为聊天平台每天都会有大量的用户在线，会出现大量的聊天数据，通过对聊天数据的统计分析，可以更好的对用户构建精准的用户画像，为用户提供更好的服务以及实现高ROI的平台运营推广，给公司的发展决策提供精确数据支撑。

基于Hadoop和Hive实现聊天数据统计分析，构建聊天数据分析报表。

数据内容如下：

- 数据大小：两个文件共14万条数据
- 列分隔符：制表符 `\t`
- 数据字典及样例数据

![](..\图片\5-14【Hive数据仓】\7-1.png)

## 7.1 建库建表&加载数据

**建库建表**

```sql
-- 如果数据库已存在就删除
drop database if exists db_msg cascade ;
-- 创建数据库
create database db_msg ;
-- 切换数据库
use db_msg ;
-- 列举数据库
show databases ;
```

```sql
-- 如果表已存在就删除
drop table if exists db_msg.tb_msg_source ;

-- 建表
-- 指定分隔符为制表符
create table db_msg.tb_msg_source(
    msg_time string comment "消息发送时间", 
    sender_name string comment "发送人昵称", 
    sender_account string comment "发送人账号", 
    sender_sex string comment "发送人性别", 
    sender_ip string comment "发送人ip地址", 
    sender_os string comment "发送人操作系统", 
    sender_phonetype string comment "发送人手机型号", 
    sender_network string comment "发送人网络类型", 
    sender_gps string comment "发送人的GPS定位", 
    receiver_name string comment "接收人昵称", 
    receiver_ip string comment "接收人IP", 
    receiver_account string comment "接收人账号", 
    receiver_os string comment "接收人操作系统", 
    receiver_phonetype string comment "接收人手机型号", 
    receiver_network string comment "接收人网络类型", 
    receiver_gps string comment "接收人的GPS定位", 
    receiver_sex string comment "接收人性别", 
    msg_type string comment "消息类型", 
    distance string comment "双方距离", 
    message string comment "消息内容"
)
row format delimited
fields terminated by '\t' ;
```

**加载数据**

HDFS上创建目录：`hdfs dfs -mkdir -p /momo/data`

上传到HDFS：`hdfs dfs -put /export/data/data1.tsv /momo/data/`、`hdfs dfs -put /export/data/data2.tsv /momo/data/`

加载到Hive表中：`load data inpath '/momo/data/data1.tsv' into table db_msg.tb_msg_source;`、`load data inpath '/momo/data/data2.tsv' into table db_msg.tb_msg_source;`

验证结果：`select msg_time, sender_name, sender_ip, sender_phonetype, receiver_name, receiver_network
from tb_msg_source
limit 10;`

## 7.2 ETL数据清洗

数据来源：聊天业务系统中导出的2021年11月01日一天24小时的用户聊天数据，以TSV文本形式存储在文件中。

这样就会导致一些问题：

1. 当前数据中，有一些数据的字段为空，不是合法数据

   ```sql
   -- 在客户端使用 beeline和datagrip都可以
   -- 使用数据库
   use db_msg;
   
   -- 查看sender_gps值的长度为0
   select msg_time, sender_name, sender_gps
   from tb_msg_source
   where  length(sender_gps) = 0
   limit 10;
   ```

   ```apl
   +----------------------+--------------+-------------+
   |       msg_time       | sender_name  | sender_gps  |
   +----------------------+--------------+-------------+
   | 2021-11-01 07:50:40  | 卯飞羽          |             |
   | 2021-11-01 07:50:39  | 逮黎明          |             |
   | 2021-11-01 07:29:30  | 谷谨           |             |
   | 2021-11-01 07:17:36  | 可嘉树          |             |
   | 2021-11-01 07:37:58  | 蛮炎彬          |             |
   | 2021-11-01 07:57:50  | 春楠           |             |
   | 2021-11-01 07:38:25  | 麻宏放          |             |
   | 2021-11-01 07:34:37  | 辛浩大          |             |
   | 2021-11-01 07:33:20  | 树项禹          |             |
   | 2021-11-01 07:21:12  | 藏珍           |             |
   +----------------------+--------------+-------------+
   ```

   对此我们可以使用where关键字过滤

2. 需求中，需要统计每天、每个小时的消息量，但是数据中没有天和小时字段，只有整体时间字段，不好处理

   ```sql
   select msg_time
   from db_msg.tb_msg_source
   limit 10;
   ```

   ```apl
   +----------------------+
   |       msg_time       |
   +----------------------+
   | 2021-11-01 07:44:37  |
   | 2021-11-01 07:29:22  |
   | 2021-11-01 07:57:58  |
   | 2021-11-01 07:26:15  |
   | 2021-11-01 07:01:10  |
   | 2021-11-01 07:23:30  |
   | 2021-11-01 07:57:22  |
   | 2021-11-01 07:42:37  |
   | 2021-11-01 07:17:08  |
   | 2021-11-01 07:15:25  |
   +----------------------+
   ```

   对此我们可以使用Substr函数

3. 需求中，需要对经度和维度构建地区的可视化地图，但是数据中GPS经纬度为一个字段，不好处理

   ```sql
   select sender_gps
   from db_msg.tb_msg_source
   limit 10;
   ```

   ```apl
   +-----------------------+
   |      sender_gps       |
   +-----------------------+
   | 123.257181,48.807394  |
   | 113.39623,22.371406   |
   | 117.885171,33.21035   |
   | 121.785398,41.86541   |
   | 124.361019,45.856657  |
   | 86.904123,45.547269   |
   | 119.651311,33.519113  |
   | 114.058533,39.39896   |
   | 117.07569,34.010827   |
   | 112.439571,33.148464  |
   +-----------------------+
   ```

   对此我们可以使用Split函数

4. 将ETL以后的结果保存到一张新的Hive表中

   ```sql
   Create table …… as select ……
   ```

ETL实现：

```sql
-- 如果表已存在就删除
drop table if exists db_msg.tb_msg_etl;

-- 将Select语句的结果保存到新表中
create table db_msg.tb_msg_etl as
select
	*,
	substr(msg_time,0,10) as dayinfo, 
	substr(msg_time,12,2) as hourinfo, -- 获取天和小时
	split(sender_gps,",")[0] as sender_lng, 
	split(sender_gps,",")[1] as sender_lat -- 提取经度纬度
from db_msg.tb_msg_source
where length(sender_gps) > 0 ; -- 过滤字段为空的数据
```

查看结果

```sql
select msg_time,dayinfo,hourinfo,sender_gps,sender_lng,sender_lat
from db_msg.tb_msg_etl
limit 10;
```

## 7.3 需求指标统计

需求如下：

- 统计今日总消息量

  ```sql
  -- 保存结果表
  create table if not exists tb_rs_total_msg_cnt
      comment "今日消息总量"
  as
  select dayinfo,                  -- 天数
         count(*) as total_msg_cnt -- 消息条数
  from db_msg.tb_msg_etl
  group by dayinfo;
  ```

- 统计今日每小时消息量、发送和接收用户数

  ```sql
  create table if not exists tb_rs_hour_msg_cnt
      comment "今日每小时消息量、发送和接收用户数"
  as
  select dayinfo,
         hourinfo,
         count(message)                   as total_msg_cnt,
         count(distinct sender_account)   as sender_usr_cnt,
         count(distinct receiver_account) as reveiver_usr_cnt
  from db_msg.tb_msg_etl
  group by dayinfo, hourinfo;
  ```

- 统计今日各地区发送消息数据量

  ```sql
  -- 统计今日各地区发送消息数据量
  create table if not exists tb_rs_loc_cnt
      comment "统计今日各地区发送消息数据量"
  as
  select dayinfo,
         sender_gps,
         cast(sender_lng as double) as longitude,
         cast(sender_lat as double) as latitude,
         count(*)                   as total_msg_cnt
  from db_msg.tb_msg_etl
  group by dayinfo, sender_gps, sender_lng, sender_lat;
  
  -- 验证是否成功
  select *
  from tb_rs_loc_cnt;
  ```

- 统计今日发送消息和接收消息的用户数

  ```sql
  -- 统计今日发送和接收用户人数
  create table if not exists tb_rs_usr_cnt
      comment "统计今日发送和接收用户人数"
  as
  select dayinfo,
         count(distinct sender_account)   as sender_usr_cnt,
         count(distinct receiver_account) as receiver_usr_cnt
  from db_msg.tb_msg_etl
  group by dayinfo;
  
  -- 查询结果
  select * from tb_rs_usr_cnt;
  ```

- 统计今日发送消息最多的Top10用户

  ```sql
  -- 统计发送消息条数最多的Top10用户
  -- 也就是统计每人发消息数量 然后排序 显示前十个
  create table if not exists tb_rs_susr_top10
      comment "发送消息条数最多的Top10用户"
  as
  select sender_name as username,
         count(*)    as sender_msg_cnt
  from db_msg.tb_msg_etl
  group by sender_name
  order by sender_msg_cnt desc
  limit 10;
  
  select * from tb_rs_susr_top10;
  ```

- 统计今日接收消息最多的Top10用户

  ```sql
  -- 统计接收消息条数最多的Top10用户
  create table if not exists tb_rs_rusr_top10
      comment "接受消息条数最多的Top10用户"
  as
  select dayinfo,
         receiver_name as username,
         count(*)      as receiver_msg_cnt
  from db_msg.tb_msg_etl
  group by dayinfo, receiver_name
  order by receiver_msg_cnt desc
  limit 10;
  ```

- 统计发送人的手机型号分布情况

  ```sql
  create table if not exists tb_rs_sender_phone
      comment "发送人的手机型号分布"
  as
  select dayinfo,
         sender_phonetype,
         count(distinct sender_account) as cnt
  from tb_msg_etl
  group by dayinfo, sender_phonetype;
  ```

- 统计发送人的设备操作系统分布情况

  ```sql
  create table if not exists tb_rs_sender_os
      comment "发送人的OS分布"
  as
  select dayinfo,
         sender_os,
         count(distinct sender_account) as cnt
  from tb_msg_etl
  group by dayinfo, sender_os;
  ```

## 7.4 FineBI实现可视化报表

FineBI的介绍：https://www.finebi.com/.

FineBI 是帆软软件有限公司推出的一款商业智能（Business Intelligence）产品。FineBI 是定位于自助大数据分
析的 BI 工具，能够帮助企业的业务人员和数据分析师，开展以问题导向的探索式分析。

FineBI的特点如下：

- 通过多人协作来实现最终的可视化构建
- 不需要通过复杂代码来实现开发，通过可视化操作实现开发
- 适合于各种数据可视化的应用场景
- 支持各种常见的分析图表和各种数据源
- 支持处理大数据

**配置数据源及数据准备**

FineBI与Hive集成的官方文档：https://help.fanruan.com/finebi/doc-view-301.html

如果使用FineBI连接Hive，读取Hive的数据表，需要在FineBI中添加Hive的驱动jar包。将Hive的驱动jar包放入FineBI的`webapps\webroot\WEB-INF\lib`目录下

我们自己放的Hive驱动包会与FineBI自带的驱动包产生冲突，导致FineBI无法识别我们自己的驱动包。安装FineBI官方提供的驱动包隔离插件。然后重启。
