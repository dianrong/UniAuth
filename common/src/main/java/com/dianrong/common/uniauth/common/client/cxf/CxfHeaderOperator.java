package com.dianrong.common.uniauth.common.client.cxf;

import com.dianrong.common.uniauth.common.apicontrol.StringHeaderValueOperator;
import com.dianrong.common.uniauth.common.client.cxf.ApiControlHeaderHolder.HeaderHolder;
import com.dianrong.common.uniauth.common.client.cxf.exp.InvalidHeaderKeyException;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 操作cxf的header.
 *
 * @author wanglin
 */
public class CxfHeaderOperator implements StringHeaderValueOperator {

  @Override
  public String getHeader(String key) {
    Assert.notNull(key);
    HeaderHolder headerHolder = getHeaderHolder(key);
    return headerHolder.get(key);
  }

  @Override
  public void setHeader(String key, String value) {
    Assert.notNull(key);
    HeaderHolder headerHolder = getHeaderHolder(key);
    headerHolder.set(key, value);
  }

  private HeaderHolder getHeaderHolder(String key) {
    HeaderHolder requestHeaderHolder = ApiControlHeaderHolder.getRequestHeaderHolder();
    if (requestHeaderHolder.getAllKeys().contains(key)) {
      return requestHeaderHolder;
    }
    HeaderHolder responseHeaderHolder = ApiControlHeaderHolder.getResponseHeaderHolder();
    if (responseHeaderHolder.getAllKeys().contains(key)) {
      return responseHeaderHolder;
    }
    throw new InvalidHeaderKeyException();
  }
}