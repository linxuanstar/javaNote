![](..\图片\2-24【Maven】\1面试.png)

# 第一章 Maven基础

`Maven` 的正确发音是`[ˈmevən]`，而不是“马瘟”以及其他什么瘟。`Maven` 在美国是一个口语化的词语，代表专家、内行的意思。 

一个对 `Maven` 比较正式的定义是这么说的：`Maven` 是一个项目管理工具，它包含了一个项目对象模型 `(POM：Project Object Model)`，一组标准集合，一个项目生命周期`(Project Lifecycle)`，一个依赖管理系统`(Dependency Management System)`和用来运行定义在生命周期阶段`(phase)`中插件`(plugin)`目标 `(goal)`的逻辑。

Maven的本质是一个项目管理工具，将项目开发和管理过程抽象成一个项目对象模型（POM）。Maven是用Java语言编写的。他管理的东西统统以面向对象的形式进行设计，最终它把一个项目看成一个对象，而这个对象叫做**POM**(project object model)，即项目对象模型。

Maven作用如下：

* 项目构建：提供标准的，跨平台的自动化构建项目的方式。
* 依赖管理：方便快捷的管理项目依赖的资源（jar包），避免资源间的版本冲突等问题。
* 统一开发结构：提供标准的，统一的项目开发结构。

## 1.1 下载安装

官网目录：https://maven.apache.org/。

maven是一个绿色软件，解压即安装。我将下载的`apache-maven-3.6.0-bin.zip`直接解压到`E:\Maven`目录下面。解压完成后可以查看一下maven自己的一个目录结构如下：

```apl
apache-maven-3.6.0
     |-- bin：可执行程序目录，
     |-- boot：maven自身的启动加载器
     |-- conf：maven配置文件的存放目录
     |-- lib：maven运行所需库的存放目录
```

接下来配置环境变量：`MAVEN_HOME=E:\Maven\apache-maven-3.6.0`、`%MAVEN_HOME%\bin`。之后在命令行窗口中测试一下环境变量是否配置成功：

```apl
C:\Windows\System32>mvn -v
Apache Maven 3.6.0 (97c98ec64a1fdfee7767ce5ffb20918da4f719f3; 2018-10-25T02:41:47+08:00)
Maven home: E:\Maven\apache-maven-3.6.0\bin\..
Java version: 1.8.0_144, vendor: Oracle Corporation, runtime: E:\JAVA\jdk1.8.0_144\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

接下来配置一下jar包下载的镜像网站：

```xml
<!-- 在settings.xml里面的mirrors标签里面导入 -->
<mirror>
  <id>aliyunmaven</id>
  <mirrorOf>*</mirrorOf>
  <name>阿里云公共仓库</name>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

配置本地仓库：

```xml
<!-- 创建一个本地仓库 E:\Maven\apache-maven-3.6.0\mvn_repository文件夹，用于存放jar包 -->
<!-- 在settings.xml里面的localRepository下导入 -->
<localRepository>E:\Maven\apache-maven-3.6.0\mvn_repository</localRepository>
```

## 1.2 Maven基本概念

**仓库**

maven 的工作需要从仓库下载一些 jar 包，本地的项目都会通过 maven 从远程仓库下载 jar 包并存在本地仓库，本地仓库就是本地文件夹，当第二次需要此 jar 包时则不再从远程仓库下载，因为本地仓库已经存在了，可以将本地仓库理解为缓存，有了本地仓库就不用每次从远程仓库下载了。

本地仓库 ：用来存储从远程仓库下载的插件和 jar 包。项目中使用一些插件或 jar 包优先从本地仓库查找默认本地仓库位置在 `${user.dir}/.m2/repository`，`${user.dir}`表示 windows 用户目录。我们可以修改。

远程仓库分为中央仓库和私服：

* 中央仓库 ：在 maven 软件中内置一个远程仓库地址 http://repo1.maven.org/maven2。它是中央仓库，服务于整个互联网。它是由 Maven 团队自己维护，里面存储了非常全的 jar 包，它包含了世界上大部分流行的开源项目构件。
* 私服：各公司/部门等小范围内存储资源的仓库，私服也可以从中央仓库获取资源。私服可以保存具有版权的资源，包含购买或自主研发的jar；一定范围内共享资源，能做到仅对内不对外开放。

**坐标**

我们说maven的仓库里存储了各种各样的资源（jar包），那这些资源我们如何找到它们呢？我们需要知道它们具体的一个位置才能知道如何找到它们，这个就叫坐标。

坐标：被Maven管理的资源的唯一标识。maven坐标的主要组成如下：

- groupId：定义当前资源隶属组织名称，通常是域名反写。

- artifactId：定义当前资源的名称，通常是项目或模块名称。

- version：定义当前资源的版本号。

- packaging：定义资源的打包方式。取值一般有三种：`jar` java工程打包为jar默认的取值就是jar、`war`项目为web工程并且打包方式为war、`pom`资源是一个分模块管理的父资源并且打包时只生成一个`pom.xml`不生成jar或其他包结构。


```xml
<!-- pom.xml文件内容 -->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!--指定pom的模型版本-->
    <modelVersion>4.0.0</modelVersion>
    <!--打包方式 web工程打包为war java工程打包为jar-->
    <packaging>war</packaging>
    
    <!--组织id-->
    <groupId>com.linxuan</groupId>
    <!--项目id-->
    <artifactId>javaweb</artifactId>
    <!--组织id  release代表完成版,SNAPSHOT代表开发版-->
    <version>1.0-SNAPSHOT</version>
    
    <!--设置当前工程的所有依赖-->
    <dependencies>
        <!--具体的依赖-->
        <dependency>
        </dependency>
    </dependencies>
</project>
```

**Maven工程目录结构**

```apl
项目的根目录:
      |-- src # 源码
      |    |-- main # 主工程代码
      |    |    |-- java # 业务逻辑代码
      |    |    |-- resources # 业务逻辑代码配置文件
      |    |    |-- webapp # web项目的资源目录。例如:jps/html/css/js
      |    |          |-- WEB-INF # 存放的是一些编译后的class文件和运行所必须的配置文件
      |    |                |-- web.xml文件
      |    |          |-- jsp/html/css/js # 存放前端资源文件
      |    |
      |    |-- test # 测试代码
      |         |-- java # 测试代码
      |         |-- resources # 测试代码所需要的配置文件
      |-- target # 
      |    |-- classes # 业务逻辑代码编译后的文件存放到这个目录下面
      |    |-- test-classes # 测试代码编译后的文件存放到这个目录下面
      |-- pom.xml # 项目的核心配置文件
```

## 1.3 创建项目

**手动创建**



## 1.3 Maven基本命令

- `mvn -version`：查看Maven版本。
- `mvn dependency:get -DgroupId=XXX -DartifactId=XXX -Dversion=XXX`：读取maven配置，下载jar包到本地仓库。需要在下载的位置打开CMD窗口。
- `compile` 是 `maven` 工程的编译命令，作用是将 `src/main/java` 下的文件编译为 `class` 文件输出到 `target` 目录下。
- `test` 是 `maven` 工程的测试命令 `mvn test`，会执行 `src/test/java` 下的单元测试类。
- `clean` 是 `maven` 工程的清理命令，执行 `clean` 会删除 `target` 目录及内容。
- `package` 是 `maven` 工程的打包命令，对于 `java` 工程执行 `package` 打成 `jar` 包，对于 `web` 工程打成 `war` 包。
- `install` 是 `maven` 工程的安装命令，执行 `install` 将 `maven` 打成 `jar` 包或 `war` 包发布到本地仓库。

 从运行结果中，可以看出： 当后面的命令执行时，前面的操作过程也都会自动执行，

## 1.4 Maven指令的生命周期

maven 对项目构建过程分为三套相互独立的生命周期，请注意这里说的是“三套”，而且“相互独立”， 这三套生命周期分别是：

- `Clean Lifecycle`： 在进行真正的构建之前进行一些清理工作。 
- `Default Lifecycle`： 构建的核心部分，编译，测试，打包，部署等等。 
- `Site Lifecycle`：生成项目报告，站点，发布站点。

同一套生命周期中，执行后边的操作，会自动执行之前所有操作。

# 第二章 分模块开发

有两种拆分方式：按照功能拆分和按照模块拆分。

将原始模块按照功能拆分成若干个子模块，方便模块间的相互调用，接口共享。

我们可以将其他的层也拆成一个个对立的模块，如：

<img src="..\图片\2-24【Maven】\1-1.png" alt="1630768869208" style="zoom: 67%;" />

这样的话，项目中的每一层都可以单独维护，也可以很方便的被别人使用。

前面我们已经完成了SSM整合，接下来，咱们就基于SSM整合的项目来实现对项目的拆分。

将我们之前做过的SSM整合案例，也就是`D:\Java\IdeaProjects\ssm\springmvc04`部署到IDEA中。这里新建了一个Maven项目，里面创建了一个`maven_02_ssm`的Moudle。`D:\Java\IdeaProjects\ssm\springmvc04`变成了`maven_02_ssm`。

## 2.1 抽取domain层

1. 创建新模块

   创建一个名称为`maven_03_pojo`的jar项目，为什么项目名是从02到03这样创建，原因后面我们会提到，这块的名称可以任意。

2. 项目中创建domain包

   在`maven_03_pojo`项目中创建`com.linxuan.domain`包，并将`maven_02_ssm`中Book类拷贝到该包中

3. 删除原项目中的domain包

   删除后，`maven_02_ssm`项目中用到`Book`的类中都会有红色提示。

   **说明：**出错的原因是`maven_02_ssm`中已经将Book类删除，所以该项目找不到Book类，所以报错

   要想解决上述问题，我们需要在`maven_02_ssm`中添加`maven_03_pojo`的依赖。

4. 建立依赖关系

   在`maven_02_ssm`项目的pom.xml添加`maven_03_pojo`的依赖。打开`maven_03_pojo`的pom.xml文件可以看到下面的信息，将其导入进去`maven_02_ssm`就可以了。

   ```xml
   <dependency>
       <groupId>com.linxuan</groupId>
       <artifactId>maven_03_pojo</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

   因为添加了依赖，所以在`maven_02_ssm`中就已经能找到Book类，所以刚才的报红提示就会消失。

5. 编译`maven_02_ssm`项目

   点击Maven里面`maven_02_ssm`项目下面的compile，去编译`maven_02_ssm`，这时我们会看到一些错误：

   ```asciiarmor
   [ERROR] Failed to execute goal on project maven_02_ssm: Could not resolve dependencies for project com.linxuan:maven_02_ssm:war:1.0-SNAPSHOT: Could not find artifact com.linxuan:maven_03_pojo:jar:1.0-SNAPSHOT -> [Help 1]
   ```

   错误信息为：不能解决`maven_02_ssm`项目的依赖问题，找不到`maven_03_pojo`这个jar包。

   为什么找不到呢？原因是Maven会从本地仓库找对应的jar包，但是本地仓库又不存在该jar包所以会报错。

   在IDEA中是有`maven_03_pojo`这个项目，所以我们只需要将`maven_03_pojo`项目安装到本地仓库即可。

6. 将项目安装本地仓库

   将需要被依赖的项目`maven_03_pojo`，使用maven的install命令，把其安装到Maven的本地仓库中。

   安装成功后，在对应的路径下就看到安装好的jar包

   当再次执行`maven_02_ssm`的compile的命令后，就已经能够成功编译。

## 2.2 抽取Dao层

1. 创建新模块

   创建一个名称为`maven_04_dao`的jar项目

2. 项目中创建dao包

   在`maven_04_dao`项目中创建`com.linxuan.dao`包，并将`maven_02_ssm`中BookDao类拷贝到该包中

   在`maven_04_dao`中会有如下几个问题需要解决下:

   1. 项目`maven_04_dao`的BookDao接口中Book类找不到报错

      解决方案在`maven_04_dao`项目的pom.xml中添加`maven_03_pojo`项目

      ```xml
      <dependencies>
          <dependency>
              <groupId>com.linxuan</groupId>
              <artifactId>maven_03_pojo</artifactId>
              <version>1.0-SNAPSHOT</version>
          </dependency>
      </dependencies>
      ```

   2. 项目`maven_04_dao`的BookDao接口中，Mybatis的增删改查注解报错

      解决方案在`maven_04_dao`项目的pom.xml中添加`mybatis`的相关依赖

      ```xml
      <dependencies>
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis</artifactId>
              <version>3.5.6</version>
          </dependency>
      
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>5.1.47</version>
          </dependency>
      </dependencies>
      ```

3. 删除原项目中的dao包

   删除Dao包以后，因为`maven_02_ssm`中的BookServiceImpl类中有使用到Dao的内容，所以需要在`maven_02_ssm`的pom.xml添加`maven_04_dao`的依赖

   ```xml
   <dependency>
       <groupId>com.linxuan</groupId>
       <artifactId>maven_04_dao</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

   此时在`maven_02_ssm`项目中就已经添加了`maven_03_pojo`和`maven_04_dao`包

   再次对`maven_02_ssm`项目进行编译，又会报错。和刚才的错误原因是一样的，maven在仓库中没有找到`maven_04_dao`,所以此时我们只需要将`maven_04_dao`安装到Maven的本地仓库即可。

4. 将项目安装到本地仓库

   将需要被依赖的项目`maven_04_dao`，使用maven的install命令，把其安装到Maven的本地仓库中。

   安装成功后，在对应的路径下就看到了安装好对应的jar包。

   当再次执行`maven_02_ssm`的compile的指令后，就已经能够成功编译。

## 2.3 运行测试并总结

将抽取后的项目进行运行，测试之前的增删改查功能依然能够使用。

所以对于项目的拆分，大致会有如下几个步骤:

1. 创建Maven模块

2. 书写模块代码

   分模块开发需要先针对模块功能进行设计，再进行编码。不会先将工程开发完毕，然后进行拆分。拆分方式可以按照功能拆也可以按照模块拆。

3. 通过maven指令安装模块到本地仓库(install 指令)

团队内部开发需要发布模块功能到团队内部可共享的仓库中(私服)，私服我们后面会讲解。

# 第三章 依赖管理

我们现在已经能把项目拆分成一个个独立的模块，当在其他项目中想要使用独立出来的这些模块，只需要在其pom.xml使用<dependency>标签来进行jar包的引入即可。

<dependency>其实就是依赖，我们先来说说什么是依赖：依赖指当前项目运行所需的jar，一个项目可以设置多个依赖。

格式为:

```xml
<!--设置当前项目所依赖的所有jar-->
<dependencies>
    <!--设置具体的依赖-->
    <dependency>
        <!--依赖所属群组id-->
        <groupId>org.springframework</groupId>
        <!--依赖所属项目id-->
        <artifactId>spring-webmvc</artifactId>
        <!--依赖版本号-->
        <version>5.2.10.RELEASE</version>
    </dependency>
</dependencies>
```

## 3.1 依赖传递与冲突

传递依赖：如果我们的项目引用了一个Jar包，而该Jar包又引用了其他Jar包，那么在默认情况下项目编译时，Maven会把直接引用和间接引用的Jar包都下载到本地。

因为有依赖传递的存在，就会导致jar包在依赖的过程中出现冲突问题。这里所说的依赖冲突是指项目依赖的某一个jar包，有多个不同的版本，因而造成类包版本冲突。

若项目中多个Jar同时引用了相同的Jar时，会产生依赖冲突，但Maven采用了三种避免冲突的策略，因此在Maven中是不存在依赖冲突的。

1. 特殊优先：当同级配置了相同资源的不同版本，后配置的覆盖先配置的。

   当引入同一个依赖(例如junit)不同版本的时候，后配置的会覆盖之前配置的。

2. 路径优先：当依赖中出现相同的资源时，层级越深，优先级越低，层级越浅，优先级越高

   ```apl
   本项目——>A.jar——>B.jar——>X.jar
   本项目——>C.jar——>X.jar
   ```

   若本项目引用了A.jar，A.jar又引用了B.jar，B.jar又引用了X.jar，并且C.jar也引用了X.jar。在此时，Maven只会引用引用路径最短的Jar。


3. 声明优先：当资源在相同层级被依赖时，配置顺序靠前的覆盖配置顺序靠后的



但是对应上面这些结果，我们不需要刻意去记它。因为不管Maven怎么选，最终的结果都会在Maven的`Dependencies`面板中展示出来，展示的是哪个版本，也就是说它选择的就是哪个版本。

如果想更全面的查看Maven中各个坐标的依赖关系，可以点击Maven面板中的`show Dependencies`

![1630853519736](..\图片\2-24【Maven】\1-7.png)

在这个视图中就能很明显的展示出jar包之间的相互依赖关系。

## 3.2 可选依赖和排除依赖

依赖传递介绍完以后，我们来思考一个问题，

![1630854436435](..\图片\2-24【Maven】\1-8.png)

* maven_02_ssm 依赖了 maven_04_dao
* maven_04_dao 依赖了 maven_03_pojo
* 因为现在有依赖传递，所以maven_02_ssm能够使用到maven_03_pojo的内容
* 如果说现在不想让maven_02_ssm依赖到maven_03_pojo，有哪些解决方案？

> 在真实使用的过程中，maven_02_ssm中是需要用到maven_03_pojo的，我们这里只是用这个例子描述我们的需求。因为有时候，maven_04_dao出于某些因素的考虑，就是不想让别人使用自己所依赖的maven_03_pojo。

### 可选依赖

* 可选依赖指对外隐藏当前所依赖的资源——不透明

在`maven_04_dao`的pom.xml,在引入`maven_03_pojo`的时候，添加`optional`

```xml
<dependency>
    <groupId>com.linxuan</groupId>
    <artifactId>maven_03_pojo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!--可选依赖是隐藏当前工程所依赖的资源，隐藏后对应资源将不具有依赖传递-->
    <optional>true</optional>
</dependency>
```

此时BookServiceImpl就已经报错了，说明由于maven_04_dao将maven_03_pojo设置成可选依赖，导致maven_02_ssm无法引用到maven_03_pojo中的内容，导致Book类找不到。

### 排除依赖

* 排除依赖指主动断开依赖的资源，被排除的资源无需指定版本---不需要

前面我们已经通过可选依赖实现了阻断`maven_03_pojo`的依赖传递，对于排除依赖，则指的是已经有依赖的事实，也就是说`maven_02_ssm`项目中已经通过依赖传递用到了`maven_03_pojo`，此时我们需要做的是将其进行排除，所以接下来需要修改`maven_02_ssm`的pom.xml

```xml
<dependency>
    <groupId>com.linxuan</groupId>
    <artifactId>maven_04_dao</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!--排除依赖是隐藏当前资源对应的依赖关系-->
    <exclusions>
        <exclusion>
            <groupId>com.linxuan</groupId>
            <artifactId>maven_03_pojo</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

这样操作后，BookServiceImpl中的Book类一样也会报错。

当然`exclusions`标签带`s`说明我们是可以依次排除多个依赖到的jar包，比如maven_04_dao中有依赖junit和mybatis,我们也可以一并将其排除。

```xml
<dependency>
    <groupId>com.linxuan</groupId>
    <artifactId>maven_04_dao</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!--排除依赖是隐藏当前资源对应的依赖关系-->
    <exclusions>
        <exclusion>
            <groupId>com.linxuan</groupId>
            <artifactId>maven_03_pojo</artifactId>
        </exclusion>
        <exclusion>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

介绍这两种方式后，简单来梳理下，就是

* `A依赖B,B依赖C`,`C`通过依赖传递会被`A`使用到，现在要想办法让`A`不去依赖`C`
* 可选依赖是在B上设置`<optional>`,`A`不知道有`C`的存在，
* 排除依赖是在A上设置`<exclusions>`,`A`知道有`C`的存在，主动将其排除掉。

## 3.3 聚合和继承

我们的项目已经从以前的单模块，变成了现在的多模块开发。项目一旦变成了多模块开发以后，就会引发一些问题，在这一节中我们主要会学习两个内容`聚合`和`继承`，用这两个知识来解决下分模块后的一些问题。

### 聚合

![1630858596147](..\图片\2-24【Maven】\1-9.png)

* 分模块开发后，需要将这四个项目都安装到本地仓库，目前我们只能通过项目Maven面板的`install`来安装，并且需要安装四个，如果我们的项目足够多，那么一个个安装起来还是比较麻烦的
* 如果四个项目都已经安装成功，当`ssm_pojo`发生变化后，我们就得将`ssm_pojo`重新安装到maven仓库，但是为了确保我们对`ssm_pojo`的修改不会影响到其他项目模块，我们需要对所有的模块进行重新编译，那又需要将所有的模块再来一遍

项目少的话还好，但是如果项目多的话，一个个操作项目就容易出现漏掉或重复操作的问题，所以我们就想能不能抽取一个项目，把所有的项目管理起来，以后我们要想操作这些项目，只需要操作这一个项目，其他所有的项目都走一样的流程，这个不就很省事省力。

这就用到了我们接下来要讲解的聚合，

* 所谓聚合：将多个模块组织成一个整体，同时进行项目构建的过程称为聚合。

* 聚合工程：通常是一个不具有业务功能的"空"工程（有且仅有一个pom文件）。

* 作用：使用聚合工程可以将多个工程编组，通过对聚合工程进行构建，实现对所包含的模块进行同步构建。
  
  当工程中某个模块发生更新（变更）时，必须保障工程中与已更新模块关联的模块同步更新，此时可以使用聚合工程来解决批量模块同步构建的问题。

关于聚合具体的实现步骤为：

1. 创建一个空的maven项目，不用任何模板，这里创建一个`maven_01_parent`。

2. 将项目的打包方式改为pom

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <groupId>com.linxuan</groupId>
       <artifactId>maven_01_parent</artifactId>
       <version>1.0-RELEASE</version>
       <packaging>pom</packaging>
       
   </project>
   ```

   **说明:**项目的打包方式，我们接触到的有三种，分别是

   * jar:默认情况，说明该项目为java项目

   * war:说明该项目为web项目

   * pom:说明该项目为聚合或继承(后面会讲)项目


3. pom.xml添加所要管理的项目

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <groupId>com.linxuan</groupId>
       <artifactId>maven_01_parent</artifactId>
       <version>1.0-RELEASE</version>
       <packaging>pom</packaging>
       
       <!--设置管理的模块名称-->
       <modules>
           <module>../maven_02_ssm</module>
           <module>../maven_03_pojo</module>
           <module>../maven_04_dao</module>
       </modules>
   </project>
   ```

4. 使用聚合统一管理项目

   测试发现，当`maven_01_parent`的`compile`被点击后，所有被其管理的项目都会被执行编译操作。这就是聚合工程的作用。

   **说明：**聚合工程管理的项目在进行运行的时候，会按照项目与项目之间的依赖关系来自动决定执行的顺序和配置的顺序无关。

聚合的知识我们就讲解完了，最后总结一句话就是，**聚合工程主要是用来管理项目**。

### 继承

我们已经完成了使用聚合工程去管理项目，聚合工程进行某一个构建操作，其他被其管理的项目也会执行相同的构建操作。那么接下来，我们再来分析下，多模块开发存在的另外一个问题，`重复配置`的问题，我们先来看张图：

![1630860344968](..\图片\2-24【Maven】\1-10.png)

* `spring-webmvc`、`spring-jdbc`在三个项目模块中都有出现，这样就出现了重复的内容
* `spring-test`只在ssm_crm和ssm_goods中出现，而在ssm_order中没有，这里是部分重复的内容
* 我们使用的spring版本目前是`5.2.10.RELEASE`，假如后期要想升级spring版本，所有跟Spring相关jar包都得被修改，涉及到的项目越多，维护成本越高

面对上面的这些问题，我们就得用到接下来要学习的继承

* 所谓继承：描述的是两个工程间的关系，与java中的继承相似，子工程可以继承父工程中的配置信息，常见于依赖关系的继承。
* 作用：简化配置、减少版本冲突。

接下来，我们到程序中去看看继承该如何实现?

1. 创建一个空的Maven项目并将其打包方式设置为pom

   因为这一步和前面maven创建聚合工程的方式是一摸一样，所以我们可以单独创建一个新的工程，也可以直接和聚合公用一个工程。实际开发中，聚合和继承一般也都放在同一个项目中，但是这两个的功能是不一样的。

2. 在子项目中设置其父工程

   分别在`maven_02_ssm`、`maven_03_pojo`、`maven_04_dao`的pom.xml中添加其父项目为`maven_01_parent`

   ```xml
   <!--配置当前工程继承自parent工程-->
   <parent>
       <groupId>com.linxuan</groupId>
       <artifactId>maven_01_parent</artifactId>
       <version>1.0-RELEASE</version>
       <!--设置父项目pom.xml位置路径-->
       <relativePath>../maven_01_parent/pom.xml</relativePath>
   </parent>
   ```

3. 优化子项目共有依赖导入问题

   1. 将子项目共同使用的jar包都抽取出来，维护在父项目的pom.xml中

      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <project xmlns="http://maven.apache.org/POM/4.0.0"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
          <modelVersion>4.0.0</modelVersion>
      
          <groupId>com.linxuan</groupId>
          <artifactId>maven_01_parent</artifactId>
          <version>1.0-RELEASE</version>
          <packaging>pom</packaging>
          
          <!--设置管理的模块名称-->
          <modules>
              <module>../maven_02_ssm</module>
              <module>../maven_03_pojo</module>
              <module>../maven_04_dao</module>
          </modules>
          
          <!--将子项目共同使用的jar包都抽取出来，维护在父项目的pom.xml中-->
          <dependencies>
              <dependency>
                  <groupId>org.springframework</groupId>
                  <artifactId>spring-core</artifactId>
                  <version>5.2.10.RELEASE</version>
              </dependency>
      
              <dependency>
                  <groupId>org.springframework</groupId>
                  <artifactId>spring-webmvc</artifactId>
                  <version>5.2.10.RELEASE</version>
              </dependency>
      
              <!--省略一些抽取的jar包-->
          </dependencies>
      </project>
      ```

   2. 删除子项目中已经被抽取到父项目的pom.xml中的jar包，如在`maven_02_ssm`的pom.xml中将已经出现在父项目的jar包删除掉

      删除完后，我们会发现父项目中有依赖对应的jar包，子项目虽然已经将重复的依赖删除掉了，但是刷新的时候，子项目中所需要的jar包依然存在。

      当项目的`<parent>`标签被移除掉，会发现多出来的jar包依赖也会随之消失。

   3. 将`maven_04_dao`项目的pom.xml中的所有依赖删除，然后添加上`maven_01_parent`的父项目坐标

      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <project xmlns="http://maven.apache.org/POM/4.0.0"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
          <modelVersion>4.0.0</modelVersion>
      
          <groupId>com.linxuan</groupId>
          <artifactId>maven_04_dao</artifactId>
          <version>1.0-SNAPSHOT</version>
      
          <!--配置当前工程继承自parent工程-->
          <parent>
              <groupId>com.linxuan</groupId>
              <artifactId>maven_01_parent</artifactId>
              <version>1.0-RELEASE</version>
              <relativePath>../maven_01_parent/pom.xml</relativePath>
          </parent>
      </project>
      ```

      刷新并查看Maven的面板，会发现maven_04_dao同样引入了父项目中的所有依赖。

      这样我们就可以解决刚才提到的第一个问题，将子项目中的公共jar包抽取到父工程中进行统一添加依赖，这样做的可以简化配置，并且当父工程中所依赖的jar包版本发生变化，所有子项目中对应的jar包版本也会跟着更新。


4. 优化子项目依赖版本问题

   如果把所有用到的jar包都管理在父项目的pom.xml，看上去更简单些，但是这样就会导致有很多项目引入了过多自己不需要的jar包。如下面看到的这张图：

   ![1630860344968](..\图片\2-24【Maven】\1-10.png)

   如果把所有的依赖都放在了父工程中进行统一维护，就会导致ssm_order项目中多引入了`spring-test`的jar包，如果这样的jar包过多的话，对于ssm_order来说也是一种"负担"。

   那针对于这种部分项目有的jar包，我们该如何管理优化呢？

   1. 在父工程mavne_01_parent的pom.xml来定义依赖管理

      ```xml
      <!--定义依赖管理-->
      <dependencyManagement>
          <dependencies>
              <dependency>
                  <groupId>junit</groupId>
                  <artifactId>junit</artifactId>
                  <version>4.12</version>
                  <scope>test</scope>
              </dependency>
          </dependencies>
      </dependencyManagement>
      ```

   2. 将maven_02_ssm的pom.xml中的junit依赖删除掉，刷新Maven

      ![1630944335419](..\图片\2-24【Maven】\1-11.png)

      刷新完会发现，在maven_02_ssm项目中的junit依赖并没有出现，这是因为：`<dependencyManagement>`标签不真正引入jar包，而是配置可供子项目选择的jar包依赖。子项目要想使用它所提供的这些jar包，需要自己添加依赖，并且不需要指定`<version>`

   3. 在maven_02_ssm的pom.xml添加junit的依赖

      ```xml
      <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <scope>test</scope>
      </dependency>
      ```

      > **注意：这里就不需要添加版本了，这样做的好处就是当父工程dependencyManagement标签中的版本发生变化后，子项目中的依赖版本也会跟着发生变化**

   4. 在maven_04_dao的pom.xml添加junit的依赖

      ```xml
      <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <scope>test</scope>
      </dependency>
      ```

      这个时候，maven_02_ssm和maven_04_dao这两个项目中的junit版本就会跟随着父项目中的标签dependencyManagement中junit的版本发生变化而变化。不需要junit的项目就不需要添加对应的依赖即可。


至此继承就已经学习完了，最后总结一句话就是，**父工程主要是用来快速配置依赖jar包和管理项目中所使用的资源**。

### 聚合与继承的区别

两种之间的作用:

* 聚合用于快速构建项目，对项目进行管理
* 继承用于快速配置和管理子项目中所使用jar包的版本

聚合和继承的相同点:

* 聚合与继承的pom.xml文件打包方式均为pom，可以将两种关系制作到同一个pom文件中
* 聚合与继承均属于设计型模块，并无实际的模块内容

聚合和继承的不同点:

* 聚合是在当前模块中配置关系，聚合可以感知到参与聚合的模块有哪些
* 继承是在子模块中配置关系，父模块无法感知哪些子模块继承了自己

相信到这里，大家已经能区分开什么是聚合和继承，但是有一个稍微麻烦的地方就是聚合和继承的工程构建，需要在聚合项目中手动添加`modules`标签，需要在所有的子项目中添加`parent`标签。

### IDEA构建聚合与继承工程

其实对于聚合和继承工程的创建，IDEA已经能帮助我们快速构建，具体的实现步骤为:

1. 创建一个Maven项目

   创建一个空的Maven项目，可以将项目中的`src`目录删除掉，这个项目作为聚合工程和父工程。

2. 创建子项目

   该项目可以被聚合工程管理，同时会继承父工程。

   ![1630947082716](..\图片\2-24【Maven】\1-12.png)

   创建成功后，maven_parent即是聚合工程又是父工程，maven_web中也有parent标签，继承的就是maven_parent,对于难以配置的内容都自动生成。

# 第四章 属性和版本管理

属性中会继续解决分模块开发项目存在的问题，版本管理主要是认识下当前主流的版本定义方式。

## 4.1 属性

前面我们已经在父工程中的dependencyManagement标签中对项目中所使用的jar包版本进行了统一的管理，但是如果在标签中有如下的内容：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>5.2.10.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.2.10.RELEASE</version>
</dependency>

<!--省略一些抽取的jar包-->
```

我们会发现，如果我们现在想更新Spring的版本，我们会发现我们依然需要更新多个jar包的版本，这样的话还是有可能出现漏改导致程序出问题，而且改起来也是比较麻烦。

问题清楚后，我们需要解决的话，就可以参考咱们java基础所学习的变量，声明一个变量，在其他地方使用该变量，当变量的值发生变化后，所有使用变量的地方，就会跟着修改，即：

![1630947749661](..\图片\2-24【Maven】\1-13.png)

1. 父工程中定义属性

   ```xml
   <properties>
       <spring.version>5.2.10.RELEASE</spring.version>
       <junit.version>4.12</junit.version>
       <mybatis-spring.version>1.3.0</mybatis-spring.version>
   </properties>
   ```

2. 修改依赖的version

   ```xml
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-core</artifactId>
       <version>${spring.version}</version>
   </dependency>
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-webmvc</artifactId>
       <version>${spring.version}</version>
   </dependency>
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-jdbc</artifactId>
       <version>${spring.version}</version>
   </dependency>
   ```

   此时，我们只需要更新父工程中properties标签中所维护的jar包版本，所有子项目中的版本也就跟着更新。当然除了将spring相关版本进行维护，我们可以将其他的jar包版本也进行抽取，这样就可以对项目中所有jar包的版本进行统一维护，如：

   ```xml
   <!--定义属性-->
   <properties>
       <spring.version>5.2.10.RELEASE</spring.version>
       <junit.version>4.12</junit.version>
       <mybatis-spring.version>1.3.0</mybatis-spring.version>
   </properties>
   ```

## 4.2 配置文件加载属性

Maven中的属性我们已经介绍过了，现在也已经能够通过Maven来集中管理Maven中依赖jar包的版本。但是又有新的需求，就是想让Maven对于属性的管理范围能更大些，比如我们之前项目中的`jdbc.properties`，这个配置文件中的属性，能不能也来让Maven进行管理呢？

答案是肯定的，具体的实现步骤如下：

1. 父工程定义属性

   在父工程maven_01_parent的properties标签中添加属性，属性标签自定义，这里我们定义为jdbc.url。

   ```xml
   <properties>
      <jdbc.url>jdbc:mysql://127.1.1.1:3306/ssm_db</jdbc.url>
   </properties>
   ```

2. jdbc.properties文件中引用属性

   在jdbc.properties，将jdbc.url的值直接获取Maven配置的属性

   ```properties
   jdbc.driver=com.mysql.jdbc.Driver
   jdbc.url=${jdbc.url}
   jdbc.username=root
   jdbc.password=root
   ```

3. 设置maven过滤文件范围

   扩大maven的控制范围，默认情况下我们上面定义的属性只能在pom文件中使用，不能够在配置文件下使用，所以我们需要使用插件来扩大maven控制范围。

   ```xml
   <build>
       <resources>
           <!--设置资源目录-->
           <resource>
               <directory>../maven_02_ssm/src/main/resources</directory>
               <!--设置能够解析${}，默认是false -->
               <filtering>true</filtering>
           </resource>
       </resources>
   </build>
   ```

   **说明:**directory路径前要添加`../`的原因是`maven_02_ssm`相对于父工程`maven_01_parent`的pom.xml路径是在其上一层的目录中，所以需要添加。

   修改完后，注意maven_02_ssm项目的resources目录就多了些东西，如下：

   ![1630977419627](..\图片\2-24【Maven】\1-14.png)

4. 测试是否生效

   测试的时候，只需要将`maven_02_ssm`项目进行打包，然后观察打包结果中最终生成的内容是否为Maven中配置的内容。

上面的属性管理就已经完成，但是有一个问题没有解决，因为不只是`maven_02_ssm`项目需要有属性被父工程管理，如果有多个项目需要配置，该如何实现呢？对此我们有两种解决方案：

* 第一种方式：

  ```xml
  <build>
      <resources>
          <!--设置资源目录，并设置能够解析${}-->
          <resource>
              <directory>../maven_02_ssm/src/main/resources</directory>
              <filtering>true</filtering>
          </resource>
          <resource>
              <directory>../maven_03_pojo/src/main/resources</directory>
              <filtering>true</filtering>
          </resource>
          ...
      </resources>
  </build>
  ```

  可以配，但是如果项目够多的话，这个配置也是比较繁琐

* 第二种方式：

  ```xml
  <build>
      <resources>
          <!--
  			${project.basedir}: 当前项目所在目录,子项目继承了父项目，
  			相当于所有的子项目都添加了资源目录的过滤
  		-->
          <resource>
              <directory>${project.basedir}/src/main/resources</directory>
              <filtering>true</filtering>
          </resource>
      </resources>
  </build>
  ```

  **说明:**打包的过程中如果报如下错误：

  ![1630948929828](..\图片\2-24【Maven】\1-15.png)

  原因就是Maven发现我们的项目为web项目，就会去找web项目的入口web.xml[配置文件配置的方式]，发现没有找到，就会报错。

  解决方案1：在`maven_02_ssm`项目的`src\main\webapp\WEB-INF\`添加一个web.xml文件

  ```
  <?xml version="1.0" encoding="UTF-8"?>
  <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
           version="3.1">
  </web-app>
  ```

  解决方案2：配置maven打包war时，忽略web.xml检查

  ```xml
  <build>
      <plugins>
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-war-plugin</artifactId>
              <version>3.2.3</version>
              <configuration>
                  <failOnMissingWebXml>false</failOnMissingWebXml>
              </configuration>
          </plugin>
      </plugins>
  </build>
  ```

上面我们所使用的都是Maven的自定义属性，除了${project.basedir},它属于Maven的内置系统属性。

在Maven中的属性分为:

- 自定义属性（常用）
- 内置属性
- Setting属性
- Java系统属性
- 环境变量属性

![1630981519370](..\图片\2-24【Maven】\1-16.png)

具体如何查看这些属性：在cmd命令行中输入`mvn help:system`

![1630981585748](..\图片\2-24【Maven】\1-17.png)

具体使用，就是使用 `${key}`来获取，key为等号左边的，值为等号右边的，比如获取红线的值，对应的写法为 `${java.runtime.name}`。

## 4.3 版本管理

关于这个版本管理解决的问题是，在Maven创建项目和引用别人项目的时候，我们都看到过如下内容：

![1630982018031](..\图片\2-24【Maven】\1-18.png)

这里面有两个单词，SNAPSHOT和RELEASE，它们所代表的含义是什么呢?

- SNAPSHOT（快照版本）
  
  项目开发过程中临时输出的版本，称为快照版本。
  
  快照版本会随着开发的进展不断更新。
  
- RELEASE（发布版本）
  
  项目开发到一定阶段里程碑后，向团队外部发布较为稳定的版本，这种版本所对应的构件文件是稳定的。
  
  即便进行功能的后续开发，也不会改变当前发布版本内容，这种版本称为发布版本

除了上面的工程版本，我们还经常能看到一些发布版本:

* alpha版：内测版，bug多不稳定内部版本不断添加新功能
* beta版：公测版，不稳定(比alpha稳定些)，bug相对较多不断添加新功能
* 纯数字版

# 第五章 多环境配置与私服

这一节中，我们会讲两个内容，分别是`多环境开发`和`跳过测试`

## 5.1 多环境开发

![1630983617755](..\图片\2-24【Maven】\1-19.png)

我们平常都是在自己的开发环境进行开发，当开发完成后，需要把开发的功能部署到测试环境供测试人员进行测试使用，等测试人员测试通过后，我们会将项目部署到生成环境上线使用。

这个时候就有一个问题是，不同环境的配置是不相同的，如不可能让三个环境都用一个数据库，所以就会有三个数据库的url配置，我们在项目中如何配置？要想实现不同环境之间的配置切换又该如何来实现呢？

maven提供配置多种环境的设定，帮助开发者在使用过程中快速切换环境。具体实现步骤：

1. 父工程配置多个环境,并指定默认激活环境

   ```xml
   <profiles>
       <!--开发环境-->
       <profile>
           <!--注意：这里使用的是id标签，因为要保证唯一，所以不能够使用name标签-->
           <id>env_dep</id>
           <properties>
               <jdbc.url>jdbc:mysql://127.1.1.1:3306/ssm_db</jdbc.url>
           </properties>
           <!--设定是否为默认启动环境-->
           <activation>
               <activeByDefault>true</activeByDefault>
           </activation>
       </profile>
       <!--生产环境-->
       <profile>
           <id>env_pro</id>
           <properties>
               <jdbc.url>jdbc:mysql://127.2.2.2:3306/ssm_db</jdbc.url>
           </properties>
       </profile>
       <!--测试环境-->
       <profile>
           <id>env_test</id>
           <properties>
               <jdbc.url>jdbc:mysql://127.3.3.3:3306/ssm_db</jdbc.url>
           </properties>
       </profile>
   </profiles>
   ```

2. 执行安装查看env_dep环境是否生效

   执行install命令，打开文件，查看到的结果为:

   ![](..\图片\2-24【Maven】\1-20.png)

3. 切换默认环境为生产环境

   ```xml
   <profiles>
       <!--开发环境-->
       <profile>
           <id>env_dep</id>
           <properties>
               <jdbc.url>jdbc:mysql://127.1.1.1:3306/ssm_db</jdbc.url>
           </properties>
       </profile>
       <!--生产环境-->
       <profile>
           <id>env_pro</id>
           <properties>
               <jdbc.url>jdbc:mysql://127.2.2.2:3306/ssm_db</jdbc.url>
           </properties>
           <!--设定是否为默认启动环境-->
           <activation>
               <activeByDefault>true</activeByDefault>
           </activation>
       </profile>
       <!--测试环境-->
       <profile>
           <id>env_test</id>
           <properties>
               <jdbc.url>jdbc:mysql://127.3.3.3:3306/ssm_db</jdbc.url>
           </properties>
       </profile>
   </profiles>
   ```

4. 执行安装并查看env_pro环境是否生效

   查看到的结果为`jdbc:mysql://127.2.2.2:3306/ssm_db`

虽然已经能够实现不同环境的切换，但是每次切换都是需要手动修改，如何来实现在不改变代码的前提下完成环境的切换呢？

这里我们可以使用命令行实现环境切换

![1630984476202](..\图片\2-24【Maven】\1-21.png)

执行安装并查看env_test环境是否生效，查看到的结果为`jdbc:mysql://127.3.3.3:3306/ssm_db`。

所以总结来说，对于多环境切换只需要两步即可：

* 父工程中定义多环境

  ```xml
  <profiles>
  	<profile>
      	<id>环境名称</id>
          <properties>
          	<key>value</key>
          </properties>
          <activation>
          	<activeByDefault>true</activeByDefault>
          </activation>
      </profile>
      ...
  </profiles>
  ```

* 使用多环境(构建过程)

  ```
  mvn 指令 -P 环境定义ID[环境定义中获取]
  ```

## 5.2 跳过测试

前面在执行`install`指令的时候，Maven都会按照顺序从上往下依次执行，每次都会执行`test`，对于`test`来说有它存在的意义：

* 可以确保每次打包或者安装的时候，程序的正确性，假如测试已经通过在我们没有修改程序的前提下再次执行打包或安装命令，由于顺序执行，测试会被再次执行，就有点耗费时间了。
* 功能开发过程中有部分模块还没有开发完毕，测试无法通过，但是想要把其中某一部分进行快速打包，此时由于测试环境失败就会导致打包失败。

遇到上面这些情况的时候，我们就想跳过测试执行下面的构建命令，具体实现方式有很多：

1. IDEA工具实现跳过测试

   ![1630985300814](..\图片\2-24【Maven】\1-22.png)

   图中的按钮为`Toggle 'Skip Tests' Mode`，Toggle翻译为切换的意思，也就是说在测试与不测试之间进行切换。

   点击一下，出现测试画横线的图片，如下:![1630985411766](..\图片\2-24【Maven】\1-23.png)

   说明测试已经被关闭，再次点击就会恢复。这种方式最简单，但是有点"暴力"，会把所有的测试都跳过，如果我们想更精细的控制哪些跳过哪些不跳过，就需要使用配置插件的方式。

2. 配置插件实现跳过测试

   在父工程中的pom.xml中添加测试插件配置

   ```xml
   <build>
       <plugins>
           <plugin>
               <artifactId>maven-surefire-plugin</artifactId>
               <version>2.12.4</version>
               <configuration>
                   <skipTests>false</skipTests>
                   <!--排除掉不参与测试的内容-->
                   <excludes>
                       <exclude>**/BookServiceTest.java</exclude>
                   </excludes>
               </configuration>
           </plugin>
       </plugins>
   </build>
   ```

   skipTests:如果为true，则跳过所有测试，如果为false，则不跳过测试

   excludes：哪些测试类不参与测试，即排除，针对skipTests为false来设置的

   includes: 哪些测试类要参与测试，即包含,针对skipTests为true来设置的

3. 命令行跳过测试

   使用Maven的命令行，`mvn 指令 -D skipTests`

注意事项:

* 执行的项目构建指令必须包含测试生命周期，否则无效果。例如执行compile生命周期，不经过test生命周期。
* 该命令可以不借助IDEA，直接使用cmd命令行进行跳过测试，需要注意的是cmd要在pom.xml所在目录下进行执行。

## 8.3 私服简介

首先来说一说什么是私服：

团队开发现状分析：

![1630987192620](..\图片\2-24【Maven】\1-24.png)

- 张三负责`ssm_crm`的开发，自己写了一个`ssm_pojo`模块，要想使用直接将`ssm_pojo`安装到本地仓库即可

- 李四负责`ssm_order`的开发，需要用到张三所写的`ssm_pojo`模块，这个时候如何将张三写的`ssm_pojo`模块交给李四呢?

- 如果直接拷贝，那么团队之间的jar包管理会非常混乱而且容器出错，这个时候我们就想能不能将写好的项目上传到中央仓库，谁想用就直接联网下载即可

- Maven的中央仓库不允许私人上传自己的jar包,那么我们就得换种思路，自己搭建一个类似于中央仓库的东西，把自己的内容上传上去，其他人就可以从上面下载jar包使用

- 这个类似于中央仓库的东西就是我们接下来要学习的私服


所以到这就有两个概念，一个是私服，一个是中央仓库

- 私服：公司内部搭建的用于存储Maven资源的服务器

- 远程仓库：Maven开发团队维护的用于存储Maven资源的服务器


所以说私服是一台独立的服务器，用于解决团队内部的资源共享与资源同步问题

搭建Maven私服的方式有很多，我们来介绍其中一种使用量比较大的实现方式：

* Nexus
  
  Sonatype公司的一款maven私服产品
  
  下载地址：https://help.sonatype.com/repomanager3/download

## 5.4 私服安装

1. 下载解压

   将`资料\latest-win64.zip`解压到一个空目录下。里面有两个文件夹，分别是：nexus-3.30.1-01和sonatype-work。两个都很重要，不能够删除。

2. 启动Nexus

   使用cmd进入到解压目录下的`nexus-3.30.1-01\bin`,执行如下命令：

   ```
   nexus.exe /run nexus
   ```

   看到如下内容，说明启动成功。

   ```asciiarmor
   Started Sonatype Nexus OSS 3.30.1-01
   ```

3. 浏览器访问

   访问地址为：`http://localhost:8081`

4. 首次登录重置密码

   这里我们设置成了`admin`--`admin`

至此私服就已经安装成功。如果要想修改一些基础配置信息，可以使用：

- 修改基础配置信息
  
  安装路径下etc目录中`nexus-default.properties`文件保存有nexus基础配置信息，例如默认访问端口。
  
- 修改服务器运行配置信息
  
  安装路径下bin目录中`nexus.vmoptions`文件保存有nexus服务器启动对应的配置信息，例如默认占用内存空间。

## 5.5 私服仓库分类

私服资源操作流程分析：

![1630989320979](..\图片\2-24【Maven】\1-25.png)

在没有私服的情况下，我们自己创建的服务都是安装在Maven的本地仓库中，私服中也有仓库，我们要把自己的资源上传到私服，最终也是放在私服的仓库中，其他人要想使用我们所上传的资源，就需要从私服的仓库中获取。

当我们要使用的资源不是自己写的，是远程中央仓库有的第三方jar包，这个时候就需要从远程中央仓库下载，每个开发者都去远程中央仓库下速度比较慢(中央仓库服务器在国外)

- 私服就再准备一个仓库，用来专门存储从远程中央仓库下载的第三方jar包，第一次访问没有就会去远程中央仓库下载，下次再访问就直接走私服下载

- 前面在介绍版本管理的时候提到过有`SNAPSHOT`和`RELEASE`，如果把这两类的都放到同一个仓库，比较混乱，所以私服就把这两个种jar包放入不同的仓库


上面我们已经介绍了有三种仓库，一种是存放`SNAPSHOT`的，一种是存放`RELEASE`还有一种是存放从远程仓库下载的第三方jar包，那么我们在获取资源的时候要从哪个仓库种获取呢？为了方便获取，我们将所有的仓库编成一个组，我们只需要访问仓库组去获取资源。

所有私服仓库总共分为三大类：

* **宿主仓库hosted** 

  保存无法从中央仓库获取的资源

  自主研发。第三方非开源项目，比如Oracle，因为是付费产品，所以中央仓库没有。

* **代理仓库proxy** 

  代理远程仓库，通过nexus访问其他公共仓库，例如中央仓库

* **仓库组group** 

  将若干个仓库组成一个群组，简化配置。仓库组不能保存资源，属于设计型仓库

![1630990244010](..\图片\2-24【Maven】\1-26.png)

## 5.6 本地仓库访问私服配置

我们通过IDEA将开发的模块上传到私服，中间是要经过本地Maven的，本地Maven需要知道私服的访问地址以及私服访问的用户名和密码。如图：

![1630990538229](..\图片\2-24【Maven】\1-27.png)

上面所说的这些内容，我们需要在本地Maven的配置文件`settings.xml`中进行配置。

1. 私服上配置仓库

   进入私服，选择齿轮`Server administration and configuration`(服务器管理和配置)设置使用。旁边的是浏览使用，我们这里选择齿轮，设置使用。

   选择右侧Repository仓库下面的Repositories储存库，点击上方的Create repository创建存储库。

   选择maven2 (hosted)，创建linxuan-snapshot仓库。在name里面输入`linxuan-snapshot`，将下方的Version policy（版本政策）选择Snapshot（快照）。最后直接点击创建。

   接下来再创建一个linxuan-release仓库，这个仓库的版本不用动，直接是release。

2. 配置本地Maven对私服的访问权限

   打开本地Maven的setting.xml文件，修改servers里面的内容，默认情况下servers里面内容被注释掉了，所以需要添加一些东西：

   ```xml
   <servers>
       <!--配置访问私服的权限-->
       <server>
           <!--私服中服务器id名称，我们上面创建的两个仓库名称。-->
           <id>linxuan-snapshot</id>
           <username>admin</username>
           <password>admin</password>
       </server>
       <server>
           <id>linxuan-release</id>
           <username>admin</username>
           <password>admin</password>
       </server>
   </servers>
   ```

3. 配置私服的访问路径

   上面没有对Maven说明私服的URL地址，所以接下来就是配置私服的访问路径：

   ```xml
   <mirrors>
       <!--配置私服的访问路径-->
       <mirror>
           <!--配置仓库组的ID-->
           <id>maven-public</id>
           <!--*代表所有内容都从私服获取-->
           <mirrorOf>*</mirrorOf>
           <!--私服仓库组maven-public的访问路径-->
           <url>http://localhost:8081/repository/maven-public/</url>
       </mirror>
   </mirrors>
   ```

   为了避免阿里云Maven私服地址的影响，建议先将之前配置的阿里云Maven私服镜像地址注释掉，等练习完后，再将其恢复。

   ![1630991535107](..\图片\2-24【Maven】\1-28.png)

至此本地仓库就能与私服进行交互了。

## 5.7 私服资源上传与下载

本地仓库与私服已经建立了连接，接下来我们就需要往私服上上传资源和下载资源，具体的实现步骤为：

第一步：配置工程上传私服的具体位置

```xml
 <!--配置当前工程保存在私服中的具体位置-->
<distributionManagement>
    <repository>
        <!--和maven/settings.xml中server中的id一致，表示使用该id对应的用户名和密码-->
        <id>linxuan-release</id>
         <!--release版本上传仓库的具体地址-->
        <url>http://localhost:8081/repository/linxuan-release/</url>
    </repository>
    <snapshotRepository>
        <!--和maven/settings.xml中server中的id一致，表示使用该id对应的用户名和密码-->
        <id>linxuan-snapshot</id>
        <!--snapshot版本上传仓库的具体地址-->
        <url>http://localhost:8081/repository/linxuan-snapshot/</url>
    </snapshotRepository>
</distributionManagement>
```

第二步：发布资源到私服

点击deploy或者执行Maven命令`mvn deploy`

**注意:**

要发布的项目都需要配置`distributionManagement`标签，要么在自己的pom.xml中配置，要么在其父项目中配置，然后子项目中继承父项目即可。

发布成功，在私服中就能看到：

![1630992513299](..\图片\2-24【Maven】\1-29.png)

现在发布是在linxuan-snapshot仓库中，如果想发布到linxuan-release仓库中就需要将项目pom.xml中的version修改成RELEASE即可。

如果想删除已经上传的资源，可以在界面上进行删除操作。

如果私服中没有对应的jar，会去中央仓库下载，速度很慢。可以配置让私服去阿里云中下载依赖。

![1630993028454](..\图片\2-24【Maven】\1-30.png)

至此私服的搭建就已经完成，相对来说有点麻烦，但是步骤都比较固定。
