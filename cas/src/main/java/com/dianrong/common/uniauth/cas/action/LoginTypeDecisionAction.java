package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.util.CasConstants;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.util.ZkNodeUtils;

import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * . 用于判断当前的登陆请求的处理方式
 *
 * @author wanglin
 */
@Slf4j
public class LoginTypeDecisionAction extends AbstractAction {

  /**
   * 默认的登陆方式，从cas的统一登陆页面登陆.
   */
  private static final String LOGIN_FROM_CAS = "cas";

  /**
   * 从业务系统的登陆页面进行登陆.
   */
  private static final String LOGIN_FROM_CUSTOM = "custom";

  /**
   * . zk 配置属性
   */
  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  @Override
  protected Event doExecute(final RequestContext context) throws Exception {
    // 获取业务系统地址
    final Service service = WebUtils.getService(context);
    if (service != null && StringUtils.hasText(service.getId())) {
      // 业务系统的service
      final String serviceUrl = service.getId().trim();
      // 参数域名编码
      final String code = WebUtils.getHttpServletRequest(context)
          .getParameter(CasConstants.LOGIN_PAGE_REQUEST_CODE_KEY);
      String destService = null;
      String tempService = null;
      // 遍历整个域名配置,查找最匹配的service.
      // 根据service和code来匹配.
      // 1 service 和 code 都匹配,则选定是为该指定的service
      // 2 只是service匹配:
      // 2.1 优先使用没有自定义登陆页面的第一个service
      // 2.2 使用有自定义登陆页面的第一个service
      for (Map.Entry<String, String> entry : allZkNodeMap.entrySet()) {
        String key = entry.getKey();
        if (ZkNodeUtils.isDomainNode(key)) {
          String domainUrl = allZkNodeMap.get(key);
          if (WebScopeUtil.judgeTwoServiceIsEqual(domainUrl, serviceUrl)) {
            String domainName = ZkNodeUtils.getDomainName(key);
            if (StringUtils.hasText(code) && code.trim().equals(domainName)) {
              // service 和 code 都匹配
              destService = domainName;
              break;
            } else {
              if (destService == null) {
                String loginPageUrl = null;
                String loginPageNodeName = ZkNodeUtils.getDomainCustomUrlNodeKey(domainName);
                if (loginPageNodeName != null) {
                  loginPageUrl = allZkNodeMap.get(loginPageNodeName);
                }
                // 如果能找到自定义登陆页面,只能作为一个临时的service使用
                // 如果能找到一个没有配置自定义登陆页面的,则采用没有自定义页面的service作为最终的service
                if (loginPageUrl != null) {
                  if (tempService == null) {
                    tempService = domainName;
                  }
                } else {
                  destService = domainName;
                }
              }
            }
          }
        }
      }

      // 如果destService没有找到,则采用有自定义登陆页面的service
      destService = destService != null ? destService : tempService;
      if (destService != null) {
        // 查找其登陆页面
        String loginPageNodeName = ZkNodeUtils.getDomainCustomUrlNodeKey(destService);
        if (StringUtils.hasText(allZkNodeMap.get(loginPageNodeName))) {
          // 将跳转url放到flowscope备用
          context.getFlowScope().put(CasConstants.CAS_CUSTOM_LOGIN_PAGE_KEY,
              allZkNodeMap.get(loginPageNodeName));
          log.debug("custom login url:" + allZkNodeMap.get(loginPageNodeName));
          return result(LOGIN_FROM_CUSTOM);
        }
      }
    }
    // 不处理
    return

    result(LOGIN_FROM_CAS);
  }
}
