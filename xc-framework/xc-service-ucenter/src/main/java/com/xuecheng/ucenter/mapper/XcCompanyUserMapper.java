package com.xuecheng.ucenter.mapper;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;

public interface XcCompanyUserMapper {
	public XcCompanyUser findByUserId(String userId);//根据用户id查询
}
