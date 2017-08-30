package com.dianrong.common.uniauth.common.util;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 一些系统级的工具方法.
 *
 * @author wanglin
 */

@Slf4j
public class SystemUtil {

  public static final String UNKOWN_IP = "unknown_ip";

  /**
   * 获取当前机器的IP地址.
   */
  public static String getLocalIp() {
    Enumeration allNetInterfaces = null;
    try {
      allNetInterfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      log.warn("Failed to get Local Ip", e);
      return UNKOWN_IP;
    }
    while (allNetInterfaces.hasMoreElements()) {
      NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
      System.out.println(netInterface.getName());
      Enumeration addresses = netInterface.getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress ip = (InetAddress) addresses.nextElement();
        if (ip instanceof Inet4Address) {
          return ip.getHostAddress();
        }
      }
    }
    return UNKOWN_IP;
  }
}
