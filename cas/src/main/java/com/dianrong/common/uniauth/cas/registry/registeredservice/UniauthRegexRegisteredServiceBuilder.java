package com.dianrong.common.uniauth.cas.registry.registeredservice;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jasig.cas.authentication.principal.DefaultPrincipalAttributesRepository;
import org.jasig.cas.services.AttributeReleasePolicy;
import org.jasig.cas.services.DefaultRegisteredServiceAccessStrategy;
import org.jasig.cas.services.DefaultRegisteredServiceUsernameProvider;
import org.jasig.cas.services.LogoutType;
import org.jasig.cas.services.RefuseRegisteredServiceProxyPolicy;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.RegisteredServiceAccessStrategy;
import org.jasig.cas.services.RegisteredServiceProxyPolicy;
import org.jasig.cas.services.RegisteredServicePublicKey;
import org.jasig.cas.services.RegisteredServiceUsernameAttributeProvider;
import org.jasig.cas.services.ReturnAllowedAttributeReleasePolicy;

/**
 * 一个属性可配的类,用于生成RegexRegisteredService
 * <p>
 * 默认值完全来自于文件: HTTPSandIMAPS-10000001.json
 * </p>
 *
 * @author wanglin
 */
public class UniauthRegexRegisteredServiceBuilder implements RegisteredServiceBuilder {

  /**
   * 设置所有属性的默认值.
   **/
  private String serviceId = "^(http|https|imaps)://.*";

  private String name = "HTTP/HTTPS and IMAPS";

  private long id = Long.MAX_VALUE;

  private String description = "";

  private String theme = "";

  private int evaluationOrder = 0;

  private Set<String> requiredHandlers;

  private LogoutType logoutType = LogoutType.BACK_CHANNEL;

  private RegisteredServiceProxyPolicy serviceProxyPolicy;

  private RegisteredServiceUsernameAttributeProvider usernameAttributeProvider;

  private AttributeReleasePolicy attributeReleasePolicy;

  private RegisteredServiceAccessStrategy accessStrategy;

  private URL logotUrl;

  /**
   * 默认对应的logouUrl为空.
   */
  private URL logoutUrl;

  /**
   * 此标识用于判定serviceId的值是否是初始化的默认值.
   */
  private boolean serviceIdIsDefault = true;

  private RegisteredServicePublicKey pulickKey;

  public UniauthRegexRegisteredServiceBuilder() {
    init();
  }

  /**
   * 初始化属性中的对象.
   */
  private void init() {
    this.requiredHandlers = new HashSet<>();
    this.serviceProxyPolicy = new RefuseRegisteredServiceProxyPolicy();
    this.usernameAttributeProvider = new DefaultRegisteredServiceUsernameProvider();
    this.accessStrategy = new DefaultRegisteredServiceAccessStrategy();

    // init attributeReleasePolicy
    ReturnAllowedAttributeReleasePolicy attributeReleasePolicy =
        new ReturnAllowedAttributeReleasePolicy();
    attributeReleasePolicy.setAllowedAttributes(Arrays.asList("tenancyid"));
    attributeReleasePolicy
        .setPrincipalAttributesRepository(new DefaultPrincipalAttributesRepository());
    attributeReleasePolicy.setAuthorizedToReleaseCredentialPassword(false);
    attributeReleasePolicy.setAuthorizedToReleaseProxyGrantingTicket(false);
    this.attributeReleasePolicy = attributeReleasePolicy;
  }

  @Override
  public RegisteredService build() {
    UniauthRegexRegisteredService registeredService = new UniauthRegexRegisteredService();
    registeredService.setAccessStrategy(this.accessStrategy);
    registeredService.setAttributeReleasePolicy(this.attributeReleasePolicy);
    registeredService.setDescription(this.description);
    registeredService.setEvaluationOrder(this.evaluationOrder);
    registeredService.setId(this.id);
    registeredService.setLogo(this.logotUrl);
    registeredService.setLogoutType(this.logoutType);
    registeredService.setLogoutUrl(this.logoutUrl);
    registeredService.setName(this.name);
    registeredService.setProxyPolicy(this.serviceProxyPolicy);
    registeredService.setPublicKey(this.pulickKey);
    registeredService.setRequiredHandlers(this.requiredHandlers);
    registeredService.setServiceId(this.serviceId);
    registeredService.setTheme(this.theme);
    registeredService.setUsernameAttributeProvider(this.usernameAttributeProvider);
    registeredService.setServiceIdIsDefault(serviceIdIsDefault);
    return registeredService;
  }

  @Override
  public RegisteredServiceBuilder setProxyPolicy(RegisteredServiceProxyPolicy serviceProxyPolicy) {
    this.serviceProxyPolicy = serviceProxyPolicy;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setServiceId(String serviceId) {
    this.serviceId = serviceId;
    this.serviceIdIsDefault = false;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setId(long id) {
    this.id = id;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setTheme(String theme) {
    this.theme = theme;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setEvaluationOrder(int evaluationOrder) {
    this.evaluationOrder = evaluationOrder;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setUsernameAttributeProvider(
      RegisteredServiceUsernameAttributeProvider usernameAttributeProvider) {
    this.usernameAttributeProvider = usernameAttributeProvider;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setRequiredHandlers(Set<String> requiredHandlers) {
    this.requiredHandlers = requiredHandlers;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setAccessStrategy(
      RegisteredServiceAccessStrategy accessStrategy) {
    this.accessStrategy = accessStrategy;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setLogoutType(LogoutType logoutType) {
    this.logoutType = logoutType;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setAttributeReleasePolicy(
      AttributeReleasePolicy attributeReleasePolicy) {
    this.attributeReleasePolicy = attributeReleasePolicy;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setLogoUrl(URL logoUrl) {
    this.logotUrl = logoUrl;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setLogoutUrl(URL logoutUrl) {
    this.logoutUrl = logoutUrl;
    return this;
  }

  @Override
  public RegisteredServiceBuilder setPublicKey(RegisteredServicePublicKey pulickKey) {
    this.pulickKey = pulickKey;
    return this;
  }
}
