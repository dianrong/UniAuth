package com.dianrong.uniauth.ssclient.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 如果需要使用Basic Auth的功能，可参考配置.
 */
@Order(0)
@Configuration
public class APISecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.userDetailsService(new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetails() {
          private static final long serialVersionUID = -4171051718858609882L;

          @Override
          public Collection<? extends GrantedAuthority> getAuthorities() {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
          }

          @Override
          public String getPassword() {
            return "123456";
          }

          @Override
          public String getUsername() {
            return "ADMIN";
          }

          @Override
          public boolean isAccountNonExpired() {
            return true;
          }

          @Override
          public boolean isAccountNonLocked() {
            return true;
          }

          @Override
          public boolean isCredentialsNonExpired() {
            return true;
          }

          @Override
          public boolean isEnabled() {
            return true;
          }
        };
      }
    }).antMatcher("/api/v1/**").authorizeRequests().anyRequest().hasRole("ADMIN").and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf()
        .disable().httpBasic();
  }
}
