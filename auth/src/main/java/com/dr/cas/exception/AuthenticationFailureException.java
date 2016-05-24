package com.dr.cas.exception;

import javax.security.auth.login.LoginException;

@SuppressWarnings("serial")
public class AuthenticationFailureException {
  public static final class ACCOUNT_LOCKED extends LoginException { }
  public static final class UNEXPECTED_ERROR extends LoginException { }
  public static final class INVALID_CREDENTIALS extends LoginException {
    public INVALID_CREDENTIALS(String msg) {
      super(msg);
    }
  }
  public static final class ACCOUNT_FAIL_TIME_4 extends LoginException { }
  public static final class ACCOUNT_FAIL_TIME_5 extends LoginException { }
  public static final class ACCOUNT_FAIL_TIME_6 extends LoginException { }
  public static final class ACCOUNT_FAIL_TIME_7 extends LoginException { }
  public static final class ACCOUNT_FAIL_TIME_8 extends LoginException { }
  public static final class ACCOUNT_FAIL_TIME_9 extends LoginException { }
  public static final class ACCOUNT_FAIL_TIME_10 extends LoginException { }
  public static final class INVALID_TOKEN extends LoginException { }
  public static final class TOKEN_IS_EXPIRED extends LoginException { }
}
