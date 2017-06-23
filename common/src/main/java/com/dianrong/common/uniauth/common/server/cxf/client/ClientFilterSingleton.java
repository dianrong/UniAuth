package com.dianrong.common.uniauth.common.server.cxf.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ClientFilterSingleton {

  private static UniauthCxfClientFilter instance = new UniauthCxfClientFilter();
  private static final AtomicBoolean propSetOnce = new AtomicBoolean(false);

  public static ClientRequestFilter getInstance() {
    return instance;
  }

  /**
   * 初始化HeaderProducer的接口.
   */
  public static void propSetInvoke(List<HeaderProducer> producers) {
    if (propSetOnce.compareAndSet(false, true)) {
      if (producers != null) {
        instance.setProducers(producers);
      }
    }
  }

  @Provider
  private static final class UniauthCxfClientFilter implements ClientRequestFilter {

    private Set<HeaderProducer> producers;

    public UniauthCxfClientFilter() {
      this(new ArrayList<HeaderProducer>());
    }

    public UniauthCxfClientFilter(List<HeaderProducer> producers) {
      setProducers(producers);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
      for (HeaderProducer producer : this.producers) {
        String key = producer.key();
        try {
          String value = producer.produce();
          if (key != null && value != null) {
            requestContext.getStringHeaders().add(key, value);
          }
        } catch (Exception ex) {
          log.error("add header [" + key + "] failed", ex);
        }
      }
    }

    // sorted list
    public void setProducers(List<HeaderProducer> producers) {
      if (producers == null) {
        return;
      }
      producers = new ArrayList<HeaderProducer>(producers);
      Collections.sort(producers, new Comparator<HeaderProducer>() {
        @Override
        public int compare(HeaderProducer o1, HeaderProducer o2) {
          if (o2.getOrder() > o1.getOrder()) {
            return 1;
          }
          if (o2.getOrder() == o1.getOrder()) {
            return 0;
          }
          return -1;
        }
      });
      Map<String, HeaderProducer> map = new HashMap<String, HeaderProducer>();
      for (HeaderProducer hp : producers) {
        map.put(hp.key(), hp);
      }
      this.producers = new HashSet<HeaderProducer>(map.values());
    }
  }
}
