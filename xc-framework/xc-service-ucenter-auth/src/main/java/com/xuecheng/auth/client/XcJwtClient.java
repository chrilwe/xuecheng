package com.xuecheng.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.utils.XcOauth2Util.UserJwt;

@FeignClient(XcServiceList.XC_SERVICE_UCENTER_JWT)
@RequestMapping("/ucenter/jwt")
public interface XcJwtClient {
	/**
	 * 生成jwt令牌
	 */
	@PostMapping("/getJwtToken")
	public String createJwtToken(UserToken userToken);
}
