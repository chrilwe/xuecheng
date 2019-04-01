package com.xuecheng.manage.media.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TestChunkFile {
	
	public static void main(String[] args) {
		RandomAccessFile w = null;
		RandomAccessFile r = null;
		
		try {
			//源文件存放地址
			String sourcePath = "D:/chunk/source/03-项目概述-技术架构 [www.52yzzy.com 吾爱优质资源网].avi";
			//分块后文件存放的地址目录
			String chunkFilesPath = "D:/chunk/chunks/";
			//每个分块文件的大小
			int chunkFileSize = 1 * 1024 * 1024;//1M
			//获取源文件对象
			File resource = new File(sourcePath);
			//计算分块后文件的数量
			long length = resource.length();
			long fileNum = length % chunkFileSize == 0?length / chunkFileSize:length / chunkFileSize + 1; 
			//将源文件写入每一个块中
			//从源文件读
			r = new RandomAccessFile(resource, "r");
			for(int i = 0; i < fileNum; i++) {
				w = new RandomAccessFile(new File(chunkFilesPath +i), "rw");
				byte[] b = new byte[1024];
				int l = -1;
				while((l = r.read(b)) != -1) {
					//将文件写入块中
					w.write(b, 0, l);
					if(l > chunkFileSize) {
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				r.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				w.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
