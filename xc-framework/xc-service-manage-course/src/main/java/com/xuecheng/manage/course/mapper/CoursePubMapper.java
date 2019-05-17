package com.xuecheng.manage.course.mapper;

import com.xuecheng.framework.domain.course.CoursePub;

public interface CoursePubMapper {
	public int addCoursePub(CoursePub coursePub);
	public CoursePub findCoursePubByCourseId(String courseId);
}
