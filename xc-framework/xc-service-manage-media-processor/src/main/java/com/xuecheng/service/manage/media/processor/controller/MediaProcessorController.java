package com.xuecheng.service.manage.media.processor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.MediaProcessorControllerApi;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.service.manage.media.processor.service.MediaProcessorService;

@RestController
@RequestMapping("/media/processor")
public class MediaProcessorController implements MediaProcessorControllerApi {
	
	@Autowired
	private MediaProcessorService mediaProcessorService;
	
	@Override
	@GetMapping("/process_video/{fileMd5}")
	public ResponseResult processVideo(@PathVariable("fileMd5")String fileMd5) {
		mediaProcessorService.processMedia(fileMd5);
		return new ResponseResult(CommonCode.SUCCESS);
	}

}
