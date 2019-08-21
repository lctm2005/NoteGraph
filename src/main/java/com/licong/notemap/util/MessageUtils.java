package com.licong.notemap.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 信息的工具类
 * <p/>
 * 支持 <code>用户{0}，{1}登录成功！</code>以及 <code>用户{user},{datetime}登录成功！</code>不支持嵌套功能
 *
 * @author bifeng.liu
 */
public abstract class MessageUtils {

    /**
     * 默认的左边封闭字符
     */
    public static final String DEFAULT_LEFT_MARK = "{";
    /**
     * 默认的右边封闭字符
     */
    public static final String DEFAULT_RIGHT_MARK = "}";
    /**
     * 默认转义字符
     */
    public static final String DEFAULT_ESCAPE_MARK = "\\";

    /**
     * 通过参数数组传值并使用默认的字符分隔格式化消息，消息体里面支持{数值}方式
     *
     * @param message 要格式化的消息
     * @param args    参数
     * @return 格式化的消息
     */
    public static String format(String message, Object... args) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                parameters.put("" + i, args[i]);
            }
        }
        return format(message, parameters, DEFAULT_LEFT_MARK, DEFAULT_RIGHT_MARK);
    }

    /**
     * 通过参数数组传值并使用自定义的字符分隔格式化消息，消息体里面支持{数值}方式
     *
     * @param message   要格式化的消息
     * @param args      参数
     * @param leftMark
     * @param rightMark
     * @return 格式化的消息
     */
    protected static String format(String message, Object[] args, String leftMark, String rightMark) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                parameters.put("" + i, args[i]);
            }
        }
        return format(message, parameters, leftMark, rightMark);
    }

    /**
     * 通过参数Map传值并使用默认字符分隔格式化消息，消息体里面支持{Key}方式
     *
     * @param message 要格式化的消息
     * @param params  参数
     * @return 格式化的消息
     */
    public static String format(String message, Map params) {
        return format(message, params, DEFAULT_LEFT_MARK, DEFAULT_RIGHT_MARK);
    }

    /**
     * 通过参数Map传值并使用自定义的字符分隔格式化消息，消息体里面支持{Key}方式
     *
     * @param message   要格式化的字符串
     * @param params    参数
     * @param leftMark
     * @param rightMark
     * @return
     */
    @SuppressWarnings("unchecked")
    protected static String format(String message, Map params, String leftMark, String rightMark) {
        String result;
        // 如果没有参数，则直接返回
        if (params == null) {
            result = message;
            return result;
        }

        // 格式串为空，设置为源串
        if (message == null || message.length() == 0) {
            result = "";
            return result;
        }

        // 格式串不含格式符号，直接设置为格式串
        if (message.indexOf(leftMark) < 0 || message.indexOf(rightMark) < 0) {
            result = message;
            return result;
        }

        int startPos = 0;
        String temp = message;
        StringBuilder sb = new StringBuilder();
        while (true) {
            int leftPos = temp.indexOf(leftMark, startPos);
            int rightPos = temp.indexOf(rightMark, leftPos + leftMark.length());
            if (leftPos == -1 || rightPos == -1) { //如果没找到，则把剩余部分加入到字符串中
                sb.append(temp);
                break;
            }
            // 查找替换变换，如果找到，则替换变量字符串，下次查找从RightMark开始，如果没找到，则下次查找从LeftMark开始
            String variable = temp.substring(leftPos + leftMark.length(), rightPos);
            if (params.containsKey(variable)) {
                String tran = "";
                Object value = params.get(variable); // 变量替换
                if (value != null) {
                    tran = value.toString();
                }
                sb.append(temp.substring(0, leftPos));
                sb.append(tran);
                if (rightPos + rightMark.length() < temp.length()) {
                    temp = temp.substring(rightPos + rightMark.length());
                } else {
                    break;
                }
            } else {
                sb.append(temp.substring(0, leftPos + leftMark.length()));
                temp = temp.substring(leftPos + leftMark.length());
            }
        }
        result = sb.toString();
        return result;
    }
}
