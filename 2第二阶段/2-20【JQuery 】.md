<!--P825-->

# 第一章 JQuery基础

## 1.1 概念

<!--P826-->

概念： 一个JavaScript框架。简化JS开发

* jQuery是一个快速、简洁的JavaScript框架，是继Prototype之后又一个优秀的JavaScript代码库（或JavaScript框架）。jQuery设计的宗旨	是“write Less，Do More”，即倡导写更少的代码，做更多的事情。它封装JavaScript常用的功能代码，提供一种简便的JavaScript设计模式，优化HTML文档操作、事件处理、动画设计和Ajax交互。
* JavaScript框架：本质上就是一些js文件，封装了js的原生代码而已

## 1.2 快速入门

<!--P827-->

### JQuery版本说明

目前jQuery有三个大版本：

* 1.x：兼容ie678,使用最为广泛的，官方只做BUG维护，
  	 功能不再新增。因此一般项目来说，使用1.x版本就可以了，
    最终版本：1.12.4 (2016年5月20日)
* 2.x：不兼容ie678，很少有人使用，官方只做BUG维护，
  	 功能不再新增。如果不考虑兼容低版本的浏览器可以使用2.x，
   最终版本：2.2.4 (2016年5月20日)
* 3.x：不兼容ie678，只支持最新的浏览器。除非特殊要求，
  	 一般不会使用3.x版本的，很多老的jQuery插件不支持这个版本。
   目前该版本是官方主要更新维护的版本。最新版本：3.2.1（2017年3月20日）

`jquery-xxx.js` 与 `jquery-xxx.min.js`区别：

1. `jquery-xxx.js`：开发版本。给程序员看的，有良好的缩进和注释。体积大一些
2. `jquery-xxx.min.js`：生产版本。程序中使用，没有缩进。体积小一些。程序加载更快

### JQuery步骤

1. 下载JQuery

2. 导入JQuery的js文件：导入min.js文件。我们用的是3.x版本。在项目下面建一个js文件夹，然后将js文件导入即可。

3. 使用

   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <title>01JQuery</title>
       <script src="js/jquery-3.3.1.min.js"></script>
   </head>
   <body>
   
       <div id="div1">div1...</div>
       <div id="div2">div2...</div>
   
       <script>
   /*
           使用传统的方式
           var div1 = document.getElementById("div1");
           var div2 = document.getElementById("div2");
   
           alert(div1.innerHTML);
           alert(div2.innerHTML);*/
   
           // 使用JQuery获取元素对象
           var div1 = $("#div1");
           var div2 = $("#div2");
   
           alert(div1.html());
           alert(div2.html());
       </script>
   </body>
   </html>
   ```

## 1.3 JQuery对象和JS对象区别与转换

<!--P828 3.29-->

1. JQuery对象在操作时，更加方便。

    ```javascript
    <script>
        // 使用传统方式获取所有名称叫做div的html元素对象
        var divs = document.getElementsByTagName("div");
        // [object HTMLCollection]
        alert(divs);
    
        // 使用JQuery方式获取所有名称叫做div的html元素对象
        var $divs = $("div");
        // [object Object]
        alert($divs);
    </script>
    ```

2. 两者都可以当做数组来使用，意味着可以使用`.length`。

    ```javascript
    <script>
        // 使用传统方式获取所有名称叫做div的html元素对象
        var divs = document.getElementsByTagName("div");
        // [object HTMLCollection]
        alert(divs.length);
    
        // 使用JQuery方式获取所有名称叫做div的html元素对象
        var $divs = $("div");
        // [object Object]
        alert($divs.length);
    </script>
    ```

3. JQuery对象和js对象方法是不通用的。

    ```javascript
    <script>
        // 使用传统方式获取所有名称叫做div的html元素对象
        var divs = document.getElementsByTagName("div");
        // 让divs中所有的div，里面的内容变成aaa
        for (var i = 0; i < divs.length; i++) {
            divs[i].innerHTML = "aaa";
        }
    
        // 使用JQuery方式获取所有名称叫做div的html元素对象
        var $divs = $("div");
        // 让divs中所有的div，里面的内容变成bbb
        $divs.html("bbb");
    </script>
    ```

3. 两者相互转换
    * `jq -- > js : jq对象[索引] 或者 jq对象.get(索引)`
    * `js -- > jq : $(js对象)`
    
    ```javascript
    <script>
        // 使用传统方式获取所有名称叫做div的html元素对象
        var divs = document.getElementsByTagName("div");
        // 将JS对象转为JQuery对象，并设置内容
        $(divs).html("aaa");
    
        // 使用JQuery方式获取所有名称叫做div的html元素对象
        var $divs = $("div");
        // 将JQuery对象转为JS对象
        $divs[0].innerHTML = "bbb";
        $divs.get(1).innerHTML = "ccc";
    </script>
    ```

## 1.4 基本语法学习

<!--P829-->

### 事件绑定

```javascript
<script>
    // 使用JS，我们之前说过他们两个的方法是不通用的，所以有bug
/*      $("#button").onclick = function () {
            alert("abc");
        }*/

    // 使用JQuery来完成事件的绑定，单击按钮之后弹出框
    $("#button").click(function () {
    	alert("abc");
	});
</script>
```

### 入口函数

通常我们是将JavaScript代码写在最上面的，但是这就造就了我们必须搞一个函数，等待html文档全部加载完成之后JavaScript代码再执行。

在JQuery也有这个机制，入口函数，就是等待DOM文档全部加载完成之后执行该函数中的代码。

```javascript
    <script>
        // 使用JavaScript来让函数等待所有页面加载完成之后执行
/*        window.onload = function () {
            $("#button").click(function () {
                alert("abc");
            });
        }*/

        // 使用JQuery来
        $(function () {
            $("#button").click(function () {
                alert("abc");
            });
        });

    </script>
```

 `window.onload`  和 `$(function)` 区别

* `window.onload` 只能定义一次,如果定义多次，后边的会将前边的覆盖掉。

  ```JavaScript
  <script>
      // 测试，使用两个window.onload
      function fun1 () {
          alert("abc");
      }
  
      function fun2 () {
          alert("def");
      }
  
      window.onload = fun1;
  	// 将fun1函数覆盖掉了，只会弹出def
      window.onload = fun2;
  
  </script>
  ```

* `$(function)`可以定义多次的。

  ```JavaScript
  <script>
      // 测试，使用两个入口函数
      $(function () {
      	alert("abc");
      });
      $(function () {
      	alert("def")
      })
  	// 两个都会弹出
  </script>
  ```

### 样式控制

```JavaScript
<script>
    // 测试，样式控制
    $(function () {
        // 建议使用第二种，因为鼠标放上去按住CTRL键，如果正确会有转发链接
        // $("#div1").css("background-color", "pink");
        $("#div1").css("backgroundColor", "pink");
    });
</script>
```

## 1.4 选择器

<!--P830-->

### 基本选择器

1. 标签选择器（元素选择器）
	* 语法： `$("html标签名")` 获得所有匹配标签名称的元素
2. id选择器 
	* 语法： `$("#id的属性值")` 获得与指定id属性值匹配的元素
3. 类选择器
	* 语法： `$(".class的属性值")` 获得与指定的class属性值匹配的元素
4. 并集选择器：
	* 语法： `$("选择器1,选择器2....")` 获取多个选择器选中的所有元素

### 层级选择器

<!--P831-->

1. 后代选择器
	* 语法： `$("A B ")` 选择A元素内部的所有B元素。是所有的后代。
2. 子选择器
	* 语法： `$("A > B")` 选择A元素内部的所有B子元素。是只有儿子的那一代。

### 属性选择器

<!--P832-->

1. 属性名称选择器 
	* 语法： `$("A[属性名]")` 包含指定属性的选择器
2. 属性选择器
	* 语法： `$("A[属性名='值']")` 包含指定属性等于指定值的选择器
3. 复合属性选择器
	* 语法： `$("A[属性名='值'][]...")` 包含多个属性条件的选择器

### 过滤选择器

<!--P833 3.30-->

1. 首元素选择器 
	* 语法： `:first` 获得选择的元素中的第一个元素
2. 尾元素选择器 
	* 语法： `:last` 获得选择的元素中的最后一个元素
3. 非元素选择器
	* 语法： `:not(selector)` 不包括指定内容的元素
4. 偶数选择器
	* 语法： `:even` 偶数，从 0 开始计数
5. 奇数选择器
	* 语法： `:odd` 奇数，从 0 开始计数
6. 等于索引选择器
	* 语法： `:eq(index)` 指定索引元素
7. 大于索引选择器 
	* 语法： `:gt(index)` 大于指定索引元素
8. 小于索引选择器 
	* 语法： `:lt(index)` 小于指定索引元素
9. 标题选择器
	* 语法： `:header` 获得标题（h1~h6）元素，固定写法

### 表单过滤选择器

<!--P834-->

1. 可用元素选择器 
	* 语法： `:enabled` 获得可用元素
2. 不可用元素选择器 
	* 语法： `:disabled` 获得不可用元素
3. 选中选择器 
	* 语法： `:checked` 获得单选/复选框选中的元素
4. 选中选择器 
	* 语法： `:selected` 获得下拉框选中的元素

## 1.5 DOM操作

<!--P835-->

### 内容操作

- `html()`: 获取/设置元素的标签体内容  

  例如：调用方法获取 `<a><font>内容</font></a>` 内容，那么就会得到： `<font>内容</font>`

- `text()`: 获取/设置元素的标签体纯文本内容

  例如：调用方法获取 `<a><font>内容</font></a>` 内容，那么就会得到：`内容`

- `val()`:获取/设置元素的value属性值

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<script  src="js/jquery-3.3.1.min.js"></script>
		<script>
			$(function() {
				// 获取myinput 的value值
				alert($("#myinput").val());
				// 设置myinput 的value值，使用的是同一个方法
				$("#myinput").val("李四");
				
				// 获取mydiv的标签体内容
				alert($("#mydiv").html());
				// 设置mydiv的标签体内容
				$("#mydiv").html("aaa");
				
				// 获取mydiv文本内容
				alert($("#mydiv").text());
				// 设置mydiv文本内容，这种方法会将标签体的内容全部改变
				// 会将<p><a href="#">标题标签</a></p>替换为bbb
				$("#mydiv").text("bbb");
			});
		</script>
	</head>
	<body>
		<input id="myinput" type="text" name="username" value="张三" /><br />
		<div id="mydiv"><p><a href="#">标题标签</a></p></div>
	</body>
</html>
```

### 属性操作

**通用属性操作**

<!--P836-->

通用属性操作：可以操作标签内所有的属性

- `attr()`: 获取/设置元素的属性
- `removeAttr()`:删除属性
- `prop()`:获取/设置元素的属性
- `removeProp()`:删除属性

<font color = "red">**固有属性prop**</font>

> attr和prop区别
>
> 1. 如果操作的是元素的固有属性，则建议使用prop
> 2. 如果操作的是元素自定义的属性，则建议使用attr
>
> 我们在这里说的是建议，大部分情况下确实是两者都可以使用，但是在一些特殊的情况下是只能够使用prop，而使用attr是获取不到的。例如：option标签的selected属性，input标签的checked属性。

```html
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>获取属性</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<script  src="js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript">
		$(function() {
			//获取北京节点的name属性值，li标签没有name属性，name属性不是固有属性
			var name = $("#bj").attr("name");
			alert(name);

			//设置北京节点的name属性的值为dabeijing
			$("#bj").attr("name", "dabeijing");

			//新增北京节点的discription属性 属性值是didu
			$("#bj").attr("discription", "didu");

			//删除北京节点的name属性并检验name属性是否存在
			$("#bj").removeAttr("name");
			
			//获得hobby的的选中状态
			var checked = $("#hoby").prop("checked");
			alert(checked);
		});
	</script>
	</head>
	<body>
		 <ul>
		 	 <li id="bj" name="beijing" xxx="yyy">北京</li>
			 <li id="tj" name="tianjin">天津</li>
		 </ul>
		 <input type="checkbox" id="hobby"/>
	</body>
</html>
```

**对class属性操作**

<!--P837-->

- `addClass()`:添加class属性值

- `removeClass()`:删除class属性值

- `toggleClass()`:切换class属性

  `toggleClass("one")`: 判断如果元素对象上存在`class="one"`，则将属性值one删除掉。  如果元素对象上不存在`class="one"`，则添加。

- `css()`:两个参数，一个属性，一个值

### CRUD操作

<!--P838-->

<!--P839-->

- `append()`:父元素将子元素追加到末尾
  * `对象1.append(对象2)`: 将对象2添加到对象1元素内部，并且在末尾
- `prepend()`:父元素将子元素追加到开头
  * `对象1.prepend(对象2)`:将对象2添加到对象1元素内部，并且在开头
- `appendTo()`:与append()方法添加相反
  * `对象1.appendTo(对象2)`:将对象1添加到对象2内部，并且在末尾
- `prependTo()`：与prepend()方法添加相反
  * `对象1.prependTo(对象2)`:将对象1添加到对象2内部，并且在开头

- `after()`:添加元素到元素后边
  * `对象1.after(对象2)`： 将对象2添加到对象1后边。对象1和对象2是兄弟关系
- `before()`:添加元素到元素前边
  * `对象1.before(对象2)`： 将对象2添加到对象1前边。对象1和对象2是兄弟关系
- `insertAfter()`
  * `对象1.insertAfter(对象2)`：将对象2添加到对象1后边。对象1和对象2是兄弟关系
- `insertBefore()`
  * `对象1.insertBefore(对象2)`： 将对象2添加到对象1前边。对象1和对象2是兄弟关系

- `remove()`:移除元素
  * `对象.remove()`:将对象删除掉
- `empty()`:清空元素的所有后代元素。
  * `对象.empty()`:将对象的后代元素全部清空，但是保留当前对象以及其属性节点

```html
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>内部插入脚本</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<script  src="js/jquery-3.3.1.min.js"></script>
	<style type="text/css">
		 	div,span{
			    width: 140px;
			    height: 140px;
			    margin: 20px;
			    background: #9999CC;
			    border: #000 1px solid;
				float:left;
			    font-size: 17px;
			    font-family:Roman;
			}
			
			div .mini{
			    width: 30px;
			    height: 30px;
			    background: #CC66FF;
			    border: #000 1px solid;
			    font-size: 12px;
			    font-family:Roman;
			}
			
			div.visible{
				display:none;
			}
	 </style>
	 <script type="text/javascript">
		$(function() {
			 // <input type="button" value="将反恐放置到city的后面"  id="b1"/>
			 $("#b1").click(function() {
				$("#city").append($("#fk"));
			 });

			 // <input type="button" value="将反恐放置到city的最前面"  id="b2"/>
			 $("#b2").click(function () {
				$("#city").prepend($("#fk"));
			 });

			 // <input type="button" value="将反恐插入到天津后面"  id="b3"/>
			 $("#b3").click(function () {
				$("#tj").after($("#fk"));
			 });

			 // <input type="button" value="将反恐插入到天津前面"  id="b4"/>
			 $("#b4").click(function () {
				$("#tj").before($("#fk"));
			 });
		});	
	</script>
	</head>
	<body>
		<input type="button" value="将反恐放置到city的后面"  id="b1"/>
		<input type="button" value="将反恐放置到city的最前面"  id="b2"/>
		<input type="button" value="将反恐插入到天津后面"  id="b3"/>
		<input type="button" value="将反恐插入到天津前面"  id="b4"/>
		 <ul id="city">
		 	 <li id="bj" name="beijing">北京</li>
			 <li id="tj" name="tianjin">天津</li>
			 <li id="cq" name="chongqing">重庆</li>
		 </ul>	 
		 <ul id="love">
		 	 <li id="fk" name="fankong">反恐</li>
			 <li id="xj" name="xingji">星际</li>
		 </ul>
		<div id="foo1">Hello1</div>    
	</body>
</html>
```

```html
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>删除节点</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<script  src="js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript">
		$(function () {
			// <input type="button" value="删除<li id='bj' name='beijing'>北京</li>"  id="b1"/>
			$("#b1").click(function () {
				$("#bj").remove();
			});
			
			// <input type="button" value="删除city所有的li节点   
			// 清空元素中的所有后代节点(不包含属性节点)"  id="b2"/>
			$("#b2").click(function () {
				$("#city").empty();
			});
		});
	</script>
	</head>
	<body>
	<input type="button" value="删除<li id='bj' name='beijing'>北京</li>"  id="b1"/>
	<input type="button" value="删除所有的子节点   清空元素中的所有后代节点(不包含属性节点)"  id="b2"/>
		 <ul id="city">
		 	 <li id="bj" name="beijing">北京</li>
			 <li id="tj" name="tianjin">天津</li>
			 <li id="cq" name="chongqing">重庆</li>
		 </ul>
		 <p class="hello">Hello</p> how are <p>you?</p> 
	</body>
</html>

```

## 1.6 案例

<!--P840-->

<!--P841-->

# 第二章 JQuery进阶

<!--P844 4.01-->

## 2.1 动画

<!--P845-->

一共有三种方式显示和隐藏元素

- `show`：显示
- `hide`：隐藏
- `toggle`：切换

### 参数

- `speed`：动画的速度。三个预定义的值(`"slow","normal", "fast"`)或表示动画时长的毫秒数值(如：1000)
- `easing`：用来指定切换效果，默认是`"swing"`，可用参数`"linear"`
  * `swing`：动画执行时效果是 先慢，中间快，最后又慢
  * `linear`：动画执行时速度是匀速的
- `fn`：在动画完成时执行的函数，每个元素执行一次。

### 默认显示和隐藏方式

- `show([speed,[easing],[fn]])`
- `hide([speed,[easing],[fn]])`
- `toggle([speed],[easing],[fn])`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script type="text/javascript" src="../js/jquery-3.3.1.min.js"></script>
	<script>
		// 隐藏
		function hideFn() {
			$("#showDiv").hide("slow", "swing", function () {
				alert(1);
			});
		};
		// 对于参数，我们可以省略中间的easing和函数，和上面的函数效果相同
		/* function hideFn() {
			$("#showDiv").hide("slow");
		};*/
		
		function showFn() {
			$("#showDiv").show("slow");
		};
		
		function toggleFn() {
			$("#showDiv").toggle("slow");
		};
	</script>
</head>
<body>
	<input type="button" value="点击按钮隐藏div" onclick="hideFn()">
	<input type="button" value="点击按钮显示div" onclick="showFn()">
	<input type="button" value="点击按钮切换div显示和隐藏" onclick="toggleFn()">

	<div id="showDiv" style="width:300px;height:300px;background:pink">
    	div显示和隐藏
	</div>
</body>
</html>
```

### 滑动显示和隐藏方式

<!--P846-->

- `slideDown([speed],[easing],[fn])`
- `slideUp([speed,[easing],[fn]])`
- `slideToggle([speed],[easing],[fn])`

### 淡入淡出显示和隐藏方式

- `fadeIn([speed],[easing],[fn])`
- `fadeOut([speed],[easing],[fn])`
- `fadeToggle([speed,[easing],[fn]])`

## 2.2 遍历

<!--P847-->

### js的遍历方式

* `for(初始化值;循环结束条件;步长)`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="../js/jquery-3.3.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
		$(function () {
			// 获取所有的ul下面的li
			var cities = $("#city li");
			
			// 遍历Li
			for (var i = 0; i < cities.length; i++) {
				// 获取内容
				alert(i + ":" + cities[i].innerHTML);
			}
		});

    </script>
</head>
<body>
    <ul id="city">
        <li>北京</li>
        <li>上海</li>
        <li>天津</li>
        <li>重庆</li>
    </ul>
</body>
</html>
```

### jq的遍历方式

<!--P848-->

<!--P849-->

* 第一种方式：`jq对象.each(callback)`

  1. `this`语法：

    ```javascript
    jquery对象.each(function() {
    
    }); 
    ```

    * `this`：集合中的每一个元素对象

    ```JavaScript
    // 这种无法获取索引
    <script type="text/javascript">
        $(function () {
            // 获取所有的ul下面的li
            var cities = $("#city li");
    
            // 使用jq对象.each(callback)
            $(cities).each(function () {
                // alert(this.innerHTML);
                // 将this转为JQuery对象
                alert($(this).html());
            });
    	});
    </script>
    ```

  2. 回调函数返回值：

     ```javascript
     jquery对象.each(function(index, element) {
     
     }); 
     ```

     * `index`:就是元素在集合中的索引
     * `element`：就是集合中的每一个元素对象

     * `false`:如果当前function返回为false，则结束循环(相当于break)。
     * `true`:如果当前function返回为true，则结束本次循环，继续下次循环(相当于continue)

     ```javascript
     <script type="text/javascript">
         $(function () {
             // 获取所有的ul下面的li
             var cities = $("#city li");
     
             $(cities).each(function (index, element) {
                 if ("上海" == $(element).html()) {
                     // 如果当前function返回为false，则结束循环(相当于break)
                     // return false;
     
                     // 如果当前function返回为true，则结束本次循环，继续下次循环(相当于continue)
                     return true;
                 }
                 // alert(index + "：" + element.innerHTML);
                 // 将JS中element对象转为JQuery对象，调用html方法
                 alert(index + "：" + $(element).html());
     
             });
         });
     
     </script>
     ```

* 第二种方式：`$.each(object, [callback])`

  ```javascript
  <script type="text/javascript">
      $(function () {
          // 获取所有的ul下面的li
          var cities = $("#city li");
  
          $.each(cities, function () {
              alert($(this).html());
          });
      });
  </script>
  ```

* 第三种方式：`for..of`: jquery 3.0 版本之后提供的方式
  `for(元素对象 of 容器对象)`

  ```javascript
  <script type="text/javascript">
      $(function () {
          // 获取所有的ul下面的li
          var cities = $("#city li");
  
          for (li of cities) {
              alert($(li).html());
          }
      });
  </script>
  ```

## 2.3 事件绑定

<!--P850-->

<!--P851-->

<!--P852-->

jquery标准的绑定方式：`jq对象.事件方法(回调函数)`

* 注：如果调用事件方法，不传递回调函数，则会触发浏览器默认行为。
  * `输入框对象.focus()`;// 获得焦点
  * `表单对象.submit()`;//让表单提交

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="../js/jquery-3.3.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
		$(function () {
		
			$("#name").click(function () {
				alert("鼠标点击了");
			});
			
			// 也可以链式编程
			$("#name").mouseover(function () {
				alert("鼠标来了");
			}).mouseout(function () {
				alert("鼠标走了");
			});
		});
	
    </script>
</head>
<body>
<input id="name" type="text" value="绑定点击事件">
</body>
</html>
```

on绑定事件/off解除绑定
* `jq对象.on("事件名称",回调函数)`
* `jq对象.off("事件名称")`如果off方法不传递任何参数，则将组件上的所有事件全部解绑

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="../js/jquery-3.3.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
		
		$(function () {
			// 使用on给按钮绑定单击事件 click
			$("#btn").on("click", function () {
				alert("被点击了");
			});
			
			// 使用off解除btn按钮的单击事件
			$("#btn2").click(function () {
				$("#btn").off("click");
			});
		});
    </script>
</head>
<body>
<input id="btn" type="button" value="使用on绑定点击事件">
<input id="btn2" type="button" value="使用off解绑点击事件">
</body>
</html>
```

事件切换：toggle
* `jq对象.toggle(fn1,fn2...)`当单击jq对象对应的组件后，会执行fn1.第二次点击会执行fn2.....

* 注意：1.9版本 .toggle() 方法删除,jQuery Migrate（迁移）插件可以恢复此功能。

   ```javascript
   // 可以利用下面代码来导入资源
   <script src="../js/jquery-migrate-1.0.0.js" type="text/javascript" charset="utf-8"></script>
   ```

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="../js/jquery-3.3.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="../js/jquery-migrate-1.0.0.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
        
        $(function () {
		
			$("#btn").toggle(function () {
				$("#myDiv").css("backgroundColor", "green");
			}, function () {
				$("#myDiv").css("backgroundColor", "pink");
			});
		});

    </script>
</head>
<body>

    <input id="btn" type="button" value="事件切换">
    <div id="myDiv" style="width:300px;height:300px;background:pink">
        点击按钮变成绿色，再次点击红色
    </div>
</body>
</html>
```

## 2.4 案例

### 显示广告

需求：
    1. 当页面加载完，3秒后。自动显示广告
    2. 广告显示5秒后，自动消失。

分析：
       1. 使用定时器来完成。setTimeout (执行一次定时器)
       2. 分析发现JQuery的显示和隐藏动画效果其实就是控制display
       3. 使用  show/hide方法来完成广告的显示

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>广告的自动显示与隐藏</title>
        <style>
            #content{width:100%;height:500px;background:#999}
        </style>

        <!--引入jquery-->
        <script type="text/javascript" src="../js/jquery-3.3.1.min.js"></script>
        <script>

            //入口函数，在页面加载完成之后，定义定时器，调用这两个方法
            $(function () {
                //定义定时器，调用adShow方法 3秒后执行一次
                setTimeout(adShow,3000);
                //定义定时器，调用adHide方法，8秒后执行一次
                setTimeout(adHide,8000);
            });
            //显示广告
            function adShow() {
                //获取广告div，调用显示方法
                $("#ad").show("slow");
            }
            //隐藏广告
            function adHide() {
                //获取广告div，调用隐藏方法
                $("#ad").hide("slow");
            }
        </script>
    </head>

    <body>
        <!-- 整体的DIV -->
        <div>
            <!-- 广告DIV -->
            <div id="ad" style="display: none;">
                <img style="width:100%" src="../img/adv.jpg" />
            </div>

            <!-- 下方正文部分 -->
            <div id="content">
                正文部分
            </div>
        </div>
    </body>
</html>
```

	2. 给结束按钮绑定单击事件
	1.1 停止定时器
	1.2 给大相框设置src属性

### 抽奖

<!--P855 4.02-->

分析

1. 给开始按钮绑定单击事件

     * 定义循环定时器

     * 切换小相框的src属性

     * 定义数组，存放图片资源路径

     * 生成随机数。数组索引

2. 给结束按钮绑定单击事件

     * 停止定时器

     * 给大相框设置src属性

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>jquery案例之抽奖</title>
        <script type="text/javascript" src="../js/jquery-3.3.1.min.js"></script>
        <script language='javascript' type='text/javascript'>
            
            var imgs = ["../img/man00.jpg",
                        "../img/man01.jpg",
                        "../img/man02.jpg",
                        "../img/man03.jpg",
                        "../img/man04.jpg",
                        "../img/man05.jpg",
                        "../img/man06.jpg",
                       ];
            var startId;//开始定时器的id
            var index;//随机角标
            $(function () {
                //处理按钮是否可以使用的效果
                $("#startID").prop("disabled",false);
                $("#stopID").prop("disabled",true);
                //1. 给开始按钮绑定单击事件
                $("#startID").click(function () {
                    // 1.1 定义循环定时器 20毫秒执行一次
                    startId = setInterval(function () {
                        //处理按钮是否可以使用的效果
                        $("#startID").prop("disabled",true);
                        $("#stopID").prop("disabled",false);
                        //1.2生成随机角标 0-6
                        index = Math.floor(Math.random() * 7);//0.000--0.999 --> * 7 --> 0.0-----6.9999
                        //1.3设置小相框的src属性
                        $("#img1ID").prop("src",imgs[index]);

                    },20);
                });
                
                //2. 给结束按钮绑定单击事件
                $("#stopID").click(function () {
                    //处理按钮是否可以使用的效果
                    $("#startID").prop("disabled",false);
                    $("#stopID").prop("disabled",true);
                    // 1.1 停止定时器
                    clearInterval(startId);
                    // 1.2 给大相框设置src属性
                    $("#img2ID").prop("src",imgs[index]).hide();
                    //显示1秒之后
                    $("#img2ID").show(1000);
                });
            });
        </script>

    </head>
    <body>
        <!-- 小像框 -->
        <div style="border-style:dotted;width:160px;height:100px">
            <img id="img1ID" src="../img/man00.jpg" style="width:160px;height:100px"/>
        </div>

        <!-- 大像框 -->
        <div
             style="border-style:double;width:800px;height:500px;position:absolute;left:500px;top:10px">
            <img id="img2ID" src="../img/man00.jpg" width="800px" height="500px"/>
        </div>

        <!-- 开始按钮 -->
        <input
               id="startID"
               type="button"
               value="点击开始"
               style="width:150px;height:150px;font-size:22px">

        <!-- 停止按钮 -->
        <input
               id="stopID"
               type="button"
               value="点击停止"
               style="width:150px;height:150px;font-size:22px">
    </body>
</html>
```

## 2.5 插件

<!--P856-->

插件：**增强JQuery的功能**

实现方式：

1. $.fn.extend (object) 
   			* 增强通过Jquery获取的对象的功能  $("#id")
2. $.extend(object)
   			* 增强JQeury对象自身的功能  $/jQuery

