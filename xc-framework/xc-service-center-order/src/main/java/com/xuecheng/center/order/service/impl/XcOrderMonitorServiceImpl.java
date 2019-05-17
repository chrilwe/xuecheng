package com.xuecheng.center.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.center.order.mapper.XcOrdersDetailMapper;
import com.xuecheng.center.order.mapper.XcOrdersMapper;
import com.xuecheng.center.order.mapper.XcOrdersPayMapper;
import com.xuecheng.center.order.service.XcOrderMonitorService;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.ext.OrderExt;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.response.OrderCode;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;
import com.xuecheng.framework.domain.order.status.XcOrderStatus;

@Service
public class XcOrderMonitorServiceImpl implements XcOrderMonitorService {
	@Autowired
	private XcOrdersMapper xcOrdersMapper;
	@Autowired
	private XcOrdersPayMapper xcOrdersPayMapper;
	@Autowired
	private XcOrdersDetailMapper xcOrdersDetailMapper;
	
	/**
	 * 订单监控更新订单状态
	 */
	@Override
	@Transactional
	public OrderResult updateXcOrdersAndXcOrdersPay(XcOrders xcOrders,XcOrdersPay xcOrdersPay) {
		if(xcOrders == null || xcOrdersPay == null) {
			ExceptionCast.cast(OrderCode.ORDER_QUERY_PARAMS_ERROR);
		}
		xcOrdersMapper.update(xcOrders);
		xcOrdersPayMapper.update(xcOrdersPay);
		return new OrderResult(CommonCode.SUCCESS);
	}
	
	/**
	 * 订单超时监控分页查询
	 */
	@Override
	public QueryOrderListResult queryOrderList(QueryOrderListRequest queryOrdersListRequest) {
		if(queryOrdersListRequest == null || StringUtils.isEmpty(queryOrdersListRequest.getOrderNumber())) {
			ExceptionCast.cast(OrderCode.ORDER_QUERY_PARAMS_ERROR);
		}
		int currentPage = queryOrdersListRequest.getCurrentPage();
		String orderNumber = queryOrdersListRequest.getOrderNumber();
		int pageSize = queryOrdersListRequest.getPageSize();
		String status = queryOrdersListRequest.getStatus();
		if(pageSize <= 0) {
			pageSize = 10;
		}
		if(currentPage <= 0) {
			currentPage = 1;
		}
		int start = (currentPage - 1) * pageSize;
		if(StringUtils.isEmpty(status)) {
			status = XcOrderStatus.PAY_NO;
		}
		
		//查询未支付订单
		List result = null;
		List<XcOrders> xcOrdersList = xcOrdersMapper.findNoPayOrders(start, pageSize, status);
		int total = xcOrdersMapper.countByStatus1(status);
		if(xcOrdersList == null || xcOrdersList.size() <= 0) {
			return new QueryOrderListResult(CommonCode.SUCCESS,total,null);
		}
		for(XcOrders xcOrders : xcOrdersList) {
			XcOrdersPay xcOrdersPay = xcOrdersPayMapper.findByOrderNum(xcOrders.getOrderNumber());
			OrderExt orderExt = new OrderExt();
			orderExt.setXcOrders(xcOrders);
			orderExt.setXcOrdersPay(xcOrdersPay);
			result.add(orderExt);
		}
		return new QueryOrderListResult(CommonCode.SUCCESS,total,result);
	}

}
