package com.dianrong.common.uniauth.sharerw.facade;

import com.dianrong.common.uniauth.sharerw.interfaces.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Created by Arc on 14/2/16.
 */
public class UARWFacade {

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
