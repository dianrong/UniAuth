package com.dianrong.common.uniauth.common.server.cxf.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

public final class ClientFilterSingleton {
	private static final Logger logger = Logger.getLogger(ClientFilterSingleton.class);
	private static UniauthCxfClientFilter instance = new UniauthCxfClientFilter();
	private static AtomicBoolean propSetOnce = new AtomicBoolean(false);

	public static ClientRequestFilter getInstance() {
		return instance;
	}

	// 只能被调用一次
	public static void propSetInvoke(List<HeaderProducer> producers) {
		if (propSetOnce.compareAndSet(false, true)) {
			if (producers != null) {
				instance.setProducers(producers);
			}
		}
	}

	@Provider
	private  static final class UniauthCxfClientFilter implements ClientRequestFilter {
		private List<HeaderProducer> producers;

		public UniauthCxfClientFilter() {
			this(new ArrayList<HeaderProducer>());
		}

		public UniauthCxfClientFilter(List<HeaderProducer> producers) {
			this.producers = producers;
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
					logger.error("add header [" + key + "] failed", ex);
				}
			}
		}

		public void setProducers(List<HeaderProducer> producers) {
			this.producers = producers;
		}
	}
}