package com.dianrong.common.uniauth.common.server.cxf.server.impl;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;
import com.dianrong.common.uniauth.common.server.cxf.server.impl.AbstractLocalHeaderConsumer;

/**.
 * 消费local header的默认实现
 * @author wanglin
 */
@Component
public class LocalHeaderConsumer extends AbstractLocalHeaderConsumer{
	@Override
	public void consume(String localeStr) {
		 if(localeStr != null) {
	            UniauthLocaleInfoHolder.setLocale(StringUtils.parseLocaleString(localeStr));
	        }
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
