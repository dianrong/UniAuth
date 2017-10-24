package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.client.custom.auth.AuthenticationTagHolder;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

/**
 * CasAuthenticationFilter, Copy from org.springframework.security.cas.web.CasAuthenticationFilter
 *
 * @author wanglin
 */
public class UniauthCasAuthenticationFilter extends UniauthAbstractAuthenticationFilter {

  public static final String CAS_STATEFUL_IDENTIFIER = "_cas_stateful_";

  public static final String CAS_STATELESS_IDENTIFIER = "_cas_stateless_";

  private RequestMatcher proxyReceptorMatcher;

  private ProxyGrantingTicketStorage proxyGrantingTicketStorage;

  private String artifactParameter = ServiceProperties.DEFAULT_CAS_ARTIFACT_PARAMETER;

  private boolean authenticateAllArtifacts;

  private AuthenticationFailureHandler proxyFailureHandler = new SimpleUrlAuthenticationFailureHandler();

  public UniauthCasAuthenticationFilter() {
    super("/login/cas");
    setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
  }

  @Override
  public AuthenticationType authenticationType() {
    return AuthenticationType.CAS;
  }

  @Override
  protected final void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, Authentication authResult)
      throws IOException, ServletException {
    boolean continueFilterChain = proxyTicketRequest(
        serviceTicketRequest(request, response), request);
    if (!continueFilterChain) {
      super.successfulAuthentication(request, response, chain, authResult);
      return;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
          + authResult);
    }

    SecurityContextHolder.getContext().setAuthentication(authResult);

    // Fire event
    if (this.eventPublisher != null) {
      eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
          authResult, this.getClass()));
    }


    chain.doFilter(request, response);
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest request,
      final HttpServletResponse response) throws AuthenticationException,
      IOException {
    // if the request is a proxy request process it and return null to indicate the
    // request has been processed
    if (proxyReceptorRequest(request)) {
      logger.debug("Responding to proxy receptor request");
      CommonUtils.readAndRespondToProxyReceptorRequest(request, response,
          this.proxyGrantingTicketStorage);
      return null;
    }

    final boolean serviceTicketRequest = serviceTicketRequest(request, response);
    final String username = serviceTicketRequest ? CAS_STATEFUL_IDENTIFIER
        : CAS_STATELESS_IDENTIFIER;
    String password = obtainArtifact(request);

    if (password == null) {
      logger.debug("Failed to obtain an artifact (cas ticket)");
      password = "";
    }

    final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        username, password);

    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

    return this.getAuthenticationManager().authenticate(authRequest);
  }

  protected String obtainArtifact(HttpServletRequest request) {
    return request.getParameter(artifactParameter);
  }

  @Override
  protected boolean requiresAuthentication(final HttpServletRequest request,
      final HttpServletResponse response) {
    final boolean serviceTicketRequest = serviceTicketRequest(request, response);
    final boolean result = serviceTicketRequest || proxyReceptorRequest(request)
        || (proxyTicketRequest(serviceTicketRequest, request));
    if (logger.isDebugEnabled()) {
      logger.debug("requiresAuthentication = " + result);
    }
    if(result){
      return true;
    }

    // 上下文中获取,查看是否已经认证成功
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      AuthenticationTagHolder.authenticated(authenticationType());
    }
    return result;
  }

  public final void setProxyAuthenticationFailureHandler(
      AuthenticationFailureHandler proxyFailureHandler) {
    Assert.notNull(proxyFailureHandler, "proxyFailureHandler cannot be null");
    this.proxyFailureHandler = proxyFailureHandler;
  }

  @Override
  public final void setAuthenticationFailureHandler(
      AuthenticationFailureHandler failureHandler) {
    super.setAuthenticationFailureHandler(new CasAuthenticationFailureHandler(
        failureHandler));
  }

  public final void setProxyReceptorUrl(final String proxyReceptorUrl) {
    this.proxyReceptorMatcher = new AntPathRequestMatcher("/**" + proxyReceptorUrl);
  }

  public final void setProxyGrantingTicketStorage(
      final ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
    this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
  }

  public final void setServiceProperties(final ServiceProperties serviceProperties) {
    this.artifactParameter = serviceProperties.getArtifactParameter();
    this.authenticateAllArtifacts = serviceProperties.isAuthenticateAllArtifacts();
  }

  private boolean serviceTicketRequest(final HttpServletRequest request,
      final HttpServletResponse response) {
    boolean result = super.requiresAuthentication(request, response);
    if (logger.isDebugEnabled()) {
      logger.debug("serviceTicketRequest = " + result);
    }
    return result;
  }

  private boolean proxyTicketRequest(final boolean serviceTicketRequest,
      final HttpServletRequest request) {
    if (serviceTicketRequest) {
      return false;
    }
    final boolean result = authenticateAllArtifacts
        && obtainArtifact(request) != null && !authenticated();
    if (logger.isDebugEnabled()) {
      logger.debug("proxyTicketRequest = " + result);
    }
    return result;
  }

  private boolean authenticated() {
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    return authentication != null && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
  }

  private boolean proxyReceptorRequest(final HttpServletRequest request) {
    final boolean result = proxyReceptorConfigured()
        && proxyReceptorMatcher.matches(request);
    if (logger.isDebugEnabled()) {
      logger.debug("proxyReceptorRequest = " + result);
    }
    return result;
  }

  private boolean proxyReceptorConfigured() {
    final boolean result = this.proxyGrantingTicketStorage != null
        && proxyReceptorMatcher != null;
    if (logger.isDebugEnabled()) {
      logger.debug("proxyReceptorConfigured = " + result);
    }
    return result;
  }

  private class CasAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final AuthenticationFailureHandler serviceTicketFailureHandler;

    public CasAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
      Assert.notNull(failureHandler, "failureHandler");
      this.serviceTicketFailureHandler = failureHandler;
    }

    public void onAuthenticationFailure(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
      if (serviceTicketRequest(request, response)) {
        serviceTicketFailureHandler.onAuthenticationFailure(request, response,
            exception);
      } else {
        proxyFailureHandler.onAuthenticationFailure(request, response, exception);
      }
    }
  }
}
