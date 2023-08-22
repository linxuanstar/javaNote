# 第一章 快速入门

Spring Security 是 Spring 家族中的一个安全管理框架。相比与另外一个安全框架 Shiro，它提供了更丰富的功能，社区资源也比 Shiro 更加地丰富。

一般来说中大型的项目都是使用 SpringSecurity 来做安全框架。小项目使用 Shiro 的比较多，因为相比与SpringSecurity，Shiro的上手更加的简单。

一般Web应用的需要进行认证和授权，而认证和授权也是 SpringSecurity 作为安全框架的核心功能。

- 认证：验证当前访问系统的是不是本系统的用户，并且要确认具体是哪个用户
- 授权：经过认证后判断当前用户是否有权限进行某个操作

## 1.1 基础项目搭建

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.0</version>
</parent>
<dependencies>
    <!-- web项目依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- security依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- Lombok依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <!-- 可选依赖是隐藏当前工程所依赖的资源，隐藏后对应资源将不具有依赖传递 默认为false-->
        <optional>true</optional>
    </dependency>
</dependencies>
```

```java
package com.linxuan;

@SpringBootApplication
public class SecurityQuickStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityQuickStartApplication.class, args);
    }
}
```

```java
@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
```

启动上面的项目，访问 http://localhost:8080/hello，会自动重定向至登录页面 http://localhost:8080/login，该登录页面是SpringSecure 自带的，只有登录之后才能够正常访问接口。用户名默认为`user`。密码会在控制台给出。

登录之后也可以访问 http://localhost:8080/logout 退出

## 1.2 登陆校验流程

![](..\图片\5-09【SpringSecurity】\1-1Login check process.png)

## 1.3 Security原理

SpringSecurity 的原理其实就是一个过滤器链，内部包含了提供各种功能的过滤器。这里我们可以看看入门案例中的过滤器，图中只展示了核心过滤器，其它的非核心过滤器并没有在图中展示。

- `UsernamePasswordAuthenticationFilter`：负责处理我们在登陆页面填写了用户名密码后的登陆请求。入门案例的认证工作主要有它负责。
- `ExceptionTranslationFilter`：处理过滤器链中抛出的 AccessDeniedException 和 AuthenticationException 
- `FilterSecurityInterceptor`：负责权限校验的过滤器。

![](..\图片\5-09【SpringSecurity】\1-2Filter Chain.png)

我们可以通过 Debug 查看当前系统中 SpringSecurity 过滤器链中有哪些过滤器及它们的顺序。在 SpringBoot 项目执行 main 方法中的代码会返回 `ConfigurableApplicationContext`对象，该对象就是一个 Spring 容器，该容器里面注入了许多的 bean 对象。

我们只需要将其返回获取到该容器，然后 Debug 查询一下该容器内部名为 DefaultSecurityFilterChain 的 bean 即可

```java
@SpringBootApplication
public class SecurityQuickStartApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SecurityQuickStartApplication.class, args);
        // Debug该行，随便打印一点东西，让Debug停留在上一行，这样能够获取Spring容器并查看bean
        System.out.println(111);
    }
}
```

![](..\图片\5-09【SpringSecurity】\1-3SpringSecurity Filter Chain.png)

## 1.4 认证流程详解

一些基础的概念：

- Authentication 接口：它的实现类表示当前访问系统的用户，封装了用户的权限等相关信息。

- AuthenticationManager 接口：定义了认证 Authentication 的方法 ，实现类是 `ProviderManager`。`ProviderManager` 的功能主要是实现认证用户，因为在写登录接口时，可以通过配置类的方式，注入Spring容器中来使用它的`authenticate`方法

- UserDetailsService 接口：加载用户特定数据的核心接口，里面定义了一个根据用户名查询用户信息的方法。这里需要自定义实现类，因为原本的实现类是`InMemoryUserDetailsManager`，它是在内存中查询，所以我们需要自定义该接口的实现类。

- UserDetails 接口：提供核心用户信息。通过 UserDetailsService 根据用户名获取处理的用户信息要封装成UserDetails 对象返回。然后将这些信息封装到 Authentication 对象中。当我们自定义 UserDetailsService 接口时，需要我们定义一个实体类来实现这个接口来供 UserDetailsService 接口返回。

`UsernamePasswordAuthenticationFilter`这个过滤器来实现认证过程逻辑的。实际上不是它这一个类就实现了，它还通过其他类来帮助他实现的，下图就是该过滤器内部实现大致流程。

![](..\图片\5-09【SpringSecurity】\1-4Certification process.png)

大致流程如下：

1. 当前端提交用户名和密码过来时，进入了`UsernamePasswordAuthenticationFilter`过滤器。
2. 在`UsernamePasswordAuthenticationFilter`过滤器里，将传进来的用户名和密码被封装成了`Authentication`对象，这时候最多只有用户名和密码，权限还没有。
3. `Authentication`对象通过`ProviderManager`的`authenticate`方法进行认证。
4. 在`ProviderManager`里面，通过调用`DaoAuthenticationProvider`的`authenticate`方法进行认证。
5. 在`DaoAuthenticationProvider`里，调用`InMemoryUserDetailsManager`的`loadUserByUsername`方法查询用户，传入的参数只有用户名字符串。在`InMemoryUserDetailsManager`的`loadUserByUsername`方法里执行了以下操作：
   1. 根据用户名查询对应得用户以及这个用户的权限信息，这里实在内存里面查询。
   2. 把对应的用户信息包括权限信息封装成`UserDetails`对象。
6. 返回`UserDetails`对象。
7. 返回给了`DaoAuthenticationProvider`，在这个对象通过`PasswordEncoder`对比`UserDetails`中的密码和`Authentication`密码是否正确。
8. 如果正确就把`UserDetails`的权限信息设置到`Authentication`对象中。
9. 返回`Authentication`对象，这样就又回到了过滤器里面`UsernamePasswordAuthenticationFilter`。
10. 如果上一步返回了`Authentication`对象，就使用`SecurityContextHolder.getContext().setAuthentication()`方法存储对象。其他过滤器会通过`SecurityContextHolder`来获取当前用户信息。当前过滤器认证完了，后面的过滤器还需要获取用户信息，比如授权过滤器。

## 1.5 自定义实现认证

登录：

1. 自定义接口：调用ProviderManager的方法进行认证，如果认证通过则生成jwt；把用户信息存入redis中
2. 自定义UserDetailsService：在这个实现类中去查询数据库

校验：

* 定义 Jwt 认证过滤器：获取token；解析token获取其中的userid；从redis中获取用户信息；存入SecurityContextHolder。

### 1.5.1 基础环境搭建

导入坐标：

~~~~xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.0</version>
</parent>
<dependencies>
    <!-- web项目依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- security依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- Lombok依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <!-- 可选依赖是隐藏当前工程所依赖的资源，隐藏后对应资源将不具有依赖传递 默认为false-->
        <optional>true</optional>
    </dependency>
    <!-- redis依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!-- fastjson依赖 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <!-- SpringBoot并没有收录，因此需要自己设定版本 -->
        <version>1.2.33</version>
    </dependency>
    <!-- jwt依赖 -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <!-- SpringBoot并没有收录，因此需要自己设定版本 -->
        <version>0.9.0</version>
    </dependency>
</dependencies>
~~~~

添加 redis 相关配置：

```java
package com.linxuan.utils;

/**
 * 自定义一个Redis序列化器，实现RedisSerializer接口，内部使用FastJson序列化
 * @param <T> 参数泛型化
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Class<T> clazz;

    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 序列化方法
     * @param t 序列化对象类型
     * @return 返回字节数组
     * @throws SerializationException 有可能抛出异常
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        // 使用fastjson进行序列化
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 反序列化方法
     * @param bytes 字节数组
     * @return 返回发序列化后的对象
     * @throws SerializationException 反序列化途中抛出的异常
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        // 使用fastjson反序列化
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz);
    }

    /**
     * 获取字节码类型
     * @param clazz 传入的字节码class对象
     * @return 返回Java中的类型
     */
    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
```

```java
package com.linxuan.config;

/**
 * 创建Redis配置类，设置一下序列化器
 */
@Configuration
public class RedisConfig {

    /**
     * 设置RedisTemplate中序列化与反序列化操作的序列化器
     * 1.注解之@Bean：将该方法的返回值制作为SpringIOC容器管理的一个Bean对象
     * 2.注解之@SuppressWarnings：告诉编译器忽略指定的警告，不用在编译完成后出现警告信息
     *
     * @param connectionFactory 参数
     * @return 返回RedisTemplate的bean对象并注入到SpringIOC容器中
     */
    @Bean
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 默认的Key序列化器为：JdkSerializationRedisSerializer
        // 因为默认都是JDK序列化器，但是这样向redis里面存储会添加一些前缀，所以需要设置为String序列化器
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置值的序列化与反序列化器为我们自定义的FastJsonRedisSerializer
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
```

创建返回结果类

```java
package com.linxuan.common;

/**
 * 创建返回结果类，用户向前端返回包装后的数据
 *  注解之@JsonInclude(JsonInclude.Include.NON_NULL)：如果字段值为null那么不序列化
 *
 * @param <T> 参数泛型化
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    // 状态码
    private Integer code;
    // 提示信息，如果有错误时，前端可以获取该字段进行提示
    private String msg;
    // 查询到的结果数据
    private T data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
}
```

创建一些基本的工具类：

④工具类

~~~~java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 60 * 60 *1000L;// 60 * 60 *1000  一个小时
    //设置秘钥明文
    public static final String JWT_KEY = "sangeng";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }
    
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("sg")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2ZDVhZi1mNjVlLTQ0MDAtYjcxMi0zYWEwOGIyOTIwYjQiLCJzdWIiOiJzZyIsImlzcyI6InNnIiwiaWF0IjoxNjM4MTA2NzEyLCJleHAiOjE2MzgxMTAzMTJ9.JVsSbkP94wuczb4QryQbAke3ysBDIL5ou8fWsbt_ebg";
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}
~~~~

~~~~java
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除Hash中的数据
     * 
     * @param key
     * @param hkey
     */
    public void delCacheMapValue(final String key, final String hkey)
    {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hkey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
~~~~

~~~~java
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
~~~~

⑤实体类

~~~~java
import java.io.Serializable;
import java.util.Date;


/**
 * 用户表(User)实体类
 *
 * @author 三更
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    
    /**
    * 主键
    */
    private Long id;
    /**
    * 用户名
    */
    private String userName;
    /**
    * 昵称
    */
    private String nickName;
    /**
    * 密码
    */
    private String password;
    /**
    * 账号状态（0正常 1停用）
    */
    private String status;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 手机号
    */
    private String phonenumber;
    /**
    * 用户性别（0男，1女，2未知）
    */
    private String sex;
    /**
    * 头像
    */
    private String avatar;
    /**
    * 用户类型（0管理员，1普通用户）
    */
    private String userType;
    /**
    * 创建人的用户id
    */
    private Long createBy;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新人
    */
    private Long updateBy;
    /**
    * 更新时间
    */
    private Date updateTime;
    /**
    * 删除标志（0代表未删除，1代表已删除）
    */
    private Integer delFlag;
}
~~~~



#### 2.3.3 实现

##### 2.3.3.1 数据库校验用户

​	从之前的分析我们可以知道，我们可以自定义一个UserDetailsService,让SpringSecurity使用我们的UserDetailsService。我们自己的UserDetailsService可以从数据库中查询用户名和密码。

###### 准备工作

​	我们先创建一个用户表， 建表语句如下：

~~~~mysql
CREATE TABLE `sys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `status` CHAR(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` VARCHAR(64) DEFAULT NULL COMMENT '邮箱',
  `phonenumber` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  `sex` CHAR(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` VARCHAR(128) DEFAULT NULL COMMENT '头像',
  `user_type` CHAR(1) NOT NULL DEFAULT '1' COMMENT '用户类型（0管理员，1普通用户）',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` INT(11) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表'
~~~~

​		引入MybatisPuls和mysql驱动的依赖

~~~~xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.3</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
~~~~

​		配置数据库信息

~~~~yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sg_security?characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
~~~~

​		定义Mapper接口

~~~~java
public interface UserMapper extends BaseMapper<User> {
}
~~~~

​		修改User实体类

~~~~java
类名上加@TableName(value = "sys_user") ,id字段上加 @TableId
~~~~

​		配置Mapper扫描

~~~~java
@SpringBootApplication
@MapperScan("com.sangeng.mapper")
public class SimpleSecurityApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SimpleSecurityApplication.class);
        System.out.println(run);
    }
}
~~~~

​		添加junit依赖

~~~~java
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
~~~~

​	   测试MP是否能正常使用

~~~~java
/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
}
~~~~



###### 核心代码实现

创建一个类实现UserDetailsService接口，重写其中的方法。更加用户名从数据库中查询用户信息

~~~~java
/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        
        //封装成UserDetails对象返回 
        return new LoginUser(user);
    }
}
~~~~

因为UserDetailsService方法的返回值是UserDetails类型，所以需要定义一个类，实现该接口，把用户信息封装在其中。

```java
/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

注意：如果要测试，需要往用户表中写入用户数据，并且如果你想让用户的密码是明文存储，需要在密码前加{noop}。例如

![image-20211216123945882](D:\Bprogram\SpringSecurity\SpringSecurity\img\image-20211216123945882.png)

这样登陆的时候就可以用sg作为用户名，1234作为密码来登陆了。



##### 2.3.3.2 密码加密存储

​	实际项目中我们不会把密码明文存储在数据库中。

​	默认使用的PasswordEncoder要求数据库中的密码格式为：{id}password 。它会根据id去判断密码的加密方式。但是我们一般不会采用这种方式。所以就需要替换PasswordEncoder。

​	我们一般使用SpringSecurity为我们提供的BCryptPasswordEncoder。

​	我们只需要使用把BCryptPasswordEncoder对象注入Spring容器中，SpringSecurity就会使用该PasswordEncoder来进行密码校验。

​	我们可以定义一个SpringSecurity的配置类，SpringSecurity要求这个配置类要继承WebSecurityConfigurerAdapter。

~~~~java
/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
~~~~

##### 2.3.3.3 登陆接口

​	接下我们需要自定义登陆接口，然后让SpringSecurity对这个接口放行,让用户访问这个接口的时候不用登录也能访问。

​	在接口中我们通过AuthenticationManager的authenticate方法来进行用户认证,所以需要在SecurityConfig中配置把AuthenticationManager注入容器。

​	认证成功的话要生成一个jwt，放入响应中返回。并且为了让用户下回请求时能通过jwt识别出具体的是哪个用户，我们需要把用户信息存入redis，可以把用户id作为key。

~~~~java
@RestController
public class LoginController {

    @Autowired
    private LoginServcie loginServcie;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginServcie.login(user);
    }
}

~~~~

~~~~java
/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
~~~~

​	

~~~~java
@Service
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }
}

~~~~



##### 2.3.3.4 认证过滤器

​	我们需要自定义一个过滤器，这个过滤器会去获取请求头中的token，对token进行解析取出其中的userid。

​	使用userid去redis中获取对应的LoginUser对象。

​	然后封装Authentication对象存入SecurityContextHolder



~~~~java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
~~~~

~~~~java
/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //把token校验过滤器添加到过滤器链中
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

~~~~



##### 2.3.3.5 退出登陆

​	我们只需要定义一个登陆接口，然后获取SecurityContextHolder中的认证信息，删除redis中对应的数据即可。

~~~~java
/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Service
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"退出成功");
    }
}
~~~~







## 认证流程详解

`UsernamePasswordAuthenticationFilter`这个过滤器来实现认证过程逻辑的。实际上不是它这一个类就实现了，它还通过其他类来帮助他实现的，下图就是该过滤器内部实现大致流程。

[![image-20211214151515385](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5Cimage-20211214151515385.png)](https://tianjiashu.github.io/image\article\Spring-Security登录认证\image-20211214151515385.png)

过程详解：

当前端提交用户名和密码过来时，进入了`UsernamePasswordAuthenticationFilter`过滤器。

- 在`UsernamePasswordAuthenticationFilter`过滤器里，将传进来的用户名和密码被封装成了**`Authentication`**对象【这时候最多只有用户名和密码，权限还没有】，**`Authentication`**对象通过**`ProviderManager`的`authenticate`方法**进行认证。

  - 在**`ProviderManager`**里面，通过调用`DaoAuthenticationProvider`的`authenticate`方法进行认证。

    - 在`DaoAuthenticationProvider`里，调用**`InMemoryUserDetailsManager`的`loadUserByUsername`方法**查询用户。【传入的参数只有**用户名字符串**】

      - 在

        **`InMemoryUserDetailsManager`的`loadUserByUsername`方法**

        里执行了以下操作

        1. 根据用户名查询对于用户以及这个用户的权限信息【**在内存里查**】
        2. 把对应的用户信息包括权限信息封装成**`UserDetails`对象**。
        3. 返回**`UserDetails`对象**。

    - 返回给了`DaoAuthenticationProvider`，在这个对象里执行了以下操作

      1. 通过**`PasswordEncoder`**对比**`UserDetails`**中的密码和**`Authentication`**密码是否正确。【**校验密码（经过加密的）**】
      2. 如果正确就把**`UserDetails`**的**权限信息**设置到**`Authentication`**对象中。
      3. 返回**`Authentication`**对象。

- 又回到了过滤器里面`UsernamePasswordAuthenticationFilter`。

  1. 如果上一步返回了**`Authentication`**对象

     就使用**`SecurityContextHolder.getContext().setAuthentication()`**方法存储对象。

     **其他过滤器**会通过`SecurityContextHolder`来获取当前用户信息。【当前过滤器认证完了，后面的过滤器还需要获取用户信息，比如授权过滤器】

> 彩色字体的类均是比较重要的**接口**，在实现认证的过程中均需要自定义一个类来重新实现或者变更为Spring中其他实现类。

概念速查：

- **`Authentication`**接口: 它的实现类，表示当前访问系统的用户，**封装了用户的权限等相关信息。**
- **`AuthenticationManager`**接口：定义了认证Authentication的方法 ,实现类是**`ProviderManager`**
  - 它的实现类是**`ProviderManager`**，它的功能主要是实现**认证用户**，因为在写登录接口时，可以通过配置类的方式，注入Spring容器中来使用它的**`authenticate`方法**。
- **`UserDetailsService`**接口：加载用户特定数据的核心接口。里面定义了一个**根据用户名查询用户信息的方法**。
  - 原本的实现类是**`InMemoryUserDetailsManager`**，它是在内存中查询，因为我们需要自定义改接口。
- **`UserDetails`**接口：提供核心用户信息。通过**`UserDetailsService`**根据用户名获取处理的用户信息要封装成**`UserDetails`**对象返回。然后将这些信息封装到**`Authentication`**对象中。
  - 当我们自定义**`UserDetailsService`**接口时，需要我们定义一个**实体类**来实现这个接口来供**`UserDetailsService`**接口返回。【注意是实体类】

# 实现登录认证

## 思路分析

**登录**

 ①自定义登录接口 [![IMG_0414(20220818-140941)](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5CIMG_0414(20220818-140941).PNG)](https://tianjiashu.github.io/image\article\Spring-Security登录认证\IMG_0414(20220818-140941).PNG)

 调用`ProviderManager`的方法进行认证 如果认证通过生成`jwt`

 把用户信息存入`redis`中【`userId`作为Key，用户信息作为Value】

 ②自定义`UserDetailsService`

 【因为原本这个**接口的实现类**是**在内存中查询用户信息**，不符合我们的要求，所以需要我们自己去实现它来自定义】

 在这个实现类中去查询数据库

**校验**：【校验的话，需要我们自己去**自定义一个过滤器**】

![IMG_0415(20220818-141117)](https://tianjiashu.github.io/post/image/article/Spring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81/IMG_0415(20220818-141117).PNG)

 ①定义`Jwt`认证过滤器

 获取token

 解析token获取其中的`userid`

 从`redis`中获取用户信息【如果每次请求都查询数据库就很浪费时间】

 存入`SecurityContextHolder`

## 准备工作

### 添加依赖

- `spring-boot-starter-security`

- `JWT`

- `spring-boot-starter-data-redis`

- `fastjson`

- `mybatis-plus`

- `mysql-connector-java`

- `lombok`

  对于`JWT`，由于版本原因还需要引入以下maven坐标。

  ```
  <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
  </dependency>
  ```

### 配置文件【`数据库`和`redis`】

```
spring:
  datasource:
    url: jdbc:mysql://192.168.37.131:3306/Security?characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  application:
    name: spring-redis
  redis:
    host: 192.168.37.131
    port: 6379   #默认端口号
    password: 123456
    database: 0 #默认提供了16个数据库（可以在配置文件中改） 默认操作0号数据库，可以在命令行 select 1 选择1号数据库，
    jedis:
      #Redis连接池配置
      pool:
        max-active: 8 #最大链接数
        max-wait: 1ms #连接池最大阻塞等待时间
        max-idle: 4   #连接池的最大空闲连接
        min-idle: 0   #连接池的最小空闲连接
```

### 添加配置类

`Redis`的配置类

> Key序列化为String,Value序列化为json

```
/**
 * Redis使用FastJson序列化
 * 
 * @author sg
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T>
{

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Class<T> clazz;
    static
    {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public FastJsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }
    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz);
    }
    protected JavaType getJavaType(Class<?> clazz)
    {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
@Configuration
public class RedisConfig {

    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
```

使用`Mybatis-plus`，要在启动类上加上**@MapperScan**注解，来配置Mapper扫描

```
@SpringBootApplication
@MapperScan("com.example.Mapper")
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

}
```

### 响应类

```
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private T data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
```

### 工具类

`JWt`的工具类还有`Redis`的工具类。【针对于工具类，我觉得有必要总结一个博客，以后开发肯定是常用的。代码太长就不贴了】

`Redis`工具类使用**@Component**注解来注入到Spring容器中。

### 实体类

```
//用户表(User)实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user") 
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    @TableId
    private Long id;//主键
  
    private String userName;//用户名
    
    private String nickName;//昵称
    
    private String password;//密码
    
    private String status;//账号状态（0正常 1停用）
    
    private String email;// 邮箱
    
    private String phonenumber;//手机号
    
    private String sex;//用户性别（0男，1女，2未知）
    
    private String avatar;//头像
    
    private String userType;//用户类型（0管理员，1普通用户）
    
    private Long createBy;//创建人的用户id
 
    private Date createTime;//创建时间
   
    private Long updateBy;//更新人
    
    private Date updateTime;//更新时间
    
    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）
}
```

### 创建一个用户表

建表语句如下：

```
CREATE TABLE `sys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `status` CHAR(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` VARCHAR(64) DEFAULT NULL COMMENT '邮箱',
  `phonenumber` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  `sex` CHAR(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` VARCHAR(128) DEFAULT NULL COMMENT '头像',
  `user_type` CHAR(1) NOT NULL DEFAULT '1' COMMENT '用户类型（0管理员，1普通用户）',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` INT(11) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表'
```

OK准备工作就完成了….

## 核心代码实现

### 数据库校验用户

首先你要写好`UserMapper`接口，来实现用户查询。

我们要创建一个类`UserDetailsServiceImpl`来实现**`UserDetailsService`**接口，来让它实现在数据库里面查询，因为它原本的实现类是查询内存的。【在Service包中】

这个接口要使用**@Service**注解，注入到Spring容器中。

重写**`loadUserByUsername`**方法，传入了`Username`参数

1. 首先要根据传入的`Username`参数，查询数据库

2. 如果没有这个用户`Objects.isNull(user)`，就抛出异常

3. 根据用户查询权限信息

4. 添加到**`UserDetails`接口的实现类**中

   1. 在domain包中创建类`LoginUser`，实现`UserDetails`接口。

   2. 完整代码如下

      ```
      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public class LoginUser implements UserDetails {
      
          private User user;
      
          @Override
          public Collection<? extends GrantedAuthority> getAuthorities() {
              return null;
          }
      
          @Override
          public String getPassword() {return user.getPassword();}
      
          @Override
          public String getUsername() {return user.getUserName();}
      
          @Override
          public boolean isAccountNonExpired() {return true;}
      
          @Override
          public boolean isAccountNonLocked() {return true;}
      
          @Override
          public boolean isCredentialsNonExpired() {return true;}
      
          @Override
          public boolean isEnabled() {return true;}
      }
      ```

`UserDetailsServiceImpl`实现类如下

```
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据传入了Passward查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = service.getOne(queryWrapper);
        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //TODO 查询对应的权限


        return new LoginUser(user);
    }
}
```

注意：如果要测试，需要往用户表中写入用户数据，并且如果你想让用户的密码是明文存储，需要在密码前加{noop}。例如

[![image-20211216123945882](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5Cimage-20211216123945882.png)](https://tianjiashu.github.io/image\article\Spring-Security登录认证\image-20211216123945882.png)

这样登陆的时候就可以用sg作为用户名，1234作为密码来登陆了。

#### 密码加密存储

![image-20220819003356284](https://tianjiashu.github.io/post/image/article/Spring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81/image-20220819003356284.png)

实际项目中我们不会把密码明文存储在数据库中。

 默认使用的`PasswordEncoder`要求数据库中的密码格式为：`{id}password `。它会根据id去判断密码的加密方式。但是我们一般不会采用这种方式。所以就需要替换`PasswordEncoder`。

 我们一般使用`SpringSecurity`为我们提供的`BCryptPasswordEncoder`。

 我们只需要使用把`BCryptPasswordEncoder`对象注入Spring容器中，`SpringSecurity`就会使用该`PasswordEncoder`来进行密码校验。

 我们可以定义一个`SpringSecurity`的配置类，`SpringSecurity`要求这个配置类要继承`WebSecurityConfigurerAdapter`。

```
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
```

配置之后，我们要将数据库`sys_user`表的用户密码，从 **1234**,改为加密之后的。

[![image-20220819003102757](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5Cimage-20220819003102757.png)](https://tianjiashu.github.io/image\article\Spring-Security登录认证\image-20220819003102757.png)

当我们进行注册时，要将密码进行加密，我们可以将`PasswordEncoder`注入进Controller里。下面我们进行测试演示。

```
@SpringBootTest
class Demo1ApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        String encode = passwordEncoder.encode("1234");
        System.out.println(encode);//$2a$10$OHOzZsC9RMCqJdWWpBzgfOfQZlEVedDXrUqHhp3HSINu4HghI59kq
		/*
			加密后的信息是动态变化的，因为我们要使用，matches()来进行比较。
		*/
        boolean matches = passwordEncoder.matches("1234", encode);
        System.out.println(matches);//true

        boolean matches2 = passwordEncoder.matches("12345", encode);
        System.out.println(matches2);//false
    }

}
```

### 登录接口

![image-20220819004454194](https://tianjiashu.github.io/post/image/article/Spring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81/image-20220819004454194.png)

接下我们需要自定义登陆接口。

1. 放行

   登录接口需要**让`SpringSecurity`对这个接口放行**【不通过过滤器链】,让用户访问这个接口的时候不用登录也能访问。

   ```
   @Configuration
   public class SecurityConfig extends WebSecurityConfigurerAdapter{
   
       //...注入BCryptPasswordEncoder....
   
       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http
                   //关闭csrf
                   .csrf().disable()
                   //不通过Session获取SecurityContext
                   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   .and()
                   .authorizeRequests()
                   // 对于登录接口 允许匿名访问
                   .antMatchers("/user/login").anonymous()//允许匿名用户访问,不允许已登入用户访问
                   // 除上面外的所有请求全部需要鉴权认证
                   .anyRequest().authenticated();
       }
   }
   ```

2. 接口中的认证

   - 在接口中我们通过**`AuthenticationManager`的`authenticate`**方法来进行用户认证。

   - 所以需要在`SecurityConfig`中配置把`AuthenticationManager`注入容器。

     ```
     @Configuration
     public class SecurityConfig extends WebSecurityConfigurerAdapter{
        //...注入BCryptPasswordEncoder....
     
         @Bean
         @Override
         public AuthenticationManager authenticationManagerBean() throws Exception {
             return super.authenticationManagerBean();
         }
     
        //配置放行....
     }
     ```

**流程：**

定义一个`Controller`，不多说了~

```
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult<Map<String,String>> login(@RequestBody User user){
        String userName = user.getUserName();
        String password = user.getPassword();
        return loginService.login(userName,password);
    }
}
```

`loginService.login();`的核心流程：

注入**`AuthenticationManager`和`RedisCache`**

- 调用**`AuthenticationManager`的`authenticate`**方法来进行用户认证。返回**`Authentication`**

  - 使用`authenticate`方法，需要传入**`Authentication`**，但**`Authentication`**是接口，因此需要去找它的实现类。这里我们使用它的实现类是`UsernamePasswordAuthenticationToken`。

    > **传入的`Authentication`只有用户名和密码**：
    >
    > - `principal` 属性为用户名
    > - `credentials` 属性为密码

  - 使用`authenticate`方法，返回的**`Authentication`**。

    > **如果不为空的话，传出的`Authentication`：**
    >
    > `Principal`属性是`Userdetails`
    >
    > `credentials` 属性为null

    ![image-20220819010124847](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5Cimage-20220819010124847.png)

- 如果**`Authentication`**为NULL，说明认证没通过，要么没查询到这个用户，要么密码比对不通过。然后就**抛出异常**。

- 如果认证通过，获取`UserId`，`JwtUtil`要将`UserId`加密成一个`toekn`。

- 将用户信息**`Authentication`**，存入`redis`。

完整代码：

```
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult<Map<String,String>> login(String userName, String password) {
        //调用`AuthenticationManager`的方法进行认证
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        // 如果认证通过生成token
            //获取userid
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
            //生成token
        String token = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",token);
        return new ResponseResult<Map<String,String>>(200,"登陆成功",map);
    }
}
```

### 认证过滤器

> 为什么要写这么一个过滤器？
>
> ![image-20211214144425527](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5Cimage-20211214144425527.png)
>
> `SpringSecurity`自带的过滤器中是用来认证用户名和密码的但我们并没有使用它，在配置的时候就去掉了。之前的登录接口我们生成了一个token，当前端访问后端的时候需要携带这个token。而这个过滤器就是认证token的。

自定义一个**过滤器**

1. 获取请求头中的`token`

   1. 如果获取的`token`字符串为空，说明前端访问后端就没有携带token。然后**放行，return**

      > **为什么是放行而不是抛异常呢？**
      >
      > 因为没有携带token，有可能前端是想要登录，因此不能抛异常。
      >
      > 就算是要访问其他资源，我们直接放行，**`Authentication`**对象没有用户任何信息，后面的过滤器也会抛出异常。后面也不会进行认证。

2. 使用`JwtUtil`对`token`进行解析取出其中的`userid`。

   如果`token`解析失败，说明前端携带的token不合法，就会抛出异常。

3. 使用`userid`去`redis`中获取对应的`LoginUser`对象。

4. 然后封装**`Authentication`**对象存入`SecurityContextHolder`。

   在封装**`Authentication`**时，使用的实现类是`UsernamePasswordAuthenticationToken`，使用的构造方法是：

   ```
   public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
       private static final long serialVersionUID = 530L;
       private final Object principal;
       private Object credentials;
   
       //.....
   
       public UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
           super(authorities);
           this.principal = principal;
           this.credentials = credentials;
           super.setAuthenticated(true);//标志为已认证状态，这样就不用再让`UsernamePasswordAuthenticationFilter`过滤器再进行认证了。
       }
   
      //.....
   }
   ```

代码：

```
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //首先需要获取token
        String token = httpServletRequest.getHeader("token");
        //判断token是否为Null
        if(!StringUtils.hasText(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //如何不为空，解析token，获得了UserId
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("toen非法");
        }
        //根据UserId查redis获取用户数据
        String key = "login:"+userId;
        LoginUser LoginUser = redisCache.getCacheObject(key);
        if(Objects.isNull(LoginUser)){
            throw new RuntimeException("用户未登录");
        }
        //然后封装Authentication对象存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(LoginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
```

> **注意：**
>
> 该过滤器实现的接口并不是之前的`Filter`，而是去继承`OncePerRequestFilter`。
>
> `OncePerRequestFilter`是Spring Boot里面的一个过滤器**抽象类**，这个过滤器抽象类通常被用于继承实现并在每次请求时**只执行一次过滤**。**他能够确保在一次请求只通过一次filter，而不需要重复执行**
>
> 而`Servlet`的`Filter`可能会执行多次。

然后我们将过滤器加到`UsernamePasswordAuthenticationFilter`的前面，在配置类中进行配置。

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
}
```

### 退出登录

定义一个登陆接口，然后获取`SecurityContextHolder`中的认证信息，删除`redis`中对应的数据即可。

**Controller层**

```
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginService loginService;
    
    @GetMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}
```

**Service层**

```
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

 
    @Override
    public ResponseResult logout() {
        //获取Authentication
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser Loginuser = (LoginUser) authentication.getPrincipal();
        User user = Loginuser.getUser();
        String userId = user.getId().toString();
        //清除cache
        redisCache.deleteObject("login:"+userId);
        return new ResponseResult(200,"注销成功");
    }
}
```

# 总结

是不是感觉有点乱，让我们缕一缕。先说一下我们**对哪些接口进行了实现，或者是更改**

- 我们自定义了**`UserDetailsService`**接口，来实现数据库查询。当中用到了**`UserDetails`接口的实现类——`LoginUser`**。

- 在**`UserDetailsService`**接口是上一层面，我们需要对密码进行解密解析并对比。因为我们使用了**`PasswordEncoder`接口的其他实现类`BCryptPasswordEncoder`**。

- 在实现登录接口的时候

  - 需要**`AuthenticationManager`的`authenticate`方法**进行认证。

  - 传入**`Authentication`**接口的实现类是`UsernamePasswordAuthenticationToken`。构造方法如下

    ```
    public UsernamePasswordAuthenticationToken(Object principal, Object credentials)
    ```

  - 这表示该**`Authentication`**是**未认证的**。之后会通过`UsernamePasswordAuthenticationFilter`过滤器来认证。

- 在实现认证过滤器时，

  - 需要使用`SecurityContextHolder.getContext().setAuthentication()`方法，将用户信息**`Authentication`**存进去。方便其他Filter使用。

  - 传入**`Authentication`**接口的实现类是`UsernamePasswordAuthenticationToken`。构造方法是

    ```
    public UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
    ```

  - 这表示该**`Authentication`**是**认证的**。之后就不会通过`UsernamePasswordAuthenticationFilter`过滤器来认证。

## 图例演示

1. 当我们登录时

   ![IMG_0418(20220819-175440)](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5CIMG_0418(20220819-175440)-16609042254092.PNG)

2. 登录之后，前端访问其他资源

   - 携带token

     ![IMG_0421(20220819-181345)](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5CIMG_0421(20220819-181345).PNG)

     > 通过DEBUG发现，如果携带token。
     >
     > - context域中存入**已认证的Authentication**，就不会访问`UserDetailsServiceImpl`。
     > - context域中存入**未认证的Authentication**，就会访问`UserDetailsServiceImpl`，来进行认证。
     >
     > 至于为什么，我也不清楚….(￣ ‘i ￣;)

   - 未携带token

     ![image-20220819181740017](https://tianjiashu.github.io/image%5Carticle%5CSpring-Security%E7%99%BB%E5%BD%95%E8%AE%A4%E8%AF%81%5Cimage-20220819181740017.png)

# 补充说明

在我学习这个登录认证的过程中，我一直有一个疑惑，就是感觉这个SpringSecurity自带的认证处理器好像没有”干活”的样子。

它的作用仅仅是“借用了”它内部的东西（方法）来进行认证。

到了三更老师讲到其他认证方案时，我才明白，当我们使用配置类时，就**去掉了`UsernamePasswordAuthenticationFilter`**。

因为

```
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }
}
```

 看一下这个`super.configure(http);`这个父类的方法，它进行了**默认的配置**

```
protected void configure(HttpSecurity http) throws Exception {
    this.logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
    ((HttpSecurity)((HttpSecurity)((AuthorizedUrl)http.authorizeRequests().anyRequest())
                    .authenticated().and())
     				.formLogin().and())		//formLogin()......
    				.httpBasic();
}
```

查看这个`formLogin()`方法：

```
public FormLoginConfigurer<HttpSecurity> formLogin() throws Exception {
    return (FormLoginConfigurer)this.getOrApply(new FormLoginConfigurer());
}
```

再查看`FormLoginConfigurer()`这个方法。

```
public FormLoginConfigurer() {
    super(new UsernamePasswordAuthenticationFilter(), (String)null);
    this.usernameParameter("username");
    this.passwordParameter("password");
}
```

我们可以看到，它添加了`UsernamePasswordAuthenticationFilter()`这个过滤器。

但是我们配置的时候，去掉了`super.configure(http);`，也就是说不使用**默认配置**了

那也就是说，不添加了`UsernamePasswordAuthenticationFilter()`这个过滤器。所以我们根本没使用这个过滤器。