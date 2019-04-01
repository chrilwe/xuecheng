package com.xuecheng.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by admin on 2018/2/7.
 */
public class Teachplan implements Serializable {
    private static final long serialVersionUID = -916357110051689485L;
  
    private String id;
    private String pname;
    private String parentid;
    private String grade;
    private String ptype;
    private String description;
    private String courseid;
    private String status;
    private Integer orderby;
    private Double timelength;
    private String trylearn;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCourseid() {
		return courseid;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}
	public Double getTimelength() {
		return timelength;
	}
	public void setTimelength(Double timelength) {
		this.timelength = timelength;
	}
	public String getTrylearn() {
		return trylearn;
	}
	public void setTrylearn(String trylearn) {
		this.trylearn = trylearn;
	}
	@Override
	public String toString() {
		return "Teachplan [id=" + id + ", pname=" + pname + ", parentid=" + parentid + ", grade=" + grade + ", ptype="
				+ ptype + ", description=" + description + ", courseid=" + courseid + ", status=" + status
				+ ", orderby=" + orderby + ", timelength=" + timelength + ", trylearn=" + trylearn + "]";
	}
    
}
