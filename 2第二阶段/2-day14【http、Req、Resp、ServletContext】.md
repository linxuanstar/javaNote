# 第一章 HTTP协议

## 1.1 HTTP概念

<!--P694-->

超文本传输协议（`HyperText Transfer Protocol -- HTTP`）是客户端和服务端进行通信的协议。 

传输协议：定义了客户端和服务器端通信的时候，发送数据的格式。

特点：

- 基于`TCP/IP`的高级协议。
- 默认端口号：`80`。
- 基于请求/响应模型的：一次请求对应一次响应。
- 无状态的：每次请求之间相互独立，不能交互数据。

历史版本：

* `1.0`：每一次请求响应会建立新的连接
* `1.1`：复用连接。

## 1.2 请求消息数据格式

请求消息：客户端发给服务器端的数据

数据格式如下：请求行、请求头、请求空行、请求体。

<!--P695 2.21-->

<!--P696-->

<font color = "red">**请求行 请求头 请求空行 请求体**</font>

1. 请求行：`请求方式 请求URL 请求协议/版本`
   实例：`GET /login.html HTTP/1.1`
   
2. 请求头：`请求头名称: 请求头值`

3. 请求空行

   空行，真的只是一个空行，它主要是起了分割的作用。**分割请求头和请求体**。

4. 请求体(请求正文)

   在GET方法中是没有请求体的，但是在POST中是有请求体的。这是因为POST的参数不显示，都在请求体中封装POST请求消息的请求参数。


字符串格式：

```java
// 请求行
POST /login.html HTTP/1.1

// Chrome浏览器请求头信息如下
Host: localhost
Connection: keep-alive
Cache-Control: max-age=0
sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="98", "Google Chrome";v="98"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
Cookie: Idea-a29be23a=709a7d50-7eeb-4abf-bcd4-105f274eb782; JSESSIONID=B5F03294DAB452B9A4C8ED9A486FCF0B
If-None-Match: W/"314-1645322642326"
If-Modified-Since: Sun, 20 Feb 2022 02:04:02 GMT

// 请求空行

// 请求体，只有POST有，是参数
username = zhangsan
```

### 请求方式

`HTTP`协议一共有着7种请求方式，但是常用的只有两种：

* `GET`请求方式：
  1. 请求的参数在请求行中，也可以理解为在URL后面。
  2. 请求的URL长度是有限制的。
  3. 不太安全。
* `POST`请求方式：
  1. 请求的参数在请求体中。
  2. 请求的URL长度是没有限制的，所以可以用于文件的上传。
  3. 相对安全。

### get方式和post方式区别

GET 和 POST 是 HTTP 请求的两种基本方法，要说它们的区别，接触过 WEB 开发的人都能说出一二。

最直观的区别就是 GET 把参数包含在 URL 中，POST 通过 request body 传递参数。

你可能自己写过无数个 GET 和 POST 请求，或者已经看过很多权威网站总结出的他们的区别，你非常清楚知道什么时候该用什么。

当你在面试中被问到这个问题，你的内心充满了自信和喜悦。

![img](D:\Java\笔记\图片\3-day14【Servlet、HTTP、Request】\get和post方式区别1)



你轻轻松松的给出了一个“标准答案”：

- GET 在浏览器回退时是无害的，而 POST 会再次提交请求。
- GET 产生的 URL 地址可以被 Bookmark，而 POST 不可以。
- GET 请求会被浏览器主动 cache，而 POST 不会，除非手动设置。
- GET 请求只能进行 URL 编码，而 POST 支持多种编码方式。
- GET 请求参数会被完整保留在浏览器历史记录里，而 POST 中的参数不会被保留。
- GET 请求在 URL 中传送的参数是有长度限制的，而 POST 么有。
- 对参数的数据类型，GET 只接受 ASCII 字符，而 POST 没有限制。
- GET 比 POST 更不安全，因为参数直接暴露在 URL 上，所以不能用来传递敏感信息。
- GET 参数通过 URL 传递，POST 放在 Request body 中。

（本标准答案参考自 w3schools）

“很遗憾，这不是我们要的回答！”



![img](https://xxx.ilovefishc.com/forum/201809/22/205231dnd30884dvt4cf8d.png)



请告诉我真相。。。

如果我告诉你 GET 和 POST 本质上没有区别你信吗？

让我们扒下 GET 和 POST 的外衣，坦诚相见吧！

GET 和 POST 是什么？HTTP 协议中的两种发送请求的方法。

HTTP是什么？

HTTP 是基于 TCP/IP 的关于数据如何在万维网中如何通信的协议。

HTTP 的底层是 TCP/IP，所以 GET 和 POST 的底层也是 TCP/IP，也就是说，GET/POST 都是 TCP 链接。

GET 和 POST 能做的事情是一样一样的。你要给 GET 加上 request body，给 POST 带上 url 参数，技术上是完全行的通的。

那么，“标准答案”里的那些区别是怎么回事？

![img](D:\Java\笔记\图片\3-day14【Servlet、HTTP、Request】\get和post方式区别2)

在我大万维网世界中，TCP 就像汽车，我们用 TCP 来运输数据，它很可靠，从来不会发生丢件少件的现象。

但是如果路上跑的全是看起来一模一样的汽车，那这个世界看起来是一团混乱，送急件的汽车可能被前面满载货物的汽车拦堵在路上，整个交通系统一定会瘫痪。

为了避免这种情况发生，交通规则 HTTP 诞生了。

HTTP 给汽车运输设定了好几个服务类别，有 GET, POST, PUT, DELETE 等等。

HTTP 规定，当执行 GET 请求的时候，要给汽车贴上 GET 的标签（设置 method 为 GET），而且要求把传送的数据放在车顶上（url 中）以方便记录。

如果是 POST 请求，就要在车上贴上 POST 的标签，并把货物放在车厢里。

当然，你也可以在 GET 的时候往车厢内偷偷藏点货物，但是这是很不光彩；也可以在 POST 的时候在车顶上也放一些数据，让人觉得傻乎乎的。

HTTP 只是个行为准则，而 TCP 才是 GET 和 POST 怎么实现的基本。

但是，我们只看到 HTTP 对 GET 和 POST 参数的传送渠道（url 还是 requrest body）提出了要求。

“标准答案”里关于参数大小的限制又是从哪来的呢？



![img](D:\Java\笔记\图片\3-day14【Servlet、HTTP、Request】\get和post方式区别3)



在我大万维网世界中，还有另一个重要的角色：运输公司。

不同的浏览器（发起 http 请求）和服务器（接受 http 请求）就是不同的运输公司。

虽然理论上，你可以在车顶上无限的堆货物（url 中无限加参数）。

但是运输公司可不傻，装货和卸货也是有很大成本的，他们会限制单次运输量来控制风险，数据量太大对浏览器和服务器都是很大负担。业界不成文的规定是，（大多数）浏览器通常都会限制 url 长度在 2K 个字节，而（大多数）服务器最多处理 64K 大小的 url。超过的部分，恕不处理。

如果你用 GET 服务，在 request body 偷偷藏了数据，不同服务器的处理方式也是不同的，有些服务器会帮你卸货，读出数据，有些服务器直接忽略，所以，虽然 GET 可以带 request body，也不能保证一定能被接收到哦。

好了，现在你知道，GET 和 POST 本质上就是 TCP 链接，并无差别。

但是由于 HTTP 的规定和浏览器/服务器的限制，导致他们在应用过程中体现出一些不同。

你以为本文就这么结束了？

![img](D:\Java\笔记\图片\3-day14【Servlet、HTTP、Request】\get和post方式区别4)

我们的大 BOSS 还等着出场呢。。。

这位 BOSS 有多神秘？

当你试图在网上找“GET 和 POST 的区别”的时候，那些你会看到的搜索结果里，从没有提到他。

他究竟是什么呢。。。

GET 和 POST 还有一个重大区别，简单的说：GET 产生一个 TCP 数据包；POST 产生两个 TCP 数据包。

长的说：对于 GET 方式的请求，浏览器会把 http header 和 data 一并发送出去，服务器响应 200（返回数据）；而对于 POST，浏览器先发送 header，服务器响应 100（continue），浏览器再发送 data，服务器响应 200（返回数据）。

也就是说，GET 只需要汽车跑一趟就把货送到了，而 POST 得跑两趟，第一趟，先去和服务器打个招呼：“嗨，我等下要送一批货来，你们打开门迎接我”，然后再回头把货送过去。

因为 POST 需要两步，时间上消耗的要多一点，看起来 GET 比 POST 更有效。

因此 Yahoo 团队有推荐用 GET 替换 POST 来优化网站性能。

但这是一个坑！！！跳入需谨慎。

为什么？

- GET 与 POST 都有自己的语义，不能随便混用。
- 据研究，在网络环境好的情况下，发一次包的时间和发两次包的时间差别基本可以无视。而在网络环境差的情况下，两次包的 TCP 在验证数据包完整性上，有非常大的优点。
- 并不是所有浏览器都会在 POST 中发送两次包，比如 Firefox 就只发送一次。

**请求头**

<font color = "red">请求行 请求头 请求空行 请求体</font>

请求头：客户端浏览器告诉服务器一些信息

常见的请求头如下：

1. `User-Agent`：浏览器告诉服务器，访问服务器使用的浏览器款式。

   这样可以让服务器获取头信息，解决浏览器的兼容性问题。

2. `Referer`：`http://localhost/login.html`

   告诉服务器，当前请求从何而来。这样有两个好处：1. 防止盗链发生。2. 统计工作。

## 1.3 响应消息

响应消息：服务器端发送给客户端的数据

数据格式如下：

- 响应行
- 响应头
- 响应空行
- 响应体

响应字符串格式如下：

```html
<!--响应行 协议/版本 响应状态码 状态码描述-->
HTTP/1.1 200
Content-Type: text/html;charset=UTF-8
Content-Length: 100
Date: Mon, 07 Mar 2022 08:01:01 GMT
Keep-Alive: timeout=20
Connection: keep-alive
```

```html
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  Hello response!
  </body>
</html>
```

<!--P717 -->

**响应行**

组成如下：`协议/版本 响应状态码 状态码描述 HTTP/1.1 200`

响应状态码：服务器告诉客户端浏览器本次请求和响应的一个状态。

分类如下：

<font color = "red">100未接受完成，200成功，300重定向，400客户端错误，500服务端错误</font>

* `1XX`：服务器接口客户端消息，但是并没有接受完成，等待一段时间之后，发送1XX状态码。

* `2XX`：成功。代表：200

* `3XX`：重定向。302(重定向)， 304(访问缓存)

* `4XX`：客户端错误。404(请求路径没有对应的资源)， 405(请求方式没有对应的`doXXX`方法)

  `404`：输入的路径在服务器上面并没有该资源。

  `405`：请求方式没有对应的`doXXX`方法

* `5XX`：服务器端出现错误。

  `500`：服务器内部出现异常

<!--P718-->

**响应头**

格式：`头名称：值`

常见的响应头有：

1. `Content-Type`：服务器告诉客户端本次响应体数据格式以及编码格式

2. `Content-disposition`：服务器告诉客户端以什么格式打开响应体数据

   常用的值如下：

   `in-line`：默认值，在当前页面内部打开。不经常使用

   `attachment`：以附件的形式打开响应体，通常用于文件下载。

**响应体**

响应体：传输的数据。

# 第二章 Request

<!--P697 2.22-->

![](D:\Java\笔记\图片\3-day14【Servlet、HTTP、Request】\1.png)

## 2.1 request对象和response对象的原理

* `request`和`response`对象是由服务器创建的。我们只是来使用他们。Tomcat创建的。
* `request`对象是来获取请求消息的，而`response`对象是来设置响应消息的

## 2.2 request对象继承体系结构

<!--P698 2.23-->

```java
ServletRequest	-- 接口
	|
HttpServletRequest	-- 接口
	|
org.apache.catalina.connector.RequestFacade 类(Tomcat编写)
```

## 2.3 request获取请求消息

<!--P699-->

<!--P700-->

根据之前所学我们可以知道请求消息数据格式一共有四种：<font color = "red">**请求行、请求头、请求空行、请求体**</font>。而如果我们需要获取的话，我们不需要获取请求空行。只需要获取请求行、请求头和请求体就可以了。

### 获取请求行数据

请求行数据格式如下：

```java
请求方式 请求URL 请求协议/版本
GET /day14/demo01?name=zhangsan HTTP/1.1
// GET：请求方式，有GET、POST等一共7种方式，我们常用的只有两种
// /day14：虚拟目录
// /demo01：Servlet路径。就是实现Servlet接口的类的配置的路径。
// name=zhangsan：GET方式的请求参数。POST的请求参数在请求体里面。
// /day14/demo01：请求URI
// http://localhost/day14/demo01：请求URL
// HTTP/1.1：请求协议及版本
```

获取请求行数据重要的方法有两个：获取虚拟目录以及获取请求URI。

具体方法如下：

* `String getMethod()`：获取请求方式 GET

* `String getContextPath()`：**获取虚拟目录** /day14

* `String getServletPath()`：获取Servlet路径 /demo01

* `String getQueryString()`：获取get方式请求参数 name=zhangsan

* `String getRequestURI()`：**获取请求URI** /day14/demo01

* `StringBuffer getRequestURL()`：获取请求URL 

  URL：统一资源定位符		相当于中华人民共和国

  URI：统一资源标识符		相当于共和国

  所以URI更大

* `String getProtocol()`：获取协议及版本 HTTP/1.1

* `String getRemoteAddr()`：获取客户及的IP地址

  ```java
  @WebServlet("/demo01Request")
  public class Demo01Request extends HttpServlet {
      // 当获取请求方式为GET方法时：
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  
          // - String getMethod()：获取请求方式 GET
          String method = req.getMethod();
          System.out.println(method);
          // - String getContextPath()：获取虚拟目录 /day14
          String contextPath = req.getContextPath();
          System.out.println(contextPath);
          // - String getServletPath()：获取Servlet路径 /demo01
          String servletPath = req.getServletPath();
          System.out.println(servletPath);
          // - String getQueryString()：获取get方式请求参数 name=zhangsan
          String queryString = req.getQueryString();
          System.out.println(queryString);
          // - String getRequestURI()：获取请求URI /day14/demo01
          String requestURI = req.getRequestURI();
          System.out.println(requestURI);
          // - StringBuffer getRequestURL()：获取请求URL
          StringBuffer requestURL = req.getRequestURL();
          System.out.println(requestURL);
          // - String getProtocol()：获取协议及版本 HTTP/1.1
          String protocol = req.getProtocol();
          System.out.println(protocol);
          // - String getRemoteAddr()：获取客户及的IP地址
          String remoteAddr = req.getRemoteAddr();
          System.out.println(remoteAddr);
  
      }
  
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      }
  }
  ```

  ```java
  GET							// 获取到的请求方式
  /day14_ServletHttpRequest		// 获取到的虚拟目录
  /Demo01Request					// 获取到的Servlet路径
  name=zhangsan					// 获取到的请求参数
  /day14_ServletHttpRequest/Demo01Request	// 获取到的URI
  http://localhost/day14_ServletHttpRequest/Demo01Request 		// 获取到的URL
  HTTP/1.1						// 获取到的协议及版本
  0:0:0:0:0:0:0:1					// 获取IP地址 IPv6
  ```

### 获取请求头数据

- `String getHeader(String name)`：通过请求头的名称获取请求头的值。
- `Enumeration<String> getHeaderNames()`：获取所有的请求头名称。

<!--P701 2.24-->

* `String getHeader(String name)`：通过请求头的名称获取请求头的值。

  请求头有两个：`User-Agent`、`Referer`

  `User-Agent`：

  ```java
  @WebServlet("/Demo03Request")
  public class Demo03Request extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          // String getHeader(String name)：通过请求头的名称获取请求头的值。
          // User-Agent：浏览器告诉服务器，访问服务器使用的浏览器款式。
          // Referer：http://localhost/login.html 告诉服务器，当前请求从何而来。这样有两个好处
  
          String header = req.getHeader("user-agent");
          if (header.contains("Chrome")) {
              System.out.println("谷歌浏览器");// 打印 谷歌浏览器
          } else if (header.contains("Firefox")) {
              System.out.println("火狐浏览器");
          }
  
      }
  
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  
      }
  }
  ```

  `Referer`：

  ```java
  @WebServlet("/Demo04Request")
  public class Demo04Request extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          // String getHeader(String name)：通过请求头的名称获取请求头的值。
          // User-Agent：浏览器告诉服务器，访问服务器使用的浏览器款式。
          // Referer：http://localhost/login.html 告诉服务器，当前请求从何而来。
  
          String refererValue = req.getHeader("referer");
          // 假如是从地址栏进入的，那么就会打印NULL，因为这时候是直接访问的服务器，没有从任何地方来
          System.out.println(refererValue);
          // 可以通过超链接来进入，在web文件夹下面创建一个HTML页面，使用超链接。
          // 打印 http://localhost/day14_ServletHttpRequest/login.html
      }
  
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  
      }
  }
  ```

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>Title</title>
  </head>
  <body>
      <a href="/day14_ServletHttpRequest/Demo04Request">链接</a>
  </body>
  </html>
  ```

* `Enumeration<String> getHeaderNames()`：获取所有的请求头名称。

  ```java
  @WebServlet("/Demo02Request")
  public class Demo02Request extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          // 目的：获取请求头数据
          // 获取所有的请求头名称，注意，只是名称，没有值
          Enumeration<String> headerNames = req.getHeaderNames();
          while (headerNames.hasMoreElements()) {
              String name = headerNames.nextElement();
              // 通过获取到的请求头名称，来获取值
              String value = req.getHeader(name);
              // 打印获取到的请求头名称和值PAYPALISHIRING
              System.out.println(name + "-->" + value);
          }
      }
  
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  
      }
  }
  ```


### 获取请求体数据

<!--P702 2.26-->

创建一个HTML表单，让用户输入数据，提交到`Demo05Request`类里面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="/day14_ServletHttpRequest/Demo05Request" method="post">
        <input type="text" placeholder="请输入用户名" name="username"><br>
        <input type="text" placeholder="请输入密码" name="password"><br>
        <input type="submit" value="提交">
    </form>
</body>
</html>
```

`Demo05Request`类里面定义如下：

```java
// 配置类的资源路径为Demo05Request
@WebServlet("/Demo05Request")
public class Demo05Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 目的：获取请求体信息
        BufferedReader br = req.getReader();
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
```

## 2.4 request其他功能

<!--P703-->

### 获取请求参数通用方法

<!--P704 2.28-->

不论是get还是post请求方式都可以使用下列方法。

* `String getParameter(String name)`：根据参数名称获取参数值。
* `String[] getParameterValues(String name)`：根据参数名称获取参数值的数组。
* `Enumeration<String> getParameterNames()`：获取所有请求的参数名称。
* `Map<String, String[]> getParameterMap()`：获取所有参数的map集合。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="/day14_ServletHttpRequest/Demo06Request" method="post">
        <input type="text" placeholder="请输入用户名" name="username"><br>
        <input type="text" placeholder="请输入密码" name="password"><br>
        <input type="checkbox" name="hobby" value="game">游戏
        <input type="checkbox" name="hobby" value="study">学习
        <br>
        <input type="submit" value="提交">
    </form>
</body>
</html>
```

```java
@WebServlet("/Demo06Request")
public class Demo06Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // String getParameter(String name)：根据参数名称获取参数值。
        String username = req.getParameter("username");
        System.out.println(username);
        System.out.println("==============");

        // String[] getParameterValues(String name)：根据参数名称获取参数值的数组。
        String[] hobbies = req.getParameterValues("hobby");
        System.out.println("hobby-->");
        for (String hobby : hobbies) {
            System.out.println(hobby);
        }
        System.out.println("=============");

        // Enumeration<String> getParameterNames()：获取所有请求的参数名称。
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            System.out.println(name);
            // 根据当前的参数名称获取参数值的数组
            String[] parameterValues = req.getParameterValues(name);
            for (String parameterValue : parameterValues) {
                System.out.println(parameterValue);
            }
        }
        System.out.println("==============");

        // Map<String, String[]> getParameterMap()：获取所有参数的map集合。
        // 获取Map集合
        Map<String, String[]> parameterMap = req.getParameterMap();
        // 获取Map集合中的所有key
        Set<String> keySet = parameterMap.keySet();
        // 遍历所有的key， 通过key来找到value
        for (String key : keySet) {
            System.out.println(key);
            // 通过key来找到value, value有可能是数组，遍历数组打印
            String[] strings1 = parameterMap.get(key);
            for (String s : strings1) {
                System.out.println(s);
            }
            System.out.println("----");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 因为这些方法都是通用的，所以我们可以只写一次，然后直接调用另一个方法就可以了。
        this.doGet(req, resp);
    }
}
```

### 乱码问题

<!--P705-->

在`Tomcat8`中，如果使用的是`get`方式请求服务器，那么`Tomcat`就会自动将乱码问题解决。但是如果使用的是`post`请求方式来请求服务器，那么乱码问题依旧存在。

解决方式如下：

* 在获取参数前，设置`request`的编码与前端页面的编码一致即可。
* 这是因为他们都是使用的是流对象，所以需要设置流对象的编码。`req.setCharacterEncoding("utf-8");`

### 请求转发

<font color = "red">req请求转发、resp响应重定向</font>

<!--P706 3.01-->

请求转发：一种在服务器内部的资源跳转方式。当浏览器地址栏输入URL之后会访问资源路径，但是该类定义的功能不能够太多，但我们又需要很多功能，所以我们可以让它转发给另一个类。

步骤如下：

1. 通过`request`对象获取请求转发器对象：`RequestDispatcher getRequestDispatcher(String path)`
2. 使用`RequestDispatcher`对象来进行转发：`forward(ServletRequest request, ServletResponse response)` 

特点：

- 浏览器地址栏路径是不会发生任何变化的。
- 只能转发到当前服务器内部资源当中，不能够转发互联网当中资源。
- <font color = "red">转发仅仅只是**一次请求**，不是两次，也不是三次</font>。

```java
@WebServlet("/Demo01Request")
public class Demo01Request extends HttpServlet {
    // 当获取请求方式为GET方法时：
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Demo01Request访问了");

        // 不建议这么使用，因为我们只用创建一次对象，只转发一次。所以没必要创建一个对象，直接匿名就可以了，使用链式编程。
        // RequestDispatcher requestDispatcher = req.getRequestDispatcher("/Demo02Request");
        // requestDispatcher.forward(req, resp);

        // Demo01Request访问了
        // Demo02Request访问了
        req.getRequestDispatcher("/Demo02Request").forward(req, resp);

        // 控制台输出：Demo01Request访问了，但是浏览器404.无法转发。
        // req.getRequestDispatcher("www.baidu.com").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

```java
@WebServlet("/Demo02Request")
public class Demo02Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Demo02Request访问了");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

### 共享数据域对象

<!--P707-->

域对象：一个有着作用范围的对象，可以在范围内共享数据。

`request域`：代表一次请求的范围，一般用于请求转发的多个资源中共享数据

方法如下：

* `void setAttribute(String name, Object o)`：存储数据。
* `Object getAttribute(String name)`：通过键获取值。
* `void removeAttribute(String name)`：通过键移出键值对。

```java
@WebServlet("/Demo01Request")
public class Demo01Request extends HttpServlet {
    // 当获取请求方式为GET方法时：
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Demo01Request访问了");

        req.setAttribute("msg", "hello world");
        req.getRequestDispatcher("/Demo02Request").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

```java
@WebServlet("/Demo02Request")
public class Demo02Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object msg = req.getAttribute("msg");
        System.out.println(msg);
        System.out.println("Demo02Request访问了");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

/*
控制台输出：
    Demo01Request访问了
    hello world
    Demo02Request访问了
*/
```

### 获取ServletContext

<!--P708-->

* `ServletContext getServletContext()`：获取`ServletContext`对象。

```java
@WebServlet("/Demo01Request")
public class Demo01Request extends HttpServlet {
    // 当获取请求方式为GET方法时：
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        // org.apache.catalina.core.ApplicationContextFacade@7ca6a408
        // 获取到对象，打印，哈希值
        System.out.println(servletContext);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```



## 2.5 用户登陆案例

案例：用户登陆

<!--P709-->

**需求**

1. 编写`login.html`登陆页面		`username & password`两个输入框
2. 使用`Druid`数据库连接池技术，操作`mysql`，`day14`数据库中`user`表
3. 使用`JDBCTemplate`技术封装`JDBC`
4. 登陆成功跳转到`SuccessServlet`    展示：登陆成功！用户名，欢迎您
5. 登陆失败跳转到`FailServlet`     展示：登陆失败，用户名或者密码错误

**分析**

<!--P708-->

![](D:\Java\笔记\图片\3-day14【Servlet、HTTP、Request】\2.png)

**实现**

<!--P709-->

<!--P710-->

<!--P711-->

<!--P712-->

1. 创建项目，导入`HTML`页面，导入`properties`配置文件，导入`jar`包，`add as libiary`。

2. 打开数据库，创建`day14`数据库，创建user表，并设置一些数据进去。

   ```sql
   -- 查询当前正在使用的数据库名称
   SELECT DATABASE();
   -- 创建day14数据库
   CREATE DATABASE day14;
   -- 进入，使用day14数据库
   USE day14;
   -- 查询所有数据库名称
   SHOW DATABASES;
   -- 创建表
   CREATE TABLE USER(
   	id INT PRIMARY KEY AUTO_INCREMENT,
   	username VARCHAR(32) UNIQUE NOT NULL,
   	PASSWORD VARCHAR(20) NOT NULL
   );
   -- 查询当前使用数据库的所有表
   SHOW TABLES;
   -- 查询USER表中内容
   SELECT * FROM USER;
   -- 插入信息
   INSERT INTO USER (username, PASSWORD) VALUES ("lisi", "456");
   ```

3. 前端页面代码：

   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <title>Title</title>
   </head>
   <body>
       <!--action的路径是虚拟路径加上Servlet资源路径-->
       <form action="/day14_ServletHttpRequest/LoginServlet" method="post">
           用户名称：<input type="text" placeholder="请输入用户名" name="username"><br>
           密码：<input type="password" placeholder="请输入密码" name="password"><br>
           <input type="submit" value="提交">
       </form>
   </body>
   </html>
   ```

4. 创建User类，来存放获取的数据

   ```java
   package cn.com.demo03.domain;
   
   public class User {
       private int id;
       private String username;
       private String password;
   
       public int getId() {
           return id;
       }
   
       public void setId(int id) {
           this.id = id;
       }
   
       public String getUsername() {
           return username;
       }
   
       public void setUsername(String username) {
           this.username = username;
       }
   
       public String getPassword() {
           return password;
       }
   
       public void setPassword(String password) {
           this.password = password;
       }
   
       @Override
       public String toString() {
           return "User{" +
                   "id=" + id +
                   ", username='" + username + '\'' +
                   ", password='" + password + '\'' +
                   '}';
       }
   }
   ```

5. 创建操作数据库中User表的类

   ```java
   package cn.com.demo03.dao;
   
   /**
    * 操作数据库中User表的类
    */
   public class UserDao {
   
       // 声明JDBCTemplate对象共用
       private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
   
       /**
        * 登陆方法
        * @param loginUser 只有用户名称和密码
        * @return user包含用户全部数据，没有查询到，返回null
        */
       public User login(User loginUser) {
           try {
               // 编写SQL
               String sql = "select * from user where username = ? and password = ?";
   
               // 调用query方法
               User user = template.queryForObject(sql,
                       new BeanPropertyRowMapper<User>(User.class),
                       loginUser.getUsername(), loginUser.getPassword());
   
               return user;
           } catch (DataAccessException e) {
               // 通常会把异常弄到日志里面，但是我们并没有学习日志，就这样抛出就可以了
               e.printStackTrace();
               return null;
           }
       }
   }
   ```

6. 创建`JDBC`工具类，使用`Druid`连接池技术

   ```java
   package cn.com.demo03.util;
   
   /**
    *  JDBC工具类，使用Druid连接池技术
    */
   public class JDBCUtils {
   
       private static DataSource ds;
   
       static {
   
           try {
               // 加载配置文件
               Properties pro = new Properties();
               // 使用ClassLoader加载配置文件，获取字节输入流
               InputStream resourceAsStream = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
               pro.load(resourceAsStream);
   
               // 初始化连接池对象
               ds = DruidDataSourceFactory.createDataSource(pro);
           } catch (IOException e) {
               e.printStackTrace();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
       /**
        * 获取连接池对象
        *
        */
       public static DataSource getDataSource() {
           return ds;
       }
   
       /**
        * 获取连接Connection对象
        */
       public static Connection getConnection() throws SQLException {
           return ds.getConnection();
       }
   }
   
   ```

7. 单元测试

   ```java
   // 自己做的单元测试，看看对不对
   package cn.com.demo03.test;
   
   public class UserDaoTest {
   
       @Test
       public void testLogin() {
           User loginuser = new User();
           loginuser.setUsername("zhangsan");
           loginuser.setPassword("12311");
   
           UserDao dao = new UserDao();
           User user = dao.login(loginuser);
   
           System.out.println(user);
       }
   
   }
   ```

8. `Servlet`类

   ```java
   package cn.com.demo03.servlet;
   
   @WebServlet("/LoginServlet")
   public class LoginServlet extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           // 设置编码
           req.setCharacterEncoding("utf-8");
           // 获取请求参数
           String username = req.getParameter("username");
           String password = req.getParameter("password");
           // 封装user对象
           User loginUser = new User();
           loginUser.setUsername(username);
           loginUser.setPassword(password);
   
           // 调用UserDao的login方法
           UserDao dao = new UserDao();
           User user = dao.login(loginUser);
   
           // 判断user
           if (user == null) {
               // 登陆失败
               req.getRequestDispatcher("/failServlet").forward(req, resp);
           } else {
               // 登陆成功
               // 存储数据
               req.setAttribute("user", user);
               // 转发
               req.getRequestDispatcher("/successServlet").forward(req, resp);
           }
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           this.doGet(req, resp);
       }
   }
   
   ```

9. 成功页面和失败页面编写

   ```java
   package cn.com.demo03.servlet;
   
   @WebServlet("/failServlet")
   public class FailServlet extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           // 给页面写一句话
           // 设置编码
           resp.setContentType("text/html;charset=utf-8");
           // 输出
           resp.getWriter().write("登陆失败，用户名称或者密码错误");
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           this.doGet(req, resp);
       }
   }
   ```

   ```java
   package cn.com.demo03.servlet;
   
   @WebServlet("/successServlet")
   public class SuccessServlet extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           // 获取request域中共享的user对象
           User user = (User) req.getAttribute("user");
   
           if (user != null) {
               // 给页面写一句话
               // 设置编码
               resp.setContentType("text/html;charset=utf-8");
               // 输出
               resp.getWriter().write("登陆成功!" + user.getUsername() + "欢迎您");
           }
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           this.doGet(req, resp);
       }
   }
   ```


## 2.6 Beanutils

```java
package cn.com.demo03.servlet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
        req.setCharacterEncoding("utf-8");
        // 获取请求参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // 封装user对象
        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        // 调用UserDao的login方法
        UserDao dao = new UserDao();
        User user = dao.login(loginUser);

        // 判断user
        if (user == null) {
            // 登陆失败
            req.getRequestDispatcher("/failServlet").forward(req, resp);
        } else {
            // 登陆成功
            // 存储数据
            req.setAttribute("user", user);
            // 转发
            req.getRequestDispatcher("/successServlet").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

```

获取请求参数，封装对象，如果有很多的话，会很麻烦。

```java
// 获取请求参数
String username = req.getParameter("username");
String password = req.getParameter("password");
// 封装user对象
User loginUser = new User();
loginUser.setUsername(username);
loginUser.setPassword(password);
```

这时候就有`Beanutils`了，导入`Beanutils`包，然后将上面代码就可以替换下面代码了。这样，不论有多少参数，那么Map集合就可以搜集起来。

```java
// 获取所有请求参数
Map<String, String[]> parameterMap = req.getParameterMap();
// 创建User对象
User loginUser = new User();
// 使用BeanUtils封装
try {
    BeanUtils.populate(loginUser, parameterMap);
} catch (IllegalAccessException e) {
    e.printStackTrace();
} catch (InvocationTargetException e) {
    e.printStackTrace();
}
```

# 第三章 Response对象

<!--P719-->

<!--P720 3.08-->

`Response`对象功能：设置响应消息。

## 3.1 方法

* `setStatus(int sc)` ：设置状态码
* `setHeader(String name, String value)`：设置响应头
* `sendRedirect(String location)` ：简单的重定向，重定向很常见。一共有三个参数，而我们只需要修改一个参数，所以提供了这个方法。这是一种非常简单的重定向方法。
* `PrintWriter getWriter()`：获取字符输出流
* `ServletOutputStream getOutputStream()`：获取字节输出流。

## 3.2 重定向实现

重定向：资源跳转的方式

![](D:\Java\笔记\图片\3-day15【Response】\1重定向.png)

```java
@WebServlet("/demo01Response")
public class Demo01Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 演示重定向
        System.out.println("demo01....");
/*        // 设置状态码
        resp.setStatus(302);
        // 设置响应头，设置地址和虚拟路径下面的资源文件。
        resp.setHeader("location", "/day15/demo02Response");*/

        // 简单的重定向方法
        resp.sendRedirect("/day15/demo02Response");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

```java
@WebServlet("/demo02Response")
public class Demo02Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 演示重定向
        System.out.println("dmeo02...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
/*
会在控制台输出：
    demo01...
    demo02...
*/
```

## 3.3 重定向特点

<!--P721 3.09--> 

我们首先来看一下转发的特点：`forward`

- 地址栏路径不变
- 转发只能访问当前服务器下面的资源
- 转发是<font color = "red">**一次**</font>请求

重定向的特点与转发正好相反：`redirect`

- 地址栏发生变化
- 重定向可以访问其他站点(服务器)的资源
- 重定向是<font color = "red">**两次**</font>请求

## 3.4 路径写法

<!--P722-->

<!--P723-->

### 相对路径

相对路径：通过相对路径不可以确定唯一资源

规则：找到当前资源和目标资源之间的相对位置关系。

- `./` ：当前目录
- `../`：后退一级目录

### 绝对路径

绝对路径：通过绝对路径可以确定唯一资源。以`/`开头的路径

写绝对路径的规则：判断定义的路径是给谁使用的？判断请求将从哪里过来，哪里发出。

* 给客户端浏览器使用：需要加虚拟目录，就是项目的访问路径

  建议虚拟目录设置为动态获取的值，这样，即使更改了项目的虚拟目录，也不会有任何后果。可以使用：`request.getContextPath()`方法。

  `<a>`， `<form>`重定向。

* 给服务器使用：不需要加上虚拟目录。

  转发路径。

## 3.5 服务器输出字符数据到浏览器

<!--P724-->

输出字符数据到浏览器的步骤：首先需要获取字符输出流。然后再输出数据就可以了。

```java
@WebServlet("/demo03Response")
public class Demo03Response extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取字符输出流对象
        PrintWriter writer = resp.getWriter();
        // 输出数据
        writer.write("Hello");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

但是要注意，由于服务器和浏览器的编码格式不同，所以会导致乱码问题，对此我们的解决方法如下：

* 告诉浏览器，服务器发送的消息体数据的编码，建议浏览器使用该编码解码。

  但是，必须记住，**应该在获取字符输出流之前就要设置编码**。

```java
// 告诉浏览器，服务器发送的消息体数据的编码。建议浏览器使用该编码解码。
// resp.setHeader("content-type", "text/html; charset = utf-8");
// 当然，上述也有简写的方法
resp.setContentType("text/html; charset = utf-8");
```

> 切记，UTF-8中间不能有空格

## 3.6 服务器输出字节数据到浏览器

<!--P725 3.10-->

```java
@WebServlet("/demo04Response")
public class Demo04Response extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码与解码
        // 切记，UTF-8中间不能有空格
        resp.setContentType("text/html; charset = utf-8");

        // 获取字节流输出对象
        ServletOutputStream outputStream = resp.getOutputStream();
        // 输出
        outputStream.write("你好".getBytes("utf-8"));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

## 3.7 验证码案例

<!--P726-->

<!--P727 3.11-->

```java
@WebServlet("/demo05Response")
public class Demo05Response extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int width = 100;
        int height = 50;
        
        // 创建对象，在内存中弄一个图片，验证码图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 美化图片
        // 画笔对象
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.PINK);
        graphics.fillRect(0, 0, width, height);
        // 画边框
        graphics.setColor(Color.BLUE);
        graphics.drawRect(0, 0, width - 1, height - 1);

        String str = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";
        // 生成随机角标
        Random ran = new Random();
        for (int i = 1; i <= 4; i++) {
            int index = ran.nextInt(str.length());
            // 获取字符
            char ch = str.charAt(index);
            // 写入验证码
            graphics.drawString(ch + "", width / 5 * i, height / 2);
        }
        // 画干扰线
        graphics.setColor(Color.GREEN);
        // 随机生成坐标点
        for (int i = 0; i < 10; i++) {
            int x1 = ran.nextInt(width);
            int x2 = ran.nextInt(width);

            int y1 = ran.nextInt(height);
            int y2 = ran.nextInt(height);
            graphics.drawLine(x1, y1, x2, y2);
        }

        // 图片输出
        ImageIO.write(image, "jpg", resp.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

<!--P728-->

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <script>
        window.onload = function () {
            // 获取图片对象
            var img = document.getElementById("checkCode");
            // 绑定单击事件
            img.onclick = function () {
                // 加一个时间戳，这样每一次请求的都不一样，不会加载浏览器缓存的图片
                var date = new Date().getTime();
                img.src = "/day15/demo05Response?" + date;
            }
        }
    </script>
</head>
<body>
    <img id="checkCode" src="/day15/demo05Response">
    <a id="change" href="/day15/register.html">看不清，换一张？</a>
</body>
</html>
```

# 第四章 ServletContext对象

<!--P729 3.12-->

`Servlet`对象：代表了整个Web应用，可以和程序的容器(服务器)来通信。

功能：

1. 获取`MIME`类型。
2. 域对象：共享数据。
3. 获取文件的真实(服务器)路径。

## 4.1 获取ServletContext对象

<!--P730-->

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

<!--P734-->

<!--P735-->

### 需求

1. 页面显示超链接
2. 点击超链接后弹出下载提示框
3. 完成图片文件下载

### 分析

1. 超链接指向的资源如果能够被浏览器解析，那么就会在浏览器中显示。如果不能够解析，则会弹出下载提示框。

2. 我们要求任何资源都必须弹出下载提示框。可以使用响应头设置资源的打开方式：

   `content-disposition:attachment;filename=xxx`

### 步骤

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

### 中文名称问题

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
