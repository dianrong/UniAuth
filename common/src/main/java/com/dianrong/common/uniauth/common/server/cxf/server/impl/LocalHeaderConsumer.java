package com.dianrong.common.uniauth.common.server.cxf.server.impl;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * . 消费local header的默认实现
 *
 * @author wanglin
 */
@Component
public class LocalHeaderConsumer extends AbstractLocalHeaderConsumer {

  @Override
  public void consume(String localeStr) {
    if (localeStr != null) {
      UniauthLocaleInfoHolder.setLocale(StringUtils.parseLocaleString(localeStr));
    } else {
      // set default
      UniauthLocaleInfoHolder.setLocale(Locale.getDefault());
    }
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
