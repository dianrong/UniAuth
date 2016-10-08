package com.dianrong.common.uniauth.client.custom.cxf;

import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.custom.LoginUserInfoHolder;
import com.dianrong.common.uniauth.client.exp.UserNotLoginException;
import com.dianrong.common.uniauth.common.server.cxf.client.impl.AbstractTenancyIdHeaderProducer;

@Component
public class TenancyIdHeaderProducer extends AbstractTenancyIdHeaderProducer{
	@Override
	public String produce() {
		try {
		return String.valueOf(LoginUserInfoHolder.getCurrentLoginUserTenancyId());
		} catch(UserNotLoginException ex) {
			// not login , ignore
		}
		return null;
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

}
