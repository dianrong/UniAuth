package com.dianrong.common.uniauth.cas.model;

import java.io.Serializable;

/**
 * 表示获取ST的返回结果对象.
 *
 * @author wanglin
 */
public class CasGetServiceTicketModel implements Serializable {

  private static final long serialVersionUID = 5755014981878759609L;

  /**
   * . 定义结果常量
   */
  private static final String RESULT_SUCCESS = "success";
  private static final String RESULT_FAILED = "failed";

  // 定义异常的常量
  public static final String LOGIN_EXCEPTION_USER_NOT_FOUND = "101";
  public static final String LOGIN_EXCEPTION_USER_NAME_PSWD_NOT_MATCH = "102";
  public static final String LOGIN_EXCEPTION_MUTILE_USER_FOUND = "103";
  public static final String LOGIN_EXCEPTION_USER_DISABLED = "104";
  public static final String LOGIN_EXCEPTION_TOO_MANY_FAILED = "105";
  public static final String LOGIN_EXCEPTION_NEED_INIT_PSWD = "106";
  public static final String LOGIN_EXCEPTION_NEED_UPDATE_PSWD = "107";
  public static final String LOGIN_EXCEPTION_LOGINFAILED = "108";

  // 创建st失败
  public static final String LOGIN_EXCEPTION_CREATE_SERVICE_FAILED = "201";

  public static final String LOGIN_EXCEPTION_UNKNOW = "301";

  // 验证lt不过
  public static final String LOGIN_EXCEPTION_LT_VALID_FAILED = "401";
  // 验证码验证不过
  public static final String LOGIN_EXCEPTION_CAPTCHA_VALID_FAILED = "402";

  // captcha 获取的相对路径
  public static final String DEFAULT_CATCHA_RELATIVE_PATH = "/uniauth/verification/captcha";


  /**
   * . 处理结果
   */
  private final String result;

  /**
   * . 结果内容
   */
  private final String content;

  /**
   * . 结果原因
   */
  private final String msg;

  /**
   * . 返回结果中 有时候会带lt
   */
  private String lt;

  /**
   * . 获取验证码的相对路径
   */
  private String captchapath;

  /**
   * . 构造函数
   */
  public CasGetServiceTicketModel(boolean success) {
    this(success, null);
  }

  /**
   * . 构造函数
   */
  public CasGetServiceTicketModel(boolean success, String content) {
    this(success, content, null);
  }

  /**
   * .
   */
  public CasGetServiceTicketModel(boolean success, String content, String msg) {
    if (success) {
      this.result = RESULT_SUCCESS;
    } else {
      this.result = RESULT_FAILED;
    }
    this.content = content;
    this.msg = msg;
  }

  public String getResult() {
    return result;
  }

  public boolean getResultSuccess() {
    return this.result.equals(RESULT_SUCCESS);
  }

  public String getContent() {
    return content;
  }

  public String getMsg() {
    return msg;
  }

  public String getLt() {
    return lt;
  }

  public void setLt(String lt) {
    this.lt = lt;
  }

  public String getCaptchapath() {
    return captchapath;
  }

  public void setCaptchapath(String captchapath) {
    this.captchapath = captchapath;
  }
}
