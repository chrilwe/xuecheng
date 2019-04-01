package com.xuecheng.framework.domain.filesystem;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by mrt on 2018/2/5.
 */
@Document(collection = "filesystem")
public class FileSystem {

    @Id
    private String fileId;
    //文件请求路径
    private String filePath;
    //文件大小
    private long fileSize;
    //文件名称
    private String fileName;
    //文件类型
    private String fileType;
    //图片宽度
    private int fileWidth;
    //图片高度
    private int fileHeight;
    //用户id，用于授权
    private String userId;
    //业务key
    private String businesskey;
    //业务标签
    private String filetag;
    //文件元信息
    private Map metadata;
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public int getFileWidth() {
		return fileWidth;
	}
	public void setFileWidth(int fileWidth) {
		this.fileWidth = fileWidth;
	}
	public int getFileHeight() {
		return fileHeight;
	}
	public void setFileHeight(int fileHeight) {
		this.fileHeight = fileHeight;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBusinesskey() {
		return businesskey;
	}
	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}
	public String getFiletag() {
		return filetag;
	}
	public void setFiletag(String filetag) {
		this.filetag = filetag;
	}
	public Map getMetadata() {
		return metadata;
	}
	public void setMetadata(Map metadata) {
		this.metadata = metadata;
	}
	@Override
	public String toString() {
		return "FileSystem [fileId=" + fileId + ", filePath=" + filePath + ", fileSize=" + fileSize + ", fileName="
				+ fileName + ", fileType=" + fileType + ", fileWidth=" + fileWidth + ", fileHeight=" + fileHeight
				+ ", userId=" + userId + ", businesskey=" + businesskey + ", filetag=" + filetag + ", metadata="
				+ metadata + "]";
	}

}
