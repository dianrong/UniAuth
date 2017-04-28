package com.dianrong.common.uniauth.server.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.uniauth.server.service.support.TenancyIdentityService;

public class TenancyBasedService {
    @Autowired
    protected TenancyIdentityService tenancyIdentityService;

}
