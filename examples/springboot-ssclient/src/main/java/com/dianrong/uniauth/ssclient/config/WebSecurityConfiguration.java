package com.dianrong.uniauth.ssclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import com.dianrong.common.uniauth.client.config.UniauthSecurityConfig;

/**
 * 自定义spring security 配置
 * 
 * @author wanglin
 */
@Configuration
public class WebSecurityConfiguration extends UniauthSecurityConfig {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/favicon.ico", "/static-content","/**/ajax-content.jsp");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }
}
