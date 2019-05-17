package com.xuecheng.framework.domain.ucenter.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;

public class LoginQrCodeResult extends ResponseResult{
	private String codeUrl;

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	@Override
	public String toString() {
		return "LoginQrCodeResult [codeUrl=" + codeUrl + "]";
	}

	public LoginQrCodeResult(ResultCode resultCode) {
		super(resultCode);
	}
	
}
