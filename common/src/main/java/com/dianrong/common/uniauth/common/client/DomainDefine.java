package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryConfiguration;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.client.enums.CasPermissionControlType;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
public class DomainDefine implements Serializable {

  private static final long serialVersionUID = 7044166044801306772L;
  private String domainCode;
  private String userInfoClass;
  private boolean rejectPublicInvocations;
  // 如果在登录成功跳转前有session,如果此配置有值,则跳转此Url.
  private String customizedSavedRequestUrl;
  // 如果在登录成功的跳转之前,集成系统中没有创建session,则跳转此Url,默认为'/'.
  private String customizedLoginRedirecUrl;
  private boolean useAllDomainUserInfoShareMode;

  // 采用的身份认证方式集合.
  private List<AuthenticationType> authenticationTypeList = Arrays.asList(AuthenticationType.ALL);

  /**
   * ServiceTicket的验证是否走内网.
   */
  private boolean serviceTicketValidateWithInnerAddress;

  /**
   * 集成系统的集成ssclient中的内存是否采用Redis,否则采用依赖ConcurrentHashMap实现的一个简易的内存实现. 默认为false.
   * 如果设置为true,需要配置Redis相关信息.具体参见Zk的配置文档.
   */
  private boolean innerCacheUseRedis = false;

  /**
   * 如果SSClient采用的缓存实现是Redis(innerCacheUseRedis=true),则可通过该参数来配置Redis的一些参数.
   */
  private RedisConnectionFactoryConfiguration innerCacheRedisConfiguration;

  /**
   * 权限控制类型定义,默认为使用uri_pattern.
   */
  private CasPermissionControlType controlType = CasPermissionControlType.URI_PATTERN;

  private static Integer domainId;
  // 每一个服务就只有一个的域名code定义
  private static String staticDomainCode;

  @Autowired
  private transient UniClientFacade uniClientFacade;

  /**
   * DomainDefine的初始化工作,会check配置的Domain Code是否正确.
   */
  @PostConstruct
  public void init() {
    Assert.notNull(this.domainCode, "please config domain code , it can not be null");
    // init static field domainId
    log.info("DomainDefine init, query domainId");
    domainId = getDomainId(this.domainCode);
  }

  // domainCode can not be null
  private Integer getDomainId(String domainCode) {
    Assert.notNull(domainCode);
    List<String> codes = new ArrayList<String>();
    codes.add(this.domainCode);
    Response<List<DomainDto>> result = null;
    while (true) {
      try {
        result = uniClientFacade.getDomainResource()
            .getAllLoginDomains(new DomainParam().setDomainCodeList(codes));
        if (result == null) {
          throw new UniauthCommonException(
              "Init DomainDefine failed, please check uniauth-server is already "
                  + "running or the server can connect to uniauth-server");
        } else {
          break;
        }
      } catch (Exception ex) {
        log.warn("failed to get domain Id, retry after 1 minitue", ex);
      }
      try {
        Thread.sleep(1000L * 60L);
      } catch (InterruptedException e) {
        log.warn("thread error", e);
      }
    }
    if (result.getInfo() != null && !result.getInfo().isEmpty()) {
      throw new UniauthCommonException(
          "query domain info by configuration:domainCode failed, please check "
              + "domainCode is correct or uniauth-server is alreay running");
    }
    List<DomainDto> data = result.getData();
    if (data == null || data.isEmpty()) {
      throw new UniauthCommonException(String.format(
          "please check whether the configuration:domainCode [%s] is correct! Need "
              + "configure a domain in Techops, the domain code is equals [%s]",
          domainCode, domainCode));
    }
    return data.get(0).getId();
  }

  public static Integer getDomainId() {
    return domainId;
  }

  /**
   * 提供静态方法获取当前集成系统的Domain Code.
   */
  public static String getStaticDomainCode() {
    if (DomainDefine.staticDomainCode == null) {
      throw new UniauthCommonException(
          "before call getStaticDomainCode, need set domainCode first");
    }
    return DomainDefine.staticDomainCode;
  }

  public String getDomainCode() {
    return domainCode;
  }

  public void setDomainCode(String domainCode) {
    this.domainCode = domainCode;
    DomainDefine.staticDomainCode = domainCode;
  }

  public String getUserInfoClass() {
    return userInfoClass;
  }

  public void setUserInfoClass(String userInfoClass) {
    this.userInfoClass = userInfoClass;
  }

  public boolean isRejectPublicInvocations() {
    return rejectPublicInvocations;
  }

  public void setRejectPublicInvocations(boolean rejectPublicInvocations) {
    this.rejectPublicInvocations = rejectPublicInvocations;
  }

  public String getCustomizedSavedRequestUrl() {
    return customizedSavedRequestUrl;
  }

  public void setCustomizedSavedRequestUrl(String customizedSavedRequestUrl) {
    this.customizedSavedRequestUrl = customizedSavedRequestUrl;
  }

  public String getCustomizedLoginRedirecUrl() {
    return customizedLoginRedirecUrl;
  }

  public void setCustomizedLoginRedirecUrl(String customizedLoginRedirecUrl) {
    this.customizedLoginRedirecUrl = customizedLoginRedirecUrl;
  }

  public static void setDomainId(Integer domainId) {
    DomainDefine.domainId = domainId;
  }

  public String getControlType() {
    return controlType.getTypeStr();
  }

  public boolean isUseAllDomainUserInfoShareMode() {
    return useAllDomainUserInfoShareMode;
  }

  public void setUseAllDomainUserInfoShareMode(boolean useAllDomainUserInfoShareMode) {
    this.useAllDomainUserInfoShareMode = useAllDomainUserInfoShareMode;
  }

  /**
   * Set permission control type.
   *
   * @throws IllegalArgumentException if parameter type is not a legal CasPermissionControlType
   * string
   */
  public void setControlType(String type) throws IllegalArgumentException {
    try {
      this.controlType = CasPermissionControlType.valueOf(type);
    } catch (IllegalArgumentException ex) {
      log.debug("illegal argument", ex);
      throw new IllegalArgumentException("permission control type supports ["
          + CasPermissionControlType.allType() + "], please check the param '" + type + "'");
    }
  }

  /**
   * 判断当前的定义的权限控制模式是否支持指定的类型.
   *
   * @param type 指定类型的权限控制模式
   * @return true or false
   */
  public boolean controlTypeSupport(CasPermissionControlType type) {
    if (type == null) {
      log.error("controlTypeSupport param is null");
      return false;
    }
    return this.controlType.support(type.getTypeStr());
  }

  public boolean isServiceTicketValidateWithInnerAddress() {
    return serviceTicketValidateWithInnerAddress;
  }

  public void setServiceTicketValidateWithInnerAddress(
      boolean serviceTicketValidateWithInnerAddress) {
    this.serviceTicketValidateWithInnerAddress = serviceTicketValidateWithInnerAddress;
  }

  public void setAuthenticationType(String authenticationType) {
    if (StringUtils.isEmpty(authenticationType)) {
      log.info("setAuthenticationType parameter authenticationType is empty, so just ignore. ");
      return;
    }
    String[] authenticationTypeItems = authenticationType.split(",");
    List<AuthenticationType> authenticationTypes = new ArrayList<>(authenticationTypeItems.length);
    for (String item : authenticationTypeItems) {
      authenticationTypes.add(AuthenticationType.valueOf(item));
    }
    this.authenticationTypeList = authenticationTypes;
  }

  public List<AuthenticationType> getAuthenticationTypeList() {
    return Collections.unmodifiableList(authenticationTypeList);
  }

  public boolean isInnerCacheUseRedis() {
    return innerCacheUseRedis;
  }

  public void setInnerCacheUseRedis(boolean innerCacheUseRedis) {
    this.innerCacheUseRedis = innerCacheUseRedis;
  }

  public RedisConnectionFactoryConfiguration getInnerCacheRedisConfiguration() {
    return innerCacheRedisConfiguration;
  }

  public void setInnerCacheRedisConfiguration(
      RedisConnectionFactoryConfiguration innerCacheRedisConfiguration) {
    this.innerCacheRedisConfiguration = innerCacheRedisConfiguration;
  }
}
