package com.dianrong.loanbusiness.subsystem.service;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluator;

public class MyPermissionEvaluator implements UniauthPermissionEvaluator {

  public MyPermissionEvaluator() {
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    System.out.println(targetDomainObject);
    if (authentication.getName().equals("zengwei.xu@dianrong.com")) {
      return true;
    }

    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return false;
  }

}
