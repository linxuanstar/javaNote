day11【XML】

<!--P660-->

# 第一章 XML快速入门

## 1.1 基本概念

<!--P661-->

XML：Extensible Markup Language 可扩展标记语言

可扩展：标签都是自定义的。

XML功能：存储数据

1. 配置文件
2. 在网络中传输

XML与HTML的区别

1. XML的标签都是自定义的，HTML是预定义的。
2. XML的语法严格，HTML语法松散。
3. XML是存储数据的，HTML是展示数据的。

## 1.2 快速入门

<!--P662-->

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

## 1.3 基本语法

1. xml文档后缀名称为：`.xml`
2. xml第一行必须定义为文档声明，不能是空格或者换行
3. xml文档中有且仅有一个根标签
4. 属性值必须使用引号(单引号，双引号都可以)引起来
5. 标签必须正确关闭，<user></user>或者<br/>
6. xml标签名称区分大小写

## 1.4 组成部分

<!--P663-->

**文档声明**

格式如下：

```xml
<?xml version = "1.0" encoding = "gbk" standalone = "yes"?>
```

属性列表

* version：版本号，必须的属性
* encoding：编码方式。告知解析引擎当前文档使用的字符集，默认值为ISO-8859-1
* standalone：是否独立    yes：不依赖其他文件     no：依赖其他文件

**指令**

结合CSS指令：

```xml
<?xml-stylesheet type = "text/css" href = "a.css"?>
```

**标签**

标签是自定义的，规则如下：

* 名称可以包含字母、数字以及其他的字符
* 名称不能以数字或者标点符号开始
* 名称不能以字母XML开始
* 名称不能包含空格

**属性**

id值唯一

**文本**

CDATA区：在该区域中的数据会被原样展示

格式：`<![CDATA[ 数据 ]]>`

# 第二章 约束

<!--P664 2.13-->

约束：规定XML文档的书写规则。约束文档都是框架编写者编写的，是我们程序员使用的。

框架编写者编写一个框架后，不知道我们的XML都会定义什么标签，所以他需要限制我们定义标签的能力，所以编写了XML约束文档。

约束文档一共有两种：

1. DTD：一种简单的约束技术
2. Schema：一种复杂的约束技术。

## 2.1 DTD

<!--P665-->

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

**导入DTD**

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

## 2.2 Schema

`Schema`，即`XML Schema`，`XSD (XML Schema Definition)`是W3C于2001年5月发布的推荐标准，指出如何形式描述`XML`文档的元素。`XSD`是许多`XML Schema` 语言中的一支。`XSD`是首先分离于`XML`本身的`schema`语言，故获取W3C的推荐地位。
像所有`XML Schema` 语言一样，`XSD`用来描述一组规则──一个XML文件必须遵守这些规则，才能根据该`schema`‘合法（Valid）’。

`Schema`约束文档的后缀名称为：`xsd`

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

# 第三章 解析

<!--P667 2.16-->

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

## 3.1 解析器

<!--P668-->

XML常见的解析器：

1. JAXP：sun公司提供的解析器，支持dom和sax两种思想。
2. DOM4J：一款优秀的解析器。
3. Jsoup：jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容。它提供了一套非常省力的API，可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。 
4. PULL：Android操作系统内置的解析器，sax方式。

## 3.2 Jsoup

<!--P669-->

> Jsoup 解析器
> Demo01Jsoup.class 获取字节码文件
> Demo01Jsoup.class.getClassLoader() 获取类加载器
> Demo01Jsoup.class.getClassLoader().getResource("demo.xml") 找到资源文件，返回路径
> Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath 路径的字符串表示形式

源代码如下：

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

<!--P670-->

Jsoup：工具类，可以解析HTML或者XML文档，返回Document

对于这个工具类，我们只需要学习parse方法，parse方法有很多重载，我们只需要学习三种即可。parse方法作用：解析HMTL文档或者XML文档，返回Document。

* ```java
  public class Demo01Jsoup {
      public static void main(String[] args) throws IOException {
          // 获取demo.xml文档路径
          String path = Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
          // 解析XML文档，加载文档进内存，获取dom树。根据XML文档来获取Document对象
          Document document = Jsoup.parse(new File(path), "utf-8");
          // 现在已经获取dom树了，可以对XML进行CRUD了。
          // 打印输出dom树
          System.out.println(document);
  
      }
  }
  ```

* `parse(String html)`：解析XML或者HTML字符串。

  ```java
  public class Demo01Jsoup {
      public static void main(String[] args) throws IOException {
          // 获取demo.xml文档路径
          String path = Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
          // 解析XML文档，加载文档进内存，获取dom树。根据XML文档来获取Document对象
          String str = "<?xml version = \"1.0\"?>\n" +
                  "<users>\n" +
                  "\n" +
                  "\t<user id = \"1\">\n" +
                  "\t\t<name>zhangsan</name>\n" +
                  "\t\t<age>23</age>\n" +
                  "\t\t<gender>male</gender>\n" +
                  "\t</user>\n" +
                  "\t\n" +
                  "\t<user id = \"2\">\t\n" +
                  "\t\t<name>lisi</name>\n" +
                  "\t\t<age>24</age>\n" +
                  "\t\t<gender>male</gender>\n" +
                  "\t</user>\n" +
                  "</users>";
          Document document = Jsoup.parse(str);
          // 现在已经获取dom树了，可以对XML进行CRUD了。
          // 打印输出dom树
          System.out.println(document);
      }
  }
  ```

* `parse(URL url, int timeoutMillis)`：通过网络路径获取指定的HTML或者XML的文档对象。

  `timeoutMillis`：超时时间

  ```java
  public class Demo01Jsoup {
      public static void main(String[] args) throws IOException {
          // 获取demo.xml文档路径
          String path = Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
          // 解析XML文档，加载文档进内存，获取dom树。根据XML文档来获取Document对象
          URL url = new URL("https://www.baidu.com/");
          Document document = Jsoup.parse(url, 1000);
          // 现在已经获取dom树了，可以对XML进行CRUD了。
          // 打印输出dom树
          System.out.println(document);
      }
  }
  ```

## 3.3 Document

Document：文档对象。代表内存中的dom树。

<!--P671-->

* getElementById(String id)：根据id属性值获取。

* getElementsByTag(String tagName)：根据标签名称获取元素对象集合。

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

* getElementsByAttribute(String key)：根据属性名称获取元素对象集合。

* getElementsByAttributeValue(String key, String value)：根据对应的属性名称和属性值获取元素对象集合。


## 3.4 Element

Elements：元素Element对象的集合。可以当做ArrayList<Element>来使用

Element：元素对象

<!--P672 2.17-->

### 获取元素对象

- getElementById(String id)：根据id属性值获取。

- getElementsByTag(String tagName)：根据标签名称获取元素对象集合。

- getElementsByAttribute(String key)：根据属性名称获取元素对象集合。

- getElementsByAttributeValue(String key, String value)：根据对应的属性名称和属性值获取元素对象集合。

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

### 获取属性值

* String attr(String key)：根据属性名称获取属性值

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

### 获取文本内容

* String text()：获取文本内容

* String html()：获取标签体的所有内容，包括子标签的字符串内容

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

## 3.5 Node

Node：节点对象

Document和Element的父类。

## 3.6 快捷查询方式

一共有两种：selector选择器、XPath。

**selector**

<!--P673-->

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

<!--P674-->

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