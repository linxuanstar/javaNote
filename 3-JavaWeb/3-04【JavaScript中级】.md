# 第四章 事件监听机制

时间监听机制概念：某些组件被执行了某些操作后，触发某些代码的执行。

- 事件：某些操作。例如：单击、双击、键盘按下了、鼠标移动了等。

- 事件源：组件。例如：按钮、文本输入框等。

- 监听器：代码。

- 注册监听：将事件、事件源和监听器结合在一起。事件源上面发生某个事件，则触发执行某个监听器打码。

JS中绑定事件一共有两种方法：直接在html标签上面绑定、通过JavaScript获取元素对象绑定`元素.on事件名=函数`。采用第二种：

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

DOM 3级：DOM3级事件 在DOM2级事件的基础上添加了更多的事件类型。

- UI事件：当用户与页面上的元素交互时触发，如：load、scroll

- 焦点事件：当元素获得或失去焦点时触发，如：blur、focus

- 鼠标事件：当用户通过鼠标在页面执行操作时触发如：dbclick、mouseup

- 滚轮事件：当使用鼠标滚轮或类似设备时触发，如：mousewheel

- 文本事件：当在文档中输入文本时触发，如：textInput

- 键盘事件：当用户通过键盘在页面上执行操作时触发，如：keydown、keypress

- 合成事件，当为IME（输入法编辑器）输入字符时触发，如：compositionstart

- 变动事件，当底层DOM结构发生变化时触发，如：DOMsubtreeModified

## 4.1 DOM事件机制

DOM2级事件模型规定事件流包括三个阶段：事件捕获阶段、目标阶段、事件冒泡阶段。

**演示事件冒泡**

```html
<body>
    <div class="box">
        <div class="item1">
            <div class="item2">
                <div class="item3">item3</div>
                item2
            </div>
            item1
        </div>
        box
    </div>

    <script>
        var box = document.querySelector('.box');
        var item1 = document.querySelector('.item1');
        var item2 = document.querySelector('.item2');
        var item3 = document.querySelector('.item3');
        box.addEventListener('click', function () {
            console.log('我是box');
        });
        item1.addEventListener('click', function () {
            console.log('我是item1');
        });
        item2.addEventListener('click', function () {
            console.log('我是item2');
        });
        item3.addEventListener('click', function () {
            console.log('我是item3');
        });
    </script>
</body>
```

点击item3时，由内到外一次响应事件函数。先执行item3，之后item2，之后item1，最后box。盒子模型中box最大，包含了item1、item2、item3。

**事件捕获示例**

```html
<script>
    var box = document.querySelector('.box');
    var item1 = document.querySelector('.item1');
    var item2 = document.querySelector('.item2');
    var item3 = document.querySelector('.item3');
    box.addEventListener('click', function () {
        console.log('我是box');
    }, true);
    item1.addEventListener('click', function () {
        console.log('我是item1');
    }, true);
    item2.addEventListener('click', function () {
        console.log('我是item2');
    }, true);
    item3.addEventListener('click', function () {
        console.log('我是item3');
    }, true);
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

