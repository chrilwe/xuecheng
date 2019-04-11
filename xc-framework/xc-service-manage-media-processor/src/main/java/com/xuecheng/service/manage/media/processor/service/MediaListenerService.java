package com.xuecheng.service.manage.media.processor.service;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

@Service
public class MediaListenerService {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private MediaProcessorService mediaProcessorService;
	
	/**
	 * 媒体文件处理
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(queues={"queue_inform_media"})
	public void listenProcessVideo(String msg, Message message, Channel channel) {
		//监听消息，获取处理视频文件的fieMd5值
		mediaProcessorService.processMedia(msg);
	}
	
	/**
	 * 文档文件处理
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(queues={"queue_inform_doc"})
	public void listenProcessDoc(String msg, Message message, Channel channel) {
		//监听消息，获取处理视频文件的fieMd5值
		//TODO
	}
	
}
