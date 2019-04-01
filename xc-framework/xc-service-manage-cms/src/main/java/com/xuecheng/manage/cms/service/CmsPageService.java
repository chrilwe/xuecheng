package com.xuecheng.manage.cms.service;

import com.xuecheng.framework.common.model.request.QueryPageRequest;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;

public interface CmsPageService {
	
	//分页查询cmspage集合数据
	public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
	//添加cmsPage
	public CmsPageResult add(CmsPage cmsPage);
	//根据页面id查询
	public CmsPage findById(String id);
	//修改页面信息
	public CmsPageResult update(CmsPage cmsPage, String id);
	//页面静态化
	public String getPageHtml(String pageId);
	//将静态化的页面放到GFS中
	public CmsPageResult saveHtml(String pageHtml, String pageId);
	//根据fieldID查询静态页面文件
	public String getHtmlByFieldId(String fieldId);

}
