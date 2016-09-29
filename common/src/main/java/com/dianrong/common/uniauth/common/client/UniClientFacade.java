package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.interfaces.read.*;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendRWResource;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendValRWResource;
import com.dianrong.common.uniauth.common.server.UniauthCxfClientLocaleFilter;
import com.dianrong.common.uniauth.common.util.CheckSDKCfg;
import com.dianrong.common.uniauth.common.util.ClientFacadeUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class UniClientFacade {

    @Value("#{uniauthConfig['uniauth_ws_endpoint']}")
    private String uniWsEndpoint;

    @Value("#{uniauthConfig['uniauth_api_name']}")
    private String apiName;

    @Value("#{uniauthConfig['uniauth_api_key']}")
    private String apiKey;

	public UniClientFacade(){}
	public UniClientFacade(String uniWsEndpoint){
		this.uniWsEndpoint = uniWsEndpoint;
		init();
	}
	public UniClientFacade(String uniWsEndpoint, String apiName, String apiKey){
		this.uniWsEndpoint = uniWsEndpoint;
		this.apiName = apiName;
		this.apiKey = apiKey;
		init();
	}

    private IDomainResource domainResource;
    private IGroupResource groupResource;
    private IPermissionResource permissionResource;
    private IUserResource userResource;
    private IRoleResource roleResource;
    private ITagResource tagResource;
    private IConfigResource configResource;
    private IUserExtendResource userExtendResource;
    private IUserExtendValResource userExtendValResource;
    private ITenancyResource tenancyResource;
    
    // read and write
    private IUserExtendRWResource userExtendRWResource;
    private IUserExtendValRWResource userExtendValRWResource;

	@PostConstruct
	public void init(){
		CheckSDKCfg.checkSDKCfg(uniWsEndpoint);
		JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
		jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		UUIDHeaderClientRequestFilter uUIDHeaderClientRequestFilter = new UUIDHeaderClientRequestFilter();
        UniauthCxfClientLocaleFilter localeFilter = new UniauthCxfClientLocaleFilter();
        List<?> providers = Arrays.asList(jacksonJsonProvider,uUIDHeaderClientRequestFilter,localeFilter);
        userExtendResource = JAXRSClientFactory.create(uniWsEndpoint, IUserExtendResource.class, providers);
        userExtendValResource = JAXRSClientFactory.create(uniWsEndpoint, IUserExtendValResource.class, providers);
		domainResource = JAXRSClientFactory.create(uniWsEndpoint, IDomainResource.class, providers);
		groupResource = JAXRSClientFactory.create(uniWsEndpoint, IGroupResource.class, providers);
		permissionResource = JAXRSClientFactory.create(uniWsEndpoint, IPermissionResource.class, providers);
		userResource = JAXRSClientFactory.create(uniWsEndpoint, IUserResource.class, providers);
		roleResource = JAXRSClientFactory.create(uniWsEndpoint, IRoleResource.class, providers);
		tagResource = JAXRSClientFactory.create(uniWsEndpoint, ITagResource.class, providers);
		configResource = JAXRSClientFactory.create(uniWsEndpoint, IConfigResource.class, providers);
		tenancyResource = JAXRSClientFactory.create(uniWsEndpoint, ITenancyResource.class, providers);
		
		// write
		userExtendRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserExtendRWResource.class, providers);
		userExtendValRWResource = JAXRSClientFactory.create(uniWsEndpoint, IUserExtendValRWResource.class, providers);
		ClientFacadeUtil.addApiKey(apiName,apiKey,domainResource,groupResource,permissionResource,userResource,roleResource,tagResource,
				configResource,userExtendResource,userExtendValResource, userExtendRWResource, userExtendValRWResource);
	}

    public IDomainResource getDomainResource() {
        return domainResource;
    }

    public String getUniWsEndpoint() {
        return uniWsEndpoint;
    }

    public IGroupResource getGroupResource() {
        return groupResource;
    }

    public IPermissionResource getPermissionResource() {
        return permissionResource;
    }

    public IUserResource getUserResource() {
        return userResource;
    }

    public IRoleResource getRoleResource() {
        return roleResource;
    }

    public ITagResource getTagResource() {
        return tagResource;
    }

    public IConfigResource getConfigResource() {
        return configResource;
    }

    public IUserExtendResource getUserExtendResource() {
        return userExtendResource;
    }

    public IUserExtendValResource getUserExtendValResource() {
        return userExtendValResource;
    }
	public IUserExtendRWResource getUserExtendRWResource() {
		return userExtendRWResource;
	}
	public void setUserExtendRWResource(IUserExtendRWResource userExtendRWResource) {
		this.userExtendRWResource = userExtendRWResource;
	}
	public IUserExtendValRWResource getUserExtendValRWResource() {
		return userExtendValRWResource;
	}
	public void setUserExtendValRWResource(IUserExtendValRWResource userExtendValRWResource) {
		this.userExtendValRWResource = userExtendValRWResource;
	}
	public ITenancyResource getTenancyResource() {
		return tenancyResource;
	}
}
