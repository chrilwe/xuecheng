package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;

public interface XcUserService {
	public XcUserExt findByUserName(String username);//根据用户名查询usertxt
	public XcMenuExt findXcMenuExtByUserName(String username);//根据账号查询权限
}
