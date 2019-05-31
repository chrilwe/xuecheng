package com.xuecheng.framework.domain.lottery.response;

public class ChooseResult {
	private String lotteryId;
	private boolean isSuccess;
	private String lotteryDetailsId;
	private String message;
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getLotteryDetailsId() {
		return lotteryDetailsId;
	}
	public void setLotteryDetailsId(String lotteryDetailsId) {
		this.lotteryDetailsId = lotteryDetailsId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ChooseResult(String lotteryId, boolean isSuccess, String lotteryDetailsId, String message) {
		super();
		this.lotteryId = lotteryId;
		this.isSuccess = isSuccess;
		this.lotteryDetailsId = lotteryDetailsId;
		this.message = message;
	}
	public ChooseResult() {
		super();
	}
	
}
