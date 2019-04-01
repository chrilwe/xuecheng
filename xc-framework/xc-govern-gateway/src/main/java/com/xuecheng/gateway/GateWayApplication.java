package com.xuecheng.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class GateWayApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(GateWayApplication.class, args);
	}

}
