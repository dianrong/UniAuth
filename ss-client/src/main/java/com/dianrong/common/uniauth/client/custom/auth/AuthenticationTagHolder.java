package com.dianrong.common.uniauth.client.custom.auth;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 用于维护每一次请求中是否已经身份认证过了.
 */
public final class AuthenticationTagHolder {
  private final static ThreadLocal<AuthenticationTag> AUTHENTICATION_TAG = new ThreadLocal<AuthenticationTag>(){
    protected AuthenticationTag initialValue() {
      return new AuthenticationTag();
    }
  };

  // Check当前请求是否已经认证.
  public static boolean isAuthenticated(){
    return AUTHENTICATION_TAG.get().tag;
  }

  // Check当前请求是否已经认证.
  public static boolean isAuthenticated(AuthenticationType authenticationType){
    if (authenticationType == null) {
      return false;
    }
    if (AUTHENTICATION_TAG.get().tag) {
      return authenticationType.equals(AUTHENTICATION_TAG.get().authenticationType);
    }
    return false;
  }

  // 是否设置成功
  public static boolean authenticated(AuthenticationType authenticationType){
    Assert.notNull(authenticationType);
    AuthenticationTag authenticationTag = AUTHENTICATION_TAG.get();
    if (authenticationTag.tag) {
      // 已经验证过,直接返回.
      return false;
    }
    authenticationTag.tag = true;
    authenticationTag.authenticationType = authenticationType;
    return true;
  }

  // 移除认证标识.
  public static void remove(){
    AUTHENTICATION_TAG.remove();
  }

  private final static class AuthenticationTag {
    // 是否已经验证过的标识.
    boolean tag;
    // 验证类型的标识.
    AuthenticationType authenticationType;
  }
}
