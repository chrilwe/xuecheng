package com.xuecheng.message.system.service;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.message.Message;

public interface MessageService {
	// 预发送消息(消息准备发送中)
	public ResponseResult readySendMessage(Message message);

	// 确认发送消息(消息正在发送中)
	public ResponseResult confirmSendMessage(String messageId);

	// 确认消息已经被消费(消息已发送)
	public ResponseResult receivedMessage(String messageId);

	// 重新发送消息
	public ResponseResult resendMessage(Message message);
	
	//删除消息
	public ResponseResult deleteById(String messageId);
	
	//根据id查询
	public Message findById(String messageId);
}
