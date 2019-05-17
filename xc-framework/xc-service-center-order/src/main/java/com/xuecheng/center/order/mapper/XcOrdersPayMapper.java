package com.xuecheng.center.order.mapper;

import com.xuecheng.framework.domain.order.XcOrdersPay;

public interface XcOrdersPayMapper {
	public int add(XcOrdersPay xcOrdersPay);
	public int deleteByOrderNum(String orderNum);
	public int update(XcOrdersPay xcOrdersPay);
	public XcOrdersPay findByOrderNum(String orderNum);
}
