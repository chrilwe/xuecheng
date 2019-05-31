package com.xuecheng.lottery.controller;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.XcLotteryControllerApi;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.web.BaseController;
import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;
import com.xuecheng.framework.domain.lottery.request.LotteryPageRequest;
import com.xuecheng.framework.domain.lottery.request.LotteryRequest;
import com.xuecheng.framework.domain.lottery.request.QueryLotteryDetailsRequest;
import com.xuecheng.framework.domain.lottery.response.ChooseResult;
import com.xuecheng.framework.domain.lottery.response.CreateLotteryResult;
import com.xuecheng.framework.domain.lottery.response.LotteryResponseMessage;
import com.xuecheng.framework.domain.lottery.response.QueryLotteryDetailsResult;
import com.xuecheng.framework.domain.lottery.response.ShowLotteryPageResult;
import com.xuecheng.framework.utils.ZkSessionUtil;
import com.xuecheng.lottery.service.XcLotteryChooseService;
import com.xuecheng.lottery.service.XcLotteryPageService;
import com.xuecheng.lottery.service.XcLotteryService;

@RestController
@RequestMapping("/lottery")
public class XcLotteryController extends BaseController implements XcLotteryControllerApi {
	
	@Autowired
	private XcLotteryService xcLotteryService;
	@Autowired
	private XcLotteryPageService xcLotteryPageService;
	@Autowired
	private XcLotteryChooseService xcLotteryChooseService;
	
	/**
	 * 创建抽奖项目
	 */
	@Override
	@PostMapping("/addLottery")
	public CreateLotteryResult createLottery(XcLottery xcLottery) {
		CreateLotteryResult createLottery = xcLotteryService.createLottery(xcLottery);
		return createLottery;
	}
	
	/**
	 * 创建奖品
	 */
	@Override
	@PostMapping("/addLotteryDetails")
	public ResponseResult createLotteryDetails(XcLotteryDetails xcLotteryDetails) {
		ResponseResult createLotteryDetails = xcLotteryService.createLotteryDetails(xcLotteryDetails);
		return createLotteryDetails;
	}
	
	
	/**
	 * 抽奖环节
	 */
	@Override
	@GetMapping("/choose")
	public ChooseResult chooseItem(LotteryRequest lotteryRequest) {
		String userId = "";
		ChooseResult chooseItem = xcLotteryChooseService.chooseItem(lotteryRequest,userId);
		return chooseItem;
	}
	
	/**
	 * 奖品列表
	 */
	@Override
	@GetMapping("/details/list/{page}/{size}")
	public QueryLotteryDetailsResult lotteryDetailsList(@PathVariable("page")int page,@PathVariable("size")int size) {
		QueryLotteryDetailsResult lotteryDetailsList = xcLotteryService.lotteryDetailsList(page, size);
		return lotteryDetailsList;
	}
	
	/**
	 * 抽奖页面预览
	 */
	@Override
	@GetMapping("/preView")
	public ResponseResult preViewLotteryPage(LotteryPageRequest pageRequest) {
		String html = xcLotteryPageService.preViewLotteryPage(pageRequest);
		if(!StringUtils.isEmpty(html)) {
			try {
				response.getOutputStream().write(html.getBytes("utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 抽奖页面发布
	 */
	@Override
	@GetMapping("/showLotteryPage/{lotteryId}")
	public ShowLotteryPageResult showLotteryPage(@PathVariable("lotteryId")String lotteryId) {
		ShowLotteryPageResult result = xcLotteryPageService.showLotteryPage(lotteryId);
		return result;
	}
	
	/**
	 * 校验概率是否合法(每次输入概率，异步请求验证)
	 */
	@Override
	@GetMapping("/validate/shotPrecent")
	public ResponseResult validateShotPrecent(String lotteryId, String shotPrecent) {
		ResponseResult result = xcLotteryService.validateShotPrecent(lotteryId, shotPrecent);
		return result;
	}
	
	/**
	 * 我的抽奖奖品列表
	 */
	@Override
	@GetMapping("/myLotteryItems")
	public QueryLotteryDetailsResult myLotteryItems(String status) {
		String userId = "";
		QueryLotteryDetailsResult myLotteryItems = xcLotteryService.myLotteryItems(userId, status);
		return myLotteryItems;
	}
	
	/**
	 * 领取奖品
	 */
	@Override
	@GetMapping("/getMyLotteryItem/{lotteryDetailsId}")
	public ResponseResult getMyLotteryItem(@PathVariable("lotteryDetailsId")String lotteryDetailsId) {
		ResponseResult myLotteryItem = xcLotteryService.getMyLotteryItem(lotteryDetailsId);
		return myLotteryItem;
	}

}
