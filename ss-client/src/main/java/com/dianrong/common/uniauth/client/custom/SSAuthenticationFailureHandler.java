package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.client.custom.model.JsonResponseModel;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.common.util.ZkNodeUtils;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

/**
 * Cas生成st(service ticket)跳转到业务系统,业务系统拿到st去cas做验证的时候验证失败的处理.<br/>
 * 如：1.返回json告知失败;2.跳转到业务系统的认证失败页面；
 *
 * @author xiaofeng
 * @date 2016年12月15日
 * @see CasAuthenticationFilter#setAuthenticationFailureHandler(AuthenticationFailureHandler)
 * CasAuthenticationFilter#setAuthenticationFailureHandler
 * @since jdk1.7
 */

@Slf4j
public class SSAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;
  @Autowired(required = false)
  private DomainDefine domainDefine;

  /**
   * Service ticket 验证失败之后, 页面上显示Retry的链接地址.
   */
  private String retryUrl = "/";

  private boolean retryUrlIsValid = false;

  /**
   * 设置重试的URL.
   */
  public void setRetryUrl(String retryUrl) {
    if (StringUtils.hasText(retryUrl)) {
      this.retryUrl = retryUrl.trim();
      if (StringUtil.isValidUrl(this.retryUrl)) {
        retryUrlIsValid = true;
      }
    } else {
      log.warn("retry URL cant's be empty!");
    }
  }

  public void setAllZkNodeMap(Map<String, String> allZkNodeMap) {
    this.allZkNodeMap = allZkNodeMap;
  }

  public void setDomainDefine(DomainDefine domainDefine) {
    this.domainDefine = domainDefine;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.error("-----------Service Ticket Authentication Failed-------------------:{}",
        exception.getMessage());
    SecurityContextHolder.getContext().setAuthentication(null);
    if (HttpRequestUtil.isAjaxRequest(request) || HttpRequestUtil.isCorsRequest(request)) {
      response.setContentType("application/json;charset=UTF-8");
      response.addHeader("Cache-Control", "no-store");
      response.getWriter().write(
          JsonUtil.object2Jason(JsonResponseModel.failure("Service ticket validation failed")));
      response.flushBuffer();
    } else {
      String authFailureUrl = authFailureUrl();
      if (StringUtils.hasText(authFailureUrl)) {
        response.sendRedirect(authFailureUrl);
      } else {
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        if (retryUrlIsValid) {
          response.getWriter().print(
              "Service ticket validation failed.  <a href='" + this.retryUrl + "'>Retry</a>");
        } else {
          String relativePath = this.retryUrl;
          if (!this.retryUrl.startsWith("/")) {
            relativePath = "/" + relativePath;
          }
          response.getWriter().print(String
              .format("Service ticket validation failed.  <a href='%s%s'>Retry</a>",
                  request.getContextPath(), relativePath));
        }
        response.flushBuffer();
      }
    }
  }

  private String authFailureUrl() {
    String authFailureUrl = null;
    if (domainDefine != null) {
      String failUrlNodeKey = ZkNodeUtils.domainAuthFailUrlNodeKey(domainDefine.getDomainCode());
      authFailureUrl = allZkNodeMap.get(failUrlNodeKey);
    }
    return authFailureUrl;
  }

}
