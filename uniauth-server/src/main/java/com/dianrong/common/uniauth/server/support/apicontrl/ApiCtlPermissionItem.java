package com.dianrong.common.uniauth.server.support.apicontrl;

import java.io.Serializable;

import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto.UriMethod;

/**
 * 权限信息
 * 
 * @author wanglin
 */
public class ApiCtlPermissionItem implements Serializable {

    private static final long serialVersionUID = -4027612354034958011L;

    private String uri;

    private UriMethod method;

    public String getUri() {
        return uri;
    }

    public ApiCtlPermissionItem setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public UriMethod getMethod() {
        return method;
    }

    public ApiCtlPermissionItem setMethod(UriMethod method) {
        this.method = method;
        return this;
    }
}
