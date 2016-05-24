package com.dr.cas.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;
import org.springframework.web.servlet.ModelAndView;

import com.dr.cas.authentication.principal.PasswordCredential;
import com.dr.cas.request.RequestErrorKey;
import com.dr.cas.request.ResultCode;
import com.dr.cas.util.RequestUtils;

public class LoginAction extends AbstractSSOController {

  @Override
  protected RememberMeUsernamePasswordCredential createCredential(HttpServletRequest request) {
    String username = request.getParameter("identity");
    String password = request.getParameter("password");
    boolean rememberMe = Boolean.valueOf(request.getParameter("rememberMe"));

    PasswordCredential credential = new PasswordCredential(username, password, rememberMe);
    String ip = RequestUtils.getIPAddress(request);
    credential.setIPAddress(ip, null);
    
    return credential;
  }

  @Override
  protected boolean validate(Credential credential, ModelAndView model) {
    // TODO: validate investment channel
    PasswordCredential cred = (PasswordCredential)credential;

    String username = cred.getUsername();
    String password = cred.getPassword();
    String errorKey = null;

    if (StringUtils.isEmpty(username)) {
      errorKey = RequestErrorKey.USER_NAME_REQUIRED.name();
    }
    else if (StringUtils.isEmpty(password)) {
      errorKey = RequestErrorKey.PASSWORD_EMPTY.name();
    }
    // validate email
    else if (username.contains("@")) {
      // Validate email
      if (!validateEmail(username)) {
        errorKey = RequestErrorKey.INVALID_EMAIL.name();
      }
    } else {
      // Validate username
      if (username.length() > USER_NAME_LENGTH_LIMIT) {
        errorKey = RequestErrorKey.USER_NAME_TOO_LONG.name();
      }
      else if (username.getBytes().length <= 3) {
        errorKey = RequestErrorKey.INVALID_USERNAME.name();
      }
      else if (!matches(username, usernamePattern)) {
        errorKey = RequestErrorKey.INVALID_USERNAME.name();
      }
    }

    if (errorKey != null) {
      model.addObject("errorKey", errorKey);
      model.addObject("result", ResultCode.ERROR);
      return false;
    }
    // TODO: Validate password
    return true;
  }

  @Override
  protected void handleLoginResult(Credential credential, ModelAndView model) {
    String errorKey = (String)model.getModel().get("errorKey");
    String result = ResultCode.SUCCESS;
    if (errorKey != null) {
      result = ResultCode.ERROR;
    }
    model.addObject("actorId", credential.getId());
    model.addObject("channelUserConvertedToDrUser", false);
    model.addObject("result", result);
  }

} 
