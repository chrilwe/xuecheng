package com.xuecheng.auth.service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.xuecheng.auth.client.XcJwtClient;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtCode;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private XcJwtClient xcJwtClient;
	
	@Value("${auth.tokenValiditySeconds}")
	private long ttl;
	@Value("${auth.max_query_retry}")
	private int max_query_retry;
	@Value("${auth.query_duration}")
	private int query_duration;
	
	@Override
	public AuthToken login(String username, String password, String clientId, String clientSecret) {
		//从微服务实例中获取申请令牌的完整url
		ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
		URI uri = serviceInstance.getUri();
		String authUrl = uri + "/auth/oauth/token";
				
		//将clientId和clientSecret转换成httpBasic字符串
		String httpBasic = this.getHttpBasic(clientId, clientSecret);
		
		//设置请求头和请求体
		HttpEntity httpEntity = this.descriptHttpEntity(authUrl, httpBasic, username, password);
		
		//设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){

			public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
			
		});
		
		//请求springsecurity申请令牌
		ResponseEntity<Map> response = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
		Map dataMap = response.getBody();
		if(dataMap == null || dataMap.get("access_token") == null 
				|| dataMap.get("jti") == null ||
				dataMap.get("refresh_token") == null) {
			String error_description = (String)dataMap.get("error_description");
			if(dataMap != null && error_description != null ) {
				if(error_description.indexOf("坏的凭证") >= 0) {
					ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);//账号或者密码错误
				} else if(error_description.indexOf("UserDetailsService returned null") >= 0) {
					ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);//不存在账号
				}
			}
			return null;
		}
		AuthToken authToken = new AuthToken();
		authToken.setAccess_token((String)dataMap.get("jti"));
		authToken.setJwt_token((String)dataMap.get("access_token"));
		authToken.setRefresh_token((String)dataMap.get("refresh_token"));
		
		//将令牌存放到redis中
		String key = "user_token:" + (String)dataMap.get("jti");
		boolean saveResult = this.saveTokenToRedis(key, JSON.toJSONString(authToken), ttl);
		if(!saveResult) {
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);//存储令牌失败
		}
		
		return authToken;
	}
	
	/**设置请求头和请求体，返回HttpEntity
	 * @param authUrl 请求令牌地址
	 * @param httpBasic "Basic "+clientId：clientSecret（经过base64编码）的字符串
	 * @param username 用户账号
	 * @param password 用户密码
	 */
	private HttpEntity descriptHttpEntity(String authUrl, String httpBasic, String username, String password) {
		//设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", httpBasic);		
				
		//设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("username", username);
		body.add("password", password);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
		return httpEntity;
	}
	
	/**
	 * 将clientId和clientSecret进行base64编码,拼装获取httpBasic字符串
	 */
	private String getHttpBasic(String clientId, String clientSecret) {
		String string = clientId + ":" + clientSecret;
		byte[] encode = Base64Utils.encode(string.getBytes());
		String httpBasic = "Basic " + new String(encode);
		return httpBasic;
	}
	
	/**
	 * 将令牌存放到redis中
	 */
	private boolean saveTokenToRedis(String key, String value, long ttl) {
		stringRedisTemplate.boundValueOps(key).set(value, ttl, TimeUnit.SECONDS);
		Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
		return expire > 0;
	}
	
	/**
	 * 根据身份令牌获取用户信息
	 */
	@Override
	public UserTokenStore getUserInfoByAccessToken(String accessToken, HttpServletRequest request) {
		if(StringUtils.isEmpty(accessToken)) {
			ExceptionCast.cast(AuthCode.AUTH_ACCESS_TOKEN_NONE);
		}
		//根据身份令牌，从redis中获取用户信息
		String authTokenJson = stringRedisTemplate.boundValueOps(accessToken).get();
		if(StringUtils.isEmpty(authTokenJson)) {
			ExceptionCast.cast(AuthCode.AUTH_GET_USERINFO_FAIL);
		}
		AuthToken authToken = JSON.parseObject(authTokenJson,AuthToken.class);
		String jwt = authToken.getJwt_token();
		
		//将jwt令牌解析出来
		UserToken userToken = this.loopGetUserToken(request);
		
		//刷新用户登录过期时间
		try {
			stringRedisTemplate.expire(accessToken, ttl, TimeUnit.SECONDS);
		} catch (Exception e) {
			ExceptionCast.cast(AuthCode.AUTH_REFRESH_TOKEN_FAIL);
		}
		
		UserTokenStore store = new UserTokenStore();
		store.setAccess_token(accessToken);
		store.setJwt_token(jwt);
		store.setCompanyId(userToken.getCompanyId());
		store.setRefresh_token(authToken.getRefresh_token());
		store.setUserId(userToken.getUserId());
		store.setUserPic(userToken.getUserPic());
		store.setUtype(userToken.getUtype());
		return store;
	}
	
	/**
	 * 发起解析令牌请求异常，重试
	 */
	private UserToken loopGetUserToken(HttpServletRequest request) {
		UserToken userToken = null;
		for(int i = 0;i < max_query_retry;i++) {
			try {
				userToken = xcJwtClient.getUserJwtFromHeader(request);
				if(userToken == null) {
					ExceptionCast.cast(JwtCode.JWT_PARSE_ERROR);
				}
				break;
			} catch (Exception e) {
				//查询次数超过最大次数，停止查询,抛出系统异常
				if(i == max_query_retry) {
					ExceptionCast.cast(JwtCode.JWT_PARSE_ERROR);
				}
				continue;
			}
		}
		return userToken;
	}
}
