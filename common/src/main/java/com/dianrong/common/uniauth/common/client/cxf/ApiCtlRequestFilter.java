package com.dianrong.common.uniauth.common.client.cxf;

import com.dianrong.common.uniauth.common.client.cxf.ApiControlHeaderHolder.HeaderHolder;
import java.io.IOException;
import java.util.Map;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ApiCtlRequestFilter implements ClientRequestFilter {

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    HeaderHolder requestHeaderHolder = ApiControlHeaderHolder.getRequestHeaderHolder();
    Map<String, String> allHeader = requestHeaderHolder.getAllHeader();
    for (Map.Entry<String, String> entry : allHeader.entrySet()) {
      try {
        String key = entry.getKey();
        String value = entry.getValue();
        if (key != null && value != null) {
          requestContext.getStringHeaders().add(key, value);
        }
      } catch (Throwable t) {
        log.error("failed to set header " + entry.getKey(), t);
      }
    }
  }
}
