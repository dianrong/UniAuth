package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Arc on 12/7/2016.
 */
@Provider
// 10 means the provider's priority is very high, log first before every request, log first after every response.
// in jax-rs, If you don't config the priority the default value is 5000.
@Priority(10)
public class UUIDHeaderClientRequestFilter implements ClientRequestFilter, ClientResponseFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(UUIDHeaderClientRequestFilter.class);
    private static final String REQ_START_TIME = "reqStartTime";
    private static final Long SLOW_CALL_MILLIS = 1000L;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String requestURI = requestContext.getUri().toString();
        String method = requestContext.getMethod();
        List<Object> objs = new LinkedList<>();
        objs.add(uuid);
        requestContext.getHeaders().put(AppConstants.API_UUID, objs);
        requestContext.setProperty(REQ_START_TIME, System.currentTimeMillis());
        LOGGER.info("Starting sdk call--- " + method + " -> uri: " + requestURI + " UUID: " + uuid + " ---");
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        Long startTime = (Long) requestContext.getProperty(REQ_START_TIME);
        Long consumeTime = System.currentTimeMillis() - startTime;
        String uuid = (String) requestContext.getHeaders().get(AppConstants.API_UUID).get(0);
        String requestURI = requestContext.getUri().toString();
        String method = requestContext.getMethod();
        Integer httpResponseStatus = responseContext.getStatus();
        String log = "Ending sdk call--- " + method + " -> " + requestURI + " consumed " + consumeTime + "ms " + " httpStatus: " + httpResponseStatus + " UUID: " + uuid + " ---";
        if (consumeTime > SLOW_CALL_MILLIS) {
            LOGGER.warn(log);
        } else {
            LOGGER.info(log);
        }
    }

}
