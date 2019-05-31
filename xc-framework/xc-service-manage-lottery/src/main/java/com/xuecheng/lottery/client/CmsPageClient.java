package com.xuecheng.lottery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;

@FeignClient(XcServiceList.XC_SERVICE_MANAGE_LOTTERY)
@RequestMapping("/cms")
public interface CmsPageClient {
	
	/**
	 * 获取预览页面HTML数据
	 */
	@GetMapping("/getHtml/{pageId}")
	public String getHtml(@PathVariable("pageId")String pageId);
	
	/**
	 * 发布静态页面
	 */
	@GetMapping("/createhtml/{id}")
	public CmsPageResult createHtml(@PathVariable("id")String id);
	
	/**
	 * cmsPage添加
	 */
	@PostMapping("/cmsPage/add")
	public CmsPageResult add(@RequestBody CmsPage cmsPage);
	
	/**
	 * 添加页面的数据模型
	 */
	@PostMapping("/cmsConfig/add")
	public CmsPageResult addCmsConfig(CmsConfig cmsConfig);
	
	/**
	 * 根据页面id查询
	 */
	@GetMapping("/get/{id}")
	public CmsPage findById(@PathVariable("id")String id);
	
	/**
	 * 修改页面信息
	 * @param cmsPage
	 * @param id
	 * @return
	 */
	@PutMapping("/update/{id}")
	public CmsPageResult edit(@RequestBody CmsPage cmsPage, @PathVariable("id")String id);
}
