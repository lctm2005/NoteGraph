package com.licong.notemap.util.filemanager;

import java.util.ArrayList;
import java.util.List;

import static com.licong.notemap.util.ObjectUtils.isNotNull;


/**
 * 文件搜索结果
 * @author James
 */
public class FileSearchResult {

	private List<SingleFileSearchResult> results = new ArrayList<SingleFileSearchResult>();
	private int count = 0;	 //命中次数
	
	/**
	 * 添加单文件搜索结果
	 * @param result
	 */
	public void put(SingleFileSearchResult result) {
		if(isNotNull(result)) {
			results.add(result);
			count += result.getCount();
		}
	}
	
	/**
	 * 获取搜索结果
	 * @return
	 */
	public List<SingleFileSearchResult> gets() {
		return results;
	}
	
	/**
	 * 是否有搜索结果
	 * @return
	 */
	public boolean hasResult() {
		return results.size() > 0;
	}
	
	/**
	 * 获取命中次数
	 * @return
	 */
	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[").append(count).append(" searched]\n");
		str.append("Occurrences:\n");
		for(SingleFileSearchResult result : results) {
			str.append(result.toString()).append("\n");
		}
		return str.toString();
	}
	
}
