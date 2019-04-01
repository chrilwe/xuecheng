package com.xuecheng.framework.common.model.response;

import lombok.Data;
import lombok.ToString;

public class QueryResponseResult extends ResponseResult {

	private QueryResult queryResult;

	public QueryResponseResult(ResultCode resultCode, QueryResult queryResult) {
		super(resultCode);
		this.queryResult = queryResult;
	}

	public QueryResult getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(QueryResult queryResult) {
		this.queryResult = queryResult;
	}

	@Override
	public String toString() {
		return "QueryResponseResult [queryResult=" + queryResult + "]";
	}
}
