package com.dianrong.common.uniauth.server.support;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;
import com.dianrong.common.uniauth.common.server.cxf.server.impl.AbstractLocalHeaderConsumer;

/**.
 * 获取local值，并放到本地变量中
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
}
