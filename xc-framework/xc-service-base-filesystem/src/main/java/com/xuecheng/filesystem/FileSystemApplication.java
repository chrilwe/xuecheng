package com.xuecheng.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages="com.xuecheng.filesystem")//扫描本项目下的所有包
public class FileSystemApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(FileSystemApplication.class, args);
	}

}
