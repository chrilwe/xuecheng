package com.xuecheng.manager.cms_client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FeginConfig {
	
	@Bean
	public RestTemplate restTemplate() {
		
		return new RestTemplate();
	}
}
