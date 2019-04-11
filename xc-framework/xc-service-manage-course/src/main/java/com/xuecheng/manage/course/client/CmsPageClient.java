package com.xuecheng.manage.course.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;

@FeignClient(XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient {
	/**
	 * 发布静态页面
	 * @param id
	 * @return
	 */
	@GetMapping("/createhtml/{id}")
	public CmsPageResult createHtml(@PathVariable("id")String id);
}
