package com.xuecheng.framework.api;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

public interface FileSystemControllerApi {
	/**
	 * 
	 * @param multipartFile  上传的文件
	 * @param filetag 文件标签
	 * @param businesskey 业务key
	 * @param metedata 元信息,json格式
	 * @return
	 */
	public UploadFileResult uploadFile(MultipartFile multipartFile, String filetag, String businesskey, String metedata);
}
