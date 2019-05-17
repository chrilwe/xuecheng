package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;

import lombok.Data;
import lombok.ToString;

/**
 * Created by mrt on 2018/3/20.
 */
public class AddCourseResult extends ResponseResult {
	
    public AddCourseResult() {
		super();
	}
	public AddCourseResult(ResultCode resultCode,String courseid) {
        super(resultCode);
        this.courseid = courseid;
    }
    private String courseid;
	public String getCourseid() {
		return courseid;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}
	@Override
	public String toString() {
		return "AddCourseResult [courseid=" + courseid + "]";
	}

}
