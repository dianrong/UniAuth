package com.dianrong.common.uniauth.client.config;

import com.dianrong.common.uniauth.client.custom.filter.DelegateUniauthAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.SSExceptionTranslationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthAbstractAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthBasicAuthAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthCasAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthJWTAuthenticationFilter;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.util.Assert;

/**
 * Uniauth提供的Spring security模版,可以尝试重写父类中的各个获取配置的方法达到修改配置的目的.
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

  @Resource(name = "uniauthSecurityContextRepository")
  private SecurityContextRepository securityContextRepository;

  /**
   * Configure security filter chain for uniauth.
   */
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    Assert.notNull(beanCreator, "UniauthSecurityConfig need managed by spring");
    log.info("start uniauth security configure");

    // <sec:intercept-url pattern="/**" access="isAuthenticated()" />
    http.authorizeRequests().anyRequest().authenticated();

    // 自定义SecurityContextRepository实现
    http.setSharedObject(SecurityContextRepository.class, getSecurityContextRepository());

    // session management configure
    http.sessionManagement().sessionAuthenticationStrategy(getSessionAuthenticationStrategy())
        .invalidSessionUrl(getInvalidSessionUrl()).enableSessionUrlRewriting(false);

    // logout delete cookie and csrf configure
    http.logout().deleteCookies("JSESSIONID").and().csrf().disable();

    configFilter(new ConfigFilterProcess() {
      @Override
      public void configFilter(Filter filter) {
        http.addFilter(filter);
      }
    }, getConcurrentSessionFilter());

    configFilter(new ConfigFilterProcess() {
      @Override
      public void configFilter(Filter filter) {
        http.addFilterAfter(filter, CasAuthenticationFilter.class);
      }
    }, getUniauthAuthenticationFilter());

    configFilter(new ConfigFilterProcess() {
      @Override
      public void configFilter(Filter filter) {
        http.addFilterBefore(filter, LogoutFilter.class);
      }
    }, getLogoutFilter());

    configFilter(new ConfigFilterProcess() {
      @Override
      public void configFilter(Filter filter) {
        http.addFilterAfter(filter, ExceptionTranslationFilter.class);
      }
    }, getExceptionTranslationFilter());

    configFilter(new ConfigFilterProcess() {
      @Override
      public void configFilter(Filter filter) {
        http.addFilterBefore(filter, CasAuthenticationFilter.class);
      }
    }, getSingleSignOutFilter());

    // entry-point configure
    http.exceptionHandling().authenticationEntryPoint(getAuthEntryPoint());
    log.info("finish uniauth security configure");
  }

  static interface ConfigFilterProcess {

    void configFilter(Filter filter);
  }

  /**
   * 配置Filter, 为空的filter则不处理.
   */
  private void configFilter(ConfigFilterProcess process, Filter filter) {
    if (process != null && filter != null) {
      process.configFilter(filter);
    }
  }

  /**
   * 构造session错误跳转的url. #{uniauthConfig['cas_server']}/login? service=#{uniauthConfig['domains.'+domainDefine.domainCode]}/login/cas
   */
  protected String getInvalidSessionUrl() {
    String invalidSessionUrl = uniauthConfig.get("cas_server") + "/login?service="
        + uniauthConfig.get("domains." + domainDefine.getDomainCode()) + "/login/cas";
    log.info("invalidSessionUrl is " + invalidSessionUrl);
    return invalidSessionUrl;
  }


  /**
   * 获取SessionAuthenticationStrategy.
   */
  protected SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
    return this.sas;
  }

  /**
   * 获取AuthEntryPoint.
   */
  protected AuthenticationEntryPoint getAuthEntryPoint() {
    return this.casAuthEntryPoint;
  }

  /**
   * 获取SecurityContextRepository.
   */
  protected SecurityContextRepository getSecurityContextRepository() {
    return this.securityContextRepository;
  }


  /**
   * 获取CasAuthenticationFilter.
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected UniauthAbstractAuthenticationFilter getCasAuthenticationFilter() {
    return beanCreator.create(UniauthCasAuthenticationFilter.class);
  }

  /**
   * 获取JWTAuthenticationFilter.
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected UniauthAbstractAuthenticationFilter getJWTAuthenticationFilter() {
    return beanCreator.create(UniauthJWTAuthenticationFilter.class);
  }

  /**
   * 获取BasicAuthAuthenticationFilter.
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected UniauthAbstractAuthenticationFilter getBasicAuthAuthenticationFilter() {
    return beanCreator.create(UniauthBasicAuthAuthenticationFilter.class);
  }

  /**
   * 获取UniauthAuthenticationFilter.一般情况下该filter用于代理Uniauth中提供的所有的AuthenticationFilter.
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected Filter getUniauthAuthenticationFilter() {
    UniauthAbstractAuthenticationFilter casAuthenticationFilter =
        getCasAuthenticationFilter();
    UniauthAbstractAuthenticationFilter jwtAuthenticationFilter =
        getJWTAuthenticationFilter();
    UniauthAbstractAuthenticationFilter basicAuthAuthenticationFilter =
        getBasicAuthAuthenticationFilter();
    List<Object> authenticationFilterList = new ArrayList<>(3);
    if (casAuthenticationFilter != null) {
      authenticationFilterList.add(casAuthenticationFilter);
    }
    if (jwtAuthenticationFilter != null) {
      authenticationFilterList.add(jwtAuthenticationFilter);
    }
    if (basicAuthAuthenticationFilter != null) {
      authenticationFilterList.add(basicAuthAuthenticationFilter);
    }
    Object[] args = new Object[authenticationFilterList.size()];
    int index = 0;
    for (Object o : authenticationFilterList) {
      args[index++] = o;
    }
    return beanCreator
        .create(DelegateUniauthAuthenticationFilter.class, args);
  }

  /**
   * 获取ConcurrentSessionFilter.
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected Filter getConcurrentSessionFilter() {
    return beanCreator.create(ConcurrentSessionFilter.class);
  }

  /**
   * 获取LogoutFilter.
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected Filter getLogoutFilter() {
    return beanCreator.create(LogoutFilter.class);
  }

  /**
   * 获取SingleSignOutFilter.支持SSO,该filter必须要有.
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected Filter getSingleSignOutFilter() {
    return beanCreator.create(SingleSignOutFilter.class);
  }

  /**
   * 获取ExceptionTranslationFilter;
   *
   * @return 返回结果可为空, 为空则不配置该filter.
   */
  protected Filter getExceptionTranslationFilter() {
    return beanCreator.create(SSExceptionTranslationFilter.class);
  }
}
