package com.dianrong.uniauth.ssclient;

import com.dianrong.common.uniauth.common.client.DomainDefine;

import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.customer.basicauth.mode.Mode;
import com.dianrong.uniauth.ssclient.config.MyAuthenticationProvider;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private UniClientFacade uniClientFacade;

  /**
   * 配置集成系统的DomainDefine.
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
   * 配置跨域的filter. 默认CorsFilter只允许POST,GET,HEAD,OPTION.<br>
   * 如果需要支持其他方法,请自行设置CorsFilter参数.<br>
   * 注意：此处有坑，如果配置了该Filter,会导致类似PUT,DELETE等请求方法403.
   */
  @Bean
  public FilterRegistrationBean indexFilterRegistration() {
    CorsFilter corsFilter = new CorsFilter();
    FilterRegistrationBean registration = new FilterRegistrationBean(corsFilter);
    registration.addUrlPatterns("/*");
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registration;
  }

  /**
   * Basic Auth 使用才配置的bean.
   */
  @Bean
  public MyAuthenticationProvider myAuthenticationProvider() {
    return (MyAuthenticationProvider) new MyAuthenticationProvider(uniClientFacade)
        .setMode(Mode.PERMISSION)
    /* .setDomainDefine(domainCode) */;
  }
}
