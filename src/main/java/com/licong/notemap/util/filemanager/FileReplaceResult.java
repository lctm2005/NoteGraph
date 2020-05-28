package com.licong.notemap.util.filemanager;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 文件内容替换结果
 * @author James
 *
 */
public class FileReplaceResult {
	
	private File file;		//替换的文件
	private int count;	//替换次数
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "FileReplaceResult [target=" + file.getAbsolutePath() + "," + count + " occurrence was replaced]";
	}
	public static void main(String[] args) {
		ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<Integer>(1);
		arrayBlockingQueue.add(1);
		arrayBlockingQueue.add(2);
	}
	
}
