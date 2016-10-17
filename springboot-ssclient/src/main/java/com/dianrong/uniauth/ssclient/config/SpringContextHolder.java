package com.dianrong.uniauth.ssclient.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**.
 * 获取applicationContext的引用
 * @author wanglin
 * 实现filter接口是为了让该bean提前被spring初始化
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, Filter{

  private static volatile ApplicationContext applicationContext;
  
  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    synchronized (SpringContextHolder.class) {
      applicationContext = context;
      SpringContextHolder.class.notifyAll();
    }
  }

  public static ApplicationContext getApplicationContext() {
    if (applicationContext == null) {
        synchronized (SpringContextHolder.class) {
          while(applicationContext == null) {
            try {
              SpringContextHolder.class.wait();
            } catch (InterruptedException e) {
              Thread.interrupted();
            }
          }
        }
    }
    return applicationContext;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
}
