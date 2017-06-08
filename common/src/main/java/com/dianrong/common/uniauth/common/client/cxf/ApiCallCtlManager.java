package com.dianrong.common.uniauth.common.client.cxf;

import com.dianrong.common.uniauth.common.apicontrol.HeaderKey;
import com.dianrong.common.uniauth.common.apicontrol.LoginRequestLoadHeaderOperator;
import com.dianrong.common.uniauth.common.apicontrol.LoginResponseLoadHeaderOperator;
import com.dianrong.common.uniauth.common.apicontrol.PtHeaderOperator;
import com.dianrong.common.uniauth.common.apicontrol.RequestVerifiedType;
import com.dianrong.common.uniauth.common.apicontrol.ResponseVerifiedType;
import com.dianrong.common.uniauth.common.apicontrol.StringHeaderValueOperator;
import com.dianrong.common.uniauth.common.apicontrol.client.AbstractInvokeHandlerDelegate;
import com.dianrong.common.uniauth.common.apicontrol.client.ClientStatus;
import com.dianrong.common.uniauth.common.apicontrol.client.InvokeHandlerDelegate;
import com.dianrong.common.uniauth.common.apicontrol.client.InvokeResultHandler;
import com.dianrong.common.uniauth.common.apicontrol.exp.AutenticationFailedException;
import com.dianrong.common.uniauth.common.apicontrol.exp.InsufficientPrivilegesException;
import com.dianrong.common.uniauth.common.apicontrol.model.LoginRequestLoad;
import com.dianrong.common.uniauth.common.apicontrol.model.LoginResponseLoad;
import com.dianrong.common.uniauth.common.client.cxf.exp.ApiCallSocketTimeOutException;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Maps;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import javax.ws.rs.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 管理API权限访问客户端的状态信息.
 *
 * @author wanglin
 */
@Slf4j
public final class ApiCallCtlManager {

  /**
   * 专门用于记录Uniauth的API访问超时的异常,方便统计.
   */
  private static final Logger LOGGER = org.slf4j.LoggerFactory
      .getLogger(AppConstants.UNIAUTH_API_CALL_TIME_OUT_LOGGER);

  // 在token过期前多少毫秒以内，去尝试刷新token, 不能是负数
  public static final long MILLISECONDS_BEFORE_TOKEN_EXPIRED_TO_REFRESH_TOKEN = 10L * 1000L;

  // 线程池，用于运行定时刷新token的任务
  private static final ScheduledExecutorService excutor = Executors
      .newSingleThreadScheduledExecutor();

  /**
   * Singleton.
   */
  private static final ApiCallCtlManager instance = new ApiCallCtlManager();

  /**
   * 获取ApiCallCtlManager实例.
   */
  public static ApiCallCtlManager getInstance() {
    return instance;
  }

  /**
   * All invoke handlers.
   */
  private Map<ClientStatus, InvokeHandlerDelegate> handlers = Maps.newConcurrentMap();

  /**
   * PtHeaderOperator.
   */
  private StringHeaderValueOperator headerOperator;

  // inner implements
  private PtHeaderOperator<LoginRequestLoad> loginRequestHeaderOperator;
  private PtHeaderOperator<LoginResponseLoad> loginResponseHeaderOperator;

  /**
   * Login lock object.
   */
  private final Object loginMutex = new Object();

  // API访问控制开关
  private ApiCallCtlSwitch ctlSwitch = new ApiCallCtlSwitch() {
    // 默认实现总是开启的
    @Override
    public boolean apiCtlOn() {
      return true;
    }
  };

  // login account
  private String account;

  // login password
  private String password;

  // 设置status字段
  private static AtomicReferenceFieldUpdater<ApiCallCtlManager, ClientStatus> referenceClientStatus;

  static {
    try {
      referenceClientStatus = AtomicReferenceFieldUpdater
          .newUpdater(ApiCallCtlManager.class, ClientStatus.class, "status");
    } catch (Exception ex) {
      throw new Error(ex);
    }
  }

  // 默认是匿名登陆
  private volatile ClientStatus status = ClientStatus.ANONYMOUS;

  /**
   * 登陆成功之后换取的token.
   */
  private volatile String token;

  /**
   * 当前的的token过期的时间.
   */
  private long credentialExpireTime;

  // response handler
  private final InvokeResultHandler responseHandler = new InvokeResultHandler() {
    @Override
    public void handle(ResponseVerifiedType type, String content) {
      if (type == null) {
        // ignore
        return;
      }
      switch (type) {
        case INSUFFICIENT_PRIVILEGES:
          // 权限不足
          throw new InsufficientPrivilegesException();
        case AUTENTICATION_FAILED:
          // 身份认证失败
          throw new AutenticationFailedException();
        case TOKEN_AVAILABLE:
          // 通过token访问能正常访问 不处理
          break;
        default:
          break;
      }
    }
  };

  // 统一返回结果处理
  // 做一些处理调用前后的准备工作
  public abstract class InvokeHandlerReponseProcess extends AbstractInvokeHandlerDelegate {

    @Override
    public final void beforeInvoke(Object target, Object proxy, Method method, Object[] args) {
      // clear request header holder
      ApiControlHeaderHolder.getRequestHeaderHolder().remove();

      doBeforeInvoke(target, proxy, method, args);
    }

    @Override
    public final Object afterInvoke(Object target, Object proxy, Method method, Object[] args,
        Object result, Throwable cause) throws Throwable {
      return doAfterInvoke(target, proxy, method, args, result, cause);
    }

    public abstract void doBeforeInvoke(Object target, Object proxy, Method method, Object[] args);

    /**
     * 请求调用完成之后.
     */
    public Object doAfterInvoke(Object target, Object proxy, Method method, Object[] args,
        Object result, Throwable cause) throws Throwable {
      ResponseVerifiedType resultType = null;
      try {
        resultType = ResponseVerifiedType
            .translateStrToResType(headerOperator.getHeader(HeaderKey.RESPONSE_TYPE));
      } catch (IllegalArgumentException e) {
        log.warn(headerOperator.getHeader(HeaderKey.RESPONSE_TYPE)
            + " is a invalid ResponseVerifiedType", e);
      }
      responseHandler.handle(resultType, headerOperator.getHeader(HeaderKey.RESPONSE_REULST));

      // 抛异常
      if (cause != null) {
        socketTimeOutExceptionCheck(cause);
        throw cause;
      }
      return result;
    }

    /**
     * 处理SocketTimeOutException.
     * @param cause 异常信息
     * @throws ApiCallSocketTimeOutException 如果异常是SocketTimeoutException
     */
    protected void socketTimeOutExceptionCheck(final Throwable cause) {
      if (cause instanceof SocketTimeoutException) {
        LOGGER.error(cause.getMessage(), cause);
        throw new ApiCallSocketTimeOutException(cause.getMessage(), cause);
      }
      // 判断反射异常
      if (cause instanceof InvocationTargetException) {
        socketTimeOutExceptionCheck(((InvocationTargetException) cause).getTargetException());
      }
      if (cause instanceof ProcessingException) {
        socketTimeOutExceptionCheck(cause.getCause());
      }
    }
  }

  /**
   * Private constructor.
   */
  private ApiCallCtlManager() {
    super();

    // init header operator
    setHeaderOperator(new CxfHeaderOperator());
    init();
  }

  /**
   * Set PtHeaderOperator.
   * @param headerOperator can not be null
   */
  public ApiCallCtlManager setHeaderOperator(StringHeaderValueOperator headerOperator) {
    Assert.notNull(headerOperator, "set headerOperator, headerOperator can not be null");
    this.headerOperator = headerOperator;
    this.loginRequestHeaderOperator = new LoginRequestLoadHeaderOperator(headerOperator);
    this.loginResponseHeaderOperator = new LoginResponseLoadHeaderOperator(headerOperator);
    return this;
  }

  /**
   * Create all invoke handlers.
   */
  private void init() {
    // ANONYMOUS
    InvokeHandlerDelegate anonymousInvokeHandler = new InvokeHandlerReponseProcess() {
      @Override
      public void doBeforeInvoke(Object target, Object proxy, Method method, Object[] args) {
        headerOperator.setHeader(HeaderKey.REQUEST_TYPE, RequestVerifiedType.ANONYMOUS.toString());
      }
    };
    handlers.put(ClientStatus.ANONYMOUS, anonymousInvokeHandler);

    // LOGIN
    InvokeHandlerDelegate loginInvokeHandler = new InvokeHandlerReponseProcess() {
      @Override
      public void doBeforeInvoke(Object target, Object proxy, Method method, Object[] args) {
        headerOperator.setHeader(HeaderKey.REQUEST_TYPE, RequestVerifiedType.LOGIN.toString());
        loginRequestHeaderOperator.setHeader(HeaderKey.REQUEST_CONTENT, getLoginLoadJson());
      }

      @Override
      public Object doAfterInvoke(Object target, Object proxy, Method method, Object[] args,
          Object result, Throwable cause) throws Throwable {
        synchronized (loginMutex) {
          boolean success = false;
          try {
            // 采用普通处理流程处理
            Object processResult = super.doAfterInvoke(target, proxy, method, args, result, cause);
            ResponseVerifiedType resultType = null;
            try {
              resultType = ResponseVerifiedType
                  .translateStrToResType(headerOperator.getHeader(HeaderKey.RESPONSE_TYPE));
            } catch (IllegalArgumentException e) {
              log.warn(headerOperator.getHeader(HeaderKey.RESPONSE_TYPE)
                  + " is a invalid ResponseVerifiedType", e);
            }
            if (ResponseVerifiedType.LOGIN_SUCCESS.equals(resultType)) {
              success = loginSuccessHandle(
                  loginResponseHeaderOperator.getHeader(HeaderKey.RESPONSE_REULST));
            }
            return processResult;
          } finally {
            if (success) {
              setStatus(ClientStatus.LOGGING, ClientStatus.TOKEN);
            } else {
              setStatus(ClientStatus.LOGGING, ClientStatus.NEED_LOGIN);
            }
            // 必须释放阻塞
            // release
            loginMutex.notifyAll();
          }
        }
      }
    };
    handlers.put(ClientStatus.NEED_LOGIN, loginInvokeHandler);

    // TOKEN
    InvokeHandlerDelegate tokenInvokeHandler = new InvokeHandlerReponseProcess() {
      @Override
      public void doBeforeInvoke(Object target, Object proxy, Method method, Object[] args) {
        headerOperator.setHeader(HeaderKey.REQUEST_TYPE, RequestVerifiedType.TOKEN.toString());
        headerOperator.setHeader(HeaderKey.REQUEST_CONTENT, token);
      }

      @Override
      public Object doAfterInvoke(Object target, Object proxy, Method method, Object[] args,
          Object result, Throwable cause) throws Throwable {
        // 采用普通处理流程处理
        Object processResult = super.doAfterInvoke(target, proxy, method, args, result, cause);
        ResponseVerifiedType resultType = null;
        try {
          resultType = ResponseVerifiedType
              .translateStrToResType(headerOperator.getHeader(HeaderKey.RESPONSE_TYPE));
        } catch (IllegalArgumentException e) {
          log.warn(headerOperator.getHeader(HeaderKey.RESPONSE_TYPE)
              + " is a invalid ResponseVerifiedType", e);
        }

        // token 过期或者 token 验证失败，都用账号密码再重新尝试一次
        if (ResponseVerifiedType.TOKEN_EXPIRED.equals(resultType)
            || ResponseVerifiedType.TOKEN_INVALID.equals(resultType)) {
          // 设置重新登陆的状态
          setStatus(ClientStatus.TOKEN, ClientStatus.NEED_LOGIN);
          // 再调用一次
          return getInvoker().invoke(target, proxy, method, args);
        }
        return processResult;
      }
    };
    handlers.put(ClientStatus.TOKEN, tokenInvokeHandler);

    long delay = MILLISECONDS_BEFORE_TOKEN_EXPIRED_TO_REFRESH_TOKEN - 1000L;
    delay = delay < 1000L ? 1000L : delay;
    long period = delay;
    // 启动token定时刷新任务
    excutor.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        if (ClientStatus.TOKEN.equals(status)) {
          long currentMilles = System.currentTimeMillis();
          long timeToExpired = credentialExpireTime - currentMilles;
          if (timeToExpired <= MILLISECONDS_BEFORE_TOKEN_EXPIRED_TO_REFRESH_TOKEN) {
            // 设置标识
            setStatus(ClientStatus.TOKEN, ClientStatus.NEED_LOGIN);
          }
        }
      }
    }, delay, period, TimeUnit.MILLISECONDS);
  }


  /**
   * Set account and password.
   * @param account can not be null
   * @param password can not be null
   * @return ApiCallCtlManager
   */
  public ApiCallCtlManager setAccount(String account, String password) {
    synchronized (loginMutex) {
      Assert.notNull(account, "account can not be null");
      Assert.notNull(password, "password can not be null");
      this.account = account;
      this.password = password;
      // set flag, try reLogin with new account
      setStatus(this.status, ClientStatus.NEED_LOGIN);
      log.info("reset account to " + account);
      return this;
    }
  }

  /**
   * 登陆成功之后的处理Handler.
   */
  public boolean loginSuccessHandle(final LoginResponseLoad response) {
    if (response == null) {
      log.error("login success, but the reponse is invalid");
      return false;
    }
    try {
      Assert.notNull(response.getToken());
    } catch (Exception ex) {
      log.error("login success, but the reponse token is null");
      return false;
    }
    this.token = response.getToken();
    this.credentialExpireTime = response.getExpireTime();
    return true;
  }

  /**
   * 生成登陆的实体对象.
   *
   * @return 登陆的实体对象
   */
  private LoginRequestLoad getLoginLoadJson() {
    return new LoginRequestLoad(this.account, this.password);
  }

  /**
   * Core method, decide witch invoke handler to return.
   *
   * @return Invoke handler not null
   */
  public InvokeHandlerDelegate getInvoker() {
    // 采用匿名方式进行访问
    if (!ctlSwitch.apiCtlOn()) {
      return handlers.get(ClientStatus.ANONYMOUS);
    }
    if (ClientStatus.NEED_LOGIN.equals(status)) {
      // 尝试去登陆
      if (setStatus(ClientStatus.NEED_LOGIN, ClientStatus.LOGGING)) {
        // 登陆
        log.info("try login with account " + this.account);
        return handlers.get(ClientStatus.NEED_LOGIN);
      } else {
        // 重新尝试获取handler
        return getInvoker();
      }
    }
    // 等待登陆结束
    if (ClientStatus.LOGGING.equals(status)) {
      synchronized (loginMutex) {
        while (ClientStatus.LOGGING.equals(status)) {
          try {
            loginMutex.wait();
          } catch (InterruptedException e) {
            log.warn("InterruptedException ", e);
            // ignore
          }
        }
      }
    }

    InvokeHandlerDelegate handler = handlers.get(status);

    // 此处返回的handler 只能是token 和 anonymous两种之一
    if (handlers.get(ClientStatus.ANONYMOUS) == handler
        || handlers.get(ClientStatus.TOKEN) == handler) {
      return handler;
    }
    return getInvoker();
  }

  /**
   * 设置当前客户端的状态.
   */
  public boolean setStatus(ClientStatus expect, ClientStatus update) {
    Assert.notNull(expect);
    Assert.notNull(update);
    return referenceClientStatus.compareAndSet(this, expect, update);
  }

  // 设置开关
  public ApiCallCtlManager setCtlSwitch(ApiCallCtlSwitch ctlSwitch) {
    this.ctlSwitch = ctlSwitch;
    return this;
  }
}
