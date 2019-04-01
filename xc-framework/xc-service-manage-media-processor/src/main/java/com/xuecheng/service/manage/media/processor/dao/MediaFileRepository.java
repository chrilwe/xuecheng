package com.xuecheng.service.manage.media.processor.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xuecheng.framework.domain.media.MediaFile;

public interface MediaFileRepository extends MongoRepository<MediaFile, String> {
	
}
