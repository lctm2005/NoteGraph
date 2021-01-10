package com.licong.notemap.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展Apache Commons ArrayUtils, 提供对字符串数组的连接处理.
 *
 * @author bifeng.liu
 * @see org.apache.commons.lang3.ArrayUtils
 */
public abstract class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {
    /**
     * <p>把数组转换成List</p>
     * <p>该转换后还可以自由添加元素</p>
     *
     * @param array 数组
     * @param <T>
     * @return
     * @see java.util.Arrays#asList(T[])
     */
    public static <T> List<T> asList(T... array) {
        if (array == null) {
            return new ArrayList<T>(0);
        }
        List<T> result = new ArrayList<T>(array.length);
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            result.add(t);
        }
        return result;
    }
}
