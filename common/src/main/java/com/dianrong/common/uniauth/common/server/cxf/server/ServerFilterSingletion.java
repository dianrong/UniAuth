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

import org.apache.log4j.Logger;

public final class ServerFilterSingletion {
	private static final Logger logger = Logger.getLogger(ServerFilterSingletion.class);
	private static UniauthCxfServerFillter instance = new UniauthCxfServerFillter();
	private static AtomicBoolean propSetOnce = new AtomicBoolean(false);

	public static ContainerRequestFilter getInstance() {
		return instance;
	}

	// 只能被调用一次
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
					if (value != null) {
						try {
							consumer.consume(value);
						} catch (Exception ex) {
							logger.warn(key + " consume failed", ex);
						}
					}
				}
			}
		}

		public void setConsumers(List<HeaderConsumer> consumers) {
			if (consumers == null) {
				return;
			}
			consumers = new ArrayList<HeaderConsumer>(consumers);
			Collections.sort(consumers, new Comparator<HeaderConsumer>(){
				@Override
				public int compare(HeaderConsumer o1, HeaderConsumer o2) {
					return o1.getOrder() - o2.getOrder();
				}
			});
			LinkedHashMap<String, HeaderConsumer> _consumers = new LinkedHashMap<String, HeaderConsumer>();
			for (HeaderConsumer hc : consumers) {
				_consumers.put(hc.key(), hc);
			}
			this.consumers = _consumers;
		}
	}
}
