package com.xuecheng.ucenter.service;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;

public interface XcUserService {
	public XcUserExt findByUserName(String username);//根据用户名查询usertxt
	public XcMenuExt findXcMenuExtByUserName(String username);//根据账号查询权限
	public XcUser findByUserid(String userId);
	public ResponseResult register(XcUserExt xcUserExt);//注册用户
	public XcUser findByPhone(String phone);//根据手机号查询用户
	public XcUser findByEmail(String email);//根据邮箱查询用户
}
