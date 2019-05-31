package com.xuecheng.lottery.service;

import com.xuecheng.framework.domain.lottery.request.LotteryRequest;
import com.xuecheng.framework.domain.lottery.response.ChooseResult;

public interface XcLotteryChooseService {
	//抽奖环节
	public ChooseResult chooseItem(LotteryRequest lotteryRequest,String userId);
}
