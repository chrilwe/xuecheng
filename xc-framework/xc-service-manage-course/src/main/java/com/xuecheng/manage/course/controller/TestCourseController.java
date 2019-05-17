package com.xuecheng.manage.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.response.AddCourseResult;

/**
 * 模拟课程管理页面前端
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/course/test")
public class TestCourseController {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;

	/**
	 * 1.模拟添加课程
	 */
	@GetMapping("/addCourseBase")
	public AddCourseResult addCourseBase() {
		CourseBase courseBase = new CourseBase();
		courseBase.setCompanyId("1");
		courseBase.setDescription("Lucene从小白开始入门到精通");
		courseBase.setGrade("200001");// 难度等级为初级
		courseBase.setMt("1-1");
		courseBase.setName("Lucene入门教程");
		courseBase.setSt("1-1-1");
		courseBase.setStudymodel("201001");// 录播
		courseBase.setUsers("适合掌握一些编程语言的初级程序员");

		// 获取请求url
		ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_COURSE);
		String url = choose.getUri() + "/course/coursebase/add";

		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("companyId", courseBase.getCompanyId());
		body.add("description", courseBase.getDescription());
		body.add("grade", courseBase.getGrade());
		body.add("mt", courseBase.getMt());
		body.add("name", courseBase.getName());
		body.add("st", courseBase.getSt());
		body.add("studymodel", courseBase.getStudymodel());
		body.add("users", courseBase.getUsers());

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);
		ResponseEntity<AddCourseResult> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				AddCourseResult.class);

		return response.getBody();
	}

	/**
	 * 模拟课程计划的添加
	 */
	@GetMapping("/teachplan/add/{courseId}")
	public ResponseResult addTeachplan(@PathVariable("courseId") String courseId) {
		Teachplan teachplan = new Teachplan();
		teachplan.setCourseid(courseId);
		teachplan.setDescription("初识lucene");
		teachplan.setGrade("1");
		teachplan.setPname("第一章");
		teachplan.setPtype("1");
		teachplan.setTimelength(24d);
		// 获取请求url
		ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_COURSE);
		String url = choose.getUri() + "/course/teachplan/add";

		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("courseid", teachplan.getCourseid());
		body.add("description", teachplan.getDescription());
		body.add("grade", teachplan.getGrade());
		body.add("status", teachplan.getStatus());
		body.add("pname", teachplan.getPname());
		body.add("timelength", teachplan.getTimelength() + "");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);
		ResponseEntity<ResponseResult> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				ResponseResult.class);

		return response.getBody();
	}

	/**
	 * 模拟课程图片上传
	 */
	@GetMapping("/pic/upload")
	public ResponseResult uploadPic() {

		return null;
	}

	/**
	 * 模拟教师信息的添加
	 */
	@GetMapping("/teacher/add")
	public ResponseResult addTeacherMessage() {
		return null;
	}

	/**
	 * 模拟分页查询媒体文件信息，供教学计划进行关联
	 */
	@GetMapping("/mediaFile/list/{page}/{size}")
	public QueryResponseResult queryMediaFile() {
		// 获取请求url
		ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_MEDIA);
		String url = choose.getUri() + "/media/list/0/1";

		// 获取媒体文件
		ResponseEntity<QueryResponseResult> response = restTemplate.getForEntity(url, QueryResponseResult.class);
		return response.getBody();
	}

	/**
	 * 模拟教学计划关联视频文件
	 */
	@GetMapping("/saveMedia")
	public ResponseResult saveMedia() {
		TeachplanMedia tm = new TeachplanMedia();
		tm.setCourseId("11611f8f-2bdd-4783-9b08-092569dd8fcc");
		tm.setMediaFileOriginalName("lucene.avi");
		tm.setMediaId("c5c75d70f382e6016d2f506d134eee11");
		tm.setMediaUrl("c5c75d70f382e6016d2f506d134eee11.m3u8");
		tm.setTeachplanId("4028e58161bd3b380161bd40cf3400ii");
		// 获取请求url
		ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_COURSE);
		String url = choose.getUri() + "/course/savemedia";

		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("courseId", tm.getCourseId());
		body.add("mediaFileOriginalName", tm.getMediaFileOriginalName());
		body.add("mediaId", tm.getMediaId());
		body.add("mediaUrl", tm.getMediaUrl());
		body.add("teachplanId", tm.getTeachplanId());
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);
		ResponseEntity<ResponseResult> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				ResponseResult.class);
		return response.getBody();
	}

	/**
	 * 模拟课程页面预览
	 */
	@GetMapping("/preview")
	public void preview() {
		// 获取请求url
		ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_COURSE);
		String url = choose.getUri() + "/course/previewhtml/{siteId}/{courseId}/{templateId}";
	}

	/**
	 * 模拟课程页面发布
	 */
}
