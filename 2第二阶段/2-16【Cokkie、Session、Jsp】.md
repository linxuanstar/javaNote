<!--P737-->

# 第一章 会话技术

## 会话技术概述

<!--P738-->

- 会话：一次会话中包含多次请求和响应。

- 一次会话：浏览器第一次给服务器资源发送请求，会话建立，直到有一方断开为止。

- 会话的功能：在一次会话的范围内的多次请求间，共享数据。

- 会话的方式有两种：第一种是客户端会话技术`Cookie`，第二种是服务端会话技术`Session`。


# 第二章 Cookie

## 2.1 概述

<!--P739 3.13-->

```java
@WebServlet("/demo01Cookie")
public class Demo01Cookie extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 创建Cookie对象
        Cookie c = new Cookie("msg", "hello");
        // 发送Cookie
        resp.addCookie(c);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

```java
@WebServlet("/demo02Cookie")
public class Demo02Cookie extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取Cookie
        Cookie[] cs = req.getCookies();
        // 获取数据，遍历Cookies
        if(cs != null) {
            for (Cookie c : cs) {
                String name = c.getName();
                String value = c.getValue();
                //msg:hello
                //Idea-a29be23a:709a7d50-7eeb-4abf-bcd4-105f274eb782
                System.out.println(name + ":" + value);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

## 2.2 Cookie原理

<!--P740-->

实现原理：基于响应头`set-Cookie`和请求头`Cookie`实现的。

![](..\图片\2-16【Cokkie、Session】\1Cookie会话技术.png)

## 2.3 Cookie细节

<!--P741-->

<!--P742-->

<!--P743-->

### 一次可以发送多个Cookie吗

- 可以，我们可以创建多个Cookie对象，使用response调用多次addCookie方法发送Cookie即可。


### Cookie在浏览器中保存多长时间

默认来说，当浏览器关闭之后，Cookie数据就会被销毁。我们可以尝试一下，上面有代码，打开服务器，地址栏键入*`/demo01Cookie`*，然后再键入*`/demo02Cookie`*，这时控制台会输出信息。但是当我们将浏览器关闭之后，`Cookie`会销毁，这时我们在键入*`/demo02Cookie`*，控制台会没有任何的回应。

如何进行持久化存储呢？我们可以利用一个方法`setMaxAge(int seconds)`，方法参数是秒数。

* 如果秒数是正数，那么就是`Cookie`的存活时间。在进行`Cookie`会话的时候会将Cookie的数据写入到硬盘的文件中，进行持久化的存储。如果到时间了，那么就会进行销毁。
* 如果秒数是负数，那么就是浏览器关闭就会销毁。
* 如果是零，那么就会删除`Cookie`的信息。假如我们先设置了秒数是300，那么会在内存中存储，但是再将参数改为0，那么就会将内存中的数据销毁。

### Cookie能不能存储中文

- 在`Tomcat8`之前，`Cookie`是不能直接存储中文数据的。我们只能将中文数据转码，一般是转为URL编码。

- 在`Tomcat8`之后，支持中文数据。


<!--P744 3.14-->

### Cookie共享范围多大？

假如在同一个`Tomcat`服务器中部署了多个Web项目，那么这些Web项目中的Cookie是不能共享的。有这么一个方法`setPath(String path):`设置Cookie的获取返回。在默认情况下，参数会设置为当前的虚拟目录。所以Cookie信息无法共享。但是我们可以修改参数，将path修改为`“/”`，这样不同的Web项目就可以访问了。

不同的Tomcat服务器之间如何共享Cookie呢？Cookie里面有这么一个方法：`setDomain(String path)`：如果设置的一级域名相同，那么多个服务器之间的Cookie也可以共享。例如我们将path修改为“.baidu.com”，那么tieba.baidu.com和newx.baidu.com中的Cookie是可以共享的。

## 2.4 Cookie特点和作用

<!--P745-->

特点：

* Cookie存储的数据在客户端浏览器中
* 浏览器对于单个的Cookie是有大小限制的，以及对同一个域名下的总Cookie的数量也是有限制的。

作用：

* Cookie一般用于存储一些少量的不太敏感的数据
* 在不登录的情况下面，完成服务器对客户端的身份识别。

## 2.5 Cookie案例

<!--P746-->

案例：记住上一次访问时间

### 需求

1. 访问一个`Servlet`，如果是第一次访问，则提示：您好，欢迎您首次访问。
2. 如果不是第一次访问，则提示：欢迎回来，您上次访问时间为：显示时间字符串。

### 分析

1. 我们可以使用`Cookie`来完成。

2. 在服务器中的`Servle`t判断是否有一个名为`lastTime`的`Cookie`

   如果有：那么代表不是第一次访问。我们需要

   1. 相应数据：欢迎回来，您上次访问时间为：
   2. 写回`Cookie`：`lastTime = 时间`

   如果没有：那么代表是第一次访问。我们需要

   1. 相应数据：您好，欢迎您首次访问
   2. 写回`Cookie`：`lastTime = 时间`

### 代码

<!--P747-->

```java
@WebServlet("/demo01CookieTest")
public class Demo01CookieTest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的消息体的数据以及编码
        resp.setContentType("text/html; charset=utf-8");

        // 获取所有的Cookie
        Cookie[] cookies = req.getCookies();
        // 设置一个flag，判断是否是第一次访问
        boolean flag = false;

        // 遍历Cookie数组
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                // 获取cookie名称
                String name = cookie.getName();
                // 判断是否是lastTime，我们会将时间信息存储到lastTime这个Cookie里面
                if ("lastTime".equals(name)) {
                    // 有这个Cookie代表我们之前已经来过，不是第一次访问
                    flag = true;

                    // 响应数据
                    String value = cookie.getValue();
                    // 解码
                    value = URLDecoder.decode(value, "utf-8");
                    resp.getWriter().write("<h1>欢迎回来，您上次访问时间为：" + value + "</h1>");

                    // 设置Cookie的Value
                    // 获取当前时间的字符串，重新设置Cookie的值，重新发送Cookie
                    // 获取当前时间
                    Date date = new Date();
                    // 设置时间格式
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy年MM月dd日 HH:mm:ss");
                    String str_date = simpleDateFormat.format(date);
                    // 由于里面有特殊字符，所以需要编码和解码
                    // 编码
                    str_date = URLEncoder.encode(str_date, "utf-8");
                    // 修改之前存储lastTime存储的时间
                    cookie.setValue(str_date);
                    cookie.setMaxAge(60 * 60 * 24 * 30);
                    resp.addCookie(cookie);
                    
                    break;
                }
            }
        }

        // 第一次访问
        if (cookies == null || cookies.length == 0 || flag == false) {
            // 设置Cookie的value
            Date date = new Date();
            // 设置时间格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy年MM月dd日 HH:mm:ss");
            String str_date = simpleDateFormat.format(date);
            // 由于里面有特殊字符，所以需要编码和解码
            // 编码
            str_date = URLEncoder.encode(str_date, "utf-8");
            // 设置lastTime这个Cookie
            Cookie cookie = new Cookie("lastTime", str_date);
            cookie.setMaxAge(60 * 60 * 24 * 30);
            resp.addCookie(cookie);

            resp.getWriter().write("<h1>您好，欢迎首次访问</h1>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

```

# 第三章 JSP

<!--P748 3.15-->

## 3.1 JSP概述

JSP：`Java Server Pages`：是一个Java服务器端页面。

可以理解为：一个特殊的页面，在这里页面里面我们可以定义HTML标签，也可以定义Java代码。jsp是一个后缀，我们创建一个Web项目，里面就带着一个jsp页面。

JSP有一个特别大的好处，就是可以简化书写。我们可以在里面定义HTML标签，也可以定义Java代码。所以很方便。

## 3.2 JSP原理

<!--P749-->

![](..\图片\2-16【Cokkie、Session】\2JSP.png)

<font color = "red">**本质上，JSP就是一个Servlet。**</font>

当我们在`index.jsp`书写代码，服务器启动，浏览器访问。那么会将`index.jsp`转为`index.java`，再转为`index.class`。

我们可以查看一下：当服务器运行，会在`C:\Users\林轩\AppData\Local\JetBrains\IntelliJIdea2021.2\tomcat\3e20fe4c-cdaf-436f-8e70-1d6bfe2452e7`产生work工作空间。在里面可以找到`index.java`和`index.class`。我们打开可以发现继承了`org.apache.jasper.runtime.HttpJspBase`。

打开Tomcat源码可以发现`HttpJspBase`继承了`HttpServlet`。所以说`jsp`本质上就是一个`Servlet`。

<!--P750-->

## 3.3 JSP脚本

所谓JSP脚本就是JSP定义Java代码的方式

1. `<% 代码 %>`：定义的`Java`代码，在`Service`方法中。`Service`方法中可以定义什么，那么在这个脚本中就可以定义什么。
2. `<%! 代码 %>`：定义的`Java`代码，在`JSP`转换后的`Java`类的成员位置。
3. `<%= 代码 %>`：定义的`Java`代码，会输出到页面上面。输出语句中可以定义什么，那么脚本中就可以定义什么。

## 2.4 JSP的内置对象

<!--P751-->

<!--P752-->

在JSP中我们不需要获取和创建对象，可以直接使用对象。JSP一共有着9个内置对象。今天只学习3个：

* `resquest`

* `response`

* `out`：字符流输出对象。可以将数据输出到页面上面。和`response.getWriter()`类似。

  二者不同之处：`response.getWriter()`方法不论在哪里，数据输出永远在`out.write()`之前。

  这是因为：在`Tomcat`服务器真正给客户端做出响应之前，会先找到`response`缓冲区数据。

# 第四章 Session

## 4.1 基本概述

<!--P753-->

`Session`：服务器端会话技术，在一次会话的多次请求间共享数据，将数据保存在服务器端的对象中。

使用`Session`：

1. 获取`HttpSession`对象。

   我们可以使用`req.getSession();`

2. 使用`HttpSession`对象：

   `Object getAttribute(String name)`：根据名称获取值

   `void setAttribute(String name, Object value)` ：设置Session名称和值。

   `void removeAttribute(String name)` ：移除。

设置`Session`：

```java
@WebServlet("/demo01Session")
public class Demo01Session extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取Session对象
        HttpSession session = req.getSession();
        // 调用Session对象方法，设置Session
        session.setAttribute("msg", "Hello world");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

获取Session：

```java
@WebServlet("/demo02Session")
public class Demo02Session extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取Session对象
        HttpSession session = req.getSession();
        // 调用Session对象，获取Session
        Object msg = session.getAttribute("msg");
        System.out.println(msg);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

## 4.2 原理

`Session`的原理是基于`Cookie`的。

<!--P754 3.16-->

![](..\图片\2-16【Cokkie、Session】\3Session.png)

## 4.3 细节分析

<!--P755-->

**当客户端关闭之后，服务器不关闭，两次获取的Session是否是同一个呢？**

* 不是。

  ```java
  // 获取Session对象
  HttpSession session = req.getSession();
  // 打印Session对象的哈希值
  System.out.println(session);
  
  // 打印信息如下
  // org.apache.catalina.session.StandardSessionFacade@64ae3650
  // org.apache.catalina.session.StandardSessionFacade@72614434
  ```

* 如果需要相同，我们可以创建Cookie，键为JSESSIONID，设置最大存活时间，让Cookie持久化保存。

  ```java
  // 获取Session对象
  HttpSession session = req.getSession();
  // 打印Session对象的哈希值
  System.out.println(session);
  
  // 设置Cookie
  Cookie cookie = new Cookie("JSESSIONID", session.getId());
  // 设置Cookie存活时间
  cookie.setMaxAge(60 * 60);
  // 添加Cookie到响应头
  resp.addCookie(cookie);
  
  // 打印信息如下：
  // org.apache.catalina.session.StandardSessionFacade@29c041a8
  // org.apache.catalina.session.StandardSessionFacade@29c041a8
  ```

**客户端不关闭，服务器关闭后，两次获取的Session是同一个吗？**

<!--P756-->

不是，如果是，也只是一个巧合。创建Session对象之后，关闭服务器，会将Session对象销毁掉。服务器关闭，内存对象被释放。

这样好吗？我们试想一下，网页端购买商品，加入购物车了。购物车是由Session和Map集合构成的，但是突然服务器重启了，这会导致什么？导致数据丢失，购买错误。所以这种现象不好。

对此我们有两种方法：

* Session的钝化：服务器正常关闭之前，将Session对象系列化到硬盘上面。序列化的过程。
* Session的活化：服务器启动之后，将Session文件转换为内存中的Session对象即可。反序列化的过程。

Tomcat已经帮我们自动做了，所以我们已经不用操心了。IDEA完成不了，我们只能使用本地的Tomcat来帮我们完成。

* 对于Tomcat而言，我们找到IDEA在本地部署的项目：`D:\Java\IdeaProjects\java_SEStrong\out\artifacts\day16_Cookie_Session_war_exploded`，然后将其打包弄到Tomcat的Webapps文件夹下面：`E:\Tomcat\apache-tomcat-8.5.75\webapps`，这样就在Tomcat里面部署了一个项目。

  接下来，我们来看看是否将其序列化和反序列化了：

  我们双击start运行服务器，然后在浏览器地址栏输入`http://localhost:8080/day16/demo01Session`，获取Session对象，设置Session。然后在浏览器地址栏输入`http://localhost:8080/day16/demo02Session`，这样就会在控制台输出Session信息。

  接着我们可以将Tomcat服务器正常关闭，双击shutdown文件，然后打开`E:\Tomcat\apache-tomcat-8.5.75\work`,work存储的是运行时产生的文件。找到`E:\Tomcat\apache-tomcat-8.5.75\work\Catalina\localhost\day16`，这时我们看到了这么一个文件：`SESSIONS.ser`。这就是Session的钝化了。服务器正常关闭之前，将Session对象系列化到硬盘上面。序列化的过程。

  当我们重新启动服务器，然后就会自动读取该文件，将该文件销毁，这时我们无需再设置Session了，直接访问`http://localhost:8080/day16/demo02Session`，这样就会在控制台输出Session信息。这就是活化。服务器启动之后，将Session文件转换为内存中的Session对象即可。反序列化的过程。

* 对于IDEA而言，有钝化，也会产生相应的文件，但是一旦启动会销毁work文件夹，然后重新新建一个work文件夹，所以无法找到`SESSIONS.ser`，无法进行活化。

**Session的失效时间，也就是什么时候被销毁？**

<!--P757-->

1. 服务器关闭，也就会被销毁

2. Session的对象调用，会调用一个方法invalidate()，进行销毁。

3. Session的默认失效时间，30分钟。在`E:\Tomcat\apache-tomcat-8.5.75\conf`里面的web.xml文件里面配置了失效时间，我们可以修改。

   ```xml
     <!-- ==================== Default Session Configuration ================= -->
     <!-- You can set the default session timeout (in minutes) for all newly   -->
     <!-- created sessions by modifying the value below.                       -->
   
       <session-config>
           <session-timeout>30</session-timeout>
       </session-config>
   ```

## 4.4 Session特点

<!--P758-->

1. Session用于存储一次会话的多次请求的数据，存在于服务器端。
2. Session可以存储任意类型，任意大小的数据。

## 4.5 Session与Cookie的区别

1. Session：主菜。Cookie：小饼干。

2. Session存储数据在服务器端，Cookie在客户端。

3. Session没有数据大小限制，Cookie有。

   ```java
   void setAttribute(String name, Object value) ：设置Session名称和值。
   ```

   参数是Object类型的，任意类型都可以。

4. Session数据安全，Cookie相对不安全。

## 4.6 验证码案例

### 需求

<!--P759-->

1. 访问带有验证码的登陆页面login.jsp
2. 用户输入用户名称，密码以及验证码
   * 如果用户输入用户名和密码有错误，跳转登录页面，提示：用户名称或者密码错误。
   * 吐过验证码输入有错误，跳转登录压面，提示：验证码错误。
   * 如果全部输入正确，则跳转到主页success.jsp，显示：用户名，欢迎您。

### 分析

![](..\图片\2-16【Cokkie、Session】\4验证码案例.png)

### 代码

<!--P760 3.18 昨天没有学习-->

<!--P761-->

```jsp
<!--login.jsp-->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
        window.onload = function () {
            document.getElementById("img").onclick = function () {
                this.src = "/day16/checkCodeServlet?time=" + new Date().getTime();
            }
        }
    </script>

    <style>
        div {
            color: red;
        }
    </style>
</head>
<body>
    <form action="/day16/loginServlet">
        <table>
            <tr>
                <td>用户名</td>
                <td><input type="text" name="username"></td>
            </tr>
            <tr>
                <td>密码</td>
                <td><input type="password" name="password"></td>
            </tr>
            <tr>
                <td>验证码</td>
                <td><input type="text" name="checkCode"></td>
            </tr>
            <tr>
                <td><img id="img" src="/day16/checkCodeServlet"></td>
            </tr>
            <tr>
                <td><input type="submit" value="提交"></td>
            </tr>
        </table>
    </form>

    <div><%=request.getAttribute("cc_error")%></div>
    <div><%=request.getAttribute("login_error")%></div>
</body>
</html>

```

```java
// 验证码图片
@WebServlet("/checkCodeServlet")
public class CheckCodeServlet extends HttpServlet {
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
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            int index = ran.nextInt(str.length());
            // 获取字符
            char ch = str.charAt(index);
            sb.append(ch);
            // 写入验证码
            graphics.drawString(ch + "", width / 5 * i, height / 2);
        }
        String checkCode_session = sb.toString();
        // 验证码存入Session
        req.getSession().setAttribute("checkCode_session", checkCode_session);

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

```java
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置request编码
        req.setCharacterEncoding("utf-8");
        // 获取参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String checkCode = req.getParameter("checkCode");
        // 获取生成的验证码
        HttpSession session = req.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        // 删除session中存储的验证码
        session.removeAttribute("checkCode_session");
        // 判断验证码是否正确
        if (checkCode_session != null && checkCode_session.equalsIgnoreCase(checkCode)) {
            // 忽略大小写比较
            // 判断用户名称
            // ====这里应该采用数据库====
            if ("zhangsan".equals(username) && "123".equals(password)) {
                // 登陆成功
                // 存储信息，用户信息
                session.setAttribute("user", username);
                // 重定向到success.jsp
                resp.sendRedirect(req.getContextPath() + "/success.jsp");

            } else {
                // 登陆失败
                // 存储提示信息到request
                req.setAttribute("login_error", "用户名或者密码错误");
                // 转发到登陆页面
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } else {
            // 验证码不一致
            // 存储提示信息到request
            req.setAttribute("cc_error", "验证码错误");
            // 转发到登陆页面
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

```

```java
<!--success.jsp-->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1><%=request.getSession().getAttribute("user")%>, 欢迎您</h1>
</body>
</html>
```

