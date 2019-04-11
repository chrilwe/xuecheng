package com.xuecheng.learning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages="com.xuecheng.learning.mapper")
@EnableEurekaClient
@ComponentScan(value="com.xuecheng.learning")
@EnableDiscoveryClient
@EnableFeignClients(basePackages="com.xuecheng.learning.client")
public class CourseLearningApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CourseLearningApplication.class, args);
	}

}
