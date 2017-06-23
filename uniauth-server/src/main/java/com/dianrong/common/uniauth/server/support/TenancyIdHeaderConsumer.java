package com.dianrong.common.uniauth.server.support;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.common.server.cxf.server.impl.AbstractTenancyIdHeaderConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TenancyIdHeaderConsumer extends AbstractTenancyIdHeaderConsumer {

  @Override
  public void consume(String value) {
    try {
      Long tenancyId = Long.parseLong(value);
      CxfHeaderHolder.TENANCYID.set(tenancyId);
    } catch (NumberFormatException ex) {
      log.error("value [" + value + "] is a  invalid  format number!");
      CxfHeaderHolder.TENANCYID.set(null);
      log.debug("request tenancyId is null");
    }
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
