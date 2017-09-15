package com.dianrong.common.uniauth.common.server.cxf.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.core.OrderComparator;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.*;

@Slf4j
public final class ClientFilterSingleton {

  private static UniauthCxfClientFilter instance = new UniauthCxfClientFilter();

  public static ClientRequestFilter getInstance() {
    return instance;
  }

  // 添加新的HeaderProducer
  public static void addNewHeaderProducers(Collection<HeaderProducer> producers) {
    instance.addHeaderProducers(producers);
  }

  public static void addNewHeaderProducers(HeaderProducer producer) {
    instance.addHeaderProducer(producer);
  }

  @Provider
  private static final class UniauthCxfClientFilter implements ClientRequestFilter {

    private Set<HeaderProducer> producers;

    private volatile boolean modified = false;

    private Set<HeaderProducer> tempProducers;

    public UniauthCxfClientFilter() {
      this.producers = new HashSet<>();
      this.tempProducers = new HashSet<>(0);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
      sortProducers();
      for (HeaderProducer producer : this.tempProducers) {
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

    public synchronized void addHeaderProducer(HeaderProducer headerProducer) {
      if (headerProducer == null) {
        log.warn("Cannot set null headerProducer");
        return;
      }
      this.producers.add(headerProducer);
      this.modified = true;
    }

    public synchronized void addHeaderProducers(Collection<HeaderProducer> headerProducers) {
      if (CollectionUtils.isEmpty(headerProducers)) {
        return;
      }
      this.producers.addAll(headerProducers);
      this.modified = true;
    }

    // sorted list
    private void sortProducers() {
      if (!this.modified) {
        return;
      }
      synchronized (this) {
        Comparator<Object> comparator = OrderComparator.INSTANCE;
        List<HeaderProducer> headerProducersList = new ArrayList<>(this.producers);
        Collections.sort(headerProducersList, comparator);
        Map<String, HeaderProducer> map =
            new HashMap<String, HeaderProducer>(headerProducersList.size());

        for (HeaderProducer hp : headerProducersList) {
          if (map.get(hp.key()) != null) {
            continue;
          }
          map.put(hp.key(), hp);
        }
        this.tempProducers = new HashSet<HeaderProducer>(map.values());
      }
      this.modified = false;
    }
  }
}
