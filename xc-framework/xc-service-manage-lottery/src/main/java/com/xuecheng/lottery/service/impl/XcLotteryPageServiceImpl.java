package com.xuecheng.lottery.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.status.PageStatus;
import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;
import com.xuecheng.framework.domain.lottery.ext.LotteryStatus;
import com.xuecheng.framework.domain.lottery.request.LotteryPageRequest;
import com.xuecheng.framework.domain.lottery.response.LotteryResponseMessage;
import com.xuecheng.framework.domain.lottery.response.ShowLotteryPageResult;
import com.xuecheng.lottery.client.CmsPageClient;
import com.xuecheng.lottery.mapper.XcLotteryDetailsMapper;
import com.xuecheng.lottery.mapper.XcLotteryMapper;
import com.xuecheng.lottery.service.XcLotteryPageService;

@Service
public class XcLotteryPageServiceImpl implements XcLotteryPageService {
	private static final Logger log = LoggerFactory.getLogger(XcLotteryPageService.class);
	@Autowired
	private CmsPageClient cmsPageClient;
	@Autowired
	private XcLotteryMapper xcLotteryMapper;
	@Autowired
	private XcLotteryDetailsMapper xcLotteryDetailsMapper;
	
	@Value("${xuecheng.page.pagePhysicalPath}")
	private String pagePhysicalPath;
	@Value("${xuecheng.page.dataUrl}")
	private String dataUrl;
	@Value("${xuecheng.page.page_uri}")
	private String pageUri;
	
	/**
	 * 抽奖页面预览
	 */
	@Override
	public String preViewLotteryPage(LotteryPageRequest request) {
		boolean validate = this.validate(request);
		if(!validate) {
			return null;
		}
		
		String lotteryId = request.getLotteryId();
		String templateId = request.getTemplateId();
		String siteId = request.getSiteId();
		
		//1.创建抽奖页面的数据模型
		XcLottery xcLottery = xcLotteryMapper.findById(lotteryId);
		this.createCmsConfig(xcLottery, lotteryId);
		
		//2.创建页面CmsPage基本信息
		CmsPage cmsPage = this.createCmsPage(lotteryId,templateId,siteId);
		
		//3.生成静态页面
		String html = cmsPageClient.getHtml(lotteryId);
		
		return null;
	}
	
	//校验参数
	private boolean validate(LotteryPageRequest request) {
		if(request == null) {
			log.error("参数错误");
			return false;
		}
		String lotteryId = request.getLotteryId();
		String templateId = request.getTemplateId();
		String siteId = request.getSiteId();
		if(StringUtils.isEmpty(lotteryId)) {
			log.error("抽奖项目id是空值");
			return false;
		}
		
		if(StringUtils.isEmpty(templateId)) {
			log.error("抽奖项目模板不能为空");
			return false;
		}
		
		if(StringUtils.isEmpty(siteId)) {
			log.error("抽奖项目所属站点id不能为空");
			return false;
		}
		return true;
	}
	
	//生成CmsPage页面基本信息
	private CmsPage createCmsPage(String lotteryId,String templateId,String siteId) {
		CmsPage cmsPage = new CmsPage();
		cmsPage.setPageCreateTime(new Date());
		cmsPage.setPageAliase("lottery");
		cmsPage.setPageHtml("lottery");
		cmsPage.setPageId(lotteryId);
		cmsPage.setPageName(lotteryId + ".html");
		cmsPage.setPagePhysicalPath(pagePhysicalPath);
		cmsPage.setPageStatus(PageStatus.NO);
		cmsPage.setPageType("static");
		cmsPage.setPageWebPath(pageUri + lotteryId + ".html");
		cmsPage.setSiteId(siteId);
		cmsPage.setTemplateId(templateId);
		cmsPage.setDataUrl(dataUrl + lotteryId);
		CmsPageResult addCmsPageResult = cmsPageClient.add(cmsPage);
		return cmsPage;
	}
	
	//生成抽奖页面的数据模型
	private CmsConfig createCmsConfig(Object data,String cmsConfigId) {
		if(data == null) {
			log.error("数据模型不允许为空");
			return null;
		}
		if(StringUtils.isEmpty(cmsConfigId)) {
			log.error("数据模型id不能为空");
			return null;
		}
		
		List<CmsConfigModel> list = new ArrayList<CmsConfigModel>();
		CmsConfigModel cmsConfigModel = new CmsConfigModel();
		cmsConfigModel.setValue(JSON.toJSONString(data));
		
		CmsConfig cmsConfig = new CmsConfig();
		cmsConfig.setId(cmsConfigId);
		cmsConfig.setModel(list);
		
		cmsPageClient.addCmsConfig(cmsConfig);
		return cmsConfig;
	}
	
	/**
	 * 抽奖页面发布
	 */
	@Override
	@Transactional
	public ShowLotteryPageResult showLotteryPage(String lotteryId) {
		if(StringUtils.isEmpty(lotteryId)) {
			return new ShowLotteryPageResult(false,LotteryResponseMessage.ERRORPARAMS);
		}
		
		CmsPage cmsPage = cmsPageClient.findById(lotteryId);
		if(cmsPage == null) {
			return new ShowLotteryPageResult(false,LotteryResponseMessage.PLEASE_PREVIEWPAGE);
		}
		
		//更新页面状态
		cmsPage.setPageStatus(PageStatus.YES);
		cmsPageClient.edit(cmsPage, lotteryId);
		//更新抽奖状态
		XcLottery xcLottery = xcLotteryMapper.findById(lotteryId);
		List<XcLotteryDetails> list = xcLotteryDetailsMapper.findByLotteryId(lotteryId, LotteryStatus.NO);
		xcLottery.setStatus(LotteryStatus.YES);
		xcLotteryMapper.update(xcLottery);
		for(XcLotteryDetails xcLotteryDetails : list) {
			xcLotteryDetails.setStatus(LotteryStatus.DETAIL_YES);
			xcLotteryDetailsMapper.update(xcLotteryDetails);
		}
		return new ShowLotteryPageResult(true,LotteryResponseMessage.SUCCESS);
	}

}
