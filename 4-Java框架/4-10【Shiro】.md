# 第一章 基础概念

Apache Shiro 是一个功能强大且易于使用的 Java 安全 / 权限框架。[Shiro](https://shiro.apache.org/) 可以完成认证、授权、加密、会话管理、与 Web 集成、缓存等。借助 Shiro 可以快速轻松地保护任何应用程序——从最小的移动应用程序到最大的 Web 和企业应用程序。

- 易于使用：使用 Shiro 构建系统安全框架非常简单。就算第一次接触也可以快速掌握。
- 全面：Shiro 包含系统安全框架需要的功能，满足安全需求的“一站式服务”。
- 灵活：Shiro 可以在任何应用程序环境中工作。虽然它可以在 Web、EJB 和 IoC 环境 中工作，但不需要依赖它们。Shiro 也没有强制要求任何规范，甚至没有很多依赖项。
- 强力支持 Web：Shiro 具有出色的 Web 应用程序支持，可以基于应用程序 URL 和 Web 协议（例如 REST）创建灵活的安全策略，同时还提供一组 JSP 库来控制页面输出。
- 兼容性强：Shiro 的设计模式使其易于与其他框架和应用程序集成。Shiro 与 Spring、Grails、Wicket、Tapestry、Mule、Apache Camel、Vaadin 等框架无缝集成。
- 社区支持：Shiro 是 Apache 软件基金会的一个开源项目，有完备的社区支持，文档 支持。如果需要，像 Katasoft 这样的商业公司也会提供专业的支持和服务。

## 1.1 Shiro 与 SpringSecurity

Spring Security 基于 Spring 开发，项目若使用 Spring 作为基础，配合 Spring Security 做权限更加方便，而 Shiro 需要和 Spring 进行整合开发；

Spring Security 功能比 Shiro 更加丰富些，例如安全维护方面；

Spring Security 社区资源相对比 Shiro 更加丰富；

Shiro 的配置和使用比较简单，Spring Security 上手复杂些；

Shiro 依赖性低，不需要任何框架和容器，可以独立运行。Spring Security 依赖 Spring 容器；

shiro 不仅仅可以使用在 web 中，它可以工作在任何应用环境中。在集群会话时 Shiro 最重要的一个好处或许就是它的会话是独立于容器的。

## 1.2 Shiro 功能

<!-- authentication:身份验证； authorization:授权；-->

![](D:\Java\笔记\图片\4-10【Shiro】\1-1.png)

Primary Concerns（主要关注点）

- Authentication：身份认证 / 登录，验证用户是不是拥有相应的身份；
- Authorization：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能进行什么操作。如验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；

- Session Manager：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通 JavaSE 环境，也可以是 Web 环境的； 

- Cryptography：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；


Supporting Features（支持特征）

- Web Support：Web 支持，可以非常容易的集成到 Web 环境； 
- Caching：缓存，比如用户登录后，其用户信息、拥有的角色 / 权限不必每次去查，这样可以提高效率； 

- Concurrency：Shiro 支持多线程应用的并发验证。在一个线程中开启另一个线程，能把权限自动传播过去； 

- Testing：提供测试支持；

- Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；

- Remember Me：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了

## 1.3 Shiro 原理

### 1.3.1 外部观察

从外部来看 Shiro ，即从应用程序角度的来观察如何使用 Shiro 完成工作

![](D:\Java\笔记\图片\4-10【Shiro】\1-2.png)

Subject：应用代码直接交互的对象是 Subject，也就是说 Shiro 的对外 API 核心 就是 Subject。Subject 代表了当前用户，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是 Subject，如网络爬虫、机器人等。与 Subject 的所有交互都会委托给 SecurityManager，Subject 其实是一个门面，SecurityManager 才是实际的执行者；

SecurityManager：安全管理器，所有与安全有关的操作都会与 SecurityManager 交互，它其管理着所有Subject。它是 Shiro 的核心，负责与 Shiro 的其他 组件进行交互，相当于 SpringMVC 中 DispatcherServlet 的角色。

Realm：Shiro 从 Realm 获取安全数据，如用户、角色、权限。SecurityManager 要验证用户身份，那么它需要从 Realm 获取相应的用户进行比较以确定用户身份是否合法，也需要从 Realm 得到用户相应的角色 / 权限进行验证用户是否能进行操作。可以把 Realm 看成 DataSource。

### 1.3.2 内部观察

![](D:\Java\笔记\图片\4-10【Shiro】\1-3.png)

Shiro 架构如下：

* Subject：任何可以与应用交互的“用户”；
* SecurityManager：相当于 SpringMVC 中的 DispatcherServlet，是 Shiro 的心脏。所有具体的交互都通过 SecurityManager 进行控制，它管理着所有 Subject，且负责进行认证、授权、会话及缓存的管理。
* Authenticator：负责 Subject 认证。它是一个扩展点，可以自定义实现，也可以使用认证策略 Authentication Strategy ，即什么情况下算用户认证通过了；
* Authorizer：授权器、即访问控制器，用来决定主体是否有权限进行相应的操作。即控制着用户能访问应用中的哪些功能；
* Realm：可以有 1 个或多个 Realm，可以认为是安全实体数据源，即用于获取安全实体的。可以是 JDBC 实现，也可以是内存实现等等。由用户提供，所以一般在应用中都需要实现自己的 Realm。
* SessionManager：管理 Session 生命周期的组件。Shiro 并不仅仅只可以用在 Web 环境，也可以用在如普通的 JavaSE 环境。
* CacheManager：缓存控制器，来管理如用户、角色、权限等的缓存的。因为这些数据基本上很少改变，放到缓存中后可以提高访问的性能。
* Cryptography：密码模块，Shiro 提高了一些常见的加密组件用于如密码加密 / 解密。

## 1.4 ini 配置文件

ini 文件是 initialization file 的缩写，即初始化文件，是 widows 系统配置文件所采用的存储格式。INI文件由节、参数（键值对）、注解组成。

在windows系统中，INI 文件很多，最重要的是 System.ini、System32.ini 和 Win.ini。ini 文件主要存放用户所作的选择以及系统的各种参数，用户可用通过修改 ini 文件，来改变应用程序和系统的很多配置。

```ini
[Section1] ;section name
keyname1=value1
keyname2=value2

[Section2] ;section name
keyname3=value3
keyname4=value4
```

**节 section**

```ini
[section]
```

- 所有的 section 名称都是独占一行，并且 section 名字都被方括号包围着`[ ]`
- 一个 section 没有明显的结束标识符，一个 section 的开始就是上一个 section 的结束
- section 不能重复，数据通过 section 去查找，每个 seletion 下可以有多个 key 和 value 的键值对
- 所有的键值对都是以节section为单位结合在一起的
- 在 section 声明后的所有 parameters 都属于这个section

**参数 parameter**

每个参数都有一个 name 和一个 value，name 和 value 由等号`=`分隔

```ini
name=value
```

**注解 comments**

comments 使用分号表示，在分号后面的文字，直到该行结尾全部为注解。

```ini
;comment ini文件的数据格式例子
```

# 第二章 登录认证

在shiro中，用户需要提供 principals 和 credentials 给 shiro，从而能验证用户身份。最常见的 principals 和credentials 组合就是用户名 / 密码。

* principals：身份，即主体的标识属性，可以是任何属性，如用户名、邮箱等，唯一即可。一个主体可以有多个principals，但只有一个 Primary principals，一般是用户名/ 邮箱 / 手机号。
* credentials：证明 / 凭证，即只有主体知道的安全值，如密码 / 数字证书等。

登录认证的基本流程如下：

1. 收集用户身份 / 凭证，即如用户名 / 密码。
2. 调用`Subject.login`进行登录，如果失败将得到相应的`AuthenticationException`异常，根据异常提示用户错误信息，否则登录成功。
3. 创建自定义的`Realm`类，继承 `org.apache.shiro.realm.AuthenticatingRealm`类， 实现 `doGetAuthenticationInfo()` 方法

## 2.1 项目搭建

Shiro不依赖容器，直接创建maven工程即可

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-core</artifactId>
        <version>1.10.0</version>
    </dependency>
    <dependency>
        <groupId>commons-logging</groupId>
        <artifactId>commons-logging</artifactId>
        <version>1.2</version>
    </dependency>
</dependencies>
```

Shiro 获取权限相关信息可以通过数据库获取，也可以通过 ini 配置文件获取。这里我们先使用 ini 配置文件来获取，等待框架整合的时候再使用数据库获取。

```ini
# 在项目目录的resources目录下面创建shiro.ini文件
[users]
zhangsan=z3
lisi=l4
```

创建测试类，获取认证对象，进行登录认证：

```java
public class ShiroRun {
    public static void main(String[] args) {
        // 初始化获取 SecurityManager
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        // 获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        // 创建 token 对象
        AuthenticationToken token = new UsernamePasswordToken("zhangsan", "z3");
        // 完成登录
        try {
            subject.login(token);
            System.out.println("登录成功");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户不存在");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误");
        } catch (AuthenticationException ae) {
            // unexpected condition? error?
        }
    }
}
```

## 2.2 登录认证源码

首先调用 `Subject.login(token)` 进行登录，其会自动委托给 SecurityManager 

```java
// Subject接口，有一个DelegatingSubject实现类
public interface Subject {
    void login(AuthenticationToken var1) throws AuthenticationException;
}
```

```java
public class DelegatingSubject implements Subject {
    public void login(AuthenticationToken token) throws AuthenticationException {
        this.clearRunAsIdentitiesInternal();
        // 委托给 SecurityManager 处理
        Subject subject = this.securityManager.login(this, token);
    }
}
```

SecurityManager 负责真正的身份验证逻辑；它会委托给 Authenticator 进行身份验证；

```java
// SecurityManager接口
public interface SecurityManager extends Authenticator, Authorizer, SessionManager {
    Subject login(Subject var1, AuthenticationToken var2) throws AuthenticationException;
}
```

```java
// SessionsSecurityManager实现了SecurityManager，所以DefaultSecurityManager也是，可以重写login方法
public class DefaultSecurityManager extends SessionsSecurityManager {
    public Subject login(Subject subject, AuthenticationToken token) throws AuthenticationException {
        AuthenticationInfo info;
        try {
            // 委托给 Authenticator 进行身份验证
            info = this.authenticate(token);
        }
    }
}
```

Authenticator 才是真正的身份验证者，Shiro API 中核心的身份认证入口点，此处可以自定义插入自己的实现；

Authenticator 可能会委托给相应的 AuthenticationStrategy 进行多 Realm 身份验证，默认 ModularRealmAuthenticator 会调用 AuthenticationStrategy 进行多 Realm 身份验证；

```java
public abstract class AuthenticatingSecurityManager extends RealmSecurityManager {
    public AuthenticationInfo authenticate(AuthenticationToken token) throws AuthenticationException {
        return this.authenticator.authenticate(token);
    }
}
```

```java
// 该接口一共由6个实现类
public interface Authenticator {
    // 选择AbstractAuthenticator抽象类对其的实现方法
    AuthenticationInfo authenticate(AuthenticationToken var1) throws AuthenticationException;
}
```

```java
public abstract class AbstractAuthenticator implements Authenticator, LogoutAware {
    public final AuthenticationInfo authenticate(AuthenticationToken token) throws AuthenticationException {
        if (token == null) {
            throw new IllegalArgumentException("Method argument (authentication token) cannot be null.");
        } else {
            log.trace("Authentication attempt received for token [{}]", token);

            AuthenticationInfo info;
            try {
                // 调用该类的doAuthenticate抽象方法
                info = this.doAuthenticate(token);
            }
        }
    }
    // ModularRealmAuthenticator类实现了该方法
    protected abstract AuthenticationInfo doAuthenticate(AuthenticationToken var1) throws AuthenticationException;
}
```

```java
public class ModularRealmAuthenticator extends AbstractAuthenticator {
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        this.assertRealmsConfigured();
        Collection<Realm> realms = this.getRealms();
        // 获取到1个Realm对象，走第一个结果
        return realms.size() == 1 ? this.doSingleRealmAuthentication((Realm)realms.iterator().next(), authenticationToken) : this.doMultiRealmAuthentication(realms, authenticationToken);
    }

    protected AuthenticationInfo doSingleRealmAuthentication(Realm realm, AuthenticationToken token) {
        if (!realm.supports(token)) {
            String msg = "Realm [" + realm + "] does not support authentication token [" + token + "].  Please ensure that the appropriate Realm implementation is configured correctly or that the realm accepts AuthenticationTokens of this type.";
            throw new UnsupportedTokenException(msg);
        } else {
            AuthenticationInfo info = realm.getAuthenticationInfo(token);
        }
    }
}
```

```java
// 这就是如果可以创建自定义的Realm类，继承AuthenticatingRealm类， 实现doGetAuthenticationInfo()方法
public abstract class AuthenticatingRealm extends CachingRealm implements Initializable {
    public final AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthenticationInfo info = this.getCachedAuthenticationInfo(token);
        if (info == null) {
            info = this.doGetAuthenticationInfo(token);
        }
    }
}
```

## 2.3 角色及授权

授权，也叫访问控制，即在应用中控制谁访问哪些资源，如访问页面 / 编辑数据 / 页面操作等。在授权中需了解的几个关键对象：主体 Subject、资源 Resource、权限 Permission、角色 Role。

Shiro 支持粗粒度权限（如用户模块的所有权限）和细粒度权限（操作某个用户的权 限， 即实例级别的）

* 主体 Subject：访问应用的用户，在 Shiro 中使用 Subject 代表该用户。用户只有授权后才允许访问相应的资源。
* 资源 Resource：在应用中用户可以访问的 URL，比如访问 JSP 页面、查看 / 编辑某些数据、访问某个业务方法、打印文本等等都是资源。用户只有授权之后才能访问。
* 权限 Permission：安全策略中的原子授权单位，通过权限我们可以表示在应用中用户有没有操作某个资源的权力。即权限表示在应用中用户能不能访问某个资源，例如访问用户列表页面查看 / 新增 / 修改 / 删除用户数据等。权限代表了用户有没有操作某个资源的权利，即反映在某个资源上的操作允不允许。
* 角色 Role：权限的集合，一般情况下会赋予用户角色而不是权限，即这样用户可以拥有一组权限，赋予权限时比较方便。例如项目经理、技术总监、CTO、开发工程师等都是角色，不同的角色拥有一组不同的权限。

授权的方式也有很多种，比如编程式、注解式、JSP/GSP 标签。

```java
// 编程式：通过写if/else 授权代码块完成
if (subject.hasRole("admin")) {
    // 有权限
} else {
    // 无权限
}
```

```java
// 注解式：通过在执行的Java方法上放置相应的注解完成，没有权限将抛出相应的异常
@RequiresRoles("admin")
public void hello() {
    // 有权限
}
```

```jsp
<!-- JSP/GSP 标签：在JSP/GSP 页面通过相应的标签完成 -->
<shiro:hasRole name="admin">
    <!-- 有权限 -->
</shiro:hasRole>
```

授权的流程如下：

1. 首先调用 Subject.isPermitted / hasRole接口，其会委托给 SecurityManager，而 SecurityManager 接着会委托给 Authorizer；
2. Authorizer 是真正的授权者，如果调用如`isPermitted(“user:view”)`，其首先会通过 PermissionResolver 把字符串转换成相应的 Permission 实例；
3. 在进行授权之前，其会调用相应的 Realm 获取 Subject 相应的角色 / 权限用于匹配传入的角色 / 权限；
4. Authorizer 会判断 Realm 的角色 / 权限是否和传入的匹配，如果有多个 Realm，会委托给ModularRealmAuthorizer 进行循环判断，如果匹配如isPermitted / hasRole 会返回 true，否则返回 false 表示授权失败

接下来实际操作一下：

```ini
; 给shiro.ini增加角色配置
[users]
zhangsan=z3,role1,role2
lisi=l4
```

```java
try {
    subject.login(token);
    System.out.println("登录成功");
    boolean hasRole = subject.hasRole("role1");
    System.out.println("是否拥有此角色 = " + hasRole);
}
```

判断权限信息

```ini
; 给shiro.ini增加角色配置
[users]
zhangsan=z3,role1,role2
lisi=l4

[roles]
role1=user:insert,user:select
```

```java
try {
    subject.login(token);
    System.out.println("登录成功");
    boolean hasRole = subject.hasRole("role1");
    System.out.println("是否拥有此角色 = " + hasRole);
    // 判断是否拥有此权限
    boolean isPermitted = subject.isPermitted("user:insert");
    System.out.println("是否拥有此权限：" + isPermitted);
    // 也可以用 checkPermission 方法，但没有返回值，没权限抛 AuthenticationException
    subject.checkPermission("user:select");
}
```

## 2.4 Shiro 加密工具

系统开发中，一些敏感信息需要进行加密，比如说用户的密码。Shiro 内嵌了常用的加密算法，比如 MD5 加密。

```java
public static void main(String[] args) {
    // 密码明文
    String password = "z3";
    // 使用 md5 加密
    Md5Hash md5Hash = new Md5Hash(password);
    // 打印会调用toString方法，Md5Hash里面调用toString方法会调用toHex方法
    System.out.println("md5加密：" + md5Hash);
    System.out.println("md5加密：" + md5Hash.toHex());
    // 带盐的 md5 加密，盐就是在密码明文后拼接新字符串，然后再进行加密
    Md5Hash md5Hash2 = new Md5Hash(password, "salt");
    System.out.println("md5 带盐加密：" + md5Hash2.toHex());
    // 为了保证安全，避免被破解还可以多次迭代加密，保证数据安全
    Md5Hash md5Hash3 = new Md5Hash(password, "salt", 3);
    System.out.println("md5 带盐三次加密：" + md5Hash3.toHex());
    // 使用父类实现加密，最后加密结果和上面加密结果一样
    SimpleHash simpleHash = new SimpleHash("MD5", password, "salt", 3);
    System.out.println("父类带盐三次加密：" + simpleHash.toHex());
}
```

## 2.5 自定义登录认证

Shiro 默认的登录认证是不带加密的，如果想要实现加密认证需要自定义登录认证， 自定义 Realm。

```java
public class MyRealm extends AuthenticatingRealm {

    /**
     * 自定义的登录认证方法，Shiro 的 login 方法底层会调用该类的认证方法完成登录认证
     * 需要配置自定义的 realm 生效，在 ini 文件中配置，或 Springboot 中配置
     * 该方法只是获取进行对比的信息，认证逻辑还是按照 Shiro 的底层认证逻辑完成认证
     */
    protected AuthenticationInfo doGetAuthenticationInfo (
            AuthenticationToken authenticationToken) throws
            AuthenticationException {
        // 获取身份信息，也就是传的token对象
        String principal = authenticationToken.getPrincipal().toString();
        // 获取凭证信息
        String password = new String((char[]) authenticationToken.getCredentials());
        //
        System.out.println("认证用户信息：" + principal + "---" + password);
        // 获取数据库中存储的用户信息
        if (principal.equals("zhangsan")) {
            // 从数据库存储的加盐迭代 3 次密码
            String pwdInfo = "7174f64b13022acd3c56e2781e098a5f";
            // 创建封装了校验逻辑的对象，将要比较的数据给该对象
            AuthenticationInfo info = new SimpleAuthenticationInfo(
                    authenticationToken.getPrincipal(),
                    pwdInfo,
                    ByteSource.Util.bytes("salt"),
                    authenticationToken.getPrincipal().toString());
            return info;
        }
        return null;
    }
}
```

```ini
[main]
md5CredentialsMatcher=org.apache.shiro.authc.credential.Md5CredentialsMatcher
md5CredentialsMatcher.hashIterations=3

myrealm=com.linxuan.main.MyRealm
myrealm.credentialsMatcher=$md5CredentialsMatcher
securityManager.realms=$myrealm

[users]
zhangsan=7174f64b13022acd3c56e2781e098a5f,role1,role2
lisi=l4

[roles]
role1=user:insert,user:select
```

```java
public class ShiroRun {
    public static void main(String[] args) {
        // 初始化获取 SecurityManager
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        // 获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        // 创建 token 对象
        AuthenticationToken token = new UsernamePasswordToken("zhangsan", "z3");
        // 完成登录
        try {
            subject.login(token);
            System.out.println("登录成功");
            boolean hasRole = subject.hasRole("role1");
            System.out.println("是否拥有此角色 = " + hasRole);
            // 判断是否拥有此权限
            boolean isPermitted = subject.isPermitted("user:insert");
            System.out.println("是否拥有此权限：" + isPermitted);
            // 也可以用 checkPermission 方法，但没有返回值，没权限抛 AuthenticationException
            subject.checkPermission("user:select");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户不存在");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误");
        } catch (AuthenticationException ae) {
            // unexpected condition? error?
        }
    }
}
```

# 第三章 SpringBoot 整合

```xml
<!-- SpringBoot起步依赖，这些版本有点古老了 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.1.RELEASE</version>
</parent>

<dependencies>
    <!-- Shiro整合SpringBoot起步依赖 -->
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-spring-boot-web-starter</artifactId>
        <version>1.9.0</version>
    </dependency>

    <!-- mybatis-plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.0.5</version>
    </dependency>
    <!-- mysql驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.46</version>
    </dependency>
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <!--
        Thymeleaf是一款用于渲染XML/XHTML/HTML5内容的模板引擎。[taim li:f]
        类似JSP，Velocity，FreeMaker等，它也可以轻易的与Spring MVC等Web框架进行集成作为Web应用的模板引擎。
        与其它模板引擎相比，Thymeleaf特点是能够直接在浏览器中打开并正确显示模板页面，不需要启动整个Web应用。
    -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
</dependencies>
```

```yml
spring:
  # 配置数据源信息
  datasource:
    # 数据源类型，HikariDataSource是springBoot自带的数据源管理工具
    type: com.zaxxer.hikari.HikariDataSource
    # MySQL58版本之后使用com.mysql.cj.jdbc.Driver
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/linxuan?characterEncoding=utf8&useSSL=false
    username: root
    password: root
  jackson:
    # 返回日期格式
    date-format: yyyy-MM-dd HH:mm:ss
    # 指定时区为东八区
    time-zone: GMT+8
mybatis-plus:
  configuration:
    # 配置日志输出
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath:mapper/*.xml
shiro:
  loginUrl: /myController/login
```

```java
/**
 * SpringBoot项目启动类
 */
@SpringBootApplication
public class ShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiroApplication.class, args);
    }
}
```

```sql
-- 数据库操作
-- 如果不存在linxuan数据库那么就创建
CREATE DATABASE IF NOT EXISTS linxuan CHARACTER SET utf8;
-- 使用linxuan数据库
USE linxuan;

-- 关闭外键约束
SET FOREIGN_KEY_CHECKS=0;
-- 如果存在该表那么就删除
DROP TABLE IF EXISTS user;
-- 如果不存在tb_book表那么就创建
CREATE TABLE IF NOT EXISTS user(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(30) DEFAULT NULL COMMENT '用户名',
    `pwd` VARCHAR(50) DEFAULT NULL COMMENT '密码',
    `rid` BIGINT(20) DEFAULT NULL COMMENT '角色编号',
    PRIMARY KEY (`id`)
);
-- 删除当前表，并创建一个字段相同的新表。用来清除当前表的所有数据
TRUNCATE TABLE user;

-- 插入数据
INSERT INTO `user`(`id`, `name`, `pwd`) VALUES 
	(1, '张三', '7174f64b13022acd3c56e2781e098a5f'), 
	(2, '李四', '7174f64b13022acd3c56e2781e098a5f');

# 外键约束置为1
SET FOREIGN_KEY_CHECKS=0;
```

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String pwd;
    private Integer rid;
}
```

## 3.1 登录认证实现

```java
@Mapper
public interface UserDao extends BaseMapper<User> {
}
```

```java
public interface UserService extends IService<User> {

    /**
     * 根据名称查询用户信息
     * @param name 用户名称
     * @return 返回用户信息
     */
    User getUserInfoByName(String name);
}
```

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据用户名称返回用户信息
     *
     * @param name 用户名称
     * @return 返回用户信息
     */
    @Override
    public User getUserInfoByName(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        User user = userDao.selectOne(wrapper);
        return user;

    }
}
```

```java
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 自定义授权方法
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 自定义登录认证方法
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1 获取用户身份信息
        String name = token.getPrincipal().toString();
        // 2 调用业务层获取用户信息（数据库中）
        User user = userService.getUserInfoByName(name);
        // 3 判断并将数据完成封装
        if (user != null) {
            AuthenticationInfo info = new SimpleAuthenticationInfo(
                    token.getPrincipal(),
                    user.getPwd(),
                    ByteSource.Util.bytes("salt"),
                    token.getPrincipal().toString()
            );
            return info;
        }
        return null;
    }
}
```

```java
@Configuration
public class ShiroConfig {

    @Autowired
    private MyRealm myRealm;


    /**
     * 配置 SecurityManager
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        // 创建 defaultWebSecurityManager 对象
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 创建加密对象，并设置相关属性
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 采用 md5 加密
        matcher.setHashAlgorithmName("md5");
        // 迭代加密次数
        matcher.setHashIterations(3);
        // 将加密对象存储到 myRealm 中
        myRealm.setCredentialsMatcher(matcher);
        // 将 myRealm 存入 defaultWebSecurityManager 对象
        defaultWebSecurityManager.setRealm(myRealm);
        // 返回
        return defaultWebSecurityManager;
    }

    // 配置 Shiro 内置过滤器拦截范围
    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new
                DefaultShiroFilterChainDefinition();
        // 设置不认证可以访问的资源
        definition.addPathDefinition("/myController/userLogin", "anon");
        definition.addPathDefinition("/login", "anon");
        // 设置需要进行登录认证的拦截范围
        definition.addPathDefinition("/**", "authc");
        return definition;
    }
}
```

```java
/**
 * 需要注意：这里并不能够使用RestController注解，要不然会无限重定向
 */
@Slf4j
@Controller
@RequestMapping("myController")
public class MyController {

    @ResponseBody
    @GetMapping("userLogin")
    public String userLogin(String name, String pwd) {
        log.info("访问了");
        // 获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        // 封装请求数据到 token 对象中
        AuthenticationToken token = new UsernamePasswordToken(name, pwd);
        // 3 调用 login 方法进行登录认证
        try {
            subject.login(token);
            return "登录成功";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("登录失败");
            return "登录失败";
        }

    }
}
```

## 3.2 实现前端页面

```xml
<!--
    [taim li:f] 是一款用于渲染XML/XHTML/HTML5内容的模板引擎。
    类似JSP，Velocity，FreeMaker等，它也可以轻易的与Spring MVC等Web框架进行集成作为Web应用的模板引擎。
    Thymeleaf最大的特点是能够直接在浏览器中打开并正确显示模板页面，而不需要启动整个Web应用。
-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

```html
<!-- 在Springboot项目下面templates文件夹下面创建login.html -->
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
        <h1>Shiro 登录认证</h1>
        <br>
        <form action="/myController/userLogin">
            <div>用户名：<input type="text" name="name" value=""></div>
            <div>密码：<input type="password" name="pwd" value=""></div>
            <div><input type="submit" value="登录"></div>
        </form>
    </body>
</html>
```

```html
<!-- 在Springboot项目下面templates文件夹下面创建main.html -->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
        <h1>Shiro 登录认证后主页面</h1>
        <br>
        <!-- 从session中获取登录用户，因此登录成功之后需要存储至Session中 -->
        登录用户为：<span th:text="${session.user}"></span>
    </body>
</html>
```

```java
/**
 * 需要注意：这里并不能够使用RestController注解，要不然会无限重定向
 * 现在要其返回的是html页面，所以更不能使用RestController注解了
 */
@Slf4j
@Controller
@RequestMapping("myController")
public class MyController {

    /**
     * 访问/myController/login直接跳转至login.html
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
     * @param name    前端输入的名称
     * @param pwd     用户密码
     * @param session 需要向Session中存储登录后的用户信息
     * @return 返回登录成功后的页面user.html
     */
    @GetMapping("userLogin")
    public String userLogin(String name, String pwd, HttpSession session) {
        log.info("访问了");
        // 获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        // 封装请求数据到 token 对象中
        AuthenticationToken token = new UsernamePasswordToken(name, pwd);
        // 调用 login 方法进行登录认证
        try {
            subject.login(token);
            session.setAttribute("user", token.getPrincipal().toString());
            return "main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("登录失败");
            return "登录失败";
        }
    }
}
```

```yml
shiro:
  loginUrl: /myController/login
```

```java
// 配置 Shiro 内置过滤器拦截范围
@Bean
public DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition definition = new
        DefaultShiroFilterChainDefinition();
    // 设置不认证可以访问的资源
    definition.addPathDefinition("/myController/userLogin", "anon");
    // 可以直接访问登录页面
    definition.addPathDefinition("/login", "anon");
    // 设置需要进行登录认证的拦截范围
    definition.addPathDefinition("/**", "authc");
    return definition;
}
```

## 3.3 多个 realm 的认证策略设置 
