# 第一章 XML快速入门

XML：Extensible Markup Language 可扩展标记语言。可扩展：标签都是自定义的。

XML功能：存储数据

1. 配置文件
2. 在网络中传输

XML与HTML的区别

1. XML的标签都是自定义的，HTML是预定义的。
2. XML的语法严格，HTML语法松散。
3. XML是存储数据的，HTML是展示数据的。

## 1.1 快速入门

```xml
<?xml version = "1.0"?>
<users>
	<user id = "1">
		<name>zhangsan</name>
		<age>23</age>
		<gender>male</gender>
	</user>
	
	<user id = "2">	
		<name>lisi</name>
		<age>24</age>
		<gender>male</gender>
	</user>
</users>
```

XML基本语法如下：

1. xml文档后缀名称为：`.xml`
2. xml第一行必须定义为文档声明，不能是空格或者换行
3. xml文档中有且仅有一个根标签
4. 属性值必须使用引号(单引号，双引号都可以)引起来
5. 标签必须正确关闭，`<user></user>`或者`<br/>`
6. xml标签名称区分大小写

## 1.2 XML组成部分

```xml
<?xml version = "1.0" encoding = "gbk" standalone = "yes"?>
```

属性列表

* version：版本号，必须的属性
* encoding：编码方式。告知解析引擎当前文档使用的字符集，默认值为ISO-8859-1
* standalone：是否独立    yes：不依赖其他文件     no：依赖其他文件

结合CSS指令：

```xml
<?xml-stylesheet type = "text/css" href = "a.css"?>
```

标签是自定义的，规则如下：

* 名称可以包含字母、数字以及其他的字符
* 名称不能以数字或者标点符号开始
* 名称不能以字母XML开始
* 名称不能包含空格

属性：id值唯一

CDATA区：在该区域中的数据会被原样展示。格式：`<![CDATA[ 数据 ]]>`

## 1.3 约束

约束：规定XML文档的书写规则。约束文档都是框架编写者编写的，是我们程序员使用的。

框架编写者编写一个框架后，不知道我们的XML都会定义什么标签，所以他需要限制我们定义标签的能力，所以编写了XML约束文档。

约束文档一共有两种：

1. DTD：一种简单的约束技术
2. Schema：一种复杂的约束技术。

### 1.3.1 DTD

`demo.dtd`基本内容如下：

```xml-dtd
<!-- 
	Element 元素的意思来定义标签
	ATTLIST 来定义属性
	这些和正则表达式一样 *代表出现0次或者多次 +代表出现一次或者多次 ?代表出现0次或者一次
		如果没有加，那么就代表只能出现一次
	#PCDATA 字符串
	#REQUIRED 必须出现
-->
<!ELEMENT students (student*)>		<!--声明了一个标签，标签名称为students，students标签里面放student子标签 *代表0次或者多次-->
<!ELEMENT student (name, age, sex)>	<!--声明了一个student标签，里面能出现name, age, sex标签，并且顺序不能够出错 只能够出现一次-->
<!ELEMENT name (#PCDATA)>
<!ELEMENT age (#PCDATA)>
<!ELEMENT sex (#PCDATA)>
<!ATTLIST student number ID #REQUIRED>	<!--声明student标签有属性，属性名字为number，属性类型为ID，ID表示唯一，也就是该属性唯一-->
```

得到了DTD约束文档，那么我们就需要按照约束文档来定义XML配置文件，而第一步我们首先需要将DTD约束文档给导入到XML文档中，这样XML配置文件才知道有什么样的规则。

而DTD文档导入又有多种情况：

* 内部dtd：将约束规则定义在XML文档中。

  ```xml
  <?xml version="1.0" encoding="utf-8" ?>
  <!--<!DOCTYPE students SYSTEM "demo.dtd">-->
  <!DOCTYPE students [
  		<!ELEMENT students (student*)>
  		<!ELEMENT student (name, age, sex)>
  		<!ELEMENT name (#PCDATA)>
  		<!ELEMENT age (#PCDATA)>
  		<!ELEMENT sex (#PCDATA)>
  		<!ATTLIST student number ID #REQUIRED>
  		]>
  ```

  将约束规则写到`[]`里面。`students`代表的是根标签名称。`!DOCTYPE`是必写的。

* 外部dtd：将约束的规则定义在外部的dtd文件中。

  而这种情况又分为两类：

  1. 本地dtd：dtd文档在电脑上面。这种情况在XML文件写入下面代码即可：

     ```xml
     <!DOCTYPE 根标签名称 SYSTEM "dtd文件位置">
     <!--<!DOCTYPE students SYSTEM "demo.dtd">-->
     ```

  2. 网络dtd：dtd文档在互联网上面存储。这种情况在XML文件写入下面代码即可：

     ```
     <!DOCTYPE 根标签名称 PUBLIC "dtd文件名称" "dtd文件的位置URL">
     ```
     
     ```jsp
     <!--这是一个jsp文档里面的-->
     <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
     ```

### 1.3.2 Schema

`Schema`，即`XML Schema`，`XSD (XML Schema Definition)`是W3C于2001年5月发布的推荐标准，指出如何形式描述`XML`文档的元素。`XSD`是许多`XML Schema` 语言中的一支。`XSD`是首先分离于`XML`本身的`schema`语言，故获取W3C的推荐地位。

像所有`XML Schema` 语言一样，`XSD`用来描述一组规则──一个XML文件必须遵守这些规则，才能根据该`schema`合法（Valid）。`Schema`约束文档的后缀名称为：`xsd`。

`demo.xsd`基本内容如下：

```xml
<!--
	Element 		元素的意思来定义标签
	complexType 	 组合类型
	simpleType		简单类型
	type			定义属性
	sequence 		按照顺序出现
	minOccurs 		最小出现
	maxOccurs 		最大出现
	unbounded		不限
	attribute		属性
	required		必须
	enumeration		枚举
	minInclusive	最小值
	maxInclusive	最大值
	pattern			组成格式

-->

<?xml version = "1.0"?>
<xsd:schema xmlns = "http://www.itcast.cn/xml"
		xmlns:xsd = "http://www.w3.org/2001/XMLSchema"
		targetNamespace = "http://www.itcast.cn/xml" elementFormDefault = "qualified">
    
	<xsd:element name = "students" type = "studentsType"/>	<!--自定义标签，定义类型，因为是自定义的类型，下面需要-->
	<xsd:complexType name = "studentsType">
		<xsd:sequence>
			<xsd:element name = "student" type = "studentType" minOccurs = "0" maxOccurs = "unbounded"/>
		</xsd:sequence>
	</xsd:complexType>
	<xsd:complexType name = "studentType">
		<xsd:sequence>
			<xsd:element name = "name" type = "xsd:string"/>
			<xsd:element name = "age" type = "ageType"/>
			<xsd:element name = "sex" type = "sexType"/>
		</xsd:sequence>
		<xsd:attribute name = "number" type = "numberType" use = "required"/>
	</xsd:complexType>
	<xsd:simpleType name = "sexType">
		<xsd:restriction base = "xsd:string">
			<xsd:enumeration value = "male"/>
			<xsd:enumeration value = "female"/>
		</xsd:restriction>
	</xsd:simpleType>
	<xsd:simpleType name = "ageType">
		<xsd:restriction base = "xsd:integer">
			<xsd:minInclusive value = "0"/>
			<xsd:maxInclusive value = "256"/>
		</xsd:restriction>
	</xsd:simpleType>
	<xsd:simpleType name = "numberType">
		<xsd:restriction base = "xsd:string">
             <!--student标签number的值需要是heima+任意四位数字-->
			<xsd:pattern value = "heima_\d{4}"/>
		</xsd:restriction>
	</xsd:simpleType>
</xsd:schema>
```

## 1.4 解析

解析：操作XML文档，将文档当中的数据读取到内存中。

操作XML文档有两种方式：

1. 解析(读取)：将文档中的数据读取到内存中。
2. 写入：将内存中的数据保存到XML文档中。持久化的存储。

解析XML文档也有两种方式：

1. DOM：将标记语言文档一次性加载进内存，在内存中形成一颗DOM数。
   * 优点：操作方便，可以对文档进行CRUD的所有操作
   * 缺点：占内存
   * 通常由于电脑端。
2. SAX：逐行读取，基于事件驱动
   * 优点：不占内存
   * 缺点：只能读取，不能增删改
   * 用于手机端，因为手机内存小。

### 1.4.1 解析器Jsoup

XML常见的解析器：

1. JAXP：sun公司提供的解析器，支持dom和sax两种思想。
2. DOM4J：一款优秀的解析器。
3. Jsoup：jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容。它提供了一套非常省力的API，可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。 
4. PULL：Android操作系统内置的解析器，sax方式。

接下来讲述一下Jsoup：

- `Demo01Jsoup.class` 获取字节码文件
- `Demo01Jsoup.class.getClassLoader()` 获取类加载器
- `Demo01Jsoup.class.getClassLoader().getResource("demo.xml")` 找到资源文件，返回路径
- `Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath` 路径的字符串表示形式

代码如下：

```java
public class Demo01Jsoup {
    public static void main(String[] args) throws IOException {
        /*
            Jsoup 解析器
            Demo01Jsoup.class 获取字节码文件
            Demo01Jsoup.class.getClassLoader() 获取类加载器
            Demo01Jsoup.class.getClassLoader().getResource("demo.xml") 找到资源文件，返回路径
            Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath 路径的字符串表示形式
         */
        // 获取demo.xml文档路径
        String path = Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
        // 解析XML文档，加载文档进内存，获取dom树。根据XML文档来获取Document对象
        Document document = Jsoup.parse(new File(path), "utf-8");
        // 现在已经获取dom树了，可以对XML进行CRUD了。
        // 获取name标签的内容，那么就要获取元素对象
        Elements elements = document.getElementsByTag("name");

        System.out.println(elements.size());
        // 获取第一个name的element对象
        Element element = elements.get(0);
        // 获取数据
        String name = element.text();
        System.out.println(name);
    }
}
```

Jsoup：工具类，可以解析HTML或者XML文档，返回Document

对于这个工具类，我们只需要学习parse方法。parse方法作用：解析HMTL文档或者XML文档，返回Document。

```java
public static Document parse(File file, @Nullable String charsetName) throws IOException {
    return DataUtil.load(file, charsetName, file.getAbsolutePath());
}
```

* `parse(File file, @Nullable String charsetName)`：解析XML指定字符串格式。

* `parse(String html)`：解析XML或者HTML字符串。

* `parse(URL url, int timeoutMillis)`：通过网络路径获取指定的HTML或者XML的文档对象`timeoutMillis`：超时时间

### 1.4.3 Document

Document：文档对象。代表内存中的dom树。

* `getElementById(String id)`：根据id属性值获取。

* `getElementsByTag(String tagName)`：根据标签名称获取元素对象集合。

  ```java
  public class Demo01Jsoup {
      public static void main(String[] args) throws IOException {
          // 获取demo.xml文档路径
          String path = Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
          // 解析XML文档，加载文档进内存，获取dom树。根据XML文档来获取Document对象
          Document document = Jsoup.parse(new File(path), "utf-8");
          // 现在已经获取dom树了，可以对XML进行CRUD了。
          // 获取元素对象了，获取所有的student对象
          Elements elements = document.getElementsByTag("user");
          System.out.println(elements);
      }
  }
  ```

* `getElementsByAttribute(String key)`：根据属性名称获取元素对象集合。

* `getElementsByAttributeValue(String key, String value)`：根据对应的属性名称和属性值获取元素对象集合。


### 1.4.4 Element

Elements：元素Element对象的集合。可以当做`ArrayList<Element>`来使用。Element：元素对象

获取元素对象

- `getElementById(String id)`：根据id属性值获取。

- `getElementsByTag(String tagName)`：根据标签名称获取元素对象集合。

- `getElementsByAttribute(String key)`：根据属性名称获取元素对象集合。

- `getElementsByAttributeValue(String key, String value)`：根据对应的属性名称和属性值获取元素对象集合。

  ```java
  public class Demo02Jsoup {
      public static void main(String[] args) throws IOException {
          // 获取dom树
          String path = Demo02Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
          Document document = Jsoup.parse(new File(path), "utf-8");
  
          // 通过Document对象获取name标签，获取所有的name标签，可以获取两个
          Elements elements = document.getElementsByTag("name");
          System.out.println(elements.size());
          // 通过Element对象获取子标签对象
          Element elementStudent = document.getElementsByTag("user").get(0);
          Elements elementName = elementStudent.getElementsByTag("name");
          System.out.println(elementName.size());
      }
  }
  ```

获取属性值

* `String attr(String key)`：根据属性名称获取属性值

  ```java
  public class Demo02Jsoup {
      public static void main(String[] args) throws IOException {
          // 获取dom树
          String path = Demo02Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
          Document document = Jsoup.parse(new File(path), "utf-8");
  
          // 通过Document对象获取name标签，获取第一个user标签
          Element elementUser = document.getElementsByTag("user").get(0);
          String id = elementUser.attr("id");
          System.out.println(id);
      }
  }
  ```

获取文本内容

* `String text()`：获取文本内容

* `String html()`：获取标签体的所有内容，包括子标签的字符串内容

  ```java
  public class Demo02Jsoup {
      public static void main(String[] args) throws IOException {
          // 获取dom树
          String path = Demo02Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
          Document document = Jsoup.parse(new File(path), "utf-8");
  
          // 通过Document对象获取name标签，获取第一个name标签
          Element elementName = document.getElementsByTag("name").get(0);
          // 将标签内部的"文本"以字符串打印出来
          String text = elementName.text();
          // 将标签内部的"标签以及文本"以字符串打印出来
          String html = elementName.html();
          System.out.println(text);
          System.out.println(html);
      }
  }
  ```

### 1.4.5 Node

Node：节点对象

Document和Element的父类。

### 1.4.6 快捷查询方式

一共有两种：selector选择器、XPath。

**selector**

```java
public class Demo02Jsoup {
    public static void main(String[] args) throws IOException {
        // 获取dom树
        String path = Demo02Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
        Document document = Jsoup.parse(new File(path), "utf-8");

        Elements elements = document.select("name");
        System.out.println(elements);
    }
}
```

有文档可以查看，查询规则到底是什么。

文档路径：`file:///E:/Jsoup/jsoup/jsoup-1.11.2-javadoc/overview-summary.html`，在`org.jsoup.nodes`包下面的`Element`类下面。找到`select`方法，点击`select`跳转到详情页面。点击查看更多`Selector`，就可以看到了。

**XPath**

XPath即为XML路径语言（XML Path Language），它是一种用来确定XML文档中某部分位置的语言。

Xpath是一个独立的东西，所以需要导包，在`E:\Jsoup\jsoup`里面，`JsoupXpath-0.3.2.jar`文件。

```java
public class Demo03Jsoup {
    public static void main(String[] args) throws IOException, XpathSyntaxErrorException {
        // 获取dom树
        String path = Demo03Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
        Document document = Jsoup.parse(new File(path), "utf-8");

        // 创建document对象，创建JXDocument对象
        JXDocument jxDocument = new JXDocument(document);
        // 结合XPath语法进行查询
        // 查询所有的user标签
        List<JXNode> jxNodes = jxDocument.selN("//user");
        for (JXNode jxNode : jxNodes) {
            System.out.println(jxNode);
        }
    }
}
```

`//user`这是特殊的规则，选取什么然后在里面输入什么就可以了。



# 第二章 Tomcat

`JavaEE`：`Java`语言在企业级开发中使用的技术规范的总和，一共规定了13项大的规范。

服务器：安装了服务器软件的计算机。

服务器软件：接受用户的请求，处理请求，做出响应。

web服务器软件：又叫做web容器，可以接受用户的请求，处理请求，做出响应。在web服务器软件当中，可以部署web项目，让用户通过浏览器来访问这些项目。

常见的`java`相关的`web`服务器软件：

* `webLogic`：`Oracle`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `webSphere`：`IBM`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `JBOSS`：`JBOSS`公司的，大型的`JavaEE`服务器，支持所有的`JavaEE`规范，收费。
* `Tomcat`：`Apache`基金组织的，中小型的`JavaEE`服务器，仅仅支持少量的`JavaEE`规范。当然，支持`servlet/jsp`。

`Tomcat`：一款`web`服务器软件。

## 2.1 Tomcat基础

安装方式：`https://tomcat.apache.org/`，直接解压缩即可，安装目录不要包含中文。

卸载直接删除安装目录即可。

Tomcat目录如下：

* `bin`：存放可执行文件。
* `conf`：存放配置文件。
* `lib`：存放依赖jar包。
* `logs`：存放日志文件。
* `temp`：存放临时文件。
* `webapps`：存放web项目。
* `work`：存放运行时的数据。

启动：找到Tomcat下载位置，`bin/startup.bat`，双击该文件。

访问：浏览器输入`http://localhost:8080`  也就是: `http://别人的ip:8080`    因为Tomcat的端口号就是8080

在启动过程中可能会遇到的两种问题：

1. 黑窗口一闪而过

   原因：没有正确的配置JAVA_HOME环境变量

2. 端口号占用了导致启动报错

   暴力解决：通过DOS命令来操作，打开cmd，键入netstat -ano，就会出现这时候使用端口号的软件，找到使用端口号为8080的软件。再打开任务管理器，杀死该软件进程。

   温柔解决：修改自身端口号。找到Tomcat下载位置，conf/server.xml，双击该文件。找到代码，将port = “8080”修改。

   ```xml
       <Connector port="8080" protocol="HTTP/1.1"
                  connectionTimeout="20000"
                  redirectPort="8443" />
   ```

   > 一般我们会将Tomcat的默认端口设置为80.80端口号是http协议的默认端口号，这样，访问的时候直接输入localhost就可以了，不用输入端口号了。

Tomcat关闭方式：

1. 正常关闭：`bin/shutdown.bat`或者`Ctrl+C`
2. 强制关闭：点击启动窗口的`×`

## 2.2 Tomcat配置

配置：部署项目，因为Tomcat就是一个web服务器软件，所以需要有项目在Tomcat上面部署。

* **直接将项目放到webapps目录下面就可以。**

  例如：在webapps目录下面创建一个hello文件夹，在hello目录下面创建hello.html文件。hello.html文件里面写一些静态资源，现在已经可以通过Tomcat来访问hello文件了。

  1. 启动Tomcat

  2. 打开浏览器，地址栏输入：`http://localhost:8080/hello/hello.html`

     /hello：项目的访问路径-->也叫做虚拟目录-->和项目名称一样

  这种有一个简化的部署方式，将项目打成一个war包，再将war包放置到webapps目录下面。然后war包会自动解压缩，如果删除的话，删除war包就可以了，项目也会自动删除。但是我弄不了。

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

  建议使用这种方式，假如不需要这个文件了，那么只需要在**index.xml后面加上_bak**，弄成临时文件，`index.xml_bak`。这样就无法读取了，所以也就废掉了。**即使服务器没有关掉，也会无法读取。**

## 2.4 项目介绍

项目有静态项目和动态项目。java动态项目的目录结构如下：

```apl
-- 项目的根目录
	-- WEB-INF目录：
		-- web.xml：web项目的核心配置文件
		-- classes目录：放置字节码文件的目录
		-- lib目录：放置依赖的jar包
```

# 第三章 Servlet入门

`Serverlet：server applet`

概念：运行在服务器端的小程序。Servlet就是一个接口，定义了Java类被浏览器访问到(tomcat识别)的规则。将来我们会自己定义一个类，实现Servlet接口，复写这些方法。

## 3.1 快速入门

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

执行原理如下：

1. 当服务器接受到客户端浏览器的请求后，会解析请求URL路径，获取访问的Servlet的资源路径。
2. 查找`web.xml`文件，看看是否有对应的`<url-pattern>`标签体内容。
3. 如果有，则在找到对应的`<servlet-class>`全类名。
4. Tomcat会将字节码文件加载进内存，并且创建其对象。
5. 调用方法。

## 3.2 方法声明周期

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

声明周期详解：

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

## 3.3 Servlet3.0

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

## 3.4 IDEA与Tomcat的相关配置

* IDEA会为每一个Tomcat部署的项目单独建立一份配置文件

  查看控制台的log，可以发现：

  ```apl
  Using CATALINA_BASE: 
  C:\Users\林轩\AppData\Local\JetBrains\IntelliJIdea2021.2\tomcat\3e20fe4c-cdaf-436f-8e70-1d6bfe2452e7
  ```

* 工作空间项目和Tomcat部署的web项目

  Tomcat真正访问的是“Tomcat部署的web项目”，“Tomcat部署的web项目”对应着“工作空间项目”的web目录下的所有资源

  WEB-INF目录下面的资源不能被浏览器直接访问。

* 断点调试：使用开始按钮旁边的“小虫子”启动debug。

## 3.5 Servlet的体系结构

`Servlet -- 接口` ——> `GenericServlet-- 抽象类` ——> `HttpServlet -- 抽象类`

### 3.5.1 GenericServlet

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

### 3.5.2 HttpServlet

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
    <!--action规定向何处提交表单 eclipse里面/不要加上-->
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

## 3.6 Servlet的相关配置

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

# 第四章 ServletContext对象

`Servlet`对象：代表了整个Web应用，可以和程序的容器(服务器)来通信。

功能：

1. 获取`MIME`类型。
2. 域对象：共享数据。
3. 获取文件的真实(服务器)路径。

## 4.1 获取ServletContext对象

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

需求如下：

1. 页面显示超链接
2. 点击超链接后弹出下载提示框
3. 完成图片文件下载

分析如下：

1. 超链接指向的资源如果能够被浏览器解析，那么就会在浏览器中显示。如果不能够解析，则会弹出下载提示框。

2. 我们要求任何资源都必须弹出下载提示框。可以使用响应头设置资源的打开方式：

   `content-disposition:attachment;filename=xxx`

**实现步骤**

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

**中文名称问题**

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