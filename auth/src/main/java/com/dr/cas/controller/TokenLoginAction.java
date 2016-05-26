package com.dr.cas.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;
import org.springframework.web.servlet.ModelAndView;

import com.dr.cas.authentication.principal.TokenCredential;
import com.dr.cas.exception.AuthenticationFailureException.INVALID_TOKEN;
import com.dr.cas.exception.AuthenticationFailureException.TOKEN_IS_EXPIRED;
import com.dr.cas.request.RequestErrorKey;
import com.dr.cas.request.ResultCode;


public class TokenLoginAction extends AbstractSSOController {

  @Override
  protected boolean validate(Credential credential, ModelAndView model) {
    TokenCredential cred = (TokenCredential)credential;

    String errorKey = null;
    if (StringUtils.isEmpty(cred.getUsername())) {
      errorKey = RequestErrorKey.USER_NAME_REQUIRED.name();
    }
    else if (StringUtils.isEmpty(cred.getAccessToken())) {
      errorKey = RequestErrorKey.PASSWORD_EMPTY.name();
    }

    if (errorKey != null) {
      model.addObject("errorKey", errorKey);
      model.addObject("result", ResultCode.INPUT);
      return false;
    }

    return true;
  }

  @Override
  protected RememberMeUsernamePasswordCredential createCredential(HttpServletRequest request) {
    String username = request.getParameter("userName");
    String accessToken = request.getParameter("accessToken");
    RememberMeUsernamePasswordCredential credential = new TokenCredential(username, accessToken);
    return credential;
  }
  
  @Override
  protected void handleLoginResult(Credential credential, ModelAndView model) {
    String errorKey = (String)model.getModel().get("errorKey");
    String result = ResultCode.SUCCESS;
    String status = TokenCredential.TokenStatus.CURRENT.name();
    if (errorKey != null) {
      result = ResultCode.ERROR;
      if (errorKey.equals(TOKEN_IS_EXPIRED.class.getSimpleName())) {
        status = TokenCredential.TokenStatus.EXPIRED.name();
      } else if (errorKey.equals(INVALID_TOKEN.class.getSimpleName())) {
        status = TokenCredential.TokenStatus.INVALID.name();
      }
    }
    model.addObject("status", status);
    model.addObject("result", result);
  }
}

  