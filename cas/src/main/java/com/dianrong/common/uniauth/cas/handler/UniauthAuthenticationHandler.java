package com.dianrong.common.uniauth.cas.handler;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.AccountDisabledException;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.InvalidLoginTimeException;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.uniauth.cas.exp.FreshUserException;
import com.dianrong.common.uniauth.cas.exp.MultiUsersFoundException;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

public class UniauthAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	
	@Autowired
	private UniClientFacade uniUniClientFacade;
	
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential)	throws GeneralSecurityException, PreventedException {
		String userName = credential.getUsername();
		String password = credential.getPassword();
		
		LoginParam loginParam = new LoginParam();
		loginParam.setAccount(userName);
		loginParam.setPassword(password);
		loginParam.setIp("127.0.0.1");
		
		Response<Void> response = uniUniClientFacade.getUserResource().login(loginParam);
		List<Info> infoList = response.getInfo();
		
		if(infoList != null && !infoList.isEmpty()){
			Info info = infoList.get(0);
			InfoName infoName = info.getName();
			
			if(InfoName.LOGIN_ERROR_USER_NOT_FOUND.equals(infoName)){
				throw new AccountNotFoundException("User " + userName + " not found in db.");
			}
			else if(InfoName.LOGIN_ERROR_MULTI_USER_FOUND.equals(infoName)){
				throw new MultiUsersFoundException("Multiple " + userName + " found in db.");
			}
			else if(InfoName.LOGIN_ERROR_STATUS_1.equals(infoName)){
				throw new AccountDisabledException(userName + " disabled(status == 1) in db.");
			}
			else if(InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT.equals(infoName)){
				throw new InvalidLoginTimeException(userName + " locked due to too many login attempts.");
			}
			else if(InfoName.LOGIN_ERROR_NEW_USER.equals(infoName)){
				throw new FreshUserException("Newly added user, must modify password first.");
			}
			else if(InfoName.LOGIN_ERROR_EXCEED_MAX_PASSWORD_VALID_MONTH.equals(infoName)){
				throw new CredentialExpiredException("Need modify password due to password policy.");
			}
			else{
				throw new FailedLoginException(userName + "/" + password + "not matched.");
			}
		}
		
		return createHandlerResult(credential, this.principalFactory.createPrincipal(userName), null);
	}
}
