package com.xuecheng.manager.cms_client.queue;
/**
 * 将消息路由到队列中
 * @author Administrator
 *
 */

import java.util.concurrent.ArrayBlockingQueue;

public class Router {
	
	private String pageId;
	
	public Router(String pageId) {
		this.pageId = pageId;
	}
	
	/**
	 * 路由
	 */
	public void rout() {
		//获取队列
		ArrayBlockingQueue<String> queue = getQueue(pageId);
		//将消息放到队列中
		try {
			queue.put(pageId);
		} catch (InterruptedException e) {
			System.out.println("队列添加信息异常！！");
			e.printStackTrace();
		}
	}
	
	/**
	 * 将消息进行hash运算取模，得到路由后的队列
	 */
	private ArrayBlockingQueue<String> getQueue(String pageId) {
		WorkQueueList queues = WorkQueueList.getInstance();
		int size = queues.size();
		int index = pageId.hashCode() % size;
		
		return queues.getQueue(index);
	}
}
