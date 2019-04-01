package com.xuecheng.filesystem.service;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

public interface XcFileSystemService {
	public void save(FileSystem fileSystem);//保存filesystem信息
	public UploadFileResult uploadFile(MultipartFile multipartFile, String filetag, String businesskey,
			String metedata);//上传文件
}
