package com.dianrong.common.uniauth.common.client;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.interfaces.read.IDomainResource;
import com.dianrong.common.uniauth.common.interfaces.read.IGroupResource;
import com.dianrong.common.uniauth.common.interfaces.read.IPermissionResource;
import com.dianrong.common.uniauth.common.interfaces.read.IUserResource;
import com.dianrong.common.uniauth.common.util.CheckZkConfig;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Component
public class UniClientFacade {
	
	@Value("#{uniauthConfig['uniauth_ws_endpoint']}")
	private String uniWsEndpoint;
	
	private IDomainResource domainResource;
	private IGroupResource groupResource;
	private IPermissionResource permissionResource;
	private IUserResource userResource;
	
	@PostConstruct
	public void init(){
		CheckZkConfig.checkZkConfig(uniWsEndpoint, "/com/dianrong/cfg/1.0.0/uniauth/uniauth_ws_endpoint", "uniauth ws endpoint");
		JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
		domainResource = JAXRSClientFactory.create(uniWsEndpoint, IDomainResource.class, Arrays.asList(jacksonJsonProvider));
		groupResource = JAXRSClientFactory.create(uniWsEndpoint, IGroupResource.class, Arrays.asList(jacksonJsonProvider));
		permissionResource = JAXRSClientFactory.create(uniWsEndpoint, IPermissionResource.class, Arrays.asList(jacksonJsonProvider));
		userResource = JAXRSClientFactory.create(uniWsEndpoint, IUserResource.class, Arrays.asList(jacksonJsonProvider));
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
}
