package com.xuecheng.govern.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ConfigApplication.class, args);
	}

}
