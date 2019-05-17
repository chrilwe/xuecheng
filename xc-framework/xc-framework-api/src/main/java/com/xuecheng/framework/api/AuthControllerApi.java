package com.xuecheng.framework.api;

import javax.servlet.http.HttpServletResponse;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;

public interface AuthControllerApi {
	public LoginResult login(LoginRequest loginRequest);
	public ResponseResult logout();
}
