package com.xuecheng.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

@Configuration
public class AlipayConfiguration {
	
	@Bean
	public AlipayClient alipayClient() {
		Configs.init("zfbinfo.properties");
		return new DefaultAlipayClient(Configs.getOpenApiDomain(),Configs.getAppid(),Configs.getPrivateKey(),"json","utf-8",Configs.getAlipayPublicKey(),Configs.getSignType());
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
