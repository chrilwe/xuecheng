package com.xuecheng.center.order.task;

import java.util.List;

import com.xuecheng.center.order.client.XcPayClient;
import com.xuecheng.center.order.service.XcOrderMonitorService;
import com.xuecheng.center.order.utils.SpringBeanUtil;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.ext.OrderExt;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.response.PayOrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;
import com.xuecheng.framework.domain.order.status.XcOrderStatus;
import com.xuecheng.framework.utils.ZkSessionUtil;

/**
 * 监控支付宝回调失败，订单最终没有更新到支付成功
 * @author Administrator
 *
 */
public class PayTask implements Runnable {

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000*60);
				//查询正在支付订单超过订单超时时间的订单列表
				//从spring容器中获取XcOrderMonitorService
				XcOrderMonitorService xcOrderMonitorService = SpringBeanUtil.getInstance().getBean(XcOrderMonitorService.class);
				
				QueryOrderListRequest queryOrdersListRequest = new QueryOrderListRequest();
				queryOrdersListRequest.setCurrentPage(1);
				queryOrdersListRequest.setPageSize(100);
				queryOrdersListRequest.setStatus(XcOrderStatus.PAYING);
				QueryOrderListResult result = xcOrderMonitorService.queryOrderList(queryOrdersListRequest);
				if(result.getTotal() == 0) {
					continue;
				}
				
				List<OrderExt> list = result.getList();
				for(OrderExt orderExt : list) {
					//查询支付宝订单支付状态
					XcPayClient xcPayClient = SpringBeanUtil.getInstance().getBean(XcPayClient.class);
					PayOrderResult alipayTradeQuery = xcPayClient.alipayTradeQuery(orderExt.getXcOrders().getOrderNumber());
					if(alipayTradeQuery.isSuccess()) {
						//更新订单状态
						ZkSessionUtil zkUtil = ZkSessionUtil.getInstance();
						boolean createNode = zkUtil.createNode("/"+orderExt.getXcOrders().getOrderNumber(), "");
						XcOrders xcOrders = orderExt.getXcOrders();
						XcOrdersPay xcOrdersPay = orderExt.getXcOrdersPay();
						xcOrders.setStatus(XcOrderStatus.PAY_YES);
						xcOrdersPay.setStatus(XcOrderStatus.PAY_YES);
						xcOrderMonitorService.updateXcOrdersAndXcOrdersPay(xcOrders, xcOrdersPay);
						zkUtil.deleteNode("/"+orderExt.getXcOrders());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
