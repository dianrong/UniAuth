package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

/**
 * judge credential's permission is passed
 * 
 * @author wanglin
 */
public interface PermissionJudger<T extends Serializable, E> {

    /**
     * decide whether the credential has permission to access the resource
     * 
     * @param Credential Credential
     * @param resource the resource
     * @return true or false
     */
    boolean judge(CallerCredential<T> Credential, E resource);
}
