package com.xuecheng.framework.domain.order.response;

import java.util.List;

import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import com.xuecheng.framework.domain.order.XcOrders;
import com.xuecheng.framework.domain.order.XcOrdersDetail;
import com.xuecheng.framework.domain.order.XcOrdersPay;

import lombok.Data;
import lombok.ToString;

/**
 * Created by mrt on 2018/3/26.
 */
public class OrderResult extends ResponseResult {
    private XcOrders xcOrders;
    private XcOrdersPay xcOrdersPay;
    private List<XcOrdersDetail> xcOrdersDetails;
    public OrderResult(ResultCode resultCode) {
		super(resultCode);
	}
	public OrderResult(ResultCode resultCode, XcOrders xcOrders) {
        super(resultCode);
        this.xcOrders = xcOrders;
    }
    public OrderResult(ResultCode resultCode, XcOrders xcOrders, XcOrdersPay xcOrdersPay, List<XcOrdersDetail> xcOrdersDetails) {
        super(resultCode);
        this.xcOrders = xcOrders;
        this.xcOrdersDetails = xcOrdersDetails;
        this.xcOrdersPay = xcOrdersPay;
    }
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
		return "OrderResult [xcOrders=" + xcOrders + ", xcOrdersPay=" + xcOrdersPay + ", xcOrdersDetails="
				+ xcOrdersDetails + "]";
	}
	
}
