package com.xuecheng.search.service;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.search.CourseSearchParam;

public interface EsCourseService {
	//根据课程id查询
	public Map<String, CoursePub> getall(String id);
	//添加课程搜索库
	public ResponseResult addCourseToEs(CoursePub coursePub);
	//根据courseId查询coursePub
	public QueryResponseResult findCoursePubByCourseId(String courseId);
	//条件查询
	public QueryResponseResult list(int page, 
			int size, CourseSearchParam courseSearchParam);
}
