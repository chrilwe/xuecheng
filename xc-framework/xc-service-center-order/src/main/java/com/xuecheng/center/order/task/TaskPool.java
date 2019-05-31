package com.xuecheng.center.order.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskPool {
	
	private static ExecutorService executorService = Executors.newCachedThreadPool();
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static TaskPool pool = null;
		static {
			pool = new TaskPool();
		}
		
		private static TaskPool getInstance() {
			return pool;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static TaskPool getInstance() {
		return Singleton.getInstance();
	}
	
	/**
	 * 将线程提交个线程池
	 */
	public void submit(Runnable runnable) {
		executorService.submit(runnable);
	}
}
