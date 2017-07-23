package com.frame.utils.pager;

import java.util.List;

 
public class Pager<T> {
	/**
	 * 分页的大小
	 */
	private int size;
	/**
	 * 分页的起始页
	 */
	private int offset;
	/**
	 * 总记录数
	 */
	private int total;
	
	private int pagesCount;
	/**
	 * 分页的数据
	 */
	private List<T> data;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getPagesCount() {
		/*故意如此，不让提示黄线*/
		 pagesCount=(total-1)/size+1;
		 return pagesCount;
	}
	 
 
}
