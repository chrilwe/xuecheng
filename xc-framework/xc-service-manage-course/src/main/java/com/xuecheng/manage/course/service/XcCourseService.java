package com.xuecheng.manage.course.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

public interface XcCourseService {	
	//基本课程： 添加课程
	public AddCourseResult addCourse(CourseBase courseBase);
	
	//课程计划: 根据courseid查询教学计划tree列表
	public TeachplanNode findTeachplanList(String courseId);
	//课程计划: 添加课程计划
	public ResponseResult addTeachplan(Teachplan teachplan);
	//课程计划： 删除课程计划
	public ResponseResult deleteTeachplanByCourseIdAndId(String courseId, String id);
	
	//课程图片：上传图片
	public UploadFileResult uploadCoursePic(MultipartFile file, String courseId);
}
