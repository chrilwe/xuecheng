package com.xuecheng.manage.media.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.manage.media.service.MediaService;

/**
 * 模拟前端上传文件过程
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/media/test")
public class TestMediaController {

	@Autowired
	private MediaService mediaService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;

	/**
	 * 模拟前端将文件切成小块文件
	 * 
	 * @return
	 */
	@GetMapping("/chunk")
	public ResponseResult chunk() {
		try {
			// 获取源文件
			String sourcePath = "F:/develop/lucene.avi";
			File sourceFile = new File(sourcePath);

			// 获取输出文件地址
			String targetPath = "F:/develop/chunks/";

			// 切割成每一块文件的大小
			int chunkSize = 1 * 1024 * 1024;// 1M
			// 计算生成目标文件的数量
			int fileNum = (int) (sourceFile.length() % chunkSize == 0 ? sourceFile.length() / chunkSize
					: sourceFile.length() / chunkSize + 1);
			System.out.println("-----文件大小-----:" + sourceFile.length());
			System.out.println("-----切割文件的数量----:" + fileNum);
			// 从源文件中读
			RandomAccessFile r = new RandomAccessFile(sourceFile, "r");
			byte[] b = new byte[1024];
			for (int i = 0; i < fileNum; i++) {
				File targetFile = new File(targetPath + i);
				RandomAccessFile w = new RandomAccessFile(targetFile, "rw");
				int len = -1;
				while ((len = r.read(b)) != -1) {
					w.write(b, 0, len);
					if (targetFile.length() > chunkSize) {
						break;
					}
				}
				w.close();
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取源文件的md5值
	 */
	@GetMapping("/getSourceFileMd5")
	public String getMd5() {
		// 获取源文件
		String sourcePath = "F:/develop/lucene.avi";
		File sourceFile = new File(sourcePath);

		String md5Hex = "";
		try {
			md5Hex = DigestUtils.md5Hex(new FileInputStream(sourceFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Hex;
	}

	/**
	 * 获取源文件大小
	 */
	@GetMapping("/getFileSize")
	public long getFileSize() {
		// 获取源文件
		String sourcePath = "F:/develop/lucene.avi";
		File sourceFile = new File(sourcePath);

		return sourceFile.length();
	}

	/**
	 * 获取文件名称
	 */
	@GetMapping("/getFileName")
	public String getFileName() {
		// 获取源文件
		String sourcePath = "F:/develop/lucene.avi";
		File sourceFile = new File(sourcePath);
		return sourceFile.getName();
	}

	/**
	 * 模拟文件注册
	 */
	@GetMapping("/register")
	public ResponseResult register() {
		// 获取文件注册服务Url
		ServiceInstance services = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_MEDIA);
		URI uri = services.getUri();
		String url = uri + "/media/register";

		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("fileMd5", this.getMd5());
		body.add("fileName", this.getFileName());
		body.add("fileSize", this.getFileSize() + "");
		body.add("mimeType", "avi");
		body.add("fileExt", "avi");
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);

		ResponseEntity<ResponseResult> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				ResponseResult.class);
		return response.getBody();
	}

	/**
	 * 模拟上传块文件校验
	 */
	@GetMapping("/checkChunk")
	public CheckChunkResult checkChunk() {
		// 获取文件注册服务Url
		ServiceInstance services = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_MEDIA);
		URI uri = services.getUri();
		String url = uri + "/media/checkchunk";

		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("fileMd5", this.getMd5());
		body.add("chunk", "0");
		body.add("chunkSize", 1 * 1024 * 1024 + "");
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);

		return restTemplate.exchange(url, HttpMethod.POST, httpEntity, CheckChunkResult.class).getBody();
	}

	/**
	 * 模拟块文件上传
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@GetMapping("/uploadchunk")
	public ResponseResult uploadChunk() throws FileNotFoundException, IOException {
		// 获取文件注册服务Url
		ServiceInstance services = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_MEDIA);
		URI uri = services.getUri();
		String url = uri + "/media/uploadchunk";

		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("chunk", "0");
		body.add("fileMd5", this.getMd5());
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);
		return restTemplate.exchange(url, HttpMethod.POST, httpEntity, ResponseResult.class).getBody();
	}

	/**
	 * 模拟所有块文件上传完成后，执行合并文件操作
	 */
	@GetMapping("/mergeFile")
	public ResponseResult mergeFile() {
		// 获取文件注册服务Url
		ServiceInstance services = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MANAGE_MEDIA);
		URI uri = services.getUri();
		String url = uri + "/media/mergechunks";

		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("fileName", this.getFileName());
		body.add("fileMd5", this.getMd5());
		body.add("fileSize", this.getFileSize() + "");
		body.add("mimeType", "avi");
		body.add("fileExt", "avi");
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);
		return restTemplate.exchange(url, HttpMethod.POST, httpEntity, ResponseResult.class).getBody();
	}
}
