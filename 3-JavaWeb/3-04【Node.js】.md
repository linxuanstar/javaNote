# 第一章 Node.js

Node.js 就是运行在服务端的 JavaScript。Node.js是一个事件驱动I/O服务端JavaScript环境，基于Google的V8引擎，V8引擎执行Javascript的速度非常快，性能非常好。

浏览器 是JavaScript的前端运行环境，Node.js 是JavaScript的后端运行环境，Node.js 中无法调用DOM和BOM等，浏览器内置API。

- 浏览器中的 JavaScript 学习路径： JavaScript 基础语法 + 浏览器内置 API（DOM + BOM） + 第三方库（jQuery、art-template 等）

- Node.js 的学习路径： JavaScript 基础语法 + Node.js 内置 API 模块（fs、path、http等）+ 第三方 API 模块（express、mysql 等）

## 1.1 下载

Node.js 的官网首页：https://nodejs.org/zh-cn/。 LTS 为长期稳定版，推荐安装 LTS 版本的 Node.js。

```apl
C:\Windows\System32>node -v
v12.14.0
C:\Windows\System32>npm -v
6.13.4
```

以往版本下载地址：https://nodejs.org/zh-cn/download/releases/

## 1.2 fs模块

fs 模块是 Node.js 官方提供的、用来操作文件的模块。它提供了一系列的方法和属性，用来满足用户对文件的操作需求。

- `fs.readFile()`：方法，用来读取指定文件中的内容 
- `fs.writeFile()`：方法，用来向指定的文件中写入内容

```js
// 导入 fs 模块来操作文件。这个提示不用管：文件是 CommonJS 模块; 它可能会转换为 ES 模块。
const fs = require('fs')

//读取文件内容
fs.readFile('D://a.txt', 'utf8', function (err, dataStr) {
    // 如果失败，打印失败结果
    if (err) {
        return console.log(err.message)
    }
    console.log(dataStr)
})
//  参数1：必选参数，字符串，表示文件的路径。
//  参数2：可选参数，表示以什么编码格式来读取文件。
//  参数3：必选参数，文件读取完成后，通过回调函数拿到读取的结果。


//向文件写入内容
fs.writeFile('D://b.txt', 'hello node', 'utf-8', function (err) {
    if (err) {
        console.log(err.message)
    }
})
//  参数1：必选参数，需要指定一个文件路径的字符串，表示文件的存放路径。
//  参数2：必选参数，表示要写入的内容。
//  参数3：可选参数，表示以什么格式写入文件内容，默认值是 utf8。
//  参数4：必选参数，文件写入完成后的回调函数。
```

在使用fs模块操作文件时，如果提供的操作路径是以 `./` 或 `../` 开头的相对路径时，很容易出现路径动态拼接错误的问题。这是因为代码在运行的时候，会以执行 node 命令时所处的目录，动态拼接出被操作文件的完整路径。

`fs.writeFile()` 方法只能用来创建文件，不能用来创建路径 ， 重复调用 `fs.writeFile()` 写入同一个文件，新写入的内容会覆盖之前的旧内容。

## 1.3 path模块

path 模块是 Node.js 官方提供的、用来处理路径的模块。它提供了一系列的方法和属性，用来满足用户对路径的处理 需求。

```js
const path = require('path')

const pathStr = path.join('/a', '/b/c', './d', 'e')
//输出 \a\b\c\d\e
console.log(pathStr) 

const path1 = 'a/b/c/a.html'  //文件夹    
// 获取文件后缀名 .html
console.log(path.extname(path1))
// 获取最后一个文件名  a.html
console.log(path.basename(path1))
```

今后凡是涉及到路径拼接的操作，都要使用 `path.join()` 方法进行处理。不要直接使用 + 进行字符串的拼接.

## 1.4 http模块

http 模块是 Node.js 官方提供的、用来创建 web 服务器的模块。通过 http 模块提供的 `http.createServer()` 方法，就能方便的把一台普通的电脑，变成一台 Web 服务器，从而对外提供 Web 资源服务。

```js
var http = require('http');

http.createServer(function (req, res) {
    // 设置HTTP状态值为200，内容类型为text/plain
    res.writeHead(200, { 'Content-Type': 'text/plain' });
    // 发送响应数据
    res.end('hello world');
}).listen(8888);

console.log('Server running at http://127.0.0.1:8888/');
```

## 1.5 模块化

编程领域中的模块化，就是遵守固定的规则，把一个大文件拆成独立并互相依赖的多个小模块。把代码进行模块化拆分的好处：提高了代码的复用性、提高了代码的可维护性、可以实现按需加载。

Node.js 中根据模块来源的不同，将模块分为了 3 大类，分别是：

- 内置模块（内置模块是由 Node.js 官方提供的，例如 fs、path、http 等）

- 自定义模块（用户创建的每个 .js 文件，都是自定义模块）

- 第三方模块（由第三方开发出来的模块，并非官方提供的内置模块，也不是用户创建的自定义模块，使用前需要先下载）

```js
//加载自定义模块
const fs =  require('./01_fs')
 
//加载内置模块
const path = require('path')
 
//加载第三方模块
const express = require('express')
```

和函数作用域类似，在自定义模块中定义的变量、方法等成员，只能在当前模块内被访问，这种模块级别的访问限制，叫做模块作用域。

在自定义模块中，可以使用 **module.exports**（简化为exports） 对象，将模块内的成员共享出去，供外界使用。 外界用 require() 方法导入自定义模块时，得到的就是 **module.exports** 所指向的对象

 Node.js 中的模块化规范 Node.js 遵循了 CommonJS 模块化规范，CommonJS 规定了模块的特性和各模块之间如何相互依赖。CommonJS 规定：

- 每个模块内部，module 变量代表当前模块。
- module 变量是一个对象，它的 exports 属性（即 module.exports）是对外的接口。
- 加载某个模块，其实是加载该模块的 module.exports 属性。require() 方法用于加载模块。

## 1.6 npm

npm, Inc公司旗下的https://www.npmjs.com/ ，它是全球最大的包共享平台，你可以从这个网站上搜索到任何你需要的包！ 

npm, Inc. 公司提供了一个包管理工具，我们可以使用这个包管理工具，从 https://registry.npmjs.org/ 服务器把需要 的包下载到本地使用。 这个包管理工具的名字叫做 **Node Package Manager（简称 npm 包管理工具）**，这个包管理工具随着 Node.js 的安 装包一起被安装到了用户的电脑上。

如果想在项目中安装指定名称的包，需要运行如下的命令：

```apl
# 安装指定包
npm install 第三方模块的完整名称
# 可简写为
npm i 第三方模块的完整名称
# 删除第三方模块
npm unstall 第三方模块的完整名称
```

```apl
# 把模块安装到项目目录，可以通过@指定安装包的版本，如：npm install vue-router@3.5.3
npm install moduleName
# 把模块安装到全局目录，具体位置可以通过npm config prefix指定
npm install moduleName -g
# -S 是 –save 的简写，意思是把模块和模块的版本添加到项目依赖 dependencies 中
npm install moduleName --save
# -D 是 –save-dev 的简写，意思是把模块和模块的版本添加到项目开发环境依赖 devDependencies 中
npm install moduleName --–save-dev
```

初次装包完成后，在项目文件夹下多一个叫做 node_modules 的文件夹和 package-lock.json 的配置文件。 其中： node_modules 文件夹用来存放所有已安装到项目中的包。

**基础设置**

```apl
# 使用npm get prefix查看npm全局模块的存放路径
npm get prefix
# 使用npm get cache查看npm缓存默认存放路径
npm get cache
# 设置全局模块的安装路径到 “node_global” 文件夹，
npm config set prefix "E:\node_js\nodejs\node_global"
# 设置缓存到 “node_cache” 文件夹
npm config set cache "E:\node_js\nodejs\node_cache"
```

 node 全局模块大多数都是可以通过命令行访问的，所以把`node_global`路径“`E:\node_js\nodejs\node_global`”加入到【系统变量 】下的【PATH】 变量中，方便直接使用命令行运行。

**下载包测试**

经过上面的步骤，nodejs下载的模块就会自动下载到我们自定义的目录，接下来我们测试一下是否更改成功。

```apl
# 全局安装。安装到之前设置的node_global目录下，同时nodejs会在node_global文件夹下创建node_modules子文件夹
# -g 的意思是将模块安装到全局，具体安装到磁盘哪个位置，要看 npm config prefix 的位置。
npm install express -g
```

**修改镜像**

```apl
# 查看当前使用的镜像路径
npm config get registry
# 更换npm为淘宝镜像
npm config set registry https://registry.npm.taobao.org/
# 检查镜像是否配置成功
npm config get registry
# 全局安装基于淘宝源的cnpm
npm install -g cnpm --registry=https://registry.npm.taobao.org
```
