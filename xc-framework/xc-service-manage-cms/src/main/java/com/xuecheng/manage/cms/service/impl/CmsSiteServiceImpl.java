package com.xuecheng.manage.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage.cms.dao.CmsSiteRepository;
import com.xuecheng.manage.cms.service.CmsSiteService;

@Service
public class CmsSiteServiceImpl implements CmsSiteService {
	
	@Autowired
	private CmsSiteRepository cmsSiteRepository;

	@Override
	public List<CmsSite> findAll() {
		List<CmsSite> list = cmsSiteRepository.findAll();
		return list;
	}
	
	
}
