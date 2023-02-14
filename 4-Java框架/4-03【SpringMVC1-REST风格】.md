![](..\图片\4-03【SpringMVC】\0-1.png)

# 第一章 SpringMVC基础

SpringMVC是隶属于Spring框架的一部分，主要是用来进行Web开发，是对Servlet进行了封装。

对于SpringMVC我们主要学习如下内容：

* 请求与响应：SpringMVC是处于Web层的框架，所以其主要的作用就是用来接收前端发过来的请求和数据然后经过处理并将处理的结果响应给前端，所以如何处理请求和响应是SpringMVC中非常重要的一块内容。

* REST风格：REST是一种软件架构风格，可以降低开发的复杂性，提高系统的可伸缩性，后期的应用也是非常广泛。

* SSM整合(注解版)：SSM整合是把咱们所学习的SpringMVC+Spring+Mybatis整合在一起来完成业务开发，是对我们所学习这三个框架的一个综合应用。

* 拦截器

目前现在web程序大都基于三层架构来实现：浏览器发送一个请求给后端服务器，后端服务器使用Servlet来接收请求和数据。如果所有的处理都交给Servlet来处理的话，所有的东西都耦合在一起，对后期的维护和扩展极为不利。将后端服务器Servlet拆分成三层，分别是`web`、`service`和`dao`

* `web`层主要由Servlet来处理，负责页面请求和数据的收集以及响应结果给前端
* `service`层主要负责业务逻辑的处理
* `dao`层主要负责数据的增删改查操作

Servlet处理请求和数据的时候，存在的问题是一个Servlet只能处理一个请求。针对web层进行了优化，采用了MVC设计模式，将其设计为`controller`、`service`和`dao`。

* `controller`负责请求和数据的接收，接收后将其转发给`service`进行业务处理
* `service`根据需要会调用dao对数据进行增删改查
* `dao`把数据处理完后将结果交给`service`，`service`再交给`controller`
* `controller`根据需求组装成Model和View，Model和View组合起来生成页面转发给前端浏览器
* 这样做的好处就是`controller`可以处理多个请求，并对请求进行分发，执行不同的业务操作。

![](..\图片\4-03【SpringMVC】\1-1.png)

随着互联网的发展，上面的模式因为是同步调用，性能慢慢的跟不是需求，所以异步调用慢慢的走到了前台，是现在比较流行的一种处理方式。

![1630427769938](..\图片\4-03【SpringMVC】\1-2.png)

因为是异步调用，所以后端不需要返回view视图，将其去除。前端如果通过异步调用的方式进行交互，后台就需要将返回的数据转换成json格式进行返回

SpringMVC主要负责的就是：`controller`如何接收请求和数据、如何将请求和数据转发给业务层、如何将响应数据转换成json发回到前端。

SpringMVC是一种基于Java实现MVC模型的轻量级Web框架。优点：使用简单、开发便捷(相比于Servlet)，灵活性强。

## 1.1 入门案例

```xml
<!-- pom.xml导入依赖和插件 -->
<dependencies>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <!-- 依赖作用范围为provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>
    <!-- 依赖spring-context、spring-aop、spring-beans、spring-core、spring-expression、spring-web -->
    <!-- 该jar包已经依赖于spring-context，所以就不用导入spring-context坐标了 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
</dependencies>
<build>
    <!--设置插件-->
    <plugins>
        <!--具体的插件配置-->
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.1</version>
            <configuration>
                <port>80</port>
                <path>/</path>
            </configuration>
        </plugin>
    </plugins>
</build>
```

```java
package com.linxuan.config;

@Configuration
// 这里的包扫描路径只需要扫描到controller层即可，具体原因后面分析
@ComponentScan("com.linxuan.controller")
public class SpringMvcConfig {   
}
```

```java
@Controller
public class UserController {
    
    @RequestMapping("/save")
    // 告诉springmvc，响应的数据就是返回给前端的数据（JSON格式），并不是资源页面名称，不需要解析。
    @ResponseBody
    public String save(){
        System.out.println("user save ...");
        // 设置返回格式为JSON。前面说过，目前主流业务都是前端发送异步请求，后台响应json数据
        return "{'info':'springmvc'}";
    }
}
```

```java
package com.linxuan.config;

// 使用配置类替换web.xml。将web.xml删除，换成ServletContainersInitConfig
public class ServletContainersInitConfig extends AbstractDispatcherServletInitializer {
    // 加载springmvc配置类
    protected WebApplicationContext createServletApplicationContext() {
        // 初始化WebApplicationContext对象
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        // 加载指定配置类
        ctx.register(SpringMvcConfig.class);
        return ctx;
    }

    // 设置由springmvc控制器处理的请求映射路径
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // 加载spring配置类
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }
}
```

启动项目很简单，只需要在Maven控制面板找到Tomcat7插件，然后运行即可。

`AbstractDispatcherServletInitializer`类是SpringMVC提供的快速初始化Web3.0容器的抽象类，它提供了三个接口方法供用户实现：

1. `createServletApplicationContext`方法，创建Servlet容器时，加载SpringMVC对应的bean并放入`WebApplicationContext`对象范围中，而`WebApplicationContext`的作用范围为`ServletContext`范围，即整个web容器范围。`createServletApplicationContext`用来加载SpringMVC环境。
2. `getServletMappings`方法，设定SpringMVC对应的请求映射路径，即SpringMVC拦截哪些请求。
3. `createRootApplicationContext`方法，如果创建Servlet容器时需要加载非SpringMVC对应的bean，使用当前方法进行，使用方式和`createServletApplicationContext`相同。`createRootApplicationContext`用来加载Spring环境。

| 名称     | @RequestMapping                                              |
| -------- | ------------------------------------------------------------ |
| 类型     | 类注解或方法注解，在SpringMVC控制器类或方法定义上方定义。`@RequestMapping("/save")` |
| 作用     | 设置当前控制器方法请求访问路径                               |
| 相关属性 | value(默认)，请求访问路径                                    |

| 名称 | @ResponseBody                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解或方法注解，在SpringMVC控制器类或方法上方，写在类上，该类的所有方法都有该注解功能。 |
| 作用 | 设置当前控制器返回值作为响应体，并不是资源名称无需解析。<br />返回值为字符串，会将其作为文本响应给前端。返回值为对象，会将对象转换成JSON响应给前端。 |
| 属性 | pattern：指定日期时间格式字符串                              |

## 1.2 web.xml讲解

web.xml是web项目程序启动的入口。我们开发用的是注解的格式开发，并没有用到web.xml，但是以后会接触。

```apl
项目的根目录:
      |-- src # 源码
      |    |-- main # 主工程代码
      |    |    |-- java # 业务逻辑代码
      |    |    |-- resources # 业务逻辑代码配置文件
      |    |    |-- webapp # web项目的资源目录。例如:jps/html/css/js
      |    |          |-- WEB-INF # 存放的是一些编译后的class文件和运行所必须的配置文件
      |    |                |-- web.xml文件
      |    |          |-- index.jsp/html/css/js # 存放前端资源文件
      |    |
      |    |-- test # 测试代码
      |         |-- java # 测试代码
      |         |-- resources # 测试代码所需要的配置文件
      |-- target # 
      |    |-- classes # 业务逻辑代码编译后的文件存放到这个目录下面
      |    |-- test-classes # 测试代码编译后的文件存放到这个目录下面
      |    |-- 项目.jar # 项目打包存放目录
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!-- web.xml中标签加载顺序为：context-param > listener > filter > servlet -->

    <!-- spring配置 Spring加载的xml文件,不配置默认为applicationContext.xml-->
    <!--指定web项目从项目的src路径下加载application-context.xml，这是springMVC所必不可少的配置项-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:applicationContext.xml</param-value>
    </context-param>
    <!-- 通过监听器加载spring运行环境，通过ContextLoaderListener自动装配ApplicationContext的配置信息-->
    <!-- 该类作为spring的listener使用，它会在创建时自动查找web.xml配置的applicationContext.xml文件 -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- 配置请求过滤器，编码格式设为UTF-8，避免中文乱码 -->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <!-- init-param将初始化需要的参数传入到filter-class中从而进行初始化 -->
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <!-- 将filter和url路径进行映射。filter和filter-mapping中的name必须是相同的，才能起到映射的作用 -->
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <!-- <url-pattern>匹配请求路径，这里是匹配所有路径。所有路径都要交给SpringMVC处理 -->
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!--spring mvc配置-->
    <!-- 配置SpingMVC的DispatcherServlet,也可以配置为继承了DispatcherServlet的自定义类 -->
    <servlet>
        <!-- 配置DispatcherServlet -->
        <servlet-name>DispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 指定spring mvc配置文件位置 不指定使用默认情况 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath*:spring-mvc.xml</param-value>
        </init-param>
        <!-- 设置启动顺序 使系统在启动时装在servlet而不是第一个servlet被访问-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <!-- ServLet 匹配映射 -->
    <servlet-mapping>
        <servlet-name>DispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

## 1.3 springmvc.xml

它是SpringMVC的配置文件。我们采用的都是注解开发，并没有用到配置文件的方式，这里来了解一下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- xmlns:mvc开启SpringMVC命名空间 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/mvc 
                           http://www.springframework.org/schema/mvc/spring-mvc.xsd
                           http://www.springframework.org/schema/context 
                           https://www.springframework.org/schema/context/spring-context.xsd">

    <!--
        启用spring mvc 注解, 默认的注解映射的支持。
        自动注册 RequestMappingHandlerMapping RequestMappingHandlerAdapter 两个 bean
        这是 Spring mvc 分发请求所必须的
        并提供了数据绑定支持、读写XML支持、读写Json支持
        等同于@EnableWebMvc
    -->
    <mvc:annotation-driven/>

    <!--
        对静态资源文件的访问，
        将无法mapping到Controller的path
        交给default servlet handler处理
    -->
    <mvc:default-servlet-handler />

    <!-- 开启注解处理器 -->
    <context:annotation-config/>
    <!--包扫描 等同于@ComponentScan-->
    <context:component-scan base-package="com.linxuan.controller"/>

    <!-- 注册Bean到Spring容器中 支持返回json(避免IE在ajax请求时，返回json出现下载文件 ) -->
    <bean id="mappingJacksonHttpMessageConverter"
          class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">　　　　　　　　
        <property name="supportedMediaTypes">
            <list>
                <value>text/html;charset=UTF-8</value>
                <value>text/json;charset=UTF-8</value>
                <value>application/json;charset=UTF-8</value>
            </list>
        </property>
    </bean>
</beans>
```

## 1.4 工作流程解析

为了更好的使用SpringMVC，接下来来分析一下SpringMVC的工作流程。我们将SpringMVC的使用过程总共分两个阶段来分析，分别是`启动服务器初始化过程`和`单次请求过程`：

![](D:\Java\笔记\图片\4-03【SpringMVC】\1-4.png)

启动服务器过程使用到的API层次结构如下：

```apl
WebApplicationInitializer(接口)
    |-- AbstractContextLoaderInitializer(实现WebApplicationInitializer接口，同时是一个抽象类)
    |    |-- AbstractDispatcherServletInitializer(继承AbstractContextLoaderInitializer，抽象类)
    |         |-- ServletContainersInitConfig(我们编写的类，继承自AbstractDispatcherServletInitializer)
    |         |-- AbstractAnnotationConfigDispatcherServletInitializer
    |-- AbstractReactiveWebInitializer(实现WebApplicationInitializer接口，同时是一个抽象类)
```

```java
// 顶层接口，提供了方法 onStartup，基于此来拓展 ServletContext
public interface WebApplicationInitializer {
	void onStartup(ServletContext servletContext) throws ServletException;
}
```

```java
// 创建 根容器（WebApplicationContext） 并给 ServletContext 注册了一个 ContextLoaderListener
public abstract class AbstractContextLoaderInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		registerContextLoaderListener(servletContext);
	}

	protected void registerContextLoaderListener(ServletContext servletContext) {
		// 创建根容器
		WebApplicationContext rootAppContext = createRootApplicationContext();
		if (rootAppContext != null) {
			// 注册一个 ContextLoaderListener
			ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
			// 允许指定 ApplicationContextInitializer
			listener.setContextInitializers(getRootApplicationContextInitializers());
			servletContext.addListener(listener);
		}
		else {
		}
	}

	// 创建根容器，子类实现
	@Nullable
	protected abstract WebApplicationContext createRootApplicationContext();

	// 子类可以指定 ApplicationContextInitializer
	@Nullable
	protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
		return null;
	}
}
```

```java
// 主要是创建web子容器，并为ServletContext创建一个DispatcherServlet(SpringMVC核心Servlet)
public abstract class AbstractDispatcherServletInitializer extends AbstractContextLoaderInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        registerDispatcherServlet(servletContext);
    }

    protected void registerDispatcherServlet(ServletContext servletContext) {
        String servletName = getServletName();

        // 创建 web 子容器
        WebApplicationContext servletAppContext = createServletApplicationContext();

        // 创建并注册 DispatcherServlet 实例，子类可以拓展
        // SpringMVC的核心Servlet，主要负责路由的匹配、分发、执行、视图返回等
        FrameworkServlet dispatcherServlet = createDispatcherServlet(servletAppContext);
        dispatcherServlet.setContetInitializers(getServletApplicationContextInitializers());
    }
    
    // 子类可以指定ApplicationContextInitializer
    @Nullable
    protected ApplicationContextInitializer<?>[] getServletApplicationContextInitializers() {
        return null;
    }

    // 创建web子容器，子类实现
    protected abstract WebApplicationContext createServletApplicationContext();
    
    // 设置请求映射路径
    protected abstract String[] getServletMappings();
}
```

```java
// 使用配置类替换web.xml。将web.xml删除，换成ServletContainersInitConfig
public class ServletContainersInitConfig extends AbstractDispatcherServletInitializer {
    // 加载springmvc配置类
    protected WebApplicationContext createServletApplicationContext() {
        // 初始化WebApplicationContext对象
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        // 加载指定配置类
        ctx.register(SpringMvcConfig.class);
        return ctx;
    }

    // 设置由springmvc控制器处理的请求映射路径
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // 加载spring配置类
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }
}
```

**启动服务器初始化过程**

1. 服务器启动，执行`ServletContainersInitConfig`类，初始化web容器。功能类似于以前的`web.xml`

2. 执行`createServletApplicationContext`方法，创建了`WebApplicationContext`对象。该方法加载SpringMVC的配置类`SpringMvcConfig`来初始化SpringMVC的容器。

   ```java
   public class ServletContainersInitConfig extends AbstractDispatcherServletInitializer {
       // 加载springmvc配置类
       protected WebApplicationContext createServletApplicationContext() {
           // 初始化WebApplicationContext对象
           AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
           // 加载指定配置类
           ctx.register(SpringMvcConfig.class);
           return ctx;
       }
   }
   ```

3. 加载`SpringMvcConfig`配置类。

   ```java
   @Configuration
   @ComponentScan("com.linxuan")
   public class SpringMvcConfig {   
   }
   ```
   
4. 执行`@ComponentScan`加载对应的bean。扫描指定包及其子包下所有类上的注解。

5. 加载`UserController`，每个`@RequestMapping`的名称对应一个具体的方法。此时就建立了`/save` 和 `save`方法的对应关系

   ```java
   @Controller
   public class UserController {
   
       @RequestMapping("/save")
       @ResponseBody
       public String save() {
           System.out.println("user save...");
           return "{'info':'springmvc'}";
       }
   }
   ```
   
6. 执行`getServletMappings`方法，设定SpringMVC拦截请求的路径规则。`/`代表所拦截请求的路径规则，只有被拦截后才能交给SpringMVC来处理请求。

   ```java
   // 设置由springmvc控制器处理的请求映射路径
   protected String[] getServletMappings() {
       return new String[]{"/"};
   }
   ```

**单次请求过程**

1. 发送请求http://localhost/save
2. web容器发现该请求满足SpringMVC拦截规则，将请求交给SpringMVC处理
3. 解析请求路径`/save`
4. 由`/save`匹配执行对应的方法`save()`。上面的第五步已经将请求路径和方法建立了对应关系，通过`/save`就能找到对应的`save`方法
5. 执行`save()`
6. 检测到有`@ResponseBody`直接将`save()`方法的返回值作为响应体返回给请求方

## 1.5 包扫描冲突

Spring中创建过一个配置类`SpringConfig`，SpringMVC中创建一个`SpringMvcConfig`的配置类。两个配置类都是使用`@ComponentScan`注解进行包扫描的。

Spring的配置类需要加载其控制的bean有业务bean(Service)、功能bean(DataSource，SqlSessionFactoryBean，MapperScannerConfigurer等)。SpringMVC的配置类加载其相关bean有表现层bean，也就是controller包下的类。

`SpringMvcConfig`将其扫描范围设置到controller即可，而`SpringConfig`需要扫描除controller之外的包，之前都是直接扫描整个包（包括了controller包）。所以接下来就是要加载Spring控制的bean的时候排除掉SpringMVC控制的bean。

具体排除方式如下：

* 方式一：Spring加载的bean设定扫描范围为精准范围，例如service包、dao包等

  ```java
  @Configuration
  // 真正做开发的时候，因为Dao是交给MapperScannerConfigurer对象来进行扫描处理的，所以只需要扫描service
  @ComponentScan({"com.linxuan.service"，"com.linxuan.dao"})
  public class SpringConfig {
  }
  ```
* 方式二：Spring加载的bean设定扫描范围为`com.linxuan`，排除掉controller包中的bean

  ```java
  @Configuration
  // excludeFilters属性：设置扫描加载bean时，排除的过滤规则
  // type属性：设置排除规则。type = FilterType.ANNOTATIONANNOTATION：按照注解排除
  // classes属性：设置排除的具体注解类，当前设置排除@Controller定义的bean
  @ComponentScan(value = "com.linxuan",
          excludeFilters = @ComponentScan.Filter(
                  type = FilterType.ANNOTATION,
                  classes = Controller.class
          )
  )
  public class SpringConfig {
  }
  ```
* 方式三：不区分Spring与SpringMVC的环境，加载到同一个环境中[了解即可]

有了Spring的配置类，要想在tomcat服务器启动将其加载，我们需要修改`ServletContainersInitConfig`

```java
public class ServletContainersInitConfig extends AbstractDispatcherServletInitializer {
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringMvcConfig.class);
        return ctx;
    }
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringConfig.class);
        return ctx;
    }
}
```

## 1.6 简化配置

对于上述的配置方式，Spring还提供了一种更简单的配置方式，可以不用再去创建`AnnotationConfigWebApplicationContext`对象，不用手动`register`对应的配置类：

```java
public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

## 1.7 PostMan工具

代码编写完后，我们要想测试，只需要打开浏览器直接输入地址发送请求即可。发送的是`GET`请求可以直接使用浏览器，但是如果要发送的是`POST`请求呢？

如果要求发送的是post请求，我们就得准备页面在页面上准备form表单，测试起来比较麻烦。所以我们就需要借助一些第三方工具，如PostMan.

* PostMan是一款功能强大的网页调试与发送网页HTTP请求的Chrome插件。
* 作用：常用于进行接口测试

* 特征：简单、实用、美观、大方。

## 1.8 访问静态界面处理

搭建环境：

```groovy
// Gradle工程导入插件及依赖
plugins {
    id 'java'
    // 导入war插件，这是一个Web工程
    id 'war'
}

dependencies {
    // SpringMVC依赖环境
    // servlet依赖。依赖作用范围compileOnly，适用于编译期需要而不需要打包，避免和tomcat中依赖冲突
    compileOnly group: 'javax.servlet', name: 'javax.servlet-api', version: '3.1.0'
    // 导入SpringMVC依赖，该jar包依赖于spring-context，所以不用导入Spring的环境
    implementation group: 'org.springframework', name: 'spring-webmvc', version: '5.2.10.RELEASE'
    // SpringMVC默认使用的是jackson来处理json的转换，导入依赖坐标
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.9.0'
    
    // 导入其他依赖环境
    // 导入lombok依赖
    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    // annotationProcessor代表main下代码的注解执行器
    annotationProcessor group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
}
```

```java
@Configuration
// 开启SpringMVC的注解支持，有多项辅助功能。包含将JSON转换成对象的功能。标配，不要忘记。
@EnableWebMvc
@ComponentScan("com.linxuan.controller")
public class SpringMvcConfig {   
}
```

```java
public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

创建一个静态页面

```html
<!-- pages目录下创建page.html -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h2>Hello 这是page页面</h2>
</body>
</html>
```

打开浏览器输入http://localhost/pages/page.html。访问页面出错，报错信息如下：

```apl
# SpringMVC拦截了静态资源，根据/pages/page.html去controller找对应的方法，找不到所以会报404的错误。
org.springframework.web.servlet.DispatcherServlet.noHandlerFound No mapping for GET /pages/books.html
```

那么SpringMVC为什么会拦截静态资源呢？想想我们之前配置的`ServletContainersInitConfig`，里面的`getServletMappings`方法，我们配置的拦截路径是`/`，也就是所有的路径我们都会拦截然后交给SpringMVC管理。所以导致了404，SpringMVC拦截路径之后去解析，根据`/pages/page.html`去`controller`找对应的方法，找不到所以会报错。

**使用类将资源放行**

SpringMVC需要将静态资源进行放行。创建`SpringMvcSupport`继承`WebMvcConfigurationSupport`，覆盖重写`addResourceHandlers`该方法，里面将一些静态资源放行。

```java
package com.linxuan.config;

@Configuration
public class SpringMvcSupport extends WebMvcConfigurationSupport {
    // 设置静态资源访问过滤，当前类需要设置为配置类，并被扫描加载
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 当访问/pages/**时候，从/pages目录下查找内容
        registry.addResourceHandler("/pages/**").addResourceLocations("/pages/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/plugins/**").addResourceLocations("/plugins/");
    }
}
```

同时也需要让该配置类被Spring管理。该配置类是在config目录下，SpringMVC扫描的是controller包，所以该配置类还未生效，要想生效需要将SpringMvcConfig配置类进行修改。

```java
@Configuration
@ComponentScan({"com.linxuan.controller", "com.linxuan.config"})
@EnableWebMvc
public class SpringMvcConfig {
}

或者

@Configuration
@ComponentScan("com.linxuan")
@EnableWebMvc
public class SpringMvcConfig {
}
```

**使用配置文件方式将资源放行**

上面使用全部的注解开发的，接下来看一下配置文件解决静态资源访问问题：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- web.xml文件 -->
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!--spring mvc配置-->
    <!-- 配置SpingMVC的DispatcherServlet,也可以配置为继承了DispatcherServlet的自定义类 -->
    <servlet>
        <!-- 配置DispatcherServlet -->
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 指定spring mvc配置文件位置 不指定使用默认情况 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath*:springmvc.xml</param-value>
        </init-param>
        <!-- 设置启动顺序 使系统在启动时装在servlet而不是第一个servlet被访问-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <!-- ServLet 匹配映射 -->
    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- xmlns:mvc开启SpringMVC命名空间 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/mvc
                           http://www.springframework.org/schema/mvc/spring-mvc.xsd
                           http://www.springframework.org/schema/context
                           https://www.springframework.org/schema/context/spring-context.xsd">
    <!-- 开启注解处理器 -->
    <context:annotation-config/>

    <!-- 设置静态资源不过滤，等于SpringMvcSupport extends WebMvcConfigurationSupport方法 -->
    <mvc:resources location="/pages/" mapping="/pages/**"></mvc:resources>
</beans>
```

# 第二章 请求与响应

之前提到过，SpringMVC是web层的框架，主要的作用是接收请求、接收数据、响应结果，所以这一章节是学习SpringMVC的重点内容。

首先来搭建环境：

```xml
<dependencies>
    <!-- SpringMVC依赖环境 -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <!-- 依赖作用范围未provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>
    <!-- 该jar包依赖于spring-context，所以不用导入Spring的环境 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
        <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
        <scope>provided</scope>
    </dependency>
</dependencies>
<build>
    <!--设置插件-->
    <plugins>
        <!--具体的插件配置-->
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.1</version>
            <configuration>
                <!-- 端口号设置为80，这样不用写端口号 -->
                <port>80</port>
                <!-- 虚拟路径配置为/，这样就不用配置虚拟路径了 -->
                <path>/</path>
            </configuration>
        </plugin>
    </plugins>
</build>
```

```groovy
// 如果是gradle构建工程，导入如下插件和坐标
plugins {
    id 'java'
    id 'war'
}

dependencies {
    compileOnly group: 'javax.servlet', name: 'javax.servlet-api', version: '3.1.0'
    implementation group: 'org.springframework', name: 'spring-webmvc', version: '5.2.10.RELEASE'
    // 导入lombok依赖
    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    // annotationProcessor代表main下代码的注解执行器
    annotationProcessor group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
}
```

```java
package com.linxuan.config;

@Configuration
@ComponentScan("com.linxuan.controller")
public class SpringMvcConfig {   
}
```

```java
package com.linxuan.config;

public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

## 2.1 设置请求映射路径

在上面的环境下添加一个`UserController`和`BookController`类：

```java
@Controller
public class UserController {

    @RequestMapping("/save")
    @ResponseBody
    public String save() {
        System.out.println("user save ...");
        return "{'module':'user save'}";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(){
        System.out.println("user delete ...");
        return "{'module':'user delete'}";
    }
}
```

```java
@Controller
public class BookController {

    @RequestMapping("/save")
    @ResponseBody
    public String save() {
        System.out.println("book save ...");
        return "{'module':'book save'}";
    }
}
```

把环境准备好后，启动Tomcat服务器，发现会报错：

```apl
# UserController和BookController都有一个save方法，他们的访问路径都是http://localhost/save。重复导致出错
Caused by: java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'userController' method com.linxuan.controller.UserController save() to { /save}: There is already 'bookController' bean method
```

解决思路就是为不同模块设置模块名作为请求路径前置：对BookController类的save方法将其访问路径设置为`http://localhost/book/save`，对UserController类的save方法将其访问路径设置为`http://localhost/user/save`。这样在同一个模块中出现命名冲突的情况就比较少了。

有两种方式来解决：将方法上的RequestMapping注解访问路径写详细、将方法上的RequestMapping注解中相同的路径提取到类上面。

* 将方法上的RequestMapping注解访问路径写详细

  ```java
  @Controller
  public class UserController {
  
      // 写上具体的路径
      @RequestMapping("/user/save")
      @ResponseBody
      public String save(){
          System.out.println("user save ...");
          return "{'module': 'user save'}";
      }
      
      @RequestMapping("/user/delete")
      @ResponseBody
      public String save(){
          System.out.println("user delete ...");
          return "{'module': 'user delete'}";
      }
  }
  ```

  ```java
  @Controller
  public class BookController {
  
      @RequestMapping("/book/save")
      @ResponseBody
      public String save(){
          System.out.println("book save ...");
          return "{'module': 'book save'}";
      }
  }
  ```

* 将方法上的RequestMapping注解中相同的路径提取到类上面

  ```java
  @Controller
  @RequestMapping("/user")
  public class UserController {
  
      @RequestMapping("/save")
      @ResponseBody
      public String save(){
          System.out.println("user save ...");
          return "{'module': 'user save'}";
      }
      
      @RequestMapping("/delete")
      @ResponseBody
      public String save(){
          System.out.println("user delete ...");
          return "{'module': 'user delete'}";
      }
  }
  ```

  ```java
  @Controller
  @RequestMapping("/book")
  public class BookController {
  
      @RequestMapping("/save")
      @ResponseBody
      public String save(){
          System.out.println("book save ...");
          return "{'module': 'book save'}";
      }
  }
  ```

第一种的方法写起来比较麻烦而且还有很多重复代码，耦合度太高。第二种方法将相同的路径提取到类上面，当类上和方法上都添加了`@RequestMapping`注解，前端发送请求的时候，要和两个注解的value值相加匹配才能访问到。耦合度大大降低了，建议使用第二种方式。

> `@RequestMapping`注解value属性前面加不加`/`都可以

## 2.2 获取普通请求参数

请求路径设置好后，下面讲解一下如何接收前端页面传递的参数。来看一下后端如何获取前端根据GET方式和POST方式发送的参数：

**GET发送单个参数**

发送请求与参数：http://localhost/commonParam?name=linxuan

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 只需要用参数接收即可
    public String commonParam(String name) {
        System.out.println("name-->" + name);
        return "{'module': 'commonParam'}";
    }
}
```

**GET发送多个参数**

发送请求与参数：http://localhost/commonParam?name=linxuan&age=23

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 多个参数接收
    public String commonParam(String name, int age) {
        System.out.println("name-->" + name + ", age-->" + age);
        return "{'module': 'commonParam'}";
    }
}
```

**GET请求中文乱码**

如果传递的参数中有中文，接收到的参数会出现中文乱码问题。例如发送请求：http://localhost/commonParam?name=张三&age=23

Tomcat8.5以后的版本已经处理了中文乱码的问题，但是使用的Tomcat插件是Tomcat7，所以需要修改`pom.xml`来解决GET请求中文乱码问题。

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.1</version>
            <configuration>
                <!-- tomcat端口号 -->
                <port>80</port>
                <!-- 虚拟目录 -->
                <path>/</path> 
                <!-- 访问路径编解码字符集 -->
                <uriEncoding>UTF-8</uriEncoding>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**POST发送参数**

发送请求与GET方式相等，只是将请求方式换成了POST：http://localhost/commonParam?name=张三&age=23

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 后端接收参数不需要修改，和GET请求方式相同
    public String commonParam(String name, int age) {
        System.out.println("name-->" + name + ", age-->" + age);
        return "{'module': 'commonParam'}";
    }
}
```

**POST请求中文乱码**

发送请求与参数：http://localhost/commonParam?name=张三&age=23

接收参数：控制台打印，会发现有中文乱码问题。

解决方案：配置过滤器

```java
public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<？>[] getRootConfigClasses() {
        return new Class[0];
    }

    protected Class<？>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    //乱码处理
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        return new Filter[]{filter};
    }
}
```

`CharacterEncodingFilter`是在spring-web包中，所以用之前需要导入对应的jar包。

## 2.3 五种类型参数传递

前面我们已经能够使用GET或POST来发送请求和数据，所携带的数据都是比较简单的数据，接下来在这个基础上，来研究一些比较复杂的参数传递：普通参数传递-地址参数和形参不一致、POJO数据类型、嵌套POJO类型参数、数组类型参数、集合类型参数。

**普通参数传递-地址参数和形参不一致**

前端通过url地址传参。后端通过定义形参即可接收参数，并且定义的形参变量名要和地址参数名相同。如果形参与地址参数名不一致该如何解决？接下来测试一下：http://localhost/commonParam?name=李四&age=23

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 参数接收，并且这里的形参名和前端传过来的参数不一样
    public String commonParam(String userName, int age) {
        System.out.println("name-->" + userName + ", age-->" + age);
        return "{'module': 'commonParam'}";
    }
}
// 控制台打印：name-->null, age-->23
```

前端传递的参数为`name`，后台接收使用的是`userName`，两个名称对不上导致接收数据失败。可以使用`@RequestParam`注解解决：

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 使用@RequestParam注解来解决地址参数和形参不一致的问题
    // 写上@RequestParam注解框架就不需要自己去解析注入，能提升框架处理性能
    public String commonParam(@RequestParam("name") String userName, int age) {
        System.out.println("name-->" + userName + ", age-->" + age);
        return "{'module': 'commonParam'}";
    }
}
```

**POJO数据类型**

简单数据类型一般处理的是参数个数比较少的请求，如果参数比较多，那么后台接收参数的时候就比较复杂，这个时候可以考虑使用POJO数据类型。

接收POJO类型的参数和普通参数一样，只需要定义和请求参数名称一样的POJO类型形参即可接收参数。

发送请求：http://localhost/commonParam?name=李四&age=23

```java
// 定义一个User实体类
@Data
public class User {
    private String name;
    private int age;
}
```

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 接收POJO类型的参数和普通参数一样，只需要定义和请求参数名称一样的POJO类型形参即可接收参数。
    public String commonParam(User user) {
        System.out.println("user--> " + user);
        return "{'module': 'commonParam'}";
    }
}
```

**嵌套POJO类型参数**

如果POJO对象中嵌套了其他的POJO类，如

```java
@Data
public class Address {
    private String province;
    private String city;
}
```

```java
@Data
public class User {
    private String name;
    private int age;
    private Address address;
}
```

接收嵌套POJO参数：请求参数名与形参对象属性名相同，按照对象层次结构关系即可接收嵌套POJO属性参数

发送请求和参数：http://localhost/commonParam?name=linxuan&age=23&address.city=handan&address.province=handan

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 请求参数名与形参对象属性名相同，按照对象层次结构关系即可接收嵌套POJO属性参数
    public String commonParam(User user) {
        System.out.println("user-->" + user);
        return "{'module': 'commonParam'}";
    }
}
// 输出：user--> User(name=linxuan, age=23, address=Address(province=handan, city=handan))
```

**数组类型参数**

如果前端需要获取用户的爱好，爱好绝大多数情况下都是多个，那么这个时候就可以通过数组来接收数据。请求参数名与形参对象属性名相同且请求参数为多个，定义数组类型即可接收参数。

发送请求：http://localhost/commonParam?likes=唱&likes=跳&likes=rap

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 请求参数名与形参对象属性名相同且请求参数为多个，定义数组类型即可接收参数
    public String commonParam(String[] likes) {
        System.out.println("likes-->" + Arrays.toString(likes));
        return "{'module': 'commonParam'}";
    }
}
```

**集合类型参数**

数组能接收多个值，那么集合也可以实现这个功能。发送请求：http://localhost/commonParam?likes=唱&likes=跳&likes=rap

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    // 使用集合来接收参数
    public String commonParam(List<String> likes) {
        System.out.println("likes-->" + likes);
        return "{'module': 'commonParam'}";
    }
}
```

运行会报错：`java.util.List.<init>()`。错误的原因是SpringMVC将List看做是一个POJO对象来处理，将其创建一个对象并准备把前端的数据封装到对象中，但是List是一个接口无法创建对象，所以报错。

解决方案是使用`@RequestParam`注解。使用`@RequestParam`注解映射到对应名称的集合对象中作为数据。

```java
@Controller
public class UserController {

    @RequestMapping("/commonParam")
    @ResponseBody
    public String commonParam(@RequestParam List<String> likes) {
        System.out.println("likes-->" + likes);
        return "{'module': 'commonParam'}";
    }
}
```

集合保存普通参数：请求参数名与形参集合对象名相同且请求参数为多个，`@RequestParam`绑定参数关系

| 名称 | @RequestParam                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义。                |
| 示例 | `public String commonParam(@RequestParam("name") String userName)` |
| 作用 | 绑定请求参数与处理器方法形参间的关系，前端参数和后端形参变量名不一样的时候可以用该注解。 |
| 属性 | required是否为必传参数、defaultValue参数默认值。             |

## 2.4 JSON数据传输参数

前面我们说过，现在比较流行的开发方式为异步调用。前后台以异步方式进行交换，传输的数据使用的是JSON，所以后端要接收前端传过来的JSON数据，接收方式如下：

* 导入依赖

  ```xml
  <!-- SpringMVC默认使用的是jackson来处理json的转换，所以需要在pom.xml添加jackson依赖 -->
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.9.0</version>
  </dependency>
  ```

  ```groovy
  // Gradle项目导入该依赖
  dependencies {
      // SpringMVC默认使用的是jackson来处理json的转换，导入依赖坐标
      implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.9.0'
  }
  ```

* 在SpringMVC的配置类中开启SpringMVC的注解支持，这里面就包含了将JSON转换成对象的功能。

  ```java
  @Configuration
  @ComponentScan("com.linxuan.controller")
  // 开启SpringMVC的注解支持，这里面就包含了将JSON转换成对象的功能。开启json数据类型自动转换
  @EnableWebMvc
  public class SpringMvcConfig {
  }
  ```

* 参数前添加`@RequestBody`。使用`@RequestBody`注解将外部传递的json数组数据映射到形参的集合对象中作为数据

对于JSON数据类型，我们常见的有三种：json普通数组、json对象、json对象数组。格式如下

```json
// json普通数组
["value1", "value2", "value3", ...]

// json对象
{
    "key1": "value1",
    "key2": "value2"
}

// json对象数组
[
    {"key1": "value1"},
    {"key2": "value2"}
]
```

**JSON普通数组**

```groovy
// Gradle项目导入该依赖
dependencies {
    // SpringMVC默认使用的是jackson来处理json的转换，导入依赖坐标
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.9.0'
}
```

```java
@Configuration
@ComponentScan("com.linxuan.controller")
// 开启SpringMVC的注解支持，这里面就包含了将JSON转换成对象的功能。开启json数据类型自动转换
@EnableWebMvc
public class SpringMvcConfig {
}
```

```java
@Controller
public class UserController {

    @RequestMapping("/Param")
    @ResponseBody
    // 使用@RequestBody注解将外部传递的json数组数据映射到形参的集合对象中作为数据
    public String commonParam(@RequestBody List<String> likes) {
        System.out.println("likes-->" + likes);
        return "{'module': 'Param'}";
    }
}
```

PostMan请求如下：http://localhost/Param，并发送JSON数据

```json
["唱", "跳", "rap"]
```

![](..\图片\4-03【SpringMVC】\1-5.png)

**JSON对象数据**

```java
@Controller
public class UserController {

    @RequestMapping("/Param")
    @ResponseBody
    public String commonParam(@RequestBody User user) {
        System.out.println("user-->" + user);
        return "{'module': 'Param'}";
    }
}
```

PostMan请求如下：http://localhost/Param，并发送JSON数据

```json
{
    "name": "linxuan",
    "age": 20,
    "address": {
        "province": "handan", 
        "city": "handan"
    }
}
```

**JSON对象数组**

集合中保存多个POJO，前端使用JSON对象数组传参

```java
@Controller
public class UserController {

    @RequestMapping("/Param")
    @ResponseBody
    public String commonParam(@RequestBody List<User> list) {
        System.out.println("list-->" + list);
        return "{'module': 'Param'}";
    }
}
```

PostMan请求如下：http://localhost/Param，并发送JSON数据

```json
[
    {"name": "linxuan", "age": 20}, 
    {"name": "chenmuyang", "age": 18}
]
```

| 名称 | @EnableWebMvc                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 配置类注解，在SpringMVC配置类上方定义                        |
| 替换 | 替换掉了`<mvc:annotation-driven>`                            |
| 作用 | 开启SpringMVC的注解支持，有多项辅助功能。包含将JSON转换成对象的功能。标配，不要忘记。 |

| 名称 | @RequestBody                                                 |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String commonParam(@RequestBody User user)`          |
| 作用 | 将请求中请求体所包含的数据传递给请求参数，就是将前端的JSON数据映射到形参的对象中作为数据 |

> SpringMVC的配置类把`@EnableWebMvc`当做标配配置上去，不要省略

## 2.5 日期类型参数传递

接下来处理一种开发中比较常见的一种数据类型：日期类型。日期类型比较特殊，因为对于日期的格式有N多中输入方式，比如：2088-08-18、2088/08/18、08/18/2088。

**使用yyyy/MM/dd格式传输日期**

```java
@Controller
public class UserController {

    @RequestMapping("/dataParam")
    @ResponseBody
    public String dataParam(Date date) {
        System.out.println("data-->" + date);
        return "{'module': 'data Param'}";
    }
}
```

使用PostMan发送GET请求：http://localhost/dataParam?date=2088/08/08

控制台打印信息如下：`data-->Sun Aug 08 00:00:00 CST 2088`。前端传输这种日期格式是没有任何问题的，这是因为SpringMVC中默认支持的日期格式就是`yyyy/MM/dd`，但是一旦前端传输的日期格式不是这种，那么就会报错。

**使用其他日期格式传输日期**

```java
@Controller
public class UserController {

    @RequestMapping("/dataParam")
    @ResponseBody
    public String dataParam(Date date) {
        System.out.println("data-->" + date);
        return "{'module': 'data Param'}";
    }
}
```

使用PostMan发送GET请求：http://localhost/dataParam?date=2088-08-08

```apl
# 前端返回404，后台报错：
20-May-2022 14:58:11.261 警告 [http-nio-80-exec-2] org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver.logException Resolved [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Failed to convert value of type 'java.lang.String' to required type 'java.util.Date'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [java.util.Date] for value '2088-08-08'; nested exception is java.lang.IllegalArgumentException]
```

SpringMVC默认支持的字符串转日期的格式为`yyyy/MM/dd`，传递的日期格式如果不符合其默认格式，SpringMVC就无法进行格式转换，所以报错。解决方案也比较简单，需要使用`@DateTimeFormat`：

```java
@Controller
public class UserController {

    @RequestMapping("/dataParam")
    @ResponseBody
    // 前端传递日期格式不是SpringMVC默认支持的，所以这里用注解将日期格式表名
    public String dataParam(@DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        System.out.println("data-->" + date);
        return "{'module': 'data Param'}";
    }
}
```

重新启动服务器，重新发送请求测试，SpringMVC就可以正确的进行日期转换了

**携带时间的日期**

接下来我们来发送一个携带时间的日期

```java
@Controller
public class UserController {

    @RequestMapping("/dataParam")
    @ResponseBody
    // 使用DateTimeFormat注解将日期格式标明
    public String dataParam(@DateTimeFormat(pattern="yyyy/MM/dd HH:mm:ss") Date date) {
        System.out.println("data-->" + date);
        return "{'module': 'data Param'}";
    }
}
```

使用PostMan发送请求：http://localhost/dataParam?date=2088/08/08 8:08:08。没有任何问题。

| 名称 | @DateTimeFormat                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String dataParam(@DateTimeFormat(pattern="yyyy/MM/dd HH:mm:ss") Date date)` |
| 作用 | 设定日期时间型数据格式，如果前端传输日期格式不是yyyy/MM/dd，那么就需要使用它来指定日期格式。 |
| 属性 | pattern：指定日期时间格式字符串                              |

## 2.6 响应页面和数据

SpringMVC接收到请求和数据后，进行一些处理，当然这个处理可以是转发给Service，Service层再调用Dao层完成的，不管怎样，处理完以后，都需要将结果告知给用户。比如：根据用户ID查询用户信息、查询用户列表、新增用户等。

对于响应，主要就包含两部分内容：响应页面、响应数据。而响应数据又包括了响应文本数据以及响应json数据。因为异步调用是目前常用的主流方式，所以我们需要更关注的就是如何返回JSON数据，对于其他只需要认识了解即可。

环境介绍：

```xml
<!-- Maven工程导入依赖 -->
<dependencies>
    <!-- SpringMVC依赖环境 -->
    <!-- servlet依赖 -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <!-- 依赖作用范围未provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>
    <!-- 该jar包依赖于spring-context，所以不用导入Spring的环境 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- SpringMVC默认使用的是jackson来处理json的转换，所以需要在pom.xml添加jackson依赖 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.9.0</version>
    </dependency>
    
    <!-- 其他依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
        <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```groovy
// Gradle工程导入插件及依赖
plugins {
    id 'java'
    // 导入war插件，这是一个Web工程
    id 'war'
}

dependencies {
    // SpringMVC依赖环境
    // servlet依赖。依赖作用范围compileOnly，适用于编译期需要而不需要打包，避免和tomcat中依赖冲突
    compileOnly group: 'javax.servlet', name: 'javax.servlet-api', version: '3.1.0'
    // 导入SpringMVC依赖，该jar包依赖于spring-context，所以不用导入Spring的环境
    implementation group: 'org.springframework', name: 'spring-webmvc', version: '5.2.10.RELEASE'
    // SpringMVC默认使用的是jackson来处理json的转换，导入依赖坐标
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.9.0'
    
    // 导入其他依赖环境
    // 导入lombok依赖
    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    // annotationProcessor代表main下代码的注解执行器
    annotationProcessor group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
}
```

```java
package com.linxuan.config;

@Configuration
// 开启SpringMVC的注解支持，有多项辅助功能。包含将JSON转换成对象的功能。标配，不要忘记。
@EnableWebMvc
@ComponentScan("com.linxuan.controller")
public class SpringMvcConfig {   
}
```

```java
package com.linxuan.config;

public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

```java
package com.linxuan.pojo;

@Data
public class User {
    private String name;
    private int age;
}
```

**响应页面**

```jsp
<!-- 在webapp目录下面创建page.jsp文件，用于一会响应页面 -->
<html>
    <body>
        <h2>Hello Spring MVC!</h2>
    </body>
</html>
```

```java
@Controller
public class UserController {
    @RequestMapping("/toJumpPage")
    // 此处不能添加@ResponseBody，如果加了该注解会直接将page.jsp当字符串返回前端。方法返回值类型为String
    public String toJumpPage(){
        System.out.println("跳转页面");
        return "page.jsp";
    }
}
```

此处涉及到页面跳转，所以不适合采用PostMan进行测试，直接打开浏览器，输入http://localhost/toJumpPage。

**响应文本数据**

```java
@Controller
public class UserController {
    
   	@RequestMapping("/toText")
	// 注意此处该注解就不能省略，如果省略了，会把response text当前页面名称去查找，如果没有会报404错误
    @ResponseBody
    public String toText(){
        System.out.println("返回纯文本数据");
        return "response text";
    }
}
```

此处不涉及到页面跳转，使用PostMan进行测试：http://localhost/toText

**响应JSON数据**

首先来看一下响应POJO单个对象

```java
@Controller
public class UserController {
    
    @RequestMapping("/toJsonPOJO")
    @ResponseBody
    public User toJsonPOJO(){
        System.out.println("返回json对象数据");
        User user = new User();
        user.setName("linxuan");
        user.setAge(20);
        return user;
    }
}
```

设置返回值为实体类类型，即可实现返回对应对象的json数据。它依赖`@ResponseBody`注解和`@EnableWebMvc`注解。`@ResponseBody`注解表示返回的是数据，不是页面，不要解析。`@EnableWebMvc`注解会将返回的数据转为JSON格式返回给前端。使用PostMan进行测试：http://localhost/toJsonPOJO

再来看一下响应POJO集合对象：

```java
@Controller
public class UserController {
    
    @RequestMapping("/toJsonList")
    @ResponseBody
    public List<User> toJsonList(){
        System.out.println("返回json集合数据");
        User user1 = new User();
        user1.setName("林炫");
        user1.setAge(20);

        User user2 = new User();
        user2.setName("陈沐阳");
        user2.setAge(30);

        List<User> userList = new ArrayList<User>();
        userList.add(user1);
        userList.add(user2);

        return userList;
    }
}
```

使用PostMan进行测试：http://localhost/toJsonList。

设置返回值为对象（List对象或者其他对象都可以），即可实现返回对应对象的json数据。它依赖`@ResponseBody`注解和`@EnableWebMvc`注解。`@ResponseBody`注解表示返回的是数据，不是页面，不要解析。`@EnableWebMvc`注解会将返回的数据转为JSON格式返回给前端。

# 第三章 Rest风格

<!-- 新增POST、修改PUT -->

REST（Representational State Transfer），表现形式状态转换，它是一种软件架构风格。根据REST风格对资源进行访问称为RESTful。

REST的优点是：隐藏资源的访问行为，无法通过地址得知对资源是何种操作、书写简化。

当我们想表示一个网络资源的时候，可以使用两种方式：

* 传统风格资源描述形式：`http://localhost/user/getById?id=1` 查询id为1的用户信息，`http://localhost/user/saveUser` 保存用户信息。
* REST风格描述形式：`http://localhost/user/1` 或者`http://localhost/user`。

传统方式一般是一个请求url对应一种操作，这样做不仅麻烦，也不安全，因为如果读取请求url地址，就大概知道该url实现的是一个什么样的操作。而REST风格则大大不一样，请求地址变的简单了，并且光看请求URL并不是很能猜出来该URL的具体功能。

一个相同的url地址即可以是新增也可以是修改或者查询，我们区分的方式是按照Request的四种请求方式：查询`GET`，新增`POST`，修改`PUT`，删除`DELETE`。

上述行为是约定方式，约定不是规范，可以打破，所以称REST风格，而不是REST规范。描述模块的名称通常使用复数，也就是加s的格式描述，表示此类资源，而非单个资源，例如：users、books、accounts......

## 3.1 RESTful入门案例

环境介绍：

```xml
<!-- Maven工程导入依赖 -->
<dependencies>
    <!-- SpringMVC依赖环境 -->
    <!-- servlet依赖 -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <!-- 依赖作用范围未provided，主代码和测试代码有用，打包的时候不会导包。避免和tomcat中冲突 -->
        <scope>provided</scope>
    </dependency>
    <!-- 该jar包依赖于spring-context，所以不用导入Spring的环境 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.10.RELEASE</version>
    </dependency>
    <!-- SpringMVC默认使用的是jackson来处理json的转换，所以需要在pom.xml添加jackson依赖 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.9.0</version>
    </dependency>
    
    <!-- 其他依赖 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
        <!-- Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件，所以不用打包的时候包含 -->
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```groovy
// Gradle工程导入插件及依赖
plugins {
    id 'java'
    // 导入war插件，这是一个Web工程
    id 'war'
}

dependencies {
    // SpringMVC依赖环境
    // servlet依赖。依赖作用范围compileOnly，适用于编译期需要而不需要打包，避免和tomcat中依赖冲突
    compileOnly group: 'javax.servlet', name: 'javax.servlet-api', version: '3.1.0'
    // 导入SpringMVC依赖，该jar包依赖于spring-context，所以不用导入Spring的环境
    implementation group: 'org.springframework', name: 'spring-webmvc', version: '5.2.10.RELEASE'
    // SpringMVC默认使用的是jackson来处理json的转换，导入依赖坐标
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.9.0'
    
    // 导入其他依赖环境
    // 导入lombok依赖
    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    // annotationProcessor代表main下代码的注解执行器
    annotationProcessor group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
}
```

```java
@Configuration
// 开启SpringMVC的注解支持，有多项辅助功能。包含将JSON转换成对象的功能。标配，不要忘记。
@EnableWebMvc
@ComponentScan("com.linxuan.controller")
public class SpringMvcConfig {   
}
```

```java
public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

```java
@Data
public class User {
    private String name;
    private int age;
}
```

```java
@Data
public class Book {
    String name;
    double price;
}
```

### 3.2.1 新增POST

PostMan发送POST请求：http://localhost/users。如果发送的不是POST请求，比如发送GET请求，则会报错。


```java
@Controller
public class UserController {
    // 设置请求路径为/users，设置当前请求方法为POST，表示REST风格中的新增操作
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public String save() {
        System.out.println("user save...");
        return "{'module': 'user save'}";
    }
}
```

### 3.2.2 删除DELETE

PostMan发送DELETE请求：http://localhost/users/1。路径中的`1`就是我们想要传递的参数。

```java
@Controller
public class UserController {
    // 设置当前请求方法为DELETE，表示REST风格中的删除操作，修改value属性值为/users/{id}，为了和路径相匹配。
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    // 方法形参前面添加@PathVariable注解，该注解表示RESTful风格传递路径参数，要求路径参数名与形参名一一对应
    public String delete(@PathVariable Integer id) {
        System.out.println("user delete..." + id);
        return "{'module': 'user delete'}";
    }
}
```

如果有多个参数需要传递，那么前端发送请求的时候可以使用：`http://localhost/users/1/tom`，路径中的`1`和`tom`就是我们想要传递的两个参数。

```java
@Controller
public class UserController {
    // 设置当前请求方法为DELETE，表示REST风格中的删除操作
    @RequestMapping(value = "/users/{id}/{name}", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@PathVariable Integer id, @PathVariable String name) {
        System.out.println("user delete..." + id+"，"+name);
        return "{'module': 'user delete'}";
    }
}
```

| 名称 | @PathVariable                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String delete(@PathVariable Integer id)`             |
| 作用 | RESTful风格传递路径参数，绑定路径参数与后端方法形参间的关系，要求路径参数名与形参名一一对应 |

### 3.2.3 修改PUT

PostMan发送PUT请求：http://localhost/users，并且携带参数

```json
{
    "name":"林炫",
    "age":20
}
```

```java
@Controller
public class UserController {
    // 设置当前请求方法为PUT，表示REST风格中的修改操作
    @RequestMapping(value = "/users"，method = RequestMethod.PUT)
    @ResponseBody
    public String update(@RequestBody User user) {
        System.out.println("user update..." + user);
        return "{'module':'user update'}";
    }
}
```

### 3.2.4 根据ID查询GET

PostMan发送GET请求：http://localhost/users/666

```java
@Controller
public class UserController {
    // 设置当前请求方法为GET，表示REST风格中的查询操作
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    // 方法形参前面添加@PathVariable注解，该注解表示RESTful风格传递路径参数，要求路径参数名与形参名一一对应
    public String getById(@PathVariable Integer id){
        System.out.println("user getById..." + id);
        return "{'module': 'user getById'}";
    }
}
```

### 3.2.5 查询所有GET

PostMan发送GET请求：http://localhost/users

```java
@Controller
public class UserController {
    // 设置当前请求方法为GET，表示REST风格中的查询操作
    @RequestMapping(value = "/users" ，method = RequestMethod.GET)
    @ResponseBody
    public String getAll() {
        System.out.println("user getAll...");
        return "{'module': 'user getAll'}";
    }
}
```

## 3.2 接收参数注解对比

关于接收参数，我们学过三个注解`@RequestBody`、`@RequestParam`、`@PathVariable`，接下来来看一下他们：

| 名称 | @RequestBody                                                 |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String commonParam(@RequestBody User user)`          |
| 作用 | 将请求中请求体所包含的数据传递给请求参数，就是将前端的JSON数据映射到形参的对象中作为数据 |

| 名称 | @RequestParam                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义。                |
| 示例 | `public String commonParam(@RequestParam("name") String userName)` |
| 作用 | 绑定请求参数与处理器方法形参间的关系，前端参数和后端形参变量名不一样的时候用该注解。 |
| 属性 | required是否为必传参数、defaultValue参数默认值。             |

| 名称 | @PathVariable                                                |
| ---- | ------------------------------------------------------------ |
| 类型 | 形参注解，在SpringMVC控制器方法形参前面定义                  |
| 示例 | `public String delete(@PathVariable Integer id)`             |
| 作用 | RESTful风格传递路径参数，绑定路径参数与后端方法形参间的关系，要求路径参数名与形参名一一对应 |

区别：

* `@RequestBody`：用于接收json数据，我们可以想想`@ResponseBody`注解，就是返回JSON数据的。
* `@RequestParam`：用于接收url地址传参或表单传参，如果前端传过来参数名称与后端形参名称不一致，那么需要该注解。
* `@PathVariable`：用于接收RESTful开发路径参数，使用`{参数名称}`描述路径参数。

应用：

* 后期开发中，发送请求参数超过1个时，以json格式为主，`@RequestBody`应用较广
* 如果发送非json格式数据，选用`@RequestParam`接收请求参数
* 采用RESTful进行开发，当参数数量较少时，例如1个，可以采用`@PathVariable`接收请求路径变量，通常用于传递id值

## 3.3 RESTful快速开发

对于上述的RESTful的开发，我们会发现好麻烦，为此我们也有解决方案：

1. 每个方法的`@RequestMapping`注解中都定义了访问路径`/users`，重复性太高。可以将`@RequestMapping`提到类上面，用来定义所有方法共同的访问路径。

2. 每个方法的`@RequestMapping`注解都要使用method属性定义请求方式，重复性太高。可以使用`@GetMapping`、`@PostMapping`、`@PutMapping`、`@DeleteMapping`代替

3. 每个方法响应数据为json格式都需要加上`@ResponseBody`注解，重复性太高。可以将`ResponseBody`提到类上面，让所有的方法都有`@ResponseBody`的功能。

4. 使用`@RestController`注解替换`@Controller`与`@ResponseBody`注解，简化书写

```java
// 等于@Controller + ResponseBody
@RestController 
@RequestMapping("/users")
public class BookController {
    
	// 等于@RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public String save(@RequestBody Book book){
        System.out.println("book save..." + book);
        return "{'module': 'book save'}";
    }

    // 等于@RequestMapping(value = "/{id}"，method = RequestMethod.DELETE)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id){
        System.out.println("book delete..." + id);
        return "{'module': 'book delete'}";
    }

    // 等于@RequestMapping(method = RequestMethod.PUT)
    @PutMapping
    public String update(@RequestBody Book book){
        System.out.println("book update..." + book);
        return "{'module': 'book update'}";
    }

    // 等于@RequestMapping(value = "/{id}"，method = RequestMethod.GET)
    @GetMapping("/{id}")
    public String getById(@PathVariable Integer id){
        System.out.println("book getById..." + id);
        return "{'module': 'book getById'}";
    }

    // 等于@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public String getAll(){
        System.out.println("book getAll...");
        return "{'module': 'book getAll'}";
    }
    
}
```

| 名称 | @RestController                                              |
| ---- | ------------------------------------------------------------ |
| 类型 | 类注解，基于SpringMVC的RESTful开发控制器类上方定义           |
| 替换 | 替换掉了@Controller与@ResponseBody两个注解                   |
| 作用 | 设置当前控制器类为RESTful风格。该类交由Spring管理，所有方法返回值作为响应体，无需解析。 |

| 名称 | @GetMapping/@PostMapping/@PutMapping/@DeleteMapping          |
| ---- | ------------------------------------------------------------ |
| 类型 | 方法注解，基于SpringMVC的RESTful开发控制器方法上方定义。例如`@GetMapping("/{id}")` |
| 作用 | 设置当前控制器方法请求访问路径与请求动作，每种对应一个请求动作。@GetMapping对应GET请求 |
| 属性 | value（默认）：请求访问路径                                  |



