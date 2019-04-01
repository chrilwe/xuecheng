package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import com.xuecheng.framework.domain.cms.CmsPage;
import lombok.Data;

/**
 * Created by mrt on 2018/3/31.
 */

public class CmsPageResult extends ResponseResult {
    CmsPage cmsPage;
    public CmsPageResult(ResultCode resultCode,CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
	public CmsPage getCmsPage() {
		return cmsPage;
	}
	public void setCmsPage(CmsPage cmsPage) {
		this.cmsPage = cmsPage;
	}
	@Override
	public String toString() {
		return "CmsPageResult [cmsPage=" + cmsPage + "]";
	}
    
}
