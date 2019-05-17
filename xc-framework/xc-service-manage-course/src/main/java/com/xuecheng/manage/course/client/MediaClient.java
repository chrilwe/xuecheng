package com.xuecheng.manage.course.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.model.response.QueryResponseResult;

@FeignClient(XcServiceList.XC_SERVICE_MANAGE_MEDIA)
public interface MediaClient {
	/**
	 * 批量查询媒体文件
	 * @param mediaIds 媒体文件id集合
	 */
	@GetMapping("/media/getMediaFile")
	public QueryResponseResult findMediaFileByMediaIds(String mediaIds);
}
