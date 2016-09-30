package com.dianrong.common.uniauth.common.server.cxf.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

public final class ServerFillterSingletion {
	private static final Logger logger = Logger.getLogger(ServerFillterSingletion.class);
	private static UniauthCxfServerFillter instance = new UniauthCxfServerFillter();
	private static AtomicBoolean propSetOnce = new AtomicBoolean(false);

	public static ContainerRequestFilter getInstance() {
		return instance;
	}

	// 只能被调用一次
	public static void propSetInvoke(Map<String, HeaderConsumer> consumers) {
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
			this(new HashMap<String, HeaderConsumer>());
		}

		public UniauthCxfServerFillter(Map<String, HeaderConsumer> consumers) {
			this.consumers = consumers;
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

		public void setConsumers(Map<String, HeaderConsumer> consumers) {
			this.consumers = consumers;
		}
	}
}
