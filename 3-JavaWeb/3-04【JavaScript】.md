# 第一章 JS基本语法

JavaScript：一门客户端脚本语言。运行在客户端浏览器中，每一个浏览器都有JavaScript的解析引擎。可以增强用户和HTML页面的交互过程，可以控制HTML元素，让页面有一些动态效果，增强用户体验。

脚本语言：无需编译，可以直接运行。

JavaScript是一种解释型脚本语言，目的是能够在客户端的网页中增加动态效果和交互能力，实现了用户与网页之间的一种实时的、动态的交互关系。它最初是由网景公司设计的，起名为LiveScript，后来Java语言非常红火，网景公司希望借助Java的名气来推广，改名为JavaScript，但是除了语法有点相似，在其他部分基本上没啥关系。后来各种被抄，客户端脚本语言市场混乱，JavaScript被Netscape公司交给ECMA(欧洲计算机制造商协会)制定标准，指定了所有的客户端脚本语言的标准：ECMAScript。

JavaScript = ECMAScript + JavaScript自己特有的东西(BOM + DOM)

+ ECMAScript：主要定义了JS的语法。
+ DOM：一套操作页面元素的API，DOM把HTML看做是文档树，通过DOM提供的API对树上的节点进行操作。
+ BOM：一套操作浏览器功能的API，通过BOM可以操作浏览器窗口。

JS代码与HTML结合方式有三方式：行内JS、script标签内部JS和外部JS。

- 行内JS：写在行内，用`属性=值`的方式来写。

  ```html
  <input type="button" value="按钮" onclick="alert('Hello World')" />
  ```

- 内部JS：定义`<script`>，标签内部就是JS代码

  ```html
  <script>
      alert('Hello,world!');
  </script>
  ```

- 外部JS：定义`<script>`，通过src属性引入外部的JS文件。推荐使用这种方式。

  ```html
  <script type="text/javascript" src="/js/Hello.js"></script>
  ```

`<srcipt>`可以定义在HTML页面的任何地方。但是定义的位置会影响执行顺序，`<srcipt`>可以定义多个。

JavaScript语法和Java相似，每个语句以`;`结束，语句块用`{···}`。如果一行只有一条语句，那么`;`可以省略。

```javascript
// 单行注释
var num = 3
/* 多行注释 */
alert(num)
```

## 1.1 数据类型

JS的数据类型分为两大类：

- 简单数据类型（基本数据类型）：boolean、number、string、null、undefined（、symbol）
- 复杂数据类型：object

**boolean**

布尔值和布尔代数的表示完全一样，一个布尔值只有**true**和**false**两种值，区分大小写。可以直接用**true**和**false**表示布尔值，也可以通过布尔运算算出来。布尔值经常用在条件判断句中。

`Boolean()`将一个值转换为其对应的boolean值，转为false的值：`null`、`undefined`、`“”`、`NaN`、`0`。

```js
// 转为false
console.log(Boolean(0));
```

**number**

JavaScript不区分整数和浮点数，统一用number表示，以下都是合法的number类型：

```js
123; // 整数123
0.456; // 浮点数0.456
1.2345e3; // 等同于1234.5
-99; // 负数
NaN; // 当无法计算结果是使用NaN表示，not a number一个不是数字的数字类型
Infinity; // 表示无限大

// number存在精度问题，所以最好不要判断浮点数是否相等。
0.2 + 0.1 = 0.30000000000000004
0.1 + 0.2 !== 0.3   //true
12
```

数值转换有多种方法：`Number()`、`parseInt()`、`parseFloat()`、`取正或取负`、`-0`

* `Number()`：直接调用Number方法，然后传入参数就会进行数值转换。

  ```js
  // 布尔值转换为1或者0
  console.log(Number(true));         // 1
  // number类型直接返回
  console.log(Number(1));            // 1
  // undefined转为NaN
  console.log(Number(undefined));    // NaN
  // null转为0
  console.log(Number(null));         // 0
  // 字符串类型：若参数只包含数字（正 负 浮点）直接转为十进制数字、若为十六进制转为十进制、若其他格式转NaN
  console.log(Number("123"));       //123
  ```

* `parseInt()`：当第一个字符不是数字字符或负号，结果为NaN。例如：`parseInt("1234blue");`
  当是0x开头所组成的字符串，符合要求可看作十六进制数，然后进行转换，结果为十进制。当要转换的是boolean类型时，结果为 NaN。

  ```js
  // 指定进制
  parseInt("070",8);    // 56
  parseInt("AF",16);    // 175
  ```

* `parseFloat()`：十六进制的字符串始终会转为0。
  无法转换boolean类型的数据，结果都为NaN。字符串包含的是一个可解析的整数，`parseFloat()`会返回整数
  没有第二个参数。

* `取正或取负`：当我们对数据进行取正或取负时，会隐性的进行数值转换。

* `-0`：进行`-0`操作时，也会隐性的进行数值转换。

**string**

用于表示由零个或多个16位Unicode字符组成的字符序列，即字符串。字符串是以单引号`'`或双引号`"`括起来的任意文本。JS中string是不可变的，当重新为一个字符串赋值时，实际上是重新开辟内存空间。

转为字符串的方法有：`toString()`、`String()`、`值+""`

* `toString()`：数值、布尔值、对象、字符串值都有这个方法，null、undefined没有这个方法。字符串使用这个方法返回字符串的副本。可传参控制输出进制。

  ```js
  var num = 10;
  num.toString();   // "10"
  // 转换为2进制
  num.toString(2);  // "1010"
  ```

* `String()`：在不知道转换值是不是null、undefined时，可以使用`String()`转换。

  ```js
  // 如果值有toString()方法，调用toString()（没有参数）并返回
  String(10);    // "10"
  // 如果是null，返回"null"
  String(null);  // 'null'
  ```

* `值+""`：转为字符串

  ```js
  18+""     // "18"
  true+""   // 'true'
  ```

**null和undefined**

`null`表示一个“空”的值，他和0以及空字符串`''`不同，0是一个数值，`''`表示长度为0的字符串，而null表示空。
`undefined`表示“未定义”。

**object**

Object类型是所有类型的超类，自定义的任何类型，默认继承Object。

一组数据和功能的集合。可以通过执行new操作符后跟要创建的对象类型的名称来创建，而创建Object类型的实例并为其添加属性或方法。

```js
var student = {
	name:'linxuan',
	age:20,
	num:xxxxxxxx
};
// 也可以是
var student = new Object();
student.name='linxuan';
```

要获取一个对象的属性，我们用`对象变量.属性名`的方法。object的每个实例都有下列属性或方法：

- `constructor`：保存着用于创建当前对象的函数
- `hasOwnProperty(propertyName)`：用于检查给定的属性是否在当前对象实例中（不是在实例的原型中），参数为字符串格式
- `isPrototypeOf(object)`：用于检查传入的对象是否是当前对象的原型
- `propertyIsEnumerable(propertyName)`：用于检查给定的属性是否能够使用for-in语句来枚举，参数是字符串形式
- `toLocaleString()`：返回对象的字符串表示，该字符串与执行环境的地区对应
- `toString()`：返回对象的字符串表示
- `valueOf()`：返回对象的字符串，数值或布尔值表示，通过与toString()返回值相同

## 1.2 typeof

typeof的作用是获取变量类型，返回的值是string类型：`“undefined”`、`“boolean”`、`“string”`、`“number”`、`“object”`、`“function”`。

```js
typeof 10     // "number"
typeof  "10"  // "string"
typeof age    // age未声明，所以是"undefined"

// typeof将null的类型定为object是因为 null被认为是空的对象引用
typeof null   // "object"
typeof undefined // "undefined"

function fn(){
    ...
}
typeof fn   // "function"
```

## 1.3 变量

一个变量就是分配了一个值的参数。使用变量可以方便的获取或者修改内存中的数据。在声明变量时使用关键字`var`（ES6新增的声明变量的关键词let和const），要注意关键字与变量名之间的空格，也可以在一行中声明多个变量，以逗号分隔变量。

变量名必须是一个JavaScript标识符，应遵循以下标准命名规则：第一个字符必须是字母、下划线（_）或者美元符（$）。后面可以跟字母、下划线、美元符、数字，但不能是其他符号。在被申明的范围内，变量的名称必须是唯一的。不能使用保留关键字作为标识符。

```js
var age, name, sex;
age = 10;
name = 'zs';
```

在JavaScript中，使用`=`对变量进行赋值。可以把任意数据类型赋值给变量，同一个变量可以反复赋值，而且可以是不同的数据类型的变量，但是只能用`var`申明一次。要显示变量，可以用`console.log(x)`。

**let**

var定义的变量，可以预解析，提前调用的结果是undefined。let定义的变量不能预解析，提前调用的结果是报错。

var定义的变量，变量名称可以重复，效果是重复赋值。let定义的变量不能重复，否则执行报错。

var定义的变量作用域是全局/局部作用域。let定义的变量如果在{}中只能在{}中调用。

在循环语句中var定义的循环变量和使用let定义的循环变量。执行原理和执行效果不同。

```js
// 提前调用 预解析
console.log( int1 );
// // 提前调用 结果是报错
console.log( int2 );

// var 定义的变量 
var int1 = 100 ;
let int2 = 200 ;
```

**const**

var定义的变量，可以预解析，提前调用的结果是undefined。const定义的变量不能预解析，提前调用的结果报错。

var定义的变量，变量名称可以重复，效果是重复赋值，const定义的变量不能重复，否则执行报错。

var定义的变量作用域是全局/局部作用域。const定义的变量如果在{}中只能在{}中调用。

const 定义的变量存储的数据数值不能改变，也就是const定义的变量，不能重复赋值。

**var和let循环**

var声明的循环变量：在整个循环变量过程中只定义了一个循环变量i，每次循环都对这一个循环变量i进行重复赋值，也就是之后的循环变量数值会覆盖之前的循环变量数值，当循环结束后只有一个循环变量i，存储的是最终的循环变量数值。

let声明的循环变量：在整个循环过程中每次循环都相当于触发执行了一个{   }，每一个{   }对于let定义的变量就是一个独立的作用域，也就是每次循环let声明的循环变量都是一个人独立作用域中的循环变量，每一次循环中循环变量都会存储不同的数据数值，互相之间不会影响，不会覆盖，也就是每次循环let声明的循环变量都相当于是一个独立的变量，不会覆盖之前的数据数值。

```html
<ul>
    <li>我是第一个li</li>
    <li>我是第二个li</li>
    <li>我是第三个li</li>
    <li>我是第四个li</li>
    <li>我是第五个li</li>
</ul>

<script>
    // 给 li 绑定事件 点击 li标签 弹出 索引下标
    // 获取标签对象
    const oLis = document.querySelectorAll('ul>li');

    // 通过 for循环 给 li标签 绑定事件
    for( var i = 0 ; i <= oLis.length -1 ; i++ ){
        // i 是 索引下标 oLis[i] 是 li标签对象
        oLis[i].addEventListener( 'click' , function(){

            // 点击时输出索引下标
            console.log( '我是var循环的i ${i}' );
        })
    }
    for( let j = 0 ; j <= oLis.length -1 ; j++ ){
        // i 是 索引下标 oLis[i] 是 li标签对象
        oLis[j].addEventListener( 'click' , function(){

            // 点击时输出索引下标
            console.log( '我是let循环的i ${j}' );
        })
    }
</script>
```

<img src="..\图片\3-04【JavaScript】\1-1.png" />

## 1.4 操作符

运算符是用于执行程序代码运算，会针对一个以上操作数项目来进行运算。JavaScript中操作符包括：算术操作符、位操作符、布尔/逻辑操作符、关系操作符、相等操作符、条件操作符、赋值操作符。

运算符的优先级：（） --> 一元运算符 --> 算术运算符 --> 关系运算符 --> 相等运算符 --> 逻辑运算符 --> 赋值运算符。

根据算术的个数，运算符又分为：一元、二元、三元

1. 如果操作数只有一个，称之为一元运算符，或者单目运算符。
2. 如果操作数有两个，称之为二元运算符，或者双目运算符，这是最多的
3. 如果操作数有三个，称之为三元运算符，或者三目运算符，在js中，只有一个`?:`

**算术运算符**

算术运算符可以分为：

- 一元运算符：`++、--、+(正号)、-(负号)`
  `++`（递增）和`–-`（递减）操作符：可用于字符串、布尔值、浮点数、整数和对象，操作数自加1或自减1。
  `+(正号)、-(负号)`：在js中，如果运算数不是运算符所要求的类型，那么js引擎会自动的将运算数进行类型转换

  ```js
  // string类型转number 按照字面值进行转换。如果字面值不是数字，那么转为NaN(不是数字的数字)。
  var a = + "123";
  // 输出number
  alert(typeof(a)); 
  // boolean类型转number：true转为1，false转为0。
  document.write(+ true);
  ```

- 二元运算符：`+、-、*、/ %` 
  由于JavaScript里面数据类型是number，所以在进行除法运算的时候，整数除整数不是整数，变成了小数。

  ```js
  var a = 3;
  var b = 4;
  // 0.75
  document.write(a / b);
  ```

**位操作符**

按内存中表示数值的位来操作数值，先将64位的值转换为32位，执行位操作，再转换回64位数值。NaN、Infinity当0来处理，非数值调用Number()。

位操作符有：按位非（~）、按位与（&）、按位或（|）、按位异或（^）、左移（<<）、有符号右移（>>）、无符号右移（>>>）。

* 按位非（~）：返回数值的反码，本质就是操作数的负值减1。按位非是数值表示的最底层执行操作，所以速度比取负减1更快

  ```js
  var num = 25;
  num = ~num;
  // -26
  console.log(num)
  ```

* 按位与（& 全1则1）、按位或（| 有1则1）、按位异或（^ 不同则1）：将两个值转为二进制然后进行按位操作。

* 左移（<<）：将数值的所有位向左移动指定的位数（移动的是二进制），左移不会影响符号位。

* 有符号右移（>>）：将数值向右移动，但保留符号位，有符号的右移和左移恰好相反。用符号位的数值来填充

* 无符号右移（>>>）：这个操作符会将32位都向右移动，对正数来说，无符号右移得结果和有符号右移的结果相同。但对负数来说，无符号右移是用0来填充。无符号右移会把负数的二进制码当作正数的二进制码

**布尔/逻辑操作符**

三种：与（&&）、或（||）、非（!）。

* `&&`运算是与运算，只有所有都为true，&&运算结果才是true；当左边是false的时候，右边就不会判断，结果直接为false。
* `||`运算是或运算，只要其中有一个true，||运算结果都为true；左边是true，同理右边不会判断，结果直接判定为true。
* `!`运算是非运算，他是一个单目运算符，把true变成false，把false变为true；`!`运算符号不仅仅用在true和false上面，可以用在其他类型上面。作用在其他类型上面的时候，会进行类型转换。
  在`number`类型中0或者NaN为假，其他为真。在`string`类型中除了空字符串("")，其他都是true。`null & undefined`都是false。对象都是true

**关系操作符**

关系操作符有：小于（<）、大于（>）、小于等于（<=）、大于等于（>=）。如果类型相同那么直接比较，类型不同则先进行类型转换再进行比较。

```js
// false
document.write((3 > 4));
// 字符串进行比较，会按照字典顺序进行比较，也就是ASCII码表。按位逐一进行比较，直到得出大小为止。
document.write(("abc" > "acd"))
// 先进行类型转换，再进行比较 true
document.write(("123" > 122));	
```

**相等操作符**

JavaScript允许对任意数据类型作比较，但特别要注意相等于算符。JavaScript在设计时，有两种相等运算符：

- 第一种是`==`，它会自动转换类型再比较。不相等操作符为`!=`。

  ```js
  // null和undefined是相等的 true
  null == undefined
  // 有一个是NaN，则结果是 false。NaN与其他的值都不想等，包括他自己
  "NaN" == NaN
  // 如果有一个布尔值，将布尔值转换为数值 true
  false == 0
  // 在比较相等前，不能将null和undefined转换为其他值。所以这里是false
  undefined == 0
  ```

- 第二种是`===`，不会自动转换类型，两个表达式（包括数据类型）相等，则结果为true。不全等操作符为`!==`

  ```js
  // false。如果是==那么会转类型再比较，结果就是true。但是===不会转类型，所以返回false
  null === undefined      
  ```

注意： NaN与其他的值都不想等，包括他自己，唯一能判断NaN的方法是通过`isNaN()`函数：`isNaN(NaN);`

**条件操作符**

条件操作符就是三元表达式`?:`。

```js
variable = boolean_expression ? true_value : false_value

var num1 = 2;
var num2 = 3;
var num3 = num1 > num2 ? 1 : 0;
// 0
document.write(num3);			
```

**赋值操作符**

简单的赋值操作符就是`=`，其作用就是把右边的值赋给左边的变量。复合赋值操作符：乘赋值 `*=`、除赋值 `/=`、模赋值 `%=`、加赋值 `+=`、减/赋值 `-=`、…。

## 1.5 流程控制语句

* `if...else...`

* `swith`。JavaScript语句可以接受任意的原始数据类型

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
  
* for-in语句：迭代语句，用来枚举对象的属性。在使用for-in循环之前，先检测该对象的值是否是null、undefined，对象若为null、undefined，不执行for-in循环体。

  ```js
  for(property in expression) statement
  ```

  ```js
  var o = {
      name: "ly",
      age: 23,
      sex: '女'
  }
  for (var key in o) {
      console.log(o[key]);
  }
  ```

* label语句：可以在代码中添加标签，以便将来使用。

  ```js
  label:statement
  ```

  ```js
  // 定义的start标签可以在将来由break、continue语句引用。加标签的语句一般都要与for循环语句配合使用
  start: for (var i = 0; i < count; i++) {
      alert(i);
  }
  ```

  ```js
  var num = 0;
  outPoint:
  for (var i = 0; i < 10; i++) {
      for (var j = 0; j < 10; j++) {
          if (i == 5 && j == 5) {
              break outPoint;
          }
          num++;
      }
  }
  alert(num);
  ```

* with语句：将代码的作用域设置到一个特定的对象中。严格模式下不允许使用with语句，将视为语法错误

  ```js
  with (expression) statement;
  ```

  ```js
  var qs = location.search.substring(1);
  var url = location.href;
  
  //用with语句
  with (location) {
      var qs = search.substrinf(1);
      var uel = href;
  }
  ```

# 第二章 基本对象

JS中的对象分为三类：

- 内置对象(静态对象)：js本身已经写好的对象，可以直接使用不需要定义。常见的内置对象有Global、Math(它们也是本地对象，根据定义的每个内置对象都是本地对象)。

- 本地对象(非静态对象)：必须实例化才能使用其方法和属性的就是本地对象。常见的本地对象有Object、Function、Date、Array、String、Boolean、Number、RegExp、Error等。

- 宿主对象：js运行和存活的地方，它的生活环境就是DOM(文档对象模型)和BOM(浏览器对象模型)。

## 2.1 函数与类

JavaScript是一种基于对象的脚本语言，代码复用的单位是函数，但它的函数比结构化程序设计语言的函数功能更丰富。函数在JavaScript中非常强大，它可以独立存在，而且JavaScript 的函数可以作为一个类使用，是定义类的唯一途径（即函数与类定义方法基本相同，只是起名时习惯把函数定义成全小写，把类定义成首字母大写）。与此同时，函数本身也是一个对象， 函数本身是Function实例。

JavaScript是弱类型语言，在定义时不需要声明返回值类型，不用声明参数的类型。函数的最大作用是提供代码的复用，将需要重复使用的代码块定义成函数，便可以最大程度减轻代码量。

函数在JavaScript中非常重要，功能也非常强大丰富，在JavaScript定义一个函数后，实际上可以得到五个东西：

- 函数：定义的就是函数，这个函数可以调用。
- 对象：定义一个函数时，系统也会创建一一个对象，该对象是Function类的实例。
- 方法：定义一个函数时，该函数通常都会附加给某个对象，作为该对象的方法。
- 变量：在定义函数的同时，也会得到一个变量。
- 类：在定义函数的同时，也得到了一个与函数同名的类。

**定义函数方式**

定义函数有三种方法：定义命名函数、定义匿名函数、使用Function类匿名函数。

```js
// 定义命名函数。定义函数hello,该函数需要一个参数
function hello (name) {
    // 函数可以有返回值，也可以没有返回值，函数的返回值使用return语句返回。
    alert(name + "，你好");
}
```

```js
// 定义匿名函数。不用给函数具体的明，而是将函数定义赋值给一个变量，这样调用该变量即可调用该函数
var f = function (name) {
    document.writeln('匿名函数<br/>');
    document.writeln('你好' + name) ;
};
```

```js
// JavaScript提供了一个Function 类，该类也可以用于定义函数
// 使用new Function()语法定义了一个匿名函数，并将该匿名函数赋给fun1变量，从而允许通过f来访问匿名函数。
// 这种方式，Function()构造器的最后一个参数包含了函数的作用，可读性变差，尽量不要使用
var fun1 = new Function("a", "b", "alert(a + b);");
```

**函数的调用**

JavaScript提供了三种调用函数的方式：直接调用函数、以call()方法调用函数、以apply()方法调用函数。

* 直接调用函数

* 以`call()`方法调用函数。调用函数时需要动态地传入一个函数引用，此时为了动态地调用函数，就需要使用call方法来调用函数了。

  ```js
  // 用于调用当前函数functionObject，并可同时使用thisObj作为本次执行时函数内部的this指针引用。
  functionObject.call( [ thisObj [, arg1 [, arg2 [, args...]]]] )
  ```

  ```js
  // 定义函数，第二个参数是函数
  var each = function (array, a) {
      for (var index in array) {
          a.call(null, index, array[index])
      }
  }
  // 使用该函数
  each([4, 20, 3], function (index, ele) {
      console.log("第" + index + "个元素是：" + ele);
  });
  ```

* 以`apply()`方法调用函数。apply()方法与call()方法功能基本相似，他们都可以动态的调用函数。通过apply(动态地调用函数时，可以以数组形式一次性传入所有调用参数。

**注意事项**

1. 定义方法的时候，形参的类型不用写，返回值类型也不用写。这是因为都是var，所以直接忽略即可。

2. 方法是一个对象，如果定义名称相同的方法，后面的方法会覆盖掉前面的方法。

3. 在JavaScript中，方法的调用只与方法的名称有关，和参数列表无关。

4. 在方法声明当中，有一个隐藏的内置对象：数组 arguments。封装着所有的实际参数。

5. 函数名称.length可以得到函数的形参个数。

**函数的实例属性和类属性**

在调用函数时使用new关键字，就可返回一个Object，这个Object不是函数的返回值，而是函数本身产生的对象。因此在JavaScript中定义的变量不仅有局部变量，还有实例属性和类属性两种。

- 局部变量：在函数中以var声明的变量。局部变量是只能在函数里访问的变量。
- 实例属性：在函数中以this前缀修饰的变量。实例属性是属于单个对象的，因此必须通过对象来访问。
- 类属性：在函数中以函数名前缀修饰的变量。类属性是属于整个类(也就是函数)本身的，因此必须通过类(也就是函数)来访问。

同一个类(也就是函数)只占用一块内存， 因此每个类属性将只占用一块内存。同一个类(也就是函数)每创建一个对象，系统将会为该对象的实例属性分配一块内存。

```js
// 创建一个函数Person，同时也是类Person
function Person(national, age) {
    // 实例属性。在函数中以this前缀修饰的变量。实例属性是属于单个对象的，因此必须通过对象来访问。
    this.age = age;
    // 类属性。在函数中以函数名称前缀修饰的变量，类属性是属于整个类本身的，因此必须通过类(也就是函数)来访问。
    Person.national = national;
    // 局部变量。在函数中以var声明的变量。局部变量是只能在函数里访问的变量。
    var bb = 0;
}

// 创建Person的一个对象person
var person = new Person('中国', 29);
// 输出person对象的年龄。年龄age是实例属性，通过对象名.属性可以访问。输出无误
document.writeln("person的age属性为" + person.age);
// 输出person对象的国家。国家national是类属性，通过类/函数名.属性可以访问。输出无误
document.writeln("通过Person访问静态national属性为" + Person.national);
// 输出bb属性。局部变量只能够在函数内部访问，不能访问。
document.writeln("person的bb属性为" + person.bb);
```

**箭头函数**

箭头函数相当于其他语言的Lambda表达式或必报写法，箭头函数是普通函数的简化写法，它的语法如下

```js
{ 参数1, 参数2, 参数3,... } => {...函数语句... }

// 上面的箭头函数等同于下面的代码
function(参数1, 参数2, 参数3....) {
    ...函数语句...
}
```

如果函数语句只有一条语句而且是return语句，那么就可以省略return；如果参数只有一个，那么就可以省略圆括号

```js
function (a) {
    return alert(a);
}
// 等同于
(a) => { return alert(a); }
// 等同于
a => alert(a);
```

**局部函数**

局部函数的概念与局部变量类似，局部变量在函数里用var关键字定义，而局部函数也在函数里定义。函数中定义的函数就叫局部函数。

局部函数只在他的父级函数里有效，出了父级函数就会失效，而且在外部函数里调用局部函数并不能让局部函数获得执行的机会，只有当外部函数被调用时，外部函数里调用的局部函数才会被执行。

**函数独立性**

函数永远是独立的。如果在函数里定义了一个局部函数，它虽然在父级函数，但是它本质上并不属于父级函数。可以通过window对象直接调用，而此时该函数中的实例this代表的将不再是父级函数，而是整个全局。

```js
function Person(name) {
    this.name = name;
    this.info = function () {
        alert("这是一个：" + this.name);
    }
}
var p = new Person("实例变量");
// 调用info的对象是p，所以此时info前的this指的还是Person类，它将输出p对象内的name变量，即“局部变量”。
p.info();
var name = "全局变量";
// 通过call调用info，调用info对象是window。程序在全局也定义了一个变量name，所以输出的就是“全局变量”。
p.info.call(window);
```

当定义某个类的方法时，该类内的局部函数是独立存在的，他不会作为类或是实例的附庸存在，这些内嵌函数可以被分离出来独立使用，这也包括让局部函数成为另一个对象的局部函数。

**函数的传参**

大部分时候，函数都需要接受参数传递，JavaScript的参数传递也全部都是采用值传递的方式。

对于基本类型参数，JavaScript 采用值传递方式，当通过实参调用函数时，传入函数里的并不是实参本身，而是实参的副本，因此在函数中修改参数值并不会对实参有任何影响。

复合类型同样也是如此，传递的也是值的副本。

```js
function changeAge(person) {
    person.age = 10;
    // 函数执行中person的age值为：10
    console.log("函数执行中person的age值为：" + person.age);
    // 将这个person对象赋值为null，测试是否是值传递
    person = null;
}
var person = { age: 5 };
// 函数调用之前person的age值为：5
console.log("函数调用之前person的age值为：" + person.age);
changeAge(person);
// 函数调用之后person的age值为：10
console.log("函数调用之后person的age值为：" + person.age);
// person对象为：[object Object]
console.log("person对象为：" + person)
```

`changeAge()`函数中的最后一行代码，将person对象赋值为了null，可是出函数后的最后一行代码，验证完person依然是个对象，而且age还是等于10，所以这表明person本身还是并未传入`changeAge()`，只是将他的副本传入了进去。

复合类型的变量本身并未持有对象本身，函数体内的age和函数体外的age是同一个age，只是他们在不同的地方调用age。`person=null`相当于毁掉了函数体内调用age的能力，但是并未毁掉age本身。相当于有两把钥匙，第二把钥匙在开完锁（将10赋值给age）后就被毁掉，但是第一把钥匙还在，所以在调用完`changeAge()`把函数内部的person赋值为null后，函数外第一把钥匙所在的person对象依然还在，age也在并且值为10。

**空参数**

JavaScript允许在定义一个有参数的函数（类）后，调用函数时如果没有传入参数，程序依然不会出现问题。只是函数内的参数已经被定义，但是没有值，所以他的值会变成undefined。

JavaScript没有其他语言中的函数重载，若模仿其他语言定义两个同名而不同参的函数，只会发生后定义的把前定义的函数覆盖掉的情况。函数名就是这个函数的唯一标识，也只能通过这个方式调用函数。

**鸭子类型理论**

鸭子类型(Duck Type)：如果弱类型语言的参数需要接受参数，则应先判断参数类型，并且判断参数是否包含了需要访问的属性。方法，只有当这些条件满都满足时，程序才开始真正处理调用参数的属性、方法。

依靠这种语法，就可以简单的完成如同Java一样的参数校验，必须输入符合要求的参数烈性，变量类型后，才执行函数体里的逻辑操作

```js
function changeAge(person) {
    // 判断person是否为对象,person下的age是否为数值类型
    if (typeof person == 'object' && typeof person.age == 'number') {
        document.write("函数执行前age的值为" + person.age + "<br />")
        person.age = 10;
        document.write("函数执行中age的值为" + person.age + "<br />")
    } else { // 否则将输出提示，参数类型不符合
        document.write("参数类型不符合" + typeof person + "<br />");
    }
}
// 分别采用不同的方式调用函数
changeAge();
changeAge('xxx');
changeAge(true);
// 采用JSON语法创建第一个对象
p = { abc: 34 };
changeAge(p);
// 采用JSON语法创建第二个对象
person = { age: 25 };
changeAge(person);
```

## 2.2 Array数组对象

特点如下：数组元素的类型可变、数组元素可以改变。

三种创建方式：`var arr = new Array(元素列表);`、`var arr = new Array(默认长度);`、`var arr = [元素列表];`。

```javascript
// 创建一个元素列表为1 2 3的数组
var arr1 = new Array(1, 2, 3);
// 创建一个长度为4的数组
var arr2 = new Array(4);
// 创建一个空数组
var arr3 = new Array();
// 创建一个元素列表为1 2 3 4的数组
var arr4 = [1, 2, 3, 4];
```

常用属性：

| **属性**    | **描述**                           |
| ----------- | ---------------------------------- |
| constructor | 返回对创建此对象的数组函数的引用。 |
| length      | 设置或返回数组中元素的数目。       |
| prototype   | 使您有能力向对象添加属性和方法。   |

常用方法：

| **方法**   | **描述**                                                     |
| ---------- | ------------------------------------------------------------ |
| concat()   | 连接两个或更多的数组，并返回结果。                           |
| join()     | 把数组的所有元素放入一个字符串。元素通过指定的分隔符进行分隔。默认`,` |
| map()      | 通过指定函数处理数组的每个元素，并返回处理后的数组。         |
| pop()      | 删除并返回数组的最后一个元素                                 |
| push()     | 向数组的末尾添加一个或更多元素，并返回新的长度。             |
| reverse()  | 颠倒数组中元素的顺序。                                       |
| shift()    | 删除并返回数组的第一个元素                                   |
| sort()     | 对数组的元素进行排序                                         |
| toString() | 把数组转换为字符串，并返回结果。                             |
| valueOf()  | 返回数组对象的原始值                                         |

## 2.3 Boolean布尔对象

Boolean 对象代表两个值:"true" 或者 "false"。Boolean 对象用于转换一个不是 Boolean 类型的值转换为 Boolean 类型值 (true 或者false)。

如果布尔对象无初始值或者其值为：0、-0、null、""、false、undefined、NaN。那么对象的值为 false。否则，其值为 true。

```js
var myBoolean = new Boolean();
```

| 属性        | 描述                                  |
| :---------- | :------------------------------------ |
| constructor | 返回对创建此对象的 Boolean 函数的引用 |
| prototype   | 使您有能力向对象添加属性和方法。      |

| 方法       | 描述                               |
| :--------- | :--------------------------------- |
| toString() | 把布尔值转换为字符串，并返回结果。 |
| valueOf()  | 返回 Boolean 对象的原始值。        |

## 2.4 Date日期对象

日期对象用于处理日期和时间。`var date = new Date();`

方法如下：

* `toLocaleString()`：返回当前date对象对应的时间本地字符串格式。
* `getTime()`：获取毫秒值。返回单签日期对象描述事件到1970年1月1日毫秒值。

```javascript
var date = new Date();	
document.write(date.toLocaleString() + "<hr>");
document.write(date.getTime() + "<hr>");
```

## 2.5 Math数学对象

Math 对象用于执行数学任务。Math 对象并不像 Date 和 String 那样是对象的类，因此没有构造函数 `Math()`。

| 属性    | 描述                                                    |
| :------ | :------------------------------------------------------ |
| E       | 返回算术常量 e，即自然对数的底数（约等于2.718）。       |
| LN2     | 返回 2 的自然对数（约等于0.693）。                      |
| LN10    | 返回 10 的自然对数（约等于2.302）。                     |
| LOG2E   | 返回以 2 为底的 e 的对数（约等于 1.4426950408889634）。 |
| LOG10E  | 返回以 10 为底的 e 的对数（约等于0.434）。              |
| PI      | 返回圆周率（约等于3.14159）。                           |
| SQRT1_2 | 返回 2 的平方根的倒数（约等于 0.707）。                 |
| SQRT2   | 返回 2 的平方根（约等于 1.414）。                       |

| 方法             | 描述                                                         |
| :--------------- | :----------------------------------------------------------- |
| abs(x)           | 返回 x 的绝对值。                                            |
| acos(x)          | 返回 x 的反余弦值。                                          |
| asin(x)          | 返回 x 的反正弦值。                                          |
| atan(x)          | 以介于 -PI/2 与 PI/2 弧度之间的数值来返回 x 的反正切值。     |
| atan2(y,x)       | 返回从 x 轴到点 (x,y) 的角度（介于 -PI/2 与 PI/2 弧度之间）。 |
| ceil(x)          | 对数进行上舍入。                                             |
| cos(x)           | 返回数的余弦。                                               |
| exp(x)           | 返回 Ex 的指数。                                             |
| floor(x)         | 对 x 进行下舍入。                                            |
| log(x)           | 返回数的自然对数（底为e）。                                  |
| max(x,y,z,...,n) | 返回 x,y,z,...,n 中的最高值。                                |
| min(x,y,z,...,n) | 返回 x,y,z,...,n中的最低值。                                 |
| pow(x,y)         | 返回 x 的 y 次幂。                                           |
| random()         | 返回 0 ~ 1 之间的随机数。                                    |
| round(x)         | 四舍五入。                                                   |
| sin(x)           | 返回数的正弦。                                               |
| sqrt(x)          | 返回数的平方根。                                             |
| tan(x)           | 返回角的正切。                                               |
| tanh(x)          | 返回一个数的双曲正切函数值。                                 |
| trunc(x)         | 将数字的小数部分去掉，只保留整数部分。                       |

## 2.6 Number数值对象

Number 对象是原始数值的包装对象。Number 创建方式 `new Number()`。

| 属性              | 描述                                   |
| :---------------- | :------------------------------------- |
| constructor       | 返回对创建此对象的 Number 函数的引用。 |
| MAX_VALUE         | 可表示的最大的数。                     |
| MIN_VALUE         | 可表示的最小的数。                     |
| NEGATIVE_INFINITY | 负无穷大，溢出时返回该值。             |
| NaN               | 非数字值。                             |
| POSITIVE_INFINITY | 正无穷大，溢出时返回该值。             |
| prototype         | 允许您可以向对象添加属性和方法。       |

| 方法                             | 描述                                                 |
| :------------------------------- | :--------------------------------------------------- |
| isFinite                         | 检测指定参数是否为无穷大。                           |
| isInteger                        | 检测指定参数是否为整数。                             |
| isNaN                            | 检测指定参数是否为 NaN。                             |
| isSafeInteger                    | 检测指定参数是否为安全整数。                         |
| toExponential(x)                 | 把对象的值转换为指数计数法。                         |
| toFixed(x)                       | 把数字转换为字符串，结果的小数点后有指定位数的数字。 |
| toLocaleString(locales, options) | 返回数字在特定语言环境下的表示字符串。               |
| toPrecision(x)                   | 把数字格式化为指定的长度。                           |
| toString()                       | 把数字转换为字符串，使用指定的基数。                 |
| valueOf()                        | 返回一个 Number 对象的基本数字值。                   |

## 2.7 String字符串对象

String 对象用于处理文本（字符串）。String 对象创建方法： `new String()`。

| 属性        | 描述                       |
| :---------- | :------------------------- |
| constructor | 对创建该对象的函数的引用   |
| length      | 字符串的长度               |
| prototype   | 允许您向对象添加属性和方法 |

| 方法                | 描述                                                         |
| :------------------ | :----------------------------------------------------------- |
| charAt()            | 返回在指定位置的字符。                                       |
| concat()            | 连接两个或更多字符串，并返回新的字符串。                     |
| endsWith()          | 判断当前字符串是否是以指定的子字符串结尾的（区分大小写）。   |
| indexOf()           | 返回某个指定的字符串值在字符串中首次出现的位置。             |
| includes()          | 查找字符串中是否包含指定的子字符串。                         |
| lastIndexOf()       | 从后向前搜索字符串，并从起始位置（0）开始计算返回字符串最后出现的位置。 |
| match()             | 查找找到一个或多个正则表达式的匹配。                         |
| repeat()            | 复制字符串指定次数，并将它们连接在一起返回。                 |
| replace()           | 在字符串中查找匹配的子串，并替换与正则表达式匹配的子串。     |
| replaceAll()        | 在字符串中查找匹配的子串，并替换与正则表达式匹配的所有子串。 |
| search()            | 查找与正则表达式相匹配的值。                                 |
| slice()             | 提取字符串的片断，并在新的字符串中返回被提取的部分。         |
| split()             | 把字符串分割为字符串数组。                                   |
| startsWith()        | 查看字符串是否以指定的子字符串开头。                         |
| substr()            | 从起始索引号提取字符串中指定数目的字符。                     |
| substring()         | 提取字符串中两个指定的索引号之间的字符。                     |
| toLowerCase()       | 把字符串转换为小写。                                         |
| toUpperCase()       | 把字符串转换为大写。                                         |
| trim()              | 去除字符串两边的空白。                                       |
| toLocaleLowerCase() | 根据本地主机的语言环境把字符串转换为小写。                   |
| toLocaleUpperCase() | 根据本地主机的语言环境把字符串转换为大写。                   |
| valueOf()           | 返回某个字符串对象的原始值。                                 |
| toString()          | 返回一个字符串。                                             |

## 2.8 RegExp正则表达式对象

正则表达式是描述字符模式的对象。正则表达式用于对字符串模式匹配及检索替换，是对字符串执行模式匹配的强大工具。

```js
// pattern描述了表达式的模式。modifiers用于指定全局匹配、区分大小写的匹配和多行匹配
var patt = new RegExp(pattern, modifiers);
// 或者更简单的方式:
var patt = /pattern/modifiers;
```

```js
// 当使用构造函数创造正则对象时，需要常规的字符转义规则（在前面加反斜杠 \）。比如，以下是等价的：
var re = new RegExp("\\w+");
var re = /\w+/;
```

| 属性        | 描述                                               |
| :---------- | :------------------------------------------------- |
| constructor | 返回一个函数，该函数是一个创建 RegExp 对象的原型。 |
| global      | 判断是否设置了 "g" 修饰符                          |
| ignoreCase  | 判断是否设置了 "i" 修饰符                          |
| lastIndex   | 用于规定下次匹配的起始位置                         |
| multiline   | 判断是否设置了 "m" 修饰符                          |
| source      | 返回正则表达式的匹配模式                           |

| 方法     | 描述                                               |
| :------- | :------------------------------------------------- |
| compile  | 在 1.5 版本中已废弃。 编译正则表达式。             |
| exec     | 检索字符串中指定的值。返回找到的值，并确定其位置。 |
| test     | 检索字符串中指定的值。返回 true 或 false。         |
| toString | 返回正则表达式的字符串。                           |

## 2.9 Global全局

JavaScript 全局属性和方法可用于创建Javascript对象。Global中封装的方法不需要对象就可以直接进行调用。`方法名称();`

| 属性      | 描述                     |
| :-------- | :----------------------- |
| Infinity  | 代表正的无穷大的数值。   |
| NaN       | 指示某个值是不是数字值。 |
| undefined | 指示未定义的值。         |

| 函数                 | 描述                                               |
| :------------------- | :------------------------------------------------- |
| decodeURI()          | 解码某个编码的 URI。                               |
| decodeURIComponent() | 解码一个编码的 URI 组件。                          |
| encodeURI()          | 把字符串编码为 URI。                               |
| encodeURIComponent() | 把字符串编码为 URI 组件。                          |
| escape()             | 对字符串进行编码。                                 |
| eval()               | 计算 JavaScript 字符串，并把它作为脚本代码来执行。 |
| isFinite()           | 检查某个值是否为有穷大的数。                       |
| isNaN()              | 检查某个值是否是数字。                             |
| Number()             | 把对象的值转换为数字。                             |
| parseFloat()         | 解析一个字符串并返回一个浮点数。                   |
| parseInt()           | 解析一个字符串并返回一个整数。                     |
| String()             | 把对象的值转换为字符串。                           |
| unescape()           | 对由 escape() 编码的字符串进行解码。               |

# 第三章 BOM和DOM

JavaScript的实现包括以下3个部分：

| JS组成                | 作用                       |
| --------------------- | -------------------------- |
| ECMAScript(核心)      | 描述了JS的语法和基本对象。 |
| 浏览器对象模型（BOM） | 与浏览器交互的方法和接口   |
| 文档对象模型 （DOM）  | 处理网页内容的方法和接口   |

BOM是浏览器对象模型，DOM是文档对象模型。前者是对浏览器本身进行操作，而后者是对浏览器（可看成容器）内的内容进行操作。BOM和DOM介绍：

1. BOM 是 各个浏览器厂商根据 DOM在各自浏览器上的实现；表现为不同浏览器定义有差别，实现方式不同。BOM 是为了操作浏览器出现的 API，window 是其的一个对象。
2. DOM 是 W3C 的标准； 所有浏览器公共遵守的标准。DOM 是为了操作文档出现的 API，document 是其的一个对象；
3. window 是 BOM 对象，而非 js 对象；javacsript是通过访问BOM（Browser Object Model）对象来访问、控制、修改客户端（浏览器）的。

归BOM管的：浏览器的标签页，地址栏，搜索栏，书签栏，窗口放大还原关闭按钮，菜单栏等等。浏览器的右键菜单。document加载时的状态栏，显示http状态码等。滚动条scroll bar。

由DOM管辖的是document。由web开发人员呕心沥血写出来的一个文件夹，里面有index.html，CSS和JS什么鬼的，部署在服务器上，我们可以通过浏览器的地址栏输入URL然后回车将这个document加载到本地，浏览，右键查看源代码等。

## 3.1 BOM浏览器对象模型

BOM（Browser Object Model）：将浏览器的各个组成部分封装为对象。常用对象有：`Window`窗口对象、`Screen`显示器屏幕对象、`Location`地址栏对象、`History`历史记录对象、`Navigator`浏览器对象。

### 3.1.1 Window窗口对象

所有浏览器都支持 window 对象。它表示浏览器窗口。所有 JavaScript 全局对象、函数以及变量均自动成为 window 对象的成员。全局变量是 window 对象的属性，全局函数是 window 对象的方法，甚至 HTML DOM 的 document 也是 window 对象的属性之一。

```js
// 二者一样的功能
window.document.getElementById("header");
document.getElementById("header");
```

window 它表示浏览器窗口。Window对象不用创建，可以直接使用。使用的时候：`window.方法名称();`。window引用也可以省略。可以简写为：`方法名称();`。

**常用属性**

获取其他BOM对象：`history`、`Location`、`Navigator`、`Screen`。

```JavaScript
// var hs1 = window.history; 等价于下面代码
var hs2 = history;
alert(hs1);
```

获取DOM对象：`document`。

```javascript
// var image = window.document.getElementById("image");
var image = document.getElementById("image");
alert(image);
```

**常用方法**

| 方法            | 作用                                                         |
| --------------- | ------------------------------------------------------------ |
| alert()         | 显示带有一段消息和一个确认按钮的警告框。                     |
| confirm()       | 显示对话框。如果用户点击确定按钮，则该方法返回true；反之返回false。 |
| prompt()        | 显示可以提示用户输入的对话框。该方法的返回值就是用户输入的内容。 |
| open()          | 打开一个新的浏览器窗口。返回值是一个新的window对象。         |
| close()         | 关闭调用对象的窗口。谁调用close方法，关闭谁。                |
| moveTo()        | 移动当前窗口                                                 |
| resizeTo()      | 调整当前窗口的尺寸                                           |
| setTimeout()    | 在指定的毫秒数后调用函数或者计算表达式。参数为要执行的代码/JavaScript 函数 |
| clearTimeout()  | 取消由setTimeout()方法设置的timeout。                        |
| setInterval()   | 循环来调用函数或者计算表达式。                               |
| clearInterval() | 取消由setInterval()方法设置的timeout。                       |

```html
<!-- // 设置两个按钮 -->
<input id = "openBtn" type = "button" value = "打开新页面">
<input id = "closeBtn" type = "button" value = "关闭新页面">
```

```js
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
```

```js
// setTimeout("fun();", 1000); 经常使用下面的方法
var id = setTimeout(fun, 1000);
document.write(id);
clearTimeout(id);
function fun() {
    alert("boom~");
}
```

```JavaScript
// 轮播图案例
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

### 3.1.2 Screen显示器屏幕对象

| 常用方法    | 作用           |
| ----------- | -------------- |
| availWidth  | 可用的屏幕宽度 |
| availHeight | 可用的屏幕高度 |

```js
console.log("可用宽度:" + window.screen.availWidth);
console.log("可用高度:" + window.screen.availHeight);
console.log("屏幕高度:" + window.screen.height);
console.log("屏幕宽度:" + window.screen.width);
```

### 3.1.3 Location地址栏对象

创建对象方法如下：使用`window.location`或者直接使用`location`。

| 常用方法   | 作用                                    |
| ---------- | --------------------------------------- |
| reload()   | 重新加载当前文档。刷新。                |
| hostname() | 返回 web 主机的域名                     |
| pathname() | 返回当前页面的路径和文件名              |
| port()     | 返回 web 主机的端口 （80 或 443）       |
| protocol() | 返回所使用的 web协议（http: 或 https:） |

| 常用属性 | 作用                    |
| -------- | ----------------------- |
| href     | 设置或者返回完整的URL。 |

### 3.1.4 history历史记录对象

window.history 对象包含浏览器的历史。

| 常用方法  | 作用                                     |
| --------- | ---------------------------------------- |
| back()    | 与在浏览器点击后退按钮相同               |
| forward() | 与在浏览器中点击向前按钮相同             |
| go()      | 参数列表的数值即为前进或者后退的页面个数 |

```js
function goForward() {
    // go() 里面的参数用来限制前进或者后退的页面个数
    history.go(2);
}
function goBack() {
    // 后退一个页面
    history.go(-1);
}
```

### 3.1.5 navigator浏览器对象

用于提供当前浏览器及操作系统等信息，这些信息都放在navigator的各个属性中。

## 3.2 DOM文档对象模型

DOM（Document Object Model）文档对象模型 是 W3C（万维网联盟）的标准。W3C DOM 标准被分为 3 个不同的部分：

- 核心 DOM - 针对任何结构化文档的标准模型
- XML DOM - 针对 XML 文档的标准模型
- HTML DOM - 针对 HTML 文档的标准模型

从`window.document`已然可以看出，`DOM`的最根本的对象是`BOM`的`window`对象的子对象。

核心DOM有：`Document`文档对象、`Element`元素对象、`Attribute`属性对象、`Text`文本对象、`Comment`注释对象、`Node`节点（是上述5个的父对象）。

| **类型**            | **描述**                                                     |
| ------------------- | ------------------------------------------------------------ |
| 元素节点（Element） | `<html>、<body>、<p>`等都是元素节点，即标签。                |
| 文本节点（Text）    | 向用户展示的内容，如`<li>...</li>`中的JavaScript、DOM、CSS等文本。 |
| 属性节点（Attr）    | 元素属性，元素才有属性,如`<a>`标签的链接属性`href="http://www.baidu.com"`。 |

HTML DOM 定义了所有 HTML 元素的对象和属性，以及访问它们的方法。HTML DOM 是关于如何获取、修改、添加或删除 HTML 元素的标准。当网页被加载时，浏览器会创建页面的文档对象模型（Document Object Model）。通过 HTML DOM，可访问 JavaScript HTML 文档的所有元素。

HTML 文档中的所有内容都是节点：整个文档是一个文档节点、每个 HTML 元素是元素节点、HTML 元素内的文本是文本节点、每个 HTML 属性是属性节点、注释是注释节点。

<img src="..\图片\3-04【JavaScript】\2-1.png" />

属性节点实际上是附属于元素的，所以不被看做是元素的子节点，因为并没有被当做是DOM的一部分。在属性节点上调用parentNode，previousSibling和nextSibling都返回null。

### 3.2.1 DOM属性

| 节点属性  | 说明                                     |
| --------- | ---------------------------------------- |
| nodeType  | 返回一个整数，这个数值代表给定节点的类型 |
| nodeName  | 返回一个字符串，其内容是节点的名字       |
| nodeValue | 返回给定节点的当前值                     |
| tagName   | 获取原节点的标签名                       |
| innerHTML | 获取元素节点的内容                       |

**nodeType**

DOM本质就是一堆节点的集合，由于包含不同类型的信息，所以就有不同类型的节点。nodeType属性会返回一个整数，这个数值代表给定节点的类型。元素节点为1、属性节点为2、文本节点为3、注释节点为8、document文档节点为9。

```html
<body>
    <p id="p">段落</p>
    <script type="text/javascript">
        <!-- 获取ID为p的元素节点 -->
        var element = document.getElementById("p");
        <!-- 获取ID为p的元素节点的第一个子节点也就是文本节点 -->
        var text = document.getElementById("p").firstChild;
        <!-- 获取ID为p的元素节点的属性节点 -->
        <!-- 属性节点实际上是附属于元素的，所以不被看做是元素的子节点，因为并没有被当做是DOM的一部分。 -->
        var property = document.getElementById("p").getAttributeNode("id");
        console.log("元素节点nodeType返回值" + element.nodeType);
        console.log("文本节点nodeType返回值" + text.nodeType);
        console.log("属性节点nodeType返回值" + property.nodeType);
        console.log("文档节点nodeType返回值" + document.nodeType);
    </script>
</body>
```

**nodeName**

nodeName是一个只读属性，不能进行设置。nodeName所包含的XML元素的标签名称永远是大写的。

- 对于元素节点，nodeName就是`标签名`。元素节点也可以通过tagName获取标签名。

- 对于文本节点，nodeName永远是`#text`

- 对于属性节点，nodeName是`属性名称`

- 对于文档节点，nodeName永远是`#document`

**nodeValue**

nodeValue：返回给定节点的当前值。

- 对于元素节点，因为本身不直接包含文本，所以nodeValue是不可用的。

- 对于文本节点，nodeValue值为文本值

- 对于属性节点，nodeValue值为属性值

**innerHTML**

innerHTML：获取元素节点的内容。尽管innerHTML只对元素节点有用，但不是所有的元素节点都能使用innerHTML，比如像`<input>` 这样的替换元素。

```html
<body>
    <label>姓名：<input type="text" value="lxy" /></label>
    <script>
        var oinput = document.getElementsByTagName("input")[0];
        console.log("oinput.innerHTML" + oinput.innerHTML);//没有内容
        console.log("oinput.value" + oinput.value);//获取input的value属性
    </script>
</body>
```

因为input里面不包含文本节点，所以用innerHTML获取不到文本节点的值。可使用value获取其属性值。类似的，form里的DOM元素（input select checkbox textarea radio）值获取时都使用value。

### 3.2.2 访问节点

DOM的思想就是每个节点都是对象，是对象我们就可以通过一些方法获取它或者改变它的属性等。可以通过多种方法来查找DOM元素：

1. 使用`getElementById()`和`getElementByTagName()`和getElementsByClassName()方法

2. 通过一个元素节点的`parentNode`、`childNodes`、`children`、`firstChild`和`lastChild`和`previousSibling`和`nextSibling`。

3. 通过`document.documentElement`和`document.body`。`document.documentElement`代表`<html>`元素。`document.body`代表`<body>`元素，可以为null，比如在body没有呈现的时候引用就是null。

| 方法                   | 说明                     |
| ---------------------- | ------------------------ |
| getElementById()       | 获取特定ID元素的节点     |
| getElementsByTagName() | 获取相同元素的节点列表   |
| getElementsByName()    | 获取相同名称的节点列表   |
| getAttribute()         | 获取特定元素节点属性的值 |
| setAttribute()         | 设置特定元素节点属性的值 |
| removeAttribute()      | 移除特定元素节点属性     |

| 遍历节点树属性  | 说明                                         |
| --------------- | -------------------------------------------- |
| childNodes      | 返回一个数组，这个数组由给定元素的子节点构成 |
| children        | 只获得子节点中的元素节点，跳过文本节点。     |
| firstChild      | 返回第一个子节点                             |
| lastChild       | 返回最后一个子节点                           |
| parentNode      | 返回一个给定节点的父节点                     |
| nextSibling     | 返回给定节点的下一个子节点                   |
| previousSibling | 返回给定节点的上一个子节点                   |

### 3.2.3 DOM操作

`DOM`通过创建树来表示文档，描述了处理网页内容的方法和接口，从而使开发者对文档的内容和结构具有空前的控制力，用`DOM API`可以轻松地删除、添加和替换节点。

**元素操作**

| DOM操作                  | 说明                                                         |
| ------------------------ | ------------------------------------------------------------ |
| creatElement(element)    | 创建一个新的元素节点                                         |
| creatTextNode()          | 创建一个包含给定文本的新文本节点                             |
| createDocumentFragment() | 创建一个文档碎片，把所有的节点都加在上面，最后把文档碎片添加到document |
| appendChild()            | 指定节点的最后一个节点列表后添加一个新的子节                 |
| insertBefore()           | 将一个给定节点插入到一个给定元素节点的给定子节点的前面       |
| removeChild()            | 从一个给定元素中删除子节点                                   |
| replaceChild()           | 把一个给定父元素里的一个子节点替换为另外一个节点             |

```html
<body>
    <input type="text" id="text" />
    <input type="button" value="添加li" id="button">
    <ul></ul>
    <script>
        window.onload = function () {
            var obutton = document.getElementById("button");
            obutton.onclick = createLi;
        }
        function createLi() {
            var oText = document.getElementById("text");
            var oUl = document.getElementsByTagName("ul")[0];
            var oLi = document.createElement("li");
            oLi.innerHTML = oText.value;
            var oA = document.createElement("a");
            oA.innerHTML = "删除";
            oA.href = "javascript:;";
            oA.onclick = function () {
                oUl.removeChild(this.parentNode);
            }
            oLi.appendChild(oA);
            oUl.appendChild(oLi);
        }
    </script>
</body>
```

**属性操作**

用`getAttribute()`，`setAttribute()`和`removeAttribute()`控制HTML标签的特性 。

**样式操作**

`obj.style.属性=属性值`，是通过添加内联样式去覆盖已有样式的。

```html
<body>
    <p id="p">p标签的内容</p>
    <script>
        var p = document.getElementById("p");
        alert("暂停观察");
        <!-- 修改属性已经属性值来改变样式 -->
        p.style.color = "red";
    </script>
</body>
```

### 3.2.2 DOM事件

时间监听机制概念：某些组件被执行了某些操作后，触发某些代码的执行。

- 事件：某些操作。例如：单击、双击、键盘按下了、鼠标移动等。

- 事件源：组件。例如：按钮、文本输入框等。

- 监听器：代码。

- 注册监听：将事件、事件源和监听器结合在一起。事件源上面发生某个事件，则触发执行某个监听器打码。

JS中绑定事件一共有两种方法：直接在html标签上面绑定、通过JavaScript获取元素对象绑定`元素.on事件名=函数`。

```html
<img id="light" src="img/light.gif">
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

DOM 1级：1998年DOM1级规范称为W3C的推荐标准。DOM1级为基本文档结构及查询提供了接口， DOM1级的目标主要是映射文档的结构。IE、Firefox、Safari、Chrome和Opera都非常完善地实现了DOM。

DOM 2级：定义了addEventListener和removeEventListener用来绑定和解绑事件。方法有三个参数：事件名（不需要写on）、事件回调函数、false/ true（事件冒泡 / 事件捕获时执行回调函数）。

DOM 3级：DOM3级事件在DOM2级事件的基础上添加了更多的事件类型。

- UI事件：当用户与页面上的元素交互时触发，如：load、scroll

- 焦点事件：当元素获得或失去焦点时触发，如：blur、focus

- 鼠标事件：当用户通过鼠标在页面执行操作时触发如：dbclick、mouseup

- 滚轮事件：当使用鼠标滚轮或类似设备时触发，如：mousewheel

- 文本事件：当在文档中输入文本时触发，如：textInput

- 键盘事件：当用户通过键盘在页面上执行操作时触发，如：keydown、keypress

- 合成事件，当为IME（输入法编辑器）输入字符时触发，如：compositionstart

- 变动事件，当底层DOM结构发生变化时触发，如：DOMsubtreeModified

```html
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

```html
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

```js
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
