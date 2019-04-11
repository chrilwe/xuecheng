package com.xuecheng.manage.cms.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.request.QueryPageRequest;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.QueryResult;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.manage.cms.config.RabbitMqConfig;
import com.xuecheng.manage.cms.dao.CmsPageRepository;
import com.xuecheng.manage.cms.dao.CmsTemplateRepository;
import com.xuecheng.manage.cms.service.CmsPageService;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class CmsPageServiceImpl implements CmsPageService {

	@Autowired
	private CmsPageRepository cmsPageRepository;
	@Autowired
	private CmsTemplateRepository cmsTemplateRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private GridFSBucket gridFSBucket;
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	

	@Override
	public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
		// 条件查询空，实例化一个条件对象,防止空指针
		if (queryPageRequest == null) {
			queryPageRequest = new QueryPageRequest();
		}

		CmsPage cmsPage = new CmsPage();
		// 站点查询条件
		if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
			cmsPage.setSiteId(queryPageRequest.getSiteId());
		}

		// 模板id查询条件
		if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
			cmsPage.setTemplateId(queryPageRequest.getTemplateId());
		}

		// 页面别名查询条件
		if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
			cmsPage.setPageAliase(queryPageRequest.getPageAliase());
		}

		if (page <= 0) {
			page = 1;
		}
		page = page - 1;

		Pageable pageable = PageRequest.of(page, size);
		ExampleMatcher matcher = ExampleMatcher.matching();
		matcher = matcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
		Example<CmsPage> example = Example.of(cmsPage, matcher);
		Page<CmsPage> cmPage = cmsPageRepository.findAll(example, pageable);
		List<CmsPage> cmsPages = cmPage.getContent();// 获取数据集合
		long totalElements = cmPage.getTotalElements();// 获取数据总记录

		QueryResult queryResult = new QueryResult();
		queryResult.setList(cmsPages);
		System.out.println(cmsPages);
		queryResult.setTotal(totalElements);
		QueryResponseResult result = new QueryResponseResult(CommonCode.SUCCESS, queryResult);

		return result;
	}

	@Override
	public CmsPageResult add(CmsPage cmsPage) {
		if (cmsPage == null) {
			// 抛出非法参数异常
			ExceptionCast.cast(CmsCode.CMS_INVALID_PARAM);
		}
		if(StringUtils.isEmpty(cmsPage.getDataUrl())) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);//dataUrl为空
		}
		if(StringUtils.isEmpty(cmsPage.getPageName())) {
			ExceptionCast.cast(CmsCode.CMS_PAGENAMEISNULL);//页面名称为空
		}
		if(StringUtils.isEmpty(cmsPage.getSiteId())) {
			ExceptionCast.cast(CmsCode.CMS_TEMPLATE_SITE_NULL);
		}
		if(StringUtils.isEmpty(cmsPage.getTemplateId())) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
		}

		CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
				cmsPage.getSiteId(), cmsPage.getPageWebPath());
		System.out.println("====查询siteId=" + cmsPage.getSiteId() + "pageName=" + cmsPage.getPageName() + "webPagePath="
				+ cmsPage.getPageWebPath() + "====:" + cmsPage1);
		if (cmsPage1 != null) {
			// 抛出页面已经存在异常
			ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
		}
		cmsPageRepository.save(cmsPage);
		return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
	}

	@Override
	public CmsPage findById(String id) {

		Optional<CmsPage> optional = cmsPageRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public CmsPageResult update(CmsPage cmsPage, String id) {
		// 根据id查询页面信息
		CmsPage page = findById(id);
		if (cmsPage != null) {
			page.setTemplateId(cmsPage.getTemplateId());
			page.setSiteId(cmsPage.getSiteId());
			page.setPageAliase(cmsPage.getPageAliase());
			page.setPageName(cmsPage.getPageName());
			page.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
			page.setPageWebPath(cmsPage.getPageWebPath());
		}

		// 更新页面
		cmsPageRepository.save(page);

		return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
	}

	@Override
	public String getPageHtml(String pageId) {
		// 获取页面数据
		CmsPage cmsPage = this.findById(pageId);
		if (cmsPage == null) {
			ExceptionCast.cast(CmsCode.CMS_PAGEISNOTEXIST);// 页面不存在
		}
		
		// 获取数据模型
		Map model = this.getModel(cmsPage);
		if(model == null) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);//根据页面的数据url获取不到数据
		}
		
		// 获取模板数据
		String template = this.getTemplateByPageId(cmsPage);
		if(StringUtils.isEmpty(template)) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);//模板不存在
		}
		
		// 页面静态化
		String html = this.generateHtml(model, template);
		return html;
	}

	// 数据模型获取
	private Map getModel(CmsPage cmsPage) {
		// 根据dataUrl查询数据模型
		String dataUrl = cmsPage.getDataUrl();
		if (StringUtils.isEmpty(dataUrl)) {
			// 数据模型不存在
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);//从页面信息中找不到获取数据的url！
		}
		ResponseEntity<CmsConfig> response = restTemplate.getForEntity(dataUrl, CmsConfig.class);
		if(response == null) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);//根据页面的数据url获取不到数据
		}
		CmsConfig cmsConfig = response.getBody();
		List<CmsConfigModel> model = cmsConfig.getModel();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("model", model);
		map.put("name", cmsConfig.getName());
		/*Map<String,String> model = new HashMap<String,String>();
		model.put("test", "test");*/
		return map;
	}

	// 模板数据获取
	private String getTemplateByPageId(CmsPage cmsPage) {
		//获取模板id
		String templateId = cmsPage.getTemplateId();
		
		//根据模板id查询模板文件fieldId
		String fieldId = "";
		Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
		if(optional.isPresent()) {
			CmsTemplate cmsTemplate = optional.get();
			if(cmsTemplate == null) {
				return null;
			}
			fieldId = cmsTemplate.getTemplateFileId();
			if(StringUtils.isEmpty(fieldId)) {
				return null;
			}
		}
		
		//获取模板文件
		String fileData = findFileByFieldId(fieldId);
		
		/*String stringTemplate = "<html><head>test</head><body>${test}<body></html>";*/
		return fileData;
	}

	// 页面静态化
	private String generateHtml(Map model, String template) {
		String content = null;
		try {
			Configuration configuration = new Configuration(Configuration.getVersion());
			// 模板加载器
			StringTemplateLoader templateLoader = new StringTemplateLoader();
			templateLoader.putTemplate("template", template);
			configuration.setTemplateLoader(templateLoader);
			// 获取模板
			Template t = configuration.getTemplate("template");

			// 页面静态化
			content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		} catch (Exception e) {
			return null;
		}
		return content;
	}
	
	/**
	 * 存储html页面
	 */
	@Override
	public CmsPageResult saveHtml(String pageHtml, String pageId) {
		// 获取页面名称
		CmsPage cmsPage = this.findById(pageId);
		if (cmsPage == null) {
			ExceptionCast.cast(CmsCode.CMS_PAGEISNOTEXIST);// 页面不存在
		}
		String pageName = cmsPage.getPageName();
		
		//存储HTML
		ByteArrayInputStream stream = new ByteArrayInputStream(pageHtml.getBytes());
		ObjectId objectId = gridFsTemplate.store(stream, pageName);
		String fieldId = objectId.toString();
		
		//更新fieldId到cmspage中
		CmsPage page = this.findById(pageId);
		page.setHtmlFileId(fieldId);
		cmsPageRepository.deleteById(pageId);
		cmsPageRepository.save(page);
		
		//将pageId发送到消息队列
		String type = "";
		if(page.getPageType().equals("0")) {
			type = "protal";//门户页面内容
		} else if(page.getPageType().equals("1")) {
			type = "course";//课程详情页
		}
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM, "inform.#."+type+".#", pageId);
		
		return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
	}

	@Override
	public String getHtmlByFieldId(String fieldId) {
		
		return findFileByFieldId(fieldId);
	}
	
	/**
	 * 根据fieldId查询文件
	 * @param fieldId
	 * @return
	 */
	private String findFileByFieldId(String fieldId) {
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fieldId)));
		//打开下载流下载文件
		GridFSDownloadStream stream = gridFSBucket.openDownloadStream(file.getObjectId());
		//创建gridfsresource
		GridFsResource resource = new GridFsResource(file, stream);
		//获取数据
		String fileData = "";
		try {
			fileData = IOUtils.toString(resource.getInputStream(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fileData;
	}
	
}
