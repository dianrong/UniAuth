package com.dianrong.common.uniauth.common.util;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import java.util.Map;

/**
 * . 用于处理zk的节点相关工具方法
 *
 * @author wanglin
 */
public class ZkNodeUtils {

  // 常量
  private static final String DEFAULT_TRUE = "true";

  /**
   * . 判断nodeName是否属于域的node
   *
   * @param nodeName node名称
   */
  public static boolean isDomainNode(final String nodeName) {
    if (nodeName == null) {
      return false;
    }
    // eg . domains.techops
    String[] nodes = nodeName.trim().split("\\" + AppConstants.ZK_CFG_SPLIT);
    if (nodes.length == 2 && nodes[0].equals(AppConstants.ZK_DOMAIN)) {
      return true;
    }
    return false;
  }

  /**
   * . 从zkNode中解析出业务系统域的名称
   *
   * @param nodeName node名称
   */
  public static String getDomainName(final String nodeName) {
    if (isDomainNode(nodeName)) {
      return nodeName.trim().split("\\" + AppConstants.ZK_CFG_SPLIT)[1];
    }
    return null;
  }

  public static String getServiceKey(final String nodeName) {
    return AppConstants.ZK_DOMAIN_PREFIX + nodeName;
  }

  public static String getDomainCustomUrlNodeKey(final String nodeName) {
    return AppConstants.ZK_DOMAIN_PREFIX + nodeName + AppConstants.ZK_CFG_SPLIT
        + AppConstants.ZK_DOMAIN_LOGIN_PAGE;
  }

  /**
   * 判断节点是否是用于处理域在登陆首页显示的控制参数.
   */
  public static boolean isShowInHomePage(final String nodeName, Map<String, String> infoes) {
    String key = AppConstants.ZK_DOMAIN_PREFIX + nodeName + AppConstants.ZK_CFG_SPLIT
        + AppConstants.ZK_DOMAIN_SHOW_IN_HOME_PAGE;
    return DEFAULT_TRUE.equalsIgnoreCase(infoes.get(key));
  }

  /**
   * 获取业务系统在service ticket认证失败时配置跳转url的节点名称.
   *
   * @param domainName 域编号
   */
  public static String domainAuthFailUrlNodeKey(String domainName) {
    return AppConstants.ZK_DOMAIN_PREFIX + domainName + AppConstants.ZK_CFG_SPLIT
        + AppConstants.ZK_DOMAIN_AUTH_FAIL_URL;
  }

  /**
   * 按照配置规则计算出对应domain的登出地址的节点
   *
   * @param domainName 域名,比如techops, crm等. 不能为空
   * @return 返回在zk配置中对应domain的登出路径的节点. 比如: domains.techops.logout_address
   */
  public static String getDomainLogoutNodeKey(final String domainName) {
    Assert.notNull(domainName);
    String nodeKey = AppConstants.ZK_DOMAIN_PREFIX + domainName + AppConstants.ZK_CFG_SPLIT
        + AppConstants.ZK_DOMAIN_LOGOUT_ADDRESS_NODE_SUFFIX;
    return nodeKey;
  }
}
