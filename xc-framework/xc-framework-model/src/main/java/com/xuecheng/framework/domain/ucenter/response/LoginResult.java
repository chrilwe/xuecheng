package com.xuecheng.framework.domain.ucenter.response;

import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.common.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by mrt on 2018/5/21.
 */
public class LoginResult extends ResponseResult {
	private String jwtToken;
	private String accessToken;
    public LoginResult() {
		super();
	}
	public LoginResult(ResultCode resultCode,String jwtToken,String accessToken) {
        super(resultCode);
        this.jwtToken = jwtToken;
        this.accessToken = accessToken;
    }
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	@Override
	public String toString() {
		return "LoginResult [jwtToken=" + jwtToken + ", accessToken=" + accessToken + "]";
	}
    
}
