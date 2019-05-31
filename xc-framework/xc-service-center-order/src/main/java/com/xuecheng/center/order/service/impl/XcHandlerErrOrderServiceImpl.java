package com.xuecheng.center.order.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuecheng.center.order.client.XcCourseClient;
import com.xuecheng.center.order.client.XcPayClient;
import com.xuecheng.center.order.mapper.XcOrdersDetailMapper;
import com.xuecheng.center.order.mapper.XcOrdersMapper;
import com.xuecheng.center.order.mapper.XcOrdersPayMapper;
import com.xuecheng.center.order.service.XcHandlerErrOrderService;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.response.OrderCode;
import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.QueryRefundResult;
import com.xuecheng.framework.domain.order.status.XcOrderStatus;

@Service
public class XcHandlerErrOrderServiceImpl implements XcHandlerErrOrderService {
	
	@Autowired
	private XcOrdersMapper xcOrdersMapper;
	@Autowired
	private XcOrdersPayMapper xcOrdersPayMapper;
	@Autowired
	private XcOrdersDetailMapper xcOrdersDetailMapper;
	@Autowired
	private XcPayClient xcPayClient;
	
	//人工干预处理支付宝退款成功，但是商户系统异常导致订单退款失败
	@Override
	@Transactional
	public ResponseResult handleRefundSuccessButOrderRefundFailed(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			ExceptionCast.cast(OrderCode.ORDER_NUMBER_ISNULL);
		}
		
		//只允许商户支付成功状态的订单退款
		XcOrdersPay xcOrdersPay = xcOrdersPayMapper.findByOrderNum(orderNumber);
		XcOrders xcOrders = xcOrdersMapper.findByOrderNum(orderNumber);
		if(xcOrdersPay == null) {
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		if(!xcOrdersPay.getStatus().equals(XcOrderStatus.PAY_YES)) {
			ExceptionCast.cast(OrderCode.ORDER_PAY_ISNOTSUCCESS);
		}
		
		//查询支付宝退款是否成功
		QueryRefundResult result = xcPayClient.queryRefundTrade(orderNumber);
		if(!result.isSuccess()) {
			ExceptionCast.cast(OrderCode.REFUND_QUERY_FAILED);
		}
		//修改订单
		xcOrders.setStatus(XcOrderStatus.PAY_CANCEL);
		xcOrdersPay.setStatus(XcOrderStatus.PAY_CANCEL);
		xcOrdersMapper.update(xcOrders);
		xcOrdersPayMapper.update(xcOrdersPay);
		return new ResponseResult(CommonCode.SUCCESS);
	}

}
