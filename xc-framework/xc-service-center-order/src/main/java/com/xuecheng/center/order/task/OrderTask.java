package com.xuecheng.center.order.task;

import java.util.Date;
import java.util.List;

import com.xuecheng.center.order.client.XcPayClient;
import com.xuecheng.center.order.config.OrderConfiguration;
import com.xuecheng.center.order.service.XcOrderCenterService;
import com.xuecheng.center.order.utils.SpringBeanUtil;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.ext.OrderExt;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;
import com.xuecheng.framework.domain.order.status.XcOrderStatus;
import com.xuecheng.framework.utils.ZkSessionUtil;

/**
 * 订单创建，开启一条线程监控订单支付状态
 * @author Administrator
 *
 */
public class OrderTask implements Runnable {
	private String orderNumber;
	public OrderTask(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public void run() {
		try {
			//线程休眠到过期时间
			Thread.sleep(OrderConfiguration.order_pay_no_ttl);
			
			//查询订单
			SpringBeanUtil util = SpringBeanUtil.getInstance();
			XcOrderCenterService xcOrderCenterService = util.getBean(XcOrderCenterService.class);
			OrderResult orderResult = xcOrderCenterService.queryOrdersByOrderNum(orderNumber);
			
			//判断订单是否支付成功,支付成功结束线程，未支付取消订单,支付中检查支付宝订单是否支付成功，支付成功结束线程
			XcOrdersPay xcOrdersPay = orderResult.getXcOrdersPay();
			XcOrders xcOrders = orderResult.getXcOrders();
			List<XcOrdersDetail> xcOrdersDetails = orderResult.getXcOrdersDetails();
			if(xcOrdersPay.getStatus().equals(XcOrderStatus.PAY_YES)) {
				return;
			}
			
			//支付超时，撤销订单
			if(xcOrdersPay.getStatus().equals(XcOrderStatus.PAY_NO)) {
				xcOrders.setStatus(XcOrderStatus.PAY_CANCEL);
				xcOrdersPay.setStatus(XcOrderStatus.PAY_CANCEL);
				xcOrderCenterService.updateOrder(xcOrders, xcOrdersDetails, xcOrdersPay);
			}
			
			//支付中，查询支付宝订单状态，支付成功修改订单状态成功，支付失败，取消订单
			XcPayClient xcPayClient = util.getBean(XcPayClient.class);
			PayOrderResult result = null;
			for(int i = 0;i < 10;i++) {
				try {
					result = xcPayClient.alipayTradeQuery(orderNumber);
					break;
				} catch (Exception e) {
					continue;
				}
			}
			if(result.isSuccess()) {
				xcOrders.setStatus(XcOrderStatus.PAY_YES);
				xcOrdersPay.setStatus(XcOrderStatus.PAY_YES);
				xcOrderCenterService.updateOrder(xcOrders, xcOrdersDetails, xcOrdersPay);
			} else {
				xcOrders.setStatus(XcOrderStatus.PAY_CANCEL);
				xcOrdersPay.setStatus(XcOrderStatus.PAY_CANCEL);
				xcOrderCenterService.updateOrder(xcOrders, xcOrdersDetails, xcOrdersPay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
