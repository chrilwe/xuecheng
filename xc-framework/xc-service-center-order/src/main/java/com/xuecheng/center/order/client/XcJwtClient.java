package com.xuecheng.center.order.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.utils.XcOauth2Util.UserJwt;

@FeignClient(XcServiceList.XC_SERVICE_UCENTER_JWT)
@RequestMapping("/ucenter/jwt")
public interface XcJwtClient {
	/**
	 * 从请求中获取令牌信息
	 */
	@RequestMapping("/getUserJwt")
	public UserToken getUserJwtFromHeader(HttpServletRequest request);
}
