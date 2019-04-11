package com.xuecheng.manage.cms.service;

import java.util.List;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;

public interface CmsConfigService {
	public CmsConfig findById(String id);
	public CmsPageResult addCmsConfig(CmsConfig cmsConfig);
	public List<CmsConfig> findCmsConfigs();
}
