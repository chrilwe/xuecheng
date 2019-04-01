package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Created by admin on 2018/2/6.
 */
public class CmsConfigModel {
    private String key;
    private String name;
    private String url;
    private Map mapValue;
    private String value;
	
    
    public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map getMapValue() {
		return mapValue;
	}
	public void setMapValue(Map mapValue) {
		this.mapValue = mapValue;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return "CmsConfigModel [key=" + key + ", name=" + name + ", url=" + url + ", mapValue=" + mapValue + ", value="
				+ value + "]";
	}
    
}
