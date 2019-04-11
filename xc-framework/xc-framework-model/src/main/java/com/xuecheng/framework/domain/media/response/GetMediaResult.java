package com.xuecheng.framework.domain.media.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;

public class GetMediaResult extends ResponseResult {
	
	private String fileUrl;
	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public GetMediaResult(ResultCode resultCode, String fileUrl) {
		super(resultCode);
		this.fileUrl = fileUrl;
	}

}
