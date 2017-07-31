package com.dianrong.common.uniauth.sharerw.facade;

import com.dianrong.common.uniauth.common.client.ApiCtrlAccountHolder;
import com.dianrong.common.uniauth.common.client.SimpleApiCtrlAccountHolder;
import com.dianrong.common.uniauth.common.client.UUIDHeaderClientRequestFilter;
import com.dianrong.common.uniauth.common.client.cxf.ApiCallCtlManager;
import com.dianrong.common.uniauth.common.client.cxf.ApiCallCtlSwitch;
import com.dianrong.common.uniauth.common.client.cxf.UniauthRSClientFactory;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.interfaces.read.IAuditResource;
import com.dianrong.common.uniauth.common.server.cxf.client.ClientFilterSingleton;
import com.dianrong.common.uniauth.common.util.ClientFacadeUtil;
import com.dianrong.common.uniauth.sharerw.interfaces.IConfigRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IDomainRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IGroupRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IPermissionRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IRoleRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.ITagRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.ITenancyRWResource;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserRWResource;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.client.ClientRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Arc on 14/2/16.
 */
@Component
public class UARWFacade {
  
  @Value("#{uniauthConfig['uniauth_ws_endpoint']}")
  private String uniWsEndpoint;

  @Value("#{uniauthConfig['uniauth_api_name']}")
  private String apiName;

  @Value("#{uniauthConfig['uniauth_api_key']}")
  private String apiKey;

  public UARWFacade() {}

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  private IDomainRWResource domainRWResource;
  private IGroupRWResource groupRWResource;
  private IPermissionRWResource permissionRWResource;
  private IRoleRWResource roleRWResource;
  private IUserRWResource userRWResource;
  private IAuditResource auditResource;
  private IConfigRWResource configRWResource;
  private ITagRWResource tagRWResource;
  private ITenancyRWResource tenancyRWResource;

  @Autowired(required = false)
  private ApiCtrlAccountHolder apiCtrlAccountHolder;

  /**
   * 初始化.
   */
  @PostConstruct
  public void init() {
    JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
    jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ClientRequestFilter cxfHeaderFilter = ClientFilterSingleton.getInstance();
    // set api control account
    if (apiCtrlAccountHolder != null) {
      ApiCallCtlManager.getInstance()
          .setAccount(apiCtrlAccountHolder.getAccount(), apiCtrlAccountHolder.getPassword())
          .setCtlSwitch(new ApiCallCtlSwitch() {
            @Override
            public boolean apiCtlOn() {
              if (allZkNodeMap != null) {
                return !"false".equalsIgnoreCase(
                    allZkNodeMap.get(AppConstants.UNIAUTH_SERVER_API_CALL_SWITCH));
              }
              return true;
            }
          });
    }
    UUIDHeaderClientRequestFilter uUIDHeaderClientRequestFilter =
        new UUIDHeaderClientRequestFilter();
    List<?> providers =
        Arrays.asList(jacksonJsonProvider, uUIDHeaderClientRequestFilter, cxfHeaderFilter);
    domainRWResource =
        UniauthRSClientFactory.create(uniWsEndpoint, IDomainRWResource.class, providers);
    groupRWResource =
        UniauthRSClientFactory.create(uniWsEndpoint, IGroupRWResource.class, providers);
    permissionRWResource =
        UniauthRSClientFactory.create(uniWsEndpoint, IPermissionRWResource.class, providers);
    userRWResource = UniauthRSClientFactory.create(uniWsEndpoint, IUserRWResource.class, providers);
    roleRWResource = UniauthRSClientFactory.create(uniWsEndpoint, IRoleRWResource.class, providers);
    auditResource = UniauthRSClientFactory.create(uniWsEndpoint, IAuditResource.class, providers);
    configRWResource =
        UniauthRSClientFactory.create(uniWsEndpoint, IConfigRWResource.class, providers);
    tagRWResource = UniauthRSClientFactory.create(uniWsEndpoint, ITagRWResource.class, providers);
    tenancyRWResource =
        UniauthRSClientFactory.create(uniWsEndpoint, ITenancyRWResource.class, providers);

    ClientFacadeUtil.addApiKey(apiName, apiKey, domainRWResource, groupRWResource,
        permissionRWResource, userRWResource, roleRWResource, auditResource, configRWResource,
        tagRWResource, tenancyRWResource);
  }

  /**
   * 构造函数.
   */
  public UARWFacade(String uniWsEndpoint, String account, String password) {
    this.uniWsEndpoint = uniWsEndpoint;
    SimpleApiCtrlAccountHolder simpleApiCtrlAccountHolder = new SimpleApiCtrlAccountHolder();
    simpleApiCtrlAccountHolder.setAccount(account);
    simpleApiCtrlAccountHolder.setPassword(password);
    apiCtrlAccountHolder = simpleApiCtrlAccountHolder;
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

  public void setApiCtrlAccountHolder(ApiCtrlAccountHolder apiCtrlAccountHolder) {
    this.apiCtrlAccountHolder = apiCtrlAccountHolder;
  }
}
