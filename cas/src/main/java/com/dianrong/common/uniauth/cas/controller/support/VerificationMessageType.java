package com.dianrong.common.uniauth.cas.controller.support;

import com.dianrong.common.uniauth.cas.exp.ValidateFailException;
import com.dianrong.common.uniauth.cas.util.CasConstants;
import org.apache.shiro.util.StringUtils;

/**
 * 验证信息类型.
 */
public enum VerificationMessageType {
  /**
   * 忘记密码验证信息.
   */
  FORGET_PASSWORD(CasConstants.VERIFICATION_EXPIRED_MINUTES),
  /**
   * 登录验证信息.
   */
  LOGIN_VERIFICATION(CasConstants.LOGIN_VERIFICATION_EXPIRED_MINUTES);

  private final long effectiveMinutes;

  private VerificationMessageType(long effectiveMinutes) {
    this.effectiveMinutes = effectiveMinutes;
  }

  public long getEffectiveMinutes() {
    return effectiveMinutes;
  }

  /**
   * 根据传入的type字符串返回验证信息的类型.
   * @param type 验证类型字符串.
   * @throws ValidateFailException 传入的type不为空,但是却不是某一种类型的验证.
   */
  public static VerificationMessageType getType(String type) throws ValidateFailException {
    // 默认方式是发送忘记密码的验证方式.
    if (!StringUtils.hasText(type)) {
      return FORGET_PASSWORD;
    }
    VerificationMessageType[] types = VerificationMessageType.values();
    for(VerificationMessageType vmt : types) {
      if (vmt.toString().equalsIgnoreCase(type.trim())) {
        return vmt;
      }
    }
    throw new ValidateFailException(type + " is a invalid VerificationMessageType string");
  }
}
