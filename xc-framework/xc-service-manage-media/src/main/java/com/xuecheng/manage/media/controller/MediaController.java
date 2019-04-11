package com.xuecheng.manage.media.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.api.MediaControllerApi;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.manage.media.config.RabbitMqConfig;
import com.xuecheng.manage.media.service.MediaService;

@RestController
@RequestMapping("/media")
public class MediaController implements MediaControllerApi {
	
	@Autowired
	private MediaService mediaService;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * 文件上传注册(检查是否存在文件目录，不存在就创建)
	 * 一级目录 : fileMd5的第一个字符
	 * 二级目录 ：fileMd5的第二个字符
	 * 三级目录 ：fileMd5
	 * 文件名 ：fileMd5 + 文件扩展名
	 * @param fileExt 文件扩展名
	 */
	@Override
	@PostMapping("/register")
	public ResponseResult register(String fileMd5, String fileName, 
			String fileSize, String mimeType, String fileExt) {
		try {
			return mediaService.register(fileMd5, fileName, fileSize, mimeType, fileExt);
		} catch (Exception e) {
			return new ResponseResult(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
		}
	}
	
	/**
	 * 检查块文件
	 */
	@Override
	@PostMapping("/checkchunk")
	public CheckChunkResult checkChunk(String fileMd5, Integer chunk, 
			Integer chunkSize) {
		try {
			return mediaService.checkChunk(fileMd5, chunk, chunkSize);
		} catch (Exception e) {
			return new CheckChunkResult(MediaCode.UPLOAD_FILE_REGISTER_FAIL, false);
		}
	}
	
	/**
	 * 上传块文件
	 */
	@Override
	@PostMapping("/uploadchunk")
	public ResponseResult uploadChunkFile(MultipartFile file, 
			Integer chunk, String fileMd5) {
		
		return mediaService.uploadChunkFile(file, chunk, fileMd5);
	}
	
	/**
	 * 合并块文件,成功之后发送消息到mq通知处理视频进程将视频转化为m3u8文件
	 */
	@Override
	@PostMapping("/mergechunks")
	public ResponseResult mergeChunkFile(String fileMd5, 
			String fileName, String fileSize, String mimeType,
			String fileExt) {
		String userId = "";
		ResponseResult result = mediaService.mergeChunkFile(fileMd5, fileName, fileSize, mimeType, fileExt, userId);
		try {
			if(result.isSuccess()) {
				//发送消息到mq
				rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM, "inform.#.media.#", fileMd5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	/**
	 * 条件分页查询媒体文件
	 */
	@Override
	@GetMapping("/list/{page}/{size}")
	public QueryResponseResult findMediaFileList(@PathVariable("page")int page, 
			@PathVariable("size")int size, QueryMediaFileRequest queryMediaFileRequest) {
		String userId = "";
		return mediaService.findMediaFileList(page, size, queryMediaFileRequest, userId);
	}
	
	/**
	 * 删除媒体文件
	 */
	@Override
	@GetMapping("/delete/{fileId}")
	public ResponseResult deleteMediaFileByFileId(@PathVariable("fileId")String fileId) {
		String userId = "";
		return mediaService.deleteMediaFileByFileId(fileId, userId);
	}
	
	/**
	 * 人工处理未自动处理的视频
	 */
	@Override
	@GetMapping("/process_video/{fileMd5}")
	public ResponseResult processVideoFile(@PathVariable("fileMd5")String fileMd5) {
		
		return mediaService.processVideo(fileMd5);
	}
	
}
