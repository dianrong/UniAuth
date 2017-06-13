package com.dianrong.common.uniauth.common.server.cxf.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ServerFilterSingletion {

  private static UniauthCxfServerFillter instance = new UniauthCxfServerFillter();
  private static AtomicBoolean propSetOnce = new AtomicBoolean(false);

  public static ContainerRequestFilter getInstance() {
    return instance;
  }

  /**
   * 对外暴露的初始化HeaderConsumer的接口.
   */
  public static void propSetInvoke(List<HeaderConsumer> consumers) {
    if (propSetOnce.compareAndSet(false, true)) {
      if (consumers != null) {
        instance.setConsumers(consumers);
      }
    }
  }

  @Provider
  private static final class UniauthCxfServerFillter implements ContainerRequestFilter {

    private Map<String, HeaderConsumer> consumers;

    public UniauthCxfServerFillter() {
      this(new ArrayList<HeaderConsumer>());
    }

    public UniauthCxfServerFillter(List<HeaderConsumer> consumers) {
      setConsumers(consumers);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
      Set<String> keys = this.consumers.keySet();
      for (String key : keys) {
        HeaderConsumer consumer = this.consumers.get(key);
        if (consumer != null) {
          String value = requestContext.getHeaderString(key);
          try {
            consumer.consume(value);
          } catch (Exception ex) {
            log.warn(key + " consume failed", ex);
          }
        }
      }
    }

    public void setConsumers(List<HeaderConsumer> consumers) {
      if (consumers == null) {
        return;
      }
      consumers = new ArrayList<HeaderConsumer>(consumers);
      Collections.sort(consumers, new Comparator<HeaderConsumer>() {
        @Override
        public int compare(HeaderConsumer o1, HeaderConsumer o2) {
          if (o2.getOrder() > o1.getOrder()) {
            return 1;
          }
          if (o2.getOrder() == o1.getOrder()) {
            return 0;
          }
          return -1;
        }
      });
      LinkedHashMap<String, HeaderConsumer> tempConsumers =
          new LinkedHashMap<String, HeaderConsumer>();
      for (HeaderConsumer hc : consumers) {
        tempConsumers.put(hc.key(), hc);
      }
      this.consumers = tempConsumers;
    }
  }
}
