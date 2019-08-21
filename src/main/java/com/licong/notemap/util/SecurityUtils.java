package com.licong.notemap.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 安全方面的工具类，提供一些较为通用的加密解密算法
 *
 * @author bifeng.liu
 */
public abstract class SecurityUtils {
    /**
     * 默认的字符集
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * 使用BASE64加密字符串，默认使用分块
     * <p/>
     * 当加密字符串超过76个字符时，字符串会被换行
     *
     * @param plainText
     * @return 加密后的字符串
     */
    public static String encode4Base64(String plainText) {
        return encode4Base64(plainText, true);
    }

    /**
     * 使用BASE64加密字符串
     * <p/>
     * 当isChunked为true是，会使用分块，当加密字符串超过76个字符时，字符串会被换行
     *
     * @param plainText
     * @param isChunked 是否分块
     * @return 加密后的字符串
     */
    public static String encode4Base64(String plainText, boolean isChunked) {
        if (plainText == null) {
            return null;
        }
        return new String(Base64.encodeBase64(plainText.getBytes(DEFAULT_CHARSET), isChunked),DEFAULT_CHARSET);
    }

    /**
     * 使用BASE64解密加密的字符串
     *
     * @param value
     * @return 解密后的字符串
     */
    public static String decode4Base64(String value) {
        if (value == null) {
            return null;
        }
        return new String(Base64.decodeBase64(value.getBytes(DEFAULT_CHARSET)),DEFAULT_CHARSET);
    }

    /**
     * 使用MD5加密字符串，默认编码(UTF-8)
     *
     * @param plainText
     * @return 加密后的字符串
     */
    public static String getMD5Digest(String plainText) {
        return getMD5Digest(plainText, DEFAULT_CHARSET);
    }

    /**
     * 使用MD5加密字符串。自定义编码
     *
     * @param plainText
     * @param charset   编码
     * @return 加密后的字符串
     */
    public static String getMD5Digest(String plainText, String charset) {
        return getMD5Digest(plainText, Charset.forName(charset));
    }

    /**
     * 使用MD5加密字符串。自定义编码
     *
     * @param plainText
     * @param charset   编码
     * @return 加密后的字符串
     */
    public static String getMD5Digest(String plainText, Charset charset) {
        if (plainText == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(plainText.getBytes(charset));
            byte[] val = md5.digest();
            return toPlusHex(val);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 把字节列表转换成16进制字符串
     *
     * @param data
     * @return
     */
    private static String toPlusHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        int by = 0;
        for (int offset = 0; offset < data.length; offset++) {
            by = data[offset];
            if (by < 0) {
                by += 256;
            }
            if (by < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(by));
        }
        return buf.toString();
    }
}
