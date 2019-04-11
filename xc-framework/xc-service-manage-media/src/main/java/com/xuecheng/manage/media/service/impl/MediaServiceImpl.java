package com.xuecheng.manage.media.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.CommonCode;
import com.xuecheng.framework.common.model.response.QueryResponseResult;
import com.xuecheng.framework.common.model.response.QueryResult;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.manage.media.client.MediaProcessorClient;
import com.xuecheng.manage.media.dao.MediaRepository;
import com.xuecheng.manage.media.service.MediaService;

@Service
public class MediaServiceImpl implements MediaService {

	@Autowired
	private MediaRepository mediaRepository;
	@Autowired
	private MediaProcessorClient mediaProcessorClient;

	@Value("${xc-service-manage-media.media-location}")
	private String mediaLocation;

	@Override
	public ResponseResult uploadChunkFile(MultipartFile file, Integer chunk, String fileMd5) {
		// 检查上传文件
		if (file == null) {
			ExceptionCast.cast(MediaCode.MERGE_FILE_ISNULL);
		}

		// 创建文件目录
		this.createChunkFileFloder(fileMd5);
		// 将块文件写到块文件目录中
		InputStream inputStream = null;
		OutputStream output = null;
		try {
			String chunkFileFloder = this.getChunkFileFloder(fileMd5);
			File f = new File(chunkFileFloder + chunk);
			inputStream = file.getInputStream();
			output = new FileOutputStream(f);
			int copy = IOUtils.copy(inputStream, output);
		} catch (Exception e) {
			ExceptionCast.cast(MediaCode.UPLOAD_FILE_FAIL);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new ResponseResult(CommonCode.SUCCESS);
	}

	/**
	 * 创建块文件目录
	 */
	private void createChunkFileFloder(String fileMd5) {
		// 获取块文件目录
		String chunkFileFloder = this.getChunkFileFloder(fileMd5);
		File chunkFile = new File(chunkFileFloder);

		// 如果不存在该目录，创建目录，否则不创建
		if (!chunkFile.exists()) {
			boolean mkdirs = chunkFile.mkdirs();
		}
	}
	
	/**
	 * 创建合并文件目录
	 */
	private boolean createMergeFileFloder(String fileMd5) {
		//获取合并文件的目录
		String mergeFileFloder = this.getMergeFileFloder(fileMd5);
		//创建合并文件对象
		File file = new File(mergeFileFloder);
		if(!file.exists()) {
			return file.mkdir();
		}
		return true;
	}

	/**
	 * 获取文件目录
	 */
	private String getFileFloder(String fileMd5) {
		String filePath = mediaLocation + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5;
		return filePath;
	}

	/**
	 * 获取合并文件目录
	 */
	private String getMergeFileFloder(String fileMd5) {
		String fileFloder = this.getFileFloder(fileMd5);

		return fileFloder + "/mergefile/";
	}

	/**
	 * 获取块文件目录
	 */
	private String getChunkFileFloder(String fileMd5) {
		String fileFloder = this.getFileFloder(fileMd5);
		String chunkFileFloder = fileFloder + "/chunks/";
		return chunkFileFloder;
	}

	@Override
	public ResponseResult register(String fileMd5, String fileName, String fileSize, String mimeType, String fileExt) {
		// 校验文件是否已经存在
		String fileFloder = this.getFileFloder(fileMd5);
		File file = new File(fileFloder);

		Optional<MediaFile> optional = mediaRepository.findById(fileMd5);
		if (file.exists() && optional.isPresent()) {
			ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
		}

		// 创建文件目录
		boolean createFloder = this.createFileFloder(fileMd5);
		if (!createFloder) {
			ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_CREATEFLOADER_FAIL);
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}

	/**
	 * 创建文件目录
	 */
	private boolean createFileFloder(String fileMd5) {
		// 获取文件的path
		String fileFloder = this.getFileFloder(fileMd5);
		// 获取文件对象
		File file = new File(fileFloder);

		// 判断文件是否已经被创建
		if (!file.exists()) {
			boolean mkdir = file.mkdir();
			return mkdir;
		}

		return true;
	}

	@Override
	public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
		// 获取块文件的路径
		String chunkFileFloder = this.getChunkFileFloder(fileMd5);
		// 获取块文件对象
		File file = new File(chunkFileFloder + chunk);

		if (file.exists()) {
			return new CheckChunkResult(MediaCode.UPLOAD_FILE_REGISTER_EXIST, true);
		} else {
			return new CheckChunkResult(MediaCode.UPLOAD_FILE_REGISTER_EXIST, false);
		}
	}

	@Override
	public ResponseResult mergeChunkFile(String fileMd5, String fileName, String fileSize, String mimeType,
			String fileExt, String userId) {
		//获取块文件地址
		String chunkFileFloder = this.getChunkFileFloder(fileMd5);
		//将块文件升序排序
		List<File> chunkFiles = this.sort_asc(chunkFileFloder);
		//获取合并文件
		File mergeFile = new File(this.getMergeFileFloder(fileMd5) + fileMd5 + "." + fileExt);
		//合并文件存在先删除文件
		if(mergeFile.exists()) {
			mergeFile.delete();
		}
		//创建合并文件的目录
		boolean create = this.createMergeFileFloder(fileMd5);
		if(!create) {
			ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_CREATEFLOADER_FAIL);
		}
		//合并文件
		boolean merge_file = this.merge_file(chunkFiles, mergeFile);
		if(!merge_file) {
			ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
		}
		//校验文件md5
		boolean checkFileMd5 = this.checkFileMd5(mergeFile, fileMd5);
		if(!checkFileMd5) {
			ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
		}
		//将文件信息存储到db
		MediaFile mediaFile = new MediaFile();
		mediaFile.setFileId(fileMd5);
		mediaFile.setFileName(fileMd5 + "." + fileExt);
		mediaFile.setFilePath(this.getMergeFileFloder(fileMd5) + fileMd5 + "." + fileExt);
		mediaFile.setFileOriginalName(fileName);
		mediaFile.setFileSize(Long.parseLong(fileSize));
		mediaFile.setFileStatus("301002");//状态为上传成功
		mediaFile.setMimeType(mimeType);
		mediaFile.setUploadTime(new Date());
		mediaFile.setFileType(fileExt);
		mediaFile.setUserId(userId);
		
		MediaFile save = mediaRepository.save(mediaFile);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	/**
	 * 对分块文件升序排序
	 */
	private List<File> sort_asc(String chunkFileFloder) {
		//获取块文件目录对象
		File file = new File(chunkFileFloder);
		File[] files = file.listFiles();
		List<File> list = new ArrayList<File>();
		for (File f : files) {
			list.add(f);
		}
		// 3.文件排序
		if (file.exists()) {
			Collections.sort(list, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
						return 1;
					}
					return -1;
				}

			});
		}
		
		return list;
	}
	
	/**
	 * 合并文件
	 * @param chunkFiles ：升序排序好的块文件
	 */
	private boolean merge_file(List<File> chunkFiles, File mergeFile) {
		try {
			RandomAccessFile w = new RandomAccessFile(mergeFile, "rw");
			for (File chunkFile : chunkFiles) {
				RandomAccessFile r = new RandomAccessFile(chunkFile, "r");
				//缓冲区
				byte[] b = new byte[1024];
				int len = -1;
				while((len = r.read(b)) != -1) {
					w.write(b, 0, len);
				}
				r.close();
			}
			w.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 校验文件MD5
	 */
	private boolean checkFileMd5(File file, String fileMd5) {
		FileInputStream input = null;
		try {
			//获取文件输入流
			input = new FileInputStream(file);
			String mergeFileMd5 = DigestUtils.md5DigestAsHex(input);
			//比较MD5
			if(!mergeFileMd5.equals(fileMd5)) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	@Override
	public QueryResponseResult findMediaFileList(int page, int size, 
			QueryMediaFileRequest queryMediaFileRequest, String userId) {
		if(StringUtils.isEmpty(userId)) {
			ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);//请认证
		}
		MediaFile mediaFile = new MediaFile();
		//校验参数,参数为空，查询条件就查询当前页的全部
		if(queryMediaFileRequest == null) {
			queryMediaFileRequest = new QueryMediaFileRequest();
		}
		
		if(StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
			mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
		}
		
		if(StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
			mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
		}
		
		if(StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
			mediaFile.setTag(queryMediaFileRequest.getTag());
		}
		
		//查询条件字段设置
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.contains());
		
		Example<MediaFile> example = Example.of(mediaFile, matcher);
		
		//分页条件
		if(page <= 0) {
			page = 1;
		}
		page = page -1;
		
		Pageable pageable = new QPageRequest(page,size);
		Page<MediaFile> pageList = mediaRepository.findAll(example, pageable);
		QueryResult queryResult = new QueryResult();
		queryResult.setList(pageList.getContent());
		queryResult.setTotal(pageList.getTotalElements());
		QueryResponseResult result = new QueryResponseResult(CommonCode.SUCCESS,queryResult );
		return result;
	}

	@Override
	public ResponseResult deleteMediaFileByFileId(String fileMd5, String userId) {
		MediaFile mediaFile = new MediaFile();
		mediaFile.setFileId(fileMd5);
		mediaFile.setUserId(userId);
		mediaRepository.delete(mediaFile);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	public ResponseResult processVideo(String fileMd5) {
		ResponseResult result = mediaProcessorClient.processVideo(fileMd5);
		return result;
	}
}
