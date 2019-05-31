package com.xuecheng.ucenter.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.domain.ucenter.response.UcenterCode;
import com.xuecheng.framework.utils.BCryptUtil;
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

	@Override
	public XcUser findByUserid(String userId) {
		if(StringUtils.isEmpty(userId)) {
			return null;
		}
		return xcUserMapper.findByUserid(userId);
	}

	@Override
	@Transactional
	public ResponseResult register(XcUserExt xcUserExt) {
		if(xcUserExt == null) {
			return new ResponseResult(UcenterCode.UCENTER_PARAMS_ERROR);
		}
		
		//判断用户信息是否已注册
		XcUserExt user = this.findByUserName(xcUserExt.getUsername());
		if(user != null) {
			return new ResponseResult(UcenterCode.UCENTER_USERNAME_ERROR);
		}
		String phone = xcUserExt.getPhone();
		String email = xcUserExt.getEmail();
		if(!StringUtils.isEmpty(phone)) {
			XcUser xcUser = this.findByPhone(phone);
			if(xcUser != null) {
				return new ResponseResult(UcenterCode.UCENTER_PHONE_ERROR);
			}
		}
		if(!StringUtils.isEmpty(email)) {
			XcUser xcUser = this.findByEmail(email);
			if(xcUser != null) {
				return new ResponseResult(UcenterCode.UCENTER_EMAIL_ERROR);
			}
		}
		
		//用户信息入库
		String userId = UUID.randomUUID().toString();
		if(StringUtils.isEmpty(xcUserExt.getId())) {
			xcUserExt.setId(userId);
			xcUserExt.setCreateTime(new Date());
		}
		if(StringUtils.isEmpty(xcUserExt.getPassword())) {
			ExceptionCast.cast(UcenterCode.UCENTER_PARAMS_ERROR);
		}
		xcUserExt.setPassword(BCryptUtil.encode(xcUserExt.getPassword()));
		xcUserMapper.add(xcUserExt);
		XcCompanyUser xcCompanyUser = new XcCompanyUser();
		xcCompanyUser.setCompanyId(xcUserExt.getCompanyId());
		xcCompanyUser.setId(UUID.randomUUID().toString());
		xcCompanyUser.setUserId(userId);
		xcCompanyUserMapper.add(xcCompanyUser);
		
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	public XcUser findByPhone(String phone) {
		if(StringUtils.isEmpty(phone)) {
			ExceptionCast.cast(UcenterCode.UCENTER_PARAMS_ERROR);
		}
		XcUser xcUser = xcUserMapper.findByPhone(phone);
		return xcUser;
	}

	@Override
	public XcUser findByEmail(String email) {
		if(StringUtils.isEmpty(email)) {
			ExceptionCast.cast(UcenterCode.UCENTER_PARAMS_ERROR);
		}
		XcUser xcUser = xcUserMapper.findByEmail(email);
		return xcUser;
	}

}
