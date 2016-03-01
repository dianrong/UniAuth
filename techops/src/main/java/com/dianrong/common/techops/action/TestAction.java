package com.dianrong.common.techops.action;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestAction {
    
    @RequestMapping(value = "/all" , method= RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_TEST')")
    public void test() {

    }
}