package com.dianrong.uniauth.ssclient.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianrong.uniauth.ssclient.bean.TestUser;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(HttpServletRequest request) {
        return "forward:index.jsp";
    }

    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public String getAadminPage(HttpServletRequest request) {
        return "content";
    }
    
    @RequestMapping(value = "/static_content", method = RequestMethod.GET)
    public String getAjaxRequestPage(HttpServletRequest request) {
        return "ajax_content";
    }
    
    @RequestMapping(value="ajax_request")
    @ResponseBody
    public TestUser ajaxRequest(HttpServletRequest request) {
        return new TestUser().setAge(29).setName("wwf");
    }
}
