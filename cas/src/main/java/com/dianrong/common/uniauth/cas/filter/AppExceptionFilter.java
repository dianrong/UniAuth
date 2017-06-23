package com.dianrong.common.uniauth.cas.filter;

import com.dianrong.common.uniauth.cas.model.vo.ApiResponse;
import com.dianrong.common.uniauth.cas.model.vo.ResponseCode;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 * 用于处理公共异常信息的filter.
 *
 * @author wanglin
 */
@Slf4j
public class AppExceptionFilter implements Filter {

  /**
   * 国际化资源对象.
   */
  @Autowired
  private MessageSource messageSource;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } catch (Exception ex) {
      log.error("server internal error", ex);
      // server error
      WebScopeUtil.writeJsonContentToResponse(response,
          JsonUtil.object2Jason(ApiResponse.failure(ResponseCode.SERVER_INTERNAL_ERROR,
              UniBundleUtil.getMsg(messageSource, "application.server.internal.error"))));
    }
  }

  @Override
  public void destroy() {
  }
}
