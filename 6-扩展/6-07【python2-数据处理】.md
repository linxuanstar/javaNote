# 第一章 Pandas数据处理

## 1.1 读取文件

Pandas需要先读取表格类型的数据，然后进行分析。

| 数据类型      | 说明                            | Pandas读取方法 |
| ------------- | ------------------------------- | -------------- |
| csv、tsv、txt | 用逗号分割、tab分割的纯文本文件 | pd.read_csv    |
| excel         | 微软xls或者xlsx文件             | pd.read_excel  |
| mysql         | 关系型数据库表                  | pd.read_sql    |

**读取csv**

```python
import pandas as pd

data = pd.read_csv("../data/GoodsOrder.csv")

# 查看前几行数据
print(data.head())
#    ID  Goods
# 0   1  柑橘类水果
# 1   1   人造黄油
# 2   1    即食汤
# 3   1  半成品面包
# 4   2     咖啡

# 查看数据的形状，返回(行数、列数) (43367, 2)
print(data.shape)
# 查看列名列表
print(data.columns)  # Index(['ID', 'Goods'], dtype='object')
# 查看索引列
print(data.index)  # RangeIndex(start=0, stop=43367, step=1)
# 查看每列的数据类型
print(data.dtypes)
# ID        int64
# Goods    object
# dtype: object
```

**读取txt文件，自己指定分隔符、列名称**

```apl
# txt文件
1	后羿	5986	1784	396	336	remotely	archer
2	马可波罗	5584	200	362	344	remotely	archer
3	鲁班七号	5989	1756	400	323	remotely	archer
4	李元芳	5725	1770	396	340	remotely	archer
5	孙尚香	6014	1756	411	346	remotely	archer
6	黄忠	5898	1784	403	319	remotely	archer
7	狄仁杰	5710	1770	376	338	remotely	archer
8	虞姬	5669	1770	407	329	remotely	archer
9	成吉思汗	5799	1742	394	329	remotely	archer
10	百里守约	5611	1784	410	329	remotely	archer
```

```python
import pandas as pd

def read_txt():
    """
    测试读取txt文件
    :return:
    """
    data = pd.read_csv(
        filepath_or_buffer="../data/test.txt",  # 指定路径
        sep="\t",  # 指定分隔符
        header=None,  # 没有列名称
        names=["ID", "值1", "值2", "值3", "值4", "值5", "值6", "值7"]  # 创建列名称
    )
    print(data)
```

**读取excel文件**

```python
import pandas as pd

# 测试读取excel文件
def read_excel():
    """
    测试读取excel文件
    :return:
    """
    grade = pd.read_excel("../data/grade.xlsx")
    print(grade.head())
```

**读取mysql文件**

```python
import pandas as pd
import pymysql

# 测试读取mysql
def read_mysql():
    conn = pymysql.connect(
        host="localhost",
        user="root",
        password="root",
        database="linxuan",
        charset="utf8"
    )
    mysql_page = pd.read_sql("SELECT * FROM transactions", con=conn)
    print(mysql_page)

```

## 1.2 Pandas数据结构

Pandas数据结构有两种：DataFrame和Series

![](..\图片\6-07【python2-数据处理】\1-1.png)

### 1.2.1 Series

Series：一维数据，一行或者一列。Series是一种类似于一维数组的对象，它由一组数据（不同数据类型）以及一组与之相关的数据标签（即索引）组成。

三种创建的方法：

1. 数据列表

   ```python
   import pandas as pd
   
   series = pd.Series([1, 'a', 2, 3, 4])
   
   # 左侧是索引，右侧是数据
   # 0    1
   # 1    a
   # 2    2
   # 3    3
   # 4    4
   # dtype: object
   print(series)
   
   # 获取索引 RangeIndex(start=0, stop=5, step=1)
   print(series.index)
   
   # 获取数据 [1 'a' 2 3 4]
   print(series.values)
   ```

2. 创建一个具有标签索引的Series

   ```python
   # 创建Series第二种方法
   def createSeries_2():
       """
       创建Series第二种方法
       :return:
       """
       seriest2 = pd.Series([1, 'a', 2, 3], index=['a', 'b', 'c', 'd'])
       print(seriest2)
   ```

3. 根据Python字典创建Series

   ```python
   # 创建Series第三种方法
   def createSeries_3():
       """
       创建Series第三种方法
       :return:
       """
       dict_my = {
           "name1": "linxuan",
           "name2": "chenmuyang",
           "name3": "yunshuangduan"
       }
       series3 = pd.Series(dict_my)
       print(series3.index) # Index(['name1', 'name2', 'name3'], dtype='object')
   ```

查询Series数据方法非常的类似于Python的字典

```python
# 查询Series
def showSeries():
    """
    查询Series
    :return:
    """
    # 创建一个自定义索引的Series
    series = pd.Series([1, 'a', 2, 3], index=['a', 'b', 'c', 'd'])
    # 打印索引为a的值
    print(series['a'])  # 1
    # 打印类型
    print(type(series['a']))  # <class 'int'>
    # 打印索引为a和b的值
    print(series[['a', 'b']])  # 是一个Series
    # 打印类型
    print(type(series[['a', 'b']]))  # <class 'pandas.core.series.Series'>
```

### 1.2.2 DataFrame

DataFrame：二维数据，整个表格，多行多列。DataFrame是一个表格型的数据结构。

* 每列可以是不同的值类型（数值、字符串、布尔值）
* 既有行索引index，也有列索引columns
* 可以被看做由Series组成的字典。

创建方式：

1. 使用文件来创建：`pd.read_csv`、`pd.read_excel`。

2. 使用多个字典序列来创建

   ```python
   # 创建DataFrame
   def createDataFrame_1():
       dict_my = {
           "语文": [99, 54, 78, 80],
           "数学": [34, 56, 88, 67],
           "英语": [45, 68, 90, 77]
       }
       df = pd.DataFrame(dict_my)
       # 打印一下
       print(df)
       # 看一下每一列的类型
       print(df.dtypes)
       # 看一下列名称
       print(df.columns)  # Index(['语文', '数学', '英语'], dtype='object')
       # 看一下索引名称
       print(df.index)  # RangeIndex(start=0, stop=4, step=1)
   ```

```python
import pandas as pd

data = pd.read_csv("D:\\比赛\\大数据\\样题\\SecondhandHouseV3.csv")
# 看一下前几行
print(data.head())
# 看一下行列
print(data.shape)
# 看一下所有的
print(data)
# 拿到户型到总价的数据
x = data.loc[:, '户型': '总价']
print(x)
# 拿到最后一列数据
y = data.iloc[:, -1]
print(data)
```

```apl
       户型      建筑面积  朝向   楼层   装修  ...  区域     学校      总价           单价   房龄
0  2室2厅1卫  104.66平米   南   高层   毛坯  ...  城关  附件无学校  191.0万   18250元/平米   2.0
1  1室1厅1卫   48.86平米  南北   中层  简装修  ...  城关  附件有学校   63.0万   12894元/平米   2.0
2  1室3厅1卫      80平米  南北   低层  简装修  ...  城关  附件无学校  118.0万   14750元/平米   NaN
3  3室2厅2卫  118.00平米  南北  中楼层   暂无  ...  安宁  附件无学校  142.0万    12034元/平米  NaN
4  3室2厅1卫   94.66平米   南   中层   毛坯  ...  安宁  附件无学校  110.0万   11621元/平米   7.0

[5 rows x 16 columns]
```

```apl
(8953, 16)
```

```apl
          户型      建筑面积  朝向   楼层   装修  ...   区域     学校      总价           单价    房龄
0     2室2厅1卫  104.66平米   南   高层   毛坯  ...   城关  附件无学校  191.0万   18250元/平米    2.0
1     1室1厅1卫   48.86平米  南北   中层  简装修  ...   城关  附件有学校   63.0万   12894元/平米    2.0
2     1室3厅1卫      80平米  南北   低层  简装修  ...   城关  附件无学校  118.0万   14750元/平米    NaN
3     3室2厅2卫  118.00平米  南北  中楼层   暂无  ...   安宁  附件无学校  142.0万    12034元/平米   NaN
4     3室2厅1卫   94.66平米   南   中层   毛坯  ...   安宁  附件无学校  110.0万   11621元/平米    7.0
...
```

```apl
          户型      建筑面积  朝向   楼层   装修  ...  建筑结构 建筑类别   区域     学校      总价
0     2室2厅1卫  104.66平米   南   高层   毛坯  ...    平层   板楼   城关  附件无学校  191.0万
1     1室1厅1卫   48.86平米  南北   中层  简装修  ...    平层   板楼   城关  附件有学校   63.0万
2     1室3厅1卫      80平米  南北   低层  简装修  ...    平层  NaN   城关  附件无学校  118.0万
3     3室2厅2卫  118.00平米  南北  中楼层   暂无  ...   NaN  NaN   安宁  附件无学校  142.0万
4     3室2厅1卫   94.66平米   南   中层   毛坯  ...    平层   板楼   安宁  附件无学校  110.0万
```

```python
# 索引列显示ID
data = pd.read_csv("D:\\比赛\\大数据\\样题\\SecondhandHouseV3.csv", index_col = "ID")

ID      户型      建筑面积  朝向   楼层   装修  ...  区域     学校      总价           单价   房龄
0  2室2厅1卫  104.66平米   南   高层   毛坯  ...  城关  附件无学校  191.0万   18250元/平米   2.0
1  1室1厅1卫   48.86平米  南北   中层  简装修  ...  城关  附件有学校   63.0万   12894元/平米   2.0
2  1室3厅1卫      80平米  南北   低层  简装修  ...  城关  附件无学校  118.0万   14750元/平米   NaN
3  3室2厅2卫  118.00平米  南北  中楼层   暂无  ...  安宁  附件无学校  142.0万    12034元/平米  NaN
4  3室2厅1卫   94.66平米   南   中层   毛坯  ...  安宁  附件无学校  110.0万   11621元/平米   7.0
```

### 1.2.3 DataFrame查询Series

* 如果只查询一列，那么返回的是pd.Series

  ```python
  def showDataFrame():
      dict_my = {
          "语文": [99, 54, 78, 80],
          "数学": [34, 56, 88, 67],
          "英语": [45, 68, 90, 77]
      }
      df = pd.DataFrame(dict_my)
      # 打印语文列 同时会有索引
      print(df['语文'])
      print(type(df['语文']))  # <class 'pandas.core.series.Series'>
  ```

* 如果查询的是多行多列，那么返回的是pd.DataFrame

  ```python
  def showDataFrame():
      dict_my = {
          "语文": [99, 54, 78, 80],
          "数学": [34, 56, 88, 67],
          "英语": [45, 68, 90, 77]
      }
      df = pd.DataFrame(dict_my)
      # 查询多列，类型是DataFrame
      print(df[['语文', '数学']])
      print(type(df[['语文', '数学']]))  # <class 'pandas.core.frame.DataFrame'>
  
  ```

* 查询一行，结果是一个pd.Series

  ```python
  # 取出来第一行的数据
  print(df.loc[0])
  # 打印第二行的数据的类型
  print(type(df.loc[1]))  # <class 'pandas.core.series.Series'>
  ```

* 查询多行，结果是一个pd.DataFrame

  ```python
  # 取出来第一行到第三行的数据
  print(df.loc[0: 2])
  # 查询所有的行，列没有指定 那就是所有的列
  print(df.loc[:])  # <class 'pandas.core.frame.DataFrame'>
  # 查询所有的行和语文列，也就是只查询语文列
  print(df.loc[:, '语文'])  # <class 'pandas.core.series.Series'>
  # 打印类型
  print(type(df.loc[0: 2]))
  ```

## 1.3 数据查询

查询之前我们要明确数据的降维：DataFrame>Series>值。

数据来添加一下：

```python
import pandas as pd

# 获取数据
df = pd.read_excel("../data/grade1.xlsx")
# 设置索引，这样就不会额外添加索引了，以学号为索引
df.set_index('学号', inplace=True)

# 数据清洗
# 将语文列后面的分去掉
df['语文'] = df['语文'].str.replace("分", '').astype("float64")

# 打印数据
print(df)
# 打印每一列的类型
print(df.dtypes)
```

```apl
# 打印数据如下
学号       语文   数学  英语  物理 化学  生物
420417241  85.2  95.6  99.3  95  91.0  75.0
420417242  75.1  95.1  99.3  95  85.0  95.4
420417243  74.1  90.1  94.4  87  91.0  79.0
420417244  66.1  95.8  87.4  94  88.0  68.0
420417245  77.2  85.6  95.1  94  75.0  70.6
[99 rows x 6 columns]
```

```apl
# 打印类型如下：
语文    float64
数学    float64
英语    float64
物理      int64
化学    float64
生物    float64
dtype: object
```

Pandas查询数据的方法如下：

1. `df.loc`方法，根据行、列的标签值进行查询
2. `df.iloc`方法，根据行、列的数字位置查询`iloc()`中的`i`指的是integer，意为integer position的意思。
3. `df.where`方法
4. `df.query`方法

Pandas使用`df.loc`查询数据的方法

1. 使用单个label值查询数据。行或者列，都可以传入单个值，这样就能够实现精确匹配。

   ```python
   # 得到单个值，传入行和列
   df.loc[420417243, '语文']
   
   # 传入单行多列，返回值是Series <class 'pandas.core.series.Series'>
   df.loc[420417245, ['语文', '数学']]
   语文    77.2
   数学    85.6
   Name: 420417245, dtype: float64
   ```

2. 使用值列表批量查询

   ```python
   # 传入多行，单列。这样返回值是Series
   df.loc[[420417243, 420417244, 420417245], "语文"]
   学号
   420417243    74.1
   420417244    66.1
   420417245    77.2
   Name: 语文, dtype: float64
   
   # 传入多行多列，返回值是DataFrame
   df.loc[[420417243, 420417244, 420417245], ["语文", "数学", "英语"]]
   学号       语文    数学    英语               
   420417243  74.1  90.1  94.4
   420417244  66.1  95.8  87.4
   420417245  77.2  85.6  95.1
   ```

3. 使用数值区间进行范围查询。区间既包含开始，也包含结束。

   ```python
   # 行index按照区间，类型<class 'pandas.core.series.Series'>
   df.loc[420417243: 420417245, '语文']
   学号
   420417243    74.1
   420417244    66.1
   420417245    77.2
   Name: 语文, dtype: float64
   
   # 行和列都按照区间查询
   df.loc[420417243: 420417245, '语文': '数学']
    学号       语文    数学           
   420417243  74.1  90.1
   420417244  66.1  95.8
   420417245  77.2  85.6
   ```

4. 使用条件表达式查询。bool列表的长度需要等于行数或者列数

   ```python
   # 简单条件查询，语文分数低于68分的列表
   df.loc[df['语文'] < 68, :]
   学号      语文    数学  英语  物理 化学  生物                                     
   420417244  66.1  95.8  87.4  94  88.0  68.0
   420417319  67.1  88.1  90.2  84  75.0  94.3
   420417332  66.6  70.5  87.4  95  95.8  93.0
   
   # 查询一下，结果都是Bool表达式
   df['语文'] < 68
   学号
   420417241    False
   420417244     True
   420417245    False
                ...  
   420417335    False
   Name: 语文, Length: 99, dtype: bool
   
   # 复杂条件查询，用&符号合并，每个条件判断都需要带上括号
   # df.loc[(df['语文'] < 68) & (df['数学'] > 90), :]
   df.loc[(df['语文'] < 68) & (df['数学'] > 90)]
   学号      语文   数学   英语  物理  化学  生物                         
   420417244  66.1  95.8  87.4  94  88.0  68.0
   ```

5. 调用函数查询

   ```python
   # 使用Lambda表达式查询
   df.loc[lambda df: (df['语文'] < 68) & (df['数学'] > 90), :]
   学号        语文    数学    英语  物理    化学    生物                      
   420417244  66.1  95.8  87.4  94  88.0  68.0
   ```

## 1.4 新增数据列

```python
import pandas as pd

# 获取数据
df = pd.read_excel("E:\PyCharm\PycharmProjects\Python网络爬虫_project\data\grade1.xlsx")
# 设置索引，这样就不会额外添加索引了，以学号为索引
df.set_index('学号', inplace=True)

print(df)

# 打印数据大致如下
学号       语文    数学   英语  物理 化学  生物              
420417241  85.2分  95.6  99.3  95  91.0  75.0
420417242  75.1分  95.1  99.3  95  85.0  95.4
420417243  74.1分  90.1  94.4  87  91.0  79.0
420417244  66.1分  95.8  87.4  94  88.0  68.0
420417245  77.2分  85.6  95.1  94  75.0  70.6
```

在进行数据分析的时候，经常需要按照一定的条件来创建新的数据列，然后进行进一步的分析。一共有四种方法：

1. 直接赋值

   ```python
   # 添加一列，名字叫做主科总成绩：语文 + 数学 + 英语
   
   # 数据先清洗一下，将分去掉
   df['语文'] = df['语文'].str.replace("分", "").astype("float64")
   
   # 新增加一列，其实就是三个Series增加
   df['主科总成绩'] = df['语文'] + df['数学'] + df['英语']
   df.head()
   
   学号       语文  数学  英语  物理  化学  生物  主科总成绩                             
   420417241  85.2  95.6  99.3  95  91.0  75.0  280.1
   420417242  75.1  95.1  99.3  95  85.0  95.4  269.5
   420417243  74.1  90.1  94.4  87  91.0  79.0  258.6
   420417244  66.1  95.8  87.4  94  88.0  68.0  249.3
   420417245  77.2  85.6  95.1  94  75.0  70.6  257.9
   ```

2. `df.apply`方法。第一个参数是函数，另一个参数是axis = 0 ? 1，0代表索引行，1代表列。

   ```python
   # 定义函数
   def get_grade(x):
       if x['主科总成绩'] > 280:
           return "优秀"
       elif x['主科总成绩'] > 240:
           return "及格"
       return "不及格"
   
   # 根据函数来添加一列
   df.loc[:, "等级"] = df.apply(get_grade, axis=1)
   
   # 统计一下等级列
   df['等级'].value_counts()
   不及格    50
   及格     48
   优秀      1
   Name: 等级, dtype: int64
   ```

3. `df.assign`方法。不会修改本身，会返回一个新的对象

   ```python
   # 可以同时添加多个列
   df_change = df.assign(
       语文修改=lambda x: x["语文"] - 1,
       数学修改=lambda x: x["数学"] - 1
   )
   
   # 打印一下
   print(df_change.head())
   学号      语文    数学  英语  物理  化学 生物 语文修改 数学修改                   
   420417241  85.2  95.6  99.3  95  91.0  75.0  84.2  94.6
   420417242  75.1  95.1  99.3  95  85.0  95.4  74.1  94.1
   420417243  74.1  90.1  94.4  87  91.0  79.0  73.1  89.1
   420417244  66.1  95.8  87.4  94  88.0  68.0  65.1  94.8
   420417245  77.2  85.6  95.1  94  75.0  70.6  76.2  84.6
   ```

4. 按照条件选择分组分别赋值。按照条件先选择数据，然后对这部分数据赋值新列。

   ```python
   # 创建空列，这是第一种创建新列的方法
   df["偏科"] = ""
   
   # 差值大于20偏科
   df.loc[(df["语文"] - df["数学"] <= -20) | (df["语文"] - df["数学"] >= 20), "偏科"] = "是"
   df.loc[(df["语文"] - df["数学"] > -20) & (df["语文"] - df["数学"] < 20), "偏科"] = "否"
   ```

## 1.5 数据统计函数

汇总类统计

```python
# 提取所有数字列的统计结果
df.describe()
       语文        数学          英语         物理         化学       生物
count  99.000000  99.00000   99.000000  99.000000  99.000000  99.000000 # 汇总
mean   73.256566  83.00202   84.085859  87.070707  76.954545  80.321212 # 平均数
std     3.844720   7.55486    9.624703   6.649003   8.547792   8.179670 # 标准差
min    66.100000  60.20000   60.000000  65.000000  60.000000  60.000000 # 最小值
25%    70.700000  78.50000   78.300000  83.000000  70.800000  75.000000
50%    72.700000  83.70000   86.000000  87.000000  76.000000  81.000000
75%    75.050000  88.05000   91.250000  93.500000  83.700000  86.650000
max    86.000000  95.80000  100.000000  96.000000  95.800000  95.800000 # 最大值

# 平均数
df["语文"].mean()
73.25656565656564

# 最大值
df["语文"].max()
86.0

# 最小值
df["语文"].min()
66.1
```

唯一去重和按值计数

```python
# 所有取值
df["语文"].unique()
array([85.2, 75.1, 74.1, 66.1, 77.2, 76.2, 74.8, 72.7, 69.9, 76.4, 73.5,
       72. , 75.6, 71.9, 82. , 74.2, 71.2, 69.2, 86. , 74.3, 79.4, 77.7,
       70.7, 74.9, 78.9, 81.3, 72.2, 84.7, 69.4, 70.6, 68. , 76.1, 76.9,
       72.6, 71.8, 73.6, 68.1, 72.8, 75.4, 76.6, 68.2, 75. , 81.2, 67.1,
       70.8, 68.4, 73.3, 71.5, 77.3, 66.6, 69.6])

# 统计值
df["语文"].value_counts()
71.9    6
69.2    5
76.4    4
69.9    4
72.0    4
····
Name: 语文, dtype: int64
```



## 1.4 数据清洗

**记录重复**

记录重复是指数据中一个或多个记录的属性的值完全相同。

1. list列表去重

   ```python
   my_list1 = pd.read_excel(r'C:\Users\林轩\Desktop\河北地质大学华信学院离校学生情况统计表.xlsx')
   # 打印宿舍号列的长度
   print(len(my_list1['宿舍号']))
   
   # 记录去重后的列表
   my_list2 = []
   for i in my_list1['宿舍号']:
       if i not in my_list2:
           my_list2.append(i)
   # 打印去重后列表的长度
   print(len(my_list2))
   ```

2. set集合去重

   ```python
   my_list1 = pd.read_excel(r'C:\Users\林轩\Desktop\河北地质大学华信学院离校学生情况统计表.xlsx')
   # 打印宿舍号列的长度
   print("之前的长度：", len(my_list1['宿舍号']))
   
   def test02(list1):
       # 记录去重后的集合
       my_set1 = set()
       for i in list1['宿舍号']:
           my_set1.add(i)
       # 打印去重后列表的长度
       print("去重后列表的长度：", len(my_set1))
   
   
   if __name__ == '__main__':
       test02(my_list1)
   
   ```

   ```python
   import pandas as pd
   
   my_list1 = pd.read_excel(r'C:\Users\林轩\Desktop\河北地质大学华信学院离校学生情况统计表.xlsx')
   # 打印宿舍号列的长度
   print("之前的长度：", len(my_list1['宿舍号']))
   
   def test02(list1):
       # 记录去重后的集合
       my_set1 = set(list1)
       # 打印去重后列表的长度
       print("去重后列表的长度：", len(my_set1))
   
   
   if __name__ == '__main__':
       test02(my_list1)
   ```

3. pandas提供了一个名为`drop_duplicates`的去重方法。该方法只对`DataFrame`或`Series`类型有效。

   `drop_duplicates()`方法的基本使用格式和参数说明如下：`pandas.DataFrame.drop_duplicates(subset-None, keep-'first', inplace-False)`

   * subset接收str或sequence。表示进行去重的列。默认为None   
   * keep接收特定str。表示重复时保留第几个数据。first:保留第一个。last:保留最后一个。false:只要有重复都不保留。
   * inplace默认为first接收bool。表示是否在原表上进行操作。默认为False

   ```python
   my_list1 = pd.read_excel(r'C:\Users\林轩\Desktop\河北地质大学华信学院离校学生情况统计表.xlsx')
   # 打印宿舍号列的长度
   print("之前的长度：", len(my_list1['宿舍号']))
   
   def test03(list1):
       print("去重后列表的长度：", len(list1['宿舍号'].drop_duplicates()))
       
   if __name__ == '__main__':
       test03(my_list1)
   ```

**属性内容重复**

属性内容重复是指数据中存在一个或多个属性名称不同，但数据完全相同。当需要去除连续型属性重复时,可以利用属性间的相似度，去除两个相似度为1的属性的其中一个：

```python
corr_ = data[['品牌标签', '仓库标签']].corr(method='kendall')

print('kendall相似度为: \n', corr_)
```

除了使用相似度矩阵进行属性去重之外,可以通过pandas库的`DataFrame.equals()`方法进行属性去重。基本使用格式和参数说明如下:`pandas.DataFrame.equals(other)`。other说明接收Series或DataFrame。表示要与第一个进行比较的另一个Series或DataFrame,无默认值

# 第二章 Matplotlib

Matplotlib是Python下的2D绘图库，也是最著名的Python绘图库。虽然Matlpotlib的代码库很庞大，但是可以通过简单的概念框架和重要的知识来理解掌握。Matplotlib图像可以分为如下4层结构。

- canvas (画板)。位于最底层，导入Matplotlib库时就自动存在。
- figure (画布)。建立在canvas之上，从这一层就能开始设置其参数。
- axes (子图)。将figure分成不同块，实现分面绘图。
- 图表信息(构图元素)。添加或修改axes上的图形信息,优化图表的显示效果。

为了方便快速绘图, Matplotlib通过pyplot模块提供了一套与Matlab类似的命令API,这些API对应图形的一个个图形元素(如坐标轴、曲线、文字等),并以此对该图形元素进行操作,而不影响其他部分。创建好画布后,只需调用pyplot模块所提供的函数,仅几行代码就可以实现添加、修改图形元素或在原有图形上绘制新图形。

根据Matplotlib图像的4层图像结构，pyplot模块绘制图形基本都遵循一个流程，使用这个流程可以完成大部分图形的绘制。pyplot模块基本绘图流程主要分为3个部分，如下图所示。

![](..\图片\6-07【python2-数据处理】\2-1.png)

1. 导入模块。绘图之前，需要先导入包含相应函数的模块。对于pyplot模块，一般使用如下风格导入。

   ```python
   import numpy as np
   import matplotlib.pyplot as plt
   ```

2. 创建画布与创建子图。第一部分主要是构建出一张空白的画布，如果需要同时展示几个图形，可将画布划分为多个部分。然后使用对象方法来完成其余的工作，示例如下。

   ```python
   pic = plt.figure(figsize=(8,7), dpi = 80)  # 创建画布，尺寸为8×7，像素值为80
   ax1 = pic.add_subplot(2, 1, 1)  # 划分为2×1图形阵，选择第1张图片
   ```

3. 添加画布内容。第二部分是绘图的主体部分。添加标题、坐标轴名称等步骤与绘制图形是并列的，没有先后顺序，可以先绘制图形，也可以先添加各类标签，但是添加图例一定要在绘制图形之后。pyplot模块中添加各类标签和图例的函数如下表所示。

   | **函数名称** | **函数作用**                                                 |
   | ------------ | ------------------------------------------------------------ |
   | **title**    | 在当前图形中添加标题，可以指定标题的名称、位置、颜色、字体大小等参数 |
   | **xlabel**   | 在当前图形中添加x轴名称，可以指定位置、颜色、字体大小等参数  |
   | **ylabel**   | 在当前图形中添加y轴名称，可以指定位置、颜色、字体大小等参数  |
   | **xlim**     | 指定当前图形x轴的范围，只能确定一个数值区间，而无法使用字符串标识 |
   | **ylim**     | 指定当前图形y轴的范围，只能确定一个数值区间，而无法使用字符串标识 |
   | **xticks**   | 指定x轴刻度的数目与取值                                      |
   | **yticks**   | 指定y轴刻度的数目与取值                                      |
   | **legend**   | 指定当前图形的图例，可以指定图例的大小、位置、标签           |

4. 图形保存与展示。绘制图形之后，可使用`matplotlib.pyplot.savefig()`函数保存图片到指定路径，使用`matplotlib.pyplot.show()`函数展示图形。

```python
import matplotlib.pyplot as plt
import numpy as np

fig = plt.figure(figsize = (6, 6), dpi = 80)  # 创建画布。大小为6×6，像素为80
x = np.linspace(0, 1, 1000)
fig.add_subplot(2, 1, 1)  # 分为2×1图形阵，选择第1张图片绘图
plt.title('y=x^2 & y=x')  # 添加标题
plt.xlabel('x')  # 添加x轴名称‘x’
plt.ylabel('y')  # 添加y轴名称‘y’
plt.xlim((0, 1))  # 指定x轴范围（0,1）
plt.ylim((0, 1))  # 指定y轴范围（0,1）
plt.xticks([0, 0.3, 0.6, 1])  # 设置x轴刻度
plt.yticks([0, 0.5, 1])  # 设置y轴刻度
plt.plot(x, x ** 2)
plt.plot(x, x)
plt.legend(['y=x^2', 'y=x'])  # 添加图例
plt.savefig('../tmp/整体流程绘图.png')  # 保存图片
plt.show()
```

通常情况下，在使用不同的数据重复的绘制同样的图形时，选择自编函数来进行绘图。有时候也会需要在图上添加文本标注。pyplot模块中，使用`matplotlib.pyplot.text()`函数能够在任意位置添加文本，其使用基本语法如下

```python
def my_plotter(ax, x, y, param_dict):
    '''
    ax 接收绘图对象 
    x  接收array 表示横轴数据 无默认值
    y  接收array 表示纵轴数据 无默认值
    param_dict 接收dict 表示传入参数 无默认值
    '''
    out = ax.plot(x, y, **param_dict)
    return out
# 以如下方式使用函数
x = [0, 1, 2, 3, 4, 5, 6, 7, 8]
y1 = x
y2 = np.sin(x)
fig, (ax1, ax2) = plt.subplots(1, 2)
my_plotter(ax1, x, y1, {'marker': 'x'})
my_plotter(ax2, x, y2, {'marker': 'o'})
ax1.text(x[4], y1[4], 'y=x')  # 在子图1添加‘y=x’
ax2.text(x[4], y2[4], 'y=sin(x)')  # 在子图2添加‘y=sin（x）’
plt.savefig('../tmp/自编函数绘图并添加文本.png')
plt.show()
```



# 第三章 pyecharts模块

如果想要做出数据可视化效果图, 可以借助pyecharts模块来完成。官网：https://pyecharts.org/#/zh-cn/

Echarts 是个由百度开源的数据可视化，凭借着良好的交互性，精巧的图表设计，得到了众多开发者的认可。而 Python 是门富有表达力的语言，很适合用于数据处理. 当数据分析遇上数据可视化时pyecharts 诞生了。

```python
# 安装模块 使用清华大学镜像网站
pip install -i https://pypi.tuna.tsinghua.edu.cn/simple pyecharts 
```

基础折线图

```python
# 导包
from pyecharts.charts import Line

# 得到折线图对象
line = Line()
# 添加x轴数据
line.add_xaxis(["中国", "美国", "日本"])
# 添加y轴数据
line.add_yaxis("GDP", [30, 20, 10])
# 生成图标
line.render()
```

![](..\图片\6-07【python2-数据处理】\3-1.png)

pyecharts模块中有很多的配置选项, 常用到2个类别的选项：全局配置选项、系列配置选项。

我们先来看一下全局配置选项：`set_global_opts`方法

```python
# 导包
from pyecharts.charts import Line
from pyecharts.options import TitleOpts, LegendOpts, ToolboxOpts, VisualMapOpts, TooltipOpts

# 得到折线图对象
line = Line()
# 添加x轴数据
line.add_xaxis(["中国", "美国", "日本"])
# 添加y轴数据
line.add_yaxis("GDP", [30, 20, 10])
# 全局配置
line.set_global_opts(
    title_opts=TitleOpts("测试", pos_left="center", pos_bottom="1%"),
    legend_opts=LegendOpts(is_show=True),
    toolbox_opts=ToolboxOpts(is_show=True),
    visualmap_opts=VisualMapOpts(is_show=True),
    tooltip_opts=TooltipOpts(is_show=True)
)
# 生成图标
line.render()
```

![](..\图片\6-07【python2-数据处理】\3-2.png)

**数据处理**

```python
# 导包
import json

# 处理数据
f_us = open("D:/美国.txt", "r", encoding="UTF-8")
# 读取美国的全部内容
data = f_us.read() 
# 把不符合json数据格式的 "jsonp_1629350871167_29498(" 去掉
data = data.replace("jsonp_1629350871167_29498(", "")
# 把不符合json数据格式的 ");" 去掉
data = data[:-2]
# 数据格式符合json格式后,对数据进行转化
data = json.loads(data)
# 获取日本的疫情数据
data = data["data"][0]["trend"]
# 获取2020年的数据
x1_data = data['updateDate'][:314]
# 获取2020年的数据
y1_data = data['list'][0]["data"][:314]
```

**创建折线图**

| **配置项** | **作用**               | **代码实例**                                             |
| ---------- | ---------------------- | -------------------------------------------------------- |
| init_opts  | 对折线图初始化设置宽高 | init_opts=opts.InitOpts(width="1600px",  height="800px") |
| .add_xaxis | 添加x轴数据            | .add_xaxis(列表)                                         |
| .add_yaxis | 添加y轴数据            |                                                          |

```python
# 导包
from pyecharts.charts import Line
import pyecharts.options as opts
# 导包
import json

# 处理数据
f_us = open("D:\Bprogram\python大数据\python\资料\可视化案例数据\折线图数据\美国.txt", "r", encoding="UTF-8")
# 读取美国的全部内容
data = f_us.read()
# 把不符合json数据格式的 "jsonp_1629350871167_29498(" 去掉
data = data.replace("jsonp_1629344292311_69436(", "")
# 把不符合json数据格式的 ");" 去掉
data = data[:-2]


# 数据格式符合json格式后,对数据进行转化
data = json.loads(data)
# 获取日本的疫情数据
data = data["data"][0]["trend"]
# 获取2020年的数据
x1_data = data['updateDate'][:314]
# 获取2020年的数据
y1_data = data['list'][0]["data"][:314]

# 创建折线图
line = Line()
# 添加数据
line.add_xaxis(x1_data)
line.add_yaxis("美国确诊人数", y1_data, label_opts=opts.LabelOpts(is_show=False))
# 设置全局选项
line.set_global_opts(
    title_opts=opts.TitleOpts("2020年确诊人数折线图", pos_left="center", pos_bottom="1%")
)
# 生成html文件
line.render()

f_us.close()
```

