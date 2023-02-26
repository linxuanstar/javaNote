# 第一章 机器学习概述

机器学习和人工智能，深度学习的关系：机器学习是人工智能的一个实现途径、深度学习是机器学习的一个方法发展而来。

![](..\图片\6-07【python4-机器学习】\1-1人工智能范围.png)

机器学习的应用场景非常多，可以说渗透到了各个行业领域当中。医疗、航空、教育、物流、电商等等领域的各种场景。

机器学习是从**数据**中**自动分析获得模型**，并利用**模型**对未知数据进行预测。

我们人从大量的日常经验中归纳规律，当面临新的问题的时候，就可以利用以往总结的规律去分析现实状况，采取最佳策略。

![](..\图片\6-07【python4-机器学习】\1-2机器学习定义.png)

算法分类：

- 监督学习(supervised learning)（预测）：输入数据是由输入特征值和目标值所组成。函数的输出可以是一个连续的值(称为回归），或是输出是有限个离散值（称作分类）。分类 k-近邻算法、贝叶斯分类、决策树与随机森林、逻辑回归、神经网络、回归 线性回归、岭回归。
- 无监督学习(unsupervised learning)：输入数据是由输入特征值所组成。聚类 k-means

# 第二章 特征工程

## 2.1 sklearn数据集

数据集结构：特征值+目标值

![](..\图片\6-07【python4-机器学习】\2-1数据集结构.png)

> 对于每一行数据我们可以称之为**样本**。有些数据集可以没有目标值。

常用数据集：

- Kaggle数据集：大数据竞赛平台、80万科学家、真实数据、数据量巨大。https://www.kaggle.com/datasets
- UCI数据集：收录了360个数据集、覆盖多个领域、数据量几十万。http://archive.ics.uci.edu/ml/
- scikit-learn：数据量小、方便学习。http://scikit-learn.org/stable/datasets/index.html

我们用的是Scikit-learn工具：Python语言的机器学习工具、Scikit-learn包括许多知名的机器学习算法的实现、Scikit-learn文档完善，容易上手，丰富的API、目前稳定版本0.19.1。

```python
# 安装scikit-learn需要Numpy, Scipy等库
pip3 install Scikit-learn==0.19.1

# 安装好之后可以通过以下命令查看是否安装成功
import sklearn
```

**scikit-learn数据集API介绍**

```python
# 加载获取流行数据集
sklearn.datasets

# 获取小规模数据集，数据包含在datasets里
datasets.load_*()

# 加载并返回鸢尾花数据集
sklearn.datasets.load_iris()

# 加载并返回波士顿房价数据集
sklearn.datasets.load_boston()

# 获取大规模数据集，需要从网络上下载，函数的第一个参数是data_home，表示数据集下载的目录,默认是 ~/scikit_learn_data/
datasets.fetch_*(data_home=None)

# subset：'train'或者'test'，'all'，可选，选择要加载的数据集。训练集的“训练”，测试集的“测试”，两者的“全部”
sklearn.datasets.fetch_20newsgroups(data_home=None,subset=‘train’)
```

**鸢尾花数据集使用**

![](..\图片\6-07【python4-机器学习】\2-2鸢尾花数据集使用.png)

```apl
load和fetch返回的数据类型datasets.base.Bunch(字典格式)
- data：特征数据数组，是 [n_samples * n_features] 的二维 numpy.ndarray 数组
- target：标签数组，是 n_samples 的一维 numpy.ndarray 数组
- DESCR：数据描述
- feature_names：特征名,新闻数据，手写数字、回归数据集没有
- target_names：标签名
```

```python
from sklearn.datasets import load_iris
# 获取鸢尾花数据集
iris = load_iris()
print("鸢尾花数据集的返回值：\n", iris)
# 返回值是一个继承自字典的Bench
print("鸢尾花的特征值:\n", iris["data"])
print("看一下多少行多少列:\n", iris.data.shape) # 看一下多少行多少列: (150, 4)
print("鸢尾花的目标值：\n", iris.target)
print("鸢尾花特征的名字：\n", iris.feature_names)
print("鸢尾花目标值的名字：\n", iris.target_names)
print("鸢尾花的描述：\n", iris.DESCR)
```

**数据集的划分**

拿到数据不能全部用来训练，需要用一部分来评估。这个时候就是数据集的划分了。

机器学习一般的数据集会划分为两个部分：

- 训练数据：用于训练，构建模型
- 测试数据：在模型检验时使用，用于评估模型是否有效

划分比例：

- 训练集：70% 80% 75%
- 测试集：30% 20% 30%

数据集划分api

```apl
sklearn.model_selection.train_test_split(arrays, *options)
- x 数据集的特征值
- y 数据集的标签值
- test_size 测试集的大小，一般为float
- random_state 随机数种子,不同的种子会造成不同的随机采样结果。相同的种子采样结果相同。
- return 训练集特征值x.train 测试集特征值x.test 训练集目标值y.train 测试集目标值y.test
```

```python
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split

def datasets_demo():
    """
    对鸢尾花数据集的演示
    :return: None
    """
    # 1、获取鸢尾花数据集
    iris = load_iris()
    print("鸢尾花数据集的返回值：\n", iris)
    # 返回值是一个继承自字典的Bench
    print("鸢尾花的特征值:\n", iris["data"])
    print("鸢尾花的目标值：\n", iris.target)
    print("鸢尾花特征的名字：\n", iris.feature_names)
    print("鸢尾花目标值的名字：\n", iris.target_names)
    print("鸢尾花的描述：\n", iris.DESCR)

    # 2、对鸢尾花数据集进行分割
    # 训练集的特征值x_train 测试集的特征值x_test 训练集的目标值y_train 测试集的目标值y_test
    x_train, x_test, y_train, y_test = train_test_split(iris.data, iris.target, random_state=22)
    print("x_train:\n", x_train.shape)
    # 随机数种子
    x_train1, x_test1, y_train1, y_test1 = train_test_split(iris.data, iris.target, random_state=6)
    x_train2, x_test2, y_train2, y_test2 = train_test_split(iris.data, iris.target, random_state=6)
    print("如果随机数种子不一致：\n", x_train == x_train1)
    print("如果随机数种子一致：\n", x_train1 == x_train2)

    return None
```

## 2.2 特征工程

特征工程是使用专业背景知识和技巧处理数据，使得特征能在机器学习算法上发挥更好的作用的过程。会直接影响机器学习的效果。

- pandas:一个数据读取非常方便以及基本的处理格式的工具
- sklearn:对于特征的处理提供了强大的接口

特征工程包含内容

- 特征抽取
- 特征预处理
- 特征降维

# 第三章 分类算法

## 3.1 sklearn转换器和估计器

**转换器**

- 1、实例化 (实例化的是一个转换器类(Transformer))
- 2、调用fit_transform(对于文档建立分类词频矩阵，不能同时调用)

我们把特征工程的接口称之为转换器，其中转换器调用有这么几种形式：fit_transform、fit、transform。转换器是特征工程的父类。

**估计器**

在sklearn中，估计器(estimator)是一个重要的角色，是一类实现了算法的API。是sklearn机器学习算法的实现。使用步骤如下：

1. 实力化一个estimator

2. estimator。fit(x_train, y_train)计算。调用完毕，模型生成。

3. 模型评估：

   * 直接对比真实值和预测值

     ```python
     y_predict = estimator.predict(x_test)
     y_test == y_predice
     ```

   * 计算准确率

     ```
     accuracy = estimator.score(x_test, y_test)
     ```

     

- 1、用于分类的估计器：
  - sklearn.neighbors k-近邻算法
  - sklearn.naive_bayes 贝叶斯
  - sklearn.linear_model.LogisticRegression 逻辑回归
  - sklearn.tree 决策树与随机森林
- 2、用于回归的估计器：
  - sklearn.linear_model.LinearRegression 线性回归
  - sklearn.linear_model.Ridge 岭回归
- 3、用于无监督学习的估计器
  - sklearn.cluster.KMeans 聚类

## 3.2 K-近邻算法

核心思想：你的“邻居”来推断出你的类别

如果一个样本在特征空间中的**k个最相似(即特征空间中最邻近)的样本中的大多数属于某一个类别**，则该样本也属于这个类别。KNN算法最早是由Cover和Hart提出的一种分类算法。

两个样本的距离可以通过如下公式计算，又叫欧式距离

![](..\图片\6-07【python4-机器学习】\3-1.png)

缺点：K值取得大那么容易受到样本不均衡的影响，取得小那么容易收到样本异常值的影响。

```apl
sklearn.neighbors.KNeighborsClassifier(n_neighbors=5,algorithm='auto')
- n_neighbors：int,可选（默认= 5），k_neighbors查询默认使用的邻居数
- algorithm：{‘auto’，‘ball_tree’，‘kd_tree’，‘brute’}，可选用于计算最近邻居的算法：‘ball_tree’将会使用 BallTree，‘kd_tree’将使用 KDTree。‘auto’将尝试根据传递给fit方法的值来决定最合适的算法。 (不同实现方式影响效率)
```

```python
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier


def knn_iris():
    """
    使用KNN算法对鸢尾花进行分类
    :return:
    """
    # 获取数据
    iris = load_iris()

    # 划分数据集
    x_train, x_test, y_train, y_test = train_test_split(iris.data, iris.target, random_state=6)

    # 标准化
    transfer = StandardScaler()
    x_train = transfer.fit_transform(x_train)
    x_test = transfer.transform(x_test)

    # KNN算法预估器
    estimator = KNeighborsClassifier(n_neighbors=3)
    estimator.fit(x_train, y_train)

    # 模型评估
    # 方法1：直接对比真实值和预测值
    y_predict = estimator.predict(x_test)
    print("直接对比真实值和预测值:\n", y_test == y_predict)

    # 方法2：计算准确率
    score = estimator.score(x_test, y_test)
    print("准确率为：", score)

    return None


if __name__ == '__main__':
    knn_iris()
```

## 3.3 模型选择与调优

交叉验证：将拿到的训练数据，分为训练和验证集。以下图为例：将数据分成5份，其中一份作为验证集。然后经过4次(组)的测试，每次都更换不同的验证集。即得到4组模型的结果，取平均值作为最终结果。又称4折交叉验证。 

我们之前知道数据分为训练集和测试集，但是为了让从训练得到模型结果更加准确。做以下处理

- 训练集：训练集+验证集
- 测试集：测试集

![](..\图片\6-07【python4-机器学习】\3-2交叉验证过程.png)

**超参数搜索-网格搜索(Grid Search)**：有很多参数是需要手动指定的（如k-近邻算法中的K值），这种叫超参数。但是手动过程繁杂，所以需要对模型预设几种超参数组合。每组超参数都采用交叉验证来进行评估。最后选出最优参数组合建立模型。

![](..\图片\6-07【python4-机器学习】\3-3超参数.png)

```apl
# 模型选择与调优API
sklearn.model_selection.GridSearchCV(estimator, param_grid=None,cv=None)
- 对估计器的指定参数值进行详尽搜索
- estimator：估计器对象
- param_grid：估计器参数(dict){“n_neighbors”:[1,3,5]}
- cv：指定几折交叉验证
- fit：输入训练数据
- score：准确率
- 结果分析：
  - best_params_: 最佳参数
  - best_score_: 在交叉验证中验证的最好结果_
  - best_estimator_：最好的参数模型
  - cv_results_:每次交叉验证后的验证集准确率结果和训练集准确率结果
```

**鸢尾花案例增加K值调优**

```python
def knn_iris_gscv():
    """
    使用KNN算法对鸢尾花进行分类，添加网格搜索和交叉验证
    :return: None
    """
    # 获取数据
    iris = load_iris()

    # 划分数据集
    x_train, x_test, y_train, y_test = train_test_split(iris.data, iris.target, random_state=6)

    # 标准化
    transfer = StandardScaler()
    x_train = transfer.fit_transform(x_train)
    x_test = transfer.transform(x_test)

    # KNN算法预估器
    estimator = KNeighborsClassifier(n_neighbors=3)

    # 加入网格搜索和交叉验证
    # 参数准备
    param_dict = {"n_neighbors": [1, 3, 4, 5, 7, 9, 11]}
    estimator = GridSearchCV(estimator, param_grid=param_dict, cv=10)

    estimator.fit(x_train, y_train)

    # 模型评估
    # 方法1：直接对比真实值和预测值
    y_predict = estimator.predict(x_test)
    print("直接对比真实值和预测值:\n", y_test == y_predict)

    # 方法2：计算准确率
    score = estimator.score(x_test, y_test)
    print("准确率为：", score)

    # - best_params_: 最佳参数
    # - best_score_: 在交叉验证中验证的最好结果_
    # - best_estimator_：最好的参数模型
    # - cv_results_: 每次交叉验证后的验证集准确率结果和训练集准确率结果

    print("最佳参数:\n", estimator.best_params_)
    print("在交叉验证中验证的最好结果:\n", estimator.best_score_)
    print("最好的参数模型:\n", estimator.best_estimator_)
    print("每次交叉验证后的验证集准确率结果和训练集准确率结果:\n", estimator.cv_results_)

    return None
```

## 3.4 朴素贝叶斯算法

联合概率：包含多个条件，且所有条件同时成立的概率。记作：P(A,B)。特性：P(A, B) = P(A)P(B)

条件概率：就是事件A在另外一个事件B已经发生条件下的发生概率。记作：P(A|B)。特性：P(A1,A2|B) = P(A1|B)P(A2|B)

相互独立：如果P(A, B) = P(A)P(B)，则称位事件A与事件B相互独立。

```python
# 朴素贝叶斯算法实现邮件分类
from re import sub
from collections import Counter
from itertools import chain
from numpy import array
from jieba import cut
from sklearn.naive_bayes import MultinomialNB
import warnings

# 忽略警告消息
warnings.filterwarnings("ignore")

def getWordsFromFile(txtFile):
    # 获取每一封邮件中的所有词语
    words = []
    # 通过with open语法打开文件，可以自动关闭。所有存储邮件文本内容的记事本文件都使用UTF8编码
    with open(txtFile, encoding='utf8') as fp:
        for line in fp:
            # 遍历每一行，删除两端的空白字符
            line = line.strip()
            # 过滤干扰字符或无效字符
            line = sub(r'[.【】0-9、—。，！~\\*]', '', line)
            # 分词
            line = cut(line)
            # 过滤长度为1的词
            line = filter(lambda word: len(word) > 1, line)
            # 把本行文本预处理得到的词语添加到words列表中
            words.extend(line)
    # 返回包含当前邮件文本中所有有效词语的列表
    return words


# 存放所有文件中的单词
# 每个元素是一个子列表，其中存放一个文件中的所有单词
allWords = []


def getTopNWords(topN):
    # 按文件编号顺序处理当前文件夹中所有记事本文件
    # 训练集中共151封邮件内容，0.txt到126.txt是垃圾邮件内容
    # 127.txt到150.txt为正常邮件内容
    txtFiles = ["../resources/" + str(i) + '.txt' for i in range(151)]
    # 获取训练集中所有邮件中的全部单词
    for txtFile in txtFiles:
        allWords.append(getWordsFromFile(txtFile))
    # 获取并返回出现次数最多的前topN个单词
    freq = Counter(chain(*allWords))
    # 打印出现次数最多的前topN个单词以及次数
    print(freq)
    print("\n")
    # 返回出现次数最多的前topN个单词
    return [w[0] for w in freq.most_common(topN)]


# 全部训练集中出现次数最多的前600个单词
topWords = getTopNWords(600)

# 获取特征向量，前600个单词的每个单词在每个邮件中出现的频率
vectors = []
# 一个words代表一封邮件通过字典拆开后的所有字符串
"""
这是0.txt的words
['国际', 'SCI', '期刊', '材料', '结构力学', '工程', '杂志', '国际', 'SCI', '期刊',
 '先进', '材料科学', '材料', '工程', '杂志', '国际', 'SCI', '期刊', '图像处理', 
 '模式识别', '人工智能', '工程', '杂志', '国际', 'SCI', '期刊', '数据', '信息', 
 '科学杂志', '国际', 'SCI', '期刊', '机器', '学习', '神经网络', '人工智能', 
 '杂志', '国际', 'SCI', '期刊', '能源', '环境', '生态', '温度', '管理', 
 '结合', '信息学', '杂志', '期刊', '网址', '论文', '篇幅', '控制', 
 '以上', '英文', '字数', '以上', '文章', '撰写', '语言', '英语', 
 '论文', '研究', '内容', '详实', '方法', '正确', '理论性', '实践性', 
 '科学性', '前沿性', '投稿', '初稿', '需要', '排版', '录用', '提供', 
 '模版', '排版', '写作', '要求', '正规', '期刊', '正规', '操作', '大牛', 
 '出版社', '期刊', '期刊', '质量', '放心', '检索', '稳定', '邀请函', '推荐', 
 '身边', '老师', '朋友', '打扰', '请谅解']
"""
for words in allWords:
    # map接收一个函数 f 和一个 list，并通过把函数 f 依次作用在 list 的每个元素上，得到一个新的 list 并返回。
    # words.count(x) 计算x在words中出现的次数
    temp = list(map(lambda x: words.count(x), topWords))
    vectors.append(temp)
vectors = array(vectors)


# 训练集中每个邮件的标签，1表示垃圾邮件，0表示正常邮件
labels = array([1] * 127 + [0] * 24)

# 创建模型，使用已知训练集进行训练
model = MultinomialNB()
model.fit(vectors, labels)


def predict(txtFile):
    # 获取指定邮件文件内容，返回分类结果
    words = getWordsFromFile(txtFile)
    currentVector = array(tuple(map(lambda x: words.count(x),
                                    topWords)))
    result = model.predict(currentVector.reshape(1, -1))[0]
    return '垃圾邮件' if result == 1 else '正常邮件'


# 151.txt至155.txt为测试邮件内容
for mail in ("../resources/" + '%d.txt' % i for i in range(151, 156)):
    print(mail, predict(mail), sep=':')
```

## KMeans算法

```python
# 导包
import numpy as np
from sklearn.cluster import KMeans
from PIL import Image
import matplotlib.pyplot as plt

# 打开并读取原始图像中像素颜色值，转换为三维数组
imOrigin = Image.open("颜色压缩测试图像.jpg")
dataOrigin = np.array(imOrigin)
# 转换为二位数组，-1表示自动计算该纬度的大小
data = dataOrigin.reshape(-1, 3)

# 使用KMeans聚类算法把所有想读的颜色值换份成为8类
kmeansPredicter = KMeans(n_clusters=8).fit(data)

# 使用每个像素所属类的中心制替换该像素的颜色
# 获取聚类标签，说明该数据属于8类中的哪一种，属于那个簇
temp = kmeansPredicter.labels_
# 获取聚类中心
dataNew = kmeansPredicter.cluster_centers_[temp]
dataNew.shape = dataOrigin.shape
# imshow()函数实现热图绘制
plt.imshow(dataNew.astype(np.uint8))
# 保存图像
plt.imsave("结果图像8.jpg", dataNew.astype(np.uint8))
# 最后显示一下图像
plt.show()
```

# 第五章 数据挖掘算法基础

## 5.1 线性回归模型

```python
from sklearn.linear_model import LinearRegression

LinearRegression(
    fit_intercept=True, # 是否有截距，若没有那么直线过远点。默认为True
    normalize="deprecated", # 是否将数据归一化
    copy_X=True, # 是否赋值数据表进行运算
    n_jobs=None # 计算的时候使用的核数
)
```

```python
from sklearn.datasets import load_iris
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
import pandas as pd
from sklearn.metrics import explained_variance_score, r2_score

# 鸢尾花数据集
iris = load_iris()

# 转为dataFrame格式
dataFrame = pd.DataFrame(iris.data)

# 拿到前三列的数据作为特征值
x = dataFrame.loc[:, '0': '2']
# 最后一列作为标签值
y = dataFrame.iloc[:, -1]

# 划分数据集 训练集的特征值x_train 测试集的特征值x_test 训练集的目标值y_train 测试集的目标值y_test
# 测试的数据占总数居的两层 训练数据占八层
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=6)
# print("训练集的特征值x_train：\n", x_train)
# print("测试集的特征值x_test：\n", x_test)
# print("训练集的目标值y_train：\n", y_train)
# print("测试集的目标值y_test：\n", y_test)

# 开始训练模型
model = LinearRegression().fit(x_train, y_train)
# print(model) # LinearRegression()

# 进行预测
pre = model.predict(x_test)

# 模型比对
print("可解释方差：", explained_variance_score(y_test, pre))  # 可解释方差： 0.9286703700509112
print("R方差", r2_score(y_test, pre))  # R方差 0.9238959592229254

```

## 5.2 逻辑回归模型

可以使用Sklearn库中的linear_model模块中的LogisticRegression类建立逻辑回归模型。常用参数如下：

```python
def __init__(
        self,
        penalty="l2", # 接收str。表示正则化选择参数，可选l1或者l2
        *,
        dual=False,
        tol=1e-4,
        C=1.0,
        fit_intercept=True,
        intercept_scaling=1,
        class_weight=None, # 接收balanced以及字典，表示类型权重参数。
        random_state=None,
        solver="lbfgs", # 表示优化算法选择参数 
        max_iter=100,
        multi_class="auto",
        verbose=0,
        warm_start=False,
        n_jobs=None,
        l1_ratio=None,
)
```

![](..\图片\6-07【python4-机器学习】\5-1.png)

示例：

```python
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.pipeline import Pipeline

# 获取数据
iris = load_iris()
# 获取属性
x = iris['data']
# 获取标签
y = iris['target']

# 划分数据
# 划分数据集 训练集的特征值x_train 测试集的特征值x_test 训练集的目标值y_train 测试集的目标值y_test
# 测试的数据占总数居的两层 训练数据占八层
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=112)

# 数据标准化
# 生成规则
scale = StandardScaler().fit(x_train)
# 将规则应用于训练数据
x_train_scale = scale.transform(x_train)
# 将规则应用于测试数据
x_test_scale = scale.transform(x_test)
# print("标准差之前训练数据的方差：", x_train.std()) # 标准差之前训练数据的方差： 1.9905649738028035
# print("标准差之后训练数据的方差：", x_train_scale.std()) # 标准差之后训练数据的方差： 0.9999999999999999
# print("标准差之前训练数据的均值：", x_train.mean()) # 标准差之前训练数据的均值： 3.4289583333333336
# print("标准差之后训练数据的均值：", x_train_scale.mean()) # 标准差之后训练数据的均值： 1.313763912473102e-16

# 训练模型
model = LogisticRegression().fit(x_train_scale, y_train)

# 生成规则和训练模型一步完成
# lr = Pipeline([('scale', StandardScaler()), ('model', LogisticRegression())])

# 模型预测
pre = model.predict(x_test_scale)

# 模型评估
print("正确率：", accuracy_score(pre, y_test))
```

## 5.3 决策树模型

可以使用Sklearn库中的tree模块中的DecisionTreeClassifier类建立逻辑回归模型。常用参数如下：

```python
def __init__(
    self,
    *,
    criterion="gini", # 表示分割质量的功能
    splitter="best", # 表示用于在每个节点上选择拆分的策略
    max_depth=None, # 表示树的最大深度
    min_samples_split=2, # 表示拆分内部所需要的最少样本数量
    min_samples_leaf=1,
    min_weight_fraction_leaf=0.0,
    max_features=None,
    random_state=None,
    max_leaf_nodes=None,
    min_impurity_decrease=0.0,
    class_weight=None,
    ccp_alpha=0.0,
)
```

![](..\图片\6-07【python4-机器学习】\5-2.png)

使用graphviz可视化决策树:http://www.graphviz.org/	

``` python
# 设置字体
edge[fontname = "SimHei"];
node[fontname = "SimHei"];

# 生成pdf
dot -T pdf 路径\tree.dot -o 路径\tree.pdf
```

使用决策树算法预测销售数量的高低

```python
# 训练数据
	天气 是否周末 是否有促销 销售数量
0   坏    否     是    	高
1   坏    是     否    	低
2   坏    是     是    	高
3   坏    是     是    	低
4   坏    否     否    	高
5   坏    是     是    	高
6   坏    否     否    	高
```

```python
from sklearn.tree import DecisionTreeClassifier
import pandas as pd
from sklearn.tree import export_graphviz

data = pd.read_excel("D:\\比赛\\大数据\\培训\\sales.xlsx")

# 这是dataFrame
# 把是、好、高设置为1，低、坏、否设置为-1
data[data == '好'] = 1
data[data == '是'] = 1
data[data == '高'] = 1
data[data != 1] = -1

# 获取属性数据 就是获取前三列
x = data.iloc[:, :3].astype('int')
# 获取标签数据
y = data.iloc[:, -1].astype('int')

# 训练模型
model = DecisionTreeClassifier().fit(x, y)

# 根据模型预测
print(model.predict(x))

# 决策树的可视化
with open(r'D:\比赛\大数据\培训\my_tree.dot', 'w', encoding='utf-8') as f:
    f = export_graphviz(model, feature_names=x.columns, out_file=f)
# 然后转一下字体，之后cmd转成pdf就可以了。但是记得软件需要下载，并且PATH添加上
```

## 5.4 KNN模型

构建K近邻分类模型

```python
from sklearn.datasets import load_digits
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score


# 获取数据集
digits = load_digits()
# 获取data
x = digits['data']
# 获取target
y = digits['target']

# 划分数据集
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=112)

# 构建模型
model = KNeighborsClassifier(n_neighbors=5).fit(x_train, y_train)
pre = model.predict(x_test)

# 模型评估
print("K近邻模型的正确率:" , accuracy_score(pre, y_test))

```

## 5.5 支持向量机

![](..\图片\6-07【python4-机器学习】\5-3.png)

```python
from sklearn.svm import SVC
```

## 5.6 神经网络

![](..\图片\6-07【python4-机器学习】\5-4.png)

```python
from sklearn.neural_network import MLPClassifier
```

![](..\图片\6-07【python4-机器学习】\5-5.png)

![](..\图片\6-07【python4-机器学习】\5-6.png)

![](..\图片\6-07【python4-机器学习】\5-7.png)

## 5.7 Kmeans聚类算法

```python
from sklearn.cluster import KMeans
```

![](..\图片\6-07【python4-机器学习】\5-8.png)

```python
import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans

# 获取数据
data = pd.read_excel("D:\\比赛\\大数据\\培训\\sales.xlsx", index_col="ID")

# 数据标准化
data_scaler = StandardScaler().fit_transform(data)

# 构建模型
model = KMeans(n_clusters=5, random_state=123).fit(data_scaler)
# 打印所有标签
# print(model.labels_)
# 标签序列化 并统计个数 标签统计
r1 = pd.Series(model.labels_).value_counts()
# 类中心
r2 = pd.DataFrame(model.cluster_centers_)
# 拼接两个dataFrame
r = pd.concat([r2, r1], axis=1)
# 设置列名称
r.columns = list(data.columns) + ['类别数目']

# 存储类结果
r3 = pd.concat([data, pd.Series(model.labels_, index = data.index)], axis = 1)
r3.columns = list(data.columns) + ['聚类类别']
r3.to_csv("D:\\比赛\\大数据\\培训\\sales.csv", encoding='utf-8')

```

