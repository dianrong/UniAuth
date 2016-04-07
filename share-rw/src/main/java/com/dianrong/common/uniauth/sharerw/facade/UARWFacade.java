package com.dianrong.common.uniauth.sharerw.facade;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import com.dianrong.common.uniauth.common.interfaces.read.IAuditResource;
import com.dianrong.common.uniauth.common.interfaces.read.IConfigResource;
import com.dianrong.common.uniauth.common.interfaces.read.ITagResource;
import com.dianrong.common.uniauth.sharerw.interfaces.*;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Created by Arc on 14/2/16.
 */
@Component
public class UARWFacade {
	
	@Value("#{uniauthConfig['uniauth_ws_endpoint']}")
    private String uniWsEndpoint;

    private IDomainRWResource domainRWResource;
    private IGroupRWResource groupRWResource;
    private IPermissionRWResource permissionRWResource;
    private IRoleRWResource roleRWResource;
    private IUserRWResource userRWResource;
    private IAuditResource auditResource;
    private IConfigRWResource configRWResource;
    private ITagResource tagResource;

    @PostConstruct
    public void init(){
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
        domainRWResource = JAXRSClientFactory.create(uniWsEndpoint, IDomainRWResource.class, Arrays.asList(jacksonJsonProvider));
        groupRWResource = JAXRSClientFactory.create(uniWsEndpoint, IGroupRWResource.class, Arrays.asList(jacksonJsonProvider));
        permissionRWResource = JAXRSClientFactory.create(uniWsEndpoint, IPermissionRWResource.class, Arrays.asList(jacksonJsonProvider));
        userRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserRWResource.class, Arrays.asList(jacksonJsonProvider));
        roleRWResource = JAXRSClientFactory.create(uniWsEndpoint, IRoleRWResource.class, Arrays.asList(jacksonJsonProvider));
        auditResource = JAXRSClientFactory.create(uniWsEndpoint, IAuditResource.class, Arrays.asList(jacksonJsonProvider));
        configRWResource = JAXRSClientFactory.create(uniWsEndpoint, IConfigRWResource.class, Arrays.asList(jacksonJsonProvider));
        tagResource = JAXRSClientFactory.create(uniWsEndpoint, ITagResource.class, Arrays.asList(jacksonJsonProvider));
    }

    public UARWFacade setUniWsEndpoint(String uniWsEndpoint) {
        this.uniWsEndpoint = uniWsEndpoint;
        return this;
    }

    public IDomainRWResource getDomainRWResource() {
        return domainRWResource;
    }

    public IGroupRWResource getGroupRWResource() {
        return groupRWResource;
    }

    public IPermissionRWResource getPermissionRWResource() {
        return permissionRWResource;
    }

    public IRoleRWResource getRoleRWResource() {
        return roleRWResource;
    }

    public IUserRWResource getUserRWResource() {
        return userRWResource;
    }

    public IAuditResource getAuditResource() {
        return auditResource;
    }

    public IConfigRWResource getConfigRWResource() {
        return configRWResource;
    }

    public ITagResource getTagResource() {
        return tagResource;
    }
}
