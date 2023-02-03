# 第二章 Tomcat

`JavaEE`：`Java`语言在企业级开发中使用的技术规范的总和，一共规定了13项大的规范。

服务器：安装了服务器软件的计算机。

服务器软件：接受用户的请求，处理请求，做出响应。

web服务器软件：又叫做web容器，可以接受用户的请求，处理请求，做出响应。在web服务器软件当中，可以部署web项目，让用户通过浏览器来访问这些项目。

常见的`java`相关的`web`服务器软件：

* `webLogic`：`Oracle`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `webSphere`：`IBM`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `JBOSS`：`JBOSS`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `Tomcat`：`Apache`基金组织的，中小型的`JavaEE`服务器，仅仅支持少量的`JavaEE`规范。当然，支持`servlet/jsp`。

`Tomcat`：一款`web`服务器软件。

## 2.1 Tomcat基础

安装方式：`https://tomcat.apache.org/`，直接解压缩即可，安装目录不要包含中文。

卸载直接删除安装目录即可。

Tomcat目录如下：

* `bin`：存放可执行文件。
* `conf`：存放配置文件。
* `lib`：存放依赖jar包。
* `logs`：存放日志文件。
* `temp`：存放临时文件。
* `webapps`：存放web项目。
* `work`：存放运行时的数据。

启动：找到Tomcat下载位置，`bin/startup.bat`，双击该文件。

访问：浏览器输入`http://localhost:8080`  也就是: `http://别人的ip:8080`    因为Tomcat的端口号就是8080

在启动过程中可能会遇到的两种问题：

1. 黑窗口一闪而过

   原因：没有正确的配置JAVA_HOME环境变量

2. 端口号占用了导致启动报错

   暴力解决：通过DOS命令来操作，打开cmd，键入netstat -ano，就会出现这时候使用端口号的软件，找到使用端口号为8080的软件。再打开任务管理器，杀死该软件进程。

   温柔解决：修改自身端口号。找到Tomcat下载位置，conf/server.xml，双击该文件。找到代码，将port = “8080”修改。

   ```xml
       <Connector port="8080" protocol="HTTP/1.1"
                  connectionTimeout="20000"
                  redirectPort="8443" />
   ```

   > 一般我们会将Tomcat的默认端口设置为80.80端口号是http协议的默认端口号，这样，访问的时候直接输入localhost就可以了，不用输入端口号了。

Tomcat关闭方式：

1. 正常关闭：`bin/shutdown.bat`或者`Ctrl+C`
2. 强制关闭：点击启动窗口的`×`

## 2.2 Tomcat配置

配置：部署项目，因为Tomcat就是一个web服务器软件，所以需要有项目在Tomcat上面部署。

* **直接将项目放到webapps目录下面就可以。**

  例如：在webapps目录下面创建一个hello文件夹，在hello目录下面创建hello.html文件。hello.html文件里面写一些静态资源，现在已经可以通过Tomcat来访问hello文件了。

  1. 启动Tomcat

  2. 打开浏览器，地址栏输入：`http://localhost:8080/hello/hello.html`

     /hello：项目的访问路径-->也叫做虚拟目录-->和项目名称一样

  这种有一个简化的部署方式，将项目打成一个war包，再将war包放置到webapps目录下面。然后war包会自动解压缩，如果删除的话，删除war包就可以了，项目也会自动删除。但是我弄不了。

* **配置conf/server.xml文件。**

  1. 打开`conf/server.xml`文件，在最下方找到`<Host>`标签，在`<Host>`标签里面配置下面代码：

     ```html
     <!-- Context docBase="项目的访问路径" path="项目资源文件"/ -->
     <!-- docBase：项目存放的路径     path：虚拟目录 -->
     <Context docBase="E:\Tomcat\Demo_Tomcat\hello" path="/hello" />
     ```

  2. 打开浏览器，地址栏输入：`http://localhost:8080/hehe/hello.html`

  这种方式**不建议**，因为乱修改会把`server.xml`配置文件修改错误。

* **在conf/catalina/localhost创建任意名称的XML文件**

  1. 在`conf/catalina/localhost`目录下面创建一个xml文件。随意命名，这里命名为`index`。打开`index.xml`文件，在文件里面配置下面代码：

     ```html
     <!-- Context docBase="项目的访问路径"/ -->
     <!-- docBase：项目存放的路径 -->
     <!-- 这里的虚拟目录变成了xml文件的名称，所以这个项目的虚拟目录是index -->
     <Context docBase="E:\Tomcat\Demo_Tomcat\hello" />
     ```

  2. 打开浏览器，地址栏输入：`http://localhost:8080/index/hello.html`

  建议使用这种方式，假如不需要这个文件了，那么只需要在**index.xml后面加上_bak**，弄成临时文件，`index.xml_bak`。这样就无法读取了，所以也就废掉了。**即使服务器没有关掉，也会无法读取。**

## 2.4 项目介绍

项目有静态项目和动态项目。java动态项目的目录结构如下：

```apl
-- 项目的根目录
	-- WEB-INF目录：
		-- web.xml：web项目的核心配置文件
		-- classes目录：放置字节码文件的目录
		-- lib目录：放置依赖的jar包
```

# 第三章 Servlet入门

`Serverlet：server applet`

概念：运行在服务器端的小程序。Servlet就是一个接口，定义了Java类被浏览器访问到(tomcat识别)的规则。将来我们会自己定义一个类，实现Servlet接口，复写这些方法。

## 3.1 快速入门

1. 创建JavaEE项目

2. 定义一个类，实现`Servlet`接口

   ```java
   public class Demo01Servlet implements Servlet{}
   ```

3. 实现接口中的抽象方法

4. 配置`Servlet`

   在`web.xml`文件中，`web-app`标签内部配置

   ```xml
   <!-- 配置Servlet -->
   <servlet>
       <servlet-name>demo01</servlet-name>
       <!--这个是全类名 反射有讲过-->
       <servlet-class>cn.com.web.servlet.Demo01Servlet</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>demo01</servlet-name>
       <url-pattern>/demo01</url-pattern>
   </servlet-mapping>
   ```

执行原理如下：

1. 当服务器接受到客户端浏览器的请求后，会解析请求URL路径，获取访问的Servlet的资源路径。
2. 查找`web.xml`文件，看看是否有对应的`<url-pattern>`标签体内容。
3. 如果有，则在找到对应的`<servlet-class>`全类名。
4. Tomcat会将字节码文件加载进内存，并且创建其对象。
5. 调用方法。

## 3.2 方法声明周期

- 被创建：执行`init`方法，只执行一次。
- 提供服务：执行`service`方法，执行多次。
- 被销毁：执行`destroy`方法，只执行一次。

创建一个`Demo02Servlet`类：

```java
public class Demo02Servlet implements Servlet {
    /**
     * 初始化方法，只有在Servlet创建的时候调用
     * @param servletConfig
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init...");
    }
	
    /**
    * 获取ServletConfig对象
    * ServletConfig：Servlet的配置对象
    * @return
    */
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * 提供服务方法
     * 每一次Servlet被访问，都会执行
     * @param servletRequest
     * @param servletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("service...");
    }

    /**
    * 获取Servlet的一些信息，版本，作者，等等
    * @return
    */
    @Override
    public String getServletInfo() {
        return null;
    }

    /**
     * 销毁方法
     * 在服务器正常关闭的时候就会执行，只会执行一次
     */
    @Override
    public void destroy() {
        System.out.println("destroy...");
    }
}
```

声明周期详解：

1. 被创建：执行`init`方法，只执行一次。

   默认情况下，Tomcat第一次被访问的时候，Servlet被创建。当然，我们也可以配置执行Servlet的创建时机。可以在`web/WEB-INF/web.xml`的`<servlet>`标签下面配置。

   ```xml
   <!--第一次访问的时候，创建。默认值一般为-1，只要是负数，那么就是第一次访问的时候创建-->
   <load-on-startup>负数</load-on-startup>
   <!--在服务器启动的时候，创建。设置值应该为0或者正整数，一般我们设置为0~10-->
   <load-on-satrtup>正数</load-on-satrtup>
   ```

   `Servlet`的`init`方法，只执行一次，**说明一个Servlet在内存中只存在一个对象，Servlet是单例的**。

   但是，正因为此，如果多个用户同时访问，那么就可能存在线程安全问题。

   解决方法：尽量不要在Servlet中定义成员变量，应该定义局部变量，在方法内部定义。即使迫不得已定义了成员变量，那么在方法中也不要对其修改任何值。这样每一次调用一个Servlet对象，成员变量都相同。

2. 提供服务：执行`service`方法，执行多次。

   每次访问`Servlet`时候，`Service`都会被调用一次。

3. 被销毁：执行`destroy`方法，只执行一次。

   `Servlet`被销毁执行。服务器关闭的时候，Servlet被销毁。只有服务器正常关闭的时候，才会执行`destroy`方法。`destroy`方法在`Servlet`被销毁之前执行，一般用于释放资源。

## 3.3 Servlet3.0

每当我们重新创建一个类的时候，都要重复的对该类进行配置。

`Servlet3.0`的存在让我们可以不用创建`web.xml`，直接使用注解来进行配置。步骤如下：

1. 创建一个JavaEE项目(模块)，选择Servlet的版本3.0以上，可以不创建`web.xml`。

2. 定义一个类，实现Servlet接口。

3. 覆盖重写方法

4. 在类上面使用@WebServlet注解，进行配置。

   ```java
   @WebServlet("/demo01")
   @WebServlet("资源路径")
   ```

5. 打开浏览器，将虚拟目录设置为项目名称。运行idea会自动跳转到虚拟目录，然后再输入demo01会进入创建的类，调用service方法。

## 3.4 IDEA与Tomcat的相关配置

* IDEA会为每一个Tomcat部署的项目单独建立一份配置文件

  查看控制台的log，可以发现：

  ```apl
  Using CATALINA_BASE: 
  C:\Users\林轩\AppData\Local\JetBrains\IntelliJIdea2021.2\tomcat\3e20fe4c-cdaf-436f-8e70-1d6bfe2452e7
  ```

* 工作空间项目和Tomcat部署的web项目

  Tomcat真正访问的是“Tomcat部署的web项目”，“Tomcat部署的web项目”对应着“工作空间项目”的web目录下的所有资源

  WEB-INF目录下面的资源不能被浏览器直接访问。

* 断点调试：使用开始按钮旁边的“小虫子”启动debug。

## 3.5 Servlet的体系结构

`Servlet -- 接口` ——> `GenericServlet-- 抽象类` ——> `HttpServlet -- 抽象类`

### 3.5.1 GenericServlet

源码：

```java
public abstract class GenericServlet implements Servlet, ServletConfig, Serializable {
    private static final long serialVersionUID = 1L;
    private transient ServletConfig config;

    public GenericServlet() {
    }

    public void destroy() {
    }

    public String getInitParameter(String name) {
        return this.getServletConfig().getInitParameter(name);
    }

    public Enumeration<String> getInitParameterNames() {
        return this.getServletConfig().getInitParameterNames();
    }

    public ServletConfig getServletConfig() {
        return this.config;
    }

    public ServletContext getServletContext() {
        return this.getServletConfig().getServletContext();
    }

    public String getServletInfo() {
        return "";
    }

    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        this.init();
    }

    public void init() throws ServletException {
    }

    public void log(String message) {
        this.getServletContext().log(this.getServletName() + ": " + message);
    }

    public void log(String message, Throwable t) {
        this.getServletContext().log(this.getServletName() + ": " + message, t);
    }

    public abstract void service(ServletRequest var1, ServletResponse var2) throws ServletException, IOException;

    public String getServletName() {
        return this.config.getServletName();
    }
}
```

`GenericServlet`将`Servlet`接口中其他的方法做了默认空实现，只将`service()`方法作为抽象方法。

将来定义`Servlet`类的时候，可以继承`GenericServlet`，实现`service()`方法即可。

```java
@WebServlet("/demo02")
public class Demo02Servlet extends GenericServlet {
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("demo02...");
    }
}
```

### 3.5.2 HttpServlet

通过浏览器提交信息，有很多中协议，get,post等等。每一次我们都需要自行判断，那么`HttpServlet`抽象类里面就帮我们弄了。

`HttpServlet`是对`http`协议的一种封装，简化操作。里面有着很多关于`http`协议的方法。

例如，get和post提交的方法。

在项目下面web文件夹下面创建一个HTML页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <!--action规定向何处提交表单 eclipse里面/不要加上-->
    <form action="/demo03" method="get">
        <input name="username"><br>
        <input type="submit" value="提交">
    </form>
</body>
</html>
```

再创建一个资源路径为`/dmeo03`的类，继承`HttpServlet`抽象类，并将表单提交后的数据传送到该类。

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

那么，当使用get的方法提交表单，控制台会输出get...。使用post方法提交表单，控制台会输出post...。

## 3.6 Servlet的相关配置

`urlpartten`：`Servlet`访问路径。这是一种配置方式，通过注解也可以配置访问路径。

* 一个`Servlet`可以定义多个访问路径。通过阅读源码我们可看到在`WebServlet`注解中定义了`URLPatterns`，并且它的返回值也是一个数组。所以我们可以通过`WebServlet`注解定义多个访问路径。

  ```java
  // WebServlet注解中定义了URLPatterns
  String[] urlPatterns() default {};
  
  // 我们可以通过WebServlet注解定义多个访问路径
  @WebServlet({"/a", "b", "c"})
  // 定义了资源路径可以为a, b, c。我们可以通过他们三个中的任意一个访问该类。
  ```

* 路径定义规则：

  1. `/xxx`。例如：`@WebServlet("/demo03")`
  2. `/xxx/xxx`：多层路径，目录结构。例如：`@WebServlet("/demo03/d")`
  3. `/xxx/*`：第二个路径可以随便写。这种方式等级比较低，因为是通配符。
  4. `*.do`：可以任意定义后缀名称，可以不是do，可以为a，b。这种方式等级比较低，因为是通配符。另外前面也不要加上`/`。

# 第四章 ServletContext对象

`Servlet`对象：代表了整个Web应用，可以和程序的容器(服务器)来通信。

功能：

1. 获取`MIME`类型。
2. 域对象：共享数据。
3. 获取文件的真实(服务器)路径。

## 4.1 获取ServletContext对象

一共有两种方法来获取`ServletContext`对象：

1. 通过`request`对象来获取
2. 通过`HttpServlet`来获取。

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

## 4.2 获取MIME类型

<!--P731-->

`MIME`类型：在互联网通信过程中定义的一种文件数据类型。格式如下：`大类型/小类型`。例如：`text/html`	`image/png`。

我们可以在服务器软件的`conf`文件夹下面来查看，里面有一个名为`web.xml`的文件，里面配置了信息。

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

## 4.3 域对象

<!--P732-->

域对象：共享数据

* `setAttribute(String name, Object value)`
* `getAttribute(String name)`
* `removeAttribute(String name)`

`ServletContext`对象范围：所有的用户所有的请求的数据。

## 4.4 获取文件的真实路径

<!--P733-->

* `String getRealPath(String path)`：获取文件的真实路径。

我们在项目里面分别创建三个TXT文档：1.在src目录下面创建a.txt。2.在web目录下面创建b.txt。3.创建WEB-INF文件夹，然后在该文件夹下面创建c.txt。

我们的目的是获取文件的真实路径。当服务器运行的时候就会在控制台输出工作空间的目录，然后我们打开就可以看到了一个配置文件。这个配置文件指向了我们项目发布的地方，在这里我们可以找到真实路径。

获取真实路径，之前我们都是用getClassloader，但是这种方法只能够获取a.txt，而b.txt和c.txt是无法获取的。所以我们只能用ServletContext对象的方法。

* 虽然我们从IDEA上面看着，a.txt是在src文件夹下面的，但是我们的项目路径是没有src文件夹的。a.txt实际上是在WEB-INF/classes路径下面的。

```java
@WebServlet("/demo03ServletContext")
public class Demo03ServletContext extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取ServletContext对象
        ServletContext servletContext = this.getServletContext();

        // 获取a.txt文档的真实路径
        String aRealPath = servletContext.getRealPath("/WEB-INF/classes/a.txt");
        // D:\Java\IdeaProjects\java_SEStrong\out\artifacts\day15_response_war_exploded\WEB-INF\classes\a.txt
        System.out.println(aRealPath);

        // 获取b.txt文档的真实路径
        String bRealPath = servletContext.getRealPath("/b.txt");
        // D:\Java\IdeaProjects\java_SEStrong\out\artifacts\day15_response_war_exploded\b.txt
        System.out.println(bRealPath);

        // 获取c.txt文档的真实路径
        String cRealPath = servletContext.getRealPath("/WEB-INF/c.txt");
        // D:\Java\IdeaProjects\java_SEStrong\out\artifacts\day15_response_war_exploded\WEB-INF\c.txt
        System.out.println(cRealPath);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

## 4.5 文件下载

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

**中文名称问题**

从网上可以找到一个`DownLoadUtils`类

```java
import java.util.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class DownLoadUtils {

    public static String getFileName(String agent, String filename) throws UnsupportedEncodingException {
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            //jdk8之后
            Base64.Encoder base64Encoder = Base64.getEncoder();
            filename = "=?utf-8?B?" + base64Encoder.encodeToString(filename.getBytes("utf-8")) + "?=";
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
}
```

中文乱码解决：

1. 获取客户端使用的浏览器版本信息
2. 根据不同的版本信息，设置`filename`的编码方式不同。通过`DownLoadUtils`类就可以了。 