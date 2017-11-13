package com.dianrong.common.uniauth.cas.exp;

import com.dianrong.common.uniauth.common.exp.UniauthException;

/**
 * StaffNo不对.
 */
public class InvalidStaffNoException extends UniauthException {

  private static final long serialVersionUID = 1665206621245374336L;

  public InvalidStaffNoException() {
    super();
  }

  public InvalidStaffNoException(String msg) {
    super(msg);
  }
}
