package com.dianrong.common.uniauth.server.metrics.custom;

import com.codahale.metrics.health.HealthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Arc on 10/10/2016.
 */
@Component
public class DBHealthCheck extends HealthCheck {

    private final String DEFAULT_QUERY = "SELECT 1 FROM DUAL";

    @Autowired
    private DataSource dataSource;

    @Override
    protected Result check() throws Exception {
        try {
            Connection connection = dataSource.getConnection();
            Statement stmt = connection.createStatement();
            stmt.setQueryTimeout(3);
            ResultSet rs = stmt.executeQuery(DEFAULT_QUERY);
            while(rs.next()) {
                int value = rs.getInt("1");
                if(value == 1) {
                    return Result.healthy();
                }
            }
        } catch(Exception e) {
            return Result.unhealthy(e);
        }
        return Result.unhealthy("unknown");
    }
}
