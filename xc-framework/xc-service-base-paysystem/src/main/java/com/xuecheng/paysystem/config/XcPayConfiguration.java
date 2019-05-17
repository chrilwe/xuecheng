package com.xuecheng.paysystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.xuecheng.paysystem.alipay.trade.config.Configs;
import com.xuecheng.paysystem.alipay.trade.service.AlipayTradeService;
import com.xuecheng.paysystem.alipay.trade.service.impl.AlipayTradeServiceImpl;

@Configuration
public class XcPayConfiguration {
	
	@Bean("tradeService")
	public AlipayTradeService tradeService() {
		Configs.init("zfbinfo.properties");
		return new AlipayTradeServiceImpl.ClientBuilder().build();
	}
	
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
