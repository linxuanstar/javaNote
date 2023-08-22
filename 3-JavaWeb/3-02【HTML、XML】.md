# 第一章 web概念描述

我们常用的软件和网站可以分为 C / S 架构和 B / S 架构，如下：

- C / S 结构：全称为 Clinet / Server 结构，是指客户端和服务器结构。常用程序有 QQ、迅雷等软件。
- B / S 结构：全程为 Browser / Server 结构，是指浏览器和服务器结构。常用的浏览器有谷歌，Edge，火狐等。

B/S 架构就是常说的 Web 项目。B/S 架构的资源有着静态资源和动态资源两种。

**静态资源**

使用静态开发技术发布的资源。所有用户访问，得到的结果是一样的。如：文本，图片，音频，视频，HTML，CSS，JavaScript。

如果用户请求的是静态资源，那么服务器会直接将静态资源发送给浏览器。浏览器中内置了静态资源的内置引擎，可以展示静态资源。

* HTML：用于搭建基础网页，展示页面的内容。
* CSS：用于美化页面，布局页面。
* JavaScript：控制页面的元素，让页面有一些动态的效果。

**动态资源**

使用动态网页及时发布的资源。所有用户访问，得到的结果可能不一样。如果用户请求的是动态资源，那么服务器就会执行动态资源，转换为静态资源，再发送给浏览器。如：jsp/servlet，php，asp...

# 第二章 HTML

HTML（Hyper Text Markup Language ）：超文本标记语言。
* 超文本：超文本是用超链接的方式，将各种不同空间的文字信息组织在一起的网状文本。
* 标记语言：它不是编程语言。有标签构成的语言。

HTML 不是一种编程语言，而是一种标记语言（markup language）。HTML 使用标记标签来描述网页，标记语言就是一套标记标签 / 元素（markup tag）。

```html
<!DOCTYPE html> <!-- 文档声明，必须写在网页的第一行 -->
<!-- html：是网页的根标签 -->
<html lang="zh">
<!-- head：表示网页中的元数据，在网页中看不见，这个是给浏览器解析的 -->
<head>
    <meta charset="UTF-8">
    <!-- title：是网页的标题，搜索引擎就会通过title来判断网页的主要内容。 -->
    <title>我的第一个网页</title>
</head>
<!-- body：是网页的主体，用户能看见的都写在body中 -->
<body>
    <h1>我的第一个网页</h1>
</body>
</html>
```

在 HTML 中除了成对出现的标签（有开始有结束），还有自结束标签（只有开始）。

```html
<!-- 自结束标签 -->
<img src="。。。" alt="。。">
```

标签中可以设置属性。在开始标签或自结束标签中设置属性，但是不能在结束标签中设置属性。属性是通过键值对设置的，`属性名=“属性值”`。属性和标签名或其它属性使用空格隔开。属性是通过官方文档规定书写的。有些属性有属性值，但是有些没有属性值，直接使用属性名。

## 2.1 块、行内元素

在 HTML 5 标准前，HTML 中的元素可以分为块级元素、行内元素（又称行级元素）和行内块级元素三种类型 ，它们的主要区别在于它们在文档流中所占据的空间和如何与其他元素相互作用。

**块级元素**

块级元素（block element）特点：

- 在默认情况下，会新起一行。
- 块级元素可以包含行内元素和其他块级元素。
- 可以设置宽度、高度、内边距、边框和外边距等盒模型属性。

常见的 HTML 块级元素包括： `<p>`、`<h1> - <h6>`、`<ul>、<ol>、<li>`、`<div>`、`<table>、<tr>、<td>`、`<hr>`、`<pre>`。由于块级元素会占据一整行，因此通常用于分隔和布局页面的不同区域，可以设置宽度和高度等盒模型属性，从而实现复杂的布局效果。

**行内元素**

行内标签，也叫内联标签。行内元素（inline element）特点：

- 默认情况下，不会以新行开始，其内容会在同一行上显示，直到到达该行的末尾才会换行显示。
- 一般情况下，行内元素只能包含数据和其他行内元素。
- 默认情况下，无法设置宽度和高度，除非将 display 设为除 inline 之外的某个值。

常见的 HTML 行内元素包括：`<a>`、`<span>`、`<b>、<i>、<u>、<strong>、<em>、<small>、<sup>、<sub>`、`<br>`、`<code>、<var>、<kbd>`、`<q>、<cite>、<blockquote>`。由于行内元素在默认情况下不会开始新行，因此通常用于文本内容和内联元素的排列和布局。但是行内元素无法设置宽度和高度等盒模型属性，其宽度和高度都是由它们所包含的内容决定的，因此不能实现像块级元素那样的复杂布局效果。

**行内块级元素**

行内块级元素（inline-block）特点：

- 行内块级元素与行内元素类似（即在默认情况下，不会以新行开始），
- 可以包含其他行内块级元素或行内元素。
- 宽度和高度是可以设置的，不需要将 display 设为除 inline 之外的某个值。

常见的 HTML 行内块级元素包括：`<img>`、`<button>`、`<input>`、`<select>`、`<textarea>`、`<label>`。通过将行内块级元素设置为 `display: inline-block;`，可以将其布局在一行中，并允许设置其宽度、高度、内边距和外边距等样式属性，从而实现更加灵活的布局效果。

## 2.2 meta标签

 meta写在head标签中，是给浏览器看的，是页面的元数据。必须有开始标签，不能有结束标签。

常见属性：

* charset属性：设置页面的编码字符集。
  charset属性值为各种字符集编码，例如utf-8、GBK...。
* name属性：指定数据的名称。
  name属性值：author（作者）、description（指定网站的描述相当于自我介绍）、keywords（页面的关键字，用于搜索引擎来搜索的，可以同时指定多个关键字，用逗号隔开）、generator（生产者）、revised（改进）、others
* content属性：指定数据的内容，和name属性关联。

## 2.3 语义化标签

HTML5中为了提高程序的可读性能，提供了一些标签。

**标题标签（块元素）**

 一共有6级标题h1、h2、h13、h4、h5、h6，一到六，一级比一级小。但是语义化标签注重看重标签的语义。搜索引擎一般也会查看h1来判断搜索内容，一般h1只有一个。

```html
<h1>我的<font color="red">第二个</font>网页</h1>
<h2>二级标题</h2>
<h3>三级标题</h3>
<h4>四级标题</h4>
<h5>五级标题</h5>
<h6>六级标题</h6>
```

**p标签：（块元素）**

 p标签就是段落标签，就是页面中的一个段落。

**hgroup标签**

它是用来给标题标签分组。

```html
<hgroup>
    <h1>我的<font color="red">第二个</font>网页</h1>
    <h2>二级标题</h2>
</hgroup>
```

**em标签：（行内元素）**

用于表示语音语调的加重，效果显示为斜体。

**strong标签：（行内元素）**

用于表示强调重要的内容，效果显示为加粗。

**blockquote标签：（块元素）**

表示一个引用别人说的话，效果为自动缩进。

**q标签：（行内元素）**

用于引用，一般用于短引用，效果自动加引号。

**br标签**

 一个自结束标签，用于换行。

## 2.4 结构化语义标签

1. header标签：表示网页的头部。
2. main标签：表示网页的主题（一个页面只有一个）。
3. footer标签：表示网页的页脚，网页最后面的部分。

4. nav标签：表示网页中的导航。

5. aside标签：和主体相关的其内容（侧边栏）。

6. article标签：表示一个独立的文章。

7. section标签：表示一个独立的标签，上面的标签不能使用的时候，就可以使用该标签。

## 2.5 列表

分为有序列表、无序列表、定义列表，它们都是块元素。

**有序列表**

```html
<ol>
    <li>结构</li>
    <li>表现</li>
    <li>行为</li>
</ol>
```

**无序列表**

```html
<ul>
    <li>结构</li>
    <li>表现</li>
    <li>行为</li>
</ul>
```

**定义列表**

使用**dl**标签创建一个定义列表，**dt**表示下定义的内容， **dd**表示对内容进行解释。这个可以用来做二级菜单。

```html
<!-- 定义列表-->
<dl>
    <dt>结构</dt>
    <dd>结构表示网页的结构</dd>
</dl>
```

## 2.6 超链接

## 2.7 表格



表格标签：

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

## 2.8 表单

概念：用于采集用户输入的数据的。用于和服务器进行交互。`form`：用于定义表单。可以定义一个范围，范围代表采集用户数据的范围。

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

**label**

指定输入项的文字描述信息，包裹住框前面的文字。

label的for属性一般会和input的id属性值想对应。如果对应，点击label标签包裹的文字，则光标聚集到框里面。

**input**

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

# 第三章 XML快速入门

XML（Extensible Markup Language）： 可扩展（标签都是自定义的）标记语言。XML功能是存储数据，可以作为配置文件，也可以在网络中传输。

XML与HTML的区别

1. XML的标签都是自定义的，HTML是预定义的。
2. XML的语法严格，HTML语法松散。
3. XML是存储数据的，HTML是展示数据的。

## 3.1 快速入门

标签是自定义的，规则如下：

* 名称可以包含字母、数字以及其他的字符
* 名称不能以数字或者标点符号开始
* 名称不能以字母XML开始
* 名称不能包含空格

```xml
<!-- xml文档后缀为.xml，另外第一行必须为文档声明，不饿能是空格、换行、注释。所以该注释也不能在第一行出现 -->
<?xml version="1.0"?>
<!-- xml文档中有且仅有一个根标签 -->
<users>
    <!-- 属性值必须使用引号(单引号，双引号都可以)引起来 -->
    <user id="1">
        <name>zhangsan</name>
        <!-- 严格区分大小写 -->
        <Name>zhangsan111</Name>
        <age>23</age>
        <gender>male</gender>
        <!-- 自闭和标签 -->
        <falg value="true"/>
    </user>

    <user id="2">	
        <name>lisi</name>
        <!-- 严格区分大小写 -->
        <Name>lisi111</Name>
        <age>24</age>
        <gender>male</gender>
        <!-- 自闭和标签 -->
        <falg value="false"/>
    </user>
</users>
```

```xml
<!-- mybatis-config.xml文件 -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--设置连接数据库的环境-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!-- MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver -->
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <!--打开MySql，创建一个数据库MyBatis-->
                <property name="url" value="jdbc:mysql://localhost:3306/MyBatis"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
</configuration>
```

```xml
<!-- UserDao.xml -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.linxuan.mybatis.mapper.UserMapper">
    <!--int insertUser();-->
    <insert id="insertUser">
        insert into tb_user values(null,'张三','123',23,'男', '12345@qq.com')
    </insert>
</mapper>
```

```xml
<!-- applicationContext.xml文件 -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- bean标签表示配置bean，id属性表示给bean起名字(ID唯一)，class属性表示给bean定义类型	-->
    <bean id="bookDao" class="com.linxuan.dao.impl.BookDaoImpl"/>
    <bean id="bookService" class="com.linxuan.service.impl.BookServiceImpl">
        <!-- 配置service与dao的关系 -->
        <!-- property配置当前bean的属性，name表示配置哪一个具体的属性，ref表示参照哪一个bean注入 -->
        <property name="bookDao" ref="bookDao"/>
    </bean>
</beans>
```

## 3.2 XML组成部分

```xml
<?xml version="1.0" encoding="gbk" standalone="yes"?>
```

属性列表

* version：版本号，必须的属性。
* encoding：编码方式。告知解析引擎当前文档使用的字符集，默认值为ISO-8859-1。
* standalone：是否独立。 yes代表不依赖其他文件，no代表依赖其他文件。

结合CSS指令：

```xml
<?xml-stylesheet type="text/css" href="a.css"?>
```

CDATA区：在该区域中的数据会被原样展示。格式：`<![CDATA[ 数据 ]]>`

## 3.3 约束XML文档

约束是规定XML文档的书写规则。约束文档都是框架编写者编写的，是我们程序员使用的。框架编写者编写一个框架后，不知道我们的XML都会定义什么标签，所以他需要限制我们定义标签的能力，所以编写了XML约束文档。

约束文档一共有两种：DTD 一种简单的约束技术、Schema 一种复杂的约束技术。

### 3.3.1 DTD

`demo.dtd`基本内容如下：

```xml-dtd
<!-- 
Element 元素的意思来定义标签
ATTLIST 来定义属性
这些和正则表达式一样 *代表出现0次或者多次 +代表出现一次或者多次 ?代表出现0次或者一次

#PCDATA 字符串
#REQUIRED 必须出现
-->
<!--声明了一个标签，名称为students，该标签里面放student子标签 *代表0次或者多次-->
<!-- *代表出现0次或者多次 +代表出现一次或者多次 ?代表出现0次或者一次。如果没有加，那么就代表只能出现一次-->
<!ELEMENT students (student*)>
<!--声明了一个student标签，里面能出现name, age, sex标签，并且顺序不能够出错 只能够出现一次-->
<!ELEMENT student (name, age, sex)>	
<!-- name标签内容为字符串 -->
<!ELEMENT name (#PCDATA)>
<!ELEMENT age (#PCDATA)>
<!ELEMENT sex (#PCDATA)>
<!-- 声明student标签有属性，属性名字为number，属性类型为ID，ID表示唯一，也就是该属性唯一 -->
<!-- 格式为：<!ATTLIST 元素名称 [属性名 属性类型 [约束] [缺省值]]+> -->
<!ATTLIST student number ID #REQUIRED>	
```

得到了DTD约束文档，那么我们就需要按照约束文档来定义XML配置文件，而第一步我们首先需要将DTD约束文档给导入到XML文档中，这样XML配置文件才知道有什么样的规则。

而DTD文档导入又有多种情况：内部dtd、外部dtd。

**内部dtd**

内部dtd：将约束规则定义在XML文档中。

```xml
<?xml version="1.0" encoding="utf-8" ?>
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

**外部dtd**

外部dtd：将约束的规则定义在外部的dtd文件中。

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

### 3.3.2 XML Schema

XSD (XML Schema Definition)是W3C于2001年5月发布的推荐标准，指出如何形式描述`XML`文档的元素。`XSD`是许多XML Schema 语言中的一支。

像所有XML Schema 语言一样，`XSD`用来描述一组规则──一个XML文件必须遵守这些规则，才能根据该`schema`合法。XML Schema以XML语言为基础，也可以说XML Schema自身就是XML的一种应用。`Schema`约束文档的后缀名称为：`xsd`。

```xml
<!-- student.xsd文件 -->
<?xml version="1.0"?>
<!-- 第一行xmlns:xsd：文件中用的元素和数据类型来自该命名空间。同时规定这些元素和数据类型应该使用前缀 xsd: -->
<!-- 第二行：显示在这个xsd文件中定义的标签来自哪一个命名空间，一般命名规则为：http://公司域名/xsd文件名 -->
<!-- 第三行：默认的命名规则，一般和targetNamespace相同。 -->
<!-- 第四行：任何 XML 实例文档所使用的且在此 schema 中声明过的元素必须被命名空间限定。 -->
<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            targetNamespace="http://www.springframework.org/schema/beans" 
            xmlns="http://www.springframework.org/schema/beans"
            elementFormDefault="qualified">

    <!-- 自定义标签，名称为students，类型为studentsType -->
    <xsd:element name="students" type="studentsType"/>
    <!-- complexType定义studentsType类型-->
    <xsd:complexType name="studentsType">
        <!-- sequence：规定标签中子元素的顺序。 -->
        <xsd:sequence>
            <!-- 按顺序出现student标签，类型为studentType，最少一次，最多不限量 -->
            <xsd:element name="student" type="studentType" minOccurs="0" maxOccurs="unbounded"/>
        </xsd:sequence>
    </xsd:complexType>
    <!-- 定义studentType类型 -->
    <xsd:complexType name="studentType">
        <!-- sequence：规定标签中子元素的顺序。 -->
        <xsd:sequence>
            <!-- name标签，类型为String类型 -->
            <xsd:element name="name" type="xsd:string"/>
            <xsd:element name="age" type="ageType"/>
            <xsd:element name="sex" type="sexType"/>
        </xsd:sequence>
        <!-- 规定具有number属性，类型为numberType， -->
        <xsd:attribute name="number" type="numberType" use="required"/>
    </xsd:complexType>
    <!-- 定义一个简单的类型，名称为sexType -->
    <xsd:simpleType name="sexType">
        <!-- restriction：定义一个约束条件。基本的数据类型为String -->
        <xsd:restriction base="xsd:string">
            <!-- 枚举类型，值为male和female -->
            <xsd:enumeration value="male"/>
            <xsd:enumeration value="female"/>
        </xsd:restriction>
    </xsd:simpleType>
    <!-- 规定age标签中数据类型为integer，最小值为0，最大值为256 -->
    <xsd:simpleType name="ageType">
        <xsd:restriction base="xsd:integer">
            <xsd:minInclusive value="0"/>
            <xsd:maxInclusive value="256"/>
        </xsd:restriction>
    </xsd:simpleType>
    <xsd:simpleType name="numberType">
        <xsd:restriction base="xsd:string">
            <!-- student标签number的值需要是linxuan+任意四位数字。使用的是正则表达式-->
            <xsd:pattern value="linxuan_\d{4}"/>
        </xsd:restriction>
    </xsd:simpleType>
</xsd:schema>
```

```xml
<?xml version="1.0"?>
<!-- 第一行xmlns：规定默认命名空间的声明。该文档中使用的元素被声明于该命名空间，和xsd中配置的xmln相对应。 -->
<!-- 第二行：表示遵守w3的xml schema规范，xml解析器解析xml文件时，就明白按照什么规范解析了。 -->
<!-- 第三、四行：由URI引用对（可以多个）组成，空白符分隔。分别为命名空间和xsd文件位置。 -->
<students xmlns="http://www.linxuan.com/xml"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.linxuan.com/xml student.xsd">

    <student number="linxuan_0001">
        <name>linxuan</name>
        <age>11</age>
        <sex>male</sex>
    </student>
    <student number="linxuan_0002">
        <name>chenmuyang</name>
        <age>12</age>
        <sex>male</sex>
    </student>
</students>
```

## 3.4 解析XML文档

解析XML文档：操作XML文档，将文档当中的数据读取到内存中。

操作XML文档有两种方式：

1. 解析(读取)：将文档中的数据读取到内存中。
2. 写入：将内存中的数据保存到XML文档中。持久化的存储。

解析XML文档也有两种方式：

1. DOM：将标记语言文档一次性加载进内存，在内存中形成一颗DOM树。这种方式操作方便，可以对文档进行CRUD的所有操作。但是占内存，通常由于电脑端。
2. SAX：逐行读取，基于事件驱动。不占内存，但是只能读取，不能增删改。通常用于手机端，因为手机内存小。

XML常见的解析器：

1. JAXP：sun公司提供的解析器，支持dom和sax两种思想。
2. DOM4J：一款优秀的解析器。
3. Jsoup：jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容。它提供了一套非常省力的API，可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。 
4. PULL：Android操作系统内置的解析器，sax方式。

### 3.4.1 解析器Jsoup

Jsoup：jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容。它提供了一套非常省力的API，可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。 

Jsoup：工具类，可以解析HTML或者XML文档，返回Document。对于这个工具类，我们只需要学习parse方法：解析HMTL文档或者XML文档，返回Document。

```java
public static Document parse(File file, @Nullable String charsetName) throws IOException {
    return DataUtil.load(file, charsetName, file.getAbsolutePath());
}
```

* `parse(File file, @Nullable String charsetName)`：解析XML指定字符串格式。

* `parse(String html)`：解析XML或者HTML字符串。

* `parse(URL url, int timeoutMillis)`：通过网络路径获取指定的HTML或者XML的文档对象`timeoutMillis`：超时时间

```xml
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.15.4</version>
</dependency>
```

代码如下：

```java
public class Demo01 {
    public static void main(String[] args) throws IOException {
        // 获取字节码文件，获取类加载器，获取资源文件路径，返回资源路径的字符串表示形式
        // 如果是Maven工程xml文件放到resources目录下面，这样会将编译后的该文件放到classes目录下面方便读取。
        String path = Demo01.class.getClassLoader().getResource("student.xml").getPath();
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

### 3.4.3 Document

Document：文档对象。代表内存中的dom树。

* `getElementById(String id)`：根据id属性值获取。
* `getElementsByTag(String tagName)`：根据标签名称获取元素对象集合。
* `getElementsByAttribute(String key)`：根据属性名称获取元素对象集合。
* `getElementsByAttributeValue(String key, String value)`：根据对应的属性名称和属性值获取元素对象集合。

```java
public class Demo01Jsoup {
    public static void main(String[] args) throws IOException {
        // 获取demo.xml文档路径
        String path=Demo01Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
        // 解析XML文档，加载文档进内存，获取dom树。根据XML文档来获取Document对象
        Document document=Jsoup.parse(new File(path), "utf-8");
        // 现在已经获取dom树了，可以对XML进行CRUD了。
        // 获取元素对象了，获取所有的student对象
        Elements elements=document.getElementsByTag("user");
        System.out.println(elements);
    }
}
```


### 3.4.4 Element

Elements：元素Element对象的集合。可以当做`ArrayList<Element>`来使用。Element：元素对象

**获取元素对象**

- `Document.getElementById(String id)`：根据id属性值获取。
- `Document.getElementsByTag(String tagName)`：根据标签名称获取元素对象集合。
- `Document.getElementsByAttribute(String key)`：根据属性名称获取元素对象集合。
- `Document.getElementsByAttributeValue(String key, String value)`：根据对应的属性名称和属性值获取元素对象集合。

```java
public class Demo02Jsoup {
    public static void main(String[] args) throws IOException {
        // 获取dom树
        String path=Demo02Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
        Document document=Jsoup.parse(new File(path), "utf-8");

        // 通过Document对象获取name标签，获取所有的name标签，可以获取两个
        Elements elements=document.getElementsByTag("name");
        System.out.println(elements.size());
        // 通过Element对象获取子标签对象
        Element elementStudent=document.getElementsByTag("user").get(0);
        Elements elementName=elementStudent.getElementsByTag("name");
        System.out.println(elementName.size());
    }
}
```

**获取属性值**

* `String attr(String key)`：根据属性名称获取属性值

```java
public class Demo02Jsoup {
    public static void main(String[] args) throws IOException {
        // 获取dom树
        String path=Demo02Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
        Document document=Jsoup.parse(new File(path), "utf-8");

        // 通过Document对象获取name标签，获取第一个user标签
        Element elementUser=document.getElementsByTag("user").get(0);
        String id=elementUser.attr("id");
        System.out.println(id);
    }
}
```

**获取文本内容**

* `String text()`：获取文本内容
* `String html()`：获取标签体的所有内容，包括子标签的字符串内容

```java
public class Demo02Jsoup {
    public static void main(String[] args) throws IOException {
        // 获取dom树
        String path=Demo02Jsoup.class.getClassLoader().getResource("demo.xml").getPath();
        Document document=Jsoup.parse(new File(path), "utf-8");

        // 通过Document对象获取name标签，获取第一个name标签
        Element elementName=document.getElementsByTag("name").get(0);
        // 将标签内部的"文本"以字符串打印出来
        String text=elementName.text();
        // 将标签内部的"标签以及文本"以字符串打印出来
        String html=elementName.html();
        System.out.println(text);
        System.out.println(html);
    }
}
```
