package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Uniauth的ajax请求返回结果实现.
 *
 * @author wanglin
 */
public class UniauthAjaxResponseProcessor implements AjaxResponseProcessor {

  private ZooKeeperConfig zooKeeperConfig;

  /**
   * 必须指定zooKeeperConfig.
   *
   * @param zooKeeperConfig zooKeeperConfig
   */
  public UniauthAjaxResponseProcessor(ZooKeeperConfig zooKeeperConfig) {
    Assert.notNull(zooKeeperConfig);
    this.zooKeeperConfig = zooKeeperConfig;
  }

  @Override
  public void sendAjaxResponse(HttpServletRequest request, HttpServletResponse response,
      CustomizedRedirectFormat customizedRedirectFormat) throws IOException {
    String casServerUrl = zooKeeperConfig.getCasServerUrl();
    String domainUrl = zooKeeperConfig.getDomainUrl();
    domainUrl += "/login/cas";
    domainUrl = HttpRequestUtil.encodeUrl(domainUrl);
    casServerUrl = casServerUrl.endsWith("/") ? casServerUrl + "login" : casServerUrl + "/login";
    String loginUrl = casServerUrl + "?service=" + domainUrl;

    response.setContentType("application/json");
    response.addHeader("Cache-Control", "no-store");
    response.setStatus(200);
    Object redirectObj;
    if (customizedRedirectFormat == null) {
      redirectObj = new SimpleRedirectFormat().getRedirectInfo(request, loginUrl);
    } else {
      redirectObj = customizedRedirectFormat.getRedirectInfo(request, loginUrl);
    }
    if (redirectObj != null) {
      response.getWriter().println(JsonUtil.object2Jason(redirectObj));
    }
  }
}
