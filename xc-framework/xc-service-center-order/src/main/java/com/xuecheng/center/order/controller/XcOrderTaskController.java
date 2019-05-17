package com.xuecheng.center.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.center.order.task.OrderTaskPool;
import com.xuecheng.framework.api.XcOrderTaskControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.order.response.OrderCode;

@RestController
@RequestMapping("/order/task")
public class XcOrderTaskController implements XcOrderTaskControllerApi {
	
	/**
	 * 订单超时监控开启
	 */
	@Override
	@GetMapping("/start")
	public ResponseResult openOrderTask() {
		try {
			OrderTaskPool pool = OrderTaskPool.getInstance();
			if(pool == null) {
				ExceptionCast.cast(OrderCode.ORDER_TASK_MONITOR);
			}
		} catch (Exception e) {
			ExceptionCast.cast(OrderCode.ORDER_TASK_MONITOR);
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}

}
