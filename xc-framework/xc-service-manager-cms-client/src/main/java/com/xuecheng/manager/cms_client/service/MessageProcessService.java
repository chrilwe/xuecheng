package com.xuecheng.manager.cms_client.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.xuecheng.manager.cms_client.queue.Router;

@Service
public class MessageProcessService {
	
	/**
	 * 门户页面处理
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(queues={"queue_inform_protal"})
	public void queue_inform_protal(String msg, Message message, Channel channel) {
		//将消息放到队列集合中，让多个线程 去处理生成静态页面任务
		Router router = new Router(msg);
		router.rout();
	}
	
	/**
	 * 课程详情页处理
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(queues={"queue_inform_course"})
	public void queue_inform_course(String msg, Message message, Channel channel) {
		//将消息放到队列集合中，让多个线程 去处理生成静态页面任务
		Router router = new Router(msg);
		router.rout();
	}
}
