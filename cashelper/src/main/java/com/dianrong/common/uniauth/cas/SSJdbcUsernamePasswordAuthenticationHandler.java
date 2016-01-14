package com.dianrong.common.uniauth.cas;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.dianrong.common.uniauth.common.util.UniPasswordEncoder;

public class SSJdbcUsernamePasswordAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
	private String sql;
	private String separator;
	
	@Override
	protected boolean authenticateUsernamePasswordInternal(UsernamePasswordCredentials credentials)
			throws AuthenticationException {
        final String inputUser = getPrincipalNameTransformer().transform(credentials.getUsername());
        final String inputPassword = credentials.getPassword();
        
        try {
            final String dbPasswordCombine = getJdbcTemplate().queryForObject(this.sql, String.class, inputUser);
            if(dbPasswordCombine == null){
            	return false;
            }
            else{
            	int lineIndex = dbPasswordCombine.indexOf(separator);
            	String dbPassword = dbPasswordCombine.substring(0, lineIndex);
            	String dbPasswordSalt = dbPasswordCombine.substring(lineIndex + 1);
            	return UniPasswordEncoder.isPasswordValid(dbPassword, inputPassword, dbPasswordSalt);
            }
        } catch (final IncorrectResultSizeDataAccessException e) {
            // this means the username was not found.
            return false;
        }
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
}
