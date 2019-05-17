package com.xuecheng.manage.course.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

public interface XcCourseService {	
	//基本课程： 添加课程
	public AddCourseResult addCourse(CourseBase courseBase);
	//基本课程：课程列表
	public List<CourseInfo> findCourseListByUserId(int page, int size, String userId);
	
	//课程计划: 根据courseid查询教学计划tree列表
	public TeachplanNode findTeachplanList(String courseId);
	//课程计划: 添加课程计划
	public ResponseResult addTeachplan(Teachplan teachplan);
	//课程计划： 删除课程计划
	public ResponseResult deleteTeachplanByCourseIdAndId(String courseId, String id);
	//课程计划关联媒体文件
	public ResponseResult saveMedia(TeachplanMedia teachplanMedia);
	
	//课程图片：上传图片
	public UploadFileResult uploadCoursePic(MultipartFile file, String courseId);
	//课程发布
	public CmsPageResult createCourseHtml(String courseId, String templateId, String siteId);
	//课程详情页面预览
	public String preViewCoursePage(String courseId, String templateId, String siteId);
	
	//添加课程营销
	public ResponseResult addCourseMarkert(CourseMarket courseMarket);
	//根据课程id查询课程基本信息
	public CourseBase findCourseBaseByCourseId(String courseId);
	//根据课程id查询课程发布信息
	public CoursePub findCoursePubByCourseId(String courseId);
}
