package com.xuecheng.manager.cms_client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.manager.cms_client.client.CmsPageClient;
import com.xuecheng.manager.cms_client.client.SearchClient;
import com.xuecheng.manager.cms_client.queue.Router;

@Service
public class MessageProcessService {
	
	@Autowired
	private SearchClient searchClient;
	@Autowired
	private CmsPageClient cmsPageClient;
	@Autowired
	private RestTemplate restTemplate;
	
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
