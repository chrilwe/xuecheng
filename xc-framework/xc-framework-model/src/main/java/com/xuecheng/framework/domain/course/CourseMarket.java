package com.xuecheng.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 2018/2/10.
 */

public class CourseMarket implements Serializable {
    private static final long serialVersionUID = -916357110051689486L;
   
    private String id;//课程id
    private String charge;
    private String valid;
    private String qq;
    private Float price;
    private Float price_old;
//    private Date expires;
    private Date startTime;
    private Date endTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getPrice_old() {
		return price_old;
	}
	public void setPrice_old(Float price_old) {
		this.price_old = price_old;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "CourseMarket [id=" + id + ", charge=" + charge + ", valid=" + valid + ", qq=" + qq + ", price=" + price
				+ ", price_old=" + price_old + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
