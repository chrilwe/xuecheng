package com.xuecheng.auth.service;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.xuecheng.framework.domain.ucenter.XcUser;


public interface AlipayAuthService {
	public AlipaySystemOauthTokenResponse getAlipaySystemOauthToken(String appAuthCode);//使用auth_code换取接口access_token及用户userId
	public String makeAlipayAuthUri(String appId,String redirectUri);//支付宝授权请求地址拼接
	public XcUser getUserDetailByAuthToken(String authToken);//根据支付宝令牌查询会员信息
}
