package com.xuecheng.center.order.mapper;

import java.util.List;

import com.xuecheng.framework.domain.order.XcOrdersDetail;

public interface XcOrdersDetailMapper {
	public int add(XcOrdersDetail xcOrdersDetail);
	public int deleteByOrderNum(String orderNum);
	public int update(XcOrdersDetail xcOrdersDetail);
	public List<XcOrdersDetail> findByOrderNum(String orderNum);
}
