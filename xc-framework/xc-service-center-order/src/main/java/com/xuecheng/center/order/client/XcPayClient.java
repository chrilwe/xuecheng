package com.xuecheng.center.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.order.response.PayOrderResult;

@FeignClient(XcServiceList.XC_SERVICE_BASE_PAYSYSTEM)
public interface XcPayClient {
	/**
	 * 商户平台主动发起查询支付宝订单状态
	 */
	@GetMapping("/alipay/trade/query/{orderNum}")
	public PayOrderResult alipayTradeQuery(@PathVariable("orderNum")String orderNum);
}
