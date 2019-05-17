package com.xuecheng.center.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix="xuecheng.order")
public class OrderConfiguration {
	public static long order_pay_no_ttl;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
