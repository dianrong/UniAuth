package com.dianrong.common.uniauth.cas.registry.registeredservice;

import java.net.URL;
import java.util.Set;
import org.jasig.cas.services.AttributeReleasePolicy;
import org.jasig.cas.services.LogoutType;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.RegisteredServiceAccessStrategy;
import org.jasig.cas.services.RegisteredServiceProxyPolicy;
import org.jasig.cas.services.RegisteredServicePublicKey;
import org.jasig.cas.services.RegisteredServiceUsernameAttributeProvider;

/**
 * 用于生成各种各样的RegisteredService.
 *
 * @author wanglin
 */
public interface RegisteredServiceBuilder {

  /**
   * 生成一个新的RegisteredService.
   * <p>
   * 由于在cas的原生实现DefaultServicesManagerImpl中,会将生成的RegisteredService放入到TreeSet排序.所以,
   * 生成的RegisteredService需要再实现Comparable接口
   * </p>
   *
   * @return 一个新的RegisteredService
   */
  RegisteredService build();

  /**
   * 设置生成的RegisteredService的RegisteredServiceProxyPolicy.
   */
  RegisteredServiceBuilder setProxyPolicy(RegisteredServiceProxyPolicy serviceProxyPolicy);

  /**
   * 设置serviceId, 用于唯一标识一个RegisteredService.
   */
  RegisteredServiceBuilder setServiceId(String serviceId);

  RegisteredServiceBuilder setId(long id);

  RegisteredServiceBuilder setName(String name);

  RegisteredServiceBuilder setTheme(String theme);

  RegisteredServiceBuilder setDescription(String description);

  RegisteredServiceBuilder setEvaluationOrder(int evaluationOrder);

  RegisteredServiceBuilder setUsernameAttributeProvider(
      RegisteredServiceUsernameAttributeProvider usernameAttributeProvider);

  RegisteredServiceBuilder setRequiredHandlers(Set<String> requiredHandlers);

  RegisteredServiceBuilder setAccessStrategy(RegisteredServiceAccessStrategy accessStrategy);

  RegisteredServiceBuilder setLogoutType(LogoutType logoutType);

  RegisteredServiceBuilder setAttributeReleasePolicy(AttributeReleasePolicy attributeReleasePolicy);

  RegisteredServiceBuilder setLogoUrl(URL logoUrl);

  RegisteredServiceBuilder setLogoutUrl(URL logoutUrl);

  RegisteredServiceBuilder setPublicKey(RegisteredServicePublicKey pulickKey);
}
