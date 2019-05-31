package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.ResponseResult;

public interface XcHandlerErrOrderControllerApi {
	public ResponseResult handlePaySuccessButOrderPayFailed(String orderNumber);
	public ResponseResult handleRefundSuccessButOrderRefundFailed();
}
