package com.dianrong.common.uniauth.sharerw.facade;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.sharerw.interfaces.IDomainRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IGroupRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IPermissionRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IRoleRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserRWResource;
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

    @PostConstruct
    public void init(){
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
        domainRWResource = JAXRSClientFactory.create(uniWsEndpoint, IDomainRWResource.class, Arrays.asList(jacksonJsonProvider));
        groupRWResource = JAXRSClientFactory.create(uniWsEndpoint, IGroupRWResource.class, Arrays.asList(jacksonJsonProvider));
        permissionRWResource = JAXRSClientFactory.create(uniWsEndpoint, IPermissionRWResource.class, Arrays.asList(jacksonJsonProvider));
        userRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserRWResource.class, Arrays.asList(jacksonJsonProvider));
        roleRWResource = JAXRSClientFactory.create(uniWsEndpoint, IRoleRWResource.class, Arrays.asList(jacksonJsonProvider));
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
}
