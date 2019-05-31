package com.xuecheng.framework.domain.order.response;

import java.util.List;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;

public class QueryRefundResult extends ResponseResult {
	private String orderNumber;
	private String refundAmount;// 本次退款请求，对应的退款金额
	private String totalAmount;// 该笔退款所对应的交易的订单金额

	public QueryRefundResult() {
		super();
	}

	public QueryRefundResult(ResultCode resultCode, String orderNumber, String refundAmount, String totalAmount) {
		super(resultCode);
		this.orderNumber = orderNumber;
		this.refundAmount = refundAmount;
		this.totalAmount = totalAmount;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "QueryRefundResult [orderNumber=" + orderNumber + ", refundAmount=" + refundAmount + ", totalAmount="
				+ totalAmount + "]";
	}

}
