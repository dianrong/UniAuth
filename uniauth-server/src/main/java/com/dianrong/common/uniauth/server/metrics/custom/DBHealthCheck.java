package com.dianrong.common.uniauth.server.metrics.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by Arc on 10/10/2016.
 */
@Component
public class DBHealthCheck extends CommonDBCheck {

    @Autowired
    private DataSource dataSource;

    @Override
    protected Result check() throws Exception {
        return getDBCheckResult(dataSource);
    }

}
