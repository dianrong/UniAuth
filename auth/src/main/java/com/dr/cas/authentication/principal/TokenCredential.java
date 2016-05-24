package com.dr.cas.authentication.principal;

import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;

public class TokenCredential extends RememberMeUsernamePasswordCredential {

  /**
   * 
   */
  private static final long serialVersionUID = -550651421846736559L;
  private String accessToken;
  
  public TokenCredential(String accountName, String accessToken) {
    super.setUsername(accountName);
    this.accessToken = accessToken;
  }
  
  public String getAccessToken() {
    return this.accessToken;
  }

  public enum TokenStatus {
    EXPIRED, CURRENT, INVALID,INVALIDATED
  }
}
