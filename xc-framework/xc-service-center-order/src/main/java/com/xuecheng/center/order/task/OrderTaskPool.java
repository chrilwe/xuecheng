package com.xuecheng.center.order.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class OrderTaskPool {
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static OrderTaskPool pool = null;
		static {
			pool = new OrderTaskPool();
		}
		
		private static OrderTaskPool getInstance() {
			return pool;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static OrderTaskPool getInstance() {
		return Singleton.getInstance();
	}
	
	/**
	 * 初始化线程池
	 */
	private OrderTaskPool() {
		//初始化线程池，并发线程大小为10
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		//初始化10个线程执行监控过期未支付的订单
		for(int i = 0;i < 10;i++) {
			OrderTask orderTask = new OrderTask();
			executorService.submit(orderTask);
			PayTask payTask = new PayTask();
			executorService.submit(payTask);
		}
	}
}
