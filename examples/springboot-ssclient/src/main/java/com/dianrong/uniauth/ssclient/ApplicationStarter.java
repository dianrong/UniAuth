package com.dianrong.uniauth.ssclient;

import com.dianrong.common.uniauth.common.client.DomainDefine;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = {ApplicationStarter.class})
@PropertySource(value = "classpath:/config/application.yml")
@ImportResource({"classpath:spring-context.xml"})
public class ApplicationStarter {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationStarter.class, args);
  }

  @Value("${domainCode}")
  private String domainCode;

  /**
   * 配置DomainDefine.
   */
  @Bean
  public DomainDefine domainDefine() {
    DomainDefine domainDefine = new DomainDefine();
    domainDefine.setDomainCode(domainCode);
    domainDefine.setUserInfoClass("com.dianrong.uniauth.ssclient.bean.SSClientUserExtInfo");
    domainDefine.setRejectPublicInvocations(false);
    domainDefine.setCustomizedLoginRedirecUrl("/content");
    return domainDefine;
  }

  /**
   * 配置跨域的Filter.
   */
  @Bean
  public FilterRegistrationBean indexFilterRegistration() {
    CorsFilter corsFilter = new CorsFilter();
    FilterRegistrationBean registration = new FilterRegistrationBean(corsFilter);
    registration.addUrlPatterns("/*");
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registration;
  }
}
