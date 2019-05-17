package com.xuecheng.message.system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;

@FeignClient(XcServiceList.XC_SERVICE_SEARCH)
@RequestMapping("/search/course")
public interface SearchClient {
	
	/**
	 * 添加课程信息
	 */
	@PostMapping("/add")
	public ResponseResult addCourseToEs(CoursePub coursePub);
}
