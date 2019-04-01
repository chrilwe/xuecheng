package com.xuecheng.filesystem.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xuecheng.framework.domain.filesystem.FileSystem;

public interface XcFileSystemRepository extends  MongoRepository<FileSystem, String>{

}
