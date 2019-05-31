package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;

public interface XcUserControllerApi {
	public XcUserExt findByUserName(String username);
	public XcMenuExt findXcMenuExtByUserName(String username);
	public XcUser findByUserId(String userId);
	public ResponseResult register(XcUserExt xcUserExt);
	public ResponseResult checkPhone(String phone);
	public ResponseResult checkEmail(String email);
}
