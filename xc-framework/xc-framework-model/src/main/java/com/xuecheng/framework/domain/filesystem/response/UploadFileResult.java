package com.xuecheng.framework.domain.filesystem.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/3/5.
 */
public class UploadFileResult extends ResponseResult{
   
	@ApiModelProperty(value = "文件信息", example = "true", required = true)
    FileSystem fileSystem;
    public UploadFileResult(ResultCode resultCode, FileSystem fileSystem) {
        super(resultCode);
        this.fileSystem = fileSystem;
    }
    
    @Override
   	public String toString() {
   		return "UploadFileResult [fileSystem=" + fileSystem + "]";
   	}
   	public FileSystem getFileSystem() {
   		return fileSystem;
   	}
   	public void setFileSystem(FileSystem fileSystem) {
   		this.fileSystem = fileSystem;
   	}
}
