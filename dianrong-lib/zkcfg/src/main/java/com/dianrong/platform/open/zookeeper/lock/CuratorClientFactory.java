package com.dianrong.platform.open.zookeeper.lock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("openCuratorClientFactory")
public class CuratorClientFactory {

  private static final Logger log = LoggerFactory.getLogger(CuratorClientFactory.class);
  private static final int DEFAULT_SESSION_TIMEOUT = 15000;
  private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

  @Value("${DR_CFG_ZOOKEEPER_ENV_URL}")
  private String connectServer;

  private int connectionTimeout;

  private int sessionTimeout;

  private CuratorFramework client;

  public CuratorFramework getClient() {
    return client;
  }

  @PostConstruct
  private void init() {
    log.debug("init CuratorClientFactory ...");
    connectServer = StringUtils.isEmpty(connectServer)
        ? System.getProperty("DR_CFG_ZOOKEEPER_ENV_URL") : connectServer;
    if (StringUtils.isEmpty(connectServer)) {
      throw new IllegalArgumentException("connectServer not exist!!!");
    }
    if (connectionTimeout <= 0) {
      connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    }
    if (sessionTimeout <= 0) {
      sessionTimeout = DEFAULT_SESSION_TIMEOUT;
    }
    log.debug("connectServer: {}", connectServer);
    log.debug("connectionTimeout: {}", connectionTimeout);
    log.debug("sessionTimeout: {}", sessionTimeout);

    client = CuratorFrameworkFactory.builder().connectString(connectServer)
        .connectionTimeoutMs(connectionTimeout).sessionTimeoutMs(sessionTimeout)
        .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();

    /*
     * client.getConnectionStateListenable().addListener( new ConnectionStateListener() {
     * 
     * @Override public void stateChanged(CuratorFramework client, ConnectionState newState) {
     * //log.debug("Connection State Changed: ", newState.toString()); } } );
     */
    client.start();
    try {
      client.getZookeeperClient().blockUntilConnectedOrTimedOut();
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
    log.info("init CuratorClientFactory ok!");
  }

  @PreDestroy
  private void destroy() {
    log.debug("destroy CuratorClientFactory ...");
    if (client != null && client.getState().equals(CuratorFrameworkState.STARTED)) {
      client.close();
      client = null;
    }
    log.info("destroy CuratorClientFactory ok!");
  }

}
