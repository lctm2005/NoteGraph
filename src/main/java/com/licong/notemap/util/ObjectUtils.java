package com.licong.notemap.util;

/**
 * Object工具类
 * 
 * @author James-li
 *
 * 2013-2-21
 */
public class ObjectUtils {

	private static final int EMPTY_NUM = 0;	
	
	/**
	 * 判断数组是否为空
	 * @param array	数组
	 * @return <code>true</code> if array is empty,else return <code>false</code> 
	 */
	public static final boolean isEmpty(final Object[] array) {
		return null == array  || EMPTY_NUM == array.length; 
	}
	
	/**
	 * 判断数组是否非空
	 * @param array	数组
	 * @return <code>true</code> if array is not empty,else return <code>false</code> 
	 */
	public  static final boolean isNotEmpty(final Object[] array) {
		return !isEmpty(array);
	}
	
	/**
	 * 判断对象是否为null
	 * @param object 对象
	 * @return <code>true</code> if object is null,else return <code>false</code> 
	 */
	public  static final boolean isNull(final Object object) {
		return null == object;
	}
	
	/**
	 * 判断对象是否非null
	 * @param object 对象
	 * @return <code>true</code> if object is not null,else return <code>false</code> 
	 */
	public static final boolean isNotNull(final Object object) {
		return !isNull(object);
	}
}
