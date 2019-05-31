package com.xuecheng.paysystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.domain.order.response.OrderCode;

@Service
public class AlipayRefundQueryService {
	
	@Autowired
	private AlipayClient alipayClient;
	
	/**
	 * 支付宝退款查询
	 * @throws AlipayApiException 
	 */
	public AlipayTradeFastpayRefundQueryResponse queryRefundTrade(String orderNumber) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizContent("{" +
		"\"out_trade_no\":"+"\""+orderNumber+"\"" +
		"  }");
		AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			//调用成功，判断退款是否成功
			if(response.getBody() != null) {
				return response;
			}
		} else {
			ExceptionCast.cast(OrderCode.REFUND_QUERY_FAILED);
		}
		return null;
	}
}
