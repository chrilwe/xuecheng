package com.xuecheng.auth.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.xuecheng.auth.client.XcJwtClient;
import com.xuecheng.auth.client.XcUserClient;
import com.xuecheng.auth.service.AlipayAuthService;
import com.xuecheng.auth.service.UserJwt;
import com.xuecheng.framework.api.AlipayAuthControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.web.BaseController;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.LoginQrCodeResult;

@RestController
@RequestMapping("/")
public class AlipayAuthController extends BaseController implements AlipayAuthControllerApi {

	@Autowired
	private AlipayAuthService alipayAuthService;
	@Autowired
	private XcUserClient xcUserClient;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private XcJwtClient xcJwtClient;

	@Value("${auth.redirect_uri}")
	private String redirectUri;
	@Value("${auth.app_id}")
	private String appId;
	@Value("${auth.tokenValiditySeconds}")
	private long timeout;

	/**
	 * 支付宝第三方应用授权请求地址拼接
	 */
	@Override
	@GetMapping("/alipay/openAuthUrl")
	public LoginQrCodeResult alipayAuthUrl() {
		String loginUrl = alipayAuthService.makeAlipayAuthUri(appId, redirectUri);
		LoginQrCodeResult result = new LoginQrCodeResult(CommonCode.SUCCESS);
		result.setCodeUrl(loginUrl);
		return result;
	}

	/**
	 * 支付宝第三方应用授权回调
	 */
	@Override
	@RequestMapping("/alipay/redirect")
	public UserTokenStore alipayRedirect() {
		// 获取应用授权回调的参数
		String appId = request.getParameter("app_id");
		String appAuthCode = request.getParameter("auth_code");
		System.out.println("----授权回调参数:appId=" + appId + "appAuthCode=" + appAuthCode);

		// 使用auth_code换取接口access_token及用户userId
		AlipaySystemOauthTokenResponse response = alipayAuthService.getAlipaySystemOauthToken(appAuthCode);
		String accessToken = response.getAccessToken();
		String alipayUserId = response.getAlipayUserId();
		String refreshToken = response.getRefreshToken();
		
		//获取会员信息
		XcUser xcUser = this.queryAlipayUserDetail(accessToken, alipayUserId);
		
		//将会员信息生成jwt令牌
		UserToken userToken = new UserToken();
		userToken.setUserId(xcUser.getId());
		userToken.setUserPic(xcUser.getUserpic());
		userToken.setUtype(xcUser.getUtype());
		String jwt = this.createJwt(userToken);
		if(StringUtils.isEmpty(jwt)) {
			ExceptionCast.cast(AuthCode.AUTH_CREATE_JWT_ISNULL);
		}
		
		//将令牌存入redis并设置过期时间
		try {
			this.setAlipayTokenToRedis(accessToken, jwt);
		} catch (Exception e) {
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
		}
		
		//将令牌信息，用户信息返回,前端将令牌放入请求头，并将身份令牌放到cookie中 供网关判断是否登录过期
		UserTokenStore store = new UserTokenStore();
		store.setAccess_token(accessToken);
		store.setJwt_token(jwt);
		store.setRefresh_token(refreshToken);
		store.setUserId(xcUser.getId());
		store.setUserPic(xcUser.getUserpic());
		store.setUtype(xcUser.getUtype());
		return store;
	}

	// 查询支付宝会员信息
	private XcUser queryAlipayUserDetail(String accessToken, String alipayUserId) {
		// 根据userId查询数据库是否存在会员信息，没有的话用支付宝身份令牌查询，并将会员信息入库
		XcUser xcUser = xcUserClient.findByUserId("alipay" + alipayUserId);
		if (xcUser == null) {
			// 根据支付宝令牌获取会员信息
			xcUser = alipayAuthService.getUserDetailByAuthToken(accessToken);
			ResponseResult register = xcUserClient.register(xcUser);
		}

		return xcUser;
	}

	// 将令牌存入redis中
	// 参数：1.支付宝身份令牌 2.支付宝会员信息jwt令牌
	private void setAlipayTokenToRedis(String accessToken, String jwt) {
		// 将令牌信息存入redis，并设置有效期(秒)
		stringRedisTemplate.boundValueOps(accessToken).set(jwt);
		stringRedisTemplate.expire(accessToken, timeout, TimeUnit.SECONDS);
	}

	// 将支付宝会员信息转化为jwt令牌
	private String createJwt(UserToken userToken) {
		String jwt = xcJwtClient.createJwtToken(userToken);
		return jwt;
	}
	
}
