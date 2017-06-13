package org.springframework.security.web.access.sessionmanage;

import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.session.InvalidSessionStrategy;

/**
 * 记录请求的上下文信息.
 *
 * @author wanglin
 */
public final class RequestCacheInvalidSessionStrategy implements InvalidSessionStrategy {

  /**
   * . RequestCache
   */
  private RequestCache requestCache = new HttpSessionRequestCache();

  @Override
  public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // ajax请求的上下文不记录
    if (!(HttpRequestUtil.isAjaxRequest(request) || HttpRequestUtil.isCorsRequest(request))) {
      requestCache.saveRequest(request, response);
    }
  }
}
