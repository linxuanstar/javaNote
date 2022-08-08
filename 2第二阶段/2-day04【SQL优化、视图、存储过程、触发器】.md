![](D:\Java\笔记\图片\3-day01 【MySQL基础】\12-8.png)

Explain 执行计划中各个字段的含义：

- `id`：select 查询的序列号，表示查询中执行 select 子句或者操作表的顺序（id相同，执行顺序从上到下；id不同，值越大越先执行）
- `select_type`：表示 SELECT 的类型，常见取值有 SIMPLE（简单表，即不适用表连接或者子查询）、PRIMARY（主查询，即外层的查询）、UNION（UNION中的第二个或者后面的查询语句）、SUBQUERY（SELECT/WHERE之后包含了子查询）等
- `type`：表示连接类型，性能由好到差的连接类型为 NULL、system、const、eq_ref、ref、range、index、all，尽量将性能提升到const就可以了。
- `possible_key`：可能应用在这张表上的索引，一个或多个
- `Key`：实际使用的索引，如果为 NULL，则没有使用索引
- `Key_len`：表示索引中使用的字节数，该值为索引字段最大可能长度，并非实际使用长度，在不损失精确性的前提下，长度越短越好
- `rows`：MySQL认为必须要执行的行数，在InnoDB引擎的表中，是一个估计值，可能并不总是准确的
- `filtered`：表示返回结果的行数占需读取行数的百分比，filtered**的值越大越好**

# 第四章 SQL优化

## 4.1 插入数据

```sql
insert into tb_test values(1,'tom');
insert into tb_test values(2,'cat');
insert into tb_test values(3,'jerry');
.....
```

如果我们需要一次性往数据库表中插入多条记录，可以从以下三个方面进行优化。

1. 批量插入数据

   ```sql
   -- 这种方式建议插入1000条数据之内，如果超过了可以使用多条insert语句
   Insert into tb_test values(1,'Tom'),(2,'Cat'),(3,'Jerry');
   ```

2. 手动控制事务

   ```sql
   start transaction;
   insert into tb_test values(1,'Tom'),(2,'Cat'),(3,'Jerry');
   insert into tb_test values(4,'Tom'),(5,'Cat'),(6,'Jerry');
   insert into tb_test values(7,'Tom'),(8,'Cat'),(9,'Jerry');
   commit;
   ```

3. 主键顺序插入，性能要高于乱序插入。

   ```sql
   主键乱序插入 : 8 1 9 21 88 2 4 15 89 5 7 3
   主键顺序插入 : 1 2 3 4 5 7 8 9 15 21 88 89
   ```

如果一次性需要插入大批量数据(比如: 几百万的记录)，使用insert语句插入性能较低，此时可以使用MySQL数据库提供的load指令进行插入。操作如下：

```sql
-- 客户端连接服务端时，加上参数 -–local-infile
mysql –-local-infile -u root -p

-- 查看从本地加载文件导入数据的开关是否打开，0为关闭，1为打开
select @@local_infile;

-- 设置全局参数local_infile为1，开启从本地加载文件导入数据的开关
set global local_infile = 1;

-- 执行load指令将准备好的数据，加载到tb_user表结构中
-- 第一个‘,’的意思是每行数据每个字段之间的分隔符，‘\n’的意思是每行数据的分隔符
load data local infile '/usr/local/mysql/sql/load_user_100w_sort.sql' into table tb_user fields terminated by ',' lines terminated by '\n' ;
```

```sh
wc -l load_user_100w_sort.sql 		# 查看该文件有多少行
head load_user_100w_sort.sql 		# 查看该文件前十行
```

> 在load时，主键顺序插入性能高于乱序插入

## 4.2 主键优化

之前我们提到，主键顺序插入的性能是要高于乱序插入的。 这一小节，就来介绍一下具体的原因，然后再分析一下主键又该如何设计。

在InnoDB存储引擎中，表数据都是根据主键顺序组织存放的，这种存储方式的表称为索引组织表 (index organized table IOT)。

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\12-5.png)

行数据，都是存储在聚集索引的叶子节点上的。而我们之前也讲解过InnoDB的逻辑结构图：

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\11.png)

在InnoDB引擎中，数据行是记录在逻辑结构 page 页中的，而每一个页的大小是固定的，默认16K。那也就意味着， 一个页中所存储的行也是有限的，如果插入的数据行row在该页存储比较大，那么将会存储到下一个页中，页与页之间会通过指针连接。

### 4.2.1 页分裂

页可以为空，也可以填充一半，也可以填充100%。每个页包含了2~N行数据(如果一行数据过大，会行溢出)，根据主键排列。

如果是主键顺序插入，那么不会有任何问题。从磁盘中申请页，主键顺序插入，当第一页写满之后，再写入第二个页，页与页之间会通过指针连接，之后再写入第三页。

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\13.png)

但是主键乱序插入效果就不一样了。

假如1#,2#页都已经写满了，存放了如图所示的数据

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\13-1.png)

此时再插入id为50的记录，按照顺序，应该存储在47之后。但是47所在的1#页，已经写满了，存储不了50对应的数据了。 那么此时会开辟一个新的页 3#。

但是并不会直接将50存入3#页，而是会将1#页后一半的数据，移动到3#页，然后在3#页，插入50。

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\13-2.png)

那么此时，这三个页之间的数据顺序是有问题的。 1#的下一个页应该是3#， 3#的下一个页是2#。 所以，此时需要重新设置链表指针。

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\13-4.png)

上述的这种现象，称之为 "页分裂"，是比较耗费性能的操作。

### 4.2.2 页合并

目前表中已有数据的索引结构(叶子节点)如下：

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\13-5.png)

当我们对已有数据进行删除时，实际上记录并没有被物理删除，只是记录被标记（flaged）为删除并且它的空间 变得允许被其他记录声明使用。

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\13-6.png)

当页中删除的记录达到 MERGE_THRESHOLD（默认为页的50%），InnoDB会开始寻找最靠近的页（前 或后）看看是否可以将两个页合并以优化空间使用。

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\13-7.png)

删除数据，并将页合并之后，再次插入新的数据21，则直接插入3#页。

这个里面所发生的合并页的这个现象，就称之为 "页合并"。

> MERGE_THRESHOLD：合并页的阈值，可以自己设置，在创建表或者创建索引时指定。

### 4.2.3 主键设计原则

* 满足业务需求的情况下，尽量降低主键的长度。 

  在InnoDB引擎中，索引分为聚集索引和二级索引。聚集索引只有一个，而二级索引有多个，二级索引的叶子节点中存储的就是主键。如果主键长度比较长，加上二级索引比较多，那么会占用大量的磁盘空间。搜索的时候也会耗费大量的磁盘IO。

* 插入数据时，尽量选择顺序插入，选择使用AUTO_INCREMENT自增主键。

* 尽量不要使用UUID做主键或者是其他自然主键，如身份证号。 

* 业务操作时，避免对主键的修改。

## 4.3 order by优化

MySQL的排序，有两种方式：

- Using filesort：通过表的索引或全表扫描，读取满足条件的数据行，然后在排序缓冲区sort buffer中完成排序操作，所有不是通过索引直接返回排序结果的排序都叫 FileSort 排序。 
- Using index：通过有序索引顺序扫描直接返回有序数据，这种情况即为 using index，不需要 额外排序，操作效率高。 

对于以上的两种排序方式，Using index的性能高，而Using filesort的性能低，我们在优化排序操作时，尽量要优化为 Using index。

目前表的索引为：

```sql
+---------+----------------------+--------------+-------------+
| Table   | Key_name             | Seq_in_index | Column_name |
+---------+----------------------+--------------+-------------+
| tb_user | PRIMARY              |            1 | id          |
| tb_user | idx_user_pro_age_sta |            1 | profession  |
| tb_user | idx_user_pro_age_sta |            2 | age         |
| tb_user | idx_user_pro_age_sta |            3 | status      |
| tb_user | idx_user_email_5     |            1 | email       |
+---------+----------------------+--------------+-------------+
```



接着来测试一下orderby的排序：

```sql
-- 查看id,age,phone字段，按照age升序排序
explain select id,age,phone from tb_user order by age ;
-- 查看id,age,phone字段，按照age, phone升序排序
explain select id,age,phone from tb_user order by age, phone;   

+----+-------------+---------+------+---------------+------+----------------+
| id | select_type | table   | type | possible_keys | key  | Extra          |
+----+-------------+---------+------+---------------+------+----------------+
|  1 | SIMPLE      | tb_user | ALL  | NULL          | NULL | Using filesort |	-- Using filesort
+----+-------------+---------+------+---------------+------+----------------+	-- 效率比较低
```

由于 age, phone 都没有索引，所以此时再排序时，出现Using filesort， 排序性能较低。



那么我们尝试为他们添加索引，然后再排序：

```sql
create index idx_user_age_phone_aa on tb_user(age,phone);	-- 创建索引 顺序是age,phone
explain select id,age,phone from tb_user order by age;		-- 排序
explain select id,age,phone from tb_user order by age , phone;	-- 排序

+-------------+
| Extra       |		-- 建立索引之后，再次进行排序查询，就由原来的Using filesort，变为了 Using index
+-------------+		-- 性能就是比较高的了。
| Using index |
+-------------+
```



之前创建的索引都是为他们进行升序排序，如果查询之后再降序排序会发生什么呢？

```sql
explain select id,age,phone from tb_user order by age desc , phone desc ;
```

答案就是也出现 `Using index`， 但是此时Extra中会出现 `Backward index scan`，这个代表反向扫描索引，因为在MySQL中我们创建的索引，默认索引的叶子节点是从小到大排序的，而此时我们查询排序时，是从大到小。所以，在扫描时，就是反向扫描，就会出现 `Backward index scan`。 



```sql
-- 根据phone，age进行升序排序，phone在前，age在后。
explain select id,age,phone from tb_user order by phone , age;

-- 结果就是Using filesort
```

排序时，也需要满足最左前缀法则，否则也会出现 `filesort`。因为在创建索引的时候， age是第一个字段，phone是第二个字段，所以排序时，也就该按照这个顺序来，否则就会出现 `Using filesort`。



```sql
-- age, phone进行降序一个升序，一个降序
explain select id,age,phone from tb_user order by age asc , phone desc ;

-- 因为创建索引时，如果未指定顺序，默认都是按照升序排序的，而查询时，一个升序，一个降序，此时就会出现Using filesort。
```

为了解决上述的问题，我们可以创建一个索引，这个联合索引中 age 升序排序，phone 倒序排序。`create index idx_user_age_phone_ad on tb_user(age asc ,phone desc);`。这样结果就是Using index了。



由上述的测试,我们得出order by优化原则: 

- A. 根据排序字段建立合适的索引，多字段排序时，也遵循最左前缀法则。
-  B. 尽量使用覆盖索引。
-  C. 多字段排序, 一个升序一个降序，此时需要注意联合索引在创建时的规则（ASC/DESC）。 
- D. 如果不可避免的出现filesort，大数据量排序时，可以适当增大排序缓冲区大小 sort_buffer_size(默认256k)。

## 4.4 group by优化

分组操作，我们主要来看看索引对于分组操作的影响。首先将tb_user表内索引除了主键索引外全部删除。

接下来，在没有索引的情况下，执行如下SQL，查询执行计划：

```sql
explain select profession , count(*) from tb_user group by profession ;

-- type: ALL   Extra: Using temporary    性能较差   使用到了中间表 
```

然后，我们在针对于 profession ， age， status 创建一个联合索引，再执行刚刚的SQL：

```sql
create index idx_user_pro_age_sta on tb_user(profession , age , status); -- 创建索引
explain select profession , count(*) from tb_user group by profession ;  -- 查看分组

-- type: index  Extra: Using index     性能比刚刚提升了  使用到了索引
```

如果仅仅根据age分组，就会出现 Using temporary ；而如果是根据 profession,age两个字段同时分组，则不会出现 Using temporary。原因是因为对于分组操作， 在联合索引中，也是符合最左前缀法则的。

所以，在分组操作中，我们需要通过以下两点进行优化，以提升性能：

- A. 在分组操作时，可以通过索引来提高效率。 
- B. 分组操作时，索引的使用也是满足最左前缀法则的。

## 4.5 limit优化

在数据量比较大时，如果进行limit分页查询，在查询时，越往后，分页查询效率越低。

因为，当在进行分页查询时，如果执行 limit 2000000,10 ，此时需要MySQL排序前2000010 记录，仅仅返回 2000000 - 2000010 的记录，其他记录丢弃，查询排序的代价非常大 。

优化思路: 一般分页查询时，通过创建覆盖索引能够比较好地提高性能，可以通过覆盖索引加子查询形式进行优化。

```sql
-- select id from tb_sku order by id limit 2000000,10 只查询出来主键，然后根据主键查询
explain select * from tb_sku t , (select id from tb_sku order by id limit 2000000,10) a where t.id = a.id;
```

## 4.6 count优化

对于下面这条语句进行查询，如果数据少还没事，但是数据多了，那么便是灾难。

```sql
select count(*) from tb_user ;
```

这种情况在InnoDB中出现正常：

- MyISAM 引擎把一个表的总行数存在了磁盘上，因此执行 `count(*)` 的时候会直接返回这个数，效率很高； 但是如果是带条件的count，MyISAM也慢。 
- InnoDB 引擎就麻烦了，它执行 `count(*)` 的时候，需要把数据一行一行地从引擎里面读出来，然后累积计数。

如果说要大幅度提升InnoDB表的count效率，主要的优化思路：自己计数(可以借助于redis这样的数据库进行,但是如果是带条件的count又比较麻烦了)。

`count()` 是一个聚合函数，对于返回的结果集，一行行地判断，如果 count 函数的参数不是NULL，累计值就加 1，否则不加，最后返回累计值。 

用法：`count（*）`、`count（主键）`、`count（字段）`、`count（数字）`

- `count（主键）`：InnoDB 引擎会遍历整张表，把每一行的主键id值都取出来，返回给服务层。服务层拿到主键后，直接按行进行累加(主键不可能为null)
- `count（字段）`：没有not null 约束 : InnoDB 引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，服务层判断是否为null，不为null，计数累加。 有not null 约束：InnoDB 引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，直接按行进行累加。
- `count（数字）`：InnoDB 引擎遍历整张表，但不取值。服务层对于返回的每一行，放一个数字“1” 进去，直接按行进行累加。
- `count（*）`：InnoDB引擎并不会把全部字段取出来，而是专门做了优化 不取值，服务层直接按行进行累加。

> 按照效率排序的话，`count(字段)` < `count(主键 id)` < `count(1)` ≈ `count(*)`，所以尽 量使用 `count(*)`。

## 4.7 update优化

注意一下update语句执行时的注意事项：

```sql
update course set name = 'javaEE' where id = 1 ;
-- 当我们在执行删除的SQL语句时，会锁定id为1这一行的数据，然后事务提交之后，行锁释放。
```

但是当我们在执行如下SQL时：

```sql
update course set name = 'SpringBoot' where name = 'PHP' ;
-- 当我们开启多个事务，在执行上述的SQL时，我们发现行锁升级为了表锁。 导致该update语句的性能大大降低。
```

InnoDB的行锁是针对索引加的锁，不是针对记录加的锁 ,并且该索引不能失效，否则会从行锁升级为表锁 。

# 第五章 视图

视图（View）是一种虚拟存在的表。视图中的数据并不在数据库中实际存在，行和列数据来自定义视图的查询中使用的表，并且是在使用视图时动态生成的。

通俗的讲，视图只保存了查询的SQL逻辑，不保存查询结果。所以我们在创建视图的时候，主要的工作就落在创建这条SQL查询语句上。

## 5.1 语法

```sql
-- 创建视图
CREATE [OR REPLACE] VIEW 视图名称[(列名列表)] AS SELECT语句 [ WITH [CASCADED | LOCAL ] CHECK OPTION ]

create or replace view stu_v_1 as select id,name from student where id <= 20 ;
```

```sql
-- 查询视图
SHOW CREATE VIEW 视图名称;
SELECT * FROM 视图名称 ...... ;
```

```sql
-- 修改视图
CREATE [OR REPLACE] VIEW 视图名称[(列名列表)] AS SELECT语句 [ WITH [ CASCADED | LOCAL ] CHECK OPTION ]
ALTER VIEW 视图名称[(列名列表)] AS SELECT语句 [ WITH [ CASCADED | LOCAL ] CHECK OPTION ]
```

```sql
-- 删除视图
DROP VIEW [IF EXISTS] 视图名称 [,视图名称] ...
```

接下来，做一个测试，演示一下视图的插入和更新：

```sql
-- 创建视图
create or replace view stu_v_1 as select id,name from student where id <= 20 ;

select * from stu_v_1;
insert into stu_v_1 values(6,'Tom');		-- 可以插入 也可以查出来
insert into stu_v_1 values(27,'Tom22');		-- 可以插入 但是无法查出来
```

执行上述的SQL，我们会发现，id为6和27的数据都是可以成功插入的。 但是我们执行查询，查询出来的数据，却没有id为27的记录。

因为我们在创建视图的时候，指定的条件为 `id<=20,` id为27的数据，是不符合条件的，所以没有查询出来，但是这条数据确实是已经成功的插入到了基表中。 

如果我们想要做到必须满足条件才能操作视图的话，那么就需要借助于视图的检查选项了。

## 5.2 检查选项

当使用`WITH CHECK OPTION`子句创建视图时，MySQL会通过视图检查正在更改的每个行，例如插入，更新，删除，以使其符合视图的定义。 

```sql
-- 创建视图的时候 有检查选项 只有满足id<=20才能够插入
create or replace view stu_v_1 as select id,name from stu where id <= 20 WITH CHECK OPTION ;
```

MySQL允许基于另一个视图创建视图，它还会检查依赖视图中的规则以保持一致性。为了确定检查的范围，mysql提供了两个选项： `CASCADED` 和 `LOCAL` ，默认值为 `CASCADED` 。

### 5.2.1 CASCADED

CASCADED：级联，会向下检查

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\14.png)

接下来我们来尝试一下：

```sql
-- 创建视图stu_v_1
create or replace view stu_v_1 as select id,name from student where id <= 20 ;
insert into stu_v_1 values(6,'Tom');		-- 可以插入 插入到了基表 也可以在视图查出来
insert into stu_v_1 values(27,'Tom22');		-- 可以插入 插入到了基表 但是无法在视图查出来

-- 根据stu_v_1创建视图stu_v_2
create VIEW stu_v_2 AS  select id, name from stu_v_1 WHERE id >= 10 WITH CASCADED CHECK OPTION ;
INSERT INTO stu_v_2 VALUES (7, 'Tom');		-- 无法插入 不满足视图stu_v_2条件
INSERT INTO stu_v_2 VALUES (27, 'Tom');		-- 无法插入 不满足视图stu_v_1条件

-- 基于视图stu_v_2创建stu_v_3
create VIEW stu_v_3 AS  select id, name from stu_v_2 WHERE id <= 15;
INSERT INTO stu_v_3 VALUES (12, 'Tom');		-- 可以插入 检查stu_v_2和stu_v_1成立 插入
INSERT INTO stu_v_3 VALUES (18, 'Tom');		-- 可以插入 stu_v_3没有创建检查视图选项 所以不会检查 会检查stu_v_2和stu_v_1
```

### 5.2.2 LOCAL

LOCAL：本地。

比如，v2视图是基于v1视图的，如果在v2视图创建的时候指定了检查选项为 local ，但是v1视图创建时未指定检查选项。 则在执行检查时，知会检查v2，不会检查v2的关联视图v1

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\14-1.png)

```sql
-- 创建视图stu_v_4
create or REPLACE view stu_v_4 as SELECT id, name from stu where id <= 15;
insert into stu_v_4 VALUES (5, 'Tom');			-- 可以插入 插入到了基表 也可以在视图查出来
insert into stu_v_4 VALUES (16, 'Tom');			-- 可以插入 插入到了基表 但是无法在视图查出来

-- 基于视图stu_v_4创建视图stu_v_5
create view stu_v_5 as SELECT id,name from stu_v_4 where id >= 10 WITH LOCAL CHECK OPTION ;
INSERT INTO stu_v_5 VALUES (13, 'Tom'); 	-- 可以插入 满足当前视图检查 递归检查stu_v_4 发现没有检查选项
INSERT INTO stu_v_5 VALUES (17, 'Tom');		-- 可以插入 满足当前视图检查 递归检查stu_v_4 发现没有检查选项

-- 基于视图stu_v_5创建视图stu_v_6
create or replace VIEW stu_v_6 as select id, name from stu_v_5 where id < 20;
INSERT INTO stu_v_6 VALUES (14, 'Tom');		-- 可以插入
```

## 5.3 视图更新

要使视图可更新，**视图中的行与基础表中的行之间必须存在一对一的关系**。如果视图包含以下任何一项，则该视图不可更新：

- A. 聚合函数或窗口函数（SUM()、 MIN()、 MAX()、 COUNT()等） 
- B. DISTINCT 
- C. GROUP BY 
- D. HAVING 
- E. UNION 或者 UNION ALL

## 5.4 视图作用

1）简单 

视图不仅可以简化用户对数据的理解，也可以简化他们的操作。那些被经常使用的查询可以被定义为视图，从而使得用户不必为以后的操作每次指定全部的条件。

2）安全 

数据库可以授权，但不能授权到数据库特定行和特定的列上。通过视图用户只能查询和修改他们所能见到的数据

3）数据独立 

视图可帮助用户屏蔽真实表结构变化带来的影响。

# 第六章 存储过程

存储过程是事先经过编译并存储在数据库中的一段 SQL 语句的集合，调用存储过程可以简化应用开发人员的很多工作，减少数据在数据库和应用服务器之间的传输，对于提高数据处理的效率是有好处的。

存储过程思想上很简单，就是数据库 SQL 语言层面的代码封装与重用。

![](D:\Java\笔记\图片\3-day01 【MySQL基础】\15.png)

特点如下：

- 封装，复用 -----------------------> 可以把某一业务SQL封装在存储过程中，需要用到的时候直接调用即可。 
- 可以接收参数，也可以返回数据 --------> 再存储过程中，可以传递参数，也可以接收返回值。 
- 减少网络交互，效率提升 -------------> 如果涉及到多条SQL，每执行一次都是一次网络传输。 而如果封装在存储过程中，我们只需要网络交互一次可能就可以了。

## 6.1 基本语法

```sql
CREATE PROCEDURE 存储过程名称 ([ 参数列表 ]) -- 创建存储过程
BEGIN
	SQL语句
END ;

create PROCEDURE p1()   -- 示例
BEGIN
    select * from student;
END;
```

```sql
CALL 名称 ([ 参数 ]);	-- 调用 格式
```

```sql
-- 查询指定数据库的存储过程及状态信息
SELECT * FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_SCHEMA = '数据库名称'; 
SHOW CREATE PROCEDURE 存储过程名称 ; -- 查询某个存储过程的定义
```

```sql
DROP PROCEDURE [ IF EXISTS ] 存储过程名称;		-- 删除存储过程
```

在命令行中，执行创建存储过程的SQL时，需要通过关键字 delimiter 指定SQL语句的结束符。

存储过程中有着SQL语句，所以不可避免的存在`;`。这个时候就会判定存储过程的SQL语句完毕，所以我们需要重新执行一下SQL语句的结束符。

```sql
delimiter $$			-- 这样就代表修改了结束符了，修改为了$$
```

## 6.2 变量

在MySQL中变量分为三种类型: 系统变量、用户定义变量、局部变量。

### 6.2.1 系统变量

系统变量是MySQL服务器提供，不是用户定义的，属于服务器层面。分为全局变量（GLOBAL）、会话变量（SESSION）。

- A. 全局变量(GLOBAL): 全局变量针对于所有的会话。 
- B. 会话变量(SESSION): 会话变量针对于单个会话，在另外一个会话窗口就不生效了。

如果没有指定SESSION/GLOBAL，默认是SESSION，会话变量。

```sql
-- 查看系统变量
SHOW [ SESSION | GLOBAL ] VARIABLES ; -- 查看所有系统变量
SHOW [ SESSION | GLOBAL ] VARIABLES LIKE '......'; -- 可以通过LIKE模糊匹配方式查找变量
SELECT @@[SESSION | GLOBAL] 系统变量名; -- 查看指定变量的值
```

```sql
-- 设置系统变量
SET [ SESSION | GLOBAL ] 系统变量名 = 值 ;
SET @@[SESSION | GLOBAL]系统变量名 = 值 ;
```

mysql服务重新启动之后，所设置的全局参数会失效，要想不失效，可以在 `/etc/my.cnf` 中配置。

### 6.2.2 用户定义变量

### 6.2.3 局部变量

## 6.3 if判断

## 6.4 参数

## 6.5 case

## 6.6 循环

### 6.6.1 while

### 6.6.2 repeat

### 6.6.3 loop

## 6.7 游标

## 6.8 条件处理程序

## 6.9 存储函数

# 第七章 触发器

