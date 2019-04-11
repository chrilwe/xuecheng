package com.xuecheng.search.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.search.dao.EsCourseDao;

@Repository
public class EsCourseDaoImpl implements EsCourseDao {

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Override
	public Map<String, CoursePub> findById(String indexName, String type, String id) {
		GetRequest getRequest = new GetRequest(indexName,type,id);
		try {
			GetResponse response = restHighLevelClient.get(getRequest );
			Map<String, Object> source = response.getSource();
			System.out.println(source);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public boolean addCoursePub(String indexName, String type, String doc_id, CoursePub coursePub) {
		// 1、创建索引请求
		IndexRequest request = this.indexRequest(indexName, type, doc_id);

		// 2、准备文档数据
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("charge", coursePub.getCharge());
		jsonMap.put("description", coursePub.getDescription());
		jsonMap.put("expires", coursePub.getExpires());
		jsonMap.put("grade", coursePub.getGrade());
		jsonMap.put("id", coursePub.getId());
		jsonMap.put("mt", coursePub.getMt());
		jsonMap.put("name", coursePub.getName());
		jsonMap.put("pic", coursePub.getPrice());
		jsonMap.put("pubTime", coursePub.getPubTime());
		jsonMap.put("qq", coursePub.getQq());
		jsonMap.put("st", coursePub.getSt());
		jsonMap.put("studyModel", coursePub.getStudymodel());
		jsonMap.put("teachMode", coursePub.getTeachmode());
		jsonMap.put("teachplan", coursePub.getTeachplan());
		jsonMap.put("timestamp", coursePub.getTimestamp());
		jsonMap.put("users", coursePub.getUsers());
		jsonMap.put("valid", coursePub.getValid());
		request.source(jsonMap);

		// 3 创建文档
		return this.createDoc(request, jsonMap);
	}


	@Override
	public boolean addTeachplanMedia(String indexName, String type, String doc_id, TeachplanMedia teachplanMedia) {
		// 1、创建索引请求
		IndexRequest request = this.indexRequest(indexName, type, doc_id);

		// 2、准备文档数据
		Map<String, Object> source = new HashMap<>();
		source.put("teachplanId", teachplanMedia.getTeachplanId());
		source.put("courseId", teachplanMedia.getCourseId());
		source.put("mediaFileOriginalName", teachplanMedia.getMediaFileOriginalName());
		source.put("mediaId", teachplanMedia.getMediaId());
		source.put("mediaUrl", teachplanMedia.getMediaUrl());
		request.source(source);

		// 3 创建文档
		boolean create = this.createDoc(request, source);

		return create;
	}

	/**
	 * 创建索引请求indexRequest
	 */
	private IndexRequest indexRequest(String indexName, String type, String doc_id) {
		// 1、创建索引请求
		IndexRequest request = new IndexRequest(indexName, // 索引
				type, // mapping type
				doc_id); // 文档id
		
		return request;
	}
	
	/**
	 * 创建索引更新请求updateRequest
	 */
	private UpdateRequest updateRequest(String indexName, String type, String doc_id) {
		return new UpdateRequest(indexName,type,doc_id);
	}

	/**
	 * 创建文档
	 */
	private boolean createDoc(IndexRequest request, Map<String, Object> source) {
		try {
			IndexResponse response = restHighLevelClient.index(request);
			System.out.print("-----------response:--"+response);
			if (response.status() == RestStatus.CREATED) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 更新文档
	 */
	private boolean updateDoc(UpdateRequest request, Map<String, Object> source) {
		try {
			UpdateResponse response = restHighLevelClient.update(request);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateCoursePub(String indexName, String type, String doc_id, CoursePub coursePub) {
		UpdateRequest request = this.updateRequest(indexName, type, doc_id);
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("charge", coursePub.getCharge());
		jsonMap.put("description", coursePub.getDescription());
		jsonMap.put("expires", coursePub.getExpires());
		jsonMap.put("grade", coursePub.getGrade());
		jsonMap.put("id", coursePub.getId());
		jsonMap.put("mt", coursePub.getMt());
		jsonMap.put("name", coursePub.getName());
		jsonMap.put("pic", coursePub.getPrice());
		jsonMap.put("pubTime", coursePub.getPubTime());
		jsonMap.put("qq", coursePub.getQq());
		jsonMap.put("st", coursePub.getSt());
		jsonMap.put("studyModel", coursePub.getStudymodel());
		jsonMap.put("teachMode", coursePub.getTeachmode());
		jsonMap.put("teachplan", coursePub.getTeachplan());
		jsonMap.put("timestamp", coursePub.getTimestamp());
		jsonMap.put("users", coursePub.getUsers());
		jsonMap.put("valid", coursePub.getValid());
		request.doc(jsonMap);
		
		return this.updateDoc(request, jsonMap);
	}

	@Override
	public boolean updateTeachplanMedia(String indexName, String type, String doc_id, TeachplanMedia teachplanMedia) {
		UpdateRequest request = this.updateRequest(indexName, type, doc_id);
		Map<String, Object> source = new HashMap<>();
		source.put("teachplanId", teachplanMedia.getTeachplanId());
		source.put("courseId", teachplanMedia.getCourseId());
		source.put("mediaFileOriginalName", teachplanMedia.getMediaFileOriginalName());
		source.put("mediaId", teachplanMedia.getMediaId());
		source.put("mediaUrl", teachplanMedia.getMediaUrl());
		request.doc(source);
		return this.updateDoc(request, source);
	}

	@Override
	public List<TeachplanMedia> findTeachplanMediaByCourseId(String indexName, 
			String type, String courseId) {
		List<TeachplanMedia> list = new ArrayList<TeachplanMedia>();
		try {
			SearchRequest searchRequest = new SearchRequest(indexName);
			searchRequest.types(type);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			MatchPhraseQueryBuilder query = this.uniqueMatchQuery("courseId", courseId);
			sourceBuilder.query(query);
			searchRequest.source(sourceBuilder);
			SearchResponse search = restHighLevelClient.search(searchRequest);
			SearchHits hits = search.getHits();
			for (SearchHit searchHit : hits) {
				Map<String, Object> source = searchHit.getSourceAsMap();
				//解析返回结果
				String teachplanId = (String) source.get("teachplanId");
				String mediaId = (String) source.get("mediaId");
				String mediaFileOriginalName = (String) source.get("mediaFileOriginalName");
				String mediaUrl = (String) source.get("mediaUrl");
				String course_id = (String) source.get("courseId");
				
				TeachplanMedia teachplanMedia = new TeachplanMedia();
				teachplanMedia.setCourseId(course_id);
				teachplanMedia.setMediaFileOriginalName(mediaFileOriginalName);
				teachplanMedia.setMediaId(mediaId);
				teachplanMedia.setMediaUrl(mediaUrl);
				teachplanMedia.setTeachplanId(teachplanId);
				list.add(teachplanMedia);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	@Override
	public List<CoursePub> findCoursePubByteachplanId(String indexName, 
			String type, String teachplanId) {
		List<CoursePub> list = new ArrayList<CoursePub>();
		try {
			SearchRequest searchRequest = new SearchRequest(indexName);
			searchRequest.types(type);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			MatchPhraseQueryBuilder query = this.uniqueMatchQuery("teachplanId", teachplanId);
			sourceBuilder.query(query);
			searchRequest.source(sourceBuilder);
			SearchResponse search = restHighLevelClient.search(searchRequest);
			SearchHits hits = search.getHits();
			for (SearchHit searchHit : hits) {
				Map<String, Object> map = searchHit.getSourceAsMap();
				CoursePub coursePub = new CoursePub();
				coursePub.setCharge((String)map.get("charge"));
				coursePub.setDescription((String)map.get("description"));
				coursePub.setExpires((String)map.get("expires"));
				coursePub.setGrade((String)map.get("grade"));
				coursePub.setId((String)map.get("id"));
				coursePub.setMt((String)map.get("mt"));
				coursePub.setName((String)map.get("name"));
				coursePub.setPic((String)map.get("pic"));
				coursePub.setPrice((Float)map.get("price"));
				coursePub.setPrice_old((Float)map.get("price_old"));
				coursePub.setPubTime((String)map.get("pubTime"));
				coursePub.setQq((String)map.get("qq"));
				coursePub.setSt((String)map.get("st"));
				coursePub.setStudymodel((String)map.get("studyModel"));
				coursePub.setTeachmode((String)map.get("teachMode"));
				coursePub.setTeachplan((String)map.get("teachplan"));
				coursePub.setTimestamp(new Date());
				coursePub.setUsers((String)map.get("users"));
				coursePub.setValid((String)map.get("valid"));
				list.add(coursePub);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	
	/**
	 * 单条件检索
	 */
	private MatchPhraseQueryBuilder uniqueMatchQuery(String fieldKey, String fieldValue) {
		MatchPhraseQueryBuilder builder = QueryBuilders.matchPhraseQuery(fieldKey, fieldValue);
		return builder;
	}
}
