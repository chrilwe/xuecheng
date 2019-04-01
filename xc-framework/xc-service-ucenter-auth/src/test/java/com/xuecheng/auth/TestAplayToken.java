package com.xuecheng.auth;

import java.net.URI;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.xuecheng.framework.common.client.XcServiceList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAplayToken {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@Test
	public void testAplayToken() {
		//获取申请地址
		ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
		URI uri = serviceInstance.getUri();
		String url = uri + "";
		
		//设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		String clientId = "XcWebApp";
		String clientSecret = "XcWebApp";
		String string = clientId + ":" + clientSecret;
		byte[] encode = Base64Utils.encode(string.getBytes());
		String httpBasic = "Basic " + new String(encode);
		System.out.println("Authorization : " +httpBasic);
		headers.add("Authorization", httpBasic);
		
		//设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("username", "itcast");
		body.add("password", "123");
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
		
		//请求springsecurity申请令牌
		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
		Map dataMap = response.getBody();
		System.out.println(dataMap);
	}
}
