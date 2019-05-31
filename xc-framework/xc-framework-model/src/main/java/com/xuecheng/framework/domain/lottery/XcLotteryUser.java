package com.xuecheng.framework.domain.lottery;

import java.io.Serializable;
import java.util.Date;

public class XcLotteryUser implements Serializable {
	private String userId;
	private Date lotteryTime;//抽奖时间
	private String lotteryDetailsId;//奖品id
	private String lotteryId;//抽奖类型id
	private int times;//抽奖剩余次数
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getLotteryTime() {
		return lotteryTime;
	}
	public void setLotteryTime(Date lotteryTime) {
		this.lotteryTime = lotteryTime;
	}
	public String getLotteryDetailsId() {
		return lotteryDetailsId;
	}
	public void setLotteryDetailsId(String lotteryDetailsId) {
		this.lotteryDetailsId = lotteryDetailsId;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	@Override
	public String toString() {
		return "XcLotteryUser [userId=" + userId + ", lotteryTime=" + lotteryTime + ", lotteryDetailsId="
				+ lotteryDetailsId + ", lotteryId=" + lotteryId + ", times=" + times + "]";
	}
	
}
