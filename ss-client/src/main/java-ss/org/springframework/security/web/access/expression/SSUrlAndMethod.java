package org.springframework.security.web.access.expression;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SSUrlAndMethod {

  private String permUrl;
  private String httpMethod;

  public String getPermUrl() {
    return permUrl;
  }

  public void setPermUrl(String permUrl) {
    this.permUrl = permUrl;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}
