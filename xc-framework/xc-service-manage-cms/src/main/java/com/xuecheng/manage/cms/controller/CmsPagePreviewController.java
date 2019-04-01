package com.xuecheng.manage.cms.controller;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuecheng.framework.api.CmsPagePreviewControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.web.BaseController;
import com.xuecheng.manage.cms.service.CmsPageService;

@Controller
@RequestMapping("/cms")
public class CmsPagePreviewController extends BaseController implements CmsPagePreviewControllerApi {
	
	@Autowired
	private CmsPageService cmsPageService;
	/**
	 * 页面预览
	 */
	@GetMapping("/preview/{pageId}")
	public void preview(@PathVariable("pageId")String pageId) {
		//获取生成的HTML字符串
		String pageHtml = cmsPageService.getPageHtml(pageId);
		
		//将生成的静态页面的字符串通过response返回
		try {
			if(StringUtils.isNotEmpty(pageHtml)) {
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(pageHtml.getBytes("utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
