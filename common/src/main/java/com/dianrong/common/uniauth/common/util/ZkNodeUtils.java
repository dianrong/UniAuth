package com.dianrong.common.uniauth.common.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.cons.AppConstants;

/**
 * . 用于处理zk的节点相关工具方法
 * 
 * @author wanglin
 */
public class ZkNodeUtils {
	private static final Logger logger = LoggerFactory.getLogger(ZkNodeUtils.class);
	// 常量
	private static final String DEFAULT_TRUE = "true";

	/**
	 * . 判断是否是属于对应租户的域名node
	 * 
	 * @param nodeName
	 * @param tenancyCode
	 *            租户code
	 * @param isDefaultTenancy
	 *            是否是默认租户code
	 * @return
	 */
	public static boolean isDomainNode(final String nodeName, final String tenancyCode, boolean isDefaultTenancy) {
		if (nodeName == null || tenancyCode == null) {
			return false;
		}

		if (isDefaultTenancy) {
			// default tenancy
			String[] nodes = nodeName.trim().split("\\" + AppConstants.ZK_CFG_SPLIT);
			if (nodes.length == 2 && nodes[0].equals(AppConstants.ZK_DOMAIN)) {
				return true;
			}
		}
		if (nodeName.trim().startsWith(tenancyCode.trim())) {
			String[] nodes = nodeName.trim().split("\\" + AppConstants.ZK_CFG_SPLIT);
			if (nodes.length == 3 && nodes[1].equals(AppConstants.ZK_DOMAIN)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * . 从zkNode中解析出业务系统域的名称
	 * 
	 * @param nodeName
	 *            node名称
	 * @return
	 */
	public static String getDomainName(final String nodeName) {
		String[] nodes = nodeName.trim().split("\\" + AppConstants.ZK_CFG_SPLIT);
		if (nodes.length == 2 && nodes[0].equals(AppConstants.ZK_DOMAIN)) {
			return nodes[1];
		}
		if (nodes.length == 3 && nodes[1].equals(AppConstants.ZK_DOMAIN)) {
			return nodes[2];
		}
		return null;
	}

	public static String getDomainTCustomLoginUrl(final String domainName, final String tenancyCode, boolean isDefaultTenancy, Map<String, String> infoes) {
		return getDomainProp(domainName, tenancyCode, isDefaultTenancy, infoes,  AppConstants.ZK_DOMAIN_LOGIN_PAGE);
	}

	public static boolean canShowInHomePage(final String domainName, final String tenancyCode, boolean isDefaultTenancy, Map<String, String> infoes) {
		return DEFAULT_TRUE.equalsIgnoreCase(getDomainProp(domainName, tenancyCode, isDefaultTenancy, infoes,  AppConstants.ZK_DOMAIN_SHOW_IN_HOME_PAGE));
	}
	
	public static String getDomainUrl(final String domainName, final String tenancyCode, boolean isDefaultTenancy, Map<String, String> infoes) {
		return getDomainProp(domainName, tenancyCode, isDefaultTenancy, infoes,  null);
	}
	
	private static String getDomainProp(final String domainName, final String tenancyCode, boolean isDefaultTenancy, Map<String, String> infoes, String prop) {
		if (infoes == null || infoes.isEmpty()) {
			logger.warn("uniauthConfig is empty or null, please check zookeeper config");
			return "";
		}
		if (domainName == null || tenancyCode == null) {
			return "";
		}
		String key = AppConstants.ZK_DOMAIN_PREFIX + domainName;
		key += StringUtils.hasText(prop) ?  AppConstants.ZK_CFG_SPLIT + prop : "";
		if (isDefaultTenancy) {
			 String val = infoes.get(key);
			 if ( val != null) {
				 return val;
			 }
		}
		key = tenancyCode.trim() + AppConstants.ZK_CFG_SPLIT + key;
		return infoes.get(key) == null ? "" : infoes.get(key);
	}
}
