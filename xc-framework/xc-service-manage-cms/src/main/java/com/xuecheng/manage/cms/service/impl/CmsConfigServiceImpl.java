package com.xuecheng.manage.cms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.manage.cms.dao.CmsConfigRepository;
import com.xuecheng.manage.cms.service.CmsConfigService;
@Service
public class CmsConfigServiceImpl implements CmsConfigService {
	
	@Autowired
	private CmsConfigRepository cmsConfigRepository;

	@Override
	public CmsConfig findById(String id) {
		Optional<CmsConfig> one = cmsConfigRepository.findById(id);
		if(one.isPresent()) {
			return one.get();
		}
		return null;
	}

	@Override
	public CmsPageResult addCmsConfig(CmsConfig cmsConfig) {
		if(cmsConfig != null) {
			cmsConfigRepository.save(cmsConfig);
		} 
		return new CmsPageResult(CommonCode.SUCCESS,null);
	}

	@Override
	public List<CmsConfig> findCmsConfigs() {
		List<CmsConfig> cmsConfigs = cmsConfigRepository.findAll();
		return cmsConfigs;
	}

}
