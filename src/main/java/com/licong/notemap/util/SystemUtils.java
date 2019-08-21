package com.licong.notemap.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public abstract class SystemUtils {


    /**
     * 环回地址
     */
    private static final String LOOPBACK_IPADDRESS = "127.0.0.1";

    /**
     * 获取本地主机上绑定的所有IP地址
     *
     * @return 本地主机上绑定的所有IP地址
     * @throws java.net.SocketException
     */
    public static final List<String> getLocalIps() throws SocketException {
        List<String> ips = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces != null && interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses != null && addresses.hasMoreElements()) {
                InetAddress addresse = addresses.nextElement();
                String ip = addresse.getHostAddress();
                if (!LOOPBACK_IPADDRESS.equals(ip)) {
                    ips.add(ip);
                }
            }
        }
        return ips;
    }

    /**
     * 取得当前服务器的IP地址
     *
     * @return
     */
    public static String getLocalHostIp() throws UnknownHostException {
        StringBuilder ipAddress = new StringBuilder();
        InetAddress addr = InetAddress.getLocalHost();
        byte[] ipAddr = addr.getAddress();
        int i = 0;
        for (; i < ipAddr.length - 1; i++) {
            ipAddress.append(ipAddr[i] & 0xFF);
            ipAddress.append(".");
        }
        ipAddress.append(ipAddr[i] & 0xFF);
        return ipAddress.toString();
    }

    /**
     * 取得当前服务器的名称
     *
     * @return
     */
    public static String getLocalHostName() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostName();
    }

    public static void main(String args[]) {
        try {
            System.out.println(getLocalIps().toArray()[0]);
            System.out.println(getLocalHostIp());
            System.out.println(getLocalHostName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
