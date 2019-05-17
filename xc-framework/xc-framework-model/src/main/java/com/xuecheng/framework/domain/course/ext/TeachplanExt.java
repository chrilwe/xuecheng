package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/2/7.
 */
public class TeachplanExt extends Teachplan {

    //媒资文件id
    private String mediaId;

    //媒资文件原始名称
    private String mediaFileOriginalName;

    //媒资文件访问地址
    private String mediaUrl;

	@Override
	public String toString() {
		return "TeachplanExt [mediaId=" + mediaId + ", mediaFileOriginalName=" + mediaFileOriginalName + ", mediaUrl="
				+ mediaUrl + "]";
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaFileOriginalName() {
		return mediaFileOriginalName;
	}

	public void setMediaFileOriginalName(String mediaFileOriginalName) {
		this.mediaFileOriginalName = mediaFileOriginalName;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
}
