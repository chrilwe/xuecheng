package com.xuecheng.learning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.domain.media.response.GetMediaResult;
import com.xuecheng.learning.client.EsCourseClient;
import com.xuecheng.learning.service.XcCourseLearningService;

@Service
public class XcCourseLearningServiceImpl implements XcCourseLearningService {
	
	@Autowired
	private EsCourseClient esCourseClient;

	@Override
	public QueryResponseResult findCoursePubByCourseId(String courseId) {
		if(StringUtils.isEmpty(courseId)) {
			ExceptionCast.cast(CommonCode.INVALID_PARAM);
		}
		return esCourseClient.findCoursePubByCourseId(courseId);
	}

	@Override
	public GetMediaResult getMedia(String courseId, String teachplanId) {
		if(StringUtils.isEmpty(courseId) || StringUtils.isEmpty(teachplanId)) {
			ExceptionCast.cast(CommonCode.INVALID_PARAM);
		}
		return esCourseClient.getMedia(courseId, teachplanId);
	}
	
}
