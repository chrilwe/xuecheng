package com.xuecheng.center.order.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.order.response.CreateOrderResult;

@RestController
public class TestController {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@GetMapping("/test/order/create")
	public CreateOrderResult createOrder() {
		ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_CENTER_ORDER);
		URI uri = choose.getUri();
		String url = uri + "/order/create";
		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("courseId", "11611f8f-2bdd-4783-9b08-092569dd8fcc");
		
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);
		ResponseEntity<CreateOrderResult> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				CreateOrderResult.class);
		return response.getBody();
	}
}
