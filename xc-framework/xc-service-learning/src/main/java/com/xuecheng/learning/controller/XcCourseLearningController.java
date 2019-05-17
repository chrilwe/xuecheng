package com.xuecheng.learning.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.XcCourseLearningControllerApi;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.media.response.GetMediaResult;
import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.utils.XcOauth2Util.UserJwt;
import com.xuecheng.learning.service.XcCourseLearningService;

@RestController
@RequestMapping("/learning/course")
public class XcCourseLearningController implements XcCourseLearningControllerApi {

	@Autowired
	private XcCourseLearningService xcCourseLearningService;

	/**
	 * 获取媒体文件地址
	 */
	@Override
	@GetMapping("/getmedia/{courseId}/{teachplanId}")
	public GetMediaResult getMedia(@PathVariable("courseId") String courseId,
			@PathVariable("teachplanId") String teachplanId) {

		return xcCourseLearningService.getMedia(courseId, teachplanId);
	}

	/**
	 * 根据课程id查询coursePub
	 */
	@Override
	@GetMapping("/list/{courseId}")
	public QueryResponseResult findCoursePubByCourseId(@PathVariable("courseId") String courseId) {

		return xcCourseLearningService.findCoursePubByCourseId(courseId);
	}

	/**
	 * 添加我的课程
	 */
	@Override
	@PostMapping("/add")
	public ResponseResult addLearningCourse(HttpServletRequest request, XcLearningCourse xcLearningCourse) {
		//从请求头取出UserJwt令牌信息
		/*XcOauth2Util oauth2Util = new XcOauth2Util();
		UserJwt userJwt = oauth2Util.getUserJwtFromHeader(request);
		String userId = userJwt.getId();
		xcLearningCourse.setUserId(userId);*/
		return xcCourseLearningService.addXcLearningCourse(xcLearningCourse);
	}

}
