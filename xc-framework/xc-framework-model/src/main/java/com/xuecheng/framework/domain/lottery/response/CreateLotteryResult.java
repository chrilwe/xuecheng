package com.xuecheng.framework.domain.lottery.response;

public class CreateLotteryResult {
	private String lotteryId;
	private boolean isSuccess;
	private String message;
	
	public CreateLotteryResult(String lotteryId,boolean isSuccess,String message) {
		this.isSuccess = isSuccess;
		this.lotteryId = lotteryId;
		this.message = message;
	}
	
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
	public CreateLotteryResult() {
		super();
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
