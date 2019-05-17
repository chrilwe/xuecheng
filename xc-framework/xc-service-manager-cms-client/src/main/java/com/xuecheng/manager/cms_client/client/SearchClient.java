package com.xuecheng.manager.cms_client.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;

@FeignClient(XcServiceList.XC_SERVICE_SEARCH)
public interface SearchClient {
	/**
	 * 导入数据到索引库
	 * @param coursePub
	 * @param teachplanMedia
	 * @return
	 */
	@PostMapping("/add")
	public ResponseResult addCourseToEs(CoursePub coursePub, TeachplanMedia teachplanMedia);
}
