package com.xuecheng.manage.course.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;

public interface TeachplanMapper {
	//根据课程id查询课程计划
	public TeachplanNode findByCourseId(String courseId);
	//插入课程计划
	public int addTeachplan(Teachplan teachplan);
	//根据courseId和parentId查询根节点
	public List<Teachplan> findByCourseIdAndParentId(@Param("courseId")String courseId, @Param("parentId")String parentId);
	//根据courseId和id删除节点
	public int deleteByCourseIdAndId(@Param("courseId")String courseId, @Param("id")String id);
	//根据parentid查询课程计划
	public Teachplan findByParentId(String parentId);
	//根据id查询
	public Teachplan findById(String id);
	//根据课程id查询
	public List<Teachplan> findTeachplanByCourseId(String courseId);
}
