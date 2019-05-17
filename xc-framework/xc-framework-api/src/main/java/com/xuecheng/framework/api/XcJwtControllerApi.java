package com.xuecheng.framework.api;

import javax.servlet.http.HttpServletRequest;

import com.xuecheng.framework.domain.ucenter.ext.UserToken;

public interface XcJwtControllerApi {
	//从请求头中获取UserJwt
	public UserToken getUserJwtFromHeader(HttpServletRequest request);
	//生成jwt令牌
	public String createJwtToken(UserToken userToken);
}
