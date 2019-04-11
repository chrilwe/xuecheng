package com.xuecheng.search.service;

import java.util.Map;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;

public interface EsCourseService {
	//根据课程id查询
	public Map<String, CoursePub> getall(String id);
	//添加课程和媒体信息到搜索库
	public ResponseResult addCourseToEs(CoursePub coursePub, TeachplanMedia teachplanMedia);
	//根据courseId查询coursePub
	public QueryResponseResult findCoursePubByCourseId(String courseId);
}
