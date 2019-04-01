package com.xuecheng.gateway.service;

import javax.servlet.http.HttpServletRequest;

import com.xuecheng.framework.domain.ucenter.ext.AuthToken;

public interface AuthService {
	//从请求头中取出jwt令牌
	public String getJwtFromHttpHeader(HttpServletRequest request, String header);
	//从cookie中取出身份令牌
	public String getUserTokenFromCookie(HttpServletRequest request, String cookieName);
	//从redis中获取AuthToken对象
	public AuthToken getAuthTokenFromRedis(String key);
}
