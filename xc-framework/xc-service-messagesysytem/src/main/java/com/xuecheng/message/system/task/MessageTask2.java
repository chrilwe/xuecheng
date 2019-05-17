package com.xuecheng.message.system.task;

import com.xuecheng.message.system.service.HandlerMessageService;

public class MessageTask2 implements Runnable {

	@Override
	public void run() {
		try {
			//定时调度
			BeanUtils<HandlerMessageService> beanUtil = new BeanUtils<HandlerMessageService>();
			HandlerMessageService handlerMessageService = beanUtil.getBean();
			handlerMessageService.handlerSendingMessage();
			Thread.sleep(60*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
