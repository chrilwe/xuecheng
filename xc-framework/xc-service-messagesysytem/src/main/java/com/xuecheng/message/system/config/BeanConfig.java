package com.xuecheng.message.system.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xuecheng.framework.domain.message.MessageMQList;
import com.xuecheng.message.system.rabbitmq.MessageConsumer;
import com.xuecheng.message.system.task.BeanUtils;
import com.xuecheng.message.system.task.TaskThreadPool;

@Configuration
public class BeanConfig {
	
	@Bean
	public MyServletContextListener myServletContextListener() {
		return new MyServletContextListener();
	}
	
	private class MyServletContextListener implements ServletContextListener {

		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void contextInitialized(ServletContextEvent arg0) {
			//线程池的初始化
			TaskThreadPool.getInstance();
			//开启消息监听
			BeanUtils<MessageConsumer> beanUtil = new BeanUtils<MessageConsumer>();
			MessageConsumer messageConsumer = beanUtil.getBean();
			messageConsumer.addCourseToEsListener(MessageMQList.EXCHANGE_TOPIC_INFORM, MessageMQList.QUEUE_INFORM_ADDCOURSETOES, MessageMQList.ROUTINGKEY_ADDCOURSETOES);
		}
		
	}
}
