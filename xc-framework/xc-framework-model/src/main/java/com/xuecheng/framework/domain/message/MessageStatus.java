package com.xuecheng.framework.domain.message;

public interface MessageStatus {
	public static final String READY_SENDING = "待发送";
	public static final String SENDING = "发送中";
	public static final String FINISHED_SENDING = "已发送";
	public static final String CANCEL = "已取消";
}
