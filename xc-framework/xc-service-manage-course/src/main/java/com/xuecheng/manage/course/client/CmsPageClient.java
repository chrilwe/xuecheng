package com.xuecheng.manage.course.client;

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
	 * 发布静态页面
	 * @param id
	 * @return
	 */
	@GetMapping("/cms/createhtml/{id}")
	public CmsPageResult createHtml(@PathVariable("id")String id);
	
	/**
	 * cmsPage添加
	 */
	@PostMapping("/cms/cmsPage/add")
	public CmsPageResult add(@RequestBody CmsPage cmsPage);
	
	/**
	 * 添加页面的数据模型
	 */
	@PostMapping("/cms/cmsConfig/add")
	public CmsPageResult addCmsConfig(CmsConfig cmsConfig);
	
	/**
	 * 页面预览
	 */
	@GetMapping("/cms/preview/{pageId}")
	public void preview(@PathVariable("pageId")String pageId);
	
	/**
	 * 获取预览页面HTML数据
	 */
	@GetMapping("/cms/getHtml/{pageId}")
	public String getHtml(@PathVariable("pageId")String pageId);
	
	/**
	 * 根据页面id查询
	 */
	@GetMapping("/cms/get/{id}")
	public CmsPage findById(@PathVariable("id")String id);
}
