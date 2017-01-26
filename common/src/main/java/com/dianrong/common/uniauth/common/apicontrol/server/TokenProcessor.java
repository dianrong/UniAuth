package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

import com.dianrong.common.uniauth.common.apicontrol.exp.InvalidTokenException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenCreateFailedException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenExpiredException;

/**
 * token process interface 
 * @author wanglin
 * @param <T>  WillExpiredCallerCredential type
 */
public interface TokenProcessor<T extends Serializable> {
    /**
     * generate token by  WillExpiredCallerCredential
     * @param credential credential
     * @return token
     * @throws TokenCreateFailedException if create token failed
     */
    String  marshal(CallerCredential<T> credential) throws TokenCreateFailedException;
    
    /**
     * unMarshal token
     * @param token can not be null
     * @return  WillExpiredCallerCredential
     * @throws InvalidTokenException if token is invalid
     * @throws TokenExpiredException if token is expired
     */
    CallerCredential<T> unMarshal(String token) throws InvalidTokenException, TokenExpiredException;
}
