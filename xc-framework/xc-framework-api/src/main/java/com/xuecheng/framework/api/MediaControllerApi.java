package com.xuecheng.framework.api;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;

public interface MediaControllerApi {
	//上传媒体文件的注册
	public ResponseResult register(String fileMd5, String fileName,
			String fileSize, String mimeType, String fileExt);
	
	//分块检查
	public CheckChunkResult checkChunk(String fileMd5, 
			Integer chunk, Integer chunkSize);
	
	//上传分块
	public ResponseResult uploadChunkFile(MultipartFile file, 
			Integer chunk, String fileMd5);
	
	//合并媒体文件
	public ResponseResult mergeChunkFile(String fileMd5, String fileName,
			String fileSize, String mimeType, String fileExt);
}
