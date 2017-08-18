package com.dianrong.loanbusiness.subsystem.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/main")
public class MainController {

  @Value("#{uniauthConfig['domains.custom-casclient']}")
  private String service;

  @Value("#{uniauthConfig['cas_server']}")
  private String casUrl;

  @Value("#{uniauthConfig['domains.custom-casclient']}")
  private String customUrl;

  /**
   * 登陆Controller.
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String getCommonPage(HttpServletRequest request) {
    request.setAttribute("casUrl", casUrl);
    return "login";
  }

  @RequestMapping(value = "/content", method = RequestMethod.GET)
  public String getAadminPage(HttpServletRequest request) {
    System.out.println(request.getRemoteUser());
    return "content";
  }
}
