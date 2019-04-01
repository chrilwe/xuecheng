package com.xuecheng.framework.domain.ucenter.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by mrt on 2018/5/21.
 */

public class AuthToken {
	
    public AuthToken() {
		
	}
    
	String access_token;//访问token
    String refresh_token;//刷新token
    String jwt_token;//jwt令牌
    
    
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getJwt_token() {
		return jwt_token;
	}
	public void setJwt_token(String jwt_token) {
		this.jwt_token = jwt_token;
	}
	@Override
	public String toString() {
		return "AuthToken [access_token=" + access_token + ", refresh_token=" + refresh_token + ", jwt_token="
				+ jwt_token + "]";
	}
    
    
}
