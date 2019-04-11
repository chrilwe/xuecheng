package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.ResponseResult;

public interface MediaProcessorControllerApi {
	public ResponseResult processVideo(String fileMd5);
}
