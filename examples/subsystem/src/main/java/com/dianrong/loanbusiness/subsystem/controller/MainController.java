package com.dianrong.loanbusiness.subsystem.controller;

import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.loanbusiness.subsystem.model.TestModel;
import com.dianrong.loanbusiness.subsystem.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/main")
@Slf4j
public class MainController {

  @Autowired
  private MyService myService;

  @SuppressWarnings("unused")
  @Autowired
  private UniClientFacade uniClientFacade;

  @Autowired
  private ZooKeeperConfig zooKeeperConfig;


  /**
   * 获取Common页面.
   */
  @RequestMapping(value = "/common", method = RequestMethod.GET)
  public String getCommonPage() {
    log.debug("Received request to show common page");
    // myService.testService();
    return "commonpage";
  }

  /**
   * 获取Admin页面.
   */
  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
  public String getAadminPage() {
    for (int i = 0; i < 100; i++) {
      System.out.println("----------------------------------------------");
    }

    System.out.println(zooKeeperConfig.getDomainUrl());
    System.out.println(zooKeeperConfig.getCasServerUrl());

    TestModel tm = new TestModel();
    myService.testModel(tm);
    log.debug("Received request to show admin page");
    return "adminpage";

  }

  /**
   * 测试.
   */
  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public String test() {
    for (int i = 0; i < 100; i++) {
      System.out.println("----------------------------------------------");
    }

    System.out.println(zooKeeperConfig.getDomainUrl());
    System.out.println(zooKeeperConfig.getCasServerUrl());

    return "adminpage";
  }
}
