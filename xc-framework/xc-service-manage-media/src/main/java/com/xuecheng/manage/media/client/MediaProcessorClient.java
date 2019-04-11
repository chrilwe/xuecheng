package com.xuecheng.manage.media.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.ResponseResult;

@FeignClient(XcServiceList.XC_SERVICE_MANAGE_MEDIA_PROCESSOR)
public interface MediaProcessorClient {
	
	@GetMapping("/media/processor/process_video/{fileMd5}")
	public ResponseResult processVideo(@PathVariable("fileMd5")String fileMd5);
}
