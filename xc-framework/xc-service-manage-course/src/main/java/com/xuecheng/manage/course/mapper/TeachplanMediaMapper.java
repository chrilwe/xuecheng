package com.xuecheng.manage.course.mapper;

import java.util.List;

import com.xuecheng.framework.domain.course.TeachplanMedia;

public interface TeachplanMediaMapper {
	//插入teachplanMedia
	public int addTeachplanMedia(TeachplanMedia teachplanMedia);
	//根据teachplanid查询
	public TeachplanMedia findByTeachplanId(String teachplanid);
	//更新
	public int updateTeachplanMedia(TeachplanMedia teachplanMedia);
	//根据课程id查询所有的TeachplanMedia
	public List<TeachplanMedia> findListByCourseId(String courseId);
}
