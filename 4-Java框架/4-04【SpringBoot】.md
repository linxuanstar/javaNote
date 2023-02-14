# 第一章 SpringBoot基础

`SpringBoot` 是由 `Pivotal` 团队提供的全新框架，其设计目的是用来简化 `Spring` 应用的初始搭建以及开发过程。

原始 `Spring` 环境搭建和开发存在以下问题：配置繁琐、依赖设置繁琐。

`SpringBoot` 程序优点恰巧就是针对 `Spring` 的缺点

* 自动配置。这个是用来解决 `Spring` 程序配置繁琐的问题
* 起步依赖。这个是用来解决 `Spring` 程序依赖设置繁琐的问题
* 辅助功能（内置服务器,...）。我们在启动 `SpringBoot` 程序时既没有使用本地的 `tomcat` 也没有使用 `tomcat` 插件，而是使用 `SpringBoot` 内置的服务器。

SpringBoot官网https://spring.io/projects/spring-boot，构建SpringBoot工程页面https://start.spring.io/

![](..\图片\4-04【SpringBoot】\1-0.png)

## 1.1 创建项目

创建步骤如下：

<img src="..\图片\4-04【SpringBoot】/1-1.png" />

![](D:\Java\笔记\图片\4-04【SpringBoot】\2-4.png)

<img src="..\图片\4-04【SpringBoot】/1-2.png" />

![](D:\Java\笔记\图片\4-04【SpringBoot】\1-3.png)

经过以上步骤后就创建了如下结构的模块，它会帮我们自动生成一个 `项目名 + Application` 类，运行该类就是运行整个程序了。上面就创建好一个SpringBoot项目了，需要注意的是：并不需要创建配置类。

SpringBoot使用Maven创建的项目结构如下：

```apl
SpringBoot项目
    |-- .mvn
    |-- src
    |    |-- main
    |    |    |-- java
    |    |    |    |-- com
    |    |    |         |-- linxuan # 项目GroupID
    |    |    |              |-- Application.java # 程序启动类，必须放在包的顶层，创建的pojo/dao和它同级
    |    |    |              |-- ServletInitializer.java
    |    |    |              |-- pojo/dao/service/controller/config # 自己之后创建的包
    |    |    |-- resources
    |    |         |-- static # SpringBoot项目没有webapp目录，html页面放在这个目录下面
    |    |         |-- templates # 存放模板文件，默认不向外开放
    |    |         |-- application.properties # 配置文件，通常删掉，用application.yaml代替
    |    |-- test
    |         |-- java
    |         |    |-- com
    |         |         |-- linxuan 
    |         |              |-- ApplicationTests.java # 测试类
    |         |-- resources
    |-- target # 项目编译后的存放位置
    |-- .gitignore # 提交给git忽略的文件
    |-- HELP.md
    |-- mvnw
    |-- mvnw.cmd
    |-- pom.xml
```

pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- 父工程，里面定义了各个jar包依赖的版本，避免了我们在使用不同软件技术时考虑版本的兼容问题。-->
    <!-- dependencyManagement标签是进行依赖版本锁定，并没有导入对应的依赖。导入依赖的时候不需要导入版本 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.2</version>
        <relativePath/>
    </parent>

    <groupId>com.linxuan</groupId>
    <artifactId>springboot1</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <packaging>jar</packaging>

    <name>springboot1</name>
    <description>springboot1</description>

    <!-- jdk版本 -->
    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- 这些starter依赖是起步依赖，是SpringBoot中常见依赖名称 -->
        <!-- web依赖，创建工程时勾选的web依赖，这里面也有许多依赖。-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- tomcat依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
        <!-- test依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- lombok依赖 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- 必须配置maven插件，否则打包有问题：springboot1-0.0.1-SNAPSHOT.jar中没有主清单属性 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

```groovy
// gradle构建，build.gradle如下
plugins {
	// java插件，这是java工程
	id 'java'
	// 引入springboot插件，第一个插件是指定springboot版本号，第二个插件是进行jar包的依赖管理
	id 'org.springframework.boot' version '3.0.2'
	id 'io.spring.dependency-management' version '1.1.0'
}

group = 'com.linxuan'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
	mavenCentral()
}

dependencies {
	// 导入SpringBoot启动依赖
	implementation 'org.springframework.boot:spring-boot-starter'

	// springboot操作数据库
	// MySQL驱动
	runtimeOnly 'com.mysql:mysql-connector-j'
	// druid连接池，SpringBoot没有管理版本，我们自己导入
	implementation group: 'com.alibaba', name: 'druid', version: '1.1.16'
	// MyBatisPlus，SpringBoot没有管理版本，我们自己导入
	implementation group: 'com.baomidou', name: 'mybatis-plus-boot-starter', version: '3.2.0'

	// 测试依赖
	testImplementation 'org.springframework.boot:spring-boot-starter-test'

	// 其他依赖
	// lombok，annotationProcessor代表main下代码的注解执行器
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

接下来创建一个controller类，之后就可以运行服务器测试了：

```java
@RestController
@RequestMapping("/books")
public class BookController {

    @GetMapping("/{id}")
    public String getById(@PathVariable Integer id){
        System.out.println("id ==> "+id);
        return "hello , spring boot!";
    }
}
```

使用 Postman 工具来测试：http://localhost:8080/books/1，最后会在PostMan响应数据`hello , spring boot!`

做完 `SpringBoot` 的入门案例后，接下来对比一下 `Spring` 程序和 `SpringBoot` 程序。如下图

| 类/配置文件                | Spring   | SpringBoot |
| -------------------------- | -------- | ---------- |
| **pom文件中的坐标**        | 手工添加 | 勾选添加   |
| **web3.0配置类**           | 手工制作 | 无         |
| **Spring/SpringMVC配置类** | 手工制作 | 无         |
| **控制器**                 | 手工制作 | 手工制作   |

## 1.3 程序启动

创建的每一个 `SpringBoot` 程序时都包含一个类似于下面的类，我们将这个类称作引导类。`SpringBoot` 的引导类是项目的入口，运行 `main` 方法就可以启动项目。

```java
@SpringBootApplication
public class Springboot1Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot1Application.class, args);
    }
}
```

由于我们在构建 `SpringBoot` 工程时已经在 `pom.xml` 中配置了如下插件

```xml
<!-- 必须配置maven插件，否则打包有问题：springboot1-0.0.1-SNAPSHOT.jar中没有主清单属性 -->
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

所以我们只需要使用 `Maven` 的 `package` 指令打包就会在 `target` 目录下生成对应的 `Jar` 包。启动它也很简单：

```apl
# 运行该jar包
java -jar 打包的jar包
```

```apl
D:\Java\IdeaProjects\ssm\springboot2\target>java -jar springboot-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.2)

# 开始启动springboot-0.0.1-SNAPSHOT.jar使用jdk17，PID时15692
Starting SpringbootApplication v0.0.1-SNAPSHOT using Java 17.0.6 with PID 15692 
# 未设置激活的配置文件，回退到 1 个默认配置文件：“默认”
No active profile set, falling back to 1 default profile: "default"
# Tomcat初始化，端口号未8080。SpringBoot集成了Tomcat服务器
Tomcat initialized with port(s): 8080 (http)
# 开始服务
Starting service [Tomcat]
# 启动Servlet引擎
Starting Servlet engine: [Apache Tomcat/10.1.5]
# 初始化Spring WebApplicationContext
Initializing Spring embedded WebApplicationContext
Root WebApplicationContext: initialization completed in 1811 ms
# Tomcat发布
Tomcat started on port(s): 8080 (http) with context path ''
# 开始项目
Started SpringbootApplication in 3.557 seconds (process running for 4.062)
```

SpringBoot默认时使用Tomcat服务器的，可以在pom.xml中设置更换服务器

```xml
<!-- 删除tomcat依赖 -->
<!-- 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>compile</scope>
</dependency> 
-->
<!--删除tomcat依赖，然后添加jetty依赖即可。注意，不要在web依赖里面排除tomcat依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

| 名称 | @SpringBootApplication                                       |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 替换 | 替换了`@Configuration`、`@EnableAutoConfiguration`、`@ComponentScan`三个注解 |
| 作用 | SpringBoot程序启动类，申明让spring boot自动给程序进行必要的配置。 |

| 名称 | @EnableAutoConfiguration                                     |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在类的上方定义                                       |
| 作用 | SpringBoot自动配置。根据依赖中的jar包，自动选择实例化某些配置，实例化对象。 |

# 第二章 SpringBoot配置文件

`SpringBoot` 提供了三种属性配置方式：application.properties、application.yml、application.yaml。`SpringBoot` 程序的配置文件名必须是 `application` ，只是后缀名不同而已。

三种配置文件的优先级：application.properties  >  application.yml   >  application.yaml。

![](D:\Java\笔记\图片\4-04【SpringBoot】\2-1.png)

**application.properties**

```properties
# 修改端口号
server.port=80
```

**application.yml**

```yaml
# resources目录下创建application.yml配置文件，改写端口号。在“:”后，数据前一定要加空格。
# 可以使用快捷键来修改端口号，直接写port，然后idea会自动补全。
server:
	port: 81
```

**application.yaml**

在 `resources` 下创建名为 `application.yaml` 的配置文件，配置内容和后缀名为 `yml` 的配置文件中的内容相同，只是使用了不同的后缀名而已。同样的，也可以利用idea补全功能。

```yaml
# resources目录下创建application.yaml配置文件，配置内容和后缀名为yml的配置文件中的内容相同。
# 这种方式更常用
server:
	port: 82
```

## 2.1 yaml配置文件

YAML（YAML Ain't Markup Language），一种数据序列化格式。 YAML 文件扩展名有两种：`.yml` (主流)、`.yaml`。这种格式的配置文件在近些年已经占有主导地位，那么这种配置文件和前期使用的配置文件是有一些优势的。

`yaml` 类型的配置文件内容如下

```yaml
enterprise:
	name: linxuan
	age: 16
	tel: 4006184000
```

yaml配置文件优点：

* 容易阅读。`yaml` 类型的配置文件比 `xml` 类型的配置文件更容易阅读，结构更加清晰

* 容易与脚本语言交互。

* 以数据为核心，重数据轻格式。`yaml` 更注重数据，而 `xml` 更注重格式


yaml配置文件语法规则：

* 大小写敏感

* 属性层级关系使用多行描述，每行结尾使用冒号结束

* 使用缩进表示层级关系，同层级左侧对齐，只允许使用空格（不允许使用Tab键）。空格的个数并不重要，只要保证同层级的左侧对齐即可。

* 属性值前面添加空格（属性名与属性值之间使用冒号+空格作为分隔）。

* `#` 表示注释

* 核心规则：数据前面要加空格与冒号隔开

数组数据在数据书写位置的下方使用减号作为数据开始符号，每行书写一个数据，减号与数据间空格分隔，例如：

```yaml
enterprise:
  name: linxuan
  age: 20
  tel: 4006184000
  subject:
    - Java
    - 前端
    - 大数据
```

## 2.2 读取yaml文件数据

读取yaml配置文件数据一共有三种方式：使用@Value注解、Environment对象、自定义对象。接下来了解一下：

```yaml
lesson: SpringBoot

server:
  port: 80

enterprise:
  name: linxuan
  age: 16
  tel: 4006184000
  subject:
    - Java
    - 前端
    - 大数据
```

**使用@Value注解**

使用 `@Value("表达式")` 注解可以从配合文件中读取数据：`${一级属性名.二级属性名……}`

```java
@RestController
@RequestMapping("/books")
public class BookController {
    
    @Value("${lesson}")
    private String lesson;
    @Value("${server.port}")
    private Integer port;
    @Value("${enterprise.subject[0]}")
    private String subject_00;

    @GetMapping("/{id}") 
    public String getById(@PathVariable Integer id){
        System.out.println(lesson); // SpringBoot
        System.out.println(port); // 80
        System.out.println(subject_00); // Java
        return "hello , spring boot!";
    }
}
```

**Environment对象**

上面方式读取到的数据特别零散，`SpringBoot` 还可以使用 `@Autowired` 注解注入 `Environment` 对象的方式读取数据。这种方式 `SpringBoot` 会将配置文件中所有的数据封装到 `Environment` 对象中，如果需要使用哪个数据只需要通过调用 `Environment` 对象的 `getProperty(String name)` 方法获取。具体代码如下：

```java
@RestController
@RequestMapping("/books")
public class BookController {
    
    @Autowired
    private Environment env;
    
    @GetMapping("/{id}")
    public String getById(@PathVariable Integer id){ 
        System.out.println(env.getProperty("lesson")); // SpringBoot
        System.out.println(env.getProperty("enterprise.name")); // 80
        System.out.println(env.getProperty("enterprise.subject[0]")); // Java
        return "hello , spring boot!";
    }
}
```

**自定义对象**

`SpringBoot` 还提供了将配置文件中的数据封装到我们自定义的实体类对象中的方式。

```java
// 将bean创建交给Spring管理
@Component
// 加载配置文件，使用prefix属性指定只加载指定前缀的数据
@ConfigurationProperties(prefix = "enterprise")
@Data
public class Enterprise {
    private String name;
    private int age;
    private String tel;
    private String[] subject;
}
```

```java
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private Enterprise enterprise;

    @GetMapping("/{id}")
    public String getById(@PathVariable Integer id){
        System.out.println(enterprise.getName());
        System.out.println(enterprise.getAge());
        System.out.println(enterprise.getSubject());
        System.out.println(enterprise.getTel());
        System.out.println(enterprise.getSubject()[0]);
        return "hello, spring boot!";
    }
}
```

使用第三种方式，在实体类上有如下警告提示

![](D:\Java\笔记\图片\4-04【SpringBoot】\2-2.png)

这个警告提示解决添加如下依赖即可

```xml
<!-- pom.xml文件 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

```groovy
// build.gradle文件
dependencies {
    annotationProcessor "org.springframework.boot:spring-boot-configuration-processor"
}
```

| 名称 | @ConfigurationProperties                          |
| ---- | ------------------------------------------------- |
| 类型 | 类注解，在类的上方定义                            |
| 作用 | 获取配置文件中的属性定义并绑定到Java Bean或属性中 |

## 2.3  多环境配置

以后在工作中，对于开发环境、测试环境、生产环境的配置肯定都不相同，比如我们开发阶段会在自己的电脑上安装 `mysql` ，连接自己电脑上的 `mysql` 即可，但是项目开发完毕后要上线就需要该配置，将环境的配置改为线上环境的。

来回的修改配置会很麻烦，而 `SpringBoot` 给开发者提供了多环境的快捷配置，需要切换环境时只需要改一个配置即可。不同类型的配置文件多环境开发的配置都不相同，接下来对不同类型的配置文件进行说明。

### 2.3.1 yaml文件

在 `application.yml` 中使用 `---` 来分割不同的配置，内容如下

```yaml
# 设置启用的环境
spring:
  profiles:
    active: dev
---
# 开发
spring:
  profiles: dev # 给开发环境起的名字 develop
server:
  port: 80
---
# 生产
spring:
  profiles: pro # 给生产环境起的名字 produce
server:
  port: 81
---
# 测试
spring:
  profiles: test # 给测试环境起的名字 test
server:
  port: 82
---
```

在上面配置中给不同配置起名字的 `spring.profiles` 配置项已经过时。最新用来起名字的配置项是：

```yaml
# 开发
spring:
  config:
    activate:
      on-profile: dev
```

SpringBoot3.0版本的必须使用最新的配置，否则会报错：

```apl
Property 'spring.profiles' imported from location 'class path resource [application.yaml]' is invalid and should be replaced with 'spring.config.activate.on-profile' [origin: class path resource [application.yaml] - 21:13]
```

修改成如下的：

```yaml
# 设置启用的环境
spring:
  profiles:
    active: dev

---
#开发
spring:
  config:
    activate:
      on-profile: dev
server:
  port: 80
---
#生产
spring:
  config:
    activate:
      on-profile: pro
server:
  port: 81
---
#测试
spring:
  config:
    activate:
      on-profile: test
server:
  port: 82
---
```

### 2.3.2 properties文件

`properties` 类型的配置文件配置多环境需要定义不同的配置文件，例如：application-dev.properties、application-test.properties、application-pro.properties。

```properties
# application-dev.properties开发环境的配置文件
server.port=80
```

```properties
# application-test.properties测试环境的配置文件
server.port=81
```

```properties
# application-pro.properties生产环境的配置文件
server.port=82
```

`SpringBoot` 只会默认加载名为 `application.properties` 的配置文件，所以需要在 `application.properties` 配置文件中设置启用哪个配置文件，配置如下：

```properties
spring.profiles.active=pro
```

### 2.3.3 命令行启动参数设置

使用 `SpringBoot` 开发的程序以后都是打成 `jar` 包，通过 `java -jar xxx.jar` 的方式启动服务的。我们首先需要将开发的程序打包成一个jar包。这里打包之前需要做两件事：执行clean指令和修改字符编码集为utf-8。然后再打包。

 `SpringBoot` 提供了在运行 `jar` 时设置开启指定的环境的方式，如下

```shell
java –jar springboot.jar –-spring.profiles.active=test
```

这种方式能修改临时端口号：

```shell
java –jar springboot.jar –-server.port=88
```

当然也可以同时设置多个配置，比如即指定启用哪个环境配置，又临时指定端口，如下

```shell
java –jar springboot.jar –-server.port=88 –-spring.profiles.active=test
```

命令行设置的端口号优先级高（也就是使用的是命令行设置的端口号），配置的优先级其实 `SpringBoot` 官网已经进行了说明，参见：https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config

![](D:\Java\笔记\图片\4-04【SpringBoot】\2-3.png)

## 2.4 多环境开发兼容问题

我们之前学过Maven中有多环境开发，而上面的SpringBoot中也有多环境开发，那么问题来了：配置不一样的时候到底是哪一个配置来做主呢？

其实我们可以仔细想一下：最终我们的SpringBoot工程是在命令终端通过jar包来启动的，最终启动的是jar包。而这个jar包是我们的Maven命令做的，所以是Maven做主，boot为辅。

首先我们来在pom.xml文件中创建多个环境：

```xml
<profiles>
    <!--开发环境-->
    <profile>
        <!--注意：这里使用的是id标签，因为要保证唯一，所以不能够使用name标签-->
        <id>dev</id>
        <properties>
            <profile.active>dev</profile.active>
        </properties>
        <!--设定是否为默认启动环境-->
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <!--生产环境-->
    <profile>
        <id>pro</id>
        <properties>
            <profile.active>pro</profile.active>
        </properties>
    </profile>
    <!--测试环境-->
    <profile>
        <id>test</id>
        <properties>
            <profile.active>test</profile.active>
        </properties>
    </profile>
</profiles>
```

将application.yml文件修改：

```yaml
#设置启动环境为开发环境
spring:
  profiles:
    active: ${profile.active} # 让启动环境由pom.xml传递过来

---
#开发环境
spring:
  profiles: dev
server:
  port: 80

---
#生产环境
spring:
  profiles: pro
server:
  port: 81

---
#测试环境
spring:
  profiles: test
server:
  port: 82
```

可是启动后，发现端口号变成了8080，并不是我们想象中的80端口号，查看打包后的boot程序发现上述的`${profile.active}`根本没有解析，这是因为我们在pom.xml文件中配置的属性只能够在pom.xml文件中使用，所以我们要扩大范围，让其能够干预到配置文件。

加一个插件就可以了：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.2.0</version>
    <!--让其能够解析resources中内容-->
    <configuration>
        <!--设置字符集-->
        <encoding>utf-8</encoding>
        <useDefaultDelimiters>true</useDefaultDelimiters>
    </configuration>
</plugin>
```

这样就可以了，打包，运行，端口号已经修改成了80了。

## 2.5 配置文件分类

有这样的场景，我们开发完毕后需要测试人员进行测试，由于测试环境和开发环境的很多配置都不相同，所以测试人员在运行我们的工程时需要临时修改很多配置，如下

```shell
java –jar springboot.jar –-spring.profiles.active=test --server.port=85 --server.servlet.context-path=/heima --server.tomcat.connection-timeout=-1 …… …… …… …… ……
```

针对这种情况，`SpringBoot` 定义了配置文件不同的放置的位置；而放在不同位置的优先级时不同的。

`SpringBoot` 中4级配置文件放置位置：

* 1级：classpath：application.yml  
* 2级：classpath：config/application.yml
* 3级：file ：application.yml
* 4级：file ：config/application.yml 

4级最高、1级最低。1级是类路径，就是我们上面程序中写的application.yml文件的位置，就是在类路径旁边放着。而文件路径就是在打包的jar包旁边放着了。

> SpringBoot 2.5.0版本存在一个bug，我们在使用这个版本时，需要在 `jar` 所在位置的 `config` 目录下创建一个任意名称的文件夹

# 第三章 SpringBoot依赖

## 3.1 Tset依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
```

一旦依赖了spring-boot-starter-test，下面这些类库将被一同依赖进去：

- JUnit：java测试事实上的标准，默认依赖版本是4.12（JUnit5和JUnit4差别比较大，集成方式有不同）。
- Spring Test & Spring Boot Test：Spring的测试支持。
- AssertJ：提供了流式的断言方式。
- Hamcrest：提供了丰富的matcher。
- Mockito：mock框架，可以按类型创建mock对象，可以根据方法参数指定特定的响应，也支持对于mock调用过程的断言。
- JSONassert：为JSON提供了断言功能。
- JsonPath：为JSON提供了XPATH功能。

现在使用的都是SpringBoot3.0的框架，默认集成了JUnit 5 作为单元测试默认库。Spring Boot Test支持的测试种类，大致可以分为如下三类：

- 单元测试：一般面向方法，编写一般业务代码时，测试成本较大。涉及到的注解有@Test。
- 功能测试：一般面向某个完整的业务功能，同时也可以使用切面测试中的mock能力，推荐使用。涉及到的注解有@SpringBootTest等。
- 切片测试：一般面向难于测试的边界功能，介于单元测试和功能测试之间。涉及到的注解有 @WebMvcTest等。主要就是对于Controller的测试，分离了Service层，这里就涉及到Moc控制层所依赖的组件了

**单元测试**

集成测试，不启动server。默认无参数的`@SpringBootTest` 注解会加载一个Web Application Context并提供Mock Web Environment，但是不会启动内置的server。这点从日志中没有打印Tomcat started on port(s)可以佐证。

```java
@SpringBootTest
class TestDemoApplicationTests {
    @Test
    void contextLoads() {
    }
}
```

**功能测试**

一般情况下，使用@SpringBootTest后，Spring将加载所有被管理的bean，基本等同于启动了整个服务，此时便可以开始功能测试。

由于web服务是最常见的服务，且我们对于web服务的测试有一些特殊的期望，所以@SpringBootTest注解中，给出了webEnvironment参数指定了web的environment，该参数的值一共有四个可选值：

- MOCK：此值为默认值，该类型提供一个mock环境，可以和@AutoConfigureMockMvc或@AutoConfigureWebTestClient搭配使用，开启Mock相关的功能。注意此时内嵌的服务（servlet容器）并没有真正启动，也不会监听web服务端口。
- RANDOM_PORT：启动一个真实的web服务，监听一个随机端口。
- DEFINED_PORT：启动一个真实的web服务，监听一个定义好的端口（从application.properties读取）。
- NONE：启动一个非web的ApplicationContext，既不提供mock环境，也不提供真实的web服务。

```java
//指定@SpringBootTest的Web Environment为RANDOM_PORT
//此时，将会加载Applicaiton Context，并启动server，server侦听在随机端口上。在测试类中通过@LocalServerPort获取该端口值。
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoTest {
    
    @LocalServerPort
    private Integer port;
    
    @Test
    @DisplayName("should access application")
    void shouldAccessApplication() {
        assertThat(port).isGreaterThan(1024);
    }
}
```

注：如果当前服务的classpath中没有包含web相关的依赖，spring将启动一个非web的ApplicationContext，此时的webEnvironment就没有什么意义了。

**切片测试**

所谓切片测试，官网文档称为 “slice” of your application，实际上是对一些特定组件的称呼。这里的slice并非单独的类（毕竟普通类只需要基于JUnit的单元测试即可），而是介于单元测试和集成测试中间的范围。

slice是指一些在特定环境下才能执行的模块，比如MVC中的Controller、JDBC数据库访问、Redis客户端等，这些模块大多脱离特定环境后不能独立运行，假如spring没有为此提供测试支持，开发者只能启动完整服务对这些模块进行测试，这在一些复杂的系统中非常不方便，所以spring为这些模块提供了测试支持，使开发者有能力单独对这些模块进行测试。

# 第四章 SpringBoot整合

## 4.1 整合junit

**回顾 Spring 整合 junit**

```java
// 设置类运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 设置Spring环境对应的配置类
@ContextConfiguration(classes = {SpringConfig.class})
// 加载配置文件
// @ContextConfiguration(locations={"classpath：applicationContext.xml"})
public class UserServiceTest {
    // 支持自动装配注入bean
    @Autowired
    private BookService bookService;
    
    @Test
    public void testSave(){
        bookService.save();
    }
}
```

**SpringBoot整合junit**

```java
package com.linxuan.service;

public interface BookService {
    public void save();
}
```

```java
package com.linxuan.service.impl;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public void save() {
        System.out.println("book service is running ...");
    }
}
```

```java
package com.linxuan;

// 测试类上面必须添加SpringBootTest注解
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void save() {
        bookService.save();
    }
}
```

| 名称 | @SpringBootTest        |
| ---- | ---------------------- |
| 类型 | 类注解，在类的上方定义 |
| 作用 | 测试类                 |

## 4.2 整合mybatis

```xml
<dependencies>
    <!-- SpringBoot整合MyBatis环境 -->
    <!-- mysql驱动，创建项目的时候搜索mysql -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!-- Druid连接池依赖，需要指定jar包。SpringBoot并没有管理其版本 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- mybatis依赖，创建项目的时候搜索mybatis -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>3.0.0</version>
    </dependency>

    <!-- SpringBoot测试依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- 其他依赖 -->
    <!-- lombok依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

```groovy
plugins {
	// java插件，这是java工程
	id 'java'
	// 引入springboot插件，第一个插件是指定springboot版本号，第二个插件是进行jar包的依赖管理
	id 'org.springframework.boot' version '3.0.2'
	id 'io.spring.dependency-management' version '1.1.0'
}

dependencies {
	// 导入SpringBoot启动依赖
	implementation 'org.springframework.boot:spring-boot-starter'

	// springboot操作数据库
	// MySQL驱动
	runtimeOnly 'com.mysql:mysql-connector-j'
	// druid连接池，SpringBoot没有管理版本，我们自己导入
	implementation group: 'com.alibaba', name: 'druid', version: '1.1.16'
	// MyBatis依赖
	implementation group: 'org.mybatis.spring.boot', name: 'mybatis-spring-boot-starter', version: '3.0.0'

	// 测试依赖
	testImplementation 'org.springframework.boot:spring-boot-starter-test'

	// 其他依赖
	// lombok，annotationProcessor代表main下代码的注解执行器
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
}
```

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;
# 如果不存在tb_book表那么就创建
CREATE TABLE IF NOT EXISTS tb_book(
    id int primary key auto_increment,
    type varchar(20),
    name varchar(50),
    description varchar(255)
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_book;
# 插入数据
INSERT  INTO `tb_book`(`id`, `type`, `name`, `description`) VALUES 
	(1, '计算机理论', 'Spring实战 第五版', 'Spring入门经典教程'), 
	(2, '计算机理论', 'Spring 5核心原理与30个类手写实践', '手写Spring精华思想'), 
	(3, '计算机理论', 'Spring 5设计模式', '刨析Spring源码中蕴含的10大设计模式'), 
	(4, '计算机理论', 'Spring MVC+Mybatis开发', '全方位解析面向Web应用的轻量级框架'), 
	(5, '计算机理论', '轻量级Java Web企业应用实战', '源码级刨析Spring框架'), 
	(6, '计算机理论', 'Java核心技术', 'Core Java第11版, Jolt大奖获奖作品'), 
	(7, '计算机理论', '深入理解Java虚拟机', '5个纬度全面刨析JVM'), 
	(8, '计算机理论', 'Java编程思想(第4版)', 'Java学习必读经典'), 
	(9, '计算机理论', '零基础学Java(全彩版)', '零基础自学编程的入门图书'), 
	(10, '市场营销', '直播就这么做:主播高效沟通实战指南', '成长为网红的秘密'), 
	(11, '市场营销', '直播销讲实战一本通', '和秋叶一起学系列网络营销书籍'), 
	(12, '市场营销', '直播带货:淘宝、天猫直播从新手到高手', '一本教你如何玩转直播的书');
```

```yaml
# application.yaml配置文件配置
spring:
  # 配置连接数据库四要素
  datasource:
    # MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/linxuan?useSSL=false
    username: root
    password: root
    # 使用Druid连接池技术连接数据库
    type: com.alibaba.druid.pool.DruidDataSource
```

```java
@Data
public class Book {
    private Integer id;
    private String name;
    private String type;
    private String description;
}
```

```java
// 这里需要导入mapper注解，和Spring整合MyBatis不一样。
// 这是因为Spring整合的时候有配置类或者配置文件指定扫描包，所以SpringBoot需要注解来指定扫描Mapper接口
// 这样MyBatis会扫描该接口并创建它的代理对象交给SpringIOC容器管理。
@Mapper
public interface BookDao {
    @Select("select * from tb_book where id = #{id}")
    public Book getById(Integer id);
}
```

```java
package com.linxuan;

@SpringBootTest
public class BookDaoTest {
    @Autowired
    private BookDao bookDao;

    @Test
    void testGetById() {
        Book book = bookDao.getById(1);
        System.out.println(book);
    }
}
```

| 名称 | @Mapper                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在dao层接口上面定义                                  |
| 作用 | 指定扫描Mapper接口，MyBatis会扫描该接口并创建它的代理对象交给SpringIOC容器管理 |

## 4.3 整合SSM案例

```xml
<dependencies>
    <!-- SpringMVC依赖 -->
    <!-- web依赖，创建工程时勾选的web依赖，这里面也有许多依赖。-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- SpringBoot整合MyBatis环境 -->
    <!-- mysql驱动 -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!-- Druid连接池依赖，需要指定jar包。SpringBoot并没有管理其版本 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- mybatis依赖 -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>3.0.0</version>
    </dependency>

    <!-- SpringBoot测试依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- 其他依赖 -->
    <!-- lombok依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;
# 如果不存在tb_book表那么就创建
CREATE TABLE IF NOT EXISTS tb_book(
    id int primary key auto_increment,
    type varchar(20),
    name varchar(50),
    description varchar(255)
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_book;
# 插入数据
INSERT  INTO `tb_book`(`id`, `type`, `name`, `description`) VALUES 
	(1, '计算机理论', 'Spring实战 第五版', 'Spring入门经典教程'), 
	(2, '计算机理论', 'Spring 5核心原理与30个类手写实践', '手写Spring精华思想'), 
	(3, '计算机理论', 'Spring 5设计模式', '刨析Spring源码中蕴含的10大设计模式'), 
	(4, '计算机理论', 'Spring MVC+Mybatis开发', '全方位解析面向Web应用的轻量级框架'), 
	(5, '计算机理论', '轻量级Java Web企业应用实战', '源码级刨析Spring框架'), 
	(6, '计算机理论', 'Java核心技术', 'Core Java第11版, Jolt大奖获奖作品'), 
	(7, '计算机理论', '深入理解Java虚拟机', '5个纬度全面刨析JVM'), 
	(8, '计算机理论', 'Java编程思想(第4版)', 'Java学习必读经典'), 
	(9, '计算机理论', '零基础学Java(全彩版)', '零基础自学编程的入门图书'), 
	(10, '市场营销', '直播就这么做:主播高效沟通实战指南', '成长为网红的秘密'), 
	(11, '市场营销', '直播销讲实战一本通', '和秋叶一起学系列网络营销书籍'), 
	(12, '市场营销', '直播带货:淘宝、天猫直播从新手到高手', '一本教你如何玩转直播的书');
```

```yaml
# application.yaml配置文件配置
spring:
  # 配置连接数据库四要素
  datasource:
    # MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/linxuan?useSSL=false
    username: root
    password: root
    # 使用Druid连接池技术连接数据库
    type: com.alibaba.druid.pool.DruidDataSource
```

```java
@Data
public class Book {
    private Integer id;
    private String name;
    private String type;
    private String description;
}
```

```java
// 这里需要导入mapper注解，和Spring整合MyBatis不一样。
// 这是因为Spring整合的时候有配置类或者配置文件指定扫描包，所以SpringBoot需要注解来指定扫描Mapper接口
// 这样MyBatis会扫描该接口并创建它的代理对象交给SpringIOC容器管理。
@Mapper
public interface BookDao {
    @Insert("insert into tb_book (type, name, description) values(#{type}, #{name}, #{description})")
    public void save(Book book);

    @Update("update tb_book set type = #{type}, name = #{name}, description = #{description} where id = #{id}")
    public void update(Book book);

    @Delete("delete from tb_book where id = #{id}")
    public void delete(Integer id);

    @Select("select * from tb_book where id = #{id}")
    public Book getById(Integer id);

    @Select("select * from tb_book")
    public List<Book> getAll();
}
```

```java
// 添加事务，为当前接口所有实现类的所有方法上添加事务
@Transactional
public interface BookService {
    /**
     * 保存
     * @param book
     * @return
     */
    public boolean save(Book book);

    /**
     * 修改
     * @param book
     * @return
     */
    public boolean update(Book book);

    /**
     * 按id删除
     * @param id
     * @return
     */
    public boolean delete(Integer id);

    /**
     * 按id查询
     * @param id
     * @return
     */
    public Book getById(Integer id);

    /**
     * 查询全部
     * @return
     */
    public List<Book> getAll();
}
```

```java
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    public boolean save(Book book) {
        bookDao.save(book);
        return true;
    }

    public boolean update(Book book) {
        bookDao.update(book);
        return true;
    }

    public boolean delete(Integer id) {
        bookDao.delete(id);
        return true;
    }

    public Book getById(Integer id) {
        return bookDao.getById(id);
    }

    public List<Book> getAll() {
        return bookDao.getAll();
    }
}
```

```java
// 等于@Controller + ResponseBody。设置当前控制器类为RESTful风格。该类交由Spring管理，所有方法返回值作为响应体，无需解析。
@RestController
@RequestMapping("/books")
public class BookController {

    // 自动装配
    @Autowired
    private BookService bookService;

    @PostMapping
    // @RequestBody 将请求中请求体所包含的数据传递给请求参数，就是将前端的JSON数据映射到形参的对象中作为数据
    public boolean save(@RequestBody Book book) {
        return bookService.save(book);
    }

    @PutMapping
    public boolean update(@RequestBody Book book) {
        return bookService.update(book);
    }

    @DeleteMapping("/{id}")
    // @PathVariable RESTful风格传递路径参数，绑定路径参数与后端方法形参间的关系，要求路径参数名与形参名对应
    public boolean delete(@PathVariable Integer id) {
        return bookService.delete(id);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Integer id) {
        return bookService.getById(id);
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }
}
```

```java
@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Test
    public void testGetById() {
        Book book = bookService.getById(1);
        System.out.println(book);
    }

    @Test
    public void testGetAll() {
        List<Book> list = bookService.getAll();
        for (Book book : list) {
            System.out.println(book);
        }
    }
}
```

## 4.4 访问页面

在 `SpringBoot` 程序中是没有 `webapp` 目录的，静态资源需要放在 `resources` 下的 `static` 下。

## 4.4 RestTemplate

`RestTemplate`是由`Spring`框架提供的一个可用于应用中调用`rest`服务的类它简化了与`http`服务的通信方式，统一了`RESTFul`的标准，封装了`http`连接，我们只需要传入`url`及其返回值类型即可。相较于之前常用的`HttpClient`，`RestTemplate`是一种更为优雅的调用`RESTFul`服务的方式。

在`Spring`应用程序中访问第三方`REST服务`与使用`Spring RestTemplate`类有关。`RestTemplate`类的设计原则与许多其他`Spring`的模板类(例如`JdbcTemplate`)相同，为执行复杂任务提供了一种具有默认行为的简化方法。

`RestTemplate`默认依赖JDK提供了`http`连接的能力（`HttpURLConnection`），如果有需要的话也可以通过`setRequestFactory`方法替换为例如`Apache HttpCompoent、Netty或OKHttp`等其他`Http libaray`。

考虑到了`RestTemplate`类是为了调用REST服务而设计的，因此它的主要方法与`REST`的基础紧密相连就不足为奇了，后者时`HTTP`协议的方法：`HEAD、GET、POST、PUT、DELETE、OPTIONS`例如，`RestTemplate`类具有`headForHeaders()、getForObject()、putForObject()，put()和delete()`等方法。

### 4.4.1 创建`RestTemplate`

因为`RestTemplate`是`Spirng`框架提供的所以只要是一个`Springboot`项目就不用考虑导包的问题，这些都是提供好的。

但是`Spring`并没有将其加入`SpringBean`容器中，需要我们手动加入，因为我们首先创建一个`Springboot`配置类，再在配置类中将我们的`RestTemlate`注册到`Bean`容器中。

第一种方法：使用`Springboot`提供的`RestTemplateBuilder`构造类来构造一个`RestTemplate`，可以自定义一些连接参数，如：连接超时时间，读取超时时间，还有认证信息等

```java
@Configuration
public class WebConfiguration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder
                //设置连接超时时间
                .setConnectTimeout(Duration.ofSeconds(5000))
                //设置读取超时时间
                .setReadTimeout(Duration.ofSeconds(5000))
                //设置认证信息
                .basicAuthentication("username","password")
                //设置根路径
                .rootUri("https://api.test.com/")
                //构建
                .build();
    }
}
```

第二种方法：使用`RestTemplate`构造方法构造一个`RestTemlate`，虽然不能像`RestTemplate`构造类那样更详细、更多样的配置参数，但是`RestTemplate`构造方法在一般情况是够用的。

```java
@Configuration
public class WebConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    
}
```

两者方法都可使用，前者提供了多样的自定义参数的选择，可以将`RestTemplate`配置的更为完善，后者则简化了配置虽然配置多样性不如前者，但是日常使用调用些`API`还是足以使用

### 4.4.2 主要方法

**GET方法**

`public <T> ResponseEntity<T> getForEntity(...)`：后缀带有`Entity`的方法都代表返回一个`ResponseEntity<T>`，`ResponseEntity<T>`是Spring对`HTTP`请求响应的封装，包括了几个重要的元素，如响应码，`contentType、contentLength`、响应消息体等。

```java
public void queryWeather() {

        ResponseEntity<Object> forEntity = restTemplate.getForEntity("https://restapi.amap.com/v3/weather/weatherInfo?city=510100&key=e7a5fa943f706602033b6b329c49fbc6", Object.class);
        System.out.println("状态码:"+forEntity.getStatusCode());
        System.out.println("状态码内容:"+forEntity.getStatusCodeValue());
        HttpHeaders headers = forEntity.getHeaders();
        System.out.println("响应头:"+headers);
        Object body = forEntity.getBody();
        System.out.println("响应内容:"+body);
    }
```

`public <T> T getForObject(...)`：相比于前者`getForEntity()`该方法则是，更偏向于直接获取响应内容的，因为他直接返回响应实体的`body`（响应内容）。比如下面这个例子：

```java
public void queryWeather() {

    Object body = restTemplate.getForObject("https://restapi.amap.com/v3/weather/weatherInfo?city=510100&key=e7a5fa943f706602033b6b329c49fbc6", Object.class);
    System.out.println(body);
}
```
