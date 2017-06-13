package com.dianrong.common.uniauth.common.server.cxf.client.impl;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderKeyDefine;
import com.dianrong.common.uniauth.common.server.cxf.client.HeaderProducer;

public abstract class AbstractTenancyIdHeaderProducer implements HeaderProducer {

  @Override
  public String key() {
    return CxfHeaderKeyDefine.TENANCY_ID_KEY;
  }
}
