package com.dr.cas.authentication.principal;

import java.math.BigDecimal;
import java.util.Map;

import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;

import com.dianrong.common.uniauth.common.bean.dto.SlIpDto;
import com.dianrong.common.uniauth.common.bean.dto.SlIpEnum;

public class PasswordCredential extends RememberMeUsernamePasswordCredential {

  /**
   * seraial version UID
   */
  private static final long serialVersionUID = 1L;
  private SlIpDto ipAddress;
  private String accessToken = null;
  private String captcha;
  private String expectedCaptcha;
  private int attempts;
  private Long actorId;
  private boolean overLimit = false;

  public String getCaptcha() {
    return captcha;
  }

  public void setCaptcha(String captcha) {
    this.captcha = captcha;
  }

  public String getExpectedCaptcha() {
    return expectedCaptcha;
  }

  public void setExpectedCaptcha(String captcha) {
    this.expectedCaptcha = captcha;
  }
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public PasswordCredential(String username, String accessToken) {
    super.setUsername(username);
    this.accessToken = accessToken;
  }

  public PasswordCredential(String username, String password, boolean rememberMe) {
    super.setUsername(username);
    super.setPassword(password);
    super.setRememberMe(rememberMe);
  }

  public void setIPAddress(String ipAddr, Map params) {
    // http://ip-api.com/json
    ipAddress = new SlIpDto(ipAddr, SlIpEnum.Type.LOGIN, SlIpEnum.Status.NORMAL);
    String ip = getParam(params, "query");
    if (ip != null && !ip.equals(ipAddr)) {
      ipAddress.setIpAddr(ip);
    }
    ipAddress.setCity(getParam(params, "city"));
    ipAddress.setCountry(getParam(params, "country"));
    ipAddress.setRegion(getParam(params, "region"));
    ipAddress.setZip(getParam(params, "zip"));
    try {
      ipAddress.setLatitude(new BigDecimal(getParam(params, "lat")));
      ipAddress.setLongitude(new BigDecimal(getParam(params, "lon")));
    } catch (Exception e) {}
  }
  
  
  public SlIpDto getIPAddress() {
    return this.ipAddress;
  }
  
  public String getIp() {
    return this.ipAddress == null ? null : ipAddress.getIpAddr();
  }
  
  private static String getParam(Map params, String key) {
    try {
      return ((String[])params.get(key))[0];
    } catch (ClassCastException e) {
      try {
        return (String)params.get(key);
      } catch (Exception ee) {}
    } catch (Exception e) {}
    return null;
  }

  public boolean hasAccessToken() {
    return this.accessToken != null;
  }
  public String getAccessToken() {
    return this.accessToken;
  }

  public void setActorId(Long actorId) {
    this.actorId = actorId;
  }
  
  public Long getActorId() {
    return this.actorId;
  }

  public int getAttempts() {
    return attempts;
  }

  public void setAttempts(int attempts) {
    this.attempts = attempts;
  }
 
  public void setOverLimit(boolean overlimit) {
    this.overLimit = overlimit;
  }
  
  public boolean isOverLimit() {
    return this.overLimit;
  }
}
