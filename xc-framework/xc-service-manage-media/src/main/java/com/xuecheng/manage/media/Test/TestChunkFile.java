package com.xuecheng.manage.media.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class TestChunkFile {
	
	public static void main(String[] args) {
		
		try {
			//源文件存放地址
			String sourcePath = "F:/develop/lucene.avi";
			//分块后文件存放的地址目录
			String chunkFilesPath = "F:/develop/chunks/";
			//每个分块文件的大小
			int chunkFileSize = 1 * 1024 * 1024;//1M
			//获取源文件对象
			File resource = new File(sourcePath);
			//计算分块后文件的数量
			long length = resource.length();
			long fileNum = (long) Math.ceil(resource.length()*1.0/chunkFileSize);
			System.out.println("-----源文件大小----"+length);
			//将源文件写入每一个块中
			//从源文件读
			byte[] b = new byte[1024];
			RandomAccessFile r  = new RandomAccessFile(resource,"r");
			for(int i = 0; i < fileNum; i++) {
				File chunkFile = new File(chunkFilesPath+i);
				RandomAccessFile w = new RandomAccessFile(chunkFile,"rw");
				int l = -1;
				while((l = r.read(b)) != -1) {
					//将文件写入块中
					w.write(b, 0, l);
					if(chunkFile.length() > chunkFileSize) {
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
	}
}
