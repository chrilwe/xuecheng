package com.xuecheng.lottery.mapper;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryUser;

public interface XcLotteryUserMapper {
	public int add(XcLotteryUser xcLotteryUser);
	public int deleteById(String userId);
	public int update(XcLotteryUser xcLotteryUser);
	public XcLotteryUser findByUserId(String userId);
	public XcLotteryUser findByUserIdAndLotteryId(@Param("userId")String userId,@Param("lotteryId")String lotteryId);
}
