package com.xuecheng.center.order.service;

import java.util.List;

import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;

public interface XcOrderMonitorService {
	//更新订单状态
	public OrderResult updateXcOrdersAndXcOrdersPay(XcOrders xcOrders,XcOrdersPay xcOrdersPay);
	//分页查询订单
	public QueryOrderListResult queryOrderList(QueryOrderListRequest queryOrdersListRequest);
}
