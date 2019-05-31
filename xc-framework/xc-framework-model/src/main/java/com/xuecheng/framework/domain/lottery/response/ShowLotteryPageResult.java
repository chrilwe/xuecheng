package com.xuecheng.framework.domain.lottery.response;

public class ShowLotteryPageResult {
	private boolean isSuccess;
	private String message;
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ShowLotteryPageResult(boolean isSuccess, String message) {
		super();
		this.isSuccess = isSuccess;
		this.message = message;
	}
	public ShowLotteryPageResult() {
		super();
	}
	
}
