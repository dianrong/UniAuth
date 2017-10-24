package com.dianrong.common.uniauth.client.custom.auth;

import com.dianrong.common.uniauth.client.custom.filter.UniauthAbstractAuthenticationFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.OrderComparator;
import org.springframework.security.web.util.UrlUtils;

@Slf4j
public class UniauthAuthenticationFilterChain implements FilterChain {

  private final FilterChain originalChain;
  // Uniauth的身份认证的FilterList.
  private final List<UniauthAbstractAuthenticationFilter> additionalFilters;
  private final int size;
  private int currentPosition = 0;

  public UniauthAuthenticationFilterChain(FilterChain chain,
      List<UniauthAbstractAuthenticationFilter> additionalFilters) {
    this.originalChain = chain;
    this.additionalFilters = additionalFilters;
    this.size = additionalFilters.size();
  }

  public void doFilter(ServletRequest request, ServletResponse response)
      throws IOException, ServletException {
    if (currentPosition == size) {
      log.debug(UrlUtils.buildRequestUrl((HttpServletRequest) request)
          + " reached end of additional filter chain; proceeding with original chain");
      originalChain.doFilter(request, response);
    } else {
      currentPosition++;
      Filter nextFilter = additionalFilters.get(currentPosition - 1);
      log.debug(UrlUtils.buildRequestUrl((HttpServletRequest) request)
          + " at position " + currentPosition + " of " + size
          + " in additional filter chain; firing Filter: '"
          + nextFilter.getClass().getSimpleName() + "'");
      nextFilter.doFilter(request, response, this);
    }
  }
}
