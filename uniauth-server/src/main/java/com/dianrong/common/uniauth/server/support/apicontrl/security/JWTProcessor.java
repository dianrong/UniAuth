package com.dianrong.common.uniauth.server.support.apicontrl.security;

import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.apicontrol.exp.InvalidTokenException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenCreateFailedException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenExpiredException;
import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.apicontrol.server.TokenProcessor;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.server.support.apicontrl.ApiCaller;
import com.dianrong.common.uniauth.server.support.apicontrl.ApiCtlPermission;
import com.dianrong.common.uniauth.server.support.apicontrl.security.exp.JWTVerifierCreateFailedException;

import lombok.extern.slf4j.Slf4j;

/**
 * jwt token processor 
 * @author wanglin
 */
@Component
@Slf4j
public class JWTProcessor implements TokenProcessor<ApiCtlPermission> {
    // define some constant string
    public static final String ISSUER = "uniauth-server";
    public static final String AUDIENCE = "all-uniauth-integrate-domains";
    public static final String SUBJECT = "uniauth-server-api-call-token";
    
    // init jwt 
    private final static JWTSecurity JWT;
    static {
        try {
            JWT = new JWTSecurity();
        } catch (JWTVerifierCreateFailedException e) {
            log.error("failed to create JWTSecurity", e);
            throw new UniauthCommonException(e.getMessage(), e);
        }  
    }
    @Override
    public String marshal(CallerCredential<ApiCtlPermission> credential) throws TokenCreateFailedException {
        Assert.notNull(credential);
        JwtInfo jwtInfo = new JwtInfo(ISSUER, AUDIENCE, SUBJECT, credential.getCallerName() ,
                credential.getAccount(), JsonUtil.object2Jason(credential.getPermissionInfo()),
                credential.getCreateTime(), credential.getExpiredTime());
        return JWT.createJwt(jwtInfo);
    }
    @Override
    public CallerCredential<ApiCtlPermission> unMarshal(String token) throws InvalidTokenException, TokenExpiredException {
        JwtInfo jwtInfo = JWT.getInfoFromJwt(token);
        // customized token check
        if (!ISSUER.equals(jwtInfo.getIssuer()) || !AUDIENCE.equals(jwtInfo.getAudience())
                || !SUBJECT.equals(jwtInfo.getSubject())) {
            log.error(token + " is a invalid token string");
            throw new InvalidTokenException();
        }
        try {
            ApiCtlPermission permission = JsonUtil.jsonToObject(jwtInfo.getPermission(), ApiCtlPermission.class);
            return new ApiCaller(jwtInfo.getAccount(), jwtInfo.getName(), permission, jwtInfo.getCreateTime(), jwtInfo.getExpireTime());
        } catch (Throwable t) {
            log.error(token + " is a invalid token string");
            throw new InvalidTokenException();
        }
    }
}
