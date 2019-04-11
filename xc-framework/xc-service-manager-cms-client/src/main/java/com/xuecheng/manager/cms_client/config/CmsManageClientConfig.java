package com.xuecheng.manager.cms_client.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xuecheng.manager.cms_client.thread.WorkThreadPool;

@Configuration
public class CmsManageClientConfig {
	
	@Bean
	public WorkThreadPool initBean() {
		
		return WorkThreadPool.getInstance();
	}
	
}
