package com.xuecheng.framework.domain.lottery.response;

import java.util.List;

public class QueryLotteryDetailsResult {
	private int total;
	private List list;
	private boolean isSuccess;
	private String message;
	
	public QueryLotteryDetailsResult(boolean isSuccess,int total,List list,String message) {
		this.total = total;
		this.list = list;
		this.isSuccess = isSuccess;
		this.message = message;
	}
	
	public QueryLotteryDetailsResult() {
		super();
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
		return "QueryLotteryDetailsResult [total=" + total + ", list=" + list + "]";
	}
	
}
