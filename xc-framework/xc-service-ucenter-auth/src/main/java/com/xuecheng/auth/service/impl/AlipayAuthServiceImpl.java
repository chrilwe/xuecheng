package com.xuecheng.auth.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.xuecheng.auth.client.XcUserClient;
import com.xuecheng.auth.service.AlipayAuthService;
import com.xuecheng.auth.service.UserJwt;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.order.response.OrderCode;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.code.AlipayAuthCode;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;

@Service
public class AlipayAuthServiceImpl implements AlipayAuthService {
	@Autowired
	private AlipayClient alipayClient;
	
	@Value("${auth.appToAppAuth_uri}")
	private String appToAppAuthUri;
	//#查询支付宝用户信息轮询最大次数
	@Value("${auth.max_query_retry}")
	private int max_query_retry;
	//#查询支付宝用户信息轮询时间间隔（毫秒）
	@Value("${auth.query_duration}")
	private int query_duration;
	
	/**
	 * 支付宝授权请求地址拼接
	 */
	@Override
	public String makeAlipayAuthUri(String appId, String redirectUri) {
		if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(redirectUri)) {
			ExceptionCast.cast(OrderCode.PAY_AUTHURL_PARAMS_ERROR);
		}

		// 拼接授权地址
		// 对redirect_uri进行UriEncode
		redirectUri = UriEncoder.encode(redirectUri);
		String alipayAuthUrl = appToAppAuthUri + "?app_id=" + appId + "&scope=auth_user&redirect_uri=" + redirectUri;

		return alipayAuthUrl;
	}
	
	/**
	 * 使用auth_code换取接口access_token及用户userId
	 */
	@Override
	public AlipaySystemOauthTokenResponse getAlipaySystemOauthToken(String appAuthCode) {
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(appAuthCode);
		request.setGrantType("authorization_code");
		AlipaySystemOauthTokenResponse oauthTokenResponse = null;
		try {
		    oauthTokenResponse = alipayClient.execute(request);
		    System.out.println(oauthTokenResponse.getAccessToken());
		    if(!oauthTokenResponse.isSuccess()) {
		    	ExceptionCast.cast(AuthCode.AUTH_GETALIPAYTOKEN_FAIL);
		    }
		  
		} catch (AlipayApiException e) {
		    //处理异常
			ExceptionCast.cast(AuthCode.AUTH_GETALIPAYTOKEN_FAIL);
		    e.printStackTrace();
		}
		return oauthTokenResponse;
	}
	
	/**
	 * 根据支付宝令牌查询会员信息
	 */
	@Override
	public XcUser getUserDetailByAuthToken(String authToken) {
		if(StringUtils.isEmpty(authToken)) {
			ExceptionCast.cast(AuthCode.AUTH_AUTHTOKEN_ISNULL);
		}
		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
		try {
		    AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(request, authToken);
		    //查询用户信息
		    XcUser xcUser = loopQueryUserDetail(request,authToken);
		    if(xcUser == null) {
		    	ExceptionCast.cast(AuthCode.AUTH_QUERYUSERDETAILS_ERROE);
		    }
		    return xcUser;   
		} catch (AlipayApiException e) {
		    //处理异常
			ExceptionCast.cast(AuthCode.AUTH_QUERYUSERDETAILS_ERROE);
		    e.printStackTrace();
		}
		return null;
	}
	
	//轮询查询用户信息(当状态码为20000)
	private XcUser loopQueryUserDetail(AlipayUserInfoShareRequest request,String authToken) {
		for(int i = 0;i < max_query_retry;i++) {
			try {
				Thread.sleep(query_duration);
				AlipayUserInfoShareResponse response = alipayClient.execute(request, authToken);
				//查询状态码
			    String code = response.getCode();
			    //状态码为20000，系统异常，发起查询重试
			    if(code.equals(AlipayAuthCode.UNKNOW_ERROR)) {
			    	continue;
			    } else if(code.equals(AlipayAuthCode.SUCCESS)) {
			    	//状态码为10000，查询成功
			    	System.out.println("--------用户信息:" + JSON.toJSONString(response));
				    return this.getUserDetail(response);
			    } else if(AlipayAuthCode.TOKEN_ERROR.equals(code)){
			    	//其他状态，明确查询错误
			    	break;
			    } else {
			    	break;
			    }
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	//获取会员信息
	private XcUser getUserDetail(AlipayUserInfoShareResponse userinfoShareResponse) {
		//获取会员信息
	    XcUser xcUser = new XcUser();
	    xcUser.setId(userinfoShareResponse.getUserId());
	    xcUser.setEmail(userinfoShareResponse.getEmail());
	    xcUser.setCreateTime(new Date());
	    xcUser.setName(userinfoShareResponse.getUserName());
	    xcUser.setPhone(userinfoShareResponse.getPhone());
	    xcUser.setSalt(userinfoShareResponse.getAvatar());
	    xcUser.setSex(userinfoShareResponse.getGender());
	    xcUser.setStatus(StringUtils.isEmpty(userinfoShareResponse.getUserStatus())?"":userinfoShareResponse.getUserStatus());
	    xcUser.setUsername(userinfoShareResponse.getNickName());
	    xcUser.setUserpic(userinfoShareResponse.getPersonPictures() == null&&userinfoShareResponse.getPersonPictures().size() <=0 ?"":userinfoShareResponse.getPersonPictures().get(0).toString());
	    xcUser.setUtype(userinfoShareResponse.getUserType());
	    return xcUser;
	}
}
