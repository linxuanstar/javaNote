# 第一章 BootStrap

## 1.1 基础概念

Bootstrap：一个前端开发的框架。Bootstrap是美国Twitter公司的设计师Mark Otto和Jacob Thornton合作基于HTML、CSS、JavaScript 开发的简洁、直观、强悍的前端开发框架，使得 Web 开发更加快捷。

框架：一个半成品软件，开发人员可以在框架基础上面再进行开发，简化编码。

响应式布局：同一套页面可以兼容不同分辨率的设备。

使用Bootstrap好处：

1. 定义了很多的css样式和js插件。我们开发人员直接可以使用这些样式和插件得到丰富的页面效果。
2. 响应式布局。

## 1.2 快速入门

1. 下载Bootstrap，`bootstrap-3.4.1-dist`。下载`jquery-3.6.0.min.js`。
2. 在项目中将`bootstrap-3.4.1-dist`文件夹下面三个文件夹复制进去。
3. 创建HTML页面，引入必要的资源文件。将`jquery-3.6.0.min.js`放到js文件夹下面。

# 第二章 响应式布局

!--P651--

响应式布局：同一套页面可以兼容不同分辨率的设备。

实现：依赖于栅格系统。栅格系统：将一行平均分为12个格子，可以指定元素占几个格子。

## 2.1 实现步骤

1. 定义容器，相当于之前的table

   一共有两种容器，分类如下：

    container：针对不同的屏幕，有着不同的两边留白
    container-fluid：每一种设备上面都是100%的宽度，都没有留白

2. 定义行。相当于之前的tr

    样式：row

3. 定义元素。指定该元素在不同的设备上面，所占的格子数目。样式：`col-设备代号-格子数目`

   设备代号如下：

    xs：超小屏幕		手机(768px)：col-xs-12
    sm：小屏幕           平板(768px)
    md：中等屏幕       桌面显示器(992px)
    lg：大屏幕              大桌面显示器(1200px)

   ```html 
   !--定义容器--
   div class=container-fluid
       !--定义行--
       div class=row
           !--
           定义元素
           在大显示器上面一行12个格子
           在pad上面一行6个格子
           --
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
           div class=col-lg-1 col-sm-2 inner栅格div
       div
   div
   ```

   ```css
   .inner {
       border 1px solid red;
   }
   ```


!--P652 2.11--

 注意：

 1. 如果一行中的格子数目超过12个，那么超出的部分自动换行。

 2. 栅格属性可以向上兼容。山各类适用于屏幕宽度大于或者等于分界点大小的设备。

    	就是你设置xs：超小屏幕，设置为`col-xs-1`,那么当在大于超小屏幕上面(pad, 笔记本，电脑)的时候，仍然是1个元素占1格。设置为col-xs-2，那么那么当在大于超小屏幕上面(pad, 笔记本，电脑)的时候，仍然是一个元素占2格。

 3. 栅格属性不能够向下兼容。如果真实设备宽度小于了设置栅格类属性的设备代码的最小值，会一个元素占满一整行。

    	设置lg：大屏幕，设置为`col-lg-1`，那么当在大屏幕上显示的是1个元素占一格，但是当在小于大屏幕的显示器(笔记本电脑, pad，超小屏幕)上面,则是一个元素占一行。

# 第三章 CSS样式和JavaScript插件

!--P653--

## 3.1 全局CSS样式

### 按钮

`class = btn btn-default;`

default是默认样式，defult可以更换为其他几个单词：

1. primary：首选项
2. success：成功
3. info：一般信息
4. warning：警告
5. danger：危险
6. link：链接

### 图片

`class = img-responsive`：图片在任意尺寸都占比100%。

下面是图片形状的类属性：

1. `class = img-rounded`：设置图片为方形
2. `class = img-circle`：设置图片为圆形
3. `class = img-thumbnail`：设置图片加一个相框 

!--P654--

### 表格

`class = table`：表格

`class = table-bordered`：有边框的表格

`class = table-hover`：鼠标悬浮上面进行更改的表格

### 表单

!--不学了，跳跃--

## 3.2 组件

### 导航条

### 分页条

## 3.3 插件

### 轮播图