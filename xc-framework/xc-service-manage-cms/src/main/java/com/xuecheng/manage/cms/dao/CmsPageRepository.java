package com.xuecheng.manage.cms.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xuecheng.framework.common.model.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.CmsPage;
/**
 * MongoRepository<T, ID> 指定数据类型和主键类型
 * @author Administrator
 *
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
	CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
