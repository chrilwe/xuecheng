package com.xuecheng.message.system.task;

import java.lang.reflect.ParameterizedType;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils<T> implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext =applicationContext;
	}
	
	public T getBean() {
		return applicationContext.getBean((Class<T>) ((ParameterizedType)new BeanUtils().getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
}
