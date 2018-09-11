# Spring-Cloud-Zuul

Routing is an integral part of a microservice architecture. For example, / may be mapped to your web application, /api/users is mapped to the user service and /api/shop is mapped to the shop service. Zuul is a JVM-based router and server-side load balancer from Netflix. Netflix uses Zuul for the following: Authentication Insights Stress Testing Canary Testing Dynamic Routing Service Migration Load Shedding Security Static Response handling Active/Active traffic management Zuul’s rule engine lets rules and filters be written in essentially any JVM language, with built-in support for Java and Groovy. https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html 这是官网对它的解释 

一、Zuul路由网关简介
路由是微服务体系结构的一个组成部分，为了统一给外服务提供统一的restful风格，Zuul是来自Netflix的基于JVM的路由器和服务器端负载均衡器。它有两大功能 分别是路由转发和过滤器。下面我们进行实际操作 ！

二、Zuul实际操作 
 1、zuul路由转发 
①首先我们要用到我们之前的三个项目，eureka-server，eureka-provider-service1,eureka-provider-service2 一个服务注册中心，两个服务提供者。 为了分辨这两个服务提供者，我们把实例名称改一下。 

②我们创建一个新的项目，名称叫eureka-zuul，pom.xml文件如下 

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>eureka-zuul</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>eureka-zuul</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.16.BUILD-SNAPSHOT</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Edgware.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

    <pluginRepositories>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>


</project>
③配置文件如下

#实例名称
spring:
  application:
    name: eureka-zuul
#端口号
server:
  port: 8201

#服务注册中心地址
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8001/eureka/

zuul:
  routes:
    order:
      path: /order/**
      serviceId: eureka-provider-order
    account:
      path: /account/**
      serviceId: eureka-provider-account
稍微解释一下zuul的配置文件

zuul下边routes是路由，再往下是自定义的内容，我这里定义了一个order和account，path是以order和account开头的下边所有的方法，实例id就是上一节用到的两个服务。
④应用主类开启Zuul注解并把自己注册到服务注册中心

@EnableZuulProxy
package com.example.eurekazuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaZuulApplication.class, args);
    }
}
⑤我们依次启动这几次服务，然后访问zuul这个服务

http://localhost:8201/order/order和http://localhost:8201/account/account





(如第一个order是路由，第二个是请求路径，如有不懂的看上节代码。这里贴图给看一下order的controller)



这样路由转换就完成了是不是特别简单？？？

总结一下：

配置文件 serviceId path 第一个是实例名称，就是再配置文件里配置的spring.application.name

第二个是路由路径。能访问的路由权限。比如这里是 /order/** 那就是可以访问这个服务下所有的服务。

二、过滤器
zuul不仅仅可以做路由转发，还可以做权限过滤器，比如你要通过zuul调用下边的服务，你需要令牌校验才可以调用，当然我们完全可以使用springmvc的Interceptor或者其他拦截器。我们在这里讲解一下zuul的过滤器

①首先我们先写一个校验类TokenFilter继承ZuulFilter重写它的方法 代码如下

package com.example.eurekazuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: MrWang
 * @date: 2018/9/11
 */


public class TokenFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String accessToken = request.getParameter("accessToken");
        if (accessToken==null){
            log.warn("accessToken is empty");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(401);
            return null;
        }
        log.info("accessToken ok");
        return null;
    }
}
②各个参数的解释

1、filterType();按类型分类过滤器。Zuul的标准类型是“pre”用于预路由过滤，
路由表示路由到原点，“post”表示路由后过滤器，“error”表示错误处理。
我们还支持静态响应的“静态”类型，参见StaticResponseFilter。
通过调用FilterProcessor.runFilters(类型)来创建或添加和运行任何filterType

(说白了就是它的几大特征，默认标准是pre，在路由过滤之前。)
2、filterOrder();还必须为过滤器定义filterOrder()。如果优先级不是，则过滤器可能具有相同的过滤顺序
对过滤器来说很重要。filterorder不需要是连续的。

（就是如果一个方法存在多个过滤器，执行的先后顺序）

3、shouldFilter()这里可以写逻辑判断，是否要过滤，这里是true,永久过滤。

 --------------------------------解释来自源码

③这里我在run里写的一些简单的逻辑校验

那么我们现在重启这个项目，重新访问http://localhost:8201/order/order 网页提示401错误，如图

那我们现在传入正确的accessToken试试看

​

嗯访问成功了。

这就是zuul的过滤器，是不是很简单？？？



