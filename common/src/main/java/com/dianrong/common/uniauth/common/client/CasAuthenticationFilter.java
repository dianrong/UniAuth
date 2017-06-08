package com.dianrong.common.uniauth.common.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.authentication.AuthenticationRedirectStrategy;
import org.jasig.cas.client.authentication.ContainsPatternUrlPatternMatcherStrategy;
import org.jasig.cas.client.authentication.DefaultAuthenticationRedirectStrategy;
import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.ExactUrlPatternMatcherStrategy;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.authentication.RegexUrlPatternMatcherStrategy;
import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Assertion;

@Slf4j
public class CasAuthenticationFilter extends AbstractCasFilter {

  /**
   * . header 中的referer头的name
   */
  private static final String HEADER_REFERER = "Referer";

  /**
   * The URL to the CAS Server login.
   */
  private String casServerLoginUrl;

  /**
   * Whether to send the renew request or not.
   */
  private boolean renew = false;

  /**
   * Whether to send the gateway request or not.
   */
  private boolean gateway = false;

  private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

  private AuthenticationRedirectStrategy authenticationRedirectStrategy =
      new DefaultAuthenticationRedirectStrategy();

  private UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategyClass = null;

  private static final Map<String, Class<? extends UrlPatternMatcherStrategy>> 
      PATTERN_MATCHER_TYPES = new HashMap<String, Class<? extends UrlPatternMatcherStrategy>>();

  static {
    PATTERN_MATCHER_TYPES.put("CONTAINS", ContainsPatternUrlPatternMatcherStrategy.class);
    PATTERN_MATCHER_TYPES.put("REGEX", RegexUrlPatternMatcherStrategy.class);
    PATTERN_MATCHER_TYPES.put("EXACT", ExactUrlPatternMatcherStrategy.class);
  }

  public CasAuthenticationFilter() {
    this(Protocol.CAS2);
  }

  protected CasAuthenticationFilter(final Protocol protocol) {
    super(protocol);
  }

  protected void initInternal(final FilterConfig filterConfig) throws ServletException {
    if (!isIgnoreInitConfiguration()) {
      super.initInternal(filterConfig);
      setCasServerLoginUrl(getString(ConfigurationKeys.CAS_SERVER_LOGIN_URL));
      setRenew(getBoolean(ConfigurationKeys.RENEW));
      setGateway(getBoolean(ConfigurationKeys.GATEWAY));

      final String ignorePattern = getString(ConfigurationKeys.IGNORE_PATTERN);
      final String ignoreUrlPatternType = getString(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE);

      if (ignorePattern != null) {
        final Class<? extends UrlPatternMatcherStrategy> ignoreUrlMatcherClass =
            PATTERN_MATCHER_TYPES.get(ignoreUrlPatternType);
        if (ignoreUrlMatcherClass != null) {
          this.ignoreUrlPatternMatcherStrategyClass =
              ReflectUtils.newInstance(ignoreUrlMatcherClass.getName());
        } else {
          try {
            log.trace("Assuming {} is a qualified class name...", ignoreUrlPatternType);
            this.ignoreUrlPatternMatcherStrategyClass =
                ReflectUtils.newInstance(ignoreUrlPatternType);
          } catch (final IllegalArgumentException e) {
            log.error("Could not instantiate class [{}]", ignoreUrlPatternType, e);
          }
        }
        if (this.ignoreUrlPatternMatcherStrategyClass != null) {
          this.ignoreUrlPatternMatcherStrategyClass.setPattern(ignorePattern);
        }
      }

      final Class<? extends GatewayResolver> gatewayStorageClass =
          getClass(ConfigurationKeys.GATEWAY_STORAGE_CLASS);

      if (gatewayStorageClass != null) {
        setGatewayStorage(ReflectUtils.newInstance(gatewayStorageClass));
      }

      final Class<? extends AuthenticationRedirectStrategy> authenticationRedirectStrategyClass =
          getClass(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS);

      if (authenticationRedirectStrategyClass != null) {
        this.authenticationRedirectStrategy =
            ReflectUtils.newInstance(authenticationRedirectStrategyClass);
      }
    }
  }

  public void init() {
    super.init();
    CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
  }

  /**
   * 过滤请求.
   */
  public final void doFilter(final ServletRequest servletRequest,
      final ServletResponse servletResponse, final FilterChain filterChain)
      throws IOException, ServletException {

    final HttpServletRequest request = (HttpServletRequest) servletRequest;
    final HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (isRequestUrlExcluded(request)) {
      log.debug("Request is ignored.");
      filterChain.doFilter(request, response);
      return;
    }

    final HttpSession session = request.getSession(false);
    final Assertion assertion =
        session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;

    if (assertion != null) {
      filterChain.doFilter(request, response);
      return;
    }

    final String serviceUrl = constructServiceUrl(request, response);
    final String ticket = retrieveTicketFromRequest(request);
    final boolean wasGatewayed =
        this.gateway && this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

    if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
      filterChain.doFilter(request, response);
      return;
    }

    final String modifiedServiceUrl;

    log.debug("no ticket and no assertion found");
    if (this.gateway) {
      log.debug("setting gateway attribute in session");
      modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
    } else {
      modifiedServiceUrl = serviceUrl;
    }

    log.debug("Constructed service url: {}", modifiedServiceUrl);

    final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl,
        getProtocol().getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);

    log.debug("redirecting to \"{}\"", urlToRedirectTo);
    this.authenticationRedirectStrategy.redirect(request, response, urlToRedirectTo);
  }

  public final void setRenew(final boolean renew) {
    this.renew = renew;
  }

  public final void setGateway(final boolean gateway) {
    this.gateway = gateway;
  }

  public final void setCasServerLoginUrl(final String casServerLoginUrl) {
    this.casServerLoginUrl = casServerLoginUrl;
  }

  public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
    this.gatewayStorage = gatewayStorage;
  }

  public void setIgnoreUrlPatternMatcherStrategyClass(
      UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategyClass) {
    this.ignoreUrlPatternMatcherStrategyClass = ignoreUrlPatternMatcherStrategyClass;
  }

  private boolean isRequestUrlExcluded(final HttpServletRequest request) {
    if (this.ignoreUrlPatternMatcherStrategyClass == null) {
      return false;
    }

    // 返回st的不能处理
    final String ticket = retrieveTicketFromRequest(request);
    if (ticket != null) {
      return false;
    }

    final StringBuffer urlBuffer = request.getRequestURL();
    if (request.getQueryString() != null) {
      urlBuffer.append("?").append(request.getQueryString());
    }
    final String requestUri = urlBuffer.toString();
    if (this.ignoreUrlPatternMatcherStrategyClass.matches(requestUri)) {
      return true;
    }
    String refererUrl = request.getHeader(HEADER_REFERER);
    if (refererUrl != null) {
      return this.ignoreUrlPatternMatcherStrategyClass.matches(refererUrl);
    }
    return false;
  }
}
