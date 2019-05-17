package com.xuecheng.framework.domain.order.ext;

import java.util.List;

import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;

public class OrderExt {
	private XcOrders xcOrders;
    private XcOrdersPay xcOrdersPay;
    private List<XcOrdersDetail> xcOrdersDetails;
	public XcOrders getXcOrders() {
		return xcOrders;
	}
	public void setXcOrders(XcOrders xcOrders) {
		this.xcOrders = xcOrders;
	}
	public XcOrdersPay getXcOrdersPay() {
		return xcOrdersPay;
	}
	public void setXcOrdersPay(XcOrdersPay xcOrdersPay) {
		this.xcOrdersPay = xcOrdersPay;
	}
	public List<XcOrdersDetail> getXcOrdersDetails() {
		return xcOrdersDetails;
	}
	public void setXcOrdersDetails(List<XcOrdersDetail> xcOrdersDetails) {
		this.xcOrdersDetails = xcOrdersDetails;
	}
	@Override
	public String toString() {
		return "OrderExt [xcOrders=" + xcOrders + ", xcOrdersPay=" + xcOrdersPay + ", xcOrdersDetails="
				+ xcOrdersDetails + "]";
	}
    
}
