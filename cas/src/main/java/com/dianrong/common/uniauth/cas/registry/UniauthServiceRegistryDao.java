package com.dianrong.common.uniauth.cas.registry;

import com.dianrong.common.uniauth.cas.registry.registeredservice.UniauthRegexRegisteredServiceBuilder;
import com.dianrong.common.uniauth.cas.util.ServiceUtils;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.Crc32;
import com.dianrong.common.uniauth.common.util.ZkNodeUtils;
import com.google.common.collect.Maps;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServiceRegistryDao;

/**
 * 实现uniauth自定义的RegisteredService管理逻辑 从zk配置的数据中获取.
 *
 * @author wanglin
 */
@Slf4j
public final class UniauthServiceRegistryDao implements ServiceRegistryDao {

  /**
   * 配置对象 key -> value.
   */
  private Map<String, String> uniauthConfig;

  /**
   * 存储初始化好的RegisteredService.
   */
  private Map<Long, RegisteredService> registeredServiceMap;

  /**
   * 构造一个UniauthServiceRegistryDao, 初始化关键的两个属性registeredServiceMap和registeredServiceBuilder
   *
   * @param uniauthConfig 一个包含所有配置数据map, 不能为空.
   */
  public UniauthServiceRegistryDao(Map<String, String> uniauthConfig) {
    Assert.notNull(uniauthConfig);
    this.uniauthConfig = uniauthConfig;
    // 初始化一次数据
    load();
  }

  @Override
  public RegisteredService save(RegisteredService registeredService) {
    log.warn("current ServiceRegistryDao implement not support save, just ignore!");
    return registeredService;
  }

  @Override
  public boolean delete(RegisteredService registeredService) {
    log.warn("current ServiceRegistryDao implement not support delete, just ignore!");
    return false;
  }

  /**
   * 实现从zk配置中导入所有的域的配置.
   */
  @Override
  public List<RegisteredService> load() {
    Map<Long, RegisteredService> registeredServiceMap = Maps.newConcurrentMap();
    Set<Entry<String, String>> entrySet = this.uniauthConfig.entrySet();
    for (Entry<String, String> entry : entrySet) {
      String nodeKey = entry.getKey();
      if (ZkNodeUtils.isDomainNode(nodeKey)) {
        String domainName = ZkNodeUtils.getDomainName(nodeKey);
        if (domainName == null) {
          continue;
        }
        String logoutUrlStr = this.uniauthConfig
            .get(ZkNodeUtils.getDomainLogoutNodeKey(domainName));
        if (logoutUrlStr == null) {
          continue;
        }
        log.info("domain {} config logout address {}", nodeKey, logoutUrlStr);
        URL logoutUrl;
        try {
          logoutUrl = new URL(logoutUrlStr);
        } catch (MalformedURLException e) {
          log.info("domain {} 's logout url {}  is a invalid Url", nodeKey, logoutUrlStr);
          continue;
        }
        String domainUrl = entry.getValue();
        if (domainUrl == null) {
          continue;
        }
        String serviceId = ServiceUtils.getRegularServiceUrl(domainUrl);
        long id = Crc32.getCrc32(serviceId);
        // add a default registeredService
        RegisteredService registeredService =
            new UniauthRegexRegisteredServiceBuilder().setName(domainName).setServiceId(serviceId)
                .setId(id).setLogoutUrl(logoutUrl).build();
        registeredServiceMap.put(registeredService.getId(), registeredService);
      }
    }
    // add a default registeredService
    RegisteredService defaultRegisteredService = new UniauthRegexRegisteredServiceBuilder().build();
    registeredServiceMap.put(defaultRegisteredService.getId(), defaultRegisteredService);
    this.registeredServiceMap = registeredServiceMap;
    log.debug("all registered service {}", this.registeredServiceMap.values());
    return new ArrayList<>(this.registeredServiceMap.values());
  }

  @Override
  public RegisteredService findServiceById(long id) {
    return this.registeredServiceMap.get(id);
  }
}
