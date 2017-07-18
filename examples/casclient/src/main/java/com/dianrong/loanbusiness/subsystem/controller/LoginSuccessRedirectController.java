package com.dianrong.loanbusiness.subsystem.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/login")
public class LoginSuccessRedirectController {

  /**
   * 跳转主页.
   */
  @RequestMapping(value = "/cas", method = RequestMethod.GET)
  public String getCommonPage(HttpServletRequest request) {
    return "redirect:/main/common";
  }
}
