package com.xuecheng.gateway.filter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.status.AuthStatus;
/**
 * 网关登录后置过滤器
 * @author Administrator
 *
 */
@Component
public class LoginPostFilter extends ZuulFilter {

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		this.handleUnAuthorizationResponse(context);
		return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "post";
	}
	
	/**
	 * 服务返回结果是401状态，认证失败,网关返回用户未登录
	 */
	private void handleUnAuthorizationResponse(RequestContext context) {
		HttpServletResponse response = context.getResponse();
		int status = response.getStatus();
		if(status == AuthStatus.UNAUTHENTICATED) {
			ResponseResult result = new ResponseResult(CommonCode.UNAUTHENTICATED);
			context.setResponseBody(JSON.toJSONString(result));
			context.getResponse().setContentType("application/json;charset=utf-8");
		}
	}
}
