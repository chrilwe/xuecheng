package com.xuecheng.manage.media.service;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
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
			String fileExt, String userId);
	
	//分页条件查询媒资文件
	public QueryResponseResult findMediaFileList(int page, int size,
			QueryMediaFileRequest queryMediaFileRequest, String userId);
	
	//删除本机构媒资文件
	public ResponseResult deleteMediaFileByFileId(String fileMd5, String userId);
	
	//手动处理未处理成功的媒体文件
	public ResponseResult processVideo(String fileMd5);
	
	//批量查询mediafile
	public QueryResponseResult findMediaFileByMediaIds(String medisIds);
}
