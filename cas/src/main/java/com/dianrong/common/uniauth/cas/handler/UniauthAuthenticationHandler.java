package com.dianrong.common.uniauth.cas.handler;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.AccountDisabledException;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.inspektr.common.web.ClientInfo;
import org.jasig.inspektr.common.web.ClientInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.uniauth.cas.exp.FreshUserException;
import com.dianrong.common.uniauth.cas.exp.MultiUsersFoundException;
import com.dianrong.common.uniauth.cas.exp.UserPasswordNotMatchException;
import com.dianrong.common.uniauth.cas.model.CasUsernamePasswordCredential;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.enm.CasProtocal;

public class UniauthAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	private static final Logger logger = LoggerFactory.getLogger(UniauthAuthenticationHandler.class);

	@Autowired
	private UniClientFacade uniClientFacade;

	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential)	throws GeneralSecurityException, PreventedException {
		CasUsernamePasswordCredential _credential = (CasUsernamePasswordCredential)credential;
		
		String userName = _credential.getUsername();
		String password = _credential.getPassword();
		String tenancyCode = _credential.getTenancyCode();
		
		LoginParam loginParam = new LoginParam();
		loginParam.setAccount(userName);
		loginParam.setPassword(password);
		loginParam.setTenancyCode(tenancyCode);
		ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
		if(clientInfo != null){
			String clientIp = clientInfo.getClientIpAddress();
			loginParam.setIp(clientIp);
		}
		
		Response<UserDto> response = uniClientFacade.getUserResource().login(loginParam);
		List<Info> infoList = response.getInfo();
		
		if(infoList != null && !infoList.isEmpty()){
			Info info = infoList.get(0);
			InfoName infoName = info.getName();
			
			try {
				if(InfoName.LOGIN_ERROR_USER_NOT_FOUND.equals(infoName)){
					throw new AccountNotFoundException("User " + userName + " not found in db.");
				}
				if(InfoName.LOGIN_ERROR.equals(infoName)){
					throw new UserPasswordNotMatchException("User " + userName + " password not match.");
				}
				else if(InfoName.LOGIN_ERROR_MULTI_USER_FOUND.equals(infoName)){
					throw new MultiUsersFoundException("Multiple " + userName + " found in db.");
				}
				else if(InfoName.LOGIN_ERROR_STATUS_1.equals(infoName)){
					throw new AccountDisabledException(userName + " disabled(status == 1) in db.");
				}
				else if(InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT.equals(infoName)){
					throw new AccountLockedException(userName + " locked due to too many failed login attempts.");
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
			} catch (Exception ex) {
				logger.error("username + password login, error :" + ex);
				throw ex;
			}
		}

	Map<String, Object> attributes = new HashMap<String, Object>();attributes.put(CasProtocal.DianRongCas.getTenancyIdName(),response.getData().getTenancyId());return
	createHandlerResult(credential, this.principalFactory.createPrincipal(userName, attributes), null);
	}
}
