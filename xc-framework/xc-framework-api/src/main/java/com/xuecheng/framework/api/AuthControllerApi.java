package com.xuecheng.framework.api;

import javax.servlet.http.HttpServletResponse;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;

public interface AuthControllerApi {
	public LoginResult login(LoginRequest loginRequest);//登录认证
	public ResponseResult logout();//用户退出登录
	public UserTokenStore getUserInfoByAccessToken(String accessToken);//根据身份令牌获取用户信息
}
