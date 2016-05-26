package com.dr.cas.controller;

import java.util.List;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.dr.cas.request.RequestErrorKey;
import com.google.code.kaptcha.Constants;

public abstract class AbstractSSOController extends AbstractController {

  private static String LC_EMAIL_COOKIE = "SL_EMAIL";
  private static int SECONDS_PER_YEAR = 3600*24*365;

  protected int USER_NAME_LENGTH_LIMIT = 12;

  protected static final Pattern usernamePattern = Pattern.compile(
      "^[^\\s><#!?$*%@\\^()\\[\\]\\{\\}\\;:,\\\"'\\/&\\+`~=\\|\\\\]{2,48}$"); // Letter,

  protected static final Pattern emailPattern = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");

  protected static Logger logger = LoggerFactory.getLogger(AbstractSSOController.class);


  /**
   * Firefox: supported  (Mozilla/5.0 (Windows NT 6.2; WOW64; rv:15.0) Gecko/20120910144328 Firefox/15.0.2 )
   * MSIE: not supported
   * IE8: not supported (Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)
   * hero: not supported: (dianrong custom)
   * Safari: not supported (Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/601.5.17 (KHTML, like Gecko)
      Version/9.1 Safari/601.5.17)
   */
  private static final String USER_AGENTS_REGEX = ".*(Trident|MSIE|hero|AppleWebKit).*";

  private static final String DEFAULT_SERVICE = "https://www.dianrong.com/api/v2/j_spring_cas_security_check";
  private static final String DEFAULT_HOME = "https://www.dianrong.com/account/my-account?fromLogin=true";
  
  @Autowired
  private CentralAuthenticationService centralAuthenticationService;

  @Autowired
  private CookieRetrievingCookieGenerator warnCookieGenerator;

  @Autowired
  private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

  /** Extractors for finding the service. */
  private List<ArgumentExtractor> argumentExtractors;

  private static boolean pathPopulated = false;

  abstract protected boolean validate(Credential credential, ModelAndView model);
  abstract protected RememberMeUsernamePasswordCredential createCredential(HttpServletRequest request);
  abstract protected void handleLoginResult(Credential credential, ModelAndView model);

  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    if (!pathPopulated) {
      final String contextPath = request.getContextPath();
      final String cookiePath = !StringUtils.isEmpty(contextPath) ? contextPath + "/" : "/";
      // always use default cookie path of "/"
      logger.info("setting Cookie path to: " + cookiePath);
      if (!cookiePath.equals(warnCookieGenerator.getCookiePath())) {
        this.warnCookieGenerator.setCookiePath(cookiePath);
        this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
      }
      pathPopulated = true;
    }

    String redirectUrl = DEFAULT_HOME;
    String serviceUrl = DEFAULT_SERVICE;
    String targetUrl = request.getParameter("targetUrl");
    
    boolean isAjax = StringUtils.isBlank(targetUrl);
    
    if (!isAjax) {
      redirectUrl = targetUrl;
    }

    String jspView = isAjax ? "casLoginResultView" : "casLoginPageView";
    ModelAndView modelAndView = new ModelAndView(jspView);
    
    RememberMeUsernamePasswordCredential credentials = createCredential(request);
    String username = credentials.getUsername();

    try {
      Service service = WebUtils.getService(this.argumentExtractors, request);

      serviceUrl = service.getId();
      if (!validate(credentials, modelAndView)) {
        // validation error
        modelAndView.addObject("code", "400");
        return modelAndView;
      }

      // call AuthenticationHandlers
      TicketGrantingTicket ticketGrantingTicketId = this.centralAuthenticationService
            .createTicketGrantingTicket(credentials);

      // create service ticket
      ServiceTicket serviceTicket = this.centralAuthenticationService.grantServiceTicket(
            ticketGrantingTicketId.getId(), service);

      this.ticketGrantingTicketCookieGenerator.removeCookie(response);
      this.ticketGrantingTicketCookieGenerator.addCookie(request, response,
          ticketGrantingTicketId.getId());

      this.warnCookieGenerator.addCookie(request, response, "true");

      // user has logged in successfully; set the cookie on the
      // process the rememberMe to set the cookie header
      if ("true".equals(credentials.isRememberMe())) {
        addCookie(response, LC_EMAIL_COOKIE, username, SECONDS_PER_YEAR);
      } else {
        removeCookie(response, LC_EMAIL_COOKIE);
      }


      String userAgent = request.getHeader("User-Agent");
      // is IE or 360
      if (isAjax && userAgent != null && userAgent.matches(USER_AGENTS_REGEX)) {
        logger.info("handle IE request " + userAgent);
        handleLoginResult(credentials, modelAndView);
        modelAndView.addObject("serviceTicket", serviceTicket.getId());
        return modelAndView;
      }


      String url = serviceUrl;
      // Android hack !!!
      // if (userAgent.contains("Android")) {
      //  url = url.replace("main.dianrong.com", "192.168.18.23");
      // }
      if (url.indexOf("?") == -1) {
        url += "?";
      } else {
        url += "&";
      }

      // set fromLogin=true
      url += "ticket=" + serviceTicket.getId();
      if (StringUtils.isBlank(targetUrl)) {
        url += "&fromLogin=true";
      } else {
        url += "&targetUrl=" + targetUrl;
      }

      logger.info("redirecting to url " + url);
      response.sendRedirect(url);

      return null;
    } catch (Exception e) {
      handleLoginException(e, modelAndView);
      handleLoginResult(credentials, modelAndView);
    } finally {
      modelAndView.addObject("service", serviceUrl);
      modelAndView.addObject("redirectUrl", redirectUrl);
      modelAndView.addObject("identity", username);
      request.getSession().removeAttribute(Constants.CAPCHA_SESSION_KEY);
    }

    return modelAndView;
  }

  public void setWarnCookieGenerator(
      final CookieRetrievingCookieGenerator warnCookieGenerator) {
    this.warnCookieGenerator = warnCookieGenerator;
  }

  public void setArgumentExtractors(
      final List<ArgumentExtractor> argumentExtractors) {
    this.argumentExtractors = argumentExtractors;
  }

  public final void setCentralAuthenticationService(
      final CentralAuthenticationService centralAuthenticationService) {
    this.centralAuthenticationService = centralAuthenticationService;
  }

  public void setTicketGrantingTicketCookieGenerator(
      final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
    this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
  }

  private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    // if set then set the cookie to the email specified
    Cookie c = new Cookie(name, value);
    c.setMaxAge(maxAge);
    c.setPath("/");
    c.setDomain("dianrong.com");
    response.addCookie(c);
  }

  private void removeCookie(HttpServletResponse response, String name) {
    addCookie(response, name, "", 0);
  }

  protected static boolean validateEmail(String email) {
    if (StringUtils.isBlank(email)) {
      return false;
    }
    if (email.length() > 50) {
      return false;
    }
    if (!matches(email, emailPattern)) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the input string matches the given regular expression pattern
   *
   * @param input String to test
   * @param pattern regular expression to match
   * @return true if the String matches the regular expression
   */
  protected static boolean matches(String input, Pattern pattern) {
    return pattern.matcher(input).matches();
  }

  protected void handleLoginException(Exception e, ModelAndView model) {

    RequestErrorKey errorKey;
    if (e instanceof AuthenticationException) {
      Class<? extends Exception> clazz = ((AuthenticationException)e).
          getHandlerErrors().values().iterator().next();
      if (LoginException.class.isAssignableFrom(clazz)) {
        errorKey = RequestErrorKey.valueOf(clazz.getSimpleName());
        model.addObject("code", "400");
      }
      else {
        logger.info("caught PreventedException " + e.getMessage());
        errorKey = RequestErrorKey.UNEXPECTED_ERROR;
        model.addObject("code", "500");
      }
    } else {
      // uncaught exceptions thrown by CAS framework
      errorKey = RequestErrorKey.UNEXPECTED_ERROR;
      logger.error("unexpected exception ", e);
    }
    model.addObject("errorKey", errorKey.name());
  }

}


