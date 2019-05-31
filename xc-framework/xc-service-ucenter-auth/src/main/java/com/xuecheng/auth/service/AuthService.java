package com.xuecheng.auth.service;

import javax.servlet.http.HttpServletRequest;

import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;

public interface AuthService {
	//用户登录认证
	public AuthToken login(String username, String password, String clientId, String clientSecret);
	//根据身份令牌获取用户信息
	public UserTokenStore getUserInfoByAccessToken(String accessToken,HttpServletRequest request);
}
