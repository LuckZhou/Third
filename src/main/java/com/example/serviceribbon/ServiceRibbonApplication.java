package com.example.serviceribbon;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

/**
 * 通过@EnableDiscoveryClient向服务中心注册；并且向程序的ioc注入一个bean：restTemplate;
 * 并通过@LoadBalanced注解表明这个restRemplate开启负载均衡的功能
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix                  //加EnableHystrix注解开启Hystrix
@EnableHystrixDashboard         //Hystrix Dashboard (断路器：Hystrix 仪表盘)
public class ServiceRibbonApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServiceRibbonApplication.class, args);
    }

    @Bean           //向程序的ioc注入一个bean：restTemplate
    @LoadBalanced   //表示这个restTemplate开启负载均衡的功能，ribbon控制的是负载均衡
    RestTemplate restTemplate(){
        return  new RestTemplate();
    }



    /**
     * Hystrix报错：Unable to connect to Command Metric Stream
     * 最新的springclound需要加下面这个代码 访问 http://localhost:8764/hystrix
     * 输入 http://localhost:8764/hystrix.stream
     * 新打开一个页面 刷新http://localhost:8764/hi?name=forezp 仪表盘就会动
     *
     */
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

}
