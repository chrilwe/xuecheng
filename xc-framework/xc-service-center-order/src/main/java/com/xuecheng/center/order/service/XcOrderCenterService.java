package com.xuecheng.center.order.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.request.CreateOrderRequest;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.response.CreateOrderResult;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;

public interface XcOrderCenterService {
	// 创建订单
	public CreateOrderResult createOrder(CreateOrderRequest createOrderRequest,String userId);

	// 根据订单号查询订单
	public OrderResult queryOrdersByOrderNum(String orderNum);
	
	//更新订单
	public OrderResult updateOrder(XcOrders xcOrders, List<XcOrdersDetail> xcOrderDetails, XcOrdersPay xcOrdersPay);
	
	//我的订单列表
	public QueryOrderListResult queryOrderList(QueryOrderListRequest queryOrdersListRequest,String userId);
	
	//取消订单
	public ResponseResult cancelOrder(String orderNumber);
	
}
