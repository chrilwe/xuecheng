package com.xuecheng.manager.cms_client.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsPage;

@FeignClient(value=XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient {
	
	@GetMapping("/cms/get/{id}")
	public CmsPage findById(@PathVariable("id")String id);
}
