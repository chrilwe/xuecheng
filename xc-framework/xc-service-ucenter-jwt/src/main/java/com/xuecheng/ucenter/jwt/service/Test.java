package com.xuecheng.ucenter.jwt.service;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;


public class Test {
	public static void main(String[] args) {
		UserToken userToken = new UserToken();
		userToken.setCompanyId("1");
		userToken.setUserId("1");
		userToken.setUserPic("http://pic");
		userToken.setUtype("vip");
		// 获取证书
		Resource resource = new ClassPathResource("xc.keystore");
		// 秘钥工厂
		KeyStoreKeyFactory keyStoreFactory = new KeyStoreKeyFactory(resource, "xuechengkeystore".toCharArray());
		// 秘钥对
		KeyPair keyPair = keyStoreFactory.getKeyPair("xckey", "xuecheng".toCharArray());
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 定义playload
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userToken.getUserId());
		map.put("userPic", userToken.getUserPic());
		map.put("companyId", userToken.getCompanyId());
		map.put("uType", userToken.getUtype());
		// 生成令牌
		Jwt jwt = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(privateKey));
		String jwtToken = jwt.getEncoded();
		System.out.print(jwtToken);
	}
}
