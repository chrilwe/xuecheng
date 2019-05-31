package com.xuecheng.framework.api;

import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.PayQrcodeResult;
import com.xuecheng.framework.domain.order.response.PayRefundResult;
import com.xuecheng.framework.domain.order.response.QueryRefundResult;

public interface XcPayControllerApi {
	public PayQrcodeResult alipay_createQrcode(String orderNum);//支付宝下单二维码地址
	public String alipayCallback();//支付宝支付成功回调
	public PayOrderResult alipayTradeQuery(String orderNum);//查询支付宝订单状态
	public PayRefundResult alipayTradeRefund(String orderNum);//支付宝退款
	public QueryRefundResult queryRefundTrade(String orderNumber);//查询支付宝退款交易信息
}
