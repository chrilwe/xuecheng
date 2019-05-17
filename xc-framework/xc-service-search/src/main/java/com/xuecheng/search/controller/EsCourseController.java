package com.xuecheng.search.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuecheng.framework.api.EsCourseControllerApi;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.response.GetMediaResult;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.search.service.EsCourseService;

@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {
	
	@Autowired
	private EsCourseService esCourseService;
	
	/**
	 * 根据课程id查询所有教学计划
	 */
	@Override
	@GetMapping("/getall/{id}")
	public Map<String, CoursePub> getall(@PathVariable("id")String id) {
		Map<String, CoursePub> getall = esCourseService.getall(id);
		return getall;
	}
	
	/**
	 * 按照条件查询
	 */
	@Override
	@GetMapping(value="/list/{page}/{size}")
	public QueryResponseResult list(@PathVariable("page")int page, 
			@PathVariable("size")int size, CourseSearchParam courseSearchParam) {
		QueryResponseResult result = esCourseService.list(page, size, courseSearchParam);
		return result;
	}
	
	/**
	 * 添加课程信息
	 */
	@Override
	@PostMapping("/add")
	public ResponseResult addCourseToEs(CoursePub coursePub) {
		/*
		 * 测试假数据
		 *
		CoursePub cp = new CoursePub();
		cp.setId("1");
		cp.setName("test_coursePub");
		TeachplanMedia tm = new TeachplanMedia();
		tm.setCourseId("1");
		tm.setMediaFileOriginalName("test_teachplanMedia");
		tm.setMediaId("2");
		tm.setMediaUrl("http://");
		tm.setTeachplanId("1");*/
		return esCourseService.addCourseToEs(coursePub);
	}
	
	/**
	 * 根据课程id查询coursePub
	 */
	@Override
	@GetMapping("/coursePub/{courseId}")
	public QueryResponseResult findCoursePubByCourseId(@PathVariable("courseId")String courseId) {
		
		return esCourseService.findCoursePubByCourseId(courseId);
	}
	
	/**
	 * 根据courseId和teachplanId查询文件访问地址
	 */
	@Override
	@GetMapping("/getMedia/{courseId}/{teachplanId}")
	public GetMediaResult getMedia(@PathVariable("courseId")String courseId, 
			@PathVariable("teachplanId")String teachplanId) {
		
		return null;
	}
	
}
