# 第一章 Hibernate基础

Hibernate 是一款主流 ORM（Object Relation Mapping 对象关系映射）框架，将⾯向对象映射成面向关系。

## 1.1 快速入门

```xml
<dependencies>
    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.19</version>
    </dependency>
    <!-- Hibernate依赖 -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.4.10.Final</version>
    </dependency>
    <!-- Lombok简化开发 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.10</version>
    </dependency>
    <!-- Junit依赖 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
<!-- 如果想要取消该配置，可以像Mybatis一样在resources目录下创建同名文件夹放配置文件，最后编译会放在一块 -->
<build>
    <!-- 默认Maven不会读取java目录下面的xml配置文件并编译，所以这里设置Maven控制文件范围 -->
    <!-- 这里标签是resources代表是主文件代码里面的配置文件，如果改为testResources是test里面的配置文件-->
    <resources>
        <!-- 设置控制的资源目录，如果只设置该标签，会直接导致Maven不读取resources目录下面的配置文件 -->
        <resource>
            <!-- 资源目录 -->
            <directory>src/main/java</directory>
            <!-- 需要控制的文件 -->
            <includes>
                <include>**/*.xml</include>
            </includes>
            <!-- 设置能够解析${}，默认是false，不写也可以 -->
            <filtering>false</filtering>
        </resource>
        <!-- 如果只设置扫描java目录，那么会取消扫描resources目录，所以这里重新设置一下 -->
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>
</build>
```

```sql
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
USE linxuan;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS people;
CREATE TABLE IF NOT EXISTS people(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL,
	-- 这里为了简便直接用double类型了，真实情况不能这么使用
	money DOUBLE NOT NULL
);
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO people VALUES(1, "林炫1", 2100), (2, "林炫2", 3300), (3, "林炫3", 3000);
```

```java
@Data
@Accessors(chain = true)
public class People {
    private Integer id;
    private String name;
    private Double money;
}
```

```xml
<?xml version="1.0"?>
<!-- <!DOCTYPE 根标签名称 PUBLIC "dtd文件名称" "dtd文件的位置URL"> -->
<!-- 文件名称为People.hbm.xml，要求该文件在项目编译后和People.java放在同一个目录下 -->
<!-- 如果该文件放在java目录下面，那么需要在pom.xml配置Maven控制文件范围。 -->
<!-- 如果该文件放在resources目录下面，那么需要创建一个同级目录来存放 com/linxuan/entity -->
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <!-- 全类名为com.linxuan.entity.People，对应表为people -->
    <class name="com.linxuan.entity.People" table="people">
        <!-- 主键名称和类型 -->
        <id name="id" type="java.lang.Integer">
            <!-- 对应数据库表的字段 -->
            <column name="id"/>
            <!-- 自增 -->
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>
        <property name="money" type="java.lang.Double">
            <column name="money"/>
        </property>
    </class>
</hibernate-mapping>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- <!DOCTYPE 根标签名称 PUBLIC "dtd文件名称" "dtd文件的位置URL"> -->
<!--　文件名称：hibernate.cfg.xml(默认必须叫做这个名字)，该文件放在resources目录下面 -->
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- 数据源配置 -->
        <property name="connection.username">root</property>
        <property name="connection.password">root</property>
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <!-- 连接URL路径中不要写`&`，写成`&amp;`转义字符。一定要加上serverTimezone=UTC，否则会报错 -->
        <property name="connection.url">
            jdbc:mysql://localhost:3306/linxuan?useUnicode=true&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=UTC
        </property>
        <!-- C3P0 -->
        <property name="hibernate.c3p0.acquire_increment">10</property>
        <property name="hibernate.c3p0.idle_test_period">10000</property>
        <property name="hibernate.c3p0.timeout">5000</property>
        <property name="hibernate.c3p0.max_size">30</property>
        <property name="hibernate.c3p0.min_size">5</property>
        <property name="hibernate.c3p0.max_statements">10</property>
        <!-- 数据库⽅⾔，这里设置为MySQL方言，自动生成MySQL的语句-->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
        <!-- 打印SQL -->
        <property name="show_sql">true</property>
        <!-- 打印SQL的时候格式化SQL -->
        <property name="format_sql">true</property>
        <!-- 是否⾃动⽣成数据库 -->
        <property name="hibernate.hbm2ddl.auto"/>

        <!-- 注册实体关系映射⽂件，注意这里文件分隔符为/ -->
        <mapping resource="com/linxuan/entity/People.hbm.xml"/>
    </session-factory>
</hibernate-configuration>
```

```java
public class Test {
    public static void main(String[] args) {
        // 创建Configuration，configure方法参数可以是配置文件名称(默认是hibernate.cfg.xml)
        Configuration configuration = new Configuration().configure();
        // 获取SessionFactory对象，构建它⾮常耗费资源，所以通常⼀个⼯程只需要创建⼀个SessionFactory。
        // SessionFactory对象：针对单个数据库映射经过编译的内存镜像⽂件，将数据库转换为Java可识别的镜像⽂件
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        // 获取Session对象。根据该对象对数据库操作
        Session session = sessionFactory.openSession();
        People people = new People();
        people.setId(1).setName("林炫").setMoney(3200D);
        // 新增用户
        session.save(people);
        // 开启事务并提交
        session.beginTransaction().commit();
        // 关闭Session对象
        session.close();
    }
}
```

## 1.2 多表操作

多表操作有着一对多和多对多的关系。他们在 Java 和数据库中的体现方式完全不一样，Hibernate 框架的作用就是将这两种方式进行转换和映射。

**一对多关系**

一对多关系经典案例就是客户和订单。每个客户可以购买多个产品并生成多个订单，但是⼀个订单只能属于⼀个客户。所以客户是一，订单是多。

* 数据库体现：数据库中一的一方是主表，多的一方时候从表，通过主外键关系来维护。

* 面向对象体现：使用集合

  ```java
  @Data
  public class Orders {
      private Integer id;
      private String name;
      // 一个订单被一个用户所持有。对一对应对象、对多对应集合
      private Customer customer;
  }
  ```

  ```java
  @Data
  public class Customer {
  
      private Integer id;
      private String name;
      // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
      private Set<Orders> ordersSet;
  }
  ```

**多对多关系**

多对多关系的经典案例就是大学生选课。一门课程可以被多个学生选择，⼀个学生可以选择多门课程，学生是多，课程也是多。

* 数据库体现：数据库中是通过两个一对多关系来维护的，学⽣和课程都是主表，额外增加⼀张中间表作为从表，两张主表和中间表都是一对多关系。

* 面向对象体现：使用集合

  ```java
  @Data
  public class Account {
      private Integer id;
      private String name;
      // 一个学生账户可以选择多个课程。对一对应对象、对多对应集合
      private Set<Course> courses;
  }
  ```

  ```java
  @Data
  public class Course {
      private Integer id;
      private String name;
      // 一个课程可以被多个学生账户选择。对一对应对象、对多对应集合
      private Set<Account> accounts;
  }
  ```

这里做一个测试代码，用于简化测试

```java
public class Test01 {

    private Session session = null;

    /**
     * 生成Session对象
     */
    @Before
    public void createSession() {
        // 创建Configuration，configure方法参数可以是配置文件名称(默认是hibernate.cfg.xml)
        Configuration configuration = new Configuration().configure();
        // 获取SessionFactory对象，构建它⾮常耗费资源，所以通常⼀个⼯程只需要创建⼀个SessionFactory。
        // SessionFactory对象：针对单个数据库映射经过编译的内存镜像⽂件，将数据库转换为Java可识别的镜像文件
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        // 获取Session对象。根据该对象对数据库操作
        session = sessionFactory.openSession();
    }

    /**
     * 关闭Session连接
     */
    @After
    public void closeSession() {
        // 开启事务并提交
        session.beginTransaction().commit();
        // 关闭session会话连接对象
        session.close();
    }
}
```

### 1.2.1 一对多

```sql
-- 创建数据库并使用
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
USE linxuan;

-- 关闭外键约束
SET FOREIGN_KEY_CHECKS = 0;
-- 删除customer顾客数据库
DROP TABLE IF EXISTS customer;
-- 创建ustomer顾客数据库
CREATE TABLE IF NOT EXISTS customer(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL
) COMMENT "顾客表";
-- 删除orders订单数据库
DROP TABLE IF EXISTS orders;
-- 创建orders订单数据库
CREATE TABLE IF NOT EXISTS orders(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL,
	cid INT,
	FOREIGN KEY (cid) REFERENCES customer(id)
) COMMENT "订单表";
-- 开启外键约束
SET FOREIGN_KEY_CHECKS = 1;

-- 插入数据
INSERT INTO customer VALUES(1, "林炫1"), (2, "林炫2"), (3, "林炫3");
INSERT INTO orders VALUES(1, "订单1", 1), (2, "订单2", 1), (3, "订单3", 1), (4, "订单4", 2);
```

```java
@Data
public class Orders {
    private Integer id;
    private String name;
    // 一个订单被一个用户所持有。对一对应对象、对多对应集合
    private Customer customer;
}
```

```java
@Data
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> ordersSet;
}
```

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!-- 文件放在resources目录的com/linxuan/entity目录下面，这样不用配置Maven控制文件范围 -->
<!-- 文件名称：Customer.hbm.xml -->
<hibernate-mapping>
    <!-- Customer顾客表和Orders订单表关系是一对多，一个顾客拥有多个订单 -->
    <class name="com.linxuan.entity.Customer" table="customer">
        <!-- 主键 -->
        <id name="id" type="java.lang.Integer">
            <!-- 主键对应字段 -->
            <column name="id"/>
            <!-- 自增 -->
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>
        <!-- set集合，名称为Customer实体类中设置的集合名称，对应的数据库表为orders -->
        <set name="orders" table="orders">
            <!-- 通过cid外键建立联系 -->
            <key column="cid"/>
            <!-- 一对多关系，多的一方的全类名为com.linxuan.entity.Orders -->
            <one-to-many class="com.linxuan.entity.Orders"/>
        </set>
    </class>
</hibernate-mapping>
```

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!-- 文件放在resources目录的com/linxuan/entity目录下面，这样不用配置Maven控制文件范围 -->
<!-- 文件名称：Orders.hbm.xml -->
<hibernate-mapping>
    <!-- Orders订单表和Customer顾客表关系是多对一，多个订单被一个用户所持有 -->
    <class name="com.linxuan.entity.Orders" table="orders">
        <!-- 主键 -->
        <id name="id" type="java.lang.Integer">
            <!-- 主键对应字段 -->
            <column name="id"/>
            <!-- 自增 -->
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>

        <!-- Orders实体类有一个属性是customer，代表该订单被哪个顾客所持有。他们通过cid字段来联系 -->
        <many-to-one name="customer" class="com.linxuan.entity.Customer" column="cid"/>
    </class>
</hibernate-mapping>
```

```xml
<!-- 注册实体关系映射⽂件，注意这里文件分隔符为/ -->
<mapping resource="com/linxuan/entity/People.hbm.xml"/>
<mapping resource="com/linxuan/entity/Customer.hbm.xml"/>
<mapping resource="com/linxuan/entity/Orders.hbm.xml"/>
```

```java
/**
 * 通过新增来测试一对多
 */
@Test
public void testOneToManyByInsert() {
    Customer customer = new Customer();
    customer.setName("李四");

    Orders orders = new Orders();
    orders.setName("订单~");
    orders.setCustomer(customer);

    session.save(customer);
    session.save(orders);
}
```

### 1.2.2 多对多

```sql
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
USE linxuan;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS t_account;
DROP TABLE IF EXISTS t_course;
DROP TABLE IF EXISTS account_course;
CREATE TABLE IF NOT EXISTS t_account(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL
);
CREATE TABLE IF NOT EXISTS t_course(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(32) NOT NULL
);
CREATE TABLE IF NOT EXISTS account_course(
	id INT PRIMARY KEY AUTO_INCREMENT,
	aid INT NOT NULL,
	cid INT NOT NULL,
	FOREIGN KEY (aid) REFERENCES t_account(id),
	FOREIGN KEY (cid) REFERENCES t_course(id)
);
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO t_account VALUES(1, "张三"), (2, "李四"), (3, "王五");
INSERT INTO t_course VALUES(1, "java"), (2, "数据结构"), (3, "数据库");
INSERT INTO account_course VALUES(1, 1, 1), (2, 1, 2), (3, 3, 2);
```

```java
@Data
public class Account {
    private Integer id;
    private String name;
    // 一个学生账户可以选择多个课程。对一对应对象、对多对应集合
    private Set<Course> courses;
}
```

```java
@Data
public class Course {
    private Integer id;
    private String name;
    // 一个课程可以被多个学生账户选择。对一对应对象、对多对应集合
    private Set<Account> accounts;
}
```

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!-- 文件放在resources目录的com/linxuan/entity目录下面，这样不用配置Maven控制文件范围 -->
<!-- 文件名称：Account.hbm.xml -->
<hibernate-mapping>
    <class name="com.linxuan.entity.Account" table="t_account">
        <id name="id" type="java.lang.Integer">
            <column name="id"/>
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>
        <!-- 多对多在数据库中主要依靠第三个表来维持关系 -->
        <set name="courses" table="account_course">
            <!-- t_account在account_course表中外键是aid -->
            <key column="aid"/>
            <!-- 多对多关系，courses全类名为Course，外键为cid -->
            <many-to-many class="com.linxuan.entity.Course" column="cid"/>
        </set>
    </class>
</hibernate-mapping>
```

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!-- 文件放在resources目录的com/linxuan/entity目录下面，这样不用配置Maven控制文件范围 -->
<!-- 文件名称：Course.hbm.xml -->
<hibernate-mapping>
    <class name="com.linxuan.entity.Course" table="t_course">
        <id name="id" type="java.lang.Integer">
            <column name="id"/>
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>
        <!-- 多对多在数据库中主要依靠第三个表来维持关系 -->
        <set name="accounts" table="account_course">
            <!-- t_course在account_course表中外键是cid -->
            <key column="cid"/>
            <!-- 多对多关系，accounts全类名为Account，外键为aid -->
            <many-to-many class="com.linxuan.entity.Account" column="aid"/>
        </set>
    </class>
</hibernate-mapping>
```

```xml
<!-- 注册实体关系映射⽂件，注意这里文件分隔符为/ -->
<mapping resource="com/linxuan/entity/Account.hbm.xml"/>
<mapping resource="com/linxuan/entity/Course.hbm.xml"/>
```

```java
@Test
public void testManyToManyByInsert() {
    Account account = new Account();
    account.setName("李四");
    account.setId(2);

    Course course = new Course();
    course.setName("数据结构");
    course.setId(2);

    Set<Course> courses = new HashSet<>();
    courses.add(course);
    account.setCourses(courses);

    // 这样新增其实有问题，会新创建这些数据然后新增，并不会关联旧数据
    session.save(course);
    session.save(account);
}
```

## 1.3 循环查询BUG

```java
@Data
public class Orders {
    private Integer id;
    private String name;
    // 一个订单被一个用户所持有。对一对应对象、对多对应集合
    private Customer customer;
}
```

```java
@Data
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> orders;
}
```

```java
/**
 * 测试循环查询的BUG
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    System.out.println(customer);
}
```

因为循环查询的存在，导致测试方法出错了。我们直到`@Data`的注解包括了`@Getter`、`@Setter`、`@toString`、`@EqualsAndHashCode`，正是因为`@toString`的问题导致了循环查询的存在。

`@ToString`会默认生成`toString()`，`Customer`类的`toString()`会查询`Set<Orders>`，`Orders`类又会查询`Customer`，这样循环往复导致了循环查询的存在。

既然是因为`toString()`而导致的，那么我们只需要在实体类中去掉重复调用即可，这里我们在`Customer` 实体类里面覆盖重写该方法。这样就避免了重复调用的问题。

> 覆盖重写`Orders`类中的`toString()`并不能解决问题，这时候去掉`@Data`，改为`@Getter`和`@Setter`

```java
@Data
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> orders;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
/**
 * 测试循环查询的BUG
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    // Customer{id=1, name='林炫1'}
    System.out.println(customer);
}
```

如果需要查询 Customer 关联的 Orders 那么需要更换注解了

```java
@Getter
@Setter
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> orders;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
@Getter
@Setter
public class Orders {
    private Integer id;
    private String name;
    // 一个订单被一个用户所持有。对一对应对象、对多对应集合
    private Customer customer;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
/**
 * 测试循环查询的BUG
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    // [Orders{id=2, name='订单2'}, Orders{id=3, name='订单3'}, Orders{id=1, name='订单1'}]
    System.out.println(customer.getOrders());
}
```

## 1.4 延迟加载

延迟加载又叫做惰性加载、懒加载。使用延迟加载可以提高程序的运行效率，Java 程序与数据库交互的频次越低，程序运行的效率就越⾼，所以我们应该尽量减少 Java 程序与数据库的交互次数，Hibernate 延迟加载就很好的做到了这⼀点。

延迟加载可以看作是⼀种优化机制，根据具体的需求，⾃动选择要执行的 SQL 语句数量。

| lazy属性值 | 解释                                                         |
| :--------: | ------------------------------------------------------------ |
|    true    | 一对多关系中，主表对应实体类的配置文件中配置。默认就是开启延迟加载的 |
|   false    | 一对多关系中，主表对应实体类的配置文件中配置。关闭延迟加载   |
|   extra    | 一对多关系中，主表对应实体类的配置文件中配置。是一种比 true 还要更加智能的延迟加载方式 |
|    fase    | 一对多关系中，从表对应实体类的配置文件中配置。关闭延迟加载   |
|   proxy    | 一对多关系中，从表对应实体类的配置文件中配置。开启延迟加载，默认是开启延迟加载的 |
|  no-proxy  | 一对多关系中，从表对应实体类的配置文件中配置。开启延迟加载   |

### 1.4.1 一对多测试

例如客户和订单的一对多关系，当我们查询客户对象时，因为有级联设置，所以会将对应的订单信息一并查询出来，这样就需要发送两条 SQL 语句，分别查询客户信息和订单信息。而延迟加载的思路是当我们查询客户的时候，如果没有访问订单数据，只发送⼀条 SQL 语句查询客户信息，如果需要访问订单数据，则发送两条 SQL。

一对多的关系中，主表和从表都是默认开启延迟加载的！

```java
@Getter
@Setter
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> orders;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
@Getter
@Setter
public class Orders {
    private Integer id;
    private String name;
    // 一个订单被一个用户所持有。对一对应对象、对多对应集合
    private Customer customer;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

**主表默认开启**

我们查询 Customer，对 orders 进行延迟加载设置，在 customer.hbm.xml 进⾏设置，延迟加载默认开启。

```xml
<!-- 对Orders表的查询开启延迟加载，当然默认就是true开启的 -->
<set name="orders" table="orders" lazy="true">
    <!-- 通过cid外键建立联系 -->
    <key column="cid"/>
    <!-- 一对多关系，多的一方的全类名为com.linxuan.entity.Orders -->
    <one-to-many class="com.linxuan.entity.Orders"/>
</set>
```

```java
/**
 * 测试延迟加载
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    // 只查询主表的数据
    System.out.println(customer);
}

/*
可以看到，SQL语句只执行了一条：
Hibernate: 
    select
        customer0_.id as id1_1_0_,
        customer0_.name as name2_1_0_ 
    from
        customer customer0_ 
    where
        customer0_.id=?
Customer{id=1, name='林炫1'}
*/
```

```java
/**
 * 测试延迟加载
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    // 查询一下从表的数据
    System.out.println(customer.getOrders());
}

/*
SQL语句执行了两条
Hibernate: 
    select
        customer0_.id as id1_1_0_,
        customer0_.name as name2_1_0_ 
    from
        customer customer0_ 
    where
        customer0_.id=?
Hibernate: 
    select
        orders0_.cid as cid3_2_0_,
        orders0_.id as id1_2_0_,
        orders0_.id as id1_2_1_,
        orders0_.name as name2_2_1_,
        orders0_.cid as cid3_2_1_ 
    from
        orders orders0_ 
    where
        orders0_.cid=?
[Orders{id=2, name='订单2'}, Orders{id=3, name='订单3'}, Orders{id=1, name='订单1'}]
*/
```

尝试关闭掉延迟加载看一下打印的 SQL 语句

```xml
<!-- 对Orders表的查询关闭延迟加载，默认是true开启的 -->
<set name="orders" table="orders" lazy="false">
    <!-- 通过cid外键建立联系 -->
    <key column="cid"/>
    <!-- 一对多关系，多的一方的全类名为com.linxuan.entity.Orders -->
    <one-to-many class="com.linxuan.entity.Orders"/>
</set>
```

```java
/**
 * 测试延迟加载
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    System.out.println(customer);
}

/*
可以看到，虽然没有调用从表，但是仍然将从表的数据查询了出来。这就是延迟加载的好处，不需要的不查询，节省资源
Hibernate: 
    select
        customer0_.id as id1_1_0_,
        customer0_.name as name2_1_0_ 
    from
        customer customer0_ 
    where
        customer0_.id=?
Hibernate: 
    select
        orders0_.cid as cid3_2_0_,
        orders0_.id as id1_2_0_,
        orders0_.id as id1_2_1_,
        orders0_.name as name2_2_1_,
        orders0_.cid as cid3_2_1_ 
    from
        orders orders0_ 
    where
        orders0_.cid=?
Customer{id=1, name='林炫1'}
*/
```

**从表默认开启**

| lazy属性值 | 解释                                                         |
| :--------: | ------------------------------------------------------------ |
|    fase    | 一对多关系中，从表对应实体类的配置文件中配置。关闭延迟加载   |
|   proxy    | 一对多关系中，从表对应实体类的配置文件中配置。开启延迟加载，默认是开启延迟加载的 |
|  no-proxy  | 一对多关系中，从表对应实体类的配置文件中配置。开启延迟加载   |

```xml
<!-- Orders实体类有一个属性是customer，代表该订单被哪个顾客所持有。他们通过cid字段来联系 -->
<!-- 不对Orders从表对应实体类延迟加载做改变，使用默认 -->
<many-to-one name="customer" class="com.linxuan.entity.Customer" column="cid" />
```

```java
/**
 * 测试一对多关系从表对应实体类延迟加载
 */
@Test
public void testSelectOrders() {
    Orders orders = session.get(Orders.class, 1);
    System.out.println(orders);
}

/*
默认开启了延迟加载
Hibernate: 
    select
        orders0_.id as id1_2_0_,
        orders0_.name as name2_2_0_,
        orders0_.cid as cid3_2_0_ 
    from
        orders orders0_ 
    where
        orders0_.id=?
Orders{id=1, name='订单1'}
*/
```

### 1.4.2 多对多测试

多对多案例就是学生和课程了，学生账户可以选择多个课程，一个课程可以被多个学生所选择。默认是开启延迟加载的，同样的参数有`true`、`false`、`extra`。

```java
@Getter
@Setter
public class Account {
    private Integer id;
    private String name;
    // 一个学生账户可以选择多个课程。对一对应对象、对多对应集合
    private Set<Course> courses;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
@Getter
@Setter
public class Course {
    private Integer id;
    private String name;
    // 一个课程可以被多个学生账户选择。对一对应对象、对多对应集合
    private Set<Account> accounts;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```



### 1.4.2 extra属性

lazy 除了可以设置 true 和 false 之外，还可以设置 extra，extra 是比 true 更加懒惰的一种加载方式， 或者说是更加智能的⼀种加载方式。

查询 Customer 对象，打印该对象对应的 orders 集合的长度

```java
@Getter
@Setter
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> orders;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
@Getter
@Setter
public class Orders {
    private Integer id;
    private String name;
    // 一个订单被一个用户所持有。对一对应对象、对多对应集合
    private Customer customer;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
/**
 * 测试延迟加载的extra属性
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    System.out.println(customer.getOrders().size());
}

/*
我们可以看到执行了两条SQL语句，第二条SQL语句是将该客户的所有订单信息查询了出来作为Orders对象封装至Set集合中，最后调用customer.getOrders().size()方法获取Orders的长度。
Hibernate: 
    select
        customer0_.id as id1_1_0_,
        customer0_.name as name2_1_0_ 
    from
        customer customer0_ 
    where
        customer0_.id=?
Hibernate: 
    select
        orders0_.cid as cid3_2_0_,
        orders0_.id as id1_2_0_,
        orders0_.id as id1_2_1_,
        orders0_.name as name2_2_1_,
        orders0_.cid as cid3_2_1_ 
    from
        orders orders0_ 
    where
        orders0_.cid=?
3
*/
```

下面来测试一下 extra 属性

```xml
<!-- 对Orders表的开启延迟加载，属性值为extra -->
<set name="orders" table="orders" lazy="extra">
    <!-- 通过cid外键建立联系 -->
    <key column="cid"/>
    <!-- 一对多关系，多的一方的全类名为com.linxuan.entity.Orders -->
    <one-to-many class="com.linxuan.entity.Orders"/>
</set>
```

```java
/**
 * 测试延迟加载的extra属性
 */
@Test
public void testSelectCustomer() {
    Customer customer = session.get(Customer.class, 1);
    System.out.println(customer.getOrders().size());
}

/*
可以看到使用这种方式第二次查询的SQL语句是直接使用了聚合函数来简化查询
Hibernate: 
    select
        customer0_.id as id1_1_0_,
        customer0_.name as name2_1_0_ 
    from
        customer customer0_ 
    where
        customer0_.id=?
Hibernate: 
    select
        count(id) 
    from
        orders 
    where
        cid =?
3
*/
```

# 第二章 配置文件详解

配置文件有两种：hibernate配置文件 hibernate.xml（我们使用的默认名称 hibernate.cfg.xml）和实体关系映射文件 hbm.xml。

```java
// 测试类都添加上了这两个方法
public void Test(){
    private Session session = null;

    /**
     * 生成Session对象
     */
    @Before
    public void createSession() {
        // 创建Configuration，configure方法参数可以是配置文件名称(默认是hibernate.cfg.xml)
        Configuration configuration = new Configuration().configure("hibernate.xml");
        // 获取SessionFactory对象，构建它⾮常耗费资源，所以通常⼀个⼯程只需要创建⼀个SessionFactory。
        // SessionFactory对象：针对单个数据库映射经过编译的内存镜像⽂件，将数据库转换为Java可识别的镜像⽂件
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        // 获取Session对象。根据该对象对数据库操作
        session = sessionFactory.openSession();
    }

    /**
     * 关闭Session连接
     */
    @After
    public void closeSession() {
        // 开启事务并提交
        session.beginTransaction().commit();
        // 关闭session会话连接对象
        session.close();
    }
}
```

## 2.1 hibernate配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- <!DOCTYPE 根标签名称 PUBLIC "dtd文件名称" "dtd文件的位置URL"> -->
<!--　文件名称：hibernate.cfg.xml(默认必须叫做这个名字)，该文件放在resources目录下面 -->
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- 数据源配置 -->
        <property name="connection.username">root</property>
        <property name="connection.password">root</property>
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <!-- 连接URL路径中不要写`&`，写成`&amp;`转义字符。一定要加上serverTimezone=UTC，否则会报错 -->
        <property name="connection.url">
            jdbc:mysql://localhost:3306/linxuan?useUnicode=true&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=UTC
        </property>
        <!-- C3P0 -->
        <property name="hibernate.c3p0.acquire_increment">10</property>
        <property name="hibernate.c3p0.idle_test_period">10000</property>
        <property name="hibernate.c3p0.timeout">5000</property>
        <property name="hibernate.c3p0.max_size">30</property>
        <property name="hibernate.c3p0.min_size">5</property>
        <property name="hibernate.c3p0.max_statements">10</property>
        <!-- 数据库⽅⾔，这里设置为MySQL方言，自动生成MySQL的语句-->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
        <!-- 打印SQL -->
        <property name="show_sql">true</property>
        <!-- 打印SQL的时候格式化SQL -->
        <property name="format_sql">true</property>
        <!-- 是否⾃动⽣成数据库 -->
        <property name="hibernate.hbm2ddl.auto"/>

        <!-- 注册实体关系映射⽂件，注意这里文件分隔符为/ -->
        <mapping resource="com/linxuan/entity/People.hbm.xml"/>
        <mapping resource="com/linxuan/entity/Customer.hbm.xml"/>
        <mapping resource="com/linxuan/entity/Orders.hbm.xml"/>
        <mapping resource="com/linxuan/entity/Account.hbm.xml"/>
        <mapping resource="com/linxuan/entity/Course.hbm.xml"/>
    </session-factory>
</hibernate-configuration>
```

### 2.1.1 使用其他名字

该文件需要默认叫做 hibernate.cfg.xml，如果修改为其他的名字，需要在`Configuration.configure()`重载方法配置一下。这里尝试修改为 hibernate.xml

```java
/**
 * 生成Session对象
 */
@Before
public void createSession() {
    // 创建Configuration，configure方法参数可以是配置文件名称(默认是hibernate.cfg.xml)
    Configuration configuration = new Configuration().configure("hibernate.xml");
    // 获取SessionFactory对象，构建它⾮常耗费资源，所以通常⼀个⼯程只需要创建⼀个SessionFactory。
    // SessionFactory对象：针对单个数据库映射经过编译的内存镜像⽂件，将数据库转换为Java可以识别的镜像⽂件
    SessionFactory sessionFactory = configuration.buildSessionFactory();
    // 获取Session对象。根据该对象对数据库操作
    session = sessionFactory.openSession();
}
```

### 2.1.2 自动生成数据库

- update：动态创建表，如果表存在，则直接使⽤，如果表不存在，则创建。
- create：⽆论表是否存在，都会重新创建。
- create-drop：初始化创建表，程序结束时删除表。
- validate：校验实体关系映射⽂件和数据表是否对应，不能对应直接报错。

## 2.2 实体关系映射文件

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!-- 文件放在resources目录的com/linxuan/entity目录下面，这样不用配置Maven控制文件范围 -->
<!-- 文件名称：Account.hbm.xml，实体类 + .hbm.xml -->
<!-- 如果加上package属性，那么下面class的name不需要写全类名了 -->
<hibernate-mapping package="com.linxuan.entity">
    <class name="com.linxuan.entity.Account" table="t_account">
        <id name="id" type="java.lang.Integer">
            <column name="id"/>
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>
        <!-- 多对多在数据库中主要依靠第三个表来维持关系 -->
        <set name="courses" table="account_course">
            <!-- t_account在account_course表中外键是aid -->
            <key column="aid"/>
            <!-- 多对多关系，courses全类名为Course，外键为cid -->
            <many-to-many class="com.linxuan.entity.Course" column="cid"/>
        </set>
    </class>
</hibernate-mapping>
```

### 2.2.1 hibernate-mapping

* package：给 class 节点对应的实体类统⼀设置包名，此处设置包名，class 的 name 属性就可以省略包名。
* schema：数据库 schema 的名称
* catalog：数据库 catalog 的名称
* default-cascade：默认的级联关系，默认为 none
* default-access：Hibernate ⽤来访问属性的策略
* default-lazy：指定未明确注明 lazy 属性的 Java 属性和集合类，Hibernate 会采用什么样的加载，默认为 true
* auto-import：指定我们是否可以在查询语句中使用非全限定类名，默认为 true。如果项目中有两个同名的持久化类，最好在这两个类的对应映射文件中配置为 false

### 2.2.2 class 标签属性

|      属性      |                            含义                            |
| :------------: | :--------------------------------------------------------: |
|      name      |                          实体类名                          |
|     table      |                          数据表名                          |
|     schema     |  数据库 schema 的名称，会覆盖 hibernate-mapping 的 schema  |
|    catalog     | 数据库 catalog 的名称，会覆盖 hibernate-mapping 的 catalog |
|     proxy      |           指定⼀个接口，在延迟加载时作为代理使⽤           |
| dynamic-update |                          动态更新                          |
| dynamic-insert |                          动态添加                          |
|     where      |                       查询时添加条件                       |

```java
@Data
@Accessors(chain = true)
public class People {
    private Integer id;
    private String name;
    private Double money;
}
```

```xml
<?xml version="1.0"?>
<!-- <!DOCTYPE 根标签名称 PUBLIC "dtd文件名称" "dtd文件的位置URL"> -->
<!-- 文件名称为People.hbm.xml，项目编译后放在和People.java同一个目录下 -->
<!-- 如果该文件放在java目录下面，那么需要在pom.xml配置Maven控制文件范围。 -->
<!-- 如果该文件放在resources目录下面，那么需要创建一个同级目录来存放 com/linxuan/entity -->
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <!-- 全类名为com.linxuan.entity.People，对应表为people -->
    <class name="com.linxuan.entity.People" table="people">
        <!-- 主键名称和类型 -->
        <id name="id" type="java.lang.Integer">
            <!-- 对应数据库表的字段 -->
            <column name="id"/>
            <!-- 自增 -->
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>
        <property name="money" type="java.lang.Double">
            <column name="money"/>
        </property>
    </class>
</hibernate-mapping>
```

#### 动态添加和动态更新

**动态添加**

```java
@Test
public void testSavePeople() {
    People people = new People();
    people.setName("张三");
    session.save(people);
}
// Hibernate: insert into people (name, money) values (?, ?)
```

虽然我们只设置了名称，但是可以看到 SQL 语句同时设置了 money，其实根本没有必要，因为即使 SQL 语句不设置，数据库也会自动给我们添上为 Null。所以这时候可以使用上动态更新属性，Hibernate 会读取我们需要设置的对象，如果没有设置值，那么创建 SQL 语句的时候也不会加上它。

```xml
<class name="com.linxuan.entity.People" table="people" dynamic-insert="true">
</class>
```

```java
@Test
public void testSavePeople() {
    People people = new People();
    people.setName("张三");
    session.save(people);
}
// Hibernate: insert into people (name) values (?)
```

**动态更新**

```java
@Test
public void testSavePeople() {
    People people = session.get(People.class, 1);
    people.setMoney(8888D);
    session.update(people);
}
// Hibernate: select people0_.id as id1_3_0_, people0_.name as name2_3_0_, people0_.money as money3_3_0_ from people people0_ where people0_.id=?
// Hibernate: update people set name=?, money=? where id=?
```

第一条 SQL 是查询信息，然后修改查询出来的数据，最后更新一下。可以看到更新的时候同时更新了一下  name 字段，但是其实根本就没有必要，因为我们修改数据的时候没有修改 name 字段，所以无需更新。这时候可以用到动态更新。

```xml
<!-- 设置动态更新 -->
<class name="com.linxuan.entity.People" table="people" dynamic-update="true"></class>
```

#### where 添加查询条件

先使用 HQL 查询一下所有的 people 表信息

```java
@Test
public void testSelectPeople() {
    // 字符串会报警告信息，这是因为需要填写People的全类名，不填也可以
    String hql = "from People";
    Query query = session.createQuery(hql);
    List peopelList = query.list();

    peopelList.forEach(System.out::println);
}
// Hibernate: select people0_.id as id1_3_, people0_.name as name2_3_, people0_.money as money3_3_ from people people0_
```

然后使用 where 属性来添加一下查询条件

```xml
<!-- People.hbm.xml配置文件的class标签使用where属性来添加依稀啊查询条件 -->
<class name="com.linxuan.entity.People" table="people" dynamic-update="true" where="id = 6"></class>
```

```java
@Test
public void testSelectPeople() {
    // 字符串会报警告信息，这是因为需要填写People的全类名，不填也可以
    String hql = "from People";
    Query query = session.createQuery(hql);
    List peopelList = query.list();

    peopelList.forEach(System.out::println);
}
// Hibernate: select people0_.id as id1_3_, people0_.name as name2_3_, people0_.money as money3_3_ from people people0_ where ( people0_.id = 6)
```

### 2.2.3 ID 标签属性

| 属性名称 | 含义               |
| -------- | ------------------ |
| name     | 实体类属性名       |
| type     | 实体类属性数据类型 |

type 可以设置两种类型的数据，Java 数据类型或者 Hibernate 映射类型。

实体类的属性数据类型必须与数据表对应的字段数据类型⼀致，比如 int 对应 int，String 对应 varchar。Java 数据类型映射到 Hibernate 映射类型，再由 Hibernate 映射类型映射到 SQL 数据类型。

ID 标签内部可以包含 column 标签（数据表的主键字段名）和 generator 标签（主键生成策略）。

generator 标签值如下：

1. hilo 算法
2. increment：Hibernate ⾃增
3. identity：数据库⾃增
4. native：本地策略，根据底层数据库自动动选择主键的生成策略
5. uuid.hex 算法
6. select 算法

```xml
<!-- 采用数据库提供的sequence机制生成主键，用于Oracle、PG、DB数据库，MySQL没有序列机制 -->
<generator class="sequence">
    <!-- Hibernate生成主键时，查找sequence并赋给主键值，主键值由数据库生成，Hibernate不负责维护 -->
    <param name="sequence">SEQ_TS_KPI_REPORT_DAP_ID</param>
</generator>
```

### 2.2.4 property 属性

```xml
<property name="name" type="java.lang.String">
    <column name="name"/>
</property>
```

- name：实体类的属性名
- type：数据类型
- column：数据表字段名
- update：该字段是否可以修改，默认为 true
- insert：该字段是否可以添加，默认为 true
- lazy：延迟加载策略

## 2.3 解决主外键重复约束

Customer 和 Orders 是一对多关系。一个 Customer 对应多个 Orders，实体类中用一个 set 集合来表示对应的 Orders。多个 Orders 对应一个 Customer，在实体类中使用一个 Customer 类来表示对应的 Customer。

正常而言使用这样且赋值操作是没有任何问题的，但是如果我们在两个对象中重复维护各自的关系，这样就会出错了。

```java
@Getter
@Setter
public class Customer {

    private Integer id;
    private String name;
    // 一个顾客可以拥有多个订单。对一对应对象、对多对应集合
    private Set<Orders> orders;
    
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```java
@Getter
@Setter
public class Orders {
    private Integer id;
    private String name;
    // 一个订单被一个用户所持有。对一对应对象、对多对应集合
    private Customer customer;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!-- 文件放在resources目录的com/linxuan/entity目录下面，这样不用配置Maven控制文件范围 -->
<!-- 文件名称：Customer.hbm.xml -->
<hibernate-mapping>
    <!-- Customer顾客表和Orders订单表关系是一对多，一个顾客拥有多个订单 -->
    <class name="com.linxuan.entity.Customer" table="customer">
        <!-- 主键 -->
        <id name="id" type="java.lang.Integer">
            <!-- 主键对应字段 -->
            <column name="id"/>
            <!-- 自增 -->
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>
        <!-- 对Orders表的开启延迟加载，属性值为extra -->
        <set name="orders" table="orders">
            <!-- 通过cid外键建立联系 -->
            <key column="cid"/>
            <!-- 一对多关系，多的一方的全类名为com.linxuan.entity.Orders -->
            <one-to-many class="com.linxuan.entity.Orders"/>
        </set>
    </class>
</hibernate-mapping>
```

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!-- 文件放在resources目录的com/linxuan/entity目录下面，这样不用配置Maven控制文件范围 -->
<!-- 文件名称：Orders.hbm.xml -->
<hibernate-mapping>
    <!-- Orders订单表和Customer顾客表关系是多对一，多个订单被一个用户所持有 -->
    <class name="com.linxuan.entity.Orders" table="orders">
        <!-- 主键 -->
        <id name="id" type="java.lang.Integer">
            <!-- 主键对应字段 -->
            <column name="id"/>
            <!-- 自增 -->
            <generator class="identity"/>
        </id>
        <property name="name" type="java.lang.String">
            <column name="name"/>
        </property>

        <!-- Orders实体类有一个属性是customer，代表该订单被哪个顾客所持有。他们通过cid字段来联系 -->
        <!-- 不对Orders从表对应实体类延迟加载做改变，使用默认 -->
        <many-to-one name="customer" class="com.linxuan.entity.Customer" column="cid" />
    </class>
</hibernate-mapping>
```

```java
@Test
public void testCustomerAndOrders() {
    Customer customer = new Customer();
    customer.setName("王五");

    Orders orders1 = new Orders();
    Orders orders2 = new Orders();
    orders1.setName("订单orders1");
    orders2.setName("订单orders2");

    orders1.setCustomer(customer);
    orders2.setCustomer(customer);

    session.save(customer);
    session.save(orders1);
    session.save(orders2);
}

// 执行的 SQL 语句如下
// Hibernate: insert into customer (name) values (?)
// Hibernate: insert into orders (name, cid) values (?, ?)
// Hibernate: insert into orders (name, cid) values (?, ?)
```

上面的执行是没有任何问题的，向顾客表和订单表分别插入了一条语句和两条语句，并且建立了主外键约束关系。但是如果我们在测试类中重复添加他们的约束关系的话，这时候执行 SQL 语句就会出错了。

```java
@Test
public void testCustomerAndOrders() {
    Customer customer = new Customer();
    customer.setName("赵六");

    Orders orders1 = new Orders();
    Orders orders2 = new Orders();
    orders1.setName("订单orders1");
    orders2.setName("订单orders2");

    // 重复建立约束关系
    orders1.setCustomer(customer);
    orders2.setCustomer(customer);
    Set<Orders> orders = new HashSet<>();
    orders.add(orders1);
    orders.add(orders2);
    customer.setOrders(orders);

    session.save(customer);
    session.save(orders1);
    session.save(orders2);
}

// Hibernate: insert into customer (name) values (?)
// Hibernate: insert into orders (name, cid) values (?, ?)
// Hibernate: insert into orders (name, cid) values (?, ?)
// Hibernate: update orders set cid=? where id=?
// Hibernate: update orders set cid=? where id=?
```

可以看到这时候就执行了 5 条 SQL 语句，最后两条更新的 SQL 语句根本没有任何意义，白白浪费了性能。就是因为双方都在维护主外键约束关系，导致多执行了 SQL 语句，其实没有必要双方都去维护，一方维护即可。

可以使用如下两种方式来解决：

1. 在 Java 代码中去掉一方维护关系代码。
2. 通过配置来解决。

这里使用第二种方式解决

```xml
<!-- inverse属性⽤来设置是否将维护权交给对⽅，默认false，不交出维护权。设置为true，Customer放弃维护。 -->
<set name="orders" table="orders" lazy="extra" inverse="true">
    <key column="cid"/>
    <one-to-many class="com.linxuan.entity.Orders"/>
</set>
```

## 2.4 cascade设置级联操作

Customer 和 Orders 是一对多关系。通过 Hibernate 直接删除 customer 主表中的数据，如果从表也有数据，那么无法删除成功，这个时候就可以通过 cascade 标签来设置级联操作。

```xml
<!-- 添加级联删除 -->
<set name="orders" table="orders" inverse="true" cascade="delete">
    <!-- 通过cid外键建立联系 -->
    <key column="cid"/>
    <!-- 一对多关系，多的一方的全类名为com.linxuan.entity.Orders -->
    <one-to-many class="com.linxuan.entity.Orders"/>
</set>
```

```java
@Test
public void testDeleteCustomer() {
    Customer customer = session.get(Customer.class, 7);
    session.delete(customer);
}
// 直接级联删除了
// Hibernate: select customer0_.id as id1_1_0_, customer0_.name as name2_1_0_ from customer customer0_ where customer0_.id=?
// Hibernate: select orders0_.cid as cid3_2_0_, orders0_.id as id1_2_0_, orders0_.id as id1_2_1_, orders0_.name as name2_2_1_, orders0_.cid as cid3_2_1_ from orders orders0_ where orders0_.cid=?
// Hibernate: delete from orders where id=?
// Hibernate: delete from orders where id=?
// Hibernate: delete from customer where id=?
```

# 第三章 Hibernate HQL语句

HQL：Hibernate Query Language，是 Hibernate 框架提供的一种查询机制，它和 SQL 类似，不同的是 HQL 是⾯向对象的查询语句，让开发者能够以⾯向对象的思想来编写查询语句，对 Java 编程是⼀种好友好的方式。

HQL 只能完成查询、修改、删除，新增是⽆法操作的。

```java
// 测试类都添加上了这两个方法
public void Test(){
    private Session session = null;

    /**
     * 生成Session对象
     */
    @Before
    public void createSession() {
        // 创建Configuration，configure方法参数可以是配置文件名称(默认是hibernate.cfg.xml)
        Configuration configuration = new Configuration().configure("hibernate.xml");
        // 获取SessionFactory对象，构建它⾮常耗费资源，所以通常⼀个⼯程只需要创建⼀个SessionFactory。
        // SessionFactory对象：针对单个数据库映射经过编译的内存镜像⽂件，将数据库转换为Java可识别的镜像⽂件
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        // 获取Session对象。根据该对象对数据库操作
        session = sessionFactory.openSession();
    }

    /**
     * 关闭Session连接
     */
    @After
    public void closeSession() {
        // 开启事务并提交
        session.beginTransaction().commit();
        // 关闭session会话连接对象
        session.close();
    }
}
```

## 3.1 查询对象

查询表中所有数据，自动完成对象的封装，返回 List 集合。HQL 进⾏查询，from 关键字后⾯不能写表名，必须写表对应的实体类名。

```java
@Test
public void testGeAllPeople() {
    // from后面跟全类名，可以只写实体类名，但是会爆红
    String hql = "from People";

    Query query = session.createQuery(hql);
    List list = query.list();
    list.forEach(System.out::println);
}
```

## 3.2 分页查询

HQL 分页查询可以通过调用 query 的方法来完成。

1. `setFirstResult()` 设置起始下标
2. `setMaxResults()` 设置截取长度

```java
@Test
public void testPagePeople() {
    // from后面跟全类名，可以只写实体类名，但是会爆红
    String hql = "from com.linxuan.entity.People";

    Query query = session.createQuery(hql);
    query.setFirstResult(1);
    query.setMaxResults(5);
    List list = query.list();
    list.forEach(System.out::println);
}
```

## 3.3 占位符

```java
@Test
public void testPeople() {
    // from后面跟全类名，可以只写实体类名，但是会爆红。占位查询
    String hql = "from com.linxuan.entity.People where name = :name";

    Query query = session.createQuery(hql);
    query.setString("name", "张三");
    List list = query.list();
    list.forEach(System.out::println);
}
```

## 3.4 级联查询

Customer 和 Orders 是一对多关系。

```java
@Test
public void testPeople() {
    // from后面跟全类名，可以只写实体类名，但是会爆红。级联查询
    String hql1 = "from Customer where name = :name";
    String hql2 = "from Orders where customer = :customer";

    Query query1 = session.createQuery(hql1);
    query1.setString("name", "王五");
    Customer customer = (Customer) query1.uniqueResult();

    Query query2 = session.createQuery(hql2);
    query2.setEntity("customer", customer);

    List list = query2.list();
    list.forEach(System.out::println);
}
```

## 3.5 查询实体对象

```java
@Test
public void testPeople() {
    // from后面跟全类名，可以只写实体类名，但是会爆红。直接查询实体对象
    String hql = "from com.linxuan.entity.People where id = 6";

    Query query = session.createQuery(hql);
    People people = (People) query.uniqueResult();
    System.out.println(people);
}
```

## 3.6 和 SQL 语句相同的查询

**where条件查询**

HQL 直接追加 where 关键字作为查询条件，与 SQL 没有区别。

```java
@Test
public void testPeopleByWhere() {
    // from后面跟全类名，可以只写实体类名，但是会爆红
    String hql = "from com.linxuan.entity.People where id = 6";

    Query query = session.createQuery(hql);
    // query.list()返回集合，集合中只有一个对象，直接使用get(0)获取返回对象即可
    People people = (People) query.list().get(0);
    System.out.println(people);
}
```

**模糊查询**

```java
@Test
public void testPeopleByLike() {
    // from后面跟全类名，可以只写实体类名，但是会爆红
    String hql = "from com.linxuan.entity.People where name like '%三%'";

    Query query = session.createQuery(hql);
    List list = query.list();
    list.forEach(System.out::println);
}
```

**order by排序**

```java
@Test
public void testPeopleByOrderBy() {
    // from后面跟全类名，可以只写实体类名，但是会爆红。desc降序排序
    String hql = "from com.linxuan.entity.People order by id desc";

    Query query = session.createQuery(hql);
    List list = query.list();
    list.forEach(System.out::println);
}
```

