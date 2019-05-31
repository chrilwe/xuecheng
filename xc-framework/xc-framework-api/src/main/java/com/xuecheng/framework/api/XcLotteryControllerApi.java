package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;
import com.xuecheng.framework.domain.lottery.request.LotteryPageRequest;
import com.xuecheng.framework.domain.lottery.request.LotteryRequest;
import com.xuecheng.framework.domain.lottery.request.QueryLotteryDetailsRequest;
import com.xuecheng.framework.domain.lottery.response.ChooseResult;
import com.xuecheng.framework.domain.lottery.response.CreateLotteryResult;
import com.xuecheng.framework.domain.lottery.response.QueryLotteryDetailsResult;
import com.xuecheng.framework.domain.lottery.response.QueryLotteryResult;
import com.xuecheng.framework.domain.lottery.response.ShowLotteryPageResult;

public interface XcLotteryControllerApi {
	//创建抽奖项目基本信息
	public CreateLotteryResult createLottery(XcLottery xcLottery);
	//创建奖品基本信息
	public ResponseResult createLotteryDetails(XcLotteryDetails xcLotteryDetails);
	//抽奖环节
	public ChooseResult chooseItem(LotteryRequest lotteryRequest);
	//奖品列表
	public QueryLotteryDetailsResult lotteryDetailsList(int page,int size);
	//抽奖页面预览
	public ResponseResult preViewLotteryPage(LotteryPageRequest request);
	//抽奖页面发布
	public ShowLotteryPageResult showLotteryPage(String lotteryId);
	//校验奖品中奖概率
	public ResponseResult validateShotPrecent(String lotteryId,String shotPrecent);
	//我的奖品列表 
	public QueryLotteryDetailsResult myLotteryItems(String status);
	//领取奖品
	public ResponseResult getMyLotteryItem(String lotteryDetailsId);
}
