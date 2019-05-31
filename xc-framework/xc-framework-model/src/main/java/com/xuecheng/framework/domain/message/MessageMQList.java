package com.xuecheng.framework.domain.message;

public interface MessageMQList {
	//交换机
	public static final String EXCHANGE_TOPIC_INFORM = "exchange_topic_inform";
	
	//队列
	public static final String QUEUE_INFORM_ADDCOURSETOES = "queue_inform_addCourseToEs";//发布课程，自动将课程信息添加到es
	public static final String QUEUE_INFORM_AUTOCHOOSECOURSE = "queue_inform_autoChooseCourse";//支付成功，自动选择课程
	
	//路由key
	public static final String ROUTINGKEY_ADDCOURSETOES = "routingkey_addCourseToEs";
	public static final String ROUTINGKEY_AUTOCHOOSECOURSE = "routingkey_autoChooseCourse";
}
