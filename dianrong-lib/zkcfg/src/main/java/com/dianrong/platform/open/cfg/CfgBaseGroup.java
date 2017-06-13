package com.dianrong.platform.open.cfg;

import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;


public class CfgBaseGroup extends ConcurrentHashMap<String, String>
    implements PathChildrenCacheListener {

  private static final long serialVersionUID = -3595850223933919899L;
  private static Logger logger = LoggerFactory.getLogger(CfgBaseGroup.class);
  private static final int MAX_RETRIES = 10; // retry 10 times
  private static final int BASE_SLEEP_TIMES = 1000; // base sleep 1 sec
  private static final int CONNECTION_TIMEOUT = 3000;


  public static final String NULL_VALUE_IN_ZK = "null";
  public static final String PATH_DELIMITER = "/";

  private static final String DEFAULT_CONNECTION_INJECT_URL = "${DR_CFG_ZOOKEEPER_ENV_URL}";
  private final Charset UTF8 = Charset.forName("UTF-8");

  @Value("${DR_CFG_ZOOKEEPER_ENV_URL}")
  protected String connectString;

  protected String cfgRootNodeName;

  protected String location;

  protected boolean needKeepAlive;

  protected PathChildrenCache cache;

  public Set<CfgEventListener> listeners = Sets.newConcurrentHashSet();

  private int maxRetries = MAX_RETRIES;

  private int baseSleepTimes = BASE_SLEEP_TIMES;

  private int connectionTimeout = CONNECTION_TIMEOUT;

  protected transient CuratorFramework client;

  private AtomicBoolean isInitialized = new AtomicBoolean(false);

  @PostConstruct
  protected void init() {
    if (isInitialized.compareAndSet(false, true)) {
      Preconditions.checkArgument(cfgRootNodeName != null && !cfgRootNodeName.trim().isEmpty());
      initialClient();
      loadDataFromCache();
      overridePartialCfgs(location);
      if (!needKeepAlive) {
        closeConection();
      }
      logger.info(
          "CfgGroup Initial Success, Cfg Size Is : {}, Cfg Root Node Name Is : {}, data is : {}",
          size(), cfgRootNodeName, super.toString());
    }
  }

  private void initialClient() {
    if (StringUtils.isEmpty(connectString) || connectString.equals(DEFAULT_CONNECTION_INJECT_URL)) {
      throw new RuntimeException("Missing Connection String.");
    }
    try {
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimes, maxRetries);
      client = CuratorFrameworkFactory.builder().connectString(connectString)
          .retryPolicy(retryPolicy).connectionTimeoutMs(connectionTimeout).build();
      client.start();

      cache = new PathChildrenCache(client, cfgRootNodeName, true);
      // Executor executor = Executors.newFixedThreadPool(cfgCacheThreadNum);
      // cache.getListenable().addListener(this, executor);
      // using same thread to execute listener
      cache.getListenable().addListener(this);

      // start(true) will refresh the cache from zookeeper, blocking call
      // data will be loaded into cache
      cache.start(StartMode.BUILD_INITIAL_CACHE);
    } catch (Exception e) {
      logger.error("CfgGroup Initial Exception, exception = {}", e);
    }
  }

  private void overridePartialCfgs(String location) {
    Properties properties = new Properties();
    try {
      properties.load(Resources.getResource(location).openStream());
    } catch (Exception e) {
      logger.warn("CfgGroup Properties Does Not Initial Successfully,Location Is Unaviliable.");
    }
    if (properties != null) {
      Enumeration<Object> keys = properties.keys();
      while (keys.hasMoreElements()) {
        Object element = keys.nextElement();
        super.put(String.valueOf(element), properties.getProperty(String.valueOf(element)));
      }
    }
  }

  private void loadDataFromCache() {
    logger.debug("load from cache " + this.cfgRootNodeName);

    List<ChildData> children = cache.getCurrentData();
    for (ChildData child : children) {
      if (child.getData() == null) {
        continue;
      }
      String path = child.getPath();
      String key = formatFullPathToKey(path);
      String value = new String(child.getData(), UTF8);
      super.put(key, value);
    }
  }

  public static String formatFullPathToKey(String fullPath) {
    return fullPath.substring(fullPath.lastIndexOf(PATH_DELIMITER) + 1, fullPath.length());
  }

  @Override
  public void childEvent(CuratorFramework aclient, PathChildrenCacheEvent event) throws Exception {
    Type eventType = event.getType();
    ChildData data = event.getData();
    if (data != null) {
      String path = data.getPath();
      // scrub configRootPath out of the key name
      String key = formatFullPathToKey(path);
      byte[] value = data.getData();
      String stringValue = new String(value, UTF8);
      if (eventType == Type.CHILD_UPDATED) {
        logger.debug("received update to pathName [{}], key [{}] value [{}]", path, key,
            stringValue);
        super.put(key, stringValue);
        notifyAllListeners(CfgEvent.UPDATE, key, stringValue);
      } else if (eventType == Type.CHILD_REMOVED) {
        logger.debug("received remove to pathName [{}], key [{}]", path, key);
        super.remove(key);
        notifyAllListeners(CfgEvent.DELETE, key, stringValue);
      } else if (eventType == Type.CHILD_ADDED) {
        logger.debug("received add to pathName [{}], key [{}]", path, key);
        super.put(key, stringValue);
        notifyAllListeners(CfgEvent.ADD, key, stringValue);
      }
    }
  }

  private void notifyAllListeners(CfgEvent event, String key, String value) {
    Iterator<CfgEventListener> iterator = listeners.iterator();
    while (iterator.hasNext()) {
      CfgEventListener listener = iterator.next();
      try {
        listener.onEvent(event, key, value);
      } catch (Exception e) {
        // Should never happen. Ensure every listener to be notified.
        logger.error("CfgGroup Listener Notify Exception　: {}", e);
      }
    }
  }


  @PreDestroy
  private void closeConection() {
    if (client != null) {
      try {
        // Give some time to the framework for multi-threads` receive and handle the "close
        // event", so that we could avoid a large number of IllegalStateExceptions.
        Thread.sleep(100L);
        client.close();
      } catch (Exception e) {
        logger.warn("CfgGroup Close Error Exception : {}", e);
      } finally {
        client = null;
      }
      try {
        cache.close();
      } catch (Exception e) {
        logger.warn("CfgGroup Close Error Exception : {}", e);
      } finally {
        cache = null;
      }
    }
  }

  public void setCfgRootNodeName(String cfgRootNodeName) {
    this.cfgRootNodeName = cfgRootNodeName;
  }

  public void setConnectString(String connectString) {
    this.connectString = connectString;
  }

  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setNeedKeepAlive(boolean needKeepAlive) {
    this.needKeepAlive = needKeepAlive;
  }

  public void setListeners(Set<CfgEventListener> listeners) {
    this.listeners = listeners;
  }

}
