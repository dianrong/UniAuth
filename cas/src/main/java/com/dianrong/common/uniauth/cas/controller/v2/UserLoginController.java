package com.dianrong.common.uniauth.cas.controller.v2;

import com.dianrong.common.uniauth.cas.controller.support.CasLoginSupport;
import com.dianrong.common.uniauth.cas.exp.FreshUserException;
import com.dianrong.common.uniauth.cas.exp.MultiUsersFoundException;
import com.dianrong.common.uniauth.cas.exp.UserPasswordNotMatchException;
import com.dianrong.common.uniauth.cas.exp.ValidateFailException;
import com.dianrong.common.uniauth.cas.model.CasUsernamePasswordCredential;
import com.dianrong.common.uniauth.cas.model.vo.ApiResponse;
import com.dianrong.common.uniauth.cas.model.vo.ResponseCode;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.common.exp.NotLoginException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.AccountDisabledException;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 该controller用于通过纯API的方式进行登陆操作.
 *
 * @author wanglin
 */
@Slf4j
@RestController
@RequestMapping("v2")
@SuppressWarnings("deprecation")
public class UserLoginController {

  /**
   * 登陆处理的支持类.
   */
  @Autowired
  private CasLoginSupport loginSupport;

  /**
   * 国际化资源对象.
   */
  @Autowired
  private MessageSource messageSource;

  /**
   * 定义异常与异常信息的关联关系.
   */
  private static final Map<Class<? extends Exception>, CodeMessage> EXCEPTION_CODE_MAP =
      new HashMap<Class<? extends Exception>, CodeMessage>() {
        private static final long serialVersionUID = 296454014783954623L;

        {
          put(AccountNotFoundException.class,
              new CodeMessage(ResponseCode.USER_NOT_FOUND, "api.v2.login.user.not.found"));
          put(UserPasswordNotMatchException.class, new CodeMessage(
              ResponseCode.USER_NAME_PSWD_NOT_MATCH, "api.v2.login.account.pssword.not.match"));
          put(MultiUsersFoundException.class, new CodeMessage(ResponseCode.MULTIPLE_USERS_FOUND,
              "api.v2.login.multiple.user.found"));
          put(AccountDisabledException.class,
              new CodeMessage(ResponseCode.ACCOUNT_DISABLED, "api.v2.login.account.disabled"));
          put(AccountLockedException.class,
              new CodeMessage(ResponseCode.ACCOUNT_LOCKED, "api.v2.login.account.locked"));
          put(FreshUserException.class, new CodeMessage(ResponseCode.ACCOUNT_NEED_INIT_PSWD,
              "api.v2.login.account.need.init"));
          put(CredentialExpiredException.class,
              new CodeMessage(ResponseCode.ACCOUNT_PSWD_RESET, "api.v2.login.account.pawd.reset"));
          put(FailedLoginException.class,
              new CodeMessage(ResponseCode.LOGIN_FAILURE, "api.v2.login.login.failure"));
        }
      };

  /**
   * 登陆, 并返回登陆结果.
   *
   * @param identity 账号
   * @param password 密码
   * @param tenancyCode 租户编码
   * @param service 登陆系统service
   * @return 登陆结果
   */
  @RequestMapping(value = "login", method = RequestMethod.POST)
  public ApiResponse<String> login(
      @RequestParam(value = "identity", required = true) String identity,
      @RequestParam(value = "password", required = true) String password,
      @RequestParam(value = "tenancyCode", required = true) String tenancyCode,
      @RequestParam(value = "service", required = false) String service, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    // construct login credential, ignore domain info
    UsernamePasswordCredential credential =
        new CasUsernamePasswordCredential(identity, password, null, tenancyCode);
    try {
      // try to login with identity and password
      if (service == null) {
        loginSupport.loginAndQueryTgt(credential, false, request, response);
        return ApiResponse.success();
      } else {
        String serviceTicket =
            loginSupport.loginAndQueryTicket(credential, false, request, response);
        boolean isLoginRedirect = loginSupport.loginRedirect(request, response, serviceTicket);
        if (isLoginRedirect) {
          return ApiResponse.success();
        } else {
          return ApiResponse.success(serviceTicket);
        }
      }
    } catch (TicketException e) {
      log.warn("ticket create failed", e);
      return ApiResponse.failure(ResponseCode.CREATE_SERVICE_TICKET_FAILURE,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.ticket.create.failure"));
    } catch (ValidateFailException e) {
      log.warn("validate parameter failed", e);
      return ApiResponse.failure(ResponseCode.PARAMETER_SERVICE_INVALIDATE,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.validate.service.failure"));
    } catch (AuthenticationException e) {
      log.debug("login failed", e);
      CodeMessage codeMessage = queryExceptionCodeMessage(e);
      return ApiResponse.failure(codeMessage.code,
          UniBundleUtil.getMsg(messageSource, codeMessage.messageCode));
    }
  }

  /**
   * 登出.
   * <P>
   * 如果当前处于登陆状态,则直接登出. 如果不处于登陆状态,则直接忽略
   * </p>
   * 
   * @return 总是返回成功
   */
  @RequestMapping(value = "logout", method = {RequestMethod.POST, RequestMethod.GET})
  public ApiResponse<Serializable> logout(HttpServletRequest request,
      HttpServletResponse response) {
    loginSupport.casLoginOut(request, response);
    return ApiResponse.success();
  }

  /**
   * 生成service ticket(该接口调用必须在用户登陆的状态下).
   * 
   * @param service 用于生成service ticket的service参数
   * @return 结果
   */
  @RequestMapping(value = "ticket", method = RequestMethod.POST)
  public ApiResponse<String> getTicket(
      @RequestParam(value = "service", required = true) String service, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      TicketGrantingTicket tgtTicket = loginSupport.queryTgtWithLogined(request, response);
      String serviceTicket = loginSupport.grantServiceTicket(request, tgtTicket.getId());
      log.info("create a service ticket {} successfully ", serviceTicket);
      return ApiResponse.success(serviceTicket);
    } catch (NotLoginException e) {
      log.info("user not login, but try to query service ticket", e);
      return ApiResponse.failure(ResponseCode.USER_NOT_LOGIN,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.user.not.login"));
    } catch (TicketException te) {
      log.warn("create service ticket failed", te);
      return ApiResponse.failure(ResponseCode.CREATE_SERVICE_TICKET_FAILURE,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.ticket.create.failure"));
    } catch (ValidateFailException vfe) {
      log.warn("parameter service is invalid", vfe);
      return ApiResponse.failure(ResponseCode.PARAMETER_SERVICE_INVALIDATE,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.validate.service.failure"));
    }
  }

  /**
   * 根据异常获取具体的异常信息.
   *
   * @param ae AuthenticationException 抛出的异常信息, 不能为空
   * @return CodeMessage 异常信息, 不会为空
   */
  private CodeMessage queryExceptionCodeMessage(AuthenticationException ae) {
    Map<String, Class<? extends Exception>> exmap = ae.getHandlerErrors();
    if (exmap != null) {
      Set<Entry<String, Class<? extends Exception>>> entrySet = exmap.entrySet();
      for (Entry<String, Class<? extends Exception>> entry : entrySet) {
        CodeMessage codeMessage = EXCEPTION_CODE_MAP.get(entry.getValue());
        if (codeMessage != null) {
          return codeMessage;
        }
      }
    }
    log.info("can not find code message {}", ae);
    return new CodeMessage(ResponseCode.LOGIN_FAILURE, "api.v2.login.login.failure");
  }

  /**
   * 便利的class, 用于包装结果和结果对应的消息码.
   */
  static class CodeMessage {

    private final Integer code;
    private final String messageCode;

    public CodeMessage(Integer code, String messageCode) {
      Assert.notNull(code);
      Assert.notNull(messageCode);
      this.code = code;
      this.messageCode = messageCode;
    }
  }
}
