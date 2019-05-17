package com.xuecheng.search.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.QueryResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.search.dao.EsCourseDao;
import com.xuecheng.search.service.EsCourseService;

@Service
public class EsCourseServiceImpl implements EsCourseService {
	
	@Value("${xuecheng.course.index}")
	private String xc_course_index;
	@Value("${xuecheng.course.type}")
	private String xc_course_type;
	@Value("${xuecheng.course.source_field}")
	private String xc_course_source_field;
	@Value("${xuecheng.media.index}")
	private String xc_media_index;
	@Value("${xuecheng.media.type}")
	private String xc_media_type;
	@Value("${xuecheng.media.source_field}")
	private String xc_media_source_field;
	
	@Autowired
	private EsCourseDao esCourseDao;

	@Override
	public Map<String, CoursePub> getall(String id) {
		//先从es库中根据courseId查询TeacnplanMedia
		//批量根据teachplanId查询CoursePub
		return esCourseDao.findById(xc_course_index, xc_course_type, id);
	}


	@Override
	public ResponseResult addCourseToEs(CoursePub coursePub) {
		if(coursePub == null) {
			return new ResponseResult(CommonCode.FAIL);
		}
		boolean addCoursePub = esCourseDao.addCoursePub(xc_course_index, xc_course_type, coursePub.getId(), coursePub);
	
		return new ResponseResult(CommonCode.FAIL);
	}


	@Override
	public QueryResponseResult findCoursePubByCourseId(String courseId) {
		if(StringUtils.isEmpty(courseId)) {
			ExceptionCast.cast(CommonCode.INVALID_PARAM);
		}
		List<CoursePub> coursePubList = new ArrayList<CoursePub>();
		//根据courseId查询teachplanMedia
		List<TeachplanMedia> teachplanMediaList = esCourseDao.findTeachplanMediaByCourseId(courseId, xc_media_type, courseId);
		//根据teachplanId查询coursePub
		for(TeachplanMedia teachplanMedia : teachplanMediaList) {
			List<CoursePub> list = esCourseDao.findCoursePubByteachplanId(courseId, xc_course_type, teachplanMedia.getTeachplanId());
			for (CoursePub coursePub : list) {
				coursePubList.add(coursePub);
			}
		}
		QueryResult<CoursePub> queryResult = new QueryResult<CoursePub>();
		queryResult.setList(coursePubList);
		queryResult.setTotal(coursePubList.size());
		return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
	}


	@Override
	public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) {
		if(courseSearchParam == null) {
			courseSearchParam = new CourseSearchParam();
		}
		
		SearchRequest searchRequest = new SearchRequest(xc_course_index);
		searchRequest.types(xc_course_type);
		//过滤源字段
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.fetchSource(xc_course_source_field.split(","), new String[]{});
		
		//创建布尔查询对象
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		//根据关键字搜索
		if(StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
			MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(
					courseSearchParam.getKeyword(), "name", "description", "teachplan")
					.minimumShouldMatch("70%")
					.field("name", 10);
			booleanQueryBuilder.must(multiMatchQuery);
		}
		
		//一级分类搜索
		if(StringUtils.isNotEmpty(courseSearchParam.getMt())) {
			booleanQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
		}
		
		//二级分类搜索
		if(StringUtils.isNotEmpty(courseSearchParam.getSt())) {
			booleanQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
		}
		
		//难度等级搜索
		if(StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
			booleanQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
		}
		searchSourceBuilder.query(booleanQueryBuilder);
		
		//分页条件
		if(page <= 0) {
			page = 1;
		}
		int from = (page - 1) * size;
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);
		
		//设置高亮
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<font class='eslight'>");
		highlightBuilder.postTags("</font>");
		//高亮字段
		highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
		searchSourceBuilder.highlighter(highlightBuilder);
		
		//组装条件,执行查询
		searchRequest.source(searchSourceBuilder);
		QueryResponseResult result = esCourseDao.queryByKeyWords(searchRequest);
		return result;
	}
	
}
