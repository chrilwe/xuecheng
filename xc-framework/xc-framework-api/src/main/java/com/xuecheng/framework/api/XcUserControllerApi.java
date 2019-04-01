package com.xuecheng.framework.api;

import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;

public interface XcUserControllerApi {
	public XcUserExt findByUserName(String username);
	public XcMenuExt findXcMenuExtByUserName(String username);
}
