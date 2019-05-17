package com.xuecheng.manager.cms_client.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;

@FeignClient(XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient {
	
	/**
	 * 根据pageId查询
	 * @param id
	 * @return
	 */
	@GetMapping("/cms/get/{id}")
	public CmsPage findById(@PathVariable("id")String id);
	
	/**
	 * cmsPage添加
	 */
	@PostMapping("/cms/add")
	public CmsPageResult add(@RequestBody CmsPage cmsPage);
	
	/**
	 * 获取数据模型
	 * @param modelId : cms_config的id
	 */
	@GetMapping("/getModel/{modelId}")
	public CmsConfig getModel(@PathVariable("modelId") String modelId);
}
