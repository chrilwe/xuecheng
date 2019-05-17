package com.xuecheng.auth.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.api.AuthControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.web.BaseController;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.utils.CookieUtil;

@RestController
@RequestMapping("/")
public class AuthController extends BaseController implements AuthControllerApi {

	@Autowired
	private AuthService authService;

	@Value("${auth.clientId}")
	private String clientId;
	@Value("${auth.clientSecret}")
	private String clientSecret;
	@Value("${auth.cookieDomain}")
	private String domain;
	@Value("${auth.cookieMaxAge}")
	private int cookieMaxAge;

	/**
	 * 登录认证，获取令牌
	 */
	@Override
	@PostMapping("/userlogin")
	public LoginResult login(LoginRequest loginRequest) {
		// 1.学成注册账号登录
		AuthToken authToken = null;
		String password = loginRequest.getPassword();
		String username = loginRequest.getUsername();
		String verifycode = loginRequest.getVerifycode();
		if (StringUtils.isEmpty(verifycode)) {
			return new LoginResult(AuthCode.AUTH_VERIFYCODE_NONE, null);// 请输入验证码
		}
		if (StringUtils.isEmpty(username)) {
			return new LoginResult(AuthCode.AUTH_USERNAME_NONE, null);// 请输入账号
		}
		if (StringUtils.isEmpty(password)) {
			return new LoginResult(AuthCode.AUTH_PASSWORD_NONE, null);// 请输入密码
		}

		// TODO 校验验证码是否正确

		// 申请令牌
		authToken = authService.login(username, password, clientId, clientSecret);
		if (authToken == null) {
			System.out.println("令牌申请失败");
			return new LoginResult(CommonCode.SERVER_ERROR, null);
		}

		// 将身份令牌放到cookie中
		CookieUtil.addCookie(response, domain, "/xuecheng", "xuecheng", authToken.getAccess_token(), cookieMaxAge,
				true);

		return new LoginResult(CommonCode.SUCCESS, authToken.getAccess_token());
	}

	@Override
	public ResponseResult logout() {
		// TODO Auto-generated method stub
		return null;
	}

}
