package com.dianrong.common.uniauth.client.config;

import com.dianrong.common.uniauth.client.custom.SSExceptionTranslationFilter;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.util.Assert;

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

  @Resource(name = "sas")
  private SessionAuthenticationStrategy sas;

  @Resource(name = "uniauthConfig")
  private Map<String, String> uniauthConfig;

  @Autowired
  private DomainDefine domainDefine;

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

    // session management configure
    http.sessionManagement().sessionAuthenticationStrategy(sas)
        .invalidSessionUrl(getInvalidSessionUrl());

    // logout delete cookie and csrf configure
    http.logout().deleteCookies("JSESSIONID").and().csrf().disable();

    // filter configure
    http.addFilter(beanCreator.create(ConcurrentSessionFilter.class));
    http.addFilter(beanCreator.create(CasAuthenticationFilter.class));
    http.addFilterBefore(beanCreator.create(LogoutFilter.class), LogoutFilter.class);
    http.addFilterAfter(beanCreator.create(SSExceptionTranslationFilter.class),
        ExceptionTranslationFilter.class);
    http.addFilterBefore(beanCreator.create(SingleSignOutFilter.class),
        CasAuthenticationFilter.class);

    // entry-point configure
    http.exceptionHandling().authenticationEntryPoint(casAuthEntryPoint);
    log.info("finish uniauth security configure");
  }

  /**
   * #{uniauthConfig['cas_server']}/login?
   * service=#{uniauthConfig['domains.'+domainDefine.domainCode]}/login/cas
   */
  private String getInvalidSessionUrl() {
    String invalidSessionUrl = uniauthConfig.get("cas_server") + "/login?service="
        + uniauthConfig.get("domains." + domainDefine.getDomainCode()) + "/login/cas";
    log.info("invalidSessionUrl is " + invalidSessionUrl);
    return invalidSessionUrl;
  }
}
