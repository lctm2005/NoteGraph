package com.licong.notemap.util;

import java.util.Map;

/**
 * <p>随机生成工具类</p>
 * 提供随机生成的一些通用方法
 *
 * @author bifeng.liu
 * @since 2015/12/14.
 */
public class RandomUtils extends org.apache.commons.lang3.RandomUtils {
    /**
     * 根据指定的样本数组和样本-权重映射随机抽取一个元素
     *
     * @param elementArray 样本数组
     * @param weightMap    样本-权重映射
     * @return
     * @throws IllegalArgumentException
     */
    public static Object randomByWeight(Object[] elementArray, Map<Object, Integer> weightMap) throws IllegalArgumentException {
        if (ArrayUtils.isEmpty(elementArray)) {
            throw new IllegalArgumentException("ElementArray must not be empty!");
        }
        if (MapUtils.isEmpty(weightMap)) {
            throw new IllegalArgumentException("WeightMap must not be empty!");
        }
        int[] weightArray = new int[elementArray.length + 1];
        Object[] tempElementArray = new Object[elementArray.length];
        for (int i = 0; i < elementArray.length; i++) {
            tempElementArray[i] = elementArray[i];
            Integer weight = weightMap.get(elementArray[i]);
            weightArray[i + 1] = weightArray[i] + (weight == null ? 0 : weight);
        }
        double randomNum = weightArray[weightArray.length - 1] * Math.random();
        int index = biSearch(weightArray, randomNum);
        return index == -1 ? null : tempElementArray[index];
    }

    /**
     * 采用二分搜索查找对应值的区间
     * <p/>
     * 如果对应值没有在给出的区间，则返回-1
     *
     * @param desArray 值区间
     * @param des      要查找的值
     * @return 返回值对应的数组下标
     */
    private static int biSearch(int[] desArray, double des) {
        int low = 0;
        int high = desArray.length - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            // 如果对应值在相应的区间，则直接返回
            if (des > desArray[middle] && des <= desArray[middle + 1]) {
                return middle;
            } else if (des < desArray[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }
}
