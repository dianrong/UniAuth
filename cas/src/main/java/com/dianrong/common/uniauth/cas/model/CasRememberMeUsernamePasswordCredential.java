package com.dianrong.common.uniauth.cas.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jasig.cas.authentication.RememberMeCredential;

public class CasRememberMeUsernamePasswordCredential extends CasUsernamePasswordCredential
    implements RememberMeCredential {

  private static final long serialVersionUID = -5552074507929396707L;

  private boolean rememberMe;

  public final boolean isRememberMe() {
    return this.rememberMe;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(rememberMe).toHashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CasRememberMeUsernamePasswordCredential other =
        (CasRememberMeUsernamePasswordCredential) obj;
    if (this.rememberMe != other.rememberMe) {
      return false;
    }
    return true;
  }

  public final void setRememberMe(final boolean rememberMe) {
    this.rememberMe = rememberMe;
  }
}
