package com.xuecheng.framework.common.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 18:33.
 * @Modified By:
 */
public class QueryResult<T> {
    //数据列表
    private List<T> list;
    //数据总数
    private long total;
	
    
    public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	
	
	@Override
	public String toString() {
		return "QueryResult [list=" + list + ", total=" + total + "]";
	}
    
    
}
