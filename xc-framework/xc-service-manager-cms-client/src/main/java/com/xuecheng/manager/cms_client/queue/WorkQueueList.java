package com.xuecheng.manager.cms_client.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 单例队列list
 * @author Administrator
 *
 */
public class WorkQueueList {
	
	private List<ArrayBlockingQueue<String>> queues = null;
	
	/**
	 * 类的初始化
	 */
	private WorkQueueList() {
		queues = new ArrayList<ArrayBlockingQueue<String>>();
	}
	
	/**
	 * 获取队列大小
	 */
	public int size() {
		return queues.size();
	}
	
	/**
	 * 根据index获取队列
	 */
	public ArrayBlockingQueue<String> getQueue(int index) {
		
		return queues.get(index);
	}
	
	/**
	 * 添加队列实例对象
	 */
	public void addQueue(ArrayBlockingQueue<String> queue) {
		queues.add(queue);
	}
	
	/**
	 * 内部静态类
	 */
	private static class Singleton {
		private static WorkQueueList workQueueList = null;
		static {
			workQueueList = new WorkQueueList();
		}
		
		private static WorkQueueList getInstance() {
			
			return workQueueList;
		}
	}
	
	/**
	 * 获取单例实例
	 */
	public static WorkQueueList getInstance() {
		
		return Singleton.getInstance();
	}
}
