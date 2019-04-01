package com.xuecheng.manager.cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.xuecheng.manager.cms_client.client")
@EnableDiscoveryClient
public class CmsManagerClientApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CmsManagerClientApplication.class, args);
	}

}
