package com.xuecheng.ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages="com.xuecheng.ucenter.mapper")
public class UcenterApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(UcenterApplication.class, args);
	}

}
