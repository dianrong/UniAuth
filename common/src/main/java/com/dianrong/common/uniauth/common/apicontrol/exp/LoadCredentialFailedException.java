package com.dianrong.common.uniauth.common.apicontrol.exp;

/**
 * 根据账号密码 加载用户失败异常.
 *
 * @author wanglin
 */
public class LoadCredentialFailedException extends Exception {

  private static final long serialVersionUID = -4557600302758367094L;

  public LoadCredentialFailedException() {
    super();
  }

  public LoadCredentialFailedException(String msg) {
    super(msg);
  }

  public LoadCredentialFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
