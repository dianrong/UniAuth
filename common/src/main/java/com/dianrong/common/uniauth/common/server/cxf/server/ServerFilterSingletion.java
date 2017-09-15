package com.dianrong.common.uniauth.common.server.cxf.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.core.OrderComparator;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.*;

@Slf4j
public final class ServerFilterSingletion {

  private static UniauthCxfServerFillter instance = new UniauthCxfServerFillter();

  public static ContainerRequestFilter getInstance() {
    return instance;
  }

  // 添加新的HeaderProducer
  public static void addNewHeaderProducers(Collection<HeaderConsumer> consumers) {
    instance.addHeaderConsumers(consumers);
  }

  public static void addNewHeaderProducers(HeaderConsumer consumer) {
    instance.addHeaderConsumer(consumer);
  }

  @Provider private static final class UniauthCxfServerFillter implements ContainerRequestFilter {

    private Set<HeaderConsumer> consumers;

    private volatile boolean modified = false;

    private volatile Map<String, HeaderConsumer> tempConsumers;

    public UniauthCxfServerFillter() {
      this.consumers = new HashSet<>();
      this.tempConsumers = new HashMap<>(0);
    }

    @Override public void filter(ContainerRequestContext requestContext) throws IOException {
      sortConsumers();
      for (Map.Entry<String, HeaderConsumer> entry : this.tempConsumers.entrySet()) {
        HeaderConsumer consumer = entry.getValue();
        String key = entry.getKey();
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

    public synchronized void addHeaderConsumer(HeaderConsumer headerConsumer) {
      if (headerConsumer == null) {
        log.warn("Cannot set null headerConsumer");
        return;
      }
      this.consumers.add(headerConsumer);
      this.modified = true;
    }

    public synchronized void addHeaderConsumers(Collection<HeaderConsumer> headerConsumers) {
      if (CollectionUtils.isEmpty(headerConsumers)) {
        return;
      }
      this.consumers.addAll(headerConsumers);
      this.modified = true;
    }

    // sorted list
    private void sortConsumers() {
      if (!this.modified) {
        return;
      }
      synchronized (this) {
        Comparator<Object> comparator = OrderComparator.INSTANCE;
        List<HeaderConsumer> headerConsumers = new ArrayList<>(this.consumers);
        Collections.sort(headerConsumers, comparator);
        Map<String, HeaderConsumer> map =
            new HashMap<String, HeaderConsumer>(headerConsumers.size());
        for (HeaderConsumer headerConsumer : headerConsumers) {
          if (map.get(headerConsumer.key()) != null) {
            continue;
          }
          map.put(headerConsumer.key(), headerConsumer);
        }
        this.tempConsumers = map;
      }
      this.modified = false;
    }
  }
}
