package com.xuecheng.gateway.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.status.AuthStatus;
import com.xuecheng.gateway.service.AuthService;

/*
 * 网关登录前置过滤器
 */
@Component
public class LoginPreFilter extends ZuulFilter {
	
	private static final String  COOKIE_NAME = "xc-cookie";
	@Autowired
	private AuthService authService;

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		
		//请求头中取出jwt令牌
		String jwt = authService.getJwtFromHttpHeader(request, "Authorization");
		//从cookie中取出accessToken
		String accessToken = authService.getUserTokenFromCookie(request, COOKIE_NAME);
		if(!StringUtils.isEmpty(accessToken)) {
			//判断该用户登录是否已经过期
			AuthToken authToken = authService.getAuthTokenFromRedis(accessToken);
			if(authToken == null) {
				this.access_denied(context);
				return null;
			}
			
			if(StringUtils.isEmpty(jwt)) {
				this.access_denied(context);
				return null;
			}
			//开头不是以"Bearer "拒绝访问
			if(jwt.startsWith("Bearer ")) {
				access_denied(context);
				return null;
			}
			
		} else {
			//cookie没有accessToken，则请求头如果有 bearer的请求拒绝访问
			if(!StringUtils.isEmpty(jwt)) {
				access_denied(context);
				return null;
			}
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		
		return true;
	}

	@Override
	public int filterOrder() {
		
		return 0;
	}

	@Override
	public String filterType() {
		
		return "pre";
	}
	
	/**
	 * 拒绝访问
	 * @param context
	 */
	private void access_denied(RequestContext context) {
		HttpServletRequest request = context.getRequest();
		context.setSendZuulResponse(false);
		ResponseResult result = new ResponseResult(CommonCode.UNAUTHENTICATED);
		context.setResponseBody(JSON.toJSONString(result));
		context.getResponse().setContentType("application/json;charset=utf-8");
	}
	
}
