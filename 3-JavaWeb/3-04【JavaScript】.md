# 第一章 JS基础

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

## 1.1 基本语法

JavaScript语法和Java相似，每个语句以`;`结束，语句块用`{···}`。如果一行只有一条语句，那么`;`可以省略。

```javascript
// 单行注释
var num = 3
/* 多行注释 */
alert(num)
```

### 1.1.1 数据类型

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

一组数据和功能的集合。可以通过执行new操作符后跟要创建的对象类型的名称来创建，而创建Object类型的实例并为其添加属性或方法。

```js
var student = {
	name:'linxuan',
	age:20,
	num:xxxxxxxx
};
// 也可以是
var student=new Object();
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

### 1.1.2 typeof

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

### 1.1.3 变量

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

![](..\图片\3-04【JavaScript】\1-1.png)

### 1.1.4 操作符

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

### 1.1.5 流程控制语句

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

![](..\图片\3-04【JavaScript】\1.png)

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

