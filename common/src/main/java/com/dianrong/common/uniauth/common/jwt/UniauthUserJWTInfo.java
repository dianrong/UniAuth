package com.dianrong.common.uniauth.common.jwt;

import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * Uniauth的JWT信息对象.
 *
 * @author wanglin
 */
@ToString
public final class UniauthUserJWTInfo {

  private final String issuer;

  private final String audience;

  private final long expireTime;

  private final long createTime;

  private final String subject;

  /**
   * 用户的标识信息. 邮箱,电话号码等.
   */
  private final String identity;

  /**
   * 租户id.
   */
  private final Long tenancyId;

  /**
   * 构造函数.
   * 
   * @param identity 账号信息.
   * @param tenancyId 租户id.
   */
  public UniauthUserJWTInfo(String issuer, String audience, String subject, String identity,
      Long tenancyId, long createTime, long expireTime) {
    super();
    Assert.notNull(identity);
    Assert.notNull(tenancyId);
    Assert.isTrue(createTime > 0);
    Assert.isTrue(expireTime > createTime);
    this.issuer = issuer;
    this.audience = audience;
    this.subject = subject;
    this.identity = identity;
    this.tenancyId = tenancyId;
    this.createTime = createTime;
    this.expireTime = expireTime;
  }

  /**
   * 构造函数.
   * 
   * @param identity 账号信息.
   * @param tenancyId 租户id.
   */
  public UniauthUserJWTInfo(String identity, Long tenancyId, long expireTime) {
    this(JWTConstant.ISSUER, JWTConstant.AUDIENCE, JWTConstant.SUBJECT, identity, tenancyId,
        System.currentTimeMillis(), System.currentTimeMillis() + expireTime);
  }

  public String getIssuer() {
    return emptyString(issuer);
  }

  public String getAudience() {
    return emptyString(audience);
  }

  public long getExpireTime() {
    return expireTime;
  }

  public long getCreateTime() {
    return createTime;
  }

  public String getSubject() {
    return emptyString(subject);
  }

  public String getIdentity() {
    return identity;
  }

  public Long getTenancyId() {
    return tenancyId;
  }

  private String emptyString(String val) {
    if (val == null) {
      return StringUtils.EMPTY;
    }
    return val;
  }
}
