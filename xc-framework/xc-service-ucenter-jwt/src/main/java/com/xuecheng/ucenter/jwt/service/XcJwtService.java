package com.xuecheng.ucenter.jwt.service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.domain.ucenter.response.JwtCode;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.utils.XcOauth2Util.UserJwt;
import com.xuecheng.ucenter.jwt.config.JwtConfiguration;

@Service
public class XcJwtService {
	
	/**
	 * 从请求中获取userjwt
	 * @param request
	 * @return
	 */
	public UserToken getUserJwt(HttpServletRequest request) {
		XcOauth2Util util = new XcOauth2Util();
		UserJwt userJwt = util.getUserJwtFromHeader(request);
		if(userJwt == null) {
			ExceptionCast.cast(JwtCode.JWT_GETUSERJWT_ISNULL);
		}
		UserToken userToken = new UserToken();
		userToken.setCompanyId(userJwt.getCompanyId());
		userToken.setUserId(userJwt.getId());
		userToken.setUserPic(userJwt.getUserpic());
		userToken.setUtype(userJwt.getUtype());
		return userToken;
	}
	
	/**
	 * 创建jwt令牌
	 * @param userJwt
	 * @return
	 */
	public String createJwtToken(UserToken userToken) {
		if(userToken == null) {
			ExceptionCast.cast(JwtCode.JWT_USERJWT_ISNULL);
		}
		//获取证书
		Resource resource = new ClassPathResource("xc.keystore");
		//秘钥工厂
		KeyStoreKeyFactory keyStoreFactory = new KeyStoreKeyFactory(resource,JwtConfiguration.secret.toCharArray());
		//秘钥对
		KeyPair keyPair = keyStoreFactory.getKeyPair(JwtConfiguration.alias, JwtConfiguration.password.toCharArray());
		//私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		//定义playload
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userToken.getUserId());
		map.put("userPic", userToken.getUserPic());
		map.put("companyId", userToken.getCompanyId());
		map.put("uType", userToken.getUtype());
		//生成令牌
		Jwt jwt = JwtHelper.encode(JSON.toJSONString(map),new RsaSigner(privateKey));
		String jwtToken = jwt.getEncoded();
		return jwtToken;
	}
}
