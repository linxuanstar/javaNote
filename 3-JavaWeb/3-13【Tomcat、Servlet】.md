# 第一章 Tomcat

JavaEE（Java Platform, Enterprise Edition）：Java开发语言的分支，是Sun公司为企业级应用推出的标准平台。JavaEE是个大杂烩，包括Applet、EJB、JDBC、JNDI、Servlet、JSP等技术的标准，运行在一个完整的应用服务器上，用来开发大规模、分布式、健壮的网络应用。一共对丁了13项大的规范。

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

# 第三章 ServletContext对象

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

## 3.1 获取MIME类型

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

## 3.2 获取域对象

域对象：共享数据。

| API                                     | 作用       |
| --------------------------------------- | ---------- |
| setAttribute(String name, Object value) | 设置域属性 |
| getAttribute(String name)               | 获取域属性 |
| removeAttribute(String name)            | 移除域属性 |

## 3.3 文件下载

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
