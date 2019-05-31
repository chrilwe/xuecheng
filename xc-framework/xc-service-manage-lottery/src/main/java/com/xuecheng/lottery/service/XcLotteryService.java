package com.xuecheng.lottery.service;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;
import com.xuecheng.framework.domain.lottery.request.LotteryRequest;
import com.xuecheng.framework.domain.lottery.request.QueryLotteryDetailsRequest;
import com.xuecheng.framework.domain.lottery.response.ChooseResult;
import com.xuecheng.framework.domain.lottery.response.CreateLotteryResult;
import com.xuecheng.framework.domain.lottery.response.QueryLotteryDetailsResult;
import com.xuecheng.framework.domain.lottery.response.QueryLotteryResult;
import com.xuecheng.framework.domain.lottery.XcLottery;
public interface XcLotteryService {
	//创建抽奖项目
	public CreateLotteryResult createLottery(XcLottery xcLottery);
	//创建奖品
	public ResponseResult createLotteryDetails(XcLotteryDetails xcLotteryDetails);
	//根据抽奖项目类型查询抽奖信息
	public QueryLotteryResult findLotteryByType(LotteryRequest lotteryRequest);
	//奖品列表
	public QueryLotteryDetailsResult lotteryDetailsList(int page,int size);
	//校验奖品中奖概率
	public ResponseResult validateShotPrecent(String lotteryId,String shotPrecent);
	//我的奖品列表 
	public QueryLotteryDetailsResult myLotteryItems(String userId,String status);
	//领取奖品
	public ResponseResult getMyLotteryItem(String lotteryDetailsId);
}
