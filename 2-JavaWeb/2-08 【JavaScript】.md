# 第一章 JavaScript基础

<!-- 脚本语言：不需要编译，直接就可以被解析执行 -->

概念：一门客户端脚本语言

* 运行在客户端浏览器中。每一个浏览器都有JavaScript的解析引擎
* 脚本语言：不需要编译，直接就可以被浏览器解析执行了

功能：可以增强用户和HTML页面的交互过程，可以控制HTML元素，让页面有一些动态效果，增强用户体验。

## 1.1 JavaScript发展史

1. 1992年，Nobase公司，开发出第一门客户端脚本语言，专门用于表单的校验。命名为：C--。后来更名为ScriptEase。
2. 1995年，NetScape(网景)公司，开发了一门客户端脚本语言：LiveScript。后来，请来了SUN公司专家修改该语言，并更名为JavaScript。
3. 1996年，微软抄袭JavaScript开发了JScript语言。
4. 1997年，ECMA(欧洲计算机制造商协会)，指定了标准，所有的客户端脚本语言的标准：ECMAScript。

所以：JavaScript = ECMAScript + JavaScript自己特有的东西(BOM + DOM)

## 1.2 ECMAScript

与HTML结合方式有两种方式：内部JS和外部JS。

- 内部JS：定义`<script`>，标签内部就是JS代码

- 外部JS：定义`<script>`，通过src属性引入外部的JS文件


`<srcipt>`可以定义在HTML页面的任何地方。但是定义的位置会影响执行顺序，`<srcipt`>可以定义多个。

**注释**

ECMAScript注释老样子，有两种：单行注释`//`，多行注释`/* */`。

**数据类型**

一共有两种数据类型：原始数据类型、引用数据类型。

* 原始数据类型(其实就是基本数据类型)：
  * `number`：数字。 整数/小数/NaN(not a number 一个不是数字的数字类型)
  * `string`：字符串。  “abc” "a" 'acb'
  * `boolean`：true和false。
  * `null`：一个对象为空的占位符。
  * `undefined`：未定义。如果一个变量没有给初始化值，那么会被默认赋值为undefined。
* 引用数据类型：对象

**变量**

变量：一小块存储数据的内存空间

语言分为两种：强类型和弱类型

* 强类型：在开辟变量存储空间的时候，定义了空间将来存储的数据的数据类型。只能存储固定类型的数据。
* 弱类型：在开辟变量存储空间的时候，不定义空间将来的存储数据类型，可以存放任意类型的数据。

java是强类型语言，而JavaScript是弱类型的语言。

```javascript
var 变量名 = 值;
```

我们可以根据typeof运算符来得知变量类型。typeof运算符有一个参数，即要检查的变量或者值，变量是哪一种类型，就会返回哪一种值。

```javascript
var str = "Hello World";
alert (typeof str); // 输出string  
alert (typeof 43);  // 输出number
```

> 备注：typeof运算符对于null值会返回“Object”。这实际上是JavaScript最初实现中的一个错误，然后被ECMAScript沿用了。现在，null被认为是对象的占位符，从而解释了这一矛盾，但是从技术上来说，它仍然是原始值。

**运算符**

一元运算符：只有一个运算数的运算符。`++、--、+(正号)、-(负号)`

* `+(正号)、-(负号)`：在js中，如果运算数不是运算符所要求的类型，那么js引擎会自动的将运算数进行类型转换

  ```javascript
  var a = + "123";
  alert(typeof (a)); // 输出number
  document.write(typeof )
  document.write(a + 1); // 输出124
  ```

* string类型转number：按照字面值进行转换。如果字面值不是数字，那么转为NaN(不是数字的数字)。
* boolean类型转number：true转为1，false转为0。

算数运算符：`+、-、*、/、%`

* 由于JavaScript里面数据类型是number，所以在进行除法运算的时候，整数除整数不是整数，变成了小数。

  ```javascript
  var a = 3;
  var b = 4;
  document.write(a / b);
  ```

比较运算符

1. 类型相同：直接比较

   ```JavaScript
   // (3 > 4): false
   document.write("(3 > 4): " + (3 > 4) + "<br>");		
   // (" abc " > " acd "): false
   document.write("(&quot abc &quot > &quot acd &quot): " + ("abc" > "acd") + "<br>");		
   ```

   `&quot`实体名称`“`，如果用`”`会产生bug

   第二个表达式，字符串进行比较，会按照字典顺序进行比较，也就是ASCII码表。按位逐一进行比较，直到得出大小为止。

2. 类型不同：先进行类型转换，再进行比较

   ```javascript
   document.write("(&quot 123 &quot > 122): " + ("123" > 122) + "<br>");	
   // (" 123 " > 122): true
   
   document.write("(&quot 123 &quot == 123): " + ("123" == 123) + "<br>");
   // (" 123 " == 123): true
   
   document.write("(&quot 123 &quot === 123): " + ("123" === 123));
   // (" 123 " === 123): false
   ```

   字符串和数字进行比较，会先将字符串转为number类型，然后比较。`==`符号比较的是转换后的值，所以会得出true，`===`全等于。在比较之前，会先判断类型，如果类型不一样，会直接返回false。

逻辑运算符：`&&、||、！`

* `&&、||`有着短路的功能，如果使用`&&`，当左边是false的时候，右边就不会判断，结果直接为false。使用`||`，左边是true，同理右边不会判断，结果直接判定为true。
* `！`：非，其他类型可以转boolean，就是`!`运算符号不仅仅用在true和false上面，可以用在其他类型上面。作用在其他类型上面的时候，会进行类型转换。

 1. `number`：0或者NaN为假，其他为真。

    ```javascript
    var num1 = 0;
    var num2 = NaN;
    var num3 = 1;
    document.write(!num1 + "<br>"); 	// true
    document.write(!!num1 + "<br>"); 	// false
    document.write(!!num2 + "<br>");	// false
    document.write(!!num3 + "<br>");	// true
    ```

 2. `string`：除了空字符串("")，其他都是true。

    ```javascript
    var str1 = "";
    var str2 = "abc";
    document.write(!!str1 + "<br>");	// false
    document.write(!!str2 + "<br>");	// true
    ```

    应用场景：

    ```javascript
    var obj = "123";
    if (obj != null && obj.length > 0) {
    	alert(23);
    }
    // 两者等效
    if (obj) {
    	alert(123);
    }
    ```

 3. `null & undefined`：都是false。

    ```javascript
    var obj1 = NaN;
    var obj2;
    document.write(!!obj1 + "<br>");	// false
    document.write(!!obj2 + "<br>");	// false
    document.write("<hr>");
    ```

 4. 对象：都是true。

    ```javascript
    var date = new Date();
    document.write(!!date + "<br>");	// true
    ```

三元运算符：`? :`

```javascript
var num1 = 2;
var num2 = 3;
var num3 = num1 > num2 ? 1 : 0;

document.write(num3);				  // 0
```

## 1.3 JavaScript特殊语法

语句都是以`;`结尾的，但是如果一行只有一条语句，那么`;`可以省略(不建议)

```javascript
var num = 3
alert(num)
```

变量的定义使用var关键字，也可以不适用

1. 使用关键字：定义的变量是局部变量
2. 不使用关键字：定义的变量是全局变量(不建议)

## 1.4 流程控制语句

* `if...else...`

* `swith`

  JavaScript语句可以接受任意的原始数据类型

  ```javascript
  var a = 1;
  switch (a) {
  	case 1:
  		alert("number");
  		break;
  	case "abc":
  		alert("string");
  		break;
  	case true:
  		alert("boolean");
  		break;
  	case null:
  		alert("null");
  		break;
  	case undefined:
  		alert("undefined");
  		break;
  }
  ```

* `while`

  ```javascript
  // 1~100 求和5050
  var sum = 0;
  var num = 1;
  while (num <= 100) {
  	sum += num;
  	num++;
  }
  alert(sum);
  ```

* `do...while`

* `for`

  ```javascript
  // 1~100 求和5050
  var sum = 0;
  for (var i = 1; i <= 100; i++) {
      sum += i;
  }
  alert(sum);
  ```

## 2.4 练习

九九乘法表：

```javascript
document.write("<table align = 'center'>");

for (var i = 1; i < 10; i++) {
    document.write("<tr>");
    
    for (var j = 1; j < i + 1; j++) {
        document.write("<td>");
        document.write(i + "*" + j + "=" +(i * j) + "&nbsp&nbsp&nbsp");
        document.write("</td>");
    }
    document.write("</tr>");
}

document.write("</table>");
```

# 第二章 JavaScript基本对象

## 2.1 Function

Function：函数，方法

创建函数有三种方式：

1. `var fun = new Function(形式参数列表，方法体);`（忘记这种方法，有点像Java）

   ```javascript
   // 创建方式
   var fun1 = new Function("a", "b", "alert(a + b);");
   // 调用方法
   fun1(3, 4);
   ```

2. `function 方法名称(形式参数列表) {方法体}`

   ```javascript
   // 创建方式
   function fun2(a, b) {
       alert(a + b);
   }
   // 调用方法
   fun2(3, 4);
   ```

3. `var 方法名称 = function(形式参数列表) {方法体}`

   ```javascript
   // 创建方式
   var fun3 = function(a, b) {
       alert(a + b);
   }
   // 调用方法
   fun3(3, 4);
   ```

**属性**

* length：代表形参的个数

  ```javascript
  function fun(a, b) {
      // 方法体
  }
  alert(fun.length);	// 2,这是因为a, b一共两个参数
  ```

**特点**

1. 定义方法的时候，形参的类型不用写，返回值类型也不用写。这是因为都是var，所以直接忽略即可。

2. 方法是一个对象，如果定义名称相同的方法，后面的方法会覆盖掉前面的方法。

3. 在JavaScript中，方法的调用只与方法的名称有关，和参数列表无关。

   ```javascript
   function fun (a, b) {
       alert(a);
       alert(b);
   }
   fun(3, 4);		// 调用方法，输出3，4
   fun(3);			// 调用方法，输出3，undefined
   fun();			// 调用方法，输出undefined，undefined
   fun(1, 2, 3);	// 调用方法，输出1,2
   ```

4. 在方法声明当中，有一个隐藏的内置对象：数组 arguments。封装着所有的实际参数。

   ```javascript
   function fun () {
       document.write(arguments[0] + "<hr>");
       document.write(arguments[1]);
   }
   fun(1, 2);		// 调用函数，输出：1，2
   ```

   ```javascript
   // 求任意个数的和
   function add() {
       var sum = 0;
       for (var i = 0; i < arguments.length; i++) {
           sum += arguments[i];
       }
       return sum;
   }
   var sum = add(1, 2, 3);
   document.write(sum);
   ```

## 2.2 Array

Array：数组

**创建**

1. `var arr = new Array(元素列表);`

   ```javascript
   var arr1 = new Array(1, 2, 3);
   document.write(arr1);		// 1,2,3
   ```

2. `var arr = new Array(默认长度);`

   ```javascript
   var arr2 = new Array(4);
   document.write(arr2);		// ,,,
   
   var arr3 = new Array();
   document.write(arr);		// 空数组，什么都不打印
   ```

3. `var arr = [元素列表];`

   ```javascript
   var arr4 = [1, 2, 3, 4];
   document.write(arr4);		// 1,2,3,4
   ```

**方法**

* `join(参数)`：将数组中的元素按照指定的分隔符拼接为字符串，如果不写分隔符，默认为`,`。

  ```javascript
  var arr = [1, "abc", true];	
  document.write(arr.join("-") + "<br>");		// 1-abc-true
  document.write(arr.join() + "<br>");		// 1,abc,true
  ```

* `push(参数)`：向数组的末尾添加一个或者更多的元素，并返回新的长度。

  ```javascript
  var arr = [1, "abc", true];	
  document.write(arr.push(2) + "<br>");	// 4
  document.write(arr.join("-"));			// 1-abc-true-2
  ```

**属性**

* `length`：数组的长度

  ```javascript
  var arr = [1, "abc", true];
  document.write(arr.length + "<hr>");		// 3
  ```

**特点**

1. JavaScript中，数组元素的类型可变。

   ```javascript
   var arr = [1, "abc", true];
   document.write(arr + "<hr>");		// 1,abc,true
   ```

2. JavaScript中，数组元素可以改变。

## 2.3 Date

Date：日期对象

**创建**

* `var date = new Date();`

  ```javascript
  var date = new Date();
  document.write(date + "<hr>");
  ```

**方法**

* `toLocaleString()`：返回当前date对象对应的时间本地字符串格式。
* `getTime()`：获取毫秒值。返回单签日期对象描述事件到1970年1月1日毫秒值。

```javascript
var date = new Date();	
document.write(date.toLocaleString() + "<hr>");
document.write(date.getTime() + "<hr>");
```

## 2.4 Math

Math：数学对象

**创建**

* 不用创建，直接使用
* `Math.方法名称();`

**方法**

* `random()`：返回0~1之间随机数，包含0但是不包含1。

  ```javascript
  document.write(Math.random() + "<hr>");
  ```

* `ceil(参数)`：对参数向上取整。

  ```javascript
  document.write(Math.ceil(3.141592653589793) + "<hr>");
  ```

* `floor(参数)`：对参数向下取整。

  ```javascript
  document.write(Math.floor(3.141592653589793) + "<hr>");
  ```

* `round(参数)`：四舍五入。

  ```javascript
  document.write(Math.round(3.141592653589793) + "<hr>");
  ```

**属性**

* `PI`

  ```javascript
  document.write(Math.PI + "<hr>");		// 3.141592653589793
  ```

**随机数练习**

```javascript
// 1~100
var num = Math.round(Math.random() * 100);
document.write(num + "<hr>");
```

## 2.5 RegExp

RegExp：正则表达式对象

正则表达式：定义字符串的组成规则。

<!--不要使用空格，严格按照格式-->

1. 单个字符：`[ ]`

   例如：`[a]、[ab]、[a-z]、[a-zA-Z0-9_]`

   由于这样写很麻烦，所以有着特殊符号代表特殊含义的单个字符

   * `\d`：单个数字字符 `[0-9]`
   * `\w`：单个单词字符 `[a-zA-Z0-9_]`

2. 量词符号：

   * `?`：表示出现0次或者1次。
   * `*`：表示出现0次或者多次。
   * `+`：表示出现1次或者多次。
   * `{m, n}`：表示出现大于m次，小于n次。
     * 如果缺少m, `{, n}`：代表最多n次。
     * 如果缺少n, `{m, }`：代表最少m次。

3. 开始和结束符号：

   * `^`：开始符号
   * `$`：结束符号

**正则对象**

创建正则对象

1. `var reg = new RegExp("正则表达式");`

   ```javascript
   var reg = new RegExp("^\\w{6, 12}$");
   // 转义字符\，用两个\\代表一个\
   ```

2. `var reg = /正则表达式/;`

   ```javascript
   var reg2 = /^\w{6, 12}$/;
   ```

方法

`test(参数)`：验证指定的字符串是否符合正则定义的规范

```javascript
var reg2 = /^\w{6,12}$/;
var username = "zhengsa";
var flag = reg2.test(username);
alert(flag);		// true
```

## 2.6 Global

特点：Global中封装的方法不需要对象就可以直接进行调用。`方法名称();`

Global方法：

- `encodeURI()`：URL编码
- `decodeURI()`：URL解码
- `encodeURIComponent()`：URL编码，编码字符更多
- `decodeURIComponent()`：URL解码，解码字符更多

在浏览器中搜索的话，输入`林炫`，可以发现地址栏会是：`%E6%9E%97%E7%82%AB`，这是因为编码导致的问题。

使用UTF-8编码，一个中文会占3个字节，1个字节8个2进制位。`1010 1011`，这是1个字节，8个2进制位，转换成16进制，`AB`，然后再在其前面自动加上`%`，所以会是：`%AB`。

```javascript
// 编码与解码
var encode1 = encodeURI("林炫");
document.write(encode1 + "<br>");	// %E6%9E%97%E7%82%AB
var decode1 = decodeURI(encode1);
document.write(decode1 + "<br>");	// 林炫

// 比试两种编码
var encode2 = encodeURI("https://www.baidu.com?wd=林炫");
document.write(encode2 + "<br>");	// https://www.baidu.com?wd=%E6%9E%97%E7%82%AB

var encode3 = encodeURIComponent("https://www.baidu.com?wd=林炫");
document.write(encode3 + "<br>");	// https%3A%2F%2Fwww.baidu.com%3Fwd%3D%E6%9E%97%E7%82%AB
```

`parseInt()`：将字符串转换为数字。这个方法会逐一判断每一个字符串是否为数字，直到不是数字为止，前面数字部分会转为number。它与`+`不一样，`+`的话会直接`NaN`。

```javascript
var str = "123abc";
var num = parseInt(str);
document.write(num + "&nbsp->&nbsp" + typeof(num));	// 123 -> number
document.write("<br>");
document.write(+str);	// NaN
```

`isNaN()`：判断一个值是否是NaN。NaN有点特殊，NaN参与的==比较全部为false。所以判断一个值是否是NaN用这个方法。

```javascript
var a = NaN;
document.write(a == NaN);		// false
document.write("<br>");
document.write(isNaN(a));		// true
```

`eval()`：将JavaScript字符串转为脚本代码执行。

```javascript
var str = "alert(123)";
eval(str);
```

# 第三章 BOM浏览器对象模型

概念：`Browser Object Model` 浏览器对象模型。将浏览器的各个组成部分封装为对象。

* `Window`：窗口对象
* `Navigator`：浏览器对象
* `Screen`：显示器屏幕对象
* `History`：历史记录对象
* `Location`：地址栏对象

![](..\图片\2-08【JavaScript】\1.png)

我们这里只简单学习一下Window和Location

## 3.1 Window

Window对象不用创建，可以直接使用。使用的时候：`window.方法名称();`。window引用也可以省略。可以简写为：`方法名称();`

### 3.1.1 常用方法

**与弹出方框有关的方法：**

1. `alert()`：显示带有一段消息和一个确认按钮的警告框。

   ```JavaScript
   alert("林炫你好");		// 林炫你好
   ```

2. `confirm()`：显示带有一段消息以及确认按钮和取消按钮的对话框。如果用户点击确定按钮，则该方法返回true；如果用户点击取消按钮，则该方法返回false。

   ```JavaScript
   var flag = confirm("请问您确定退出吗？");
   if (flag) {
       document.write("欢迎下次光临");
   } else {
       document.write("好的呢~");
   }
   ```

3. `prompt()`：显示可以提示用户输入的对话框。该方法的返回值就是用户输入的内容。

   ```JavaScript
   var username = prompt("请输入用户名称：");		// 加入用户输入“林炫”
   document.write(username);					// 那么这里输出“林炫”
   ```

**与开关窗口有关的方法：**

1. `open()`：打开一个新的浏览器窗口。参数会输入到地址栏里面，所以输入百度的官网会打开百度的页面。返回值是一个新的window对象。

2. `close()`：关闭调用对象的窗口。谁调用close方法，关闭谁。

   ```html
   // 设置两个按钮
   <input id = "openBtn" type = "button" value = "打开新页面">
   <input id = "closeBtn" type = "button" value = "关闭新页面">
   ```

   ```javascript
   <script>
   	// 让newWindow成为全局对象。
   	var newWindow;
   	
   	// 打开新的页面
   	// 绑定事件
   	var openBtn = document.getElementById("openBtn");
   	// 事件被点击的时候打开百度官网，返回新打开页面的对象。
   	openBtn.onclick = function fun() {
   		newWindow = open("https://www.baidu.com");
   	}
   	
       // 关闭新打开的页面
   	var closeBtn = document.getElementById("closeBtn");
   	closeBtn.onclick = function fun() {
   		// 等价于newWindow.close();
   		newWindow.window.close();
   	}
   </script>
   ```

**与定时器有关的方法：**

1. `setTimeout()`：在指定的毫秒数后调用函数或者计算表达式。参数：要执行的代码/JavaScript 函数

   , 等待的毫秒数。返回值是一个编号。

2. `clearTimeout()`：取消由setTimeout()方法设置的timeout。

3. `setInterval()`：循环来调用函数或者计算表达式。

4. `clearInterval()`：取消由setInterval()方法设置的timeout。

   ```javascript
   // setTimeout("fun();", 1000); 经常使用下面的方法
   var id = setTimeout(fun, 1000);
   document.write(id);
   clearTimeout(id);
   function fun() {
       alert("boom~");
   }
   ```

### 3.1.2 常用属性

1. 获取其他BOM对象：`history`、`Location`、`Navigator`、`Screen`。

   ```JavaScript
// var hs1 = window.history; 等价于下面代码
   var hs2 = history;
alert(hs1);
   ```

2. 获取DOM对象：`document`。

   ```javascript
   // var image = window.document.getElementById("image");
   var image = document.getElementById("image");
   alert(image);
   ```

### 3.1.3 轮播图案例

```HTML
<img id = "image" src = "img/1.jpg" alt = "img" >
```

```JavaScript
var i = 2;

// 定义函数
function fun () {
    i++;
    if (i == 5) {
        i = 1;
    }
    // 获取图片对象
    var image = document.getElementById("image");
    // 更改图片
    image.src = "img/" + i + ".jpg";

}
// 1秒循环更换图片，并返回参数，方便取消轮播图
var id = setInterval(fun, 1000);

// 取消轮播图
// clearInterval(id);
```

## 3.2 Location

创建对象方法如下：使用`window.location`或者直接使用`location`。

常用方法为是`reload()`，重新加载当前文档。刷新。

```javascript
// 获取按钮
var btn = document.getElementById("btn");
// 绑定事件
btn.onclick = function () {
    // 刷新页面
    location.reload();
}
```

常用属性有`href`，设置或者返回完整的URL。

```HTML
<input id = "btn2" type = "button"  value = "百度">
```

```html
<script>
	// 获取按钮
	var btn2 = document.getElementById("btn2");
	// 绑定事件
	btn2.onclick = function () {
		// 设置跳转页面
		location.href = ("https://www.baidu.com");
	}
	// 返回当前URL
	alert(location.href); //file:///C:/Users/%E6%9E%97%E8%BD%A9/Desktop/demo.html
</script>
```

下面来看一个案例：**自动跳转首页案例**

```HTML
<p>
    <span id = "timeout">5&nbsp</span>秒就要跳转了~
</p>
```

```css
p {
    text-align: center;
}
span {
    color: red;
}
```

```javascript
var i = 4;

function timeout() {
    // 判断是否为0，为0跳转到新的页面
    while (i == 0) {
        location.href = "https://www.baidu.com";
    }
    // 获取span标签内元素对象，并修改
    var timeout = document.getElementById("timeout");
    timeout.innerHTML = i-- + " ";
}

setInterval(timeout, 1000);
```

# 第四章 DOM文档对象模型

概念：`Document Object Model` 文档对象模型。将标记语言文档的各个组成部分封装为对象，可以使用这些对象对标记语言文档进行CRUD的动态操作。

W3C，万维网联盟将DOM标准分为了3个不同的部分：

1. 核心 DOM——针对任何结构化文档的标准模型。
2. XML DOM——针对XML文档的标准模型。
3. HTML DOM——针对HTML文档的标准模型。

核心DOM有：

1. `Document`：文档对象。
2. `Element`：元素对象。
3. `Attribute`：属性对象。
4. `Text`：文本对象。
5. `Comment`：注释对象。

Node：节点对象，上述5个的父对象。

## 4.1 核心DOM模型

### 4.1.1 Document文档对象

创建(获取)该对象有两种方法：可以使用`window.document`获取，也可以直接`document`来获取，window可以省略。

方法如下：

* 获取Element对象：

  1. `getElementByID()`：根据id属性值获取元素对象。id属性值一般唯一。
  2. `getElementsByTagName()`：根据元素名称获取元素对象们。返回值是一个数组。
  3. `getElementsByClassName()`：根据Class属性值获取元素对象们。返回值是一个数组。
  4. `getElementsByName()`：根据name属性值获取元素对象们。返回值是一个数组。

  ```JavaScript
  // 根据元素名称获取元素对象们。返回值是一个数组
  var divs = document.getElementsByTagName("div");
  document.write("<hr>" + divs.length + "<hr>");
  ```

* 创建其他DOM对象：

  1. `createAttribute(name);`
  2. `createComment();`
  3. `createElement();`
  4. `createTextNode();`

  ```javascript
  var table = document.createElement("table");
  alert(table);
  ```

### 4.1.2 Element元素对象

获取(创建)该对象方法是：通过document来获取和创建。

Element方法：

1. `removeAttribute()`：删除属性

2. `setAttribute()`：设置属性

   ```HTML
   <a>点击跳转百度</a>
   <input id = "btn1" type = "button" value = "设置属性">
   <input id = "btn2" type = "button" value = "取消属性">
   ```

   ```JavaScript
   // 获取btn1
   var set_arr = document.getElementById("btn1");
   // 绑定事件
   set_arr.onclick = function() {
       // 获取a标签，因为是数组，所以取0号元素
       var element_a = document.getElementsByTagName("a")[0];
       element_a.setAttribute("href", "http://www.baidu.com");
   }
   
   var remove_arr = document.getElementById("btn2");
   remove_arr.onclick = function() {
       var element_a = document.getElementsByTagName("a")[0];
       element_a.removeAttribute("href");
   }
   ```

### 4.1.3 Node节点对象

Node：节点对象，其他5个的父对象。所有的dom对象都可以被认为是一个节点。

Node方法：

* CRUD dom树：

  1. `appendChild()`：向节点的子节点列表的结尾添加新的子节点。
  2. `removeChild()`：删除并返回当前节点的指定子节点。
  3. `replaceChild()`：用新节点替换一个子节点。

  ```HTML
  <div id = "div1">
      <div id = "div2">div2</div>
      div1
  </div>
  <a href = "javascript: void(0);" id = "del">删除子节点</a>
  <a href = "javascript: void(0);" id = "add">添加子节点</a>
  ```

  ```css
  body {
      background: azure;
  
  }
  
  div {
      border: 1px solid red;
  }
  
  #div1 {
      height: 200px;
      width: 200px;
  }
  
  #div2 {
      height: 100px;
      width: 100px;
  }
  
  #div3 {
      height: 100px;
      width: 100px;
  }
  ```

  ```JavaScript
  // 目的：点击a标签，删除div2
  // 获取a标签
  var a_del = document.getElementById("del");
  a_del.onclick = function() {
      var div1 = document.getElementById("div1");
      div1.removeChild(div2);
  }
  
  
  var a_add = document.getElementById("add");
  a_add.onclick = function() {
      var div1 = document.getElementById("div1");
      // 给div1添加子节点
      // 创建div3节点
      var div3 = document.createElement("div");
      div3.setAttribute("id", "div3");
      // 添加div3节点
      div1.appendChild(div3);
  }
  
  // 属性调用
  var div2 = document.getElementById("div2");
  var div1 = div2.parentNode;
  document.write("<br>" + div1);
  ```

* 属性：parentNode 返回节点的父节点。

  ```JavaScript
  // 属性调用
  var div2 = document.getElementById("div2");
  var div1 = div2.parentNode;
  document.write("<br>" + div1);
  ```

## 4.3 动态表格案例

**基础样式**

```HTML
<div>
	<input id = "id" type = "text" placeholder = "请输入编号">
	<input id = "name" type = "text" placeholder = "请输入姓名">
	<input id = "gender" type = "text" placeholder = "请输入性别">
	<input id = "btn_add" type = "button" value = "添加" >
</div>

<table>
	<caption>学生信息表</caption>
	<tr>
		<th>编号</th>
		<th>姓名</th>
		<th>性别</th>
		<th>操作</th>
	</tr>
	
	<tr>
		<td>1</td>
		<td>令狐冲</td>
		<td>男</td>
		<td><a href = "javascript: void(0);" onclick = "delTr(this)">删除</a></td>
	</tr>
	
	<tr>
		<td>2</td>
		<td>任我行</td>
		<td>男</td>
		<td><a href = "javascript: void(0);" onclick = "delTr(this)">删除</a></td>
	</tr>
	
	<tr>
		<td>3</td>
		<td>岳不群</td>
		<td>？</td>
		<td><a href = "javascript: void(0);" onclick = "delTr(this)">删除</a></td>
	</tr>
</table>
```

```css
<style>
table {
    border: 1px solid;
    margin: auto;
    width: 500px;
}

td, th {
    text-align: center;
    border: 1px solid;
}

div {
    text-align: center;
    margin: 50px;
}
</style>
```

**添加操作**

第一种操作：

```javascript
// 点击添加按钮，方法实现
document.getElementById("btn_add").onclick = function() {

    // 获取用户输入的内容
    var id = document.getElementById("id").value;
    var name = document.getElementById("name").value;
    var gender = document.getElementById("gender").value;

    // 创建td
    // id一栏td
    var td_id = document.createElement("td");
    var text_id = document.createTextNode(id);
    td_id.appendChild(text_id);
    // name一栏td
    var td_name = document.createElement("td");
    var text_name = document.createTextNode(name);
    td_name.appendChild(text_name);
    // gender一栏td
    var td_gender = document.createElement("td");
    var text_gender = document.createTextNode(gender);
    td_gender.appendChild(text_gender);
    // 操作一栏
    var td_a = document.createElement("td");
    var ele_a = document.createElement("a");
    ele_a.setAttribute("href", "javascript: void(0);");
    ele_a.setAttribute("onclick", "delTr(this);");
    var text_a = document.createTextNode("删除");
    ele_a.appendChild(text_a);
    td_a.appendChild(ele_a);

    // 创建tr
    var tr = document.createElement("tr");
    // 添加td到tr中
    tr.appendChild(td_id);
    tr.appendChild(td_name);
    tr.appendChild(td_gender);
    tr.appendChild(td_a);
    // 获取table
    var table = document.getElementsByTagName("table")[0];
    table.appendChild(tr);

}
```

第二种操作：

```JavaScript
// 使用innerHTML方法来操作
// 点击添加按钮，方法实现
document.getElementById("btn_add").onclick = function() {

    // 获取用户输入的内容
    var id = document.getElementById("id").value;
    var name = document.getElementById("name").value;
    var gender = document.getElementById("gender").value;

    // 获取table
    var table = document.getElementsByTagName("table")[0];

    // 追加一行
    table.innerHTML += "<tr>" +
        "<td>" + id + "</td>" +
        "<td>" + name + "</td>" +
        "<td>" + gender + "</td>" +
        "<td><a href = 'javascript: void(0)' onclick = 'delTr(this)'>删除</a></td>" +
        "</tr>";
}
```

**删除操作**

```JavaScript
// 使用a标签onclick属性，设置为delTr(this)参数，this返回当前对象。
// 删除操作
function delTr(obj) {
    // 根据返回的this对象，找到table对象和tr对象。
    var table = obj.parentNode.parentNode.parentNode;
    var tr = obj.parentNode.parentNode;

    table.removeChild(tr);
}
```

## 4.4 HTML DOM模型

HTML DOM——针对HTML文档的标准模型。

### 4.4.1 innerTHML

innerHTML：标签体的设置和获取。就是获取该标签体的内容，也可以修改该标签体的内容。

```html 
<div id = "div1">
    div
</div>
```

```javascript
var div = document.getElementById("div1");
var innerHTML = div.innerHTML;
document.write("<hr>" + innerHTML + "<hr>");

// 将div替换为文本框
// div.innerHTML = "<input type = 'text'>";

// 在div后面追加一个文本框
// div.innerHTML += "<input type = 'text'>";
```

### 4.4.2 控制样式

**使用元素的style属性**

```HTML
<div id = "div1">div</div>
```

```JavaScript
var div = document.getElementById("div1");
div.onclick = function() {
    div.style.border = "1px solid red";

    div.style.width = "200px";
    // font-size --> fontSize
    div.style.fontSize = "20px";
}
```

**通过className属性**

```css
.div {
    border: 1px solid red;
    width: 100px;
    height: 100px;
}
```

```javascript
// 提前定义好类选择器的样式，通过元素的className属性来设置class属性值。
var div = document.getElementById("div1");
div.onclick = function() {
    div.className = "div"
}
```

# 第五章 事件监听机制

时间监听机制概念：某些组件被执行了某些操作后，触发某些代码的执行。

- 事件：某些操作。例如：单击、双击、键盘按下了、鼠标移动了等。

- 事件源：组件。例如：按钮、文本输入框等。

- 监听器：代码。

- 注册监听：将事件、事件源和监听器结合在一起。当事件源上面发生了某个事件，则触发执行某个监听器打码。

## 5.1 简单入门

功能：某些组件被执行了某些操作之后，触发某些代码的执行。

绑定事件一共有两种方法：

1. 直接在html标签上面，指定事件的属性(操作)，属性值就是JavaScript代码。但是这种方式的耦合性太高了，所以不建议使用这种方式，可以使用第二种方式。

   ```HTML
   <!--事件：onlick单击事件--> 
   <img src="img/light.gif" onclick="alert('点击了');">
   ```

   当然也可以使用下面的操作

   ```HTML
   <img src="img/light.gif" onclick="fun();">
   	
   <script>
   	function fun() {
   		alert("点击了");
   	}
   </script>
   ```

2. 通过JavaScript获取元素对象

   ```HTML
   <img id="light" src="img/light.gif">
   ```

   ```html
   <script>
   	// 创建方法
   	function fun() {
   		alert("点击了");
   	}
   	
   	// 获取light对象
   	var light = document.getElementById("light");
   	// 绑定事件
   	light.onclick = fun;
   </script>
   ```

## 5.2 常见的事件

**点击事件**

* `onclick`：单击事件。
* `ondblclick`：双击事件。

**焦点事件**

* `onblur`：失去焦点。

* `onfocus`：元素获得焦点。

  ```JavaScript
  window.onload = function() {
      document.getElementById("username").onblur = function() {
          alert("失去焦点了");
      }
  }
  ```

**加载事件**

`onload`：一张页面或者一幅图像完成加载。

```JavaScript
// 通常是用这个事件包裹其他事件，当页面加载完毕之后，等其他事件触发，触发代码执行
window.onload = function() {
    // 事件
}
```

**鼠标事件**

* `onmousedown`：鼠标按钮被按下。

* `onmouseup`：鼠标按键被松开。

* `onmousemove`：鼠标被移动。

* `onmouseover`：鼠标移动到某元素上面。

* `onmouseout`：鼠标从某元素移开。

  ```JavaScript
  window.onload = function() {
      document.getElementById("username").onmousedown = function(event) {
          // alert("鼠标来了");
          // 左键点击文本框，显示0。齿轮点击显示1。右键点击显示2。
          alert(event.button);
      }
  }
  ```

**键盘事件**

* `onkeydown`：某个键盘按键被按下。

* `onkeyup`：某个键盘按键被松开。

* `onkeypress`：某个键盘按键被松开并按下。

  ```JavaScript
  window.onload = function() {
      document.getElementById("username").onkeydown = function(event) {
          // 键盘敲回车键
          if (event.keyCode == 13) {
              alert("提交表单");
          }
      }
  }
  ```

**选中和改变**

* `onselect`：文本被选中。

* `onchange`：域的内容被改变。

  常用于下拉列表

  ```HTML
  <select id = "city">
      <option>--请选择--</option>
      <option>北京</option>
      <option>上海</option>
      <option>广州</option>
      <option>深圳</option>
  </select>
  ```

  ```JavaScript
  window.onload = function() {
      document.getElementById("city").onchange = function(event) {
          alert("改变了");
      }
  }
  ```

**表单事件**

* `onsubmit`：确认按钮被点击。

* `onreset`：重置按钮被点击。

  第一种方式：

  ```HTML
  <form action = "#" id = "form">
  	<input type = "text" id = "username">
  	<input type = "submit" value = "提交">
  </form>	
  ```

  ```JavaScript
  window.onload = function() {
      document.getElementById("form").onsubmit = function() {
          // 校验用户名称输入是否正确。这里我们不进行校验，假设已经校验完毕
          // 用户点击提交，返回false，不提交。
          var flag = false;
          return flag;
      }
  }
  ```

  第二种方式：

  ```HTML
  <!--
  	return inputForm();
  	只有inputForm()的话：onclick = false;我们并没有将其返回，只是有一个false的值，我们并没有返回，所以需要返回这个false值。
  -->
  <form action = "#" id = "form" onclick = " return inputForm();">
  	<input type = "text" id = "username">
  	<input type = "submit" value = "提交">
  </form>	
  ```

  ```JavaScript
  function inputForm() {
      return false;
  }
  ```

## 5.3 表格全选案例

```HTML
<table>
	<caption>学生信息表</caption>
	<tr>
		<th><input type = "checkbox" name = "cb" id = "cb1"></th>
		<th>编号</th>
		<th>姓名</th>
		<th>性别</th>
		<th>操作</th>
	</tr>
	
	<tr>
		<td><input type = "checkbox" name = "cb"></td>
		<td>1</td>
		<td>令狐冲</td>
		<td>男</td>
		<td><a href = "javascript: void(0);">删除</a></td>
	</tr>
	
	<tr>
		<td><input type = "checkbox" name = "cb"></td>
		<td>2</td>
		<td>任我行</td>
		<td>男</td>
		<td><a href = "javascript: void(0);">删除</a></td>
	</tr>
	
	<tr>
		<td><input type = "checkbox" name = "cb"></td>
		<td>3</td>
		<td>岳不群</td>
		<td>？</td>
		<td><a href = "javascript: void(0);">删除</a></td>
	</tr>
</table>
<div>
	<input type = "button" id = "selectAll" value = "全选">
	<input type = "button" id = "unSelectAll" value = "全不选">
	<input type = "button" id = "selectRev" value = "反选">
</div>
```

```css
	<style>
		body {
			background-color: azure;
		}
		
		table {
			border: 1px solid;
			margin: auto;
			width: 500px;
		}

		td, th {
			text-align: center;
			border: 1px solid;
		}
		
		div {
			margin-top: 10px;
			text-align: center;
		}
		
		.over {
			background-color: pink;
		}
		
		.out {
			background-color: out;
		}
		
	</style>
```

```javascript
// 页面加载完成之后绑定事件
window.onload = function() {
    // 给全选按钮绑定单击事件
    document.getElementById("selectAll").onclick = function() {
        // 全选
        // 获取所有的checkbox
        var cbs = document.getElementsByName("cb");
        // 遍历
        for (var i = 0; i < cbs.length; i++) {
            // 设置每一个cb的状态为选中
            cbs[i].checked = true;
        }
    }

    document.getElementById("unSelectAll").onclick = function() {
        // 全不选
        // 获取所有的checkbox
        var cbs = document.getElementsByName("cb");
        // 遍历
        for (var i = 0; i < cbs.length; i++) {
            // 设置每一个cb的状态为不选中
            cbs[i].checked = false;
        }
    }


    document.getElementById("selectRev").onclick = function() {
        // 反选
        // 获取所有的checkbox
        var cbs = document.getElementsByName("cb");
        // 遍历
        for (var i = 0; i < cbs.length; i++) {

            cbs[i].checked = !cbs[i].checked;
        }
    }

    document.getElementById("cb1").onclick = function() {
        // 第一个cb点击
        // 获取所有的checkbox
        var cbs = document.getElementsByName("cb");
        // 遍历
        for (var i = 1; i < cbs.length; i++) {
            cbs[i].checked = this.checked;

        }
    }

    var trs = document.getElementsByTagName("tr");
    for (var i = 0; i < trs.length; i++) {
        // 移到元素上面
        trs[i].onmouseover = function() {
            this.className = "over";
        }

        // 移出元素
        trs[i].onmouseout = function() {
            this.className = "out"
        }
    }
}
```

