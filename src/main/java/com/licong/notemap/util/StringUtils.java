package com.licong.notemap.util;

import java.util.UUID;

/**
 * 字符串工具类
 * <p/>
 *
 * @author bifeng.liu
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 默认单词分割字符<code>"_"</code>
     */
    public static final char DEFAULT_WORD_SEPARATOR_CHAR = '_';

    /**
     * 检查提供的CharSequence长度是否为0
     * <p><pre class="code">
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str 要检查的字符
     * @return {@code true} 如果CharSequence不为null且有值
     * @see #hasText(CharSequence)
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }


    /**
     * 检查提供的CharSequence存在文本(会掉空白文本、如回车、空格等)
     * <p><pre class="code">
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str 要检查的字符
     * @return {@code true} 如果CharSequence不为null且不全是空白字符
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从首位置开始，根据限制长度截取全角字符串
     * <p/>
     * 其中截取长度的计算：全角字符算两个字符，全角字符包括汉字等
     *
     * @param str         要截取的字符串
     * @param limitLength 截取的长度
     * @return 截取后的字符串
     * @see #subSBCString(String, int, int)
     */
    public static String subSBCString(String str, int limitLength) {
        return subSBCString(str, 0, limitLength);
    }

    /**
     * 从传入的位置开始，根据限制长度截取全角字符串
     * <p/>
     * 其中截取长度的计算：全角字符算两个字符，全角字符包括汉字等
     *
     * @param str         要截取的字符串
     * @param startIndex  开始位置
     * @param limitLength 截取的长度
     * @return 截取后的字符串
     */
    public static String subSBCString(String str, int startIndex, int limitLength) {
        if (!hasLength(str) || limitLength <= 0 || startIndex >= str.length()) {
            return "";
        }
        startIndex = startIndex < 0 ? 0 : startIndex;
        String doStr = str.substring(startIndex);
        int byteLen = 0; // 将汉字转换成两个字符后的字符串长度
        int strPos = 0;  // 对原始字符串截取的长度
        byte[] strBytes = null;
        try {
            strBytes = doStr.getBytes("gbk");// 将字符串转换成字符数组
        } catch (Exception ex) {
            strBytes = doStr.getBytes();
        }
        for (int i = 0; i < strBytes.length; i++) {
            if (strBytes[i] >= 0) {
                byteLen = byteLen + 1;
            } else {
                byteLen = byteLen + 2;// 一个汉字等于两个字符
                i++;
            }
            strPos++;

            if (byteLen >= limitLength) {
                if (strBytes[byteLen - 1] < 0) {
                    strPos--;
                }
                return doStr.substring(0, strPos);
            }
        }
        return doStr;
    }

    /**
     * 生成UUID
     * <p/>
     * 当hasSymbol为false时，返回字符串格式为32位16进制数字
     * 当hasSymbol为true时，返回字符串格式为：xxxxxxxx-xxxx-xxxx-xxxxxx-xxxxxxxxxx (8-4-4-4-12)
     *
     * @return
     */
    public static String generateUUID(boolean hasSymbol) {
        UUID uuid = UUID.randomUUID();
        String result = uuid.toString();
        return hasSymbol ? result : result.replace("-", "");
    }

    /**
     * 把字符串转换成JSON格式
     *
     * @param str
     * @return
     */
    public static String toJsonQuote(String str) {
        if ((str == null) || (str.length() == 0)) {
            return "\"\"";
        }
        if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
            return str.toLowerCase();
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(len + 4);

        sb.append('"');
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            switch (c) {
                case '"':
                case '/':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if ((c < ' ') || (c >= '')) {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u");
                        sb.append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
                    // no break
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * 使用默认的分隔符，转换成驼峰的显示方式
     *
     * @param str
     * @return
     */
    public static String toCamelCase(String str) {
        return toCamelCase(str, DEFAULT_WORD_SEPARATOR_CHAR);
    }

    /**
     * 使用字符串分隔符转换成驼峰显示方式
     *
     * @param str
     * @param separatorChar 分隔符
     * @return
     */
    public static String toCamelCase(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str.length());
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == separatorChar) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 把驼峰的显示方式转换成用某字符分隔的字符串
     *
     * @param str
     * @param separatorChar 分隔符
     * @return
     */
    public static String revertCamelCase(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean nextUpperCase = true;
            if (i < (str.length() - 1)) {
                nextUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) {
                        sb.append(separatorChar);
                    }
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
