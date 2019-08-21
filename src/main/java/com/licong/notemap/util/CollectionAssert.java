package com.licong.notemap.util;

import java.util.Collection;

/**
 * 集合的断言类，提供包括集合、数组的断言
 *
 * @author bifeng.liu
 */
public abstract class CollectionAssert {


    /**
     * <p>断言两个集合是否相等</p>
     * <p>如果不相等，则直接抛出异常</p>
     *
     * @param firstCollection  第一个集合, 可能为 <code>null</code>
     * @param secondCollection 第二个集合, 可能为 <code>null</code>
     */
    public static void assertCollectionEquals(String message, final Collection firstCollection, final Collection secondCollection) {
        if (!isEqualCollection(firstCollection, secondCollection)) {
            fail(message);
        }
    }

    /**
     * <p>断言两个集合是否不相等</p>
     * <p>如果相等，则直接抛出异常</p>
     *
     * @param firstCollection  第一个集合, 可能为 <code>null</code>
     * @param secondCollection 第二个集合, 可能为 <code>null</code>
     */
    public static void assertNotCollectionEquals(String message, final Collection firstCollection, final Collection secondCollection) {
        if (isEqualCollection(firstCollection, secondCollection)) {
            fail(message);
        }
    }

    private static boolean isEqualCollection(final Collection firstCollection, final Collection secondCollection) {
        if (firstCollection == null && secondCollection == null) return true;
        if (firstCollection == null || secondCollection == null) return false;
        return CollectionUtils.isEqualCollection(firstCollection, secondCollection);
    }


    /**
     * 测试失败时，抛出一个异常
     *
     * @param message 用于{@link AssertionError} (<code>null</code>)的信息
     * @see AssertionError
     */
    private static void fail(String message) {
        if (message == null) {
            throw new AssertionError();
        }
        throw new AssertionError(message);
    }
}
