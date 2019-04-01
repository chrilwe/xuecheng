package com.xuecheng.manage.media.service;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;

public interface MediaService {
	//上传块文件
	public ResponseResult uploadChunkFile(MultipartFile file, 
			Integer chunk, String fileMd5);
	
	//文件上传注册
	public ResponseResult register(String fileMd5, String fileName, 
			String fileSize, String mimeType, String fileExt);
	
	//检查块文件是否存在
	public CheckChunkResult checkChunk(String fileMd5, Integer chunk, 
			Integer chunkSize);
	
	//合并块文件
	public ResponseResult mergeChunkFile(String fileMd5, 
			String fileName, String fileSize, String mimeType,
			String fileExt);
}
