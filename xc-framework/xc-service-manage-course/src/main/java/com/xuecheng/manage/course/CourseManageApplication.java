package com.xuecheng.manage.course;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.xuecheng.manage.course.mapper")//扫描mapper
public class CourseManageApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CourseManageApplication.class, args);
	}

}
