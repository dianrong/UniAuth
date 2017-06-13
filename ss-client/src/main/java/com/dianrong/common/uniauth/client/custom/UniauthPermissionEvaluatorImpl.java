package com.dianrong.common.uniauth.client.custom;

import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("uniauthPermissionEvaluator")
@Slf4j
public class UniauthPermissionEvaluatorImpl implements UniauthPermissionEvaluator {

  public UniauthPermissionEvaluatorImpl() {
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetObject,
      Object permission) {
    log.warn(
        "Denying user " + authentication.getName() + " permission '" + permission + "' on object "
            + targetObject);
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    log.warn("Denying user " + authentication.getName() + " permission '" + permission
        + "' on object with Id '" + targetId);
    return false;
  }

}
