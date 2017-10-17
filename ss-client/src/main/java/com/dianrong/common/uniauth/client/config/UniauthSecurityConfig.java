package com.dianrong.common.uniauth.client.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.client.custom.filter.*;

import lombok.extern.slf4j.Slf4j;

/**
 * Uniauth 针对spring boot的集成配置对象 一般做法是直接继承该类作为spring security配置.<br>
 * 如果需要 重写自定义configure（HttpSecurity http）<br>
 * 请不要忘记: 调用该类中的配置：super.configure(HttpSecurity http)<br>
 *
 * @author wanglin
 */
@Slf4j
public class UniauthSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private ConfigureBeanCreator beanCreator;

  @Autowired
  private CasAuthenticationEntryPoint casAuthEntryPoint;

  /**
   * Configure security filter chain for uniauth.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    Assert.notNull(beanCreator, "UniauthSecurityConfig need managed by spring");
    log.info("start uniauth security configure");

    // <sec:intercept-url pattern="/**" access="isAuthenticated()" />
    http.authorizeRequests().anyRequest().authenticated();

    // logout delete cookie and csrf configure
    http.logout().deleteCookies("JSESSIONID").and().csrf().disable();

    // filter configure
    UniauthCasAuthenticationFilter casAuthenticationFilter =
        beanCreator.create(UniauthCasAuthenticationFilter.class);
    UniauthJWTAuthenticationFilter jwtAuthenticationFilter =
        beanCreator.create(UniauthJWTAuthenticationFilter.class);

    AllAuthenticationFilter allAuthenticationFilter = beanCreator
        .create(AllAuthenticationFilter.class, casAuthenticationFilter, jwtAuthenticationFilter);

    DelegateAuthenticationFilter authenticationFilter =
        beanCreator.create(DelegateAuthenticationFilter.class, casAuthenticationFilter,
            jwtAuthenticationFilter, allAuthenticationFilter);

    http.addFilter(beanCreator.create(ConcurrentSessionFilter.class));
    http.addFilterAfter(authenticationFilter, CasAuthenticationFilter.class);
    UniauthBasicAuthAuthenticationFilter basicAuthAuthenticationFilter =
        beanCreator.create(UniauthBasicAuthAuthenticationFilter.class);
    http.addFilterAfter(basicAuthAuthenticationFilter, BasicAuthenticationFilter.class);
    http.addFilterBefore(beanCreator.create(LogoutFilter.class), LogoutFilter.class);
    http.addFilterAfter(beanCreator.create(SSExceptionTranslationFilter.class),
        ExceptionTranslationFilter.class);
    http.addFilterBefore(beanCreator.create(SingleSignOutFilter.class),
        CasAuthenticationFilter.class);

    // entry-point configure
    http.exceptionHandling().authenticationEntryPoint(casAuthEntryPoint);
    log.info("finish uniauth security configure");
  }
}
