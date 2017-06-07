package com.dianrong.common.uniauth.cas.service.support;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.common.server.cxf.client.impl.AbstractTenancyIdHeaderProducer;
import org.springframework.stereotype.Component;

@Component
public class TenancyIdHeaderProducer extends AbstractTenancyIdHeaderProducer {

  @Override
  public String produce() {
    Object tenancyId = CxfHeaderHolder.TENANCYID.get();
    return tenancyId == null ? null : tenancyId.toString();
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
