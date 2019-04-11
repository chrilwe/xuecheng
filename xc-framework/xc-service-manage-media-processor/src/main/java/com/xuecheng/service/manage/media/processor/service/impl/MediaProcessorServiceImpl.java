package com.xuecheng.service.manage.media.processor.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.service.manage.media.processor.dao.MediaFileRepository;
import com.xuecheng.service.manage.media.processor.service.MediaProcessorService;

@Service
public class MediaProcessorServiceImpl implements MediaProcessorService {
	
	@Autowired
	private MediaFileRepository mediaFileRepository;
	
	@Value("${xc-service-manage-media-processor.processor-media-location}")
    private String m3u8floder_path;
    @Value("${{xc-service-manage-media-processor.ffmpeg-location}")
    private String ffmpeg_path;
	
	@Override
	public void processMedia(String fileMd5) {
		//根据id查询MediaFile
		Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5.toString());
		if(optional.isPresent()) {
			MediaFile mediaFile = optional.get();
			if(mediaFile == null 
					|| StringUtils.isEmpty(mediaFile.getFileName()) 
					|| StringUtils.isEmpty(mediaFile.getFilePath())
					|| StringUtils.isEmpty(mediaFile.getFilePath())
					|| !mediaFile.getFileType().equals("avi")) {
				mediaFile.setProcessStatus("303004");//处理状态为无需处理
				mediaFileRepository.save(mediaFile);
				return;
			}
			mediaFile.setProcessStatus("303001");//处理状态为未处理
			mediaFileRepository.save(mediaFile);
			
			String video_path = mediaFile.getFilePath();
			String m3u8_name = mediaFile.getFileName();
			HlsVideoUtil util = new HlsVideoUtil(ffmpeg_path ,video_path,m3u8_name ,m3u8floder_path);
			String result = util.generateM3u8();
			if(result == null || !result.equals("SUCCESS")) {
				mediaFile.setProcessStatus("303003");//处理状态为失败
				MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
				mediaFileProcess_m3u8.setErrorMsg("处理媒体失败:"+result);
				mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8 );
				mediaFileRepository.save(mediaFile);
			}
			
			mediaFile.setProcessStatus("303002");//处理状态为成功
			List<String> get_ts_list = util.get_ts_list();
			MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
			mediaFileProcess_m3u8.setTslist(get_ts_list);
			mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
			mediaFile.setFileUrl(m3u8floder_path + fileMd5 + ".m3u8");
			mediaFileRepository.save(mediaFile);
		}

	}
}
