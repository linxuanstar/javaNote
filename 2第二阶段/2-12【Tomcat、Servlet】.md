<!--P675-->

# 第一章 Web回顾

# 第二章 Tomcat

## 2.1 名词解释

<!--P677-->

`JavaEE`：`Java`语言在企业级开发中使用的技术规范的总和，一共规定了13项大的规范。

服务器：安装了服务器软件的计算机。

服务器软件：接受用户的请求，处理请求，做出响应。

web服务器软件：又叫做web容器，可以接受用户的请求，处理请求，做出响应。在web服务器软件当中，可以部署web项目，让用户通过浏览器来访问这些项目。

常见的`java`相关的`web`服务器软件：

* `webLogic`：`Oracle`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `webSphere`：`IBM`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `JBOSS`：`JBOSS`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `Tomcat`：`Apache`基金组织的，中小型的`JavaEE`服务器，仅仅支持少量的`JavaEE`规范。当然，支持`servlet/jsp`。

## 2.2 了解Tomcat

<!--P678-->

`Tomcat`：一款`web`服务器软件

因为时间已经过去很久了，所以没有找到`Tomcat8.5.31`版本的，我用的是`Tomcat8.5.75`版本。具体安装目录：`E:\Tomcat`。

### Tomcat基础

- 下载：`https://tomcat.apache.org/`

- 安装：解压缩下载的压缩包即可。另外，安装的目录不要有中文。

- 卸载：直接删除安装目录即可。


### Tomcat目录

* `bin`：存放可执行文件。
* `conf`：存放配置文件。
* `lib`：存放依赖jar包。
* `logs`：存放日志文件。
* `temp`：存放临时文件。
* `webapps`：存放web项目。
* `work`：存放运行时的数据。

### Tomcat启动

<!--P679-->

启动：找到Tomcat下载位置，`bin/startup.bat`，双击该文件。

访问：浏览器输入`http://localhost:8080`  也就是: `http://别人的ip:8080`    因为Tomcat的端口号就是8080

在启动过程中可能会遇到的两种问题：

1. 黑窗口一闪而过

   * 原因：没有正确的配置JAVA_HOME环境变量

2. 端口号占用了导致启动报错

   * 暴力解决：通过DOS命令来操作，打开cmd，键入netstat -ano，就会出现这时候使用端口号的软件，找到使用端口号为8080的软件。再打开任务管理器，杀死该软件进程。

   * 温柔解决：修改自身端口号。找到Tomcat下载位置，conf/server.xml，双击该文件。找到代码，将port = “8080”修改。

     ```xml
         <Connector port="8080" protocol="HTTP/1.1"
                    connectionTimeout="20000"
                    redirectPort="8443" />
     ```

     > 一般我们会将Tomcat的默认端口设置为80.80端口号是http协议的默认端口号，这样，访问的时候直接输入localhost就可以了，不用输入端口号了。

### Tomcat关闭

1. 正常关闭：`bin/shutdown.bat`或者`Ctrl+C`
2. 强制关闭：点击启动窗口的`×`

## 2.3 Tomcat配置

<!--P681 2.18-->

配置：部署项目，因为Tomcat就是一个web服务器软件，所以需要有项目在Tomcat上面部署。

* **直接将项目放到webapps目录下面就可以。**

  例如：在webapps目录下面创建一个hello文件夹，在hello目录下面创建hello.html文件。hello.html文件里面写一些静态资源，现在已经可以通过Tomcat来访问hello文件了。

  1. 启动Tomcat

  2. 打开浏览器，地址栏输入：`http://localhost:8080/hello/hello.html`

     /hello：项目的访问路径-->也叫做虚拟目录-->和项目名称一样

  这种有一个简化的部署方式，将项目打成一个war包，再将war包放置到webapps目录下面。然后war包会自动解压缩，如果删除的话，删除war包就可以了，项目也会自动删除。<font color = "red">**但是我弄不了。**</font>

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

  建议使用这种方式，假如不需要这个文件了，那么只需要在**index.xml后面加上_bak**，弄成临时文件，`index.xml_bak`。这样就无法读取了，所以也就废掉了。<font color = "red">**即使服务器没有关掉，也会无法读取。**</font>

## 2.4 项目

<!--P682-->

项目有静态项目和动态项目。

java动态项目的目录结构如下：

> -- 项目的根目录
> 	-- WEB-INF目录：
> 		-- web.xml：web项目的核心配置文件
> 		-- classes目录：放置字节码文件的目录
> 		-- lib目录：放置依赖的jar包

## 2.5 Tomcat集成IDEA

<!--P683-->

将Tomcat集成到IDEA中，并且创建JAVAEE项目，部署项目。

# 第三章 Servlet入门

<!--P684-->

## 3.1 基础概念

`Serverlet：server applet`

概念：运行在服务器端的小程序。Servlet就是一个接口，定义了Java类被浏览器访问到(tomcat识别)的规则。将来我们会自己定义一个类，实现Servlet接口，复写这些方法。

## 3.2 快速入门

<!--P685-->

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

## 3.3 执行原理

<!--P686-->

1. 当服务器接受到客户端浏览器的请求后，会解析请求URL路径，获取访问的Servlet的资源路径。
2. 查找`web.xml`文件，看看是否有对应的`<url-pattern>`标签体内容。
3. 如果有，则在找到对应的`<servlet-class>`全类名。
4. Tomcat会将字节码文件加载进内存，并且创建其对象。
5. 调用方法。

## 3.4 Servlet中方法声明周期

<!--P687-->

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

在`web/WEB-INF/web.xml`里面配置下面代码：

```xml
    <!-- 配置demo02 Servlet -->
    <servlet>
        <servlet-name>demo02</servlet-name>
        <servlet-class>cn.com.web.servlet.Demo02Servlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>demo02</servlet-name>
        <url-pattern>/demo02</url-pattern>
    </servlet-mapping>
```

## 3.5 声明周期详解

<!--P688 2.19-->

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

## 3.6 Servlet3.0

<!--P689-->

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

## 3.7 IDEA与Tomcat的相关配置

<!--P690-->

* IDEA会为每一个Tomcat部署的项目单独建立一份配置文件

  查看控制台的log，可以发现：

  ```asciiarmor
  Using CATALINA_BASE: 
  C:\Users\林轩\AppData\Local\JetBrains\IntelliJIdea2021.2\tomcat\3e20fe4c-cdaf-436f-8e70-1d6bfe2452e7
  ```

* 工作空间项目和Tomcat部署的web项目

  Tomcat真正访问的是“Tomcat部署的web项目”，“Tomcat部署的web项目”对应着“工作空间项目”的web目录下的所有资源

  WEB-INF目录下面的资源不能被浏览器直接访问。

* 断点调试：使用开始按钮旁边的“小虫子”启动debug。

<!--P691-->

# 第四章 Servlet

## 4.1 Servlet的体系结构

<!--P692-->

`Servlet -- 接口` ——> `GenericServlet-- 抽象类` ——> `HttpServlet -- 抽象类`

### GenericServlet

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

### HttpServlet

<!--P692 2.20-->

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
    <!--action规定向何处提交表单-->
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

## 4.2 Servlet的相关配置

<!--P693-->

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