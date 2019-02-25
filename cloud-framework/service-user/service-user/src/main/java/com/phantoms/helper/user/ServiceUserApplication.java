package com.phantoms.helper.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@EnableFeignClients(basePackages = { "com.phantoms.helper.*.serviceapi" })
@ComponentScan(value = { "com.phantoms.framework.cloudbase.configuration",
                         "com.phantoms.framework.cloudbase.dbConfig",
                         "com.phantoms.helper.*.serviceapi.feign.hystrix"})
//扫描自己默认包路径
@ComponentScan 
public class ServiceUserApplication {

    private final static Logger logger = LoggerFactory.getLogger(ServiceUserApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceUserApplication.class, args);
	}
}
