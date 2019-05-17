package com.xuecheng.center.order.task;

import java.util.Date;
import java.util.List;

import com.xuecheng.center.order.config.OrderConfiguration;
import com.xuecheng.center.order.service.XcOrderCenterService;
import com.xuecheng.center.order.service.XcOrderMonitorService;
import com.xuecheng.center.order.utils.SpringBeanUtil;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.ext.OrderExt;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;
import com.xuecheng.framework.domain.order.status.XcOrderStatus;
import com.xuecheng.framework.utils.ZkSessionUtil;

/**
 * 监控超时未完成支付订单
 * @author Administrator
 *
 */
public class OrderTask implements Runnable {

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(5000);
				long currentTime = System.currentTimeMillis();
				//从spring容器中获取XcOrderMonitorService
				XcOrderMonitorService xcOrderMonitorService = SpringBeanUtil.getInstance().getBean(XcOrderMonitorService.class);
				
				//查询未支付订单，判断是否超时，超时订单自动取消
				QueryOrderListRequest queryOrdersListRequest = new QueryOrderListRequest();
				queryOrdersListRequest.setCurrentPage(1);
				queryOrdersListRequest.setPageSize(100);
				queryOrdersListRequest.setStatus(XcOrderStatus.PAY_NO);
				QueryOrderListResult queryOrderListResult = xcOrderMonitorService.queryOrderList(queryOrdersListRequest);
				if(queryOrderListResult.getTotal() == 0 && queryOrderListResult.getList() == null) {
					continue;
				}
				List<OrderExt> list = queryOrderListResult.getList();
				for(OrderExt orderExt : list) {
					Date startTime = orderExt.getXcOrders().getStartTime();
					if((currentTime - startTime.getTime())/1000/60 > OrderConfiguration.order_pay_no_ttl) {
						//其他线程更新相同的订单，只允许一条线程更新成功
						ZkSessionUtil zkUtil = ZkSessionUtil.getInstance();
						boolean createNode = zkUtil.createNode("/"+orderExt.getXcOrders().getOrderNumber(), "");
						if(!createNode) {
							continue;
						}
						xcOrderMonitorService.updateXcOrdersAndXcOrdersPay(orderExt.getXcOrders(), orderExt.getXcOrdersPay());
						zkUtil.deleteNode("/"+orderExt.getXcOrders());
					}
				}
				
				int countResult = queryOrderListResult.getTotal()%queryOrdersListRequest.getPageSize();
				int count = queryOrderListResult.getTotal()/queryOrdersListRequest.getPageSize();
				int totalPage = countResult == 0?count:(count+1);
				for(int i = 1;i < totalPage;i++) {
					queryOrdersListRequest.setCurrentPage(i);
					queryOrdersListRequest.setPageSize(100);
					queryOrdersListRequest.setStatus(XcOrderStatus.PAY_NO);
					QueryOrderListResult orderListResult = xcOrderMonitorService.queryOrderList(queryOrdersListRequest);
					if(orderListResult.getTotal() == 0 && orderListResult.getList() == null) {
						continue;
					}
		
					List<OrderExt> list1 = queryOrderListResult.getList();
					for(OrderExt orderExt : list1) {
						//其他线程更新相同的订单，只允许一条线程更新成功
						ZkSessionUtil zkUtil = ZkSessionUtil.getInstance();
						boolean createNode = zkUtil.createNode("/"+orderExt.getXcOrders().getOrderNumber(), "");
						if(!createNode) {
							continue;
						}
						xcOrderMonitorService.updateXcOrdersAndXcOrdersPay(orderExt.getXcOrders(), orderExt.getXcOrdersPay());
						zkUtil.deleteNode("/"+orderExt.getXcOrders());
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
