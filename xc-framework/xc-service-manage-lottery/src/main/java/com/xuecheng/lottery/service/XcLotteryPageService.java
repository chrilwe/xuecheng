package com.xuecheng.lottery.service;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.lottery.request.LotteryPageRequest;
import com.xuecheng.framework.domain.lottery.response.ShowLotteryPageResult;

public interface XcLotteryPageService {
	//抽奖页面预览
	public String preViewLotteryPage(LotteryPageRequest request);
	//抽奖页面发布
	public ShowLotteryPageResult showLotteryPage(String lotteryId);
}
