package com.dianrong.common.uniauth.sharerw.facade;

import com.dianrong.common.uniauth.common.client.UUIDHeaderClientRequestFilter;
import com.dianrong.common.uniauth.common.interfaces.read.IAuditResource;
import com.dianrong.common.uniauth.common.server.UniauthCxfClientLocaleFilter;
import com.dianrong.common.uniauth.sharerw.interfaces.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

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
    private ITenancyRWResource tenancyRWResource;

    @PostConstruct
    public void init(){
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
        jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UniauthCxfClientLocaleFilter localeFilter = new UniauthCxfClientLocaleFilter();
        UUIDHeaderClientRequestFilter uUIDHeaderClientRequestFilter = new UUIDHeaderClientRequestFilter();
        List<?> providers = Arrays.asList(jacksonJsonProvider,uUIDHeaderClientRequestFilter,localeFilter);
        domainRWResource = JAXRSClientFactory.create(uniWsEndpoint, IDomainRWResource.class, providers);
        groupRWResource = JAXRSClientFactory.create(uniWsEndpoint, IGroupRWResource.class, providers);
        permissionRWResource = JAXRSClientFactory.create(uniWsEndpoint, IPermissionRWResource.class, providers);
        userRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserRWResource.class, providers);
        roleRWResource = JAXRSClientFactory.create(uniWsEndpoint, IRoleRWResource.class, providers);
        auditResource = JAXRSClientFactory.create(uniWsEndpoint, IAuditResource.class, providers);
        configRWResource = JAXRSClientFactory.create(uniWsEndpoint, IConfigRWResource.class, providers);
        tagRWResource = JAXRSClientFactory.create(uniWsEndpoint, ITagRWResource.class, providers);
        tenancyRWResource = JAXRSClientFactory.create(uniWsEndpoint, ITenancyRWResource.class, providers);
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

	public ITenancyRWResource getTenancyRWResource() {
		return tenancyRWResource;
	}
}
