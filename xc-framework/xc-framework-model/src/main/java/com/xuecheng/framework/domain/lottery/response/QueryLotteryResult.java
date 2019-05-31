package com.xuecheng.framework.domain.lottery.response;

import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;

public class QueryLotteryResult {
	private XcLottery xcLottery;
	private XcLotteryDetails xcLotteryDetails;
	private boolean isSuccess;
	
	public QueryLotteryResult(boolean isSuccess,XcLottery xcLottery,XcLotteryDetails xcLotteryDetails) {
		this.isSuccess = isSuccess;
		this.xcLottery = xcLottery;
		this.xcLotteryDetails = xcLotteryDetails;
	}
	
	public QueryLotteryResult() {
		
	}

	public XcLottery getXcLottery() {
		return xcLottery;
	}

	public void setXcLottery(XcLottery xcLottery) {
		this.xcLottery = xcLottery;
	}

	public XcLotteryDetails getXcLotteryDetails() {
		return xcLotteryDetails;
	}

	public void setXcLotteryDetails(XcLotteryDetails xcLotteryDetails) {
		this.xcLotteryDetails = xcLotteryDetails;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	@Override
	public String toString() {
		return "QueryLotteryResult [xcLottery=" + xcLottery + ", xcLotteryDetails=" + xcLotteryDetails + ", isSuccess="
				+ isSuccess + "]";
	}
	
	
}
