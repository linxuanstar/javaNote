<!--P762-->

# 第一章 JSP进阶

<!--P763-->

## 1.1 指令

JSP指令：用于配置JSP页面，导入资源文件。

格式：`<%@ 指令名称 属性名1=属性值1 属性名2=属性值2...%>`

​			`<%@ page contentType="text/html;charset=UTF-8" language="java" %>`

​			上面的Page就是一个指令名称。除了page还有几种指令名称。

指令名称分类

* page：配置JSP页面
* include：页面包含的。导入页面的资源文件。
* taglib：导入资源

### page指令名称

<!--P764 3.19-->

page指令名称作用： 配置JSP页面的

它的属性名称分类如下：

* **contentType**：等同于response.setContentType()。设置响应体的mime类型以及字符集；设置当前jsp页面的编码（只能是高级的IDE才能生效，如果使用低级工具，则需要设置pageEncoding属性设置当前页面的字符集）

  例如：`contentType="text/html;charset=UTF-8"`

* **pageEncoding**：设置当前页面的字符集

  例如：`pageEncoding="GBK"`

* **language**：设置语言

  例如：`language="java"`

* **import**：导包的，java代码如果需要导包，那么就会在这个里面进行导包

  例如：`import="java.util.List"`

* **buffer**：缓冲区的大小，默认是8kb

  例如：`buffer="16kb"`

* **errorPage**：当前页面发生异常后，会自动跳转到指定的错误页面

  例如：`errorPage="500.jsp"`在当前页面造一个数学异常，然后就会报错，浏览器显示错误。但是显示的错误不好看，泄露源码了。那么我们可以让它自动跳转到指定的错误页面。

* **isErrorPage**：标识当前也是是否是错误页面。
	
	* true：是，可以使用内置对象exception
	* false：否。默认值。不可以使用内置对象exception

### include指令名称

<!--P765-->

include	： 页面包含的。导入其他页面的资源文件。因为有很多代码都会重复，所以我们可以定义一个jsp页面，然后统一导入即可。

```jsp
<%@include file="top.jsp"%>
```

### taglib指令名称

taglib	： 导入资源，导入标签库。

我们在IDEA里面创建WEB-INF文件夹，在WEB-INF下面创建lib文件夹。导入一些JSTL标签jar包。`javax.servlet.jsp.jstl.jar` `jstl-impl.jar`

然后键入下面的代码，就导入jstl的标签库了。

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

* prefix：前缀，自定义的，我们想定义成什么就定义什么。当然，大家都有一些约定俗称的规则。JSTL就是c。

## 1.2 注释

<!--P766-->

1. html注释：
	`<!-- -->`:只能注释html代码片段
	
2. jsp注释：推荐使用
	`<%-- --%>`：可以注释所有的代码片段
	
3. Java注释：在JSP的`<%%>`区域内部可以使用注释

     `//` `/**/`

## 1.3 内置对象

<!--P767-->

内置对象：**在jsp页面中不需要创建，直接使用的对象**。

|   变量名    |      真实类型       |                     作用                     |
| :---------: | :-----------------: | :------------------------------------------: |
| pageContext |     PageContext     | 当前页面共享数据，还可以获取其他八个内置对象 |
|   request   | HttpServletRequest  |         一次请求访问的多个资源(转发)         |
|   session   |     HttpSession     |             一次会话的多个请求间             |
| application |   ServletContext    |              所有用户间共享数据              |
|  response   | HttpServletResponse |                   响应对象                   |
|    page     |       Object        |        当前页面(Servlet)的对象  this         |
|     out     |      JspWriter      |          输出对象，数据输出到页面上          |
|   config    |    ServletConfig    |              Servlet的配置对象               |
|  exception  |      Throwable      |                   异常对象                   |

### pageContext

pageContext：当前页面共享数据，还可以获取其他八个内置对象，里面配置了一些方法来获取其他八个对象。

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
    <%
      pageContext.setAttribute("msg", "林炫你好");
    %>

    <%=pageContext.getAttribute("msg")%>
  </body>
</html>

<!-- 会在页面输出林炫你好 -->
```

# 第二章 MVC开发模式

## 2.1 JSP演变历史

<!--P768-->

1. 早期只有servlet，只能使用response输出标签数据，非常麻烦。
2. 后来有了jsp，简化了Servlet的开发，如果过度使用jsp，在jsp中即写大量的java代码，又写html标签，造成难于维护，难于分工协作。
3. 再后来，java的web开发，借鉴mvc开发模式，使得程序的设计更加合理性。

## 2.2 MVC详解

<!--P769-->

![](D:\Java\笔记\图片\3-day17【EL、JSTL】\1MVC开发模式.bmp)

**M**：Model，模型。JavaBean

* 完成具体的业务操作，如：查询数据库，封装对象

**V**：View，视图。JSP

* 展示数据

**C**：Controller，控制器。Servlet

* 获取用户的输入
* 调用模型
* 将数据交给视图进行展示

## 2.3 MVC优缺点

* 优点：
  1. 耦合性低，方便维护，可以利于分工协作
  1. 重用性高

* 缺点：
  1. 使得项目架构变得复杂，对开发人员要求高

# 第三章 EL表达式

## 3.1 基本概述

<!--P770-->

1. 概念：Expression Language 表达式语言

2. 作用：替换和简化jsp页面中java代码的编写

3. 语法：`${表达式}`，**不加分号**

   > 注意：
   >
   > jsp默认支持el表达式的。如果要忽略el表达式那么有两种方法：
   >
   > 1. 设置jsp中page指令中：`isELIgnored="true"` 忽略当前jsp页面中所有的el表达式
   > 2. `\${表达式}` ：忽略当前这个el表达式，反斜线相当于转义字符。

## 3.2 运算符

<!--P771-->

1. 算数运算符： `+ - * /(div) %(mod)`
2. 比较运算符： `> < >= <= == !=`
3. 逻辑运算符： `&&(and) ||(or) !(not)`
4. 空运算符： `empty`
    * 功能：用于判断字符串、集合、数组对象是否为null或者长度是否为0
    * `${empty list}`:判断字符串、集合、数组对象是否为null或者长度为0
    * `${not empty str}`:表示判断字符串、集合、数组对象是否不为null 并且 长度>0

## 3.3 获取值

<!--P772-->

注意：**el表达式只能从域对象中获取值**

获取值的语法一共有两种，如下：
1. `${域名称.键名}`：从指定域中获取指定键的值
   
    * 域名称：
        1. pageScope          --> pageContext
        2. requestScope     --> request
        3. sessionScope      --> session
        4. applicationScope --> application（ServletContext）
    * 举例：在request域中存储了name=张三
    * 获取：`${requestScope.name}`
    
    创建一个el01.jsp，在里面键入如下代码：
    
    ```jsp
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <%
        	// 在域中存储数据
            request.setAttribute("linxuan", "nihao");
            session.setAttribute("session", "attribute");
        %>
    
    <!--页面打印值，注意第三个不会打印NULL，只会打印空字符串-->
    ${requestScope.linxuan}<br>
    ${sessionScope.session}<br>
    ${sessionScope.linxuan}
    </body>
    </html>
    
    <!--
    	打印结果：
    		nihao
    		attribute
    -->
    ```
    
    > 注意：
    >
    > ​	如果获取的键没有，那么不会打印NULL，只会打印空字符串。
    
2. `${键名}`：表示依次从最小的域中查找是否有该键对应的值，直到找到为止。

    * 从从最小的域中开始查找，如果找到了，那么会停止，之后的域便不会查找。
    * 与域对象放置的位置无关。

    ```jsp
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <%
            // 在域中存储数据
            session.setAttribute("name", "张三2");
            request.setAttribute("name", "张三1");
            session.setAttribute("lisi", "李四");
        %>
    <!--打印数据-->
    ${name}
    </body>
    </html>
    
    <!--
    	打印结果：
    		张三1
    -->
    ```

## 3.4 获取对象的值

<!--P773-->

获取对象的值的方法：`${域名称.键名.属性名}`

* 如果只是`${域名称.键名}`，会打印哈希值

* 属性名是getter和setter方法去掉get或者set，然后将剩余的部分首字母变成小写。
* 调用setName方法，去掉set，将N变成小写。`setName --> Name --> name`

* **本质上会去调用对象的getter和setter方法**

创建一个User类，存储姓名，年龄，日期。

```java
import java.util.Date;

public class User {
    private String name;
    private int age;
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
```

创建jsp文件，获取User对象，存储数据。再获取值

```jsp
<%@ page import="cn.com.demo01.User" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        // 获取User对象，存储数据
        User user = new User();
        user.setName("linxuan");
        user.setAge(19);
        user.setDate(new Date());

        request.setAttribute("user", user);
    %>

    <!--获取对象中的值-->
    ${requestScope.user.name}<br>
    ${user.age}<br>
    ${user.date}<br>
    ${user.date.year}<br>
</body>
</html>

<!--
    输出：
        linxuan
        19
        Sat Mar 19 16:08:40 CST 2022
        122
-->
```

我们发现，输出的日期格式并不是我们想要的，所以我们可以利用一波获取对象的值的方法，在User类里面定义好输出日期的格式。

```java
User类：
/**
 * 逻辑视图，非常常用的一种方式
 * @return
 */
public String getDateStr() {
    if (date != null) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    	return sdf.format(date);
    } else {
    	return "";
    }
}

jsp文件里面键入下面代码：
<!--输出指定格式的日期-->
${user.dateStr}

<!--这样就会在页面输入中国的时间格式了-->
```

## 3.5 获取List、Map集合的值

<!--P774-->

### 获取List集合

获取List集合的方法：`${域名称.键名[索引]}`

```jsp
<%@ page import="cn.com.demo01.User" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        // 获取User对象，存储数据
        User user = new User();
        user.setName("linxuan");
        user.setAge(19);
        user.setDate(new Date());

        request.setAttribute("user", user);

        List list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add(user);

        request.setAttribute("list", list);
    %>

    <!--输出list集合值-->
    ${requestScope.list}<br>
    ${requestScope.list[0]}<br>
    ${requestScope.list[1]}<br>
    ${requestScope.list[2].name}<br>
    <!--对越界索引做了一个优化，所以不会报错，只会打印空字符串-->
    ${requestScope.list[3]}

</body>
</html>

<!--
	输出：
		[aaa, bbb, cn.com.demo01.User@4d322f31]
        aaa
        bbb
        linxuan
-->
```

> 注意：
>
> ​	在EL表达式里面对越界索引异常做了优化，并不会报错，只会打印空字符串

### 获取Map集合

获取Map集合有两种方法，如下：

* `${域名称.键名.key名称}`
* `${域名称.键名["key名称"]}`

```jsp
<%@ page import="cn.com.demo01.User" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        // 获取User对象，存储数据
        User user = new User();
        user.setName("linxuan");
        user.setAge(19);
        user.setDate(new Date());

        Map map = new HashMap();
        map.put("name", "李四");
        map.put("gender", "男");
        map.put("user", user);

        request.setAttribute("map", map);
    %>

    <!--获取Map值-->
    ${map.name}<br>
    ${map["gender"]}<br>
    ${map.user.name}<br>
</body>
</html>

<!--
	输出：
        李四
        男
        linxuan
-->
```

## 3.6 隐式对象：

<!--P775 3.21-->

el表达式中一共有着11个隐式对象，我们只需要学习pageContext即可。

* pageContext：获取jsp其他八个内置对象
  * ${pageContext.request.contextPath}：动态获取虚拟目录

# 第四章 JSTL标签

## 4.1 基本概述

<!--P776-->

JSTL标签概念：JavaServer Pages Tag Library  JSP标准标签库
* **是由Apache组织提供的开源的免费的jsp标签**

1. 作用：用于简化和替换jsp页面上的java代码		
2. 使用步骤：
  1. 导入jstl相关jar包
  2. 引入标签库：taglib指令：  `<%@ taglib %>`
  3. 使用标签

## 4.2 常用的JSTL标签

### if标签

<!--P777-->

**if标签相当于java代码的if语句**

if标签里面有着一个test属性，该属性必须添加，否则报错，该属性接受boolean表达式
* 如果表达式为true，则显示if标签体内容，如果为false，则不显示标签体内容

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <html>
  <head>
      <title>Title</title>
  </head>
  <body>
      <c:if test="true">
          <h1>你好</h1>
      </c:if>
  </body>
  </html>
  
  <!--
  	显示：
  		你好
  -->
  ```

* 一般情况下，test属性值会结合el表达式一起使用

  ```jsp
  <%@ page import="java.util.List" %>
  <%@ page import="java.util.ArrayList" %>
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <html>
  <head>
      <title>Title</title>
  </head>
  <body>
      <%
          List<Integer> list = new ArrayList<>();
          list.add(1);
          request.setAttribute("list", list);
      %>
  
      <!--判断是否为空-->
      <c:if test="${not empty list}">
          <h2>不为空</h2>
      </c:if>
  
  </body>
  </html>
  
  <!--
  	显示：
  		不为空
  -->
  ```

> 注意：c:if标签没有else情况，如果我们想要else情况，那么可以再定义一个c:if标签

### choose标签

<!--P778-->

**choose标签相当于java代码的switch语句**

* 使用choose标签声明 == 相当于switch声明
* 使用when标签做判断 == 相当于case
* 使用otherwise标签做其他情况的声明 == 相当于default

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        // 存储信息
        request.setAttribute("number", 1);
    %>

    <c:choose>
        <c:when test="${number == 1}">星期一</c:when>
        <c:when test="${number == 2}">星期二</c:when>
        <c:when test="${number == 3}">星期三</c:when>
        <c:when test="${number == 4}">星期四</c:when>
        <c:when test="${number == 5}">星期五</c:when>
        <c:when test="${number == 6}">星期六</c:when>
        <c:when test="${number == 7}">星期天</c:when>

        <c:otherwise>数字输入有误</c:otherwise>
    </c:choose>
</body>
</html>

<!--
	显示：
		星期一
-->
```

### foreach标签

<!--P779-->

**foreach标签相当于java代码的for语句**

对于for循环，在Java中我们知道有两种，一种普通for循环，用于完成重复的操作；一种是增强for循环，会直接遍历容器。

同样的，在JSTL里面对于for循环也有两种，他们都叫做foreach。只是属性不同而已。

* 普通的for循环属性如下：

  1. begin：开始值
  2. end：结束值
  3. var：临时变量
  4. setp：步长

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <html>
  <head>
      <title>Title</title>
  </head>
  <body>
  
      <c:forEach begin="1" end="10" var="i" step="1">
          ${i} <br>
      </c:forEach>
  </body>
  </html>
  
  <!--
  	显示：
  		1
          2
          3
          4
          5
          6
          7
          8
          9
          10
  -->
  ```

* 增强for循环属性如下：

  1. items：容器对象
  2. var：容器中元素的临时变量
  3. varStatus：循环状态对象
     * index：容器中元素的索引，从0开始
     * count：循环次数，从1开始

  ```jsp
  <%@ page import="java.util.List" %>
  <%@ page import="java.util.ArrayList" %>
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <html>
  <head>
      <title>Title</title>
  </head>
  <body>
  
      <%
          List<String> list = new ArrayList<>();
          list.add("aaa");
          list.add("bbb");
          list.add("ccc");
          request.setAttribute("list", list);
      %>
  
      <c:forEach items="${list}" var="str" varStatus="s">
          ${str} ${s.index} ${s.count} <br>
      </c:forEach>
  </body>
  </html>
  
  <!--
  	显示：
          aaa 0 1
          bbb 1 2
          ccc 2 3
  -->
  ```

## 4.3 练习

<!--P780-->

* 需求：在request域中有一个存有User对象的List集合。需要使用jstl+el将list集合数据展示到jsp页面的表格table中

```jsp
<%@ page import="java.util.List" %>
<%@ page import="cn.com.demo01.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        List<User> list = new ArrayList<>();
        list.add(new User("张三", 24, new Date()));
        list.add(new User("李四", 35, new Date()));
        list.add(new User("王五", 46, new Date()));
        request.setAttribute("list", list);
    %>

    <table border="1" align="center" width="500">
        <tr>
            <th>编号</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>日期</th>
        </tr>

        <c:forEach items="${list}" var="user" varStatus="u">
            
            <!--弄一些颜色来搞的丰富一些-->
            <c:if test="${u.count % 2 == 0}">
                <tr bgcolor="aqua">
                    <td>${u.count}</td>
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                    <td>${user.dateStr}</td>
                </tr>
            </c:if>

            <c:if test="${u.count % 2 != 0}">
                <tr bgcolor="red">
                    <td>${u.count}</td>
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                    <td>${user.dateStr}</td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
</body>
</html>

<!--
	显示：
        编号	姓名	年龄	日期
        1	张三	24	2022-03-21 11:31:09
        2	李四	35	2022-03-21 11:31:09
        3	王五	46	2022-03-21 11:31:09
-->
```

```java
package cn.com.demo01;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String name;
    private int age;
    private Date date;

    public User(String name, int age, Date date) {
        this.name = name;
        this.age = age;
        this.date = date;
    }

    public User() {
    }

    /**
     * 逻辑视图，非常常用的一种方式
     * @return
     */
    public String getDateStr() {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

```

# 第五章 三层架构：软件设计架构

<!--P781-->

1. 界面层(表示层)：用户看的得界面。用户可以通过界面上的组件和服务器进行交互
2. 业务逻辑层：处理业务逻辑的。
3. 数据访问层：操作数据存储文件。
3. ![](D:\Java\笔记\图片\3-day17【EL、JSTL】\2三层架构.bmp)

# 第六章 案例：用户信息列表展示

<!--p782-->

<!--P783-->

## 6.1 需求

需求：用户信息的增删改查操作

## 6.2 设计

技术选型：`Servlet+JSP+MySQL+JDBCTempleat+Duird+BeanUtilS+tomcat`

数据库设计：

```sql
create database day17; -- 创建数据库
use day17; 			   -- 使用数据库
create table user(   -- 创建表
	id int primary key auto_increment,
	name varchar(20) not null,
	gender varchar(5),
	age int,
	address varchar(32),
	qq	varchar(20),
	email varchar(50)
);
```

## 6.3 开发

1. 环境搭建
	1. 创建数据库环境
	2. 创建项目，导入需要的jar包
2. 编写代码

## 6.4 测试

## 6.5 部署运维

## 6.6 分析

<!--P784-->

<!--P785 跳跃一P，没电了-->

<!--P786 3.23--->



![](D:\Java\笔记\图片\3-day17【EL、JSTL】\3列表查询分析.bmp)

创建存储信息的User类：

```java
package cn.linxuan.domain;

public class User {
    private int id;
    private String name;
    private String gender;
    private int age;
    private String address;
    private String qq;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", qq='" + qq + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

```

创建index.jsp：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>首页</title>

  <!-- 1. 导入CSS的全局样式 -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
  <script src="js/jquery-2.1.0.min.js"></script>
  <!-- 3. 导入bootstrap的js文件 -->
  <script src="js/bootstrap.min.js"></script>
  <script type="text/javascript">
  </script>
</head>
<body>
<div align="center">
  <a
          href="${pageContext.request.contextPath}/userListServlet" style="text-decoration:none;font-size:33px">查询所有用户信息
  </a>
</div>
</body>
</html>
```

创建UserListServlet类：

```java
package cn.linxuan.web.servlet;

import cn.linxuan.domain.User;
import cn.linxuan.service.UserService;
import cn.linxuan.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/userListServlet")
public class UserListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 调用UserService完成查询
        UserService userService = new UserServiceImpl();
        List<User> users = userService.findAll();
        // 将list集合存入request域中
        req.setAttribute("users", users);
        // 转发到list.jsp
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

```

创建UserService接口：

```java

import cn.linxuan.domain.User;

import java.util.List;

/**
 * 用户管理的业务接口
 */
public interface UserService {

    /**
     * 查询所有的用户信息
     * @return
     */
    public List<User> findAll();
}

```

实现UserService接口：

```java
package cn.linxuan.service.impl;

import cn.linxuan.dao.UserDao;
import cn.linxuan.dao.impl.UserDaoImpl;
import cn.linxuan.domain.User;
import cn.linxuan.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    @Override
    public List<User> findAll() {
        // 调用Dao完成查询
        return dao.findAll();
    }
}

```

创建UserDao类：

```JAVA
package cn.linxuan.dao;

import cn.linxuan.domain.User;

import java.util.List;

/**
 * 用户操作的Dao
 */
public interface UserDao {
    public List<User> findAll();
}

```

实现UserDao类：

```java
package cn.linxuan.dao.impl;

import cn.linxuan.dao.UserDao;
import cn.linxuan.domain.User;
import cn.linxuan.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<User> findAll() {
        // 使用JDBC操作数据库
        // 定义sql
        String sql = "select * from user";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return users;
    }
}
```

导入工具类，使用DURID连接池技术：

```java
package cn.linxuan.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC工具类 使用Durid连接池
 */
public class JDBCUtils {

    private static DataSource ds ;

    static {

        try {
            //1.加载配置文件
            Properties pro = new Properties();
            //使用ClassLoader加载配置文件，获取字节输入流
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            pro.load(is);

            //2.初始化连接池对象
            ds = DruidDataSourceFactory.createDataSource(pro);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接池对象
     */
    public static DataSource getDataSource(){
        return ds;
    }


    /**
     * 获取连接Connection对象
     */
    public static Connection getConnection() throws SQLException {
        return  ds.getConnection();
    }
}
```

创建list.jsp：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>用户信息管理系统</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h3 style="text-align: center">用户信息列表</h3>
    <table border="1" class="table table-bordered table-hover">
        <tr class="success">
            <th>编号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>籍贯</th>
            <th>QQ</th>
            <th>邮箱</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${users}" var="user" varStatus="s">
            <tr>
                <td>${s.count}</td>
                <td>${user.name}</td>
                <td>${user.gender}</td>
                <td>${user.age}</td>
                <td>${user.address}</td>
                <td>${user.qq}</td>
                <td>${user.email}</td>
                <td><a class="btn btn-default btn-sm" href="update.html">修改</a>&nbsp;<a class="btn btn-default btn-sm" href="">删除</a></td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="8" align="center"><a class="btn btn-primary" href="add.html">添加联系人</a></td>
        </tr>
    </table>
</div>
</body>
</html>

```

# 第七章 案例进阶

<!--P787 也是day18了，在这里写就不新建了-->

<!--P788-->

## 7.1 修改页面

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>用户信息管理系统</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h3 style="text-align: center">用户信息列表</h3>

    <div style="float: left;">

        <form class="form-inline">
            <div class="form-group">
                <label for="exampleInputName2">姓名</label>
                <input type="text" class="form-control" id="exampleInputName2" >
            </div>
            <div class="form-group">
                <label for="exampleInputName3">籍贯</label>
                <input type="text" class="form-control" id="exampleInputName3" >
            </div>

            <div class="form-group">
                <label for="exampleInputEmail2">邮箱</label>
                <input type="email" class="form-control" id="exampleInputEmail2"  >
            </div>
            <button type="submit" class="btn btn-default">查询</button>
        </form>

    </div>

    <div style="float: right;margin: 5px;">

        <a class="btn btn-primary" href="add.html">添加联系人</a>
        <a class="btn btn-primary" href="add.html">删除选中</a>

    </div>

    <table border="1" class="table table-bordered table-hover">
        <tr class="success">
            <th><input type="checkbox"></th>
            <th>编号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>籍贯</th>
            <th>QQ</th>
            <th>邮箱</th>
            <th>操作</th>
        </tr>

        <c:forEach items="${users}" var="user" varStatus="s">
            <tr>
                <td><input type="checkbox"></td>
                <td>${s.count}</td>
                <td>${user.name}</td>
                <td>${user.gender}</td>
                <td>${user.age}</td>
                <td>${user.address}</td>
                <td>${user.qq}</td>
                <td>${user.email}</td>
                <td><a class="btn btn-default btn-sm" href="update.html">修改</a>&nbsp;<a class="btn btn-default btn-sm" href="">删除</a></td>
            </tr>

        </c:forEach>


    </table>
    <div>
        <nav aria-label="Page navigation">
            <ul class="pagination">
                <li>
                    <a href="#" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <li><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li>
                    <a href="#" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
                <span style="font-size: 25px;margin-left: 5px;">
                    共16条记录，共4页
                </span>

            </ul>
        </nav>
    </div>
</div>
</body>
</html>

```

<!--P789-->

<!--P794-->

