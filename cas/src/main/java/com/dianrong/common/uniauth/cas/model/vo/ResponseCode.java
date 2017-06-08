package com.dianrong.common.uniauth.cas.model.vo;

/**
 * 定义Cas的API的返回状态码.
 * @author wanglin
 */
public interface ResponseCode {

  /** 登陆结果类.**/
  /**
   * 用户未找到.
   */
  int USER_NOT_FOUND = 101;
  
  /**
   * 账号密码不匹配.
   */
  int USER_NAME_PSWD_NOT_MATCH = 102;
  
  /**
   * 同一个账号找到多个用户.
   */
  int MULTIPLE_USERS_FOUND = 103;
  
  /**
   * 账号被禁用了.
   */
  int ACCOUNT_DISABLED = 104;

  /**
   * 连续登陆失败次数过多,账号被锁定.
   */
  int ACCOUNT_LOCKED = 105;
  
  /**
   * 账号需要初始化密码.
   */
  
  int ACCOUNT_NEED_INIT_PSWD = 106;
  
  /**
   * 根据密码更新策略,需要重新设置密码.
   */
  int ACCOUNT_PSWD_RESET = 107;
  
  /**
   * 登陆失败, 但是不知道原因.
  */
  int LOGIN_FAILURE = 108;

  /** 状态类. **/
  /**
   * 一个通用的结果状态码,成功.
   */
  int SUCCESS = 200;
  
  /**
   * 用户未登陆.
   */
  int USER_NOT_LOGIN = 201;

  /** 用户请求操作结果类. **/
  /**
   * 创建ticket 失败了, service ticket.
   */
  int CREATE_SERVICE_TICKET_FAILURE = 301;

  /** 参数校验类. **/
  /**
   * 参数service不符合规范.
   */
  int PARAMETER_SERVICE_INVALIDATE = 401;

  /** 应用级状态码. **/
  /**
   * 服务器端异常.
   */
  int SERVER_INTERNAL_ERROR = 500;
}
