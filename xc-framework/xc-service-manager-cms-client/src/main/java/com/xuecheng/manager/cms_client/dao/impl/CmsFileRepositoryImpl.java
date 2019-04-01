package com.xuecheng.manager.cms_client.dao.impl;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manager.cms_client.dao.CmsFileRepository;

@Repository
public class CmsFileRepositoryImpl implements CmsFileRepository {
	
	@Autowired
	private GridFSBucket gridFSBucket;
	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Override
	public String findByFieldId(String fieldId) {
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fieldId)));
		//打开下载流下载文件
		GridFSDownloadStream stream = gridFSBucket.openDownloadStream(file.getObjectId());
		//创建gridfsresource
		GridFsResource resource = new GridFsResource(file, stream);
		//获取数据
		String fileData = "";
		try {
			fileData = IOUtils.toString(resource.getInputStream(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fileData;
	}

}
