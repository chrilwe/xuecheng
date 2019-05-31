package com.xuecheng.paysystem.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.message.Message;
@FeignClient(XcServiceList.XC_SERVICE_MESSAGESYSTEM)
@RequestMapping("/message/system")
public interface XcMessageClient {
	
	/**
	 * 消息预发送(待发送)
	 */
	@GetMapping("/ready_send_message")
	public ResponseResult readySendMessage(Message message);
	
	/**
	 * 消息发送（发送中）
	 */
	@GetMapping("/confirm_send_message")
	public ResponseResult confirmSendMessage(String messageId);
}
