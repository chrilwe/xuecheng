package com.xuecheng.learning.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.domain.media.response.GetMediaResult;

@FeignClient(XcServiceList.XC_SERVICE_SEARCH)
public interface EsCourseClient {
	/**
	 * 根据课程id查询coursePub
	 */
	@GetMapping("/search/course/coursePub/{courseId}")
	public QueryResponseResult findCoursePubByCourseId(@PathVariable("courseId")String courseId);

	/**
	 * 根据courseId和teachplanId查询文件访问地址
	 */
	@GetMapping("/getMedia/{courseId}/{teachplanId}")
	public GetMediaResult getMedia(@PathVariable("courseId")String courseId, 
			@PathVariable("teachplanId")String teachplanId);
}
