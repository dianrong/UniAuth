package com.dianrong.loanbusiness.subsystem.controller;

import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.loanbusiness.subsystem.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/main2")
@Slf4j
public class MainController2 {

  @SuppressWarnings("unused")
  @Autowired
  private MyService myService;

  @SuppressWarnings("unused")
  @Autowired
  private UniClientFacade uniClientFacade;

  /**
   * 获取Common页面.
   */
  @RequestMapping(value = "/common", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
  public String getCommonPage() {
    log.debug("Received request to show common page");
    // myService.testService();
    return "commonpage";
  }

  /**
   * 获取Admin页面.
   */
  @RequestMapping(value = "/admin", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
  public String getAadminPage() {
    log.debug("Received request to show admin page");
    return "adminpage";
  }

}
