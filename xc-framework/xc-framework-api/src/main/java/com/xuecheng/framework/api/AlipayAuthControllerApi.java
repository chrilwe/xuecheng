package com.xuecheng.framework.api;

import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import com.xuecheng.framework.domain.ucenter.response.LoginQrCodeResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;

public interface AlipayAuthControllerApi {
	public LoginResult alipayRedirect();//支付宝第三方授权回调
	public LoginQrCodeResult alipayAuthUrl();//支付宝第三方授权请求地址拼接
}
