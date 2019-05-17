package com.xuecheng.manage.cms.testfreemarker;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.course.ext.CourseInfo;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * freemarker test
 * @author Administrator
 *
 */
public class Test {
	
	public static void main(String[] args) throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		//模板路径
		String path = new Test().getClass().getResource("/").getPath();
		System.out.println(path);
		configuration.setDirectoryForTemplateLoading(new File(path + "/templates"));
		//设置字符集
		configuration.setDefaultEncoding("utf-8");
		//加载模板
		Template template = configuration.getTemplate("test.ftl");
		
		Map<String,Object> map = new HashMap<String,Object>();
		//数据模型
		CourseInfo courseInfo = new CourseInfo();
		courseInfo.setName("xiaobai");
		Map<String,Object> mapValue = new HashMap<String,Object>();
		mapValue.put("courseInfo", courseInfo);
		
		List<CmsConfigModel> model = new ArrayList<CmsConfigModel>();
		CmsConfigModel cmsConfigModel = new CmsConfigModel();
		cmsConfigModel.setMapValue(mapValue);
		cmsConfigModel.setName("课程详情页");
		model.add(cmsConfigModel);
		
		CmsConfig cmsConfig = new CmsConfig();
		cmsConfig.setId("11");
		cmsConfig.setModel(model);
		cmsConfig.setName("课程详情页");
		map.put("model", model);
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
		System.out.println(html);
		/*ByteArrayInputStream input = new ByteArrayInputStream(html.getBytes("utf-8"));
		OutputStream output = new FileOutputStream(new File("D:/11.html"));
		int copy = IOUtils.copy(input, output);*/
		
		/*String stringTemplate = "<html><head>test</head><body>${test}<body></html>";
		//模板加载器 
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate("template", stringTemplate);
		configuration.setTemplateLoader(templateLoader);
		//获取模板
		Template template = configuration.getTemplate("template");
		
		
		//数据模型
		Map<String,String> model = new HashMap<String,String>();
		model.put("test", "test");
		//页面静态化
		String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		System.out.println(content);*/
	}
}
