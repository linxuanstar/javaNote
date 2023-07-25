# 第一章 Feign远程调用

先来看我们以前利用`RestTemplate`发起远程调用的代码：

```java
public Order queryOrderById(Long orderId) {
    // 1.查询订单
    Order order = orderMapper.findById(orderId);
    // 2.根据查询到的订单里面的userId来调用查询user
    String url = "http://userservice/user/" + order.getUserId();
    User user = restTemplate.getForObject(url, User.class);
    // 3.user信息存入order里面
    order.setUser(user);
    // 4.返回
    return order;
}
```

可以发现第二步是存在一些问题的，存在下面的问题：

* 代码可读性差，编程体验不统一
* 参数复杂URL难以维护

我们可以使用Feigh。Feign是一个声明式的http客户端，官方地址：https://github.com/OpenFeign/feign。其作用就是帮助我们优雅的实现http请求的发送，解决上面提到的问题。

## 1.1 Feign替代RestTemplate

Fegin的使用步骤如下：

1. 引入依赖

   我们在order-service服务的`pom.xml`文件中引入feign的依赖：

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

2. 添加注解

   在order-service的启动类添加注解开启Feign的功能：

   ```java
   @MapperScan("com.linxuan.order.mapper")
   @SpringBootApplication
   @EnableFeignClients // 添加注解开启Feign的功能
   public class OrderApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(OrderApplication.class, args);
       }
   
       @Bean
       @LoadBalanced // 负载均衡注解
       public RestTemplate restTemplate() {
           return new RestTemplate();
       }
   }
   ```

3. 编写Feign的客户端

   在order-service中新建一个接口，内容如下：

   ```java
   package com.linxuan.order.client;
   
   import com.linxuan.order.pojo.User;
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   
   @FeignClient("userservice")
   public interface UserClient {
       @GetMapping("/user/{id}")
       User findById(@PathVariable("id") Long id);
   }
   ```

   这个客户端主要是基于SpringMVC的注解来声明远程调用的信息，比如：

   - 服务名称：userservice

   - 请求方式：GET

   - 请求路径：/user/{id}

   - 请求参数：Long id

   - 返回值类型：User

   这样，Feign就可以帮助我们发送http请求，无需自己使用RestTemplate来发送了。

4. 测试

   修改order-service中的OrderService类中的queryOrderById方法，使用Feign客户端代替RestTemplate：

   ```java
   @Service
   public class OrderService {
   
       @Autowired
       private OrderMapper orderMapper;
   
       @Autowired
       private UserClient userClient;
   
       public Order queryOrderById(Long orderId) {
           // 1.查询订单
           Order order = orderMapper.findById(orderId);
           // 2.根据查询到的订单里面的userId来调用查询user
           // 利用Feign发起http请求，查询用户
           User user = userClient.findById(order.getUserId());
           // 3.user信息存入order里面
           order.setUser(user);
           // 4.返回
           return order;
       }
   }
   ```

   修改好代码之后，启动order-service和user-service服务，然后浏览器输入http://localhost:8080/order/103，看看访问是否成功。

   > 有可能我们启动失败，提示Nacos没有启动，但是这时候我们去启动Nacos的话该启动哪一个？我们之前已经将nacos的本地地址修改了，所以这里要修改回来，修改成localhost:8848，然后启动最开始的Nacos。

总结，使用Feign的步骤：

① 引入依赖

② 添加`@EnableFeignClients`注解

③ 编写`FeignClient`接口

④ 使用`FeignClient`中定义的方法代替`RestTemplate`

## 1.2 自定义配置

Feign可以支持很多的自定义配置，如下表所示：

| 类型                | 作用             | 说明                                                   |
| ------------------- | ---------------- | ------------------------------------------------------ |
| feign.Logger.Level  | 修改日志级别     | 包含四种不同的级别：NONE、BASIC、HEADERS、FULL         |
| feign.codec.Decoder | 响应结果的解析器 | http远程调用的结果做解析，例如解析json字符串为java对象 |
| feign.codec.Encoder | 请求参数编码     | 将请求参数编码，便于通过http请求发送                   |
| feign. Contract     | 支持的注解格式   | 默认是SpringMVC的注解                                  |
| feign. Retryer      | 失败重试机制     | 请求失败的重试机制，默认是没有，不过会使用Ribbon的重试 |

一般情况下，默认值就能满足我们使用，如果要自定义时，只需要创建自定义的@Bean覆盖默认Bean即可。

下面以日志为例来演示如何自定义配置，首先来介绍一下四种日志级别：

- NONE：不记录任何日志信息，这是默认值。
- BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
- HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
- FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据。

一共有两种方式自定义配置日志，配置文件方式和Java代码方式：

1. 配置文件方式

   基于配置文件修改feign的日志级别可以针对单个服务：

   ```yml
   feign:  
     client:
       config: 
         userservice: # 针对某个微服务的配置
           loggerLevel: FULL #  日志级别 
   ```

   也可以针对所有服务：

   ```yaml
   feign:  
     client:
       config: 
         default: # 这里用default就是全局配置，如果是写服务名称，则是针对某个微服务的配置
           loggerLevel: FULL #  日志级别 
   ```


2. Java代码方式

   也可以基于Java代码来修改日志级别，先声明一个类，然后声明一个Logger.Level的对象：

   ```java
   public class DefaultFeignConfiguration  {
       @Bean
       public Logger.Level feignLogLevel(){
           return Logger.Level.BASIC; // 日志级别为BASIC
       }
   }
   ```

   如果要**全局生效**，将其放到启动类的`@EnableFeignClients`这个注解中：

   ```java
   @EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration .class) 
   ```

   如果是**局部生效**，则把它放到对应的`@FeignClient`这个注解中：

   ```java
   @FeignClient(value = "userservice", configuration = DefaultFeignConfiguration .class) 
   ```

## 1.3 Feign使用优化

Feign使用优化有两个：①日志级别尽量用basic，②使用HttpClient或OKHttp代替URLConnection。日志级别的设置上面已经介绍了，接下来介绍一下使用连接池代替默认的URLConnection。

Feign底层发起http请求，依赖于其它的框架。其底层客户端实现包括：

- URLConnection：默认实现，不支持连接池

- Apache HttpClient ：支持连接池

- OKHttp：支持连接池


因此提高Feign的性能主要手段就是使用**连接池**代替默认的URLConnection。

这里我们用Apache的HttpClient来演示。

1. 引入依赖 

   在order-service的pom文件中引入Apache的HttpClient依赖：

   ```xml
   <!--httpClient的依赖 -->
   <dependency>
       <groupId>io.github.openfeign</groupId>
       <artifactId>feign-httpclient</artifactId>
   </dependency>
   ```

2. 配置连接池

   在order-service的application.yml中添加配置：

   ```yaml
   feign:
     client:
       config:
         default: # default全局的配置
           loggerLevel: BASIC # 日志级别，BASIC就是基本的请求和响应信息
     httpclient:
       enabled: true # 开启feign对HttpClient的支持
       max-connections: 200 # 最大的连接数
       max-connections-per-route: 50 # 每个路径的最大连接数
   ```


## 1.4 最佳实践

所谓最近实践，就是使用过程中总结的经验，最好的一种使用方式。

仔细观察可以发现，Feign的客户端与服务提供者的controller代码非常相似：

feign客户端，在order-service中：

```java
@FeignClient("userservice")
public interface UserClient {

    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}

```

UserController，在user-service中：

```java
    /**
     * 路径： /user/110
     * 端口号是8081 id最小为1
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/user/{id}")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }
```

上述的代码重复度有点高，所以我们需要简化一下

### 继承方式

一样的代码可以通过继承来共享：

1. 定义一个API接口，利用定义方法，并基于SpringMVC注解做声明。

2. Feign客户端和Controller都集成改接口

   ![](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714190640857.png)

优点：简单；实现了代码共享

缺点：服务提供方、服务消费方紧耦合；参数列表中的注解映射并不会继承，因此Controller中必须再次声明方法、参数列表、注解。

### 抽取方式

将Feign的Client抽取为独立模块，并且把接口有关的POJO、默认的Feign配置都放到这个模块中，提供给所有消费者使用。

例如，将UserClient、User、Feign的默认配置都抽取到一个feign-api包中，所有微服务引用该依赖包，即可直接使用。

![](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714214041796.png)

### 基于抽取的最佳实践

1. 抽取

   首先创建一个module，命名为feign-api。在feign-api中然后引入feign的starter依赖：

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

   然后，order-service中编写的UserClient、User、DefaultFeignConfiguration都复制到feign-api项目中

2. 在order-service中使用feign-api

   首先，删除order-service中的UserClient、User、DefaultFeignConfiguration等类或接口。

   在order-service的pom文件中中引入feign-api的依赖：

   ```xml
   <dependency>
       <groupId>com.linxuan.demo</groupId>
       <artifactId>feign-api</artifactId>
       <version>1.0</version>
   </dependency>
   ```

   修改order-service中的所有与上述三个组件有关的导包部分，改成导入feign-api中的包。当然feign-api中导入的UserClient接口的导包部分也要修改。

3. 重启测试

   重启后，发现服务报错了：

   ![image-20210714205623048](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714205623048.png)

   这是因为UserClient现在在com.linxuan.feign.clients包下，而order-service的`@EnableFeignClients`注解是在com.linxuan.order包下，不在同一个包，无法扫描到UserClient。

   无法为其创建对象，没有在Spring容器中管理。所以导入注入失败。

4. 解决扫描包问题

   方式一：指定Feign应该扫描的包：

   ```java
   @EnableFeignClients(basePackages = "com.linxuan.feign.clients")
   ```

   方式二：指定需要加载的Client接口：

   ```java
   @EnableFeignClients(clients = {UserClient.class})
   ```


# 第二章 Gateway服务网关

Spring Cloud Gateway 是 Spring Cloud 的一个全新项目，该项目是基于 Spring 5.0，Spring Boot 2.0 和 Project Reactor 等响应式编程和事件流技术开发的网关，它旨在为微服务架构提供一种简单有效的统一的 API 路由管理方式。

Gateway网关是我们服务的守门神，所有微服务的统一入口。

网关的**核心功能特性**：权限控制、请求路由、 限流。

- **权限控制**：网关为微服务入口，需要校验用户是是否有请求资格，如果没有则进行拦截。
- **路由和负载均衡**：一切请求都必须先经过gateway，但网关不处理业务，而是根据某种规则，把请求转发到某个微服务，这个过程叫做路由。当然路由的目标服务有多个时，还需要做负载均衡。
- **限流**：当请求流量过高时，在网关中按照下流的微服务能够接受的速度来放行请求，避免服务压力过大。

![](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714210131152.png)

在SpringCloud中网关的实现包括两种：gateway和zuul。

Zuul是基于Servlet的实现，属于阻塞式编程。而SpringCloudGateway则是基于Spring5中提供的WebFlux，属于响应式编程的实现，具备更好的性能。

## 2.1 gateway快速入门

下面，我们就演示下网关的基本路由功能。基本步骤如下：

1. 创建gateway服务，引入依赖

   创建一个gateway模块，微服务。引入依赖：

   ```xml
   <!--网关-->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-gateway</artifactId>
   </dependency>
   <!--nacos服务发现依赖-->
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```

2. 编写启动类

   ```java
   package com.linxuan.gateway;
   
   // 启动类
   @SpringBootApplication
   public class GatewayApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(GatewayApplication.class, args);
       }
   }
   ```

3. 编写基础配置和路由规则

   创建application.yml文件，内容如下：

   ```yml
   server:
     port: 10010 # 网关端口
   spring:
     application:
       name: gateway # 服务名称
     cloud:
       nacos:
         server-addr: localhost:8848 # nacos地址
       gateway:
         routes: # 网关路由配置
           - id: user-service # 路由id，自定义，只要唯一即可
             # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址	
             uri: lb://userservice # 路由的目标地址 lb就是负载均衡，后面跟服务名称
             predicates: # 路由断言，也就是判断请求是否符合路由规则的条件
               - Path=/user/** # 这个是按照路径匹配，只要以/user/开头就符合要求
   ```

   我们将符合`Path` 规则的一切请求，都代理到 `uri`参数指定的地址。

   本例中，我们将 `/user/**`开头的请求，代理到`lb://userservice`，lb是负载均衡，根据服务名拉取服务列表，实现负载均衡。

4. 重启测试

   重启网关，访问http://localhost:10010/user/1时，符合`/user/**`规则，请求转发到uri：http://userservice/user/1，得到了结果：

   ```json
   {
       "id":1,
       "username":"柳岩",
       "address":"湖南省衡阳市"
   }
   ```

5. 网关路由的流程图

   整个访问的流程如下：

   ![image-20210714211742956](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714211742956.png)

网关搭建步骤：

1. 创建项目，引入nacos服务发现和gateway依赖

2. 配置application.yml，包括服务基本信息、nacos地址、路由

路由配置包括：

1. 路由id：路由的唯一标示

2. 路由目标（uri）：路由的目标地址，http代表固定地址，lb代表根据服务名负载均衡

3. 路由断言（predicates）：判断路由的规则，

4. 路由过滤器（filters）：对请求或响应做处理

接下来，就重点来学习路由断言和路由过滤器的详细知识

## 2.2 断言工厂

我们在配置文件中写的断言规则只是字符串，这些字符串会被Predicate Factory读取并处理，转变为路由判断的条件。例如Path=/user/**是按照路径匹配，这个规则是由

`org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory`类来处理的，像这样的断言工厂在SpringCloudGateway还有十几个:

| **名称**   | **说明**                       | **示例**                                                     |
| ---------- | ------------------------------ | ------------------------------------------------------------ |
| After      | 是某个时间点后的请求           | -  After=2037-01-20T17:42:47.789-07:00[America/Denver]       |
| Before     | 是某个时间点之前的请求         | -  Before=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]       |
| Between    | 是某两个时间点之前的请求       | -  Between=2037-01-20T17:42:47.789-07:00[America/Denver],  2037-01-21T17:42:47.789-07:00[America/Denver] |
| Cookie     | 请求必须包含某些cookie         | - Cookie=chocolate, ch.p                                     |
| Header     | 请求必须包含某些header         | - Header=X-Request-Id, \d+                                   |
| Host       | 请求必须是访问某个host（域名） | -  Host=**.somehost.org,**.anotherhost.org                   |
| Method     | 请求方式必须是指定方式         | - Method=GET,POST                                            |
| Path       | 请求路径必须符合指定规则       | - Path=/red/{segment},/blue/**                               |
| Query      | 请求参数必须包含指定参数       | - Query=name, Jack或者-  Query=name                          |
| RemoteAddr | 请求者的ip必须是指定范围       | - RemoteAddr=192.168.1.1/24                                  |
| Weight     | 权重处理                       |                                                              |

我们只需要掌握Path这种路由工程就可以了。上面的没有必要全部掌握，只需要听说就可以了，用的时候可以访问官网来查看。https://cloud.spring.io/spring-cloud-gateway/reference/html/#gateway-request-predicates-factories

PredicateFactory的作用：读取用户定义的断言条件，对请求做出判断。

Path=/user/**的含义：路径是以/user开头的就认为是符合的。

## 2.3 过滤器工厂

GatewayFilter是网关中提供的一种过滤器，可以对进入网关的请求和微服务返回的响应做处理：

![image-20210714212312871](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714212312871.png)

Spring提供了31种不同的路由过滤器工厂。例如：

| **名称**             | **说明**                     |
| -------------------- | ---------------------------- |
| AddRequestHeader     | 给当前请求添加一个请求头     |
| RemoveRequestHeader  | 移除请求中的一个请求头       |
| AddResponseHeader    | 给响应结果中添加一个响应头   |
| RemoveResponseHeader | 从响应结果中移除有一个响应头 |
| RequestRateLimiter   | 限制请求的流量               |

**请求头过滤器**

下面我们以AddRequestHeader 为例来讲解。

**需求**：给所有进入userservice的请求添加一个请求头：`Truth = lixnuan is freaking awesome!`

只需要修改gateway服务的application.yml文件，添加路由过滤即可：

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: user-service 
        uri: lb://userservice 
        predicates: 
        - Path=/user/** 
        filters: # 过滤器
        - AddRequestHeader=Truth, linxuan is freaking awesome! # 添加请求头
```

当前过滤器写在userservice路由下，因此仅仅对访问userservice的请求有效。

我们来测试一下，既然添加了一个请求头，那么我们就将请求头打印输出来看一看。路由断言是只要符合/user/就路由到userservice微服务，所以我们在userservice服务里面打印看看，修改一下UserController类的代码：

```java
@GetMapping("/{id}")
// 请求头如果有Truth，那么就获取参数并打印
public User queryById(@PathVariable("id") Long id,
                      @RequestHeader(value = "Truth", required = false) String truth) {
    System.out.println("truth: " + truth);
    return userService.queryById(id);
}
```

**默认过滤器**

如果要对所有的路由都生效，则可以将过滤器工厂写到default下。格式如下：

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: user-service 
        uri: lb://userservice 
        predicates: 
        - Path=/user/**
      default-filters: # 默认过滤项
      - AddRequestHeader=Truth, linxuan is freaking awesome! 
```

**总结**

过滤器的作用是什么？

1. 对路由的请求或响应做加工处理，比如添加请求头

2. 配置在路由下的过滤器只对当前路由的请求生效


defaultFilters的作用是什么？

* 对所有路由都生效的过滤器

## 2.4 全局过滤器

上一节学习的过滤器，网关提供了31种，但每一种过滤器的作用都是固定的。如果我们希望拦截请求，做自己的业务逻辑则没办法实现。

### 全局过滤器作用

全局过滤器的作用也是处理一切进入网关的请求和微服务响应，与GatewayFilter的作用一样。区别在于GatewayFilter通过配置定义，处理逻辑是固定的；而GlobalFilter的逻辑需要自己写代码实现。

定义方式是实现GlobalFilter接口。

```java
public interface GlobalFilter {
    /**
     *  处理当前请求，有必要的话通过{@link GatewayFilterChain}将请求交给下一个过滤器处理
     *
     * @param exchange 请求上下文，里面可以获取Request、Response等信息
     * @param chain 用来把请求委托给下一个过滤器 
     * @return {@code Mono<Void>} 返回标示当前过滤器业务结束
     */
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);
}
```

在filter中编写自定义逻辑，可以实现下列功能：登录状态判断、权限校验和请求限流等

### 自定义全局过滤器

需求：定义全局过滤器，拦截请求，判断请求的参数是否满足下面条件，如果同时满足则放行，否则拦截：

- 参数中是否有authorization，

- authorization参数值是否为admin

实现如下：

在gateway中定义一个过滤器：

```java
package com.linxuan.gateway.filters;

// Order是过滤器执行的顺序，值越小则越先执行。我们也可以实现Orderd接口，实现方法返回值来搞值
// @Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求参数
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        // 2.获取authorization参数
        String auth = params.getFirst("authorization");
        // 3.校验
        if ("admin".equals(auth)) {
            // 放行
            return chain.filter(exchange);
        }
        // 4.拦截
        // 4.1.禁止访问，设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        // 4.2.结束处理
        return exchange.getResponse().setComplete();
    }
    
    // 返回值代表执行顺序 == @Order(-1)
    @Override
    public int getOrder() {
        return -1;
    }
}
```

这时候我们在浏览器中访问http://localhost:10010/user/1是不会通过的，访问http://localhost:10010/user/1?authorization=admin123也不会通过，而访问http://localhost:10010/user/1?authorization=admin会通过。

### 过滤器执行顺序

请求进入网关会碰到三类过滤器：当前路由的过滤器、DefaultFilter、GlobalFilter

请求路由后，会将当前路由过滤器和DefaultFilter、GlobalFilter，合并到一个过滤器链（集合）中，排序后依次执行每个过滤器：

![image-20210714214228409](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714214228409.png)

排序的规则是什么呢？

- 每一个过滤器都必须指定一个int类型的order值，**order值越小，优先级越高，执行顺序越靠前**。
- GlobalFilter通过实现`Ordered`接口，或者添加`@Order`注解来指定order值，由我们自己指定
- 路由过滤器和defaultFilter的order由Spring指定，默认是按照声明顺序从1递增。
- 当过滤器的order值一样时，会按照 defaultFilter > 路由过滤器 > GlobalFilter的顺序执行。

详细内容，可以查看源码：

`org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator#getFilters()`方法是先加载defaultFilters，然后再加载某个route的filters，然后合并。

`org.springframework.cloud.gateway.handler.FilteringWebHandler#handle()`方法会加载全局过滤器，与前面的过滤器合并后根据order排序，组织过滤器链

## 2.5 跨域问题

跨域：域名不一致就是跨域，主要包括：

- 域名不同： www.taobao.com 和 www.taobao.org 和 www.jd.com 和 miaosha.jd.com

- 域名相同，端口不同：localhost:8080和localhost8081

跨域问题：浏览器禁止请求的发起者与服务端发生跨域ajax请求，请求被浏览器拦截的问题

解决方案：CORS，这个以前应该学习过，这里不再赘述了。不知道的小伙伴可以查看https://www.ruanyifeng.com/blog/2016/04/cors.html

### 模拟跨域问题

找到课前资料的页面文件：

![image-20210714215713563](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714215713563.png)

放入tomcat或者nginx这样的web服务器中，启动并访问。

可以在浏览器控制台看到下面的错误：

![image-20210714215832675](..\图片\6-03【Nacos配置管理、Feign、Gateway】/image-20210714215832675.png)

从localhost:8090访问localhost:10010，端口不同，显然是跨域的请求。

### 解决跨域问题

在gateway服务的application.yml文件中，添加下面的配置：

```yaml
spring:
  cloud:
    gateway:
      # 。。。
      globalcors: # 全局的跨域处理
        add-to-simple-url-handler-mapping: true # 解决options请求被拦截问题
        corsConfigurations:
          '[/**]':
            allowedOrigins: # 允许哪些网站的跨域请求 
              - "http://localhost:8090"
            allowedMethods: # 允许的跨域ajax的请求方式
              - "GET"
              - "POST"
              - "DELETE"
              - "PUT"
              - "OPTIONS"
            allowedHeaders: "*" # 允许在请求中携带的头信息
            allowCredentials: true # 是否允许携带cookie
            maxAge: 360000 # 这次跨域检测的有效期
```

 
