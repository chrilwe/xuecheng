package com.xuecheng.manage.cms.service.impl;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.manage.cms.dao.CmsTemplateRepository;
import com.xuecheng.manage.cms.service.CmsTemplateService;

@Service
public class CmsTemplateServiceImpl implements CmsTemplateService {
	
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private CmsTemplateRepository cmsTemplateRepository;

	@Override
	public String saveTemplate(String template, String templateName,
			String siteId, String templateParameter) {
		if(StringUtils.isEmpty(template)) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);//模板为空
		}
		//将文件上传到gfs
		ByteArrayInputStream content = new ByteArrayInputStream(template.getBytes());
		ObjectId objectId = gridFsTemplate.store(content, templateName);
		String fieldId = objectId.toString();
		
		//插入template信息到Mongodb
		CmsTemplate cmsTemplate = new CmsTemplate();
		cmsTemplate.setSiteId(siteId);
		cmsTemplate.setTemplateFileId(fieldId);
		cmsTemplate.setTemplateName(templateName);
		cmsTemplate.setTemplateParameter(templateParameter);
		cmsTemplateRepository.insert(cmsTemplate);
		return fieldId;
	}

	@Override
	public List<CmsTemplate> findAll() {
		
		return cmsTemplateRepository.findAll();
	}

}
