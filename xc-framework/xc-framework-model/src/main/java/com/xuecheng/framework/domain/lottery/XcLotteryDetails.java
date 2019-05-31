package com.xuecheng.framework.domain.lottery;

import java.io.Serializable;
import java.util.Date;

public class XcLotteryDetails implements Serializable  {
	private String id;
	private String name;//奖品名称
	private String pic;//奖品图片
	private String money;//奖品金额
	private String status;//奖品的状态
	private Date createTime;//创建时间
	private String type;//奖品类型：红包或者课程
	private String shotPrecent;//中奖概率
	private String lotteryId;//抽奖类型
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getShotPrecent() {
		return shotPrecent;
	}
	public void setShotPrecent(String shotPrecent) {
		this.shotPrecent = shotPrecent;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	@Override
	public String toString() {
		return "XcLotteryDetails [id=" + id + ", name=" + name + ", pic=" + pic + ", money=" + money + ", status="
				+ status + ", createTime=" + createTime + ", type=" + type + ", shotPrecent=" + shotPrecent
				+ ", lotteryId=" + lotteryId + "]";
	}
	
}
