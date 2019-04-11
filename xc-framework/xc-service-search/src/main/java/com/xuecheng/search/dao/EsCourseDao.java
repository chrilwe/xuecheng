package com.xuecheng.search.dao;

import java.util.List;
import java.util.Map;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;

public interface EsCourseDao {
	//根据课程id查询
	public Map<String, CoursePub> findById(String indexName, String type, String id);
	//插入coursePub
	public boolean addCoursePub(String indexName, 
			String type, String doc_id, CoursePub coursePub);
	//插入teachplanMedia
	public boolean addTeachplanMedia(String indexName, 
			String type, String doc_id, TeachplanMedia teachplanMedia);
	//更新coursePub
	public boolean updateCoursePub(String indexName, 
			String type, String doc_id, CoursePub coursePub);
	//更新teachplanMedia
	public boolean updateTeachplanMedia(String indexName, 
			String type, String doc_id, TeachplanMedia teachplanMedia);
	//根据课程courseId查询TeachplanMedia
	public List<TeachplanMedia> findTeachplanMediaByCourseId(String indexName, String type, String courseId);
	//根据teachplanId批量查询coursePub
	public List<CoursePub> findCoursePubByteachplanId(String indexName, String type, String teachplanId);
}
