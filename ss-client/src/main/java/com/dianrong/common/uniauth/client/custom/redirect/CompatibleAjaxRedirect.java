package com.dianrong.common.uniauth.client.custom.redirect;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.DefaultRedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 该跳转策略兼容处理Ajax请求.
 *
 * @author wanglin
 */
@Slf4j
public class CompatibleAjaxRedirect extends DefaultRedirectStrategy {

  private UniauthRedirectFormat redirectFormat = new SimpleRedirectFormat();

  @Override
  public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
      throws IOException {
    if (HttpRequestUtil.isAjaxRequest(request) || HttpRequestUtil.isCorsRequest(request)) {
      if (!response.isCommitted()) {
        log.warn("current request is a ajax request, redirect strategy "
            + "write json to response. the normal redirect url is {}", url);
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Cache-Control", "no-store");
        response.getWriter()
            .write(JsonUtil.object2Jason(redirectFormat.getRedirectInfo(request, url)));
        response.flushBuffer();
      }
      return;
    }
    super.sendRedirect(request, response, url);
  }

  public UniauthRedirectFormat getRedirectFormat() {
    return redirectFormat;
  }

  public void setRedirectFormat(UniauthRedirectFormat redirectFormat) {
    Assert.notNull(redirectFormat);
    this.redirectFormat = redirectFormat;
  }
}
