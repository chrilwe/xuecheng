package com.xuecheng.framework.domain.lottery;

import java.io.Serializable;

public class XcLotteryShotItems implements Serializable {
	private String lotteryId;
	private String lotteryDetailsId;
	private String userId;
	private String status;
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getLotteryDetailsId() {
		return lotteryDetailsId;
	}
	public void setLotteryDetailsId(String lotteryDetailsId) {
		this.lotteryDetailsId = lotteryDetailsId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "XcLotteryShotItems [lotteryId=" + lotteryId + ", lotteryDetailsId=" + lotteryDetailsId + ", userId="
				+ userId + ", status=" + status + "]";
	}
	
}
