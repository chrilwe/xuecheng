package com.xuecheng.message.system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePub;

@FeignClient(XcServiceList.XC_SERVICE_MANAGE_COURSE)
@RequestMapping("/course")
public interface CourseClient {
	/**
	 * 根据课程id查询课程基本信息
	 */
	@GetMapping("/find/coursebase/{courseId}")
	public CourseBase findCourseBaseByCourseid(@PathVariable("courseId")String courseId);
	
	
	@GetMapping("/find/coursePub/{courseId}")
	public CoursePub findCoursePubByCourseId(@PathVariable("courseId")String courseId);
}
