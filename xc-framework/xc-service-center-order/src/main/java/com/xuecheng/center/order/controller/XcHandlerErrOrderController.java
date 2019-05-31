package com.xuecheng.center.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.center.order.service.XcHandlerErrOrderService;
import com.xuecheng.framework.common.model.response.ResponseResult;

@RestController
@RequestMapping("/order/process")
public class XcHandlerErrOrderController {
	
	@Autowired
	private XcHandlerErrOrderService xcHandlerErrOrderService;
	
	/**
	 * 人工干预退款成功，但是修改订单发生异常导致订单状态错误
	 */
	@GetMapping("/refund/error/{orderNumber}")
	public ResponseResult handleRefundSuccessButOrderRefundFailed(@PathVariable("orderNumber")String orderNumber) {
		
		return xcHandlerErrOrderService.handleRefundSuccessButOrderRefundFailed(orderNumber);
	}
}
