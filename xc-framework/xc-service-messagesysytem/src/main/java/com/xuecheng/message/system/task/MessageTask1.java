package com.xuecheng.message.system.task;

import com.xuecheng.message.system.service.HandlerMessageService;
import com.xuecheng.message.system.service.MessageService;

public class MessageTask1 implements Runnable {

	@Override
	public void run() {
		try {
			//定时调度
			BeanUtils<HandlerMessageService> beanUtil = new BeanUtils<HandlerMessageService>();
			HandlerMessageService handlerMessageService = beanUtil.getBean();
			handlerMessageService.handlerReadySendindMessage();
			Thread.sleep(60*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
