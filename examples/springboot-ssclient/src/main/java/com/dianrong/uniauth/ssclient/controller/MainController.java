package com.dianrong.uniauth.ssclient.controller;

import com.dianrong.uniauth.ssclient.bean.TestUser;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @RequestMapping(value = "/static-content", method = RequestMethod.GET)
    public String getAjaxRequestPage(HttpServletRequest request) {
        return "ajax-content";
    }
    
    @RequestMapping(value="ajax-request")
    @ResponseBody
    public TestUser ajaxRequest(HttpServletRequest request) {
        return new TestUser().setAge(29).setName("wwf");
    }

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    @ResponseBody
    public String apiPage() {
        return "Hello world api";
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @ResponseBody
    public String homePageRole() {
        return "This my home page";
    }

    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('techops')")
    @ResponseBody
    public String homePagePermission() {
        return "This my permission page";
    }

    @RequestMapping(value = "/permissionType", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('techops')")
    @ResponseBody
    public String homePagePermissionType() {
        return "This my permissionType page";
    }
    
    @RequestMapping(value = "/put-request", method = RequestMethod.PUT)
    @ResponseBody
    public String putRequest() {
        return "this is put request";
    }
    
    @RequestMapping(value = "/del-request", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteRequest() {
        return "this is delete request";
    }
    
    @RequestMapping(value = "/ajax/audit", method = RequestMethod.GET)
    @ResponseBody
    public String ajaxAudit() {
        return "Hello ajax audit!";
    }
}
