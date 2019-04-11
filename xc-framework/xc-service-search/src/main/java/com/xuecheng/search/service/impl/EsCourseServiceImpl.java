package com.xuecheng.search.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.QueryResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.search.dao.EsCourseDao;
import com.xuecheng.search.service.EsCourseService;

@Service
public class EsCourseServiceImpl implements EsCourseService {
	
	private static final String indexName = "xuecheng";
	private static final String course_pub_type = "course_pub";
	private static final String teachplan_media_type = "teachplan_media";
	
	@Autowired
	private EsCourseDao esCourseDao;

	@Override
	public Map<String, CoursePub> getall(String id) {
		//先从es库中根据courseId查询TeacnplanMedia
		//批量根据teachplanId查询CoursePub
		return esCourseDao.findById(indexName, course_pub_type, id);
	}


	@Override
	public ResponseResult addCourseToEs(CoursePub coursePub, TeachplanMedia teachplanMedia) {
		if(coursePub == null || teachplanMedia == null) {
			return new ResponseResult(CommonCode.FAIL);
		}
		boolean addCoursePub = esCourseDao.addCoursePub(indexName, course_pub_type, teachplanMedia.getTeachplanId(), coursePub);
		if(addCoursePub) {
			esCourseDao.addTeachplanMedia(indexName, teachplan_media_type, teachplanMedia.getTeachplanId(), teachplanMedia);
			return new ResponseResult(CommonCode.SUCCESS);
		}
	
		return new ResponseResult(CommonCode.FAIL);
	}


	@Override
	public QueryResponseResult findCoursePubByCourseId(String courseId) {
		if(StringUtils.isEmpty(courseId)) {
			ExceptionCast.cast(CommonCode.INVALID_PARAM);
		}
		List<CoursePub> coursePubList = new ArrayList<CoursePub>();
		//根据courseId查询teachplanMedia
		List<TeachplanMedia> teachplanMediaList = esCourseDao.findTeachplanMediaByCourseId(courseId, teachplan_media_type, courseId);
		//根据teachplanId查询coursePub
		for(TeachplanMedia teachplanMedia : teachplanMediaList) {
			List<CoursePub> list = esCourseDao.findCoursePubByteachplanId(courseId, course_pub_type, teachplanMedia.getTeachplanId());
			for (CoursePub coursePub : list) {
				coursePubList.add(coursePub);
			}
		}
		QueryResult<CoursePub> queryResult = new QueryResult<CoursePub>();
		queryResult.setList(coursePubList);
		queryResult.setTotal(coursePubList.size());
		return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
	}


}
