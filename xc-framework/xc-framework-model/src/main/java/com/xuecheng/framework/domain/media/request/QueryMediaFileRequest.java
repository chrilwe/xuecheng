package com.xuecheng.framework.domain.media.request;

import com.xuecheng.framework.common.model.request.RequestData;
import lombok.Data;

public class QueryMediaFileRequest extends RequestData {

    private String fileOriginalName;
    private String processStatus;
    private String tag;
	public String getFileOriginalName() {
		return fileOriginalName;
	}
	public void setFileOriginalName(String fileOriginalName) {
		this.fileOriginalName = fileOriginalName;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Override
	public String toString() {
		return "QueryMediaFileRequest [fileOriginalName=" + fileOriginalName + ", processStatus=" + processStatus
				+ ", tag=" + tag + "]";
	}
	
}
