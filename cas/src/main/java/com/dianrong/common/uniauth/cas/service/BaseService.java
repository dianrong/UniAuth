package com.dianrong.common.uniauth.cas.service;

import com.dianrong.common.uniauth.cas.exp.FreshUserException;
import com.dianrong.common.uniauth.cas.exp.MultiUsersFoundException;
import com.dianrong.common.uniauth.cas.exp.OtherException;
import com.dianrong.common.uniauth.cas.exp.ResetPasswordException;
import com.dianrong.common.uniauth.cas.exp.UserPasswordNotMatchException;
import com.dianrong.common.uniauth.cas.exp.ValidateFailException;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.AccountDisabledException;

/**
 * . 基础类,提供远程调用的公用方法
 *
 * @author wanglin
 */
@Slf4j
public abstract class BaseService {

  /**
   * InfoName与异常class的map.
   */
  private static Map<InfoName, Class<? extends Exception>> infoExceptionClass =
      new HashMap<InfoName, Class<? extends Exception>>() {
        private static final long serialVersionUID = 7320484331289180713L;
        {
          put(InfoName.REDIRECT, OtherException.class);
          put(InfoName.BAD_REQUEST, OtherException.class);
          put(InfoName.VALIDATE_FAIL, ValidateFailException.class);
          put(InfoName.INTERNAL_ERROR, OtherException.class);
          put(InfoName.STACKTRACE, OtherException.class);
          put(InfoName.LOGIN_ERROR_USER_NOT_FOUND, AccountNotFoundException.class);
          put(InfoName.LOGIN_ERROR_MULTI_USER_FOUND, MultiUsersFoundException.class);
          put(InfoName.LOGIN_ERROR, UserPasswordNotMatchException.class);
          put(InfoName.LOGIN_ERROR_STATUS_1, AccountDisabledException.class);

          put(InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT, AccountLockedException.class);
          put(InfoName.LOGIN_ERROR_NEW_USER, FreshUserException.class);
          put(InfoName.LOGIN_ERROR_EXCEED_MAX_PASSWORD_VALID_MONTH,
              CredentialExpiredException.class);
          put(InfoName.GRP_NOT_OWNER, OtherException.class);
        }
      };

  /**
   * . 根据infoname获取异常的class
   */
  private Exception getExceptionClassByInfoName(InfoName infoName, String errorMsg) {
    Class<? extends Exception> exceptionClass = infoExceptionClass.get(infoName);
    if (exceptionClass == null) {
      exceptionClass = OtherException.class;
    }
    if (StringUtil.strIsNullOrEmpty(errorMsg)) {
      errorMsg = "";
    }

    Constructor<? extends Exception> constructor = null;
    // 获取构造函数
    try {
      constructor = exceptionClass.getConstructor(String.class);
    } catch (NoSuchMethodException e) {
      log.info(exceptionClass + " has no constructor with 1 String parameter");
    } catch (SecurityException e) {
      log.info(e.getMessage());
    }

    try {
      if (constructor == null) {
        // 采用默认构造函数生成exception对象
        return exceptionClass.newInstance();
      } else {
        return constructor.newInstance(errorMsg);
      }
    } catch (InstantiationException e) {
      log.info(e.getMessage());
    } catch (IllegalAccessException e) {
      log.info(e.getMessage());
    } catch (InvocationTargetException e) {
      log.info(e.getMessage());
    }
    // 默认值
    return new Exception(errorMsg);
  }

  /**
   * . 处理异常信息
   *
   * @param infoList infoList
   * @throws ResetPasswordException 异常
   */
  protected void checkInfoList(List<Info> infoList) throws Exception {
    if (infoList != null && !infoList.isEmpty()) {
      for (Info info : infoList) {
        throw getExceptionClassByInfoName(info.getName(), info.getMsg());
      }
    }
  }
}
