package com.dianrong.common.uniauth.sharerw.facade;

import com.dianrong.common.uniauth.common.interfaces.read.IAuditResource;
import com.dianrong.common.uniauth.sharerw.interfaces.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

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
    private ITagRWResource tagRWResource;
    private IUserExtendRWResource userExtendRWResource;
    private IUserExtendValRWResource userExtendValRWResource;

    @PostConstruct
    public void init(){
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
        jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        userExtendRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserExtendRWResource.class, Arrays.asList(jacksonJsonProvider));
        userExtendValRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserExtendValRWResource.class, Arrays.asList(jacksonJsonProvider));
        domainRWResource = JAXRSClientFactory.create(uniWsEndpoint, IDomainRWResource.class, Arrays.asList(jacksonJsonProvider));
        groupRWResource = JAXRSClientFactory.create(uniWsEndpoint, IGroupRWResource.class, Arrays.asList(jacksonJsonProvider));
        permissionRWResource = JAXRSClientFactory.create(uniWsEndpoint, IPermissionRWResource.class, Arrays.asList(jacksonJsonProvider));
        userRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserRWResource.class, Arrays.asList(jacksonJsonProvider));
        roleRWResource = JAXRSClientFactory.create(uniWsEndpoint, IRoleRWResource.class, Arrays.asList(jacksonJsonProvider));
        auditResource = JAXRSClientFactory.create(uniWsEndpoint, IAuditResource.class, Arrays.asList(jacksonJsonProvider));
        configRWResource = JAXRSClientFactory.create(uniWsEndpoint, IConfigRWResource.class, Arrays.asList(jacksonJsonProvider));
        tagRWResource = JAXRSClientFactory.create(uniWsEndpoint, ITagRWResource.class, Arrays.asList(jacksonJsonProvider));
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

    public String getUniWsEndpoint() {
        return uniWsEndpoint;
    }

    public ITagRWResource getTagRWResource() {
        return tagRWResource;
    }

    public IUserExtendRWResource getUserExtendRWResource() {
        return userExtendRWResource;
    }

    public IUserExtendValRWResource getUserExtendValRWResource() {
        return userExtendValRWResource;
    }
}
