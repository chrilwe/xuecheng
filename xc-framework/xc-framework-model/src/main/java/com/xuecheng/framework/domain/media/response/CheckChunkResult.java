package com.xuecheng.framework.domain.media.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by admin on 2018/3/5.
 */
public class CheckChunkResult extends ResponseResult{

	public CheckChunkResult(ResultCode resultCode, boolean fileExist) {
        super(resultCode);
        this.fileExist = fileExist;
    }
    
	public CheckChunkResult() {
		super();
	}

	@ApiModelProperty(value = "文件分块存在标记", example = "true", required = true)
    boolean fileExist;
	public boolean isFileExist() {
		return fileExist;
	}
	public void setFileExist(boolean fileExist) {
		this.fileExist = fileExist;
	}
	 @Override
		public String toString() {
			return "CheckChunkResult [fileExist=" + fileExist + "]";
		}
}
