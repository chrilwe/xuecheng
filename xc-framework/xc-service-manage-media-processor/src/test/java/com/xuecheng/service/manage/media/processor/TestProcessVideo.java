package com.xuecheng.service.manage.media.processor;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.utils.HlsVideoUtil;

public class TestProcessVideo {
	public static void main(String[] args) {
		MediaFile file = new MediaFile();
		String fileName = file.getFileName();
		String ffmpeg_path = "D:/ffmpeg/ffmpeg-20190330-5282cba-win32-static/bin/ffmpeg.exe";
		String video_path = "F:/develop/video/lucene.avi";
		String m3u8_name = "lucene.m3u8";
		String m3u8floder_path = "F:/develop/video/video/";
		HlsVideoUtil util = new HlsVideoUtil(ffmpeg_path ,video_path,m3u8_name ,m3u8floder_path );
		String generateM3u8 = util.generateM3u8();
	}
}
