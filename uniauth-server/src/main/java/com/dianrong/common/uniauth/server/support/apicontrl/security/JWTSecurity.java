package com.dianrong.common.uniauth.server.support.apicontrl.security;

import java.util.Date;

import org.springframework.util.Assert;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dianrong.common.uniauth.common.apicontrol.exp.InvalidTokenException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenCreateFailedException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenExpiredException;
import com.dianrong.common.uniauth.server.support.apicontrl.security.exp.JWTVerifierCreateFailedException;

import lombok.extern.slf4j.Slf4j;

/**
 * jwt 操作相关的api
 * 涉及到数据的签名和验证 
 * @author wanglin
 */
@Slf4j
final class JWTSecurity {
    
    // 指定秘钥
    private static final String SECURITY_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCud_DotADycQRhsiMSViewAxUJx3buJZLqd-VF0fO1N5dfZUJljxhgn7XU8itVV5dyTY-9nKgY6X0GLy2kB8K0yoszmaEhppAxumOkTNZnoyRHNstr4Osau-LZwRd2tzOmiVFB2hocmST3ewsmEVWOhS26ST-MwAVex7KzCroSYwIDAQAB";
    
    // jwt verifier
    private final JWTVerifier verifier;
    
    /**
     * @throws JWTVerifierCreateFailedException failed to create JWTVerifier
     */
    JWTSecurity() throws JWTVerifierCreateFailedException {
        try {
            verifier= JWT.require(Algorithm.HMAC512(SECURITY_KEY)).build();
        } catch(Exception e) {
            log.error("failed to create JWTVerifier ", e);
            throw new JWTVerifierCreateFailedException("failed create JWTVerifier ", e);
        }
    }
    
    /**
     * create a new jwt
     * @param jwt jwt
     * @return token not null
     * @throws TokenCreateFailedException if create token failed
     */
    String createJwt(JwtInfo jwt) throws TokenCreateFailedException {
        try {
            Assert.notNull(jwt);
            Date issuedAt = new Date(jwt.getCreateTime());
            Date expiresAt = new Date(jwt.getExpireTime());
            return JWT.create().withIssuer(jwt.getIssuer())
                    .withIssuedAt(issuedAt)
                    .withExpiresAt(expiresAt)
                    .withAudience(jwt.getAudience())
                    .withSubject(jwt.getSubject())
                    .withClaim(JwtInfo.USER_NAME_KEY, jwt.getName())
                    .withClaim(JwtInfo.USER_ACCOUNT_KEY, jwt.getAccount())
                    .withClaim(JwtInfo.PERMISSION_KEY, jwt.getPermission())
                    .sign(Algorithm.HMAC512(SECURITY_KEY));
        } catch (Exception e) {
            log.error("failed create jwt ", e);
            throw new TokenCreateFailedException("failed create jwt", e);
        }
    }
    
    /**
     * get jwt information from token
     * @param token token String
     * @return JwtParameter not null
     * @throws InvalidTokenException
     * @throws TokenExpiredException token is expired
     */
    JwtInfo getInfoFromJwt(String token) throws InvalidTokenException, TokenExpiredException {
        try {
            Assert.notNull(token);
            DecodedJWT decodedJWT = this.verifier.verify(token);
            String audience = decodedJWT.getAudience() == null ? null : decodedJWT.getAudience().isEmpty() ? null : decodedJWT.getAudience().get(0);
            long createTime = decodedJWT.getIssuedAt().getTime();
            long expireTIme = decodedJWT.getExpiresAt().getTime();
            return new JwtInfo(decodedJWT.getIssuer(), audience, decodedJWT.getSubject(),
                    decodedJWT.getClaim(JwtInfo.USER_NAME_KEY).asString(),
                    decodedJWT.getClaim(JwtInfo.USER_ACCOUNT_KEY).asString(),
                    decodedJWT.getClaim(JwtInfo.PERMISSION_KEY).asString(),
                    createTime , expireTIme);
        } catch(InvalidClaimException invalidException) {
            if (invalidException.getMessage() != null && invalidException.getMessage().contains("The Token has expired on")) {
                throw new TokenExpiredException();
            } 
            log.error(token + " is a invalid jwt token ", invalidException);
            throw new InvalidTokenException(token + " is a invalid jwt token ", invalidException);
        } catch (Throwable t) {
            log.error(token + " is a invalid jwt token ", t);
            throw new InvalidTokenException(token + " is a invalid jwt token ", t);
        }
    }
}
