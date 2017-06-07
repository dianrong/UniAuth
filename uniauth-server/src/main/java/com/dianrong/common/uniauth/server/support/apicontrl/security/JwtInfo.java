package com.dianrong.common.uniauth.server.support.apicontrl.security;

import org.springframework.util.Assert;

/**
 * 加密 解密的参数.
 *
 * @author wanglin
 */
final class JwtInfo {

  // jwt token, customized field key
  // user name
  static final String USER_NAME_KEY = "user_name";
  // user account
  static final String USER_ACCOUNT_KEY = "user_account";
  // permission
  static final String PERMISSION_KEY = "permission_string";

  private final String issuer;

  private final String audience;

  private final long expireTime;

  private final long createTime;

  private final String subject;

  private final String name;

  private final String account;

  private final String permission;

  JwtInfo(String issuer, String audience, String subject, String name, String account,
      String permission, long createTime, long expireTime) {
    super();
    Assert.notNull(account);
    Assert.isTrue(createTime > 0);
    Assert.isTrue(expireTime > createTime);
    this.issuer = issuer;
    this.audience = audience;
    this.subject = subject;
    this.name = name;
    this.account = account;
    this.permission = permission;
    this.createTime = createTime;
    this.expireTime = expireTime;
  }

  public String getIssuer() {
    return issuer == null ? "" : issuer;
  }

  public String getAudience() {
    return audience == null ? "" : audience;
  }

  public long getExpireTime() {
    return expireTime;
  }

  public long getCreateTime() {
    return createTime;
  }

  public String getSubject() {
    return subject == null ? "" : subject;
  }

  public String getName() {
    return name == null ? "" : name;
  }

  public String getAccount() {
    return account;
  }

  public String getPermission() {
    return permission == null ? "" : permission;
  }
}
