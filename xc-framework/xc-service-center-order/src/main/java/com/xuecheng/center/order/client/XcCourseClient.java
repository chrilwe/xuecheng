package com.xuecheng.center.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.course.CoursePub;

@FeignClient(XcServiceList.XC_SERVICE_MANAGE_COURSE)
@RequestMapping("/course")
public interface XcCourseClient {
	
	/**
	 * 根据课程id查询课程发布的信息
	 * @param courseId
	 * @return
	 */
	@GetMapping("/find/coursePub/{courseId}")
	public CoursePub findCoursePubByCourseId(@PathVariable("courseId")String courseId);
}
