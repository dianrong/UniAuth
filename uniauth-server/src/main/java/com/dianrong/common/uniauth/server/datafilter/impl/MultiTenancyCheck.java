package com.dianrong.common.uniauth.server.datafilter.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.uniauth.server.service.support.TenancyIdentityService;

public abstract class MultiTenancyCheck extends AbstractDataFilter {
    // 目前是基于多租户的数据检验
    @Autowired
    private TenancyIdentityService tenancyIdentityService;

    /**
     *  获取当前的租户id
     */
    public Long getTenancyId() {
        return tenancyIdentityService.getTenancyIdWithCheck();
    }


    public TenancyIdentityService getTenancyIdentityService() {
        return tenancyIdentityService;
    }


    public void setTenancyIdentityService(TenancyIdentityService tenancyIdentityService) {
        this.tenancyIdentityService = tenancyIdentityService;
    }
}
