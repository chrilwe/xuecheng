package com.xuecheng.manage.cms.testfreemarker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

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
		/*//模板路径
		String path = new Test().getClass().getResource("/").getPath();
		System.out.println(path);
		configuration.setDirectoryForTemplateLoading(new File(path + "/templates"));
		//设置字符集
		configuration.setDefaultEncoding("utf-8");
		//加载模板
		Template template = configuration.getTemplate("test.ftl");*/
		
		
		String stringTemplate = "<html><head>test</head><body>${test}<body></html>";
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
		System.out.println(content);
	}
}
