package com.xuecheng.framework.domain.lottery.response;

public interface LotteryResponseMessage {
	public static final String SUCCESS = "请求成功!!";
	public static final String ERRORPARAMS = "错误的请求参数！！！";
	public static final String LOTTERY_EXIST = "已经存在该抽奖项目！！！";
	public static final String PLEASE_PREVIEWPAGE = "请先预览页面！！！";
	public static final String LOTTERY_ERROR = "无效的抽奖！！！";
	public static final String LOTTERYDETAILS_NULL = "抽奖奖品还没有添加！！！";
	public static final String LOTTERY_OUTINDEX = "产生的随机数不在所分配的区间范围！！！";
	public static final String LOTTERY_TIMES_USED = "抽奖次数已经用完！！！";
	public static final String LOTTERY_ISCHOOSING = "抽奖正在进行中，请等抽奖结束后再进行抽奖！！！";
}
