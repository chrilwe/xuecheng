package com.xuecheng.manage.course.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;

public interface CourseBaseMapper {
	//插入课程基本信息
	public int addCourse(CourseBase courseBase);
	//根据courseid查询课程
	public CourseBase findByCourseId(String courseId);
	//根据用户id查询课程列表
	public List<CourseInfo> findByUserId(@Param("begin")int begin, @Param("size")int size, @Param("userId")String userId);
	//更新课程上线状态
	public int updateCourseStatusById(@Param("status")String status, @Param("id")String id);
}
