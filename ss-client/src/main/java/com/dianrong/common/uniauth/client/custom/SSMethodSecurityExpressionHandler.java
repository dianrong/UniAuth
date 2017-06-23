package com.dianrong.common.uniauth.client.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;

public class SSMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

  @Autowired
  private UniauthPermissionEvaluator permissionEvaluator;

  public SSMethodSecurityExpressionHandler() {
  }

  public UniauthPermissionEvaluator getPermissionEvaluator() {
    return permissionEvaluator;
  }

  public void setPermissionEvaluator(UniauthPermissionEvaluator permissionEvaluator) {
    this.permissionEvaluator = permissionEvaluator;
  }
}
