package com.dianrong.common.uniauth.server.service;

import org.springframework.beans.factory.annotation.Autowired;

public class TenancyBasedService {
    @Autowired
    protected TenancyService tenancyService;

    public TenancyService getTenancyService() {
        return tenancyService;
    }

    public void setTenancyService(TenancyService tenancyService) {
        this.tenancyService = tenancyService;
    }
}
