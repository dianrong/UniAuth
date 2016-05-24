package com.dr.cas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class BorrowerLoginPageController extends AbstractController {

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    ModelAndView mv = new ModelAndView("borrowerLoginPage");
    String service = request.getParameter("service");
    mv.addObject("service", service);
    return mv;
  }

}
