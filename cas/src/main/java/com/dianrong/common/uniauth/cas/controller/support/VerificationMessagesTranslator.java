package com.dianrong.common.uniauth.cas.controller.support;

import com.dianrong.common.uniauth.cas.exp.UnsupportedVerificationTypeException;
import com.dianrong.common.uniauth.cas.exp.ValidateFailException;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.common.util.Assert;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证信息的转换器.
 */
public final class VerificationMessagesTranslator {

  private static final Map<VerificationMessageType, MessageTranslator> MESSAGE_TYPE_TRANSLATOR_MAP =
      new HashMap<VerificationMessageType, MessageTranslator>() {
        {
          put(VerificationMessageType.FORGET_PASSWORD, new ForgetPwdMessageTranslator());
          put(VerificationMessageType.LOGIN_VERIFICATION, new LoginVerificationMessageTranslator());
        }
      };

  /**
   * 获取发送的内容.
   *
   * @param type          验证的类型
   * @param messageSource 国际化资源对象.
   * @param args          转化需要的参数列表.
   * @throws UnsupportedVerificationTypeException 如果指定的类型type不支持.
   */
  public static String getContent(VerificationMessageType type, MessageSource messageSource,
      Object... args) throws UnsupportedVerificationTypeException {
    Assert.notNull(type);
    MessageTranslator translator = MESSAGE_TYPE_TRANSLATOR_MAP.get(type);
    if (translator == null) {
      throw new UnsupportedVerificationTypeException(type.toString());
    }
    return translator.getContent(messageSource, args);
  }

  /**
   * 获取内容的Title.
   *
   * @param type          验证的类型
   * @param messageSource 国际化资源对象.
   * @param args          转化需要的参数列表.
   * @throws UnsupportedVerificationTypeException 如果指定的类型type不支持.
   */
  public static String getTitle(VerificationMessageType type, MessageSource messageSource,
      Object... args) {
    Assert.notNull(type);
    MessageTranslator translator = MESSAGE_TYPE_TRANSLATOR_MAP.get(type);
    if (translator == null) {
      throw new UnsupportedVerificationTypeException(type.toString());
    }
    return translator.getTitle(messageSource, args);
  }

  private static interface MessageTranslator {

    /**
     * 获取内容.
     *
     * @throws ValidateFailException 参数不对
     */
    String getContent(MessageSource messageSource, Object args[]) throws ValidateFailException;

    /**
     * 获取title.
     *
     * @throws ValidateFailException 参数不对
     */
    String getTitle(MessageSource messageSource, Object args[]) throws ValidateFailException;
  }


  /**
   * 忘记密码验证.
   */
  private static class ForgetPwdMessageTranslator implements MessageTranslator {

    @Override public String getContent(MessageSource messageSource, Object[] args) {
      if (args.length != 2) {
        throw new ValidateFailException("Translate forget password message need 2 parameters!");
      }
      return UniBundleUtil
          .getMsg(messageSource, "verification.controller.verification.content", args);
    }

    @Override public String getTitle(MessageSource messageSource, Object[] args) {
      return UniBundleUtil
          .getMsg(messageSource, "verification.controller.verification.title", args);
    }
  }


  /**
   * 登录验证.
   */
  private static class LoginVerificationMessageTranslator implements MessageTranslator {

    @Override public String getContent(MessageSource messageSource, Object[] args) {
      if (args.length != 2) {
        throw new ValidateFailException("Translate login verification message need 2 parameters!");
      }
      return UniBundleUtil
          .getMsg(messageSource, "verification.controller.login.verification.content", args);
    }

    @Override public String getTitle(MessageSource messageSource, Object[] args) {
      return UniBundleUtil
          .getMsg(messageSource, "verification.controller.login.verification.title", args);
    }
  }
}
