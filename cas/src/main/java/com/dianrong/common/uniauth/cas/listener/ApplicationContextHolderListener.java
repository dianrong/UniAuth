package com.dianrong.common.uniauth.cas.listener;

import com.dianrong.common.uniauth.cas.util.SpringContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 初始化完全的listener
 *
 * @author wanglin
 */
@Component("applicationContextHolderListener")
public class ApplicationContextHolderListener implements ApplicationContextAware {

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextHolder.injectApplicationContext(applicationContext);
  }
}
