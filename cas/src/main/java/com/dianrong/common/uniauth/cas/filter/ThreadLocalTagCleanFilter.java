package com.dianrong.common.uniauth.cas.filter;

import com.dianrong.common.uniauth.cas.helper.StaffNoPersistTagHolder;
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
 * 清空各种ThreadLocal信息.
 *
 * @author wanglin
 */
@Slf4j
public class ThreadLocalTagCleanFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } finally {
      StaffNoPersistTagHolder.remove();
    }
  }

  @Override
  public void destroy() {
  }
}
