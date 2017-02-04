package com.dianrong.common.uniauth.client.config.configurations;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.config.Configure;

/**
 * . create new SingleSignOutFilter
 * 
 * @author wanglin
 */
@Component
public final class SingleSignOutFilterConfigure implements Configure<SingleSignOutFilter> {
	@Override
	public SingleSignOutFilter create() {
		SingleSignOutFilter singleLogoutFilter = new SingleSignOutFilter();
		singleLogoutFilter.setIgnoreInitConfiguration(true);
		return singleLogoutFilter;
	}

	@Override
	public boolean isSupport(Class<?> cls) {
		return SingleSignOutFilter.class.equals(cls);
	}
}
