package com.xuecheng.framework.api;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
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
	
	//分页查询我的媒资文件
	public QueryResponseResult findMediaFileList(int page, int size,
			QueryMediaFileRequest queryMediaFileRequest);
	
	//删除媒资文件
	public ResponseResult deleteMediaFileByFileId(String fileId);
	
	//手动处理没有处理成功的媒体文件
	public ResponseResult processVideoFile(String fileMd5);
	
	//批量查询媒体文件
	public QueryResponseResult findMediaFileByMediaIds(String mediaIds);
}
