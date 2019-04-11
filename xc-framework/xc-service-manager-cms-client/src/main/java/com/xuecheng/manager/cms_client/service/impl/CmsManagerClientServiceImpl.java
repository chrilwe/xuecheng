package com.xuecheng.manager.cms_client.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.manager.cms_client.client.CmsPageClient;
import com.xuecheng.manager.cms_client.dao.CmsFileRepository;
import com.xuecheng.manager.cms_client.queue.Router;
import com.xuecheng.manager.cms_client.service.CmsManagerClientService;

@Service
public class CmsManagerClientServiceImpl implements CmsManagerClientService {

	@Autowired
	private CmsFileRepository cmsFileRepository;
	@Autowired
	private CmsPageClient cmsPageClient;
	
	@Override
	public boolean generHtml(String pageId) {
		// 调用manage——cms，根据pageId查询htmlFieldId,物理路径,文件名称
		CmsPage cmsPage = cmsPageClient.findById(pageId);
		String htmlFileId = cmsPage.getHtmlFileId();
		String pagePhysicalPath = cmsPage.getPagePhysicalPath();
		String pageName = cmsPage.getPageName();

		// 根据fieldId查询html文件
		String html = cmsFileRepository.findByFieldId(htmlFileId);
		if (StringUtils.isEmpty(html)) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);// 生成的静态html为空！
		}

		// 将静态文件生成html推送到nginx中
		try {
			FileOutputStream output = new FileOutputStream(new File(pagePhysicalPath + "/" + pageName));
			IOUtils.write(html.getBytes(), output);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
