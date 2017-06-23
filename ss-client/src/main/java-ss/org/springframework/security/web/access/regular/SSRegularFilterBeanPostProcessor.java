package org.springframework.security.web.access.regular;

import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.client.DomainDefine.CasPermissionControlType;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import java.util.List;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.SwitchControl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

/**
 * 用于在服务启动的时候将SSRegularPermissionFilter注入到spring security的处理链路中
 *
 * @author wanglin
 */
public class SSRegularFilterBeanPostProcessor implements BeanPostProcessor, Ordered, SwitchControl {

  /**
   * .
   * spring security处理filter链的bean name
   */
  private static final String SPRING_SECURITY_FILETER_BEAN_NAME = "org.springframework.security.filterChainProxy";

  @Autowired
  private DomainDefine domainDefine;

  /**
   * .
   * 最低优先级，最后执行
   */
  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }

  /**
   * .
   * do not process
   */
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  /**
   * .
   * update bean field
   * 注入uniauth的正则处理权限的filter
   */
  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    if (!isOn()) {
      return bean;
    }
    if (SPRING_SECURITY_FILETER_BEAN_NAME.equals(beanName) && bean instanceof FilterChainProxy) {
      FilterChainProxy fillterProxyChainBean = (FilterChainProxy) bean;
      List<SecurityFilterChain> filters = fillterProxyChainBean.getFilterChains();
      if (filters == null) {
        throw new UniauthCommonException("bean [" + SPRING_SECURITY_FILETER_BEAN_NAME
            + "] is invalid, the  field filterChains is null");
      }
      DefaultSecurityFilterChain destFilterChain = null;
      // 遍历查找 带有AnyRequestMatcher的SecurityFilterChain,将SSRegularPermissionFilter加入进去
      for (SecurityFilterChain securityFilter : filters) {
        if (securityFilter instanceof DefaultSecurityFilterChain) {
          DefaultSecurityFilterChain defaultsecurityFilter = (DefaultSecurityFilterChain) securityFilter;
          if (defaultsecurityFilter.getRequestMatcher() instanceof AnyRequestMatcher) {
            destFilterChain = defaultsecurityFilter;
            break;
          }
        }
      }
      // add one
      if (destFilterChain == null) {
        @SuppressWarnings("unchecked")
        List<SecurityFilterChain> filterChains = (List<SecurityFilterChain>) ReflectionUtils
            .getField(fillterProxyChainBean, "filterChains", false);
        filterChains.add(new DefaultSecurityFilterChain(AnyRequestMatcher.INSTANCE,
            new SSRegularPermissionFilter()));
      } else {
        int index = 0;
        List<Filter> filterList = destFilterChain.getFilters();
        for (Filter filter : filterList) {
          if (filter instanceof FilterSecurityInterceptor) {
            break;
          }
          index++;
        }
        // 放到org.springframework.security.web.access.intercept.FilterSecurityInterceptor 这个filter前面
        filterList.add(index, new SSRegularPermissionFilter());
      }
    }
    return bean;
  }

  @Override
  public boolean isOn() {
    return domainDefine.controlTypeSupport(CasPermissionControlType.REGULAR_PATTERN);
  }
}
