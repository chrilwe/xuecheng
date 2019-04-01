package com.xuecheng.framework.domain.media;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 10:04.
 * @Modified By:
 */
public class MediaFileProcess_m3u8 extends MediaFileProcess {

    //ts列表
    private List<String> tslist;
    //错误信息
    private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<String> getTslist() {
		return tslist;
	}

	public void setTslist(List<String> tslist) {
		this.tslist = tslist;
	}

	@Override
	public String toString() {
		return "MediaFileProcess_m3u8 [tslist=" + tslist + ", errorMsg=" + errorMsg + "]";
	}

}
