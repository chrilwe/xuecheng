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
public class XcOrdersDetail implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;
    
    private String id;
    private String orderNumber;
    private String itemId;
    private Integer itemNum;
    private int itemPrice;
    private String valid;
    private Date startTime;
    private Date endTime;
	private String itemTitle;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Integer getItemNum() {
		return itemNum;
	}
	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	public int getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
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
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	@Override
	public String toString() {
		return "XcOrdersDetail [id=" + id + ", orderNumber=" + orderNumber + ", itemId=" + itemId + ", itemNum="
				+ itemNum + ", itemPrice=" + itemPrice + ", valid=" + valid + ", startTime=" + startTime + ", endTime="
				+ endTime + ", itemTitle=" + itemTitle + "]";
	}
	
}
