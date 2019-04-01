package com.xuecheng.manage.course.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

@FeignClient(XcServiceList.XC_SERVICE_BASE_FILESYSTEM)
public interface FileSystemClient {
	@PostMapping("/filesystem/upload")
	public UploadFileResult uploadFile(MultipartFile multipartFile, String filetag, String businesskey,
			String metedata);
}
