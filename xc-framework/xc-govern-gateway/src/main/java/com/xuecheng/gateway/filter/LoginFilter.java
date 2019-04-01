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
import com.xuecheng.gateway.service.AuthService;

/*
 * 网关登录过滤器
 */
@Component
public class LoginFilter extends ZuulFilter {
	
	private static final String  COOKIE_NAME = "xc-cookie";
	@Autowired
	private AuthService authService;

	@Override
	public Object run() throws ZuulException {
		//如果请求头带着jwt令牌过来,从cookie中拿身份令牌，在从redis中查询用户令牌是否已经过期
		//如果请求头没有带着jwt令牌过来,不做任何处理，因为可能此时访问的资源是可以不通过认证的
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		String jwt = authService.getJwtFromHttpHeader(request, "Authorization");
		if(!StringUtils.isEmpty(jwt)) {
			//开头不是以"Bearer "拒绝访问
			if(jwt.startsWith("Bearer ")) {
				access_denied(context);
				return null;
			}
			
			//cookie中没有用户令牌，拒绝访问
			String userToken = authService.getUserTokenFromCookie(request, COOKIE_NAME);
			if(StringUtils.isEmpty(userToken)) {
				access_denied(context);
				return null;
			}
			
			//redis中不存在令牌信息或者令牌已经过期,拒绝访问
			AuthToken authToken = authService.getAuthTokenFromRedis("user_token:" + userToken);
			if(authToken == null) {
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
	
	private void access_denied(RequestContext context) {
		HttpServletRequest request = context.getRequest();
		context.setSendZuulResponse(false);
		ResponseResult result = new ResponseResult(CommonCode.UNAUTHENTICATED);
		context.setResponseBody(JSON.toJSONString(result));
		context.getResponse().setContentType("application/json;charset=utf-8");
	}
}
