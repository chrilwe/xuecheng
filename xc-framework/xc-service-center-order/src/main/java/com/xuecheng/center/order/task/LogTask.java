package com.xuecheng.center.order.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

public class LogTask implements Runnable {
	
	private String logPath;
	private String orderDetailsJson;
	
	public LogTask(String logPath,String orderDetailsJson) {
		this.logPath = logPath;
		this.orderDetailsJson = orderDetailsJson;
	}
	
	@Override
	public void run() {
		//文件命名,当前的时间：年月日
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String logName = sdf.format(new Date());
		
		//判断当前路径的文件夹是否存在
		File floder = new File(logPath);
		if(!floder.exists()) {
			floder.mkdirs();
		}
		
		//获取文本文件对象
		File file = new File(logPath + logName + ".text");
		//将订单数据写入文本文件中
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file,true);
			fileWriter.write(orderDetailsJson + "\r\n");
			
			//TODO 实时统计当前购买热门数据,通过 kafka收集数据
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
