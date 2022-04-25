day02 【MySQL基础】

<!--P476-->

# 第一章 数据库基本概念

<!--P477-->

数据库英文单词：`DataBase`	简称：`DB`

数据库：用于存储和管理数据的仓库。

数据库特点

1. 持久化存储数据的。其实数据库就是一个文件系统
2. 方便存储和管理数据
3. 使用了统一的方式操作数据库 -- `SQL`

<!--P478-->

常见的数据库软件

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\1.png)

# 第二章 MySQL数据库软件

## 2.1 安装

<!--P479-->

## 2.2 卸载

<!--P480-->

1. 去MySQL的安装目录找到`my.ini`文件

   ```sql
   datadir="C:/ProgramData/MySQL/MySQL Server 5.5/Data/"
   ```

2. 卸载`MySQL`

3. 删除`C:/ProgramData`目录下面的MySQL文件夹（隐藏文件夹）

## 2.3 配置

<!--P481-->

**MySQL服务启动**

1. 手动启动
2. cmd命令窗口 --> 输入services.msc 打开服务的窗口
3. 使用管理员权限打开cmd
   * `net start mysql`：启动mysql服务
   * `net stop mysql`：关闭mysql服务

<!--P482-->

**MySQL登陆**

1. `mysql -uroot -p+密码`
2. `mysql -h+ip -uroot -p+连接目标的密码`（这种可以远程登陆MySQL）
3. `mysql --host = ip        --user = root         --password = 连接目标的密码`

**MySQL退出**

1. `exit`
2. `quit`
3. 直接叉掉窗口，但不建议

**MySQL目录结构**

<!--P483 1.20 之前有着P474和P475-->

1. MySQL安装目录
   * 配置文件 `my.ini`
2. MySQL数据目录
   * 数据库：文件夹
   * 表：文件
   * 数据：数据

# 第三章 SQL

<!--P484-->

## 3.1 什么是SQL

`Structured Query Language`：结构化查询语言

其实就是定义了操作所有**关系型数据库**的规则。

每一种数据库操作的方式存在不一样的地方，称为"方言"。

## 3.2 SQL通用语法

<!--P485-->

* SQL语句可以单行或者多行书写，以分号结尾。

  ```sql
  mysql> show databases
      ->
      ->
      -> ;
  ```

* 可使用空格和缩进来增强语句的可读性。

  ```sql
  mysql> show      databases      ;
  ```

* MySQL数据库的SQL语句不区分大小写，关键字建议使用大写。

  ```sql
  mysql> SHOW DATABASES;
  ```

* 注释

  * 单行注释第一种：`-- 注释内容`**（这种注释方式，两个横杠与注释内容之间务必要空格）**

    ```sql
    mysql> SHOW DATABASES; -- 查询所有的数据库名称
    ```

  * 单行注释第二种：`#注释内容`

    ```sql
    mysql> SHOW DATABASES; #查询所有的数据库名称
    ```

  * 多行注释：`/* */`

    ```sql
    mysql> SHOW DATABASES; /*查询所有的数据库名称*/
    ```

## 3.3 SQL分类

<!--P486-->

SQL语言共分为四大类：数据查询语言DQL，数据操纵语言DML，数据定义语言DDL，数据控制语言DCL。 

1. DDL（Data Definition Language）数据定义语言

   用来定义数据库对象：数据库，表，列等。关键字：create，drop，alter等。

2. DML（Data Manipulation  Language）数据操作语言

   用来对数据库总表的数据进行增删改。关键字：insert，delete，update等。

3. DQL（Data Query Language）数据查询语言

   用来查询数据库中表的记录（数据）。关键字：select，where等。

4. DCL（Data Control Language）数据控制语言

   用来定义数据库的访问权限和安全级别，及创建用户。关键字：GRANT, REVOKE等

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\3.png)

## 3.4 DDL操作数据库、表

### 操作数据库

<!--P487-->

<!--P488-->

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
   create database if not exists db4 character set gbk;		-- 创建db4数据库，判断是否存在，并制定字符集为gbk 
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

### 操作表

<!--P489-->

<!--P490-->

<!--P491-->

<!--P492 1.21-->

1. C（create）：创建

   ```sql
   creat table 表名(
   	列名1 数据类型1，
   	列名2 数据类型2，
   	...
   	列名n 数据类型n  -- 这里不加逗号
   );
   ```

   数据库类型：

   * int：整数类型	age int

   * double：小数类型

   * date：日期，只包含年月日，yyy-MM-dd

   * datetime：日期，包含年月日时分秒 yyyy-MM-dd HH:mm:ss

   * timestamp：时间戳类型 包含年月日时分秒

     如果不给这个字段赋值，或者赋值为null，那么默认使用当前的系统时间来自动赋值

   * varchar：字符串    name varchar(20):姓名最大20个字符

   创建表：

   ```sql
   create table student(
   	id int,
   	name varchar(32),
   	age int,
   	score double(4,1),
   	birthday date,
   	insert_time timestamp
   );
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
   desc + 表名称;		--查询表结构
   ```
   ```sql
   show create table 表名;		--查询某个表的字符集（查询某个表的创建语句）
   ```

3. U（Update）：修改

   ```sql
   alter table 表名 rename to 新的表名；		-- 修改表名
   ```

   ```sql
   alter table 表名 character set 字符集名称;		-- 修改表的字符集
   ```

   ```sql
   alter table 表名 add 列名 数据类型;		-- 添加一列
   ```

   ```sql
   alter table 表名 change 列名 新列名 新数据类型;		--修改列名称 类型
   alter table 表名 modify 列名 新数据类型;		-- 修改列名称 类型
   ```

   ```sql
   alter table 表名 drop 需要删除的列名;		-- 删除列
   ```

4. D（Delete）：删除

   ```sql
   drop table 表名;		-- 删除表
   ```

   ```sql
   drop table if exists 表名;		-- 判断是否存在此表，存在删除
   ```


## 3.5 SQLyog图形化界面

<!--P493-->

## 3.6 DML增删改表中数据 

<!--P494-->

### 添加数据

```sql
insert into 表名(列名1，列名2，...列名n) values(值1，值2，...值n);
```

注意

1. 列名和值需要一一对应。

2. 如果表名后面不定义列名，那么默认给所有的列添加值

   如果缺少值，与列名不对应，那么会报错

3. 除了数字类型，其他类型需要使用引号（**单引号和双引号都可以**）引起来。

```sql
INSERT INTO stu(id, NAME) VALUES(1, "林炫");
INSERT INTO stu VALUES(1, "张三", 21, 10, "2001-11-11", NULL);		-- 不定义列名，直接全部赋值
```

### 删除数据

<!--P495-->

```sql
delete from 表名 [where条件];
-- DELETE FROM stu WHERE id = 1;
```

注意：

1. 如果不加上条件，则会删除表中所有的记录。

2. 如果要删除所有的记录，有下面两种方法

   ```sql
   delete from 表名;		-- 不推荐使用，效率低下，表里面有多少行就会执行多少
   ```

   ```sql
   truncate table 表名;		-- 推荐使用，效率更高，先删除表，然后再创建一个一样名称的空表。
   ```

### 修改数据

<!--P496-->

```sql
update 表名 set 列名 = 值1， 列名 = 值2， ...[where 条件];
```

注意：

* 如果不加上任何条件，那么会将表中所有的记录全部修改。

## 3.7 DQL查询表中记录

<!--P502 视频集数反了-->

### 语法

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

### 基础查询

**字段就是列**

* 多个字段的查询

  ```sql
  select 字段名1，字段名2,...from 表名; 
  ```

  如果查询所有的字段，那么可以使用*来代替字段列表。

  ```sql
  select * form 表名;
  ```

* 去除重复的值

  ```sql
  select distinct 字段名 from 表名;		-- distinct
  ```

  如果需要去除两列字段的重复值，需要两列字段值重复，才会去除。

* 计算列

  可以使用四则运算来计算一些列的值。+ - * /

  ```sql
  select 字段名，字段名，四则运算字段名（字段名1 + 字段名2） from 表名;
  ```

* 起别名

  ```sql
  select 字段名 as 新的名称，...from 表名;		-- as
  ```

  as关键字可以省略，用空格来代替

### 条件查询

<!--P503 1.22 视频集数反了-->

`where`字句后面跟条件语句

运算符：

* `> 、<、<=、>=、=、<>。`

  ```sql
  select * from 表名 where 列名 运算符 条件;
  select * from person where age > 20;		-- 挑选person表里面age列大于20的人
  ```

  注意：

  ​	运算符里面没有`==`，只有`=`，所以判断的时候用`=`就好了。

  ​	运算符`<>` 等价于 `!=` 。

* `between...and`

  ```sql
  select * from 表名 where 列名 between...and...;
  select * from person where age between 20 and 30;		-- 挑选person表里面age列大于等于20并且小于等于30
  select * from person where age >= 20 and <= 30;			-- 两者等价 
  ```

* `in(集合)`

  ```sql
  select * from 表名 where 列名 in(集合);
  select * from person where age in(22, 18, 55);		-- 挑选person表里面age列的22，18,55行。
  select * from person where age 22 or 18 or 55;		-- 等价于这一行
  ```

* `is null`

  ```sql
  select * from 表名 where 列名 is null;
  select * from person where english is null;			-- 查询英语成绩为null
  select * from person where english is not null;		-- 查询英语成绩不为null
  ```

* `and`或者`&&`    `or`或者`||`    `not`或者`！`

  ```sql
  select * from 表名 where 列名 and或者&& 条件;
  select * from person where age > 20 && age < 50;		-- 挑选person表里面age列大于20,并且小于50的人。 										注意：不建议这样使用，可以使用and关键字
  select * from person where age > 20 and age < 50;		-- 建议使用
  ```

  ```sql
  select * from 表名 where 列名 or或者|| 条件;
  ```

  ```
  select * from 表名 where 列名 not或者！ 条件;
  ```

* `like`：模糊查询

  <!--P504-->

  占位符：

  ​	`"_"`：单个任意字符

  ​	`"%"`多个任意字符

  在SQL里面单双引号都可以

  ```sql
  select * from 表名 where 列名 like 条件;
  ```

  ```sql
  select * from person where name like "马%";			-- 查询姓马的人
  select * from person where name like "_化%";			-- 查询第二个字是化的人
  select * from person where name like "___";			-- 查询姓名是三个字的人
  select * from person where name like "%德%";			-- 查询姓名中包括德的人
  ```


<!--P497-->

## 3.8 DQL查询语句

### 排序查询

<!--P498-->

* 语法

  ```sql
  select * from 表名 order by 字句；
  select * from 表名 order by 排序字段1 排序方式1，排序字段2 排序方式2...;
  ```

* 排序方式

  ```sql
  ASC：升序，默认的。
  DESC：降序。
  ```

* 注意：

  如果有多个排序条件，则当前面的条件值一样的时候，就会排序第二条件。

```sql
SELECT * FROM person ORDER BY math;		-- 默认对math列进行升序排序
SELECT * FROM person ORDER BY math DESC;	-- 对math列进行降序排序
SELECT * FROM person ORDER BY math ASC, english DESC;	-- 对math列进行升序排序，math列有重复值的时候判断English列，按照english列进行降序排序
```

### 聚合函数

<!--P499-->

聚合函数：将一列数据作为一个整体，进行纵向计算。例如：计算个数，计算和，计算平均值。

聚合函数的计算是排除NULL值的，就是遇到有NULL值会忽略。

```sql
select 函数名称(列名称) from 表名称;
```

* `count`：计算个数

  ```sql
  select count(主键、ID列) from 表名称;		-- 一般选择非空的列：主键
  ```

  ```sql
  select count(*) from 表名称;		-- * 代表全部，找到一个非空的列来搞，不建议
  ```

* `max`：计算最大值

  ```sql
  select max(列名称) from 表名称;		-- 计算该表该列最大值
  ```

* `min`：计算最小值

  ```sql
  select min(列名称) from 表名称;		-- 计算该表该列最大值
  ```

* `sum`：计算和

  ```sql
  select sum(列名称) from 表名称;		-- 计算该表该列数值的和
  ```

* `avg`：计算平均值

  ```sql
  select avg(列名称) from 表名称;		-- 计算该表该列平均值
  ```

由于聚合函数的计算是排除`NULL`的值的，所以对此我们有两种解决方案：

1. 选择不包含非空的列进行计算

2. `IFNULL函数`

   ```sql
   select 函数(IFNULL(列名称， 值)) from 表名称;		-- IFNULL函数会判断NULL，如果是NULL那么会将NULL变为值
   select count(IFNULL(math, 0)) from student;		-- count函数计算math列个数，IFNULL函数对math列进行判断，如果有着NULL，那么将NULL变为0，其他的不改变
   ```

### 分组查询

<!--P500-->

分组查询就是将以行为单位，来进行查询。

```sql
select 分组查询字段 from 表名称 group by 列名;	-- 分组查询字段：分组字段、聚合函数
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

### 分页查询

<!--P501-->

分页查询，类似于百度搜索出来后有很多词条，词条按页码来出现。

```sql
select * from 表名 limit 开始的索引，每页查询的条数;
select * from person limit 0, 3;		-- 第1页
select * from person limit 3, 3;		-- 第2页
select * from person limit 6, 3;		-- 第3页
```

* 公式：开始的索引 = （当前的页码 - 1） * 每页显示的条数。
* 开始的索引是从0开始的
* 分页操作是一个“方言”， 其他的数据库都有各自的“方言”

## 3.9 DCL

1. DDL（Data Definition Language）数据定义语言

   用来定义数据库对象：数据库，表，列等。关键字：create，drop，alter等。

2. DML（Data Manipulation  Language）数据库操作语言

   用来对数据库总表的数据进行增删改。关键字：insert，delete，update等。

3. DQL（Data Query Language）数据查询语言

   用来查询数据库中表的记录（数据）。关键字：select，where等。

4. DCL（Data Control Language）数据控制语言

   用来定义数据库的访问权限和安全级别，及创建用户。关键字：GRANT, REVOKE等

<!--P536-->

DBA：数据库管理员

### 管理用户

**查询用户**

1. 切换到mysql数据库

   ```sql
   USE mysql;
   ```

2. 查询user表

   ```sql
   SELECT * FROM USER;
   ```

查询出来的用户有两个，一个的地址是localhost，另一个是%，%的意思是通配符，就是哪里都可以。

**添加用户**

```sql
CREATE USER "用户名"@"主机名" IDENTIFIED BY "密码";
CREATE USER "zhangsan"@"localhost" IDENTIFIED BY "123";	-- 主机名就是主机地址
```

**删除用户**

```sql
DROP USER "用户名"@"主机名";
DROP USER "zhangsan"@"localhost";
```

**修改用户密码**

<!--P537-->

有两种方式：

```sql
UPDATE USER SET PASSWORD = PASSWORD("新密码") WHERE USER = "用户名";	-- 放在password里面会自动加密
UPDATE USER SET PASSWORD = PASSWORD("abc") WHERE USER = "zhangsan";
```

```sql
SET PASSWORD FOR "用户名"@"主机名" = PASSWORD("新密码");
SET PASSWORD FOR "zhangsan"@"localhost" = PASSWORD("123");
```

假如我们修改root密码，之后忘记了那么也可以重新登陆MySQL。

1. 使用cmd命令行，输入：`net stop mysql`    停止mysql的服务，记得使用管理员权限。
2. 使用无验证的方式来启动mysql服务，输入：`mysqld -- skip-grant-tables`。
3. 打开另外一个cmd命令行窗口，输入：`mysql`,。
4. 使用命令行修改root用户密码，关闭两个命令行窗口。
5. 右键打开任务管理器，结束掉mysqld.exe进程。
6. 使用管理员权限打开cmd命令行，输入：`net start mysql`。
7. 这时候就可以使用新的密码来登陆了。

### 权限管理

<!--P538-->

**查询权限**

```sql
SHOW GRANTS FOR "用户名"@"主机名称";
SHOW GRANTS FOR "zhangsan"@"localhost";
```

**授予权限**

```sql
GRANT 权限列表 ON 数据库名称.表名 TO "用户名"@"主机名称";
GRANT SELECT, DELETE, UPDATE ON day3.account TO "zhangsan"@"localhost";
GRANT ALL ON *.* TO "zhangsan"@"localhost";	-- 授予全部权限
```

**撤销权限**

```sql
REVOKE 权限列表 ON 数据库名称.表名 FROM "用户名"@"主机名称";
REVOKE UPDATE ON day3.account FROM "zhangsan"@"localhost";
```



day04【MySQL多表、事务】

<!--P520-->

# 第四章 多表查询

<!--P521-->

## 4.1 多表查询语法

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

INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("孙悟空", "男", 7200, "2013-02-24", 1);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("猪八戒", "男", 3600, "2010-12-02", 1);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("唐僧", "男", 9000, "2008-08-08", 2);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("白骨精", "女", 5000, "2015-10-07", 2);
INSERT INTO emp (NAME, gender, salary, join_date, dept_id) VALUES ("蜘蛛精", "女", 4500, "2011-3-14", 3);
```

多表查询语句：

```sql
select * from dept, emp;
```

**笛卡尔积**：

* 有两个集合，A，B，取两个集合的所有组成情况。例如上面的表，有两个表，外键存在，笛卡尔积为15，所以上面使用多表查询语句查询后会出来15条结果。
* 因此要完成多表查询，需要消除无用的数据。

## 4.2 多表查询分类

### 内连接查询

<!--P522-->

隐式内连接

```sql
SELECT * FROM emp, dept WHERE emp.`dept_id` = dept.`id`;
```

```sql
-- 查询员工表的姓名，性别。部门表的名称
SELECT emp.`name`, emp.`gender`, dept.`name` FROM emp, dept WHERE emp.`dept_id` = dept.`id`;
```

```sql
-- 这是最规范的写法
SELECT 
	t1.`name`,	-- 员工表的姓名
	t1.`gender`,	-- 员工表的性别
	t2.`name`	-- 部门表的名称
FROM
	emp t1, 	-- 对员工表进行重命名
	dept t2		-- 对部门表进行重命名
WHERE 
	t1.`dept_id` = t2.`id`;
```

显示内连接

* 语法：

  ```sql
  select 字段列表 from 表名1 inner join 表名2 on 条件
  ```

* 例如：

  ```sql
  SELECT * FROM emp INNER JOIN dept ON emp.`dept_id` = dept.`id`;
  SELECT * FROM emp [INNER] JOIN dept ON emp.`dept_id` = dept.`id`;	-- inner可选 
  ```

内连接查询需要了解以下：

1. 从那些表中查询数据
2. 条件是什么
3. 查询那些字段

### 外连接查询

<!--P523-->

1. 左外连接：查询的是左表所有的数据以及其交集部分。

   ```sql
   select 字段列表 from 表1 left [outer] join 表2 on 条件
   ```

   ```sql
   SELECT emp.*, dept.name FROM emp LEFT JOIN dept ON emp.`dept_id` = dept.`id`;
   ```

2. 右外连接：查询的是右表所有的数据以及其交集部分。

### 子查询

<!--P524-->

子查询：查询中嵌套查询，称嵌套查询为子查询。

```sql
-- 查询工资最高的员工信息是多少 9000
select max(salary) from emp;
```

```sql
-- 查询工资等于9000的员工信息
select * from emp where emp.'salary' = 9000;
```

```sql
-- 以上操作可以合为一项
select * from emp where emp.'salary' = (select max(salary) from emp);
```

<!--P525 1.24-->

子查询的不同情况：

1. 子查询的结果是单行单列的。

   子查询作为条件，使用运算符来判断。运算符：`> >= < <= =`

   ```sql
   -- 查询平均工资
   SELECT AVG(salary) FROM emp;
   -- 查询小于平均工资的
   SELECT * FROM emp WHERE emp.`salary` < 5860;
   -- 子查询，来直接查询小于平均工资的
   SELECT * FROM emp WHERE emp.`salary` < (SELECT AVG(salary) FROM emp);
   ```

   子查询的定义是，查询中嵌套查询，称嵌套查询为子查询。在上述语句中，子查询为查询平均工资的语句，该语句的结果正是单行单列的。

2. 子查询的结果是多行单列的。

   子查询作为条件，使用运算符in来判断，相当于or。

   ```sql
   -- 查询“财务部”和“市场部”所有的员工信息
   -- 查询“财务部”和“市场部”所有的员工信息的id
   SELECT id FROM dept WHERE NAME = "财务部" OR NAME = "市场部";
   -- 查询id信息
   SELECT * FROM emp WHERE dept_id = 3 OR dept_id = 2;
   -- 查询“财务部”和“市场部”所有的员工信息
   SELECT * FROM emp WHERE dept_id IN(SELECT id FROM dept WHERE NAME = "财务部" OR NAME = "市场部");
   ```

3. 子查询的结果是多行多列的

   <!--P526-->

   子查询可以作为一张虚拟表，参与查询

   ```sql
   -- 查询员工入职日期是2011-11-11日之后的员工信息和部门信息
   -- 普通内连接
   SELECT * FROM emp, dept WHERE emp.`dept_id` = dept.`id` AND emp.`join_date` > "2011-11-11";
   -- 子查询
   SELECT * FROM dept t1, (SELECT * FROM emp WHERE join_date > "2011-11-11") t2 WHERE t1.id = t2.dept_id;
   ```

## 4.3 多表查询练习

<!--P527-->

### 创建表并添加信息

```sql
-- 部门表
CREATE TABLE dept (
	id INT PRIMARY KEY ,	-- 部门id
	dname VARCHAR(20), 	-- 部门名称
	location VARCHAR(20)	-- 部门所在地
);
-- 添加四个部门
INSERT INTO dept (id, dname, location) VALUES
(10, "教研部", "北京"),
(20, "学工部", "上海"), 
(30, "销售部", "广州"), 
(40, "财务部", "深圳");
```

```sql
-- 职务表
CREATE TABLE job (
	id INT PRIMARY KEY, 	-- 职务id
	jname VARCHAR(20), 	-- 职务名称
	description VARCHAR(50)	-- 职务描述
);
-- 添加四个职务
INSERT INTO job (id, jname, description) VALUES 
(1, "董事长", "管理整个公司"), 
(2, "经理", "管理部门员工"), 
(3, "销售员", "向客人推销产品"), 
(4, "文员", "使用办公软件");
```

```sql
-- 员工表
CREATE TABLE emp (
	id INT PRIMARY KEY, 	-- 员工id
	ename VARCHAR(20), 	-- 员工姓名
	job_id INT,		-- 职务id
	mgr INT, 		-- 上级领导
	joindate DATE, 		-- 入职日期
	salary DECIMAL(7, 2),	-- 工资
	bonus DECIMAL(7, 2), 	-- 奖金
	dept_id INT, 		-- 所在部门编号
	CONSTRAINT emp_jobid_ref_job_id_fk FOREIGN KEY (job_id) REFERENCES job (id), 
	CONSTRAINT emp_deptid_ref_dept_id_fk FOREIGN KEY (dept_id) REFERENCES dept(id)
);
-- 添加员工
INSERT INTO emp (id, ename, job_id, mgr, joindate, salary, bonus, dept_id) VALUES 
(1001, "孙悟空", 4, 1004, "2000-12-17", "8000.00", NULL, 20), 
(1002, "卢俊义", 3, 1006, "2001-02-20", "1600.00", "3000.00", 30), 
(1003, "林冲", 3, 1006, "2001-02-22", "12500.00", "5000.00", 30), 
(1004, "唐僧", 2, 1009, "2001-04-02", "29750.00", NULL, 20), 
(1005, "李逵", 4, 1006, "2001-09-26", "12800.00", "14000.00", 30), 
(1006, "宋江", 2, 1009, "2001-05-01", "28500.00", NULL, 30), 
(1007, "刘备", 2, 1009, "2001-09-01", "24500.00", NULL, 10), 
(1008, "猪八戒", 4, 1004, "2007-04-19", "30000.00", NULL, 20), 
(1009, "罗贯中", 1, NULL, "2001-11-17", "50000.00", NULL, 10), 
(1010, "吴用", 3, 1006, "2001-09-08", "15000.00", "0.00", 30), 
(1011, "沙僧", 4, 1004, "2007-05-23", "11000.00", NULL, 20), 
(1012, "李逵", 4, 1006, "2001-12-03", "9500.00", NULL, 30), 
(1013, "小白龙", 4, 1004, "2001-12-03", "30000.00", NULL, 20), 
(1014, "关羽", 4, 1007, "2002-12-03", "13000.00", NULL, 10);
```

```sql
-- 工资等级表
CREATE TABLE salarygrade (
	grade INT PRIMARY KEY,	-- 级别
	losalary INT, 		-- 最低工资
	hisalary INT 		-- 最高工资
);
-- 添加5个工资等级
INSERT INTO salarygrade(grade, losalary, hisalary) VALUES 
(1, 7000, 12000), 
(2, 12010, 14000), 
(3, 14010, 20000), 
(4, 20010, 30000), 
(5, 30010, 99990);
```

### 问题 需求

```sql
-- 1. 查询所有员工信息。查询员工编号，员工姓名，工资，职务名称，职务描述

-- 2. 查询员工编号，员工姓名，工资，职务名称，职务描述，部门名称，部门位置

-- 3. 查询员工姓名，工资，工资等级

-- 4. 查询员工姓名，工资，职务名称，职务描述，部门名称，部门位置，工资等级

-- 5. 查询部门编号，部门名称，部门位置，部门人数

-- 6. 查询所有员工的姓名及其直接上级的姓名，没有领导的员工也需要查询
```

### 回答

```sql
-- 1. 查询所有员工信息。查询员工编号，员工姓名，工资，职务名称，职务描述
SELECT
	emp.id, emp.ename, emp.salary, emp.job_id, job.jname, job.description 
FROM 
	emp, job 
WHERE 
	emp.job_id = job.id; 
```

```sql
-- 2. 查询员工编号，员工姓名，工资，职务名称，职务描述，部门名称，部门位置
SELECT 
	emp.id, emp.ename, emp.bonus, job.jname, job.description, dept.dname, dept.`location`
FROM
	emp, job, dept
WHERE
	emp.job_id = job.id AND emp.dept_id = dept.id;
```

<!--P528-->

```sql
-- 3. 查询员工姓名，工资，工资等级
SELECT
	emp.ename,
	emp.salary, 
	salarygrade.grade
FROM
	emp, 
	salarygrade
WHERE
	emp.salary BETWEEN salarygrade.losalary AND salarygrade.hisalary;
```

```sql
-- 4. 查询员工姓名，工资，职务名称，职务描述，部门名称，部门位置，工资等级
SELECT 
	emp.ename, 
	emp.salary, 
	job.jname, 
	job.description, 
	dept.dname, 
	dept.location, 
	salarygrade.grade
FROM
	emp, 
	job, 
	dept, 
	salarygrade
WHERE
	emp.job_id = job.id AND
	emp.dept_id = dept.id AND
	emp.salary BETWEEN salarygrade.losalary AND salarygrade.hisalary;
```

<!--P529 1.25-->

```sql
-- 5. 查询部门编号，部门名称，部门位置，部门人数
SELECT
	t1.id, 
	t1.dname, 
	t1.location, 
	t2.total
FROM 
	dept t1,
	-- 让这个表作为虚拟表 
	(
	SELECT
		dept_id, 
		COUNT(id) total
	FROM
		emp
	GROUP BY 
		dept_id	
	) t2
WHERE 
	t1.id = t2.dept_id;
```

```sql
-- 6. 查询所有员工的姓名及其直接上级的姓名，没有领导的员工也需要查询
-- 这个不完善
SELECT
	t1.ename, 
	t1.mgr, 
	t2.id, 
	t2.ename
FROM
	emp t1, 
	emp t2
WHERE
	t1.mgr = t2.id;
```

```sql
-- 这个完善了
SELECT
	t1.ename, 
	t1.mgr, 
	t2.id, 
	t2.ename
FROM
	emp t1
LEFT JOIN
	emp t2
ON
	t1.mgr = t2.id;
```

# 第五章 约束

<!--P505 集数反了-->

约束：对表中的数据进行限定，保证数据的正确性、有效性和完整性。

约束共分为：主键约束、非空约束、唯一约束和外键约束。

## 5.1 非空约束

<!--P506-->

非空约束：not null 值不能为空，NULL

共有两种方式来添加非空约束

1. 创建表的时候添加非空约束

   ```sql
   CREATE TABLE stu(
   	id INT,
   	NAME VARCHAR(20) not null		-- name为非空
   );
   ```

2. 创建表完成之后可以添加非空约束

   ```sql
   ALTER TABLE stu MODIFY NAME VARCHAR(20) NOT NULL;	-- 更改表，添加非空约束
   ```

   **同理，此方法也可以去除非空约束。**

## 5.2 唯一约束

<!--P507-->

唯一约束：unique 又叫做唯一索引index 就是值不能够重复

有两种方法来添加唯一约束

1. 创建表的时候，来添加唯一约束

   ```sql
   CREATE TABLE stu(
   	id INT,
   	NAME VARCHAR(20) UNIQUE
   );
   ```

   注意在MySQL中，唯一约束限定的列的值可以有多个NULL

2. 创建表完成之后可以添加唯一约束

   ```sql
   ALTER TABLE stu MODIFY phone_number VARCHAR(20) UNIQUE;	-- 更改表，添加唯一约束
   ```

* 删除唯一约束的话，与以往不同

  ```sql
  ALTER TABLE stu DROP INDEX phone_number;	-- 删除唯一约束
  ```

## 5.3 主键约束

<!--P508-->

主键约束：非空且唯一，也就是上面两个的综合

一张表只能有一个字段为主键，通常主键都是id字段

主键就是表中记录的唯一标识

同样，有两种方法添加主键约束

1. 创建表的时候，来添加主键约束

   ```sql
   CREATE TABLE stu(
   	id INT PRIMARY KEY,
   	NAME VARCHAR(20) UNIQUE
   );
   ```

2. 创建表完成之后，添加主键约束

   ```sql
   ALTER TABLE stu MODIFY id INT PRIMARY KEY;
   ```

* 由于唯一约束不同，所以删除主键约束，也不同

  ```sql
  ALTER TABLE stu DROP PRIMARY KEY;
  ```

* 自动增长

  <!--P509-->

  概念：如果某一列是数值类型的，那么可以使用auto_increment来完成值的自动增长

  1. 创建表的时候，添加主键约束，并且完成主键自动增长

     ```sql
     CREATE TABLE stu(
     	id INT PRIMARY KEY auto_increment,
     	NAME VARCHAR(20) UNIQUE
     );
     ```

  2. 创建表完成之后，添加自动增长

     ```sql
     ALTER TABLE stu MODIFY id INT auto_increment;
     ```

  * 删除自动增长

    ```sql
    ALTER TABLE stu MODIFY id INT;
    ```

## 5.4 外键约束

<!--P510-->

外键约束：`foreign key`, 就是让表与表之间产生关系，更改表的时候如果有错误会警告，从而保证数据的正确性。

有两种方式来添加外键

1. 创建表的时候，添加外键

   ```sql
   create table 表名(
   	...，
   	外键列，
   	constraint 外键名称 foreign key (外键列名称) references 主表名称(主表列名称)
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
   	dep_id int,		-- 外键对应主表的主键 外键列名称
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

   ![](D:\Java\笔记\图片\3-day02【MySQL约束】\1.png)

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

<!--P511 1.23-->

由于添加外键之后，多表之间是产生了联系了的，因此操作的时候，是操作不了的，但是我们想要操作的话，可以添加级联操作

级联操作共有两种：

1. 级联更新：`ON UPDATE CASCADE`

2. 级联删除：`ON DELETE CASCADE`

   级联删除，只是在删除表的时候，与之关联的都会删除，并不是删除级联操作。正因为此，级联删除不建议使用。

   级联更新可以同时使用，也可以分开使用。

```sql
ALTER TAABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY （外键字段名称） REFERENCES 主表名称 （主表列名称） ON UPDATE CASCADE ON DELETE CASCADE;
```

```sql
alter table employee add constraint emp_dept_fk foreign key (dep_id) references department(id) ON UPDATE CASCADE ON DELETE CASCADE;
```

# 第六章 数据库的设计

## 6.1 多表之间的关系

**分类**

<!--P512-->

1. 一对一(了解)	如：人和身份证

2. 一对多(多对一)   如：部门和员工

3. 多对多     如：学生和课程


**实现关系**

<!--P513-->

1. 一对多(多对一)	如：部门和员工

   实现方式：在多的一方建立外键，指向一的一方的主键。

   ![](D:\Java\笔记\图片\3-day02【MySQL约束】\2.png)

   <!--P514-->

2. 多对多 如：学生和课程

   实现方式：多对多关系实现需要借助第三张中间表。

   中间表至少包含两个字段，这两个字段作为第三张表的外键，分别指向两张表的主键。

   ![](D:\Java\笔记\图片\3-day02【MySQL约束】\3.png)

   <!--P515-->

3. 一对一(了解)    如：人和身份证

   实现方式：一对一关系实现，可以在任意一方添加**唯一外键**指向另一方的主键。

   通常一对一的关系，我们可以合成一张表来实现。

**案例介绍**

<!--P516s-->

旅游网

![](D:\Java\笔记\图片\3-day02【MySQL约束】\4.png)

```sql
-- 创建旅游线路分类表 tab_category
-- cid 旅游线路分类主键，自动增长
-- cname 旅游线路分类名称非空，唯一，字符串100
CREATE TABLE tab_category(
	cid INT PRIMARY KEY AUTO_INCREMENT,
	cname VARCHAR(100) NOT NULL UNIQUE
);

-- 添加旅游线路分类数据
INSERT INTO tab_category (cname) VALUES("周边游"), ("出境游"), ("国内游"), ("港澳游");

```

```sql
-- 创建旅游线路表 tab_route
/*
rid 旅游线路主键，自动增长
rname 旅游线路民称非空，唯一，字符串100 
price 价格
rdate 上架时间，日期类型
cid 外键，所属分类
*/
CREATE TABLE tab_route(
	rid INT PRIMARY KEY AUTO_INCREMENT,
	rname VARCHAR(100) NOT NULL UNIQUE,
	price DOUBLE,
	rdate DATE,
	cid INT,
	FOREIGN KEY (cid) REFERENCES tab_category (cid)		-- 创建外键，省略写法
);
-- 太麻烦了，就不添加数据了
-- constraint 外键名称 foreign key (外键列名称) references 主表名称(主表列名称)
```

```sql
-- 创建用户表 tab_user
/*
uid 用户主键，自增长
username 用户名长度100，唯一，非空
password 密码长度30 非空
name 真实姓名长度100
birthday 生日
sex 性别，定长字符串1
telephone 手机号，字符串11
email 邮箱，字符串长度100
*/
CREATE TABLE tab_user(
	uid INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(100) NOT NULL UNIQUE,
	PASSWORD VARCHAR(100) NOT NULL,
	NAME VARCHAR(100),	
	birthday DATE,
	sex CHAR(1) DEFAULT "男",		-- 定死字符长度为1，默认值为男
	telephone VARCHAR(11),
	email VARCHAR(100)	
);
-- 太麻烦了，就不添加数据了
```

```sql
-- 创建收藏表 tab_favorite
/*
rid 旅游线路id，外键
date 收藏时间
uid 用户id，外键
*/
CREATE TABLE tab_favorite(
	rid INT,
	DATE DATETIME,
	uid INT,
	PRIMARY KEY (rid, uid),		-- 创建复合主键，这样没有重复的值
	FOREIGN KEY (rid) REFERENCES tab_route(rid),
	FOREIGN KEY (uid) REFERENCES tab_user(uid)
);
```

![](D:\Java\笔记\图片\3-day02【MySQL约束】\5.png)

## 6.2 数据库设计的范式

<!--P517-->

* 设计关系数据库时，遵从不同的规范要求，设计出合理的关系型数据库，这些不同的规范要求被称为不同的范式，各种范式呈递次规范，越高的范式数据库冗余越小。
* 关系数据库有六种范式：`第一范式（1NF）`、`第二范式（2NF）`、`第三范式（3NF）`、`巴斯-科德范式（BCNF）`、`第四范式(4NF）`和`第五范式（5NF，又称完美范式）`。

<!--P518-->

* **函数依赖**：`A --> B`，如果通过A属性(属性组)的值，可以确定唯一B属性的值，那么称B依赖于A。

  例如：学号 --> 姓名	（学号，学号课程）--> 分数

* **完全函数依赖**：`A --> B`，如果A是一个属性组，则B属性值的确定需要依赖于A属性组中所有的属性值。

  例如：（学号，学号课程）--> 分数

* **部分函数依赖**：`A --> B`，如果A是一个属性组，则B属性值的确定只需要依赖于A属性组中某一些值。

  例如：（学号，学号课程）--> 姓名

* **传递函数依赖**：`A --> B`，`B --> C`，如果通过A属性(属性组)的值，可以确定唯一B属性的值，在通过B属性(属性组)的值可以确定唯一C属性的值，则称C传递函数依赖于A。

  例如：学号 --> 系名，系名 --> 系主任

* **码**：如果在一张表中，一个属性或者属性组，被其他所有的属性所完全依赖，则称这个属性(属性组)为该表的码。

  例如：该表中的码：（学号，课程名称）。

* **主属性**：码属性组中的所有属性。

* **非主属性**：除码属性组的属性。

关系数据库有六种范式：`第一范式（1NF）`、`第二范式（2NF）`、`第三范式（3NF）`、`巴斯-科德范式（BCNF）`、`第四范式(4NF）`和`第五范式（5NF，又称完美范式）`，下面介绍一下前三种范式：

1. `第一范式（1NF）`：数据库表的每一列都是不可分割的原子数据项 
2. `第二范式（2NF）`：在1NF的基础上，非码属性必须完全依赖于候选码（在1NF基础上消除非主属性对主码的部分函数依赖） 
3. `第三范式（3NF）`：在2NF基础上，任何非主属性不依赖于其它非主属性（在2NF基础上消除传递依赖）

### 详解

* 一范式，关系数据库已经帮我们控制好了。 

* 二范式，就是要有主键，其他属性都要依赖于这个主键。（部分函数依赖指的是存在组合关键字中的某些字段决定非关键字段的情况），也即所有非关键字段都完全依赖于任意一组候选关键字。 

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

* 三范式，就是不能有冗余，一张表，只能有主键，依赖主键的属性，外键，不能包含外键表的非主键属性。

  > 不过在生产环境，通常不会遵守的，肯定会有冗余，否则导出连接，会死人的，这个要看情况的，有些冗余是肯定需要的 

  假定学生关系表为`Student(学号, 姓名, 年龄, 所在学院, 学院地点, 学院电话)`，关键字为单一关键字"学号"，因为存在如下决定关系： `(学号) → (姓名, 年龄, 所在学院, 学院地点, 学院电话)` 

  这个数据库是符合2NF的，但是不符合3NF，因为存在如下决定关系：      `(学号) → (所在学院) → (学院地点, 学院电话)` 。即存在非关键字段"学院地点"、"学院电话"对关键字段"学号"的传递函数依赖。      

  它也会存在数据冗余、更新异常、插入异常和删除异常的情况。      

  把学生关系表分为如下两个表：`学生：(学号, 姓名, 年龄, 所在学院)`； `学院：(学院, 地点, 电话)`。

  这样的数据库表是符合第三范式的，消除了数据冗余、更新异常、插入异常和删除异常。 

# 第七章 事务

## 7.1 事务的基本介绍

<!--P530-->

### 概念

* 如果一个包含多个步骤的业务操作，被事务管理，那么这些操作要么同时成功，要么同时失败。

### 操作

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

### 默认自动提交

<!--P531-->

MySQL数据库中事务是默认自动提交的

事务提交的两种方式：

* 自动提交：
  * MySQL就是自动提交的
  * 一条DML（增删改）语句会自动提交一次事务。
* 手动提交：
  * Oracle数据库默认是手动提交事务
  * 需要先开启事务，然后再提交

修改事务的默认提交方式：

* 查看事务的默认提交方式：

  ```sql
  select @@autocommit;		-- 1代表自动提交   0代表手动提交
  ```

* 修改默认提交方式

  ```sql
  set @@autocommit = 0；
  ```

## 7.2 事务的四大特征

<!--P532-->

1. **原子性**：是不可分割的最小操作单位，要么同时成功，要么同时失败。
2. **持久性**：当事务提交或者回滚之后，数据库会持久化的保存数据。
3. **隔离性**：多个事务之间，相互独立。
4. **一致性**：事务操作前后，数据总量不变。

<font color = "red">**原子，持久，隔离，一致。**</font>

## 1.3 事务的隔离级别

<!--P533-->

### 概念

多个事务之间隔离的，相互独立的。但是如果多个事务操作同一批数据，则会引发一些问题，设置不同的隔离级别就可以解决这些问题。

**存在问题如下**

1. **脏读**：一个事务，读取到另一个事务中没有提交的数据。

   事务A读取了事务B更新的数据，然后B回滚操作，那么A读取到的数据是脏数据

2. **不可重复读（虚读）**：在同一个事务中，两次读取到的数据不一样。

   事务 A 多次读取同一数据，事务 B 在事务A多次读取的过程中，对数据作了更新并提交，导致事务A多次读取同一数据时，结果不一致。

3. **幻读**：一个事务操作（DML）数据表中所有记录，另一个事务添加了一条数据，则第一个事务查询不到自己的修改。

   系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这就叫幻读。

> <font color = "red">**小结：不可重复读的和幻读很容易混淆，不可重复读侧重于修改，幻读侧重于新增或删除。解决不可重复读的问题只需锁住满足条件的行，解决幻读需要锁表** </font>

**隔离级别一共有四种**

1. `read uncommitted`：读未提交

   产生的问题：脏读、不可重复读、幻读。

2. `read committed`：读已提交(Oracle默认隔离级别)

   产生的问题：不可重复读、幻读。

3. `repeatable read`：可重复读(MySQL默认级别)

   产生的问题：幻读。

4. `serializable`：串行化

   可以解决所有的问题

> 注意：隔离级别从小到大安全性越来越高，但是效率越来越低。
>
> 1. 事务隔离级别为读提交时，写数据只会锁住相应的行
> 2. 事务隔离级别为可重复读时，如果检索条件有索引（包括主键索引）的时候，默认加锁方式是next-key 锁；如果检索条件没有索引，更新数据时会锁住整张表。一个间隙被事务加了锁，其他事务是不能在这个间隙插入记录的，这样可以防止幻读。
> 3. 事务隔离级别为串行化时，读写数据都会锁住整张表
> 4. 隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大。

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

读未提交`read uncommitted`：读未提交：

> 张三给李四转账500元，由于这是事务中，所以是临时的，可以被回滚或者提交。
>
> 可是我们在右边的事务中查看账户表却发现表里面已经被修改了，如果这时李四用右边的MySQL查看发现账户已经修改了，那么相信了张三已经转账了，就会打欠条了。
>
> 可是我们直接回滚，这时会发现数据都会归为原来。但是受伤的依旧是李四。所以这不被允许。
>
> 这个例子中的产生的问题是：脏读、不可重复读、幻读(无法演示)。
>
> **脏读：右边事务查询到了左边事务未提交的数据。**
>
> **不可重复读：右边事务两次查询到的表数据不一样。**
>
> 不可重复读的合理性：向领导汇报表格，前一秒是一个数值，但是公司的利润一直在变化，所以下一秒就会更改，所以需要变成不可重复读。

1. 打开一个客户端A，并设置当前事务模式为`read uncommitted`：读未提交，查询表account的初始值：

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\1.png)

2. 在客户端A的事务提交之前，打开另一个客户端B，更新表account：

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\2.png)

3. 这时，虽然客户端B的事务还没提交，但是客户端A就可以查询到B已经更新的数据：

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\3.png)

4. 一旦客户端B的事务因为某种原因回滚，所有的操作都将会被撤销，那客户端A查询到的数据其实就是脏数据：

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\4.png)

5. 在客户端A执行更新语句`update account set balance = balance - 50 where id =1`，lilei的`balance`没有变成350，居然是400，是不是很奇怪，数据不一致啊，如果你这么想就太天真 了，在应用程序中，我们会用400-50=350，并不知道其他会话回滚了，要想解决这个问题可以采用读已提交的隔离级别

   ![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\5.png)

### read committed

读已提交。设置隔离级别是`read committed`读已提交，会把脏读给消灭掉，但是依然会留着不可重复读。

1. 打开一个客户端A，并设置当前事务模式为`read committed（读已提交）`，查询表`account`的所有记录：

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\6.png)

2. 在客户端A的事务提交之前，打开另一个客户端B，更新表`account`：

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\7.png)

3. 这时，客户端B的事务还没提交，客户端A不能查询到B已经更新的数据，解决了脏读问题：

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\8.png)

4. 客户端B的事务提交

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\9.png)

5. 客户端A执行与上一步相同的查询，结果 与上一步不一致，即产生了不可重复读的问题

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\10.png)

### repeatable read

可重复读。设置隔离级别是`repeatable read`可重复读，那么会把不可重复读消灭掉，想要在右表读取到数据更改，那么只能手动结束右表的事务，`committed`或者`rollback`。

1. 打开一个客户端A，并设置当前事务模式为`repeatable read`，查询表`account`的所有记录

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\11.png)

2. 在客户端A的事务提交之前，打开另一个客户端B，更新表`account`并提交

   客户端B的事务居然可以修改客户端A事务查询到的行，也就是mysql的可重复读不会锁住事务查询到的行，这一点出乎我的意料，sql标准中事务隔离级别为可重复读时，读写操作要锁行的，mysql居然没有锁，我了个去。在应用程序中要注意给行加锁，不然你会以步骤（1）中lilei的balance为400作为中间值去做其他操作

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\12.png)

3. 在客户端A查询表`account`的所有记录，与步骤（1）查询结果一致，没有出现不可重复读的问题

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\13.png)

4. 在客户端A，接着执行`update balance = balance - 50 where id = 1`，`balance`没有变成`400-50=350`，lilei的balance值用的是步骤（2）中的350来算的，所以是300，数据的一致性倒是没有被破坏。

   可重复读的隔离级别下使用了MVCC机制，select操作不会更新版本号，是快照读（历史版本）；insert、update和delete会更新版本号，是当前读（当前版本）。

   ![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\14.png)

5. 重新打开客户端B，插入一条新数据后提交

![](D:\Java\笔记\图片\3-day03【MySQL多表、事务】\15.png)

6. 在客户端A计算balance之和，值为300+16000+2400=18700，没有把客户端B的值算进去，客户端A提交后再计算balance之和，居然变成了19300，这是因为把客户端B的600算进去了。

   站在客户的角度，客户是看不到客户端B的，它会觉得是天下掉馅饼了，多了600块，这就是幻读，站在开发者的角度，数据的一致性并没有破坏。

   但是在应用程序中，我们的代码可能会把18700提交给用户了，如果你一定要避免这情况小概率状况的发生，那么就要采取下面要介绍的事务隔离级别“串行化”

   ```ABAP
   mysql> select sum(balance) from account;
   +--------------+
   | sum(balance) |
   +--------------+
   | 18700 |
   +--------------+
   1 row in set (0.00 sec)
    
   mysql> commit;
   Query OK, 0 rows affected (0.00 sec)
    
   mysql> select sum(balance) from account;
   +--------------+
   | sum(balance) |
   +--------------+
   | 19300 |
   +--------------+
   1 row in set (0.00 sec)
   ```

### serializable　

串行化，设置隔离级别是`serializable`串行化，类似于加一个锁，就是在该事务中，其他事务影响不了。只要设置了该隔离级别，那么其他的事务甚至都不能查询该事务中修改的表。右表不能够查询account表。只有等左边的事务回滚或者提交结束事务，右边的事务才能够显示出来account表格，否则一直在等待。

1. 打开一个客户端A，并设置当前事务模式为serializable，查询表account的初始值：

   ```ABAP
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

   ```ABAP
   mysql> set session transaction isolation level serializable;
   Query OK, 0 rows affected (0.00 sec)
   
   mysql> start transaction;
   Query OK, 0 rows affected (0.00 sec)
   
   mysql> insert into account values(5,'tom',0);
   ERROR 1205 (HY000): Lock wait timeout exceeded; try restarting transaction
   ```

# 第六章 数据库的备份和还原

<!--P519-->

## 6.1 备份

命令行备份数据库：`mysqldump -u用户名 -p密码 数据库名称 > 保存的路径`。

## 6.2 还原

命令行还原数据库

1. 登陆数据库：`mysql -u用户名  -p密码`。
2. 创建数据库：`create database 数据库名称`。
3. 使用数据库：`use 数据库名称`。
4. 执行sql语句：`source 文件路径`。

