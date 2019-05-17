package com.xuecheng.framework.api;

import java.util.List;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.request.CreateOrderRequest;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.request.UpdateOrderRequest;
import com.xuecheng.framework.domain.order.response.CreateOrderResult;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;

public interface XcOrderControllerApi {
	//创建订单
	public CreateOrderResult createOrder(CreateOrderRequest createOrderRequest);
	//根据订单号查询订单
	public OrderResult queryOrdersByOrderNum(String orderNum);
	//更新订单状态
	public OrderResult updateOrder(UpdateOrderRequest updateOrderRequest);
	//我的订单列表
	public QueryOrderListResult queryOrderList(QueryOrderListRequest queryOrdersListRequest);
	//取消订单
	public ResponseResult cancelOrder(String orderNumber);
}
