package com.xuecheng.manager.cms_client.queue;

public class Test {
	public static void main(String[] args) {
		WorkQueueList list = WorkQueueList.getInstance();
		WorkQueueList list1 = WorkQueueList.getInstance();
		System.out.println(list == list1);
	}
}
