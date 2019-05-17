package com.xuecheng.message.system.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.message.Message;
import com.xuecheng.framework.domain.message.MessageStatus;
import com.xuecheng.framework.domain.message.response.MessageCode;
import com.xuecheng.framework.utils.ZkSessionUtil;
import com.xuecheng.message.system.mapper.MessageMapper;
import com.xuecheng.message.system.rabbitmq.MessageSender;
import com.xuecheng.message.system.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private MessageSender messageSender;
	

	@Override
	@Transactional
	public ResponseResult readySendMessage(Message message) {
		if(message == null || StringUtils.isEmpty(message.getId())) {
			ExceptionCast.cast(MessageCode.MESSAGE_ID_NULL);
		}
		
		Message m = messageMapper.findById(message.getId());
		if(message != null) {
			ExceptionCast.cast(MessageCode.MESSAGE_ALREADY_EXISTS);
		}
		message.setStatus(MessageStatus.READY_SENDING);
		message.setAreadyDead("NO");
		message.setCreateTime(new Date());
		message.setResendTime(0);
		message.setVersion(1);
		messageMapper.add(message);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	@Transactional
	public ResponseResult confirmSendMessage(String messageId) {
		Message message = messageMapper.findById(messageId);
		if(message == null) {
			ExceptionCast.cast(MessageCode.MESSAGE_ISNOT_EXISTS);
		}
		//查看消息状态，只对待发送状态的消息有效，防止修改了其他状态的消息
		String status = message.getStatus();
		if(!status.equals(MessageStatus.READY_SENDING)) {
			ExceptionCast.cast(MessageCode.MESSAGE_STATUS_ERROR);
		}
		message.setStatus(MessageStatus.SENDING);
		messageMapper.update(message);
		//将消息id发送到消息队列
		messageSender.sendMessage(message.getRabbitExchange(), message.getRabbitQueue(), message.getRoutingKey(), messageId);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	@Transactional
	public ResponseResult receivedMessage(String messageId) {
		if(StringUtils.isEmpty(messageId)) {
			ExceptionCast.cast(MessageCode.MESSAGE_ID_NULL);
		}
		Message message = messageMapper.findById(messageId);
		if(message == null) {
			ExceptionCast.cast(MessageCode.MESSAGE_ISNOT_EXISTS);
		}
		String status = message.getStatus();
		if(!status.equals(MessageStatus.SENDING)) {
			ExceptionCast.cast(MessageCode.MESSAGE_STATUS_ERROR);
		}
		message.setStatus(MessageStatus.FINISHED_SENDING);
		messageMapper.update(message);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	@Transactional
	public ResponseResult resendMessage(Message message) {
		if(message == null || StringUtils.isEmpty(message.getId())) {
			ExceptionCast.cast(MessageCode.MESSAGE_ID_NULL);
		}
		
		//重新发送，发送中超时的消息进行重新发送，其他状态不进行重新发送
		Message m = messageMapper.findById(message.getId());
		//判断该消息是否已死亡
		String areadyDead = m.getAreadyDead();
		if(areadyDead.equals("YES")) {
			ExceptionCast.cast(MessageCode.MESSAGE_ALREAD_DEAD);
		}
		String status = m.getStatus();
		if(!status.equals(MessageStatus.SENDING)) {
			ExceptionCast.cast(MessageCode.MESSAGE_STATUS_ERROR);
		}
		
		int resendTime = m.getResendTime();
		//判断是否超过5次重发，超过5次直接进入死亡消息
		if(resendTime > 5) {
			m.setAreadyDead("YES");
		}else {
			m.setResendTime(resendTime + 1);
		}
		m.setStatus(MessageStatus.SENDING);
		int update = messageMapper.update(message);
		if(update <= 0) {
			return null;
		}
		//将消息id发送到消息队列
		messageSender.sendMessage(message.getRabbitExchange(), message.getRabbitQueue(), message.getRoutingKey(), message.getId());
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	@Transactional
	public ResponseResult deleteById(String messageId) {
		int deleteById = messageMapper.deleteById(messageId);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	public Message findById(String messageId) {
		if(StringUtils.isEmpty(messageId)) {
			ExceptionCast.cast(MessageCode.MESSAGE_ID_NULL);
		}
		return messageMapper.findById(messageId);
	}
	
}
