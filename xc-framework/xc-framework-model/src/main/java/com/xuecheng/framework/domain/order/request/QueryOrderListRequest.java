package com.xuecheng.framework.domain.order.request;

import com.xuecheng.framework.common.model.request.RequestData;

public class QueryOrderListRequest extends RequestData {
	private String orderNumber;//订单号
	private int currentPage;//当前页码
	private int pageSize;//页面记录条数
	private String status;//查询订单状态
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "QueryOrderListRequest [orderNumber=" + orderNumber + ", currentPage=" + currentPage + ", pageSize="
				+ pageSize + ", status=" + status + "]";
	}
	
}
