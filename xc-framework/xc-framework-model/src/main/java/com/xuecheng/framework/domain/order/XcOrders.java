package com.xuecheng.framework.domain.order;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 2018/2/10.
 */
public class XcOrders implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;
    
    private String orderNumber;
    private int initialPrice;
    private int price;
    private Date startTime;
    private Date endTime;
    private String status;
    private String userId;
    private String details;
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getInitialPrice() {
		return initialPrice;
	}
	public void setInitialPrice(int initialPrice) {
		this.initialPrice = initialPrice;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	@Override
	public String toString() {
		return "XcOrders [orderNumber=" + orderNumber + ", initialPrice=" + initialPrice + ", price=" + price
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", userId=" + userId
				+ ", details=" + details + "]";
	}

}
