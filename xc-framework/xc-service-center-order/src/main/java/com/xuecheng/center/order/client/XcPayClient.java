package com.xuecheng.center.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.QueryRefundResult;

@FeignClient(XcServiceList.XC_SERVICE_BASE_PAYSYSTEM)
public interface XcPayClient {
	/**
	 * 商户平台主动发起查询支付宝订单状态
	 */
	@GetMapping("/pay/alipay/trade/query/{orderNum}")
	public PayOrderResult alipayTradeQuery(@PathVariable("orderNum")String orderNum);
	
	/**
	 * 查询支付宝退款交易信息
	 */
	@GetMapping("/pay/alipay/refund/query/{orderNum}")
	public QueryRefundResult queryRefundTrade(@PathVariable("orderNumber")String orderNumber);
}
