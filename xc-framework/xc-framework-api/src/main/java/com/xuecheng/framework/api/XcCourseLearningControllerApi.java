package com.xuecheng.framework.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.media.response.GetMediaResult;

public interface XcCourseLearningControllerApi {
	//根据coourseId和teachplanId查询媒体文件的地址
	public GetMediaResult getMedia(String courseId, String teachplanId);
	//根据课程id查询coursePub
	public QueryResponseResult findCoursePubByCourseId(String courseId);
	//添加我的课程
	public ResponseResult addLearningCourse(HttpServletRequest request, XcLearningCourse xcLearningCourse);
}
