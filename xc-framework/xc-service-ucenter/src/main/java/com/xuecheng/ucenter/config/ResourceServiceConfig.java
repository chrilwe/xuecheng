package com.xuecheng.ucenter.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)//激活权限注解
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter {
	
	private static final String PUBLIC_KEY = "publickey.txt";
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/ucenter/getuserext","/**").permitAll()//放行
				.anyRequest().authenticated();
	}
	
	//定义TokenStore
	@Bean
	public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
		
		return new JwtTokenStore(jwtAccessTokenConverter);
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setVerifierKey(getPublicKey());//设置公钥
		return jwtAccessTokenConverter;
	}
	
	/**
	 * 获取公钥
	 */
	private String getPublicKey() {
		Resource resource = new ClassPathResource(PUBLIC_KEY);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
			BufferedReader br = new BufferedReader(inputStreamReader);
			return br.lines().collect(Collectors.joining("\n"));
		} catch (Exception e) {
			return null;
		}
	}

}
