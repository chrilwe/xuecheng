package com.xuecheng.message.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.message.Message;

public interface MessageMapper {
	public int add(Message message);
	public int update(Message message);
	public Message findById(String messageId);
	public List<Message> findListByStatus(@Param("start")int start,@Param("size")int size,@Param("status")String status);
	public int countReadySendingStatus();
	public int countSendingStatus();
	public int deleteById(String messageId);
}
