package com.dianrong.common.uniauth.server.support.apicontrl.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dianrong.common.uniauth.common.apicontrol.exp.InvalidTokenException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenCreateFailedException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenExpiredException;
import com.dianrong.common.uniauth.server.support.apicontrl.security.exp.JwtVerifierCreateFailedException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * JWT 操作相关的API 涉及到数据的签名和验证.
 *
 * @author wanglin
 */
@Slf4j
final class JwtSecurity {

  // 默认的秘钥
  public static final String DEFAULT_SECURITY_KEY =
      "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCud_DotADycQRhsiMSViewAxUJ"
          + "x3buJZLqd-VF0fO1N5dfZUJljxhgn7XU8itVV5dyTY-9nKgY6X0GLy2kB8K0yoszmaEhpp"
          + "AxumOkTNZnoyRHNstr4Osau-LZwRd2tzOmiVFB2hocmST3ewsmEVWOhS26ST-Mw"
          + "AVex7KzCroSYwIDAQAB";

  /**
   * 秘钥.
   */
  private final String securityKey;

  // jwt verifier
  private final JWTVerifier verifier;

  /**
   * @throws JwtVerifierCreateFailedException failed to create JWTVerifier.
   */
  JwtSecurity() throws JwtVerifierCreateFailedException {
    this(DEFAULT_SECURITY_KEY);
  }

  /**
   * 构造一个JwtSecurity.
   *
   * @param securityKey specify the secret key
   * @throws JwtVerifierCreateFailedException failed to create JWTVerifier
   */
  JwtSecurity(String securityKey) throws JwtVerifierCreateFailedException {
    Assert.notNull(securityKey);
    this.securityKey = securityKey;
    try {
      verifier = JWT.require(Algorithm.HMAC512(this.securityKey)).build();
    } catch (Exception e) {
      log.error("failed to create JWTVerifier ", e);
      throw new JwtVerifierCreateFailedException("failed create JWTVerifier ", e);
    }
  }

  /**
   * Create a new jwt.
   *
   * @param jwt jwt
   * @return token not null
   * @throws TokenCreateFailedException if create token failed
   */
  String createJwt(JwtInfo jwt) throws TokenCreateFailedException {
    try {
      Assert.notNull(jwt);
      Date issuedAt = new Date(jwt.getCreateTime());
      Date expiresAt = new Date(jwt.getExpireTime());
      return JWT.create().withIssuer(jwt.getIssuer()).withIssuedAt(issuedAt)
          .withExpiresAt(expiresAt).withAudience(jwt.getAudience()).withSubject(jwt.getSubject())
          .withClaim(JwtInfo.USER_NAME_KEY, jwt.getName())
          .withClaim(JwtInfo.USER_ACCOUNT_KEY, jwt.getAccount())
          .withClaim(JwtInfo.PERMISSION_KEY, jwt.getPermission())
          .sign(Algorithm.HMAC512(this.securityKey));
    } catch (Exception e) {
      log.error("failed create jwt ", e);
      throw new TokenCreateFailedException("failed create jwt: " + jwt, e);
    }
  }

  /**
   * Get jwt information from token.
   *
   * @param token token String
   * @return JwtParameter not null
   * @throws InvalidTokenException token is invalid
   * @throws TokenExpiredException token is expired
   */
  JwtInfo getInfoFromJwt(String token) throws InvalidTokenException, TokenExpiredException {
    try {
      Assert.notNull(token);
      DecodedJWT decodedJwt = this.verifier.verify(token);
      String audience = decodedJwt.getAudience() == null ? null
          : decodedJwt.getAudience().isEmpty() ? null : decodedJwt.getAudience().get(0);
      long createTime = decodedJwt.getIssuedAt().getTime();
      long expireTIme = decodedJwt.getExpiresAt().getTime();
      return new JwtInfo(decodedJwt.getIssuer(), audience, decodedJwt.getSubject(),
          decodedJwt.getClaim(JwtInfo.USER_NAME_KEY).asString(),
          decodedJwt.getClaim(JwtInfo.USER_ACCOUNT_KEY).asString(),
          decodedJwt.getClaim(JwtInfo.PERMISSION_KEY).asString(), createTime, expireTIme);
    } catch (InvalidClaimException invalidException) {
      if (invalidException.getMessage() != null
          && invalidException.getMessage().contains("The Token has expired on")) {
        throw new TokenExpiredException(token + " is expired");
      }
      log.error(token + " is a invalid jwt token ", invalidException);
      throw new InvalidTokenException(token + " is a invalid jwt token ", invalidException);
    } catch (Throwable t) {
      log.error(token + " is a invalid jwt token ", t);
      throw new InvalidTokenException(token + " is a invalid jwt token ", t);
    }
  }
}
