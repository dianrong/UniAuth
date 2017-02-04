package com.dianrong.uniauth.ssclient.config;

import org.springframework.security.config.annotation.web.builders.WebSecurity;

import com.dianrong.common.uniauth.client.config.UniauthSecurityConfig;

/**
 * 自定义spring security 配置 
 * @author wanglin
 */
public class WebSecurityConfiguration extends UniauthSecurityConfig {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/favicon.ico");
    }
}