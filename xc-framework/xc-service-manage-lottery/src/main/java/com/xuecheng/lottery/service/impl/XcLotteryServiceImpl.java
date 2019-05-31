package com.xuecheng.lottery.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.lottery.XcLottery;
import com.xuecheng.framework.domain.lottery.XcLotteryDetails;
import com.xuecheng.framework.domain.lottery.XcLotteryShotItems;
import com.xuecheng.framework.domain.lottery.XcLotteryUser;
import com.xuecheng.framework.domain.lottery.ext.LotteryDetailsType;
import com.xuecheng.framework.domain.lottery.ext.LotteryStatus;
import com.xuecheng.framework.domain.lottery.request.LotteryRequest;
import com.xuecheng.framework.domain.lottery.request.QueryLotteryDetailsRequest;
import com.xuecheng.framework.domain.lottery.response.ChooseResult;
import com.xuecheng.framework.domain.lottery.response.CreateLotteryResult;
import com.xuecheng.framework.domain.lottery.response.LotteryResponseMessage;
import com.xuecheng.framework.domain.lottery.response.QueryLotteryDetailsResult;
import com.xuecheng.framework.domain.lottery.response.QueryLotteryResult;
import com.xuecheng.framework.utils.ZkSessionUtil;
import com.xuecheng.lottery.mapper.XcLotteryDetailsMapper;
import com.xuecheng.lottery.mapper.XcLotteryMapper;
import com.xuecheng.lottery.mapper.XcLotteryShotItemsMapper;
import com.xuecheng.lottery.mapper.XcLotteryUserMapper;
import com.xuecheng.lottery.service.XcLotteryService;

@Service
public class XcLotteryServiceImpl implements XcLotteryService {
	private static final Logger log = LoggerFactory.getLogger(XcLotteryServiceImpl.class);
	@Autowired
	private XcLotteryMapper xcLotteryMapper;
	@Autowired
	private XcLotteryDetailsMapper xcLotteryDetailsMapper;
	@Autowired
	private XcLotteryUserMapper xcLotteryUserMapper;
	@Autowired
	private XcLotteryShotItemsMapper xcLotteryShotItemsMapper;

	@Value("${xuecheng.lottery.type}")
	private String TYPE;

	/**
	 * 创建抽奖项目基本信息
	 */
	@Override
	@Transactional
	public CreateLotteryResult createLottery(XcLottery xcLottery) {
		boolean result = this.validateCreateLotteryRequestParams(xcLottery);
		if (!result) {
			return new CreateLotteryResult(null, false, LotteryResponseMessage.ERRORPARAMS);
		}
		String id = xcLottery.getType();
		// 判断是否已经创建了该类型的抽奖项目
		XcLottery findById = xcLotteryMapper.findById(id);
		if (findById != null) {
			return new CreateLotteryResult(null, false, LotteryResponseMessage.LOTTERY_EXIST);
		}

		xcLottery.setId(id);
		// 未发布状态
		xcLottery.setStatus(LotteryStatus.NO);
		xcLotteryMapper.add(xcLottery);

		return new CreateLotteryResult(id, true, LotteryResponseMessage.SUCCESS);
	}

	// 校验创建抽奖参数
	protected boolean validateCreateLotteryRequestParams(XcLottery xcLottery) {
		if (xcLottery == null) {
			log.error("创建抽奖参数为空");
			return false;
		}
		int times = xcLottery.getTimes();// 一天抽奖次数
		String type = xcLottery.getType();// 抽奖类别
		if (times <= 0 || StringUtils.isEmpty(type)) {
			log.error("抽奖次数和抽奖类型不能为空");
			return false;
		}

		// 抽奖类型是否存在
		if (StringUtils.isEmpty(TYPE)) {
			log.error("配置文件中没有配置抽奖类型");
			return false;
		}
		String[] types = TYPE.split(",");
		boolean isExist = false;
		for (String t : types) {
			if (t.equals(xcLottery.getStatus())) {
				isExist = true;
			}
		}
		if (!isExist) {
			log.error("该抽奖类型不存在");
			return false;
		}
		return true;
	}

	/**
	 * 创建奖品信息
	 */
	@Override
	@Transactional
	public ResponseResult createLotteryDetails(XcLotteryDetails xcLotteryDetails) {
		boolean result = this.validateLotteryDetailsParams(xcLotteryDetails);
		if (!result) {
			return new ResponseResult(CommonCode.INVALID_PARAM);
		}
		// 校验奖品概率是否合法
		ResponseResult validateShotPrecent = this.validateShotPrecent(xcLotteryDetails.getLotteryId(),
				xcLotteryDetails.getShotPrecent());
		if (!validateShotPrecent.isSuccess()) {
			return new ResponseResult(CommonCode.INVALID_PARAM);
		}

		xcLotteryDetails.setCreateTime(new Date());
		xcLotteryDetails.setId(UUID.randomUUID().toString());
		xcLotteryDetails.setStatus(LotteryStatus.DETAIL_YES);
		xcLotteryDetailsMapper.add(xcLotteryDetails);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 校验奖品创建参数
	protected boolean validateLotteryDetailsParams(XcLotteryDetails xcLotteryDetails) {
		if (xcLotteryDetails == null) {
			log.error("创建奖品参数为空");
			return false;
		}

		String name = xcLotteryDetails.getName();
		String pic = xcLotteryDetails.getPic();
		String lotteryId = xcLotteryDetails.getLotteryId();
		String shotPrecent = xcLotteryDetails.getShotPrecent();// 抽奖概率
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pic) || StringUtils.isEmpty(lotteryId)
				|| StringUtils.isEmpty(shotPrecent)) {
			log.error("必填参数不能为空");
			return false;
		}
		return true;
	}

	// 解析字符串 ids:需要解析的字符串 split:解析的分割符号
	protected String[] splitIds(String ids, String split) {
		if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(split)) {
			return null;
		}
		String[] idsList = ids.split(split);
		return idsList;
	}

	/**
	 * 根据抽奖类型查询抽奖信息
	 */
	@Override
	public QueryLotteryResult findLotteryByType(LotteryRequest lotteryRequest) {
		// TODO Auto-generated method stub

		return null;
	}

	/**
	 * 奖品列表
	 */
	@Override
	public QueryLotteryDetailsResult lotteryDetailsList(int page, int size) {
		if (page <= 0) {
			page = 1;
		}
		if (size <= 0) {
			size = 10;
		}
		List<XcLotteryDetails> list = xcLotteryDetailsMapper.findByPage((page - 1) * size, size,
				LotteryStatus.DETAIL_YES);
		int total = xcLotteryDetailsMapper.countYes(LotteryStatus.DETAIL_YES);
		return new QueryLotteryDetailsResult(true, 0, list, LotteryResponseMessage.SUCCESS);
	}

	/**
	 * 校验奖品中奖的概率
	 */
	@Override
	public ResponseResult validateShotPrecent(String lotteryId, String shotPrecent) {
		if (StringUtils.isEmpty(lotteryId) || StringUtils.isEmpty(shotPrecent)) {
			return new ResponseResult(CommonCode.INVALID_PARAM);
		}
		// 中奖概率不能小于1%
		if (Float.parseFloat(shotPrecent) < 1f) {
			return new ResponseResult(CommonCode.INVALID_PARAM);
		}
		// 根据lotteryId查询奖品
		List<XcLotteryDetails> list = xcLotteryDetailsMapper.findByLotteryId(lotteryId, LotteryStatus.DETAIL_YES);

		// 首次添加奖品,校验通过
		if (list == null) {
			if (Float.parseFloat(shotPrecent) <= 100) {
				return new ResponseResult(CommonCode.SUCCESS);
			}
		}

		// 第二件奖品开始不能超过前面奖品剩下的百分比
		float p = 0f;
		for (XcLotteryDetails xcLotteryDetails : list) {
			String precent = xcLotteryDetails.getShotPrecent();
			float parseFloat = Float.parseFloat(precent);
			p += parseFloat;
		}
		if ((100 - p) < Float.parseFloat(shotPrecent)) {
			// 概率超过限定
			return new ResponseResult(CommonCode.INVALID_PARAM);
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}
	
	/**
	 * 我的奖品
	 */
	@Override
	public QueryLotteryDetailsResult myLotteryItems(String userId,String status) {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(status)) {
			ExceptionCast.cast(CommonCode.INVALID_PARAM);
		}
		List<XcLotteryShotItems> findList = xcLotteryShotItemsMapper.findList(userId, status);
		if(findList == null) {
			return new QueryLotteryDetailsResult(true,0,null,null);
		}
		List<XcLotteryDetails> list = null;
		for(XcLotteryShotItems item : findList) {
			String lotteryDetailsId = item.getLotteryDetailsId();
			XcLotteryDetails xcLotteryDetails = xcLotteryDetailsMapper.findById(lotteryDetailsId);
			list.add(xcLotteryDetails);
		}
		return new QueryLotteryDetailsResult(true,0,list,LotteryResponseMessage.SUCCESS);
	}
	
	/**
	 * 领取奖品
	 */
	@Override
	@Transactional
	public ResponseResult getMyLotteryItem(String lotteryDetailsId) {
		if(StringUtils.isEmpty(lotteryDetailsId)) {
			ExceptionCast.cast(CommonCode.INVALID_PARAM);
		}
		//判断该用户是否存在该奖品
		XcLotteryShotItems findBylotteryDetailsId = xcLotteryShotItemsMapper.findBylotteryDetailsId(lotteryDetailsId);
		if(findBylotteryDetailsId == null) {
			//TODO
		}
		
		//根据lotteryDetailsId查询奖品详情
		XcLotteryDetails findById = xcLotteryDetailsMapper.findById(lotteryDetailsId);
		String type = findById.getType();
		return null;
	}
	
	//按照奖品类型执行各个业务:目前只支持course和redpackage
	private boolean getByType(String type) {
		if(type.equals(LotteryDetailsType.COURSE)) {
			//TODO 自动添加到我的选课中
		} else if(type.equals(LotteryDetailsType.REDPACKAGE)) {
			//TODO 检查是否存在支付宝账号,没有需要输入支付宝账号,红包以支付宝红包方式发送给用户
		} 
		return false;
	}

}
