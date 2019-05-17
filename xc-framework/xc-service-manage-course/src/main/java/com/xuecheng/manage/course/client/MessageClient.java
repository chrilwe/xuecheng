package com.xuecheng.manage.course.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.message.Message;

@FeignClient(XcServiceList.XC_SERVICE_MESSAGESYSTEM)
public interface MessageClient {
	/**
	 * 消息预发送(待发送)
	 */
	@GetMapping("/message/system/ready_send_message")
	public ResponseResult readySendMessage(Message message);
	
	/**
	 * 消息发送（发送中）
	 */
	@GetMapping("/message/system/confirm_send_message")
	public ResponseResult confirmSendMessage(String messageId);
	
	/**
	 * 确认消息（已发送）
	 */
	@GetMapping("/message/system/received_message")
	public ResponseResult receivedMessage(String messageId);
	
	/**
	 * 重新发送消息
	 */
	@GetMapping("/message/system/resend_message")
	public ResponseResult resendMessage(Message message);
}
