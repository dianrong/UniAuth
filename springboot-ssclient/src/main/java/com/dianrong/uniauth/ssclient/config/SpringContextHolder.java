package com.dianrong.uniauth.ssclient.config;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
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
 * 实现Servlet接口是为了让该bean提前被spring初始化
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, Servlet{

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

  // 该servlet 不提供对外的工作  只是为了让spring boot 提前初始化该bean
  @Override
  public void init(ServletConfig config) throws ServletException {
  }

  @Override
  public ServletConfig getServletConfig() {
    return null;
  }

  @Override
  public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
  }

  @Override
  public String getServletInfo() {
    return null;
  }

  @Override
  public void destroy() {
  }
}
