package com.dr.cas.controller;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class LoginPageController extends AbstractController {

  private static final String DIANRONG_DOMAIN = ".dianrong.com";
  private static final String DEFAULT_HOME_URL = "https://www" + DIANRONG_DOMAIN + "/account/my-account?fromLogin=true";
  private static final String DEFAULT_SERVICE_URI = "/api/v2/j_spring_cas_security_check";

  protected static Logger logger = LoggerFactory.getLogger(LoginPageController.class);
  
  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    ModelAndView mv = new ModelAndView("loginPage");
   
    String referer = request.getHeader("Referer");
    String redirectUrl = request.getParameter("redirectUrl");
    boolean redirect = "true".equals(request.getParameter("redirect"));
    
    if (redirect) {
      redirectUrl = referer; //use the refer url from header
    } else if (redirectUrl == null || redirectUrl.isEmpty()) {
      redirectUrl = DEFAULT_HOME_URL;
    }
   
    String service = locateService(redirectUrl);
    mv.addObject("redirectUrl", redirectUrl);
    mv.addObject("service", service);
 
    return mv;
  }
  
  
  private static String locateService(String redirectUrl) {
    
    String domain = getDianrongDomain(redirectUrl);
    
    String serviceUri = DEFAULT_SERVICE_URI;

    // guess service ID, forum
    if (domain.startsWith("forum")) {
      serviceUri = "/sso.php";
    }
    else {
      // /api/v2/j_spring_cas_security_check
      serviceUri = DEFAULT_SERVICE_URI;
    }

    // on dev env, use http
    String scheme = domain.startsWith("localhost") ? "http://" : "https://";
    // service url always SSL
    return scheme + domain + serviceUri;
  }

  private static String getDianrongDomain(String url) {
    String domain = "";
    if (url == null || url.isEmpty()) {
      return "www" + DIANRONG_DOMAIN;
    }

    try {
      URL u = new URL(url);
      domain += u.getHost();
      if (!domain.endsWith(DIANRONG_DOMAIN)
          && !domain.equals("localhost")) { // for localhost dev environment
        // invalid redirect url
        throw new IllegalArgumentException("invalid domain");
      }
      if (u.getPort() > 0) {
        domain += ":" + u.getPort();
      }
    } catch (Exception e) {
      logger.info("return default domain on invalid url " + url);
      domain = "www" + DIANRONG_DOMAIN;
    }
    return domain;
  }
  
  
  public static void main(String[] args) {
    
    System.out.println(getDianrongDomain("https://www-dev.dianrong.com/account/my-account?fromLogin=true"));
    System.out.println(getDianrongDomain("https://www-demo.dianrong.com/account/my-account?fromLogin=true"));
    System.out.println(getDianrongDomain("https://www.dianrong.com/account/my-account?fromLogin=true"));
    System.out.println(getDianrongDomain("http://www.yahoo.com/hahah"));
    System.out.println(getDianrongDomain("http://forum.dianrong.com/myhome"));
    System.out.println(getDianrongDomain("http://forum-dev.dianrong.com/myhome"));
    System.out.println(getDianrongDomain("http://borrower.dianrong.com/myhome"));
    System.out.println(getDianrongDomain("http://localhost:9888/api/v2/loans"));
    System.out.println(getDianrongDomain(null));

    System.out.println(locateService("https://www-dev.dianrong.com/account/my-account?fromLogin=true"));
    System.out.println(locateService("https://www-demo.dianrong.com/account/my-account?fromLogin=true"));
    System.out.println(locateService("https://www.dianrong.com/account/my-account?fromLogin=true"));
    System.out.println(locateService("http://www.yahoo.com/hahah"));
    System.out.println(locateService("http://forum.dianrong.com/myhome"));
    System.out.println(locateService("http://forum-dev.dianrong.com/myhome"));
    System.out.println(locateService("http://borrower.dianrong.com/myhome"));
    System.out.println(locateService("http://borrower-dev.dianrong.com/myhome"));
    System.out.println(locateService("http://localhost:9888/api/v2/loans"));
    System.out.println(locateService(null));
    
    
  }
}
