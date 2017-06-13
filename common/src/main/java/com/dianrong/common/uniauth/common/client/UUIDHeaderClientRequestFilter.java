package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Arc on 12/7/2016.
 */
@Provider
// 10 means the provider's priority is very high, log first before every request, log first after
// every response.
// in jax-rs, If you don't config the priority the default value is 5000.
@Priority(10)
@Slf4j
public class UUIDHeaderClientRequestFilter implements ClientRequestFilter, ClientResponseFilter {

  private static final String REQ_START_TIME = "reqStartTime";
  private static final Long SLOW_CALL_MILLIS = 1000L;

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    String uuid = UUID.randomUUID().toString();
    List<Object> objs = new LinkedList<>();
    objs.add(uuid);
    requestContext.getHeaders().put(AppConstants.API_UUID, objs);
    requestContext.setProperty(REQ_START_TIME, System.currentTimeMillis());
    String requestUri = requestContext.getUri().toString();
    String method = requestContext.getMethod();
    log.info(
        "Starting sdk call--- " + method + " -> uri: " + requestUri + " UUID: " + uuid + " ---");
  }

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    Long startTime = (Long) requestContext.getProperty(REQ_START_TIME);
    Long consumeTime = System.currentTimeMillis() - startTime;
    String uuid = (String) requestContext.getHeaders().get(AppConstants.API_UUID).get(0);
    String requestUri = requestContext.getUri().toString();
    String method = requestContext.getMethod();
    Integer httpResponseStatus = responseContext.getStatus();
    String logstr = "Ending sdk call--- " + method + " -> " + requestUri + " consumed "
        + consumeTime + "ms " + " httpStatus: " + httpResponseStatus + " UUID: " + uuid + " ---";
    if (consumeTime > SLOW_CALL_MILLIS) {
      log.warn(logstr);
    } else {
      log.info(logstr);
    }
  }

}
