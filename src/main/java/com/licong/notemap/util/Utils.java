package com.licong.notemap.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * 工具类，提供一些较为通用的方法
 *
 * @author bifeng.liu
 */
public abstract class Utils {

    /**
     * 把字符转换成Boolean，
     * <p/>
     * 当为true/yes/ok/1字符串时，返回true,否则返回false，不区分大小写
     *
     * @param s
     * @return
     */
    public static boolean toBoolean(String s) {
        if (s == null || s.length() == 0) return false;
        return s.compareToIgnoreCase("TRUE") == 0 || s.compareToIgnoreCase("YES") == 0
                || s.compareToIgnoreCase("OK") == 0 || s.compareToIgnoreCase("1") == 0;
    }

    /**
     * 把字符串转换成Integer类型，如果转换出错，则抛出异常
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static int toInt(String s) throws ParseException {
        DecimalFormat df = new DecimalFormat();
        return df.parse(s).intValue();
    }

    /**
     * 把字符串转换成Integer类型，如果转换出错，则返回0
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static int toInt(String s, int defaultValue) {
        if (s == null || s.length() == 0) {
            return defaultValue;
        }
        try {
            return toInt(s);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    /**
     * 把字符串转换成Long类型，如果转换出错，则抛出异常
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static long toLong(String s) throws ParseException {
        DecimalFormat df = new DecimalFormat();
        return df.parse(s).longValue();
    }

    /**
     * 把字符串转换成Long类型，如果转换出错，则返回0
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static long toLong(String s, long defaultValue) {
        if (s == null || s.length() == 0) {
            return defaultValue;
        }
        try {
            return toLong(s);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    /**
     * 把字符串转换成Double类型，如果转换出错，则抛出异常
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static double toDouble(String s) {
        BigDecimal dec = new BigDecimal(s);
        return dec.doubleValue();
    }

    /**
     * 把字符串转换成Double类型，如果转换出错，则返回0
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static double toDouble(String s, double defaultValue) {
        if (s == null || s.length() == 0) {
            return defaultValue;
        }
        try {
            return toDouble(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

