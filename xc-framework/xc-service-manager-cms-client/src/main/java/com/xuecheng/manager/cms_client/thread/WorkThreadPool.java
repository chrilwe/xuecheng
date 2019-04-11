package com.xuecheng.manager.cms_client.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xuecheng.manager.cms_client.queue.WorkQueueList;

/**
 * 单例线程池
 * @author Administrator
 *
 */
public class WorkThreadPool {
	//初始化大小为10的线程池
	private static final int nThreads = 10;
	
	/**
	 * 类的初始化
	 */
	private WorkThreadPool() {
		ExecutorService pool = Executors.newFixedThreadPool(nThreads);
		WorkQueueList queues = WorkQueueList.getInstance();
		for(int i = 0; i < nThreads; i++) {
			ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(50);
			WorkThread workThread = new WorkThread(queue);
			queues.addQueue(queue);
			pool.execute(workThread);
		}
	}
	
	/**
	 * 内部静态类
	 */
	private static class Singleton {
		private static WorkThreadPool workThreadPool = null;
		static {
			workThreadPool = new WorkThreadPool();
		}
		
		private static WorkThreadPool getInstance() {
			
			return workThreadPool;
		}
	}
	
	/**
	 * 获取单例实例
	 */
	public static WorkThreadPool getInstance() {
		return Singleton.getInstance();
	}
	
	/**
	 * 类的初始方法
	 */
	public static void init() {
		getInstance();
	} 
}
