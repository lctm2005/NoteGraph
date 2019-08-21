package com.licong.notemap.util;

/**
 * IP地址的工具类
 * <p/>
 * 支持从IP V4转换Long，Long转换成IP V4
 *
 * @author bifeng.liu
 */
public abstract class IpAddressUtils {

    public static final long IPV4_MAX = 4294967295L;

    /**
     * 将IPv4(127.0.0.1)形式的IP地址转换成十进制整数
     * <p/>
     * 处理算法：
     * 通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
     */
    public static long ipv4ToLong(String ipAddress) {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = ipAddress.indexOf(".");
        int position2 = ipAddress.indexOf(".", position1 + 1);
        int position3 = ipAddress.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(ipAddress.substring(0, position1));
        ip[1] = Long.parseLong(ipAddress.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(ipAddress.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(ipAddress.substring(position3 + 1));
        if (isUnValidIpv4Field(ip[0]) || isUnValidIpv4Field(ip[1])
                || isUnValidIpv4Field(ip[2]) || isUnValidIpv4Field(ip[3])) {
            throw new IllegalArgumentException("无效的IP地址[" + ipAddress + "]");
        }
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 将十进制整数形式转换成IPv4(127.0.0.1)形式的ip地址
     * <p/>
     * 处理算法：
     * 将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。
     * 通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。
     * 通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。
     * 通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。
     */
    public static String longToIpv4(long ipAddress) {
        if (ipAddress < 0 || ipAddress > IPV4_MAX) {
            throw new IllegalArgumentException("无效的数值[" + ipAddress + "]，数值必须在0-" + IPV4_MAX + "之间");
        }
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(String.valueOf((ipAddress >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((ipAddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((ipAddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((ipAddress & 0x000000FF)));
        return sb.toString();
    }

    /**
     * 是否为无效的IP v4的值
     *
     * @param field
     * @return
     */
    private static boolean isUnValidIpv4Field(long field) {
        return field < 0 || field > 255;
    }
}
