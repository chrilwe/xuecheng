package com.xuecheng.manage.course.mapper;

import com.xuecheng.framework.domain.course.CourseOff;

public interface CourseOffMapper {
	//根据课程id查询courseOff
	public CourseOff findCourseOffByCourseId(String courseId);
}
