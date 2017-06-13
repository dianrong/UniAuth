package com.dianrong.uniauth.ssclient.bean;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluatorImpl;
import java.io.Serializable;
import org.springframework.security.core.Authentication;

public class SSClientPermissionEvaluator extends UniauthPermissionEvaluatorImpl {

  public SSClientPermissionEvaluator() {
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetObject,
      Object permission) {
    return super.hasPermission(authentication, targetObject, permission);
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return super.hasPermission(authentication, targetId, targetType, permission);
  }
}
