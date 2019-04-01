package com.xuecheng.ucenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.XcUserControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.XcUserService;

@RestController
@RequestMapping("/ucenter")
public class XcUserController implements XcUserControllerApi {
	
	@Autowired
	private XcUserService xcUserService;
	
	/**
	 * 根据账号查询
	 * @param username
	 * @return
	 */
	@GetMapping("/getuserext")
	public XcUserExt findByUserName(@RequestParam("username") String username) {
		XcUserExt xcUserExt = xcUserService.findByUserName(username);
		return xcUserExt;
	}
	
	/**
	 * 根据账号查询用户权限
	 */
	@Override
	@GetMapping("/getmenuext")
	public XcMenuExt findXcMenuExtByUserName(@RequestParam("username") String username) {
		XcMenuExt xcMenuExt = xcUserService.findXcMenuExtByUserName(username);
		return xcMenuExt;
	}
}
