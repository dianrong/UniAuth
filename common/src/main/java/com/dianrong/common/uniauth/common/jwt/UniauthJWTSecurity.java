package com.dianrong.common.uniauth.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidJWTExpiredException;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidSecurityKeyException;
import com.dianrong.common.uniauth.common.jwt.exp.JWTVerifierCreateFailedException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTCreateFailedException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTExpiredException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * JWT操作相关的API 涉及加密和解密函数.
 *
 * @author wanglin
 */
@Slf4j
public class UniauthJWTSecurity {

  /**
   * 私钥.
   */
  private final RSAPrivateKey rsaPrivateKey;

  /**
   * 公钥.
   */
  private final RSAPublicKey rsaPublicKey;

  /**
   * JWT的验证对象.
   */
  private final JWTVerifier verifier;

  /**
   * 兼容服务器时间.JWT的issued时间的提前秒偏移..
   */
  private int issuedSecondsAheadOffset = 0;

  /**
   * 构造一个UniauthJWTSecurity.
   *
   * @param rsaPrivateKey 私钥
   * @param rsaPublicKey 公钥
   * @param spareSecurityKey 备用的秘钥对.
   * @throws JWTVerifierCreateFailedException 创建对应的JWTVerifier失败.
   * @throws InvalidSecurityKeyException 传入的公钥或私钥不规范.
   */
  public UniauthJWTSecurity(final String rsaPrivateKey, String rsaPublicKey, SpareSecurityKey spareSecurityKey)
      throws JWTVerifierCreateFailedException, NoSuchAlgorithmException,
      InvalidSecurityKeyException {
    String privateKey;
    String publicKey;
    if (!StringUtils.hasText(rsaPrivateKey) || !StringUtils.hasText(rsaPublicKey)) {
      log.info("privateKey or publicKey is missing, use SpareSecurityKey");
      Assert.notNull(spareSecurityKey, "SpareSecurityKey can not be null.");
      privateKey = spareSecurityKey.getPrivateKey() ;
      publicKey = spareSecurityKey.getPublicKey();
    } else {
      privateKey = rsaPrivateKey;
      publicKey = rsaPublicKey;
    }
    try {
      this.rsaPublicKey = RSASecurityKeyHelper.getPublickKey(publicKey);
    } catch (InvalidKeySpecException e) {
      log.error("PublicKey:{}  is invalid!", publicKey);
      throw new InvalidSecurityKeyException("PublicKey:" + publicKey + " is invalid!");
    }
    try {
      this.rsaPrivateKey = RSASecurityKeyHelper.getPrivateKey(privateKey);
    } catch (InvalidKeySpecException e) {
      log.error("PrivateKey:{}  is invalid!", privateKey);
      throw new InvalidSecurityKeyException("PrivateKey:" + privateKey + "  is invalid!");
    }
    try {
      this.verifier = JWT.require(Algorithm.RSA256(this.rsaPublicKey)).build();
    } catch (Exception e) {
      log.error("failed to create JWTVerifier ", e);
      throw new JWTVerifierCreateFailedException("Failed create JWTVerifier ", e);
    }
  }

  /**
   * 构造一个UniauthJWTSecurity.
   *
   * @param rsaPrivateKey 私钥,不能为空.
   * @param rsaPublicKey 公钥,不能为空.
   * @throws JWTVerifierCreateFailedException 创建对应的JWTVerifier失败.
   * @throws InvalidSecurityKeyException 传入的公钥或私钥不规范.
   */
  public UniauthJWTSecurity(final String rsaPrivateKey, String rsaPublicKey)
      throws JWTVerifierCreateFailedException, NoSuchAlgorithmException,
      InvalidSecurityKeyException {
    this(rsaPrivateKey, rsaPublicKey, null);
  }

  /**
   * 创建JWT. 使用私钥加密生成JWT.
   *
   * @throws LoginJWTCreateFailedException 生成登陆JWT失败.
   */
  public String createJwt(UniauthUserJWTInfo jwtInfo) throws LoginJWTCreateFailedException {
    Assert.notNull(jwtInfo);
    try {
      Date issuedAt = new Date(getIssuedTime(jwtInfo.getCreateTime(), true));
      Date expiresAt = new Date(jwtInfo.getExpireTime());
      Integer tenancyId = jwtInfo.getTenancyId() == null ? null : jwtInfo.getTenancyId().intValue();
      return JWT.create().withIssuer(jwtInfo.getIssuer()).withIssuedAt(issuedAt)
          .withExpiresAt(expiresAt).withAudience(jwtInfo.getAudience())
          .withSubject(jwtInfo.getSubject())
          .withClaim(JWTConstant.IDENTITY_KEY, jwtInfo.getIdentity())
          .withClaim(JWTConstant.TENANCY_ID_KEY, tenancyId).sign(Algorithm.RSA256(rsaPrivateKey));
    } catch (Exception e) {
      log.error("Failed create login jwt ", e);
      throw new LoginJWTCreateFailedException("Failed create login jwt: " + jwtInfo, e);
    }
  }

  /**
   * 解密JWT,生成登陆用户身份信息.
   *
   * @throws LoginJWTExpiredException JWT已经过期了.
   * @throws InvalidJWTExpiredException 非法的JWT.
   */
  public UniauthUserJWTInfo getInfoFromJwt(String jwt)
      throws LoginJWTExpiredException, InvalidJWTExpiredException {
    Assert.notNull(jwt);
    try {
      DecodedJWT decodedJwt = this.verifier.verify(jwt);
      String audience = decodedJwt.getAudience() == null ? null
          : decodedJwt.getAudience().isEmpty() ? null : decodedJwt.getAudience().get(0);
      long createTime = getIssuedTime(decodedJwt.getIssuedAt().getTime(), false);
      long expireTIme = decodedJwt.getExpiresAt().getTime();
      Integer tenancyId = decodedJwt.getClaim(JWTConstant.TENANCY_ID_KEY).asInt();
      return new UniauthUserJWTInfo(decodedJwt.getIssuer(), audience, decodedJwt.getSubject(),
          decodedJwt.getClaim(JWTConstant.IDENTITY_KEY).asString(),
          tenancyId == null ? null : tenancyId.longValue(), createTime, expireTIme);
    } catch (InvalidClaimException invalidException) {
      if (invalidException.getMessage() != null
          && invalidException.getMessage().contains("The Token has expired on")) {
        throw new LoginJWTExpiredException(jwt + " is expired!");
      }
      log.error(jwt + " is expired!", invalidException);
      throw new InvalidJWTExpiredException(jwt + " is a invalid jwt token ", invalidException);
    } catch (Throwable t) {
      log.error(jwt + " is a invalid jwt token ", t);
      throw new InvalidJWTExpiredException(jwt + " is a invalid jwt token ", t);
    }
  }

  /**
   * 处理JWT的Issued时间的偏移量.
   *
   * @param jwtIssuedTime JWT的真实时间数据.
   * @param create true:生成JWT的issued时间处理. false: 或者验证JWT的时间处理.
   * @return 偏移量处理过后的issued的毫秒数.
   */
  private long getIssuedTime(long jwtIssuedTime, boolean create) {
    if (create) {
      return jwtIssuedTime - this.issuedSecondsAheadOffset * 1000L;
    } else {
      return jwtIssuedTime + this.issuedSecondsAheadOffset * 1000L;
    }
  }

  public int getIssuedSecondsAheadOffset() {
    return issuedSecondsAheadOffset;
  }

  /**
   * 可为正,也可为负.
   */
  public void setIssuedSecondsAheadOffset(int issuedSecondsAheadOffset) {
    this.issuedSecondsAheadOffset = issuedSecondsAheadOffset;
  }
}
