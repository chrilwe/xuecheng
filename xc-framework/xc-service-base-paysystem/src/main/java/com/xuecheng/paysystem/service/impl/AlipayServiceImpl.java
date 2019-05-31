package com.xuecheng.paysystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.message.Message;
import com.xuecheng.framework.domain.message.MessageMQList;
import com.xuecheng.framework.domain.message.MessageStatus;
import com.xuecheng.framework.domain.message.response.MessageCode;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.request.UpdateOrderRequest;
import com.xuecheng.framework.domain.order.response.OrderCode;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.PayQrcodeResult;
import com.xuecheng.framework.domain.order.status.XcOrderStatus;
import com.xuecheng.paysystem.alipay.trade.config.Configs;
import com.xuecheng.paysystem.alipay.trade.config.Constants;
import com.xuecheng.paysystem.alipay.trade.model.ExtendParams;
import com.xuecheng.paysystem.alipay.trade.model.GoodsDetail;
import com.xuecheng.paysystem.alipay.trade.model.PayStatus;
import com.xuecheng.paysystem.alipay.trade.model.TradeStatus;
import com.xuecheng.paysystem.alipay.trade.model.builder.AlipayTradeCancelRequestBuilder;
import com.xuecheng.paysystem.alipay.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.xuecheng.paysystem.alipay.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.xuecheng.paysystem.alipay.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.xuecheng.paysystem.alipay.trade.model.builder.RequestBuilder;
import com.xuecheng.paysystem.alipay.trade.model.result.AlipayF2FPayResult;
import com.xuecheng.paysystem.alipay.trade.model.result.AlipayF2FPrecreateResult;
import com.xuecheng.paysystem.alipay.trade.model.result.AlipayF2FQueryResult;
import com.xuecheng.paysystem.alipay.trade.model.result.AlipayF2FRefundResult;
import com.xuecheng.paysystem.alipay.trade.service.AlipayTradeService;
import com.xuecheng.paysystem.alipay.trade.utils.Utils;
import com.xuecheng.paysystem.client.XcMessageClient;
import com.xuecheng.paysystem.client.XcOrderClient;
import com.xuecheng.paysystem.demo.Main;
import com.xuecheng.paysystem.service.AlipayService;

@Service
public class AlipayServiceImpl implements AlipayService {
	private static Log log = LogFactory.getLog(AlipayServiceImpl.class);
	private static ExecutorService executorService = Executors.newCachedThreadPool();

	@Autowired
	private AlipayClient alipayClient;
	@Autowired
	private AlipayTradeService tradeService;
	@Autowired
	private XcOrderClient xcOrderClient;
	@Autowired
	private XcMessageClient xcMessageClient;

	@Value("${xuecheng.alipay.refund_ttl}")
	private long refundTtl;// 退款超时（分钟）
	@Value("${xuecheng.alipay.store_id}")
	private String storeId;
	@Value("${xuecheng.alipay.pay_ttl}")
	private String payTtl;
	@Value("${xuecheng.alipay.notify_url}")
	private String notifyUrl;

	/**
	 * 支付宝预下单生成二维码连接地址
	 */
	@Override
	public PayQrcodeResult createQrcode(String orderNum) {
		if (StringUtils.isEmpty(orderNum)) {
			ExceptionCast.cast(OrderCode.ORDER_NUMBER_ISNULL);
		}
		
		//根据订单号查询订单
		OrderResult orderResult = xcOrderClient.queryOrdersByOrderNum(orderNum);
		if(!orderResult.isSuccess()) {
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		
		//判断订单状态是否为未支付
		XcOrders xcOrders = orderResult.getXcOrders();
		List<XcOrdersDetail> list = orderResult.getXcOrdersDetails();
		XcOrdersPay xcOrdersPay = orderResult.getXcOrdersPay();
		if(!xcOrdersPay.getStatus().equals(XcOrderStatus.PAY_NO)) {
			ExceptionCast.cast(OrderCode.ORDER_PAY_ISNOTSUCCESS);
		}
		
		String totalAmount = "";
		if(xcOrders.getPrice() < 100) {
			totalAmount = "0." + xcOrders.getPrice();
		} else {
			totalAmount = xcOrders.getPrice()/100 +"."+(xcOrders.getPrice()-xcOrders.getPrice()/100);
		}
		// 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
		String body = "购买课程共"+xcOrders.getPrice()+"元";
		// 商户操作员编号，添加此参数可以为商户操作员做销售统计
		String operatorId = "test_operator_id";
		// 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
		ExtendParams extendParams = new ExtendParams();
		extendParams.setSysServiceProviderId("2088100200300400500");

		// 商品明细列表，需填写购买商品详细信息，
		List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
		for(XcOrdersDetail xcOrdersDetail : list) {
			GoodsDetail goodsDetail = GoodsDetail.newInstance(xcOrdersDetail.getItemId(), xcOrdersDetail.getItemTitle(), xcOrdersDetail.getItemPrice(), xcOrdersDetail.getItemNum());
			goodsDetailList.add(goodsDetail);
		}

		// 创建扫码支付请求builder，设置请求参数
		AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder().setSubject("学成在线支付宝扫码支付")
				.setTotalAmount(totalAmount).setOutTradeNo(orderNum).setUndiscountableAmount("0")
				.setSellerId("").setBody(body).setOperatorId(operatorId).setStoreId(storeId)
				.setExtendParams(extendParams).setTimeoutExpress(payTtl)
				.setNotifyUrl(notifyUrl)//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
				.setGoodsDetailList(goodsDetailList);

		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);

		// 校验预下单是否成功
		AlipayTradePrecreateResponse response = result.getResponse();
		String code = response.getCode();
		if (Constants.SUCCESS.equals(code)) {
			// 预下单成功
			UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
			xcOrders.setStatus(XcOrderStatus.PAYING);
			xcOrdersPay.setStatus(XcOrderStatus.PAYING);
			updateOrderRequest.setXcOrders(xcOrders);
			updateOrderRequest.setXcOrdersDetails(list);
			updateOrderRequest.setXcOrdersPay(xcOrdersPay);
			PayQrcodeResult payQrcodeResult = new PayQrcodeResult(CommonCode.SUCCESS);
			payQrcodeResult.setCodeUrl(response.getQrCode());
			payQrcodeResult.setOrderNumber(orderNum);
			return payQrcodeResult;
		}
		if (Constants.ERROR.equals(code)) {
			// 预下单系统异常
			ExceptionCast.cast(OrderCode.PAY_ERROR);
		}
		if (Constants.FAILED.equals(code)) {
			// 预下单失败
			ExceptionCast.cast(OrderCode.PAY_FAILED);
		}
		if (Constants.PAYING.equals(code)) {
			// 正在支付中
			ExceptionCast.cast(OrderCode.PAY_PAYING);
		}

		return null;
	}

	/**
	 * 支付宝异步通知商户
	 */
	@Override
	public String alipayNotice(HttpServletRequest request, Map<String, String> params) {
		// 打印异步通知参数
		log.info("支付宝异步通知参数:" + JSON.toJSONString(params));
		
		try {
			//验证签名
			boolean rsaCheckV1 = AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), "UTF-8",
					Configs.getSignType());
			if(!rsaCheckV1) {
				ExceptionCast.cast(OrderCode.PAY_RSACHECKV1_FAIL);
			}
			
			//判断订单是否存在
			String outTradeNo = params.get("out_trade_no");
			OrderResult orderResult = xcOrderClient.queryOrdersByOrderNum(outTradeNo);
			if(orderResult.getXcOrdersPay() == null) {
				ExceptionCast.cast(OrderCode.Pay_USERERROR);
			}
			
			//只允许支付中状态的订单才能支付
			if(orderResult.getXcOrdersPay().getStatus().equals(XcOrderStatus.PAYING)) {
				//判断商家支付宝账号是否正确
				String seller_id = params.get("seller_id");
				if(!seller_id.equals(Configs.getPid())) {
					log.error("seller_id="+seller_id+"不正确！！");
					ExceptionCast.cast(OrderCode.PAY_SELLERID_OR_SELLEREMAIL_ERROR);
				}
				
				//判断支付金额是否正确
				String total_amount = params.get("total_amount");
				int price = orderResult.getXcOrders().getPrice();//订单实际金额（分）
				String realPrice = (price % 100 < 0)?("0."+price):((price/100)+"."+(price - price/100));
				if(!total_amount.equals(realPrice)) {
					ExceptionCast.cast(OrderCode.PAY_ORDER_MONENY_ISNOSAME);
				}
				
				//修改订单，自动添加我的课程
				String messageId = UUID.randomUUID().toString();
				Message message = new Message();
				message.setAreadyDead("NO");
				message.setCreateTime(new Date());
				message.setId(messageId);
				message.setMessageBody(JSON.toJSONString(orderResult));
				message.setMessageType("json");
				message.setRabbitExchange(MessageMQList.EXCHANGE_TOPIC_INFORM);
				message.setRabbitQueue(MessageMQList.QUEUE_INFORM_AUTOCHOOSECOURSE);
				message.setRoutingKey(MessageMQList.ROUTINGKEY_AUTOCHOOSECOURSE);
				message.setResendTime(0);
				message.setStatus(MessageStatus.READY_SENDING);
				try {
					ResponseResult readySendMessageResult = xcMessageClient.readySendMessage(message);
				} catch (Exception e) {
					ExceptionCast.cast(MessageCode.MESSAGE_READYSENDING_ERROR);
				}
				
				XcOrders xcOrders = orderResult.getXcOrders();
				xcOrders.setEndTime(new Date());
				xcOrders.setStatus(XcOrderStatus.PAY_YES);
				List<XcOrdersDetail> xcOrdersDetails = orderResult.getXcOrdersDetails();
				for(XcOrdersDetail xcOrdersDetail : xcOrdersDetails) {
					xcOrdersDetail.setEndTime(new Date());
				}
				XcOrdersPay xcOrdersPay = orderResult.getXcOrdersPay();
				xcOrdersPay.setStatus(XcOrderStatus.PAY_YES);
				UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
				updateOrderRequest.setXcOrders(xcOrders);
				updateOrderRequest.setXcOrdersDetails(xcOrdersDetails);
				updateOrderRequest.setXcOrdersPay(xcOrdersPay);
				OrderResult updateOrder = xcOrderClient.updateOrder(updateOrderRequest);
				
				xcMessageClient.confirmSendMessage(messageId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("支付宝通知商户发起回调异常:{}", e);
		}
		return PayStatus.SUCCESS;
	}

	/**
	 * 查询支付宝订单状态
	 */
	@Override
	public AlipayF2FPayResult alipayTradeQuery(String orderNum) {
		if (StringUtils.isEmpty(orderNum)) {
			ExceptionCast.cast(OrderCode.ORDER_NUMBER_ISNULL);
		}

		// 创建查询支付订单请求
		AlipayTradeQueryRequestBuilder queryBuiler = new AlipayTradeQueryRequestBuilder()
				.setOutTradeNo(orderNum);
		// 校验创建的请求
		validateBuilder(queryBuiler);

		// 轮询查询支付是否成功
		AlipayTradeQueryResponse queryResponse = loopQueryResult(queryBuiler);

		return checkQueryAndCancel(orderNum, queryBuiler.getAppAuthToken(),
				new AlipayF2FPayResult(new AlipayTradePayResponse()), queryResponse);
	}

	//校验请求
	private void validateBuilder(RequestBuilder builder) {
		if (builder == null) {
			throw new NullPointerException("builder should not be NULL!");
		}

		if (!builder.validate()) {
			throw new IllegalStateException("builder validate failed! " + builder.toString());
		}
	}

	//轮询查询支付订单
	private AlipayTradeQueryResponse loopQueryResult(AlipayTradeQueryRequestBuilder builder) {
		AlipayTradeQueryResponse queryResult = null;
		for (int i = 0; i < Configs.getMaxQueryRetry(); i++) {
			Utils.sleep(Configs.getQueryDuration());

			AlipayTradeQueryResponse response = tradeQuery(builder);
			if (response != null) {
				if (stopQuery(response)) {
					return response;
				}
				queryResult = response;
			}
		}
		return queryResult;
	}

	//查询支付订单
	private AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryRequestBuilder builder) {
		validateBuilder(builder);

		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
		request.setBizContent(builder.toJsonString());
		log.info("trade.query bizContent:" + request.getBizContent());
		return (AlipayTradeQueryResponse) getResponse(alipayClient, request);
	}

	// 判断是否停止查询
	private boolean stopQuery(AlipayTradeQueryResponse response) {
		if (Constants.SUCCESS.equals(response.getCode())) {
			if ("TRADE_FINISHED".equals(response.getTradeStatus()) || "TRADE_SUCCESS".equals(response.getTradeStatus())
					|| "TRADE_CLOSED".equals(response.getTradeStatus())) {
				// 如果查询到交易成功、交易结束、交易关闭，则返回对应结果
				return true;
			}
		}
		return false;
	}

	//根据查询结果queryResponse判断交易是否支付成功，如果支付成功则更新result并返回，如果不成功则调用撤销
	private AlipayF2FPayResult checkQueryAndCancel(String outTradeNo, String appAuthToken, AlipayF2FPayResult result,
			AlipayTradeQueryResponse queryResponse) {
		if (querySuccess(queryResponse)) {
			// 如果查询返回支付成功，则返回相应结果
			result.setTradeStatus(TradeStatus.SUCCESS);
			result.setResponse(toPayResponse(queryResponse));
			return result;
		}

		// 如果查询结果不为成功，则调用撤销
		AlipayTradeCancelRequestBuilder builder = new AlipayTradeCancelRequestBuilder().setOutTradeNo(outTradeNo);
		builder.setAppAuthToken(appAuthToken);
		AlipayTradeCancelResponse cancelResponse = cancelPayResult(builder);
		if (tradeError(cancelResponse)) {
			// 如果第一次同步撤销返回异常，则标记支付交易为未知状态
			result.setTradeStatus(TradeStatus.UNKNOWN);
		} else {
			// 标记支付为失败，如果撤销未能成功，产生的单边帐由人工处理
			result.setTradeStatus(TradeStatus.FAILED);
		}
		return result;
	}

	//查询返回“支付成功”
	private boolean querySuccess(AlipayTradeQueryResponse response) {
		return response != null && Constants.SUCCESS.equals(response.getCode())
				&& ("TRADE_SUCCESS".equals(response.getTradeStatus())
						|| "TRADE_FINISHED".equals(response.getTradeStatus()));
	}

	//将查询应答转换为支付应答
	private AlipayTradePayResponse toPayResponse(AlipayTradeQueryResponse response) {
		AlipayTradePayResponse payResponse = new AlipayTradePayResponse();
		// 只有查询明确返回成功才能将返回码设置为10000，否则均为失败
		payResponse.setCode(querySuccess(response) ? Constants.SUCCESS : Constants.FAILED);
		// 补充交易状态信息
		StringBuilder msg = new StringBuilder(response.getMsg()).append(" tradeStatus:")
				.append(response.getTradeStatus());
		payResponse.setMsg(msg.toString());
		payResponse.setSubCode(response.getSubCode());
		payResponse.setSubMsg(response.getSubMsg());
		payResponse.setBody(response.getBody());
		payResponse.setParams(response.getParams());

		// payResponse应该是交易支付时间，但是response里是本次交易打款给卖家的时间,是否有问题
		// payResponse.setGmtPayment(response.getSendPayDate());
		payResponse.setBuyerLogonId(response.getBuyerLogonId());
		payResponse.setFundBillList(response.getFundBillList());
		payResponse.setOpenId(response.getOpenId());
		payResponse.setOutTradeNo(response.getOutTradeNo());
		payResponse.setReceiptAmount(response.getReceiptAmount());
		payResponse.setTotalAmount(response.getTotalAmount());
		payResponse.setTradeNo(response.getTradeNo());
		return payResponse;
	}

	//交易异常，或发生系统错误
	protected boolean tradeError(AlipayResponse response) {
		return response == null || Constants.ERROR.equals(response.getCode());
	}

	//根据外部订单号outTradeNo撤销订单
	private AlipayTradeCancelResponse cancelPayResult(AlipayTradeCancelRequestBuilder builder) {
		AlipayTradeCancelResponse response = tradeCancel(builder);
		if (cancelSuccess(response)) {
			// 如果撤销成功，则返回撤销结果
			return response;
		}

		// 撤销失败
		if (needRetry(response)) {
			// 如果需要重试，首先记录日志，然后调用异步撤销
			log.warn("begin async cancel request:" + builder);
			asyncCancel(builder);
		}
		return response;
	}

	//撤销返回“撤销成功”
	private boolean cancelSuccess(AlipayTradeCancelResponse response) {
		return response != null && Constants.SUCCESS.equals(response.getCode());
	}

	//撤销需要重试
	private boolean needRetry(AlipayTradeCancelResponse response) {
		return response == null || "Y".equals(response.getRetryFlag());
	}

	//异步撤销
	private void asyncCancel(final AlipayTradeCancelRequestBuilder builder) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < Configs.getMaxCancelRetry(); i++) {
					Utils.sleep(Configs.getCancelDuration());

					AlipayTradeCancelResponse response = tradeCancel(builder);
					if (cancelSuccess(response) || !needRetry(response)) {
						// 如果撤销成功或者应答告知不需要重试撤销，则返回撤销结果（无论撤销是成功失败，失败人工处理）
						return;
					}
				}
			}
		});
	}

	//根据外部订单号outTradeNo撤销订单
	private AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelRequestBuilder builder) {
		validateBuilder(builder);

		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
		request.setBizContent(builder.toJsonString());
		log.info("trade.cancel bizContent:" + request.getBizContent());

		return (AlipayTradeCancelResponse) getResponse(alipayClient, request);
	}

	//调用AlipayClient的execute方法，进行远程调用
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AlipayResponse getResponse(AlipayClient client, AlipayRequest request) {
		try {
			AlipayResponse response = client.execute(request);
			if (response != null) {
				log.info(response.getBody());
			}
			return response;

		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 退款
	 */
	@Override
	public AlipayTradeRefundResponse alipayTradeRefund(String orderNum) {
		long currentTimeMillis = System.currentTimeMillis();
		// 只有订单支付成功的才允许退款
		OrderResult result = xcOrderClient.queryOrdersByOrderNum(orderNum);
		if(result.isSuccess() && result.getXcOrdersPay() != null) {
			String payStatus = result.getXcOrdersPay().getStatus();
			if(payStatus.equals(XcOrderStatus.PAY_YES)) {
				//判断退款是否超时
				Date endTime = result.getXcOrders().getEndTime();//完成支付时间
				long time = (System.currentTimeMillis() - endTime.getTime())/1000/60;//分钟
				if(refundTtl < time) {
					ExceptionCast.cast(OrderCode.PAY_REFUND_TIMEOUT);
				}
				
				// (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
				String refundReason = "正常退款";
				// 创建退款请求builder，设置请求参数
				AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder().setOutTradeNo(orderNum)
						.setRefundAmount(String.valueOf(result.getXcOrders().getPrice())).setRefundReason(refundReason).setOutRequestNo("")
						.setStoreId(storeId);

				AlipayF2FRefundResult resultRefund = tradeService.tradeRefund(builder);
				switch (resultRefund.getTradeStatus()) {
				case SUCCESS:
					log.info("支付宝退款成功: )");
					//退款成功修改订单状态,如果修改订单状态过程发生异常，人工干预处理
					try {
						XcOrders xcOrders = result.getXcOrders();
						List<XcOrdersDetail> xcOrdersDetails = result.getXcOrdersDetails();
						XcOrdersPay xcOrdersPay = result.getXcOrdersPay();
						UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
						xcOrders.setStatus(XcOrderStatus.PAYING);
						xcOrdersPay.setStatus(XcOrderStatus.PAYING);
						updateOrderRequest.setXcOrders(xcOrders);
						updateOrderRequest.setXcOrdersDetails(xcOrdersDetails);
						updateOrderRequest.setXcOrdersPay(xcOrdersPay);
						OrderResult updateResult = xcOrderClient.updateOrder(updateOrderRequest);
						if(!updateResult.isSuccess()) {
							ExceptionCast.cast(OrderCode.PAY_REFUND_UNKNOWN);
						}
					} catch (Exception e) {
						ExceptionCast.cast(OrderCode.PAY_REFUND_UNKNOWN);
					}
					break;

				case FAILED:
					log.error("支付宝退款失败!!!");
					ExceptionCast.cast(OrderCode.PAY_REFUND_FAIL);

				case UNKNOWN:
					log.error("系统异常，订单退款状态未知!!!");
					ExceptionCast.cast(OrderCode.PAY_REFUND_UNKNOWN);

				default:
					log.error("不支持的交易状态，交易返回异常!!!");
					ExceptionCast.cast(OrderCode.PAY_NOSUPPORT);
				}
				return resultRefund.getResponse();
			}
		}
		return null;
	}
	
}
