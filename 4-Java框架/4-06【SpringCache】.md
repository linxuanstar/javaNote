# 第一章 Spring Cache

Spring cache是一个框架，实现了基于注解的缓存功能，只需要简单地加一个注解，就能实现缓存功能。Spring Cache提供了一层抽象，底层可以切换不同的cache实现。具体就是通过CacheManager接口来统一不同的缓存技术。

CacheManager是Spring提供的各种缓存技术抽象接口。针对不同的缓存技术需要实现不同的CacheManager：

| CacheManager        | 描述                               |
| ------------------- | ---------------------------------- |
| EhCacheCacheManager | 使用EhCache作为缓存技术            |
| GuavaCacheManager   | 使用Google的GuavaCache作为缓存技术 |
| RedisCacheManager   | 使用Redis作为缓存技术              |

## 1.1 常用注解

| 注解           | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| @EnableCaching | 开启缓存注解功能                                             |
| @Cacheable     | 方法执行前查看缓存中是否有数据，有数据则返回，无数据则调用方法并将返回值存入缓存 |
| @CachePut      | 将方法的返回值放到缓存中                                     |
| @CacheEvict    | 将一条或多条数据从缓存中删除                                 |

在spring boot项目中，使用缓存技术只需在项目中导入相关缓存技术的依赖包，并在启动类上使用`@EnableCaching`开启缓存支持即可。

例如，使用Redis作为缓存技术，只需要导入Spring data Redis的maven坐标即可。

## 1.2 入门案例

我们先使用最基本的缓存方式来使用一下Spring Cache，Map缓存

1. Maven导入提供的工程，cache_demo，里面的坐标为：

   ```xml
   <dependencies>
       	<!-- 
   			因为我们只是来使用Spring Cahche最基本的功能 所以不用导入多余的包 
   			spring-boot-starter-web 里面依赖着一些基础的jar包
   		-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
               <scope>compile</scope>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>1.18.20</version>
           </dependency>
   
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>fastjson</artifactId>
               <version>1.2.76</version>
           </dependency>
   
           <dependency>
               <groupId>commons-lang</groupId>
               <artifactId>commons-lang</artifactId>
               <version>2.6</version>
           </dependency>
   		
       	<!-- 如果使用Spring Cache Redis作为缓存的话 就要打开注释 -->
   		<!--
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-cache</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-data-redis</artifactId>
           </dependency>
   		-->
   		
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <scope>runtime</scope>
           </dependency>
   
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>3.4.2</version>
           </dependency>
   
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>druid-spring-boot-starter</artifactId>
               <version>1.1.23</version>
           </dependency>
       </dependencies>
   ```

2. 创建数据库：cache_demo，里面只有一张表user表

   ```sql
   -- 创建数据库
   CREATE DATABASE cache_demo;
   -- 使用数据库
   USE cache_demo;
   -- 创建表
   CREATE TABLE USER(
   	id BIGINT PRIMARY KEY NOT NULL,
   	NAME VARCHAR(255),
   	age INT,
   	address VARCHAR(255)
   );
   ```

3. yml文件内容如下：

   ```yml
   server:
     port: 8080
   spring:
     application:
       # 应用的名称，可选
       name: cache_demo
     datasource:
       druid:
         driver-class-name: com.mysql.cj.jdbc.Driver
         url: jdbc:mysql://localhost:3306/cache_demo?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
         username: root
         password: root
   mybatis-plus:
     configuration:
       #在映射实体或者属性时，将数据库中表名和字段名中的下划线去掉，按照驼峰命名法映射
       map-underscore-to-camel-case: true
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
     global-config:
       db-config:
         id-type: ASSIGN_ID
   ```

4. 实体类：

   ```java
   @Data
   public class User implements Serializable {
   
       private static final long serialVersionUID = 1L;
       private Long id;
       private String name;
       private int age;
       private String address;
   }
   ```
   
5. Mapper接口、Service接口、Service接口实现类：

   ```java
   @Mapper
   public interface UserMapper extends BaseMapper<User>{
   }
   ```

   ```java
   public interface UserService extends IService<User> {
   }
   ```

   ```java
   @Service
   public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
   }
   ```

6. Controller层内容如下：

   ```java
   @RestController
   @RequestMapping("/user")
   @Slf4j
   public class UserController {
   
       @Autowired
       private CacheManager cacheManager;
   
       @Autowired
       private UserService userService;
   
       @PostMapping
       public User save(User user){
           userService.save(user);
           return user;
       }
   
       @DeleteMapping("/{id}")
       public void delete(@PathVariable Long id){
           userService.removeById(id);
       }
   
       @PutMapping
       public User update(User user){
           userService.updateById(user);
           return user;
       }
   
       @GetMapping("/{id}")
       public User getById(@PathVariable Long id){
           User user = userService.getById(id);
           return user;
       }
   
       @GetMapping("/list")
       public List<User> list(User user){
           LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.eq(user.getId() != null,User::getId,user.getId());
           queryWrapper.eq(user.getName() != null,User::getName,user.getName());
           List<User> list = userService.list(queryWrapper);
           return list;
       }
   }
   ```

## 1.3 使用CachePut注解

`@CachePut`：将方法的返回值放到缓存中。一般该注解放在新增的方法上面。该注解我们操作的时候需要关注两个属性：value和key，value属性在前、key属性在后。

* value属性：我们所缓存的名称，名称相当于一类的缓存，这一类下面有很多的缓存数据
* key属性：缓存的key。每个用户都会缓存一份数据，并且都不能相同，所以这个key不能够写死，因此需要动态获得。我们可以使用#来获取。例如：#result会获得该方法的返回值、#root.method获得该方法、#[参数名称]获得参数等等。这是SPEL

我们可以来试一下，在User Controller类里面的save方法加一个@CachePut注解：

```java
    @CachePut(value = "userCache", key = "#user.id")
    @PostMapping
    public User save(User user){
        userService.save(user);
        return user;
    }
```

运行该项目，打开PostMan使用Post方式传输，参数弄成Body下面的form-data，数据为name和age。传送两次数据，我们可以在cacheManager看到：

![](..\图片\4-06【SpringCache】\屏幕截图 2022-07-11 164839.png)

这些都是缓存的数据，userCache的缓存的名称是一大类，缓存的键是id，值是user对象。缓存到了ConcurrentHashMap对象里面，这个Map是基于内存的，如果我们的项目关闭，那么缓存的数据也就没有了。

## 1.4 使用CacheEvict 注解

<!-- evict 驱逐; (尤指依法从房屋或土地上)赶出; 逐出; -->

@CacheEvict注解：将一条数据或者多条数据从缓存中删除

```java
// 执行delete的时候 同时删除缓存

@CacheEvict(value = "userCache", key = "#id")
// @CacheEvict(value = "userCache", key = "#p0")
// @CacheEvict(value = "userCache", key = "#root.args[0]")
@DeleteMapping("/{id}")
public void delete(@PathVariable Long id){
    userService.removeById(id);
}
```

```java
// 更新的时候 同时删除缓存

@CacheEvict(value = "userCache", key = "#user.id")
// @CacheEvict(value = "userCache", key = "#p0.id")
// @CacheEvict(value = "userCache", key = "#root.args[0].id")
// @CacheEvict(value = "userCache", key = "#result.id")
@PutMapping
public User update(User user){
    userService.updateById(user);
    return user;
}
```

##  1.5 使用Cacheable注解

`@Cacheable`：在方法执行前Spring先查看缓存中是否有数据，如果有数据，那么直接返回缓存数据，若没有数据，调用方法并将方法返回值放到缓存中。

```java
// 每当方法执行的时候 都会先查看缓存的数据

@Cacheable(value = "userCache", key = "#id")
@GetMapping("/{id}")
public User getById(@PathVariable Long id){
    User user = userService.getById(id);
    return user;
}
```

但是上面的方法也有一个BUG，就是如果我们穿过来的ID不存在，那么方法获取User也就为空。但是Spring会将其缓存至内存中，也就是null。对于这种问题我们也有解决方法：

```java
// condition：满足条件才缓存数据

@Cacheable(value = "userCache", key = "#id", unless = "#result == null")
@GetMapping("/{id}")
public User getById(@PathVariable Long id){
    User user = userService.getById(id);
    return user;
}
```

list方法：

```java
@Cacheable(value = "userCache", key = "#user.id + '_' + #user.name")
@GetMapping("/list")
public List<User> list(User user){
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(user.getId() != null,User::getId,user.getId());
    queryWrapper.eq(user.getName() != null,User::getName,user.getName());
    List<User> list = userService.list(queryWrapper);
    return list;
}
```

## 1.6 使用Redis缓存数据

1. 导入Maven坐标

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-cache</artifactId>
   </dependency>
   
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

2. yml文件添加：

   ```yml
   # Redis相关配置
   spring:
       redis:
           host: localhost
           port: 6379
           database: 0 # 启动默认使用0号数据库
       cache:
           redis:
               time-to-live: 1800000 # 设置缓存过期时间 可以不设置
   ```

3. 启动类上面添加`@EnableCaching`注解
