package com.commonModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CommonModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonModuleApplication.class, args);
	}

}
