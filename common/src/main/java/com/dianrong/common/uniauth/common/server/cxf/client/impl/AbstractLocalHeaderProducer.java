package com.dianrong.common.uniauth.common.server.cxf.client.impl;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderKeyDefine;
import com.dianrong.common.uniauth.common.server.cxf.client.HeaderProducer;

public abstract class AbstractLocalHeaderProducer implements HeaderProducer {

  @Override
  public String key() {
    return CxfHeaderKeyDefine.LOCALE_KEY;
  }
}
