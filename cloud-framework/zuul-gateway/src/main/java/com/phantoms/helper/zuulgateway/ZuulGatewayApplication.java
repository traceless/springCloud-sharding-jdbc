package com.phantoms.helper.zuulgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 *  zuul 网关
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年11月28日 	Created
 *
 * </pre>
 * @since 1.
 */

// @EnableEurekaClient 只适用于Eureka作为注册中心
// @EnableDiscoveryClient 可以是其他注册中心。
// @EnableZuulProxy 包含了 @EnableCircuitBreaker和 @EnableDiscoveryClient（Edgware版本后 2.0后不需要EnableDiscoveryClient了 ），代替了 @EnableZuulServer
// @EnableZuulServer 
//注解@SpringCloudApplication包括：@SpringBootApplication、@EnableDiscoveryClient、@EnableCircuitBreaker（熔断器），分别是SpringBoot注解、注册服务中心Eureka注解、断路器注解。
//从Spring Cloud Edgware开始，@EnableDiscoveryClient 或@EnableEurekaClient 可省略。只需加上相关依赖，并进行相应配置，即可将微服务注册到服务发现组件上。
@EnableZuulProxy
//不需要数据库配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ZuulGatewayApplication {

    private final static Logger logger = LoggerFactory.getLogger(ZuulGatewayApplication.class);

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ZuulGatewayApplication.class, args);
	}
}
