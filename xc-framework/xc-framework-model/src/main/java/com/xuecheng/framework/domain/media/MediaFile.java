package com.xuecheng.framework.domain.media;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 10:04.
 * @Modified By:
 */
@Document(collection = "media_file")
public class MediaFile {
    /*
    文件id、名称、大小、文件类型、文件状态（未上传、上传完成、上传失败）、上传时间、视频处理方式、视频处理状态、hls_m3u8,hls_ts_list、课程视频信息（课程id、章节id）
     */
    @Id
    //文件id
    private String fileId;
    //文件名称
    private String fileName;
    //文件原始名称
    private String fileOriginalName;
    //文件路径
    private String filePath;
    //文件url
    private String fileUrl;
    //文件类型
    private String fileType;
    //mimetype
    private String mimeType;
    //文件大小
    private Long fileSize;
    //文件状态
    private String fileStatus;
    //上传时间
    private Date uploadTime;
    //处理状态
    private String processStatus;
    //hls处理
    private MediaFileProcess_m3u8 mediaFileProcess_m3u8;
    //userId
    private String userId;
    //视频时间长度
    private String mediaTime;
    //tag标签用于查询
    private String tag;

	public String getMediaTime() {
		return mediaTime;
	}

	public void setMediaTime(String mediaTime) {
		this.mediaTime = mediaTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileOriginalName() {
		return fileOriginalName;
	}

	public void setFileOriginalName(String fileOriginalName) {
		this.fileOriginalName = fileOriginalName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public MediaFileProcess_m3u8 getMediaFileProcess_m3u8() {
		return mediaFileProcess_m3u8;
	}

	public void setMediaFileProcess_m3u8(MediaFileProcess_m3u8 mediaFileProcess_m3u8) {
		this.mediaFileProcess_m3u8 = mediaFileProcess_m3u8;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "MediaFile [fileId=" + fileId + ", fileName=" + fileName + ", fileOriginalName=" + fileOriginalName
				+ ", filePath=" + filePath + ", fileUrl=" + fileUrl + ", fileType=" + fileType + ", mimeType="
				+ mimeType + ", fileSize=" + fileSize + ", fileStatus=" + fileStatus + ", uploadTime=" + uploadTime
				+ ", processStatus=" + processStatus + ", mediaFileProcess_m3u8=" + mediaFileProcess_m3u8 + ", userId="
				+ userId + ", mediaTime=" + mediaTime + ", tag=" + tag + "]";
	}

}
