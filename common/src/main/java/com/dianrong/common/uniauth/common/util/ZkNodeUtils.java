package com.dianrong.common.uniauth.common.util;

import java.util.Map;

import com.dianrong.common.uniauth.common.cons.AppConstants;

/**
 * . 用于处理zk的节点相关工具方法
 * 
 * @author wanglin
 */
public class ZkNodeUtils {
	// 常量
	private static final String DEFAULT_TRUE = "true";

	/**.
	 * 判断nodeName是否属于域的node
	 * @param nodeName node名称
	 * @return
	 */
	public static boolean isDomainNode(final String nodeName) {
		if(nodeName == null) {
			return false;
		}
		// eg . domains.techops
		String[] nodes = nodeName.trim().split("\\"+AppConstants.ZK_CFG_SPLIT);
		if(nodes.length == 2 && nodes[0].equals(AppConstants.ZK_DOMAIN)) {
			return true;
		}
		return false;
	}
	
	/**.
	 * 从zkNode中解析出业务系统域的名称
	 * @param nodeName node名称
	 * @return
	 */
	public static String getDomainName(final String nodeName) {
		if(isDomainNode(nodeName)) {
			return nodeName.trim().split("\\"+AppConstants.ZK_CFG_SPLIT)[1];
		}
		return null;
	}
	
	
	public static String getDomainCustomUrlNodeKey(final String nodeName) {
		return AppConstants.ZK_DOMAIN_PREFIX + nodeName + AppConstants.ZK_CFG_SPLIT + AppConstants.ZK_DOMAIN_LOGIN_PAGE;
	}
	
	public static boolean IsShowInHomePage(final String nodeName, Map<String, String> infoes) {
		String key =  AppConstants.ZK_DOMAIN_PREFIX + nodeName + AppConstants.ZK_CFG_SPLIT + AppConstants.ZK_DOMAIN_SHOW_IN_HOME_PAGE;
		return DEFAULT_TRUE.equalsIgnoreCase(infoes.get(key));
	}
}
