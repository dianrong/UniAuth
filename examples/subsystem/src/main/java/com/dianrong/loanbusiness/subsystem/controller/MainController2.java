package com.dianrong.loanbusiness.subsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.loanbusiness.subsystem.service.MyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/main2")
@Slf4j
public class MainController2 {

  @Autowired
  private MyService myService;

  @Autowired
  private UniClientFacade uniClientFacade;

  @RequestMapping(value = "/common", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
  public String getCommonPage() {
    log.debug("Received request to show common page");
    // myService.testService();
    return "commonpage";
  }

  @RequestMapping(value = "/admin", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
  public String getAadminPage() {
        /*
         * DomainParam domainParam = new DomainParam(); domainParam.setCode("techops");
         * Response<List<UrlRoleMappingDto>> response =
         * uniClientFacade.getPermissionResource().getUrlRoleMapping(domainParam);
         * 
         * TestModel tm = new TestModel(); myService.testModel(tm);
         */
    log.debug("Received request to show admin page");
    return "adminpage";
  }

}
