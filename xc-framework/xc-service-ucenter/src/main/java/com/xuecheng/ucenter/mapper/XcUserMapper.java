package com.xuecheng.ucenter.mapper;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;

public interface XcUserMapper {
	public XcUser findByUserName(String username);//根据用户账号查询
	public XcMenu findXcMenuByUserName(String username);//根据用户账号查询权限
}
