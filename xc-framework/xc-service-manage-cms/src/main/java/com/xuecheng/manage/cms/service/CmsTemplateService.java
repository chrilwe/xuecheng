package com.xuecheng.manage.cms.service;

import java.util.List;

import com.xuecheng.framework.domain.cms.CmsTemplate;

public interface CmsTemplateService {
	//保存模板文件
	public String saveTemplate(String template, String templateName,
			String siteId, String templateParameter);
	//查询全部
	public List<CmsTemplate> findAll();
}
