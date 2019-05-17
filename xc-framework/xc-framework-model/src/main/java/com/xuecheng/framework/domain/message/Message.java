package com.xuecheng.framework.domain.message;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private String id;
	private String messageBody;//(消息内容)
	private String messageType;//(消息数据类型)
	private Date createTime;
	private int resendTime;//（重发次数，超过5次进入死亡）
	private String areadyDead;//(YES,NO)
	private String status;//消息状态（待发送，发送中，已发送）
	private String rabbitExchange;//交换机名称
	private String rabbitQueue;//队列名称
	private String routingKey;//路由key
	private int version;//版本号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getResendTime() {
		return resendTime;
	}
	public void setResendTime(int resendTime) {
		this.resendTime = resendTime;
	}
	public String getAreadyDead() {
		return areadyDead;
	}
	public void setAreadyDead(String areadyDead) {
		this.areadyDead = areadyDead;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRabbitExchange() {
		return rabbitExchange;
	}
	public void setRabbitExchange(String rabbitExchange) {
		this.rabbitExchange = rabbitExchange;
	}
	public String getRabbitQueue() {
		return rabbitQueue;
	}
	public void setRabbitQueue(String rabbitQueue) {
		this.rabbitQueue = rabbitQueue;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", messageBody=" + messageBody + ", messageType=" + messageType + ", createTime="
				+ createTime + ", resendTime=" + resendTime + ", areadyDead=" + areadyDead + ", status=" + status
				+ ", rabbitExchange=" + rabbitExchange + ", rabbitQueue=" + rabbitQueue + ", routingKey=" + routingKey
				+ ", version=" + version + "]";
	}
	
}
