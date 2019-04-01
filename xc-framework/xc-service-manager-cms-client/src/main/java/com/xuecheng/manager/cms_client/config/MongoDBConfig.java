package com.xuecheng.manager.cms_client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

@Configuration
public class MongoDBConfig {
	
	@Value("${spring.data.mongodb.database}")
	private String dbName;
	
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
