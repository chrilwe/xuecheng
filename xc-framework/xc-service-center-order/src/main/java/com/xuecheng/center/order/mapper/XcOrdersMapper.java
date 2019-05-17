package com.xuecheng.center.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.order.XcOrders;

public interface XcOrdersMapper {
	public int add(XcOrders xcOrders);
	public int deleteByOrderNum(String orderNum);
	public int update(XcOrders xcOrders);
	public XcOrders findByOrderNum(String orderNum);
	public List<XcOrders> findNoPayOrderByUserId(@Param("userId")String userId, @Param("status")String status);
	public List<XcOrders> queryOrdersList(@Param("start")int start,@Param("size")int size,@Param("status")String status,@Param("userId")String userId);
	public int countByStatus(@Param("status")String status,@Param("userId")String userId);
	public List<XcOrders> findNoPayOrders(@Param("start")int start,@Param("size")int size,@Param("status")String status);
	public int countByStatus1(@Param("status")String status);
}
