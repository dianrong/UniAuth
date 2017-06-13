package com.dianrong.common.uniauth.common.client.cxf;

import com.dianrong.common.uniauth.common.client.cxf.ApiControlHeaderHolder.HeaderHolder;
import java.io.IOException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ApiCtlResponseFilter implements ClientResponseFilter {

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
      throws IOException {
    HeaderHolder responseHeaderHolder = ApiControlHeaderHolder.getResponseHeaderHolder();
    // clear response holder
    responseHeaderHolder.remove();

    for (String key : responseHeaderHolder.getAllKeys()) {
      try {
        String value = responseContext.getHeaderString(key);
        log.debug("get header: " + key + ":" + value);
        responseHeaderHolder.set(key, value);
      } catch (Throwable t) {
        log.error("failed to get header " + key, t);
      }
    }
  }
}
