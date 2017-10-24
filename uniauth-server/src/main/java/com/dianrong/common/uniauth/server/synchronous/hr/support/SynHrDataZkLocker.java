package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.SystemUtil;
import com.dianrong.common.uniauth.server.synchronous.exp.AcquireLockFailureException;
import com.dianrong.common.uniauth.server.synchronous.support.ProcessLocker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 基于ZK实现的同步HR数据的锁.
 */
@Component
@Slf4j
public class SynHrDataZkLocker implements ProcessLocker, InitializingBean {

  /**
   * 默认的Lock节点名称.
   */
  public static final String DEFAULT_LOCK_NODE_NAME = "syn_hr_data_lock";

  /**
   * 默认的基础节点名称.
   */
  public static final String DEFAULT_BASE_NODE_NAME = "/com/dianrong/cfg/1.0.0/uniauth";

  /**
   * 开关.
   */
  @Autowired
  private HrDataSynchronousSwitcher switchControl;

  /**
   * Zookeeper的连接字符串.
   */
  @Value("${DR_CFG_ZOOKEEPER_ENV_URL}")
  private String zkConnectionString;

  private String lockNodeName = DEFAULT_LOCK_NODE_NAME;

  private String baseNodePath;

  private String lockNodePath;

  private int sessionTimeOut = 5000;

  private CuratorFramework zkClient;

  private String lockNodeContent;

  /**
   * 默认的重试机制.
   */
  private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

  /**
   * 标识是否处于获取锁的状态.
   */
  private volatile boolean lockFlag = false;


  /**
   * 初始化操作.
   */
  public void init() {
    if (!switchControl.isOn()) {
      log.info("Synchronous switcher is off, so don't init HR data synchronous zookeeper locker."
          + "If want set up, please config zk[synchronization.hr.switch=true].");
      return;
    }

    Assert.notNull(zkConnectionString, "Missing zookeeper connection string");
    log.debug("ConnectionString: {}", zkConnectionString);
    log.debug("sessionTimeout: {}", sessionTimeOut);
    initZkNode();
    this.zkClient = CuratorFrameworkFactory.builder().connectString(this.zkConnectionString)
        .sessionTimeoutMs(this.sessionTimeOut).retryPolicy(this.retryPolicy).build();
    // 添加断线监听
    this.zkClient.getConnectionStateListenable().addListener(new LockNodeConnectionStateListener());
    this.zkClient.start();

    try {
      this.zkClient.getZookeeperClient().blockUntilConnectedOrTimedOut();
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
      throw new UniauthCommonException(e.getMessage(), e);
    }

    tryAcquireLock();
  }

  private void tryAcquireLock() {
    // 是否重复尝试获取锁
    boolean continueTry = true;
    while (continueTry) {
      continueTry = false;
      // 尝试获取锁
      try {
        String localIP = SystemUtil.getLocalIp();
        this.zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
            .forPath(this.lockNodePath, localIP.getBytes());
        // 获取锁成功
        this.lockFlag = true;
        this.lockNodeContent = localIP;
      } catch (KeeperException.NodeExistsException e) {
        log.info("Acquire lock failed:" + ExceptionUtils.getStackTrace(e));
        this.lockFlag = false;
        // 获取最新的LockNode信息.
        try {
          this.lockNodeContent =
              new String(this.zkClient.getData().forPath(this.lockNodePath), "UTF-8");
        } catch (Exception e1) {
          log.error("Failed to get lock node name");
          this.lockNodeContent = SystemUtil.UNKOWN_IP;
        }
        // 开启监视
        PathChildrenCache cache = new PathChildrenCache(this.zkClient, this.baseNodePath, true);
        try {
          cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e1) {
          log.info("Listen zookeeper error.", e);
        }
        cache.getListenable().addListener(new PathChildrenCacheListener() {
          @Override
          public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
              throws Exception {
            if (event != null && event.getData() != null && getLockNodePath()
                .equals(event.getData().getPath())) {
              switch (event.getType()) {
                case CHILD_UPDATED:
                  // Update 最新的节点值.
                  setLockNodeContent(event.getData().toString());
                  break;
                case CHILD_REMOVED:
                  // 尝试获取锁
                  tryAcquireLock();
                  break;
                default:
                  break;
              }
            }
          }
        });
      } catch (Exception e) {
        log.info("Failed acquire lock,sleep 5 seconds and retry.", e);
        try {
          Thread.sleep(5000L);
        } catch (InterruptedException e1) {
          log.debug("Thread sleep interrupted", e1);
        }
        // 重新尝试获取锁. 如果直接调用自己的话,无限的重试会造成栈溢出.
        continueTry = true;
      }
    }
  }

  @Override
  public void lock() throws AcquireLockFailureException {
    if (this.lockFlag) {
      return;
    }
    AcquireLockFailureException afe = new AcquireLockFailureException(
        "Lock is hold by Server :" + this.lockNodeContent);
    afe.setHoldLockServerIp(this.lockNodeContent);
    throw afe;
  }

  /**
   * 初始化node.
   */
  private void initZkNode() {
    String systemSetZkPath = System.getProperty("DR_CFG_ZOOKEEPER_BASE_PATH");
    if (StringUtils.hasText(systemSetZkPath)) {
      this.baseNodePath = systemSetZkPath.trim() + "/uniauth";
    } else {
      this.baseNodePath = DEFAULT_BASE_NODE_NAME;
    }
    this.lockNodePath = baseNodePath + "/" + this.lockNodeName;
  }

  @Override
  public boolean tryLock() {
    return lockFlag;
  }

  /**
   * 监听断线情况.
   */
  private class LockNodeConnectionStateListener implements ConnectionStateListener {

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
      if (ConnectionState.LOST.equals(newState)) {
        // 遇到断线的情况
        lockFlag = false;
        lockNodeContent = SystemUtil.UNKOWN_IP;
        // 重新去尝试获取锁
        tryAcquireLock();
      }
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    init();
  }

  public void setLockNodeName(String lockNodeName) {
    Assert.notNull("Lock node name can not be null");
    this.lockNodeName = lockNodeName;
  }

  public String getZkConnectionString() {
    return zkConnectionString;
  }

  public void setZkConnectionString(String zkConnectionString) {
    this.zkConnectionString = zkConnectionString;
  }

  public int getSessionTimeOut() {
    return sessionTimeOut;
  }

  public void setSessionTimeOut(int sessionTimeOut) {
    this.sessionTimeOut = sessionTimeOut;
  }

  public String getLockNodeName() {
    return lockNodeName;
  }

  public String getBaseNodePath() {
    return baseNodePath;
  }

  public void setBaseNodePath(String baseNodePath) {
    this.baseNodePath = baseNodePath;
  }

  public void setRetryPolicy(RetryPolicy retryPolicy) {
    Assert.notNull(retryPolicy);
    this.retryPolicy = retryPolicy;
  }

  /**
   * 设置最新的Lock节点的值.
   */
  private void setLockNodeContent(String lockNodeContent) {
    this.lockNodeContent = lockNodeContent;
  }

  public String getLockNodePath() {
    return this.lockNodePath;
  }
}
