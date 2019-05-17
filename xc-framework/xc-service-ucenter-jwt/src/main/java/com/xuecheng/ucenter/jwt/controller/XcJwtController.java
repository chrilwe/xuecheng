package com.xuecheng.ucenter.jwt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.XcJwtControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.utils.XcOauth2Util.UserJwt;
import com.xuecheng.ucenter.jwt.service.XcJwtService;

@RestController
@RequestMapping("/ucenter/jwt")
public class XcJwtController implements XcJwtControllerApi {

	@Autowired
	private XcJwtService xcJwtService;

	/**
	 * 从请求中获取令牌信息
	 */
	@Override
	@RequestMapping("/getUserJwt")
	public UserToken getUserJwtFromHeader(HttpServletRequest request) {
		UserToken userToken = xcJwtService.getUserJwt(request);
		
		return userToken;
	}

	/**
	 * 生成jwt令牌
	 */
	@Override
	@PostMapping("/getJwtToken")
	public String createJwtToken(UserToken userToken) {
		
		return xcJwtService.createJwtToken(userToken);
	}

}
