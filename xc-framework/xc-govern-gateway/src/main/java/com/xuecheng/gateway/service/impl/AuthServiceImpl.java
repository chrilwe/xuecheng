package com.xuecheng.gateway.service.impl;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.gateway.service.AuthService;
@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 从请求头中获取jwt令牌
	 */
	@Override
	public String getJwtFromHttpHeader(HttpServletRequest request, String header) {
		String auth = request.getHeader(header);
		if(!StringUtils.isEmpty(auth)) {
			return auth;
		}
		return null;
	}
	
	/**
	 * 从cookie中获取用户身份令牌
	 */
	@Override
	public String getUserTokenFromCookie(HttpServletRequest request, String cookieName) {
		//Map<String, String> cookie = CookieUtil.readCookie(request, cookieName);
		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				if(name.equals(cookieName)) {
					cookieValue = value;
					break;
				}
			}
		}
		
		return cookieValue;
	}
	
	/**
	 * 从redis中获取AuthToken
	 */
	@Override
	public AuthToken getAuthTokenFromRedis(String key) {
		String string = stringRedisTemplate.opsForValue().get(key);
		if(!StringUtils.isEmpty(string)) {
			return JSON.parseObject(string, AuthToken.class);
		}
		return null;
	}

}
