package com.phantoms.helper.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@EnableFeignClients(basePackages = { "com.phantoms.helper.*.serviceapi" })
@ComponentScan(value = { "com.phantoms.framework.cloudbase.configuration",
                         "com.phantoms.framework.cloudbase.dbConfig",
                         "com.phantoms.helper.*.serviceapi.feign.hystrix"})
@ComponentScan
public class ServicePaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicePaymentApplication.class, args);
	}
}
