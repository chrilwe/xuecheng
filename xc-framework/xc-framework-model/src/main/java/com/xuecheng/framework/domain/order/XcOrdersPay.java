package com.xuecheng.framework.domain.order;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by admin on 2018/2/10.
 */
public class XcOrdersPay implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;
    
    private String id;
    private String orderNumber;
    private String payNumber;
    private String status;
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
	public String getPayNumber() {
		return payNumber;
	}
	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "XcOrdersPay [id=" + id + ", orderNumber=" + orderNumber + ", payNumber=" + payNumber + ", status="
				+ status + "]";
	}

}
