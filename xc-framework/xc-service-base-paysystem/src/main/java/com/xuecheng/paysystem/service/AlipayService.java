package com.xuecheng.paysystem.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.PayQrcodeResult;
import com.xuecheng.paysystem.alipay.trade.model.result.AlipayF2FPayResult;

public interface AlipayService {
	public PayQrcodeResult createQrcode(String orderNum);//创建支付二维码
	public String alipayNotice(HttpServletRequest request, Map<String,String> params);//支付宝异步通知商户
	public AlipayF2FPayResult alipayTradeQuery(String orderNum);//查询支付宝订单状态	
	public AlipayTradeRefundResponse alipayTradeRefund(String orderNum);//支付宝退款
}
