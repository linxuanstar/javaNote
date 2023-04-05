<img src="..\图片\3-14【Req、Resp】\1.png" />

* `request`和`response`对象是由服务器创建的。我们只是来使用他们。Tomcat创建的。
* `request`对象是来获取请求消息的，而`response`对象是来设置响应消息的

# 第一章 Request对象

request对象继承体系结构如下：

```apl
ServletRequest	-- 接口
	|
HttpServletRequest	-- 接口
	|
org.apache.catalina.connector.RequestFacade 类(Tomcat编写)
```

## 1.1 获取请求消息

请求消息数据格式一共有四种：请求行、请求头、请求空行、请求体。接下来获取一下他们。

### 1.1.1 获取请求行数据

请求行数据格式如下：

```apl
请求方式 请求URL 请求协议/版本
GET /day14/demo01?name=zhangsan HTTP/1.1
```

常用方法如下：

| API                          | 作用                |
| ---------------------------- | ------------------- |
| String getMethod()           | 获取请求方式        |
| String getContextPath()      | 获取虚拟目录        |
| String getServletPath()      | 获取Servlet路径     |
| String getQueryString()      | 获取get方式请求参数 |
| String getRequestURI()       | 获取请求URI         |
| StringBuffer getRequestURL() | 获取请求URL         |
| String getProtocol()         | 获取协议及版本      |
| String getRemoteAddr()       | 获取客户的IP地址    |

```apl
GET								# 获取到的请求方式
/day14_ServletHttpRequest		  # 获取到的虚拟目录
/Demo01Request					 # 获取到的Servlet路径
name=zhangsan					 # 获取到的请求参数
/day14_ServletHttpRequest/Demo01Request						# 获取到的URI
http://localhost/day14_ServletHttpRequest/Demo01Request 	  # 获取到的URL
HTTP/1.1						# 获取到的协议及版本
0:0:0:0:0:0:0:1					 # 获取IP地址 IPv6
```

> URI：统一资源标识符，相当于共和国。URL：统一资源定位符，相当于中华人民共和国。

### 1.1.2 获取请求头数据

常用的请求头：User-Agent、Referer。

* User-Agent：浏览器告诉服务器，访问服务器使用的浏览器款式。
* Referer：告诉服务器，当前请求从何而来。

常用方法：

| API                                  | 作用                           |
| ------------------------------------ | ------------------------------ |
| String getHeader(String name)        | 通过请求头的名称获取请求头的值 |
| Enumeration<String> getHeaderNames() | 获取所有的请求头名称           |

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <!-- href的路径是虚拟路径加上Servlet资源路径，如果设置虚拟路径为/，那么就是/Demo03Request-->
    <a href="/day14_ServletHttpRequest/Demo03Request">链接</a>
</body>
</html>
```

```java
@WebServlet("/Demo03Request")
public class Demo03Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 通过请求头的名称获取请求头的值。
        String header = req.getHeader("user-agent");
        if (header.contains("Chrome")) {
            System.out.println("谷歌浏览器");// 打印 谷歌浏览器
        } else if (header.contains("Firefox")) {
            System.out.println("火狐浏览器");
        }

        // 通过请求头的名称获取请求头的值。
        String refererValue = req.getHeader("referer");
        // 假如是从地址栏进入的，那么就会打印NULL，因为这时候是直接访问的服务器，没有从任何地方来
        // 上面是从超链接来进入，这样就会打印http://localhost/day14_ServletHttpRequest/login.html
        System.out.println(refererValue);

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

### 1.1.3 获取请求体数据

| API                | 作用           |
| ------------------ | -------------- |
| String getReader() | 获取请求体数据 |

创建一个HTML表单，让用户输入数据，提交到`Demo05Request`类里面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <!-- action的路径是虚拟路径加上Servlet资源路径，如果设置虚拟路径为/，那么就是/Demo05Request-->
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
        // 获取请求体信息
        BufferedReader br = req.getReader();
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
```

## 1.2 获取请求参数

不论是get还是post请求方式都可以使用下列方法：

| API                                      | 作用                         |
| ---------------------------------------- | ---------------------------- |
| String getParameter(String name)         | 根据参数名称获取参数值       |
| String[] getParameterValues(String name) | 根据参数名称获取参数值的数组 |
| Enumeration<String> getParameterNames()  | 获取所有请求的参数名称       |
| Map<String, String[]> getParameterMap()  | 获取所有参数的map集合        |

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <!-- action的路径是虚拟路径加上Servlet资源路径 -->
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

## 1.3 解决乱码问题

在`Tomcat8`中，如果使用的是`get`方式请求服务器，那么`Tomcat`就会自动将乱码问题解决。但是如果使用的是`post`请求方式来请求服务器，那么乱码问题依旧存在。

解决方式如下：在获取参数前，设置`request`的编码与前端页面的编码一致即可。他们都是使用的是流对象，所以需要设置流对象的编码`req.setCharacterEncoding("utf-8");`。

## 1.4 请求转发

req请求转发、resp响应重定向

请求转发：一种在服务器内部的资源跳转方式。当浏览器地址栏输入URL之后会访问资源路径，但是该类定义的功能不能够太多，但我们又需要很多功能，所以我们可以让它转发给另一个类。

特点：浏览器地址栏路径是不会发生任何变化的、只能转发到当前服务器内部资源当中，不能够转发互联网当中资源、转发仅仅只是一次请求。

步骤如下：

1. 通过`request`对象获取请求转发器对象：`RequestDispatcher getRequestDispatcher(String path)`
2. 使用`RequestDispatcher`对象来进行转发：`forward(ServletRequest request, ServletResponse response)` 

```java
@WebServlet("/Demo01Request")
public class Demo01Request extends HttpServlet {
    // 当获取请求方式为GET方法时：
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Demo01Request访问了");
        
        // 转发至虚拟路径为/Demo02Request中
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

## 1.5 request域对象

域对象是一个有着作用范围的对象，可以在范围内共享数据。前面提到过ServletContext域对象，它的共享范围特别大，是整个web项目，对所有的客户端共享数据。request域对象共享范围小，一般用于请求转发的多个资源中共享数据。

request域：代表一次请求的范围，一般用于请求转发的多个资源中共享数据。

方法如下：

| API                                      | 作用             |
| ---------------------------------------- | ---------------- |
| void setAttribute(String name, Object o) | 存储数据         |
| Object getAttribute(String name)         | 通过键获取值     |
| void removeAttribute(String name)        | 通过键移出键值对 |

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
```

## 1.6 获取ServletContext

前面提到过，可以通过`ServletContext getServletContext()`方法获取`ServletContext`对象。

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

## 1.7 用户登陆案例

<img src="..\图片\3-14【Req、Resp】\2.png" />

```xml
<dependencies>
    <!-- Servletjar包 -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <!-- 依赖作用范围为provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>

    <!-- 连接MySQL依赖环境 -->
    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>
    <!-- Druid连接池技术，简化开发 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- Spring整合JDBC，里面有JDBCTemplate简化开发-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>

    <!-- 测试依赖环境 -->
    <!-- Junti测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.8.2</version>
        <scope>test</scope>
    </dependency>

    <!-- 其他依赖环境 -->
    <!-- Lombok简化开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
        <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**JDBC连接数据库**

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;

# 如果这个表存在，那么就删除
Drop TABLE IF EXISTS tb_user;
# 如果不存在tb_user表那么就创建
CREATE TABLE IF NOT EXISTS tb_user (
	id INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(32) UNIQUE NOT NULL,
	PASSWORD VARCHAR(20) NOT NULL
);

# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_user;
# 插入信息
INSERT INTO tb_user (username, PASSWORD) VALUES ("lisi", "456");
```

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# 这里面的键最好是这些，如果修改那么无法初始化连接池对象。和Spring里面的键不一样。
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
driver = com.mysql.cj.jdbc.Driver
url = jdbc:mysql://localhost:3306/linxuan?useSSL=false
username = root
password = root
```

```java
package com.linxuan.pojo;

@Data
public class User {
    // 根据《阿里巴巴开发Java手册》，实体类要设置类型为包装类。因为后端有可能返回NULL，这样可以接收。
    private Integer id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }
}
```

```java
package com.linxuan.utils;

// JDBC工具类
public class JDBCUtils {

    private static DataSource ds;

    static {
        try {
            // 创建Properties对象
            Properties pro = new Properties();
            // 使用ClassLoader加载配置文件，获取字节输入流
            ClassLoader classLoader = JDBCUtils.class.getClassLoader();
            InputStream resourceAsStream = classLoader.getResourceAsStream("jdbc.properties");
            // 加载配置文件
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
     * @return DataSource
     */
    public static DataSource getDataSource() {
        return ds;
    }

    /**
     * 获取连接Connection对象
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
```

```java
package com.linxuan.dao;

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
            String sql = "select * from tb_user where username = ? and password = ?";

            // 调用query方法
            User user = null;
            try {
                user = template.queryForObject(sql,
                                               new BeanPropertyRowMapper<User>(User.class),
                                               loginUser.getUsername(),
                                               loginUser.getPassword());
            } catch (DataAccessException e) {
                // 假如返回的user是null，那么就会抛异常，所以这里将异常打印出来。
                // 当然这在业务中是正常的。前端输入信息错误，后端查询为空，所以业务正常，也可以不做任何处理。
                e.printStackTrace();
            }

            return user;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

```java
package com.linxuan.dao;

// 编写测试类，测试dao能否连接数据库
public class UserDaoTest {

    @Test
    public void testLogin() {
        User loginuser = new User("lisi", "456");
        User user = new UserDao().login(loginuser);
        System.out.println(user);
    }
}
```

**前端页面**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <!-- action的路径是虚拟路径加上Servlet资源路径，如果设置虚拟路径为/，那么就是/loginServlet-->
    <form action="/day14_ServletHttpRequest/loginServlet" method="post">
        用户名称：<input type="text" placeholder="请输入用户名" name="username"><br>
        密码：<input type="password" placeholder="请输入密码" name="password"><br>
        <input type="submit" value="提交">
    </form>
</body>
</html>
```

**Servlet类**

```java
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
        req.setCharacterEncoding("utf-8");
        // 获取请求参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // 封装user对象
        User loginUser = new User(username, password);

        // 调用UserDao的login方法
        User user = new UserDao().login(loginUser);

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

```java
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

## 1.8 Beanutils

BeanUtils是Apache Commons组件的成员之一，主要用于简化JavaBean封装数据的操作。 简化反射封装参数的步骤，给对象封装参数。

BeanUtils的应用场景：

1. 快速将一个JavaBean各个属性的值，赋值给具有相同结构的另一个JavaBean中。

2. 快速收集表单中的所有数据到JavaBean中。

```xml
<!-- BeanUtils依赖 -->
<dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>1.9.4</version>
</dependency>
```

例如下面获取请求参数，封装对象，如果有很多的话，会很麻烦。

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

# 第二章 Response对象

`Response`对象功能：设置响应消息。

常用方法如下：

| API                                   | 作用           |
| ------------------------------------- | -------------- |
| setStatus(int sc)                     | 设置状态码     |
| setHeader(String name, String value)  | 设置响应头     |
| sendRedirect(String location)         | 重定向         |
| PrintWriter getWriter()               | 获取字符输出流 |
| ServletOutputStream getOutputStream() | 获取字节输出流 |

响应状态码：

| 状态码 | 类别                             | 原因                       |
| ------ | -------------------------------- | -------------------------- |
| 1xx    | Informational(信息性状态码)      | 接收的请求正在处理         |
| 2xx    | Success（成功状态码）            | 请求正常处理完毕           |
| 3xx    | Redirection（重定向状态码）      | 需要进行附加操作以完成请求 |
| 4xx    | Client Error（客户端错误状态码） | 服务器无法处理请求         |
| 5xx    | Server Error（服务器错误状态码） | 服务器处理请求错误         |

## 2.1 重定向实现

重定向：资源跳转的方式

<img src="..\图片\3-14【Req、Resp】\2-1重定向.png" />

```java
@WebServlet("/demo01Response")
public class Demo01Request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置状态码
        resp.setStatus(302);
        // 设置响应头，设置地址和虚拟路径下面的资源文件。
        resp.setHeader("location", "/day15/demo02Response");

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
```

## 2.2 重定向特点

我们首先来看一下转发的特点：`forward`

- 地址栏路径不变
- 转发只能访问当前服务器下面的资源
- 转发是一次请求

重定向的特点与转发正好相反：`redirect`

- 地址栏发生变化
- 重定向可以访问其他站点(服务器)的资源
- 重定向是两次请求

## 2.3 相对路径和绝对路径

**相对路径**

相对路径：通过相对路径不可以确定唯一资源

规则：找到当前资源和目标资源之间的相对位置关系。

- `./` ：当前目录
- `../`：后退一级目录

**绝对路径**

绝对路径：通过绝对路径可以确定唯一资源。以`/`开头的路径

写绝对路径的规则：判断定义的路径是给谁使用的？判断请求将从哪里过来，哪里发出。

* 给客户端浏览器使用：需要加虚拟目录，就是项目的访问路径

  建议虚拟目录设置为动态获取的值，这样，即使更改了项目的虚拟目录，也不会有任何后果。可以使用：`request.getContextPath()`方法。

  `<a>`， `<form>`重定向。

* 给服务器使用：不需要加上虚拟目录。

  转发路径。

## 2.4 服务器输出字符数据到浏览器

输出字符数据到浏览器的步骤：首先需要获取字符输出流。然后再输出数据就可以了。

```java
@WebServlet("/demo03Response")
public class Demo03Response extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 告诉浏览器，服务器发送的消息体数据的编码。建议浏览器使用该编码解码。
        // resp.setHeader("content-type", "text/html; charset = utf-8");
        // 当然，上述也有简写的方法
        resp.setContentType("text/html; charset=utf-8");

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

## 2.5 服务器输出字节数据到浏览器

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
