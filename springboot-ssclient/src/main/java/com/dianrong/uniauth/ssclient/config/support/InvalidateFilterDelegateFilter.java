package com.dianrong.uniauth.ssclient.config.support;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.util.Assert;

/**.
 * 代理其他filter,使其filter失效
 * @author wanglin
 */
public class InvalidateFilterDelegateFilter implements Filter{
  
  private final Filter souceFilter;
  
  private boolean invalidateFilter = true;
  
  public InvalidateFilterDelegateFilter(Filter sourceFilter) {
    Assert.notNull(sourceFilter);
    this.souceFilter = sourceFilter;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (invalidateFilter) {
      chain.doFilter(request, response);
    } else {
      souceFilter.doFilter(request, response, chain);
    }
  }

  @Override
  public void destroy() {
  }
}
