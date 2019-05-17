package com.xuecheng.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.XcUser;
/**
 * 远程调用用户中心客户端
 * @author Administrator
 *
 */
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
@FeignClient(value=XcServiceList.XC_SERVICE_UCENTER)
public interface XcUserClient {
	
	//根据用户名查询xcuserext
	@GetMapping("/ucenter/getuserext")
	public XcUserExt findByUserName(@RequestParam("username") String username);
	
	@GetMapping("/findbyid")
	public XcUser findByUserId(String userId);
	
	/**
	 * 注册
	 */
	@PostMapping("/register")
	public ResponseResult register(XcUser xcUser);
}
