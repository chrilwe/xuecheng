package com.xuecheng.framework.domain.lottery;

import java.io.Serializable;
import java.util.Date;

public class XcLottery implements Serializable {
	private String id;
	private String type;//抽奖类型
	private Date startTime;//抽奖开始时间
	private Date endTime;//抽奖结束时间
	private String status;//抽奖状态
	private int times;//抽奖次数
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	@Override
	public String toString() {
		return "XcLottery [id=" + id + ", type=" + type + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", status=" + status + ", times=" + times + "]";
	}
	
}
