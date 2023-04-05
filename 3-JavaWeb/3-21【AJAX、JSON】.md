# 第一章 Ajax

`AJAX` (Asynchronous JavaScript And XML)：异步的 JavaScript 和 XML。AJAX 作用有以下两方面：与服务器进行数据交换、异步交互。

**与服务器进行数据交换**

通过AJAX可以给服务器发送请求，服务器将数据直接响应回给浏览器。我们先来看之前做功能的流程：`Servlet` 调用完业务逻辑层后将数据存储到域对象中，然后跳转到指定的 `jsp` 页面，在页面上使用 `EL表达式` 和 `JSTL` 标签库进行数据的展示。

<img src="..\图片\3-22【AJAX、JSON】\1-1.png" />

而我们学习了AJAX 后，就可以使用AJAX和服务器进行通信，以达到使用 HTML+AJAX来替换JSP页面了。浏览器发送请求servlet，servlet 调用完业务逻辑层后将数据直接响应回给浏览器页面，页面使用 HTML 来进行数据展示。

<img src="..\图片\3-22【AJAX、JSON】\1-2.png" />

**异步交互**

可以在不重新加载整个页面的情况下，与服务器交换数据并更新部分网页的技术，如：搜索联想、用户名是否可用校验，等等…

## 1.1 异步和同步

同步发送请求过程如下：浏览器页面在发送请求给服务器，在服务器处理请求的过程中，浏览器页面不能做其他的操作。只能等到服务器响应结束后浏览器页面才能继续做其他的操作。

<img src="..\图片\3-22【AJAX、JSON】\1-3.png" />

异步发送请求过程如下：浏览器页面发送请求给服务器，服务器处理请求过程中，浏览器页面还可以做其他操作。

<img src="..\图片\3-22【AJAX、JSON】\1-4.png" />

## 1.2  快速入门

```java
@WebServlet("/ajaxServlet")
public class AjaxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 响应数据
        response.getWriter().write("hello ajax~");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
```

```html
<!-- 前端页面实现如下，命名为index.html，在webapp目录下面创建 -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<script>
    //1. 创建核心对象
    var xhttp;
    if (window.XMLHttpRequest) {
        xhttp = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    //2. 发送请求
    xhttp.open("GET", "http://localhost/ajaxServlet");
    xhttp.send();

    //3. 获取响应
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // 通过 this.responseText 可以获取到服务端响应的数据
            alert(this.responseText);
        }
    };
</script>
</body>
</html>
```

浏览器地址栏输入：http://localhost/html/index.html，在html加载的时候会发送ajax请求。可以通过开发者模式查看发送的 AJAX 请求。在浏览器上按 `F12` 快捷键

<img src="..\图片\3-22【AJAX、JSON】\1-5.png" />

## 1.3 axios

Axios 对原生的AJAX进行封装，简化书写。Axios官网是：https://www.axios-http.cn。axios 使用分为以下两步：引入 axios 的 js 文件、使用axios 发送请求并获取响应结果。

```java
@WebServlet("/ajaxServlet")
public class AjaxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 响应数据
        response.getWriter().write("hello ajax~");
        System.out.println("11111111111111");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>123</title>
</head>
<body>

<!-- 引入 axios 的 js 文件 -->
<script src="../js/axios-0.18.0.js"></script>
<script>
    // 发送get请求
    axios({
        method: "get",
        url: "http://localhost/ajaxServlet"
    }).then(function (resp) {
        alert(resp.data);
        alert("124");
    })
</script>
</body>
</html>
```

```js
// 使用axios 发送请求，并获取响应结果。发送 post 请求
axios({
    method:"post",
    url:"http://localhost:8080/ajax-demo1/aJAXDemo1",
    data:"username=zhangsan"
}).then(function (resp){
    alert(resp.data);
});
```

`axios()` 是用来发送异步请求的，小括号中使用 js 对象传递请求相关的参数：

* `method` 属性：用来设置请求方式的。取值为 `get` 或者 `post`。
* `url` 属性：用来书写请求的资源路径。如果是 `get` 请求，需要将请求参数拼接到路径的后面，格式为： `url?参数名=参数值&参数名2=参数值2`。
* `data` 属性：作为请求体被发送的数据。也就是说如果是 `post` 请求的话，数据需要作为 `data` 属性的值。

`then()` 需要传递一个匿名函数。我们将 `then()` 中传递的匿名函数称为 回调函数，意思是该匿名函数在发送请求时不会被调用，而是在成功响应后调用的函数。而该回调函数中的 `resp` 参数是对响应的数据进行封装的对象，通过 `resp.data` 可以获取到响应的数据。

为了方便起见， Axios 已经为所有支持的请求方法提供了别名。如下：

| 请求    | 别名                              |
| ------- | --------------------------------- |
| get     | `axios.get(url[,config])`         |
| delete  | `axios.delete(url[,config])`      |
| head    | `axios.head(url[,config])`        |
| options | `axios.option(url[,config])`      |
| post    | `axios.post(url[,data[,config])`  |
| put     | `axios.put(url[,data[,config])`   |
| patch   | `axios.patch(url[,data[,config])` |

入门案例中的 `get` 请求代码可以改为如下：

```js
axios.get("http://localhost:8080/ajax-demo/axiosServlet?username=zhangsan").then(function (resp) {
    alert(resp.data);
});
```

入门案例中的 `post` 请求代码可以改为如下：

```js
axios.post("http://localhost:8080/ajax-demo/axiosServlet","username=zhangsan").then(function (resp) {
    alert(resp.data);
})
```

# 第二章 JSON

概念：`JavaScript Object Notation`。JavaScript 对象表示法。由于其语法格式简单，层次结构鲜明，现多用于作为数据载体，在网络中进行数据传输。

JSON 串的键要求必须使用双引号括起来，而值根据要表示的类型确定。value 的数据类型分为如下：数字（整数或浮点数）、字符串（使用双引号括起来）、逻辑值（true或者false）、数组（在方括号中）、对象（在花括号中）、null。

```json
var jsonStr = '{
			"name":"zhangsan",
			"age":23,
			"addr":["北京","上海","西安"]
}'
```

对于JSON数据类型，我们常见的有三种：json普通数组、json对象、json对象数组。格式如下

```json
// json普通数组
["value1", "value2", "value3", ...]

// json对象
{
    "key1": "value1",
    "key2": "value2"
}

// json对象数组
[
    {"key1": "value1"},
    {"key2": "value2"}
]
```

## 2.1 前端获取JSON值

如果JSON是一个 js 对象，我们就可以通过 `js对象.属性名` 的方式来获取数据。JS 提供了一个对象 `JSON` ，该对象有如下两个方法：

* `parse(str)` ：将 JSON串转换为 js 对象。使用方式是： `var jsObject = JSON.parse(jsonStr);`
* `stringify(obj)` ：将 js 对象转换为 JSON 串。使用方式是：`var jsonStr = JSON.stringify(jsObject)`

代码演示：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<script>
    //1. 定义JSON字符串 这就是一个字符串
    var jsonStr = '{"name":"zhangsan","age":23,"addr":["北京","上海","西安"]}'
    alert(jsonStr);

    //2. 将 JSON 字符串转为 JS 对象
    let jsObject = JSON.parse(jsonStr);
    alert(jsObject)
    alert(jsObject.name)
    //3. 将 JS 对象转换为 JSON 字符串
    let jsonStr2 = JSON.stringify(jsObject);
    alert(jsonStr2)
</script>
</body>
</html>
```

## 2.2 发送异步请求携带参数

后面使用 `axios` 发送请求时，如果要携带复杂的数据时都会以 `JSON` 格式进行传递。可以提前定义一个 js 对象，用来封装需要提交的参数，然后使用 `JSON.stringify(js对象)` 转换为 `JSON` 串，再将该 `JSON` 串作为 `axios` 的 `data` 属性值进行请求参数的提交。如下：

```js
var jsObject = {name:"张三"};

axios({
    // 发送异步请求时，如果请求参数是JSON格式，那请求方式必须是POST。因为JSON串需要放在请求体中。
    method:"post",
    url:"http://localhost:8080/ajax-demo/axiosServlet",
    data: JSON.stringify(jsObject)
}).then(function (resp) {
    alert(resp.data);
})
```

而 `axios` 是一个很强大的工具。我们只需要将需要提交的参数封装成 js 对象，并将该 js 对象作为 `axios` 的 `data` 属性值进行，它会自动将 js 对象转换为 `JSON` 串进行提交。如下：

```js
var jsObject = {name:"张三"};

axios({
    // 发送异步请求时，如果请求参数是JSON格式，那请求方式必须是POST。因为JSON串需要放在请求体中。
    method:"post",
    url:"http://localhost:8080/ajax-demo/axiosServlet",
    data:jsObject  //这里 axios 会将该js对象转换为 json 串的
}).then(function (resp) {
    alert(resp.data);
})
```

## 2.3 JSON和Java对象转换

以后我们会以 json 格式的数据进行前后端交互。前端发送请求时，如果是复杂的数据就会以 json 提交给后端；而后端如果需要响应一些复杂的数据时，也需要以 json 格式将数据响应回给浏览器。

在后端我们就需要重点学习以下两部分操作：请求数据（JSON字符串转为Java对象）、响应数据（Java对象转为JSON字符串）。

`Fastjson` 是阿里巴巴提供的一个Java语言编写的高性能功能完善的 `JSON` 库，是目前Java语言中最快的 `JSON` 库，可以实现 `Java` 对象和 `JSON` 字符串的相互转换。

```xml
<!-- 可以实现Java对象和JSON字符串的相互转换。后面SpringMVC里面有着jackson也可以实现该功能 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.62</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
    <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
    <scope>provided</scope>
</dependency>
```

```java
@Data
@AllArgsConstrunctor
@NoArgsConstrunctor
public class User {
    Integer id;
    String username;
    String password;
}
```

```java
public static void main(String[] args) {

    User user = new User(1, "zhangsan", "123");

    // 将Java对象转为JSON字符串
    String jsonString = JSON.toJSONString(user);
    // {"id":1,"password":"123","username":"zhangsan"}
    System.out.println(jsonString);

    //2. 将JSON字符串转为Java对象 \是转义字符，这样就不会解析双引号了
    User u = JSON.parseObject("{\"id\":1,\"password\":\"123\",\"username\":\"zhangsan\"}", User.class);
    System.out.println(u);
}
```


# 第三章 案例

使用Axios + JSON 完成品牌列表数据查询和添加。

<img src="..\图片\3-22【AJAX、JSON】\3-1.png" />

## 3.1 基础环境准备

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>

<dependencies>
    <!-- Servlet依赖 -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <!-- 依赖作用范围未provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>

    <!-- mybatis操作数据库相关依赖 -->
    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.32</version>
    </dependency>
    <!-- Druid连接池 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- Mybatis核心依赖 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.7</version>
    </dependency>

    <!-- 测试依赖 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>

    <!-- JSON和Java对象格式的转换 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>2.0.25</version>
    </dependency>

    <!-- 日志 -->
    <!-- 使用Log4j，这样可以打印mybatis中执行的SQL语句 -->
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>

    <!-- 其他依赖 -->
    <!-- Lombok简化实体类开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
        <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;

# 如果存在该表那么就删除
DROP TABLE IF EXISTS tb_brand;
# 如果不存在tb_brand表那么就创建
CREATE TABLE IF NOT EXISTS tb_brand(
    id INT PRIMARY KEY AUTO_INCREMENT comment "序号",
    brand_name VARCHAR(20) NOT NULL comment "品牌名称",
    company_name VARCHAR(20) NOT NULL  comment "企业名称",
    ordered BIGINT NOT NULL comment "排序",
    description VARCHAR(32) NOT NULL  comment "品牌介绍",
    STATUS INT comment "状态"
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_book;

# 插入数据
INSERT INTO tb_brand (brand_name, company_name, ordered, description, STATUS) VALUES
	("三只松鼠", "三只松鼠", 100, "三只松鼠，好吃不上火", 0), 
	("优衣库", "优衣库", 10, "优衣库，服适人生", 1),   
	("小米", "小米科技有限公司", 1000, "为发烧而生", 0), 
	("鸿星尔克", "鸿星尔克", 101, "1111", 0);
# 查询数据
SELECT * FROM tb_brand;
```

```properties
# resources目录下面创建一个jdbc.properties文件，添加对应键值对
# MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/linxuan?useSSL=false
jdbc.username=root
jdbc.password=root
```

```xml
<!-- log4j的配置文件log4j.xml，这样可以在控制台输出SQL语句，这句注释不要放在第一行-->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<!-- xmlns:log4j="http://jakarta.apache.org/log4j/"会爆红，不过没得关系 -->
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
    <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
        <param name="Encoding" value="UTF-8"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS} %m (%F:%L) \n"/>
        </layout>
    </appender>
    <logger name="java.sql">
        <level value="debug"/>
    </logger>
    <logger name="org.apache.ibatis">
        <level value="info"/>
    </logger>
    <root>
        <level value="debug"/>
        <appender-ref ref="STDOUT"/>
    </root>
</log4j:configuration>
```

```java
package com.linxuan.pojo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand{
    Integer id;
    String brandName;
    String companyName;
    Long ordered;
    String description;
    Integer status;
}
```

## 3.2 MyBatis环境准备

```xml
<!-- mybatis-config.xml -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--引入properties文件，此时就可以${属性名}的方式访问属性值-->
    <properties resource="jdbc.properties"/>

    <!--设置Mybatis全局配置-->
    <settings>
        <!-- 开启驼峰命名 -->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>

    <typeAliases>
        <!--以包为单位，设置改包下所有的类型都拥有默认的别名，即类名且不区分大小写-->
        <package name="com.linxuan.pojo"/>
    </typeAliases>

    <!--设置连接数据库的环境-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!--设置驱动类的全类名-->
                <property name="driver" value="${jdbc.driver}"/>
                <!--设置连接数据库的连接地址-->
                <property name="url" value="${jdbc.url}"/>
                <!--设置连接数据库的用户名-->
                <property name="username" value="${jdbc.username}"/>
                <!--设置连接数据库的密码-->
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

    <!--引入映射文件-->
    <mappers>
        <package name="com.linxuan.dao"/>
    </mappers>
</configuration>
```

```java
package com.linxuan.dao;

public interface BrandDao {

    /**
     * 查询所有并返回
     * @return
     */
    List<Brand> getAll();

    /**
     * 新增功能
     * @param brand
     * @return
     */
    int save(Brand brand);
}
```

```xml
<!-- 在resources目录下面com/linxuan/dao目录下面的BrandDao.xml配置文件-->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.linxuan.dao.BrandDao">

    <!-- List<Brand> getAll(); -->
    <select id="getAll" resultType="brand">
        SELECT * FROM tb_brand;
    </select>

    <!-- int save(Brand brand); -->
    <insert id="save">
        insert into tb_brand value (null, #{brandName}, #{companyName}, #{ordered}, #{description}, #{status});
    </insert>

</mapper>
```

```java
package com.linxuan.utils;

public class SqlSessionFactoryUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        // 静态代码块会随着类的加载而自动执行，且只执行一次
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }
}
```

```java
package com.linxuan.service;

public class BrandService {
    private SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();

    // 查询所有
    public List<Brand> getAll() {
        // 获取SqlSession对象，同时设置事务为自动提交
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 获取BranDao对象
        BrandDao brandDao = sqlSession.getMapper(BrandDao.class);
        // 获取结果
        List<Brand> brands = brandDao.getAll();
        // 关闭sqlSession，必须在获取结果之后才能够关闭SqlSession
        sqlSession.close();
        return brands;
    }

    // 新增
    public int save(Brand brand) {
        // 获取SqlSession对象，同时设置事务为自动提交
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 获取BranDao对象
        BrandDao brandDao = sqlSession.getMapper(BrandDao.class);
        // 获取结果
        int ret = brandDao.save(brand);
        // 关闭sqlSession，必须在获取结果之后才能够关闭SqlSession
        sqlSession.close();
        return ret;
    }
}
```

```java
package com.linxuan.service;

// 测试类，在Test目录下面创建
public class BrandServiceTest {

    BrandService brandService = new BrandService();

    // 测试Service里面的方法是否成功
    @Test
    public void testGetAll() {
        brandService.getAll().forEach(System.out::println);
    }

    @Test
    public void testSave() {
        Brand brand = new Brand(null, "三只松鼠111", "三只松鼠", 111L, "三只松鼠", 0);
        int ret = brandService.save(brand);
        System.out.println(ret);
    }
}
```

## 3.3 查询所有

```html
<!-- index.html -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <a href="addBrand.html"><input type="button" value="新增"></a><br>
<hr>
<!-- 表格内容一会动态设置 -->
<table id="brandTable" border="1" cellspacing="0" width="100%"></table>
<!-- 引入axios.js资源文件。需要提前导入axios.js文件。 -->
<script src="js/axios-0.18.0.js"></script>
<script>
    //1. 当页面加载完成后，发送ajax请求
    window.onload = function () {
        //2. 发送ajax请求
        axios.get("/selectAll").then(function (resp) {
            //获取数据
            let brands = resp.data;
            let tableData = " <tr>\n" +
                "        <th>序号</th>\n" +
                "        <th>品牌名称</th>\n" +
                "        <th>企业名称</th>\n" +
                "        <th>排序</th>\n" +
                "        <th>品牌介绍</th>\n" +
                "        <th>状态</th>\n" +
                "        <th>操作</th>\n" +
                "    </tr>";

            for (let i = 0; i < brands.length; i++) {
                let brand = brands[i];

                tableData += "\n" +
                    "    <tr align=\"center\">\n" +
                    "        <td>" + (i + 1) + "</td>\n" +
                    "        <td>" + brand.brandName + "</td>\n" +
                    "        <td>" + brand.companyName + "</td>\n" +
                    "        <td>" + brand.ordered + "</td>\n" +
                    "        <td>" + brand.description + "</td>\n" +
                    "        <td>" + brand.status + "</td>\n" +
                    "\n" +
                    "        <td><a href=\"#\">修改</a> <a href=\"#\">删除</a></td>\n" +
                    "    </tr>";
            }
            // 设置表格数据
            document.getElementById("brandTable").innerHTML = tableData;
        })
    }
</script>
</body>
</html>
```

```java
@WebServlet("/selectAll")
public class SelectAllServlet extends HttpServlet {
    private BrandService brandService = new BrandService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置返回编码
        resp.setContentType("text/json;charset=utf-8");
        // 调用service类里面的getAll方法
        List<Brand> brands = brandService.getAll();
        // 打印控制台看看有没有执行
        // System.out.println(JSON.toJSONString(brands));
        // 将方法返回值转换为JSON格式返回前端
        resp.getWriter().write(JSON.toJSONString(brands));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

## 3.4 添加功能

```html
<!-- addBrand.html -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>添加品牌</title>
</head>
<body>
<h3>添加品牌</h3>
<form action="" method="post">
    品牌名称：<input id="brandName" name="brandName" /><br>
    企业名称：<input id="companyName" name="companyName" /><br>
    排序：<input id="ordered" name="ordered" /><br>
    描述信息：<textarea rows="5" cols="20" id="description" name="description"></textarea><br>
    状态：
    <input type="radio" name="status" value="0">禁用
    <input type="radio" name="status" value="1">启用<br>

    <input type="button" id="btn" value="提交">
</form>

<!-- 引入axios.js文件 -->
<script src="js/axios-0.18.0.js"></script>
<script>
    // 给按钮绑定单击事件
    document.getElementById("btn").onclick = function () {
        // 将表单数据转为json
        var formData = {
            brandName: "",
            companyName: "",
            ordered: "",
            description: "",
            status: "",
        };
        // 获取表单数据
        let brandName = document.getElementById("brandName").value;
        let companyName = document.getElementById("companyName").value;
        let ordered = document.getElementById("ordered").value;
        let description = document.getElementById("description").value;
        // 这个不需要获取值，获取到的是两个对象，然后遍历
        let status = document.getElementsByName("status");

        // 设置数据
        formData.brandName = brandName;
        formData.companyName = companyName;
        formData.ordered = ordered;
        formData.description = description;
        // 因为有两个status，所以需要都获取到，然后遍历看看哪一个被选中了，最后赋值
        for (let i = 0; i < status.length; i++) {
            if (status[i].checked) {
                formData.status = status[i].value;
            }
        }

        // 发送ajax请求
        axios.post("/save", formData).then(function (resp) {
            // 判断响应数据是否为 success
            if (resp.data == "success") {
                location.href = "http://localhost/brand.html";
            }
        })
    }
</script>
</body>
</html>
```

```java
@WebServlet("/save")
public class AddServlet extends HttpServlet {

    private BrandService brandService = new BrandService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 接收数据。request.getParameter 不能接收json的数据
        // String brandName = request.getParameter("brandName");

        // 获取请求体数据
        BufferedReader br = req.getReader();
        String params = br.readLine();
        // 将JSON字符串转为Java对象
        Brand brand = JSON.parseObject(params, Brand.class);
        // 调用service添加
        brandService.save(brand);
        // 响应成功标识
        resp.getWriter().write("success");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

## 3.5 Vue优化查询所有

Vue声明周期/钩子函数如下：

| 状态          | 阶段周期 |
| ------------- | -------- |
| beforeCreate  | 创建前   |
| created       | 创建后   |
| beforeMount   | 载入前   |
| mounted       | 挂载完成 |
| beforeUpdate  | 更新前   |
| updated       | 更新后   |
| beforeDestroy | 销毁前   |
| destroyed     | 销毁后   |

`mounted`：挂载完成，Vue初始化成功，HTML页面渲染成功。而以后我们会在该方法中发送异步请求，加载数据。

我们的目的是页面加载完成之后发送异步请求`/select`给后端，后端查询数据然后返回给前端，最后遍历集合数据展示表格即可。使用Vue优化的话，函数可以放在`mounted`钩子函数里面，遍历可以使用`v-for`标签。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div id="app">
    <a href="addBrand.html"><input type="button" value="新增"></a><br>
    <hr>
    <table id="brandTable" border="1" cellspacing="0" width="100%">
        <tr>
            <th>序号</th>
            <th>品牌名称</th>
            <th>企业名称</th>
            <th>排序</th>
            <th>品牌介绍</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        <!--
            使用v-for遍历tr
        -->
        <tr v-for="(brand, i) in brands" align="center">
            <td>{{i + 1}}</td>
            <td>{{brand.brandName}}</td>
            <td>{{brand.companyName}}</td>
            <td>{{brand.ordered}}</td>
            <td>{{brand.description}}</td>
            <td>{{brand.statusStr}}</td>
            <td><a href="#">修改</a> <a href="#">删除</a></td>
        </tr>
    </table>
</div>
<script src="js/axios-0.18.0.js"></script>
<script src="js/vue.js"></script>

<script>
    new Vue({
        el: "#app",
        // 在 Vue 对象中定义模型数据
        data() {
            return {
                brands: []
            }
        },
        // 在钩子函数中发送异步请求，并将响应的数据赋值给数据模型
        mounted() {
            // 页面加载完成后，发送异步请求，查询数据
            var _this = this;

            // 这个可以省略成下面的
            axios({
                method: "get",
                url: "/selectAll"
            }).then(function (resp) {
                _this.brands = resp.data;
            })

            // 等同于下面的
            // axios.get("/selectAllServlet").then(function (resp) {
            //     _this.brands = resp.data;
            // })
        }
    })
</script>
</body>
</html>
```

## 3.6 Vue优化添加功能

<img src="..\图片\3-22【AJAX、JSON】\3-1.png" />

点击新增后跳转到一个新增的页面，新增完成之后点击提交按钮，发送ajax请求，携带JSON数据传送给后台。后台获取数据，添加至数据库并返回结果。前端获取后端返回的数据，判断是否添加成功，成功则跳转列表页面。

使用Vue优化的话，可以给这些框绑定一个模型数据，利用双向绑定特性，在发送异步请求时提交数据。

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>添加品牌</title>
    </head>
    <body>
        <div id="app">
            <h3>添加品牌</h3>
            <form action="" method="post">
                品牌名称：<input id="brandName" v-model="brand.brandName" name="brandName"><br>
                企业名称：<input id="companyName" v-model="brand.companyName" name="companyName"><br>
                排序：<input id="ordered" v-model="brand.ordered" name="ordered"><br>
                描述信息：<textarea rows="5" cols="20" id="description" v-model="brand.description" name="description"></textarea><br>
                状态：
                <input type="radio" name="status" v-model="brand.status" value="0">禁用
                <input type="radio" name="status" v-model="brand.status" value="1">启用<br>

                <input type="button" id="btn" @click="submitForm" value="提交">
            </form>
        </div>
        <script src="js/axios-0.18.0.js"></script>
        <script src="js/vue.js"></script>
        <script>
            new Vue({
                el: "#app",
                data(){
                    return {
                        brand:{}
                    }
                },
                methods:{
                    submitForm(){
                        // 发送ajax请求，添加
                        var _this = this;
                        axios({
                            method:"post",
                            url:"/addServlet",
                            data:_this.brand
                        }).then(function (resp) {
                            // 判断响应数据是否为 success
                            if(resp.data == "success"){
                                location.href = "/brand.html";
                            }
                        })
                    }
                }
            })
        </script>
    </body>
</html>
```



1. **在 addBrand.html 页面引入 vue 的js文件**

   ```html
   <script src="js/vue.js"></script>
   ```

2. **创建 Vue 对象**

   * 在 Vue 对象中定义模型数据 `brand` 
   * 定义一个 `submitForm()` 函数，用于给 `提交` 按钮提供绑定的函数
   * 在 `submitForm()` 函数中发送 ajax 请求，并将模型数据 `brand` 作为参数进行传递

   ```js
   new Vue({
       el: "#app",
       data(){
           return {
               brand:{}
           }
       },
       methods:{
           submitForm(){
               // 发送ajax请求，添加
               var _this = this;
               axios({
                   method:"post",
                   url:"http://localhost:8080/brand-demo/addServlet",
                   data:_this.brand
               }).then(function (resp) {
                   // 判断响应数据是否为 success
                   if(resp.data == "success"){
                       location.href = "http://localhost:8080/brand-demo/brand.html";
                   }
               })
   
           }
       }
   })
   ```

3. **修改视图**

   * 定义 `<div id="app"></div>` ，指定该 `div` 标签受 Vue 管理

   * 将 `body` 标签中所有的内容拷贝作为上面 `div` 标签中

   * 给每一个表单项标签绑定模型数据。最后这些数据要被封装到 `brand` 对象中

     ```html
     <div id="app">
         <h3>添加品牌</h3>
         <form action="" method="post">
             品牌名称：<input id="brandName" v-model="brand.brandName" name="brandName"><br>
             企业名称：<input id="companyName" v-model="brand.companyName" name="companyName"><br>
             排序：<input id="ordered" v-model="brand.ordered" name="ordered"><br>
             描述信息：<textarea rows="5" cols="20" id="description" v-model="brand.description" name="description"></textarea><br>
             状态：
             <input type="radio" name="status" v-model="brand.status" value="0">禁用
             <input type="radio" name="status" v-model="brand.status" value="1">启用<br>
     
             <input type="button" id="btn" @click="submitForm" value="提交">
         </form>
     </div>
     ```

**整体页面代码如下：**

```html

```

