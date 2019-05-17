package com.xuecheng.framework.domain.order.response;

import java.util.List;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;

public class QueryOrderListResult extends ResponseResult {
	private int total;
	private List list;
	public QueryOrderListResult(ResultCode resultCode, int total, List list) {
		super(resultCode);
		this.total = total;
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "QueryOrderListResult [total=" + total + ", list=" + list + "]";
	}
	
}
