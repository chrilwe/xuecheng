package com.xuecheng.filesystem.controller;

import java.io.File;
import java.io.FileInputStream;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.filesystem.service.XcFileSystemService;
import com.xuecheng.framework.api.FileSystemControllerApi;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {
	
	@Autowired
	private XcFileSystemService xcFileSystemService;
	
	/**
	 * 
	 * @param multipartFile  上传的文件
	 * @param filetag 文件标签
	 * @param businesskey 业务key
	 * @param metedata 元信息,json格式
	 * @return
	 */
	@PostMapping("/upload")
	@Override
	public UploadFileResult uploadFile(MultipartFile multipartFile, String filetag, String businesskey,
			String metedata) {
		UploadFileResult result = xcFileSystemService.uploadFile(multipartFile, filetag, businesskey, metedata);
		return result;
	}
	
	@GetMapping("/testUpload")
	public String uploadTest() throws Exception {
		String classpath = this.getClass().getResource("/").getPath();
		ClientGlobal.init(classpath + "FastDfs.conf");
		System.out.println(classpath + "FastDfs.conf");
		ClientGlobal.setG_charset("UTF-8");
		ClientGlobal.setG_connect_timeout(30);
		ClientGlobal.setG_network_timeout(50);
		File file = new File("D:/新建文件夹/Lighthouse.jpg");
		byte[] file_buff = file.toString().getBytes();
		
		//创建TrackerClient
		TrackerClient client = new TrackerClient();
		//获取trackerserver
		TrackerServer trackerServer = client.getConnection();
		//获取storage
		StorageServer storeStorage = client.getStoreStorage(trackerServer);
		//创建storageclient
		StorageClient1 storageClient = new StorageClient1(trackerServer, storeStorage);
		
		String fileId = storageClient.upload_file1(file_buff, "jpg", null);
		return fileId;
	}

}
