package com.xuecheng.lottery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;

public interface XcLotteryDetailsMapper {
	public int add(XcLotteryDetails xcLotteryDetails);
	public int deleteById(String id);
	public int update(XcLotteryDetails xcLotteryDetails);
	public XcLotteryDetails findById(String id);
	public List<XcLotteryDetails> findAll();
	public List<XcLotteryDetails> findByPage(@Param("start")int start,@Param("size")int size,@Param("status")String status);
	public int countYes(String status);
	public List<XcLotteryDetails> findByLotteryId(@Param("lotteryId")String lotteryId,@Param("status")String status);
}
