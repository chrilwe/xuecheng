package com.xuecheng.manage.cms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.jms.Topic;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;

@RestController
public class TestGfsController {
	
	@Autowired
	private GridFSBucket gridFSBucket;
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private JmsMessagingTemplate jms;
	@Autowired
	private Topic topic;
	
	@GetMapping("/testGfs")
	public void testGfs() throws FileNotFoundException {
		FileInputStream content = new FileInputStream(new File("D:/ext.dic"));
		ObjectId objectId = gridFsTemplate.store(content, "测试文件上传GFS");
		String fileId = objectId.toString();
		System.out.println(fileId);
	}
	
	@GetMapping("/getGfs")
	public String get() throws IllegalStateException, IOException {
		String fieldId = "5c8e3be578e70213b4da878e";
		//查询文件
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fieldId)));
		//打开下载流下载文件
		GridFSDownloadStream stream = gridFSBucket.openDownloadStream(file.getObjectId());
		//创建gridfsresource
		GridFsResource resource = new GridFsResource(file, stream);
		//获取数据
		String string = IOUtils.toString(resource.getInputStream(), "utf-8");
		System.out.println(string);
		return string;
	}
	
	@GetMapping("/activemq")
	public String send(String message) {
		try {
			jms.convertAndSend(topic, message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	@GetMapping("/saveTemplate")
	public String save() throws FileNotFoundException {
		String path = "";
		path = this.getClass().getResource("/").getPath() + "/templates/index_banner.ftl";
		System.out.println("--------------path-------:"+path);
		FileInputStream content = new FileInputStream(new File(path));
		ObjectId objectId = gridFsTemplate.store(content, "轮播图模板");
		String fileId = objectId.toString();
		return fileId;
	}
}
