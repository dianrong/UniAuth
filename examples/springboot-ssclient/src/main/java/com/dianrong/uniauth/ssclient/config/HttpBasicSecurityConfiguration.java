package com.dianrong.uniauth.ssclient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 如果需要使用basic auth的功能，可参考配置
 */
@Order(1)
public class HttpBasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private MyAuthenticationProvider myAuthenticationProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authenticationProvider(myAuthenticationProvider)
        .authorizeRequests()
        .antMatchers("/api/**").hasRole("techops")
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().csrf().disable()
        .httpBasic();
  }
}
