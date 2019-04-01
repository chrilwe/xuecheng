package com.xuecheng.framework.api;

import java.util.List;

import com.xuecheng.framework.common.model.request.QueryPageRequest;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
/**
 * 
 * @author Administrator 页面cms接口api
 *
 */
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
public interface CmsPageControllerApi {
	
	//分页查询页面
	public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
	//cms添加页面
	public CmsPageResult add(CmsPage cmsPage);
	//根据页面id查询
	public CmsPage findById(String id);
	//修改页面内容
	public CmsPageResult edit(CmsPage cmsPage, String id);
	//上传模板文件
	public String uploadTemplate(String templateName,
			String siteId, String templateParameter);
	//查询所有站点
	public List<CmsSite> findAllSite();
	//查询所有页面模板
	public List<CmsTemplate> findAllTemplate();
}
