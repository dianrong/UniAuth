package com.dianrong.common.uniauth.server.filter;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * 负责将ThreadLocal中的tenancy identity清理.
 *
 * @author wanglin
 */
public class TenancyIdentityClearFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } finally {
      CxfHeaderHolder.clearAllHolder();
    }
  }

  @Override
  public void destroy() {
  }
}
