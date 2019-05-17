package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.message.Message;

public interface MessageControllerApi {
	//预发送消息(消息准备发送中)
	public ResponseResult readySendMessage(Message message);
	//确认发送消息(消息正在发送中)
	public ResponseResult confirmSendMessage(String messageId);
	//确认消息已经被消费(消息已发送)
	public ResponseResult receivedMessage(String messageId);
	//重新发送消息
	public ResponseResult resendMessage(Message message);
	//根据消息id查询消息
	public Message findById(String messageId);
}
