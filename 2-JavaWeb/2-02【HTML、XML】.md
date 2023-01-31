# 第一章 web概念描述

## 1.1 架构分类

- **C/S结构**：全称为Clinet/Server结构，是指客户端和服务器结构。常用程序有QQ、迅雷等软件。

![Clinet/Server结构](D:\Java\笔记\图片\day11【网络编程】\1.png)

- **B/S结构**：全程为Browser/Server结构，是指浏览器和服务器结构。常用的浏览器有谷歌，Edge，火狐等。

![Browser/Server结构](D:\Java\笔记\图片\day11【网络编程】\2.png)

## 1.2 B/S架构详解

<!--P571-->

### 资源分类

1. 静态资源：
   * 使用静态开发技术发布的资源。
   * 特点：
     * 所有用户访问，得到的结果是一样的。
     * 如：文本，图片，音频，视频，HTML，CSS，JavaScript
     * 如果用户请求的是静态资源，那么服务器会直接将静态资源发送给浏览器。浏览器中内置了静态资源的内置引擎，可以展示静态资源。
2. 动态资源：
   * 使用动态网页及时发布的资源。
   * 特点：
     * 所有用户访问，得到的结果可能不一样。
     * 如：jsp/servlet，php，asp...
     * 如果用户请求的是动态资源，那么服务器就会执行动态资源，转换为静态资源，再发送给浏览器。

* 想要学习动态资源，那么首先要学习静态资源

### 静态资源

* HTML：用于搭建基础网页，展示页面的内容。
* CSS：用于美化页面，布局页面。
* JavaScript：控制页面的元素，让页面有一些动态的效果。

# 第二章 HTML

概念：是最基础的网页开发语言

* Hyper Text Markup Language 超文本标记语言
  * 超文本：超文本是用超链接的方式，将各种不同空间的文字信息组织在一起的网状文本。
  * 标记语言：它不是编程语言。有标签构成的语言。

.......



1. 

2.  

3.  

4.  

5.  

6.  div和span：

   * div：每一个div占满一行。块级标签。
   * span：文本信息在一行展示。行内标签，也叫内联标签。

7. 语义化标签：HTML5中为了提高程序的可读性能，提供了一些标签。<header>：页眉   <footer>：页脚


   就是提供了一些特有的标签，没有任何的效果，可以与css相联系，然后使用。

8. 表格标签：

   * table，tr，th，td。
   * table：定义表格
     * width：宽度
     * border：边框
     * cellpading：定义内容和单元格之间的距离，如果设置为0，那么久没有距离，离得很近。
     * cellspacing：定义单元格之间距离，设置为0，那么单元格之间的线重合，就会合为一条。
     * bgcolor：背景色
     * align：对齐方式
   * tr：定义行
     * bgcolor：背景色
     * align：对齐方式
   * th：定义表头单元格
   * td：定义单元格
     * colspan：跨列
     * rowspan：跨行
     * row：一行
   * caption：表格标题
   * thead：表格的头部分
   * tbody：表格的体部分
   * tfoot：表格的脚部分



<!--P587-->

# 第三章 HTML标签

## 3.1 表单标签

<!--P588-->

概念：用于采集用户输入的数据的。用于和服务器进行交互。

`form`：用于定义表单。可以定义一个范围，范围代表采集用户数据的范围。

有两种属性：

* action：指定提交数据的URL
* method：指定提交方式。
  * method一共有7种值，其中有两种比较常用
    - get：
      1. 请求参数会在地址栏中显示。会封装到请求行中。
      2. 请求参数大小有限制。
      3. 不安全。
    - post：
      1. 请求餐宿不会在地址栏中显示。封装在请求体中。
      2. 请求参数大小没有限制。
      3. 安全。
  * 表单项中的数据要想要被提交：必须指定name的属性。

## 3.2 表单项标签

<!--P589-->

### label

指定输入项的文字描述信息，包裹住框前面的文字。

label的for属性一般会和input的id属性值想对应。如果对应，点击label标签包裹的文字，则光标聚集到框里面。

### input

可以通过type属性值，改变元素展示的样式

* type属性
  1. text值：文字输入框，输入文本信息
  2. password值：密码输入框，输入密码，输入后会转换为暗码
  3. radio值：单选框，但是选择区域的name属性的值要相同
  4. checkbox值：多选框
  5. submit值：提交框
* placeholder属性
  * 值可以是任意东西，会在框里面用灰色显示，输入东西，值消失。
* checked属性
  * 默认选择该框，可以在单选框和多选框里面使用
* value属性：
  * 值可以是任意东西，提交的时候会提交值





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

