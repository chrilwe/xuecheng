package com.xuecheng.center.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.xuecheng.center.order.client.XcCourseClient;
import com.xuecheng.center.order.client.XcJwtClient;
import com.xuecheng.center.order.mapper.XcOrdersDetailMapper;
import com.xuecheng.center.order.mapper.XcOrdersMapper;
import com.xuecheng.center.order.mapper.XcOrdersPayMapper;
import com.xuecheng.center.order.service.XcOrderCenterService;
import com.xuecheng.center.order.task.LogTask;
import com.xuecheng.center.order.task.OrderTask;
import com.xuecheng.center.order.task.TaskPool;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;
import com.xuecheng.framework.domain.order.ext.OrderExt;
import com.xuecheng.framework.domain.order.request.CreateOrderRequest;
import com.xuecheng.framework.domain.order.request.QueryOrderListRequest;
import com.xuecheng.framework.domain.order.response.CreateOrderResult;
import com.xuecheng.framework.domain.order.response.OrderCode;
import com.xuecheng.framework.domain.order.response.OrderResult;
import com.xuecheng.framework.domain.order.response.QueryOrderListResult;
import com.xuecheng.framework.domain.order.status.XcOrderStatus;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.utils.GenerateOrderNum;
import com.xuecheng.framework.utils.XcOauth2Util.UserJwt;

@Service
public class XcOrderCenterServiceImpl implements XcOrderCenterService {
	@Autowired
	private XcOrdersMapper xcOrdersMapper;
	@Autowired
	private XcOrdersPayMapper xcOrdersPayMapper;
	@Autowired
	private XcOrdersDetailMapper xcOrdersDetailMapper;
	@Autowired
	private XcCourseClient xcCourseClient;
	
	@Value("${xuecheng.order.log_path}")
	private String log_path;
	
	/**
	 * 创建订单
	 */
	@Override
	@Transactional
	public CreateOrderResult createOrder(CreateOrderRequest createOrderRequest,String userId) {
		if(createOrderRequest == null || StringUtils.isEmpty(createOrderRequest.getCourseId())) {
			ExceptionCast.cast(OrderCode.ORDER_ADD_ITEMISNULL);
		}
		if(StringUtils.isEmpty(userId)) {
			ExceptionCast.cast(OrderCode.USER_NO_LOGIN);
		}
		//查询订单,判断订单是否存在
		//String userId = "1";
		List<XcOrders> list = xcOrdersMapper.findNoPayOrderByUserId(userId, XcOrderStatus.PAY_NO);
		if(list.size() > 0) {
			//请先完成未支付订单
			ExceptionCast.cast(OrderCode.ORDER_PLEASE_PAY_NOPAYORDER);
		}
		
		//订单号生成
		GenerateOrderNum generateOrderNum = new GenerateOrderNum();
		String orderNumber = generateOrderNum.generate();
		if(StringUtils.isEmpty(orderNumber)) {
			//订单号生成失败
			ExceptionCast.cast(OrderCode.ORDER_ADD_ORDERNUMERROR);
		}
		
		Date startTime = new Date();
		
		//创建订单基本信息
		CoursePub coursePub = xcCourseClient.findCoursePubByCourseId(createOrderRequest.getCourseId());
		if(coursePub == null) {
			//没有找到课程信息
			ExceptionCast.cast(OrderCode.ORDER_ADD_GETCOURSEERROR);
		}
	
		XcOrders xcOrders = new XcOrders();
		xcOrders.setUserId(userId);
		xcOrders.setStartTime(startTime);
		xcOrders.setInitialPrice(coursePub.getPrice().intValue());
		xcOrders.setOrderNumber(orderNumber);
		xcOrders.setPrice(coursePub.getPrice().intValue());
		xcOrders.setStatus(XcOrderStatus.PAY_NO);
		xcOrdersMapper.add(xcOrders);
		
		//创建订单商品详情信息
		XcOrdersDetail xcOrdersDetail = new XcOrdersDetail();
		xcOrdersDetail.setId(UUID.randomUUID().toString());
		xcOrdersDetail.setItemId(createOrderRequest.getCourseId());
		xcOrdersDetail.setItemNum(1);
		xcOrdersDetail.setItemPrice(coursePub.getPrice().intValue());
		xcOrdersDetail.setOrderNumber(orderNumber);
		xcOrdersDetail.setValid(coursePub.getValid());
		xcOrdersDetail.setStartTime(startTime);
		xcOrdersDetail.setItemTitle(coursePub.getName());
		xcOrdersDetailMapper.add(xcOrdersDetail);
		
		//创建订单支付信息
		XcOrdersPay xcOrdersPay = new XcOrdersPay();
		xcOrdersPay.setId(UUID.randomUUID().toString());
		xcOrdersPay.setOrderNumber(orderNumber);
		xcOrdersPay.setStatus(XcOrderStatus.PAY_NO);
		xcOrdersPayMapper.add(xcOrdersPay);
		
		//订单创建完成，创建监控订单是否超时任务
		TaskPool taskPool = TaskPool.getInstance();
		taskPool.submit(new OrderTask(orderNumber));
		//生成购买日志
		taskPool.submit(new LogTask(log_path,JSON.toJSONString(xcOrdersDetail)));
		return new CreateOrderResult(CommonCode.SUCCESS,xcOrders);
	}
	
	/**
	 * 根据订单号查询订单
	 */
	@Override
	public OrderResult queryOrdersByOrderNum(String orderNum) {
		if(StringUtils.isEmpty(orderNum)) {
			ExceptionCast.cast(OrderCode.ORDER_NUMBER_ISNULL);
		}
		XcOrders xcOrders = xcOrdersMapper.findByOrderNum(orderNum);
		XcOrdersPay xcOrdersPay = xcOrdersPayMapper.findByOrderNum(orderNum);
		List<XcOrdersDetail> list = xcOrdersDetailMapper.findByOrderNum(orderNum);
		if(xcOrders == null || xcOrdersPay == null || list == null) {
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		return new OrderResult(CommonCode.SUCCESS,xcOrders,xcOrdersPay,list);
	}
	
	/**
	 * 更新订单
	 */
	@Override
	@Transactional
	public OrderResult updateOrder(XcOrders xcOrders, List<XcOrdersDetail> xcOrdersDetails, XcOrdersPay xcOrdersPay) {
		if(xcOrders == null || xcOrdersDetails == null || xcOrdersPay == null) {
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		//更新订单基本信息
		String orderNum = xcOrders.getOrderNumber();
		if(StringUtils.isEmpty(orderNum)) {
			ExceptionCast.cast(OrderCode.ORDER_NUMBER_ISNULL);
		}
		XcOrders findXcOrders = xcOrdersMapper.findByOrderNum(orderNum);
		if(findXcOrders == null) {
			//未找到订单信息
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		xcOrdersMapper.update(xcOrders);
		
		//更新订单商品详情信息
		List<XcOrdersDetail> list = xcOrdersDetailMapper.findByOrderNum(xcOrders.getOrderNumber());
		if(list == null || list.size() <= 0) {
			//未找到订单信息
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		for(XcOrdersDetail xcOrdersDetail : xcOrdersDetails) {
			xcOrdersDetailMapper.update(xcOrdersDetail);
		}
		
		//更新订单支付信息
		XcOrdersPay findXcOrdersPay = xcOrdersPayMapper.findByOrderNum(orderNum);
		if(findXcOrdersPay == null) {
			//未找到订单信息
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		xcOrdersPayMapper.update(xcOrdersPay);
		return new OrderResult(CommonCode.SUCCESS,xcOrders);
	}
	
	/**
	 * 查询订单列表
	 */
	@Override
	public QueryOrderListResult queryOrderList(QueryOrderListRequest queryOrdersListRequest,String userId) {
		if(queryOrdersListRequest == null || StringUtils.isEmpty(queryOrdersListRequest.getOrderNumber())) {
			ExceptionCast.cast(OrderCode.ORDER_QUERY_PARAMS_ERROR);
		}
		if(StringUtils.isEmpty(userId)) {
			ExceptionCast.cast(OrderCode.USER_NO_LOGIN);
		}
		
		int currentPage = queryOrdersListRequest.getCurrentPage();
		String orderNumber = queryOrdersListRequest.getOrderNumber();
		int pageSize = queryOrdersListRequest.getPageSize();
		String status = queryOrdersListRequest.getStatus();
		
		if(pageSize <= 0) {
			pageSize = 10;
		}
		if(currentPage <= 0) {
			currentPage = 1;
		}
		if(StringUtils.isEmpty(status)) {
			status = XcOrderStatus.PAY_YES;
		}
		int start = (currentPage - 1) * pageSize;
			
		//查询订单信息
		List<OrderExt> orderExts = null;
		List<XcOrders> xcOrdersList = xcOrdersMapper.queryOrdersList(start, pageSize, status, userId);
		int total = xcOrdersMapper.countByStatus(status, userId);
		if(xcOrdersList == null || xcOrdersList.size() <= 0) {
			return new QueryOrderListResult(CommonCode.SUCCESS,total,null);
		}
		for(XcOrders xcOrders : xcOrdersList) {
			List<XcOrdersDetail> list = xcOrdersDetailMapper.findByOrderNum(orderNumber);
			XcOrdersPay xcOrdersPay = xcOrdersPayMapper.findByOrderNum(orderNumber);
			OrderExt extOrder = new OrderExt();
			extOrder.setXcOrders(xcOrders);
			extOrder.setXcOrdersDetails(list);
			extOrder.setXcOrdersPay(xcOrdersPay);
			orderExts.add(extOrder);
		}
		return new QueryOrderListResult(CommonCode.SUCCESS,total,orderExts);
	}
	
	/**
	 * 取消订单
	 */
	@Override
	@Transactional
	public ResponseResult cancelOrder(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			ExceptionCast.cast(OrderCode.ORDER_NUMBER_ISNULL);
		}
		
		//未支付订单可以取消，其他状态不能取消
		XcOrders xcOrders = xcOrdersMapper.findByOrderNum(orderNumber);
		XcOrdersPay xcOrdersPay = xcOrdersPayMapper.findByOrderNum(orderNumber);
		if(xcOrders == null || xcOrdersPay == null) {
			ExceptionCast.cast(OrderCode.ORDER_FINISH_NOTFOUNDORDER);
		}
		if(!xcOrders.getStatus().equals(XcOrderStatus.PAY_NO) && !xcOrdersPay.getStatus().equals(XcOrderStatus.PAY_NO)) {
			ExceptionCast.cast(OrderCode.ORDER_CANCELED_OR_PAYFINISHED);
		}
		
		//更新订单状态为取消订单状态
		xcOrders.setStatus(XcOrderStatus.PAY_CANCEL);
		xcOrdersPay.setStatus(XcOrderStatus.PAY_CANCEL);
		int update = xcOrdersMapper.update(xcOrders);
		int update2 = xcOrdersPayMapper.update(xcOrdersPay);
		return new ResponseResult(CommonCode.SUCCESS);
	}
	
	
}
