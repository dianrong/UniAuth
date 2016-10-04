package com.dianrong.common.uniauth.server.support;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.server.cxf.server.impl.AbstractTenancyIdHeaderConsumer;

@Component
public class TenancyIdHeaderConsumer extends AbstractTenancyIdHeaderConsumer {
	private static Logger logger = Logger.getLogger(TenancyIdHeaderConsumer.class);

	@Override
	public void consume(String value) {
		try {
			Long tenancyId = Long.parseLong(value);
			CxfHeaderHolder.TENANCYID.set(tenancyId);
		} catch (NumberFormatException ex) {
			logger.error("value [" + value + "] is a  invalid  format number!");
		}
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
