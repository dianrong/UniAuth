package com.dianrong.common.uniauth.common.cache.redis;

import com.dianrong.common.uniauth.common.util.StringUtil;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Redis ConnectionFactory的配置参数.
 */
@Slf4j @ToString public class RedisConnectionFactoryConfiguration {
  /**
   * 选取redis的database,默认值为0.
   */
  private int database = 0;

  /**
   * 连接redis的密码
   */
  private String password;

  /**
   * 连接的超时时间(毫秒数).
   */
  private int timeout = 3000;

  // Single configuration
  /**
   * Redis服务器Host.
   */
  private String host = "localhost";

  /**
   * Redis服务器端口.
   */
  private int port = 6379;

  // Sentinel configuration
  /**
   * Master节点.
   */
  private String master = "mymaster";
  /**
   * 哨兵节点集合.
   */
  private Set<String> sentinels;

  // Cluster configuration
  /**
   * 集群节点集合.
   */
  private Set<String> clusterNodes;

  /**
   * 集群失败之后的重定向次数.
   */
  private int maxRedirects = 5;

  public int getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    if (database == null) {
      return;
    }
    Integer tempDatabase = StringUtil.tryToTranslateStrToInt(database);
    Assert.isTrue(tempDatabase == null || tempDatabase >= 0,
        "parameter database must be equal or greater than 0");
    this.database = tempDatabase;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(String timeout) {
    if (timeout == null) {
      return;
    }
    Integer tempTimeout = StringUtil.tryToTranslateStrToInt(timeout);
    Assert.isTrue(tempTimeout == null || tempTimeout >= 0,
        "parameter timeout must be equal or greater than 0");
    this.timeout = tempTimeout;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(String port) {
    if (port == null) {
      return;
    }
    Integer tempPort = StringUtil.tryToTranslateStrToInt(port);
    Assert.isTrue(tempPort == null || (tempPort > 0 && tempPort < 65535),
        "parameter timeout must be greater than 0 and less than 65535");
    this.port = tempPort;
  }

  public String getMaster() {
    return master;
  }

  public void setMaster(String master) {
    this.master = master;
  }

  public Set<String> getSentinels() {
    return sentinels;
  }

  public void setSentinels(String sentinels) {
    if (sentinels == null) {
      return;
    }
    this.sentinels = StringUtils.commaDelimitedListToSet(sentinels.trim());
  }

  public Set<String> getClusterNodes() {
    return clusterNodes;
  }

  public void setClusterNodes(String clusterNodes) {
    if (clusterNodes == null) {
      return;
    }
    this.clusterNodes = StringUtils.commaDelimitedListToSet(clusterNodes.trim());
  }

  public int getMaxRedirects() {
    return maxRedirects;
  }

  public void setMaxRedirects(String maxRedirects) {
    if (maxRedirects == null) {
      return;
    }
    Integer tempMaxRedirects = StringUtil.tryToTranslateStrToInt(maxRedirects);
    Assert.isTrue(tempMaxRedirects == null || (tempMaxRedirects >= 0),
        "parameter timeout must be equal or greater than 0");
    this.maxRedirects = tempMaxRedirects;
  }
}
