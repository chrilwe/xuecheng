package com.xuecheng.message.system.service;

public interface HandlerMessageService {
	//处理待发送状态消息超时任务
	public void handlerReadySendindMessage();
	//处理发送中状态消息超时任务
	public void handlerSendingMessage();
}
