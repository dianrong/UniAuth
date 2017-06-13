package com.dianrong.uniauth.ssclient.controller;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/main")
public class MainController {

  @Value("#{uniauthConfig['domains.'+domainDefine.domainCode]}")
  private String service;

  @Value("#{uniauthConfig['cas_server']}")
  private String casUrl;

  @Value("#{uniauthConfig['domains.'+domainDefine.domainCode]}")
  private String customUrl;

  @Autowired
  private ServiceProperties casService;

  /**
   * 登陆Controller.
   */
  @SuppressWarnings("deprecation")
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String getCommonPage(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String lt = request.getParameter("lt");
    if (!StringUtils.hasLength(lt)) {
      String requestLtUrl = casUrl;
      if (requestLtUrl.contains("?")) {
        requestLtUrl += "&";
      } else {
        requestLtUrl += "?";
      }
      requestLtUrl += "service=" + URLEncoder.encode(casService.getService());
      response.sendRedirect(requestLtUrl);
      return null;
    }
    // 设置登陆的lt和对应的业务系统service
    request.setAttribute("ltval", lt);
    request.setAttribute("service", casService.getService());
    request.setAttribute("casUrl", casUrl);
    request.setAttribute("customUrl", customUrl);
    return "login";
  }

  @RequestMapping(value = "/content", method = RequestMethod.GET)
  public String getAadminPage(HttpServletRequest request) {
    return "content";
  }
}
