package com.xuecheng.manager.cms_client.thread;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.xuecheng.manager.cms_client.service.CmsManagerClientService;
import com.xuecheng.manager.cms_client.utils.SpringBeanUtil;

/**
 * 工作线程:从阻塞队列中获取pageId,并将静态页面推送到nginx
 * @author Administrator
 *
 */
public class WorkThread implements Runnable {

	/**
	 * 获取目标队列
	 */
	private ArrayBlockingQueue<String> queue;
	public WorkThread(ArrayBlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while(true) {
			try {
				//从队列中取出pageId，没有队列阻塞
				String pageId = queue.take();
				System.out.println("--------------------pageId:"+pageId);
				
				//执行业务操作
				CmsManagerClientService cmsManagerClientService = SpringBeanUtil.getBean(CmsManagerClientService.class);
				cmsManagerClientService.generHtml(pageId);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("从队列取出数据异常!!");
			}
		}
	}

}
