package com.dianrong.uniauth.ssclient.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(HttpServletRequest request) {
        return "forward:index.jsp";
    }

    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public String getAadminPage(HttpServletRequest request) {
        System.out.println(request.getRemoteUser());
        return "content";
    }
}
