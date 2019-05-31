package com.xuecheng.framework.domain.lottery.request;

public class QueryLotteryDetailsRequest {
	private int page;//当前页
	private int rows;//当前页大小
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
}
