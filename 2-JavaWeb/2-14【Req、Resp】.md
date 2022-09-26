

# 第二章 Request对象

![](..\图片\2-14【HTTP、Req、Resp】\1.png)

* `request`和`response`对象是由服务器创建的。我们只是来使用他们。Tomcat创建的。
* `request`对象是来获取请求消息的，而`response`对象是来设置响应消息的

request对象继承体系结构如下：

```apl
ServletRequest	-- 接口
	|
HttpServletRequest	-- 接口
	|
org.apache.catalina.connector.RequestFacade 类(Tomcat编写)
```

## 2.1 获取请求消息

根据之前所学我们可以知道请求消息数据格式一共有四种：**请求行、请求头、请求空行、请求体**。而如果我们需要获取的话，我们不需要获取请求空行。只需要获取请求行、请求头和请求体就可以了。

### 2.1.1 获取请求行数据

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

### 2.1.2 获取请求头数据

- `String getHeader(String name)`：通过请求头的名称获取请求头的值。
- `Enumeration<String> getHeaderNames()`：获取所有的请求头名称。

`String getHeader(String name)`：通过请求头的名称获取请求头的值。

请求头有两个：`User-Agent`、`Referer`

* `User-Agent`：

  ```java
  @WebServlet("/Demo03Request")
  public class Demo03Request extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          // String getHeader(String name)：通过请求头的名称获取请求头的值。
          // User-Agent：浏览器告诉服务器，访问服务器使用的浏览器款式。
          // Referer：http://localhost/login.html 告诉服务器，当前请求从何而来。
  
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

* `Referer`：

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

`Enumeration<String> getHeaderNames()`：获取所有的请求头名称。

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


### 2.1.3 获取请求体数据

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

## 2.2 request功能

接下来看一下Request的其他功能

### 2.2.1 获取请求参数

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

### 2.2.2 解决乱码问题

在`Tomcat8`中，如果使用的是`get`方式请求服务器，那么`Tomcat`就会自动将乱码问题解决。但是如果使用的是`post`请求方式来请求服务器，那么乱码问题依旧存在。

解决方式如下：

* 在获取参数前，设置`request`的编码与前端页面的编码一致即可。
* 这是因为他们都是使用的是流对象，所以需要设置流对象的编码。`req.setCharacterEncoding("utf-8");`

### 2.2.3 请求转发

req请求转发、resp响应重定向

请求转发：一种在服务器内部的资源跳转方式。当浏览器地址栏输入URL之后会访问资源路径，但是该类定义的功能不能够太多，但我们又需要很多功能，所以我们可以让它转发给另一个类。

步骤如下：

1. 通过`request`对象获取请求转发器对象：`RequestDispatcher getRequestDispatcher(String path)`
2. 使用`RequestDispatcher`对象来进行转发：`forward(ServletRequest request, ServletResponse response)` 

特点：

- 浏览器地址栏路径是不会发生任何变化的。
- 只能转发到当前服务器内部资源当中，不能够转发互联网当中资源。
- 转发仅仅只是**一次请求**，不是两次，也不是三次。

```java
@WebServlet("/Demo01Request")
public class Demo01Request extends HttpServlet {
    // 当获取请求方式为GET方法时：
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Demo01Request访问了");

        // 不建议这么使用，只创建一次对象只转发一次。所以没必要创建一个对象 直接匿名就可以了，使用链式编程。
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

### 2.2.4 共享数据域对象

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

### 2.2.5 获取ServletContext

`ServletContext getServletContext()`：获取`ServletContext`对象。

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

案例：用户登陆。需求如下：

1. 编写`login.html`登陆页面		`username & password`两个输入框
2. 使用`Druid`数据库连接池技术，操作`mysql`，`day14`数据库中`user`表
3. 使用`JDBCTemplate`技术封装`JDBC`
4. 登陆成功跳转到`SuccessServlet`    展示：登陆成功！用户名，欢迎您
5. 登陆失败跳转到`FailServlet`     展示：登陆失败，用户名或者密码错误

**分析**

![](..\图片\2-14【HTTP、Req、Resp】\2.png)

**实现**

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
   
   	// 省略get和set方法 toString方法
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
                       loginUser.getUsername(), 
                       loginUser.getPassword());
               
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

`Response`对象功能：设置响应消息。

常用方法如下：

* `setStatus(int sc)` ：设置状态码
* `setHeader(String name, String value)`：设置响应头
* `sendRedirect(String location)` ：简单的重定向，重定向很常见。一共有三个参数，而我们只需要修改一个参数，所以提供了这个方法。这是一种非常简单的重定向方法。
* `PrintWriter getWriter()`：获取字符输出流
* `ServletOutputStream getOutputStream()`：获取字节输出流。

相应重定向：

| 状态码 | 类别                             | 原因                       |
| ------ | -------------------------------- | -------------------------- |
| 1xx    | Informational(信息性状态码)      | 接收的请求正在处理         |
| 2xx    | Success（成功状态码）            | 请求正常处理完毕           |
| 3xx    | Redirection（重定向状态码）      | 需要进行附加操作以完成请求 |
| 4xx    | Client Error（客户端错误状态码） | 服务器无法处理请求         |
| 5xx    | Server Error（服务器错误状态码） | 服务器处理请求错误         |

## 3.1 重定向实现

重定向：资源跳转的方式

![](..\图片\2-14【HTTP、Req、Resp】\1重定向.png)

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

我们首先来看一下转发的特点：`forward`

- 地址栏路径不变
- 转发只能访问当前服务器下面的资源
- 转发是**一次**请求

重定向的特点与转发正好相反：`redirect`

- 地址栏发生变化
- 重定向可以访问其他站点(服务器)的资源
- 重定向是**两次**请求

## 3.4 相对路径和绝对路径

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

## 3.5 服务器输出字符数据到浏览器

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

* 告诉浏览器，服务器发送的消息体数据的编码，建议浏览器使用该编码解码。但是，必须记住，**应该在获取字符输出流之前就要设置编码**。


```java
// 告诉浏览器，服务器发送的消息体数据的编码。建议浏览器使用该编码解码。
// resp.setHeader("content-type", "text/html; charset = utf-8");
// 当然，上述也有简写的方法
resp.setContentType("text/html; charset = utf-8");
```

> 切记，UTF-8中间不能有空格

## 3.6 服务器输出字节数据到浏览器

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

