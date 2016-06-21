package com.dianrong.common.uniauth.cas.action;

import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ZkNodeUtils;

/**.
 * 用于判断当前的登陆请求的处理方式
 * @author wanglin
 */
public class LoginTypeDecisionAction extends AbstractAction {
	  /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 默认的登陆方式，从cas的统一登陆页面登陆
	 */
	private static final String LOGIN_FROM_CAS = "cas";
	
	/**
	 * 从业务系统的登陆页面进行登陆
	 */
	private static final String LOGIN_FROM_CUSTOM = "custom";
	
	/**.
	 * zk 配置属性
	 */
	@Resource(name="uniauthConfig")
	private Map<String, String> allZkNodeMap;

	@Override
	protected Event doExecute(final RequestContext context) throws Exception {
		// 获取业务系统地址
        final Service service = WebUtils.getService(context);
        if(service != null && StringUtils.hasText(service.getId())) {
        	// 业务系统的地址
        	final String serviceUrl =  service.getId().trim();
        	// 通过业务地址反找其是否走自定制的页面
        	for(String domainCode: allZkNodeMap.keySet() ){
        		if(ZkNodeUtils.isDomainNode(domainCode)) {
        			String domainUrl = allZkNodeMap.get(domainCode);
        			if(domainUrl != null && serviceUrl.equals(domainUrl.trim())) {
        				// 查找其登陆页面
        				String loginPageNodeName = domainCode + AppConstants.ZK_CFG_SPLIT + AppConstants.ZK_DOMAIN_LOGIN_PAGE;
        				if(StringUtils.hasText(allZkNodeMap.get(loginPageNodeName))) {
        					// 将跳转url放到flowscope备用
        					context.getFlowScope().put(AppConstants.CAS_CUSTOM_LOGIN_PAGE_KEY, allZkNodeMap.get(loginPageNodeName));
        					logger.debug("custom login url:" + allZkNodeMap.get(loginPageNodeName));
        					return result(LOGIN_FROM_CUSTOM);
        				}
        			}
        		}
        	}
        }
        // 不处理
        return result(LOGIN_FROM_CAS);
	}
}