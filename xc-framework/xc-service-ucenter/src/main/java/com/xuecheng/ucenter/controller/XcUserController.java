package com.xuecheng.ucenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.XcUserControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.domain.ucenter.response.UcenterCode;
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

	@Override
	@GetMapping("/findbyid")
	public XcUser findByUserId(@RequestParam("username")String userId) {
		
		return xcUserService.findByUserid(userId);
	}
	
	/**
	 * 注册
	 */
	@Override
	@PostMapping("/register")
	public ResponseResult register(XcUserExt xcUserExt) {
		
		return xcUserService.register(xcUserExt);
	}
	
	/**
	 * 手机号码校验
	 */
	@Override
	public ResponseResult checkPhone(String phone) {
		XcUser xcUser = xcUserService.findByPhone(phone);
		if(xcUser != null) {
			ExceptionCast.cast(UcenterCode.UCENTER_PHONE_ERROR);
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}
	
	/**
	 * 邮箱校验
	 */
	@Override
	public ResponseResult checkEmail(String email) {
		XcUser xcUser = xcUserService.findByEmail(email);
		if(xcUser != null) {
			ExceptionCast.cast(UcenterCode.UCENTER_EMAIL_ERROR);
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}
}
