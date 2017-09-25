package com.dianrong.common.uniauth.server.filter;


import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.support.tree.TreeTypeHolder;

import javax.servlet.*;
import java.io.IOException;

/**
 * 清空ThreadLocal中的信息.
 */

public class HolderClearFilter implements Filter {
  @Override public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } finally {
      // 清空Holder信息
      CxfHeaderHolder.clearAllHolder();
      TreeTypeHolder.clear();
    }
  }

  @Override public void destroy() {

  }
}
