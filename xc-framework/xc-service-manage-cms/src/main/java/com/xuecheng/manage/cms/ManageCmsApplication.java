package com.xuecheng.manage.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages="com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages="com.xuecheng.manage.cms")//扫描本项目下的所有包
@EntityScan(basePackages="com.xuecheng.framework.domain.cms")//扫描实体类
@EnableEurekaClient
public class ManageCmsApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ManageCmsApplication.class, args);
	}
}
