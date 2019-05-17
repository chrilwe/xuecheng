package com.xuecheng.framework.domain.order.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import com.xuecheng.framework.domain.order.XcOrders;
import lombok.Data;
import lombok.ToString;

/**
 * Created by mrt on 2018/3/26.
 */
public class CreateOrderResult extends ResponseResult {
    private XcOrders xcOrders;
    public CreateOrderResult(ResultCode resultCode, XcOrders xcOrders) {
        super(resultCode);
        this.xcOrders = xcOrders;
    }
	public CreateOrderResult() {
		super();
	}
	public XcOrders getXcOrders() {
		return xcOrders;
	}
	public void setXcOrders(XcOrders xcOrders) {
		this.xcOrders = xcOrders;
	}
	@Override
	public String toString() {
		return "CreateOrderResult [xcOrders=" + xcOrders + "]";
	}


}
