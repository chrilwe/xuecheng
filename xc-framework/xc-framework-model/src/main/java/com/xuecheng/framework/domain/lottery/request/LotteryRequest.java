package com.xuecheng.framework.domain.lottery.request;

public class LotteryRequest {
	private String type;//抽奖类型
	private String ids;//奖品id字符串
	private String lotteryId;//抽奖项目id
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public LotteryRequest() {
		super();
	}
	
}
