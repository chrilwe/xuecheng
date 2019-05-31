package com.xuecheng.center.order.service;

import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.model.response.ResponseResult;

public interface XcHandlerErrOrderService {
	public ResponseResult handleRefundSuccessButOrderRefundFailed(String orderNumber);
}
