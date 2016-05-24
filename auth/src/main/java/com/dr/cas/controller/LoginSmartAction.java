package com.dr.cas.controller;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;
import org.springframework.web.servlet.ModelAndView;

import com.dr.cas.authentication.principal.PasswordCredential;
import com.dr.cas.request.RequestErrorKey;
import com.dr.cas.request.ResultCode;
import com.google.code.kaptcha.Constants;

public class LoginSmartAction extends LoginAction {

  @Override
  protected RememberMeUsernamePasswordCredential createCredential(HttpServletRequest request) {
    // TODO Auto-generated method stub
    PasswordCredential credential = (PasswordCredential)super.createCredential(request);
    
    credential.setCaptcha(request.getParameter("captcha"));
    String expectedCaptcha = (String)request.getSession().getAttribute(Constants.CAPCHA_SESSION_KEY);
    credential.setExpectedCaptcha(expectedCaptcha);
    request.getSession().removeAttribute(Constants.CAPCHA_SESSION_KEY);
    return credential;

  }

  @Override
  protected boolean validate(Credential credential, ModelAndView model) {
    // TODO Auto-generated method stub
    
    PasswordCredential cred = (PasswordCredential)credential;
    model.addObject("needCaptcha", false);
    if (cred.getExpectedCaptcha() != null) {
      String captcha = cred.getCaptcha();
      if (!cred.getExpectedCaptcha().equalsIgnoreCase(captcha)) {
        model.addObject("errorKey", RequestErrorKey.INCORRECT_CAPTCHA.name());
        model.addObject("result", ResultCode.INPUT);
        model.addObject("needCaptcha", true);
        return false;
      }
    }
    return super.validate(credential, model);
  }

  @Override
  protected void handleLoginResult(Credential credential, ModelAndView model) {
    PasswordCredential cred = (PasswordCredential)credential;
    if (cred.isOverLimit()) {
      model.addObject("needCaptcha", true);
      if (!model.getModel().containsKey("errorKey")) {
        model.addObject("errorKey", RequestErrorKey.INCORRECT_CAPTCHA.name());
      }
    }

    super.handleLoginResult(credential, model);
  }

}
