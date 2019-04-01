package com.xuecheng.framework.common.model.request;

public class QueryPageRequest extends RequestData {
	
	//站点id
	private String siteId;
	//页面id
	private String pageId;
	//页面名称
	private String pageName;
	//别名
	private String pageAliase;
	//模板id
	private String templateId;
	
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageAliase() {
		return pageAliase;
	}
	public void setPageAliase(String pageAliase) {
		this.pageAliase = pageAliase;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	
	@Override
	public String toString() {
		return "QueryPageRequest [siteId=" + siteId + ", pageId=" + pageId + ", pageName=" + pageName + ", pageAliase="
				+ pageAliase + ", templateId=" + templateId + "]";
	}
	
}
