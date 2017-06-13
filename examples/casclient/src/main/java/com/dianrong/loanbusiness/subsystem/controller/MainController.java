package com.dianrong.loanbusiness.subsystem.controller;

import com.dianrong.loanbusiness.subsystem.service.MyService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/main")
public class MainController {

  @Autowired
  private MyService myService;

  /**
   * 获取Common页面.
   */
  @RequestMapping(value = "/common", method = RequestMethod.GET)
  public String getCommonPage(HttpServletRequest request) {
    myService.testService();
    System.out.println(request.getRemoteUser());
    return "commonpage";
  }

  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  public String getAadminPage() {
    return "adminpage";
  }
}
