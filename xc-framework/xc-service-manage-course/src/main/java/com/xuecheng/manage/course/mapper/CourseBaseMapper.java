package com.xuecheng.manage.course.mapper;

import com.xuecheng.framework.domain.course.CourseBase;

public interface CourseBaseMapper {
	//插入课程基本信息
	public int addCourse(CourseBase courseBase);
	//根据courseid查询课程
	public CourseBase findByCourseId(String courseId);
}
