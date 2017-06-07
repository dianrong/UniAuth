package com.dianrong.common.uniauth.common.server.cxf.client.impl;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;
import org.springframework.stereotype.Component;

/**
 * . 默认实现
 *
 * @author wanglin
 */
@Component
public class LocalHeaderProducer extends AbstractLocalHeaderProducer {

  @Override
  public String produce() {
    return UniauthLocaleInfoHolder.getLocale().toString();
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
