package com.xuecheng.center.order.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 从spring容器中获取已注册的类
 * @author Administrator
 *
 */
public class SpringBeanUtil implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public <T> T getBean(Class<T> classType) {
		return applicationContext.getBean(classType);
	}
	
	private static class Singleton {
		private static SpringBeanUtil util = null;
		static {
			util = new SpringBeanUtil();
		}
		private static SpringBeanUtil getInstance() {
			return util;
		}
	}
	
	public static SpringBeanUtil getInstance() {
		return Singleton.getInstance();
	}
}
