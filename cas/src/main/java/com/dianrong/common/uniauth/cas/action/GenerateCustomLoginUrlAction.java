package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.service.CfgService;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import javax.servlet.http.HttpServletRequest;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.webflow.execution.RequestContext;

/**
 * . 用于生成用户的登陆页面的url 存放到flowscope中
 *
 * @author wanglin
 */
public class GenerateCustomLoginUrlAction {

  @Autowired
  private CfgService cfgService;

  /**
   * Generate the login ticket.
   *
   * @param context the context
   * @return <code>"generated"</code>
   */
  public final String generate(final RequestContext context) {
    // 获取lt
    final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
    String lt = (String) request.getSession().getAttribute(AppConstants.CAS_CUSTOM_LOGIN_LT_KEY);
    // 获取客户自定义页面url
    String baseUrl = (String) context.getFlowScope().get(AppConstants.CAS_CUSTOM_LOGIN_PAGE_KEY);
    // 以上的值 必须都存在
    if (baseUrl.contains("?")) {
      baseUrl += "&";
    } else {
      baseUrl += "?";
    }
    // lt
    baseUrl += "lt=" + lt;
    // captcha
    if (WebScopeUtil.loginNeedCaptcha(request.getSession())) {
      baseUrl += "&captchapath=" + cfgService.getCaptchaAbsolutePath();
    }
    context.getFlowScope().put(AppConstants.CAS_CUSTOM_LOGIN_URL_KEY, baseUrl);
    return "generated";
  }
}
