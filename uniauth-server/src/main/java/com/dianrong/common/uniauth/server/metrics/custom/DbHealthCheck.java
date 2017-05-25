package com.dianrong.common.uniauth.server.metrics.custom;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Arc on 10/10/2016.
 */
@Component
public class DbHealthCheck extends CommonDbCheck {

  @Autowired
  private DataSource dataSource;

  @Override
  protected Result check() throws Exception {
    return getDbCheckResult(dataSource);
  }

}
