package com.xuecheng.framework.domain.order.status;

public interface XcOrderStatus {
	//未支付编码
	public static final String PAY_NO = "201";
	//支付中编码
	public static final String PAYING = "202";
	//已支付编码
	public static final String PAY_YES = "200";
	//已退款编码
	public static final String PAY_REFUND = "203";
	//已取消编码
	public static final String PAY_CANCEL = "204";
}
