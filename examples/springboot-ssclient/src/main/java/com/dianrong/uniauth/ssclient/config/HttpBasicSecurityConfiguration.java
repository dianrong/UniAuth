package com.dianrong.uniauth.ssclient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Created by denghb on 6/23/17.
 */
@Configuration
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
