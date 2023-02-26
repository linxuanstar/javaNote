# 第一章 JDBC快速入门

概念：`java database connectivity` java数据库连接，java语言操作数据库。

JDBC本质：其实就是官方（sun公司）定义的一套操作所有关系型数据库的规则，即接口。各个数据库厂商去实现这套接口，提供数据库驱动jar包。我们可以使用这套接口（JDBC）编程，真正执行的代码是驱动jar包中的实现类。

## 1.1 连接数据库步骤

步骤如下：

1. 导入驱动jar包，`mysql-connector-java-5.1.37-bin.jar`。在idea模块中创建libs文件夹复制jar包到文件夹里面，然后右键点击`ADD as Library`。

2. 注册驱动

3. 获取数据库连接对象 Connection

4. 定义sql

5. 获取执行sql语句对象 Statement

6. 执行sql，接受返回结果

7. 处理结果

8. 释放资源

```java
public class Demo01Jdbc {
    public static void main(String[] args) throws Exception {

        // 1. 导入驱动jar包，mysql-connector-java-5.1.37-bin.jar。
        // 2. 注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 3. 获取数据库连接对象 Connection
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/day3", "root", "root");
        // 4. 定义sql
        String sql = "update account set balance = 2000 where id = 1";
        // 5. 获取执行sql语句对象 Statement
        Statement statement = connection.createStatement();
        // 6. 执行sql，接受返回结果
        int i = statement.executeUpdate(sql);
        // 7. 处理结果
        System.out.println(i);
        // 8. 释放资源
        statement.close();
        connection.close();
    }
}
```

- `DriverManage`：驱动管理对象
- `Connection`：数据库连接对象
- `Statement`：执行sql的对象
- `ResultSet`：结果集对象
- `PreparedStatement`：执行sql的对象

## 1.2 DriverManage

`DriverManage`：驱动管理对象。注册驱动，告诉程序该使用哪一个数据库驱动jar包

* `static void registerDriver(Driver driver)`：注册与给定的驱动程序`DriverManager`。

* 我们写代码使用的是：`Class.forName("com.mysql.jdbc.Driver");`

* 但是可以查看源代码，在`"com.mysql.jdbc.Driver"`类里面查看发现存在着静态代码块

  ```java
  static {
      try {
          java.sql.DriverManager.registerDriver(new Driver());
      } catch (SQLException E) {
          throw new RuntimeException("Can't register driver!");
      }
  }
  ```

* 所以我们依然使用的是`DriverManager`驱动管理对象。

* 注意：mysql5之后的驱动jar包是可以省略注册驱动的步骤的，但是最好还是加上去。

**获取数据库连接**

`static Connection getConnection(String url, String user, String password)`：获取数据库连接。

参数如下：

- `url`：指定连接的路径。`jdbc:mysql://IP地址(域名):端口号/数据库名称`，例如`jdbc:mysql://localhost:3306/day3`
- `user`：用户名
- `password`：密码

> 如果连接的是本机mysql服务器，并且mysql服务默认端口是3306，那么URL可以简写成为：`jdbc:mysql:///数据库名称`，三个杠。

## 1.3 Connection

`Connection`：数据库连接对象

**获取执行SQL的对象**

* `Statement createStatement()` ：创建一个 `Statement`对象，用于将SQL语句发送到数据库。
* `PreparedStatement prepareStatement(String sql)`：创建一个 `PreparedStatement`对象，用于将参数化的SQL语句发送到数据库。  

**管理事务**

* 开启事务：`void setAutoCommit(boolean autoCommit)`：将此连接的自动提交模式设置为给定状态。调用该方法设置参数为`FALSE`，即开启该事务。
* 提交事务：`commit()`；
* 回滚事务：`rollback()`;

```java
public class Demo01Jdbc {

    public static void main(String[] args) throws Exception {

        // 1. 导入驱动jar包，mysql-connector-java-5.1.37-bin.jar。
        // 2. 注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 3. 获取数据库连接对象 Connection
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/day3", "root", "root");
        // 4. 定义sql
        String sql = "update account set balance = 2000 where id = 1";
        // 5. 获取执行sql语句对象 Statement
        Statement statement = connection.createStatement();
        // 6. 执行sql，接受返回结果
        int i = statement.executeUpdate(sql);
        // 7. 处理结果
        System.out.println(i);
        // 8. 释放资源
        statement.close();
        connection.close();
    }
}
```

## 1.4 Statement

`Statement`：执行sql的对象

**执行sql**

* `boolean execute(String sql)`：可以执行任意的sql，但是不要用，了解即可

* `int executeUpdate(String sql)`：执行`DDL（create，alter， drop）`、`DML（insert， update， delete）`语句。方法的返回值是影响的行数，可以通过这个影响的行数来判断DML语句是否被执行成功，返回值`>0`则成功，反之则失效。

* `ResultSet executeQuery(String sql)`：执行DQL（select）语句。方法的返回值是一个结果集。

## 1.5 JDBC练习

**insert语句**

```java
public class Demo02JDBC {
    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;
        try {
            // 1.注册驱动，如果不注册也没有问题，会自动注册上去
            Class.forName("com.mysql.jdbc.Driver");
            // 2.定义sql
            String sql = "insert into account values(null, 'wangwu', 3000)";
            // 3.获取Connection对象
            connection = DriverManager.getConnection("jdbc:mysql:///day3", "root", "root");
            // 4.获取执行sql对象 statement
            statement = connection.createStatement();
            // 5.执行sql 返回影响的行数
            int count = statement.executeUpdate(sql);
            // 6.处理结果
            System.out.println(count);
            if (count > 0) {
                System.out.println("添加成功！");
            } else {
                System.out.println("添加失败！");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 避免空指针异常
            // statement.close();
            // 7.释放资源
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

## 1.6 ResultSet功能

`ResultSet`：结果集对象，封装查询结果

`next()`：游标向下移动一行。

`getXXX(参数)`：获取数据。

* `XXX`：代表数据类型	如：`int getInt()`，`String getString()`
* 参数：
  1. `int`：代表列的编号，**从1开始**	如：`getString(1)`
  2. `String`：代表列的名称。               如：`getDouble("balance")`

```java
public class Demo06JDBC {
    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            // 1.注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2.创建SQL语句
            String sql = "select * from account";
            // 3.创建Connection对象
            connection = DriverManager.getConnection("jdbc:mysql:///day3", "root", "root");
            // 4.获取执行mysql对象
            statement = connection.createStatement();
            // 5.执行sql对象
            rs = statement.executeQuery(sql);
            // 6.处理结果
            // 让游标向下移动一行
            while(rs.next()) {
                // 获取数据
                int id = rs.getInt(1);
                String name = rs.getString("name");
                double balance = rs.getDouble(3);

                System.out.println(id + "---" + name + "---" + balance);
            };
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

**练习**

把emp表所有的内容封装一起

```java
// 导入一个Lambok的注解 后面MybatisPlus会讲到 等于导入了Get Set方法，toString、equals和hashcode方法
@Data
public class Emp {
    private int id;
    private String ename;
    private int job_id;
    private int mgr;
    private Date joindate;
    private double salary;
    private double bonus;
    private int dept_id;
}
```

```java
public class Demo01TestJDBC {

    public static void main(String[] args) {
        List<Emp> list = new Demo01TestJDBC().findAll();
        System.out.println(list);
    }

    public List<Emp> findAll() {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Emp> list = null;

        try {
            // 1.注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2.定义SQL语句
            String sql = "select * from emp";
            // 3.获取连接
            connection = DriverManager.getConnection("jdbc:mysql:///day3", "root", "root");
            // 4.获取执行SQL语句对象
            statement = connection.createStatement();
            // 5.执行SQL语句
            resultSet = statement.executeQuery(sql);

            // 6.遍历结果集，封装对象，装载集合。
            Emp emp = null;
            list = new ArrayList<Emp>();
            while (resultSet.next()) {
                // 获取数据
                int id = resultSet.getInt("id");
                String ename = resultSet.getString("ename");
                int job_id = resultSet.getInt("job_id");
                int mgr = resultSet.getInt("mgr");
                Date joindate = resultSet.getDate("joindate");
                double salary = resultSet.getDouble("salary");
                double bonus = resultSet.getDouble("bonus");
                int dept_id = resultSet.getInt("dept_id");
                // 创建emp对象，并赋值
                emp = new Emp();
                emp.setId(id);
                emp.setEname(ename);
                emp.setJob_id(job_id);
                emp.setMgr(mgr);
                emp.setJoindate(joindate);
                emp.setSalary(salary);
                emp.setBonus(bonus);
                emp.setDept_id(dept_id);
                // 装载集合
                list.add(emp);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return list;
    }
}
```

## 1.7 PreparedStatement功能

`PreparedStatement`：执行sql的对象

### 1.7.1 SQL注入问题

在拼接SQL的时候，有一些SQL的特殊关键字参与字符串的拼接。会造成安全性问题

例如：输入用户名称随便，输入密码的时候：`a' or 'a' = 'a'`

那么SQL语句就变成了：`select * from user where username = "lshfow" and password = 'a' or 'a' = 'a'`

由于用户输入的字符串里面有一个or，而当or判定成为TRUE的时候，系统就会查询所有的表格，因此用户会成功进入系统。

* 解决SQL注入问题：使用`PreparedStatement`对象来解决。
* 预编译的SQL：参数使用？来作为占位符

后期我们都会使用PreparedStatement来完成增删改查的所有操作，这样可以防止SQL注入，另外效率更高。

### 1.7.2 解决SQL注入

1. 导入驱动jar包

2. 注册驱动

3. 获取数据库连接对象 Connection

4. 定义SQL语句

   注意：SQL的参数使用？作为占位符。例如：`select * from user where username = ？and password = ?`

5. 获取执行SQL语句的对象`PreparedStatement`      `Connection.prepareStatement(String sql)`

6. 给`？`赋值

   方法：`setXXX(参数1， 参数2)`

   *  `参数1`：`？`的位置编号 从1开始
   *  `参数2`：`？`的值

7. 执行SQL，接受返回结果，不需要传递SQL语句

8. 处理结果

9. 释放资源

```java
// 解决SQL注入问题
public class Demo02JDBC {
    public static void main(String[] args) {
        // 键盘录入，接收用户名称和密码
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名称：");
        String username = scanner.nextLine();
        System.out.println("请输入密码");
        String password = scanner.nextLine();
        
        // 调用方法
        boolean flag = longin(username, password);
        
        // 判断结果
        if (flag) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }

    public static boolean longin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 获取连接
            connection = JDBCUtils.connection();
            // 定义sql
            String sql = "select * from user where username = ? and password = ?";
            // 获取执行sql对象
            preparedStatement = connection.prepareStatement(sql);
            // 给？赋值
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            // 执行sql
            resultSet = preparedStatement.executeQuery();
            // 判断
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, preparedStatement, connection);
        }

        return false;
    }
}
```

# 第二章 JDBC接口和类

## 2.1 抽取JDBC工具类 JDBCUtils

由于我们JDBC操作重复操作太高，所以我们可以来简化一些书写。

抽取一个方法来获取连接对象

* 需求：不想传递参数，这样太过于麻烦，保证工具类的通用性，能够及时的更改

* 解决：创建一个配置文件 `jdbc.properties`

  ```properties
  url = jdbc:mysql:///day3
  user = root
  password = root
  driver = com.mysql.jdbc.Driver
  ```
  
  抽取JDBC工具类

  ```java
  // JDBC工具类
  public class JDBCUtils {
      private static String url;
      private static String user;
      private static String password;
      private static String driver;
  
      // 文件读取，只需要读取一次就可以拿到这些值。使用静态代码块。
      static {
          
          // 读取资源文件，获取值
          try {
              // 1.创建Properties集合类
              Properties pro = new Properties();
  
              // 获取src路径下面的文件的方式 --> ClassLoader 类加载器 可以加载字节码文件进内存
              ClassLoader classLoader = JDBCUtils.class.getClassLoader();
              URL resource = classLoader.getResource("jdbc.properties");
              String path = resource.getPath();
              // 加载文件
              pro.load(new FileReader(path));
  
              // 3.获取数据，赋值
              url = pro.getProperty("url");
              user = pro.getProperty("user");
              password = pro.getProperty("password");
              driver = pro.getProperty("driver");
  
              Class.forName(driver);
  
          } catch (IOException e) {
              e.printStackTrace();
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          }
      }
  
      // 获取连接
      public static Connection connection() throws SQLException {
          return DriverManager.getConnection(url, user, password);
      }
  
      // 释放资源
      public static void close(Statement statement, Connection connection) {
          if (statement != null) {
              try {
                  statement.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
  
          if (connection != null) {
              try {
                  connection.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
      }
      public static void close(ResultSet resultSet, Statement statement, Connection connection) {
          if (resultSet != null) {
              try {
                  resultSet.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
  
          if (statement != null) {
              try {
                  statement.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
  
          if (connection != null) {
              try {
                  connection.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
      }
  }
  ```
  
  主方法调用

  ```java
  public class Demo01JDBC {
  
      public static void main(String[] args) {
          List<Emp> list = new Demo01TestJDBC().findAll();
          System.out.println(list);
      }
  
  
      public List<Emp> findAll() {
  
          Connection connection = null;
          Statement statement = null;
          ResultSet resultSet = null;
          List<Emp> list = null;
  
          try {
              // 使用JDBCUils类来简化
              // 获取连接
              connection = JDBCUtils.connection();
  
              // 定义SQL语句
              String sql = "select * from emp";
              // 获取执行SQL语句对象
              statement = connection.createStatement();
              // 执行SQL语句
              resultSet = statement.executeQuery(sql);
  
              // 6.遍历结果集，封装对象，装载集合。
              Emp emp = null;
              list = new ArrayList<Emp>();
              while (resultSet.next()) {
                  // 获取数据
                  int id = resultSet.getInt("id");
                  String ename = resultSet.getString("ename");
                  int job_id = resultSet.getInt("job_id");
                  int mgr = resultSet.getInt("mgr");
                  Date joindate = resultSet.getDate("joindate");
                  double salary = resultSet.getDouble("salary");
                  double bonus = resultSet.getDouble("bonus");
                  int dept_id = resultSet.getInt("dept_id");
                  // 创建emp对象，并赋值
                  emp = new Emp();
                  emp.setId(id);
                  emp.setEname(ename);
                  emp.setJob_id(job_id);
                  emp.setMgr(mgr);
                  emp.setJoindate(joindate);
                  emp.setSalary(salary);
                  emp.setBonus(bonus);
                  emp.setDept_id(dept_id);
                  // 装载集合
                  list.add(emp);
              }
          } catch (SQLException throwables) {
              throwables.printStackTrace();
          } finally {
              JDBCUtils.close(resultSet, statement, connection);
          }
          
          return list;
      }
  }
  ```

## 2.2 JDBC实现登陆案例

通过键盘录入用户名称和密码，然后判断用户是否登陆成功。

1. 创建一个表，录入用户名称和密码

   ```sql
   CREATE DATABASE db4;
   
   CREATE TABLE USER (
   	id INT PRIMARY KEY AUTO_INCREMENT,
   	username VARCHAR(20),
   	PASSWORD VARCHAR(20)
   );
   
   INSERT INTO USER VALUE(NULL, "zhangsan", "123");
   INSERT INTO USER VALUE(NULL, "lisi", "456");
   ```

2. 创建JDBC工具类，上面创建好了

3. 主方法

   ```java
   public class Demo01JDBC {
       public static void main(String[] args) {
           // 键盘录入，接收用户名称和密码
           Scanner scanner = new Scanner(System.in);
           System.out.println("请输入用户名称：");
           String username = scanner.nextLine();
           System.out.println("请输入密码");
           String password = scanner.nextLine();
           
           // 调用方法
           boolean flag = longin(username, password);
           
           // 判断结果
           if (flag) {
               System.out.println("成功");
           } else {
               System.out.println("失败");
           }
       }
   
       public static boolean longin(String username, String password) {
           if (username == null || password == null) {
               return false;
           }
   
           Connection connection = null;
           Statement statement = null;
           ResultSet resultSet = null;
   
           try {
               // 获取连接
               connection = JDBCUtils.connection();
               // 定义sql
               String sql = "select * from user where username = '" + username + "' and password = '" + password + "'";
               // 获取执行sql对象
               statement = connection.createStatement();
               // 执行sql
               resultSet = statement.executeQuery(sql);
               // 判断
               return resultSet.next();
           } catch (SQLException e) {
               e.printStackTrace();
           } finally {
               JDBCUtils.close(resultSet, statement, connection);
           }
   
           return false;
       }
   }
   ```


## 2.3 JDBC控制事务

事务：一个包含多个步骤的业务操作。如果这个业务操作被多事务管理，则这个步骤要么同时成功，要么同时失败。

使用`Connection`对象来管理事务

* 开启事务：`setAutoCommit(boolean autoCommit)` 调用该方法设置参数为`FALSE`，即开启事务。在执行SQL之前开启事务

* 提交事务：`commit()`  。当所有的SQL都执行完提交事务

* 回滚事务：`rollback()`。在catch中回滚事务


演示一个转账的操作：

```java
// 转账操作
public class Demo03JDBC {
    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        try {
            // 获取connection对象
            connection = JDBCUtils.connection();

            // 开启事务
            connection.setAutoCommit(false);

            // 定义SQL语句
            // zhangsan减500
            String sql1 = "update account set balance = balance - ? where id = ?";
            // lisi加500
            String sql2 = "update account set balance = balance + ? where id = ?";
            // 获取执行sql对象
            preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement2 = connection.prepareStatement(sql2);
            // 对sql进行赋值操作
            preparedStatement1.setInt(1, 500);
            preparedStatement1.setInt(2, 1);
            preparedStatement2.setInt(1, 500);
            preparedStatement2.setInt(2, 2);

            // 执行sql
            preparedStatement1.executeUpdate();
            
            // 设置异常
            // int i = 3/0;
            // preparedStatement2.executeUpdate();
            
            // 没有问题，提交事务
            connection.commit();
        } catch (Exception e) {
            // 有问题，回滚
            try {
                // 有可能为null
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtils.close(preparedStatement1, connection);
            JDBCUtils.close(preparedStatement2, null);
        }
    }
}
```

# 第三章 数据库连接池

数据库连接池：其实就是一个容器(集合)，存放数据库连接的容器。

当系统初始化好后，容器被创建，容器中会申请一些连接对象，当用户来访问数据库的时候，从容器中，获取连接对象，用户访问完之后，会将连接对象归还给容器。

好处如下：节约资源、让用户访问更加高效。

## 3.1 实现

标准接口：`DataSource`，方法如下：

* 获取连接：`getConnection()`
* 归还连接：`Connection.close()`。如果连接对象`Connection`是从连接池中获取的，那么调用`Connection.close`方法，则不会再关闭连接了，而是归还连接。

一般我们不会来实现它，有数据库厂商来实现

1. `C3P0`：数据库连接池技术
2. `Druid`：数据库连接池实现技术，由阿里巴巴提供

## 3.2 C3P0 数据库连接池技术

步骤

1. 导入jar包，一共有三个

   `mysql-connector-java-5.1.37-bin.jar`（数据库驱动jar包）
`c3p0-0.9.5.2.jar``
   ``mchange-commons-java-0.2.11.jar`

2. 定义配置文件

   名称：`c3p0-config.xml`

   路径：将文件放在src目录下面就可以。

3. 创建核心对象，数据库连接池对象，`ComboPooledDataSource`。

4. 获取连接：`getConnection`

```java
public class Demo01C3p0 {
    public static void main(String[] args) throws SQLException {

        // 1.创建数据库连接池对象
        DataSource ds = new ComboPooledDataSource();
        // 2.获取连接对象
        Connection connection = ds.getConnection();
        // 3.打印
        System.out.println(connection);
    }
}
```

## 3.3 Druid连接池

步骤：

1. 导入jar包，一共有三个

   `druid-1.0.9.jar`

2. 定义配置文件

   名称：是`properties`形式的，名称任意，后缀改为`properties`

   路径：都可以，任意

3. 获取数据库连接池对象：通过工厂类来获取  `DruidDataSourceFactory`

4. 获取连接：`getConnection`

```java
public class Demo01Druid {
    public static void main(String[] args) throws Exception {

        // 导入jar包
        // 定义配置文件
        // 加载配置文件
        Properties pro = new Properties();
        InputStream ras = Demo01Druid.class.getClassLoader().getResourceAsStream("druid.properties");
        pro.load(ras);
        
        // 获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(pro);
        // 获取连接
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
```

## 3.4  Druid 工具类

Druid 定义工具类

```java
// Druid连接池的工具类
public class DruidUtils {

    // 1.定义成员变量 Database
    private static DataSource ds;

    static {
        try {
            // 加载配置文件
            Properties pro = new Properties();
            pro.load(Demo01Druid.class.getClassLoader().getResourceAsStream("druid.properties"));

            // 获取database
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 获取连接
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // 释放资源
    public static void close(Statement statement, Connection connection) {
        close(null, statement, connection);
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取连接池方法
    public static DataSource getDataSource() {
        return ds;
    }
}
```

Druid 测试工具类

```java
public class Demo02Druid {
    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // 获取连接
            connection = DruidUtils.getConnection();
            // 定义sql
            String sql = "insert into account values(null, ?, ?)";
            // 获取pstmt对象
            pstmt = connection.prepareStatement(sql);
            // 给？赋值
            pstmt.setString(1, "王五");
            pstmt.setInt(2, 3000);
            // 执行sql
            int count = pstmt.executeUpdate();
            System.out.println(count);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DruidUtils.close(pstmt, connection);
        }
    }
}
```

# 第四章 JDBCTemplate

`Spring`框架对`JDBC`的简单封装。提供了一个`JDBCTemplate`对象简化`JDBC`的开发

步骤：

1. 导入jar包

2. 创建`JdbcTemplate`对象。依赖于数据源`DataSource`。`JdbcTemplate template = new JdbcTemplate(ds);`

3. 调用`JdbcTemplate`的方法来完成CRUD的操作

   * `update()`：执行DML语句。增、删、改语句
   * `queryForMap()`：查询结果将结果集封装为Map集合。
   * `queryForList()`：查询结果将结果集封装为List集合。
   * `query()`：查询结果，将结果封装为JavaBean对象。
   * `queryForObject`：查询结果，将结果封装为对象。

```java
public class Demo01JdbcTemplate {
    public static void main(String[] args) {

        // 导入jar包
        // 创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(DruidUtils.getDataSource());
        // 调用方法
        String sql = "update account set balance = 2000 where id = ?";
        int count = template.update(sql, 1);
        System.out.println(count);
    }
}
```

## 5.1 练习

1. 修改1号数据的salary为10000
2. 添加一条记录
3. 删除刚才添加的记录

```java
public class Demo02JdbcTemplate {

    // 获取JDBCTemplate对象
    private JdbcTemplate template = new JdbcTemplate(DruidUtils.getDataSource());

    // day3数据库，修改emp表，修改1号数据的salary为10000
    @Test
    public void test1() {
        // 定义sql
        String sql = "update emp set salary = 10000 where id = 1001";
        // 执行sql
        int count = template.update(sql);
        System.out.println(count);
    }

    @Test
    public void test2() {

        String sql = "insert into emp(id, ename, dept_id) values(?, ?, ?)";
        int count = template.update(sql, 1015, "郭靖", 10);
        System.out.println(count);
    }

    @Test
    public void test3() {
        String sql = "delete from emp where id = ?";
        int count = template.update(sql, 1015);
        System.out.println(count);
    }
}
```

```java
    // 获取JDBCTemplate对象
    private JdbcTemplate template = new JdbcTemplate(DruidUtils.getDataSource());
```

4. 查询id为1的记录，将其封装为Map集合。

   ```java
       // 查询id为1001的记录，将其封装为Map集合。
       @Test
       public void test4() {
           String sql = "select * from emp where id = ?";
           Map<String, Object> map = template.queryForMap(sql, 1001);
           System.out.println(map);
       }
   ```

5. 查询所有记录，将其封装为List

   ```java
   // 查询所有记录，将其封装为List
   @Test
   public void test5() {
       String sql = "select * from emp";
       List<Map<String, Object>> list = template.queryForList(sql);
       for (Map<String, Object> stringObjectMap : list) {
           System.out.println(stringObjectMap);
       }
   }
   ```

6. 查询所有记录，将其封装为Emp对象的List集合

   ```java
   // 查询所有记录，将其封装为Emp对象
   @Test
   public void test6() {
       String sql = "select * from emp";
       List<Emp> list = template.query(sql, new RowMapper<Emp>() {
           @Override
           public Emp mapRow(ResultSet resultSet, int i) throws SQLException {
               Emp emp = new Emp();
               int id = resultSet.getInt("id");
               String ename = resultSet.getString("ename");
               int job_id = resultSet.getInt("job_id");
               int mgr = resultSet.getInt("mgr");
               Date joindate = resultSet.getDate("joindate");
               double salary = resultSet.getDouble("salary");
               double bonus = resultSet.getDouble("bonus");
               int dept_id = resultSet.getInt("dept_id");
               // 创建emp对象，并赋值
               emp = new Emp();
               emp.setId(id);
               emp.setEname(ename);
               emp.setJob_id(job_id);
               emp.setMgr(mgr);
               emp.setJoindate(joindate);
               emp.setSalary(salary);
               emp.setBonus(bonus);
               emp.setDept_id(dept_id);
   
               return emp;
           }
       });
   
       for (Emp emp : list) {
           System.out.println(emp);
       }
   }
   ```

   使用下面方法那么有可能错误，是因为需要将类型转换，int->Integer double->Double。

   ```java
   // 查询所有记录，将其封装为Emp对象的List集合
   @Test
   public void test6_2() {
       String sql = "select * from emp";
       List<Emp> list = template.query(sql, new BeanPropertyRowMapper<Emp>(Emp.class));
       for (Emp emp : list) {
           System.out.println(emp);
       }
   }
   ```

7. 查询总记录数。

   ```java
   // 查询总记录数目
   @Test
   public void test7() {
       String sql = "select count(id) from emp";
       Long total = template.queryForObject(sql, long.class);
       System.out.println(total);
   }
   ```

