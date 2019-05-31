package com.xuecheng.framework.domain.order.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;

public class PayRefundResult extends ResponseResult {
	private String orderNumber;
	private String refundAmount;// 本次退款请求，对应的退款金额
	public PayRefundResult(ResultCode resultCode,String orderNumber,String refundAmount) {
		super(resultCode);
		this.orderNumber = orderNumber;
		this.refundAmount = refundAmount;
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
	@Override
	public String toString() {
		return "PayRefundResult [orderNumber=" + orderNumber + ", refundAmount=" + refundAmount + "]";
	}
	public PayRefundResult() {
		super();
	}
	
}
