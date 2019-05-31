package com.xuecheng.framework.domain.message.response;

import com.xuecheng.framework.common.model.response.ResultCode;

public enum MessageCode implements ResultCode {
	MESSAGE_ALREADY_EXISTS(false,1000,"消息已经存在！"),
	MESSAGE_ID_NULL(false,1001,"消息ID为空！"),
	MESSAGE_ISNOT_EXISTS(false,1002,"消息不存在"),
	MESSAGE_STATUS_ERROR(false,1003,"消息状态异常！"),
	MESSAGE_ALREAD_DEAD(false,1004,"消息已进入死亡！"),
	MESSAGE_CREATENODE_TIMEOUT(false,1005,"获取分布式锁超时！"),
	MESSAGE_READYSENDING_ERROR(false,1006,"消息预发送异常！！！");
	
	//操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    
	private MessageCode(boolean success, int code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	@Override
	public boolean success() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int code() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String message() {
		// TODO Auto-generated method stub
		return null;
	}

}
