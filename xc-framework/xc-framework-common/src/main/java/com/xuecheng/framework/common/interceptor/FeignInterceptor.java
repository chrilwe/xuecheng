package com.xuecheng.framework.common.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		Enumeration<String> attributeNames = request.getAttributeNames();
		if(attributeNames != null) {
			while(attributeNames.hasMoreElements()) {
				String headerName = attributeNames.nextElement();
				String headerValue = request.getHeader(headerName);
				//将所有的头信息向下传递
				requestTemplate.header(headerName, headerValue);
			}
		}
	}

}
