# 第一章 Tomcat

JavaEE（Java Platform, Enterprise Edition）：Java开发语言的分支，是Sun公司为企业级应用推出的标准平台。JavaEE是个大杂烩，包括Applet、EJB、JDBC、JNDI、Servlet、JSP等技术的标准，运行在一个完整的应用服务器上，用来开发大规模、分布式、健壮的网络应用。

JavaWeb：使用java语言为基础，利用JavaEE中的Servlet、JSP等技术开发动态页面，方便用户通过浏览器与服务器后台交互。JavaWeb应用程序可运行在一个轻量级的Web服务器中，比如Tomcat。可以粗略地认为JavaWeb就是JavaEE的一部分，是成为JavaEE大师过程中的第一站。

web资源即放在Internet网上供外界访问的文件或程序，又根据它们呈现的效果及原理不同，将它们划分为静态资源和动态资源。

* 静态资源：固定不变数据文件，例如HTML+CSS+JavaScript
* 动态资源：一段服务程序，运行后，生成的数据文件。例如：servlet、jsp、php、.net、ruby、python等

服务器就是安装了服务器软件的计算机。web服务器软件又叫做web容器，可以接受用户的请求，处理请求，做出响应。在web服务器软件当中，可以部署web项目，让用户通过浏览器来访问这些项目。常见服务器：

| 名称     | 公司             | 介绍                                                         |
| -------- | ---------------- | ------------------------------------------------------------ |
| Tomcat   | `Apache`基金组织 | 中小型的`JavaEE`服务器，仅支持少量的`JavaEE`规范，支持`servlet/jsp`。 |
| JBOSS    | `JBOSS`公司      | 大型的`JavaEE`服务器，支持所有的`JavaEE`规范，开源免费。     |
| webLogic | `Oracle`公司     | 大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。         |

## 1.1 Tomcat安装下载

官网：https://tomcat.apache.org/。下载绿色版直接解压缩即可，安装目录不要包含中文。如果需要卸载直接删除安装目录即可。

Tomcat目录如下：

```apl
apache-tomcat-9.0.11
    |-- bin      # 存放可执行文件，启动Tomcat文件就在该目录下
    |-- conf     # 存放配置文件
    |-- lib      # 存放依赖jar包
    |-- logs     # 存放日志文件
    |-- temp     # 存放临时文件
    |-- webapps  # 存放web项目
    |-- work     # 存放运行时的数据
```

```apl
# 使用命令行方式启动，或者可以直接进入到该目录双击该文件运行
E:\Tomcat\apache-tomcat-9.0.11\bin>startup.bat
Using CATALINA_BASE:   "E:\Tomcat\apache-tomcat-9.0.11"
Using CATALINA_HOME:   "E:\Tomcat\apache-tomcat-9.0.11"
Using CATALINA_TMPDIR: "E:\Tomcat\apache-tomcat-9.0.11\temp"
Using JRE_HOME:        "E:\JAVA\jdk1.8.0_144"
Using CLASSPATH:       "E:\Tomcat\apache-tomcat-9.0.11\bin\bootstrap.jar;E:\Tomcat\apache-tomcat-9.0.11\bin\tomcat-juli.jar"
```

```apl
# 关闭方式：1.双击shutdown.bat。2.在额外打开的黑窗口中按Ctrl+C关闭。3.直接叉掉额外打开的黑窗口。（建议1或2）
```

浏览器输入http://localhost:8080，验证是否启动。用`ip:端口号`的方式打开，`ip`是localhost也就是`127.0.0.1`，Tomcat的端口号是8080。

在启动过程中可能会遇到的两种问题：

1. 黑窗口一闪而过。没有正确的配置`JAVA_HOME`环境变量，配置一下就可以了。

2. 端口号占用了导致启动报错。这时候解决有两种方式：找到使用端口号为8080的软件将其杀死、修改Tomcat自身的端口号。

   ```xml
   <!-- conf/server.xml -->
   <Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
   ```
   

一般会将Tomcat的默认端口设置为80，80端口号是http协议的默认端口号，这样，访问的时候直接输入localhost就可以了，不用输入端口号了。

## 1.2 Tomcat部署项目

部署项目，因为Tomcat就是一个web服务器软件，所以需要有项目在Tomcat上面部署。使用Tomcat部署项目一共有三种方式：将项目放到`webapps`目录下面、配置`conf/server.xml`文件、在`conf/catalina/localhost`创建任意名称的XML文件。创建一个文件夹测试一下：

```apl
D:（D盘）
 |-- hello(文件夹)
        |-- hello.html
```

```html
<!--hello.html内容-->
<html>
    <body>
        <h2>Hello World!</h2>
    </body>
</html>
```

接下来详细介绍一下：

**将项目放到webapps目录下面**

将在D盘创建的文件夹复制到`webapps`目录下，然后就可以通过Tomcat来访问hello文件了。打开浏览器，地址为http://localhost:8080/hello/hello.html。这里的`/hello`是项目的访问路径，也叫做虚拟目录，和项目的名称一样。

这种有一个简化的部署方式，将项目打成一个war包，再将war包放置到webapps目录下面。然后war包会自动解压缩，如果删除的话，删除war包就可以了，项目也会自动删除。

**配置conf/server.xml文件**

上面的方式必须将文件放到webapps目录下面，但是假如不想将资源放到该目录下，那么可以通过配置文件方式来修改：

```xml
<!-- 打开conf/server.xml，找到Hhost标签，在该标签内部配置如下代码 -->
<!-- Context docBase="项目的访问路径" path="项目资源文件=虚拟目录"/ 虚拟目录可以设置为其他的-->
<Context docBase="D:\hello" path="/hello" />
```

修改完毕之后，保存。打开浏览器http://localhost:8080/hehe/hello.html。这种方式不建议使用，因为乱修改会把`server.xml`配置文件修改错误。

**在conf/catalina/localhost创建任意名称的XML文件**

在`conf/catalina/localhost`目录下面创建一个`index.xml`文件。在文件里面配置下面代码：

```html
<!-- Context docBase="项目的访问路径"/ -->
<!-- 这里的虚拟目录变成了xml文件的名称，所以这个项目的虚拟目录是index -->
<Context docBase="D:\hello" />
```

打开浏览器，地址栏输入：http://localhost:8080/index/hello.html。建议使用这种方式来部署项目。

## 1.3 项目介绍

项目有静态项目和动态项目。java动态项目的目录结构如下：

```apl
-- 项目的根目录
	-- WEB-INF目录：
		-- web.xml：web项目的核心配置文件
		-- classes目录：放置字节码文件的目录
		-- lib目录：放置依赖的jar包
```

# 第二章 Servlet入门

Serverlet（server applet）：运行在服务器端的小程序。

Servlet就是一个接口，定义了Java类被浏览器访问到的规则。将来我们会自己定义一个类，实现Servlet接口，复写这些方法。

## 2.1 快速入门

```xml
<!-- 导入Servletjar包 -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <!-- 依赖作用范围为provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
    <scope>provided</scope>
</dependency>
```

```java
// 自定义一个类实现servlet接口，并且实现它的方法
public class Demo01Servlet implements Servlet{

    // 初始化方法，只有在Servlet创建的时候调用
    @Override
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
    }

    // 获取ServletConfig对象
    @Override
    public ServletConfig getServletConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    // 提供服务方法 每一次Servlet被访问，都会执行
    @Override
    public void service(ServletRequest req,ServletResponse res)throws ServletException, IOException {
        System.out.println("==================");
    }

    // 获取Servlet的一些信息，版本，作者，等等
    @Override
    public String getServletInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    // 销毁方法 在服务器正常关闭的时候就会执行，只会执行一次
    @Override
    public void destroy() {
        // TODO Auto-generated method stub   
    }   
}
```

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd">

<web-app>
    <display-name>Archetype Created Web Application</display-name>
    <!-- web.xml内部配置Servlet -->
    <servlet>
        <servlet-name>demo01</servlet-name>
        <!--这个是全类名 反射有讲过-->
        <servlet-class>com.linxuan.web.Demo01Servlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>demo01</servlet-name>
        <url-pattern>/demo01</url-pattern>
    </servlet-mapping>
</web-app>
```

执行原理如下：

1. 当服务器接受到客户端浏览器的请求后，会解析请求URL路径，获取访问的Servlet的资源路径。
2. 查找`web.xml`文件，看看是否有对应的`<url-pattern>`标签体内容。
3. 如果有，则在找到对应的`<servlet-class>`全类名。
4. Tomcat会将字节码文件加载进内存，并且创建其对象。
5. 调用方法。

## 2.2 Servlet声明周期

Servlet声明周期如下：被创建 --> 提供服务 --> 被销毁。

**被创建**

执行`init`方法，只执行一次。默认情况下，Tomcat第一次被访问的时候，Servlet被创建。当然可以在web.xml中配置创建的时机。

```xml
<web-app>
    <!-- web.xml内部配置Servlet -->
    <servlet>
        <!-- 第一次访问的时候，创建。默认值一般为-1，只要是负数，那么就是第一次访问的时候创建 -->
        <load-on-startup>负数</load-on-startup>
        <!-- 在服务器启动的时候，创建。设置值应该为0或者正整数，一般我们设置为0~10 -->
        <load-on-satrtup>正数</load-on-satrtup>
    </servlet>
</web-app>
```

`Servlet`的`init`方法，只执行一次，说明一个Servlet在内存中只存在一个对象，Servlet是单例的。但是正因为此，如果多个用户同时访问，那么就可能存在线程安全问题。

解决方法：尽量不要在Servlet中定义成员变量，应该定义局部变量（在方法内部定义）。即使迫不得已定义了成员变量，那么在方法中也不要对其修改任何值。这样每一次调用一个Servlet对象，成员变量都相同。

**提供服务**

执行`service`方法，执行多次。每次访问`Servlet`时候，`Service`都会被调用一次。

**被销毁**

执行`destroy`方法，只执行一次。`Servlet`被销毁执行。服务器关闭的时候，Servlet被销毁。只有服务器正常关闭的时候，才会执行`destroy`方法。`destroy`方法在`Servlet`被销毁之前执行，一般用于释放资源。

## 2.3 Servlet3.0注解

每当我们重新创建一个类的时候，都要重复的对该类进行配置。`Servlet3.0`的存在让我们可以不用创建`web.xml`，直接使用注解来进行配置。

```java
// 使用注解代替了配置类，每当访问路径为/demo02的时候就会执行该Servlet
@WebServlet("/demo02")
public class Demo02Servlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req,ServletResponse resp)throws ServletException,IOException {
        System.out.println("-------------");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
    }
}
```

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet {

    /**
     * The URL patterns of the servlet。这样定义名称是value，那么就不用写属性了
     *
     * @return the URL patterns of the servlet
     */
    String[] value() default {};

    /**
     * The URL patterns of the servlet
     *
     * @return the URL patterns of the servlet
     */
    String[] urlPatterns() default {};
}
```

`urlpartten`：`Servlet`访问路径。这是一种配置方式，通过注解也可以配置访问路径。通过阅读源码我们可看到在`WebServlet`注解中定义了`URLPatterns`，并且它的返回值也是一个数组。所以我们可以通过`WebServlet`注解定义多个访问路径。

```java
// 定义了资源路径可以为a, b, c。我们可以通过他们三个中的任意一个访问该类。
@WebServlet({"/a", "b", "c"})
```

路径定义规则：

1. `/xxx`。例如：`@WebServlet("/demo03")`
2. `/xxx/xxx`：多层路径，目录结构。例如：`@WebServlet("/demo03/d")`
3. `/xxx/*`：第二个路径可以随便写。这种方式等级比较低，因为是通配符。
4. `*.do`：可以任意定义后缀名称，可以不是do，可以为a，b。这种方式等级比较低，因为是通配符。另外前面也不要加上`/`。

## 2.4 IDEA与Tomcat的配置

前面说过Tomcat部署项目一共右三种方式：将项目放到webapps目录下面、配置conf/server.xml文件、在conf/catalina/localhost创建任意名称的XML文件。

Idea采用的是第三种：在conf/catalina/localhost创建任意名称的XML文件。只不过IDEA并没有在原始的Tomcat的目录下创建xml文件，它会为每一个Tomcat部署的项目单独建立一份配置文件，配置文件里面配置xml文件。查看控制台的log，可以发现：

```apl
# catalina.base指向每个Tomcat目录私有信息的位置，就是conf、logs、temp、webapps和work的父目录
Using CATALINA_BASE: C:\Users\林轩\AppData\Local\JetBrains\IntelliJIdea2022.3\tomcat\78f703fe-5891-4017-8725-bca0e72827d2
```

```apl
78f703fe-5891-4017-8725-bca0e72827d2
    |-- conf
    |    |-- Catalina
    |    |    |-- localhost
    |    |         |-- ROOT.xml # 这里面配置了项目的访问路径，虚拟目录。
    |    |-- 其他的配置文件
    |-- logs
    |-- work
```

如果控制台乱码，只需要在配置的tomcat服务器的VM options添加如下即可：

```properties
-Dfile.encoding=UTF-8
```

## 2.5 Servlet的体系结构

Servlet体系结构如下：

```apl
Servlet(interface) # 接口
    |-- GenericServlet(abstract class) # 实现类，实现了Servlet接口，同时也是抽象类
         |-- HttpServlet(abstract class) # 继承类，继承了GenericServlet抽象类，同时也是抽象类
```

**GenericServlet**

`GenericServlet`将`Servlet`接口中其他的方法做了默认空实现，只将`service()`方法作为抽象方法。将来定义`Servlet`类的时候，可以继承`GenericServlet`，实现`service()`方法即可。

```java
@WebServlet("/demo02")
public class Demo02Servlet extends GenericServlet {
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("demo02...");
    }
}
```

**HttpServlet**

通过浏览器提交信息，有很多种协议（get、post）等等。每一次我们都需要自行判断，那么`HttpServlet`抽象类里面就帮我们弄好了这个判断的过程。`HttpServlet`是对`http`协议的一种封装，简化操作。里面有着很多关于`http`协议的方法。

创建一个资源路径为`/dmeo03`的类，继承`HttpServlet`抽象类

```java
@WebServlet("/demo03")
public class Demo03Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post...");
    }
}
```

## 2.6 ServletContext对象

ServletContext是一个全局的储存信息的空间，服务器开始就存在，服务器关闭才释放。WEB容器在启动时，它会为每个Web应用程序都创建一个对应的ServletContext，它代表当前Web应用，并且它被所有客户端共享。

由于一个WEB应用中的所有Servlet共享同一个ServletContext对象，因此Servlet对象之间可以通过ServletContext对象来实现通讯。ServletContext对象通常也被称之为context域对象。当web应用关闭、Tomcat关闭或者Web应用reload的时候，ServletContext对象会被销毁。

一共有两种方法来获取`ServletContext`对象：通过`request`对象来获取、通过`HttpServlet`来获取。

```java
@WebServlet("/demo01ServletContext")
public class Demo01ServletContext extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取ServletContext对象
        ServletContext servletContext1 = req.getServletContext();
        // 获取ServletContext对象
        ServletContext servletContext2 = this.getServletContext();

        // 打印出相同的哈希地址
        System.out.println(servletContext1);
        System.out.println(servletContext2);
        System.out.println(servletContext1 == servletContext2);// true
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

### 2.6.1 获取MIME类型

`MIME`类型是在互联网通信过程中定义的一种文件数据类型。格式为大类型/小类型，例如`text/html`、`image/png`。Tomcat中conf目录下面的we.xml里面就配置了大量的这种类型信息。

获取方法：`String getMimeType(String file)`

```java
@WebServlet("/demo02ServletContext")
public class Demo02ServletContext extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 目的：打印出文件的MIME类型， 就是后缀名称.
        // 获取ServletContext对象
        ServletContext servletContext = this.getServletContext();
        String fileName = "linxuan.jpg";
        String mimeType = servletContext.getMimeType(fileName);
        System.out.println(mimeType); // image/jpeg

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

### 2.6.2 获取域对象

域对象：共享数据。

| API                                     | 作用       |
| --------------------------------------- | ---------- |
| setAttribute(String name, Object value) | 设置域属性 |
| getAttribute(String name)               | 获取域属性 |
| removeAttribute(String name)            | 移除域属性 |

### 2.6.3 文件下载

需求如下：

1. 页面显示超链接
2. 点击超链接后弹出下载提示框
3. 完成图片文件下载

分析如下：

1. 超链接指向的资源如果能够被浏览器解析，那么就会在浏览器中显示。如果不能够解析，则会弹出下载提示框。

2. 我们要求任何资源都必须弹出下载提示框。可以使用响应头设置资源的打开方式：

   `content-disposition:attachment;filename=xxx`

**实现步骤**

1. 定义页面，编辑超链接`href`属性，指向`Servlet`，传递资源名称`filename`
2. 定义`Servlet`
   1. 获取文件名称
   2. 使用字节输入流加载文件进内存
   3. 指定response响应头。
   4. 将数据写出到response输出流

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <a href="/day15/demo01DownLoad?filename=12.jpg">下载图片</a>
</body>
</html>
```

```java
@WebServlet("/demo01DownLoad")
public class Demo01DownLoad extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求参数，也就是获取文件的名称
        String filename = req.getParameter("filename");
        // 使用字节输入流加载文件进内存
        // 获取ServletContext对象
        ServletContext servletContext = this.getServletContext();
        // 找到文件服务器路径，也就是真实路径
        String realPath = servletContext.getRealPath("/image/" + filename);
        // 字节流关联
        FileInputStream fis = new FileInputStream(realPath);

        // 设置response的响应头
        // 设置响应头类型，content-type
        String mimeType = servletContext.getMimeType(filename);
        resp.setHeader("content-type", mimeType);
        // 设置响应头打开方式：content-disposition
        resp.setHeader("content-disposition", "attachment;filename=" + filename);
        // 将输入流的数据写入到输出流中
        ServletOutputStream sos = resp.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len = 0;
        while ((len = fis.read(buff)) != -1) {
            sos.write(buff, 0, len);
        }

        fis.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

# 第三章 Filter

Filter：过滤器，主要负责拦截请求和放行。web的三大组件之一。

当访问服务器的资源时，过滤器可以将请求拦截下来，完成一些特殊的功能。一般用于完成通用的操作。如：登录验证、统一编码处理、敏感字符过滤...

```java
// 注解的方式配置，也可以通过web.xml配置文件配置。访问所有资源，都会执行该过滤器
@WebFilter("/*") 
// 实现接口Filter
public class Demo01Filter implements Filter {
    
    // 服务器启动后，会创建Filter对象，然后调用init方法。只执行一次。用于加载资源
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    // 每一次请求被拦截资源时，会执行。执行多次
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Demo01Filter执行了");

        // 放行。如果不放行，那么访问路径不会执行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 在服务器关闭后，Filter对象被销毁。如果服务器是正常关闭，则会执行destroy方法。只执行一次。用于释放资源
    @Override
    public void destroy() {

    }
}
```

## 3.1 web.xml配置

```xml
<!-- 在Web文件夹下面创建WEB-INF文件夹，然后在里面创建Web.xml配置文件，配置如下内容。 -->
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!--在Web.xml文件配置如下信息-->
    <filter>
        <!--这里是随便声明一个名字, 用于引用过滤器-->
        <filter-name>Demo01</filter-name>
        <!-- 实现Filter类的全类名-->
        <filter-class>com.linxuan.filter.Demo01Filter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Demo01</filter-name>
        <!--拦截路径-->
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

## 3.2 过滤器执行流程

流程如下：执行过滤器 -> 执行放行后的资源 -> 回来执行过滤器放行代码下边的代码。

```java
@WebFilter("/*")
public class Demo02Filter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 对request对象请求消息增强
        System.out.println("Demo02Filter执行了");

        // 放行了
        chain.doFilter(request, response);

        // 对象response对象的响应消息增强
        System.out.println("Demo02Filter回来了");
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
```

```jsp
<!-- index -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
    <%
      System.out.println("index.jsp");
    %>
  </body>
</html>
```

浏览器访问index.jsp页面，控制台输出语句信息如下：

```apl
Demo02Filter执行了
index.jsp
Demo02Filter回来了
```

## 3.3 过滤器配置详解

### 3.3.1 拦截路径配置

1. 具体资源路径：`/index.jsp`。只有访问index.jsp资源时，过滤器才会被执行

2. 拦截目录：`/user/*`。访问/user下的所有资源时，过滤器都会被执行

3. 后缀名拦截：`*.jsp`。访问所有后缀名为jsp资源时，过滤器都会被执行，没有`/`

4. 拦截所有资源：`/*`。访问所有资源时，过滤器都会被执行


### 3.3.2 拦截方式配置

拦截方式配置：资源被访问的方式，例如浏览器直接请求、转发、重定向等等访问方式。例如想要该资源只有被转发的时候被拦截，或者是只有被直接请求的时候被拦截。

两种配置方式：注解配置、web.xml配置

**注解配置**

使用注解来配置拦截方式的话，需要设置dispatcherTypes属性。

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebFilter {

    // 数组格式，代表可以定义多个。默认是DispatcherType.REQUEST，只有直接请求资源的时候过滤器会执行
    DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};
}
```

```java
public enum DispatcherType {
    // 只有是转发访问的时候过滤器才会执行
    FORWARD,
    // 包含访问资源
    INCLUDE,
    // 只有浏览器直接请求资源的时候过滤器会执行
    REQUEST,
    // 异步访问资源
    ASYNC,
    // 错误跳转资源
    ERROR
}
```

下面来测试一下：将dispatcherTypes属性设置为：dispatcherTypes.REQUEST。然后创建一个Servlet来测试如果转发到index.jsp是否会被过滤器所过滤。

```java
// 浏览器直接请求index.jsp资源的时候，过滤器才会执行
@WebFilter(value = "/index.jsp", dispatcherTypes = DispatcherType.REQUEST)
public class Demo01Filter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Demo01Filter执行了");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

```java
@WebServlet("/demo01Servlet")
public class Demo01Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Demo01Servlet...");

        // 转发给index.jsp
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

当从浏览器浏览器直接访问index.jsp，那么过滤器会执行。但是如果访问demo01Servlet转发至index.jsp，过滤器并不会执行，由于设置为了只过滤请求资源，不会过滤转发，因此，过滤器不会过滤。

**web.xml配置**

设置`<dispatcher></dispatcher>`标签即可。我们只需要在`<filter-mapping>`标签内部定义`<dispatcher>`，在`<dispatcher>`内部定义值就好了，同样的也有五个值。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <filter>
        <!--这里是随便声明一个名字, 用于引用过滤器-->
        <filter-name>Demo01</filter-name>
        <filter-class>cn.linxuan.demo01.filter.Demo01Filter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Demo01</filter-name>
        <!--拦截路径-->
        <url-pattern>/*</url-pattern>
        <!--使用web.xml来配置过滤器拦截方式-->
        <dispatcher>FORWARD</dispatcher>
    </filter-mapping>
</web-app>
```

## 3.4 配置多个过滤器

如果有两个过滤器，例如过滤器1和过滤器2，那么执行的顺序如下：过滤器1 -> 过滤器2 -> 资源执行 -> 过滤器2 -> 过滤器1。

过滤器执行的顺序：

1. 注解配置：按照类名的字符串比较规则比较，值小的先执行。下面的就是先执行Demo01Filter。

2. web.xml配置：`<filter-mapping>`谁定义在上边，谁先执行

```java
@WebFilter("/*")
public class Demo01Filter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Demo01Filter执行了");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Demo01Filter回来了");
    }
}
```

```java
@WebFilter("/*")
public class Demo02Filter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        
        System.out.println("Demo02Filter执行了");
        // 放行了
        chain.doFilter(request, response);
        System.out.println("Demo02Filter回来了");
    }
}
```

最后访问index.jsp，会在控制台输出如下信息：

```html
<!--
    Demo01Filter执行了
    Demo02Filter执行了
    index.jsp
    Demo02Filter回来了
    Demo01Filter回来了
-->
```

# 第四章 Listener

Listener概念：监听器，web的三大组件之一。

Servlet的监听器Listener，它是实现了javax.servlet.ServletContextListener接口的服务器端程序，它也是随web应用的启动而启动，只初始化一次，随web应用的停止而销毁。

主要作用是做一些初始化的内容添加工作、设置一些基本的内容、比如一些参数或者是一些固定的对象等等。监听作用域对象的创建和销毁以及属性的相关配置，可以添加一些公共的属性配置，做逻辑判断

主要监听三种作用域：ServletContext(application)、Session、request。

- ServletContext对象----监听ServletContext对象，可以使web应用得知web组件的加载和卸载等运行情况
- HttpSession对象----监听HttpSession对象，可以使web应用了解会话期间的状态并做出反应
- ServletRequest对象----监听ServletRequest对象，可以使web应用控制web请求的生命周期

监听器类型如下：

| 监听对象       | 监听器接口                                              | 监听事件            |
| -------------- | ------------------------------------------------------- | ------------------- |
| ServletContext | ServletContextListener、ServletContextAttributeListener | ServletContextEvent |
| HttpSession    | HttpSessionListener、HttpSessionActivationListener      | HttpSessionEvent    |
| ServletRequest | ServletRequestListener、ServletRequestAttributeListener | ServletRequestEvent |

## 4.1 web.xml配置

Listener配置信息必须在Filter和Servlet配置之前，Listener的初始化（ServletContentListener初始化）比Servlet和Filter都优先，而销毁比Servlet和Filter都慢。

```xml
<listener>
    <listener-class>com.listener.class</listener-class>
</listener>
```

spring使用ContextLoaderListener加载ApplicationContext配置信息。

ContextLoaderListener的作用就是启动Web容器时，自动装配ApplicationContext的配置信息。因为它实现了ServletContextListener这个接口，在web.xml配置这个监听器，启动容器时，就会默认执行它实现的方法。

ContextLoaderListener如何查找ApplicationContext.xml的配置位置以及配置多个xml：如果在web.xml中不写任何参数配置信息，默认的路径是"/WEB-INF/applicationContext.xml"，在WEB-INF目录下创建的xml文件的名称必须是applicationContext.xml。如果是要自定义文件名可以在web.xml里加入contextConfigLocation这个context参数。

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <!-- 采用的是通配符方式，查找WEB-INF/spring目录下xml文件。如有多个xml文件，以“,”分隔。 -->
    <param-value>classpath:spring/applicationContext-*.xml</param-value>
</context-param>

<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

## 4.2 ServletContext监听

ServletContextListener：用于对Servlet整个上下文进行监听（创建、销毁）。

- `public void contextInitialized(ServletContextEvent sce);`：上下文初始化
- `public void contextDestroyed(ServletContextEvent sce);`：上下文销毁
- `public ServletContext getServletContext();`：ServletContextEvent事件，取得一个ServletContext（application）对象

```java
import javax.servlet.*;

@WebListener("/listen")
public class MyListense2 implements ServletContextListener, ServletContextAttributeListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("初始化");
    }
}
```

## 4.3 Session监听

Session属于http协议下的内容，接口位于`javax.servlet.http.*`包下。

HttpSessionListener接口：对Session的整体状态的监听。

- `public void sessionCreated(HttpSessionEvent se);`：session创建
- `public void sessionDestroyed(HttpSessionEvent se);`：session销毁
- `public HttpSession getSession();`：取得当前操作的session
