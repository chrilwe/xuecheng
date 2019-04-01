package com.xuecheng.manage.media.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xuecheng.framework.domain.media.MediaFile;

public interface MediaRepository extends MongoRepository<MediaFile, String> {

}
