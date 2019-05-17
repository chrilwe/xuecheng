package com.xuecheng.message.system.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskThreadPool {
	
	/**
	 * 初始化线程池
	 */
	private TaskThreadPool() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		executorService.execute(new MessageTask1());
		executorService.execute(new MessageTask2());
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static TaskThreadPool pool = null;
		static {
			pool = new TaskThreadPool();
		}
		public static TaskThreadPool getInstance() {
			return pool;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static TaskThreadPool getInstance() {
		return Singleton.getInstance();
	}
}
