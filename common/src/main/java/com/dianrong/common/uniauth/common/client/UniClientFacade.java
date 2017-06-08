package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.client.cxf.ApiCallCtlManager;
import com.dianrong.common.uniauth.common.client.cxf.ApiCallCtlSwitch;
import com.dianrong.common.uniauth.common.client.cxf.UniauthRSClientFactory;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.interfaces.read.IAttributeExtendResource;
import com.dianrong.common.uniauth.common.interfaces.read.IConfigResource;
import com.dianrong.common.uniauth.common.interfaces.read.IDomainResource;
import com.dianrong.common.uniauth.common.interfaces.read.IGroupResource;
import com.dianrong.common.uniauth.common.interfaces.read.IPermissionResource;
import com.dianrong.common.uniauth.common.interfaces.read.IRoleResource;
import com.dianrong.common.uniauth.common.interfaces.read.ITagResource;
import com.dianrong.common.uniauth.common.interfaces.read.ITenancyResource;
import com.dianrong.common.uniauth.common.interfaces.read.IUserExtendResource;
import com.dianrong.common.uniauth.common.interfaces.read.IUserExtendValResource;
import com.dianrong.common.uniauth.common.interfaces.read.IUserResource;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IAttributeExtendRWResource;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendRWResource;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendValRWResource;
import com.dianrong.common.uniauth.common.server.cxf.client.ClientFilterSingleton;
import com.dianrong.common.uniauth.common.util.CheckSdkCfg;
import com.dianrong.common.uniauth.common.util.ClientFacadeUtil;
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

@SuppressWarnings("deprecation")
@Component
public class UniClientFacade {

  @Value("#{uniauthConfig['uniauth_ws_endpoint']}")
  private String uniWsEndpoint;

  @Value("#{uniauthConfig['uniauth_api_name']}")
  private String apiName;

  @Value("#{uniauthConfig['uniauth_api_key']}")
  private String apiKey;

  @Autowired(required = false)
  private ApiCtrlAccountHolder apiCtrlAccountHolder;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  public UniClientFacade() {
  }

  public UniClientFacade(String uniWsEndpoint) {
    this.uniWsEndpoint = uniWsEndpoint;
    init();
  }

  /**
   * 构造一个API调用客户端.
   */
  public UniClientFacade(String uniWsEndpoint, String apiName, String apiKey, String account,
      String password) {
    this.uniWsEndpoint = uniWsEndpoint;
    this.apiName = apiName;
    this.apiKey = apiKey;
    SimpleApiCtrlAccountHolder simpleApiCtrlAccountHolder = new SimpleApiCtrlAccountHolder();
    simpleApiCtrlAccountHolder.setAccount(account);
    simpleApiCtrlAccountHolder.setPassword(password);
    apiCtrlAccountHolder = simpleApiCtrlAccountHolder;
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
  private IAttributeExtendResource attributeExtendResource;

  // read and write
  private IUserExtendRWResource userExtendRWResource;
  private IUserExtendValRWResource userExtendValRWResource;
  private IAttributeExtendRWResource attributeExtendRWResource;

  /**
   * 初始化.
   */
  @PostConstruct
  public void init() {
    CheckSdkCfg.checkSdkCfg(uniWsEndpoint);
    JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
    jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    UUIDHeaderClientRequestFilter uUIDHeaderClientRequestFilter = new UUIDHeaderClientRequestFilter();
    ClientRequestFilter cxfHeaderFilter = ClientFilterSingleton.getInstance();
    // set api control account
    if (apiCtrlAccountHolder != null) {
      ApiCallCtlManager.getInstance()
          .setAccount(apiCtrlAccountHolder.getAccount(), apiCtrlAccountHolder.getPassword())
          // 设置开关
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
    List<?> providers = Arrays
        .asList(jacksonJsonProvider, uUIDHeaderClientRequestFilter, cxfHeaderFilter);
    userExtendResource = UniauthRSClientFactory
        .create(uniWsEndpoint, IUserExtendResource.class, providers);
    userExtendValResource = UniauthRSClientFactory
        .create(uniWsEndpoint, IUserExtendValResource.class, providers);
    domainResource = UniauthRSClientFactory.create(uniWsEndpoint, IDomainResource.class, providers);
    groupResource = UniauthRSClientFactory.create(uniWsEndpoint, IGroupResource.class, providers);
    permissionResource = UniauthRSClientFactory
        .create(uniWsEndpoint, IPermissionResource.class, providers);
    userResource = UniauthRSClientFactory.create(uniWsEndpoint, IUserResource.class, providers);
    roleResource = UniauthRSClientFactory.create(uniWsEndpoint, IRoleResource.class, providers);
    tagResource = UniauthRSClientFactory.create(uniWsEndpoint, ITagResource.class, providers);
    configResource = UniauthRSClientFactory.create(uniWsEndpoint, IConfigResource.class, providers);
    tenancyResource = UniauthRSClientFactory
        .create(uniWsEndpoint, ITenancyResource.class, providers);
    attributeExtendResource = UniauthRSClientFactory
        .create(uniWsEndpoint, IAttributeExtendResource.class, providers);

    // write
    userExtendRWResource = UniauthRSClientFactory
        .create(uniWsEndpoint, IUserExtendRWResource.class, providers);
    userExtendValRWResource = UniauthRSClientFactory
        .create(uniWsEndpoint, IUserExtendValRWResource.class, providers);
    attributeExtendRWResource = UniauthRSClientFactory
        .create(uniWsEndpoint, IAttributeExtendRWResource.class, providers);
    ClientFacadeUtil
        .addApiKey(apiName, apiKey, domainResource, groupResource, permissionResource, userResource,
            roleResource, tagResource, configResource, tenancyResource,
            userExtendResource, userExtendValResource, userExtendRWResource,
            userExtendValRWResource, attributeExtendResource, attributeExtendRWResource);
  }

  public void setApiCtrlAccountHolder(ApiCtrlAccountHolder apiCtrlAccountHolder) {
    this.apiCtrlAccountHolder = apiCtrlAccountHolder;
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

  /**
   * @see getAttributeExtendResource
   */
  @Deprecated
  public IUserExtendResource getUserExtendResource() {
    return userExtendResource;
  }

  public IUserExtendValResource getUserExtendValResource() {
    return userExtendValResource;
  }

  /**
   * @see getAttributeExtendRWResource.
   */
  @Deprecated
  public IUserExtendRWResource getUserExtendRWResource() {
    return userExtendRWResource;
  }

  public IUserExtendValRWResource getUserExtendValRWResource() {
    return userExtendValRWResource;
  }

  public ITenancyResource getTenancyResource() {
    return tenancyResource;
  }

  public IAttributeExtendResource getAttributeExtendResource() {
    return attributeExtendResource;
  }

  public IAttributeExtendRWResource getAttributeExtendRWResource() {
    return attributeExtendRWResource;
  }
}
