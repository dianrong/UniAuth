package com.dianrong.common.uniauth.cas.model;

import java.io.Serializable;

public class ForgetPasswordModel implements Serializable {

  private static final long serialVersionUID = -5629584597142529188L;

  private String email;
  private String captchaText;
  private String emailVerifyCode;
  private String newPassword;
  private String savedLoginContext;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCaptchaText() {
    return captchaText;
  }

  public void setCaptchaText(String captchaText) {
    this.captchaText = captchaText;
  }

  public String getEmailVerifyCode() {
    return emailVerifyCode;
  }

  public void setEmailVerifyCode(String emailVerifyCode) {
    this.emailVerifyCode = emailVerifyCode;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getSavedLoginContext() {
    return savedLoginContext;
  }

  public void setSavedLoginContext(String savedLoginContext) {
    this.savedLoginContext = savedLoginContext;
  }
}
