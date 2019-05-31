package com.xuecheng.paysystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayMarketingCdpAdvertiseCreateRequest;
import com.alipay.api.response.AlipayMarketingCdpAdvertiseCreateResponse;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.paysystem.service.XcADService;

@Service
public class XcADServiceImpl implements XcADService {
	
	@Autowired
	private AlipayClient alipayClient;

	/**
	 * 创建支付宝投放广告
	 */
	@Override
	public ResponseResult marketingCdpAdvertise() {
		AlipayMarketingCdpAdvertiseCreateRequest request = new AlipayMarketingCdpAdvertiseCreateRequest();
		request.setBizContent("{" +
		"    \"ad_code\":\"CDP_OPEN_MERCHANT\"," +
		"    \"content_type\":\"URL\"," +
		"    \"content\":\"http://m.alipay.com/J/fdfd\"," +
		"    \"action_url\":\"http://m.alipay.com/J/dfdf\"," +
		"    \"ad_rules\":\"{\"shop_id\":[\"2015090800077000000002549828\"]}\"," +
		"    \"height\":\"100\"," +
		"    \"start_time\":\"2016-02-24 12:12:12\"," +
		"    \"end_time\":\"2017-02-24 12:12:12\"" +
		"  }");
		try {
			AlipayMarketingCdpAdvertiseCreateResponse response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
