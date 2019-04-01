package com.xuecheng.service.manage.media.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@ComponentScan(basePackages="com.xuecheng.service.manage.media.processor")
public class MediaProcessorApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MediaProcessorApplication.class, args);
	}

}
