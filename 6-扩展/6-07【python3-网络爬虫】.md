爬虫基本流程：

1. 发起请求：通过HTTP库向目标站点发起请求（ Request ），请求可以包含额外的headers等信息，等待服务器响应。
2. 获取响应内容：如果服务器能正常响应，会得到一个Response，Response的内容便是所要获取的页面内容，类型可能有HTML，Json字符串，二进制数据（如图片视频）等类型。
3. 解析内容：得到的内容可能是HTML，可以用正则表达式、网页解析库进行解析。可能是Json，可以直接转为Json对象解析，可能是二进制数据，可以做保存或者进一步的处理。
4. 保存数据：保存形式多样，可以存为文本，也可以保存至数据库，或者保存特定格式的文件。

# 第一章 Request请求

requests库是一个原生的HTTP库，发送原生的HTTP 1.1请求，无需手动为URL添加查询字串，也不需要对POST数据进行表单编码。

## 1.1 生成请求

requests库生成请求的代码非常便利：

```python
import requests

rq = requests.get('https://baidu.com')
print(rq)  # <Response [200]>
```

## 1.2 查看响应内容

```python
print('响应状态码：', rq.status_code)
print('编码：', rq.encoding)
print('请求头：', rq.headers)
print('实体：', rq.text)  # rq.content与rq.text类似，但返回值是bytes类型
```

Requests 会推测的网页文本编码，当推测出错时，需要手动指定encoding编码，避免返回的网页内容解析出现乱码。

chardet库的detect函数可检测给定字符串的编码。

```python
import chardet
chardet.detect(rq.content)  # {'encoding': 'utf-8', 'confidence': 0.99, 'language': ''}
```

## 1.3 请求头与响应头处理

requests库使用headers参数在GET请求中上传参数，参数形式为字典。

使用headers属性即可查看服务器返回的响应头，通常响应头返回的结果会与上传的请求参数对应。

```python
headers = {'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) ……'}  # 设置请求头
rq = requests.get(url, headers=headers)
rq.headers  # 返回的响应头
```

## 1.4 Timeout设置

为避免因等待服务器响应造成程序永久失去响应，通常需要给程序设置一个时间作为限制，超过该时间后程序将会自动停止等待。在requests库中通过设置timeout这个参数实现，超过该参数设定的秒数后，程序会停止等待。

```python
rq = requests.get(url, headers=headers, timeout=2.)
```

## 1.5 生成完成请求

使用requests库的request方法向网站“http://www.tipdm.com/”发送一个完整的GET请求，该请求包含链接、请求头、响应头、超时时间和状态码，并且编码应正确设置。

```python
url = 'https://baidu.com'  # 目标网址
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36'}
rq = requests.get(url, headers=headers, timeout=2.0)
rq.encoding = chardet.detect(rq.content)['encoding']  # 设置编码方式
```

# 第二章 解析网页

通过`requests.get(url)`获取的响应结果rq.text为字符串对象，若想从其中高效地提取出目标信息，需将其解析成具有明确结构的数据类型。

利用某些工具可将`rq.text` 解析为Document Object Model（文档对象模型）

有两种方式：

- 方案一：`lxml.etree + Xpath`
- 方案二：`beautifulsoup + CSS Selector`

## 2.1 Xpath

XML路径语言（XML Path Language），它是一种基于XML的树状结构，在数据结构树中找寻节点，确定XML文档中某部分位置的语言。XPath只能处理文档的DOM表现形式。

使用Xpath需要从lxml库中导入etree模块，然后使用其HTML类对需要匹配的HTML对象进行初始化

```python
import requests
import chardet
from lxml import etree

resp = requests.get('https://baidu.com')
resp.encoding = chardet.detect(resp.content)['encoding']

dom = etree.HTML(resp.text)  # 接收字符串
print(dom.xpath('/html/head/title')) # 打印地址 [<Element title at 0x241cdf84dc0>]
print(dom.xpath('/html/head/title/text()'))  # 取出来值 ['百度一下，你就知道']
```

Xpath常用匹配表达式如下

| **表达式** |           **说明**           |
| :--------: | :--------------------------: |
|  nodename  | 选取nodename节点的所有子节点 |
|     /      |   从当前节点选取直接子节点   |
|     //     |    从当前节点选取子孙节点    |
|     .      |         选取当前节点         |
|     ..     |     选取当前节点的父节点     |
|     @      |           选取属性           |

```python
print(dom.xpath('//title/text()'))  # 取出来值 ['百度一下，你就知道']
print(dom.xpath('/html//title/text()'))  # 取出来值 ['百度一下，你就知道']

print(dom.xpath('//body//div/a/text()'))  # 取出来所有的a标签的内容
print(dom.xpath('//body//div/a[1]/text()'))  # 取出来第一个a标签内容
print(dom.xpath('//body//div/a[@href="http://news.baidu.com"]/text()'))  # 取出来a标签中href属性为http://news.baidu.com的
```

Xpath可通过谓语来查找某个特定的节点或包含某个指定的值的节点，谓语被嵌在路径后的方括号中

| **表达式**                    | **说明**                                         |
| ----------------------------- | ------------------------------------------------ |
| /html/body/div[1]             | 选取属于body子节点下的第一个div节点              |
| /html/body/div[last()]        | 选取属于body子节点下的最后一个div节点            |
| /html/body/div[last()-1]      | 选取属于body子节点下的倒数第二个div节点          |
| /html/body/div[positon()<3]   | 选取属于body子节点下的下前两个div节点            |
| /html/body/div[@id]           | 选取属于body子节点下的带有id属性的div节点        |
| /html/body/div[@id=“content”] | 选取属于body子节点下的id属性值为content的div节点 |
| /html /body/div[xx>10.00]     | 选取属于body子节点下的xx元素值大于10的节点       |

有时仅掌握了目标对象的部分特征，当需要模糊搜索该类对象时，可使用Xpathon功能函数来实现定位查找。

| **功能函数** | **示例**                                       | **说明**                       |
| ------------ | ---------------------------------------------- | ------------------------------ |
| starts-with  | //div[starts-with(@id,”co”)]                   | 选取id值以co开头的div节点      |
| contains     | //div[contains(@id,”co”)]                      | 选取id值包含co的div节点        |
| and          | //div[contains(@id,”co”)andcontains(@id,”en”)] | 选取id值包含co和en的div节点    |
| text()       | //li[contains(text(),”first”)]                 | 选取节点文本包含first的div节点 |

使用text方法可以提取某个单独标签下的文本。

使用string方法提取出定位到的子节点及其子孙节点下的全部文本。

使用@获取标签属性值。

```python
xpath("//a/text()")  #获取a标签下的文本
xpath("//a//text()")  #获取a标签以及子标签中的内容
xpath(“//a/@href”)  # 获取a标签中的连接，即获取标签属性值 （位置/@属性）
```

## 2.2 Beautiful Soup解析网页

Beautiful Soup 是一个可以从HTML或XML文件中提取数据的Python库，可通过转换器实现常用的文档导航、查找、修改等操作。Beautiful Soup不仅支持Python标准库中的HTML解析器，还支持一些第三方的解析器。

| **解析器**      | **语法格式**                                                 | **优点**                                              | **缺点**                                   |
| --------------- | ------------------------------------------------------------ | ----------------------------------------------------- | ------------------------------------------ |
| Python标准库    | BeautifulSoup(markup, "html.parser")                         | Python的内置标准库执行速度适中文档容错能力强          | Python2.7.3或3.2.2前的版本中文档容错能力差 |
| lxml HTML解析器 | BeautifulSoup(markup, "lxml")                                | 速度快文档容错能力强                                  | 需要安装C语言库                            |
| lxml XML解析器  | BeautifulSoup(markup, ["lxml-xml"])BeautifulSoup(markup, "xml") | 速度快唯一支持XML的解析器                             | 需要安装C语言库                            |
| html5lib        | BeautifulSoup(markup, "html5lib")                            | 最好的容错性以浏览器的方式解析文档生成HTML5格式的文档 | 速度慢不依赖外部扩展                       |

创建BeautifulSoup对象

beautifulsoup4库中导入BeautifulSoup类对网页数据进行解析。

```python
BeautifulSoup("<html>data</html>")  # 通过字符串创建
BeautifulSoup(open("index.html"))  # 通过HTML文件创建
```



```python
from bs4 import BeautifulSoup
with open('html_doc.html') as html_doc:
soup = BeautifulSoup(html_doc,features='lxml') # 用lxml解释器进行解析
```

定位目标内容：获取某些标签内容

```python
soup.head  # 获取网页的header标签
soup.body  # 获取网页的header标签
soup.a  # 获取网页的第一个a标签

```

定位目标内容：CSS Selector

CSS (Cascading Style Sheets)是一种样式表语言，是所有浏览器内置的，用于描述以HTML或XML编写的文档的外观和样式。
CSS Selector用于选择、定位HTML/XML文档中样式化/结构化的元素，可类比于Xpath的作用。

```python
soup.select(目标标签路径)
soup.select('title') # 获取soup对象中的所有title标签

```

```python
soup.select('p')  #查找所有p标签
soup.select('body a') # 查找body标签下面的a标签,相对路径
soup.select('body > a') #查找body标签下面的直接a标签

```

```python
通过上下级关系或位置定位标签
soup.select("p:nth-of-type(2)") #第二个p标签
soup.select('p > a:nth-child(2)')  # 选择父元素为 <p> 元素的第二个<a>元素
soup.select('p > a:nth-last-child(2)') # 选择父元素为 <p> 元素的倒数第二个<a>元素
soup.select('p > a:last-child')  # 选择父元素为 <p> 元素的最后一个<a>元素
soup.select('p > a:first-child') # 选择父元素为 <p> 元素的第一个<a>元素

```

```python
通过属性及其取值定位标签
soup.select(".sister")  # 通过CSS的类名查找(即class属性为sister的所有标签)
soup.select('a[href="http://example.com/elsie"]') # 通过属性的值来查找
soup.select('a[href^="http://example.com/"]') # 通过属性的值来查找
soup.select("p >  #link1") # 选择父元素为 <p>且id属性为link1的所有标签

```

```python
获取标签中的内容或属性值
soup.select('p')[0].text # 获得标签的中的内容（字符串）
soup.select('p')[1].get('class') # 获得标签的id属性值

```

通过find_all查找目标元素

使用方式：`BeautifulSoup.find_all(name,attrs,recursive,string,**kwargs)`

| **参数**  | **说明**                                                     |
| --------- | ------------------------------------------------------------ |
| name      | 接收string。表示查找所有名字为name的tag，字符串对象会被自动忽略掉，搜索name参数的值可以使用任一类型的过滤器：字符串、正则表达式、列表、方法或True。无默认值 |
| attrs     | 接收string。表示查找符合CSS类名的tag，使用class做参数会导致语法错误，从BeautifulSoup的4.1.1版本开始，可以通过class_参数搜索有指定CSS类名的tag。无默认值 |
| recursive | 接收Built-in。表示是否检索当前tag的所有子孙节点。默认为True，若只想搜索tag的直接子节点，可将该参数设为False |
| string    | 接收string。表示搜索文档中匹配传入的字符串的内容，与name参数的可选值一样，string参数也接收多种过滤器。无默认值 |
| **kwargs  | 若一个指定名字的参数不是搜索内置的参数名，搜索时会把该参数当作指定名字tag的属性来搜索 |

```python
soup.find_all('p')  # 根据名称查找标签
soup.find_all(id='link1') # 根据属性值查找标签

```

## 2.3 **元素定位方式**

# 第三章 存储数据

## 3.1 chrome开发者工具

chrome开发者工具各面板功能如下。

| **面板**                | **说明**                                                     |
| ----------------------- | ------------------------------------------------------------ |
| 元素面板（Elements）    | 该面板可查看渲染页面所需的HTML、CSS和DOM（DocumentObject Model）对象，并可实时编辑这些元素调试页面渲染效果 |
| 控制台面板（Console）   | 该面板记录各种警告与错误信息，并可作为shell在页面上与JavaScript交互 |
| 源代码面板（Sources）   | 该面板中可以设置断点调试JavaScript                           |
| 网络面板（Network）     | 该面板可查看页面请求、下载的资源文件及优化网页加载性能。还可查看HTTP的请求头、响应内容等 |
| 性能面板（Performance） | 原旧版chrome中的时间线面板（Timeline），该页面展示页面加载时所有事件花费时长的完整分析 |
| 内存面板（Memory）      | 原旧版chrome中的分析面板（Profiles），提供比性能面板更详细的分析，如可跟踪内存泄露等 |
| 应用面板（Application） | 原旧版chrome中的资源面板（Profiles），该面板可检查加载的所有资源 |
| 安全面板（Security）    | 该面板可调试当前网页的安全和认证等问题并确保网站上已正确地实现HTTPS |
| 审查面板（Audits）      | 该面板对当前网页的网络利用情况、网页性能方面进行诊断，并给出优化建议 |

## 3.2 存储数据

对于爬取到的数据，我们一般会利用pandas处理成dataframe的格式，此时应注意要保证各字段元素个数一致。

当数据转成dataframe后，再根据业务需求将dataframe写入磁盘，常用的形式有csv文件、数据库文件或json文件。

将数据存储为JSON文件的过程为一个编码过程，编码过程常用dump函数, 将Python对象转换为JSON对象，并通过fp文件流将JSON对象写入文件内。

# 第四章 案例

## 4.1 爬取淘宝

```python
url = "https://uland.taobao.com/sem/tbsearch?refpid=mm_26632258_3504122_32538762&keyword=%E5%8C%85%E5%8C%85&clk1=8770d7f6cdda9fb3a62239ab2331e522&upsId=8770d7f6cdda9fb3a62239ab2331e522&spm=a2e0b.20350158.31919782.1&pid=mm_26632258_3504122_32538762&union_lens=recoveryid%3A201_33.61.131.248_14791355_1680917201800%3Bprepvid%3A201_33.61.131.248_14791355_1680917201800&pnum="


# 获取响应数据
def get_html(url):
    browser = webdriver.Chrome()
    browser.get(url)
    response = browser.page_source
    browser.close()
    return response


# 将响应数据转为DOM树将需要的数据进行解析
def parser(resp):
    # 转为DOM树
    dom = etree.HTML(resp)
    # 将所有需要的数据放到li_list列表里面
    li_list = dom.xpath('//*[@id="mx_5"]/ul/li')
    # 设置结果为package_info列表
    package_info = []
    # 循环列表变成字典放到package_info列表里面
    for li in li_list:
        # 获取商品名称栏
        title = li.xpath('./a/div[1]/span/text()')[0]
        # 获取价格栏
        price = li.xpath('./a/div[2]/span[2]/text()')[0]
        # 获取店铺栏
        shop = li.xpath('./a/div[3]/div/text()')[0]
        # 获取月销栏
        sales = li.xpath('./a/div[4]/div[2]/text()')[0].split(" ")[1]
        # 以字典的格式向package_info列表里面添加
        package_info.append({'商品名称': title, '价格': price, '店铺': sales, '销量': sales})
    return package_info


# 存储数据
def save_info_csv(package_info):
    # 直接转为DataFrame格式，不需要添加索引
    df = pd.DataFrame(package_info)
    # 以追加的方式，不需要索引：mode='a', index=False
    df.to_excel('./package1.xlsx', index=False)


if __name__ == '__main__':
    resp = ""
    for i in range(1, 3):
        # 获取链接
        url += "i"
        # 获取响应结果
        resp += get_html(url)
    # 列表里面存储字典获取数据
    package_info = parser(resp)
    # 存储至CSV文件
    save_info_csv(package_info)

```

