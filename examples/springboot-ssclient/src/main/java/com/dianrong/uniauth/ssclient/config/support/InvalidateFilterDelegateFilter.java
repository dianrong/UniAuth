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
  
  private final Filter delegatedFilter;
  
  private boolean invalidateFilter = true;
  
  public InvalidateFilterDelegateFilter(Filter delegatedFilter) {
    Assert.notNull(delegatedFilter);
    this.delegatedFilter = delegatedFilter;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (invalidateFilter) {
      chain.doFilter(request, response);
    } else {
      delegatedFilter.doFilter(request, response, chain);
    }
  }

  @Override
  public void destroy() {
  }
}
