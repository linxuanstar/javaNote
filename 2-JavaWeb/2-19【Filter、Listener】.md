# 第一章 Filter

<!--P810-->

## 1.1 基本概念

* 生活中的过滤器：净水器,空气净化器，土匪、
* web中的过滤器：当访问服务器的资源时，过滤器可以将请求拦截下来，完成一些特殊的功能。
* 过滤器的作用：
	* 一般用于完成通用的操作。如：登录验证、统一编码处理、敏感字符过滤...

![](..\图片\2-19【Filter、Listener】\1.Filter过滤器.bmp)

## 1.2 快速入门

<!--P811 3.24 前面的太无聊了，跳过去了-->

步骤如下：

1. 定义一个类，实现接口Filter

2. 复写方法

3. 配置拦截路径
	
	* web.xml
	
	* 注解

```java
/**
 * 过滤器快速入门
 */
@WebFilter("/*") // 访问所有资源，都会执行该过滤器
public class Demo01Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Demo01Filter执行了");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

```

> 注意：
>
>  	1. 如果不放行，那么访问路径不会执行
>  	2. `@WebFilter("/*")`   // 访问所有资源，都会执行该过滤器

## 1.3 Web.xml配置

<!--P812-->

在Web文件夹下面创建*WEB-INF*文件夹，然后在里面创建Web.xml配置文件。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    
<!--在Web.xml文件配置如下信息-->
    <filter>
        <!--这里是随便声明一个名字, 用于引用过滤器-->
        <filter-name>Demo01</filter-name>
        <!--Filter的全类名-->
        <filter-class>cn.linxuan.demo01.filter.Demo01Filter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Demo01</filter-name>
        <!--拦截路径-->
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
</web-app>
```

```xml
<!--在Web.xml文件配置如下信息-->
    <filter>
        <!--这里是随便声明一个名字, 用于引用过滤器-->
        <filter-name>Demo01</filter-name>
        <!--Filter的全类名-->
        <filter-class>cn.linxuan.demo01.filter.Demo01Filter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Demo01</filter-name>
        <!--拦截路径-->
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

## 1.4 过滤器执行流程

<!--P813-->

流程如下：

1. 执行过滤器
2. 执行放行后的资源
3. 回来执行过滤器放行代码下边的代码

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

```html
<!--
控制台输出：
	Demo02Filter执行了
	index.jsp
	Demo02Filter回来了
-->
```

## 1.5 过滤器生命周期方法

* `init()`:在服务器启动后，会创建Filter对象，然后调用init方法。只执行一次。用于加载资源
* `doFilter()`:每一次请求被拦截资源时，会执行。执行多次
* `destroy()`:在服务器关闭后，Filter对象被销毁。如果服务器是正常关闭，则会执行destroy方法。只执行一次。用于释放资源

## 1.6 过滤器配置详解

### 拦截路径配置

<!--P814-->

1. 具体资源路径： `/index.jsp`   

   ​	只有访问index.jsp资源时，过滤器才会被执行

2. 拦截目录： `/user/*`	

   ​	访问/user下的所有资源时，过滤器都会被执行

3. 后缀名拦截： `*.jsp`		

   ​	访问所有后缀名为jsp资源时，过滤器都会被执行，**没有`/`**

4. 拦截所有资源：`/*`		

   ​	访问所有资源时，过滤器都会被执行

### 拦截方式配置

<!--P815-->

拦截方式配置：资源被访问的方式，例如浏览器直接请求、转发、重定向等等访问方式。

* 注解配置：
	
	使用注解来配置拦截方式的话，**需要设置dispatcherTypes属性**
	1. REQUEST：默认值。**只有浏览器直接请求资源的时候过滤器会执行。**
	
	   我们将dispatcherTypes属性设置为：dispatcherTypes.REQUEST。然后创建一个Servlet来测试如果转发到index.jsp是否会被过滤器所过滤。
	
	   ```java
	   // 浏览器直接请求index.jsp资源的时候，过滤器才会执行
	   @WebFilter(value = "/index.jsp", dispatcherTypes = DispatcherType.REQUEST)
	   public class Demo01Filter implements Filter {
	       @Override
	       public void init(FilterConfig filterConfig) throws ServletException {
	   
	       }
	   
	       @Override
	       public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	           System.out.println("Demo01Filter执行了");
	   
	           // 放行
	           filterChain.doFilter(servletRequest, servletResponse);
	       }
	   
	       @Override
	       public void destroy() {
	   
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
	
	   上面有index.jsp源码，所以这里不贴了。
	
	   * 如果通过浏览器直接访问index.jsp，那么过滤器会执行，会在控制台输出如下信息：
	
	     ```html
	     <!--
	         Demo01Filter执行了
	         index.jsp
	     -->
	     ```
	
	   * 但是如果访问demo01Servlet，由于设置为了只过滤请求资源，不会过滤转发，因此，过滤器不会过滤。会在控制台输出如下信息：
	
	     ```html
	     <!--
	         Demo01Servlet...
	         index.jsp
	     -->
	     ```
	
	2. FORWARD：转发访问资源，**只有是转发访问的时候过滤器才会执行。**
	
	   将上面的代码修改一下，只需要将dispatcherTypes属性修改为：dispatcherTypes.FORWARD。
	
	   ```java
	   @WebFilter(value = "/index.jsp", dispatcherTypes = DispatcherType.FORWARD)
	   ```
	
	   那么访问index.jsp和访问demo01Servlet的结果就会发生变化。
	
	   * 如果直接访问index.jsp，由于我们将过滤器的属性设置为FORWARD，访问index.jsp没有转发，所以过滤器不会执行。只是会在控制台上面显示：
	
	     ```html
	     <!--
	         index.jsp
	     -->
	     ```
	
	   * 如果访问demo01Servlet，那么过滤器就会执行，所以会在控制台上面显示：
	
	     ```html
	     <!--
	         Demo01Servlet...
	         Demo01Filter执行了
	         index.jsp
	     -->
	     ```
	
	> 注意：
	>
	> ​      如果我们想要两种情况都能够让过滤器执行呢？我们可以观察WebFilter源码，里面将dispatcherTypes定义成了如下内容：`DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};`。我们可以看到dispatcherTypes是一个数组，所以我们可以定义多个dispatcherTypes属性来让过滤器执行。
	>
	> ​      例如：`@WebFilter(value = "/index.jsp", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})`
	
	3. INCLUDE：包含访问资源
	
	4. ERROR：错误跳转资源
	
	5. ASYNC：异步访问资源
	
* web.xml配置
	
	设置<dispatcher></dispatcher>标签即可
	
	我们只需要在<filter-mapping>标签内部定义<dispatcher>，在<dispatcher>内部定义值就好了，同样的也有五个值。
	
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

## 1.7 过滤器链(配置多个过滤器)

<!--P816 3.25-->

### 执行顺序

如果有两个过滤器：过滤器1和过滤器2，那么执行的顺序如下：

1. 过滤器1
2. 过滤器2
3. 资源执行
4. 过滤器2
5. 过滤器1 

**我们创建两个过滤器，然后再访问index.jsp**

```java
@WebFilter("/*")
public class Demo01Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Demo01Filter执行了");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("Demo01Filter回来了");
    }

    @Override
    public void destroy() {

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

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
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

### 过滤器先后顺序问题

1. 注解配置：按照类名的字符串比较规则比较，值小的先执行
	
	我们肯定好奇，为什么上边的两个过滤器是Demo01Filter先执行，而并不是Demo02Filter先执行呢？这就是因为过滤器先后顺序问题了。根据注解配置，按照类名的字符串比较规则比较，值小的先执行，所以先执行Dmeo01Filter。
	
2. web.xml配置： <filter-mapping>谁定义在上边，谁先执行



## 1.8 案例

### 案例1_登录验证

<!--P817-->

<!--P818-->

需求如下：
1. 访问day17_case案例的资源。验证其是否登录
2. 如果登录了，则直接放行。
3. 如果没有登录，则跳转到登录页面，提示"您尚未登录，请先登录"。

分析：

![](..\图片\2-19【Filter、Listener】\2.案例1_登录验证.bmp)

```java
public class Demo01Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 强制转换类型
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 获取资源请求路径
        String uri = request.getRequestURI();
        // 判断是否包含登陆相关的资源
        if (uri.contains("/login.jsp") || uri.contains("/loginServlet") || uri.contains("/css/") || uri.contains("/js/") || uri.contains("/fonts/") || uri.contains("/checkCodeServlet")) {
            // 包含，用户就是想要登陆，那么直接放行
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // 不包含，需要验证用户是否登陆
            // 从获取session中获取user
            Object user = request.getSession().getAttribute("user");
            if (user != null) {
                // 登陆了，直接放行
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // 没有登陆，跳转登陆页面
                request.setAttribute("login_msg", "您尚未登陆，请登录");
                request.getRequestDispatcher("/login.jsp").forward(request, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
```

### 案例2_敏感词汇过滤

<!--P819-->

![](..\图片\2-19【Filter、Listener】\3.过滤敏感词汇.bmp)

需求
1. 对day17_case案例录入的数据进行敏感词汇过滤
2. 敏感词汇参考《敏感词汇.txt》
3. 如果是敏感词汇，替换为 *** 

分析：
1. 对request对象进行增强。增强获取参数相关方法
2. 放行。传递代理对象

## 1.9 设计模式

<!--P820 3.26-->

<!--P821-->

增强对象的功能，我们可以用到设计模式。**设计模式：一些通用的解决固定问题的方式**

有两种设计模式可以解决，装饰模式和代理模式，这里我们介绍代理模式：

![](..\图片\2-19【Filter、Listener】\4.代理.bmp)

* 基本概念：
  1. 真实对象：被代理的对象，上述图片的联想公司。
  2. 代理对象：上述图片中的西安联想代理商。
  3. 代理模式：代理对象代理真实对象，达到增强真实对象功能的目的

* 实现方式：

  1. 静态代理：有一个类文件描述代理模式

  2. 动态代理：在内存中形成代理类

     * 实现步骤：
       1. 代理对象和真实对象实现相同的接口
       2. 代理对象 = Proxy.newProxyInstance();
       3. 使用代理对象调用方法。
       4. 增强方法
     
       创建一个卖电脑的接口
     
       ```java
       public interface SaleComputer {
       
           public String sale(double money);
       
           public void show();
       }
       ```
     
       创建一个联想真实类实现卖电脑接口
     
       ```java
       /**
        * 真实类，注意：是类，不是对象
        *      类的实例化是对象
        */
       public class Lenovo implements SaleComputer{
           @Override
           public String sale(double money) {
       
               System.out.println("花了" + money + "元购买了一台电脑");
       
               return "联想电脑";
           }
       
           @Override
           public void show() {
               System.out.println("展示电脑...");
           }
       }
       ```
     
       代理对象实现卖电脑接口，调用方法
     
       ```java
       public class ProxyTest {
       
           public static void main(String[] args) {
               // 创建真实对象
               Lenovo lenovo = new Lenovo();
       
               // 动态代理增强Lenovo对象
               /*
                   三个参数：
                   1.类加载器：真实对象.getClass().getClassLoader()
                   2.接口数组：真实对象.getClass().getInterface()
                   3.处理器：new InvocationHandler()匿名内部类，以后业务逻辑代码在这里实现
                */
               SaleComputer proxy_lenovo = (SaleComputer) Proxy.newProxyInstance(lenovo.getClass().getClassLoader(), lenovo.getClass().getInterfaces(), new InvocationHandler() {
                   /**
                    * 代理逻辑编写的方法，代理对象调用的所有方法都会触发该方法执行
                    * 参数：
                    *      1.proxy：代理对象
                    *      2.method：代理对象调用的方法，被封装的方法
                    *      3.args：方法执行时传递的实际参数
                    */
                   @Override
                   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                       System.out.println("invoke方法执行了");
                       System.out.println("代理对象调用的方法：" + method.getName());
                       System.out.println("方法执行时传递的实际参数" + args[0]);
                       return null;
                   }
               });
       
               // 调用方法
               String computer = proxy_lenovo.sale(8000);
               System.out.println(computer);
           }
       }
       ```
     
       ```html
       <!--
           invoke方法执行了
           代理对象调用的方法：sale
           方法执行时传递的实际参数8000.0
           null
       -->
       ```
     
     * 增强方式：
     
       1. 增强参数列表
     
       2. 增强返回值类型
     
       3. 增强方法体执行逻辑
     
       ```java
       public class ProxyTest {
       
           public static void main(String[] args) {
               // 创建真实对象
               Lenovo lenovo = new Lenovo();
       
               // 动态代理增强Lenovo对象
               /*
                   三个参数：
                   1.类加载器：真实对象.getClass().getClassLoader()
                   2.接口数组：真实对象.getClass().getInterface()
                   3.处理器：new InvocationHandler()匿名内部类，以后业务逻辑代码在这里实现
                */
               SaleComputer proxy_lenovo = (SaleComputer) Proxy.newProxyInstance(lenovo.getClass().getClassLoader(), lenovo.getClass().getInterfaces(), new InvocationHandler() {
                   /**
                    * 代理逻辑编写的方法，代理对象调用的所有方法都会触发该方法执行
                    * 参数：
                    *      1.proxy：代理对象
                    *      2.method：代理对象调用的方法，被封装的方法
                    *      3.args：方法执行时传递的实际参数
                    */
                   @Override
                   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
       
                       if (method.getName().equals("sale")) {
                           // 增强参数
                           double money = (double) args[0];
                           money *= 0.85;
                           System.out.println("专车接");
                           // 使用真实对象调用该方法
                           String obj = (String) method.invoke(lenovo, money);
                           System.out.println("免费送货");
                           // 增强返回值
                           return obj + "_鼠标垫";
                       } else {
                           Object obj = method.invoke(lenovo, args);
                           return obj;
                       }
       
                   }
               });
       
               // 调用方法
               String computer = proxy_lenovo.sale(8000);
               System.out.println(computer);
           }
       }
       
       ```

## 1.10 案例2_敏感词汇过滤代码实现

<!--P822-->

在src路径下创建一个格式为GBK的“敏感词汇.txt”

```java
坏蛋
笨蛋
```

创建一个SensitiveWordsFilter实现Filter

```java
@WebFilter("/*")
public class SensitiveWordsFilter implements Filter {

    private List<String> list = new ArrayList<String>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        try {
            // 获取文件真实路径
            ServletContext servletContext = filterConfig.getServletContext();
            String realPath = servletContext.getRealPath("/WEB-INF/classes/敏感词汇.txt");
            // 读取文件
            BufferedReader br = new BufferedReader(new FileReader(realPath));
            // 将文件的每一行数据添加到List集合中
            String line = null;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();

            System.out.println(realPath);
            System.out.println(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 创建代理对象，增强getParameter方法
        ServletRequest proxy_req = (ServletRequest) Proxy.newProxyInstance(servletRequest.getClass().getClassLoader(), servletRequest.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 增强getParameter方法
                // 判断是否是getParameter方法
                if (method.getName().equals("getParameter")) {
                    // 增强返回值
                    // 获取返回值
                    String value = (String) method.invoke(servletRequest, args);
                    if (value != null) {
                        for (String str : list) {
                            if (value.contains(str)) {
                                value = value.replaceAll(str, "***");
                            }
                        }
                    }
                    return value;
                }
                return method.invoke(servletRequest, args);
            }
        });

        // 放行
        filterChain.doFilter(proxy_req, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

```

创建一个Servlet

```java
@WebServlet("/testServlet")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String msg = req.getParameter("msg");

        System.out.println(name + ":" + msg);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

浏览器键入：`localhost/day19/testServlet?name=zhangsan&msg=大坏蛋`

控制台会输出：

```java
D:\Java\IdeaProjects\java_SEStrong\out\artifacts\day19_filter_listener_war_exploded\WEB-INF\classes\敏感词汇.txt
[坏蛋, 笨蛋]

zhangsan:大***
```

# 第二章 Listener

## 2.1 基础概念

<!--P823-->

<!--P824-->

Listener概念：监听器，web的三大组件之一。

我们之前学过一点关于监听的东西，在JavaScript里面就学过。

事件监听机制基本概念如下：
* 事件	：一件事情
* 事件源 ：事件发生的地方
* 监听器 ：一个对象
* 注册监听：将事件、事件源、监听器绑定在一起。 当事件源上发生某个事件后，执行监听器代码

## 2.2 ServletContextListener

`ServletContextListener`监听器对象：监听`ServletContext`对象的创建和销毁

方法如下：
* `void contextDestroyed(ServletContextEvent sce)` ：ServletContext对象被销毁之前会调用该方法

* `void contextInitialized(ServletContextEvent sce)` ：ServletContext对象创建后会调用该方法

  这些方法其实很像我们的ServletContext的`init()`和`destroy()`

## 2.3 实现监听器基本步骤

1. 定义一个类，实现ServletContextListener接口
2. 复写方法
3. 配置
	1. web.xml
			<listener>
			 <listener-class>cn.itcast.web.listener.ContextLoaderListener</listener-class>
			</listener>

			* 指定初始化参数<context-param>
	2. 注解：
		* @WebListener
