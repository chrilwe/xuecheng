package com.xuecheng.lottery.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;
import com.xuecheng.framework.domain.lottery.XcLotteryShotItems;
import com.xuecheng.framework.domain.lottery.XcLotteryUser;
import com.xuecheng.framework.domain.lottery.ext.LotteryStatus;
import com.xuecheng.framework.domain.lottery.request.LotteryRequest;
import com.xuecheng.framework.domain.lottery.response.ChooseResult;
import com.xuecheng.framework.domain.lottery.response.LotteryResponseMessage;
import com.xuecheng.framework.utils.ZkSessionUtil;
import com.xuecheng.lottery.mapper.XcLotteryDetailsMapper;
import com.xuecheng.lottery.mapper.XcLotteryMapper;
import com.xuecheng.lottery.mapper.XcLotteryShotItemsMapper;
import com.xuecheng.lottery.mapper.XcLotteryUserMapper;
import com.xuecheng.lottery.service.XcLotteryChooseService;

@Service
public class XcLotteryChooseServiceImpl implements XcLotteryChooseService {
	
	@Autowired
	private XcLotteryMapper xcLotteryMapper;
	@Autowired
	private XcLotteryDetailsMapper xcLotteryDetailsMapper;
	@Autowired
	private XcLotteryUserMapper xcLotteryUserMapper;
	@Autowired
	private XcLotteryShotItemsMapper xcLotteryShotItemsMapper;

	/**
	 * 抽奖环节
	 */
	@Override
	@Transactional
	public ChooseResult chooseItem(LotteryRequest lotteryRequest, String userId) {
		// 加锁：在某一时刻只允许一条请求到达,其他请求驳回
		ZkSessionUtil zkUtil = ZkSessionUtil.getInstance();
		boolean createNode = zkUtil.createNode("/" + userId, "");
		if (!createNode) {
			return new ChooseResult(null, false, LotteryResponseMessage.LOTTERY_ISCHOOSING, null);
		}
		if (lotteryRequest == null || StringUtils.isEmpty(userId)) {
			return new ChooseResult(null, false, LotteryResponseMessage.ERRORPARAMS, null);
		}
		String lotteryId = lotteryRequest.getLotteryId();
		if (StringUtils.isEmpty(lotteryId)) {
			return new ChooseResult(lotteryId, false, LotteryResponseMessage.ERRORPARAMS, null);
		}

		// 判断当前抽奖是否有效
		XcLottery xcLottery = xcLotteryMapper.findById(lotteryId);
		String status = xcLottery.getStatus();
		if (!status.equals(LotteryStatus.YES)) {
			return new ChooseResult(lotteryId, false, LotteryResponseMessage.LOTTERY_ERROR, null);
		}

		// 判断用户是否当天抽奖次数已经用完
		XcLotteryUser xcLotteryUser = xcLotteryUserMapper.findByUserIdAndLotteryId(userId, lotteryId);
		if (xcLotteryUser != null) {
			int times = xcLotteryUser.getTimes();
			if (times >= xcLottery.getTimes()) {
				return new ChooseResult(lotteryId, false, LotteryResponseMessage.LOTTERY_TIMES_USED, null);
			}
		}

		// 根据lotteryId查询该抽奖类型的奖品
		List<XcLotteryDetails> list = xcLotteryDetailsMapper.findByLotteryId(lotteryId, LotteryStatus.DETAIL_YES);
		if (list == null) {
			return new ChooseResult(null, false, LotteryResponseMessage.LOTTERYDETAILS_NULL, null);
		}

		// 对奖品根据抽奖概率分配随机数
		Map<String, String> map = this.lotteryRandomNumber(list);
		// 产生随机数
		Random random = new Random();
		int randomNum = random.nextInt(100);
		if (randomNum <= 0) {
			randomNum = random.nextInt(100);
		}
		// 随机数所对应的奖品
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String string = map.get(key);
			String[] split = string.split(",");
			int firstIndex = Integer.parseInt(split[0]);
			int lastIndex = Integer.parseInt(split[1]);
			
			if (randomNum >= firstIndex && randomNum <= lastIndex) {
				//生成用户抽奖记录
				if(xcLotteryUser == null) {
					XcLotteryUser lotteryUser = new XcLotteryUser();
					lotteryUser.setLotteryDetailsId(key);
					lotteryUser.setLotteryId(lotteryId);
					lotteryUser.setLotteryTime(new Date());
					lotteryUser.setTimes(xcLottery.getTimes() - 1);
					lotteryUser.setUserId(userId);
					xcLotteryUserMapper.add(xcLotteryUser);
				} else {
					xcLotteryUser.setTimes(xcLotteryUser.getTimes() - 1);
					xcLotteryUserMapper.update(xcLotteryUser);
				}
				if(!key.equals("noShot")) {
					//添加到我的奖品
					XcLotteryShotItems item = new XcLotteryShotItems();
					item.setLotteryDetailsId(key);
					item.setLotteryId(lotteryId);
					item.setStatus(LotteryStatus.DETAIL_YES);
					item.setUserId(userId);
					xcLotteryShotItemsMapper.add(item);
				}
				//解锁
				boolean deleteNode = zkUtil.deleteNode("/"+userId);
				if(!deleteNode) {
					ExceptionCast.cast(CommonCode.FAIL);
				}
				return new ChooseResult(lotteryId, true, LotteryResponseMessage.SUCCESS, key);
			}
		}
		return new ChooseResult(lotteryId, false, LotteryResponseMessage.LOTTERY_ERROR, null);
	}

	// 为奖品分配生成随机数
	private Map<String, String> lotteryRandomNumber(List<XcLotteryDetails> list) {
		//中奖分配
		int lastIndex = 0;
		Map<String, String> map = null;
		for (XcLotteryDetails xcLotteryDetails : list) {
			String shotPrecent = xcLotteryDetails.getShotPrecent();
			String id = xcLotteryDetails.getId();
			int parseInt = Integer.parseInt(shotPrecent);
			int firstIndex = lastIndex + 1;
			lastIndex += parseInt;

			map.put(id, firstIndex + "," + lastIndex);
		}
		//不中奖的分配
		map.put("noShot", (100 - lastIndex) + "");
		return map;
	}

}
