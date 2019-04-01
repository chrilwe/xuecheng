package com.xuecheng.filesystem.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.XcFileSystemRepository;
import com.xuecheng.filesystem.service.XcFileSystemService;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

@Service
public class XcFileSystemServiceImpl implements XcFileSystemService {
	
	@Autowired
	private XcFileSystemRepository xcFileSystemRepository;
	
	@Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
	private int connect_timeout_in_seconds;
	@Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
	private int network_timeout_in_seconds;
	@Value("${xuecheng.fastdfs.charset}")
	private String charset;
	@Value("${xuecheng.fastdfs.tracker_servers}")
	private String tracker_servers;

	@Override
	public void save(FileSystem fileSystem) {
		FileSystem save = xcFileSystemRepository.save(fileSystem);
	}

	@Override
	public UploadFileResult uploadFile(MultipartFile multipartFile, String filetag, String businesskey,
			String metedata) {
		//上传文件到fdfs
		if(multipartFile == null) {
			ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);//上传文件为空
		}
		if(StringUtils.isEmpty(businesskey)) {
			ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_BUSINESSISNULL);//业务id为空
		}
		String fileId = this.upload_fdfs(multipartFile);
		
		//保存文件元数据
		FileSystem fileSystem = new FileSystem();
		fileSystem.setFileId(fileId);
		fileSystem.setBusinesskey(businesskey);
		fileSystem.setFilePath(fileId);
		fileSystem.setFiletag(filetag);
		fileSystem.setFileSize(multipartFile.getSize());
		fileSystem.setFileName(multipartFile.getOriginalFilename());
		fileSystem.setFileType(multipartFile.getContentType());
		try {
			if(!StringUtils.isEmpty(metedata)) {
				Map map = JSON.parseObject(metedata, Map.class);
				fileSystem.setMetadata(map);
			}
		} catch (Exception e) {
			ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_METAERROR);//上传文件的元信息请使用json格式
		}
		this.save(fileSystem);
		return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
	}
	
	/**
	 * 初始化fdfs
	 */
	private void init_fdfs() {
		try {
			String classpath = this.getClass().getResource("/").getPath();
			ClientGlobal.init(classpath + "FastDfs.conf");
			ClientGlobal.setG_charset(charset);
			ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
			ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionCast.cast(FileSystemCode.FS_INIT_ERROR);//初始化失败！
		}
	}
	
	/**
	 * 上传文件到fdfs
	 */
	private String upload_fdfs(MultipartFile multipartFile) {
		//初始化fdfs
		this.init_fdfs();
		
		try {
			//创建TrackerClient
			TrackerClient client = new TrackerClient();
			//获取trackerserver
			TrackerServer trackerServer = client.getConnection();
			//获取storage
			StorageServer storeStorage = client.getStoreStorage(trackerServer);
			//创建storageclient
			StorageClient1 storageClient = new StorageClient1(trackerServer, storeStorage);
			
			byte[] file_buff = multipartFile.getBytes();
			//文件名
			String filename = multipartFile.getOriginalFilename();
			String file_ext_name = filename.substring(filename.lastIndexOf(".") + 1);
			String fileId = storageClient.upload_file1(file_buff, file_ext_name, null);
			
			return fileId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
