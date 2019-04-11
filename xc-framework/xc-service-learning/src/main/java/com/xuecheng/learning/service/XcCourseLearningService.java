package com.xuecheng.learning.service;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.domain.media.response.GetMediaResult;

public interface XcCourseLearningService {
	public QueryResponseResult findCoursePubByCourseId(String courseId);
	public GetMediaResult getMedia(String courseId, String teachplanId);
}
