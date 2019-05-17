package com.xuecheng.manage.course.controller;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.api.XcCourseControllerApi;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.web.BaseController;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.manage.course.service.XcCourseService;

@RestController
@RequestMapping("/course")
public class XcCourseController extends BaseController implements XcCourseControllerApi {
	
	@Autowired
	private XcCourseService xcCourseService;
	
	/**
	 * 根据courseId查询三层树状课程计划
	 * @param courseId 课程id
	 */
	@GetMapping("/teachplan/list/{courseId}")
	public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
		if(StringUtils.isEmpty(courseId)) {
			ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);//课程id为空
		}
		return xcCourseService.findTeachplanList(courseId);
	}
	
	/**
	 * 添加课程
	 * @param coursebase 课程基本信息
	 */
	@Override
	@PostMapping("/coursebase/add")
	public AddCourseResult addCourse(CourseBase courseBase) {
		if(courseBase == null) {
			ExceptionCast.cast(CourseCode.COURSE_COMMIT_NULL);
		}
		//获取用户id
		String userId = "1";
		courseBase.setUserId(userId);
		AddCourseResult result = null;
		try {
			result = xcCourseService.addCourse(courseBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 添加课程计划 
	 * @param teachplan 课程计划基本信息
	 */
	@Override
	@PostMapping("/teachplan/add")
	public ResponseResult addTeachplan(Teachplan teachplan) {
		ResponseResult result = xcCourseService.addTeachplan(teachplan);
		return result;
	}
	
	/**
	 * 添加课程营销信息
	 */
	@Override
	@PostMapping("/coursemarket/add")
	public ResponseResult addCourseMarkert(CourseMarket courseMarket) {
		
		return xcCourseService.addCourseMarkert(courseMarket);
	}
	
	/**
	 * 删除课程计划
	 */
	@GetMapping("/teachplan/delete")
	public ResponseResult deleteTeachplanByCourseIdAndId(@RequestParam("courseId")String courseId, @RequestParam("id")String id) {
		
		return xcCourseService.deleteTeachplanByCourseIdAndId(courseId, id);
	}
	
	/**
	 * 上传课程图片
	 * @param file 图片文件
	 * @param courseId 课程id
	 */
	@Override
	@PostMapping("/pic/upload")
	public UploadFileResult upLoadCoursePic(MultipartFile file, String courseId) {
		//UploadFileResult result = xcCourseService.uploadCoursePic(file, courseId);
		return null;
	}
	
	/**
	 * 查询课程列表
	 */
	@Override
	@GetMapping("/list/{page}/{size}")
	public List<CourseInfo> findCourseListByUserId(@PathVariable("page")int page, 
			@PathVariable("size")int size, 
			String userId) {
	
		return xcCourseService.findCourseListByUserId(page, size, userId);
	}
	
	/**
	 * 课程计划关联媒体文件
	 */
	@Override
	@PostMapping("/savemedia")
	public ResponseResult saveMedia(TeachplanMedia teachplanMedia) {
		
		return xcCourseService.saveMedia(teachplanMedia);
	}
	
	/**
	 * 课程发布
	 * @param courseId 课程id
	 * @param templateId 页面模板id
	 */
	@Override
	@GetMapping("/createhtml/{siteId}/{courseId}/{templateId}")
	public CmsPageResult createCourseHtml(@PathVariable("courseId")String courseId,
			@PathVariable("templateId")String templateId, @PathVariable("siteId")String siteId) {
		
		return xcCourseService.createCourseHtml(courseId, templateId, siteId);
	}
	
	/**
	 * 课程详情页面预览
	 */
	@Override
	@GetMapping("/previewhtml/{siteId}/{courseId}/{templateId}")
	public void preViewCoursePage(@PathVariable("courseId")String courseId,
			@PathVariable("templateId")String templateId, @PathVariable("siteId")String siteId) {
		try {
			String html = xcCourseService.preViewCoursePage(courseId, templateId, siteId);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(html.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据课程id查询课程基本信息
	 */
	@Override
	@GetMapping("/find/coursebase/{courseId}")
	public CourseBase findCourseBaseByCourseid(@PathVariable("courseId")String courseId) {
		
		return xcCourseService.findCourseBaseByCourseId(courseId);
	}
	
	/**
	 * 根据courseId查询发布课程信息
	 */
	@Override
	@GetMapping("/find/coursePub/{courseId}")
	public CoursePub findCoursePubByCourseId(@PathVariable("courseId")String courseId) {
		
		return xcCourseService.findCoursePubByCourseId(courseId);
	}	
}
