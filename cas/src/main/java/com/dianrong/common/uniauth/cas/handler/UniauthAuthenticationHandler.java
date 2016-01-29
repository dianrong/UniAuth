package com.dianrong.common.uniauth.cas.handler;

import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;

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
		if(response.getInfo() != null && !response.getInfo().isEmpty()){
			throw new FailedLoginException();
		}
		
		return createHandlerResult(credential, this.principalFactory.createPrincipal(userName), null);
	}
}
