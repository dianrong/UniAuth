package org.springframework.security.web.access.repo;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.CollectionUtils;

@Slf4j
public class CompositeSecurityContextRepository implements SecurityContextRepository {

  private List<UniauthSecurityContextRepository> securityContextRepositoryList;

  /**
   * 默认的SecurityContextRepository.
   */
  private UniauthSecurityContextRepository defaultSecurityContextRepository =
      new CasSecurityContextRepository();

  public CompositeSecurityContextRepository() {
    this(null);
  }

  public CompositeSecurityContextRepository(
      Collection<UniauthSecurityContextRepository> securityContextRepositoryList) {
    if (CollectionUtils.isEmpty(securityContextRepositoryList)) {
      this.securityContextRepositoryList = Collections.emptyList();
    } else {
      this.securityContextRepositoryList = new ArrayList<>(securityContextRepositoryList);
    }
  }

  @Override
  public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
    if (this.securityContextRepositoryList.isEmpty()) {
      return defaultSecurityContextRepository.loadContext(requestResponseHolder);
    }
    if (this.securityContextRepositoryList.size() == 1) {
      return this.securityContextRepositoryList.get(0).loadContext(requestResponseHolder);
    }
    AuthenticationType defaultAuthenticationType = defaultSecurityContextRepository
        .supportedAuthenticationType();
    AuthTypeAndSecurityContext[] allSecurityContext = new AuthTypeAndSecurityContext[this.securityContextRepositoryList
        .size()];
    AuthTypeAndSecurityContext defaultSecurityContext = null;
    int index = 0;
    for (UniauthSecurityContextRepository contextRepository : this.securityContextRepositoryList) {
      SecurityContext securityContext = contextRepository.loadContext(requestResponseHolder);
      AuthenticationType authenticationType = contextRepository.supportedAuthenticationType();
      // 对DynamicSecurityContext做特殊处理.
      if (securityContext instanceof DynamicSecurityContext) {
        securityContext = ((DynamicSecurityContext) securityContext)
            .getDestSecurityContext(authenticationType);
      }
      AuthTypeAndSecurityContext typeAndSecurityContext = new AuthTypeAndSecurityContext(
          securityContext, authenticationType);

      if (contextRepository.supportedAuthenticationType().isSupported(defaultAuthenticationType)) {
        defaultSecurityContext = typeAndSecurityContext;
      }
      allSecurityContext[index++] = typeAndSecurityContext;
    }
    defaultSecurityContext =
        defaultSecurityContext != null ? defaultSecurityContext : allSecurityContext[0];
    return new DynamicSecurityContext(defaultSecurityContext, allSecurityContext);
  }

  @Override
  public void saveContext(SecurityContext context, HttpServletRequest request,
      HttpServletResponse response) {
    if (context instanceof DynamicSecurityContext) {
      Map<AuthenticationType, SecurityContext> securityContextMap = ((DynamicSecurityContext) context)
          .getAuthTypeAndSecurityContextMap();
      // 遍历当前的securityContextRepositoryList,匹配对应的UniauthSecurityContextRepository.
      for (UniauthSecurityContextRepository contextRepository : this.securityContextRepositoryList) {
        AuthenticationType authenticationType = contextRepository.supportedAuthenticationType();
        SecurityContext securityContext = securityContextMap.get(authenticationType);
        if (securityContext != null) {
          contextRepository.saveContext(securityContext, request, response);
        }
      }
      return;
    }
    Authentication authentication = context.getAuthentication();
    if (authentication != null) {
      // 根据返回的不同的Authentication来决定使用哪个SecurityContextRepository.
      for (UniauthSecurityContextRepository contextRepository : this.securityContextRepositoryList) {
        if (contextRepository.supportAuthenticationClz()
            .isAssignableFrom(authentication.getClass())) {
          contextRepository.saveContext(context, request, response);
          return;
        }
      }
    }
    this.defaultSecurityContextRepository.saveContext(context, request, response);
  }

  @Override
  public boolean containsContext(HttpServletRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      // 根据返回的不同的Authentication来决定使用哪个SecurityContextRepository.
      for (UniauthSecurityContextRepository contextRepository : this.securityContextRepositoryList) {
        if (contextRepository.supportAuthenticationClz()
            .isAssignableFrom(authentication.getClass())) {
          return contextRepository.containsContext(request);
        }
      }
    }
    return this.defaultSecurityContextRepository.containsContext(request);
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
      UniauthSecurityContextRepository defaultSecurityContextRepository) {
    Assert.notNull(defaultSecurityContextRepository,
        "DefaultSecurityContextRepository can not be set to null");
    this.defaultSecurityContextRepository = defaultSecurityContextRepository;
  }
}
