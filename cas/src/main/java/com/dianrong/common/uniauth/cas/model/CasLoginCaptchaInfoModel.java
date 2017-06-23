package com.dianrong.common.uniauth.cas.model;

import java.io.Serializable;

/**
 * 辅助model,记录每一个session进行Cas登陆,验证码相关信息<br> 
 * PS: 一个session一个，不用进行多线程的考虑.
 */
public class CasLoginCaptchaInfoModel implements Serializable {

  private static final long serialVersionUID = -3765249768294450877L;

  /**
   * . 失败多少次就显示验证码的默认值
   */
  public static final int DEFAULT_SHOW_CAPTCHA_FAILED_COUNT = 3;

  /**
   * . 重新登陆不显示验证码的等待毫秒数
   */
  public static long PER_TIME_MILLS_RELOGIN_NO_CAPTCHA = 10L * 60L * 1000L;

  /**
   * . 已经登陆失败次数
   */
  private int failCount;

  /**
   * . 上一次登陆失败的时间毫秒数
   */
  private long lastLoginFailedMilles;

  /**
   * . 失败多少次就显示验证码
   */
  private int maxFailCount;

  /**
   * . 构造函数
   */
  public CasLoginCaptchaInfoModel() {
    this(DEFAULT_SHOW_CAPTCHA_FAILED_COUNT);
  }

  /**
   * . 构造函数
   *
   * @param maxFailCount 失败次数
   */
  public CasLoginCaptchaInfoModel(int maxFailCount) {
    this.failCount = 0;
    this.lastLoginFailedMilles = System.currentTimeMillis();
    this.maxFailCount = maxFailCount < 0 ? DEFAULT_SHOW_CAPTCHA_FAILED_COUNT : maxFailCount;
  }

  /**
   * . 判断失败一次之后是否能继续正常登陆(不要验证码)
   *
   * @return 结果
   */
  public boolean canLoginWithouCaptchaForFailedOnce() {
    failedCountInc();
    return canLoginWithoutCaptcha();
  }

  /**
   * . 失败数增加一次
   */
  public void failedCountInc() {
    // 判断是否已经过期了
    if (this.lastLoginFailedMilles + PER_TIME_MILLS_RELOGIN_NO_CAPTCHA < System
        .currentTimeMillis()) {
      reInit();
    }

    this.failCount++;
    // 刷新最新的失败时间
    this.lastLoginFailedMilles = System.currentTimeMillis();
  }

  /**
   * . 判断是否能够不用验证码的登陆
   *
   * @return 是否能够不用验证码验证来登陆
   */
  public boolean canLoginWithoutCaptcha() {
    if (this.failCount < maxFailCount) {
      return true;
    }

    // 判断是否已经过期了
    if (this.lastLoginFailedMilles + PER_TIME_MILLS_RELOGIN_NO_CAPTCHA < System
        .currentTimeMillis()) {
      reInit();
      return true;
    }
    return false;
  }

  /**
   * . 重新初始化数据
   */
  public void reInit() {
    this.failCount = 0;
    this.lastLoginFailedMilles = System.currentTimeMillis();
  }
}
