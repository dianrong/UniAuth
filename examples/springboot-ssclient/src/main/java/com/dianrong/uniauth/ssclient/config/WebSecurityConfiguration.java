package com.dianrong.uniauth.ssclient.config;

import com.dianrong.common.uniauth.client.config.UniauthSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 自定义spring security 配置
 *
 * @author wanglin
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends UniauthSecurityConfig {

  @Autowired
  private MyAuthenticationProvider myAuthenticationProvider;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/js/**", "/favicon.ico", "/static-content","/**/ajax-content.jsp");
  }

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

