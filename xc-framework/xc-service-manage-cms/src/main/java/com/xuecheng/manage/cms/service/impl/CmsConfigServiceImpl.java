package com.xuecheng.manage.cms.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.domain.cms.CmsConfig;
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

}
