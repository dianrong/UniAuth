package org.springframework.security.web.access.sessionmanage;

import com.dianrong.common.uniauth.client.custom.redirect.UniauthRedirectFormat;
import com.dianrong.common.uniauth.client.custom.redirect.SimpleRedirectFormat;
import com.dianrong.common.uniauth.client.custom.UniauthAjaxResponseProcessor;
import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;

/**
 * 用于手态配置SessionManagementFilter.
 *
 * @author wanglin
 */
@Slf4j
public class SessionManageBeanPostProcessor implements BeanPostProcessor {

  private ZooKeeperConfig zooKeeperConfig;

  /**
   * 通过注解配置CustomizedRedirectFormat.
   */
  @Autowired(required = false)
  private UniauthRedirectFormat customizedRedirectFormat = new SimpleRedirectFormat();

  public ZooKeeperConfig getZooKeeperConfig() {
    return zooKeeperConfig;
  }

  public void setZooKeeperConfig(ZooKeeperConfig zooKeeperConfig) {
    this.zooKeeperConfig = zooKeeperConfig;
  }

  public UniauthRedirectFormat getCustomizedRedirectFormat() {
    return customizedRedirectFormat;
  }

  /**
   * 设置自定义的跳转格式处理.
   */
  public void setCustomizedRedirectFormat(UniauthRedirectFormat customizedRedirectFormat) {
    if (customizedRedirectFormat == null) {
      log.warn("set customizedRedirectFormat is null");
    } else {
      log.info("set customizedRedirectFormat {}", customizedRedirectFormat);
    }
    this.customizedRedirectFormat = customizedRedirectFormat;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof SessionManagementFilter) {
      log.info("config SessionManagementFilter manually");
      SessionManagementFilter sessionManagementFilter = (SessionManagementFilter) bean;
      InvalidSessionStrategy invalidSessionStrategy = (InvalidSessionStrategy) ReflectionUtils
          .getField(sessionManagementFilter, "invalidSessionStrategy");
      List<InvalidSessionStrategy> invalidSessionStrategies =
          new ArrayList<InvalidSessionStrategy>();
      if (invalidSessionStrategy != null) {
        log.info("SessionManagementFilter has original InvalidSessionStrategy : "
            + invalidSessionStrategy);
      }
      invalidSessionStrategies.add(new CompatibleAjaxInvalidSessionStrategy(invalidSessionStrategy,
          new UniauthAjaxResponseProcessor(this.zooKeeperConfig),
          SimpleRedirectInvalidSessionStrategy.class)
              .setCustomizedRedirectFormat(this.customizedRedirectFormat));
      invalidSessionStrategies.add(new RequestCacheInvalidSessionStrategy());
      CompositeInvalidSessionStrategy compositeInvalidSessionStrategy =
          new CompositeInvalidSessionStrategy();
      compositeInvalidSessionStrategy.setInvalidSessionStrategies(invalidSessionStrategies);
      sessionManagementFilter.setInvalidSessionStrategy(compositeInvalidSessionStrategy);
    }
    return bean;
  }
}
