package com.xuecheng.framework.domain.cms.response;


import com.xuecheng.framework.common.model.response.ResultCode;
import lombok.ToString;

/**
 * Created by mrt on 2018/3/5.
 */
@ToString
public enum CmsCode implements ResultCode {
	CMS_PAGEISNOTEXIST(false,24000,"页面不存在"),
    CMS_ADDPAGE_EXISTSNAME(false,24001,"页面名称已存在！"),
    CMS_GENERATEHTML_DATAURLISNULL(false,24002,"从页面信息中找不到获取数据的url！"),
    CMS_GENERATEHTML_DATAISNULL(false,24003,"根据页面的数据url获取不到数据！"),
    CMS_GENERATEHTML_TEMPLATEISNULL(false,24004,"页面模板为空！"),
    CMS_GENERATEHTML_HTMLISNULL(false,24005,"生成的静态html为空！"),
    CMS_GENERATEHTML_SAVEHTMLERROR(false,24006,"保存静态html出错！"),
    CMS_COURSE_PERVIEWISNULL(false,24007,"预览页面为空！"),
	CMS_INVALID_PARAM(false,24008,"非法参数!"),
	CMS_TEMPLATE_NAME_NULL(false,24009,"页面模板名称为空！"),
	CMS_TEMPLATE_SITE_NULL(false,24010,"模板关联的站点id为空!"),
	CMS_PAGENAMEISNULL(false,24011,"页面名称为空!"),
	CMS_ADDCMSPAGE_FAIL(false,24012,"添加CMS_PAGE失败!"),
	CMS_ADDCMSCONFIG_FAIL(false,24012,"添加CMS_CONFIG失败!");
    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CmsCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public boolean success() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int code() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String message() {
		// TODO Auto-generated method stub
		return null;
	}

   
}
