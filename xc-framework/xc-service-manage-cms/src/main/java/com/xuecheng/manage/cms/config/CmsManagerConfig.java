package com.xuecheng.manage.cms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

@Configuration
public class CmsManagerConfig {
	
	@Value("${spring.data.mongodb.database}")
	private String dbName;
	
	@Bean("restTemplate")
	public RestTemplate registryRestTemplate() {
		
		return new RestTemplate();
	}
	
	/**
	 * MongoDB 配置
	 */
	@Bean
	public GridFSBucket gridFsBucket(MongoClient mongoClient) {
		MongoDatabase database = mongoClient.getDatabase(dbName);
		GridFSBucket bucket = GridFSBuckets.create(database);
		return bucket;
	}
}
