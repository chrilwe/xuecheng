package com.xuecheng.framework.api;

import java.util.List;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.response.GetMediaResult;

public interface XcCourseLearningControllerApi {
	//根据coourseId和teachplanId查询媒体文件的地址
	public GetMediaResult getMedia(String courseId, String teachplanId);
	//根据课程id查询coursePub
	public QueryResponseResult findCoursePubByCourseId(String courseId);
}
