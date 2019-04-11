package com.xuecheng.framework.api;

import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.response.GetMediaResult;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * Created by Administrator.
 */
//@Api(value = "课程搜索",description = "课程搜索",tags = {"课程搜索"})
public interface EsCourseControllerApi {
    //搜索课程信息
    //@ApiOperation("课程综合搜索")
    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam);

    //@ApiOperation("根据课程id查询课程信息")
    public Map<String,CoursePub> getall(String id);

    //@ApiOperation("根据课程计划id查询课程媒资信息")
    //public TeachplanMediaPub getmedia(String id);
    
    //添加课程和媒体信息到搜索库中
    public ResponseResult addCourseToEs(CoursePub coursePub, TeachplanMedia teachplanMedia);
    
    //根据课程id查询coursepub
    public QueryResponseResult findCoursePubByCourseId(String courseId);
    
    //获取媒体文件地址
    public GetMediaResult getMedia(String courseId, String teachplanId);
}
