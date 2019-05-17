package com.xuecheng.framework.domain.media;

import com.xuecheng.framework.utils.MD5Util;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * Created by admin on 2018/3/5.
 */
@Document(collection = "media_video_course")
public class MediaVideoCourse {

    @Id
    private String id;
    //课程id
    private String courseid;
    //章节id
    private String chapter;
    //文件id
    private String fileId;
    //视频处理方式
    private String processType;
    //视频处理状态
    private String processStatus;
    //HLS处理结果
    private String hls_m3u8;
    private List<String> hls_ts_list;

    public MediaVideoCourse(String fileId,String courseid,String chapter){
        this.fileId = fileId;
        this.courseid = courseid;
        this.chapter = chapter;
        this.id = MD5Util.getStringMD5(courseid+chapter);
        this.processType = "302002";//生成 hls
        this.processStatus="303001";//未处理
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getHls_m3u8() {
		return hls_m3u8;
	}

	public void setHls_m3u8(String hls_m3u8) {
		this.hls_m3u8 = hls_m3u8;
	}

	public List<String> getHls_ts_list() {
		return hls_ts_list;
	}

	public void setHls_ts_list(List<String> hls_ts_list) {
		this.hls_ts_list = hls_ts_list;
	}

	@Override
	public String toString() {
		return "MediaVideoCourse [id=" + id + ", courseid=" + courseid + ", chapter=" + chapter + ", fileId=" + fileId
				+ ", processType=" + processType + ", processStatus=" + processStatus + ", hls_m3u8=" + hls_m3u8
				+ ", hls_ts_list=" + hls_ts_list + "]";
	}

}
