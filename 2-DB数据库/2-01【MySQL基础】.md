# 第一章 数据库和MySQL

## 1.1 数据库介绍

数据库：用于存储和管理数据的仓库。英文：`DataBase`简称：`DB`。

数据库管理系统：操纵和管理数据库的大型软件。英文：`DataBase Management System` 简称：`DBMS`

数据库特点

1. 持久化存储数据的。其实数据库就是一个文件系统
2. 方便存储和管理数据
3. 使用了统一的方式操作数据库 -- `SQL`

常见的关系型数据库软件：

![](..\图片\2-01【MySQL】\1.png)

## 1.2 MySQL数据库软件

之前下载过一个5.5的版本，但是太老了，删掉了。现在又下载了一个8.0.26的版本。

**服务启动**

1. cmd命令窗口 --> 输入`services.msc` 打开服务的窗口

2. 使用管理员权限打开cmd

   `net start mysql`：启动mysql服务

   `net stop mysql`：关闭mysql服务

**登录**

两种方式可以登录，自带的和Windows的命令窗口。

- 打开`MySQL 8.0 Command Line Client - Unicode`，输入密码就可以连接了。
- `mysql [-h IP地址] [-P 端口] -u root -p`：连接MySQL。括号是可以加上也可以不加上。输入命令然后回车直接输入密码就可以连接上了。 

**卸载**

1. win+R 打开运行，输入 `services.msc` 点击 "确定" 调出系统服务。停止MySQL服务。
2. 打开控制面板 ---> 卸载程序 ---> 卸载MySQL相关所有组件。
3. 删除MySQL安装目录，在C盘Program Files文件夹下面`C:\Program Files\MySQL`
4. 删除`C:/ProgramData`目录下面的MySQL文件夹（隐藏文件夹）

如果已将MySQL卸载，但是通过任务管理器--->服务，查看到MySQL服务仍然残留在系统服务里。解决办法：以管理员方式运行cmd命令行，输入以下命令：`sc delete 服务名称（如MySQL80）`。这样可以实现删除服务。

# 第二章 SQL

SQL：操作关系型数据库的编程语言，定义了操作所有关系型数据库的规则。结构化查询语言。 英文：`Structured Query Language`

每一种数据库操作的方式存在不一样的地方，称为"方言"。

## 2.1 SQL通用语法

1. SQL语句可以单行或者多行书写，以分号结尾。

2. 可使用空格和缩进来增强语句的可读性。

3. MySQL数据库的SQL语句不区分大小写，关键字建议使用大写。

4. 注释：

   单行注释：单行注释第一种：`-- 注释内容`**（这种注释方式，两个横杠与注释内容之间务必要空格）**

   单行注释第二种：`#注释内容`，（MySQL特有）。

   多行注释：`/* */`

5. 清屏：`system cls`或者是`system clear;`。 这个应该是MySQL 8之后才有

6. 查询结果纵向：加上`\G`，将查询结果进行按列打印，可以使每个字段打印到单独的行。

7. SQL语句加上`\g`，等于加上`;`。`\g = ;`。

## 2.2 SQL分类

SQL语言共分为四大类：数据查询语言DQL，数据操纵语言DML，数据定义语言DDL，数据控制语言DCL。 

1. `DDL（Data Definition Language）`数据定义语言。用来定义数据库对象：数据库，表，字段等。关键字：create，show，alter，drop等。

2. `DML（Data Manipulation  Language）`数据操作语言。用来对数据库总表的数据进行增删改。关键字：insert，delete，update等。

3. `DQL（Data Query Language）`数据查询语言。用来查询数据库中表的记录（数据）。关键字：select，where等。

4. `DCL（Data Control Language）`数据控制语言。用来定义数据库的访问权限和安全级别，及创建用户。关键字：GRANT, REVOKE等

![](..\图片\2-01【MySQL】\2.png)

## 2.3 MySQL数据类型

数值类型：

* `TINYINT`：相当于Java中的`byte`类型，大小为1个字节。有符号(SIGNED)范围是(-128，127)，无符号(UNSIGNED)范围是(0，255)。
* `SMALLINT`：相当于Java中的`short`类型，大小为2个字节。
* `MEDIUMINT`：大小为3个字节。
* `INT或INTEGER`：相当于Java中的`int`类型，大小为4个字节。有符号(SIGNED)范围是(-2147483648，2147483647)，无符号(UNSIGNED)范围是(0，4294967295)。
* `BIGINT`：相当于Java中的`Long`类型，大小为8个字节。有符号(SIGNED)范围是(-2^63，2^63-1)，无符号(UNSIGNED)范围是(0，2^64-1)。
* `FLOAT`：相当于Java中的`float`类型，大小为4个字节。
* `DOUBLE`：相当于Java中的`double`类型，大小为8个字节。
* `DECIMAL`：依赖于M(精度)和D(标度)的值

字符串类型：

| 类型       | 大小                  | 描述                         |
| ---------- | --------------------- | ---------------------------- |
| CHAR       | 0-255 bytes           | 定长字符串                   |
| VARCHAR    | 0-65535 bytes         | 变长字符串                   |
| TINYBLOB   | 0-255 bytes           | 不超过255个字符的二进制数据  |
| TINYTEXT   | 0-255 bytes           | 短文本字符串                 |
| BLOB       | 0-65 535 bytes        | 二进制形式的长文本数据       |
| TEXT       | 0-65 535 bytes        | 长文本数据                   |
| MEDIUMBLOB | 0-16 777 215 bytes    | 二进制形式的中等长度文本数据 |
| MEDIUMTEXT | 0-16 777 215 bytes    | 中等长度文本数据             |
| LONGBLOB   | 0-4 294 967 295 bytes | 二进制形式的极大文本数据     |
| LONGTEXT   | 0-4 294 967 295 bytes | 极大文本数据                 |

日期时间类型：

| 类型      | 大小 | 范围                                       | 格式                | 描述                    |
| --------- | ---- | ------------------------------------------ | ------------------- | ----------------------- |
| DATE      | 3    | 1000-01-01 至  9999-12-31                  | YYYY-MM-DD          | 日期值                  |
| TIME      | 3    | -838:59:59 至  838:59:59                   | HH:MM:SS            | 时间值或持续时间        |
| YEAR      | 1    | 1901 至 2155                               | YYYY                | 年份值                  |
| DATETIME  | 8    | 1000-01-01 00:00:00 至 9999-12-31 23:59:59 | YYYY-MM-DD HH:MM:SS | 混合日期和时间值        |
| TIMESTAMP | 4    | 1970-01-01 00:00:01 至 2038-01-19 03:14:07 | YYYY-MM-DD HH:MM:SS | 混合日期 时间值和时间戳 |

## 2.4 DDL操作数据库和表

`DDL（Data Definition Language）`数据定义语言

用来定义数据库对象：数据库，表，字段等。关键字：create，show，alter，drop等。

**操作数据库**

操作数据库即CRUD

1. C（create）：创建

   ```sql
   create database 数据库名称；		-- 创建数据库
   ```

   ```sql
   create database if not exists 数据库名称;		-- 创建数据库，判断不存在，再创建
   ```

   ```sql
   create database 数据库名称 character set 字符集名称;		-- 创建指定字符集数据库
   ```

   ```sql
   create database if not exists db4 character set gbk;		-- 创建db4数据库，判断是否存在，并制定字符集为gbk    UTF8字符集长度为3字节，有些符号占4字节，所以推荐用utf8mb4字符集
   ```

   ```sql
   CREATE DATABASE [ IF NOT EXISTS ] 数据库名 [ DEFAULT CHARSET 字符集] [COLLATE 排序规则 ];
   ```

2. R（Retrieve）：查询

   ```sql
   show databases;		-- 查询所有数据库的名称
   ```

   ```sql
   show create database 数据库名称;		-- 查询某个数据库的字符集（查询某个数据库的创建语句）
   ```

3. U（Update）：修改

   ```sql
   alter database 数据库名称 character set 字符集名称;		-- 修改数据库的字符集
   ```

4. D（Delete）：删除

   ```sql
   drop database 数据库名称;		-- 删除数据库
   ```

   ```sql
   drop database if exists 数据库名称;		-- 判断数据库是否存在，存在再删除
   ```

5. 使用数据库

   ```sql
   select database();		-- 查询当前正在使用的数据库名称
   ```

   ```sql
   use 数据库名称;		-- 进入该数据库，使用数据库
   ```

**操作表**

1. C（create）：创建

   ```sql
   CREATE TABLE 表名(
   	字段1 字段1类型 [COMMENT 字段1注释],
   	字段2 字段2类型 [COMMENT 字段2注释],
   	字段3 字段3类型 [COMMENT 字段3注释],
   	...
   	字段n 字段n类型 [COMMENT 字段n注释]    -- 最后一个字段后面没有逗号
   )[ COMMENT 表注释 ];
   ```

   复制表：

   ```sql
   create table 表名 like 被复制的表名;
   ```
   
2. R（Retrieve）：查询

   ```sql
   show tables;	-- 查询某个数据库中所有表的表名称
   ```

   ```sql
   desc + 表名称;		-- 查询表结构，有什么字段
   ```
   ```sql
   show create table 表名;		-- 查询某个表的字符集（查询某个表的创建语句）
   ```

3. U（Update）：修改

   ```sql
   alter table 表名 rename to 新的表名；		-- 修改表名
   ```

   ```sql
   alter table 表名 character set 字符集名称;		-- 修改表的字符集
   ```

   ```sql
   alter table 表名 add 字段名 数据类型  [COMMENT 注释] [约束];		-- 添加字段
   ```

   ```sql
   alter table 表名 change 字段名 新字段名 新数据类型;		-- 修改字段名称 类型
   alter table 表名 modify 字段名 新数据类型;		-- 修改字段数据类型
   ```

   ```sql
   alter table 表名 drop 需要删除的字段名;		-- 删除字段
   ```

4. D（Delete）：删除

   ```sql
   drop table 表名;		-- 删除表
   ```

   ```sql
   drop table if exists 表名;		-- 判断是否存在此表，存在删除
   ```
   
   ```sql
   TRUNCATE TABLE 表名;				-- 删除当前表，并创建一个新表 字段相同
   ```

## 2.5 DML增删改表中数据 

`DML（Data Manipulation  Language）`数据操作语言

用来对数据库总表的数据进行增删改。关键字：insert，delete，update等。

**添加数据**

```sql
insert into 表名 (字段名1，字段名2，...字段名n) values (值1，值2，...值n);

INSERT INTO stu (id, NAME) VALUES (1, "林炫");
INSERT INTO stu VALUES (1, "张三", 21, 10, "2001-11-11", NULL);		-- 不定义字段名，直接全部赋值
```

```sql
-- 批量添加数据
INSERT INTO 表名 (字段名1，字段名2，...字段名n) VALUES (值1，值2，...值n)，(值1，值2，...值n),...;
INSERT INTO dept (id, name) VALUES (1, '研发部'), (2, '市场部'),(3, '财务部'), (4, '销售部');
```

需要注意如下事项：

1. 字段名和值需要一一对应。

2. 如果表名后面不定义字段名，那么默认给所有的列添加值。如果缺少值，与字段名不对应，那么会报错

3. 除了数字类型，其他类型需要使用引号（单引号和双引号都可以）引起来。

**删除数据**

```sql
delete from 表名 [where条件];		-- 删除数据，如果不加上条件，会删除所有数据
delete from 表名;					 -- 不推荐使用，效率低下，表里面有多少行就会执行多少
truncate table 表名;		          -- 推荐使用，效率更高，先删除表，然后再创建一个一样名称的空表。
```

**修改数据**

```sql
update 表名 set 字段名 = 值1， 字段名 = 值2， ...[where 条件];
```

注意：如果不加上任何条件，那么会将表中所有的记录全部修改。

## 2.6 DQL查询表中记录

`DQL（Data Query Language）`数据查询语言

用来查询数据库中表的记录（数据）。关键字：select，where等。

```sql
select
	字段列表
from
	表名
where
	条件列表
group by
	分组字段
having
	分组之后的条件
order by
	排序
limit
	分页限定;
```

**基础查询**

* 多个字段的查询

  ```sql
  select 字段名1，字段名2,...from 表名; 
  select * form 表名; 		-- 如果查询所有的字段，那么可以使用*来代替字段列表。
  ```

* 去除重复的值

  ```sql
  select distinct 字段名 from 表名;     -- 如果需要去除两列字段的重复值，需要两列字段值重复，才会去除。
  ```

* 计算列

  可以使用四则运算来计算一些列的值。`+ - * /`

  ```sql
  select 字段名, 字段名, 四则运算字段名（字段名1 + 字段名2） from 表名;
  ```

* 起别名

  ```sql
  select 字段名 as 新的名称，...from 表名;		-- as关键字可以省略，用空格来代替
  ```
  
* 默认填充值

  ```sql
  select '填充值' 字段名称 from 表名;            -- 字段名称可以不存在，最后行数与表行数相同
  ```

**条件查询**

语法如下：`SELECT 字段列表 FROM 表名 WHERE 条件列表`。`where`字句后面跟条件列表。

条件如下：

| 比较运算符          | 功能                                           |
| ------------------- | ---------------------------------------------- |
| >                   | 大于                                           |
| >=                  | 大于等于                                       |
| <                   | 小于                                           |
| <=                  | 小于等于                                       |
| =                   | 等于                                           |
| <> 或 !=            | 不等于                                         |
| BETWEEN ... AND ... | 在某个范围内（含最小、最大值）                 |
| IN(...)             | 在in之后的列表中的值，多选一                   |
| LIKE 占位符         | 模糊匹配（`_`匹配单个字符，`%`匹配任意个字符） |
| IS NULL             | 是NULL                                         |

| 逻辑运算符 | 功能                         |
| ---------- | ---------------------------- |
| AND 或 &&  | 并且（多个条件同时成立）     |
| OR 或 \|\| | 或者（多个条件任意一个成立） |
| NOT 或 !   | 非，不是                     |

**聚合函数**

聚合函数：将一列数据作为一个整体，进行纵向计算。例如：计算个数，计算和，计算平均值。

聚合函数的计算是排除NULL值的，就是遇到有NULL值会忽略。

```sql
select 函数名称(字段名称) from 表名称;
```

* `count`：计算个数

  ```sql
  select count(主键、ID列) from 表名称;		-- 一般选择非空的列：主键
  ```

  ```sql
  select count(*) from 表名称;		-- * 代表全部，找到一个非空的列来搞，不建议
  ```

  ```sql
  SELECT COUNT(DISTINCT COUNTRY) FROM psur_list;  -- 在聚合函数中DISTINCT 一般跟 COUNT 结合使用
  ```

* `max`：计算最大值

  ```sql
  select max(字段名称) from 表名称;		-- 计算该表该列最大值
  ```

* `min`：计算最小值

  ```sql
  select min(字段名称) from 表名称;		-- 计算该表该列最大值
  ```

* `sum`：计算和

  ```sql
  select sum(字段名称) from 表名称;		-- 计算该表该列数值的和
  ```

* `avg`：计算平均值

  ```sql
  select avg(字段名称) from 表名称;		-- 计算该表该列平均值
  ```

由于聚合函数的计算是排除`NULL`的值的，所以对此我们有两种解决方案：

1. 选择不包含非空的列进行计算

2. `IFNULL函数`

   ```sql
   select 函数(IFNULL(字段名称, 值)) from 表名称;  -- IFNULL函数会判断NULL，如果是NULL那么会将NULL变为值
   select count(IFNULL(math, 0)) from student;		-- count函数计算math列个数，IFNULL函数对math列进行判断，如果有着NULL，那么将NULL变为0，其他的不改变
   ```

**分组查询**

分组查询就是将以行为单位，来进行查询。

```sql
-- 分组查询语法
select 分组查询字段列表 from 表名称 [where 条件] group by 分组字段名 [having 分组后过滤条件];	
```

```sql
select sex, avg(math) from 表名称 group by sex;		-- 按照性别进行分组，分别查询男、女同学平均分
```

```sql
select sex, avg(math), count(id) from 表名称 group by sex;		-- 按照性别进行分组，分别查询男、女同学平均分、人数
```

```sql
select sex, avg(math), count(id) from 表名称 where math > 70 group by sex;		-- 按照性别进行分组，分别查询男、女同学平均分、人数   要求：分数低于70的人不参与分组
```

```sql
select sex, avg(math), count(id) from 表名称 where math > 70 group by sex having count(id) > 2; -- 按照性别进行分组，分别查询男、女同学平均分、人数   要求：分数低于70的人不参与分组
```

`where` 和 `having`区别：

1. `where`在分组之前进行限定，如果不满足条件，那么不参与分组。`having`在分组之后进行限定，如果不满足结果，则不会被查询出来。
2. `where`后面不可以跟聚合函数，`having`后面可以进行聚合函数的判断。

注意：

* 执行顺序：where > 聚合函数 > having。
* 分组之后，查询的字段一般为聚合函数和分组字段，查询其他字段没有任何意义。

**排序查询**

`order by`

```sql
-- 排序查询语法  ASC：升序，默认的。 DESC：降序。
select 字段列表 from 表名 order by 排序字段1 排序方式1，排序字段2 排序方式2...;
```

```sql
SELECT * FROM person ORDER BY math;		-- 默认对math列进行升序排序
SELECT * FROM person ORDER BY math DESC;	-- 对math列进行降序排序
SELECT * FROM person ORDER BY math ASC, english DESC;	-- 对math列进行升序排序，math列有重复值的时候判断English列，按照english列进行降序排序
```

注意：如果有多个排序条件，则当前面的条件值一样的时候，就会排序第二条件。

**分页查询**

分页查询，类似于百度搜索出来后有很多词条，词条按页码来出现。关键字是`limit`。

```sql
select 字段列表 from 表名 limit 开始的索引，每页查询的条数;
select * from person limit 0, 3;		-- 第1页
select * from person limit 3, 3;		-- 第2页
select * from person limit 6, 3;		-- 第3页
```

* 公式：开始的索引 = （当前的页码 - 1） * 每页显示的条数。
* 开始的索引是从0开始的。如果查询的是第一页数据，那么起始索引可以省略，直接简写为`limit 查询条数`。
* 分页操作是一个“方言”， 其他的数据库都有各自的“方言”。MySQL中是Limit，其他数据库是其他的关键子。

**执行顺序**

```sql
from
	表名列表
where
	条件列表
group by
	分组字段列表
having
	分组后条件列表
select
	字段列表
order by
	排序字段列表
limit
	分页参数
```

## 2.7 DCL数据控制语言

`DCL（Data Control Language）`数据控制语言

用来定义数据库的访问权限和安全级别，及创建用户。关键字：GRANT, REVOKE等

**管理用户**

* 查询用户

  ```sql
  USE mysql;						-- 切换到mysql数据库
  SELECT * FROM USER;				 -- 查询user表
  ```


* 添加用户

  ```sql
  CREATE USER "用户名"@"主机名" IDENTIFIED BY "密码";
  CREATE USER "zhangsan"@"localhost" IDENTIFIED BY "123";			-- 主机名就是主机地址
  CREATE USER "zhangsan"@"%" IDENTIFIED BY "123";			-- 添加用户zhangsan，任意地方都可以访问
  ```

* 删除用户

  ```sql
  DROP USER "用户名"@"主机名";
  DROP USER "zhangsan"@"localhost";
  ```

* 修改用户密码

  ```sql
  ALTER USER '用户名'@'主机名' IDENTIFIED WITH mysql_native_password BY '新密码';
  ALTER USER 'zhangsan'@'localhost' IDENTIFIED WITH mysql_native_password BY '123';  -- 使用MySQL本地密码处理方式来修改密码
  ```

假如我们修改root密码，之后忘记了那么也可以重新登陆MySQL。

1. 使用cmd命令行，输入：`net stop mysql`    停止mysql的服务，记得使用管理员权限。
2. 使用无验证的方式来启动mysql服务，输入：`mysqld -- skip-grant-tables`。
3. 打开另外一个cmd命令行窗口，输入：`mysql`,。
4. 使用命令行修改root用户密码，关闭两个命令行窗口。
5. 右键打开任务管理器，结束掉mysqld.exe进程。
6. 使用管理员权限打开cmd命令行，输入：`net start mysql`。
7. 这时候就可以使用新的密码来登陆了。

**权限管理**

| 权限                | 说明               |
| ------------------- | ------------------ |
| ALL, ALL PRIVILEGES | 所有权限           |
| SELECT              | 查询数据           |
| INSERT              | 插入数据           |
| UPDATE              | 修改数据           |
| DELETE              | 删除数据           |
| ALTER               | 修改表             |
| DROP                | 删除数据库/表/视图 |
| CREATE              | 创建数据库/表      |

* 查询权限

  ```sql
  SHOW GRANTS FOR "用户名"@"主机名称";
  SHOW GRANTS FOR "zhangsan"@"localhost";
  ```

* 授予权限

  ```sql
  GRANT 权限列表 ON 数据库名称.表名 TO "用户名"@"主机名称";
  GRANT SELECT, DELETE, UPDATE ON day3.account TO "zhangsan"@"localhost";
  GRANT ALL ON *.* TO "zhangsan"@"localhost";	              -- 授予全部权限
  ```

* 撤销权限

  ```sql
  REVOKE 权限列表 ON 数据库名称.表名 FROM "用户名"@"主机名称";
  REVOKE UPDATE ON day3.account FROM "zhangsan"@"localhost";
  ```

# 第三章 函数和约束

函数 是指一段可以直接被另一段程序调用的程序或代码。 也就意味着，这一段程序或代码在MySQL中 已经给我们提供了，我们要做的就是在合适的业务场景调用对应的函数完成对应的业务需求即可。 

## 3.1 字符串函数

常用函数：

| 函数                               | 功能                                                       |
| ---------------------------------- | ---------------------------------------------------------- |
| CONCAT(s1, s2, ..., sn)            | 字符串拼接，将s1, s2, ..., sn拼接成一个字符串              |
| GROUP_CONCAT()                     | 分组中括号里对应的字符串进行连接                           |
| SUBSTRING_INDEX(str, delim, count) | 有分隔符的字符串拆分                                       |
| LOWER(str)                         | 将字符串全部转为小写                                       |
| UPPER(str)                         | 将字符串全部转为大写                                       |
| LPAD(str, n, pad)                  | 左填充，用字符串pad对str的左边进行填充，达到n个字符串长度  |
| RPAD(str, n, pad)                  | 右填充，用字符串pad对str的右边进行填充，达到n个字符串长度  |
| TRIM(str)                          | 去掉字符串头部和尾部的空格                                 |
| SUBSTRING(str, start, len)         | 返回从字符串str从start位置起的len个长度的字符串，**没有0** |
| LENGTH(str)                        | 获取传入的字符串str长度                                    |
| CHAR_LENGTH(str)                   | 获取传入的字符串str的字符数                                |

使用示例：

```mysql
# 如果没有指定SEPARATOR的话，也就是说没有写，那么就会默认以 ','分隔
GROUP_CONCAT([DISTINCT] 要连接的字段 [Order BY ASC/DESC 排序字段] [Separator '分隔符'])
```

```mysql
SELECT CONCAT('Hello', 'World');			-- 拼接 打印：Helloworld
SELECT LPAD('01', 5, '-');					-- 左填充 打印：---01
SELECT TRIM(' Hello World ');				-- 去除空格 打印：Hello World
SELECT SUBSTRING('Hello World', 1, 5);	-- 剪切（起始索引为1） 打印：Hello
```

```sql
update emp set workno = lpad(workno, 5, '0');		-- 修改workno字段，前面增加0，直到位数为5
```

## 3.2 数值函数

常见函数：

| 函数        | 功能                             |
| ----------- | -------------------------------- |
| CEIL(x)     | 向上取整                         |
| FLOOR(x)    | 向下取整                         |
| MOD(x, y)   | 返回x/y的模                      |
| RAND()      | 返回0~1内的随机数，后面又小数    |
| ROUND(x, y) | 求参数x的四舍五入值，保留y位小数 |

```sql
select lpad(round(rand()*1000000 , 0), 6, '0');    -- 通过数据库的函数，生成一个六位数的随机验证码。    获取随机数可以通过rand()函数，但是获取出来的随机数是在0-1之间的，所以可以在其基础上乘以1000000，然后舍弃小数部分，如果长度不足6位，补0
```

## 3.3 日期函数

常用函数：

| 函数                               | 功能                                              |
| ---------------------------------- | ------------------------------------------------- |
| CURDATE()                          | 返回当前日期                                      |
| CURTIME()                          | 返回当前时间                                      |
| NOW()                              | 返回当前日期和时间                                |
| YEAR(date)                         | 获取指定date的年份                                |
| MONTH(date)                        | 获取指定date的月份                                |
| DAY(date)                          | 获取指定date的日期                                |
| DATE_ADD(date, INTERVAL expr type) | 返回一个日期/时间值加上一个时间间隔expr后的时间值 |
| DATEDIFF(date1, date2)             | 返回起始时间date1和结束时间date2之间的天数        |

例子：

```mysql
SELECT DATE_ADD(NOW(), INTERVAL 70 YEAR);				-- DATE_ADD
```

## 3.4 流程函数

常用函数：

| 函数                                                         | 功能                                                      |
| ------------------------------------------------------------ | --------------------------------------------------------- |
| IF(value, t, f)                                              | 如果value为true，则返回t，否则返回f                       |
| IFNULL(value1, value2)                                       | 如果value1不为空，返回value1，否则返回value2              |
| CASE WHEN [ val1 ] THEN [ res1 ] ... ELSE [ default ] END    | 如果val1为true，返回res1，... 否则返回default默认值       |
| CASE [ expr ] WHEN [ val1 ] THEN [ res1 ] ... ELSE [ default ] END | 如果expr的值等于val1，返回res1，... 否则返回default默认值 |

例子：

```sql
-- 查询emp表的name和workaddress，如果workaddress在北京或者上海显示一线城市，否则显示二线城市
select
	name,
	(case workaddress when '北京市' then '一线城市' when '上海市' then '一线城市' else '二线城市' end) as '工作地址'
from employee;
```

## 3.5 约束

约束：对表中的数据进行限定，保证数据的正确性、有效性和完整性。

常见约束类型如下：

| 约束                    | 描述                                                     | 关键字      |
| ----------------------- | -------------------------------------------------------- | ----------- |
| 非空约束                | 限制该字段的数据不能为null                               | NOT NULL    |
| 唯一约束                | 保证该字段的所有数据都是唯一、不重复的                   | UNIQUE      |
| 主键约束                | 主键是一行数据的唯一标识，要求非空且唯一                 | PRIMARY KEY |
| 默认约束                | 保存数据时，如果未指定该字段的值，则采用默认值           | DEFAULT     |
| 检查约束（8.0.1版本后） | 保证字段值满足某一个条件                                 | CHECK       |
| 外键约束                | 用来让两张图的数据之间建立连接，保证数据的一致性和完整性 | FOREIGN KEY |

## 3.6 基础约束

**非空约束**

非空约束关键字是`NOT NULL`

```sql
CREATE TABLE stu(
	id INT,
	NAME VARCHAR(20) not null		-- 创建表的时候指定该字段非空约束，name为非空
);
```

```sql
ALTER TABLE stu MODIFY NAME VARCHAR(20) NOT NULL;	-- 更改表，添加非空约束，也可以去除非空约束
```

**唯一约束**

唯一约束关键字是`UNIQUE`，又叫做唯一索引。注意在MySQL中，唯一约束限定的列的值可以有多个NULL

```sql
CREATE TABLE stu(
	id INT,
	NAME VARCHAR(20) UNIQUE					-- 创建表的时候指定该字段唯一
);
```

```sql
ALTER TABLE stu MODIFY phone_number VARCHAR(20) UNIQUE;	-- 创建表完成之后可以添加唯一约束
```

```sql
ALTER TABLE stu DROP INDEX phone_number;	-- 删除唯一约束
```

**主键约束**

主键约束关键字`PRIMARY KEY`是非空且唯一，也就是上面两个的综合。一张表只能有一个字段为主键，通常主键都是id字段

```sql
CREATE TABLE stu(
	id INT PRIMARY KEY,					-- 创建表的时候，来添加主键约束
	NAME VARCHAR(20) UNIQUE			
);
```

```sql
ALTER TABLE stu MODIFY id INT PRIMARY KEY;		-- 创建表完成之后，添加主键约束
```

```sql
ALTER TABLE stu DROP PRIMARY KEY;				-- 删除主键约束
```

**自动增长**

如果某一列是数值类型的，那么可以使用`auto_increment`来完成值的自动增长

```sql
CREATE TABLE stu(
	id INT PRIMARY KEY auto_increment,	-- 创建表的时候，添加主键约束，并且完成主键自动增长
	NAME VARCHAR(20) UNIQUE
);
```

```sql
ALTER TABLE stu MODIFY id INT auto_increment;  	-- 创建表完成之后，添加自动增长
```

```sql
ALTER TABLE stu MODIFY id INT;		-- 删除自动增长
```

**检查约束和默认约束**

```sql
CREATE TABLE tb_user (
    id     INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID唯一标识',
    name   VARCHAR(10) NOT NULL UNIQUE COMMENT '姓名',
    age    INT CHECK (age > 0 && age <= 120) COMMENT '年龄',  	-- 检查约束
    status CHAR(1) DEFAULT '1' COMMENT '状态',				   -- 默认约束
    gender CHAR(1) COMMENT '性别'
);
```

## 5.4 外键约束

外键约束：`FOREIGN KEY`, 就是让表与表之间产生关系，更改表的时候如果有错误会警告，从而保证数据的正确性。

有两种方式来添加外键

1. 创建表的时候，添加外键

   ```sql
   create table 表名(
   	...，
   	外键列，
   	constraint 外键名称 foreign key (外键字段名称) references 主表名称(主表字段名称)
   );
   ```

   ```sql
   -- 创建部门表 （id, dep_name, dep_location）
   -- 一方，主表
   create table department(
   	id int primary key auto_increment,
   	dep_name varchar(20),
   	dep_location varchar(20)
   );
   ```

   ```sql
   -- 创建员工表（id, name, age, dep_id）
   -- 多方，从表
   create table employee(
   	id int primary key auto_increment,
   	name varchar(20),
   	age int,
   	dep_id int,		-- 外键对应主表的主键 外键字段名称
   	constraint emp_dept_fk foreign key (dep_id) references department(id)
   );
   ```

   ```sql
   -- 添加部门
   INSERT INTO department (id, dep_name, dep_location) VALUES (1, "研发部", "广州");
   INSERT INTO department (id, dep_name, dep_location) VALUES (2, "销售部", "深圳");
   -- 添加员工,dep_id代表员工所在的部门
   INSERT INTO employee (id, NAME, age, dep_id) VALUES (1, "张三", 20, 1);
   INSERT INTO employee (id, NAME, age, dep_id) VALUES (2, "李四", 21, 1);
   INSERT INTO employee (id, NAME, age, dep_id) VALUES (3, "王五", 20, 1);
   INSERT INTO employee (id, NAME, age, dep_id) VALUES (4, "老王", 20, 2);
   INSERT INTO employee (id, NAME, age, dep_id) VALUES (5, "大王", 22, 2);
   INSERT INTO employee (id, NAME, age, dep_id) VALUES (6, "小王", 18, 2);
   ```

   ![](..\图片\2-01【MySQL】\3.png)

2. 创建表之后，添加外键

   ```sql
   alter table employee add constraint emp_dept_fk foreign key (dep_id) references department(id);
   -- add后面与创建表的时候的语句一样
   ```

3. 删除外键

   ```sql
   alter table 表名 drop foreign key 外键名称;
   ```

**级联操作**

由于添加外键之后，多表之间是产生了联系了的，因此操作的时候，是操作不了的，但是我们想要操作的话，可以添加级联操作

| 行为        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| NO ACTION   | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则不允许删除/更新（与RESTRICT一致） |
| RESTRICT    | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则不允许删除/更新（与NO ACTION一致） |
| CASCADE     | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则也删除/更新外键在子表中的记录 |
| SET NULL    | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则设置子表中该外键值为null（要求该外键允许为null） |
| SET DEFAULT | 父表有变更时，子表将外键设为一个默认值（Innodb不支持）       |

使用方法如下：

1. 级联更新：`ON UPDATE CASCADE`，更新则外键对应的数据也会更新。

2. 级联删除：`ON DELETE CASCADE`，删除则外键对应的数据也会删除。


```sql
ALTER TAABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY （外键字段名称） REFERENCES 主表名称 （主表字段名称） ON UPDATE CASCADE ON DELETE CASCADE;				-- CASCADE可以替换为其他
```

```sql
alter table employee add constraint emp_dept_fk foreign key (dep_id) references department(id) ON UPDATE CASCADE ON DELETE CASCADE;
```

# 第四章 多表查询

多表查询分类：

* 连接查询

  内连接：相当于查询A、B交集部分数据

  外连接：

  * 左外连接：查询左所有数据，以及两张表交集部分数据
  * 右外连接：查询右表所有数据，以及两张表交集部分数据

  自连接：当前表与自身的连接查询，自连接必须使用表别名

* 子查询

![](D:\Java\笔记\图片\2-01【MySQL】\4.png)

## 4.1 多表关系

多表关系有：

1. 一对一(了解)。如：人和身份证

   实现方式：一对一关系实现，可以在任意一方添加唯一外键指向另一方的主键。

   通常一对一的关系，我们可以合成一张表来实现。

2. 一对多(多对一)。 如：部门和员工

   实现方式：在多的一方建立外键，指向一的一方的主键。

   ![](D:\Java\笔记\图片\2-01【MySQL】\5.png)

3. 多对多。如：学生和课程

   实现方式：多对多关系实现需要借助第三张中间表。

   中间表至少包含两个字段，这两个字段作为第三张表的外键，分别指向两张表的主键。

   ![](D:\Java\笔记\图片\2-01【MySQL】\6.png)

## 4.2 多表查询语法

```sql
-- 创建部门表
CREATE TABLE dept(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20)
);

INSERT INTO dept (NAME) VALUES ("开发部"), ("市场部"), ("财务部");
```

```sql
-- 创建员工表
CREATE TABLE emp(
    id INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(20),
    gender CHAR(1),
    salary DOUBLE,
    join_date DATE,
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES dept(id)	-- 外键，关联部门表
);

-- 添加6条数据
INSERT INTO emp VALUES (null, "孙悟空", "男", 7200, "2013-02-24", 1),
					(null, "猪八戒", "男", 3600, "2010-12-02", 1),
					(null, "唐僧", "男", 9000, "2008-08-08", 2),
					(null, "白骨精", "女", 5000, "2015-10-07", 2),
					(null, "蜘蛛精", "女", 4500, "2011-3-14", 3),
					(null, '白龙马', '男', 3800, '2000-06-05', NULL);
```

多表查询语句：

```sql
select * from dept, emp;			-- 查询结果一共有18条数据
```

可是这样会查询出来很多的数据，这就是因为笛卡尔积了！

**笛卡尔积**：

* 有两个集合，A，B，取两个集合的所有组成情况。例如上面的表，有两个表，外键存在，笛卡尔积为18，所以上面使用多表查询语句查询后会出来18条结果。
* 因此要完成多表查询，需要消除无用的数据。

```sql
SELECT * FROM emp, dept WHERE emp.`dept_id` = dept.`id`;
```

```sh
# 查询结果如下，一共5条数据：
+--------+--------+
| NAME   | NAME   |
+--------+--------+
| 孙悟空 | 开发部 |
| 猪八戒 | 开发部 |
| 唐僧   | 市场部 |
| 白骨精 | 市场部 |
| 蜘蛛精 | 财务部 |			# 需要注意的是，这里并没有查询出来白龙马，这是因为白龙马的部门为NULL
+--------+--------+
```

## 4.3 内连接查询

内连接查询：相当于查询A、B交集部分数据

![](D:\Java\笔记\图片\2-01【MySQL】\4.png)

**隐式内连接**

```sql
SELECT * FROM emp, dept WHERE emp.dept_id = dept.id;
```

**显示内连接**

```sql
select 字段列表 from 表名1 [inner] join 表名2 on 条件			-- 显示内连接语法
```

```sql
SELECT emp.NAME, dept.NAME from dept INNER JOIN emp ON dept.id = emp.dept_id;
SELECT emp.NAME, dept.NAME from dept JOIN emp ON dept.id = emp.dept_id;   -- inner可选可不选
```

```sh
# 查询结果如下，一共5条数据：
+--------+--------+
| NAME   | NAME   |
+--------+--------+
| 孙悟空 | 开发部 |
| 猪八戒 | 开发部 |
| 唐僧   | 市场部 |
| 白骨精 | 市场部 |
| 蜘蛛精 | 财务部 |			# 需要注意的是，这里并没有查询出来白龙马，这是因为白龙马的部门为NULL
+--------+--------+
```

## 4.4 外连接查询

之前我们查询数据，一直没有查询出来白龙马，因为它的部门是NULL，而我们想要查询白龙马的话就可以使用外连接了。外连接有两种，左外连接和右外连接：

* 左外连接：查询左表所有数据，以及两张表交集部分数据
* 右外连接：查询右表所有数据，以及两张表交集部分数据

![](D:\Java\笔记\图片\2-01【MySQL】\4.png)

**左外连接查询**

左外连接相当于查询表1(左表)的所有数据，当然也包含表1和表2交集部分的数据。

```sql
SELECT 字段列表 FROM 表1 LEFT [ OUTER ] JOIN 表2 ON 条件 ... ;
```

```sql
SELECT emp.NAME, dept.NAME from emp LEFT JOIN dept ON emp.dept_id = dept.id;
```

```sh
+--------+--------+
| NAME   | NAME   |
+--------+--------+
| 孙悟空 | 开发部 |
| 猪八戒 | 开发部 |
| 唐僧   | 市场部 |
| 白骨精 | 市场部 |
| 蜘蛛精 | 财务部 |
| 白龙马 | NULL   |				# 这时候就能够查询出来白龙马了
+--------+--------+
```

**右外连接查询**

右外连接相当于查询表2(右表)的所有数据，当然也包含表1和表2交集部分的数据。

```sql
 SELECT 字段列表 FROM 表1 RIGHT [ OUTER ] JOIN 表2 ON 条件 ... ;
```

## 4.5 自连接查询

**自连接查询**

自连接查询，顾名思义，就是自己连接自己，也就是把一张表连接查询多次。我们先来学习一下自连接的查询语法： 

```sql
SELECT 字段列表 FROM 表A 别名A JOIN 表A 别名B ON 条件 ... ;
```

而对于自连接查询，可以是内连接查询，也可以是外连接查询。

```sql
select a.name, b.name from emp a, emp b where a.managerid = b.id;  -- 查询员工及其所属领导的名字
```

```sql
-- 查询所有员工 emp 及其领导的名字 emp , 如果员工没有领导, 也需要查询出来 因此使用左外连接查询
select a.name '员工', b.name '领导' from emp a left join emp b on a.managerid = b.id;   
```

> 在自连接查询中，必须要为表起别名，要不然我们不清楚所指定的条件、返回的字段，到底是哪一张表的字段。

**联合查询**

对于union查询，就是把多次查询的结果合并起来，形成一个新的查询结果集。

```sql
SELECT 字段列表 FROM 表A ...
UNION [ ALL ]
SELECT 字段列表 FROM 表B ....;
```

- 对于联合查询的多张表的列数必须保持一致，字段类型也需要保持一致。 
- union all 会将全部的数据直接合并在一起，union 会对合并之后的数据去重。

> 如果多条查询语句查询出来的结果，字段数量不一致，在进行union/union all联合查询时，将会报错。

## 4.6 子查询

SQL语句中嵌套SELECT语句，称为嵌套查询，又称子查询。子查询：查询中嵌套查询，称嵌套查询为子查询。

```sql
SELECT * FROM t1 WHERE column1 = ( SELECT column1 FROM t2 );
```

子查询外部的语句可以是INSERT / UPDATE / DELETE / SELECT 的任何一个。

根据子查询结果不同，分为： 

- A. 标量子查询(子查询结果为单个值） 
- B. 列子查询(子查询结果为一列) 
- C. 行子查询(子查询结果为一行) 
- D. 表子查询(子查询结果为多行多列) 

根据子查询位置，分为： 

- A. WHERE之后 
- B. FROM之后 
- C. SELECT之后

**标量子查询** 

子查询返回的结果是单个值（数字、字符串、日期等），最简单的形式，这种子查询称为标量子查询。 常用的操作符：`= <> > >= < <=` 

案例: 查询 "销售部" 的所有员工信息 

完成这个需求时，我们可以将需求分解为两步： 

1. 查询 "销售部" 部门ID 

   ```sql
   select id from dept where name = '销售部';
   ```

2. 根据 "销售部" 部门ID, 查询员工信息

   ```sql
   select * from emp where dept_id = (select id from dept where name = '销售部');
   ```

**列子查询** 

子查询返回的结果是一列（可以是多行），这种子查询称为列子查询。 常用的操作符：IN 、NOT IN 、 ANY 、SOME 、 ALL

| 操作符 | 描述                                   |
| ------ | -------------------------------------- |
| IN     | 在指定的集合范围内，多选一             |
| NOT IN | 不在指定的集合范围内                   |
| ANY    | 子查询返回列表中，有任意一个满足即可   |
| SOME   | 与ANY等同，使用SOME的地方都可以使用ANY |
| ALL    | 子查询返回列表的所有值都必须满足       |

```sql
-- 查询销售部和市场部的所有员工信息
select * from employee where dept in (select id from dept where name = '销售部' or name = '市场部');
-- 查询比财务部所有人工资都高的员工信息
select * from employee where salary > all(select salary from employee where dept = (select id from dept where name = '财务部'));
-- 查询比研发部任意一人工资高的员工信息
select * from employee where salary > any (select salary from employee where dept = (select id from dept where name = '研发部'));
```

**行子查询**

返回的结果是一行（可以是多列）。

常用操作符：`=, <, >, IN, NOT IN`

```sql
-- 查询与xxx的薪资及直属领导相同的员工信息
select * from employee where (salary, manager) = (12500, 1);
select * from employee where (salary, manager) = (select salary, manager from employee where name = 'xxx');
```

**表子查询**

返回的结果是多行多列

常用操作符：`IN`

```sql
-- 查询与xxx1，xxx2的职位和薪资相同的员工
select * from employee where (job, salary) in (select job, salary from employee where name = 'xxx1' or name = 'xxx2');
-- 查询入职日期是2006-01-01之后的员工，及其部门信息
select e.*, d.* from (select * from employee where entrydate > '2006-01-01') as e left join dept as d on e.dept = d.id;
```

# 第五章 数据库设计范式

设计关系数据库时，遵从不同的规范要求，设计出合理的关系型数据库，这些不同的规范要求被称为不同的范式，各种范式呈递次规范，越高的范式数据库冗余越小。

关系数据库有六种范式：`第一范式（1NF）`、`第二范式（2NF）`、`第三范式（3NF）`、`巴斯-科德范式（BCNF）`、`第四范式(4NF）`和`第五范式（5NF，又称完美范式）`。

基本概念如下：

* 函数依赖：`A --> B`，如果通过A属性(属性组)的值，可以确定唯一B属性的值，那么称B依赖于A。

  例如：学号 --> 姓名	（学号，学号课程）--> 分数

* 完全函数依赖：`A --> B`，如果A是一个属性组，则B属性值的确定需要依赖于A属性组中所有的属性值。

  例如：（学号，学号课程）--> 分数

* 部分函数依赖：`A --> B`，如果A是一个属性组，则B属性值的确定只需要依赖于A属性组中某一些值。

  例如：（学号，学号课程）--> 姓名

* 传递函数依赖：`A --> B`，`B --> C`，如果通过A属性(属性组)的值，可以确定唯一B属性的值，在通过B属性(属性组)的值可以确定唯一C属性的值，则称C传递函数依赖于A。

  例如：学号 --> 系名，系名 --> 系主任

* 码：如果在一张表中，一个属性或者属性组，被其他所有的属性所完全依赖，则称这个属性(属性组)为该表的码。

  例如：该表中的码：（学号，课程名称）。

* 主属性：码属性组中的所有属性。

* 非主属性：除码属性组的属性。

关系数据库有六种范式：`第一范式（1NF）`、`第二范式（2NF）`、`第三范式（3NF）`、`巴斯-科德范式（BCNF）`、`第四范式(4NF）`和`第五范式（5NF，又称完美范式）`，下面介绍一下前三种范式：

1. `第一范式（1NF）`：数据库表的每一列都是不可分割的原子数据项 
2. `第二范式（2NF）`：在1NF的基础上，非码属性必须完全依赖于候选码（在1NF基础上消除非主属性对主码的部分函数依赖） 
3. `第三范式（3NF）`：在2NF基础上，任何非主属性不依赖于其它非主属性（在2NF基础上消除传递依赖）

## 5.1 第一范式

一范式，关系数据库已经帮我们控制好了。 

## 5.2 第二范式

二范式，就是要有主键，其他属性都要依赖于这个主键。（部分函数依赖指的是存在组合关键字中的某些字段决定非关键字段的情况），也即所有非关键字段都完全依赖于任意一组候选关键字。 

假定选课关系表为`SelectCourse(学号, 姓名, 年龄, 课程名称, 成绩, 学分)`，关键字为组合关键字`(学号, 课程名称)`，因为存在如下决定关系：

`(学号, 课程名称) → (姓名, 年龄, 成绩, 学分)`。这个数据库表不满足第二范式，因为存在如下决定关系： `(课程名称) → (学分)`； `(学号) → (姓名, 年龄)`。即存在组合关键字中的字段决定非关键字的情况。

由于不符合2NF，这个选课关系表会存在如下问题：

1. 数据冗余：

   同一门课程由n个学生选修，"学分"就重复n-1次；同一个学生选修了m门课程，姓名和年龄就重复了m-1次。

2. 更新异常：

   若调整了某门课程的学分，数据表中所有行的"学分"值都要更新，否则会出现同一门课程学分不同的情况。

3. 插入异常：

   假设要开设一门新的课程，暂时还没有人选修。这样，由于还没有"学号"关键字，课程名称和学分也无法记录入数据库。

4. 删除异常：

   假设一批学生已经完成课程的选修，这些选修记录就应该从数据库表中删除。但是，与此同时，课程名称和学分信息也被删除了。很显然，这也会导致插入异常。

把选课关系表SelectCourse改为如下三个表：

* 学生：`Student(学号, 姓名, 年龄)`；

* 课程：`Course(课程名称, 学分)`；

* 选课关系：`SelectCourse(学号, 课程名称, 成绩)`。

这样的数据库表是符合第二范式的，消除了数据冗余、更新异常、插入异常和删除异常。另外，所有单关键字的数据库表都符合第二范式，因为不可能存在组合关键字。

## 5.3 第三范式

三范式，就是不能有冗余，一张表，只能有主键，依赖主键的属性，外键，不能包含外键表的非主键属性。

假定学生关系表为`Student(学号, 姓名, 年龄, 所在学院, 学院地点, 学院电话)`，关键字为单一关键字"学号"，因为存在如下决定关系： `(学号) → (姓名, 年龄, 所在学院, 学院地点, 学院电话)` 

这个数据库是符合2NF的，但是不符合3NF，因为存在如下决定关系：      `(学号) → (所在学院) → (学院地点, 学院电话)` 。即存在非关键字段"学院地点"、"学院电话"对关键字段"学号"的传递函数依赖。      

它也会存在数据冗余、更新异常、插入异常和删除异常的情况。      

把学生关系表分为如下两个表：`学生：(学号, 姓名, 年龄, 所在学院)`； `学院：(学院, 地点, 电话)`。

这样的数据库表是符合第三范式的，消除了数据冗余、更新异常、插入异常和删除异常。 

# 第五章 事务

事务是一组操作的集合，事务会把所有操作作为一个整体一起向系统提交或撤销操作请求，即这些操作要么同时成功，要么同时失败。

如果一个包含多个步骤的业务操作，被事务管理，那么这些操作要么同时成功，要么同时失败。

1. 开启事务：`start transaction;`
2. 回滚：`rollback;`
3. 提交：`commit;`

```sql
-- 开启事务，只要不提交或者回滚，一直在事务中，数据为临时数据
START TRANSACTION;

UPDATE account SET balance = balance - 500 WHERE NAME = "张三";

UPDATE account SET balance = balance + 500 WHERE NAME = "李四";

-- 发现问题，回滚
ROLLBACK;
-- 没有问题，提交
COMMIT;
```

MySQL数据库中事务是默认自动提交的。

事务提交的两种方式：

* 自动提交：MySQL就是自动提交的，一条DML（增删改）语句会自动提交一次事务。
* 手动提交：Oracle数据库默认是手动提交事务，需要先开启事务，然后再提交。

修改事务的默认提交方式：

* 查看事务的默认提交方式：

  ```sql
  select @@autocommit;		-- 1代表自动提交   0代表手动提交
  ```

* 修改默认提交方式

  ```sql
  set @@autocommit = 0；
  ```

## 7.1 事务的四大特性

`ACID`

* A原子性(atomicity)：事务中的所有操作，要么全部成功，要么全部失败。或称不可分割性。
* C一致性(consistency)：要保证数据库内部完整性约束、声明性约束。
* I隔离性(isolation)：对同一资源操作的事务不能够同时发生。又称独立性。
* D持久性(durability)：对数据库做出的一切修改将永久保存，不管是否出现故障。

## 7.2 并发事务常见问题

多个事务之间隔离的，相互独立的。但是如果多个事务操作同一批数据，则会引发一些问题，设置不同的隔离级别就可以解决这些问题。

存在问题如下

1. **脏读**：一个事务，读取到另一个事务中没有提交的数据。

   事务A读取了事务B更新的数据，然后B回滚操作，那么A读取到的数据是脏数据

2. **不可重复读（虚读）**：在同一个事务中，两次读取到的数据不一样。

   事务 A 多次读取同一数据，事务 B 在事务A多次读取的过程中，对数据作了更新并提交，导致事务A多次读取同一数据时，结果不一致。

3. **幻读**：一个事务操作（DML）数据表中所有记录，另一个事务添加了一条数据，则第一个事务查询不到自己的修改。

   系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这就叫幻读。

> 小结：不可重复读的和幻读很容易混淆，不可重复读侧重于修改，幻读侧重于新增或删除。解决不可重复读的问题只需锁住满足条件的行，解决幻读需要锁表

| 问题       | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| 脏读       | 一个事务读到另一个事务还没提交的数据                         |
| 不可重复读 | 一个事务先后读取同一条记录，但两次读取的数据不同             |
| 幻读       | 一个事务按照条件查询数据时，没有对应的数据行，但是再插入数据时，又发现这行数据已经存在 |

## 7.3 事务四种隔离级别

隔离级别一共有四种：

1. `read uncommitted`：读未提交

   产生的问题：脏读、不可重复读、幻读。

2. `read committed`：读已提交(Oracle默认隔离级别)

   产生的问题：不可重复读、幻读。

3. `repeatable read`：可重复读(MySQL默认级别)

   产生的问题：幻读。

4. `serializable`：串行化

   可以解决所有的问题

> 注意：隔离级别从小到大安全性越来越高，但是效率越来越低。隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大。

数据库查询隔离级别：

```sql
select @@tx_isolation;
```

数据库设置隔离级别：

```sql
set global transaction isolation level 级别字符串;
```

1. 设置隔离级别是`repeatable read`可重复读，那么会把不可重复读消灭掉，想要在右表读取到数据更改，那么只能手动结束右表的事务，committed或者rollback。但是留着幻读，幻读，我们演示不了
2. 设置隔离级别是`serializable`串行化，类似于加一个锁，就是在该事务中，其他事务影响不了。只要设置了该隔离级别，那么其他的事务甚至都不能查询该事务中修改的表。右表不能够查询account表。只有等左边的事务回滚或者提交结束事务，右边的事务才能够显示出来account表格，否则一直在等待。

### read uncommitted

读未提交`read uncommitted`：

> 场景：张三给李四转账500元，由于这是事务中，所以是临时的，可以被回滚或者提交。这时候我们再打开一个客户端，让李四去查询，李四查询的时候发现账户已经修改了，那么相信了张三已经转账了，就会打欠条了。可是如果直接回滚，会发现数据都会归为原来。
>
> 产生的问题是：脏读、不可重复读、幻读(无法演示)

1. 打开一个客户端A，并设置当前事务模式为`read uncommitted`：读未提交，查询表account的初始值：

![](D:\Java\笔记\图片\2-01【MySQL】\7-1.png)

2. 在客户端A的事务提交之前，打开另一个客户端B，更新表account：

![](D:\Java\笔记\图片\2-01【MySQL】\7-2.png)

3. 这时，虽然客户端B的事务还没提交，但是客户端A就可以查询到B已经更新的数据：

![](D:\Java\笔记\图片\2-01【MySQL】\7-3.png)

4. 一旦客户端B的事务因为某种原因回滚，所有的操作都将会被撤销，那客户端A查询到的数据其实就是脏数据：

![](D:\Java\笔记\图片\2-01【MySQL】\7-4.png)

### read committed

读已提交。设置隔离级别是`read committed`读已提交，会把脏读给消灭掉，但是依然会留着不可重复读。

1. 打开一个客户端A，并设置当前事务模式为`read committed（读已提交）`，查询表`account`的所有记录：

![](D:\Java\笔记\图片\2-01【MySQL】\7-6.png)

2. 在客户端A的事务提交之前，打开另一个客户端B，更新表`account`：

![](D:\Java\笔记\图片\2-01【MySQL】\7-7.png)

3. 这时，客户端B的事务还没提交，客户端A不能查询到B已经更新的数据，解决了脏读问题：

![](D:\Java\笔记\图片\2-01【MySQL】\7-8.png)

4. 客户端B的事务提交

![](D:\Java\笔记\图片\2-01【MySQL】\7-9.png)

5. 客户端A执行与上一步相同的查询，结果与上一步不一致，即产生了不可重复读的问题

![](D:\Java\笔记\图片\2-01【MySQL】\7-10.png)

### repeatable read

可重复读。设置隔离级别是`repeatable read`可重复读，那么会把不可重复读消灭掉，想要在客户端A读取到数据更改，那么只能手动结束客户端A的事务，`committed`或者`rollback`。

1. 打开一个客户端A，并设置当前事务模式为`repeatable read`，查询表`account`的所有记录

![](D:\Java\笔记\图片\2-01【MySQL】\7-11.png)

2. 在客户端A的事务提交之前，打开另一个客户端B，更新表`account`并提交


![](D:\Java\笔记\图片\2-01【MySQL】\7-12.png)

3. 在客户端A查询表`account`的所有记录，与步骤（1）查询结果一致，没有出现不可重复读的问题

![](D:\Java\笔记\图片\2-01【MySQL】\7-13.png)

4. 在客户端A，接着执行`update balance = balance - 50 where id = 1`，`balance`没有变成`400-50=350`，lilei的balance值用的是步骤（2）中的350来算的，所以是300，数据的一致性倒是没有被破坏。

   ![](D:\Java\笔记\图片\2-01【MySQL】\7-14.png)

5. 重新打开客户端B，插入一条新数据后提交

![](D:\Java\笔记\图片\2-01【MySQL】\7-15.png)

6. 在客户端A计算balance之和，值为300+16000+2400=18700，没有把客户端B的值算进去，客户端A提交后再计算balance之和，居然变成了19300，这是因为把客户端B的600算进去了。

   站在客户的角度，客户是看不到客户端B的，它会觉得是天下掉馅饼了，多了600块，这就是幻读，站在开发者的角度，数据的一致性并没有破坏。

   但是在应用程序中，我们的代码可能会把18700提交给用户了，如果你一定要避免这情况小概率状况的发生，那么就要采取下面要介绍的事务隔离级别“串行化”


### serializable　

串行化，设置隔离级别是`serializable`串行化，类似于加一个锁，就是在该事务中，其他事务影响不了。只要设置了该隔离级别，那么其他的事务甚至都不能查询该事务中修改的表。右表不能够查询account表。只有等左边的事务回滚或者提交结束事务，右边的事务才能够显示出来account表格，否则一直在等待。

1. 打开一个客户端A，并设置当前事务模式为serializable，查询表account的初始值：

   ```sql
   mysql> set session transaction isolation level serializable;
   Query OK, 0 rows affected (0.00 sec)
   
   mysql> start transaction;
   Query OK, 0 rows affected (0.00 sec)
   
   mysql> select * from account;
   +------+--------+---------+
   | id | name | balance |
   +------+--------+---------+
   | 1 | lilei | 10000 |
   | 2 | hanmei | 10000 |
   | 3 | lucy | 10000 |
   | 4 | lily | 10000 |
   +------+--------+---------+
   rows in set (0.00 sec)
   ```

2. 打开一个客户端B，并设置当前事务模式为serializable，插入一条记录报错，表被锁了插入失败，mysql中事务隔离级别为serializable时会锁表，因此不会出现幻读的情况，这种隔离级别并发性极低，往往一个事务霸占了一张表，其他成千上万个事务只有干瞪眼，得等他用完提交才可以使用，开发中很少会用到。

   ```sql
   mysql> set session transaction isolation level serializable;
   Query OK, 0 rows affected (0.00 sec)
   
   mysql> start transaction;
   Query OK, 0 rows affected (0.00 sec)
   
   mysql> insert into account values(5,'tom',0);
   ERROR 1205 (HY000): Lock wait timeout exceeded; try restarting transaction
   ```
