package com.xuecheng.message.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.MessageControllerApi;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.message.Message;
import com.xuecheng.message.system.service.MessageService;

@RestController
@RequestMapping("/message/system")
public class MessageController implements MessageControllerApi {
	
	@Autowired
	private MessageService messageService;
	
	/**
	 * 消息预发送(待发送)
	 */
	@Override
	@GetMapping("/ready_send_message")
	public ResponseResult readySendMessage(Message message) {
		
		return messageService.readySendMessage(message);
	}
	
	/**
	 * 消息发送（发送中）
	 */
	@Override
	@GetMapping("/confirm_send_message")
	public ResponseResult confirmSendMessage(String messageId) {
		
		return messageService.confirmSendMessage(messageId);
	}
	
	/**
	 * 确认消息（已发送）
	 */
	@Override
	@GetMapping("/received_message")
	public ResponseResult receivedMessage(String messageId) {
		
		return messageService.receivedMessage(messageId);
	}
	
	/**
	 * 重新发送消息
	 */
	@Override
	@GetMapping("/resend_message")
	public ResponseResult resendMessage(Message message) {
		
		return messageService.resendMessage(message);
	}
	
	/**
	 * 根据消息id查询消息
	 */
	@Override
	@GetMapping("/findById/{messageId}")
	public Message findById(@PathVariable("messageId")String messageId) {
		
		return messageService.findById(messageId);
	}

}
