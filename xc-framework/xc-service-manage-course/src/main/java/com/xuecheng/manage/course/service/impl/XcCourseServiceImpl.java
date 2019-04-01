package com.xuecheng.manage.course.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.manage.course.client.FileSystemClient;
import com.xuecheng.manage.course.mapper.CourseBaseMapper;
import com.xuecheng.manage.course.mapper.CoursePicMapper;
import com.xuecheng.manage.course.mapper.TeachplanMapper;
import com.xuecheng.manage.course.service.XcCourseService;

@Service
public class XcCourseServiceImpl implements XcCourseService {
	
	@Autowired
	private TeachplanMapper teachplanMapper;
	@Autowired
	private CourseBaseMapper courseBaseMapper;
	@Autowired
	private FileSystemClient fileSystemClient;
	@Autowired
	private CoursePicMapper coursePicMapper;
	
	@Value("${xuecheng.fastdfs.tracker_server}")
	private String trackerServer;//文件服务器URI

	@Override
	public TeachplanNode findTeachplanList(String courseId) {
		
		return teachplanMapper.findByCourseId(courseId);
	}

	@Override
	@Transactional
	public AddCourseResult addCourse(CourseBase courseBase) {
		//自动生成courseId
		String courseId = UUID.randomUUID().toString();
		courseBase.setId(courseId);
		int addCourse = courseBaseMapper.addCourse(courseBase);
		return new AddCourseResult(CommonCode.SUCCESS, courseId);
	}

	@Override
	@Transactional
	public ResponseResult addTeachplan(Teachplan teachplan) {
		//校验信息
		if(teachplan == null || StringUtils.isEmpty(teachplan.getCourseid())
				|| StringUtils.isEmpty(teachplan.getPname())) {
			ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
		}
		
		String courseId = teachplan.getCourseid();//获取课程id
		String parentId = teachplan.getParentid();//获取分类节点id
		if(StringUtils.isEmpty(parentId)) {
			//客户端没有填根节点,获取根节点信息
			parentId = this.getTeachplanRoot(courseId);			
		}
		
		//创建子节点
		Teachplan children = new Teachplan();
		children.setCourseid(courseId);
		//当前节点的级别是父类节点的下一级
		List<Teachplan> pTeachplans = teachplanMapper.findByCourseIdAndParentId(courseId, parentId);
		Teachplan t = pTeachplans.get(0);
		if(t.getGrade().equals("1")) {
			children.setGrade("2");
		} else if(t.getGrade().equals("2")) {
			children.setGrade("3");
		}
		children.setId(UUID.randomUUID().toString());
		children.setParentid(parentId);
		children.setPname(teachplan.getPname());
		children.setStatus("0");
		return new ResponseResult(CommonCode.SUCCESS);
	}
	
	/**
	 * 获取根节点id
	 * 如果有根节点，返回根节点id
	 * 没有根节点，自动创建根节点，并且返回节点id
	 * @param courseId 课程id
	 */
	private String getTeachplanRoot(String courseId) {
		//根据courseId和parentId查询课程根节点(一个courseId1级节点唯一)
		List<Teachplan> teachplans = teachplanMapper.findByCourseIdAndParentId(courseId, "0");
		String parentId = "";
		//不存在根节点，创建一个根节点
		if(teachplans == null && teachplans.size() == 0) {
			CourseBase courseBase = courseBaseMapper.findByCourseId(courseId);
			
			parentId = UUID.randomUUID().toString();
			Teachplan teachplan = new Teachplan();
			teachplan.setCourseid(courseId);
			teachplan.setGrade("1");//1级别
			teachplan.setId(parentId);
			teachplan.setParentid("0");
			teachplan.setStatus("0");//未发布状态
			teachplan.setPname(courseBase.getName());//将课程名称作为根节点名称
		}
		//存在根节点，直接将根节点id返回
		Teachplan teachplan = teachplans.get(0);
		parentId = teachplan.getId();
		return parentId;
	}

	@Override
	@Transactional
	public ResponseResult deleteTeachplanByCourseIdAndId(String courseId, String id) {
		//判断当前节点是否为叶子节点,如果是叶子节点删除叶子节点即可，如果不是叶子节点需要删除当前节点和子节点
		delete(courseId, id);
		return new ResponseResult(CommonCode.SUCCESS);
	}
	
	
	private void delete(String courseId, String id) {
		teachplanMapper.deleteByCourseIdAndId(courseId, id);
		//根据当前节点的id作为parentId查询是否有子节点
		Teachplan teachplan = teachplanMapper.findByParentId(id);
		if(teachplan == null) {
			return;
		}
		id = teachplan.getId();
		delete(courseId, id);
	}

	@Override
	@Transactional
	public UploadFileResult uploadCoursePic(MultipartFile file, String courseId) {
		//调用文件系统服务，上传文件
		String filetag = "";
		String businesskey = "";
		String metedata = "";
		UploadFileResult result = fileSystemClient.uploadFile(file, filetag, businesskey, metedata);
		FileSystem fileSystem = result.getFileSystem();
		String filePath = fileSystem.getFilePath();
		//插入CoursePic
		CoursePic coursePic = new CoursePic();
		coursePic.setCourseid(courseId);
		coursePic.setPic(trackerServer + "/" + filePath);
		coursePicMapper.addCoursePic(coursePic);
		return new UploadFileResult(CommonCode.SUCCESS,result.getFileSystem());
	}
}
