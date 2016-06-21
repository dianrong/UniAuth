package com.dianrong.common.uniauth.common.util;

import com.dianrong.common.uniauth.common.cons.AppConstants;

/**.
 * 用于处理zk的节点相关工具方法
 * @author wanglin
 */
public class ZkNodeUtils {
	
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
			return nodeName.trim().split(AppConstants.ZK_CFG_SPLIT)[1];
		}
		return null;
	}
	
	/**.
	 * 判断节点是否属于Domain的某种属性节点
	 * @param nodeName 节点
	 * @param propName 属性名
	 * @return
	 */
	public static boolean isDomainProp(final String nodeName, String propName) {
		if(nodeName == null || propName == null) {
			return false;
		}
		// eg . domains.techops
		String[] nodes = nodeName.trim().split(AppConstants.ZK_CFG_SPLIT);
		if(nodes.length == 3 && nodes[0].equals(AppConstants.ZK_DOMAIN) && nodes[3].equals(propName)) {
			return true;
		}
		return false;
	}
	
}
