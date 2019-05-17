package com.xuecheng.message.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.message.Message;
import com.xuecheng.framework.domain.message.MessageStatus;
import com.xuecheng.message.system.client.CourseClient;
import com.xuecheng.message.system.mapper.MessageMapper;
import com.xuecheng.message.system.service.HandlerMessageService;
import com.xuecheng.message.system.service.MessageService;

@Service
public class HandlerMessageServiceImpl implements HandlerMessageService {
	private static final int size = 100;//每次分页大小
	private static final int timeout = 100;//过期时间
	
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private MessageService messageService;
	@Autowired
	private CourseClient courseClient;

	@Override
	public void handlerReadySendindMessage() {
		//查看业务状态是否已经改变，如果改变了重新发送消息,如果未改变删除消息记录
		// 分页查询记录,每次100条记录
		int count = messageMapper.countReadySendingStatus();
		int length = count % size == 0?count / size:count / size + 1;
		for(int i = 0;i < length; i++) {
			List<Message> list = messageMapper.findListByStatus((i-1)*size, size, MessageStatus.READY_SENDING);
		//处理查询到 的100条记录
			for (Message message : list) {
				Date createTime = message.getCreateTime();
				Date currentTime = new Date();
				//相隔时间(秒)
				long time = (currentTime.getTime() - createTime.getTime())/1000;
				if(time > timeout) {
					//查看业务代码状态是否已经改变
					CourseBase courseBase = courseClient.findCourseBaseByCourseid(message.getMessageBody());
					if(courseBase.getStatus().equals("202002")) {
						//重新发送消息到消息队列
						messageService.resendMessage(message);
					} else if(courseBase.getStatus().equals("202001")) {
						//状态未发生变化，将消息删除
						messageService.deleteById(message.getId());
					}
				}
			}
		}
	}

	@Override
	public void handlerSendingMessage() {
		//分页查询发送中状态的消息
		int count = messageMapper.countSendingStatus();
		int length = count % size == 0?count / size:count / size + 1;
		for(int i = 0;i < length; i++) {
			List<Message> list = messageMapper.findListByStatus((i-1)*size, size, MessageStatus.SENDING);
			for (Message message : list) {
				Date createTime = message.getCreateTime();
				Date currentTime = new Date();
				long time = (currentTime.getTime() - createTime.getTime())/1000;
				if(time > size) {
					//超时的消息，重新发送消息
					messageService.resendMessage(message);
				}
			}
		}
	}

}
