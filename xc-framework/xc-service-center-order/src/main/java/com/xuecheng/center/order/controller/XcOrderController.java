package com.xuecheng.center.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.center.order.client.XcJwtClient;
import com.xuecheng.center.order.service.XcOrderCenterService;
import com.xuecheng.framework.api.XcOrderControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.web.BaseController;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.request.CreateOrderRequest;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.request.UpdateOrderRequest;
import com.xuecheng.framework.domain.order.response.CreateOrderResult;
import com.xuecheng.framework.domain.order.response.OrderCode;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;

@RestController
@RequestMapping("/order")
public class XcOrderController extends BaseController implements XcOrderControllerApi {

	@Autowired
	private XcOrderCenterService xcOrderCenterService;
	@Autowired
	private XcJwtClient xcJwtClient;

	// 从请求头中获取jwt令牌，并且解析令牌获得userId
	private UserToken userToken(HttpServletRequest request) {
		UserToken userToken = xcJwtClient.getUserJwtFromHeader(request);
		if (userToken == null) {
			ExceptionCast.cast(OrderCode.JWT_USERTOKEN_ERROR);
		}
		return userToken;
	}

	/**
	 * 创建订单
	 */
	@Override
	@PostMapping("/create")
	public CreateOrderResult createOrder(CreateOrderRequest createOrderRequest) {
		String userId = "";
		try {
			UserToken userToken = userToken(request);
			userId = userToken.getUserId();
		} catch (Exception e) {
			ExceptionCast.cast(OrderCode.JWT_USERTOKEN_ERROR);
		}
		return xcOrderCenterService.createOrder(createOrderRequest, userId);
	}

	/**
	 * 根据订单号查询订单
	 */
	@Override
	@GetMapping("/query/{orderNum}")
	public OrderResult queryOrdersByOrderNum(@PathVariable("orderNum") String orderNum) {

		return xcOrderCenterService.queryOrdersByOrderNum(orderNum);
	}

	/**
	 * 更新订单状态
	 */
	@Override
	@PostMapping("/update")
	public OrderResult updateOrder(UpdateOrderRequest updateOrderRequest) {
		XcOrders xcOrders = updateOrderRequest.getXcOrders();
		List<XcOrdersDetail> xcOrdersDetails = updateOrderRequest.getXcOrdersDetails();
		XcOrdersPay xcOrdersPay = updateOrderRequest.getXcOrdersPay();
		return xcOrderCenterService.updateOrder(xcOrders, xcOrdersDetails, xcOrdersPay);
	}

	/**
	 * 我的订单列表
	 */
	@Override
	@GetMapping("/query/order/list")
	public QueryOrderListResult queryOrderList(QueryOrderListRequest queryOrdersListRequest) {
		String userId = "";
		try {
			UserToken userToken = userToken(request);
			userId = userToken.getUserId();
		} catch (Exception e) {
			ExceptionCast.cast(OrderCode.JWT_USERTOKEN_ERROR);
		}
		return xcOrderCenterService.queryOrderList(queryOrdersListRequest, userId);
	}

	/**
	 * 取消订单
	 */
	@Override
	@GetMapping("/cancel/{orderNumber}")
	public ResponseResult cancelOrder(@PathVariable("orderNumber") String orderNumber) {

		return xcOrderCenterService.cancelOrder(orderNumber);
	}

}
