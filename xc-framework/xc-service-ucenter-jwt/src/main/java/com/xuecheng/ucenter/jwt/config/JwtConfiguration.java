package com.xuecheng.ucenter.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="encrypt.key-store")
public class JwtConfiguration {
	public static String secret;
	public static String alias;
	public static String password;
}
