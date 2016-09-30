package com.dianrong.common.uniauth.client.custom.cxf;

import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;
import com.dianrong.common.uniauth.common.server.cxf.client.impl.AbstractLocalHeaderProducer;

@Component
public class LocalHeaderProducer extends AbstractLocalHeaderProducer{
	@Override
	public String produce() {
		return UniauthLocaleInfoHolder.getLocale().toString();
	}
}
