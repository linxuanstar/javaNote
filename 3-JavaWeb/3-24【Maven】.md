<img src="..\图片\3-24【Maven】\0-1面试.png"/>

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
<!--配置阿里云镜像仓库-->
<mirror>
    <!--此镜像的唯一标识符号，用来区分不同的mirror元素-->
    <id>nexus-aliyun</id>
    <!--对哪种仓库进行镜像，简单来说就是替代哪个仓库-->
    <mirrorOf>central</mirrorOf>
    <!--镜像名称-->
    <name>Nexus aliyun</name>
    <!--镜像URL-->
    <!--<url>http://maven.aliyun.com/nexus/content/groups/public</url> 已经过时了-->
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

配置本地仓库：

```xml
<!-- 创建一个本地仓库 E:\Maven\apache-maven-3.6.0\mvn_repository文件夹，用于存放jar包 -->
<!-- 在settings.xml里面的localRepository下导入 -->
<localRepository>E:\Maven\apache-maven-3.6.0\mvn_repository</localRepository>
```

## 1.2 基本概念

**仓库**

maven 的工作需要从仓库下载一些 jar 包，本地的项目都会通过 maven 从远程仓库下载 jar 包并存在本地仓库，本地仓库就是本地文件夹，当第二次需要此 jar 包时则不再从远程仓库下载，因为本地仓库已经存在了，可以将本地仓库理解为缓存，有了本地仓库就不用每次从远程仓库下载了。

本地仓库 ：用来存储从远程仓库下载的插件和 jar 包。项目中使用一些插件或 jar 包优先从本地仓库查找默认本地仓库位置在 `${user.dir}/.m2/repository`，`${user.dir}`表示 windows 用户目录。我们可以修改。

远程仓库分为中央仓库和私服：

* 中央仓库 ：在 maven 软件中内置一个远程仓库地址 https://repo1.maven.org/maven2。它是中央仓库，服务于整个互联网。它是由 Maven 团队自己维护，里面存储了非常全的 jar 包，它包含了世界上大部分流行的开源项目构件。
* 私服：各公司/部门等小范围内存储资源的仓库，私服也可以从中央仓库获取资源。私服可以保存具有版权的资源，包含购买或自主研发的jar；一定范围内共享资源，能做到仅对内不对外开放。

**坐标**

我们说maven的仓库里存储了各种各样的资源（jar包），那这些资源我们如何找到它们呢？我们需要知道它们具体的一个位置才能知道如何找到它们，这个就叫坐标。https://mvnrepository.com/从这里获取坐标。

坐标：被Maven管理的资源的唯一标识。maven坐标（GAV）的主要组成如下：

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
    <!--版本号  release代表完成版，SNAPSHOT代表开发版-->
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
      |    |          |-- index.jsp/html/css/js # 存放前端资源文件
      |    |
      |    |-- test # 测试代码
      |         |-- java # 测试代码
      |         |-- resources # 测试代码所需要的配置文件
      |-- target # 
      |    |-- classes # 业务逻辑代码编译后的文件存放到这个目录下面
      |    |-- test-classes # 测试代码编译后的文件存放到这个目录下面
      |    |-- 项目.jar # 项目打包存放目录
      |
      |-- mvnw
      |-- mvnw.cmd
      |-- pom.xml # 项目的核心配置文件
```

## 1.3 基本命令

Maven命令使用mvn开头，后面添加功能参数，可以执行多个命令，使用空格分隔。

| 命令         | 作用                                             |
| ------------ | ------------------------------------------------ |
| mvn -version | 查看Maven版本。                                  |
| mvn compile  | 编译。将main目录下面java文件编译输出到target目录 |
| mvn test     | 测试。执行test目录下面单元测试类                 |
| mvn clean    | 清理。删除target目录内容                         |
| mvn package  | 打包                                             |
| mvn install  | 安装到本地仓库，不会安装测试代码                 |

`mvn dependency:get -DgroupId=XXX -DartifactId=XXX -Dversion=XXX`：读取maven配置，下载jar包到本地仓库。需要在下载的位置打开CMD窗口。

## 1.4 手动创建项目

**手动创建**

手动创建一个Maven项目，目录结构如下：

```apl
Maven-pro:
      |-- src # 源码
      |    |-- main # 主工程代码
      |    |    |-- java # 业务逻辑代码
      |    |         |-- com
      |    |              |-- linxuan
      |    |                   |-- Demo.java
      |    |    |-- resources # 业务逻辑代码配置文件
      |    |
      |    |-- test # 测试代码
      |         |-- java # 测试代码
      |              |-- com
      |                   |-- linxuan
      |                        |-- DemoTest.java
      |         |-- resources # 测试代码所需要的配置文件
      |-- pom.xml # 项目的核心配置文件
```

```java
package com.linxuan;

public class Demo{
	public String say(String name) {
		System.out.println("hello " + name);
		return "hello " + name;
	}
}
```

```java
package com.linxuan;

import org.junit.Test;
import org.junit.Assert;

public class DemoTest{
	
	@Test
	public void testSay() {
		Demo d = new Demo();
		String ret = d.say("maven");
		Assert.assertEquals("hello maven", ret);
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
		 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <packaging>jar</packaging>
    
    <groupId>com.linxuan</groupId>
    <artifactId>Maven-pro</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <dependencies>
		<!-- 单元测试 -->
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.11</version>
		</dependency>
    </dependencies>
</project>
```

这样一个Maven工程就创建好了。之后就可以在Maven-pro目录下面使用Maven命令了。

**插件创建**

```sh
mvn archetype:generate # 使用插件生成
    -DgroupId={project-packaging} 
    -DartifactId={project-name} 
    -DarchetypeArtifactId=maven-archetype-quickstart # 指定生成项目的模板
    -Dversion=0.0.1-snapshot # 指定版本号
    -DinteractiveMode=false
```

```sh
# 创建一个java工程 使用的是maven-archetype-quickstart模板 项目名称为java-project
mvn archetype:generate -DgroupId=com.linxuan -DartifactId=java-project -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0-SNAPSHOT -DinteractiveMode=false
```

```sh
# 创建一个web工程 使用的是maven-archetype-webapp模板
mvn archetype:generate -DgroupId=com.linxuan -DartifactId=web-project -DarchetypeArtifactId=maven-archetype-webapp -Dversion=1.0-SNAPSHOT -DinteractiveMode=false
```

## 1.5 Idea创建Maven项目

创建一个空的项目，不使用任何的构建工具。项目名称为maven-project。然后设置项目运行SDK。

**Idea集成Maven**

<img src="..\图片\3-24【Maven】\1-1Idea集成Maven.png"/>

**创建Maven项目**

这里因为已经创建了一个项目了，所以是在该项目下面创建一个Maven模块。右键项目名称，创建模块。

<img src="..\图片\3-24【Maven】\1-2创建项目.png"/>

<img src="..\图片\3-24【Maven】\1-2创建项目2.png"/>

**使用Maven控制面板执行命令**

<img src="..\图片\3-24【Maven】\1-3Idea执行命令.png"/>

**使用配置文件执行命令**

<img src="..\图片\3-24【Maven】\1-4配置文件编写命令.png"/>

**Idea创建Web项目**

与普通的项目步骤一样，只是在模板上更换为了`org.apache.maven.archetypes:maven-archetype-webapp`。创建好项目之后就是集成Tomcat，这里我们使用插件。

打开Maven网路仓库，搜索`tomcat maven`，找到包路径为`org.apache.tomcat.maven`的tomcat插件，挑选版本。

```xml
<!-- 添加到pom.xml的build标签下面的plugins标签下面的plugin标签下面-->
<!-- https://mvnrepository.com/artifact/org.apache.tomcat.maven/tomcat7-maven-plugin -->
<groupId>org.apache.tomcat.maven</groupId>
<artifactId>tomcat7-maven-plugin</artifactId>
<version>2.1</version>
```

启动该项目只需要点击Plugins-->tomcat7-->tomcat7:run即可。

当然对于tomcat插件我们也可以进一步修改

```xml
<!--构建-->
<build>
    <!--设置插件-->
    <plugins>
        <!--具体的插件配置-->
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.1</version>
            <configuration>
                <!--端口号-->
                <port>80</port>
                <!--虚拟路径-->
                <path>/</path>
            </configuration>
        </plugin>
    </plugins>
</build>
```

同样的，也可以使用配置文件执行命令

<img src="..\图片\3-24【Maven】\1-5tomcat7.png"/>

```xml
<!--配置maven打包war时，忽略web.xml检查-->
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

## 1.6 生命周期和插件

同一套生命周期中，执行后边的操作，会自动执行之前所有操作。

maven 对项目构建过程分为三套相互独立的生命周期，请注意这里说的是“三套”，而且“相互独立”。每套声明周期执行之前会将前面的命令执行。这三套生命周期分别是：

- `Clean Lifecycle`： 在进行真正的构建之前进行一些清理工作。 

  | Clean声明周期阶段 | 作用                              |
  | ----------------- | --------------------------------- |
  | pre-clean         | 执行一些需要在clean之前完成的工作 |
  | clean             | 移除上一次构建产生的所有文件      |
  | post-clean        | 执行一些在clean之后立刻完成的工作 |

- `Default Lifecycle`： 构建的核心部分，编译，测试，打包，部署等等。 

  | 阶段                                        | 描述                                               |
  | ------------------------------------------- | -------------------------------------------------- |
  | validate（校验）                            | 验证项目是否正确以及所有必要信息是否可用。         |
  | initialize（初始化）                        | 初始化构建状态。                                   |
  | generate-sources（生成源代码）              | 生成编译阶段需要的所有源码文件。                   |
  | process-sources（处理源代码）               | 处理源码文件，例如过滤某些值。                     |
  | generate-resources（生成资源文件）          | 生成项目打包阶段需要的资源文件。                   |
  | process-resources（处理资源文件）           | 复制和处理资源到输出目录，为打包阶段做准备。       |
  | **compile**（编译）                         | 编译源代码，并移动到输出目录。                     |
  | process-classes（处理类文件）               | 处理编译生成的字节码文件                           |
  | generate-test-sources（生成测试源代码）     | 生成编译阶段需要的测试源代码。                     |
  | process-test-sources（处理测试源代码）      | 处理测试资源，并复制到测试输出目录。               |
  | generate-test-resources（生成测试资源文件） | 为测试创建资源文件                                 |
  | process-test-resources（处理测试资源文件）  | 复制和处理测试资源到目标目录                       |
  | **test-compile**（编译测试源代码）          | 编译测试源代码并移动到测试输出目录中。             |
  | process-test-classes（处理测试类文件）      | 处理测试源码编译生成的文件                         |
  | **test**（测试）                            | 使用适当的单元测试框架（例如 JUnit）运行测试。     |
  | prepare-package（准备打包）                 | 在真正打包之前，执行一些必要的操作。               |
  | **package**（打包）                         | 获取编译后的代码，并按照可发布的格式进行打包。     |
  | pre-integration-test（集成测试前）          | 集成测试执行前，执行所需的操作，例如设置环境变量。 |
  | integration-test（集成测试）                | 处理和部署所需的包到集成测试能够运行的环境中。     |
  | post-integration-test（集成测试后）         | 在集成测试被执行后执行必要的操作，例如清理环境。   |
  | verify（验证）                              | 对集成测试的结果进行检查，以保证质量达标。         |
  | **install**（安装）                         | 安装打包的项目到本地仓库，以供其他项目使用。       |
  | deploy（部署）                              | 拷贝最终的包文件到远程仓库中。                     |

- `Site Lifecycle`：生成项目报告，站点，发布站点。

  | Site声明周期阶段 | 描述                                               |
  | ---------------- | -------------------------------------------------- |
  | pre-site         | 执行一些在生成站点文档之前的工作                   |
  | site             | 生成项目的站点文档                                 |
  | post-site        | 执行一些在生成站点文档之后完成的工作，为部署做准备 |
  | site-deploy      | 将生成的站点文档部署到特定的服务器上               |

插件与生命周期内的阶段绑定，在执行到对应生命周期时执行对应的插件。maven默认在各个生命周期上都绑定了预先设定的插件来完成相应功能，插件还可以完成一些自定义功能。Maven官网插件对应网站：https://maven.apache.org/plugins/index.html

我们来测试一下打源码包的插件：

```xml
<build>
    <plugins>
        <!--打源码包的插件-->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
            <version>2.2.1</version>
            <!--该插件执行的时候以及动作-->
            <executions>
                <execution>
                    <!--执行的动作-->
                    <goals>
                        <!--jar是对main代码打包、test-jar是对test代码打包-->
                        <goal>jar</goal>
                    </goals>
                    <!--声明周期阶段 也就是到该阶段就要运行插件了-->
                    <phase>generate-test-resources</phase>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

将该插件添加Idea之后

<img src="..\图片\3-24【Maven】\1-6添加插件.png"/>

这时候我们就可以执行声明周期（lifecycle）来测试了：

* 首先执行`clean`将target目录清理掉。
* 执行`compile`编译将main目录下面编译到target目录下面。这时候target目录下面有两个目录：classes、maven-status。
* 执行`test`测试，测试test目录下面的代码。可以发现这时候target目录下面有四个目录和一个jar包：classes、maven-status、surefire-reports、test-classes、web01-1.0-SNAPSHOT-sources.jar。正常来说执行test阶段会将该生命周期前面的阶段都执行一下，但是前面并没有打包的阶段，这里偏偏就出现了一个源码包。这就是插件起作用了，当执行到`generate-test-resources`阶段就会执行打源码包操作。

# 第二章 依赖管理

依赖指当前项目运行所需的jar，一个项目可以设置多个依赖。

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

## 2.1 依赖传递与冲突

传递依赖：如果我们的项目引用了一个Jar包，而该Jar包又引用了其他Jar包，那么在默认情况下项目编译时，Maven会把直接引用和间接引用的Jar包都下载到本地。

因为有依赖传递的存在，就会导致jar包在依赖的过程中出现冲突问题。这里所说的依赖冲突是指项目依赖的某一个jar包，有多个不同的版本，因而造成类包版本冲突。

若项目中多个Jar同时引用了相同的Jar时，会产生依赖冲突，但Maven采用了三种避免冲突的策略，因此在Maven中是不存在依赖冲突的。

1. 特殊优先：当同级配置了相同资源的不同版本，后配置的覆盖先配置的。

2. 路径优先：当依赖中出现相同的资源时，层级越深，优先级越低，层级越浅，优先级越高

   ```apl
   # Maven只会引用引用路径最短的Jar。
   # 本项目直接依赖于A.jar、C.jar，间接依赖于B.jar、X.jar。
   本项目——>A.jar——>B.jar——>X.jar
   本项目——>C.jar——>X.jar
   ```
   


3. 声明优先：当资源在相同层级被依赖时，配置顺序靠前的覆盖配置顺序靠后的

上面这些结果，我们不需要刻意去记它。因为不管Maven怎么选，最终的结果都会在Maven的`Dependencies`面板中展示出来。如果想更全面的查看Maven中各个坐标的依赖关系，可以点击Maven面板中的`show Dependencies`

<img src="..\图片\3-24【Maven】\2-1依赖传递及冲突.png"/>

在这个视图中就能很明显的展示出jar包之间的相互依赖关系。

## 2.2 可选依赖与排除依赖

简单梳理下，就是

* `A依赖B,B依赖C`,`C`通过依赖传递会被`A`使用到，现在要想办法让`A`不去依赖`C`
* 可选依赖是在B上设置`<optional>`,`A`不知道有`C`的存在。
* 排除依赖是在A上设置`<exclusions>`,`A`知道有`C`的存在，主动将其排除掉。

**可选依赖**

可选依赖指对外隐藏当前所依赖的资源——不透明

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <!-- 可选依赖是隐藏当前工程所依赖的资源，隐藏后对应资源将不具有依赖传递 默认为false-->
    <optional>true</optional>
</dependency>
```

**排除依赖**

排除依赖指主动断开依赖的资源，被排除的资源无需指定版本---不需要

```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>3.8.1</version>
        <!--排除依赖是隐藏当前资源对应的依赖关系-->
        <exclusions>
            <exclusion>
                <groupId>org.hamcrest</groupId>
                <artifactId>hamcrest-core</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

## 2.3 依赖范围

```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>3.8.1</version>
        <!-- 指定作用范围 -->
        <scope>test</scope>
    </dependency>
</dependencies>
```

依赖的jar默认情况可以在任何地方可用，可以通过`scope`标签设定其作用范围。`scope`标签的取值一共有四种`compile`、`test`、`provided`、`runtime`。这里的范围主要是指以下三种范围：

1. 主程序范围有效（src/main目录范围内）
2. 测试程序范围内有效（src/test目录范围内）。主程序不能够使用，打包的时候也不参与进去。
3. 是否参与打包（package指令范围内）

四种取值与范围的对应情况如下：

| scope           | 主代码 | 测试代码 | 打包 | 范例        |
| --------------- | ------ | -------- | ---- | ----------- |
| compile（默认） | Y      | Y        | Y    | Log4j       |
| test            |        | Y        |      | junit       |
| provided        | Y      | Y        |      | servlet-api |
| runtime         |        |          | Y    | jdbc        |

带有依赖范围的资源在进行传递的时候，作用范围将受到影响。如下表，空的是不传递。

| 横\竖为直接依赖\间接依赖 | compile | test | provided | runtime |
| ------------------------ | ------- | ---- | -------- | ------- |
| **compile**              | compile | test | provided | runtime |
| **test**                 |         |      |          |         |
| **provided**             |         |      |          |         |
| **runtime**              | runtime | test | provided | runtime |

# 第三章 SpringMVC分模块开发

分模块开发有两种拆分方式：按照功能拆分和按照模块拆分。将原始模块按照功能拆分成若干个子模块，这样方便模块间的相互调用，接口共享。

我们可以将其他的层也拆成一个个对立的模块，如：

<img src="..\图片\3-24【Maven】\3-1分模块划分.png"/>

这样的话，项目中的每一层都可以单独维护，也可以很方便的被别人使用。

## 3.1 抽取POJO实体层

步骤很简单，直接新建一个模块（目录直接放到根目录下面，不要放到原始项目目录下面），然后创建对应的包，最后将domain包下面的所有类复制过去就行了。

<img src="..\图片\3-24【Maven】\3-2pojo实体类拆分.png"/>

## 3.2 抽取Dao数据层

新建模块，然后拷贝原始项目中相应的相关内容到ssm_dao模块中即可。这个里面需要拷贝的内容有：数据层接口、配置文件、pom.xml文件依赖的坐标。

<img src="..\图片\3-24【Maven】\3-3dao拆分.png"/>

复制完数据层接口接口之后发现导入的依赖并没有，所以会报错。因此我们修改一下原始项目的pom.xml文件，粘贴到ssm-dao模块中。

原始项目pom.xml依赖及插件如下：

* mybatis环境：mybatis环境、mysql环境、spring整合jdbc、spring整合mybatis、druid连接池、分页插件坐标、
* springmvc环境：springmvc环境、jackson相关坐标、servlet环境、
* 其他组件：junit单元测试、spring整合junit
* 插件：tomcat7-maven-plugin

因为实在数据层dao里面，所以会用到mybatis环境。springmvc环境和tomcat7插件删掉即可，而junit单元测试可留可不留（数据层做完可以来单元测试）。最后在加上spring的环境，之前因为有springmvc环境，它依赖于spring的环境，这下将springmvc环境删掉了，所以这里添加上spring的环境。

最后pom.xml文件如下：

```xml
<dependencies>
    <!--spring环境-->
    <!--spring环境-->
    <!--spring环境-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>

    <!--mybatis环境-->
    <!--mybatis环境-->
    <!--mybatis环境-->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.3</version>
    </dependency>
    <!--mysql环境-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.47</version>
    </dependency>
    <!--spring整合jdbc-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>
    <!--spring整合mybatis-->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>2.0.3</version>
    </dependency>
    <!--druid连接池-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!--分页插件坐标-->
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper</artifactId>
        <version>5.1.2</version>
    </dependency>
</dependencies>
```

接下来来修改一下配置文件，resources目录下面有：数据层操控数据库文件userDao.xml、Spring配置文件applicationContext.xml、数据库连接配置文件jdbc.properties、SpringMVC配置文件spring-mvc.xml。

这些配置文件中userDao.xml不用动，jdbc.properties修改为自己数据库信息，spring-mvc.xml直接删除，所以我们只需要动一下applicationContext.xml即可。

```xml
<!-- applicationContext.xml更名为applicationContext-dao.xml 防止和service模块重名现象-->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/tx 
                           http://www.springframework.org/schema/tx/spring-tx.xsd
                           ">
    <!--主程序对应的配置文件-->
    <!--主程序对应的配置文件-->

    <!--开启bean注解扫描 排除Controller包-->
    <context:component-scan base-package="com.linxuan">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

    <!--开启注解式事务-->
    <tx:annotation-driven transaction-manager="txManager"/>

    <!--加载properties文件-->
    <context:property-placeholder location="classpath*:jdbc.properties"/>

    <!--数据源-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!--整合mybatis到spring中-->
    <bean class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="com.linxuan.domain"/>
        <!--分页插件-->
        <property name="plugins">
            <array>
                <bean class="com.github.pagehelper.PageInterceptor">
                    <property name="properties">
                        <props>
                            <prop key="helperDialect">mysql</prop>
                            <prop key="reasonable">true</prop>
                        </props>
                    </property>
                </bean>
            </array>
        </property>
    </bean>

    <!--映射扫描-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.linxuan.dao"/>
    </bean>

    <!--事务管理器-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

</beans>
```

修改为如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--稍微修改一下约束-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <!--主程序对应的配置文件-->
    <!--主程序对应的配置文件-->

    <!--开启bean注解扫描 不需要过滤掉Controller层 这里根本没有-->
    <context:component-scan base-package="com.linxuan"/>

    <!--不需要在dao层开启事务-->

    <!--加载properties文件-->
    <context:property-placeholder location="classpath*:jdbc.properties"/>

    <!--数据源-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!--整合mybatis到spring中-->
    <bean class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="com.linxuan.domain"/>
        <!--分页插件-->
        <property name="plugins">
            <array>
                <bean class="com.github.pagehelper.PageInterceptor">
                    <property name="properties">
                        <props>
                            <prop key="helperDialect">mysql</prop>
                            <prop key="reasonable">true</prop>
                        </props>
                    </property>
                </bean>
            </array>
        </property>
    </bean>

    <!--映射扫描-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.linxuan.dao"/>
    </bean>

    <!--也不需要留事务管理器-->
</beans>
```

最后就是在pom.xml中导入一下实体层，并且将ssm_pojo使用install命令将其安装到仓库中供ssm_dao模块调用。

```xml
<!--实体层-->
<dependency>
    <groupId>com.linxuan</groupId>
    <artifactId>ssm_pojo</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

最后测试一下整个环节是否打通只需要在ssm_dao模块执行compile编译命令看看是否报错。

## 3.3 抽取service业务层

新建模块，然后拷贝原始项目中相应的相关内容到ssm_service模块中即可。这个里面需要拷贝的内容有：业务层接口及实现类、配置文件、pom.xml文件依赖的坐标、测试代码、测试代码配置文件。

<img src="..\图片\3-24【Maven】\3-4service拆分.png"/>

首先来导入依赖，原始项目pom.xml依赖及插件如下：

* mybatis环境：mybatis环境、mysql环境、spring整合jdbc、spring整合mybatis、druid连接池、分页插件坐标、
* springmvc环境：springmvc环境、jackson相关坐标、servlet环境、
* 其他组件：junit单元测试、spring整合junit
* 插件：tomcat7-maven-plugin

因为是在业务侧层service里面，所以将mybatis环境、springmvc环境和tomcat7插件删掉即可，而junit单元测试留下，这里我们会做一下单元测试。最后在加上spring的环境和数据层的依赖即可（不要忘记让dao层install一下）。

```xml
<dependencies>
    <!--导入dao层依赖-->
    <dependency>
        <groupId>com.linxuan</groupId>
        <artifactId>ssm_dao</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!--spring环境-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>


    <!--其他组件-->
    <!--junit单元测试-->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
    <!--spring整合junit-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>
</dependencies>
```

接下来修改配置文件applicationContext-service.xml，里面配置有：bean注解扫描、事务配置、mybatis配置。只需要保留bean注解扫描和事务配置即可，bean注解扫描也需要动一下。

```xml
<!--主程序对应的配置文件-->
<!--开启bean注解扫描-->
<context:component-scan base-package="com.linxuan"/>

<!--开启注解式事务-->
<tx:annotation-driven transaction-manager="txManager"/>

<!--事务管理器-->
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <!--这里表示引用外部bean，dataSource会报红，不过不用担心，在dao层里面有，我们已经导入dao层的依赖了-->
    <property name="dataSource" ref="dataSource"/>
</bean>
```

到这里已经完成一大半了，主要业务代码已经弄好了，可以编译一下看看是否成功。

接下来就是修改测试代码了。测试代码的配置文件和主业务代码的配置文件并不相同，所以这里复制主业务代码配置文件到测试代码位置，然后将原来的删掉。

相应的测试代码也需要进行修改，因为配置文件里面只配置了业务层的配置，并没有配置数据层的配置。对此有两种解决方法：导入两个配置文件（业务层和数据层）、补全配置文件（业务层配置文件添加上数据层的配置）。这里采用第一种：

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-service.xml", "classpath:applicationContext-dao.xml"})
```

之后test一下，执行所有的测试代码，发现成功。然后将service模块安装到本地仓库就可以了。

## 3.4 抽取Controller表现层

根据web模板创建模块。

<img src="..\图片\3-24【Maven】\3-5controller拆分.png"/>

pom.xml添加依赖及插件

```xml
<dependencies>
    <!--springmvc环境-->
    <!--springmvc环境-->
    <!--springmvc环境-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>
    <!--jackson相关坐标3个-->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.9.0</version>
    </dependency>
    <!--<dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.9.0</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>2.9.0</version>
        </dependency>-->
    <!--servlet环境-->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <!--设置插件-->
    <plugins>
        <!--具体的插件配置-->
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.1</version>
            <configuration>
                <port>80</port>
                <path>/</path>
            </configuration>
        </plugin>
    </plugins>
</build>
```

web.xml里面有一个配置信息需要修改：

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <!--
        原始为：<param-value>classpath*:applicationContext.xml</param-value>
        因为这里我们将Spring的配置文件拆分了，一个是Dao层，一个是Service层，所以这里让他全部导入。
    -->
    <!--<param-value>classpath*:applicationContext.xml</param-value>-->
    <param-value>classpath*:applicationContext-*.xml</param-value>
</context-param>
```

springmvc配置文件spring-mvc.xml不需要动

```xml
<!--驱动-->
<mvc:annotation-driven/>
<!--包扫描-->
<context:component-scan base-package="com.linxuan.controller"/>
```

编译通过，然后tomcat插件运行，最后拿Postman测试一下即可。新增POST：`localhost:8080/user?userName=jack2&password=1234&realName=jack3&gender=1&1999/09/09`

# 第四章 Maven高级

## 4.1 聚合和继承

上面项目分模块开发后，通过项目Maven面板的`install`来安装到本地仓库，如果我们的项目足够多，那么一个个安装起来还是比较麻烦的。并且当其中一个模块发生变化，为了确保不会影响到其他项目模块，需要对所有的模块进行重新编译安装。这样就太麻烦了，这就用到了我们接下来要讲解的聚合和继承。

聚合和继承的工程构建，需要在聚合项目中手动添加`modules`标签，需要在所有的子项目中添加`parent`标签。

* 聚合用于快速构建项目，对项目进行管理
* 继承用于快速配置和管理子项目中所使用jar包的版本

聚合和继承的相同点:

* 聚合与继承的pom.xml文件打包方式均为pom，可以将两种关系制作到同一个pom文件中
* 聚合与继承均属于设计型模块，并无实际的模块内容

聚合和继承的不同点:

* 聚合是在当前模块中配置关系，聚合可以感知到参与聚合的模块有哪些
* 继承是在子模块中配置关系，父模块无法感知哪些子模块继承了自己

**聚合**

所谓聚合就是将多个模块组织成一个整体，同时进行项目构建的过程称为聚合。聚合工程通常是一个不具有业务功能的"空"工程（有且仅有一个pom文件）。聚合工程主要是用来管理项目。

使用聚合工程可以将多个工程编组，通过对聚合工程进行构建，实现对所包含的模块进行同步构建。

实现步骤为：不用任何模板创建一个空的maven项目ssm、将项目的打包方式改为pom、pom.xml添加所要管理的项目。这样以后执行命令的时候只需要执行父工程的命令就行了。

```xml
<!--父工程项目坐标GAV-->
<groupId>com.linxuan</groupId>
<artifactId>ssm</artifactId>
<version>1.0-SNAPSHOT</version>
<!--将项目的打包方式改为pom-->
<packaging>pom</packaging>

<!-- pom.xml添加所要管理的项目 设置管理的模块名称-->
<modules>
    <module>../ssm_controller</module>
    <module>../ssm_service</module>
    <module>../ssm_dao</module>
    <module>../ssm_pojo</module>
</modules>
```

聚合工程管理的项目在进行运行的时候，会按照项目与项目之间的依赖关系来自动决定执行的顺序和配置的顺序无关。

**继承**

继承用于快速配置和管理子项目中所使用jar包的版本。子工程可以继承父工程中的配置信息，常见于依赖关系的继承。这样可以简化配置、减少版本冲突。

实现步骤为：不用任何模板创建一个空的父工程maven项目、将项目的打包方式改为pom、在子项目中设置父项目pom.xml位置、将子项目共同使用的jar包抽取出来维护在父项目的pom.xml中。这样就实现了继承。

```xml
<!--配置当前工程继承自parent工程-->
<parent>
    <groupId>com.linxuan</groupId>
    <artifactId>ssm_controller</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!-- 设置父项目pom.xml位置路径，如果是<relativePath/>那么代表从仓库里面找 -->
    <relativePath>../ssm/pom.xml</relativePath>
</parent>
```

如果把所有用到的jar包都管理在父项目的pom.xml，看上去更简单些，但是这样就会导致有很多项目引入了过多自己不需要的jar包。针对于这种部分项目有的jar包，我们可以在父工程定义依赖管理：

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

`<dependencyManagement>`标签不真正引入jar包，而是配置可供子项目选择的jar包依赖。子项目要想使用它所提供的这些jar包，需要自己添加依赖，并且不需要指定`<version>`。然后在子工程里面引入依赖即可：

```xml
<!--这里就不需要添加版本了，当父工程中的版本发生变化后，子项目中的依赖版本也会跟着发生变化-->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <scope>test</scope>
</dependency>
```

继承的资源如下：

| 继承标签                | 作用         | 继承标签             | 作用                                                       |
| ----------------------- | ------------ | -------------------- | ---------------------------------------------------------- |
| groupId                 | 项目组 ID    |                      |                                                            |
| version                 | 项目版本     | ciManagement         | 持续继承信息                                               |
| description             | 描述信息     | scm                  | 版本控制信息                                               |
| organization            | 组织信息     | mailingListserv      | 邮件列表信息                                               |
| inceptionYear           | 创始年份     | properties           | 自定义的 Maven 属性                                        |
| url                     | url 地址     | dependencies         | 依赖配置                                                   |
| develoers               | 开发者信息   | dependencyManagement | 依赖管理配置                                               |
| contributors            | 贡献者信息   | repositories         | 仓库配置                                                   |
| distributionManagerment | 部署信息     | build                | 源码目录配置、输出目录配置、<br />插件配置、插件管理配置等 |
| issueManagement         | 缺陷跟踪信息 | reporting            | 报告输出目录配置、报告插件配置等                           |

## 4.2 属性properties

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```

在Maven中的属性分为：自定义属性（常用）、内置属性、Setting属性、Java系统属性、环境变量属性。

| 属性分类     | 引用格式                     | 实例                        |
| ------------ | ---------------------------- | --------------------------- |
| 自定义属性   | ${自定义属性名称}            | ${spring.version}           |
| 内置属性     | ${内置属性名称}              | ${basedir}     ${version}   |
| Setting属性  | ${settings.属性名称}         | ${settings.localRepository} |
| Java系统属性 | ${系统属性分类.系统属性名称} | ${user.home}                |
| 环境变量属性 | ${env.环境变量属性名称}      | ${env.JAVA_HOME}            |

可以在cmd命令行中输入`mvn help:system`查看Java系统属性和环境变量属性 

```apl
C:\Windows\System32>mvn help:system
===============================================================================
System Properties
===============================================================================

java.runtime.name=Java(TM) SE Runtime Environment
sun.boot.library.path=E:\JAVA\jdk1.8.0_144\jre\bin
java.vm.version=25.144-b01
java.vm.vendor=Oracle Corporation...
```

接下来详细介绍一下自定义属性，自定义属性会继续解决分模块开发项目存在的问题。

**自定义属性**

前面我们已经在父工程中的dependencyManagement标签中对项目中所使用的jar包版本进行了统一的管理，但是如果在父工程的标签中有如下的内容：

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
```

这时想更新Spring的版本，依然需要更新多个jar包的版本。这时候就用到了自定义属性，用properties标签定义属性设置版本，其他的地方应用这个版本值。这样更改的时候只需要更改properties标签内容即可。

```xml
<!-- 定义属性 设置版本-->
<properties>
    <!--这里的标签命名自定义，但是习惯是技术栈名称+.version-->
    <spring.version>5.2.10.RELEASE</spring.version>
    <junit.version>4.12</junit.version>
    <mybatis-spring.version>1.3.0</mybatis-spring.version>
</properties>
```

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

**配置文件加载属性**

让Maven对于属性的管理范围能更大些，比如我们之前项目中的`jdbc.properties`这个配置文件中的属性，也让Maven进行管理。具体的实现步骤如下：

1. 父工程定义属性。在父工程ssm的properties标签中添加属性，属性标签自定义，这里我们定义为jdbc.url。

   ```xml
   <properties>
      <jdbc.url>jdbc:mysql://127.1.1.1:3306/ssm_db</jdbc.url>
   </properties>
   ```

2. jdbc.properties文件中引用属性。在jdbc.properties，将jdbc.url的值直接获取Maven配置的属性

   ```properties
   jdbc.driver=com.mysql.jdbc.Driver
   jdbc.url=${jdbc.url}
   jdbc.username=root
   jdbc.password=root
   ```

3. 父工程设置maven过滤文件范围。扩大maven的控制范围，默认情况下我们上面定义的属性只能在pom文件中使用，不能够在配置文件下使用，所以我们需要使用插件来扩大maven控制范围。

   ```xml
   <build>
       <!--这里标签是resources代表是主文件代码里面地配置文件，如果改为testResources是test里面地配置文件-->
       <resources>
           <!--设置资源目录-->
           <resource>
               <!--最后是在ssm_dao模块下面地jdbc.properties里面解析-->
               <directory>../ssm_dao/src/main/resources</directory>
               <!--设置能够解析${}，默认是false -->
               <filtering>true</filtering>
           </resource>
       </resources>
   </build>
   ```

4. 测试是否生效。测试的时候，只需要将`ssm`项目进行打包，然后观察打包结果中最终生成的内容是否为Maven中配置的内容。

如果有多个项目需要有属性被父工程管理，对此我们有两种解决方案：设置多个资源目录并让它能够解析`${}`、使用通配符。这里来说明一下第二种：

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

## 4.3 项目版本管理

关于这个版本管理解决的问题是，在Maven创建项目和引用别人项目的时候，我们都看到过如下内容：

```xml
<!--自己编写的项目ssm_controller-->
<groupId>com.linxuan</groupId>
<artifactId>ssm_controller</artifactId>
<version>1.0-SNAPSHOT</version>

<!--引入的SpringMVCjar包-->
<groupId>org.springframework</groupId>
<artifactId>spring-webmvc</artifactId>
<version>5.1.9.RELEASE</version>
```

SNAPSHOT（快照版本）：项目开发过程中临时输出的版本，称为快照版本。快照版本会随着开发的进展不断更新。

RELEASE（发布版本）：项目开发到一定阶段里程碑后，向团队外部发布较为稳定的版本，这种版本所对应的构件文件是稳定的。即便进行功能的后续开发，也不会改变当前发布版本内容，这种版本称为发布版本。

除了上面的工程版本，我们还经常能看到一些发布版本:

* alpha版：内测版。bug多、不稳定、内部版本、不断添加新功能。
* beta版：公测版。不稳定(比alpha稳定些)，bug相对较多、不断添加新功能。
* 纯数字版

工程版本号约定规范：`<主版本>.<此版本>.<增量版本>.<里程碑版本>`。例如：5.1.9.RELEASE。

* 主版本：表示项目重大架构的变更。例如Spring5相较于Spring4的迭代。
* 次版本：表示有较大的功能增加和变化，或者全面系统地修复漏洞。
* 增量版本：表示有重大漏洞地修复。
* 里程碑版本：表示一个版本地里程碑（版本内部）。这样地版本同下一个正式版本对比，相对来说不是很稳定，有待更多地测试。

## 4.4 多环境开发配置

我们平常都是在自己的开发环境进行开发，当开发完成后，需要把开发的功能部署到测试环境供测试人员进行测试使用，等测试人员测试通过后，会将项目部署到生成环境上线使用。

这个时候就有一个问题是，不同环境的配置是不相同的，如不可能让三个环境都用一个数据库，所以就会有三个数据库的url配置，我们在项目中如何配置？要想实现不同环境之间的配置切换又该如何来实现呢？

maven提供配置多种环境的设定，帮助开发者在使用过程中快速切换环境。

```xml
<!--父工程配置多个环境，并指定默认激活环境-->
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

可以通过install命令安装包到本地进行查看是否能够进行切换。虽然已经能够实现不同环境的切换，但是每次切换都是需要手动修改，当然也可以使用命令行实现环境切换：

```sh
mvn 指令 -P 环境定义ID[环境定义中获取]
```

<img src="..\图片\3-24【Maven】\4-1调出命令行.png"/>

## 4.5 跳过测试

前面在执行`install`指令的时候，Maven都会执行`Default Lifecycle`这个声明周期，每一次都会执行`test`。对于`test`来说有它存在的意义：可以确保每次打包或者安装的时候，程序的正确性。

但是假如测试已经通过，在我们没有修改程序的前提下再次执行打包或安装命令，测试会被再次执行，这就有点耗费时间了。或者功能开发过程中有部分模块还没有开发完毕，这样就会导致测试无法通过，但是想要把其中某一部分进行快速打包，此时就会导致打包失败。

遇到上面这些情况的时候，我们就想跳过测试执行下面的构建命令，具体实现方式有很多：IDEA工具实现跳过测试、配置插件实现跳过测试、命令行跳过测试。接下来详细介绍一下：

1. IDEA工具实现跳过测试。图中的按钮为`Toggle 'Skip Tests' Mode`，Toggle翻译为切换的意思，也就是说在测试与不测试之间进行切换。

   <img src="..\图片\3-24【Maven】\4-2跳过test.png"/>

   这种方式最简单，但是有点"暴力"，会把所有的测试都跳过，如果我们想更精细的控制哪些跳过哪些不跳过，就需要使用配置插件的方式。

2. 配置插件实现跳过测试。在父工程中的pom.xml中添加测试插件配置

   ```xml
   <build>
       <plugins>
           <plugin>
               <artifactId>maven-surefire-plugin</artifactId>
               <version>2.12.4</version>
               <configuration>
                   <!--如果为true，则跳过所有测试，如果为false，则不跳过测试-->
                   <skipTests>false</skipTests>
                   <!--includes哪些测试类要参与测试 针对skipTests为true来设置的-->
                   <!--excludes排除掉不参与测试的内容-->
                   <excludes>
                       <exclude>**/BookServiceTest.java</exclude>
                   </excludes>
               </configuration>
           </plugin>
       </plugins>
   </build>
   ```

3. 命令行跳过测试。使用Maven的命令行，`mvn 指令 -D skipTests`cmd要在pom.xml所在目录下进行执行。


# 第五章 Maven私服

私服是公司内部搭建的用于存储Maven资源的服务器。所以说私服是一台独立的服务器，用于解决团队内部的资源共享与资源同步问题

搭建Maven私服的方式有很多，我们来介绍其中一种使用量比较大的实现方式Nexus。

**私服安装**

Nexus是Sonatype公司的一款maven私服产品，下载地址：https://help.sonatype.com/repomanager3/download。私服安装步骤如下：

1. 下载解压。将`latest-win64.zip`解压到一个空目录下，这里解压到了`E:\nexus`。里面有两个文件夹：nexus-3.30.1-01和sonatype-work。

2. 启动Nexus。

   ```apl
   # 启动Nexus
   E:\nexus\nexus-3.30.1-01\bin>nexus.exe /run nexus
   2023-01-28 13:13:05,229+0800 INFO  [FelixStartLevel] *SYSTEM org.sonatype.nexus.pax.logging.NexusLogActivator - start
   2023-01-28 13:13:05,817+0800 INFO  [FelixStartLevel] *SYSTEM org.sonatype.nexus.features.internal.FeaturesWrapper - Fast FeaturesService starting
   ......
   # 看到如下内容，说明启动成功
   -------------------------------------------------
   Started Sonatype Nexus OSS 3.30.1-01
   -------------------------------------------------
   ```

3. 浏览器访问。访问地址为http://localhost:8081

4. 首次登录重置密码。这里我们设置成了`admin`--`admin`

至此私服就已经安装成功。安装路径下etc目录中`nexus-default.properties`文件保存有nexus基础配置信息。安装路径下bin目录中`nexus.vmoptions`文件保存有nexus服务器启动对应的配置信息，可以修改服务器运行配置信息，例如默认占用内存空间。

**私服仓库**

在没有私服的情况下，我们自己创建的服务都是安装在Maven的本地仓库中。私服中也有仓库，我们要把自己的资源上传到私服，最终也是放在私服的仓库中，其他人要想使用我们所上传的资源，就需要从私服的仓库中获取。

自己写的服务代码有多种版本`SNAPSHOT`和`RELEASE`，如果把这两类的都放到同一个仓库，比较混乱，所以私服就把这两个种jar包放入不同的仓库。但是这样获取的时候要遍历所有地仓库来找寻，所以为了方便获取，我们将若干个仓库编成一个组，我们只需要访问仓库组去获取资源。

当我们要使用的资源远程中央仓库有的第三方jar包，这个时候就需要从远程中央仓库下载，私服就再准备一个仓库，用来专门存储从远程中央仓库下载的第三方jar包，第一次访问没有就会去远程中央仓库下载，下次再访问就直接走私服下载。

<img src="..\图片\3-24【Maven】\5-1私服.png"/>

所有私服仓库总共分为三大类：

* 宿主仓库hosted：保存无法从中央仓库获取的资源。比如自主研发的项目、企业购买的第三方非开源项目。

* 代理仓库proxy ：代理远程仓库，通过nexus访问其他公共仓库，例如中央仓库。

* 仓库组group：将若干个仓库组成一个群组，简化配置。仓库组不能保存资源，属于设计型仓库

| 仓库类别 | 英文名称 | 功能                    | 关联操作 |
| -------- | -------- | ----------------------- | -------- |
| 宿主仓库 | hosted   | 保存自主研发+第三方资源 | 上传     |
| 代理仓库 | proxy    | 代理连接中央仓库        | 下载     |
| 仓库组   | group    | 为仓库编组简化下载操作  | 下载     |

**Nexus界面**

<img src="..\图片\3-24【Maven】\5-2nexus界面.png"/>

创建仓库步骤如下：

- 进入私服，选择齿轮`Server administration and configuration`(服务器管理和配置)设置使用。旁边的是浏览使用，我们这里选择齿轮，设置使用。
- 选择右侧Repository仓库下面的Repositories储存库，点击上方的Create repository创建存储库。
- 选择maven2 (hosted)，创建linxuan-snapshot仓库。在name里面输入`linxuan-snapshot`，将下方的Version policy（版本政策）选择Snapshot（快照）。最后直接点击创建。

<img src="..\图片\3-24【Maven】\5-3创建仓库.png"/>

可以将刚刚创建的仓库添加到仓库组里面，步骤如下：

* 进入设置，选择右侧Repository仓库下面的Repositories储存库。
* 选择仓库组并进入，然后找到要添加的仓库添加。

<img src="..\图片\3-24【Maven】\5-4添加进仓库组.png"/>

添加jar包进仓库，步骤如下：

* 进入浏览使用，选择Browse浏览，选择需要上传jar包的仓库
* 点击Uplocad component，进入上传jar包的界面。

<img src="..\图片\3-24【Maven】\5-5上传文件.png"/>

<img src="..\图片\3-24【Maven】\5-6查看仓库.png"/>

## 5.1 本地仓库访问私服配置

我们通过IDEA将开发的模块上传到私服，中间是要经过本地Maven的，本地Maven需要知道私服的访问地址以及私服访问的用户名和密码。如图：

<img src="..\图片\3-24【Maven】\5-6私服配置.png"/>

上面所说的这些内容，我们需要在本地Maven的配置文件`settings.xml`中进行配置。

1. 私服上配置仓库。上面已经创建了一个linxuan-snapshot的仓库，再按照上面的路径创建一个linxuan.release仓库。

2. 配置本地Maven对私服的访问权限。打开本地Maven的setting.xml文件，修改servers里面的内容，默认情况下servers里面内容被注释掉了，所以需要添加一些东西：

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
   
3. 配置私服的访问路径。上面没有对Maven说明私服的URL地址，所以接下来就是配置私服的访问路径：

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
   

至此本地仓库就能与私服进行交互了。如果一会关闭私服，那么这些也要给注释掉。

## 5.2 IDEA私服资源上传与下载

本地仓库与私服已经建立了连接，接下来我们就需要借助IDEA向私服上上传资源和下载资源，具体的实现步骤为：

第一步：配置工程上传私服的具体位置

```xml
<!--IDEApom.xml 配置当前工程保存在私服中的具体位置-->
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

第二步：发布资源到私服。点击deploy或者执行Maven命令`mvn deploy`

<img src="..\图片\3-24【Maven】\5-7上传.png"/>
