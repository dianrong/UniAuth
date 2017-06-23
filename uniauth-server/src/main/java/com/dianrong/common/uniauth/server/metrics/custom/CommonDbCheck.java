package com.dianrong.common.uniauth.server.metrics.custom;

import com.codahale.metrics.health.HealthCheck;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Arc on 4/11/2016.
 */
public abstract class CommonDbCheck extends HealthCheck {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommonDbCheck.class);

  protected static final String DEFAULT_QUERY = "SELECT 1 FROM DUAL";

  protected Result getDbCheckResult(DataSource dataSource) {
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      connection = dataSource.getConnection();
      stmt = connection.createStatement();
      stmt.setQueryTimeout(3);
      rs = stmt.executeQuery(DEFAULT_QUERY);
      while (rs.next()) {
        int value = rs.getInt("1");
        if (value == 1) {
          return Result.healthy();
        }
      }
    } catch (Exception e) {
      return Result.unhealthy(e);
    } finally {
      closeQuietly(rs);
      closeQuietly(stmt);
      closeQuietly(connection);
    }
    return Result.unhealthy("unknown");
  }

  private void closeQuietly(AutoCloseable autoCloseable) {
    try {
      if (autoCloseable != null) {
        autoCloseable.close();
      }
    } catch (Exception e) {
      LOGGER.error("CommonDBCheck closeQuietly", e);
    }
  }

}
