package com.dianrong.common.uniauth.server.service.attribute.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 扩展属性的值不规范的异常.
 */
public class InvalidPropertyValueException extends UniauthCommonException {

  private static final long serialVersionUID = 6742145350967709995L;

  private String invalidValue;

  private String type;

  public InvalidPropertyValueException(String type, String invalidValue) {
    super();
    this.invalidValue = invalidValue;
    this.type = type;
  }

  public InvalidPropertyValueException(String msg, String type, String invalidValue) {
    super(msg);
    this.invalidValue = invalidValue;
    this.type = type;
  }

  public InvalidPropertyValueException(String msg, Throwable t, String type, String invalidValue) {
    super(msg, t);
    this.invalidValue = invalidValue;
    this.type = type;
  }

  public String getInvalidValue() {
    return invalidValue;
  }

  public String getType() {
    return type;
  }
}
