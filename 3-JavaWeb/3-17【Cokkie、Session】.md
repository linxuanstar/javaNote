会话技术概述

- 会话：一次会话中包含多次请求和响应。

- 一次会话：浏览器第一次给服务器资源发送请求，会话建立，直到有一方断开为止。

- 会话的功能：在一次会话的范围内的多次请求间，共享数据。

- 会话的方式有两种：第一种是客户端会话技术`Cookie`，第二种是服务端会话技术`Session`。

# 第一章 Cookie

Cookie存储的数据在客户端浏览器中、浏览器对于单个的Cookie是有大小限制的。Cookie一般用于存储一些少量的不太敏感的数据、在不登录的情况下完成服务器对客户端的身份识别。

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

## 1.1 Cookie原理

实现原理：基于响应头`set-Cookie`和请求头`Cookie`实现的。

![](..\图片\3-17【Cokkie、Session】\1Cookie会话技术.png)

## 1.2 Cookie细节

**一次可以发送多个Cookie吗**

我们可以创建多个Cookie对象，使用response调用多次addCookie方法发送Cookie即可。

**Cookie在浏览器中保存多长时间**

默认来说，当浏览器关闭之后，Cookie数据就会被销毁。我们可以利用一个方法`setMaxAge(int seconds)`进行持久化存储，方法参数是秒数。

* 如果秒数是正数，那么就是`Cookie`的存活时间。在进行`Cookie`会话的时候会将Cookie的数据写入到硬盘的文件中，进行持久化的存储。如果到时间了，那么就会进行销毁。
* 如果秒数是负数，那么就是浏览器关闭就会销毁。
* 如果是零，那么就会删除`Cookie`的信息。假如我们先设置了秒数是300，那么会在内存中存储，但是再将参数改为0，那么就会将内存中的数据销毁。

**Cookie能不能存储中文**

- 在`Tomcat8`之前，`Cookie`是不能直接存储中文数据的。我们只能将中文数据转码，一般是转为URL编码。

- 在`Tomcat8`之后，支持中文数据。

**Cookie共享范围多大？**

假如在同一个`Tomcat`服务器中部署了多个Web项目，那么这些Web项目中的Cookie是不能共享的。有这么一个方法`setPath(String path):`设置Cookie的获取返回。在默认情况下，参数会设置为当前的虚拟目录。所以Cookie信息无法共享。但是我们可以修改参数，将path修改为`“/”`，这样不同的Web项目就可以访问了。

不同的Tomcat服务器之间如何共享Cookie呢？Cookie里面有这么一个方法：`setDomain(String path)`：如果设置的一级域名相同，那么多个服务器之间的Cookie也可以共享。例如我们将path修改为“.baidu.com”，那么tieba.baidu.com和newx.baidu.com中的Cookie是可以共享的。

## 1.3 Cookie案例

记住上一次访问时间

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

        // cookies不为NULL，遍历Cookie数组，看看里面是否有访问时间
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
                    SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日 HH:mm:ss");
                    String str_date = sdf.format(date);
                    
                    // 由于里面有特殊字符，所以需要编码和解码
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

# 第二章 Session

`Session`：服务器端会话技术，在一次会话的多次请求间共享数据，将数据保存在服务器端的对象中。Session用于存储一次会话的多次请求的数据，存在于服务器端。Session可以存储任意类型，任意大小的数据。

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

## 2.1 Session原理

`Session`的原理是基于`Cookie`的。

![](..\图片\3-17【Cokkie、Session】\3Session.png)

## 2.2 Session细节

**当客户端关闭之后，服务器不关闭，两次获取的Session是否是同一个呢？**

不是。如果需要相同，我们可以创建Cookie，键为JSESSIONID，设置最大存活时间，让Cookie持久化保存。

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

不是，如果是，也只是一个巧合。创建Session对象之后，关闭服务器，会将Session对象销毁掉。服务器关闭，内存对象被释放。

可以将数据持久化存储：

* Session的钝化：服务器正常关闭之前，将Session对象系列化到硬盘上面。序列化的过程。
* Session的活化：服务器启动之后，将Session文件转换为内存中的Session对象即可。反序列化的过程。

**Session的失效时间，也就是什么时候被销毁？**

服务器关闭，也就会被销毁。Session的对象调用，会调用一个方法`invalidate()`，进行销毁。

Session的默认失效时间为30分钟。在conf目录下面的的web.xml文件里面配置了失效时间，我们可以修改。

```xml
<!-- ==================== Default Session Configuration ================= -->
<!-- You can set the default session timeout (in minutes) for all newly   -->
<!-- created sessions by modifying the value below.                       -->

<session-config>
    <session-timeout>30</session-timeout>
</session-config>
```

# 第三章 Session与Cookie

1. Session：主菜。Cookie：小饼干。

2. Session存储数据在服务器端，Cookie在客户端。

3. Session没有数据大小限制，Cookie有。

4. Session数据安全，Cookie相对不安全。
