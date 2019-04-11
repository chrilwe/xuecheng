package com.xuecheng.manage.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.xuecheng.manage.media")
@EnableEurekaClient
@EnableFeignClients(basePackages="com.xuecheng.manage.media.client")
public class MediaManageApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MediaManageApplication.class, args);
	}

}
