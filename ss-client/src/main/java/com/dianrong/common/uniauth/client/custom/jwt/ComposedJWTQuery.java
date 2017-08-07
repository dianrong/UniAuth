package com.dianrong.common.uniauth.client.custom.jwt;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;

/**
 * JWTQuery的适配实现,负责查找Spring环境中所有的JWTQuery实现,<br>
 * 并依次调用获取JWT,直到获取到JWT或者返回null.
 * 
 * @author wanglin
 *
 */

@Slf4j
public class ComposedJWTQuery implements JWTQuery, ApplicationContextAware, InitializingBean {

  /**
   * JWTQuery实现集合.
   */
  private Set<JWTQuery> jwtQuerySets = Sets.newHashSet();

  private ApplicationContext applicationContext;

  @Override
  public String getJWT(HttpServletRequest request) {
    for (JWTQuery jwtQuery : jwtQuerySets) {
      String jwt = jwtQuery.getJWT(request);
      if (jwt != null) {
        return jwt;
      }
    }
    return null;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    String[] names = this.applicationContext.getBeanNamesForType(JWTQuery.class, false, false);
    List<JWTQuery> jwtQuerys = Lists.newArrayList();
    for (String name : names) {
      Object object = this.applicationContext.getBean(name);
      if (object instanceof ComposedJWTQuery) {
        log.debug("Ignore ComposedJWTQuery type JWTQuery :" + object);
        continue;
      }
      jwtQuerys.add((JWTQuery) object);
    }
    Comparator<Object> comparator = OrderComparator.INSTANCE;
    Collections.sort(jwtQuerys, comparator);
    this.jwtQuerySets = Collections.unmodifiableSet(new LinkedHashSet<>(jwtQuerys));
  }
}
