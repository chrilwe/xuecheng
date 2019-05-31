package com.xuecheng.paysystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.XcADControllerApi;
import com.xuecheng.framework.common.model.response.ResponseResult;

@RestController
@RequestMapping("/ad")
public class XcADController implements XcADControllerApi {
	
	/**
	 * 创建支付宝投放广告
	 */
	@Override
	public ResponseResult marketingCdpAdvertise() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 修改支付宝投放广告
	 */
	@Override
	public ResponseResult marketingCdpAdvertiseModify() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 操作支付宝投放广告
	 */
	@Override
	public ResponseResult marketingCdpAdvertiseOperate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询支付宝投放广告
	 */
	@Override
	public ResponseResult marketingCdpAdvertiseQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
