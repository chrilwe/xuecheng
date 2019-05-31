package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.ResponseResult;

public interface XcADControllerApi {
	//创建支付宝投放广告
	public ResponseResult marketingCdpAdvertise();
	//修改支付宝投放广告
	public ResponseResult marketingCdpAdvertiseModify();
	//操作支付宝投放广告
	public ResponseResult marketingCdpAdvertiseOperate();
	//查询支付宝投放广告
	public ResponseResult marketingCdpAdvertiseQuery();
}
