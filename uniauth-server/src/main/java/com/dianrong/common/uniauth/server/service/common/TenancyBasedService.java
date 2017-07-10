package com.dianrong.common.uniauth.server.service.common;

import com.dianrong.common.uniauth.server.service.inner.TenancyInnerService;

import org.springframework.beans.factory.annotation.Autowired;

public class TenancyBasedService {
  @Autowired
  protected TenancyInnerService tenancyService;
}
