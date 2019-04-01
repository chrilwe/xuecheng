package com.xuecheng.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by admin on 2018/2/10.
 */
public class CourseBase implements Serializable {
    private static final long serialVersionUID = -916357110051689486L;
  
    private String id;
    private String name;
    private String users;
    private String mt;
    private String st;
    private String grade;
    private String studymodel;
    private String teachmode;
    private String description;
    private String status;
    private String companyId;
    private String userId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "CourseBase [id=" + id + ", name=" + name + ", users=" + users + ", mt=" + mt + ", st=" + st + ", grade="
				+ grade + ", studymodel=" + studymodel + ", teachmode=" + teachmode + ", description=" + description
				+ ", status=" + status + ", companyId=" + companyId + ", userId=" + userId + "]";
	}

}
