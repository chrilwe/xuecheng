package com.xuecheng.paysystem.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.request.UpdateOrderRequest;
import com.xuecheng.framework.domain.order.response.OrderResult;

@FeignClient(XcServiceList.XC_SERVICE_CENTER_ORDER)
@RequestMapping("/order")
public interface XcOrderClient {
	
	/**
	 * 根据订单号查询订单
	 */
	@GetMapping("/query/{orderNum}")
	public OrderResult queryOrdersByOrderNum(@PathVariable("orderNum")String orderNum);
	
	/**
	 * 更新订单状态
	 */
	@PostMapping("/update")
	public OrderResult updateOrder(UpdateOrderRequest updateOrderRequest);
}
