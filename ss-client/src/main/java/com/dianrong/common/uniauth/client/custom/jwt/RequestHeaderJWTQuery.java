package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.common.jwt.JWTConstant;
import com.dianrong.common.uniauth.common.util.Assert;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;

/**
 * 获取HTTP请求中的JWT值.
 * 
 * @author wanglin
 *
 */
@Order(0)
public class RequestHeaderJWTQuery implements JWTQuery {

  /**
   * Http请求中JWT的header名.
   */
  private String jwtHeaderName = JWTConstant.JWT_NAME;

  @Override
  public String getJWT(HttpServletRequest request) {
    return request.getHeader(jwtHeaderName);
  }

  public String getJwtHeaderName() {
    return jwtHeaderName;
  }

  public void setJwtHeaderName(String jwtHeaderName) {
    Assert.notNull(jwtHeaderName);
    this.jwtHeaderName = jwtHeaderName;
  }
}
