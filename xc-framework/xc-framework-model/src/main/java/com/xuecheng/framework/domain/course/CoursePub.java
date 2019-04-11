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
public class CoursePub implements Serializable {
    private static final long serialVersionUID = -916357110051689487L;
  
    private String id;
    private String name;
    private String users;
    private String mt;
    private String st;
    private String grade;
    private String studymodel;
    private String teachmode;
    private String description;
    private String pic;//图片
    private Date timestamp;//时间戳
    private String charge;
    private String valid;
    private String qq;
    private Float price;
    private Float price_old;
    private String expires;
    private String teachplan;//课程计划
    private String pubTime;//课程发布时间
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
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getStudymodel() {
		return studymodel;
	}
	public void setStudymodel(String studymodel) {
		this.studymodel = studymodel;
	}
	public String getTeachmode() {
		return teachmode;
	}
	public void setTeachmode(String teachmode) {
		this.teachmode = teachmode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	public String getTeachplan() {
		return teachplan;
	}
	public void setTeachplan(String teachplan) {
		this.teachplan = teachplan;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	@Override
	public String toString() {
		return "CoursePub [id=" + id + ", name=" + name + ", users=" + users + ", mt=" + mt + ", st=" + st + ", grade="
				+ grade + ", studymodel=" + studymodel + ", teachmode=" + teachmode + ", description=" + description
				+ ", pic=" + pic + ", timestamp=" + timestamp + ", charge=" + charge + ", valid=" + valid + ", qq=" + qq
				+ ", price=" + price + ", price_old=" + price_old + ", expires=" + expires + ", teachplan=" + teachplan
				+ ", pubTime=" + pubTime + "]";
	}
    
}
