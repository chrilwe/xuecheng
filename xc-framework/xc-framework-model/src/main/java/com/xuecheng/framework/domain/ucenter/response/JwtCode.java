package com.xuecheng.framework.domain.ucenter.response;

import com.xuecheng.framework.common.model.response.ResultCode;

public enum JwtCode implements ResultCode {
	
	JWT_GETUSERJWT_ISNULL(false,10,"获取的令牌信息为空！！！"),
	JWT_USERJWT_ISNULL(false,11,"生成令牌的参数错误！！"),
	JWT_PARSE_ERROR(false,12,"解析jwt令牌失败！！！");
	private boolean success;
	private int code;
	private String message;
	
	private JwtCode(boolean success,int code,String message) {
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
