package com.xuecheng.lottery.mapper;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.lottery.XcLottery;

public interface XcLotteryMapper {
	public int add(XcLottery xcLottery);
	public int deleteById(String id);
	public int update(XcLottery xcLottery);
	public XcLottery findById(String id);
	public XcLottery findByTypeAndStatus(@Param("type")String type,@Param("status")String status);
}
