package com.dianrong.common.uniauth.cas;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

public class SSJdbcUsernamePasswordAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
	private String sql;
	
	@Override
	protected boolean authenticateUsernamePasswordInternal(UsernamePasswordCredentials credentials)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
