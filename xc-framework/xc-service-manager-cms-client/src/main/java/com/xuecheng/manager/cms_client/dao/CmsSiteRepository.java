package com.xuecheng.manager.cms_client.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xuecheng.framework.domain.cms.CmsSite;

public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {

}
