package com.xuecheng.lottery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.lottery.XcLotteryShotItems;

public interface XcLotteryShotItemsMapper {
	public int add(XcLotteryShotItems item);
	public int deletebylotteryDetailsId(String id);
	public int update(XcLotteryShotItems item);
	public XcLotteryShotItems findBylotteryDetailsId(String id);
	public List<XcLotteryShotItems> findList(@Param("userId")String userId,@Param("status")String status);
}
