package com.dianrong.common.uniauth.common.server.cxf.server.impl;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderKeyDefine;
import com.dianrong.common.uniauth.common.server.cxf.server.HeaderConsumer;

public abstract class AbstractLocalHeaderConsumer implements HeaderConsumer {

  @Override
  public String key() {
    return CxfHeaderKeyDefine.LOCALE_KEY;
  }
}
