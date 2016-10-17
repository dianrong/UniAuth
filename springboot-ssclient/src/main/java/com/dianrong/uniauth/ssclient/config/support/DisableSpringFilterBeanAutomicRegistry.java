package com.dianrong.uniauth.ssclient.config.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.catalina.core.ApplicationFilterRegistration;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.springframework.boot.context.embedded.ServletContextInitializer;

import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import com.dianrong.uniauth.ssclient.config.SpringContextHolder;

public class DisableSpringFilterBeanAutomicRegistry implements ServletContextInitializer{
  // 需要放过的filterClass的name列表
  private static final String[] excludeFilterClassNames = {
  };
  
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    Map<String, Filter> maps = needInvalidateFilterNames();
    for (Entry<String, Filter> entry : maps.entrySet()) {
      // replace
      FilterRegistration  registration =  servletContext.getFilterRegistration(entry.getKey());
      if (registration instanceof ApplicationFilterRegistration) {
        ApplicationFilterRegistration _registration = (ApplicationFilterRegistration)registration;
        FilterDef filterDef =  (FilterDef)ReflectionUtils.getField(_registration, "filterDef", false);
        if (filterDef != null) {
          InvalidateFilterDelegateFilter delegateFilter = new InvalidateFilterDelegateFilter(entry.getValue());
          filterDef.setFilter(delegateFilter);
          filterDef.setFilterClass(delegateFilter.getClass().getName());
        }
      }
    }
  }
  
  /**.
   * 计算需要invalidate的filterName的列表
   * @return
   */
  private Map<String, Filter> needInvalidateFilterNames() {
    Map<String, ServletContextInitializer> needKeepMap = getBeansFromSpring(ServletContextInitializer.class);
    Map<String, Filter> springFilters = getBeansFromSpring(Filter.class);
    Map<String, Filter> maps = new HashMap<String, Filter>(springFilters.size());
    for(Entry<String, Filter> entry : springFilters.entrySet()) {
      Filter filter = entry.getValue();
      String name = entry.getKey();
      // 过滤需要keep的bean
      if (needKeepMap.containsKey(name)) {
        continue;
      }
      boolean needKeep = false;
      for (String excludeName : excludeFilterClassNames) {
        if (filter.getClass().getName().equals(excludeName)) {
          needKeep = true;
          break;
        }
      }
      if (needKeep) {
        continue;
      }
      maps.put(name, filter);
    }
    return Collections.unmodifiableMap(maps);
  }
  
  /**.
   * 从spring中获取对应类型的bean
   * @param type
   * @return
   */
  private <T>  Map<String, T>  getBeansFromSpring (Class<T> type) {
    Map<String, T> map = new LinkedHashMap<String, T>();
    String[] names = SpringContextHolder.getApplicationContext().getBeanNamesForType(type, true, false);
    if (names != null && names.length > 0) {
      for(String name : names){
        T bean = SpringContextHolder.getApplicationContext().getBean(name, type);
        if (bean != null) {
          map.put(name, bean);
        }
      }
    }
    return map;
  }
}
