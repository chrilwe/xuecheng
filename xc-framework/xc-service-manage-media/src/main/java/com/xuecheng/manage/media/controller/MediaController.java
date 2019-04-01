package com.xuecheng.manage.media.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.api.MediaControllerApi;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.manage.media.service.MediaService;

@RestController
@RequestMapping("/media/upload")
public class MediaController implements MediaControllerApi {
	
	@Autowired
	private MediaService mediaService;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Topic topic;
	
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
		ResponseResult result = mediaService.mergeChunkFile(fileMd5, fileName, fileSize, mimeType, fileExt);
		try {
			if(result.isSuccess()) {
				//发送消息到mq
				jmsTemplate.convertAndSend(topic, fileMd5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
}
