package org.springframework.security.web.access.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.client.custom.AuthenticationTypeDetector;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompositeSecurityContextRepository implements SecurityContextRepository {

  private AuthenticationTypeDetector authenticationTypeDetector;

  private List<UniauthSecurityContextRepository> securityContextRepositoryList;

  /**
   * 默认的SecurityContextRepository.
   */
  private SecurityContextRepository defaultSecurityContextRepository =
      new HttpSessionSecurityContextRepository();

  public CompositeSecurityContextRepository(AuthenticationTypeDetector authenticationTypeDetector) {
    this(authenticationTypeDetector, null);
  }

  public CompositeSecurityContextRepository(AuthenticationTypeDetector authenticationTypeDetector,
      Collection<UniauthSecurityContextRepository> securityContextRepositoryList) {
    Assert.notNull(authenticationTypeDetector, "AuthenticationTypeDetector must not be null");
    this.authenticationTypeDetector = authenticationTypeDetector;
    if (StringUtils.isEmpty(securityContextRepositoryList)) {
      this.securityContextRepositoryList = Collections.emptyList();
    } else {
      this.securityContextRepositoryList = new ArrayList<>(securityContextRepositoryList);
    }
  }

  @Override
  public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
    return findSecurityContextRepository(requestResponseHolder.getRequest())
        .loadContext(requestResponseHolder);
  }

  @Override
  public void saveContext(SecurityContext context, HttpServletRequest request,
      HttpServletResponse response) {
    findSecurityContextRepository(request).saveContext(context, request, response);
  }

  @Override
  public boolean containsContext(HttpServletRequest request) {
    return findSecurityContextRepository(request).containsContext(request);
  }

  /**
   * 根据HttpServletRequest请求判断当前应该使用的SecurityContextRepository.
   * 
   * @return 不会返回null.
   */
  private SecurityContextRepository findSecurityContextRepository(HttpServletRequest request) {
    AuthenticationType authenticationType = authenticationTypeDetector.detect(request);
    for (UniauthSecurityContextRepository securityContextRepository : securityContextRepositoryList) {
      if (securityContextRepository.support(authenticationType)) {
        return securityContextRepository;
      }
    }
    return defaultSecurityContextRepository;
  }

  public AuthenticationTypeDetector getAuthenticationTypeDetector() {
    return authenticationTypeDetector;
  }

  public void setAuthenticationTypeDetector(AuthenticationTypeDetector authenticationTypeDetector) {
    this.authenticationTypeDetector = authenticationTypeDetector;
  }

  public List<UniauthSecurityContextRepository> getSecurityContextRepositoryList() {
    return Collections.unmodifiableList(securityContextRepositoryList);
  }

  public void setSecurityContextRepositoryList(
      List<UniauthSecurityContextRepository> securityContextRepositoryList) {
    if (securityContextRepositoryList == null) {
      log.warn("securityContextRepositoryList can not set be null, ignored.");
      return;
    }
    this.securityContextRepositoryList = new ArrayList<>(securityContextRepositoryList);
  }

  public SecurityContextRepository getDefaultSecurityContextRepository() {
    return defaultSecurityContextRepository;
  }

  public void setDefaultSecurityContextRepository(
      SecurityContextRepository defaultSecurityContextRepository) {
    Assert.notNull(defaultSecurityContextRepository,
        "DefaultSecurityContextRepository can not be set to null");
    this.defaultSecurityContextRepository = defaultSecurityContextRepository;
  }
}
