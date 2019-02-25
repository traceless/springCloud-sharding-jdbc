package com.phantoms.helper.user.serviceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ServiceUserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceUserApiApplication.class, args);
	}
}
