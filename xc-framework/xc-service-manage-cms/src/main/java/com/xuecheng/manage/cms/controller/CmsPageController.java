package com.xuecheng.manage.cms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.CmsPageControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.request.QueryPageRequest;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.manage.cms.service.CmsConfigService;
import com.xuecheng.manage.cms.service.CmsPageService;
import com.xuecheng.manage.cms.service.CmsSiteService;
import com.xuecheng.manage.cms.service.CmsTemplateService;

@RestController
@RequestMapping("/cms")
public class CmsPageController implements CmsPageControllerApi {
	
	@Autowired
	private CmsPageService cmsPageService;
	@Autowired
	private CmsConfigService cmsConfigService;
	@Autowired
	private CmsTemplateService cmsTemplateService;
	@Autowired
	private CmsSiteService cmsSiteService;
	
	/**
	 * 分页查询页面
	 * @param page:当前页面
	 * @param size:页面大小
	 */
	@GetMapping("/list/{page}/{size}")
	public QueryResponseResult findList(@PathVariable("page")int page, @PathVariable("size")int size, QueryPageRequest queryPageRequest) {
		QueryResponseResult result = null;
		try {
			result = cmsPageService.findList(page, size, queryPageRequest);
		} catch (Exception e) {
			result = new QueryResponseResult(CommonCode.FAIL, null);
		}
		System.out.println(result);
		return result;
	}
	
	/**
	 * cmsPage添加
	 */
	@PostMapping("/cmsPage/add")
	public CmsPageResult add(@RequestBody CmsPage cmsPage) {
		
		return cmsPageService.add(cmsPage);
	}
	
	/**
	 * 根据页面id查询
	 */
	@GetMapping("/get/{id}")
	public CmsPage findById(@PathVariable("id")String id) {
		
		return cmsPageService.findById(id);
	}
	
	/**
	 * 修改页面信息
	 * @param cmsPage
	 * @param id
	 * @return
	 */
	@PutMapping("/update/{id}")
	public CmsPageResult edit(@RequestBody CmsPage cmsPage, @PathVariable("id")String id) {
		
		return cmsPageService.update(cmsPage, id);
	}
	
	/**
	 * 发布静态页面
	 */
	@GetMapping("/createhtml/{id}")
	public CmsPageResult createHtml(@PathVariable("id")String id) {
		//生成页面
		String pageHtml = cmsPageService.getPageHtml(id);
		//将页面存放到GFS中,发送消息到客户端生成html文件推送到nginx中
		CmsPageResult result = cmsPageService.saveHtml(pageHtml, id);
		return result;
	}
	
	/**
	 * 获取数据模型
	 * @param modelId : cms_config的id
	 */
	@GetMapping("/getModel/{modelId}")
	public CmsConfig getModel(@PathVariable("modelId") String modelId) {
		return cmsConfigService.findById(modelId);
	}
	
	/**
	 * 上传模板文件
	 * @param template ：模板文件
	 * @param templateName ：模板名称
	 * @param siteId ：站点id
	 * @param templateParameter ： 模板参数
	 * @return fieldId : 文件的fieldId
	 */
	@Override
	@GetMapping("/template/save/{siteId}")
	public String uploadTemplate(@RequestParam("templateName")String templateName,
			@PathVariable("siteId")String siteId,
			@RequestParam(name="templateParameter",defaultValue="")String templateParameter) {
		
		if(StringUtils.isEmpty(templateName)) {
			ExceptionCast.cast(CmsCode.CMS_TEMPLATE_NAME_NULL);//模板名称为空
		}
		if(StringUtils.isEmpty(siteId)) {
			ExceptionCast.cast(CmsCode.CMS_TEMPLATE_SITE_NULL);//站点id为空
		}
		String templatePath = this.getClass().getResource("/").getPath() + "templates/"+templateName+".ftl";
		String template = "";
		try {
			template = IOUtils.toString(new FileInputStream(new File(templatePath)), "utf-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fieldId = cmsTemplateService.saveTemplate(template, templateName, siteId, templateParameter);
		
		return fieldId;
	}
	
	/**
	 * 查询所有站点
	 */
	@Override
	@GetMapping("/site/list")
	public List<CmsSite> findAllSite() {
		
		return cmsSiteService.findAll();
	}
	
	/**
	 * 查询所有模板
	 */
	@Override
	@GetMapping("/template/list")
	public List<CmsTemplate> findAllTemplate() {
		
		return cmsTemplateService.findAll();
	}
	
	/**
	 * 添加页面的数据模型
	 */
	@Override
	@PostMapping("/cmsConfig/add")
	public CmsPageResult addCmsConfig(CmsConfig cmsConfig) {
		CmsPageResult result = cmsConfigService.addCmsConfig(cmsConfig);
		return result;
	}
	
	/**
	 * 查询所有的数据模型
	 */
	@Override
	@GetMapping("/cmsConfig/findAll")
	public List<CmsConfig> findCmsConfigs() {
		List<CmsConfig> cmsConfigs = cmsConfigService.findCmsConfigs();
		return cmsConfigs;
	}
	
	/**
	 * 获取预览页面HTML数据
	 */
	@Override
	@GetMapping("/getHtml/{pageId}")
	public String getHtml(@PathVariable("pageId")String pageId) {
		
		return cmsPageService.getPageHtml(pageId);
	}
	
}
