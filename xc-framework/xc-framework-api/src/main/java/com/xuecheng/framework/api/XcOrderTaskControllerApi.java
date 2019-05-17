package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.ResponseResult;

public interface XcOrderTaskControllerApi {
	//开启订单超时支付监控
	public ResponseResult openOrderTask();
}
