# 第一章 Filter

Filter：过滤器，主要负责拦截请求和放行。web的三大组件之一。

当访问服务器的资源时，过滤器可以将请求拦截下来，完成一些特殊的功能。一般用于完成通用的操作。如：登录验证、统一编码处理、敏感字符过滤...

## 1.1 快速入门

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

## 1.2 web.xml配置

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

## 1.3 过滤器执行流程

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

## 1.4 过滤器配置详解

### 1.4.1 拦截路径配置

1. 具体资源路径：`/index.jsp`。只有访问index.jsp资源时，过滤器才会被执行

2. 拦截目录：`/user/*`。访问/user下的所有资源时，过滤器都会被执行

3. 后缀名拦截：`*.jsp`。访问所有后缀名为jsp资源时，过滤器都会被执行，没有`/`

4. 拦截所有资源：`/*`。访问所有资源时，过滤器都会被执行


### 1.4.2 拦截方式配置

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

## 1.5 配置多个过滤器

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

# 第二章 Listener

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

## 2.1 web.xml配置

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

## 2.2 ServletContext监听

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

## 2.3 Session监听

Session属于http协议下的内容，接口位于`javax.servlet.http.*`包下。

HttpSessionListener接口：对Session的整体状态的监听。

- `public void sessionCreated(HttpSessionEvent se);`：session创建
- `public void sessionDestroyed(HttpSessionEvent se);`：session销毁
- `public HttpSession getSession();`：取得当前操作的session
