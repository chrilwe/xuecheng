package com.xuecheng.ucenter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.mapper.XcCompanyUserMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.service.XcUserService;

@Service
public class XcUserServiceImpl implements XcUserService {
	
	@Autowired
	private XcUserMapper xcUserMapper;
	@Autowired
	private XcCompanyUserMapper xcCompanyUserMapper;

	@Override
	public XcUserExt findByUserName(String username) {
		//根据用户名查询xcuser
		XcUser xcUser = xcUserMapper.findByUserName(username);
		if(xcUser == null) {
			return null;
		}
		
		//根据用户id查询xccompanyuser
		XcUserExt xcUserExt = new XcUserExt();
		XcCompanyUser xcCompanyUser = xcCompanyUserMapper.findByUserId(xcUser.getId());
		if(xcCompanyUser != null) {
			xcUserExt.setCompanyId(xcCompanyUser.getCompanyId());
		}
		
		xcUserExt.setBirthday(xcUser.getBirthday());
		xcUserExt.setCreateTime(xcUser.getCreateTime());
		xcUserExt.setEmail(xcUser.getEmail());
		xcUserExt.setId(xcUser.getId());
		xcUserExt.setName(xcUser.getName());
		xcUserExt.setPassword(xcUser.getPassword());
		return xcUserExt;
	}

	@Override
	public XcMenuExt findXcMenuExtByUserName(String username) {
		XcMenu xcMenu = xcUserMapper.findXcMenuByUserName(username);
		XcMenuExt xcMenuExt = new XcMenuExt();
		
		return xcMenuExt;
	}

}
