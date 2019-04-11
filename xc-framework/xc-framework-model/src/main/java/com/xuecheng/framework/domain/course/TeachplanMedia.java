package com.xuecheng.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by admin on 2018/2/7.
 */
public class TeachplanMedia implements Serializable {
    private static final long serialVersionUID = -916357110051689485L;
    
    private String teachplanId;
    private String mediaId;
    private String mediaFileOriginalName;
    private String mediaUrl;
    private String courseId;
	public String getTeachplanId() {
		return teachplanId;
	}
	public void setTeachplanId(String teachplanId) {
		this.teachplanId = teachplanId;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getMediaFileOriginalName() {
		return mediaFileOriginalName;
	}
	public void setMediaFileOriginalName(String mediaFileOriginalName) {
		this.mediaFileOriginalName = mediaFileOriginalName;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	@Override
	public String toString() {
		return "TeachplanMedia [teachplanId=" + teachplanId + ", mediaId=" + mediaId + ", mediaFileOriginalName="
				+ mediaFileOriginalName + ", mediaUrl=" + mediaUrl + ", courseId=" + courseId + "]";
	}
    
}
