# 第一章 MyBatisPlus基础

MyBatisPlus（简称MP）是基于MyBatis框架基础上开发的增强型工具，旨在简化开发、提高效率。MyBatisPlus的官网为：https://baomidou.com/。

MP旨在成为MyBatis的最好搭档，而不是替换MyBatis，所以可以理解为MP是MyBatis的一套增强工具，它是在MyBatis的基础上进行开发的，我们虽然使用MP但是底层依然是MyBatis的东西，也就是说我们也可以在MP中写MyBatis的内容。

MP的特性：

- 无侵入：只做增强不做改变，不会对现有工程产生影响
- 强大的 CRUD 操作：内置通用 Mapper，少量配置即可实现单表CRUD 操作
- 支持 Lambda：编写查询条件无需担心字段写错
- 支持主键自动生成
- 内置分页插件...

## 1.1 常用注解

| 名称 | @Mapper                                                      |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在dao层接口上面定义                                  |
| 作用 | 指定扫描Mapper接口，MP会扫描该接口并创建它的代理对象交给SpringIOC容器管理 |

| 名称 | @MapperScan                                                  |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，在启动类上面定义。例如：`@MapperScan("com.linxuan.dao")` |
| 作用 | 扫描指定包下面的所有Dao接口，这样比@Mapper简便，只需要在启动类上面添加一次即可 |

| 名称     | @TableField                                                  |
| -------- | ------------------------------------------------------------ |
| 类型     | 属性注解，实体类属性上方定义。                               |
| 作用     | 设置当前属性对应的数据库表中的字段关系                       |
| 相关属性 | value(默认)：设置表与该属性对应的字段名称<br />exist：设置属性在数据库表字段中是否存在，默认为true，此属性不能与value合并使用<br />select：设置属性是否参与查询，此属性与select()映射配置不冲突<br />fill：字段填充标记，值FieldFill枚举类为字段填充策略。 |

| 名称 | @TableName                                          |
| ---- | --------------------------------------------------- |
| 类型 | 类注解，实体类上方定义。例如`@TableName("tb_user")` |
| 作用 | 设置当前类对应于数据库表关系                        |
| 属性 | value(默认)：设置数据库表名称                       |

| 名称 | @TableId                                                     |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解，在实体类表示主键的属性上方定义                     |
| 作用 | 设置当前类中主键属性的生成策略                               |
| 属性 | value(默认)：设置数据库表主键名称。type：设置主键属性的生成策略，值查照IdType的枚举值 |

| 名称 | @TableLogic                                                  |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解，在实体类用于表示删除字段的属性上方定义。例如`@TableLogic(value="0", delval="1")` |
| 作用 | 标识该字段为进行逻辑删除的字段                               |
| 属性 | value：逻辑未删除值。delval：逻辑删除值。                    |

## 1.2 整合MyBatisPlus

使用方式如下：

```sql
# 数据库操作
# 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
# 使用linxuan数据库
USE linxuan;
-- 关闭外键约束
SET FOREIGN_KEY_CHECKS = 0;

# 如果这个表存在，那么就删除
Drop TABLE IF EXISTS tb_user;
# 如果不存在tb_user表那么就创建
CREATE TABLE IF NOT EXISTS tb_user (
    id bigint(20) primary key auto_increment,
    name varchar(32) not null,
    password  varchar(32) not null,
    age int(3) not null ,
    tel varchar(32) not null
);
# 删除当前表，并创建一个新表 字段相同。用来清除当前表的所有数据
TRUNCATE TABLE tb_user;
# 插入数据
insert into tb_user values(1,'Tom','tom',3,'18866668888'), 
                          (2,'Jerry','jerry',4,'16688886666'), 
                          (3,'linxuan','linxuan666',21,'12345678910');

# 外键约束置为1
SET FOREIGN_KEY_CHECKS = 1;
```

```xml
<dependencies>
    <!-- SpringBoot整合MyBatis环境 -->
    <!-- mysql驱动，创建项目的时候搜索mysql -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!-- Druid连接池依赖，需要指定jar包。SpringBoot并没有管理其版本 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!-- MyBatisPlus依赖，需要指定版本。并未被收录到系统内置配置，需要手动在pom.xml中配置添加 -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.3</version>
    </dependency>

    <!-- SpringBoot测试依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- 其他依赖 -->
    <!-- lombok依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

```groovy
plugins {
	// java插件，这是java工程
	id 'java'
	// 引入springboot插件，第一个插件是指定springboot版本号，第二个插件是进行jar包的依赖管理
	id 'org.springframework.boot' version '3.0.2'
	id 'io.spring.dependency-management' version '1.1.0'
}

dependencies {
	// 导入SpringBoot启动依赖
	implementation 'org.springframework.boot:spring-boot-starter'

	// springboot操作数据库
	// MySQL驱动
	runtimeOnly 'com.mysql:mysql-connector-j'
	// druid连接池，SpringBoot没有管理版本，我们自己导入
	implementation group: 'com.alibaba', name: 'druid', version: '1.1.16'
	// MyBatisPlus依赖，如果SpringBoot版本为3.0.2，那么MyBatisPlus需要3.5.3
	implementation group: 'com.baomidou', name: 'mybatis-plus-boot-starter', version: '3.5.3'

	// 测试依赖
	testImplementation 'org.springframework.boot:spring-boot-starter-test'

	// 其他依赖
	// lombok，annotationProcessor代表main下代码的注解执行器
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
}
```

![](..\图片\5-03【MyBatisPlus】\2-1.png)

从MP的依赖关系可以看出，通过依赖传递已经将MyBatis与MyBatis整合Spring的jar包导入，我们不需要额外在添加MyBatis的相关jar包。

```yaml
# application.yaml配置文件配置
spring:
  # 配置连接数据库四要素
  datasource:
    # MySQL8驱动为com.mysql.cj.jdbc.Driver，之前的是com.mysql.jdbc.Driver
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/linxuan?useSSL=false
    username: root
    password: root
    # 使用Druid连接池技术连接数据库
    type: com.alibaba.druid.pool.DruidDataSource
```

```java
// MP默认情况下会使用模型类的类名首字母小写当表名使用，但是这里并没有user表。所以让查询的表名与实体类产生关联
@TableName("tb_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String tel;
}
```

```java
// Dao接口要想被容器扫描到，可以在Dao接口上添加@Mapper注解，也可以在项目启动类上添加@MapperScan注解
// 该方案的缺点是需要在每一Dao接口中添加注解
@Mapper
public interface UserDao extends BaseMapper<User>{
}
```

```java
@SpringBootApplication
// Dao接口要想被容器扫描到，可以在项目启动类上添加@MapperScan注解，也可以在Dao接口上添加@Mapper注解
// 该方法是只需要写一次，则指定包下的所有Dao接口都能被扫描到，这样@Mapper注解就不用写多次
// @MapperScan("com.linxuan.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

```java
@SpringBootTest
class MpDemoApplicationTests {
    @Autowired
    private UserDao userDao;
    
    @Test
    public void testGetAll() {
        List<User> userList = userDao.selectList(null);
        System.out.println(userList);
    }
}
```

userDao注入的时候下面有红线提示的原因：UserDao是一个接口，不能实例化对象。只有在服务器启动IOC容器初始化后，由框架创建DAO接口的代理对象来注入。现在服务器并未启动，所以代理对象也未创建，IDEA查找不到对应的对象注入，所以提示报红。一旦服务启动，就能注入其代理对象，所以该错误提示不影响正常运行。

## 1.3 标准CRUD使用

在MP中内置了大量的方法，和MyBatis对比如下：

| 功能       | MyBatis自定义接口      | MP接口                                      |
| ---------- | ---------------------- | ------------------------------------------- |
| 新增       | boolean save(T t)      | int insert(T t)                             |
| 删除       | boolean delete(int id) | int deleteById(Serializable id)             |
| 修改       | boolean update(T t)    | int updateById(T t)                         |
| 根据ID查询 | T getById(int id)      | T selectById(Serializable id)               |
| 查询全部   | List<T> getAll()       | List<T> selectList(Wrapper<T> queryWrapper) |

**新增**

新增方法：`int insert(T t)`

```java
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void testSave() {
        User user = new User(null, "张三", "123", 32, "123456");
        int ret = userDao.insert(user);
        System.out.println(ret);
    }
}
```

执行测试后，数据库表中就会添加一条数据。创建tb_user表的时候设置好了为自增策略，所以新增加的数据的ID应该为4，但是这里并不是：

```sql
mysql> select * from tb_user;
+---------------------+---------+------------+-----+-------------+
| id                  | name    | password   | age | tel         |
+---------------------+---------+------------+-----+-------------+
|                   1 | Tom     | tom        |   3 | 18866668888 |
|                   2 | Jerry   | jerry      |   4 | 16688886666 |
|                   3 | linxuan | linxuan666 |  21 | 123456789   |
| 1625117541103857666 | 张三    | 123        |  32 | 123456      | # 主键不是4，问题在于主键ID生成策略
+---------------------+---------+------------+-----+-------------+
4 rows in set (0.00 sec)
```

**删除**

删除方法：`int deleteById (Serializable id)`，参数类型是一个序列化类。MP使用Serializable作为参数类型，就好比可以用Object接收任何数据类型一样。这是因为String和Number是Serializable的子类，Number又是Float、Double、Integer等类的父类，能作为主键的数据类型都已经是Serializable的子类。

```java
public abstract class Number implements java.io.Serializable {}
```

```java
public final class Long extends Number
        implements Comparable<Long>, Constable, ConstantDesc {}
```

```java
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    
    @Test
    void testDelete() {
        int ret = userDao.deleteById(1625117541103857666L);
        System.out.println(ret);
    }
}
```

**修改**

修改方法：`int updateById(T t);`。

```java
@Test
void testUpdate() {
    // 只修改实体对象中有值的字段。如果不传递值过去，那么就不会修改。
    User user = new User(1L, "Tom888", "tom888", null, null);
    int ret = userDao.updateById(user);
    System.out.println(ret);
}
```

**根据ID查询**

根据ID查询的方法：`T selectById (Serializable id)`

```java
@Test
void testGetById() {
    User user = userDao.selectById(2L);
    System.out.println(user);
}
```

**查询所有**

查询所有的方法：`List<T> selectList(Wrapper<T> queryWrapper)`。参数Wrapper：用来构建条件查询的条件，目前直接传为Null。

```java
@Test
void testGetAll() {
    List<User> userList = userDao.selectList(null);
    System.out.println(userList);
}
```

## 1.4 MP Mapper层内置方法

**添加数据**

`int insert(T entity);`：添加一条新数据。

**删除数据**

- `int deleteById(Serializable id);`：根据主键 ID 删除
- `int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);`：根据 map 定义字段的条件删除
- `int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);`：根据实体类定义的 条件删除对象
- `int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);`：进行批量删除。

**修改数据**

- `int updateById(@Param(Constants.ENTITY) T entity);`：根据 ID 修改实体对象。
- `int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);`根据 updateWrapper 条件修改实体对象

**查询数据**

- `T selectById(Serializable id);`：根据 主键 ID 查询数据
- `List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);`：进行批量查询
- `List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);`：根据表字段条件查询
- `T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);`：根据实体类封装对象 查询一条记录
- `Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);`：查询记录的总条数
- `List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);`：查询所有记录（返回 entity 集合）
- `List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);`：查询所有记录（返回 map 集合）
- `List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);`：查询所有记录（但只保存第一个字段的值）
- `<E extends IPage<T>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);`：查询所有记录（返回 entity 集合），分页
- `<E extends IPage<Map<String, Object>>> E selectMapsPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);`：查询所有记录（返回 map 集合），分页

## 1.5 MP Service层内置方法

```java
【添加数据：（增）】
    default boolean save(T entity); // 调用 BaseMapper 的 insert 方法，用于添加一条数据。
    boolean saveBatch(Collection<T> entityList, int batchSize); // 批量插入数据
注：
    entityList 表示实体对象集合 
    batchSize 表示一次批量插入的数据量，默认为 1000
```

```java
【添加或修改数据：（增或改）】
    // id 若存在，则修改， id 不存在则新增数据
    boolean saveOrUpdate(T entity);
    // 先根据条件尝试更新，然后再执行 saveOrUpdate 操作
    default boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper); 
    // 批量插入并修改数据 
    boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize); 
```

```java
【删除数据：（删）】
    // 调用 BaseMapper 的 deleteById 方法，根据 id 删除数据。
    default boolean removeById(Serializable id); 
    // 调用 BaseMapper 的 deleteByMap 方法，根据 map 定义字段的条件删除
    default boolean removeByMap(Map<String, Object> columnMap); 
    // 调用 BaseMapper 的 delete 方法，根据实体类定义的 条件删除对象。
    default boolean remove(Wrapper<T> queryWrapper); 
    // 用 BaseMapper 的 deleteBatchIds 方法, 进行批量删除。
    default boolean removeByIds(Collection<? extends Serializable> idList); 
```

```java
【修改数据：（改）】
    // 调用 BaseMapper 的 updateById 方法，根据 ID 选择修改。
    default boolean updateById(T entity); 
    // 调用 BaseMapper 的 update 方法，根据 updateWrapper 条件修改实体对象。
    default boolean update(T entity, Wrapper<T> updateWrapper); 
    // 批量更新数据
    boolean updateBatchById(Collection<T> entityList, int batchSize); 
```

```java
【查找数据：（查）】
    // 调用 BaseMapper 的 selectById 方法，根据 主键 ID 返回数据。
    default T getById(Serializable id); 
    // 返回一条记录（实体类保存）。
    default T getOne(Wrapper<T> queryWrapper); 
    // 返回一条记录（map 保存）。
    Map<String, Object> getMap(Wrapper<T> queryWrapper); 

    // 调用 BaseMapper 的 selectBatchIds 方法，批量查询数据。
    default List<T> listByIds(Collection<? extends Serializable> idList); 
    // 调用 BaseMapper 的 selectByMap 方法，根据表字段条件查询
    default List<T> listByMap(Map<String, Object> columnMap); 

    // 返回所有数据。
    default List<T> list(); 
    // 调用 BaseMapper 的 selectList 方法，查询所有记录（返回 entity 集合）。
    default List<T> list(Wrapper<T> queryWrapper); 
    // 调用 BaseMapper 的 selectMaps 方法，查询所有记录（返回 map 集合）。
    default List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper); 
    // 返回全部记录，但只返回第一个字段的值。
    default List<Object> listObjs(); 

    // 根据条件返回 记录数。
    default int count(Wrapper<T> queryWrapper); 

    // 调用 BaseMapper 的 selectPage 方法，分页查询
    default <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper); 
    // 调用 BaseMapper 的 selectMapsPage 方法，分页查询
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<T> queryWrapper); 
注：
    get 用于返回一条记录。
    list 用于返回多条记录。
    count 用于返回记录总数。
    page 用于分页查询。
```

```java
【链式调用：】
    default QueryChainWrapper<T> query(); // 普通链式查询
    default LambdaQueryChainWrapper<T> lambdaQuery(); // 支持 Lambda 表达式的修改
    default UpdateChainWrapper<T> update(); // 普通链式修改
    default LambdaUpdateChainWrapper<T> lambdaUpdate(); // 支持 Lambda 表达式的修改
注：
    query 表示查询
    update 表示修改
    Lambda 表示内部支持 Lambda 写法。
形如：
    query().eq("column", value).one();
    lambdaQuery().eq(Entity::getId, value).list();
    update().eq("column", value).remove();
    lambdaUpdate().eq(Entity::getId, value).update(entity);
```

## 1.6 分页功能

分页查询使用的方法是：`IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper);`

- `IPage`：用来构建分页查询条件，同时也是方法的返回值。
- `Wrapper`：用来构建条件查询的条件，目前我们没有可直接传为Null

```java
// IPage是一个接口
public interface IPage<T> extends Serializable {}
```

```java
// Page是它的实现类
public class Page<T> implements IPage<T> {}
```

可以在官网查看步骤https://mp.baomidou.com/，开启分页功能步骤如下：

```java
@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    // 分页查询
    @Test
    void testSelectPage() {
        // 1 创建IPage分页对象,设置分页参数,1为当前页码，3为每页显示的记录数
        IPage<User> page = new Page<>(1, 3);
        // 2 执行分页查询
        userDao.selectPage(page, null);
        // 3 获取分页结果
        System.out.println("当前页码值：" + page.getCurrent());
        System.out.println("每页显示数：" + page.getSize());
        System.out.println("一共多少页：" + page.getPages());
        System.out.println("一共多少条数据：" + page.getTotal());
        System.out.println("数据：" + page.getRecords());
    }
}
```

```java
@Configuration
public class MybatisPlusConfig {

    // 设置分页拦截器
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        // 1 创建MybatisPlusInterceptor拦截器对象
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        // 2 添加分页拦截器
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpInterceptor;
    }
}
```

如果想查看MP执行的SQL语句，我们可以修改application.yml配置文件。在application.yml添加如下信息：

```yml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 打印SQL日志到控制台
```

打开日志后，就可以在控制台打印出对应的SQL语句，开启日志功能性能就会受到影响，调试完后记得关闭。

## 1.7 取消打印日志

测试的时候，控制台打印的日志比较多，速度有点慢而且不利于查看运行结果，所以接下来我们把这个日志处理下:

* 取消初始化spring日志打印，resources目录下添加`logback.xml`，名称固定，内容如下:
  
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <configuration>
  </configuration>
  ```

* 取消MybatisPlus启动banner图标，就是取消在控制台打印mybatisplus图标。
  
  ```yml
  mybatis-plus:
    configuration:
      #在映射实体或者属性时，将数据库中表名和字段名中的下划线去掉，按照驼峰命名法映射
      map-underscore-to-camel-case: true
      # mybatis-plus日志控制台输出
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    global-config:
      # 关闭mybatisplus启动图标
      banner: off 
  ```
  
* 取消SpringBoot的log打印，取消在控制台打印SpringBoot图标。
  
  ```yml
  spring:
    main:
      # 关闭SpringBoot启动图标(banner)
      banner-mode: off 
  ```

解决控制台打印日志过多的相关操作可以不用去做，一般会被用来方便我们查看程序运行的结果。

# 第二章 DQL编程控制

增删改查四个操作中，查询是非常重要的也是非常复杂的操作，这块需要我们重点学习下，这节我们主要学习的内容有四个：条件查询方式、查询投影、查询条件设定、字段映射与表名映射。

## 2.1 条件查询

MyBatisPlus将书写复杂的SQL查询条件进行了封装，使用编程的形式完成查询条件的组合。这个我们在前面都有见过，比如查询所有和分页查询的时候，都有看到过一个`Wrapper`类，这个类就是用来构建查询条件的，如下所示：

* `IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper);`
* `List<T> selectList(Wrapper<T> queryWrapper);`

### 2.1.1 构建条件查询

在进行查询的时候，入口是在Wrapper这个类上，因为它是一个抽象类，所以我们需要去找它对应的实现类，关于实现类也有很多，说明我们有多种构建查询条件对象的方式。

```apl
Object
  |-- Wrapper
       |-- AbstractChainWrapper
       |-- AbstractWrapper # 查询条件封装
            |-- AbstractKtWrapper
            |-- AbstractLambdaWrapper # Lambda 语法使用 Wrapper 统一处理解析 lambda 获取 column
            |    |-- LambdaQueryWrapper # Lambda 语法使用 Wrapper(常用)
            |    |-- LambdaUpdateWrapper # Lambda 更新封装
            |-- QueryWrapper # Entity 对象封装操作类(常用)
            |-- UpdateWrapper # Update 条件封装
```

Wrapper的实现类有多种：`QueryWrapper`、`LambdaQueryWrapper`。

**使用QueryWrapper来构建条件查询**

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll(){
        // 这里不能使用多态，Wrapper里面没有lt方法
        QueryWrapper qw = new QueryWrapper();
        // lt: 小于(<),
        // 最终的sql语句为 SELECT id, name, password, age, tel FROM user WHERE (age < ?)
        qw.lt("age", 18);
        List<User> userList = userDao.selectList(qw);
        System.out.println(userList);
    }
}
```

第一种方式介绍完后，有个小问题就是在写条件的时候，容易出错，比如age写错，就会导致查询不成功。

**QueryWrapper的基础上使用lambda**

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll(){
        // 构建LambdaQueryWrapper的时候泛型不能省。
        QueryWrapper<User> qw = new QueryWrapper<User>();
        // User::getAge 为lambda表达式中的 类名::方法名
        // 最终的sql语句为 SELECT id,name,password,age,tel FROM user WHERE (age < ?)
        qw.lambda().lt(User::getAge, 10);
        List<User> userList = userDao.selectList(qw);
        System.out.println(userList);
    }
}
```

但是这种方式的话因为要一直重复写`.lambda()`，有点麻烦，所以想着把它去掉，那么可以使用第三种方式。

**使用LambdaQueryWrapper构建条件查询**

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll(){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        // User::getAge 为lambda表达式中的 类名::方法名
        // 最终的sql语句为 SELECT id,name,password,age,tel FROM user WHERE (age < ?)
        lqw.lt(User::getAge, 10);
        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

这种方式就解决了上一种方式所存在的问题。

### 2.1.2 多条件构建

有一个需求：查询数据库表中，年龄在10岁到30岁之间的用户信息

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll(){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.lt(User::getAge, 30);
        // gt：大于(>)
        // 最终的SQL语句 SELECT id,name,password,age,tel FROM user WHERE (age < ? AND age > ?)
        lqw.gt(User::getAge, 10);

        // 构建多条件的时候，可以支持链式编程
        // lqw.lt(User::getAge, 30).gt(User::getAge, 10);

        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

有一个需求：查询数据库表中，年龄小于10或年龄大于30的数据

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll(){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        // or()就相当于我们sql语句中的or关键字,不加默认是and
        // 最终的sql语句为 SELECT id,name,password,age,tel FROM user WHERE (age < ? OR age > ?)
        lqw.lt(User::getAge, 10).or().gt(User::getAge, 30);
        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

### 2.1.3 null判定

需求：查询数据库表中，根据输入年龄范围来查询符合条件的记录。用户在输入值的时候，如果只输入第一个框，说明要查询大于该年龄的用户；如果只输入第二个框，说明要查询小于该年龄的用户；如果两个框都输入了，说明要查询年龄在两个范围之间的用户。

如果用户只输入一个数据，那么就需要做第二个框的null值判定，否则会出现查询100<=age<=null，这样肯定不会查询成功的。

那么这个时候问题来了，用户输入了两个age数据，要求查询中间的age。但是后端的实体类中目前只有一个age属性，那么后端如何接收呢？我们可以使用两个简单数据类型，也可以使用一个模型类：

1. 方案一：添加属性age2，这种做法可以但是会影响到原模型类的属性内容
   
   ```java
   @Data
   public class User {
       private Long id;
       private String name;
       private String password;
       private Integer age;
       private String tel;
       // 添加一个属性age2，这种做法可以但是会影响到原模型类的属性内容
       private Integer age2;
   }
   ```
   
2. 方案二：新建一个模型类，让其继承User类，并在其中添加age2属性。UserQuery在拥有User属性后同时添加了age2属性。
   
   ```java
   @Data
   public class UserQuery extends User {
       private Integer age2;
   }
   ```

环境准备好后，我们来实现下刚才的需求：

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll() {
        // 模拟页面传递过来的查询数据
        UserQuery uq = new UserQuery();
        uq.setAge(10);
        uq.setAge2(30);
        // 构建条件查询对象
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        if (null != uq.getAge2()) {
            lqw.lt(User::getAge, uq.getAge2());
        }
        if (null != uq.getAge()) {
            lqw.gt(User::getAge, uq.getAge());
        }
        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

上面的写法可以完成条件为非空的判断，但是问题很明显，如果条件多的话，每个条件都需要判断，代码量就比较大。

MP给我们提供了简化方式：`public Children lt(boolean condition, R column, Object val)`：如果第一个参数condition判断为真，那么才会加入后面的条件。

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll() {
        // 模拟页面传递过来的查询数据
        UserQuery uq = new UserQuery();
        uq.setAge(10);
        uq.setAge2(30);
        // 构建查询条件对象
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.lt(null != uq.getAge2(), User::getAge, uq.getAge2());
        lqw.gt(null != uq.getAge(), User::getAge, uq.getAge());
        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

## 2.2 查询投影

目前我们在查询数据的时候，什么都没有做默认就是查询表中所有字段的内容，我们所说的查询投影即不查询所有字段，只查询出指定内容的数据。

**查询指定字段**

方法为：`select(...)`方法用来设置查询的字段列，可以设置多个

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll(){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        // 最终的sql语句为 SELECT id, name, age FROM user
        lqw.select(User::getId, User::getName, User::getAge);

        // 如果使用的不是lambda，就需要手动指定字段
        // QueryWrapper<User> lqw = new QueryWrapper<User>();
        // lqw.select("id","name","age","tel");

        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

**聚合查询**

需求：聚合函数查询，完成count(总记录数)、max(最大值)、min(最小值)、avg(平均值)、sum(求和)的使用。

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll() {
        // 聚合与分组查询，无法使用lambda表达式来完成
        // 这里使用的是QueryWrapper类
        QueryWrapper<User> qw = new QueryWrapper<User>();

        // count(总记录数) SELECT count(*) as count FROM user
        // qw.select("count(*) as count");
        
        // max(最大值) SELECT max(age) as maxAge FROM user
        // qw.select("max(age) as maxAge");
        
        // min(最小值) SELECT min(age) as minAge FROM user
        // qw.select("min(age) as minAge");
        
        // sum(求和) SELECT sum(age) as sumAge FROM user
        // qw.select("sum(age) as sumAge");
        
        // avg(平均值) SELECT avg(age) as avgAge FROM user
        qw.select("avg(age) as avgAge");
        List<Map<String, Object>> userList = userDao.selectMaps(qw);
        System.out.println(userList);
    }
}
```

**分组查询**

需求：分组查询，完成 group by的查询使用，groupBy为分组。

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testGetAll() {
        // 聚合与分组查询，无法使用lambda表达式来完成
        QueryWrapper<User> lqw = new QueryWrapper<User>();

        // 最终的sql语句为 SELECT count(*) as count, age FROM user GROUP BY age
        lqw.select("count(*) as count, age");
        lqw.groupBy("age");
        List<Map<String, Object>> list = userDao.selectMaps(lqw);
        System.out.println(list);
    }
}
```

## 2.3 查询条件

MP的查询条件有很多：范围匹配（> 、 = 、between）、模糊匹配（like）、空判定（null）、包含性匹配（in）、分组（group）、排序（order）……

官方文档：https://mp.baomidou.com/guide/wrapper.html#abstractwrapper

**范围查询**

| 方法      | 作用            |
| --------- | --------------- |
| eq        | 等于(=)         |
| gt()      | 大于(>)         |
| ge()      | 大于等于(>=)    |
| lt()      | 小于(<)         |
| lte()     | 小于等于(<=)    |
| between() | between ? and ? |

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    // 对年龄进行范围查询
    void testGetAll(){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.between(User::getAge, 10, 30);
        // SELECT id,name,password,age,tel FROM user WHERE (age BETWEEN ? AND ?)
        // selectList：查询结果为多个或者单个
        // selectOne:查询结果为单个
        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

**模糊查询**

| 方法        | 作用                 |
| ----------- | -------------------- |
| like()      | 前后加百分号，如 %J% |
| likeLeft()  | 前面加百分号，如 %J  |
| likeRight() | 后面加百分号，如 J%  |

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    // 查询表中name属性的值以J开头的用户信息，使用like进行模糊查询
    @Test
    void testGetAll(){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.likeRight(User::getName, "J");
        // SELECT id,name,password,age,tel FROM user WHERE (name LIKE ?)
        List<User> userList = userDao.selectList(lqw);
        System.out.println(userList);
    }
}
```

**排序查询**

* `public Children orderBy(boolean condition, boolean isAsc, R... columns)`：condition ：条件，返回boolean，当condition为true，进行排序，如果为false，则不排序。isAsc:是否为升序，true为升序，false为降序。columns：需要操作的列
* `default Children orderByAsc/Desc(R column 单个column)`：按照指定字段进行升序/降序
* `default Children orderByAsc/Desc(R... columns 多个column)`：按照多个字段进行升序/降序
* `default Children orderByAsc/Desc(boolean condition, R... columns)`：condition:条件，true添加排序，false不添加排序。多个columns：按照多个字段进行排序。

```java
@SpringBootTest
class Mybatisplus02DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    // 查询所有数据，然后按照id降序
    @Test
    void testGetAll(){
        LambdaQueryWrapper<User> lwq = new LambdaQueryWrapper<>();
        
        /**
         * public Children orderBy(boolean condition, boolean isAsc, R... columns)
         *  condition ：条件，返回boolean，当condition为true，进行排序，如果为false，则不排序
         *  isAsc:是否为升序，true为升序，false为降序
         *  columns：需要操作的列
         */
        lwq.orderBy(true, false, User::getId);

        List<User> users = userDao.selectList(lqw);
        System.out.println(users);
    }
}
```

## 2.4 映射匹配兼容性

前面我们已经能从表中查询出数据，并将数据封装到模型类中，这整个过程涉及到一张表和一个模型类：表名为tb_user，实体类名为User，实体类的所有属性和表的字段名相同。虽然表名和实体类名不一样，但是在实体类上面使用注解标注了操作的表名称了，所以能够查询成功。

但是如果有下面的情况就会导致查询不成功了：表名与实体类名不一致、表字段与实体类属性名不一致、实体类中添加了表中未定义的字段名、采用默认查询开放了更多的字段查看权限。

**表名与实体类名不一致**

表的名称和模型类的名称不一致，导致查询失败，这个时候通常会报如下错误信息:

```apl
# 数据库中的表不存在
Table 'databaseName.tableNaem' doesn't exist
```

解决方案是使用MP提供的一个注解`@TableName`来设置表与模型类之间的对应关系。

```java
// MP默认情况下会使用模型类的类名首字母小写当表名使用，但是这里并没有user表。所以让查询的表名与实体类产生关联
@TableName("tb_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String tel;
}
```

**表字段与实体类属性名不一致**

当表的列名和实体类的属性名不一致，就会导致数据封装不到模型对象，这个时候就需要其中一方做出修改，可是我们要求两方都不能够修改。MP提供了一个注解`@TableField`，使用该注解可以实现模型类属性名和表的列名之间的映射关系。

```sql
CREATE TABLE tb_user (
    id bigint(20) primary key auto_increment,
    name varchar(32) not null,
    # 将字段password修改成pwd
    pwd varchar(32) not null,
    age int(3) not null ,
    tel varchar(32) not null
);
```

```java
// MP默认情况下会使用模型类的类名首字母小写当表名使用，但是这里并没有user表。所以让查询的表名与实体类产生关联
@TableName("tb_user")
@Data
public class User {
    private Long id;
    private String name;
    // 添加一个@TableField注解，让属性和字段产生关联
    @TableField(value = "pwd")
    private String password;
    private Integer age;
    private String tel;
}
```

**实体类中添加了表中未定义的字段名**

当实体类中多了一个数据库表不存在的字段，就会导致生成的sql语句中在select的时候查询了数据库不存在的字段，程序运行就会报错。

具体的解决方案用到的还是`@TableField`注解，它有一个属性叫`exist`，设置该字段是否在数据库表中存在，如果设置为false则不存在，生成sql语句查询的时候，就不会再查询该字段了。

```sql
CREATE TABLE user (
    id bigint(20) primary key auto_increment,
    name varchar(32) not null,
    password varchar(32) not null,
    age int(3) not null ,
    tel varchar(32) not null,
);
```

```java
// MP默认情况下会使用模型类的类名首字母小写当表名使用，但是这里并没有user表。所以让查询的表名与实体类产生关联
@TableName("tb_user")
@Data
public class User {
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String tel;
    // 该属性名在表中未定义，所以查询的时候排除该字段
    @TableField(exist = false)
    private Integer online;
}
```

**采用默认查询开放了更多的字段查看权限**

查询表中所有的列的数据，就可能把一些敏感数据查询到返回给前端，例如密码。这个时候就需要限制哪些字段默认不要进行查询。

解决方案是`@TableField`注解的一个属性叫`select`，该属性设置默认是否需要查询该字段的值，true(默认值)表示默认查询该字段，false表示默认不查询该字段。

```java
@Data
public class User {
    private Long id;
    private String name;
    // 添加一个@TableField注解，让属性和字段产生关联，设置默认不需要查询该字段的值
    @TableField(select=false)
    private String password;
    private Integer age;
    private String tel;
}
```

| 名称     | @TableField                                                  |
| -------- | ------------------------------------------------------------ |
| 类型     | 属性注解，实体类属性上方定义。                               |
| 作用     | 设置当前属性对应的数据库表中的字段关系                       |
| 相关属性 | value(默认)：设置表与该属性对应的字段名称<br/>exist：设置属性在数据库表字段中是否存在，默认为true，此属性不能与value合并使用<br/>select：设置属性是否参与查询，此属性与select()映射配置不冲突 |

| 名称 | @TableName                                          |
| ---- | --------------------------------------------------- |
| 类型 | 类注解，实体类上方定义。例如`@TableName("tb_user")` |
| 作用 | 设置当前类对应于数据库表关系                        |
| 属性 | value(默认)：设置数据库表名称                       |

# 第三章 DML编程控制

## 3.1 主键生成策略

前面在新增的时候有一个问题：新增成功后，主键ID是一个很长串的内容。

我们更想要的是按照数据库表字段进行自增长。在解决这个问题之前，我们先来分析下ID该如何选择，我们知道不同的表需要应用不同的id生成策略：

* 日志：自增（1,2,3,4，……）
* 购物订单：特殊规则（FQ23948AK3843）
* 外卖单：关联地区日期等信息（10 04 20200314 34 91）
* 关系表：可省略id ……

MP在`@TableId`注解里面定义了了许多的主键生成策略：

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableId {

    String value() default "";
    // 主键类型
    IdType type() default IdType.NONE;
}
```

```java
@Getter
public enum IdType {
    
    private final int key;

    // 数据库ID自增 <p>该类型请确保数据库设置了 ID自增 否则无效</p>
    AUTO(0),
    // 该类型为未设置主键类型(注解里等于跟随全局,全局里约等于 INPUT)  不设置id生成策略
    NONE(1),
    // 用户输入ID <p>该类型可以通过自己注册自动填充插件进行填充</p>   用户手工输入id
    INPUT(2),
    // 插入对象ID 为空，才自动填充。分配ID (主键类型为number或string）雪花算法生成id(可兼容数值型与字符串型)
    ASSIGN_ID(3),
    // 插入对象ID 为空，才自动填充。分配UUID (主键类型为 string)     以UUID生成算法作为id生成策略
    ASSIGN_UUID(4);

    IdType(int key) {
        this.key = key;
    }
}
```

主键ID的生成策略，详细介绍：

* NONE： 不设置id生成策略，MP不自动生成，约等于INPUT，所以这两种方式都需要用户手动设置，但是手动设置第一个问题是容易出现相同的ID造成主键冲突，为了保证主键不冲突就需要做很多判定，实现起来比较复杂。
* AUTO：数据库ID自增，这种策略适合在数据库服务器只有1台的情况下使用，不可作为分布式ID使用。
* ASSIGN_ID：可以在分布式的情况下使用，生成的是Long类型的数字，可以排序性能也高，但是生成的策略和服务器时间有关，如果修改了系统时间就有可能导致出现重复主键。
* ASSIGN_UUID：可以在分布式的情况下使用，而且能够保证唯一，但是生成的主键是32位的字符串，长度过长占用空间而且还不能排序，查询性能也慢。

使用方式如下：

```java
// 让查询的表明与实体类产生关联
@TableName("tb_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    // 主键生成策略 自动增长
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String tel;
}
```

**AUTO策略**

`AUTO`的作用是使用数据库ID自增，在使用该策略的时候一定要确保对应的数据库表设置了ID主键自增，否则无效。

**INPUT策略**

INPUT：用户手工输入id。这种ID生成策略，需要将表的自增策略删除掉。然后设置生成策略 `@TableId(type = IdType.INPUT)`

```sql
ALTER TABLE stu MODIFY id INT PRIMARY KEY;		-- 创建表完成之后，添加主键约束
ALTER TABLE stu DROP PRIMARY KEY;				-- 删除主键约束
```

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testSave() {
        // 手动设置主键ID
        User user = new User(5L, "林炫", "123", 20, "123");
        userDao.insert(user);
    }
}
```

**ASSIGN_ID策略**

ASSIGN_ID：雪花算法生成id(可兼容数值型与字符串型)。设置生成策略为ASSIGN_ID `@TableId(type = IdType.ASSIGN_ID)`

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testSave() {
        User user = new User(null, "林炫", "123", 22, "123");
        userDao.insert(user);
    }
}
```

**ASSIGN_UUID策略**

ASSIGN_UUID：以UUID生成算法作为id生成策略。使用uuid需要注意的是，主键的类型不能是Long，而应该改成String类型。

修改表的主键类型，主键类型设置为varchar，长度要大于32，因为UUID生成的主键为32位，如果长度小的话就会导致插入失败。

```java
@TableId(type = IdType.ASSIGN_UUID)
private String id;
```

```java
@Test
void testSave() {
    User user = new User(null, "林炫", "123", 22, "123");
    userDao.insert(user);
}
```

| 名称 | @TableId                                                     |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解，在实体类表示主键的属性上方定义                     |
| 作用 | 设置当前类中主键属性的生成策略                               |
| 属性 | value(默认)：设置数据库表主键名称。type：设置主键属性的生成策略，值查照IdType的枚举值 |

## 3.2 SnowFlake雪花算法

雪花算法是Twitter公司发明的一种算法，主要目的是解决在分布式环境下，ID怎样生成的问题。雪花算法(SnowFlake)是用Scala写的分布式ID算法，其生成的结果是一个64bit大小整数，它的结构如下图：

![](..\图片\5-03【MyBatisPlus】\2-2.png)

1. 1bit，我们不会使用的，因为二进制中最高位是符号位，1表示负数，0表示正数。生成的id一般都是用整数，所以最高位固定为0。
2. 41bit-时间戳，用来记录时间戳，毫秒级。
3. 10bit-工作机器id，用来记录工作机器id，其中高位5bit是数据中心ID其取值范围0-31，低位5bit是工作节点ID其取值范围0-31，两个组合起来最多可以容纳1024个节点。
4. 序列号占用12bit，每个节点每毫秒0开始不断累加，最多可以累加到4095，一共可以产生4096个ID。

当数据量足够大的时候，一台数据库服务器存储不下，这个时候就需要多台数据库服务器进行存储。比如订单表就有可能被存储在不同的服务器上，如果用数据库表的自增主键，因为在两台服务器上所以会出现冲突。这个时候就需要一个全局唯一ID，这个ID就是分布式ID。

优点：高并发分布式环境下生成不重复 id，每秒可生成百万个不重复 id。基于时间戳，以及同一时间戳下序列号自增，基本保证 id 有序递增。不依赖第三方库或者中间件。算法简单，在内存中进行，效率高。

缺点：依赖服务器时间，服务器时钟回拨时可能会生成重复 id。算法中可通过记录最后一个生成 id 时的时间戳来解决，每次生成 id 之前比较当前服务器时钟是否被回拨，避免生成重复 id。

## 3.3 简化配置

**实体类主键策略设置**

对于主键ID的策略已经介绍完，但是如果要在项目中的每一个模型类上都需要使用相同的生成策略，例如：

```java
@Data
public class User {
    // 在这个项目上所有的实体类的主键上面都是用相同的生成策略，每一个都设置的话，太过于麻烦
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String tel;
}
```

我们只需要在配置文件中添加如下内容：

```yml
mybatis-plus:
  global-config:
    db-config:
      # 配置完成后，每个模型类的主键ID策略都将成为assign_id
      id-type: assign_id
```

**数据库表与实体类的映射关系**

MP会默认将模型类的类名名首字母小写作为表名使用，假如数据库表的名称都以`tb_`开头，那么就需要将所有的实体类类上添加`@TableName`。配置起来还是比较繁琐，简化方式为在配置文件中配置如下内容：

```yml
mybatis-plus:
  global-config:
    db-config:
        # 设置表的前缀内容，这样MP就会拿tb_加上模型类的首字母小写，就刚好组装成数据库的表名。
        table-prefix: tb_
```

```yml
mybatis-plus:
  configuration:
    #在映射实体或者属性时，将数据库中表名和字段名中的下划线去掉，按照驼峰命名法映射
    map-underscore-to-camel-case: true
    # mybatis-plus日志控制台输出
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    # 关闭mybatisplus启动图标
    banner: off 
```

## 3.4 批量操作

API如下：

* `int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);`：删除（根据ID 批量删除），参数是一个集合，可以存放多个id值。
* `List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);`：查询（根据ID 批量查询），参数是一个集合，可以存放多个id值。

```java
@SpringBootTest
class Mybatisplus03DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testDeleteAndSelect(){
        // 删除指定多条数据
        List<Long> list1 = new ArrayList<>();
        list1.add(1402551342481838081L);
        list1.add(1402553134049501186L);
        list1.add(1402553619611430913L);
        userDao.deleteBatchIds(list1);

        // 查询指定多条数据
        List<Long> list2 = new ArrayList<>();
        list2.add(1L);
        list2.add(3L);
        list2.add(4L);
        userDao.selectBatchIds(list2);
    }
}
```

## 3.5 逻辑删除

所以对于删除操作业务问题来说有：

* 物理删除：业务数据从数据库中丢弃，执行的是delete操作
* 逻辑删除：为数据设置是否可用状态字段，删除时设置状态字段为不可用状态，数据保留在数据库中，执行的是update操作

先来分析下问题：

![](..\图片\5-03【MyBatisPlus】/1-6.png)

这是一个员工和其所签的合同表，关系是一个员工可以签多个合同，是一个一(员工)对多(合同)的表。

员工ID为1的张业绩，总共签了三个合同，如果此时他离职了，我们需要将员工表中的数据进行删除，会执行delete操作，如果表在设计的时候有主外键关系，那么同时也得将合同表中的前三条数据也删除掉。

![](..\图片\5-03【MyBatisPlus】/1-7.png)

后期要统计所签合同的总金额，就会发现对不上，原因是已经将员工1签的合同信息删除掉了。如果只删除员工不删除合同表数据，那么合同的员工编号对应的员工信息不存在，那么就会出现垃圾数据，就会出现无主合同，根本不知道有张业绩这个人的存在。

所以经过分析，我们不应该将表中的数据删除掉，而是需要进行保留，但是又得把离职的人和在职的人进行区分。区分的方式，就是在员工表中添加一列数据`deleted`，如果为0说明在职员工，如果离职则将其改完1。如下：

![](..\图片\5-03【MyBatisPlus】/1-8.png)

接下来我们来看一下在MP中如何进行逻辑删除：

1. 修改数据库表添加`deleted`列。字段名可以任意，内容也可以自定义，比如`0`代表正常，`1`代表删除，可以在添加列的同时设置其默认值为`0`正常。
   
2. 实体类添加属性。添加与数据库表的列对应的一个属性名，名称可以任意，如果和数据表列名对不上，可以使用`@TableField`进行关系映射，如果一致，则会自动对应。标识新增的字段为逻辑删除字段，使用`@TableLogic`
   
   ```java
   @Data
   public class User {
       private String id;
       private String name;
       private String password;
       private Integer age;
       private String tel;
       private Integer online;
       // value为正常数据的值，delval为删除数据的值
       @TableLogic(value="0", delval="1")
       private Integer deleted;
   }
   ```
   
3. 运行删除方法
   
   ```java
   @SpringBootTest
   class ApplicationTests {
   
       @Autowired
       private UserDao userDao;
   
       @Test
       void testDelete() {
           // 逻辑删除最后走的是update操作，会将指定的字段修改成删除状态对应的值。
           // 实际执行的SQL语句 UPDATE user SET deleted = 1 WHERE id = ? AND deleted = 0
          userDao.deleteById(1L);
       }
   }
   ```
   

逻辑删除的本质其实是修改操作。如果加了逻辑删除字段，查询数据时也会自动带上逻辑删除字段。

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    // 执行查询操作
    void testFind(){
        // MP的逻辑删除会将所有的查询都添加一个未被删除的条件，也就是已经被删除的数据是不应该被查询出来的
        // 执行的SQL语句 SELECT id, name, password, age, tel, deleted FROM user WHERE deleted = 0
        System.out.println(userDao.selectList(null));
    }
}
```

如果我们想要将已经删除的数据显示出来，那么我们只能够自己写SQL语句来查询了，通过Dao层书写SQL语句，然后调用即可。

如果每个表都要有逻辑删除，那么就需要在每个模型类的属性上添加`@TableLogic`注解，我们可以对此优化一下，在配置文件中添加全局配置，如下:

```yml
mybatis-plus:
  global-config:
    db-config:
      # 逻辑删除字段名
      logic-delete-field: deleted
      # 逻辑删除字面值：未删除为0
      logic-not-delete-value: 0
      # 逻辑删除字面值：删除为1
      logic-delete-value: 1
```

| 名称 | @TableLogic                                                  |
| ---- | ------------------------------------------------------------ |
| 类型 | 属性注解，在实体类用于表示删除字段的属性上方定义。例如`@TableLogic(value="0", delval="1")` |
| 作用 | 标识该字段为进行逻辑删除的字段                               |
| 属性 | value：逻辑未删除值。delval：逻辑删除值。                    |

## 3.6 乐观锁

官方文档：https://baomidou.com/。

乐观锁就是持比较乐观态度的锁。就是在操作数据时非常乐观，认为别的线程不会同时修改数据，所以不会上锁，但是在更新的时候会判断在此期间别的线程有没有更新过这个数据。

乐观锁的实现方式如下：

* 数据库表中添加version列，比如默认值给1

* 第一个线程要修改数据之前，取出记录时，获取当前数据库中的version=1

* 第二个线程要修改数据之前，取出记录时，获取当前数据库中的version=1

* 第一个线程执行更新时，set version = version + 1 where version = oldVersion

* 第二个线程执行更新时，set version = version + 1 where version = oldVersion

* 假如这两个线程都来更新数据，第一个和第二个线程都可能先执行
  
  假如第一个线程先执行更新，会把version改为2，第二个线程再更新的时候，set version = 2 where version = 1，此时数据库表的数据version已经为2，所以第二个线程会修改失败
  
  假如第二个线程先执行更新，会把version改为2，第一个线程再更新的时候，set version = 2 where version = 1，此时数据库表的数据version已经为2，所以第一个线程会修改失败
  
  不管谁先执行都会确保只能有一个线程更新数据，这就是MP提供的乐观锁的实现原理分析。

分析完步骤后，具体的实现步骤如下：

1. 数据库表添加列。列名可以任意，比如使用`version`，给列设置默认值为`1`。
   
2. 在模型类中添加对应的属性。根据添加的字段列名，在模型类中添加对应的属性值。
   
   ```java
   @Data
   public class User {
       private String id;
       private String name;
       private String password;
       private Integer age;
       private String tel;
       private Integer deleted;
       // 添加属性version对应字段version
       @Version
       private Integer version;
   }
   ```
   
3. 添加乐观锁的拦截器
   
   ```java
   @Configuration
   public class MpConfig {
       @Bean
       public MybatisPlusInterceptor mpInterceptor() {
           // 1.定义Mp拦截器
           MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
           // 2.添加乐观锁拦截器
           mpInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
           return mpInterceptor;
       }
   }
   ```

4. 执行更新操作
   
   ```java
   @SpringBootTest
   class Mybatisplus03DqlApplicationTests {
   
       @Autowired
       private UserDao userDao;
   
       @Test
       void testUpdate(){
           User user = new User();
           user.setId(3L);
           user.setName("Jock666");
           user.setVersion(1);
           // SQL操作：UPDATE user SET name=?, version=? WHERE id=? AND version=? AND deleted=0
           userDao.updateById(user);
       }
   }
   
   // ==>  Preparing: UPDATE user SET name=?, version=? WHERE id=? AND version=? AND deleted=0
   // ==> Parameters: Jock777(String), 2(Integer), 3(Long), 1(Integer)
   // <==    Updates: 1
   ```
   
   我们会发现，我们传递的是1，MP会将1进行加1，然后，更新回到数据库表中。可以看一下参数，第二个参数就是更新后的version版本，最后一个参数是我们传递的version版本。
   

所以要想实现乐观锁，首先第一步应该是拿到表中的version，然后拿version当条件在将version加1更新回到数据库表中，所以我们在查询的时候，需要对其进行查询

```java
@SpringBootTest
class Mybatisplus03DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testUpdate(){
        // 1.先通过要修改的数据id将当前数据查询出来
        // 查询数据库数据，目的是获取version的值
        User user = userDao.selectById(3L);
        // 2.将要修改的属性逐一设置进去
        user.setName("Jock888");
        userDao.updateById(user);
    }
}

// ==>  Preparing: SELECT id,name,password,age,tel,deleted,version FROM user WHERE id=? AND deleted=0
// ==> Parameters: 3(Long)
// <==    Columns: id, name, password, age, tel, deleted, version
// <==        Row: 3, Jock777, 123456, 41, 18812345678, 0, 2
// <==      Total: 1
```

大概分析完乐观锁的实现步骤以后，我们来模拟一种加锁的情况，看看能不能实现多个人修改同一个数据的时候，只能有一个人修改成功。

```java
@SpringBootTest
class Mybatisplus03DqlApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testUpdate(){
        //1.先通过要修改的数据id将当前数据查询出来
        // 查询ID为3的数据，此时version=3
        User user = userDao.selectById(3L);     //version=3
        // 查询ID为3的数据，此时version=3
        User user2 = userDao.selectById(3L);    //version=3
        user2.setName("Jock aaa");
        // 执行version = 4 where version = 3，修改完之后数据库表的version变为3
        userDao.updateById(user2);              //version=>4
        user.setName("Jock bbb");
        // 执行version = 4 where version = 3，但是此时version = 4，所以修改失败
        userDao.updateById(user);
    }
}
```

# 第四章 快速开发

## 4.1 代码生成器

1. 创建一个Maven项目

2. 导入对应的jar包
   
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project 
            xmlns="http://maven.apache.org/POM/4.0.0" 
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                                https://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.5.1</version>
       </parent>
       <groupId>com.linxuan</groupId>
       <artifactId>mybatisplus_04_generator</artifactId>
       <version>0.0.1-SNAPSHOT</version>
       <properties>
           <java.version>1.8</java.version>
       </properties>
   
       <!--mybatisplus_04_generator项目中对于MyBatis的环境是没有进行配置-->
       <dependencies>
           <!--spring webmvc-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
   
           <!--mybatisplus-->
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>3.4.1</version>
           </dependency>
   
           <!--druid-->
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>druid</artifactId>
               <version>1.1.16</version>
           </dependency>
   
           <!--mysql-->
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <scope>runtime</scope>
           </dependency>
   
           <!--test-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
   
           <!--lombok-->
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>1.18.12</version>
           </dependency>
   
           <!--代码生成器-->
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-generator</artifactId>
               <version>3.4.1</version>
           </dependency>
   
           <!--velocity模板引擎-->
           <dependency>
               <groupId>org.apache.velocity</groupId>
               <artifactId>velocity-engine-core</artifactId>
               <version>2.3</version>
           </dependency>
   
       </dependencies>
   
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
               </plugin>
           </plugins>
       </build>
   </project>
   ```
   
3. 编写引导类
   
   ```java
   @SpringBootApplication
   public class Mybatisplus04GeneratorApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(Mybatisplus04GeneratorApplication.class, args);
       }
   
   }
   ```

4. 创建代码生成类
   
   ```java
   public class CodeGenerator {
       public static void main(String[] args) {
           //1.获取代码生成器的对象
           AutoGenerator autoGenerator = new AutoGenerator();
   
           //设置数据库相关配置
           DataSourceConfig dataSource = new DataSourceConfig();
           dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
           dataSource.setUrl("jdbc:mysql://localhost:3306/mybatisplus_db?serverTimezone=UTC");
           dataSource.setUsername("root");
           dataSource.setPassword("root");
           autoGenerator.setDataSource(dataSource);
   
           //设置全局配置
           GlobalConfig globalConfig = new GlobalConfig();
           globalConfig.setOutputDir(System.getProperty("user.dir")+"/mybatisplus_04_generator/src/main/java");    //设置代码生成位置
           globalConfig.setOpen(false);    //设置生成完毕后是否打开生成代码所在的目录
           globalConfig.setAuthor("林炫");    //设置作者
           globalConfig.setFileOverride(true);     //设置是否覆盖原始生成的文件
           globalConfig.setMapperName("%sDao");    //设置数据层接口名，%s为占位符，指代模块名称
           globalConfig.setIdType(IdType.ASSIGN_ID);   //设置Id生成策略
           autoGenerator.setGlobalConfig(globalConfig);
   
           //设置包名相关配置
           PackageConfig packageInfo = new PackageConfig();
           packageInfo.setParent("com.aaa");   //设置生成的包名，与代码所在位置不冲突，二者叠加组成完整路径
           packageInfo.setEntity("domain");    //设置实体类包名
           packageInfo.setMapper("dao");   //设置数据层包名
           autoGenerator.setPackageInfo(packageInfo);
   
           //策略设置
           StrategyConfig strategyConfig = new StrategyConfig();
           strategyConfig.setInclude("tbl_user");  //设置当前参与生成的表名，参数为可变参数
           strategyConfig.setTablePrefix("tbl_");  //设置数据库表的前缀名称，模块名 = 数据库表名 - 前缀名  例如： User = tbl_user - tbl_
           strategyConfig.setRestControllerStyle(true);    //设置是否启用Rest风格
           strategyConfig.setVersionFieldName("version");  //设置乐观锁字段名
           strategyConfig.setLogicDeleteFieldName("deleted");  //设置逻辑删除字段名
           strategyConfig.setEntityLombokModel(true);  //设置是否启用lombok
           autoGenerator.setStrategy(strategyConfig);
           //2.执行生成操作
           autoGenerator.execute();
       }
   }
   ```
   
   对于代码生成器中的代码内容，我们可以直接从官方文档中获取代码进行修改:https://mp.baomidou.com/guide/generator.html

5. 运行程序
   
   运行成功后，会在当前项目中生成很多代码，代码包含`controller`,`service`，`mapper`和`entity`。
   

至此代码生成器就已经完成工作，我们能快速根据数据库表来创建对应的类，简化我们的代码开发。

## 4.2 MP中Service的CRUD

回顾我们之前业务层代码的编写，编写接口和对应的实现类：

```java
public interface UserService{
}

@Service
public class UserServiceImpl implements UserService{
}
```

接口和实现类有了以后，需要在接口和实现类中声明方法

```java
public interface UserService{
    public List<User> findAll();
}

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    public List<User> findAll(){
        return userDao.selectList(null);
    }
}
```

MP看到上面的代码以后就说这些方法也是比较固定和通用的，那我来帮你抽取下，所以MP提供了一个Service接口和实现类，分别是：`IService`和`ServiceImpl`，后者是对前者的一个具体实现。

以后我们自己写的Service就可以进行如下修改:

```java
public interface UserService extends IService<User>{

}

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService{

}
```

修改以后的好处是，MP已经帮我们把业务层的一些基础的增删改查都已经实现了，可以直接进行使用。

编写测试类进行测试:

```java
@SpringBootTest
class Mybatisplus04GeneratorApplicationTests {

    private IUserService userService;

    @Test
    void testFindAll() {
        List<User> list = userService.list();
        System.out.println(list);
    }
}
```
