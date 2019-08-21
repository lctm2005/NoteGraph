package com.licong.notemap.util;

/**
 * <p></p>
 *
 * @author bifeng.liu
 * @since 2015/11/25.
 */
public class URLUtils {

    public static final char HTTP_SEPARATOR = '/';
    public static final char PERIOD_SIGN = '?';
    public static final char AMPERSAND_SIGN = '&';
    public static final char EQUAL_SIGN = '=';


    /**
     * 连接URL，自动处理 "/" 的问题
     *
     * @param paths
     * @return
     */
    public static String combine(String... paths) {
        if (ArrayUtils.isEmpty(paths)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (StringUtils.isEmpty(path)) {
                continue;
            }
            if (sb.length() == 0) {
                sb.append(path);
                continue;
            }
            boolean hasLastSeparator = sb.charAt(sb.length() - 1) == HTTP_SEPARATOR;
            boolean hasFirstSeparator = path.charAt(0) == HTTP_SEPARATOR;
            if (!hasLastSeparator && !hasFirstSeparator) {
                sb.append(HTTP_SEPARATOR);
                sb.append(path);
            } else if (hasLastSeparator && hasFirstSeparator) {
                sb.append(path.substring(1));
            } else {
                sb.append(path);
            }
        }
        return sb.toString();
    }

    public static String combineParameter(String path, String parameterName, Object parameterValue) {
        if (path == null || StringUtils.isEmpty(parameterName)) {
            return path;
        }
        StringBuilder sb = new StringBuilder(path);
        if (path.indexOf(PERIOD_SIGN) == -1) {
            sb.append(PERIOD_SIGN);
        } else {
            sb.append(AMPERSAND_SIGN);
        }
        sb.append(parameterName);
        sb.append(EQUAL_SIGN);
        sb.append(parameterValue == null ? "" : parameterValue);
        return sb.toString();
    }


    public static String combineArrayParameter(String path, String parameterName, Iterable<?> parameterValues) {
        if (path == null || StringUtils.isEmpty(parameterName)) {
            return path;
        }
        StringBuilder sb = new StringBuilder(path);
        if (path.indexOf(PERIOD_SIGN) == -1) {
            sb.append(PERIOD_SIGN);
        } else {
            sb.append(AMPERSAND_SIGN);
        }
        if (parameterValues != null && parameterValues.iterator().hasNext()) {
            while (parameterValues.iterator().hasNext()) {
                Object value = parameterValues.iterator().next();
                sb.append(parameterName).append(EQUAL_SIGN).append(value);
                sb.append(AMPERSAND_SIGN);
            }
            return sb.toString().substring(0, sb.length() - 1);
        } else {
            sb.append(parameterName);
            sb.append(EQUAL_SIGN);
            return sb.toString();
        }
    }

    public static void main(String[] args) {
//        System.out.println(concat("http://1.1.1.1:8080", "aaaa", "cccc"));
//        System.out.println(concat("http://1.1.1.1:8080/", "/aaaa", "/cccc/"));
//        System.out.println(concat("http://1.1.1.1:8080/", "aaaa/", "/cccc/"));
//        System.out.println(concat(null, "aaaa/", "/cccc/"));

        System.out.println(combineParameter("", "aaaa", "cccc"));
        System.out.println(combineParameter("/", "aaaa", "cccc"));
        System.out.println(combineParameter("http://1.1.1.1:8080", "aaaa", "cccc"));
        System.out.println(combineParameter("http://1.1.1.1:8080?1=1", null, "12313"));
        System.out.println(combineParameter("http://1.1.1.1:8080?1=3&1=2", "aaaa", "cccc"));
        System.out.println(combineParameter("http://1.1.1.1:8080?1=1&", "aaaa", "cccc"));
        System.out.println(combineParameter("http://1.1.1.1:8080?1111", "aaaa", "cccc"));
    }
}
