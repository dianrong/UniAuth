package com.dianrong.common.uniauth.cas.filter;

import com.dianrong.common.uniauth.cas.helper.CasCrossFilterCacheHelper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.filters.Constants;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/**
 * 用于处理CAS中跨域请求的问题.
 */
public class CasCorsFilter implements Filter {

  private static final Log log = LogFactory.getLog(CasCorsFilter.class);
  private static final StringManager sm = StringManager.getManager(Constants.Package);


  /**
   * A {@link Collection} of origins consisting of zero or more origins that are allowed access to
   * the resource.
   */
  private final Collection<String> allowedOrigins;

  /**
   * Determines if any origin is allowed to make request.
   */
  private boolean anyOriginAllowed;

  /**
   * A {@link Collection} of methods consisting of zero or more methods that are supported by the
   * resource.
   */
  private final Collection<String> allowedHttpMethods;

  /**
   * A {@link Collection} of headers consisting of zero or more header field names that are
   * supported by the resource.
   */
  private final Collection<String> allowedHttpHeaders;

  /**
   * A {@link Collection} of exposed headers consisting of zero or more header field names of
   * headers other than the simple response headers that the resource might use and can be exposed.
   */
  private final Collection<String> exposedHeaders;

  /**
   * A supports credentials flag that indicates whether the resource supports user credentials in
   * the request. It is true when the resource does and false otherwise.
   */
  private boolean supportsCredentials;

  /**
   * Indicates (in seconds) how long the results of a pre-flight request can be cached in a
   * pre-flight result cache.
   */
  private long preflightMaxAge;

  /**
   * Determines if the request should be decorated or not.
   */
  private boolean decorateRequest;

  /**
   * 构造函数.
   */
  public CasCorsFilter() {
    this.allowedOrigins = new HashSet<>();
    this.allowedHttpMethods = new HashSet<>();
    this.allowedHttpHeaders = new HashSet<>();
    this.exposedHeaders = new HashSet<>();
  }


  @Override
  public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
      final FilterChain filterChain) throws IOException, ServletException {
    if (!(servletRequest instanceof HttpServletRequest)
        || !(servletResponse instanceof HttpServletResponse)) {
      throw new ServletException(sm.getString("casCORSFilter.onlyHttp"));
    }

    // Safe to downcast at this point.
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    // Determines the CORS request type.
    CasCorsFilter.CorsRequestType requestType = checkRequestType(request);

    // Adds CORS specific attributes to request.
    if (decorateRequest) {
      CasCorsFilter.decorateCorsProperties(request, requestType);
    }
    switch (requestType) {
      case SIMPLE:
      case ACTUAL:
        // Handles a Simple CORS request.
        // Handles an Actual CORS request.
        this.handleSimpleCors(request, response, filterChain);
        break;
      case PRE_FLIGHT:
        // Handles a Pre-flight CORS request.
        this.handlePreflightCors(request, response, filterChain);
        break;
      case NOT_CORS:
        // Handles a Normal request that is not a cross-origin request.
        this.handleNonCors(request, response, filterChain);
        break;
      default:
        // Handles a CORS request that violates specification.
        this.handleInvalidCors(request, response, filterChain);
        break;
    }
  }


  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    // Initialize defaults
    parseAndStore(DEFAULT_ALLOWED_ORIGINS, DEFAULT_ALLOWED_HTTP_METHODS,
        DEFAULT_ALLOWED_HTTP_HEADERS, DEFAULT_EXPOSED_HEADERS, DEFAULT_SUPPORTS_CREDENTIALS,
        DEFAULT_PREFLIGHT_MAXAGE, DEFAULT_DECORATE_REQUEST);

    if (filterConfig != null) {
      // 默认值为空
      String configAllowedOrigins = "";
      String configAllowedHttpMethods = this.getCorsAllowedMethods();
      String configAllowedHttpHeaders = this.getCorsAllowedHeaders();
      String configExposedHeaders = this.getCorsExposedHeaders();
      String configSupportsCredentials = this.getCorsSupportCredentials();
      String configPreflightMaxAge = this.getCorsPreflightMaxage();
      String configDecorateRequest = this.getCorsRequestDecorate();

      parseAndStore(configAllowedOrigins, configAllowedHttpMethods, configAllowedHttpHeaders,
          configExposedHeaders, configSupportsCredentials, configPreflightMaxAge,
          configDecorateRequest);
    }
  }


  /**
   * Handles a CORS request of type {@link CorsRequestType}.SIMPLE.
   *
   * @param request The {@link HttpServletRequest} object.
   * @param response The {@link HttpServletResponse} object.
   * @param filterChain The {@link FilterChain} object.
   * @see <a href="http://www.w3.org/TR/cors/#resource-requests">Simple Cross-Origin Request, Actual
   *      Request, and Redirects</a>
   */
  protected void handleSimpleCors(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws IOException, ServletException {

    CasCorsFilter.CorsRequestType requestType = checkRequestType(request);
    if (!(requestType == CasCorsFilter.CorsRequestType.SIMPLE
        || requestType == CasCorsFilter.CorsRequestType.ACTUAL)) {
      throw new IllegalArgumentException(sm.getString("casCORSFilter.wrongType2",
          CasCorsFilter.CorsRequestType.SIMPLE, CasCorsFilter.CorsRequestType.ACTUAL));
    }

    final String origin = request.getHeader(CasCorsFilter.REQUEST_HEADER_ORIGIN);
    final String method = request.getMethod();

    // Section 6.1.2
    if (!isOriginAllowed(origin)) {
      handleInvalidCors(request, response, filterChain);
      return;
    }

    if (!allowedHttpMethods.contains(method)) {
      handleInvalidCors(request, response, filterChain);
      return;
    }

    // Section 6.1.3
    // Add a single Access-Control-Allow-Origin header.
    if (anyOriginAllowed && !supportsCredentials) {
      // If resource doesn't support credentials and if any origin is
      // allowed
      // to make CORS request, return header with '*'.
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    } else {
      // If the resource supports credentials add a single
      // Access-Control-Allow-Origin header, with the value of the Origin
      // header as value.
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin);
    }

    // Section 6.1.3
    // If the resource supports credentials, add a single
    // Access-Control-Allow-Credentials header with the case-sensitive
    // string "true" as value.
    if (supportsCredentials) {
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    }

    // Section 6.1.4
    // If the list of exposed headers is not empty add one or more
    // Access-Control-Expose-Headers headers, with as values the header
    // field names given in the list of exposed headers.
    if ((exposedHeaders != null) && (!exposedHeaders.isEmpty())) {
      String exposedHeadersString = join(exposedHeaders, ",");
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
          exposedHeadersString);
    }

    // Forward the request down the filter chain.
    filterChain.doFilter(request, response);
  }


  /**
   * Handles CORS pre-flight request.
   *
   * @param request The {@link HttpServletRequest} object.
   * @param response The {@link HttpServletResponse} object.
   * @param filterChain The {@link FilterChain} object.
   */
  protected void handlePreflightCors(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws IOException, ServletException {

    CorsRequestType requestType = checkRequestType(request);
    if (requestType != CorsRequestType.PRE_FLIGHT) {
      throw new IllegalArgumentException(sm.getString("casCORSFilter.wrongType1",
          CorsRequestType.PRE_FLIGHT.name().toLowerCase(Locale.ENGLISH)));
    }

    final String origin = request.getHeader(CasCorsFilter.REQUEST_HEADER_ORIGIN);

    // Section 6.2.2
    if (!isOriginAllowed(origin)) {
      handleInvalidCors(request, response, filterChain);
      return;
    }

    // Section 6.2.3
    String accessControlRequestMethod =
        request.getHeader(CasCorsFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD);
    if (accessControlRequestMethod == null) {
      handleInvalidCors(request, response, filterChain);
      return;
    } else {
      accessControlRequestMethod = accessControlRequestMethod.trim();
    }

    // Section 6.2.4
    String accessControlRequestHeadersHeader =
        request.getHeader(CasCorsFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS);
    List<String> accessControlRequestHeaders = new LinkedList<String>();
    if (accessControlRequestHeadersHeader != null
        && !accessControlRequestHeadersHeader.trim().isEmpty()) {
      String[] headers = accessControlRequestHeadersHeader.trim().split(",");
      for (String header : headers) {
        accessControlRequestHeaders.add(header.trim().toLowerCase(Locale.ENGLISH));
      }
    }

    // Section 6.2.5
    if (!allowedHttpMethods.contains(accessControlRequestMethod)) {
      handleInvalidCors(request, response, filterChain);
      return;
    }

    // Section 6.2.6
    if (!accessControlRequestHeaders.isEmpty()) {
      for (String header : accessControlRequestHeaders) {
        if (!allowedHttpHeaders.contains(header)) {
          handleInvalidCors(request, response, filterChain);
          return;
        }
      }
    }

    // Section 6.2.7
    if (supportsCredentials) {
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin);
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    } else {
      if (anyOriginAllowed) {
        response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
      } else {
        response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin);
      }
    }

    // Section 6.2.8
    if (preflightMaxAge > 0) {
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE,
          String.valueOf(preflightMaxAge));
    }

    // Section 6.2.9
    response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS,
        accessControlRequestMethod);

    // Section 6.2.10
    if ((allowedHttpHeaders != null) && (!allowedHttpHeaders.isEmpty())) {
      response.addHeader(CasCorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS,
          join(allowedHttpHeaders, ","));
    }

    // Do not forward the request down the filter chain.
  }


  /**
   * Handles a request, that's not a CORS request, but is a valid request i.e. it is not a
   * cross-origin request. This implementation, just forwards the request down the filter chain.
   *
   * @param request The {@link HttpServletRequest} object.
   * @param response The {@link HttpServletResponse} object.
   * @param filterChain The {@link FilterChain} object.
   */
  private void handleNonCors(final HttpServletRequest request, final HttpServletResponse response,
      final FilterChain filterChain) throws IOException, ServletException {
    // Let request pass.
    filterChain.doFilter(request, response);
  }


  /**
   * Handles a CORS request that violates specification.
   *
   * @param request The {@link HttpServletRequest} object.
   * @param response The {@link HttpServletResponse} object.
   * @param filterChain The {@link FilterChain} object.
   */
  private void handleInvalidCors(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain) {
    String origin = request.getHeader(CasCorsFilter.REQUEST_HEADER_ORIGIN);
    String method = request.getMethod();
    String accessControlRequestHeaders =
        request.getHeader(REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS);

    response.setContentType("text/plain");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.resetBuffer();

    if (log.isDebugEnabled()) {
      // Debug so no need for i18n
      StringBuilder message = new StringBuilder("Invalid CORS request; Origin=");
      message.append(origin);
      message.append(";Method=");
      message.append(method);
      if (accessControlRequestHeaders != null) {
        message.append(";Access-Control-Request-Headers=");
        message.append(accessControlRequestHeaders);
      }
      log.debug(message.toString());
    }
  }


  @Override
  public void destroy() {
    // NOOP
  }


  /**
   * Decorates the {@link HttpServletRequest}, with CORS attributes.
   * <ul>
   * <li><b>cors.isCorsRequest:</b> Flag to determine if request is a CORS request. Set to
   * <code>true</code> if CORS request; <code>false</code> otherwise.</li>
   * <li><b>cors.request.origin:</b> The Origin URL.</li>
   * <li><b>cors.request.type:</b> Type of request. Values: <code>simple</code> or
   * <code>preflight</code> or <code>not_cors</code> or <code>invalid_cors</code></li>
   * <li><b>cors.request.headers:</b> Request headers sent as 'Access-Control-Request-Headers'
   * header, for pre-flight request.</li>
   * </ul>
   *
   * @param request The {@link HttpServletRequest} object.
   * @param corsRequestType The {@link CorsRequestType} object.
   */
  protected static void decorateCorsProperties(final HttpServletRequest request,
      final CorsRequestType corsRequestType) {
    if (request == null) {
      throw new IllegalArgumentException(sm.getString("casCORSFilter.nullRequest"));
    }

    if (corsRequestType == null) {
      throw new IllegalArgumentException(sm.getString("casCORSFilter.nullRequestType"));
    }

    switch (corsRequestType) {
      case SIMPLE:
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, Boolean.TRUE);
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
            request.getHeader(CasCorsFilter.REQUEST_HEADER_ORIGIN));
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
            corsRequestType.name().toLowerCase(Locale.ENGLISH));
        break;
      case ACTUAL:
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, Boolean.TRUE);
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
            request.getHeader(CasCorsFilter.REQUEST_HEADER_ORIGIN));
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
            corsRequestType.name().toLowerCase(Locale.ENGLISH));
        break;
      case PRE_FLIGHT:
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, Boolean.TRUE);
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
            request.getHeader(CasCorsFilter.REQUEST_HEADER_ORIGIN));
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
            corsRequestType.name().toLowerCase(Locale.ENGLISH));
        String headers = request.getHeader(REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS);
        if (headers == null) {
          headers = "";
        }
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_HEADERS, headers);
        break;
      case NOT_CORS:
        request.setAttribute(CasCorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, Boolean.FALSE);
        break;
      default:
        // Don't set any attributes
        break;
    }
  }


  /**
   * Joins elements of {@link Set} into a string, where each element is separated by the provided
   * separator.
   *
   * @param elements The {@link Set} containing elements to join together.
   * @param joinSeparator The character to be used for separating elements.
   * @return The joined {@link String}; <code>null</code> if elements {@link Set} is null.
   */
  protected static String join(final Collection<String> elements, final String joinSeparator) {
    String separator = ",";
    if (elements == null) {
      return null;
    }
    if (joinSeparator != null) {
      separator = joinSeparator;
    }
    StringBuilder buffer = new StringBuilder();
    boolean isFirst = true;
    for (String element : elements) {
      if (!isFirst) {
        buffer.append(separator);
      } else {
        isFirst = false;
      }

      if (element != null) {
        buffer.append(element);
      }
    }

    return buffer.toString();
  }


  /**
   * Determines the request type.
   */
  protected CorsRequestType checkRequestType(final HttpServletRequest request) {
    CorsRequestType requestType = CorsRequestType.INVALID_CORS;
    if (request == null) {
      throw new IllegalArgumentException(sm.getString("casCORSFilter.nullRequest"));
    }
    String originHeader = request.getHeader(REQUEST_HEADER_ORIGIN);
    // Section 6.1.1 and Section 6.2.1
    if (originHeader != null) {
      if (originHeader.isEmpty()) {
        requestType = CorsRequestType.INVALID_CORS;
      } else if (!isValidOrigin(originHeader)) {
        requestType = CorsRequestType.INVALID_CORS;
      } else if (isLocalOrigin(request, originHeader)) {
        return CorsRequestType.NOT_CORS;
      } else {
        String method = request.getMethod();
        if (method != null) {
          if ("OPTIONS".equals(method)) {
            String accessControlRequestMethodHeader =
                request.getHeader(REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD);
            if (accessControlRequestMethodHeader != null
                && !accessControlRequestMethodHeader.isEmpty()) {
              requestType = CorsRequestType.PRE_FLIGHT;
            } else if (accessControlRequestMethodHeader != null
                && accessControlRequestMethodHeader.isEmpty()) {
              requestType = CorsRequestType.INVALID_CORS;
            } else {
              requestType = CorsRequestType.ACTUAL;
            }
          } else if ("GET".equals(method) || "HEAD".equals(method)) {
            requestType = CorsRequestType.SIMPLE;
          } else if ("POST".equals(method)) {
            String mediaType = getMediaType(request.getContentType());
            if (mediaType != null) {
              if (SIMPLE_HTTP_REQUEST_CONTENT_TYPE_VALUES.contains(mediaType)) {
                requestType = CorsRequestType.SIMPLE;
              } else {
                requestType = CorsRequestType.ACTUAL;
              }
            }
          } else {
            requestType = CorsRequestType.ACTUAL;
          }
        }
      }
    } else {
      requestType = CorsRequestType.NOT_CORS;
    }

    return requestType;
  }


  private boolean isLocalOrigin(HttpServletRequest request, String origin) {

    // Build scheme://host:port from request
    StringBuilder target = new StringBuilder();
    String scheme = request.getScheme();
    if (scheme == null) {
      return false;
    } else {
      scheme = scheme.toLowerCase(Locale.ENGLISH);
    }
    target.append(scheme);
    target.append("://");

    String host = request.getServerName();
    if (host == null) {
      return false;
    }
    target.append(host);

    int port = request.getServerPort();
    if ("http".equals(scheme) && port != 80 || "https".equals(scheme) && port != 443) {
      target.append(':');
      target.append(port);
    }

    return origin.equalsIgnoreCase(target.toString());
  }


  /*
   * Return the lower case, trimmed value of the media type from the content type.
   */
  private String getMediaType(String contentType) {
    if (contentType == null) {
      return null;
    }
    String result = contentType.toLowerCase(Locale.ENGLISH);
    int firstSemiColonIndex = result.indexOf(';');
    if (firstSemiColonIndex > -1) {
      result = result.substring(0, firstSemiColonIndex);
    }
    result = result.trim();
    return result;
  }

  /**
   * Checks if the Origin is allowed to make a CORS request.
   *
   * @param origin The Origin.
   * @return <code>true</code> if origin is allowed; <code>false</code> otherwise.
   */
  private boolean isOriginAllowed(final String origin) {
    if (anyOriginAllowed) {
      return true;
    }

    // If 'Origin' header is a case-sensitive match of any of allowed
    // origins, then return true, else return false.

    // customize the isOriginAllowedLogicHere
    // return allowedOrigins.contains(origin);
    if (this.crossFilterCacheHelper != null) {
      for (Pattern pattern : this.crossFilterCacheHelper.getOriginRegularCacheSet()) {
        Matcher matcher = pattern.matcher(origin);
        if (matcher.matches()) {
          return true;
        }
      }
    }
    // for(String allowedOrigin : allowedOrigins) {
    // Pattern pattern = patternCache.get(allowedOrigin);
    // if (pattern == null) {
    // try {
    // pattern = Pattern.compile(allowedOrigin);
    // patternCache.put(allowedOrigin, pattern);
    // } catch(PatternSyntaxException pse) {
    // log.error("invalid regular pattern : " + allowedOrigin);
    // }
    // }
    //
    // if (pattern != null) {
    // Matcher matcher = pattern.matcher(origin);
    // if (matcher.matches()) {
    // return true;
    // }
    // }
    // }
    return false;
  }


  /**
   * Parses each param-value and populates configuration variables. If a param is provided, it
   * overrides the default.
   *
   * @param allowedOrigins A {@link String} of comma separated origins.
   * @param allowedHttpMethods A {@link String} of comma separated HTTP methods.
   * @param allowedHttpHeaders A {@link String} of comma separated HTTP headers.
   * @param exposedHeaders A {@link String} of comma separated headers that needs to be exposed.
   * @param supportsCredentials "true" if support credentials needs to be enabled.
   * @param preflightMaxAge The amount of seconds the user agent is allowed to cache the result of
   */
  private void parseAndStore(final String allowedOrigins, final String allowedHttpMethods,
      final String allowedHttpHeaders, final String exposedHeaders,
      final String supportsCredentials, final String preflightMaxAge, final String decorateRequest)
      throws ServletException {
    if (allowedOrigins != null) {
      if (allowedOrigins.trim().equals("*")) {
        this.anyOriginAllowed = true;
      } else {
        this.anyOriginAllowed = false;
        Set<String> setAllowedOrigins = parseStringToSet(allowedOrigins);
        this.allowedOrigins.clear();
        this.allowedOrigins.addAll(setAllowedOrigins);
      }
    }

    if (allowedHttpMethods != null) {
      Set<String> setAllowedHttpMethods = parseStringToSet(allowedHttpMethods);
      this.allowedHttpMethods.clear();
      this.allowedHttpMethods.addAll(setAllowedHttpMethods);
    }

    if (allowedHttpHeaders != null) {
      Set<String> setAllowedHttpHeaders = parseStringToSet(allowedHttpHeaders);
      Set<String> lowerCaseHeaders = new HashSet<String>();
      for (String header : setAllowedHttpHeaders) {
        String lowerCase = header.toLowerCase(Locale.ENGLISH);
        lowerCaseHeaders.add(lowerCase);
      }
      this.allowedHttpHeaders.clear();
      this.allowedHttpHeaders.addAll(lowerCaseHeaders);
    }

    if (exposedHeaders != null) {
      Set<String> setExposedHeaders = parseStringToSet(exposedHeaders);
      this.exposedHeaders.clear();
      this.exposedHeaders.addAll(setExposedHeaders);
    }

    if (supportsCredentials != null) {
      // For any value other then 'true' this will be false.
      this.supportsCredentials = Boolean.parseBoolean(supportsCredentials);
    }

    if (preflightMaxAge != null) {
      try {
        if (!preflightMaxAge.isEmpty()) {
          this.preflightMaxAge = Long.parseLong(preflightMaxAge);
        } else {
          this.preflightMaxAge = 0L;
        }
      } catch (NumberFormatException e) {
        throw new ServletException(sm.getString("casCORSFilter.invalidPreflightMaxAge"), e);
      }
    }

    if (decorateRequest != null) {
      // For any value other then 'true' this will be false.
      this.decorateRequest = Boolean.parseBoolean(decorateRequest);
    }
  }

  /**
   * Takes a comma separated list and returns a Set
   * 
   * @param data A comma separated list of strings.
   * @return Set
   */
  private Set<String> parseStringToSet(final String data) {
    String[] splits;

    if (data != null && data.length() > 0) {
      splits = data.split(";");
    } else {
      splits = new String[] {};
    }

    Set<String> set = new HashSet<String>();
    if (splits.length > 0) {
      for (String split : splits) {
        set.add(split.trim());
      }
    }

    return set;
  }


  /**
   * Checks if a given origin is valid or not. Criteria:
   * <ul>
   * <li>If an encoded character is present in origin, it's not valid.</li>
   * <li>If origin is "null", it's valid.</li>
   * <li>Origin should be a valid {@link URI}</li>
   * </ul>
   *
   * @see <a href="http://tools.ietf.org/html/rfc952">RFC952</a>
   */
  protected static boolean isValidOrigin(String origin) {
    // Checks for encoded characters. Helps prevent CRLF injection.
    if (origin.contains("%")) {
      return false;
    }

    // "null" is a valid origin
    if ("null".equals(origin)) {
      return true;
    }

    URI originUri;
    try {
      originUri = new URI(origin);
    } catch (URISyntaxException e) {
      return false;
    }
    // If scheme for URI is null, return false. Return true otherwise.
    return originUri.getScheme() != null;

  }


  /**
   * Determines if any origin is allowed to make CORS request.
   *
   * @return <code>true</code> if it's enabled; false otherwise.
   */
  public boolean isAnyOriginAllowed() {
    return anyOriginAllowed;
  }


  /**
   * Returns a {@link Set} of headers that should be exposed by browser.
   */
  public Collection<String> getExposedHeaders() {
    return exposedHeaders;
  }


  /**
   * Determines is supports credentials is enabled.
   */
  public boolean isSupportsCredentials() {
    return supportsCredentials;
  }


  /**
   * Returns the preflight response cache time in seconds.
   *
   * @return Time to cache in seconds.
   */
  public long getPreflightMaxAge() {
    return preflightMaxAge;
  }


  /**
   * Returns the {@link Set} of allowed origins that are allowed to make requests.
   *
   * @return {@link Set}
   */
  public Collection<String> getAllowedOrigins() {
    return allowedOrigins;
  }


  /**
   * Returns a {@link Set} of HTTP methods that are allowed to make requests.
   *
   * @return {@link Set}
   */
  public Collection<String> getAllowedHttpMethods() {
    return allowedHttpMethods;
  }


  /**
   * Returns a {@link Set} of headers support by resource.
   *
   * @return {@link Set}
   */
  public Collection<String> getAllowedHttpHeaders() {
    return allowedHttpHeaders;
  }

  // -------------------------------------------------- CORS Response Headers
  /**
   * The Access-Control-Allow-Origin header indicates whether a resource can be shared based by
   * returning the value of the Origin request header in the response.
   */
  public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN =
      "Access-Control-Allow-Origin";

  /**
   * The Access-Control-Allow-Credentials header indicates whether the response to request can be
   * exposed when the omit credentials flag is unset. When part of the response to a preflight
   * request it indicates that the actual request can include user credentials.
   */
  public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS =
      "Access-Control-Allow-Credentials";

  /**
   * The Access-Control-Expose-Headers header indicates which headers are safe to expose to the API.
   * of a CORS API specification
   */
  public static final String RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS =
      "Access-Control-Expose-Headers";

  /**
   * The Access-Control-Max-Age header indicates how long the results of a preflight request can be
   * cached in a preflight result cache.
   */
  public static final String RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

  /**
   * The Access-Control-Allow-Methods header indicates, as part of the response to a preflight
   * request, which methods can be used during the actual request.
   */
  public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS =
      "Access-Control-Allow-Methods";

  /**
   * The Access-Control-Allow-Headers header indicates, as part of the response to a preflight
   * request, which header field names can be used during the actual request.
   */
  public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS =
      "Access-Control-Allow-Headers";

  // -------------------------------------------------- CORS Request Headers
  /**
   * The Origin header indicates where the cross-origin request or preflight request originates
   * from.
   */
  public static final String REQUEST_HEADER_ORIGIN = "Origin";

  /**
   * The Access-Control-Request-Method header indicates which method will be used in the actual
   * request as part of the preflight request.
   */
  public static final String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD =
      "Access-Control-Request-Method";

  /**
   * The Access-Control-Request-Headers header indicates which headers will be used in the actual
   * request as part of the preflight request.
   */
  public static final String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS =
      "Access-Control-Request-Headers";

  // ----------------------------------------------------- Request attributes
  /**
   * The prefix to a CORS request attribute.
   */
  public static final String HTTP_REQUEST_ATTRIBUTE_PREFIX = "cors.";

  /**
   * Attribute that contains the origin of the request.
   */
  public static final String HTTP_REQUEST_ATTRIBUTE_ORIGIN =
      HTTP_REQUEST_ATTRIBUTE_PREFIX + "request.origin";

  /**
   * Boolean value, suggesting if the request is a CORS request or not.
   */
  public static final String HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST =
      HTTP_REQUEST_ATTRIBUTE_PREFIX + "isCorsRequest";

  /**
   * Type of CORS request, of type {@link CorsRequestType}.
   */
  public static final String HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE =
      HTTP_REQUEST_ATTRIBUTE_PREFIX + "request.type";

  /**
   * Request headers sent as 'Access-Control-Request-Headers' header, for pre-flight request.
   */
  public static final String HTTP_REQUEST_ATTRIBUTE_REQUEST_HEADERS =
      HTTP_REQUEST_ATTRIBUTE_PREFIX + "request.headers";

  // -------------------------------------------------------------- Constants

  /**
   * Enumerates varies types of CORS requests. Also, provides utility methods to determine the
   * request type.
   */
  protected static enum CorsRequestType {
    /**
     * A simple HTTP request, i.e. it shouldn't be pre-flighted.
     */
    SIMPLE,
    /**
     * A HTTP request that needs to be pre-flighted.
     */
    ACTUAL,
    /**
     * A pre-flight CORS request, to get meta information, before a non-simple HTTP request is sent.
     */
    PRE_FLIGHT,
    /**
     * Not a CORS request, but a normal request.
     */
    NOT_CORS,
    /**
     * An invalid CORS request, i.e. it qualifies to be a CORS request, but fails to be a valid one.
     */
    INVALID_CORS
  }

  /**
   * {@link Collection} of HTTP methods. Case sensitive.
   *
   * @deprecated Not used. Will be removed in Tomcat 9.0.x onwards.
   */
  @Deprecated
  public static final Collection<String> HTTP_METHODS = new HashSet<String>(
      Arrays.asList("OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "CONNECT"));

  /**
   * {@link Collection} of non-simple HTTP methods. Case sensitive.
   *
   * @deprecated Not used. Will be removed in Tomcat 9.0.x onwards. All HTTP methods not in
   *             {@link #SIMPLE_HTTP_METHODS} are assumed to be non-simple.
   */
  @Deprecated
  public static final Collection<String> COMPLEX_HTTP_METHODS =
      new HashSet<String>(Arrays.asList("PUT", "DELETE", "TRACE", "CONNECT"));
  /**
   * {@link Collection} of Simple HTTP methods. Case sensitive.
   *
   * @see <a href="http://www.w3.org/TR/cors/#terminology"
   *      >http://www.w3.org/TR/cors/#terminology</a>
   */
  public static final Collection<String> SIMPLE_HTTP_METHODS =
      new HashSet<String>(Arrays.asList("GET", "POST", "HEAD"));

  /**
   * {@link Collection} of Simple HTTP request headers. Case in-sensitive.
   *
   * @see <a href="http://www.w3.org/TR/cors/#terminology"
   *      >http://www.w3.org/TR/cors/#terminology</a>
   */
  public static final Collection<String> SIMPLE_HTTP_REQUEST_HEADERS =
      new HashSet<String>(Arrays.asList("Accept", "Accept-Language", "Content-Language"));

  /**
   * {@link Collection} of Simple HTTP request headers. Case in-sensitive.
   *
   * @see <a href="http://www.w3.org/TR/cors/#terminology"
   *      >http://www.w3.org/TR/cors/#terminology</a>
   */
  public static final Collection<String> SIMPLE_HTTP_RESPONSE_HEADERS =
      new HashSet<String>(Arrays.asList("Cache-Control", "Content-Language", "Content-Type",
          "Expires", "Last-Modified", "Pragma"));

  /**
   * {@link Collection} of media type values for the Content-Type header that will be treated as
   * 'simple'. Note media-type values are compared ignoring parameters and in a case-insensitive
   * manner.
   *
   * @see <a href="http://www.w3.org/TR/cors/#terminology"
   *      >http://www.w3.org/TR/cors/#terminology</a>
   */
  public static final Collection<String> SIMPLE_HTTP_REQUEST_CONTENT_TYPE_VALUES =
      new HashSet<String>(
          Arrays.asList("application/x-www-form-urlencoded", "multipart/form-data", "text/plain"));

  // ------------------------------------------------ Configuration Defaults
  /**
   * By default, all origins are allowed to make requests.
   */
  public static final String DEFAULT_ALLOWED_ORIGINS = "*";

  /**
   * By default, following methods are supported: GET, POST, HEAD and OPTIONS.
   */
  public static final String DEFAULT_ALLOWED_HTTP_METHODS = "GET,POST,HEAD,OPTIONS";

  /**
   * By default, time duration to cache pre-flight response is 30 mins.
   */
  public static final String DEFAULT_PREFLIGHT_MAXAGE = "1800";

  /**
   * By default, support credentials is turned on.
   */
  public static final String DEFAULT_SUPPORTS_CREDENTIALS = "true";

  /**
   * By default, following headers are supported: Origin,Accept,X-Requested-With, Content-Type,
   * Access-Control-Request-Method, and Access-Control-Request-Headers.
   */
  public static final String DEFAULT_ALLOWED_HTTP_HEADERS =
      "Origin,Accept,X-Requested-With,Content-Type,"
          + "Access-Control-Request-Method,Access-Control-Request-Headers";

  /**
   * By default, none of the headers are exposed in response.
   */
  public static final String DEFAULT_EXPOSED_HEADERS = "";

  /**
   * By default, request is decorated with CORS attributes.
   */
  public static final String DEFAULT_DECORATE_REQUEST = "true";

  // --------------------------------------------------------cache helper
  private CasCrossFilterCacheHelper crossFilterCacheHelper;

  /**
   * The crossFilterCacheHelper.
   */
  public CasCrossFilterCacheHelper getCrossFilterCacheHelper() {
    return crossFilterCacheHelper;
  }


  /**
   * Get crossFilterCacheHelper the crossFilterCacheHelper to set.
   */
  public void setCrossFilterCacheHelper(CasCrossFilterCacheHelper crossFilterCacheHelper) {
    this.crossFilterCacheHelper = crossFilterCacheHelper;
  }

  // ----------------------------------------Filter Config param-value(s)
  /**
   * support credentials from {@link FilterConfig}.
   */
  private String corsSupportCredentials;

  /**
   * Key to retrieve exposed headers from {@link FilterConfig}.
   */
  private String corsExposedHeaders;

  /**
   * allowed headers from {@link FilterConfig}.
   */
  private String corsAllowedHeaders;

  /**
   * allowed methods from {@link FilterConfig}.
   */
  private String corsAllowedMethods;

  /**
   * preflight max age from {@link FilterConfig}.
   */
  private String corsPreflightMaxage;

  /**
   * determine if request should be decorated.
   */
  private String corsRequestDecorate;

  public boolean isDecorateRequest() {
    return decorateRequest;
  }


  public void setDecorateRequest(boolean decorateRequest) {
    this.decorateRequest = decorateRequest;
  }


  public String getCorsSupportCredentials() {
    return corsSupportCredentials;
  }


  public void setCorsSupportCredentials(String corsSupportCredentials) {
    this.corsSupportCredentials = corsSupportCredentials;
  }


  public String getCorsExposedHeaders() {
    return corsExposedHeaders;
  }


  public void setCorsExposedHeaders(String corsExposedHeaders) {
    this.corsExposedHeaders = corsExposedHeaders;
  }


  public String getCorsAllowedHeaders() {
    return corsAllowedHeaders;
  }


  public void setCorsAllowedHeaders(String corsAllowedHeaders) {
    this.corsAllowedHeaders = corsAllowedHeaders;
  }


  public String getCorsAllowedMethods() {
    return corsAllowedMethods;
  }


  public void setCorsAllowedMethods(String corsAllowedMethods) {
    this.corsAllowedMethods = corsAllowedMethods;
  }


  public String getCorsPreflightMaxage() {
    return corsPreflightMaxage;
  }


  public void setCorsPreflightMaxage(String corsPreflightMaxage) {
    this.corsPreflightMaxage = corsPreflightMaxage;
  }


  public String getCorsRequestDecorate() {
    return corsRequestDecorate;
  }


  public void setCorsRequestDecorate(String corsRequestDecorate) {
    this.corsRequestDecorate = corsRequestDecorate;
  }


  public void setAnyOriginAllowed(boolean anyOriginAllowed) {
    this.anyOriginAllowed = anyOriginAllowed;
  }


  public void setSupportsCredentials(boolean supportsCredentials) {
    this.supportsCredentials = supportsCredentials;
  }


  public void setPreflightMaxAge(long preflightMaxAge) {
    this.preflightMaxAge = preflightMaxAge;
  }

  // ----------------------------------------Filter Config Init param-name(s)
  /**
   * Key to retrieve allowed origins from {@link FilterConfig}.
   */
  public static final String PARAM_CORS_ALLOWED_ORIGINS = "cors.allowed.origins";

  /**
   * Key to retrieve support credentials from {@link FilterConfig}.
   */
  public static final String PARAM_CORS_SUPPORT_CREDENTIALS = "cors.support.credentials";

  /**
   * Key to retrieve exposed headers from {@link FilterConfig}.
   */
  public static final String PARAM_CORS_EXPOSED_HEADERS = "cors.exposed.headers";

  /**
   * Key to retrieve allowed headers from {@link FilterConfig}.
   */
  public static final String PARAM_CORS_ALLOWED_HEADERS = "cors.allowed.headers";

  /**
   * Key to retrieve allowed methods from {@link FilterConfig}.
   */
  public static final String PARAM_CORS_ALLOWED_METHODS = "cors.allowed.methods";

  /**
   * Key to retrieve preflight max age from {@link FilterConfig}.
   */
  public static final String PARAM_CORS_PREFLIGHT_MAXAGE = "cors.preflight.maxage";

  /**
   * Key to determine if request should be decorated.
   */
  public static final String PARAM_CORS_REQUEST_DECORATE = "cors.request.decorate";
}
