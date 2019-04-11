package com.xuecheng.framework.api;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

public interface XcCourseControllerApi {
	//我的课程列表
	public List<CourseInfo> findCourseListByUserId(int page, int size, String userId);
	//添加课程
	public AddCourseResult addCourse(CourseBase courseBase);
	//根据课程id查询课程计划
	public TeachplanNode findTeachplanList(String courseId);
	//添加课程计划
	public ResponseResult addTeachplan(Teachplan teachplan);
	//删除课程计划
	public ResponseResult deleteTeachplanByCourseIdAndId(String courseId, String id);
	//上传课程图片
	public UploadFileResult upLoadCoursePic(MultipartFile file, String courseId);
	//课程计划关联媒体资源
	public ResponseResult saveMedia(TeachplanMedia teachplanMedia);
	//发布课程
	public CmsPageResult createCourseHtml(String courseId);
	 
}
