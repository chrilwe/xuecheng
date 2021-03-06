package com.xuecheng.paysystem.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.xuecheng.framework.api.XcPayControllerApi;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.web.BaseController;
import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.PayQrcodeResult;
import com.xuecheng.framework.domain.order.response.PayRefundResult;
import com.xuecheng.framework.domain.order.response.QueryRefundResult;
import com.xuecheng.paysystem.alipay.trade.config.Configs;
import com.xuecheng.paysystem.alipay.trade.model.result.AlipayF2FPayResult;
import com.xuecheng.paysystem.service.AlipayRefundQueryService;
import com.xuecheng.paysystem.service.AlipayService;

@RestController
@RequestMapping("/pay")
public class XcPayController extends BaseController implements XcPayControllerApi {
	
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private AlipayRefundQueryService alipayRefundQueryService;
	
	/**
	 * 支付宝生成二维码连接地址
	 */
	@Override
	@GetMapping("/alipay/qrcode/{orderNum}")
	public PayQrcodeResult alipay_createQrcode(@PathVariable("orderNum")String orderNum) {
		PayQrcodeResult result = alipayService.createQrcode(orderNum);
		return result;
	}
	
	/**
	 * 支付宝支付成功,异步通知商户
	 */
	@Override
	@RequestMapping("/alipay/callback")
	public String alipayCallback() {
		//从request中获取回调信息
		Map<String, String> map = this.alipayNoticeParamsAsMap(request);
		String result = alipayService.alipayNotice(request, map);
		return result;
	}
	
	/**
	 * 回调信息
	 */
	private Map<String, String> alipayNoticeParamsAsMap(HttpServletRequest request) {
		Map<String,String> resultMap = new HashMap<String,String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		for(Iterator iterator = parameterMap.keySet().iterator();iterator.hasNext();) {
			String key = (String) iterator.next();
			String[] values = (String[])parameterMap.get(key);
			String value = "";
			for(int i = 0;i < values.length;i++) {
				value += (i == values.length-1)?values[i]:values[i]+","; 
			}
			resultMap.put(key, value);
		}
		return resultMap;
	}
	
	/**
	 * 商户平台主动发起查询支付宝订单状态
	 */
	@Override
	@GetMapping("/alipay/trade/query/{orderNum}")
	public PayOrderResult alipayTradeQuery(@PathVariable("orderNum")String orderNum) {
		AlipayF2FPayResult alipayTradeQuery = alipayService.alipayTradeQuery(orderNum);
		PayOrderResult result = new PayOrderResult();
		if(alipayTradeQuery.isTradeSuccess()) {
			result.setCode(Integer.parseInt(alipayTradeQuery.getResponse().getCode()));
			result.setOrderNumber(alipayTradeQuery.getResponse().getTradeNo());
			result.setMoney(Float.valueOf(alipayTradeQuery.getResponse().getTotalAmount()));
			result.setSuccess(true);
			return result;
		} else {
			//
			result.setCode(Integer.parseInt(alipayTradeQuery.getResponse().getCode()));
			result.setOrderNumber(alipayTradeQuery.getResponse().getTradeNo());
			result.setMoney(Float.valueOf(alipayTradeQuery.getResponse().getTotalAmount()));
			result.setSuccess(false);
		}
		return null;
	}
	
	/**
	 * 支付宝退款
	 */
	@Override
	@GetMapping("/alipay/refund/{orderNum}")
	public PayRefundResult alipayTradeRefund(@PathVariable("orderNum")String orderNum) {
		AlipayTradeRefundResponse result = alipayService.alipayTradeRefund(orderNum);
		if(result.isSuccess()) {
			return new PayRefundResult(CommonCode.SUCCESS,result.getOutTradeNo(),result.getRefundFee());
		}
		return null;
	}
	
	/**
	 * 查询支付宝退款交易信息
	 */
	@Override
	@GetMapping("/alipay/refund/query/{orderNum}")
	public QueryRefundResult queryRefundTrade(@PathVariable("orderNumber")String orderNumber) {
		QueryRefundResult result = null;
		AlipayTradeFastpayRefundQueryResponse response = null;
		try {
			response = alipayRefundQueryService.queryRefundTrade(orderNumber);
			if(response == null) {
				result = new QueryRefundResult(CommonCode.FAIL,orderNumber,"","");
			}
		} catch (AlipayApiException e) {
			result = new QueryRefundResult(CommonCode.FAIL,orderNumber,"","");
		}
		return new QueryRefundResult(CommonCode.SUCCESS,orderNumber,response.getRefundAmount(),response.getTotalAmount());
	}
	
}
