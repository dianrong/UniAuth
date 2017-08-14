package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.common.jwt.JWTConstant;
import com.dianrong.common.uniauth.common.util.Assert;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;

/**
 * 简单的逻辑获取JWT.
 * 
 * @author wanglin
 *
 */
@Order(100)
public class RequestParameterJWTQuery implements JWTQuery {

  /**
   * Http请求jwt的请求参数名.
   */
  private String jwtParameterName = JWTConstant.JWT_NAME;

  @Override
  public String getJWT(HttpServletRequest request) {
    return request.getParameter(jwtParameterName);
  }

  public String getJwtParameterName() {
    return jwtParameterName;
  }

  public void setJwtParameterName(String jwtParameterName) {
    Assert.notNull(jwtParameterName);
    this.jwtParameterName = jwtParameterName;
  }
}
